<%-- 
    Document   : KpiTableProperties
    Created on : Sep 24, 2014, 7:02:45 PM
    Author     : Sandeep
--%>

<%@page import="com.progen.report.data.TableHeaderRow,java.lang.reflect.Type,com.progen.report.data.DataFacade,com.google.gson.Gson,java.lang.reflect.Type,com.google.gson.reflect.TypeToken,com.progen.report.data.TableBuilder,com.progen.report.data.RowViewTableBuilder"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.HashMap,java.util.HashMap,java.util.Set,prg.db.PbReturnObject,prg.db.ContainerConstants,prg.db.Container,java.util.ArrayList,com.progen.reportview.bd.PbReportViewerBD"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%

   String themeColor="blue";
    String reportId = request.getParameter("reportId");
    Container container = null;
    String val;
    String val11="";
    String valelement;
    String valelement1="";
   String measurevalintial="";
    String result="";
    PbReportViewerBD KPIDashboardBD=new PbReportViewerBD();
     ArrayList<String> DisplayLabels=new ArrayList();
     ArrayList<String> DisplayColumns=new ArrayList();
    int rowViewByCount = -1;
            int columnViewBycount = -1;
            int ViewByCount=-1;
            int count=0;
            int CloumnCount=10;
            boolean showExtraTabs = true;
       if (reportId != null) {
                if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                    container = (Container) map.get(reportId);
                    columnViewBycount = Integer.parseInt(container.getColumnViewByCount());
                    rowViewByCount = (container.getViewByCount());
                    ViewByCount=(container.getViewByCount());
                    
                     DisplayLabels=container.getDisplayLabels();
                     DisplayColumns=container.getDisplayColumns();
             count = container.getkpiTopBottomCount();
             PbReturnObject DataObject=(PbReturnObject) container.getRetObj();
              
             if(count<=0){
                    count=CloumnCount;
}
             int RowCount=DataObject.getRowCount();
             if(RowCount<count){
    count=RowCount;
    }
                    }
                }
