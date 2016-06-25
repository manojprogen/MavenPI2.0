<%-- 
    Document   : editTimeSetup
    Created on : Sep 17, 2009, 8:11:29 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />

        <link href="myStyles.css" rel="stylesheet" type="text/css">
            <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>
            function savetimesetUp(){
                document.myForm2.action="saveTimeSetUp.do";
                document.myForm2.submit();
                parent.refreshList3();
            }
             function canceltimesetUp(){
               parent.canceltimesetUp();
            }
             function customcheck(){
                 if(document.getElementById('custom').checked==true)
                     {
                         document.getElementById('customDiv').style.display='';
                     }else{
                         document.getElementById('customDiv').style.display='none';
                     }
            }

        </script>
        <style>
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
        </style>

    </head>

    <body>
        <%


 String calId=request.getParameter("calId");
 String calType=request.getParameter("calType");
        String calName=request.getParameter("calName");
        String denomTab=request.getParameter("denomTab");

                       %>
        <center>
            <form name="myForm2" method="post">
                <table align="center" >
                
                
                <tr><td>&nbsp;</td></tr>
                    <tr>
                        <td><label class="label" >Calender ID</label></td>
                        <td><input type="text" name="calId" id="calId" readOnly value="<%=calId%>"> </td>
                    </tr><tr>
                        <td><label class="label" >Calender Type</label></td>
                        <td><input type="text" name="calType" id="calType" readOnly value="<%=calType%>"> </td>
                    </tr><tr>
                        <td><label class="label" >Calender Name</label></td>
                        <td><input type="text" name="calName" id="calName" value="<%=calName%>"> </td>
                        <tr></tr>
                        <td><label class="label" >Denorm Table</label></td>
                        <td><input type="text" name="denomTab" readOnly id="denomTab" value="<%=denomTab%>"> </td>

                    </tr>
                 <%--   <tr><td>Customise</td>
                        <td>
                            <input type="checkbox" id="custom" name="custom"  onchange="customcheck()">
                        </td>
                    </tr>
                    --%>
                   </table>
                   <%--
                    <div id="customDiv" style="display:none">
                      <table>

                    <tr>
                        <td>Year Start&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;<input type="text" name="strYear" id="strYear"> </td>
                    </tr><tr>
                        <td>Year End&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;<input type="text" name="endYear" id="endYear"> </td>
                    </tr>

                      </table>
                    </div>
                    --%>
                        <table>
                            <tr>
                                <td>
                                    <input type="button" class="navtitle-hover" style="width:auto" name="Save" value="Save" onclick="savetimesetUp()">
                                    <input type="button" class="navtitle-hover" style="width:auto" name="Cancel" value="Cancel" onclick="canceltimesetUp()">
                                </td>
                            </tr>

                        </table>


                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">

            </form>
            </center>
    </body>
</html>
