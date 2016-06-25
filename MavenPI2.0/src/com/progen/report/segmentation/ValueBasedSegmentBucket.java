/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.segmentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progen
 */
public class ValueBasedSegmentBucket implements SegmentBucket, Serializable {

    ArrayList<String> Limit = new ArrayList<String>();
    String bucketName = "";
    //ArrayList<String> limitValue = new ArrayList<String>();

    public ValueBasedSegmentBucket(String bucketName, List<String> Limit) {

        for (String bucketMember : Limit) {
            this.Limit.add(bucketMember);
        }

        this.bucketName = bucketName;

    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
//     public ArrayList<String> getLimit() {
//       String[] splitLimit = Limit.split(";");
//       for(String limit : splitLimit )
//           limitValue.add(limit);
//       return limitValue;
//    }

    public ArrayList<String> getLimit() {
        return Limit;
    }

    public void setLimit(ArrayList Limit) {
        this.Limit = Limit;
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

    public String getlimitArrayToString() {
        ArrayList<String> ListValues = new ArrayList<String>();
        ListValues.addAll(this.getLimit());
        if (ListValues.size() > 1) {
            String str = "";
            for (int i = 0; i < ListValues.size(); i++) {
                str += ListValues.get(i);
                if (i < ListValues.size() - 1) {
                    str += ";";
                }

            }

            return str;

        } else {
            String str = ListValues.get(0);
            return str;
        }
    }

    public String toXml() {
        //String limitStr = null;

        StringBuilder strb = new StringBuilder();
        strb.append("<BucketName>").append(this.bucketName).append("</BucketName><Limit>");

//        for(String limitValue : this.Limit){
//            limitStr.concat(limitValue);
//            limitStr.concat(";");
//        }
//        String limitValue = limitStr.substring(0, limitStr.length()-1);
//

        strb.append(getlimitArrayToString()).append("</Limit>");

        return strb.toString();
    }
}
