/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arun
 */
public class ReportTableProperties implements Serializable {

    private static final long serialVersionUID = 752647115569828L;
    String crosstabGrandTotalDisplay;
    String crosstabSubTotalDisplay;
    String tableRowViewDisplayMode;
    boolean isTransposed;
    boolean crosstabDynamicRowDisplay;
    boolean crosstabWrapHeaders;
    String tableDisplayMode;
    int topBtmCount;
    int kpitopBtmCount;
    String topBtmType;
    String topBtmMode;
    String topBtmcolNam;
    int subTotalTopBtmCount;
    String subTotalTopBtmType = "TopBottomNone";
    String subTotalTopBtmMode;
    String adhocDrillType;
    // String Avgcalculationtype;
    // boolean isAdhocDrill;
    boolean isParameterDrill;
    boolean isStTimePeriodReq;
    boolean isTreeTableDisplay;
    boolean isdrillvalues;
    boolean percentWiseExist;
    boolean isSerialnum = true;
    String msrDrillType;
    boolean isrenameTotal;
    String originalTotalName;
    String RenamedTotalName;
    boolean isCustTotEnabled;
    String custTotName = null;
    String NewUiyr = null;
    String NewUiqr = null;
    String mappedTo = null;
    boolean summarizedMeasuresEnabled;
    boolean MaskZerovalues;
    boolean isRowGrandTotalRenamed;
    String RowRenamedTotalName;
    String OriginalRowTotalName;
    boolean HideMsrHeading = false;
    boolean enablecomparision;
    boolean addrowenable;
    boolean grdTotalReqkpi;
    boolean netTotalReqkpi;
    boolean multiflag = false;//added by sruthi for multi calendar
    //added by Dinanath for showing trendIcon
    boolean isEnableToShowTrendIcon;
    HashMap<String, String> ComparisionMsrs = new HashMap<String, String>();
    HashMap<String, String> rowcolorkpi = new HashMap<String, String>();
    HashMap<String, String> ispriorcoloumn = new HashMap<String, String>();
    HashMap<String, String> rowaddingmap1 = new HashMap<String, String>();
    HashMap<String, HashMap<String, String>> rowaddingmap = new HashMap<String, HashMap<String, String>>();
    HashMap<String, String> crossalignment = new HashMap<String, String>();
    HashMap<String, String> renameDetails = new HashMap<String, String>();
    HashMap<String, String> summerizedMsrRename = new HashMap<String, String>();
    HashMap<String, String> CTcolGtAggType = new HashMap<String, String>();
    ArrayList<String> qfilters = new ArrayList<String>();
    ArrayList<String> grandtotalZero = new ArrayList<String>();//added by sruthi for hideGtZero
    boolean grandtotalzero;//added by sruthi for hideGtZero
    boolean qfilter;
    HashMap<String, String> rowTextMap = new HashMap<String, String>();
    HashMap<String, HashMap<String, String>> fontFormats = new HashMap<String, HashMap<String, String>>();
    boolean numberFormatHeader;//added by sruthi for numberformate
    String grandTotalBGColor;//added by Mohit Gupta
    String subTotalBGColor;// ""
    String headerBgColor;
    HashMap<String, String> customHeader = new HashMap<String, String>();//added by sruthi for custom header
    ArrayList<String> selectedviews = new ArrayList<String>();
    HashMap<String, ArrayList<String>> tablecolumnproperties = new HashMap<String, ArrayList<String>>();//added by sruthi for tablecolumn pro
    Map<String, String> measureColor = new HashMap<String, String>();
    Map<String, String> scriptColor = new HashMap<String, String>();//added by sruthi  script color
    ArrayList<String> alldetailslist = new ArrayList<String>();
    HashMap<String, ArrayList<String>>  isRunTimeComparisionMsr = new HashMap<String,  ArrayList<String>>();//added by anitha for MTD,QTD,YTD in AO Report
    HashMap<String, ArrayList<String>>  RunTimeComparisionMsrHashMap = new HashMap<String,  ArrayList<String>>();//added by anitha for MTD,QTD,YTD in AO Report
    String filterNo = "";
    boolean Priorflag = false;
    boolean dateenable;
      boolean enableComp=false;
public Map<String, String> measurebgColor = new HashMap<>();
public Map<String , String> ViewbyAligns = new HashMap<String, String>();
public Map<String , String> viewbydataAligns = new HashMap<String, String>();
public String ONClickInformation="";
public String ElementIdvalue="";
public String refreshOnSorting="";
public String clearInformation="";
public String searchdata="";   
public String groupColumns="";
public HashMap<String , String> rtMeasureCompareWith = new HashMap<String, String>();//added by 
    ReportTableProperties() {
        summarizedMeasuresEnabled = false;
        isCustTotEnabled = false;
        isrenameTotal = false;
        grdTotalReqkpi = true;
        netTotalReqkpi = true;
        originalTotalName = "none";
        RenamedTotalName = "none";
        isTreeTableDisplay = false;
        isParameterDrill = false;
        adhocDrillType = "drilldown";
        // adhocDrillType = "none";
        // Avgcalculationtype="Exclude0";
        msrDrillType = "ReportDrill";
    
        isTransposed = false;
        isdrillvalues = false;
        crosstabDynamicRowDisplay = false;
        percentWiseExist = false;
        crosstabGrandTotalDisplay = ContainerConstants.CROSSTAB_GRANDTOTAL_LAST;
        crosstabSubTotalDisplay = ContainerConstants.CROSSTAB_SUBTOTAL_AFTER;
        tableRowViewDisplayMode = ContainerConstants.TABLE_ROWVIEW_DISPLAY_UNWRAPPED;
        crosstabWrapHeaders = false;
        tableDisplayMode = ContainerConstants.PROGEN_TABLE_DISPLAY;
        topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_NONE;
        MaskZerovalues = false;
        isRowGrandTotalRenamed = false;
        RowRenamedTotalName = "none";
        OriginalRowTotalName = "none";
        grandtotalzero = false;//added by sruthi for hideGtZero
        qfilter = false;
        isEnableToShowTrendIcon = false;//added by Dinanath for showing trendIcon
        numberFormatHeader = true;//added by sruthi for numberformate
    }

