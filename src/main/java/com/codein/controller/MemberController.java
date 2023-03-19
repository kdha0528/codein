package com.codein.controller;


import com.codein.request.PageSize;
import com.codein.request.Signup;
import com.codein.response.MemberResponse;
import com.codein.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid Signup signup) {
        signup.validate();
        memberService.signup(signup);
    }

    @GetMapping("/")
    public List<MemberResponse> getMemberList(@ModelAttribute PageSize pageSize) {
        return memberService.getMemberList(pageSize);
    }
}
