<%-- 
    Document   : templateViewer
    Created on : 19 May, 2016, 10:17:09 AM
    Author     : Faiz Ansari
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%String contextPath = request.getContextPath();
    int USERID = Integer.parseInt(String.valueOf(session.getAttribute("USERID")));
     String templateId = request.getParameter("templateId");
     session.setAttribute("templateId",templateId);
     String templateName = session.getAttribute("templateName").toString();
     String templateDesc = session.getAttribute("templateDesc").toString();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi</title>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/font-awesome.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/latofonts.css"/>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/jqueryUI/jquery-ui.css"/>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/jqueryUI/jquery-ui.theme.css"/>  
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/Grid/jquery.gridster.css" />
        <link type="text/css" href="<%=contextPath%>/css/d3/tooltip.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/Green/TableCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/Green/ReportCss.css" rel="stylesheet"/>
        <!--<link type="text/css" href="<%=contextPath%>/css/d3/xtendChart.css" rel="stylesheet"/>-->
        <!--<link rel="stylesheet" href="<%=contextPath%>/template/datatable/datatables.min.css" type="text/css">-->
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/global.css"/>   
        <link type='text/css' rel='stylesheet' href="<%=contextPath%>/template/css/managementTemplate.css"/>
        <script type="text/javascript" src="<%=contextPath%>/JS/jquery-1.12.1.min.js"></script>

    </head>
    <body onload="init()">
        <div id='rootContainer' class="col-xs-12 row container-fluid"> 
            <div id='headerSection' class="col-xs-12 rmpm">
                <jsp:include page="../Headerfolder/headerPage.jsp"></jsp:include></div>
                <div id='contentSection' class="col-xs-12 rmpm">
                    <div id='managementTempl'>               
                        <div class="rootContainer">
                            <div id='firstKPI' class="topLeftSection">

                </div>
                            <div id='secondKPI' class="topRightSection">

                            </div>
                        </div>
                    </div> 
                    <div class="col-xs-12 topSection rmpm">
                        <div id='pageNavigation' class="col-xs-12 pageSection rmpm brd"></div>
                        <div class="col-xs-12 menuSection rmpm brd">
                            <i onclick='showMgtMenu()' class="fa fa-bars"></i>
                            <ul id='mgtMenu'>
                            <li><a href="javascript:void()" onclick="javascript:$('#mgtMenu').hide();" data-toggle="modal" data-target="#myModal">Select Template</a></li>
                            <li><a href="javascript:void()" onclick="saveTemplate();">Save Template</a>
                            </li>
                            </ul>
                        </div>
                    </div>
                    <div id='mgtTem2Left' class="col-xs-12 leftSection rmpm brd">
                        
                         <!-- chart div -->

            <div id="xtendChartssTD" style="display: none;vertical-align: top;margin-top: 30px">
                <div id="gridsterDiv" class="gridster" style="position:fixed;float: left;padding-bottom: 100px;margin-bottom: 138px;background-color: #ECEFF1; width:100% ;  overflow: auto;" ondrop="drop(event, this.id)" class="dragClass" ondragover="allowDrop(event)">
                    <ul id="gridUL" type="none" style="width: 100%;height:100%;">

                    </ul>
                </div>
            </div>
            <div id="xtendChartTD" style="display: none;vertical-align: top;margin: 40px 0px 0px 0px;">


                <div id="xtendProp" style="display:block;width:100%;height:auto;margin-top:0%" >
                </div>
                <div id="xtendChart" style="float:left;width:100%" ></div>
                <!--           <div id="addUnderConsideration" style="display: none" title="">
                                                     <img style="margin-left:27%" src="images/under_construction.png">
                              </div>-->
            </div>

            <!-- end of chart div -->
                    </div>
                    <div id='mgtTem2Right' class="col-xs-12 rightSection rmpm">
                    </div>
                </div>
                <div id='footerSection' class="col-xs-12 rmpm"></div>
            </div>
            <!--Modal-->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="vertical-alignment-helper">
                    <div class="modal-dialog vertical-align-center">
                        <div class="modal-content">
                            <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                             <h4 class="modal-title" id="myModalLabel">Select Template</h4>
                            </div>
                        <div id='templateDesignCont' class="modal-body" >
                            <div class="col-xs-4">
                                <input onclick="javascript:$('#mgtTemType').val(0);selectTemplate(0)" type="radio" name='templateOtp' checked="checked">Template 1<br>
                                <input onclick="javascript:$('#mgtTemType').val(1);selectTemplate(1)" type="radio" name='templateOtp'>Template 2<br>
                                <input onclick="javascript:$('#mgtTemType').val(2);selectTemplate(2)" type="radio" name='templateOtp'>Template 3<br>
                                <input onclick="javascript:$('#mgtTemType').val(3);selectTemplate(3)" type="radio" name='templateOtp'>Template 4<br>
                                <input id='mgtTemType' type="text" value='0' hidden="true">
                            </div>
                            <div id='templateDesign' class="col-xs-8">
                                <img data-toggle='modal' data-target='#imageFullModal' src='<%=contextPath%>/template/images/templateDesign1.png'>
                            </div>
                        </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button onclick="createMgTemKpis($('#mgtTemType').val())" type="button" class="btn btn-primary" data-dismiss="modal">Save changes</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--End Modal-->
             <!--Image Modal-->
        <!-- Modal fullscreen -->
        <div class="modal modal-fullscreen fade" id="imageFullModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
              </div>
              <div class="modal-body">
                  <img src="<%=contextPath%>/template/images/templateDesign1.png"/>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>
        <!--End Image Modal-->
        <!--<input id='ctxPath' type='text' value='<%=contextPath%>' hidden="true">-->
        <form action="" method='POST' id='templateForm'>
            <input type="hidden" id="templateId" name="templateId" value="<%=templateId%>"/>  
            <input type="hidden" id="templateName" name="templateName" value="<%=templateName%>"/>  
            <input type="hidden" id="templateDesc" name="templateDesc" value="<%=templateDesc%>"/>  
              <input type="hidden" name="measureNameMap" id="measureNameMap"/>
                <input type="hidden" name="measureIdMap" id="measureIdMap"/>
                <input type="hidden" name="pageMap" id="pageMap"/>
                <input type="hidden" name="pageIdMap" id="pageIdMap"/>
            
        </form>
        
            <form action="" method="POST" id="graphForm">
                <input type="hidden" id="viewby" name="viewby"/>
                <input type="hidden" id="viewbyIds" name="viewbyIds"/>
                <input type="hidden" id="groupbys" name="groupbys"/>
                <input type="hidden" name="measure" id="measure"/>
                <input type="hidden" name="measureIds" id="measureIds"/>
                <input type="hidden" name="defaultMeasure" id="defaultMeasures"/>
                <input type="hidden" name="defaultMeasureId" id="defaultMeasureId"/>
                <input type="hidden" name="aggregation" id="aggregation"/>
                <input type="hidden" id="templateId" name="templateId" value="template1"/>
                <input type="hidden" id="templateName" name="templateName" value="templateTest"/>
                <input type="hidden" id="usersId" name="usersId" value="<%=USERID%>"/>
            <input type="hidden" id="numOfCharts" name="numOfCharts"/>
            <input type="hidden" id="lines" name="lines"/>
            <input type="hidden" id="currLine" name="currLine" />
            <input type="hidden" id="currLineCharts" name="currLineCharts" />
            <input type="hidden" id="chartData" name="chartData" />
            <input type="hidden" id="chartMeta" name="chartMeta" />
            <input type="hidden" id="drills" name="drills" /> <input type="hidden" id="notfilters" name="notfilters" />
            <input type="hidden" id="filters1" name="filters1" />
            <input type="hidden" id="filtersmapgraph" name="filtersmapgraph" />
            <input type="hidden" id="driver" name="driver" />
            <input type="hidden" id="driverList" name="driverList" />
            <input type="hidden" id="ctxpath" name="ctxpath" value="<%=request.getContextPath()%>" />
            <input type="hidden" id="actionGO" name="ctxpath" value="<%=request.getParameter("action")%>" />
            <input type="hidden" id="drilltype" name="drilltype"  />
            <input type="hidden" id="drillFormat" name="drillFormat"  />
            <input type="hidden" id="type" name="type"  />
            <input type="hidden" id="chartType" name="chartType"  />
            <input type="hidden" id="visualChartType" name="visualChartType"  />
            <input type="hidden" id="currType" name="currType"  />
            <input type="hidden" id="changeVisualType" name="changeVisualType"  />
            <input type="hidden" id="tabDiv" name="tabDiv"  />
            <input type="hidden" id="legends" name="legends"  />
            <input type="hidden" name="conditionalMeasure" id="conditionalMeasure" />
            <input type="hidden" name="shadeType" id="shadeType" />
            <input type="hidden" name="isShaded" id="isShaded" value="false"/>
            <input type="hidden" name="conditionalMap" id="conditionalMap" />
            <input type="hidden" name="measureColor" id="measureColor"/>
            <input type="hidden" name="timeMap" id="timeMap"/>
            <input type="hidden" name="timeDetailsArray" id="timeDetailsArray"/>
            <input type="hidden" name="draggableViewBys" id="draggableViewBys"/>
            <input type="hidden" name="multigblrefresh" id="multigblrefresh"/>
            <input type="hidden" name="searchtype" id="searchtype"/>
            <!--added by shobhit for multi dashboard on 22/09/15--> 
            <input type="hidden" id="parentViewBy" name="parentViewBy"  />
            <input type="hidden" id="childViewBys" name="childViewBys"  />
            <input type="hidden" id="childMeasBys" name="childMeasBys"  />
            <input type="hidden" id="selectedViewBys" name="selectedViewBys"  />
            <input type="hidden" id="selectedMeasBys" name="selectedMeasBys"  />
            <input type="hidden" id="reportPageMapping" name="reportPageMapping"  />
            <input type="hidden" id="currencyType" name="currencyType"  />
            <input type="hidden" id="numberFormatMap" name="numberFormatMap"  />
            <input type="hidden" id="pageSequence" name="pageSequence"  />
            <input type="hidden" id="templateMeta" name="templateMeta"  />
            <input type="hidden" id="advanceChartType" name="advanceChartType"  />
            <input type="hidden" id="advanceChartData" name="advanceChartData"  />
        </form>

    </body>
    
    <script type="text/javascript" src="https://code.jquery.com/jquery-migrate-1.4.0.min.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/JS/jqueryUI/jquery-ui.js"></script>    
    <script type="text/javascript" src="<%=contextPath%>/JS/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/JS/jquery.nicescroll.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/JS/Grid/jquery.gridster.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/customtooltip.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/d3.v3.min.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/jquery.dataTables.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/d3.layout.cloud.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/topojson.v1.min.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypes.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/progenChartType.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypesGroup.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeOthers.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeBars.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeCircular.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeLine.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeMap.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeDashboard.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeCombo.js"></script>
    <script src="<%=contextPath%>/javascript/languageResource/MessageBundle_<%=session.getAttribute("UserLocaleFormat")%>.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=contextPath%>/JS/global.js"></script>	
    <script type="text/javascript" src="<%=contextPath%>/template/js/templateViewer.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/template/js/managementTemplate.js"></script>

    <script type="text/javascript">

                    var gridster1;
                    var widt = parseInt((((($(window).width() * .56)) - 35) / 4) / 8);
