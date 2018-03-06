/**
 * 
 */

console.log("Ajax!");

const request = new XMLHttpRequest();

request.open("GET", "http://localhost:9000/ajax/req");

request.addEventListener("load", (e) => {
	console.log(e);
	const div = document.getElementById("ajax");
	div.innerHTML = e;
});

request.send();
