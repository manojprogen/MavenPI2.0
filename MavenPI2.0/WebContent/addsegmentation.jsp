<%--

--%>
<!--start by etta-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
%>

<html>
    <head>


        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
          <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
<!--        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />-->





        <title>JSP Page</title>


        <style type="text/css">
            *{
                 font-size: 12px;
            }
            .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: black;
                padding-left:12px;
                padding-top:5px;
                border:0px;
            }
            #ui-datepicker-div
            {
                z-index: 9999999;
            }
        </style>
        <script type="text/javascript">
            var groupmembers=""
            var rules=""
            var groupValues=""
            var tablename=""
            var DependColumnlist=""
            var BaseColumnList=""

            $(document).ready(function(){
                $.get('<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=getSegmentationNames',function(data){
                    var jsonVar=eval('('+data+')')
                    var segmentid=jsonVar.segmentid
                    var conid=jsonVar.conid
                    var relationname=jsonVar.relationname
                    groupmembers=jsonVar.groupmemberlist
                    rules=jsonVar.rulesList
                    groupValues=jsonVar.groupValues
                    tablenames=jsonVar.tablenamesList
                    DependColumnlist=jsonVar.DependColumnlist
                    BaseColumnList=jsonVar.BaseColumnList
                    var htmlVar=""
                    for(var i=0;i<BaseColumnList.length;i++)
                    {
                           htmlVar+="<tr><td><input type='text' name='' value='"+relationname[i]+"' readonly id='relationId"+i+"'/><input type='hidden' name='' value='"+segmentid[i]+"' readonly id='segmentid"+i+"'/></td>\n\
                                            <td><input type='text' name='' value='"+tablenames[i]+"' readonly id='tablenamesId"+i+"'/><input type='hidden' name='' value='"+conid[i]+"' readonly id='conid"+i+"'/></td>\n\
                                            <td><input type='text' name='' value='"+DependColumnlist[i]+"' readonly id='DependColumnId"+i+"' /></td>\n\
                                            <td><input type='text' name='' value='"+BaseColumnList[i]+"' readonly id='BaseColumnId"+i+"'/></td>\n\
                                            <td><img border='0' align='middle' title='edit' onclick='editsegvalues("+i+")' alt='' src='/pi/images/editList.gif'></td>\n\
                                            <td><input type='submit'  value='Delete' onclick='editRelations("+i+")' class='migrate' style='height: 20px; width: 110px'></td></tr>"
                      
                       
                       
                    
                    }
                    $("#segmentationTabBody").html(htmlVar)

                });
                if ($.browser.msie == true){
                    $("#basecoldivid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#membrvaldivid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#segmentCreation").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#crategroupsid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#editgroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 900,
                        position: 'justify',
                        modal: true
                    });
                    $("#createdatedivid").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#basecoldivid1").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $("#basecoldivid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#membrvaldivid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#segmentCreation").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#crategroupsid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#editgroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 900,
                        position: 'justify',
                        modal: true
                    });
                    $("#createdatedivid").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#basecoldivid1").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                }
            });
        </script>
        <script type="text/javascript">
            var extposition="";
            var id;
            var rowCount="";
            var conid="";
            var tablename3="";
            var basecolumn="";
            var dependentcolumn="";
            var relation="";
            var segmentid="";
            $(document).ready(function()
            {
                $.get("<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=getConnectionNames",function(data){
                    var jsonVar=eval('('+data+')')
                    var connids=jsonVar.connids
                    var connames=jsonVar.connnames
                    var htmlVar=""
                    var jsonvaleuse=""
                    htmlVar+="<option  value=''>--SELECT--</option>"
                    for(var i=0;i<connids.length;i++)
                    {
                        jsonvaleuse=connames[i]

                        htmlVar+="<option  value='"+connids[i]+"'>"+jsonvaleuse+"</option>"
                    }
                    $("#connnames").html(htmlVar)
                });
                $('#startdate1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat:'yy-mm-dd'
                    
                });
                $('#enddate1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat:'yy-mm-dd'
                });
            });
            function getTables()
            {
                var conId=$("#connnames").val();
                $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getDisplayTableNames&conId='+conId,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        var tabNames=jsonVar.tablenames
                        var htmlVar=""
                        var jsonvaleuse=""
                        htmlVar+="<option  value=''>--SELECT--</option>"
                        for(var i=0;i<tabNames.length;i++)
                        {
                            jsonvaleuse=tabNames[i]
                            htmlVar+="<option  value='"+tabNames[i]+"'>"+jsonvaleuse+"</option>"
                        }
                        $("#tableid").html(htmlVar);
                    }
                });
            }
            function editsegvalues(id)
            {
                conid=$("#conid"+id).val()
                relation=$("#relationId"+id).val()
                tablename3=$("#tablenamesId"+id).val()
                dependentcolumn=$("#DependColumnId"+id).val()
                basecolumn=$("#BaseColumnId"+id).val()
                segmentid=$("#segmentid"+id).val()
                var count=$('#segmntTableId tr').length
                var gromember=groupmembers[id]
                var rule=rules[id]
                var groupValue=groupValues[id]
                $("#segmntTableId").find("tr:gt(0)").remove();
                var tempvalues=groupValue.substr(1,groupValue.length-2);
                var grpvalues=tempvalues.split(",");
               
                for(var i=0;i<grpvalues.length;i++){
                    $("#segmntTableId").append("<tr><td><input type='text' name='membergrp'  style='width:120px' id='membergrpid"+(i+1)+"' value='"+gromember+"'/></td>\n\
                <td><select id='selectid"+(i+1)+"' class='grpMembrsid' name='selectedroles'><option value='IN'>"+rule+"</option></select></td>\n\
                <td><input type='text' name='membervalues'  style='width:120px' id='membervalueid"+(i+1)+"' value='"+grpvalues[i]+"' onclick='segmntMemCols(this.id)'/></td></tr>")
                }
                $("#basecoldivid").dialog('open')
            }
            function getColumns()
            {
                var connectionID=$("#connnames").val()
                var tabname=$("#tableid").val();
                $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getTableColumns&tabname='+tabname+'&connectionID='+connectionID,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        var colNames=jsonVar.columnnames
                        var htmlVar=""
                        var jsonvaleuse=""
                        htmlVar+="<option  value=''>--SELECT--</option>"
                        for(var i=0;i<colNames.length;i++)
                        {
                            jsonvaleuse=colNames[i]
                            htmlVar+="<option  value='"+colNames[i]+"'>"+jsonvaleuse+"</option>"
                        }
                        $("#columnsid").html(htmlVar);
                        $("#basecolumnid").html(htmlVar);
                    }
                });
            }
            function segCol(){
                $("#membergrpid1").val('')
                $("#membervalueid1").val('')
                $("#segmntTableId").find("tr:gt(1)").remove();
                $("#basecoldivid").dialog('open');
            }
            function closebutt(){
                $("#basecoldivid").dialog('close');
            }
            function segmntMemCols(sid){
                id=sid
                var tempID=sid.toString().replace("membervalueid","selectid","gi")
                if($("#"+tempID).val()!="LIKE"){
                    var segcolumns=$("#basecolumnid").val()
                    var connectionId=$("#connnames").val()
                    var tablename=$("#tableid").val();
               
                    if(connectionId!="")
                    {
                        $.ajax({
                            url: 'targetmeasuresaction.do?targetMeasures=getSegmentColumns&tablename='+tablename+'&segcolumns='+segcolumns+'&connectionId='+connectionId,
                            success: function(data){
                                var jsonVar=eval('('+data+')')
                                $("#htmsdrgndrpid").html("")
                                $("#htmsdrgndrpid").html(jsonVar.htmlStr);
                                $("#membrvaldivid").dialog('open');
                                isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
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
                                $(".myDragTabs").draggable({

                                    helper:"clone",
                                    effect:["", "fade"]
                                });
                                $("#dropTabs").droppable({
                                    activeClass:"blueBorder",
                                    accept:'.myDragTabs',
                                    drop: function(ev, ui) {
                                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                                    }
                                }
                            );
                                //                        grpColArray=jsonVar.memberValues
                                $(".sortable").sortable();
                            }
                        })
                        //                $("#membrvaldivid").dialog('open');
                    }
                    else
                    {
                        var connectionId=conid
                        var tablename=tablename3
                        var segcolumns =basecolumn
                        var membervalue=$("#"+sid).val();
                 
                        $.ajax({
                            url: 'targetmeasuresaction.do?targetMeasures=getSegmentColumns&tablename='+tablename+'&segcolumns='+segcolumns+'&connectionId='+connectionId+'&membervalue='+membervalue,
                            success: function(data){
                                var jsonVar=eval('('+data+')')
                                $("#htmsdrgndrpid").html("")
                                $("#htmsdrgndrpid").html(jsonVar.htmlStr);
                                $("#membrvaldivid").dialog('open');
                                isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
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
                                $(".myDragTabs").draggable({

                                    helper:"clone",
                                    effect:["", "fade"]
                                });
                                $("#dropTabs").droppable({
                                    activeClass:"blueBorder",
                                    accept:'.myDragTabs',
                                    drop: function(ev, ui) {
                                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                                    }
                                }
                            );
                                //                        grpColArray=jsonVar.memberValues
                                $(".sortable").sortable();
                            }
                        })
                    }
                }
            }
            function saveGrpMems()
            {
               
                var valListUl=document.getElementById("sortable");
                var valListUlIds=valListUl.getElementsByTagName("li");
                var valList=[];
                for(var i=0;i<valListUlIds.length;i++){
                    valList.push(valListUlIds[i].id.split("_li")[0])
                    $("#"+id).val(valList);
                }
                $("#membrvaldivid").dialog('close')
            }
            function cancelGrpMems(){
                $("#membrvaldivid").dialog('close')
            }
            function selectMemVals(){
                var selval= $("#selectmemsid").val()
                if(selval=="LIKE"){
                    $("#likeId1").show()
                    $("#membervalueid1").hide()
                }
                else{
                    $("#membervalueid1").show()
                    $("#likeId1").hide()
                }
            }
            function saveGrpMemVals(){
                var relationName=$("#relationname").val()
                var connId=$("#connnames").val()
                var tableName=$("#tableid").val()
                var dependColumn=$("#columnsid").val()
                var baseColumn=$("#basecolumnid").val()
                if(connId!="")
                {
                    $.post('targetmeasuresaction.do?targetMeasures=saveSegmentationValues&relationName='+relationName+'&connId='+connId+'&tableName='+tableName+'&dependColumn='+dependColumn+'&baseColumn='+baseColumn, $("#columnForm").serialize(),
                    function(data)
                    {
                    });
                    <%--$("#membrvaldivid").dialog('close')--%>
                }
                else
                {
                    var tablename=tablename3
                    var dependentcolumnname=dependentcolumn
                    var basecolumnname=basecolumn
                    var connectionId=conid
                    $.post('targetmeasuresaction.do?targetMeasures=updateSegmentationValues&tableName='+tablename+'&dependColumn='+dependentcolumnname+'&baseColumn='+basecolumnname+'&conid='+connectionId, $("#columnForm").serialize(),
                    function(data)
                    {
                    });
                }
               $("#basecoldivid").dialog('close')
                
            }
            function addRow()
            {
                rowCount=$('#segmntTableId tr').length
                $("#segmntTableId").append("<tr><td><input type='text' name='membergrp'  style='width:120px' id='membergrpid"+rowCount+"'/></td>\n\
           <td><select id='selectid"+rowCount+"' class='grpMembrsid' name='selectedroles'><option value='IN'>IN</option><option value='LIKE'>LIKE<option></select></td>\n\
           <td><input type='text' name='membervalues' maxlength='255' style='width: 120px;' onclick='segmntMemCols(this.id)'id='membervalueid"+rowCount+"' </td>\n\
           <td><input type='text' name='textid'  style='width:120px;display: none' id='likeId1'/> </td></tr>")
                $("#grpMembrsid"+rowCount).html($("#grpMembrsid1").html())
            }
            function deleteParentRow(tableID)
            {
                var tablename=tablename3
                var connectionId=conid
                var dependentcolumnname=dependentcolumn
                var basecolumnname=basecolumn
                var relationname=relation
                var segmentids= segmentid
                    
                try {
                    var table =tableID ;
                    var rowCount = table.rows.length;
                    if(rowCount > 2) {
                   
                        var membergrpid=$("#membergrpid"+(rowCount-1)).val()
                        var membervalueid=$("#membervalueid"+(rowCount-1)).val()
                       
                        $.post('targetmeasuresaction.do?targetMeasures=deleteSegmentationValues&connId='+connectionId+'&tableName='+tablename+'&basecolumnname='+basecolumnname+'&dependentcolumnname='+dependentcolumnname+'&membergrpid='+membergrpid+'&membervalueid='+membervalueid+'&relation='+relationname+'&segmentid='+segmentids, $("#columnForm").serialize(),
                        function(data)
                        {
                        });
                        table.deleteRow(rowCount - 1);
                         
                    }
                }
                catch(e) {
                    alert(e);
                }
            }
            function editRelations(id)
            {
                var segmentid=$("#segmentid"+id).val()
                var relationId=$("#relationId"+id).val()
                var DependColumn=$("#DependColumnId"+id).val()
                alert('DependColumn---'+DependColumn)
                $.post('targetmeasuresaction.do?targetMeasures=editRelations&segmentid='+segmentid+'&relationId='+relationId, $("#columnForm").serialize(),
                function(data)
                {
                });
            }
            function creategroups()
            { 
                $("#crategroupsid").dialog('open');
            }
            function editgroups()
            {
                $("#editgroupsid").dialog('open');
            }
            function CreateDate()
            {
                $.post("<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=getConnectionNames",function(data){
                   
                    var jsonVar=eval('('+data+')')
                    var connids=jsonVar.connids
                    var connames=jsonVar.connnames
                    var htmlVar=""
                    var jsonvaleuse=""
                    htmlVar+="<option  value=''>--SELECT--</option>"
                    for(var i=0;i<connids.length;i++)
                    {
                        jsonvaleuse=connames[i]

                        htmlVar+="<option  value='"+connids[i]+"'>"+jsonvaleuse+"</option>"
                    }
                    $("#connnamesid").html(htmlVar)
                     
                });
                $("#createdatedivid").dialog('open');
            }
            function getRelatedTables()
            {
                var conId=$("#connnamesid").val();
                $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getDisplayTableNames&conId='+conId,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        var tabNames=jsonVar.tablenames
                        var htmlVar=""
                        var jsonvaleuse=""
                        htmlVar+="<option  value=''>--SELECT--</option>"
                        for(var i=0;i<tabNames.length;i++)
                        {
                            jsonvaleuse=tabNames[i]
                            htmlVar+="<option  value='"+tabNames[i]+"'>"+jsonvaleuse+"</option>"
                        }
                        $("#tableid1").html(htmlVar);
                    }
                });
            }
            function getColumnNames()
            {
                var connectionID=$("#connnamesid").val()
                var tabname=$("#tableid1").val();
                $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getTableColumns&tabname='+tabname+'&connectionID='+connectionID,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        var colNames=jsonVar.columnnames
                        var htmlVar=""
                        var jsonvaleuse=""
                        htmlVar+="<option  value=''>--SELECT--</option>"
                        for(var i=0;i<colNames.length;i++)
                        {
                            jsonvaleuse=colNames[i]
                            htmlVar+="<option  value='"+colNames[i]+"'>"+jsonvaleuse+"</option>"
                        }
                        $("#columnsid1").html(htmlVar);
                        $("#basecolumnid1").html(htmlVar);
                    }
                });
                
            }
            <%-- function getMembervalues(sid)
             {
                id=sid
                  var tempID=sid.toString().replace("membervalueid","selectid","gi")
                  if($("#"+tempID).val()!="LIKE"){
                 var segcolumns=$("#basecolumnid1").val()
                 var connectionId=$("#connnamesid").val()
                 var tablename=$("#tableid1").val();
                 $.ajax({
                     url: 'targetmeasuresaction.do?targetMeasures=getSegmentColumns&tablename='+tablename+'&segcolumns='+segcolumns+'&connectionId='+connectionId,
                     success: function(data){
                         var jsonVar=eval('('+data+')')
                         $("#htmsdrgndrpid").html("")
                         $("#htmsdrgndrpid").html(jsonVar.htmlStr);
                         $("#membrvaldivid").dialog('open');
                         isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
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
                         $(".myDragTabs").draggable({

                            helper:"clone",
                            effect:["", "fade"]
                        });
                        $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
                    );
                        //                        grpColArray=jsonVar.memberValues
                        $(".sortable").sortable();
                    }
                })
                 }
                 }--%>
                     function segColnext()
                     {
                         $("#membergrpid1").val('')
                         $("#membervalueid1").val('')
                         $("#segmntTableId1").find("tr:gt(1)").remove();
                    
                         $("#basecoldivid1").dialog('open');
                     }
                     function getDatePicker(id)
                     {
                         $("#"+id).datepicker({
                             changeMonth: true,
                             changeYear: true,
                             dateFormat:'yy-mm-dd'

                         });
                     }
                     function ExtraRow()
                     {
                         var Count=$('#segmntTableId1 tr').length
                         alert('Count---'+Count)
                         $("#segmntTableId1").append("<tr><td><input type='text' name='Seasonvalues'  style='width:120px' id='Seasonvalue"+Count+"'/></td>\n\
           <td><input type='text' name='startdate' maxlength='255' style='width: 120px;' id='startdate"+Count+"' onclick='getDatePicker(this.id)' /></td>\n\
           <td><input type='text' name='enddate' maxlength='255' style='width: 120px;' id='enddate"+Count+"' onclick='getDatePicker(this.id)' /></td></tr>")
                    
                     }
                     function saveDate()
                     {
                         var connId=$("#connnamesid").val()
                         var tablename=$("#tableid1").val()
                         var dependentcolumn=$("#columnsid1").val()
                         var basecolumn=$("#basecolumnid1").val()
                         $.post('targetmeasuresaction.do?targetMeasures=updateDate&connId='+connId+'&tablename='+tablename+'&dependentcolumn='+dependentcolumn+'&basecolumn='+basecolumn, $("#DateForm").serialize(),
                         function(data)
                         {
                         });



                     }


                     //            function dependColGrpMembrs(){
                     //                var connectionId=$("#connnames").val()
                     //                var tablename=$("#tableid").val();
                     //                var dependCols=$("#columnsid").val()
                     //                $.ajax({
                     //                    url: 'targetmeasuresaction.do?targetMeasures=getDependColumns&tablename='+tablename+'&dependCols='+dependCols+'&connectionId='+connectionId,
                     //                    success: function(data){
                     //                        var jsonVar=eval('('+data+')')
                     //
                     //
                     //                        var    dependColNames=jsonVar.dependcolnames
                     //
                     //                        var htmlVar=""
                     //
                     //                        var jsonvaleuse=""
                     //                        htmlVar+="<option  value=''>--SELECT--</option>"
                     //                        for(var i=0;i<dependColNames.length;i++)
                     //                        {
                     //                            jsonvaleuse=dependColNames[i]
                     //
                     //                            htmlVar+="<option  value='"+dependColNames[i]+"'>"+jsonvaleuse+"</option>"
                     //
                     //                        }
                     //
                     //                        $("#grpMembrsid1").html(htmlVar);
                     //
                     //                    }
                     //                });
                     //            }

        </script>

    </head>
    <body>


        <div id ="" title="" style=" height: 400px; border-width: 4px; border-style: groove; margin-top: auto; margin-left: 100px; margin-right: 100px; margin-bottom: 100px; margin: 80px 10px 10px 10px; padding-bottom: 80px; padding-left: 15px; padding-top: 50px;">
            <table align="left">
                <tr>
                    <td><input type="button" class="migrate  themeColor" name="creategroup" value="Create Group"  onclick="creategroups()" style="height: 20px; width: 100px"/></td>
                </tr>
                <tr>
                    <td><input type="button" class="migrate  themeColor" name="editgroup" value="Edit Group" onclick="editgroups()" style="height: 20px; width: 100px"/></td>
                </tr>
                <tr>
                    <%--<td><input type="button" class="migrate  themeColor" name="Createdate" value="Create Date" onclick="CreateDate()" style="height: 20px; width: 100px"/></td>--%>
                </tr>
            </table>
            <form>
                <div id="crategroupsid">
                    <table height:400px width:400px align="left">
                        <tr>
                            <td class="migrate themeColor"><b>Define Relation</b></td>
                            <td> <input type="text" name="relationname" value="" id="relationname"/></td>
                        </tr>

                        <tr>
                            <td class="migrate themeColor"><b> Connection Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="connnames" name="connnames" style="width:auto" onchange="getTables()">
                                    <option>---SELECT---</option>

                                </select></td>
                        </tr>

                        <tr>
                            <td class="migrate themeColor"><b>Table Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="tableid" name="tableid" style="width:auto" onchange="getColumns()">


                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td class="migrate themeColor" ><b>Depend Column </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="columnsid"  style="width:auto"><option>---SELECT---</option>

                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="migrate themeColor"><b>Base Column </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="basecolumnid" style="width:auto"><option>---SELECT---</option>

                                </select>
                            </td>
                        </tr><br/>
                        <tr>
                        <center> <td>
                                <input type="button" onclick="segCol()"  value="Next" class="migrate  themeColor"/>
                            </td></center>

                        </tr>
                    </table></div>
                <div id="editgroupsid">
                    <table id="segmentationId" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="1" align="right" style="width: 100%;">
                        <thead>
                            <tr>
                                <th class="migrate themeColor">RelationName</th>
                                <th class="migrate themeColor">TableName</th>
                                <th class="migrate themeColor">Depend Column</th>
                                <th class="migrate themeColor">Base Column</th>
                                <th class="migrate themeColor">Edit</th>
                                <th class="migrate themeColor">Delete Relation</th>
                            </tr>
                        </thead>
                        <tbody id="segmentationTabBody">
                        </tbody>
                    </table></form></div><br><br>

    </table><br><br>
