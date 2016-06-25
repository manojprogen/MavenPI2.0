<%-- 
    Document   : PortalsDownload
    Created on : Nov 11, 2011, 3:02:24 PM
    Author     : progen
--%>
<%@page import="prg.db.PbDb,prg.db.PbReturnObject,java.util.List,com.progen.portal.Portal,com.progen.portal.PortLet,java.util.Iterator,com.progen.portal.PortalPdfGenerator,com.google.common.collect.Iterables"%>

<% PbDb pbdb = new PbDb();
String headerTitle=null;
  String headerQry = "select SETUP_CHAR_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='COMPANY_NAME'" ;
                PbReturnObject headerObj = pbdb.execSelectSQL(headerQry);
                int rowCount=headerObj.getRowCount();
                if(rowCount!=0)
                     headerTitle = headerObj.getFieldValueString(0, 0);
                else
                    headerTitle = "Progen Business Solutions";
                PortalPdfGenerator pdf = new PortalPdfGenerator();
                 pdf.setHeaderTitle(headerTitle);
                 pdf.setReportName("Pdf Report");
                 pdf.setFileName("downloadPDF.pdf");
                 pdf.setResponse(response);
                 pdf.setRequest(request);
                 pdf.portalPDF();
          


                %>

