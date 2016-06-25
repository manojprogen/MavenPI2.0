
<%@page import="java.sql.*"%>
<%@page import="utils.db.ProgenConnection"%>
<%@page import="utils.db.JDBCConnectionPool"%>
<%@ page import="prg.personalisedreps.client.PbPersonalisedrepManager"%>
<%@page import="prg.personalisedreps.params.PbPersonalisedrepParams" %>
<%@page import="prg.db.PbReturnObject" %>
<%@ page import="prg.db.Session"%>
<%@ page import="prg.util.TableUtils"%>
<%@ page import="utils.db.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<Head>

     <%/***** Report Comments
             Report Name has been Assigned to report to display it in report area
             **/
            String ReportId = request.getParameter("REPORTID");
            DisplayFrame disp = new DisplayFrame("","Personalised Reports");

            /***** Report Comments
             Tabs are added as per current design
             **/


            /***** Report Comments
             Advertisment and Report quick fact have been set. They stored in database and parameter will drive the contents
             **/
            disp.settab(request.getContextPath()+"/reportRunner.jsp?REPORTID="+ReportId,"Reports");
            disp.settab(request.getContextPath()+"/Copy/Jsps/Customize.jsp?REPORTID="+ReportId,"My Customized Reports");
            disp.setselectedtab(request.getContextPath()+"/PersonalisedReports/JSPS/pbPersonalisedReportsList.jsp?REPORTID="+ReportId," Snapshot");
            disp.setProgenUtilitiesStatus(false);
            disp.setComposeStatus(false);
            disp.setCustomStatus(false);
            disp.setAdv();
            String cPath=request.getContextPath();
            String UserId=null ;
         if (session.getAttribute("ID")!=null)
             UserId = (String)session.getAttribute("ID");
         else if (request.getParameter("userId")!=null )
             UserId = request.getParameter("userId");
          else if (session.getAttribute("userid")!=null)
             UserId = (String)session.getAttribute("userid");
            disp.setCtxPath(request.getContextPath());
            disp.setPbUserId(Integer.parseInt(UserId));
            disp.setQuick("Value Analysis");
            //out.println(disp.DisplayPreHead());
           %>
     <Script language="javascript"  src="../JS/myPersonalisedReport.js"></Script>
     <link href="../css/myStyles.css" rel="stylesheet" type="text/css">

      <%
          //out.println(disp.DisplayPostHead());%>
</Head>
<Body>
     <%

          // out.println(disp.DisplayPreBody());
          // out.println(disp.DisplayPreRow());
           %>

<Center>
<Br>
<Br>

    <%

    int rowCount=0;
    try
    {   
        Session prgSession = new Session();
        PbPersonalisedrepManager params = new PbPersonalisedrepManager();
        PbPersonalisedrepManager Client = new PbPersonalisedrepManager();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in shared reports list");
        PbReturnObject retObj = Client.getSharedReportList(UserId);
        rowCount =retObj.getRowCount();
        retObj.writeString();
       //out.println("In Else Block");
       String ImagePath=request.getContextPath()+"/images/*.gif";
       String[] columnURLS={"","",""};
       String[] columnURLSValues={"","","PATH"};
        //checkboxes--checkbox, hyperlink--url,redio buttons --redio
        String[] columnTypes={"checkbox","","url"};
        String[] columnNames={"ID","Report_Cust_Name","Click Here to View"};
        String[] tableColumnsNames={"PRG_PERSONALIZED_ID","PRG_REPORT_CUST_NAME","VIEW1"};
        int[] columnWidths={5,40,40};
        String[] defaultChecked= {"N"};

        boolean[] columnSortables={false,true,false};//use false  to restrict sorting
        boolean[] columnFilterble={false,true,false};//use false  to restrict filterable
        String[] checkBoxNames={"chk2","",""};//other than chk1 u can use any name
        String forwardTo=request.getContextPath()+"/Bharathi/PersonalisedReports/JSPS/pbSharedReportList.jsp";
        TableUtils TableUtilsRef= new TableUtils();
        TableUtilsRef.setPbReturnObject(retObj);
        TableUtilsRef.setTableColumnsNames(tableColumnsNames);
        TableUtilsRef.setColumnNames(columnNames);
        TableUtilsRef.setFilterable(true);
        TableUtilsRef.setDefaults();
        TableUtilsRef.setColumnFilterble(columnFilterble);
        TableUtilsRef.setColumnSortable(columnSortables);
        TableUtilsRef.setColumnWidths(columnWidths);
        pageContext.setAttribute(TableUtilsRef.getItems(),TableUtilsRef.getTotalRecords());
        TableUtilsRef.setAction(request.getContextPath()+"/Bharathi/PersonalisedReports/JSPS/pbSharedReportList.jsp");
        TableUtilsRef.setPageContext(pageContext);
        TableUtilsRef.setColumnTypes(columnTypes);
        TableUtilsRef.setDefaultChecked(defaultChecked);
        TableUtilsRef.setShowExports(false);
        TableUtilsRef.setColumnURLS(columnURLS);
        TableUtilsRef.setForwardTo(forwardTo);
        TableUtilsRef.setColumnURLSValues(columnURLSValues);
        //TableUtilsRef.setCheckboxDisabled(checkboxDisabled);
        TableUtilsRef.setCheckBoxNames(checkBoxNames);
        TableUtilsRef.execute();
        session.setAttribute("TableUtilsBean",TableUtilsRef);
        //String pagePath=request.getContextPath()+"/commonPage.jsp";


    %>
    <jsp:include page="commonPage.jsp"/>




    <Form name="myForm2">
    <Table>
        <Tr>
            <Td></Td>
           <%-- <Td><Input class="btn" type="button" value="Cancel" onclick="checkMain('<%=request.getContextPath()%>')"></Td> --%>
             <% // if(rowCount>0)
            // {%>
           <%-- <Td><Input class="btn" type="button" value="Create" onclick="javascript:checkCreate('pbPersonalisedReportRegister.jsp');"></Td> --%>
                <Td><Input class="btn" type="button" value="Edit" onclick="javascript:checkEdit('Report','pbPersonalisedReportGetUpdate.jsp');"></Td>
                 <Td><Input class="btn" type="button" value="Delete" onclick="javascript:checkDelete('Report','pbPersonalisedReportDelete.jsp');"></Td>
         <%//}
           //   else{
           //     %>
              <%--  <Td><Input class="btn" type="button" value="Create" onclick="javascript:checkCreate('pbPersonalisedReportRegister.jsp');"></Td> --%>

            <%//}%>
         </Tr>

    </Table>
</Form>

<form name="frmParameter"  method=post ><input type="hidden" value="1" ></form>
<%
       // }
    %>
    <%
        }
    catch(Exception ex)
    {
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
    }
    %>
    <% // out.println(disp.DisplayPostRow());
   // out.println(disp.DisplayPostBody()); %>
</Body>
</Html>













