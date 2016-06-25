
<%@page  pageEncoding="UTF-8" contentType="text/html" import="java.util.*,java.io.*,utils.db.*,prg.db.PbReturnObject,prg.db.PbDb,com.progen.portal.Portal,com.progen.portal.PortLet,com.google.common.collect.Iterables,com.progen.portal.PortletXMLHelper"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<%String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }


            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String[] day = {"B", "L"};
            String[] days = {"Beg. of Week", "End of Week"};
            String portalTabContent = String.valueOf(request.getAttribute("portalTabContent"));
            String currentTabId = String.valueOf(request.getAttribute("portalTabId"));
            String currentTabName = String.valueOf(request.getAttribute("portalTabName"));
            String currDate = String.valueOf(request.getAttribute("CURRDATE"));
            String periodType = String.valueOf(request.getAttribute("PERIODTYPE"));
            PbDb pbdb = new PbDb();
            String curDate = "";
            String dateCurr = "";
            String value="";
            String valu="";
            String mont="";
            String CurrValue="";
               String ddformT = null;
                if(session.getAttribute("dateFormat")!=null){
                    ddformT = session.getAttribute("dateFormat").toString();
                    }
              
            if (request.getAttribute("CURRDATE") == null || "".equalsIgnoreCase(String.valueOf(request.getAttribute("CURRDATE")))) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    //curDate = "select convert(varchar,getdate(),110)";
                    curDate = "select convert(varchar,isnull(setup_date_value - 0,getdate()),101) as D1 from prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";
                } else {
                    //curDate = "select sysdate from dual";
                    curDate = "select to_char(nvl(setup_date_value - 0,sysdate),'MM/DD/YYYY') as D1 from prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";
                }

                PbReturnObject dateObj = pbdb.execSelectSQL(curDate);
                dateCurr = dateObj.getFieldValueString(0, 0);

                  if(ddformT==null || ddformT.equals("dd/mm/yy")){
                    value=dateCurr;
                     int slashval=value.indexOf("/");
                     int slashLast=value.lastIndexOf("/");
                     valu=value.substring(0, slashval);
                     mont=value.substring(slashval+1, slashLast+1);
                     CurrValue=mont.concat(valu).concat(value.substring(slashLast));
                    dateCurr=CurrValue;
                    }

            } else {
                dateCurr = currDate;
            }
             
            //if(session.getAttribute("dateCurr")!=null){
            //  if(String.valueOf(session.getAttribute("dateCurr")).equalsIgnoreCase(dateCurr)){
            //    
//                    session.setAttribute("dateCurr",dateCurr);
            //              }else{
            //                
//                    session.setAttribute("dateCurr",session.getAttribute("dateCurr"));
            //}
            //}else{
            //  
            session.setAttribute("dateCurr", dateCurr);
            session.setAttribute("periodType", periodType);
            // }
            // 
            // 
            ArrayList<String> alist = new ArrayList<String>();
            alist.add("Month");
            alist.add("Week");
            alist.add("Day");
            alist.add("Qtr");
            alist.add("Year");
            alist.toArray();

            String val="";
            String mon="";
            String CurrValu="";

            if(session.getAttribute("dateFormat")!=null && !ddformT.equals("dd/mm/yy")){

            val=dateCurr.substring(0, 2);
            mon=dateCurr.substring(3, 5);
            CurrValu=mon.concat("/").concat(val).concat(dateCurr.substring(5));
            dateCurr=CurrValu;
            }

          Object[] elements = alist.toArray();
            for(int i=0;i<elements.length;i++){
                
            }                    
     LinkedHashMap<String,ArrayList> reportParameters = new LinkedHashMap<String,ArrayList>();
      String userId=(String) session.getAttribute("USERID");      
         String qry = "select distinct PORTLET_ID from PRG_PORTAL_PORTLETS_ASSIGN where PORTL_ID="+currentTabId;
         PbReturnObject portletIds = pbdb.execSelectSQL(qry);
           List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");             
              List<PortLet> portlets=null;               
                Portal tempportal=null;
            tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(currentTabId)));                                                                                                                    
       String roleIdQry = "SELECT UA.USER_FOLDER_ID,UF.FOLDER_NAME  from PRG_GRP_USER_FOLDER_ASSIGNMENT UA,PRG_USER_FOLDER UF where UA.USER_FOLDER_ID=UF.FOLDER_ID and UA.USER_ID="+userId;
       String roleNames = "";
       PbReturnObject roleIdResult =  pbdb.execSelectSQL(roleIdQry); 
       PbReturnObject roleNameResult; 
       ArrayList roleIdsList = new ArrayList();
       ArrayList roleNamesList = new ArrayList();
       for(int k=0;k<roleIdResult.getRowCount();k++){
           roleIdsList.add(roleIdResult.getFieldValueInt(k, "USER_FOLDER_ID"));           
           roleNamesList.add(roleIdResult.getFieldValueString(k, "FOLDER_NAME"));
       }
       String filterQry = "select FOLDER_ID from PRG_PORTAL_TAB_MASTER where FILTER_FLAG='true' and PORTAL_ID = "+currentTabId;
       PbReturnObject filterQryResult =  pbdb.execSelectSQL(filterQry);
       String filtefolderName="";
       ArrayList filterfoldName=new ArrayList();
       PbReturnObject filtefolderNameResult;
       for(int k=0;k<filterQryResult.getRowCount();k++){
           filterQryResult.getFieldValueString(0, 0);
           filtefolderName = "select FOLDER_NAME from prg_user_folder where FOLDER_ID = "+filterQryResult.getFieldValueString(0, 0);
           filtefolderNameResult =  pbdb.execSelectSQL(filtefolderName);
           filterfoldName.add(filtefolderNameResult.getFieldValueString(0, 0));
       }
       String ruleDim="select ELEMENT_ID from PRG_PORTAL_TAB_MASTER where PORTAL_ID = "+currentTabId;
       PbReturnObject elementId =  pbdb.execSelectSQL(ruleDim);
       String ruleQry="";
       String Value = "";
       for(int i=0;i<elementId.getRowCount();i++){ 
       if(!elementId.getFieldValueString(0, 0).equalsIgnoreCase("All")){
       ruleQry = "select DISP_NAME from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = "+elementId.getFieldValueString(0, 0);
       PbReturnObject dispName =  pbdb.execSelectSQL(ruleQry);
       for(int j=0;j<dispName.getRowCount();j++){
           Value = dispName.getFieldValueString(0, 0);
       }
             }
       } 
       String dimValue = "";
       String dimVal= "select DIM_VALUES from PRG_PORTAL_TAB_MASTER where PORTAL_ID = "+currentTabId;      
       PbReturnObject dimValQry =  pbdb.execSelectSQL(dimVal);
       for(int i=0;i<dimValQry.getRowCount();i++){
           dimValue = dimValQry.getFieldValueString(0, 0);
       }
       String contextPath=request.getContextPath();
