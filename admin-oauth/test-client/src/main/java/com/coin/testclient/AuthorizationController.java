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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Joe Grandja
 */
@Controller
@RequestMapping("/")
public class AuthorizationController {
    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;

    @Autowired
    @Qualifier("messagingClientAuthCodeRestTemplate")
    private OAuth2RestTemplate messagingClientAuthCodeRestTemplate;

    @Autowired
    @Qualifier("messagingClientClientCredsRestTemplate")
    private OAuth2RestTemplate messagingClientClientCredsRestTemplate;

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("test")
    @ResponseBody
    public String text() {
        return "test";
    }

    @RequestMapping(value = "authorized", params = {"grant_type=authorization_code"})
    @ResponseBody
    public String authorized(HttpServletRequest request) {
        // messagingClientClientCredsRestTemplate.getForObject()
        return "authorized";
    }

    @RequestMapping(value = "authorized", params = {"grant_type=client_credentials"})
    @ResponseBody
    public String authorized2(HttpServletRequest request) {
        // messagingClientClientCredsRestTemplate.postForObject(null, String[].class)
        return "authorized";
    }

    @Qualifier("restTemplate")
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/test.html")
    public String hello(String code, Model model, HttpSession session) {
        if (code != null) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("client_id", "ms-client");
            map.add("client_secret", "aa");
            map.add("redirect_uri", "http://localhost:8280/test.html");
            map.add("grant_type", "authorization_code");
            Map resp = restTemplate.postForObject("http://localhost:8090/oauth/token", map, Map.class);
            System.out.println("resp = " + resp);
            String access_token = (String) resp.get("access_token");

            System.out.println(access_token);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + access_token);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8091/admin/hello", HttpMethod.GET, httpEntity, String.class);
            model.addAttribute("msg", entity.getBody());
            // model.addAttribute("refresh_token", )
        }
        return "test";
    }
}