package com.beryste.eventz.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("v1/api")
public class EventController {

    @GetMapping
    public String hello() {
        return "Hello server";
    }
}
