<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="com.progen.Createtable.CreatetableDAO"%>
<%@page import="com.lowagie.text.Document"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.progen.report.NewProductNameHelper"%>
<%@page import="com.progen.datasnapshots.DataSnapshotGenerator"%>
<%@page import="prg.util.OfficeGenerator"%>
<%@page import="prg.util.PbXMLGenerator"%>
<%@page import="prg.util.PbCDGenerator"%>
<%@page import="prg.util.pbTSGenerator"%>
<%@page import="prg.util.PbCSVGenerator"%>
<%@page import="prg.util.PbHtmlGenerator"%>
<%@page import="prg.db.Container"%> 
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="java.util.HashMap" %>
<%@page import="prg.util.PbExcelDriver"%>
<%@page import="prg.util.PbExcelGenerator"%>
<%@page import="prg.util.PbPDFDriver"%>
<%@page import="java.util.ArrayList" %>
<%@page import="com.progen.report.charts.PbGraphDisplay"%>
<%@page import="com.progen.servlet.ServletUtilities" %>
<%@page import="java.io.FileInputStream" %>
<%@page import="java.io.ByteArrayOutputStream" %>

<%@page import="prg.db.PbDb"%>
<%


            try {

                String dlType = request.getParameter("dType");
                //if(dlType == null){
                  //  dlType=session.getAttribute("dlType").toString();
                    //}


                if (dlType != null) {
                    Container container = null;
                    ArrayList timeDetailsArray = null;
                    String tabName = null;
                    String headerTitle = null;
                    PbDb pbdb = new PbDb();
                    String headerQry = "select SETUP_CHAR_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='COMPANY_NAME'";
                    PbReturnObject headerObj = pbdb.execSelectSQL(headerQry);
                    int rowCount = headerObj.getRowCount();
                    if (rowCount != 0) {
                        headerTitle = headerObj.getFieldValueString(0, 0);
                    } else {
                        headerTitle = "Progen Business Solutions";
                    }
                    String reportName = "";
                    String displayType = "ReportAndGraph";//Report for Table,Graph for Graph only and ReportAndGraph for Table and Graph
                    String[] grpPaths = null;
                    ArrayList grpDetails = null;
                    String expRec = "All";// by default we export all records
                    HashMap ColorCodeMap = null;
                    HashMap TableHashMap = null;
                    String userId = String.valueOf(session.getAttribute("USERID"));

                    HashMap map = null;
                    tabName = request.getParameter("tabId");
                    //dlType = request.getParameter("dType");
                    displayType = request.getParameter("displayType");
                    expRec = request.getParameter("expRec");
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if(dlType.equals("kpidownload")) {
                        String FileName = "";
                         FileName = (String)session.getAttribute("KpiName");
                                                  
                            ServletUtilities.downloadFile(FileName, response, "application/xls");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                    }
                   else if (dlType.equals("oneviewHtml")) {
                        String FileName = "";
                         FileName = (String)session.getAttribute("OneviewName");
                            ServletUtilities.downloadFile(FileName, response, "application/html");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                    }
                   else if (dlType.equals("oneviewPdf")) {
                        String FileName = "";
                         FileName = (String)session.getAttribute("OneviewName");
                             ServletUtilities.downloadFile(FileName, response, "application/pdf");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                    }
                    else if (dlType.equals("targetMeasure")) {
                        String FileName = "";
                         FileName = (String)session.getAttribute("targetMeasureExcelFile");
                            ServletUtilities.downloadFile(FileName, response, "application/xls");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                    }
                    else if (dlType.equals("exceldown")) {
                        //String exce=request.getParameter("dType");
                        String connId = request.getParameter("connid");
                        String tableName = request.getParameter("tablelistid");

                        String FileName = "";
                        CreatetableDAO crtdao = new CreatetableDAO();

                        FileName = crtdao.saveExcelDetails(tableName, connId);
                        ServletUtilities.downloadFile(FileName, response, "application/xls");
                        ServletUtilities.markFileForDeletion(FileName, request.getSession(false));

                    } else if (dlType.equals("excel") || dlType.equals("html")) {
                        String htmlType = request.getParameter("dType");
                        String goalName = request.getParameter("goalName");

                        ArrayList<String> measures = new ArrayList<String>();
                        //measures.add((String) container.getDisplayLabels().get(0));
                        ArrayList<String> viewByValues = new ArrayList<String>();
                        ArrayList<String> currentValues = new ArrayList<String>();
                        ArrayList<String> priorValues = new ArrayList<String>();
                        ArrayList<String> changedValues = new ArrayList<String>();
                        ArrayList<String> goalPerVal = new ArrayList<String>();
                        ArrayList<String> goalChangeVal = new ArrayList<String>();
                        ArrayList<String> htmltypGoalName = new ArrayList<String>();
                        htmltypGoalName.add(goalName);
                        htmltypGoalName.add(htmlType);
                        String measureValues[] = request.getParameterValues("vieByname");
                        measures.addAll(Arrays.asList(measureValues));
                        String[] viewBys = request.getParameterValues("viewBys");
                        viewByValues.addAll(Arrays.asList(viewBys));
                        String[] currval = request.getParameterValues("currvalues");
                        currentValues.addAll(Arrays.asList(currval));
                        String[] priorVals = request.getParameterValues("measureValues");
                        priorValues.addAll(Arrays.asList(priorVals));
                        String[] changedVal = request.getParameterValues("changedVals");
                        changedValues.addAll(Arrays.asList(changedVal));
                        String[] goalChanged = request.getParameterValues("formperent");
                        goalPerVal.addAll(Arrays.asList(goalChanged));
                        String[] goalChangedValues = request.getParameterValues("goalTimeIndividual");
                        if (goalChangedValues != null) {
                            goalChangeVal.addAll(Arrays.asList(goalChangedValues));
                        }

                        NewProductNameHelper newProdData = new NewProductNameHelper();

                        newProdData.setGoalNameType(htmltypGoalName);
                        newProdData.setMeasures(measures);
                        newProdData.setViewByNameValue(viewByValues);
                        newProdData.setCurrentValue(currentValues);
                        newProdData.setPriorValue(priorValues);
                        newProdData.setChangedpercent(changedValues);
                        newProdData.setGoalChangePernt(goalPerVal);
                        newProdData.setGoalChangeValue(goalChangeVal);


                        String FileName = "";
                        //String[] mailids=userstextarea.split(",");


                        DataSnapshotGenerator dataSnapshotGenrtr = new DataSnapshotGenerator();

                        if (htmlType.equals("html")) {
                            //completeContent.append(dataSnapshotGenrtr.generateHtmlForMail(container, userId, "fromHtml"));
                            FileName = dataSnapshotGenrtr.generateNewProductHtml(newProdData, "html");
                            ServletUtilities.downloadFile(FileName, response, "application/html");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                        } else if (htmlType.equals("excel")) {
                            FileName = dataSnapshotGenrtr.generateNewProductExcel(newProdData, "xls");
                            ServletUtilities.downloadFile(FileName, response, "application/xls");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                        }
                    }else if(dlType.equals("queryexcel")) {
                        String FileName = request.getParameter("data");
                            ServletUtilities.downloadFile(FileName, response, "application/xls");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                     }else if(dlType.equals("pdf")) {
                        String FileName = request.getParameter("data");
                            ServletUtilities.downloadFile(FileName, response, "application/pdf");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                   }else if(dlType.equals("QueryHTML")) {
                        String FileName = request.getParameter("data");
                            ServletUtilities.downloadFile(FileName, response, "application/html");
                            ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                    }
                    else if (dlType.equals("MeasDrill")) {
                        String tableId = request.getParameter("tableId");
                        String reportId = request.getParameter("REPORTID");
                        String dimName = request.getParameter("dimName");
                        container = Container.getContainerFromSession(request, reportId);
                        ReportTemplateDAO reportdao = new ReportTemplateDAO();
                        PbReturnObject retObj1 = null;
                        ArrayList alist = null;

                        alist = reportdao.viewTableData(tableId, container, dimName);


                        //retObj2 = (PbReturnObject) alist.get(0);
                        retObj1 = (PbReturnObject) alist.get(0);

                        String[] tableColumnNames = retObj1.getColumnNames();

                        DataSnapshotGenerator dsnapgntr = new DataSnapshotGenerator();
                        String filename = dsnapgntr.measureDrill(tableColumnNames, reportId, retObj1);
                        ServletUtilities.downloadFile(filename, response, "application/xls");
                        // ServletUtilities.markFileForDeletion(filename, request.getSession(false));

                    } else {
                        if (map != null) {
                           container = (Container) map.get(tabName);
                            if (container != null) {
                                TableHashMap = container.getTableHashMap();
                                ColorCodeMap = (HashMap) TableHashMap.get("ColorCodeMap");
                                if (container.getReportName() != null) {
                                    reportName = container.getReportName();
                                    reportName = reportName.trim().replace(" ", "_");
                                     reportName = reportName.replaceAll("\\s", "");
                                }
                                timeDetailsArray = container.getTimeDetailsArray();
                                if (dlType != null) {



//                            ArrayList disCols = container.getDisplayLabels();
//                            ArrayList dTypes = container.getDataTypes();
                       //Start of code by Manik for hidding measures during export
                            ArrayList<String> disCols = (ArrayList<String>)container.getDisplayLabels().clone();
                            ArrayList<String> dTypes = (ArrayList<String>)container.getDataTypes().clone();
                            ArrayList<String> cols = (ArrayList<String>)container.getDisplayColumns().clone();
                                    //ArrayList<String> disCols = container.getDisplayLabels();
                                    //ArrayList<String> dTypes = container.getDataTypes();
                                    //ArrayList<String> cols = container.gettempdisplaycol();
                            ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
                            for (Object hiddenCol : hiddenCols) {
                                int index = cols.indexOf("A_" + hiddenCol.toString());
                                if (index != -1) {
                                    cols.remove(index);
                                    disCols.remove(index);
                                    dTypes.remove(index);
                                }
                            }
                        //End of code by Manik for hidding measures during export
                                    int fromRow = container.getFromRow();
                                    int toRow = container.getRetObj().getViewSequence().size();
                                    if (expRec != null && expRec.equalsIgnoreCase("Current")) {
                                        int noOfDispRows = fromRow + Integer.parseInt(container.getPagesPerSlide());
                                        if (noOfDispRows < toRow) {
                                            toRow = noOfDispRows;
                                        }
                                    }

                                    int counter = 0;
                                    String[] columns = null;
                                    String[] disColumns = null;
                                    String[] types = null;

                                    if (cols.contains("CBX")) {
                                        columns = new String[cols.size() - 1];
                                        disColumns = new String[cols.size() - 1];
                                        types = new String[cols.size() - 1];
                                    } else {
                                        columns = new String[cols.size()];
                                        disColumns = new String[cols.size()];
                                        types = new String[cols.size()];
                                    }

                                    for (int i = 0; i < cols.size(); i++) {
                                        if (!"CBX".equals(String.valueOf(cols.get(i)))) {
                                            columns[counter] = String.valueOf(cols.get(i));
                                            disColumns[counter] = String.valueOf(disCols.get(i));
                                            types[counter] = String.valueOf(dTypes.get(i));
                                            counter++;
                                        }
                                    }
                                    if (dlType.equals("E")) {
                                      ArrayList filterValues = new ArrayList();
                                        PbExcelGenerator driver1 = new PbExcelGenerator();//
                                        String viewbyvalues = "";
                                        String[] values = new String[container.getReportCollect().paramValueList.size()];
                                        if (container.getReportCollect().paramValueList != null) {
                                            for (int k = 0; k < container.getReportCollect().paramValueList.size(); k++) {
                                                values = container.getReportCollect().paramValueList.get(k).toString().split(":");

                                                viewbyvalues = values[1];

                                                if (!viewbyvalues.contains("[All]")) {
                                                    String value = (String) container.getReportCollect().paramValueList.get(k);
                                                    if (!filterValues.contains(value)) {
                                                        filterValues.add(value);
                                                    }
                                                }
                                            }
                                        }
                                        String paramType = request.getParameter("paramType");
                                        driver1.setColorCodeMap(ColorCodeMap);
                                        driver1.setFilterValues(filterValues);
                                        driver1.setParamType(paramType);
                                        driver1.setTimeDetailsArray(timeDetailsArray);
                                        driver1.setResponse(response);
                                        driver1.setTypes(types);
                                        driver1.setDisplayColumns(columns);
                                        driver1.setDisplayLabels(disColumns);
                                        driver1.setDisplayType(displayType);
                                        driver1.setRequest(request);
                                        driver1.setFilePaths(container.getImgPaths());
                                        driver1.setContainer(container);
                                        driver1.setFromRow(fromRow);
                                        driver1.setToRow(toRow);
                                        driver1.setLogoPath(this.getServletContext().getRealPath("/").replace("\\.\\", "\\") + "images\\pi_logo.gif");
                                        driver1.setRet(container.getRetObj());
                                        driver1.setHeaderTitle(headerTitle);
                                        driver1.setColorGroup(container.getColorGroup());
                                        driver1.setRepParameter(container.getReportParameter());
                                        driver1.setSortColumns(container.getSortColumns());
                                        driver1.setSortTypes(container.getSortTypes());
                                        driver1.setSortDataTypes(container.getSortDataTypes());
                                        if (reportName != null && !"".equalsIgnoreCase(reportName)) {
                                           reportName = reportName.replaceAll("\\s", "");

                                            driver1.setReportName(reportName);
                                            driver1.setFileName(reportName + ".xls");
                                            driver1.createExcel();
                                        } else {
                                            driver1.setReportName("Excel Report");
                                            driver1.setFileName("downloadExcel.xls");
                                            driver1.createExcel();
                                        }
                                    } else if (dlType.equals("P")) {
                                        PbPDFDriver pdf = new PbPDFDriver();
                                         ArrayList filterValues = new ArrayList();
                                        String viewbyvalues = "";
                                        String[] values = new String[container.getReportCollect().paramValueList.size()];
                                        if (container.getReportCollect().paramValueList != null) {
                                            for (int k = 0; k < container.getReportCollect().paramValueList.size(); k++) {
                                                values = container.getReportCollect().paramValueList.get(k).toString().split(":");

                                                viewbyvalues = values[1];

                                               if (!viewbyvalues.contains("[All]")) {
                                                    String value = (String) container.getReportCollect().paramValueList.get(k);
                                                    if (!filterValues.contains(value)) {
                                                        filterValues.add(value);
                                                    }
                                                }
                                            }
                                        }
                                        String paramType = request.getParameter("paramType");
                                        String pdfTypeSelect = request.getParameter("pdfTypeSelect");
                                        String pdfCellHeight = request.getParameter("pdfCellHeight");
                                        String pdfCellFont = request.getParameter("pdfCellFont");

                                        pdf.setColorCodeMap(ColorCodeMap);
                                        pdf.setCellHeight(Float.parseFloat(pdfCellHeight));
                                        if(pdfCellFont!=null){
                                        pdf.font.setSize(Float.parseFloat(pdfCellFont));
                                        }
                                        pdf.setParamType(paramType);
                                        pdf.setPdfTypeSelect(pdfTypeSelect);
                                        pdf.setTimeDetailsArray(timeDetailsArray);
                                        pdf.setFilterValues(filterValues);
                                        pdf.setResponse(response);
                                        pdf.setTypes(types);
                                        pdf.setDisplayColumns(columns);
                                        pdf.setDisplayLabels(disColumns);
                                        pdf.setDisplayType(displayType);
                                        pdf.setColorGroup(container.getColorGroup());
                                        pdf.setRepParameter(container.getReportParameter());
                                        pdf.setRequest(request);
                                        pdf.setFilePaths(container.getImgPaths());
                                        pdf.setLogoPath(this.getServletContext().getRealPath("/").replace("\\.\\", "\\") + "images\\pi_logo.gif");
                                        pdf.setHeaderTitle(headerTitle);
                                        pdf.setFromRow(fromRow);
                                        pdf.setToRow(toRow);
                                        pdf.setContainer(container);
                                        pdf.setRet(container.getRetObj());

                                        if (reportName != null && !"".equalsIgnoreCase(reportName)) {
                                           reportName = reportName.replaceAll("\\s", "");
                                            pdf.setReportName(reportName);
                                            pdf.setFileName(reportName + ".pdf");
                                            pdf.createPDF();
                                        } else {
                                            pdf.setReportName("Pdf Report");
                                            pdf.setFileName("downloadPDF.pdf");
                                            pdf.createPDF();
                                        }
                                        /*PDFCreator creator = new PDFCreator();
                                        String fileName = creator.createPurchaseOrderPDF("", userId, "/home/arun/Desktop/progen.gif", container);
                                        String tempDirName = System.getProperty("java.io.tmpdir");
                                        FileInputStream fis = new FileInputStream(tempDirName+"/"+fileName);
                                        byte[] bytes = new byte[fis.available()];
                                        while (-1 != fis.read(bytes)) {}

                                        ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
                                        bos.write(bytes);

                                        response.setContentType("application/pdf");
                                        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                                        response.setContentLength(bos.size());

                                        ServletOutputStream outputstream = response.getOutputStream();
                                        bos.writeTo(outputstream);
                                        outputstream.flush();
                                         */
                                    } else if (dlType.equals("H")) {
                                        PbHtmlGenerator htmlGen = new PbHtmlGenerator(response, session);
                                        String paramType = request.getParameter("paramType");
                                        //
                                        String htmlCellHeight = request.getParameter("htmlCellHeight");
                                        //
                                        htmlGen.setFromRow(fromRow);
                                        htmlGen.setToRow(toRow);
                                        htmlGen.setParamType(paramType);
                                        htmlGen.setHtmlCellHeight(htmlCellHeight);
                                        htmlGen.setFromFlag("fromHtml");
                                        htmlGen.createHTMLFile(container, userId);
                                    } else if (dlType.equals("OfficeExcel")) {
                                        OfficeGenerator officeGen = new OfficeGenerator(response, session);
                                        officeGen.setFromRow(fromRow);
                                        officeGen.setToRow(toRow);
                                        officeGen.createExcelFile(container, userId);
                                    } else if (dlType.equals("CSV") || dlType.equals("TS") || dlType.equals("CD") || dlType.equals("CSN")) {
                                        /*DelimiterSeparator delmSeparator=new DelimiterSeparator(response);
                                        delmSeparator.setTimeDetailsArray(timeDetailsArray);
                                        delmSeparator.setResponse(response);
                                        delmSeparator.setTypes(types);
                                        delmSeparator.setDisplayColumns(columns);
                                        delmSeparator.setDisplayLabels(disColumns);
                                        delmSeparator.setRequest(request);
                                        delmSeparator.setLogoPath(this.getServletContext().getRealPath("/").replace("\\.\\", "\\") + "images\\pi_logo.gif");
                                        delmSeparator.setHeaderTitle(headerTitle);
                                        if (expRec != null && expRec.equalsIgnoreCase("Current")) {
                                        delmSeparator.setReturnObject(container.getDisplayedSetRetObj());
                                        } else {
                                        delmSeparator.setReturnObject(container.getRetObj());
                                        }*/

                                        if (dlType.equals("CSV")) {
                                            PbCSVGenerator CSVGen = new PbCSVGenerator(response, session);
                                            CSVGen.setFromRow(fromRow);
                                            CSVGen.setToRow(toRow);
                                            CSVGen.createCSVFile(container, userId, dlType);


                                        }
                                        if (dlType.equals("CSN")) {
                                            PbCSVGenerator CSVGen = new PbCSVGenerator(response, session);
                                            CSVGen.setFromRow(fromRow);
                                            CSVGen.setToRow(toRow);
                                            CSVGen.createCSVFile(container, userId, dlType);


                                        } else if (dlType.equals("TS")) {
                                            pbTSGenerator TSGen = new pbTSGenerator(response, session);
                                            TSGen.setFromRow(fromRow);
                                            TSGen.setToRow(toRow);
                                            TSGen.createTSFile(container, userId);

                                        } else if (dlType.equals("CD")) {
                                            PbCDGenerator CDGen = new PbCDGenerator(response, session);
                                            String delimiter = request.getParameter("dlimiter");
                                            String textIdentifier = request.getParameter("txtIdentifier");
                                            CDGen.setDelimiter(delimiter);
                                            CDGen.setTextIdentifier(textIdentifier);
                                            CDGen.setFromRow(fromRow);
                                            CDGen.setToRow(toRow);
                                            CDGen.createCDFile(container, userId);
                                        }
                                    } else if (dlType.equals("X")) {
                                        PbXMLGenerator XMLGen = new PbXMLGenerator(response, session);
                                        XMLGen.setFromRow(fromRow);
                                        XMLGen.setToRow(toRow);
                                        XMLGen.createXMLFile(container, userId);
                                    }

                                }
                            }
                        }
                    }

                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
%>
