<%--
    Document   : MyAddCalendar
    Created on : 14 Aug, 2012, 3:55:27 PM
    Author     : progen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.*,java.io.*,java.util.*,prg.db.PbReturnObject,prg.db.PbDb,com.progen.users.UserLayerDAO,prg.db.OnceViewContainer,com.progen.action.UserStatusHelper"%>
<%@page import="com.progen.portal.Portal,com.progen.portal.PortletXMLHelper,com.progen.portal.PortLet,com.google.common.collect.Iterables,java.text.SimpleDateFormat,com.progen.action.UserStatusHelper"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%!
    public int nullIntconv(String inv) {
        int conv = -1;

        try {
            conv = Integer.parseInt(inv);
        } catch (Exception e) {
        }
        return conv;
    }
%>
<%

                                    PbDb pbdb = new PbDb();
                                    String userId=(String) session.getAttribute("USERID");
                                    String roleIdQry1 = "SELECT UA.USER_FOLDER_ID,UF.FOLDER_NAME from PRG_GRP_USER_FOLDER_ASSIGNMENT UA,PRG_USER_FOLDER UF where UA.USER_FOLDER_ID=UF.FOLDER_ID and UA.USER_ID="+userId;
                                    String roleNames1 = "";
                                PbReturnObject roleIdResult1 =  pbdb.execSelectSQL(roleIdQry1);
                            PbReturnObject roleNameResult1;
                            ArrayList roleIdsList1 = new ArrayList();
                            ArrayList roleNamesList1 = new ArrayList();
                        for(int k=0;k<roleIdResult1.getRowCount();k++){
                            roleIdsList1.add(roleIdResult1.getFieldValueInt(k, "USER_FOLDER_ID"));
                            roleNamesList1.add(roleIdResult1.getFieldValueString(k, "FOLDER_NAME"));
                        }


            int iYear = nullIntconv(request.getParameter("iYear"));
            int iMonth = nullIntconv(request.getParameter("iMonth"));

            Calendar ca = new GregorianCalendar();
            int iTDay = ca.get(Calendar.DATE);
            int iTYear = ca.get(Calendar.YEAR);
            int iTMonth = ca.get(Calendar.MONTH);

            if (iYear == -1) {
        iYear = iTYear;

    }

    if (iMonth == -1) {

        iMonth = iTMonth;
    }


            GregorianCalendar cal = new GregorianCalendar(iYear, iMonth, 1);

            int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int weekStartDay = cal.get(Calendar.DAY_OF_WEEK);

            cal = new GregorianCalendar(iYear, iMonth, days);
            int iTotalweeks = cal.get(Calendar.WEEK_OF_MONTH);
            Calendar calendar = Calendar.getInstance();
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
int rowcount=0;

            List<String> busRoleIDs = new ArrayList<String>();
            List<String> busRoleNames = new ArrayList<String>();

            busRoleIDs = (List<String>) request.getAttribute("BusRoleIds");
            busRoleNames = (List<String>) request.getAttribute("BusRoleNames");

            String icalDetailqry = "select * from ICAL_MASTER order by ICAL_SEQ";
            List icalIds = new ArrayList();
            List icalNames = new ArrayList();
            PbReturnObject retObj = pbdb.execSelectSQL(icalDetailqry);
            if(retObj != null && retObj.getRowCount() != 0){
                for(int i=0;i<retObj.getRowCount();i++){
                icalIds.add(retObj.getFieldValueInt(i, "ICAL_ID"));
                icalNames.add(retObj.getFieldValueString(i, "ICAL_NAME"));
                }
            }
            String icalId = (String)session.getAttribute("icalId");
            OnceViewContainer container = null;
            if(icalId != null && !icalId.equalsIgnoreCase("")){
            HashMap map2 = new HashMap();
               if(session.getAttribute("ICALDETAILS")!=null){
             map2 = (HashMap) session.getAttribute("ICALDETAILS");
              container = (OnceViewContainer) map2.get(icalId);
               }
        }



            UserLayerDAO userdao=new UserLayerDAO();
            int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
            String userTypeAdmin = userdao.getUserTypeForFeatures(USERID);


%>




<%
        String[] NbrFormatsDisp = {"Absolute", "Thousands(K)", "Millions(M)","Lakhs(L)","Crores(C)"};
        String[] NbrFormats = {"A", "K", "M","L","Cr"};
        String[] roundvalue={"0","1","2","3","4","5"};
            String[] roundtext={"No Decimal","One Decimal","Two Decimal","Three Decimal","Four Decimal","Five Decimal"};
         String userType = null;
    //       boolean isPortalEnableforUser=false;
    boolean isQDEnableforUser=false;
    boolean isPowerAnalyserEnableforUser=false;
  //  boolean isOneViewEnableforUser=false;
  //  boolean isScoreCardsEnableforUser=false;
    ServletContext context = getServletContext();
     HashMap<String,UserStatusHelper> statushelper;
     statushelper=(HashMap)context.getAttribute("helperclass");
     UserStatusHelper helper=new UserStatusHelper();
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){
       // isPortalEnableforUser=helper.getPortalViewer();
        isQDEnableforUser=helper.getQueryStudio();
        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
    //    isOneViewEnableforUser=helper.getOneView();
    //  isScoreCardsEnableforUser=helper.getScoreCards();
        userType=helper.getUserType();
        }}
            String themeColor = "blue";
            Locale locale = null;
            //          String fromReport=String.valueOf(request.getAttribute("fromReport"));
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

            String useragent = request.getHeader("User-Agent");
            String browserType = "";
            String user = useragent.toLowerCase();
            if (user.indexOf("msie") != -1) {
                browserType = "IE";
            } else if (user.indexOf("netscape6") != -1) {
                browserType = "Net";
            } else if (user.indexOf("mozilla") != -1) {
                browserType = "Moz";
            }

            ArrayList alist = new ArrayList();
            alist.add("No Comparision");
            alist.add("Last Day");
            alist.add("Last Week");
            alist.add("Last Month");
            alist.add("Last Year");
            alist.toArray();

               boolean icalhomeFlag = (Boolean)request.getAttribute("icalhomeFlag");
               String icalIdh = (String)request.getAttribute("icalId");
               String icalNameh = (String)request.getAttribute("icalName");
               String contextPath=request.getContextPath();




%>
<html>
    <head>
        <%-- <link rel="stylesheet" href="/pi/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen"/> --%>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
    <%--    <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" /> --%>

    <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />



        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>i-Cal</title>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>
<!--        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
              <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
       <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
          <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
     <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <%-- <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" /> --%>
    <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
    <link type="text/css" href="<%=contextPath%>/stylesheets/themes/white/TableCss.css" rel="stylesheet" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>

<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.jqplot.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.jqplot.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.barRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.categoryAxisRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.pointLabels.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.pieRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.donutRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.canvasAxisLabelRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.canvasTextRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.dateAxisRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.logAxisRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.canvasAxisTickRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.highlighter.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.bubbleRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.funnelRenderer.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.ClickableBars.js"></script>-->
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.meterGaugeRenderer.min.js"></script>-->
             <script type="text/javascript" src="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
