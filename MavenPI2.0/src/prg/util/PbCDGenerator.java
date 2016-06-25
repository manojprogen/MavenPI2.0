/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.servlet.ServletUtilities;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class PbCDGenerator {

    private HttpServletResponse response = null;
    private HttpSession session = null;
    private String delimiter;
    private String textIdentifier;
    private int fromRow;
    private int toRow;

    public PbCDGenerator(HttpServletResponse response, HttpSession session) {
        this.response = response;
        this.session = session;

    }

    public void createCDFile(Container container, String userId) throws IOException {

        DataSnapshotGenerator dataSnapshotGenrtr = new DataSnapshotGenerator();
        dataSnapshotGenrtr.setDelimiter(delimiter);
        dataSnapshotGenrtr.setTextIdentifier(textIdentifier);
        dataSnapshotGenrtr.setFromRow(fromRow);
        dataSnapshotGenrtr.setToRow(toRow);
        String dataSnapshotFileName = dataSnapshotGenrtr.generateAndStoreCDSnapshot(container, userId);
        ServletUtilities.downloadFile(dataSnapshotFileName, response, "application/csv");
        ServletUtilities.markFileForDeletion(dataSnapshotFileName, session);
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getTextIdentifier() {
        return textIdentifier;
    }

    public void setTextIdentifier(String textIdentifier) {
        this.textIdentifier = textIdentifier;
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
