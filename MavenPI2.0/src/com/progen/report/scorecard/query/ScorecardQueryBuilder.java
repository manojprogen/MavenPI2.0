/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.query;

import com.google.common.collect.Iterables;
import com.progen.report.query.*;
import java.util.*;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ScorecardQueryBuilder {

    public Iterable<ScorecardQueryResult> buildScorecardResultset(Iterable<ScorecardQueryDetails> scoreCardDetails, String userId, String folderId, ArrayList timeDetails) {
        ArrayList<ScorecardQueryResult> resultList = new ArrayList<ScorecardQueryResult>();
        boolean priorAvailable = true;
        ArrayList priorTimeDetails = (ArrayList) timeDetails.clone();
        String priorDate = null;

        //get a list of KPI ScorecardQueryDetails
        Iterable<ScorecardQueryDetails> filter = Iterables.filter(scoreCardDetails, ScorecardQueryDetails.getKPIBasedScorecardMembers());
        Iterator<ScorecardQueryDetails> iter = filter.iterator();
        //extract measures
        List<String> kpiMeasIds = new ArrayList<String>();
        while (iter.hasNext()) {
            ScorecardQueryDetails detail = iter.next();
            kpiMeasIds.add(detail.getMeasElementId());
            if (detail.getTargetElementId() != null && !"".equalsIgnoreCase(detail.getTargetElementId())) {
                kpiMeasIds.add(detail.getTargetElementId());
            }
        }
//        targetMeasIds.add("75743");
        //fire KPI query
        if (!kpiMeasIds.isEmpty()) {
            KPIDataSet kpiDataSet = KPIDataSetHelper.buildKPIDataSet(kpiMeasIds, timeDetails, userId, folderId);

            if (!("PRG_DATE_RANGE".equalsIgnoreCase((String) timeDetails.get(1)))) {
                priorDate = getPriorDate(kpiMeasIds.get(0), (String) timeDetails.get(3), (String) timeDetails.get(4), (String) timeDetails.get(2));
                if (priorDate == null) {
                    priorAvailable = false;
                }
                priorTimeDetails.set(2, priorDate);
            }

            PbReturnObject retObj = kpiDataSet.getKPIData();
            int noOfDays = kpiDataSet.getNoOfDays();

            PbReturnObject priorRetObj = null;
            int priorNoOfDays = 1;
            if (priorAvailable) {
                KPIDataSet kpiPriorDataSet = KPIDataSetHelper.buildKPIDataSet(kpiMeasIds, priorTimeDetails, userId, folderId);
                priorRetObj = kpiPriorDataSet.getKPIData();
                priorNoOfDays = kpiPriorDataSet.getNoOfDays();
            }

            //build list of ScorecardQueryResult
            iter = filter.iterator();
            while (iter.hasNext()) {
                ScorecardQueryDetails detail = iter.next();
                ScorecardQueryResult result = new ScorecardQueryResult();
                result.setScoreMemberId(detail.getScoreCardMemberId());
                result.setRetObj(retObj);
                result.setPriorRetObj(priorRetObj);
                result.setNoOfDays(noOfDays);
                result.setPriorNoOfDays(priorNoOfDays);
                resultList.add(result);
            }
        }

        //get a list of Dimension ScorecardQueryDetails
        Set<String> queriedDims = new HashSet<String>();
        filter = Iterables.filter(scoreCardDetails, ScorecardQueryDetails.getDimensionBasedScorecardMembers());
        iter = filter.iterator();

        while (iter.hasNext()) {
            ScorecardQueryDetails detail = iter.next();
            String dimId = detail.getParamDimId();
            if (!queriedDims.contains(dimId)) {
                Set<String> dimValues = new HashSet<String>();
                queriedDims.add(dimId);
                Iterable<ScorecardQueryDetails> dimFilter = Iterables.filter(scoreCardDetails, ScorecardQueryDetails.getDimensionBasedScorecardMembers(dimId));
                Iterator<ScorecardQueryDetails> dimIter = dimFilter.iterator();

                //extract measures and param values
                Set<String> measSet = new HashSet<String>();
                ArrayList<String> measIds = new ArrayList<String>();
                while (dimIter.hasNext()) {
                    ScorecardQueryDetails dimDetail = dimIter.next();
                    measSet.add(dimDetail.getMeasElementId());
                    if (dimDetail.getTargetElementId() != null) {
                        measSet.add(dimDetail.getTargetElementId());
                    }
                    dimValues.add(dimDetail.getParamValue());
                }
                queriedDims.add(dimId);

                Iterator<String> measSetIter = measSet.iterator();
                while (measSetIter.hasNext()) {
                    measIds.add(measSetIter.next());
                }

                if (!("PRG_DATE_RANGE".equalsIgnoreCase((String) timeDetails.get(1)))) {
                    if (priorDate == null) {
                        priorDate = getPriorDate(measIds.get(0), (String) timeDetails.get(3), (String) timeDetails.get(4), (String) timeDetails.get(2));
                        if (priorDate == null) {
                            priorAvailable = false;
                        }
                        priorTimeDetails.set(2, priorDate);
                    }
                }

                StringBuilder valuesSB = new StringBuilder();
                Iterator<String> dimValIter = dimValues.iterator();
                while (dimValIter.hasNext()) {
                    String val = dimValIter.next();
                    valuesSB.append(",").append(val);
                }

                String dimValuesStr = "";
                if (valuesSB.length() > 0) {
                    dimValuesStr = valuesSB.substring(1);
                }

                Map<String, String> paramValues = new HashMap<String, String>();
                paramValues.put(dimId, dimValuesStr);
                ArrayList<String> rowViewBys = new ArrayList<String>();
                rowViewBys.add(dimId);
                ArrayList<String> colViewBys = new ArrayList<String>();

                //fire query
                DataSetHelper helper = new DataSetHelper.DataSetHelperBuilder().measIds(measIds).rowViewBys(rowViewBys).colViewBys(colViewBys).paramValues(paramValues).timeDetails(timeDetails).userId(userId).bizRole(new String[]{folderId}).build();
                DataSet dataSet = helper.getDataSet();

                PbReturnObject dimRetObj = dataSet.getData();
                int noOfDays = dataSet.getNoOfDays();
                PbReturnObject dimPriorRetObj = null;
                int priorNoOfDays = 0;

                if (priorAvailable) {
                    helper = new DataSetHelper.DataSetHelperBuilder().measIds(measIds).rowViewBys(rowViewBys).colViewBys(colViewBys).paramValues(paramValues).timeDetails(priorTimeDetails).userId(userId).bizRole(new String[]{folderId}).build();

                    DataSet priorDataSet = helper.getDataSet();
                    priorDataSet.getNoOfDays();
                    dimPriorRetObj = priorDataSet.getData();
                    priorNoOfDays = priorDataSet.getNoOfDays();
                }

                //build list of ScorecardQueryResult
                dimIter = dimFilter.iterator();
                while (dimIter.hasNext()) {
                    ScorecardQueryDetails dimDetail = dimIter.next();
                    ScorecardQueryResult result = new ScorecardQueryResult();
                    result.setScoreMemberId(dimDetail.getScoreCardMemberId());
                    result.setRetObj(dimRetObj);
                    result.setPriorRetObj(dimPriorRetObj);
                    result.setNoOfDays(noOfDays);
                    result.setPriorNoOfDays(priorNoOfDays);
                    resultList.add(result);
                }
            }
        }

        return resultList;
    }

    private String getPriorDate(String measId, String periodType, String prgCmp, String currentDate) {
        PbTimeRanges timeRanges = new PbTimeRanges();
        timeRanges.setElementID(measId);
        String priorDate = timeRanges.getPriorDate(periodType, prgCmp, currentDate);
        return priorDate;
    }
}
