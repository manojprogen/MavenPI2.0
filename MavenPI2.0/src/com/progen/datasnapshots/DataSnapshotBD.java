/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datasnapshots;

import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.db.ProgenDataSet;
import com.progen.report.colorgroup.ColorCodeBuilder;
import com.progen.report.*;
import com.progen.report.util.sort.DataSetFilter;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.ReportSchedulePreferences;
import com.progen.scheduler.ReportScheduleSlices;
import com.progen.scheduler.UserDimensionMap;
import com.progen.scheduler.db.TimeHelper;
import com.progen.servlet.ServletUtilities;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author arun
 */
public class DataSnapshotBD {

    public static Logger logger = Logger.getLogger(DataSnapshotBD.class);

    public String getFileName(String reportId, String userId) {
        return ServletUtilities.prefix + reportId + "_" + userId + "_DS";
    }

    public Iterable<String> createDataSnapshot(DataSnapshot snapShot, String userId, String fromHtml, String advHtmlFileProps, String snapName) {
        //  ServletWriterTransferObject swt;
        Container container;
        String reportSnapshot;
        String xmlSnapshot = "";
        ArrayList<String> snapShotFiles = new ArrayList<String>();
        //
        DataSnapshotDAO snapShotDAO = new DataSnapshotDAO();
        Clob clobObj = null;
        Clob xmlColbObj = null;
        String htmlStatus = "";
        if (fromHtml.equalsIgnoreCase("fromAdvancedHtml")) {
            htmlStatus = "Advanced";
        } else {
            htmlStatus = "Basic";
        }

        try {
            container = this.generateContainer(snapShot.getReportId(), userId, null, null);
            if (!snapName.equalsIgnoreCase("")) {
                snapShot.setSnapShotName(snapName);
            } else {
                snapShot.setSnapShotName(container.getReportCollect().reportName);
            }
            int snapShotId = snapShotDAO.insertDataSnapshot(snapShot, Integer.parseInt(userId), htmlStatus);
            reportSnapshot = downloadAndSaveDataSnapshot(container, userId);

            if (fromHtml.equalsIgnoreCase("fromAdvancedHtml")) {
                //xmlSnapshot=downloadAndSaveXmlSnapshot(container,userId);
                xmlSnapshot = snapShotDAO.getAdvancedHtmlReturnObjectFile(container, userId, advHtmlFileProps);
                snapShotFiles.add(xmlSnapshot);
                snapShot.setAdvSnapshotFileName(xmlSnapshot);
                snapShotDAO.updateAdvancedHtmlFileName(xmlSnapshot, snapShotId);
            }
            snapShotFiles.add(reportSnapshot);

            if (snapShotId != -1) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    String clobObj1 = null;
                    clobObj1 = updateSnapShotHtmlViewString(snapShotId, reportSnapshot, "fromHtml");
                } else {
                    clobObj = updateSnapShotHtmlView(snapShotId, reportSnapshot, "fromHtml");
                }
                // if(fromHtml.equalsIgnoreCase("fromAdvancedHtml"))
                //xmlColbObj=updateSnapshotXml(snapShotId,xmlSnapshot);
                // clobObj.free();
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        return snapShotFiles;
    }

    private Clob updateSnapShotHtmlView(int snapShotId, String fileName, String fromOption) {
        Clob clobObj = null;
        DataSnapshotDAO snapShotDAO = new DataSnapshotDAO();
        Reader reader = null;
        try {
            reader = ServletUtilities.createBufferedReader(fileName);
            clobObj = snapShotDAO.updateSnapshotHtmlView(snapShotId, reader, fromOption);

            reader.close();
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
        }
        return clobObj;
    }

    private String updateSnapShotHtmlViewString(int snapShotId, String fileName, String fromOption) {
        String clobObj = null;
        DataSnapshotDAO snapShotDAO = new DataSnapshotDAO();
        Reader reader = null;
        try {
            reader = ServletUtilities.createBufferedReader(fileName);
//                 if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//
//                }
//                else
//                 {
            clobObj = snapShotDAO.updateSnapShotHtmlViewString(snapShotId, reader, fromOption);
//                }
            reader.close();
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
        }
        return clobObj;
    }

    public ArrayList<String> downloadDataSnapShotHtml(String[] reportIds, String userId, boolean allViewBys) throws ParseException {
        ArrayList<String> fileNames = new ArrayList<String>();
        //  ServletWriterTransferObject swt;
        String snapshotFileName;
        Container container;
        //
        for (String reportId : reportIds) {
            container = this.generateContainer(reportId, userId, null, null);
            snapshotFileName = this.downloadAndSaveDataSnapshot(container, userId);
            fileNames.add(snapshotFileName);


            if (allViewBys) {
                Iterable<String> rowViewBys = container.getReportCollect().formulatePossibleRowViewBys();
                for (String rowViewBy : rowViewBys) {
                    Container containerViewBy = this.generateContainerForRowViewBy(container, rowViewBy, userId);
                    snapshotFileName = this.downloadAndSaveDataSnapshot(containerViewBy, userId);
                    fileNames.add(snapshotFileName);
                }
            }
        }
        return fileNames;
    }

