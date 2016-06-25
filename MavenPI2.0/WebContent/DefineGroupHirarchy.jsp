<%-- 
    Document   : DefineGroupHirarchy
    Created on : 13 Jul, 2012, 7:24:11 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.query.RTMeasureElement,prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.HashMap,java.util.ArrayList,com.progen.reportview.db.PbReportViewerDAO,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.db.SelectDbSpecificFunc,com.progen.reportdesigner.db.DashboardTemplateDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if ( (session.getAttribute("loadDialogs") != null && session.getAttribute("loadDialogs").equals("true") )
                 || (request.getParameter("loadDialogs") != null && request.getParameter("loadDialogs").equalsIgnoreCase("TRUE") )
                )
            {
            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            ReportTemplateDAO templateDAO = new ReportTemplateDAO();
            DashboardTemplateDAO dbDao=new DashboardTemplateDAO();
            //String folderIds = request.getParameter("folderIds");
            //String reportId = request.getParameter("REPORTID");

            //if(request.getSession().getAttribute("currentReportId")==null || reportId.trim().equalsIgnoreCase("")){
               // reportId=(String)session.getAttribute("REPORTID");
            //}
            String folderIds = (String)request.getParameter("folderIds");
            String groupId = (String)request.getParameter("groupId");
            String groupName=request.getParameter("groupName");
            String parentId=request.getParameter("parentId");
            String prevColumns = "";
            String rootColumns="";
            String factsHtml = null;
            ArrayList Parameters = new ArrayList();
            SelectDbSpecificFunc checknull = new SelectDbSpecificFunc();
            String themeColor="blue";

                
                    factsHtml = templateDAO.getMeasures(folderIds, Parameters,request.getContextPath());
                    prevColumns=dbDao.getPrevElements(groupId, parentId, folderIds);
                    prevColumns=prevColumns.substring(1);
                    if(parentId==null)
                   {
                        rootColumns=prevColumns;

                        }
                
             if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
                    String contextPath=request.getContextPath();
            
         %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
         <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>

        <script type="text/javascript" >
               $(document).ready(function() {
//                 if($.browser.msie== true)
//                {
//                    $("#delelteElements").dialog({
//                        autoOpen: false,
//                        height:200,
//                        width:200,
//                        position: 'justify',
//                        modal: true,
//                        buttons: {
//                        Continue: function () {
//                            isPrevDeleted=true;
//                            alert('isPrevDeleted'+isPrevDeleted);
//                            $(t$his).dialog("close");
//
//                        },
//                        Cancel: function () {
//                            isPrevDeleted=false;
//                            alert('isPrevDeleted'+isPrevDeleted);
//                            $(this).dialog("close").slideUp();
//                        }
//                    }
//
//
//                    });
//                }
//                 else
//                {
//                    $("#delelteElements").dialog({
//                        autoOpen: false,
//                        height:200,
//                        width: 200,
//                        position: 'justify',
//                        modal: true,
//                        buttons: {
//                        Continue: function () {
//                             isPrevDeleted=true;
//                             alert('isPrevDeleted'+isPrevDeleted);
//                            $(this).dialog("close");
//
//                        },
//                        Cancel: function () {
//                            isPrevDeleted=false;
//                            alert('isPrevDeleted'+isPrevDeleted);
//                            $(this).dialog("close").slideUp();
//                            }
//                        }
//                    });
//                }
                $("#myList3").treeview({
                    animated:"slow"
                    //persist: "cookie"
                });

               
                $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
                })

//        $(".formulaViewMenu").contextMenu({
//            menu: 'formulaViewListMenu',
//            leftButton: true },
//        function(action, el, pos) {
//            var formula = $(el).attr('title');
//            var measureName = document.getElementById($(el).attr('id')).innerHTML;
//            var elementId = $(el).attr('id');
//            switch(action){
//                case "view" :{
//                        contextMenuWorkFormulaView(action, el, pos);
//                        break;
//                    }
//                case "edit" :{
//
//                        parent.$("#tableColsDialog").dialog('close');
//                        var ctxPath='<%=request.getContextPath()%>';
//                        $.ajax({
//                            url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&reportId='+''+'&from=viewer',
//                            success:function(data) {
//                                editMeasure(elementId,formula,measureName,ctxPath,);
//                            }
//                        });
////                            }
////                        });
//                        break;
//
//                    }
//                }
//            });

            //code for double click to add measures in Edit Table option of ReportViewer

                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });

             //End

     //       $(function() {

                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#selectedMeasures');


                $(dragMeasure).dblclick(function(event) {
                            var elementId=event.target.id;
                            var pElementName=$("#"+elementId).html();
                            var obj=document.getElementById(elementId).parentNode;
                            var ulobj=obj.getElementsByTagName("ul");
                            if(ulobj!=null&&ulobj[0]!=null&&ulobj[0]!=undefined){
                            var liobjects=ulobj[0].getElementsByTagName("li");
                            var spanObj;
                            createColumn(elementId,pElementName);
                            for(var i=0;i<liobjects.length;i++)
                               {
                                   spanObj=liobjects[i].getElementsByTagName("span");
                                  createColumn(spanObj[0].id,spanObj[0].innerHTML);

                               }
                       }

             });



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
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());
                    }
                });


                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());
                    }
                }
            );

            });

        </script>
    
       <style type="text/css" >
            *{font:11px verdana;}
        </style>
    </head>
    <body onload="prevCols('<%=prevColumns%>')">
        <form action="" name="myForm3" method="post">
        <table style="width:100%;height:250px" border="solid black 1px">

