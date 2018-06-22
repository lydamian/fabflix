/**
 * insert_new_star.js
 */

console.log("insert_new_star.js page called...");

function handleSearchResult(resultDataString)
{
	console.log("handleSearchResult in insert_new_star.js called...");
	var successResponse = document.getElementById("success_insertion_of_star");
	successResponse.textContent = "Successful addition of star";
	/*
	var parsedResult = JSON.parse(resultDataString);
	console.log(resultDataString);

    // If login success, redirect to index.html page
    if (resultDataJson["status"] === "success") {
        alert("your star has successfully been added");
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {

        console.log("show error message");
        console.log(resultDataJson["message"]);
        alert("error updating star");
    }
    */
}

function submitSearchForm(formSubmitEvent)
{
	formSubmitEvent.preventDefault();

    jQuery.get(
        "InsertStarServlet",
        // Serialize the login form to the data sent by POST request
        jQuery("#add_star_form").serialize(),
        (resultDataString) => handleSearchResult(resultDataString));
}

console.log("search.js page called...");

/* should check if the user entered a starName*, else error */


function validateForm(){
		var successResponse = document.getElementById("success_insertion_of_star");
		successResponse.textContent = "";
    	var x = document.getElementById("star_name").value
        if (x == ""){
    		alert("This field cannot be left blank");
        }
        else{
        	jQuery("#add_star_form").submit((event) => submitSearchForm(event));
            
        }
        return false;
}



//jQuery("#add_star_form").submit((event) => submitSearchForm(event));