    // for Scheduling reports
    public ArrayList<String> downloadDataSnapShotHtmlOrPdfOrExcel(String[] reportIds, String userId, ReportSchedule schedule) throws ParseException {
        ArrayList<String> fileNames = new ArrayList<String>();
        String snapshotFileName;
        Container container = null;
        Container container1 = null;
        Container container2 = null;
        Container container3 = null;
        Container container4 = null;
        Container container5 = null;
        Container container6 = null;
        ArrayList<String> dimValueArr = new ArrayList<String>();
        String dimId = "";
        boolean isAutoSplit = schedule.isIsAutoSplited();
        String viewByType = schedule.getViewBy();
        List<UserDimensionMap> userDimMapList = schedule.getUsermap();
        List<ReportSchedulePreferences> schdPreferenceList = schedule.getReportSchedulePrefrences();
        List<ReportScheduleSlices> schdSliceList = schedule.getReportScheduleSlices();


        if (isAutoSplit && userDimMapList != null) {
            List<ReportSchedulePreferences> reptSchedulerPrfr = this.getReportSchedulePreferences(schedule.getUsermap());
            schdPreferenceList.addAll(reptSchedulerPrfr);
        }


        String dimVal = "";
        for (String reportId : reportIds) {
            ArrayList<String> rowViewByList = new ArrayList<String>();
            if (isTimeViewBy(schedule.getViewById())) {
                rowViewByList.add("TIME");
            } else {
                rowViewByList.add(schedule.getViewById());
            }
            List arrList = schedule.getDataSelectionTypes();
            //container = this.generateContainer(reportId, userId, rowViewByList, schedule);
            container1 = this.generateContainer(reportId, userId, rowViewByList, (String) arrList.get(0), schedule);
            if (arrList.size() > 1) {
                container2 = this.generateContainer(reportId, userId, rowViewByList, (String) arrList.get(1), schedule);
            }
            if (arrList.size() > 2) {
                container3 = this.generateContainer(reportId, userId, rowViewByList, (String) arrList.get(2), schedule);
            }
            if (arrList.size() > 3) {
                container4 = this.generateContainer(reportId, userId, rowViewByList, (String) arrList.get(3), schedule);
            }
            if (arrList.size() > 4) {
                container5 = this.generateContainer(reportId, userId, rowViewByList, (String) arrList.get(4), schedule);
            }
            if (arrList.size() > 5) {
                container6 = this.generateContainer(reportId, userId, rowViewByList, (String) arrList.get(5), schedule);
            }


            if ("html".equalsIgnoreCase(schedule.getContentType())) {

                if (arrList.size() >= 1) {
                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container1, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container1.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container1.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container1.getRetObj().setViewSequence(intvals);
                        }

                        container1.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container1.setGrandTotalReq(false);
                            container1.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container1.setGrandTotalReq(false);
                            container1.setNetTotalReq(true);
                        } else {
                            container1.setGrandTotalReq(true);
                            container1.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshot(container1, userId);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 1) {
                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container2, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container2.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container2.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container2.getRetObj().setViewSequence(intvals);
                        }

                        container2.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container2.setGrandTotalReq(false);
                            container2.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container2.setGrandTotalReq(false);
                            container2.setNetTotalReq(true);
                        } else {
                            container2.setGrandTotalReq(true);
                            container2.setNetTotalReq(true);
                        }
                    }

