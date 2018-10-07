package com.sliit.ssd.doublesubmitcookie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sliit.ssd.doublesubmitcookie.model.SessionStore;

@SpringBootApplication
public class DoubleSubmitCookieApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoubleSubmitCookieApplication.class, args);
		new SessionStore().assignUserCredentials();
	}
}
