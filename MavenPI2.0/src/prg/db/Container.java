/*
 * Container.java
 *
 * Created on June 6, 2009, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.db;

/**
 *
 * @author Administrator
 */
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.query.RunTimeMeasure;
import com.progen.report.*;
import com.progen.report.colorgroup.ColorGroup;
import com.progen.report.data.DataFacade;
import com.progen.report.data.RunTimeMeasCalculator;
import com.progen.report.excel.ExcelCellFormatGroup;
import com.progen.report.excel.ExcelColumnGroup;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.query.PbDashBoardQuery;
import com.progen.report.query.PbReportQuery;
import com.progen.report.segmentation.Segment;
import com.progen.report.whatIf.WhatIfScenario;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.OverlayData;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.ContainerConstants.SortOrder;
import prg.util.ProgenTimeDefinition;

public class Container implements Serializable {

    public static Logger logger = Logger.getLogger(Container.class);

    public static boolean isColorAppyForAllParameters() {
        return colorAppyForAllParameters;
    }

    public static void setColorAppyForAllParameters(boolean aColorAppyForAllParameters) {
        colorAppyForAllParameters = aColorAppyForAllParameters;
    }

    public static boolean isColorAppyAcrossCurrentData() {
        return colorAppyAcrossCurrentData;
    }

    public static void setColorAppyAcrossCurrentData(boolean aColorAppyAcrossCurrentData) {
        colorAppyAcrossCurrentData = aColorAppyAcrossCurrentData;
    }
    private boolean measureGroupEnable;
    private ArrayList<MeasureGroup> measureGroups = new ArrayList<MeasureGroup>();
    private MultiPeriodKPI multiPeriodKPI = null;
    private DashletPropertiesHelper dashletPropertiesHelper;
    private HashMap<String, DashletPropertiesHelper> dashlets = new HashMap<String, DashletPropertiesHelper>();
    private PbReportQuery viewbyqry;
    private PbDashBoardQuery viewbyqry1;
    public HashMap<String, HashMap<String, String>> rowaddingmapc = new HashMap<String, HashMap<String, String>>();
    Map<String, String> scriptIndicator = new HashMap<String, String>();
    public HashMap timememdetails = new HashMap();
    private String dateFormat;
    private HashMap fixedparamhashmap = new HashMap();
    private String dependentRepQry = null;
    private String tempRepQry = null;
    private String depviewby = null;
    private String textkpiViewBy = null;
    private ArrayList drillviewvalues = new ArrayList();
    private List<String> oneviewdrilltimedetails = new ArrayList<String>();
    private List<String> oneviewGraphTimedetails = new ArrayList<String>();
    private List<String> oneviewTableTimedetails = new ArrayList<String>();
    private LinkedHashMap currentTimeDetails = new LinkedHashMap();
    private boolean drillViewByCheck;
    private String reSetColor;
    private String oneviewTableDate = null;
    private String oneviewGraphDate = null;
    private String drillfromRepId;
    private String selectedgraph = null;
    private ArrayList columns = new ArrayList();
    private HashMap changeGraphColumns = new HashMap();
    private String dateoptions = null;
    private String dateSubStringValues = null;
    private HashMap slectedGraphType = new HashMap();
    private String grpdivwidth;
    private String clearInformation;
    private String grpdivHeight;
    private String grpFrameHeight;
    private PbReturnObject kpiretObj = new PbReturnObject();
    private ArrayList<String> kpiQrycls = new ArrayList<String>();
    private LinkedHashMap oneviewRepParamDetails = new LinkedHashMap();
    private String onebusroleId;
    private String ONClickInformation;//added by Manoj 
    private String ElementIdvalue;
    private String Idsubtotal;
    private String lookupViewBys; //Added by Ram
     private String percentWise; 
    private HashMap<String, String> filterLookupData = new HashMap<String, String>();
    private HashMap<String, String> filterLookupOriginalToNew = new HashMap<String, String>();
    private HashMap<String, ArrayList<String>> initilizeFilterElement = new HashMap<String, ArrayList<String>>();
    private boolean reportDrill = false;//By Ram
    private ArrayList<String> explicitSortColumns = new ArrayList<String>();
    private char[] explicitSortTypes;
    private boolean subTotalSort;
    private boolean iskpidasboard = false;
    private boolean IsTimedasboard = false;
    //Faiz Ansari
    public String separator = "";
    //End!!
    private String percentWiseTable;
    private boolean isReportAccessible = false;
    private String garphIdForFact;
    private String subtotalsort;
    private String garphIdsForFact;
    private String folderIdsForFact;
    private String selectedParameterIds;
    private boolean isSplitBy;
    private boolean isSubtotalTopBottom = false;
    private String splitBy;
    private HashMap paramhashmapPA = new HashMap();
    private int subTotalRowCount = 0;
    private int TotalNumberOfPages = 0;
    private int CurrentpageSartRowCount = 0;
    private int TotalRowCountAfterSubFilter = 0;
    private Set<String> DistinctViewBys = new HashSet<String>();
    private HashMap<Integer, Integer> PageAndRowsMap = new HashMap<Integer, Integer>();
    private int runtimeColCount = 0;
    private String percentWiseTableName;
    private HashMap<String, Container> parentContainerMap;
    private HashMap<String, List<Map<String, String>>> chartData;
    //private boolean summarizedMeasuresEnabled=false;
    //  public String msrDrillReportSelection;
    public HashMap<String, HashMap<String, String>> msrDrillReportIdandnames = new HashMap<String, HashMap<String, String>>();
    HashMap<String, String> rowaddingmapkpi = new HashMap<String, String>();
    HashMap<String, String> crossalign = new HashMap<String, String>();
    public String customsetting = null;
    public String day0 = null;
    public String dated = null;
    public String fullName0 = null;
    public String day = null;
    public String date = null;
    public String fullName = null;
    public String cfday = null;
    public String cfdate = null;
    public String cffullName = null;
    public String ctday = null;
    public String ctdate = null;
    public String ctfullName = null;
    public Date cystdate = null;
    public String companyName = "";
    public String companyId = "";
    public String orgCompNames = "";
    public int totalComp = 0;
    public String srtUpCharVal = "";
    public HashMap<String, String> round = new HashMap<String, String>();
    public HashMap<String, String> no_format = new HashMap<String, String>();
    public HashMap<String, String> negative_val = new HashMap<String, String>();
    public HashMap<String, String> bgcolor = new HashMap<String, String>();
    public HashMap<String, String> fontcolor = new HashMap<String, String>();
    public HashMap<String, String> alignment = new HashMap<String, String>();
    public HashMap<String, String> symbol = new HashMap<String, String>();
    public HashMap<String, HashMap<String, String>> formatsMap = new HashMap<String, HashMap<String, String>>();
    public boolean isformatchanged = false;
    public boolean aoEditSummFlag = false;
    private String displayFiltersGlobal;
    public int graphviewByCount;
    public int prevStateCnt = 55;
    public boolean showPrevState = false;
    public String NewTabValue = "false";
    public HashMap<String, String> renamed = new HashMap<String, String>();
    public int repclctcnt = 55;
    public String fromBKP = "false";
    private PbReportCollection collect;
    public HashMap<Integer, PbReportCollection> collectionhashmap = new HashMap<Integer, PbReportCollection>();
    private boolean averageValue = false;
    public String gTAverage = "true";
    public HashMap<String, String> subGtVal = new HashMap<String, String>();
    private String GrandTotalSectionDisplay = "";
    public HashMap<String, String> Displaylabelmap = new HashMap<String, String>();
    public HashMap modifymeasure = new HashMap();
    public HashMap modifymeasureAttrChnge = new HashMap();
    private String topBottomDispaly = "Top5";
    public HashMap retObjHashmap = new HashMap();
    public boolean isRTMeasExists = false;
    private PbReturnObject insightRetObj = new PbReturnObject();
    private PbReturnObject dashretobj = new PbReturnObject();
    private String contextPath;
    private static final long serialVersionUID = 75264711556228L;
    private boolean quickRefreshEnabled = false;
    private boolean quickautoRefresh = false;
    private boolean refreshCompareFlag = false;
    public boolean Flagg = false;
    private String facadePath = null;
    private HashMap<String, ArrayList<String>> currAndPriorValOfMsrMap = new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> newUiFilter = new HashMap<String, ArrayList<String>>();
    public HashMap<String, String> rtMeasMap = new HashMap<String, String>();
    public ArrayList displaClsBkp = new ArrayList();
    private PbReturnObject MsrRetObj = new PbReturnObject();
    private boolean busTemplateFromOneview = false;
    //added by nazneen
    private ArrayList<String> withoutKpiQrycls = new ArrayList<String>();
    public ArrayList<String> measureIdsList = new ArrayList<String>();
    public ArrayList<String> timePeriodsList = new ArrayList<String>();
    public ArrayList<String> aggType = new ArrayList<String>();
    public ArrayList<String> DistinctTimePeriods = new ArrayList<String>();
    public ArrayList<String> DistinctMesList = new ArrayList<String>();
    public ArrayList<String> currentTimeperiods = new ArrayList<>();
    public ArrayList<String> AllColumns = new ArrayList<>();
    public ArrayList<String> qfilters = new ArrayList<>();
    public ArrayList<String> ViewBy = new ArrayList<>();
    private ArrayList<String> withKpiQrycls = new ArrayList<String>();
    private ArrayList<Object> rowData = new ArrayList<Object>();
    public String subTotalValue = "";
    private boolean isOthersFlag = false;
    public HashMap<String, String> summarizedHashmap = new HashMap<String, String>();
//    private PbReturnObject importExcelRetObj=new PbReturnObject();
    public int GTValBeforSearchFilter = 0;
    public boolean isSearchFilterApplied = false;
    public boolean setIsSubToTalSearchFilterApplied = false;
    public Locale locale = null;// for setting language added By Mohit Gupta
    public String rowGrandTotDispPos;
    public ImportExcelDetail importExcelDetails = null;
    public String filePath = "";
    private HashMap<String, String> renameDetails = new HashMap<String, String>();
    private boolean TotatRowsCountedAfterSubFilter = false;
    private String averagecalculationtype = "Exclude 0";
    public HashMap<String, String> crosscolmap = new HashMap<String, String>();
    //public HashMap<String , String> crosscolmap1 = new HashMap<String , String>();
    public HashMap<String, Integer> STcrosscolmap = new HashMap<String, Integer>();
    private String defavgcalculationtype = "Exclude 0";
    public HashMap<String, String> avgCalCMap = new HashMap<String, String>();
    public boolean rowGrandTotal = false;
    private int subTotalTopBottomCount = 0;
    public String reportTitleSize = "";
    public String reportTitleAlign = "";
    public String colorOnGrp = "";
    public String piTheme = "";
    public String sytm = "";
    public String fColor = "";
    public String hideDate = "";
    public String fromDateToDate = "";
    public String isYearCal = "";
    public String piVersion = "";
    public String defaultPageSize = "";  //added by Mayank
    public String Filters = "";
    public String Comparison = "";   // added by krishan
    public String datetoggl = "";    // added by krishan
    public String datashow = "";     // added by krishan
    public String report_Headrvalue = "";   //added by krishan
    public String chart_isDraggable = "";   //added by Ashutosh
    //Start of code by Nazneen on 18Jan14 for Analytical Object
    public String finalSql_AO = "";
    public String osql_AO = "";
    public String OViewByCol_AO = "";
    public String OorderByCol_AO = "";
    public String omsql_AO = "";
    public String OmViewByCol_AO = "";
    public String OmorderByCol_AO = "";
    public String ColOrderByCol_AO = "";
    public String osqlGroup_AO = null;
    public String finalViewByCol_AO = "";
    public String oWrapper_AO = "";
    public String tableName_AO = "";
    public String filters_AO = "";
    public boolean isAOEnable = false;
    public boolean isAOReport = false;
    public String AOId = "";
    public String cacheAO = "";
    public String isNewRowAdded = "";
    public String checkGOflag = "";
    public boolean grandtotalzero = false;//added by sruthi for hideGtZero
    public boolean numberheader = true;//added by sruthi for numberformate
    public HashMap summerizedgtval = new HashMap();//added by sruthi for hideGtZero
    public boolean isExcelAdded = false;
    public boolean isLoadedData = false;
    public boolean isPowerAnalyserEnableforUser = false; //added by Mohit for analyzer
    public String showall = "";              //added by krishan
    public String showiconreport = "";      //added by krishan
    public String showicongraph = "";       //added by krishan
    public String showicontrends = "";      //added by krishan
    public String showiconadvancevisual = "";  //added by krishan
    public boolean showicons = false;
    public String ReportLayout = "";  //added by mohit for kpi and none
    public String aoAsGoId = "";
    public boolean zeroFlag = false;
    public boolean isReportFromAO = false; //added by Mohit for analyzer
    public boolean saveAsNewRep = false;
    public String filepath1 = "";//added by sruthi for setpath after scheduling 22/12/2014
    public HashMap<String, String> isPercentColumnwithAbsolute = new HashMap<String, String>();
    public HashMap<String, BigDecimal> othervalues = new HashMap<String, BigDecimal>();//added by sruthi on 14 Nov 14 otherval for running total
    public ArrayList<BigDecimal> runningval = new ArrayList<BigDecimal>();//added by sruthi on 14 Nov 14 otherval for running total
    HashMap<String, List> inMap; // added by amar
    boolean isItermediateFiletrs = false;
    private static Map<String, Map<String, String>> dbData; //add dashboard data
    private Map<String, List<Map<String, String>>> dbrdData;
    private static Map<String, Map<String, String>> trendData; //add trend data
    private static Map<String, List<Double>> visualData; //add trend data
    private XtendReportMeta reportMeta;
    private XtendReportMeta reportTrendMeta;
    private OverlayData overlayData;
    private Map<String, Map<String, List<String>>> treeData;
    private Map<String, String> timeDetailsMap;
    public HashMap<String, BigDecimal> allMesChange = new HashMap<String, BigDecimal>();
    public ArrayList<String> ChangeColumns = new ArrayList<String>();
    public HashMap updateaggregation = new HashMap();//added by sruthi for  onthefly modifymeasures.
    public boolean flag = false;//added by sruthi for  onthefly modifymeasures.
    public String reportquery = null;//added by sruthi to display the full query
    public String aoSdate = "";
    public String tduration = "";
    public String aoEdate = "";
    public String aoTimePeriod = "daily";
//     public  ArrayList<String> DistinctTimePeriods =new ArrayList<String>();
//    public  ArrayList<String> DistinctMesList =new ArrayList<String>();
//    public ArrayList<String> currentTimeperiods = new ArrayList<>();
//   public ArrayList<String> AllColumns = new ArrayList<>();
//   public  ArrayList<String> measureIdsList =new ArrayList<String>();
//    public  ArrayList<String> timePeriodsList =new ArrayList<String>();
//    public HashMap<String, BigDecimal> allMesChange = new HashMap<String, BigDecimal>();
//      public ArrayList<String> ChangeColumns = new ArrayList<String>();
    private List<ArrayList<String>> calculativeVal = new ArrayList<ArrayList<String>>();   //Added By Ram
    private String Def_Comp = "";
    private boolean changeFlag = false;
    boolean compChnged = false;
    private List<ArrayList<String>> gtVal = new ArrayList<ArrayList<String>>();//Added By Ram
    public HashMap multiCalendarDetails = new HashMap();//added by sruthi for multicalendar
    // public ArrayList<String> selectedviews=new ArrayList<String>();
    //added by Dinanath
    public boolean isFromLocalCollect = false;
    public boolean dateenable = true;
     public Map<String,String> globalDateFlagMap = new HashMap<String,String>();
    public String dateClause="";
        public String searchdata=null;
public boolean enableComp = true;
    private static boolean colorAppyForAllParameters=false; //Added By Ram For colorCode on all viewBys
    private static boolean colorAppyAcrossCurrentData=false; //By Ram For colorCode on CrossTab Across Current Data
    private String globalDateFlagForheader="no";
    public   HashMap<String,ArrayList<String>> advdatalist=new HashMap<String,ArrayList<String>>();
    public void setFromLocalCollect(boolean isFromLocalCollect) {
        this.isFromLocalCollect = isFromLocalCollect;
    }

    public boolean getFromLocalCollect() {
        return this.isFromLocalCollect;
    }

    public HashMap<String, HashMap<String, String>> getMsrDrillReportIdandnames() {
        return msrDrillReportIdandnames;
    }
    private String excelFilePath;

    public void setMsrDrillReportIdandnames(HashMap<String, HashMap<String, String>> msrDrillReportIdandnames) {
        this.msrDrillReportIdandnames = msrDrillReportIdandnames;
    }

    public String getMsrDrillReportSelection() {
        return reportCollect.msrDrillReportSelection;
    }

    public void setMsrDrillReportSelection(String msrDrillReportSelection) {
        this.reportCollect.msrDrillReportSelection = msrDrillReportSelection;
    }

    public ArrayList getReportParameterNames() {
        return ReportParameterNames;
    }

    public void setReportParameterNames(ArrayList aReportParameterNames) {
        ReportParameterNames = aReportParameterNames;
    }

    public ArrayList getReportParameterIds() {
        return ReportParameterIds;
    }

    public void setReportParameterIds(ArrayList aReportParameterIds) {
        ReportParameterIds = aReportParameterIds;
    }

    public HashMap getParameterIds() {
        return ParameterIds;
    }

    public void setParameterIds(HashMap aParameterIds) {
        ParameterIds = aParameterIds;
    }

    public boolean isMapEnabled() {
        return reportCollect.isMapEnabled;
    }

    public void setMapEnabled(boolean isMapEnabled) {
        this.reportCollect.isMapEnabled = isMapEnabled;
    }

    public LinkedHashMap getMoreKpiDetails() {
        return MoreKpiDetails;
    }

    public void setMoreKpiDetails(LinkedHashMap aMoreKpiDetails) {
        MoreKpiDetails = aMoreKpiDetails;
    }

    public HashMap getKpiQuery() {
        return kpiQuery;
    }

    public void setKpiQuery(HashMap aKpiQuery) {
        kpiQuery = aKpiQuery;
    }

    /**
     * Creates a new instance of Container
     */
    public Container() {
//        reportCollect = new PbReportCollection();
    }
    private String showlyAxislabel = "";
    private String showryAxislabel = "";
    private String showxAxislabel = "";
    private int measurePosition;
    private int currentPage = 1;
    private int fromRow = 0;
    private String pagesPerSlide = "0";
    private String defPagesPerSlide = "25";
    //private String sortColumn = null;
    private String cbxColumn = null;
    private String tableId = null;//It is report id assigning to a Table
    private ArrayList<String> displayColumns = null;
    private ArrayList<String> originalColumns = null;
    private ArrayList<String> displayColumnsBkp = null;
    private ArrayList<String> originalColumnsBkp = null;
    private ArrayList displayLabels = null;
    private ArrayList displayGraphLabels = null;
    private ArrayList displayLabelskpi = null;
    private ArrayList displayLabelsBkp = null;
    private ArrayList dataTypes = null;
    private ArrayList dataTypesBkp = null;
    private ArrayList dispTypes = null;
    private ArrayList dispTypesBkp = null;
    //used for sorting
    //private String sortType = null;
    private ArrayList<String> sortColumns = new ArrayList<String>(); //elementids in which sort happens
    private char[] sortTypes; //for each elementId what is the order of sort 0 - Ascend 1 - descend
    //used for sorting end
    private ArrayList<String> srchColumns = new ArrayList<String>();
    private ArrayList<String> srchCondition = new ArrayList<String>();
    private ArrayList<Object> srchValue = new ArrayList<Object>();
    private ArrayList<String> subTotalSrchColumns = new ArrayList<String>();
    private ArrayList<String> subTotalSrchCondition = new ArrayList<String>();
    private ArrayList<String> subTotalSrchValue = new ArrayList<String>();
    //private ArrayList srchReq = null;
    //private ArrayList totalCols = null;
    private ProgenDataSet ret = null;
    private PbReturnObject kpiRetObj = null;
    private PbReturnObject timeSeriesRetObj = null;
    private PbReturnObject priorRetObj = null;
    private PbReturnObject sortRet = null;
    private PbReturnObject displayedSet = null;
    private ArrayList selected = new ArrayList();
    private ArrayList<String> alignments = new ArrayList<String>();
//    private ArrayList signs = new ArrayList();
    private ArrayList<String> links = new ArrayList<String>();
    private ArrayList<String> rowViewList;
    private ProgenDataSet Grret = null;
    private String refreshGr = null;
    private HashMap repLinks = new HashMap();
    private HashMap dependentviewbyId = new HashMap();
    public HashMap dependentviewbyIdQry = new HashMap();
    public HashMap depViewByConditionsmap = new HashMap();
    public Boolean isDependentReport;
    public String parentid;
    private String groupColumns = "";
    private ArrayList tableList = new ArrayList();
    private int NoOfScheduleReport = 0;

    public ArrayList<String> getRowViewList() {
        return rowViewList;
    }

    public void setRowViewList(ArrayList<String> rowViewList) {
        this.rowViewList = rowViewList;
    }
    private ArrayList<String> columnViewList;

    public ArrayList<String> getColumnViewList() {
        return columnViewList;
    }

    public void setColumnViewList(ArrayList<String> columnViewList) {
        this.columnViewList = columnViewList;
    }
    //added by santhosh.kumar@progenbusiness.com
    private String graphTableId;
    private ArrayList tableRemainingColumns = null;
    private ArrayList tableRemainingDisplayColumns = null;
    private String currentPath;
    private boolean hideTable = false;
    private String frameHgt1 = "370";//for graph iframe
    private String frameHgt2 = null;//"370";// for table iframe
    private HashMap columnsVisibility = new HashMap();
    private HashMap swapGraphAnalysis = new HashMap();//added on 30/07/09 for swapping graph Analysis
    private String reportMode = "view";//added on 01/08/09  to find whether report opened in edit mode or view mode
    private String[] imgPaths;//added on 17/09/09
    public String[] DisplayColsfortimeDB;
    //private String reportName = "";//added on 17/09/09
    //private String reportId;
    private ArrayList viewByColNames;
    private ArrayList viewByElementIds;
    private HashMap[] graphMapDetails;
//    private String viewByCount = "1";
    private int viewByCount = 1;
    private ArrayList columnViewByElementIds = new ArrayList();
    private String columnViewByCount = "0";
    private PbReportCollection reportCollect = null;
    //private String reportDesc = "";//added on 17/09/09
    private HashMap ParametersHashMap = new HashMap();
    private HashMap TableHashMap = new HashMap();
    private HashMap GraphHashMap = new HashMap();
    private HashMap ReportHashMap = new HashMap();
    private String[] KPIS = null;
    private HashMap CrossTabGraphHashMap = null;
    //private pbDashboardCollection dashboardCollection = null;// added by santhosh.kumar@progenbusiness.com on 19-12-2009 for retreiving collection object
    private PbReturnObject displayedSetWithGT = null;
    private boolean srchReq = false;
    private boolean netTotalReq = false;
    private boolean grdTotalReq = false;
    private boolean avgTotalReq = false;
    private boolean catMinValueReq = false;
    private boolean catMaxValueReq = false;
    private boolean overAllMinValueReq = false;
    private boolean overAllMaxValueReq = false;
    private String tableSymbols = "";
    private boolean timeSeries = false;
    private String measureNamePosition = "";
    private boolean rowCountReq = false;
    private boolean measDrill = false;
    private boolean adhocDrill = false;
    private boolean Avgcalculation = false;
    private boolean treeTableDisplay = false;
    private boolean drillvalues = false;
    private boolean catAvgTotalReq = false;

    /*
     * private ArrayList showColumnTotals; private ArrayList
     * showColumnSubTotals; private ArrayList showColumnAvg; private ArrayList
     * showColumnMax; private ArrayList showColumnMin; private ArrayList
     * showColumnCatMax; private ArrayList showColumnCatMin; private ArrayList
     * showColumnSymbols;
     */
    private HashMap columnProperties = new HashMap();
    //private HashMap paramDefaultValuesHashMap = new HashMap();
    private HashMap ParameterGroupAnalysisHashMap = new HashMap();
    private String FavName = ""; //added on 29/12/2009 for Favourite Parameters
    private String FavDesc = ""; //added on 29/12/2009 for Favourite Parameters
    private HashMap DashboardHashMap = new HashMap();//added on 04/01/2010
    private String DbrdDesc = "";//added on 04/01/2010
    private String DbrdName = "";//added on 04/01/2010
    private String dashboardId;//added on 04/01/2010
    private HashMap kpisMap = new HashMap();//added on 04/01/2010
    private HashMap kpiIdNamesMap = new HashMap();//added on 04/01/2010
    private HashMap divDetails = new HashMap();//added on 04/01/2010
    private HashMap divGraphs = new HashMap();//added on 04/01/2010
    private HashMap grpColsbyDivMap = new HashMap();//added on 04/01/2010
    private HashMap newDbrdGraph = new HashMap();//added on 04/01/2010
    private HashMap kpiGrps = new HashMap();//added on 04/01/2010
//    private String columnDrillURL = "";
    private ArrayList<String> columnDrillLinks = new ArrayList<String>();
    private ArrayList tableMeasure = null;
    private ArrayList tableMeasureNames = null;
    private String timeLevel = null;
    private HashMap repReqParamsHashMap = new HashMap();
    private String defaultSortedColumn = null;
//    private String tableDisplayRows = null;
    private ArrayList timeDetailsArray;
    private HashMap kpiCommentHashMap = new HashMap();
    private String columnViewByName = null;
    //added by uday
    private String scenarioId;
    private String userTypeAdmin = null;
    private HashMap scenarioHashMap;
    private String scenarioName;
    private String scenarioDesc;
    private HashMap scenarioTimeRangeMap;
    private int columnsPerSlide = 0;
    private int defcolumnsPerSlide = 10;
    private int currentColumnPage = 1;
    private int fromColumn = 0;
    private boolean rowGrandTotalReq = false;
    private boolean rowNetTotalReq = false;
    //for new Dashboard design on 4-03-10
    private HashMap DBKPIHashMap;
    private HashMap DBKPIGraphHashMap;
    private HashMap DBGraphHashMap;
    //added by santhosh.k on 09-03-2010 for the purpose of column pagination
    private ArrayList displayColumnsInColumnPagination = null;
    private ArrayList displayLabelsInColumnPagination = null;
    //DisplayLabelsInColumnPagination
    //added by chiranjeevi on 13-03-2010 for tracking of Parameter Drill down
    private HashMap ParameterDrillMap = null;
    private String sqlStr = null;
    //added by k
    public HashMap TimeParameterHashMap = new HashMap();
    public HashMap ReportParameterHashMap = new HashMap();
    public ArrayList ReportParameterNames = new ArrayList();
    public ArrayList ReportParameterIds = new ArrayList();
    //added by k may 29-05-10....
    private LinkedHashMap MoreKpiDetails;
    private HashMap kpiQuery;
    private String kpinamesstr = "";
    private String kpielementsstr = "";
    private String SqlStr = "";
    private String ParamSectionDisplay = "";
    private String reportParamDrillAssis = "";
    private String parameterDrillDisplay = "";
    private String rowSuffix = null;
    private LinkedHashMap savegraphchanges = new LinkedHashMap();
    private HashMultimap<String, String> statMap = HashMultimap.create();
    public static final String MEDIAN = "median";
    public static final String MEAN = "mean";
    public static final String STANDARD_DEVIATION = "sd";
    public static final String VARIANCE = "variance";
    //Used for the google maps
    private List<String> mapMainMeasure = new ArrayList<String>();
    private List<String> mapMainMeasureLabel = new ArrayList<String>();
    private List<String> mapSupportingMeasures = new ArrayList<String>();
    private List<String> mapSupportingMeasuresLabels = new ArrayList<String>();
    public List<String> geographyDimensionIds = new ArrayList<String>();
    private HashMap<String, Segment> dimSegmentMap;
    private WhatIfScenario whatIfScenario = null;
    private ReportParameter repParameter;
    private HashMap ParameterIds = new HashMap();//for dashboard designer parameters( addded by k)
    private ColorGroup colorGroup = new ColorGroup();
    private ExcelCellFormatGroup excelCellGroup = new ExcelCellFormatGroup();
    private ExcelColumnGroup excelColumnGroup = new ExcelColumnGroup();
    private List<String> rtExcelColumns = new ArrayList<String>();
    private List<String> rtTargetColumns = new ArrayList<String>();
    private ArrayListMultimap<Integer, Integer> crosstabColumnSpan;
    private boolean viewByChanged = false;
    private ReportTableProperties tableProperties = new ReportTableProperties();
    private boolean isReportCrosstab;
    private HashMap dbrdViewerDispValue;
    private HashMap dbrdDispValue;
    public HashMap<String, Integer> measRoundingPrecisions = new HashMap<String, Integer>();
    private HashMap<String, ColorHelper> signHelperMap = new HashMap<String, ColorHelper>();
    private HashMap<String, ColorHelper> fontColorMap = new HashMap<String, ColorHelper>();
    private HashMap<String, ReportParameterSecurity> paramSecurityMap = new HashMap<String, ReportParameterSecurity>();
    private int noOfDays;
    private boolean drillAcrossSupported;
    private LinkedHashSet<SortColumn> sortColumnLst = new LinkedHashSet<SortColumn>();
    private ArrayListMultimap<String, String> crossTabGraphColMap = ArrayListMultimap.create();
    private HashMap<String, String> grpIdElementIdMap = new HashMap<String, String>();
    private String graphCrossTabMeas;
    private ArrayListMultimap<String, Integer> subTotRows = ArrayListMultimap.create();
    // private HashMap<String,ArrayList<String>> summerizedTableHashMap=new HashMap<String, ArrayList<String>>();
    public String ReportType = "R";
    public String denomTable = "";
    public String stype = "n";//bhargavi
    public int trwcnt = 0;//bhargavi
    public int trwcnte = 0;//bhargavi
    public boolean measurealis = false;
    //Added By Prabal 6-feb
    public static HashMap<String, String> default_tab = new HashMap<String, String>(50);
    public static String def_key = "";

