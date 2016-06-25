<%@page import="com.progen.i18n.TranslaterHelper,java.util.Map,com.progen.report.PbReportCollection,com.progen.report.data.TableDataRow,com.progen.report.data.TableRow,com.progen.users.UserLayerDAO,org.apache.log4j.*"%>
<%@page import="com.progen.action.UserStatusHelper,com.google.gson.reflect.TypeToken,com.google.gson.Gson,com.progen.reportview.bd.PbReportViewerBD,com.progen.db.ProgenDataSet,prg.db.ContainerConstants,com.google.common.collect.ArrayListMultimap"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List,java.util.Iterator,java.util.Set,prg.db.PbReturnObject,prg.db.PbDb,java.util.ArrayList,java.util.HashMap,java.util.Vector,java.math.BigDecimal,java.text.NumberFormat,java.util.Locale,com.progen.users.PrivilegeManager"%>
<%@page import="java.math.MathContext,prg.db.Container,java.net.URLEncoder,com.progen.buildtable.PbBuildTable,com.progen.reportview.db.PbReportViewerDAO,prg.util.screenDimensions,com.progen.query.RTMeasureElement,com.progen.log.ProgenLog"%>
<%
Logger logger=Logger.getLogger("pbDisplay.jsp");
//for clearing cache

    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    String cellSpanJSON = "";
    String tableHTML = "";
    String menuHTML = "";
    String measureHeaderColor="";
String elementId="";
String srchsubtotal="";
String Idsubtotal="";
String srchConditionnew="";
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
    ArrayList viewbynames = new ArrayList();
    ArrayList viewbyids = new ArrayList();
    ArrayList ViewbyCount1 = new ArrayList();
    ArrayList CEP = new ArrayList();
    ArrayList CEPNames = new ArrayList();
    ArrayList sortColumns = new ArrayList();
    boolean excelDisplay = false;
    boolean headLines = false;
    boolean portalViewer = false;
    boolean reportAnalyzer = false;
    boolean whatif = false;
    int vcount = 0;
    String rtCols = "";
    boolean targetApplicable = false;
    int USERID = 0;
    boolean refreshGraph = true;
    ArrayList timeDetails = new ArrayList();
    boolean isSplitBy = false;
    HashMap<String, ArrayList> splitMap = new HashMap<String, ArrayList>() {

        {
            put("Day", new ArrayList() {

                {
                    add("Week");
                    add("Month");
                }
            });
            put("Week", new ArrayList() {

                {
                    add("Month");
                    add("Qtr");
                }
            });
            put("Month", new ArrayList() {

                {
                    add("Qtr");
                    add("Year");
                }
            });
            put("Qtr", new ArrayList() {

                {
                    add("Year");
                }
            });
            put("Year", new ArrayList() {

                {
                    add("Year");
                }
            });
        }
    };
    if (session.getAttribute("USERID") != null) {
        USERID = Integer.parseInt((String) session.getAttribute("USERID"));
    }
     String Opertion = (String)session.getAttribute("searchOpetartion");
     
 String Value1 = (String)session.getAttribute("Value"); 

  
    String singleselection=(String)session.getAttribute("singleselection");
    
    screenDimensions dims = new screenDimensions();
    int pageFont, anchorFont;
    HashMap screenMap = dims.getFontSize(session, request, response);
    ////.println("screenMap --" + screenMap.size());
    if (!String.valueOf(screenMap.get("pageFont")).equalsIgnoreCase("NULL")) {
        pageFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
        anchorFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
        ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
    } else {
        pageFont = 11;
        anchorFont = 11;
        ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
    }
    PbBuildTable buildTable = new PbBuildTable();
    PbReportViewerDAO dao = new PbReportViewerDAO();
    String tabName = null;
    String sqlString = "";
    int containerFromRow = 0, containerToRow = 0;
    tabName = request.getParameter("tabId");
//added By manoj 
     measureHeaderColor=request.getParameter("HeaderColorNew");
     elementId=request.getParameter("elemntIdNew");
       srchConditionnew=request.getParameter("srchConditionnew");
      Idsubtotal=request.getParameter("sortColumn");
      srchsubtotal=request.getParameter("sort");
     Container containerNew = null;
     HashMap mapNew = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
      containerNew = (Container) mapNew.get(tabName); 
      if(Idsubtotal != null && srchsubtotal != null)
                   {
          containerNew.setIdsubtotal(Idsubtotal);
         containerNew.setSubtotalsort(srchsubtotal);
                   }
      if(measureHeaderColor!=null &&  elementId != null && srchConditionnew != null)
                   {
           
      if(srchConditionnew.equals("true") ||srchConditionnew.equals("false") )
                   {
     containerNew.setONClickInformation(measureHeaderColor);
     containerNew.setElementIdvalue(elementId);
     containerNew.setclearInformation(srchConditionnew);
     
         }
          }
     
 //end  
     
String priorenable=request.getParameter("priorEnable");  
        if(priorenable!=null && !priorenable.isEmpty() && priorenable.equalsIgnoreCase("priorEnable")){
         boolean data=true;
            containerNew.setPriorEnableFlag(data);  
        }
         String priorEnableReset=request.getParameter("priorEnableReset");  
        if(priorEnableReset!=null && !priorEnableReset.isEmpty() && priorEnableReset.equalsIgnoreCase("priorEnableReset")){
         boolean data=false;
            containerNew.setPriorEnableFlag(data);  
        }

    boolean showExtraTabs = true;
    if (tabName == null && request.getAttribute("tabId") != null) {
        tabName = (String) request.getAttribute("tabId");
    }
    if (session.getAttribute("USERID") == null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")) {
        response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
    } else {
        //added by Mohit Gupta for default locale
                    Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
        //ended By Mohit Gupta
        String PbReportId = tabName;
        PbDb pbdb = new PbDb();
        String isGraphThere = null;
        String themeColor = "blue";
        try {
            int reportId = Integer.parseInt(PbReportId);
            String grfqry = "select * from prg_ar_graph_master where report_id='" + PbReportId + "'";
            PbReturnObject grfobj = new PbReturnObject();
            ////.println("grfqry is : "+grfqry);
            grfobj = pbdb.execSelectSQL(grfqry);
            if (grfobj.getRowCount() != 0) {
                isGraphThere = "Yes";
            } else {
                isGraphThere = "No_Graphs";
            }
        } catch (NumberFormatException nfe) {
            isGraphThere = "No_Graphs";
        }
        // isGraphThere = "No_Graphs";
        if (session.getAttribute("theme") == null) {
            session.setAttribute("theme", themeColor);
        } else {
            themeColor = String.valueOf(session.getAttribute("theme"));
        }
        UserStatusHelper ushelpers = new UserStatusHelper();
        if (session.getAttribute("userstatushelper") != null) {
            ushelpers = (UserStatusHelper) session.getAttribute("userstatushelper");
            boolean isActive = ushelpers.getIsActive();
            headLines = ushelpers.getHeadlines();
            portalViewer = ushelpers.getPortalViewer();
            reportAnalyzer = ushelpers.getReportAnalyzer();
            whatif = ushelpers.getWhatif();
        }
        String isreportdrillPopUp = request.getParameter("isreportdrillPopUp");
        //   
//                added by manik
        int widpopup = 0;
        int heipopup = 0;
        String ww = request.getParameter("widpopup");
        String hh = request.getParameter("heipopup");
        if (isreportdrillPopUp != null && isreportdrillPopUp.equalsIgnoreCase("true")) {
            widpopup = Integer.parseInt(ww);
            heipopup = Integer.parseInt(hh);
        }
        // End for Table Display Region
//
        String isrefreshrowcount = request.getParameter("isrefreshrowcount");


        boolean isPowerAnalyserEnableforUser = false;
        ServletContext context = getServletContext();
        HashMap<String, UserStatusHelper> statushelper;
        if (context.getAttribute("helperclass") != null) {
            statushelper = (HashMap) context.getAttribute("helperclass");
            UserStatusHelper helper = new UserStatusHelper();
            if (!statushelper.isEmpty()) {
                helper = statushelper.get(request.getSession(false).getId());
                if (helper != null) {
                    isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
                }
            }
        }

        UserLayerDAO userdao = new UserLayerDAO();
        HashMap paramhashmapPA = new HashMap();
        String userType = userdao.getUserTypeForFeatures(USERID);
        paramhashmapPA = userdao.getFeatureListAnaLyzer(userType, USERID);
        Container containerSet = null;
        HashMap map1 = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        containerSet = (Container) map1.get(tabName);
        containerSet.setparametersHash(paramhashmapPA);
        containerSet.setuserTypeAdmin(userType);
        boolean isTopbottomTableEnable = containerSet.isTopBottomTableEnable();
       String ctxpath=request.getContextPath();
%>
<html>
    <head>
       <link rel="stylesheet" type="text/css" href="<%= ctxpath%>/css/latofonts.css"/>
        <link rel="stylesheet" href="<%=ctxpath%>/css/font-awesome.min.css" type="text/css"/>
        <script type="text/javascript" src="<%= ctxpath%>/JS/global.js"></script>
        <meta http-equiv="Content-type" content="text/html;charset=UTF-8">
        <script type="text/javascript" src="<%= ctxpath%>/javascript/reportviewer/graphViewer.js"></script>
        <script src="<%= ctxpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=ctxpath%>/javascript/lib/jquery/js/newJquerySheet.js" type="text/javascript"></script>
        <script src="<%=ctxpath%>/javascript/lib/jquery/js/jqcontextmenu.js" type="text/javascript"></script>
        <script src="<%=ctxpath%>/javascript/lib/jquery/js/colorpicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <% if (request.getParameter("fromOnview") != null && request.getParameter("fromOnview").equalsIgnoreCase("true")) {%>
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/OneviewCss.css" rel="stylesheet" />
        <%} else {%>
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <%}%>
        <script src="<%=ctxpath%>/TableDisplay/JS/pbDisplayJS.js" type="text/javascript"></script>
        <script src="<%=ctxpath%>/TableDisplay/.jquery.handsontable.js" type="text/javascript"></script>
        <link type="text/css" href="<%=ctxpath%>/TableDisplay/jquery.handsontable.css" rel="stylesheet" />
        <% if (browserType.equalsIgnoreCase("IE")) {%>
        <link rel="stylesheet" type="text/css" href="<%=ctxpath%>/stylesheets/jquery.iesheet.css"/>
        <%} else {%>
        <link rel="stylesheet" type="text/css" href="<%=ctxpath%>/stylesheets/jquery.sheet.css"/>
        <%}%>
        <link href="<%=ctxpath%>/css/styles.css" rel="stylesheet" type="text/css">
        <link href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">
        <link href="<%=ctxpath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="<%=ctxpath%>/stylesheets/colorpicker.css" />
        <link rel="stylesheet" type="text/css" href="<%=ctxpath%>/stylesheets/jqcontextmenu.css"/>
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/tooltip.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/toggle-button.css" rel="stylesheet" />
        <script src="<%=ctxpath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=ctxpath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/pbReportViewerJS.js"></script>
        <link rel="stylesheet" href="<%=ctxpath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
          <link type="text/css" href="<%=ctxpath%>/css/global.css" rel="stylesheet" />
