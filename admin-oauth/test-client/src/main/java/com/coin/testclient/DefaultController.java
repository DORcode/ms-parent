/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coin.testclient;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Joe Grandja
 */
@Controller
public class DefaultController {

	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return login();
	}

	@GetMapping("/api/user")
	@ResponseBody
	public Authentication user(Authentication user) {
		return user;
	}

	@GetMapping("callback")
	public void callback(Model mv, Authentication user, HttpServletResponse response) throws IOException {
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) user.getDetails();
		if(user.isAuthenticated()) {
			String address = String.format("http://localhost:3002/test?access_token=%s&token_type=%s&sessionId=%s",
					details.getTokenValue(), details.getTokenType(), details.getSessionId());
			response.sendRedirect(address);
		} else {
			// TokenEndpointAuthenticationFilter
			// AuthorizationServerTokenServices
			// SavedRequestAwareAuthenticationSuccessHandler
		}
//            mv.addAttribute("isLogin", true);
//        } else {
//            mv.addAttribute("isLogin", false);
//        }
//        return "/skip";
	}

}