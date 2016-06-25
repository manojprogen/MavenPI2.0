
<%@page import="java.util.*,prg.db.PbReturnObject,com.progen.userlayer.db.UserLayerDAO"%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/pbUserLayer.js"></script>
        <link href="stylesheets/pbUserLayer.css" rel="stylesheet" type="text/css"/>

        
    </head>
    <%
    String subFolderUserId =request.getParameter("subFolderUserId");
    String data[] = subFolderUserId.split(":");
    String subFolderId="";//data[0];
    String userId="";//data[1];
    for(int u=0;u<data.length;u++)
        {
          subFolderId = data[0];
          userId = data[1];
        }
    //////////////////////////////////////////////////////////.println.println(subFolderId+" subFolderId userId "+userId);
    UserLayerDAO uDao = new UserLayerDAO();
    PbReturnObject allData = uDao.getUserCustomDrill(subFolderId,userId);
    PbReturnObject details = (PbReturnObject)allData.getObject("Details");
    PbReturnObject members = (PbReturnObject)allData.getObject("Members");
    PbReturnObject RelDetails = (PbReturnObject)allData.getObject("RelDetails");
    PbReturnObject dimDetails = (PbReturnObject)allData.getObject("DimDetails");

    HashMap childs = new HashMap();
    for(int m=0;m<members.getRowCount();m++)
        {
          childs.put(members.getFieldValueString(m,"MEMBER_ID"),members.getFieldValueString(m,"CHILD_MEMBER_ID"));
        }
    //////////////////////////////////////////////////////////.println.println(" childs "+childs);
    HashMap dimMembers=new HashMap();
    Vector allDims=new Vector();
    ArrayList al = new ArrayList();
    String dimId = "";
     for(int m=0;m<details.getRowCount();m++)
        {
           dimId =details.getFieldValueString(m,"DIM_ID");
           if(allDims.contains(dimId))
           {
             al=(ArrayList)dimMembers.get(dimId);
             al.add(details.getFieldValueString(m,"MEMBER_ID"));
             dimMembers.put(dimId,al);
           }
           else
               {
                 al = new ArrayList();
                 al.add(details.getFieldValueString(m,"MEMBER_ID"));
                 dimMembers.put(dimId,al);
               }
        }
    ArrayList dimList = new ArrayList();
    HashMap membersVal=new HashMap();
    for(int m=0;m<dimDetails.getRowCount();m++)
              {
                ArrayList det = new ArrayList();
                det.add(dimDetails.getFieldValueString(m,"MEMBER_NAME"));
                dimList.add(dimDetails.getFieldValueString(m,"MEMBER_ID"));
                membersVal.put(dimDetails.getFieldValueString(m,"MEMBER_ID"),det);
              }
    //////////////////////////////////////////////////////////.println.println(dimList+" dimList membersVal "+membersVal);
    String allSIds = "";
     for(int n=0;n<dimList.size();n++){
         if(childs.containsKey(dimList.get(n).toString()))
         allSIds = allSIds+","+dimList.get(n).toString();
         }
    %>
    <body>
        <center>
            <Form name="myForm">
                       <div height="50px"> <font style="font-weight:bold;font-size:12px" face="verdana">Edit User Custom Drill</font></div><Br>
                       <Input type="HIDDEN" name="subFolderId" id="subFolderId" value="<%=subFolderId%>">
                       <Input type="HIDDEN" name="userId" id="userId" value="<%=userId%>">
                       <Input TYPE="hidden" name="allSIds" id="allSIds" value="<%=allSIds%>">

                         <Table>
                            <Tr>
                                <Td ALIGN="center" style="background-color:silver;font-weight:bold;font-size:12px">Member Name</Td>
                                <Td>&nbsp;&nbsp;&nbsp;</Td>
                                <Td ALIGN="center" style="background-color:silver;font-weight:bold;font-size:12px">Child Member Name</Td>

                            </Tr><Tr></Tr><Tr></Tr>
                            <%
                                for(int p=0;p<dimList.size();p++)
                           {
                             String memId = dimList.get(p).toString();
                             String memName="";
                             String chid="";
                             String childName="";
                             if(childs.containsKey(memId))
                             {
                             if(childs.containsKey(memId))
                                 chid = childs.get(memId).toString();
                             if(membersVal.containsKey(memId))
                                 {
                                   ArrayList d = (ArrayList)membersVal.get(memId);
                                   memName = d.get(0).toString();
                                 }
                             if(membersVal.containsKey(chid))
                                 {
                                   ArrayList d = (ArrayList)membersVal.get(chid);
                                   childName = d.get(0).toString();
                                 }
                            %>
                            <Tr>
                                <Td><Input type="text" readonly STYLE="background-color:white" value="<%=memName%>"></Td>
                                <Td>&nbsp;&nbsp;&nbsp;</Td>
                                  <Td>
                                    <select style="width:100px" id="<%=memId%>" name="<%=memId%>">
                                                                               <%
                                           for(int r=0;r<dimList.size();r++)
                                          {
                                                  String childM = dimList.get(r).toString();
                                                  ArrayList al2 =new ArrayList();
                                                  String isSelected="";
                                                  String memName2 ="";
                                              if(childs.containsKey(childM))
                                              {
                                               if(membersVal.containsKey(childM))
                                                {
                                                 if(chid.equalsIgnoreCase(childM))
                                                     isSelected = "selected";
                                                 al2=(ArrayList)membersVal.get(childM);
                                                 memName2 = al2.get(0).toString();
                                       %>
                                       <option value="<%=childM%>" <%=isSelected%>>
                                                <%=memName2%>
                                        </option>
                                       <%
                                               }}%>
                                       <%}%>
                                    </select>
                                </Td>

                            </Tr>
                            <%}}%>
                         </Table><Br/>
                      <Table>
                        <Tr>
                            <Td align="center"><Input TYPE="button" value="Save" ONCLICK="saveUserDrill()"></Td>
                            <Td align="center"><Input TYPE="button" value="Cancel" ONCLICK="cancelEditUserDrill()"></Td>
                        </Tr>
                      </Table>
            </Form>
        </center>
                         <script type="text/javascript">
             function cancelEditUserDrill()
             {
                 parent.cancelEditUserDrillParent();
             }
             function saveUserDrill()
             {
                 var allSIds = document.getElementById("allSIds");
                 var userId = document.getElementById("userId").value;
                 var subFolderId = document.getElementById("subFolderId").value;
                 var totalUrl ="";
                 totalUrl = "subFolderId="+subFolderId+"-"+"userId="+userId;
                 var allId = allSIds.value.split(",");
                    for(var m=0;m<allId.length;m++)
                    {
                       // alert(allId[m]);
                        if(m!=0)
                            {
                                var val= document.getElementById(allId[m]);
                                //alert(' val '+val)
                                for(var p=0;p<val.length;p++)
                                    {
                                        if(val[p].selected){
                                            //alert(val[p].value);
                                            totalUrl = totalUrl+"-"+allId[m]+"="+val[p].value;
                                        }
                                    }
                            }
                    }
                    // alert('totalUrl '+totalUrl)

                    $.ajax({
                    url: 'userLayerAction.do?userParam=editUserCustomRoleDrill&totalUrl='+totalUrl,
                    success: function(data) {
                        if(data=="false")
                            alert("Drill Not Saved.")
                        else{
                                 alert('Drill Saved.')
                                 parent.cancelEditUserDrillParent();
                            }

                            }
                        });

             }
         </script>
    </body>
</html>
