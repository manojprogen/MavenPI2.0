<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%--
    Document   : Uploadfile
    Created on : July 4, 2013, 5:30:19 PM
    Author     : Vikranth
--%>
<%
String contextPath=request.getContextPath();
%>

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

       <title>piEE</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <!--<script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>

        <html:base/>
       
     
    </head>

    <body>
        <div align="center" style=" height: 450px;background-color: #d1d1d1; border-width: 4px;border-style: solid;border-color: gray; margin-top: auto; margin-left: auto; margin-right: auto; width:1350px; ">

            <form id="down" name="down">
            <div><table class="" align="left" >

          <tr>
<td class="" align="left" colspan="1" style="white-space: nowrap;">Connection Name</td>
<td class="navtitle-hover" align="left" colspan="1">
<select id="connid" name="connid" onchange="enableDeable()">
    <%
    String query="";
   query = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;
    list = pbdb.execSelectSQL(query);
        %>

<option value="">--SELECT--</option>
<% for(int i=0;i<list.rowCount;i++) {%>
<option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option>
<% } %>
</select>
</td>
<td></td><td></td><td></td><td></td><td></td><td></td>

<td align="left" width="20%">
<!--    <input id="executeId" class="navtitle-hover" type="button"  onclick="execQueryFor()" disabled="true"  style="width:auto" value="Execute"/></td>-->
<td  class=""  align="right">Enter Email Id:<input type="email" id="emailid" size="30" style="white-space: nowrap;"  name="formdata"></td>
    <td></td><td></td><td></td><td></td><td></td><td></td>
    <td><input type="button" class="navtitle-hover" id="buttonid"  style="width:auto" onclick="sendmail()" value="send" name="but"></td>


</tr>
<tr><td></td><td></td></tr>
            <tr><td></td><td></td></tr>
            <tr><td></td><td></td></tr>
            <tr><td></td><td></td></tr>
            <tr><td></td><td></td></tr>
            </table> </div>

<tr style="width:100%">
    <td class="migrate" align="left" colspan="2" rowspan="">
    <textarea id="QueryArea" style="background-color:white; font-size: 16px; color:black;width:1320px;height:300px;display:'';" name="Query" rows="4" cols="50" disabled="true" onkeypress="enableExecutFun()" >
    </textarea>
<!--
    <div id="resultAreaId" style="background-color:white; color:black;width:1320px;height:300px;display:none;">
    </div>-->
         </td>
</tr>
<!--    <input id="QueryArea"  type="textArea" style="background-color:white; color:black;width:1320px;height:300px;" value="" name="Query">-->


          <tr>
          <td class="" align="left" colspan="1" style="white-space: nowrap;">Select Format:</td>
          <td id="divdisp" style="display:none">
              <select id="downType" name="downType" disabled=true; onchange="enableformat()">
                         <option value="">--SELECT--</option>
                         <option value="QueryHTML">HTML</option>
                         <option value="pdf">PDF</option>
                         <option value="queryexcel">Excel</option>

                     </select>
          </td>

          <td></td><td></td><td></td><td></td><td></td><td></td>
          <td></td><td></td>
          <td align="right" bottom="0px" ><input id="btn"  class="navtitle-hover" type="button" onclick="downDataFor()" style="width:auto" value="Download"></td></tr>
            </form>
<div><iframe id="Queryexcel" NAME='exportKPi' frameborder="0" SRC="TableDisplay/pbDownload.jsp"></iframe></div>

        </div>
 <script type="text/javascript">

function getFolderDet(){
    $("#divdisp").show();
}
//function enableExecutFun(){
//    document.getElementById("executeId").disabled=false;
//}
function enableDeable(){
    var conType = $("#connid").val();
     if(conType!=""){
         document.getElementById("QueryArea").disabled=false;
          document.getElementById("downType").disabled=false;
            }
            else if(conType==""){
                   document.getElementById("QueryArea").disabled=true;
                 //  document.getElementById("executeId").disabled=true;
                   document.getElementById("downType").disabled=true;
                   document.getElementById("btn").disabled=true;
            }

    }
function enableformat()
{
        var downid=  $("#downType").val();
         if(downid==""){
        document.getElementById("btn").disabled=true;
    }
    else{
        document.getElementById("btn").disabled=false;
    }

}
function sendmail(){

    var conId = $("#connid").val();
    var querystring = $("#QueryArea").val();
    var email = $("#emailid").val();
    if(email!=""){
 $.post(
 'createtableAction.do?param=executeMailQuery&conId='+conId+'&querystring='+encodeURIComponent(querystring)+'&email='+email,
    function(data)
      {
//    document.getElementById("QueryArea").style.display='none';
//    document.getElementById("resultAreaId").style.display='';
    alert(data)
    }
    );
}else{
    alert("Please enter email id!")
}

}
//function execQueryFor(){
//
//    var conId = $("#connid").val();
//    var querystring = $("#QueryArea").val();
// $.post(
// 'createtableAction.do?param=executeManualQuery&conId='+conId+'&querystring='+querystring,
//    function(data)
//      {
//    document.getElementById("QueryArea").style.display='none';
//    document.getElementById("resultAreaId").style.display='';
//    $("#resultAreaId").html(data);
//      }
//        );
//
//     document.getElementById("downType").disabled=false;
//}
             function downDataFor(){
                var conId = $("#connid").val();
                var dtype = $("#downType").val();
                 var querystring = $("#QueryArea").val();
               if(dtype!=""){
                $.post('createtableAction.do?param=downLoadQueryData&dtype='+dtype+'&conId='+conId,'&querystring='+encodeURIComponent(querystring),
                function(data){
                    if(data!=""){
                   var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+dtype+"&data="+data;
                       var frameObj=document.getElementById("Queryexcel");
                    frameObj.src=source;
                    }
                    else{
                        alert("Table does not exist or there no data in table")
                    }
//                  document.getElementById("QueryArea").style.display='';
//                  document.getElementById("resultAreaId").style.display='none';
                } );
            }
               else{
               alert("please select Format")
               }
            }

        </script>
    </body>
</html:html>

