package com.library.digitallibrary.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardWebController {

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/index";
    }

    // optional: visiting "/" could redirect to admin for admin users
    // keep/remove as you want
    // @GetMapping("/")
    // public String root() { return "redirect:/admin"; }
}