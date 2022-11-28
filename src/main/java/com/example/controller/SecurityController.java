package com.example.controller;

import com.example.model.domain.entity.User;
import com.example.model.domain.form.ChangePasswordForm;
import com.example.model.domain.form.SignUpForm;
import com.example.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class SecurityController {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null &&
                auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(User.Role.ADMIN.name())
                                || a.getAuthority().equals(User.Role.MEMBER.name()))) {
            return "redirect:/user/home";
        }
        return "signin";
    }

    @GetMapping("signup")
    public void loadSignUn() {
    }

    @PostMapping("signup")
    public String signUn(@Valid @ModelAttribute(name = "form") SignUpForm signUpForm,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }

        userService.signUp(signUpForm);
        return "redirect:/";
    }

    @PostMapping("user/changepass")
    public String changePass(@ModelAttribute ChangePasswordForm form,
                             RedirectAttributes redirect) {
        userService.changePassword(form);
        redirect.addFlashAttribute("message", "Your Password has Been Change");
        return "redirect:/";
    }

    @ModelAttribute(name = "form")
    SignUpForm signUpForm() {
        return new SignUpForm();
    }

}
