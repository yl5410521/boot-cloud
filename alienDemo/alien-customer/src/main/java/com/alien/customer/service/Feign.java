package com.alien.customer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(value = "provider-server",path = "provider",name = "provider-server")
public interface Feign {
	@RequestMapping(value = "/test/{id}",method = RequestMethod.GET)
    public String test(@PathVariable ("id") String id);

}