<!--                <input type="hidden" name="prevColumns" id="prevColumns" value="">-->
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
                    <td id="selectedMeasures" width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Measures to here</font></div>
                        <div style="height:250px;overflow:auto">
                            <ul id="sortable" class="sortable">
                            </ul>
                        </div>
                    </td>
                </tr>
        </table>
        <table style="width:100%" align="center">
            <tr>
                <td colspan="2" style="height:10px"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveCols()">
                </td>
            </tr>
        </table>
        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
            <li class="edit"><a href="#edit">Edit</a></li>
        </ul>
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
             <table><tr><td id="value"></td></tr></table>
        </div>
        </form
        <div id="delelteElements" align="center" title="Delete Confirmation" style="display: none">
            <p align="center"> You are trying to Delete Parent Element, deletion of Which will deletes its Children<br> Would you like to continue..? <br></p>
<!--            <table align="center">
                <tr>
                    <td align="left"><input type="button" class="navtitle-hover" style="width:auto" value="Yes" onclick="deleteElement('true')"></td>
                    <td align="right"><input type="button" class="navtitle-hover" style="width:auto" value="No" onclick="deleteElement('false')"></td>
                </tr>
            </table>-->
        </div>

    </body>
        <script type="text/javascript" >
           
            var grpColArray=new Array();
            var prevColsArray=new Array();
            var rootColsArray=new Array();
            
            var prevColsStr="<%=prevColumns%>";
