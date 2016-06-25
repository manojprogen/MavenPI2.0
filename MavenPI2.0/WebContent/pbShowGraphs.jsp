<%-- 
    Document   : pbShowGraphs
    Created on : Oct 3, 2009, 6:05:30 PM
    Author     : mahesh.sanampudi@progenbusiness.com
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
        <title>Graphs</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
 <!--       <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/dashboardDesign.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>
        <link href="<%=request.getContextPath()%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>

        <%String path = request.getContextPath();%>

        <script type="text/javascript">
            var y="";
            var xmlHttp;
            var ctxPath;
            var path = "<%=path%>";
            var repIdValue;
            var dataValue;
           
            var passGrpId;
            var passRepId;

           // added by susheela start
            var graphId;

            function saveGraphs(){
//                alert("Inside saveGraph");
                 var  divId=parent.document.getElementById("divId").value;
                parent.document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                    $.ajax({
                        url: path+'/dashboardTemplateViewerAction.do?templateParam2=getGraphByGraphId&graphId='+passGrpId+'&reportId='+passRepId
                            +'&dashboardId='+parent.document.getElementById("dbrdId").value+'&addedToDashBoard=true&divId='+divId,

                    success: function(data) {
                        if(data != ""){
                            parent.document.getElementById(divId).innerHTML ="";
                             parent.document.getElementById(divId).innerHTML = data;

                        }
                    }
                });
                

                parent.cancelRepGraph();
            }
            function cancelGraph(){
                parent.cancelRepGraph();
            }

            function cancelGraph2(){

                var divId=parent.document.getElementById("divId").value;
                parent.document.getElementById(divId).innerHTML = '';
                parent.document.getElementById(divId).style.display='block';
                parent.cancelRepGraph();

            }
            // added by susheela over

            $(document).ready(function() {
                $("#graphTree").treeview({
                    animated: "normal",
                    unique:true
                });

                $('ul#reports li').quicksearch({
                    position: 'before',
                    attached: 'ul#reports',
                    loaderText: '',
                    delay: 100
                });
            });

            // added by susheela start
            function dragGraph(repId){
                repIdValue= repId;
                        }

            function buildDbrdGraph(reportId,graphId){
                passRepId=reportId
                passGrpId=graphId
                var  divId=parent.document.getElementById("divId").value;
                document.getElementById("dragDiv").innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                $.ajax({
                        url: path+'/dashboardTemplateViewerAction.do?templateParam2=getGraphByGraphId&graphId='+graphId+'&reportId='+reportId+'&divId='+divId+'&dashboardId='+parent.document.getElementById("dbrdId").value,

                    success: function(data) {
                        if(data != ""){
                            document.getElementById("dragDiv").innerHTML ="";
                            document.getElementById("dragDiv").innerHTML = data;

                    }
            }
                });
                    }
        </script>
        <style type="text/css">
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#EAF2F7;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
            *{font:11px verdana}
        </style>
    </head>
    <body >
        <%
        String GraphData = "";
        if (request.getAttribute("Graphs") != null) {
            GraphData = String.valueOf(request.getAttribute("Graphs"));
        }
        %>
        <form name="myForm2" method="post" action="">
            <table style="width:100%;height:320px" border="solid black 1px">
                <tr>
                    <td width="40%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Graphs</font></div>
                        <div class="masterDiv" style="height:280px;width:300px;overflow-x:auto;overflow-y:auto">
                            <ul id="graphTree" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="reports">
                                        <%=GraphData%>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td width="60%" valign="top"  id="draggableGraphs" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Graph Preview</font>
                        </h3>
                        <div id="dragDiv" style="height:280px;width:500px;overflow-y:auto;">
                               
                        </div>
                    </td>
                </tr>
            </table>
        </form>
                                    <br/>
        <center>
            <input type="button" class="navtitle-hover" value="Save" onclick="saveGraphs()">
            <%--<input type="button" class="navtitle-hover" value="Cancel" onclick="cancelGraph()">--%>
        </center>
    </body>
</html>

