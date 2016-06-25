
<%@page import="java.util.Locale"%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="com.progen.db.PbBaseDAO"%>
<%@page import="java.util.Properties"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,java.util.ArrayList" %>
<%@page import="prg.db.PbDb" %>
<%@page import="utils.db.ProgenConnection" %>
<%@page import="java.util.Date" %>
<%@page import="com.progen.reportdesigner.action.ReportTemplateAction" %>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO" %>
<%@page import="java.io.InputStream"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>

<%
            boolean isCompanyValid = false;
             Locale cle = null;
             cle = (Locale) session.getAttribute("UserLocaleFormat");
             
            UserLayerDAO userdao=new UserLayerDAO();
            String userIdSt=null;
            if (session.getAttribute("USERID") != null) {
            userIdSt = (String) session.getAttribute("USERID");
            }
                String adminType=null;
                adminType=userdao.getUserTypeForFeatures(Integer.parseInt(userIdSt));

            String Pagename = "User List";
            String url = request.getRequestURL().toString();
            brdcrmb.inserting(Pagename, url);
            PbReturnObject retObject = null;
            PbDb pbdb = new PbDb();
            PbReturnObject list = null;
            PbReturnObject Active = null;
            String sortValue = "";
            String sortOptionvalue = "";
            String dbType = "";
            String loginName = "";
            String userType = null;
            String userType1=null;
            if (session.getAttribute("MetadataDbType") != null) {
                dbType = (String) session.getAttribute("MetadataDbType");
            }
            if (session.getAttribute("prgr") != null) {
                retObject = (PbReturnObject) session.getAttribute("prgr");
            }
            if (session.getAttribute("selvalUList") != null) {
                sortValue = (String) session.getAttribute("selvalUList");
            }
            if (session.getAttribute("seloptionuList") != null) {
                sortOptionvalue = (String) session.getAttribute("seloptionuList");
            }
            if (session.getAttribute("LOGINID") != null) {
                loginName = (String) session.getAttribute("LOGINID");
            }
            //LOGINID
            if (session.getAttribute("prgr") == null) {
                String query = "";
                if (isCompanyValid) {
                    if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        query = "select pu.*, po.ORGANISATION_NAME, pt.USER_TYPE_NAME,case when getdate()-COALESCE(pu.pu_end_date,getdate())<=0 then 'Active' else 'Expired' end as USER_STATUS"
                                + " from prg_ar_users pu left outer join prg_org_master po on(pu.account_type= po.org_id ) left outer join  prg_user_type pt on (pt.user_type_id=pu.user_type)"
                                + " order by pu.pu_end_date desc,pu.last_update_date desc";
                    }
                    else if (dbType.equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        query = "select pu.*, po.ORGANISATION_NAME, pt.USER_TYPE_NAME,case when now()-COALESCE(pu.pu_end_date,now())<=0 then 'Active' else 'Expired' end as USER_STATUS"
                                + " from prg_ar_users pu left outer join prg_org_master po on(pu.account_type= po.org_id ) left outer join  prg_user_type pt on (pt.user_type_id=pu.user_type)"
                                + " order by pu.pu_end_date desc,pu.last_update_date desc";
                    }
                    else {
                        query = "select pu.*, po.organisation_name, pt.user_type_name,case when sysdate-COALESCE(pu.pu_end_date,sysdate)<=0 then 'Active' else 'Expired' end as user_status from prg_ar_users pu,"
                                + " prg_org_master po,prg_user_type pt where pu.account_type= po.org_id(+) and pt.user_type_id(+)=pu.user_type order by pu.pu_end_date desc,pu.last_update_date desc";
                    }

                } else {
                    if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        query = "select pu.*, po.ORGANISATION_NAME, pt.USER_TYPE_NAME,case when getdate()-COALESCE(pu.pu_end_date,getdate())<=0 then 'Active' else 'Expired' end"
                                + " as USER_STATUS,pa.POWER_ANALYZER,pa.QUERY_STUDIO,pa.RESTRICTED_POWER_ANALYZER from prg_ar_users as pu left Outer Join prg_org_master as po on pu.account_type= po.org_id Left Outer Join prg_user_type as pt on pt.user_type_id=pu.user_type Inner Join prg_user_assignments pa on pu.pu_id = pa.user_id"
                                + " order by pu.pu_end_date desc,pu.last_update_date desc";
                    }
                     else if (dbType.equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        query = "select pu.*, po.ORGANISATION_NAME, pt.USER_TYPE_NAME,case when now()-COALESCE(pu.pu_end_date,now())<=0 then 'Active' else 'Expired' end"
                                + " as USER_STATUS,pa.POWER_ANALYZER,pa.QUERY_STUDIO,pa.RESTRICTED_POWER_ANALYZER from prg_ar_users as pu left Outer Join prg_org_master as po on pu.account_type= po.org_id Left Outer Join prg_user_type as pt on pt.user_type_id=pu.user_type Inner Join prg_user_assignments pa on pu.pu_id = pa.user_id"
                                + " order by pu.pu_end_date desc,pu.last_update_date desc";
                    }
                    else {
                        query = "select pu.*, po.organisation_name, pt.user_type_name,case when sysdate-COALESCE(pu.pu_end_date,sysdate)<=0 then 'Active' else 'Expired' end as user_status,pa.POWER_ANALYZER,pa.QUERY_STUDIO,pa.RESTRICTED_POWER_ANALYZER from prg_ar_users pu,"
                                + " prg_org_master po,prg_user_type pt,prg_user_assignments pa where pu.account_type= po.org_id(+) and pt.user_type_id(+)=pu.user_type and pu.pu_id=pa.user_id order by pu.pu_end_date desc,pu.last_update_date desc";
                    }

                }
                //.println("query=====" + query);
                list = pbdb.execSelectSQL(query);
                
                sortValue = "pu_start_date";
                sortOptionvalue = "ASC";
            } else {
                list = retObject;
                list.writeString();
            }

             String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
          UserStatusHelper helper=UserStatusHelper.getUserStatusHelper();
       if (helper == null) {
           Properties helperProps = new Properties();
           InputStream servletStream =request.getSession().getServletContext().getResourceAsStream("/WEB-INF/GenerateAssignModule.xml");
             if (servletStream != null) {
               try {
                   helperProps.loadFromXML(servletStream);
                   helper.createUserStatusHelper(helperProps);
                   helper=UserStatusHelper.getUserStatusHelper();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
          int portalCount=helper.getPortalsTotalCnt();
          int qdCount=helper.getQueryStudionTotalCnt();
          int powerAnaylzerCount=helper.getPowerAnalyzerTotalCnt();
          int onviewCount=helper.getOneViewTotalCnt();
          int scorecardCount=helper.getScorecardsTotalCnt();
          int superadmincount=helper.getSuperAdmin();
%>
<html>
    <head>
        <title>Metadata</title>
        <%--        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
      <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
                <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />--%>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/scripts.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen"/>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />-->
        <script type="text/javascript">
            var cActiveVal="";
            var userStatus = "";
            var modulename = "";
            function parentColseModUserTypePar()
            {
                $('#modifyUserType').dialog('close');

                window.location.href=window.location.href;
            }

            function cancelUsersReportAssign()
            {
                $('#userNewReport').dialog('close');
            }
            function reportToUser(userid)
            {
                var userId="";
                var i=0;
//                var obj = document.forms.myForm.chkusers;
//                if(isNaN(obj.length))
//                {
//                    if(document.forms.myForm.chkusers.checked)
//                    {
                        initDialog();
                        userId=userid;
                        
                        frameobj = document.getElementById("selectUserReportData");
                        frameobj.src= "userNewReportAssignment.jsp?userId="+userId;
                        $("#userNewReport").dialog('open');
//                    }
//                    else
//                    {
//                        alert("Please select user to Assign Reports")
//                    }
//                }
//                else
//                {
//                    for(var j=0;j<obj.length;j++)
//                    {
//                        if(document.forms.myForm.chkusers[j].checked==true)
//                        {
//                            i++;
//                            userId=document.forms.myForm.chkusers[j].value;
//
//                        }
//                    }
//
//                    if(i>1)
//                    {
//                        alert("Please select only one user to Assign Report");
//                    }
//                    else if(i==0)
//                    {
//                        alert("Please select user to Assign Report");
//                    }
//                    else
//                    {
//                        initDialog();
//                        
//                        frameobj = document.getElementById("selectUserReportData");
//                        frameobj.src= "userNewReportAssignment.jsp?userId="+userId;
//                        $("#userNewReport").dialog('open');
//                    }
//                }
                
               
                // var source2 = "userNewReportAssignment.jsp?userId="+userId;
               
            }

            function initDialog(){
                if ($.browser.msie == true){
                    //modifyUserType
                    $("#modifyUserType").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                     $("#superAdminUserDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });

                    $("#reportPrivM").dialog({
                        autoOpen: false,
                        height: 280,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#reportPriv").dialog({
                        autoOpen: false,
                        height: 280,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });

                    $("#reportGraphPriv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#reportTablePriv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });

                    $("#userAssignRoles").dialog({
                        autoOpen: false,
                        height: 450,
                        width:750,
                        position: 'justify',
                        modal: true
                    });
                    $("#userDialog").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });
                    $("#startPagePriv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#usertypenew").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 250,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#userNewReport").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#modifyCompany").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#viewByDialoguser").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#resetpswrddiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 200,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    
                    $("#resetExpiry").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 340,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });
                     $("#showLicensesDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 900,
                        position: 'justify',
                        modal: true
                    });

                $("#portalAssignmentDiv").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });
                  $("#userActivation").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });  
                    $("#renameUserdiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify'
                        //modal: true
                    });


                 $("#modal_dialog").dialog({
                        autoOpen: false,
                        height: 100,
                        width: 230,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                }
                else{
                     $("#showLicensesDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 900,
                        position: 'justify',
                        modal: true
                    });
                    $("#superAdminUserDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#modifyUserType").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#resetpswrddiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#reportPrivM").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#reportPriv").dialog({
                        autoOpen: false,
                        height: 230,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });

                    $("#reportGraphPriv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#reportTablePriv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                     $("#usertypenew").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 250,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });

                    $("#userAssignRoles").dialog({
                        autoOpen: false,
                        height:400,
                        width:750,
                        position: 'justify',
                        modal: true
                    });
                    $("#userDialog").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });
                    $("#startPagePriv").dialog({
                        autoOpen: false,
                        height: 490,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#userNewReport").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#viewByDialoguser").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#modifyCompany").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#resetExpiry").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 300,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });
                    $("#portalAssignmentDiv").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });
                    $("#userActivation").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });
                    $("#renameUserdiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#modal_dialog").dialog({
                        autoOpen: false,
                        height: 100,
                        width: 230,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
            }
        </script>
        <script type="text/javascript">
            function createUser(){
                initDialog();
               $.ajax({
                     url: "<%=request.getContextPath()%>/baseAction.do?param=getLicenceForUser&userId="+<%=userIdStr%>,
                    success: function(data){
                        if(data=='success'){
                    $('#userDialog').dialog('open');
                        }

                    else{
                        alert("User Limit Exceed");
            }
                    }
                });
			
            }

            function cancelUsersFolders(){
                
                $('#userAssignRoles').dialog('close');

            }
            function cancelUser()
            { 
                $('#userDialog').dialog('close');
            }
            function AssignPriveleges(){
                var limit = document.getElementById("limit").value;
                for(var i=0;i<limit;i++)
                {
                    var userchk = document.getElementById("userCheck"+i).checked;
                    if(userchk == true)
                    {
                        userchk = document.getElementById("userCheck"+i).value;
                        document.getElementById("selecteduser").value = userchk;
                    }
                }
                $("#reportPriv").dialog('open');

            }


            function AssignStartPage(){
                var user;
                var i=0;
                var obj = document.forms.myForm.chkusers;
                if(isNaN(obj.length))
                {
                    if(document.forms.myForm.chkusers.checked)
                    {
                        $("#reportPriv").dialog('open');
                    }
                    else
                    {
                        alert("Please select user to Assign StartPage")
                    }
                }
                else
                {
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.myForm.chkusers[j].checked==true)
                        {
                            i++;
                            user=document.forms.myForm.chkusers[j].value;

                        }
                    }

                    if(i>1)
                    {
                        alert("Please select only one user to Assign StartPage");
                    }
                    else if(i==0)
                    {
                        alert("Please select user to Assign StartPage");
                    }
                    else
                    {   document.getElementById("userId").value = user;
                        document.getElementById('reportstart').style.display='block';
                        document.getElementById('fadestart').style.display='block';
                        document.getElementById('mainBody').style.overflow='hidden';
                    }
                }
            }

            function cancelPriv(){
                $("#reportPriv").dialog('close');
                unCheckUserBox();
            }
            function goStartPage(){
                document.getElementById("startPagePriv").style.display='block';
                document.getElementById("fadePriv").style.display='block';
                document.getElementById('mainBody').style.overflow='hidden';
            }
            function closeStartDiv(){
                document.getElementById("startPagePriv").style.display='none';
                document.getElementById("fadePriv").style.display='none';
                document.getElementById('mainBody').style.overflow='auto';
            }
            //added for userPriv start
            function AssignPrivelegesnew(){
                var user;
                var i=0;
                var obj = document.forms.myForm.chkusers;
                if(isNaN(obj.length))
                {
                    if(document.forms.myForm.chkusers.checked)
                    {
                        $("#reportPriv").dialog('open');
                    }
                    else
                    {
                        alert("Please select user to Assign Privilages")
                    }
                }
                else
                {
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.myForm.chkusers[j].checked==true)
                        {
                            i++;
                            user=document.forms.myForm.chkusers[j].value;

                        }
                    }
                    if(i==1)
                    {
                        document.getElementById("AssignedPrivilages").style.display = 'block';
                    }
                    if(i>1)
                    {
                        alert("Please select only one user to Assign Privilages");
                    }
                    else if(i==0)
                    {
                        alert("Please select user to Assign Privilages");
                    }
                    else
                    {   document.getElementById("userId").value = user;

                        $("#reportPriv").dialog('open');
                    }
                }
            }
            //end of userPriv
            //added for ReportPriv
            function newReportPrivileges1(){
                $("#reportPrivM").dialog('open');
            }
            function newReportGraphPrivileges1(){

                $("#reportGraphPriv").dialog('open');
            }
            function newReportTablePrivileges1(){

                $("#reportTablePriv").dialog('open');
            }
            function Assignstartnew(){
                initDialog();
                // var userId;
                var user = "";
                var i=0;
                var obj = document.forms.myForm.chkusers;
                // chk1= document.myForm.chk2

                if(isNaN(obj.length))
                {
                    if(document.forms.myForm.chkusers.checked)
                    {
                        $("#startPagePriv").dialog('open');
                        var frameObj = document.getElementById("startPageFrame");
                        frameObj.src =  "loginStart.jsp?&checkUser="+user;
                    }
                    else
                    {
                        alert("Please select user to Assign StartPage")
                    }
                }
                else
                {
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.myForm.chkusers[j].checked==true)
                        {
                            i++;
                            user=document.forms.myForm.chkusers[j].value;

                        }
                    }

                    if(i>1)
                    {
                        alert("Please select only one user to Assign StartPage");
                    }
                    else if(i==0)
                    {
                        alert("Please select user to Assign StartPage");
                    }
                    else
                    {   document.getElementById("userId").value = user;
                        $("#startPagePriv").dialog('open');
                        var frameObj = document.getElementById("startPageFrame");
                        frameObj.src = "loginStart.jsp?&checkUser="+user;
                    }
                }
            }
            function cancelStart(){
                $("#startPagePriv").dialog('close');

            }
            //end of ReportPriv
            function viewDashboardG(path){
                document.forms.myForm.action=path;
                document.forms.myForm.submit();
            }
            function viewReportG(path){
                document.forms.myForm.action=path;
                document.forms.myForm.submit();
            }

            function goGlobe(){
                $(".navigateDialog").dialog('open');
            }
            function closeStart(){
                $(".navigateDialog").dialog('close');
            }
            function goPaths(path){
                parent.closeStart();
                document.forms.myForm.action=path;
                document.forms.myForm.submit();
            }

            function getprevilages(userid){
                return false;
                $.ajax({
                    url: "savePrivilages.do?method=getPrivilages&userId="+userid,
                    success: function(userid){
                        if(userid!=""){
                            document.forms.myFormH.action="savePrivilages.do?method=getPrivilages&userId="+userid;
                            document.forms.myFormH.submit();
                        }
                        else if(data==''){
                            document.getElementById('portal').style.display='block';
                            document.getElementById('fade').style.display='block';
                        }
                    }
                });
            }

            function createGroupUsers(){
            var moduleName="createGroup";
             $("#logicalGroup").show();
             $("#userListDiv").hide();
             $("#selectTest").hide();
                modulename = moduleName;
                $.ajax({
                    url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=showPortalAssignment&moduleName="+moduleName,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        $("#draggedTabledata").html("")
                        $("#draggedTabledata").html(jsonVar.htmlStr);
                        $("#doneIdCreate").html("")
                        $("#doneIdCreate").html("").append("<table align='right' width='100%'><tr align='center'><td><input type='button' value='Done' class='navtitle-hover' onclick='saveNewGroupDetails()'></td></tr></table>");

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
                $("#draggedTabledata").show();
                $("#CreateGroupName").show();
            }
            function editGroup(){
                 $("#userListDiv").hide();
                 $("#logicalGroup").hide();
                 $("#selectTest").show();
                 $("#editGroupName").hide();
                 $("#draggedTabledata2").hide();
                 $("#doneIdEdit").hide();
            }
            function EditGroupList(groupId,groupName){

                var modulename = "editGroup";
                $("#editGroupName").show();
                var groupId =groupId;
                $.ajax({
                    url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=showGroupDetails&moduleName="+modulename+"&groupId="+groupId,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        $("#draggedTabledata2").html("")
                        $("#draggedTabledata2").html(jsonVar.htmlStr);
                        $("#doneIdEdit").html("")
                         $("#doneIdEdit").html("").append("<table align='right' width='100%'><tr align='center'><td><input type='button' value='Done' class='navtitle-hover' onclick='saveEditGroupDetails("+groupId+")'></td></tr></table>");

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
                    }
                });
                $("#draggedTabledata2").show();
                $("#groupNameForEdit").val(groupName);
                $("#groupNameForEdit").show();
                $("#doneIdEdit").show();
            }
            function clearName(){
            $("#groupNameForEdit").val("");
            }
            function saveEditGroupDetails(groupId){
                var UserIds=new Array;
                var userNames=new Array
                var ulObj=document.getElementById("sortable");
                var groupName=document.getElementById("groupNameForEdit").value;
                var liObj=ulObj.getElementsByTagName("li");
                for(var i=0;i<liObj.length;i++){
                    mbrIds=(liObj[i].id).split("~");
                    UserIds.push(mbrIds[0].replace("_li", "", "gi"));
                    $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
                    userNames.push($(this).find("td").eq(1).html());
                    }
                )
                }
                $.ajax({
                    url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=editGroupDetails&UserIds="+UserIds+"&groupName="+groupName+"&userNames="+userNames+"&groupId="+groupId,
                    success: function(data){
                        window.location.href = window.location.href;
                    }
                });
                $("#logicalGroup").dialog('close')
            }

            function saveNewGroupDetails(){
                var UserIds=new Array;
                var userNames=new Array
                var groupName=document.getElementById("groupName").value;
                if(groupName==''){
                    alert("Please enter Group Name");
                }
                else
                {
                    var ulObj=document.getElementById("sortable");
                    var liObj=ulObj.getElementsByTagName("li");
                    for(var i=0;i<liObj.length;i++){
                        mbrIds=(liObj[i].id).split("~");
                        UserIds.push(mbrIds[0].replace("_li", "", "gi"));
                        $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
                            userNames.push($(this).find("td").eq(1).html());
                        }
                    )
                    }
                    $.ajax({
                        url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=saveNewGroup&UserIds="+UserIds+"&userNames="+userNames+"&groupName="+groupName,

                        success: function(data){
                            window.location.href = window.location.href;
                        }
                    });
                    $("#logicalGroup").dialog('close')

                }
            }


        </script>
        <script  type="text/javascript">
            var xmlhttp;
            function ShowPrivilages(){
                initDialog();
                var i=0;
                var userId;
                var chkusersobj = document.forms.myForm.chkusers;
                for(var j=0;j<chkusersobj.length;j++){
                    if(chkusersobj[j].checked){
                        userId=chkusersobj[j].value;
                        i++;
                    }
                }
                if(i==0){
                    alert("Please Select a User");
                    return false;
                }
                else if(i>1){
                    alert("Please Select only one User");
                    return false;
                }
                else{                   
                    //$.ajax({
                    //url: "<%=request.getContextPath()%>/savePrivilages.do?method=getPrivilages&userId="+userId,
                    //success: function(data){
                    var source="<%=request.getContextPath()%>/savePrivilages.do?method=getPrivilages&userId="+userId;
                    var frameObj=document.getElementById("savePrivilagesdiv");
                    frameObj.src=source;
                    //document.getElementById("savePrivilagesdiv").innerHTML=data;
                    //document.getElementById("savePrivilages").style.display ='block';
                    //}
                    //});
                    $("#reportPriv").dialog('open');
                }
            }
            function afterSaveUser(){
                document.forms.myForm.action="AdminTab.jsp";
                document.forms.myForm.submit();
            }
            function UserReportPreveliges(){
                initDialog();
                var i=0;
                var userId;
                var chkusersobj = document.forms.myForm.chkusers;
                for(var j=0;j<chkusersobj.length;j++){
                    if(chkusersobj[j].checked){
                        userId=chkusersobj[j].value;
                        i++;
                    }
                }

                if(i==0){
                    alert("Please Select a User");
                    return false;
                }
                else if(i>1){
                    alert("Please Select only one User");
                    return false;
                }
                else{
                    var source="<%=request.getContextPath()%>/savePrivilages.do?method=getReportPrevilages&userId="+userId;
                    var frameObj=document.getElementById("reportPrivMFrame");
                    frameObj.src=source;
                    $("#reportPrivM").dialog('open');
                }
            }
            function cancelUserReportPreveliges(){
                $("#reportPrivM").dialog('close');
                unCheckUserBox();
            }
            function UserGraphPreveliges(){
                initDialog();
                var i=0;
                var userId;
                var chkusersobj = document.forms.myForm.chkusers;
                for(var j=0;j<chkusersobj.length;j++){
                    if(chkusersobj[j].checked){
                        userId=chkusersobj[j].value;
                        i++;
                    }
                }

                if(i==0){
                    alert("Please Select a User");
                    return false;
                }
                else if(i>1){
                    alert("Please Select only one User");
                    return false;
                }
                else{
                    var source="<%=request.getContextPath()%>/savePrivilages.do?method=getReportGraphPrevilages&userId="+userId;
                   
                    var frameObj=document.getElementById("reportGraphPrivFrame");
                    frameObj.src=source;
                    $("#reportGraphPriv").dialog('open');
                }
            }
            function cancelUserGraphPreveliges(){

                $("#reportGraphPriv").dialog('close');
                unCheckUserBox();
            }
            function UserTablePreveliges(){
                initDialog();
                var i=0;
                var userId;
                var chkusersobj = document.forms.myForm.chkusers;
                for(var j=0;j<chkusersobj.length;j++){
                    if(chkusersobj[j].checked){
                        userId=chkusersobj[j].value;
                        i++;
                    }
                }
                if(i==0){
                    alert("Please Select a User");
                    return false;
                }
                else if(i>1){
                    alert("Please Select only one User");
                    return false;
                }
                else{
                    var source="<%=request.getContextPath()%>/savePrivilages.do?method=getReportTablePrevilages&userId="+userId;
                   
                    var frameObj=document.getElementById("reportTablePrivFrame");
                    frameObj.src=source;
                    $("#reportTablePriv").dialog('open');
                }
            }
            function cancelUserTablePreveliges(){

                $("#reportTablePriv").dialog('close');
                unCheckUserBox();
            }

            function assignRoles(userid)
            {
                initDialog();
                var i=0;
//                var obj = document.myForm.chkusers;
                // chk1= document.myForm.chk1
                var userId="";
//                if(isNaN(obj.length))
//                {
//                    if(document.myForm.chkusers.checked)
//                    { 
                        userId=userid;
                        document.getElementById("userAssignRoles").innerHTML = "<iframe class=frame1 src=userRoleAssignment.jsp?userId="+userId+" style='width:700px;height:350px' frameborder='0'></iframe>";

                        $("#userAssignRoles").dialog('open');
//                    }
//                    else
//                    {
//                        alert("Please select user to assign Roles")
//                    }
//                }
//                else
//                {
//                    for(var j=0;j<obj.length;j++)
//                    {
//                        if(document.myForm.chkusers[j].checked==true)
//                        {   userId=document.myForm.chkusers[j].value;
//                            i++;
//                            
//                            
//                        }
//                    }
//
//                    if(i>1)
//                    {
//                        alert("Please select only one user to assign Roles");
//                    }
//                    else if(i==0)
//                    {
//                        alert("Please select user to assign Roles");
//                    }
//                    else
//                    {
//                        document.getElementById("userAssignRoles").innerHTML = "<iframe class=frame1 src=userFolderAssignment.jsp?userId="+userId+" style='width:700px;height:350px'  frameborder='0'></iframe>";
//
//                        $("#userAssignRoles").dialog('open');
//                    }
//                }
            }
            function unCheckUserBox(){
                var chkusersobj = document.forms.myForm.chkusers;
                for(var i=0;i<chkusersobj.length;i++){
                    chkusersobj[i].checked=false;
                }
            }
            function newReportGraphPrivileges(){
                initDialog();
                var i=0;
                var userId;
                var chkusersobj = document.forms.myForm.chkusers;
                for(var j=0;j<chkusersobj.length;j++){
                    if(chkusersobj[j].checked){
                        userId=chkusersobj[j].value;
                        i++;
                    }
                }

                if(i==0){
                    alert("Please Select a User");
                    return false;
                }
                else if(i>1){
                    alert("Please Select only one User");
                    return false;
                }
                else{
                    $.ajax({
                        url: "<%=request.getContextPath()%>/savePrivilages.do?method=getReportGraphPrevilages&userId="+userId,
                        success: function(data){
                            document.getElementById("AssignedRepGraphPrivilages").innerHTML=data;
                            //document.getElementById("AssignedRepGraphPrivilages").style.display ='block';
                        }
                    });
                    $("#reportGraphPriv").dialog('open');
                }
            }
            function saveRepGrpPriv(){
                var userId;
                var chkusersobj = document.forms.myForm.chkusers;
                for(var i=0;i<chkusersobj.length;i++){
                    if(chkusersobj[i].checked){
                        userId=chkusersobj[i].value;
                    }
                }

                var grpTypes = document.getElementById("grptypCheck").checked;
                var grpCols = document.getElementById("grpcolsCheck").checked;
                var rowsCheck = document.getElementById("rowsCheck").checked;
                var grpProperties = document.getElementById("grpprpCheck").checked;
                //var editCheck = document.getElementById("editCheck").checked;

                if(grpTypes == true)
                {
                    var grpTypes=document.getElementById("grptypCheck").value //= document.getElementById("linkCheck").value;
                }
                if(grpCols == true)
                {
                    var grpCols=document.getElementById("grpcolsCheck").value //= document.getElementById("composeCheck").value;
                }
                if(rowsCheck == true)
                {
                    var rowsCheck=document.getElementById("rowsCheck").value //= document.getElementById("topCheck").value;
                }
                if(grpProperties == true)
                {
                    var grpProperties=document.getElementById("grpprpCheck").value //= document.getElementById("snapCheck").value;
                }
            <%--if(editCheck == true)
            {
                var editCheck=document.getElementById("editCheck").value //= document.getElementById("snapCheck").value;
            }--%>
                    if(grpTypes == false && grpCols == false && rowsCheck == false && grpProperties == false )
                    {
                        alert('Please Select Atleast One Previlage')
                    }else{
                        document.forms.myForm.action="savePrivilages.do?method=saveRepGraphPrevilages&userId="+userId+"&grpTypes="+grpTypes+"&grpCols="+grpCols+"&rowsCheck="+rowsCheck+"&grpProperties="+grpProperties;
                        document.forms.myForm.submit();
                    }
                }

                function newReportTablePrivileges(){
                    initDialog();
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;
                            i++;
                        }
                    }

                    if(i==0){
                        alert("Please Select a User");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User");
                        return false;
                    }
                    else{
                        $.ajax({
                            url: "<%=request.getContextPath()%>/savePrivilages.do?method=getReportTablePrevilages&userId="+userId,
                            success: function(data){
                                document.getElementById("AssignedRepTablePrivilages").innerHTML=data;
                                // document.getElementById("AssignedRepTablePrivilages").style.display ='block';
                            }
                        });
                        $("#reportTablePriv").dialog('open');
                    }
                }
                function saveRepTabPriv(){
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var i=0;i<chkusersobj.length;i++){
                        if(chkusersobj[i].checked){
                            userId=chkusersobj[i].value;
                        }
                    }

                    var expCheck = document.getElementById("ExpCheck").checked;
                    var hideShowColsCheck = document.getElementById("hideShowColsCheck").checked;
                    var TablePropertiesCheck = document.getElementById("TablePropertiesCheck").checked;
                    var hideTableCheck = document.getElementById("hideTableCheck").checked;
                    var transTableCheck = document.getElementById("transTableCheck").checked;

                    if(expCheck == true)
                    {
                        var expCheck=document.getElementById("ExpCheck").value //= document.getElementById("composeCheck").value;
                    }
                    if(hideShowColsCheck == true)
                    {
                        var hideShowColsCheck=document.getElementById("hideShowColsCheck").value //= document.getElementById("topCheck").value;
                    }
                    if(TablePropertiesCheck == true)
                    {
                        var TablePropertiesCheck=document.getElementById("TablePropertiesCheck").value //= document.getElementById("customCheck").value;
                    }
                    if(hideTableCheck == true)
                    {
                        var hideTableCheck=document.getElementById("hideTableCheck").value //= document.getElementById("snapCheck").value;
                    }
                    if(transTableCheck == true)
                    {
                        var transTableCheck=document.getElementById("transTableCheck").value //= document.getElementById("schedCheck").value;
                    }
                    if(expCheck == false && hideShowColsCheck == false && TablePropertiesCheck == false && hideTableCheck == false && transTableCheck == false)
                    {
                        alert('Please Select Atleast One Previlage')
                    }else{
                        document.forms.myForm.action="savePrivilages.do?method=saveRepTablePrevilages&userId="+userId+"&expCheck="+expCheck+"&hideShowColsCheck="+hideShowColsCheck+"&TablePropertiesCheck="+TablePropertiesCheck+"&hideTableCheck="+hideTableCheck+"&transTableCheck="+transTableCheck;
                        document.forms.myForm.submit();
                    }
                }

                function changeUserType(userId)
                {
                    // alert(' in change type '+userId);
                    initDialog();
                    var frameobj = document.getElementById("modifyUserTypeData");
                    frameobj.src= "modifyUserType.jsp?userId="+userId;
                    $("#modifyUserType").dialog('open');
                }


                function viewByReportUser(userId){

                    initDialog();
                    var frameobj = document.getElementById("viewByFrameuser");
                    frameobj.src= "ViewByReportDetails.jsp?userId="+userId;
                    // frameobj.src= "forATest.jsp";

                    $("#viewByDialoguser").dialog('open');

                }


                function assignPrivileges(){
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;
                            i++;
                        }
                    }
                    if(i==0){
                        alert("Please Select a User");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User");
                        return false;
                    }
                    else{
                        initDialog();
                        
                        $("#superAdminUserDiv").dialog('open');
                        $("#imgDiv").show();
                        $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=initializeUserLicenseModules&userId="+userId,
                            success: function(data){
                                  
                                  var frameobj = document.getElementById("superAdminUserFrame");
                                  frameobj.src= "<%=request.getContextPath()%>/SuperAdmin/UserModuleAssignment.jsp?userId="+userId;
                                   $("#imgDiv").hide();
                                 }
                        });

                       

                    }

                }
                function logout(){
                  document.forms.myForm.action="<%=request.getContextPath()%>/baseAction.do?param=logoutApplication";
                  document.forms.myForm.submit();
                }
                function closeDialog(){
                //alert("close")
                  $('#superAdminUserDiv').dialog('close')

                }


                function modifyUType()
                {
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;
                            i++;
                        }
                    }
                    if(i==0){
                        alert("Please Select a User");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User");
                        return false;
                    }
                    else{
                        initDialog();
                        var frameobj = document.getElementById("modifyUserTypeData");
                        frameobj.src= "modifyUserType.jsp?userId="+userId;
                        $("#modifyUserType").dialog('open');
                    }
                }
                function orderBYsel(){
                    var selval=document.getElementById("sortBySel").value;
                    var seloption;
                    var optionobj = document.forms.myForm.sortOption;
                    for(var i=0;i<optionobj.length;i++){
                        if(optionobj[i].checked){
                            var seloption=optionobj[i].value
                        }
                    }
                    $.ajax({
                        url: "sortTabdetails.do?param=sortUserlist&selval="+selval+"&seloption="+seloption,
                        success: function(data){


                        }
                    });
                    document.forms.myForm.action="AdminTab.jsp#User_Creation";
                    document.forms.myForm.submit();

                }
                function modifyCompany(){
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;
                            i++;
                        }
                    }
                    if(i==0){
                        alert("Please Select a User");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User");
                        return false;
                    }
                    else{
                        initDialog();
                        var frameobj = document.getElementById("modifyCompanyData");
                        frameobj.src= "modifyCompany.jsp?userId="+userId;
                        $("#modifyCompany").dialog('open');
                    }
                }
                function cancelModifyCompany(){
                   // alert('in cancelmodifycompany')
                    $("#modifyCompany").dialog('close');
                }
                function ResetPassword(){
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;
                            i++;
                        }
                    }
                    if(i==0){
                        alert("Please Select a User");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User");
                        return false;
                    }
                    else{
                        /* $.ajax({
                            url: "<%//=request.getContextPath()%>/passwordAction.do?pswrdparam=verifyResetPasswrod&userId="+userId,
                            success: function(data){
                                if(data=='success'){*/
                        initDialog()
                        $("#resetpswrddiv").dialog('open');
                        //alert('in else')
                        var frameObj = document.getElementById("resetpswrdframe");
                        var source = '<%=request.getContextPath()%>/ResetPassword.jsp?userId='+userId;
                        frameObj.src = source;
                        // alert(frameObj.src);
                        /* }else{
                                    alert("This is not Admin User No privilage to Reset Password");
                                    return false;
                                }
                            }
                        });*/

                    }
                }
                function closeRestepswrd(){
                    $("#resetpswrddiv").dialog('close');
                }

                function expireUser(){
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;
                            i++;
                        }
                    }
                    if(i==0){
                        alert("Please Select a User");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User");
                        return false;
                    }else{
                        $.ajax({
                            url: 'organisationDetails.do?param=expireUser&userId='+userId,
                            success: function(data) {
                                if(data==1){
                                    document.forms.myForm.action = "<%=request.getContextPath()%>/AdminTab.jsp";
                                    document.forms.myForm.submit();
                                }else{
                                    alert('User already Expired')
                                }
                            }
                        });
                    }
                }
                function ResetExpiryDate(){
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.myForm.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;
                            i++;
                        }
                    }
                    if(i==0){
                        alert("Please Select a User");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User");
                        return false;
                    }else{
                        initDialog();
                        var frameobj = document.getElementById("resetExpiryData");
                        frameobj.src= "<%=request.getContextPath()%>/pbResetExpiryDate.jsp?userId="+userId+"&userFlag=Y";
                        $("#resetExpiry").dialog('open');
                    }
                }
                function showLicensePrivileges()
                {
                   initDialog();
                   $("#showLicensesDiv").dialog('open');
                   $("#showLicensesDiv").html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                   $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=showSuperAdminLicensePrivileges",
                            success: function(data){
                                 
                                  $("#showLicensesDiv").html(data)
                                 
                                 }
                        });
                }
                function closeLicenseDiv(){
                  $("#showLicensesDiv").dialog('close');
                }
                function Assignment(moduleName){
                    initDialog();
                  $("#portalAssignmentDiv").dialog('open');
                  modulename = moduleName;
                  var licenceCnt=0;
                  if(moduleName=="Portal Assignment")
                      licenceCnt='<%=portalCount%>'
                  else if(moduleName=="QueryStudio Assignment")
                      licenceCnt='<%=qdCount%>'
                  else if(moduleName=="SuperAdmin Assignment")
                      licenceCnt='<%=superadmincount%>'
                  else if(moduleName=="PowerAnalyzer Assignment")
                      licenceCnt='<%=powerAnaylzerCount%>'
                  else if(moduleName=="One View Assignment")
                      licenceCnt='<%=onviewCount%>'
                  else if(moduleName=="Scorecards Assignment")
                      licenceCnt='<%=scorecardCount%>'
                  $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=showPortalAssignment&moduleName="+moduleName,
                            success: function(data){
                                        var jsonVar=eval('('+data+')')
                        $("#portalAssignmentDiv").html("")
                        if(moduleName=="QueryStudio Assignment")
                        $("#portalAssignmentDiv").html(jsonVar.htmlStr).append("<table align='center' width='100%'><tr><td align='center'><input type='button' value='Done' class='navtitle-hover' onclick='updateAssignment()'></td></tr><tr><td align='center'>Available License Count for Admin Assignment '"+licenceCnt+"'</td></tr></table>");
                         else
                        $("#portalAssignmentDiv").html(jsonVar.htmlStr).append("<table align='center' width='100%'><tr><td align='center'><input type='button' value='Done' class='navtitle-hover' onclick='updateAssignment()'></td></tr><tr><td align='center'>Available License Count for ''"+moduleName+"'' \t\t '"+licenceCnt+"'</td></tr></table>");
                                             
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
                function updateAssignment(moduleName){                 
                 var UserIds=new Array;
                 var userNames=new Array

                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                var flag="true";
                for(var i=0;i<liObj.length;i++){
//                     var content
                    mbrIds=(liObj[i].id).split("~");
                   UserIds.push(mbrIds[0].replace("_li", "", "gi"));
                   $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
                     userNames.push($(this).find("td").eq(1).html());
                    }
                    )
                 }
                    if(modulename=="QueryStudio Assignment"){
                           if(UserIds.length<1){
                             alert("Atleast 1 Admin User has to be Assigned")
                           }
                           else{
                              var curUserId='<%=userIdStr%>'
                              var loginName='<%=loginName.toUpperCase()%>'
                              if($.inArray(curUserId, UserIds)>-1 || loginName=="PROGEN"){
                              flag="true"                               
                              updateAssignmentFun(UserIds,userNames,flag); // if current user availble means flag is true only for qd
                            }
                            else{
                              flag="false"
                              updateAssignmentFun(UserIds,userNames,flag); // if current user availble means flag is false only for qd
                              //logout();
                            }
                            }
                       }
                       else{
                          flag="true"
                           updateAssignmentFun(UserIds,userNames,flag);
                       }
           
            
                }
                function updateAssignmentFun(UserIds,userNames,flag){                
                 $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=validateAssignments&moduleName="+modulename+"&UserIds="+UserIds+"&userNames="+userNames,
                            success: function(data){                               
                                if(data=="true"){
                                    $("#portalAssignmentDiv").dialog('close');
                                  $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=saveAssignments&moduleName="+modulename+"&UserIds="+UserIds+"&userNames="+userNames,
                            success: function(data){
                                
                                if(flag=="false")
                                    logout();
                                else
                                    window.location.href = window.location.href;

                                //alert("Refresh The User Page To View The Changes")
                            }
                              });
                                }
                                else{
                                    alert("you have exceeded the number of Licenses purchased \n  Please Conatact System Adminstrator")
                                }
                            }
                     });
                }
                function updateUserStatus(status){
               initDialog();
               var tablecode = "";
               $("#portalAssignmentDiv").dialog('open');
               userStatus = status;
               $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=getUserStatusDets&status="+status,
                            success: function(data){
                      var jsonVar=eval('('+data+')')
                        $("#portalAssignmentDiv").html("")
                        $("#portalAssignmentDiv").html(jsonVar.htmlStr).append("<table align='center'><tr><td align='center'><input type='button' value='Done' class='navtitle-hover' onclick='updateUserActivation()'></td></tr><tr><td>Please Do Not Disturb The RightSide Dragged Values</td></tr></table>");
                        
                      
                        isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
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
                        //                        grpColArray=jsonVar.memberValues
                        $(".sortable").sortable();
                                 
                                 
                                 }
                                   
                        });
                    
                }
                   function changeusertypename(usertype,userId){
                            initDialog();
                            $('#iduser').val(userId);
                            $('#oldusertypename').val(usertype);
                          if(usertype=='Admin'){
                              $('select#usertypename').find("option#Admin").attr("selected",true);
                              }
                              else if(usertype=='Power Analyzer'){
                                  $('select#usertypename').find("option#Power Analyzer").attr("selected",true);
                              }else if(usertype=='Restricted Power Analyzer'){
                                  $('select#usertypename').find("option#Restricted Power Analyzer").attr("selected",true);
                              }else{
                                $('select#usertypename').find("option#Analyzer").attr("selected",true);
                              }
                          $("#usertypenew").dialog('open');
                }
                                 
               function updateuser()
                {
                    var oldusertype=$('#oldusertypename').val();
                   var a= $('#usertypename').val();
                   var id=$('#iduser').val();
                   $.ajax({
                    url: "superAdminAction.do?superAdminParam=usertypevalidate&Usertype="+a+"&Userid="+id+"&oldusertype="+oldusertype,
                    success: function(data){
//                        if(data!='true'){
//                            alert("Admin already Exists");
//                            window.location.href=window.location.href;
//                        }
//                        else{
                            alert("User Type Updated Successfully");
                            $("#usertypenew").dialog('close');
                            window.location.href=window.location.href;
//                        }
                    }
                });
                    
                }
              function updateUserActivation(){
              $("#portalAssignmentDiv").dialog('close');
              
                  var UserIds=new Array;
                 var userNames=new Array

                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");

                for(var i=0;i<liObj.length;i++){
//                     var content
                    mbrIds=(liObj[i].id).split("~");
                   UserIds.push(mbrIds[0].replace("_li", "", "gi"));
                   $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
                     userNames.push($(this).find("td").eq(1).html());
                    }
                    )
            }
            if(userStatus=="activateUser"){
                $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=validateUserActivation&UserIds="+UserIds+"&userNames="+userNames,
                            success: function(data){
                                if(data=="true"){
                                   $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=saveUserActivation&status="+userStatus+"&UserIds="+UserIds+"&userNames="+userNames,
                            success: function(data){
                                alert("Refresh The User Page To View The Changes")
                            }
            });
                                }
                                else{
                                    alert("You Exceeded The Limit Of Users Please Conatact Your System Admin")
                                }
                            }
            });
            }
            else{
            $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=saveUserActivation&status="+userStatus+"&UserIds="+UserIds+"&userNames="+userNames,
                            success: function(data){
                                alert("Refresh The User Page To View The Changes")
                            }
            });
            }
                }
                function goBiManager(path){
               window.location.href=path;
            }

            function renameUser1(userName,userId){
                    var chkusersobj = document.forms.myForm.chkusers;
                        initDialog()
                        $("#renameUserdiv").dialog('open');
                        $("#oldName").val(userName);
                        $("#renamesubmit").val(userId);

                }

                function createNewNameForUser(){
                var userid = document.getElementById("renamesubmit").value;
                var newName = document.getElementById("newName").value;
                var oldName = document.getElementById("oldName").value;
                     $.ajax({
                        url: "<%=request.getContextPath()%>/passwordAction.do?pswrdparam=updateUserName&newName="+newName+"&oldName="+oldName+"&userid="+userid,
                        success: function(data){
                            if(data=="success"){
                                 $("#renameUserdiv").dialog('close');
                               window.location.href=window.location.href;
                            }
                            else{
                                alert("Invalid entries");
                            }
                        }
                    });
                }


            function suspendUser(userId){
                initDialog()
                $("#modal_dialog").dialog('open');
                $('#btnYes').click(Yes);
                $('#btnNo').click(No);
                 function Yes() {
                $("#modal_dialog").dialog('close');
               $.ajax({
                       url: "<%=request.getContextPath()%>/baseAction.do?param=suspendUser&userId="+userId,
                        success: function(data){
                           if(data=='success'){
                               window.location.href = window.location.href;
                           }
                        }
                    });
                }
                function No() {
               $("#modal_dialog").dialog('close');
             }
            }
            function activateUser(userId){
                  $.ajax({
                        url: "<%=request.getContextPath()%>/baseAction.do?param=activateUser&userId="+userId,
                        success: function(data){
                           if(data=='success'){
                               alert("User Activated successfully");
                               window.location.href = window.location.href;
                           }
                           else{
                               alert("Users Limit Exceed");
                           }
                        }
                    });
            }
            function ParallelUsage(){
                $.ajax({
                        url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=checkParallelUsage",
                        success: function(data){
                               alert(data);
                               window.location.href = window.location.href;
                        }
                    });
            }

        </script>
        <style type="text/css">
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }
            .black_overlay{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:hidden;
            }
            .startWhite{
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:450px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .startpage {
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:450px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_start{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 120%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }      
            a:hover{text-decoration:underline;font-weight:normal}

            .startpage {
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:400px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_start{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 150%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }

            #hyperType:hover{text-decoration:underline}
        </style>

    </head>

    <script type="text/javascript">
        function loadpage(){

            //document.getElementById("<%=sortOptionvalue%>").checked=true;
            // document.getElementById("sortBySel").value=<%=sortValue%>
        }
    </script>
    <body id="mainBody" onload="loadpage()">
        <script type="text/javascript">
            $(document).ready(function(){
                $("#tablesorterUserList")
                .tablesorter({headers : {0:{sorter:false}}})
                .tablesorterPager({container: $('#pagerUserList')})
                $.get('organisationDetails.do?param=checkActiveKey', function(data) {
                    cActiveVal=data
                    disableButton(data)
                   
                });

            });

            function disableButton(val){
                if(val=="true"){
                    document.getElementById("creatUser").style.display=''
                }else{
                    document.getElementById("creatUser").style.display='none'
                }
            }
        </script>

           <div align="center" id="logicalGroup"  style="display:none;width:100%;height:100%;position:centre;" title="Assign Report to Users">
              <table style="width:100%">
               <tr><td align="right" width="50%">
                        <input id="biManager" class="navtitle-hover" style="width:auto" type="button" value="Home" onclick="goBiManager('pbBIManager.jsp')">
                   </td></tr> </table>
               <table style="width:70%">
                    <tr>
                        <td  style="height:10px;width:20%" align="right">
                        </td>
                    </tr>
                    <tr>
                    </tr>

                        <tr id="CreateGroupName" style="display:none;">
                        <td style="color: #000000;font-size:12px;"><span style="color:red">*</span>Enter Group Name&nbsp;&nbsp;&nbsp;
                        <input id="groupName" name="groupName" style="width:auto" type="text"></td>
                    </tr>
                    <table id="draggedTabledata" border="1" width="70%" style="height:120px;display:none;" valign="top" align="center">
                    </table>
                </table>
            <div id="doneIdCreate"></div>
                        </div>

                    <div id="selectTest"  style="display:none;width:100%;height:100%;">
                         <table style="width:100%">
               <tr><td align="right" width="50%">
                        <input id="biManager" class="navtitle-hover" style="width:auto" type="button" value="Home" onclick="goBiManager('pbBIManager.jsp')">
                   </td></tr> </table>
                            <table  align="center" width="50%">
                                <tr><td width="30%" style="color: #000000;font-size:12px;" align="left">Select Group For Edit</td>
                                <td><select name="selectgroup" id="selectgroup">
                            <%
                                        String queryList1 = "select * from PRG_AR_LOGICGROUP_MASTER";
                                        PbReturnObject grouplist1 = null;
                                        grouplist1 = pbdb.execSelectSQL(queryList1);
                            %>
                            <option selected id="selectBox" onclick="clearName()" value="none" >none</option>
                            <%
                                        for (int i = 0; i < grouplist1.getRowCount(); i++) {
                                            String str1 = grouplist1.getFieldValueString(i, 1);
                                            String strId1 = grouplist1.getFieldValueString(i, 0);
                            %>
                     <option id="<%=strId1%>" value="<%=str1%>" onclick="EditGroupList(this.id,this.value)"><%=str1%></option>
                            <% }%>
                        </select></td>
                                <td id="editGroupName" style="display:none;font-family:verdana;color:#000000;font-size:12px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    Edit Group Name&nbsp;&nbsp;&nbsp;
                        <input id="groupNameForEdit" value="" name="groupName"   type="text"></td>
                                </tr></table>
                        <table id="draggedTabledata2" border="1" width="70%" style="height:120px;display:none;" valign="top" align="center">
                </table>
                         <div id="doneIdEdit"></div>
                        </div>

        <div id="userListDiv">
        <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>
        <form name="myForm"  method="post" action="">
            <table id="sortBytable" style="width:100%">
                <tr>
                    <td align="left" width="50%">
                        <div id="pagerUserList" class="pager" align="left" >
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option value="5">5</option>
                                <option selected value="10">10</option>
                                <option value="<%=list.getRowCount()%>">All</option>
                            </select>
                        </div>
                    </td>
                    <td align="right" width="50%">
                        <input id="biManager" class="navtitle-hover" style="width:auto" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("home", cle)%>" onclick="goBiManager('pbBIManager.jsp')">
                    <input id="creatUser" class="navtitle-hover" style="width:auto" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("Create_User", cle)%>" onclick="createUser();">
                    <input id="checkParallel" class="navtitle-hover" style="width:auto" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("Parallel_Usage", cle)%>" onclick="ParallelUsage();">
<!--                     <a href="javascript:void(0)">&nbsp;&nbsp;Check Parallel Usage</a>-->
                    </td>
<%--                    <td align="right" width="50%">
                        <table width="100%">
                            <tr>
                                <td class="wordStyle" align="right" width="20%" style="font-weight:bold;">Sort by :
                                    <select id="sortBySel" name="sortBySel" onchange="orderBYsel()">
                                        <%
                                                    if (!sortValue.equalsIgnoreCase("")) {%>
                                        <script language="text/javascript">
                                            var sVal=document.getElementById('<%=sortValue%>').text;
                                            var optioObje=document.getElementById("dispItem1");
                                            optioObje.value='<%=sortValue%>';

                                            optioObje.text=sVal;
                                            document.getElementById('<%=sortValue%>').style.display='none';
                                        </script>
                                        <option id="dispItem1" value=""> </option>
                                        <option id="pu_start_date" value="pu_start_date" >Created Date</option>
                                        <option id="pu_login_id" value="pu_login_id">User Id</option>
                                        <option id="pu_firstname" value="pu_firstname">First Name</option>
                                        <option id="pu_lastname" value="pu_lastname">Last Name</option>
                                        <option id="pu_email" value="pu_email">Email</option>
                                        <option id="organisation_name" value="organisation_name">Company Name</option>
                                        <option id="USER_TYPE_NAME" value="USER_TYPE_NAME">User Type Name</option>
                                        <option id="pu_end_date" value="pu_end_date">Expiry Date</option>

                                        <%} else {%>
                                        <option id="pu_start_date" value="pu_start_date" >Created Date</option>
                                        <option id="pu_login_id" value="pu_login_id">User Id</option>
                                        <option id="pu_firstname" value="pu_firstname">First Name</option>
                                        <option id="pu_lastname" value="pu_lastname">Last Name</option>
                                        <option id="pu_email" value="pu_email">Email</option>
                                        <option id="organisation_name" value="organisation_name">Company Name</option>
                                        <option id="USER_TYPE_NAME" value="USER_TYPE_NAME">User Type Name</option>
                                        <option id="pu_end_date" value="pu_end_date">Expiry Date</option>


                                        <%}%></select>
                                </td>

                                <td class="wordStyle" align="right" width="10%" style="font-weight:bold;" valign="top">
                                    Sort Option :
                                </td>
                                <td align="right" width="1%" valign="top">
                                    <%
                                                if (sortOptionvalue.equalsIgnoreCase("ASC")) {
                                    %>
                                    <input type="radio" id="asc" name="sortOption" checked value="ASC" onchange="orderBYsel()">
                                    <%} else {%>
                                    <input type="radio" id="asc" name="sortOption"  value="ASC" onchange="orderBYsel()">
                                    <%}%>
                                </td>
                                <td class="wordStyle" align="right" width="4%" valign="top">Ascending</td>

                                <td align="right" width="1%" valign="top">
                                    <%
                                                if (sortOptionvalue.equalsIgnoreCase("DESC")) {
                                    %>
                                    <input type="radio" id="desc" name="sortOption" checked value="DESC" onchange="orderBYsel()">
                                    <%} else {%>
                                    <input type="radio" id="desc" name="sortOption" value="DESC" onchange="orderBYsel()">
                                    <%}%>
                                </td>
                                <td class="wordStyle" align="right" width="4%" valign="top">Descending</td>
                            </tr>
                        </table>
                    </td>--%>
                </tr>
            </table>

            <table align="center" id="tablesorterUserList" class="tablesorter"  style="width:98%" cellpadding="0" cellspacing="1">
                <thead>
                    <tr> <th>&nbsp;</th>
                        <th><%=TranslaterHelper.getTranslatedInLocale("User_Id", cle)%></th>
                        <th><%=TranslaterHelper.getTranslatedInLocale("First_Name", cle)%></th>
                        <th><%=TranslaterHelper.getTranslatedInLocale("Last_Name", cle)%></th>
                        <%--<th>Email</th>--%>
<!--                        <th>Company Name</th>-->
                        <th><%=TranslaterHelper.getTranslatedInLocale("User_Type_Name", cle)%></th>
                        <th><%=TranslaterHelper.getTranslatedInLocale("User_State", cle)%></th>
                        <th><%=TranslaterHelper.getTranslatedInLocale("Creation_Date", cle)%></th>
                        <th><%=TranslaterHelper.getTranslatedInLocale("Last_Modified", cle)%></th>
<!--                        <th>User Status</th>-->
                        <th><%=TranslaterHelper.getTranslatedInLocale("Edit_Roles", cle)%></th>
                        <th><%=TranslaterHelper.getTranslatedInLocale("Edit_Reports", cle)%></th>
                       
<%--                        <th>Assigned Roles/Reports</th>--%>
                    </tr>
                </thead>
                <tbody>
                    <%int i = 0;
                                for (i = 0; i < list.getRowCount(); i++) {
                                    String status = "";
                                    status = list.getFieldValueString(i, 24);
                                    /*
                                    if (status.equalsIgnoreCase("Active")) {

                                    PbReturnObject compExpObj =null;
                                    String userQry="select pu_id from prg_ar_users";
                                    PbReturnObject userObj = pbdb.execSelectSQL(userQry);

                                    String compExpQry = "select organisation_name,case when sysdate-nvl(org_end_date,sysdate)<=0 then 'Active' else 'Expired' end as comp_status from prg_org_master where org_id=" + list.getFieldValueString(i, "ACCOUNT_TYPE");
                                    ////.println("compExpQry==="+compExpQry);
                                    compExpObj = pbdb.execSelectSQL(compExpQry);
                                    for (int j = 0; j < compExpObj.getRowCount(); j++) {
                                    status = (compExpObj.getFieldValueString(j, "COMP_STATUS"));
                                    }
                                    }
                                     */

         PbBaseDAO dao1=new PbBaseDAO();
         String queryadd = null;
         PbReturnObject retObjcheck=null;
         int userIdadd = Integer.parseInt(list.getFieldValueString(i, 0));
         queryadd = "select * from PRG_USER_ASSIGNMENTS where USER_ID="+userIdadd;
         retObjcheck=pbdb.execSelectSQL(queryadd);
                   if(adminType.equalsIgnoreCase("SUPERADMIN") || !retObjcheck.getFieldValueString(0, 1).equalsIgnoreCase("PROGEN")){
                         %>
                    <tr>
                        <%if (list.getFieldValueString(i, 1).equalsIgnoreCase("progen") && !loginName.equalsIgnoreCase("progen")) {%>
                        <td style="width:20px">
                            <input type="checkbox" id="chkusers" name="chkusers" value="<%=list.getFieldValueInt(i, 0)%>" disabled>
                        </td>
                        <%} else {%>
                        <td style="width:20px">
                            <input type="checkbox" id="chkusers" name="chkusers" value="<%=list.getFieldValueInt(i, 0)%>">
                        </td>
                        <%}%>
                        <td valign="top" style="width:100px">
                           <a style="font-size:11px;" href="javascript:renameUser1('<%=list.getFieldValueString(i, 1)%>','<%=list.getFieldValueString(i, 0)%>')">
                            <%=list.getFieldValueString(i, 1)%>
                        </a>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, 2)%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, 4)%>
                        </td>
                        <%--<td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, 7)%>
                        </td>--%>
<%--                       <td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, "ORGANISATION_NAME")%>
                        </td>--%>
                        <td valign="top" style="width:110px" >
<%--                            <a style="font-size:11px;" href="javascript:changeUserType('<%=list.getFieldValueInt(i, 0)%>')"><%=list.getFieldValueString(i, "USER_TYPE_NAME")%></a>--%>
                        <%
                        //Modify by Ashutosh
                        if(list.getFieldValueString(i, 29).equalsIgnoreCase("Y")){
                            userType = "Admin";
                        }else if(list.getFieldValueString(i,28).equalsIgnoreCase("Y")){
                            userType ="Power Analyzer";
                        }else{
                            userType = "Analyzer";            
                        }
                        if(list.getFieldValueString(i,30).equalsIgnoreCase("Y")){
                            userType ="Restricted Power Analyzer";
                        }
                          if(list.getFieldValueString(i, 29).equalsIgnoreCase("Y") && list.getFieldValueString(i, 25).equalsIgnoreCase("Y")){
                              userType = "Admin";
                              userType1="Power Analyzer";
                          }else{
                            userType1=null;
                          }
                        
                                    
                        %>
                        <a style="font-size:11px;" href="javascript:changeusertypename('<%=userType%>','<%=list.getFieldValueString(i, 0)%>')">
                        <%=userType%>                        
                        </a>
                        <%if(userType1!=null){%>
                        ,<a style="font-size:11px;" href="javascript:changeusertypename('<%=userType1%>','<%=list.getFieldValueString(i, 0)%>')">
                        <%=userType1%>
                        <%}%>
                        </a>
                        </td>
                       <%
                   PbBaseDAO dao=new PbBaseDAO();
                   PbReturnObject retObj1=dao.getUserActiveState(list.getFieldValueString(i, 0));
     String query2 = null;
     PbReturnObject retObj=null;
     int userId = Integer.parseInt(list.getFieldValueString(i, 0));
     query2 = "select * from PRG_USER_ASSIGNMENTS where USER_ID="+userId;
     retObj=pbdb.execSelectSQL(query2);
                   String userState =  retObj1.getFieldValueString(0,0);
                   if(retObj.getFieldValueString(0, 1).equalsIgnoreCase("PROGEN") || retObj.getFieldValueString(0, 12).equalsIgnoreCase("Y")){  %>
                   <td align="center" style="width:110px">Active</td>
               <% } else {
               if(userState.equalsIgnoreCase("Y")){
                %>

                <td align="center" style="width:110px"><a href="javascript:void(0)" onclick="suspendUser(<%=list.getFieldValueString(i, 0)%>)">Active</a></td>
                <% }
                   else{
                %>
                <td align="center" style="width:110px"><a href="javascript:void(0)" onclick="activateUser(<%=list.getFieldValueString(i, 0)%>)">InActive</a></td>
                <% } } %>
                        <td valign="top" style="width:100px">
                            <%if (list.getFieldValueString(i, 16).equalsIgnoreCase(null) || "".equalsIgnoreCase(list.getFieldValueString(i, 16))) {%>
                            <%=""%>
                            <%} else {%>
                            <%=list.getFieldValueDateString(i, 16)%>
                            <%}%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%if (list.getFieldValueString(i,20).equalsIgnoreCase(null) || "".equalsIgnoreCase(list.getFieldValueString(i, 20))) {%>
                            <%=list.getFieldValueDateString(i,15)%>
                            <%} else {%>
                            <%=list.getFieldValueDateString(i,20)%>
                            <%}%>
                        </td>
<!--                        <td valign="top" style="width:100px" >
                            <%
                            if(list.getFieldValueString(i, 15).charAt(0)=='Y'){
                                status="Active";
                            }else{
                                 status = "Inactive";       
                            }
                            %>
                            <%=status%>
                        </td>-->
<!--   commented for spanco and enhanced it                     <td valign="top" style="width:100px">
                            <a style="font-size:11px;" href="javascript:viewByReportUser('<%=list.getFieldValueInt(i, 0)%>')">View</a>
                        </td>-->
                     <td valign="top" style="width:60px"><a class="ui-icon ui-icon-pencil" style="font-size:11px;" href="javascript:assignRoles('<%=list.getFieldValueInt(i, 0)%>')" title="Manage Roles"></a></td>
                    <td valign="top" style="width:60px"><a class="ui-icon ui-icon-plus" style="font-size:11px;" href="javascript:reportToUser('<%=list.getFieldValueInt(i, 0)%>')" title="Manage Reports"></a></td>
                    </tr>
                    <%
                    }
                    }%>
                </tbody>
            </table>
            <input type="hidden" name="limit" id="limit" value="<%=i%>">
            <input type="hidden" id="selecteduser" name="selecteduser">
            <input type="hidden" name="userId" id="userId">
            <br><br>
            <table align="center">
