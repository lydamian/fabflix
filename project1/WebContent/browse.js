function handleGenreResult(resultDataString)
{
	console.log("Success");
	//console.log(resultDataString);
	window.location.replace("results.html");
	sessionStorage.setItem('resultSearchObj', JSON.stringify(resultDataString));
}
function handleTitleResult(resultDataString)
{
	console.log("Success");
	window.location.replace("results.html");
	sessionStorage.setItem('resultSearchObj', JSON.stringify(resultDataString));
}
function chooseGenre()
{
	console.log("choose genre js called");
	var e = document.getElementById("genre-dropdown");
	var genreChosen = e.options[e.selectedIndex].value;
	
	$.ajax({
		type: "GET",
		url: "GenreSearchServlet",
		data: {genre: genreChosen},
		success: (resultDataString) => handleGenreResult(resultDataString)
	});
	console.log(genreChosen);
}

function chooseTitle()
{
	console.log("choose title js called");
	var e = document.getElementById("title-dropdown");
	var titleChosen = e.options[e.selectedIndex].value;
	
	$.ajax({
		type: "GET",
		url: "TitleSearchServlet",
		data: {title: titleChosen},
		success: (resultDataString) => handleTitleResult(resultDataString)
	});
	console.log(titleChosen);
}