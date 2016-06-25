/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
class SaveFormulaDAO extends PbDb {

    public static Logger logger = Logger.getLogger(SaveFormulaDAO.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbBussGrpResBundleSqlServer();
            } else {
                resourceBundle = new PbBussGrpResourceBundle();
            }
        }

        return resourceBundle;
    }
    private SaveFormulaBD saveFormulaBD;

    SaveFormulaDAO(SaveFormulaBD saveFormulaBD) {
        this.saveFormulaBD = saveFormulaBD;
    }

    public boolean getStatusOfFormula() throws Exception {
        PbReturnObject statusObject = null;
        String[] tableName = saveFormulaBD.getTableName().split(",");
        String formula = saveFormulaBD.getFormulaForSave().replace(",", "");
        String query = "";
        if (saveFormulaBD.getAggregationType() != null) {
            query = "SELECT " + saveFormulaBD.getAggregationType() + "(" + formula + ") " + "FROM  " + tableName[0] + " " + tableName[0];
        } else {
            query = "SELECT (" + formula + ") " + "FROM  " + tableName[0] + " " + tableName[0];
        }
        try {
            statusObject = super.execSelectSQL(query, ProgenConnection.getInstance().getConnectionByConId(saveFormulaBD.getConnectionId()));
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        if (statusObject != null) {
            return true;
        } else {
            return false;
        }

    }

    public void saveFormula() {
        ArrayList<String> queryList = new ArrayList<String>();
        String[] measures = saveFormulaBD.getMeasure().split(",");
        String srcQuery = getResourceBundle().getString("saveFormulainBUSSSRC");
        String detailsQuery = getResourceBundle().getString("saveFormulainBUSSDetails");

        Object[] srcObject = new Object[4];
        srcObject[0] = saveFormulaBD.getBussTableId();
        srcObject[1] = saveFormulaBD.getBussTableId();
        srcObject[2] = saveFormulaBD.getFormulaName();
        srcObject[3] = saveFormulaBD.getMeasureType();
        queryList.add(super.buildQuery(srcQuery, srcObject));
        Object[] detailsObjects = new Object[8];
        detailsObjects[0] = saveFormulaBD.getBussTableId();
        detailsObjects[1] = "(+" + saveFormulaBD.getFormulaName().replace(",", "") + ")";
        detailsObjects[2] = saveFormulaBD.getMeasureType();
        detailsObjects[3] = saveFormulaBD.getFormulaForSave();
        detailsObjects[4] = saveFormulaBD.getAggregationType();
        detailsObjects[5] = saveFormulaBD.getFormulaName().replace("_", " ");
        //referd Element
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            if (measures.length < 1) {
                detailsObjects[6] = "'" + measures[0] + "'";
                //display formul
                detailsObjects[7] = "'B_" + measures[0] + "'";
            } else {
                detailsObjects[6] = "null";
                detailsObjects[7] = "null";
            }

        } else {
            if (measures.length < 1) {
                detailsObjects[6] = measures[0];
                //display formul
                detailsObjects[7] = "B_" + measures[0];
            } else {
                detailsObjects[6] = null;
                detailsObjects[7] = null;
            }

        }

        queryList.add(super.buildQuery(detailsQuery, detailsObjects));
//       
        super.executeMultiple(queryList);
    }
}
