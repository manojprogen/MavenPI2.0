
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="prg.db.Container"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<%

            String loguserId = String.valueOf(session.getAttribute("USERID"));
            HashMap ParametersHashMap = null;
            HashMap TableHashMap = null;
            HashMap GraphHashMap = null;
            HashMap ReportHashMap = null;

            String whatIfScenarioId = "";
            Container container = null;
            String whatIfScenarioName = "";
            String whatIfScenarioDesc = "";
            HashMap map = new HashMap();
            String ReportFolders = "";
            String ReportDimensions = "";
            String ParamRegion = "";
            String ParamDispRegion = "";
            String TableRegion = "";
            String GraphRegion = "";
            String prevParamArray = "";
            String prevTimeParams = "";

            String prevREP = "";
            String prevCEP = "";
            String prevMeasures = "";
            String prevMeasureNames = "";
            String prevMeasureNamesList = "";

            ArrayList Parameters = new ArrayList();
            ArrayList ParametersNames = new ArrayList();

            ArrayList TimeParameters = new ArrayList();
            ArrayList TimeParametersNames = new ArrayList();


            ArrayList REP = new ArrayList();
            ArrayList CEP = new ArrayList();
            ArrayList Measures = new ArrayList();
            ArrayList MeasuresNames = new ArrayList();

            HashMap GraphTypesHashMap = null;
            HashMap GraphClassesHashMap = null;

            String[] grpTypeskeys = new String[0];
            String[] grpClasseskeys = new String[0];


            if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {

                try {
                    GraphTypesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
                    GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");

                    grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                    grpClasseskeys = (String[]) GraphClassesHashMap.keySet().toArray(new String[0]);

                    whatIfScenarioId = String.valueOf(request.getAttribute("whatIfScenarioId"));
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");

                    if (map.get(whatIfScenarioId) != null) {
                        container = (prg.db.Container) map.get(whatIfScenarioId);
                    } else {
                        container = new prg.db.Container();
                    }
                    whatIfScenarioName = container.getWhatIfScenarioName();
                    whatIfScenarioDesc = container.getWhatIfScenarioDesc();

                    ParametersHashMap = container.getParametersHashMap();
                    TableHashMap = container.getTableHashMap();
                    GraphHashMap = container.getGraphHashMap();
                    ReportHashMap = container.getReportHashMap();

                    if (request.getAttribute("ReportFolders") != null) {
                        ReportFolders = String.valueOf(request.getAttribute("ReportFolders"));
                    }
                    if (request.getAttribute("ReportDimensions") != null) {
                        ReportDimensions = String.valueOf(request.getAttribute("ReportDimensions"));
                    }
                    if (request.getAttribute("ParamRegion") != null) {
                        ParamRegion = String.valueOf(request.getAttribute("ParamRegion"));
                    }
                    if (request.getAttribute("ParamDispRegion") != null) {
                        ParamDispRegion = String.valueOf(request.getAttribute("ParamDispRegion"));
                    }

                    if (request.getAttribute("TableRegion") != null) {
                        TableRegion = String.valueOf(request.getAttribute("TableRegion"));
                    }
                    if (request.getAttribute("GraphRegion") != null) {
                        GraphRegion = String.valueOf(request.getAttribute("GraphRegion"));
                    }

                    if (TableHashMap != null) {
                        REP = (ArrayList) TableHashMap.get("REP");
                        CEP = (ArrayList) TableHashMap.get("CEP");
                        Measures = (ArrayList) TableHashMap.get("Measures");
                        MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");

                        if (REP != null) {
                            for (int j = 0; j < REP.size(); j++) {
                                prevREP = prevREP + "," + REP.get(j);
                            }
                            if (!(prevREP.equalsIgnoreCase(""))) {
                                prevREP = prevREP.substring(1);
                            }
                        }
                        if (CEP != null) {
                            for (int j = 0; j < CEP.size(); j++) {
                                prevCEP = prevCEP + "," + CEP.get(j);
                            }
                            if (!(prevCEP.equalsIgnoreCase(""))) {
                                prevCEP = prevCEP.substring(1);
                            }
                        }
                        if (Measures != null && MeasuresNames != null) {
                            for (int j = 0; j < Measures.size(); j++) {
                                prevMeasures = prevMeasures + "," + Measures.get(j);
                                prevMeasureNames = prevMeasureNames + "," + MeasuresNames.get(j);
                                prevMeasureNamesList = prevMeasureNamesList + "," + MeasuresNames.get(j) + "-" + Measures.get(j);
                            }
                            if (!(prevMeasures.equalsIgnoreCase(""))) {
                                prevMeasures = prevMeasures.substring(1);
                                prevMeasureNames = prevMeasureNames.substring(1);
                                prevMeasureNamesList = prevMeasureNamesList.substring(1);
                            }
                        }
                    }
                    if (ParametersHashMap.get("Parameters") != null) {
                        Parameters = (ArrayList) container.getParametersHashMap().get("Parameters");
                        ParametersNames = (ArrayList) container.getParametersHashMap().get("ParametersNames");
                    }

                    if (Parameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                            prevParamArray = prevParamArray + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                        }
                        if (!(prevParamArray.equalsIgnoreCase(""))) {
                            prevParamArray = prevParamArray.substring(1);
                        }
                    }
                    if (ParametersHashMap.get("TimeParameters") != null) {
                        TimeParameters = (ArrayList) ParametersHashMap.get("TimeParameters");
                        TimeParametersNames = (ArrayList) ParametersHashMap.get("TimeParametersNames");
                    }
                    if (Parameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                            prevTimeParams = prevTimeParams + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                        }
                        if (!(prevTimeParams.equalsIgnoreCase(""))) {
                            prevTimeParams = prevTimeParams.substring(1);
                        }
                    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>

        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/watifDesign.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.slider.js"></script>



        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=request.getContextPath()%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript">
            $(document).ready(function(){
                if ($.browser.msie == true){
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#favouriteParamsDialog").dialog({
                        autoOpen: false,
                        height: 380,
                        width: 420,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#favouriteParamsDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 430,
                        position: 'justify',
                        modal: true
                    });
                }
            });
        </script>
        <script>
            jQuery(document).ready(function()
            {
                $("#breadCrumb").jBreadCrumb();
            });
            $(document).ready(function() {
                $("#repTemTree").treeview({
                    animated: "normal",
                    unique:true
                });               
                /*
                 $("#repTemTree2").treeview({
                    animated: "normal",
                    unique:true
                });
                 */
                $("#graphTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $("#tableTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $(".column").sortable({
                    connectWith: '.column'
                });

                $("#custmemDispDia").dialog({
                    // bgiframe: true,
                    autoOpen: false,
                    height: 650,
                    width: 550,
                    modal: true
                });
                $("#paramSecurity").dialog({                   
                    // bgiframe: true,
                    autoOpen: false,
                    height: 400,
                    width: 600,
                    modal: true
                });
                $("#paramDefaultVal").dialog({                   
                    // bgiframe: true,
                    autoOpen: false,
                    height: 400,
                    width: 600,
                    modal: true
                });

            });

            $(function() {

            });
            function contMenu(Obj){               
                $("#"+Obj.id).contextMenu({
                    menu: 'parampotionsListMenu'
                }, function(action, el, pos) {
                    contextMenuParamWork(action, el, pos);
                });                
            }
            function createWhatIfScenario()
            {
                document.myForm2.action="reportTemplateAction.do?templateParam=saveWhatIfScenario";
                document.myForm2.submit();
            }
