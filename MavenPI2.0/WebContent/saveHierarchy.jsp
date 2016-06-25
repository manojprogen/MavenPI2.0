<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,utils.db.*,java.sql.*,prg.db.PbDb,prg.db.PbReturnObject"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String dbType = null;
            if (session != null && session.getAttribute("MetadataDbType") != null) {
                dbType = (String) session.getAttribute("MetadataDbType");
            }
            PbDb pbDb=new PbDb();
            PbReturnObject pbReturnObject=null;
            try {
              
                
                String defaultHirearchyStatus = "Y";
                int hierarchyId = 0;
                String check = request.getParameter("check");
                PbReturnObject pbro1=new PbReturnObject();

                String hieName = request.getParameter("hieName");
                String hieDesc = request.getParameter("hieDesc");
                String dimId = request.getParameter("dimId");
                 String namecheck[]=null;
                  String idcheck[]=null;
                if(check!=null)
                    {
                        String sql1 = "SELECT REL_ID, REL_NAME FROM PRG_QRY_DIM_REL where dim_id=" + dimId ;
                          pbro1=new PbReturnObject();
                         pbro1= pbDb.execSelectSQL(sql1);
                         if(pbro1.getRowCount()>0){
                             namecheck=new String[pbro1.getRowCount()];
                             idcheck=new String[pbro1.getRowCount()];
               for(int i=0;i<pbro1.getRowCount();i++){
                   
                   idcheck[i]=pbro1.getFieldValueString(i,0);
                   namecheck[i]=pbro1.getFieldValueString(i,1);
               }
              }
                    }
                int rowcount = Integer.parseInt(request.getParameter("rowcount"));
                String levels[] = new String[rowcount];
                String members[] = new String[rowcount];
                for (int i = 0; i < rowcount; i++) {
                    String val = "val" + (i + 1);
                    String mem = "memv" + (i + 1);
                    levels[i] = request.getParameter(val);
                    members[i] = request.getParameter(mem);

                }


               
               

                if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                } else {
                    pbReturnObject = pbDb.execSelectSQL("select PRG_QRY_DIM_REL_SEQ.nextval from dual");
                  
                    hierarchyId = pbReturnObject.getFieldValueInt(0,0);
//Getting hierarchyId//
               

                }



//Checking This Dimension already has a Default_Hierarchy//


                String updateQuery = "";
                if (request.getParameter("chk") == null) {
                    defaultHirearchyStatus = "N";
                }

                if (defaultHirearchyStatus.equals("Y")) {
                    updateQuery = "update PRG_QRY_DIM_REL set IS_DEFAULT='N' where dim_id=" + dimId;
             
                    pbDb.execModifySQL(updateQuery);
                }

               String query ="";
                if(check!=null)
                   {
                    for (int j = 0; j < idcheck.length ; j++) {
                   for (int i = 0; i < rowcount; i++) {
                    PbReturnObject pbro3=pbDb.execSelectSQL("select MEM_ID from PRG_QRY_DIM_REL_DETAILS where REL_ID=" + idcheck[j]);
                    int flag=0;
                    for(int k=0;k<pbro3.getRowCount();k++){
                         //if(pbro3.getFieldValueString(k,0)==members[i])
                           if(members[i].equals(pbro3.getFieldValueString(k,0)))

                              flag=1;
                         }
                    if(flag==0){
                    query = "insert into PRG_QRY_DIM_REL_DETAILS (REL_ID, MEM_ID, REL_LEVEL) values (" + idcheck[j] + "," + members[i] + "," + levels[i] + ")";
                    
                     pbDb.execModifySQL(query);
                     }
                }
                   }
                    }
               else{

                if (ProgenConnection.SQL_SERVER.equals(dbType)||ProgenConnection.MYSQL.equals(dbType)) {
                   query = "insert into PRG_QRY_DIM_REL (DIM_ID, DESCRIPTION, IS_DEFAULT, REL_NAME) values('" + dimId + "','" + hieDesc + "','" + defaultHirearchyStatus + "','" + hieName + "')";

                } else {
                     query = "insert into PRG_QRY_DIM_REL (REL_ID, DIM_ID, DESCRIPTION, IS_DEFAULT, REL_NAME) values(" + hierarchyId + ",'" + dimId + "','" + hieDesc + "','" + defaultHirearchyStatus + "','" + hieName + "')";

                }

               //.println("query\t"+query);
                pbDb.execModifySQL(query);

//For Inserting into Default Hierarchy///
                if (defaultHirearchyStatus.equals("Y")) {
                 
                     if (ProgenConnection.SQL_SERVER.equals(dbType)){
                    pbDb.execModifySQL("update prg_qry_dimensions set default_hierarchy_id=ident_current('PRG_QRY_DIM_REL')  where dimension_id=" + dimId);
                     //.println(" update \t update prg_qry_dimensions set default_hierarchy_id=ident_current('PRG_QRY_DIM_REL')  where dimension_id="+dimId);
                 }else if(ProgenConnection.MYSQL.equals(dbType)){
                     pbDb.execModifySQL("update prg_qry_dimensions set default_hierarchy_id=(select LAST_INSERT_ID(REL_ID) FROM PRG_QRY_DIM_REL ORDER BY 1 DESC LIMIT 1)  where dimension_id=" + dimId);
                 }
                     else {
                      pbDb.execModifySQL("update prg_qry_dimensions set default_hierarchy_id=" + hierarchyId + " where dimension_id=" + dimId);
                    }}

                for (int i = 0; i < rowcount; i++) {
                 
                     if (ProgenConnection.SQL_SERVER.equals(dbType)){
                    pbDb.execModifySQL("insert into PRG_QRY_DIM_REL_DETAILS (REL_ID, MEM_ID, REL_LEVEL) values ( ident_current('PRG_QRY_DIM_REL')," + members[i] + "," + levels[i] + ")");
                 //.println("insert into PRG_QRY_DIM_REL_DETAILS (REL_ID, MEM_ID, REL_LEVEL) values ( ident_current('PRG_QRY_DIM_REL')," + members[i] + "," + levels[i] + ")");
                   }else if(ProgenConnection.MYSQL.equals(dbType)){
                     pbDb.execModifySQL("insert into PRG_QRY_DIM_REL_DETAILS (REL_ID, MEM_ID, REL_LEVEL) values ( (select LAST_INSERT_ID(REL_ID) FROM PRG_QRY_DIM_REL ORDER BY 1 DESC LIMIT 1)," + members[i] + "," + levels[i] + ")");

                    }else{
                       pbDb.execModifySQL("insert into PRG_QRY_DIM_REL_DETAILS (REL_ID, MEM_ID, REL_LEVEL) values (" + hierarchyId + "," + members[i] + "," + levels[i] + ")");
                }}
                 }
            } catch (Exception e) {
                e.printStackTrace();
            }
//response.sendRedirect(request.getContextPath()+"/getAllDimensions.do");
%>

<script>
    parent.refreshparenthie1();
</script>