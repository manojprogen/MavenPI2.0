
<%@page import="java.util.HashMap,prg.db.PbReturnObject,com.progen.connection.ConnectionDAO"%>
<% String contextPath=request.getContextPath(); %>
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
        <link href="stylesheets/ui.theme.css" rel="stylesheet" type="text/css" />

        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet"/>
        <link href="<%=contextPath%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css"/>
        
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/BusinessGroup.js"></script>
 

        <title>JSP Page</title>
        <%
     String dbTableId =request.getParameter("dbTableId");
        
    String connectionId="";
     ConnectionDAO conDao=new ConnectionDAO();
    PbReturnObject all =conDao.getRelatedDbTables1(dbTableId);
    PbReturnObject dbDetails= (PbReturnObject)all.getObject("dbDetails");
    PbReturnObject relTabInfo= (PbReturnObject)all.getObject("relTabInfo");
    PbReturnObject relGrpInfo= (PbReturnObject)all.getObject("relGrpInfo");
    PbReturnObject relGrpNames= (PbReturnObject)all.getObject("relGrpNames");
    if(relTabInfo.getRowCount()>0)
    connectionId = relTabInfo.getFieldValueString(0,0);
    HashMap relTabs = new HashMap();
    HashMap relGrps = new HashMap();
    HashMap relGrpName = new HashMap();
    HashMap relGrpList = new HashMap();

    for(int m=0;m<relTabInfo.getRowCount();m++)
            {
          relTabs.put(relTabInfo.getFieldValueString(m,2),relTabInfo.getFieldValueString(m,3));
        }
    for(int m=0;m<relGrpInfo.getRowCount();m++)
                {
          relGrps.put(relGrpInfo.getFieldValueString(m,8),relGrpInfo.getFieldValueString(m,3));
                }
     for(int m=0;m<relGrpNames.getRowCount();m++)
        {
          relGrpName.put(relGrpNames.getFieldValueString(m,0),relGrpNames.getFieldValueString(m,6));
                           }

        %>
       
    </head>
   

    <body>
      <center>
           <Form>
               <Table id="tablesorter" STYLE="width:70%" CLASS="tablesorter" border="1">
                       <Thead>
                          <select id="selectGroup"  onchange="tabDetail()"><option value="">--Select Group--</option>
                          <%for(int m=0;m<dbDetails.getRowCount();m++){


                               String relTable="";
                              String clauseType="";
                             String relationshipId="";
                               relTable=dbDetails.getFieldValueString(m,"TABLE_ID");
                              clauseType=dbDetails.getFieldValueString(m,"CLAUSE_TYPE");
                              relationshipId=dbDetails.getFieldValueString(m,"RELATIONSHIP_ID");
                              if(relTable.equalsIgnoreCase(dbTableId))
                                  {
                                   relTable=dbDetails.getFieldValueString(m,"TABLE_ID2");

                                  }

                               String grpId="";
                              if(relGrps.containsKey(relTable))
                                  grpId = (String)relGrps.get(relTable);
                              String grpName="";
                              if(relGrpName.containsKey(grpId) && !relGrpList.containsKey(grpId)){
                                  grpName = (String)relGrpName.get(grpId);
                                  relGrpList.put(grpId, grpName);
                           %>
                                <option value="<%=grpId%>"><%=grpName%></option>
                          <%}
                          }%>
                           <Tr><Th class="header" ALIGN="left" style="background-color:#79C9EC; "><font color="black" >Table Name</font></Th>
                               <%--<Th class="header" ALIGN="left" style="background-color:#79C9EC; "><font color="black" >Table2 Id</font></Th>--%>
                               <Th class="header" ALIGN="left" style="background-color:#79C9EC "><font color="black" >Actual Clause</font></Th>
                           <Th class="header" ALIGN="left" style="background-color:#79C9EC "><font color="black" >Relationship Exist</font></Th>
                           <Th class="header" ALIGN="left" style="background-color:#79C9EC "><font color="black" >Are Equal</font></Th>
                           <Th class="header" ALIGN="left" style="background-color:#79C9EC "><font color="black" >Action</font></Th></Tr>

                       </select>
                        </Thead>
                       <Tbody id="tabGrpDetail">
                        </Tbody>
                   </Table><Br/><Br/>

                 <Table>
                       <Tr>
                           
                            <Td align="center"><Input TYPE="button" value="Cancel" ONCLICK="cancelRelatedDbTables()" style="background-color:#79C9EC; "></Td>

                       </Tr>
                </Table>
            </Form>
        </center>
                                <script type="text/javascript">

          function cancelRelatedDbTables()
            {
              parent.$("#editDbTableRelations1").dialog('close')

            }
            function UpdateRelationship(bussrelationId,actualClause)
            {
                $.ajax({
                        url: 'editconn.do?parameter=updateBussTable&bussrltId='+bussrelationId+'&actualClause='+actualClause,
                        success: function(data) {
                        alert("Update Successfully")
                        tabDetail();
                        }

                });

            }
            function MigrateRelationship(relationshipId,grpid)
            {
                  $.ajax({
                        url: 'editconn.do?parameter=InsertBussTable&rltId='+relationshipId+'&grpid='+grpid,
                        success: function(data) {
                        alert("Migrate Successfully")
                        tabDetail();
                        }

                });

            }
           function tabDetail()
            {
                
                       var grpId=$("#selectGroup").val();
                       var tabId=<%=dbTableId%>;
                       var tabName;
                       var relationshipid;
                       var bussrelationid;
                       var equal;
                       var relationship;
                $.ajax({
                        url: 'editconn.do?parameter=getRelatedInfo&groupId='+grpId+'&dbTableId='+tabId,
                        success: function(data) {
                           var jsonVar=eval('('+data+')')
                        var tabName1=jsonVar.tableid;
                        tabName=jsonVar.dbTableName;
                        var tabName2=jsonVar.tableid2;
                        var actualclause=jsonVar.actualclause;
                        relationship=jsonVar.relationship;
                        equal=jsonVar.equal;
                        relationshipid=jsonVar.relationshipid;
                        bussrelationid=jsonVar.bussrelationid;
                       var htmlVar="";
                         for(var i=0;i<tabName1.length;i++)
                {
                    if(relationship[i]=='N'&& equal[i]=='N')
                        {
                        htmlVar+="<Tr id='"+tabName1[i]+"'><Td align='left'>"+tabName[i]+"</Td><Td align='left'>"+actualclause[i]+"</Td><Td align='left'>"+relationship[i]+"</Td><Td align='left'>"+equal[i]+"</Td><Td align='left'><Input TYPE='button' value='Migrate' ONCLICK='MigrateRelationship("+relationshipid[i]+","+grpId+")' style='background-color:#79C9EC; '></Td></Tr>";
                        }
                        else if(equal[i]=='N')
                            { htmlVar+="<Tr id='"+tabName1[i]+"'><Td align='left'>"+tabName[i]+"</Td><Td align='left'>"+actualclause[i]+"</Td><Td align='left'>"+relationship[i]+"</Td><Td align='left'>"+equal[i]+"</Td><Td align='left'><Input TYPE='button' value='Update' ONCLICK=\"UpdateRelationship('"+bussrelationid[i]+"','"+actualclause[i]+"')\" style='background-color:#79C9EC; '></Td></Tr>";

                            }
                            else{
                                 htmlVar+="<Tr id='"+tabName1[i]+"'><Td align='left'>"+tabName[i]+"</Td><Td align='left'>"+actualclause[i]+"</Td><Td align='left'>"+relationship[i]+"</Td><Td align='left'>"+equal[i]+"</Td><Td align='left'>Not Migrate</Td></Tr>";
                        
                            }
           }
                        $("#tabGrpDetail").html(htmlVar);
            }
                });

         }
          </script>
    </body>
</html>
