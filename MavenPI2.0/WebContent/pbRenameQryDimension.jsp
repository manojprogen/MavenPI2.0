<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,com.progen.dimensions.DimensionEditDAO"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        
        
          <style>
              *{
                margin:         0px;
                padding:        0px;
                font:           12px verdana;
            }
              h3{
                  font:         14px verdana;
                  font-weight:  bold;
                  color:        #369;
              }
          </style>
    </head>
    <body>
        <%
           String dimensionId=request.getParameter("dimensionId");
           String connectionId = request.getParameter("connectionId");
           ////////////////////////////////////////////////////////////.println.println(" dimensionId "+dimensionId);
           DimensionEditDAO dimDao = new DimensionEditDAO();
           String dimName="";
           PbReturnObject dimObj=dimDao.getDimensionName(dimensionId);
           dimName = dimObj.getFieldValueString(0,"DIMENSION_NAME");
           String dimdesc = dimObj.getFieldValueString(0,"DIMENSION_DESC");
           ////////////////////////////////////////////////////////////.println.println(" dimName "+dimName);
        %>
        <center>
            <Form name="myForm">
                <br><br><br>
                <h3>Rename Dimension Name</h3>

                <br><br><br><br>
                <Table CELLSPACING="5" CELLPADDING="5">
                      <Tr><Input type="HIDDEN" name="dimensionId" id="dimensionId" value="<%=dimensionId%>">
                          <Input type="HIDDEN" name="connectionId" id="connectionId" value="<%=connectionId%>">
                          <Td>
                              New Dimension Name
                          </Td>
                          <Td>
                              <Input type="text" name="newDimName" id="newDimName" value="<%=dimName%>">
                          </Td>
                      </Tr>
                      <Tr>
                          <Td>
                              Dimension Description
                          </Td>
                          <Td>
                              <Input type="text" name="dimdesc" id="dimdesc" value="<%=dimdesc%>">
                          </Td>
                      </Tr>
                </Table><Br/><Br/>
                <Table>
                       <Tr>
                            <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Save" ONCLICK="saveRenameDim()"></Td>
                            <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Cancel" ONCLICK="cancelRenameDim()"></Td>
                       </Tr>
                </Table>
            </Form>
        </center>
                           <script type="text/javascript">
             //added by susheela start
           function showMessage()
           {
              // alert('in show');
             var dimensionId=document.getElementById("dimensionId").value;
             var connectionId = document.getElementById("connectionId").value;
             var newDimName = document.getElementById("newDimName").value;
             var dimdesc = document.getElementById("dimdesc").value;
              var t=confirm("Are you sure you want to rename.");
              if(t==true)
                  {
                     // alert('in true')
                       $.ajax({
                            url: 'dimensioncheck.do?dimensionParam=renameDimension&dimensionId='+dimensionId+'&connectionId='+connectionId+'&newDimName='+newDimName+'&dimdesc='+dimdesc,
                            success: function(data) {
                                         // alert(data)
                                          if(data==1)
                                           {
                                              alert("Dimesion is renamed Successfully.");
                                               var frameObj=parent.document.getElementById("renameDimensionFrame");
                                               // var divObj=parent.document.getElementById("renameDimenion");
                                                frameObj.style.display='none';
                                               // divObj.style.display='none';
                                                parent.document.getElementById('fade').style.display='none';
                                                parent.refreshDim();
                                           }

                        }
                     });
                  }
              
           }
         function saveRenameDim()
         {
             var dimensionId=document.getElementById("dimensionId").value;
             var connectionId = document.getElementById("connectionId").value;
             var newDimName = document.getElementById("newDimName").value;
            // alert('dimensionId '+dimensionId+' connectionId '+connectionId);
                $.ajax({
                            url: 'dimensioncheck.do?dimensionParam=checkDimensionForRename&dimensionId='+dimensionId+'&connectionId='+connectionId+'&newDimName='+newDimName,
                            success: function(data) {
                                          //alert(data)
                                          if(data==1)
                                              {
                                                  showMessage();
                                              }
                                          if(data==2)
                                           {
                                              alert("Dimesion is being used in business group.");
                                              showMessage();
                                           }
                                           else if(data==3)
                                               {
                                                alert("Dimesion name already exists.");
                                               }

                        }
                     });
         }
         function cancelRenameDim()
         {
                var frameObj=parent.document.getElementById("renameDimensionFrame");
               // var divObj=parent.document.getElementById("renameDimenion");
               // frameObj.style.display='none';
               // divObj.style.display='none';
                parent.document.getElementById('fade').style.display='none';
                frameObj.style.display='none';
               // parent.refreshDim();
         }
           //added by susheela over
          </script>
    </body>
</html>
