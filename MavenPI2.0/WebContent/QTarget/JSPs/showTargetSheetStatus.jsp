<%@page import="java.io.*"%>
<%@page import="prg.target.sheetschceduler.ExcelScheduler"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript">
    function  inUploadExcel()
    {
          var path = '<%=request.getContextPath()%>';
     
          document.forms.myFormexcel.action=path+'/QTarget/JSPs/newUpload.jsp';
          document.forms.myFormexcel.submit();
    }
    </script>

    </head>
    
    <body>
       
       <%
      
        //////////////////////////////////////////.println(" in sjdhsj ");
        String targetId=(String)request.getAttribute("targetId");
        String UserId=(String)request.getAttribute("UserId");
        String myFile = (String)request.getAttribute("myFile");
        File tempFile=(File)request.getAttribute("tempFile");
        
        String uploadStatus=(String)request.getAttribute("uploadStatus");
        String status = (String)request.getAttribute("status");
        
       /* request.setAttribute("targetId",targetId);
        request.setAttribute("UserId",UserId);
        request.setAttribute("myFile",myFile);
        request.setAttribute("tempFile",tempFile); */

        session.setAttribute("targetId",targetId);
        session.setAttribute("UserId",UserId);
        session.setAttribute("myFile",myFile);
        session.setAttribute("tempFile",tempFile);

        if(status.equalsIgnoreCase(""))
            {
             out.println("The Excel Sheet Selected is not for this target");
            }

        if(status.equalsIgnoreCase("Available"))
            {
              if(uploadStatus.equalsIgnoreCase(""))
                  {
                   out.println("Your Request has been submitted. To view the data go to target UI.");
                  }
              else if(uploadStatus.equalsIgnoreCase("Uploaded"))
                  {
                   out.println("The Sheet has already been uploaded. Uploading it again will overwrite existing data.Are you sure you want to continue.");
                  }
            }
        //////////////////////////////////////////.println(" targetId "+targetId+" UserId "+UserId+" myFile " +myFile+" tempFile "+tempFile);

         if(status.equalsIgnoreCase("Available"))
         {
             if(uploadStatus.equalsIgnoreCase(""))
             {
                //////////////////////////////////////////.println(" in upload ");
                ExcelScheduler es = new ExcelScheduler(targetId,UserId,myFile.toString(),tempFile.toString());
                es.readExcel(targetId,UserId,myFile.toString(),tempFile.toString());
             }
        }
        %>
        <Center>
        <Form name="myFormexcel">
            <Br/>
            <%
             if(status.equalsIgnoreCase("Available")&&uploadStatus.equalsIgnoreCase("uploaded")){ %>
            <Table>
                <Tr>
                    <Td><INPUT TYPE="button" name="Ok" value="Ok" onclick="inUploadExcel()"> </Td>
                </Tr>
            </Table>
            <%}%>

        </Form>
        </Center>
    </body>
</html>
