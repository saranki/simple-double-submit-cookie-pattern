package com.sliit.ssd.doublesubmitcookie.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sliit.ssd.doublesubmitcookie.model.Transfer;
import com.sliit.ssd.doublesubmitcookie.service.AuthenticationService;

/**
 * @author Saranki
 *
 */

@Controller
public class TransferController {
	
	@Autowired
	AuthenticationService authenticationService;

	private static Logger logger = LoggerFactory.getLogger(TransferController.class);

	/**
	 * Binding the transfer object attributes to Thymleaf
	 * 
	 * @param model
	 * @return transfer page
	 */
	@GetMapping("/transfer")
	public String showTransferPage(Model model) {
		logger.info("calling GET transfer...");
		model.addAttribute("transfer", new Transfer());
		return "transfer";
	}
	
	
	/**
	 * If the user is authenticated successfully and the csrf token is valid the
	 * state changing transaction will get executed successfully.
	 * 
	 * @param transfer
	 * @param request
	 * @return
	 */
	@PostMapping("/transfer")
	public String transferFunds(@ModelAttribute Transfer transfer, HttpServletRequest request) {
		logger.debug("calling POST transfer...");

		if (authenticationService.isAuthenticated(request.getCookies(), transfer.getCsrf())) {
			logger.debug("Token validated successfully...");
			return "redirect:/transfer?status=success";
		} 
		logger.debug("failed transfering");
		return "redirect:/transfer?status=failed";
	}
}