    public StringBuilder toXml() {
        StringBuilder reportTblPropXml = new StringBuilder();
        reportTblPropXml.append("<RowViewProperties>");
        reportTblPropXml.append("<DisplayMode>");
        reportTblPropXml.append(this.tableRowViewDisplayMode);
        reportTblPropXml.append("</DisplayMode>");
        reportTblPropXml.append("</RowViewProperties>");

        //added by sruthi for hideGtZero
        reportTblPropXml.append("<GrandTotalZero>");
        reportTblPropXml.append(this.grandtotalzero);
        reportTblPropXml.append("</GrandTotalZero>");
//ended by sruthi
        //added by sruthi for multicalendar
        reportTblPropXml.append("<MultiCalendarFlag>");
        reportTblPropXml.append(this.multiflag);
        reportTblPropXml.append("</MultiCalendarFlag>");
//ended by sruthi
//added by sruthi for numberformate
        reportTblPropXml.append("<qfilter>");
        reportTblPropXml.append(this.qfilter);
        reportTblPropXml.append("</qfilter>");

        reportTblPropXml.append("<NumberFormatHeader>");
        reportTblPropXml.append(this.numberFormatHeader);
        reportTblPropXml.append("</NumberFormatHeader>");
        //ended by sruthi

        //added By Mohit Gupta GT BGcolor and SubTotal color
        reportTblPropXml.append("<GrandTotalBGColor>");
        if (this.grandTotalBGColor != null && this.grandTotalBGColor != "") {
            this.grandTotalBGColor = this.grandTotalBGColor;
        }
        reportTblPropXml.append(this.grandTotalBGColor);
        reportTblPropXml.append("</GrandTotalBGColor>");


        reportTblPropXml.append("<SubTotalBGColor>");
        reportTblPropXml.append(this.subTotalBGColor);
        reportTblPropXml.append("</SubTotalBGColor>");

        // ended by Mohit Gupta
        // added by Manoj 
        reportTblPropXml.append("<HeaderBgColor>");
        reportTblPropXml.append(this.headerBgColor);
        reportTblPropXml.append("</HeaderBgColor>");



        reportTblPropXml.append("<Transposed>");
        reportTblPropXml.append(this.isTransposed);
        reportTblPropXml.append("</Transposed>");

        reportTblPropXml.append("<GrandTotalDisplaykpi>");
        reportTblPropXml.append(this.grdTotalReqkpi);
        reportTblPropXml.append("</GrandTotalDisplaykpi>");

        reportTblPropXml.append("<SubTotalDisplaykpi>");
        reportTblPropXml.append(this.netTotalReqkpi);
        reportTblPropXml.append("</SubTotalDisplaykpi>");

        reportTblPropXml.append("<kpitopBtmCount>");
        reportTblPropXml.append(this.kpitopBtmCount);
        reportTblPropXml.append("</kpitopBtmCount>");

        reportTblPropXml.append("<drillvalues>");
        reportTblPropXml.append(this.isdrillvalues);
        reportTblPropXml.append("</drillvalues>");

        reportTblPropXml.append("<AdhocDrill>");
        reportTblPropXml.append(this.adhocDrillType);
        reportTblPropXml.append("</AdhocDrill>");


        reportTblPropXml.append("<ParameterDrill>");
        reportTblPropXml.append(this.isParameterDrill);
        reportTblPropXml.append("</ParameterDrill>");

        reportTblPropXml.append("<TreeTableDisplay>");
        reportTblPropXml.append(this.isTreeTableDisplay);
        reportTblPropXml.append("</TreeTableDisplay>");

        reportTblPropXml.append("<SerailNumDisplay>");
        reportTblPropXml.append(this.isSerialnum);
        reportTblPropXml.append("</SerailNumDisplay>");

        reportTblPropXml.append("<MeasureDrill>");
        reportTblPropXml.append(this.msrDrillType);
        reportTblPropXml.append("</MeasureDrill>");

        reportTblPropXml.append("<SummarizedMeasuresEnable>");
        reportTblPropXml.append(this.summarizedMeasuresEnabled);
        reportTblPropXml.append("</SummarizedMeasuresEnable>");

        reportTblPropXml.append("<MaskZerovalues>");
        reportTblPropXml.append(this.MaskZerovalues);
        reportTblPropXml.append("</MaskZerovalues>");

        reportTblPropXml.append("<HideMsrHeading>");
        reportTblPropXml.append(this.HideMsrHeading);
        reportTblPropXml.append("</HideMsrHeading>");


        reportTblPropXml.append("<CustTotEnabled>");
        reportTblPropXml.append(this.isCustTotEnabled);
        reportTblPropXml.append("</CustTotEnabled>");
        //sandeep
        reportTblPropXml.append("<newUIyear>");

        reportTblPropXml.append(this.NewUiyr);
        reportTblPropXml.append("</newUIyear>");
        reportTblPropXml.append("<newUIqtr>");

        reportTblPropXml.append(this.NewUiqr);
        reportTblPropXml.append("</newUIqtr>");

           if(ElementIdvalue!=null&&!ElementIdvalue.isEmpty()){
           reportTblPropXml.append("<elementIdvalue>");
        reportTblPropXml.append(this.ElementIdvalue);
        reportTblPropXml.append("</elementIdvalue>");
           }
              if(ONClickInformation!=null&&!ONClickInformation.isEmpty()){
        reportTblPropXml.append("<oNClickInformation>");
        reportTblPropXml.append(this.ONClickInformation);
        reportTblPropXml.append("</oNClickInformation>");
              }
              
               if(refreshOnSorting!=null&&!refreshOnSorting.isEmpty()){
        reportTblPropXml.append("<refreshOnSorting>");
        reportTblPropXml.append(this.refreshOnSorting);
        reportTblPropXml.append("</refreshOnSorting>");
              }
                 if(clearInformation!=null&&!clearInformation.isEmpty()){
        reportTblPropXml.append("<clearInformation>");
        reportTblPropXml.append(this.clearInformation);
        reportTblPropXml.append("</clearInformation>");
                 }
           if(searchdata!=null&&!searchdata.isEmpty()){
         reportTblPropXml.append("<searchdata>");
        reportTblPropXml.append(this.searchdata);
        reportTblPropXml.append("</searchdata>");
           }
              if(groupColumns!=null&&!groupColumns.isEmpty()){
        reportTblPropXml.append("<groupColumns>");
        reportTblPropXml.append(this.groupColumns);
        reportTblPropXml.append("</groupColumns>");    
              }  

        if (isCustTotEnabled) {
            reportTblPropXml.append("<custTotal>");
            reportTblPropXml.append("<custTotName>");
            reportTblPropXml.append(this.custTotName);
            reportTblPropXml.append("</custTotName>");
            reportTblPropXml.append("<mappedTo>");
            reportTblPropXml.append(this.mappedTo);
            reportTblPropXml.append("</mappedTo>");
            reportTblPropXml.append("</custTotal>");
        }
        //added by Dinanath for showing trendIcon
        reportTblPropXml.append("<isEnableToShowTrendIcon>");
        reportTblPropXml.append(this.isEnableToShowTrendIcon);
        reportTblPropXml.append("</isEnableToShowTrendIcon>");
        //endded

        reportTblPropXml.append("<isrenameTotal>");
        reportTblPropXml.append(this.isrenameTotal);
        reportTblPropXml.append("</isrenameTotal>");

        if (isrenameTotal) {
            reportTblPropXml.append("<renameTotal>");
            ArrayList originalNames = new ArrayList(renameDetails.keySet());
            for (int i = 0; i < originalNames.size(); i++) {
                reportTblPropXml.append("<originalTotalName>");
                reportTblPropXml.append(originalNames.get(i));
                reportTblPropXml.append("</originalTotalName>");
                reportTblPropXml.append("<RenamedTotalName>");
                reportTblPropXml.append(this.renameDetails.get(originalNames.get(i)));
                reportTblPropXml.append("</RenamedTotalName>");
            }

            reportTblPropXml.append("</renameTotal>");
//        reportTblPropXml.append("<originalTotalName>");
//        reportTblPropXml.append(this.originalTotalName);
//        reportTblPropXml.append("</originalTotalName>");
//        reportTblPropXml.append("<RenamedTotalName>");
//        reportTblPropXml.append(this.RenamedTotalName);
//        reportTblPropXml.append("</RenamedTotalName>");
//        reportTblPropXml.append("</renameTotal>");
        }
        if (enablecomparision) {
            reportTblPropXml.append("<enablecomparision>");
            ArrayList originalNames = new ArrayList(ComparisionMsrs.keySet());
            for (int i = 0; i < originalNames.size(); i++) {
                reportTblPropXml.append("<originalmsr>");
                reportTblPropXml.append(originalNames.get(i));
                reportTblPropXml.append("</originalmsr>");
                reportTblPropXml.append("<priormsr>");
                reportTblPropXml.append(this.ComparisionMsrs.get(originalNames.get(i)));
                reportTblPropXml.append("</priormsr>");
            }

            reportTblPropXml.append("</enablecomparision>");

        }

        if (addrowenable) {
            reportTblPropXml.append("<addrowenable>");
            ArrayList originalNames = new ArrayList(rowaddingmap.keySet());
            for (int i = 0; i < originalNames.size(); i++) {
                reportTblPropXml.append("<newrowEntry>");
                reportTblPropXml.append("<rowid>");
                reportTblPropXml.append(originalNames.get(i));
                reportTblPropXml.append("</rowid>");
                reportTblPropXml.append("<rowvalue>");
                reportTblPropXml.append(this.rowaddingmap.get(originalNames.get(i)));
                reportTblPropXml.append("</rowvalue>");
                reportTblPropXml.append("</newrowEntry>");
            }
            reportTblPropXml.append("</addrowenable>");

        }

        if (summerizedMsrRename != null && !summerizedMsrRename.isEmpty()) {
            reportTblPropXml.append("<SummerizedMsrRenames>");
            ArrayList originalNames = new ArrayList(summerizedMsrRename.keySet());
            for (int i = 0; i < originalNames.size(); i++) {
                reportTblPropXml.append("<SummerizedMsrEntry>");
                reportTblPropXml.append("<SummerizedMsrID>");
                reportTblPropXml.append(originalNames.get(i));
                reportTblPropXml.append("</SummerizedMsrID>");
                reportTblPropXml.append("<RenamedSummerizedMsrName>");
                reportTblPropXml.append(this.summerizedMsrRename.get(originalNames.get(i)));
                reportTblPropXml.append("</RenamedSummerizedMsrName>");
                reportTblPropXml.append("</SummerizedMsrEntry>");
            }
            reportTblPropXml.append("</SummerizedMsrRenames>");

        }
        //start of code by Bhargavi for col gt as avg for sum measures
        if (CTcolGtAggType != null && !CTcolGtAggType.isEmpty()) {
            reportTblPropXml.append("<CTcolGtAggType>");
            ArrayList originalNames = new ArrayList(CTcolGtAggType.keySet());
            for (int i = 0; i < originalNames.size(); i++) {
                reportTblPropXml.append("<MsrID>");
                reportTblPropXml.append(originalNames.get(i));
                reportTblPropXml.append("</MsrID>");
                reportTblPropXml.append("<modifiedCTcolGtAggType>");
                reportTblPropXml.append(this.CTcolGtAggType.get(originalNames.get(i)));
                reportTblPropXml.append("</modifiedCTcolGtAggType>");
            }
            reportTblPropXml.append("</CTcolGtAggType>");

        }
//end of code by Bhargavi for col gt as avg for sum measures


        reportTblPropXml.append("<CrosstabDynamicRowDisplay>");
        reportTblPropXml.append(this.crosstabDynamicRowDisplay);
        reportTblPropXml.append("</CrosstabDynamicRowDisplay>");

        reportTblPropXml.append("<CrosstabProperties>");
        reportTblPropXml.append("<GrandTotalDisplay>");
        reportTblPropXml.append(this.crosstabGrandTotalDisplay);
        reportTblPropXml.append("</GrandTotalDisplay>");
        reportTblPropXml.append("<SubTotalDisplay>");
        reportTblPropXml.append(this.crosstabSubTotalDisplay);
        reportTblPropXml.append("</SubTotalDisplay>");
        reportTblPropXml.append("<CrosstabHeaderWrapped>");
        reportTblPropXml.append(this.crosstabWrapHeaders);
        reportTblPropXml.append("</CrosstabHeaderWrapped>");
        reportTblPropXml.append("</CrosstabProperties>");
        reportTblPropXml.append("<PercentWiseExist>");
        reportTblPropXml.append(this.percentWiseExist);
        reportTblPropXml.append("</PercentWiseExist>");

        if (!topBtmType.equals(ContainerConstants.TOP_BOTTOM_TYPE_NONE)) {
            reportTblPropXml.append("<TopBottom>");
            reportTblPropXml.append("<TopBottomType>");
            reportTblPropXml.append(this.topBtmType);
            reportTblPropXml.append("</TopBottomType>");
            reportTblPropXml.append("<TopBottomMode>");
            reportTblPropXml.append(this.topBtmMode);
            reportTblPropXml.append("</TopBottomMode>");
            reportTblPropXml.append("<TopBottomCount>");
            reportTblPropXml.append(this.topBtmCount);
            reportTblPropXml.append("</TopBottomCount>");
            reportTblPropXml.append("<TopBottomcolNam>");
            reportTblPropXml.append(this.topBtmcolNam);
            reportTblPropXml.append("</TopBottomcolNam>");
            reportTblPropXml.append("</TopBottom>");
        }
        if (!subTotalTopBtmType.equals(ContainerConstants.TOP_BOTTOM_TYPE_NONE)) {
            reportTblPropXml.append("<SubTotalTopBottom>");
            reportTblPropXml.append("<subTotalTopBtmType>");
            reportTblPropXml.append(this.subTotalTopBtmType);
            reportTblPropXml.append("</subTotalTopBtmType>");
            reportTblPropXml.append("<subTotalTopBtmMode>");
            reportTblPropXml.append(this.subTotalTopBtmMode);
            reportTblPropXml.append("</subTotalTopBtmMode>");
            reportTblPropXml.append("<subTotalTopBtmCount>");
            reportTblPropXml.append(this.subTotalTopBtmCount);
            reportTblPropXml.append("</subTotalTopBtmCount>");
            reportTblPropXml.append("</SubTotalTopBottom>");

        }

        reportTblPropXml.append("<isRowrenameTotal>");
        reportTblPropXml.append(this.isRowGrandTotalRenamed);
        reportTblPropXml.append("</isRowrenameTotal>");
        if (isRowGrandTotalRenamed) {
            reportTblPropXml.append("<rowGrandTotalRename>");
            reportTblPropXml.append("<originalRowTotalName>");
            reportTblPropXml.append(this.OriginalRowTotalName);
            reportTblPropXml.append("</originalRowTotalName>");
            reportTblPropXml.append("<RenamedRowTotalName>");
            reportTblPropXml.append(this.RowRenamedTotalName);
            reportTblPropXml.append("</RenamedRowTotalName>");
            reportTblPropXml.append("</rowGrandTotalRename>");
        }
        //Start of code By Bhargavi for rowcoloring and edit font format on 26th Dec 2014
        reportTblPropXml.append("<rowColoring>");
        ArrayList originalNames1 = new ArrayList(rowcolorkpi.keySet());
        for (int i = 0; i < originalNames1.size(); i++) {
            reportTblPropXml.append("<originalTotalName>");
            reportTblPropXml.append(originalNames1.get(i));
            reportTblPropXml.append("</originalTotalName>");
            reportTblPropXml.append("<Appliedcolor>");
            reportTblPropXml.append(this.rowcolorkpi.get(originalNames1.get(i)));
            reportTblPropXml.append("</Appliedcolor>");
        }

        reportTblPropXml.append("</rowColoring>");


//sandeep
        reportTblPropXml.append("<crossalign>");
        if (crossalignment != null && !crossalignment.isEmpty()) {//added by Dinanath for handling null
            ArrayList crossalignmentvalmap = new ArrayList(crossalignment.keySet());
            for (int i = 0; i < crossalignmentvalmap.size(); i++) {
                reportTblPropXml.append("<crossalignmentkey>");
                reportTblPropXml.append(crossalignmentvalmap.get(i));
                reportTblPropXml.append("</crossalignmentkey>");
                reportTblPropXml.append("<crossalignmentval>");
                reportTblPropXml.append(this.crossalignment.get(crossalignmentvalmap.get(i)));
                reportTblPropXml.append("</crossalignmentval>");
            }
        }
        reportTblPropXml.append("</crossalign>");

//end of sandeep code
        reportTblPropXml.append("<rowText>");
        ArrayList originalNames2 = new ArrayList(rowTextMap.keySet());
        for (int i = 0; i < originalNames2.size(); i++) {
            reportTblPropXml.append("<originalTotalName>");
            reportTblPropXml.append(originalNames2.get(i));
            reportTblPropXml.append("</originalTotalName>");
            reportTblPropXml.append("<AppliedText>");
            reportTblPropXml.append(this.rowTextMap.get(originalNames2.get(i)));
            reportTblPropXml.append("</AppliedText>");
        }

        reportTblPropXml.append("</rowText>");

        if (fontFormats != null && !fontFormats.isEmpty()) {
            reportTblPropXml.append("<fontFormats>");
            ArrayList originalNames = new ArrayList(fontFormats.keySet());
            for (int i = 0; i < originalNames.size(); i++) {

                Gson gson = new Gson();
                HashMap<String, String> tableHashMap = this.fontFormats.get(originalNames.get(i));
                Type tarType = new TypeToken<Map<String, String>>() {
                }.getType();
                String inMapStr = gson.toJson(tableHashMap, tarType);
                reportTblPropXml.append("<MsrID>");
                reportTblPropXml.append(originalNames.get(i));
                reportTblPropXml.append("</MsrID>");
                reportTblPropXml.append("<modifiedFontTypes>");
                reportTblPropXml.append(inMapStr);
                reportTblPropXml.append("</modifiedFontTypes>");
            }
            reportTblPropXml.append("</fontFormats>");

        }
        //end of code by Bhargavi
        //added by sruthi for hideGtZero
        if (grandtotalZero != null && !grandtotalZero.isEmpty()) {
            ArrayList gtoriginalNames = new ArrayList(grandtotalZero);
            reportTblPropXml.append("<grandtotalZerovalue>");
            for (int i = 0; i < gtoriginalNames.size(); i++) {
                reportTblPropXml.append("<grandtotalZeromrs>");
                reportTblPropXml.append(gtoriginalNames.get(i));
                reportTblPropXml.append("</grandtotalZeromrs>");
            }
            reportTblPropXml.append("</grandtotalZerovalue>");
        }
        //ended by sruthi
        if (selectedviews != null && !selectedviews.isEmpty()) {
            ArrayList selectedViewby = new ArrayList(selectedviews);
            reportTblPropXml.append("<selectedviews>");
            for (int i = 0; i < selectedViewby.size(); i++) {
                reportTblPropXml.append("<selectedViewby>");
                reportTblPropXml.append(selectedViewby.get(i));
                reportTblPropXml.append("</selectedViewby>");
            }
            reportTblPropXml.append("</selectedviews>");
        }

        //added by sruthi for custom header
        if (customHeader != null && !customHeader.isEmpty()) {
            reportTblPropXml.append("<CustomHeader>");
            ArrayList coustamHeaderkeys = new ArrayList(customHeader.keySet());
            for (int i = 0; i < coustamHeaderkeys.size(); i++) {
                reportTblPropXml.append("<CustomHeaderKeys>");
                reportTblPropXml.append(coustamHeaderkeys.get(i));
                reportTblPropXml.append("</CustomHeaderKeys>");
                reportTblPropXml.append("<CustomHeaderData>");
                reportTblPropXml.append(this.customHeader.get(coustamHeaderkeys.get(i)));
                reportTblPropXml.append("</CustomHeaderData>");
            }
            reportTblPropXml.append("</CustomHeader>");
        }
        //ended by sruthi
        //added by sruthi for tablecolumn pro
        if (tablecolumnproperties != null && !tablecolumnproperties.isEmpty()) {
            reportTblPropXml.append("<TableColumnPropertiesHeader>");
            ArrayList tablecolumnpropertieskeys = new ArrayList(tablecolumnproperties.keySet());
            for (int i = 0; i < tablecolumnpropertieskeys.size(); i++) {
                reportTblPropXml.append("<TableColumnPropertiesHeaderKeys>");
                reportTblPropXml.append(tablecolumnpropertieskeys.get(i));
                reportTblPropXml.append("</TableColumnPropertiesHeaderKeys>");
                reportTblPropXml.append("<TableColumnPropertiesHeaderData>");
                reportTblPropXml.append(this.tablecolumnproperties.get(tablecolumnpropertieskeys.get(i)));
                reportTblPropXml.append("</TableColumnPropertiesHeaderData>");
            }
            reportTblPropXml.append("</TableColumnPropertiesHeader>");
        }
        //ended by sruthi
        if (qfilters != null && !qfilters.isEmpty()) {
            ArrayList qfilters1 = new ArrayList(qfilters);
            reportTblPropXml.append("<qfiltersl>");
            for (int i = 0; i < qfilters1.size(); i++) {
                reportTblPropXml.append("<qfilters>");
                reportTblPropXml.append(qfilters1.get(i));
                reportTblPropXml.append("</qfilters>");
            }
            reportTblPropXml.append("</qfiltersl>");


        }
//added by sruthi for measurecolor
        if (measureColor != null && !measureColor.isEmpty()) {
            reportTblPropXml.append("<MeasureHeader>");
            ArrayList MeasureColorDatakeys = new ArrayList(measureColor.keySet());
            for (int i = 0; i < MeasureColorDatakeys.size(); i++) {
                reportTblPropXml.append("<MeasureColorHeaderKeys>");
                reportTblPropXml.append(MeasureColorDatakeys.get(i));
                reportTblPropXml.append("</MeasureColorHeaderKeys>");
                reportTblPropXml.append("<MeasureColorData>");
                reportTblPropXml.append(this.measureColor.get(MeasureColorDatakeys.get(i)).toString().replace("#", ""));
           
                reportTblPropXml.append("</MeasureColorData>");
            }
            reportTblPropXml.append("</MeasureHeader>");
        }

//ended by sruthi
        //added by sruthi for measurecolor
        if (scriptColor != null && !scriptColor.isEmpty()) {
            reportTblPropXml.append("<MeasurescriptColor>");
            ArrayList MeasurescriptColorDatakeys = new ArrayList(scriptColor.keySet());
            for (int i = 0; i < MeasurescriptColorDatakeys.size(); i++) {
                reportTblPropXml.append("<MeasurescriptColorKeys>");
                reportTblPropXml.append(MeasurescriptColorDatakeys.get(i));
                reportTblPropXml.append("</MeasurescriptColorKeys>");
                reportTblPropXml.append("<MeasurescriptColorData>");
                reportTblPropXml.append(this.scriptColor.get(MeasurescriptColorDatakeys.get(i)).toString().replace("#", ""));
                reportTblPropXml.append("</MeasurescriptColorData>");
            }
            reportTblPropXml.append("</MeasurescriptColor>");
        }

//ended by sruthi

        //added by sruthi for alldetails
        if (alldetailslist != null && !alldetailslist.isEmpty()) {
            ArrayList alldetailslistdata = new ArrayList(alldetailslist);
            reportTblPropXml.append("<selectedalldetailslis>");
            for (int i = 0; i < alldetailslistdata.size(); i++) {
                reportTblPropXml.append("<selectedalldata>");
                reportTblPropXml.append(alldetailslistdata.get(i).toString().replace("#", " "));
                reportTblPropXml.append("</selectedalldata>");
            }
            reportTblPropXml.append("</selectedalldetailslis>");
        }
        //ended by sruthi
        reportTblPropXml.append("<showStTimePeriod>");
        reportTblPropXml.append(this.isStTimePeriodReq);
        reportTblPropXml.append("</showStTimePeriod>");

        reportTblPropXml.append("<dateenableui>");
        reportTblPropXml.append(this.dateenable);
        reportTblPropXml.append("</dateenableui>");
        
         reportTblPropXml.append("<enableCompui>");
        reportTblPropXml.append(this.enableComp);
        reportTblPropXml.append("</enableCompui>");

          //added by sruthi for tableviewbycolumnproperties
        if(ViewbyAligns!=null&&!ViewbyAligns.isEmpty()){
          reportTblPropXml.append("<ViewbyAligns>");
        ArrayList ViewbysAligns=new ArrayList(ViewbyAligns.keySet());
        for(int i=0;i<ViewbysAligns.size();i++){
        reportTblPropXml.append("<ViewbyAlignsKeys>");
        reportTblPropXml.append(ViewbysAligns.get(i));
        reportTblPropXml.append("</ViewbyAlignsKeys>");
        reportTblPropXml.append("<ViewbyAlignsData>");
        reportTblPropXml.append(this.ViewbyAligns.get(ViewbysAligns.get(i).toString()));
        reportTblPropXml.append("</ViewbyAlignsData>");
        }
        reportTblPropXml.append("</ViewbyAligns>");
        }
          if(viewbydataAligns!=null&&!viewbydataAligns.isEmpty()){
          reportTblPropXml.append("<viewbydataAligns>");
        ArrayList viewbydataAlignsDatakeys=new ArrayList(viewbydataAligns.keySet());
        for(int i=0;i<viewbydataAlignsDatakeys.size();i++){
        reportTblPropXml.append("<viewbydataAlignsKeys>");
        reportTblPropXml.append(viewbydataAlignsDatakeys.get(i));
        reportTblPropXml.append("</viewbydataAlignsKeys>");
        reportTblPropXml.append("<viewbydataAlignsData>");
        reportTblPropXml.append(this.viewbydataAligns.get(viewbydataAlignsDatakeys.get(i).toString()));
        reportTblPropXml.append("</viewbydataAlignsData>");
        }
        reportTblPropXml.append("</viewbydataAligns>");
        }//ended by sruthi
        //added by sruthi for showfilters
//        reportTblPropXml.append("<ShowFilters>");
//        reportTblPropXml.append(this.filterNo);
//        reportTblPropXml.append("</ShowFilters>");
        //ended by sruthi

        //added by anitha for MTD,QTD,YTD in AO Report
        if (isRunTimeComparisionMsr != null && !isRunTimeComparisionMsr.isEmpty()) {
            reportTblPropXml.append("<RunTimeComparisionMsr>");
            ArrayList isRunTimeMeasureDatakeys = new ArrayList(isRunTimeComparisionMsr.keySet());
            for (int i = 0; i < isRunTimeMeasureDatakeys.size(); i++) {
                reportTblPropXml.append("<RunTimeComparisionMsrKeys>");
                reportTblPropXml.append(isRunTimeMeasureDatakeys.get(i));
                reportTblPropXml.append("</RunTimeComparisionMsrKeys>");
                reportTblPropXml.append("<RunTimeComparisionMsrData>");
                reportTblPropXml.append(this.isRunTimeComparisionMsr.get(isRunTimeMeasureDatakeys.get(i)));
                reportTblPropXml.append("</RunTimeComparisionMsrData>");
            }
            reportTblPropXml.append("</RunTimeComparisionMsr>");
        }else if(RunTimeComparisionMsrHashMap != null && !RunTimeComparisionMsrHashMap.isEmpty()){
            reportTblPropXml.append("<RunTimeComparisionMsr>");
            ArrayList isRunTimeMeasureDatakeys = new ArrayList(RunTimeComparisionMsrHashMap.keySet());
            for (int i = 0; i < isRunTimeMeasureDatakeys.size(); i++) {
                if(isRunTimeMeasureDatakeys.get(i).toString().equalsIgnoreCase("reportQryElementIds")||
                        isRunTimeMeasureDatakeys.get(i).toString().equalsIgnoreCase("reportQryAggregations")||
                        isRunTimeMeasureDatakeys.get(i).toString().equalsIgnoreCase("reportQryColNames")||
                        isRunTimeMeasureDatakeys.get(i).toString().equalsIgnoreCase("reportQryColTypes")){
                reportTblPropXml.append("<RunTimeComparisionMsrKeys>");
                reportTblPropXml.append(isRunTimeMeasureDatakeys.get(i));
                reportTblPropXml.append("</RunTimeComparisionMsrKeys>");
                reportTblPropXml.append("<RunTimeComparisionMsrData>");
                reportTblPropXml.append(this.RunTimeComparisionMsrHashMap.get(isRunTimeMeasureDatakeys.get(i)));
                reportTblPropXml.append("</RunTimeComparisionMsrData>");
        }
                }
            reportTblPropXml.append("</RunTimeComparisionMsr>");
        }
        //end of code by anitha for MTD,QTD,YTD in AO Report
           
        
        //added by sruthi for background color 
               if(measurebgColor!=null&&!measurebgColor.isEmpty()){
          reportTblPropXml.append("<measurebgColor>");
        ArrayList measurebgColorkeys=new ArrayList(measurebgColor.keySet());
        for(int i=0;i<measurebgColorkeys.size();i++){
        reportTblPropXml.append("<measurebgColorKeys>");
        reportTblPropXml.append(measurebgColorkeys.get(i));
        reportTblPropXml.append("</measurebgColorKeys>");
        reportTblPropXml.append("<measurebgColorData>");
        reportTblPropXml.append(this.measurebgColor.get(measurebgColorkeys.get(i).toString()));
        reportTblPropXml.append("</measurebgColorData>");
        }
        reportTblPropXml.append("</measurebgColor>");
        }//ended by sruthi  
               
         //added by anitha for RT Time Agg compate with
        if(rtMeasureCompareWith!=null&& !rtMeasureCompareWith.isEmpty()){
        reportTblPropXml.append("<rtMeasureCompareWith>");
        ArrayList rtMeasureCompareWithDatakeys=new ArrayList(rtMeasureCompareWith.keySet());
        for(int i=0;i<rtMeasureCompareWithDatakeys.size();i++){
        reportTblPropXml.append("<rtMeasureCompareWithKeys>");
        reportTblPropXml.append(rtMeasureCompareWithDatakeys.get(i));
        reportTblPropXml.append("</rtMeasureCompareWithKeys>");
        reportTblPropXml.append("<rtMeasureCompareWithData>");
        reportTblPropXml.append(this.rtMeasureCompareWith.get(rtMeasureCompareWithDatakeys.get(i).toString()));
        reportTblPropXml.append("</rtMeasureCompareWithData>");
        }
        reportTblPropXml.append("</rtMeasureCompareWith>"); 
        }
        //end of code by anitha for RT Time Agg compate with
        
        return reportTblPropXml;
    }
}
