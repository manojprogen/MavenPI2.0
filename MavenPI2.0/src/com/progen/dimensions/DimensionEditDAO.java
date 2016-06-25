package com.progen.dimensions;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class DimensionEditDAO extends PbDb {

    PbDimensionResourceBundle rsrcBundle = new PbDimensionResourceBundle();
    public static Logger logger = Logger.getLogger(DimensionEditDAO.class);

    public String checkQueryDimension(String dimId) throws Exception {
        String status = "";
        String qryDimQuery = rsrcBundle.getString("checkQryDimension");
        Object dimOb[] = new Object[1];
        dimOb[0] = dimId;
        String finqryDimQuery = buildQuery(qryDimQuery, dimOb);
        ////////////////////////////////////////////.println(" finqryDimQuery "+finqryDimQuery);
        PbReturnObject dimObj = execSelectSQL(finqryDimQuery);
        if (dimObj.getRowCount() > 0) {
            status = "Not Deleteable";
        } else {
            status = "Deleteable";
        }
        return status;
    }

    public PbReturnObject getDimensionName(String dimId) throws Exception {
        String dimName = "";
        String getDimName = rsrcBundle.getString("getDimName");
        Object dimOb[] = new Object[1];
        dimOb[0] = dimId;
        String fingetDimName = buildQuery(getDimName, dimOb);
        ////////////////////////////////////////////.println(" fingetDimName "+fingetDimName);
        PbReturnObject nameObj = execSelectSQL(fingetDimName);
        dimName = nameObj.getFieldValueString(0, "DIMENSION_NAME");
        return nameObj;
    }

    public String deleteQueryDimension(String dimId) throws Exception {
        String status = "";
        ArrayList delList = new ArrayList();
        String deleteDimTabDetails = rsrcBundle.getString("deleteDimTabDetails");
        String deleteDimTables = rsrcBundle.getString("deleteDimTables");
        String deleteDimRelDetails = rsrcBundle.getString("deleteDimRelDetails");
        String deleteDimRel = rsrcBundle.getString("deleteDimRel");
        String deleteDimMemDetails = rsrcBundle.getString("deleteDimMemDetails");
        String deleteDimMember = rsrcBundle.getString("deleteDimMember");
        String deleteDimension = rsrcBundle.getString("deleteDimension");
        Object dimOb[] = new Object[1];
        dimOb[0] = dimId;
        String findeleteDimTabDetails = buildQuery(deleteDimTabDetails, dimOb);
        delList.add(findeleteDimTabDetails);
        ////////////////////////////////////////////.println(" findeleteDimTabDetails "+findeleteDimTabDetails);
        String findeleteDimTables = buildQuery(deleteDimTables, dimOb);
        delList.add(findeleteDimTables);
        ////////////////////////////////////////////.println(" findeleteDimTables "+findeleteDimTables);
        String findeleteDimRelDetails = buildQuery(deleteDimRelDetails, dimOb);
        delList.add(findeleteDimRelDetails);
        ////////////////////////////////////////////.println(" findeleteDimRelDetails "+findeleteDimRelDetails);
        String findeleteDimRel = buildQuery(deleteDimRel, dimOb);
        delList.add(findeleteDimRel);
        ////////////////////////////////////////////.println(" findeleteDimRel "+findeleteDimRel);

        String findeleteDimMemDetails = buildQuery(deleteDimMemDetails, dimOb);
        delList.add(findeleteDimMemDetails);
        ////////////////////////////////////////////.println(" findeleteDimMemDetails "+findeleteDimMemDetails);
        String findeleteDimMember = buildQuery(deleteDimMember, dimOb);
        delList.add(findeleteDimMember);
        ////////////////////////////////////////////.println(" findeleteDimMember "+findeleteDimMember);
        String findeleteDimension = buildQuery(deleteDimension, dimOb);
        delList.add(findeleteDimension);
        ////////////////////////////////////////////.println(" findeleteDimension "+findeleteDimension);

        try {
            executeMultiple(delList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return status;
    }

    public HashMap checkQueryDimensionForRename(String dimId, String conId, String newDimName) throws Exception {
        String status = "";
        HashMap details = new HashMap();
        String qryDimQuery = rsrcBundle.getString("checkQryDimension");
        Object dimOb[] = new Object[1];
        dimOb[0] = dimId;
        String finqryDimQuery = buildQuery(qryDimQuery, dimOb);
        ////////////////////////////////////////////.println(newDimName+" finqryDimQuery "+finqryDimQuery);
        PbReturnObject dimObj = execSelectSQL(finqryDimQuery);
        if (dimObj.getRowCount() > 0) {
            status = "No";
        } else {
            status = "Yes";
        }

        details.put("status", status);
        String checkDimForRename = rsrcBundle.getString("checkDimForRename");
        Object conOb[] = new Object[1];
        conOb[0] = conId;
        String fincheckDimForRename = buildQuery(checkDimForRename, conOb);
        ////////////////////////////////////////////.println(" fincheckDimForRename "+fincheckDimForRename);
        String alreadyExists = "false";
        PbReturnObject retOb = execSelectSQL(fincheckDimForRename);
        for (int u = 0; u < retOb.getRowCount(); u++) {
            String dimName = retOb.getFieldValueString(u, "DIMENSION_NAME");
            String localDimId = retOb.getFieldValueString(u, "DIMENSION_ID");
            ////////////////////////////////////////////.println(dimName+" newDimName "+newDimName);
            if (dimName.equalsIgnoreCase(newDimName)) {
                if (!localDimId.equalsIgnoreCase(dimId)) {
                    alreadyExists = "true";
                }
            }
            if (alreadyExists.equalsIgnoreCase("true")) {
                break;
            }
        }
        ////////////////////////////////////////////.println(status+" -in func alreadyExists "+alreadyExists);
        details.put("alreadyExists", alreadyExists);
        return details;
    }

    public boolean renameDimension(String dimId, String newDimName, String dimdesc) throws Exception {
        String renameQryDimension = rsrcBundle.getString("renameQryDimension");
        String renameGrpDimension = rsrcBundle.getString("renameGrpDimension");
        String renameRepDimension = rsrcBundle.getString("renameRepDimension");
        boolean st = true;
        Object updateObj[] = new Object[3];
        updateObj[0] = newDimName;
        updateObj[1] = dimdesc;
        updateObj[2] = dimId;
        Object updateObj1[] = new Object[2];
        updateObj1[0] = newDimName;
        updateObj1[1] = dimId;
        String finrenameQryDimension = buildQuery(renameQryDimension, updateObj);
        String finrenameGrpDimension = buildQuery(renameGrpDimension, updateObj);
        String finrenameRepDimension = buildQuery(renameRepDimension, updateObj1);
        //
        ////////////////////////////////////////////.println(" finrenameQryDimension "+ finrenameQryDimension);
        try {
            execModifySQL(finrenameQryDimension);
            execModifySQL(finrenameGrpDimension);
            execModifySQL(finrenameRepDimension);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return st;
    }
    //07-12-09

    public String checkQueryDimTableDelete(String connectionId, String dimId) throws Exception {
        String status = "";
        String checkQuery = "select * from prg_grp_dimensions where qry_dim_id=" + dimId;
        PbReturnObject pbro = execSelectSQL(checkQuery);
        if (pbro.getRowCount() > 0) {
            status = "false";
        } else {
            status = "true";
        }
        return status;
    }

    public void deleteDimTable(String connectionId, String dimId) throws Exception {
        String deleteQ = "delete from prg_qry_dim_tables where  dim_id=" + dimId;
        ArrayList al = new ArrayList();
        // //////////////////////////////////////.println(" deleteQ "+deleteQ);
        al.add(deleteQ);
        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public String editHierarachy(String dimid, String relID) {

        String query = "select M.MEMBER_NAME as MEMBER_NAME ,r.MEM_ID as MEM_ID ,R.REL_ID as REL_ID  from prg_qry_dim_member m,prg_qry_dim_rel_details r where  r.mem_id =m.member_id AND m.dim_id=" + dimid + " and r.rel_id=" + relID + " order by rel_level";
        PbReturnObject dimDetailsObject = new PbReturnObject();
        StringBuilder sbLis = new StringBuilder();
        try {
            dimDetailsObject = super.execSelectSQL(query);

            for (int i = 0; i < dimDetailsObject.getRowCount(); i++) {

                sbLis.append("<li  class='navtitle-hover' style='width:180px;height: 25px; color:white ' " + "id='").append(dimDetailsObject.getFieldValueString(i, "MEM_ID")).append("'>").append(" <table width='100%'><tr><td width='70%' align='left'><font style='font-size: 13px;text-align: center' color='black' > ").append(dimDetailsObject.getFieldValueString(i, "MEMBER_NAME")).append("</font></td><td width='70%' align='right'> <span id='' class='ui-icon ui-icon-trash' onclick=deleteHierarach('" + dimDetailsObject.getFieldValueString(i, "MEM_ID") + "','" + dimDetailsObject.getFieldValueString(i, "REL_ID") + "','" + dimid + "')/> </td></tr></table>").append("</li>");


            }
            sbLis.append("splitStr");
            sbLis.append("<input type='button' class='navtitle-hover' value='Done' name='saveEditHierarachy' align='middle' onclick=saveHirrarchy('" + dimid + "','" + relID + "')>");

        } catch (Exception e) {
        }
        return sbLis.toString();
    }

    public String deleteHierarachyLevel(String memId, String relId, String dimID) {
        String query = "delete from prg_qry_dim_rel_details where mem_id =" + memId + " and rel_id =" + relId;
        try {
            super.execModifySQL(query);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return editHierarachy(dimID, relId);
    }

    public boolean saveHierarachyLevel(String[] memIdArray, String relId, String dimID) {
        String query = "update prg_qry_dim_rel_details set rel_level=& where mem_id=& and rel_id =&";
        String finalQuery = "";
        Object[] object = null;
        ArrayList queryList = new ArrayList();
        boolean chresult = false;
        try {
            object = new Object[3];
            for (int i = 0; i < memIdArray.length; i++) {
                object[0] = i + 1;
                object[1] = memIdArray[i];
                object[2] = relId;
                finalQuery = super.buildQuery(query, object);
                queryList.add(finalQuery);
            }
            chresult = super.executeMultiple(queryList);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        return chresult;
    }
}
