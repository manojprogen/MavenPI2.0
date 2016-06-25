
<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.*,java.sql.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html><head>
        <script>
   
        </script>
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
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("hi in start in delete");
        //String type=session.getAttribute("tabType").toString();

                        String colNames[] = request.getParameterValues("chk2");
                        if (colNames != null) {
                            String columnIds = "";
                            String tabId = request.getParameter("dimtableId");

                            String tabids[] = tabId.split(",");
                            tabId = tabids[0];


                            String dimtableId = "";
                            for (int i = 0; i < colNames.length; i++) {
                                String vals[] = colNames[i].split(",");

                                dimtableId = vals[1];

                                if (tabId.equals(dimtableId)) {

                                    columnIds += "," + vals[0];
                                }


                            }
                            columnIds = columnIds.substring(1);
                            Connection con = null;
                            Statement st, st1 = null;
                            ResultSet rs = null;
                            Class.forName("oracle.jdbc.driver.OracleDriver");
                            con =  utils.db.ProgenConnection.getInstance().getConnection();

                            st = con.createStatement();

        //String sql="update prg_db_master_table_details set is_active='N' where column_id  not in ("+columnIds+") and table_id="+tabId;
                            String sql = "update prg_qry_dim_tab_details  set is_available='N' where col_id  not in (" + columnIds + ") and dim_tab_id=" + tabId;

                            st.executeUpdate(sql);
                            st1 = con.createStatement();
                            String sql1 = "update prg_qry_dim_tab_details  set is_available='Y' where col_id  in (" + columnIds + ") and dim_tab_id=" + tabId;

                            st.executeUpdate(sql1);
                            st.close();
                            con.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    response.sendRedirect(request.getContextPath() + "/getAllDimensions.do");

        %>
    </body>
</html>