<!--             <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.EnhancedLegendRenderer.js"></script>-->
<!--             <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.trendline.js"></script>-->
        <link rel="stylesheet" href="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
           <link href="<%=contextPath%>/javascript/jqplot/jquery.jqplotOneview.min.css" rel="stylesheet" type="text/css" />

           <script type="text/javascript">

        var width=1300;
            var height=200;
            var tdId=0;
            var colSp=1;
            var rowSp=1;

            var prevRow=0;
            var divName='';
            var row=0;
            var checkval = false;


               var noofDays = '<%=days%>';
               var html;
               var totalWeaks = '<%=iTotalweeks%>';
            var weekStartDay = '<%=weekStartDay%>';
            var imonth = '<%=iMonth%>';
            var iyear = '<%=iYear%>';
            var nodays = '<%=days%>';
            var datadispRows = "";
            var current= '<%=iTDay%>';
            var measVarIds = '<%=session.getAttribute("elementIds")%>';
            var measureIds = new Array();
            measureIds = measVarIds.split(",");
            var measureVarNames = '<%=session.getAttribute("elementNames")%>';

            var measureOrgVarNames = '<%=session.getAttribute("orgelementNames")%>';
            var showVariables = '<%=session.getAttribute("showVariables")%>';
            var name1 = '<%=session.getAttribute("name")%>';
            var cumCheck1 = '<%=session.getAttribute("cumCheck")%>';
            var measIds = null;
            var measNames = null;
            var IdMeas = new Array();
            var NamesMeas = new Array();
            IdMeas = null;
            <%if(container != null){%>
            measIds = '<%=container.getElementIds()%>';
            measNames = '<%=container.getElementNames()%>';
            IdMeas = measIds.split(",");
            NamesMeas = measNames.split(",");
                <%}%>
            var measureNames = new Array();
            var measureorgNames = new Array();
            measureNames = measureVarNames.split(",");
            measureorgNames = measureOrgVarNames.split(",");
            var jsonValues1 = [];
            var compCheck = "";
            var primaryIds = "";
            var primaryMeas = "";
            var comparisionType1 = "";
            <%if(container != null){%>
            var roleId1 = '<%=container.getRoleId()%>' ;
            var primaryMeasure1 = '<%=container.getPrimaryMeasure()%>';
            var dateArray1 = '<%=container.getNodays()%>';
            var viewType1 = '<%=container.getViewType()%>';
              comparisionType1 = '<%=container.getComparisionType()%>';
            var monthlyIcal1 = '<%=container.getMonthlyCal()%>';
            <%}%>
            var icalFlag = '<%=session.getAttribute("icalFlag")%>';
            var ibusRoleId = '<%=session.getAttribute("ibusRoleId")%>';
            var icaid1 = '<%=icalId%>';
            var monthlyView = false;
            var reset = false;
            $(document).ready(function(){

                if('<%=icalhomeFlag%>' == "true"){
                    getIcalDisplay1('<%=icalIdh%>','<%=icalNameh%>')
                }
                if(icalFlag == "true" && name1 != "null"){
                    $("#compareVals").show();
                   $("#icalDetails").show();
                   if(monthlyIcal1 == "true"){
                       $("#empty1").show();
                       $("#empty2").show();
                       $("#viewDailId").hide();
                       $("#viewCumId").hide();
                       $("#viewDailValTD").hide();
                       $("#viewCumValTD").hide();
                   }else{
                      $("#viewDailcheckTD").show();
                      $("#viewCumcheckTD").show();
                      $("#viewDailValTD").show();
                      $("#viewCumValTD").show();
                   }
                   $("#measureFormatTab").show();
                $("#dataDisplayOptions").show();
                $("#outsidePlusOption").show();
                $("#DispIcalName").show();
                $("#optionsTr").show();
                $("#nameTr").hide();
                $("#hLine").show();
                $("#DispIcalName").html("<td id='MyIcalName' style='font-size: 14pt;color: #25587E;' align='left' >"+name1+"</td>");
                $("#saveCal").show();
                $("#goHome").show();
                $("#icalNameId").dialog('close');
                $("#blankdiv").hide();
                $("#calendar").show();
                $("#blankdiv").hide();
                $("#calendar").show();
                if($("#viewDailId").is(':checked')){
                    checkval = true;
                }
                getDiplayOnOpen(roleId1,icaid1,primaryMeasure1,dateArray1,viewType1,NamesMeas,monthlyIcal1);
                }else if(showVariables == "true" && name1 != "null"){
                   // alert(name1)
                       $("#compareVals").show();
                   $("#icalDetails").show();
                   $("#viewDailcheckTD").show();
                   $("#viewCumcheckTD").show();
                   $("#viewDailValTD").show();
                   $("#viewCumValTD").show();
                   $("#measureFormatTab").show();
                $("#dataDisplayOptions").show();
                $("#outsidePlusOption").show();
                $("#DispIcalName").show();
                $("#optionsTr").show();
                $("#nameTr").hide();
                $("#hLine").show();
                $("#DispIcalName").html("<td id='MyIcalName' style='font-size: 14pt;color: #25587E;' align='left' >"+name1+"</td>");
                $("#saveCal").show();
                $("#goHome").show();
                $("#icalNameId").dialog('close');
                $("#blankdiv").hide();
                $("#calendar").show();

                if(measureIds != "null" ){
                $("#blankdiv").hide();
                $("#calendar").show();
                if($("#viewDailId").is(':checked')){
                    checkval = true;
                }
                displayMeasuresAccordingtoMonth(measureIds,measureNames,checkval,cumCheck1)
               }} else{
                $("#blankdiv").show();
               }

                if($.browser.msie==true){

                    $("#calendrMeasureID").dialog({
                        autoOpen: false,
                        height: 380,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                     $("#DataDisplayDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 250,
                        position: 'justify',
                        modal: true
                    });
                     $("#selectIdDiv").dialog({
                        autoOpen: false,
                        height: 110,
                        width: 210,
                        position: 'justify',
                        modal: true
                    });
                     $("#deleteMeasureDiv").dialog({
                        autoOpen: false,
                        height: 300,

                        position: 'justify',
                        modal: true
                    });

                 $("#icalNameId").dialog({
                        autoOpen: false,
                        height: 110,
                        width: 300,
                        position: 'justify',
                        fontFamily: 'Calibri',
                        modal: true
                    });
                    $("#formatMeasure").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 660,
                        position: 'justify',
                        modal: true
                    });
                    $("#primaryMeasureDiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 250,
                        position: 'justify',
                        modal: true
                    });
                    $("#trendGraphDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#saveoptionDiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                }
                else{


                    $("#calendrMeasureID").dialog({
                        autoOpen: false,
                        height: 380,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#DataDisplayDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 250,
                        position: 'justify',
                        modal: true
                    });
                     $("#selectIdDiv").dialog({
                        autoOpen: false,
                        height: 110,
                        width: 210,
                        position: 'justify',
                        modal: true
                    });

                    $("#deleteMeasureDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 250,
                        position: 'justify',
                        modal: true
                    });
                     $("#icalNameId").dialog({
                        autoOpen: false,
                        height: 110,
                        width: 300,
                        position: 'justify',
                        fontFamily: 'Calibri',
                        modal: true
                    });
                    $("#formatMeasure").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 660,
                        position: 'justify',
                        modal: true
                    });
                    $("#primaryMeasureDiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 250,
                        position: 'justify',
                        modal: true
                    });
                    $("#trendGraphDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#saveoptionDiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });

                }
            });
            function gotoDBCON(ctxPath){
                if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.forms.frm.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                document.forms.frm.submit();
                }
            }
            function goPaths(path){
//                alert(path)
                var modulecode=path.replace("home.jsp#","");
                var userType='<%=userType%>'
                if(modulecode=='Dashboard_Studio' || modulecode=='Report_Studio'){
                    if(!<%=isPowerAnalyserEnableforUser%>)
                        alert("You do not have the sufficient previlages")
                }else if(modulecode=='pbBIManager.jsp'){
                    if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                }
                parent.closeStart();
                document.forms.frm.action=path;
                document.forms.frm.submit();
            }
            function pbBiManager(){
                var path = "";
                if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                else {
                    path = "srchQueryAction.do?srchParam=pbBiManager";
                }
                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
            function addCommas(nStr)
{
    var x,x1,x2;
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}


            function goTo()
            {
                var busrole = ''
                    busrole = $("#roleType").val();
                var name = $("#MyIcalName").html();
                if(overWriteId == ""){
                        Id = <%=icalId%>;
                    }else{
                        Id = overWriteId;
                    }
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=makeSessionVarNull&fromOnchane=true&name='+name+'&reset='+reset+'&icalId='+Id+'&ibusRoleId='+busrole,
                        function(data){
                                   document.frm.submit();
                        });
            }
            function displayMeasuresAccordingtoMonth(measureId,measureName,checkval1,cumCheck){

                var comparision = $("#comparision").val();
                     if(overWriteId == ""){
                        Id = <%=icalId%>;
                    }else{
                        Id = overWriteId;
                    }
                    for(var d=0;d<31;d++){
                                $("#t"+d).html('');
                             }
                             if(comparisionType1 != "undefined" && comparisionType1 != null && comparisionType1 != "" && comparisionType1 != "No Comparision"){
                                 $("#comparision").attr("value", comparisionType1);
                                 comparision = comparisionType1;
                             }
            if(measureId != undefined){
                $("#loadingmetadata").show();
//                                alert(measureName+"measureName"+measureId+"measureId"+busroleId+"busroleId"+imonth+"imonth"+iyear+"iyear"+nodays+"nodays"+checkval1+"checkval1"+Id+"Id"+comparision+"comparision");
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasureGraphtest&measureName='+measureName+'&measureId='+measureId+'&busroleId='+busroleId+'&iMonth='+imonth+'&iYear='+iyear+'&days='+nodays+'&checkval='+checkval1+'&icalId='+Id+'&compareVal='+comparision,
                        function(data){

                            $("#loadingmetadata").hide();
                            //var jsonavle = eval('('+data+')');
                            var j=0;
                            var count=rowsno;
                            var value=0;
                            var k=nodays;
                            if(cumCheck == "cumCheck"){
                                if(datadispRows != ""){
                                    k= datadispRows;
                                }
                            }else{
                             if($("#TillCurrentDate").is(':checked'))
                                 {k=current}
                                 else if($("#TillpreviousDate").is(':checked'))
                                     {k=current-1}
                                     else if($("#NextOneWeek").is(':checked'))
                                         {k=parseInt(current)+7}
                                         else if($("#Next15Days").is(':checked'))
                                         {k=parseInt(current)+15}
                                          else
                                         {k=nodays;}
                            }

                              var d=0;
                           var jsonVare1=eval('('+data+')')
                            var jsonVare = jsonVare1.lmap;
                            var jsonVare2 = jsonVare1.lmap1;

                        var keys = [];
                        var jsonvalues = [];
                        var priorjsonvalues = [];
//                        var jsonVare=eval('('+data+')')
                        for (var key in jsonVare) {
                            if (jsonVare.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                             keys.push(key);
                        }
                        for(var i=0;i<measureName.length;i++)
                        {
                            var PM = false;
                            if(keys[i] != undefined && keys[i].indexOf("I")!=-1){
                                PM = true;
                            }
                           jsonvalues = jsonVare[keys[i]];
                            priorjsonvalues = jsonVare2[keys[i]];
                            for(var j=0;j<k;j++){
                                var html=" <table width='100%'>";
                                    var m="";
                                    var sumValue=0.0;
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        m = 'hover2';
                                    }else if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                    }else  if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover';
                                    }
//                                   if(jsonvalues[j] != undefined){
//                                    html+="<tr ><td class='"+m+"' width='70%'>"+nameList[i]+"</td>";
//                                   }else{

                                        html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'><a class='hover' href='javascript:void(0)' onclick=\"getRollingData('"+keys[i].replace("I", "", "gi")+"','"+measureName[i]+"','"+busroleId+"','"+j+"')\">"+measureName[i]+"</a></td>";
                                //   }
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        jsonvalues[j] = jsonvalues[j].replace("N", "", "gi")
                                        m = 'hover2';
                                        if(PM == true){
                                            $("#tr"+j).css("background-color", "#FF1414");
                                        }
                                    }else
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("P", "", "gi")
                                        if(PM == true){
                                            $("#tr"+j).css("background-color", "#8AFB17");
                                        }
                                    }else if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("V", "", "gi")
                                    }
                                    if(checkval1 == true){
                                   if(jsonvalues[j]!=undefined){
                                       if(PM == false){


                                   html+="<td class='"+m+"' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }else{

                                          html+="<td class='hover' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }
                                   }
                                   else
                                   {
                                       if(PM == false){
                                       html+="<td class='"+m+"' width='50%' align='right'>-</td></tr>";
                                       }else{
                                           html+="<td class='hover' width='50%' align='right'>-</td></tr>";
                                       }
                                    }
                                    }
                                  else if(jsonvalues[j]!=undefined){
                                      var length = 2;
                                       for(d=j;d>=0;d--)
                                           {
                                               var l = "";
                                               var lenarray = new Array();
                                               l = jsonvalues[d].indexOf("K");
                                               if(jsonvalues[d].indexOf(".")){
                                                   lenarray = jsonvalues[d].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}
                                               if(jsonvalues[d].indexOf("K")!= -1){
                                                   l = "K";
                                               }else if(jsonvalues[d].indexOf("L")!= -1){
                                                   l = "L";
                                               }else if(jsonvalues[d].indexOf("M")!= -1){
                                                   l = "M";
                                               }else if(jsonvalues[d].indexOf("r")!= -1){
                                                   l = "Cr";
                                               }
                                               sumValue = parseFloat(sumValue)+parseFloat(jsonvalues[d].replace(",", "", "gi")) ;
                                           }value=sumValue;
                                           if(sumValue.toString().indexOf(".") !=-1){
                                               lenarray = sumValue.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               sumValue = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                           sumValue = addCommas(sumValue);
                                           if(l == "r")
                                           sumValue = sumValue+"Cr";
                                       else if(l == "M" || l=="K" || l == "L")
                                           sumValue = sumValue+l;
                                       else
                                           sumValue = sumValue;
                                           if(PM == false){
                                           html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }else{
                                               html+="<td class='hover' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }

                                   }


                            else {
                                var l = "";
                                               var lenarray = new Array();
                                               if(jsonvalues[j] != undefined){
                                               l = jsonvalues[j].indexOf("K");
                                               if(jsonvalues[j].indexOf(".")){
                                                   lenarray = jsonvalues[j].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}}
                                           if(value.toString().indexOf(".") !=-1){
                                               lenarray = value.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               value = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                value = addCommas(value);
                                if(PM == false){
                                  html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+value+"</td></tr>";
                                }else{
                                    html+="<td class='hover' width='50%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+value+"</td></tr>";
                                }
                            }
                            html+="</table>";
                                    $("#t"+j).append(html);
                            }
                        }
                        jsonValues1 = measureName;
                        });
                        }
            }

            var rowsno = '<%=rowcount%>';
            var busroleId ='';
            var icalName = "";
            var measureAddCheck = false;
            var overWriteId = "";

            function selectedType()
            {
                document.getElementById("icalOptions").style.display='none';
                var CalCheck = $("#DispIcalName").html();
//                if(measureAddCheck == true){
                rowsno++;
                    $("#calendrMeasureID").dialog('option','title','Calendar Measures')
                         $("#calendrMeasureID").dialog('open');
//                }else{
//                    alert("Please Add I-cal");
//                }
            }
            function displayMeasure(){
                $("#blankdiv").hide();
                 var busrole = ''
                    busrole = $("#roleType").val();
                    if(busrole!='')
                        busroleId = busrole;
                    if(busroleId!=''){
                        var frameObj=document.getElementById("measureFrameId");
                        var source="<%=request.getContextPath()%>/reportViewer.do?reportBy=getMeasuresForOneView&busroleID="+busroleId;
                        frameObj.src=source;
                        $("#calendrMeasureID").dialog('option','title','Calendar Measures');
                         $("#calendrMeasureID").dialog('open');
                    }
                    else{
                        alert("Please Select Business Role!");
                    }
//                    var busrole = '1644';
//
//                //busrole = $("#busroleId").val();
//                if(busrole!='')
//                    busroleId = busrole;
////                var htmlVar = "";
//                htmlVar += "<table><tr><td>Role:</td><td><select><option></option></select></td><td><input type='button' value='go' onclick=\"displayMeasure()\" href='javascript:void(0)'></td></tr></table>";
//                $("#calendrMeasureID").html(htmlVar);
//                 var frameObj=document.getElementById("measureFrameId");
//                var source="/reportViewer.do?reportBy=getMeasuresForOneView&busroleID="+busroleId;
//                frameObj.src=source;
//                $("#calendrMeasureID").dialog('option','title','Calendar Measures')
//                $("#calendrMeasureID").dialog('open');
//

            }


 function measureDelete(){
     document.getElementById("icalOptions").style.display='none';
                $("#deleteMeasureDiv").dialog('open');
                 }

                  function deleteDivClose(){
                $("#deleteMeasureDiv").dialog('close');
                 }

            function MouseEvents(objRef, evt) {
                if (evt.type == "mouseover") {
                objRef.className = "hover";
                } else {
                objRef.className = "";
                }
              }

              function DataDisplay(){
                  if(overWriteId != ""){

                  }
                $("#DataDisplayDiv").dialog('open');
                 }


                    function DisplayData(){
                $("#DataDisplayDiv").dialog('close');
                 }

                  function SelectTypes(){
                $("#selectIdDiv").dialog('open');
                 }
                function listDisp()
                {
                    $("#icalOptions").toggle(500);
                }
            function addIcal(){
                 if(!<%=isPowerAnalyserEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.getElementById("icalOptions").style.display='none';
                $("#icalNameId").dialog('open');
                $("#icaldivid").val('');}

            }
            var oneViewIdValue='';
            var check = false;
            function dispsaveoptions(){
                $("#icalDetails").show();
               $("#viewDailcheckTD").show();
               $("#viewCumcheckTD").show();
               $("#viewDailValTD").show();
               $("#viewCumValTD").show();
               $("#compareVals").show();
               $("#saveCal").show();
               $("#goHome").show();
               $("#measureFormatTab").show();
               $("#dataDisplayOptions").show();
               $("#outsidePlusOption").show();
               $("#DispIcalName").show();
               $("#optionsTr").show();
                $("#nameTr").hide();
               $("#hLine").show();
               document.getElementById("icalOptions").style.display='none';
               var checkDailyval = false;
               var Id = "";
               if(overWriteId == ""){
                   Id = <%=icalId%>;
               }else{
                   Id = overWriteId;
               }
                var name = $("#MyIcalName").html();
               $("#DispIcalName").html("<td id='MyIcalName' style='font-size: 14pt;color: #25587E;' align='left' >"+name+"</td>");
                 var compareVal = $("#comparision").val();
                 if($("#viewDailId").is(':checked')){
                             checkDailyval = true;
                         }
                         var k=nodays;
                             if($("#TillCurrentDate").is(':checked'))
                                 {k=current}
                                 else if($("#TillpreviousDate").is(':checked'))
                                     {k=current-1}
                                     else if($("#NextOneWeek").is(':checked'))
                                         {k=current+7}
                                         else if($("#Next15Days").is(':checked'))
                                         {k=current+15}
                                          else
                                         {k=nodays;}
                var icalTabDetails = new Array();
                var iName = "";
                if(icalName == ""){
                    iName = name1;
                }else{
                    iName = icalName;
                }
                var htmlVar = ""
                $("#saveoptionDiv").dialog('open');
            }
           function saveIcal()
           {
               $("#saveoptionDiv").dialog('close');
               $("#icalDetails").show();
               $("#viewDailcheckTD").show();
               $("#viewCumcheckTD").show();
               $("#viewDailValTD").show();
               $("#viewCumValTD").show();
               $("#compareVals").show();
               $("#saveCal").show();
               $("#goHome").show();
               $("#measureFormatTab").show();
               $("#dataDisplayOptions").show();
               $("#outsidePlusOption").show();
               $("#DispIcalName").show();
               $("#optionsTr").show();
                $("#nameTr").hide();
               $("#hLine").show();
               document.getElementById("icalOptions").style.display='none';
               var checkDailyval = false;
               var Id = "";
               if(overWriteId == ""){
                   Id = <%=icalId%>;
               }else{
                   Id = overWriteId;
               }
               var name = $("#MyIcalName").html();
               $("#DispIcalName").html("<td id='MyIcalName' style='font-size: 14pt;color: #25587E;' align='left' >"+name+"</td>");
                 var compareVal = $("#comparision").val();
                 if($("#viewDailId").is(':checked')){
                             checkDailyval = true;
                         }
                         var k=nodays;
                             if($("#TillCurrentDate").is(':checked'))
                                 {k=current}
                                 else if($("#TillpreviousDate").is(':checked'))
                                     {k=current-1}
                                     else if($("#NextOneWeek").is(':checked'))
                                         {k=current+7}
                                         else if($("#Next15Days").is(':checked'))
                                         {k=current+15}
                                          else
                                         {k=nodays;}
                var icalTabDetails = new Array();
                var iName = "";
                if(icalName == ""){
                    iName = name1;
                }else{
                    iName = icalName;
                }
                if(ibusRoleId != "" && ibusRoleId != "null" && busroleId != "null" && busroleId == "select" && busroleId !="" && busroleId != null && ibusRoleId != null){
                    busroleId = ibusRoleId;
                }
                var timeDetails = "";
                if($("#currdetails").is(':checked'))
                    timeDetails = "currdetails";
                else
                    timeDetails = "sysDetails";
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=saveMeasure&busroleId='+busroleId+'&icalName='+iName+'&nodays='+k+'&imonth='+imonth+'&iyear='+iyear+'&checkDailyval='+checkDailyval+'&overWriteId='+Id+'&compareVal='+compareVal+'&primaryMeas='+primaryMeas+'&timeDetails='+timeDetails+'&monthlyView='+monthlyView,
                     function(data){
                         if(data == "true"){
                             $("#icalDetails").hide();
                 $("#viewDailcheckTD").hide();
                 $("#viewCumcheckTD").hide();
                 $("#viewDailValTD").hide();
                 $("#viewCumValTD").hide();
                 $("#compareVals").hide();
                $("#dataDisplayOptions").hide();
                $("#measureFormatTab").hide();
                $("#outsidePlusOption").hide();
                $("#saveCal").hide();
                $("#goHome").hide();
                $("#DispIcalName").hide();
                $("#optionsTr").hide();
                $("#nameTr").show();
                $("#hLine").hide();
                $("#calendar").hide();
                $("#blankdiv").show();
                             window.location.href = window.location.href;
                             alert("Saved Successfully");
                         }
                     });
            }
            function insertIcalName(){
                check = true;
                $("#icalDetails").show();
                $("#viewDailcheckTD").show();
                $("#viewCumcheckTD").show();
                $("#viewDailValTD").show();
                $("#viewCumValTD").show();
                $("#compareVals").show();
                $("#dataDisplayOptions").show();
                $("#measureFormatTab").show();
                $("#outsidePlusOption").show();
                $("#DispIcalName").show();
                $("#optionsTr").show();
                $("#nameTr").hide();
                $("#hLine").show();
                $("#saveCal").show();
                $("#goHome").show();
                $("#icalNameId").dialog('close');
                $("#blankdiv").hide();
                $("#calendar").show();
                for(var d=0;d<31;d++){
                                 $("#t"+d).html('');
                             }
                icalName = $("#icaldivid").val();
                $("#DispIcalName").html("<td id='MyIcalName' style='font-size: 14pt;color: #25587E;' align='left' class='colorClass'>"+icalName+"</td>");
            }

            function refreshCurrentPage(){
                window.location.href = window.location.href;
            }
            function getIcalDisplay1(icalId,icalName){
                measureAddCheck = true;
                overWriteId = icalId;
                $("#DataDisplayDiv").dialog('close');
                $("#icalDetails").show();
                $("#viewDailcheckTD").show();
                $("#viewCumcheckTD").show();
                $("#viewDailValTD").show();
                $("#viewCumValTD").show();
                $("#compareVals").show();
                $("#dataDisplayOptions").show();
                $("#measureFormatTab").show();
                $("#outsidePlusOption").show();
                $("#saveCal").show();
                $("#goHome").show();
                $("#DispIcalName").show();
                $("#optionsTr").show();
                $("#nameTr").hide();
                $("#hLine").show();
                $("#calendar").html();

                $("#DispIcalName").html("<td id='MyIcalName' style='font-size: 14pt;color: #25587E;' align='left' class='colorClass'>"+icalName+"</td>");
                  //refreshCurrentPage();
                  //document.frm.submit();
            $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasuresForIcal&icalId='+icalId,
                     function(data){

                         var jsonVar=eval('('+data+')')
                             var idList = jsonVar.idList;
                             var nameList = jsonVar.nameList;
                             var roleId = jsonVar.roleId;
                             busroleId = roleId;
                             var dateArray = new Array();
                             var dateDetails = jsonVar.dateDetails;
                             var comparisionType = jsonVar.comparisionType;
                             var primaryMeasure = jsonVar.primaryMeasure;
                             var monthlyIcal = jsonVar.monthlyIcal;
                             if(monthlyIcal == "true"){
                                 $("#empty1").show();
                                 $("#empty2").show();
                                 $("#viewDailId").hide();
                                 $("#viewCumId").hide();
                                 $("#viewDailValTD").hide();
                                 $("#viewCumValTD").hide();
                             }
                             dateArray = dateDetails.split("/");
                             for(var d=0;d<31;d++){
                                $("#t"+d).html('');
                             }
                             var viewType = jsonVar.viewType;
                             if(viewType == "false"){
                                 $("#viewCumId").attr("checked",true);
                             }else{
                                 $("#viewDailId").attr("checked",true);
                             }
                             if(comparisionType != null && comparisionType != "" && comparisionType != "No Comparision"){
                                 $("#comparision").attr("value", comparisionType);
                                 $("#compareVals").show();
                             }
                             jsonValues1 = nameList;
                             $("#iMonth1").val(dateArray[1]);
                             $("#iYear1").val(dateArray[2]);
//                             $("#calendar").show();
                             goForOpen(roleId,icalId,primaryMeasure,dateArray[0]);
                     });

            }
            function goForOpen(roleId,viewType){
                var name = $("#MyIcalName").html();

                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=saveValsForInitialOpen&name='+name,
                     function(data){
                         document.frm.submit();

                     });
            }
            function getDiplayOnOpen(roleId,icalId,primaryMeasure,dateArray,viewType,nameList,monthlyIcal1){

                if(monthlyIcal1 == "false"){
                    monthlyView = monthlyIcal1;
                var idList = "";
                for(var d=0;d<31;d++){
                                $("#t"+d).html('');
                             }
                             if(comparisionType1 != null && comparisionType1 != "" && comparisionType1 != "No Comparision"){
                                 $("#comparision").attr("value", comparisionType1);
                             }
                $("#loadingmetadata").show();
                             $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasureGraphtest&measureName='+nameList+'&measureId='+idList+'&busroleId='+roleId+'&iMonth='+imonth+'&iYear='+iyear+'&days='+nodays+'&icalId='+icalId+'&primaryMeasure='+primaryMeasure,
                        function(data){

                            $("#loadingmetadata").hide();
                            var jsonVare1=eval('('+data+')')
                            var jsonVare = jsonVare1.lmap;
                            var jsonVare2 = jsonVare1.lmap1;
                             var k=0;
                             if($("#TillCurrentDate").is(':checked'))
                                 {k=current}
                                 else if($("#TillpreviousDate").is(':checked'))
                                     {k=current-1}
                                     else if($("#NextOneWeek").is(':checked'))
                                         {k=parseInt(current)+parseInt(7)
                                            }
                                         else if($("#Next15Days").is(':checked'))
                                         {k=parseInt(current)+15
                                         }
                                          else
                                         {k=nodays;}
                                         datadispRows=dateArray;
                                         k = dateArray;

                        var keys = [];
                        var value = 0;
                        var d=0;
                        var jsonvalues = [];
                        var priorjsonvalues = [];

                         var jsonVare1=eval('('+data+')')
                          var jsonVare = jsonVare1.lmap;
                             var jsonVare2 = jsonVare1.lmap1;
                        for (var key in jsonVare) {
                            if (jsonVare.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                           keys.push(key);
                        }

                        for(var i=0;i<nameList.length;i++)
                        {
                            var PM = false;
                            if(keys[i] != undefined && keys[i].indexOf("I")!=-1){
                                PM = true;
                            }
                            jsonvalues = jsonVare[keys[i]];

                            priorjsonvalues = jsonVare2[keys[i]];
                            for(var j=0;j<k;j++){
                                var html=" <table width='100%'>";
                                    var m="";
                                    var sumValue=0.0;
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        m = 'hover2';
                                    }else if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                    }else  if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover';
                                    }
//                                   if(jsonvalues[j] != undefined){
//                                    html+="<tr ><td class='"+m+"' width='70%'>"+nameList[i]+"</td>";
//                                   }else{

                                        html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'><a class='hover' href='javascript:void(0)' onclick=\"getRollingData('"+keys[i].replace("I", "", "gi")+"','"+nameList[i]+"','"+roleId+"','"+j+"')\">"+nameList[i]+"</a></td>";
                                //   }
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        jsonvalues[j] = jsonvalues[j].replace("N", "", "gi")
                                        m = 'hover2';
                                        if(PM == true  && primaryMeasure != null && primaryMeasure != "" && keys[i].replace("I", "", "gi") == primaryMeasure){
                                            $("#tr"+j).css("background-color", "#FF1414");
                                        }
                                    }else
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("P", "", "gi")
                                        if(PM == true  && primaryMeasure != null && primaryMeasure != "" && keys[i].replace("I", "", "gi") == primaryMeasure){
                                            $("#tr"+j).css("background-color", "#8AFB17");
                                        }
                                    }else if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("V", "", "gi")
                                    }
                                    if(viewType == "true"){
                                   if(jsonvalues[j]!=undefined){
                                       if(PM == false){
                                   html+="<td class='"+m+"' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }else{
                                          html+="<td class='hover' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }
                                   }
                                   else
                                   {
                                       if(PM == false){
                                       html+="<td class='"+m+"' width='50%' align='right'>-</td></tr>";
                                       }else{
                                           html+="<td class='hover' width='50%' align='right'>-</td></tr>";
                                       }
                                    }
                                    }
                                  else if(jsonvalues[j]!=undefined){
                                      var length = 2;
                                       for(d=j;d>=0;d--)
                                           {
                                               var l = "";
                                               var lenarray = new Array();
                                               l = jsonvalues[d].indexOf("K");
                                               if(jsonvalues[d].indexOf(".")){
                                                   lenarray = jsonvalues[d].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}
                                               if(jsonvalues[d].indexOf("K")!= -1){
                                                   l = "K";
                                               }else if(jsonvalues[d].indexOf("L")!= -1){
                                                   l = "L";
                                               }else if(jsonvalues[d].indexOf("M")!= -1){
                                                   l = "M";
                                               }else if(jsonvalues[d].indexOf("r")!= -1){
                                                   l = "Cr";
                                               }
                                               sumValue = parseFloat(sumValue)+parseFloat(jsonvalues[d].replace(",", "", "gi")) ;
                                           }value=sumValue;
                                           if(sumValue.toString().indexOf(".") !=-1){
                                               lenarray = sumValue.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               sumValue = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                           sumValue = addCommas(sumValue);
                                           if(l == "r")
                                           sumValue = sumValue+"Cr";
                                       else if(l == "M" || l=="K" || l == "L")
                                           sumValue = sumValue+l;
                                       else
                                           sumValue = sumValue;
                                           if(PM == false){
                                           html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }else{
                                               html+="<td class='hover' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }

                                   }


                            else {
                                var l = "";
                                               var lenarray = new Array();
                                               if(jsonvalues[j] != undefined){
                                               l = jsonvalues[j].indexOf("K");
                                               if(jsonvalues[j].indexOf(".")){
                                                   lenarray = jsonvalues[j].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}}
                                           if(value.toString().indexOf(".") !=-1){
                                               lenarray = value.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               value = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                value = addCommas(value);
                                if(PM == false){
                                  html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+value+"</td></tr>";
                                }else{
                                    html+="<td class='hover' width='50%' style='font-weight: bold' align='right'>"+value+"</td></tr>";
                                }
                            }
                            html+="</table>";
                                    $("#t"+j).append(html);
                            }
                        }
                        jsonValues1 = nameList;
                        });
                             $("#blankdiv").hide();
                             $("#calendar").show();
                             }else{
                                 if(comparisionType1 != null && comparisionType1 != "" && comparisionType1 != "No Comparision"){
                                 $("#comparision").attr("value", comparisionType1);
                             }
                                 monthIcal();
                             }

            }
            function getIcalValues(idList,nameList,roleId,dateDetails,viewType){
                var dateArray = new Array();
                dateArray = dateDetails.split("/");


            }
            function deleteIcal(icalId){
                            var confirmText= confirm("Are you sure you want to delete Ical");
                             if(confirmText==true){
                                 $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=deleteIcal&icalId='+icalId,
                        function(data){
                            alert("Ical Deleted successfully");
                            window.location.href = window.location.href;
                            });
                             }
            }
            function icalHome(){
                 $("#icalDetails").hide();
                 $("#viewDailcheckTD").hide();
                 $("#viewCumcheckTD").hide();
                 $("#viewDailValTD").hide();
                 $("#viewCumValTD").hide();
                 $("#compareVals").hide();
                $("#dataDisplayOptions").hide();
                $("#measureFormatTab").hide();
                $("#outsidePlusOption").hide();
                $("#saveCal").hide();
                $("#goHome").hide();
                $("#DispIcalName").hide();
                $("#optionsTr").hide();
                $("#nameTr").show();
                $("#hLine").hide();
                $("#calendar").hide();
                $("#blankdiv").show();
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=setSessionVal&&name='+null,
                        function(data){
                            window.location.href = window.location.href;
                        });
            }
            var jsonavle  = "";
            function cumulativeViewCheck(cumCheck){
                $("#DataDisplayDiv").dialog('close');
                $("#blankdiv").hide();
                $("#calendar").show();
                var measIds = new Array();
                var meaNmaes = new Array();
                for(var d=0;d<31;d++){
                    $("#t"+d).html('');
        }
        measIds = measureIds;
        meaNmaes = measureNames;
        if(document.getElementById("viewDailId") != null && document.getElementById("viewDailId").checked){
            if(measIds == "null"){
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getSessionVal',
                function(data){
                    measIds = data;
                    var measureIdsArr = new Array();
                    var meaIdsArr = new Array();
                    var meaNamesArr = new Array();
            measureIdsArr = measIds.split("@");
            meaIdsArr = measureIdsArr[0].split(",");
            meaNamesArr = measureIdsArr[1].split(",");
                        displayMeasuresAccordingtoMonth(meaIdsArr,meaNamesArr,true,cumCheck)
                });
            }else{
                    displayMeasuresAccordingtoMonth(measIds,meaNmaes,true,cumCheck)
            }
        }else{
            if(measIds == "null"){
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getSessionVal',
                function(data){
                    measIds = data;
                    var measureIdsArr = new Array();
                    var meaIdsArr = new Array();
                    var meaNamesArr = new Array();
            measureIdsArr = measIds.split("@");
            meaIdsArr = measureIdsArr[0].split(",");
            meaNamesArr = measureIdsArr[1].split(",");
                        displayMeasuresAccordingtoMonth(meaIdsArr,meaNamesArr,false,cumCheck)
                });
            }else{
                    displayMeasuresAccordingtoMonth(measIds,meaNmaes,false,cumCheck)
            }
        }
                }
                var idList = "";
                var nameList1 = "";
                function measureFormat(){
                    var htmlVar = "";
                    htmlVar +="<thead><tr><th height='30%' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;' align='center' class='header' nowrap=''>Measure Name</th>";
                    htmlVar +="<th height='30%' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;' align='center' class='header' nowrap=''>Display Measure Name</th>";
                    htmlVar +="<th height='30%' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;' align='center' class='header' nowrap=''>Number Format</th>";
                    htmlVar +="<th height='30%' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;' align='center' class='header' nowrap=''>Round</th>";
                    htmlVar +="</tr></thead><tbody><tr></tr><tr></tr><tr></tr><tr></tr>";
                    var Id = "";
                    if(overWriteId == ""){
                        Id = <%=icalId%>;
                    }else{
                        Id = overWriteId;
                    }
                    $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasuresForFormating&icalId='+Id,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){
                            var jsonavle = eval('('+data+')');
                            idList = jsonavle.idList;
                            var nameList = jsonavle.nameList;
                            nameList1 = nameList;
                            var displaynameList = jsonavle.displaynameList;
                            var formatList = jsonavle.formatList;
                            var val = "";
                            var roundingList = jsonavle.roundingList;
                            for(var i=0;i<idList.length;i++){
                               htmlVar += "<tr><td id='default"+idList[i]+"'><input type='textbox' readonly value='"+nameList[i]+"'>&nbsp;&nbsp;</td>";
                               htmlVar += "<td><input id='changeName"+i+"' type='textbox' value='"+displaynameList[i]+"'>&nbsp;&nbsp;</td>";
                               htmlVar += "<td id='format"+idList[i]+"'>";
                                htmlVar += "<select id='formatVal"+i+"' name='formatVal"+i+"' class='formatVal'>";
                                   <%for (int j = 0; j < NbrFormats.length; j++) {%>
                                        if(formatList[i]=='<%=NbrFormats[j]%>')
                                            htmlVar += "<option selected value=<%=NbrFormats[j]%>><%=NbrFormatsDisp[j]%></option>";
                                        else
                                            htmlVar += "<option value=<%=NbrFormats[j]%>><%=NbrFormatsDisp[j]%></option>"
                               <%}%>
                                   htmlVar += "</select>";
                               htmlVar += "&nbsp;&nbsp;</td>";
                               htmlVar += "<td id='round"+idList[i]+"'>";
                               htmlVar += "<select id='roundVal"+i+"' name='roundVal"+i+"' class='roundVal'>";
                               <%for(int j=0;j<roundvalue.length;j++){%>
                                   if(roundingList[i]=='<%=roundvalue[j]%>')
                                       htmlVar += "<option selected value=<%=roundvalue[j]%>><%=roundtext[j]%></option>"
                                   else
                                       htmlVar += "<option value=<%=roundvalue[j]%>><%=roundtext[j]%></option>"
                                   <%}%>
                                       htmlVar += "</select>";
                               htmlVar += "&nbsp;&nbsp;</td></tr>";
                            }
                            htmlVar += "<tr></tr><tr></tr><tr></tr><tr></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' align='center' class='navtitle-hover' value='Done' Name='Done' onclick='applyValues("+Id+","+idList+")'></td></tr></tbody>"
                    }
                });
                              $("#measureFormatTab").html(htmlVar);
                              $("#formatMeasure").dialog('open');
                }
                function enableComp(){
                    document.getElementById("icalOptions").style.display='none';
                    compCheck = "checked";
                    $("#compareVals").show();
                }
                function primaryMeasure(){
                    var Id = "";
                    if(overWriteId == ""){
                        Id = <%=icalId%>;
                    }else{
                        Id = overWriteId;
                    }
                    var htmlVar1 = "";
                    $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasuresForFormating&icalId='+Id,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){
                    var jsonavle = eval('('+data+')');
                    var ids = "";
                            ids = jsonavle.idList;
                            primaryIds = ids;
                            //idList = ids;
                            var nameList = jsonavle.nameList;
                            var primMeas = jsonavle.primaryMeasure;
                            //nameList1 = nameList;
                    htmlVar1 +="<table><thead><tr><th height='30%' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;' align='center' class='header' nowrap=''>Measure Name</th>";
                    htmlVar1 +="<th height='30%' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;' align='center' class='header' nowrap=''>Select Measure</th></tr></thead>";
                    htmlVar1 +="<tbody><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>";
                    for(var i=0;i<ids.length;i++){
                        if(primMeas != null && primMeas != "" && primMeas == ids[i]){
                            htmlVar1 +="<tr><td id='name"+ids[i]+"' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;'>"+nameList[i]+"</td><td align='center'><input type='checkbox' class='"+ids[i]+"' id='check"+ids[i]+"' checked></td></tr>";
                        }else{
                            htmlVar1 +="<tr><td id='name"+ids[i]+"' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;'>"+nameList[i]+"</td><td align='center'><input type='checkbox' class='"+ids[i]+"' id='check"+ids[i]+"'></td></tr>";
                        }
                    }
                    htmlVar1 +="<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>";
                    htmlVar1 +="<tr><td align='right'><input type='button' style='font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;' class='navtitle-hover' onclick='getPrimaryMeasure()' value='Done' name='Done' title='Done'></td></tr>";
                    htmlVar1 +="</tbody>";
                }
                });
                $("#primaryMeasureDiv").html(htmlVar1);
                $("#primaryMeasureDiv").dialog('open');
                }
                function getPrimaryMeasure(){
                var checkval1 = false;
                    if($("#viewDailId").is(':checked')){
                    checkval1 = true;
                }
                var Id = "";
                    if(overWriteId == ""){
                        Id = <%=icalId%>;
                    }else{
                        Id = overWriteId;
                    }
                   $("#primaryMeasureDiv").dialog('close');
                   document.getElementById("icalOptions").style.display='none';
                for(var q=0;q<primaryIds.length;q++){
                    $('.'+jQuery.trim(primaryIds[q]).toString().replace(" ","_","gi")).each(function(index) {
//                        alert( $(objectVal).val() )
                        var compareVal = "";
                        if (this.checked){
                            primaryMeas = primaryIds[q];
                            for(var d=0;d<31;d++){
                                 $("#t"+d).html('');
                                 $("#tr"+d).css("background-color", "");
                }
                           // alert(primaryIds[i])
                           $("#loadingmetadata").show();
                             $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasureGraphtest&measureName='+jsonValues1+'&measureId='+primaryIds+'&busroleId='+busroleId+'&iMonth='+imonth+'&iYear='+iyear+'&days='+nodays+'&checkval='+checkval+'&compareVal='+compareVal+'&primaryMeasure='+primaryIds[q]+'&icalId='+Id,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){
                    $("#loadingmetadata").hide();
                           var j=0;
                            var count=rowsno;
                            var value=0;
                            var k=nodays;
                             if($("#TillCurrentDate").is(':checked'))
                                 {k=current}
                                 else if($("#TillpreviousDate").is(':checked'))
                                     {k=current-1}
                                     else if($("#NextOneWeek").is(':checked'))
                                         {k=parseInt(current)+7}
                                         else if($("#Next15Days").is(':checked'))
                                         {k=parseInt(current)+15}
                                          else
                                         {k=nodays;}

                              var d=0;
                            var jsonVare1=eval('('+data+')')
                            var jsonVare = jsonVare1.lmap;
                            var jsonVare2 = jsonVare1.lmap1;
                        var keys = [];
                        var jsonvalues = [];
                        var priorjsonvalues = [];
                        for (var key in jsonVare) {
                            if (jsonVare.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        for(var i=0;i<keys.length;i++)
                        {
                            var PM = false;
                            if(keys[i] != undefined && keys[i].indexOf("I")!=-1){
                                PM = true;
                            }
                            jsonvalues = jsonVare[keys[i]];
                            priorjsonvalues = jsonVare2[keys[i]];
                            for(var j=0;j<k;j++){
                                if(priorjsonvalues == 'undefined'){
                                    priorjsonvalues[j] = "";
                                }
                                var html=" <table width='100%'>";
                                    var m="";
                                    var sumValue=0.0;
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        m = 'hover2';
                                    }else if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                    }else  if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover';
                                    }
//                                   if(jsonvalues[j] != undefined){
//                                    html+="<tr ><td class='"+m+"' width='70%'>"+nameList[i]+"</td>";
//                                   }else{
                                     //html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'>"+jsonValues1[i]+"</td>";
                                     html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'><a class='hover' href='javascript:void(0)' onclick=\"getRollingData('"+keys[i].replace("I", "", "gi")+"','"+jsonValues1[i]+"','"+busroleId+"','"+j+"')\">"+jsonValues1[i]+"</a></td>";
                                //   }
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        jsonvalues[j] = jsonvalues[j].replace("N", "", "gi")
                                        m = 'hover2';
                                        if(PM == true  && keys[i].replace("I", "", "gi") == primaryIds[q]){
                                            $("#tr"+j).css("background-color", "#FF1414");
                                        }
                                    }else
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("P", "", "gi")
                                        if(PM == true  && keys[i].replace("I", "", "gi") == primaryIds[q]){
                                            $("#tr"+j).css("background-color", "#8AFB17");
                                        }
                                    }else if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("V", "", "gi")
                                    }
                                    if(checkval1 == true){
                                   if(jsonvalues[j]!=undefined){
                                       if(PM == false){
                                   html+="<td class='"+m+"' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }else{
                                          html+="<td class='hover' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }
                                   }
                                   else
                                   {
                                       if(PM == false){
                                       html+="<td class='"+m+"' width='50%' align='right'>-</td></tr>";
                                       }else{
                                           html+="<td class='hover' width='50%' align='right'>-</td></tr>";
                                       }
                                    }
                                    }
                                  else if(jsonvalues[j]!=undefined){
                                      var length = 2;
                                       for(d=j;d>=0;d--)
                                           {
                                               var l = "";
                                               var lenarray = new Array();
                                               l = jsonvalues[d].indexOf("K");
                                               if(jsonvalues[d].indexOf(".")){
                                                   lenarray = jsonvalues[d].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}
                                               if(jsonvalues[d].indexOf("K")!= -1){
                                                   l = "K";
                                               }else if(jsonvalues[d].indexOf("L")!= -1){
                                                   l = "L";
                                               }else if(jsonvalues[d].indexOf("M")!= -1){
                                                   l = "M";
                                               }else if(jsonvalues[d].indexOf("r")!= -1){
                                                   l = "Cr";
                                               }
                                               sumValue = parseFloat(sumValue)+parseFloat(jsonvalues[d].replace(",", "", "gi")) ;
                                           }value=sumValue;
                                           if(sumValue.toString().indexOf(".") !=-1){
                                               lenarray = sumValue.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               sumValue = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                           sumValue = addCommas(sumValue);
                                           if(l == "r")
                                           sumValue = sumValue+"Cr";
                                       else if(l == "M" || l=="K" || l == "L")
                                           sumValue = sumValue+l;
                                       else
                                           sumValue = sumValue;
                                           if(PM == false){
                                           html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }else{
                                               html+="<td class='hover' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }

                                   }


                            else {
                                var l = "";
                                               var lenarray = new Array();
                                               if(jsonvalues[j] != undefined){
                                               l = jsonvalues[j].indexOf("K");
                                               if(jsonvalues[j].indexOf(".")){
                                                   lenarray = jsonvalues[j].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}}
                                           if(value.toString().indexOf(".") !=-1){
                                               lenarray = value.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               value = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                value = addCommas(value);
                                if(PM == false){
                                  html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+value+"</td></tr>";
                                }else{
                                    html+="<td class='hover' width='50%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+value+"</td></tr>";
                                }
                            }
                            html+="</table>";
                                    $("#t"+j).append(html);
                            }
                        }
                     }
                    });
                  }
                    });
                }
                }