    public static String getDefault_tab(String key) {
        return default_tab.get(key);
    }

    public static void setDefault_tab(String key, String value) {
        def_key = key;
        default_tab.put(key, value);
    }
    //End Added By Prabal 6-feb

    public String getReportType() {
        return ReportType;
    }

    public void setReportType(String ReportType) {
        this.ReportType = ReportType;
    }

    public void refresh() {
        currentPage = 1;
        fromRow = 0;
        pagesPerSlide = defPagesPerSlide;
        //String sortType = null;
        //String sortColumn = null;
        //sortType = null;
        //displayColumns = null;
        dataTypes = null;
        dispTypes = null;
        //PbReturnObject ret = null;
        //PbReturnObject sortRet = null;
        ret = null;
        sortRet = null;
        selected.clear();
        alignments.clear();
//        signs.clear();
        links.clear();

        //clear sort columns and sortTypes
        sortColumns.clear();
        sortTypes = null;

        srchColumns.clear();
        srchCondition.clear();
        srchValue.clear();

        //
        fromColumn = 0;
        columnsPerSlide = defcolumnsPerSlide;
        //
    }

    public void refreshToOriginals() {
        originalColumns.clear();
        originalColumns = (ArrayList) originalColumnsBkp.clone();
        displayColumns.clear();
        displayColumns = (ArrayList) displayColumnsBkp.clone();
        displayLabels.clear();
        displayLabels = (ArrayList) displayLabelsBkp.clone();
        dataTypes.clear();
        dataTypes = (ArrayList) dataTypesBkp.clone();
        dispTypes.clear();
        dispTypes = (ArrayList) dispTypesBkp.clone();
        //added by santhosh.k on 18-02-2010
        fromColumn = 0;
        columnsPerSlide = defcolumnsPerSlide;
    }

    public int getCurrentPage() {
        if (this.currentPage < 1) {
            return 1;
        } else {
            return this.currentPage;
        }
    }

    public void setCurrentPage(int page) {
        this.currentPage = page;
    }

    public int getFromRow() {
        return this.fromRow;
    }

    public void setFromRow(int fRow) {
        this.fromRow = fRow;
    }

    public String getPagesPerSlide() {
        if (!this.pagesPerSlide.equalsIgnoreCase("0")) {
            return this.pagesPerSlide;
        } else {
            return this.defPagesPerSlide;
        }
    }

    public void setPagesPerSlide(String pages) {
        this.pagesPerSlide = pages;
    }

    public void setDefaultPagesPerSlide(String pages) {
        this.defPagesPerSlide = pages;
        this.pagesPerSlide = pages;
    }

    /*
     * public String getSortType() { return this.sortType; }
     *
     * public void setSortType(String sType) { this.sortType = sType;
    }
     */
    public ArrayList<String> getSortColumns() {
        return this.sortColumns;
    }

    public char[] getSortTypes() {
        return this.sortTypes;
    }

    public char[] getSortDataTypes() {
        return this.getColumnDataTypes(this.sortColumns);
    }

    public char[] getColumnDataTypes(ArrayList<String> columns) {
        if (columns == null || columns.isEmpty()) {
            return null;
        } else {
            char[] colDataTypes = new char[columns.size()];

            int i = 0;
            int j = 0;
            for (String sortCol : columns) {
                i = 0;
                for (String origCol : this.displayColumns) {
                    //j = 0;
                    if (origCol.equals(sortCol)) {
                        colDataTypes[j] = ((String) this.dataTypes.get(i)).charAt(0);
                        j++;
                        break;
                    }
                    i++;
                }
            }
            return colDataTypes;
        }
    }

    public boolean setSearchColumn(String columnId, String condition, String value, String format) {


        if (columnId == null) {
            srchColumns.clear();
            srchCondition.clear();
            srchValue.clear();
        } else {
            int indx = 0;
            Object valueConverted = value;
            if (this.displayColumns != null) {
                for (String displayCol : this.displayColumns) {
                    if (displayCol.equals(columnId)) {
                        if (dataTypes.get(indx).equals("C")) {
                            if (condition.equalsIgnoreCase("BT")) {
                                ArrayList<String> btwValues = new ArrayList<String>();
                                String[] values = value.split(",");
                                btwValues.add(values[0]);
                                btwValues.add(values[1]);
                                valueConverted = btwValues;
                            } else {
                                valueConverted = value;
                            }
                        } else if (dataTypes.get(indx).equals("N")) {
                            if (condition.equalsIgnoreCase("BT")) {
                                ArrayList<BigDecimal> btwValues = new ArrayList<BigDecimal>();
                                String[] values = value.split(",");
                                try {
                                    btwValues.add(new BigDecimal(values[0]));
                                    btwValues.add(new BigDecimal(values[1]));
                                } catch (NumberFormatException ex) {
//                                ProgenLog.log(ProgenLog.SEVERE, this, "setSearchColumn", "Invalid Search Input for Number");
                                    logger.error("Invalid Search Input for Number");
                                    return false;
                                }
                                valueConverted = btwValues;
                            } else {
                                try {
                                    if ((condition.equalsIgnoreCase("TOP") || (condition.equalsIgnoreCase("BTM")))) {

                                        if (value.endsWith("%")) {
                                            String newValue = value.substring(0, value.length() - 1);
                                            BigDecimal newValueConverted = new BigDecimal(newValue);
                                            valueConverted = value;

                                        } else {
                                            valueConverted = new BigDecimal(value);
                                        }
                                    } else {
                                        valueConverted = new BigDecimal(value);
                                    }
                                } catch (NumberFormatException ex) {
//                                ProgenLog.log(ProgenLog.SEVERE, this, "setSearchColumn", "Invalid Search Input for Number");
                                    logger.error("Invalid Search Input for Number", ex);
                                    return false;
                                }
                            }
                        } else if (dataTypes.get(indx).equals("D")) {
                            if (format != null && !format.equalsIgnoreCase("") && format.equalsIgnoreCase("frmTabFilter")) {
                                if (condition.equalsIgnoreCase("BT")) {
                                    ArrayList btwValues = new ArrayList();
                                    String[] values = value.split(",");
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date d = null;
                                    Date d1 = null;
                                    try {
                                        d = sdf.parse(values[0]);
                                        d1 = sdf.parse(values[1]);
                                    } catch (ParseException ex) {
//                                    ProgenLog.log(ProgenLog.SEVERE, this, "setSearchColumn", "Invalid Search Input for Date");
                                        logger.error("Invalid Search Input for Date:", ex);
                                        return false;
                                    }
                                    btwValues.add(sdf2.format(d));
                                    btwValues.add(sdf2.format(d1));
                                    valueConverted = btwValues;
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date d = null;
                                    try {
                                        d = sdf.parse(value);
                                    } catch (ParseException ex) {
//                                    ProgenLog.log(ProgenLog.SEVERE, this, "setSearchColumn", "Invalid Search Input for Date");
                                        logger.error("Invalid Search Input for Date:", ex);
                                        return false;
                                    }

                                    valueConverted = sdf2.format(d);
                                }
                            } else {
                                valueConverted = value;
                            }
                        }
                        break;
                    }
                    indx++;
                }
            }
            if (srchColumns.contains(columnId)) {
                indx = srchColumns.indexOf(columnId);
                srchCondition.set(indx, condition);
                srchValue.set(indx, valueConverted);
            } else {
                srchColumns.add(columnId);
                srchCondition.add(condition);
                srchValue.add(valueConverted);
            }
        }
        return true;
    }

    public boolean setSubToTalSearchColumn(String columnId, String condition, String value, String format) {


        if (columnId == null) {
            getSubTotalSrchColumns().clear();
            getSubTotalSrchCondition().clear();
            getSubTotalSrchValue().clear();

        } else {
            int indx = 0;
            Object valueConverted = value;
            if (this.displayColumns != null) {
                for (String displayCol : this.displayColumns) {
                    if (displayCol.equals(columnId)) {
                        if (dataTypes.get(indx).equals("C")) {
                            if (condition.equalsIgnoreCase("BT")) {
                                ArrayList<String> btwValues = new ArrayList<String>();
                                String[] values = value.split(",");
                                btwValues.add(values[0]);
                                btwValues.add(values[1]);
                                valueConverted = btwValues;
                            } else {
                                valueConverted = value;
                            }
                        } else if (dataTypes.get(indx).equals("N")) {
                            if (condition.equalsIgnoreCase("BT")) {
                                ArrayList<BigDecimal> btwValues = new ArrayList<BigDecimal>();
                                String[] values = value.split(",");
                                try {
                                    btwValues.add(new BigDecimal(values[0]));
                                    btwValues.add(new BigDecimal(values[1]));
                                } catch (NumberFormatException ex) {
//                                ProgenLog.log(ProgenLog.SEVERE, this, "setSubToTalSearchColumn", "Invalid Search Input for Number");
                                    logger.error("Invalid Search Input for Number", ex);
                                    return false;
                                }
                                valueConverted = btwValues;
                            } else {
                                try {
                                    if ((condition.equalsIgnoreCase("TOP") || (condition.equalsIgnoreCase("BTM")))) {

                                        if (value.endsWith("%")) {
                                            String newValue = value.substring(0, value.length() - 1);
                                            BigDecimal newValueConverted = new BigDecimal(newValue);
                                            valueConverted = value;

                                        } else {
                                            valueConverted = new BigDecimal(value);
                                        }
                                    } else {
                                        valueConverted = new BigDecimal(value);
                                    }
                                } catch (NumberFormatException ex) {
//                                ProgenLog.log(ProgenLog.SEVERE, this, "setSubToTalSearchColumn", "Invalid Search Input for Number");
                                    logger.error("Invalid Search Input for Number:", ex);
                                    return false;
                                }
                            }
                        } else if (dataTypes.get(indx).equals("D")) {
                            if (format != null && !format.equalsIgnoreCase("") && format.equalsIgnoreCase("frmTabFilter")) {
                                if (condition.equalsIgnoreCase("BT")) {
                                    ArrayList btwValues = new ArrayList();
                                    String[] values = value.split(",");
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date d = null;
                                    Date d1 = null;
                                    try {
                                        d = sdf.parse(values[0]);
                                        d1 = sdf.parse(values[1]);
                                    } catch (ParseException ex) {
//                                    ProgenLog.log(ProgenLog.SEVERE, this, "setSearchColumn", "Invalid Search Input for Date");
                                        logger.error("Invalid Search Input for Date", ex);
                                        return false;
                                    }
                                    btwValues.add(sdf2.format(d));
                                    btwValues.add(sdf2.format(d1));
                                    valueConverted = btwValues;
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date d = null;
                                    try {
                                        d = sdf.parse(value);
                                    } catch (ParseException ex) {
//                                    ProgenLog.log(ProgenLog.SEVERE, this, "setSearchColumn", "Invalid Search Input for Date");
                                        logger.error("Invalid Search Input for Date", ex);
                                        return false;
                                    }

                                    valueConverted = sdf2.format(d);
                                }
                            } else {
                                valueConverted = value;
                            }
                        }
                        break;
                    }
                    indx++;
                }
            }
            if (getSubTotalSrchColumns().contains(columnId)) {
                indx = getSubTotalSrchColumns().indexOf(columnId);
                getSubTotalSrchCondition().set(indx, condition);
                getSubTotalSrchValue().set(indx, valueConverted.toString());
            } else {
                getSubTotalSrchColumns().add(columnId);
                getSubTotalSrchCondition().add(condition);
                getSubTotalSrchValue().add(valueConverted.toString());
            }
        }
        return true;
    }

    public boolean ClearSearchFilterOnSubTotal(String Column) {
        if (getSubTotalSrchColumns().contains(Column)) {
            int indx = getSubTotalSrchColumns().indexOf(Column);
            getSubTotalSrchColumns().remove(indx);
            getSubTotalSrchCondition().remove(indx);
            getSubTotalSrchValue().remove(indx);

        }
        if (getSubTotalSrchColumns().isEmpty()) {
            setIsSubTotalSearchFilterApplied(false);
        }
        return true;
    }

    public ArrayList<String> getSearchColumns() {
        return this.srchColumns;
    }

    public ArrayList<String> getSearchConditions() {
        return this.srchCondition;
    }

    public ArrayList<Object> getSearchValues() {
        return this.srchValue;
    }

    public void setSortColumn(String sortColumn, SortOrder sortOrder) {
        SortColumn column;
        try {
            column = Iterables.find(sortColumnLst, SortColumn.getColumnPredicate(sortColumn));
            column.setSortOrder(sortOrder);
        } catch (NoSuchElementException nse) {
            column = new SortColumn(sortColumn, sortOrder);
            sortColumnLst.add(column);
        }

    }

    public void resetSortColumn() {
        this.sortColumnLst.clear();
    }

    public Collection<SortColumn> getSortColumnList() {
        return this.sortColumnLst;
    }

    /**
     * Method for Setting Sort column for Progen Table Mutliple columns can be
     * sorted Dimensions are always sorted first then only Measures are sorted,
     * this will give the behavior of Group By An array list sortColumns is
     * maintained to capture the Sort Columns and the Char[] sortTypes will
     * denoted how each column is sorted (Asc or Desc)
     *
     * @param sCol - Element Id of the Measure
     * @param sType - Sort Type 0 or 1 (A/D)
     */
    public void setSortColumn(String sCol, String sType) {
        if (sCol == null) {
            this.sortColumns.clear();
            this.sortTypes = null;
        } else {
            //start of sandeep code on 30/10/214 for clear kpi sort coloums
            if (this.isIskpidasboard()) {
                this.sortColumns.clear();
                this.sortTypes = null;
            }
            //end of sandeep code
            if (sortColumns.contains(sCol)) {
                int indx = sortColumns.indexOf(sCol);
                this.sortTypes[indx] = sType.charAt(0);
            } else {
                this.sortColumns.add(sCol);
                char[] curSortTypes = this.sortTypes;
                this.sortTypes = new char[this.sortColumns.size()];

                if (curSortTypes != null) {
                    int i;
                    for (i = 0; i < curSortTypes.length; i++) {
                        this.sortTypes[i] = curSortTypes[i];
                    }

                    this.sortTypes[i] = sType.charAt(0);
                } else {
                    this.sortTypes[0] = sType.charAt(0);
                }
            }

            //Keep in original column order
            if (this.sortColumns.size() > 1) {
                ArrayList<String> sortColOrdrd = new ArrayList<String>(this.sortColumns.size());
                char[] sortTypOrdrd = new char[this.sortTypes.length];
                int indx;
                int j = 0;

                for (int i = 0; i < this.originalColumns.size(); i++) {
                    if (sortColumns.contains(this.originalColumns.get(i))) {
                        indx = sortColumns.indexOf(this.originalColumns.get(i));
                        sortColOrdrd.add(sortColumns.get(indx));
                        sortTypOrdrd[j] = this.sortTypes[indx];
                        j++;
                    }
                }
                sortColumns.clear();

                sortColumns = sortColOrdrd;
                sortTypes = sortTypOrdrd;
            } //added by bhargavi for sorting/table filter works simuntaneously
            else {
                ArrayList<String> sortColOrdrd = new ArrayList<String>(this.sortColumns.size());
                char[] sortTypOrdrd = new char[this.sortTypes.length];
                int indx;
                int j = 0;

                for (int i = 0; i < this.originalColumns.size(); i++) {
                    if (sortColumns.contains(this.originalColumns.get(i))) {
                        indx = sortColumns.indexOf(this.originalColumns.get(i));
                        sortColOrdrd.add(sortColumns.get(indx));
                        sortTypOrdrd[j] = this.sortTypes[indx];
                        j++;
                    }
                }
                sortColumns.clear();

                sortColumns = sortColOrdrd;
                sortTypes = sortTypOrdrd;
            }
        }
    }

    /**
     * Method for Setting Sort column for Top 5 Bottom 5 For Top 5 Measure is
     * sorted in Descending Order For Bottom 5 Measure is sorted in Ascending
     * Order
     *
     * @param sCol - Element Id of the Measure
     * @param sType - Sort Type 0 or 1 (A/D)
     */
    public void setSortColumnTopBottom(String sCol, String sType) {
        if (this.sortColumns == null) {
            this.sortColumns = new ArrayList<String>();
        }

        int removeIndex;
        int i;

        //remove all Numerical columns in Sort
        //If user chooses Top 5 on meas1 then immediately Top 5 on another Measure
        //then we will clear the previous measure from sort list
        if (sortColumns.size() > 0) {
            char[] sortDataTypes = this.getSortDataTypes();
            for (i = sortDataTypes.length - 1; i >= 0; i--) {
                if (sortDataTypes[i] != 'N') //loop through till we find a C data type which is dimension
                {
                    break;
                }
            }
            removeIndex = i;

            //remove all previous measures from the sort
            if (removeIndex < sortDataTypes.length - 1) {
                Set<String> sortRemCols = new HashSet<String>();
                for (int j = removeIndex + 1; j < sortDataTypes.length; j++) {
                    //this.sortColumns.remove(j); //stupid logic index decreases after each remove
                    sortRemCols.add(this.sortColumns.get(j));
                    this.sortTypes[j] = 'x';
                }
                if (removeIndex == -1) {
                    this.sortTypes = null;
                    //remove unwanted columns fro sortColumns
                    for (String sortCol : sortRemCols) {
                        this.sortColumns.remove(sortCol);
                    }

                } else {
                    char[] oldSortTypes = this.sortTypes;
                    this.sortTypes = new char[removeIndex + 1];
                    i = 0;
                    for (int k = 0; k < oldSortTypes.length; k++) {
                        if (oldSortTypes[k] != 'x') {
                            this.sortTypes[i++] = oldSortTypes[k];
                        }
                    }

                    //remove unwanted columns fro sortColumns
                    for (String sortCol : sortRemCols) {
                        this.sortColumns.remove(sortCol);
                    }

                }
            }
        }

        //start constructing new Sort columns and Sort Types
        //if column exists already then just update the sort Type as asc/dsc
        if (sortColumns.contains(sCol)) {
            int indx = sortColumns.indexOf(sCol);
            this.sortTypes[indx] = sType.charAt(0);
        } //column doesn't exist in the sort list so add the column to sort list
        //capture the sort type for the column
        else {
            /*
             * if(this.viewByElementIds.size() >= 2 && !this.isReportCrosstab &&
             * netTotalReq){ ArrayList hSortTypes=new ArrayList(); if(sortTypes
             * != null && sortTypes.length>0){ for(int
             * temp=0;temp<sortTypes.length;temp++){
             * hSortTypes.add(sortTypes[temp]); } } int count=0; for(int
             * j=0;j<(this.viewByElementIds.size()-1);j++){ String
             * viewbyId="A_"+viewByElementIds.get(j).toString();
             * if(!sortColumns.contains(viewbyId)){
             * this.sortColumns.add(viewbyId); if(explicitSortColumns != null &&
             * !explicitSortColumns.contains(viewbyId)){
             * this.explicitSortColumns.add(viewbyId); hSortTypes.add('0'); }
             *
             * }
             * }
             *
             * this.sortColumns.add(sCol); this.sortTypes=new
             * char[sortColumns.size()]; this.explicitSortTypes=new
             * char[hSortTypes.size()]; int k=0;
             * for(k=0;k<hSortTypes.size();k++){
             * this.sortTypes[k]=hSortTypes.get(k).toString().charAt(0);
             * this.explicitSortTypes[k]=hSortTypes.get(k).toString().charAt(0);
             * } this.sortTypes[k]=sType.charAt(0);
             *
             * }else{
             */
            if (!netTotalReq) {
                if (this.getExplicitSortColumns() != null && !this.explicitSortColumns.isEmpty()) {
                    for (int l = 0; l < this.getExplicitSortColumns().size(); l++) {
                        if (this.sortColumns.contains(getExplicitSortColumns().get(l))) {
                            this.sortTypes[sortColumns.indexOf(getExplicitSortColumns().get(l))] = 'x';
                            this.sortColumns.remove(getExplicitSortColumns().get(l));
                        }
                    }
                    char[] tempSortTypes = new char[sortColumns.size()];
                    int count = 0;
                    for (int temp1 = 0; temp1 < sortTypes.length; temp1++) {
                        if (sortTypes[temp1] == 'x') {
                            continue;
                        } else {
                            tempSortTypes[count] = sortTypes[temp1];
                            count++;
                        }
                    }
                    sortTypes = tempSortTypes;
                }
            }
            this.sortColumns.add(sCol);
            char[] curSortTypes = this.sortTypes;
            this.sortTypes = new char[this.sortColumns.size()];

            if (curSortTypes != null) {
                for (i = 0; i < curSortTypes.length; i++) {
                    this.sortTypes[i] = curSortTypes[i];
                }

                this.sortTypes[i] = sType.charAt(0);
            } else {
                this.sortTypes[0] = sType.charAt(0);
            }
//                }
        }

        //Keep in original column order
        //We will always have Dimension first then only Measure in the order of sorting
        if (this.sortColumns.size() > 1) {
            ArrayList<String> sortColOrdrd = new ArrayList<String>(this.sortColumns.size());
            char[] sortTypOrdrd = new char[this.sortTypes.length];
            int indx;
            int j = 0;

            for (i = 0; i < this.originalColumns.size(); i++) {
                if (sortColumns.contains(this.originalColumns.get(i))) {
                    indx = sortColumns.indexOf(this.originalColumns.get(i));
                    sortColOrdrd.add(sortColumns.get(indx));
                    sortTypOrdrd[j] = this.sortTypes[indx];
                    j++;
                }
            }
            sortColumns.clear();
            sortColumns = sortColOrdrd;
            sortTypes = sortTypOrdrd;
        }
    }

    public void setSubTotalSortColumnTopBottom(String sCol, String sType) {
        if (sCol == null) {
            this.explicitSortColumns.clear();
            this.explicitSortTypes = null;
        } else if (explicitSortColumns.contains(sCol)) {
            int indx = explicitSortColumns.indexOf(sCol);
            //code added by Amar
            explicitSortColumns.remove(indx);
            explicitSortColumns.add(sCol);
            ArrayList<Character> cList = new ArrayList<Character>();
            for (char c : explicitSortTypes) {
                cList.add(c);
            }
            cList.remove(indx);
            cList.add(sType.charAt(0));
            int i = 0;
            for (Character ch : cList) {
                explicitSortTypes[i] = ch;
                i++;
            }
            // end of code by Amar
        } else {
            int flag = 0;
            this.explicitSortColumns.clear();
            ArrayList hSortTypes = new ArrayList();
            for (int j = 0; j < (this.viewByElementIds.size() - 1); j++) {
                String viewbyId = "A_" + viewByElementIds.get(j).toString();
                if (explicitSortColumns != null && !explicitSortColumns.contains(viewbyId)) {
                    this.explicitSortColumns.add(viewbyId);
                    hSortTypes.add('0');
                }
            }
            if (!this.explicitSortColumns.contains(sCol)) {
                this.explicitSortColumns.add(sCol);
                flag++;
            }
            //this.explicitSortColumns.add(sCol);
            this.explicitSortTypes = new char[explicitSortColumns.size()];
            int k = 0;
            for (k = 0; k < hSortTypes.size(); k++) {
                this.explicitSortTypes[k] = hSortTypes.get(k).toString().charAt(0);
            }
            if (flag == 1) {
                this.explicitSortTypes[k] = sType.charAt(0);

            }
            //  this.explicitSortTypes[k]=sType.charAt(0);
        }
    }

    public ArrayList<String> getDisplayColumns() {
        return this.displayColumns;
    }

    public void setDisplayColumns(ArrayList<String> disColumns) {
        this.displayColumns = (ArrayList<String>) disColumns.clone();

        if (this.displayColumnsBkp == null) {
            this.displayColumnsBkp = (ArrayList<String>) disColumns.clone();

        }
    }

    public ArrayList getDisplayLabels() {
        return this.displayLabels;
    }

    public ArrayList getDisplayLabelskpi() {
        return this.displayLabelskpi;
    }

    public void setDisplayLabels(ArrayList disLabels) {
        this.displayLabels = (ArrayList) disLabels.clone();
        if (this.displayLabelsBkp == null) {
            this.displayLabelsBkp = (ArrayList) disLabels.clone();
        }
    }

    public void setDisplayLabelskpi(ArrayList disLabels) {
        this.displayLabelskpi = (ArrayList) disLabels.clone();

    }

    public ArrayList<String> getOriginalColumns() {
        return this.originalColumns;
    }

    public void setOriginalColumns(ArrayList<String> oriCols) {
        this.originalColumns = (ArrayList<String>) oriCols.clone();
        if (this.originalColumnsBkp == null) {
            this.originalColumnsBkp = (ArrayList<String>) oriCols.clone();
        }
    }

    public ArrayList getDataTypes() {
        return this.dataTypes;
    }

    public void setDataTypes(ArrayList dataTypes) {
        this.dataTypes = (ArrayList) dataTypes.clone();
        if (this.dataTypesBkp == null) {
            this.dataTypesBkp = (ArrayList) dataTypes.clone();
        }
    }

    public ArrayList getDisplayTypes() {
        return this.dispTypes;
    }

    public void setDisplayTypes(ArrayList disTypes) {
        this.dispTypes = (ArrayList) disTypes.clone();
        if (this.dispTypesBkp == null) {
            this.dispTypesBkp = (ArrayList) disTypes.clone();
        }
    }

    public boolean getSearchReq() {
        return this.srchReq;
    }

    public void setSearchReq(boolean srchreq) {
        this.srchReq = srchreq;
    }

    public ProgenDataSet getRetObj() {
        return this.ret;
    }

    public void setRetObj(ProgenDataSet obj) {
        this.ret = obj;
    }

    public PbReturnObject getKpiRetObj() {
        return kpiRetObj;
    }

    public void setKpiRetObj(PbReturnObject kpiRetObj) {
        this.kpiRetObj = kpiRetObj;
    }

    public PbReturnObject getTimeSeriesRetObj() {
        return timeSeriesRetObj;
    }

    public void setTimeSeriesRetObj(PbReturnObject timeSeriesRetObj) {
        this.timeSeriesRetObj = timeSeriesRetObj;
    }

    public PbReturnObject getPriorRetObj() {
        return priorRetObj;
    }

    public void setPriorRetObj(PbReturnObject priorRetObj) {
        this.priorRetObj = priorRetObj;
    }

    public PbReturnObject getSortRetObj() {
        return this.sortRet;
    }

    public void setSortRetObj(PbReturnObject obj) {
        this.sortRet = obj;
    }

    public void setSelected(ArrayList vect) {
        selected = vect;
    }

    public ArrayList getSelected() {
        return this.selected;
    }

    public void setAlignments(ArrayList<String> align) {
        this.alignments = align;
    }

    public ArrayList<String> getAlignments() {
        return this.alignments;
    }

//    public void setSigns(ArrayList signs) {
//        this.signs = signs;
//    }
//
//    public ArrayList getSigns() {
//        return this.signs;
//    }
    public void setNetTotalReq(boolean req) {
        this.netTotalReq = req;
    }

    public boolean getNetTotalReq() {
        return this.netTotalReq;
    }

    public void setGrandTotalReq(boolean req) {
        this.grdTotalReq = req;
    }

    public boolean getGrandTotalReq() {
        return this.grdTotalReq;
    }

    //added by bhargavi
    public void setGrandTotalReqForkpi(boolean req) {
        tableProperties.grdTotalReqkpi = req;
    }

    public boolean getGrandTotalReqForkpi() {
        return tableProperties.grdTotalReqkpi;
    }

    public void setNetTotalReqForkpi(boolean req) {
        tableProperties.netTotalReqkpi = req;
    }

    public boolean getNetTotalReqForkpi() {
        return tableProperties.netTotalReqkpi;
    }
//end of code by bhargavi

    public void setAvgTotalReq(boolean req) {
        this.avgTotalReq = req;
    }

    public boolean getAvgTotalReq() {
        return this.avgTotalReq;
    }

