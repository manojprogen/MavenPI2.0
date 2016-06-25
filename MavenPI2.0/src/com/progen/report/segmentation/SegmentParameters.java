/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.segmentation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progen
 */
public class SegmentParameters {

    double measValue;
    String dimValue;
    String bucketName;
    double lowerLmt;
    double upperLmt;
    SegmentType segmentType;
    ArrayList<String> bucketValues;

    public SegmentParameters(SegmentType segmentType) {
        this.segmentType = segmentType;
    }

    public void setMeasValue(double measValue) {
        this.measValue = measValue;

    }

    public void setdimValue(String dimValue) {
        this.dimValue = dimValue;

    }

    public double getMeasValue() {
        return measValue;

    }

    public String getDimValue() {
        return dimValue;
    }

    public SegmentBucket createBucketDefinition(String bucketName, double lowerLmt, double upperLmt) {
        if (this.segmentType == SegmentType.MEASURE_BASED) {
            SegmentBucket bucket = new MeasureBasedSegmentBucket(bucketName, lowerLmt, upperLmt);
            return bucket;
        }
        return new NullSegmentBucket();
    }

    public SegmentBucket createBucketDefinition(String bucketName, List<String> bucketValues) {
        if (this.segmentType == SegmentType.VALUE_BASED) {
            SegmentBucket bucket = new ValueBasedSegmentBucket(bucketName, bucketValues);
            return bucket;
        }
        return new NullSegmentBucket();
    }
}
