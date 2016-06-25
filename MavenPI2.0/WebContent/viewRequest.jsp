<%-- 
    Document   : viewRequest
    Created on : Apr 4, 2012, 11:27:37 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

 <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
String conPath=request.getContextPath();
         %>


<html>
    <head>
        <jsp:include page="Headerfolder/headerPage.jsp"/>

         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=conPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=conPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />


        <style type="text/css">
         .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
                border:0px;
            }
      </style>

       

    </head>
    <body> 

<form method="post" action="" name="myFrom">

<table border="1" align="center">
<h4 align="center">Master Request</h2>
<tr><td>
   <table id="MasterId" align="center">
      
                <thead><tr>
                        <th class="migrate">Master Request Id</th>
                        <th class="migrate">Request Name</th>
                        <th class="migrate">Request Type</th>
                        <th class="migrate">Request Current Status</th>
                        <th class="migrate">Details</th>
                    </tr>
                </thead>
                <tbody id="segmentationTabBody">
                </tbody>
                </table>
</tr></td></table>
<table border="1" align="center">
<h4 align="center">Master Request Details</h3>
<tr><td>

<table id="DetailId" align="center">

                <thead><tr>
                        <th class="migrate">DetailRequestId</th>
                        <th class="migrate">Request Name</th>
                        <th class="migrate">Request Type</th>
                        <th class="migrate">Request Start Date</th>
                        <th class="migrate">Request End Date</th>
                        <th class="migrate">Request Current Status</th>

                    </tr>
                </thead>
                <tbody id="DetailTabBody">
                </tbody>
                </table>
</tr></td></table>

</form><br><br>
    
</form>
<table width="100%" class="fontsty" bgcolor="#bdbdbd">
            <tr style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%" bgcolor="#bdbdbd">
                    <center><font  style="color:#fff;font-size:10px;font-family:verdana;" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold">Progen Business Solutions.</a> All Rights Reserved</font></center>
                </td>
            </tr>
        </table>
 <script type="text/javascript">
            $(document).ready(function()
         {
             $.get('<%= request.getContextPath()%>/studioAction.do?studioParam=getMaster',function(data){
                 var jsonVar=eval('('+data+')')
                              var ids=jsonVar.requestIdList
                              var name=jsonVar.requestNameList
                              var type=jsonVar.requestTypeList
                              var status=jsonVar.requestStatusList

                              var htmlVar=""
                       for(var i=0;i<ids.length;i++)
                           {
                             htmlVar+="<tr><td><input type='text' name='' value='"+ids[i]+"' readonly id='masterId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+name[i]+"' readonly id='nameId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+type[i]+"' readonly id='typeId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+status[i]+"' readonly id='statusId"+i+"'/></td>\n\
                                           <td><input value='Show Details' type='button' onclick='loadDetail("+i+")' ></td></tr>"
                           }
                  $("#segmentationTabBody").html(htmlVar)
            });
            });
            </script>
            <script type="text/javascript">
            function loadDetail(id)
            { 
            var masterId = $("#masterId"+id).val()
                      
             $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=getDetails&ids='+masterId, $("#columnForm").serialize(),
                       function(data)
                               {
                              var jsonVar=eval('('+data+')')
                              var ids=jsonVar.requestIdList
                              var name=jsonVar.requestNameList
                              var type=jsonVar.requestTypeList
                              var stDate=jsonVar.requestStDateList
                              var ltDate=jsonVar.requestLtDateList
                              var status=jsonVar.requestStatusList
                               var htmlVar=""
                            for(var i=0;i<ids.length;i++)
                           {
                             htmlVar+="<tr><td><input type='text' name='' value='"+ids[i]+"' readonly id='masterId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+name[i]+"' readonly id='nameId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+type[i]+"' readonly id='typeId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+stDate[i]+"' readonly id='typeId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+ltDate[i]+"' readonly id='typeId"+i+"'/></td>\n\
                                           <td><input type='text' name='' value='"+status[i]+"' readonly id='statusId"+i+"'/></td></td></tr>"
                                           
                           }
                  $("#DetailTabBody").html(htmlVar)

                
            });


            }
                 
        </script>
</html>
