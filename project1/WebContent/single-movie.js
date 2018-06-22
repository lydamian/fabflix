/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
var globResults;
function getParameterByName(target) {
	console.log("Getting parameter id from url");
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    //console.log(decodeURIComponent(results[2].replace(/\+/g, " ")));
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleGenreResult(resultDataString)
{
	console.log("Success");
	//console.log(resultDataString);
	window.location.replace("results.html");
	sessionStorage.setItem('resultSearchObj', JSON.stringify(resultDataString));
}
function getGenre(thisGenre)
{
	console.log(thisGenre);
	$.ajax({
		type: "GET",
		url: "GenreSearchServlet",
		data: {genre: thisGenre},
		success: (resultDataString) => handleGenreResult(resultDataString)
	});
}





var testVar = sessionStorage.getItem('shopping-cart-obj');
var anotherVar = JSON.parse(testVar);
console.log(anotherVar);


/* ----------------------------------------------------------------------------- */
function openCart(thisId, thisMovie)
{
	console.log(thisId);
	console.log(thisMovie);
	/*window.location.replace('add-to-cart.html?id=' + thisId +'');*/
	/*add id to session */
	var oldShoppingCart = sessionStorage.getItem('shopping-cart-obj');
	var oldShoppingCartJS = JSON.parse(oldShoppingCart);
	if(oldShoppingCart == null){
		var cartArray = [];
		cartArray.push({ "movieId":thisId, "movieTitle":thisMovie, "quantity":1 });
		/*var JSONObj = {"ShoppingCart":[ "Ford", "BMW", "Fiat" ]}*/
		sessionStorage.setItem('shopping-cart-obj', JSON.stringify(cartArray));
	}
	else {
		var cartArray = [];
		for(let i = 0; i < oldShoppingCartJS.length; i++)
		{
			cartArray[i] = oldShoppingCartJS[i];
		}
		cartArray.push({ "movieId":thisId, "movieTitle":thisMovie, "quantity":1  });
		sessionStorage.setItem('shopping-cart-obj', JSON.stringify(cartArray));
	}
}	
/*------------------------------------------------------------------------------*/




function handleResult(resultData) {

    console.log("handleResult: populating single movie info from results");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let starInfoElement = jQuery("#movie_info");

    // append two html <p> created to the h3 body, which will refresh the page
    starInfoElement.append("<p>Movie Title: " + resultData[0]["movie_title"] + "</p>");

    console.log("handleResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");
    
    //console.log(resultData[0]["movie_title"]);

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(10, resultData.length); i++) {
    	var aId = resultData[i]["movie_id"];
    	var aTitle = resultData[i]["movie_title"]
    	
    	
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        var genresArray = resultData[i]["movie_genres"].split(',');
        rowHTML +=
            "<th>";
            // Add a link to single-star.html with id passed with GET url parameter
            for (let j = 0; j < genresArray.length; j++)
            {
            	var aGenre = genresArray[j];
            	rowHTML +=
            '<a href="#"  onclick="getGenre(\'' + aGenre + '\');return false;">'
            + genresArray[j];     // display star_name for the link text
            if (j < genresArray.length - 1)
        	{
        	rowHTML += ", ";
        	}
            rowHTML+='</a>'
            
            } rowHTML += "</th>";
            
        var starsArray = resultData[i]['movie_stars_id'].split(',');
        var starsNamesArray = resultData[i]["movie_stars"].split(',');
        rowHTML +=
            "<th>";
            // Add a link to single-star.html with id passed with GET url parameter
            for (let k = 0; k < starsArray.length; k++)
            {
            	rowHTML +=
            '<a href="single-star.html?id=' + starsArray[k] + '">'
            + starsNamesArray[k];     // display star_name for the link text
            if (k < starsArray.length - 1)
        	{
        	rowHTML += ", ";
        	}
            rowHTML+='</a>'
            
            } rowHTML += "</th>";
            
            rowHTML += "<th>" + '<button type=submit" id = "cart" onclick ="openCart(\'' + aId + '\',\'' + aTitle + '\');"> Add to Cart</button>' + "</th>";
            
            
         
        rowHTML += "</tr>";
        

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
        
        /*let htmlButton = "<p>" + '<button type=submit" id = "cart" onclick ="openCart(\'' + aId + '\',\'' + aTitle + '\');"> Add to Cart</button>' + "</th>" + "</p>";
        let bodyElement = jQuery("#buttongoeshere");
        bodyElement.append(htmlButton);*/
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let movieId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "single-movie?id=" + movieId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});





/*------------------------------------------------------*/

	