</div>
<div id="basecoldivid" style="display: none">
    <form name="columnForm" id="columnForm" method="post" action="">
        <table id="segmntTableId" width="">
            <tr>
                <th  class="migrate themeColor"> Group Members</th>
                <th></th>
                <th class="migrate themeColor" width="">  Member Values</th>
            </tr>
            <tr>
                <td>
                    <input type="text"  id="membergrpid1" name="membergrp" class="myTextbox" style="width:120px" maxlength="255" value="" />
                </td>
                <td><select id="selectid1" name="selectedroles">
                        <option value="IN">IN</option>
                        <option value="LIKE">LIKE</option>
                    </select></td>
                <td><input type="text"  id="membervalueid1" name="membervalues" class="myTextbox" style="width:120px" maxlength="255" value="" onclick="segmntMemCols(this.id)">

                </td>
            </tr>
        </table>
        <br/><br/>
        <input type="button" id="buttonSaveId" value="Save" name="Save" onclick="saveGrpMemVals()" class="migrate  themeColor">
        <input type="button" value="Add Row" id="addButtonId" onclick="addRow()"  name="" class="migrate  themeColor" />
        <input type="button" value="Delete Row" id="deleteButtonId" onclick="deleteParentRow(segmntTableId)"  name="" class="migrate  themeColor" />

        <input type="button" id="buttonId" value="Close" onclick="closebutt()" class="migrate  themeColor">
    </form>
