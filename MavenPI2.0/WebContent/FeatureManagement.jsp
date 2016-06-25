<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,com.progen.users.UserLayerDAO,com.progen.superadmin.SuperAdminAction,com.progen.users.PrivilegeManager,utils.db.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*,prg.db.PbReturnObject"%>
<%@page import="prg.db.Session,java.util.*,prg.db.PbDb,prg.util.screenDimensions" %>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%--
    Document   : featureManagement
    Created on : Mar 13, 2013,  PM
    Author     : Gopesh.sharma@progenbusiness.com
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%          //for clearing cache

            int USERID=0;
            boolean refreshGraph = true;
            if(session.getAttribute("USERID")!=null)
            USERID = Integer.parseInt((String) session.getAttribute("USERID"));

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);

            //added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");

    String userType=null;
    UserLayerDAO uDao=new UserLayerDAO();
    userType=uDao.getUserTypeForFeatures(USERID);

            String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

            screenDimensions dims =new screenDimensions();
                int pageFont,anchorFont;
                HashMap screenMap  =dims.getFontSize(session,request,response);
                ////.println("screenMap --"+screenMap .size());
                if(!String.valueOf(screenMap .get("pageFont")).equalsIgnoreCase("NULL")){
                pageFont=Integer.parseInt(String.valueOf(screenMap .get("pageFont")));
                anchorFont = Integer.parseInt(String.valueOf(screenMap .get("pageFont")))+1;
                ////.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }else{
                pageFont = 11;
                anchorFont = 12;
                ////.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }
            boolean showExtraTabs = true;
            ServletContext context = getServletContext();
            //boolean isAxa = Boolean.parseBoolean(context.getInitParameter("isAxa"));
            if(session.getAttribute("USERID")==null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")){
            response.sendRedirect(request.getContextPath()+"/newpbLogin.jsp");
   }else{



                String[] paramList = uDao.paramList;
                String[] graphList = uDao.graphList;
                String[] tableList = uDao.tableList;
                String[] actionList = uDao.actionList;

    HashMap ParamHashMap=new HashMap();
    ParamHashMap=uDao.getParamSelectedList(userType);
    ArrayList<Boolean> paramSelectedListPA = (ArrayList<Boolean>)ParamHashMap.get("paramSelectedListPA");
    ArrayList<Boolean> paramSelectedListPPA = (ArrayList<Boolean>)ParamHashMap.get("paramSelectedListPPA");
    ArrayList<Boolean> paramSelectedListCCA = new ArrayList<Boolean>();
    ArrayList<Boolean> paramSelectedListCCPA = new ArrayList<Boolean>();
    if(userType.equalsIgnoreCase("ADMIN")){
    paramSelectedListCCA = (ArrayList<Boolean>)ParamHashMap.get("paramSelectedListCCA");
    paramSelectedListCCPA = (ArrayList<Boolean>)ParamHashMap.get("paramSelectedListCCPA");
    }
    int sizeofList=paramSelectedListPA.size();
    int countGraph=paramList.length;
    int countTable=countGraph+graphList.length;
    int countAction=countTable+tableList.length;
    ArrayList groupIdsList = new ArrayList();
    ArrayList groupNamesList = new ArrayList();
String checkCon="";
String contextPath=request.getContextPath();
%>
<html>
     <head>
       
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/scripts.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.tabs.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%=contextPath%>/dragAndDropTable.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>


        <script  type="text/javascript">
            var xmlhttp;
            var groupidss=new Array;
            var groupNamesl=new Array;
             function initDialog(){
                    $("#draggedTabledataAssign").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });
                     $("#draggedTabledataAssign").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });


            }

            function adminAssign(){
            $("#userListDiv").hide();
                }
                 function userList(){
            $("#userListDiv").show();
            $("#logicalGroup").hide();
             $("#selectTest").hide();
                }
               function pbBiManager(){
                var path = "";
                    path = "srchQueryAction.do?srchParam=pbBiManager";

                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
              function goBiManager(path){
               window.location.href=path;
            }
             function modlistDis(){
             $("#modulesOption").toggle();
            }
            function modlistOut(){
            $("#modulesOption").hide();
            }
            function featureGroup(){
                $("#featureGroup").show();

            }
            function paramSection(){
                if($("#paramSectionList").is(':visible')){
                    $("#paramImg").attr("src", "images/treeViewImages/plus.gif");
                }else{
                    $("#paramImg").attr("src", "images/treeViewImages/minus.gif");
                }
                $("#paramSectionList").toggle();

            }
             function graphSection(){
                  if($("#graphSectionList").is(':visible')){
                    $("#graphImg").attr("src", "images/treeViewImages/plus.gif");
                }else{
                    $("#graphImg").attr("src", "images/treeViewImages/minus.gif");
                }
                $("#graphSectionList").toggle();
            }
             function tableSection(){
                 if($("#tableSectionList").is(':visible')){
                    $("#tableImg").attr("src", "images/treeViewImages/plus.gif");
                }else{
                    $("#tableImg").attr("src", "images/treeViewImages/minus.gif");
                }
                $("#tableSectionList").toggle();
            }
             function actionSection(){
                  if($("#actionSectionList").is(':visible')){
                    $("#actionImg").attr("src", "images/treeViewImages/plus.gif");
                }else{
                    $("#actionImg").attr("src", "images/treeViewImages/minus.gif");
                }
                $("#actionSectionList").toggle();
            }
             function showOtherOptions(){
                $("#divtab").hide();
            }
            function showGroups(groupIdList){
            var moduleName="Groups";
                        initDialog();
                         $("#draggedTabledataAssign").dialog('open');
                    $.ajax({
                        url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=showPortalAssignment&moduleName="+moduleName,
                        success: function(data){
                            var jsonVar=eval('('+data+')')
                            $("#draggedTabledataAssign").html("")
                            $("#draggedTabledataAssign").html(jsonVar.htmlStr).append("<table align='right' width='100%'><tr align='center'><td><input type='button' value='Done'class='navtitle-hover' onclick='saveEditGroupDetails("+groupIdList+")'></td></tr></table>");
                            isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                            grpColArray=jsonVar.memberValues

                            $("#myList3").treeview({
                                animated:"slow",
                                persist: "cookie"
                            });
                            $('ul#myList3 li').quicksearch({
                                position: 'before',
                                attached: 'ul#myList3',
                                loaderText: '',
                                delay: 100
                            })
                            $(".myDragTabs").draggable({

                                helper:"clone",
                                effect:["", "fade"]
                            });
                            $("#dropTabs").droppable({
                                activeClass:"blueBorder",
                                accept:'.myDragTabs',
                                drop: function(ev, ui) {
                                    createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                                }
                            }
                        );

                            $(".sortable").sortable();

                        }

                    });

                }
                function saveEditGroupDetails(groupIdList){
                var GroupIds=new Array;
                var groupNames=new Array
                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                for(var i=0;i<liObj.length;i++){
                    mbrIds=(liObj[i].id).split("~");
                    GroupIds.push(mbrIds[0].replace("_li", "", "gi"));
                    groupidss.push(mbrIds[0].replace("_li", "", "gi"));
                    $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
                    groupNames.push($(this).find("td").eq(1).html());
                    groupNamesl.push($(this).find("td").eq(1).html());
                    }
                )
                }
                <%=groupIdsList%>=groupidss;
                if($("#paramSectionList").is(':visible')){
                    $("#paramImg").attr("src", "images/treeViewImages/plus.gif");
                }else{
                    $("#paramImg").attr("src", "images/treeViewImages/minus.gif");
                }
                $("#paramSectionList").toggle();
                $("#draggedTabledataAssign").dialog('close')
                var confirmDel=confirm("Do you want to Assign features to Groups");
                    if(confirmDel!=true){
                    window.location.href=window.location.href;
                    }
            }
             function goBiManager(path){
               window.location.href=path;
            }
            function saveSectionDetails(){
//                var UserIds=new Array;
//                var userNames=new Array
//                var ulObj=document.getElementById("sortable");
//                    var liObj=ulObj.getElementsByTagName("li");
//                    for(var i=0;i<liObj.length;i++){
//                        mbrIds=(liObj[i].id).split("~");
//                        UserIds.push(mbrIds[0].replace("_li", "", "gi"));
//                        $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
//                            userNames.push($(this).find("td").eq(1).html());
//                         }
//                    )
//                    }
//                    alert(UserIds);alert(userNames);
                    var moduleName="Report";
                $.post("<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=featureManagement&moduleName="+moduleName+"&groupIds="+groupidss,$("#reportPropertiesform").serialize(),
                                function(data){
                    window.location.href=window.location.href;
                                });

            }

           function selectParamAllAn()
            {
                if ($("#selectAllA").is(':checked')) {
                var paramName;
             <%  for(int i=0;i<paramList.length;i++) { %>
             paramName=("<%=paramList[i]%>");
             $('#'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                   var paramName;
             <%  for(int i=0;i<paramList.length;i++) { %>
             paramName=("<%=paramList[i]%>");
             $('#'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }

            function selectParamAllPAn()
            { if ($("#selectAllPA").is(':checked')) {
                var paramName;
             <%  for(int i=0;i<paramList.length;i++) { %>
             paramName=("<%=paramList[i]%>");
             $('#p'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                      var paramName;
             <%  for(int i=0;i<paramList.length;i++) { %>
             paramName=("<%=paramList[i]%>");
             $('#p'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }
             function selectgraphAllAn()
            { if ($("#selectAllAnalyzerGraph").is(':checked')) {
                var graphName;
             <%  for(int i=0;i<graphList.length;i++) { %>
             graphName=("<%=graphList[i]%>");
             $('#ga'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                     var graphName;
             <%  for(int i=0;i<graphList.length;i++) { %>
             graphName=("<%=graphList[i]%>");
             $('#ga'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }

            function selectgraphAllPAn()
            { if ($("#selectAllPowerAnalyzerGraph").is(':checked')) {
                var graphName;
             <%  for(int i=0;i<graphList.length;i++) { %>
             graphName=("<%=graphList[i]%>");
             $('#gpa'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                      var paramName;
             <%  for(int i=0;i<graphList.length;i++) { %>
             graphName=("<%=graphList[i]%>");
             $('#gpa'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }
             function selecttableAllAn()
            {  if ($("#selectAllAnalyzerTable").is(':checked')) {
                var tableName;
             <%  for(int i=0;i<tableList.length;i++) { %>
             tableName=("<%=tableList[i]%>");
             $('#ta'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                    var tableName;
             <%  for(int i=0;i<tableList.length;i++) { %>
             tableName=("<%=tableList[i]%>");
             $('#ta'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }

            function selecttableAllPAn()
            { if ($("#selectAllPowerAnalyzerTable").is(':checked')) {
                var tableName;
             <%  for(int i=0;i<tableList.length;i++) { %>
             tableName=("<%=tableList[i]%>");
             $('#tpa'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                     var tableName;
             <%  for(int i=0;i<tableList.length;i++) { %>
             tableName=("<%=tableList[i]%>");
             $('#tpa'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }
            function selectLhsAllAn()
            {  if ($("#selectAllAnalyzerLHS").is(':checked')) {
                var actionName;
             <%  for(int i=0;i<actionList.length;i++) { %>
             actionName=("<%=actionList[i]%>");
             $('#aa'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                      var actionName;
             <%  for(int i=0;i<actionList.length;i++) { %>
             actionName=("<%=actionList[i]%>");
             $('#aa'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }

            function selectLhsAllPAn()
            {  if ($("#selectAllPowerAnalyzerLHS").is(':checked')) {
                var actionName;
             <%  for(int i=0;i<actionList.length;i++) { %>
             actionName=("<%=actionList[i]%>");
             $('#paa'+<%=i%>).attr('checked',true);
             <% } %>
                 }
                 else{
                  var actionName;
             <%  for(int i=0;i<actionList.length;i++) { %>
             actionName=("<%=actionList[i]%>");
             $('#paa'+<%=i%>).attr('checked',false);
             <% } %>
                 }
            }

                </script>


         <script type="text/javascript">
            $(document).ready(function(){
                $("#breadCrumb").jBreadCrumb();
                $("#tabs").tabs();
            });

        </script>
     <style type="text/css">


               #modulesOption {
                    background:none repeat scroll 0 0 white;
                    list-style:none outside none;
                    width:auto;
                    display:none;
                    height:auto;
                    background-color:white;
                    overflow: visible;
                    position:absolute;
                    text-align:left;
                    z-index: 9999999;
                    margin-left: 0px;
                    margin-top: 20px;
                    }
                  #features {
                    height:25px;
                    background-color:whitesmoke;
                    font-size: 15px;
                    text-align:center;
                    margin-left: 50px;
                    margin-right: 50px;
                    margin-top: 0px;
                    }

                    #sectionName{
                        margin-top: 20px;
                        font-size: 15px;
                        width: auto;
                        color: #0095B6;
                        }


     </style>

                  <table style="width:100%;">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>



         <div id="tabs" style="width:99%;min-height:640px;height:auto" align="center">
            <ul>
                <li ><a href="javascript:void(0)" id="" title=""  onclick="modlistDis()"><%=TranslaterHelper.getTranslatedInLocale("Report_Title_Size", cle)%></a>
                </li>
                <li ><a href="javascript:void(0)" id=""  title=""  onclick="modlistDis()"><%=TranslaterHelper.getTranslatedInLocale("Report_Title_Size", cle)%></a></li>
                <li ><a href="javascript:void(0)" title="Logic Group Creation" onClick="showOtherOptions()"><%=TranslaterHelper.getTranslatedInLocale("Report_Title_Size", cle)%></a></li>
                </ul>
             <div id="usersDiv">
            <table  align='right'  style="align:right">
                 <%                         PbDb pbdb = new PbDb();
                 ArrayList groupidList = new ArrayList();
                                        String queryList1 = "select * from PRG_AR_LOGICGROUP_MASTER";
                                        PbReturnObject grouplist1 = null;
                                        grouplist1 = pbdb.execSelectSQL(queryList1);

                                        for (int i = 0; i < grouplist1.getRowCount(); i++) {
                                            String str1 = grouplist1.getFieldValueString(i, 1);
                                            groupidList.add(grouplist1.getFieldValueString(i, 0));}
                 %>
            <tr><td><input type="button" value="Assign to Groups" class="navtitle-hover" onclick="showGroups('<%=groupidList%>')">
            <td><input type="button" value="Home" class="navtitle-hover" onclick="goBiManager('pbBIManager.jsp')"></td></tr>
            </table>
            <table id="usersTable" width="50%" style="display:none;">
            </table>
            <div align="center" id="logicalGroup"  style="display:none;width:100%;height:100%;position:centre;margin-left: 10%" title="Assign Report to Users">
               <table style="width:70%">
                    <tr>
                        <td  style="height:10px;width:20%" align="right">
                        </td>
                    </tr>
                    <tr>
                    </tr>

                    <div id="draggedTabledataAssign"  style="height:auto;display:none;">
                    </div>
                </table>
            <div id="doneIdCreate"></div>
                        </div>
        </div>
        <form id="reportPropertiesform" name="reportProperties" method="post" action="">

                <table id="sectionVise" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="0" style="width:70%;">
                    <thead style="text-align: center;">
                        <tr>
                            <th nowrap valign="top" style="font-size:14px; width:25%;"> <%=TranslaterHelper.getTranslatedInLocale("Sections", cle)%></th>
                            <th nowrap valign="top" style="font-size:14px; width:25%;"> <%=TranslaterHelper.getTranslatedInLocale("Sub_Sections", cle)%></th>
                            <th nowrap valign="top" style="font-size:14px; width:25%;"> <%=TranslaterHelper.getTranslatedInLocale("Analyzer_Features", cle)%></th>
                            <th nowrap valign="top" id="PowerAnalyzerhead" style="font-size:14px; width:25%;">  <%=TranslaterHelper.getTranslatedInLocale("Power_Analyzer_Features", cle)%></th>
                      </tr>
                    </thead>
                    <tbody id="sectionsvalue" style="text-align: left;">
                        <tr><td class="" id="sectionName"><a id="tableImg1" href="javascript:void(0)" onclick="paramSection()">
                                    <img alt=""  id="paramImg" src="images/treeViewImages/plus.gif"/>
                                </a><%=TranslaterHelper.getTranslatedInLocale("Parameter_Section", cle)%></td>
                            <td><%=TranslaterHelper.getTranslatedInLocale("Select_All_Param_Options", cle)%></td>
                            <td style="text-align: center;"><input id="selectAllA" type="checkbox" name="" value=""  onclick="selectParamAllAn()"/></td>
                            <td style="text-align: center;"><input id="selectAllPA" type="checkbox" name="" value="" onclick="selectParamAllPAn()"/></td>
                        </tr>
<!--                          <td><input type="checkbox" name="ChangeInReport" id="ChangeInReport"></td>-->
                        <tbody id="paramSectionList" style="display:none;">
                            <%  for(int i=0;i<paramList.length;i++) {%>
                            <tr>
                        <td></td><td style="font-size:13px;"><%=paramList[i]%></td>

                        <%if(sizeofList>0){
                            String chechCon="";
                            String dis="";
                            String chechCon1="";
                            String dis1="";
                            if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPA.get(i)) {
                                chechCon="checked";dis="";
                                }else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCA.get(i)) {
                                    chechCon="checked";dis="";
                                     } else if(userType.equalsIgnoreCase("ADMIN") && !paramSelectedListCCA.get(i) && paramSelectedListPA.get(i)) {
                                         chechCon="";dis="";
                                         } else if(userType.equalsIgnoreCase("SUPERADMIN")) {
                                             chechCon="";dis="";
                                              } else{
                                                chechCon="";dis="disabled";
                                                    } %>

                <td style="text-align: center;">
                    <input id="<%=i%>" type="checkbox"  <%=dis%> name="<%=paramList[i]%>" value="<%=paramList[i]%>" <%=chechCon%>/>
                </td>

                                <% if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPPA.get(i)) {
                                chechCon1="checked";dis1="";
                                 }else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCPA.get(i)) {
                                     chechCon1="checked";dis1="";
                                     } else if(userType.equalsIgnoreCase("ADMIN") && !paramSelectedListCCPA.get(i) && paramSelectedListPPA.get(i)) {
                                         chechCon1="";dis1="";
                                          } else if(userType.equalsIgnoreCase("SUPERADMIN")) {
                                             chechCon1="";dis1="";
                                              }
                                              else {
                                                 chechCon1="";dis1="disabled";
                                                   }
                       %>

                        <td style="text-align: center;">
                            <input id="p<%=i%>" <%=dis1%> type="checkbox"  name="P_<%=paramList[i]%>" value="<%=paramList[i]%>" <%=chechCon1%>/>
                        </td>

                        <%
                        }  else {
                            %>
                        <td style="text-align: center;"><input id="<%=i%>" type="checkbox" name="<%=paramList[i]%>" value="<%=paramList[i]%>"/></td>
                        <td style="text-align: center;"><input id="<%=i%>" type="checkbox" name="P_<%=paramList[i]%>" value="<%=paramList[i]%>"/></td>
                        <% } %>
                           </tr>
                           <%
                            } %>
                        </tbody>


                        <tr><td id="sectionName"><a href="javascript:void(0)" onclick="graphSection()">
                                    <img alt=""  id="graphImg" src="images/treeViewImages/plus.gif"/></a><%=TranslaterHelper.getTranslatedInLocale("Graph_Section", cle)%></td>
                            <td><%=TranslaterHelper.getTranslatedInLocale("Select_All_Graph_Options", cle)%></td>
                            <td style="text-align: center;"><input id="selectAllAnalyzerGraph" type="checkbox" name="" value=""  onclick="selectgraphAllAn()"/></td>
                            <td style="text-align: center;"><input id="selectAllPowerAnalyzerGraph" type="checkbox" name="" value="" onclick="selectgraphAllPAn()"/></td>
                        </tr>
                        <tbody id="graphSectionList" style="display:none;">
                            <%  for(int i=0;i<graphList.length;i++) {%>
                            <tr>
                        <td></td><td style="font-size:13px;"><%=graphList[i]%></td>
                        <% if(sizeofList>0){
                        if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPA.get(countGraph+i)) { %>
                        <td style="text-align: center;"><input id="ga<%=i%>" type="checkbox" name="<%=graphList[i]%>" value="<%=graphList[i]%>" checked /></td>
                       <% }else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCA.get(countGraph+i)) { %>
                        <td style="text-align: center;"><input id="gpa<%=i%>" type="checkbox" name="<%=graphList[i]%>" value="<%=graphList[i]%>" /></td>
                        <% } else if(userType.equalsIgnoreCase("SUPERADMIN")) {%>
                        <td style="text-align: center;"><input id="ga<%=i%>" type="checkbox" name="<%=graphList[i]%>" value="<%=graphList[i]%>"/></td>
                        <% }else { %>
                        <td style="text-align: center;"><input id="ga<%=i%>" type="checkbox" disabled name="<%=graphList[i]%>" value="<%=graphList[i]%>" /></td>
                        <% }if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPPA.get(countGraph+i)) { %>
                        <td style="text-align: center;"><input id="gpa<%=i%>" type="checkbox" name="P_<%=graphList[i]%>" value="<%=graphList[i]%>" checked /></td>
                        <% }else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCPA.get(countGraph+i)) { %>
                        <td style="text-align: center;"><input id="gpa<%=i%>" type="checkbox" name="P_<%=graphList[i]%>" value="<%=graphList[i]%>" checked /></td>
                        <% } else if(userType.equalsIgnoreCase("ADMIN") && !paramSelectedListCCPA.get(countGraph+i) && paramSelectedListPPA.get(countGraph+i)) {%>
                        <td style="text-align: center;"><input id="gpa<%=i%>" type="checkbox" name="P_<%=graphList[i]%>" value="<%=graphList[i]%>"  onclick=""/></td>
                        <% } else if(userType.equalsIgnoreCase("SUPERADMIN")) { %>
                        <td style="text-align: center;"><input id="gpa<%=i%>" type="checkbox" name="P_<%=graphList[i]%>" value="<%=graphList[i]%>" /></td>
                        <% } else {%>
                        <td style="text-align: center;"><input id="gpa<%=i%>" type="checkbox" disabled name="P_<%=graphList[i]%>" value="<%=graphList[i]%>" /></td>
                        <% }
                        }else {%>
                        <td style="text-align: center;"><input id="ga<%=i%>" type="checkbox" name="<%=graphList[i]%>" value="<%=graphList[i]%>" /></td>
                        <td style="text-align: center;"><input id="gpa<%=i%>" type="checkbox" name="P_<%=graphList[i]%>" value="<%=graphList[i]%>" /></td>
                        <% } %>
                            </tr>
                           <% } %>
                        </tbody>
                         <tr><td id="sectionName"><a href="javascript:void(0)" onclick="tableSection()">
                                     <img alt=""  id="tableImg" src="images/treeViewImages/plus.gif"/></a><%=TranslaterHelper.getTranslatedInLocale("Table_Section", cle)%></td>
                             <td><%=TranslaterHelper.getTranslatedInLocale("Select_All_Table_Options", cle)%></td>
                            <td style="text-align: center;"><input id="selectAllAnalyzerTable" type="checkbox" name="" value=""  onclick="selecttableAllAn()"/></td>
                            <td style="text-align: center;"><input id="selectAllPowerAnalyzerTable" type="checkbox" name="" value="" onclick="selecttableAllPAn()"/></td>
                         </tr>
                         <tbody id="tableSectionList" style="display:none;">
                            <%  for(int i=0;i<tableList.length;i++) {%>
                            <tr>
                        <td></td><td style="font-size:13px;"><%=tableList[i]%></td>
                        <%  if(sizeofList>0){
                        if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPA.get(countTable+i)) { %>
                        <td style="text-align: center;"><input id="ta<%=i%>" type="checkbox" name="<%=tableList[i]%>" value="<%=tableList[i]%>" checked /></td>
                        <% } else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCA.get(countTable+i)) { %>
                       <td style="text-align: center;"><input id="ta<%=i%>" type="checkbox" name="<%=tableList[i]%>" value="<%=tableList[i]%>" checked /></td>
                        <% } else if(userType.equalsIgnoreCase("ADMIN") && !paramSelectedListCCA.get(countTable+i) && paramSelectedListPA.get(countTable+i)) {%>
                       <td style="text-align: center;"><input id="ta<%=i%>" type="checkbox" name="<%=tableList[i]%>" value="<%=tableList[i]%>"  onclick=""/></td>
                       <% } else if(userType.equalsIgnoreCase("SUPERADMIN")) { %>
                        <td style="text-align: center;"><input id="ta<%=i%>" type="checkbox" name="<%=tableList[i]%>" value="<%=tableList[i]%>" /></td>
                        <% } else {%>
                        <td style="text-align: center;"><input id="ta<%=i%>" type="checkbox" disabled name="<%=tableList[i]%>" value="<%=tableList[i]%>" /></td>
                        <% }if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPPA.get(countTable+i)) { %>
                        <td style="text-align: center;"><input id="tpa<%=i%>" type="checkbox" name="P_<%=tableList[i]%>" value="<%=tableList[i]%>" checked /></td>
                       <% }else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCPA.get(countTable+i)) {%>
                        <td style="text-align: center;"><input id="tpa<%=i%>" type="checkbox" name="P_<%=tableList[i]%>" value="<%=tableList[i]%>" checked /></td>
                        <% } else if(userType.equalsIgnoreCase("ADMIN") && !paramSelectedListCCPA.get(countTable+i) && paramSelectedListPPA.get(countTable+i)) {%>
                        <td style="text-align: center;"><input id="tpa<%=i%>" type="checkbox" name="P_<%=tableList[i]%>" value="<%=tableList[i]%>"  onclick=""/></td>
                        <% } else if(userType.equalsIgnoreCase("SUPERADMIN")) {  %>
                        <td style="text-align: center;"><input id="tpa<%=i%>" type="checkbox" name="P_<%=tableList[i]%>" value="<%=tableList[i]%>" /></td>
                          <% }else {%>
                          <td style="text-align: center;"><input id="tpa<%=i%>" type="checkbox" disabled name="P_<%=tableList[i]%>" value="<%=tableList[i]%>" onclick=""/></td>
                        <% }
                          } else {%>
                        <td style="text-align: center;"><input id="ta<%=i%>" type="checkbox" name="<%=tableList[i]%>" value="<%=tableList[i]%>" /></td>
                        <td style="text-align: center;"><input id="tpa<%=i%>" type="checkbox" name="P_<%=tableList[i]%>" value="<%=tableList[i]%>" /></td>
                        <% } %>
                            </tr>
                           <% } %>
                        </tbody>
                         <tr><td id="sectionName"><a href="javascript:void(0)" onclick="actionSection()">
                                     <img alt=""  id="actionImg" src="images/treeViewImages/plus.gif"/></a><%=TranslaterHelper.getTranslatedInLocale("LHS_Sections", cle)%></td>
                             <td><%=TranslaterHelper.getTranslatedInLocale("Select_All_LHS_Options", cle)%></td>
                            <td style="text-align: center;"><input id="selectAllAnalyzerLHS" type="checkbox" name="" value=""  onclick="selectLhsAllAn()"/></td>
                            <td style="text-align: center;"><input id="selectAllPowerAnalyzerLHS" type="checkbox" name="" value="" onclick="selectLhsAllPAn()"/></td>
                            </tr>
                         <tbody id="actionSectionList" style="display:none;">
                            <%  for(int i=0;i<actionList.length;i++) {%>
                            <tr>
                        <td></td><td style="font-size:13px;"><%=actionList[i]%></td>
                        <%  if(sizeofList>0){
                        if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPA.get(countAction+i)) { %>
                        <td style="text-align: center;"><input id="aa<%=i%>" type="checkbox" name="<%=actionList[i]%>" value="<%=actionList[i]%>" checked /></td>
                        <% } else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCA.get(countAction+i)) { %>
                       <td style="text-align: center;"><input id="aa<%=i%>" type="checkbox" name="<%=actionList[i]%>" value="<%=actionList[i]%>" checked /></td>
                       <% } else if(userType.equalsIgnoreCase("ADMIN") && !paramSelectedListCCA.get(countAction+i) && paramSelectedListPA.get(countAction+i)) {%>
                        <td style="text-align: center;"><input id="aa<%=i%>" type="checkbox" name="<%=actionList[i]%>" value="<%=actionList[i]%>"  onclick=""/></td>
                       <% } else if(userType.equalsIgnoreCase("SUPERADMIN")) { %>
                        <td style="text-align: center;"><input id="aa<%=i%>" type="checkbox" name="<%=actionList[i]%>" value="<%=actionList[i]%>" /></td>
                        <% } else {%>
                        <td style="text-align: center;"><input id="aa<%=i%>" type="checkbox" disabled name="<%=actionList[i]%>" value="<%=actionList[i]%>" /></td>
                        <% }if(userType.equalsIgnoreCase("SUPERADMIN") && paramSelectedListPPA.get(countAction+i)) { %>
                        <td style="text-align: center;"><input id="paa<%=i%>" type="checkbox" name="P_<%=actionList[i]%>" value="<%=actionList[i]%>" checked /></td>
                       <% }else if(userType.equalsIgnoreCase("ADMIN") && paramSelectedListCCPA.get(countAction+i)) {%>
                        <td style="text-align: center;"><input id="paa<%=i%>" type="checkbox" name="P_<%=actionList[i]%>" value="<%=actionList[i]%>" checked /></td>
                        <% } else if(userType.equalsIgnoreCase("ADMIN") && !paramSelectedListCCPA.get(countAction+i) && paramSelectedListPPA.get(countAction+i)) {%>
                        <td style="text-align: center;"><input id="paa<%=i%>" type="checkbox" name="P_<%=actionList[i]%>" value="<%=actionList[i]%>" /></td>
                        <% } else if(userType.equalsIgnoreCase("SUPERADMIN")) {  %>
                        <td style="text-align: center;"><input id="paa<%=i%>" type="checkbox" name="P_<%=actionList[i]%>" value="<%=actionList[i]%>"/></td>
                          <% }else {%>
                          <td style="text-align: center;"><input id="paa<%=i%>" type="checkbox" disabled name="P_<%=actionList[i]%>" value="<%=actionList[i]%>"/></td>
                        <% }
                          } else {%>
                        <td style="text-align: center;"><input id="aa<%=i%>" type="checkbox" name="<%=actionList[i]%>" value="<%=actionList[i]%>"/></td>
                        <td style="text-align: center;"><input id="paa<%=i%>" type="checkbox" name="P_<%=actionList[i]%>" value="<%=actionList[i]%>"/></td>
                        <% } %>
                            </tr>
                           <% } %>
                        </tbody>
                    </tbody>
                </table>


                        <table>
                            <tr align='center' ><td><input type="button" value="<%=TranslaterHelper.getTranslatedInLocale("done", cle)%>" class="navtitle-hover" onclick="saveSectionDetails()"></td></tr>
                        </table>
                        </form>


             </div>


    </body>
</html>
<% } %>