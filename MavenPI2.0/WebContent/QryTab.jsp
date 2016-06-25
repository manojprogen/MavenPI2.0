<%-- 
    Document   : QryTab
    Created on : Nov 11, 2009, 4:37:50 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="QryTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">

                    <table cellspacing="30" cellpadding="20" >
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Database Connection#Database_Connection" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/DB Connection.gif"  style="cursor:pointer" title="Database Connection"></a></td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Dimensions#Dimensions" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Dim.gif"  style="cursor:pointer" title="Dimensions"></a></td>
                                    </tr>

                                </table>
                            </td>
                    <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Time SetUp#Time_SetUp" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Time.gif"  style="cursor:pointer" title="Time Setup"></a></td>
                                    </tr>

                                </table>
                    </td>
                        </tr>
                        <tr align="center">
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Business Groups#Business_Groups" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Biz Groups.gif"  style="cursor:pointer" title="Business Groups"></a></td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Business Roles#Business_Roles" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Biz Roles.gif"  style="cursor:pointer" title="Business Roles"></a></td>
                                    </tr>

                                </table>
                            </td>

                        </tr>
                    </table>

                </div>
    </body>
</html>
