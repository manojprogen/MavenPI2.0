package com.progen.buildtable;

//import com.progen.log.ProgenLog;
import com.progen.report.data.*;
import com.progen.report.display.table.*;
import com.progen.users.UserLayerDAO;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.jsp.JspWriter;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.PbReturnObject;

public class PbBuildTable {

    private int fromRow = 0;
    private int toRow = 0;
    private int fromColumn = 0;
    private int toColumn = 0;
    private Container container = null;
    private String contextPath = null;
    private Set<TableCellSpan> cellSpanSet = new HashSet<TableCellSpan>();
    public static Logger logger = Logger.getLogger(PbBuildTable.class);
    private String userId;

    public String getHTML(JspWriter out) throws Exception {
//        ProgenLog.log(ProgenLog.FINE, this,"getHTML","Enter Build Table "+System.currentTimeMillis());
        logger.info("Enter Build Table " + System.currentTimeMillis());
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(contextPath);
        if(facade.container.getAOId()==null){
        facade.CheckCurrentAndPriorMsrs();
        }
        TableBuilder tableBldr;
        UserLayerDAO uDAO = new UserLayerDAO();
        container.resetSubTotalRows();
        if (container.isTransposed()) {
            tableBldr = new TransposeTableBuilder(facade);
        } else if (ContainerConstants.TABLE_ROWVIEW_DISPLAY_UNWRAPPED.equals(container.getRowViewDisplayMode())) {
            tableBldr = new RowViewTableBuilder(facade);
        } else if (ContainerConstants.TABLE_ROWVIEW_DISPLAY_WRAPPED.equals(container.getRowViewDisplayMode())) {
            tableBldr = new SingleRowViewTableBuilder(facade);
        } else {
            tableBldr = new RowViewTableBuilder(facade);
        }

        tableBldr.setFromAndToRow(fromRow, toRow);

        StringBuilder htmlBuffer = new StringBuilder();
        TableDisplay displayHelper = null;
        TableDisplay menuHelper = null;
        TableDisplay searchHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        TableDisplay excelControlsHelper = null;
        if (container.isTreeTableDisplay()) {
            displayHelper = new InsightTableHeaderDisplay(tableBldr);
        } else if (ContainerConstants.PROGEN_TABLE_DISPLAY.equalsIgnoreCase(container.getTableDisplayMode())) {
            displayHelper = new TableHeaderDisplay(tableBldr);
            menuHelper = new TableMenuDisplay(tableBldr);
            HashMap paramhashmapPA = new HashMap();
            String userTypeAdmin = null;
            paramhashmapPA = facade.getparameterHash();
            userTypeAdmin = facade.getuserTypeAdmin();
            if (uDAO.getFeatureEnableHashMap("Table Search", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                searchHelper = new TableSearchDisplay(tableBldr);
            } else {
                container.setSearchReq(false);
            }
            bodyHelper = new TableBodyDisplay(tableBldr);
            subTotalHelper = new TableSubtotalDisplay(tableBldr);
        } else {
            excelControlsHelper = new ExcelControlsDisplay(tableBldr);
            displayHelper = new ExcelHeaderDisplay(tableBldr);
            bodyHelper = new ExcelBodyDisplay(tableBldr);
            subTotalHelper = new ExcelSubtotalDisplay(tableBldr);
        }

        displayHelper.setWriter(out);
        if (container.isTreeTableDisplay()) {
            // 
            //displayHelper.setNext(bodyHelper);
        } else if (container.isTransposed() || container.isMeasureGroupEnable()) {
            displayHelper.setNext(bodyHelper);
        } else {
            if (ContainerConstants.PROGEN_TABLE_DISPLAY.equalsIgnoreCase(container.getTableDisplayMode())) {
                if (container.getSearchReq()) {
                    displayHelper.setNext(menuHelper).setNext(searchHelper).setNext(bodyHelper).setNext(subTotalHelper);
                } else {
                    displayHelper.setNext(menuHelper).setNext(bodyHelper).setNext(subTotalHelper);
                }
            } else {
                excelControlsHelper.setNext(displayHelper).setNext(bodyHelper).setNext(subTotalHelper);
                displayHelper = excelControlsHelper;
            }
        }
        if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("GT")) {
            container.setGrandTotalReq(false);
        } else if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("ST")) {
            container.setNetTotalReq(false);
        } else if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("OVEMAX")) {
            container.setOverAllMaxValueReq(false);
        } else if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("OVEMIN")) {
            container.setOverAllMinValueReq(false);
        } else if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("CATMAX")) {
            container.setCatMaxValueReq(false);
        } else if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("CATMIN")) {
            container.setCatMinValueReq(false);
        } else if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("AVG")) {
            container.setAvgTotalReq(false);
        } else if (container.getMappedTo() != null && !container.getMappedTo().toString().isEmpty() && container.getMappedTo().equalsIgnoreCase("CATAVG")) {
            container.setCatAvgTotalReq(false);
        }
        htmlBuffer.append(displayHelper.generateOutputHTML());
        htmlBuffer.append("</Table>");
        htmlBuffer.append("<div id=\"bottom_anchor\"></div>");
//        htmlBuffer.append("</div>"); // By Manik for Table Display Region
        cellSpanSet = tableBldr.getCellSpans();
//        ProgenLog.log(ProgenLog.FINE, this,"getHTML","Exit Build Table "+System.currentTimeMillis());
        logger.info("Exit Build Table " + System.currentTimeMillis());
        return htmlBuffer.toString();
    }

    public String getMenuHTML(JspWriter out) {
        if (container.isTransposed()) {
            return "";
        }

        DataFacade facade = new DataFacade(container);
        facade.setCtxPath(contextPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        tableBldr.setFromAndToRow(fromRow, toRow);

        StringBuilder htmlBuffer = new StringBuilder();
        TableDisplay menuHelper = new TableMenuDisplay(tableBldr);
        menuHelper.setWriter(out);

        htmlBuffer.append(menuHelper.generateOutputHTML());

        return htmlBuffer.toString();
    }

    public String getCellSpanJSON() {
        String cellSpanJSON = "";
        int i = 0;
        for (TableCellSpan cellSpan : cellSpanSet) {
            if (i == 0) {
                cellSpanJSON = "{";
            } else {
                cellSpanJSON += ",";
            }
            i++;
            cellSpanJSON += "Span" + i + " : ";
            cellSpanJSON += cellSpan.toString();
        }
        if (i != 0) {
            cellSpanJSON += "}";
        }

        return cellSpanJSON;
    }

    public String getTabData(PbReturnObject retObj, JspWriter out) throws Exception {
        return null;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public int getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(int fromColumn) {
        this.fromColumn = fromColumn;
    }

    public int getToColumn() {
        return toColumn;
    }

    public void setToColumn(int toColumn) {
        this.toColumn = toColumn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
