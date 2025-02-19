package com.be.custom.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckHealthyAPI {

    @GetMapping("/check-healthy")
    public void checkHealthyAPI() {
    }
}
