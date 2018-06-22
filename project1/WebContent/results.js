/* This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function getStars(resultData, idx) {
	
}
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
	
	//var cartArray = [];
	/**/
	
	
	var testVar = sessionStorage.getItem('shopping-cart-obj');
	var anotherVar = JSON.parse(testVar);
	console.log(anotherVar);
}
function handleSearchResult() {
	
    console.log("handleStarResult: populating star table from resultData");
    
    var resultDataJS = sessionStorage.getItem('resultSearchObj');
    var resultData = JSON.parse(resultDataJS);
    //console.log(resultData[0]["id"]);
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#movie_table");

    // Iterate through resultData
    for (let i = 0; i < resultData.length; i++) {
    	console.log(resultData.length);
    	title = resultData[i]["title"];
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        var aId = resultData[i]["id"];
        var aTitle = resultData[i]["title"];
        rowHTML += "<tr>";
        
        rowHTML += "<th>" + resultData[i]["id"] + "</th>";
        
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['id'] + '">'
            + resultData[i]["title"] +     // display star_name for the link text
            '</a>'  + '<button type="submit" id = "cart" onclick ="openCart(\'' + aId + '\',\'' + aTitle + '\');"> Add to Cart</button>' + "</th>";
        
        
        rowHTML += "<th>" + resultData[i]["year"] + "</th>";
        
        rowHTML += "<th>" + resultData[i]["director"] + "</th>";
        
        rowHTML += "<th>" + resultData[i]["genres"] + "</th>";
        
        var starsArray = resultData[i]['stars_id'].split(',');
        var starsNamesArray = resultData[i]["stars"].split(',');
        rowHTML +=
            "<th>";
            // Add a link to single-star.html with id passed with GET url parameter
            for (let j = 0; j < starsArray.length; j++)
            {
            	rowHTML +=
            '<a href="single-star.html?id=' + starsArray[j] + '">'
            + starsNamesArray[j];     // display star_name for the link text
            if (j < starsArray.length - 1)
        	{
        	rowHTML += ", ";
        	}
            rowHTML+='</a>'
            
            } rowHTML += "</th>";
        
        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}

console.log("results.js page called...");

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
handleSearchResult();
$(document).ready(function() {
    $('#movie_table').DataTable();
} );
// Makes the HTTP GET request and registers on success callback function handleSearchResult
/*jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "search", // Setting request url, which is mapped by SearchServlet in SearchServlet.java
    success: (resultData) => handleSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});*/


/*jQuery.ajax({
dataType: "json", // Setting return data type
method: "GET", // Setting request method
url: "search", // Setting request url, which is mapped by SearchServlet in SearchServlet.java
success: (resultData) => handleSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});*/


/*$(document).ready(function(){
	$("#cart").click(function(){
		window.alert("SUPU  MYYY FAMMM!!!");
	})
});*/
