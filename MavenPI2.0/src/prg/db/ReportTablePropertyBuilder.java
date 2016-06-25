/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.MeasureGroup;
import com.progen.report.MeasureGroupColumn;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public enum ReportTablePropertyBuilder implements javax.xml.stream.StreamFilter {

    TABLE_PROPERTY_BUILDER;
    public static Logger logger = Logger.getLogger(ReportTablePropertyBuilder.class);

    public void updateTablePropertiesInContainer(Container container, StringBuilder tablePropertiesXml) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream input = new ByteArrayInputStream(tablePropertiesXml.toString().getBytes());
        int eventType;
        try {
            XMLStreamReader xmlStreamReader = inputFactory.createFilteredReader(inputFactory.createXMLStreamReader(input), this);
            while (xmlStreamReader.hasNext()) {
                eventType = xmlStreamReader.next();
                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    if ("Sort".equals(getName(xmlStreamReader))) {
                        updateSortPropertiesInContainer(container, xmlStreamReader);
                    }
                    if ("ESort".equals(getName(xmlStreamReader))) {
                        updateSubTotalSortPropertiesInContainer(container, xmlStreamReader);
                    }

                    if ("RowViewProperties".equals(getName(xmlStreamReader))) {
                        updateRowViewPropertiesInContainer(container, xmlStreamReader);
                    }

                    if ("Transposed".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setTransposeTable(new Boolean(xmlStreamReader.getText()));
                        container.setTransposeTable(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if ("GrandTotalDisplaykpi".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setGrandTotalReqForkpi(new Boolean(xmlStreamReader.getText()));
                        container.setGrandTotalReqForkpi(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if ("SubTotalDisplaykpi".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setNetTotalReqForkpi(new Boolean(xmlStreamReader.getText()));
                        container.setNetTotalReqForkpi(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    //sandeep
                    if ("kpitopBtmCount".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setkpiTopBottomColumn(Integer.parseInt(xmlStreamReader.getText()));

                    }
                    if ("drillvalues".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setdrillvalues(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if ("AdhocDrill".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setAdhocDrillType(xmlStreamReader.getText());
                    }
                    if ("ParameterDrill".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setParameterDrill(new Boolean(xmlStreamReader.getText()));
                        container.setParameterDrill(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if ("TreeTableDisplay".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setTreeTableDisplay(new Boolean(xmlStreamReader.getText()));
                        container.setTreeTableDisplay(Boolean.valueOf(xmlStreamReader.getText()));
                    }

                    if ("SerailNumDisplay".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setSerialNumDisplay(Boolean.valueOf(xmlStreamReader.getText()));
//                          container.setSerialNumDisplay(new Boolean(xmlStreamReader.getText()));
                    }
                    if ("MeasureDrill".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setMeasureDrillType(xmlStreamReader.getText());
                    }
//sandeep
                    if ("newUIyear".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        String value = xmlStreamReader.getText();
                        if (value != null && !value.equalsIgnoreCase("null") && value != "") {
                            container.setNewUIyr(xmlStreamReader.getText());
                        }
                    }
                    if ("newUIqtr".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        String value = xmlStreamReader.getText();
                        if (value != null && !value.equalsIgnoreCase("null") && value != "") {
                            container.setNewUiqr(xmlStreamReader.getText());
                        }
                    }

                    if ("SummarizedMeasuresEnable".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setSummarizedMeasuresEnabled(Boolean.valueOf(xmlStreamReader.getText()));
//                        container.setSummarizedMeasuresEnabled(new Boolean(xmlStreamReader.getText()));
                    }

                    if ("MaskZerovalues".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setisMaskZeros(Boolean.valueOf(xmlStreamReader.getText()));
//                         container.setisMaskZeros(new Boolean(xmlStreamReader.getText()));
                    }
                    if ("HideMsrHeading".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setHideMsrHeading(new Boolean(xmlStreamReader.getText()));
                        container.setHideMsrHeading(Boolean.valueOf(xmlStreamReader.getText()));
                    }

                    if ("CustTotEnabled".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                      container.setIsCustTotEnabled(new Boolean(xmlStreamReader.getText()));
                        container.setIsCustTotEnabled(Boolean.valueOf(xmlStreamReader.getText()));
                    }

                    if (container.isCustomTotEnabled()) {
                        if ("custTotal".equals(getName(xmlStreamReader))) {
                            updatcustomTotalPropertiesInContainer(container, xmlStreamReader);
                        }
                    }
                    if ("isrenameTotal".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                     container.setRenameTotal(new Boolean(xmlStreamReader.getText()));
                        container.setRenameTotal(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if (container.isRenameTotal()) {
                        if ("renameTotal".equals(getName(xmlStreamReader))) {
                            updatrenameTotalPropertiesInContainer(container, xmlStreamReader);
                        }
                    }

                    if ("enablecomparision".equals(getName(xmlStreamReader))) {
                        updatenablecomparisionInContainer(container, xmlStreamReader);
                    }


                    if ("addrowenable".equals(getName(xmlStreamReader))) {
                        updaterowadding(container, xmlStreamReader);
                    }

                    if (container.isReportCrosstab() && container.isSummarizedMeasuresEnabled()) {

                        if ("SummerizedMsrRenames".equals(getName(xmlStreamReader))) {
                            updatreSummeraizedMeasuresNames(container, xmlStreamReader);
                        }

                    }
//start of code by Bhargavi for col gt as avg for sum measures
                    if (container.isReportCrosstab()) {

                        if ("CTcolGtAggType".equals(getName(xmlStreamReader))) {
                            updateCTcolGtAggType(container, xmlStreamReader);
                        }

                    }
//end of code by Bhargavi for col gt as avg for sum measures
                    if ("CrosstabDynamicRowDisplay".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setCrosstabDynamicRowDisplay(new Boolean(xmlStreamReader.getText()));
                        container.setCrosstabDynamicRowDisplay(Boolean.valueOf(xmlStreamReader.getText()));
                    }

                    if ("CrosstabProperties".equals(getName(xmlStreamReader))) {
                        updateCrosstabPropertiesInContainer(container, xmlStreamReader);
                    }

                    if ("TopBottom".equals(getName(xmlStreamReader))) {
                        updateTopBottomPropertiesInContainer(container, xmlStreamReader);
                    }
                    if ("SubTotalTopBottom".equals(getName(xmlStreamReader))) {
                        updateSubTotalTopBottomPropertiesInContainer(container, xmlStreamReader);
                    }
                    if ("TableSearch".equals(getName(xmlStreamReader))) {
                        updateTableSearchInContainer(container, xmlStreamReader);
                    }
                    if ("TableSubTotalSearch".equals(getName(xmlStreamReader))) {
                        updateTableSubTotalSearchInContainer(container, xmlStreamReader);
                    }
                    if ("MeasureGroups".equals(getName(xmlStreamReader))) {
                        updateMeasureGroupContainer(container, xmlStreamReader);
                    }

                    if ("PercentWiseExist".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                       container.setPercentWiseStatus(new Boolean(xmlStreamReader.getText()));
                        container.setPercentWiseStatus(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if ("Group".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        updateGroupPropertiesInContainer(container, xmlStreamReader);
                    }
                    if ("GraphCrosstabMeas".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setGraphCrossTabMeas(xmlStreamReader.getText());
                    }
                    if ("isRowrenameTotal".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                             container.setRowGTRenameTotal(new Boolean(xmlStreamReader.getText()));
                        container.setRowGTRenameTotal(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    //added by Dinanath for showTrendIcon
                    if ("isEnableToShowTrendIcon".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setToShowTrendIcon(Boolean.valueOf(xmlStreamReader.getText()));
//                        container.setToShowTrendIcon(new Boolean(xmlStreamReader.getText()));
                    }
                    //endded by Dinanath
                    if (container.isrowRenameTotal()) {
                        if ("rowGrandTotalRename".equals(getName(xmlStreamReader))) {
                            updatRowrenameTotalPropertiesInContainer(container, xmlStreamReader);
                        }
                    }
                    //Start of code By Bhargavi on 26th Dec 2014
                    if ("rowColoring".equals(getName(xmlStreamReader))) {
                        updateRowColorForkpi(container, xmlStreamReader);
                    }


                    if ("crossalign".equals(getName(xmlStreamReader))) {
                        upsatecrossalign(container, xmlStreamReader);
                    }

                    if ("fontFormats".equals(getName(xmlStreamReader))) {
                        updateFontFormats(container, xmlStreamReader);
                    }

                    if ("rowText".equals(getName(xmlStreamReader))) {
                        updateRowText(container, xmlStreamReader);
                    }
                    //end of code by Bhargavi
                    //added by sruthi for hideGtZero
                    if ("grandtotalZerovalue".equals(getName(xmlStreamReader))) {
                        // eventType = xmlStreamReader.next();
                        hidemeasureset(container, xmlStreamReader);
                    }
                    if ("qfiltersl".equals(getName(xmlStreamReader))) {
                        setqfilters(container, xmlStreamReader);
                    }
                    if ("GrandTotalZero".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
//                        container.setGrandTotalZero(new Boolean(xmlStreamReader.getText()));
                        container.setGrandTotalZero(Boolean.valueOf(xmlStreamReader.getText()));
                    }//ended by sruthi
                    //added by sruthi for numberformate
                    if ("NumberFormatHeader".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setNumberFormatHeader(Boolean.valueOf(xmlStreamReader.getText()));
//                          container.setNumberFormatHeader(new Boolean(xmlStreamReader.getText()));
                    }
                    //ended by sruthi
                    //added by sruthi for customHeader
                    if ("CustomHeader".equals(getName(xmlStreamReader))) {
                        coustamHeaderset(container, xmlStreamReader);
                    }
                    //ended by sruthi
                    if ("showStTimePeriod".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setStTimePeriod(Boolean.valueOf(xmlStreamReader.getText()));
//                        container.setStTimePeriod(new Boolean(xmlStreamReader.getText()));
                    }
                    //added by sruthi for tablecolumn pro
                    if ("TableColumnPropertiesHeader".equals(getName(xmlStreamReader))) {
                        TableColumnProperties(container, xmlStreamReader);
                    }//ended by sruthi
                    //added by sruthi formulticalendar for reports
                    if ("MultiCalendarFlag".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setMultiCalendarFlag(Boolean.valueOf(xmlStreamReader.getText()));
//                          container.setMultiCalendarFlag(new Boolean(xmlStreamReader.getText()));
                    }//ended by sruthi
                    //added by sruthi for alldetails
                    if ("selectedalldetailslis".equals(getName(xmlStreamReader))) {
                        //eventType=xmlStreamReader.next();
                        setAlldetails(container, xmlStreamReader);
                    }  //ended by sruthi  
                    if ("selectedviews".equals(getName(xmlStreamReader))) {
                        // eventType = xmlStreamReader.next();
                        selectedViewby(container, xmlStreamReader);
                    }
                    if ("MeasureHeader".equals(getName(xmlStreamReader))) {
                        // eventType = xmlStreamReader.next();
                        MeasureColorHeader(container, xmlStreamReader);
                    }
                    if ("MeasurescriptColor".equals(getName(xmlStreamReader))) {
                        // eventType = xmlStreamReader.next();
                        MeasureTextColor(container, xmlStreamReader);
                    }
                    //added By Mohit Gupta
                    if ("GrandTotalBGColor".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setGrandTotalBGColor(xmlStreamReader.getText());
//                          container.setGrandTotalBGColor(new String(xmlStreamReader.getText())); Prabal
                    }
                    if ("SubTotalBGColor".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setSubTotalBGColor(xmlStreamReader.getText());
//                            container.setSubTotalBGColor(new String(xmlStreamReader.getText()));Prabal
                    }
                    //added by Manoj 
                    if ("HeaderBgColor".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setHeaderBgColor(xmlStreamReader.getText());
//                           container.setHeaderBgColor(new String(xmlStreamReader.getText()));/prabal
                    }
                    if ("PriorEnableFlag".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setPriorEnableFlag(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if ("dateenableui".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setdateenable(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                    if ("enableCompui".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setenableComp(Boolean.valueOf(xmlStreamReader.getText()));
                    }
                      if("ViewbyAligns".equals(getName(xmlStreamReader))){
                         ViewbyAligns(container,xmlStreamReader);
                     } 
                 if("viewbydataAligns".equals(getName(xmlStreamReader))){
                         viewbydataAligns(container,xmlStreamReader);
                     }
                    //added by sruthi for showfilters
//                     if("ShowFilters".equals(getName(xmlStreamReader))){
//                         eventType=xmlStreamReader.next();
//                           container.setshowfilters(new String(xmlStreamReader.getText()));
//            }//ended by sruthi
                    //added by anitha for MTD,QTD,YTD in AO report
//                    if ("RunTimeComparisionMsr".equals(getName(xmlStreamReader))) {
//                        IsRunTimeComparisionMsr(container,xmlStreamReader);
//                    }
                    //end of code by anitha for MTD,QTD,YTD in AO report
                  if ("elementIdvalue".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setElementIdvalue(xmlStreamReader.getText());
                    }
                 if ("oNClickInformation".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setONClickInformation(xmlStreamReader.getText());
                    }
                 
                  if ("refreshOnSorting".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setRefreshGr(xmlStreamReader.getText());
                    }
                  if ("clearInformation".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setclearInformation(xmlStreamReader.getText());
                    }
                    if("searchdata".equalsIgnoreCase(getName(xmlStreamReader))){
                       eventType = xmlStreamReader.next();
                        container.setsearch(xmlStreamReader.getText());  
                    }
                    if("groupColumns".equalsIgnoreCase(getName(xmlStreamReader))){
                       eventType = xmlStreamReader.next();
                        container.setGroupColumn1(xmlStreamReader.getText());  
                    }
                     if("measurebgColor".equalsIgnoreCase(getName(xmlStreamReader))){
                       measurebgColor(container, xmlStreamReader);
                   }
                }
             

            }
        } catch (XMLStreamException ex) {
//            ProgenLog.log(ProgenLog.SEVERE,this, "updateTablePropertiesInContainer", ex.getMessage());
            logger.error("enableSummerizedMeasure:", ex);
        }
    }

    public void enableSummerizedMeasure(Container container, StringBuilder tablePropertiesXml) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream input = new ByteArrayInputStream(tablePropertiesXml.toString().getBytes());
        int eventType;
        try {
            XMLStreamReader xmlStreamReader = inputFactory.createFilteredReader(inputFactory.createXMLStreamReader(input), this);
            while (xmlStreamReader.hasNext()) {
                eventType = xmlStreamReader.next();
                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    if ("SummarizedMeasuresEnable".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next();
                        container.setSummarizedMeasuresEnabled(Boolean.valueOf(xmlStreamReader.getText()));
//                         container.setSummarizedMeasuresEnabled(new Boolean(xmlStreamReader.getText()));
                    }
                }
                if (eventType == XMLStreamConstants.END_ELEMENT && "SummarizedMeasuresEnable".equals(getName(xmlStreamReader))) {
                    break;
                }
            }
        } catch (XMLStreamException ex) {
//            ProgenLog.log(ProgenLog.SEVERE,this, "enableSummerizedMeasure", ex.getMessage());
            logger.error("enableSummerizedMeasure:", ex);
        }
    }

    public String[] getCrosstabTotalsDisplayProperty(StringBuilder tablePropertiesXml) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream input = new ByteArrayInputStream(tablePropertiesXml.toString().getBytes());
        String[] totalDisplays = new String[0];
        int eventType;
        try {
            XMLStreamReader xmlStreamReader = inputFactory.createFilteredReader(inputFactory.createXMLStreamReader(input), this);
            while (xmlStreamReader.hasNext()) {
                eventType = xmlStreamReader.next();
                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    if ("CrosstabProperties".equals(getName(xmlStreamReader))) {
                        totalDisplays = new String[2];
                    }
                    if ("GrandTotalDisplay".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next(); //text of <GrandTotalDisplay>
                        totalDisplays[0] = xmlStreamReader.getText();
                    }
                    if ("SubTotalDisplay".equals(getName(xmlStreamReader))) {
                        eventType = xmlStreamReader.next(); //text of <SubTotalDisplay>
                        totalDisplays[1] = xmlStreamReader.getText();
                    }
                }
                if (eventType == XMLStreamConstants.END_ELEMENT && "CrosstabProperties".equals(getName(xmlStreamReader))) {
                    break;
                }
            }
        } catch (XMLStreamException ex) {
//            ProgenLog.log(ProgenLog.SEVERE,this, "updateTablePropertiesInContainer", ex.getMessage());
            logger.error("enableSummerizedMeasure:", ex);
        }
        return totalDisplays;
    }

    private static void updateSortPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        ArrayList<String> sortColumns = new ArrayList<String>();
        ArrayList<String> sortTypes = new ArrayList<String>();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("SortColumn".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //<ColumnName>
                    eventType = xmlStreamReader.next();

                    //reached ColumnName
                    if (!sortColumns.contains(getText(xmlStreamReader))) {
                        sortColumns.add(getText(xmlStreamReader));
                    }

                    eventType = xmlStreamReader.next(); //</ColumnName>

                    eventType = xmlStreamReader.next(); //<SortType>
                    eventType = xmlStreamReader.next();

                    //reached SortType
                    sortTypes.add(getText(xmlStreamReader));
                    eventType = xmlStreamReader.next(); //</SortType>

                }
            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "Sort".equals(getName(xmlStreamReader))) {
                break;
            }
        }

        if (!sortColumns.isEmpty()) {
            int index = 0;
            for (String sortColumn : sortColumns) {
                container.setSortColumn(sortColumn, sortTypes.get(index));
                index++;
            }
        }
    }

    private static void updateSubTotalSortPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        ArrayList<String> sortColumns = new ArrayList<String>();
        ArrayList<String> sortTypes = new ArrayList<String>();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("ESortColumn".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //<ColumnName>
                    eventType = xmlStreamReader.next();

                    //reached ColumnName
                    if (!sortColumns.contains(getText(xmlStreamReader))) {
                        sortColumns.add(getText(xmlStreamReader));
                    }

                    eventType = xmlStreamReader.next(); //</ColumnName>

                    eventType = xmlStreamReader.next(); //<SortType>
                    eventType = xmlStreamReader.next();

                    //reached SortType
                    sortTypes.add(getText(xmlStreamReader));
                    eventType = xmlStreamReader.next(); //</SortType>

                }
            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "ESort".equals(getName(xmlStreamReader))) {
                break;
            }
        }

        if (!sortColumns.isEmpty()) {
            int index = 0;
            for (String sortColumn : sortColumns) {
                container.setSubTotalSort(true);
                container.setSortColumnForSubTot(sortColumn, sortTypes.get(index));
//                container.setSortColumn(sortColumn,sortTypes.get(index));
                index++;
            }
        }
    }

    private static void updateRowViewPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("DisplayMode".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <DisplayMode>
                    container.setRowViewDisplayMode(xmlStreamReader.getText());
                }
            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "RowViewProperties".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }

    private static void updateCrosstabPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("GrandTotalDisplay".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <GrandTotalDisplay>
                    container.setCrosstabGrandTotalDisplayPosition(xmlStreamReader.getText());
                }
                if ("SubTotalDisplay".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <SubTotalDisplay>
                    container.setCrosstabSubTotalDisplayPosition(xmlStreamReader.getText());
                }
                if ("CrosstabHeaderWrapped".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <CrosstabHeaderWrapped>
                    container.wrapHeadersInCrosstab(Boolean.valueOf(xmlStreamReader.getText()));
//                      container.wrapHeadersInCrosstab(new Boolean(xmlStreamReader.getText()));
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "CrosstabProperties".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }

    private static void updateTopBottomPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String topBtmType = null;
        String topBtmMode = null;
        String topBtmcolNam = null;
        int topBtmCount = 0;
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("TopBottomType".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <TopBottomType>
                    topBtmType = xmlStreamReader.getText();
                }
                if ("TopBottomMode".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <TopBottomMode>
                    topBtmMode = xmlStreamReader.getText();
                }
                if ("TopBottomCount".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <TopBottomCount>
                    topBtmCount = Integer.parseInt(xmlStreamReader.getText());
                }
                if ("TopBottomcolNam".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <TopBottomCount>
                    topBtmcolNam = xmlStreamReader.getText();
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "TopBottom".equals(getName(xmlStreamReader))) {
                break;
            }
        }
        if (topBtmcolNam == null || topBtmcolNam.equals("")) {
            container.setTopBottomColumn(topBtmType, topBtmMode, topBtmCount);
        } else {
            container.setTopBottomColumn1(topBtmType, topBtmMode, topBtmCount, topBtmcolNam);
        }
    }

    private static void updateSubTotalTopBottomPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String topBtmType = null;
        String topBtmMode = null;
        int topBtmCount = 0;
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("subTotalTopBtmType".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <TopBottomType>
                    topBtmType = xmlStreamReader.getText();
                }
                if ("subTotalTopBtmMode".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <TopBottomMode>
                    topBtmMode = xmlStreamReader.getText();
                }
                if ("subTotalTopBtmCount".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //text of <TopBottomCount>
                    topBtmCount = Integer.parseInt(xmlStreamReader.getText());
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "TopBottom".equals(getName(xmlStreamReader))) {
                break;
            }
        }
        container.setIsSubtotalTopBottom(true);
        container.setSubTotalTopBottomCount(topBtmCount);
//       container.setSubTotalTopBottomColumn(topBtmType, ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE, topBtmCount);
        container.setSubTotalTopBottomColumn(topBtmType, ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE, topBtmCount);
//       container.setTopBottomColumn(topBtmType, topBtmMode, topBtmCount);
    }

    private static String getText(XMLStreamReader reader) {
        if (reader.hasText()) {
            return reader.getText();
        }
        return "";
    }

    private static String getName(XMLStreamReader reader) {
        if (reader.hasName()) {
            return reader.getLocalName();
        }
        return "";
    }

    public boolean accept(XMLStreamReader reader) {
        if (!reader.isStartElement() && !reader.isEndElement() && !reader.isCharacters()) {
            return false;
        } else {
            return true;
        }
    }

    private static void updateTableSearchInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        ArrayList<String> searchColumns = new ArrayList<String>();
        ArrayList<String> searchCondition = new ArrayList<String>();
        ArrayList<Object> searchValue = new ArrayList<Object>();

        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("Search".equals(getName(xmlStreamReader))) {

                    eventType = xmlStreamReader.next(); //<SearchColumn>
                    eventType = xmlStreamReader.next();

                    //reached ColumnName
                    searchColumns.add(getText(xmlStreamReader));

                    eventType = xmlStreamReader.next(); //</SearchColumn>

                    eventType = xmlStreamReader.next(); //<searchCondition>
                    eventType = xmlStreamReader.next();

                    String srchCurrentCond = getText(xmlStreamReader);
                    searchCondition.add(srchCurrentCond);
                    eventType = xmlStreamReader.next(); //</searchCondition>


                    eventType = xmlStreamReader.next(); //<searchValue>
                    eventType = xmlStreamReader.next();

                    //reached Searchvalue
                    searchValue.add(getText(xmlStreamReader));
                    eventType = xmlStreamReader.next(); //</searchValue>

                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "TableSearch".equals(getName(xmlStreamReader))) {
                break;
            }

        }


        if (!searchColumns.isEmpty()) {
            int index = 0;
            for (String srchcolumn : searchColumns) {
                container.setSearchColumn(srchcolumn, searchCondition.get(index), searchValue.get(index).toString(), null);
                index++;
            }
        }

    }

    private static void updateTableSubTotalSearchInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        ArrayList<String> searchColumns = new ArrayList<String>();
        ArrayList<String> searchCondition = new ArrayList<String>();
        ArrayList<Object> searchValue = new ArrayList<Object>();

        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("subTotalSearch".equals(getName(xmlStreamReader))) {

                    eventType = xmlStreamReader.next(); //<SearchColumn>
                    eventType = xmlStreamReader.next();

                    //reached ColumnName
                    searchColumns.add(getText(xmlStreamReader));

                    eventType = xmlStreamReader.next(); //</SearchColumn>

                    eventType = xmlStreamReader.next(); //<searchCondition>
                    eventType = xmlStreamReader.next();

                    String srchCurrentCond = getText(xmlStreamReader);
                    searchCondition.add(srchCurrentCond);
                    eventType = xmlStreamReader.next(); //</searchCondition>


                    eventType = xmlStreamReader.next(); //<searchValue>
                    eventType = xmlStreamReader.next();

                    //reached Searchvalue
                    searchValue.add(getText(xmlStreamReader));
                    eventType = xmlStreamReader.next(); //</searchValue>

                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "TableSubTotalSearch".equals(getName(xmlStreamReader))) {
                break;
            }

        }


        if (!searchColumns.isEmpty()) {
            container.setIsSubTotalSearchFilterApplied(true);
            int index = 0;
            for (String srchcolumn : searchColumns) {
                container.setSubToTalSearchColumn(srchcolumn, searchCondition.get(index), searchValue.get(index).toString(), null);
                index++;
            }
        }

    }

    private void updateMeasureGroupContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String measureGrpNames = "";
        ArrayList<String> measureGrpColumnNames = new ArrayList<String>();
        ArrayList<String> measureGrpColumnIDs = new ArrayList<String>();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("MeasureGroup".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    eventType = xmlStreamReader.next();
                    measureGrpNames = getText(xmlStreamReader);
                    eventType = xmlStreamReader.next();
                    eventType = xmlStreamReader.next();

                    while (xmlStreamReader.hasNext()) {
                        if ("ColumnID".equals(getName(xmlStreamReader))) {
                            eventType = xmlStreamReader.next();

                            measureGrpColumnIDs.add(getText(xmlStreamReader));
                        } else if ("ColumnName".equals(getName(xmlStreamReader))) {
                            eventType = xmlStreamReader.next();
                            measureGrpColumnNames.add(getText(xmlStreamReader));
                        }


                        eventType = xmlStreamReader.next();
                        eventType = xmlStreamReader.next();


                        if (eventType == XMLStreamConstants.END_ELEMENT && "MeasureGroup".equals(getName(xmlStreamReader))) {
                            break;
                        }
                    }


                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "MeasureGroups".equals(getName(xmlStreamReader))) {
                break;
            }
            MeasureGroup measureGroup = new MeasureGroup(measureGrpNames);
            for (int i = 0; i < measureGrpColumnNames.size(); i++) {
                MeasureGroupColumn groupColumn = new MeasureGroupColumn(measureGrpColumnIDs.get(i), measureGrpColumnNames.get(i));
                measureGroup.addMeasureGroupColumn(groupColumn);
            }
            measureGrpColumnNames = new ArrayList<String>();
            measureGrpColumnIDs = new ArrayList<String>();
            container.addMeasureGroups(measureGroup);
        }
    }

    private static void updatcustomTotalPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("custTotName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setCustTotName(xmlStreamReader.getText());
                }
                if ("mappedTo".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setMappedTo(xmlStreamReader.getText());
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "custTotal".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }

    private static void updatrenameTotalPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String OriginalName = "";
        String renamedTotalName = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("originalTotalName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setoriginalTotalName(xmlStreamReader.getText());
                    OriginalName = xmlStreamReader.getText();
                }
                if ("RenamedTotalName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setRenamedTotalName(xmlStreamReader.getText());
                    renamedTotalName = xmlStreamReader.getText();
                }

                container.addRenameDetails(OriginalName, renamedTotalName);

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "renameTotal".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }

    private static void updatenablecomparisionInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String OriginalName = "";
        String priormsr = "";
        container.setisComparisionEnabled(true);
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("originalmsr".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setoriginalTotalName(xmlStreamReader.getText());
                    OriginalName = xmlStreamReader.getText();
                }
                if ("priormsr".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setRenamedTotalName(xmlStreamReader.getText());
                    priormsr = xmlStreamReader.getText();
                }

                container.addComparisionEnabledmsr(OriginalName, priormsr);

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "enablecomparision".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }

    private static void updaterowadding(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String keyid = "";
        String rowpositionvalue = "";
        container.setaddrowenable(true);
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("newrowEntry".equals(getName(xmlStreamReader))) {

                    if (keyid != null && !keyid.isEmpty() && !keyid.equals("") && rowpositionvalue != null && !rowpositionvalue.equals("")) {

                        container.rowaddingmapkpi(keyid, rowpositionvalue);
                        keyid = "";
                        rowpositionvalue = "";
                    }
                }
                if ("rowid".equals(getName(xmlStreamReader))) {

                    eventType = xmlStreamReader.next();
                    keyid = xmlStreamReader.getText();
                }
                if ("rowvalue".equals(getName(xmlStreamReader))) {

                    eventType = xmlStreamReader.next();
                    rowpositionvalue = xmlStreamReader.getText();
                }



            }
            if (keyid != null && !keyid.isEmpty() && !keyid.equals("") && rowpositionvalue != null && !rowpositionvalue.equals("")) {
                container.rowaddingmapkpi(keyid, rowpositionvalue);
                keyid = "";
                rowpositionvalue = "";
            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "addrowenable".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }
    //start of code by Bhargavi for col gt as avg for sum measures

    private static void updateCTcolGtAggType(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String MsrID = "";
        String aggType = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {

                if ("MsrID".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();

                    MsrID = xmlStreamReader.getText();
                }
                if ("modifiedCTcolGtAggType".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();

                    aggType = xmlStreamReader.getText();
                }

                container.setCTcolGtAggType(MsrID, aggType);

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "CTcolGtAggType".equals(getName(xmlStreamReader))) {
                break;
            }

        }

    }
