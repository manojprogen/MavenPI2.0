
<%@page import="com.progen.connection.ConnectionDAO,java.util.HashMap,prg.db.PbReturnObject"%>
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
        <link href="stylesheets/ui.theme.css" rel="stylesheet" type="text/css" />

        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet"/>
        <link href="<%=contextPath%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/TableDisplay/css/TableDisplay.css" />
         <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/BusinessGroup.js"></script>


        <title>JSP Page</title>
     
      
    </head>
    <%
    String dbTableId =request.getParameter("dbTableId");
    String connectionId="";
     String tableName="";
    ////////////////////////////////////////////////////////////////////////////.println.println(" dbTableId in jsp-. "+dbTableId);
    ConnectionDAO conDao=new ConnectionDAO();
    PbReturnObject all =conDao.getRelatedDbTables(dbTableId);
    PbReturnObject dbCols= (PbReturnObject)all.getObject("dbCols");
    PbReturnObject dbDetails= (PbReturnObject)all.getObject("dbDetails");
    PbReturnObject relTabInfo= (PbReturnObject)all.getObject("relTabInfo");
     PbReturnObject dbTabInfo= (PbReturnObject)all.getObject("dbTabInfo");
    if(relTabInfo.getRowCount()>0)
    connectionId = dbTabInfo.getFieldValueString(0,"CONNECTION_ID");
      tableName = dbTabInfo.getFieldValueString(0,"TABLE_NAME");
    HashMap relTabs = new HashMap();
    HashMap relCols = new HashMap();
    for(int m=0;m<relTabInfo.getRowCount();m++)
        {
          relTabs.put(relTabInfo.getFieldValueString(m,"TABLE_ID"),relTabInfo.getFieldValueString(m,"TABLE_NAME"));
        }
    for(int m=0;m<dbCols.getRowCount();m++)
        {
          relCols.put(dbCols.getFieldValueString(m,"COLUMN_ID"),dbCols.getFieldValueString(m,"TABLE_COL_NAME"));
        }
    ////////////////////////////////////////////////////////////////////////////.println.println(" relTabs "+relTabs);
    ////////////////////////////////////////////////////////////////////////////.println.println("relCols  "+relCols);
    String allTabValues="";

    %>
    <body>
      <center>
           <Form>
                   
 <Table  style="width:70%" class="prgtable">
     <Tr><td><div height="50px" align="left"> <font style="font-weight:bold;font-size:12px;color:black;" face="verdana"><%=tableName%></font></div></td>
         <Td align="right" width="5%"><Input TYPE="button" value="Save" ONCLICK="saveRelatedDbTables1()" class="navtitle-hover"></Td>
                           <Td align="right" width="5%"><Input TYPE="button" value="Cancel" ONCLICK="cancelRelatedDbTables()" class="navtitle-hover"></Td>

                       </Tr>
                </Table>
                   <Table id="relTabledetails" style="width:70%"  border="1">
                       <Thead>
                           <Tr><Th class="header" ALIGN="left" style="background-color:#79C9EC; "><font color="black" >Related Table</font></Th><Th class="header" ALIGN="left" style="background-color:#79C9EC "><font color="black" >Actual Clause</font></Th>
                           <Th class="header" ALIGN="left" style="background-color:#79C9EC "><font color="black" >Clause Type</font></Th>
                           <Th class="header" ALIGN="left" style="background-color:#79C9EC "><font color="black" >Delete</font></Th></Tr>
                       </Thead>
                       <Tbody>
                          <%for(int m=0;m<dbDetails.getRowCount();m++){
                              String relTable="";
                              String relCol="";
                              String val="P";
                              String relTableS="";
                              String relColS="";
                              String relTableP="";
                              String relColP="";
                              String actualClause="";
                              String clauseType="";
                             String relationshipId="";
                            /*  relTable=dbDetails.getFieldValueString(m,"P_TABLE_ID");
                              relCol = dbDetails.getFieldValueString(m,"P_COL_ID1");
                              relTableS = dbDetails.getFieldValueString(m,"S_TABLE_ID");
                              relColS = dbDetails.getFieldValueString(m,"S_COL_ID1");
                              relTableP = dbDetails.getFieldValueString(m,"P_TABLE_ID");
                              relColP = dbDetails.getFieldValueString(m,"P_COL_ID1");*/
                              relTable=dbDetails.getFieldValueString(m,"TABLE_ID");
                              actualClause=dbDetails.getFieldValueString(m,"ACTUAL_CLAUSE");
                              clauseType=dbDetails.getFieldValueString(m,"CLAUSE_TYPE");
                              relationshipId=dbDetails.getFieldValueString(m,"RELATIONSHIP_ID");
                              if(relTable.equalsIgnoreCase(dbTableId))
                                  {
                                   relTable=dbDetails.getFieldValueString(m,"TABLE_ID2");
                                //   relCol = dbDetails.getFieldValueString(m,"S_COL_ID1");
                                   val="S";
                                  }
                               String tabName="";
                               if(relTabs.containsKey(relTable))
                               {
                                   if(relTabs!=null)
                                       {
                                  tabName = (String)relTabs.get(relTable);
                                  }                                  
                               }
                            /*  String colName="";
                              if(relCols.containsKey(relCol))
                                  colName = (String)relCols.get(relCol);*/
                              ////////////////////////////////////////////////////////////////////////////.println.println(" relCol "+relCol);
                              allTabValues =allTabValues+","+relationshipId;
                           %>
                           <Tr id="<%=relationshipId%>"><Td><%=tabName%></Td><Td><%=actualClause%></Td>
                               <td><%=clauseType%></td><Td><a class="ui-icon ui-icon-close" id="<%=m%>" onclick="javascript:updateCols(this)"></a></Td></Tr>
                          <%}%>
                          </Tbody>
                   </Table><Br/><Br/>
                   <Input type="hidden" name="allTabValues" id="allTabValues" value="<%=allTabValues%>">
                   <Input type="hidden" name="dbTableId" id="dbTableId" value="<%=dbTableId%>">
                   <Input type="hidden" name="connectionId" id="connectionId" value="<%=connectionId%>">
                
            </Form>
        </center>
                     <script type="text/javascript">
           <%-- function saveRelatedDbTables()
            {
                //alert('in save ');
                var allTabValues = document.getElementById("allTabValues").value;
                var dbTableId = document.getElementById("dbTableId").value;
                var relationTable=document.getElementById("relTabledetails");
                var connectionId = document.getElementById("connectionId").value;

                var tabRow=relationTable.rows.length;
                var trObjs = relationTable.getElementsByTagName("Tr")
               // alert("rows are:: "+tabRow);
                var values="";
                for(var i=0;i<tabRow;i++)
                {
                    var tt =trObjs[i].getAttribute("id");
                    if(tt!=null)
                    values=values+","+tt;
                //alert('tt '+tt);
                }
               // alert(' values '+values);
              //alert(allTabValues+' allTabValues values '+values+" dbTableId "+dbTableId);
               $.ajax({
                        url: 'editconn.do?parameter=editRelatedDbTables&connectionId='+connectionId+'&dbTableId='+dbTableId+'&allTabValues='+allTabValues+'&values='+values,
                        success: function(data) {
                           if(data==1){
                            alert("The relation is saved.");
                                        var frameObj=parent.document.getElementById("editDbTableRelationsFrame");
                                        var divObj=parent.document.getElementById("editDbTableRelations");
                                        frameObj.style.display='none';
                                        divObj.style.display='none';
                                        parent.document.getElementById('fade').style.display='none';
                                        //parent.refreshDim();
                           }
                        }
                    });
            }