                    snapshotFileName = this.downloadAndSaveDataSnapshot(container2, userId);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 2) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container3, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container3.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container3.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container3.getRetObj().setViewSequence(intvals);
                        }

                        container3.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container3.setGrandTotalReq(false);
                            container3.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container3.setGrandTotalReq(false);
                            container3.setNetTotalReq(true);
                        } else {
                            container3.setGrandTotalReq(true);
                            container3.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshot(container3, userId);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 3) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container4, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container4.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container4.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container4.getRetObj().setViewSequence(intvals);
                        }

                        container4.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container4.setGrandTotalReq(false);
                            container4.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container4.setGrandTotalReq(false);
                            container4.setNetTotalReq(true);
                        } else {
                            container4.setGrandTotalReq(true);
                            container4.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshot(container4, userId);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 4) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container5, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container5.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container5.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container5.getRetObj().setViewSequence(intvals);
                        }

                        container5.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container5.setGrandTotalReq(false);
                            container5.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container5.setGrandTotalReq(false);
                            container5.setNetTotalReq(true);
                        } else {
                            container5.setGrandTotalReq(true);
                            container5.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshot(container5, userId);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 5) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container6, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container6.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container6.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container6.getRetObj().setViewSequence(intvals);
                        }

                        container6.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container6.setGrandTotalReq(false);
                            container6.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container6.setGrandTotalReq(false);
                            container6.setNetTotalReq(true);
                        } else {
                            container6.setGrandTotalReq(true);
                            container6.setNetTotalReq(true);
                        }
                    }

                    snapshotFileName = this.downloadAndSaveDataSnapshot(container6, userId);
                    fileNames.add(snapshotFileName);
                }
            }

            if ("pdf".equalsIgnoreCase(schedule.getContentType())) {
                if (arrList.size() >= 1) {
                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container1, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container1.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container1.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container1.getRetObj().setViewSequence(intvals);
                        }

                        container1.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container1.setGrandTotalReq(false);
                            container1.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container1.setGrandTotalReq(false);
                            container1.setNetTotalReq(true);
                        } else {
                            container1.setGrandTotalReq(true);
                            container1.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotPdf(container1);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 1) {
                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container2, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container2.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container2.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container2.getRetObj().setViewSequence(intvals);
                        }

                        container2.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container2.setGrandTotalReq(false);
                            container2.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container2.setGrandTotalReq(false);
                            container2.setNetTotalReq(true);
                        } else {
                            container2.setGrandTotalReq(true);
                            container2.setNetTotalReq(true);
                        }
                    }

                    snapshotFileName = this.downloadAndSaveDataSnapshotPdf(container2);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 2) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container3, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container3.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container3.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container3.getRetObj().setViewSequence(intvals);
                        }

                        container3.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container3.setGrandTotalReq(false);
                            container3.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container3.setGrandTotalReq(false);
                            container3.setNetTotalReq(true);
                        } else {
                            container3.setGrandTotalReq(true);
                            container3.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotPdf(container3);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 3) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container4, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container4.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container4.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container4.getRetObj().setViewSequence(intvals);
                        }

                        container4.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container4.setGrandTotalReq(false);
                            container4.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container4.setGrandTotalReq(false);
                            container4.setNetTotalReq(true);
                        } else {
                            container4.setGrandTotalReq(true);
                            container4.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotPdf(container4);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 4) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container5, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container5.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container5.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container5.getRetObj().setViewSequence(intvals);
                        }

                        container5.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container5.setGrandTotalReq(false);
                            container5.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container5.setGrandTotalReq(false);
                            container5.setNetTotalReq(true);
                        } else {
                            container5.setGrandTotalReq(true);
                            container5.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotPdf(container5);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 5) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container6, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container6.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container6.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container6.getRetObj().setViewSequence(intvals);
                        }

                        container6.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container6.setGrandTotalReq(false);
                            container6.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container6.setGrandTotalReq(false);
                            container6.setNetTotalReq(true);
                        } else {
                            container6.setGrandTotalReq(true);
                            container6.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotPdf(container6);
                    fileNames.add(snapshotFileName);
                }

            }
            if ("excel".equalsIgnoreCase(schedule.getContentType())) {
                if (arrList.size() >= 1) {
                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container1, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container1.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container1.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container1.getRetObj().setViewSequence(intvals);
                        }

                        container1.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container1.setGrandTotalReq(false);
                            container1.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container1.setGrandTotalReq(false);
                            container1.setNetTotalReq(true);
                        } else {
                            container1.setGrandTotalReq(true);
                            container1.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotExcel(container1);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 1) {
                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container2, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container2.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container2.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container2.getRetObj().setViewSequence(intvals);
                        }

                        container2.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container2.setGrandTotalReq(false);
                            container2.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container2.setGrandTotalReq(false);
                            container2.setNetTotalReq(true);
                        } else {
                            container2.setGrandTotalReq(true);
                            container2.setNetTotalReq(true);
                        }
                    }

                    snapshotFileName = this.downloadAndSaveDataSnapshotExcel(container2);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 2) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container3, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container3.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container3.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container3.getRetObj().setViewSequence(intvals);
                        }

                        container3.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container3.setGrandTotalReq(false);
                            container3.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container3.setGrandTotalReq(false);
                            container3.setNetTotalReq(true);
                        } else {
                            container3.setGrandTotalReq(true);
                            container3.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotExcel(container3);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 3) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container4, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container4.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container4.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container4.getRetObj().setViewSequence(intvals);
                        }

                        container4.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container4.setGrandTotalReq(false);
                            container4.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container4.setGrandTotalReq(false);
                            container4.setNetTotalReq(true);
                        } else {
                            container4.setGrandTotalReq(true);
                            container4.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotExcel(container4);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 4) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container5, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container5.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container5.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container5.getRetObj().setViewSequence(intvals);
                        }

                        container5.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container5.setGrandTotalReq(false);
                            container5.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container5.setGrandTotalReq(false);
                            container5.setNetTotalReq(true);
                        } else {
                            container5.setGrandTotalReq(true);
                            container5.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotExcel(container5);
                    fileNames.add(snapshotFileName);
                }
                if (arrList.size() > 5) {

                    if (viewByType.equalsIgnoreCase("AllViewBy")) {
                        List<ReportScheduleSlices> reptSchdSlices = this.getReportScheduledSlices(container6, schedule.getViewById());
                        schdSliceList.addAll(reptSchdSlices);
                    }
                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        dimId = schedulePreferences.getDimId();
                        String[] colValues = schedulePreferences.getDimValues().split(",");
                        dimValueArr.addAll(Arrays.asList(colValues));
                    }


                    for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
                        //for filtering each value from all the values of Return Object
                        dimVal = schedulePreferences.getDimValues();
                        String[] dimValArr = dimVal.split(",");
                        List<String> dimValList = Arrays.asList(dimValArr);
                        if (!(dimValList.contains("All"))) {
                            SearchFilter filter = new SearchFilter();
                            ArrayList<Integer> intvals = new ArrayList<Integer>();
                            if (!(dimId.startsWith("A_"))) {
                                dimId = "A_" + dimId;
                            }

                            filter.add(dimId, "IN", dimValList);
                            ArrayList<String> columnList = new ArrayList<String>();
                            columnList.add(dimId);
                            container6.getRetObj().resetViewSequence();

                            DataSetFilter dataSetFilter = new DataSetFilter();
                            dataSetFilter.setData(container6.getRetObj(), columnList);
                            dataSetFilter.setSearchFilter(filter);
                            intvals = dataSetFilter.searchDataSet();
                            container6.getRetObj().setViewSequence(intvals);
                        }

                        container6.getReportCollect().updateParameterDefaults(dimId.replace("A_", ""), dimValList, false);
                        // for getting Grand Total and Category SubTotal depending upon the number of dimension values
                        if (dimValList.size() == 1) {
                            container6.setGrandTotalReq(false);
                            container6.setNetTotalReq(false);
                        } else if (dimValList.size() > 1 && !(dimValList.contains("All"))) {
                            container6.setGrandTotalReq(false);
                            container6.setNetTotalReq(true);
                        } else {
                            container6.setGrandTotalReq(true);
                            container6.setNetTotalReq(true);
                        }
                    }
                    snapshotFileName = this.downloadAndSaveDataSnapshotExcel(container6);
                    fileNames.add(snapshotFileName);
                }

            }

            // For scheduling additional Slices of viewBy except the default viewBys
            if (schdSliceList != null && !schdSliceList.isEmpty()) {
                for (int i = 0; i < fileNames.size(); i++) {
                    for (ReportScheduleSlices repSchdSlices : schdSliceList) {
                        this.addSliceToSnapshot(container, userId, fileNames.get(i), schdPreferenceList.get(i), repSchdSlices);
                    }

                }
            }

        }
        return fileNames;
    }
