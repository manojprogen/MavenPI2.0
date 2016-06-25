<%@page import="prg.db.PbReturnObject,java.util.*,com.progen.userlayer.db.UserLayerDAO"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
                <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
                <!--<link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />-->

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
        <link href="stylesheets/pbUserLayer.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        
    </head>
    <%
      String subFolderId = request.getParameter("subFoldId");
      ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" subFoldId in jsp "+subFolderId);
      UserLayerDAO uDao = new UserLayerDAO();
      PbReturnObject allData = uDao.getRelationForDrill(subFolderId);
      PbReturnObject relDet = (PbReturnObject)allData.getObject("RelationDetails");
      PbReturnObject dimDetails = (PbReturnObject)allData.getObject("DimDetails");
      PbReturnObject allInfo = (PbReturnObject)allData.getObject("allInfo");
      HashMap details = new HashMap();
      for(int u=0;u<allInfo.getRowCount();u++)
          details.put(allInfo.getFieldValueString(u,4),allInfo.getFieldValueString(u,5));
      ArrayList dimList = new ArrayList();
      HashMap members = new HashMap();
//      HashMap dimention = new HashMap();
      ArrayList dimn = new ArrayList();
      for(int m=0;m<dimDetails.getRowCount();m++)
      {
        ArrayList det = new ArrayList();
        det.add(dimDetails.getFieldValueString(m,4));
        dimn.add(dimDetails.getFieldValueString(m,2));
        members.put(dimDetails.getFieldValueString(m,3),det);
        dimList.add(dimDetails.getFieldValueString(m,3));
      }
      HashMap dimMem = new HashMap();
      ArrayList relDetList=new ArrayList();
      for(int n=0;n<relDet.getRowCount();n++)
          {
              String dimId = relDet.getFieldValueString(n,3);
              if(dimMem.containsKey(dimId))
                  {
                  relDetList = (ArrayList)dimMem.get(dimId);
                  relDetList.add(relDet.getFieldValueString(n,0));
                  dimMem.put(dimId,relDetList);
                }
              else
                {
                    relDetList = new ArrayList();
                    relDetList.add(relDet.getFieldValueString(n,0));
                    dimMem.put(dimId,relDetList);
                }
              // dimList.add(relDet.getFieldValueString(n,0));
          }
      HashMap childs = new HashMap();
      String oldDimId=relDet.getFieldValueString(0,3);
      boolean skFl=false;
     for(int n=0;n<relDet.getRowCount();n++)
          {

             String memId = relDet.getFieldValueString(n,0);
             String dimId ="";
             String childId="";
             int next=n+1;
             String chElementId=relDet.getFieldValueString(n,0);
             if(next<relDet.getRowCount())
             {     dimId=relDet.getFieldValueString(next,3);
                   ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(chElementId+" oldDimId- "+oldDimId+" dimId "+dimId);
                   if(oldDimId.equalsIgnoreCase(dimId))
                   {
                       ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" in if 2");
                       chElementId=relDet.getFieldValueString(next,0);
                      // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" chElementId "+chElementId);
                   }
             }
             oldDimId = dimId;
             childs.put(memId,chElementId);
          }
     // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" childs "+childs);
       for(int n=0;n<relDet.getRowCount();n++)
          {
             String dimId = relDet.getFieldValueString(n,3);
             String memId = relDet.getFieldValueString(n,0);
             if(childs.containsKey(memId))
              {
                 String chId = childs.get(memId).toString();
                 if(dimMem.containsKey(dimId))
                     {
                        ArrayList det = (ArrayList)dimMem.get(dimId);
                        if(!det.contains(chId))
                            childs.put(memId,memId);
                     }
              }
          }
 
     Set kSet = (Set)dimMem.keySet();
     String arr[] = (String [])kSet.toArray(new String[0]);
     ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" details "+details);
     String allSIds = "";
     for(int n=0;n<dimList.size();n++){
         if(details.containsKey(dimList.get(n).toString()))
         allSIds = allSIds+","+dimList.get(n).toString();
         }
     //////////////////////////////////////////////////////////////////////////////////////////////////.println.println(allSIds+" details "+details);
    %>
    <body>
        <center>
            <Form name="myForm">
                <div height="50px"> <font style="font-weight:bold;font-size:12px;color:#369;" face="verdana">Edit Custom Drill</font></div><Br>
                <div height="200px">
                    <Input TYPE="hidden" name="allSIds" id="allSIds" value="<%=allSIds%>">
                    <Input TYPE="hidden" name="subFolderId" id="subFolderId" value="<%=subFolderId%>">
                    <Table WIDTH="100%" ALIGN="center">
                           <%for(int p=0;p<dimList.size();p++)
                           { String memId = dimList.get(p).toString();
                             String dimName=dimn.get(p).toString();
                             String memName="";
                             String chid="";
                              if(details.containsKey(memId))
                                              {
                             if(details.containsKey(memId))
                                 chid = details.get(memId).toString();
                             if(members.containsKey(memId))
                                 {
                                   ArrayList d = (ArrayList)members.get(memId);

                                   memName = dimName+"-"+d.get(0).toString();
                                 }
                             %>
                            <Tr>
                                <Td WIDTH="50%"><Input type="text" size="27" readonly STYLE="background-color:white" value="<%=memName%>"></Td>
                                <Td WIDTH="50%">
                                    <select style="width:100%" id="<%=memId%>" name="<%=memId%>">
                                       <%
                                           for(int r=0;r<dimList.size();r++)
                                          {
                                               dimName=dimn.get(r).toString();
                                                  String childM = dimList.get(r).toString();
                                                  ArrayList al2 =new ArrayList();
                                                  String isSelected="";
                                                  String memName2 ="";
                                              if(details.containsKey(childM))
                                              {
                                               if(members.containsKey(childM))
                                                {
                                                 if(chid.equalsIgnoreCase(childM))
                                                     isSelected = "selected";
                                                 al2=(ArrayList)members.get(childM);
                                                 memName2 = al2.get(0).toString();
                                       %>
                                       <option value="<%=childM%>" <%=isSelected%>>
                                                <%=dimName+"-"+memName2%>
                                        </option>
                                       <%
                                               }}%>
                                       <%}%>
                                    </select>
                                </Td>
                            </Tr>
                             <%}}%>

                    </Table>
                </div>
                <br/>
                <Table>
                    <Tr>
                        <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Save" ONCLICK="saveDrill()"></Td>
                        <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Cancel" ONCLICK="cancelDrill()"></Td>
                    </Tr>
                </Table>
            </Form>
        </center>
                              <script type="text/javascript">

      //added by susheela
        function saveDrill()
            {
                var allSIds = document.getElementById("allSIds");
                var subFolderId = document.getElementById("subFolderId");
               // alert(allSIds.value);
                var totalUrl ="";
                totalUrl = "subFolderId="+subFolderId.value+"";
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
                        

                    url: 'userLayerAction.do?userParam=editCustomeRoleDrill&totalUrl='+totalUrl,
                    success: function(data) {
                        if(data=="false")
                            alert("Drill Not Saved.")
                        else{
                                 alert('Drill Saved.')
                                 parent.saveDrillDiv();
                            }

                    }
                }); 
            }
        function cancelDrill()
        {
            cancelShowDrill();
        }
         function cancelShowDrill()
        {
            parent.cancelDrillDiv();
        }
        </script>
    </body>
</html>
