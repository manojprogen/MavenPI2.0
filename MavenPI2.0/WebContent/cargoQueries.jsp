<%--
    Document   : newjsp1
    Created on : 16 Mar, 2012, 11:04:53 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>


         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
        String contextPath=request.getContextPath();
         %>
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
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
        <form action="" method="post" id="columnForm">
            <br><br><br><br>
            <table  border = "2" align="center">
              <tr>
                <td align="left" class="migrate">
                    <label>Initial Load for Cargo </label>
                </td>
                <td align="right">
                     <input type="button" class="" value="Run" id="CargoInit" onclick="cargoInit(this.id)"/>
                </td>
              </tr>
              <tr>
                  <td align="left" class="migrate">
                     <label>Initial Load for Cargo </label>
                  </td>
                  <td align ="right">
                     <input type="button" class="" value="Run" id="CargoIncr" onclick="cargoIncr(this.id)"/>
                  </td>
            </tr>
       </table>
            <br><br>
       </form>
        <form method="post" action="viewRequest.jsp">
           <table align="center">
            <tr><td><input type="submit" value="View Request"></td></tr>
        </table>
       </form>
           <script type="text/javascript">

       function cargoInit(id){
                 alert('id---------'+id)
                 $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=cargoQuery&ids='+id, $("#columnForm").serialize(),
                 function(data) {
                 });
          }

       function cargoIncr(id){
                alert('id---------'+id)
                $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=cargoQuery&ids='+id, $("#columnForm").serialize(),
                function(data){
                });
            }
        </script>
     </body>
</html>