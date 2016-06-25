<%@page import="prg.db.PbReturnObject"%>
<%@page import="com.progen.userlayer.db.UserLayerDAO"%>
<%@page import="java.util.*"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
              <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>-->
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/pbUserLayer.js"></script>


        <script type="text/javascript">
            function updateVals()
            {
                var memId = document.getElementById("memId").value;
               
                var orgId = document.getElementById("orgId").value;
                var totalUrl = "orgId="+orgId+"~memId="+memId+"~";
               // alert('Are you sure you want to remove the access.');
                var t=confirm('Are you sure you want to remove the access.');
                if(t==true){
                $.ajax({
                    url: 'organisationDetails.do?param=deleteDimMemberValuesForAcc&totalUrl='+totalUrl,
                    success: function(data) {
                       // alert('data '+data);
                        if(data=="1")
                            alert("Values Not Saved.")
                        else{
                                 alert('Values Set To Default.');
                                 cancelSelectValsParent();
                            }

                    }
                    });
                }
            }
             function cancelSelectValsParent()
             {
                 parent.cancelSelectValsAccParent();
             }
             function cancelSelectVals()
             {
                 parent.cancelSelectValsAccParent();
             }
             function saveSelectVals()
             {
                 var totalUrl="";
                 var memId = document.getElementById("memId").value;
                 var orgId = document.getElementById("orgId").value;
                 var allVals = document.getElementById("allVals").value;
                 totalUrl = "orgId="+orgId+"~memId="+memId+"~allVals=";
                 var allId = allVals.split(",");
                 //alert('allVals '+allVals);

                  for(var m=0;m<allId.length;m++)
                    {
                                var val= document.getElementById(allId[m]); //document.getElementById("parameters2").checked=false
                                if(document.getElementById(val.value).checked==true)
                                totalUrl = totalUrl+","+val.value;

                    }
                    //alert('totalUrl '+totalUrl);
                    $.ajax({
                    url: 'organisationDetails.do?param=saveOrgDimMemberVals&totalUrl='+totalUrl,
                    success: function(data) {
                       // alert('data '+data);
                        if(data=="1")
                            alert("Values Not Saved.")
                        else{
                                 alert('Values Saved.')
                                 cancelSelectValsParent();
                            }

                    }
                    });
             }
         </script>
    </head>
    <%

    String memId=request.getParameter("memId");//+"&subFolderIdUser="+subFolderIdUser+"&userId="+userId
    String orgId = request.getParameter("orgId");
  
    String MemberName= request.getParameter("memberName");
    String subFolderId= request.getParameter("subFolderId");
   ////.println(" subFolderId in jsp "+subFolderId);
    ////.println(" memId "+memId+" orgId "+orgId+" MemberName "+MemberName+"subFolderId="+subFolderId);//getMemberValuesForDim
    if(MemberName==null||MemberName.equalsIgnoreCase("null"))
        MemberName="";
    MemberName=MemberName.replace("~"," ");
    UserLayerDAO uDao=new UserLayerDAO();
  //  String roleSt=uDao.getRolePublishedStForAcc(orgId);
   // ////.println(" roleSt "+roleSt);
    boolean showStatus=false;
    //if(roleSt.equalsIgnoreCase("published"))
        showStatus=true;
    HashMap details =new HashMap();
    ArrayList insertedMemberValues = new ArrayList();
    if(showStatus==true){
    details = uDao.getMemberValuesForDimForAccount(memId,orgId,subFolderId);//getMemberValuesForDim(memId,subFolderIdUser);    //"";//
    insertedMemberValues = uDao.getAddedMemberValuesForAccount(orgId,memId);
    ////.println(" details "+details);
    ////.println(" insertedMemberValues "+insertedMemberValues);
    }
    String detailsArr[]=(String[])details.keySet().toArray(new String[0]);
    String allVals = "";
    boolean dataFlag=false;
    
    

    if(insertedMemberValues.size()>0)
        dataFlag = true;
    %>
    <body>
        <Center>

             <div height="50px"> <font style="color:#369;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">Restrict Account Access</font></div><Br>

            <% if(showStatus==true){%>
            <Table>
        <%for(int m=0;m<detailsArr.length;m++)
        {
            String memName=detailsArr[m];
            ArrayList det = (ArrayList)details.get(memName);//detailsArr[m];

        %>
        <Tr><Td></Td><Td><font style="font-weight:bold;"><%=MemberName%></font></Td></Tr>
        <%
            for(int t=1;t<det.size();t++)
            {
                String val=det.get(t).toString();
                allVals = allVals+","+val;
                String isSelected="checked";
                //////////////////////////////////////////////////////.println.println(" dataFlag "+dataFlag);
                if(dataFlag==true)
                    {
                    if(!insertedMemberValues.contains(val))
                        {
                          isSelected="";
                        }
                    }
                else if(dataFlag==false)
                    isSelected="checked";
        %>

       <Tr>
           <Td><Input type="CHECKBOX" <%=isSelected%> name="<%=val%>" id="<%=val%>" value="<%=val%>"></Td>
           <Td><%=val%></Td>
       </Tr>
        <%
            }
        %>

        <%}
        allVals=allVals.substring(1);
        //////////////////////////////////////////////////////.println.println(" allVals-- "+allVals);
          %>
        </Table>
        <Input type="HIDDEN" name="allVals" id="allVals" value="<%=allVals%>">
        <Input type="HIDDEN" name="orgId" id="orgId" value="<%=orgId%>">
     
        <Input type="HIDDEN" name="memId" id="memId" value="<%=memId%>">
        <Table>
              <Tr>
                  <Td align="center"><Input TYPE="button" value="Reset To Default" ONCLICK="updateVals()"></Td>
                  <Td align="center"><Input TYPE="button" value="Save" ONCLICK="saveSelectVals()"></Td>
                  <Td align="center"><Input TYPE="button" value="Cancel" ONCLICK="cancelSelectVals()"></Td>
              </Tr>
        </Table>
        <%}else{%>
            <div height="50px"> <font style="color:#369;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">The Role Is Not Published</font></div><Br>

            <Table>
              <Tr>
                  <Td align="center"><Input TYPE="button" value="Cancel" ONCLICK="cancelSelectVals()"></Td>
              </Tr>
        </Table>
           <%}%>

        </Center>
    </body>
</html>
