<%--
    Document   : pbDashboardGroupKpi
    Created on : 19 Sep, 2011, 1:07:34 PM
    Author     : Veenadhari.g@progenbusiness.com
--%>

<%@page import="prg.db.Container,java.math.*,java.text.*,java.util.*,com.progen.report.DashletDetail,com.progen.report.entities.KPI,com.progen.report.KPIElement"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.pbDashboardCollection,prg.db.PbReturnObject,utils.db.ProgenConnection,prg.db.PbDb,com.progen.report.kpi.KPISingleGroupHelper"%>

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
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
        <style type="text/css">

            * {
                font-family: verdana;
                font-size: 11px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>


        <%

                    String KpiData = "";
                    String kpiType = "";
                    String dbrdId = "";
                    String divId = "";
                    HashMap map = new HashMap();
                    Container container = null;
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
                    if (request.getAttribute("dashletId") != null) {
                        divId = String.valueOf(request.getAttribute("dashletId"));
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
                     Set<String> GroupelementIds=new HashSet<String>();
                    pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                    DashletDetail detail = collect.getDashletDetail(divId);
                     if(detail.getSingleGroupHelpers()!=null && !detail.getSingleGroupHelpers().isEmpty()){
                    List<KPISingleGroupHelper> kPISingleGroupHelpers= detail.getSingleGroupHelpers();
                    for(KPISingleGroupHelper groupingHelper:kPISingleGroupHelpers){
                       if(groupingHelper.getElementIds()!=null && !groupingHelper.getElementIds().isEmpty()) {
                        for(String elmId:groupingHelper.getElementIds()){
                             GroupelementIds.add(elmId);
                        }
                        }
                      }
                    }
                    if (detail != null) {
                        KPI kpiDetails = (KPI) detail.getReportDetails();

                        if (kpiDetails != null) {
                            List<KPIElement> elems = kpiDetails.getKPIElements();


                            for (KPIElement elem : elems) {
                                 if (elem.getElementId().equalsIgnoreCase(elem.getRefElementId())){
                                if(!GroupelementIds.isEmpty()){
                                if (elem.getElementId().equalsIgnoreCase(elem.getRefElementId()) && !detail.isGroupElement(elem.getElementId())) {
                                       if(!GroupelementIds.contains(elem.getElementId())){
                                    kpielementsnos = kpielementsnos + "," + elem.getElementName();
                                    kpielementnames = kpielementnames + "," + elem.getElementId();
                                    }
                                    }
                                }
                                                               else{
                                    kpielementsnos = kpielementsnos + "," + elem.getElementName();
                                    kpielementnames = kpielementnames + "," + elem.getElementId();
                                  }
                                }
                            }
                           if(!kpielementsnos.equalsIgnoreCase("")){
                            kpielementsnos = kpielementsnos.substring(1);
                            kpielementnames = kpielementnames.substring(1);
                       }
                            }
                    }

                    ArrayList timeArray = (ArrayList) ParametersMap.get("TimeDetailstList");
                    HashMap timeDetsMap = (HashMap) ParametersMap.get("TimeDimHashMap");
                    if (timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        ArrayList month = (ArrayList) timeDetsMap.get("AS_OF_DATE");
                        String dateSql = "";

                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            dateSql = "select datepart(dd, convert(datetime,'" + month.get(0) + "',101))";
                        }
                        else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            dateSql = "select day(str_to_date('" + month.get(0) + "','%m/%d/%Y'))";
                        }
                        else {
                            dateSql = "select TO_NUMBER(TO_CHAR((TO_DATE('" + month.get(0) + "','MM/DD/YYYY')),'DD')) FROM DUAL";
                        }

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
        %>

      
    </head>

    <body onload="getElementDetails()">


        <form name="kpiGroupForm" id="kpiGroupForm" method="post" action="">
            <table align="center">
                <tr>
                    <td valign="top">
                        Enter Group Name&nbsp;<input type="text" value="" name="groupname" id="groupname">
                            </td><td>Calculation Type&nbsp;<select id="calculationType" name="calculationType">
                                    <option value="sum">Sum</option>
                                    <option value="avg">Average</option>
                                    <option value="None">None</option>
                                </select> </td><%--<td>
                                     <input type="button"  class="navtitle-hover" style="width:auto"  value="Done" onclick="saveKpis()">
                                </td>--%>
                </tr>
            </table>
            <div id="dragAndDropDiv" style="overflow:auto" >

            </div>
        </form><br />
        <center>
            <input type="button"  class="navtitle-hover" style="width:auto"  value="Done" onclick="saveKpis()">
        </center>
          <script type="text/javascript">
            var kpielements='<%=kpielementsnos%>';
            var kpinames='<%=kpielementnames%>';
            var assinIdAndVales=new Array
            var isMemberUseInOtherLevel="false"
            var y="";
            var xmlHttp;
            var ctxPath;
            var divId='<%=divId%>';
            function getElementDetails()
            {
                $.post(
                'dashboardTemplateViewerAction.do?templateParam2=getAllKpiDetails&kpielements='+encodeURIComponent(kpielements)+'&kpinames='+kpinames,
                   function(data){ 
                       var jsonVar=eval('('+data+')')
                        $("#dragAndDropDiv").html("")
                        $("#dragAndDropDiv").html(jsonVar.htmlStr)


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
                        grpColArray=jsonVar.memberValues
                        $(".sortable").sortable();
                   });
//                $.ajax({
//                    url:'dashboardTemplateViewerAction.do?templateParam2=getAllKpiDetails&kpielements='+encodeURI(kpielements)+'&kpinames='+kpinames,
//                    success:function(data){
//                        var jsonVar=eval('('+data+')')
//                        $("#dragAndDropDiv").html("")
//                        $("#dragAndDropDiv").html(jsonVar.htmlStr)
//
//
//                        isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
//                        $("#myList3").treeview({
//                            animated:"slow",
//                            persist: "cookie"
//                        });
//
//                        $('ul#myList3 li').quicksearch({
//                            position: 'before',
//                            attached: 'ul#myList3',
//                            loaderText: '',
//                            delay: 100
//                        })
//                        $(".myDragTabs").draggable({
//                            helper:"clone",
//                            effect:["", "fade"]
//                        });
//                        $("#dropTabs").droppable({
//                            activeClass:"blueBorder",
//                            accept:'.myDragTabs',
//                            drop: function(ev, ui) {
//                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
//                            }
//                        }
//                    );
//                        grpColArray=jsonVar.memberValues
//                        $(".sortable").sortable();
//                    }
//                })
            }

            function saveKpis(){

                var EleIds=new Array;
                 var kpiNames=new Array

                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");

                for(var i=0;i<liObj.length;i++){
//                     var content
                    mbrIds=(liObj[i].id).split("~");
                   EleIds.push(mbrIds[0].replace("_li", "", "gi"));
                   $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
                     kpiNames.push($(this).find("td").eq(1).html());
                    }
                    )

            }
            var groupName=$("#groupname").val();
            var calculationType=$("#calculationType").val();
            var dashLetID='<%=request.getAttribute("dashletId")%>'
            var dashboardId='<%=request.getAttribute("dbrdId")%>'
            var kpimasterid='<%=request.getAttribute("kpiMasterId")%>'
            var kpitype='<%=kpiType%>'
//                alert("dashLetID\t"+dashLetID+"\ndashboardId\t"+dashboardId+"\nkpimasterid\t"+kpimasterid)
               $.ajax({
                   url:'dashboardTemplateViewerAction.do?templateParam2=buildKpiGrouping&dashletId='+dashLetID+'&dashboardId='+dashboardId+'&kpimasterid='+kpimasterid+'&groupName='+encodeURIComponent(groupName)+'&calculationType='+calculationType+'&EleIds='+EleIds+'&kpiNames='+kpiNames+'&kpiType='+kpitype,
                   success:(function(data){parent.$("#kpisDialog").dialog('close');
                       parent.$("#kpisDialog1").dialog('close');
                   parent.displayKPI(dashLetID, dashboardId, "","",kpimasterid, "","","","false")
                   } )

                    });


            }

        </script>
    </body>
</html>

