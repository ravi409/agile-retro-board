package com.quickthinker.agile.retro.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetroController {
	
	@RequestMapping(value="/hello", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public String test(){
		return "Hello World";
	}

}
