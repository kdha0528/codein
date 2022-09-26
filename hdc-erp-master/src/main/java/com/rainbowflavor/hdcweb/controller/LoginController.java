package com.rainbowflavor.hdcweb.controller;

import com.rainbowflavor.hdcweb.dto.SignupDto;
import com.rainbowflavor.hdcweb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;

    @GetMapping(value="/login")
    public String loginPage(){
        log.info("login controller = /login");
        return "page/signin";
    }


    @PostMapping(value="/signup")
    public String signUp(@ModelAttribute SignupDto signupDto){
        log.info("signup controller = {}", signupDto);

        userService.joinUser(signupDto);
        return "page/mail-auth";
    }

    @GetMapping("/mail-auth")
    public String mailAuth() {
        return "page/mail-auth";
    }

    @GetMapping("/confirm-signup")
    public String confirmSignupToken(@RequestParam String token){
        log.info("confirm signup token controller = {}", token);

        userService.confirmEmail(token);
        return "page/mail-auth-dest";
    }
}

