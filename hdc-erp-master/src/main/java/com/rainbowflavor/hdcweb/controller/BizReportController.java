package com.rainbowflavor.hdcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BizReportController {

    @GetMapping(value="/biz-report")
    public String index(){
        return "contents/biz-report";
    }
}