//            function gohome(){
//                document.forms.myForm2.action="baseAction.do?param=goHome";
//                document.forms.myForm2.submit();
//            }
//            function logout(){
//                document.forms.myForm2.action="baseAction.do?param=logoutApplication";
//                document.forms.myForm2.submit();
//            }
            function cancelWhatIfScenario(){
                document.forms.myForm2.action='<%=request.getContextPath()%>'+"/home.jsp#What-if_Analysis";
                document.forms.myForm2.submit();
            }
            function viewDashboardG(path){
                document.forms.myForm2.action=path;
                document.forms.myForm2.submit();
            }
            function viewReportG(path){
                document.forms.myForm2.action=path;
                document.forms.myForm2.submit();
            }

//            function goGlobe(){
//                $(".navigateDialog").dialog('open');
//            }
//            function closeStart(){
//                $(".navigateDialog").dialog('close');
//            }
            function goPaths(path){
                parent.closeStart();
                document.forms.myForm2.action=path;
                document.forms.myForm2.submit();
            }


            //for adding custom measure by bharathi reddy
            function contextMenuWorkCustMeasure(action, el, pos) {
                switch (action) {
                    case "addCustMeasure":
                        {
                            addCustomMeasure();
                            break;
                        }
                }
            }

            function createCustMeasure(Obj){               
                $("#"+Obj.id).contextMenu({
                    menu: 'myMenu'
                },function(action, el, pos) {                   
                    contextMenuWorkCustMeasure(action, el, pos);
                });

            }

            function addCustomMeasure(reportId)
            {
                var f=document.getElementById('custmemDisp');               
                var s="createCustMember.jsp?folderIds="+buildFldIds()+"&reportId="+reportId;
                f.src=s;               
                $("#custmemDispDia").dialog('open');
            }
            function cancelCustMember(){               
                $("#custmemDispDia").dialog('close');
            }
            function cancelCustMembersave(columnname){               
                $("#custmemDispDia").dialog('close');
                var branches = $("<li><img src='icons pinvoke/table.png' ><span >&nbsp;"+columnname+"</span></li>").appendTo("#customMeasure");
                $("#customMeasure").treeview({
                    add: branches
                });
            }
            //ends

            function contextMenuParamWork(action, el, pos) {
                switch (action) {
                    case "addDefaultValue":
                        {
                            var elementId=el.attr('id').split("-")[1];
                            addParamDefaultValues(elementId);
                            break;
                        }
                    case "addParamSecurity":
                        {
                            var elementId=el.attr('id').split("-")[1];
                            addParamSecurity(elementId);
                            break;
                        }
                    case "addcomboBox":
                        {
                            var elementId=el.attr('id').split("-")[1];
                            break;
                        }
                    case "excludeCEP":
                        {
                            var elementId=el.attr('id').split("-")[1];
                            var whatIfScenarioId=document.getElementById('whatIfScenarioId').value;
                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=excludeCEP&elementId='+elementId+'&whatIfScenarioId='+whatIfScenarioId,
                                success: function(data){
                                    document.getElementById("cepExc").value=data;
                                }
                            });
                            break;
                        }
                    case "excludeREP":
                        {
                            var elementId=el.attr('id').split("-")[1];
                            var whatIfScenarioId=document.getElementById('whatIfScenarioId').value;                            
                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=excludeREP&elementId='+elementId+'&whatIfScenarioId='+whatIfScenarioId,
                                success: function(data){                                    
                                    document.getElementById("repExc").value=data;
                                }
                            });
                            break;
                        }
                }
            }

            function addParamSecurity(elementId){
                var f=document.getElementById('paramSecurityDisp');
                var whatIfScenarioId=document.getElementById('whatIfScenarioId').value;                
                var s="pbParamSecurity.jsp?elementId="+elementId+'&whatIfScenarioId='+whatIfScenarioId+"&ReportType=R";
                f.src=s;                
                $("#paramSecurity").dialog('open');
            }

            function cancelParamSecurity(){                
                $("#paramSecurity").dialog('close');
            }

            function addParamDefaultValues(elementId){
                var f=document.getElementById('paramDefaultValDisp');
                var whatIfScenarioId=document.getElementById('whatIfScenarioId').value;                
                var s="pbParamDefaultValues.jsp?elementId="+elementId+'&whatIfScenarioId='+whatIfScenarioId+"&ReportType=R";
                f.src=s;                
                $("#paramDefaultVal").dialog('open');
            }
            function cancelParamdefSecurity(){
                $("#paramDefaultVal").dialog('close');
            }

        </script>
        <style type="text/css">
            .column { width:auto; float: left; padding-bottom: 5px; }
            .portlet { margin: 0 1em 1em 0;width:auto }
            .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em;width:auto }
            .portlet-header .ui-icon { float: right;width:auto }
            .portlet-content { padding: 0.4em;width:auto; }
            .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important; }
            .ui-sortable-placeholder * { visibility: hidden; }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .divClass{
                display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 15%;
                left: 25%;
                width: 550px;
                height:650px;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }
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
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .paramRegion{
                background-color:#e6e6e6;
            }
            a {font-family:Verdana;cursor:pointer;}
            a:link {color:#369}
            *{
                font: 11px verdana;
            }
            .flagDiv{
                width:auto;
                height:auto;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1001
            }

        </style>
        <style type="text/css">
            #demo-frame > div.demo { padding: 10px !important; };
        </style>
        <script type="text/javascript">
            function formatStr(EL,maxchars){
                strbuff=EL.innerHTML;
                newstr='';
                startI = 0;
                max=maxchars;
                str='';
                subarr=new Array(parseInt(strbuff.length/max+1));
                for (i=0;i<subarr.length;i++)
                {
                    subarr[i]=strbuff.substr(startI,max);
                    startI+=max;
                }
                for (i=0;i<subarr.length-1;i++)
                {
                    newstr+=subarr[i]+'<br/>';
                }
                newstr+=subarr[subarr.length-1];
                if(subarr.length==1){
                    EL.innerHTML=EL.innerHTML;
                }else{
                    EL.innerHTML=newstr;
                }
            }
        </script>
    </head>

    <body class="body" onload="formatStr(document.getElementById('whatIfScenarioName'),30);">
        <%
                    String url = null;
                    String Pagename = whatIfScenarioName;
                    if (request.getAttribute("whatifdesignerurl") != null) {
                        url = request.getAttribute("whatifdesignerurl").toString();
                    }

                    String UserFldsData = "";
                    if (request.getAttribute("UserFlds") != null) {
                        UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
                    }
        %>

         <table style="width:100%;">
                            <tr>
                                <td valign="top" style="width:100%;">
                                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                                </td>
                            </tr>
                        </table>
        <form name="myForm2" method="post">
            <input type="hidden" name="PrevParams" id="PrevParams" value="<%=prevParamArray%>">
            <input type="hidden" name="PrevTimeParams" id="PrevTimeParams" value="<%=prevTimeParams%>"
            <input type="hidden" name="whatIfScenarioId" id="whatIfScenarioId" value="<%=whatIfScenarioId%>">
            <table width="100%" >
                <tr>
                    <td valign="top">
                       
                    </td>
                </tr>
                <tr style="height:15px;width:100%;max-height:100%">
                    <td>
                        <table width="100%" class="ui-corner-all">
                            <tr>
                                <td style="height:10px;width:10%" >
                                    <span id="whatIfScenarioName"  style="color: #4F4F4F;font-family:verdana;font-size:15px;font-weight:bold"  title="<%=whatIfScenarioName%>"><%=whatIfScenarioName%></span>
                                </td>
                                <td valign="top" style="height:10px;width:30%">
                                    <div id='breadCrumb' class='breadCrumb module'style="width:500px">
                                        <ul>
                                            <li style="display:none;"></li>
                                            <li style="display:none;"></li>
                                            <% String pgnam = "";
                    if (brdcrmb.getPgname1() != null) {
                        pgnam = brdcrmb.getPgname1().toString();

                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname1()%>
                                            </li>

                                            <%
                        } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl1()%>'><%=brdcrmb.getPgname1()%></a>
                                            </li>
                                            <%
                        }
                    }
                    if (brdcrmb.getPgname2() != null) {
                        pgnam = brdcrmb.getPgname2().toString();

                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname2()%>
                                            </li>
                                            <%
                        } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl2()%>'><%=brdcrmb.getPgname2()%></a>
                                            </li>
                                            <%                    }
                    }
                    if (brdcrmb.getPgname3() != null) {
                        pgnam = brdcrmb.getPgname3().toString();
                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname3()%>
                                            </li>
                                            <%
                        } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl3()%>'><%=brdcrmb.getPgname3()%></a>
                                            </li>
                                            <%
                        }
                    }
                    if (brdcrmb.getPgname4() != null) {
                        pgnam = brdcrmb.getPgname4().toString();
                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname4()%>
                                            </li>
                                            <%
                        } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl4()%>'><%=brdcrmb.getPgname4()%></a>
                                            </li>
                                            <%
                        }
                    }
                    if (brdcrmb.getPgname5() != null) {
                        pgnam = brdcrmb.getPgname5().toString();
                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname5()%>
                                            </li>
                                            <%
                        } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl5()%>'><%=brdcrmb.getPgname5()%></a>
                                            </li>
                                            <%
                        }
                    }
                                            %>
                                            <li style="display:none;"></li>
                                            <li style="display:none;"></li>
                                        </ul>
                                    </div>
                                    <div class="chevronOverlay main"></div>
                                </td>
                                <td style="height:10px;width:20%;" align="right">