//            var rootColsStr="";
//             var rootCols=prevColsStr.split(",");
////            alert(prevCols.length);
//            for(var k=0;k<rootCols.length;k++){
//                var pr=rootCols[k].split("^");
//                rootColsArray.push(pr[0]);
//              }

         

            function prevCols(prevColsStr)
            {
//            alert("prevColsStr"+prevColsStr)
            var prevCols=prevColsStr.split(",");
//            alert(prevCols.length);
            for(var k=0;k<prevCols.length;k++){
                var pr=prevCols[k].split("^");
                prevColsArray.push(pr[0]);
                createColumn(pr[0],pr[1]);
            }
            }
           

            function saveCols(){
                parent.$("#tableColsDialog").dialog('close');
                var urlVar;
                var cols="";
                 var groupName="<%=groupName%>";
                var groupId="<%=groupId%>";
                var folderId="<%=folderIds%>";
                var parentId="<%=parentId%>";
                var colsUl=document.getElementById("sortable");
                 var ViewFrom="<%=session.getAttribute("ViewFrom")%>";
                if(colsUl!=undefined || colsUl!=null){
                 
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                        }
//                        alert('final columns: '+cols+"parentId"+parentId);
                        if(parentId=="null" || parentId==""){
                            urlVar="GroupMeassureCreation.do?templateParam=insertElements&groupName="+groupName+"&folderId="+folderId+"&groupId="+groupId+"&elements="+cols;
                        }
                        else if(parentId!="null" || parentId !="")
                            {
                                urlVar="GroupMeassureCreation.do?templateParam=insertElements&parentElement="+parentId+"&groupId="+groupId+"&elements="+cols+"&folderId="+folderId;
                            }
//                        alert('urlVar'+urlVar);
                        $.ajax({


                            url:urlVar,
                            success: function(data){
                                if(data=='false'){
                                    var source ="";
                                    var dSrc = parent.document.getElementById("iframe1");
                                    dSrc.src = source;
                                }else{
                                    parent.$("#dragandDrop").dialog('close');
                                   
                                    parent. $("#confirmartionDiv").html(data);
//                                    parent.window.location.href=parent.window.location.href;
                                }
                            }
                        });
                        cancelChangeGrpColumns();
                    }
                    else{
                        //alert("Please Select Measures");
                    }
                }
            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                
                var x=index.replace("GrpCol","");
                var i=0;


                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==x)
                        {
//                                if(rootColsArray.indexOf(x)!=-1)
//                                {
//                                    alert("Sorry...You cannot delete the Root Parent in the Hirarchy..!!!");
//                                }
//                                else{
                                if(prevColsArray.indexOf(x)!=-1)
                                {
                                    var confirmbox=confirm("This element is a Parent,Deletion of which changes the hirarchy.You want to proceed..??");
                                    if(confirmbox)
                                        {
                                            parentUL.removeChild(LiObj);
                                            grpColArray.splice(i,1);
                                        }
                                }
                                else
                                {
                                    parentUL.removeChild(LiObj);
                                    grpColArray.splice(i,1);

                                }
//                            }
                            
                        }

                        

                }
            }
         
            function createColumn(elmntId,elementName){
                var parentUL=document.getElementById("sortable");
//                alert("parentUL"+parentUL)
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
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
                $(".sortable").sortable();
                $(".sortable").disableSelection();
            }

//            function createColumn(elmntId,elementName,tarLoc){
//                alert('tarLoc'+tarLoc);
//                alert('elmntId'+elmntId);
//                alert('elementName'+elementName);
//                var parentUL=document.getElementById(tarLoc);
//                alert(parentUL);
//                var x=grpColArray.toString();
//                if(x.match(elmntId)==null){
//                    grpColArray.push(elmntId);
//
//                    var childLI=document.createElement("li");
//                    childLI.id='GrpCol'+elmntId;
//
//                    childLI.style.width='auto';
//                    childLI.style.height='25px';
//                    childLI.style.color='white';
//                    childLI.className='navtitle-hover';
//                    var table=document.createElement("table");
//                    table.id="GrpTab"+elmntId;
//                    var row=table.insertRow(0);
//                    var cell1=row.insertCell(0);
//
//                    var a=document.createElement("a");
//                    var deleteElement = 'GrpCol'+elmntId;
//                    a.href="javascript:deleteColumn('"+deleteElement+"')";
//                    a.innerHTML="a";
//                    a.className="ui-icon ui-icon-close";
//                    cell1.appendChild(a);
//                    var cell2=row.insertCell(1);
//
//                    cell2.style.color='black';
//                    cell2.innerHTML=elementName;
//                    childLI.appendChild(table);
//                    parentUL.appendChild(childLI);
//                }
//            }

            function cancelCols(){
                cancelChangeGrpColumns();
            }
            function cancelChangeGrpColumns(){
                var frameObj=parent.document.getElementById("tableColsFrame");
              //  frameObj.style.display='none';
              parent.$("#tableColsDialog").dialog('close');
                parent.document.getElementById('fadestart').style.display='none';
            }
            function contextMenuWorkFormulaView(action, el, pos){
                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');
                
            }

        </script>

</html>
<% } %>