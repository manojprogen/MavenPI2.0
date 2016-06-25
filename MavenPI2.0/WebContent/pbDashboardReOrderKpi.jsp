<%-- 
    Document   : pbDashboardReOrderKpi
    Created on : 20 Jul, 2012, 4:43:24 PM
    Author     : Ramesh
--%>

<%@page import="prg.db.PbDb,prg.db.Container,java.math.*,java.text.*,java.util.*,com.progen.report.DashletDetail,com.progen.report.KPIElement,com.progen.report.entities.KPI"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.ProgenConnection,prg.db.PbReturnObject,com.progen.report.pbDashboardCollection,com.progen.report.kpi.KPISingleGroupHelper,utils.db.ProgenConnection,com.google.common.base.Joiner"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                    session.setAttribute("theme", themeColor);
                } else {
                    themeColor = String.valueOf(session.getAttribute("theme"));
                }
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard Kpis</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <!--<script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/docs.js"></script>-->
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>


          <%

            String KpiData = "";
            String kpiType = "";
            String dbrdId = "";
            String divId = "";
            HashMap map = new HashMap();
            Container container = null;
            String kpiidsStr = "";
            String kpnamesStr = "";
            long diffDays = 0;

            if (request.getAttribute("Kpis") != null) {
                KpiData = String.valueOf(request.getAttribute("Kpis"));
            }
            if (request.getAttribute("kpiType") != null) {
                kpiType = String.valueOf(request.getAttribute("kpiType"));
            }
            if (request.getAttribute("dbrdId") != null) {
                dbrdId = String.valueOf(request.getAttribute("dbrdId"));
            }
           if (request.getAttribute("divId") != null) {
                divId = String.valueOf(request.getAttribute("divId"));
            }
            if(request.getAttribute("kpiidsStr")!=null) {
                kpiidsStr = String.valueOf(request.getAttribute("kpiidsStr"));
            }
            if(request.getAttribute("kpinamesStr")!=null) {
                kpnamesStr = String.valueOf(request.getAttribute("kpinamesStr"));
            }


            if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
            }
            if (map.get(dbrdId) != null) {
                container = (prg.db.Container) map.get(dbrdId);
            } else {
                container = new prg.db.Container();
            }
            HashMap ParametersMap = container.getParametersHashMap();

            String kpielementsnos = "";
            String kpielementnames = "";
            pbDashboardCollection collect = (pbDashboardCollection)container.getReportCollect();
            DashletDetail detail = collect.getDashletDetail(divId);
            if(detail!=null){
            KPI kpiDetails=null;
                   if(detail.getDisplayType() !=null && detail.getDisplayType().equalsIgnoreCase("KpiWithGraph")){
                        kpiDetails=detail.getKpiDetails();
                        if(kpiDetails==null){
                            kpiDetails = (KPI) detail.getReportDetails();
                            }
                    }else{
                      kpiDetails = (KPI) detail.getReportDetails();
                    }
            if(kpiDetails!=null){
            List<KPIElement> elems = kpiDetails.getKPIElements();


            for (KPIElement elem:elems){
                if(elem.isIsGroupElement()){
                    KPISingleGroupHelper helper=null;
                    for(KPISingleGroupHelper groupHelper:detail.getSingleGroupHelpers()){
                    if(groupHelper.getGroupName()!=null && groupHelper.getGroupName().equalsIgnoreCase(elem.getElementName())){
                    helper=groupHelper;
                    break;
                    }
                    }
                    if(helper != null && helper.getGroupName()!= null && kpielementsnos != null){
                 kpielementsnos = kpielementsnos + "," +helper.getGroupName();
                    kpielementnames = kpielementnames + "," + helper.getGroupName();
                    }
                } else{
                if (elem.getElementId().equalsIgnoreCase(elem.getRefElementId())){
                    kpielementsnos = kpielementsnos + "," + elem.getElementName();
                    kpielementnames = kpielementnames + "," + elem.getElementId();
                }
                }
            }
        //    String kpiGroupElements = "";
       //    if(detail.getSingleGroupHelpers()!=null && !detail.getSingleGroupHelpers().isEmpty()){
      //          List<KPISingleGroupHelper> kPISingleGroupHelpers= detail.getSingleGroupHelpers();
     //           for(KPISingleGroupHelper groupingHelper:kPISingleGroupHelpers){
    //                kpiGroupElements = kpiGroupElements + "," + groupingHelper.getGroupName();

                     //          for(String elementID:groupingHelper.getElementIds()){
                   //        if(groupingHelper.getCalcType().equalsIgnoreCase("sum")){
                  //             
                  //  kpiGroupElementIds = Joiner.on("+").join(groupingHelper.getElementIds());
                  //  }else{
                  //  kpiGroupElementIds = Joiner.on("~").join(groupingHelper.getElementIds());
                  //      }
                  //  kpiGroupElementIds =  kpiGroupElementIds +","+kpiGroupElementIds;
                  //  }
   //            }
   //             }
  //            kpielementsnos = kpielementsnos + kpiGroupElements;
 //             kpielementnames = kpielementnames + kpiGroupElements;
            kpielementsnos = kpielementsnos.substring(1);
            kpielementnames = kpielementnames.substring(1);
            }
        }

            ArrayList timeArray = (ArrayList) ParametersMap.get("TimeDetailstList");
                HashMap timeDetsMap = (HashMap) ParametersMap.get("TimeDimHashMap");
            if (timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                ArrayList month = (ArrayList) timeDetsMap.get("AS_OF_DATE");
                String dateSql="";

                 if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
                 dateSql = "select datepart(dd, convert(datetime,'"+ month.get(0) +"',101))";
                else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL))
                    dateSql = "select day(str_to_date('"+ month.get(0) +"','%m/%d/%Y'))";
                else
                  dateSql = "select TO_NUMBER(TO_CHAR((TO_DATE('" + month.get(0) + "','MM/DD/YYYY')),'DD')) FROM DUAL";

                PbDb pbdb = new PbDb();
                PbReturnObject retObj = pbdb.execSelectSQL(dateSql);
                if (retObj.getRowCount() > 0) {
                    diffDays = retObj.getFieldValueInt(0, 0);
                }
            } else {
                ArrayList fromDate = (ArrayList) timeDetsMap.get("AS_OF_DATE1");
                ArrayList toDate = (ArrayList) timeDetsMap.get("AS_OF_DATE2");
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date fromdt = df.parse(String.valueOf(fromDate.get(0)));
                Date todt = df.parse(String.valueOf(toDate.get(0)));
                diffDays = (todt.getTime() - fromdt.getTime()) / (24 * 60 * 60 * 1000);
            }
                int size = kpnamesStr.length()-1;
                kpnamesStr = kpnamesStr.substring(0,size);
        %>
        <script type="text/javascript">
            var y="";
            var xmlHttp;
            var ctxPath;
            function saveKpis(dayDiff,kpiType,divId){
                reorderKpis(dayDiff,kpiType,divId);
            }
            function cancelKpi(){
                parent.cancelRepKpi();
            }

            $(document).ready(function() {
                $("#kpiTree").treeview({
                    animated: "normal",
                    unique:true
                });

                //addeb by bharathi reddy fro search option
                $('ul#kpiTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#kpiTree',
                    loaderText: '',
                    delay: 100
                })
                $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView(action, el, pos);
            });
                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });
            });

            $(function() {
                var dragKpi=$('#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span')
                var dropKpis=$('#draggableKpis');

                $(dragKpi).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropKpis).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var kpi=ui.draggable.html();
                        createKpis(ui.draggable.html(),ui.draggable.attr('id'));
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }


        </script>

        <style type="text/css">
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#EAF2F7;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
            *{font:11px verdana}
        </style>

        <script type="text/javascript">
            var divId=parent.document.getElementById("divId").value;
            var kpielements='<%=kpnamesStr %>';
            var kpinames='<%=kpiidsStr%>';

            prevKpis(divId);

            function getElementDetails()
            {
               
                var kpiid=kpielements
                var Eleid=kpinames


                var kpiidarray= kpiid.split(",");
                var elearray=Eleid.split(",");
                var part_num=0;
                while (part_num < kpiidarray.length)
                {
                    createDynamicKpis(kpiidarray[part_num],elearray[part_num])
                    part_num+=1;
                }
              }

                          function createDynamicKpis(kpiName,elementName){
                              var i=0;
                              var parentUL=document.getElementById("sortable");
                              var parentDiv=parent.document.getElementById("editDispKpi");
                              var x=kpiArray.toString();
                              if(x.match(elementName)==null){
                                  kpiArray.push(kpiName+"^"+elementName)
                                  var childLI=document.createElement("li");
                                  childLI.id=kpiName+"^"+elementName;
                                  childLI.style.width='auto';
                                  childLI.style.height='auto';
                                  childLI.style.color='white';
                                  childLI.className='navtitle-hover';
                                  var table=document.createElement("table");
                                  table.id=kpiName+i;
                                  var row=table.insertRow(0);
                                  var cell1=row.insertCell(0);
                                  var a=document.createElement("a");
                                  a.href="javascript:deleteKpi('"+kpiName+'^'+elementName+"')";
                                  a.innerHTML="a";
                                  a.className="ui-icon ui-icon-close";
                                  cell1.appendChild(a);
                                  var cell2=row.insertCell(1);
                                  cell2.style.color='black';
                                  cell2.innerHTML=kpiName;
                                  childLI.appendChild(table);
                                  parentUL.appendChild(childLI);
                                  i++;
                              }
                              $("#sortable").sortable();
                              $("#sortable").disableSelection();
                          }
        </script>
    </head>
    <script>
        var divId=parent.document.getElementById("divId").value;
        prevKpis(divId);
    </script>
    <body onload="getElementDetails()">


        <form name="myForm2" method="post">
            <table style="width:50%;height:220px" border="solid black 1px" align="center">
                <tr>
                   
                    <td width="50%" valign="top"  id="draggableKpis" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">ReOrder Kpi Sequence</font>
                        </h3>
                        <div id="dragDiv" style="height:200px;overflow-y:auto">
                            <ul id="sortable">
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="dayDiff" id="dayDiff" value="<%=diffDays%>">
            <input type="hidden" name="kpiType" id="kpiType" value="<%=kpiType%>">
        </form>
        <br/>
        <center>
            <input type="button"  class="navtitle-hover" style="width:auto" value="Done" onclick="saveKpis('<%=diffDays%>','<%=kpiType%>',<%=divId%>)">
            <%--<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelKpi()">--%>
        </center>
        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
        </ul>
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
            <table>
                <tr>
                    <td id="value"></td>
                </tr>
                </table>
                    </div>
    </body>
</html>


