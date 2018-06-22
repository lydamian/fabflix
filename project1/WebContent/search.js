function handleSearchResult(resultDataString)
{
	//resultDataJson = JSON.parse(resultDataString);
	 window.location.replace("results.html");
	 console.log(resultDataString);
	 sessionStorage.setItem('resultSearchObj', JSON.stringify(resultDataString));
}

function submitSearchForm(formSubmitEvent)
{
	formSubmitEvent.preventDefault();

    jQuery.get(
        "search",
        // Serialize the login form to the data sent by POST request
        jQuery("#search_form").serialize(),
        (resultDataString) => handleSearchResult(resultDataString));
}

console.log("search.js page called...");

jQuery("#search_form").submit((event) => submitSearchForm(event));