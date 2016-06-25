<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>



<title>Resizeable demo - Interface plugin for jQuery</title>
		<script type="text/javascript" src="javascript/jquery-latest.js"></script>
		<script type="text/javascript" src="javascript/interface.js"></script>
		<script type="text/javascript" src="javascript/jquery.tableresizer.js"></script>
<style type="text/css">

            *
            {
                margin:         0px;
                padding:        0px;
                font:           11px sans-serif;
            }

            ul.project
            {
                margin-left:    30px;
            }

            h1
            {
                font:           24px sans-serif;
            }

            h2
            {
                margin-top:     30px;
                font:           bold 16px sans-serif;
            }

            hr
            {
                margin-bottom:  4px;
            }

           

            p
            {
                margin:         10px 0px;
            }

            #container
            {
                padding:        20px;
                background:     #FFF;
                width:          600px;
                margin:         0px auto;
            }

        </style>

</head><body style="position: static;">


	
	<table style="width: 675px; height: 352px; border:1px" id="tableContent"><tr><th></th><th>Team</th><th>P</th><th>W</th><th>D</th><th>L</th><th>F</th><th>A</th><th>W</th><th>D</th><th>L</th><th>F</th><th>A</th><th>GD</th><th>PTS</th></tr>
                
	</table>
	

<script type="text/javascript">
$(document).ready(

function()
            {
                var opts =
                {
                    row_border:"2px solid #CCC",
                    col_border:"2px solid #000"
                };

                $("#tableContent").tableresizer(opts);
            });
</script>
		
		
<d</body></html>