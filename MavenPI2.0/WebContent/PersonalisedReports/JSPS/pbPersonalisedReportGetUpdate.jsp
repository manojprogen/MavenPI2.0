<%@ page import="prg.personalisedreps.client.PbPersonalisedrepManager"%>
 <%@page import="prg.personalisedreps.params.PbPersonalisedrepParams" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="utils.db.JDBCConnectionPool"%>
<%@page import="utils.db.ProgenConnection"%>
<%@page import="java.sql.Connection"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.db.Session"%> 
<%@page import="utils.db.*"%>

<%-- Get data from the user create a Manager object and send the params object to get the user details--%>
<Html>
<Head>
    <%/***** Report Comments 
             Report Name has been Assigned to report to display it in report area
             **/
            DisplayFrame disp = new DisplayFrame("","Edit Personalised Report");
            
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
                 
          function checkSeqnum(jspName)
          {  
            //alert('hi');
              var seqnum=document.myForm.repseqno.value;
               //if(seqnum>10)
               //{
                  // alert('Please donot  enter Sequence Number more than 10');
              // }
              // else if(seqnum<=0)
               //{
                   // alert('sequence number starts from 1');
              // }
              // else
              // {
                  // var x=confirm('Are you sure you want to Submit');
                  // if(x==true)
                  // {
                       document.myForm.action=jspName;
                       document.myForm.submit();
                  // }
          
              // }
            }   
          function checkSave(jspName)
          {
          // var x=confirm('Are you sure you want to Submit');
           //if(x==true)
                 //  {
                       document.myForm.action=jspName;
                       document.myForm.submit();
                 //  }
          
          }
     </Script>
    
</Head>
<Body onload="document.myForm.repcustname.focus();">
     
           
<Center>
<Center>
    <Form  name="myForm" id="updateform" method="get">
        <center>
<font size=2px> Fields marked <span style="color:red">*</span> are MANDATORY</font>
</center>
   
    <Br>
    <%
    
    Session prgSession = new Session();
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("VALUES ARE "+request.getParameter("chk2"));    
    PbPersonalisedrepParams params = new PbPersonalisedrepParams();
    PbPersonalisedrepManager Client = new PbPersonalisedrepManager();
    params.setPersonalisedId(request.getParameter("chk2"));
    prgSession.setObject(params);
    try
    {
    PbReturnObject retObj = Client.getPersonalisedReportDetails(prgSession);
    
    %>
    <Table>
        <Input type="hidden" name="personalisedid" value="<%=params.getPersonalisedId()%>">
         <Tr>
             <Td class="myhead"><span style="color:red">*</Span>Report Cust Name</Td>
            <Td><Input type="text"  class="myTextbox5" name="repcustname" maxlength=50 value="<%=retObj.getFieldValueString(0,"PRG_REPORT_CUST_NAME") %>"></Td>
        </Tr>
        <Tr>
            <Td class="myhead">Sequence Number</Td>
            <Td><Input type="text"  class="myTextbox5" name="repseqno"   value="<%=retObj.getFieldValueInt(0,"PRG_SEQUENCE_NO") %>" ></Td>
        </Tr>
  
   </Table>
    <Table>
        
         <Tr>
             <Td><input class="btn" type="button" value="Back"  onclick="checkCancel('pbPersonalisedReportsList.jsp')";></Td>
             <Td><Input class="btn" type="button" value="Save"  onclick="checkSeqnum('pbPersonalisedReportUpdate.jsp')";></Td>
         </Tr>
    </Table>  
   
    <%     }
    catch(Exception ex)
    {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
    }
    %>

   
 </Form>
</Center>

</Body>
</Html>
         




