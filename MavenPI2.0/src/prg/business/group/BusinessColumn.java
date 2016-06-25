/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public class BusinessColumn {

    int bussTableId;
    String bussTableName;
    int elementId;
    int bussColId;
    String bussColName;
    String bussColType;
    String defaultAggregation;
    String actualColFormula;
    String colDispName;
    String colDispDesc;
    String refElements;
    int bussSrcId;
    String displayFormula;

    public static class BusinessColumnBuilder {

        private int bussTableId;
        private String bussTableName;
        private int elementId;
        private int bussColId;
        private String bussColName;
        private String bussColType;
        private String defaultAggregation;
        private String actualColFormula;
        private String colDispName;
        private String colDispDesc;
        private String refElements;
        private int bussSrcId;
        private String displayFormula;
        public static Logger logger = Logger.getLogger(BusinessColumnBuilder.class);

        public BusinessColumn build() {
            return new BusinessColumn(this);
        }

        public BusinessColumnBuilder bussTableId(int val) {
            bussTableId = val;
            return this;
        }

        public BusinessColumnBuilder bussTableName(String val) {
            bussTableName = val;
            return this;
        }

        public BusinessColumnBuilder elementId(int val) {
            elementId = val;
            return this;
        }

        public BusinessColumnBuilder bussColId(int val) {
            bussColId = val;
            return this;
        }

        public BusinessColumnBuilder bussColName(String val) {
            bussColName = val;
            return this;
        }

        public BusinessColumnBuilder bussColType(String val) {
            bussColType = val;
            return this;
        }

        public BusinessColumnBuilder defaultAggregation(String val) {
            defaultAggregation = val;
            return this;
        }

        public BusinessColumnBuilder actualColFormula(String val) {
            actualColFormula = val;
            return this;
        }

        public BusinessColumnBuilder colDispName(String val) {
            colDispName = val;
            return this;
        }

        public BusinessColumnBuilder colDispDesc(String val) {
            colDispDesc = val;
            return this;
        }

        public BusinessColumnBuilder refElements(String val) {
            refElements = val;
            return this;
        }

        public BusinessColumnBuilder displayFormula(String val) {
            displayFormula = val;
            return this;
        }

        public BusinessColumnBuilder bussSrcId(int val) {
            bussSrcId = val;
            return this;
        }
    }

    private BusinessColumn(BusinessColumnBuilder builder) {
        bussColId = builder.bussColId;
        bussTableId = builder.bussTableId;
        bussTableName = builder.bussTableName;
        elementId = builder.elementId;
        bussColName = builder.bussColName;
        bussColType = builder.bussColType;
        defaultAggregation = builder.defaultAggregation;
        actualColFormula = builder.actualColFormula;
        colDispName = builder.colDispName;
        colDispDesc = builder.colDispDesc;
        refElements = builder.refElements;
        bussSrcId = builder.bussSrcId;
        displayFormula = builder.displayFormula;
    }

    public int bussTableId() {
        return bussTableId;
    }

    public String bussTableName() {
        return bussTableName;
    }

    public int elementId() {
        return elementId;
    }

    public int bussColId() {
        return bussColId;
    }

    public String bussColName() {
        return bussColName;
    }

    public String bussColType() {
        return bussColType;
    }

    public String defaultAggregation() {
        return defaultAggregation;
    }

    public String actualColFormula() {
        return actualColFormula;
    }

    public String colDispName() {
        return colDispName;
    }

    public String colDispDesc() {
        return colDispDesc;
    }

    public String refElements() {
        return refElements;
    }

    public int bussSrcId() {
        return bussSrcId;
    }

    public String displayFormula() {
        return displayFormula;
    }
}
