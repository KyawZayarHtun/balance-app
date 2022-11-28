package com.example.controller;

import com.example.controller.utils.Pagination;
import com.example.model.service.UserAccessLogService;
import com.example.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("user/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccessLogService logService;

    @GetMapping
    public String index(ModelMap model,
                        @RequestParam Optional<Integer> page,
                        @RequestParam Optional<Integer> size) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var userVO = userService.findByLoginId(username);
        model.put("user", userVO);
        var accessLogs = logService.search(username, page, size);
        model.put("list", accessLogs.getContent());

        var pagination = Pagination.builder("/user/profile")
                .page(accessLogs)
                .build();
        model.put("page", pagination);
        return "profile";
    }

    @PostMapping("contact")
    String updateContact(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateContact(username, email, phone);
        return "redirect:/user/profile";
    }

}
