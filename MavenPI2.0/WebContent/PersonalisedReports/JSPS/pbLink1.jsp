<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="utils.db.*"%>

<Html>
    <Head>
        <%/***** Report Comments
             Report Name has been Assigned to report to display it in report area
             **/
            DisplayFrame disp = new DisplayFrame("","Create Personalised Report ");

            /***** Report Comments
             Tabs are added as per current design
             **/


            /***** Report Comments
             Advertisment and Report quick fact have been set. They stored in database and parameter will drive the contents
             **/

           disp.setAdv();
           String cPath=request.getContextPath();
           disp.setCtxPath(cPath);
           disp.setQuick("Value Analysis");
           out.println(disp.DisplayPreHead());
           %>
         <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
          <Script language="javascript"  src="../JS/myPersonalisedReport.js"></Script>
          <Script>
                var my_window = null;
                function check() {
                if(my_window && !my_window.closed)
                my_window.focus();
                }

                function getValues(){
                // alert('lll -- ');
                // my_window= window.open("getMemberValues.jsp?paraId="+pp,"Select Member Values","status=1,width=750,height=450,resizable=no,modal");
                // my_window= window.open("getMemberValues2.jsp?paraId="+pp,"Select Member Values","status=1,width=750,height=450,resizable=no,modal");

                my_window= window.open("pbPersonalisedReportsList.jsp");

                }


         </SCRIPT>

           <%
          out.println(disp.DisplayPostHead());%>
   </Head>

      <Body>
          <%

           out.println(disp.DisplayPreBody());
           out.println(disp.DisplayPreRow());
           %>
          <Center>
            <Form name="myForm" >
                <center>
                    <a onclick="getValues();">LINK</a>
              <%
              String userid="2";
              session.setAttribute("userid",userid);
              %>
             </Form>
          </center>
          <%  out.println(disp.DisplayPostRow());
    out.println(disp.DisplayPostBody()); %>
    </Body>
</Html>