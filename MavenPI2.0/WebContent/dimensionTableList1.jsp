<%-- 
    Document   : dimensionTableList1
    Created on : Aug 21, 2009, 5:45:00 PM
    Author     : Saurabh
--%>
<%@page import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>
            var y="";
            var xmlHttp;
            var ctxPath;

            function saveTables()
            {
                //alert(1)
                 $.post("saveDimTableList.do?tables="+y,function(data){
                 parent.refreshparent();
               })


//                 ctxPath=document.getElementById("h").value;
//                 $.post(ctxPath+"/saveDimTableList.do?tables="+y,function(data){
//
//                })

                           }

            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }
            function stateChanged()
            {
                if (xmlHttp.readyState==4)
                {

                    var output1=xmlHttp.responseText
                    //walert(output1);


                }
            }

            function cancelTables()
            {
                //alert("kkk");
                parent.cancelTablesp1();
            }
            $(function() {

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });


                $("#dropTabs").droppable(
                {
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',

                    drop: function(ev, ui) {
                        /*   for(a in ui.draggable){
                    alert(ui.draggable.html());
                    alert(a)
                }*/
                        //$draggableTables.html(ui.draggable.html());
                        var x=$("#dropTabs").html();
                        y = y+ui.draggable.attr('id')+",";
                        // alert("y is "+y)
                        // alert("x is "+x)

                        $("#dropTabs").html(x+ui.draggable.html()+"<br>");
                        var z = ui.draggable.attr('id');
                        //alert("z is "+z)
                        document.getElementById(z).style.display='none';
                    }

                }
            );

            });
        </script>

        <style>
            .myhead
            {
                font-family: Verdana;
                font-size: 8pt;
                font-weight: bold;
                padding-left:12px;
                background-color:#b4d9ee;
                width:50%;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
             *{
                margin:         0px;
                padding:        0px;
                font:           11px verdana;
            }

        </style>
    </head>
    <body>
        <center>
            <table width="100%">
                <tr>
                    <th><font style="font-family:verdana;font-size:12px;color:#369">Tables List (Drag Tables to Right and click Save)</font></th>
                </tr>
            </table>
        </center>
        <form name="myForm">
            <table align="center" border="1px" width="100%" style="height:100%">
                <tr>
                    <td class="myhead" style="width:50%" valign="top">
                        <table style="height:100%" border="0">
                            <%
        ArrayList list = (ArrayList) request.getAttribute("list");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String[] tables = ((String) list.get(i)).split("-");
                            %>

                            <tr>
                            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                                <td class="myDragTabs" valign="top" style="font-weight:normal"  id="<%=tables[0]%>"><%=tables[1]%></td>
                            </tr>
                            <%
            }






        }
                            %>
                        </table >
                    </td>
                    <td id="dropTabs" style="width:50%" class="myhead" valign="top">

                    </td>

                </tr>
        </table></form>
        <br><center>
            <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTables()">&nbsp;<input type="button" class="navtitle-hover" style="width:auto"  value="Cancel" onclick="cancelTables()">
        </center>
    </body>
</html>
