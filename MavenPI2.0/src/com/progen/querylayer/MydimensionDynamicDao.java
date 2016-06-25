package com.progen.querylayer;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class MydimensionDynamicDao {

    public static Logger logger = Logger.getLogger(MydimensionDynamicDao.class);
    PbReturnObject pbretobj = new PbReturnObject();
    PbDb pbdb = new PbDb();
    private boolean flag = false;

    public String getDimensions(String connId) {

        StringBuffer dimbuffer = new StringBuffer();
        String dimstr = null;

        ArrayList finalList = new ArrayList();

        try {
            dimbuffer.append("<div style='height:553px;overflow-y:auto;'>");
            dimbuffer.append("<ul id='myList1' class='filetree'>");
            dimbuffer.append("<li class='closed' style='background-image:url('images/treeViewImages/plus.gif')><img src='images/treeViewImages/Dim.gif'><span id='123' class='dimMenu'><font size='1.5px' face='verdana'>&nbsp;Dimensions</font></span>");
            dimbuffer.append("<ul>");

            Object[] sqlobj = new Object[1];
            sqlobj[0] = connId;

            String sql = "select * from PRG_QRY_DIMENSIONS where connection_id in ('&')";
            String buildedquery = pbdb.buildQuery(sql, sqlobj);
            //S.println("buildedquery" + buildedquery);
            pbretobj = pbdb.execSelectSQL(buildedquery);

            //////////////////////////////////////////////////////////////////////////////////////.println("sql--->>"+sql);
//            rs = allDimensions.executeQuery(sql);
            int i = 0;
            for (i = 0; i < pbretobj.getRowCount(); i++) {
                Dimension dimension = new Dimension();
                dimension.setDimensionName(pbretobj.getFieldValueString(i, 1));
                dimension.setDimensionId(pbretobj.getFieldValueString(i, 0));
                if (pbretobj.getFieldValueString(i, 1).equalsIgnoreCase("UserDayInfo(Time)")) {
                    flag = true;
                    dimbuffer.append("<li class='closed' id=" + pbretobj.getFieldValueString(i, 0) + "><img src='images/treeViewImages/Dim.gif'><span class='individualDim' id=" + pbretobj.getFieldValueString(i, 0) + "><font size='1px' face='verdana'>&nbsp;&nbsp;" + pbretobj.getFieldValueString(i, 1) + "</font></span>");
                } else if (pbretobj.getFieldValueString(i, 1).equalsIgnoreCase("Time")) {
                    flag = true;
                    dimbuffer.append("<li class='closed' id=" + pbretobj.getFieldValueString(i, 0) + "><img src='images/treeViewImages/Dim.gif'><span class='individualTimeDim' id=" + pbretobj.getFieldValueString(i, 0) + "><font size='1px' face='verdana'>&nbsp;&nbsp;" + pbretobj.getFieldValueString(i, 1) + "</font></span>");
                } else {
                    flag = false;
                    dimbuffer.append("<li class='closed' id=" + pbretobj.getFieldValueString(i, 0) + "><img src='images/treeViewImages/Dim.gif'><span class='individualDim' id=" + pbretobj.getFieldValueString(i, 0) + "><font size='1px' face='verdana'>&nbsp;&nbsp;" + pbretobj.getFieldValueString(i, 1) + "</font></span>");
                }
                dimbuffer.append("<ul>");
                dimbuffer.append(getTableList(pbretobj.getFieldValueString(i, 0)));
//                dimbuffer.append(tablestr);
                dimbuffer.append(getMembersList(pbretobj.getFieldValueString(i, 0)));
//                dimbuffer.append(memlist);
//                String hierstr = getHeirarchyList(pbretobj.getFieldValueString(i, "DIMENSION_ID"));
                dimbuffer.append(getHeirarchyList(pbretobj.getFieldValueString(i, 0)));
//                ArrayList list = new ArrayList();

                //list = getTableList(rs.getString("DIMENSION_ID"));
//                dimension.setTableList(list);
//                ArrayList membersList = new ArrayList();
//                membersList = getMembersList(rs.getString("DIMENSION_ID"));
//                dimension.setMembersList(membersList);
//                ArrayList hierarchyList = new ArrayList();
//                hierarchyList = getHeirarchyList(rs.getString("DIMENSION_ID"));
//                dimension.setHierarchyList(hierarchyList);

                finalList.add(dimension);
                dimbuffer.append("</ul>");
                dimbuffer.append("</li>");

            }
            ////.println("dimbuffer \t" + dimbuffer);
//            allDimensions.close();
//            con.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        dimbuffer.append("</ul></li></ul></div>");
        dimstr = dimbuffer.toString();
        return dimstr;
//        return finalList;
    }

    public String getTableList(String dimId) {

        StringBuffer tablelist = new StringBuffer();

        String tablestr = null;
//        ////////////////////.println("dimname \t" + dimname);
//        ArrayList list = new ArrayList();
        try {
//            ProgenConnection prgcon = new ProgenConnection();

            String sql = "select q.tab_id,d.table_name, q.dim_id, q.dim_tab_id from prg_qry_dim_tables q , prg_db_master_table d where q.tab_id= d.table_id and q.dim_id=" + dimId;

//            tablelist.append("<img src='images/treeViewImages/Dim.gif'><span class='individualDim'><font size='1px' face='verdana'>&nbsp;&nbsp;" + dimname + "</font></span>");

            if (flag == true) {
                tablelist.append("<li class='closed' ><span class='folder' ><font size='1px' face='verdana' ><span id=" + dimId + ">&nbsp;Tables</span></font></span>");
            } else {
                tablelist.append("<li class='closed' ><span class='folder' ><font size='1px' face='verdana' ><span  class='tabMenu' id=" + dimId + ">&nbsp;Tables</span></font></span>");
            }
            tablelist.append("<ul>");
            PbReturnObject allTableObject = pbdb.execSelectSQL(sql);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] originaltabId = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                originaltabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[3]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);



            }
            String[] colNames = null;
            String colName = "";
            String tabName1 = "";
            String tabId1 = "";
            String sqlcolumns = "select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id=";
            for (int i = 0; i < tabId.length; i++) {


                tabName1 = tabName[i];
                tabId1 = tabId[i];

                if (!(tabName1.equalsIgnoreCase("")) && tabName1 != null) {
                    if (tabName1.equals("PR_DAY_DENOM")) {
                        tablelist.append("<li class='closed' id=" + tabId1 + "," + originaltabId[i] + "," + dimId + "," + tabName1 + "><img src='images/treeViewImages/database_table.png'><span id='table' class='standTimeDimMemMenu' onclick='dimColDelete(" + tabId1 + ")'><font size='1px' face='verdana'>&nbsp;" + tabName1 + "</font></span>");

                    } else {
                        tablelist.append("<li class='closed' id=" + tabId1 + "," + originaltabId[i] + "," + dimId + "," + tabName1 + "><img src='images/treeViewImages/database_table.png'><span id='table' class='memMenu' onclick='dimColDelete(" + tabId1 + ")'><font size='1px' face='verdana'>&nbsp;" + tabName1 + "</font></span>");
                    }
                    tablelist.append("<ul>");
                }

                String qury = sqlcolumns + tabId[i];

                int rowCountFlag = 0;
                PbReturnObject pbReturnObject = pbdb.execSelectSQL(qury);
                DimensionTable table = new DimensionTable();
                table.setTableName(tabName1);
                table.setTableId(tabId1 + "," + originaltabId[i] + "," + dimId);
//                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                String is_pk = "";
                String is_available = "";
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + tabId1 + "," + originaltabId[i] + "," + dimId;
                    is_pk = pbReturnObject.getFieldValueString(j, colNames[2]);
                    is_available = pbReturnObject.getFieldValueString(j, colNames[3]);

                    if (colName != null && !(colName.equalsIgnoreCase(""))) {
                        tablelist.append("<li id=" + columnId + "," + colName + "," + is_pk + " class='closed'>");
                        if (flag == true) {
                            if (is_available.equals("Y")) {
                                tablelist.append("<input type='checkbox' name='chk2' checked value=" + columnId + ">");
                                tablelist.append("<span><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                            }
                            if (is_available.equals("N")) {
                                tablelist.append("<input type='checkbox' name='chk2' value=" + columnId + ">");
                                tablelist.append("<span><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                            }
                            if (is_pk.equals("Y")) {
                                tablelist.append("<img src='images/treeViewImages/key.png' >");
                            }
                        } else {
                            if (is_available.equals("Y")) {
                                tablelist.append("<input type='checkbox' name='chk2' checked value=" + columnId + ">");
                                tablelist.append("<span class=\"dimensionTableMenu\"><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                            }
                            if (is_available.equals("N")) {
                                tablelist.append("<input type='checkbox' name='chk2' value=" + columnId + ">");
                                tablelist.append("<span class=\"dimensionTableMenu\"><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                            }
                            if (is_pk.equals("Y")) {
                                tablelist.append("<img src='images/treeViewImages/key.png' >");
                            }
                        }
                        tablelist.append("</li>");
                    }



                    DimensionTable secTable = new DimensionTable();
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");

                    }

                    secTable.setColumnName(colName);
                    secTable.setColumnId(columnId);
                    secTable.setIsPk(is_pk);
                    secTable.setIsAvailable(is_available);

//                    list.add(secTable);
                }
                tablelist.append("</ul></li>");
            }
            tablelist.append("</ul></li>");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        tablestr = tablelist.toString();
        return tablestr;
    }

    public String getMembersList(String dimId) {

        StringBuffer membufferstr = new StringBuffer();
        String memstr = null;

        ArrayList list = new ArrayList();
        try {

            String sql = "select member_id, member_name from prg_qry_dim_member where dim_id=" + dimId;

            membufferstr.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Members</font></span>");
            membufferstr.append("<ul>");

            PbReturnObject allMembersObject = pbdb.execSelectSQL(sql);
            String[] MemberNames = null;
            MemberNames = allMembersObject.getColumnNames();
            String[] memId = new String[allMembersObject.getRowCount()];
            String[] memName = new String[allMembersObject.getRowCount()];
            for (int i = 0; i < allMembersObject.getRowCount(); i++) {

                memId[i] = String.valueOf(allMembersObject.getFieldValueInt(i, MemberNames[0]));
                memName[i] = allMembersObject.getFieldValueString(i, MemberNames[1]);

            }

            String memName1 = "";
            String memId1 = "";

            for (int i = 0; i < memId.length; i++) {
                memName1 = memName[i];
                memId1 = memId[i];
                DimensionTable table = new DimensionTable();
                if (!(memName1.equals(""))) {
                    String memName2 = memName1;
                    membufferstr.append(" <li id=" + memName2.replaceAll(" ", "") + "~" + memId1 + "><img src='images/treeViewImages/database_table.png'><span id='member' class='memRenameMenu'>");
                    membufferstr.append("<font size='1px' face='verdana'>&nbsp;" + memName1 + "</font></span>");
                    membufferstr.append("</li>");
                }
                table.setMemberName(memName1);
                table.setMemberId(memId1);
                list.add(table);
            }
            membufferstr.append("</ul>");
            membufferstr.append("</li>");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        memstr = membufferstr.toString();
        return memstr;
    }

    public String getHeirarchyList(String dimId) {

        StringBuffer hierarchybufr = new StringBuffer();
        String hierarchystr = null;

        ArrayList list = new ArrayList();
        try {
            hierarchybufr.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'><span class='hieMenu' id=" + dimId + ">&nbsp;Hierarchies</span></font></span>");
            hierarchybufr.append("<ul>");

            String sql = "select rel_id, rel_name,IS_DEFAULT from prg_qry_dim_rel where dim_id=" + dimId;

            PbReturnObject allHierachyObject = pbdb.execSelectSQL(sql);
            String[] tableNames = null;
            tableNames = allHierachyObject.getColumnNames();
            String[] relId = new String[allHierachyObject.getRowCount()];
            String[] relName = new String[allHierachyObject.getRowCount()];
            String[] relType = new String[allHierachyObject.getRowCount()];
            for (int i = 0; i < allHierachyObject.getRowCount(); i++) {

                relId[i] = String.valueOf(allHierachyObject.getFieldValueInt(i, tableNames[0]));
                relName[i] = allHierachyObject.getFieldValueString(i, tableNames[1]);
                relType[i] = allHierachyObject.getFieldValueString(i, tableNames[2]);

            }
            String[] colNames = null;
            String colName = "";
            String relName1 = "";
            String relId1 = "";
            String relType1 = "";

            String sqlcolumns = "select m.member_id,m.member_name from prg_qry_dim_member m, prg_qry_dim_rel_details d where m.member_id= d.mem_id and d.rel_id=";
            for (int i = 0; i < relId.length; i++) {
                relName1 = relName[i];
                relId1 = relId[i];
                relType1 = relType[i];
                String clsname = "closed";
                if (i + 1 == relId.length) {
                    clsname = "last";
                }
                if (!(relName1).equals("")) {
                    hierarchybufr.append("<li class='closed'><img src='images/dim.png'>");
                    if (relType1.equals("Y")) {
                        hierarchybufr.append("<span id='heirarchy_" + dimId + "_" + relId1 + "' class='editHierarchyMenu'><font size='1px' face='verdana'>" + relName1);
                        hierarchybufr.append("<font size='1px' face='verdana' color='green'>&nbsp;<b>D</b></font></font></span>");
                    }
                    if (!(relType1.equals("Y"))) {
                        hierarchybufr.append("<span id='heirarchy_" + dimId + "_" + relId1 + "' class='editHierarchyMenu'> <font size='1px' face='verdana'>&nbsp;" + relType1 + "&nbsp;</font></span>");
                    }
                    if (i + 1 == relId.length) {
                        hierarchybufr.append("<ul><li class='last'>");
                    } else {
                        hierarchybufr.append("<ul><li>");
                    }
                }

                int rowCountFlag = 0;
                PbReturnObject pbReturnObject = pbdb.execSelectSQL(sqlcolumns + relId[i] + " order by d.rel_level");

                DimensionTable table = new DimensionTable();
                table.setRelationName(relName1);
                table.setRelationId(relId1);
                table.setRelType(relType1);
                if (pbReturnObject.getRowCount() == 0) {
                    table.setEndTable("true");
                }
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + relId1 + "," + dimId;

                    if (!(colName.equals(""))) {
                        hierarchybufr.append("<span class='parentGrpMenu' id=" + columnId + "><font size='1px' face='verdana'>");
                        hierarchybufr.append("&nbsp;" + colName + "</font></span>");
                    }

                    DimensionTable secTable = new DimensionTable();
                    secTable.setEndColumn("true");//newly added
                    if (j + 1 != pbReturnObject.getRowCount()) {
                        hierarchybufr.append("<ul><li>");
                    }
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
                        secTable.setEndColumn("false");//newly added    
                        hierarchybufr.append("</li></ul>");
                        hierarchybufr.append("</li>");
                    }
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        for (int endli = pbReturnObject.getRowCount(); endli > 1; endli--) {
                            hierarchybufr.append("</ul></li>");
                        }
                    }

                    secTable.setRelColumnName(colName);
                    secTable.setRelColumnId(columnId);
                    list.add(secTable);
                }

            }
            hierarchybufr.append("</ul></li>");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        hierarchystr = hierarchybufr.toString();

        return hierarchystr;
    }
}