<!--                commented for spanco-->
<!--                <tr>
                    <Td></Td>
                    <%if (list.getRowCount() == 0) {%>
                    <Td><input id="creatUser" class="navtitle-hover" style="width:auto" type="button" value="Create User" onclick="createUser();"><Td>
                        <%} else {%>
                    <Td><input id="creatUser" class="navtitle-hover" style="width:auto" type="button" value="Create User" onclick="createUser();"><Td>
                        <%--  <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Edit" onclick="checkEdit();"></Td>--%>
                    <Td><Input class="navtitle-hover" style="width:auto" type="button" value="Delete" onclick="checkDelete();"></Td>-->
<!--  <Td><Input class="navtitle-hover" style="width:auto" type="button" value="Assign Business Roles" onclick="assignRoles();"></Td>-->
                  <!--   <Td><Input class="navtitle-hover" style="width:auto" type="button" value="Expire User" onclick="expireUser();"></Td>
                </tr>-->

                        <tr>
                            
                        </tr>
            </table><table id="Assignstartnew1"><tr>
<!--                    <Td align="right"><input id="creatUser" class="navtitle-hover" style="width:auto" type="button" value="Create User" onclick="createUser();"><Td>-->
                    <td><input class="navtitle-hover" style="width:auto" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("Start_Page", cle)%>" onclick="Assignstartnew()"></td>
                        <%--<Td ><Input  class="navtitle-hover" style="width:auto" type="button" value="Assign Privileges" onclick="ShowPrivilages();"></Td>--%>
                        <%--<td><input class="navtitle-hover" style="width:auto" type="button" value="Report Privileges" onclick="UserReportPreveliges()"></td>--%>
<!--                    <td><input class="navtitle-hover" style="width:auto" type="button" value="Reset Password" onclick="ResetPassword()"></td>-->
<!-- commented for spanco    <td><input class="navtitle-hover" style="width:auto" type="button" value="Reset Expiry Date" onclick="ResetExpiryDate()"></td>-->
                        <%-- <Td><Input  class="navtitle-hover" style="width:auto" type="button" value="Temp" onclick="temp();"></Td>--%>
<!-- commented for spanco <Td><Input class="navtitle-hover" style="width:auto" type="button" value="Modify Company" onclick="modifyCompany();"></Td>-->
                </tr>
                
            </table>
            <table><tr>
                    <%--<Td ><Input class="navtitle-hover" style="width:auto" type="button" value="Report Graph Privileges" onclick="UserGraphPreveliges();"></Td>--%>
                    <%--<td><input class="navtitle-hover" style="width:auto" type="button" value="Report Table Privileges" onclick="UserTablePreveliges()"></td>--%>
<!-- commented for spanco <td><input class="navtitle-hover" style="width:auto" type="button" value="Assign Reports To User" onclick="reportToUser()"></td>-->
<!-- commented for spanco <td><input class="navtitle-hover" style="width:auto" type="button" value="Modify User Type" onclick="modifyUType()"></td>-->
<!--                    <td><input class="navtitle-hover" style="width:auto" type="button" value="Assign Privileges" onclick="assignPrivileges()"></td>-->
                        <%-- <Td><Input  class="navtitle-hover" style="width:auto" type="button" value="Temp" onclick="temp();"></Td>--%>
                        <%}%>
                </tr>
            </table>
                <table>
                    <tr>
