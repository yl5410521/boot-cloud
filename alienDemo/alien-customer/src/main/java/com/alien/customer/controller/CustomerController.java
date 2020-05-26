package com.alien.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.alien.customer.service.Feign;

@RestController
public class CustomerController {
	@Autowired
	private Feign feign;

	@GetMapping("/test/{id}")
	public String test(@PathVariable ("id") String id) {
		System.out.println("客户：他来了"+id);

		return feign.test(id);
	}

}
