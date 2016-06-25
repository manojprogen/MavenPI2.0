/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.reportview.db.PbReportViewerDAO;
import java.util.HashMap;
import java.util.Map;
import prg.db.PbReturnObject;

/**
 *
 * @author ProGen
 */
public class PbDisplayLabel {

    private int user_id;
    private int company_id;
    private String element_id;
    private String disp_label;
    private String act_formula;
    private String default_company;
    Map<String, HashMap<String, String>> disp_lab = new HashMap<String, HashMap<String, String>>();
    public static PbDisplayLabel displayLabel = null;

    public static PbDisplayLabel getPbDisplayLabel() {
        return displayLabel;
    }

    public static void createPbDisplayLabel(PbReturnObject dispLabel) {
        if (displayLabel == null) {
            displayLabel = new PbDisplayLabel();
        }
        if (dispLabel != null && dispLabel.getRowCount() > 0) {
            displayLabel.setUserId(dispLabel.getFieldValueInt(0, "USER_ID"));
            displayLabel.setDefaultCompanyId(dispLabel.getFieldValueString(0, "DEFAULT_COMPANY"));

            displayLabel.setLabelMap(dispLabel);
        }

    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getCompanyId() {
        return company_id;
    }

    public void setCompanyId(int authUser) {
        this.company_id = company_id;
    }

    public String getDefaultCompanyId() {
        return default_company;
    }

    public void setDefaultCompanyId(String default_comp) {
        this.default_company = default_comp;
    }

    public void setLabelMap(PbReturnObject dispLabel) {
        setDispLab();
        PbReportViewerDAO pbrv = new PbReportViewerDAO();
        PbReturnObject obj = pbrv.getColDisplayCompany(user_id);
//        for (int j = 0; j < dispLabel.getRowCount(); j++) {
//            String element_d = dispLabel.getFieldValueDateString(j, "ELEMENT_ID");
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                disp_lab.put(dispLabel.getFieldValueString(j, "ELEMENT_ID"), dispLabel.getFieldValueString(j, "DISP_LABEL"));
//            } else {
//                disp_lab.put(dispLabel.getFieldValueString(j, "element_id"), dispLabel.getFieldValueString(j, "disp_label"));
//            }
//        }
        String company_id = "";
        for (int j = 0; j < obj.getRowCount(); j++) {
            HashMap<String, String> labMap = new HashMap<String, String>();
            company_id = obj.getFieldValueString(j, 0);

            for (int k = 0; k < dispLabel.getRowCount(); k++) {
                if (company_id.equalsIgnoreCase(dispLabel.getFieldValueString(k, "COMPANY_ID"))) {
                    labMap.put(dispLabel.getFieldValueString(k, "ELEMENT_ID"), dispLabel.getFieldValueString(k, "DISP_LABEL"));
                }
            }
            disp_lab.put(company_id, labMap);
        }
    }
//    public String getColDisplay(String id){
//        if(disp_lab!=null && !disp_lab.isEmpty())
//          return disp_lab.get(id);
//        else
//            return null;
//
//    }

    public String getColDisplayWithCompany(String compId, String eleId) {
        if (disp_lab != null && !disp_lab.isEmpty()) {
            HashMap<String, String> eleMap = disp_lab.get(compId);
            if (eleMap != null && !eleMap.isEmpty()) {
                return eleMap.get(eleId);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public void setDispLab() {
        this.disp_lab.clear();

    }
}
