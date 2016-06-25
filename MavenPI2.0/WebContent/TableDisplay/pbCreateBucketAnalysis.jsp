
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextpath=request.getContextPath();
%>
<html>
    <head>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextpath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/pbUserLayer.js"></script>
        <link href="stylesheets/pbUserLayer.css" rel="stylesheet" type="text/css" />
         <link type="text/css" href="<%=contextpath%>/stylesheets/metadataButton.css" rel="stylesheet" />
      
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
        <%
        String report_id=request.getParameter("REPORTID");
        String factColEle=request.getParameter("FactColId");
        String colName=request.getParameter("colNames");
        int vcount=Integer.parseInt(request.getParameter("vcount"));

        %>

        <form name="myForm" method="post">
             <input type="hidden" id="h">
             <input type="hidden" id="elementId" name="elementId" value="<%=factColEle%>">
             <input type="hidden" id="reportId" name="reportId" value="<%=report_id%>">
            <center>
                <table align="center" border="0" style="width:70%">
                    <tr>
                        <td>
                            <label class="label" >Measure Name</label>
                        </td>
                        <td>
                            <input type="text" readonly name="measure" value="<%=colName%>" style="width:200px">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="label" >Bucket Name</label>
                        </td>
                        <td>
                            <input type="text" name="bucket" id="bucket" onkeyup="goDesc(this)" style="width:200px">
                        </td>
                    </tr>
                    <tr>
                        <td  >
                            <label class="label" >Bucket Description</label>
                        </td>
                        <td>
                            <input type="text" name="bdesc" id="bdesc" style="width:200px">
                        </td>
                    </tr>
                      <tr>
                        <td  >
                            <label class="label" >View By</label>
                        </td>
                        <td>
                          <%--  <input type="checkbox" name="isNewViewBy" id="isNewViewBy" value="Y" Checked onchange="chageView()">--%>
                          <select name="viewBy" id="viewBy"  onchange="chageView()" style="width:205px">
                              <option value="new">As New View By</option>
                              <option value="replace">Replace with</option>
                              <option value="crossTab">As Cross Tab</option>                              
                          </select>

                          <div id="viewByDiv" style="display:none">
                              <select name="viewByRow" id="viewByRow" style="width:205px">
                            <%for(int i=0;i<vcount;i++){
                             if(i==0 && vcount>1){
                             %>
                           <%--   <option value="all">All ViewBys  </option>--%>
                            <%}%>
                              <option value="<%=(i)%>">ViewBy <%=(i+1)%> </option>



                            <%}%>
                              </select>
                          </div>


                        </td>
                    </tr>
                    <tr>
                        <td  >
                            <label class="label" >Number of Buckets</label>
                        </td>
                        <td>
                            <input type="text" name="number"  id="number" size="2" maxlength="2" autocomplete="off" style="width:200px" onkeyup="addRows('addTable'),checkNumber(this)" id="number">
                        </td>
                    </tr>
                </table><br><br>
                <center><input type="button" value="Cancel" class="navtitle-hover" style="width:auto" id="btn" onclick="cancelanalysisBuckets()"></center>
                <div id="headings" style="height:200px;display:none;overflow:auto"><table align="center" border="0" id="addTable">
                        <tr>
                            <th class="prgtableheader" style="background-color:#b4d9ee;">Display Value</th>

                            <th class="prgtableheader" >Start Limit</th>
                            <th class="prgtableheader" >End Limit</th>

                        </tr>

                </table></div><br>
                <div id="buttons" style="display:none">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveBuckets('<%=request.getContextPath()%>')">&nbsp;<input type="button" class="navtitle-hover" style="width:auto"  value="Cancel" onclick="cancelanalysisBuckets()">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Add Row" onclick="addSingleRow('addTable')"/>
                    <input type="button"  class="navtitle-hover" style="width:auto" value="Delete Row" onclick="deleteSingleRow('addTable')" /></div>

            </center>
            
        </form>
                      <SCRIPT language="javascript">

            var oldtotal = 0;
            var newtotal = 0;
            var totalRowCount = 0;
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
                    if(i==1){
                document.getElementById("headings").style.display = '';
                document.getElementById("buttons").style.display = '';
                document.getElementById("btn").style.display = 'none';
                    }
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

                   /* if(i-1>=1){
                        if(document.getElementById('sl'+i-1).value=="" ||document.getElementById('sl'+i-1).value==null){
                            element3.value ="";
                        }
                        else{
                            element3.value =document.getElementById('sl'+i).value;
                        }
                    }*/
                    element3.onkeyup = function(){
                        checkNumber(this);
                    }
                    cell3.appendChild(element3);

                    var cell4 = row.insertCell(2);
                    var element4 = document.createElement("input");
                    element4.type = "text";
                    element4.name = "el"+i;
                    element4.id = "el"+i;
                   // alert(i);
                    var count=i;
                    element4.onkeyup = function(){
                        checkNumber(this);
                    }
                    element4.onblur = function(){
                    // alert('hi'+count)
                     addnextVa1(this,count);
                    }
                    cell4.appendChild(element4);

                }
            }

            function saveBuckets(ctxpath)
            {
                var bucket = document.getElementById('bucket').value;
                var bdesc = document.getElementById('bdesc').value;
                var number = document.getElementById('number').value;
                var table =  document.getElementById('addTable');
                var trowCount = table.rows.length-1;
                number=trowCount;
                
                if(bucket==''){
                    alert("Please enter Bucket Name");
                }
                else  if(bdesc==''){
                    alert("Please enter Bucket Description")
                }
                else if(number==''){
                    alert("Please enter number of Buckets")
                }else if(!(number=='') && (number!=0)){
                    var count=0;
                    for(var i=1;i<=number;i++){
                        var dv= document.getElementById('dv'+i).value;
                        var sl=document.getElementById('sl'+i).value;
                        var el= document.getElementById('el'+i).value;
                        if(dv=="" || sl=="" ||el==""){
                            alert("Please enter all Bucket Details");
                            count=1;
                            break;
                        }

            }                     //  alert('isNewViewBy'+document.getElementById("isNewViewBy").value)
                                    document.myForm.action =ctxpath+"/reportViewer.do?reportBy=saveBucketAnalysisBy";
                                    document.myForm.submit();
                                    parent.parent.savecancelBucket();
                   /* if(count==0){
                    
                        $.ajax({
                            url: 'DuplicateBucketNameExist?name='+bucket+'&busTableId='+busTableId,
                            success: function(data) {
                                if(data==0){
                                    document.myForm.action = "saveBucket.do"
                                    document.myForm.submit();
                                    parent.cancelBuckets1();
                                }
                                else{
                                   
                                    alert("Bucket Name Already Exists");
                                }

                            }
                        });
                    }*/
                }
                else{
                  //  alert('DuplicateBucketNameExist?name='+name+'&busTableId='+busTableId)
                    /*$.ajax({
                        url: 'DuplicateBucketNameExist?name='+name+'&busTableId='+busTableId,
                        success: function(data) {
                            if(data==0){
                                document.myForm.action = "saveBucket.do"
                                document.myForm.submit();
                                parent.cancelBuckets1();
                            }
                            else{
                                //parent.checkUserFolder();
                                alert("Bucket Name Already Exists");
                            }

                        }
                    });*/
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
              /*  if(rowCount-1>1){
                    if(document.getElementById("sl"+rowCount-1).value==""){
                        element3.value ="";
                    }
                    else{
                        element3.value =document.getElementById("sl"+rowCount-1).value;
                    }

                }*/
                element3.onkeyup = function(){

                    checkNumber(this);
                }
                cell3.appendChild(element3);

                var cell4 = row.insertCell(2);
                var element4 = document.createElement("input");
                element4.type = "text";
                element4.name = "el"+rowCount;
                element4.id = "el"+rowCount;
               element4.onkeyup = function(){

                    checkNumber(this);
                }
                element4.onblur = function(){

                    addnextVa1(this,rowCount);
                }
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

            function cancelanalysisBuckets()
            {
                //alert("kkk");
                parent.cancelanalysisBuckets();
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
                //alert('hi'+trowCount+'alert'+rowc)

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
         function chageView(){
             if(document.forms.myForm.viewBy.value=="new"){
              document.getElementById("viewByDiv").style.display='none';
             }else if(document.forms.myForm.viewBy.value=="replace"){
                // alert('replace')
                 document.getElementById("viewByDiv").style.display='';
             }else{
                 document.getElementById("viewByDiv").style.display='none';
             }

         }        </SCRIPT>

    </body>
</html>
