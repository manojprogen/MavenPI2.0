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
public class PbXMLGenerator {

    private HttpServletResponse response = null;
    private HttpSession session = null;
    private int fromRow;
    private int toRow;

    public PbXMLGenerator(HttpServletResponse response, HttpSession session) {
        this.response = response;
        this.session = session;
    }

    public void createXMLFile(Container container, String userId) throws IOException {

        DataSnapshotGenerator dataSnapshotGenrtr = new DataSnapshotGenerator();
        dataSnapshotGenrtr.setFromRow(fromRow);
        dataSnapshotGenrtr.setToRow(toRow);
        String dataSnapshotFileName = dataSnapshotGenrtr.generateAndStoreXMLSnapshot(container, userId);
        ServletUtilities.downloadFile(dataSnapshotFileName, response, "application/xml");
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
}
