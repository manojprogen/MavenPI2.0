<html><head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"><title>Check off the foods you want to eat today.</title>

<link href="flavorzoom_files/flavorzoom.css" rel="stylesheet" type="text/css">
<script type="text/javascript"src="<%=request.getContextPath()%>/querydesigner/JS/jquery.js"></script>
<script type="text/javascript"src="<%=request.getContextPath()%>/querydesigner/JS/tablefilter.js"></script>
<script type="text/javascript" src="flavorzoom_files/my_food_plan_pick_foods.js"></script><script type="<%=request.getContextPath()%>/querydesigner/JS/uitablefilter.js"></script></head><body>


<Script>

$(function() {
  var theTable = $('table.food_planner')

  theTable.find("tbody > tr").find("td:eq(1)").mousedown(function(){
    $(this).prev().find(":checkbox").click()
  });

  $("#filter").keyup(function() {
  $.uiTableFilter( theTable, this.value,1,yes);
  
  })

  $('#filter-form').submit(function(){
    theTable.find("tbody > tr:visible > td:eq(1)").mousedown();
    return false;
  }).focus(); //Give focus to input field
});


</script>

</head>
<body>
    <div id="mainbody">

<form id="filter-form">Filter: <input name="filter" id="filter" value="" maxlength="30" size="30" type="text"></form><br>

<table class="food_planner" name="demotable"><thead><tr><th colspan="2">Your Food List</th><tr></thead>
    <tbody><tr style="display: table-row;">

          </tr><tr><td><input name="food_id_1" value="23558" checked="checked" id="" style="" type="checkbox"></td><td>
          &nbsp; &nbsp; &nbsp;Beef, ground, 95% lean meat / 5% fat, patty, cooked, broiled<input name="food_source_row_1" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_2" value="18061" checked="checked" id="" style="" type="checkbox"></td><td>
          &nbsp; &nbsp; &nbsp;Bread, rye, toasted<input name="food_source_row_2" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_3" value="11742" checked="checked" id="" style="" type="checkbox"></td><td>
          &nbsp; &nbsp; &nbsp;Broccoli, cooked, boiled, drained, with salt<input name="food_source_row_3" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_4" value="1001" checked="checked" id="" style="" type="checkbox"></td><td>

          &nbsp; &nbsp; &nbsp;Butter, salted<input name="food_source_row_4" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_5" value="6242" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;CAMPBELL Soup Company, CAMPBELL'S CHUNKY Microwavable Bowls, Chicken and Dumplings Soup<input name="food_source_row_5" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_6" value="6396" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;CAMPBELL Soup Company, CAMPBELL'S CHUNKY Soups, Fajita Chicken with Rice &amp; Beans Soup<input name="food_source_row_6" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_7" value="11960" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Carrots, baby, raw<input name="food_source_row_7" value="USDA" type="hidden"></td></tr><tr style="display: table-row;"><td><input name="food_id_8" value="1040" checked="checked" id="" style="" type="checkbox"></td><td>

&nbsp; &nbsp; &nbsp;Cheese, swiss<input name="food_source_row_8" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_9" value="7933" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Chicken breast, oven-roasted, fat-free, sliced<input name="food_source_row_9" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_10" value="14209" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Coffee, brewed from grounds, prepared with tap water<input name="food_source_row_10" value="USDA" type="hidden"></td></tr><tr style="display: table-row;"><td><input name="food_id_11" value="1130" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Egg, whole, cooked, omelet<input name="food_source_row_11" value="USDA" type="hidden"></td></tr><tr style="display: table-row;"><td><input name="food_id_12" value="18265" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;English muffins, wheat, toasted<input name="food_source_row_12" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_13" value="15092" id="" style="" type="checkbox"></td><td>

&nbsp; &nbsp; &nbsp;Fish, sea bass, mixed species, cooked, dry heat<input name="food_source_row_13" value="USDA" type="hidden"></td></tr><tr style="display: table-row;"><td><input name="food_id_14" value="18640" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;HEINZ, WEIGHT WATCHER, Chocolate Eclair, frozen<input name="food_source_row_14" value="USDA" type="hidden"></td></tr><tr style="display: table-row;"><td><input name="food_id_15" value="42138" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Mayonnaise, reduced-calorie or diet, cholesterol-free<input name="food_source_row_15" value="USDA" type="hidden"></td></tr><tr style="display: table-row;"><td><input name="food_id_16" value="4053" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Oil, olive, salad or cooking<input name="food_source_row_16" value="USDA" type="hidden"></td></tr><tr style="display: table-row;"><td><input name="food_id_17" value="9203" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Oranges, raw, Florida<input name="food_source_row_17" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_18" value="20047" checked="checked" id="" style="" type="checkbox"></td><td>

&nbsp; &nbsp; &nbsp;Rice, white, long-grain, parboiled, enriched, cooked<input name="food_source_row_18" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_19" value="18350" checked="checked" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Rolls, hamburger or hotdog, plain<input name="food_source_row_19" value="USDA" type="hidden"></td></tr><tr><td><input name="food_id_20" value="14476" id="" style="" type="checkbox"></td><td>
&nbsp; &nbsp; &nbsp;Tea, ready-to-drink, LIPTON BRISK iced tea, with lemon flavor<input name="food_source_row_20" value="USDA" type="hidden"></td></tr></tbody></table><br><br></form>


	</div>


</body></html>
