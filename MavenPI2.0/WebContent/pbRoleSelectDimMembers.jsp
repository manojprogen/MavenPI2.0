<%@page import="java.util.*,com.progen.userlayer.db.UserLayerDAO,prg.db.PbReturnObject"%>

<% String contextPath=request.getContextPath(); %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
              <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
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
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/pbUserLayer.js"></script>


       
    </head>
    <%
    String sFolder=request.getParameter("sFolder");
    String memId=request.getParameter("memId");
    String dimTabId=request.getParameter("dimTabId");
    //////////////////////////////////////////////////////////////////.println.println(memId+" dimTabId "+dimTabId+" sFolder "+sFolder);
    UserLayerDAO uDao=new UserLayerDAO();
    HashMap details =uDao.getMemberValuesForDimRole(memId,dimTabId,sFolder);
    String detailsArr[]=(String[])details.keySet().toArray(new String[0]);
    HashMap detVal = uDao.getRoleFilterDetails(memId,dimTabId,sFolder);
    String dispName=(String)detVal.get("dispName");
    String folderId=(String)detVal.get("folderId");
    ArrayList insertedMemberValues = new ArrayList();
    insertedMemberValues = uDao.getAddedMemberValuesForRole(memId,dimTabId,sFolder);
    String allVals = "";
        boolean dataFlag=false;
    //////////////////////////////////////////////////////////////////.println.println(" insertedMemberValues "+insertedMemberValues);

    if(insertedMemberValues.size()>0)
        dataFlag = true;
   
    %>
    <body>
        <Center>
            <Form name="myForm" id="myForm">
        <div height="50px"> <font style="color:black;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">Select Values To Restrict Access</font></div><Br>

        <Table>
        <%for(int m=0;m<detailsArr.length;m++)
        {
            String memName=detailsArr[m];
            ArrayList det = (ArrayList)details.get(memName);//detailsArr[m];

        %>
        <Tr><Td></Td><Td><font style="font-weight:bold;"><%=dispName%></font></Td></Tr>
        <%
            for(int t=1;t<det.size();t++)
            {
                String val=det.get(t).toString();
                allVals = allVals+","+val;
                String isSelected="checked";
                //////////////////////////////////////////////////////////////////////.println.println(" dataFlag "+dataFlag);
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
        if(allVals.length()>0)
        allVals=allVals.substring(1);
        //////////////////////////////////////////////////////////////////.println.println(" allVals-- "+allVals);
          %>
        </Table>
       <Br/><Br/>
        <Table>
              <Tr>
                  <Td align="center"><Input TYPE="button" value="Reset To Default" ONCLICK="updateVals()"></Td>
                  <Td align="center"><Input TYPE="button" value="Save" ONCLICK="saveSelectVals()"></Td>
                  <Td align="center"><Input TYPE="button" value="Cancel" ONCLICK="cancelSelectVals()"></Td>
              </Tr>
        </Table>
          <Input type="hidden" name="memId" id="memId" value="<%=memId%>">
          <Input type="hidden" name="allVals" id="allVals" value="<%=allVals%>">
          <Input type="hidden" name="folderId" id="folderId" value="<%=folderId%>">
        </Form>
        </Center>
         <script type="text/javascript">
        function cancelSelectVals()
             {
                 parent.cancelSelectValsParentRole();
             }
              function saveSelectVals()
             {
                 var totalUrl="";
                 var folderId=document.getElementById("folderId").value;
                 var memId = document.getElementById("memId").value;
                 var allVals = document.getElementById("allVals").value;
                 totalUrl = "memId="+memId+"~folderId="+folderId+"~allVals=";
                 var allId = allVals.split(",");
                // alert('allVals '+allVals);
                 for(var m=0;m<allId.length;m++)
                    {
                                var val= document.getElementById(allId[m]); //document.getElementById("parameters2").checked=false
                                if(document.getElementById(val.value).checked==true)
                                    totalUrl = totalUrl+","+val.value.replace("&",";");
                    }
                    //alert(' totalUrl '+totalUrl);
                    $.ajax({
                    url: 'userLayerAction.do?userParam=addUserDimMemberValuesForRole&totalUrl='+totalUrl,
                    success: function(data) {
                       // alert('data '+data);
                        if(data=="1")
                            alert("Values Not Saved.")
                        else{
                                 alert('Values Saved Successfully.');
                                 cancelSelectVals();
                            }

                    }
                    });
             }
            
            function updateVals()
            {
                 var folderId=document.getElementById("folderId").value;
                 var memId = document.getElementById("memId").value;

                var totalUrl = "memId="+memId+"~folderId="+folderId;
               // alert('Are you sure you want to remove the access.');
                var t=confirm('Are you sure you want to remove the access.');
                if(t==true){
                $.ajax({
                    url: 'userLayerAction.do?userParam=deleteDimMemberValuesForRole&totalUrl='+totalUrl,
                    success: function(data) {
                       // alert('data '+data);
                        if(data=="1")
                            alert("Values Not Saved.")
                        else{
                                 alert('Values Set To Default.');
                                 cancelSelectVals();
                            }

                    }
                    });
                }
            }
         </script>
    </body>
</html>
