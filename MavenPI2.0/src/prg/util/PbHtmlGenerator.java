/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prg.db.Container;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class PbHtmlGenerator {

    private HttpServletResponse response = null;
    private HttpSession session = null;
    private int fromRow;
    private int toRow;
    private String fromFlag;
    private String paramType;
    private String htmlCellHeight;
    Container container = null;

    public PbHtmlGenerator(HttpServletResponse response, HttpSession session) {
        this.response = response;
        this.session = session;
    }

    public PbHtmlGenerator() {
    }

    public void setContainer(Container container) {
        this.container = container;

    }

    public void createHTMLFile(Container container, String userId) throws IOException, ParseException {

        DataSnapshotGenerator dataSnapshotGenrtr = new DataSnapshotGenerator();
        dataSnapshotGenrtr.setFromRow(fromRow);
        dataSnapshotGenrtr.setToRow(toRow);
        dataSnapshotGenrtr.paramType = this.getParamType();
        dataSnapshotGenrtr.htmlCellHeight = this.getHtmlCellHeight();
        String dataSnapshotFileName = dataSnapshotGenrtr.generateAndStoreHtmlSnapshot(container, userId, this.fromFlag);
        ServletUtilities.downloadFile(dataSnapshotFileName, response, "application/html");
        ServletUtilities.markFileForDeletion(dataSnapshotFileName, session);
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

    public String getFromFlag() {
        return fromFlag;
    }

    public void setFromFlag(String fromFlag) {
        this.fromFlag = fromFlag;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getHtmlCellHeight() {
        return htmlCellHeight;
    }

    public void setHtmlCellHeight(String htmlCellHeight) {
        this.htmlCellHeight = htmlCellHeight;
    }

    public String queryhtmlDownload(PbReturnObject retObj, String connId) throws IOException {
        ServletWriterTransferObject swt = null;
        swt = ServletUtilities.createBufferedWriter("test", "html");
        Writer writer = swt.writer;
        StringBuilder tableHtml = new StringBuilder();
        int colcunt = retObj.getColumnCount();

        tableHtml.append("<table border=2>");
        tableHtml.append("<tr>");
        for (int i = 0; i < colcunt; i++) {
            tableHtml.append("<th style='font-size: 15px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'>").append(retObj.getColumnNames()[i]).append("</th>");
        }
        tableHtml.append("</tr>");
        for (int i = 0; i < retObj.getRowCount(); i++) {
            tableHtml.append("<tr>");
            for (int j = 0; j < colcunt; j++) {
                tableHtml.append("<td>").append(retObj.getFieldValueString(i, j)).append("</td>");
            }
            tableHtml.append("</tr>");
        }
        tableHtml.append("</table>");

        writer.write(tableHtml.toString());
        writer.flush();
        writer.close();
        return swt.fileName;
    }
}
