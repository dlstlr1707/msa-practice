package com.example.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
    @GetMapping("/join")
    public String join() {
        return "sign-up";
    }
    @GetMapping("/login")
    public String login() {
        return "sign-in";
    }
    @GetMapping("/logout")
    public String logoutExc(HttpSession session) {
        // model 안지워도 지워짐
        session.invalidate();
        return "redirect:sign-in";
    }
    @GetMapping("/weather")
    public String weather(
            HttpSession session,
            Model model
    ){
        String userName = (String)session.getAttribute("userName");
        if(userName != null) {
            model.addAttribute("userName", userName);
        }else{
            System.out.println("세션 없음");
        }
        return "weather";
    }
}
