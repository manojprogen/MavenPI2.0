<%--
    Document   : pbRowSpan
    Created on : Jan 9, 2010, 8:01:02 PM
    Author     : Administrator
--%>

<%@page import="prg.db.PbReturnObject, java.util.*" %>

<%!
    public String getTabData(prg.db.PbReturnObject myret,
            java.util.ArrayList keyFig,
            java.util.ArrayList disCols) // add from and to
    {
        String colName = null;
        String tmpStr = null;
        String temp = null;
        String temp1 = null;
        StringBuffer finalBuffer = new StringBuffer();
        java.util.HashMap oldVals = new java.util.HashMap();
        java.util.HashMap spans = new java.util.HashMap();
        java.util.HashMap childs = new java.util.HashMap();

        for (int i = 0; i < keyFig.size(); i++) {
            oldVals.put(keyFig.get(i), "NOVAL");
            spans.put(keyFig.get(i), "1");

            if(i ==0){
                temp = String.valueOf(keyFig.get(i));
            } else {
                childs.put(temp,String.valueOf(keyFig.get(i)));
                temp = String.valueOf(keyFig.get(i));
            }
        }
        temp = null;
////////////////////////////////////////////////.println.println("lksdksdd"+childs);

        for (int i = 0; i < myret.getRowCount(); i++) { //use from and to
            finalBuffer.append("<Tr>");
            for (int j = 0; j < disCols.size(); j++) {
                colName = (String) disCols.get(j);

                if (keyFig.contains(colName)) {
                    if ((oldVals.get(colName)).equals(myret.getFieldValue(i, colName))) {
                        temp = String.valueOf(spans.get(colName));
                        int ctr = Integer.parseInt(temp);
                        ctr++;
                        spans.put(colName, ctr);
                    } else {
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
                        //while(temp1 != null){
                         //   oldVals.remove(temp1);
                         //   temp1 = String.valueOf(childs.get(temp1));
                        //}
                    }
                } else {
                    finalBuffer.append("<Td>");
                    finalBuffer.append(myret.getFieldValueString(i, colName));
                    finalBuffer.append("</Td>");
                }
            }
            finalBuffer.append("</Tr>");
        }

        tmpStr = finalBuffer.toString();
        for (int i = 0; i < keyFig.size(); i++) {
            temp = String.valueOf(keyFig.get(i));
            tmpStr = tmpStr.replaceAll(temp + "¥", String.valueOf(spans.get(temp)));
        }

        return tmpStr;
    }
%>

<%
            String[] cols = new String[]{"COUNTRY", "STATE", "VAL"};
            PbReturnObject ret = new PbReturnObject();
            ret.setColumnNames(cols);

            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "100");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "100");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "50");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "60");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "300");
            ret.addRow();

            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "300");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "200");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "500");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "60");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "300");
            ret.addRow();

            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "350");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "400");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "330");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "600");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "30");
            ret.addRow();

            ArrayList keyFigure = new ArrayList(); //View by
            keyFigure.add("COUNTRY");
            keyFigure.add("STATE");

            ArrayList disCols = new ArrayList();
            disCols.add("COUNTRY");
            disCols.add("STATE");
            disCols.add("VAL");


%>
<table align="center" width="60%" border="1">
    <Tr>
        <th width="33%">Country</th>
        <th width="33%">State</th>
        <th width="33%">Price</th>
    </Tr>
    <%

            //getTabData(ret, keyFigure, disCols, out);
            out.println(getTabData(ret,keyFigure,disCols));
    %>
</table>