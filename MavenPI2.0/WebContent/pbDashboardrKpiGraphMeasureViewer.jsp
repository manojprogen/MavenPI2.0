<%--
    Document   : pbDashboardKpi
    Created on : Oct 5, 2009, 1:39:01 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page  contentType="text/html" pageEncoding="UTF-8" import="java.util.*,prg.db.Container" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
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
<!--        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
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
            <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
            <script>
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
            });
</script>
       
        <style>
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
    </head>
    <body onload="Viewer_prevKpis()">
        <%
            

            //////////.println("---->reportParameters=" + reportParameters);
            //////////.println("----->reportParametersValues=" + reportParametersValues);



            //////////.println("in viewer newly added jsp page");
            String KpiData = "";
            String kpiType = "";
            String dbrdId = "";
            HashMap map = new HashMap();
            Container container = null;

           //pbDashboardCollection collect = new pbDashboardCollection();



            long diffDays = 0;

            if (request.getAttribute("KpisGraphs") != null) {
                KpiData = String.valueOf(request.getAttribute("KpisGraphs"));
            }
            if (request.getAttribute("kpiType") != null) {
                kpiType = String.valueOf(request.getAttribute("kpiType"));
            }
            if (request.getAttribute("DashboardId") != null) {
                dbrdId = String.valueOf(request.getAttribute("DashboardId"));
                 ////.println("before dbrdId="+dbrdId);
            }
            ArrayList arl = new ArrayList();
            if (request.getAttribute("TimeDetailstList") != null) {
                arl = (ArrayList) (request.getAttribute("TimeDetailstList"));
            }

            //////////.println("ArrayList???????????????????=" + arl);

            if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
            }
            if (map.get(dbrdId) != null) {
                container = (prg.db.Container) map.get(dbrdId);
                ////.println("after dbrdId="+dbrdId);

            } else {
                container = new prg.db.Container();
            }


            HashMap ParametersMap = container.getParametersHashMap();

            ArrayList timeArray = (ArrayList) ParametersMap.get("TimeDetailstList");
            HashMap timeDetsMap = (HashMap) ParametersMap.get("TimeDimHashMap");


            HashMap reportParameters = new HashMap();
            HashMap reportParametersValues = new HashMap();
            String[] BuildedParamsWithTime =null;
            String[] BuildedParams= null;
            String BuildedParamsWithTimeStr = "";
            String BuildedParamsStr = "";
            ArrayList BuildedParamsForSession=new ArrayList();

            ////////////.println("reportParameters="+reportParameters);
            // //////////.println("reportParametersValues="+reportParametersValues);


            HashMap hmobj = container.getReportParameterHashMap();

           // //.println("container.getReportParameterHashMap()="+container.getReportParameterHashMap());

            if(hmobj!=null)
           {
                    reportParameters = (HashMap) hmobj.get("reportParameters");
                    reportParametersValues = (HashMap) hmobj.get("reportParametersValues");
                    if(reportParametersValues!=null)
                   {
                            BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);

                             for (int j = 0; j < BuildedParams.length; j++) {
                                BuildedParamsStr += "," + BuildedParams[j];

                            }

                            BuildedParamsStr = BuildedParamsStr.substring(1);


                            for(int i=0;i<BuildedParams.length;i++)
                                {
                                   BuildedParamsForSession.add(BuildedParams[i]);

                                }

                    }
           }


            //added by k

            if (timeArray == null) {
                
                HashMap hm = container.getTimeParameterHashMap();

                timeArray = (ArrayList) hm.get("TimeDetailstList");
               // //.println("before timeArray="+timeArray);

            }

            if (timeDetsMap == null) {

                HashMap hm = container.getTimeParameterHashMap();

                timeDetsMap = (HashMap) hm.get("TimeDimHashMap");
                ////////////.println("Dasboard Kpi hm="+hm);
                        if(timeDetsMap!=null)
                      {
                            BuildedParamsWithTime = (String[]) timeDetsMap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
                             for (int i = 0; i < BuildedParamsWithTime.length; i++) {
                                BuildedParamsWithTimeStr += "," + BuildedParamsWithTime[i];
                                // //////////.println("BuildedParamsWithTime="+BuildedParamsWithTime[i]);
                            }
                            BuildedParamsWithTimeStr = BuildedParamsWithTimeStr.substring(1);
                        }


            }


            //getting timedimm based on first two values of timeArray AAraylist

            String timeDimension = "";
             ////.println("after timeArray"+timeArray);

            if (timeArray.get(0).equals("Day") && timeArray.get(1).equals("PRG_STD")) {
                timeDimension = "Time-Period Basis";
            } else if (timeArray.get(0).equals("Day") && timeArray.get(1).equals("PRG_DATE_RANGE")) {
                timeDimension = "Time-Range Basis";
            } else if (timeArray.get(0).equals("Day") && timeArray.get(1).equals("PRG_DAY_ROLLING")) {
                timeDimension = "Time-Rolling Period";
            } else if (timeArray.get(0).equals("Day") && timeArray.get(1).equals("PRG_MONTH_RANGE")) {
                timeDimension = "Time-Month Range Basis";
            } else if (timeArray.get(0).equals("Day") && timeArray.get(1).equals("PRG_QUARTER_RANGE")) {
                timeDimension = "Time-Quarter Range Basis";
            } else if ((timeArray.get(0).equals("Day") && timeArray.get(1).equals("PRG_YEAR_RANGE")) ||
                    (timeArray.get(0).equals("WEEK") && timeArray.get(1).equals("PRG_WEEK_CMP")) ||
                    (timeArray.get(0).equals("Month") && timeArray.get(1).equals("PRG_STD")) ||
                    (timeArray.get(0).equals("QUARTER") && timeArray.get(1).equals("PRG_QUARTER_CMP")) ||
                    (timeArray.get(0).equals("YEAR") && timeArray.get(1).equals("PRG_YEAR_CMP"))) {
                timeDimension = "Time-Year Range Basis";
            } else {
                timeDimension = "";
            }


                    //////////.println("timeDimension="+timeDimension);



            //////////.println("BuildedParamsStr=" + BuildedParamsStr);
            //////////.println("BuildedParamsWithTimeStr=" + BuildedParamsWithTimeStr);


            //////////.println("in jsp KpiData=" + KpiData);
            //////////.println("in jsp kpiType=" + kpiType);
            //////////.println("in jsp dbrdId=" + dbrdId);
            //////////.println("in jsp map=" + map);
            //////////.println("timeArray in jsp is: " + timeArray);
            //////////.println("timeDetsMap in jsp is: " + timeDetsMap);



            //setting time parameters ,timedetailsList to session ,so we can retrive from getNeedlevalue() from dashBoardTemplateActionViewer class
             HttpSession hs = request.getSession(false);


            hs.setAttribute("MyParameters", BuildedParamsForSession);
            hs.setAttribute("MyTimeDetailstList", timeArray);

            //////////.println("MyTimeDetailstList="+timeArray);
            //////////.println("MyParameters="+BuildedParamsForSession);

            String timedim = timeArray.get(1).toString();
            //////////.println("timedim="+timedim);
        %>
        <form name="myForm2" method="post">
            <table style="width:100%;height:220px" border="solid black 1px" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag KPI from below</font></div>
                        <div class="masterDiv" style="height:200px;overflow-y:auto">
                            <ul id="kpiTree" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="kpis">
                                        <%=KpiData%>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td width="50%" valign="top"  id="draggableKpis" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Drop KPI Here </font>
                        </h3>
                        <div id="dragDiv" style="height:200px;overflow-y:auto">
                            <ul id="sortable">
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
             <input type="hidden" name="kpis" id="kpis" value="" >
            <input type="hidden" name="kpiIds" id="kpiIds" value="" >
            <input type="hidden" name="timeDim" id="timeDim" value="<%=timedim%>" >
        </form>
                                    <br/>
        <center>
            <%if (timedim.equalsIgnoreCase("PRG_STD")) {%>
            <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="Viewer_saveKpisGraphType()">
            <%} else {%>
            <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="Viewer_saveKpisGraph()">
            <%}%>
            <input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelRepKpiGraphm()">
        </center>
             <script>
            var y="";
            var xmlHttp;
            var ctxPath;

            function Viewer_saveKpisGraph(){

            ////alert("Viewer_saveKpisGraph")
            Viewer_dispKpisGraphsm();
               // dispKpisGraph();
               
            }
            function Viewer_saveKpisGraphType(){
                      ////alert("Viewer_saveKpisGraphType")
                 Viewer_dispKpisGraphsmTargetType();
            }
            function cancelRepKpiGraphm(){
               // //alert('hi')
                parent.cancelRepKpiGraph();
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
            });

            $(function() {
                var dragKpi=$('#kpis > li > ul > li > ul > li >span,#kpis > li > ul > li >  span')
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
                        createKpisGraph(ui.draggable.html(),ui.draggable.attr('id'));
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });

        </script>

    </body>
</html>
