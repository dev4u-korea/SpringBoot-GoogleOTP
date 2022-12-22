package com.demo.sample;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.lang.reflect.Member;

@RestController
public class MemberCtrl {

    @PostMapping(value="/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String postHaandlerForJsonRequest(@RequestBody Member member) {
        return member.toString();
    }

    @PostMapping(value="/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postHandlerForFormRequest(Member member) {

        return member.toString();
    }
}
