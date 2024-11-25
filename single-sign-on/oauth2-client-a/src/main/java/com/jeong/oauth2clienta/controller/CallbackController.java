package com.jeong.oauth2clienta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CallbackController {

    @GetMapping("/callback")
    public void getCallback(){

    }

}
