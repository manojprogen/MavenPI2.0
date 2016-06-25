/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import com.progen.report.KPIElement;
import com.progen.report.data.DataFacade;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author progen
 */
public class OnceViewContainer implements Serializable {

    public List<OneViewLetDetails> onviewLetdetails = new ArrayList<OneViewLetDetails>();
    public String oneviewName;
    public String oneviewId;
    public int heigth = 200;
    public int width = 1400;
    private int rows;
    private int columns;
    public List<String> timedetails = new ArrayList<String>();
    private static final long serialVersionUID = 75264711556227L;
    private String elementIds;
    private String elementNames;
    private LinkedHashMap icalDetailsHashmap;
    private String formatList;
    private String roundList;
    private String dispNameList;
    private String applyFormatValues;
    private String comparisionType;
    private String primaryMeasure;
    private String roleId;
    private String viewType;
    private String nodays;
    private String monthlyCal;
    private String containerFileName;
    private String settingType;
    private String settingDate;
    private boolean everyTimeUpdate;
    private String refreshString;
    public HashMap timeHashMap = new HashMap();
    public HashMap regHashMap;
    private HashMap tempRegHashMap;
    private String pdfPageType;
    private String pdfPageFit;
    public String height;
    private LinkedHashMap reportParameterValues;
    private String filterBusinessRole;
    public ArrayList paramIds = new ArrayList();
    public ArrayList paramName = new ArrayList();
    private ArrayList tableList = new ArrayList();
    public String iconVisibility = "";
    private String advHtmlFileProps;
    private String oldAdvHtmlFileProps;
    private String oneViewVersion;
    private String userId;
    private HashMap GraphSizesDtlsHashMap = new HashMap();
    private HashMap<String, String> jqpToJfNameMap = new HashMap<String, String>();
    private Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
    private Map<String, List<String>> filterMap;
    private Map<String, List<String>> allFiltersnames = new HashMap<String, List<String>>();
    ArrayList viewbygblname = new ArrayList();
    ArrayList parameterlist = new ArrayList();
    private HashMap jqpMap = new HashMap();
    private String contextPath;
    private Container container;
    private String oneViewType;
    private String istrasnspose;
    private DataFacade facade;
    private List<KPIElement> kpiElements = new ArrayList<KPIElement>();
    public String day0 = null;
    public String dated = null;
    public String fullName0 = null;
    public String day = null;
    public String date = null;
    public String fullName = null;
    public String datetype = null;
    public String cfday = null;
    public String cfdate = null;
    public String cffullName = null;
    public String ctday = null;
    public String ctdate = null;
    public String ctfullName = null;
    private ArrayList<String> filterParameters = new ArrayList<String>();

    public OnceViewContainer() {
    }

    public void addDashletDetail(OneViewLetDetails detail) {
        this.onviewLetdetails.add(detail);
    }

