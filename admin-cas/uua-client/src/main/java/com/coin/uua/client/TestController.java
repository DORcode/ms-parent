package com.coin.uua.client;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName TestController
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-30 19:30
 * @Version V1.0
 **/
@RestController
@RequestMapping
public class TestController {

    @GetMapping("currentUser")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Object currentUser(HttpServletRequest request, Authentication user) {
        // AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
}
