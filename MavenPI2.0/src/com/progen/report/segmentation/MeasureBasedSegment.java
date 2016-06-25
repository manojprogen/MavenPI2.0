/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.segmentation;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 *
 * @author progen
 */
public class MeasureBasedSegment extends Segment implements Serializable {

    private String measEleId;
    private LinkedHashSet<MeasureBasedSegmentBucket> segmentBucketLst;

    public MeasureBasedSegment(String segmentName, String dimension, String segName) {
        super(segmentName, dimension, segName);
        this.segmentBucketLst = new LinkedHashSet<MeasureBasedSegmentBucket>();
    }

    @Override
    public String getSegmentType() {
        return "MeasureBased";
    }

    public String getMeasureElementId() {
        return this.measEleId;
    }

    public void setMeasureElementId(String measElemtId) {
        this.measEleId = measElemtId;
    }

    public String getBucketName(double measValue) {
        Iterable<MeasureBasedSegmentBucket> bucket = Iterables.filter(segmentBucketLst, this.getSegmentBucketPredicate(measValue));
        Iterator<MeasureBasedSegmentBucket> bucketIter = bucket.iterator();
        if (bucketIter.hasNext()) {
            return bucketIter.next().getBucketName();
        }
        return "Other";
    }

    protected Predicate<MeasureBasedSegmentBucket> getSegmentBucketPredicate(final double measValue) {
        Predicate<MeasureBasedSegmentBucket> bucketPredicate = new Predicate<MeasureBasedSegmentBucket>() {

            public boolean apply(MeasureBasedSegmentBucket input) {
                if ((measValue >= input.getLowerLimit()) && (measValue <= input.getUpperLimit())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return bucketPredicate;
    }

    public ArrayList<Double> getLowerLimits() {
        ArrayList lowerLimitList = new ArrayList();
        for (MeasureBasedSegmentBucket segementlist : segmentBucketLst) {
            lowerLimitList.add(segementlist.getLowerLimit());

        }
        return lowerLimitList;
    }

    public ArrayList<Double> getUpperLimits() {
        ArrayList upperLimitList = new ArrayList();
        for (MeasureBasedSegmentBucket segementlist : segmentBucketLst) {
            upperLimitList.add(segementlist.getUpperLimit());

        }
        return upperLimitList;
    }

    public ArrayList<String> getBucketName() {
        ArrayList bucketNameList = new ArrayList();
        for (MeasureBasedSegmentBucket segementlist : segmentBucketLst) {
            bucketNameList.add(segementlist.getBucketName());

        }
        return bucketNameList;
    }

    public int getBucketSize() {
        int bucketSize = segmentBucketLst.size();
        return bucketSize;
    }

    public String getDataForJSON() {
        int bucketSize = this.getBucketSize();

        ArrayList<String> bucketNameList = new ArrayList<String>();
        ArrayList<Double> lowerLimitList = new ArrayList<Double>();
        ArrayList<Double> upperLimitList = new ArrayList<Double>();

        StringBuilder bucketname = new StringBuilder();
        StringBuilder lowerlimit = new StringBuilder();
        StringBuilder upperlimit = new StringBuilder();
        StringBuilder dataForJson = new StringBuilder();

        bucketNameList = this.getBucketName();
        lowerLimitList = this.getLowerLimits();
        upperLimitList = this.getUpperLimits();

        for (int i = 0; i < bucketSize; i++) {


            bucketname.append("," + "\"").append(bucketNameList.get(i)).append("\"");
            lowerlimit.append(",").append(lowerLimitList.get(i)).append("");
            upperlimit.append(",").append(upperLimitList.get(i)).append("");

        }
        bucketname.replace(0, 1, "");
        lowerlimit.replace(0, 1, "");
        upperlimit.replace(0, 1, "");

        dataForJson.append(",").append("\"SegmentName\":[" + bucketname.toString() + "]").append(",").append("\"LowerLimit\":[" + lowerlimit.toString() + "]").append(",").append("\"UpperLimit\":[" + upperlimit.toString() + "]").append("}");



        return dataForJson.toString();
    }

    public String toXml() {
        {

            StringBuilder strb = new StringBuilder();
            strb.append("<DimensionSegment>");
            strb.append("<MeasureElementId>").append(measEleId).append("</MeasureElementId>");
            for (MeasureBasedSegmentBucket bucket : segmentBucketLst) {

                strb.append("<SegmentBucket>").append(bucket.toXml()).append("</SegmentBucket>");

            }
            strb.append("</DimensionSegment>");
            return strb.toString();

        }
    }

    @Override
    public void resetBuckets() {
        this.segmentBucketLst = new LinkedHashSet<MeasureBasedSegmentBucket>();
    }

    @Override
    public void addSegmentBucket(SegmentBucket bucket) {
        if (bucket instanceof MeasureBasedSegmentBucket) {
            this.segmentBucketLst.add((MeasureBasedSegmentBucket) bucket);
        }
    }
}