%>

<html>
    <head>
        <title></title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>-->
<!--        <script  type="text/javascript"  language="JavaScript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script>-->

        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/pbPortletView.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/pbPortalTabViewerCSS.css"/>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
         <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
        <script type="text/javascript"  src="<%=contextPath%>/tracker/JS/dateSelection.js"></script>
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/TableDisplay/overlib.js"></script>-->

        <style type="text/css">
                #ui-datepicker-div
                {
                  z-index: 9999999;
                }
                .ajaxboxstyle {
                background-color:white;
                border: black;
                height:200px;
                margin:0 0.5em;
                overflow-x:hidden;
                overflow-y:auto;
                overflow:scroll;
                position:absolute;
                text-align:left;
                border-top: 1px groove #848484;
                border-right: 1px inset #999999;
                border-bottom: 1px inset #999999;
                border-left: 1px groove #848484;
                width:200px;
            }
            .myTextbox3{
                background-color:white;
            }
            .floatLeft { width: 50%; float: left; }
            .floatRight {width: 50%; float: right; }
            .container { overflow: hidden; }

        </style>
        <script  type="text/javascript" >
$(document).ready(function(){
                  if('<%=ddformT%>'!='null' ){
                       $('#cdate<%=currentTabId%>').datepicker()
			$("#cdate<%=currentTabId%>").datepicker( "option", "dateFormat", '<%=ddformT%>');
                        }

                 $("#print").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 900,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                  $("#applyPortletcolrdiv").dialog({
                          autoOpen: false,
                          height:350,
                          width: 750,
                          position: 'justify',
                          modal: true
                      }
                  );
                  $("#portalscheduler").dialog({
                          autoOpen: false,
                          height:400,
                          width: 650,
                          position: 'justify',
                          modal: true
                      }
                  );
                  $("#zoomer").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 900,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                $('#cdate'+<%=currentTabId%>).datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $("#sDatepicker").datepicker({

                 changeMonth: true,
                 changeYear: true
                 });
             $("#eDatepicker").datepicker({

                 changeMonth: true,
                 changeYear: true
                 });
            });  
            </script>
       


    </head>
    <body>

        <form action=""  name="myForm" method="post">
            <input type="hidden" name="currportletId" id="currportletId" value="">
            <input type="hidden" name="dateCurr" id="dateCurr" value="<%=currDate%>">
            <table  style="height:100%;min-width:1350px;max-width:100%">
                <tr valign="top">
                    <td>
                        <table style="width: 65%;" align="left">
                            <tr style="width: 50%;">
                                <td style="width: 2%;">
                                    <a href='javascript:void(0)' class="ui-icon ui-icon-plus" onclick="goPortlet('<%=currentTabId%>')" title="Add Portlet" style="color:#369;text-decoration:none"></a></td>
                                
                                <td style="width: 4%;">
                                    <input type="text" id="cdate<%=currentTabId%>" NAME="cdate<%=currentTabId%>" title="Date" style="width:90px; border:medium hidden;color:#369;" value="<%=dateCurr%>">
                                </td>
                               
                                <td style="width: 2%;"><select id="CBO_PRG_PERIOD_TYPE<%=currentTabId%>" title="Duration"  name="CBO_PRG_PERIOD_TYPE<%=currentTabId%>" class="myTextbox3" >
                                        <% for(int i=0;i<alist.size();i++){
                                            if(periodType!=null)                                       
                                            if(periodType.equalsIgnoreCase(alist.get(i))){%>
                                               <option selected value='<%=alist.get(i)%>'><%=alist.get(i)%></option>
                                        <%}else{%>
                                        <option value='<%=alist.get(i)%>'><%=alist.get(i)%></option>
                                        <%}}%>
<!--                                        <option > Day </option>
                                        <option> Week </option>
                                        <option selected value="Month"> Month </option>
                                        <option> Qtr </option>
                                        <option> Year </option>-->
                                    </select> </td>
                                <td style="width: 70%;">
                                    &nbsp;<input type="button" id="Go" NAME="Go" class="navtitle-hover" value="Go" onclick="getDate()">&nbsp;
                                </td>
                                
