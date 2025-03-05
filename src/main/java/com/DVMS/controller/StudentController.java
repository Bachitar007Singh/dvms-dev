package com.DVMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    @GetMapping("/student/login")
    public String showStudentLoginForm() {
        return "student-login";
    }

    @PostMapping("/student/login")
    public String processStudentLogin(@RequestParam("email") String email,
                                      @RequestParam("rollNo") String rollNo,
                                      @RequestParam("password") String password,
                                      Model model) {
        // Implement your authentication logic here
        // For demonstration, let's assume a hardcoded check

        if ("student@example.com".equals(email) && "12345".equals(rollNo) && "password".equals(password)) {
            return "redirect:/leave-request"; // Redirect to leave form
        } else {
            model.addAttribute("error", "Invalid email, roll no, or password.");
            return "student-login";
        }
    }
}