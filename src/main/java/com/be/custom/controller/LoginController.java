package com.be.custom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping(value = "/")
	public String index() {
		return "redirect:swagger-ui.html";
	}
}