    public void setCheckBoxValueCol(String cbxCol) {
        this.cbxColumn = cbxCol;
    }

    public String getCheckBoxValueCol() {
        return this.cbxColumn;
    }

    public void setLinks(ArrayList links) {
        this.links = links;
    }

    public ArrayList getLinks() {
        return this.links;
    }

    public void setDisplayedSet(PbReturnObject ret) {
        this.displayedSet = ret;
    }

    public PbReturnObject getDisplayedSetRetObj() {
        return displayedSet;
    }

    public ArrayList getDisplayedSet() {
        ArrayList alist = new ArrayList();
        HashMap hmap = null;
        int graphDisplayedRows = 0;
//        if (displayedSet != null) {
        graphDisplayedRows = ret.getViewSequence().size();
//            graphDisplayedRows = displayedSet.getRowCount();
//            if (displayedSet.getRowCount() < 10) {
//            graphDisplayedRows = displayedSet.getRowCount();
//        } else {
//            graphDisplayedRows = 10;
//        }
        for (int index = 0; index < graphDisplayedRows; index++) {
            hmap = new HashMap();
            for (int j = 0; j < originalColumns.size(); j++) {
                hmap.put(String.valueOf(originalColumns.get(j)), ret.getFieldValueString(ret.getViewSequence().get(index), String.valueOf(originalColumns.get(j))));
            }
            alist.add(hmap);
        }
//        }
        return alist;
    }

    public ArrayList getALlRecordsSet() {
        ArrayList alist = new ArrayList();
        HashMap hmap = null;
        int graphDisplayedRows = getRetObj().getRowCount();

        for (int index = 0; index < graphDisplayedRows; index++) {
            hmap = new HashMap();
            for (int j = 0; j < originalColumns.size(); j++) {
                hmap.put(String.valueOf(originalColumns.get(j)), getRetObj().getFieldValueString(index, String.valueOf(originalColumns.get(j))));
            }
            alist.add(hmap);
        }
        return alist;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public void setSessionContext(HttpSession session, Container container) {
        if (this.tableId != null) {
            HashMap map = new HashMap();
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
            }
            if (map.get(this.tableId) != null) {
                map.remove(this.tableId);
                map.put(this.tableId, container);
            } else {
                map.put(this.tableId, container);
            }
            session.setAttribute("PROGENTABLES", map);
        }
    }

    public ArrayList getTableRemainingColumns() {
        return tableRemainingColumns;
    }

    public void setTableRemainingColumns(ArrayList tableRemainingColumns) {
        this.tableRemainingColumns = tableRemainingColumns;
    }

    public ArrayList getTableRemainingDisplayColumns() {
        return tableRemainingDisplayColumns;
    }

