package com.sliit.ssd.doublesubmitcookie.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sliit.ssd.doublesubmitcookie.model.SessionStore;
import com.sliit.ssd.doublesubmitcookie.model.User;
import com.sliit.ssd.doublesubmitcookie.service.AuthenticationService;
import com.sliit.ssd.doublesubmitcookie.util.CredentialsConfig;
import com.sliit.ssd.doublesubmitcookie.util.HashUtil;

/**
 * @author Saranki
 *
 */

@Service
public class AuthenticationService {
	
	@Autowired
	CredentialsConfig credentialConfig;

	@Autowired
	HashUtil hashUtil;
	private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	SessionStore sessionStore = new SessionStore();
	
	/**
	 * Check if the credentials entered by the user in the Login form
	 * are same as the user credentials stored in the HashMap.
	 * Compares the username and the hash value of the typed in
	 * password.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isValidUser(String username, String password) throws NoSuchAlgorithmException {
		logger.info("User Validation...");
		return (username.equals(credentialConfig.getAuthUser())
				&& hashUtil.convertToHash(password).equals(credentialConfig.getPassword()));
	}
	
	/**
	 * Check if the user is already authenticated using the cookies that came along with the request.
	 * If so then extract the session Id and the username from the cookies.
	 * 
	 * @param cookies
	 * @return 
	 */
	public boolean isUserAuthenticated(Cookie[] cookies) {
		logger.debug("isAuthenticated? " + cookies);
		String username = "";
		String sessionID = "";

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("sessionID")) {
					sessionID = cookie.getValue();
				} else if (cookie.getName().equals("username")) {
					username = cookie.getValue();
				}
			}
		}
		return isUserSessionValid(username, sessionID);
	}

	/**
	 * If the user is already authenticated check if the form token and the token in the cookiestore are the same
	 * 
	 * @param cookies, csrfToken
	 * @return
	 */
	public boolean isAuthenticated(Cookie[] cookies, String csrfToken) {
		logger.debug("isAuthenticated? " + cookies.length + ", " + csrfToken);
		Map<String, String> cookieStore = new HashMap<>();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				cookieStore.put(cookie.getName(), cookie.getValue());
			}
		}
		logger.debug("Cookie store size , " + cookieStore.size());

		if (isUserSessionValid(cookieStore.get("username"), cookieStore.get("sessionID"))
				&& isCSRFTokenValid(cookieStore.get("csrf"), csrfToken)) {
			logger.info("Token validated...");
			return true;
		}
		return false;
	}
	
	/**
	 * Generate a random value for session Id
	 * 
	 * @return
	 * 
	 * **/
	public String generateSessionId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Check if the current session Id which was extracted from the cookie 
	 * and the already stored session Id in the HashMap are the same.
	 * 
	 * @param username
	 * @param sessionId
	 * @return
	 */
	public boolean isUserSessionValid(String username, String sessionId) {
		logger.debug("Checking if user session is valid... " + username + ", " + sessionId);
		if (sessionStore.getCredentials(username) != null) {
			logger.debug("AAA " + sessionStore.getCredentials(username));
			return sessionId.equals(sessionStore.getCredentials(username).getSessionId());
		}
		return false;
	}

	/**
	 * Generate a random value for session Id
	 * 
	 * @return
	 * 
	 * **/
	public String generateCSRFToken(String sessionId) {
		return sessionId + System.currentTimeMillis();
	}
	
	/**
	 * Store the session Id HashMap for the particular user
	 * 
	 * @param username
	 * @return
	 * */
	public String generateCredentialsToUser(String username) {
		User user = sessionStore.getCredentials(username);
		logger.debug("user object from map -> " + user.toString());
		
		String sessionId = generateSessionId();
		user.setSessionId(sessionId);
		
		sessionStore.storeUsersAndTokens(user);

		logger.info("Storing session...");
		return sessionId;
	}

	/**
	 * Check if the form token and the token in the cookiestore are the same
	 * 
	 * @param cookieToken
	 * @param formToken
	 * @return
	 * */
	public boolean isCSRFTokenValid(String cookieToken, String formToken) {
		return (cookieToken.equals(formToken));
	}

	/**
	 * Get the sessionId from the cookie that comes along with a request
	 * 
	 * @param cookies
	 * @return
	 * 
	 */
	public String getSessionIdFromCookie(Cookie[] cookies) {
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("sessionID")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the csrf token from the cookie that comes along with a request
	 * 
	 * @param cookies
	 * @return
	 * 
	 */
	public String getCSRFTokenFromCookie(Cookie[] cookies) {
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("csrf")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
