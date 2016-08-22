package com.tulgaa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String index() {
		return "index.html";
	}

	
	
/*	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(1*60);
		
		ModelAndView model = new ModelAndView();
		if (error != null) {
		//	model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
			return "false";
		}
		else{
			return "true";
		}
		
	}*/
}
