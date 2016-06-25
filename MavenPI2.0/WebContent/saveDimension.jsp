<%@page import="prg.db.PbDb,com.progen.reportview.db.ProgenReportViewerDAO"%>
<%
     //Connection con=null;
//    Statement st=null;
//    ResultSet rs=null;
try{
    String name=request.getParameter("dimensionName");
    String desc=request.getParameter("desc");
    ProgenReportViewerDAO dao=new ProgenReportViewerDAO();
    dao.insertSaveDiemension(name,desc);
    
//Class.forName("oracle.jdbc.driver.OracleDriver");
//con=ProgenConnection.getInstance().getConnection();
//
//st=con.createStatement();

//String query = "insert into PRG_QRY_DIMENSIONS (DIMENSION_ID,DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID) values(qry_dimensions_pk.nextval,"+name+",'"+desc+"','Y','')";
//PbDb b=new PbDb();
//
//st.executeUpdate(query);
//st.close();
//con.close();
}
catch(Exception e){
e.printStackTrace();
    }
%>