<!--                                    <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                    <a href="javascript:void(0)" onclick="javascript:gohome('<%=loguserId%>')" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |-->
<!--                                    <a href="#" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |-->
<!--                                    <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table style="width:100%" border="solid black 1px" >


                <tr>
                    <td width="13%" valign="top" class="draggedTable1" >
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                            &nbsp;<font style="font-weight:bold" face="verdana" size="1px">Report Designer Menu</font>
                        </div>
                        <div class="masterDiv">
                            <ul id="repTemTree" class="filetree treeview-famfamfam">
                                <li class="closed" ><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Business Roles</span>
                                    <ul id="userFlds" class="background">
                                        <%=UserFldsData%>
                                    </ul>
                                </li>

                                <li class="closed"><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Dimensions</span>
                                    <ul id="userDims" class="background">
                                    </ul>
                                </li>
                                <%--<li class="closed"><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Buckets</span>
                                    <ul id="userBuckets" class="background">
                                    </ul>
                                </li>--%>

                                <%--newly added by bharathi reddy for adding custom measure <ul>"--%>
                                <li class="closed"><img src="icons pinvoke/folder-horizontal.png"><span id="123" class="cntxtMenu" onmouseup="createCustMeasure(this)">Custom Measure</span>
                                    <ul id="customMeasure" class="background" >

                                    </ul>
                                </li>

                                <%--</ul>--%>
                                <ul id="myMenu" class="contextMenu" >
                                    <li><a href="#addCustMeasure">Add Measure</a></li>
                                </ul>
                                <%--newly added by bharathi reddy for adding custom measure end--%>
                                <li class="closed"><%--<img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Favourite Params</span>--%>
                                    <ul id="favourParams" class="background">
                                    </ul>
                                </li>
                            </ul>
                            <%--added by bhharathi reddy for adding param potions--%>
                            <ul id="parampotionsListMenu" class="contextMenu" style="width:auto;text-align:left;">
                                <li class="addDefaultValue"><a href="#addDefaultValue">Set DefaultValue</a></li>
                                <li class="addParamSecurity"><a href="#addParamSecurity">Set Parameter Security</a></li>
                                <%-- <li class="addcomboBox"><a href="#addCustMeasure">Combo Box Sequence</a></li> --%>
                                <li class="addcomboBox"><a href="#excludeREP">Exclude from Row Edge</a></li>
                                <li class="addcomboBox"><a href="#excludeCEP">Exclude from Column Edge</a></li>
                            </ul>
                            <%-- ends--%>
                        </div>
                    </td>
                <script type="text/javascript">
                    getWhatIfUserDims();
                </script>
                <td  valign="top" width="87%" >
                    <table style="height:auto;max-height:100%;width:100%"  border="0">
                        <tr>
                            <td valign="top" width="100%" height="100px" class="paramRegion">
                                <table style="height:100%" class="draggedTable"  id="newDragTables">
                                    <tr id="dragDims">
                                        <td style="height:100%" id="draggableDims" valign="top">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                &nbsp;&nbsp;<font size="2" style="font-weight:bold">Drag Parameters To Here </font> &nbsp;&nbsp;<a id="showParams" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>

                                            </h3>
                                            <div id="dragDiv" style="min-width:800px;min-height:100px">
                                                <ul id="sortable" style="width:800px">
                                                    <%=ParamRegion%>
                                                </ul>
                                            </div>
                                        </td>

                                    <tr id="favParams">
                                        <td align="right">
                                            <input type="button" align="right" id="saveFavParams" name="saveFavParams" disabled value="Save as Favourite" onclick="saveFavouriteParams()">
                                        </td>
                                    </tr>
                        </tr>
                        <tr id="lovParams" style="height:100%;display:none">
                            <td valign="top" style="height:100%">
                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                    &nbsp;&nbsp;<font size="2" style="font-weight:bold">Parameters</font>&nbsp;&nbsp;<a id="editParams" href="javascript:showMbrs()" title="Click to Edit Parameters" >Edit</a>
                                </h3>
                                <div align="left" id="paramDisp" style="min-width:800px;min-height:100px;">
                                    <%=ParamDispRegion%>
                                </div>
                            </td>
                        </tr>

                    </table>
                    <!--  <a id="Refresh" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>-->
                </td>
                </tr>

                <%--<tr>
                    <td  id="RangesRegion" align="left" width="100%" height="80px" valign="top">
                        <div id="previewlinkdiv" style="display:none">
                            <table align="right">
                                <tr>
                                    <td>
                                        <a id="prevTab1"  href="javascript:PreviewTable1()" title="Preview Table" >Preview</a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="demo" id="rangeregiondiv">

                        </div>
                    </td>
                </tr>--%>

                <tr>
                    <td align="left" width="100%" height="180px" valign="top">
                        <table style="height:100%" class="draggedTable" border="0" >
                            <tr id="previewGraph">
                                <td  style="height:100%" valign="top" >

                                    <h3 style="height:20px;width:100%" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                        &nbsp;&nbsp;<font size="2" style="font-weight:bold">Graphs </font> &nbsp;&nbsp;
                                        <a href="javascript:void(0)" onclick="showGraphs()">Add Graphs</a>&nbsp;&nbsp;
                                        <a href="javascript:void(0)" id="editGraphs" title="Preview Graphs" onclick="previewGraphs()">Preview</a>
                                    </h3>
                                    <div   id="previewDispGraph" style="width:100%;min-height:250px;height:auto;max-height:100%">
                                        <%=GraphRegion%>
                                    </div>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td align="left" width="100%" height="180px" valign="top">
                        <table style="height:100%" class="draggedTable" border="0" >
                            <tr id="editTable">
                                <td style="height:250px" valign="top">
                                    <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                        &nbsp;&nbsp;<font size="2" style="font-weight:bold">Table </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:PreviewTable1()" title="Preview Table" >Preview</a>
                                    </h3>
                                    
                                    <div id="tableDiv" style="width:100%;min-height:230px;">
                                        <Table>
                                            <Tr>
                                                <Td>
                                                    <%--don,t chage showrowparam1 and column params1 method please added by bharathi reddy these are required to exclude row and col edge--%>
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <a id="rep"  href="javascript:void(0)"  onclick="showRowParams2()"    title="Click to Select Row Parameters" >Row Edge</a>&nbsp;
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <div style="display:none;width:100px;height:100px;overflow:auto;color:black;position:absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;" id="repDiv">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </Td>
                                                <%--<Td>
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <a id="cep"  href="javascript:void(0)"  onclick="showColParams2()"   title="Click to Select Column Parameters"  >Column Edge</a>&nbsp;
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <div style="display:none;width:100px;height:100px;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;" id="cepDiv">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </Td>--%>
                                                <Td>
                                                    <a id="Measure"  href="javascript:void(0)" onclick="showMeasures()" title="Click to Select Measures" >Measures</a>&nbsp;
                                                </Td>
                                            </Tr>
                                        </Table>
                                    </div>
                                </td>
                            </tr>
                            <tr id="previewTable" style="display:none;">
                                <td  style="height:250px" valign="top" >
                                    <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                        <font size="2" style="font-weight:bold">Table</font>
                                        &nbsp;&nbsp;<a id="prevTab"  href="javascript:void(0)" onclick="EditTable()" title="Click to Edit Table" >Edit</a>
                                        &nbsp;&nbsp; <a id="TableProperties" href="javascript:void(0)" onclick="showTableProperties()" title="Click to set Table Propertie" >Table Properties</a>
                                        &nbsp;&nbsp; <a id="CustomMeasure" href="javascript:void(0)" onclick="addCustomMeasure('<%=whatIfScenarioId%>')" title="Click to Add Custom Measures" >Define Custom Measure</a>
                                    </h3>
                                    <div id="previewDispTable" style="width:auto;height:auto;overflow:auto">
                                        <%=TableRegion%>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>


