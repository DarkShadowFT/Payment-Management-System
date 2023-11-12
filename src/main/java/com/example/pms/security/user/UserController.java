package com.example.pms.security.user;

import com.example.pms.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignupForm(SignupForm signupForm) {
        boolean isExistingUser = customUserDetailsService.userExists(signupForm.getUsername());
        if (isExistingUser) {
            return "redirect:/signup?error";
        }

        UserDetails userDetails = User.withUsername(signupForm.getUsername())
                .password(passwordEncoder().encode(signupForm.getPassword()))
                .roles("USER")
                .build();

        customUserDetailsService.createUser(userDetails);
        return "redirect:/login";
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
