/**
 * 
 */

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    var resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login success, redirect to index.html page
    if (resultDataJson["status"] === "success") {
    	alert("SUCCESS! YOUR ORDER WILL BE SHIPPED IN 10000 YEARS!");
        window.location.replace("confirmation.html");
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {
        jQuery("#login_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitCheckoutForm(formSubmitEvent) {
    console.log("checkout submit login form...");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    var shoppingCart = sessionStorage.getItem('shopping-cart-obj');
	var shoppingCartJS = JSON.parse(shoppingCart);
    var movieIds = [];
    for (let i = 0; i < shoppingCartJS.length; i++){
    	movieIds.push(shoppingCartJS[i]["movieId"]);
    }
    console.log(movieIds);
    jQuery.post(
        "CheckoutServlet",
        // Serialize the login form to the data sent by POST request
        jQuery("#checkout-form").serialize() + '&movieIds=' + movieIds,
        (resultDataString) => handleLoginResult(resultDataString));

}

// Bind the submit action of the form to a handler function
jQuery("#checkout-form").submit((event) => submitCheckoutForm(event));







/* ----------------------------------------------- SEPERATION OF CONCERN/TASKS --------------------------*/









function updateQuantity(movieId)
{
	console.log("change quantity js called");
	var e = document.getElementById('\'' + movieId + '\'');
	var quantityChosen = e.options[e.selectedIndex].value;
	
	var current = sessionStorage.getItem('shopping-cart-obj');
	var currentJS = JSON.parse(current);
	for (let i = 0; i < currentJS.length; i++) {
		if (currentJS[i]["movieId"] == movieId)
		{
			currentJS[i]["quantity"] = quantityChosen;
		}
	}
	console.log(currentJS);
	sessionStorage.setItem('shopping-cart-obj', JSON.stringify(currentJS));
}

function handleSearchResult()
{
	var resultDataJS = sessionStorage.getItem('shopping-cart-obj');
    var resultData = JSON.parse(resultDataJS);
	let movieCartTableBodyElement = jQuery("#checkout-cart");
    // Iterate through resultData
    for (let i = 0; i < resultData.length; i++) {
    	//console.log(resultData.length);
        // Concatenate the html tags with resultData jsonObject
    	actualQuantity = resultData[i]["quantity"];
        let rowHTML = "";
        rowHTML += "<tr>";
        
        rowHTML += "<th>" + resultData[i]["movieId"] + "</th>";
        
        rowHTML += "<th>" + resultData[i]["movieTitle"] + "</th>";
        
        rowHTML += "<th>" + resultData[i]["quantity"] + "</th";
        
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieCartTableBodyElement.append(rowHTML); 
        
    }
}

handleSearchResult();