<div id="measuresDialog" style="display:none" title="Add Measures">
    <iframe id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%" SRC='#'></iframe>
</div>

<input type="hidden" name="allGraphIds" value="" id="allGraphIds">
<input type="hidden" name="REPIds" value="" id="REPIds">
<input type="hidden" name="CEPIds" value="" id="CEPIds">
<input type="hidden" name="MsrIds" value="" id="MsrIds">
<input type="hidden" name="Measures" value="" id="Measures">
<input type="hidden" name="watifIds" value="" id="watifIds">
<input type="hidden" name="repExc" value="" id="repExc">
<input type="hidden" name="cepExc" value="" id="cepExc">
<input type="hidden" name="graphColumns" value="" id="graphColumns">
<input type="hidden" name="currGrpColId" value="" id="currGrpColId">


<div id="fade" class="black_overlay"></div>

<!--new graphs list div starts here-->
<div id="graphList" title="Add Graphs" style="display:none">
    <table width="100%">
        <tbody>
            

            <tr>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Bar_thumb.gif"  style="cursor:pointer;cursor:hand" title="Bar" height="100" width="100px" onclick="getGraphName(this,'Bar')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Bar3D_thumb.gif" style="cursor:pointer;cursor:hand" title="Bar3D" height="100" width="100px" onclick="getGraphName(this,'Bar3D')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Bubble_thumb.gif" style="cursor:pointer;cursor:hand" title="Bubble" height="100" width="100" onclick="getGraphName(this,'Bubble')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Column_thumb.gif" style="cursor:pointer;cursor:hand" title="Column" height="100" width="100" onclick="getGraphName(this,'Column')">&nbsp;</td>
            </tr>
            <tr>
                <td align="left">Bar</td>
                <td align="left">Bar3D</td>
                <td align="left">Bubble</td>
                <td align="left">Column</td>
            </tr>
            <tr>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Column3D_thumb.gif"  style="cursor:pointer;cursor:hand" title="Column3D" height="100" width="100px" onclick="getGraphName(this,'Column3D')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/ColumnPie_thumb.gif" style="cursor:pointer;cursor:hand" title="ColumnPie" height="100" width="100px" onclick="getGraphName(this,'ColumnPie')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/ColumnPie3D_thumb.gif" style="cursor:pointer;cursor:hand" title="ColumnPie3D" height="100" width="100" onclick="getGraphName(this,'ColumnPie3D')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Dual Axis_thumb.gif" style="cursor:pointer;cursor:hand" title="Dual Axis" height="100" width="100" onclick="getGraphName(this,'Dual Axis')">&nbsp;</td>
            </tr>
            <tr>
                <td align="left">Column3D</td>
                <td align="left">ColumnPie</td>
                <td align="left">ColumnPie3D</td>
                <td align="left">Dual Axis</td>
            </tr>
            <tr>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Line_thumb.gif"  style="cursor:pointer;cursor:hand" title="Line" height="100" width="100px" onclick="getGraphName(this,'Line')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Line3D_thumb.gif" style="cursor:pointer;cursor:hand" title="Line3D" height="100" width="100px" onclick="getGraphName(this,'Line3D')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Meter_thumb.gif" style="cursor:pointer;cursor:hand" title="Meter" height="100" width="100" onclick="getGraphName(this,'Meter')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Overlaid_thumb.gif" style="cursor:pointer;cursor:hand" title="Overlaid" height="100" width="100" onclick="getGraphName(this,'Overlaid')">&nbsp;</td>
            </tr>
            <tr>
                <td align="left">Line</td>
                <td align="left">Line3D</td>
                <td align="left">Meter</td
                <td align="left">Overlaid</td>
            </tr>
            <tr>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Pie_thumb.gif" style="cursor:pointer;cursor:hand" title="Pie" height="100" width="100px" onclick="getGraphName(this,'Pie')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Pie3D_thumb.gif" style="cursor:pointer;cursor:hand" title="Pie3D" height="100" width="100" onclick="getGraphName(this,'Pie3D')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Ring_thumb.gif"  style="cursor:pointer;cursor:hand" title="Ring" height="100" width="100px" onclick="getGraphName(this,'Ring')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Stacked_thumb.gif"  style="cursor:pointer;cursor:hand" title="Stacked" height="100" width="100px" onclick="getGraphName(this,'Stacked')">&nbsp;</td>
            </tr>
            <tr>
                <td align="left">Pie</td>
                <td align="left">Pie3D</td>
                <td align="left">Ring</td>
                <td align="left">Stacked</td>
            </tr>
            <tr>

                <td align="left"><img src="<%=request.getContextPath()%>/images/TimeSeries_thumb.gif" style="cursor:pointer;cursor:hand" title="TimeSeries" height="100" width="100px" onclick="getGraphName(this,'TimeSeries')">&nbsp;</td>
                <td align="left"><img src="<%=request.getContextPath()%>/images/Waterfall_thumb.gif" style="cursor:pointer;cursor:hand" title="Waterfall" height="100" width="100" onclick="getGraphName(this,'Waterfall')">&nbsp;</td>
            </tr>
            <tr>
                <td align="left">TimeSeries</td>
                <td align="left">Waterfall</td
            </tr>
        </tbody>
    </table>