<!--                        <input class="navtitle-hover" style="width:auto" type="button" value="Show License Details" onclick="showLicensePrivileges()">-->
                    </tr>
                </table>
                <table align="center">
                            <tr>
                <table id="buttonField"><tr>
                              
                            <td><input class="navtitle-hover" style="width:auto" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("Reset_Password", cle)%>" onclick="ResetPassword()"></td>
<!--                               <td>
                                 <input type="button" class="navtitle-hover" id="activateUser" name="Activate User" value ="Activate User" onclick="updateUserStatus(this.id)"/>  
                               </td>
                               <td align="left">
                                 <input type="button" class="navtitle-hover" id="inactivateUser"  name="Inactivate User" value ="Inactivate User" onclick="updateUserStatus(this.id)"/>  
                               </td>-->
<%
                               if(adminType.equalsIgnoreCase("SUPERADMIN")){
                                %>
                               <td>
                                 <input type="button" class="navtitle-hover" id="SuperAdmin Assignment" name="Super Admin Assignment" value ="<%=TranslaterHelper.getTranslatedInLocale("Super_Admin_Assignment", cle)%>" onclick="Assignment(this.id)"/>
                               </td><% } %>
                               <td>
                                 <input type="button" class="navtitle-hover" id="QueryStudio Assignment" name="Admin Assignment" value ="<%=TranslaterHelper.getTranslatedInLocale("Admin_Assignment", cle)%>" onclick="Assignment(this.id)"/>
                               </td>

                               <td align="left">
                                 <input type="button" class="navtitle-hover" id="PowerAnalyzer Assignment"  name="Power Analyzer Assignment" value ="<%=TranslaterHelper.getTranslatedInLocale("Power_Analyzer_Assignment", cle)%>" onclick="Assignment(this.id)"/>
                               </td>
                              </tr> </table>
                           </tr>                           
                        </table>