    public void setTableRemainingDisplayColumns(ArrayList tableRemainingDisplayColumns) {
        this.tableRemainingDisplayColumns = tableRemainingDisplayColumns;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public boolean isHideTable() {
        return hideTable;
    }

    public void setHideTable(boolean hideTable) {
        this.hideTable = hideTable;
    }

    public String getFrameHgt1() {
        return frameHgt1;
    }

    public void setFrameHgt1(String frameHgt1) {
        this.frameHgt1 = frameHgt1;
    }

    public String getFrameHgt2() {
        return frameHgt2;
    }

    public void setFrameHgt2(String frameHgt2) {
        this.frameHgt2 = frameHgt2;
    }

    public HashMap getColumnsVisibility() {
        return columnsVisibility;
    }

    public void setColumnsVisibility(HashMap columnsVisibility) {
        this.columnsVisibility = columnsVisibility;
    }

    public HashMap getSwapGraphAnalysis() {
        return swapGraphAnalysis;
    }

    public void setSwapGraphAnalysis(HashMap swapGraphAnalysis) {
        this.swapGraphAnalysis = swapGraphAnalysis;
    }

    public String getReportMode() {
        return reportMode;
    }

    public void setReportMode(String reportMode) {
        this.reportMode = reportMode;
    }

    public String[] getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(String[] imgPaths) {
        this.imgPaths = imgPaths;
    }

    public String getReportName() {
        return this.reportCollect.reportName;
    }

    public void setReportName(String reportName) {
        this.reportCollect.reportName = reportName;
    }

    public String getReportId() {
        return this.reportCollect.reportId;
    }

    public void setReportId(String reportId) {
        this.reportCollect.reportId = reportId;
    }

    public ArrayList getViewByColNames() {
        return viewByColNames;
    }

    public void setViewByColNames(ArrayList viewByColNames) {
        this.viewByColNames = viewByColNames;
    }

    public ArrayList getViewByElementIds() {
        return viewByElementIds;
    }

    public void setViewByElementIds(ArrayList viewByElementIds) {
        this.viewByElementIds = viewByElementIds;
    }

    public HashMap[] getGraphMapDetails() {
        return graphMapDetails;
    }

    public void setGraphMapDetails(HashMap[] graphMapDetails) {
        this.graphMapDetails = graphMapDetails;
    }

    public String getGraphTableId() {
        return graphTableId;
    }

    public void setGraphTableId(String graphTableId) {
        this.graphTableId = graphTableId;
    }

    public int getViewByCount() {
        return viewByCount;
    }

    public void setViewByCount(int viewByCount) {
        this.viewByCount = viewByCount;
    }

    public ArrayList getColumnViewByElementIds() {
        return columnViewByElementIds;
    }

    public void setColumnViewByElementIds(ArrayList columnViewByElementIds) {
        this.columnViewByElementIds = columnViewByElementIds;
    }

    public String getColumnViewByCount() {
        return columnViewByCount;
    }

    public void setColumnViewByCount(String columnViewByCount) {
        this.columnViewByCount = columnViewByCount;
    }

    public PbReportCollection getReportCollect() {
        return reportCollect;
    }

    public void setReportCollect(PbReportCollection reportCollect) {
        this.reportCollect = reportCollect;
    }

    public String getReportDesc() {
        return this.reportCollect.reportDesc;
    }

    public void setReportDesc(String reportDesc) {
        this.reportCollect.reportDesc = reportDesc;
    }

    public HashMap getParametersHashMap() {
        return ParametersHashMap;
    }

    public void setParametersHashMap(HashMap ParametersHashMap) {
        this.ParametersHashMap = ParametersHashMap;
    }

    public HashMap getTableHashMap() {
        return TableHashMap;
    }

    public void setTableHashMap(HashMap TableHashMap) {
        this.TableHashMap = TableHashMap;
    }

    public HashMap getGraphHashMap() {
        return GraphHashMap;
    }

    public void setGraphHashMap(HashMap GraphHashMap) {
        this.GraphHashMap = GraphHashMap;
    }

    public HashMap getReportHashMap() {
        return ReportHashMap;
    }

    public void setReportHashMap(HashMap ReportHashMap) {
        this.ReportHashMap = ReportHashMap;
    }

    public String[] getKPIS() {
        return KPIS;
    }

    public void setKPIS(String[] KPIS) {
        this.KPIS = KPIS;
    }

//    public HashMap getParamDefaultValuesHashMap() {
//        return paramDefaultValuesHashMap;
//    }
//
//    public void setParamDefaultValuesHashMap(HashMap paramDefaultValuesHashMap) {
//        this.paramDefaultValuesHashMap = paramDefaultValuesHashMap;
//    }
    public void resetBackUpVariables() {
        this.originalColumnsBkp = null;
        this.displayColumnsBkp = null;
        this.displayLabelsBkp = null;
        this.dataTypesBkp = null;
        this.dispTypesBkp = null;
    }

    public HashMap getCrossTabGraphHashMap() {
        return CrossTabGraphHashMap;
    }

    public void setCrossTabGraphHashMap(HashMap CrossTabGraphHashMap) {
        this.CrossTabGraphHashMap = CrossTabGraphHashMap;
    }
    /*
     * public pbDashboardCollection getDashboardCollection() { return
     * dashboardCollection; }
     *
     * public void setDashboardCollection(pbDashboardCollection
     * dashboardCollection) { this.dashboardCollection = dashboardCollection; }
     */

    public void setDisplayedSetWithGT(PbReturnObject displayedSetWithGT) {
        this.displayedSetWithGT = displayedSetWithGT;
    }

    public PbReturnObject getDisplayedSetRetObjWithGT() {
        return this.displayedSetWithGT;
    }

    public ArrayList getDisplayedSetWithGT() {
        ArrayList alist = new ArrayList();
        HashMap hmap = null;
        int graphDisplayedRows = 0;
        if (displayedSetWithGT != null) {
            if (displayedSetWithGT.getRowCount() < 10) {
                graphDisplayedRows = displayedSetWithGT.getRowCount();
            } else {
                graphDisplayedRows = 10;
            }
            for (int index = 0; index < graphDisplayedRows; index++) {
                hmap = new HashMap();
                for (int j = 0; j < originalColumns.size(); j++) {
                    hmap.put(String.valueOf(originalColumns.get(j)), displayedSetWithGT.getFieldValueString(index, String.valueOf(originalColumns.get(j))));
                }
                alist.add(hmap);
            }
        }

        return alist;
    }

    public boolean getCatMinValueReq() {
        return catMinValueReq;
    }

    public void setCatMinValueReq(boolean catMinValueReq) {
        this.catMinValueReq = catMinValueReq;
    }

    public boolean getCatMaxValueReq() {
        return catMaxValueReq;
    }

    public void setCatMaxValueReq(boolean catMaxValueReq) {
        this.catMaxValueReq = catMaxValueReq;
    }

    public boolean getOverAllMinValueReq() {
        return overAllMinValueReq;
    }

    public void setOverAllMinValueReq(boolean overAllMinValueReq) {
        this.overAllMinValueReq = overAllMinValueReq;
    }

    public boolean getOverAllMaxValueReq() {
        return overAllMaxValueReq;
    }

    public void setOverAllMaxValueReq(boolean overAllMaxValueReq) {
        this.overAllMaxValueReq = overAllMaxValueReq;
    }

    public String getTableSymbols() {
        return tableSymbols;
    }

    public void setTableSymbols(String tableSymbols) {
        this.tableSymbols = tableSymbols;
    }

    /*
     * public ArrayList getShowColumnTotals() { return showColumnTotals; }
     *
     * public void setShowColumnTotals(ArrayList showColumnTotals) {
     * this.showColumnTotals = showColumnTotals; }
     *
     * public ArrayList getShowColumnSubTotals() { return showColumnSubTotals; }
     *
     * public void setShowColumnSubTotals(ArrayList showColumnSubTotals) {
     * this.showColumnSubTotals = showColumnSubTotals; }
     *
     * public ArrayList getShowColumnAvg() { return showColumnAvg; }
     *
     * public void setShowColumnAvg(ArrayList showColumnAvg) {
     * this.showColumnAvg = showColumnAvg; }
     *
     * public ArrayList getShowColumnMax() { return showColumnMax; }
     *
     * public void setShowColumnMax(ArrayList showColumnMax) {
     * this.showColumnMax = showColumnMax; }
     *
     * public ArrayList getShowColumnMin() { return showColumnMin; }
     *
     * public void setShowColumnMin(ArrayList showColumnMin) {
     * this.showColumnMin = showColumnMin; }
     *
     * public ArrayList getShowColumnCatMax() { return showColumnCatMax; }
     *
     * public void setShowColumnCatMax(ArrayList showColumnCatMax) {
     * this.showColumnCatMax = showColumnCatMax; }
     *
     * public ArrayList getShowColumnCatMin() { return showColumnCatMin; }
     *
     * public void setShowColumnCatMin(ArrayList showColumnCatMin) {
     * this.showColumnCatMin = showColumnCatMin; }
     *
     * public ArrayList getShowColumnSymbols() { return showColumnSymbols; }
     *
     * public void setShowColumnSymbols(ArrayList showColumnSymbols) {
     * this.showColumnSymbols = showColumnSymbols; }
     */
    public HashMap getColumnProperties() {
        return this.columnProperties;
    }

    public void setColumnProperties(HashMap columnProperties) {
        this.columnProperties = columnProperties;
    }

    public boolean isIndicatorEnabled(String meas) {
        String indicator = reportCollect.indicatorMeasures.get(meas);
        if ("Y".equalsIgnoreCase(indicator)) {
            return true;
        } else {
            return false;
        }
    }

    public String getFavName() {
        return FavName;
    }

    public void setFavName(String FavName) {
        this.FavName = FavName;
    }

    public String getFavDesc() {
        return FavDesc;
    }

    public void setFavDesc(String FavDesc) {
        this.FavDesc = FavDesc;
    }

    public HashMap getDashboardHashMap() {
        return DashboardHashMap;
    }

    public void setDashboardHashMap(HashMap DashboardHashMap) {
        this.DashboardHashMap = DashboardHashMap;
    }

    public String getDbrdDesc() {
        return DbrdDesc;
    }

    public void setDbrdDesc(String DbrdDesc) {
        this.DbrdDesc = DbrdDesc;
    }

    public String getDbrdName() {
        return DbrdName;
    }

    public void setDbrdName(String DbrdName) {
        this.DbrdName = DbrdName;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public HashMap getKpisMap() {
        return kpisMap;
    }

    public void setKpisMap(HashMap kpisMap) {
        this.kpisMap = kpisMap;
    }

    public HashMap getKpiIdNamesMap() {
        return kpiIdNamesMap;
    }

    public void setKpiIdNamesMap(HashMap kpiIdNamesMap) {
        this.kpiIdNamesMap = kpiIdNamesMap;
    }

    public HashMap getDivDetails() {
        return divDetails;
    }

    public void setDivDetails(HashMap divDetails) {
        this.divDetails = divDetails;
    }

    public HashMap getDivGraphs() {
        return divGraphs;
    }

    public void setDivGraphs(HashMap divGraphs) {
        this.divGraphs = divGraphs;
    }

    public HashMap getGrpColsbyDivMap() {
        return grpColsbyDivMap;
    }

    public void setGrpColsbyDivMap(HashMap grpColsbyDivMap) {
        this.grpColsbyDivMap = grpColsbyDivMap;
    }

    public HashMap getNewDbrdGraph() {
        return newDbrdGraph;
    }

    public void setNewDbrdGraph(HashMap newDbrdGraph) {
        this.newDbrdGraph = newDbrdGraph;
    }

    public HashMap getKpiGrps() {
        return kpiGrps;
    }

    public void setKpiGrps(HashMap kpiGrps) {
        this.kpiGrps = kpiGrps;
    }

    public ArrayList<String> getColumnDrillLinks() {
        return columnDrillLinks;
    }

    public void setColumnDrillLinks(ArrayList<String> columnDrillLinks) {
        this.columnDrillLinks = columnDrillLinks;
    }

    public ArrayList getTableMeasure() {
        return tableMeasure;
    }

    public void setTableMeasure(ArrayList tableMeasure) {
        this.tableMeasure = tableMeasure;
    }

    public ArrayList getTableMeasureNames() {
        return tableMeasureNames;
    }

    public void setTableMeasureNames(ArrayList tableMeasureNames) {
        this.tableMeasureNames = tableMeasureNames;
    }

    public String getTimeLevel() {
        return timeLevel;
    }

    public void setTimeLevel(String timeLevel) {
        this.timeLevel = timeLevel;
    }

    public boolean isTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(boolean timeSeries) {
        this.timeSeries = timeSeries;
    }

    public HashMap getRepReqParamsHashMap() {
        return repReqParamsHashMap;
    }

    public void setRepReqParamsHashMap(HashMap repReqParamsHashMap) {
        this.repReqParamsHashMap = repReqParamsHashMap;
    }

    public String getDefaultSortedColumn() {
        return defaultSortedColumn;
    }

    public void setDefaultSortedColumn(String defaultSortedColumn) {
        this.defaultSortedColumn = defaultSortedColumn;
    }

    public ArrayList getTimeDetailsArray() {
        return timeDetailsArray;
    }

    public void setTimeDetailsArray(ArrayList timeDetailsArray) {
        this.timeDetailsArray = timeDetailsArray;
    }

    public HashMap getKpiCommentHashMap() {
        return kpiCommentHashMap;
    }

    public void setKpiCommentHashMap(HashMap kpiCommentHashMap) {
        this.kpiCommentHashMap = kpiCommentHashMap;
    }

    public String getColumnViewByName() {
        return columnViewByName;
    }

    public void setColumnViewByName(String columnViewByName) {
        this.columnViewByName = columnViewByName;
    }

    //added by uday
    /**
     * @return the scenarioId
     */
    public String getScenarioId() {
        return scenarioId;
    }

    /**
     * @param scenarioId the scenarioId to set
     */
    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * @return the scenarioHashMap
     */
    public HashMap getScenarioHashMap() {
        return scenarioHashMap;
    }

    /**
     * @param scenarioHashMap the scenarioHashMap to set
     */
    public void setScenarioHashMap(HashMap scenarioHashMap) {
        this.scenarioHashMap = scenarioHashMap;
    }

    /**
     * @return the scenarioName
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * @param scenarioName the scenarioName to set
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * @return the scenarioDesc
     */
    public String getScenarioDesc() {
        return scenarioDesc;
    }

    /**
     * @param scenarioDesc the scenarioDesc to set
     */
    public void setScenarioDesc(String scenarioDesc) {
        this.scenarioDesc = scenarioDesc;
    }

    /**
     * @return the scenarioTimeRangeMap
     */
    public HashMap getScenarioTimeRangeMap() {
        return scenarioTimeRangeMap;
    }

    /**
     * @param scenarioTimeRangeMap the scenarioTimeRangeMap to set
     */
    public void setScenarioTimeRangeMap(HashMap scenarioTimeRangeMap) {
        this.scenarioTimeRangeMap = scenarioTimeRangeMap;
    }

    public void setSessionContextScenario(HttpSession session, Container container, String scnName) {

        HashMap map = new HashMap();
        if (session.getAttribute("SCENARIOTAB") != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            map.remove(scnName);
            map.put(scnName, container);

        } else {
            map.put(scnName, container);

        }
        session.setAttribute("SCENARIOTAB", map);
    }

    public HashMap getParameterGroupAnalysisHashMap() {
        return ParameterGroupAnalysisHashMap;
    }

    public void setParameterGroupAnalysisHashMap(HashMap ParameterGroupAnalysisHashMap) {
        this.ParameterGroupAnalysisHashMap = ParameterGroupAnalysisHashMap;
    }

    public int getColumnsPerSlide() {
        if (this.columnsPerSlide == 0) {
            return this.defcolumnsPerSlide;
        } else {
            return this.columnsPerSlide;
        }
    }

    public void setColumnsPerSlide(int columnsPerSlide) {
        this.columnsPerSlide = columnsPerSlide;
    }

    public int getCurrentColumnPage() {
        if (this.currentColumnPage < 1) {
            return 1;
        } else {
            return this.currentColumnPage;
        }
    }

    public void setCurrentColumnPage(int currentColumnPage) {
        this.currentColumnPage = currentColumnPage;
    }

    public int getFromColumn() {
        return this.fromColumn;
    }

    public void setFromColumn(int fromColumn) {
        this.fromColumn = fromColumn;
    }

    public boolean getRowGrandTotalReq() {
        return rowGrandTotalReq;
    }

    public void setRowGrandTotalReq(boolean rowGrandTotalReq) {
        this.rowGrandTotalReq = rowGrandTotalReq;
    }

    public boolean getRowNetTotalReq() {
        return rowNetTotalReq;
    }

    public void setRowNetTotalReq(boolean rowNetTotalReq) {
        this.rowNetTotalReq = rowNetTotalReq;
    }

    public HashMap getDBKPIHashMap() {
        return DBKPIHashMap;
    }

    public HashMap getDBKPIGraphHashMap() {
        return DBKPIGraphHashMap;
    }

    public HashMap getDBGraphHashMap() {
        return DBGraphHashMap;
    }

    public void setDBKPIHashMap(HashMap DBKPIHashMap) {
        this.DBKPIHashMap = DBKPIHashMap;
    }

    public void setDBKPIGraphHashMap(HashMap DBKPIGraphHashMap) {
        this.DBKPIGraphHashMap = DBKPIGraphHashMap;
    }

    public void setDBGraphHashMap(HashMap DBGraphHashMap) {
        this.DBGraphHashMap = DBGraphHashMap;
    }

    public ArrayList getDisplayColumnsInColumnPagination() {
        return displayColumnsInColumnPagination;
    }

    public void setDisplayColumnsInColumnPagination(ArrayList displayColumnsInColumnPagination) {
        this.displayColumnsInColumnPagination = displayColumnsInColumnPagination;
    }

    public ArrayList getDisplayLabelsInColumnPagination() {
        return displayLabelsInColumnPagination;
    }

    public void setDisplayLabelsInColumnPagination(ArrayList displayLabelsInColumnPagination) {
        this.displayLabelsInColumnPagination = displayLabelsInColumnPagination;
    }

    public //DisplayLabelsInColumnPagination
            //added by chiranjeevi on 13-03-2010 for tracking of Parameter Drill down
            HashMap getParameterDrillMap() {
        return ParameterDrillMap;
    }

    public void setParameterDrillMap(HashMap ParameterDrillMap) {
        this.ParameterDrillMap = ParameterDrillMap;
    }

//     /**
//     * @return the tableDisplayRows
//     */
//    public String getTableDisplayRows() {
//        return tableDisplayRows;
//}
//
//    /**
//     * @param tableDisplayRows the tableDisplayRows to set
//     */
//    public void setTableDisplayRows(String tableDisplayRows) {
//        this.tableDisplayRows = tableDisplayRows;
//    }
    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

//added by k
    public HashMap getTimeParameterHashMap() {
        return TimeParameterHashMap;
    }

    public void setTimeParameterHashMap(HashMap TimeParameterHashMap) {
        this.TimeParameterHashMap = TimeParameterHashMap;
    }

    public HashMap getReportParameterHashMap() {
        return ReportParameterHashMap;
    }

    public void setReportParameterHashMap(HashMap ReportParameterHashMap) {
        this.ReportParameterHashMap = ReportParameterHashMap;
    }

    public String getKpinamesstr() {
        return kpinamesstr;
    }

    public void setKpinamesstr(String akpinamesstr) {
        kpinamesstr = akpinamesstr;
    }

    public String getKpielementsstr() {
        return kpielementsstr;
    }

    public void setKpielementsstr(String akpielementsstr) {
        kpielementsstr = akpielementsstr;
    }

    /**
     * @return the ParamSectionDisplay
     */
    public String getParamSectionDisplay() {
        return ParamSectionDisplay;
    }

    /**
     * @param ParamSectionDisplay the ParamSectionDisplay to set
     */
    public void setParamSectionDisplay(String ParamSectionDisplay) {
        this.ParamSectionDisplay = ParamSectionDisplay;
    }

    /**
     * @return the reportParamDrillAssis
     */
    public String getReportParamDrillAssis() {
        return reportParamDrillAssis;
    }

    /**
     * @param reportParamDrillAssis the reportParamDrillAssis to set
     */
    public void setReportParamDrillAssis(String reportParamDrillAssis) {
        this.reportParamDrillAssis = reportParamDrillAssis;
    }

    /**
     * @return the parameterDrillDisplay
     */
    public String getParameterDrillDisplay() {
        return parameterDrillDisplay;
    }

    /**
     * @param parameterDrillDisplay the parameterDrillDisplay to set
     */
    public void setParameterDrillDisplay(String parameterDrillDisplay) {
        this.parameterDrillDisplay = parameterDrillDisplay;
    }

    public List<String> getMapMainMeasure() {
        return mapMainMeasure;
    }

    public void setMapMainMeasure(String mapMainMeasure) {
        this.mapMainMeasure.add(mapMainMeasure);
    }

    public void setMapMainMeasureLabel(String mapMainMeasureLabel) {
        this.mapMainMeasureLabel.add(mapMainMeasureLabel);
    }

    public List<String> getMapMainMeasureLabel() {
        return this.mapMainMeasureLabel;
    }

    public List<String> getMapSupportingMeasures() {
        return mapSupportingMeasures;
    }

    public void setMapSupportingMeasures(List<String> mapSupportingMeasures) {
        this.mapSupportingMeasures = mapSupportingMeasures;
    }

    public void setMapSupportingMeasuresLabels(List<String> mapSupportingMeasuresLabels) {
        this.mapSupportingMeasuresLabels = mapSupportingMeasuresLabels;
    }

    public List<String> getMapSupportingMeasuresLabels() {
        return this.mapSupportingMeasuresLabels;
    }

    public void resetMapMainMeasures() {
        this.mapMainMeasure = new ArrayList<String>();
    }

    public void resetMapMainMeasureLabels() {
        this.mapMainMeasureLabel = new ArrayList<String>();
    }

    public void resetMapSupportingMeasures() {
        this.mapSupportingMeasures = new ArrayList<String>();
    }

    public void resetMapSupportingMeasuresLabels() {
        this.mapSupportingMeasuresLabels = new ArrayList<String>();
    }

    /**
     * Get the list of Measures shown in the Table
     *
     * @return ArrayList<String> of element Ids of Measures used in Table
     * ElementIds will be of A_ format
     */
    public ArrayList<String> getTableDisplayMeasures() {
        ArrayList<String> measLst = new ArrayList<String>();
        if (this.TableHashMap != null && this.TableHashMap.get("Measures") != null) {
            ArrayList<String> tblMeasLst = (ArrayList<String>) this.TableHashMap.get("Measures");
            if (tblMeasLst != null) {
                for (String measure : tblMeasLst) //  if(!measLst.contains(measure))
                {
                    measLst.add(measure);
                }
            }
        }
        return measLst;
    }

    public void setStatFuncForMeas(String func, String measName) {
        statMap.put(measName, func);
    }

    public Set<String> getStatFuncForMeas(String measName) {
        return this.statMap.get(measName);
    }

    public HashMultimap<String, String> getStatMeasMap() {
        return this.statMap;
    }

    public Set<String> getStatFuncsForReport() {
        Set<String> measSet = this.statMap.keySet();
        Set<String> statFuncSet = new HashSet<String>();
        for (String meas : measSet) {
            Set<String> statFunc = this.statMap.get(meas);
            for (String func : statFunc) {
                statFuncSet.add(func);
            }
        }
        return statFuncSet;
    }

    public void resetStatFuncForMeas() {
        this.statMap = null;
        this.statMap = HashMultimap.create();
    }

    /**
     * Is the report a Crosstab Report
     *
     * @return boolean
     */
    public boolean isReportCrosstab() {
//        if ( this.reportCollect.reportColViewbyValues != null && this.reportCollect.reportColViewbyValues.size() > 0 )
//            return true;
//        else
//            return false;
        return this.isReportCrosstab;
    }

    public void setReportCrosstab(boolean isReportCrosstab) {
        this.isReportCrosstab = isReportCrosstab;
    }

    /**
     * Gets the number of Measures in a report This includes both calculated
     * measure and Query Measures Calculated Measure is %wise measure (eg.) For
     * getting Query Measures alone use function getTableMeasures()
     *
     * @return Measure Count
     */
    public int getReportMeasureCount() {
        int measCount = 0;
        int rowViewCount = (this.viewByCount);
        if (isReportCrosstab()) {
            measCount = this.getTableMeasure().size();
            int rtMeas = 0;
            int qryMeas = 0;
            for (int i = rowViewCount; i < displayColumns.size(); i++) {
                if (RTMeasureElement.isRunTimeMeasure(this.displayColumns.get(i))) //instead of limiting to %wise just count any run time measure
                {
                    rtMeas++;
                } else {
                    qryMeas++;
                }
            }
            int colDimGrps = qryMeas / measCount;
            rtMeas = rtMeas / colDimGrps;
            return (measCount + rtMeas);
        } else {
            measCount = this.displayColumns.size() - rowViewCount;
            return measCount;
        }
    }

    /**
     * Gets the list of measures in the report (progen table) For getting Query
     * Measures alone use function getTableMeasures()
     *
     * @return ArrayList of Measures
     */
    public ArrayList<String> getReportMeasureNames() {
        ArrayList<String> measNameList = new ArrayList<String>();
        if (this.getTableHashMap() != null) {
            ArrayList<String> tmpList = (ArrayList<String>) this.getTableHashMap().get("MeasuresNames");
            if (tmpList != null) {
                for (String measName : tmpList) //  if(!measNameList.contains(measName))
                {
                    measNameList.add(measName);
                }
            }
        }
        return measNameList;
    }

    public LinkedHashMap getSavegraphchanges() {
        return savegraphchanges;
    }

    public void setSavegraphchanges(LinkedHashMap savegraphchanges) {
        this.savegraphchanges = savegraphchanges;
    }

    public static Container getContainerFromSession(HttpServletRequest request, String reportId) {
        HttpSession session = request.getSession(false);
        HashMap map;
        Container container = null;
        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(reportId) != null) {
                    container = (Container) map.get(reportId);
                }
            }
        }
        return container;
    }

    public boolean isRTMeasurePresent(String elementId, RTMeasureElement colType) {
        boolean isMeasurePresent = false;
        if (colType == RTMeasureElement.RANK) {
            elementId = elementId.concat(RTMeasureElement.RANK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.PERCENTWISE) {
            elementId = elementId.concat(RTMeasureElement.PERCENTWISE.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.PERCENTWISESUBTOTAL) {
            elementId = elementId.concat(RTMeasureElement.PERCENTWISESUBTOTAL.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.WHATIF) {
            elementId = elementId.concat(RTMeasureElement.WHATIF.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.WHATIFTARGET) {
            elementId = elementId.concat(RTMeasureElement.WHATIFTARGET.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.RUNNINGTOTAL) {
            elementId = elementId.concat(RTMeasureElement.RUNNINGTOTAL.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.GOALSEEK) {
            elementId = elementId.concat(RTMeasureElement.GOALSEEK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.USERGOALSEEK) {
            elementId = elementId.concat(RTMeasureElement.USERGOALSEEK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.TIMEBASED) {
            elementId = elementId.concat(RTMeasureElement.TIMEBASED.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.TIMECHANGEDPER) {
            elementId = elementId.concat(RTMeasureElement.TIMECHANGEDPER.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.USERGOALPERCENT) {
            elementId = elementId.concat(RTMeasureElement.USERGOALPERCENT.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
            //added by anitha for MTD,QTD,YTD in AO report
        } else if (colType == RTMeasureElement.MONTHJOIN) {
            elementId = elementId.concat(RTMeasureElement.MONTHJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
        }
        } else if (colType == RTMeasureElement.QTRJOIN) {
            elementId = elementId.concat(RTMeasureElement.QTRJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.YEARJOIN) {
            elementId = elementId.concat(RTMeasureElement.YEARJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.PMONTHJOIN) {
            elementId = elementId.concat(RTMeasureElement.PMONTHJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.PQTRJOIN) {
            elementId = elementId.concat(RTMeasureElement.PQTRJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.PYEARJOIN) {
            elementId = elementId.concat(RTMeasureElement.PYEARJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.MOMJOIN) {
            elementId = elementId.concat(RTMeasureElement.MOMJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.YOYJOIN) {
            elementId = elementId.concat(RTMeasureElement.YOYJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.QOQJOIN) {
            elementId = elementId.concat(RTMeasureElement.QOQJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.MOYMJOIN) {
            elementId = elementId.concat(RTMeasureElement.MOYMJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.QOYQJOIN) {
            elementId = elementId.concat(RTMeasureElement.QOYQJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.MOMPERJOIN) {
            elementId = elementId.concat(RTMeasureElement.MOMPERJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.YOYPERJOIN) {
            elementId = elementId.concat(RTMeasureElement.YOYPERJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.QOQPERJOIN) {
            elementId = elementId.concat(RTMeasureElement.QOQPERJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.MOYMPERJOIN) {
            elementId = elementId.concat(RTMeasureElement.MOYMPERJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        } else if (colType == RTMeasureElement.QOYQPERJOIN) {
            elementId = elementId.concat(RTMeasureElement.QOYQPERJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }
            //end of code by anitha for MTD,QTD,YTD in AO report
        //added by sandeep for Rank MTD,QTD,YTD tackid-->TA_R_265
else if (colType == RTMeasureElement.QTDRANK) {
            elementId = elementId.concat(RTMeasureElement.QTDRANK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.YTDRANK) {
            elementId = elementId.concat(RTMeasureElement.YTDRANK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.PMTDRANK) {
            elementId = elementId.concat(RTMeasureElement.PMTDRANK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.PQTDRANK) {
            elementId = elementId.concat(RTMeasureElement.PQTDRANK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.PYTDRANK) {
            elementId = elementId.concat(RTMeasureElement.PYTDRANK.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }
        //added by anitha
        else if (colType == RTMeasureElement.WEEKJOIN) {
            elementId = elementId.concat(RTMeasureElement.WEEKJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.PWEEKJOIN) {
            elementId = elementId.concat(RTMeasureElement.PWEEKJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.PYWEEKJOIN) {
            elementId = elementId.concat(RTMeasureElement.PYWEEKJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.WOWJOIN) {
            elementId = elementId.concat(RTMeasureElement.WOWJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.WOYWJOIN) {
            elementId = elementId.concat(RTMeasureElement.WOYWJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.WOWPERJOIN) {
            elementId = elementId.concat(RTMeasureElement.WOWPERJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }else if (colType == RTMeasureElement.WOYWPERJOIN) {
            elementId = elementId.concat(RTMeasureElement.WOYWPERJOIN.getColumnType());
            if (this.displayColumns.contains(elementId)) {
                isMeasurePresent = true;
            }
        }
        //end of code by anitha
        return isMeasurePresent;


    }

    public LinkedListMultimap<RTMeasureElement, String> getRunTimeMeasureList() {

        LinkedListMultimap<RTMeasureElement, String> rtMeasLst;

        rtMeasLst = LinkedListMultimap.create();

        for (String element : this.getDisplayColumns()) {
            if (RTMeasureElement.isRunTimeMeasure(element)) {
                rtMeasLst.put(RTMeasureElement.getMeasureType(element), element);
            }
        }
        return rtMeasLst;
    }

    public String getNumberSymbol(String columnName) {
        HashMap NFMap = null;
        String nbrSymbol = "";

        if (this.getTableHashMap() != null) {
            NFMap = (HashMap) this.getTableHashMap().get("NFMap");
            PbReportViewerBD reportViewerBd = new PbReportViewerBD();
            if (NFMap != null) {
                if (RTMeasureElement.isRunTimeMeasure(columnName)) {
                    nbrSymbol = RTMeasureElement.getMeasureType(columnName).getNumberSymbol();
                }

                if (this.getTableHashMap() != null) {
                    if (this.getTableHashMap().get("NFMap") != null) {
                        NFMap = (HashMap) this.getTableHashMap().get("NFMap");
                        if (isReportCrosstab) {
                            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) this.getRetObj()).crosstabMeasureId;
                            if (crosstabMeasureId != null && crosstabMeasureId.containsKey(columnName)) {
                                columnName = (String) crosstabMeasureId.get(columnName);
                            }
                            NFMap = reportViewerBd.getNFMapForCT((HashMap) this.getTableHashMap().get("NFMap"), this);
                        }
                        if (NFMap.get(columnName) != null) {
                            nbrSymbol = String.valueOf(NFMap.get(columnName));
                        }
                    }
                }
            } else {
                nbrSymbol = getNo_format(columnName);
            }
        }

        return nbrSymbol;
    }

    public Segment getDimensionSegment(String dimension) {
        boolean isThereSegment = false;
        Segment segment = null;
        if (this.dimSegmentMap != null) {
            segment = this.dimSegmentMap.get(dimension);
        }
        return segment;
    }

    public ArrayList<Segment> getDimensionSegment() {
        ArrayList<Segment> dimSegmentvalues = new ArrayList<Segment>();
        if (this.dimSegmentMap != null) {
            Set<String> segmentKeySet = dimSegmentMap.keySet();
            for (String dimvalue : segmentKeySet) {
                dimSegmentvalues.add(dimSegmentMap.get(dimvalue));
            }
        }
        return dimSegmentvalues;
    }

    public boolean isDimensionSegmented(String dimension) {
        boolean isThereSegment = false;
        Segment segment = null;
        if (this.dimSegmentMap != null) {
            segment = this.dimSegmentMap.get(dimension);
            if (segment != null) {
                isThereSegment = true;
            }
        }
        return isThereSegment;
    }

    public void addDimensionSegment(String dimEleId, Segment segment) {
        if (this.dimSegmentMap == null) {
            this.dimSegmentMap = new HashMap<String, Segment>();
        }

        this.dimSegmentMap.put(dimEleId, segment);
    }

    /**
     * @return the whatIfScenario
     */
    public WhatIfScenario getWhatIfScenario() {
        return whatIfScenario;
    }

    /**
     * @param whatIfScenario the whatIfScenario to set
     */
    public void setWhatIfScenario(WhatIfScenario whatIfScenario) {
        this.whatIfScenario = whatIfScenario;
    }

    public ArrayList<String> getWhatIfMeasureList() {
        return this.whatIfScenario.getWhatIfMeasureList();
    }

    public void setReportParameter(ReportParameter param) {
        this.repParameter = param;
    }

    public ReportParameter getReportParameter() {
        return this.repParameter;
    }

    public ColorGroup getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(ColorGroup colorGroup) {
        this.colorGroup = colorGroup;
    }

    public void setCrosstabColumnSpan(ArrayListMultimap<Integer, Integer> colSpans) {
        this.crosstabColumnSpan = colSpans;
    }

    public ArrayListMultimap<Integer, Integer> getCrosstabColumnSpan() {
        return this.crosstabColumnSpan;
    }

    public boolean isViewByChanged() {
        return viewByChanged;
    }

    public void setViewByChanged(boolean viewByChanged) {
        this.viewByChanged = viewByChanged;
    }

    public void setRowViewDisplayMode(String rowViewDisplayMode) {
        tableProperties.tableRowViewDisplayMode = rowViewDisplayMode;
    }

    public boolean setCrosstabGrandTotalDisplayPosition(String ctGrandTotalDisplayPosition) {
        if (!tableProperties.crosstabGrandTotalDisplay.equalsIgnoreCase(ctGrandTotalDisplayPosition)) {
            tableProperties.crosstabGrandTotalDisplay = ctGrandTotalDisplayPosition;
            return true;
        }
        return false;
    }

    public boolean setCrosstabSubTotalDisplayPosition(String ctSubTotalDisplayPosition) {
        if (!tableProperties.crosstabSubTotalDisplay.equalsIgnoreCase(ctSubTotalDisplayPosition)) {
            tableProperties.crosstabSubTotalDisplay = ctSubTotalDisplayPosition;
            return true;
        }
        return false;
    }

    public void wrapHeadersInCrosstab(boolean wrapHeaders) {
        tableProperties.crosstabWrapHeaders = wrapHeaders;
    }

    public boolean isCrosstabHeaderWrapped() {
        return tableProperties.crosstabWrapHeaders;
    }

    public void setTransposeTable(boolean transpose) {
        tableProperties.isTransposed = transpose;
    }

    public void setCrosstabDynamicRowDisplay(boolean dynamicRowDisplay) {
        tableProperties.crosstabDynamicRowDisplay = dynamicRowDisplay;
    }

    public String getRowViewDisplayMode() {
        return tableProperties.tableRowViewDisplayMode;
    }

    public String getCrosstabGrandTotalDisplayPosition() {
        return tableProperties.crosstabGrandTotalDisplay;
    }

    public String getCrosstabSubTotalDisplayPosition() {
        return tableProperties.crosstabSubTotalDisplay;
    }

    public boolean isTransposed() {
        return tableProperties.isTransposed;
    }

    public boolean isDynamicRowsDisplayedInCrosstab() {
        return tableProperties.crosstabDynamicRowDisplay;
    }

    public HashMap getDbrdDispValue() {
        return dbrdDispValue;
    }

    public void setDbrdDispValue(HashMap dbrdDispValue) {
        this.dbrdDispValue = dbrdDispValue;
    }

    public void setTopBottomColumn(String topBtmType, String topBtmMode, int topBtmCount) {
        tableProperties.topBtmCount = topBtmCount;
        tableProperties.topBtmType = topBtmType;
        tableProperties.topBtmMode = topBtmMode;
    }

    public void setkpiTopBottomColumn(int kpitopBtmCount) {
        tableProperties.kpitopBtmCount = kpitopBtmCount;
//        tableProperties.topBtmType = topBtmType;
//        tableProperties.topBtmMode = topBtmMode;
    }

    public void setSubTotalTopBottomColumn(String topBtmType, String topBtmMode, int topBtmCount) {
        tableProperties.subTotalTopBtmCount = topBtmCount;
        tableProperties.subTotalTopBtmType = topBtmType;
        tableProperties.subTotalTopBtmMode = topBtmMode;
    }

    public void resetTopBottom() {
        tableProperties.topBtmCount = 0;
        tableProperties.topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_NONE;
        tableProperties.topBtmMode = null;
    }

    public boolean isTopBottomSet() {
        return ((tableProperties.topBtmType.equals(ContainerConstants.TOP_BOTTOM_TYPE_NONE)) ? false : true);
    }

    public boolean isSubTotalTopBottomSet() {
        if (tableProperties.subTotalTopBtmType != null && ContainerConstants.TOP_BOTTOM_TYPE_NONE != null) {
            return ((tableProperties.subTotalTopBtmType.equals(ContainerConstants.TOP_BOTTOM_TYPE_NONE)) ? false : true);
        } else {

            return false;
        }
    }

    public int getTopBottomCount() {
        return tableProperties.topBtmCount;
    }

    public int getkpiTopBottomCount() {
        return tableProperties.kpitopBtmCount;
    }

    public int getSubTtlTopBottomCount() {
        return tableProperties.subTotalTopBtmCount;
    }

    public String getTopBottomType() {
        return tableProperties.topBtmType;
    }

    public String getTopBottomMode() {
        return tableProperties.topBtmMode;
    }

    public String getgetSubTtlTopBottomType() {
        return tableProperties.subTotalTopBtmType;
    }

    public String getgetSubTtlTopBottomMode() {
        return tableProperties.subTotalTopBtmMode;
    }

    public String tablePropertiesXml() {
        StringBuilder tblProperties = new StringBuilder();
        tblProperties.append("<TableProperty>");
        int index = 0;
        int srchIndex = 0;
        int srchIndexSrch = 0;
        if (!this.sortColumns.isEmpty()) {
            tblProperties.append("<Sort>");
            for (String sortColumn : this.sortColumns) {
                tblProperties.append("<SortColumn>");
                tblProperties.append("<ColumnName>");
                tblProperties.append(sortColumn);
                tblProperties.append("</ColumnName>");
                tblProperties.append("<SortType>");
                tblProperties.append(this.sortTypes[index]);
                tblProperties.append("</SortType>");
                tblProperties.append("</SortColumn>");
                index++;
            }
            tblProperties.append("</Sort>");
        }
        index = 0;
        if (!this.explicitSortColumns.isEmpty() && netTotalReq) {
            tblProperties.append("<ESort>");
            for (String sortColumn : this.explicitSortColumns) {
                tblProperties.append("<ESortColumn>");
                tblProperties.append("<EColumnName>");
                tblProperties.append(sortColumn);
                tblProperties.append("</EColumnName>");
                tblProperties.append("<ESortType>");
                tblProperties.append(this.explicitSortTypes[index]);
                tblProperties.append("</ESortType>");
                tblProperties.append("</ESortColumn>");
                index++;
            }
            tblProperties.append("</ESort>");
        }
        if (!this.srchColumns.isEmpty()) {
            tblProperties.append("<TableSearch>");
            for (String srchColumn : this.srchColumns) {
                tblProperties.append("<Search>");
                tblProperties.append("<Column>");
                tblProperties.append(srchColumn);
                tblProperties.append("</Column>");
                tblProperties.append("<Condition>");
                tblProperties.append(this.srchCondition.get(srchIndex));
                tblProperties.append("</Condition>");
                tblProperties.append("<Value>");
                if (this.srchCondition.get(srchIndex).equalsIgnoreCase("BT")) {
                    ArrayList<BigDecimal> betweenVal = (ArrayList) this.srchValue.get(srchIndex);
                    tblProperties.append(betweenVal.get(0)).append(',').append(betweenVal.get(1));
                } else {
                    tblProperties.append(this.srchValue.get(srchIndex).toString());
                }

                tblProperties.append("</Value>");
                tblProperties.append("</Search>");
                srchIndex++;
            }
            tblProperties.append("</TableSearch>");
        }

        if (!this.subTotalSrchColumns.isEmpty()) {
            tblProperties.append("<TableSubTotalSearch>");
            for (String srchColumn : this.subTotalSrchColumns) {
                tblProperties.append("<subTotalSearch>");
                tblProperties.append("<Column>");
                tblProperties.append(srchColumn);
                tblProperties.append("</Column>");
                tblProperties.append("<subTotalSrchCondition>");
                tblProperties.append(this.subTotalSrchCondition.get(srchIndexSrch));
                tblProperties.append("</subTotalSrchCondition>");
                tblProperties.append("<SrchValue>");
                tblProperties.append(this.subTotalSrchValue.get(srchIndexSrch).toString());
                tblProperties.append("</SrchValue>");
                tblProperties.append("</subTotalSearch>");
                srchIndexSrch++;
            }
            tblProperties.append("</TableSubTotalSearch>");
        }
        if (this.isMeasureGroupEnable()) {
            tblProperties.append("<MeasureGroups>");
            ArrayList<MeasureGroup> measuGroups = this.measureGroups;
            for (MeasureGroup measureGroup : measuGroups) {
                tblProperties.append("<MeasureGroup>");
                tblProperties.append("<MeasureGroupName>");
                tblProperties.append(measureGroup.getGroupName());
                tblProperties.append("</MeasureGroupName>");
                ArrayList<MeasureGroupColumn> groupColumns = measureGroup.getParticipatingMeasures();
                for (MeasureGroupColumn groupColumn : groupColumns) {
                    tblProperties.append("<ColumnID>");
                    tblProperties.append(groupColumn.getMeasID());
                    tblProperties.append("</ColumnID>");
                    tblProperties.append("<ColumnName>");
                    tblProperties.append(groupColumn.getAlternateLabel());
                    tblProperties.append("</ColumnName>");
                }
                tblProperties.append("</MeasureGroup>");
            }

            tblProperties.append("</MeasureGroups>");

        }
        if (this.groupColumns != null && !this.groupColumns.isEmpty()) {
            tblProperties.append("<Group>");

            tblProperties.append("<GroupColumn>");
            tblProperties.append("<GoupId>");
            tblProperties.append(this.groupColumns);
            tblProperties.append("</GoupId>");
            tblProperties.append("</GroupColumn>");


            tblProperties.append("</Group>");
        }
        if (this.graphCrossTabMeas != null && !this.graphCrossTabMeas.isEmpty()) {
            tblProperties.append("<GraphCrosstabMeas>");
            tblProperties.append(this.graphCrossTabMeas);
            tblProperties.append("</GraphCrosstabMeas>");
        }
        tblProperties.append(this.tableProperties.toXml());
        tblProperties.append("</TableProperty>");
        return tblProperties.toString();
    }

    public HashMap getDbrdViewerDispValue() {
        return dbrdViewerDispValue;
    }

    public void setDbrdViewerDispValue(HashMap dbrdViewerDispValue) {
        this.dbrdViewerDispValue = dbrdViewerDispValue;
    }

    public String getTableDisplayMode() {
        return tableProperties.tableDisplayMode;
    }

    public void setTableDisplayMode(String displayMode) {
        this.tableProperties.tableDisplayMode = displayMode;
    }

    public void setRoundPrecisionForMeasure(String measEleId, int precision) {
        this.measRoundingPrecisions.put(measEleId, precision);
    }

    public int getRoundPrecisionForMeasure(String measEleId) {
        int precision = -999;
        //modified by Nazneen for Modify Measure Attribute
//        if(this.getRound(measEleId)==null || this.getRound(measEleId).equalsIgnoreCase("")){
//        if ( this.measRoundingPrecisions.get(measEleId) != null )
//            precision = this.measRoundingPrecisions.get(measEleId);
//        }else{
//            precision=Integer.parseInt(this.getRound(measEleId));
//        }
        if (this.measRoundingPrecisions.get(measEleId) == null || this.measRoundingPrecisions.get(measEleId).toString().equalsIgnoreCase("")) {
            if (this.getRound(measEleId) == null || this.getRound(measEleId).equalsIgnoreCase("")) {
                precision = -999;
            } else {
                precision = Integer.parseInt(this.getRound(measEleId));
            }
        } else {
            precision = this.measRoundingPrecisions.get(measEleId);
        }
        return precision;
    }

    public void resetSignsForMeasure() {
        this.signHelperMap.clear();
    }

    public ColorHelper getSignForMeasure(String measure) {
        if (signHelperMap != null && signHelperMap.get(measure) != null) {
            return signHelperMap.get(measure);
        } else {
            return null;
        }
    }

    public void setSignForMeasure(String measure, ColorHelper helper) {
        signHelperMap.put(measure, helper);
    }

    public boolean isSignSetForMeasure(String measure) {
        if (signHelperMap.get(measure) == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isFontColorSetForMeasure(String measure) {
        if (fontColorMap.get(measure) == null) {
            return false;
        } else {
            return true;
        }
    }

    public ColorHelper getFontColorForMeasure(String measure) {

        if (fontColorMap != null && fontColorMap.get(measure) != null) {
            return fontColorMap.get(measure);
        } else {
            return null;
        }
    }

//added by Mohit for Custom setting
    public void setcustomsetting(String custom) {
        this.customsetting = custom;
    }

    public String getcustomsetting() {
        return this.customsetting;
    }

    public void setFontColorMap(String measure, ColorHelper helper) {
        fontColorMap.put(measure, helper);
    }

    public ExcelCellFormatGroup getExcelCellGroup() {
        return excelCellGroup;
    }

    public void setExcelCellGroup(ExcelCellFormatGroup excelCellGroup) {
        this.excelCellGroup = excelCellGroup;
    }

    public ExcelColumnGroup getExcelColumnGroup() {
        return excelColumnGroup;
    }

    public void setExcelColumnGroup(ExcelColumnGroup excelColumnGroup) {
        this.excelColumnGroup = excelColumnGroup;
    }

    public List<String> getRtExcelColumns() {
        return rtExcelColumns;
    }

    public void setRtExcelColumns(List<String> rtExcelColumns) {
        this.rtExcelColumns = rtExcelColumns;
    }

    public String getMeasureName(String elementId) {
        String measName = null;
        if (!this.isReportCrosstab) {
            if (elementId != null && displayColumns != null) {
                if (!elementId.startsWith("A_")) {
                    elementId = "A_" + elementId;
                }
                int index = displayColumns.indexOf(elementId);
                if (index >= 0) {
                    measName = (String) displayLabels.get(index);
                    if (!this.isReportCrosstab && measName.contains("[")) {
                        measName = measName.replace("[", "").replace("]", "").trim().split(",")[1];
                    }
                } else {
                    index = displayColumns.indexOf(elementId.replace("A_", ""));
                    if (index > 0) {
                        measName = (String) displayColumns.get(index);
                    }
                }
            }
        }
        if (measName == null) {
            HashMap<String, String> crosstabMsrMap = getReportCollect().getCrosstabMsrMap();
            if (crosstabMsrMap != null && crosstabMsrMap.containsKey(elementId)) {
                measName = (String) crosstabMsrMap.get(elementId);
                if (!this.isReportCrosstab && measName.contains("[")) {
                    measName = measName.replace("[", "").replace("]", "").trim().split(",")[1];
                }

            } else {
                if (elementId.startsWith("A_")) {
                    elementId = elementId.replace("A_", "");
                }
                ArrayList elementIds = (ArrayList) ReportHashMap.get("reportQryElementIds");
                ArrayList elementNames = (ArrayList) ReportHashMap.get("reportQryColNames");

                int index = elementIds.indexOf(elementId);
                if (index >= 0) {
                    measName = (String) elementNames.get(index);
                }
            }
        }
        return measName;
    }

    public int getNoOfDays() {
        return this.noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public void initializeRuntimeMeasures() {
        DataFacade facade = DataFacade.getFacade(this);
        RunTimeMeasCalculator calculator = new RunTimeMeasCalculator(facade);
        RunTimeMeasure rtMeasure;
        LinkedListMultimap<RTMeasureElement, String> rtMeasLst = this.getRunTimeMeasureList();
        Set<RTMeasureElement> keySet = rtMeasLst.keySet();
        Iterator<RTMeasureElement> rtMeasIter = keySet.iterator();
        while (rtMeasIter.hasNext()) {
            RTMeasureElement runtimeType = rtMeasIter.next();
            List<String> measLst = rtMeasLst.get(runtimeType);
            /*
             * if ( RTMeasureElement.PERCENTWISESUBTOTAL == runtimeType ) { for
             * (String measure : measLst) { rtMeasure =
             * calculator.calculatePercentWiseForSubtotal(RTMeasureElement.getOriginalColumn(measure),
             * displayColumns.get(0)); this.ret.setRunTimeMeasureData(measure,
             * rtMeasure); } } else
            {
             */
            //Start of code by Nazneen in May 2012 for Rank On ST
            ArrayList<String> rowViewBys = reportCollect.reportRowViewbyValues;
            //end of code by Nazneen in May 2012 for Rank On ST
            if (RTMeasureElement.WHATIF != runtimeType && RTMeasureElement.WHATIFTARGET != runtimeType) {
                for (String measure : measLst) {
                    //modified by Nazneen in May 2012 for Rank On ST
//                    rtMeasure = calculator.computeRunTimeMeasure(measure, runtimeType, displayColumns.get(0));
                    String measureType = getmeasureType(measure.replaceAll("_rankST", ""));
                    String symbol = getNumberSymbol(measure.replaceAll("_rankST", ""));
                    int precision = getRoundPrecisionForMeasure(measure.replaceAll("_rankST", ""));
                    rtMeasure = calculator.computeRunTimeMeasure(measure, runtimeType, displayColumns.get(0), rowViewBys, measureType, symbol, precision);
                    if(rtMeasure!=null)
                    this.ret.setRunTimeMeasureData(measure, rtMeasure);
//                    this.getRetObj().computeRuntimeMeasure(measure, runtimeType);
                }
            }
//            }
        }
    }

    public Iterable<String> getParamSecureValue(String paramId) {
        Iterable<String> secureValues;
        ReportParameterSecurity paramSecurity = this.paramSecurityMap.get(paramId);
        if (paramSecurity != null) {
            secureValues = paramSecurity.getParamSecureValues();
        } else {
            secureValues = new ArrayList<String>();
        }
        return secureValues;
    }

    public ReportParameterSecurity getParameterSecurity(String paramId) {
        ReportParameterSecurity paramSecurity = this.paramSecurityMap.get(paramId);
        return paramSecurity;
    }

    public void setParameterSecurity(String paramId, String memberId, Iterable<String> paramValues) {
        ReportParameterSecurity paramSecurity = new ReportParameterSecurity();
        paramSecurity.setMemberId(memberId);
        paramSecurity.setParamSecureValues(paramValues);
        paramSecurity.setParameteElementId(paramId);
        paramSecurityMap.put(paramId, paramSecurity);
    }

    public void removeParameterSecurity(String paramId) {
        this.paramSecurityMap.remove(paramId);
    }

    public Iterable<String> getParameterElements() {
        Set<String> parameterElements = new HashSet<String>();
        if (!this.reportCollect.reportParameters.isEmpty()) {
            parameterElements = this.reportCollect.reportParameters.keySet();
        }
        return parameterElements;
    }

    public List<String> getParameterDefaultValues(String elementId) {
        return this.reportCollect.getDefaultValue(elementId);
    }
    //added by sruthi for table coloumn prop
    public void changeGlobalLabelForMeasure(String measId, String measLabel) {
        ArrayList<String> measurids = new ArrayList<String>();
        ArrayList<String> Measures = (ArrayList<String>) TableHashMap.get("Measures");
        ArrayList<String> MeasuresNames = (ArrayList<String>) TableHashMap.get("MeasuresNames");
        if (this.getMeasurAlis()) {
            if (this.isReportCrosstab()) {
                ArrayList<String> hashkeys = new ArrayList<String>();
                HashMap<String, String> crosstabMeasureId = ((PbReturnObject) this.getRetObj()).crosstabMeasureId;
                if (crosstabMeasureId != null) {
                    for (String key : crosstabMeasureId.keySet()) {
                        hashkeys.add(key);
                    }
                    for (int i = 0; i < hashkeys.size(); i++) {
                        if (crosstabMeasureId.containsKey(hashkeys.get(i))) {
                            String ColId = (String) crosstabMeasureId.get(hashkeys.get(i));
                            if (measId.equalsIgnoreCase(ColId)) {
                                measurids.add(hashkeys.get(i));
                            }
                        }
                    }
                }
            } else {
                measurids.add(measId);
            }
            for (int j = 0; j < measurids.size(); j++) {
                int measIndex = this.getDisplayColumns().indexOf(measurids.get(j));
                if (this.isReportCrosstab() && measurids.get(j).contains("A_")) {
                    ArrayList Name = (ArrayList<String>) this.getDisplayLabels().get(measIndex);
                    Name.set(1, measLabel);
                    this.getDisplayLabels().set(measIndex, Name);
                    tableProperties.summerizedMsrRename.put(measurids.get(j), measLabel);
                } else {
                    if (this.isReportCrosstab()) {
                        // ArrayList  crossmeasure=new ArrayList();
                        ArrayList Name = (ArrayList<String>) this.getDisplayLabels().get(measIndex);
                        Name.set(1, measLabel);
                        this.getDisplayLabels().set(measIndex, Name);
                        // String measuredata=null;
                        //measuredata=(String) this.getDisplayLabels().get(measIndex);
                        // String[] arrmeasure=measuredata.replace("[","").replace("]","").split(",");
//                          this.getDisplayLabels().set(measIndex, renamedat);
                        //  crossmeasure.add(this.getDisplayLabels().get(measIndex));
                    } else {
                        this.getDisplayLabels().set(measIndex, measLabel);
                    }
                }
                if (this.isReportCrosstab() && measurids.get(j).contains("A_")) {//added by Govardhan for renaming summerized msrs in hybrid report....
                    HashMap<String, ArrayList<String>> Summarizedmap = this.getSummerizedTableHashMap();
                    ArrayList MsrID = Summarizedmap.get("summerizedQryeIds");
                    ArrayList msrNames = Summarizedmap.get("summerizedQryColNames");
                    if (Summarizedmap != null && !Summarizedmap.isEmpty()) {
                        if (MsrID.contains(measurids.get(j))) {
                            int index = MsrID.indexOf(measurids.get(j).toString().replace("A_", ""));
                            msrNames.remove(index);
                            msrNames.add(index, measLabel);
                            Summarizedmap.put("summerizedQryColNames", msrNames);
                            this.setSummerizedTableHashMap(Summarizedmap);
                        }
                    }
                } else {
                    // ArrayList<String> Measures = (ArrayList<String>) TableHashMap.get("Measures");
                    // ArrayList<String> MeasuresNames = (ArrayList<String>) TableHashMap.get("MeasuresNames");
                    if (Measures.contains(measurids.get(j))) {
                        measIndex = Measures.indexOf(measurids.get(j));
                        MeasuresNames.set(measIndex, measLabel);
                        TableHashMap.put("MeasuresNames", MeasuresNames);
                    }
                }
            }
            if (Measures.contains(measId)) {
                int measIndex1 = Measures.indexOf(measId);
                MeasuresNames.set(measIndex1, measLabel);
                TableHashMap.put("MeasuresNames", MeasuresNames);
            }
        }
    }
// enf of sruthi code

    public void changeLabelForMeasure(String measId, String measLabel) {
        int measIndex = this.getDisplayColumns().indexOf(measId);
        if (this.isReportCrosstab() && measId.contains("A_")) {
            ArrayList Name = (ArrayList<String>) this.getDisplayLabels().get(measIndex);
            Name.set(1, measLabel);
            this.getDisplayLabels().set(measIndex, Name);
            tableProperties.summerizedMsrRename.put(measId, measLabel);
        } else {
            //added by sruthi for rename cross tab measure
            if (this.isReportCrosstab()) {
                ArrayList Name = (ArrayList<String>) this.getDisplayLabels().get(measIndex);
                Name.set(1, measLabel);
                this.getDisplayLabels().set(measIndex, Name);
            } else {
                this.getDisplayLabels().set(measIndex, measLabel);
            }
        }
        if (this.isReportCrosstab() && measId.contains("A_")) {//added by Govardhan for renaming summerized msrs in hybrid report....
            HashMap<String, ArrayList<String>> Summarizedmap = this.getSummerizedTableHashMap();
            ArrayList MsrID = Summarizedmap.get("summerizedQryeIds");
            ArrayList msrNames = Summarizedmap.get("summerizedQryColNames");
            int index = MsrID.indexOf(measId.toString().replace("A_", ""));
            msrNames.remove(index);
            msrNames.add(index, measLabel);
            Summarizedmap.put("summerizedQryColNames", msrNames);
            this.setSummerizedTableHashMap(Summarizedmap);

        } else {
            ArrayList<String> Measures = (ArrayList<String>) TableHashMap.get("Measures");
            ArrayList<String> MeasuresNames = (ArrayList<String>) TableHashMap.get("MeasuresNames");
            if (Measures.contains(measId)) {
                measIndex = Measures.indexOf(measId);
                MeasuresNames.set(measIndex, measLabel);
                TableHashMap.put("MeasuresNames", MeasuresNames);
            } else {
                ArrayList RepElementIds = (ArrayList) TableHashMap.get("REP");
                if (RepElementIds.contains(measId.replace("A_", ""))) {
                    ArrayList rep = new ArrayList();
                    String measid = measId.replace("A_", "");
                    rep.add(measid);
                    ArrayList repN = new ArrayList();
                    repN.add(measLabel);
//                 TableHashMap.put("REP", rep);
//                TableHashMap.put("REPNames", repN);
                    ArrayList alist = (ArrayList) TableHashMap.get("REP");
                    ArrayList REPNames = (ArrayList) TableHashMap.get("REPNames");
                    if (alist.size() > 0) {
                        for (int i = 0; i < alist.size(); i++) {
                            if (alist.get(i).toString().equalsIgnoreCase(measid)) {
                                REPNames.set(i, measLabel);
                            }
                        }
                        TableHashMap.put("REPNames", REPNames);
                    }
                }

            }
        }
    }

    public boolean isTargetEntryApplicable() {
        ArrayList<String> rowViewBys = reportCollect.reportRowViewbyValues;
        if (rowViewBys != null && rowViewBys.size() == 1 && "TIME".equals(rowViewBys.get(0))) {
            return true;
        }
        return false;
    }

    public List<String> getRtTargetColumns() {
        return rtTargetColumns;
    }

    public void setRtTargetColumns(List<String> rtTargetColumns) {
        this.rtTargetColumns = rtTargetColumns;
    }

    public void addRTTargetColumn(String column) {
        if (this.rtTargetColumns == null) {
            this.rtTargetColumns = new ArrayList<String>();
        }

        rtTargetColumns.add(column);
    }

    public boolean isTargetColumn(String measId) {
        if (this.rtTargetColumns != null && this.rtTargetColumns.contains(measId)) {
            return true;
        }
        return false;
    }

    public void addGeographyDimensionIds(String dimensionId) {
        this.geographyDimensionIds.add(dimensionId);
    }

    public List<String> getGeographyDimensionIds() {
        return this.geographyDimensionIds;
    }

    public void addMeasureGroups(MeasureGroup measureGroup) {
        this.measureGroups.add(measureGroup);
    }

    public boolean isMeasureGroupEnable() {
        if (measureGroups.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    public boolean isGroupedMeasure(String measID) {
        MeasureGroupColumn groupColumn = null;
        for (MeasureGroup measureGroup : measureGroups) {
            ArrayList<MeasureGroupColumn> measureGroupColumns = measureGroup.getParticipatingMeasures();
            Iterator<MeasureGroupColumn> moduleIter = Iterables.filter(measureGroupColumns, MeasureGroupColumn.getMeasureGroupColumnPredicate(measID)).iterator();
            if (moduleIter.hasNext()) {
                groupColumn = moduleIter.next();
            }
        }
        if (groupColumn != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getMeasureGrpName(String measuID) {
        MeasureGroup measGroup = null;
        MeasureGroupColumn groupColumn = null;
        for (MeasureGroup measureGroup : measureGroups) {
            ArrayList<MeasureGroupColumn> measureGroupColumns = measureGroup.getParticipatingMeasures();
            Iterator<MeasureGroupColumn> moduleIter = Iterables.filter(measureGroupColumns, MeasureGroupColumn.getMeasureGroupColumnPredicate(measuID)).iterator();
            if (moduleIter.hasNext()) {
                groupColumn = moduleIter.next();
                measGroup = measureGroup;
                break;
            }
        }
        return measGroup.getGroupName();
    }

    public int getMesGrpColumnSpan(String measuID) {
        MeasureGroup measGroup = null;
        MeasureGroupColumn groupColumn = null;
        for (MeasureGroup measureGroup : measureGroups) {
            ArrayList<MeasureGroupColumn> measureGroupColumns = measureGroup.getParticipatingMeasures();
            Iterator<MeasureGroupColumn> moduleIter = Iterables.filter(measureGroupColumns, MeasureGroupColumn.getMeasureGroupColumnPredicate(measuID)).iterator();
            if (moduleIter.hasNext()) {
                groupColumn = moduleIter.next();
                measGroup = measureGroup;
                break;
            }
        }
        return measGroup.getParticipatingMeasures().size();
    }

    public String getGrpMeasureName(String measuID) {
        MeasureGroup measGroup = null;
        MeasureGroupColumn groupColumn = null;
        for (MeasureGroup measureGroup : measureGroups) {
            ArrayList<MeasureGroupColumn> measureGroupColumns = measureGroup.getParticipatingMeasures();
            Iterator<MeasureGroupColumn> moduleIter = Iterables.filter(measureGroupColumns, MeasureGroupColumn.getMeasureGroupColumnPredicate(measuID)).iterator();
            if (moduleIter.hasNext()) {
                groupColumn = moduleIter.next();

                break;
            }
        }
        return groupColumn.getAlternateLabel();
    }

    public boolean isFormulaMeasure(String elementId) {
        if (elementId.startsWith("A_")) {
            return reportCollect.isFormulaMeasure(elementId.replace("A_", ""));
        } else {
            return reportCollect.isFormulaMeasure(elementId);
        }

    }

    public ArrayListMultimap<String, Integer> getSubTotRows() {
        return subTotRows;
    }

    public void setSubTotRows(String measId, Integer row) {
        this.subTotRows.put(measId, row);
    }

    public void resetSubTotalRows() {
        subTotRows.clear();
    }

    public void setIndicatorEnabled(String meas, boolean enable) {
        if (enable) {
            reportCollect.indicatorMeasures.put(meas, "Y");
        } else {
            reportCollect.indicatorMeasures.put(meas, "N");
        }
    }

    public void setIndicatorEnabled(String colName, String showIndicators) {
        reportCollect.indicatorMeasures.put(colName, showIndicators);
    }

    public boolean isDrillAcrossSupported() {
        return drillAcrossSupported;
    }

    public void setDrillAcrossSupported(boolean drillAcrossSupported) {
        this.drillAcrossSupported = drillAcrossSupported;
    }

    public int getMeasurePosition() {
        return measurePosition;
    }

    public boolean setMeasurePosition(int measurePosition) {
        if (this.measurePosition != measurePosition) {
            this.measurePosition = measurePosition;
            return true;
        }
        return false;
    }

    public String getParamHtml() {
        StringBuilder html = new StringBuilder();
        Set<ReportParameterValue> repParamValues = this.repParameter.getReportParameterValues();
        for (ReportParameterValue param : repParamValues) {
            String elemId = param.getElementId();
            String elemName = reportCollect.getParameterDispName(elemId);
            html.append(elemName + " : " + param.getParameterValues() + " ");
        }
        return html.toString();
    }

    /**
     * @return the measureNamePosition
     */
    public String getMeasureNamePosition() {
        return measureNamePosition;
    }

    /**
     * @return the showlyAxislabel
     */
    public String getShowlyAxislabel() {
        return showlyAxislabel;
    }

    /**
     * @return the showryAxislabel
     */
    public String getShowryAxislabel() {
        return showryAxislabel;
    }

    /**
     * @return the showxAxislabel
     */
    public String getShowxAxislabel() {
        return showxAxislabel;
    }

    public void setMultiPeriodKPI(MultiPeriodKPI multiPeriodKPI) {
        this.multiPeriodKPI = multiPeriodKPI;
    }

    public MultiPeriodKPI getMultiPeriodKPI() {
        return this.multiPeriodKPI;
    }

    public ArrayListMultimap<String, String> getCrossTabGraphColMap() {
        return crossTabGraphColMap;
    }

    public void setCrossTabGraphColMap(ArrayListMultimap<String, String> crossTabGraphColMap) {
        this.crossTabGraphColMap = crossTabGraphColMap;
    }

    public HashMap<String, String> getGrpIdElementIdMap() {
        return grpIdElementIdMap;
    }

    public void setGrpIdElementIdMap(HashMap<String, String> grpIdElementIdMap) {
        this.grpIdElementIdMap = grpIdElementIdMap;
    }

    public DashletPropertiesHelper getDashletPropertiesHelper() {
        return dashletPropertiesHelper;
    }

    public void setDashletPropertiesHelper(DashletPropertiesHelper dashletPropertiesHelper) {
        this.dashletPropertiesHelper = dashletPropertiesHelper;
    }

    public boolean isSegementedReport() {
        if (dimSegmentMap != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRowCountReq() {
        return rowCountReq;
    }

    public void setRowCountReq(boolean rowCountReq) {
        this.rowCountReq = rowCountReq;
    }

    public ProgenDataSet getGrret() {
        return Grret;
    }

    public void setGrret(ProgenDataSet Grret) {
        this.Grret = Grret;
    }

    public PbReportQuery getViewbyqry() {
        return viewbyqry;
    }

    public void setViewbyqry(PbReportQuery viewbyqry) {
        this.viewbyqry = viewbyqry;
    }

    public void setViewbyqryDashBoard(PbDashBoardQuery viewbyqry) {
        this.viewbyqry1 = viewbyqry;
    }

    public String getRefreshGr() {
        return refreshGr;
    //  return  tableProperties.refreshOnSorting;
        
    }

    public void setRefreshGr(String refreshGr) {
        this.refreshGr = refreshGr;
        //tableProperties.refreshOnSorting = refreshGr;
       
    }

    public void addscriptIndicator(String elementId, String isscriptInd) {
        reportCollect.scriptIndicators.put(elementId, isscriptInd);
    }

    public String getscriptIndicator(String elementId) {
        return reportCollect.scriptIndicators.get(elementId);
    }

    public void settimeConversion(String elementId, String value) {
        reportCollect.timeConversion.put(elementId, value);
    }

    public String gettimeConversion(String elementId) {
        return reportCollect.timeConversion.get(elementId);
    }

    public String getTextAlign(String element) {
        String alignment = "center";
        if (reportCollect.scriptAligns.get(element) != null) {
            if (reportCollect.scriptAligns.get(element.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "")) != null) {
                alignment = reportCollect.scriptAligns.get(element.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", ""));
            }
        }
        return alignment;
    }

    public String getMeasureAlign(String element) {
        String alignment = "center";
        if (reportCollect.measureAligns.get(element) != null) {
            alignment = reportCollect.measureAligns.get(element);
        }
        return alignment;
    }

    public void setTextAlignments(String colName, String scriptAlign) {

        reportCollect.scriptAligns.put(colName, scriptAlign);
    }

    public void setMeasureAlignments(String colName, String measureAlign) {

        reportCollect.measureAligns.put(colName, measureAlign);
    }
 public String getviewbyAlignment(String viewbyid){
      String alignment="Left" ;
      if(tableProperties.ViewbyAligns.get(viewbyid)!=null){
          alignment=tableProperties.ViewbyAligns.get(viewbyid);
      }
      return alignment;
    }
public void setviewbyAlignments(String colName,String viewbyalign){

    tableProperties.ViewbyAligns.put(colName,viewbyalign);
}
public void setViewbydataAlignments(String colName,String viewbydata){
    tableProperties.viewbydataAligns.put(colName, viewbydata);
}
public String getViewbydataAlignments(String viewbyid){
      String alignment="Left" ;
      if(tableProperties.viewbydataAligns.get(viewbyid)!=null){
          alignment=tableProperties.viewbydataAligns.get(viewbyid);
      }
      return alignment;
    }
    public void setMeasureType(String colName, String measureType) {

        reportCollect.measureTypes.put(colName, measureType);
    }

    public void setTextColor(String colName, String scriptAlign) {

        tableProperties.scriptColor.put(colName, scriptAlign);
    }
//added by sruthi for header color

    public void setMeasureColor(String colName, String measureAlign) {

        tableProperties.measureColor.put(colName, measureAlign);
    }//ended by sruthi
    //added by sruthi for header color

    public String getMeasureColor(String element) {
        String color = "";
        if (tableProperties.measureColor != null && !tableProperties.measureColor.isEmpty() && tableProperties.measureColor.get(element) != null) {
            color = tableProperties.measureColor.get(element);
        }
        return color;
    }//ended by sruthi

    public String getTextColor(String element) {
        String textcolor = "";
        if (tableProperties.scriptColor != null && !tableProperties.scriptColor.isEmpty()) {
            if (tableProperties.scriptColor.get(element.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "")) != null) {
                textcolor = tableProperties.scriptColor.get(element.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", ""));
            }
        }
        return textcolor;
    }

    public String getmeasureType(String colName) {
        return reportCollect.measureTypes.get(colName);
    }

    public boolean isMeasDrill() {
        return measDrill;
    }

    public void setMeasDrill(boolean measDrill) {
        this.measDrill = measDrill;
    }

    public HashMap getTimememdetails() {
        return timememdetails;
    }

    public void setTimememdetails(HashMap timememdetails) {
        this.timememdetails = timememdetails;
    }

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    public HashMap getFixedparamhashmap() {
        return fixedparamhashmap;
    }

    public void setFixedparamhashmap(HashMap fixedparamhashmap) {
        this.fixedparamhashmap = fixedparamhashmap;
    }

    /**
     * @param dateFormat the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getAdhocDrillType() {
        return tableProperties.adhocDrillType;
    }

    public void setAdhocDrillType(String adhocDrill) {
        tableProperties.adhocDrillType = adhocDrill;
    }

    public boolean isTreeTableDisplay() {
        return tableProperties.isTreeTableDisplay;
    }

    public void setTreeTableDisplay(boolean treeTableDisplay) {
        tableProperties.isTreeTableDisplay = treeTableDisplay;
    }

    public boolean isParameterDrill() {
        return tableProperties.isParameterDrill;
    }

    public void setParameterDrill(boolean paramDrill) {
        tableProperties.isParameterDrill = paramDrill;
    }

    public boolean isSerialNumDisplay() {
        return tableProperties.isSerialnum;
    }

    public void setSerialNumDisplay(boolean serialNumDisplay) {
        tableProperties.isSerialnum = serialNumDisplay;
    }

    public HashMap getRepLinks() {
        return repLinks;
    }

    public void setRepLinks(HashMap repLinks) {
        this.repLinks = repLinks;
    }

    public String getDependentRepQry() {
        return dependentRepQry;
    }

    public void setDependentRepQry(String dependentRepQry) {
        this.dependentRepQry = dependentRepQry;
    }

    public String getTempRepQry() {
        return tempRepQry;
    }

    public void setTempRepQry(String tempRepQry) {
        this.tempRepQry = tempRepQry;
    }

    public HashMap getDependentviewbyId() {
        return dependentviewbyId;
    }

    public void setDependentviewbyId(HashMap dependentviewbyId) {
        this.dependentviewbyId = dependentviewbyId;
    }

    public HashMap getDependentviewbyIdQry() {
        return dependentviewbyIdQry;
    }

    public void setDependentviewbyIdQry(HashMap dependentviewbyIdQry) {
        this.dependentviewbyIdQry = dependentviewbyIdQry;
    }

    public HashMap getDepViewByConditionsmap() {
        return depViewByConditionsmap;
    }

    public void setDepViewByConditionsmap(HashMap depViewByConditionsmap) {
        this.depViewByConditionsmap = depViewByConditionsmap;
    }

    public String getTextkpiViewBy() {
        return this.textkpiViewBy;
    }

    public void setTextkpiViewBy(String textkpiViewBy) {
        this.textkpiViewBy = textkpiViewBy;
    }

    public void setDrillViewValues(ArrayList drillvalues) {
        this.drillviewvalues.add(drillvalues);
    }

    public ArrayList getDrillViewValues() {
        return this.drillviewvalues;
    }

    public void setdrillvalues(boolean drillvalues) {
        tableProperties.isdrillvalues = drillvalues;
    }

    public boolean isdrillvalues() {
        return tableProperties.isdrillvalues;
    }

    /**
     * @return the currentTimeDetails
     */
    public LinkedHashMap getCurrentTimeDetails() {
        return currentTimeDetails;
    }

    /**
     * @param currentTimeDetails the currentTimeDetails to set
     */
    public void setCurrentTimeDetails(LinkedHashMap currentTimeDetails) {
        this.currentTimeDetails = currentTimeDetails;
    }

    /**
     * @return the drillViewByCheck
     */
    public boolean isDrillViewByCheck() {
        return drillViewByCheck;
    }

    /**
     * @param drillViewByCheck the drillViewByCheck to set
     */
    public void setDrillViewByCheck(boolean drillViewByCheck) {
        this.drillViewByCheck = drillViewByCheck;
    }

    /**
     * @return the oneviewTableTimedetails
     */
    public List<String> getOneviewTableTimedetails() {
        return oneviewTableTimedetails;
    }

    /**
     * @param oneviewTableTimedetails the oneviewTableTimedetails to set
     */
    public void setOneviewTableTimedetails(List<String> oneviewTableTimedetails) {
        this.oneviewTableTimedetails = oneviewTableTimedetails;
    }

    /**
     * @return the oneviewGraphTimedetails
     */
    public List<String> getOneviewGraphTimedetails() {
        return oneviewGraphTimedetails;
    }

    /**
     * @param oneviewGraphTimedetails the oneviewGraphTimedetails to set
     */
    public void setOneviewGraphTimedetails(List<String> oneviewGraphTimedetails) {
        this.oneviewGraphTimedetails = oneviewGraphTimedetails;
    }

    public List<String> getoneviewdrilltimedetails() {
        return oneviewdrilltimedetails;
    }

    public void setoneviewdrilltimedetails(List<String> oneviewdrilltimedetails) {
        this.oneviewdrilltimedetails = oneviewdrilltimedetails;
    }

    public String getDrillfromRepId() {
        return this.drillfromRepId;
    }

    public String getOneviewTableDate() {
        return oneviewTableDate;
    }

    public void setOneviewTableDate(String oneviewTableDate) {
        this.oneviewTableDate = oneviewTableDate;
    }

    public String getOneviewGraphDate() {
        return oneviewGraphDate;
    }

    public void setOneviewGraphDate(String oneviewGraphDate) {
        this.oneviewGraphDate = oneviewGraphDate;
    }

    public void setDrillfromRepId(String drillfromRepId) {
        this.drillfromRepId = drillfromRepId;
    }

    public HashMap getChangeGraphColumns() {
        return changeGraphColumns;
    }

    public void setChangeGraphColumns(HashMap changeGraphColumns) {
        this.changeGraphColumns = changeGraphColumns;
    }

    public void setDateandTimeOptions(String elementId, String dateoptions) {
        this.reportCollect.dateoptions.put(elementId, dateoptions);
    }

    public String getDateandTimeOptions(String elementId) {
        return reportCollect.dateoptions.get(elementId);
    }

    public void setDateSubStringValues(String elementId, String dateSubStringValues) {
        this.reportCollect.dateSubStringValues.put(elementId, dateSubStringValues);
    }

    public String getDateSubStringValues(String elementId) {
        return reportCollect.dateSubStringValues.get(elementId);
    }

    public void setPercentWiseStatus(boolean percentWiseExist) {
        this.tableProperties.percentWiseExist = percentWiseExist;
    }

    public boolean isPercentWiseStatus() {
        return this.tableProperties.percentWiseExist;
    }

    public String getSelectedgraph() {
        return selectedgraph;
    }

    /**
     * @param selectedgraph the selectedgraph to set
     */
    public void setSelectedgraph(String selectedgraph) {
        this.selectedgraph = selectedgraph;
    }

    /**
     * @return the slectedGraphType
     */
    public String getSlectedGraphType(String grpId) {
        String displayType = "null";
        if (this.slectedGraphType.get(grpId) != null) {
            return this.slectedGraphType.get(grpId).toString();
        } else {
            return displayType;
        }
    }

    /**
     * @param slectedGraphType the slectedGraphType to set
     */
    public void setSlectedGraphType(String grpId, String displayType) {

        this.slectedGraphType.put(grpId, displayType);
    }

    public String getGrpFrameHeight() {
        return grpFrameHeight;
    }

    public void setGrpFrameHeight(String grpFrameHeight) {
        this.grpFrameHeight = grpFrameHeight;
    }

    public String getGrpdivHeight() {
        return grpdivHeight;
    }

    public void setGrpdivHeight(String grpdivHeight) {
        this.grpdivHeight = grpdivHeight;
    }

    public String getGrpdivwidth() {
        return grpdivwidth;
    }

    public void setGrpdivwidth(String grpdivwidth) {
        this.grpdivwidth = grpdivwidth;
    }

    public PbReturnObject getKpiretObj() {
        return kpiretObj;
    }

    public void setKpiretObj(PbReturnObject kpiretObj) {
        this.kpiretObj = kpiretObj;
    }

    public ArrayList<String> getKpiQrycls() {
        return kpiQrycls;
    }

    public void setKpiQrycls(ArrayList<String> kpiQrycls) {
        this.kpiQrycls = kpiQrycls;
    }

    public void setDateFormatt(String elementId, String dateformatt) {
        this.reportCollect.dateFormatt.put(elementId, dateformatt);
    }

    public String getDateFormatt(String elementId) {
        return reportCollect.dateFormatt.get(elementId);
    }

    public void setCatAvgTotalReq(boolean req) {
        this.catAvgTotalReq = req;
    }

    public boolean getCatAvgTotalReq() {
        return this.catAvgTotalReq;
    }

    public boolean isCustomTotEnabled() {
        return tableProperties.isCustTotEnabled;
    }

    public void setIsCustTotEnabled(boolean isCustTotEnabled) {
        tableProperties.isCustTotEnabled = isCustTotEnabled;
    }

    public String getCustTotName() {
        return tableProperties.custTotName;
    }

    public void setCustTotName(String custTotName) {
        tableProperties.custTotName = custTotName;
    }

    public String getMappedTo() {
        return tableProperties.mappedTo;
    }

    public void setMappedTo(String mappedTo) {
        tableProperties.mappedTo = mappedTo;
    }

    public String getReportDrillMap(String elementId) {
        return reportCollect.reportDrillMap.get(elementId);
    }

    public void setReportDrillMap(String elementId, String value) {
        reportCollect.reportDrillMap.put(elementId, value);
    }

    public String getMeasureDrillType() {
        return tableProperties.msrDrillType;
    }

    public void setMeasureDrillType(String msrDrill) {
        tableProperties.msrDrillType = msrDrill;
    }
    // krishan pratap

    public String getreportDrillMaptooltip(String elementId) {
        return reportCollect.reportDrillMaptooltip.get(elementId);
    }

    public void setreportDrillMaptooltip(String elementId, String value) {
        this.reportCollect.reportDrillMaptooltip.put(elementId, value);
    }

    public LinkedHashMap getOneviewRepParamDetails() {
        return oneviewRepParamDetails;
    }

    public void setOneviewRepParamDetails(LinkedHashMap oneviewRepParamDetails) {
        this.oneviewRepParamDetails = oneviewRepParamDetails;
    }

    public String getOnebusroleId() {
        return onebusroleId;
    }

    public void setOnebusroleId(String onebusroleId) {
        this.onebusroleId = onebusroleId;
    }

    public boolean isRenameTotal() {
        return tableProperties.isrenameTotal;
    }

    public void setRenameTotal(boolean renameTotal) {
        tableProperties.isrenameTotal = renameTotal;
    }

//added by Dinanath for showing trend Icon
    public boolean isToShowTrendIcon() {
        return tableProperties.isEnableToShowTrendIcon;
    }

    public void setToShowTrendIcon(boolean isEnableToShowTrendIcon) {
        tableProperties.isEnableToShowTrendIcon = isEnableToShowTrendIcon;
    }
    //endded by Dinanath

    public boolean isComparisionEnabled() {
        return tableProperties.enablecomparision;
    }

    public void setisComparisionEnabled(boolean renameTotal) {
        tableProperties.enablecomparision = renameTotal;
    }

    public boolean isaddrowenable() {
        return tableProperties.addrowenable;
    }

    public void setaddrowenable(boolean addrowenable) {
        tableProperties.addrowenable = addrowenable;
    }

    public void addComparisionEnabledmsr(String msr, String priorMsr) {
        tableProperties.ComparisionMsrs.put(msr, priorMsr);
    }

    public HashMap<String, String> getComparisionEnabledmsr() {

        return tableProperties.ComparisionMsrs;
    }
    //sandeep

    public void addispriorcoloumn(String msr, String priorMsr) {
        tableProperties.ispriorcoloumn.put(msr, priorMsr);
    }

    public HashMap<String, String> getispriorcoloumn() {

        return tableProperties.ispriorcoloumn;
    }

    public void rowAddingids(String msr, HashMap position) {
        tableProperties.rowaddingmap.put(msr, position);
    }

    public HashMap getrowAddingids() {

        return tableProperties.rowaddingmap;
    }

    public HashMap getcrossalign() {

        return tableProperties.crossalignment;
    }

    public void rowaddingmapkpi(String msr, String position) {
        String[] rowsadding = position.split(",");
        HashMap<String, String> rowaddingmapinner = new HashMap<String, String>();
        for (int i = 0; i < rowsadding.length; i++) {
            String v1 = rowsadding[0];
            v1 = v1.replace("{", "").replace("\"", "");
            String[] rowsadding1 = v1.split("=");
            v1 = rowsadding1[1];
            String v11 = rowsadding[1];
            v11 = v11.replace("}", "").replace("\"", "");
            rowsadding = v11.split("=");
            v11 = rowsadding[1];
            rowaddingmapinner.put("position", v1);
            rowaddingmapinner.put("type", v11);
            break;
//                                      crosstabvales.add(v1);
        }
        tableProperties.rowaddingmap.put(msr, rowaddingmapinner);
    }

    public void crossalign(String key, String val) {
        tableProperties.crossalignment.put(key, val);
    }

//sandeep
    public String getoriginalTotalName() {
        return tableProperties.originalTotalName;
    }

    public void setoriginalTotalName(String originalTotalName) {
        tableProperties.originalTotalName = originalTotalName;
    }

    public String getRenamedTotalName() {
        return tableProperties.RenamedTotalName;
    }

    public void setRenamedTotalName(String renamedTotalName) {
        tableProperties.RenamedTotalName = renamedTotalName;
    }

    public boolean isReportAccessible() {
        return isReportAccessible;
    }

    public void setIsReportAccessible(boolean isReportAccessible) {
        this.isReportAccessible = isReportAccessible;
    }

    public void setGroupColumn(String sortColumn) {
        this.groupColumns = sortColumn;
         tableProperties.groupColumns =sortColumn;
    }

    public String getGroupColumns() {
        return this.groupColumns;
    }

    public ArrayList getTableList() {
        return tableList;
    }

    public void setTableList(ArrayList tableList) {
        this.tableList = tableList;
    }

    public String getGarphIdForFact() {
        return garphIdForFact;
    }

    public void setGarphIdForFact(String garphIdForFact) {
        this.garphIdForFact = garphIdForFact;
    }

    public String getGarphIdsForFact() {
        return garphIdsForFact;
    }

    public void setGarphIdsForFact(String garphIdsForFact) {
        this.garphIdsForFact = garphIdsForFact;
    }

    public String getFolderIdsForFact() {
        return folderIdsForFact;
    }

    public void setFolderIdsForFact(String folderIdsForFact) {
        this.folderIdsForFact = folderIdsForFact;
    }

    public String getSelectedParameterIds() {
        return selectedParameterIds;
    }

    public void setSelectedParameterIds(String selectedParameterIds) {
        this.selectedParameterIds = selectedParameterIds;
    }

    public boolean isSummarizedMeasuresEnabled() {
        return tableProperties.summarizedMeasuresEnabled;
    }

    public void setSummarizedMeasuresEnabled(boolean summarizedMeasuresEnabled) {
        tableProperties.summarizedMeasuresEnabled = summarizedMeasuresEnabled;
    }

    public HashMap<String, ArrayList<String>> getSummerizedTableHashMap() {
        return reportCollect.summerizedTableHashMap;
    }

    public void setSummerizedTableHashMap(HashMap<String, ArrayList<String>> summerizedTableHashMap) {
        reportCollect.summerizedTableHashMap = summerizedTableHashMap;
    }

    public boolean isMaskZeros() {
        return tableProperties.MaskZerovalues;
    }

    public void setisMaskZeros(boolean isMaskZeros) {
        tableProperties.MaskZerovalues = isMaskZeros;
    }

    public boolean isSplitBy() {
        return isSplitBy;
    }

    public void setIsSplitBy(boolean isSplitBy) {
        this.isSplitBy = isSplitBy;
    }

    public String getSplitBy() {
        return splitBy;
    }

    public void setSplitBy(String splitBy) {
        this.splitBy = splitBy;
    }

    public ArrayList<String> getExplicitSortColumns() {
        return explicitSortColumns;
    }

    public void setExplicitSortColumns(ArrayList<String> explicitSortColumns) {
        this.explicitSortColumns = explicitSortColumns;
    }

    public char[] getExplicitSortTypes() {
        return explicitSortTypes;
    }

    public void setExplicitSortTypes(char[] explicitSortTypes) {
        this.explicitSortTypes = explicitSortTypes;
    }

    /**
     * @return the subTotalSort
     */
    public boolean isSubTotalSort() {
        return subTotalSort;
    }

    /**
     * @param subTotalSort the subTotalSort to set
     */
    public void setSubTotalSort(boolean subTotalSort) {
        this.subTotalSort = subTotalSort;
    }

    public void setSortColumnForSubTot(String sCol, String sType) {
        if (explicitSortColumns.contains(sCol)) {
            int indx = explicitSortColumns.indexOf(sCol);
            //code added by Amar
            explicitSortColumns.remove(indx);
            explicitSortColumns.add(sCol);
            ArrayList<Character> cList = new ArrayList<Character>();
            for (char c : explicitSortTypes) {
                cList.add(c);
            }
            cList.remove(indx);
            cList.add(sType.charAt(0));
            int i = 0;
            for (Character ch : cList) {
                explicitSortTypes[i] = ch;
                i++;
            }
            // end of code by Amar
        } else {
            int flag = 0;
            this.explicitSortColumns.clear();
            ArrayList hSortTypes = new ArrayList();
            for (int j = 0; j < (this.viewByElementIds.size() - 1); j++) {
                String viewbyId = "A_" + viewByElementIds.get(j).toString();
                if (explicitSortColumns != null && !explicitSortColumns.contains(viewbyId)) {
                    this.explicitSortColumns.add(viewbyId);
                    hSortTypes.add('0');
                }
            }
            if (!this.explicitSortColumns.contains(sCol)) {
                this.explicitSortColumns.add(sCol);
                flag++;
            }
            this.explicitSortTypes = new char[explicitSortColumns.size()];
            int k = 0;
            for (k = 0; k < hSortTypes.size(); k++) {
                this.explicitSortTypes[k] = hSortTypes.get(k).toString().charAt(0);
            }
            if (flag == 1) {
                this.explicitSortTypes[k] = sType.charAt(0);
            }

        }
    }

    public void increaseColCount(int count) {
        runtimeColCount += count;
    }

    public int getRunTimeColCount() {
        return this.runtimeColCount;
    }

    public void setrunTimeColCount(int runtimeColCount) {
        this.runtimeColCount = runtimeColCount;
    }

    public Object getAggregationType(String elemId) {
        return this.reportCollect.getAggrigationMap().get(elemId);
    }

    public Container getParentContainer(String parentReportId) {
        if (parentContainerMap != null) {
            return parentContainerMap.get(parentReportId);
        } else {
            return null;
        }
    }

    public void setParentContainerMap(String parentReportId, Container parentContainer) {
        this.parentContainerMap = new HashMap<String, Container>();
        this.parentContainerMap.put(parentReportId, parentContainer);
    }

    public void evaluateReportDateHeaders() {
        ArrayList<String> timeinfo = this.reportCollect.timeDetailsArray;
        String[] vals1 = null;
        String vals = " ";
        vals = timeinfo.toString();
        vals = vals.replace("[", "");
        vals = vals.replace("]", "");
        vals1 = vals.split(",");
        DateFormat formatter;
        String[] dates1 = new String[10];
        String[] dates = new String[10];
        String[] date0 = new String[10];
        String[] dats0 = new String[10];
        String[] cfdates = new String[10];
        String[] cfdates1 = new String[10];
        String[] ctdates = new String[10];
        String[] ctdates1 = new String[10];
        if (vals1[2].contains("/")) {
            date0 = vals1[2].trim().split("/");
        }
        if (vals1[2].contains("-")) {
            date0 = vals1[2].split("-");
//              dats0 = vals1[2].split("-");
            String values = dats0[0];
            String[] repdates = vals1[2].split("-");
            if (repdates[0].trim().toString().length() == 2) {
                date0[0] = repdates[0].trim();
                date0[1] = repdates[1].trim();
                date0[2] = repdates[2].trim();
            } else {
                date0[2] = repdates[0].trim();
                date0[0] = repdates[1].trim();
                date0[1] = repdates[2].trim();
            }
        }
        if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
            if (vals1[3].contains("/")) {
                dates = vals1[3].split("/");
            }
            if (vals1[4].contains("/")) {
                cfdates = vals1[4].split("/");
            }
            if (vals1[5].contains("/") && vals1[5] != "") {
                ctdates = vals1[5].split("/");
            }
            if (vals1[3].contains("-")) {
                dates1 = vals1[3].split(" ");
                String values = dates1[1];
                String[] repdates = values.split("-");
                if (repdates[0].trim().toString().length() == 2) {
                    dates[0] = repdates[0].trim();
                    dates[1] = repdates[1].trim();
                    dates[2] = repdates[2].trim();
                } else {
                    dates[2] = repdates[0].trim();
                    dates[0] = repdates[1].trim();
                    dates[1] = repdates[2].trim();
                }
            }
            if (vals1[4].contains("-") && !vals1[4].isEmpty()) {
                cfdates1 = vals1[4].split(" ");
                String values = cfdates1[1];
                String[] repdates = values.split("-");
                if (repdates[0].trim().toString().length() == 2) {
                    cfdates[0] = repdates[0].trim();
                    cfdates[1] = repdates[1].trim();
                    cfdates[2] = repdates[2].trim();
                } else {
                    cfdates[2] = repdates[0].trim();
                    cfdates[0] = repdates[1].trim();
                    cfdates[1] = repdates[2].trim();
                }
            }
            if (vals1[5].contains("-")) {
                ctdates1 = vals1[5].split(" ");
                String values = ctdates1[1].trim();
                String[] repdates = values.split("-");
                if (repdates[0].trim().toString().length() == 2) {
                    ctdates[0] = repdates[0].trim();
                    ctdates[1] = repdates[1].trim();
                    ctdates[2] = repdates[2].trim();
                } else {
                    ctdates[2] = repdates[0].trim();
                    ctdates[0] = repdates[1].trim();
                    ctdates[1] = repdates[2].trim();
                }
            }
        }
        //  dates[0];//month
        // dates[1];//day
        // dates[2];//year
        Calendar ca1 = Calendar.getInstance();
        if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
            ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].trim()) - 1, Integer.parseInt(date0[1]));
            java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
            String partialName0 = new SimpleDateFormat("MMM").format(d0);
            day0 = d0.toString().substring(0, 3);
            dated = date0[1];
            fullName0 = partialName0 + "'" + date0[2];
//            fullName0 =date0[2];
            // to date
            ca1.set(Integer.parseInt(dates[2].substring(0, 4)), Integer.parseInt(dates[0].trim()) - 1, Integer.parseInt(dates[1]));
            java.util.Date d = new java.util.Date(ca1.getTimeInMillis());
            String partialName = new SimpleDateFormat("MMM").format(d);
            day = d.toString().substring(0, 3);
            date = dates[1];
            fullName = partialName + "'" + dates[2];
//            fullName =dates[2];
            // compare from
            if (cfdates[0] != null && cfdates[1] != null && cfdates[2] != null) {
                ca1.set(Integer.parseInt(cfdates[2].substring(0, 4)), Integer.parseInt(cfdates[0].trim()) - 1, Integer.parseInt(cfdates[1]));
                java.util.Date cfd = new java.util.Date(ca1.getTimeInMillis());
                String cfName = new SimpleDateFormat("MMM").format(cfd);
                cfday = cfd.toString().substring(0, 3);
                cfdate = cfdates[1];
                cffullName = cfName + "'" + cfdates[2];
//                cffullName =cfdates[2];
            }
            // compare to
            if (ctdates[0] != null && ctdates[1] != null && ctdates[2] != null) {
                ca1.set(Integer.parseInt(ctdates[2].substring(0, 4)), Integer.parseInt(ctdates[0].trim()) - 1, Integer.parseInt(ctdates[1]));
                java.util.Date ctd = new java.util.Date(ca1.getTimeInMillis());
                String ctName = new SimpleDateFormat("MMM").format(ctd);
                ctday = ctd.toString().substring(0, 3);
                ctdate = ctdates[1];
                ctfullName = ctName + "'" + ctdates[2];
            }
        } else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD")) {
            // 
            //  ca1.set(Integer.parseInt( date0[2] .substring(0,4)), Integer.parseInt( date0[0].replace(" ",""))-1, Integer.parseInt(date0[1]));
            ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].trim()) - 1, Integer.parseInt(date0[1]));
            java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
            String partialName0 = new SimpleDateFormat("MMM").format(d0);
            day0 = d0.toString().substring(0, 3);
            dated = date0[1];
            fullName0 = partialName0 + "'" + date0[2];
            //fullName0 = date0[2];
        } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DAY_ROLLING")) {
            ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].trim()) - 1, Integer.parseInt(date0[1]));
            java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
            String partialName0 = new SimpleDateFormat("MMM").format(d0);
            day0 = d0.toString().substring(0, 3);
            dated = date0[1];
            fullName0 = partialName0 + "'" + date0[2];
//            fullName0=date0[2];
        }
    }

    /**
     * @return the graphCrossTabMeas
     */
    public String getGraphCrossTabMeas() {
        return graphCrossTabMeas;
    }

    /**
     * @param graphCrossTabMeas the graphCrossTabMeas to set
     */
    public void setGraphCrossTabMeas(String graphCrossTabMeas) {
        this.graphCrossTabMeas = graphCrossTabMeas;
    }

    public HashMap getparameterHash() {
        return paramhashmapPA;
    }

    public void setparametersHash(HashMap paramhashmapPA) {
        this.paramhashmapPA = paramhashmapPA;
    }

    public String getuserTypeAdmin() {
        return userTypeAdmin;
    }

    public void setuserTypeAdmin(String userTypeAdmin) {
        this.userTypeAdmin = userTypeAdmin;
    }

    public String getSymbol(String elementId) {
        return this.symbol.get(elementId);
    }

    public void setSymbol(String elementId, String symbol) {
        this.symbol.put(elementId, symbol);
    }

    public String getAlignment(String elementId) {
        return this.alignment.get(elementId);

    }

    public void setCollectObject(int i, PbReportCollection collect) {
        String filepath = null;
        String filename = null;
        PbReportCollection clonedCollectObject = new PbReportCollection();
        if (i != 5) {
            filepath = System.getProperty("java.io.tmpdir");
            filename = "CollectionDetails" + collect.reportId + "_" + i + "_" + System.currentTimeMillis() + ".txt";
            try {
                writeFileDetails(filepath, filename, collect);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            try {
                clonedCollectObject = (PbReportCollection) readFileDetails(filepath, filename);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            collectionhashmap.put(i, clonedCollectObject);
        } else {
            collectionhashmap.remove(0);
            collectionhashmap.put(i - 5, (PbReportCollection) collectionhashmap.get(i - 4));
            collectionhashmap.put(i - 4, (PbReportCollection) collectionhashmap.get(i - 3));
            collectionhashmap.put(i - 3, (PbReportCollection) collectionhashmap.get(i - 2));
            collectionhashmap.put(i - 2, (PbReportCollection) collectionhashmap.get(i - 1));
            collectionhashmap.put(i - 1, collect);

            //has to write code
        }
    }

    public void setAlignment(String elementId, String alignment) {
        this.alignment.put(elementId, alignment);
    }

    public String getFontcolor(String elementId) {
        return this.fontcolor.get(elementId);
    }

    public void setFontcolor(String elementId, String fontcolor) {
        this.fontcolor.put(elementId, fontcolor);
    }

    public String getBGcolor(String elementId) {
        return this.bgcolor.get(elementId);
    }

    public void setBGcolor(String elementId, String bgcolor) {
        this.bgcolor.put(elementId, bgcolor);
    }

    public String getNegative_val(String elementId) {
        return this.negative_val.get(elementId);
    }

    public void setNegative_val(String elementId, String negative_val) {
        this.negative_val.put(elementId, negative_val);
    }

    public String getNo_format(String elementId) {
        return this.no_format.get(elementId);
    }

    public void setNo_format(String elementId, String no_format) {
        this.no_format.put(elementId, no_format);
    }

    public String getRound(String elementId) {
        return this.round.get(elementId);
    }

    public void setRound(String elementId, String round) {
        this.round.put(elementId, round);
    }

    public String getDisplayFiltersGlobal() {
        return displayFiltersGlobal;
    }

    public void setDisplayFiltersGlobal(String displayFiltersGlobal) {
        this.displayFiltersGlobal = displayFiltersGlobal;
    }

    public void setBKPCollectObject(PbReportCollection repcollect) {
        collect = repcollect;
    }

    public PbReportCollection getBKPCollectObject() {
        return this.collect;
    }

    public void setfromBKP(String fromBkp) {
        fromBKP = fromBkp;
    }

    public String getFromBkp() {
        return this.fromBKP;
    }

    public void isPrevStateRep(boolean showRepPrevState) {
        showPrevState = showRepPrevState;
    }

    public void setRepPreStateCnt(int repPrevStateCnt) {
        prevStateCnt = repPrevStateCnt;
    }

    public boolean getaverageValue() {
        return averageValue;
    }

    public void setaverageValue(boolean averageValue) {
        this.averageValue = averageValue;
    }

    public String getgTAverage() {
        return gTAverage;
    }

    public void setgTAverage(String gTAverage) {
        this.gTAverage = gTAverage;
    }

    public String getsubGtVal(String elementId) {
        return this.subGtVal.get(elementId);
    }

    public void setsubGtVal(String elementId, String subGtVal) {
        this.subGtVal.put(elementId, subGtVal);
    }

    public String getGrandTotalSectionDisplay() {
        return GrandTotalSectionDisplay;
    }

    public void setGrandTotalSectionDisplay(String GrandTotalSectionDisplay) {
        this.GrandTotalSectionDisplay = GrandTotalSectionDisplay;
    }

    public boolean writeFileDetails(String filePath, String fileName, Object obj) throws Exception {
        boolean flag = false;
        File file = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
//        try{
        fos = new FileOutputStream(filePath + "/" + fileName);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.close();
        flag = true;
//        }catch(Exception e){
//            logger.error("Exception:",e);
//        }
        return flag;
    }

    public Object readFileDetails(String filePath, String fileName) throws Exception {
        Object redObj = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
//        try{
        fis = new FileInputStream(filePath + "/" + fileName);
        ois = new ObjectInputStream(fis);
        redObj = ois.readObject();
        ois.close();
        fis.close();
//        }catch(Exception e){
//            logger.error("Exception:",e);
//        }
        return redObj;
    }

    public HashMap getdisplayLabelmap(ArrayList QryColumns) {
        if (reportCollect.crosstabmeasureHashMap != null && reportCollect.crosstabmeasureHashMap.size() > 0) {
            this.Displaylabelmap = reportCollect.crosstabmeasureHashMap;
        } else {
            if (this.getDisplayColumns() != null && (this.Displaylabelmap == null || this.Displaylabelmap.size() == 0)) {
                for (int i = viewByCount; i < this.getDisplayColumns().size(); i++) {
                    if (QryColumns != null && QryColumns.contains(this.getDisplayColumns().get(i).replace("A_", ""))) {
                        this.Displaylabelmap.put(this.getDisplayColumns().get(i), this.getDisplayLabels().get(this.getDisplayColumns().indexOf(this.getDisplayColumns().get(i))).toString());
                    }
                }
            }
        }
        return this.Displaylabelmap;
    }

    public boolean isLockdataset(String elemId) {
        boolean locked = false;
        String elem = elemId.replace("A_", "").trim();
        if (this.reportCollect.lockdatasetmap != null && !this.reportCollect.lockdatasetmap.isEmpty()) {
            if (this.reportCollect.lockdatasetmap.get(elem) != null && !this.reportCollect.lockdatasetmap.get(elem).isEmpty()) {
                locked = Boolean.parseBoolean(this.reportCollect.lockdatasetmap.get(elem).toString());
            }
        }
        return locked;
    }

    public void setmodifymeasure(HashMap modifymeasure) {
        this.modifymeasure = modifymeasure;

    }

    public HashMap getmodifymeasure() {
        return modifymeasure;
    }

    public void setmodifymeasureAttrChnge(HashMap modifymeasureAttrChnge) {
        this.modifymeasureAttrChnge = modifymeasureAttrChnge;

    }

    public HashMap getmodifymeasureAttrChng() {
        return modifymeasureAttrChnge;
    }

    public void setrowSuffix(String suffix) {
        this.rowSuffix = suffix;

    }

    public String getrowSuffix() {
        return rowSuffix;
    }

    public String getTopBottomDispaly() {
        return topBottomDispaly;
    }

    public void setTopBottomDispaly(String topBottomDispaly) {
        this.topBottomDispaly = topBottomDispaly;
    }

    public void setisrenamed(String elementId, String renamed) {
        this.renamed.put(elementId, renamed);

    }

    public HashMap getisrenamed() {
        return renamed;
    }

    /**
     * @return the insightRetObj
     */
    public PbReturnObject getInsightRetObj() {
        return insightRetObj;
    }

    /**
     * @param insightRetObj the insightRetObj to set
     */
    public void setInsightRetObj(PbReturnObject insightRetObj) {
        this.insightRetObj = insightRetObj;
    }

    public PbReturnObject getdashretobj() {
        return dashretobj;
    }

    /**
     * @param insightRetObj the insightRetObj to set
     */
    public void setdashretobj(PbReturnObject dashretobj) {
        this.dashretobj = dashretobj;
    }
    //added by nazneen

    public void setdenomTable(String denomTable) {
        this.denomTable = denomTable;

    }

    public String getdenomTable() {
        return denomTable;
    }

    /**
     * @return the contextPath
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * @param contextPath the contextPath to set
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public boolean isQuickRefreshEnabled() {
        return quickRefreshEnabled;
    }

    public void setQuickRefreshEnabled(boolean quickRefreshEnabled) {
        this.quickRefreshEnabled = quickRefreshEnabled;
    }

    /**
     * @return the refreshCompareFlag
     */
    public boolean isRefreshCompareFlag() {
        return refreshCompareFlag;
    }

    /**
     * @param refreshCompareFlag the refreshCompareFlag to set
     */
    public void setRefreshCompareFlag(boolean refreshCompareFlag) {
        this.refreshCompareFlag = refreshCompareFlag;
    }

    /**
     * @return the facadePath
     */
    public String getFacadePath() {
        return facadePath;
    }

    /**
     * @param facadePath the facadePath to set
     */
    public void setFacadePath(String facadePath) {
        this.facadePath = facadePath;
    }

    /**
     * @return the quickautoRefresh
     */
    public boolean isQuickautoRefresh() {
        return quickautoRefresh;
    }

    /**
     * @param quickautoRefresh the quickautoRefresh to set
     */
    public void setQuickautoRefresh(boolean quickautoRefresh) {
        this.quickautoRefresh = quickautoRefresh;
    }

    /**
     * @return the currAndPriorValOfMsrMap
     */
    public HashMap<String, ArrayList<String>> getCurrAndPriorValOfMsrMap() {
        return currAndPriorValOfMsrMap;
    }

    /**
     * @param currAndPriorValOfMsrMap the currAndPriorValOfMsrMap to set
     */
    public void setCurrAndPriorValOfMsrMap(HashMap<String, ArrayList<String>> currAndPriorValOfMsrMap) {
        this.currAndPriorValOfMsrMap = currAndPriorValOfMsrMap;
    }

    public HashMap<String, ArrayList<String>> getnewUiFilter() {
        return newUiFilter;
    }

    /**
     * @param currAndPriorValOfMsrMap the currAndPriorValOfMsrMap to set
     */
    public void setnewUiFilter(HashMap<String, ArrayList<String>> newUiFilter) {
        this.newUiFilter = newUiFilter;
    }

    public void isRTMeasExists(boolean isrtmeasexists) {
        this.isRTMeasExists = isrtmeasexists;
    }

    /**
     * @return the isTopBottomTableEnable
     */
    public boolean isTopBottomTableEnable() {
        return getReportCollect().isTopBottomTableEnable;
    }

    /**
     * @param isTopBottomTableEnable the isTopBottomTableEnable to set
     */
    public void setTopBottomTableEnable(boolean isTopBottomTableEnable) {
        getReportCollect().isTopBottomTableEnable = isTopBottomTableEnable;
    }

    /**
     * @return the TopBottomTableHashMap
     */
    public HashMap<String, String> getTopBottomTableHashMap() {
        return reportCollect.TopBottomTableHashMap;
    }

    /**
     * @param TopBottomTableHashMap the TopBottomTableHashMap to set
     */
    public void setTopBottomTableHashMap(HashMap<String, String> TopBottomTableHashMap) {
        this.reportCollect.TopBottomTableHashMap = TopBottomTableHashMap;
    }

    /**
     * @return the MsrRetObj
     */
    public PbReturnObject getMsrRetObj() {
        return MsrRetObj;
    }

    /**
     * @param MsrRetObj the MsrRetObj to set
     */
    public void setMsrRetObj(PbReturnObject MsrRetObj) {
        this.MsrRetObj = MsrRetObj;
    }

    /**
     * @return the busTemplateFromOneview
     */
    public boolean isBusTemplateFromOneview() {
        return busTemplateFromOneview;
    }

    /**
     * @param busTemplateFromOneview the busTemplateFromOneview to set
     */
    public void setBusTemplateFromOneview(boolean busTemplateFromOneview) {
        this.busTemplateFromOneview = busTemplateFromOneview;
    }
    //start of code by Nazneen for sub total deviation

    public void setSubTotalDeviation(String elementId, String value) {
        reportCollect.subTotalDeviation.put(elementId, value);
    }

    public String getSubTotalDeviation(String elementId) {
        return reportCollect.subTotalDeviation.get(elementId);
    }
    //end of code by Nazneen for sub total deviation
    //added by Nazneen for Elements without kpi query

    public ArrayList<String> getEleWithoutKpiQry() {
        return withoutKpiQrycls;
    }

    public void setEleWithoutKpiQry(ArrayList<String> withoutKpiQrycls) {
        this.withoutKpiQrycls = withoutKpiQrycls;
    }

    public ArrayList<String> getEleWithKpiQry() {
        return withKpiQrycls;
    }

    public void setEleWithKpiQry(ArrayList<String> withKpiQrycls) {
        this.withKpiQrycls = withKpiQrycls;
    }
//public void setAvgcalculationtype(String Avgcalculation) {
//        tableProperties.Avgcalculationtype =Avgcalculation ;
//}
//public String getAvgcalculationtype() {
//        return tableProperties.Avgcalculationtype;
//    }
    public boolean getFlagg() {
        return Flagg;
    }

    public void setFlagg(boolean Flagg) {
        this.Flagg = Flagg;
    }

    public void setGTRowCntBeforSearchFilter(int GTValBeforSearchFilter) {
        this.GTValBeforSearchFilter = GTValBeforSearchFilter;
    }

    public int getGTRowCntBeforSearchFilter() {
        return this.GTValBeforSearchFilter;
    }

    public void setIsSearchFilterApplied(Boolean isSearchFilterApplied) {
        this.isSearchFilterApplied = isSearchFilterApplied;
    }

    public void setIsSubTotalSearchFilterApplied(Boolean isSearchFilterApplied) {
        this.setIsSubToTalSearchFilterApplied = isSearchFilterApplied;
    }

    public boolean getIsSearchFilterApplied() {
        return this.isSearchFilterApplied;
    }

    public boolean getIsSubToTalSearchFilterApplied() {
        return this.setIsSubToTalSearchFilterApplied;
    }

    public String getStdTimePeriodName(int column) {
        String columnName = displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
            Date date = null;
            final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                date = df.parse(ddate);
            } catch (ParseException ex) {
                logger.error("Exception:", ex);
            }
            final java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            if (columnName.contains("Prior")) {
                int prevMonthNum = -1;
                if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Period") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Complete Last Month")) {
                        cal.add(GregorianCalendar.MONTH, prevMonthNum);
                    } else {
                        cal.add(GregorianCalendar.YEAR, prevMonthNum);
                    }
                    String monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
                    String newColName = displayLabels.get(column).toString() + "(" + monthName + ")";
                    String replacedStr = displayLabels.get(column).toString();
                    columnName = (displayLabels.get(column).toString().replace(replacedStr, newColName));

                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Year") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Complete Last Year")) {
                        cal.add(GregorianCalendar.YEAR, prevMonthNum);
                        String monthName = String.valueOf( cal.get(java.util.Calendar.YEAR));
                        String newColName = displayLabels.get(column).toString() + "(" + monthName + ")";
                        String replacedStr = displayLabels.get(column).toString();
                        columnName = (displayLabels.get(column).toString().replace(replacedStr, newColName));
                    }
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                    String quarter = "";
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Complete Last Qtr")) {

                        int prevdayNum1 = prevMonthNum * 3;
                        cal.add(GregorianCalendar.MONTH, prevdayNum1);
                    } else {
                        cal.add(GregorianCalendar.YEAR, prevMonthNum);
                    }
                    int month1 = cal.get(Calendar.MONTH);
                    if (month1 < 3) {
                        quarter = "Q1";
                    } else if (month1 < 6) {
                        quarter = "Q2";
                    } else if (month1 < 9) {
                        quarter = "Q3";
                    } else {
                        quarter = "Q4";
                    }
                    String quarter1 = quarter + "-" + cal.get(java.util.Calendar.YEAR);
                    String newColName = displayLabels.get(column).toString() + "(" + quarter1 + ")";
                    String replacedStr = displayLabels.get(column).toString();
                    columnName = (displayLabels.get(column).toString().replace(replacedStr, newColName));

                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Previous Day")) {
                        cal.add(GregorianCalendar.DAY_OF_WEEK, prevMonthNum);
                        String day = new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
                        String newColName = displayLabels.get(column).toString() + "(" + day + ")";
                        String replacedStr = displayLabels.get(column).toString();
                        columnName = (displayLabels.get(column).toString().replace(replacedStr, newColName));
                    } else if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Same Day Last Month")) {
                    } else {
                    }
                }
            } else {
                String monthName = "";
                if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                    monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
//                    monthName = "" + cal.get(java.util.Calendar.YEAR);
                    monthName = String.valueOf( cal.get(java.util.Calendar.YEAR));
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                    String quarter = "";
                    int month1 = cal.get(Calendar.MONTH);
                    if (month1 < 3) {
                        quarter = "Q1";
                    } else if (month1 < 6) {
                        quarter = "Q2";
                    } else if (month1 < 9) {
                        quarter = "Q3";
                    } else {
                        quarter = "Q4";
                    }
                    monthName = quarter + "-" + cal.get(java.util.Calendar.YEAR);
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                }
                String newColName = displayLabels.get(column).toString() + "(" + monthName + ")";
                String replacedStr = displayLabels.get(column).toString();
                columnName = (displayLabels.get(column).toString().replace(replacedStr, newColName));
            }
        }
        return columnName;
    }

    public String getMonthNameforTrailingFormula(int column) {
        String columnName = displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String actualName = displayLabels.get(column).toString();
        // String tralingFormName = actualName.substring(7,actualName.length()).replaceAll("(", "").replaceAll(")", "").trim();
        String monthNum = "-" + actualName.split("-")[1].replace(")", "").trim();
        int prevMonthNum = Integer.parseInt(monthNum);
        cal.add(GregorianCalendar.MONTH, prevMonthNum);
        String monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
        int strtPos = displayLabels.get(column).toString().indexOf("(");
        int endPos = displayLabels.get(column).toString().indexOf(")");
        String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
        columnName = (displayLabels.get(column).toString().replace(replacedStr, monthName));
//                            }else{
//                   columnName=displayLabels.get(column).toString();
//                }

        return columnName;
    }
//add by sruthi for prev year
    public String getQuaterNameforTimeFormula(int column) {
        //start of code by bhargavi
        String columnName = displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        if (displayLabels.get(column).toString().contains("YTD")) {

            columnName = cal.get(java.util.Calendar.YEAR) + " till " + new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)];
            columnName = "YTD-" + columnName;
            int strtPos = displayLabels.get(column).toString().indexOf("(");
            int endPos = displayLabels.get(column).toString().indexOf(")");
            String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
            columnName = (displayLabels.get(column).toString().replace(replacedStr, columnName));
            //columnName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.YEAR)]+"till"+cal.get(java.util.Calendar.MONTH);
        } else if (displayLabels.get(column).toString().contains("CYD")) {

//            columnName = "" + cal.get(java.util.Calendar.YEAR);// + " till " + new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)];
            columnName = String.valueOf(cal.get(java.util.Calendar.YEAR));
            int strtPos = displayLabels.get(column).toString().indexOf("(");
            int endPos = displayLabels.get(column).toString().indexOf(")");
            String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
            columnName = (displayLabels.get(column).toString().replace(replacedStr, columnName));

        } else if (displayLabels.get(column).toString().contains("QTD")) {

            String quarter = "";
            int month1 = cal.get(Calendar.MONTH);
            if (month1 < 3) {
                quarter = "Q1";
            } else if (month1 < 6) {
                quarter = "Q2";
            } else if (month1 < 9) {
                quarter = "Q3";
            } else {
                quarter = "Q4";
            }
            String quarter12 = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
            String quarter13 = quarter12.split("-")[1].replace(")", "").trim();
            String quarter1 = quarter + "-" + quarter13;
            int strtPos = displayLabels.get(column).toString().indexOf("(");
            int endPos = displayLabels.get(column).toString().indexOf(")");
            String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
            columnName = (displayLabels.get(column).toString().replace(replacedStr, quarter1));

        } else if (displayLabels.get(column).toString().contains("MTD")) {

            String monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
            int strtPos = displayLabels.get(column).toString().indexOf("(");
            int endPos = displayLabels.get(column).toString().indexOf(")");
            String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
            columnName = (displayLabels.get(column).toString().replace(replacedStr, monthName));

        } else if (displayLabels.get(column).toString().contains("Prior ytd")) {

            int prevyearNum = -1;
            cal.add(GregorianCalendar.YEAR, prevyearNum);
            columnName = cal.get(java.util.Calendar.YEAR) + " till " + new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)];
            columnName = "YTD-" + columnName;
            int strtPos = displayLabels.get(column).toString().indexOf("(");
            int endPos = displayLabels.get(column).toString().indexOf(")");
            String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
            columnName = (displayLabels.get(column).toString().replace(replacedStr, columnName));
            //columnName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.YEAR)]+"till"+cal.get(java.util.Calendar.MONTH);
        }
        return columnName;
        //end of code by bhargavi
    }

    public String getYearNameforTrailingFormula(int column) {
        String columnName = displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String yearNum = "-" + columnName.split("-")[1].replace(")", "").trim();
        int prevyearNum = Integer.parseInt(yearNum);
        cal.add(GregorianCalendar.YEAR, prevyearNum);
//        String year = "" + cal.get(java.util.Calendar.YEAR);
        String year = String.valueOf( cal.get(java.util.Calendar.YEAR));
        // int year = new cal.get(java.util.Calendar.MONTH)+"-"+DateFormatSymbols().getYear()[cal.get(java.util.Calendar.YEAR)];
        int strtPos = displayLabels.get(column).toString().indexOf("(");
        int endPos = displayLabels.get(column).toString().indexOf(")");
        String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
        columnName = (displayLabels.get(column).toString().replace(replacedStr, year));
//                }else{
//                columnName=displayLabels.get(column).toString();
//                }
        return columnName;
    }//ended by sruthi
//start of code by bhargavi

    public String getDayNameforTrailingFormula(int column) {
        String columnName = displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String DayNum = "-" + columnName.split("-")[1].replace(")", "").trim();
        int prevdayNum = Integer.parseInt(DayNum);
        cal.add(GregorianCalendar.DAY_OF_WEEK, prevdayNum);
        String day = new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
//int day1 = cal.get(java.util.Calendar.DATE);
        int strtPos = displayLabels.get(column).toString().indexOf("(");
        int endPos = displayLabels.get(column).toString().indexOf(")");
        String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
        columnName = (displayLabels.get(column).toString().replace(replacedStr, day));
//}else{
//                columnName=displayLabels.get(column).toString();
//                }
        return columnName;
    }

    public String getQuaterNameforTrailingFormula(int column) {
        String columnName = displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String DayNum = "-" + columnName.split("-")[1].replace(")", "").trim();
        int prevdayNum = Integer.parseInt(DayNum);
        String quarter = "";
        int prevdayNum1 = prevdayNum * 3;
        cal.add(GregorianCalendar.MONTH, prevdayNum1);
        int month1 = cal.get(Calendar.MONTH);
        if (month1 < 3) {
            quarter = "Q1";
        } else if (month1 < 6) {
            quarter = "Q2";
        } else if (month1 < 9) {
            quarter = "Q3";
        } else {
            quarter = "Q4";
        }
        String quarter12 = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
        String quarter13 = quarter12.split("-")[1].replace(")", "").trim();
        String quarter1 = quarter + "-" + quarter13;
        int strtPos = displayLabels.get(column).toString().indexOf("(");
        int endPos = displayLabels.get(column).toString().indexOf(")");
        String replacedStr = displayLabels.get(column).toString().substring(strtPos + 1, endPos);
        columnName = (displayLabels.get(column).toString().replace(replacedStr, quarter1));
//}else{
//                columnName=displayLabels.get(column).toString();
//                }
        return columnName;
    }
//end of code by bhargavi
//    /**
//     * @return the importExcelRetObj
//     */
//    public PbReturnObject getImportExcelRetObj() {
//        return importExcelDetails.getReturnObject();
//    }
//
//    /**
//     * @param importExcelRetObj the importExcelRetObj to set
//     */
//    public void setImportExcelRetObj(PbReturnObject importExcelRetObj) {
//        this.importExcelRetObj = importExcelRetObj;
//    }

    public void setaveragecalculationtype(String colName, String averagecalculationtype) {
        if (averagecalculationtype != null && !averagecalculationtype.equalsIgnoreCase("null")) {
            this.avgCalCMap.put(colName, averagecalculationtype);
        } else {
            this.avgCalCMap.put(colName, defavgcalculationtype);
        }

    }

    public String getaveragecalculationtype(String colName) {
        if (this.avgCalCMap.get(colName) != null && !this.avgCalCMap.get(colName).equalsIgnoreCase("null")) {
            return this.avgCalCMap.get(colName);
        } else {
            return this.defavgcalculationtype;
        }
    }
// public void setcrosscolmap1(HashMap<String,String> crosscolmap) {
//        this.crosscolmap = crosscolmap;
//    }

//  public HashMap getcrosscolmap1() {
//        return crosscolmap;
//    }
    public HashMap getSTcrosscolmap() {
        return STcrosscolmap;
    }

    public void setSTcrosscolmap(HashMap STcrosscolmap1) {
        this.STcrosscolmap = STcrosscolmap1;
    }

    public void setcrosscolmap1(String colName, String averagecalculationtype) {
        reportCollect.crosscolmap1.put(colName, averagecalculationtype);
    }

    public HashMap getcrosscolmap1() {
        return reportCollect.crosscolmap1;
    }

    /**
     * @return the excelFilePath
     */
    public String getExcelFilePath() {
        return excelFilePath;
    }

    /**
     * @param excelFilePath the excelFilePath to set
     */
    public void setExcelFilePath(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    public void setOthersData(ArrayList<Object> rowData) {
        this.rowData = rowData;
    }

    public ArrayList<Object> getOthersData() {
        return rowData;
    }

    public boolean isOthersRequired() {
        return isOthersFlag;
    }

    public void setisOthersRequired(boolean isOthersFlag) {
        this.isOthersFlag = isOthersFlag;
    }

    public boolean isrowRenameTotal() {
        return tableProperties.isRowGrandTotalRenamed;
    }

    public void setRowGTRenameTotal(boolean rowGTrenameTotal) {
        tableProperties.isRowGrandTotalRenamed = rowGTrenameTotal;
    }

    public String getOriginalRowTotalName() {
        return tableProperties.OriginalRowTotalName;
    }

    public void setOriginalRowTotalName(String originalTotalName) {
        tableProperties.OriginalRowTotalName = originalTotalName;
    }

    public String getRowRenamedTotalName() {
        return tableProperties.RowRenamedTotalName;
    }

    public void setRowRenamedTotalName(String renamedTotalName) {
        tableProperties.RowRenamedTotalName = renamedTotalName;
    }
    //adde by Nazneen in Mar14

    public void setFilesPath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilesPath() {
        return this.filePath;
    }

    /**
     * @return the renameDetails
     */
    public HashMap<String, String> getRenameDetails() {
        return tableProperties.renameDetails;
    }

    public void addRenameDetails(String originalTotalName, String renamedTotalName) {
        tableProperties.renameDetails.put(originalTotalName, renamedTotalName);
    }
    // start of code  bhargavi on 1/05/14

    public int gettrwcnt() {
        return this.trwcnt;
    }

    public void settrwcnt(int trwcnt) {
        this.trwcnt = trwcnt;
    }

    public String getsorttype() {
        return this.stype;
    }

    public void setsorttype(String stype) {
        this.stype = stype;
    }
    // end of code  bhargavi on 1/05/14

    /**
     * @return the subTotalTopBottomCount
     */
    public int getSubTotalTopBottomCount() {
        return subTotalTopBottomCount;
    }

    /**
     * @param subTotalTopBottomCount the subTotalTopBottomCount to set
     */
    public void setSubTotalTopBottomCount(int subTotalTopBottomCount) {
        this.subTotalTopBottomCount = subTotalTopBottomCount;
    }

    /**
     * @return the isSubtotalTopBottom
     */
    public boolean isIsSubtotalTopBottom() {
        return isSubtotalTopBottom;
    }

    /**
     * @param isSubtotalTopBottom the isSubtotalTopBottom to set
     */
    public void setIsSubtotalTopBottom(boolean isSubtotalTopBottom) {
        this.isSubtotalTopBottom = isSubtotalTopBottom;
    }

    /**
     * @return the subTotalSrchColumns
     */
    public ArrayList<String> getSubTotalSrchColumns() {
        return subTotalSrchColumns;
    }

    /**
     * @param subTotalSrchColumns the subTotalSrchColumns to set
     */
    public void setSubTotalSrchColumns(ArrayList<String> subTotalSrchColumns) {
        this.subTotalSrchColumns = subTotalSrchColumns;
    }

    /**
     * @return the subTotalSrchCondition
     */
    public ArrayList<String> getSubTotalSrchCondition() {
        return subTotalSrchCondition;
    }

    /**
     * @return the subTotalSrchValue
     */
    public ArrayList<String> getSubTotalSrchValue() {
        return subTotalSrchValue;
    }

    //added by Amar
    public ArrayList<String> setViewByList() {

        ArrayList<String> tempViewByList = new ArrayList<String>();
        for (int j = 0; j < (this.viewByElementIds.size() - 1); j++) {
            String viewbyId = "A_" + viewByElementIds.get(j).toString();
            if (tempViewByList != null && !tempViewByList.contains(viewbyId)) {
                tempViewByList.add(viewbyId);
            }
        }
        return tempViewByList;
    }
    //end by Amar

    public int getTotalRowCount() {
        return this.trwcnte;
    }
//added by Govardhan for ST Filter

    public void setTotalRowCount(int trwcnte) {
        this.trwcnte = trwcnte;
    }

    /**
     * @return the subTotalRowCount
     */
    public int getSubTotalRowCount() {
        return subTotalRowCount;
    }

    /**
     * @param subTotalRowCount the subTotalRowCount to set
     */
    public void setSubTotalRowCount(int subTotalRowCount) {
        this.subTotalRowCount = subTotalRowCount;
    }

    /**
     * @return the TotalRowCountAfterSubFilter
     */
    public int getTotalRowCountAfterSubFilter() {
        return TotalRowCountAfterSubFilter;
    }

    /**
     * @param TotalRowCountAfterSubFilter the TotalRowCountAfterSubFilter to set
     */
    public void setTotalRowCountAfterSubFilter(int TotalRowCountAfterSubFilter) {
        this.TotalRowCountAfterSubFilter = TotalRowCountAfterSubFilter;
    }

    /**
     * @return the PageAndRowsMap
     */
    public HashMap<Integer, Integer> getPageAndRowsMap() {
        return PageAndRowsMap;
    }

    /**
     * @param PageAndRowsMap the PageAndRowsMap to set
     */
    public void setPageAndRowsMap(HashMap<Integer, Integer> PageAndRowsMap) {
        this.PageAndRowsMap = PageAndRowsMap;
    }

    public void updatePageAndRowsMap(Integer page, Integer count) {
        this.PageAndRowsMap.put(page, count);
    }

    /**
     * @return the TotalNumberOfPages
     */
    public int getTotalNumberOfPages() {
        return TotalNumberOfPages;
    }

    /**
     * @param TotalNumberOfPages the TotalNumberOfPages to set
     */
    public void setTotalNumberOfPages(int TotalNumberOfPages) {
        this.TotalNumberOfPages = TotalNumberOfPages;
    }

    /**
     * @return the CurrentpageSartRowCount
     */
    public int getCurrentpageSartRowCount() {
        return CurrentpageSartRowCount;
    }

    /**
     * @param CurrentpageSartRowCount the CurrentpageSartRowCount to set
     */
    public void setCurrentpageSartRowCount(int CurrentpageSartRowCount) {
        this.CurrentpageSartRowCount = CurrentpageSartRowCount;
    }

    /**
     * @return the TotatRowsCountedAfterSubFilter
     */
    public boolean isTotatRowsCountedAfterSubFilter() {
        return TotatRowsCountedAfterSubFilter;
    }

    /**
     * @param TotatRowsCountedAfterSubFilter the TotatRowsCountedAfterSubFilter
     * to set
     */
    public void setTotatRowsCountedAfterSubFilter(boolean TotatRowsCountedAfterSubFilter) {
        this.TotatRowsCountedAfterSubFilter = TotatRowsCountedAfterSubFilter;
    }

    public Set<String> getDistinctViewBys() {
        return DistinctViewBys;
    }

    public void setDistinctViewBys(Set<String> DistinctViewBys) {
        this.DistinctViewBys = DistinctViewBys;
    }
    //ended by Govardhan for ST Filter
    //added by bhargavi for double sort

    public void setTopBottomColumn1(String topBtmType, String topBtmMode, int topBtmCount, String topBtmcolNam) {
        tableProperties.topBtmCount = topBtmCount;
        tableProperties.topBtmType = topBtmType;
        tableProperties.topBtmMode = topBtmMode;
        tableProperties.topBtmcolNam = topBtmcolNam;
    }

    public String getTopBottomClmId() {
        return tableProperties.topBtmcolNam;
    }
    //ended by bhargavi for double sort
    //added by bhargavi for refresh rowcount

    public void setRepInNewtab(String NewTabValue) {
        this.NewTabValue = NewTabValue;
    }

    /**
     * @return the HideMsrHeading
     */
    public boolean isHideMsrHeading() {
        return tableProperties.HideMsrHeading;
    }

    /**
     * @param HideMsrHeading the HideMsrHeading to set
     */
    public void setHideMsrHeading(boolean HideMsrHeading) {
        tableProperties.HideMsrHeading = HideMsrHeading;
    }
//ended by bhargavi

    public void setIsPercentColumnwithAbsolute(String elementId, String isPercentColumnAbsolute) {
        if (isPercentColumnwithAbsolute != null && !isPercentColumnwithAbsolute.containsKey(elementId.replace("A_", ""))) {
            this.isPercentColumnwithAbsolute.put(elementId, isPercentColumnAbsolute);
        }
    }

    public String getIsPercentColumnwithAbsolute(String elementId) {
        if (isPercentColumnwithAbsolute != null && isPercentColumnwithAbsolute.containsKey(elementId.replace("A_", ""))) {
            return this.isPercentColumnwithAbsolute.get(elementId);
        } else {
            return "false";
        }

    }

    public String getStdTimePeriodNameOnColName(String columnName) {

        String replacedStr = columnName;
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
            Date date = null;
            final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                date = df.parse(ddate);
            } catch (ParseException ex) {
                logger.error("Exception:", ex);
            }
            final java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            if (columnName.contains("Prior")) {
                int prevMonthNum = -1;
                if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Period") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Complete Last Period")) {
                        cal.add(GregorianCalendar.MONTH, prevMonthNum);
                    } else {
                        cal.add(GregorianCalendar.YEAR, prevMonthNum);
                    }
                    String monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
                    String newColName = columnName + "(" + monthName + ")";
                    columnName = (columnName.replace(replacedStr, newColName));

                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Year") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Complete Last Year")) {
                        cal.add(GregorianCalendar.YEAR, prevMonthNum);
//                        String monthName = "" + cal.get(java.util.Calendar.YEAR);
                        String monthName = String.valueOf( cal.get(java.util.Calendar.YEAR));
                        String newColName = columnName + "(" + monthName + ")";
                        columnName = (columnName.toString().replace(replacedStr, newColName));
                    }
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                    String quarter = "";
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Complete Last Qtr")) {

                        int prevdayNum1 = prevMonthNum * 3;
                        cal.add(GregorianCalendar.MONTH, prevdayNum1);
                    } else {
                        cal.add(GregorianCalendar.YEAR, prevMonthNum);
                    }
                    int month1 = cal.get(Calendar.MONTH);
                    if (month1 < 3) {
                        quarter = "Q1";
                    } else if (month1 < 6) {
                        quarter = "Q2";
                    } else if (month1 < 9) {
                        quarter = "Q3";
                    } else {
                        quarter = "Q4";
                    }
                    String quarter1 = quarter + "-" + cal.get(java.util.Calendar.YEAR);
                    String newColName = columnName + "(" + quarter1 + ")";
                    columnName = (columnName.replace(replacedStr, newColName));

                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Previous Day")) {
                        cal.add(GregorianCalendar.DAY_OF_WEEK, prevMonthNum);
                        String day = new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
                        String newColName = columnName + "(" + day + ")";
                        columnName = (columnName.replace(replacedStr, newColName));
                    } else if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Same Day Last Month")) {
                    } else {
                    }
                }
            } else {
                String monthName = "";
                if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                    monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
//                    monthName = "" + cal.get(java.util.Calendar.YEAR);
                    monthName = String.valueOf( cal.get(java.util.Calendar.YEAR));
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                    String quarter = "";
                    int month1 = cal.get(Calendar.MONTH);
                    if (month1 < 3) {
                        quarter = "Q1";
                    } else if (month1 < 6) {
                        quarter = "Q2";
                    } else if (month1 < 9) {
                        quarter = "Q3";
                    } else {
                        quarter = "Q4";
                    }
                    monthName = quarter + "-" + cal.get(java.util.Calendar.YEAR);
                } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                }
                String newColName = columnName + "(" + monthName + ")";
                columnName = (columnName.replace(replacedStr, newColName));
            }
        }
        return columnName;
    }
    //start of code by Nazneen for showing Previous 1 Month etc as Month Name

    public String getMonthNameforTrailingFormulaOnColName(String columnName) {
//    String columnName=displayLabels.get(column).toString();

        if (!columnName.contains("-")) {
            return columnName;
        }
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            String ddate = timeDetailsArray.get(2).toString();
            Date date = null;
            final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                date = df.parse(ddate);
            } catch (ParseException ex) {
                logger.error("Exception:", ex);
            }
            final java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            String actualName = columnName;
            // String tralingFormName = actualName.substring(7,actualName.length()).replaceAll("(", "").replaceAll(")", "").trim();
            String monthNum = "-" + actualName.split("-")[1].replace(")", "").trim();
            int prevMonthNum = Integer.parseInt(monthNum);
            cal.add(GregorianCalendar.MONTH, prevMonthNum);
            String monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
            int strtPos = columnName.indexOf("(");
            int endPos = columnName.indexOf(")");
            String replacedStr = columnName.substring(strtPos + 1, endPos);
            columnName = (columnName.replace(replacedStr, monthName));
        } else {
            columnName = columnName;
        }
        return columnName;
    }
    //end of code by Nazneen for showing Previous 1 Month etc as Month Name

    public String getDayNameforTrailingFormulaOnColName(String columnName) {
        //  String columnName=displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String DayNum = "-" + columnName.split("-")[1].replace(")", "").trim();
        int prevdayNum = Integer.parseInt(DayNum);
        cal.add(GregorianCalendar.DAY_OF_WEEK, prevdayNum);
        String day = new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
//int day1 = cal.get(java.util.Calendar.DATE);
        int strtPos = columnName.indexOf("(");
        int endPos = columnName.indexOf(")");
        String replacedStr = columnName.substring(strtPos + 1, endPos);
        columnName = (columnName.replace(replacedStr, day));

        return columnName;
    }

    public String getYearNameforTrailingFormulaOnColName(String columnName) {
        if (!columnName.contains("-")) {
            return columnName;
        }
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String yearNum = "-" + columnName.split("-")[1].replace(")", "").trim();
        int prevyearNum = Integer.parseInt(yearNum);
        cal.add(GregorianCalendar.YEAR, prevyearNum);
        String year = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
        // int year = new cal.get(java.util.Calendar.MONTH)+"-"+DateFormatSymbols().getYear()[cal.get(java.util.Calendar.YEAR)];
        int strtPos = columnName.indexOf("(");
        int endPos = columnName.indexOf(")");
        String replacedStr = columnName.substring(strtPos + 1, endPos);
        columnName = (columnName.replace(replacedStr, year));
        return columnName;
    }

    public String getQuaterNameforTrailingFormulaOnColName(String columnName) {
        //String columnName=displayLabels.get(column).toString();
        String ddate;
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String DayNum = "-" + columnName.split("-")[1].replace(")", "").trim();
        int prevdayNum = Integer.parseInt(DayNum);
        String quarter = "";
        int prevdayNum1 = prevdayNum * 3;
        cal.add(GregorianCalendar.MONTH, prevdayNum1);
        int month1 = cal.get(Calendar.MONTH);
        if (month1 < 3) {
            quarter = "Q1";
        } else if (month1 < 6) {
            quarter = "Q2";
        } else if (month1 < 9) {
            quarter = "Q3";
        } else {
            quarter = "Q4";
        }
        String quarter12 = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
        String quarter13 = quarter12.split("-")[1].replace(")", "").trim();
        String quarter1 = quarter + "-" + quarter13;
        int strtPos = columnName.indexOf("(");
        int endPos = columnName.indexOf(")");
        String replacedStr = columnName.substring(strtPos + 1, endPos);
        columnName = (columnName.replace(replacedStr, quarter1));
//}else{
//                columnName=displayLabels.get(column).toString();
//                }
        return columnName;
    }

    public String getQuaterNameforTimeFormulaOnColName(String columnName) {
        //start of code by bhargavi
        // String columnName = displayLabels.get(column).toString();
        String columnName1;
        String ddate = "";
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(2).toString();
        } else {
            ddate = timeDetailsArray.get(3).toString();
        }
        Date date = null;
        final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = df.parse(ddate);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        if (columnName.contains("YTD")) {

            //String yearNum = "-"+columnName.split("-")[1].replace(")", "").trim();
            // int prevyearNum = Integer.parseInt(yearNum);
            // cal.add(GregorianCalendar.YEAR,prevyearNum);
            columnName1 = cal.get(java.util.Calendar.YEAR) + " till " + new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)];
            columnName1 = "YTD-" + columnName1;
            int strtPos = columnName.toString().indexOf("(");
            int endPos = columnName.indexOf(")");
            String replacedStr = columnName.substring(strtPos + 1, endPos);
            columnName = (columnName.replace(replacedStr, columnName1));
            //columnName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.YEAR)]+"till"+cal.get(java.util.Calendar.MONTH);
        } else if (columnName.toString().contains("CYD")) {


            columnName1 = String.valueOf( cal.get(java.util.Calendar.YEAR));// + " till " + new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)];
            int strtPos = columnName.indexOf("(");
            int endPos = columnName.indexOf(")");
            String replacedStr = columnName.toString().substring(strtPos + 1, endPos);
            columnName = (columnName.toString().replace(replacedStr, columnName1));
        } else if (columnName.toString().contains("QTD")) {


            String quarter = "";
            int month1 = cal.get(Calendar.MONTH);
            if (month1 < 3) {
                quarter = "Q1";
            } else if (month1 < 6) {
                quarter = "Q2";
            } else if (month1 < 9) {
                quarter = "Q3";
            } else {
                quarter = "Q4";
            }
            String quarter12 = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
            String quarter13 = quarter12.split("-")[1].replace(")", "").trim();
            String quarter1 = quarter + "-" + quarter13;
            int strtPos = columnName.indexOf("(");
            int endPos = columnName.indexOf(")");
            String replacedStr = columnName.substring(strtPos + 1, endPos);
            columnName = (columnName.replace(replacedStr, quarter1));

        } else if (columnName.toString().contains("MTD")) {


            String monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)] + "-" + cal.get(java.util.Calendar.YEAR);
            int strtPos = columnName.indexOf("(");
            int endPos = columnName.indexOf(")");
            String replacedStr = columnName.substring(strtPos + 1, endPos);
            columnName = (columnName.replace(replacedStr, monthName));
        } else if (columnName.toString().contains("Prior ytd")) {

            int prevyearNum = -1;
            cal.add(GregorianCalendar.YEAR, prevyearNum);
            columnName1 = cal.get(java.util.Calendar.YEAR) + " till " + new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)];
            columnName1 = "YTD-" + columnName1;
            int strtPos = columnName.toString().indexOf("(");
            int endPos = columnName.toString().indexOf(")");
            String replacedStr = columnName.substring(strtPos + 1, endPos);
            columnName = (columnName.replace(replacedStr, columnName1));
            //columnName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.YEAR)]+"till"+cal.get(java.util.Calendar.MONTH);
        }
        return columnName;
    }
    //start of code by Nazneen for GT as Avg for Sum Measures

    public void setCTGtAggType(String measName, String value) {
        String elementId = "";
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) this.getRetObj()).crosstabMeasureId;
        if (crosstabMeasureId != null && crosstabMeasureId.containsKey(measName)) {
            elementId = crosstabMeasureId.get(measName);
            reportCollect.gtCTAvgType.put(elementId, value);
            this.kpiRetObj.gtCTAvgType.put(elementId, value);
        }
    }

    public String getCTGtAggType(String elementId) {
        if (reportCollect.gtCTAvgType != null && reportCollect.gtCTAvgType.containsKey(elementId)) {
            return reportCollect.gtCTAvgType.get(elementId);
        } else {
            return "";
        }
    }
    //end of code by Nazneen for GT as Avg for Sum Measures
    //start of code by bhargavi for colGT as Avg for Sum Measures

    public void setCTcolGtAggType(String measName, String value) {
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) this.getRetObj()).crosstabMeasureId;
        if (crosstabMeasureId != null && crosstabMeasureId.containsKey(measName)) {
            tableProperties.CTcolGtAggType.put(measName, value);
        }
    }

    public String getCTcolGtAggType(String measName) {
        if (tableProperties.CTcolGtAggType != null && tableProperties.CTcolGtAggType.containsKey(measName)) {
            return tableProperties.CTcolGtAggType.get(measName);
        } else {
            return "";
        }
    }
    //end of code by bhargavi for colGT as Avg for Sum Measures
    //added by sruthi otherval for running total on 14 Nov 14

    public void setOtherValue(HashMap<String, BigDecimal> othervalue) {
        this.othervalues = othervalue;

    }

    public HashMap<String, BigDecimal> getOtherValue() {
        return othervalues;
    }

    public void setRunningTotal(ArrayList<BigDecimal> runningvalues) {
        this.runningval = runningvalues;
    }

    public ArrayList<BigDecimal> getRunningTotal() {
        return runningval;
    }
    //ended by sruthi on 14 Nov 14
    //added by sruthi for setpath after scheduling 22/12/2014

    public String getfilepath() {
        return filepath1;
    }

    public void setfilepath(String filepath) {
        this.filepath1 = filepath;
    }
    //ended by sruthi

    public void setIskpidasboard(boolean iskpidasboard) {
        this.iskpidasboard = iskpidasboard;
    }

    public boolean isIskpidasboard() {
        return iskpidasboard;
    }

    public void setIsTimedasboard(boolean IsTimedasboard) {
        this.IsTimedasboard = IsTimedasboard;
    }

    public boolean IsTimedasboard() {
        return IsTimedasboard;
    }
    // Added by Amar to set intermediate filters

    public void setIsIntermediateFilters(boolean isItr) {
        this.isItermediateFiletrs = isItr;
    }

    public boolean getIsIntermediateFilters() {
        return this.isItermediateFiletrs;
    }

    public void setIntermediateFilters(HashMap inMap) {
        this.inMap = inMap;
    }

    public HashMap<String, List> getIntermediateFilters() {
        return this.inMap;
    }

