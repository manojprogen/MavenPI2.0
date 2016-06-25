<%--
    Document   : dataCorrection
    Created on : Jan 15, 2010, 12:00:28 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.progen.xlupdateAction.XlupdateDAO"%>
<%@ page import="prg.db.PbReturnObject"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%
            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            PbReturnObject prgr = new PbReturnObject();
            XlupdateDAO getcol = new XlupdateDAO();
            prgr = getcol.getColumns();

        %>
        <script type="text/javascript">


            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }

            function getcolumns(){
                var tablename =document.getElementById("tablename").value;
                //alert("tablename"+tablename);
                var path=document.getElementById("path").value;
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                var url=path+"/GetTabvalue";
                url=url+"?tableName="+tablename;
                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);
            }



            function showSelected()
            {
                
                var selop = document.getElementById("colnames").value;
                var tablename =document.getElementById("tablename").value;
                var path=document.getElementById("path").value;
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                var url=path+"/GetTablevalues";
                url=url+"?q="+selop+"&tableName="+tablename;
               
                //  alert("url is:: "+url)
                xmlHttp.onreadystatechange=stateChanged10;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);
            }



            function stateChanged10()
            {
                if (xmlHttp.readyState==4)
                {
                    var output=xmlHttp.responseText
                    //   alert("op is:: "+output)
                    var tableId=document.getElementById("valesTable");
                    var rows=tableId.rows.length;
                    //  alert(rows);

                    var scnMonthList=output.split("\n");

                    for(var i=0;i<rows-3;i++){

                        // document.getElementById('colmnoldvalues'+i).innerHTML = "";
                        var scnStobj = document.getElementById('colmnoldvalues'+i).value;
                        //  alert("in 1st"+scnStobj)
                        /* for(var i=scnStobj.length-1;i>=0;i--)
                        {
                            scnStobj.options[i] = null;
                        }
                        scnStobj.options[0] = new Option("--Select--","--Select--");*/
                        document.getElementById('colmnoldvalues'+i).innerHTML='';
                        var optn = document.createElement("OPTION");
                        optn.text = "--select--";
                        optn.value ="";
                        document.getElementById('colmnoldvalues'+i).options.add(optn);
                        for(var k=0;k<scnMonthList.length-1;k++)
                        {

                            var monthName = scnMonthList[k];

                            addOption(document.getElementById('colmnoldvalues'+i),monthName,monthName);

                        }

                    }

                }


            }

            function addOption(selectbox,text,value)
            {

                var optn = document.createElement("OPTION");
                //optn.innerHTML = "";
                optn.text = text;
                optn.value = value;
                selectbox.options.add(optn);
            }
            /* function columnNewValue(li){



                var colOldval = document.getElementById('colmnoldvalues0').value;
                alert("colmnoldvalues"+colOldval);

                var path=document.getElementById("path").value;
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                var url=path+"/GetTabvalue";
                url=url+"?colOldval="+colOldval;
                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);
            }*/
            function stateChanged(){
                if (xmlHttp.readyState==4)
                {
                    var output=xmlHttp.responseText
                    // alert("op is:: "+output)
                    var colmunnameslist=output.split("\n");
                    var colnameobj = document.getElementById('colnames').value;
                    document.getElementById('colnames').innerHTML='';
                    var optn = document.createElement("OPTION");
                    optn.text = "--select--";
                    optn.value = " ";
                    document.getElementById('colnames').options.add(optn);
                    //  document.getElementById("colnames").value=output;
                    for(var k=0;k<colmunnameslist.length-1;k++)
                    {

                        var columnName = colmunnameslist[k];

                        addOption(document.getElementById('colnames'),columnName,columnName);

                    }

                }
            }
            function addrow(){
                // var tableId=document.getElementById("valesTable");
                //var rows=tableId.rows.length;
                var table = document.getElementById("valesTable");
                var rowCount = table.rows.length;
                //alert("rows"+rowCount);
                var idx = rowCount-3 ;
                var row = table.insertRow(rowCount);
                row.id="row"+idx;
                var cell0 = row.insertCell(0);
                cell0.innerHTML="<select name=\"colmnoldvalues\" id=\"colmnoldvalues"+idx+"\" onclick=\"stateChanged10()\"  style=\"width: 180px;\" >"+
                    "<option value=\"\">--select--</option></select>";
                var cell1 = row.insertCell(1);
                cell1.innerHTML="<input type=\"text\" name=\"colnewvalue\" value=\"\" id=\"colnewvalue"+idx+"\"  style=\"width: 180px;\" >";

            }

            function deleterow(){
                try {
                    var table = document.getElementById("valesTable");
                    var rowCount = table.rows.length;
            <%--if(rowCount > 1) {
            table.deleteRow(rowCount - 1);
            }--%>

                        if(rowCount>5){
                            // if(rowCount > 0) {
                            table.deleteRow(rowCount - 1);
                            //}
                        }
                    }catch(e) {
                        alert(e);
                    }
                }
                function update(){
                    var oldvalarr = new Array();
                    var newvalarr = new Array();
                    var table = document.getElementById("valesTable");
                    var rowCount = table.rows.length;
                    var tablename =document.getElementById("tablename").value;
                    var colName =document.getElementById("colnames").value;
                    for(var v = 0; v<rowCount-3;v++){
                        var oldval = document.getElementById('colmnoldvalues'+v).value;
                        oldvalarr[v]=oldval;
                        ceeck=oldval;
                        if(document.getElementById('colnewvalue'+v).value==""){
                            var newval = "UN ASSIGNED";
                            newvalarr[v]=newval;
                        }
                        else{
                            var newval =document.getElementById('colnewvalue'+v).value;
                            newvalarr[v]=newval;
                        }
                    }
                    var oldvalarr1=oldvalarr.toString();
                    //  alert("old value"+oldvalarr1)
                    var newvalarr1=newvalarr.toString();
                   
                        $.ajax({
                        url: 'xlUpdate.do?parm=upDateColumns&oldvlaArr='+oldvalarr1+'&newValArr='+newvalarr1+'&tableName='+tablename+'&colname='+colName,
                        success: function(data) {
                              //alert(data)
                            if(data==""){
                                alert("Update successfully");
                                window.location.reload(true);
                                //document.getElementById("datacorrectiondiv").style.display='none';
                                // document.getElementById("datacorrectionheader").style.display='none';

                            }else {
                                alert("Update Error ")
                            }


                        }
                    });

                    
                    }
                    


                
                function cancelUpdate(){

                 window.location.reload(true);
                }



        </script>
    </head>
    <body>
        <center>
            <div id="datacorrectionheader" style="width:35.5%; background-color: rgb(180, 217, 238); color: black;display:block" class="prgtableheader2"><b>Data Correction</b>

            </div>
            <div id="datacorrectiondiv" style="width: 35%; min-height: 100%; max-height: 100%;display:block"  class="ui-tabs ui-widget ui-widget-content ui-corner-all">
                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                &nbsp;
                <table id="valesTable" >
                    <tr>
                        <td>
                            Table Name:
                        </td>
                        <td>
                            <select id="tablename" style="width:180px" onchange="getcolumns()">
                                <option value="">---select---</option>
                                <option value="PRG_KNP_BASE_DIM"> Base Data</option>
                                <option value="PRG_KNP_COMP_DIM1"> Company Data </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Column Name:
                        </td>

                        <td>

                            <select id="colnames" onchange="showSelected()" style="width:180px">
                                <option value="">--select--</option>
                               

                                </option>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b>
                                Old Values
                            </b>
                        </td>
                        <td>
                            <b>
                                New Values
                            </b>
                        </td>
                    </tr>
                    <% for (int limt = 0; limt < 5; limt++) {%>
                    <tr>

                        <td>


                            <select id="colmnoldvalues<%=limt%>" style="width:180px">
                                <option value="">--select--</option>
                            </select>

                        </td>

                        <td>
                            <input type="text" id="colnewvalue<%=limt%>" name="colnewvalue" value="" style="width:180px">
                        </td>
                    </tr> <% }%>
                    <table>
                        <tr>
                            <td>
                                <input type="button" class="navtitle-hover"  name="Add Row" value="Add Row" onclick="addrow()">
                            </td>
                            <td>
                                <input type="button" class="navtitle-hover"  name="Delete Row" value="Delete Row" onclick="deleterow()">
                            </td>
                            <td>
                                <input type="button" class="navtitle-hover" name="Update" value="Update" onclick="update()">
                            </td>
                           <td>
                                <input type="button" class="navtitle-hover" name="Cancel" value="cancel" onclick="cancelUpdate()">
                            </td>

                        </tr>

                    </table>

                    <input type="hidden" name="path" id="path" value="<%=request.getContextPath()%>">
                    <input type="hidden" name="limt" id="limt" value="0">

                </table>

                &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                &nbsp;
            </div></center>


    </center>
</body>
</html>
