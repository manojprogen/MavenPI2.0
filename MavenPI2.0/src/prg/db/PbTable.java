
/*
 * PbTable.java
 *
 * Created on April 20, 2009, 6:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.db;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author Administrator
 */
public class PbTable implements Serializable {

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
