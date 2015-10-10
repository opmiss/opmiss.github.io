var fs = require('fs'); 
var output = "data.json"; 
fs.readFile('countries.json','utf8', function(err, data){
	if (err) throw err; 
	var input = data.toString(); 
	fs.writeFile(output, JSON.stringify(parse(input)), function(e){
		if (e) throw e; 
	}); 
})

function parse(input){
	var obj = JSON.parse(input);
	var countries  = obj.table.tbody.tr;
	var output = []; 
	for (var i=1; i<countries.length; i++){
		var name = countries[i].td[3]+'-'+countries[i].td[0]; 
		var loc = {lat:countries[i].td[1],lon:countries[i].td[2]}; 
		output.push({label:name, loc:loc}); 
	}
	return output; 
}
