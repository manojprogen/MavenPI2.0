/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.report.XtendAdapter;
import com.progen.servlet.ServletUtilities;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class PbCSVGenerator {

    private HttpServletResponse response = null;
    private HttpSession session = null;
    private int fromRow;
    private int toRow;

    public PbCSVGenerator(HttpServletResponse response, HttpSession session) {
        this.response = response;
        this.session = session;
    }

    public void createCSVFile(Container container, String userId, String csvType) throws IOException {

        DataSnapshotGenerator dataSnapshotGenrtr = new DataSnapshotGenerator();
        dataSnapshotGenrtr.setFromRow(fromRow);
        dataSnapshotGenrtr.setToRow(toRow);
        String dataSnapshotFileName = dataSnapshotGenrtr.generateAndStoreCSVSnapshot(container, userId, csvType);
        ServletUtilities.downloadFile(dataSnapshotFileName, response, "application/csv");
        ServletUtilities.markFileForDeletion(dataSnapshotFileName, session);

    }

    public void createCSVObj(Container container, String userId, String csvType) throws IOException {
        DataSnapshotGenerator dataSnapshotGenrtr = new DataSnapshotGenerator();
        XtendAdapter xtendAdapter = new XtendAdapter();
        xtendAdapter.setFromRow(fromRow);
        xtendAdapter.setToRow(toRow);
        dataSnapshotGenrtr.setFromRow(fromRow);
        dataSnapshotGenrtr.setToRow(toRow);
        String dataSnapshotFileName = dataSnapshotGenrtr.generateAndStoreCSVSnapshotXtend(container, userId, csvType);
        xtendAdapter.generateAndStoreCSV(container, userId, csvType, dataSnapshotFileName);

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