<!--          <script type="text/javascript" src="<%= request.getContextPath()%>/dragAndDropTable.js"></script>

        <link rel="stylesheet" href="<%= request.getContextPath()%>/bootstrap-3.2.0-dist/css/bootstrap.min.css">
      <link rel="stylesheet" href="<%= request.getContextPath()%>/bootstrap-3.2.0-dist/css/bootstrap-theme.min.css">
      <script src="<%= request.getContextPath()%>/bootstrap-3.2.0-dist/js/bootstrap.min.js"></script>
        -->
        
        <style type="text/css">

            /*html>body div.scrollable myTableBody {
                height: 100px;
                overflow: auto;
            }*/
            /* div table.prgtable {
                         width:99%;
                 }*/
            /*
            div.scrollable table.headerTable
            {
                position: absolute;
            }
            div.scrollable table.dataTable thead
            {
                visibility: hidden;
            }*/
            .ui-progressbar-value { background-image: url(images/barchart.gif); }
            table.grid .collapsible {
                padding: 0 0 3px 0;
            }

            .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(<%=request.getContextPath()%>/images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }

            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(<%=request.getContextPath()%>/images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
            .tabMenuCol1
            {
                background-color:#ffffff;
            }
            .myTableBodyClass {
                min-height:inherit;
                height: 225px;
                overflow: auto;
            }
            .myTableBodyClassFor800 {
                min-height:inherit;
                height: 150px;
                overflow:  auto;

            }
            .myTableBodyClass_limit {
                height: auto;
                overflow: auto;
            }

            .myTableBodyClassOnlyTable {
                height: 520px;
                overflow: auto;
            }
            a {cursor:pointer;font-size:<%=anchorFont%>px;text-decoration:none}
            #colorSelector, #colorSelector1 {
                position: relative;
                width: 36px;
                height: 36px;
                background: url("Images/select.png");
            }
            #colorSelector div,#colorSelector1 div {
                position: absolute;
                top: 3px;
                left: 3px;
                width: 30px;
                height: 30px;
                background: url("Images/select.png") center;
            }

            /*            #paramHeaderId { position: absolute; top: 0px; left:0px; margin: auto; right: 0px; width: 60%; border: none; z-index: 50; background: transparent }*/

        </style>
       
    </head>
    <body class="background" onload="makeScrollableTable('displayTable',false,'auto');" onclick='hideEle();'>
        <%
            String ViewFrom = (String) session.getAttribute("ViewFrom");
            if (ViewFrom == null) {
                ViewFrom = "";
            }

            if (request.getSession(false) != null) {
                try {
                    Container container = null;
                    //ProgenLog.log(ProgenLog.FINE, this, "pbDisplay.jsp", "Enter");
                    logger.info("Enter");
                    //String currPath = request.getParameter("path");
                    //String ctxPath = request.getContextPath();
                    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                    HashMap TableHashMap = null;
                    int columnViewbyCount = 0;
                    int ViewbyCount = 0;

                    if (map != null) {
                        container = (Container) map.get(tabName);
                        HashMap ReportHashMap = container.getReportHashMap();
                        TableHashMap = container.getTableHashMap();
                        String[] repBisRoles = (String[]) ReportHashMap.get("ReportFolders");
                        StringBuffer sbufRoles = new StringBuffer("");
                        for (int k = 0; k < repBisRoles.length; k++) {
                            sbufRoles.append("," + repBisRoles[k]);
                        }
                        PbReportCollection collect = container.getReportCollect();
                        timeDetails = collect.timeDetailsArray;
                        isSplitBy = container.isSplitBy();
                        // request.getSession().setAttribute("currentReportId",tabName);
                        // request.getSession().setAttribute("currentBizRoles",sbufRoles.toString().substring(1));
                        // container.setFromRow(0);
                        String reportMode = container.getReportMode();
                        String c_img_first = "Images/firstPage1.png";//"Images/first.GIF";
                        String c_img_up = "Images/prevPage1.png";//"Images/up.GIF";
                        String c_img_down = "Images/nextPage1.png";//"Images/down.GIF";
                        String c_img_last = "Images/lastPage1.png";//"Images/last.GIF";
                        String c_img_refresh = "Images/refresh.GIF";
                        String c_img_separator = "Images/separator.gif";
                        String c_img_rowsDisplayed = "Images/rowsDisplayed.gif";

                        //added by santhosh.kumar@progenbusiness.com
                        ArrayList tableRemainingColumns = container.getTableRemainingColumns();
                        ArrayList tableRemainingDisplayColumns = container.getTableRemainingDisplayColumns();
                        BigDecimal dividend = null;

                        String hideTableStr = "";
                        //String reportType = container.getReportType();
                        HashMap ColumnsVisibility = container.getColumnsVisibility();
                        String PrevShownCols = "";
                        String drillColumnIds = "";
                        String TableId = container.getGraphTableId();
                        ArrayList columnviewbys=new ArrayList();
                        int start = 1;
                        start = (container.getViewByCount());
                        columnViewbyCount = Integer.parseInt(container.getColumnViewByCount());
                        ViewbyCount = (container.getViewByCount());
                        ViewbyCount1 = (container.getViewByElementIds());
                        CEP = (ArrayList) TableHashMap.get("CEP");                                                                    
                        sortColumns = container.getSortColumns();
                        logger.info("ViewbyCount1...............:" + ViewbyCount1);
                        

                        //upto here

                        String swapFrom = request.getParameter("swapFrom");
                        String swapTo = request.getParameter("swapTo");
                        String pageSize = request.getParameter("slidePages");
                        try{
                         int psize=Integer.parseInt(pageSize);
                         container.defaultPageSize=pageSize;
                        }catch(NumberFormatException e)
                       {
//                        if (pageSize == null) {
                            //     pageSize=container.getPagesPerSlide();
                        //    pageSize = container.defaultPageSize; // added by mayank
                            if (pageSize == null || pageSize.equalsIgnoreCase("")) {
                                pageSize = "25";
                            }
                        }
                        String source = request.getParameter("source");
                        String search = request.getParameter("search");
                        String srchCol = request.getParameter("srchCol");
                        String srchValue = request.getParameter("srchValue");
                        String refresh = request.getParameter("refresh");
                        String sort = request.getParameter("sort");
                        String sortType = request.getParameter("sortType");
                        String sortDataType = request.getParameter("sortDataType");
                        String sortColumn = request.getParameter("sortColumn");
                        String rowSearch = request.getParameter("rowSearch");

                        String colAction = request.getParameter("sourceValue");
                        String custElementId = request.getParameter("custElementId");

                        //added by santhosh.k on 18-02-2010
                        String columnsource = request.getParameter("columnsource");
                        String columnpageSize = request.getParameter("columnslidePages");
                        String columnSearch = request.getParameter("columnSearch");
                        String groupColumns = request.getParameter("groupColumn");
                        if (container.getGroupColumns() != null && !container.getGroupColumns().toString().isEmpty()) {
                            groupColumns = container.getGroupColumns().toString().replace("[", "").replace("]", "");
                        }

                        //added by santhosh.kumar@progenbusiness.com
                        Vector PresShownCols = new Vector();
                        int topbottomCount;
                        ArrayList<Integer> rowSequence=null;
                        int topBottomRowCount;

                        /*
                        if (request.getParameter("nettotreq") != null) {
                        container.setNetTotalReq(Boolean.parseBoolean(request.getParameter("nettotreq")));
                        }
                        if (request.getParameter("grdtotreq") != null) {
                        container.setGrandTotalReq(Boolean.parseBoolean(request.getParameter("grdtotreq")));
                        }
                        if (request.getParameter("avgtotreq") != null) {
                        container.setAvgTotalReq(Boolean.parseBoolean(request.getParameter("avgtotreq")));
                        }
                        if (request.getParameter("overAllMax") != null) {
                        container.setOverAllMaxValueReq(Boolean.parseBoolean(request.getParameter("overAllMax")));
                        }
                        if (request.getParameter("overAllMin") != null) {
                        container.setOverAllMinValueReq(Boolean.parseBoolean(request.getParameter("overAllMin")));
                        }
                        if (request.getParameter("catMax") != null) {
                        container.setCatMaxValueReq(Boolean.parseBoolean(request.getParameter("catMax")));
                        }
                        if (request.getParameter("catMin") != null) {
                        container.setCatMinValueReq(Boolean.parseBoolean(request.getParameter("catMin")));
                        }
                         */


                        if (request.getParameter("hideTable") != null) {
                            container.setHideTable(Boolean.parseBoolean(request.getParameter("hideTable")));
                        }
                        if (request.getParameter("presShownCols") != null) {
                            String[] PresShownColsAry = request.getParameter("presShownCols").split(",");
                            for (int i = 0; i < PresShownColsAry.length; i++) {
                                PresShownCols.add(PresShownColsAry[i]);
                            }
                        }
                        //upto here

                        //boolean searchRequired = container.getSearchReq();
                        //boolean netTotalReq = container.getNetTotalReq();
                        //boolean grdTotalReq = container.getGrandTotalReq();
                        //boolean avgTotalReq = container.getAvgTotalReq();


                        ArrayList displayCols = container.getDisplayColumns();
                        ArrayList displayLabels = container.getDisplayLabels();
                        ArrayList originalCols = container.getOriginalColumns();
                        ArrayList dataTypes = container.getDataTypes();
                        ArrayList displayTypes = container.getDisplayTypes();
                        ArrayList alignments = container.getAlignments();
                        ArrayList<String> sortCols = null;
                        ArrayList<String> sortColsubT = null;
                        char[] sortTypes = null;//ArrayList sortTypes = null;
                        char[] sortDataTypes = null;
                        //ArrayList sortDataTypes = null; //<XLS Removal>
                        //added by santhosh.kumar@progenbusiness.com on 28/10/2009 for getting unDisplayed columns from original columns
                        ArrayList unDisplayedColumns = null;
                        unDisplayedColumns = (ArrayList) originalCols.clone();
                        unDisplayedColumns.removeAll(displayCols);
                        //end of code  for getting unDisplayed columns from original columns

                        //Swapping columns starts here
                        if (swapFrom != null && swapTo != null) {
                            int fromIndex = displayCols.indexOf(swapFrom);
                            int toIndex = displayCols.indexOf(swapTo);
                            if (fromIndex >= 0 && toIndex >= 0) {
                                String swapFrCol = String.valueOf(displayCols.get(fromIndex));
                                String dtyp = String.valueOf(dataTypes.get(fromIndex));
                                String disTyp = String.valueOf(displayTypes.get(fromIndex));
                                String oric = String.valueOf(originalCols.get(fromIndex));
                                String disLab = String.valueOf(displayLabels.get(fromIndex));

                                //Swap column
                                displayCols.set(fromIndex, displayCols.get(toIndex));
                                displayCols.set(toIndex, swapFrCol);
                                //Swap data types
                                dataTypes.set(fromIndex, dataTypes.get(toIndex));
                                dataTypes.set(toIndex, dtyp);
                                //Swap display types
                                displayTypes.set(fromIndex, displayTypes.get(toIndex));
                                displayTypes.set(toIndex, dtyp);
                                //Swap original cols
                                originalCols.set(fromIndex, originalCols.get(toIndex));
                                originalCols.set(toIndex, oric);
                                //Swap display Labels
                                displayLabels.set(fromIndex, displayLabels.get(toIndex));
                                displayLabels.set(toIndex, disLab);


                                //Set back to container
                                container.setDisplayColumns(displayCols);
                                container.setDataTypes(dataTypes);
                                container.setDisplayTypes(displayTypes);
                                container.setDisplayLabels(displayLabels);

                                container.setOriginalColumns(originalCols);

                            }
                        }
                        //Ending swapping columns

                        //boolean isTotalsReq = netTotalReq || grdTotalReq || avgTotalReq;
                        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
                        nFormat.setMaximumFractionDigits(1);
                        nFormat.setMinimumFractionDigits(1);

                        BigDecimal subTotals[] = new BigDecimal[displayCols.size()];
                        BigDecimal grandTotals[] = new BigDecimal[displayCols.size()];
                        BigDecimal avgTotals[] = new BigDecimal[displayCols.size()];

                        //String columnName = null;
                        String disColumnName = null;
                        String dataType = null;
                        String styleStr = null;

                        sortCols = container.getSortColumns();
                        sortTypes = container.getSortTypes();
                        //added by Amar to get subtotal sorting status
                        sortColsubT = container.getExplicitSortColumns();

                        PbReturnObject displayedSet = new PbReturnObject();
                        PbReturnObject displayedSetwithGT = new PbReturnObject();
                        String[] displayedOrigCols = new String[originalCols.size()];
                        for (int i = 0; i < displayedOrigCols.length; i++) {
                            displayedOrigCols[i] = String.valueOf(originalCols.get(i));
                        }
                        displayedSet.setColumnNames(displayedOrigCols);
                        displayedSetwithGT.setColumnNames(displayedOrigCols);

                        for (int colId = start; colId < originalCols.size(); colId++) {
                            if (String.valueOf(ColumnsVisibility.get(String.valueOf(originalCols.get(colId)))).equalsIgnoreCase("''")) {
                                PrevShownCols = PrevShownCols + "," + String.valueOf(originalCols.get(colId));
                            }
                        }
                        if (!(PrevShownCols.equalsIgnoreCase(""))) {
                            PrevShownCols = PrevShownCols.substring(1);
                        }
                        for (int colId = start; colId < originalCols.size(); colId++) {

                            if ((request.getParameter("presShownCols") != null)) {
                                if (PresShownCols.size() != 0 && (!PresShownCols.contains(String.valueOf(originalCols.get(colId))))) {
                                    ColumnsVisibility.put(String.valueOf(originalCols.get(colId)), "none");
                                } else {
                                    ColumnsVisibility.put(String.valueOf(originalCols.get(colId)), "''");
                                }
                            }
                        }
                        container.setColumnsVisibility(ColumnsVisibility);// prepare display columns
                        if (displayCols == null || displayCols.size() == 0) {
                            displayCols = originalCols;
                        }// prepare alignments
                        if (alignments.size() == 0) {
                            for (int i = 0; i < dataTypes.size(); i++) {
                                dataType = String.valueOf(dataTypes.get(i));
                                if ("C".equals(dataType)) {
                                    alignments.add("LEFT");
                                } else if ("D".equals(dataType)) {
                                    alignments.add("CENTER");
                                } else if ("N".equals(dataType)) {
                                    alignments.add("RIGHT");
                                } else if ("CBX".equals(dataType)) {
                                    alignments.add("CENTER");

                                }
                            }
                        }

                        PbReturnObject srchObj = null;
                        Gson gson = new Gson();
                        ProgenDataSet retObj = container.getRetObj();


                        //<XLS Removal>
                        //retObj.initializeViewSequence();

                        int rowCount = retObj.getViewSequence().size();//getRowCount();
                        int columnCount = container.getDisplayColumns().size();
                        String pagesPerSlide = container.getPagesPerSlide();
                        //  //.println("container.B4\t"+container.getPagesPerSlide());
                        if (refresh != null || session.getAttribute("refreshFrmChViewby")!= null ) {
                            session.setAttribute("refreshFrmChViewby", null);
                            container.setRefreshGr("true");
                            container.setSortRetObj(null);
                            //container.setPagesPerSlide("0");
                            container.setSortColumn(null, "");
                            //container.setSortType(null);
                            container.setSelected(new ArrayList());
                            container.setSearchColumn(null, null, null, null);
                            container.setGroupColumn1("");
                            //<Stat Funcs>
                            container.resetStatFuncForMeas();
                                containerNew.setONClickInformation("");
     containerNew.setElementIdvalue("");
     containerNew.setclearInformation("");
                            //<XLS Removal>
                            retObj.resetViewSequence();
                            ///
                            //container.refreshToOriginals(); commented by santhosh.kumar@progenbusiness.com on 08-02-2010
                            //
                            // Added by Amar
                            container.setSubTotalSortColumnTopBottom(null, "");
                            // end of code
                            displayCols = container.getDisplayColumns();
                            displayLabels = container.getDisplayLabels();
                            originalCols = container.getOriginalColumns();
                            dataTypes = container.getDataTypes();
                            displayTypes = container.getDisplayTypes();
                            //pagesPerSlide = Integer.toString(rowCount);
                            sortCols = container.getSortColumns();
                            sortTypes = container.getSortTypes();
                            rowCount = retObj.getViewSequence().size();//getRowCount();
                            //container.refresh();
                            container.resetTopBottom();
                            // Added by Amar to reset SubTotal Top Bottom
                            container.resetSubTotalTopBottom();
                            // End of code
                            refreshGraph = true;
                            container.setGrret(retObj);
                            // Added by Amar
                            container.setIsSubtotalTopBottom(false);
                            try {
                                if (Integer.parseInt(pageSize) > 25) {
                                    pageSize = "25";
                                    container.setPagesPerSlide(pageSize);
                                }
                            } catch (Exception e) {
                                if (pageSize != null && pageSize.equalsIgnoreCase("All")) {
                                    pageSize = ((Integer) rowCount).toString();
                                }
                            }
                            //end of code
                        } //any column sort lands here
                        else if (sort != null) {
                             container.setRefreshGr("false");
                             if(container.isReportCrosstab()){
                                  container.setRefreshGr("false");
                       sortColumn=sortColumn.replace("A_", "");
                   }
                            if (sortType != null && sortType.equalsIgnoreCase("SUBTOTALSORT")) {
                                container.setSubTotalSort(true);
                                container.setSortColumnForSubTot(sortColumn, sort);
                                sortCols = container.getExplicitSortColumns();
                                sortTypes = container.getExplicitSortTypes();
                                sortDataTypes = container.getColumnDataTypes(sortCols);
                                ArrayList<Integer> sortedrowSequence = retObj.getViewSequence();
                                rowSequence = retObj.sortDataSetForSubTotal(sortCols, sortTypes, sortDataTypes, container);//dataTypes, container.getOriginalColumns());
                                //if(rowSequence.size()!=sortedrowSequence.size()){
                                //    rowSequence = sortedrowSequence;
                                //}
                            } else {
                                //container.setSortType(sort);
                                 if(container.isReportCrosstab()){
                                     if(sortColumn.equalsIgnoreCase("TIME")){
                                    container.setSortColumn("A_"+sortColumn, sort);
                                 }else if(sortColumn.contains("A")){
                                       container.setSortColumn(sortColumn, sort);
                                 }else{
                                 container.setSortColumn("A_"+sortColumn, sort);
                                 }
                                     }else{
                                 if(sortColumn.equalsIgnoreCase("A_TIME")){

                                 String sortColumn1=sortColumn.replace("A_", "");
                                   container.setSortColumn(sortColumn1, sort);
                                 }else{
                                container.setSortColumn(sortColumn, sort);
                                }
                                 }
                                sortCols = container.getSortColumns();
                                sortTypes = container.getSortTypes();
                                sortDataTypes = container.getSortDataTypes();
                                //srchObj = retObj.sort(sortCols, sortTypes, sortDataTypes);
                                //srchObj = retObj.sort(sortCols, sortTypes, container.getDataTypes());//commented by santhosh.kumar@progenbusiness.com for the purpose of percent of column on 08-02-2010
                                //srchObj = retObj.sortModified(sortCols, sortTypes, container.getDataTypes(), container.getOriginalColumns());

                                //container.setSortRetObj(srchObj);
                                //retObj = null;
                                //retObj = srchObj;
                                //modified by anitha
                                Boolean TimeAggSortFlag = false;
                                try {
                                rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                                    } catch (Exception e) {
                                        if(sortCols!=null && !sortCols.isEmpty()){
                                            for(int i=0;i<sortCols.size();i++){
                                                if(sortCols.get(i)!=null && !sortCols.get(i).isEmpty()&&(sortCols.get(i).contains("_MTD")|| sortCols.get(i).contains("_QTD")|| sortCols.get(i).contains("_YTD")|| sortCols.get(i).contains("_PMTD")|| sortCols.get(i).contains("_PQTD")|| sortCols.get(i).contains("_PYTD")
                                                    ||sortCols.get(i).contains("_MOM")||sortCols.get(i).contains("_QOQ")||sortCols.get(i).contains("_YOY")||sortCols.get(i).contains("_MOYM")||sortCols.get(i).contains("_QOYQ")||sortCols.get(i).contains("_MOMPer")||sortCols.get(i).contains("_QOQPer")||sortCols.get(i).contains("_YOYPer")||sortCols.get(i).contains("_MOYMPer")||sortCols.contains("_QOYQPer")
                                                    ||sortCols.get(i).contains("_PYMTD")||sortCols.get(i).contains("_PYQTD")||sortCols.get(i).contains("_WTD")||sortCols.get(i).contains("_PWTD")||sortCols.get(i).contains("_PYWTD")||sortCols.get(i).contains("_WOWPer")||sortCols.get(i).contains("_WOYWPer")||sortCols.get(i).contains("_WOW")||sortCols.get(i).contains("_WOYW"))){
                                                    TimeAggSortFlag = true;
                            }
                                            }
                                        }
                                        System.out.println("TimeAggSortFlag+++++++++++"+TimeAggSortFlag);
                                        if(TimeAggSortFlag)                                                                            
                                        rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes,container);
                                    }
                            }
                            retObj.setViewSequence(rowSequence);
                            rowCount = rowSequence.size();
                            //container.resetTopBottom();
                            refreshGraph = true;

                        } //<Top5/Bottom5>
                        else if (colAction != null) {
                            //  //.println(request.getParameter("count"));
                            topbottomCount = 0;//Integer.parseInt(request.getParameter("count"));
                            topBottomRowCount = 0;
                            String topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_NONE;
                            if (colAction.equals("toprows") || colAction.equals("toprowsWithOthers") || colAction.equals("SubTotaltoprows")) {
                                sort = "1"; //sort descend
                                container.setsorttype(sort);//bhargavi 1/05/14
                                topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS;
                            } else if (colAction.equals("bottomrows") || colAction.equals("subTotalBottomRows")) {
                                sort = "0"; //sort ascend
                                container.setsorttype(sort);//bhargavi 1/05/14
                                topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS;
                            }
                            if (colAction.equals("toprows") || colAction.equals("bottomrows") || colAction.equals("toprowsWithOthers") || colAction.equals("SubTotaltoprows") || colAction.equals("subTotalBottomRows")) {
                                refreshGraph = true;
                                sortColumn = request.getParameter("columnName");
                                 if(container.isReportCrosstab()){ 
                            if(sortColumn.contains("A_A"))
                       sortColumn=sortColumn.replace("A_", "");
                   }
                                if (!colAction.equals("SubTotaltoprows") && !colAction.equals("subTotalBottomRows")) {
                                    container.setSortColumnTopBottom(sortColumn, sort);
                                    sortCols = container.getSortColumns();
                                    sortTypes = container.getSortTypes();
                                } else {
                                    container.setIsSubtotalTopBottom(true);
                                    container.setSubTotalSortColumnTopBottom(sortColumn, sort);
                                    sortCols = container.getExplicitSortColumns();
                                    sortTypes = container.getExplicitSortTypes();
                                }
                                retObj.resetViewSequence();
                                topbottomCount = Integer.parseInt(request.getParameter("topbottomCount"));
                                //start of code modified by bhargavi for double sort
                                if (colAction.equals("toprowsWithOthers")) {
                                    container.setTopBottomColumn(topBtmType, ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS_OTHERS, topbottomCount);
                                    container.setTopBottomColumn1(topBtmType, ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS_OTHERS, topbottomCount, sortColumn);
                                } else if (colAction.equals("SubTotaltoprows") || colAction.equals("subTotalBottomRows")) {
                                    container.setSubTotalTopBottomColumn(topBtmType, ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE, topbottomCount);
                                } else {
                                    container.setTopBottomColumn(topBtmType, ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE, topbottomCount);
                                    container.setTopBottomColumn1(topBtmType, ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE, topbottomCount, sortColumn);
                                }
                                //end of code modified by bhargavi for double sort
                                if (colAction.equals("SubTotaltoprows") || colAction.equals("subTotalBottomRows")) {
                                    container.setSubTotalTopBottomCount(topbottomCount);
                                    sortDataTypes = container.getColumnDataTypes(sortCols);
                                    rowSequence = retObj.sortDataSetForSubTotal(sortCols, sortTypes, sortDataTypes, container);
                                } else {
                                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                }

                                retObj.setViewSequence(rowSequence);
                                container.setGrret(retObj);
                                topBottomRowCount = rowSequence.size();
                                rowCount = rowSequence.size();
                                if (Integer.parseInt(pagesPerSlide) > topBottomRowCount) {
                                    pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                }
                                if (Integer.parseInt(pagesPerSlide) < topbottomCount) {
                                    if (topBottomRowCount < topbottomCount) {
                                        pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                    } else {
                                        pagesPerSlide = ((Integer) topbottomCount).toString();
                                    }
                                }
                            }
                            if (colAction.equals("toprowsPercent")) {
                                sort = "1"; //sort descend
                                topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS;
                            } else if (colAction.equals("bottomrowsPercent")) {
                                sort = "0"; //sort ascend
                                topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS;
                            }
                            if (colAction.equals("toprowsPercent") || colAction.equals("bottomrowsPercent")) {
                                refreshGraph = true;
                                sortColumn = request.getParameter("columnName");
                                 if(container.isReportCrosstab()){
                                 if(sortColumn.contains("A_A"))
                       sortColumn=sortColumn.replace("A_", "");
                   }
                                container.setSortColumnTopBottom(sortColumn, sort);
                                sortCols = container.getSortColumns();
                                sortTypes = container.getSortTypes();

                                topbottomCount = Integer.parseInt(request.getParameter("topbottomCount"));

                                container.setTopBottomColumn(topBtmType, ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE, topbottomCount);

                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                retObj.setViewSequence(rowSequence);
                                topBottomRowCount = rowSequence.size();
                                rowCount = rowSequence.size();
                                if (Integer.parseInt(pagesPerSlide) > topBottomRowCount) {
                                    pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                }
                                if (Integer.parseInt(pagesPerSlide) < topBottomRowCount) {
                                    if (rowCount < topbottomCount) {
                                        pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                    } else {
                                        pagesPerSlide = ((Integer) topbottomCount).toString();
                                    }
                                }
                            }

                            //<Stat Funcs>
                            if (colAction.equalsIgnoreCase("Mean") || colAction.equalsIgnoreCase("Median") || colAction.equalsIgnoreCase("SD") || colAction.equalsIgnoreCase("Variance")) {
                                String colName = request.getParameter("columnName");
                                if(container.isReportCrosstab()){
                       colName=colName.replace("A_", "");
                   }
                                if (colAction.equalsIgnoreCase("Mean")) {
                                    container.setStatFuncForMeas(Container.MEAN, colName);
                                } else if (colAction.equalsIgnoreCase("Median")) {
                                    container.setStatFuncForMeas(Container.MEDIAN, colName);
                                } else if (colAction.equalsIgnoreCase("SD")) {
                                    container.setStatFuncForMeas(Container.STANDARD_DEVIATION, colName);
                                } else if (colAction.equalsIgnoreCase("Variance")) {
                                    container.setStatFuncForMeas(Container.VARIANCE, colName);
                                }

                            }

                            if (colAction.equalsIgnoreCase("rounding")) {

                                String colName = request.getParameter("columnName");
                                if(container.isReportCrosstab()){
                       colName=colName.replace("A_", "");
                   }
                                if (request.getParameter("precision") != null) {
                                    int precision = Integer.parseInt(request.getParameter("precision"));
                                    if (!container.isReportCrosstab()) {
                                        container.setRoundPrecisionForMeasure(colName, precision);
                                    } else {
                                        HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
                                        if (container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")) != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")).contains(colName.replace("A_", ""))) {
                                            container.setRoundPrecisionForMeasure(colName, precision);
                                        } else {
                                            PbReportViewerBD reportViewerBD = new PbReportViewerBD();
                                            int whichMeasure = reportViewerBD.findMeasureIndexInCT(container, colName);
                                            ArrayList displayColumns = container.getDisplayColumns();
                                            int size = displayColumns.size();
                                            int measCount = container.getReportMeasureCount();
                                            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
                                            for (int i = whichMeasure; i < size;) {
                                                colName = (String) displayColumns.get(i);
                                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                                    container.setRoundPrecisionForMeasure(crosstabMeasureId.get(colName), precision);
                                                }
                                                if (container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")) != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")).contains(colName.replace("A_", ""))) {
                                                } else {
                                                    container.setRoundPrecisionForMeasure(colName, precision);
                                                }
                                                i += measCount;

                                            }
                                        }
                                    }
                                }

                            }


                        }

                        if (colAction == null) {
                            colAction = "";
                        }
                        if ((container.isTopBottomSet() || container.isSubTotalTopBottomSet()) && !colAction.equals("toprows") && !colAction.equals("bottomrows") && !colAction.equals("SubTotaltoprows") && !colAction.equals("subTotalBottomRows")
                                && !colAction.equals("toprowsPercent") && !colAction.equals("toprowsPercent")) {
                            sortCols = container.getSortColumns();
                            sortTypes = container.getSortTypes();
                            ArrayList<String> sortCols1 = new ArrayList();
                            //start of code by govadhan
                            char[] sortTypes1 = new char[1];

                            if (container.getTopBottomClmId() != null && !container.getTopBottomClmId().equalsIgnoreCase("null") && !container.getTopBottomClmId().equals("") && sortCols.contains(container.getTopBottomClmId())) {
                                //Added by Amar
                                if (sortCols.size() > 1) {
                                    sortCols1 = container.getSortColumns();
                                    sortTypes1 = container.getSortTypes();
                                } else {
                                    sortCols1.add(container.getTopBottomClmId());
                                    int index = sortCols.indexOf(container.getTopBottomClmId());
                                    sortTypes1[0] = sortTypes[index];
                                }
                            }//end of code by govardhan
                            topbottomCount = container.getTopBottomCount();
                            if (container.isSubTotalTopBottomSet()) {
                                sortCols = container.getExplicitSortColumns();
                                sortTypes = container.getExplicitSortTypes();


                                topbottomCount = container.getSubTtlTopBottomCount();
                            }
                            if (!container.isSubTotalTopBottomSet()) {
                                if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                                    refreshGraph = true;
                                    sort = "1";
                                    if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                        rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                    } else {//start of code by govadhan
                                        if (container.getTopBottomClmId() != null && !container.getTopBottomClmId().equalsIgnoreCase("null") && !container.getTopBottomClmId().equals("") && sortCols.contains(container.getTopBottomClmId())) {
                                            rowSequence = retObj.findTopBottom(sortCols1, sortTypes1, topbottomCount);
                                        } else {
                                            rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);

                                        }

                                    }

                                    retObj.setViewSequence(rowSequence);
                                    if (sortCols != null && (groupColumns == null || groupColumns.isEmpty())) //check if any previous sort is present retain them
                                    {
                                        sortCols = container.getSortColumns();
                                        if (!sortCols.isEmpty() && !container.isSubTotalSort()) {
                                            refreshGraph = true;
                                            sortTypes = container.getSortTypes();
                                            sortDataTypes = container.getSortDataTypes();
                                            rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                                            retObj.setViewSequence(rowSequence);
                                            rowCount = rowSequence.size();
                                        } else if (!sortColsubT.isEmpty()) {
                                            sortTypes = container.getExplicitSortTypes();
                                            sortDataTypes = container.getColumnDataTypes(sortColsubT);
                                            ArrayList<Integer> sortedrowSequence = retObj.getViewSequence();
                                            rowSequence = retObj.sortDataSetForSubTotal(sortColsubT, sortTypes, sortDataTypes, container);
                                            retObj.setViewSequence(rowSequence);
                                            rowCount = rowSequence.size();
                                        }
                                    } //end of code by govadhan
                                    topBottomRowCount = rowSequence.size();
                                    rowCount = rowSequence.size();
                                    retObj.setViewSequence(rowSequence);
                                    if (Integer.parseInt(pagesPerSlide) > topBottomRowCount) {
                                        pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                    }
                                    if (Integer.parseInt(pagesPerSlide) < topbottomCount) {
                                        if (topBottomRowCount < topbottomCount) {
                                            pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                        } else {
                                            pagesPerSlide = ((Integer) topbottomCount).toString();
                                        }
                                    }
                                } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                                    refreshGraph = true;
                                    sort = "0";
                                    if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                        rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                    } else {//start of code by bhargavi
                                        if (container.getTopBottomClmId() != null && !container.getTopBottomClmId().equalsIgnoreCase("null") && !container.getTopBottomClmId().equals("") && sortCols.contains(container.getTopBottomClmId())) {
                                            rowSequence = retObj.findTopBottom(sortCols1, sortTypes1, topbottomCount);
                                        } else {
                                            rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);

                                        }
                                        //rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                    }
                                    retObj.setViewSequence(rowSequence);
                                    if (sortCols != null && (groupColumns == null || groupColumns.isEmpty())) //check if any previous sort is present retain them
                                    {
                                        sortCols = container.getSortColumns();
                                        if (!sortCols.isEmpty() && !container.isSubTotalSort()) {
                                            refreshGraph = true;
                                            sortTypes = container.getSortTypes();
                                            sortDataTypes = container.getSortDataTypes();
                                            rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                                            retObj.setViewSequence(rowSequence);
                                            rowCount = rowSequence.size();
                                        } else if (!sortColsubT.isEmpty()) {
                                            sortTypes = container.getExplicitSortTypes();
                                            sortDataTypes = container.getColumnDataTypes(sortColsubT);
                                            ArrayList<Integer> sortedrowSequence = retObj.getViewSequence();
                                            rowSequence = retObj.sortDataSetForSubTotal(sortColsubT, sortTypes, sortDataTypes, container);
                                            retObj.setViewSequence(rowSequence);
                                            rowCount = rowSequence.size();
                                        }
                                    } //end of code by bhargavi
                                    topBottomRowCount = rowSequence.size();
                                    //rowCount = retObj.getRowCount();
                                    rowCount = rowSequence.size();

                                    if (Integer.parseInt(pagesPerSlide) > topBottomRowCount) {
                                        pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                    }
                                    if (Integer.parseInt(pagesPerSlide) < topbottomCount) {
                                        if (topBottomRowCount < topbottomCount) {
                                            pagesPerSlide = ((Integer) topBottomRowCount).toString();
                                        } else {
                                            pagesPerSlide = ((Integer) topbottomCount).toString();
                                        }
                                    }
                                }
                            } else if (container.isSubTotalTopBottomSet()) {
                                if (container.getgetSubTtlTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                                    sort = "1";
                                    container.setSubTotalTopBottomCount(topbottomCount);
                                    sortDataTypes = container.getColumnDataTypes(sortCols);
                                    rowSequence = retObj.sortDataSetForSubTotal(sortCols, sortTypes, sortDataTypes, container);
                                    retObj.setViewSequence(rowSequence);
                                } else if (container.getgetSubTtlTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {

                                    container.setSubTotalTopBottomCount(topbottomCount);
                                    sortDataTypes = container.getColumnDataTypes(sortCols);
                                    rowSequence = retObj.sortDataSetForSubTotal(sortCols, sortTypes, sortDataTypes, container);
                                    retObj.setViewSequence(rowSequence);
                                }

                            }

                        } else if (sort == null) //user hasn't done any sort
                        {
                            if (sortCols != null && (groupColumns == null || groupColumns.isEmpty())) //check if any previous sort is present retain them
                            {
                                sortCols = container.getSortColumns();
                                if (!sortCols.isEmpty() && !container.isSubTotalSort()) {
                                    refreshGraph = true;
                                    sortTypes = container.getSortTypes();
                                    sortDataTypes = container.getSortDataTypes();
                                    rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                                    retObj.setViewSequence(rowSequence);
                                    rowCount = rowSequence.size();
                                } else if (!sortColsubT.isEmpty()) {
                                    sortTypes = container.getExplicitSortTypes();
                                    sortDataTypes = container.getColumnDataTypes(sortColsubT);
                                    ArrayList<Integer> sortedrowSequence = retObj.getViewSequence();
                                    rowSequence = retObj.sortDataSetForSubTotal(sortColsubT, sortTypes, sortDataTypes, container);
                                    retObj.setViewSequence(rowSequence);
                                    rowCount = rowSequence.size();
                                }
                            }
                        }
                        if (colAction.equalsIgnoreCase("customSeq")) {
                            ArrayList<Integer> custViewSeq = (ArrayList<Integer>) session.getAttribute(custElementId);
                            retObj.setViewSequence(custViewSeq);
                        }
                        if (!container.getReportCollect().getCustomSequence().isEmpty() && !colAction.equalsIgnoreCase("customSeq") && !container.isTopBottomSet() && sort == null && sortCols.isEmpty()) {
                            retObj.setViewSequence(dao.setCustomViewSequence(tabName, container));
                        }

                        //while reloading the page retain the previous sorting if any


                        //recompute RT Measures after any table operations
                        container.initializeRuntimeMeasures();



                        //String pagesPerSlide = container.getPagesPerSlide();


                        if (pagesPerSlide.equalsIgnoreCase("0")) {
                            pagesPerSlide = ((Integer) rowCount).toString();//retObj.getRowCount());
                        }

                        if (pageSize != null) {
                            if (pageSize.equalsIgnoreCase("All")) {
                                pagesPerSlide = ((Integer) rowCount).toString();//retObj.getRowCount());
                            } else {
                                pagesPerSlide = pageSize;
                            }

                            container.setPagesPerSlide(pagesPerSlide);
                        } else {
                            if (pagesPerSlide.equalsIgnoreCase("All")) {
                                pagesPerSlide = ((Integer) rowCount).toString();//retObj.getRowCount());
                            } else if (pagesPerSlide.equalsIgnoreCase("")) {
                                pagesPerSlide = ((Integer) rowCount).toString();//retObj.getRowCount());//String.valueOf(retObj.getRowCount());
                            }
                            //container.setPagesPerSlide(pagesPerSlide);
                        }
                        container.setColumnsPerSlide(displayCols.size());//modified by santhosh.k on 04-03-2010 for displaying all columns at a time
                        int columnsPerSlide = container.getColumnsPerSlide();
                        if (columnpageSize != null) {
                            columnsPerSlide = Integer.parseInt(columnpageSize);
                            container.setColumnsPerSlide(columnsPerSlide);
                        }

                        if ("R".equals(source)) {
                            ////.println("container.getCurrentPage() \t"+container.getCurrentPage() );
                            ////.println("Integer.parseInt(pagesPerSlide) \t"+Integer.parseInt(pagesPerSlide) );
                            if (container.getCurrentPage() * Integer.parseInt(pagesPerSlide) < rowCount) {
                                container.setCurrentPage(container.getCurrentPage() + 1);
                                //   container.setFromRow(container.getFromRow() + Integer.parseInt(pagesPerSlide));
                                if ((rowCount - Integer.parseInt(pagesPerSlide)) > 0) {
                                    ////.println("container.getFromRow() \t"+container.getFromRow() );
                                    container.setFromRow(container.getFromRow() + Integer.parseInt(pagesPerSlide));
                                } else {
                                    container.setFromRow(0);
                                }
                            }
                        } else if ("L".equals(source)) {
                            if (container.getCurrentPage() > 1) {
                                container.setCurrentPage(container.getCurrentPage() - 1);
                                if ((container.getFromRow() - Integer.parseInt(pagesPerSlide)) >= 0) {
                                    container.setFromRow(container.getFromRow() - Integer.parseInt(pagesPerSlide));
                                } else {
                                    container.setCurrentPage(1);
                                    container.setFromRow(0);
                                }
                            } else {
                                container.setCurrentPage(1);
                                container.setFromRow(0);
                            }
                        } else if ("S".equals(source)) {
                            container.setCurrentPage(1);
                            container.setFromRow(0);
                            ////.println("retObj.getViewSequence().size()"+retObj.getViewSequence().size());
                            ////.println("pagesPerSlide\t"+pagesPerSlide);

                            /*
                            if(Integer.parseInt(pagesPerSlide)<retObj.getViewSequence().size()){
                            pagesPerSlide=Integer.toString(rowCount);
                            }
                            
                             */
                        } else if ("E".equals(source)) {
                            ////.println("rowCount\t"+rowCount);
                            ////.println("rowCount / Integer.parseInt(pagesPerSlide)\t"+rowCount / Integer.parseInt(pagesPerSlide));

                            container.setCurrentPage((rowCount / Integer.parseInt(pagesPerSlide)) * Integer.parseInt(pagesPerSlide));
                            if ((rowCount - Integer.parseInt(pagesPerSlide)) > 0) {
                                container.setFromRow((rowCount / Integer.parseInt(pagesPerSlide)) * Integer.parseInt(pagesPerSlide));
                            } else {
                                container.setFromRow(0);
                            }
                        } else if ("Grouping".equalsIgnoreCase(source) || (groupColumns != null && !groupColumns.isEmpty())) {
                            container.setSortColumn(groupColumns, "0");
                            sortCols = container.getSortColumns();
                            container.setGroupColumn(groupColumns);
                            sortDataTypes = container.getSortDataTypes();
                            rowSequence = retObj.twoRowviewGrouping(sortCols, sortTypes, sortDataTypes);
                            retObj.setViewSequence(rowSequence);
                            rowCount = rowSequence.size();
                        }//code by bhargavi
                        int fromRow = 0;
                        if ((isreportdrillPopUp != null && isreportdrillPopUp.equalsIgnoreCase("true")) || (isrefreshrowcount != null && isrefreshrowcount.equalsIgnoreCase("true"))) {
                            fromRow = 0;
                        } else if (container.NewTabValue != null && container.NewTabValue.equalsIgnoreCase("true")) {
                            fromRow = 0;
                        } //end of code by bhargavi
                        else {
                            fromRow = container.getFromRow();
                        }
                        container.NewTabValue = "false";
                        //pagesPerSlide * (displaySet-1);
                        if (rowSearch != null) {
                            fromRow = Integer.parseInt(rowSearch);
                            container.setCurrentPage((fromRow / Integer.parseInt(pagesPerSlide)) + 1);
                            container.setFromRow(fromRow);
                        }
                        int toRow = fromRow + Integer.parseInt(pagesPerSlide);
                        if (toRow > rowCount) {
                            toRow = rowCount;
                        }

                        int displaySet = container.getCurrentPage();
                        if (displaySet == 0) {
                            displaySet = 1;
                        }

                        //pagination for column wise

                        if ("RC".equalsIgnoreCase(columnsource)) {
                            if (container.getCurrentColumnPage() * columnsPerSlide < columnCount) {
                                container.setCurrentColumnPage(container.getCurrentColumnPage() + 1);
                                container.setFromColumn(container.getFromColumn() + columnsPerSlide);
                            } else {
                                container.setCurrentColumnPage((columnCount / columnsPerSlide) + 1);
                                if ((columnCount - columnsPerSlide) > 0) {
                                    container.setFromColumn(columnCount - columnsPerSlide);
                                } else {
                                    container.setFromColumn(0);
                                }
                            }
                        } else if ("LC".equalsIgnoreCase(columnsource)) {
                            if (container.getCurrentColumnPage() > 1) {
                                container.setCurrentColumnPage(container.getCurrentColumnPage() - 1);
                                if ((container.getFromColumn() - columnsPerSlide) >= 0) {
                                    container.setFromColumn(container.getFromColumn() - columnsPerSlide);
                                } else {
                                    container.setCurrentColumnPage(1);
                                    container.setFromColumn(0);
                                }
                            } else {
                                container.setCurrentColumnPage(1);
                                container.setFromColumn(0);
                            }
                        } else if ("SC".equalsIgnoreCase(columnsource)) {
                            container.setCurrentColumnPage(1);
                            container.setFromColumn(0);
                        } else if ("EC".equalsIgnoreCase(columnsource)) {
                            container.setCurrentColumnPage((columnCount / columnsPerSlide) + 1);
                            if ((columnCount - columnsPerSlide) > 0) {
                                container.setFromColumn(columnCount - columnsPerSlide);
                            } else {
                                container.setFromColumn(0);
                            }
                        }

                        int fromColumn = container.getFromColumn();  //columnsPerSlide * (displaySet-1);
                        if (columnSearch != null) {
                            fromColumn = Integer.parseInt(columnSearch);
                            container.setCurrentColumnPage((fromColumn / columnsPerSlide) + 1);
                            container.setFromColumn(fromColumn);
                        }
                        int toColumn = fromColumn + columnsPerSlide;
                        if (toColumn > columnCount) {
                            toColumn = columnCount;
                        }



                        for (int i = 0; i < subTotals.length; i++) {
                            subTotals[i] = new BigDecimal("0");
                            grandTotals[i] = new BigDecimal("0");
                            avgTotals[i] = new BigDecimal("0");
                        }

                        ArrayList repTabPrevList = new ArrayList();
                        String userId = String.valueOf(session.getAttribute("USERID"));
                        String repTabPrevilage = "SELECT PRTP_ID,USER_ID,PREVILAGE_NAME FROM PRG_AR_REPTAB_PREVILAGES WHERE USER_ID=" + userId;
                        PbReturnObject repTabprevilageObj = pbdb.execSelectSQL(repTabPrevilage);
                        for (int tab = 0; tab < repTabprevilageObj.getRowCount(); tab++) {
                            repTabPrevList.add(repTabprevilageObj.getFieldValueString(tab, "PREVILAGE_NAME"));
                        }
                        //added  by bharathi reddy on 11th feb10 for grp by analysis

