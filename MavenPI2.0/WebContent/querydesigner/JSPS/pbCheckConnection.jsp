
<%@page import="prg.db.PbDb"%>
<%@page import="java.sql.*" %>
<%@page import="utils.db.ProgenConnection" %>
<%@page import="java.io.File" %>
<%@page import="java.io.FileOutputStream" %>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>


        <%String dbcode = request.getParameter("dbcode");
 
        %>





        <%

                    /////Start Of Code For Oracle Database/////////
                    if (dbcode.equals("1")) {
                        int i = 0;
                        Connection con = null;
                        String connection = null, username = null, password = null, sid = null, server = null, port = null;
                        try {

                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dbcode is-->"+dbcode);
                            connection = request.getParameter("connectionname");
                            username = request.getParameter("username");
                            password = request.getParameter("password");
                            sid = request.getParameter("Serviceid");
                            server = request.getParameter("server");
                            port = request.getParameter("Port");
                            Class.forName("oracle.jdbc.driver.OracleDriver");
                            //
                            con = DriverManager.getConnection("jdbc:oracle:thin:@" + server + ":" + port + ":" + sid + "", username, password);
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("DDDDDD");
                            if (con == null) {
                            } else {
                                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Connection Successful");
                            }


                        } catch (Exception e) {
                            i = 1;
                            e.printStackTrace();
                        }
        %>


        <%

                                if (con == null) {
                                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if "+con);
%>

        <script>
            // document.myForm.action = "pbGetConnection.jsp";
            //document.myForm.submit();

            history.go(-1);
            alert("Enter Correct Credentials");
        </script>

        <%            } else {
                                    try {

                                        // out.println("in else");
                                        Connection con1 = utils.db.ProgenConnection.getInstance().getConnection();
                                        Statement st = con1.createStatement();
                                        String query = "";
                                         if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                         query = "insert into prg_user_connections (CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY,DB_CONNECTION_TYPE,MAX_ACTIVE,MAX_WAIT) values('" + connection + "','" + username + "','" + password + "','" + server + "','" + sid + "','','" + port + "','','','ORACLE',10,7000)";
                                       }
                                        else
                                            {
                                                query = "insert into prg_user_connections (CONNECTION_ID,CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY,DB_CONNECTION_TYPE,MAX_ACTIVE,MAX_WAIT) values(PRG_USER_CONNECTIONS_SEQ.nextval,'" + connection + "','" + username + "','" + password + "','" + server + "','" + sid + "','','" + port + "','','','ORACLE',10,7000)";
                                            }
        
                                        st.executeUpdate(query);
                                        st.close();
                                        con1.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }


                                response.sendRedirect(request.getContextPath() + "/getAllTables.do");

                            } ////////End Of Code For Oracle Database//////////////
                            //Start Of Code For Excel//////////
                            else if (dbcode.equals("2")) {
                                Connection con = null;
                                String connection = null, dsn = null;
                                try {

                                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dbcode is-->"+dbcode);
                                    connection = request.getParameter("connectionname");
                                    dsn = request.getParameter("exceldsn");
                                    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                                    con = DriverManager.getConnection("jdbc:odbc:" + dsn + "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (con == null) {
        %>

        <form name="myForm" method="post">
            <script>

                history.go(-1);
                alert("Enter Correct Credentialss");
            </script>
        </form>

        <%            } else {
                        try {
                            String exlPath = request.getParameter("path1");
                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("path---------->" + exlPath);

                            // out.println("in else");
                            FileOutputStream fileOutputStream = null;
                            File file = new File("F:\\" + exlPath);
                            long fileSize = file.length();
                            if (file.exists()) {
                                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("File Already exists");
                            } else {
                                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("File does not  exists");
                                // fileOutputStream = new FileOutputStream(file);
                                // fileOutputStream.write(file, 0, file.length());
                                //  fileOutputStream.close();
                            }

                            Connection con1 = ProgenConnection.getInstance().getConnection();
                            Statement st = con1.createStatement();
                            String query = "";
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                         query = "insert into prg_user_connections (CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY,DSN_NAME,DB_CONNECTION_TYPE,EXL_FILE_PATH,MAX_ACTIVE,MAX_WAIT) values('" + connection + "','','','','','','','','','" + dsn + "','EXCEL','" + exlPath + "',10,7000)";
                           }
                                        else
                                            {
                             query = "insert into prg_user_connections (CONNECTION_ID,CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY,DSN_NAME,DB_CONNECTION_TYPE,EXL_FILE_PATH,MAX_ACTIVE,MAX_WAIT) values(PRG_USER_CONNECTIONS_SEQ.nextval,'" + connection + "','','','','','','','','','" + dsn + "','EXCEL','" + exlPath + "',10,7000)";

}                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Query is "+query);
                            // st.executeUpdate(query);
                            st.close();
                            con1.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    response.sendRedirect(request.getContextPath() + "/getAllTables.do");

                }
  else if (dbcode.equals("3")) {
                
                Connection con = null;
                String connectionName = null;
                try {
                    connectionName = request.getParameter("connectionname");
                    String username = request.getParameter("pstgreusername");
                    String password = request.getParameter("pstgrepassword");
                    String server = request.getParameter("postgresserver");
                    String port = request.getParameter("pstgrePort");
                    String databasename = request.getParameter("pstgredbname");
                    con = ProgenConnection.getInstance().getConnection();
                    Statement st = con.createStatement();
                    String query = "";
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                         query =  "insert into prg_user_connections (CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,PORT,DB_CONNECTION_TYPE,DBNAME,MAX_ACTIVE,MAX_WAIT) " +
                            "values('" + connectionName + "','" + username + "','" + password + "','" + server + "','" + port + "','PostgreSQL','" + databasename + "',10,7000)";
                   }
                                        else
                                            {
                     query = "insert into prg_user_connections (CONNECTION_ID,CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,PORT,DB_CONNECTION_TYPE,DBNAME,MAX_ACTIVE,MAX_WAIT) " +
                            "values(PRG_USER_CONNECTIONS_SEQ.nextval,'" + connectionName + "','" + username + "','" + password + "','" + server + "','" + port + "','PostgreSQL','" + databasename + "',10,7000)";

}                     
                    st.execute(query);
                    st.close();
                    con.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    
        }
                response.sendRedirect(request.getContextPath() + "/getAllTables.do");
            }        ////End Of Code For Excel/////
                else if (dbcode.equals("4")) {
                    String dbConnectionType=request.getParameter("dbname");
                    String sqldbname = request.getParameter("sqlserverdbname");
                    String sqlun = request.getParameter("sqlserverusername");
                    String sqlpswd = request.getParameter("sqlserverpswrd");
                    String sqlport = request.getParameter("sqlserverportnum");
                    String sqlhstname = request.getParameter("sqlserverhostname");
                    String connectionname = request.getParameter("connectionname");

                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    Connection conn=null;
                 try{
                     conn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + sqlhstname + ":" + sqlport + "/" + sqldbname, sqlun, sqlpswd);

                    if (conn == null) {
                    } else {

                    }


                } catch (Exception e) {
                e.printStackTrace();
            }
                   if(conn == null) {
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if "+con);
        %>

        <script>
            history.go(-1);
            alert("Enter Correct Credentials");
        </script>

        <%            }   else {
                        try {

                            // out.println("in else");
                            Connection con1 = utils.db.ProgenConnection.getInstance().getConnection();
                            Statement st = con1.createStatement();
                            String query = "insert into prg_user_connections (CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY,DB_CONNECTION_TYPE,DBNAME,MAX_ACTIVE,MAX_WAIT) " +
                                    "values('"+ connectionname + "','" + sqlun + "','" + sqlpswd + "','" + sqlhstname + "','','','" + sqlport + "','','','"+dbConnectionType+"','"+sqldbname+"',10,7000)";
                    //.println("Query is "+query);
                    st.executeUpdate(query);
                    st.close();
                    con1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            response.sendRedirect (request.getContextPath() + "/getAllTables.do");

                }
  else if (dbcode.equals("5")) {
                
                Connection con = null;
                String connectionName = null;
                try {
                    connectionName = request.getParameter("connectionname");
                    String username = request.getParameter("mysqlusername");
                    String password = request.getParameter("mysqlpassword");
                    String server = request.getParameter("mysqlserver");
                    String port = request.getParameter("mysqlport");
                    String databasename = request.getParameter("mysqldbname");
                    String mySqlquery = "";
                     if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                         mySqlquery = "insert into prg_user_connections (CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,PORT,DB_CONNECTION_TYPE,MAX_ACTIVE,MAX_WAIT,DBNAME) "
                      + "values('" + connectionName + "','" + username + "','" + password + "','" + server + "','" + port + "','Mysql',10,7000,'"+databasename+"')";
               }
                                        else
                                            {
                     mySqlquery = "insert into prg_user_connections (CONNECTION_ID,CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,PORT,DB_CONNECTION_TYPE,MAX_ACTIVE,MAX_WAIT,DBNAME) "
                      + "values(PRG_USER_CONNECTIONS_SEQ.nextval,'" + connectionName + "','" + username + "','" + password + "','" + server + "','" + port + "','Mysql',10,7000,'"+databasename+"')";
                 }
                  PbDb pbDb =new PbDb();
                  pbDb.execModifySQL(mySqlquery);
                } catch (Exception e) {
                    e.printStackTrace();
                    
        }
                response.sendRedirect(request.getContextPath() + "/getAllTables.do");
            }


        %>

    </body>
</html>