</div>
<!--new graphs list div ends here-->

<!--style="cursor:hand;text-decoration:none" -->
<input type="hidden"  id="h" value="<%=request.getContextPath()%>">

<div>
    <iframe  id="graphCols" NAME='bucketDisp'  STYLE='display:none;'   class="white_content1" SRC=''></iframe>
</div>
<div id="graphDtlsDiv" title="Graph Details" style="display:none">
    <iframe  id="graphDtls" NAME='graphDtls' frameborder="0" width="100%" height="100%"  SRC='#'></iframe>
</div>
<div>
    <iframe  id="dispRowValues" NAME='dispRowValues'  class="white_content1" STYLE='display:none;' SRC=''></iframe>
</div>


<table width="100%" >
    <tr>
        <td height="10px">&nbsp;</td>
        <td height="10px">&nbsp;</td>
    </tr>
    <tr>
        <td height="10px">&nbsp;</td>
        <td align="center"><input type="button" class="navtitle-hover" value="Next" onclick="createWhatIfScenario()" style="width:auto">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="javascript:cancelWhatIfScenario();"></td>
    </tr>
    <tr>
        <td height="10px">&nbsp;</td>
        <td height="10px">&nbsp;</td>
    </tr>
</table>
<table style="width:100%">
    <tr>
        <td valign="top" style="width:100%;">
            <jsp:include page="Headerfolder/footerPage.jsp"/>
        </td>
    </tr>