//    private ServletWriterTransferObject createAndSaveDataSnapShotHtml(String reportId,String userId)
//    {
//        PbReportViewerBD reportViewerBd = new PbReportViewerBD();
//        PbReportCollection collect = new PbReportCollection();
//        collect.reportId = reportId;
//        collect.ctxPath = "";
//        collect.reportIncomingParameters = new HashMap<String,String>();
//        try {
//            collect.getParamMetaData(true);
//
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
//        Container container = reportViewerBd.prepareReport(collect, reportId, userId);
//        DataFacade facade = new DataFacade(container);
//        facade.setUserId(userId);
//        facade.setCtxPath(collect.ctxPath);
//        TableBuilder tableBldr = new RowViewTableBuilder(facade);
//        tableBldr.setFromAndToRow(0,container.getRetObj().getViewSequence().size());
//        TableDisplay displayHelper = null;
//        TableDisplay bodyHelper = null;
//        TableDisplay subTotalHelper = null;
//        displayHelper = new HtmlTableHeaderDisplay(tableBldr);
//        bodyHelper = new HtmlTableBodyDisplay(tableBldr);
//        subTotalHelper = new HtmlTableSubTotalDisplay(tableBldr);
//        ServletWriterTransferObject swt = null;
//
//        try {
//                swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId),"html");
//                Writer writer = swt.writer;
//                displayHelper.setWriter(writer);
//                displayHelper.setNext(bodyHelper).setNext(subTotalHelper);
//
//                StringBuilder tableHtml = new StringBuilder();
//                tableHtml.append(displayHelper.generateOutputHTML());
//                writer.write(tableHtml.toString());
//                writer.flush();
//                writer.close();
//                swt.setReportName(collect.reportName);
//
//        } catch (IOException ex) {
//            logger.error("Exception:",ex);
//        }
//
//        return swt;
//    }

    public Container generateContainer(String reportId, String userId, ArrayList<String> rowViewBylist, ReportSchedule schedule) {
        PbReportViewerBD reportViewerBd = new PbReportViewerBD();
        PbReportCollection collect = new PbReportCollection();

        collect.reportId = reportId;
        collect.ctxPath = "";
        collect.reportIncomingParameters = new HashMap<String, String>();
        try {
            if (rowViewBylist != null && !rowViewBylist.isEmpty()) {
                collect.overrideRowViewByDimId = rowViewBylist.get(0);
            }

            collect.getParamMetaData(true);

            if (schedule != null && schedule.getParameterXml() != null) {
                ReportParameter repParam = new ReportParameter();
                ColorCodeBuilder builder = new ColorCodeBuilder();
                LinkedHashMap<String, String> paramMap = builder.parseReportParamXML(schedule.getParameterXml());
                repParam.setReportParameters(paramMap);

                Set<ReportParameterValue> valSet = repParam.getReportParameterValues();
                for (ReportParameterValue val : valSet) {
                    String elemId = val.getElementId();
                    String elemName = collect.getParameterDispName(elemId);
                    //String paramValue = val.getParameterValues();
                    List<String> paramValueLst = val.getParameterValues();
//                     paramValueLst.add(paramValue);
                    collect.setParameters(elemId, elemName, paramValueLst);
                }
            }

            //UPDATE::: This section of code needs to be uncommented when deployed for production systems.
            // Now, it is temporarily commented because of unavailability of latest data in the database.
            //logic also needs to be upgraded depending on the requirement.


            if (schedule != null) {
                String viewById = schedule.getViewById();
                if (isTimeViewBy(viewById)) {
                    ArrayList timeDetails = createTimeDetails(viewById);
                    collect.timeDetailsArray = timeDetails;
                } else {
                    ArrayList timeDetails = createTimeDetails(schedule);
                    collect.timeDetailsArray = timeDetails;
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        Container container = reportViewerBd.prepareReport(collect, reportId, userId);
        checkTopBottom(container);
        return container;
    }

    private void checkTopBottom(Container container) {
        ArrayList<String> sortCols = null;
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;
        int topbottomCount;
        ArrayList<Integer> rowSequence;
        ProgenDataSet retObj = container.getRetObj();
        String sort = "";

        if (container.isTopBottomSet()) {
            sortCols = container.getSortColumns();
            sortTypes = container.getSortTypes();

            topbottomCount = container.getTopBottomCount();

            if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                sort = "1";
                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                } else {
                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                }

                retObj.setViewSequence(rowSequence);
            } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                sort = "0";
                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                } else {
                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                }
                retObj.setViewSequence(rowSequence);
            }

        }
    }

    private ArrayList createTimeDetails(ReportSchedule schedule) {
        ArrayList timeDetails = new ArrayList();
        Date now = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
        String date = fmt.format(now);
        TimeHelper helper = new TimeHelper();
        helper.elementId = getElemIdForQuery(schedule);

        String frequency = schedule.getFrequency();
        String particularDay = schedule.getParticularDay();
        String dataSelection = schedule.getDataSelection();
        List<String> dataSelection1 = schedule.getDataSelectionTypes();

        if ("1".equals(frequency) || "Daily".equalsIgnoreCase(frequency)) { //Daily

            for (int i = 0; i < dataSelection1.size(); i++) {
                String selectedData = dataSelection1.get(i);

                if ("Current Day".equalsIgnoreCase(selectedData)) {
                    date = helper.getCurrentDay();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_STD");
                    timeDetails.add(date);
                    timeDetails.add("Day");
                    timeDetails.add("Last Period");
                }
                if ("Last Day".equalsIgnoreCase(selectedData)) {

                    date = helper.getLastDay();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_STD");
                    timeDetails.add(date);
                    timeDetails.add("Day");
                    timeDetails.add("Last Period");
                }
                if ("WTD".equalsIgnoreCase(selectedData)) {
                    date = helper.getCurrentWeekStartDate();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_STD");
                    timeDetails.add(date);
                    timeDetails.add("Week");
                    timeDetails.add("Last Period");
                }
//            if("MTD".equalsIgnoreCase(selectedData)){
//                date = helper.getCurrentMonthStartDate();
//                 timeDetails.add("Day");
//                 timeDetails.add("PRG_STD");
//                 timeDetails.add(date);
//                 timeDetails.add("Month");
//                 timeDetails.add("Last Period");
//            }
//            if("QTD".equalsIgnoreCase(selectedData)){
//                date = helper.getCurrentQuarterStartDate();
//                 timeDetails.add("Day");
//                 timeDetails.add("PRG_STD");
//                 timeDetails.add(date);
//                 timeDetails.add("Quarter");
//                 timeDetails.add("Last Period");
//            }
//            else if("YTD".equalsIgnoreCase(selectedData)){
//                date = helper.getCurrentYearStartDate();
//                 timeDetails.add("Day");
//                 timeDetails.add("PRG_STD");
//                 timeDetails.add(date);
//                 timeDetails.add("Year");
//                 timeDetails.add("Last Period");
//            }
            }

        } else if ("2".equals(frequency) || "Monthly".equalsIgnoreCase(frequency)) { //Monthly
            if ("L".equalsIgnoreCase(particularDay) || "B".equalsIgnoreCase(particularDay)) {    //EOM or BOM
                if ("L".equalsIgnoreCase(particularDay)) {    //EOM
                    date = helper.getCurrentDay();
                } else {   //BOM
                    date = helper.getLastDay();
                }
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                timeDetails.add("Month");
                timeDetails.add("Last Period");
            } else {   // Any Specific Date
                if ("last".equalsIgnoreCase(dataSelection)) {    //last month data
                    date = helper.getLastMonthEndDate();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_STD");
                    timeDetails.add(date);
                    timeDetails.add("Month");
                    timeDetails.add("Last Period");
                } else if ("last".equalsIgnoreCase(dataSelection)) {    //current month data
                    date = helper.getCurrentDay();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_STD");
                    timeDetails.add(date);
                    timeDetails.add("Month");
                    timeDetails.add("Last Period");
                } else if ("both".equalsIgnoreCase(dataSelection)) {    //last month data + current month data
                    String startDate = helper.getLastMonthStartDate();
                    String endDate = helper.getCurrentDay();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_DATE_RANGE");
                    timeDetails.add(startDate);
                    timeDetails.add(endDate);
                    timeDetails.add(null);
                    timeDetails.add(null);
                }
            }
        } else if ("3".equals(frequency) || "Weekly".equalsIgnoreCase(frequency)) { //Weekly
            String startDate = "";
            String endDate = "";
            if ("B".equalsIgnoreCase(particularDay)) {
                startDate = helper.getLastWeekStartDate();
                endDate = helper.getLastWeekEndDate();
            } else if ("L".equalsIgnoreCase(particularDay)) {
                startDate = helper.getCurrentWeekStartDate();
                endDate = helper.getCurrentWeekEndDate();
            }
            timeDetails.add("Day");
            timeDetails.add("PRG_DATE_RANGE");
            timeDetails.add(startDate);
            timeDetails.add(endDate);
            timeDetails.add(null);
            timeDetails.add(null);
        }
        return timeDetails;
    }

    private Container generateContainerForRowViewBy(Container container, String rowViewBy, String userId) {
        PbReportViewerBD reportViewerBd = new PbReportViewerBD();
        ArrayList<String> rowViewBys = new ArrayList<String>();
        rowViewBys.add(rowViewBy);
        ArrayList<String> colViewBys = container.getReportCollect().reportColViewbyValues;
        reportViewerBd.changeViewByOfReport(container, userId, rowViewBys, colViewBys);//.prepareReport(collect, collect.reportId, userId);
        return container;
    }

    private String downloadAndSaveDataSnapshot(Container container, String userId) throws ParseException {
        DataSnapshotGenerator SnapshotGenerator = new DataSnapshotGenerator();
        String snapshotFileName = SnapshotGenerator.generateAndStoreHtmlSnapshot(container, userId, "fromHtml");
        return snapshotFileName;
    }

    private String downloadAndSaveXmlSnapshot(Container container, String userId) {
        DataSnapshotGenerator SnapshotGenerator = new DataSnapshotGenerator();
//           ProgenLog.log(ProgenLog.FINE, this, "downloadAndSaveXmlSnapshot creating xml", "Exit");
        logger.info("creating xml Exit");
        String snapshotFileName = SnapshotGenerator.generateXmlSnapshot(container, userId);
//         ProgenLog.log(ProgenLog.FINE, this, "downloadAndSaveXmlSnapshot creating xml", "Exit");
        logger.info("creating xml Exit");
        return snapshotFileName;
    }

    private void addSliceToSnapshot(Container container, String userId, String fileName, ReportSchedulePreferences preference, ReportScheduleSlices sliceInfo) {
        PbReportCollection collect = container.getReportCollect();
        ArrayList<String> rowViewBys = new ArrayList<String>();
        ArrayList<String> timeDetails = null;
        String sliceViewBy = sliceInfo.getRowViewById();
        if (isTimeViewBy(sliceViewBy)) {
            rowViewBys.add("TIME");
            ArrayList contTimeDetails = container.getTimeDetailsArray();
            timeDetails = new ArrayList<String>();

            String timeType = (String) contTimeDetails.get(1);
            String endDate = "";
            if ("PRG_STD".equalsIgnoreCase(timeType)) {
                endDate = (String) contTimeDetails.get(2);
            } else {
                endDate = (String) contTimeDetails.get(3);
            }

            timeDetails.add("Day");
            timeDetails.add("PRG_STD");
            timeDetails.add(endDate);
            timeDetails.add(sliceViewBy);
            timeDetails.add("Last Period");
            sliceViewBy = "TIME";
        } else {
            rowViewBys.add(sliceViewBy);
        }

        List<String> defaultDimValList = collect.getDefaultValue(preference.getDimId());
        String prefDimVal = preference.getDimValues();
        boolean doSwap = false;

        if (defaultDimValList != null && defaultDimValList.contains("All")) {
            if ("All".equalsIgnoreCase(prefDimVal)) {
            } else {
                doSwap = true;
                String[] paramValArr = preference.getDimValues().split(",");
                List<String> paramValList = Arrays.asList(paramValArr);
                collect.updateParameterDefaults(preference.getDimId(), paramValList, false);
            }
        } else {
            String[] paramValArr = preference.getDimValues().split(",");
            List<String> paramValList = Arrays.asList(paramValArr);
            collect.updateParameterDefaults(preference.getDimId(), paramValList, false);
        }
//        paramValArr = sliceInfo.getRowViewByVal().split(",");
//        paramValList = Arrays.asList(paramValArr);
//        collect.updateParameterDefaults(sliceViewBy, paramValList, false);

        PbReportViewerBD viewerBD = new PbReportViewerBD();
        viewerBD.setRowViewIds(rowViewBys);
        viewerBD.setColViewIds(new ArrayList<String>());
        viewerBD.setTimeDetails(timeDetails);
        viewerBD.prepareReport("ChangeViewBy", container, container.getReportId(), userId, null);
        if (container.getRetObj().getRowCount() == 1) {
            container.setGrandTotalReq(false);
            container.setNetTotalReq(false);
        } else if (container.getRetObj().getRowCount() > 1) {
            container.setGrandTotalReq(false);
            container.setNetTotalReq(true);
        }
        DataSnapshotGenerator ssGen = new DataSnapshotGenerator();

        if (container.getRetObj().getRowCount() > 0) {
            ssGen.addSliceToSnapshot(container, userId, fileName);
        }

        if (doSwap) {
            ArrayList<String> list = new ArrayList<String>();
//            list.add(defaultDimVal);
            collect.updateParameterDefaults(preference.getDimId(), defaultDimValList, false); //changed by srikanth prev:collect.updateParameterDefaults(preference.getDimId(), list, false)
        } else {
            collect.resetParameterDefaults(preference.getDimId());
        }

        //collect.resetParameterDefaults(sliceViewBy);
    }

    public DataSnapshot openDataSnapshot(int snapShotId, String userId) throws ParseException {
        Container container;
        String snapShotFileName;
        Calendar currDate = Calendar.getInstance();
        Date currentDate;
        DataSnapshot snapShot = new DataSnapshot();
        Clob clobObj = null;
        String stringObj = null;

        DataSnapshotDAO snapShotDAO = new DataSnapshotDAO();
        currentDate = snapShotDAO.getCurrentDate();
        currDate.setTime(currentDate);
        snapShot = snapShotDAO.openDataSnapshot(snapShotId);
        String reportId = snapShot.getReportId();
        if (snapShot.isRefreshNeeded(currDate)) {
            container = this.generateContainer(reportId, userId, null, null);
            snapShotFileName = this.downloadAndSaveDataSnapshot(container, userId);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                stringObj = updateSnapShotHtmlViewString(snapShotId, snapShotFileName, "fromHtml");
                snapShot.setHtmlViewString(stringObj);
            } else {
                clobObj = updateSnapShotHtmlView(snapShotId, snapShotFileName, "fromHtml");
                snapShot.setHtmlView(clobObj);
            }
            //snapShot.setHtmlView(clobObj);
        }


        return snapShot;
    }

    private List<ReportSchedulePreferences> getReportSchedulePreferences(List<UserDimensionMap> userMapList) {
        List<ReportSchedulePreferences> reptSchedulePreferences = new ArrayList<ReportSchedulePreferences>();
        for (UserDimensionMap userMap : userMapList) {
            ReportSchedulePreferences schedulePrefer = new ReportSchedulePreferences();
            schedulePrefer.setDimId(userMap.getDimensionId());
            schedulePrefer.setDimValues(userMap.getDimensionValue());
            schedulePrefer.setMailIds(userMap.getMailId());
            reptSchedulePreferences.add(schedulePrefer);
        }
        return reptSchedulePreferences;

    }

    private List<ReportScheduleSlices> getReportScheduledSlices(Container container, String defaultViewById) {
        List<ReportScheduleSlices> reptSlicesList = new ArrayList<ReportScheduleSlices>();
        HashMap paramMap = container.getParametersHashMap();
        ArrayList paramIds = (ArrayList) paramMap.get("Parameters");
        for (int i = 0; i < paramIds.size(); i++) {
            if (!defaultViewById.equals((String) paramIds.get(i))) {
                ReportScheduleSlices slices = new ReportScheduleSlices();
                slices.setRowViewById((String) paramIds.get(i));
                slices.setRowViewByVal("All");
                reptSlicesList.add(slices);
            }

        }
        return reptSlicesList;
    }

    public String getXmlString(DataSnapshot snapShot) {

        Clob htmlTbl = snapShot.getXmlView();
        StringBuilder xmlBuilder = new StringBuilder();
        Reader clobReader = null;
        try {
            clobReader = htmlTbl.getCharacterStream();
            char[] cbuf;
            int toRead = 0;
            do {
                cbuf = new char[5196];
                toRead = clobReader.read(cbuf, 0, 5196);
                if (toRead == -1) {
                    break;
                }
                xmlBuilder.append(cbuf);
            } while (true);
            clobReader.close();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return xmlBuilder.toString();
    }

    private boolean isTimeViewBy(String sliceViewBy) {
        if ("Day".equalsIgnoreCase(sliceViewBy)
                || "Week".equalsIgnoreCase(sliceViewBy)
                || "Month".equalsIgnoreCase(sliceViewBy)
                || "Quarter".equalsIgnoreCase(sliceViewBy)
                || "Year".equalsIgnoreCase(sliceViewBy)) {
            return true;
        } else {
            return false;
        }
    }

    private String getElemIdForQuery(ReportSchedule schedule) {
        String elemId = null;
        elemId = schedule.getViewById();
        if ("TIME".equalsIgnoreCase(elemId)) {
            String temp = schedule.getCheckedViewByIds();
            if (temp != null) {
                String[] tempArr = temp.split(",");
                if (tempArr.length > 0) {
                    elemId = tempArr[0];
                }
            }
        }

        return elemId;
    }

    private ArrayList createTimeDetails(String viewById) {
        ArrayList<String> timeDetails = new ArrayList<String>();
        ProgenParam param = new ProgenParam();
        timeDetails.add("Day");
        timeDetails.add("PRG_STD");
        timeDetails.add(param.getdateforpage());
        timeDetails.add(viewById);
        timeDetails.add("Last Period");
        return timeDetails;
    }

    public int saveHeadline(Container container, String userId, String headlinename, String reportId, int rowCount) throws ParseException {
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
        String headline;

        int headlineId = snapshotDAO.insertHeadline(headlinename, reportId, rowCount, userId);

        DataSnapshotGenerator snapshotGenerator = new DataSnapshotGenerator();
        headline = snapshotGenerator.generateAndStoreHtmlSnapshot(container, userId, "fromHeadline");//plz check this hard code of fromHeadline
        if (headlineId != -1) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                updateSnapShotHtmlViewString(headlineId, headline, "fromHeadline");
            } else {
                updateSnapShotHtmlView(headlineId, headline, "fromHeadline");
            }
        }

        return headlineId;
    }

    public ArrayList<String> downloadDataKPIHtml(String[] reportIds, String userId, String KPIHtml) {
        DataSnapshotGenerator dataSnapshotGenerator = new DataSnapshotGenerator();
        return dataSnapshotGenerator.generateKPIHtmlFile(reportIds, userId, KPIHtml);

    }

    public Container generateContainer(String DashboardId, String userId) throws SQLException {//for kpi Scheduler
        HashMap map = null;
        Container container = null;
        container = new Container();
        pbDashboardCollection collect = new pbDashboardCollection();
        container.setReportCollect(collect);
        container.setReportId(DashboardId);
        container.setTableId(DashboardId);
        HashMap DBKPIHashMap = container.getDBKPIHashMap();
        HashMap DBKPIGraphHashMap = container.getDBKPIGraphHashMap();
        HashMap DBGraphHashMap = container.getDBGraphHashMap();
        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        DBKPIHashMap = (DBKPIHashMap == null) ? new HashMap() : DBKPIHashMap;
        DBKPIGraphHashMap = (DBKPIGraphHashMap == null) ? new HashMap() : DBKPIGraphHashMap;
        DBGraphHashMap = (DBGraphHashMap == null) ? new HashMap() : DBGraphHashMap;
        container.setDBKPIHashMap(DBKPIHashMap);
        container.setDBKPIGraphHashMap(DBKPIGraphHashMap);
        container.setDBGraphHashMap(DBGraphHashMap);
        container.setTimeParameterHashMap(new HashMap());
        container.setReportParameterHashMap(new HashMap());
        container.setReportParameterNames(new ArrayList());
        container.setMoreKpiDetails(new LinkedHashMap());
        container.setKpiQuery(new HashMap());
        container.setSavegraphchanges(new LinkedHashMap());
        String DashbdName = null;
        String Dashbddesc = null;
        PbDb pbdb = new PbDb();

        String dashNameQry = "select REPORT_NAME,REPORT_DESC from PRG_AR_REPORT_MASTER where REPORT_ID=" + DashboardId;
        // //.println("dashnameqry-in ---" + dashNameQry);
        PbReturnObject dashNameObj = pbdb.execSelectSQL(dashNameQry);
        if (dashNameObj.getRowCount() > 0) {
            DashbdName = dashNameObj.getFieldValueString(0, 0);
            Dashbddesc = dashNameObj.getFieldValueString(0, 1);
        }
        container.setDbrdName(DashbdName);
        container.setDbrdDesc(Dashbddesc);
        dashboardViewerBD.setIsFxCharts(false);
        dashboardViewerBD.setHttpSession(null);
        dashboardViewerBD.setDashBoardId(DashboardId);
        dashboardViewerBD.setUserId(userId);
        dashboardViewerBD.setServletRequest(null);
        dashboardViewerBD.setServletResponse(null);
        dashboardViewerBD.setContainer(container);
        return dashboardViewerBD.displayDashboardBD(userId);//new code

    }
