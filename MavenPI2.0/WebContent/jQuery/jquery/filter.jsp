<html>
<head>
  <script src="http://code.jquery.com/jquery-latest.js"></script>

  <script>
  $(document).ready(function(){

    $("div").css("background", "#b4b0da")
            .filter(function (index) {
                  return index == 1 || $(this).attr("id") == "fourth";
                })
            .css("border", "3px double red");

  });
  </script>
  <style>
  div { width:60px; height:60px; margin:5px; float:left;
        border:3px white solid; }
  </style>
</head>
<body>
  <div id="first"></div>
  <div id="second"></div>
  <div id="third"></div>
  <div id="fourth"></div>
  <div id="fifth"></div>
  <div id="sixth"></div>
</body>
</html>
