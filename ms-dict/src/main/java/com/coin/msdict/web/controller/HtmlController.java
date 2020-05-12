package com.coin.msdict.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName HtmlController
 * @Description: TODO
 * @Author kh
 * @Date 2020/5/7 17:23
 * @Version V1.0
 **/
@Controller
public class HtmlController {

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "开始");
        return "index";
    }
}
