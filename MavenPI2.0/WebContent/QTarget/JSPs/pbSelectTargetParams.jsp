<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="java.util.ArrayList"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <Script language="javascript"  src="../JS/myScripts.js"></Script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <%--<script>
            window.onload = function(){
				var pw = window.opener;
				if(pw){
					var inputFrm = pw.document.forms['myForm'];
					var outputFrm = document.forms['myForm'];

					outputFrm.elements['txtasno'].value = inputFrm.elements['targetName'].value;
					outputFrm.elements['txtremarks'].value = inputFrm.elements['country1'].value;
					outputFrm.elements['txtsperson'].value = inputFrm.elements['measure'].value;
                    outputFrm.elements['txtsperson'].value = inputFrm.elements['primaryDimension'].value;
				}
			}
        </script>
        --%>
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        <script>
            function resetAndClose()
            {
                var pw = window.opener;
				if(pw)
                {

                    var inputFrm = pw.document.forms['myForm'];
                    pw.document.getElementById("parameters2").checked=false;
                    //inputFrm.elements['parameters'][0].checked = false;
                    window.close();
                }
            }
        </script>
         <style>
            *{font:12px verdana;color:#000}
        </style>
    </head>


    <body>
<%
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        //String businessAreaName = request.getParameter("businessArea");
        String selectedMeasure = request.getParameter("selectedMeasure");
        //////////////////////////////////////////////////////////////////////////////.println("selectedMeasure si:: "+selectedMeasure);
        session.setAttribute("selectedMeasure",selectedMeasure);
        String selectedParamIds = request.getParameter("parameterIds");
        String[] selectedPIds = null;
        ArrayList selectedIds = new ArrayList();
        selectedPIds = selectedParamIds.split(",");
        for(int i=0;i<selectedPIds.length;i++)
        {
            selectedIds.add(selectedPIds[i]);
        }
        //////////////////////////////////////////////////////////////////////////////.println("selectedIds are:::::: "+selectedIds);

        targetParams.setMeasureId(selectedMeasure);
        targetSession.setObject(targetParams);
        PbReturnObject parameterNames = targetClient.getMeasureParameters(targetSession);

        int rowCount = parameterNames.getRowCount();
        ArrayList paramIds = new ArrayList();
        String parameterId = null;
        for(int m=0;m<rowCount;m++)
        {
            parameterId = String.valueOf(parameterNames.getFieldValueInt(m,"MEMBER_ID"));
            //////////////////////////////////////////////////////////////////////////////.println("parameterId is: "+parameterId);
            paramIds.add(parameterId);
        }




%>
<br><br><br>
 <center>
    <h2>Parameter(s) Selection</h2>
    <br>
    <form name="myForm" method="post">

        <table border="0" width="40%">
<%
        if((paramIds.size()==selectedIds.size()) || selectedParamIds=="")
        {
%>
            <tr>
                <th width="5%"><input checked type="checkbox" name="checkCtr" value="yes" onclick="CheckAll(document.myForm.chk1)"></th>
                <th width="95%" align="left"><font style="font-weight:bold">Parameter Names</font></th>
            </tr>
<%
        }
        else
        {
%>
            <tr>
                <th width="5%" ><input type="checkbox" name="checkCtr" value="yes" onclick="CheckAll(document.myForm.chk1)"></th>
                <th width="95%" align="left" >Parameter Name</th>
            </tr>
<%
        }
%>

<%
            if(selectedParamIds=="")
            {
                for(int i=0;i<rowCount;i++)
                {
                    String paramName = parameterNames.getFieldValueString(i,"MEMBER_NAME");
%>
            <tr>
                <td align="center"> <input checked type="checkbox" name="chk1" value="<%=paramIds.get(i)%>" onclick="CheckTop2(document.myForm.checkCtr)"> </td>
                <td ><%=paramName%></td>
            </tr>
<%
                }
            }
            else
            {
                for(int i=0;i<rowCount;i++)
                {
                    String paramName = parameterNames.getFieldValueString(i,"MEMBER_NAME");
                    if(selectedIds.contains(paramIds.get(i)))
                    {
%>
            <tr>
                <td  align="center"> <input checked type="checkbox" name="chk1" value="<%=paramIds.get(i)%>" onclick="CheckTop2(document.myForm.checkCtr)"> </td>
                <td><%=paramName%></td>
            </tr>
<%
                    }
                    else
                    {
%>
            <tr>
                <td  align="center"> <input type="checkbox" name="chk1" value="<%=paramIds.get(i)%>" onclick="CheckTop2(document.myForm.checkCtr)"> </td>
                <td ><%=paramName%></td>
            </tr>
<%
                    }
                }
            }
%>
        </table>
        <br>
        <table>
            <tr>
                <td>
                    <%--<input type="button" class="btn" value="Save" onclick="saveTargetParameters();">--%>
                    <input type="button" class="navtitle-hover" value="Save" onclick="checkHierarchy();">
                </td>
                <td>
                    <input type="button" class="navtitle-hover" value="Cancel" onclick="resetAndClose()">
                </td>
                
            </tr>
        </table>
    </form>
</center>


    </body>
</html>