//graphs Data
    public HashMap<String, List<Map<String, String>>> getChartData() {
        return chartData;
    }

    public void setChartData(HashMap<String, List<Map<String, String>>> chartData) {
        this.chartData = chartData;
    }

    public static Map<String, Map<String, String>> getDbData() {
        return dbData;
    }

    public static void setDbData(Map<String, Map<String, String>> aDbData) {
        dbData = aDbData;
    }

    public  Map<String, List<Map<String, String>>> getDbrdData() {
        return dbrdData;
    }

    public  void setDbrdData(Map<String, List<Map<String, String>>> adbrdData) {
        dbrdData = adbrdData;
    }

    public static Map<String, Map<String, String>> getTrendData() {
        return trendData;
    }

    public static void setTrendData(Map<String, Map<String, String>> trendData1) {
        trendData = trendData1;
    }

    public void setReportMeta(XtendReportMeta reportmeta) {
        reportMeta = reportmeta;
    }

    public XtendReportMeta getReportMeta() {
        return reportMeta;
    }

    public void setTrendReportMeta(XtendReportMeta reportTrendmeta) {
        reportTrendMeta = reportTrendmeta;
    }

    public XtendReportMeta getTrendReportMeta() {
        return reportTrendMeta;
    }

    public void setVisualData(Map<String, List<Double>> visualData) {
        this.visualData = visualData;
    }

    public Map<String, List<Double>> getVisualData() {
        return visualData;
    }

    public OverlayData getOverlayData() {
        return overlayData;
    }

    public void setOverlayData(OverlayData overlayData) {
        this.overlayData = overlayData;
    }

    public Map<String, Map<String, List<String>>> getTreeData() {
        return treeData;
    }

    public void setTreeData(Map<String, Map<String, List<String>>> treeData) {
        this.treeData = treeData;
    }

    public void setSummerizedGt(HashMap summerizedgt) {
        this.summerizedgtval = summerizedgt;
    }

    public HashMap getSummerizedGt() {
        return summerizedgtval;
    }

    public void sethidegtzero1(ArrayList<String> hidetablemeasures) {
        tableProperties.grandtotalZero = hidetablemeasures;
    }

    public ArrayList<String> gethidegtzero1() {
        return tableProperties.grandtotalZero;
    }

    public void setGrandTotalZero(boolean grandzero) {
        tableProperties.grandtotalzero = grandzero;
    }

    public boolean getGrandTotalZero() {
        return tableProperties.grandtotalzero;
    }