//end of code by Bhargavi for col gt as avg for sum measures
    private static void setqfilters(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;

        ArrayList<String> viewbyids = new ArrayList<String>();
        String aggType = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {

                if ("qfilters".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();

                    viewbyids.add(xmlStreamReader.getText());
                }



            }
            container.setqfilters(viewbyids);
            if (eventType == XMLStreamConstants.END_ELEMENT && "grandtotalZerovalue".equals(getName(xmlStreamReader))) {
                break;
            }

        }

    }
    //added by sruthi for hideGtZero

    private static void hidemeasureset(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;

        ArrayList<String> MsrID = new ArrayList<String>();
        String aggType = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {

                if ("grandtotalZeromrs".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();

                    MsrID.add(xmlStreamReader.getText());
                }



            }
            container.sethidegtzero1(MsrID);
            if (eventType == XMLStreamConstants.END_ELEMENT && "grandtotalZerovalue".equals(getName(xmlStreamReader))) {
                break;
            }

        }

    }
//ended by sruthi

    private static void selectedViewby(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        ArrayList<String> MsrID = new ArrayList<String>();
        String aggType = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("selectedViewby".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    MsrID.add(xmlStreamReader.getText());
                }
            }
            container.setSelectedviewby(MsrID);
            if (eventType == XMLStreamConstants.END_ELEMENT && "selectedviews".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }

    private static void updateGroupPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String sortColumns = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("GroupColumn".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next(); //<ColumnName>
                    eventType = xmlStreamReader.next();

                    //reached ColumnName
                    sortColumns = getText(xmlStreamReader);

                    eventType = xmlStreamReader.next(); //</ColumnName>

                    eventType = xmlStreamReader.next(); //<SortType>
                    eventType = xmlStreamReader.next();

                    //reached SortType

                    eventType = xmlStreamReader.next(); //</SortType>

                }
            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "Group".equals(getName(xmlStreamReader))) {
                break;
            }
        }

        if (!sortColumns.isEmpty()) {
            container.setGroupColumn(sortColumns);



        }
    }

    private static void updatRowrenameTotalPropertiesInContainer(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("originalRowTotalName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setOriginalRowTotalName(xmlStreamReader.getText());
                }
                if ("RenamedRowTotalName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    container.setRowRenamedTotalName(xmlStreamReader.getText());
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "rowGrandTotalRename".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }

    private static void updatreSummeraizedMeasuresNames(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String MsrID = "";
        String renamedTotalName = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("SummerizedMsrEntry".equals(getName(xmlStreamReader))) {
                    if (MsrID != null && !MsrID.isEmpty() && !MsrID.equals("") && renamedTotalName != null && !renamedTotalName.isEmpty() && !renamedTotalName.equals("")) {
                        container.changeLabelForMeasure(MsrID, renamedTotalName);
                        MsrID = "";
                        renamedTotalName = "";
                    }
                }

                if ("SummerizedMsrID".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();

                    MsrID = xmlStreamReader.getText();
                }
                if ("RenamedSummerizedMsrName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();

                    renamedTotalName = xmlStreamReader.getText();
                }



            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "SummerizedMsrRenames".equals(getName(xmlStreamReader))) {

                if (MsrID != null && !MsrID.isEmpty() && !MsrID.equals("") && renamedTotalName != null && !renamedTotalName.isEmpty() && !renamedTotalName.equals("")) {
                    container.changeLabelForMeasure(MsrID, renamedTotalName);
                }

                break;
            }
        }

    }
    //Start of code by Bhargavi for rowcolor in kpi on 26th Dec 2014

    private static void updateRowColorForkpi(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String measureName = "";
        String color = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("originalTotalName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    measureName = xmlStreamReader.getText();
                }
                if ("Appliedcolor".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    color = xmlStreamReader.getText();
                }

                container.setrowcolorForkpi(measureName, color);

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "rowColoring".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }
    //sandeep

    private static void upsatecrossalign(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String type = "";
        String val = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("crossalignmentkey".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    type = xmlStreamReader.getText();
                }
                if ("crossalignmentval".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    val = xmlStreamReader.getText();
                }

                container.crossalign(type, val);

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "crossalign".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }

    private static void updateRowText(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String measureName = "";
        String text = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("originalTotalName".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    measureName = xmlStreamReader.getText();
                }
                if ("AppliedText".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    text = xmlStreamReader.getText();
                }

                container.setRowText(measureName, text);

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "rowText".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }
//Start of code by Bhargavi for edit font formats in kpi on 26th Dec 2014

    private static void updateFontFormats(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String MsrID = "";
        HashMap<String, String> hybTableHashMap = new HashMap<String, String>();
        String format = "";
        Gson gson = new Gson();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {

                if ("MsrID".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();

                    MsrID = xmlStreamReader.getText();
                }
                if ("modifiedFontTypes".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    format = xmlStreamReader.getText();

                    Type mapTarType = new TypeToken<Map<String, String>>() {
                    }.getType();
                    hybTableHashMap = gson.fromJson(format, mapTarType);
                }
                container.setFontFormatsType(MsrID, hybTableHashMap);
            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "fontFormats".equals(getName(xmlStreamReader))) {
                break;
            }

        }

    }
    //end of code By Bhargavi
    //added by sruthi for custom header

    public static void coustamHeaderset(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String customkeys = "";
        String customdata = "";
        HashMap<String, String> customheader = new HashMap<String, String>();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("CustomHeaderKeys".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    customkeys = xmlStreamReader.getText();
                }
                if ("CustomHeaderData".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    customdata = xmlStreamReader.getText();
                }
                customheader.put(customkeys, customdata);
                container.setCustomHeader(customheader);

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "CustomHeader".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }
    //ended by sruthi
    //added by sruthi for tablecolumn pro

    public static void TableColumnProperties(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String tableColumnkeys = "";
        String format = "";
        //  ArrayList<String> tableColumndata=new ArrayList<String>();
        HashMap<String, ArrayList<String>> tableColumn = new HashMap<String, ArrayList<String>>();
        Gson gson = new Gson();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("TableColumnPropertiesHeaderKeys".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    tableColumnkeys = xmlStreamReader.getText();
                }
                if ("TableColumnPropertiesHeaderData".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    ArrayList<String> tableColumndata = new ArrayList<String>();
                    format = xmlStreamReader.getText().replace("[", "").replace("]", "");
                    String[] format1 = format.split(",");
                    for (String data : format1) {
                        tableColumndata.add(data);
                    }
                    // tableColumndata.add(format.replace("[[", "").replace("]]", "").replace("[", "").replace("]", ""));
                    tableColumn.put(tableColumnkeys, tableColumndata);
                    container.setTableColumnProperties(tableColumn);

                    //  Type mapTarType = new TypeToken<ArrayList<String>>() {
                    // }.getType();
                    //  tableColumndata=gson.fromJson(format,mapTarType);
                    //  tableColumn.put(tableColumnkeys,tableColumndata);
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "TableColumnPropertiesHeader".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }//ended by sruthi
    //added by sruthi for measurecolor

    public static void MeasureColorHeader(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String measurecolorkeys = "";
        String format = "";
        HashMap<String, ArrayList<String>> measurecolor = new HashMap<String, ArrayList<String>>();
        Gson gson = new Gson();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("MeasureColorHeaderKeys".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    measurecolorkeys = xmlStreamReader.getText();
                }
                if ("MeasureColorData".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    ArrayList<String> measurecolordata = new ArrayList<String>();
                    format = xmlStreamReader.getText();//.replace("[", "").replace("]", "");
                    container.setMeasureColor(measurecolorkeys, "#" + format);
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "MeasureHeader".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }//ended by sruthi
    //added by sruthi for measureText color

    public static void MeasureTextColor(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        String measurecolorkeys = "";
        String format = "";
        HashMap<String, ArrayList<String>> measurecolor = new HashMap<String, ArrayList<String>>();
        Gson gson = new Gson();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("MeasurescriptColorKeys".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    measurecolorkeys = xmlStreamReader.getText();
                }
                if ("MeasurescriptColorData".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    ArrayList<String> measurecolordata = new ArrayList<String>();
                    format = xmlStreamReader.getText();//.replace("[", "").replace("]", "");
                    container.setTextColor(measurecolorkeys, "#" + format);
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "MeasurescriptColor".equals(getName(xmlStreamReader))) {
                break;
            }
        }
    }//ended by sruthi     
    //added by sruthi for alldetails

    private static void setAlldetails(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException {
        int eventType;
        ArrayList<String> MsrID = new ArrayList<String>();
        String aggType = "";
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("selectedalldata".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    MsrID.add(xmlStreamReader.getText());
                }
            }
            container.setAllDetails(MsrID);
            if (eventType == XMLStreamConstants.END_ELEMENT && "selectedalldetailslis".equals(getName(xmlStreamReader))) {
                break;
            }
        }

    }
    //ended by sruthi      
     //added by sruthi for viewbycolumnproperties         
               public static void ViewbyAligns(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException{
                 int eventType;
        String ViewbyAlignsDatakeys="";
         String format="";
       // HashMap<String,ArrayList<String>> ViewbyAligns=new HashMap<String,ArrayList<String>>();
         Gson gson=new Gson();
        while ( xmlStreamReader.hasNext() )
        {
            eventType = xmlStreamReader.next();
            if ( eventType == XMLStreamConstants.START_ELEMENT )
            {
                if ( "ViewbyAlignsKeys".equals(getName(xmlStreamReader)) )
                {
                    eventType = xmlStreamReader.next();
                   ViewbyAlignsDatakeys=xmlStreamReader.getText();
}
                if ( "ViewbyAlignsData".equals(getName(xmlStreamReader)))
                {
                    eventType = xmlStreamReader.next();
                  //  ArrayList<String> measurecolordata=new ArrayList<String>();
                   format=xmlStreamReader.getText();//.replace("[", "").replace("]", "");
               container.setviewbyAlignments(ViewbyAlignsDatakeys,format);
                }
             
}
            if ( eventType == XMLStreamConstants.END_ELEMENT &&  "ViewbyAligns".equals(getName(xmlStreamReader)) )
                break;
        }
         }
                public static void viewbydataAligns(Container container, XMLStreamReader xmlStreamReader) throws XMLStreamException{
                 int eventType;
        String ViewbyAlignsDatakeys="";
         String format="";
       // HashMap<String,ArrayList<String>> ViewbyAligns=new HashMap<String,ArrayList<String>>();
         Gson gson=new Gson();
        while ( xmlStreamReader.hasNext() )
        {
            eventType = xmlStreamReader.next();
            if ( eventType == XMLStreamConstants.START_ELEMENT )
            {
                if ( "viewbydataAlignsKeys".equals(getName(xmlStreamReader)) )
                {
                    eventType = xmlStreamReader.next();
                   ViewbyAlignsDatakeys=xmlStreamReader.getText();
}
                if ( "viewbydataAlignsData".equals(getName(xmlStreamReader)))
                {
                    eventType = xmlStreamReader.next();
                  //  ArrayList<String> measurecolordata=new ArrayList<String>();
                   format=xmlStreamReader.getText();//.replace("[", "").replace("]", "");
               container.setViewbydataAlignments(ViewbyAlignsDatakeys,format);
                }
             
}
            if ( eventType == XMLStreamConstants.END_ELEMENT &&  "viewbydataAligns".equals(getName(xmlStreamReader)) )
                break;
        }
         }
    //ended by sruthi
                //added by anitha for MTD,QTD,YTD in AO report
    public void IsRunTimeComparisionMsr(Container container, StringBuilder tablePropertiesXml) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream input = new ByteArrayInputStream(tablePropertiesXml.toString().getBytes());
        int eventType;
        try {
            XMLStreamReader xmlStreamReader = inputFactory.createFilteredReader(inputFactory.createXMLStreamReader(input), this);
        String tableColumnkeys = "";
        String format = "";
        HashMap<String, ArrayList<String>> tableColumn = new HashMap<String, ArrayList<String>>();
        Gson gson = new Gson();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("RunTimeComparisionMsrKeys".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    tableColumnkeys = xmlStreamReader.getText();
                }
                if ("RunTimeComparisionMsrData".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    ArrayList<String> tableColumndata = new ArrayList<String>();
                    format = xmlStreamReader.getText().replace("[", "").replace("]", "");
                    String[] format1 = format.split(",");
                    for (String data : format1) {
                        tableColumndata.add(data.trim());
                    }
                    tableColumn.put(tableColumnkeys, tableColumndata);
                    container.setRunTimeComparisionMsrHashMap(tableColumn);
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "RunTimeComparisionMsr".equals(getName(xmlStreamReader))) {
                break;
            }
        }
        }catch(Exception e){

        }
    }
    //end of code by anitha for MTD,QTD,YTD in AO report
     //added by sruthi for background color in tablecolumn pro
