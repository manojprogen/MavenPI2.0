/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.test;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author progen
 */
public class TableHelper {

    private String TableName;
    private Set<String> columnNames = new HashSet<String>();
    private ArrayList<String> columnTypes = new ArrayList<String>();
    private ArrayList columnSizes = new ArrayList();
    private HttpServletRequest request;

    /**
     * @return the TableName
     */
    public String getTableName() {
        return TableName;
    }

    /**
     * @param TableName the TableName to set
     */
    public void setTableName(String TableName) {
        this.TableName = TableName;
    }

    /**
     * @return the columnNames
     */
    public Set<String> getColumnNames() {
        return columnNames;
    }

    /**
     * @param columnNames the columnNames to set
     */
    public void setColumnNames(Set<String> columnNames) {
//        
//        
        this.columnNames = columnNames;
    }

    /**
     * @return the columnTypes
     */
    public ArrayList<String> getColumnTypes() {
        return columnTypes;
    }

    /**
     * @param columnTypes the columnTypes to set
     */
    public void setColumnTypes(ArrayList<String> columnTypes) {
        this.columnTypes = columnTypes;
    }

    /**
     * @return the columnSizes
     */
    public ArrayList getColumnSizes() {
        return columnSizes;
    }

    /**
     * @param columnSizes the columnSizes to set
     */
    public void setColumnSizes(ArrayList columnSizes) {
        this.columnSizes = columnSizes;
    }

    public static Predicate<TableHelper> getAccessTablePredicate(final String tableName) {
        Predicate<TableHelper> predicate = new Predicate<TableHelper>() {

            public boolean apply(TableHelper input) {
                if (input.TableName.equalsIgnoreCase(tableName)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public TableHelper getColumnValues(String tableName) {
        HashMap hashMap = (HashMap) request.getSession(false).getAttribute("MYSQLTABLES");
        TableHelper tableHelper = (TableHelper) hashMap.get(tableName);
        if (tableHelper == null) {
            tableHelper = (TableHelper) hashMap.get(tableName.toLowerCase());
        }
        return tableHelper;

    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
