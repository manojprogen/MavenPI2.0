<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="java.util.ArrayList"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <Script language="javascript"  src="../JS/myScripts.js"></Script>
         <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
    </head>
    <%
    String path = request.getContextPath();
    %>
    <body>
<%
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        String userId = (String)session.getAttribute("userId");
        ////////////////////////////////////////////////////////////////////////.println("userId in pbCheckLock page is: "+userId);

        String selectedTargetId = request.getParameter("chk1");
        ////////////////////////////////////////////////////////////////////////.println("selectedTargetId in pbCheckLock is: "+selectedTargetId);
        String timeLevel = request.getParameter("minTimeLevel");
        //////////////////////////////////////////.println("minTimeLevel is: "+timeLevel);

        PbReturnObject getAllLocks = targetClient.getAllLocks(targetSession);
        int count = getAllLocks.getRowCount();
        ArrayList keyValues = new ArrayList();
        int str = 0;
        ////////////////////////////////////////////////////////////////////////.println("Locks count is:: "+count);
        if(count != 0)
        {
            for(int i=0;i<count;i++)
            {
                
                keyValues.add(String.valueOf(getAllLocks.getFieldValueInt(i,"KEY_VALUE")));
                
            }
            ////////////////////////////////////////////////////////////////////////.println("all keyValues are: "+keyValues);

            for(int j=0;j<keyValues.size();j++)
            {
                if(selectedTargetId.equalsIgnoreCase((String)keyValues.get(j)))
                {
                    str = 1;
                }
            }
        }        

        if(str==1)
        {
            session.setAttribute("TI",selectedTargetId);
            response.sendRedirect("pbDisplayWarning.jsp");
        }
        else
        {
            session.setAttribute("TI",selectedTargetId);
            session.setAttribute("targetId",Integer.valueOf(Integer.parseInt(selectedTargetId)));
            targetParams.setTargetId(selectedTargetId);
            targetParams.setUserId(userId);
            targetSession.setObject(targetParams);
        
            targetClient.addLock(targetSession);
            //////////////////////////////////////////.println(" path --."+path);
            
            String url = path+"/targetView.do?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel;
            response.sendRedirect(url);
        }

%>
    </body>
</html>
