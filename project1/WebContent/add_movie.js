/**
 * add_movie.js
 */

console.log("add_movie.js page called...");

function handleSearchResult(resultDataString)
{
	console.log("handleSearchResult in insert_movie.js called...");
	var resultDataJson = JSON.parse(resultDataString);
	if(resultDataJson["status"] === "success"){
		alert("Movie Succesfully Added");
	}else{
		alert("Duplicate found, unable to add movie");
	}

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
	console.log("submitSearchForm in add_movie.js function called...");
	formSubmitEvent.preventDefault();

    jQuery.get(
        "AddMovieServlet",
        // Serialize the login form to the data sent by get request
        jQuery("#add_movie_form").serialize(),
        (resultDataString) => handleSearchResult(resultDataString));
}

/* should check if user entered all values on form if not, error */

jQuery("#add_movie_form").submit((event) => submitSearchForm(event));