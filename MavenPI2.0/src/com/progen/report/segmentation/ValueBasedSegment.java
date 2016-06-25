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
public class ValueBasedSegment extends Segment implements Serializable {

    private LinkedHashSet<ValueBasedSegmentBucket> segmentBucketLst;

    public ValueBasedSegment(String segmentName, String dimension, String segParmVal) {
        super(segmentName, dimension, segParmVal);
        this.segmentBucketLst = new LinkedHashSet<ValueBasedSegmentBucket>();
    }

    public ArrayList<String> getLimits() {
        ArrayList<String> LimitList = new ArrayList<String>();
        for (ValueBasedSegmentBucket segementlist : this.segmentBucketLst) {
            ArrayList<String> ListValues = new ArrayList<String>();
            ListValues.addAll(((ValueBasedSegmentBucket) segementlist).getLimit());
            if (ListValues.size() > 1) {
                String str = "";
                for (int i = 0; i < ListValues.size(); i++) {
                    str += ListValues.get(i);
                    if (i < ListValues.size() - 1) {
                        str += ";";
                    }

                }
                LimitList.add(str);
            } else {
                LimitList.addAll(segementlist.getLimit());
            }
        }

        return LimitList;


    }

    public ArrayList<String> getBucketName() {

        ArrayList bucketNameList = new ArrayList();
        for (ValueBasedSegmentBucket segementlist : this.segmentBucketLst) {
            bucketNameList.add(segementlist.getBucketName());
        }
        return bucketNameList;
    }

    public int getBucketSize() {
        int bucketSize = this.segmentBucketLst.size();
        return bucketSize;
    }

    @Override
    public String getDataForJSON() {
        int bucketSize = this.getBucketSize();

        ArrayList<String> LimitList = new ArrayList<String>();
        ArrayList<String> bucketNameList = new ArrayList<String>();

        StringBuilder bucketname = new StringBuilder();
        StringBuilder limit = new StringBuilder();
        StringBuilder dataForJson = new StringBuilder();

        bucketNameList = this.getBucketName();
        LimitList = this.getLimits();

        for (int i = 0; i < bucketSize; i++) {

            bucketname.append("," + "\"").append(bucketNameList.get(i)).append("\"");
            limit.append("," + "\"").append(LimitList.get(i)).append("\"");

        }
        bucketname.replace(0, 1, "");
        limit.replace(0, 1, "");

        dataForJson.append(",").append("\"SegmentName\":[" + bucketname.toString() + "]").append(",").append("\"LowerLimit\":[" + limit.toString() + "]").append("}");



        return dataForJson.toString();

    }

    public String toXml() {
        {

            StringBuilder strb = new StringBuilder();
            strb.append("<DimensionSegment>");
            for (ValueBasedSegmentBucket bucket : this.segmentBucketLst) {

                strb.append("<SegmentBucket>").append(bucket.toXml()).append("</SegmentBucket>");

            }
            strb.append("</DimensionSegment>");
            return strb.toString();

        }
    }

    public String getBucketName(String dimValue) {
        Iterable<ValueBasedSegmentBucket> bucket = Iterables.filter(segmentBucketLst, this.getSegmentBucketPredicate(dimValue));
        Iterator<ValueBasedSegmentBucket> bucketIter = bucket.iterator();
        if (bucketIter.hasNext()) {
            return bucketIter.next().getBucketName();
        }
        return "Other";
    }

    protected Predicate<ValueBasedSegmentBucket> getSegmentBucketPredicate(final String dimValue) {
        Predicate<ValueBasedSegmentBucket> bucketPredicate = new Predicate<ValueBasedSegmentBucket>() {

            public boolean apply(ValueBasedSegmentBucket input) {
                if ((input.getLimit().contains(dimValue))) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return bucketPredicate;
    }

    @Override
    public void resetBuckets() {
        this.segmentBucketLst = new LinkedHashSet<ValueBasedSegmentBucket>();
    }

    @Override
    public void addSegmentBucket(SegmentBucket bucket) {
        if (bucket instanceof ValueBasedSegmentBucket) {
            this.segmentBucketLst.add((ValueBasedSegmentBucket) bucket);
        }
    }

    @Override
    public String getSegmentType() {
        return "ValueBased";
    }
}