</div>
<div id="membrvaldivid" style="display: none">
    <table width="100%">
        <tr>
            <td id="htmsdrgndrpid"></td>
        </tr>
        <tr>
            <td align="center"> <input type="button" value="Save" name="Save" onclick="saveGrpMems(this.id)" class="migrate  themeColor"/>
                <input type="button" value="Cancel" name="Cancel" onclick="cancelGrpMems()" class="migrate  themeColor"/>
            </td>
        </tr>
    </table>
</div>
<div id="createdatedivid">
    <table height:400px width:400px align="center">
        <tr>
            <td class="migrate  themeColor"><b> Connection Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="connnamesid" name="connnames" style="width:auto" onchange="getRelatedTables()">
                    <option>---SELECT---</option>

                </select></td>
        </tr>

        <tr>
            <td class="migrate  themeColor"><b>Table Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="tableid1" name="tableid" style="width:auto" onchange="getColumnNames()">


                </select>
            </td>
        </tr>

        <tr>
            <td class="migrate  themeColor"><b>Depend Column </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="columnsid1"  style="width:auto"><option>---SELECT---</option>

                </select>
            </td>
        </tr>
        <tr>
            <td class="migrate  themeColor"><b>Base Column </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="basecolumnid1" style="width:auto"><option>---SELECT---</option>

                </select>
            </td>
        </tr><br/>
        <tr>
        <center> <td>
                <input type="button" onclick="segColnext()"  value="Next" class="migrate  themeColor"/>
            </td></center>

        </tr>
    </table>
