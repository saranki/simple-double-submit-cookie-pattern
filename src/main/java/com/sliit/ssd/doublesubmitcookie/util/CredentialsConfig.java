/**
 * 
 */
package com.sliit.ssd.doublesubmitcookie.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Saranki
 *
 */
@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class CredentialsConfig {
	@Value("${authuser}")
    private String authuser;

    @Value("${authpassword}")
    private String password;

	public String getAuthUser() {
		return this.authuser;
	}

	public void setAuthUser(String username) {
		this.authuser = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