//                                    PbReturnObject viewBydetspbro = null;
                        //("TableHashMap====="+TableHashMap);
                        //                                   viewBydetspbro = dao.getViewByNames((ArrayList) TableHashMap.get("REP"), container.getReportId());
                        //("container.getSqlStr() is : "+container.getSqlStr());
                        sqlString = container.getSqlStr();
                        sqlString = sqlString.replace("\"", "").replace("'", "~");
                        // added by sruthi to display fullquery
                        String reportquery = null;
                        reportquery = container.getReportQuery();
                        reportquery = reportquery.replace("\"", "").replace("'", "~");//ended by sruhthi
                        //  
        %>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="background">
            <tr>
                <td width="100%">
                    <div align="center" style="width:100%;height:auto;" class="background">
                        <form action="" name="myForm" id="myForm" method="POST">
                            <input type="hidden" name="columnIds" id="columnIds" value="">
                            <input type="hidden" name="reportId" id="reportId" value="">
                            <input type="hidden" name="tableId" id="tableId" value="">
                            <input type="hidden" name="lastSeq" id="lastSeq" value="">
                            <input type="hidden" name="viewBysCount" id="viewBysCount" value="<%=container.getViewByCount()%>">
                            <input type="hidden" name="oldMsrName" id="oldMsrName" value="">
                            <table width="100%"  CELLPADDING="0" CELLSPACING="1" id="paramHeaderId" style="">
                                <tr VALIGN="top" >
                                    <td VALIGN="top" >
                                        <!--MODIFIED BY ANITHA background-color: transparent;-->
                                        <table border='0'  class="progenTable" style="background-color: transparent;width:auto;margin:2px 0px 2px 15px ">
                                            <tr ALIGN="left" VALIGN="top"  style="width:auto">
<!--                                             <td ALIGN="left" valign="top" ><Img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" id="FIRST" name="FIRST" style="cursor:pointer;cursor:hand" onClick="parent.goFirst(<%=(fromRow + 1)%>,<%=container.getTableId()%>)" title="First Page" />&nbsp;&nbsp;</td>
                                                <td ALIGN="left" valign="top" ><Img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" id="UP" name="UP" style="cursor:pointer;cursor:hand" onClick="parent.goUp(<%=(fromRow + 1)%>,<%=container.getTableId()%>)" title="Previous Page" />&nbsp;&nbsp;</td>-->
                                                <td ALIGN="left" valign="top" ><i id="FIRST" name="FIRST" onClick="parent.goFirst(<%=(fromRow + 1)%>,<%=container.getTableId()%>)" title="First Page" class="fa fa-angle-left" style='cursor:pointer;color:#4d4d4d;font-size:1.8em'></i></td>
                                                <td ALIGN="left" valign="top" ><i id="UP" name="UP" onClick="parent.goUp(<%=(fromRow + 1)%>,<%=container.getTableId()%>)" title="Previous Page" class="fa fa-angle-double-left" style='cursor:pointer;color:#4d4d4d;font-size:1.8em'></i></td>
                                                    <% if (!container.getIsSubToTalSearchFilterApplied() && !container.isIsSubtotalTopBottom()) {%>
                                                <td ALIGN="left" valign="top" ><input type=text name="fVal" id="fVal" size=3 class="inputbox gFontFamily gFontSize11" style="text-align:right;border:1px solid #d2d2d2;color: #4d4d4d;height:12px;margin-top:4px;"  value="<%=(fromRow + 1)%>" onKeyUp="parent.doSetFirstRow(this,event,<%=retObj.getRowCount()%>,<%=(fromRow + 1)%>,<%=container.getTableId()%>)">&nbsp;&nbsp;-</td><td id="ToRowAndRowCount" style="color: #4d4d4d;padding-top: 5px;"><%=toRow%> <%=TranslaterHelper.getTranslatedInLocale("of", cL)%> <%=rowCount%></td>
                                                    <% } else {%>
                                                <td ALIGN="left" valign="top" ><input type=text name="fVal" id="fVal" size=3 class="inputbox gFontFamily gFontSize11" style="text-align:right;border:1px solid #d2d2d2;color: #4d4d4d;height:12px;margin-top:4px;"  value="<%=(fromRow + 1)%>" onKeyUp="parent.doSetFirstRow(this,event,<%=retObj.getRowCount()%>,<%=(fromRow + 1)%>,<%=container.getTableId()%>)">&nbsp;&nbsp;-</td><td id="ToRowAndRowCount" style="color: #4d4d4d;padding-top: 5px;"></td>

                                                <% }%>
