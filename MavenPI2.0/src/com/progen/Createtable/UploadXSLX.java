/* ====================================================================
 Document   : UploadXSLX
 Created on : Jan 12, 2015, 9:30:19 PM
 Author     : Mohit jain
 ==================================================================== */
package com.progen.Createtable;

import com.progen.contypes.GetConnectionType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class UploadXSLX {

    public static Logger logger = Logger.getLogger(UploadXSLX.class);

    UploadXSLX() {
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * The type of the data value is indicated by an attribute on the cell. The
     * value is usually in a "v" element within the cell.
     */
    enum xssfDataType {

        BOOL,
        ERROR,
        FORMULA,
        INLINESTR,
        SSTINDEX,
        NUMBER,}
    private OPCPackage xlsxPackage;
    private int minColumns;
    private PrintStream output;
    String uploadTableName;
    String connId;
    String chkid;
    String isSelectedSheet;
    PreparedStatement ps = null;
    PreparedStatement ps1 = null;
    String[] dbcolType = null;
    int totalcolumn = 0;
    int count = 0;
    int j = 0;
    Boolean linechange = false;
    int coveredcol = 0;
    int totalrow = 0;
    StringBuilder sqlQuery = null;
    Connection con1 = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    String[] formats = {
        "dd-MMM-yy", "dd-MMM-y", "dd-MMM-yyyy", "yyyy-MM-dd", "dd-MM-yy", "dd-MM-yy H:mm", "MM/dd/YY"};
    Date varDate = null;
    java.sql.Date sqlDate = null;
    int totalExcelcol = 0;
    boolean firstisnull = false;

    /**
     * Derived from http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
     * <p/>
     * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
     * http://www.ecma-international.org/publications/standards/Ecma-376.htm
     * <p/>
     * A web-friendly version is http://openiso.org/Ecma/376/Part4
     */
    class MyXSSFSheetHandler extends DefaultHandler {

        /**
         * Table with styles
         */
        private StylesTable stylesTable;
        /**
         * Table with unique strings
         */
        private ReadOnlySharedStringsTable sharedStringsTable;
        /**
         * Destination for data
         */
        private final PrintStream output;
        /**
         * Number of columns to read starting with leftmost
         */
        private final int minColumnCount;
        // Set when V start element is seen
        private boolean vIsOpen;
        // Set when cell start element is seen;
        // used when cell close element is seen.
        private xssfDataType nextDataType;
        // Used to format numeric cell values.
        private short formatIndex;
        private String formatString;
        private final DataFormatter formatter;
        private int thisColumn = -1;
        // The last column printed to the output stream
        private int lastColumnNumber = -1;
        // Gathers characters as they are seen.
        private StringBuffer value;

        /**
         * Accepts objects needed while parsing.
         *
         * @param styles Table of styles
         * @param strings Table of shared strings
         * @param cols Minimum number of columns to show
         * @param target Sink for output
         */
        public MyXSSFSheetHandler(
                StylesTable styles,
                ReadOnlySharedStringsTable strings,
                int cols,
                PrintStream target) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.minColumnCount = cols;
            this.output = target;
            this.value = new StringBuffer();
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
        }

        /*
         * (non-Javadoc) @see
         * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        @Override
        public void startElement(String uri, String localName, String name,
                Attributes attributes) throws SAXException {

            if ("inlineStr".equals(name) || "v".equals(name)) {
                vIsOpen = true;
                // Clear contents cache
                value.setLength(0);
            } // c => cell
            else if ("c".equals(name)) {
                // Get the cell reference
                String r = attributes.getValue("r");
                int firstDigit = -1;
                for (int c = 0; c < r.length(); ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                thisColumn = nameToColumn(r.substring(0, firstDigit));
//                
                // Set up defaults.
                this.nextDataType = xssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                String cellType = attributes.getValue("t");
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellType)) {
                    nextDataType = xssfDataType.BOOL;
                } else if ("e".equals(cellType)) {
                    nextDataType = xssfDataType.ERROR;
                } else if ("inlineStr".equals(cellType)) {
                    nextDataType = xssfDataType.INLINESTR;
                } else if ("s".equals(cellType)) {
                    nextDataType = xssfDataType.SSTINDEX;
                } else if ("str".equals(cellType)) {
                    nextDataType = xssfDataType.FORMULA;
                } else if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null) {
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                    }
                }
            }

        }

        /*
         * (non-Javadoc) @see
         * org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
         * java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            String thisStr = null;

            // v => contents of a cell
            if ("v".equals(name)) {
                // Process the value contents as required.
                // Do now, as characters() may be called more than once
                switch (nextDataType) {

                    case BOOL:
                        char first = value.charAt(0);
                        thisStr = first == '0' ? "FALSE" : "TRUE";
                        break;

                    case ERROR:
                        thisStr = "\"ERROR:" + value.toString() + '"';
                        break;

                    case FORMULA:
                        // A formula could result in a string value,
                        // so always add double-quote characters.
                        thisStr = value.toString();
                        break;

                    case INLINESTR:
                        // TODO: have seen an example of this, so it's untested.
                        XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                        thisStr = rtsi.toString();
                        break;

                    case SSTINDEX:
                        String sstIndex = value.toString();
                        try {
                            int idx = Integer.parseInt(sstIndex);
                            XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                            thisStr = rtss.toString();
                        } catch (NumberFormatException ex) {
                            output.println("Failed to parse SST index '" + sstIndex + "': " + ex.toString());
                        }
                        break;

                    case NUMBER:
                        String n = value.toString();
                        if (this.formatString != null) {
                            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                        } else {
                            thisStr = n;
                        }
                        break;

                    default:
                        thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
                        break;
                }

                if (true) {
                    String s = thisStr;
                }
                // Output after we've seen the string contents
                // Emit commas for any fields that were missing on this row
                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }

                int nullcol = 0;
                String data = "";
                for (int i = lastColumnNumber; i < thisColumn; ++i) {
                    if (totalcolumn == coveredcol) {
                        coveredcol = 0;
                        thisColumn++;
                        firstisnull = true;
                        //code if first column is null
                    }
                    linechange = false;
                    nullcol = thisColumn - i;
                    if (count == 0) {
                        totalExcelcol++;
                    }

                    if (count > 0) {
                        if (thisStr.equalsIgnoreCase("NULL")) {
                            thisStr = "";
                        }
                        try {
//                      if(thisStr!=null)
//                      {
                            if (j < totalcolumn) {
                                if (j > 22) {
//                                  
                                }
                                if (dbcolType[j].equalsIgnoreCase("number") || dbcolType[j].equalsIgnoreCase("numeric")) {

                                    if (thisStr.equalsIgnoreCase("")) {
                                        thisStr = "0";
                                    }

                                    if (nullcol != 1) {
                                        ps.setInt(j + 1, 0);
                                    } else {
                                        ps.setInt(j + 1, Integer.parseInt(thisStr));
                                    }
                                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("bigint")) {//added by Dinanath
                                

                                    if (thisStr.equalsIgnoreCase("")) {
                                        thisStr = "0";
                                    }

                                    if (nullcol != 1) {
                                        ps.setBigDecimal(j + 1, BigDecimal.valueOf(0));
                                    } else {
                                        ps.setBigDecimal(j + 1, BigDecimal.valueOf(Double.parseDouble(thisStr)));
                                    }
                                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("DATETIME") || dbcolType[j].equalsIgnoreCase("DATE")) {
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");



                                    if (thisStr.equalsIgnoreCase("")) {
                                        ps.setDate(j + 1, null);
                                    } else {
                                        if (thisStr.contains("\'")) {
//                                thisStr=thisStr.replaceAll("\'", "-");
                                            thisStr = "01-" + thisStr.substring(0, 3) + "-20" + thisStr.substring(4);
//                                 thisStr="01-"+thisStr;
//                                 thisStr=

                                        }
                                        if (nullcol != 1) {
                                            ps.setDate(j + 1, null);
                                        } else {
                                            for (String parse : formats) {
                                                dateFormat = new SimpleDateFormat(parse);
                                                try {
                                                    varDate = dateFormat.parse(thisStr);

//                              
                                                    break;
                                                } catch (Exception e) {
//           logger.error("Exception:",e);
//            check = false;
                                                }
                                            }
//                              varDate=dateFormat.parse(thisStr);
                                            sqlDate = new java.sql.Date(varDate.getTime());
                                            ps.setDate(j + 1, sqlDate);
                                        }
                                    }
                                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("DOUBLE")) {
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                                    if (thisStr.equalsIgnoreCase("")) {
                                        thisStr = "0";
                                    }
                                    if (nullcol != 1) {
                                        ps.setDouble(j + 1, 0);
                                    } else {

//                                        thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                        thisStr = thisStr.replaceAll("/[^0-9.-]/g", "");
                                        ps.setDouble(j + 1, Double.parseDouble(thisStr));
                                    }
                                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("float")) {//added by Dinanath
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                                    if (thisStr.equalsIgnoreCase("")) {
                                        thisStr = "0";
                                    }
                                    if (nullcol != 1) {
                                        ps.setFloat(j + 1, 0);
                                    } else {

//                                        thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                        thisStr = thisStr.replaceAll("/[^0-9.-]/g", "");
                                        ps.setFloat(j + 1, Float.parseFloat(thisStr));
                                    }
                                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("int")) {//added by Dinanath
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                                    if (thisStr.equalsIgnoreCase("")) {
                                        thisStr = "0";
                                    }
                                    if (nullcol != 1) {
                                        ps.setInt(j + 1, 0);
                                    } else {

//                                        thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                        ps.setInt(j + 1, Integer.parseInt(thisStr));
                                    }
                                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("DECIMAL")) {
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                    if (thisStr.equalsIgnoreCase("")) {
                                        thisStr = "0";
                                    }

                                    if (nullcol != 1) {
                                        ps.setDouble(j + 1, 0);
                                    } else {

//                                        thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                        thisStr = thisStr.replaceAll("/[^0-9.-]/g", "");
                                        ps.setDouble(j + 1, Double.parseDouble(thisStr));
                                    }
                                    ++j;
                                    coveredcol++;
                                } else {

                                    if (nullcol != 1) {
                                        ps.setString(j + 1, null);
                                    } else {
                                        ps.setString(j + 1, thisStr);
                                    }
                                    ++j;
                                    coveredcol++;
                                }
//                      }else
//                         {
//                           ps.setString(j+1,null);
//                         }

                            }

                        } catch (Exception e) {
                            logger.error("Exception:", e);
//            check = false;
                        }



                    }




//                    output.print(',');

                }
                if (firstisnull == true) {
                    thisColumn--;
                    firstisnull = false;

                }

                // Might be the empty string.
//                output.print(thisStr);

                if (lastColumnNumber == thisColumn) {
                    if (thisStr.equalsIgnoreCase("NULL")) {
                        thisStr = "";
                    }
                    if (count > 0) {
                        try {

                            if (dbcolType[j].equalsIgnoreCase("number") || dbcolType[j].equalsIgnoreCase("numeric")) {

                                if (thisStr.equalsIgnoreCase("")) {
                                    thisStr = "0";
                                }
                                if (linechange == true) {
                                    ps.setInt(j + 1, Integer.parseInt(thisStr));
                                } else {
                                    ps.setInt(j + 1, 0);
                                }
                                ++j;
                                coveredcol = 1;
                            }else  if (dbcolType[j].equalsIgnoreCase("bigint")) {//added by Dinanath

                                if (thisStr.equalsIgnoreCase("")) {
                                    thisStr = "0";
                                }
                                if (linechange == true) {
                                    ps.setBigDecimal(j + 1, BigDecimal.valueOf(Double.valueOf(thisStr)));
                                } else {
                                    ps.setBigDecimal(j + 1, BigDecimal.valueOf(0));
                                }
                                ++j;
                                coveredcol = 1;
                            } else if (dbcolType[j].equalsIgnoreCase("DATETIME") || dbcolType[j].equalsIgnoreCase("DATE")) {
//                         SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                                if (thisStr.equalsIgnoreCase("")) {
                                    ps.setDate(j + 1, null);
                                } else {


                                    if (linechange == true) {

                                        if (thisStr.contains("\'")) {
//                                thisStr=thisStr.replaceAll("\'", "-");
                                            thisStr = "01-" + thisStr.substring(0, 3) + "-20" + thisStr.substring(4);
//                                 thisStr="01-"+thisStr;
//                                 thisStr=

                                        }

                                        for (String parse : formats) {
                                            dateFormat = new SimpleDateFormat(parse);
                                            try {
                                                varDate = dateFormat.parse(thisStr);
//                              
                                                break;
                                            } catch (Exception e) {
//           logger.error("Exception:",e);
//            check = false;
                                            }
                                        }
//                              varDate=dateFormat.parse(thisStr);
                                        sqlDate = new java.sql.Date(varDate.getTime());

//                       
                                        ps.setDate(j + 1, sqlDate);
                                    } else {
                                        ps.setDate(j + 1, null);
                                    }
                                }
                                ++j;
                                coveredcol = 1;
                            } else if (dbcolType[j].equalsIgnoreCase("DOUBLE")) {
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                if (thisStr.equalsIgnoreCase("")) {
                                    thisStr = "0";
                                }
//                                thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                    thisStr = thisStr.replaceAll("/[^0-9.-]/g", "");

                                if (linechange == true) {
                                    ps.setDouble(j + 1, Double.parseDouble(thisStr));
                                } else {
//                              varDate=dateFormat.parse(thisStr);
//                         sqlDate = new java.sql.Date(varDate.getTime());
                                    ps.setDouble(j + 1, 0);
                                }
                                ++j;
                                coveredcol = 1;
                            } else if (dbcolType[j].equalsIgnoreCase("float")) {//added by Dinanath
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                if (thisStr.equalsIgnoreCase("")) {
                                    thisStr = "0";
                                }
//                                thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                thisStr = thisStr.replaceAll("/[^0-9.-]/g", "");

                                if (linechange == true) {
                                    ps.setFloat(j + 1, Float.parseFloat(thisStr));
                                } else {
//                              varDate=dateFormat.parse(thisStr);
//                         sqlDate = new java.sql.Date(varDate.getTime());
                                    ps.setFloat(j + 1, 0);
                                }
                                ++j;
                                coveredcol = 1;
                            } else if (dbcolType[j].equalsIgnoreCase("int")) {
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                if (thisStr.equalsIgnoreCase("")) {
                                    thisStr = "0";
                                }
//                                thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                thisStr = thisStr.replaceAll("/[^0-9.-]/g", "");

                                if (linechange == true) {
                                    ps.setInt(j + 1, Integer.parseInt(thisStr));
                                } else {
//                              varDate=dateFormat.parse(thisStr);
//                         sqlDate = new java.sql.Date(varDate.getTime());
                                    ps.setInt(j + 1, 0);
                                }
                                ++j;
                                coveredcol = 1;
                            } else if (dbcolType[j].equalsIgnoreCase("DECIMAL")) {
//                           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                if (thisStr.equalsIgnoreCase("")) {
                                    thisStr = "0";
                                }

                                if (nullcol != 1) {
                                    ps.setDouble(j + 1, 0);
                                } else {

//                                    thisStr = thisStr.replaceAll("[^\\w.]+", "");
                                    thisStr = thisStr.replaceAll("/[^0-9.-]/g", "");
                                    ps.setDouble(j + 1, Double.parseDouble(thisStr));
                                }
                                ++j;
                                coveredcol = 1;
                            } else {
                                if (linechange == true) {
                                    ps.setString(j + 1, thisStr);
                                } else {
                                    ps.setString(j + 1, null);
                                }
                                ++j;
                                coveredcol = 1;
                            }
                        } catch (Exception e) {
                            logger.error("Exception:", e);
//            check = false;
                        }
                    }
                    if (count == 0) {
                        totalExcelcol++;
                    }
                }

                if (thisColumn > -1) {
                    lastColumnNumber = thisColumn;
                }

            } else if ("row".equals(name)) {
//               
                // Print out any missing commas if needed
                if (minColumns > 0) {
//                    output.print("minColumns");
                    if (lastColumnNumber == -1) {
                        lastColumnNumber = 0;
                    }
                    for (int i = lastColumnNumber; i < (this.minColumnCount); i++) {
//                        output.print(',');
//                        output.print("mohit");
                    }
                }

                try {

                    totalrow++;
//                 if(totalrow==449)
//                 {
//                     
//                 }
                    j = 0;
                    count++;
//                coveredcol=0;
                    linechange = true;
                    if (count > 1 && coveredcol == totalcolumn) {
                        ps.addBatch();
                        if (totalrow == 5000) {
//                          output.println("totalrow "+totalrow);
                            ps.executeBatch();
//                         ps.close();
                            ps = con1.prepareStatement(sqlQuery.toString());
                            totalrow = 0;

                        }

                    } else if (count > 1) {
                        if (coveredcol > 0 && coveredcol < totalcolumn) {
                            for (int j = coveredcol; j < totalcolumn; j++) {
                                if (dbcolType[j].equalsIgnoreCase("number") || dbcolType[j].equalsIgnoreCase("numeric")) {
                                    ps.setInt(j + 1, 0);
//                           ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("DATETIME") || dbcolType[j].equalsIgnoreCase("DATE")) {
                                    ps.setDate(j + 1, null);
//                    ++j;
                                    coveredcol++;

                                } else if (dbcolType[j].equalsIgnoreCase("DOUBLE")) {
//
                                    ps.setDouble(j + 1, 0);

//                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("DECIMAL")) {
//
                                    ps.setDouble(j + 1, 0);

//                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("float")) {//added by Dinanath
//
                                    ps.setFloat(j + 1, 0);

//                    ++j;
                                    coveredcol++;
                                } else if (dbcolType[j].equalsIgnoreCase("int")) {//added by Dinanath
//
                                    ps.setInt(j + 1, 0);

//                    ++j;
                                    coveredcol++;
                                } else {
                                    ps.setString(j + 1, null);
//                    ++j;
                                    coveredcol++;
                                }
                            }
                            ps.addBatch();
                            if (totalrow == 5000) {
                                ps.executeBatch();
//                         ps.close();
                                ps = con1.prepareStatement(sqlQuery.toString());
//                          output.println("totalrow "+totalrow);
                                totalrow = 0;


                            }

                        }
                    }

                } catch (Exception e) {
                    logger.error("Exception:", e);
//            check = false;
                }
                // We're onto a new row

                lastColumnNumber = -1;
            }
        }

        /**
         * Captures characters only if a suitable element is open. Originally
         * was just "v"; extended for inlineStr also.
         */
        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (vIsOpen) {
                value.append(ch, start, length);
            }
        }

        /**
         * Converts an Excel column name like "C" to a zero-based index.
         *
         * @param name
         * @return Index corresponding to the specified name
         */
        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length(); ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }
    }

    ///////////////////////////////////////
    /**
     * Creates a new XLSX -> CSV converter dateFormat
     *
     * @param pkg The XLSX package to process
     * @param output The PrintStream to output the CSV to
     * @param minColumns The minimum number of columns to output, or -1 for no
     * minimum
     */
    public UploadXSLX(OPCPackage pkg, PrintStream output, int minColumns, String uploadTableName, String connId, String chkid) {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
        this.uploadTableName = uploadTableName;
        this.connId = connId;
        this.chkid = chkid;
        String query = "";
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connId);
//          Connection con =  ProgenConnection.getInstance().getConnection();
        PbReturnObject pbReturnObject = new PbReturnObject();
        PbDb PbDb = new PbDb();
        try {
            GetConnectionType getConnectionType = new GetConnectionType();
            String connectionType = getConnectionType.getConTypeByConnID(connId);
            if (con != null) {
                if (connectionType.equalsIgnoreCase("SqlServer")) {
                    query = "SELECT TOP 1 * FROM " + uploadTableName;
                } else if (connectionType.equalsIgnoreCase("ORACLE")) {
                    query = "select * from  " + uploadTableName + " where ROWNUM = 1 ";
                } else {
                    query = "select * from  " + uploadTableName + " limit 1 ";
                }

                pbReturnObject = PbDb.execSelectSQL(query, con);
            } else {
                if (connectionType.equalsIgnoreCase("SqlServer")) {
                    query = "SELECT TOP 1 * FROM " + uploadTableName;
                } else if (connectionType.equalsIgnoreCase("ORACLE")) {
                    query = "select * from  " + uploadTableName + " where ROWNUM = 1 ";
                } else {
                    query = "select * from  " + uploadTableName + " limit 1 ";
                }
                pbReturnObject = PbDb.execSelectSQL(query);
            }
            sqlQuery = new StringBuilder();
            sqlQuery.append("insert into ").append(uploadTableName).append(" values(");
            StringBuilder innersqlquery = new StringBuilder("");
            for (int count = 0; count < pbReturnObject.getColumnCount(); count++) {
                innersqlquery.append("," + "?");
            }
            sqlQuery.append(innersqlquery.substring(1));
            sqlQuery.append(")");
            con1 = ProgenConnection.getInstance().getConnectionByConId(connId);
            ps = con1.prepareStatement(sqlQuery.toString());
//                ps.setString(1,"mohit");
//                ps.addBatch();
//                ps.execute();
//               ps1=con1.prepareStatement("truncate table "+uploadTableName);
            dbcolType = pbReturnObject.getColumnTypes();
            totalcolumn = pbReturnObject.getColumnCount();


        } catch (Exception e) {
            logger.error("Exception:", e);
//            check = false;
        }
    }

    /**
     * Parses and shows the content of one sheet using the specified styles and
     * shared-strings tables.
     *
     * @param styles
     * @param strings
     * @param sheetInputStream
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            InputStream sheetInputStream)
            throws IOException, ParserConfigurationException, SAXException {
        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        ContentHandler handler = new MyXSSFSheetHandler(styles, strings, this.minColumns, this.output);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void process()
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {

        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 1;
        if ("truncate".equals(chkid)) {
            try {
                PreparedStatement ps1 = con1.prepareStatement("truncate table " + uploadTableName);
                ps1.addBatch();
                ps1.executeBatch();
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
//            this.output.println();
//            this.output.println(sheetName + " [index=" + index + "]:");
            if (index == 1) {
                processSheet(styles, strings, stream);
                try {
                    ps.executeBatch();
                    ps.close();
                    con1.close();
                    stream.close();
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }

                ++index;
            }
        }
    }

    public String GetAllSheets(OPCPackage xlsxPackage)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
//        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
//        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
//        int index = 0;
        InputStream stream = null;
        String totalinfo = "";
        while (iter.hasNext()) {
            stream = iter.next();
            totalinfo = totalinfo + iter.getSheetName() + "::";
//            index++;

        }
        try {
            stream.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return totalinfo;
    }

    public void UploadIntoDatabase(OPCPackage pkg, PrintStream output, int minColumns, String connId, String type, Map<String, String> AllInfo, HttpServletResponse response) throws IOException, SAXException, OpenXML4JException, ParserConfigurationException {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
        this.connId = connId;
        String query = "";

        PrintWriter out = response.getWriter();
        PbReturnObject pbReturnObject = new PbReturnObject();
        PbDb PbDb = new PbDb();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        try {
            while (iter.hasNext()) {
                index++;
                Connection con = ProgenConnection.getInstance().getConnectionByConId(connId);
                logger.info("data loading is continue........: " + index);
                InputStream stream = iter.next();
                String sheetName = iter.getSheetName();

                if (type.equalsIgnoreCase("st")) {
//                    this.uploadTableName = (AllInfo.get("Sheet")).split("::")[0];
//                    this.chkid = (AllInfo.get("Sheet")).split("::")[1];
                    this.uploadTableName = (AllInfo.get(sheetName)).split("::")[0];
                    if (index == 1) {
                        this.chkid = (AllInfo.get(sheetName)).split("::")[1];
                    }
                    this.isSelectedSheet = (AllInfo.get(sheetName)).split("::")[2];
                } else if (type.equalsIgnoreCase("mt")) {
                    this.uploadTableName = (AllInfo.get(sheetName)).split("::")[0];
                    this.chkid = (AllInfo.get(sheetName)).split("::")[1];
                    this.isSelectedSheet = (AllInfo.get(sheetName)).split("::")[2];
                }

                GetConnectionType getConnectionType = new GetConnectionType();
                String connectionType = getConnectionType.getConTypeByConnID(connId);
                if (con != null) {
                    if (connectionType.equalsIgnoreCase("SqlServer")) {
                        query = "SELECT TOP 1 * FROM " + uploadTableName;
                    } else if (connectionType.equalsIgnoreCase("ORACLE")) {
                        query = "select * from  " + uploadTableName + " where ROWNUM = 1 ";
                    } else {
                        query = "select * from  " + uploadTableName + " limit 1 ";
                    }
                    pbReturnObject = PbDb.execSelectSQL(query, con);
                } else {
                    if (connectionType.equalsIgnoreCase("SqlServer")) {
                        query = "SELECT TOP 1 * FROM " + uploadTableName;
                    } else if (connectionType.equalsIgnoreCase("ORACLE")) {
                        query = "select * from  " + uploadTableName + " where ROWNUM = 1 ";
                    } else {
                        query = "select * from  " + uploadTableName + " limit 1 ";
                    }
                    pbReturnObject = PbDb.execSelectSQL(query);
                }
                sqlQuery = new StringBuilder();
                sqlQuery.append("insert into ").append(uploadTableName).append(" values(");
                StringBuilder innersqlquery = new StringBuilder("");
                for (int count = 0; count < pbReturnObject.getColumnCount(); count++) {
                    innersqlquery.append("," + "?");
                }
                sqlQuery.append(innersqlquery.substring(1));
                sqlQuery.append(")");
                con1 = ProgenConnection.getInstance().getConnectionByConId(connId);
                ps = con1.prepareStatement(sqlQuery.toString());
                dbcolType = pbReturnObject.getColumnTypes();
                totalcolumn = pbReturnObject.getColumnCount();
                //modified by Dinanath
                if (type.equalsIgnoreCase("mt")) {
                    if (isSelectedSheet.equalsIgnoreCase("true")) {
                        if ("Yes".equals(chkid)) {
                            if (type.equalsIgnoreCase("st")) {
                                chkid = "No";
                            }
                            try {
                                PreparedStatement ps1 = con1.prepareStatement("truncate table " + uploadTableName);
                                ps1.addBatch();
                                ps1.executeBatch();
                            } catch (Exception e) {
                                logger.error("Exception:", e);
                                out.print(e.getMessage());
                            }
                        }
                        try {
                            count = 0;
                            processSheet(styles, strings, stream);
                            ps.executeBatch();

                            if (con1 != null) {
                                con1.close();
                            }
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                            out.print(e.getMessage());
                        }

                    }//if isSelectedSheet closed
                } else if (type.equalsIgnoreCase("st")) {
                    if (isSelectedSheet.equalsIgnoreCase("true")) {
                        if ("Yes".equals(chkid)) {
                            if (type.equalsIgnoreCase("st")) {
                                chkid = "No";
                            }
                            try {
                                PreparedStatement ps1 = con1.prepareStatement("truncate table " + uploadTableName);
                                ps1.addBatch();
                                ps1.executeBatch();
                            } catch (Exception e) {
                                logger.error("Exception:", e);
                                out.print(e.getMessage());
                            }
                        }
                        try {
                            count = 0;
                            processSheet(styles, strings, stream);
                            ps.executeBatch();

                            if (con1 != null) {
                                try {
                                    con1.close();
                                    con1 = null;
                                } catch (SQLException ex) {
                                    //  logger.error("Exception:",ex);
                                }
                            }
                            if (ps != null) {
                                try {
                                    ps.close();
                                    ps = null;
                                } catch (SQLException ex) {
                                    //  logger.error("Exception:",ex);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                            out.print(e.getMessage());
                        }
                    }
                }
                //calling procedure by Dinanath Parit
                if (uploadTableName.equalsIgnoreCase("ldr_data")) {
                    try {
                        logger.info("ldr_data Procedure is calling.......");
//                    PbDb pbdb = new PbDb();
                        Connection connection = ProgenConnection.getInstance().getConnectionByConId(connId);
                        //con = pbdb.getConnection();
                        logger.info("Connection is established for ldr_data");
                        CallableStatement proc = null;
                        //proc = con.prepareCall("{ call LDR_Proc('2014-01-01','2014-12-31') }");
                        proc = connection.prepareCall("{ call bbraun.LDR_Proc() }");
                        proc.execute();
                        logger.info("Procedure executed Sucessfully");
                        //response.getWriter().println("Procedure has called successfully");
                        connection.close();
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    }
                }//if
                if (uploadTableName.equalsIgnoreCase("ebit_data")) {
                    try {
                        logger.info("ebit_data Procedure is calling.......");
//                    PbDb pbdb = new PbDb();
                        Connection connection2 = ProgenConnection.getInstance().getConnectionByConId(connId);
                        //con = pbdb.getConnection();
                        logger.info("Connection is established for ebit_data");
                        CallableStatement proc = null;
                        //proc = con.prepareCall("{ call LDR_Proc('2014-01-01','2014-12-31') }");
                        proc = connection2.prepareCall("{ call bbraun.EBIT_Proc() }");

                        proc.execute();
                        logger.info("bbraun.EBIT_Proc() Procedure executed Sucessfully");
                        //response.getWriter().println("Procedure has called successfully");
                        connection2.close();
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    }
                }//if

            }//while endded

        } catch (Exception ex) {
            logger.error("Exception:", ex);
            out.print(ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                } catch (SQLException ex) {
                    //  logger.error("Exception:",ex);
                }
            }
            if (con1 != null) {
                try {
                    con1.close();
                    con1 = null;
                } catch (SQLException ex) {
                    //  logger.error("Exception:",ex);
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
//        if (args.length < 1) {
//            System.err.println("Use:");
//            System.err.println("  XLSX2CSV <xlsx file> [min columns]");
//            return;
//        }
//
//        File xlsxFile = new File(args[0]);
//        if (!xlsxFile.exists()) {
//            System.err.println("Not found or not a file: " + xlsxFile.getPath());
//            return;
//        }
//
//        int minColumns = -1;
//        if (args.length >= 2)
//            minColumns = Integer.parseInt(args[1]);
//
//        // The package open is instantaneous, as it should be.
//        OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
//		XLSX2CSV xlsx2csv = new XLSX2CSV(p, System.out, minColumns,);
//		xlsx2csv.process();
    }
}
