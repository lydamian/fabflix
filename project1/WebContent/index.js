var previous_suggestions = {};

function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	
	if (query in previous_suggestions)
	{
		console.log("sending cache results");
		doneCallback( { suggestions: JSON.parse(previous_suggestions[query])} );
		return;
		//handleLookupAjaxSuccess(previous_suggestions[query], query, doneCallback) ;
	}
	else
	{
		console.log("Nothing in cache, searching database...");
		console.log("sending AJAX request to backend Java Servlet")
		// TODO: if you want to check past query results first, you can do it here
		
		// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
		// with the query data
		jQuery.ajax({
			"method": "GET",
			// generate the request url from the query.
			// escape the query string to avoid errors caused by special characters 
			"url": "movie-suggestion?query=" + escape(query),
			"success": function(data) {
				// pass the data, query, and doneCallback function into the success handler
				handleLookupAjaxSuccess(data, query, doneCallback) 
			},
			"error": function(errorData) {
				console.log("lookup ajax error")
				console.log(errorData)
			}
		})
	}
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	previous_suggestions[query] = data;
	doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["data"]["title"])
	var url = "single-movie.html" + "?id=" + suggestion["data"]["id"]
	console.log(url)
	window.location.replace(url);
}

$('#normal_search_autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    minChars: 3,
    // set the groupby name in the response json data field
    groupBy: "category",
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});

/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
}

// bind pressing enter key to a handler function
$('#normal_search_autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#normal_search_autocomplete').val())
	}
})

//when user clicks search button
function handleSearchResult(resultDataString)
{
	 window.location.replace("results.html");
	 console.log(resultDataString);
	 sessionStorage.setItem('resultSearchObj', JSON.stringify(resultDataString));
}

function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();

    jQuery.get(
        "search",
        // Serialize the login form to the data sent by POST request
        jQuery("#autocomplete_form").serialize() + "&year=#&date=#&star=#",
        (resultDataString) => handleSearchResult(resultDataString));

}

// Bind the submit action of the form to a handler function
jQuery("#autocomplete_form").submit((event) => submitSearchForm(event));