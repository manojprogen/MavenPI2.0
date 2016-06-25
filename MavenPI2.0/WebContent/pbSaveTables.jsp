
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.business.group.BusinessGroupDAO,java.util.*,utils.db.*,java.sql.*,prg.db.PbReturnObject,prg.db.PbDb,java.util.ArrayList,com.progen.connection.ConnectionMetadata,com.progen.connection.ConnectionDAO,prg.test.TableHelper,com.google.common.collect.Iterables"%>
<%--<%@page import="org.apache.poi.hssf.record.formula.functions.T"%>--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String dbType = null;
            if (session != null && session.getAttribute("MetadataDbType") != null) {
                dbType = (String) session.getAttribute("MetadataDbType");
            }
            // Connection metaDataConn = null;
             Connection dataConn = null;
             PbReturnObject retObj = null;
            // Statement st = null;
            //  ResultSet rs = null;
            PbReturnObject rs = new PbReturnObject();
            //.println("session.getAttribute(mysqlTables)" + session.getAttribute("mysqlTables"));
            PbDb pbDb = new PbDb();
            try {
                // Statement colSt = null;
                //ResultSet colRs = null;
                ArrayList queryList = new ArrayList();
                PbReturnObject colRs = new PbReturnObject();
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("hi in start");
//String type=session.getAttribute("tabType").toString();
                //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tables --" + request.getParameter("tables"));
                String tables = request.getParameter("tables");
                String tabNames[] = tables.split("~");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("COUNT IS--->" + tabNames.length);
                String activeConnection = request.getParameter("activeConnection");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("active connection" + activeConnection);


                // Class.forName("oracle.jdbc.driver.OracleDriver");

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connection==" + con);

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("statement =" + st);
                String sql = "select * from prg_user_connections where CONNECTION_ID=" + activeConnection;
                //
                rs = pbDb.execSelectSQL(sql);
                PbReturnObject rsObjectTab = new PbReturnObject();
                String sql2="select table_name from prg_db_master_table where connection_id="+ activeConnection;
                rsObjectTab=pbDb.execSelectSQL(sql2);

                int sourceId = 0;
                String owner = "";
                for (int rsRow = 0; rsRow < rs.getRowCount(); rsRow++) {
                    owner = rs.getFieldValueString(rsRow, 2);
                    sourceId = rs.getFieldValueInt(rsRow, 0);
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("source Id=" + sourceId);
                }
                String[] name= new String[tabNames.length];
                String types1[] = new String[tabNames.length];
                for (int i = 0; i < tabNames.length; i++) {
                    String s[] = tabNames[i].split(",");
                    name[i] = s[0];
                    types1[i] = s[1];
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("name==" + names[i] + "type===" + type[i]);
                }
                List<String> names = new ArrayList<String>(Arrays.asList(name));
                List<String> type = new ArrayList<String>(Arrays.asList(types1));
                //
                names.remove("PR_DAY_INFO");
                int prDayDen=0;
                for(int i=0;i<rsObjectTab.getRowCount();i++){
                    if(rsObjectTab.getFieldValueString(i, 0).equalsIgnoreCase("PR_DAY_DENOM") && prDayDen==0){
                        type.remove(names.indexOf("PR_DAY_DENOM"));
                        names.remove("PR_DAY_DENOM");
                        prDayDen=1;
                        break;
                    }
//                    if(rsObjectTab.getFieldValueString(i, 0).equalsIgnoreCase("PR_DAY_INFO")){
//                        type.remove(names.indexOf("PR_DAY_INFO"));
//                        names.remove("PR_DAY_INFO");
//                    }
                    }

                 //
                String finalQuery="";

                //adding time dimension to Dimensions
                finalQuery = "select DIMENSION_NAME from PRG_QRY_DIMENSIONS where CONNECTION_ID="+activeConnection;
                boolean flag = false;
                retObj = pbDb.execSelectSQL(finalQuery);
                for(int i=0;i<retObj.rowCount;i++){
                    if(retObj.getFieldValueString(i,0).equals("Time")){
                        flag = true;
                        }
                    }
//here check for prg_day_denom if it is not there add it in names and typer

                //Connection con1 =  utils.db.ProgenConnection.getInstance().getConnection();
                //   Statement st1 = metaDataConn.createStatement();
                 HashMap tablecols=null;
                if(session.getAttribute("mysqlTables")!=null)
                  tablecols = (HashMap) session.getAttribute("mysqlTables");
                // 

                for (int i = 0; i < names.size(); i++) {
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("index is "+i);

                    String query="";
                    if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                        query = "insert into prg_db_master_table (CONNECTION_ID,TABLE_TYPE,TABLE_NAME,TABLE_ALIAS,USER_SCHEMA) values(" + sourceId + ",'" + type.get(i).toString() + "','" + names.get(i).toString() + "','" + names.get(i).toString() + "','" + names.get(i).toString() + "')";

                       } else  {
                        query = "insert into prg_db_master_table (TABLE_ID,CONNECTION_ID,TABLE_TYPE,TABLE_NAME,TABLE_ALIAS,USER_SCHEMA) values(prg_database_master_seq.nextval," + sourceId + ",'" + type.get(i).toString() + "','" + names.get(i).toString() + "','" + names.get(i).toString() + "','" + names.get(i).toString() + "')";
                    }
                    pbDb.execModifySQL(query);


                    


                    if (session.getAttribute("mysqlTables") != null) {
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in my sql condition");
                        HashMap hmap = new HashMap();
                        ArrayList tbles = new ArrayList();

                       
                      //    TableHelper tableHelper=new TableHelper();
                        //  tableHelper.setRequest(request);
                        // tableHelper=tableHelper.getColumnValues(names.get(i).toString());
                                 // (TableHelper)tablecols.get(names[i].toString());
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("HASHMAP IS " + tablecols);
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------------------------------------------------");
                          
                         ArrayList<String> types=new ArrayList();
                        ArrayList<String> columns = new ArrayList();
                        ArrayList lengths=new ArrayList();
                        PbReturnObject retObj1=new PbReturnObject();
                        Connection ScCon=ProgenConnection.getInstance().getConnectionByConId(rs.getFieldValueString(0, 0).toString());

                        if(ProgenConnection.MYSQL.equals(dbType) || ScCon.toString().contains("mysql")){
                            if(names.get(i).toString().equalsIgnoreCase("PR_DAY_DENOM") || names.get(i).toString().equalsIgnoreCase("PR_DAY_INFO")){
                                retObj1=pbDb.execSelectSQL("SHOW FIELDS FROM "+names.get(i).toString());
                            }else{
                                Connection con=ProgenConnection.getInstance().getConnectionByConId(rs.getFieldValueString(0, 0).toString());
                                Statement st = con.createStatement();
                                retObj1=pbDb.execSelectSQL("SHOW FIELDS FROM "+rs.getFieldValueString(0, 12)+"."+names.get(i).toString(), con);
                            }

                            //
                            for(int n=0;n<retObj1.rowCount;n++){
                                String tempType=retObj1.getFieldValueString(n, 1).toString();
                                
                                String type1;
                                String length1;
                                if(tempType.equalsIgnoreCase("date") || tempType.equalsIgnoreCase("datetime") || tempType.equalsIgnoreCase("time") || tempType.equalsIgnoreCase("tinytext") || tempType.equalsIgnoreCase("blob") || tempType.equalsIgnoreCase("longblob") || tempType.equalsIgnoreCase("text") || tempType.equalsIgnoreCase("mediumtext") || tempType.equalsIgnoreCase("longtext")){
                                 type1=tempType;
                                 length1="0";
                                } else if(tempType.equalsIgnoreCase("double") || tempType.equalsIgnoreCase("float") && !tempType.contains("(")){
                                    type1=tempType;
                                    length1="0";
                                } else {
                                type1=tempType.substring(0,tempType.indexOf("("));
                                if(type1.equalsIgnoreCase("decimal") || type1.equalsIgnoreCase("float") || tempType.equalsIgnoreCase("double") || tempType.equalsIgnoreCase("tinyint") || tempType.equalsIgnoreCase("bigint")){
                                length1=tempType.substring(tempType.indexOf("(")+1,tempType.indexOf(","));
                                }else{
                                length1=tempType.substring(tempType.indexOf("(")+1,tempType.indexOf(")"));
                                }
                                }
                                //
                                String column=retObj1.getFieldValueString(n, 0).toString();
                            types.add(n, type1);
                            columns.add(n,column);
                          //  lengths.add(n,Integer.parseInt(length1));
                            lengths.add(n,length1);
                            }
                        }else{
                             TableHelper tableHelper=new TableHelper();
                          tableHelper.setRequest(request);
                         tableHelper=tableHelper.getColumnValues(names.get(i).toString());
                         
                        types = tableHelper.getColumnTypes();
                        Set<String> columnNames=new HashSet<String>();
//changed by nazneen
                        columnNames = tableHelper.getColumnNames();
                        String str = columnNames.toString();
                        str = str.replace("[", "").replace("]", "");

                        String colNames[] = str.split(",");
                        for(int k=0 ; k < colNames.length ; k++){
                            columns.add(k,colNames[k]);
                        }
                       // columns =(ArrayList)tableHelper.getColumnNames();
                        lengths = tableHelper.getColumnSizes();
                        }

                       int count=0;

                        for (int j=0;j<columns.size();j++) {
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("The Datatype is----------------------------" + types.get(count));

                            String k = "";
//VARCHAR, DATE, INT, VARCHAR, VARCHAR, VARCHAR, BIT, INT, VARCHAR, VARCHAR

                            if(types!=null && types.get(count)!=null){
                            if (types.get(count).toString().equalsIgnoreCase("decimal") || types.get(count).toString().equalsIgnoreCase("INT") || types.get(count).toString().equalsIgnoreCase("BIT") || types.get(count).toString().equalsIgnoreCase("bigint") || types.get(count).toString().equalsIgnoreCase("FLOAT")) {
                                k = "NUMBER";
                                //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("The value if it is a decimal is"+k);
                            } else if (types.get(count).toString().equalsIgnoreCase("timestamp") || types.get(count).toString().equalsIgnoreCase("DATE") || types.get(count).toString().equalsIgnoreCase("datetime")) {
                                k = "DATE";
                                //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("The value if it is a timestamp is"+k);
                            } else {
                                k = "VARCHAR";
                                //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("The value if it is a varchar is"+k);
                            }}else{
                             k = "VARCHAR";

                            }

                               String len = lengths.get(count).toString();


                            //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("The datatypeis======================= " + types.toString());
                            String COLQUERY = "";
                            if (ProgenConnection.MYSQL.equals(dbType)) {
                            COLQUERY = "insert into prg_db_master_table_details (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE,COL_LENGTH,IS_PRIMARY_KEY,IS_ACTIVE) values ((select max(table_id) from prg_db_master_table),'" + columns.get(count).toString() + "','" + columns.get(count).toString() + "','" + k + "'," + lengths.get(count).toString() + ",'N','Y')";
                            queryList.add("insert into prg_db_master_table_details (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE,COL_LENGTH,IS_PRIMARY_KEY,IS_ACTIVE) values ((select max(table_id) from prg_db_master_table),'" + columns.get(count).toString() + "','" + columns.get(count).toString() + "','" + k + "'," + lengths.get(count).toString() + ",'N','Y')");
                            }
                            else
                                {
                                COLQUERY = "insert into prg_db_master_table_details (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE,COL_LENGTH,IS_PRIMARY_KEY,IS_ACTIVE) values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,PRG_DATABASE_MASTER_SEQ.currval,'" + columns.get(count).toString() + "','" + columns.get(count).toString() + "','" + k + "'," + lengths.get(count).toString() + ",'N','Y')";
                            queryList.add("insert into prg_db_master_table_details (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE,COL_LENGTH,IS_PRIMARY_KEY,IS_ACTIVE) values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,PRG_DATABASE_MASTER_SEQ.currval,'" + columns.get(count).toString() + "','" + columns.get(count).toString() + "','" + k + "'," + lengths.get(count).toString() + ",'N','Y')");
                            }
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Added Query IS-->" + COLQUERY);
                            pbDb.execModifySQL(COLQUERY);

                       count++;
                        }
//                        pbDb.executeMultiple(queryList);
                        if (i == (names.size() - 1)) {
                            session.removeAttribute("hm");
                            session.removeAttribute("sqlTables");
                            session.removeAttribute("mysqlTables");
                        }
                    } else if (session.getAttribute("hm") != null)//Adding Columns Of Excel Sheet
                    {
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("In Else Part"+names[i]);
                        HashMap details = (HashMap) session.getAttribute("hm");

                        ArrayList a = (ArrayList) details.get(names.get(i).toString());
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("ArrayList is--->" + a);
                        String str = "";
                        for (int f = 0; f < a.size(); f++) {
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("a.getf " + a.get(f));
                            String str1 = "";
                            if (ProgenConnection.MYSQL.equals(dbType)) {
                             str1 = "insert into prg_db_master_table_details (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,IS_PRIMARY_KEY,IS_ACTIVE) values (LAST_INSERT_ID(table_id) from prg_db_master_table ORDER BY 1 DESC LIMIT 1,'" + a.get(f) + "','" + a.get(f) + "','N','Y')";

                            }
                            else {
                            str1 = "insert into prg_db_master_table_details (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,IS_PRIMARY_KEY,IS_ACTIVE) values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,prg_database_master_seq.currval,'" + a.get(f) + "','" + a.get(f) + "','N','Y')";
                            }
                            //
                            pbDb.execModifySQL(str1);

                        }
                        if (i == (names.size()- 1)) {
                            session.removeAttribute("hm");
                        }
                    } else if (session.getAttribute("hm") == null) {
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("In If Part");
                        String columnQuery = "";
                        ConnectionDAO connectionDAO = new ConnectionDAO();
                        ConnectionMetadata connectionMetadata = connectionDAO.getConnectionByConId(activeConnection);
                        if (connectionMetadata.getDbType().equals("SqlServer")) {
                            columnQuery = " select column_name,data_type,character_maximum_length from information_schema.columns where table_name = '" + names.get(i).toString() + "'";
                        } else if (connectionMetadata.getDbType().equals("PostgreSQL")) {
                            columnQuery = "SELECT a.attname as Column,pg_catalog.format_type(a.atttypid, a.atttypmod) as DATA_TYPE  FROM "
                                    + " pg_catalog.pg_attribute a  WHERE a.attnum > 0  AND NOT a.attisdropped  AND a.attrelid = ("
                                    + " SELECT c.oid  FROM pg_catalog.pg_class c LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace  WHERE c.relname ~ '^(" + names.get(i).toString() + ")$'"
                                    + " AND pg_catalog.pg_table_is_visible(c.oid) )";
                        } else if (connectionMetadata.getDbType().equals("MYSQL")) {
                            columnQuery = "SELECT DISTINCT column_name,data_type, data_length from all_tab_cols where table_name='" + names.get(i).toString() + "' and owner=upper('" + owner + "')";
                        } else {
                            columnQuery = "SELECT DISTINCT column_name,data_type, data_length from all_tab_cols where table_name='" + names.get(i).toString() + "' and owner=upper('" + owner + "')";
                        }


                        dataConn = ProgenConnection.getInstance().getConnectionByConId(activeConnection);

                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("new con"+newcon);
                        //colSt = dataConn.createStatement();

                        //Statement clost1 = metaDataConn.createStatement();
                        ArrayList queryList1 = new ArrayList();
                        // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("columnQuery is "+columnQuery);
                        colRs = pbDb.execSelectSQL(columnQuery, dataConn);
                        dataConn = null;
                        String columnName = "";
                        String dataType = "";
                        String colmLenth = "";

                        for (int row = 0; row < colRs.getRowCount(); row++) {
                            if (colRs.getFieldValueString(row, 0) != "") {
                               columnName = colRs.getFieldValueString(row, 0);
                            } else {
                                columnName = "null";
                            }
                            if (colRs.getFieldValueString(row, 1) != "") {
                                dataType = colRs.getFieldValueString(row, 1);
                            } else {
                                dataType = "null";
                            }
                            if( colRs.getFieldValueString(row, 2)!="")
                                colmLenth=colRs.getFieldValueString(row, 2);
                            else
                                  colmLenth="null";
                            if (ProgenConnection.SQL_SERVER.equals(dbType)) {
                                queryList1.add("insert into prg_db_master_table_details (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE,COL_LENGTH,IS_PRIMARY_KEY,IS_ACTIVE) values (ident_current('PRG_DB_MASTER_TABLE'),'" + columnName + "','" + columnName + "','" + dataType + "'," + colmLenth + ",'N','Y')");

                                 } else if(ProgenConnection.MYSQL.equals(dbType)){
                                     queryList1.add("insert into prg_db_master_table_details (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE,COL_LENGTH,IS_PRIMARY_KEY,IS_ACTIVE) values ((select last_insert_id(table_id) from PRG_DB_MASTER_TABLE order by 1 desc limit 1),'" + columnName + "','" + columnName + "','" + dataType + "'," + colmLenth + ",'N','Y')");
                                 }else{
                                queryList1.add("insert into prg_db_master_table_details (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE,COL_LENGTH,IS_PRIMARY_KEY,IS_ACTIVE) values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,prg_database_master_seq.currval,'" + colRs.getFieldValueString(row, 0) + "','" + colRs.getFieldValueString(row, 0) + "','" + colRs.getFieldValueString(row, "DATA_TYPE") + "','" + colRs.getFieldValueString(row, "DATA_LENGTH") + "','N','Y')");

                            }

                        }

                        for(int k=0;k<queryList1.size();k++){
                            if(queryList1.get(k).toString()!=null){
                        pbDb.execUpdateSQL(queryList1.get(k).toString());
                         }
                        }


                    }
                }

                      if (flag == false) {
                                          if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {

                                              finalQuery = "insert into prg_qry_dimensions(DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID,CONNECTION_ID) values('Time','Time','Y',null," + activeConnection + ")";
                                          } else {
                                              finalQuery = "insert into prg_qry_dimensions(DIMENSION_ID,DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID,CONNECTION_ID) values(PRG_QRY_DIMENSIONS_SEQ.nextval,'Time','Time','Y',null," + activeConnection + ")";
                                          }
                                          pbDb.execInsert(finalQuery);
                                          String dimension_id = "";
                                          finalQuery = "select DIMENSION_ID from prg_qry_dimensions where CONNECTION_ID =" + activeConnection+" and DIMENSION_NAME = 'Time' ";
                                          retObj = pbDb.execSelectSQL(finalQuery);
                                          if (retObj.getRowCount() == 1) {
                                              dimension_id = retObj.getFieldValueString(0, 0);

                                          }

                                          String table_id = "";
                                          finalQuery = "select TABLE_ID from prg_db_master_table where CONNECTION_ID=" + activeConnection + " and TABLE_NAME ='PR_DAY_DENOM'";
                                          retObj = pbDb.execSelectSQL(finalQuery);
                                          ArrayList queryList1=new ArrayList();
                                          if (retObj.getRowCount() == 1) {
                                              table_id = retObj.getFieldValueString(0,0);
                                              if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                                                   finalQuery = "insert into prg_qry_dim_tables (DIM_ID,TAB_ID) values('" + dimension_id + "','" + table_id + "')";
                                                 }else
                                              {
                                              finalQuery = "insert into prg_qry_dim_tables (DIM_TAB_ID,DIM_ID,TAB_ID) values(PRG_QRY_DIM_TABLES_SEQ.nextVal," + dimension_id + "," + table_id + ")";

                                          }
                                          queryList1.add(finalQuery);
                                              }

                                          //pbDb.execModifySQL(finalQuery);

                                          if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                                              finalQuery = "insert into PRG_QRY_DIM_TAB_DETAILS (DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) ";
                                          finalQuery = finalQuery+"select (select LAST_INSERT_ID(DIM_TAB_ID) from PRG_QRY_DIM_TABLES ORDER BY 1 DESC LIMIT 1),dd.COLUMN_ID ,'Y','N' from prg_db_master_table dt,prg_db_master_table_details dd where dt.connection_id = " + activeConnection + " and dt.table_name = 'PR_DAY_DENOM' and dt.table_id = dd.table_id";
                                           } else
                                              {
                                          finalQuery = "insert into PRG_QRY_DIM_TAB_DETAILS (DIM_TAB_COL_ID,DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) ";
                                          finalQuery = finalQuery+"select PRG_QRY_DIM_TAB_DETAILS_SEQ.nextval,PRG_QRY_DIM_TABLES_SEQ.CURRVAL,dd.COLUMN_ID ,'Y','N' from prg_db_master_table dt,prg_db_master_table_details dd where dt.connection_id = " + activeConnection + " and dt.table_name = 'PR_DAY_DENOM' and dt.table_id = dd.table_id";

                                               }
                                          queryList1.add(finalQuery);
                                          pbDb.executeMultiple(queryList1);
                                         // retObj = pbDb.execSelectSQL(finalQuery);
                                          //int i = retObj.getRowCount();
                                      }


                // metaDataConn.close();

                //  metaDataConn = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dataConn != null) {
                    dataConn.close();
                }

            }

%>
<script>
    parent.cancelTableList2();
</script>
