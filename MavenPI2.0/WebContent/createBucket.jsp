
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.Container,prg.db.PbReturnObject, prg.business.group.DynamicBusinessGroupDAO" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String colId = request.getParameter("colId");
            String tabId = request.getParameter("tabId");
            String colName = request.getParameter("colName");
            String colType = request.getParameter("colType");
            String tabName = request.getParameter("tabName");
            String grpId = request.getParameter("grpId");
            String bussTableId = request.getParameter("bussTableId");
            String bussColId = request.getParameter("bussColId");
            String connId = request.getParameter("connId");
            String tempBuck = request.getParameter("tempBuck");
            String folderId = request.getParameter("folderId");
            String elementId = request.getParameter("elementId");
            String tempBussColName = request.getParameter("tempBussColName");
            String reportId = request.getParameter("reportId");
            PbReturnObject returnObject1 = new PbReturnObject();
            PbReturnObject returnObject = new PbReturnObject();
            DynamicBusinessGroupDAO businessGroupDAO = new DynamicBusinessGroupDAO();
            returnObject1 = businessGroupDAO.getColType(elementId);
            String avg = "0";
            String min = "0";
            String max = "0";
            String eleColType=  "";

            Container container = Container.getContainerFromSession(request, reportId);
            if(returnObject1.getRowCount()>0){
                 eleColType = returnObject1.getFieldValueString(0, 0);
                returnObject = new PbReturnObject();
                businessGroupDAO = new DynamicBusinessGroupDAO();
                if(!eleColType.equalsIgnoreCase("summarized")){
                returnObject = businessGroupDAO.getMxminValofMesu(colName, grpId, tabName, eleColType, elementId);
                String minMaxAvgVal = businessGroupDAO.getMinMaxAvgVal(container,tempBussColName);
                if (minMaxAvgVal == "" || minMaxAvgVal.equalsIgnoreCase(" ") || minMaxAvgVal == null) {
                }
                else {
                        String[] minMaxAvgValArr = minMaxAvgVal.split(",");
                        avg = minMaxAvgValArr[0].toString();
                        min = minMaxAvgValArr[1].toString();
                        max = minMaxAvgValArr[2].toString();
                }
            }
}

            String contxPath=request.getContextPath();
%>