//                function checkMeasureSelection(measObject){
//                    for(var i=0;i<primaryIds.length;i++){
//                        alert($("#check"+primaryIds[i]).val())
//                        if($("#check"+primaryIds[i]).is(':checked')){
//                            alert("Please select only one measure");
//                    }
//                    }
//                }
                function changeComparision(){
                    var comparision = $("#comparision").val();
                    var checkval1 = false;
                    var Id = "";
                    if(overWriteId == ""){
                        Id = <%=icalId%>;
                    }else{
                        Id = overWriteId;
                    }
                    if($("#viewDailId").is(':checked')){
                    checkval1 = true;
                }
                if(check == false){
                    if(measureAddCheck == true || measureIds != null){
                        for(var d=0;d<31;d++){
                                 $("#t"+d).html('');
                             }
                             var measIds = new Array();
                var meaNmaes = new Array();
                var measureIdsArr = new Array();
                    var meaIdsArr = new Array();
                    var meaNamesArr = new Array();
                          $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getSessionVal',
                function(data){
                    measIds = data;
            measureIdsArr = measIds.split("@");
            meaIdsArr = measureIdsArr[0].split(",");
            meaNamesArr = measureIdsArr[1].split(",");
                });
                    $("#loadingmetadata").show();
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasureGraphtest&measureName='+measureNames+'&measureId='+measureIds+'&busroleId='+busroleId+'&iMonth='+imonth+'&iYear='+iyear+'&days='+nodays+'&checkval='+checkval1+'&icalId='+Id+'&compareVal='+comparision,
                        function(data){
                            $("#loadingmetadata").hide();
                            //var jsonavle = eval('('+data+')');
                            var j=0;
                            var count=rowsno;
                            var value=0;
                            var k=nodays;
                             if($("#TillCurrentDate").is(':checked'))
                                 {k=current}
                                 else if($("#TillpreviousDate").is(':checked'))
                                     {k=current-1}
                                     else if($("#NextOneWeek").is(':checked'))
                                         {k=parseInt(current)+7}
                                         else if($("#Next15Days").is(':checked'))
                                         {k=parseInt(current)+15}
                                          else
                                         {k=nodays;}

                              var d=0;
                             var jsonVare1=eval('('+data+')')
                            var jsonVare = jsonVare1.lmap;
                            var jsonVare2 = jsonVare1.lmap1;
                        var keys = [];
                        var jsonvalues = [];
                        var priorjsonvalues = [];
                        for (var key in jsonVare) {
                            if (jsonVare.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        for(var i=0;i<keys.length;i++)
                        {
                            var PM = false;
                            if(keys[i] != undefined && keys[i].indexOf("I")!=-1){
                                PM = true;
                            }
                            jsonvalues = jsonVare[keys[i]];
                             priorjsonvalues = jsonVare2[keys[i]];
                            for(var j=0;j<k;j++){
                                var html=" <table width='100%'>";
                                    var m="";
                                    var sumValue=0.0;
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        m = 'hover2';
                                    }else if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                    }else  if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover';
                                    }
//                                   if(jsonvalues[j] != undefined){
//                                    html+="<tr ><td class='"+m+"' width='70%'>"+nameList[i]+"</td>";
//                                   }else{
                                     //html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'>"+meaNamesArr[i]+"</td>";
                                     html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'><a class='hover' onclick=\"getRollingData('"+keys[i].replace("I", "", "gi")+"','"+meaNamesArr[i]+"','"+busroleId+"','"+j+"')\">"+meaNamesArr[i]+"</a></td>";
                                //   }
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        jsonvalues[j] = jsonvalues[j].replace("N", "", "gi")
                                        m = 'hover2';
                                        if(PM == true){
                                            $("#tr"+j).css("background-color", "#FF1414");
                                        }
                                    }else
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("P", "", "gi")
                                        if(PM == true){
                                            $("#tr"+j).css("background-color", "#8AFB17");
                                        }
                                    }else if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("V", "", "gi")
                                    }
                                    if(checkval1 == true){
                                   if(jsonvalues[j]!=undefined){
                                       if(PM == false){
                                   html+="<td class='"+m+"' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }else{
                                          html+="<td class='hover' width='30%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+jsonvalues[j]+"</td></tr>";
                                       }
                                   }
                                   else
                                   {
                                       if(PM == false){
                                       html+="<td class='"+m+"' width='50%' align='right'>-</td></tr>";
                                       }else{
                                           html+="<td class='hover' width='50%' align='right'>-</td></tr>";
                                       }
                                    }
                                    }
                                  else if(jsonvalues[j]!=undefined){
                                      var length = 2;
                                       for(d=j;d>=0;d--)
                                           {
                                               var l = "";
                                               var lenarray = new Array();
                                               l = jsonvalues[d].indexOf("K");
                                               if(jsonvalues[d].indexOf(".")){
                                                   lenarray = jsonvalues[d].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}
                                               if(jsonvalues[d].indexOf("K")!= -1){
                                                   l = "K";
                                               }else if(jsonvalues[d].indexOf("L")!= -1){
                                                   l = "L";
                                               }else if(jsonvalues[d].indexOf("M")!= -1){
                                                   l = "M";
                                               }else if(jsonvalues[d].indexOf("r")!= -1){
                                                   l = "Cr";
                                               }
                                               sumValue = parseFloat(sumValue)+parseFloat(jsonvalues[d].replace(",", "", "gi")) ;
                                           }value=sumValue;
                                           if(sumValue.toString().indexOf(".") !=-1){
                                               lenarray = sumValue.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               sumValue = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                           sumValue = addCommas(sumValue);
                                           if(l == "r")
                                           sumValue = sumValue+"Cr";
                                       else if(l == "M" || l=="K" || l == "L")
                                           sumValue = sumValue+l;
                                       else
                                           sumValue = sumValue;
                                           if(PM == false){
                                           html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }else{
                                               html+="<td class='hover' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }

                                   }


                            else {
                                var l = "";
                                               var lenarray = new Array();
                                               if(jsonvalues[j] != undefined){
                                               l = jsonvalues[j].indexOf("K");
                                               if(jsonvalues[j].indexOf(".")){
                                                   lenarray = jsonvalues[j].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}}
                                           if(value.toString().indexOf(".") !=-1){
                                               lenarray = value.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               value = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                value = addCommas(value);
                                if(PM == false){
                                  html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+value+"</td></tr>";
                                }else{
                                    html+="<td class='hover' width='50%' style='font-weight: bold' align='right' title='"+priorjsonvalues[j]+"'>"+value+"</td></tr>";
                                }
                            }
                            html+="</table>";
                                    $("#t"+j).append(html);
                            }
                        }
                        });
                        }
                        }
                }
                function applyValues(icalId,idList1){
                    var formatList = new Array();
                    var roundList = new Array();
                    var dispNames = new Array();
                    var name = $("#MyIcalName").html();
                    for(var j=0;j<idList.length;j++){
                          var temp=$("#formatVal"+j).val();
                          var temp1=$("#roundVal"+j).val();
                          var temp2=$("#changeName"+j).val();
                          formatList.push(temp);
                          roundList.push(temp1);
                          dispNames.push(temp2);
                    }
                    $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=applyFormating&formatList='+formatList+'&applyValues=applyValues'+'&name='+name+'&fromOnchane=true'+'&icalId='+icalId+'&roundList='+roundList+'&dispNames='+dispNames,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){
                    document.frm.submit();
                }
                });
                }
                function monthIcal()
                {

                    for(var j=0;j<12;j++){
                    $("#monthView"+j).html('');
                    }
                    var comparision = $("#comparision").val();
                     $("#calendar").hide();
                     document.getElementById("icalOptions").style.display='none';
                     $("#monthlyIcal").show();
                     monthlyView = true;
                     $("#empty1").show();
                     $("#empty2").show();
                     $("#viewDailId").hide();
                     $("#viewCumId").hide();
                     $("#viewDailValTD").hide();
                     $("#viewCumValTD").hide();
                       $("#calendar").hide();
                     var checkval1 = true;
                     $("#loadingmetadata").show();
                     $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMonthlyDataForIcal&measureName='+measureNames+'&measureId='+measureIds+'&busroleId='+busroleId+'&iMonth='+imonth+'&iYear='+iyear+'&days='+noofDays+'&icalId='+<%=icalId%>+'&compareVal='+comparision,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){

                    $("#loadingmetadata").hide();
                    var d=0;
                            var jsonVare=eval('('+data+')')
                        var keys = [];
                        var jsonvalues = [];
                        for (var key in jsonVare) {
                            if (jsonVare.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        for(var i=0;i<keys.length;i++)
                        {
                            jsonvalues = jsonVare[keys[i]];
                            for(var j=0;j<jsonvalues.length;j++){
                                var html=" <table width='100%'>";
                                   var m='hover';
                                    var sumValue=0.0;
                                    var value = 0.0;
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        m = 'hover2';
                                    }else if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                    }else{
                                        m = 'hover';
                                    }
//                                   if(jsonvalues[j] != undefined){
//                                    html+="<tr ><td class='"+m+"' width='70%'>"+nameList[i]+"</td>";
//                                   }else{
                                     if(jsonValues1 == ""){
                                         if(measureNames == "")

                                         html+="<tr><td class='hover' style='font-size: 9pt;' width='70%'>"+measureNames[i]+"</td>";
                                     else
                                         html+="<tr><td class='hover' style='font-size: 9pt;' width='70%'>"+measureorgNames[i]+"</td>";
                                     }
                                     else
                                         html+="<tr><td class='hover' style='font-size: 9pt;' width='70%'>"+jsonValues1[i]+"</td>";
                                //   }
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        jsonvalues[j] = jsonvalues[j].replace("N", "", "gi")
                                        m = 'hover2';
                                    }else
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("P", "", "gi")
                                    }else if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("V", "", "gi")
                                    }
                                    if(checkval1 == true){
                                   if(jsonvalues[j]!=undefined){
                                   html+="<td class='"+m+"' width='30%' style='font-weight: bold' align='right'>"+jsonvalues[j]+"</td></tr>";
                                   }
                                   else{html+="<td class='"+m+"' width='50%' align='right'>-</td></tr>";
                                    }
                                    }
                                  else if(jsonvalues[j]!=undefined){
                                      var length = 2;
                                       for(d=j;d>=0;d--)
                                           {
                                               var l = "";
                                               var lenarray = new Array();
                                               l = jsonvalues[d].indexOf("K");
                                               if(jsonvalues[d].indexOf(".")){
                                                   lenarray = jsonvalues[d].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }
                                               }
                                               if(jsonvalues[d].indexOf("K")!= -1){
                                                   l = "K";
                                               }else if(jsonvalues[d].indexOf("L")!= -1){
                                                   l = "L";
                                               }else if(jsonvalues[d].indexOf("M")!= -1){
                                                   l = "M";
                                               }else if(jsonvalues[d].indexOf("r")!= -1){
                                                   l = "Cr";
                                               }
                                               sumValue = parseFloat(sumValue)+parseFloat(jsonvalues[d].replace(",", "", "gi")) ;
                                           }value=sumValue;
                                           if(sumValue.toString().indexOf(".") !=-1){
                                               lenarray = sumValue.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               sumValue = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                           sumValue = addCommas(sumValue);
                                           if(l == "r")
                                           sumValue = sumValue+"Cr";
                                       else if(l == "M" || l=="K" || l == "L")
                                           sumValue = sumValue+l;
                                       else
                                           sumValue = sumValue;
                                           html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";

                                   }


                            else {
                                var l = "";
                                               var lenarray = new Array();
                                               if(jsonvalues[j] != undefined){
                                               l = jsonvalues[j].indexOf("K");
                                               if(jsonvalues[j].indexOf(".")){
                                                   lenarray = jsonvalues[j].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}}
                                           if(value.toString().indexOf(".") !=-1){
                                               lenarray = value.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               value = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                value = addCommas(value);
                                  html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+value+"</td></tr>";
                            }
                            html+="</table>";
                                    $("#monthView"+j).append(html);
                            }
                        }
                }
                });
                }
                function getRollingData(id,name,roleId,dayVal){
                    $("#trendGraphDiv").dialog('open')
                 var value= document.getElementById("trendGraphDiv");
                value.innerHTML='<center><img id="imgId" width="100px" height="100px" src=\"<%=request.getContextPath()%>/images/ajax.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getRollingDataForIcal&measureName='+name+'&measureId='+id+'&busroleId='+roleId+'&noofDays='+dayVal+'&iMonth='+imonth+'&iyear='+iyear,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){
                        $("#trendGraphDiv").html(data);
                    }
                });
                }
                function resetMonthly(){
                document.getElementById("icalOptions").style.display='none';
                reset = true;
                    $("#calendar").show();
                    $("#monthlyIcal").hide();
                    $("#empty1").hide();
                    $("#empty2").hide();
                    $("#viewDailId").show();
                    $("#viewCumId").show();
                    $("#viewDailValTD").show();
                    $("#viewCumValTD").show();
                    if(monthlyView == true){
                        goTo();
                    }
                        monthlyView = false;
                }
        </script>
        <style>

            body
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                font-family:FreeMono;
                color: darkblue
            }
            li
            {
             font-family: Calibri, Calibri, Calibri, sans-serif;
                color:blue
            }