    public void addTimeDetails(String timedetails) {
        this.timedetails.add(timedetails);
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public String getElementIds() {
        return elementIds;
    }

    public void setElementIds(String elementIds) {
        this.elementIds = elementIds;
    }

    public String getElementNames() {
        return elementNames;
    }

    public void setElementNames(String elementNames) {
        this.elementNames = elementNames;
    }

    public LinkedHashMap getIcalDetailsHashmap() {
        return icalDetailsHashmap;
    }

    public void setIcalDetailsHashmap(LinkedHashMap icalDetailsHashmap) {
        this.icalDetailsHashmap = icalDetailsHashmap;
    }

    public String getFormatList() {
        return formatList;
    }

    public void setFormatList(String formatList) {
        this.formatList = formatList;
    }

    public String getApplyFormatValues() {
        return applyFormatValues;
    }

    public void setApplyFormatValues(String applyFormatValues) {
        this.applyFormatValues = applyFormatValues;
    }

    public String getRoundList() {
        return roundList;
    }

    public void setRoundList(String roundList) {
        this.roundList = roundList;
    }

    public String getComparisionType() {
        return comparisionType;
    }

    public void setComparisionType(String comparisionType) {
        this.comparisionType = comparisionType;
    }

    public String getPrimaryMeasure() {
        return primaryMeasure;
    }

    public void setPrimaryMeasure(String primaryMeasure) {
        this.primaryMeasure = primaryMeasure;
    }

    public String getDispNameList() {
        return dispNameList;
    }

    public void setDispNameList(String dispNameList) {
        this.dispNameList = dispNameList;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getNodays() {
        return nodays;
    }

    public void setNodays(String nodays) {
        this.nodays = nodays;
    }

    public String getMonthlyCal() {
        return monthlyCal;
    }

    public void setMonthlyCal(String monthlyCal) {
        this.monthlyCal = monthlyCal;
    }

    /**
     * @return the containerFileName
     */
    public String getContainerFileName() {
        return containerFileName;
    }

    /**
     * @param containerFileName the containerFileName to set
     */
    public void setContainerFileName(String containerFileName) {
        this.containerFileName = containerFileName;
    }

    /**
     * @return the settingType
     */
    public String getSettingType() {
        return settingType;
    }

    /**
     * @param settingType the settingType to set
     */
    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    /**
     * @return the settingDate
     */
    public String getSettingDate() {
        return settingDate;
    }

    /**
     * @param settingDate the settingDate to set
     */
    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    /**
     * @return the everyTimeUpdate
     */
    public boolean isEveryTimeUpdate() {
        return everyTimeUpdate;
    }

    /**
     * @param everyTimeUpdate the everyTimeUpdate to set
     */
    public void setEveryTimeUpdate(boolean everyTimeUpdate) {
        this.everyTimeUpdate = everyTimeUpdate;
    }

    public void setRefreshString(String refreshString) {
        this.refreshString = refreshString;
    }

    public void SetRegHashMap(HashMap regHashMap) {
        this.regHashMap = regHashMap;
    }

    /**
     * @return RegionFileNames
     */
    public HashMap getRegHashMap() {
        return regHashMap;
    }

    public void SetTempRegHashMap(HashMap regHashMap) {
        this.tempRegHashMap = regHashMap;
    }

    /**
     * @return the TemporaryRegionFileNames
     */
    public HashMap getTempRegHashMap() {
        return tempRegHashMap;
    }

    /**
     * @return the pdfPageType
     */
    public String getPdfPageType() {
        return pdfPageType;
    }

    /**
     * @param pdfPageType the pdfPageType to set
     */
    public void setPdfPageType(String pdfPageType) {
        this.pdfPageType = pdfPageType;
    }

    /**
     * @return the pdfPageFit
     */
    public String getPdfPageFit() {
        return pdfPageFit;
    }

    /**
     * @param pdfPageFit the pdfPageFit to set
     */
    public void setPdfPageFit(String pdfPageFit) {
        this.pdfPageFit = pdfPageFit;
    }

    public LinkedHashMap getReportParameterValues() {
        return reportParameterValues;
    }

    public void setReportParameterValues(LinkedHashMap reportParameterValues) {
        this.reportParameterValues = reportParameterValues;
    }

    public String getFilterBusinessRole() {
        return filterBusinessRole;
    }

    public void setFilterBusinessRole(String filterBusinessRole) {
        this.filterBusinessRole = filterBusinessRole;
    }

    public ArrayList getParamIds() {
        return this.paramIds;
    }

    public void setParamIds(ArrayList paramId) {
        this.paramIds = paramId;
    }

    public ArrayList getParamNames() {
        return this.paramName;
    }

    public void setParamNames(ArrayList paramName) {
        this.paramName = paramName;
    }

    /**
     * @return the advHtmlFileProps
     */
    public String getAdvHtmlFileProps() {
        return advHtmlFileProps;
    }

    /**
     * @param advHtmlFileProps the advHtmlFileProps to set
     */
    public void setAdvHtmlFileProps(String advHtmlFileProps) {
        this.advHtmlFileProps = advHtmlFileProps;
    }

    /**
     * @return the oldAdvHtmlFileProps
     */
    public String getOldAdvHtmlFileProps() {
        return oldAdvHtmlFileProps;
    }

    /**
     * @param oldAdvHtmlFileProps the oldAdvHtmlFileProps to set
     */
    public void setOldAdvHtmlFileProps(String oldAdvHtmlFileProps) {
        this.oldAdvHtmlFileProps = oldAdvHtmlFileProps;
    }

    /**
     * @return the oneViewVersion
     */
    public String getOneViewVersion() {
        return oneViewVersion;
    }

    /**
     * @param oneViewVersion the oneViewVersion to set
     */
    public void setOneViewVersion(String oneViewVersion) {
        this.oneViewVersion = oneViewVersion;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the GraphSizesDtlsHashMap
     */
    public HashMap getGraphSizesDtlsHashMap() {
        return GraphSizesDtlsHashMap;
    }

    /**
     * @param GraphSizesDtlsHashMap the GraphSizesDtlsHashMap to set
     */
    public void setGraphSizesDtlsHashMap(HashMap GraphSizesDtlsHashMap) {
        this.GraphSizesDtlsHashMap = GraphSizesDtlsHashMap;
    }

    /**
     * @return the jqpToJfNameMap
     */
    public HashMap<String, String> getJqpToJfNameMap() {
        return jqpToJfNameMap;
    }

    /**
     * @param jqpToJfNameMap the jqpToJfNameMap to set
     */
    public void setJqpToJfNameMap(HashMap<String, String> jqpToJfNameMap) {
        this.jqpToJfNameMap = jqpToJfNameMap;
    }

    public Map<String, List<String>> getallFilters() {
        return allFilters;
    }

    /**
     * @param jqpToJfNameMap the jqpToJfNameMap to set
     */
    public void setallFilters(Map<String, List<String>> allFilters) {
        this.allFilters = allFilters;
    }

    public Map<String, List<String>> getallFiltersnames() {
        return allFiltersnames;
    }

    public void setFilterMap(Map<String, List<String>> filterMap) {
        this.filterMap = filterMap;
    }

    public Map<String, List<String>> getFilterMap() {
        return filterMap;
    }

    public void setviewbygblname(ArrayList viewbygblname) {
        this.viewbygblname = viewbygblname;
    }

    public ArrayList getviewbygblname() {
        return viewbygblname;
    }

    public void setparameterlist(ArrayList parameterlist) {
        this.parameterlist = parameterlist;
    }

    public ArrayList getparameterlist() {
        return parameterlist;
    }

    /**
     * @param jqpToJfNameMap the jqpToJfNameMap to set
     */
    public void setallFiltersnames(Map<String, List<String>> allFiltersnames) {
        this.allFiltersnames = allFiltersnames;
    }

    /**
     * @return the jqpMap
     */
    public HashMap getJqpMap() {
        return jqpMap;
    }

    /**
     * @param jqpMap the jqpMap to set
     */
    public void setJqpMap(HashMap jqpMap) {
        this.jqpMap = jqpMap;
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

    public String getIconVisibility() {
        return iconVisibility;
    }

    public void setIconVisibility(String iconVisibility) {
        this.iconVisibility = iconVisibility;
    }

    /**
     * @return the container
     */
    public Container getContainer() {
        return container;
    }

    /**
     * @param container the container to set
     */
    public void setContainer(Container container) {
        this.container = container;
    }

    /**
     * @return the oneViewType
     */
    public String getOneViewType() {
        return oneViewType;
    }

    /**
     * @param oneViewType the oneViewType to set
     */
    public void setOneViewType(String oneViewType) {
        this.oneViewType = oneViewType;
    }

    public String getistrasnspose() {
        return istrasnspose;
    }

    /**
     * @param oneViewType the oneViewType to set
     */
    public void setistrasnsposee(String istrasnspose) {
        this.istrasnspose = istrasnspose;
    }

    /**
     * @return the facade
     */
    public DataFacade getFacade() {
        return facade;
    }

    /**
     * @param facade the facade to set
     */
    public void setFacade(DataFacade facade) {
        this.facade = facade;
    }

    /**
     * @return the kpiElements
     */
    public List<KPIElement> getKpiElements() {
        return kpiElements;
    }

    /**
     * @param kpiElements the kpiElements to set
     */
    public void setKpiElements(List<KPIElement> kpiElements) {
        this.kpiElements = kpiElements;
    }

// sandeep to format the date for displaying on top
    public void evaluateReportDateHeaders(OnceViewContainer onecontainer, String oneviewtypedate) {
        ArrayList<String> timeinfo = (ArrayList<String>) onecontainer.timedetails;
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
            if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                dats0 = vals1[3].trim().split("/");
                if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("false")) {
                    if (vals1[4].contains("/")) {
                        cfdates = vals1[4].split("/");
                    }
                    if (vals1[5].contains("/") && vals1[5] != "") {
                        ctdates = vals1[5].split("/");
                    }
                }
            }
        }
        if (vals1[2].contains("-")) {
            date0 = vals1[2].split("-");
            dats0 = vals1[3].split("-");
//              dats0 = vals1[2].split("-");
//            String values = dats0[0];
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
            if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                String[] repdates1 = vals1[3].split("-");
                if (repdates1[0].trim().toString().length() == 2) {
                    dats0[0] = repdates1[0].trim();
                    dats0[1] = repdates1[1].trim();
                    dats0[2] = repdates1[2].trim();
                } else {
                    dats0[2] = repdates1[0].trim();
                    dats0[0] = repdates1[1].trim();
                    dats0[1] = repdates1[2].trim();
                }
                if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("false")) {
                    if (vals1[4].contains("-") && !vals1[4].isEmpty()) {
                        cfdates1 = vals1[4].split(" ");
                        String values = cfdates1[1];
                        String[] repdates12 = values.split("-");
                        if (repdates[0].trim().toString().length() == 2) {
                            cfdates[0] = repdates12[0].trim();
                            cfdates[1] = repdates12[1].trim();
                            cfdates[2] = repdates12[2].trim();
                        } else {
                            cfdates[2] = repdates[0].trim();
                            cfdates[0] = repdates[1].trim();
                            cfdates[1] = repdates[2].trim();
                        }
                    }
                    if (vals1[5].contains("-")) {
                        ctdates1 = vals1[5].split(" ");
                        String values1 = ctdates1[1].trim();
                        String[] repdates1c = values1.split("-");
                        if (repdates[0].trim().toString().length() == 2) {
                            ctdates[0] = repdates1c[0].trim();
                            ctdates[1] = repdates1c[1].trim();
                            ctdates[2] = repdates1c[2].trim();
                        } else {
                            ctdates[2] = repdates1c[0].trim();
                            ctdates[0] = repdates1c[1].trim();
                            ctdates[1] = repdates1c[2].trim();
                        }
                    }
                }
            }

        }