<!--                               <td  align="right">
                                    Role:</td>
                               <td align="right"><select id="BusinessRole" class="myTextbox3" onchange="getDimensions()" >
                                       <option value="select">--select--</option>
                                <%--        <% for(int i=0;i<roleIdsList.size();i++){  
                                        %>                                         
                                        <option value='<%=roleIdsList.get(i)%>' ><%=roleNamesList.get(i)%></option>                                        
                                        <%}%>   --%>
                                    </select> </td>
                               <td  align="right">
                                    Parameters:</td>
                               <td align="right"><select id="SelectedMeasure" class="myTextbox3" onchange="getMeasures()" > 
                                       <option value="select">--select--</option>
                                    </select> </td>
                                <td  align="center" > Value:</td>
                                <td align="right"><select id="DimName"  name="DimName" class="myTextbox3" > 
                                        <option value="select">--select--</option>
                                    </select> </td>
                                    <td align="right">
                                    <input type="button" id="Go" NAME="Go" value="Go" onclick="applyFilter()" >&nbsp;
                                    </td>
                                <td align="right">
                                    <input type="button" id="SaveFilter" NAME="SaveFilter" value="Save" onclick="saveFilter()" >
                                </td>                                                               -->                                                            
                            </tr>

                        </table>                            
                                    <table style="width: 15%;" align="left">
                                        <tr style="width: 15%;">
                        <% if(Value.equalsIgnoreCase("")){%>
                        <td id="dimChangeVal" style="width: 10%"></td>
                               <% }else{%>
                                    <td id="dimChangeVal" style="width: 10%"><%=Value%></td>
                               <% } %>  
                               <td style="width: 1%">
                                   <% if(dimValue.equalsIgnoreCase("All")){%>
                                   <input type="text" id="paramTextId" >                                   
                               <% }else{%>
                                   <input type="text" id="paramTextId" value="<%=dimValQry.getFieldValueString(0, 0)%>" >
                               <% } %>
                                   <div id="paramVals" class="ajaxboxstyle" style="display: none;overflow: auto;">
                                                    <table id="paramDivVals"></table>
                                                </div></td>
                                                <td ><img src="<%=request.getContextPath()%>/images/include.png" onclick="paramValues()"/></td>
                               <td style="width: 1%">
                                   <input type="button" id="Go" NAME="Go" value="Go" class="navtitle-hover" title="Apply Filter" onclick="applyFilter()" >
                                    </td>
                               <td style="width: 1%">
                                   <input type="button" id="SaveFilter" NAME="SaveFilter" class="navtitle-hover" title="Save Filter" value="Save" onclick="saveFilter()" >
                                </td>
                            <td style="width: 1%">                                
                                <a href="javascript:void(0);" class="ui-icon ui-icon-star" onclick="selectValues()" title="Parameters" style="text-decoration:none;" ></a>
                            </td>
                            <td style="width: 1%"> <a href="javascript:void(0);" onclick="resetGlobalFilter('<%=currentTabId%>')" class="ui-icon ui-icon-refresh" title="Reset Filter" style="text-decoration:none;" ></a></td>
                         <td style="width: 1%"> <a href="javascript:void(0);" onclick="printPortal()" title="Export to PDF"  class="ui-icon ui-icon-document" style="text-decoration:none;" id="printPortal<%=currentTabId%>"></a></td>
