<%@ page import="prg.db.PbReturnObject" %>
<%@ page import="java.util.*" %>

<jsp:useBean id="Container" class="prg.db.Container" scope="session" />

<%    //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            //Temporary static data
            String cols[] = new String[]{"COL1", "COL2", "COL3", "COL4", "COL5"};
            PbReturnObject ret = new PbReturnObject();
            ret.setColumnNames(cols);

            String val1 = "", val2 = "", val3 = "";
            int vVal = 5;
            Calendar cal = Calendar.getInstance();
            Date d1 = cal.getTime();
            Date d2 = cal.getTime();
            d2.setDate(20);
            for (int i = 0; i < 500; i++) {
                val1 = "" + (i + 1);
                val2 = "VAL" + i + "2";
                val3 = "VAL" + i + "3";
                ret.setFieldValueString("COL1", val1);
                ret.setFieldValueString("COL2", val2);
                ret.setFieldValueString("COL3", val3);
                ret.setFieldValueString("COL4", vVal + "");
                if (i % 2 == 0) {
                    ret.setFieldValue("COL5", d1);
                } else {
                    ret.setFieldValue("COL5", d2);
                }
                ret.addRow();

                vVal = (vVal + 10 + i) * -1;
            }
// End of building temp ret obj


            Container.refresh();
            ArrayList displayColumns = new ArrayList();
            ArrayList dataTypes = new ArrayList();
            ArrayList displayTypes = new ArrayList();
            //ArrayList alignments = new ArrayList();
            ArrayList signs = new ArrayList();
            //----------------------------------
            displayColumns.add("CBX");
            displayColumns.add("COL1");
            displayColumns.add("COL2");
            displayColumns.add("COL3");
            displayColumns.add("COL4");
            displayColumns.add("COL5");
            //----------------------------------
            dataTypes.add("CBX");
            dataTypes.add("N");
            dataTypes.add("C");
            dataTypes.add("C");
            dataTypes.add("N");
            dataTypes.add("D");
            //----------------------------------
            displayTypes.add("CBX"); //checkbox
            displayTypes.add("T"); //TD display
            displayTypes.add("T"); //TD display
            displayTypes.add("H"); //Hyperlink
            displayTypes.add("T"); //Hyperlink
            displayTypes.add("T"); //Hyperlink
            //--------Optional-----------------
            //alignments.add("CENTER");
            //alignments.add("LEFT");
            //alignments.add("LEFT");
            //alignments.add("LEFT");
            //alignments.add("RIGHT");
            //alignments.add("CENTER");
            //----------------------------------
            signs.add("COL4");
            //----------------------------------

            Container.setDefaultPagesPerSlide("10");
            Container.setCheckBoxValueCol("COL1");
            Container.setDisplayColumns(displayColumns);
            Container.setDataTypes(dataTypes);
            Container.setDisplayTypes(displayTypes);
            Container.setSearchReq(true);
            Container.setNetTotalReq(true);
            Container.setGrandTotalReq(true);
            Container.setAvgTotalReq(true);
            Container.setCatAvgTotalReq(true);
            //Container.setAlignments(alignments);
            Container.setSigns(signs);
            Container.setRetObj(null);

            Container.setCurrentPage(1);
            Container.setRetObj(ret);
            Container.setSelected(new ArrayList());

%>

<%@ include file="pbTableMap.jsp" %>


