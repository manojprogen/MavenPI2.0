/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.reportview.db.ProgenReportViewerDAO;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import prg.db.Container;
import prg.db.PbReturnObject;

/**
 *
 * @author shobhit
 */
public class ProgenTimeDefinition {

    private Map<String, String> timeDefinition = new HashMap<String, String>();
    public static ProgenTimeDefinition instance = null;

    private ProgenTimeDefinition() {
    }

    public ProgenTimeDefinition(String reportId, Container container, String userId) throws SQLException, FileNotFoundException {
        String[] timeComparisonArray = new String[]{"MTD", "PMTD", "PYMTD", "QTD", "PQTD", "PYQTD", "YTD", "PYTD","WTD","PWTD","PYWTD", "PRIOR", "PT", "GT"};

        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        PbReturnObject retObj = null;
        String elementId = null;
        try {
            elementId = dao.getElementId(reportId);
        } catch (Exception ex) {
            elementId = "";
        }
        retObj = dao.getTimeDefinationReturnObject(container, retObj, elementId);
        dao.setGraphFilterData(container,reportId,elementId,userId);

        if (retObj != null && retObj.getRowCount() > 0) {
            ArrayList timeDetails = new ArrayList();
            timeDetails = container.getTimeDetailsArray();
            for (int i = 0; i < timeComparisonArray.length; i++) {

                if (timeComparisonArray[i].equalsIgnoreCase("PRIOR")) {
                    if (timeDetails.get(3).toString().equalsIgnoreCase("QTR") && timeDetails.get(4).toString().equalsIgnoreCase("Last Qtr")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Last Month")) {
                        timeDefinition.put(timeComparisonArray[i], retObj.getFieldValueString(0, 4));
                    } else if (timeDetails.get(3).toString().equalsIgnoreCase("QTR") && (timeDetails.get(4).toString().equalsIgnoreCase("Same Qtr Last Year")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Last Year")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Same Month Last Year"))) {
                        timeDefinition.put(timeComparisonArray[i], retObj.getFieldValueString(0, 1));
                    } else if (timeDetails.get(3).toString().equalsIgnoreCase("Month") && (timeDetails.get(4).toString().equalsIgnoreCase("Last Month")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetails.get(4).toString().equalsIgnoreCase("Last Period"))) {
                        timeDefinition.put(timeComparisonArray[i], retObj.getFieldValueString(0, 1));
                    } else if (timeDetails.get(3).toString().equalsIgnoreCase("Month") && timeDetails.get(4).toString().equalsIgnoreCase("Last Period")) {
                        timeDefinition.put(timeComparisonArray[i], retObj.getFieldValueString(0, 1));
                    } else if (timeDetails.get(3).toString().equalsIgnoreCase("Month") && (timeDetails.get(4).toString().equalsIgnoreCase("Same Month Last Year")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Last Year") || timeDetails.get(4).toString().equalsIgnoreCase("Same Qtr Last Year"))) {
                        timeDefinition.put(timeComparisonArray[i], retObj.getFieldValueString(0, 2));
                    } else if (timeDetails.get(3).toString().equalsIgnoreCase("Year")) {
                        timeDefinition.put(timeComparisonArray[i], retObj.getFieldValueString(0, 7));
                    }else if(timeDetails.get(3).toString().equalsIgnoreCase("week") && (timeDetails.get(4).toString().equalsIgnoreCase("Last Week") ||
                    		timeDetails.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetails.get(4).toString().equalsIgnoreCase("Last PERIOD")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Last Month"))){
                    	timeDefinition.put( timeComparisonArray[i],retObj.getFieldValueString(0, "PW_CUST_NAME"));
                    }
                    else if (timeDetails.get(3).toString().equalsIgnoreCase("WEEK") && (timeDetails.get(4).toString().equalsIgnoreCase("Same Week Last Year")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Last Year")
                            || timeDetails.get(4).toString().equalsIgnoreCase("Same Month Last Year"))) {
                    	timeDefinition.put( timeComparisonArray[i],retObj.getFieldValueString(0, "LYW_CUST_NAME"));
                    }

                }
                else if(timeComparisonArray[i].equalsIgnoreCase("PT")){
                  timeDefinition.put(timeComparisonArray[i], "PT");   
                }else if(timeComparisonArray[i].equalsIgnoreCase("GT")){
                   timeDefinition.put(timeComparisonArray[i], "GT");  
                }
                else {

                    timeDefinition.put(timeComparisonArray[i], retObj.getFieldValueString(0, i));
                }
            }
        }

    }

    public static ProgenTimeDefinition getInstance(String reportId, Container container,String userId) {
        if (instance == null) {
            try {
                instance = new ProgenTimeDefinition(reportId, container, userId);
            } catch (SQLException ex) {
                Logger.getLogger(ProgenTimeDefinition.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProgenTimeDefinition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    /**
     * @return the timeDefinition
     */
    public Map<String, String> getTimeDefinition() {
        return timeDefinition;
    }

    /**
     * @param timeDefinition the timeDefinition to set
     */
    public void setTimeDefinition(Map<String, String> timeDefinition) {
        this.timeDefinition = timeDefinition;
    }
}