//ended by sruhti
    public void setCountOfviewbys(int count) {
        this.graphviewByCount = count;
    }

    public int getCountOfviewbys() {
        return this.graphviewByCount;
    }

//added by sruthi to display the full query
    public void setReportQuery(String query) {
        this.reportquery = query;
    }

    public String getReportQuery() {
        return this.reportquery;
    }
    //ended by sruhti
    //added by sruthi for  onthefly modifymeasures.

    public void setupdate(boolean flag) {
        this.flag = flag;
    }

    public boolean getupdate() {
        return this.flag;
    }

    public void setupdateAggregation(HashMap modifymeasureAttrChnge) {
        this.updateaggregation = modifymeasureAttrChnge;

    }

    public HashMap getupdateAggregation() {
        return updateaggregation;
    }
//ended by sruthi
//start of code by Bhargavi for font formats and row coloring in kpi on 26th Dec 2014

    public void setFontFormatsType(String MsrID, HashMap<String, String> formats) {
        tableProperties.fontFormats.put(MsrID, formats);
    }

    public HashMap getFontFormatsType(String measName) {
        return tableProperties.fontFormats.get(measName);
    }

    public HashMap getFontFormatsHashMap() {
        return tableProperties.fontFormats;
    }

    public void setrowcolorForkpi(String colname, String color) {
        tableProperties.rowcolorkpi.put(colname, color);
    }

    public String getrowcolorForkpi(String measName) {
        return tableProperties.rowcolorkpi.get(measName);
    }

    public HashMap getrowcolorForkpiHashMap() {
        return tableProperties.rowcolorkpi;
    }

    public void setRowText(String msrId, String value) {
        tableProperties.rowTextMap.put(msrId, value);
    }

    public HashMap getRowText() {
        return tableProperties.rowTextMap;
    }