<!--                 <tr>
                               <table><tr>
                               <td>
                                 <input type="button" class="navtitle-hover" id="Portal Assignment" onclick="Assignment(this.id)" name="Portal Viewer Assignment" value ="Portal Viewer Assignment" />
                               </td>
                                <td>
                                    <input type="button" class="navtitle-hover" id="headlineAssignment" onclick="Assignment(this.id)" name="Headlines Assignment" value ="Headlines Assignment" />  
                               </td>
                               <td>
                                 <input type="button" class="navtitle-hover" id="whatifassignment" onclick="Assignment(this.id)" name="What-if Assignment" value ="What-if Assignment" />  
                               </td>
                               <td>
                                    <input type="button" class="navtitle-hover" id="One View Assignment" onclick="Assignment(this.id)" name="One View Assignment" value ="One View Assignment" />
                               </td>
                               <td>
                                 <input type="button" class="navtitle-hover" id="Scorecards Assignment" onclick="Assignment(this.id)" name="Scorecards Assignment" value ="Scorecards Assignment" />
                               </td>
                              </tr> </table>
                           </tr>-->
            <%--</div>--%>
            <br>
            <div id="reportPriv" title="Assign Privileges" style="display: none;">
                <%--<div id="savePrivilagesdiv"></div>--%>
                <iframe src="#" scrolling="no"  height="100%" width="100%" frameborder="0" id="savePrivilagesdiv"></iframe>
            </div>
            <div id="reportPrivM" title="Report Privileges" style="display: none;">
                <iframe src="#" scrolling="no"  height="100%" width="100%" frameborder="0" id="reportPrivMFrame"></iframe>
            </div>
            <div id="reportGraphPriv" title="Graph Privileges" style="display: none;">
                <iframe src="#" scrolling="no"  height="100%" width="100%" frameborder="0" id="reportGraphPrivFrame"></iframe>
            </div>
            <div id="reportTablePriv" title="Report Table Privileges" style="display: none;">
                <iframe src="#" scrolling="no"  height="100%" width="100%" frameborder="0" id="reportTablePrivFrame"></iframe>
            </div>
            <div id="logicalGroupPriv" title="Report Table Privileges" style="display: none;">
                <iframe src="#" scrolling="no"  height="100%" width="100%" frameborder="0" id="reportTablePrivFrame"></iframe>
            </div>
            <div id="fadePriv" class="black_overlay"></div>
            <div id="fadeReportPriv" class="black_overlay"></div>
            <div id="fadeReportGraphPriv" class="black_overlay"></div>
            <div id="fadeReportTablePriv" class="black_overlay"></div>
            <%
                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("session.getAttribute(repList)  " +session.getAttribute("repList"));

                        PbReturnObject Dlist = new ReportTemplateDAO().getAllDashs();
                        PbReturnObject Rlist = new ReportTemplateDAO().getAllreps();
            %>

            <div id="userDialog" title="User Creation" STYLE='display:none' >
                <iframe src="newUserRegister.jsp" scrolling="no" STYLE='display:block;' height="100%" width="560px" frameborder="0" id="userframe"></iframe>
            </div>
            <div id="startPagePriv"  title="Login Start Page" STYLE='display:none'>
                <iframe src="#" height="100%" width="100%" frameborder="0" id="startPageFrame"></iframe>
            </div>

            <div id="userNewReport" title="User Report Assign" STYLE='display:none'>
                <iframe  id="selectUserReportData" NAME='selectUserReportData' frameborder="0" height="100%" width="100%" SRC='#'></iframe>

            </div>
           
        </form>
        <%-- <div id="reportstart" class="navigateDialog" title="Navigation" style="display: none;">
             <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" ></iframe>
         </div>
        --%>
        <div id="fadestart" class="black_start"></div>
        <div id="userAssignRoles" title="Assign Roles To User" STYLE='display:none' >
            <%--  <iframe src="userFolderAssignment.jsp" scrolling="no" STYLE='display:block;' height="100%" width="560px" frameborder="0" id="userframe"></iframe>--%>
        </div>

        <div id="modifyUserType"  title="Modify User Type" STYLE='display:none;height:auto'>
            <iframe  id="modifyUserTypeData" NAME='modifyUserTypeData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
        </div>
        <div id="modifyCompany"  title="Modify Company" STYLE='display:none;height:auto'>
            <iframe  id="modifyCompanyData" NAME='modifyCompanyData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
        </div>
        <div id="resetExpiry"  title="Reset ExpiryDate" STYLE='display:none;height:auto'>
            <iframe  id="resetExpiryData" NAME='resetExpiryData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
        </div>
        <div id="resetpswrddiv" title="Reset Password" style='display:none;height:auto'>
            <iframe id="resetpswrdframe" name="resetpswrdframe" frameborder="0" height="100%" width="100%" style='overflow:auto' frameborder="0"></iframe>
        </div>
        <div id="viewByDialoguser"  align="center" title="Assigned Roles and Reports" STYLE='display:none;height:auto' >
            <iframe id="viewByFrameuser" src="forATest.jsp" scrolling="no"  height="100%" width="100%" frameborder="0" ></iframe>
        </div>
        <div id="portalAssignmentDiv" STYLE='display:none'>
   
        </div>
        <div id="userActivation" STYLE='display:none'>
            
        </div>
            <div id="usertypenew" title="Change User Type Name" STYLE='display:none'>
                 <table>
                     <tr>
                         <td>
                             Old User Type Name :<input type="text"  id="oldusertypename" name="old user type name" readonly>
                         </td>
                     </tr>
                     
                     <tr>
                         <td>
                             New User Type Name:<select  id="usertypename" value="">
