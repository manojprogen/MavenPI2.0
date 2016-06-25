<%@page import="prg.db.Container"%>
<%@page import="prg.db.PbReturnObject,prg.db.PbDb"%>
<%@ page import="java.util.HashMap" %>
<%@page import="prg.util.PbExcelDriver"%>
<%@page import="prg.util.PbExcelGenerator"%>
<%@page import="prg.util.PbPDFDriver"%>
<%@page import="java.util.*" %>
<%@page import="com.progen.report.charts.PbGraphDisplay"%>

<%


        PbDb pbdb = new PbDb();
        //////////////////////////////////////////.println("---------------");
        String Basis=request.getParameter("Basis");
        ////////////////////////.println(" Basis =-=- "+Basis);
        ArrayList rowEdgevalues = (ArrayList) session.getAttribute("rowEdgeValuesA");
        ArrayList columnEdgeValues = (ArrayList) session.getAttribute("columnEdgeValuesA");
        HashMap originalResult=(HashMap) session.getAttribute("originalResult");
        String primaryAnalyze=(String) session.getAttribute("primaryAnalyze");
        String secAnalyze=(String) session.getAttribute("secAnalyze");
        String periodType=(String) session.getAttribute("periodType");
        String startRange=(String) session.getAttribute("startRange");
        String endRange=(String) session.getAttribute("endRange");


        String dataQuery2=(String) session.getAttribute("dataQuery2");
        String dataQuery=(String) session.getAttribute("dataQuery");
        String parentDataQ=(String) session.getAttribute("parentDataQ");
        String parentDataQ2=(String) session.getAttribute("parentDataQ2");
         if(dataQuery2.equalsIgnoreCase(""))
            dataQuery2="No";
        if(dataQuery.equalsIgnoreCase(""))
            dataQuery="No";
        if(parentDataQ.equalsIgnoreCase(""))
            parentDataQ="No";
        if(parentDataQ2.equalsIgnoreCase(""))
            parentDataQ2="No";

        
        HashMap nonAllIds=new HashMap();
        HashMap RestrictingTotal=(HashMap)session.getAttribute("RestrictingTotal");
        //////////////////////////////////////////.println(" in djj "+periodType);

        HashMap boxNames = new HashMap();
        Enumeration ee = request.getParameterNames();
        while (ee.hasMoreElements()) {
            String reqKey = (String) ee.nextElement();
            String val = request.getParameter(reqKey);
            if (reqKey.startsWith("CBOARP")) {
                String elementId = reqKey.substring(6);
                if (val.equalsIgnoreCase("All")) {
                    nonAllIds.put(elementId, "-1");
                } else {
                    nonAllIds.put(elementId, val);
                }
            }
          }
        //////////////////////////////////////////.println(" nonAllIds in jsp "+nonAllIds);
        try
        {
                String rowViewBys[]=new String[rowEdgevalues.size()-1];//{"A","B","c","D"};
                String colViewBys[]=new String[columnEdgeValues.size()];//{"1","2","3","4"};
                for(int m=1,n=0;m<=rowViewBys.length;m++,n++)
                    rowViewBys[n]=(String)rowEdgevalues.get(m).toString();
                for(int m=0;m<colViewBys.length;m++)
                    colViewBys[m]=(String)columnEdgeValues.get(m).toString();

                for(int y=0;y<colViewBys.length;y++)
                    {
                    ////////////////////////.println(" colViewBys-= in jsp "+colViewBys[y]);
                    }
                ////////////////////////.println(" colViewBys.lengthin jsp-  "+colViewBys.length);
                String targetId = request.getParameter("TARGETID");
               //String targetId ="10";
                String finalQuery = "SELECT  TARGET_NAME FROM TARGET_MASTER where TARGET_ID=" + targetId;
                PbReturnObject pbro = pbdb.execSelectSQL(finalQuery);
                String targetName = pbro.getFieldValueString(0, 0);
                ////////////////////////.println("targetName--" + targetName);
                ////////////////////////.println(colViewBys+" =--=  rowViewBys "+rowViewBys);
                ////////////////////////.println(" columnEdgeValues "+columnEdgeValues);
                ////////////////////////.println(" rowEdgevalues "+rowEdgevalues);
                String primaryViewName="";
                primaryViewName=rowEdgevalues.get(0).toString();

                //////////////////////////////////////////.println("primaryAnalyze--" + primaryAnalyze);

                //////////////////////////////////////////.println("secAnalyze--" + secAnalyze);
                PbExcelGenerator pbexgen=new PbExcelGenerator();
                pbexgen.setPrimList(rowEdgevalues);
                pbexgen.setSecList(columnEdgeValues);
                pbexgen.setTargetName(targetName);
                pbexgen.setDisplayColumns(colViewBys);
                pbexgen.setTargetDisplayRows(rowViewBys);
                pbexgen.setFileName("Target-"+targetId+".xls");
                pbexgen.setResponse(response);
                pbexgen.setRequest(request);
                pbexgen.setTargetOriMap(originalResult);
                pbexgen.setTargetId(targetId);
                pbexgen.setPeriodType(periodType);
                pbexgen.setPrimaryAnalyze(primaryAnalyze);
                pbexgen.setSecAnalyze(secAnalyze);
                pbexgen.setNonAllVals(nonAllIds);
                pbexgen.setStartRange(startRange);
                pbexgen.setPrimaryViewByName(primaryViewName);
                pbexgen.setEndRange(endRange);
                pbexgen.setRestrictingTotal(RestrictingTotal);
                //added on 15-02-2010
                pbexgen.setDataQuery(dataQuery);
                pbexgen.setDataQuery2(dataQuery2);
                pbexgen.setParentDataQ(parentDataQ);
                pbexgen.setParentDataQ2(parentDataQ2);
                pbexgen.setTargetBasis(Basis);
                
                pbexgen.createExcelForTarget();
        }
        catch(Exception e)
        {
        e.printStackTrace();
        }
   
%>