//end of code by Bhargavi

    public void setQryColumns(ArrayList<String> measureIdsList) {
        this.measureIdsList = measureIdsList;
    }

//      public void setTimePeriodsList(ArrayList<String> timePeriodsList, ArrayList<String> aggType, ArrayList<String> DistinctTimePeriods, ArrayList<String> DistinctMesList ){
//
//          this.timePeriodsList = timePeriodsList;
//          this.aggType = aggType;
//          this.DistinctTimePeriods = DistinctTimePeriods;
//          this.DistinctMesList = DistinctMesList;
//      }
    public void setTimePeriodsList(ArrayList<String> timePeriodsList) {

        this.timePeriodsList = timePeriodsList;

    }

    public void setDistinctTimePeriodsList(ArrayList<String> DistinctTimePeriods) {

        this.DistinctTimePeriods = DistinctTimePeriods;

    }

    public void setDisplayColsfortimeDB(String[] DisplayColsfortimeDB) {
        this.DisplayColsfortimeDB = DisplayColsfortimeDB;
    }

    public String[] getDisplayColsfortimeDB() {
        return this.DisplayColsfortimeDB;
    }

    public void setallMesChange(HashMap<String, BigDecimal> allMesChange) {
        this.allMesChange = allMesChange;
    }

    public HashMap<String, BigDecimal> getallMesChange() {
        return allMesChange;
    }

    public void setChangeColumns(ArrayList<String> disColumns) {
        this.ChangeColumns = (ArrayList<String>) disColumns.clone();


    }

    public ArrayList getChangeColumns() {
        return this.ChangeColumns;
    }

    public void setAllColumns(ArrayList<String> disColumns) {
        this.AllColumns = (ArrayList<String>) disColumns.clone();


    }

    public ArrayList getAllColumns() {
        return this.AllColumns;
    }

    public void setViewBy(ArrayList<String> ViewBy) {
        this.ViewBy = (ArrayList<String>) ViewBy.clone();


    }

    public ArrayList getViewBy() {
        return this.ViewBy;
    }

    public void setqfilters(ArrayList<String> qfilters) {
        this.qfilters = (ArrayList<String>) qfilters.clone();
        tableProperties.qfilters = this.qfilters;

    }

    public ArrayList getqfilters() {
        return this.qfilters;
    }
    //Added By Amar to reset SubTotal Top bottom

    public void resetSubTotalTopBottom() {
        tableProperties.subTotalTopBtmCount = 0;
        tableProperties.subTotalTopBtmType = ContainerConstants.TOP_BOTTOM_TYPE_NONE;
        tableProperties.subTotalTopBtmMode = null;
    }
    // end of code

    /**
     * @return the timeDetailsMap
     */
    public Map<String, String> getTimeDetailsMap() {
        return timeDetailsMap;
    }

    /**
     * @param timeDetailsMap the timeDetailsMap to set
     */
    public void setTimeDetailsMap(Map<String, String> timeDetailsMap) {
        this.timeDetailsMap = timeDetailsMap;
    }

    /**
     * @return the displayGraphLabels
     */
    public ArrayList getDisplayGraphLabels() {
        return displayGraphLabels;
    }

    /**
     * @param displayGraphLabels the displayGraphLabels to set
     */
    public void setDisplayGraphLabels(ArrayList displayGraphLabels) {
        this.displayGraphLabels = displayGraphLabels;
    }
    //added by sruthi for numberformate

    public void setNumberFormatHeader(boolean numberFormatHeader) {
        tableProperties.numberFormatHeader = numberFormatHeader;
    }

    public boolean getNumberFormatHeader() {
        return tableProperties.numberFormatHeader;
    }

    public void setNumberHeader(boolean numberheader) {
        this.numberheader = numberheader;
    }

    public boolean getNumberHeader() {
        return this.numberheader;
    }
    //ended by sruthi

    //Added By Ram
    public List<ArrayList<String>> getCalculativeVal() {
        return calculativeVal;
    }

    public void setCalculativeVal(List<ArrayList<String>> calculativeVal) {
        this.calculativeVal = calculativeVal;
    }
    //Ended By Ram

    public void setDefaultCompanyId(String def_comp) {
        this.Def_Comp = def_comp;
    }

    public String getDefaultCompanyId() {
        return this.Def_Comp;
    }

    public void setCompanyChangedFlag(boolean chnged) {
        this.compChnged = chnged;
    }

    public boolean getCompanyChangedFlag() {
        return this.compChnged;
    }
    //added by Dinanath for elementId and element mesure name
    private String kpiElementIds = null;
    private String kpiElementMesureName = null;
    private String customkpiheads = null;

    public void setKPIElementIds(String kpiElementIds) {
        this.kpiElementIds = kpiElementIds;
    }

    public String getKPIElementIds() {
        return this.kpiElementIds;
    }

    public void setKPIElementMesureName(String kpiElementMesureName) {
        this.kpiElementMesureName = kpiElementMesureName;
    }

    public String getKPIElementMesureName() {
        return this.kpiElementMesureName;
    }

    public void setCustomKPIHeaderNames(String customkpiheads) {
        this.customkpiheads = customkpiheads;
    }

    public String getCustomKPIHeaderNames() {
        return this.customkpiheads;
    }
