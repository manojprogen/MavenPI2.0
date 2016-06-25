/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class SaveParentGroup extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveParentGroup.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DimensionResourceBundle resBundle = new DimensionResourceBundle();
        PbDb pbdb = new PbDb();
        String finalQuery = "";
        ArrayList list = new ArrayList();
        Connection con = null;
        try {
            String tableName = request.getParameter("tableName");
            String colName = request.getParameter("colName");
            String connectionId = request.getParameter("connectionId");
            int tabRowCount = Integer.parseInt(request.getParameter("tabRowCount"));
            String groupName = request.getParameter("grpName").trim();
            groupName = groupName.replace(" ", "_");;
            groupName = groupName.replace("#", "_");
            groupName = groupName.replace("&", "_");
            groupName = groupName.replace("!", "_");
            groupName = groupName.replace("@", "_");
            groupName = groupName.replace("(", "_");
            groupName = groupName.replace(")", "_");
            groupName = groupName.replace("[", "_");
            groupName = groupName.replace("]", "_");
            groupName = groupName.replace("{", "_");
            groupName = groupName.replace("}", "_");
            String groupNameori = groupName;
            groupName = groupName.toUpperCase();
            String groupDesc = request.getParameter("grpDesc");
            String dbtableId = request.getParameter("tabId");
            String dbcolId = request.getParameter("colId");
            String memId = request.getParameter("memId");
            String hieId = request.getParameter("hieId");
            String dimId = request.getParameter("dimId");
            String isNewTable = request.getParameter("isNewTable");

            //code to get dimTabId
            String dimNewTabIdQuery = "select dim_tab_id  from prg_qry_dim_tables where tab_id=" + dbtableId + " and dim_id=" + dimId;
            PbReturnObject pbrodimtab = pbdb.execSelectSQL(dimNewTabIdQuery);
            String dimtabId = String.valueOf(pbrodimtab.getFieldValueInt(0, 0));

            //code to check parent is from same table
            String rellevelQuery = "select rel_level from prg_qry_dim_rel_details where mem_id=" + memId + "  and rel_id=" + hieId;
            PbReturnObject pbrorellevel = pbdb.execSelectSQL(rellevelQuery);
            int rellevel = pbrorellevel.getFieldValueInt(0, 0);

            /*
             * if(rellevel>1){ String parentdimTabIdQuery="select dim_tab_id
             * from prg_qry_dim_member where member_id in (select mem_id from
             * prg_qry_dim_rel_details where rel_id="+hieId+" and
             * rel_level="+(rellevel-1)+")"; PbReturnObject
             * pbroparent=pbdb.execSelectSQL(parentdimTabIdQuery); String
             * parentdimtabId=String.valueOf(pbroparent.getFieldValueInt(0,0));
             * if(!dimtabId.equalsIgnoreCase(parentdimtabId)){ isNewTable="Y"; }
             *
             * }
             */
            //end

            String dbNewColId = "";
            String parent[] = new String[tabRowCount];
            String child[] = new String[tabRowCount];
            String gParent[] = new String[tabRowCount];
            for (int i = 0; i < tabRowCount; i++) {
                parent[i] = request.getParameter("parent[" + i + "]");
                child[i] = request.getParameter("keyValue[" + i + "]");
                if (isNewTable.equalsIgnoreCase("Y")) {
                    gParent[i] = request.getParameter("gParent[" + i + "]");
                }
            }
            //String
            if (isNewTable.equalsIgnoreCase("Y")) {
                //code for creating new table
                String addNewTable = resBundle.getString("addNewTable");
                Object obj9[] = new Object[2];
                obj9[0] = "PR_" + groupName;
                obj9[1] = "Id number," + groupName + " varchar2(4000),parent varchar2(4000)";

                String finaladdNewTable = pbdb.buildQuery(addNewTable, obj9);
                con = new BusinessGroupDAO().getConnectionIdConnection(connectionId);
                Statement st1 = con.createStatement();
                st1.executeUpdate(finaladdNewTable);
                String insertParentColumnValues = resBundle.getString("insertParentColumnValues");
                //String seqName="PR_"+groupName+"_SEQ";
                //String createSeq="CREATE SEQUENCE  "+seqName+"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE";
                // st1.executeUpdate(createSeq);
                for (int k = 0; k < parent.length; k++) {
                    // String seqIdQuery = "select "+seqName+".nextval from dual";
                    //  PbReturnObject pbrocolseq = pbdb.execSelectSQL(seqIdQuery);
                    // String seqId = String.valueOf(pbrocolseq.getFieldValueInt(0, 0));

                    Object obj10[] = new Object[5];
                    obj10[0] = "PR_" + groupName;
                    obj10[1] = groupName;
                    obj10[2] = k + 1;
                    obj10[3] = parent[k];
                    obj10[4] = gParent[k];

                    String finalinsertParentColumnValues = pbdb.buildQuery(insertParentColumnValues, obj10);
                    st1.executeUpdate(finalinsertParentColumnValues);

                }

            }


            //ADD pARENT COLUMN
            String addParentColumn = resBundle.getString("addParentColumn");
            Object obj[] = new Object[4];
            obj[0] = tableName;
            obj[1] = groupName;
            obj[2] = "VARCHAR2";
            obj[3] = "4000";
            String finaladdParentColumn = pbdb.buildQuery(addParentColumn, obj);
            con = new BusinessGroupDAO().getConnectionIdConnection(connectionId);
            Statement st = con.createStatement();
            st.executeUpdate(finaladdParentColumn);
            String updateParentColumnValues = resBundle.getString("updateParentColumnValues");
            String totChildvaluesList = "";
            for (int k = 0; k < parent.length; k++) {
                String childlist[] = child[k].split(",");
//                String childvallist = "";
                StringBuilder childvallist = new StringBuilder();


                for (int j = 0; j < childlist.length; j++) {
                    childvallist.append(",'").append(childlist[j].trim()).append("'");
//                    childvallist +=",'" + childlist[j].trim() + "'";
                }
//                childvallist = childvallist.substring(1);
                childvallist = new StringBuilder(childvallist.substring(1));
                Object obj1[] = new Object[5];
                obj1[0] = tableName;
                obj1[1] = groupName;
                obj1[2] = parent[k];
                obj1[3] = colName;
                obj1[4] = childvallist;
                totChildvaluesList += "," + childvallist;
                String finalupdateParentColumnValues = pbdb.buildQuery(updateParentColumnValues, obj1);
                st.executeUpdate(finalupdateParentColumnValues);
            }
            //ENDS
            if (!totChildvaluesList.equalsIgnoreCase("")) {
                totChildvaluesList = totChildvaluesList.substring(1);
            }
            String updateParentColumnValuesothers = resBundle.getString("updateParentColumnValuesothers");
            Object obj1[] = new Object[5];
            obj1[0] = tableName;
            obj1[1] = groupName;
            obj1[2] = "Others";
            obj1[3] = colName;
            obj1[4] = totChildvaluesList;
            String finalupdateParentColumnValues = pbdb.buildQuery(updateParentColumnValuesothers, obj1);
            st.executeUpdate(finalupdateParentColumnValues);

            if (isNewTable.equalsIgnoreCase("Y")) {
                //code for inserting table
                HttpSession session = request.getSession(false);
                String USERID = String.valueOf(session.getAttribute("USERID"));
                String dbtabQuery = "select PRG_DATABASE_MASTER_SEQ.nextval from dual";
                PbReturnObject pbrodbtab = pbdb.execSelectSQL(dbtabQuery);
                dbtableId = String.valueOf(pbrodbtab.getFieldValueInt(0, 0));
                String adddbTable = resBundle.getString("adddbTable");
                Object obj11[] = new Object[11];
                obj11[0] = connectionId;
                obj11[1] = "PR_" + groupName;
                obj11[2] = dbtableId;
                obj11[3] = "PR_" + groupName;
                obj11[4] = "PR_" + groupName;
                obj11[5] = "Table";
                obj11[6] = USERID;
                obj11[7] = USERID;
                obj11[8] = "sysdate";
                obj11[9] = "sysdate";
                obj11[10] = "";
                finalQuery = pbdb.buildQuery(adddbTable, obj11);

                list.add(finalQuery);

                //end
                //Insert into dbTable details
                String colNameList[] = new String[3];
                colNameList[0] = "ID";
                colNameList[1] = groupName;
                colNameList[2] = "PARENT";
                String colTypesList[] = new String[3];
                colTypesList[0] = "NUMBER";
                colTypesList[1] = "VARCHAR2";
                colTypesList[2] = "VARCHAR2";
                String colSizesList[] = new String[3];
                colSizesList[0] = "22";
                colSizesList[1] = "4000";
                colSizesList[2] = "4000";
                String dbNewColIdList[] = new String[3];
                for (int i = 0; i < 3; i++) {
                    String dbNewColIdQuery = "select PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual";
                    PbReturnObject pbrocol = pbdb.execSelectSQL(dbNewColIdQuery);
                    String dbNewColId1 = String.valueOf(pbrocol.getFieldValueInt(0, 0));
                    String addDbTableDets = resBundle.getString("addDbTableDets");
                    dbNewColIdList[i] = dbNewColId1;
                    dbNewColId = dbNewColId1;
                    Object obj2[] = new Object[8];
                    obj2[0] = dbNewColId1;
                    obj2[1] = dbtableId;
                    obj2[2] = colNameList[i];
                    obj2[3] = colNameList[i];
                    obj2[4] = colTypesList[i];
                    obj2[5] = colSizesList[i];
                    if (i == 0) {
                        obj2[6] = "Y";
                    } else {
                        obj2[6] = "N";
                    }
                    obj2[7] = "Y";
                    finalQuery = pbdb.buildQuery(addDbTableDets, obj2);

                    list.add(finalQuery);
                }

                //ends
                //insert into dim_Tab
                String dbdimTabQuery = "select PRG_QRY_DIM_TABLES_SEQ.nextval from dual";
                PbReturnObject pbrodimTab = pbdb.execSelectSQL(dbdimTabQuery);
                dimtabId = String.valueOf(pbrodimTab.getFieldValueInt(0, 0));
                String addDimTable = resBundle.getString("addDimTable");
                Object obj12[] = new Object[3];
                obj12[0] = dimtabId;
                obj12[1] = dimId;
                obj12[2] = dbtableId;

                finalQuery = pbdb.buildQuery(addDimTable, obj12);
                list.add(finalQuery);


                //Insert into prg_qry_dim_tab_detalis
                String dimNewColIdList[] = new String[3];
                for (int j = 0; j < 3; j++) {
                    String dimNewColIdQuery = "select PRG_QRY_DIM_TAB_DETAILS_SEQ.nextval from dual";
                    PbReturnObject pbrodimcol = pbdb.execSelectSQL(dimNewColIdQuery);
                    String dimNewColId = String.valueOf(pbrodimcol.getFieldValueInt(0, 0));
                    String addDimtabDets = resBundle.getString("addDimtabDets");
                    dimNewColIdList[j] = dimNewColId;
                    Object obj3[] = new Object[5];
                    obj3[0] = dimNewColId;
                    obj3[1] = dimtabId;
                    obj3[2] = dbNewColIdList[j];
                    obj3[3] = "Y";
                    obj3[4] = "N";

                    finalQuery = pbdb.buildQuery(addDimtabDets, obj3);
                    list.add(finalQuery);
                }
                //end


            } else {
                //Insert into dbTable details
                String dbNewColIdQuery = "select PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual";
                PbReturnObject pbrocol = pbdb.execSelectSQL(dbNewColIdQuery);
                dbNewColId = String.valueOf(pbrocol.getFieldValueInt(0, 0));
                String addDbTableDets = resBundle.getString("addDbTableDets");
                Object obj2[] = new Object[8];
                obj2[0] = dbNewColId;
                obj2[1] = dbtableId;
                obj2[2] = groupName;
                obj2[3] = groupName;
                obj2[4] = "VARCHAR2";
                obj2[5] = "4000";
                obj2[6] = "N";
                obj2[7] = "Y";
                finalQuery = pbdb.buildQuery(addDbTableDets, obj2);

                list.add(finalQuery);


                //ends

                //Insert into prg_qry_dim_tab_detalis

                String dimNewColIdQuery = "select PRG_QRY_DIM_TAB_DETAILS_SEQ.nextval from dual";
                PbReturnObject pbrodimcol = pbdb.execSelectSQL(dimNewColIdQuery);
                String dimNewColId = String.valueOf(pbrodimcol.getFieldValueInt(0, 0));
                String addDimtabDets = resBundle.getString("addDimtabDets");
                Object obj3[] = new Object[5];
                obj3[0] = dimNewColId;
                obj3[1] = dimtabId;
                obj3[2] = dbNewColId;
                obj3[3] = "Y";
                obj3[4] = "N";

                finalQuery = pbdb.buildQuery(addDimtabDets, obj3);
                //////////////////////////////////////////////////////////////////////////////////////////////////.println.println("addDimtabDets---" + finalQuery);
                list.add(finalQuery);
                //end

            }


            //Insert into Dim member
            String memQuery = "select PRG_QRY_DIM_MEMBER_SEQ.nextval from dual";
            PbReturnObject pbromemId = pbdb.execSelectSQL(memQuery);
            String dimmemId = String.valueOf(pbromemId.getFieldValueInt(0, 0));
            String addDimMember = resBundle.getString("addDimMember");
            Object obj4[] = new Object[8];
            obj4[0] = dimmemId;
            obj4[1] = groupNameori;
            obj4[2] = dimId;
            obj4[3] = dimtabId;
            obj4[4] = "Y";
            obj4[5] = dbtableId;
            if (isNewTable.equalsIgnoreCase("Y")) {
                obj4[6] = "SELECT " + groupName + "," + groupName + " From  PR_" + groupName;;
            } else {

                obj4[6] = "SELECT " + groupName + "," + groupName + " From " + tableName;
            }
            obj4[7] = groupDesc;

            finalQuery = pbdb.buildQuery(addDimMember, obj4);
            //////////////////////////////////////////////////////////////////////////////////////////////////.println.println("addDimMember---" + finalQuery);
            list.add(finalQuery);
            //end

            //insert into Dim mem Details
            String addDimMemDets = resBundle.getString("addDimMemDets");
            String memkeyQuery = "select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual";
            PbReturnObject pbromemkeyId = pbdb.execSelectSQL(memkeyQuery);
            String dimmemkeyId = String.valueOf(pbromemkeyId.getFieldValueInt(0, 0));
            String memvalQuery = "select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual";
            PbReturnObject pbromemvalId = pbdb.execSelectSQL(memvalQuery);
            String dimmemvalId = String.valueOf(pbromemvalId.getFieldValueInt(0, 0));

            Object obj5[] = new Object[4];
            obj5[0] = dimmemkeyId;
            obj5[1] = dimmemId;
            obj5[2] = dbNewColId;
            obj5[3] = "KEY";
            finalQuery = pbdb.buildQuery(addDimMemDets, obj5);
            //////////////////////////////////////////////////////////////////////////////////////////////////.println.println("addDimMemDets---" + finalQuery);
            list.add(finalQuery);

            Object obj6[] = new Object[4];
            obj6[0] = dimmemvalId;
            obj6[1] = dimmemId;
            obj6[2] = dbNewColId;
            obj6[3] = "VALUE";
            finalQuery = pbdb.buildQuery(addDimMemDets, obj6);
            //////////////////////////////////////////////////////////////////////////////////////////////////.println.println("addDimMemDets---" + finalQuery);
            list.add(finalQuery);
            //end


            //code to get rellevel
            // String rellevelQuery="select rel_level from prg_qry_dim_rel_details where mem_id="+memId;
            //PbReturnObject rellevelpbro=pbdb.execSelectSQL(rellevelQuery);
            // int rellevel=rellevelpbro.getFieldValueInt(0,0);
            String updateRelLevel = resBundle.getString("updateRelLevel");
            Object obj7[] = new Object[2];
            obj7[0] = hieId;
            obj7[1] = rellevel;
            finalQuery = pbdb.buildQuery(updateRelLevel, obj7);
            ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("updateRelLevel---" + finalQuery);
            list.add(finalQuery);

            //end

            //insert new one in rel_dets
            String addDimRelDets = resBundle.getString("addDimRelDets");
            Object obj8[] = new Object[3];
            obj8[0] = hieId;
            obj8[1] = dimmemId;
            obj8[2] = rellevel;
            finalQuery = pbdb.buildQuery(addDimRelDets, obj8);
            ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("insertRelLevel---" + finalQuery);
            list.add(finalQuery);

            //end
            pbdb.executeMultiple(list);


            con.close();
            request.setAttribute("forward", "success");
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return mapping.findForward(SUCCESS);
    }
}
