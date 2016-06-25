$(document).ready(function(){

	// first example
	$("#navigation").treeview({
		persist: "location",
		collapsed: true,
		unique: true
	});

	// second example
	$("#browser").treeview({
        animated: "normal",
        unique:true
		});
	$("#add").click(function() {

       var branches = $("<li><img src='images/treeViewImages/database_connect.png'><span>New Sublist</span><ul>" +
			"<li class='closed'><span class='folder'>Tables</span>" +
			"<ul class='closed'id='folder212'><li><span class='file'>File 2.1.1</span></li>"+
						"<li><span class='file'>File 2.1.2</span></li></ul></li>"+
			"<li class='closed'><span class='folder'>Views</span>"+
			"<ul id='folder2162'><li><span class='file'>File 2.1.1</span></li>"+
						"<li><span class='file'>File 2.1.2</span></li></ul></li>"+
		"</li></ul></li>").appendTo("#databaseConnection");
		$("#databaseConnection").treeview({
			add: branches
		});
		branches = $("<li class='closed'><span class='folder'>New Sublist</span><ul><li><span class='file'>Item1</span></li><li><span class='file'>Item2</span></li></ul></li>").prependTo("#folder21");
		$("#databaseConnection").treeview({
			add: branches
		});
        }

);

	// third example
	$("#red").treeview({
		animated: "fast",
		collapsed: true,
		unique: true,
		persist: "cookie",
		toggle: function() {
			window.console && console.log("%o was toggled", this);
		}
	});

	// fourth example
	$("#black, #gray").treeview({
		control: "#treecontrol",
		persist: "cookie",
		cookieId: "treeview-black"
	});

});