</table>
<!--<div id="reportstart" class="navigateDialog" title="Navigation" style="display:none">
    <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" ></iframe>
</div>-->

<div id="dispTabProp" title="Table Properties" style="display:none">
    <iframe id="dispTabPropFrame" NAME='dispTabPropFrame' width="100%" height="100%"  frameborder="0" SRC='#'></iframe>
</div>

<div id="fadestart" class="black_start"></div>

<ul id=custMeasureListMenu" class="contextMenu" style="width:150px;text-align:left">
    <li class="addCustMeasure"><a href="#addCustMeasure">Add Measure</a></li>
</ul>

<div style="display:none" id="custmemDispDia" title="Custom Measures">
    <iframe  id="custmemDisp" NAME='custmemDisp' height="100%" width="100%" frameborder="0" SRC='#'></iframe>
</div>

<div style="display:none" id="paramDefaultVal" title="Parameter Default Values">
    <iframe  id="paramDefaultValDisp" NAME='paramDefaultValDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
</div>
<div style="display:none" id="paramSecurity" title="Parameter Security">
    <iframe  id="paramSecurityDisp" NAME='paramSecurityDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
</div>

<div style="display:none;height:330px;" id="rowParamDisplay" title="Row Parameters">
    <table style="width:100%;height:auto" border="1">
        <tr>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Available Row Parameters</font></div>
                <div style="height:230px;overflow:auto">
                    <ul id="availableRowParamSortable" class="availableRowParameters">

                    </ul>
                </div>
            </td>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Existing Row Parameters</font></div>
                <div id="dropingrowprms" style="height:230px;overflow:auto">
                    <ul id="existingRowParamSortable"  class="existingRowParameters">

                    </ul>
                </div>
            </td>
        </tr>
    </table>
    <center><input type="button" name="Done"  value="Done" onclick="getRowEdgeParams2()"></center>
</div>
<div style="display:none" id="colParamDisplay" title="Column Parameters">
    <table style="width:100%;height:auto" border="1">
        <tr>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Available Column Parameters</font></div>
                <div style="height:230px;overflow:auto">
                    <ul id="availableColParamSortable" class="availableColParameters">

                    </ul>
                </div>
            </td>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Existing Column Parameters</font></div>
                <div id="dropingcolprms" style="height:230px;overflow:auto">
                    <ul id="existingColParamSortable"  class="existingColParameters">

                    </ul>
                </div>
            </td>
        </tr>
    </table>
    <center><input type="button" name="Done"  value="Done" onclick="getColumnEdgeParams2()"></center>
</div>


<%--added by bhharathi reddy for adding param potions ends--%>

<%--added for Favourite Parameters--%>
<div id="favouriteParamsDialog" title="Save As Favourite" style="display:none">
    <iframe  id="favouriteParams" NAME='favouriteParams' width="100%" height="100%" frameborder="0" SRC='pbFavParameters.jsp'></iframe>
</div>
</form>
</body>
</html>
<%
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            } else {
                response.sendRedirect(request.getContextPath() + "/pbSessionExpired.jsp");
            }
%>