public void measurebgColor(Container container,  XMLStreamReader xmlStreamReader)throws XMLStreamException{
    int eventType;
        String measurecolorkeys = "";
        String format = "";
        HashMap<String, ArrayList<String>> measurecolor = new HashMap<String, ArrayList<String>>();
        Gson gson = new Gson();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("measurebgColorKeys".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    measurecolorkeys = xmlStreamReader.getText();
                }
                if ("measurebgColorData".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    ArrayList<String> measurecolordata = new ArrayList<String>();
                    format = xmlStreamReader.getText();//.replace("[", "").replace("]", "");
                    container.setMeasureBgColor(measurecolorkeys, "#" + format);
                }

            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "measurebgColor".equals(getName(xmlStreamReader))) {
                break;
            }
        }
}//ended by sruthi
//added by anitha for RT Time Agg Compare with
    public void rtMeasureCompareWith(Container container, StringBuilder tablePropertiesXml) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream input = new ByteArrayInputStream(tablePropertiesXml.toString().getBytes());
        int eventType;
        try {
            XMLStreamReader xmlStreamReader = inputFactory.createFilteredReader(inputFactory.createXMLStreamReader(input), this);
        String tableColumnkeys = "";
        String format = "";
        HashMap<String, String> tableColumn = new HashMap<String, String>();
        Gson gson = new Gson();
        while (xmlStreamReader.hasNext()) {
            eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                if ("rtMeasureCompareWithKeys".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    tableColumnkeys = xmlStreamReader.getText();
                }
                if ("rtMeasureCompareWithData".equals(getName(xmlStreamReader))) {
                    eventType = xmlStreamReader.next();
                    format=xmlStreamReader.getText();
                    if(container.getrtMeasureCompareWith(tableColumnkeys)!=null &&container.getrtMeasureCompareWith(tableColumnkeys).equalsIgnoreCase("reset")){
                        container.setrtMeasureCompareWith(tableColumnkeys, "reset");
                    }else if(container.getrtMeasureCompareWith(tableColumnkeys)!=null &&!container.getrtMeasureCompareWith(tableColumnkeys).equalsIgnoreCase("")){
                        container.setrtMeasureCompareWith(tableColumnkeys, container.getrtMeasureCompareWith(tableColumnkeys));
                    }else{
                    container.setrtMeasureCompareWith(tableColumnkeys, format);                    
                }                                                
            }
            }
            if (eventType == XMLStreamConstants.END_ELEMENT && "rtMeasureCompareWith".equals(getName(xmlStreamReader))) {
                break;
            }
        }
        }catch(Exception e){

        }
    }
    //end of code by anitha for RT Time Agg Compare with

}
