

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="prg.db.PbReturnObject,java.util.*,java.sql.*,prg.business.group.BusinessGroupDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />

        <link href="myStyles.css" rel="stylesheet" type="text/css">
            <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
      

    </head>

    <body>
        <%

       String connId=String.valueOf(session.getAttribute("connId"));
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in tmie list-->"+connId);

        String minlevel = request.getParameter("minlevel");
            String CalenderId = request.getParameter("CalenderId");
   
        Connection con1 = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        //Class.forName("oracle.jdbc.driver.OracleDriver");
        con=new BusinessGroupDAO().getConnectionIdConnection(connId);
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("conn Object--in create--"+con);
       
        st = con.createStatement();
        if (minlevel.equals("1")) {
                sql = "SELECT rowid,PYEAR, START_DATE, END_DATE, CUST_YEAR FROM PR_YEAR_" + CalenderId;
        } else if (minlevel.equals("2")) {
                sql = "SELECT rowid,QUARTER_NO, DYEAR, START_DATE, END_DATE, CUST_NAME FROM PR_QUARTER_" + CalenderId;
        } else if (minlevel.equals("3")) {
                sql = "SELECT rowid,MONTH_NO, DYEAR, START_DATE, END_DATE, MONTH_NAME, CUST_NAME FROM PR_MONTH_" + CalenderId;
        } else if (minlevel.equals("4")) {
                sql = "SELECT rowid,WEEK_NO, DYEAR, START_DATE, END_DATE, CUST_NAME FROM PR_WEEK_" + CalenderId;
        } else if (minlevel.equals("5")) {
                sql = "SELECT rowid,DDATE, START_DATE, END_DATE, DAYOFYEAR, DAYOFMONTH FROM PR_DAYS_" + CalenderId;
        }
           //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sql--->"+sql);
        rs = st.executeQuery(sql);
     
      
         String rowids="";
         int j=0;
        
        while(rs.next()){
            rowids+="~------~"+rs.getString(1);
        
            j++;
            }

         rs1 = st.executeQuery(sql);
        PbReturnObject pbro = new PbReturnObject(rs1);
       if(pbro.getRowCount()>0){

           rowids=rowids.substring(8);  }
        pbro.writeString();
       
        String valCheck="";
              if(minlevel.equals("1")){
              for (int i = 0; i < pbro.getRowCount(); i++) {
              valCheck+="~"+pbro.getFieldValueString(i,"CUST_YEAR");
              }
               valCheck=valCheck.substring(1);
              }else if(minlevel.equals("2")){
              for (int i = 0; i < pbro.getRowCount(); i++) {
              valCheck+="~"+pbro.getFieldValueString(i,"CUST_NAME");
              }
               valCheck=valCheck.substring(1);
             }
               else if(minlevel.equals("3")){
              for (int i = 0; i < pbro.getRowCount(); i++) {
              valCheck+="~"+pbro.getFieldValueString(i,"CUST_NAME");
              }
               valCheck=valCheck.substring(1);
             }
               else if(minlevel.equals("4")){
              for (int i = 0; i < pbro.getRowCount(); i++) {
              valCheck+="~"+pbro.getFieldValueString(i,"CUST_NAME");
              }
              valCheck=valCheck.substring(1);
             }

        %>
        <center>
            <form name="myForm2" method="post">
                <table align="center" >


                    <tr><td>&nbsp;</td></tr>


                    <%if (minlevel.equals("1")) {%>
                    <tr>
                        <td style="background-color:#b4d9ee" align="center"><b>PYEAR</b></td>

                        <td style="background-color:#b4d9ee" align="center"><b>START DATE</b></td>

                        <td style="background-color:#b4d9ee" align="center"><b>END DATE</b></td>

                        <td style="background-color:#b4d9ee" align="center"><b>CUST YEAR</b></td>

                    </tr>
                    <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                    <tr>
                       <%--  <input type="hidden" name="Yrowid<%=i%>" id="Yrowid<%=i%>" value="<%=pbro.getFieldValueString(i, 0)%>">--%>
                        <td  style="background-color: #b4d9ee"><input type="text"  style="width:90px;background-color:white" name="YPYEAR<%=i%>" id="YPYEAR<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 1)%>"></td>
                        <td  style="background-color: #b4d9ee"><input type="text"  style="width:110px;background-color:white"  name="YSTART_DATE<%=i%>" id="YSTART_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 2)%>"></td>
                        <td  style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white"  name="YEND_DATE<%=i%>" id="YEND_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 3)%>"></td>
                        <td  style="background-color: #b4d9ee"><input type="text" style="width:150px;background-color:white"  name="YCUST_YEAR<%=i%>" id="YCUST_YEAR<%=i%>"  value="<%=pbro.getFieldValueString(i, 4)%>"></td>
                    </tr>
                    <%}

                     } else if (minlevel.equals("2")) {%>
                    <tr>
                        <td style="background-color: #b4d9ee" align="center"><b>QUARTER NO</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>PR QUARTER </b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>START DATE </b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>END DATE </b></td>
                        <td style="background-color: #b4d9ee" align="center"><b>CUST NAME </b></td>
                    </tr>
                    <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                    <tr>
                      <%--  <input type="hidden" name="Qrowid<%=i%>" id="Qrowid<%=i%>" value="<%=pbro.getFieldValueString(i, 0)%>">--%>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="QQUARTER_NO<%=i%>" id="QQUARTER_NO<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 1)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="QPR_QUARTER<%=i%>" id="QPR_QUARTER<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 2)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="QSTART_DATE<%=i%>" id="QSTART_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 3)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text"  style="width:110px;background-color:white" name="QEND_DATE<%=i%>"  id="QEND_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 4)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="QCUST_NAME<%=i%>"  id="QCUST_NAME<%=i%>" value="<%=pbro.getFieldValueString(i, 5)%>" ></td>
                    </tr>
                    <%}
                     } else if (minlevel.equals("3")) {%>
                    <tr>
                        <td style="background-color: #b4d9ee" align="center"><b>MONTH NO</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>DYEAR </b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>START DATE</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>END DATE</b></td>
                        <td style="background-color: #b4d9ee" align="center"><b>MONTH NAME</b></td>
                        <td style="background-color: #b4d9ee" align="center"><b>CUST NAME</b></td>
                    </tr>

                    <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                    <tr><%--<input type="hidden" name="Mrowid<%=i%>" id="Mrowid<%=i%>" value="<%=pbro.getFieldValueString(i, 0)%>">--%>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:100px;background-color:white" name="MMONTH_NO<%=i%>" id="MMONTH_NO<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 1)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:100px;background-color:white" name="MDYEAR<%=i%>" id="MDYEAR<%=i%>" readOnly  value="<%=pbro.getFieldValueInt(i, 2)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:100px;background-color:white" name="MSTART_DATE<%=i%>" id="MSTART_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 3)%>">  </td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:100px;background-color:white" name="MEND_DATE<%=i%>"  id="MEND_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 4)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="MMONTH_NAME<%=i%>"  id="MMONTH_NAME<%=i%>" readOnly value="<%=pbro.getFieldValueString(i, 5)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:100px;background-color:white" name="MCUST_NAME<%=i%>"  id="MCUST_NAME<%=i%>" value="<%=pbro.getFieldValueString(i, 6)%>"></td>
                    </tr>

                    <%
                       }
                   } else if (minlevel.equals("4")) {%>

                    <tr>
                        <td style="background-color: #b4d9ee" align="center"><b>WEEK_NO</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>DYEAR</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>START DATE</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>END DATE</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>CUST NAME</b></td>
                    </tr>
                    <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                    <tr>
                         <%--<input type="hidden" name="Wrowid<%=i%>" id="Wrowid<%=i%>" value="<%=pbro.getFieldValueString(i, 0)%>">--%>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="WWEEK_NO<%=i%>" id="WWEEK_NO<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 1)%>"> </td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="WDYEAR<%=i%>" id="WDYEAR<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 2)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="WSTART_DATE<%=i%>" id="WSTART_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 3)%>"> </td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="WEND_DATE<%=i%>"  id="WEND_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueDate(i, 4)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="WCUST_NAME<%=i%>"  id="WCUST_NAME<%=i%>" value="<%=pbro.getFieldValueString(i, 5)%>"></td>
                    </tr>

                    <%
                      }
                   } else if (minlevel.equals("5")) {%>

                    <tr>
                        <td style="background-color: #b4d9ee" align="center"><b>DDATE</b></td>


                        <td style="background-color: #b4d9ee" align="center"><b>START DATE</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>END DATE</b></td>

                        <td style="background-color: #b4d9ee" align="center"><b>DAY OF YEAR</b></td>
                        <td style="background-color: #b4d9ee" align="center"><b>DAY OF MONTH</b></td>
                    </tr>
                    <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                    <tr><%-- <input type="hidden" name="Drowid<%=i%>" id="Drowid<%=i%>" value="<%=pbro.getFieldValueString(i, 0)%>">--%>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="DDDATE<%=i%>" id="DDDATE<%=i%>" readOnly value="<%=pbro.getFieldValueString(i, 1)%>"> </td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="DSTART_DATE<%=i%>" id="DSTART_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueString(i, 2)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="DEND_DATE<%=i%>" id="DEND_DATE<%=i%>" readOnly value="<%=pbro.getFieldValueString(i, 3)%>"> </td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:110px;background-color:white" name="DDAYOFYEAR<%=i%>"  id="DDAYOFYEAR<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 4)%>"></td>
                        <td style="background-color: #b4d9ee"><input type="text" style="width:120px;background-color:white" name="DDAYOFMONTH<%=i%>"  id="DDAYOFMONTH<%=i%>" readOnly value="<%=pbro.getFieldValueInt(i, 5)%>"></td>
                    </tr>

                    <%}
                    }%>
                </table>

                <table>
                    <tr>
                        <td>
                            <%if (!minlevel.equals("5")) {%>
                            <input type="button" class="navtitle-hover" style="width:auto" name="Save" value="Save" onclick="savetimesetUp()">
                            <%}%>
                            <input type="button" class="navtitle-hover" style="width:auto" name="Cancel" value="Cancel" onclick="canceltimesetUpCustom()">
                        </td>
                    </tr>

                </table>
                 <input type="hidden" name="connId" id="connId" value="<%=connId%>" >
                 <input type="hidden" name="rowids" id="rowids" value="<%=rowids%>">
                <input type="hidden" name="minlevel" id="minlevel" value="<%=minlevel%>">
                <input type="hidden" name="valCheck" id="valCheck" value="<%=valCheck%>">
                <input type="hidden" name="rowCount" id="rowCount" value="<%=pbro.getRowCount()%>">
              
            </form>
                  <script>
            function savetimesetUp(){
                document.myForm2.action="saveStandardCalenderCustom.do";
                document.myForm2.submit();
                 parent.canceltimesetUpCustom();
            }
            function canceltimesetUpCustom(){
                parent.canceltimesetUpCustom();
            }

        </script>
        </center>
        <%
        rs.close();
        rs1.close();
        st.close();
        con.close();
        %>
    </body>
    
</html>