<!--                                            <td ALIGN="left" valign="top" ><Img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" id="DOWN" name="DOWN" style="cursor:pointer;cursor:hand" onClick="parent.goDown(<%=toRow%>,<%=rowCount%>,<%=container.getTableId()%>)" title="Next Page" />&nbsp;&nbsp;</td>
                                                <td ALIGN="left" valign="top" ><Img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" id="LAST" name="LAST" style="cursor:pointer;cursor:hand" onClick="parent.goLast(<%=toRow%>,<%=rowCount%>,<%=container.getTableId()%>)" title="Last Page" />&nbsp;&nbsp;</td>-->
                                                <td ALIGN="left" valign="top" ><i class="fa fa-angle-double-right" id="DOWN" name="DOWN" onClick="parent.goDown(<%=toRow%>,<%=rowCount%>,<%=container.getTableId()%>)" title="Next Page" style='cursor:pointer;color:#4d4d4d;font-size:1.8em'></i></td>
                                                <td ALIGN="left" valign="top" ><i class="fa fa-angle-right" id="LAST" name="LAST" onClick="parent.goLast(<%=toRow%>,<%=rowCount%>,<%=container.getTableId()%>)" title="Last Page" style='cursor:pointer;color:#4d4d4d;font-size:1.8em'></i></td>
                                                
                                                    <% if (isreportdrillPopUp == null) {%>
                                                    <% if (userdao.getFeatureEnable("Refresh Table") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                <td ALIGN="left" valign="top" >
<!--                                            <Img alt="REFRESH "  id="REFRESH" name="REFRESH" style="cursor:pointer;cursor:hand" onClick="parent.goRefresh('<%=container.getTableId()%>')" title="Refresh List" />&nbsp;&nbsp;</td>-->
<!--                                            <td ALIGN="left" valign="top" ><a onClick="parent.goRefresh('<%=container.getTableId()%>')"  title="Refresh List"> Refresh </a>&nbsp;</td>-->
<!--                                            <td ALIGN="left" valign="top" > <Img alt="" src="<%=request.getContextPath()%>/images/refersh_image.png"  style="height: 12px;"  onClick="parent.goRefresh('<%=container.getTableId()%>')"  title="Refresh List" />&nbsp;</td>-->
                                                <td ALIGN="left" valign="top" ><i class="fa fa-refresh" onClick="parent.goRefresh('<%=container.getTableId()%>')"  title="Refresh List" style='color: #4d4d4d;cursor: pointer;font-size: 1.2em;margin-top: 4px;'></i> </td>
                                                <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0;margin:3px;"  alt="Separator" /></td>
                                                    <% }
                                                        }%>
                                                <td style="color:#4d4d4d;padding-top: 5px;" title="<%=TranslaterHelper.getTranslatedInLocale("Rows_Displayed", cL)%>"><%=TranslaterHelper.getTranslatedInLocale("Rows_Displayed", cL)%></td>
                                                <td ALIGN="left" valign="top" ><input type=text name="recordsSize" id="recordsSize" size=3 class="inputbox" style="border: 1px solid #d2d2d2;color: #4d4d4d;height: 12px;text-align: right;margin-top: 4px;" onKeyUp="parent.doSetRecordSize(this,event,<%=retObj.getRowCount()%>,<%=pagesPerSlide%>,'<%=container.getTableId()%>','<%=container.getReportId()%>')" value="<%=pagesPerSlide%>"></td>
                                                <% if (userdao.getFeatureEnable("Display All Records") || userType.equalsIgnoreCase("SUPERADMIN")) {%><td ALIGN="left" valign="top" style="color:#4d4d4d;padding-top: 5px;cursor:pointer;" onClick="parent.setRecordSize('All','<%=container.getTableId()%>')"  title="<%=TranslaterHelper.getTranslatedInLocale("Display_All_Records", cL)%>" > &nbsp;<%=TranslaterHelper.getTranslatedInLocale("All", cL)%> </td><td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0;margin:3px"  alt="Separator" /></td>
                                                    <% }//if (PrivilegeManager.isComponentEnabledForUser("REPORT", "TABPROPS", USERID)) {%>
                                                    <% if (request.getParameter("fromOnview") != null && request.getParameter("fromOnview").equalsIgnoreCase("true")) {%>
                                                    <%} else {
                                                        if (isreportdrillPopUp == null) {
                                                            if (userdao.getFeatureEnable("Time Display") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
    <!--                                                <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></td>
                                                    <td Align="left" valign="top" ><a class="ui-icon ui-icon-clock" onclick="parent.displayRepSpanTimeInfo('<%=container.getReportId()%>')"></a></td>
                                                    <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></td><% }%> -->
                                                <% if (userdao.getFeatureEnable("Table Properties") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                <td ALIGN="left" valign="top" style="color:#4d4d4d;padding-top: 5px;cursor:pointer;" onclick="parent.showTableProperties('<%=tabName%>', '<%=request.getContextPath()%>','<%=pageSize%>')"  title="<%=TranslaterHelper.getTranslatedInLocale("Table_Properties",cL)%>"><%=TranslaterHelper.getTranslatedInLocale("Table_Properties",cL)%>
                                                    <!--                                                   <div class="tooltip tooltip-north" style="margin-left: 9%; margin-top: -11%">-->

                                                    <!--<a style="float: right" href="javascript:void(0)"  onclick="parent.showTableProperties('<%=tabName%>', '<%=request.getContextPath()%>','<%=pageSize%>')" class="ui-icon ui-icon-pencil"  title="Table Properties">Table Properties</a>&nbsp;&nbsp;-->
                                                     
                                                    <!--                                                    <span class="tooltip-content">Click To Change Table Properties  </span>        -->
                                                    <!--                                                    </div>-->
                                                </td><td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0;margin:3px"  alt="Separator" /></td>
                                                  <td ALIGN="left" valign="top" style="cursor: pointer;float: right;margin-left: 1%;color:#4d4d4d;padding-top: 5px; white-space: nowrap" href="javascript:void(0)"  onclick="parent.showTableColumnProperties('<%=tabName%>', '<%=request.getContextPath()%>','<%=pageSize%>')" class=""  title="<%=TranslaterHelper.getTranslatedInLocale("Table_Column_Properties",cL)%>"><%=TranslaterHelper.getTranslatedInLocale("Table_Column_Properties",cL)%>
                                                </td><% } %>
<!--   ended by sruthi                          
                                                
                                                    <% if (userdao.getFeatureEnable("Table Properties") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                <td ALIGN="left" valign="top" style="">
                                                    <!--                                                   <div class="tooltip tooltip-north" style="margin-left: 9%; margin-top: -11%">-->

                                                    <!--<a style="float: right" href="javascript:void(0)"  onclick="parent.showTableProperties('<%=tabName%>', '<%=request.getContextPath()%>','<%=pageSize%>')" class="ui-icon ui-icon-pencil"  title="Table Properties">Table Properties</a>&nbsp;&nbsp;-->
                                                    <!--
                                                                                                    &nbsp;&nbsp;<a id="showSql" href="javascript:showSqlQuery('show')" title="Click to See SQL Query" >Display SQL Query </a>
                                                                                           <td ALIGN="left" valign="top">&nbsp;<img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></td>-->
                                                    <!--                                                    <span class="tooltip-content">Click To Change Table Properties  </span>        -->
                                                    <!--                                                    </div>-->
                                                </td>

                                                <%  }%>
                                                <!--   <td ALIGN="left" valign="top"><div class="tooltip tooltip-north" style="margin-left: 72%; margin-top: -11.5%"><a title="" onclick="parent.editViewBy()" class="ui-icon ui-icon-copy" href="javascript:void(0)"></a>
                                                           <span class="tooltip-content">Click to Change View Bys </span></div>
                                                   </td>  -->
                                                <!--added by Dinanath-->
                                                <!--                                             <td ALIGN="right" valign="top">&nbsp;&nbsp;<a title="Click To Tag This Report" onclick="parent.saveReportTagName()" href="javascript:void(0)">Tag Report</a>
                                                                                              </td><td ALIGN="left" valign="top">&nbsp;<img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></td>-->
                                                <!--ended by Dinanath-->
                                                <%//}
                                                    if (ViewFrom.equalsIgnoreCase("Viewer") || ViewFrom.equalsIgnoreCase("Designer")) {
                                                        //  if (PrivilegeManager.isComponentEnabledForUser("REPORT", "CHANGEDISPSTYLE", USERID)) {
                                                %>
                                                                                              <!-- <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                                                                <td>
                                                                                                    <a href="javascript:changeDisplayStyle('<%=request.getContextPath()%>','<%=container.getReportId()%>')">Change Table Display Style</a>
                                                                                                </td>
                                                
                                                <% if (ViewFrom.equalsIgnoreCase("Viewer") || ViewFrom.equalsIgnoreCase("Designer")) {
                                                        // if (PrivilegeManager.isComponentEnabledForUser("REPORT", "EDITTAB", USERID)) {%>
                                                <% if (columnViewbyCount > 0) {%>
                                                                        <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                            <td>    <table width="100%">

                                                <a href="javascript:void(0);" onclick ="parent.showColumnList(document.getElementById('ColumnList'),document.getElementsByName('colNames'),'=PrevShownCols%>','tabName%>');"  title="Click For Edit Table">Edit Table</a>&nbsp;&nbsp;
                                               <a href="javascript:void(0);" onclick ="dispChangeTableColumns('<%=request.getContextPath()%>','<%=sbufRoles.toString().substring(1)%>','<%=tabName%>');" class="ui-icon ui-icon-calculator" title="Edit Table"></a>

                                            <tr valign="top">
                                                <td valign="top">
                                                    <div   class="flagDiv" style='display:none' id="ColumnList">
                                                        <table>
                                                <%String chked = "";


                                                    //For Crosstab displayCols comes as ArrayList
                                                    //ArrayList has columnName, measName. For each meas Column Name repeats
                                                    //we will only hide Dimension Column Name
                                                    int measCount = 0;
                                                    if (container.getTableDisplayMeasures() != null) {
                                                        measCount = container.getTableDisplayMeasures().size();
                                                    }

                                                    for (int colIndex = start; colIndex < displayCols.size(); colIndex += measCount) {
                                                        if (String.valueOf(ColumnsVisibility.get(String.valueOf(displayCols.get(colIndex)))).equalsIgnoreCase("''")) {
                                                            chked = "checked";
                                                        } else {
                                                            chked = "";
                                                        }%>
                                                <tr>
                                                    <td ALIGN="left"><input type="checkbox" <%=chked%>  name="colNames" id="checkBox<%=colIndex%>" onClick='parent.modifyColumns("column<%=colIndex%>",document.getElementById("checkBox<%=colIndex%>"),document.getElementById("displayTable"),"<%=String.valueOf(displayCols.get(colIndex))%>")' value="<%=String.valueOf(displayCols.get(colIndex))%>"></td>
                                                <%if (!displayCols.get(colIndex).toString().contains("A_")) {%>
                                                <td ALIGN="left"><%=((ArrayList) displayLabels.get(colIndex)).get(0)%></td>
                                                <%} else {%>
                                                <td ALIGN="left"><%=(displayLabels.get(colIndex).toString())%></td>

                                                <%}%>
                                            </tr>
                                                <%}%>
                                            </table>
                                            </div>
                                            </td>
                                        </tr>

                            </table>
                            </td>
                                                <% } else {
                                                    if (userdao.getFeatureEnable("Edit Table") || userType.equalsIgnoreCase("SUPERADMIN")) {
                                                %> <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></td>
                                    
                                    
                                                                                <td><a  href="javascript:void(0)" onclick="dispChangeTableColumns('<%=request.getContextPath()%>','<%=sbufRoles.toString().substring(1)%>','<%=tabName%>')"  class="ui-icon ui-icon-calculator"title="Edit Table">Edit Table</a>
                                                <% }
                                                        }
                                                    }%></td>
                                                    <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                <%
                                                    //   if (PrivilegeManager.isComponentEnabledForUser("REPORT", "WHATIF", USERID)) {
                                                %>
                                                <% if (container != null && !container.isReportCrosstab() && (ViewFrom.equalsIgnoreCase("Viewer"))) {
                                                        if (userdao.getFeatureEnable("What-if Analysis") || userType.equalsIgnoreCase("SUPERADMIN")) {%>

                                           <td>
                                                <a id="whatifSc" href="javascript:openWhatIfDiloge('<%=request.getContextPath()%>','<%=container.getReportId()%>')" class="ui-icon ui-icon-arrow-4-diag" title="Perform What If" >What If Analysis</a></td>
                                           <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                <% }
                                                    if (container != null && container.getWhatIfScenario() != null) {
                                                        ArrayList<Double> sliderValues = null;
                                                        if (container.getWhatIfScenario() != null) {
                                                            sliderValues = container.getWhatIfScenario().getWhatIfMeasureSliderValues();


                                                        }

                                                %>
                                                -->
                                                <td>
                                                    <a href="javascript:openWhatifScenario('<%= container.getWhatIfMeasureList().toString()%>','<%= ((ArrayList<String>) TableHashMap.get("Measures")).toString()%>','<%= ((ArrayList<String>) TableHashMap.get("MeasuresNames")).toString()%>','<%=request.getContextPath()%>','<%=sliderValues%>')" title="Select WhatIf Range" id="whatifLink">
                                                        <img border="0" src="<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/elements/icons/chart.png">

                                                    </a>

                                                </td>
                                                <td>
                                                    <a href="javascript:clearWhatifScenario('<%= container.getWhatIfMeasureList().toString()%>','<%= ((ArrayList<String>) TableHashMap.get("Measures")).toString()%>','<%= ((ArrayList<String>) TableHashMap.get("MeasuresNames")).toString()%>','<%=request.getContextPath()%>','<%=sliderValues%>')" title="Select WhatIf Range" id="whatifLink">
                                                        <img  title="clearWhatIf" border="0" src="<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/elements/icons/clear-icon.png">

                                                    </a>

                                                </td> <!--
                                <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                <% }
                                                    }
                                                    // }
                                                %>
                                                            
                                                            
                                                            
                                                            
                                                            
                                                <% if (container != null && !container.isReportCrosstab() && (ViewFrom.equalsIgnoreCase("Viewer"))) {
                                                        if (userdao.getFeatureEnable("Group Measures") || userType.equalsIgnoreCase("SUPERADMIN")) {%>

                                                    <td ALIGN="left"   valign="top"><a href="javascript:openGroupMeasureList('<%=request.getContextPath()%>','<%=container.getReportId()%>')" class="ui-icon ui-icon-cart" title="Group Measures">Group Measures</a></td>
                                               <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                <% }
                                                            }
                                                        }
                                                    }
                                                    if (userdao.getFeatureEnable("Export to Excel") || userType.equalsIgnoreCase("SUPERADMIN")) {
                                                        // if (PrivilegeManager.isComponentEnabledForUser("REPORT", "EXPORTS", USERID)) {%>

                                               <td ALIGN="left"   valign="top" width="4%">
                                                   <img src='<%= request.getContextPath()%>/images/excel.gif' style="cursor: pointer;" align="left" onclick="parent.showExports('<%= PbReportId%>','<%=request.getContextPath()%>')" value='' title='Exports'/></td>
                                                    <a href="javascript:void(0)" style="cursor:hand;text-decoration:none;font-family:verdana;" class="ui-icon ui-icon-arrowthick-1-s" title="Exports" onclick="parent.showExports('<%= PbReportId%>','<%=request.getContextPath()%>')">Exports</a></td>
                                                    <td><td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                <%//}
                                                    }
                                                    if (PrivilegeManager.isComponentEnabledForUser("REPORT", "HIDETAB", USERID)) {%>
                                                <%--<td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                <td ALIGN="left" valign="top"><a  id="showtable" href="javascript:parent.showTable(document.getElementById('displayTable'),'<%=tabName%>')"  title="Hide Table"> <%=hideTableStr%></a>&nbsp;&nbsp;</td>--%>
                                                <%}%>
                                                <%--<%if (columnViewbyCount != 0) {%>
                                                <td ALIGN="left" style='text-decoration:none;font-family:verdana;;'><span style="cursor:pointer;font-family:verdana;" onclick="iText('<%=container.getTableMeasureNames().get(0)%>')">Measure is :<%=container.getTableMeasureNames().get(0)%></span></td>
                                                <%}%>--%>

                                                <%for (int j = start; j < displayCols.size(); j++) {
                                                        drillColumnIds = drillColumnIds + "," + String.valueOf(displayCols.get(j));
                                                    }
                                                    if (!(drillColumnIds.equalsIgnoreCase(""))) {
                                                        drillColumnIds = drillColumnIds.substring(1);

                                                    }%>


                                                <%-- *******Commenting Column Settings

                                                                                                                        if (showExtraTabs) {
                                                                                       if(ViewFrom.equalsIgnoreCase("Viewer"))       {             %>

                                                <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</Td>
                                                <td  ALIGN="left"  valign="top">
                                                    <table>
                                                        <tr valign="top">
                                                            <td valign="top">
                                                                <a href="javascript:void(0)"  title="Click For Column Settings" onclick='parent.openColumnSettingsDIV(document.getElementById("columnSettingsDIV"))'>Column Settings</a>
                                                            </td>
                                                        </tr>
                                                        <tr valign="top">
                                                            <td valign="top">
                                                                <div class="flagDiv" style="display:none;width:auto;height:auto;"  id="columnSettingsDIV">
                                                                    <table>
                                                                        <tr valign="top">
                                                                            <td ALIGN="left" valign="top">
                                                                                <Img src=<%=c_img_first%> id="FIRSTColumn" name="FIRSTColumn" style="cursor:pointer;cursor:hand" onClick="parent.goColumnFirst(<%=(fromColumn + 1)%>,<%=container.getTableId()%>)" title="First Page" />&nbsp;&nbsp;
                                                                                <Img src=<%=c_img_up%> id="UPColumn" name="UPColumn" style="cursor:pointer;cursor:hand" onClick="parent.goColumnUp(<%=(fromColumn + 1)%>,<%=container.getTableId()%>)" title="Previous Page" />&nbsp;&nbsp;
                                                                                <Img src=<%=c_img_down%> id="DOWNColumn" name="DOWNColumn" style="cursor:pointer;cursor:hand" onClick="parent.goColumnDown(<%=toColumn%>,<%=columnCount%>,<%=container.getTableId()%>)" title="Next Page" />&nbsp;&nbsp;
                                                                                <Img src=<%=c_img_last%> id="LASTColumn" name="LASTColumn" style="cursor:pointer;cursor:hand" onClick="parent.goLast(<%=toColumn%>,<%=columnCount%>,<%=container.getTableId()%>)" title="Last Page" />&nbsp;&nbsp;
                                                                            </td>
                                                                        </tr>
                                                                        <tr valign="top">
                                                                            <td ALIGN="left" valign="top">
                                                                                <input type=text name="fValColumn" id="fValColumn" size=3 class="inputbox" style="text-align:right"  value="<%=(fromColumn + 1)%>" onKeyUp="parent.doSetFirstColumn(this,event,<%=columnCount%>,<%=(fromColumn + 1)%>,<%=container.getTableId()%>)">&nbsp;&nbsp;_&nbsp;&nbsp;<%=toColumn%> of <%=columnCount%>&nbsp;&nbsp;
                                                                                Column Displayed
                                                                                <input type=text name="recordsSizeColumn" id="recordsSizeColumn" size=3 class="inputbox" style="text-align:right" onKeyUp="parent.doSetColumnSize(this,event,<%=columnCount%>,<%=columnsPerSlide%>,<%=container.getTableId()%>)" value="<%=columnsPerSlide%>">&nbsp;&nbsp;
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <%}

                                                      }--%>
                                                <% if (reportMode.equalsIgnoreCase("edit")) {%>
                                               <td ALIGN="left"   valign="top"><a   href="javascript:parent.reportColumnDrill('<%=drillColumnIds%>','<%=tabName%>','<%=TableId%>');"  title="Click For Drill Across Column"> Drill across Column </a>&nbsp;&nbsp;</td>
                                                <%}
                                                    if (isreportdrillPopUp == null) {
                                                        if (userdao.getFeatureEnable("Define Custom Measure") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
    
    
                                                    <td ALIGN="left"   valign="top"><a id="CustomMeasure" href="javascript:addCustomMeasure('<%=request.getContextPath()%>','<%=container.getReportId()%>')" class="ui-icon ui-icon-plus" title="Add Custom Measures" >Define Custom Measure</a></td>
    
    
                                                 <li id="showsql" style="width:150px;">
                                                    <a id="showSql" href="javascript:showSqlQuery('show')" title="Click to See SQL Query" >Display SQL Query </a>
                                                </li>
    
                                                      <td ALIGN="left" valign="top"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" />&nbsp;&nbsp;</td>
                                                <% }
                                                    if (userdao.getFeatureEnable("Define Custom Sequence") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                              <td><a onclick="parent.difineCustomSeq('<%=sbufRoles.toString().substring(1)%>','<%=tabName%>')"  class="ui-icon ui-icon-signal" title="Define Custom Sequence">Define Custom Sequence</a></td>
                                                <%   }
                                                        }
                                                    }
                                                    if (isreportdrillPopUp == null) {
                                                        if (userdao.getFeatureEnable("Table Save") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td>
                                                <td align="left" valign="top"><a style="text-decoration:none" onclick="saveTableRegion('<%=request.getContextPath()%>','<%=container.getReportId()%>')" title="saveTableRegion" class="ui-icon ui-icon-disk" href="javascript:void(0)" alt="">saveTableRegion</a></td>
                                                <%}
                                                    if (container.getViewByCount() == 1) {%>
                                                  <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td>
                                                 <td align="left" valign="top"><a style="text-decoration:none" onclick="handsonTable('<%=request.getContextPath()%>','<%=container.getReportId()%>')" title="handsontable" class="ui-icon ui-icon-person" href="javascript:void(0)" alt="">handsontable</a></td>
                                                <% }%>
                                                <%if (container.isTransposed()) {%>
                                                <td align="left" valign="top"><a style="text-decoration:none" onclick="splitBy()" title="Split By" class="ui-icon ui-icon-shuffle" href="javascript:void(0)" alt="">Split By</a></td>
                                                <% }%>
                                               <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td>
                                               <td align="left" valign="top"><a style="text-decoration:none" onclick="topBottom('<%=request.getContextPath()%>','<%=container.getReportId()%>')" title="Top/Bottom Table" class="ui-icon ui-icon-transferthick-e-w" href="javascript:void(0)" alt=""></a></td>
                                               <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td> -->
       <!--                                         &nbsp;&nbsp;<td align="left" valign="top">&nbsp;&nbsp;<span style="text-decoration:none;color:gray;" onclick="parent.hideMeasures('<%=request.getContextPath()%>','<%=container.getReportId()%>')" title="show/hide Measures" class="" href="javascript:void(0)" >Show/Hide Measure</span></td>
                                                <td ALIGN="left" valign="top">&nbsp;<img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></td>-->
       <!--                                         &nbsp;&nbsp;<td align="left" valign="top">&nbsp;&nbsp;<span style="text-decoration:none" onclick="parent.reportBugMail23('<%=request.getContextPath()%>','<%=container.getReportId()%>','<%=tabName%>')" title="Report Bug Mail" class="" href="javascript:void(0)" >Report Bug Mail</span></td>
                                                <td ALIGN="left" valign="top">&nbsp;<img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></td>-->
                                                <!--  <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td>
                                               <td align="left" valign="top"><a style="text-decoration:none"  title="Share Report" class="ui-icon ui-icon-contact" href="javascript:parent.openShareReportDiv()" alt=""></a></td>
       
                                                <%if (container.getViewByCount() > 1) {%>
                                                <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td>
                                                <td align="left" valign="top"><a style="text-decoration:none" onclick="parent.hideViewbys('<%=request.getContextPath()%>','<%=container.getReportId()%>')" title="show/hide Viewbys" class="ui-icon ui-icon-star" href="javascript:void(0)" alt=""></a></td>
                                                <%}%>
                                                <% if (userdao.getFeatureEnable("Import Excel") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                  <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td>
                                              <td align="left" valign="top"><a style="text-decoration:none" onclick="importExcelFile('<%=request.getContextPath()%>','<%=container.getReportId()%>')" title="import Excel File" class="ui-icon ui-icon-arrowthick-1-n" href="javascript:void(0)" alt=""></a></td>
                                                <%}%>
      
                                                <%if (container.getIsSubToTalSearchFilterApplied()) {%>
                                                 <td align="left" valign="top"><img alt="Separator" style="border:0" src="Images/separator.gif">&nbsp;&nbsp;</td>
                                                <td align="left" valign="top"><a style="text-decoration:none" onclick="showSubTotalSearchfilters('<%=request.getContextPath()%>','<%=container.getReportId()%>')" title="Show applied Search filters " class="ui-icon ui-icon-search" href="javascript:void(0)" alt=""></a></td>
                                                <td align="left" valign="top" id="SubTtlFilterText"></td>
                                                <% }
                                                    }%>
                                                </table>
                                    </td>
                                    </tr>-->
                                            <tr id="viewvalue">
                                                <td id="drillview" style="font-family: helvetica; font-size: 10px; color: rgb(150, 113, 23);" >
                                                    <% if (container.getDrillViewValues() != null && container.isdrillvalues() == true) {%>
                                                    <%--      <% for(int i=container.getDrillViewValues().size();i<5 || i==0;i--){ %>--%>
                                                    <%=container.getDrillViewValues().toString().replace("[", "").replace("]", "").replace(",", " >")%>
                                                    <% }%>
                                                    <%-- <% } %>--%>
                                                </td>

                                            </tr>

                                        </table>

                                        <div id="excelSheet" style="height: 100%"></div>

                                        <script>
                                                var divob= parent.document.getElementById('tabTable').offsetWidth;
                                                //   alert("uuuuuuuuuuuu   "+divob);
                                                var ww=  parent.$("#iframe1").width();
                                                // alert("ww     "+ww)

                                        </script>

                                        <!--    Start of code by manik for proper display of table   -->
                                        <div id="progenTableDiv1" style="display:'';width:100%;height:100%;overflow-y:auto; margin-top: -3px">
                                            <div id="qfilters" style="display:none;width: 10%;height:100%; margin-left: 2em; border: 1px solid #F4F4F4; border-radius: 12px;">
                                            </div>


                                            <script>
                                                    $("#progenTableDiv1").width(parent.$("#tabTable").width());
                                            </script>

                                            <script>
                                                    $("#progenTableDiv1").width(parent.$("#tabTable").width());
                                            </script>

                                            <% if (isreportdrillPopUp != null && isreportdrillPopUp.equalsIgnoreCase("true")) {%>
                                            <script>
                                                    var divobj= document.getElementById('progenTableDiv1');
                                                    divobj.style.height=((2/3)*<%=heipopup%>)+"px";
                                                    divobj.style.width="98%";
                                            </script>

                                            <% }// end of code by Manik for Table Display Region
                                                if (!isTopbottomTableEnable) {
                                                    buildTable.setContainer(container);
                                                    buildTable.setFromRow(fromRow);
                                                    buildTable.setToRow(toRow);
                                                    buildTable.setFromColumn(fromColumn);
                                                    buildTable.setToColumn(toColumn);
                                                    buildTable.setContextPath(request.getContextPath());
                                                    buildTable.setUserId(userId);
                                                    ////.println(retObj.hashCode());
                                                    //out.print(buildTable.getTabData(retObj, out));
                                                    if (container.isTreeTableDisplay()) {
                                                        out.print(buildTable.getHTML(out));
                                                        /*  buildInsightTable.setContainer(container);
                                                        buildInsightTable.setFromRow(fromRow);
                                                        buildInsightTable.setToRow(toRow);
                                                        out.print(buildInsightTable.generateInsightTableHeader());*/
                                                    } else if (ContainerConstants.PROGEN_TABLE_DISPLAY.equalsIgnoreCase(container.getTableDisplayMode())) {
                                                        out.print(buildTable.getHTML(out));
                                                    } else {
                                                        /*tableHTML = buildTable.getHTML(out);
                                                        menuHTML = buildTable.getMenuHTML(out);
                                                        excelDisplay = true;
                                                        List<String> rtColsList = container.getRtExcelColumns();
                                                        ArrayList<String> dispCols = container.getDisplayColumns();
                                                        targetApplicable = container.isTargetEntryApplicable();
                                                        for (int i=0;i<rtColsList.size();i++){
                                                        int index = dispCols.indexOf(rtColsList.get(i));
                                                        if (index >= 0)
                                                        rtCols = rtCols + "," + index;
                                                        }
                                                        if (!("".equals(rtCols)))
                                                        rtCols = rtCols.substring(1);
                                                        containerFromRow = fromRow;
                                                        containerToRow = toRow;*/
                                                        out.print(buildTable.getHTML(out));
                                                        menuHTML = buildTable.getMenuHTML(out);
                                                        excelDisplay = true;
                                                    }

                                                    //out.print(tableHTML);
                                                    cellSpanJSON = "";
                                                    //  cellSpanJSON = buildTable.getCellSpanJSON();
                                                    if (container.getIsSubToTalSearchFilterApplied() && container.getViewByCount() == 2) {
                                                        vcount = container.getSubTotalRowCount();

                                                        //     
                                                    }


//                                                    ProgenLog.log(ProgenLog.FINE, this, "pbDisplay.jsp", "Exit");
                                                    logger.info("Exit");                                                                %>
                                        </div>
                                        <!--       End of code by manik for proper display of table-->
                                        <%} else {%>


                                        <div id="topBottomDispDiv" style="width:100%;display:'';">
                                            <table style="width: 97%"><tr style="height:20%"><td style="width:100%;height:35px;"><u><span style="font-size:13px;font-family: Calibri, Calibri, Calibri, sans-serif;font-weight:bold;">Top/Bottom</span></u></td></tr></table>
                                            <table id="topBot" style="display:'';width:97%;height: 100%" cellpadding="2"   cellspacing="2"  class="ui-corner-all" >
                                                <tbody>
                                                    <tr align="center">
                                                <center> <img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>
                                                <td><iframe  scrolling="no" id="topbottomFrame" frameborder="0" style="width:100%;height:700px">
                                                    </iframe>
                                                </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <%}%>
                                        <!--                                    </td>
                                                                        </tr>-->
                                        <!--                            </table>-->
                                        <script type="text/javascript">
                                                if(isTopbottomEnable)
                                                    TableSelection();
                                                initCollapser("");
                                        </script>

                                        <script type="text/javascript">
                                                //added by govardhan...
                                                var isSubTotalFilter=<%=container.getIsSubToTalSearchFilterApplied()%>;
                                                var isSubtotalTopBottom=<%=container.isIsSubtotalTopBottom()%>;
                                                var viewbycount=<%=container.getViewByCount()%>;
                                                var CEP='<%=CEP%>';                                                
                                                CEP = CEP.split(", ");
                                                var sortColumns='<%=sortColumns%>';                                                
                                                var TotalRowCount=<%=container.getTotalRowCountAfterSubFilter()%>;
                                                var StartRowcountOfPage=<%=container.getCurrentpageSartRowCount() + 1%>;
                                                if((isSubTotalFilter || isSubtotalTopBottom) && (viewbycount==2 || viewbycount==3)){
                                                    var valueCnt=<%= container.getSubTotalRowCount()%>;
                                                    //                                   alert("TotalRowCount"+TotalRowCount+"StartRowcountOfPage"+StartRowcountOfPage+"valueCnt"+valueCnt);
                                                    document.getElementById('fVal').value=StartRowcountOfPage;
                                                    var  DistinctViewBys=<%=container.getDistinctViewBys().size()%>;
                                                    //                                  alert("sdgshfdhdfghhjj");
                                                    var FirstViewByname='<%=container.getDisplayLabels().get(0).toString()%>';
                                                    //                                  alert("DistinctViewBys"+DistinctViewBys+"FirstViewByname"+FirstViewByname);
                                                    //                                  code modified by Amar
                                                    //if(isSubtotalTopBottom && viewbycount==2 ){
                                                    if(isSubtotalTopBottom ){
                                                        // end of code modification by Amar
                                                        document.getElementById('recordsSize').innerHTML=(parseInt(StartRowcountOfPage)-1+parseInt(valueCnt));
                                                        document.getElementById('ToRowAndRowCount').innerHTML=(parseInt(StartRowcountOfPage)-1+parseInt(valueCnt))+" of "+(parseInt(StartRowcountOfPage)-1+parseInt(valueCnt));
                                                        document.getElementById('recordsSize').value=(parseInt(valueCnt));
                                                    }else{

                                                        document.getElementById('SubTtlFilterText').innerHTML="Number Of Distinct "+FirstViewByname+"'s Filtered are "+DistinctViewBys;
                                                        document.getElementById('ToRowAndRowCount').innerHTML=(parseInt(StartRowcountOfPage)-1+parseInt(valueCnt))+" of "+TotalRowCount;
                                                        // alert("valueCnt"+valueCnt);
                                                        document.getElementById('recordsSize').value=(parseInt(valueCnt));
                                                    }

                                                }
                                        </script>

                                        <input id="showSqlBox" type="hidden" value="<%=sqlString.replace("'", "")%>">
                                        <!-- added by sruthi to display full query  -->
                                        <input id="showSqlboxquery" type="hidden" value="<%=sqlString.replace("'", "")%>">
                                        <input id="showSqlboxfull" type="hidden" value="<%=reportquery.replace("'", "")%>">
                                        <!-- ended by sruthi                           -->
                                        </form>

                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <ul id="NFCtxMenu" class="contextMenu" style="width:auto;text-align:left;">
                                <li class="insert"><a href="#K">Thousands(K)</a></li>
                                <li class="insert"><a href="#Mn">Millions(Mn)</a></li>
                                <li class="insert"><a href="#Abs">Absolute</a></li>

                            </ul>
                            <div style="display:none" id="custmemDispDia" title="Custom Measures">
                                <iframe  id="custmemDisp" NAME='custmemDisp' height="100%" width="100%" frameborder="0" ></iframe>
                            </div>
                            <input type="hidden" id="whatifCheck" name="whatifCheck" value="0">
                            <%
                                container.setSessionContext(request.getSession(false), container);
                            %>
                            <script type="text/javascript">
                                    var ViewFrom="<%=session.getAttribute("ViewFrom")%>";
                                    if(ViewFrom=="Viewer"){
//                                        var source='<%//=request.getContextPath() + "/TableDisplay/pbGraphDisplayRegion.jsp"%>';
//                                        parent.refreshGraphs(source,<%=tabName%>);

                                    }

                                    if ( ViewFrom=="Search" )
                                        parent.hideProgress();
                                                            
                                $(document).ready(function(){
                                    resizeProgenDiv();
                                    <%if(!container.isReportCrosstab()){%>
                                        adjustReportArrows();
                                    <%}%>   
                                });
                       
                            </script>
                            <%
                                        } else {
                                            response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
                                        }
                                    } catch (Exception exp) {
                                        exp.printStackTrace();
                                    }
                                } else {
                                    response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
                                }%>



                            <script type="text/javascript">
                                    $(document).ready(function(){
                                        $("#progenTableDiv1").height($(window).height()-42+"px");
                                        $("#progenTableDiv1").width($(window).width());
                                        
                                        resizeProgenDiv();
                                            
                                            
                                        var html="";
                                        //  html +="<div><center><input type='button' style='margin-left:10px;' class='' onclick=gofilter() value='Go'  ></center></div>"
                                <%  Container container1 = null;

                                    HashMap map11 = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                                    if (map11 != null) {
                                        container1 = (Container) map11.get(tabName);
                                    }

                                    List<String> parameterlist = new ArrayList<String>();
                                    PbReportCollection collect = container1.getReportCollect();
                                    HashMap reportParam = collect.reportParameters;
                                    PbDb db = new PbDb();
                                    PbReturnObject retObj = null;
                                    String qry = "select DISTINCT ELEMENT_ID, PARAM_DISP_NAME, DISP_SEQ_NO from PRG_AR_REPORT_PARAM_DETAILS where REPORT_ID = '" + PbReportId + "' order by 3";
                                    retObj = db.execSelectSQL(qry);
                                    if (reportParam != null && !reportParam.isEmpty()) {
                                        for (int j = 0; j < reportParam.size(); j++) {
                                            viewbynames.add(retObj.getFieldValueString(j, "PARAM_DISP_NAME"));
                                            viewbyids.add(retObj.getFieldValueString(j, "ELEMENT_ID"));
                                        }

                                    }
                                    ArrayList viewbynames1 = (ArrayList) container1.getViewByColNames();
                                    for (int i = 0; i < viewbynames.size(); i++) {
                                        if (container1.getViewByElementIds().contains(viewbyids.get(i))) {
                                        } else {
                                            String vname = viewbynames.get(i).toString().replace(" ", "__");
                                %>

                                            html += "<div   style='font:14px; height:auto;'>";
                                <%if (i == 0) {%>
                                            html += "<span style='font-size:12px; color:gray;'><%= viewbynames.get(i)%></span><a id='showfiltr<%=viewbyids.get(i)%>'  style='margin-left: 55px;' onclick=lovfiltersreptable('<%=vname%>','<%=viewbyids.get(i)%>','null','table') ><img id='downid<%=viewbyids.get(i)%>' alt=''  border='0px'  style='height:17px;'  title='Show/Hide Filters'  src='<%=request.getContextPath()%>/images/arrow_down.png'/> </a>";
                                <% } else {%>
                                            html += "<span style='font-size:12px; color:gray;'><%= viewbynames.get(i)%></span> <a id='showfiltr<%=viewbyids.get(i)%>'  style='margin-left: 55px;' onclick=lovfiltersreptable('<%=vname%>','<%=viewbyids.get(i)%>','null','table') ><img id='downid<%=viewbyids.get(i)%>' alt=''  border='0px'  style='height:17px;'  title='Show/Hide Filters'  src='<%=request.getContextPath()%>/images/arrow_down.png'/> </a>";
                                <%}%>
                                            html += "</div>";

                                            html += "<ul id='showviewby<%=viewbyids.get(i)%>' style='display:none;margin-left:10px'>";



                                            html += "</ul>";
                                <%}
                                    }%>

                                                $("#qfilters").html(html);
                                                $("#datePickerForReport").datepicker({
                                                    changeMonth: true,
                                                    changeYear: true,
                                                    showButtonPanel: true,
                                                    numberOfMonths: 1,
                                                    stepMonths: 1,dateFormat: 'dd-mm-yy'
                                                });
                                                //                $("#datePickerForReport1").datepicker({dateFormat: 'dd-mm-yy'});
                                                $("#datePickerForReport1").datepicker({
                                                    changeMonth: true,
                                                    changeYear: true,
                                                    showButtonPanel: true,
                                                    numberOfMonths: 1,
                                                    stepMonths: 1,dateFormat: 'dd-mm-yy'
                                                });
                                                //                $("#datePickerForReport2").datepicker({dateFormat: 'dd-mm-yy'});
                                                $("#datePickerForReport2").datepicker({
                                                    changeMonth: true,
                                                    changeYear: true,
                                                    numberOfMonths: 1,
                                                    stepMonths: 1,dateFormat: 'dd-mm-yy'
                                                });
                                                //                $("#datePickerForReport3").datepicker({dateFormat: 'dd-mm-yy'});
                                                $("#datePickerForReport3").datepicker({
                                                    changeMonth: true,
                                                    changeYear: true,
                                                    showButtonPanel: true,
                                                    numberOfMonths: 1,
                                                    stepMonths: 1,dateFormat: 'dd-mm-yy'
                                                });
                                                //                $("#datePickerForReport4").datepicker({dateFormat: 'dd-mm-yy'});
                                                $("#datePickerForReport4").datepicker({
                                                    changeMonth: true,
                                                    changeYear: true,
                                                    numberOfMonths: 1,
                                                    stepMonths: 1,dateFormat: 'dd-mm-yy'
                                                });
                                                //                $( "input[name='srchText1']" ).datepicker({dateFormat: 'dd-mm-yy'});
                                                $( "input[name='srchText3']" ).datepicker({
                                                    changeMonth: true,
                                                    changeYear: true,
                                                    showButtonPanel: true,
                                                    numberOfMonths: 1,
                                                    stepMonths: 1,dateFormat: 'dd-mm-yy'
                                                });
                                                //                $( "input[name='srchText2']" ).datepicker({dateFormat: 'dd-mm-yy'});
                                                $( "input[name='srchText4']" ).datepicker({
                                                    changeMonth: true,
                                                    changeYear: true,
                                                    showButtonPanel: true,
                                                    numberOfMonths: 1,
                                                    stepMonths: 1,dateFormat: 'dd-mm-yy'
                                                });

                                                $('#colorSelector').ColorPicker({
                                                    color: '#0000ff',
                                                    onShow: function (colpkr) {
                                                        $(colpkr).fadeIn(500);
                                                        return false;
                                                    },
                                                    onHide: function (colpkr) {
                                                        $(colpkr).fadeOut(500);
                                                        return false;
                                                    },
                                                    onChange: function (hsb, hex, rgb) {
                                                        $('#colorSelector div').css('backgroundColor', '#' + hex);
                                                    }
                                                });
                                                $('#colorSelector1').ColorPicker({
                                                    color: '#0000ff',
                                                    onShow: function (colpkr) {
                                                        $(colpkr).fadeIn(500);
                                                        return false;
                                                    },
                                                    onHide: function (colpkr) {
                                                        $(colpkr).fadeOut(500);
                                                        return false;
                                                    },
                                                    onChange: function (hsb, hex, rgb) {
                                                        $('#colorSelector1 div').css('backgroundColor', '#' + hex);
                                                    }
                                                });

                                                //Start of Code By Nazneen For Div Resize
                                                //                      var width2=parent.document.getElementById("tabTable").offsetWidth;
                                                //                     var innerdiv1 = document.getElementById('progenTableDiv');
                                                //                     var px= width2+"px";
                                                //                     innerdiv1.style.width=px;
                                                //                      //End of Code By Nazneen For Div Resize
                                                //
                                                //                //by manik
                                                //                innerdiv1.style.height = 680 + "px";
                                                //                //



                                                $("#formatCell").dialog({
                                                    bgiframe: true,
                                                    autoOpen: false,
                                                    height: 200,
                                                    width: 300,
                                                    modal: true,
                                                    Cancel: function() {
                                                        $(this).dialog('close');
                                                    }
                                                });
                                                $("#addTargetDiv").dialog({
                                                    bgiframe: true,
                                                    autoOpen: false,
                                                    height: 100,
                                                    width: 300,
                                                    modal: true,
                                                    Cancel: function() {
                                                        $(this).dialog('close');
                                                    }
                                                });
                                                $("#addExcelColumnDiv").dialog({
                                                    bgiframe: true,
                                                    autoOpen: false,
                                                    height: 100,
                                                    width: 300,
                                                    modal: true,
                                                    Cancel: function() {
                                                        $(this).dialog('close');
                                                    }
                                                });
                                                $("#importExcelDiv").dialog({
                                                    bgiframe: true,
                                                    autoOpen: false,
                                                    height: 150,
                                                    width: 310,
                                                    modal: true
                                                });
                                                $("#drillAcrossDiv").dialog({
                                                    bgiframe: true,
                                                    autoOpen: false,
                                                    height: 150,
                                                    width: 310,
                                                    modal: true
                                                });
                                                $("#splitByDiv").dialog({
                                                    bgiframe: true,
                                                    autoOpen: false,
                                                    height: 110,
                                                    width: 280,
                                                    modal: true,
                                                    position:['center',10]
                                                });

//                                                    Added by Faiz Ansari
//                                        $("#progenTable").css({"transform":"translate(0px)"})
//                                        $("#theaddiv").css({
//                                            "position":"absolute"
//                                        });
//                                        $("#theaddiv").next().css({
//                                            "margin-top":$("#progenTable thead").height(),
//                                            "position":"absolute",
//                                            "overflow":"auto",
//                                            "height":($(window).height()-($("#progenTable thead").height()+42))
//                                        });
//                                    <%if(collect.reportColViewbyValues != null && collect.reportColViewbyValues.size() > 0){%>
//                                        resizeTblCont("crosstab");
//                                    <%}else{%>                                        
//                                        resizeTblCont("standrad");
//                                    <%}%>



                                                var cellSpans = '<%=cellSpanJSON%>';
                                                var tableContent = '<%=tableHTML%>';
                                                var menuContent = '<%=menuHTML%>';
                                                var excelDisp = <%=excelDisplay%>;
                                                var repId='<%= PbReportId%>'
                                                var ctxPath='<%=request.getContextPath()%>';
                                                var rtCols = '<%=rtCols%>';
                                                var fromRow = <%=containerFromRow%>;
                                                var toRow = <%=containerToRow%>;
                                                var targetApplicable = <%=targetApplicable%>;

                                                var rtColsArray = new Array();
                                                if (rtCols != ""){
                                                    var temp = rtCols.split(",");
                                                    for (var i=0;i<temp.length;i++){
                                                        rtColsArray.push(parseInt(temp[i]));
                                                    }
                                                }

                                                if (!excelDisp){
                                                    if ( cellSpans != "" )
                                                    {
                                                        //alert(cellSpans);
                                                        //var cellSpanObj = eval (cellSpans);
                                                        //alert(cellSpanObj.Span1);
                                                        //cellSpans = {Span1:["Row1","Row2"],Span2:["Row11","Row22"]};
                                                        //alert("Arun "+cellSpans);

                                                        var cellSpanObj = eval('('+cellSpans+')');
                                                        for ( var span in cellSpanObj )
                                                        {
                                                            var aCellSpan = cellSpanObj[span];
                                                            $('#'+aCellSpan[0]).attr('rowspan',aCellSpan[1]);
                                                        }
                                                    }
                                                    if (checkBrowser() != "ie"){
                                                        var tbody = document.getElementById("myTableBody");
                                                        var trobjs = tbody.getElementsByTagName("tr");
                                                        var lenoftrobjs = trobjs.length;
                                <%if (isGraphThere.equalsIgnoreCase("yes")) {%>
                                                    if(lenoftrobjs<=parseInt(10)){
                                                        document.getElementById("myTableBody").className='myTableBodyClass_limit';
                                                    }else{
                                                        if(screen.width=='800'){
                                                            document.getElementById("myTableBody").className='myTableBodyClassFor800';
                                                        }else{
                                <%if (session.getAttribute("GraphStatus").toString().equalsIgnoreCase("none")) {%>
                                                            document.getElementById("myTableBody").className='myTableBodyClassOnlyTable';
                                <%} else {%>
                                                            document.getElementById("myTableBody").className='myTableBodyClass';
                                <%}%>
                                                        }
                                                    }
                                <%} else {%>
                                                    if(lenoftrobjs<=parseInt(10)){
                                                        document.getElementById("myTableBody").className='myTableBodyClass_limit';
                                                    }else{
                                                        document.getElementById("myTableBody").className='myTableBodyClassOnlyTable';
                                                    }
                                <%}%>
                                                    /* document.getElementById("myTableBody").style.height = "200px";
                            document.getElementById("myTableBody").style.overflow = "auto";*/

                                                }

                                            }

                                            if (excelDisp){
                                                if (menuContent != "")
                                                    addBarTopMenu(menuContent);

                                                var paneRightClickMenu = jQuery("<ul id=\"contextmenu2\" class=\"jqcontextmenu\">"+
                                                    "<li><a href=\"javascript:addRow(this)\">Insert Row</a></li>"+
                                                    "<li><a href=\"javascript:openAddColumnDialog(this)\">Insert Column</a></li>"+
                                                    "<li><a href=\"javascript:insertComment()\">Insert Comment</a></li>"+
                                                    "<li><a href=\"javascript:clearComment()\">Clear Comment</a></li>"+
                                                    "<li><a href=\"javascript:openFormatCellDialog(this)\">Format Cell</a></li>"+
                                                    "<li><a href=\"javascript:clearFormat(this)\">Clear Format</a></li>"+
                                                    "</ul>");

                                                var headerRightClickMenu = jQuery("<ul id=\"contextmenu3\" class=\"jqcontextmenu\">"+
                                                    "<li><a href=\"javascript:addRow(this)\">Insert Row</a></li>"+
                                                    "<li><a href=\"javascript:openAddColumnDialog(this)\">Insert Column</a></li>"+
                                                    "</ul>");

                                                jQuery("#dummyArea").append(paneRightClickMenu);
                                                jQuery("#dummyArea").append(headerRightClickMenu);

                                                var commentDiv = jQuery("<div id=\"commentWindow\" style=\"display:none\">"+
                                                    "<textarea id=\"commentArea\" style=\"height:75px;width:200px;\"></textarea>"+
                                                    "</div>");

                                                jQuery("#dummyArea").append(commentDiv);

                                                $("#myTableBody").addcontextmenu('contextmenu2');
                                                $(".headerText").addcontextmenu('contextmenu3');
                                                $("#myTableBody").mousedown(function(evt){
                                                    if (evt.button == 2){   //On rightclick, update the cell information
                                                        updateSelectedCell(evt.target);
                                                    }
                                                });
                                                setReportId(repId);
                                                setContextPath(ctxPath);
                                                $("#jSheetControls_formula").keydown(handleKeyPress);

                                                $("#jSheetControls_formula").keyup(handleKeyUp);

                                                $("#myTableBody").mousedown(handleMouseDown)
                                                .mouseover(handleMouseOver)
                                                .bind("selectstart", function () {
                                                    return false; // prevent text selection in IE
                                                });

                                                $(document).mouseup(handleMouseUp);
                                            }
                                        });

                                        //sandeep
                                        var newuifilter={};
                                        var globalfiltervalue="";
                                         var filvallues1=[];
                             var finalids=[];
                                        function onmousediv(id) {
                    $("#"+id).attr("style","height:150px;border-bottom: 1px solid #d1d1d1;border-top: 1px solid #d1d1d1;overflow-y:scroll");
                                        }
                                        function onmouseoutdiv(id){
                                            $("#"+id).attr("style","height:150px;border-bottom: 1px solid #d1d1d1;border-top: 1px solid #d1d1d1;overflow:hidden");
                                        }
                                        function expandoptions(event){
                                               event.stopPropagation();
                                             $("#expandmore").toggle(200);
    }
                                        function showfilterview(event,elementnm,elementid,ulid,tdid,flagviewby,reset,submenu,isCrossTabReport,gtCrossTabLastButOneCol,tableParameter){
                                            event.stopPropagation(); //   TA_R_11 : Menus are not hiding when clicked on page/body
                                            $(".hideDropDown").slideUp("fast");
                                            var colviewbyflag = 'false';
                                            var ele2=document.getElementById("progenTableDiv1");                                            
                                            var filterMap1 = {};
                                            var hashmap=[];  parent.selectedview="true";  var viewname=[]; var viewids=[];
                                            var filterValues="";
                                            var filterValues1=""; var reportid='<%=PbReportId%>'
                                            var filterid="";
                                            var filterValues1=new Array;   var ctxPath='<%=request.getContextPath()%>'
                                            var usedviewbyid1="";
                                            var html="";var htmlctb="";var htmlNctb=""
                                            var size;
                                            var anchor_top = $("#"+tdid).offset().top+20;
//                                            if(tableParameter=='true')
//                                                {
//                                                       var leftMargin =12.7667+$("#"+tdid).offset().left; 
//                                                      
//                                                }
//                                                else {
//                                            var leftMargin =$("#"+tdid).children().children().offset().left;                                            
//                                                   
//                                                }
//                                            var width =$("#"+tdid).children().children().width();
                                            // TA_R_10 : Alignment issue of dropdown menubars on report header
//                                            if(submenu=="lastmeasure"){$("#"+ulid).css({'left':($(window).width()-($("#"+ulid).width()+5))});}
//                                            else{$("#"+ulid).css({'left':leftMargin});}
//                                            $("#"+ulid).css({'width':width});
//                                            $("#"+ulid).css({'top':anchor_top});
//                                            var leftMargin=leftMargin+((width-190)/2);
//                                            alert(flagviewby)
                                            var leftMargin=($("#"+ulid).parent().width()-180)/2;
                                            if(flagviewby == "viewby" || flagviewby=="ctviewby"|| $("#"+tdid).attr("align")=="left"){leftMargin=0;}
                                            if($("#"+tdid).attr("align")=="right"){leftMargin=$("#"+ulid).parent().width()-180;}
                                            $("#"+ulid).css({
                                                width:"180px",
                                                left:leftMargin
//                                                top:anchor_top+15
                                            });
                                            var jsonVar;
                                            if(flagviewby=='viewby' || flagviewby=='ctviewby'){
                           //sruthi
                         $.ajax({
        type:'POST',
        async: false,
        
        url: ctxPath+"/reportTemplateAction.do?templateParam=changeEditViewBy&reportId="+reportid+"&ctxpath="+ctxPath,
        success: function(data){
          
        }
    })
           parent.ViewByArrayId=[];
        parent.ViewByArrayName=[];
         parent.ColViewByArrayId=[];
        parent.ColViewByArrayName=[];
                                <%
ArrayList<String> viewbynamenew=new ArrayList<String>(); ArrayList<String> Viewbyidnew=new ArrayList<String>();
ArrayList<String> viewlist=new ArrayList<String>();
  
    HashMap<String,List> viewdata=new HashMap<String, List>();
  ArrayList<String> viewbyname=new ArrayList<String>();
                                      if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(PbReportId) != null) {
                 Container container = (Container) map.get(PbReportId);
                 if (container != null) {
                  collect = container.getReportCollect();   
                    HashMap inMap1;
                     inMap1 = (HashMap) collect.reportParameters;
                     List al12 = null;
                    // al12 = (List<String>) inMap1.get(a1[j]);
                 viewlist=container.getSelectedviewby();
                        
                   if(viewlist!=null && !viewlist.isEmpty()){
                       for(int k=0;k<viewlist.size();k++){
                         al12 = (List<String>) inMap1.get(viewlist.get(k));
                         viewbyname.add(al12.get(1).toString());
                       }
                 viewdata.put("Viewbyid",viewlist);
                 viewdata.put("Viewbyname",viewbyname);
                   
                   }
                                 }
                                 }
                       }
