function handleConfirmation()
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

handleConfirmation();
sessionStorage.removeItem('shopping-cart-obj');