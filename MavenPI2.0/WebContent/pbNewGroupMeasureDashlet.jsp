<%-- 
    Document   : pbNewGroupMeasureDashlet
    Created on : 13 Aug, 2012, 11:00:27 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            String themeColor = "blue";
            String dashletId = request.getParameter("dashletId");
            String dashBoardId = request.getParameter("dashBoardId");
            String refReportId = request.getParameter("refReportId");
            String graphId = request.getParameter("graphId");
            String dispSequence = request.getParameter("dispSequence");
            String kpiMasterId = request.getParameter("kpiMasterId");
            String dispType = request.getParameter("dispType");
            String dashletName = request.getParameter("dashletName");
            String editDbrd = request.getParameter("editDbrd");
String contextPath=request.getContextPath();

%>

<html>
    <head>
        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <link href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">-->
        <link href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
         <style type="text/css" >
            .ui-progressbar-value { background-image: url(images/barchart.gif); }
            .ajaxboxstyle {
                background-color:#FFFFFF;
                border: 0.1em solid #0000FF;
                height:50px;
                margin:0 0.5em;
                overflow-x:hidden;
                overflow-y:auto;
                position:absolute;
                text-align:left;
                border-top: 1px groove #848484;
                border-right: 1px inset #999999;
                border-bottom: 1px inset #999999;
                border-left: 1px groove #848484;
                background-color:#f0f0f0;
                width:450px;
            }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }

            table.grid .collapsible {
                padding: 0 0 3px 0;
            }

            .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }

            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
        </style>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Group Measure Hierarchy</title>
        <script type="text/javascript">
         $(document).ready(function(){
                 if ($.browser.msie == true){
                     $("#DrillToReport").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 200,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
               
                 }
                 else
                     {
                         $("#DrillToReport").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 200,
                        position: 'justify',
                        modal: true,
                        resizable:false
            });

                     }
               
            });
            </script>
       
    </head>
    <body onload="initialGroupMeasure()" id="bodycontent">
        <form action="" name="frmParameter" method=""></form>
         <script type="text/javascript">
           
            function initCollapser(divId){
                if (divId == ""){
                    $(".tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
                else{
                    $("#"+divId+" > .tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
            }
            function initialGroupMeasure()
            {
                var dashletId="<%=dashletId %>";
                var dashBoardId="<%=dashBoardId %>";
                var refReportId="<%=refReportId %>";
                var graphId="<%=graphId %>";
                var kpiMasterId="<%=kpiMasterId %>";
                var dispSequence="<%=dispSequence %>";
                var dispType="<%=dispType %>";
                var dashletName="<%=dashletName %>";
                var editDbrd="<%=editDbrd %>";
                var dashletObj=document.getElementById("bodycontent");
                dashletObj.innerHTML='<div id="InnerDashlet-'+dashletId+'" ></div>';
                $("#InnerDashlet-"+dashletId).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');

                $.ajax({type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                url: 'dashboardViewer.do?reportBy=displayGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd,
                success: function(data){
                    $("#InnerDashlet-"+dashletId).html(data);
                     initCollapser("");
                }
                });
            }
            function loadGroupMeassure(dbrdId,dashletId,groupId,rootElementList)
            {
                var disprows = $("#viewType").val();
                var dispType=$("input[name='groupView-"+dashletId+"']:checked").val();
                //                  alert('dispType'+dispType);
                $("#InnerDashlet-"+dashletId).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');
                $.ajax({type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: "dashboardTemplateAction.do?templateParam2=reloadGroupMeassures&dbrdId="+dbrdId+"&dashletId="+dashletId+"&rowsToDisp="+disprows+"&groupId="+groupId+"&rootElements="+rootElementList+"&groupType="+dispType,
                    success: function(data){
//                        alert(data);
//                        if($("#GroupMeassureProperties").dialog('isOpen'))
//                        {
//                            $("#GroupMeassureProperties").dialog('close');
//                        }
                        $("#InnerDashlet-"+dashletId).html(data);
                        initCollapser("");
                        //                            currRow.attr("initialized","true");
                    }
                });

            }
            function loadChildData(currRowId, measId, childDivId, dashletId,groupId,dbrdId,expand)
            {
                //                alert('load child data method'+groupId);
                var dispRows = $("#viewType").val();
                var currRow = $("#"+currRowId);
                var dispType=$("input[name='groupView-"+dashletId+"']:checked").val();

                var isInitialized = currRow.attr("initialized");
                var childRow = currRow.next();

                $( "#"+childDivId+"prgBar").progressbar({value: 37});
                
                if (!isInitialized){
                    //                    alert('calling action method');

                    $.ajax({type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                        url: "dashboardTemplateAction.do?templateParam2=groupMeasureInitialView&meassId="+measId+"&dbrdId="+dbrdId+"&dashletId="+dashletId+"&rowsToDisp="+dispRows+"&groupId="+groupId+"&groupType="+dispType,
                        success: function(data){
                            $( "#"+childDivId+"prgBar").remove();
                            $("#"+childDivId).html(data);
                            currRow.attr("initialized","true");
                            initCollapser(childDivId);
                        }
                    });

                }
            }

            function initCollapser(divId){
                if (divId == ""){

                    $(".tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true

                    })

                }
                else{
                    $("#"+divId+" > .tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
            }
            function loadChildDrillData(currRowId, dashletId,dbrdId, meassId, childDivId, nextLevel,groupId, expand){
                //                alert('nextLevel:'+nextLevel);
                //                alert('dbrdId'+dbrdId);

                var dispRows = $("#viewType").val();
                var currRow = $("#"+currRowId);
                var isInitialized = currRow.attr("initialized");
                $( "#"+childDivId+"prgBar").progressbar({value: 37});
                var dispType=$("input[name='groupView-"+dashletId+"']:checked").val();
                if (isInitialized && !expand)
                //                    alert('load childdrill data method in '+isInitialized+''+expand);
                    return;
                if (expand){
                    //                    alert('load childdrill data method in '+isInitialized+''+expand);
                    var anchorImg = currRow.find("td.collapsible a");
                    if (anchorImg.hasClass("collapsed")){
                        anchorImg.removeClass("collapsed");
                        anchorImg.addClass("expanded");

                        var childRow = currRow.next();
                        if (childRow.hasClass("expand-child")){
                            childRow.find("td").show();
                        }
                    }
                }
                $.ajax({type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: "dashboardTemplateAction.do?templateParam2=groupMeasureInitialView&meassId="+meassId+"&rowsToDisp="+dispRows+"&dbrdId="+dbrdId+"&nextLevel="+nextLevel+"&dashletId="+dashletId+"&groupId="+groupId+"&groupType="+dispType,
                    success: function(data){
                        $( "#"+childDivId+"prgBar").remove();
                        $("#"+childDivId).html(data);
                        currRow.attr("initialized","true");
                        initCollapser(childDivId);
                    }
                });
            }
            function ViewGroupInMultiple(viewAs)
            {
                $("#"+viewAs).toggle(500);
            }
            function drillToReportForGroup(dbrdId,dashletId,groupId,rootElements,folderId)
            {
                parent.$("#DrillToReportforGroups").dialog('open');
                $.ajax({type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: "GroupMeassureCreation.do?templateParam=drillToReport&groupId="+groupId+"&dbrdId="+dbrdId+"&dashletId="+dashletId+"&folderId="+folderId+"&rootElements="+rootElements,
                    success: function(data){
                        parent.$("#DrillToReportforGroups").html(data);
                    }

            });
            }
            function submiturls1forGroup($ch)
            {
                parent.document.frmParameter.action = $ch;
                parent.document.frmParameter.submit();
            }
            function submitDbrdUrlforGroup(path){
              parent.document.forms.frmParameter.action=path;
    //                alert("path "+path)
              parent.document.forms.frmParameter.submit();
        }
            
        </script>
        
    </body>
</html>
