  <%@ page import="java.sql.*"%>
 <%@ page import="utils.db.ProgenConnection"%>
 <%@ page import="utils.db.JDBCConnectionPool"%>
 <%@ page import="prg.tracker.params.PbTrackerParams"%>
 <%@ page import="prg.tracker.client.PbTrackerManager"%>
 <%@ page import="prg.db.PbReturnObject"%>
 <%@ page import="prg.db.Session"%>
 <%@ page import="prg.db.PbDb"%>
 <%@ page import="utils.db.*"%>
 <%@ page import="prg.util.TableUtils"%>

<Head>
    <%
            DisplayFrame disp = new DisplayFrame("","Trackers");
            disp.setAdv();
            disp.setQuick("Value Analysis");
            disp.setCtxPath(request.getContextPath());
           out.println(disp.DisplayPreHead());
           %>
    <Script language="javascript" src="../JS/myScripts.js"></Script>
     <link href="../css/myStyles.css" rel="stylesheet" type="text/css">

       <%
          out.println(disp.DisplayPostHead());%>

</Head>
<Body>
     <%

           out.println(disp.DisplayPreBody());
           out.println(disp.DisplayPreRow());
           %>
    <Center>
    <Br>
    <Br>

        <%
       /*
        String userId=null;
        if (request.getParameter("userId")!=null ){
        userId = request.getParameter("userId");
        //////////////////////////////////////////////////////////////////////.println.println("User from paremeter id " + userId);
        }
        else if (session.getAttribute("userId")!=null){
        userId = (String)session.getAttribute("userId");
        //////////////////////////////////////////////////////////////////////.println.println("User from session " + userId);
        }

        session.setAttribute("userId",userId);
        //////////////////////////////////////////////////////////////////////.println.println("User id " + (String)session.getAttribute("userId"));

   */


        int rowCount=0;
        try
        {
        Session prgSession = new Session();
        PbTrackerManager Client = new PbTrackerManager();
        PbReturnObject retObj = Client.getTrackerList(prgSession);
        rowCount =retObj.getRowCount();
        retObj.writeString();
        //////////////////////////////////////////////////////////////////////.println.println("after list---------");

        String ImagePath=request.getContextPath()+"/images/*.gif";
        //checkboxes--checkbox, hyperlink--url,redio buttons --redio
        String[] columnURLS={"","",""};
        String[] columnTypes={"checkbox","",""};
        String[] columnURLSValues={"","",""};
        String[] columnNames={" _ ","Tracker_Name","Measure"};
        String[] tableColumnsNames={"TRACKER_ID","TRACKER_NAME","MEASURE"};
        int[] columnWidths={5,20,20};
        String[] defaultChecked= {"N"};
        //boolean[] checkboxDisabled={false,false,false,false,false,false,false};// use true to disable check box
        //String[] columnsVisibility ={"show","show","show","show","show","show","show"};//use hide  not to display the column
        boolean[] columnSortables={false,true,true};//use false  to restrict sorting
        boolean[] columnFilterble={false,true,true};//use false  to restrict filterable
        String[] checkBoxNames={"chk21","",""};//other than chk1 u can use any name
        String forwardTo=request.getContextPath()+"/tracker/JSPS/pbTrackerList.jsp";
        TableUtils TableUtilsRef= new TableUtils();
        TableUtilsRef.setPbReturnObject(retObj);
        TableUtilsRef.setTableColumnsNames(tableColumnsNames);
        TableUtilsRef.setColumnNames(columnNames);
        TableUtilsRef.setDefaults();
        TableUtilsRef.setColumnFilterble(columnFilterble);
        TableUtilsRef.setColumnSortable(columnSortables);
        TableUtilsRef.setColumnWidths(columnWidths);
        //TableUtilsRef.setPageContext(pageContext);
        //TableUtilsRef.settitle("Reports List");
        pageContext.setAttribute(TableUtilsRef.getItems(),TableUtilsRef.getTotalRecords());
        TableUtilsRef.setAction(request.getContextPath()+"//tracker/JSPS/pbTrackerList.jsp");
       // TableUtilsRef.setPageContext(pageContext);
        TableUtilsRef.setColumnTypes(columnTypes);
        TableUtilsRef.setColumnURLS(columnURLS);
        //TableUtilsRef.setImagePath(ImagePath);
        TableUtilsRef.setForwardTo(forwardTo);
       TableUtilsRef.setColumnURLSValues(columnURLSValues);
        TableUtilsRef.setDefaultChecked(defaultChecked);
        TableUtilsRef.setShowExports(false);
        //TableUtilsRef.setCheckboxDisabled(checkboxDisabled);
        TableUtilsRef.setCheckBoxNames(checkBoxNames);
        //TableUtilsRef.setRowsDisplayed(2);
        TableUtilsRef.execute();
        session.setAttribute("TableUtilsBean",TableUtilsRef);


    %>
    <jsp:include page="commonPage.jsp"/>

    <Br>
        <Form name="myForm2" method="post">
         <table>
             <tr>
                 <td>
                     <input typr="button" class="btn" value="" onclick="">
                 </td>
             </tr>

         </table>

    </Form>

    <%
    }
    catch(Exception ex)
    {
    //////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
    }

    out.println(disp.DisplayPostRow());
    out.println(disp.DisplayPostBody()); %>
   

