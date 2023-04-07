package com.codein.controller;

import com.codein.requestdto.PostingDto;
import com.codein.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/writepost")
    public String writePost(@RequestBody @Valid PostingDto postingDto, @CookieValue(value = "accesstoken") Cookie cookie) {
        postService.post(postingDto.toPostingServiceDto(), cookie.getValue());
        return "redirect:/home";
    }
}
