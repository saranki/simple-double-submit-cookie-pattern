/**
 * 
 */
package com.sliit.ssd.doublesubmitcookie.model;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Saranki
 *
 */
public class SessionStore {
	public static HashMap<String, User> userStore = new HashMap<>();
	// private static HashMap<String, String> sessionTokenStore = new
	// HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(SessionStore.class);

	public void assignUserCredentials() {
		User user = new User();
		user.setUsername("master");
		user.setPassword("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9");
		userStore.put(user.getUsername(), user);
		logger.info("Initial user credentials assigned...");
	}

	public void storeUsersAndTokens(User user) {
		userStore.put(user.getUsername(), user);
		logger.debug("user object from map from main clas -> " + userStore.get(user.getUsername()).toString());
		// if (user.getToken() != null) {
		// sessionTokenStore.put(user.getSessionId(), user.getToken());
		// }
	}

	public User getCredentials(String username) {
		return userStore.get(username);
	}

	// public String getCsrfTokenForSession(String sessionId) {
	// return sessionTokenStore.get(sessionId);
	// }

}
