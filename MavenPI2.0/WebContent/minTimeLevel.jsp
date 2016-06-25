<%@page import="java.util.ArrayList"%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script>
            function getMinTimeLevel(userFolderIdsStr,path)
            {
                var str = userFolderIdsStr;
                alert(path+'/userFolderMinTime.do?userFolderTime=getUserFolderMinTimeLevel&userFolderIdsStr='+userFolderIdsStr)
                $.ajax({
                    url: path+'/userFolderMinTime.do?userFolderTime=getUserFolderMinTimeLevel&userFolderIdsStr='+userFolderIdsStr,
                    success: function(data) {
                        if(data != "")
                        {
                            document.getElementById("relatedTablesTree").innerHTML = data;
                        }
                    }
                });
            }
        </script>
    </head>
    <body >
<%
        ArrayList userFolderIds = new ArrayList();
        ////////////////////////////////////////////////////////////////////////////////////////////////.println.println("userFolderIds are:: "+userFolderIds);

        String userFolderIdsStr = null;
        for(int i=0;i<userFolderIds.size();i++)
        {
            if(userFolderIdsStr == null)
            {
                userFolderIdsStr = (String)userFolderIds.get(i);
            }
            else
            {
                userFolderIdsStr = userFolderIdsStr+","+userFolderIds.get(i);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////.println.println("userFolderIdsStr is:: "+userFolderIdsStr);

        


%>
<form action="" name="myForm" method="post">
click <a href="javascript:void(0)" onclick="getMinTimeLevel('<%=userFolderIdsStr%>','<%=request.getContextPath()%>')">here</a>
</form>
        
        <br>
           <span id="userFolderMinTimeLevel">50</span>
    </body>
</html>