//end of code by Dinanath

    /**
     * @return the gtVal
     */
    public List<ArrayList<String>> getGtVal() {
        return gtVal;
    }

    /**
     * @param gtVal the gtVal to set
     */
    public void setGtVal(List<ArrayList<String>> gtVal) {
        this.gtVal = gtVal;
    }

    /**
     * @return the changeFlag
     */
    public boolean isChangeFlag() {
        return changeFlag;
    }

    /**
     * @param changeFlag the changeFlag to set
     */
    public void setChangeFlag(boolean changeFlag) {
        this.changeFlag = changeFlag;
    }
    //added by sruthi for custom header

    public void setCustomHeader(HashMap<String, String> customheader) {
        tableProperties.customHeader = customheader;
    }

    public HashMap<String, String> getCustomHeader() {
        return tableProperties.customHeader;
    }
    //ended by sruthi

    public boolean isShowStTimePeriod() {
        return tableProperties.isStTimePeriodReq;
    }

    public void setStTimePeriod(boolean stTimePeriod) {
        tableProperties.isStTimePeriodReq = stTimePeriod;
    }
    //added by sruthi for tablecolumn pro

    public void setTableColumnProperties(HashMap<String, ArrayList<String>> tablecolumnproperties) {
        tableProperties.tablecolumnproperties = tablecolumnproperties;
    }

    public HashMap<String, ArrayList<String>> getTableColumnProperties() {
        return tableProperties.tablecolumnproperties;
    }

    public void setMeasurAlis(boolean measurename) {
        this.measurealis = measurename;
    }

    public boolean getMeasurAlis() {
        return measurealis;
    }
//ended by sruthi

    //Added by Ram 
    public String getLookupViewBys() {
        return lookupViewBys;
    }

    public void setLookupViewBys(String lookupViewBys) {
        this.lookupViewBys = lookupViewBys;
    }
    //Endded by Ram
    //added by sruthi for multicalendar for reports

    public void setMultiCalendarHashMap(HashMap multiCalendarDetails1) {
        this.multiCalendarDetails = multiCalendarDetails1;
        this.reportCollect.setcollectMulticalender(multiCalendarDetails);
    }

    public HashMap getMultiCalendarHashMap() {
        return multiCalendarDetails;
    }

    public void setMultiCalendarFlag(boolean data) {
        tableProperties.multiflag = data;
    }

    public boolean getMultiCalendarFlag() {
        return tableProperties.multiflag;
    }
    //ended by sruthi

    public void setSelectedviewby(ArrayList<String> selectedviewbys) {
        tableProperties.selectedviews = selectedviewbys;
    }

    public ArrayList<String> getSelectedviewby() {
        return tableProperties.selectedviews;
    }

    /**
     * @return the ONClickInformation
     */
    public String getONClickInformation() {
        return tableProperties.ONClickInformation;
    }

    /**
     * @param ONClickInformation the ONClickInformation to set
     */
    public void setONClickInformation(String ONClickInformation) {
        tableProperties.ONClickInformation = ONClickInformation;
    }

    /**
     * @return the ElementIdvalue
     */
    public String getElementIdvalue() {
        return tableProperties.ElementIdvalue;
    }

    /**
     * @param ElementIdvalue the ElementIdvalue to set
     */
    public void setElementIdvalue(String ElementIdvalue) {
        tableProperties.ElementIdvalue = ElementIdvalue;
    }
//sandeep

    public void setNewUIyr(String year) {
        tableProperties.NewUiyr = year;
    }

    public String getNewUIyr() {
        return tableProperties.NewUiyr;
    }

    public void setNewUiqr(String year) {
        tableProperties.NewUiqr = year;
    }

    public String getNewUiqr() {
        return tableProperties.NewUiqr;
    }
    //Added By Mohit Gupta for GT and subTotal BG color.

    public void setGrandTotalBGColor(String grandTotalBGColor) {
        tableProperties.grandTotalBGColor = grandTotalBGColor;
    }

    public String getGrandTotalBGColor() {
        return tableProperties.grandTotalBGColor;
    }

    public void setSubTotalBGColor(String subTotalBGColor) {
        tableProperties.subTotalBGColor = subTotalBGColor;
    }

    public String getSubTotalBGColor() {
        return tableProperties.subTotalBGColor;
    }

    //Added By manoj 
    public void setHeaderBgColor(String headerBgColor) {
        tableProperties.headerBgColor = headerBgColor;
    }

    public String getHeaderBgColor() {
        return tableProperties.headerBgColor;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }

    /**
     * @return the reSetColor
     */
    public String getReSetColor() {
        return reSetColor;
    }

    /**
     * @param reSetColor the reSetColor to set
     */
    public void setReSetColor(String reSetColor) {
        this.reSetColor = reSetColor;
    }

    public String getAOId() {
        return this.reportCollect.AOId;
    }

    public void setAOId(String reportId) {
        this.reportCollect.AOId = reportId;
        this.AOId = reportId;
    }

    public String getAOName() {
        return this.reportCollect.aoName;
    }

    public void setAOName(String aoName) {
        this.reportCollect.aoName = aoName;
    }
    //added by sruthi for showfilters

    public void setshowfilters(String filterno) {
        tableProperties.filterNo = filterno;
    }

    public String getshowfilters() {
        return tableProperties.filterNo;
    } //ended by sruthi

    public HashMap<String, String> getFilterLookupData() {
        return filterLookupData;
    }

    /**
     * @param filterLookupData the filterLookupData to set
     */
    public void setFilterLookupData(HashMap<String, String> filterLookupData) {
        this.filterLookupData = filterLookupData;
    }
    //added by sruthi for prior icons

    public void setPriorEnableFlag(boolean data) {
        tableProperties.Priorflag = data;
    }

    public boolean getPriorEnableFlag() {
        return tableProperties.Priorflag;
    }//ended by sruthi

    /**
     * @return the filterLookupOriginalToNew
     */
    public HashMap<String, String> getFilterLookupOriginalToNew() {
        return filterLookupOriginalToNew;
    }

    /**
     * @param filterLookupOriginalToNew the filterLookupOriginalToNew to set
     */
    public void setFilterLookupOriginalToNew(HashMap<String, String> filterLookupOriginalToNew) {
        this.filterLookupOriginalToNew = filterLookupOriginalToNew;
    }

    /**
     * @return the initilizeFilterElement
     */
    public HashMap<String, ArrayList<String>> getInitilizeFilterElement() {
        return initilizeFilterElement;
    }

    /**
     * @param initilizeFilterElement the initilizeFilterElement to set
     */
    public void setInitilizeFilterElement(HashMap<String, ArrayList<String>> initilizeFilterElement) {
        this.initilizeFilterElement = initilizeFilterElement;
    }

    /**
     * @return the reportDrill
     */
    public boolean isReportDrill() {
        return reportDrill;
    }

    /**
     * @param reportDrill the reportDrill to set
     */
    public void setReportDrill(boolean reportDrill) {
        this.reportDrill = reportDrill;
    }

    public void setSaveAsNewRepFlag(boolean flag) {
        this.saveAsNewRep = flag;
    }

    public boolean getSaveAsNewRepFlag() {
        return this.saveAsNewRep;
    }

    public String getPercentWiseTable() {
        return percentWiseTable;
    }

    /**
     * @param percentWiseTable the percentWiseTable to set
     */
    public void setPercentWiseTable(String percentWiseTable) {
        this.percentWiseTable = percentWiseTable;
    }

    public String getPercentWiseTableName() {
        return percentWiseTableName;
    }

    public String getSubtotalsort() {
        return subtotalsort;
    }

    public String getclearInformation() {
        return tableProperties.clearInformation;
    }

    /**
     * @param ONClickInformation the ONClickInformation to set
     */
    public void setclearInformation(String clearInformation) {
        tableProperties.clearInformation = clearInformation;
    }

    /**
     * @param Idsubtotal the Idsubtotal to set
     */
    public void setSubtotalsort(String subtotalsort) {
        this.subtotalsort = subtotalsort;
    }

    public String getIdsubtotal() {
        return Idsubtotal;
    }

    /**
     * @param Idsubtotal the Idsubtotal to set
     */
    public void setIdsubtotal(String Idsubtotal) {
        this.Idsubtotal = Idsubtotal;
    }

    /**
     * @param percentWiseTableName the percentWiseTableName to set
     */
    public void setPercentWiseTableName(String percentWiseTableName) {
        this.percentWiseTableName = percentWiseTableName;
    }
    //added by sruthi for alldetails

    public void setAllDetails(ArrayList<String> alldetailsarr) {
        tableProperties.alldetailslist = alldetailsarr;
    }

    public ArrayList<String> getAllDetails() {
        return tableProperties.alldetailslist;
    } //ended by sruthi
    //added by Dinanath for editAO
    private boolean isFromAOEdit = false;

    public void setIsFromAOEdit(boolean isFromAOEdit1) {
        this.isFromAOEdit = isFromAOEdit1;
    }

    public boolean getIsFromAOEdit() {
        return this.isFromAOEdit;
    }

    public void setdateenable(boolean dateenable) {
        this.dateenable = dateenable;
        tableProperties.dateenable = dateenable;
    }

    public boolean getdateenable() {
        return this.dateenable;
    }

     public void setenableComp(boolean enableComp) {
        this.enableComp = enableComp;
        tableProperties.enableComp = enableComp;
    }

    public boolean getenableComp() {
        return this.enableComp;
    }

    /**
     * @return the NoOfScheduleReport
     */
    public int getNoOfScheduleReport() {
        return NoOfScheduleReport;
    }

    /**
     * @param NoOfScheduleReport the NoOfScheduleReport to set
     */
    public void setNoOfScheduleReport(int NoOfScheduleReport) {
        this.NoOfScheduleReport = NoOfScheduleReport;
    }
    //added by anitha for MTD,QTD,YTD in AO report
    public void setRunTimeComparisionMsr(HashMap RunTimeComparisionMsrMap) {
        tableProperties.isRunTimeComparisionMsr = RunTimeComparisionMsrMap;
}

    public HashMap<String, ArrayList<String>> getRunTimeComparisionMsr() {
        return tableProperties.isRunTimeComparisionMsr;
    }

    public HashMap getRunTimeComparisionMsrHashMap() {
        return  tableProperties.RunTimeComparisionMsrHashMap;
    }
    public void setRunTimeComparisionMsrHashMap(HashMap RunTimeComparisionMsrHashMap) {
        tableProperties.RunTimeComparisionMsrHashMap = RunTimeComparisionMsrHashMap;
    }
    //end of code by anitha for MTD,QTD,YTD in AO report
    
    //added by anitha for RT Time Agg compate with
    public String getHeaderNameforRTMeasure(int column,Map timeDefinition) {        
        String columnName = displayLabels.get(column).toString();        
        if(timeDefinition!=null &&!timeDefinition.isEmpty()){
        if (displayLabels.get(column).toString().contains("PYTD") ||  displayLabels.get(column).toString().contains("_PYtdrank")) {            
            String name = (String) timeDefinition.get("PYTD");
            columnName = columnName.replace("PYTD",name);
        }else if (displayLabels.get(column).toString().contains("PQTD")||  displayLabels.get(column).toString().contains("_PQtdrank")) {            
            String name = (String) timeDefinition.get("PQTD");
            columnName = columnName.replace("PQTD",name);
        }else if (displayLabels.get(column).toString().contains("PMTD")||  displayLabels.get(column).toString().contains("_PMtdrank")) {
            String name = (String) timeDefinition.get("PMTD");
            columnName = columnName.replace("PMTD",name);
        }else if (displayLabels.get(column).toString().contains("PYMTD")) {
            String name = (String) timeDefinition.get("PYMTD");
            columnName = columnName.replace("PYMTD",name);
        }else if (displayLabels.get(column).toString().contains("PYQTD")) {
            String name = (String) timeDefinition.get("PYQTD");
            columnName = columnName.replace("PYQTD",name);
        }else if (displayLabels.get(column).toString().contains("YTD")|| displayLabels.get(column).toString().contains("_Ytdrank")) {
           String name = (String) timeDefinition.get("YTD");
            columnName = columnName.replace("YTD",name);
        }else if (displayLabels.get(column).toString().contains("QTD")|| displayLabels.get(column).toString().contains("_Qtdrank")) {            
            String name = (String) timeDefinition.get("QTD");
            columnName = columnName.replace("QTD",name);
        }else if (displayLabels.get(column).toString().contains("MTD")) {           
            String name = (String) timeDefinition.get("MTD");
            columnName = columnName.replace("MTD",name);
        }else if (displayLabels.get(column).toString().contains("PWTD")) {           
            String name = (String) timeDefinition.get("PWTD");
            columnName = columnName.replace("PWTD", name);
        }else if (displayLabels.get(column).toString().contains("PYWTD")) {           
            String name = (String) timeDefinition.get("PYWTD");
            columnName = columnName.replace("PYWTD", name);
        }else if (displayLabels.get(column).toString().contains("WTD")) {           
            String name = (String) timeDefinition.get("WTD");
            columnName = columnName.replace("WTD", name);
        }
        }
        return columnName;        
    }
    //end of code by anitha for RT Time Agg compate with
    
    public void setsearch(String search){
        tableProperties.searchdata=search;
    }
    public String getsearch(){
        return tableProperties.searchdata;
    }
     public void setGroupColumn1(String sortColumn) {
         tableProperties.groupColumns =  sortColumn;
    }

    public String getGroupColumns1() {
        return tableProperties.groupColumns;
    }
    //added by sruthi for background color in tablecolumn pro
 public void setMeasureBgColor(String colName, String measurebgcolor) {
        if(tableProperties.measurebgColor==null){
            tableProperties.measurebgColor = new HashMap<>();
        }
        tableProperties.measurebgColor.put(colName, measurebgcolor);
    }
     public String getMeasureBgColor(String element) {
        String measurebgcolor = "";
        if (tableProperties.measurebgColor != null && !tableProperties.measurebgColor.isEmpty()) {
            if (tableProperties.measurebgColor.get(element.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "")) != null) {
                measurebgcolor = tableProperties.measurebgColor.get(element.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", ""));
            }
        }
        return measurebgcolor;
    }//ended by sruthi
     
     //added by anitha
     public void setrtMeasureCompareWith(String colName, String viewbydata) {
        if(tableProperties.rtMeasureCompareWith==null)
            tableProperties.rtMeasureCompareWith = new HashMap<>();
        tableProperties.rtMeasureCompareWith.put(colName, viewbydata);
    }

    public String getrtMeasureCompareWith(String viewbyid) {
        String rtMeasureCompareWith = "";
        if (tableProperties.rtMeasureCompareWith!=null &&tableProperties.rtMeasureCompareWith.get(viewbyid) != null) {
            rtMeasureCompareWith = tableProperties.rtMeasureCompareWith.get(viewbyid);
        }
        return rtMeasureCompareWith;
    }
    //end of code by anitha
     public Map<String,String> getGlobalDateFlagMap() {
		return globalDateFlagMap;
	}

	public void setGlobalDateFlagMap(Map<String,String> globalDateFlagMap) {
		this.globalDateFlagMap = globalDateFlagMap;
	}

	public String getDateClause() {
		return dateClause;
	}

	public void setDateClause(String dateClause) {
		this.dateClause = dateClause;
	}
        //added by anitha
    public String getHeaderNameforAONormalMeasure(int column,Map timeDefinition,String globalDateFlagForheader) { 
        String ddate = "";
        String columnNameDisp = displayLabels.get(column).toString(); 
        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ddate = timeDetailsArray.get(3).toString();
}
        String columnName = "";
        if(globalDateFlagForheader!=null &&globalDateFlagForheader.equalsIgnoreCase("yes")){
        if(ddate!=null &&ddate.equalsIgnoreCase("Month")){
           columnName = (String) timeDefinition.get("MTD");
           columnName = columnNameDisp+" ("+columnName+")";
        }else if(ddate!=null &&ddate.equalsIgnoreCase("Qtr")){               
           columnName = columnName+(String) timeDefinition.get("QTD"); 
           columnName = columnNameDisp+" ("+columnName+")";
        }else if(ddate!=null &&ddate.equalsIgnoreCase("Year")){               
           columnName = (String) timeDefinition.get("YTD"); 
           columnName = columnNameDisp+" ("+columnName+")";
        }else if(ddate!=null &&ddate.equalsIgnoreCase("Week")){               
           columnName = (String) timeDefinition.get("WTD");
           columnName = columnNameDisp+" ("+columnName+")";
        }else{
           columnName = displayLabels.get(column).toString();
        }}else{
           columnName = displayLabels.get(column).toString();
        }  
        if(columnName==null&& columnName.equalsIgnoreCase("")){
           columnName = displayLabels.get(column).toString();
        }
        return columnName;  
}
    //end of code by anitha 

    /**
     * @return the globalDateFlagForheader
     */
    public String getGlobalDateFlagForheader() {
        return globalDateFlagForheader;
    }

    /**
     * @param globalDateFlagForheader the globalDateFlagForheader to set
     */
    public void setGlobalDateFlagForheader(String globalDateFlagForheader) {
        this.globalDateFlagForheader = globalDateFlagForheader;
    }

    /**
     * @return the percentWise
     */
    public String getPercentWise() {
        return percentWise;
}

    /**
     * @param percentWise the percentWise to set
     */
    public void setPercentWise(String percentWise) {
        this.percentWise = percentWise;
    }
    
    public HashMap<String,ArrayList<String>> getadvdatalist(){
        return this.advdatalist;
    }
    public void setadvdatalist(HashMap<String,ArrayList<String>> filterdata){
        this.advdatalist=filterdata;
    }
}
