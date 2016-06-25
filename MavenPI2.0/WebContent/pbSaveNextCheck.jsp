

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,utils.db.*"%>
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
if(dbcode.equals("1"))

{
int i=0;
Connection con = null;
String connection=null,username=null,password=null,sid=null,server=null,port=null;
try
        {

   

    connection = request.getParameter("connectionname");
    username = request.getParameter("username");
    password = request.getParameter("password");
    sid = request.getParameter("Serviceid");
    server = request.getParameter("server");
    port = request.getParameter("Port");
    Class.forName("oracle.jdbc.driver.OracleDriver");
    //
    con = DriverManager.getConnection("jdbc:oracle:thin:@"+server+":"+port+":"+sid,username,password);
    
   }
    catch(Exception e)
    {
        i=1;
        e.printStackTrace();
    }
%>


    <%

    if(con==null)
    {
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if "+con);
%>

        <script>
           // document.myForm.action = "pbGetConnection.jsp";
            //document.myForm.submit();

            history.go(-1);
            alert("Enter Correct Credentials");
            window.close();
        </script>

<%
    }
else
    {
                try
                    {

           // out.println("in else");
            Connection con1 =  utils.db.ProgenConnection.getInstance().getConnection();
            Statement st = con1.createStatement();
            String query = "insert into prg_user_connections (CONNECTION_ID,CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY) values(PRG_USER_CONNECTIONS_SEQ.nextval,'"+connection+"','"+username+"','"+password+"','"+server+"','"+sid+"','','"+port+"','','')";
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Query is "+query);
            st.executeUpdate(query);
            st.close();
            con1.close();
            }

                catch(Exception e)
                        {
                            e.printStackTrace();
                            }
                 response.sendRedirect(request.getContextPath()+"/TableList.jsp?connection="+connection);

    }


       

}




////////End Of Code For Oracle Database//////////////

//Start Of Code For Excel//////////
else if(dbcode.equals("2"))
{
        Connection con = null;
        String connection=null,dsn=null;
        try
        {

             ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dbcode is-->"+dbcode);
             connection = request.getParameter("connectionname");
             dsn = request.getParameter("exceldsn");
             Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            con = DriverManager.getConnection("jdbc:odbc:"+dsn+"");
        }

        catch(Exception e)
                {
                    e.printStackTrace();
                }

                if(con==null)
                {
%>

 <form name="myForm" method="post">
        <script>
           // document.myForm.action = "pbGetConnection.jsp";
            //document.myForm.submit();
            history.go(-1);
            alert("Enter Correct Credentialss");
        </script>
    </form>

<%
}

else
    {
                try
                    {

           // out.println("in else");
            Connection con1 = utils.db.ProgenConnection.getInstance().getConnection();
            Statement st = con1.createStatement();
            String query = "insert into prg_user_connections (CONNECTION_ID,CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY,DSN_NAME) values(PRG_USER_CONNECTIONS_SEQ.nextval,'"+connection+"','','','','','','','','','"+dsn+"')";
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Query is "+query);
            st.executeUpdate(query);
            st.close();
            con1.close();
            }

                catch(Exception e)
                        {
                            e.printStackTrace();
                            }

    }

}
////End Of Code For Excel/////










%>

    </body>
</html>

