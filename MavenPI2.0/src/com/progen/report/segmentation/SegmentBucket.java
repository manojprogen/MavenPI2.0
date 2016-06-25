/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.segmentation;

/**
 *
 * @author progen
 */
public interface SegmentBucket {

    public void setBucketName(String bucketName);

    public String getBucketName();

    public String toXml();
}