<html>
    <head>

         <script type="text/javascript" src="<%=contxPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contxPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script><!--
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>-->
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/pbUserLayer.js"></script>
        <link href="stylesheets/pbUserLayer.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=contxPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
       

        <style>
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color:black;
                padding-left:12px;
                width:50%;
                background-color: #b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: black;
                padding-left:12px;
                background-color: #b4d9ee;
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
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
        </style>

    </head>
    <body>

        <form name="myForm1" id="myForm1" method="post" id="myForm1">
            <input type="hidden" id="h">
            <center>
                <table align="center" border="0" style="width:70%">
                    <tr>
                        <td  >
                            <label class="label" >Measure Name</label>
                        </td>
                        <td>
                            <input type="text" readonly name="measure" value="<%=colName%>" id="measure">
                        </td>
                    </tr>
                    <tr>
                        <td  >
                            <label class="label" >Bucket Name</label>
                        </td>
                        <td>
                            <input type="text" name="bucket" id="bucket" onkeyup="goDesc(this)">
                        </td>
                    </tr>
                    <tr>
                        <td  >
                            <label class="label" >Bucket Description</label>
                        </td>
                        <td>
                            <input type="text" name="bdesc" id="bdesc">
                        </td>
                    </tr>
                    <tr>
                        <td  >
                            <label class="label" >Number of Buckets</label>
                        </td>
                        <td>
                            <input type="text" name="number"  id="number" size="2" maxlength="2" autocomplete="off" onkeyup="addRows('addTable'),checkNumber(this)" >
                        </td>
                    </tr>
                </table><br><br>
                <center><input type="button" value="Cancel" class="navtitle-hover" style="width:auto" id="btn" onclick="cancelBuckets()"></center>
                <div id="headings" style="display:none">

                    <% if (returnObject.getRowCount() != 0) {%>
                    <table border="1" width="80%">
                        <thead><tr>
                                <th  class="prgtableheader" style="background-color:#b4d9ee; font-size: small;">
                                    Max
                                </th>
                                <th  class="prgtableheader" style="background-color:#b4d9ee; font-size: small;">
                                    Min
                                </th>
                                <th  class="prgtableheader" style="background-color:#b4d9ee; font-size: small;">
                                    Avg
                                </th></tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td align="center" >
                                    <%= max%>
                                </td>
                                <td align="center" >
                                    <%= min%>
                                </td>
                                <td align="center" >
                                    <%= avg%>
                                </td>
                            </tr>
                        </tbody>

                    </table>
                    <br> <br>
                    <%}%>
                    <table align="center" border="0" id="addTable">
                        <tr>
                            <th class="prgtableheader" style="background-color:#b4d9ee;">Display Value</th>

                            <th class="prgtableheader" >Start Limit</th>
                            <th class="prgtableheader" >End Limit</th>
                        </tr>
                    </table></div><br>
                <div id="buttons" style="display:none">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveBuckets('<%=bussTableId%>')">&nbsp;
                    <input type="button" class="navtitle-hover" style="width:auto"  value="Cancel" onclick="cancelBuckets()">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Add Row" onclick="addSingleRow('addTable')"/>
                    <input type="button"   class="navtitle-hover" style="width:auto" value="Delete Row" onclick="deleteSingleRow('addTable')" /></div>
            </center>
            <input type="hidden" name="colId" id="colId" value="<%=colId%>">
            <input type="hidden" name="tabId" id="tabId" value="<%=tabId%>">
            <input type="hidden" name="colName" id="colName" value="<%=colName%>">
            <input type="hidden" name="colType" id="colType" value="<%=colType%>">
            <input type="hidden" name="tabName" id="tabName" value="<%=tabName%>">
            <input type="hidden" name="grpId" id="grpId" value="<%=grpId%>">
            <input type="hidden" name="bussTableId" id="bussTableId" value="<%=bussTableId%>">
            <input type="hidden" name="bussColId" id="bussColId" value="<%=bussColId%>">
            <input type="hidden" name="connid" id="connid" value="<%=connId%>">
            <input type="hidden" name="tempBuck" id="tempBuck" value="<%=tempBuck%>">
            <input type="hidden" name="folderId" id="folderId" value="<%=folderId%>">
            <input type="hidden" name="elementId" id="elementId" value="<%=elementId%>">
            <input type="hidden" name="buckMaxVal" id="buckMaxVal" value="<%=max%>">
            <input type="hidden" name="buckMinVal" id="buckMinVal" value="<%=min%>">
            <input type="hidden" name="buckAvgVal" id="buckAvgVal" value="<%=avg%>">
            <input type="hidden" name="BucketType" id="BucketType" value="<%=eleColType%>">
        </form>
         <SCRIPT language="javascript">

            var oldtotal = 0;
            var newtotal = 0;
            var totalRowCount = 0;
             var dv="";
            var sl="";
            var el="";
            function addRow(tableID) {

                var table = document.getElementById(tableID);

                var rowCount = table.rows.length;

                var row = table.insertRow(rowCount);

                var cell1 = row.insertCell(0);
                var element1 = document.createElement("input");
                element1.type = "checkbox";
                cell1.appendChild(element1);

                var cell2 = row.insertCell(1);
                cell2.innerHTML = rowCount + 1;

                var cell3 = row.insertCell(2);
                var element2 = document.createElement("input");
                element2.type = "text";
                cell3.appendChild(element2);

            }

            function deleteRow(tableID) {
                try {
                    var table = document.getElementById(tableID);
                    var rowCount = table.rows.length;

                    for(var i=0; i<rowCount; i++) {
                        var row = table.rows[i];
                        var chkbox = row.cells[0].childNodes[0];
                        if(null != chkbox && true == chkbox.checked) {
                            table.deleteRow(i);
                            rowCount--;
                            i--;
                        }

                    }
                }catch(e) {
                    alert(e);
                }
            }

            function addRows(tableID)
            {
                document.getElementById("headings").style.display = '';
                document.getElementById("buttons").style.display = '';
                document.getElementById("btn").style.display = 'none';
                var status = document.getElementById("number").value;
                if(status=="")
                {
                    document.getElementById("headings").style.display = 'none';
                    document.getElementById("buttons").style.display = 'none';
                    document.getElementById("btn").style.display = '';
                }
                var num = document.getElementById("number").value;
                oldtotal = newtotal;
                newtotal = num;
                // alert("num "+num+"old  "+oldtotal+"new  "+newtotal);
                // alert(num);
                var table1 = document.getElementById(tableID);
                for(var j=oldtotal;j>=1;j--)
                {
                    try{
                        // alert("loop "+j);
                        // alert(table1.hasChildNodes());
                        table1.deleteRow(j);
                        // alert("deleted row "+j);
                    }catch(e){alert("e is "+e)
                    }
                }

                for(var i=1;i<=num;i++)
                {
                    // alert("loooop "+i)
                    var table = document.getElementById(tableID);
                    var row = table.insertRow(i);
                    row.id = i;
                    var cell1 = row.insertCell(0);
                    var element1 = document.createElement("input");
                    element1.type = "text";
                    element1.name = "dv"+i;
                    element1.id = "dv"+i;

                    //element1.value = i;
                    cell1.appendChild(element1);

                    /*  var cell2 = row.insertCell(1);
                     var element2 = document.createElement("input");
                     element2.type = "text";
                     element2.name = "cv"+i;
                     cell2.appendChild(element2);*/

                    var cell3 = row.insertCell(1);
                    var element3 = document.createElement("input");
                    element3.type = "text";
                    element3.name = "sl"+i;
                    element3.id = "sl"+i;
                    if(i==1){
                        element3.setAttribute('onkeypress','return isNumberKey(event)')
                        element3.setAttribute('onblur',' checkMinValaue(this)')
                    }else{
                        element3.readOnly=true

                    }

                    /* if(i-1>=1){
                        if(document.getElementById('sl'+i-1).value=="" ||document.getElementById('sl'+i-1).value==null){
                            element3.value ="";
                        }
                        else{
                            element3.value =document.getElementById('sl'+i).value;
                        }
                    }*/


            <%--= function(){
                       isNumberKey(event)
                        checkNumber(this);
                    }--%>
                                cell3.appendChild(element3);

                                var cell4 = row.insertCell(2);
                                var element4 = document.createElement("input");
                                element4.type = "text";
                                element4.name = "el"+i;
                                element4.id = "el"+i;
                                element4.setAttribute('onkeypress','return isNumberKey(event)')
                                element4.setAttribute('onblur','displayStariLimt(this)')
            <%-- element4.onkeyup = function(){
                 //   if(i>)
                 //
                 //    element4.value = document.getElementById("el"+rowCount-1).value;
                 //alert('i'+i)
                 //checkendval(this,i);
             }--%>
                         cell4.appendChild(element4);

                         /* var cell5 = row.insertCell(4);
                     var element1 = "DELETE";

                     cell5.appendChild(element1);*/
                     }
                 }

                 function checkMinValaue(num){

                     var numVal=num.value;
                     var minValue='<%=min%>'
                     var tempBuck = '<%=tempBuck%>'
                     if(tempBuck!='yes'){
                     if(parseInt(numVal) > parseInt(minValue)){
                         alert("Bucket start limt of the first bucket should be less than or equal to minimum value of measure")
                     }
                     }
                     //commented by Nazneen on 28Jan14
//                     else {
//                         if(parseInt(numVal) < parseInt(minValue)){
//                         alert("Bucket start limt of the first bucket should be greater than or equal to minimum value of measure")
//                     }
//                     }
//                     return num.focus();
                 }

                 function displayStariLimt(val){
                     var disIdnumber=(val.id).replace("el","")
                     if(document.getElementById("sl"+(parseInt(disIdnumber)+1))!=null){
                         document.getElementById("sl"+(parseInt(disIdnumber)+1)).value=''
                         document.getElementById("sl"+(parseInt(disIdnumber)+1)).value=parseInt(val.value)+.0001
                     }
                     //inputStartlimt
                 }

                 function saveBuckets(busTableId)
                 {
                     var tempBuck = '<%=tempBuck%>'
                       var maxValCheck=' <%=max%>'
                       var minValCheck=' <%=min%>'
                     var table =  document.getElementById('addTable');
                     var trowCount = table.rows.length-1;
                     var checkVal=document.getElementById("el"+(trowCount)).value
                     if(tempBuck!='yes'){
                     if(parseInt(checkVal) <= parseInt(maxValCheck)){
                         alert("Bucket end limt of the last bucket should be greater than or equal to maximum value of measure")
                         return document.getElementById("el"+(trowCount)).focus();
                     }
                     }
//                     commented by Nazneen on 28Jan14
//                     if(parseInt(checkVal) <= parseInt(minValCheck)){
//                         alert("Bucket end limt should be greater than or equal to minimum value of measure")
//                         return document.getElementById("el"+(trowCount)).focus();
//                     }
                     var bucket = document.getElementById('bucket').value;
                     var bdesc = document.getElementById('bdesc').value;
                     var number = document.getElementById('number').value;
                     var measure=$("#measure").val()

                     var tabId=$("#tabId").val()

                     var colId=$("#colId").val()

                     var colType=$("#colType").val()

                     var colName=$("#colName").val()

                     var tabName=$('#tabName').val()

                     var grpId=$('#grpId').val()

                     var bussTableId=$('#bussTableId').val()

                     var bussColId=$("#bussColId").val()

                     var connid=$("#connid").val()

                     var tempBuck=$("#tempBuck").val()

                     var folderId=$("#folderId").val()
                     var elementId=$("#elementId").val()


                     if(bucket==''){
                         alert("Please enter Bucket Name");
                     }
                     else  if(bdesc==''){
                         alert("Please enter Bucket Description")
                     }
                     else if(number==''){
                         alert("Please enter number of Buckets")
                     }else if(!(number=='')){
                         var count=0;
                         for(var i=1;i<=number;i++){
                             dv= document.getElementById('dv'+i).value;
                             sl=document.getElementById('sl'+i).value;
                             el= document.getElementById('el'+i).value;
                             if(dv=="" || sl=="" ||el==""){
                                 alert("Please enter all Bucket Details");
                                 count=1;
                                 break;
                             }

                         }

                         if(count==0){
                             parent.$("#loadingmetadata").show();
                             // alert('DuplicateBucketNameExist?name='+bucket+'&busTableId='+busTableId)
                             $.ajax({
                                 url: 'DuplicateBucketNameExist?name='+bucket+'&busTableId='+busTableId,
                                 success: function(data) {

                                 if(data==0){
//                                        $.post('<%=request.getContextPath()%>/saveBucket.do',$("#myForm1").serialize(),
//                                        function(data1)
//                                            {
//                                                alert('function data--->'+data1)
//                                            });
                                             var form =  $('#myForm1');
                                            $.ajax( {
                                            type: "POST",
                                            url: form.attr( 'action' ),
                                            data: form.serialize(),
                                            success: function( response ) {
                                               $.post('<%=request.getContextPath()%>/saveBucket.do',$("#myForm1").serialize(),
                                                function(data1)
                                            {
                                                var status = '<%request.getAttribute("statusOfBucket");%>';
//                                               if(status=='true'){
                                                if(tempBuck=='yes'){
                                                    alert('Bucket Saved Successfully...\nTo use it, Please add as dimension from parameter region & Reset')
                                                }
                                                else {
                                                        alert('Bucket Saved Successfully')
                                                }
//                                               }
//                                               else {
//                                                   alert('Error! Bucket not Saved')
//                                               }
                                                 parent.$("#loadingmetadata").hide();
                                            });
                                            }
                                            });



                                       <%-- document.myForm.action = "saveBucket.do"
                                        document.myForm.submit();--%>
                                        parent.$("#createBucketdiv").dialog('close')
                                        parent.refreshPage()
                                      <%--  parent.document.getElementById("bucketDisp").style.display='none';
                                        parent.document.getElementById('fade').style.display='';--%>
            <%--parent.parent.document.getElementById('loading').style.display='block';--%>
            <%-- alert('bucket Created Successfully');--%>

                                    }
                                    else{
                                        //parent.checkUserFolder();
                                        alert("Bucket Name Already Exists");
                                    }
                                    }
                            });
                        }
                    }
                    else{
                        alert('DuplicateBucketNameExist?name='+name+'&busTableId='+busTableId)
                        parent.$("#loadingmetadata").show();
                        $.ajax({
                            url: 'DuplicateBucketNameExist?name='+name+'&busTableId='+busTableId,
                            success: function(data) {
                                if(data==0){
//                                     $.post( "saveBucket.do",$("#myForm1").serialize(),function(data)
//                                            {
//
//                                            });
                                        var form =  $('#myForm1');
                                            $.ajax( {
                                            type: "POST",
                                            url: form.attr( 'action' ),
                                            data: form.serialize(),
                                            success: function( response ) {
                                               $.post('<%=request.getContextPath()%>/saveBucket.do',$("#myForm1").serialize(),
                                                function(data1)
                                            {
                                               var status = '<%request.getAttribute("statusOfBucket");%>';
                                               if(status=='true'){
                                                if(tempBuck=='yes'){
                                                    alert('Bucket Saved Successfully.Please add as dimension from parameter region to use it')
                                                }
                                                else {
                                                        alert('Bucket Saved Successfully')
                                                }
                                                }
                                               else {
                                                   alert('Error! Bucket not Saved')
                                               }
                                                        parent.$("#loadingmetadata").hide();
                                            });
                                            }
                                            });
                                   <%-- document.myForm.action = "saveBucket.do"
                                    document.myForm.submit();--%>
                                    parent.cancelBuckets1();
                                }
                                else{
                                    //parent.checkUserFolder();
                                    alert("Bucket Name Already Exists");
                                }

                            }
                        });
                    }
                    //  document.myForm.action = "saveBucket.do"
                    //  document.myForm.submit();
                    // alert("jjj")

                    //  parent.cancelBuckets1();
                    //parent.cancelBuckets();
                }

                function addSingleRow(tableID)
                {
                    var table = document.getElementById(tableID);

                    var rowCount = table.rows.length;
                    totalRowCount = rowCount;
                    var row = table.insertRow(rowCount);

                    row.id = rowCount;
                    var cell1 = row.insertCell(0);
                    var element1 = document.createElement("input");
                    element1.type = "text";
                    element1.name = "dv"+rowCount;
                    element1.id = "dv"+rowCount;

                    //element1.value = i;
                    cell1.appendChild(element1);

                    /*  var cell2 = row.insertCell(1);
                    var element2 = document.createElement("input");
                    element2.type = "text";
                    element2.name = "cv"+rowCount;
                    cell2.appendChild(element2);*/

                    var cell3 = row.insertCell(1);
                    var element3 = document.createElement("input");
                    element3.type = "text";
                    element3.name = "sl"+rowCount;
                    element3.id = "sl"+rowCount;
                    element3.readOnly=true

                    /*  if(rowCount-1>1){
                    if(document.getElementById("sl"+rowCount-1).value==""){
                        element3.value ="";
                    }
                    else{
                        element3.value =document.getElementById("sl"+rowCount-1).value;
                    }

                }*/
            <%--element3.onkeyup = function(){

                        checkNumber(this);
                    }--%>
                            cell3.appendChild(element3);

                            var cell4 = row.insertCell(2);
                            var element4 = document.createElement("input");
                            element4.type = "text";
                            element4.name = "el"+rowCount;
                            element4.id = "el"+rowCount;
                            element3.setAttribute('onkeypress','return isNumberKey(event)')
                            element4.setAttribute('onblur','displayStariLimt(this)')
            <%--   element4.onkeyup = function(){

                    }--%>
                            cell4.appendChild(element4);
                        }

                        function deleteSingleRow(tableID)
                        {
                            try{
                                var table1 = document.getElementById(tableID);
                                var n=document.getElementById('number').value;
                                // alert(totalRowCount)
                                if(totalRowCount>n){
                                    table1.deleteRow(totalRowCount);
                                    totalRowCount--;
                                }
                            }
                            catch(e){alert("e is "+e);
                            }
                        }

                        function cancelBuckets()
                        {
                            //alert("kkk");
                            parent.cancelBuckets();
                        }

                        function duplicateName(name,bustableId)
                        {
                            //alert('DuplicateBucketNameExist?name='+name+'&busTableId='+busTableId)
                            $.ajax({
                                url: 'DuplicateBucketNameExist?name='+name+'&busTableId='+busTableId,
                                success: function(data) {
                                    if(data==0)
                                        document.getElementById('bdesc').value = name.value;
                                    else{
                                        //parent.checkUserFolder();
                                        alert("Bucket Name Already Exists");
                                    }

                                }
                            });

                        }

                        function goDesc(name){
                            //alert('desc')
                            document.getElementById('bdesc').value = name.value;
                        }

                        function checkNumber(num)
                        {
                            var n = num.value.length;
                            for (i=0;i<n; i++)

                            // We start a FOR loop to check every character from the first (0) to the last (n)

                            {

                                cchar=num.value.charAt(i);

                                // cchar is the name of each character entered. charAt(i) return the character at position i.

                                if (parseFloat(cchar)|| (cchar=='.')||(cchar=='0'))

                                // This is the crux! If the character can be changed into a number ...

                                // Or if it is a decimal point then ...

                                {

                                    // Here we do nothing!

                                }

                                else

                                {

                                    alert('The character \''+cchar+'\' is not a number\nPlease enter numbers only');

                                    //document.form1.text1.value='';
                                    //self.focus();
                                    break;

                                    //Otherwise, if the character isn't a number then we tell the user

                                    // and clear the form.

                                }

                            }
                            //alert(num.value.length)
                        }
                        function checkendval(num,rowc){


                            var table =  document.getElementById('addTable');

                            var trowCount = table.rows.length-1;
//                            alert('hi'+trowCount+'alert'+rowc)

                        }
                        function isNumberKey(evt)
                        {
                            var charCode = (evt.which) ? evt.which : event.keyCode
//                            alert(charCode)
                            if(charCode==44 || charCode==45)
                                return true;
                            if (charCode > 31 && (charCode < 48 || charCode > 57))
                                return false;
                            return true;
                        }

        </SCRIPT>
    </body>
</html>
