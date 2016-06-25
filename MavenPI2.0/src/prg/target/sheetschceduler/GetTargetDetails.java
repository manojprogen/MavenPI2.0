package prg.target.sheetschceduler;

import prg.db.PbDb;
import prg.db.PbReturnObject;

public class GetTargetDetails {

    public String getParentEleId(String eleId, String targetId) throws Exception {
        String parentId = "-1";
        String parentQ = "select * from prg_target_param_details where target_id=" + targetId + " and dim_id in(select dim_id from prg_target_param_details "
                + " where target_id=" + targetId + " and element_id in(" + eleId + ") ) order by dim_id, rel_level";
        PbDb pbDb = new PbDb();
        ////////////////////////.println(" parentQ -0 -0 "+parentQ);
        PbReturnObject pbro = pbDb.execSelectSQL(parentQ);
        int n = -1;
        for (int y = 0; y < pbro.getRowCount(); y++) {
            if (pbro.getFieldValueString(y, "ELEMENT_ID").equalsIgnoreCase(eleId) && y != 0) {
                n = y - 1;
            }
        }
        if (n > -1) {
            parentId = pbro.getFieldValueString(n, "ELEMENT_ID");
        }
        ////////////////////////.println(" parentId -=- = "+parentId);
        return parentId;
    }
}
