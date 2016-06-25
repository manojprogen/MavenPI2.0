<%-- 
    Document   : pbGetStickyNotes
    Created on : Aug 29, 2009, 4:11:50 PM
    Author     : Administrator
--%>

<%@page import="java.util.Set,java.util.HashMap,java.util.ArrayList,java.util.List,java.io.ByteArrayInputStream,org.jdom.input.SAXBuilder,org.jdom.Element,org.jdom.Document,java.sql.*,prg.db.PbReturnObject,utils.db.ProgenConnection"%>


<%
            String userId = String.valueOf(session.getAttribute("USERID"));
            String ReportId1 = String.valueOf(request.getAttribute("REPORTID"));
            String compURL = String.valueOf(request.getAttribute("currentURL"));
            String urldets[] = compURL.split(";");
            String defkey = "";
            String defvalue = "";
            String nextValidate = "";

            // String timeArraystr1 = request.getParameter("timeArraystr");
            ArrayList timeArraystr1 = new ArrayList();
            if (timeArraystr1 == null) {
                timeArraystr1 = timeArray;
            }
            //   String timeArraystr2[] = timeArraystr1.split(",");
            HashMap ParamUrlHashMap = new HashMap();
            for (int i = 2; i < urldets.length; i++) {
                if (urldets[i].startsWith("CBOARP")) {
                    defkey = urldets[i].split("=")[0];
                    defkey = defkey.substring(6);
                    defvalue = urldets[i].split("=")[1];
                }
                if (urldets[i].startsWith("CBOVIEW_BY")) {
                    defkey = urldets[i].split("=")[0];
                    defkey = defkey.substring(10);
                    defvalue = urldets[i].split("=")[1];
                }
                ParamUrlHashMap.put(defkey, defvalue);
            }
            SAXBuilder builder = new SAXBuilder();
            Document document = null;
            Element root = null;
            String query = "SELECT * FROM PRG_USER_STICKYNOTE " +
                    "WHERE USER_ID = '" + userId + "'" +
                    " AND REPORT_ID = '" + ReportId1 + "'";

            PbReturnObject stickyRetObj = null;
            int stickyNoteCount = 0;
            ArrayList listcount = new ArrayList();
            String noteInitial = "ENTER YOUR NOTES HERE";
            try {
                
                Connection stickyCon = ProgenConnection.getInstance().getConnection();
                Statement StickySt = stickyCon.createStatement();
                ResultSet StickyRs = StickySt.executeQuery(query);
                stickyRetObj = new PbReturnObject(StickyRs);
                String DateValue = "";
                HashMap paramValueMap = new HashMap();
                String PeriodValue = "";
                String CompareValue = "";
                String elemenValue = "";
                String elemenId = "";
                String viewValue = "";
                String viewId = "";
                for (int r = 0; r < stickyRetObj.getRowCount(); r++) {
                    if (stickyRetObj.getFieldValueClobString(r, "PARAM_XML") != null && stickyRetObj.getFieldValueClobString(r, "PARAM_XML") != "") {
                       // //.println("clobvalue-----" + stickyRetObj.getFieldValueClobString(r, "PARAM_XML"));
                        document = builder.build(new ByteArrayInputStream(stickyRetObj.getFieldValueClobString(r, "PARAM_XML").getBytes()));
                        root = document.getRootElement();
                        List row = root.getChildren("TimeDimensions");
                        for (int p = 0; p < row.size(); p++) {
                            Element TimeDimensions = (Element) row.get(p);
                            List asOfDate = TimeDimensions.getChildren("AsOfDate");
                            for (int j = 0; j < asOfDate.size(); j++) {
                                Element asOfDateValue = (Element) asOfDate.get(j);
                                DateValue = asOfDateValue.getText();
                            }
                            List periodType = TimeDimensions.getChildren("PeriodType");
                            for (int j = 0; j < periodType.size(); j++) {
                                Element periodTypeValue = (Element) periodType.get(j);
                                PeriodValue = periodTypeValue.getText();
                            }
                          /*  List compareType = TimeDimensions.getChildren("CompareWith");
                            for (int j = 0; j < compareType.size(); j++) {
                                Element compareWithValue = (Element) compareType.get(j);
                                CompareValue = compareWithValue.getText();
                            } */
                        }
                        if (timeArray.contains(DateValue)) {
                            if (timeArray.contains(PeriodValue)) {
                               // if (timeArray.contains(CompareValue)) {
                                    nextValidate = "Success";
                            //    }
                            }
                        } else {
                            nextValidate = "Fail";
                        }
                        if (nextValidate.equalsIgnoreCase("Success")) {
                            List rowParam = root.getChildren("ParamSection");
                            for (int i = 0; i < rowParam.size(); i++) {
                                Element ParamSection = (Element) rowParam.get(i);
                                List ElementId = ParamSection.getChildren("ElementId");
                                for (int j = 0; j < ElementId.size(); j++) {
                                    Element eleId = (Element) ElementId.get(j);
                                    elemenId = eleId.getText();
                                    List ElementValue = ParamSection.getChildren("ElementValue");
                                    Element eleValue = (Element) ElementValue.get(j);
                                    elemenValue = eleValue.getText();
                                  //  //////////////.println("elemenId" + elemenId + "elemenValue---" + elemenValue);
                                    paramValueMap.put(elemenId, elemenValue);
                                }
                                List ViewById = ParamSection.getChildren("ViewById");
                                List ViewByValue = ParamSection.getChildren("ViewByValue");
                                for (int j = 0; j < ViewById.size(); j++) {
                                    Element viewById = (Element) ViewById.get(j);
                                    viewId = viewById.getText();
                                    Element viewByValue = (Element) ViewByValue.get(j);
                                    viewValue = viewByValue.getText();
                                    paramValueMap.put(viewId, viewValue);
                                }
                            }
                            Set paramKeys = paramValueMap.keySet();
                            Object paramKeysarr[] = paramKeys.toArray();
                            Set paramUrlKeys = ParamUrlHashMap.keySet();
                            Object paramUrlKeyarr[] = paramUrlKeys.toArray();
                            String urlParamValue = "";
                            String xmlParamValue = "";
                            int count = 0;
                            for (int k = 0; k < paramKeysarr.length; k++) {
                                urlParamValue = String.valueOf(ParamUrlHashMap.get(paramUrlKeyarr[k]));
                                xmlParamValue = String.valueOf(paramValueMap.get(paramKeysarr[k]));
                                if (urlParamValue.equalsIgnoreCase(xmlParamValue)) {
                                    count++;
                                }
                            }

                            if (count == paramKeysarr.length) {
                                listcount.add(r);

                            } else {
                               // //.println("Sticky Note Not Available");
                            }
                        }
                    }
                }
                stickyNoteCount = stickyRetObj.getRowCount();
                stickyCon.close();

            } catch (Exception e) {
                stickyRetObj = new PbReturnObject();
                e.printStackTrace();
            }
%>