/*<%--for blank spaces--%>*/
            .dsb
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                background-color:#F1F1F1
            }

            .tsb
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                background-color:#F2FFFF
            }

/*<%--for header--%>*/
           .hsb
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                background-color:#C2DFFF
            }
           /* <%--for alternate column--%>  ECE4F9*/
            .lsb
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                background-color:#D8F0F8
            }
            .nsb
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                background-color:#EFF9FC
            }
            .hover
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                color: black;
            }
            .hover1
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                color: green;
            }
            .hover2
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                color: red;
            }
            .colorCode{
              /*  font-family: Calibri, Calibri, Calibri, sans-serif; */
              font-family: Calibri, Calibri, Calibri, sans-serif;
                color: #369;
            }
            .colorClass{
                font-family: Calibri, Calibri, Calibri, sans-serif;
                color:#000;
            }
            .header{
                font-family: Calibri, Calibri, Calibri, sans-serif;
                font-size: 14pt;
                color: black;
                background-color:#EAF2F7;
                height:100%;
            }
            .loading_image{
                display: block;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 120%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                z-index:1001;
                overflow:auto;
            }
            .border-side {
  border-bottom: solid 1px black;
}


        </style>
    </head>

    <body>

<!--       <div align="center" width="100%" max-height="auto" style=" height:100px; border-width:1px; border-style: groove; border-color: skyblue; margin-top: 0px; margin-left: 0px; margin-right: 0px; margin-bottom: 0px; margin: 0px 0px 0px 0px; padding-bottom: 0px; padding-left: 0px; padding-top: 0px;">-->

         <table style="min-width:100%;font-family: Calibri, Calibri, Calibri, sans-serif;font-size:14pt;color: darkblue" max-height="auto" max-width="auto" >
            <tr>
                <td valign="top" style="min-width:100%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
            <tr></tr>
        </table>

                <form name="frm" method="post" height="100%" action="srchQueryAction.do?srchParam=icalPage">

