/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.report.PbReportCollection;
import com.progen.report.data.DataFacade;
import com.progen.report.data.RowViewTableBuilder;
import com.progen.report.data.TableBuilder;
import com.progen.report.display.OfficeTableBodyDisplay;
import com.progen.report.display.OfficeTableHeaderDisplay;
import com.progen.report.display.OfficeTableSubTotalDisplay;
import com.progen.report.display.table.TableDisplay;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class OfficeGenerator {

    private HttpServletResponse response = null;
    private HttpSession session = null;
    private int fromRow;
    private int toRow;
    public static Logger logger = Logger.getLogger(OfficeGenerator.class);

    public OfficeGenerator(HttpServletResponse response, HttpSession session) {
        this.response = response;
        this.session = session;
    }

    public String getFileName(String reportId, String userId) {
        return ServletUtilities.prefix + reportId + "_" + userId + "_DS";
    }

    public void createExcelFile(Container container, String userId) throws IOException {
        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        displayHelper = new OfficeTableHeaderDisplay(tableBldr);
        displayHelper.setSheet(sheet);
        displayHelper.setWorkbook(workbook);
        bodyHelper = new OfficeTableBodyDisplay(tableBldr);
        bodyHelper.setSheet(sheet);
        bodyHelper.setHeadlineflag("Export"); // added by Amar for halding records more tha 5k
        bodyHelper.setWorkbook(workbook);
        subTotalHelper = new OfficeTableSubTotalDisplay(tableBldr);
        subTotalHelper.setSheet(sheet);
        subTotalHelper.setWorkbook(workbook);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createFileOutputStream(this.getFileName(reportId, userId), "xlsx");
            ServletUtilities.markFileForDeletion(swt.fileName, session);
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);
            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(displayHelper.generateOutputHTML());
            FileOutputStream fos = (FileOutputStream) swt.outputStream;
            workbook.write(fos);
            fos.close();
            ServletUtilities.downloadFile(swt.fileName, response, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }
}
