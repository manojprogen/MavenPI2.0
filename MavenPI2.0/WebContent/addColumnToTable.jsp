<%-- 
    Document   : addColumnToTable
    Created on : 15 Oct, 2010, 4:11:40 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String tableId = request.getParameter("tableId");
            String tablename = request.getParameter("tableName");
            String flag = request.getParameter("flag");

%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery-impromptu.3.1.min.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->
<link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/ReportCss.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.slider.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/global.css"/>


<script type="text/javascript">
    var custnamesArray=new Array
    var custDBcolsLengths=new Array
    var cusColType=new Array
            
    $(document).ready(function(){
        
        var tableID='<%=tableId%>';
        var tableName='<%=tablename%>'
        if(tableName != "" ){
           
            $.get('<%= request.getContextPath()%>/editconn.do?parameter=addColumnToTable&tableId='+tableID+'&tableName='+tableName, function(data){
              
                var dataJson=eval("("+data+")")

                for(var i=0;i<dataJson.columnsNamesOrgDb.length;i++){
                    createColumn("orgDB_"+dataJson.columnsNamesOrgDb[i],"selectedMeasuresUL",dataJson.columnsNamesOrgDb[i])
                }
                for(var j=0;j<dataJson.custDbColumns.length;j++){

                    createColumn("custDB_"+dataJson.custDbColumns[j],"MeasuresUL",dataJson.custDbColumns[j])
                    custnamesArray.push(dataJson.custDbColumns[j]);
                    custDBcolsLengths.push(dataJson.custdataLength[j])
                    cusColType.push(dataJson.custDatatype[j])
                }

            });
        }else{
            $.get('<%= request.getContextPath()%>/getAllBusinessGroups.do?method=getBussGrpDetails&bussTableId='+tableID, function(data){

                var dataJson=eval("("+data+")")
                $("#next").hide();
                $("#save").show();
                for(var i=0;i<dataJson.bussColNames.length;i++){
                    createColumn("orgDB_"+dataJson.bussColIds[i],"selectedMeasuresUL",dataJson.bussColNames[i])
                }
               

            });
        }
       
    });
    $(document).ready(function() {

        $(".navtitle-hover").draggable({
            helper:"clone",
            effect:["", "fade"]
        });
        $(".titlehover").draggable({
            helper:"clone",
            effect:["", "fade"]
        });
                    
        $("#MeasuresDIV").droppable({

            activeClass:"blueBorder",
            accept:'.titlehover',
            drop: function(ev, ui) {
                var check=false;
                var dropDivID=this.id;
                var dropDivobj =document.getElementById(dropDivID) ;
                var dropUlobj= dropDivobj.getElementsByTagName("ul");
                var dropUlID= dropUlobj[0].getAttribute("id");
                var draggableLIid=ui.draggable.attr('id');
                var liObjs = document.getElementById(draggableLIid);
                var tableObjs = liObjs.getElementsByTagName("table");
                var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                var tdObjs =  trObjs[0].getElementsByTagName("td");
                var content = tdObjs[0].innerHTML;
                var draggableID =ui.draggable.attr('id')
                for(var i=0;i<custnamesArray.length;i++){
                    var tempvar=draggableLIid;
                    tempvar=tempvar.replace("orgDB_","","gi")
                    if(tempvar==custnamesArray[i]){
                        check=true;
                        break;

                    }
                }
                if(custnamesArray.length==0){
                    check=true;
                }
                          
                if(check){
                    createColumn(draggableLIid,"MeasuresUL",content);
                    deleteLI(draggableID);

                }}
        });


        $("#selectedMeasuresDIV").droppable({

            activeClass:"blueBorder",
            accept:'.navtitle-hover',

            drop: function(ev, ui) {
                // alert("indddd2")
                var dropDivID=this.id;
                var dropDivobj =document.getElementById(dropDivID) ;
                var dropUlobj= dropDivobj.getElementsByTagName("ul");
                var dropUlID= dropUlobj[0].getAttribute("id");
                var draggableLIid=ui.draggable.attr('id');

                var liObjs = document.getElementById(draggableLIid);
                var tableObjs = liObjs.getElementsByTagName("table");
                var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                var tdObjs =  trObjs[0].getElementsByTagName("td");
                var content = tdObjs[0].innerHTML;
                var draggableID =ui.draggable.attr('id')
                createColumn(draggableLIid,"selectedMeasuresUL",content);
                deleteLI(draggableID);
            }
        });

    });

    function createColumn(elmntId,tarLoc,content){
        var parentUL=document.getElementById(tarLoc);

        var childLI=document.createElement("li");
                    
        childLI.style.width='150px';
        childLI.style.height='18px';
        childLI.style.color='black';
        //class="navtitle-hover"
        if(tarLoc=='selectedMeasuresUL'){
            //custDB_,orgDB_
            childLI.id=elmntId.replace("custDB_", "orgDB_","gi");
            childLI.className='titlehover ui-draggable';
        }else{
            childLI.id=elmntId.replace("orgDB_", "custDB_","gi");
            childLI.className='navtitle-hover ui-draggable';
        }
        var tableStr="<table width='100%'>";
        tableStr+="<tbody>";
        tableStr+="<tr valign='top' align='center'>";
        tableStr+="<td width='70%' align='left'><font style='color:black,font-size:15px'>"+content+"</font></td>";
        tableStr+="</tr>";
        tableStr+="</tbody>";
        tableStr+="</table>";
                     
        childLI.innerHTML=tableStr;
        parentUL.appendChild(childLI);
        $(".sortable").sortable();
                
    }
    function dropDiv(dropId){
        var dropId = dropId;
    }
               
    function deleteLI(liID){
        var LiObj=document.getElementById(liID);
        try{

            var parentUL=document.getElementById(LiObj.parentNode.id);
            parentUL.removeChild(LiObj);
        }catch(err){
            alert(err)
        }


    }
    function addColumntoTab(){
        var tablid='<%=tableId%>'
        var tableName='<%=tablename%>'
        var colNames=new Array
        var colsTypes=new Array
        var colsLengths=new Array
        $('#selectedMeasuresUL li').each(function(index) {
            var textValue = $(this).text();
           // alert("textValue\t"+textValue)
          
            for(var i=0;i<custnamesArray.length;i++){
                if(textValue==custnamesArray[i]){
                    colNames.push(textValue)
                    colsTypes.push( cusColType[i])
                    colsLengths.push( custDBcolsLengths[i])
                    break;
                }

            }
//            if (jQuery.inArray(textValue, custnamesArray)==true){
//                colNames.push(textValue)
//                colsTypes.push(cusColType[jQuery.inArray(textValue, cusColType)])
//                colsLengths.push(custDBcolsLengths[jQuery.inArray(textValue, custDBcolsLengths)])
//            }

        });
      if(colsLengths.length>0){

            
           $.ajax({
               url:'<%= request.getContextPath()%>/editconn.do?parameter=insertInDbMasterDetails&tableId='+tablid+'&colleName='+colNames.toString()+
                    '&colsTypes='+colsTypes.toString()+'&colsLengths='+colsLengths.toString()+'&tableName='+tableName,
                success: function(data){
                    parent.parent.document.getElementById('loading').style.display='';
                    var confVar;
                    if(data=="false"){
                        alert('Error in inserting.');
                    }else{

                        var dataJson=eval("("+data+")")
                        var table="";
                        table+="</br></br><p>  Business Groups are </p><br>"
                        table+="<table width='100%' border='1'><thead><tr><th class='navtitle-hover'>BusinessGroups name </th><th class='navtitle-hover'> Check to Migrate </th></tr></thead><tbody>"
                        for(var i=0;i<dataJson.grpName.length;i++){
                            table+="<tr><td>"+dataJson.grpName[i]+"</td><td>";

                            table+="<input type='checkbox' name='bussGrpCheck' id='bussGrpCheck' checked value='"+dataJson.grpName[i]+"' ></td></tr>";

                        }
                        table+="</tbody></table>";
                        table+="<p>   Please check  Which Business Groups you want migrate these changes, and click on Next </p>"
                                    <%
                                    if(flag!=null){
                                    %>
                        table+="<br><table align='right' ><tr><td> <input type='button' onclick=cancelFunction()  name='cancel' value='Cancel'  class='navtitle-hover' style='width:100px;' ></td><td><input type='button' onclick=saveinBussGrpSrc1() name='Ok' value='Ok'  class='navtitle-hover' style='width:100px;' ></td><td></td></tr></table>"
                        <%
                               }else{
                         %>
                        table+="<br><table align='right' ><tr><td> <input type='button' onclick=cancelFunction()  name='cancel' value='Cancel'  class='navtitle-hover' style='width:100px;' ></td><td><input type='button' onclick=saveinBussGrpSrc() name='next' value='Next'  class=prgBtn' style='width:100px;' ></td><td></td></tr></table>"
                        <%
                               }
                         %>
                        parent.parent.document.getElementById('loading').style.display='none';
                        if(dataJson.grpName.length>0){
                            parent.$("#FolderDetails").dialog('open')
                            parent.$("#FolderDetails").html("");
                            parent.$("#FolderDetails").html(table);
                            
                        }else{
                            alert("No BusinessGroup to migrate")
                            parent.cancelFunction();
                            

                        }

                    }



                }
            });
        }else{

        }

    }
    function saveBussGrpDetails(bussTabId){
         var bussColumnNames=new Array;
         var bussColuIds=new Array;
       $('#MeasuresUL li').each(function(index) {
            var textValue = $(this).text();
              bussColumnNames.push(textValue)
             var idVal=$(this).attr('id');
             var tempV=idVal.toString().split("_")
              bussColuIds.push(tempV[1])
             
        });
        if(bussColumnNames.length==0){
            bussColumnNames='null';
            bussColuIds='null'
        }
//        alert("bussColuIds\t"+bussColuIds)
       $.ajax({
           url:'getAllBusinessGroups.do?method=saveAdditionalTimeDim&bussTabId='+bussTabId+'&bussColumnNames='+bussColumnNames.toString()+'&bussColuIds='+bussColuIds,
        success:function(data){

         if(data=="true"){
             parent.closeMoreTimeDim();
         }
        }});
    }

          