<!--       <div align="center" valign="top" min-width="100%" style=" height: 500px; border-width:0px; border-style: groove; border-color: skyblue; margin-top: 0px; margin-left: 0px; margin-right: 0px; margin-bottom: 0px; margin: 0px 0px 0px 0px; padding-bottom: 0px; padding-left: 0px; padding-top: 0px;">
            -->
                <table cellspacing="0" cellpadding="0"   align="right" width="100%" height="100%" valign="top">
                    <tr id="nameTr"><td><div style="padding-left:7px;padding-right: 7px;font-size: 14pt;color: #25587E;" align='left'>I-Cal</div></td></tr>

                    <tr id="optionsTr" style="display:none;" valign="top" height=auto>
                        <td><div style="padding-left:7px;padding-right: 7px;padding-bottom: 7px"><table   width="100%" border="0" cellspacing="0" cellpadding="0" valign="top">
                                <tr></tr>
                                <tr id="DispIcalName" style="display: none;" >
                  </tr>
                  <tr id="hLine" style="display: none;"><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td>
                      <td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td>
                  <td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td><td><hr size="3" color="#3090C7" bgcolor="#3090C7" width="100%"/></td></tr>
                  <tr><td>&nbsp;</td></tr>

                  <tr valign="top"><td id="icalDetails" class="colorCode" valign="top" style="display:none;font-size: 12px;" width="13%">

                                        <select id="iMonth1" name="iMonth" style="background-color: white;font-family: Calibri, Calibri, Calibri, sans-serif;">
                                            <%
                                                        // print month in combo box to change month in calendar
                                                        for (int im = 0; im <= 11; im++) {
                                                            if (im == iMonth) {
                                            %>
                                            <option style="font-family: Calibri, Calibri, Calibri, sans-serif;" value="<%=im%>" selected="selected"><%=new SimpleDateFormat("MMMM").format(new Date(2008, im, 01))%></option>
                                            <%
                                                      } else {
                                            %>
                                            <option style="font-family: Calibri, Calibri, Calibri, sans-serif;" value="<%=im%>"><%=new SimpleDateFormat("MMMM").format(new Date(2008, im, 01))%></option>
                                            <%
                                                            }
                                                        }
                                            %>
                                        </select>

                                        <select id="iYear1" name="iYear" style="background-color: white;" >
                                            <% int cnt = 1;
                                                        // start year and end year in combo box to change year in calendar
                                                        for (int iy = iTYear - 10; iy <= iTYear + 20; iy++) {
                                                            if (iy == iYear) {
                                            %>
                                            <option value="<%=iy%>" selected="selected"><%=iy%></option>
                                            <%
                                                      } else {
                                            %>
                                            <option value="<%=iy%>"><%=iy%></option>
                                            <%
                                                            }
                                                        }
                                            %>
                                        </select>&nbsp;
                                        <input type="button" class="navtitle-hover" value="Go" title="Go" name="Go" onclick="goTo()">
                                        </td>
                                        <td id="viewDailcheckTD" style="display:none;"><input id="viewDailId" onchange="cumulativeViewCheck('cumCheck')" type='radio' name='answer_id' value='313' checked="checked"></td><td id="viewDailValTD" style="color: #369;display:none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;"> Daily</td>
                                        <td id="viewCumcheckTD" style="display:none;"><input id="viewCumId" onchange="cumulativeViewCheck('cumCheck')"  type='radio' name='answer_id' value='314'  style="font-family: Calibri, Calibri, Calibri, sans-serif;"> </td><td id="viewCumValTD" style="display:none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;color: #369;"> Cumulative</td>
                                        <td id="empty1" style="display:none;"></td><td style="display:none;" id="empty2"></td>
                                        <td id="compareVals" style="display: none;" width="15%" class="colorCode">Comparision:<select id="comparision" onchange="changeComparision()">
                                            <%for(int i=0;i<alist.size();i++){%>

                                        <option value='<%=alist.get(i)%>'><%=alist.get(i)%></option>
                                            <%}%>
                                        </select></td>




                                     <td id="icalname" style="font-size: 15px" align="center" valign="top" width="51%"></td>
                                     <td id="outsidePlusOption" valign="top" align="left" style="display:none;" width="2%">
                                         <a class="ui-icon ui-icon-plusthick" title="Ical Options" style="text-decoration: none;" onclick="listDisp()" href="javascript:void(0)"></a>
                                         <div id="icalOptions" style="display:none;width:125px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;">
                                             <table border='0' align='left' >
                                                 <tr><td>
                                                         <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="addIcal()" >Add New I-cal</a></td></tr></table>--%>
                                                         <table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="selectedType()" >Edit Measures</a></td></tr></table>
                                                         <table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="measureFormat()" >Format</a></td></tr></table>
                                                         <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="enableComp()" >Enable Comparision</a></td></tr></table> --%>
                                                         <table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="monthIcal()" >Monthly</a></td></tr></table>
                                                         <table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="primaryMeasure()" >Primary Measure</a></td></tr></table>
                                                         <table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="resetMonthly()" >Reset</a></td></tr></table>
                                                     </td></tr>
                                             </table>
                                             </div>
                                      </td>
                                      <td id="dataDisplayOptions" style="display: none;" width="2%"><a class="ui-icon ui-icon-pencil" title="Data Display" style="text-decoration:none" onclick="DataDisplay()" href="javascript:void(0)"></a></td>
                                      <td id="saveCal" style="display: none;" width="2%"><a class="ui-icon ui-icon-disk" title="Save Ical" style="text-decoration:none" onclick="dispsaveoptions()" href="javascript:void(0)"></a></td>
                                      <td id="goHome" width="5%" style="display: none"><a class="ui-icon ui-icon-home" href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" title="I-Cal Home" onclick="icalHome()"></a></td>
                                </tr>
                                 <tr><td>&nbsp;</td></tr>




                            </table></div></td>
                    </tr>

                     <tr>
                      <td>
                         <div align="center" id="blankdiv" width="100%" max-height="auto" style="display: none;overflow: auto;  height: 400px; border-width: 4px; border-style: groove; border-color: skyblue; margin-top: auto; margin-left: 100px; margin-right: 2px; margin-bottom: 200px; margin: 10px 10px 10px 10px; padding-bottom: 80px; padding-left: 450px; padding-top: 17px;padding-right: 50px;">
                             <div style="padding-right: 400px" >
                                 <% if(isPowerAnalyserEnableforUser || userTypeAdmin.equalsIgnoreCase("ADMIN")){ %>
                                 <table width="100%"><tr><td width="98%">&nbsp;&nbsp;</td><td width="2%"><a class="ui-icon ui-icon-plusthick" style="float:right;font-family: Calibri, Calibri, Calibri, sans-serif;" title="Add Ical" onclick="addIcal()" href="javascript:void(0)"></a></td></tr></table>
                                 <% } %>
                             <table></table>
                                 <table id="tablesorterReport" class="tablesorter" cellspacing="1" cellpadding="0" border="0px solid" align="left"  width="50%" style="">
                        <thead>
                            <tr>
                                <th height="30%" width="50%" style="font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;" align="center" class="header" nowrap="">Ical Name</th>
                                <th height="30%" width="50%" style="font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;" align="center" class="header" nowrap="">Options</th>
                            </tr>
                            </thead>
                                <% for(int j=0;j<icalIds.size();j++){ %>
                                <tr><td height="30" align="center" style="font-size: 9pt;font-family: Calibri, Calibri, Calibri, sans-serif;">
                                        <a onclick="getIcalDisplay1('<%=icalIds.get(j)%>','<%=icalNames.get(j)%>')" style="font-family: Calibri, Calibri, Calibri, sans-serif;" href="javascript:void(0)" title="<%=icalNames.get(j) %>" id="linkIds+<%=j%>"><%=icalNames.get(j) %></a>
                                    </td>
                                    <td valign="top" align="center"><a  href='javascript:void(0)' class="ui-icon ui-icon-trash" onclick="deleteIcal('<%=icalIds.get(j)%>')" title="Delete <%=icalNames.get(j)%>"></a></td></tr>
                                    <%}%>

                        </table></div>
                         </div>
                     </td>
                    </tr>
                    <tr  style="display: none" id="monthlyIcal">
                        <td width="100%" height="100%" >
                            <div style="padding-left:7px;padding-right: 7px;padding-bottom: 7px;height: 700px;max-height: auto">
                                <table  align="center" border="1em solid" cellpadding="0" cellspacing="0" width="100%" align="center"  max-height="auto" height="100%">
                    <% int mnth=-1;
                    int monthView = 0;
                    for(int i=0;i<4;i++)
                     {%>
                     <tr id="row<%=i%>" width="100%" height="auto">
                         <% for(int j=0;j<3;j++)
                     { mnth++;
                       if(mnth%2==0){
                        %>
                     <td height="100" id="col<%=i%><%=j%>" width="33%" valign="top" align="center"  max-height="auto" min-height="400px" class="lsb">
                         <%}
                       else{%>
                       <td height="100" id="col<%=i%><%=j%>" width="33%" valign="top" align="center"  max-height="auto" min-height="400px" class="nsb">
                         <%}%>
                         <% if  (iTYear == iYear && iTMonth == mnth) {%>
                         <table width="100%"  style="color:red"  valign="top" height="100%">
                             <%} else {%><table width="100%" valign="top"><%}%>
                          <tr height="1%"><td style="font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 12;font-weight: bold" class="hsb" align="left" height="1%">
                                  <%switch (mnth) {
                               case 0:
                                     out.print("January");
                                      break;
                                case 1:
                                     out.print("February");
                                      break;
                                case 2:
                                     out.print("March");
                                     break;
                                case 3:
                                     out.print("April");
                                  break;
                                case 4:
                                     out.print("May");
                                     break;
                                case 5:
                                     out.print("June");
                                       break;
                                case 6:
                                      out.print("July");
                                      break;
                                case 7:
                                      out.print("August");
                                     break;
                                case 8:
                                      out.print("September");
                                       break;
                                case 9:
                                      out.print("October");
                                       break;
                                case 10:
                                      out.print("November");
                                      break;
                                case 11:
                                      out.print("December");
                                      break;
                                }
                         %></td>
                          </tr>
                          <tr height="99%" valign="top" style="font-family: Calibri, Calibri, Calibri, sans-serif;" ><td id="monthView<%=monthView%>" style="font-family: Calibri, Calibri, Calibri, sans-serif;"  valign="top" align="center"  max-height="auto" >&nbsp;</td></tr>

                         </table>
                     </td><%monthView++;}%>
                     </tr><%}%>
              </table>
              </div>
             </td>
                    </tr>
                    <tr max-width="auto" max-height="auto"  class="dropDownMenu1" style="display: none" id="calendar">
                        <td><div style="padding-left:7px;padding-right: 7px;"><table  align-text="right" border="1em solid" cellpadding="0" cellspacing="0" width="100%" id="icalId">
                                <tbody>
                                    <%
                                    int k=0;

                    for (int i = 1; i <= iTotalweeks; i++) {%>
                                    <tr>
                                        <%
                                                             for (int j = 1; j <= 7; j++) {
                                                                 if (cnt < weekStartDay || (cnt - weekStartDay + 1) > days) {%>
                                                                 <td align="center" max-height="auto" height="2%" valign="top" class="dsb">
                                                                     <table height="15%" valign="top" width="100%"><tr valign="top" height="2%"><td width="73%" align="left"><table height="15" width="100%" ><tr><td></td></tr></table></td></tr></table><hr/></td>
                                        <%} else {
                                             if (cnt % 2 == 0) {%>
                                        <td align="center"  height="100" ><table width="100%" height="100%"  cellspacing="0" cellpadding="0" class="lsb">
                                                <%} else {%>
                                                <td align="center"  height="100"><table width="100%" height="100%"  cellspacing="0" cellpadding="0" class="nsb">
                                                        <%}%>
                                                        <tr height="2%">
                                                            <td   width="73%" align="left">
                                                                <% if (iTDay == (cnt - weekStartDay + 1) && (iTYear == iYear && iTMonth == iMonth)) {%>
                                                                <table width="100%"  style="color:red" class="hsb" >
                                                                    <%} else {%><table width="100%" style="color:black" class="hsb"><%}%>
                                                                        <tr><td  width ="10%" style="font-family: Calibri, Calibri, Calibri, sans-serif;font-weight: bold;" ><%=new SimpleDateFormat("MMMM").format(new Date(2008, iMonth, 01))%></td><td td="d" style="font-family: Calibri, Calibri, Calibri, sans-serif;font-weight: bold;" width="5%"><%=(cnt - weekStartDay + 1)%> </td><td style="font-family: Calibri, Calibri, Calibri, sans-serif;"> <%
                                                                                                int w = cnt % 7;
                                                                                                switch (w) {
                                                                                                    case 1:
                                                                                                        out.print(",Sunday");
                                                                                                        break;
                                                                                                    case 2:
                                                                                                        out.print(",Monday");
                                                                                                        break;
                                                                                                    case 3:
                                                                                                        out.print(",Tuesday");
                                                                                                        break;
                                                                                                    case 4:
                                                                                                        out.print(",Wednesday");
                                                                                                        break;
                                                                                                    case 5:
                                                                                                        out.print(",Thursday");
                                                                                                        break;
                                                                                                    case 6:
                                                                                                        out.print(",Friday");
                                                                                                        break;
                                                                                                    case 0:
                                                                                                        out.print(",Saturday");
                                                                                                        break;
                                                                                                }
                                                                                %></td>
                                                                        </tr>
                                                                    </table>
                                                                    <hr/>
                                                            </td>
                                                        </tr>
                                                       <% if (iTDay == (cnt - weekStartDay + 1) && (iTYear == iYear && iTMonth == iMonth)) {%>
                                                       <tr valign="top" height="99%" id="tr<%=k%>"><td  valign="top"  id="t<%=k%>">&nbsp;</td></tr>
                                                    </table></td>
                                                    <%} else {%>
                                                <tr height="99%" valign="top" id="tr<%=k%>" style="font-family: Calibri, Calibri, Calibri, sans-serif;" ><td valign="top" style="font-family: Calibri, Calibri, Calibri, sans-serif;" onmouseover="MouseEvents(this, event)" onmouseout="MouseEvents(this, event)" valign="top" id="t<%=k%>">&nbsp;</td></tr>
                                            </table></td>
                                            <%}

                                                                    k++; }
                                                                     cnt++;
                                                                }
                                            %>
                                    </tr>
                                    <%
                                                }
                                    %>
                                </tbody>
                            </table></div></td>
                    </tr>
                </table>
