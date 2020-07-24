The URL paths provided by the framework are /oauth/authorize (the authorization endpoint), 
/oauth/token (the token endpoint), 
/oauth/confirm_access (user posts approval for grants here), 
/oauth/error (used to render errors in the authorization server), 
/oauth/check_token (used by Resource Servers to decode access tokens), 
and /oauth/token_key (exposes public key for token verification if using JWT tokens).

客户端登录后，从服务端重定向后，/oauth/token一直不能成功获得token，加了context path解决