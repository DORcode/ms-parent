The URL paths provided by the framework are /oauth/authorize (the authorization endpoint), 
/oauth/token (the token endpoint), 
/oauth/confirm_access (user posts approval for grants here), 
/oauth/error (used to render errors in the authorization server), 
/oauth/check_token (used by Resource Servers to decode access tokens), 
and /oauth/token_key (exposes public key for token verification if using JWT tokens).

客户端登录后，从服务端重定向后，/oauth/token一直不能成功获得token，加了context path解决

.and().logout()// 用户退出操作
.logoutRequestMatcher(new AntPathRequestMatcher("/logout","POST"))// 用户退出所访问的路径，需要使用Post方式
.permitAll().logoutSuccessUrl("/login?logout=true")


AuthenticationFilter -> AuthenticationManager AuthenticationProvider-> ProviderManager -> AuthenticationProvider(自定义来进行验证)

OAuth2Server Filter链

Security filter chain: [
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter
  HeaderWriterFilter
  LogoutFilter
  OAuth2AuthenticationProcessingFilter
  UsernamePasswordAuthenticationFilter
  DefaultLoginPageGeneratingFilter
  DefaultLogoutPageGeneratingFilter
  BasicAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  FilterSecurityInterceptor
]

OAuth2Client Filter链

Security filter chain: [
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter
  HeaderWriterFilter
  CsrfFilter
  LogoutFilter
  OAuth2AuthorizationRequestRedirectFilter
  OAuth2LoginAuthenticationFilter
  DefaultLoginPageGeneratingFilter
  DefaultLogoutPageGeneratingFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  FilterSecurityInterceptor
]

spring security cas

第一点是通过ServiceProperties指定CasAuthenticationFilter的authenticateAllArtifacts为true，
这样CasAuthenticationFilter将会尝试对所有ticket进行认证，而不是只认证来自filterProccessUrl指定地址的请求。
这样代理端在请求被代理端的资源时将proxy ticket以参数ticket进行传递时，CasAuthenticationFilter才会让CasAuthenticationProvider对proxy ticket进行校验，
这样我们的请求才有可能被CasAuthenticationProvider认证成功并请求到真正的资源。



第二点是指定CasAuthenticationFilter使用的AuthenticationDetailsSource为ServiceAuthenticationDetailsSource。
CasAuthenticationFilter默认使用的是WebAuthenticationDetailsSource。ServiceAuthenticationDetailsSource将构建一个ServiceAuthenticationDetails
对象作为当前Authentication的details对象。ServiceAuthenticationDetailsSource构建的ServiceAuthenticationDetails对象会将当前请求的地址构建为一个serviceUrl，
通过其getServiceUrl()方法可以获取到该serviceUrl地址。之后该Authentication对象传递到CasAuthenticationProvider进行认证时就可以从Authentication的details中获取到对应的serviceUrl，
并在通过Cas Server对代理端以参数ticket传递过来的proxy ticket进行验证时连同对应的serviceUrl一起传递过去。因为之前代理端申请proxy ticket时就是通过该serviceUrl进行申请的，
Cas Server需要对于它们的配对来验证对应的proxy ticket是否有效。



第三点是将CasAuthenticationProvider的TicketValidator由Cas20ServiceTicketValidator改为Cas20ProxyTicketValidator，
因为Cas Proxy被代理端需要调用Cas Server的proxyValidator对代理端传递过来的proxy ticket进行验证。
此外需要通过acceptAnyProxy或allowedProxyChains指定将接受哪些代理。acceptAnyProxy用以指定是否接受所有的代理，
可选值为true或false。allowedProxyChains则用以指定具体接受哪些代理，其对应的值是代理端在获取pgtId时提供给Cas Server的回调地址，
如我们需要接受前面示例中代理端的代理，则我们的allowedProxyChains的值应该是“https://elim:8043/app/proxyCallback”。
如果需要接受多个代理端的代理，则在指定allowedProxyChains时多个代理端回调地址应各占一行。



针对以上三点，我们的Spring Security应用整合Cas作为Cas Proxy的被代理端时需要对我们的配置进行如下改造。