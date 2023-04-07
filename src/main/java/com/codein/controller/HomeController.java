package com.codein.controller;

import com.codein.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {


    private final MemberService memberService;


}
