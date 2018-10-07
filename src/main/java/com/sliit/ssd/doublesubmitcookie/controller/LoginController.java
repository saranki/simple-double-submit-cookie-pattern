/**
 * 
 */
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

	@GetMapping("/")
	public String loadPage(Model model, HttpServletRequest request) {
		if (!authenticationService.isUserAuthenticated(request.getCookies())) {
			return "redirect:/login";
		} else {
			return "redirect:/transfer";
		}
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", new User());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute User user, HttpServletResponse servletResponse, Model model) {

		String username = user.getUsername();
		String password = user.getPassword();
		try {
			logger.info("Authenticating User...");
			if (authenticationService.isValidUser(username, password)) {
				String sessionID = authenticationService.generateCredentialsToUser(username);
				String csrfToken = authenticationService.generateCSRFToken(sessionID);
				
				// Set it in the set-cookie header
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
