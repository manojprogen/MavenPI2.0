/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.segmentation;

import com.progen.report.data.DataFacade;
import java.util.ArrayList;

/**
 *
 * @author arun
 */
public class DimensionSegmentHelper {

    public Segment createSegment(SegmentType segmentType, String segmentName, String dimension, String segParmVal) {
        Segment segment;
        segment = new ValueBasedSegment(segmentName, dimension, segParmVal);
        return segment;
    }

    public Segment createSegment(SegmentType segmentType, String segmentName, String dimension, String measure, String segName) {
        Segment segment = null;
        if (segmentType == SegmentType.MEASURE_BASED) {
            segment = new MeasureBasedSegment(segmentName, dimension, segName);
            ((MeasureBasedSegment) segment).setMeasureElementId(measure);
        }

        return segment;
    }

    public ArrayList<String> makeBucketNames(DataFacade facade, Segment segment) {
        String qryCol;
        if (segment instanceof MeasureBasedSegment) {
            qryCol = ((MeasureBasedSegment) segment).getMeasureElementId();
        } else {
            qryCol = segment.getDimension();
        }

        ArrayList<String> segmentValues = new ArrayList<String>();

        if (segment instanceof MeasureBasedSegment) {
            for (int i = 0; i < facade.getRowCount(); i++) {
                double dataValue = facade.getMeasureData(i, qryCol).doubleValue();
                segmentValues.add(((MeasureBasedSegment) segment).getBucketName(dataValue));
            }
        } else if (segment instanceof ValueBasedSegment) {
            for (int i = 0; i < facade.getRowCount(); i++) {
                String dataValue = facade.getDimensionData(i, qryCol);
                segmentValues.add(((ValueBasedSegment) segment).getBucketName(dataValue));
            }
        }
        return segmentValues;
    }
}