viewbynamenew=viewbyname;Viewbyidnew=viewlist;
                                    ArrayList name = viewbynames1;
                                    ArrayList nameold = viewbynames;
                                    ArrayList viewid = viewbyids;
                                    ArrayList Usedviewby1 = ViewbyCount1;
                                   ArrayList columnviewbysid=collect.reportColViewbyValues;
                                   ArrayList<String> columnviewbyname=container1.getSelectedviewby();
                                   HashMap colhmap = container1.getTableHashMap();
                                   ArrayList CEPNames1 =(ArrayList) colhmap.get("CEPNames");
                                   logger.info("columnviewbyname...."+columnviewbyname);
                                %>filvallues1='<%=viewbynamenew%>';finalids='<%=Viewbyidnew%>';
                                                usedviewbyid1='<%=Usedviewby1%>';
                                                filterValues1='<%=nameold%>';
                                                filterValues='<%=name%>';
                                                filterid='<%=viewbyids%>';
                                                var CEPNames = '<%=CEPNames1%>';
                                                CEPNames = CEPNames.split(", ");
// end of sruthi code
                                                // var filterValues1=filterValues.replace((/\[.]);
                                                // filterValues=filterMap1[elementid];
                                                usedviewbyid1=usedviewbyid1.split(", ");
                                                filterValues=filterValues.split(", ");
                                                    
                                               if (filvallues1 !='null' && filvallues1 != '' && filvallues1 != '[]'){
                                                     filterValues1=filvallues1.split(", ");
                                                
                                                          filterid=finalids.split(", ");
                                                   }else{
                                                filterValues1=filterValues1.split(", ");
                                                filterid=filterid.split(", ");
                                                   }
                                                size=filterValues.length;

