<%-- 
    Document   : PbEditReportDrill
    Created on : Feb 6, 2010, 11:26:21 AM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.reportdesigner.db.ReportTemplateDAO,prg.db.*,java.util.*,com.progen.reportdesigner.db.DashboardTemplateDAO,javax.print.DocFlavor.STRING"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


   <%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String fromPage = request.getParameter("fromPage");
            String reportId=request.getParameter("reportId");
            String folderId=request.getParameter("folderId");
            String contextPath=request.getContextPath();
   %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        
    </head>
    <body>
        <%  
            Map<String,String> drillMap = (Map<String, String>) session.getAttribute(reportId+"-DrillMap");
            String userId=(String)session.getAttribute("USERID");
            PbDb pbdb=new PbDb();
           // //////////////////////////////////////.println.println(userId+" reportId/./ "+reportId);
            PbReturnObject userDrillObj=new PbReturnObject();
            HashMap eleDet=new HashMap();
            HashMap typeDet=new HashMap();
            String userReportDrill="select * from prg_report_custom_drill where report_id="+reportId+" and user_id="+userId;
            

            if(userDrillObj.getRowCount()==0)
             {
             // //////////////////////////////////////.println.println(" userReportDrill "+userReportDrill);
              userDrillObj=pbdb.execSelectSQL(userReportDrill);
             }

            String userDrill="select * from prg_grp_role_custom_drill where drill_id in(select drill_id from PRG_USER_ROLE_DRILL where " +
                    "user_id="+userId+" and sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id " +
                    "in(select folder_id from prg_ar_report_details where report_id="+reportId+")))";
           // //////////////////////////////////////.println.println(" userDrill "+userDrill);
            if(userDrillObj.getRowCount()==0)
             {
              userDrillObj=pbdb.execSelectSQL(userDrill);
             }

            String userRoleDrillQ="select * from prg_grp_role_custom_drill where sub_folder_id in(select sub_folder_id " +
                    "from prg_user_folder_detail where folder_id in(select folder_id from prg_ar_report_details where " +
                    "report_id="+reportId+")) and drill_id is null";
           // //////////////////////////////////////.println.println(" userRoleDrillQ "+userRoleDrillQ);
            if(userDrillObj!=null)
                {
                if(userDrillObj.getRowCount()>0)
                    {
                    
                    }
                }
            if(userDrillObj.getRowCount()==0)
            {
                userDrillObj=pbdb.execSelectSQL(userRoleDrillQ);
            }
            for(int m=0;m<userDrillObj.getRowCount();m++)
                {
                   String  memId="";
                   String assignType="";
                   memId=userDrillObj.getFieldValueString(m,2);
                   assignType=userDrillObj.getFieldValueString(m,4);
                   ArrayList al=new ArrayList();
                   al.add(userDrillObj.getFieldValueString(m,3));
                   eleDet.put(memId,al);
                  typeDet.put(memId,assignType);
                }

            //String getReportMembersQ="select * from prg_ar_report_param_details where report_id="+reportId+" order by dim_id";
              String getReportMembersQ="select * from prg_user_all_ddim_details where info_element_id " +
                      "in(select element_id from prg_ar_report_param_details where report_id="+reportId+") order by info_dim_id";
              
            PbReturnObject repParamObj=pbdb.execSelectSQL(getReportMembersQ);
            String repDispNamesQ="select * from prg_ar_report_param_details where report_id="+reportId+" order by dim_id";
            HashMap allRepEl=new HashMap();
            ArrayList display=new ArrayList();
            ArrayList elementIds = new ArrayList();
            HashMap eleNames=new HashMap();
            HashMap memNames=new HashMap();
            for(int u=0;u<repParamObj.getRowCount();u++)
            {
                display.add(repParamObj.getFieldValueString(u,6));
                elementIds.add(repParamObj.getFieldValueString(u,1));
                allRepEl.put(repParamObj.getFieldValueString(u,1),repParamObj.getFieldValueString(u,6));
                memNames.put(repParamObj.getFieldValueString(u,6),repParamObj.getFieldValueString(u,7));
            }
            PbReturnObject repObj=pbdb.execSelectSQL(repDispNamesQ);
            for(int m=0;m<repObj.getRowCount();m++)
             {
                eleNames.put(repObj.getFieldValueString(m,2),repObj.getFieldValueString(m,3));
             }
           // //////////////////////////////////////.println.println(memNames+"=-=- eleDet "+eleDet);
           // //////////////////////////////////////.println.println(" display "+display);
            String allSIds="";
            for(int m=0;m<display.size();m++)
            {
               allSIds=allSIds+",D"+display.get(m).toString();
            }
            if(allSIds.length()>0)
                allSIds=allSIds.substring(1);
           // //////////////////////////////////////.println.println(" allSIds "+allSIds);
           // //////////////////////////////////////.println.println(" eleDet "+eleDet);
            
         %>
         <%
            ReportTemplateDAO dao=new ReportTemplateDAO();
            PbReturnObject pbReport=dao.getReportNamesforFolderId(folderId);
            String allRIdsVal="";

         %>
         <center>
             <Form action=""  name="reportDrillFrm" id="reportDrillFrm">
                 <%if("insights".equalsIgnoreCase(fromPage))
                     {%>

           <div height="50px"> <font style="color:#369;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">Edit Insight Drill</font></div>
          <%}else{%>
<!--                 <div style="height:50px"> <font style="color:#369;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">Edit Report Drill</font></div>-->
                 <%}%>
           <Table  WIDTH="100%" ALIGN="center">
               <Tr STYLE="height:20px">
                        <Td ALIGN="center" WIDTH="50%"style="background-color:#b4d9ee;font-size:12px;font-family:verdana">Member Name</Td>
                        <td align="center" width="100%" style="background-color:#b4d9ee;font-size:12px;font-family:verdana"> Report Drill</td>

                        <Td ALIGN="center" WIDTH="100%" style="background-color:#b4d9ee;font-size:12px;font-family:verdana">Drill To</Td>

               </Tr>
               <Tr>
                    <br>
                </Tr>
               <%
                   

                  for(int y=0;y<display.size();y++){
                String memberId=display.get(y).toString();
               String memId="";
               String dMemId="";
               String rMemId="";
               String eName="";
               String elementId=elementIds.get(y).toString();
               //if(memNames.containsKey(memberId))
                   memId=memberId;
                   dMemId="D"+memberId;//(String)allRepEl.get(memberId);
                   rMemId="R"+memberId;
                   allRIdsVal=allRIdsVal+","+rMemId;
                   String showSelect1="block";
                    String showSelect2="none";
                    String isChecked="";
                    if(typeDet.containsKey(display.get(y).toString())){
                    if(eleDet.containsKey(display.get(y).toString()) && typeDet.get(display.get(y).toString()).toString().equalsIgnoreCase("D"))
                     {
                        showSelect1="block";
                        showSelect2="none";
                        isChecked="";
                        }
                   else if(typeDet.get(display.get(y).toString()).toString().equalsIgnoreCase("R"))
                       {
                       isChecked="checked";
                       showSelect2="block";
                       showSelect1="none";
                   }
                  }
                       

               if(memNames.containsKey(memberId))
                   eName=(String)memNames.get(memberId);
               %>
               <Tr>
                   <Td WIDTH="50%"><Input type="text" readonly STYLE="background-color:white" value="<%=eName%>">
                       
                   </Td>
                   <td width="100%" align="center"><input type="checkbox" name="<%=dMemId%>" <%=isChecked%> onchange="switchToDrill('<%=dMemId%>','<%=rMemId%>')"></td>
                                       <Td WIDTH="50%">
                       
                       <select style="width:100px ; display: <%=showSelect1%>" id="<%=dMemId%>" name="<%=memId%>" elementId="<%=elementId%>">
                           <%

                   
                           for(int h=0;h<display.size();h++){
                               String isSelected="";
                               String memName2="";
                               String childM = "";
                               String childEle="";
                              // if(fromPage=="insights")
                                  // childM=elementIds.get(h).toString();
                               //else
                               childM=display.get(h).toString();
                               String childElementId = elementIds.get(h).toString();
                               ////////////////////////////////////////.println.println(" childM "+childM);
                                if(memNames.containsKey(childM))
                                           {
                                  
                                             if("insights".equalsIgnoreCase(fromPage) && drillMap != null){

                                                  memName2 = (String) eleNames.get(childElementId);
                                                  if(drillMap.containsKey(elementId))
                                                    {
                                                      String chId=drillMap.get(elementId);
                                                      if(chId.equalsIgnoreCase(childElementId))
                                                      isSelected="selected";
                                                    }
                                             }
                                             else{

                                             memName2=(String)memNames.get(childM);

                                             if(eleDet.containsKey(memId))
                                             {
                                                
                                                 ArrayList al=(ArrayList)eleDet.get(memId);
                                                  String chId=al.get(0).toString();
                                                  if(chId.equalsIgnoreCase(childM))
                                                  isSelected="selected";
                                                  if(typeDet.containsKey(memId)){
                                                  if(typeDet.get(memId).toString().equalsIgnoreCase("D")){
                                                        showSelect1="block";
                                                        showSelect2="none";
                                                        }
                                             }
                                                                                                   }
                                             }

                           %>
                           <option value="<%=childM%>" <%=isSelected%> elementId="<%=childElementId%>">
                           <%=memName2%>
                            </option>
                             <% } %>
                            <% }%>
                             </select>
                             <select style="width:100px ; display: <%=showSelect2%>" id="<%=rMemId%>" name="<%=memId%>" elementId="<%=elementId%>">
                            
                            <%
                                
                                
                                for(int i=0;i<pbReport.getRowCount();i++)
                             {
                                    String isSelected="";
                                    String reportNameToDisp=pbReport.getFieldValueString(i,1);
                                    String reportIdToDisp=pbReport.getFieldValueString(i,0);
                                    if(eleDet.containsKey(memId)){
                                    if(eleDet.get(memId).toString().replace("[","").replace("]", "").trim().equalsIgnoreCase(reportIdToDisp))
                                      {
                                        
                                        if(typeDet.get(memId).toString().equalsIgnoreCase("R")){
                                            isSelected="selected";
                                            showSelect2="block";
                                            showSelect1="none";
                                            }


                                       }
                                
                                   }
                             %>
                             
<!--                             reportName=repDet.containsValue(reportIdToDisp)?reportNameToDisp:reportName;
                                 String reportName=pbReport.getFieldValueString(0,"REPORT_NAME");
                                  isSelected=eleDet.containsValue(reportIdToDisp)? "selected":"";
                                String repId=pbReport.getFieldValueString(0,"REPORT_ID");
-->
                                <option value="<%=reportIdToDisp%>" <%=isSelected%> >
                                     <%=reportNameToDisp%>
                           </option>

                         <%}%>
                             </select>
                    </Td>
               </Tr>
               <%} if(allRIdsVal.length() >0) { allRIdsVal = allRIdsVal.substring(1);}%>
           </Table>
           <Br/>
            <Input type="hidden" name="allSIdsVal" id="allSIdsVal" value="<%=allSIds%>">
            <Input type="hidden" name="reportId" id="reportId" value="<%=reportId%>">
            <Input type="hidden" name="userId" id="userId" value="<%=userId%>">
            <input type="hidden" name="allRIdsVal" id="allRIdsVal" value="<%=allRIdsVal%>">
           <Table>
               <Tr><Td>
               <Input type="BUTTON" name="Save" CLASS="navtitle-hover" value="Save" ONCLICK="saveValues()">

               </Td></Tr>
           </Table>
       </Form>
       </center>
            <script type="text/javascript">
        function saveValues()
        {
            var fromPage = '<%=fromPage%>';
             var allSIdsVal = document.reportDrillFrm.allSIdsVal.value;
             var userId=document.reportDrillFrm.userId.value;
             var reportId=document.reportDrillFrm.reportId.value;
             var totalUrl ="";
             var allId = new Array();
             var allDIds=allSIdsVal.split(",");
             var typeUrl="";
             var allRIdsVal=document.reportDrillFrm.allRIdsVal.value;
             var allRids=allRIdsVal.split(",");
             
             for(var i=0;i<allDIds.length;i++)
                 {
                     allId.push(allDIds[i].substr(1));
                 }
//                 alert(allId);
//                 alert('All Rids:'+ allRids);

                if (fromPage == 'insights'){
                    var drillValues = "";
                    for(var m=0;m<allId.length;m++)
                    {
                        var dimId = $("#"+allId[m]).attr("elementId");
                        var childDimId = $("#"+allId[m]+" option:selected").attr("elementId");
                        drillValues = drillValues + ","+dimId+"="+childDimId;
                    }
                    if (drillValues.length > 0)
                        drillValues = drillValues.substr(1, drillValues.length);
                  $.ajax({
                      url:'insights.do?reportBy=saveInsightDrillForUser&drillValues='+drillValues+'&reportId='+reportId+'&userId='+userId,
                      success:function(data){
                        parent.closeDrillDialog();
                      }
                  });
                }
                else{

                    for(var m=0;m<allId.length;m++)
                    {
//                        alert(document.getElementById(allId[m]));
                        var cDId= document.getElementById(allDIds[m]);
                        var cRId=document.getElementById(allRids[m]);
                        var selId="";
                        if(cDId.style.display == 'block' && cRId.style.display == 'none')
                            {
                                 for(var p=0;p<cDId.length;p++)
                                    {
                                        if(cDId[p].selected)
                                        {
//                                            alert('selected Value: '+val[p].value);
//                                            alert(cDId[p].value);
                                            totalUrl = totalUrl+"-"+allId[m]+"="+cDId[p].value;
                                            typeUrl = typeUrl+"-"+allId[m]+"=D";


                                        }
                                    }
                            }
                            if(cRId.style.display == 'block' && cDId.style.display == 'none')
                                {
                                    for(var p=0;p<cRId.length;p++)
                                    {
                                        if(cRId[p].selected)
                                        {
//                                            alert('selected Value: '+cRId[p].value);
                                         totalUrl = totalUrl+"-"+allId[m]+"="+cRId[p].value;
                                         typeUrl = typeUrl+"-"+allId[m]+"=R";

                                        }
                                    }
                                }
                    }
//                    alert('total URL:'+totalUrl);
//                    alert('type Url:'+typeUrl);

                     $.ajax({
                    url: 'reportViewer.do?reportBy=saveReportDrillForUser&totalUrl='+totalUrl+'&reportId='+reportId+'&userId='+userId+'&typeUrl='+typeUrl,
                    success: function(data) {
                        if(data=="false")
                            alert("Drill Not Saved.")
                        else{
                                 alert('Drill is save and would be available after Reset')
                                 parent.saveReportDrillDiv();
                        }
                    }
                });
            }
        }
        function switchToDrill(select1,select2)
        {
//            alert(document.getElementById(select1));
//            alert(select2);
            var slct1=document.getElementById(select1);
            var slct2=document.getElementById(select2);
            slct1.style.display=(slct1.style.display == 'none') ? 'block' : 'none';
            slct2.style.display=(slct2.style.display == 'none') ? 'block' : 'none';
           
        }
     </script>
    </body>
</html>
