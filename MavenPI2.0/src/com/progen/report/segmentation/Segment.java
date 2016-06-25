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
public abstract class Segment implements Serializable {

    private String segmentName;
    private String dimension;
    private String segName;

    public Segment(String segmentName, String dimension, String segName) {
        this.segmentName = segmentName;
        this.dimension = dimension;
        this.segName = segName;
    }

    public Segment(String segmentName, String dimension) {
        this.segmentName = segmentName;
        this.dimension = dimension;
    }

    public abstract String toXml();

    public abstract void resetBuckets();

    public abstract void addSegmentBucket(SegmentBucket bucket);

    public abstract String getSegmentType();

    public String getDimension() {
        return this.dimension;
    }

    public String getSegName() {
        return this.segName;
    }

    public abstract String getDataForJSON();
}
