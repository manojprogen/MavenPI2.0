<%-- 
    Document   : stickyList
    Created on : Mar 4, 2010, 5:26:57 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%

    String themeColor="blue";
     if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@page import="prg.db.PbDb" %>
        <%@page import="prg.db.PbReturnObject" %>
        <title>JSP Page</title>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <script type="text/javascript">
            function viewText(id,name){
                var sText = document.getElementById(id).value;
                parent.viewTextParent(sText,name);
            }
            function deleteSticky(snote_id,userId){
                var delConfirm = confirm("Delete Sticky Note");
                if(delConfirm == true){
                    var snote_id = snote_id;
                    var userId = userId;
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=deleteSticky&snote_id='+snote_id+'&userId='+userId,
                        success: function(){
                            parent.document.forms.myFormH.action = '<%=request.getContextPath()%>'+"/AdminTab.jsp#Sticky_List";
                            parent.document.forms.myFormH.submit();
                        }
                    });
                }                
            }
        </script>
        <style type="text/css">
       
            #hyperSticky:hover{text-decoration:underline}
        </style>
    </head>
    <body>
        <%
            PbDb pbdb = new PbDb();
            String stickyQry = "";
            String userId = null;

            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

            stickyQry = "select pr.REPORT_NAME,ps.STICKY_NOTES_ID,ps.STICKY_LABEL,ps.S_NOTE,ps.created_on,ps.REPORT_ID from PRG_USER_STICKYNOTE ps,PRG_AR_REPORT_MASTER pr where ps.user_id =" + userId + " AND pr.REPORT_ID = ps.report_id";
            PbReturnObject list = pbdb.execSelectSQL(stickyQry);


        %>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#tablesorterSticky")
                .tablesorter()
                .tablesorterPager({container: $('#pagerStickyList')})
            });
        </script>
        <form name="stickListForm" method="post" action="">
            <table align="left" width="100%">
                <tr style="width:100%">
                    <td align="left" width="100%">
                        <div id="pagerStickyList" class="pager" align="left" >
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option value="5">5</option>
                                <option selected value="10">10</option>
                                <option value="<%=list.getRowCount()%>">All</option>
                            </select>
                        </div>
                    </td>
                </tr>
                <tr style="width:100%">
                    <td style="width:100%">
            <table align="center" id="tablesorterSticky" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
                <thead>
                    <tr>
                        <%--<th nowrap><input type="checkbox" name="RepSelect" id="RepSelect1" onclick="return selectAllReps()">Select All</th>--%>
                        <th nowrap>Report Name</th>
                        <th nowrap>Sticky Name</th>
                        <th nowrap>Created On</th>
                        <th nowrap>Text</th>
                        <th nowrap>Delete</th>

                    </tr>
                </thead>
                <tbody>
                    <%--<tbody style="height:400px;overflow-x:hidden">--%>
                    <%int i = 0;
            for (i = 0; i < list.getRowCount(); i++) {%>
                    <tr>
                        <%-- <td style="width:30px">
                             <input type="checkbox" name="RepSelect" id="S_NOTE<%=(i + 1)%>" value="<%=list.getFieldValueString(i, 1)%>">
                         </td>--%>
                        <td>
                            <%=list.getFieldValueString(i, 0)%>
                        </td>

                        <td>
                            <%=list.getFieldValueString(i, 2)%>
                        </td>

                        <td>
                            <%=list.getFieldValueDateString(i, 4)%>
                        </td>
                        <%
                String sText = list.getFieldValueString(i, 3);
                if (sText.length() > 120) {
                    sText = sText.substring(0, 120) + "....";
                } else {
                    sText = sText;
                }
                        %>
                        <td class="wordStyle" id="hyperSticky" style="font-size:11px" title="Click To View Text" onclick="viewText('stickyText<%=i%>','<%=list.getFieldValueString(i, 2)%>')">
                            <%=sText%>
                        </td>
                        <td align="center">
                            <input type="hidden" name="stickyText" id="stickyText<%=i%>" value="<%=list.getFieldValueString(i, 3)%>">
                            <input type="button" class="navtitle-hover" value="Delete" id="delSticky" onclick="deleteSticky('<%=list.getFieldValueString(i, 1)%>','<%=userId%>')">
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="userId" id="userId" value="<%=userId%>">
        </form>
    </body>
</html>