</div>
<div id="basecoldivid1" style="display: none">
    <form name="columnForm" id="DateForm" method="post" action="">
        <table id="segmntTableId1" width="">
            <thead>
                <tr>
                    <th class="migrate  themeColor">Season</th>
                    <th class="migrate  themeColor">Start Date</th>
                    <th class="migrate  themeColor">End Date</th>
                </tr>
            </thead>
            <tbody id="DateTabBody">
                <tr>
                    <td><input type="text"  id="Seasonvalue1" name="Seasonvalues" class="myTextbox" style="width:120px" maxlength="255" value="" /></td>
                    <td><input type="text"  id="startdate1" name="startdate" class="myTextbox" style="width:120px" maxlength="255" value=""/></td>
                    <td><input type="text"  id="enddate1" name="enddate" class="myTextbox" style="width:120px" maxlength="255" value=""/></td>
                </tr>
               
            </tbody>
        </table>
        <br/><br/>
        <input type="submit" id="buttonSaveId" value="Save" name="Save" onclick="saveDate()" class="migrate  themeColor">
        <input type="button" value="Add Row" id="addButtonId" onclick="ExtraRow()"  name="" class="migrate  themeColor" />
        <input type="button" value="Delete Row" id="deleteButtonId" onclick="deleteParentRow(segmntTableId)"  name="" class="migrate  themeColor" />

        <input type="button" id="buttonId" value="Close" onclick="closebutt()" class="migrate  themeColor">
    </form>
</div>

</body>
</html>

<!--end by etta-->







