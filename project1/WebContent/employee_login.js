
/**
 * Handle the data returned by EmployeeLoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
	console.log("handleLoginResult in employee_login.js function called...");
	console.log("result Data String from EmployeeLoginServlet is: " + resultDataString);
    resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson);
    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login success, redirect to index.html page
    if (resultDataJson["status"] === "success") {
    	console.log("login successful in employee_login.js");
        window.location.replace("employee_dashboard.html");
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {
    	console.log("login unsuccessful in employee_login.js");
        console.log("show error message");
        console.log(resultDataJson["message"]);
        alert(resultDataJson["message"]);
        jQuery("#login_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("employee submit login form");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();

    jQuery.post(
        "EmployeeLoginServlet",
        // Serialize the login form to the data sent by POST request
        jQuery("#employee_login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString));

}

// Bind the submit action of the form to a handler function
jQuery("#employee_login_form").submit((event) => submitLoginForm(event));