DataFacade facade = new DataFacade(container);
 TableBuilder tableBldr;
 tableBldr = new RowViewTableBuilder(facade);
 TableHeaderRow[] headerRows;
         headerRows = tableBldr.getHeaderRowData();
           ArrayList<String> columnviews=new ArrayList();
         columnviews= container.getReportCollect().reportColViewbyValues;
          ArrayList<String> sortCols =new ArrayList();
         sortCols = container.getSortColumns();
          char[] sortTypes = null;
          sortTypes = container.getSortTypes();
          String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Properties</title>
        <style type="text/css" >
            *{font : 11px verdana}
        </style>
            <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/jquery-1.3.2.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />


        
    </head>
    <body>
        <form action="" name="grpProForm" id="grpProForm" method="post">
            <table  width="100%" >
                <%-- <%if(!facade.isReportCrosstab()){%>--%>
                
                <tr width="50%" height="45px">

                    <td width="50%" align="center" > Select no of coloumns:</td>
                        <td width="%" > <input id="noofcol"  value="<%=count%>" type="text" style="width:35%"name="Save" >
                    </td>

                </tr>

                <tr width="50%" height="45px">
                    <td width="50%" align="center" > Sort On:</td>
                    <td width="%" > <select id='measurekpi' class='myTextbox3' >
                            <%
                           if(facade.isReportCrosstab()){
 for ( TableHeaderRow headerRow : headerRows ){
   val11= headerRow.getRowData(0);
   valelement1 = "A_"+columnviews.get(0);

break;
 }
 %>
 <option value="<%=valelement1%>"><%=val11%></option>
      <%
      for (int MsrsLngth1 = 0; MsrsLngth1 < DisplayLabels.size(); MsrsLngth1++) {
          val11 = DisplayLabels.get(0);
                                            valelement1 = DisplayColumns.get(0);
break;
          }
 %>
 <option value="<%=valelement1%>"><%=val11%></option>
  <%
 for (int MsrsLngth = 1; MsrsLngth < DisplayLabels.size(); MsrsLngth++) {
    ArrayList<String> crosstabvales = new ArrayList<String>();
 Object paramObject=null;
  paramObject=DisplayLabels.get(MsrsLngth);
  
 Type listOfTestObject = new TypeToken<ArrayList>(){}.getType();
                Gson gson = new Gson();
   String  paramObject1=gson.toJson(paramObject, listOfTestObject);
//                 paramObject1=(String) paramObject;

                                    String[] vals= paramObject1.split(",");
                                    for(int i=0; i<vals.length;i++){
                                        String v1=vals[i];
                                        v1=v1.replace("[", "");
                                      crosstabvales.add(v1);
}
                                    
             
        String measureval = crosstabvales.get(1).replace("[", "").replace("\"", "").replace("]", "");
   if(measurevalintial!=null && measurevalintial!="" && measurevalintial.equals(measureval)){

break;
}

if(measurevalintial=="" || measurevalintial==null){
measurevalintial=measureval;
}
        val11=measureval;
 valelement1 = DisplayColumns.get(MsrsLngth);
%>

 <option value="<%=valelement1%>"><%=val11%></option>
     <%
  
}      }else{
                                        for (int MsrsLngth = 0; MsrsLngth < DisplayLabels.size(); MsrsLngth++) {
                                            val = DisplayLabels.get(MsrsLngth);
                                            valelement = DisplayColumns.get(MsrsLngth);
                                            if(sortCols!=null  && sortCols.size()>0){
                                        for (int MsrsLngth1 = 0; MsrsLngth1 < sortCols.size(); MsrsLngth1++) {
                                        String valelement11 = sortCols.get(MsrsLngth1);
                                        
                                        if(valelement.equals(valelement11)){
%>
 <option  selected value="<%=valelement11%>"><%=val%></option>
                                                <%}
                                        else{%>
                                          <option value="<%=valelement%>"><%=val%></option>
                                             <%}
                                            }
                                                }else{
                            %>
                            <option value="<%=valelement%>"><%=val%></option>

                            <%  }
                                        }
                                        }
                            %>

                        </select>
                    </td>
                <td width="50%" align="center" > Sorting type:</td>
<!--                    <Td><select id='topbottom' class='myTextbox3' >
 <option value="5" >top5</option>
 <option value="10" >top10</option>
 <option value="5" >bottom5</option>
 <option value="10" >bottom10</option>
                        </select>
                    </Td>-->
<td width="%" ><select id='assendessend' class='myTextbox3' >
        <%
        if(sortTypes!=null){
             for ( int i=0; i<sortTypes.length; i++ ){
                 if(sortTypes[i]=='0'){%>
                      <option selected value="Sort Ascend" >Sort Ascend</option>
                      <option value="Sort Descend" >Sort Descend</option>
                <% }else{%>
                     <option selected value="Sort Descend" >Sort Descend</option>
                      <option value="Sort Ascend" >Sort Ascend</option>
            <%}

             }
        }else{
        %>
 <option value="Sort Ascend" >Sort Ascend</option>
 <option value="Sort Descend" >Sort Descend</option>
 <%}
%>
                        </select>
                    </td>
                </tr>
                    <br>
                 <Tr height="25px" align="bottom">
                    <td align="center" colspan="9">
                        <input type="button" name="Save" value="Done" class="navtitle-hover" onclick="kpitopbottom()">
                    </td>
                </Tr>
                <%--<%}else{%>
                <tr width="50%" height="45px">
                    <td width="50%" align="center" > Sort On:</td>
                   <td width="%" ><select id="SortMeasure" name="SortMeasure" class="myTextbox5">
   <%
 for (int MsrsLngth = 1; MsrsLngth < DisplayLabels.size(); MsrsLngth++) {
    ArrayList<String> crosstabvales = new ArrayList<String>();
 Object paramObject=null;
  paramObject=DisplayLabels.get(MsrsLngth);

 Type listOfTestObject = new TypeToken<ArrayList>(){}.getType();
                Gson gson = new Gson();
   String  paramObject1=gson.toJson(paramObject, listOfTestObject);
//                 paramObject1=(String) paramObject;

                                    String[] vals= paramObject1.split(",");
                                    for(int i=0; i<vals.length;i++){
                                        String v1=vals[i];
                                        v1=v1.replace("[", "");
                                      crosstabvales.add(v1);
}


        String measureval = crosstabvales.get(1).replace("[", "").replace("\"", "").replace("]", "");
   if(measurevalintial!=null && measurevalintial!="" && measurevalintial.equals(measureval)){

break;
}

if(measurevalintial=="" || measurevalintial==null){
measurevalintial=measureval;
}
        val11=measureval;
 valelement1 = DisplayColumns.get(MsrsLngth);
%>

 <option value="<%=valelement1%>"><%=val11%></option>
     <%}%>
                   </select></td></tr>

                <%}%>--%>
            </table>
        </form>
            <script type="text/javascript" >
//  $(document).ready(function(){
//      document.getElementById('noofcol').value = '<%=count%>';
//
//        });
            function kpitopbottom(columnName,disColumnName,REPORTID,noofRows,ctxPath){
                var disColumnName= $( "#measurekpi option:selected" ).text();
                var columnName= $( "#measurekpi option:selected" ).val();
                var noofRows= $( "#topbottom option:selected" ).val();
                var noofRowstext= $( "#topbottom option:selected" ).text();
                var assenddessnd= $( "#assendessend option:selected" ).val();
                var noofcol= $( "#noofcol" ).val();
                var toprows;
if(noofRowstext=='bottom5' || noofRowstext=='bottom10'){
    toprows='bottomrows';
}else{
toprows='toprows';
}
          ctxPath="<%=request.getContextPath()%>";
                REPORTID=<%=reportId%>
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=kpitableChanges&tableChange=tableProperties&sort="+assenddessnd+"&source=S&sortColumn="+columnName+"&topbottomCount="+noofRows+"&noofcol="+noofcol+"&tabId="+REPORTID,
                function(data){
//                    alert(data)
  parent.$("#kpidispTabProp").dialog('close');
                    parent.$("#KpiDashboardDiv").html(data);
                });

                
//    var source = "newKpiDashboard.jsp?sourceValue=toprows&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID;
   
    }


        </script>
    </body>
</html>
