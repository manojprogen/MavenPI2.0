<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js"splitter></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/jquery.contextMenu.css" rel="stylesheet" />



        <style>
            .pics {
                height:  150px;
                width:   220px;
                padding: 0;
                -moz-border-radius: 10px;
                -webkit-border-radius: 10px;
                padding: 12px;
                border:  1px solid #ccc;
                background-color: #eee;
            }
            .contentDiv{
                height:145px;
                width:218px;
                position:absolute;
                background-color:#fff;
                border:1px solid silver;
                -moz-border-radius: 4px;
                -webkit-border-radius: 4px;
            }
            .body{
                background-color:#e6e6e6;
            }
            .startWhite{
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:450px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_start{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 120%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
        </style>
        <script>
            function cntxtMenu(Obj){
                document.getElementById("divId").value=Obj.id;
                //alert($('#'+Obj.id))
                $('#'+Obj.id).contextMenu({
                    menu: 'myMenu'
                },function() {
                    var frameObj=document.getElementById("userframe");
                    frameObj.src="pbStartwebPage.jsp";
                    frameObj.style.display='block';

                    document.getElementById("startPagePriv").style.width='800px';
                    document.getElementById("startPagePriv").style.height='450px';
                    document.getElementById("userframe").height='450px';
                    document.getElementById("userframe").width='800px';
                    document.getElementById('startPagePriv').style.display='block';
                    document.getElementById('fadestart').style.display='block';
                    document.getElementById('mainBody').style.overflow='hidden';
                });
            }
            function closeStartDiv(){
                document.getElementById('startPagePriv').style.display='none';
                document.getElementById('fadestart').style.display='none';
                document.getElementById('mainBody').style.overflow='auto';

            }

        </script>
    </head>
    <body class="body1" id="mainBody">
        <form name="myFormTestland" action="">
            <table cellpadding="9" cellspacing="9" align="center">
                <tr>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div id="div1" class="contentDiv" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv" id="div2" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv" id="div3" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv"id="div4" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv" id="div5" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv" id="div6" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv" id="div7" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv" id="div8" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                    <td>
                        <div id="sidebar" class="pics" >
                            <div class="contentDiv" id="div9" onmouseup="cntxtMenu(this)"><font style="font-family:verdana;font-size:16px;font-weight:bold;color:#369;position:absolute" align="center" >Click Here To Add</font></div>
                        </div>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="divId" id="divId" value="">
        </form>
        <ul id="myMenu" class="contextMenu">
            <li class="create"><a href="#create">Add Page</a></li>
        </ul>
        <div id="startPagePriv" class="startWhite" title="Start Page">
            <iframe id="userframe" src="" scrolling="no" height="450px" width="800px" frameborder="0" ></iframe>
        </div>
        <div id="fadestart" class="black_start"></div>
    </body>
</html>
