
<%--<%@page import="prg.report.param.security.PbReportParamGetAllChilds" %>--%>
<%@page import="utils.db.*,java.util.ArrayList,java.util.HashMap,java.util.Vector,prg.db.PbReturnObject"%>


<html>
    <head>
        <script>
        </script>
    </head>
    <%
                
        PbReturnObject pbro=new PbReturnObject();
        ArrayList kpiArray=(ArrayList)session.getAttribute("kpiIds");
        ArrayList grpArray=(ArrayList)session.getAttribute("grpArray");
        //String grpNames[] =new String[kpiArray.size()];
        ArrayList repNames = new ArrayList();
        HashMap kpiDrillRepMap=new HashMap();
        for(int k=0;k<kpiArray.size();k++)
        {
            repNames.add(request.getParameter("repName"+k));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---kpinames are : ----"+kpiArray.get(k));
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("repNames is:: "+repNames);
            kpiDrillRepMap.put(kpiArray.get(k), repNames.get(k));
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("repNames is:: "+repNames);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("kpiDrillRepMap is:: "+kpiDrillRepMap);
        
        session.setAttribute("kpiDrillRepMap", kpiDrillRepMap);

     /*   for (int i = 0; i < kpiArray.size(); i++)
        {
            //for(int j=0;j<grpNames.size();j++)
            //{
                //pbro=repClient.getDrillReportId(Integer.parseInt((String)grpNames.get(j)));
            if((String.valueOf(grpNames.get(i))=="null") || (String.valueOf(grpNames.get(i))==""))
            {
                dashBrdParams.setDrillReport(0);
                dashBrdParams.setRefReportId(0);
            }
            else
            {
                dashBrdParams.setDrillReport(Integer.parseInt((String.valueOf(grpNames.get(i)))));
                dashBrdParams.setRefReportId(Integer.parseInt((String.valueOf(grpNames.get(i)))));
            }
                dashBrdParams.setDashBoardId(reportId);
            if((String.valueOf(grpArray.get(i))=="null") || (String.valueOf(grpArray.get(i))==""))
                dashBrdParams.setRefGraph(0);
            else
                dashBrdParams.setRefGraph(Integer.parseInt((String)grpArray.get(i)));
                repSession.setObject(dashBrdParams);
                repClient.updateDashboardDetails(repSession);
            //}
        }     */
    %>
</html>
