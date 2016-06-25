<%-- 
    Document   : advancedHtmlSnapshot
    Created on : 5 May, 2011, 6:25:40 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.io.ByteArrayInputStream,org.jdom.Document,org.jdom.input.SAXBuilder,java.io.Reader,prg.db.PbReturnObject,prg.db.Container,java.util.ArrayList,com.progen.datasnapshots.XMLReturnObject,java.sql.Clob,com.progen.datasnapshots.AdvancedHtmlData,com.progen.datasnapshots.DataSnapshot"%>
<% 
String contextPath=request.getContextPath();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Advanced HTML</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
       <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
           <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
         <%
                String themeColor = "blue";
               XMLReturnObject xmlRetObj=(XMLReturnObject) session.getAttribute("xmlRetObj");
                String snapShotName = "";
                DataSnapshot snapShot = (DataSnapshot) session.getAttribute("snapShot");
                AdvancedHtmlData advancedHtmlData=(AdvancedHtmlData)session.getAttribute("advancedHtmlData");
                snapShotName = snapShot.getSnapShotName();
              
                  

        PbReturnObject retObj=xmlRetObj.getRetObj();
        Container container=xmlRetObj.getContainer();
               ArrayList dispLabels=container.getReportParameterNames();
               String reportId=snapShot.getReportId();


                    %>

                       
                 <style type="text/css">
                     table.tablesorter tbody td {
                            background-color: #FFFFFF;
                            border-color: WhiteSmoke;
                            padding: 4px;
                            vertical-align: top;
                        }
                 </style>
        
    </head>
    <body>
        
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
              
        <form name="advancedHtmlForm" method="post" action="" style="width:96%">
             
            <div class="navtitle1" style=" max-width: 100%; cursor: auto; height: 20px;margin-left: 10px">
                <span> <font size="2">Advanced HTML Report - <%=snapShotName%> </font><b> </b></span>
            </div>
                <table align="right"><tr><td>
            <input type="button" value="Back" onclick="javascript:gotoHtmlReportHome()" class="navtitle-hover" style="width:50px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
                    </td></tr></table>
            <br><br>


               <div id="tabParameters" >
                   <br>
                  
                                                   <table width="100%">
                                                       <tr><td width="60%">
                                                       <table>
                                                       <tr>
                                                      <%for(int i=0;i<advancedHtmlData.getDimensionIds().size();i++){%>
                                                      <td>
                                                      <input id="<%=advancedHtmlData.getDimensionIds().get(i)%>" type="checkbox" value="<%=advancedHtmlData.getDimensionNames().get(i)%>" name="selector[]"/>
                                                          <td><%=advancedHtmlData.getDimensionNames().get(i)%></td>
                                                      <%}%>
                                                      <td> <input type="button" value="Go" onclick="getNewPbReturnObject()" class="navtitle-hover" style="width:70px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"/></td>
                                                       </tr></table></td>
                                                       
                                                     
                                                      </tr>
                                                   </table>
                                                      
          
                                                    </div>
        
<br>
<div style="display: none" id="displayDrillSeq">
<table width="100%" >
    <tr><td>

        <table > <tr>
        
        <td>
            <label for="drillSeq" id="drillSeq" style="font-size:12px">Drill Sequence</label>
        </td>
            </tr>
        </table></td>
        <td>
    <table align="right" >
        <tr>
       <td> <input type="button" value="clearFilter" onclick="clearFilter()" class="navtitle-hover" style="width:70px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"/>
<!--      <input type="button" value="getFilter" onclick="getFilterValues()" class="navtitle-hover" style="width:70px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"/>-->
       </td></tr>
    </table></td>
      
    </tr>
</table>
</div>
<table width="100%" style="margin-left:5px"><tr><td>
<label for="reccnt" id="reccnt" style="font-size:12px"> Record Count:</label>
        </td></tr></table>
 <br>
 <table id="ahtmlTable" cellspacing="0" cellpadding="0" border="1px"  style="border-color:#989898;border-style: solid;margin-left: 25px" align="center" width="100%" class="tablesorter"></table>
  
       <div id="dimensionValsDialog" title="Add Measures" style="display:none" >
           <table style="width:100%;height:250px" border="solid black 1px">
                    <tr>
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Measures from below</font></div>
                            <div style="height:250px;overflow-y:scroll">
                                <ul id="myList3" class="filetree treeview-famfamfam">
                                    <ul id="dimensions" ></ul>
                                </ul>
                            </div>
                        </td>
                        <td id="selectedDimValues" width="50%" valign="top">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Measures to here</font></div>
                            <div style="height:250px;overflow-y:auto">
                                <ul id="sortable">

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
                            <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveDimensionValues()">
                        </td>
                    </tr>
                </table>
        </div>


        </form>
  <script type="text/javascript">
                             var grpColArray=new Array();
                             $(document).ready(function(){
                       $("#dimensionValsDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'justify',
                        modal: true

                    });
                   
                      $("#ahtmlTable").tablesorter();

                      var viewbyList = [];

                viewbyList[0] ='<%=advancedHtmlData.getDimensionIds().get(0)%>';
                $("#"+'<%=advancedHtmlData.getDimensionIds().get(0)%>').attr('checked', 'true');

         $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getNewPbReturnObject&viewbyList='+viewbyList, $("#advancedHtmlForm").serialize() ,
                 function(data)
                 {
                         var drilldata=data.split("||");
                       $("#ahtmlTable").html(drilldata[0]);
                       $("#reccnt").html("Record Count:"+drilldata[1]);
                         $("#ahtmlTable").tablesorter()
                 })

                      
                        });

             function dispParameters(){
                if(document.getElementById("tabParameters").style.display=="none"){
                    document.getElementById("tabParameters").style.display = "block";

                }else{
                    document.getElementById("tabParameters").style.display = "none";

                }
            }

