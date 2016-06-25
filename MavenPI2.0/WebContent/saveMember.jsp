
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,java.sql.*,utils.db.*,java.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%

            String dbType = null;
            String dimId=null;
            String flag= request.getParameter("flag");
            String check= request.getParameter("check");
            if (session != null && session.getAttribute("MetadataDbType") != null) {
                dbType = (String) session.getAttribute("MetadataDbType");
            }
            try {
                Statement colSt, colSt1, colSt2 = null;
                ResultSet colRs = null;
                String isdenom = request.getParameter("isdenom");

                PbDb pbdb = new PbDb();
                 dimId = request.getParameter("dimId");

                String dimtabId = request.getParameter("dimtabId");

                String dbtabId = request.getParameter("dbtabId");

                String dbtabName = request.getParameter("dbtabName");

                Connection con = null;
                Statement st = null;
                ResultSet rs = null;

                String queryname = "select MEMBER_NAME  from prg_qry_dim_member where dim_id=" + dimId + " order by MEMBER_NAME desc";

                prg.db.PbReturnObject pbro1 = pbdb.execSelectSQL(queryname);
                if (isdenom == null) {
                    String desc = request.getParameter("desc");

                    String memname = request.getParameter("memname");


                    String key = request.getParameter("key");

                    String keyValue = request.getParameter("keyValue");

                    String all = request.getParameter("all");

                    String defaultValue = request.getParameter("defaultValue");


                    int p = 0;

                    for (int i = 0; i < pbro1.getRowCount(); i++) {
                        String n[] = pbro1.getFieldValueString(i, 0).split("_");

                        if (n[0].equalsIgnoreCase(memname)) {


                            if (n.length > 1) {

                                p = Integer.parseInt(n[1] + 1);
                                break;
                            } else {

                                p = 1;
                                break;
                            }
                        }
                    }

                    if (p > 0) {
                        memname = memname + "_" + (p);
                    }
                    PbReturnObject pbro = new PbReturnObject();
                    int nextval = 0;

                    String query = "";
                    if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                        query = "insert into prg_qry_dim_member ( MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,MEMBER_DESC,MEMBER_ORDER_BY,ISNULLvALUE) values('" + memname + "'," + dimId + "," + dimtabId + ",'N',null,null,'" + desc + "',"+request.getParameter("orderBy")+",'"+request.getParameter("isnullValue") +"')";

                    } else {
                        pbro = pbdb.execSelectSQL("select PRG_QRY_DIM_MEMBER_SEQ.nextval from dual");
                        nextval = pbro.getFieldValueInt(0, "NEXTVAL");
                        query = "insert into prg_qry_dim_member (MEMBER_ID, MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,MEMBER_DESC,MEMBER_ORDER_BY,ISNULLvALUE) values(" + nextval + ",'" + memname + "'," + dimId + "," + dimtabId + ",'N','','','" + desc + "',"+request.getParameter("orderBy")+",'"+request.getParameter("isnullValue") +"')";

                    }
                 
                    pbdb.execModifySQL(query);

                    if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                        pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ( ident_current('PRG_QRY_DIM_MEMBER') ," + key + ",'KEY')");

                    } else {
                        pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + key + ",'KEY')");


                    }
                    if (all == null) {
                        if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {

                            pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ( ident_current('PRG_QRY_DIM_MEMBER')," + keyValue + ",'VALUE')");

                        } else {
                            pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + keyValue + ",'VALUE')");

                        }
                    } else {
                        if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                            pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ( ident_current('PRG_QRY_DIM_MEMBER')," + keyValue + ",'VALUE')");

                        } else {
                            pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + keyValue + ",'VALUE')");

                        }
                    }

                } else {
                    int tabRowCount = Integer.parseInt(request.getParameter("tabRowCount")) - 1;

                    String keyarr[] = new String[tabRowCount - 1];
                    String keyValuearr[] = new String[tabRowCount - 1];
                    String keyarrname[] = new String[tabRowCount - 1];
                    String keyValuearrname[] = new String[tabRowCount - 1];
                    String descarr[] = new String[tabRowCount - 1];
                    String memnamearr[] = new String[tabRowCount - 1];
                    String allarr[] = new String[tabRowCount - 1];
                    String defaultarr[] = new String[tabRowCount - 1];
                    String isnullValueArr[] = new String[tabRowCount - 1];
                    String orderByColArr[] = new String[tabRowCount - 1];
                    int valuename = 1;
                    int q = 0;

                    for (int i = 1; i < tabRowCount; i++) {
                        int f = 0;


                        keyarr[i - 1] = request.getParameter("key[" + i + "]").split(",")[0];
                        keyarrname[i - 1] = request.getParameter("key[" + i + "]").split(",")[1];

                        keyValuearr[i - 1] = request.getParameter("keyValue[" + i + "]").split(",")[0];
                        isnullValueArr[i - 1] = request.getParameter("isnullValue[" + i + "]");
                        orderByColArr[i - 1] = request.getParameter("orderBy[" + i + "]");
                        keyValuearrname[i - 1] = request.getParameter("keyValue[" + i + "]").split(",")[1];
                        descarr[i - 1] = request.getParameter("desc[" + i + "]");

                        memnamearr[i - 1] = request.getParameter("memname[" + i + "]");
                        if (i > 1) {

                            for (int h = 0; h < i - 1; h++) {
                                if (memnamearr[i - 1].equals(memnamearr[h].split("_")[0])) {
                                    if (memnamearr[h].split("_").length > 1) {

                                        memnamearr[i - 1] = memnamearr[i - 1] + "_" + (Integer.parseInt(memnamearr[h].split("_")[1]) + 1);
                                        f = 1;
                                        break;
                                    } else {

                                        memnamearr[i - 1] = memnamearr[i - 1] + "_" + valuename;
                                        valuename++;
                                        break;
                                    }
                                } else {

                                    memnamearr[i - 1] = request.getParameter("memname[" + i + "]");
                                }

                            }//inner
                        }

                        allarr[i - 1] = request.getParameter("all[" + i + "]");

                        defaultarr[i - 1] = request.getParameter("defaultValue[" + i + "]");

                        if (f == 0) {
                            for (int j = 0; j < pbro1.getRowCount(); j++) {
                                String n[] = pbro1.getFieldValueString(j, 0).split("_");


                                if (n[0].equalsIgnoreCase(memnamearr[i - 1])) {


                                    if (n.length > 1) {

                                        q = Integer.parseInt(n[1] + 1);
                                        break;
                                    } else {

                                        q = 1;
                                        break;
                                    }
                                }
                            }

                            if (q > 0) {
                                memnamearr[i - 1] = memnamearr[i - 1] + "_" + (q);
                            }

                        }


                    }


                    for (int i = 0; i < tabRowCount - 1; i++) {
                        PbReturnObject pbro = new PbReturnObject();
                        int nextval = 0;
                        String denorQuery = "SELECT DISTINCT " + keyarrname[i] + "," + keyValuearrname[i] + " FROM " + dbtabName;
                        String query = "";
                        if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                            query = "insert into prg_qry_dim_member ( MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,MEMBER_DESC,MEMBER_ORDER_BY,ISNULLvALUE) values('" + memnamearr[i] + "'," + dimId + "," + dimtabId + ",'Y'," + dbtabId + ",'" + denorQuery + "','" + descarr[i] + "',"+orderByColArr[i]+",'"+isnullValueArr[i]+"')";

                        } else {
                            pbro = pbdb.execSelectSQL("select PRG_QRY_DIM_MEMBER_SEQ.nextval from dual");
                            nextval = pbro.getFieldValueInt(0, "NEXTVAL");
                            query = "insert into prg_qry_dim_member (MEMBER_ID, MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,MEMBER_DESC,MEMBER_ORDER_BY,ISNULLvALUE) values(" + nextval + ",'" + memnamearr[i] + "'," + dimId + "," + dimtabId + ",'Y'," + dbtabId + ",'" + denorQuery + "','" + descarr[i] + "',"+orderByColArr[i]+",'"+isnullValueArr[i]+"')";

                        }
                        //   pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ( ident_current('PRG_QRY_DIM_MEMBER')," + keyValue + ",'VALUE')");



                        pbdb.execModifySQL(query);
                        //colSt = con.createStatement();
                        String querydet = "";
                        if (ProgenConnection.SQL_SERVER.equals(dbType)) {
                            querydet = "insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values (ident_current('PRG_QRY_DIM_MEMBER')," + keyarr[i] + ",'KEY')";

                        } else if(ProgenConnection.MYSQL.equals(dbType)) {
                        querydet = "insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ((select LAST_INSERT_ID(MEMBER_ID) FROM PRG_QRY_DIM_MEMBER ORDER BY 1 DESC LIMIT 1)," + keyarr[i] + ",'KEY')";

                        }
                        else {
                            querydet = "insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + keyarr[i] + ",'KEY')";

                        }

                        pbdb.execModifySQL(querydet);
                        if (allarr[i] == null) {
                            if (ProgenConnection.SQL_SERVER.equals(dbType)) {
                                pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_ID, COL_ID, COL_TYPE,DEFAULT_VAL) values (ident_current('PRG_QRY_DIM_MEMBER')," + keyValuearr[i] + ",'VALUE','" + defaultarr[i] + "')");

                            } else if(ProgenConnection.MYSQL.equals(dbType)) {
                        pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_ID, COL_ID, COL_TYPE,DEFAULT_VAL) values ((select LAST_INSERT_ID(MEMBER_ID) FROM PRG_QRY_DIM_MEMBER ORDER BY 1 DESC LIMIT 1)," + keyValuearr[i] + ",'VALUE','" + defaultarr[i] + "')");

                        }
                                else {
                               pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + keyValuearr[i] + ",'VALUE')");

                            }
                        } else {
                            if (ProgenConnection.SQL_SERVER.equals(dbType)) {
                                pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values (ident_current('PRG_QRY_DIM_MEMBER')," + keyValuearr[i] + ",'VALUE')");
                                } else if(ProgenConnection.MYSQL.equals(dbType)) {
                        pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ((select LAST_INSERT_ID(MEMBER_ID) FROM PRG_QRY_DIM_MEMBER ORDER BY 1 DESC LIMIT 1)," + keyValuearr[i] + ",'VALUE')");

                            } else {
                                pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + keyValuearr[i] + ",'VALUE')");

                            }
                        }


                    }

                }




            } catch (Exception e) {
                e.printStackTrace();
            }
//response.sendRedirect(request.getContextPath()+"/getAllDimensions.do");
%>

<script>
    <%
if(flag!=null){
    if(check!=null)
                {
%>
   // parent.refreshparentmem1();
    parent.createHierarchy2(<%=dimId%>);
    <%} else {
    %>
    parent.createHierarchy1(<%=dimId%>);
     <%  }
}
            else
                {
            %>
    parent.refreshparentmem1();
                <%}%>
</script>
