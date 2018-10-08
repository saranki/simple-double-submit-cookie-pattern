/**
 * On load methods
 * Get the csrf token from getCookie method and attach the token into a 
 * hidden input field in the form
 * Check the status of the state changing operation and 
 * provide the alert messages accordingly.
 * 
 * */

window.addEventListener('load', function() {
	let csrf = getCookie("csrf");
	$("#btn-submit").before(
			'<input type="hidden" name="csrf" value="' + csrf + '">');
	checkStatus();
});

/**
 * Get the csrf token from the cookie
 * 
 * */
function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

/**
 * Check the status in url and display success or failure alert messages based
 * on that
 * 
 */
function checkStatus() {
	if (getParameterByName("status")
			&& getParameterByName("status") === "failed") {
		bootbox.alert({
			message : "The transaction was not completed!"
		});
	} else if (getParameterByName("status")
			&& getParameterByName("status") === "success") {
		bootbox.alert({
			message : "The transaction was completed successfully!!!"
		});
	}
}

/**
 * Split the url and get the status of the transaction displayed in the url.
 * 
 * **/
function getParameterByName(name, url) {
	if (!url)
		url = window.location.href;
	name = name.replace(/[\[\]]/g, '\\$&');
	var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'), results = regex
			.exec(url);
	if (!results)
		return null;
	if (!results[2])
		return '';
	return decodeURIComponent(results[2].replace(/\+/g, ' '));
}