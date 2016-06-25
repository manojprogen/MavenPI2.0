<%-- 
    Document   : editBucketDetails
    Created on : 22 Jul, 2010, 12:56:23 PM
    Author     : sreekanth
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.business.group.DynamicBusinessGroupDAO" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<% 
String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <title>Edit Bucket Details</title>
        <%         //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String grpId = "";
            String tempBuck = "";
            DynamicBusinessGroupDAO dynamicBusinessGroupDAO = new DynamicBusinessGroupDAO();
            PbReturnObject bucketDetails = null;
            PbReturnObject objectDeteails = null;
            try {
                grpId = request.getParameter("busGroupID");
                tempBuck = request.getParameter("tempBuck");
                bucketDetails = dynamicBusinessGroupDAO.getBucketEditDetails(grpId);


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        %>
      
    </head>
    <body>
        <form name="editBktDetailsForm" method="post" >
            <div>
                <%--  <div id="datacorrectionheader" style="width:35.5%; background-color: rgb(180, 217, 238); color: black;display:block" class="prgtableheader2">
                      Edit Bucket Details
                  </div>--%>
                <table align="center">
                    <tr><td style="font-size: small">
                            Select Bucket Name
                        </td>
                        <td>
                            <select id="bktName" name="bktName" onchange="showDetails(this)" >
                                <option value="">
                                    --Select--
                                </option>
                                <%for (int row = 0; row < bucketDetails.getRowCount(); row++) {%>
                                <option value="<%= bucketDetails.getFieldValue(row, "BUCKET_ID")%>_<%= bucketDetails.getFieldValue(row, "BUSS_COL_ID")%>">
                                    <%= bucketDetails.getFieldValue(row, "BUCKET_NAME")%>
                                </option>
                                <%}%>
                            </select>
                        </td>

                </table> <br>


            </div>
            <div id="detailsDiv" style="display: none">

                <table align="center" border="0" style="width:70%">

                    <tr>
                        <td  >
                            <label class="label" style="font-size: small" >Bucket Name</label>
                        </td>
                        <td>
                            <input type="text" name="bucket" id="bucket" value=""  readonly>
                        </td>
                    </tr>
                    <tr>
                        <td  >
                            <label class="label" style="font-size: small" >Bucket Description</label>
                        </td>
                        <td>
                            <input type="text" name="bdesc" id="bdesc" value="" readonly>
                        </td>
                    </tr>

                </table><br>
                <table align="center" id="mesuDetailsTab" style="display: none" border="1"  width="95%">
                    <thead>
                        <tr>
                            <th style="background-color: rgb(180, 217, 238); font-size: small;">
                                Measure Name
                            </th>
                            <th style="background-color: rgb(180, 217, 238); font-size: small;">
                                Max 
                            </th>
                            <th style="background-color: rgb(180, 217, 238); font-size: small;">
                                Min
                            </th>
                            <th style="background-color: rgb(180, 217, 238); font-size: small;">
                                Avg
                            </th>
                        </tr>
                    </thead>
                    <tbody id="tableBody">

                    </tbody>

                </table>
                <br>
                <div id="disDetails">
                    <table align="center" border="0" id="addTable">

                    </table>
                </div>
                <br><br>
                <div id="buttons" align="center">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="updateBucketdetails( )">&nbsp;
                    <input type="button" class="navtitle-hover" style="width:auto"  value="Cancel" onclick="cancelBuckets()">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Add Row" onclick="addBktSingleRow()"/>
                    <input type="button"   class="navtitle-hover" style="width:auto" value="Delete Row" onclick="deleteSingleRow()" /></div>
                <input type='hidden' name='number'  id='number' size='2' maxlength='2'  id='number' value='' >
            </div>


        </form>
                              <script type="text/javascript">
            var bucktName="";
            var totalRowCount=0
            var minValCheck=0
            var maxValCheck=0
            function showDetails(obj){
                var selectId=obj.id
                var valueBktID=document.getElementById(selectId).value;
                var bktValue=valueBktID.split("_");
                var bktID=bktValue[0];
                var bussColId=bktValue[1]; 
                var w = document.editBktDetailsForm.bktName.selectedIndex;
                bucktName = document.editBktDetailsForm.bktName.options[w].text;
                document.getElementById("bucket").value=bucktName;
                document.getElementById("bdesc").value=bucktName;
                $.ajax({
                    url: 'businesGroup.do?action=getBucketChildDetails&grpId='+<%= grpId%>+'&bktID='+bktID+'&bussColId='+bussColId,
                    success: function(data) {
                        var dataObj=data.toString().split("~")
                        var minAndMaxVal=dataObj[0].split("::")
                        document.getElementById("detailsDiv").style.display ='';
                        document.getElementById("mesuDetailsTab").style.display ='';
                        document.getElementById("addTable").innerHTML ='';
                        document.getElementById("number").value =minAndMaxVal[0];
                        document.getElementById("addTable").innerHTML=dataObj[1];
                        var innerStr=""
                        minValCheck=minAndMaxVal[2];
                        maxValCheck=minAndMaxVal[1];
                        innerStr+="<tr> <td> "+minAndMaxVal[3]+" </td><td> "+minAndMaxVal[1]+" </td><td> "+minAndMaxVal[2]+" </td><td>"+minAndMaxVal[4]+"</td><tr>"
                        document.getElementById("tableBody").innerHTML=''
                        document.getElementById("tableBody").innerHTML=innerStr
                    }

                });
                
            }


            function addBktSingleRow()
            {
               
                var table = document.getElementById("addTable");

                var rowCount = table.rows.length;
                totalRowCount = rowCount;
                var row = table.insertRow(rowCount);
                var disrowCount=rowCount-1;
                row.id = rowCount;
                var cell1 = row.insertCell(0);
                var element1 = document.createElement("input");
                element1.type = "text";
                element1.name = "inputValue"+disrowCount;
                element1.id = "inputValue"+disrowCount;
                cell1.appendChild(element1);
                var cell3 = row.insertCell(1);
                var element3 = document.createElement("input");
                element3.type = "text";
                element3.readOnly=true
                element3.name = "inputStartlimt"+disrowCount;
                element3.id = "inputStartlimt"+disrowCount;
                var valStmin=document.getElementById("inputEndlimt"+(disrowCount-1)).value
                element3.value = parseInt(valStmin) +.0001
                cell3.appendChild(element3);

                var cell4 = row.insertCell(2);
                var element4 = document.createElement("input");
                element4.type = "text";
                element4.name = "inputEndlimt"+disrowCount;
                element4.id = "inputEndlimt"+disrowCount;
                element4.setAttribute('onkeypress','return isNumberKey(event)')
            <%-- element4.onblur = function(){
                 addnextVa1(this,rowCount);
             }--%>
                        element4.setAttribute('onblur','displayStariLimt(this)')
                        cell4.appendChild(element4);


                    }
                    function checkNumber(num)
                    {
                        var n = num.value.length;
                        for (i=0;i<n; i++)
                        {
                            cchar=num.value.charAt(i);
                            if (parseFloat(cchar)|| (cchar=='.')||(cchar=='0'))

                            {   // Here we do nothing!

                            }
                            else {
                                alert('The character \''+cchar+'\' is not a number\nPlease enter numbers only');
                                break;

                            }

                        }

                    }
                    function addnextVa1(elId,elrownum){
                        var elrownum=elId.id.toString().substr(2,elId.id.toString().length-1);
                        var table =  document.getElementById('addTable');
                        var trowCount = table.rows.length-1;
                        if(elrownum<trowCount){
                            var str="sl"+(parseInt(elrownum)+1);
                            document.getElementById(str).value=parseInt(document.getElementById(elId.id).value)+0.0001;
                        }
                    }

                    function deleteSingleRow()
                    {
                        try{
                            var conCheck=confirm("Are You Sure, You Want to Delete ?")
                            if(conCheck){
                                var table1 = document.getElementById('addTable');
                                var n=1;
                                var table =  document.getElementById('addTable');
                                var trowCount = table.rows.length-1;
           
                                if(trowCount>n){
                                    table1.deleteRow(trowCount);
                                     
                                }
                       
                            }
                   
                        }
                        catch(e){alert("e is "+e);
                        }
                    }

                    function updateBucketdetails(){
                        var table =  document.getElementById('addTable');
                        var trowCount = table.rows.length-1;
                        var checkVal=document.getElementById("inputEndlimt"+(trowCount-1)).value
                         var tempBuck = '<%=tempBuck%>'
                        if(tempBuck!='yes'){
                        if(parseInt(checkVal) <= parseInt(maxValCheck)){
                       
                            alert("Bucket end limit of the last bucket should be greater than or equal to maximum value of measure")
                            return document.getElementById("inputEndlimt"+(trowCount-1)).focus();
                        }else{
                         
                            var w = document.editBktDetailsForm.bktName.selectedIndex;
                            var bktName = document.editBktDetailsForm.bktName.options[w].text;
                            var bktIDandbktColid = (document.editBktDetailsForm.bktName.options[w].value).split("_");
                            var bktID = bktIDandbktColid[0]
                            var rowCount =document.getElementById("addTable").getElementsByTagName("tr").length
                            rowCount=rowCount-1
                            alert('Bucket details updated Successfully')   
                            document.editBktDetailsForm.action='businesGroup.do?action=updateBucketDetails&bktName='+bktName+'&bktID='+bktID+'&rowCount='+rowCount+'&grpId='+<%= grpId%>
                            document.editBktDetailsForm.submit();
                            parent.$("#editBucketDiv").dialog('close');
                                                     
                        }
                        }
                        else {
                        if(parseInt(checkVal) <= parseInt(minValCheck)){                       
                            alert("Bucket end limt should be greater than or equal to minimum value of measure")
                            return document.getElementById("inputEndlimt"+(trowCount-1)).focus();
                        }else{
                            var w = document.editBktDetailsForm.bktName.selectedIndex;
                                var bktName = document.editBktDetailsForm.bktName.options[w].text;
                                var bktIDandbktColid = (document.editBktDetailsForm.bktName.options[w].value).split("_");
                                var bktID = bktIDandbktColid[0]
                                var rowCount =document.getElementById("addTable").getElementsByTagName("tr").length
                                rowCount=rowCount-1
                                alert('Bucket details updated Successfully')
                                document.editBktDetailsForm.action='businesGroup.do?action=updateBucketDetails&bktName='+bktName+'&bktID='+bktID+'&rowCount='+rowCount+'&grpId='+<%= grpId%>
                                document.editBktDetailsForm.submit();
                                parent.$("#editBucketDiv").dialog('close');
                                
                        }
                        }
            
                    }
                    function checkMinValaue(num){
                        var numVal=num.value;
                         var tempBuck = '<%=tempBuck%>'
                     if(tempBuck!='yes'){
                        if(numVal > minValCheck){
                            alert("Bucket start limit of the first bucket should be less than or equal to minimum value of measure")
                        }
                     }
                      else {
                         if(numVal < minValCheck ){
                         alert("Bucket start limt of the first bucket should be greater than or equal to minimum value of measure")
                     }
                        return num.focus();
                    }
                    }

                    function displayStariLimt(val){
               
                        var disIdnumber=(val.id).replace("inputEndlimt","")
                        if(document.getElementById("inputStartlimt"+(parseInt(disIdnumber)+1))!=null){
                            document.getElementById("inputStartlimt"+(parseInt(disIdnumber)+1)).value=''
                            document.getElementById("inputStartlimt"+(parseInt(disIdnumber)+1)).value=parseInt(val.value)+.0001
                        }
                        //inputStartlimt
                    }
              
                    function isNumberKey(evt)
                    {
                        var charCode = (evt.which) ? evt.which : event.keyCode
                        if(charCode==44)
                            return true;
                        if (charCode > 31 && (charCode < 48 || charCode > 57))
                            return false;

                        return true;
                    }
                    //added by Nazneen on 29Jan14
                    function cancelBuckets(){
                        parent.$("#editBucketDiv").dialog('close');
                    }

        </script>
    </body>
</html>
