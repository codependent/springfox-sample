package com.codependent.springfox;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class FooRestController {

	@ApiOperation(value="Get FOOs")
	@RequestMapping(value="/v1/foos", method=RequestMethod.GET)
	public String getFoo(){
		return "foo";
	}
	
}