//                    var widt = $("#mgtTem2Left").width();
                    gridster1 = $(".gridster > ul").gridster({
                        widget_margins: [11, 11],
                        //        autogenerate_stylesheet: false,
                        widget_base_dimensions: [widt, widt],
                        min_cols: 29,
                        resize: {
                            enabled: false,
                            axes: ['both'],
                            handle_class: 'gs-resize-handle',
                            start: function (e, ui, $widget) {
                                //                    gridResize(e, ui,$widget);
                                //alert("id  "+($($widget).attr('id')).replace("div","" ))
                                //$('#renameTitle'+($($widget).attr('id')).replace("div","" )).quickfit({ max: 24, min: 14, truncate: true });


                            },
                            stop: function (e, ui, $widget) {
                                var newHeight = this.resize_coords.data.height;
                                var newWidth = this.resize_coords.data.width;
                                //                 gridResize(newHeight, newWidth);
                                //      alert("id  "+($($widget).attr('id')).replace("div","" ))
                                var chartId = $($widget).attr('id').replace("div", "");
                                localRefresh(chartId);
                            }
                        }
                        // edit by shivam
                    }).data('gridster').disable();



                    $(window).ready(function(){
//                        createTemplatePages(4, 'page1');
                        var default_Page = "default";
                        loadingPage(default_Page);
                    });
//                    $(window).resize(function(){
//                        var default_Page = "default";
//                        loadingPage(default_Page);
//                    });    
                    
                    function goPaths(path) {
                        var modulecode = path.replace("home.jsp#", "");
                        var ctxpath = '<%=request.getContextPath()%>';
                        $.ajax({
                            url: ctxpath + "/portalTemplateAction.do?paramportal=setModuleNameInSession&modulecode=" + modulecode,
                            success: function (data) {
                                parent.closeStart();
                                document.forms.searchForm.action = path;
                                document.forms.searchForm.submit();
                            }
                        });
                    }

                    function loadingPage(default_Page) {
                        resizeDivs();
                        generateTemplateData1st('<%= templateId%>', 'graph', gridster1);
                    }
                    function resizeDivs(){
                        $("#gridsterDiv").css({
                            'display': 'block',
                            'height':$("#mgtTem2Left").height(),
                            'width':$("#mgtTem2Left").width()
                        });
                        $("#xtendChartssTD").css({
                            'margin-top': '0px',
                            'height':$("#mgtTem2Left").height(),
                            'width':$("#mgtTem2Left").width(),
                            'overflow':'hidden'
                        });
                    }

    </script>
</html>