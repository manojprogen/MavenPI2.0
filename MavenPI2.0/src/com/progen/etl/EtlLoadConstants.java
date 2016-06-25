/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

/**
 *
 * @author Administrator
 */
public class EtlLoadConstants {

    public static final int BATCH_SIZE = 1000;
    public static final int EXTRACT_BATCH_SIZE = 12000;
    public static final String EXCEL_SOURCE = "Excel";
    public static final String ACCESS_SOURCE = "Access";
    public static final String VIEW_BY_CHANNEL = "Channel";
    public static final String VIEW_BY_SUB_CHANNEL = "SubChannel";
    public static final String VIEW_BY_AGENT = "Agent";
    public static final String EXCEL_CANNOT_OPEN_WORKBOOK = "Could not Open Workbook";
    public static final String EXCEL_CANNOT_VALIDATE_SHEET = "Could not Validate Sheet with Table ";
    public static final String EXCEL_COLUMN_COUNT_VALIDATE_SHEET = "Number of columns in the Sheet is not matching with Table ";
    public static final String EXCEL_COLUMN_INVALID = "Data in the column is not in a single format. Check the excel sheet column for ";
    public static final String EXCEL_COLUMN_DATA_TYPE_INVALID = "Data in the column is not matching with that of the table. Check the column ";
    public static final String EXCEL_SHEET_VALID = "Excel Sheet Valid";
    public static final String INSERT_FAILED = "Upload of data to table failed. Please check the system logs";
    public static final String UPDATE_LOAD_DATE_SUCCESS = "Last load Date updated";
    public static final String DELETE_DATA_SUCCESS = "Deletion Success";
    public static final String DELETE_DATA_FAILED = "Deletion Failed";
    public static final String SHEET_NOT_VALID = "Columns in Sheet not matching Database table";
    public static final String DATE_RANGE_ERROR = "Data exists in the table for the given Date Range";
    public static final String INSERTION_ERROR = "An error occured while inserting data into table";
    public static final String UPLOAD_SUCCESS = "Data uploaded Successfully";
    public static final String ACCESS_TABLE_NOT_FOUND = " Table not found in the mdb file ";
    public static final String ACCESS_CONNECTION_ERROR = "Could not connect to the Access mdb file ";
    public static final String CANNOT_UPLOAD = "CANNOT_UPLOAD";
    public static final String CONFIRM_UPLOAD = "CONFIRM_UPLOAD";
    public static final int MAX_ROW_COUNT = 5000;
    public static final String CONNECTION_ERROR = "Cannot connect to the database, please try again.";
}
