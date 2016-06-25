/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.segmentation;

import com.progen.reportdesigner.db.ReportTemplateResBundleSqlServer;
import com.progen.reportdesigner.db.ReportTemplateResourceBundle;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class DimensionSegmentBuilder extends PbDb {

    public static Logger logger = Logger.getLogger(DimensionSegmentBuilder.class);
    private String measId;
    private String dimId;
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ReportTemplateResBundleSqlServer();
            } else {
                resourceBundle = new ReportTemplateResourceBundle();
            }
        }

        return resourceBundle;
    }

    public Segment buildDimensionSegment(String reportId) throws JDOMException {


        Document DimensionSegmentDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
//        DimensionSegment dimsegment=null;
        SegmentBucket segmentBucket = null;
        Segment segment = null;
        String segmentType = "";
        String Limit = "";
        String segName = "";

        DimensionSegmentHelper helper = new DimensionSegmentHelper();
        SegmentParameters segParams;

        String bucketName = "";
        double lowerLimit;
        double upperLimit = 0;
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = null;
        ArrayList<String> bucketNameList = new ArrayList<String>();
        ArrayList<Double> lowerLimitList = new ArrayList<Double>();
        ArrayList<Double> upperLimitList = new ArrayList<Double>();

        String FolderId = "";
        String segxml = "";
        String getSegmentValuesQuery = getResourceBundle().getString("getSegmentValues");

        StringBuilder outerBuffer = new StringBuilder("");

        try {
            obj = new Object[1];
            obj[0] = reportId;
            finalQuery = buildQuery(getSegmentValuesQuery, obj);
            retObj = execSelectSQL(finalQuery);


            if (retObj != null && retObj.getRowCount() != 0) {

                colNames = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

                        segxml = retObj.getFieldValueClobString(i, "SEGMENT_DEFINITION");

                    } else {

                        segxml = retObj.getFieldValueOracleClobString(i, "SEGMENT_DEFINITION");
                    }
                    //measId=retObj.getFieldValueString(i,"MEASURE_ID");
                    dimId = retObj.getFieldValueString(i, "DIMENSION_ID");
                    segmentType = retObj.getFieldValueString(i, "SEGMENT_TYPE");
                    segName = retObj.getFieldValueString(i, "SEGMENT_NAME");
                    outerBuffer.append(segxml);
                }
                //seg = new MeasureBasedSegment(measId);


                String segmentValueXML = outerBuffer.toString();
                try {
                    DimensionSegmentDocument = builder.build(new ByteArrayInputStream(segmentValueXML.getBytes()));
                    root = DimensionSegmentDocument.getRootElement();

                    if (segmentType.equalsIgnoreCase("MeasureBased")) {
                        List segmentMeasureElementId = root.getChildren("MeasureElementId");
                        Element MeasureElementId = (Element) segmentMeasureElementId.get(0);
                        measId = MeasureElementId.getText();
                        segment = helper.createSegment(SegmentType.MEASURE_BASED, "DUMMY", dimId, measId, segName);


                        segParams = new SegmentParameters(SegmentType.MEASURE_BASED);

                        List segmentbucketlist = root.getChildren("SegmentBucket");
                        for (int i = 0; i < segmentbucketlist.size(); i++) {
                            Element segmentMeasures = (Element) segmentbucketlist.get(i);
                            List bucket = segmentMeasures.getChildren("BucketName");
                            List lower = segmentMeasures.getChildren("LowerLimit");
                            List high = segmentMeasures.getChildren("UpperLimit");
                            Element bucketname = (Element) bucket.get(0);
                            Element lowerlimit = (Element) lower.get(0);
                            Element upperlimit = (Element) high.get(0);
                            bucketName = bucketname.getText();
                            lowerLimit = Double.parseDouble(lowerlimit.getText());
                            upperLimit = Double.parseDouble(upperlimit.getText());

                            bucketNameList.add(bucketName);
                            lowerLimitList.add(lowerLimit);
                            upperLimitList.add(upperLimit);
                            segmentBucket = segParams.createBucketDefinition(bucketName, lowerLimit, upperLimit);
                            segment.addSegmentBucket(segmentBucket);
                        }

                    } else {
                        segment = helper.createSegment(SegmentType.VALUE_BASED, "DUMMY", dimId, segName);
                        segParams = new SegmentParameters(SegmentType.VALUE_BASED);
                        // segment = new ValueBasedSegment(dimId);
                        List segmentbucketlist = root.getChildren("SegmentBucket");
                        for (int i = 0; i < segmentbucketlist.size(); i++) {
                            Element segmentMeasures = (Element) segmentbucketlist.get(i);
                            List bucket = segmentMeasures.getChildren("BucketName");
                            List lower = segmentMeasures.getChildren("Limit");
                            Element bucketname = (Element) bucket.get(0);
                            Element limit = (Element) lower.get(0);
                            bucketName = bucketname.getText();
                            Limit = limit.getText();
                            String[] limitValues = Limit.split(";");
                            ArrayList<String> limitValuesList = new ArrayList<String>();
                            limitValuesList.addAll(Arrays.asList(limitValues));

                            segmentBucket = segParams.createBucketDefinition(bucketName, limitValuesList);
                            segment.addSegmentBucket(segmentBucket);

                        }


                    }
                } catch (JDOMException ex) {
                    logger.error("Exception:", ex);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }

            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return segment;

    }
}
