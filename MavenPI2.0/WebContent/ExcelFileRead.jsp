<%-- 
    Document   : ExcelFileRead
    Created on : Feb 10, 2014, 2:18:22 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.Container,java.util.ArrayList,java.util.HashMap"%>
<!DOCTYPE html>
<%
   String themeColor="blue";  
   String reportId="";
   HashMap excelMap;
   ArrayList excelNamelst;
   ArrayList excelTypelist;
   ArrayList<String> rowViewbyIds = new ArrayList<String>();
   ArrayList<String> rowViewbyNames = new ArrayList<String>();
   String importExcelFrmReport=request.getAttribute("importExcelFrmReport").toString();
//   
   reportId=request.getAttribute("reportId").toString();
   excelMap=(HashMap)request.getAttribute("excelDeatilsMap");
   excelNamelst=(ArrayList)excelMap.get("excelNames");
   excelTypelist=(ArrayList)excelMap.get("excelcolType");
   Container container=Container.getContainerFromSession(request, reportId);
   rowViewbyIds = (ArrayList) container.getTableHashMap().get("REP");        
   rowViewbyNames = (ArrayList) container.getTableHashMap().get("REPNames");
   int[] optionVal={1,0,4};
   String[] option={"String","Number","Boolean"};
   String contextPath=request.getContextPath();
  

//   
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
<!--               <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />-->
        <title>JSP Page</title>
        <script type="text/javascript"> 
           var ctxPath='<%=request.getContextPath()%>';
       $(document).ready(function(){
         parent.$("#importExcelFileDiv").width(950).height(350);
      });            
            function closeUploadFile(){
              parent.$("#importExcelFileDiv").dialog('close');        
              alert("File has been Uploaded succesfully");
              parent.$("#mappingExcelFileDiv").dialog('open');
              parent.$("#mappingExcelFileDiv").html($("#readExcelDiv").html());
//              parent.$("#importExcelFileDiv").html($("#readExcelDiv").html());
            }
        </script>
    </head>
    <body>
         
        <div id="readExcelDiv" name="readExcelDiv" style="width:300px;height:200px; display:none">
            <form id="ExcelFileForm" name="ExcelFileForm" method="post" style="width: auto">
            <table style="width:100%">
                <thead>
                    <tr><th>Column Name</th>
                        <th>Display Name</th>
                        <th>Type</th>
                        <th>Dim Mapped</th>
                        <th>Aggregation</th>
                        <th>Data Type</th>
                    </tr>
                </thead>
               <% for(int i=0;i<excelNamelst.size();i++){ %>
               <tr>
                   <td><input type="text" style="width:150px;" name="excelcolName" value="<%=excelNamelst.get(i)%>" readonly></td>
                   <td><input type="text" style="width:150px;" name="dispColName" value="<%=excelNamelst.get(i)%>" ></td>
                   <td>
                       <select id="Type<%=i%>" name="Type" style="width:100px;">                          
                           <%if(Integer.parseInt(excelTypelist.get(i).toString())==1){%>
                            <option  selected="" value="D">Dimenssion</option>
                            <option value="M">Measure</option>
                           <%}else{%>                        
                            <option value="M">Measure</option>
                            <option value="D">Dimenssion</option>
                           <%}%>
                       </select>
                   </td> 
                   <td>
                       <select id="MappingDim<%=i%>" name="MappingDim" style="width:120px;">
                      <%for(int j=0;j<rowViewbyIds.size();j++){
                          if(rowViewbyNames.get(j).equalsIgnoreCase(excelNamelst.get(i).toString())){%>
                          <option value="<%=rowViewbyIds.get(j)%>" selected><%=rowViewbyNames.get(j)%></option>
                      <%}else{%>
                      <option value="<%=rowViewbyIds.get(j)%>"><%=rowViewbyNames.get(j)%></option>
                      <%}}%>
                       </select>
                   </td>
                   <td>
                       <select id="Aggregation<%=i%>" name="Aggregation" style="width:120px;">
                           <option value="SUM">SUM</option>
                           <option value="AVG">AVG</option>
                           <option value="MIN">MIN</option>
                           <option value="MAX">MAX</option>
                           <option value="COUNT">COUNT</option>
                           <option value="COUNTDISTINCT">COUNTDISTINCT</option>
                       </select>
                   </td>
                   <td>
                       <select id="DataType<%=i%>" name="DataType" style="width:120px;">
                           <%for(int k=0;k<optionVal.length;k++){
                               if(Integer.parseInt(excelTypelist.get(i).toString())==optionVal[k]){%>
                               <option value="<%=option[k]%>" selected><%=option[k]%></option> 
                               <%}else{%>
                               <option value="<%=option[k]%>" ><%=option[k]%></option> 
                               <%}
                              }%>
                               <option value="Date" >Date</option> 
                       </select>
                   </td>
               </tr>
               <%}%>
               <tr>
               <br><br>
               <td align="center" colspan="6"><input type="button" class="navtitle-hover" value="Done" onclick="saveExcelFileDetails('<%=request.getContextPath()%>','<%=reportId%>')"></td>
               </tr>
            </table>
            </form>
        </div>
        
    </body>
</html>
<script type="text/javascript">
            this.closeUploadFile();
        </script>