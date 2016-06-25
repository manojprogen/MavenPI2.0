/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSearchRow;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class TableSearchDisplay extends TableDisplay {

    public TableSearchDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateHTML() {
        StringBuilder search = new StringBuilder();
        int colCount = tableBldr.getColumnCount();


        TableSearchRow row = tableBldr.getSearchData();
        search.append("<Tr style=\"display:none;\">");
        if (tableBldr.isDrillAcrossSupported()) {
            search.append("<td width=\"5\"/>");
        }

//        commented by manik to hide + sign adhoc drill
//        if(!tableBldr.getAdhocDrillType().equalsIgnoreCase("none"))
//        search.append("<td width=\"5\"/></td>");

        if (tableBldr.isSerialNumDisplay()) {
            search.append("<td width=\"5\"/></td>");
        }
        String srchVal;
        String columnId;
        String reportId;
        String ctxPath;
        ArrayList<String> dataTypes;

        reportId = row.getReportId();
        ctxPath = row.getPath();
        dataTypes = row.getDataType();
        for (int column = tableBldr.getFromColumn(); column < colCount; column++) {
            srchVal = row.getRowData(column);
            columnId = row.getColumnId(column);
            if (dataTypes.get(column).equalsIgnoreCase("N")) {
                search.append("<Td ID=\"").append(row.getID(column)).append("\" style=\"display:").append(row.getDisplayStyle(column)).append("\" ><Input type='text' name='search").append(column).append("' class=\"inputbox\" style=\"width:100%\" value=\"").append(srchVal).append("\" readonly onClick=\"parent.doOpenMenu(document.getElementById('SRCHMENU_").append(column).append("'))\">");
                search.append("<Div style=\"position:absolute;display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto; z-index: 1;\" id=\"SRCHMENU_").append(column).append("\" >");
                search.append("<Table border=\"1\" class=\"tableBody\" CELLPADDING=\"0\" CELLSPACING=\"0\" STYLE=\"padding:0\">");
                search.append("<Tr>");
                search.append("<Td> > </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt;\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'>','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"</Td>");
                search.append(" </Tr>");
                search.append("<Tr>");
                search.append("<Td> < </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'<','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> = </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'=','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> >= </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'>=','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> <= </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'<=','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> <> </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText1\" id=\"1srch").append(column).append("\" size=\"5\" onkeyup=\"parent.doSrchOperation(this,'BT','").append(columnId).append("',event,'").append(reportId).append("',document.getElementById('2srch").append(column).append("'),'").append(ctxPath).append("') \">");
                search.append(" -");
                search.append("<input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText2\" id=\"2srch").append(column).append("\" size=\"5\" onkeyup=\"parent.doSrchOperation(this,'BT','").append(columnId).append("',event,'").append(reportId).append("',document.getElementById('1srch").append(column).append("'),'").append(ctxPath).append("') \">");
                search.append(" </Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> TOP </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'TOP','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> BTM </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'BTM','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td colspan=\"2\"><a style=\"font-size:8pt\" href=\"javascript:parent.doNewSrchOperation('!=','").append(columnId).append("','").append(reportId).append("','").append(ctxPath).append("')\">Exclude Zero</a></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td colspan=\"2\"><a style=\"font-size:8pt\" href=\"javascript:parent.doNewSrchOperation('clear','").append(columnId).append("','").append(reportId).append("','").append(ctxPath).append("')\">Clear Filter</a></Td>");
                search.append("</Tr>");
                search.append(" </Table>");
                search.append("</Div>");
                search.append("</Td>");
            } else if (dataTypes.get(column).equalsIgnoreCase("D")) {
                search.append("<Td ID=\"").append(row.getID(column)).append("\" style=\"display:").append(row.getDisplayStyle(column)).append("\" ><Input type='text' name='search").append(column).append("' class=\"inputbox\" style=\"width:100%\" value=\"").append(srchVal).append("\" readonly onClick=\"parent.doOpenMenu(document.getElementById('SRCHMENU_").append(column).append("'))\">");
                search.append("<Div style=\"position:absolute;display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto; z-index: 1;\" id=\"SRCHMENU_").append(column).append("\" >");
                search.append("<Table border=\"1\" class=\"tableBody\" CELLPADDING=\"0\" CELLSPACING=\"0\" STYLE=\"padding:0\">");
                search.append("<Tr>");
                search.append("<Td> > </Td>");
                search.append("<Td><input id=\"datePickerForReport\" type=\"text\" style=\"font-size:8pt;\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'>','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"</Td>");
                search.append(" </Tr>");
                search.append("<Tr>");
                search.append("<Td> < </Td>");
                search.append("<Td><input id=\"datePickerForReport1\" type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'<','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> = </Td>");
                search.append("<Td><input id=\"datePickerForReport2\" type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'=','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> >= </Td>");
                search.append("<Td><input id=\"datePickerForReport3\" type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'>=','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> <= </Td>");
                search.append("<Td><input id=\"datePickerForReport4\" type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'<=','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> <> </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText3\" id=\"1srchd").append(column).append("\" size=\"5\" onkeyup=\"parent.doSrchOperation(this,'BT','").append(columnId).append("',event,'").append(reportId).append("',document.getElementById('2srchd").append(column).append("'),'").append(ctxPath).append("') \">");
                search.append(" -");
                search.append("<input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText4\" id=\"2srchd").append(column).append("\" size=\"5\" onkeyup=\"parent.doSrchOperation(this,'BT','").append(columnId).append("',event,'").append(reportId).append("',document.getElementById('1srchd").append(column).append("'),'").append(ctxPath).append("') \">");
                search.append(" </Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td colspan=\"2\"><a style=\"font-size:8pt\" href=\"javascript:parent.doNewSrchOperation('clear','").append(columnId).append("','").append(reportId).append("','").append(ctxPath).append("')\">Clear Filter</a></Td>");
                search.append("</Tr>");
                search.append(" </Table>");
                search.append("</Div>");
                search.append("</Td>");
            } else {
                search.append("<Td ID=\"").append(row.getID(column)).append("\" style=\"display:").append(row.getDisplayStyle(column)).append("\" ><Input type='text' name='search").append(column).append("' class=\"inputbox\" style=\"width:100%\" value=\"").append(srchVal).append("\" readonly onClick=\"parent.doOpenMenu(document.getElementById('SRCHMENU_").append(column).append("'))\">");
                search.append("<Div style=\"position:absolute;display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto; z-index: 1;\" name=\"SRCHMENU_\" id=\"SRCHMENU_").append(column).append("\" >");
                search.append("<Table border=\"1\" class=\"tableBody\" CELLPADDING=\"0\" CELLSPACING=\"0\" STYLE=\"padding:0\">");
                search.append("<Tr>");
                search.append("<Td> = </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'=','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> * </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText\" size=\"8\" onkeyup=\"parent.doSrchOperation(this,'*','").append(columnId).append("',event,'").append(reportId).append("','").append("','").append(ctxPath).append("')\"></Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td> <> </Td>");
                search.append("<Td><input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText1\" id=\"1srch").append(column).append("\" size=\"5\" onkeyup=\"parent.doSrchOperation(this,'BT','").append(columnId).append("',event,'").append(reportId).append("',document.getElementById('2srch").append(column).append("'),'").append(ctxPath).append("') \">");
                search.append(" -");
                search.append("<input type=\"text\" style=\"font-size:8pt\" class=\"inputbox\" name=\"srchText2\" id=\"2srch").append(column).append("\" size=\"5\" onkeyup=\"parent.doSrchOperation(this,'BT','").append(columnId).append("',event,'").append(reportId).append("',document.getElementById('1srch").append(column).append("'),'").append(ctxPath).append("') \">");
                search.append(" </Td>");
                search.append("</Tr>");
                search.append("<Tr>");
                search.append("<Td colspan=\"2\"><a style=\"font-size:8pt\" href=\"javascript:parent.doNewSrchOperation('clear','").append(columnId).append("','").append(reportId).append("','").append(ctxPath).append("')\">Clear Filter</a></Td>");
                search.append("</Tr>");
                search.append(" </Table>");
                search.append("</Div>");
                search.append("</Td>");

            }

        }
        search.append("</Tr>");

        return parentHtml.append(search);
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
}