var singleselection='<%=singleselection%>'
if(singleselection!=null && singleselection=='true'){
    parent.sigleselectflag=true
    }
                                                var reportid='<%=PbReportId%>'
                                                 $("#reportId").val(reportid)
 var ctxPath='<%=request.getContextPath()%>'
                                                 html+="<li><a onclick=expandoptions(event) >More</a><span style='float: right; margin-top: -18px; margin-right: 1em;'><i class='fa fa-plus'></i></span>";

                                                html+="<ul id='expandmore' style='display:none;'><li><a style='display:block;margin-left:2em;text-decoration:none;padding:5px;color:#333;' onclick=parent.sort('A_"+elementid+"','0','N','"+reportid+"','"+reportid+"') ><%=TranslaterHelper.getTranslatedInLocale("Sort_Ascend", cL)%></a></li>";
                                                html+="<li><a style='display:block;margin-left:2em;text-decoration:none;padding:5px;color:#333;'onclick=parent.sort('A_"+elementid+"','1','N','"+reportid+"','"+reportid+"') ><%=TranslaterHelper.getTranslatedInLocale("Sort_Descend", cL)%></a></li>";

                                                  html+="<li><a style='display:block;text-decoration:none;padding:5px;margin-left:2em;color:#333;'onclick=\"parent.renameMeasure('A_"+elementid+"','"+elementnm+"','"+ReportId+"','"+ctxPath+"')\" >Parameter Rename</a></li>";
                                                  //html += "<li><a><span onclick=parent.editViewByprop()><%=TranslaterHelper.getTranslatedInLocale("Select_Viewbys", cL)%> </span></a></li>"

                                            
                                                html += "<li><a style='display:block;text-decoration:none;padding:5px;margin-left:2em;color:#333;'><span onclick=parent.editViewByprop()><%=TranslaterHelper.getTranslatedInLocale("Select_Viewbys", cL)%> </span></a></li>"
                                                

if(submenu=='groupmeasure'){
                                                    html+="<li><a style='display:block;margin-left:2em;text-decoration:none;padding:5px;color:#333;' onclick=parent.twoRowViewGrouping('A_"+elementid+"','C','"+reportid+"','"+ctxPath+"') ><%=TranslaterHelper.getTranslatedInLocale("Grouping", cL)%></a></li>";

}
html+="</ul></li>"
var sortColumnFlag = 'false';
                                  if(sortColumns!=null&&sortColumns!=undefined&&sortColumns!=""){
                                      sortColumnFlag = 'true';
                                  }
                                                       if(parent.sigleselectflag){
      html += "<li style='padding:2px;'><div id='enablemultiselect' style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 0px; \"><span style='color:black;margin-left:1em;'  onclick=enablesnglselect(event,'"+sortColumnFlag+"','"+reportid+"','"+flagviewby+"')  >Enable Multi-Select</span></div></li>";

}else{
     html += "<li style='padding:2px;'><div id='enablemultiselect' style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 0px;background-color:lightgray;\"><span style='color:black;margin-left:1em;' onclick=enablesnglselect(event,'"+sortColumnFlag+"','"+reportid+"','"+flagviewby+"')  >Enable Multi-Select</span></div></li>";
     }
                                                html +="<ul id='ulmore"+elementid+"'  style=\"border-bottom: 1px solid #d1d1d1;border-top: 1px solid #d1d1d1; overflow:hidden;height:150px; padding:1px; border-radius: 1px;\" onmouseover=onmousediv('ulmore"+elementid+"') onmouseout=onmouseoutdiv('ulmore"+elementid+"') >"
                 

                                                var index=5;
                                                if(filterValues1.length>5){
                                                    index=filterValues1.length;
                                                }else{
                                                    index=filterValues1.length;
                                                }
 var flagtime="true";
                                                for(var i2=0;i2<usedviewbyid1.length;i2++){
                                                    colviewbyflag = 'false';
                                                    var flag='true';
                                                    var filtervalue=filterValues[i2];
                                                    var filteridvalues= usedviewbyid1[i2];
                                                    //alert(filteridvalues.replace("[","").replace("]",""))
                                                    // alert(filtervalue.replace("[","").replace("]",""))
                                                    //                 if(filteridvalues.replace("[","").replace("]","")==usedviewbyid1[i2].replace("[","").replace("]","")){
//                   html += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox' style='margin-left:3px;' id="+usedviewbyid1[i2].replace("[","").replace("]","")+" onclick=storearray('"+usedviewbyid1[i2].replace("[","").replace("]","")+"','"+encodeURIComponent(filtervalue.replace("[","").replace("]",""))+"') checked ><span style='color:black;margin-left:1em;'>"+filtervalue.replace("[","").replace("]","")+"</span></div></li>";
               if(filtervalue.toString().replace("[","").replace("]","")=='Time' || filtervalue.toString().replace("[","").replace("]","")=='TIME'){
     htmlNctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox' class ='enablemultiTIME' style='margin-left:3px;' onclick=storearray(event,'TIME','TIME') checked ><span style='color:black;margin-left:1em;'>"+filtervalue.replace("[","").replace("]","")+"</span></div></li>";
flagtime="false"
                 storearray1('TIME','TIME');
               }else{
                   htmlNctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox'  class ='enablemulti" + usedviewbyid1[i2].replace("[","").replace("]","") + "' style='margin-left:3px;' id="+usedviewbyid1[i2].replace("[","").replace("]","")+" onclick=\"storearray(event,'"+usedviewbyid1[i2].toString().replace("[","").replace("]","")+"','"+filtervalue.toString().replace("[","").replace("]","")+"')\" checked ><span style='color:black;margin-left:1em;'>"+filtervalue.replace("[","").replace("]","")+"</span></div></li>";
            storearray1(usedviewbyid1[i2].toString().replace("[","").replace("]",""),filtervalue.toString().replace("[","").replace("]",""));

               } 
                                                    globalfiltervalue=index;
                                                    flag='false';
                                                    var type=filtervalue.replace("[","").replace("]","");
                                                    if(type=='Time' || type=='TIME' || type=='Qtr' || type=='Day' || type=='Month' || type=='Year'){
                                                        flagtime="Time";
                                                    }
                                                    // }
                                                }for(var i1=0;i1<index;i1++){
                    
                                                    var filtervalue=filterValues1[i1];
       var filteridvalues= usedviewbyid1[i1];
                                                    var flag="true";
                                                    for(var i2=0;i2<usedviewbyid1.length;i2++){
                                                        if(usedviewbyid1[i2].toString().replace("[","").replace("]","")==filterid[i1].toString().replace("[","").replace("]","")){
                                                            flag="false";
                                                        }
                                                    }

                                                    if(flag=='true'){
                                                        htmlNctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox' class ='enablemulti" + filterid[i1].replace("[","").replace("]","") + "' style='margin-left:3px;' id="+filterid[i1].replace("[","").replace("]","")+" onclick=\"storearray(event,'"+filterid[i1].toString().replace("[","").replace("]","")+"','"+filterValues1[i1].toString().replace("[","").replace("]","")+"')\" ><span style='color:black;margin-left:1em;'>"+filterValues1[i1].replace("[","").replace("]","")+"</span></div></li>";
                                                        globalfiltervalue=i1+1;
                                                    }}
                                                 if(flagviewby=='viewby'){
                                                     html+=htmlNctb
}

                                                
                                                   
                                                   
                                  for(var i2=0;i2<CEPNames.length;i2++){
                                      colviewbyflag = 'true';
                                       //if(CEPNames[i2]!=null &&CEPNames[i2]!=undefined&&CEPNames[i2]!=""){
                                                    var flag='true';                                                    
                                                    var filtervalue=CEPNames[i2].replace("[","").replace("]","");
                                                   // var filteridvalues= CEP[i2].toString().replace("[","").replace("]","");                                                                                                        
                                  if(filtervalue.toString().replace("[","").replace("]","")=='Time' || filtervalue.toString().replace("[","").replace("]","")=='TIME'){
                                    htmlctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox' class ='enablemultiTIME' style='margin-left:3px;' onclick=ctbcheckboxarray(event,'TIME','TIME') checked ><span style='color:black;margin-left:1em;'>"+filtervalue.replace("[","").replace("]","")+"</span></div></li>";
                                    flagtime="false"
                                    ctbopenarray('TIME','TIME');
                                  }else{
                                    
                                     htmlctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox' class ='enablemulti" + CEP[i2].replace("[","").replace("]","") + "' style='margin-left:3px;' id="+CEP[i2]+" onclick=\"ctbcheckboxarray(event,'"+CEP[i2].toString().replace("[","").replace("]","")+"','"+filtervalue.toString().replace("[","").replace("]","")+"')\" checked ><span style='color:black;margin-left:1em;'>"+filtervalue.replace("[","").replace("]","")+"</span></div></li>";
                                     ctbopenarray(CEP[i2].toString().replace("[","").replace("]",""),filtervalue.toString().replace("[","").replace("]",""));
                                    
                                  } 
                                                    globalfiltervalue=index;
                                                    flag='false';
                                                    var type=filtervalue.toString().replace("[","").replace("]","");
                                                    if(type=='Time' || type=='TIME' || type=='Qtr' || type=='Day' || type=='Month' || type=='Year'){
                                                        flagtime="Time";
                                                    }                                                 
                                               // }
                                                }
                                              

                                                for(var i1=0;i1<index;i1++){
                    
                                                    var filtervalue=filterValues1[i1];
                                                    
                                                    var flag="true";                                                    
                                                    if(CEP!=null &&CEP!=undefined&&CEP!=""){
                                                        for(var i3=0;i3<CEPNames.length;i3++){
                                                           if(CEP[i3].toString().replace("[","").replace("]","")==filterid[i1].toString().replace("[","").replace("]","")){
                                                            flag="false";
                                                        }
                                                    } }                                                   
                                                    if(isCrossTabReport){
                                                        if(filterid[i1].toString().replace("[","").replace("]","")==elementid){
                                                            if(flag=='true'){
                                                                if(filterid[i1].replace("[","").replace("]","")==elementid)
                                                        htmlctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox' class ='enablemulti" + filterid[i1].replace("[","").replace("]","") + "' style='margin-left:3px;' id="+filterid[i1].replace("[","").replace("]","")+" checked onclick=\"ctbcheckboxarray(event,'"+filterid[i1].toString().replace("[","").replace("]","")+"','"+filterValues1[i1].toString().replace("[","").replace("]","")+"')\" ><span style='color:black;margin-left:1em;'>"+filterValues1[i1].replace("[","").replace("]","")+"</span></div></li>";
                                                    }
                                                        globalfiltervalue=i1+1;
                                                    }else{
                                                        if(usedviewbyid1[i1]!=undefined&&CEP[i1]!=undefined){                                                                 
                                                        if(usedviewbyid1[i1].toString().replace("[","").replace("]","")!=CEP[i1].toString().replace("[","").replace("]","")){
                                                        htmlctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox'  class ='enablemulti" + filterid[i1].replace("[","").replace("]","") + "' style='margin-left:3px;' id="+filterid[i1].replace("[","").replace("]","")+" onclick=\"ctbcheckboxarray(event,'"+filterid[i1].toString().replace("[","").replace("]","")+"','"+filterValues1[i1].toString().replace("[","").replace("]","")+"')\" ><span style='color:black;margin-left:1em;'>"+filterValues1[i1].replace("[","").replace("]","")+"</span></div></li>";
                                                       }
                                                        }else{
                                                            htmlctb += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox'  class ='enablemulti" + filterid[i1].replace("[","").replace("]","") + "' style='margin-left:3px;' id="+filterid[i1].replace("[","").replace("]","")+" onclick=\"ctbcheckboxarray(event,'"+filterid[i1].toString().replace("[","").replace("]","")+"','"+filterValues1[i1].toString().replace("[","").replace("]","")+"')\" ><span style='color:black;margin-left:1em;'>"+filterValues1[i1].replace("[","").replace("]","")+"</span></div></li>";
                                                        }
                                                    }                                                                                                                 
                                                    } 
                                                      
                                                }
                                                 if(flagviewby=='ctviewby'){
                                                     html+=htmlctb
                                                          if(flagtime=='true'){
         html += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox'  class ='enablemultiTIME' style='margin-left:3px;' onclick=ctbcheckboxarray(event,'TIME','TIME')><span style='color:black;margin-left:1em;'>Time</span></div></li>";
}
}else{
                                                if(flagtime=='true'){
         html += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input type='checkbox' class ='enablemultiTIME' style='margin-left:3px;' onclick=storearray(event,'TIME','TIME')><span style='color:black;margin-left:1em;'>Time</span></div></li>";
 }
 }
                                                html +="</ul>";
                                                html +="";
                                                //    html += "<li><center><input type='button' style='background-color:lightgray;height:20px;' onclick=parent.saveViewByForReport() value='Apply'  ></center></li>"

                                                html += "<li style='width: 100%;text-align: center;background-color: #8BC34A;'><span style='display: block;padding: 5px;font-size: 14px;color: #000;' onclick=parent.saveViewByForReport("+sortColumnFlag+")>Apply</span></li>"
                                                //html += "<li style='width: 100%;text-align: center;background-color: #8BC34A;'><span style='display: block;padding: 5px;font-size: 14px;color: #000;' onclick=parent.saveViewByForReport('"+<%=columnviewbysid%>+"')>Apply</span></li>"
//                                                $("#"+ulid).toggle(200);
//                                                $("#"+ulid).html(html);

                                                 if($(".repTblMnu").is(":visible")){$(".repTblMnu").slideUp("fast");}
                                                 if($("#"+ulid).is(":visible")){$(".repTblMnu").slideUp("fast");}
                                                 else{
                                                $("#"+ulid).html(html);
                                                     $("#"+ulid).slideDown("slow");
                                                }