<!--            </div>-->
    <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
        </div>

            <div id="calendrMeasureID" style="display:none ;background-color:#fff">
                 <table>
                    <tr>
                        <td style="font-family: Calibri, Calibri, Calibri, sans-serif;">Business Role:</td>
                        <td style="font-family: Calibri, Calibri, Calibri, sans-serif;" align="left"><select id="roleType" class="myTextbox3" onchange="displayMeasure()" >
                                <option style="font-family: Calibri, Calibri, Calibri, sans-serif;" value="select">--select--</option>
                                        <% for(int i=0;i<roleIdsList1.size();i++){
                                        %>
                                        <option style="font-family: Calibri, Calibri, Calibri, sans-serif;" value='<%=roleIdsList1.get(i)%>' ><%=roleNamesList1.get(i)%></option>
                                        <%}%>
                                    </select> </td>
                    </tr>
                    </table>
                <iframe id="measureFrameId" NAME='measureFrameId' width="100%" height="80%" frameborder="0" SRC=''></iframe>
            </div>

         <div id="DataDisplayDiv" title="Data Display"  style="display:none;background-color:#fff; ">

                               <table width="100%" class="ui-corner-all" style="height:80%" border="0px solid black" cellpadding="0" cellspacing="0">
                                   <tr><td style="font-family: Calibri, Calibri, Calibri, sans-serif;" height="20"><input id="TillCurrentDate" type='radio' name='answer' value='s1' > Till Current Date</td></tr>
                                      <tr><td style="font-family: Calibri, Calibri, Calibri, sans-serif;" height="20"><input id="TillpreviousDate" type='radio' name='answer' value='s2' > Till Previous Date</td></tr>
                                       <tr><td style="font-family: Calibri, Calibri, Calibri, sans-serif;" height="20"><input id="NextOneWeek" type='radio' name='answer' value='s3'>For Next One Week</td></tr>
                                        <tr><td style="font-family: Calibri, Calibri, Calibri, sans-serif; " height="20"><input id="Next15Days" type='radio' name='answer' value='s4'>For Next 15 Days </td></tr>
                                         <tr><td style="font-family: Calibri, Calibri, Calibri, sans-serif; " height="20"><input id="EntireMonth" type='radio' name='answer' value='s5' checked="checked">For Entire Month </td></tr>

