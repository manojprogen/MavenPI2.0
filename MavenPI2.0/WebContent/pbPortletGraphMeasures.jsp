

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.db.SelectDbSpecificFunc,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.reportview.db.PbReportViewerDAO,prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.HashMap,java.util.ArrayList" %>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            ReportTemplateDAO templateDAO = new ReportTemplateDAO();
            String portletId=request.getParameter("portletId");
            String portalTabId=request.getParameter("portalTabId");
            String folderIds = request.getParameter("folderIds");
            String graphIds = request.getParameter("grpIds");
            String grpId = request.getParameter("graphId");
            String prevmeasureIds=request.getParameter("measureId");
            String prevmeasureNames=request.getParameter("measureName");
            String prevmIds[]=prevmeasureIds.split(",");
            String prevmNames[]=prevmeasureNames.split(",");
            String factsHtml = null;

            factsHtml = templateDAO.getMeasures(folderIds,null, request.getContextPath());
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <title>piEE</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
         <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
         <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" >
            var grpColArray=new Array();
             var mesNameArray=new Array();
             $(document).ready(function() {
            $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
                })
                $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView(action, el, pos);
                });
                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });
             });

                 $(function() {
                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#dropTabs');
                var sort1Measures=$('#sortable1');
                var sort2Measures=$('#sortable2');
                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $(dropMeasures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                });

                 $("#sortable").sortable();
                $("#sortable").disableSelection();

            });

          
        </script>
        <style type="text/css">
            *{font:11px verdana}
        </style>
        </head>

        <body>
      <% try {%>
        <table style="width:100%;height:270px" border="solid black 1px">
            <form action=""  name="myForm3" method="post">
                <input type="hidden" name="prevColumns" id="prevColumns" value="">
                <input type="hidden" name="forwardFlag" id="forwardFlag" value="">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Measures from below</font></div>
                        <div style="height:250px;overflow:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">

                                <%--<li class="open" style="background-image:url('<%=request.getContextPath()%>/images/treeViewImages/plus.gif')">--%>
                                <ul id="measures">
                                    <%=factsHtml%>

                                </ul>
                                <%--</li>--%>
                            </ul>
                        </div>
                    </td>

                    <td id="dropTabs" width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Graph Columns to here</font></div>
                        <div style="height:250px;overflow:auto">
                            <ul id="sortable">

                            </ul>
                        </div>
                    </td>

                </tr>
            </form>
        </table>

        <center><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCols()">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelCols()"></center>
           <%} catch (Exception e) {
                    e.printStackTrace();
                }

        %>

        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
        </ul>
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
            <table >
                <tr>
                    <td id="value"></td>
                </tr>
                </table>
                    </div>
        <script>
            showprevMeasures();
        </script>
          <script>
          function createColumn(elmntId,elementName,tarLoc){

                var parentUL=document.getElementById(tarLoc);
                var x=grpColArray.toString();
                if(x.match(elmntId)==null){
                   grpColArray.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id="GrpTab"+elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.id = "eleName"+elmntId;
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
            }


            function saveCols(){
              var colsUl=document.getElementById("sortable");
              var cols="";
              var tdId ;
              var innerTd;
              var measureNames ;
              if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");

                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                            //   alert("cols---"+cols)
                            tdId = colIds[i].id.replace("GrpCol","");
                            innerTd = document.getElementById("eleName"+tdId).innerHTML;
                            mesNameArray.push(innerTd);
                            measureNames = mesNameArray.toString().replace("&gt;","~").replace("&lt;","^");
                            //    alert("measureNames---"+measureNames)
                        }

                        if(cols!=""){
                            cols = cols.substring(1);
                        }
                     //   parentDiv.style.height = '250'
                       // parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" /></center>';
                   $.ajax({
                                url: 'portalViewer.do?portalBy=changeGraphMeasure&portletId='+<%=portletId%>+'&portalTabId='+<%=portalTabId%>+'&measureNames='+measureNames+'&measureIds='+cols,
                                success: function(data){
                                     var rowEdgeParams="";
                    var colEdgeParams="";
                    var rowParamIdObj=document.getElementsByName("chkREP-"+<%=portletId%>+ "-" +<%=portalTabId%>)
                    var columnParmObject=''
                    var CEPNames=''
                    var REPNames=''
                     columnParmObject=document.getElementsByName("chkCEP-"+<%=portletId%>+"-"+<%=portalTabId%>)
                     if(rowParamIdObj!=null){

                    for(var i=0;i<rowParamIdObj.length;i++){
                       if(rowParamIdObj[i].checked){
                            rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                            REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
                        }
                    }
                    if(rowEdgeParams!=""){
                        rowEdgeParams=rowEdgeParams.substring(1);
                        REPNames=REPNames.substring(1);
                         }
                      }
                    if(columnParmObject!=null){

                            for(var j=0;j<columnParmObject.length;j++){
                             if(columnParmObject[j].checked){
                            colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                            CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                             }
                            }
                              if(colEdgeParams!=""){
                             colEdgeParams=colEdgeParams.substring(1);
                             CEPNames=CEPNames.substring(1);
                            }
                         }

                 parent.getPortletDetails(<%=portletId%>, rowEdgeParams, colEdgeParams,"","",<%=portalTabId%>,"");

                                }
                            });
                    cancelCols();
                   }
                    else{
                        alert("Please Select Graph Columns");
                    }
                 }
            }
            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);
                var x=index.replace("GrpCol","");
                var i=0;

                for(i=0;i<grpColArray.length;i++){
                      if(grpColArray[i]==x)
                        grpColArray.splice(i,1);
                }

            }
             function cancelCols(){
                parent.$("#graphMeasuresDiv").dialog("close");
                    }
             function contextMenuWorkFormulaView(action, el, pos){
                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');
            }
           function showprevMeasures(){
                 <% for(int i=0;i<prevmIds.length;i++){%>
            createColumn('<%=prevmIds[i]%>','<%=prevmNames[i]%>',"sortable");
                <%}%>
            }
  </script>
        </body>
        </html>
