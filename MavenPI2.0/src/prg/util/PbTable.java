package prg.util;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author Administrator
 */
public class PbTable implements Serializable {

    private static final long serialVersionUID = 75264759875569828L;
    private Hashtable container = new Hashtable();
    private int rowCount = 0;

    /**
     * Creates a new instance of PbTable
     */
    public PbTable() {
    }

    public int getRowCount() {
        return rowCount;
    }

    public void addRow(Object tableRow) {
        container.put(String.valueOf(rowCount), tableRow);
        rowCount++;
    }

    public Object getRow(int rowNumber) {
        if (rowNumber >= rowCount || rowNumber < 0) {
            return null;
        } else {
            return container.get(String.valueOf(rowNumber));
        }
    }
}