        //  dates[0];//month
        // dates[1];//day
        // dates[2];//year
        Calendar ca1 = Calendar.getInstance();
        Calendar ca11 = Calendar.getInstance();

        // 
        //  ca1.set(Integer.parseInt( date0[2] .substring(0,4)), Integer.parseInt( date0[0].replace(" ",""))-1, Integer.parseInt(date0[1]));
        if (timeinfo.get(1).equalsIgnoreCase("PRG_STD")) {
            ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].trim()) - 1, Integer.parseInt(date0[1]));
            java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
            String partialName0 = new SimpleDateFormat("MMM").format(d0);
            day0 = d0.toString().substring(0, 3);
            dated = date0[1];
            fullName0 = partialName0 + "'" + date0[2].substring(2, 4);
        }
        if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
            ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].trim()) - 1, Integer.parseInt(date0[1]));
            java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
            String partialName0 = new SimpleDateFormat("MMM").format(d0);
            day0 = d0.toString().substring(0, 3);
            dated = date0[1];
            fullName0 = partialName0 + "'" + date0[2].substring(2, 4);


            ca11.set(Integer.parseInt(dats0[2].substring(0, 4)), Integer.parseInt(dats0[0].trim()) - 1, Integer.parseInt(dats0[1]));
            java.util.Date d01 = new java.util.Date(ca11.getTimeInMillis());
            String partialName01 = new SimpleDateFormat("MMM").format(d01);
            day = d01.toString().substring(0, 3);
            date = dats0[1];
            fullName = partialName01 + "'" + dats0[2].substring(2, 4);
            if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("false")) {
                // compare from
                if (cfdates[0] != null && cfdates[1] != null && cfdates[2] != null) {
                    ca1.set(Integer.parseInt(cfdates[2].substring(0, 4)), Integer.parseInt(cfdates[0].trim()) - 1, Integer.parseInt(cfdates[1]));
                    java.util.Date cfd = new java.util.Date(ca1.getTimeInMillis());
                    String cfName = new SimpleDateFormat("MMM").format(cfd);
                    cfday = cfd.toString().substring(0, 3);
                    cfdate = cfdates[1];
                    cffullName = cfName + "'" + cfdates[2].substring(2, 4);
                }
                // compare to
                if (ctdates[0] != null && ctdates[1] != null && ctdates[2] != null) {
                    ca1.set(Integer.parseInt(ctdates[2].substring(0, 4)), Integer.parseInt(ctdates[0].trim()) - 1, Integer.parseInt(ctdates[1]));
                    java.util.Date ctd = new java.util.Date(ca1.getTimeInMillis());
                    String ctName = new SimpleDateFormat("MMM").format(ctd);
                    ctday = ctd.toString().substring(0, 3);
                    ctdate = ctdates[1];
                    ctfullName = ctName + "'" + ctdates[2].substring(2, 4);
                }
            }
        }
        datetype = timeinfo.get(1);

    }
    //code added by Bhargavi

    public void setFilterParameters(ArrayList<String> filterParameters) {
        this.filterParameters = filterParameters;
    }

    public ArrayList<String> getFilterParameters() {
        return filterParameters;
    }
    //end of code
}
