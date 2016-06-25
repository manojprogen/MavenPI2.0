<%-- 
    Document   : pbUpdateNotes
    Created on : Aug 29, 2009, 4:08:03 PM
    Author     : Administrator
--%>

<%@page import="java.text.SimpleDateFormat,java.text.DateFormat,java.util.Date,java.sql.*,utils.db.ProgenConnection,prg.db.PbReturnObject"%>

<%
            String userId = String.valueOf(session.getAttribute("USERID"));
            String repId = request.getParameter("REPORTID");

            //////////////////////////////////////////////////////////////////////////////////.println.println("userId is "+userId);
            //////////////////////////////////////////////////////////////////////////////////.println.println("repId in pbUpdateNotes is "+repId);

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm");
            Date date = new Date();
            String dispDate = dateFormat.format(date);


            String topp = request.getParameter("topp");
            String leftp = request.getParameter("leftp");
            String widthp = request.getParameter("widthp");
            String heightp = request.getParameter("heightp");
            String snote = request.getParameter("snote");
            String noteId = request.getParameter("noteId");
            String operation = request.getParameter("operation");
            String noteLabel = request.getParameter("noteLabel");
            String parameters = request.getParameter("parameters");
            //////////////.println("parameters----"+parameters);
            String TimeLevel = request.getParameter("TimeLevelstr");
            //////////////.println("---TimeLevel----"+TimeLevel);
            String xmlData = request.getParameter("xmlData");
            String nextId = null;
            PbReturnObject dateObj = null;
            String dateQuery = "";
            String idQuery = "";
            if (noteId != null) {

                String query = null;
               
                Connection con = ProgenConnection.getInstance().getConnection();
                Statement st = con.createStatement();
                if ("D".equals(operation)) {
                    query = "DELETE FROM PRG_USER_STICKYNOTE " +
                            " WHERE USER_ID='" + userId + "'" +
                            " AND REPORT_ID='" + repId + "'" +
                            " AND STICKY_NOTES_ID='" + noteId + "'";
                } else if ("NEWID".equals(noteId)) {
                    if (!(ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER))) {
                        idQuery = "SELECT PRG_USER_STICKYNOTE_SEQ.nextval NEXTID FROM dual";
                    }
                    PbReturnObject ret = null;
                    try {
                        ResultSet rs = st.executeQuery(idQuery);
                        ret = new PbReturnObject(rs);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        dateQuery = "select getdate()";
                    } else {
                        dateQuery = "select sysdate from dual";
                    }
                    ResultSet rs1 = st.executeQuery(dateQuery);
                    dateObj = new PbReturnObject(rs1);
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        query = "INSERT INTO PRG_USER_STICKYNOTE VALUES ('" + userId + "'," +
                                "'" + repId + "'," +
                                leftp + "," +
                                topp + "," +
                                widthp + "," +
                                heightp + "," +
                                "'Y'," +
                                "'" + noteLabel + "'," +
                                "'" + snote + "'," +
                                "'" + parameters + "'," +
                                "'" + dateObj.getFieldValueDateString(0, 0) + "'," +
                                "'" + TimeLevel + "'," +
                                "'" + xmlData + "')";
                        // //.println("query in sql server-----:::" + query);
                    } else {
                        nextId = ret.getFieldValueString(0, "NEXTID");
                        query = "INSERT INTO PRG_USER_STICKYNOTE VALUES ('" + userId + "'," +
                                "'" + repId + "'," +
                                nextId + "," +
                                leftp + "," +
                                topp + "," +
                                widthp + "," +
                                heightp + "," +
                                "'Y'," +
                                "'" + noteLabel + "'," +
                                "'" + snote + "'," +
                                "'" + parameters + "'," +
                                "'" + dateObj.getFieldValueDateString(0, 0) + "'," +
                                "'" + TimeLevel + "'," +
                                "'" + xmlData + "')";
                        // //.println("query-----:::" + query);

                    }
                } else {
                    query = "UPDATE PRG_USER_STICKYNOTE SET " +
                            " S_NOTE='" + snote + "'" +
                            " ,POS_LEFT=" + leftp +
                            " ,POS_TOP=" + topp +
                            " ,POS_WIDTH=" + widthp +
                            " ,POS_HEIGHT=" + heightp +                                                       
                            " WHERE USER_ID='" + userId + "'" +
                            " AND REPORT_ID='" + repId + "'" +
                            " AND PARAMETER_ID='" + parameters + "'" +
                            " AND TIME_LEVEL='" + TimeLevel + "'" +
                            " AND STICKY_NOTES_ID='" + noteId + "'";

                  //  //////////////.println("update query---::" + query);
                }

                try {
                    PbReturnObject ret2 = null;
                    st.executeUpdate(query);
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        String nxtsqlqry = " SELECT IDENT_CURRENT('PRG_USER_STICKYNOTE') ";
                            ResultSet rs2 = st.executeQuery(nxtsqlqry);
                            ret2 = new PbReturnObject(rs2);
                        nextId = ret2.getFieldValueString(0, 0);
                        // //.println("nextID in sql server---" + nextId);
                    }

                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
%>
<script type="text/javascript">
    if(<%=nextId%> != null)
    parent.setNewId("<%=nextId%>");
</script>
