<%@page import="prg.db.PbReturnObject,prg.business.group.PbBusinessGroupEditDAO"%>
<% String contextPath=request.getContextPath();%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
             <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/BusinessGroup.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        
        <title>JSP Page</title>
                 
    </head>
    <body>
        <%
        String groupName = request.getParameter("groupName");
        String connId = request.getParameter("connId");
        String grpDesc = request.getParameter("grpDesc");
        String sourceGrpId = request.getParameter("grpId");
        ////////////////////////////////////////////////////////////////////////.println.println(" connId "+connId);
        ////////////////////////////////////////////////////////////////////////.println.println(grpDesc+" grp "+groupName);
        PbBusinessGroupEditDAO bgDao=new PbBusinessGroupEditDAO();
        PbReturnObject all=bgDao.getGroupDetailsWithBucket(sourceGrpId);
        PbReturnObject factObj=(PbReturnObject)all.getObject("factObj");
        PbReturnObject dimObj=(PbReturnObject)all.getObject("dimObj");
        String dbDimIds = "";
        String grpDimIds = "";
        for(int m=0;m<dimObj.getRowCount();m++)
            {
            dbDimIds=dbDimIds+","+dimObj.getFieldValueString(m,"QRY_DIM_ID");
            grpDimIds=grpDimIds+","+dimObj.getFieldValueString(m,"DIM_ID");
            }
        if(dbDimIds.length()>1)
            dbDimIds=dbDimIds.substring(1);
        if(grpDimIds.length()>1)
        grpDimIds = grpDimIds.substring(1);

        %>
    </body>
    <Center>
    <Form>
               <INPUT TYPE="hidden" name="grpDimIds" id="grpDimIds" value="<%=grpDimIds%>">
               <INPUT TYPE="hidden" name="dbDimIds" id="dbDimIds" value="<%=dbDimIds%>">
               <INPUT TYPE="hidden" name="groupName" id="groupName" value="<%=groupName%>">
               <INPUT TYPE="hidden" name="grpDesc" id="grpDesc" value="<%=grpDesc%>">
               <INPUT TYPE="hidden" name="sourceGrpId" id="sourceGrpId" value="<%=sourceGrpId%>">
               <INPUT TYPE="hidden" name="connId" id="connId" value="<%=connId%>">

                 <font style="color:black;font-weight:bold;font-size:12px">Select Dimensions To Be Copied</font>
                <Table>
                    <%for(int m=0;m<dimObj.getRowCount();m++){
                     %>
                  <Tr><Td><Input CHECKED TYPE="checkbox" name="<%=dimObj.getFieldValueString(m,"DIM_ID")%>" id="<%=dimObj.getFieldValueString(m,"DIM_ID")%>" value="<%=dimObj.getFieldValueString(m,"DIM_ID")%>"></Td>
                  <Td><%=dimObj.getFieldValueString(m,"DIM_NAME")%></Td></Tr>
                    <%}%>
                </Table>
                 <Table>
                       <Tr>
                           <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Save" ONCLICK="saveCopy()">
                           </Td>
                           <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Cancel" ONCLICK="cancelCopy()">
                           </Td>
                       </Tr>
                 </Table>
    </Form>
    </Center>
                <script type="text/javascript">
             function saveCopy()
             {
                 var dbDimIds=document.getElementById("dbDimIds").value;
                 var grpDimIds = document.getElementById("grpDimIds").value;
                 var connId = document.getElementById("connId").value;
                 var sourceGrpId = document.getElementById("sourceGrpId").value;
                 var grpDesc = document.getElementById("grpDesc").value;
                 var groupName = document.getElementById("groupName").value;

                // alert(dbDimIds+' dbDimIds grpDimIds '+grpDimIds);
                  var dbDimId = dbDimIds.split(",");
                  var grpDimId = grpDimIds.split(",");
                 var selectedDbDimId="";
                 var selectedGrpDimId="";
                    for(var m=0;m<grpDimId.length;m++)
                    {
                                var val= document.getElementById(grpDimId[m]); 
                                if(document.getElementById(val.value).checked==true)
                                selectedGrpDimId = selectedGrpDimId+","+val.value;
                    }
                //alert('selectedGrpDimId '+selectedGrpDimId+' selectedDbDimId '+selectedDbDimId);
                //alert('businessgroupeditaction.do?groupdetails=copyGroupDimForConn&groupName='+groupName+'&connId='+connId+'&grpDesc='+grpDesc+'&sourceGrpId='+sourceGrpId+'&selectedGrpDimId='+selectedGrpDimId);
                  $.ajax({
                     url: 'businessgroupeditaction.do?groupdetails=copyGroupDimForConn&groupName='+groupName+'&connId='+connId+'&grpDesc='+grpDesc+'&sourceGrpId='+sourceGrpId+'&selectedGrpDimId='+selectedGrpDimId,
                    success: function(data) {
                        if(data==1)
                           {
                              alert('Group copied successfully');
                              parent.cancelCopyRefresh();
                           }
                        }
                    });
                
             }
             function cancelCopy()
             {
                 parent.cancelGrpCopyParent();
             }
         </script>
</html>
