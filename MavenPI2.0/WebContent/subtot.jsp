<%--
    Document   : pbRowSpan
    Created on : Jan 9, 2010, 8:01:02 PM
    Author     : Administrator
--%>

<%@page import="prg.db.PbReturnObject" %>
<%@page import="java.util.ArrayList,java.util.HashMap" %>

<%!
    public void getTabData(PbReturnObject myret, ArrayList keyFig, ArrayList disCols, ArrayList totCols, ArrayList dataTypes, ArrayList dispTypes, JspWriter out) throws Exception // add from and to
    {
        String colName = null;
        String dataType = null;
        String dispType = null;
        String tmpStr = null;
        String temp = null;
        String temp1 = null;
        String temp2 = null;
        String subcol = null;
        boolean subtotals = true;
        StringBuffer finalBuffer = new StringBuffer();
        HashMap oldVals = new HashMap();
        HashMap spans = new HashMap();
        HashMap childs = new HashMap(); //PARENT(key) - CHILD(value)
        HashMap parents = new HashMap();//CHILD(key) - PARENT(value)
        HashMap totals = new HashMap();


        HashMap iterations = new HashMap();
        int msrColStart = keyFig.size() + 1;
        int lastRow = 0;
        for (int i = 0; i < keyFig.size(); i++) {
            oldVals.put(keyFig.get(i), "NOVAL");
            spans.put(keyFig.get(i), "1");

            if (i == 0) {
                temp = String.valueOf(keyFig.get(i));
                parents.put(temp, "null");
            } else {
                childs.put(temp, String.valueOf(keyFig.get(i)));
                parents.put(String.valueOf(keyFig.get(i)), temp);
                temp = String.valueOf(keyFig.get(i));
            }

            for (int j = 0; j < totCols.size(); j++) {
                totals.put(keyFig.get(i) + "" + totCols.get(j), "0");
                //iterations.put(keyFig.get(i)+""+totCols.get(j),"0");
            }
        }
        temp = null;
        int fromRow = 0;
        int toRow = myret.getRowCount();

        for (int i = fromRow; i < toRow; i++) { //use from and to
            subtotals = true;
            finalBuffer.append("<Tr>");
            for (int j = 0; j < disCols.size(); j++) {
                colName = (String) disCols.get(j);
                dataType = (String) dataTypes.get(j);
                dispType = (String) dispTypes.get(j);
                if (keyFig.contains(colName)) {
                    if ((oldVals.get(colName)).equals(myret.getFieldValue(i, colName))) {
                        temp = String.valueOf(spans.get(colName));
                        int ctr = Integer.parseInt(temp);
                        ctr++;
                        spans.put(colName, ctr);
                    } else {

                        if (i > fromRow && subtotals) {
                            // Start appending subtotal row
                            temp1 = null;
                            temp1 = String.valueOf(childs.get(colName));//gets child using the parent key                          
                            while (!(temp1 == null || "null".equals(temp1))) {
                                int loc = disCols.indexOf(temp1);
                                for (int n = loc; n < disCols.size(); n++) {//Display childs subtotal
                                    subcol = String.valueOf(disCols.get(n));
                                    if (n == loc) {
                                        finalBuffer.append("<Td><b>");
                                        finalBuffer.append(myret.getFieldValue(i - 1, subcol) + "  Sub Total ");
                                        finalBuffer.append("</b></Td>");
                                    } else {
                                        if (n >= msrColStart) {
                                            finalBuffer.append("<Td>");
                                            if (totCols.indexOf(subcol) >= 0) {
                                                //int ite = Integer.parseInt(String.valueOf(iterations.get(temp1+""+subcol)));
                                                //if(ite > 1)
                                                finalBuffer.append(totals.get(temp1 + "" + subcol));
                                                totals.put(temp1 + "" + subcol, "0");
                                                //iterations.put(temp1+""+subcol,"0");
                                            }
                                            finalBuffer.append("&nbsp;</Td>");
                                        } else {
                                            finalBuffer.append("<Td>");
                                            finalBuffer.append("&nbsp;</Td>");
                                        }
                                    }
                                } //end of display childs subtotal
                                //Increment span of parents to 1 by its childs
                                temp2 = null;
                                temp2 = String.valueOf(parents.get(temp1));
                                while (!(temp2 == null || "null".equals(temp2))) {
                                    spans.put(temp2, String.valueOf((Integer.parseInt(String.valueOf(spans.get(temp2)))) + 1));
                                    temp2 = String.valueOf(parents.get(temp2));
                                }

                                finalBuffer.append("</Tr>");
                                finalBuffer.append("<Tr>");
                                temp1 = String.valueOf(childs.get(temp1));
                            }
                            //Display subtotal for current col                           
                            for (int n = j; n < disCols.size(); n++) {
                                subcol = String.valueOf(disCols.get(n));
                                if (n == j) {
                                    finalBuffer.append("<Td><b>");
                                    finalBuffer.append(myret.getFieldValue(i - 1, colName) + "  Sub Total");
                                    finalBuffer.append("</b></Td>");
                                } else {
                                    if (n >= msrColStart) {
                                        finalBuffer.append("<Td>");
                                        if (totCols.indexOf(subcol) >= 0) {
                                            //int ite = Integer.parseInt(String.valueOf(iterations.get(colName+""+subcol)));
                                            //if(ite > 1)
                                            finalBuffer.append(totals.get(colName + "" + subcol));
                                            totals.put(colName + "" + subcol, "0");
                                            //iterations.put(colName+""+subcol,"0");
                                        }
                                        finalBuffer.append("&nbsp;</Td>");
                                    } else {
                                        finalBuffer.append("<Td> ");
                                        finalBuffer.append("&nbsp;</Td>");
                                    }
                                }
                            }
                            //Increment span of parents to 1 by its parents
                            temp2 = null;
                            temp2 = String.valueOf(parents.get(colName));
                            while (!(temp2 == null || "null".equals(temp2))) {
                                spans.put(temp2, String.valueOf((Integer.parseInt(String.valueOf(spans.get(temp2)))) + 1));
                                temp2 = String.valueOf(parents.get(temp2));
                            }

                            finalBuffer.append("</Tr>");
                            finalBuffer.append("<Tr>");
                            // End appending subtotal row

                            subtotals = false;
                        }

                        tmpStr = finalBuffer.toString();
                        tmpStr = tmpStr.replaceAll(colName + "¥", String.valueOf(spans.get(colName)));
                        finalBuffer = new StringBuffer();
                        finalBuffer.append(tmpStr);

                        spans.put(colName, "1");
                        oldVals.put(colName, myret.getFieldValue(i, colName));

                        finalBuffer.append("<Td rowspan='");
                        finalBuffer.append(colName + "¥'>");
                        finalBuffer.append(myret.getFieldValueString(i, colName));
                        finalBuffer.append("</Td>");

                        temp1 = null;
                        temp1 = String.valueOf(childs.get(colName));
                        while (!(temp1 == null || "null".equals(temp1))) {
                            oldVals.put(temp1, "NOVAL");
                            temp1 = String.valueOf(childs.get(temp1));
                        }
                        lastRow = i + 1;
                    }
                } else {
                    finalBuffer.append("<Td>");
                    finalBuffer.append(myret.getFieldValueString(i, colName));
                    finalBuffer.append("</Td>");
                }

                //sum up of totals
                if (keyFig.indexOf(colName) >= 0) {
                    for (int k = 0; k < totCols.size(); k++) {
                        temp1 = String.valueOf(totCols.get(k));
                        int sval = Integer.parseInt(String.valueOf(totals.get(colName + "" + temp1)));
                        int nval = Integer.parseInt(myret.getFieldValueString(i, temp1));
                        int fval = sval + nval;
                        totals.put(colName + "" + temp1, fval);

                        //nval = Integer.parseInt(String.valueOf(iterations.get(colName+""+temp1)));
                        //nval = nval + 1;
                        //iterations.put(colName+""+temp1,String.valueOf(nval));
                    }
                }
            }
            finalBuffer.append("</Tr>");
        }



// Begin of process for the last data set
        tmpStr = finalBuffer.toString();
        for (int i = 0; i < keyFig.size(); i++) {
            temp = String.valueOf(keyFig.get(i));
            tmpStr = tmpStr.replaceAll(temp + "¥", String.valueOf(spans.get(temp)));
        }
//######### added by santhosh.kumar
        //out.println(tmpStr);
        //out.print("<script>alert('starts')</script>");
        finalBuffer = new StringBuffer();
        finalBuffer.append(tmpStr);
        int maxTrs = keyFig.size();
        int counter = 0;
        for (int j = 0; j < disCols.size(); j++) {
            colName = String.valueOf(disCols.get(j));
            if (keyFig.indexOf(colName) != -1) {
                ////////////////////////////.println.println("(keyFig.indexOf(colName) != -1) is " + (keyFig.indexOf(colName) != -1));
                counter++;

                if (counter <= maxTrs) {
                    finalBuffer.append("<Tr>");
                    temp1 = null;
                    temp1 = String.valueOf(childs.get(colName));//gets child using the parent key
                    while (!(temp1 == null || "null".equals(temp1))) {
                        int loc = disCols.indexOf(temp1);
                        for (int n = 0; n < disCols.size(); n++) {//Display childs subtotal
                            subcol = String.valueOf(disCols.get(n));
                            if (n == loc) {
                                finalBuffer.append("<Td><b>");
                                finalBuffer.append(myret.getFieldValue(lastRow - 1, subcol) + "  Sub Total ");
                                finalBuffer.append("</b></Td>");
                            } else if (n >= msrColStart) {
                                finalBuffer.append("<Td>");
                                if (totCols.indexOf(subcol) >= 0) {
                                    finalBuffer.append(totals.get(temp1 + "" + subcol));
                                    totals.put(temp1 + "" + subcol, "0");
                                } else {
                                    finalBuffer.append("&nbsp;");
                                }
                                finalBuffer.append("</Td>");
                            } else {
                                finalBuffer.append("<Td>");
                                finalBuffer.append("&nbsp;</Td>");
                            }
                        }
                        temp2 = null;
                        temp2 = String.valueOf(parents.get(temp1));
                        while (!(temp2 == null || "null".equals(temp2))) {
                            spans.put(temp2, String.valueOf((Integer.parseInt(String.valueOf(spans.get(temp2)))) + 1));
                            temp2 = String.valueOf(parents.get(temp2));
                        }
                        finalBuffer.append("</Tr>");
                        counter++;
                        finalBuffer.append("<Tr>");

                        temp1 = String.valueOf(childs.get(temp1));
                    }
                    ////////////////////////////.println.println("Before for loop");
                    for (int n = 0; n < disCols.size(); n++) {
                        subcol = String.valueOf(disCols.get(n));
                        if (n == j) {
                            finalBuffer.append("<Td><b>");
                            finalBuffer.append(myret.getFieldValue(lastRow - 1, colName) + "  Sub Total");
                            finalBuffer.append("</b></Td>");
                        } else if (n >= msrColStart) {
                            finalBuffer.append("<Td>");
                            if (totCols.indexOf(subcol) >= 0) {
                                finalBuffer.append(totals.get(colName + "" + subcol));
                                totals.put(colName + "" + subcol, "0");
                            } else {
                                finalBuffer.append("&nbsp;");
                            }
                            finalBuffer.append("</Td>");
                        } else {
                            finalBuffer.append("<Td> ");
                            finalBuffer.append("&nbsp;</Td>");
                        }
                    }
                    finalBuffer.append("</Tr>");
                    temp2 = null;
                    temp2 = String.valueOf(parents.get(colName));
                    while (!(temp2 == null || "null".equals(temp2))) {
                        spans.put(temp2, String.valueOf((Integer.parseInt(String.valueOf(spans.get(temp2)))) + 1));
                        temp2 = String.valueOf(parents.get(temp2));
                    }
                    subtotals = false;

                    //////////////////////////.println.println("counter is " + counter);
                }
            }
        }
        tmpStr = finalBuffer.toString();
        out.println(tmpStr);
        //return tmpStr;
    }
