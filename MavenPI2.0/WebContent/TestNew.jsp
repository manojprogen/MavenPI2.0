<%-- 
    Document   : TestNew
    Created on : Jan 12, 2010, 8:09:41 PM
    Author     : Administrator
--%>

<%--
    Document   : pbRowSpan
    Created on : Jan 9, 2010, 8:01:02 PM
    Author     : Administrator
--%>

<%@page import="prg.db.PbReturnObject, java.util.*" %>


<%!
    public String getTabData(PbReturnObject myret,
            ArrayList keyFig,
            ArrayList disCols) // add from and to
    {
        String colName = null;
        String tmpStr = null;
        String temp = null;
        StringBuffer finalBuffer = new StringBuffer();
        HashMap oldVals = new HashMap();
        HashMap spans = new HashMap();

        for (int i = 0; i < keyFig.size(); i++) {
            oldVals.put(keyFig.get(i), "NOVAL");
            spans.put(keyFig.get(i), "1");
        }

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
            if (Integer.parseInt(spans.get(temp).toString()) != 1) {
                tmpStr = tmpStr.replaceAll(temp + "¥", String.valueOf(spans.get(temp)));
                //tmpStr="<tr></tr>";
            } else {
                tmpStr = tmpStr.replaceAll(temp + "¥", String.valueOf(spans.get(temp)));
            }

        }

        return tmpStr;
    }

    public String tranposeTable(PbReturnObject myret, ArrayList keyFig, ArrayList disCols) {
        StringBuffer finalBuffer = new StringBuffer();

        finalBuffer.append("<Tr>");
        for (int j = 0; j < disCols.size(); j++) {
            for (int i = 0; i < myret.getRowCount() + 1; i++) {
            }
        }
        finalBuffer.append("</Tr>");

        return "";
    }

%>

<%
            String[] cols = new String[]{"COUNTRY", "REGION", "STATE", "VAL"};
            PbReturnObject ret = new PbReturnObject();
            ret.setColumnNames(cols);

            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "100");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "100");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "50");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "60");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "AP");
            ret.setFieldValue("VAL", "300");
            ret.addRow();

            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "300");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "200");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "500");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "60");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "INDIA");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "UP");
            ret.setFieldValue("VAL", "300");
            ret.addRow();

            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "350");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "400");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "SOUTH");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "330");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "600");
            ret.addRow();
            ret.setFieldValue("COUNTRY", "PAKISTAN");
            ret.setFieldValue("REGION", "NORTH");
            ret.setFieldValue("STATE", "NP");
            ret.setFieldValue("VAL", "30");
            ret.addRow();

            ArrayList keyFigure = new ArrayList(); //View by
            keyFigure.add("COUNTRY");
            keyFigure.add("REGION");
            keyFigure.add("STATE");

            ArrayList disCols = new ArrayList();
            disCols.add("COUNTRY");
            disCols.add("REGION");
            disCols.add("STATE");
            disCols.add("VAL");


%>
<html>
    <head>
        <script>

            function transposeTable(id) {
                var table = document.getElementById(id);
               
                //var tbody = table.tBodies[0];
                var rows = table.rows;
                var numRows = rows.length;
                var numCells = rows[0].cells.length;
                var i, j, k;

                for(var r=0; r<numRows; r++) {
                    var currentRow = rows[r];

                    for (var c=r+1; c<numCells; c++) {

                        // skip diagonal cells
                        if (c != r) {

                            // Get the current (upper) cell to swap
                            var currentCell = currentRow.cells[c];
                            currentCell.colspan=currentCell.rowSpan;
                            currentCell.rowSpan=1;

                            // Get the swap (lower) cell
                            var swapRow = rows[c];
                            var swapCell = swapRow && swapRow.cells[r]


                            // If both exist, swap them
                            if (currentCell && swapCell) {
                                currentRow.insertBefore(swapCell, currentCell);
                                swapRow.insertBefore(currentCell, swapRow.cells[r]);
                            }

                            // If current exists but not swap, keep adding cells
                            // from current row. Add rows if required
                            else if (currentCell) {
                                k = c;

                                do {

                                    if (!swapRow) {
                                        swapRow = currentRow.cloneNode(false);
                                        table.appendChild(swapRow);
                                    }

                                    swapRow.appendChild(currentRow.cells[k]);
                                    swapRow = rows[swapRow.rowIndex + 1];

                                    // Increment counter to shortcut loop when finished
                                } while (++c < numCells)
                                }
                            }
                        }

                        // If there are more rows than cells, add cells to
                        // appropriate rows
                        if (r > numCells - 1) {

                            // Keep current row index
                            j = r;

                            while (currentRow) {
                                i = 0;

                                // Add cells from lower rows to upper rows
                                do {
                                    rows[i].appendChild(currentRow.cells[0]);
                                } while (++i < numCells)

                                // Remove empty rows along the way
                                    table.removeChild(currentRow);
                                currentRow = rows[j];

                                // Increment counter to shortcut loop when finished
                                r++;
                            }
                        }
                    }
                }
        </script>
    </head>
    <body>

    </body>
</html>
<table align="center" width="60%" border="1" id="tableObje">
    <Tr>
        <th width="25%">Country</th>
        <th width="25%">Region</th>
        <th width="25%">State</th>
        <th width="25%">Price</th>
    </Tr>
    <%
            out.println(getTabData(ret, keyFigure, disCols));
    %>
    <script>
            //transposeTable("tableObje");
    </script>
</table>