</script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>addColumnToTable</title>

    </head>
    <body>
        <form name="addColumn" method="post" action="">
            <center>
                <div  style="width: 90%; display:block"  class="ui-tabs ui-widget ui-widget-content ui-corner-all">
                    <div class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width:89%;" >
                        <table  align="center" border="1" style="width: 100%;height:auto ">
                            <tr  align="center" valign="top" style="min-width:40%; max-width: 100%"  >
                                <td align="left" valign="top" width="50%">
                                    <div style="height:400px; overflow:auto" id="MeasuresDIV"  align="left" onmouseup="dropDiv('MeasuresUL')">
                                        <div> <table  align="center" border="0" width="100%">
                                                <tr  align="left" valign="top" style=" background-color: rgb(180, 217, 238); color: black;">
                                                    <td style="font-weight:bold" align="center"> <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;">  Measures  </font> </div></td>

                                                </tr></table></div><div style="overflow: auto;height: 370px">
                                            <div style="overflow: auto">
                                                <ul id="MeasuresUL" class="sortable">



                                                </ul></div></div>
                                    </div>
                                </td>
                                <td  align="left" valign="top" width="50%">
                                    <div style="height:400px; overflow:auto" id="selectedMeasuresDIV" align="left" onmouseup="dropDiv('selectedMeasuresUL')">
                                        <div> <table  align="center" border="0" width="100%">
                                                <tr  align="left" valign="top" style=" background-color: rgb(180, 217, 238); color: black;">
                                                    <td style="font-weight:bold" align="center">  <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;"> Drag measures to here  </font> </div></td>
                                                </tr></table></div><div style="overflow: auto;height: 370px">
                                            <ul id="selectedMeasuresUL" class="sortable">

                                            </ul></div>
                                    </div>

                                </td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td>
                                    <input type="button" onclick="addColumntoTab()" name="next" value="Next" style="width: 40px; height: 20px;" class="navtitle-hover" id="next" >
                                </td>
                                <td>
                                      <input type="button" onclick="saveBussGrpDetails('<%=tableId%>')" name="save" id="save" value="Save"  class="navtitle-hover" style="display: none">
                                </td>
                                <td>

                                </td>
                            </tr>
                        </table>

                    </div> </div></center>
        </form>
    </body>
</html>