//added by Dinanath for generating container for scheduler

    public Container generateKpidsbSchedulerContainer(String DashboardId, String userId) throws SQLException {//for kpi Scheduler
        HashMap map = null;
        Container container = null;
        container = new Container();
        pbDashboardCollection collect = new pbDashboardCollection();
        container.setReportCollect(collect);
        container.setReportId(DashboardId);
        container.setTableId(DashboardId);
        HashMap DBKPIHashMap = container.getDBKPIHashMap();
        HashMap DBKPIGraphHashMap = container.getDBKPIGraphHashMap();
        HashMap DBGraphHashMap = container.getDBGraphHashMap();
        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        DBKPIHashMap = (DBKPIHashMap == null) ? new HashMap() : DBKPIHashMap;
        DBKPIGraphHashMap = (DBKPIGraphHashMap == null) ? new HashMap() : DBKPIGraphHashMap;
        DBGraphHashMap = (DBGraphHashMap == null) ? new HashMap() : DBGraphHashMap;
        container.setDBKPIHashMap(DBKPIHashMap);
        container.setDBKPIGraphHashMap(DBKPIGraphHashMap);
        container.setDBGraphHashMap(DBGraphHashMap);
        container.setTimeParameterHashMap(new HashMap());
        container.setReportParameterHashMap(new HashMap());
        container.setReportParameterNames(new ArrayList());
        container.setMoreKpiDetails(new LinkedHashMap());
        container.setKpiQuery(new HashMap());
        container.setSavegraphchanges(new LinkedHashMap());
        //added by Dinanath for setting viewBy
        ArrayList rowviewnames = new ArrayList<String>();
        String GetDefaultViewBy = dashboardViewerBD.GetDefaultViewBy(DashboardId);
        if (GetDefaultViewBy != null && !GetDefaultViewBy.equalsIgnoreCase("") && !GetDefaultViewBy.equalsIgnoreCase("[]")) {
            String[] split = GetDefaultViewBy.replaceAll("\\[", "").replaceAll("\\]", "").trim().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].trim();
            }
            rowviewnames = new ArrayList<>(Arrays.asList(split));              //added by mohit
            container.setViewBy(rowviewnames);
        } else {
            container.setViewBy(rowviewnames);
        }
        String DashbdName = null;
        String Dashbddesc = null;
        PbDb pbdb = new PbDb();

        String dashNameQry = "select REPORT_NAME,REPORT_DESC from PRG_AR_REPORT_MASTER where REPORT_ID=" + DashboardId;
        // //.println("dashnameqry-in ---" + dashNameQry);
        PbReturnObject dashNameObj = pbdb.execSelectSQL(dashNameQry);
        if (dashNameObj.getRowCount() > 0) {
            DashbdName = dashNameObj.getFieldValueString(0, 0);
            Dashbddesc = dashNameObj.getFieldValueString(0, 1);
        }
        container.setDbrdName(DashbdName);
        container.setDbrdDesc(Dashbddesc);
        dashboardViewerBD.setIsFxCharts(false);
        dashboardViewerBD.setHttpSession(null);
        dashboardViewerBD.setDashBoardId(DashboardId);
        dashboardViewerBD.setUserId(userId);
        dashboardViewerBD.setServletRequest(null);
        dashboardViewerBD.setServletResponse(null);
        dashboardViewerBD.setContainer(container);
        return dashboardViewerBD.displayDashboardBDforScheduler(userId);//new code

    }

    private String downloadAndSaveDataSnapshotPdf(Container container) //for scheudling report QTD MTD YTD WTD
    {
        DataSnapshotGenerator SnapshotGenerator = new DataSnapshotGenerator();
        String snapshotFileName = SnapshotGenerator.generateReportInfoPdf(container);
        return snapshotFileName;
    }

    private String downloadAndSaveDataSnapshotExcel(Container container) {
        DataSnapshotGenerator SnapshotGenerator = new DataSnapshotGenerator();
        String snapshotFileName = SnapshotGenerator.generateReportInfoExcel(container);
        return snapshotFileName;
    }

    public Container generateContainer(String reportId, String userId, ArrayList<String> rowViewBylist, String schedule, ReportSchedule schdule) {
        PbReportViewerBD reportViewerBd = new PbReportViewerBD();
        PbReportCollection collect = new PbReportCollection();

        collect.reportId = reportId;
        collect.ctxPath = "";
        collect.reportIncomingParameters = new HashMap<String, String>();
        try {
            if (rowViewBylist != null && !rowViewBylist.isEmpty()) {
                collect.overrideRowViewByDimId = rowViewBylist.get(0);
            }

            collect.getParamMetaData(true);

            if (schdule != null && schdule.getParameterXml() != null) {
                ReportParameter repParam = new ReportParameter();
                ColorCodeBuilder builder = new ColorCodeBuilder();
                LinkedHashMap<String, String> paramMap = builder.parseReportParamXML(schdule.getParameterXml());
                repParam.setReportParameters(paramMap);

                Set<ReportParameterValue> valSet = repParam.getReportParameterValues();
                for (ReportParameterValue val : valSet) {
                    String elemId = val.getElementId();
                    String elemName = collect.getParameterDispName(elemId);
//                     String paramValue = val.getParameterValues();
                    List<String> paramValueLst = val.getParameterValues();
//                     paramValueLst.add(paramValue);
                    collect.setParameters(elemId, elemName, paramValueLst);
                }
            }

            //UPDATE::: This section of code needs to be uncommented when deployed for production systems.
            // Now, it is temporarily commented because of unavailability of latest data in the database.
            //logic also needs to be upgraded depending on the requirement.


            if (schedule != null) {
                String viewById = schdule.getViewById();
                if (isTimeViewBy(viewById)) {
                    ArrayList timeDetails = createTimeDetails(viewById);
                    collect.timeDetailsArray = timeDetails;
                } else {
                    ArrayList timeDetails = createTimeDetails(schedule, schdule);
                    collect.timeDetailsArray = timeDetails;
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        Container container = reportViewerBd.prepareReport(collect, reportId, userId);
        checkTopBottom(container);
        return container;
    }

    private ArrayList createTimeDetails(String schedule, ReportSchedule schdule) {
        ArrayList timeDetails = new ArrayList();
        Date now = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
        String date = fmt.format(now);
        TimeHelper helper = new TimeHelper();
        helper.elementId = getElemIdForQuery(schdule);

        String frequency = schdule.getFrequency();
        String particularDay = schdule.getParticularDay();
        String dataSelection = schdule.getDataSelection();
        // List<String> dataSelection1 = schdule.getDataSelectionTypes();

        if ("1".equals(frequency) || "Daily".equalsIgnoreCase(frequency)) { //Daily

            if ("Current Day".equalsIgnoreCase(schedule)) {
                date = helper.getCurrentDay();
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                timeDetails.add("Day");
                timeDetails.add("Last Period");
            }
            if ("Last Day".equalsIgnoreCase(schedule)) {

                date = helper.getLastDay();
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                timeDetails.add("Day");
                timeDetails.add("Last Period");
            }
            if ("WTD".equalsIgnoreCase(schedule)) {
//                 String startDate = helper.getCurrentWeekStartDate();
//                 String endDate = helper.getCurrentDay();
                date = helper.getCurrentWeekEndDate();
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                //timeDetails.add(endDate);
                timeDetails.add("Week");
                timeDetails.add("Last Period");
            }
            if ("MTD".equalsIgnoreCase(schedule)) {
                date = helper.getCurrentMonthEndDate();
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                //timeDetails.add(endDate);
                timeDetails.add("Month");
                timeDetails.add("Last Period");
            }
            if ("QTD".equalsIgnoreCase(schedule)) {
//                String startDate = helper.getCurrentQuarterStartDate();
//                 String endDate = helper.getCurrentDay();
                date = helper.getCurrentQuarterEndDate();
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                // timeDetails.add(endDate);
                timeDetails.add("Qtr");
                timeDetails.add("Last Period");
            } else if ("YTD".equalsIgnoreCase(schedule)) {
//                 String startDate = helper.getCurrentYearStartDate();
//                 String endDate = helper.getCurrentDay();
                date = helper.getCurrentYearEndDate();
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                //timeDetails.add(endDate);
                timeDetails.add("Year");
                timeDetails.add("Last Period");
            }
        } else if ("2".equals(frequency) || "Monthly".equalsIgnoreCase(frequency)) { //Monthly
            if ("L".equalsIgnoreCase(particularDay) || "B".equalsIgnoreCase(particularDay)) {    //EOM or BOM
                if ("L".equalsIgnoreCase(particularDay)) {    //EOM
                    date = helper.getCurrentDay();
                } else {   //BOM
                    date = helper.getLastDay();
                }
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(date);
                timeDetails.add("Month");
                timeDetails.add("Last Period");
            } else {   // Any Specific Date
                if ("last".equalsIgnoreCase(dataSelection)) {    //last month data
                    date = helper.getLastMonthEndDate();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_STD");
                    timeDetails.add(date);
                    timeDetails.add("Month");
                    timeDetails.add("Last Period");
                } else if ("last".equalsIgnoreCase(dataSelection)) {    //current month data
                    date = helper.getCurrentDay();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_STD");
                    timeDetails.add(date);
                    timeDetails.add("Month");
                    timeDetails.add("Last Period");
                } else if ("both".equalsIgnoreCase(dataSelection)) {    //last month data + current month data
                    String startDate = helper.getLastMonthStartDate();
                    String endDate = helper.getCurrentDay();
                    timeDetails.add("Day");
                    timeDetails.add("PRG_DATE_RANGE");
                    timeDetails.add(startDate);
                    timeDetails.add(endDate);
                    timeDetails.add(null);
                    timeDetails.add(null);
                }
            }
        } else if ("3".equals(frequency) || "Weekly".equalsIgnoreCase(frequency)) { //Weekly
            String startDate = "";
            String endDate = "";
            if ("B".equalsIgnoreCase(particularDay)) {
                startDate = helper.getLastWeekStartDate();
                endDate = helper.getLastWeekEndDate();
            } else if ("L".equalsIgnoreCase(particularDay)) {
                startDate = helper.getCurrentWeekStartDate();
                endDate = helper.getCurrentWeekEndDate();
            }
            timeDetails.add("Day");
            timeDetails.add("PRG_DATE_RANGE");
            timeDetails.add(startDate);
            timeDetails.add(endDate);
            timeDetails.add(null);
            timeDetails.add(null);
        }

        return timeDetails;

    }
}
