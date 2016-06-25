<%@page import="com.progen.userlayer.db.UserLayerDAO,java.util.*,prg.db.PbReturnObject"%>


<html>
    <Head>
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
        <link href="stylesheets/pbUserLayer.css" rel="stylesheet" type="text/css"/>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <script type="text/javascript">
             function cancelViewDrill()
             {
                 parent.cancelViewDrillParent();
             }
         </script>
         </Head>
         <%
              String subFolderId = request.getParameter("subFoldId");
              //////////////////////////////////////////////////////////////////////////////////.println.println(" subFoldId in view jsp "+subFolderId);
              UserLayerDAO uDao = new UserLayerDAO();
              PbReturnObject allData = uDao.getRelationForDrill(subFolderId);
              PbReturnObject relDet = (PbReturnObject)allData.getObject("RelationDetails");
              PbReturnObject dimDetails = (PbReturnObject)allData.getObject("DimDetails");
              PbReturnObject allInfo = (PbReturnObject)allData.getObject("allInfo");
              HashMap details = new HashMap();
              for(int u=0;u<allInfo.getRowCount();u++)
                  details.put(allInfo.getFieldValueString(u,3),allInfo.getFieldValueString(u,4));
              ArrayList dimList = new ArrayList();
              HashMap members = new HashMap();
              ArrayList dimn = new ArrayList();
              for(int m=0;m<dimDetails.getRowCount();m++)
              {
                ArrayList det = new ArrayList();
                det.add(dimDetails.getFieldValueString(m,4));
                dimn.add(dimDetails.getFieldValueString(m,2));
                dimList.add(dimDetails.getFieldValueString(m,3));
                members.put(dimDetails.getFieldValueString(m,3),det);

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
                       //dimList.add(relDet.getFieldValueString(n,0));
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
                           //////////////////////////////////////////////////////////////////////////////////.println.println(chElementId+" oldDimId- "+oldDimId+" dimId "+dimId);
                           if(oldDimId.equalsIgnoreCase(dimId))
                           {
                               //////////////////////////////////////////////////////////////////////////////////.println.println(" in if 2");
                               chElementId=relDet.getFieldValueString(next,0);
                               //////////////////////////////////////////////////////////////////////////////////.println.println(" chElementId "+chElementId);
                           }
                     }
                     oldDimId = dimId;
                     childs.put(memId,chElementId);
                  }
              //////////////////////////////////////////////////////////////////////////////////.println.println(" childs "+childs);
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
          ////////////////////////////////////////////////////////////////////////////////.println.println(" dimList "+dimList);
          ////////////////////////////////////////////////////////////////////////////////.println.println(" details "+details);
          ////////////////////////////////////////////////////////////////////////////////.println.println(" members "+members);
         %>
         <Body>
                 <center>
                     <Form name="myForm">
                <div height="50px"> <font style="color:#369;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">View Custom Drill</font></div><Br>
               
                <Table WIDTH="60%" ALIGN="center">
                    <Tr STYLE="height:20px">
                        <Td ALIGN="center" WIDTH="50%"style="background-color:#b4d9ee;font-size:12px;font-family:verdana">Member Name</Td>

                        <Td ALIGN="center" WIDTH="50%" style="background-color:#b4d9ee;font-size:12px;font-family:verdana">Child Member Name</Td>

                    </Tr>
                            <Tr>
                        <br>
                    </Tr>
                           <% Set keysSet = details.keySet();
                           String kArr[] = (String[])keysSet.toArray(new String[0]);
                            for(int p=0;p<dimList.size();p++)
                           { String memId = dimList.get(p).toString();
                             String memName="";
                             String dimName=dimn.get(p).toString();
                             String chid="";
                             String childName="";
                             if(details.containsKey(memId))
                             {
                             if(details.containsKey(memId))
                                 chid = details.get(memId).toString();
                             if(members.containsKey(memId))
                                 {
                                   ArrayList d = (ArrayList)members.get(memId);
                                   memName = dimName+"-"+d.get(0).toString();
                                 }
                             if(members.containsKey(chid))
                                 {
                                   ArrayList d = (ArrayList)members.get(chid);
                                   childName = dimName+"-"+d.get(0).toString();
                                 }
                             %>
                            <Tr>
                        <Td WIDTH="50%"><Input type="text" size="24" readonly STYLE="background-color:white" value="<%=memName%>"></Td>

                        <Td WIDTH="50%"><Input type="text" size="24" readonly STYLE="background-color:white"  value="<%=childName%>"></Td>
                            </Tr>
                            <%}}%>
                         </Table><Br/>
                         <Table>
                    <Tr><Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Cancel" ONCLICK="cancelViewDrill()"></Td></Tr>
                         </Table>
                     </Form>
                 </center>
         </Body>
         </Html>
