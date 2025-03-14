package com.example.msaboardproject.controller;

import com.example.msaboardproject.model.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController {
    @GetMapping("/")
    public String boardList(){
        return "board-list";
    }
    @GetMapping("/write")
    public String boardWrite(){
        return "board-write";
    }

    @GetMapping("/detail")
    public String boardDetail(
            @RequestParam("id")long id,
            Model model
    ){
        model.addAttribute("id", id);
        return "board-detail";
    }

    @GetMapping("/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, Model model){

        model.addAttribute("id",id);
        return "board-update";
    }
}
