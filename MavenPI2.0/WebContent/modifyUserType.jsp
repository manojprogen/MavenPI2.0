<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,java.util.ArrayList" %>
<%@page import="prg.db.PbDb" %>
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
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
            <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/dragTable.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/scripts.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.tabs.js"></script>
      <script type="text/javascript">
     function modifyUserType()
     {
         var i=document.forms.modUserType.uType;
          var userType="";
         for(var g=0;g<i.length;g++)
             {
                 if(i[g].checked==true)
                     {
                         userType=i[g].value;

                     }
             }
           //  alert('userType '+userType)
        //document.forms.modUserType.uType.value;
         var userId=document.forms.modUserType.userId.value;
        // alert(userType+' userId '+userId)
        var t = confirm('It Will change the user Privileges. Are you sure');

        if(t==true)
        {
         $.ajax({
                //url: 'organisationDetails.do?param=deleteCompany&orgId='+orgId,
                 url: '<%=request.getContextPath()%>'+'/saveUserTypeAction.do?param=modifyUsertype&userId='+userId+'&userType='+userType,
                success: function(data) {
                    if(data==1)
                        {
                            parent.parentColseModUserTypePar();
                           //parentColseModUserType();
                        }
                     }
                });
        }
     }
     function parentColseModUserType()
     {
         parent.parentColseModUserTypePar();
     }
       </script>
    </head>
    <body>
        <%
            String userId=request.getParameter("userId");
            PbDb pbdb=new PbDb();
            String userTypeQ="select * from prg_user_type";
            String isSelected="";
            PbReturnObject pbro=pbdb.execSelectSQL(userTypeQ);
            String uQ="select * from prg_ar_users";
            PbReturnObject pbro2=pbdb.execSelectSQL(uQ);
            String uType="";
            for(int m=0;m<pbro2.getRowCount();m++)
            {
              if(userId.equalsIgnoreCase(pbro2.getFieldValueString(m,"PU_ID")))
                  {
                    uType=pbro2.getFieldValueString(m,"USER_TYPE");
                  }
            }
          //////////////////////////////////////////////.println.println(" uType "+uType);
        %>
        <Center>
            <form action="javascript:void(0)" name="modUserType" id="modUserType" method="post">
           <font size="3px"> Select New User Type</font><Br/>
            <Table>
                <%for(int m=0;m<pbro.getRowCount();m++){
                      isSelected="";
                     if(pbro.getFieldValueString(m,"USER_TYPE_ID").equalsIgnoreCase(uType))
                      isSelected="checked";
                    %>
                <Tr>
                    <Td><Input <%=isSelected%> type="RADIO" name="uType" id="uType" VALUE="<%=pbro.getFieldValueString(m,"USER_TYPE_ID")%>"></Td>
                    <Td><%=pbro.getFieldValueString(m,"USER_TYPE_NAME")%></Td>
                </Tr>
                <%}%>
            </Table>
            <Table>
                <Tr>
                    <Td><INPUT class="navtitle-hover"type="BUTTON" value="save" name="Save" ONCLICK="modifyUserType()"></Td>
                </Tr>
            </Table>
            <INPUT type="hidden" name="userId" id="userId" value="<%=userId%>">
        </form>
        </Center>
        </body>
</html>