--%>

     function saveRelatedDbTables1()
            {
                //alert('in save ');
                var allTabValues = document.getElementById("allTabValues").value;
                var dbTableId = document.getElementById("dbTableId").value;
                var relationTable=document.getElementById("relTabledetails");
                var connectionId = document.getElementById("connectionId").value;

                var tabRow=relationTable.rows.length;
                var trObjs = relationTable.getElementsByTagName("Tr")
               // alert("rows are:: "+tabRow);
                var values="";
                for(var i=0;i<tabRow;i++)
                {
                    var tt =trObjs[i].getAttribute("id");
                    if(tt!=null)
                    values=values+","+tt;
                //alert('tt '+tt);
                }
               // alert(' values '+values);
              //alert(allTabValues+' allTabValues values '+values+" dbTableId "+dbTableId);
               $.ajax({
                        url: 'editconn.do?parameter=editRelatedDbTables&connectionId='+connectionId+'&dbTableId='+dbTableId+'&allTabValues='+allTabValues+'&values='+values,
                        success: function(data) {
                           if(data==1){
                            alert("The relation is saved.");
                             parent.$("#editDbTableRelations").dialog('close');
                                       <%-- var frameObj=parent.document.getElementById("editDbTableRelationsFrame");
                                        var divObj=parent.document.getElementById("editDbTableRelations");
                                        frameObj.style.display='none';
                                        divObj.style.display='none';
                                        parent.document.getElementById('fade').style.display='none';--%>
                                        //parent.refreshDim();
                           }
                        }
                    });
            }

            function updateCols(val)
            {
              deleteColumnDbRelation(val);
            }
            function deleteColumnDbRelation(index)
            {
                var relationTable=document.getElementById("relTabledetails");
                var bodyObj = relationTable.getElementsByTagName("tbody")[0];
                var trObjs = bodyObj.getElementsByTagName("tr");
               // alert("trObjs length is: "+trObjs.length)
               // alert('index.id   '+index.id);
                bodyObj.deleteRow(index.id);

                for(var i=0;i<trObjs.length;i++)
                {
                    var tdObjs = trObjs[i].getElementsByTagName("td");
                    var childObj = tdObjs[2].getElementsByTagName("a");
                    childObj.id = i;
                }

            }
          function cancelRelatedDbTables()
         {
//            var frameObj=parent.document.getElementById("editDbTableRelationsFrame");
//            var divObj=parent.document.getElementById("editDbTableRelations");
//            frameObj.style.display='none';
//            divObj.style.display='none';
            //parent.document.getElementById('fade').style.display='none';
            parent.$("#editDbTableRelations").dialog('close')
            //parent.refreshDim();
         }
                /*    $.ajax({
                    url: 'editconn.do?parameter=deleteUserConnection&connectionId='+conId,
                    success: function(data) {
                        //alert(data);
                        if(data==1)
                            alert("Unable to Delete Connection.Some Business Group exists.")
                        else{
                              alert("Connection deleted successfully.")
                        }

                    }
                }); */
           //added by susheela over
          </script>
    </body>
</html>
