<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,utils.db.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html>
    <head>
       
    </head>

    <body onunload="history.go(-1)">
        <%
                    String collen[] = request.getParameterValues("chk2");
        //String collen[]=request.getParameterValues("chk3");
                    int length1 = 0;
                    if (collen != null) {
                        length1 = collen.length;

                    }

        %>
        <%

                    try {

        //String type=session.getAttribute("tabType").toString();

                        String colNames[] = request.getParameterValues("chk2");
                        if (colNames != null) {
                            String columnIds = "";
                            String tabId = request.getParameter("tabledeleteId");

                            for (int i = 0; i < colNames.length; i++) {
                                String vals[] = colNames[i].split(",");
                                columnIds += "," + vals[0];
                                //tabId=vals[1];
                            }
                            columnIds = columnIds.substring(1);
                            Connection con = null;
                            Statement st, st1 = null;
                            ResultSet rs = null;
                            Class.forName("oracle.jdbc.driver.OracleDriver");
                            con =  utils.db.ProgenConnection.getInstance().getConnection();

                            st = con.createStatement();

        //String sql="update prg_db_master_table_details set is_active='N' where column_id  not in ("+columnIds+") and table_id="+tabId;
                            String sql = "update prg_db_master_table_details set is_active='N'  where column_id  not in (" + columnIds + ") and table_id=" + tabId;

                            st.executeUpdate(sql);
                            st1 = con.createStatement();
                            String sql1 = "update prg_db_master_table_details set is_active='Y' where column_id in (" + columnIds + ") and table_id=" + tabId;

                            st1.executeUpdate(sql1);
                            st.close();
                            st1.close();
                            con.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        //response.sendRedirect("MyTable.jsp");
                    response.sendRedirect(request.getContextPath() + "/getAllTables.do");

        %>
    </body>
     <script>
            function deletemsg(strlen){
                // alert('length in onload=='+strlen);
                if(strlen==0){

                }else{
                    alert('Unselected columns are deleted');
                }
            }

        </script>
  
</html>