<tr><td  height="20">&nbsp;</td></tr>

                               </table>
                             <center>
                                <input class="navtitle-hover" type="button" onclick="cumulativeViewCheck('doneCheck')" value="Done" style="width:auto">
                             </center>
            </div>


<div id="deleteMeasureDiv"  title="Delete Measure" style="display:none">

                   <center>
                       <input  class="navtitle-hover" type="button" onclick="deleteDivClose()" value="Delete" style="width:auto; vertical-align: bottom">
                    </center>
               </div>
             <div id="icalNameId" style="display: none;font-family: Calibri, Calibri, Calibri, sans-serif; background-color:#fff;" title="Calendar Name">
                <table>
                    <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
                    <tr>
                        <td style="font-family: Calibri, Calibri, Calibri, sans-serif;" >
                            Enter Name&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="" value="" id="icaldivid">
                        </td>
                    </tr>
                    <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
                    <tr  style="height:10px;">
                        <td align="center">
                            <input type="button" name="" style="font-family: Calibri, Calibri, Calibri, sans-serif;" value="Save" class="navtitle-hover" onclick="insertIcalName()">
                        </td>
                    </tr>
                </table>

            </div>

          </form>

                                    <div id="formatMeasure" title="Format Measure" style="display: none;font-family: Calibri, Calibri, Calibri, sans-serif;background-color:#fff;" >
                                        <form action=""  name="MeasuresForm" id="MeasuresForm" method="post">
                                        <table id="measureFormatTab">
                                        </table>
                                            </form>
                                    </div>
                                    <div id="primaryMeasureDiv" title="Format Measure" style="display: none;font-family: Calibri, Calibri, Calibri, sans-serif;background-color:#fff;" >
                                    </div>
                                    <div id="trendGraphDiv" title="Compare with last 30 days" style="display: none;font-family: Calibri, Calibri, Calibri, sans-serif; background-color:#fff " >
                                    </div>
                                    <div id="saveoptionDiv" title="Save I-cal" style="display:none;background-color: #fff">
                                        <table>
                                            <tr><td colspan="2"><font size="2" style="font-weight: bold;font-family: Calibri, Calibri, Calibri, sans-serif;">Do you want to save on which time details?</font></td></tr>
                                            <tr><td><input type="radio" style="font-family: Calibri, Calibri, Calibri, sans-serif;" name="Date" id="sysDate" value="sysDate">System Date</td>
                                                <td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="currdetails" value="currdetails" checked>Current Details</td></tr>
                                            <tr><td><br></td></tr>
                                            <tr><td><br></td></tr>
                                            <tr><td colspan="2" align="center"><input type="button" value="Save" class="navtitle-hover" style="width:40px;height:25px;color:black" onclick="saveIcal()"/>&nbsp;&nbsp;&nbsp;
                                        </table>
                                    </div>
        <table style="width:100%;" id="footID">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>

    </body>
</html>
