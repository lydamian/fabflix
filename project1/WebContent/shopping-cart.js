console.log("Hi you entered shopping cart.js");

function getParameterByName(target) {
	console.log("shopping-cart.js page loaded...");
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
function deleteMovie(movieId)
{
		console.log("delete movie called");
		var current = sessionStorage.getItem('shopping-cart-obj');
		var currentJS = JSON.parse(current);
		for (let i = 0; i < currentJS.length; i++) {
			if (currentJS[i]["movieId"] == movieId)
			{
				currentJS.splice(i, 1);
			}
		}
		console.log(currentJS);
		sessionStorage.setItem('shopping-cart-obj', JSON.stringify(currentJS));
		
}
function updateQuantity(movieId)
{
	console.log("change quantity js called");
	var e = document.getElementById('\'' + movieId + '\'');
	var quantityChosen = e.options[e.selectedIndex].value;
	
	if (quantityChosen == 0)
		deleteMovie(movieId);
	else {
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
}

function handleSearchResult()
{
	var resultDataJS = sessionStorage.getItem('shopping-cart-obj');
    var resultData = JSON.parse(resultDataJS);
	let movieCartTableBodyElement = jQuery("#movie_cart_table");
	var  actualQuanitity;
    // Iterate through resultData
    for (let i = 0; i < resultData.length; i++) {
    	//console.log(resultData.length);
        // Concatenate the html tags with resultData jsonObject
    	actualQuantity = resultData[i]["quantity"];
        let rowHTML = "";
        rowHTML += "<tr>";
        
        rowHTML += "<th>" + resultData[i]["movieId"] + "</th>";
        
        rowHTML += "<th>" + resultData[i]["movieTitle"] + "</th>";
        
        rowHTML += "<th>" + '<p>' + resultData[i]["quantity"] + '</p>' + '<select name="quantity-dropdown" id="\'' + resultData[i]["movieId"] + '\'" onchange="updateQuantity(\'' + resultData[i]["movieId"] +'\')"><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="0">0</option></select>' + "</th>";
        
        
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieCartTableBodyElement.append(rowHTML); 
        
    }
}
/*window.onload = function()
{
	var resultDataJS = sessionStorage.getItem('shopping-cart-obj');
    var resultData = JSON.parse(resultDataJS);
    for (let i = 0; i < resultData.length; i++) {
	document.getElementById(resultData[i]["movieId"]).selectedIndex = actualQuantity;
    }
}*/
handleSearchResult();