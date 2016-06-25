/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.segmentation;

import java.io.Serializable;

/**
 *
 * @author progen
 */
public class MeasureBasedSegmentBucket implements SegmentBucket, Serializable {

    // public SegmentBucket addSegmentBucket(String bucketName, double lowerLimit, double upperLimit){
    String bucketName;
    double lowerLimit;
    double upperLimit;

    public MeasureBasedSegmentBucket(String bucketName, double lowerLimit, double upperLimit) {

        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.bucketName = bucketName;

    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SegmentBucket && o != null) {
            if (((SegmentBucket) o).getBucketName().equals(this.bucketName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.bucketName != null ? this.bucketName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.bucketName;
    }

    public String toXml() {
        StringBuilder strb = new StringBuilder();
        strb.append("<BucketName>").append(this.bucketName).append("</BucketName><LowerLimit>").append(this.lowerLimit).append("</LowerLimit><UpperLimit>").append(this.upperLimit).append("</UpperLimit>");

        return strb.toString();
    }
}
