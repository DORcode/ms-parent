package com.coin.uua.client;

import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengJianSheng
 * @date 2019-02-11
 */
// @Slf4j
public class UaaUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

//    @Autowired
//    private UserService userService;

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        //        SysUser sysUser = userService.getByUsername(username);
//        if (null == sysUser) {
//            log.warn("用户{}不存在", username);
//            throw new UsernameNotFoundException(username);
//        }
//        List<SysPermission> permissionList = permissionService.findByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(permissionList)) {
//            for (SysPermission sysPermission : permissionList) {
//                authorityList.add(new SimpleGrantedAuthority(sysPermission.getCode()));
//            }
//        }
//
//        MyUser myUser = new MyUser(sysUser.getUsername(), passwordEncoder.encode(sysUser.getPassword()), authorityList);
//
//        log.info("登录成功！用户: {}", JSON.toJSONString(myUser));
        User user = new User("user", "user" , authorityList);
        return user;
    }
}