%>

<%
            //String[] cols = new String[]{"COUNTRY", "REGION", "STATE","CITY", "VAL1", "VAL2"};
            String[] cols = new String[]{"COUNTRY", "REGION", "STATE", "VAL1", "VAL2"};
            PbReturnObject ret = new PbReturnObject();
            ret.setColumnNames(cols);

            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "AP");
            //ret.setFieldValue("CITY", "AP C");
            ret.setFieldValue("VAL1", "100");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "KA");
            //ret.setFieldValue("CITY", "KA C");
            ret.setFieldValue("VAL1", "100");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "TN");
            //ret.setFieldValue("CITY", "TN C");
            ret.setFieldValue("VAL1", "50");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "KR");
            //ret.setFieldValue("CITY", "KR C");
            ret.setFieldValue("VAL1", "60");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "ANN");
            //ret.setFieldValue("CITY", "ANN C");
            ret.setFieldValue("VAL1", "300");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();

            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "UP");
            //ret.setFieldValue("CITY", "UP C");
            ret.setFieldValue("VAL1", "300");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "MP");
            //ret.setFieldValue("CITY", "MP C");
            ret.setFieldValue("VAL1", "200");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "RJ");
            //ret.setFieldValue("CITY", "RJ C");
            ret.setFieldValue("VAL1", "500");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "JK");
            //ret.setFieldValue("CITY", "JK C");
            ret.setFieldValue("VAL1", "60");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "UK");
            //ret.setFieldValue("CITY", "UK C");
            ret.setFieldValue("VAL1", "300");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();

            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "PJ");
            //ret.setFieldValue("CITY", "PJ C");
            ret.setFieldValue("VAL1", "350");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "HY");
            //ret.setFieldValue("CITY", "HY C");
            ret.setFieldValue("VAL1", "400");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "HP");
            //ret.setFieldValue("CITY", "HP C");
            ret.setFieldValue("VAL1", "330");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "NP");
            //ret.setFieldValue("CITY", "NP C");
            ret.setFieldValue("VAL1", "600");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "NP");
            //ret.setFieldValue("CITY", "NP C");
            ret.setFieldValue("VAL1", "30");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();

            ret.setFieldValue("COUNTRY", "AFGHANISTAN");
            ret.setFieldValue("REGION", "EAST");
            ret.setFieldValue("STATE", "KP1");
            //ret.setFieldValue("CITY", "KP C");
            ret.setFieldValue("VAL1", "300");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "AFGHANISTAN");
            ret.setFieldValue("REGION", "EAST");
            ret.setFieldValue("STATE", "KP2");
            //ret.setFieldValue("CITY", "KP C");
            ret.setFieldValue("VAL1", "300");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "AFGHANISTAN");
            ret.setFieldValue("REGION", "WEST");
            ret.setFieldValue("STATE", "KQ1");
            //ret.setFieldValue("CITY", "KP C");
            ret.setFieldValue("VAL1", "300");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "AFGHANISTAN");
            ret.setFieldValue("REGION", "WEST");
            ret.setFieldValue("STATE", "KQ2");
            //ret.setFieldValue("CITY", "KP C");
            ret.setFieldValue("VAL1", "300");
            ret.setFieldValue("VAL2", "111");
            ret.addRow();

            ArrayList keyFigure = new ArrayList(); //View by
            keyFigure.add("COUNTRY");
            keyFigure.add("REGION");
            //keyFigure.add("STATE");

            ArrayList disCols = new ArrayList();
            disCols.add("COUNTRY");
            disCols.add("REGION");
            disCols.add("STATE");
            //disCols.add("CITY");
            disCols.add("VAL1");
            disCols.add("VAL2");

            ArrayList totCols = new ArrayList();
            totCols.add("VAL1");
            totCols.add("VAL2");

            ArrayList dataTypes = new ArrayList(); //View by
            dataTypes.add("C");
            dataTypes.add("C");
            dataTypes.add("C");
            //dataTypes.add("C");
            dataTypes.add("N");
            dataTypes.add("N");

            ArrayList dispTypes = new ArrayList();
            dispTypes.add("H");
            dispTypes.add("T");
            dispTypes.add("T");
            //dispTypes.add("T");
            dispTypes.add("T");
            dispTypes.add("T");
%>
<table align="center" width="60%" border="1">
    <Tr>
        <%-- <th width="17%">Country</th>
         <th width="17%">Region</th>
         <th width="17%">State</th>
         <th width="17%">City</th>
         <th width="17%">Price1</th>
         <th width="17%">Price2</th>--%>
        <th width="20%">Country</th>
        <th width="20%">Region</th>
        <th width="20%">State</th>
        <th width="20%">Price1</th>
        <th width="20%">Price2</th>
    </Tr>
    <%

            //getTabData(ret, keyFigure, disCols, out);
            getTabData(ret, keyFigure, disCols, totCols, dataTypes, dispTypes, out);
    %>
</table>