<!--                                  <option value="10002" id="10002">Analyzer</option>
                                 <option value="9999" id="9999">Admin</option>-->
                                  <option value="Analyzer" id="10002">Analyzer</option>
                                 <option value="Admin" id="9999">Admin</option>
                                 <option value="Power Analyzer"id="10000">Power Analyzer</option>
                                 <option value="Restricted Power Analyzer"id="10001">Restricted Power Analyzer</option>
                             </select>
                         </td>
                         <td style="display: none">
                             <input type="text" name="us" value="" id="iduser">
                         </td>
                     </tr><br></br>
                         <tr>
                             <td style="text-align:center;">
                         <input type="button" class="navtitle-hover" value="done" onclick="updateuser()">
                             </td>
                         </tr>                    
                      </table>
                     </div>
<!--         <input type="button" class="navtitle-hover" value="Done" name="Done" onclick="updateAssignment()"/>-->
<!--        <table>
                <tr>
                    <td>
                        
                    </td>
                </tr>
            </table>-->

<!--        <div id="superAdminUserDiv"  align="center" title="Assign/Revoke Privileges" style='display:none;' >
            <div id="imgDiv" style="display: none;">
              <center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>
           </div>
            <iframe id="superAdminUserFrame" src="#" scrolling="no"  height="100%" width="100%" frameborder="0" ></iframe>
        </div>
        <div id="showLicensesDiv" title="License Details" style="display: none;">

        </div>-->
   <div id='modal_dialog' style="display:none;">
    <table>
        <tr><td style="text-align:right">   Do You Wish To  </td>
        <td style="text-align:left">InActive User </td></tr>
        <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
        <tr>
    <td width="50%" style="text-align:right"><input style="width:50%;" width="20%" id="btnYes" class="navtitle-hover"  type="submit" value="Yes" ></td>
    <td width="50%" style="text-align:left"><input style="width:50%;" id="btnNo"  class="navtitle-hover"  type="submit" value="NO" ></td>
    </tr>
    </table>
</div>

<script>
function DoSomethingDangerous() {
    var warning = 'Are you sure you want to do this?';
    $('.title').html(warning);
    var dialog = $('#modal_dialog').dialog();
    function Yes() {
        dialog.dialog('close');
        // Do something
    }
    function No() {
        dialog.dialog('close');
        // Do something else
    }
    $('#btnYes').click(Yes);
    $('#btnNo').click(No);
}
</script>
        <div id="renameUserdiv" style="display:none;">
        <form action="javascript:void(0)" method="post" onsubmit="return createNewNameForUser()">
            <table align="center">
            <tr>
                <td>Old Name</td>
                <td><input type="text" id="oldName" name="oldName"></td>
            </tr>
            <tr>
                <td>New Name</td>
                <td><input type="text" id="newName" name="newName"></td>
            </tr>
            <tr>
                    <td colspan="2">
                        <br/>
                    </td>
                </tr>
                <tr>
                <td colspan="2" align="center">
                    <input class="navtitle-hover" style="width:auto" type="submit" value="Rename User" >
                </td>
            </tr>
             <input type="hidden" id="renamesubmit" name="renamesubmit" value="">
        </table>

            </form>
            </div>

    </body>
</html>