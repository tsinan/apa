package com.x.apa.webportal.webpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liumeng
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String toIndexPage(){
		return "redirect:/webpage/apa/dashboard";
	}
}
