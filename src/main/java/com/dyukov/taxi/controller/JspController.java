package com.dyukov.taxi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JspController {

    @GetMapping("/userLogin")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "userLogin";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "redirect:/login.html";
    }

}
