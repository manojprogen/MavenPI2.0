<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.business.group.BusinessGroupDAO,java.sql.*,prg.db.PbReturnObject,prg.db.PbDb,java.util.HashMap"%>
<%--
    Document   : AddDescription
    Created on : Dec 15, 2009, 4:15:19 PM
    Author     : Administrator
--%>

<link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
<link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Help Text</title>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <style>
            h1{
                color:#369;
                font-size:12px;
                font-weight:bold;
                text-transform:uppercase;
            }
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
            .ui-corner-all{
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            *{
                font:11px verdana;
            }
        </style>
    </head>
    <body><center><br><br><br><h1>Description </h1>
            <%
        String DIMENSIONID = "";
        String dimenDetails = "";
        PbDb pobj = new PbDb();
        PbReturnObject prgr = new PbReturnObject();
        try {
            DIMENSIONID = request.getParameter("qryDimId");
            dimenDetails = "select distinct  DIMENSION_NAME,DIMENSION_DESC,DIM_HELPTEXT from PRG_QRY_DIMENSIONS WHERE DIMENSION_ID =" + DIMENSIONID;
            prgr = pobj.execSelectSQL(dimenDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }%>
            <form method="post" name="myForm">
                <table width="70%" align="center" border="0px solid">

                    <%for (int i = 0; i < prgr.getRowCount(); i++) {%>
                    <tr style="width:100%">
                        <td>DIMENSION_NAME</td>
                        <td>
                            <input type="text" name="descname" value="<%=prgr.getFieldValueString(i, "DIMENSION_NAME")%>" readonly>
                        </td>
                    </tr>
                    <tr style="width:100%">
                        <td>DIMENSION_DESC</td>
                        <td>
                            <input type="text" name="dimdesc" value="<%=prgr.getFieldValueString(i, "DIMENSION_DESC")%>">
                        </td>
                    </tr>
                    <tr style="width:100%">
                        <td>DIM_HELPTEXT</td>
                        <td>
                            <input type="text" name="helptext" value="<%=prgr.getFieldValueString(i, "DIM_HELPTEXT")%>">
                        </td>
                    </tr>
                    <% }
                    %>
                    <input type="hidden" name="dimeID" value="<%=DIMENSIONID%>">
                        </table>
                        <table width="70%" border="0px solid" align="center">
                    <tr style="width:100%">
                        <td align="center" style="width:100%">
                            <input type="button" class="navtitle-hover"  style="width:auto" name="Save" value="Save" onclick="UpdateConnection()" >&nbsp;
                            <input type="button" class="navtitle-hover" style="width:auto" name="cancel" value="Cancel" onclick="Canceldetails()" >
                        </td>
                    </tr>
                </table>
            </form>
            <%

            %>
    </center>
    <script language="javascript">

            function UpdateConnection(){
                document.forms.myForm.action="editconn.do?parameter=updateUserDetails";
                document.forms.myForm.submit();
                parent.addDescClose();
                alert('Update successfully')
            }
            function Canceldetails(){
                parent.addDescClose();
            }


        </script>
    </body>
</html>
