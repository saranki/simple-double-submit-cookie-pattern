package com.sliit.ssd.doublesubmitcookie.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sliit.ssd.doublesubmitcookie.model.Transfer;
import com.sliit.ssd.doublesubmitcookie.model.User;
import com.sliit.ssd.doublesubmitcookie.service.AuthenticationService;
import com.sliit.ssd.doublesubmitcookie.util.HashUtil;

/**
 * @author Saranki
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	HashUtil hashUtil;

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	/**
	 * If the user is authenticated he will be redirected to transfer page of
	 * not will be redirected to login page
	 * 
	 * @param model
	 * @param request
	 * @return login or transfer page based on the condition
	 */
	@GetMapping("/")
	public String loadPage(Model model, HttpServletRequest request) {
		if (!authenticationService.isUserAuthenticated(request.getCookies())) {
			return "redirect:/login";
		} else {
			return "redirect:/transfer";
		}
	}
	
	/**
	 * Binding the user object attributes to Thymleaf
	 * 
	 * @param model
	 * @return login page
	 */
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", new User());
		return "login";
	}

	/**
	 * If the user is authenticated successfully 3 cookies will be generated.
	 * 1. Session cookie to store session details
	 * 2. User cookie to store user details for a particular session
	 * 3. CSRF token cookie in which csrf token is attached. This token will be 
	 * later embedded into the form as a hidden field which will be sent through a POST request 
	 * 
	 * @param user
	 * @param servletResponse
	 * @param model
	 * @return transfer page if conditions are fulfilled
	 */
	@PostMapping("/login")
	public String login(@ModelAttribute User user, HttpServletResponse servletResponse, Model model) {

		String username = user.getUsername();
		String password = user.getPassword();
		try {
			logger.info("Authenticating User...");
			if (authenticationService.isValidUser(username, password)) {
				String sessionID = authenticationService.generateCredentialsToUser(username);
				String csrfToken = authenticationService.generateCSRFToken(sessionID);
				
				Cookie sessionCookie = new Cookie("sessionID", sessionID);
				Cookie userCookie = new Cookie("username", username);
				Cookie tokenCookie = new Cookie("csrf", csrfToken);
				servletResponse.addCookie(sessionCookie);
				servletResponse.addCookie(userCookie);
				servletResponse.addCookie(tokenCookie);
			}
			logger.info("Logged in successfully");
			model.addAttribute("transfer", new Transfer());
			return "redirect:/transfer";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "redirect:/login?status=failed";
	}
}