<!--                               <td align="right"> <a href="javascript:void(0);" title="Schedule Portal" onclick="schedulePortal()" style="text-decoration:none;" ><img src="icons pinvoke/mail.png"></a></td>-->
                               <td style="width: 1%"> <a href="javascript:void(0);" onclick="deleteEmptyPortlet('<%=currentTabId%>')" class="ui-icon ui-icon-trash" title="Delete Portlet" style="text-decoration:none;" ></a></td>                               
                                        </tr>
                        </table>
                               </td>
                </tr>
                <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
                <tr valign="top">
                    <td valign="top" width="100%">
                        <div id="divPortlet"  style="width:1350px;height:100%;">
                            <%if (portalTabContent != null && !"".equalsIgnoreCase(portalTabContent)) {%>
                            <%=portalTabContent%>
                            <%} else {%>
                            <div id="PortalColumn1_<%=currentTabId%>" class="column"></div>
                            <div id="PortalColumn2_<%=currentTabId%>" class="column"></div>
                            <div id="PortalColumn3_<%=currentTabId%>" class="column"></div>
                            <div id="PortalColumn4_<%=currentTabId%>" class="column"  style="width:0px"></div>
                            <%}%>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <div id="portlet<%=currentTabId%>" class="createPortlet" style="display:none" title="Add Portlet">
            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                <tr valign="top" >
                    <td valign="top" class="myHead" style="width:30%">Portlet Name</td>
                    <td valign="top" style="width:70%">
                        <input type="text" maxlength="35" name="portletName<%=currentTabId%>" style="width:80%" id="portletName<%=currentTabId%>" onkeyup="tabmsgportlet('<%=currentTabId%>')" ><br>
                    </td>
                </tr>
                <tr valign="top"  style="height:2px">
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr valign="top" >
                    <td  valign="top" class="myHead" style="width:30%">Description</td>
                    <td valign="top" style="width:70%">
                        <textarea cols="" rows=""  name="porletDesc<%=currentTabId%>" id="portletDesc<%=currentTabId%>" style="width:80%"></textarea>
                    </td>
                </tr>
                <tr valign="top"  style="height:2px">
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr valign="top" >
                    <td valign="top"   colspan="2" align="center">
                        <input type="button" class="navtitle-hover" style="width:auto;" value="Create" id="portletsave" onclick="savePortlet('<%=currentTabId%>')">&nbsp;
                        <%--<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelPortlet('<%=currentTabId%>')">--%>
                    </td>
                </tr>
            </table>
        </div>


        <!-- code to edit portlet name-->
        <div id="editportlet<%=currentTabId%>" style="display:none" title="Edit Portlet Name" >
            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                <tr valign="top" >
                    <td valign="top" class="myHead" style="width:30%">Portlet Name</td>
                    <td valign="top" style="width:70%">
                        <input type="text" maxlength="35" name="editportletName<%=currentTabId%>" style="width:80%" id="editportletName<%=currentTabId%>" onkeyup="edittabmsgportlet('<%=currentTabId%>')" ><br>
                    </td>
                </tr>
                <tr valign="top"  style="height:2px">
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr valign="top" >
                    <td  valign="top" class="myHead" style="width:30%">Description</td>
                    <td valign="top" style="width:70%">
                        <textarea cols="" rows=""  name="editporletDesc<%=currentTabId%>" id="editportletDesc<%=currentTabId%>" style="width:80%"></textarea>
                    </td>
                </tr>
                <tr valign="top" >
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr valign="top" align="center">
                    <td valign="top"   colspan="2" align="center">
                        <input type="button" class="navtitle-hover" style="width:auto" value="Update" id="portletsave" onclick="updatePortletName('<%=currentTabId%>','<%=dateCurr%>')">&nbsp;
                        <%-- <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelEditPortlet('<%=currentTabId%>')">--%>
                    </td>
                </tr>
            </table>
        </div>
        <!-- end of code to edit portlet name-->
        <div>
            <iframe  id="portletGraphFrame" NAME='portletGraphFrame'  class="white_content"  STYLE='display:none;width:100%;height:100%;top:0%;left:0%;overflow:scroll' SRC=''></iframe>
        </div>
        <div>
            <iframe  id="portletTableFrame" NAME='portletTableFrame'     class="white_content" SRC='' STYLE='display:none;width:100%;height:100%;top:0%;left:0%;overflow:scroll'></iframe>
        </div>
        <div id="drilldownSuperdiv-<%=currentTabId%>">

        </div>
        <div id="zoomer" style="display:none;"  title="Graph">
        </div>
        <div id="print" style="display:none;" >
         <IFRAME NAME="dFrame" id="dFrame" STYLE="display:none;width:0px;height:0px"  frameborder="0"></IFRAME>
        </div>

          <div id="applyPortletcolrdiv" title="Apply Color Based Grouping" style="display:none">
              <iframe id="applyPortletcolorframe" name="applyPortletcolorframe" frameborder="0" marginheight="0" marginwidth="0" src='#' width="100%" height="100%"></iframe>
           </div>
        <div id="portalscheduler" style="display:none" title="Portal Schedule">
            <form action=""  name="portalSchduleForm" id="portalSchduleForm" method="post">
                    <table width="66%" align="center">
                        <Tr>
                            <Td class="myhead"><span style="color:red">*</span>Scheduler Name </Td>
                            <Td><Input type="text" name="schdPortalName" id="schdPortalName" maxlength=100  style="width:160px" value="<%=currentTabName%>" readonly="" >
                            </Td>

                        </Tr>
                        <Tr>

                            <Td class="myhead"><span style="color:red">*</span>Start Date</Td>
                            <Td width="30%"><Input type="text" name="startdate" id="sDatepicker" maxlength=100  style="width:160px" value="" >
                            </Td>
                            <td width="20%"></td>

                        </Tr>
                        <Tr>

                            <Td class="myhead"><span style="color:red">*</span>End Date</Td>
                            <Td><Input type="text" name="enddate" id="eDatepicker" maxlength=100  style="width:160px" value="">
                            </Td>
                            <td width="20%"></td>

                        </Tr>

                        <tr>
                            <Td class="myhead" width="40%">Frequency</Td>
                            <Td width="10%">
                                <select name="frequency" id="frequency" class="myTextbox5" onchange="addDate(this)" style="width:120px">
                                    <Option value="1">Daily</Option>
                                    <Option value="2">Monthly</Option>
                                    <%--<Option value="3">Weekly</Option>--%>


                                </select>
                            </Td>
                            <td>
                                <div id="onlyDateSelect" style="display:none" >
                                    Day
                                    <select name="monthDate" id="monthDate" onchange="addDate(this)">
                                        <option value='L'>EOM</option>
                                        <option value='B'>BOM</option>
                                        <%for (int i = 1; i <= 31; i++) {%>
                                        <option value='<%=i%>'><%=i%></option>
                                        <%}%>

                                    </select>
                                </div>
                            </td>
                            <td>
                                <div id="dayOfWeek" style="display:none;width: auto">
                                    <select name="alertDay" id="alertDay">
                                        <%for (int i = 1; i <= days.length; i++) {%>
                                        <option value='<%=day[i - 1]%>'><%=days[i - 1]%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </td>
                              <td>
                                <div id="timeselect" style="display: none">
                                    <td>Hrs</td>
                                    <td>
                                        <select name="hrs" id="hrs" >
                                            <%for (int i = 00; i < 24; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                    <td>Min</td>
                                    <td>
                                        <select name="mins" id="mins">
                                            <%for (int i = 00; i < 60; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table id="dataSelection" width="66%" align="center"><tr>
                                        <td class="myhead" width="40%"> Data   </td>
                                        <td id="dailyDataTD">
                                            <select id="dailyData" name="dailyData">
                                                <option value="current">Current Day</option>
                                                <option value="last">Last Day</option>
                                            </select>
                                        </td>
                                        <td id="monthlyDataTD" style="display:none;">
                                            <select id="monthlyData" name="monthlyData">
                                                <option value="last">Last Month</option>
                                                <option value="mtd">MTD</option>
                                                <option value="both">Last Month + MTD</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>

                    <table style="width:70%;" id="emailDiv">
                        <tr> <td colspan='2' style='height:20px'></td></tr>
                        <tr>
                            <td></td>
                            <td  style="text-align: center" width="10%"><font style="font-size:small;" >Email Id</font></td>
                        </tr>
                        <tbody id="emailDiv">
                                <tr id="row2">
                                <Td  class="myhead" width="40%">MAILTO</Td>
                                <td width="10%">
                                    <input type="text" id="2mail" name="mail" style="width: 225px">
                                </td>
                                <Td width="2px">
                                    <IMG ALIGN="middle" onclick='addDimEmailRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                                </Td>
                                <Td>
                                    <IMG ALIGN="middle" onclick='deleteDimEmailRow("row2")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                </Td>
                            </tr>
                        </tbody>
                    </table>
                    <table style='width:100%' align='left'>
                        <tr>
                            <td colspan='2' style='height:15px'></td>
                        </tr>
                        <tr>
                            <td id="saveButton" colspan='' align='right' width="40%"><input type='button' class='navtitle-hover' style='width:30%' value='Save' onclick='savePortalSchedule("false")'></td>
                            <td id="runButton" width="55%"><input type='button' class='navtitle-hover'  style='width:40%' value='Run/Execute' onclick='savePortalSchedule("true")'></td>
                         </tr>
                    </table>
            </form>
                                <div id="deleteEmptyPortlet" title="Portlet Names" >
                                    <table id="delEmpPortlet">                                        
                                    </table>
                                </div>
                                <div id="selectParams" title="Parameters" >
                                    <table id="selParams">
                                        <tr></tr>
                                        <tr>
                                            <td>Business Role:</td>
                                            <td align="left"><select id="BusinessRole" class="myTextbox3" onchange="getDimensions()" >
                                       <option value="select">--select--</option>
                                        <% for(int i=0;i<roleIdsList.size();i++){  
                                        %>                                         
                                        <option value='<%=roleIdsList.get(i)%>' ><%=roleNamesList.get(i)%></option>                                        
                                        <%}%>   
                                    </select> </td>
                                        </tr>
                                        <tr>
                                            <td>Parameter:</td>
                                             <td align="left"><select id="SelectedMeasure" class="myTextbox3" onchange="getMeasures()" > 
                                       <option value="select">--select--</option>
                                    </select> </td>
                                        </tr>
<!--                                        <tr>
                                            <td onclick="closeDiv()">Value:</td>                                                                                    
                                            <td align="left"><input type="text" id="paramTextId" onclick="closeDiv()" onmouseup="paramValues()"><div id="paramVals" class="ajaxboxstyle" style="display: none;overflow: auto;">
                                                    <table id="paramDivVals"></table>
                                                </div></td></tr>-->
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                   <tr> <td align="center">
                                           <input type="button" id="Go" NAME="Go" value="Go" class="navtitle-hover" onclick="getFiltervals()" >
                                    </td>
                                </tr>
                                    </table>
                                </div>
        </div>  
                                     <script  type="text/javascript" >
<%--            var selectedreportId="";
            var drillReportId="";
            var portlId="";--%>
                var jsonVar;
                var dimJsonVar;
                     
//            $("#deleteEmptyPortlet").dialog({
//                          autoOpen: false,
//                          height:350,
//                          width: 450,
//                          position: 'justify',
//                          modal: true
//                      }
//                  );
            $("#selectParams").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
            $('.column').sortable({
                connectWith: '.column',
                helper:"clone",
                effect:["", "fade"],
                stop: function(event, ui) {
                    var str=$('#PortalColumn1_<%=currentTabId%>').sortable('toArray')+";"+$('#PortalColumn2_<%=currentTabId%>').sortable('toArray')+";"+$('#PortalColumn3_<%=currentTabId%>').sortable('toArray');
                    $.ajax({
                        url: 'portalViewer.do?portalBy=savePortletOrder&portletIds='+str+'&tabId=<%=currentTabId%>',
                        success: function(data){
                        }
                    });

                }
            });
//            $(".tablesorter1").columnFilters().tablesorter({
//                widthFixed: true,
//                widgets: ['zebra']
//            }).tablesorterPager({
//                container: $(".pager")
//            });
             <%--$("#tablesorter116-341").tablesorter({headers : {0:{sorter:false}}});--%>

            function getDate(){
                var tabid='<%=currentTabId%>';
                var tabname='<%=currentTabName%>';
                //alert(tabname);
                var datenow=document.getElementById("cdate"+tabid).value;
                var periodType = document.getElementById("CBO_PRG_PERIOD_TYPE"+tabid).value;
//                for(var i=0;i<portletids.length;i++){
//                    alert('in for '+portletids[i])
                     $.ajax({
                        url: 'portalViewer.do?portalBy=viewPortalTab&TABID='+tabid+'&TABNAME='+tabname+'&currDate='+datenow+'&periodType='+periodType,
//                        url: "portalViewer.do?portalBy=viewPortlet&PORTLETID="+portletids[i]+"&REP=''&CEP=''&perBy=Top-10&gpType=''&portalTabId="+tabid,
//                        target:'#'+tabname,
                        success: function(data){
                            if(data!=""){
                                document.getElementById("dateCurr").value=datenow;
                                document.getElementById("periodType").value=periodType;
//                                document.forms.myForm.action="portalViewer.do?portalBy=viewPortalTab&TABID="+tabid+"&TABNAME="+tabname;
                                document.forms.myForm.submit();
                              document.forms.frmParameter.action="pbPortalViewer.jsp#"+jQuery.trim(tabname.replace(" ","_","gi"))
                              document.forms.frmParameter.submit()
                            }
                        }
                });
            }

            //            }
            function prstDate(){
                document.getElementById("cdate<%=currentTabId%>").value='<%=dateCurr%>';
                document.getElementById("CBO_PRG_PERIOD_TYPE<%=currentTabId%>").value='<%=periodType%>';
            }

    function EXTTable(portletId,dispdiv,selectId,portalTabId,date){
             var tabid='<%=currentTabId%>';
             var tabname='<%=currentTabName%>';
             var selectId=selectId.value;
            dispdiv.style.display='none';
     $.post('portalViewer.do?portalBy=extendTablePortlet&portletId='+portletId+'&portalTabId='+portalTabId+'&selectId='+selectId,function(data){
//                    document.forms.frmParameter.action="pbPortalViewer.jsp#"+jQuery.trim(tabname.replace(" ","_","gi"))
//                     document.forms.frmParameter.submit()
                var divID="#portlet-"+portalTabId+"-"+portletId
               if(selectId==1)
               $(divID).height(420)
               else if(selectId==2)
                $(divID).height(700)
              else
                $(divID).height(1050)


     });
      }
function zoomer(divid,name)
        {
            var tempid =divid.replace("zoom","","gi").split("-")
            closeOpenDivs(tempid[1],tempid[0])
            $("#zoomer").data('title.dialog',name)
            $("#zoomer").dialog('open');
            document.getElementById("zoomer").innerHTML=document.getElementById(divid).innerHTML;
            var divObj = document.getElementById("zoomer");
            var imgObjs = divObj.getElementsByTagName("img")
            imgObjs[0].style.width = '700px';
            imgObjs[0].style.height = '500px';
           }
function printPortal()
{

   var source ="<%=request.getContextPath()%>/PortalsDownload.jsp?portalTabId=<%=currentTabId%>";
    var dSrc = document.getElementById("dFrame");
    dSrc.src = source;

}

          function applyColor(portletId){                                               //fordashboardTableColor

                      if(document.getElementById("colordivId"+portletId).style.display=='none'){
                        $("#colordivId"+portletId).show();
                    }else{
                        $("#colordivId"+portletId).hide();
                    }

                }
                function applyPortletcolor(measVal,portalTabId,portletId){
                           var mesval=$("select#"+measVal).val();
                           var mesarry= new Array();
                              mesarry=mesval.split(",");
                              var columnName=mesarry[0];
                              var disColumnName=mesarry[1];
                              var labelName=mesarry[2];
                             // alert("columnName\t:"+columnName+"\tdisColumnName\t:"+disColumnName+"\treportId\t:"+portalTabId);
                       var frameObj = document.getElementById("applyPortletcolorframe");
                          frameObj.src= "PbPortalColor.jsp?columnname="+columnName+"&dispcolumname="+disColumnName+"&portalId="+portalTabId+"&portletId="+portletId+"&labelName="+labelName+"&fromModule=Portal";
                        $("#colordivId"+portletId).hide();
                       $("#applyPortletcolrdiv").dialog('open');
                     }
               function schedulePortal(){

                     $("#frequency").val("");
                     $("#sDatepicker").val("");
                     $("#eDatepicker").val("");
                     $("#2mail").val("");
                   var table = document.getElementById("emailDiv");
                        var rowCount = table.rows.length;
                    for(j=rowCount;j>3;j--){
                                table.deleteRow(idx);
                            idx--;
                    }
                   $("#portalscheduler").dialog('open');
                     }
              function addDimEmailRow(){
                    var table=document.getElementById("emailDiv")
                    var rowCount = table.rows.length;
                    idx = rowCount ;
                    var row = table.insertRow(rowCount);
                    row.id="row"+idx;
                    var tdhtml="<td   class='myhead' width='13%'>";
                    tdhtml+="MAILTO";
                    tdhtml+="</td><td width='10p%'><input type='text' id=\""+idx+"mail\" name='mail' style='width: 225px'> </td> <td width='1%'>";
                    tdhtml+="<img  align='middle' SRC='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' BORDER='0' ALT=''  onclick='addDimEmailRow()' title='Add Row' /></td>";
                    tdhtml+="<td width='2px'><img  align='middle' title='Delete Row' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' BORDER='0' ALT=''  onclick=\" deleteDimEmailRow('"+row.id+"')\"  /></td>";
                    row.innerHTML=tdhtml;
                }
                function deleteDimEmailRow(rowId)
                {
                    var rowId=rowId.substr(3);
                    try {
                        var table = document.getElementById("emailDiv");
                        var rowCount = table.rows.length;
                        if(rowCount > 3) {
                            table.deleteRow(rowId);
                            idx--;
                        }
                        else{
                            alert("You cannot delete all the rows");
                        }
                    }catch(e) {
                        //                alert(e);
                    }
                }
                function savePortalSchedule(runFlag){
                   var stDATE = $("#sDatepicker").val();
                    var edDATE = $("#eDatepicker").val();
                     $("#portalscheduler").dialog('close');

                        $.post('<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear='+stDATE+'&endYear='+edDATE+'&scheduler='+"scheduler",
                     function(data){
                            if(data=='true')
                            {
                    if(runFlag=="true"){
                        //               $("#loading").show();
                        $.post("portalViewer.do?portalBy=runPortalScheduler&portalTabId="+<%=currentTabId%>,$("#portalSchduleForm").serialize(),
                        function(data){
   //                                      $("#loading").hide();
                            alert("Portal has been sent Successfully")
                        });
                    }
                    else{
                        $.post("portalViewer.do?portalBy=savePortalScheduler&portalTabId="+<%=currentTabId%>,$("#portalSchduleForm").serialize(),
                        function(data) {
                            alert("Portal has been saved Successfully ")
                        });
                    }

                    }else
                    {
                                alert("Please select End Year correctly.")
                    }
                    });
            }
            function deletePortlesfromExt(crtPortId){
                var divId="#deleteEmptyPortlet"
                $(divId).dialog({
                    autoOpen: false,
                    height:350,
                    width: 450,
                    position: 'justify',
                    modal: true
                });                
                $.ajax({
                      url: '<%=request.getContextPath()%>/portalViewer.do?portalBy=deleteEmptyPortlet&portalTabId='+crtPortId,
                      success: function(data){
                    jsonVar = eval('('+data+')')
                    var keys = [];
                    var htmlVar="" 
                    var jsonvalues;
                        for (var key in jsonVar) {
                            if (jsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        htmlVar += "<tr valign='top'><td style='color:black;'><span>Portlet Names are appearing in the Vertical order of portlets</span></td></tr>&nbsp;"
                        for(var i=0;i<keys.length;i++)
                        {
                           jsonvalues = jsonVar[keys[i]];                           
                           htmlVar += "<tr><td id='"+jsonvalues.toString().replace(" ","_","gi")+"'>"+jsonvalues+"</td><td><input class='"+keys[i]+"' id='"+keys[i]+"' type='checkbox' value='"+jsonvalues+"'></td></tr>";
                        }
                        htmlVar += "<tr><td><input type='button' name='Done' class='navtitle-hover' value='Done' onclick='deleteSelPortlet("+crtPortId+")' ></td></tr>"
                        $("#delEmpPortlet").html(htmlVar);
                        $(divId).dialog('open');
                      }
                });                 
            }
            function deleteSelPortlet(crtPortId){                
                var keys = [];
                        for (var key in jsonVar) {
                            if (jsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        } 
                 var portletIds = new Array 
                 var portletNames = new Array 
                 for(var i=0;i<keys.length;i++){                     
                           $("#"+keys[i]).each(function(index){
                               if(this.checked){
                                   portletIds.push(this.id)
                                   portletNames.push(this.value)
                               }                               
                           });                    
                 }  
                 $.ajax({
                      url: '<%=request.getContextPath()%>/portalViewer.do?portalBy=deleteSelectedPortlet&portletIds='+portletIds+'&portletNames='+portletNames+'&portalTabId='+crtPortId,
                      success: function(data){
                                if(data=='true'){                                    
                                    parent.document.forms.frmParameter.action="pbPortalViewer.jsp#";
                                    parent.document.forms.frmParameter.submit();
                                    alert("Portlets deleted successfully")
                                }else{
                                    alert("Portlets not deleted")
                                }
                          }
                   });
                   $("#deleteEmptyPortlet").dialog('close');
            }
            function getMeasures(){    
               var elementID = $("#SelectedMeasure").val();               
                var contextPathVar='<%=request.getContextPath() %>'
                $.post("portalViewer.do?portalBy=getDimensionsForFilter&elementID="+elementID+"&path="+contextPathVar,function(data){                         
                    var jsonVar=eval('('+data+')');
                    var jsonvaleuse="";
                    var htmlVar="";
                    var dimensionNames = jsonVar.dimNames;
                    for(var i=0;i<dimensionNames.length;i++)
                        {
                            jsonvaleuse=dimensionNames[i]
                            htmlVar+="<tr><td id='"+dimensionNames[i]+"' onmouseout=delDimColor('"+dimensionNames[i]+"') onmouseover=getDimBColor('"+dimensionNames[i]+"') onclick=getDimNames('"+jsonvaleuse.toString().replace(" ", "_", "gi")+"') >"+jsonvaleuse+"</td></tr>"
                        } 
                        htmlVar+="<tr><td id='All' onmouseout=delDimColor('All') onmouseover=getDimBColor('All') onclick=getDimNames('All')>All</td></tr>";
                        $("#paramVals").html(htmlVar);
                });
            }
            function getDimBColor(id){
                $("#"+id).css({'background-color':'#369'});

            }
            function delDimColor(id){
                $("#"+id).css({'background-color':'white'});
            }
            function getDimNames(name){ 
                var nameList = name.toString().replace("_", " ", "gi");
                $("#paramTextId").val();
                $("#paramTextId").attr("value",function(i, val) {
                    if(val=="All"){
                        $("#paramVals").hide();                   
                    return nameList;
                     
                    }                
                 else if(val!=""&&val!=""){
                     $("#paramVals").hide();
                    return val+','+nameList;
                 }
                else{
                    $("#paramVals").hide();
                    return nameList;
                }                
                });
                $("#dimVal").attr("value",$("#paramTextId").val());
            }
            function applyFilter(){
                $("#selectParams").dialog('close');
                var dimensionName = $("#paramTextId").val();
                var elementID = $("#SelectedMeasure").val();
                var folderId = $("#BusinessRole").val();                
                <%                                       
                for(int i=0;i<portletIds.getRowCount();i++){                                
                                
              portlets=tempportal.getPortlets();                      %>                     
             var rowEdgeParams="";
        var colEdgeParams="";        
        var rowParamIdObj=document.getElementsByName('chkREP-" + <%=Integer.parseInt(portletIds.getFieldValueString(i, 0))%> + "-" + <%=currentTabId%> + "');
        var columnParmObject=''
        var CEPNames=''
        var REPNames='' 
        var gpType=''
                    
        if(rowParamIdObj!=null){

         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+<%=Integer.parseInt(portletIds.getFieldValueString(i, 0))%>+"-"+<%=currentTabId%>)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
        }
     <%   } %>
                $.post("portalViewer.do?portalBy=applyDimensionFilter&elementID="+elementID+"&dimensionName="+dimensionName+"&currentTabId="+<%=currentTabId%>+"&folderId="+folderId,function(data){
                     <% for(int j=0;j<portletIds.getRowCount();j++){                                
                                
              portlets=tempportal.getPortlets(); %> 
                    getPortletDetails(<%=Integer.parseInt(portletIds.getFieldValueString(j, 0))%>, rowEdgeParams, colEdgeParams,"","",<%=currentTabId%>,"");                                        
                   <% }%>
                });                
            }  
            function getDimensions(){                
                var htmlVar="";
                var jsonvalues = []; 
                var keys = [];
                var folderId = $("#BusinessRole").val();
                $.post("portalViewer.do?portalBy=getDimensionBasedOnRule&folderId="+folderId,function(data){
                    dimJsonVar=eval('('+data+')')                                                
                    for (var key in dimJsonVar) {
                            if (dimJsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }                         
                        for(var i=0;i<keys.length;i++)
                        {                            
                            jsonvalues = dimJsonVar[keys[i]];
                            htmlVar+="<option value='"+keys[i]+"'>"+jsonvalues+"</option>"
                        }
                        $("#SelectedMeasure").html(htmlVar);                        
                });
            }
            function saveFilter(){
                $("#selectParams").dialog('close');
                var BusinessRole = $("#BusinessRole").val();
                var SelectedMeasure = $("#SelectedMeasure").val();
                var DimName = $("#paramTextId").val();                
                $.post("portalViewer.do?portalBy=saveFilterInPortal&BusinessRole="+BusinessRole+"&SelectedMeasure="+SelectedMeasure+"&DimName="+DimName+"&currentTabId="+<%=currentTabId%>+"&userId="+<%=userId%>,function(data){
                    if(data=="1"){
                        alert("Portal Filter has been saved successfully");
                    }
                });
            }
//            for (var key in dimJsonVar) {
//                            if (dimJsonVar.hasOwnProperty(key)) {
//                                keys.push(key);
//                            }
//                        }                         
//                        for(var i=0;i<keys.length;i++)
//                        {                            
//                            jsonvalues = dimJsonVar[keys[i]];
//                        } 
//                        $("#BusinessRole").val();
        function selectValues(){                
//            var htmlVar="";
//            htmlVar+="<tr></tr>";
//            htmlVar+="<tr></tr>";
//            htmlVar+="<tr><td>Business Role:</td><td><input type='text'></td></tr>";
//            htmlVar+="<tr><td>Parameter:</td><td><input type='text'></td></tr>";
//            htmlVar+="<tr><td>Value:</td><td><input type='text'></td></tr>";
//            htmlVar += "<tr><td><input type='button' name='Done' class='navtitle-hover' value='Done' onclick='deleteSelPortlet("+crtPortId+")' ></td></tr>"
//            $("#selParams").html(htmlVar);
            $("#selectParams").dialog('open');
        }
        function paramValues(){             
            
           if(document.getElementById("paramVals").style.display=='none'){
               <% if(!elementId.getFieldValueString(0, 0).equalsIgnoreCase("All")){ %>
               var elmentID;
               <% if(elementId.getFieldValueString(0, 0).equalsIgnoreCase("All") && elementId ==null){ %>
               elmentID = $("#SelectedMeasure").val(); 
               <%  }else{%>
               elmentID = <%=elementId.getFieldValueString(0, 0)%>
                   <%  }%>
                var contextPathVar='<%=request.getContextPath() %>'
                $.post("portalViewer.do?portalBy=getDimensionsForFilter&elementID="+elmentID+"&path="+contextPathVar,function(data){                         
                    var jsonVar=eval('('+data+')');
                    var jsonvaleuse="";
                    var htmlVar="";
                    var dimensionNames = jsonVar.dimNames;
                    for(var i=0;i<dimensionNames.length;i++)
                        {
                            jsonvaleuse=dimensionNames[i]
                            htmlVar+="<tr><td id='"+dimensionNames[i]+"' onmouseout=delDimColor('"+dimensionNames[i]+"') onmouseover=getDimBColor('"+dimensionNames[i]+"') onclick=getDimNames('"+jsonvaleuse.toString().replace(" ", "_", "gi")+"') >"+jsonvaleuse+"</td></tr>"
                        } 
                        htmlVar+="<tr><td id='All' onmouseout=delDimColor('All') onmouseover=getDimBColor('All') onclick=getDimNames('All')>All</td></tr>";
                        $("#paramVals").html(htmlVar);
                });
                <%  }%>
             $("#paramVals").show();  
// $("#paramTextId").val('');  
        }
         else{
             $("#paramVals").hide();    
         }
        }
        function closeDiv(){
            $('.ajaxboxstyle').hide();  
        }
        function getFiltervals(){
            var elementID = $("#SelectedMeasure").val();
            $.post("portalViewer.do?portalBy=getFiltervals&elementID="+elementID,function(data){
                if(data!=null){                                       
                    document.getElementById("dimChangeVal").innerHTML = data.replace(" ","","gi");
                    $("#selectParams").dialog('close');
                    $("#paramTextId").attr("value","All");
                }
            });
        }
        function resetGlobalFilter(portalTabId){
            var select = 'select'
            var folderId = $("#BusinessRole").val();
            var fId = folderId;
            if(fId == select)
                folderId = <%=filterQryResult.getFieldValueString(0, 0)%>                         
            $("#paramTextId").attr("value","");
            $("#SelectedMeasure").attr("value","");
            $("#BusinessRole").attr("value","");
            $("#paramTextId").attr("value","");
            document.getElementById("dimChangeVal").innerHTML = "";
//            var dimensionName = $("#paramTextId").val();
//                var elementID = $("#SelectedMeasure").val();
//                var folderId = $("#BusinessRole").val();
           <%                                    
                for(int i=0;i<portletIds.getRowCount();i++){
                session.setAttribute("globalFilter", "true");
              portlets=tempportal.getPortlets();                      %>                     
             var rowEdgeParams="";
        var colEdgeParams="";        
        var rowParamIdObj=document.getElementsByName('chkREP-" + <%=Integer.parseInt(portletIds.getFieldValueString(i, 0))%> + "-" + <%=currentTabId%> + "');
        var columnParmObject=''
        var CEPNames=''
        var REPNames='' 
        var gpType=''
                    
        if(rowParamIdObj!=null){

         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+<%=Integer.parseInt(portletIds.getFieldValueString(i, 0))%>+"-"+<%=currentTabId%>)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
        }
     <%   } %>                  
            
            $.post("portalViewer.do?portalBy=resetGlobalFilter&currentTabId="+portalTabId+"&userId="+<%=userId%>+"&folderId="+folderId,function(data){
                <% for(int j=0;j<portletIds.getRowCount();j++){                                             
              portlets=tempportal.getPortlets(); %> 
                    getPortletDetails(<%=Integer.parseInt(portletIds.getFieldValueString(j, 0))%>, rowEdgeParams, colEdgeParams,"","",<%=currentTabId%>,"");                                        
                   <% }%>
            });
        }
        </script>
    </body>
</html>




