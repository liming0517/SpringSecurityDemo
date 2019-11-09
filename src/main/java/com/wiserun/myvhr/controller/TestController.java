package com.wiserun.myvhr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class TestController {
    @RequestMapping("/hello")
    public String test(Model model){
        model.addAttribute("hello","thymeleaf");
        return "index";
    }
}