parent.selectedview="false";
                                            }else{
                                                var reportid='<%=PbReportId%>'
                                                var ctxPath='<%=request.getContextPath()%>'
                                                showfilterMeasure(elementnm,elementid,ulid,tdid,flagviewby,reportid,ctxPath,reset,submenu,isCrossTabReport,gtCrossTabLastButOneCol);


                                            }


                                        }

                                        // added by krishan pratap

                                        function gofilter1(ulid,elementid){
                                            //alert("calling"+globalfiltervalue)
                                            var   filterValues="";
                                            var filterid="";
                                            var html=""
                                            var usedviewbyid="";

                                <%
                                    ArrayList name1 = viewbynames;
                                    ArrayList viewid1 = viewbyids;
                                    ArrayList Usedviewby = ViewbyCount1;
                                %>
                                            usedviewbyid='<%=Usedviewby%>';
                                            filterValues='<%=name1%>';
                                            filterid='<%=viewid1%>';
                                            usedviewbyid=usedviewbyid.split(", ");
                                            filterValues=filterValues.split(", ");
                                            // alert("filterValues"+filterValues)
                                            filterid=filterid.split(", ");
                                            // alert("usedviewbyid"+usedviewbyid)
                                            var index=5;
                                            if(filterValues.length>5){
                                                if(globalfiltervalue+5>filterValues.length){
                                                    index=filterValues.length;
                                                }
                                                else{
                                                    index=5+globalfiltervalue;

                                                }
                                                // alert("if"+index+"globalfiltervalue"+globalfiltervalue);
                                            }else{
                                                index=filterValues.length;
                                                // alert(index);
                                            }
                                            for(var i1=globalfiltervalue;i1<index;i1++){
                                                var flag1='true';
                                                var filtervalue=filterValues[i1];
                                                var filteridvalues= filterid[i1];
                                                for(var i2=0;i2<usedviewbyid.length;i2++){
                                                    //alert(usedviewbyid.length)
                                                    if(filteridvalues.replace("[","").replace("]","")==usedviewbyid[i2].replace("[","").replace("]","")){

                                                        //  alert("calling"+filteridvalues)
                                                        html += "<li><span style='color:gray;'><input type='checkbox' id="+filteridvalues.replace("[","").replace("]","")+" onchange=storearray('"+filteridvalues.replace("[","").replace("]","")+"','"+encodeURIComponent(filtervalue.replace("[","").replace("]",""))+"') checked >"+filtervalue.replace("[","").replace("]","")+"</span></li>";
                                                        storearray1(filteridvalues.replace("[","").replace("]",""),encodeURIComponent(filtervalue.replace("[","").replace("]","")));
                                                        globalfiltervalue=index;
                                                        flag1='false';
                                                    }
                                                }
                                                if(flag1=='true'){                   
                                                    html += "<li><span style='color:gray;'><input type='checkbox' id="+filteridvalues.replace("[","").replace("]","")+" onchange=storearray('"+filteridvalues.replace("[","").replace("]","")+"','"+encodeURIComponent(filtervalue.replace("[","").replace("]",""))+"') >"+filtervalue.replace("[","").replace("]","")+"</span></li>";
                                                    globalfiltervalue=index;
                                                }
                                            }


                                            $("#"+elementid+"contextmenu3").append(html);


                                        }
                                         window.onresize = function(event) {
                                            resizeProgenDiv();
                                            
//                                            $("#iframe1").height($(window).height());
//                                            $("#theaddiv").next().height(($(window).height()-($("#progenTable thead").height()+42)));
                                            
                                            if($(window).width() > $("#theaddiv").width()){
//                                                $("#theaddiv").css({
//                                                   "width":($(window).width()), 
//                                                   "background-color":"#f1f1f1"
//                                                });
//                                                $("#theaddiv").next().css({
//                                                    "width":($(window).width())                                                    
//                                                });
                                            }
                                          }
                                         function resizeProgenDiv(){
                                                $("#progenTableDiv1").height(($(window).height())-30);
                                               $("#progenTableDiv1").width($(window).width());
                                             }
                            </script>
                            <div id="formatCell" style="display: none;" title="Format Celtitlel">
                                <table>

                                    <tr>
                                        <td> Change Background Color <div id="colorSelector"><div style="background-color: rgb(140, 14, 64);"></div></div>
                                    </tr>
                                    <tr>
                                        <td height="20px"> </td>
                                    </tr>


                                    <tr>
                                        <td> Change Font Color <div id="colorSelector1"><div style="background-color: rgb(140, 14, 64);"></div></div>
                                    </tr>
                                    <tr>
                                        <td height="20px"> </td>
                                    </tr>

                                    <tr>
                                        <td align="center"><input type="button" class="navtitle-hover" value="Save" onClick="formatExcelCell()"></td>
                                    </tr>
                                </table>


                            </div>
                            <div id="addExcelColumnDiv" style="display: none;" title="Add Excel Column">
                                <table>
                                    <tr>
                                        <td>Column Name </td>
                                        <td><input id="colName" type="text"></td>
                                    </tr>
                                    <tr>
                                        <td align="center"><input type="button" class="navtitle-hover" value="Save" onClick="addExcelColumn()"></td>
                                    </tr>
                                </table>

                            </div>
                            <div  id="importExcelDiv"  title="Import Excel File" style="display:none;">
                                <form action="<%=request.getContextPath()%>/excel.do?reportBy=importRTExcelColumn&reportId=<%=PbReportId%>" enctype="multipart/form-data" method="post">
                                    <p>
                                        Please specify a file to import<br>
                                        <input type="file" name="datafile" size="27" id="datafile">
                                    </p>
                                    <div><center>
                                            <input type="submit"  class="navtitle-hover" value="Import" onclick="closeImportForm()"></center>
                                    </div>
                                </form>
                            </div>
                            <div  id="exportExcelDiv"  title="Export Excel File" style="display:none;">
                                <form action="<%=request.getContextPath()%>/ExporttoExcel.jsp?reportId=<%=PbReportId%>" enctype="multipart/form-data" method="post" id="exportForm">
                                </form>
                            </div>
                            <div  id="addTargetDiv"  title="Add Target" style="display:none;">
                                <br>
                                <table border="0">
                                    <tr>
                                        <td><b>Select Measure :</b> </td>
                                        <td>
                                            <select style="width: 130px;" class="myTextbox5"  name="TargetMeasures" id="TargetMeasures" >
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                                <br> <br>
                                <input type="button" class="navtitle-hover" value="Save" onclick="dynamicAddTargetCol()" >
                            </div>
                            <div id="drillAcrossDiv"  title="Drill Across" style="display:none;">
                                <br>
                                <table border="0">
                                    <tr>
                                        <td><b>Select Report :</b> </td>
                                        <td>
                                            <select style="width: 130px;" class="myTextbox5"  name="drillAcrossReport" id="drillAcrossReport" >
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                                <br> <br>
                                <center><input type="button" class="navtitle-hover" value="Go" onclick="doDrillAcross('<%=tabName%>', '<%=request.getContextPath()%>')" ></center>
                            </div>
                            <div id="dummyArea"></div>

                            <div id="transposeTableStyle" style="display:none">
                                Select Alignment:
                                <select id="selectAlignment">
                                    <option value="left">Left</option>
                                    <option value="right">Right</option>
                                </select><br>
                                Select FontStyle:
                                <select id="selectFont">
                                    <option value="normal">Normal</option>
                                    <option value="bold">Bold</option>
                                </select><br>
                                <table align="center"><tr><td>
                                            <input class="navtitle-hover" type="button" id="tableAttr" value="Done" onclick="setRowStyleAndFont()">
                                        </td></tr></table>
                            </div>
                            <span id="reptimeDetails">

                            </span>
                            <input type="hidden" id="rowIdVal" name="rowIdVal" value=""/>
                            <div id="splitByDiv" title="Split By" style="display:none ">
                                <table border="0" align="center">
                                    <tr>
                                        <%
                                            String splitbyChcked = "";
                                            String display = "none";
                                            boolean value = false;
                                            if (isSplitBy) {
                                                splitbyChcked = "checked";
                                                display = "block";
                                                value = true;
                                            }

                                        %>
                                        <td>Split By</td>
                                        <td><table><tr><td><input type="checkbox" value="<%=value%>" id="splitByCheckBox" <%=splitbyChcked%> name="splitByCheckBox" onclick="checkSplitBy(this.id,'spliBySelect')"></td>
                                                    <td ><select name="splitBy" id="spliBySelect" style="display:<%=display%>">
                                                            <%
                                                                if (timeDetails != null && !timeDetails.isEmpty()) {
                                                                    ArrayList nextTimeDetails = splitMap.get(timeDetails.get(3));
                                                                    if (nextTimeDetails != null) {
                                                                        for (int i = 0; i < nextTimeDetails.size(); i++) {%>
                                                            <option value="<%=nextTimeDetails.get(i)%>"><%=nextTimeDetails.get(i)%></option>
                                                            <% }
                                                                    }
                                                                }%>
                                                        </select></td></tr></table></td>
                                    </tr>
                                    <tr></tr>
                                    <tr>
                                        <td align="center">
                                            <input type="button" name="saveSplitBy" id="saveSplitBy" value="Apply" class="navtitle-hover" onclick="saveSplitBy('<%=tabName%>', '<%=request.getContextPath()%>')">
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div  id="handsonTableId" style="width: 100%; height:100%;display: none;">
                                <iframe id="handsonTableFrame" NAME='handsonTableFrame' width="100%" height="100%" frameborder="0" ></iframe>
                            </div>
                            <div id="colmunFormulaId" style="display: none;" title="Handsontable Column Formula">

                            </div>
                            <div id="columnFilterId" style="display: none;" title="Handsontable Column Filter">

                            </div>
                            <div id="hotColumnFilterId" style="display: none;" >

                            </div>
                            <form id="handsonTableParentFormId" name="" action="">
                                <table id="handsontableparentId" style="display: none"> </table>
                            </form>


                            <div id="SubTotalSrch" style="display:none">
                                <center>
                                    <br>
                                    <table style="width:100%" >
                                        <tr>
                                            <td valign="top" style="width:40%" id="SearchColumn">SubTotal Value</td>
                                            <td valign="top" style="width:20%">
                                                <select name="SubTotalSrchOption" id="SubTotalSrchOption" >
                                                    <option value='GT'>></option>"
                                                    <option value='GE'>>=</option>"
                                                    <option value='LT'><</option>"
                                                    <option value='LE'><=</option>"
                                                    <option value='EQ'>==</option>"
                                                </select>
                                            </td>
                                            <td valign="top" style="width:150px">
                                                <input type="text"  id="SubTtlSrch">
                                            </td>
                                        </tr>
                                    </table>
                                </center>
                                <center> <table><tr><td id="DistinctviewByText"></td>
                                        </tr></table>            </center>
                                <center>
                                    <input class="navtitle-hover" type="button" value="close" onclick="CloseFilterdiloge()">

                                </center>

                            </div>

                            <div id="showFormulaHandsOnTable" style="display: none"></div>
                            <!--                                              <div id="topBottomDispDiv" style="width:100%;display:none;">
                                                                              <table style="width: 97%"><tr style="height:20%"><td style="width:100%;height:35px;"><u><span style="font-size:13px;font-family: Calibri, Calibri, Calibri, sans-serif;font-weight:bold;">Top/Bottom</span></u></td></tr></table>
                                                         <table id="topBot" style="display:'';width:97%;height: 100%" cellpadding="2"   cellspacing="2"  class="ui-corner-all" >
                                                             <tbody>
                                                                 <tr align="center">
                                                             <center> <img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>
                                                                     <td><iframe src="about:blank" scrolling="no" id="topbottomFrame" frameborder="0" style="width:100%;height:700px"></iframe>
                                                                     </td>
                                                                 </tr>
                                                             </tbody>
                                                         </table>
                                                     </div>-->
                            <%
                                if (!ViewFrom.equalsIgnoreCase("Designer")) {
                                    PbReturnObject pbro = null;
                                    String query = "";
                                    query = "select GT_VISIBLE from prg_ar_report_master where report_id=" + PbReportId;
                                    pbro = pbdb.execSelectSQL(query);
                                    String gtVisible = pbro.getFieldValueString(0, 0);
                                    String displayprop = "";
                                    if (gtVisible != null && gtVisible.equalsIgnoreCase("true")) {
                                        displayprop = "";
                                    } else {
                                        displayprop = "none";
                                    }
                                    Container container = null;
                                    //ProgenLog.log(ProgenLog.FINE, this, "pbDisplay.jsp", "Enter");
                                    logger.info("Enter");
                                    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                                    if (map != null) {
                                        container = (Container) map.get(tabName);
                                    }
                                    String region = container.getGrandTotalSectionDisplay();

                            %>
                            <script type="text/javascript">
                                    var map = {};
                                <%

                                    Map<String, String> timeMap = container.getTimeDetailsMap(); // add by mayank

                                    ArrayList colIds = container.getDisplayGraphLabels();
                                    if (timeMap != null && colIds != null) {
                                        for (int col = 0; col < colIds.size(); col++) {
                                %>    
                                        var key = '<%=colIds.get(col).toString().replace("A_", "").trim()%>';
                                        var value = '<%= timeMap.get(colIds.get(col).toString().replace("A_", "").trim())%>';
                                        map[key] = value;
        
                                <% }%>
      
                                        parent.timeMapValue = map;   
                                <%
                                    }%>
                                            //added by mayank
   
                                            parent.displayFunction("<%=region%>","<%=displayprop%>");           
                                            function applyFilterToTrend(viewId, name,filterList){
                                                parent.applyFilterToTrend1("<%=PbReportId%>",viewId,name,filterList);
                                            }

                                            function hideGraphSec(text){
                                                if(text == "" || typeof text == "undefined" || text=="null" || text == null){
                                                    return false;
                                                }
                                                if(text=="Table"){
                                                    $("#paramHeaderId").css({'display':''});
                                                }else{
        
                                                    $("#paramHeaderId").css({'display':'none'});
                                                }
                                            }
                            </script>
                                            <script type="text/javascript">
            /*********************************
        Author:-Faiz Ansari
        Details:-
             *********************************/
            /***** Code Start *****/                      
            var ids="";
             var valueALT;
            valueALT=<%=Value1%>;
            var valueALT1;
            valueALT1='<%=Opertion%>';
            function showTSubMenu(event,id){
                event.stopPropagation(); //   TA_R_11 : Menus are not hiding when clicked on page/body
                ids=id;
//                $("#"+id).children().children().children().css({"display":"block"});
                if(! $("#"+id).children().children().children().is(":visible")){
                $(".viewBySubMenu").hide();                
                    $(".hideDropDown").slideUp("fast");
                    $("#"+id).children().children().children().slideDown("fast");
                    
            }
                else{
                    $("#"+id).children().children().children().slideUp("fast");
                }
                
            }
            function hideTSubMenu(){$("#"+ids).children().children().children().css({"display":"none"});}
             function hideme(id){
                 if(id=="mmoreFilters"){
                        $("#tableOption").fadeIn();
                   }
                   $("#"+id).slideUp();
               }
            /***** Code End *****/
            var isreportdrillPopUp=<%=isreportdrillPopUp%>;
            var isTopbottomEnable=<%=isTopbottomTableEnable%>;
            $(document).ready(function(){
                resizeProgenDiv();
                $(".hideDropDown").click(function(event){event.stopPropagation(); });
                $(".background").mouseover(function(){
                    var container = parent.$(".ui-multiselect-menu");
                    container.hide(500);
                });
                $(".background").mouseover(function(){
                    var container = parent.$("#themeselect2");
                    container.hide(500);
                });
                $(".background").mouseover(function(){
                    var container = parent.$("#modules");
                    container.hide(500);
                });
                $("#xtendChartssTD").mouseover(function(){
                    var container = parent.$("#themeselect2");
                    container.hide(500);
                });
                $("#transposeTableStyle").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    modal: true,
                    position:'justify',
                    title:'Format Row'
                });

                $("#colmunFormulaId").dialog({
                    autoOpen: false,
                    height: 450,
                    width: 350,
                    position: 'top',
                    modal: true
                });
                $("#columnFilterId").dialog({
                    autoOpen: false,
                    height: 260,
                    width: 255,
                    position: 'top',
                    modal: true
                });
                $("#hotColumnFilterId").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 500,
                    position: 'top',
                    modal: true
                });
            });

            //modified by Manik on 29Apr14
            //    function moveScroll(){
            //                //alert("inside function")
            //                var scroll = $("#progenTableDiv").scrollTop();
            //                var leftMargin= $("#progenTable").offset().left;
            //                var anchor_top= $("#progenTable").offset().top;
            //                //alert("left="+leftMargin+"top="+anchor_top)
            //                var anchor_top_static=0;
            //                var anchor_bottom = $("#bottom_anchor").offset().top;
            //                if (scroll>anchor_top_static && scroll<(anchor_bottom-anchor_top)) {
            //
            //                    clone_table = $("#clone");
            //                    if(clone_table.length == 0){
            //                        clone_table = $("#progenTable").clone();
            //                        clone_table.attr('id', 'clone');
            //                        clone_table.css({position:'fixed','pointer-events': 'none',top:36, 'background-color': '#C0C0C0'});
            //                        clone_table.width($("#progenTable").width());
            //                        clone_table.css('left',leftMargin);
            //                        $("#progenTableDiv").append(clone_table);
            //                        $("#clone").css({visibility:'hidden'});
            //                        $("#clone #theaddiv").css({visibility:'visible','pointer-events':'auto'});
            //                    }else {
            //                        clone_table.css('left',leftMargin);
            //
            //                    }
            //                } else {
            //                    $("#clone").remove();
            //                }
            //            }
            function moveScroll() {
                var scroll = $("#progenTableDiv").scrollTop();
                var leftMargin = $("#progenTable").offset().left;
                var anchor_top = $("#progenTable").offset().top;
                var anchor_top_static = 0;
                var anchor_bottom = $("#bottom_anchor").offset().top;

                // edit by Manik for header not to be displayed when rows are more than 100
                var rowsize = document.getElementById("recordsSize").value;
                if (rowsize <= 100) {

                    if (scroll > anchor_top_static && scroll < (anchor_bottom - anchor_top)) {

                        clone_table = $("#clone");
                        if (clone_table.length == 0) {
                            clone_table = $("#progenTable").clone();
                            clone_table.attr('id', 'clone');
                            clone_table.css({position: 'fixed', 'pointer-events': 'none', top: 36, 'background-color': '#C0C0C0'});
                            clone_table.width($("#progenTable").width());
                            clone_table.css('left', leftMargin);
                            $("#progenTableDiv").append(clone_table);
                            $("#clone").css({visibility: 'hidden'});
                            $("#clone #theaddiv").css({visibility: 'visible', 'pointer-events': 'auto'});
                        } else {
                            clone_table.css('left', leftMargin);

                        }
                    } else {
                        $("#clone").remove();
                    }
                }
            }
            //end of code  by Manik for header not to be displayed when rows are more than 100

            function refreshReportTables(ctxPath,reportId){
                $("#importExcelDiv").dialog('close');
                parent.refreshReportTables(ctxPath,reportId);
            }
            function refreshReportTables11(ctxPath,reportId){
               var pageSize="<%=request.getParameter("slidePages")%>";
                parent.refreshReportTables(ctxPath,reportId,pageSize);
            }
            function formatExcelCell(){
                var bgc=colorToHex($("#colorSelector div").css("backgroundColor"));
                var fontC=colorToHex($("#colorSelector1 div").css("backgroundColor"));
                formatExcelSheetCell(bgc,fontC);
            }
            function addExcelColumn(){

                var colName = $("#colName").val();
                dynamicAddCol(colName);
            }

            function openWhatifScenario(whatifMes,Measures,MeasuresNames,conPath,sliderValues){
                //alert("sliderValues"+sliderValues)
                var reportId='<%= PbReportId%>'
                var whatIfcheckValue= $('#whatifCheck').val()
                var whatifMesArr="";
                whatifMes = whatifMes.replace("[","").replace("]","")
                Measures = Measures.replace("[","").replace("]","")
                MeasuresNames = MeasuresNames.replace("[","").replace("]","")
                sliderValues=sliderValues.replace("[","").replace("]","")
                var whatifMesArr = whatifMes.split(",")
                var measArr = Measures.split(",")
                var measNamesArr = MeasuresNames.split(",")
                var sliderValuesArray=sliderValues.split(",")
                if (!parent.$("#demoContainer").mb_getState("closed")){
                    parent.closeWhatIfContainer()

                }else{
                    parent.openWhatIfContainer(whatifMesArr,reportId,measArr,measNamesArr,conPath,sliderValuesArray)
                }


            }
            function clearWhatifScenario(whatifMes,Measures,MeasuresNames,conPath,sliderValues){
                alert("Do You Want To Clear WhatIf?");
                var reportId='<%= PbReportId%>'
                //                alert(reportId);

                $.ajax({
                    url:'<%=request.getContextPath()%>/whatIfScenerioAction.do?whatIfParam=clearWhatIf&reportId='+reportId,
                    success:function(){
                        alert("WhatIf Icons Will Disappear Once You Reset The Report!");

                    }


                });




            }
            function openGroupMeasureList(contextPath,reportID){
                //                $.ajax({
                //        url:contextPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
                //        success:function(data){
                //            var jsonVar=eval('('+data+')')
                //            var userType=jsonVar.userType;
                //            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
                //            if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
                var varconfirm=confirm("Analytical functions in the new format will not be suported")
                if(varconfirm)
                    parent.openGroupMeasureList(contextPath,reportID)
                //            }else{
                //                alert("You do not have the sufficient previlages")
                //            }
                //        }});

            }


            function NFContextMenu(action, el, pos) {
                switch (action) {
                    case "K":
                        {
                            var cols=el.attr("id").split("~");
                            parent.formatNumber(cols[0], cols[1], "K", "<%=tabName%>", "<%=request.getContextPath()%>");
                            break;
                        }
                    case "Mn":
                        {
                            var cols=el.attr("id").split("~");
                            parent.formatNumber(cols[0], cols[1], "Mn", "<%=tabName%>", "<%=request.getContextPath()%>");
                            break;
                        }
                    case "Abs":
                        {
                            var cols=el.attr("id").split("~");
                            parent.formatNumber(cols[0], cols[1], "Abs", "<%=tabName%>", "<%=request.getContextPath()%>");
                            break;
                        }

                    }
                }

                function colorToHex(color) {
                if(color != undefined){
                    if (color.substr(0, 1) === '#') {
                        return color;
                    }
                    var digits = /(.*?)rgb\((\d+), (\d+), (\d+)\)/.exec(color);

                    var red = parseInt(digits[2]);
                    var green = parseInt(digits[3]);
                    var blue = parseInt(digits[4]);

                    var rgb = blue | (green << 8) | (red << 16);
                    return digits[1] + '#' + rgb.toString(16);
                }
                };

                function setRowAttribute(id,text,font)
                {
                    //                    alert("==="+id+"====")
                    //                    var rowId='rowId'+id;
                    //                    $("#"+rowId+" td").each(function(){
                    //                       // alert($(this).html());
                    //                        $(this).attr('style','text-align:left');
                    //
                    //                    })
                    if(text!=undefined&&font!=undefined)
                    {
                        $("select#selectAlignment").val(text) ;
                        $("select#selectFont").val(font);
                    }
                    $("#rowIdVal").val(id);
                    $("#transposeTableStyle").dialog('open');
                }

                function setRowStyleAndFont()
                {
                    $("#transposeTableStyle").dialog('close');
                    var rowId="rowId"+$("#rowIdVal").val();
                    var alignStyle="text-align:"+$("select#selectAlignment").val();
                    var fontStyle=$("select#selectFont").val();
                    if(fontStyle=='bold')
                        fontStyle="font-weight:bold";
                    else
                        fontStyle="font-weight:normal";
                    $("#"+rowId+" td").each(function(index){
                        $(this).attr('style',alignStyle);

                    })
                    $("#"+rowId+" font").each(function(){
                        $(this).attr('style',fontStyle);
                    })
                    $.post('<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=setTransposeFormats&rowId='+rowId+'&alignStyle='+$("select#selectAlignment").val()+'&fontStyle='+$("select#selectFont").val()+'&reportId='+<%=tabName%>,
                    function(data){

                    });
                }
                function loadChildData(currRowId, dimId, childDivId, paramVals){
                    // alert(paramVals)
                    var reportId='<%= PbReportId%>';
                    var currRow = $("#"+currRowId);
                    var isInitialized = currRow.attr("initialized");
                    $( "#"+childDivId+"prgBar").progressbar({value: 37});
                    if (!isInitialized){
                        // $( "#"+childDivId+"prgBar").remove();
                        //                    alert($("#insightTableData").html());
                        //                   var htmlVar="<table width=\"100%\" border=\"1\" id=\"insightTableData\"  class=\"tablesorter\" style=\"border-collapse:collapse;border-left-style: hidden;border-right-style: hidden;\">"
                        //                    htmlVar+=$("#insightTableData").html()+"</table>";
                        //                    $("#"+childDivId).html(htmlVar);
                        //                    currRow.attr("initialized","true");
                        //                    initCollapser(childDivId);
                        $.post('<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=getInsightTableData&viewByDim='+dimId+'&paramVals='+paramVals+'&reportId='+reportId,
                        function(data){
                            // alert(data)
                            $( "#"+childDivId+"prgBar").remove();
                            $("#"+childDivId).html(data);
                            currRow.attr("initialized","true");
                            initCollapser(childDivId);
                        });
                    }

                }
                function initCollapser(divId){
                    // alert(divId)
                    if (divId == ""){
                        $(".tablesorter")
                        .collapsible("td.collapsible", {
                            collapse: true
                        });
                        $(".prgtable")
                        .collapsible("td.collapsible", {
                            collapse: true
                        });

                    }
                    else{
                        $("#"+divId+" > .tablesorter")
                        .collapsible("td.collapsible", {
                            collapse: true
                        });
                    }
                }
                function loadChildDrillData(currRowId, masterDimId, dimId, dimData, childDivId, paramVals,childDimId,expand){
                    //alert(paramVals+"childDimId"+childDimId)
                    var reportId='<%= PbReportId%>'
                    var currRow = $("#"+currRowId);
                    var isInitialized = currRow.attr("initialized");
                    $( "#"+childDivId+"prgBar").progressbar({value: 37});
                    if (isInitialized && !expand)
                        return;
                    if (expand){
                        var anchorImg = currRow.find("td.collapsible a");
                        if (anchorImg.hasClass("collapsed")){
                            anchorImg.removeClass("collapsed");
                            anchorImg.addClass("expanded");

                            var childRow = currRow.next();
                            if (childRow.hasClass("expand-child")){
                                childRow.find("td").show();
                            }
                        }
                    }
                    $.post("<%=request.getContextPath()%>"+"/reportViewer.do?reportBy=getViewbyDrillData&viewByDim="+dimId+"&dimValue="+dimData+"&paramVals="+paramVals+"&masterDimension="+masterDimId+"&reportId="+reportId+"&childDimId="+childDimId,
                    function(data){
                        $( "#"+childDivId+"prgBar").remove();
                        $("#"+childDivId).html(data);
                        currRow.attr("initialized","true");
                        initCollapser(childDivId);
                    });
                }
                function checkSplitBy(id,selId){
                    //            alert('checkSplitBy');
                    if($("#"+id).is(':checked')){
                        $("#"+id).attr('value','true');
                        $("#"+selId).show();
                    }else{
                        $("#"+id).attr('value','false');
                        $("#"+selId).hide();
                    }
                }
                function splitBy(){
                    $("#splitByDiv").dialog('open');
                }
                function saveSplitBy(reportId,ctxPath){
                    var ischecked=$("#splitByCheckBox").val();
                    var splitval=$("#spliBySelect").val();
                    //            alert(reportId+' ,'+ctxPath);
                    $.post("<%=request.getContextPath()%>"+"/reportViewer.do?reportBy=saveSplitBy&reportId="+reportId+"&isSplitBy="+ischecked+"&splitValue="+splitval,
                    function(data){
                        $("#splitByDiv").dialog('close');
                        var path=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&action=paramChange";
                        parent.submiturls1(path);
                    });
                }
                var rowList = '';
                var colCount = '';
                var finalva = '';
                var disize = '';
                var pbReportId = '';
                var headerSize = '';
                function handsonTable(ctxPath,reportId){
                    if(document.getElementById("progenTableDiv").style.display==''){
                        document.getElementById("progenTableDiv").style.display='none'
                        document.getElementById("paramHeaderId").style.display='none'
                        document.getElementById("handsonTableId").style.display=''
                        $.ajax({
                            url:'<%=request.getContextPath()%>/handsontableaction.do?handsonParam=removeHandsontableSession',
                            success:function(){
                                var source = '<%=request.getContextPath()%>/handsontable.jsp?reportId='+reportId+"&ctxPath="+ctxPath;
                                var dSrc = document.getElementById("handsonTableFrame");
                                dSrc.src = source;
                            }
                        })
                    }
                    else{
                        document.getElementById("progenTableDiv").style.display=''
                        document.getElementById("paramHeaderId").style.display=''
                        document.getElementById("handsonTableId").style.display='none'
                    }
                }
                function isNumberKey(evt)
                {
                    var charCode = (evt.which) ? evt.which : event.keyCode
                    if (charCode != 46 && charCode > 31
                        && (charCode < 48 || charCode > 57))
                        return false;

                    return true;
                }
                function handsontableFormula(){
                    var operator = $("#handsonTableOperatorId").val();
                    var headerName = '';
                    headerName = $("#ColumnName").val();
                    var numberValueId = $("#hiddenNumberValue").val();
                    var operatorType = $("#operatorValueId").val();
                    var finalValu = rowList.toString().split(",");
                    var finalv = finalva.toString().concat(",").concat(headerName)

                    var selectedMe = new Array();

                    for(var i=1;i<disize;i++){
                        if($("#checkeValues"+i).is(":checked")){
                            selectedMe.push(i);
                        }
                    }
                    if(selectedMe.length==0){
                        alert("Please select atleast one measure")
                    }else if(headerName==''){
                        alert("Please Enter name")
                    }
                    else if(selectedMe.length>=1 && selectedMe.length<7){
                        $("#colmunFormulaId").dialog('close')
                        var tableval = "";
                        for(var i=0;i<finalValu.length;i++){
                            tableval+="<tr><td><input type='text' name='handsonData' value='"+finalValu[i]+"'></td><tr>";
                        }
                        $("#handsontableparentId").html(tableval);
                        $.post(
                        '<%=request.getContextPath()%>/handsontableaction.do?handsonParam=buildFormulaColumn&reportId='+pbReportId+"&calheaders="+encodeURIComponent(finalv)+"&selectedMe="+selectedMe+"&operator="+encodeURIComponent(operator)+"&numberValueId="+numberValueId+"&headerName="+encodeURIComponent(headerName)+"&operatorType="+encodeURIComponent(operatorType), $("#handsonTableParentFormId").serialize(),
                        function (data){
                            if(data=='success'){
                                //                        window.location.href=window.location.href
                                var source = '<%=request.getContextPath()%>/handsontable.jsp?reportId='+pbReportId;
                                var dSrc = document.getElementById("handsonTableFrame");
                                dSrc.src = source;
                            }
                        }
                    );
                    }else{
                        alert("Please select up to Ten measures only")
                    }
                }
                //sandeep

                function showHideFilr(id,arrowid,i){
                    $("#"+id).toggle(500);
                    var html="";
                    if(document.getElementById(arrowid)!=null && document.getElementById(arrowid).style.display==''){
                        html += "<img id='upid"+i+"' alt=''  border='0px' style='height:17px;' title='Show/Hide Filters'  src='<%=request.getContextPath()%>/images/arrow_up.png'/>";
                    }else{
                        html += "<img id="+arrowid+" alt=''  border='0px'   style='height:17px;' title='Show/Hide Filters'  src='<%=request.getContextPath()%>/images/arrow_down.png'/>";
                    }
                    $('#showfiltr'+i).html('');
                    $('#showfiltr'+i).html(html);
                }
                var filterValues=[];
                function chekfilters(value,id){
                    var filtervalue=value.replace("__"," ");
                    filterValues.push(filtervalue);
                    parent.$("#CBOARP"+id).val(JSON.stringify(filterValues));

                }
                function chekfilters1(value,id){
                    var filtervalue=value.replace("__"," ");
                    filterValues.push(filtervalue);
                    parent.$("#CBOARP"+id).val(JSON.stringify(filterValues));
                    parent.submitform();
                }
                function gofilter(){
                    parent.submitform();
                }
                //end of sandeep code for new ui filters
                function showFormulaHandsOnTable(){
                    // alert("click ok")
                    $("#showFormulaHandsOnTable").dialog('close');
                }

                function clearFormula(){
                    $("#formulaId").val('');
                }
                var operatorVal = ''
                function selectMeasure(){
                    operatorVal = '';
                    var selectedMe = new Array();
                    for(var i=1;i<disize;i++){
                        if($("#checkeValues"+i).is(":checked")){
                            selectedMe.push(i);
                        }
                    }
                    if(selectedMe.length==0){
                        $('#handsonTableOperatorId option[value=""]').attr('selected','selected');
                        alert("Please Select Measures!");
                    }else{
                        var finalv = finalva.split(",")
                        operatorVal = $("#handsonTableOperatorId").val();
                        var testvalue = '(';
                        for(var i=0;i<selectedMe.length;i++){
                            if(i==0){
                                testvalue+=finalv[selectedMe[0]];
                            }else{
                                testvalue+=operatorVal+''+finalv[selectedMe[0]];
                            }
                        }
                        testvalue+=')';
                        var finalVal = testvalue.toString()
                        $("#formulaId").val(finalVal)
                    }
                }
                function numberValue(operator){
                    var numberValueId = '';
                    numberValueId = $("#numberValueId").val();
                    var selectedMe = new Array();
                    for(var i=1;i<disize;i++){
                        if($("#checkeValues"+i).is(":checked")){
                            selectedMe.push(i);
                        }
                    }

                    var finalv = finalva.split(",")
                    if(numberValueId!=''){
                        if((selectedMe.length!=0 && operatorVal!='') || (selectedMe.length==1 && operatorVal=='')){
                            var testvalue = '(';
                            for(var i=0;i<selectedMe.length;i++){
                                testvalue+=finalv[selectedMe[0]]+''+operatorVal;
                            }
                            testvalue+=')'+operator+''+numberValueId;
                            $("#formulaId").val(testvalue)
                            $("#numberValueId").val('')
                            $("#hiddenNumberValue").val(numberValueId);
                            $("#operatorValueId").val(operator);
                        }else{
                            alert("Select Operator and Measures!")
                        }
                    }else{
                        alert("Please Enter a number!");
                    }
                }
                function resetHot(){
                    $.ajax({
                        url:'<%=request.getContextPath()%>/handsontableaction.do?handsonParam=removeHandsontableSession',
                        success:function(){
                            var source = '<%=request.getContextPath()%>/handsontable.jsp?reportId='+reportId+"&ctxPath="+ctxPath;
                            var dSrc = document.getElementById("handsonTableFrame");
                            dSrc.src = source;
                        }
                    })
                }
                var globalVal='';
                function handsontableFilters(){
                    var selectedMe = new Array();
                    var finalValu = rowList.toString().split(",");
                    var finalv = finalva.toString();
                    var selectedMeas = '';
                    for(var i=1;i<disize;i++){
                        if($("#checkFilterVal"+i).is(":checked")){
                            selectedMe.push(i);
                            globalVal = i;
                            selectedMeas=$("#checkFilterVal"+i).val();
                        }
                    }
                    if(selectedMe.length==1){
                        var tableval = "";
                        for(var i=0;i<finalValu.length;i++){
                            tableval+="<tr><td><input type='text' name='handsonData' value='"+finalValu[i]+"'></td><tr>";
                        }
                        $("#handsontableparentId").html(tableval);
                        $("#columnFilterId").dialog('close');
                        $.post(
                        '<%=request.getContextPath()%>/handsontableaction.do?handsonParam=builColumnFilter&reportId='+pbReportId+"&calheaders="+disize+"&selectedMe="+selectedMe+"&selectedMeas="+encodeURIComponent(selectedMeas), $("#handsonTableParentFormId").serialize(),
                        function (data){
                            $("#hotColumnFilterId").html(data)
                            $("#hotColumnFilterId").dialog('option','title',selectedMeas+' Hot Column Filter Values');
                            $("#hotColumnFilterId").dialog('open');
                            //                       if(data=='success'){
                            //                        window.location.href=window.location.href
                            //                        var source = '<%=request.getContextPath()%>/handsontable.jsp?reportId='+pbReportId;
                            //                        var dSrc = document.getElementById("handsonTableFrame");
                            //                        dSrc.src = source;
                            //                    }
                        }
                    );
                    }else{
                        alert("Please select one measure only!");
                    }
                }
                function operatorOnchange(){
                    var operator = $("#operatorValueId").val();
                    if(operator=='<>'){
                        document.getElementById("firstValueOnlyId").style.display='none';
                        document.getElementById("firstValue1Id").style.display='';
                        document.getElementById("secondValueId").style.display='';
                    }
                    else{
                        document.getElementById("firstValueOnlyId").style.display='';
                        document.getElementById("firstValue1Id").style.display='none';
                        document.getElementById("secondValueId").style.display='none';
                    }
                }
                function applayFiltering(){


                    var firstvalOnly = $("#firstValueId").val();
                    var firstVal = $("#firstValId").val();
                    var secondVal = $("#secondValId").val();
                    var operator = $("#operatorValueId").val();
                    if((firstVal!='' && secondVal=='') || (firstVal=='' && secondVal!='')){
                        alert("Please Select Both Values")
                    }else {
                        $("#hotColumnFilterId").dialog('close');
                        var finalValu = rowList.toString().split(",");
                        var tableval = "";
                        for(var i=0;i<finalValu.length;i++){
                            tableval+="<tr><td><input type='text' name='handsonData' value='"+finalValu[i]+"'></td><tr>";
                        }
                        $("#handsontableparentId").html(tableval);
                        $.post(
                        '<%=request.getContextPath()%>/handsontableaction.do?handsonParam=applyingColFilter&reportId='+pbReportId+"&calheaders="+encodeURIComponent(finalva)+"&operator="+encodeURIComponent(operator)+"&firstvalOnly="+firstvalOnly+"&firstVal="+firstVal+"&secondVal="+secondVal+"&globalVal="+globalVal, $("#handsonTableParentFormId").serialize(),
                        function (data){
                            //                       if(data=='success'){
                            var source = '<%=request.getContextPath()%>/handsontable.jsp?reportId='+pbReportId;
                            var dSrc = document.getElementById("handsonTableFrame");
                            dSrc.src = source;
                            //                    }
                        }
                    );
                    }
                }
                function showFormulaHandsOntable(reportId,index){
                    $("#showFormulaHandsOnTable").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'top',
                        modal: true,
                        resizable:false,
                        model:true,
                        resize:'auto',
                        title:'View Formula'
                    });
                    $.ajax({
                        url:'<%=request.getContextPath()%>/handsontableaction.do?handsonParam=getReportFormula&reportId='+reportId+"&columnNum="+index,
                        success:function (data){
                            // alert(data)
                            $("#showFormulaHandsOnTable").html(data);
                            //                     alert("Successfully Saved")
                            $("#showFormulaHandsOnTable").dialog('open');
                        }

                    }

                );


                }
        </script>
                         <script type="text/javascript">

                var container = new Array();
                var onResizeHandler;

                function scrollbarWidth(){
                    var w;

                    if (! document.body.currentStyle)   document.body.currentStyle = document.body.style;

                    if (document.body.currentStyle.overflowY == 'visible' || document.body.currentStyle.overflowY == 'scroll'){
                        w = document.body.offsetWidth - document.body.clientLeft - document.body.clientWidth;
                    }else{
                        win = window.open("about:blank", "_blank", "top=0,left=0,width=100,height=100,scrollbars=yes");
                        win.document.writeln('scrollbar');
                        w = win.document.body.offsetWidth - win.document.body.clientLeft - win.document.body.clientWidth;
                        win.close();
                    }

                    return w;
                }

                function getActualWidth(e){
                    if (! e.currentStyle)   e.currentStyle = e.style;

                    return  e.clientWidth - parseInt(e.currentStyle.paddingLeft) - parseInt(e.currentStyle.paddingRight);
                }

                function findRowWidth(r){
                    for (var i=0; i < r.length; i++){
                        r[i].actualWidth = getActualWidth(r[i]);
                    }
                }

                function setRowWidth(r){
                    for (var i=0; i < r.length; i++){
                        r[i].width = r[i].actualWidth;
                        r[i].innerHTML = r[i].innerHTML ;
                        // r[i].innerHTML = '<span style="width:' + r[i].actualWidth + ';">' + r[i].innerHTML + '</span>';
                    }
                }

                function fixTableWidth(tbl){
                    for (var i=0; i < tbl.tHead.rows.length; i++)   findRowWidth(tbl.tHead.rows[i].cells);
                    findRowWidth(tbl.tBodies[0].rows[0].cells);
                    if (tbl.tFoot)  for (var i=0; i < tbl.tFoot.rows.length; i++)   findRowWidth(tbl.tFoot.rows[i].cells);

                    //tbl.width = '';

                    for (var i=0; i < tbl.tHead.rows.length; i++)   setRowWidth(tbl.tHead.rows[i].cells);
                    setRowWidth(tbl.tBodies[0].rows[0].cells);
                    if (tbl.tFoot)  for (var i=0; i < tbl.tFoot.rows.length; i++)   setRowWidth(tbl.tFoot.rows[i].cells);
                }                
                function makeScrollableTable(tbl,scrollFooter,height){
                        
                    /*var c, pNode, hdr, ftr, wrapper, rect;

                    if (typeof tbl == 'string') tbl = document.getElementById(tbl);

                    pNode = tbl.parentNode;
                    fixTableWidth(tbl);

                    c = container.length;
                    container[c] = document.createElement('<SPAN style="height: 100; overflow: auto;">');
                    container[c].id = tbl.id + "Container";
                    pNode.insertBefore(container[c], tbl);
                    container[c].appendChild(tbl);
                    container[c].style.width = tbl.clientWidth + 2 * tbl.clientLeft + scrollbarWidth();

                    hdr = tbl.cloneNode(false);
                    hdr.id += 'Header';
                    hdr.appendChild(tbl.tHead.cloneNode(true));
                    tbl.tHead.style.display = 'none';

                    if (!scrollFooter || !tbl.tFoot){
                        ftr = document.createElement('<SPAN style="width:1;height:1;clip: rect(0 1 1 0);background-color:transparent;">');
                        ftr.id = tbl.id + 'Footer';
                        ftr.style.border = tbl.style.border;
                        ftr.style.width = getActualWidth(tbl) + 3 * tbl.clientLeft;
                        ftr.style.borderBottom = ftr.style.borderLeft = ftr.style.borderRight = 'none';
                    }else{
                        ftr = tbl.cloneNode(false);
                        ftr.id += 'Footer';
                        ftr.appendChild(tbl.tFoot.cloneNode(true));
                        ftr.style.borderTop = 'none';
                        tbl.tFoot.style.display = 'none';
                    }

                    wrapper = document.createElement('<table border=0 cellspacing=0 cellpadding=0>');
                    wrapper.id = tbl.id + 'Wrapper';
                    pNode.insertBefore(wrapper, container[c]);

                    wrapper.insertRow(0).insertCell(0).appendChild(hdr);
                    wrapper.insertRow(1).insertCell(0).appendChild(container[c]);
                    wrapper.insertRow(2).insertCell(0).appendChild(ftr);

                    wrapper.align = tbl.align;
                    tbl.align = hdr.align = ftr.align = 'left';
                    hdr.style.borderBottom = 'none';
                    tbl.style.borderTop = tbl.style.borderBottom = 'none';

                    // adjust page size
                    if (c == 0 && height == 'auto'){
                        onResizeAdjustTable();
                        onResizeHandler = window.onresize;
                        window.onresize = onResizeAdjustTable;
                    }else{
                        container[c].style.height = height;
                    }*/
                    //                       parent.closeWhatIfContainer()
                }

                function onResizeAdjustTable(){
                    if (onResizeHandler) onResizeHandler();

                    var rect = container[0].getClientRects()(0);
                    var h = document.body.clientHeight - (rect.top + (document.body.scrollHeight - rect.bottom));
                    container[0].style.height = (h > 0) ? h : 1;
                }

                function printPage(){
                    var tbs = document.getElementsByTagName('TABLE');
                    var e;

                    for (var i=0; i < container.length; i++)    container[i].style.overflow = '';

                    window.print();

                    for (var i=0; i < container.length; i++)    container[i].style.overflow = 'auto';
                }
                //                function topBottom(ctxPath,reportId){
                //                    if(document.getElementById("progenTableDiv").style.display==''){
                //                 document.getElementById("progenTableDiv").style.display='none'
                //                    document.getElementById("paramHeaderId").style.display=''
                //                    $("#topBottomDispDiv").show();
                //        $.ajax({
                //            url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=getMultiRetObj&reportId='+reportId,
                //            success:function (data){
                //                  var source = '<%=request.getContextPath()%>/TopBottomTable.jsp?REPORTID=<%=PbReportId%>';
                //                        var dSrc = document.getElementById("topbottomFrame");
                //                        dSrc.src = source;
                //                        $("#imgId").hide();
                //            }
                //
                //
                //        });
                //        }
                //        else{
                //
                //            $.ajax({
                //                 url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=disableTopbottomEnable&reportId='+'<%=PbReportId%>',
                //                    success:function (data){
                //                    document.getElementById("progenTableDiv").style.display=''
                //                    document.getElementById("paramHeaderId").style.display=''
                //                    $("#topBottomDispDiv").hide();
                //                      }
                //                  });
                //        }
                //
                //    }
                function topBottom(ctxPath,reportId){
                    var topbottomenable;
                    if(isTopbottomEnable){
                        topbottomenable=false;
                        $.ajax({
                            url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=disableTopbottomEnable&reportId='+'<%=PbReportId%>'+'&topbottomenable='+topbottomenable,
                            success:function (data){

                                //                        alert(topbottomenable)

                                window.location.href= window.location.href;
                            }});
                    }else{
                        topbottomenable=true;
                        $.ajax({
                            url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=disableTopbottomEnable&reportId='+'<%=PbReportId%>'+'&topbottomenable='+topbottomenable,
                            success:function (data){
                                //                        alert(topbottomenable)

                                window.location.href= window.location.href;
                            }});
                    }

                }

                function TableSelection(){
                    //  alert("tableselection***"+isTopbottomEnable)
                    document.getElementById("paramHeaderId").style.display=''
                    $("#topBottomDispDiv").show();
                    $.ajax({
                        url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=getMultiRetObj&reportId='+'<%=PbReportId%>',
                        success:function (data){
                            var jsonVar=eval('('+data+')');
                            var perBy=jsonVar.TopBottomVal;
                            var msrBy=jsonVar.TopBottomMsr;
                            //                 alert(perBy+",,,,"+msrBy)
                            var source = '<%=request.getContextPath()%>/TopBottomTable.jsp?REPORTID='+'<%=PbReportId%>'+'&perBy='+perBy+'&msrBy='+msrBy;
                            var dSrc = document.getElementById("topbottomFrame");
                            dSrc.src = source;
                            $("#imgId").hide();
                        }


                    });
                }



        </script>
                            <% }%>
                            </body>
                            </html>
                            <%}%>