//            function getNewPbReturnObject(id)
//            {
//                var drillValue=$("#"+id).val();
//
//
//               if($('#'+id).attr('checked'))
//                    {
//
//                 $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getNewPbReturnObject&id='+id, $("#advancedHtmlForm").serialize() ,
//                 function(data)
//                 {
//
//                       $("#ahtmlTable").html(data);
//                         $("#ahtmlTable").tablesorter({headers : {0:{sorter:false}}})
//                 })
//
//                }
//                else{
//                   $("#ahtmlTable").html("");
//
//                }
//            }
            function drilldown(value,elementId)
            {
                var viewbyList = [];
                $(':checkbox:checked').each(function(i){
                viewbyList[i] = $(this).attr('id');
                });
       
                $.ajax({
                     url:'<%=request.getContextPath()%>/dataSnapshot.do?doAction=getDrillMapForSnapshot&drillValue='+value+'&reportId='+'<%=reportId%>'+'&elementId='+elementId+"&viewbyList="+viewbyList,
                    success:function(data)
                    {
//                        alert(data);
                         var drilldata=data.split("||");
//                          alert(drilldata[1]);
                         if(drilldata[0]!="")
                         $("#ahtmlTable").html(drilldata[0]);
                         drilldata[2]=drilldata[2].replace('{','');
                         drilldata[2]=drilldata[2].replace('}','');
                         var drillValues=drilldata[2].split(',')
                         var dimNames="";
                         var actDimName;
//                         alert(drilldata[2]);
                         for(var i=0;i<drillValues.length;i++)
                            {
                                actDimName="";
                                var dimName=drillValues[i].split("=");
                                var dId=dimName[0]
                                var dName=dimName[1];
                                actDimName=$("#"+dId.trim()).val();
                                dimNames=dimNames+actDimName+":"+dName
                                if(i!=drillValues.length-1)
                                    dimNames=dimNames+" , "
                            }
                            $("INPUT[type='checkbox']").attr('checked', false);
                            $("#"+drilldata[3]).attr('checked',true);
                            $("#drillSeq").html("Drill Sequence:  "+dimNames);
                             $("#reccnt").html(" Record Count: "+drilldata[1]);
                             $("#displayDrillSeq").attr("style", "display:block");
                               $("#ahtmlTable").tablesorter()

                    }
                })


            }
            function clearFilter()
            {
                 var viewbyList = [];
                $(':checkbox:checked').each(function(i){
                viewbyList[i] = $(this).attr('id');
                });
                $.ajax({
                    url:'<%=request.getContextPath()%>/dataSnapshot.do?doAction=clearFilter&viewbyList='+viewbyList,
                   success:function(data)
                   {
                       var drilldata=data.split("||");
                       $("#ahtmlTable").html(drilldata[0]);
                       $("#reccnt").html("Record Count:"+drilldata[1]);
                   }
                })
                 $("#displayDrillSeq").attr("style", "display:none");
            }
            function getFilterValues()
            {
                  $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getDimensionValues', $("#advancedHtmlForm").serialize() ,
                 function(data)
                 {
                     $("#dimensions").html(data);
                           $("#myList3").treeview({
                    animated:"slow"

                    //persist: "cookie"
                });
                $("#sortable").sortable();
                  var dragMeasure=$('#dimensions > li > ul > li > ul > li > span,#dimensions > li > ul > li >  span')
                var dropMeasures=$('#selectedDimValues');

                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropMeasures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#dimensions > li > ul > li > ul > li > span,#dimensions > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                });
                 $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                }
            );


                 })
                $("#dimensionValsDialog").dialog('open');
            }

            function gotoHtmlReportHome(){
                document.forms.advancedHtmlForm.action = "<%=request.getContextPath()%>/home.jsp#Html_Reports";
                document.forms.advancedHtmlForm.submit();
            }
          function getNewPbReturnObject() {

    var viewbyList = [];
    $(':checkbox:checked').each(function(i){
        viewbyList[i] = $(this).attr('id');
         });
         $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getNewPbReturnObject&viewbyList='+viewbyList, $("#advancedHtmlForm").serialize() ,
                 function(data)
                 {
                         var drilldata=data.split("||");
                       $("#ahtmlTable").html(drilldata[0]);
                       $("#reccnt").html("Record Count:"+drilldata[1]);
                         $("#ahtmlTable").tablesorter()
                 })
   

}
             function createColumn(elmntId,elementName,tarLoc){

                var parentUL=document.getElementById(tarLoc);
                var x=grpColArray.toString();
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);
                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;
                    childLI.style.width='220px';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.style.align='center';
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
        }
      function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);
                var x=index.replace("GrpCol","");
                var i=0;

                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==x){
                        grpColArray.splice(i,1);
                    }

                }
            }
            function saveDimensionValues()
            {
                 var cols="";

                var colsUl=document.getElementById("sortable");
                if(colsUl!=undefined || colsUl!=null)
                {
                    var colIds=colsUl.getElementsByTagName("li");

                    if(colIds!=null && colIds.length!=0)
                    {
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                        }

                         $("#measuresDiv").dialog('close');
                    }
                    else
                    {
                    alert("Please select atleast one Measure ");


                    }
                }
               $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getDrillMapForSnapshot&drillValue='+value+'&reportId='+'<%=reportId%>'+'&elementId='+elementId+"&viewbyList="+viewbyList, $("#advancedHtmlForm").serialize() ,
                 function(data)
                 {


                 })

            }
            </script>
    </body>
</html>
<!--border='1' style="border-collapse:separate;border-spacing:0px;-moz-border-radius: 6px 6px 6px 6px;" CELLPADDING="0" CELLSPACING="1"-->