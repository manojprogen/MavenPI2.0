/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.Createtable;

//import com.uploadingfile.struts.GlobalParametersDAO;
import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.userlayer.db.UserLayerDAO;
import com.uploadingfile.struts.GlobalParametersDAO;
import com.uploadingfile.struts.StrutsUplaodFile;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.*;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.*;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class CreatetableAction extends LookupDispatchAction {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    public static Logger logger = Logger.getLogger(CreatetableAction.class);

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("createTable", "createTable");
        map.put("alterTableinDB", "alterTableinDB");
        map.put("uploadFile", "uploadFile");
        map.put("saveTableDetails", "saveTableDetails");
        map.put("CheckDSNname", "CheckDSNname");
        map.put("uploadFileUtility", "uploadFileUtility");
        map.put("checkInDB", "checkInDB");
        map.put("truncateTableInDB", "truncateTableInDB");
        map.put("addDatainDb", "addDatainDb");
        map.put("saveGlobalParms", "saveGlobalParms");
        map.put("getExcelDownload", "getExcelDownload");
        map.put("getCustomerTableNames", "getCustomerTableNames");
        map.put("uploadSSISFile", "uploadSSISFile");
        map.put("uploadSSISFileUtility", "uploadSSISFileUtility");
        map.put("uploadExcelData1", "uploadExcelData1");
        map.put("executeManualQuery", "executeManualQuery");
        map.put("downLoadQueryData", "downLoadQueryData");
        map.put("executeMailQuery", "executeMailQuery");
        map.put("uploadExcelFile", "uploadExcelFile");
        map.put("uploadExcelIntoDB", "uploadExcelIntoDB");
        map.put("getAllTables", "getAllTables");
        map.put("CallEtls", "CallEtls");
        map.put("AddExcel", "AddExcel");
        map.put("GetAllSheets", "GetAllSheets");
        map.put("SaveAndUploadXslx", "SaveAndUploadXslx");
        map.put("getLoadDataFromAlreadyInsertedWorkbook", "getLoadDataFromAlreadyInsertedWorkbook");
        map.put("getFixedFullpathFilename", "getFixedFullpathFilename");
        map.put("addExcelData", "addExcelData");
        map.put("GetAllSheetsFromDatabase", "GetAllSheetsFromDatabase");
        map.put("AddHeaderPhotoScheduler", "AddHeaderPhotoScheduler");
        map.put("AddTwoFooterPhotoInScheduler", "AddTwoFooterPhotoInScheduler");
        map.put("CreateHtmlSignatureForScheduler", "CreateHtmlSignatureForScheduler");
        map.put("CreateOptionalHeaderAsHtml", "CreateOptionalHeaderAsHtml");
        map.put("CreateOptionalFooterAsHtml", "CreateOptionalFooterAsHtml");
        map.put("getCustomerHelpPersonNameAndEmailId", "getCustomerHelpPersonNameAndEmailId");
        map.put("newCustomerBugReportMail", "newCustomerBugReportMail");
        map.put("uploadExcelFileTemplate", "uploadExcelFileTemplate");
        map.put("ExecuteAnyProcedure", "ExecuteAnyProcedure");
        return map;
    }

    public ActionForward saveGlobalParms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter Out = response.getWriter();
        ActionForward forward = null;
        GlobalParametersDAO dao = new GlobalParametersDAO();
        //added by Nazneen for logo based on company
        String isCompLogo = session.getAttribute("isCompLogo").toString();
        if (isCompLogo.equalsIgnoreCase("YES")) {
            String companyId = session.getAttribute("companyId").toString();
            dao.isCompLogo = isCompLogo;
            dao.companyId = companyId;
        }
        //end of code by Nazneen for logo based on company
        StrutsUplaodFile fileform = (StrutsUplaodFile) form;
        String datepicker = request.getParameter("dateForReport");
        String sDateformat = fileform.getsDateFormat();
        String sessions = fileform.getSession();
        String Language = request.getParameter("Language");
        FormFile leftLogo = fileform.getLeftLogo();
        FormFile rightLogo = fileform.getRightLogo();
        String footertext = fileform.getFootertext();
        String records = fileform.getRecords();
        String querycache = fileform.getQuerycache();
        String query = fileform.getQuery();
        String dataset = fileform.getDataset();
        String geoenable = fileform.getGeoenable();
        String debug = fileform.getDebug();
        String report = fileform.getReport();
        String duplicateSegmentation = fileform.getDuplicateSegmentation();
        if ((debug != null) && ("yes".equalsIgnoreCase(debug))) {
            session.setAttribute("ISDEBUGENABLE", debug);
        }
        try {
            int i = Integer.parseInt(fileform.getSession());
            session.setMaxInactiveInterval(i);
        } catch (NumberFormatException ne) {
            logger.error("Exception:", ne);
        }
        String filePathOfLeftLogo = null;
        String fileNameOfLeftLogo = null;
        byte[] fileDataOfLeftLogo = leftLogo.getFileData();
        filePathOfLeftLogo = this.getServlet().getServletContext().getRealPath("/") + ("images/") + leftLogo.getFileName();
        fileNameOfLeftLogo = leftLogo.getFileName();
        File tempFileLeft = new File(filePathOfLeftLogo);

        String filePathOfRightLogo = null;
        String fileNameOfRightLogo = null;
        byte[] fileDataOfRightLogo = rightLogo.getFileData();
        filePathOfRightLogo = this.getServlet().getServletContext().getRealPath("/") + ("images/") + rightLogo.getFileName();
        fileNameOfRightLogo = rightLogo.getFileName();
        File tempFileRight = new File(filePathOfRightLogo);

        FileOutputStream fos = null;

        if (tempFileLeft != null) {
            if (!tempFileLeft.exists()) {
                tempFileLeft.createNewFile();
                fos = new FileOutputStream(tempFileLeft);
                fos.write(fileDataOfLeftLogo);
                if (fos != null) {
                    fos.close();
                }
            }
        }
        if (tempFileRight != null) {
            if (!tempFileRight.exists()) {
                tempFileRight.createNewFile();
                fos = new FileOutputStream(tempFileRight);
                fos.write(fileDataOfRightLogo);
                if (fos != null) {
                    fos.close();
                }
            }
        }
        if (query != null) {
            File cacheFolder = new File(query + "cache");
            if (cacheFolder.exists() == false) {
                cacheFolder.mkdir();
            }
            String filePathOfCache = query + cacheFolder.getName();
        }

        ArrayList globalParamsList = new ArrayList();
        globalParamsList.add(0, datepicker);
        globalParamsList.add(1, sDateformat);
        globalParamsList.add(2, sessions);
        globalParamsList.add(3, Language);
        globalParamsList.add(4, leftLogo);
        globalParamsList.add(5, rightLogo);
        globalParamsList.add(6, footertext);
        globalParamsList.add(7, records);
        globalParamsList.add(8, querycache);
        globalParamsList.add(9, query);
        globalParamsList.add(10, dataset);
        globalParamsList.add(11, geoenable);
        globalParamsList.add(12, debug);
        globalParamsList.add(13, report);
        globalParamsList.add(14, duplicateSegmentation);
        dao.saveGlobalParams(globalParamsList);



//        String sDateFormat = dao.getsDateFormat(sDateformat);
//        String Sessions = dao.getSession(sessions);
//        String Records = dao.getRecords(records);
//        String Query = dao.getQuery(query);
//        String dataSet = dao.getDataSet(dataset);

//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
        return mapping.findForward("loginPage");
    }

    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
//        request.getParameter("bbg");
        if (session != null) {
            if (request.getAttribute("requestFrom") != null) {
                String tablename = (String) request.getAttribute("sheetName");
                forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
                ////
            } else {
                if (request.getParameter("requestFrom") != null) {
                    String tablename = (String) request.getParameter("tablename");
                    forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
                    ////

                } else {
                    String tablename = (String) request.getParameter("bbg");
//                    String tablename = (String) request.getAttribute("sheetName");
                    forward = uploadFileUtility(request, form, mapping, "uploadfile", tablename, response);
                    ////
                }

            }
            return forward;
//            return null;
        } else {
            return mapping.findForward("sessionExpired");
//            return null;
        }
    }

//<!--Added by mohit only for Data flow -->
    public ActionForward CallEtls(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter Out = response.getWriter();
        CreatetableDAO createdao = new CreatetableDAO();
        String result = createdao.CallEtls(request);
        Out.print(result);
//        ActionForward forward = null;
        return null;
    }

    public ActionForward createTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        CreatetableDAO createdao = new CreatetableDAO();
        ////
        boolean createTable = Boolean.parseBoolean(request.getParameter("createTable"));

//        boolean isFileExists = Boolean.parseBoolean(request.getParameter("isFileExists"));
//        String filePath = request.getParameter("filePath");
//        String fileName = request.getParameter("fileName");
        String sheetName = request.getParameter("tablename");
        //
//        int sheetNum = Integer.parseInt(request.getParameter("sheetNum"));
//        int nextSheetNum = Integer.parseInt(request.getParameter("nextSheetNum"));
//        String Strcolumnobj = request.getParameter("Strcolumnobj");
//        String Strcoltypeobj = request.getParameter("Strcoltypeobj");
//        String colNames[] = Strcolumnobj.split(",");
//        String colTypes[] = Strcoltypeobj.split(",");
//        ArrayList Alist = null;
        if (createTable == true) {
            return null;// Alist = (createdao.createTable(sheetName, colNames, colTypes, filePath));
        } else {
            String requestFrom = "showExceldetails";
            request.removeAttribute("filePath");
            request.removeAttribute("isFileExists");
            request.removeAttribute("fileName");
            request.removeAttribute("sheetNum");
            request.setAttribute("requestFrom", requestFrom);
            request.setAttribute("sheetName", sheetName);
            ////
            return mapping.findForward("goToUploadFile");
        }

    }

    public ActionForward alterTableinDB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ////
        HttpSession session = request.getSession(false);
        PrintWriter Out = response.getWriter();
        String filePath = request.getParameter("filePath");
        String fileName = request.getParameter("fileName");
        String sheetNumber = request.getParameter("sheetNumber");
        String altercolNames = request.getParameter("altercolNames");
        String renamecolnames = request.getParameter("renamecolnames");
        String sheetName = request.getParameter("sheetname");
        String hashValArray = request.getParameter("hashValArray");
        String kesSetArray = request.getParameter("kesSetArray");
        String deAgrKeset = request.getParameter("deAgrKeset");
        String[] alterColmNames = altercolNames.split(",");
        String[] renameColnames = renamecolnames.split(",");

        String[] kesSetStrArray = kesSetArray.split(",");
        String[] hashValStrArray = hashValArray.split(",");
        String[] deAgrKesetArray = deAgrKeset.replace("[", "").replace("]", "").split(",");
        HashMap dispColnames = new HashMap();
        HashMap deAgrKesetHM = new HashMap();

        ArrayList check1 = new ArrayList();
        for (int count = 0; count < kesSetStrArray.length; count++) {
            dispColnames.put(kesSetStrArray[count], hashValStrArray[count]);
            deAgrKesetHM.put(kesSetStrArray[count], deAgrKesetArray[count]);

        }

        CreatetableDAO createdao = new CreatetableDAO();
        ArrayList paramsAlterTableinDB = new ArrayList();
        paramsAlterTableinDB.add(filePath);
        paramsAlterTableinDB.add(alterColmNames);
        paramsAlterTableinDB.add(renameColnames);
        paramsAlterTableinDB.add(Integer.parseInt(sheetNumber));
        paramsAlterTableinDB.add(sheetName);
//        paramsAlterTableinDB.add(dispColnames);
        session.setAttribute("dispColnames", dispColnames);
        session.setAttribute("deAgrKesetHM", deAgrKesetHM);
        check1 = createdao.alterTableinDB(paramsAlterTableinDB);
        //.createTable(sheetName, colNames, colTypes, filePath))
        //
        String checkboo = check1.get(3).toString();
        if (checkboo.equalsIgnoreCase("false")) {
            Out.print(checkboo);
        } else {
            Out.print(check1.toString());
        }


        return null;
    }

    public ActionForward saveTableDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ArrayList paramArrlist = new ArrayList();
        PrintWriter Out = response.getWriter();
        String dimTabNames = request.getParameter("dimTabNames");
        String dimColumns = request.getParameter("dimColumns");
        String sheetName = request.getParameter("sheetName");
        String valradio = request.getParameter("valradio");
        String tableNAme = request.getParameter("tableNAme");
        String fileName = request.getParameter("fileName");
        //
        ////
        //ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©
        String[] dimTableNames = dimTabNames.split(",");
        //dimColumns
        String[] dimColumnnames = dimColumns.split(",");
        for (int i = 0; i < dimTableNames.length; i++) {
            ////
            ////
        }
        paramArrlist.add(dimTableNames);
        paramArrlist.add(dimColumnnames);
        paramArrlist.add(sheetName);
        paramArrlist.add(valradio);
        paramArrlist.add(fileName);
        paramArrlist.add(tableNAme);
        CreatetableDAO createdao = new CreatetableDAO();
        String strResult = createdao.saveTableDetails(paramArrlist);
        //Out.print(strResult);
        ////
        return null;
    }

    public ActionForward CheckDSNname(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        PrintWriter Out = response.getWriter();
        String dnsName = request.getParameter("dnsname");
        CreatetableDAO createdao = new CreatetableDAO();
        boolean check = createdao.CheckDSNname(dnsName);
        Out.print(check);

        return null;
    }

    public ActionForward checkInDB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        // CreatetableDAO.setSession(session);

        PrintWriter Out = response.getWriter();
        String selectedcolname = request.getParameter("selectedcolname");
        String filename = request.getParameter("fileName");
        String checkQuery = "";
        ////
        String[] seArraydcolname = selectedcolname.split(",");

        CreatetableDAO createdao = new CreatetableDAO();
        CreatetableDAO.setSession(session);
        PbReturnObject checkBo = createdao.checkInDB(selectedcolname, filename);
        if (checkBo.rowCount != 0) {
            for (int pck = 0; pck < checkBo.getRowCount(); pck++) {
                checkQuery += "," + checkBo.getFieldValue(pck, "DBMASTER_TABLE_ID");

            }
            checkQuery = checkQuery.substring(1);
        } else {

            checkQuery = "false";

        }
        Out.print(checkQuery);

        return null;
    }

    public ActionForward truncateTableInDB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        PrintWriter Out = response.getWriter();
        String delTableIds = request.getParameter("delTableIds");
        String fileName = request.getParameter("fileName");
        String filepath = request.getParameter("filepath");
        String sheetnum = request.getParameter("sheetnum");
        ////
        TruncateTableDAO truncateTable = new TruncateTableDAO();
        boolean check = truncateTable.truncateTable(delTableIds, filepath, sheetnum);
        Out.print(check);

        return null;
    }

    public ActionForward addDatainDb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        PrintWriter Out = response.getWriter();
        String delTableIds = request.getParameter("delTableIds");
        String fileName = request.getParameter("fileName");
        String filepath = request.getParameter("filepath");
        String sheetnum = request.getParameter("sheetnum");
        ////
        TruncateTableDAO truncateTable = new TruncateTableDAO();
        boolean check = truncateTable.addDatainDb(delTableIds, filepath, sheetnum);
        Out.print(check);

        return null;
    }
//modify by sunita

    public ActionForward uploadFileUtility(HttpServletRequest request, ActionForm form, ActionMapping mapping, String forwardpage, String tablename, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        CreatetableDAO xlupload = new CreatetableDAO();
        boolean isFileExists = false;
        PrintWriter Out = response.getWriter();
        boolean flag = false;
        String filePath = null;
        int sheetNum = -1;
        int nextSheetNum = -1;
        String fileName = null;
        String chkid = null;
        PbReturnObject prgr = null;
        isFileExists = request.getParameter("isFileExists") == null ? (request.getAttribute("isFileExists") == null ? false : true) : true;
        filePath = request.getParameter("filePath") == null ? (request.getAttribute("filePath") == null ? null : request.getAttribute("filePath").toString()) : request.getParameter("filePath");
        fileName = request.getParameter("fileName") == null ? (request.getAttribute("fileName") == null ? null : request.getAttribute("fileName").toString()) : request.getParameter("fileName");
        sheetNum = request.getParameter("sheetNum") == null ? (request.getAttribute("sheetNum") == null ? -1 : Integer.parseInt(request.getAttribute("sheetNum").toString())) : Integer.parseInt(request.getParameter("sheetNum"));
        nextSheetNum = request.getParameter("nextSheetNum") == null ? (request.getAttribute("nextSheetNum") == null ? -1 : Integer.parseInt(request.getAttribute("nextSheetNum").toString())) : Integer.parseInt(request.getParameter("nextSheetNum"));
        chkid = request.getParameter("id") == null ? (request.getAttribute("id") == null ? null : request.getAttribute("id").toString()) : request.getParameter("id");

        StrutsUplaodFile fileform = null;
        FormFile myFile = null;
        String uploadTableName = null;
        String connId = null;
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;

        ArrayList Alist = new ArrayList();
        String[] strcolumNames = null;
        String[] strcolumTypes = null;
        String sheetName = null;
        int totalSheet = 0;
        ////
        ////
        ////
        ////
        ////

        if (!isFileExists) {
            fileform = (StrutsUplaodFile) form;
            myFile = fileform.getFilename();
            uploadTableName = fileform.getTablename();
            connId = request.getParameter("connid");
            fileData = myFile.getFileData();
            filePath = (this.getServlet().getServletContext().getRealPath("/") + Calendar.getInstance().getTimeInMillis() + "_" + myFile.getFileName()).replace("\\", "\\\\");
            fileName = myFile.getFileName();
            tempFile = new File(filePath);
            sheetNum = 0;
            nextSheetNum = 0;
            if (tempFile != null) {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            isFileExists = true;
        }

//        if (nextSheetNum == -1) {
//            tempFile.delete();
//            request.removeAttribute("filePath");
//            request.removeAttribute("isFileExists");
//            request.removeAttribute("fileName");
//            request.removeAttribute("sheetNum");
//
//            return mapping.findForward("successpage");//forward to some other pages as there is no next excel sheet
//        } else {
//            tempFile = new File(filePath);
//            Alist = (xlupload.readExcel(tempFile, nextSheetNum));//retreive the number of sheets a and sheet name
//            if (Alist != null && Alist.size() != 0) {
//                strcolumNames = (String[]) Alist.get(0);
//                strcolumTypes = (String[]) Alist.get(1);
//                sheetName = (String) Alist.get(2);
//                nextSheetNum = Integer.parseInt(String.valueOf(Alist.get(3)));
//                prgr = (PbReturnObject) Alist.get(4);
//                totalSheet = Integer.parseInt(String.valueOf(Alist.get(5)));
//                nextSheetNum = sheetNum + 1;
//                sheetNum = sheetNum + 1;
//            } else {
//                nextSheetNum = -1;
//                tempFile.delete();
//                request.removeAttribute("filePath");
//                request.removeAttribute("isFileExists");
//                request.removeAttribute("fileName");
//                request.removeAttribute("sheetNum");
//                return null;
//            }
//            request.setAttribute("strcolumNames", strcolumNames);
//            request.setAttribute("strcolumTypes", strcolumTypes);
//            request.setAttribute("filePath", filePath);
//            request.setAttribute("sheetNum", sheetNum);
//            request.setAttribute("nextSheetNum", nextSheetNum);
//            request.setAttribute("isFileExists", isFileExists);
//            request.setAttribute("sheetName", sheetName);
//            request.setAttribute("fileName", fileName);
//            request.setAttribute("prgr", prgr);
//            request.setAttribute("totalSheet", totalSheet);
//            request.setAttribute("tablename", tablename);
//           // request.setAttribute("uploadTableName",uploadTableName);
//            //return mapping.findForward("showExceldetails");
//            //return mapping.findForward("saveTable");
//            xlupload.saveTableDetailsfromExcelSheet(tempFile,uploadTableName);
//            return mapping.findForward(forwardpage);
//
        //   }
//        flag = xlupload.saveTableDetailsfromExcelSheet(tempFile,uploadTableName,connId,chkid);
        flag = xlupload.saveTableDetailsfromExcelSheet(tempFile, tablename, connId, chkid);
        if (flag == true) {
            return mapping.findForward(forwardpage);
//            return null;
        } else {
            return mapping.findForward("exceptionPage");
//            return null;
//             return mapping.findForward(forwardpage);
        }
    }

    public ActionForward getExcelDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String connId = null;
        String filePath = null;
        filePath = this.getServlet().getServletContext().getRealPath("/");
        String flag = "";
        String downloadTableName = null;
        downloadTableName = request.getParameter("tablelistid");
        connId = request.getParameter("connid");
        CreatetableDAO xldownload = new CreatetableDAO();
        flag = xldownload.saveExcelDetails(downloadTableName, connId);
//    ServletUtilities.downloadFile(flag, response, "apllication/xls");

//    PrintWriter out = response.getWriter();
//            out.print(flag);
        return null;
    }

    public ActionForward getCustomerTableNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String connid = request.getParameter("conid");
        CreatetableDAO custdao = new CreatetableDAO();
        String tables = custdao.getCustomerTableNames(connid);
        response.getWriter().print(tables);
        return null;
    }
    //added by Nazneen

    public ActionForward uploadSSISFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        if (session != null) {
            if (request.getAttribute("requestFrom") != null) {
                String tablename = (String) request.getAttribute("sheetName");
                forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
            } else {
                if (request.getParameter("requestFrom") != null) {
                    String tablename = (String) request.getParameter("tablename");
                    forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
                } else {
                    String tablename = (String) request.getAttribute("sheetName");
                    forward = uploadSSISFileUtility(request, form, mapping, "uploadfile", tablename, response);
                }
            }
            return forward;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward uploadSSISFileUtility(HttpServletRequest request, ActionForm form, ActionMapping mapping, String forwardpage, String tablename, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        CreatetableDAO xlupload = new CreatetableDAO();
        boolean isFileExists = false;
        PrintWriter Out = response.getWriter();
        boolean flag = false;
        String filePath = null;
        int sheetNum = -1;
        int nextSheetNum = -1;
        String fileName = null;
        PbReturnObject prgr = null;
        isFileExists = request.getParameter("isFileExists") == null ? (request.getAttribute("isFileExists") == null ? false : true) : true;
        filePath = request.getParameter("filePath") == null ? (request.getAttribute("filePath") == null ? null : request.getAttribute("filePath").toString()) : request.getParameter("filePath");
        fileName = request.getParameter("fileName") == null ? (request.getAttribute("fileName") == null ? null : request.getAttribute("fileName").toString()) : request.getParameter("fileName");
        sheetNum = request.getParameter("sheetNum") == null ? (request.getAttribute("sheetNum") == null ? -1 : Integer.parseInt(request.getAttribute("sheetNum").toString())) : Integer.parseInt(request.getParameter("sheetNum"));
        nextSheetNum = request.getParameter("nextSheetNum") == null ? (request.getAttribute("nextSheetNum") == null ? -1 : Integer.parseInt(request.getAttribute("nextSheetNum").toString())) : Integer.parseInt(request.getParameter("nextSheetNum"));
        StrutsUplaodFile fileform = null;
        FormFile myFile = null;
        String uploadTableName = null;
        String connId = null;
        String dateFormat = null;
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;
        ArrayList Alist = new ArrayList();
        String[] strcolumNames = null;
        String[] strcolumTypes = null;
        String sheetName = null;
        int totalSheet = 0;

        if (!isFileExists) {
            fileform = (StrutsUplaodFile) form;
            myFile = fileform.getFilename();
            uploadTableName = fileform.getTablename();
            connId = request.getParameter("connid");
            dateFormat = request.getParameter("dateFormat");
            fileData = myFile.getFileData();
            filePath = this.getServlet().getServletContext().getRealPath("/");
            filePath = this.getServlet().getServletContext().getRealPath("/") + "TempExcel\\".replace("\\.\\", "\\") + Calendar.getInstance().getTimeInMillis() + "_" + myFile.getFileName().replace("xlsx", "xls");
            fileName = myFile.getFileName();
            tempFile = new File(filePath);
            sheetNum = 0;
            nextSheetNum = 0;
            if (tempFile != null) {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            isFileExists = true;
        }
        flag = xlupload.saveSSISTableDetailsfromExcelSheet(tempFile, uploadTableName, connId, dateFormat);
        if (flag == true) {
            return mapping.findForward("uploadSSISData");
        } else {
            return mapping.findForward("exceptionPage");
        }
    }

    public ActionForward uploadExcelData1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        boolean isFileExists = false;
        String filePath = "";
        StrutsUplaodFile fileform = null;
        FormFile myFile = null;
        String elementId = "";
        String bussRoleId = "";
        String periodType = "";
        String startValue = "";
        String endValue = "";
        String elemtName = "";
        String monthName = "";
        String regName = "";
        boolean fromExcelUpload = false;
        boolean dayandWeekLevel = false;
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;

        if (!isFileExists) {
            fileform = (StrutsUplaodFile) form;
            myFile = fileform.getFilename();
            elementId = request.getParameter("elementId");
            bussRoleId = request.getParameter("bussRoleId");
            periodType = request.getParameter("periodType");
            startValue = request.getParameter("startValue");
            endValue = request.getParameter("endValue");
            elemtName = request.getParameter("elemtName");
            monthName = request.getParameter("monthName");
            regName = request.getParameter("regName");
            dayandWeekLevel = Boolean.parseBoolean(request.getParameter("dayandWeekLevel"));
            fromExcelUpload = Boolean.parseBoolean(request.getParameter("fromExcelUpload"));
            fileData = myFile.getFileData();
            filePath = this.getServlet().getServletContext().getRealPath("/") + Calendar.getInstance().getTimeInMillis() + "_" + myFile.getFileName().replace("xlsx", "xls");
            tempFile = new File(filePath);
            if (tempFile != null) {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            isFileExists = true;
        }
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        if (!dayandWeekLevel) {
            try {
                ws = new WorkbookSettings();
                ws.setLocale(new Locale("en", "EN"));
                workbook = Workbook.getWorkbook(new FileInputStream(tempFile));
                s = workbook.getSheet(0);
                List<String> publshTypes = new ArrayList<String>();
                List<String> targetValues = new ArrayList<String>();
                List<String> customNames = new ArrayList<String>();
                UserLayerDAO usrlrDao = new UserLayerDAO();
                if (!fromExcelUpload) {
                    for (int i = 1; i < s.getRows(); i++) {
                        for (int j = 0; j < 2; j++) {
                            Cell cell = s.getCell(j, i);
                            if (j == 0) {
                                publshTypes.add(cell.getContents());
                            } else {
                                targetValues.add(cell.getContents());
                            }
                        }
                    }

                    usrlrDao.testForTargetValuesExits(elementId, periodType, startValue, endValue);
                    usrlrDao.insertTargetMeasureValues(elementId, elemtName, userId, bussRoleId, startValue, endValue, periodType, targetValues, publshTypes);
                } else {
                    for (int i = 1; i < s.getRows(); i++) {
                        for (int j = 0; j < 3; j++) {
                            Cell cell = s.getCell(j, i);
                            if (j == 2) {
                                publshTypes.add(cell.getContents());
                            } else if (j == 1) {
                                targetValues.add(cell.getContents());
                            } else if (j == 0) {
                                customNames.add(cell.getContents());
                            }
                        }
                    }
                    StringBuilder targetUpload = new StringBuilder();
                    //targetUpload.append("<tr>").append("<td width='50%'><h5>").append("Target Values").append("</h5></td>").append("<td width='50%'><h5>").append("Publish Type").append("</h5></td>").append("</tr>");

                    String exstingQuery = "SELECT CUSTOM_NAME,TARGET_VALUE,PUBLISH_TYPE FROM PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementId + "' ORDER BY TARGET_ID ASC";
                    PbReturnObject retobj = null;
                    Connection con = null;
                    con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                    try {
                        retobj = new PbDb().execSelectSQL(exstingQuery, con);
                        if (retobj.rowCount > 0) {
                            for (int i = 0; i < retobj.rowCount; i++) {
                                if (targetValues.get(i).equalsIgnoreCase(retobj.getFieldValueString(i, "TARGET_VALUE")) && publshTypes.get(i).equalsIgnoreCase(retobj.getFieldValueString(i, "PUBLISH_TYPE"))) {
                                    targetUpload.append("<tr>").append("<td width='50%'>").append(targetValues.get(i)).append("</td>").append("<td width='50%'>").append(publshTypes.get(i)).append("</td>").append("</tr>");
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    }
                    int value = targetUpload.length();
                    if (value == 0) {
                        usrlrDao.updateExcelPublishType(elementId, userId, bussRoleId, startValue, endValue, periodType, targetValues, publshTypes, customNames);
                    } else {
                        request.setAttribute("PublishingTypes", targetUpload.toString());
                    }
                }
            } catch (BiffException e) {
                logger.error("Exception:", e);
            }
//        if(fromExcelUpload){
            return mapping.findForward("excelUploadforSuccess");
        } else {
            try {
                ws = new WorkbookSettings();
                ws.setLocale(new Locale("en", "EN"));
                workbook = Workbook.getWorkbook(new FileInputStream(tempFile));
                s = workbook.getSheet(0);
                List<String> totalValues = new ArrayList<String>();
                List<String> viewBynames = new ArrayList<String>();
                List<String> week1 = new ArrayList<String>();
                List<String> week2 = new ArrayList<String>();
                List<String> week3 = new ArrayList<String>();
                List<String> week4 = new ArrayList<String>();
                List<String> week5 = new ArrayList<String>();
                List<String> week6 = new ArrayList<String>();

                UserLayerDAO usrlrDao = new UserLayerDAO();
                String viewByID = "";

                UserLayerDAO userLayerDAO = new UserLayerDAO();
                viewByID = userLayerDAO.getEndDateValue(elementId, monthName, bussRoleId, regName).get(0).toString();

                for (int i = 1; i < s.getRows() - 1; i++) {
                    for (int j = 0; j < s.getColumns(); j++) {
                        Cell cell = s.getCell(j, i);
                        if (j == 0) {
                            viewBynames.add(cell.getContents());
                        } else if (j == 1) {
                            week1.add(cell.getContents());
                        } else if (j == 2) {
                            week2.add(cell.getContents());
                        } else if (j == 3) {
                            week3.add(cell.getContents());
                        } else if (j == 4) {
                            week4.add(cell.getContents());
                        } else if (j == 5) {
                            week5.add(cell.getContents());
                        }

                        if (s.getColumns() == 8) {
                            if (j == 6) {
                                week6.add(cell.getContents());
                            } else if (j == 7) {
                                totalValues.add(cell.getContents());
                            }
                        }
                        if (s.getColumns() == 7 && j == 6) {
                            totalValues.add(cell.getContents());
                        }
                    }
                }
                usrlrDao.uploadWeekLevelData(elementId, monthName, viewByID, regName, elemtName, totalValues, viewBynames, week1, week2, week3, week4, week5, week6);

            } catch (BiffException e) {
                logger.error("Exception:", e);
            }

            return mapping.findForward("excelUploadforSuccess");
        }
    }

    public ActionForward executeManualQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PbReturnObject retObj = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        String conId = request.getParameter("conId");
        String querystring = request.getParameter("querystring");
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByConId(conId.toString());
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        retObj = db.execSelectSQL(querystring, connection);
        session.setAttribute("queryRetObj", retObj);
        // PrintWriter Out = response.getWriter().printf("Successfully Executed");
        //out.printf("Sucessfully Executed");
        // StringBuilder tableHtml = new StringBuilder();
//     int colcunt=retObj.getColumnCount();
//     tableHtml.append("<table border=2>");
//      tableHtml.append("<th style='font-size: 15px;background-color: rgb(231, 230, 230);color:#336698;font-family: verdana;text-align: left;'>").append("Progen Business solution").append("</th>");
//      tableHtml.append("</table>");
//     tableHtml.append("<table border=2>");
//      tableHtml.append("<tr>");
//     for(int i=0; i<colcunt; i++){
//        tableHtml.append("<th style='font-size: 15px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'>").append(retObj.getColumnNames()[i]).append("</th>");
//      }
//       tableHtml.append("</tr>");
//     for(int i=0; i<retObj.getRowCount(); i++)
//     {
//          tableHtml.append("<tr>");
//         for(int j=0; j<colcunt; j++)
//         {
//             tableHtml.append("<td>").append(retObj.getFieldValueString(i,j)).append("</td>");
//         }
//         tableHtml.append("</tr>");
//     }
//     tableHtml.append("</table>");
//     response.getWriter().print(tableHtml.toString());
        return null;
    }

    public ActionForward downLoadQueryData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);

        PbReturnObject retObj = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        String conId = request.getParameter("conId");
        String querystring = request.getParameter("querystring");
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByConId(conId.toString());
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        retObj = db.execSelectSQL(querystring, connection);
        String dtype = request.getParameter("dtype");

        String fileName = "";


        PbExcelGenerator excelDownload = new PbExcelGenerator();
        PbPDFDriver pdfDownload = new PbPDFDriver();
        PbHtmlGenerator htmlDownload = new PbHtmlGenerator();
        if (dtype.equalsIgnoreCase("queryexcel")) {
            fileName = excelDownload.queryExcelDownload(retObj, conId);
        } else if (dtype.equalsIgnoreCase("pdf")) {
            fileName = pdfDownload.queryPdfDownload(retObj, conId, response);
        } else {
            fileName = htmlDownload.queryhtmlDownload(retObj, conId);
        }
        response.getWriter().print(fileName);
        return null;
    }

    public ActionForward executeMailQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        PrintWriter Out = response.getWriter();
        DataSnapshotGenerator mailgo = new DataSnapshotGenerator();
        PbExcelGenerator excelDownload = new PbExcelGenerator();
        // PbHtmlGenerator htmlDownload = new PbHtmlGenerator();
        String fileName = "";
        PbReturnObject retObj = null;
        PbDb db = new PbDb();
        String conId = request.getParameter("conId");
        String querystring = request.getParameter("querystring");
        String mailid = request.getParameter("email");
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByConId(conId.toString());
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        retObj = db.execSelectSQL(querystring, connection);
        fileName = excelDownload.queryExcelDownload(retObj, conId);
        //fileName=htmlDownload.queryhtmlDownload(retObj,conId);
        mailgo.sendMail(fileName, mailid);
        response.getWriter().print("Sends Successfully");
        return null;
    }
//added By Amar for Multiple Excel Export

    public ActionForward uploadExcelFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        if (session != null) {
            if (request.getAttribute("requestFrom") != null) {
                String tablename = (String) request.getAttribute("sheetName");
                forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
                ////
            } else {
                if (request.getParameter("requestFrom") != null) {
                    String tablename = (String) request.getParameter("tablename");
                    forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
                    ////

                } else {
                    String tablename = (String) request.getAttribute("sheetName");
                    forward = uploadExcelFileUtility(request, form, mapping, "uploadfile", tablename, response);
                    ////
                }

            }
            return mapping.findForward("uploadFileSucceed");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward uploadExcelFileUtility(HttpServletRequest request, ActionForm form, ActionMapping mapping, String forwardpage, String tablename, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // CreatetableDAO xlupload = new CreatetableDAO();
        boolean isFileExists = false;
        PrintWriter Out = response.getWriter();
        boolean flag = false;
        //String filePath = null;
        int sheetNum = -1;
        int nextSheetNum = -1;
        String fileName = null;
        String chkid = null;
        PbReturnObject prgr = null;
//        isFileExists = request.getParameter("isFileExists") == null ? (request.getAttribute("isFileExists") == null ? false : true) : true;
//        //filePath = request.getParameter("filePath") == null ? (request.getAttribute("filePath") == null ? null : request.getAttribute("filePath").toString()) : request.getParameter("filePath");
//        fileName = request.getParameter("fileName") == null ? (request.getAttribute("fileName") == null ? null : request.getAttribute("fileName").toString()) : request.getParameter("fileName");
//        sheetNum = request.getParameter("sheetNum") == null ? (request.getAttribute("sheetNum") == null ? -1 : Integer.parseInt(request.getAttribute("sheetNum").toString())) : Integer.parseInt(request.getParameter("sheetNum"));
//        nextSheetNum = request.getParameter("nextSheetNum") == null ? (request.getAttribute("nextSheetNum") == null ? -1 : Integer.parseInt(request.getAttribute("nextSheetNum").toString())) : Integer.parseInt(request.getParameter("nextSheetNum"));
//       chkid = request.getParameter("id") == null ? (request.getAttribute("id") == null ? null : request.getAttribute("id").toString()) : request.getParameter("id");

        StrutsUplaodFile fileform = null;
        FormFile myFile = null;
        String uploadTableName = null;
        String connId = null;
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;

        ArrayList Alist = new ArrayList();
        String[] strcolumNames = null;
        String[] strcolumTypes = null;
        String sheetName = null;
        int totalSheet = 0;
        if (!isFileExists) {
            fileform = (StrutsUplaodFile) form;
            myFile = fileform.getFilename();
            uploadTableName = fileform.getTablename();
            //connId = request.getParameter("connid");
            fileData = myFile.getFileData();
            // filePath = this.getServlet().getServletContext().getRealPath("/") + "TempExcel\\".replace("\\.\\", "\\") + Calendar.getInstance().getTimeInMillis() + "_" + myFile.getFileName();
            String folderPath = (String) session.getAttribute("reportAdvHtmlFileProps") + "/importExcel";
            // container.getReportCollect().setReportAdvHtmlFileProps(localPath);
            //String folderPath=container.getReportCollect().getReportAdvHtmlFileProps()+"/SharedReports";
            //
            File folderDir = new File(folderPath);
            if (!folderDir.exists()) {
                folderDir.mkdir();
            }
            //String targetPath=(String)session.getAttribute("oldAdvHtmlFileProps");
            fileName = myFile.getFileName();
            String reportFileName = "/" + uploadTableName + ".xls";
            String filePath = folderPath + reportFileName;
            tempFile = new File(filePath);
            sheetNum = 0;
            nextSheetNum = 0;
            if (tempFile != null) {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }
                session.setAttribute("excelpath", tempFile);
                session.setAttribute("fileName", fileName);
            }
            isFileExists = true;
        }


        //flag = xlupload.saveTableDetailsfromExcelSheet(tempFile,uploadTableName,connId,chkid);
//        if(flag==true){
//            return mapping.findForward(forwardpage);
//        }else{
//            return mapping.findForward("exceptionPage");
//        }
        return null;
    }

    public ActionForward uploadExcelIntoDB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        if (session != null) {
//            if (request.getAttribute("requestFrom") != null) {
//                String tablename = (String) request.getAttribute("sheetName");
//                forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename,response);
//                ////
//            } else {
//                if (request.getParameter("requestFrom") != null) {
//                    String tablename = (String) request.getParameter("tablename");
//                    forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename,response);
//                    ////
//
//                } else {
//                    String tablename = (String) request.getAttribute("sheetName");
            forward = uploadExcelFileUtility(request, form, mapping, "uploadfile", "mohit", response);
//                    ////
//}
//
//            }
            return mapping.findForward("uploadFileSucceed");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
//      <!--Added by mohit for xslx uploading -->

    public ActionForward getAllTables(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        PrintWriter Out = response.getWriter();
//        String delTableIds = request.getParameter("delTableIds");
//        String fileName = request.getParameter("fileName");
//        String filepath = request.getParameter("filepath");
//        String sheetnum = request.getParameter("sheetnum");
        ////
//        TruncateTableDAO truncateTable = new TruncateTableDAO();
        CreatetableDAO cd = new CreatetableDAO();
        String result = null;
        result = cd.getAllTables(request.getParameter("conid"));
        Out.print(result);
//        boolean check = truncateTable.truncateTable(delTableIds, filepath, sheetnum);
//        Out.print(check);

        return null;
    }

    public ActionForward AddExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        if (session != null) {
//  CreatetableDAO xlupload = new CreatetableDAO();
            PrintWriter Out = response.getWriter();
            String filePath = null;
            String fileName = null;
            StrutsUplaodFile fileform = null;
            FormFile myFile = null;
            FileOutputStream fos = null;
            byte[] fileData = null;
            File tempFile = null;
            try {
                fileform = (StrutsUplaodFile) form;
                myFile = fileform.getFilename();
                fileData = myFile.getFileData();
//            filePath = "c://usr/local/cache/UploadedExcels";
                filePath = File.separator + "usr" + File.separator + "local" + File.separator + "cache" + File.separator + "UploadedExcels";
                tempFile = new File(filePath);
                if (tempFile != null) {
                    if (tempFile.exists()) {
                    } else {
                        tempFile.mkdirs();
                    }
                    tempFile = new File(filePath + File.separator + myFile);
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }

            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        }
        Container container = new Container();
        container.isExcelAdded = true;
        session.setAttribute("isExcelAdded", container);
        return mapping.findForward("uploadfile");

    }

    public ActionForward GetAllSheets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        String filename = request.getParameter("sheetname");
        PrintWriter Out = response.getWriter();
        String filePath = null;
        String fileName = null;
        StrutsUplaodFile fileform = null;
        FormFile myFile = null;
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;
        String totalinfo = "";
        try {
//               filePath = "c://usr/local/cache/UploadedExcels";
            filePath = File.separator + "usr" + File.separator + "local" + File.separator + "cache" + File.separator + "UploadedExcels";
//                if (tempFile != null) {
            tempFile = new File(filePath + File.separator + filename);
            OPCPackage pkg = OPCPackage.open(tempFile, PackageAccess.READ);
            UploadXSLX upxs = new UploadXSLX();
            totalinfo = upxs.GetAllSheets(pkg);
//                }
        } catch (Exception e) {
            logger.error("Exception:", e);
            return mapping.findForward("exceptionPage");
        }
        Out.print(totalinfo);
        return null;
    }

    public ActionForward SaveAndUploadXslx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        CreatetableDAO cd = new CreatetableDAO();
        cd.SaveAndUploadXslx(request, response);
        return null;
    }
    //added by Dinanath

    public ActionForward getLoadDataFromAlreadyInsertedWorkbook(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String select = request.getParameter("select");
        CreatetableDAO createTableDAO = new CreatetableDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        result = createTableDAO.getLoadDataFromAlreadyInsertedWorkbook(select, request);
        out.print(result);
        return null;
    }
//added by Dinanath

    public ActionForward getFixedFullpathFilename(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        PrintWriter Out = response.getWriter();
        CreatetableDAO dao = new CreatetableDAO();
        dao.getFixedFullpathFilename(request, response);
//        
        Out.print("success");
        return null;
    }
    //added by Dinanath

    public ActionForward addExcelData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String wbid = request.getParameter("wbid");
        // String fullpath = request.getParameter("fullpath");
        String filename = request.getParameter("fn");
        String connectionId = request.getParameter("connid");
        String insertIntoTableType = request.getParameter("tabletype");
        ActionForward forward = null;
        PrintWriter out = response.getWriter();
        String filePath = null;
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        if (session != null) {
//  CreatetableDAO xlupload = new CreatetableDAO();
            String fileName = null;
            StrutsUplaodFile fileform = null;
            FormFile myFile = null;
            FileOutputStream fos = null;
            byte[] fileData = null;
            File tempFile = null;
            try {
                fileform = (StrutsUplaodFile) form;
                myFile = fileform.getFilename();
                if (myFile != null) {
                    fileData = myFile.getFileData();
//            filePath = "c://usr/local/cache/UploadedExcels";
                    filePath = filePath + "/UploadedExcels";
                    tempFile = new File(filePath);
                    if (tempFile != null) {
                        if (tempFile.exists()) {
                        } else {
                            tempFile.mkdirs();
                        }
                        tempFile = new File(filePath + File.separator + myFile);
                        tempFile.createNewFile();
                        fos = new FileOutputStream(tempFile);
                        fos.write(fileData);
                        if (fos != null) {
                            fos.close();
                        }
                        OPCPackage pkg = OPCPackage.open(tempFile, PackageAccess.READ);
                        //UploadXSLX upxs = new UploadXSLX();
                        CreatetableDAO dao = new CreatetableDAO();
                        dao.addVariableExcelData(request, pkg, wbid, filename, connectionId, insertIntoTableType, response);
                        logger.info("With Variable Data loading is finished");
                    }
                }

            } catch (Exception e) {
                out.print("Null pointer exception:" + e.getStackTrace());
                return mapping.findForward("exceptionPage");
            }
        }
//        out.print("success");
        Container container = new Container();
        //container.isExcelAdded=true;
        container.isLoadedData = true;
        //session.setAttribute("isExcelAdded", container);
        session.setAttribute("isLoadedData", container);
        return mapping.findForward("uploadfile");
    }
    //added by Dinanath
    public ActionForward GetAllSheetsFromDatabase(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter Out = response.getWriter();
        CreatetableDAO dao = new CreatetableDAO();
        String htmlData = dao.getAllSheetsFromDatabase(request);
        Out.print(htmlData);
        return null;
    }
// added by Dinanath for uploading header photograph while html report scheduling
    public ActionForward AddHeaderPhotoScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        String filePath = null;
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        if (session != null) {
//  CreatetableDAO xlupload = new CreatetableDAO();
            PrintWriter Out = response.getWriter();

            String fileName = null;
            StrutsUplaodFile fileform = null;
            FormFile myFile = null;
            FileOutputStream fos = null;
            byte[] fileData = null;
            File tempFile = null;
            try {
                fileform = (StrutsUplaodFile) form;
                myFile = fileform.getFilename();
                fileData = myFile.getFileData();

//                filePath = "c://usr/local/cache";
                String ext = myFile.toString().substring(myFile.toString().lastIndexOf("."), myFile.toString().length());
                ext = ext.toLowerCase();
                fileName = "HeaderImageOFHtmlEmail" + ext;
                tempFile = new File(filePath);
                if (tempFile != null) {
                    if (!tempFile.exists()) {
                        tempFile.mkdirs();
                    }
                    tempFile = new File(filePath + File.separator + fileName);
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }
                if (!fileName.contains(".jpg")) {
                    BufferedImage bufferedImage = null;
                    try {
                        //read image file

                        bufferedImage = ImageIO.read(new File(filePath + File.separator + fileName));
                        // create a blank, RGB, same width and height, and a white background
                        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                        // write to jpeg file
                        ImageIO.write(newBufferedImage, "jpg", new File(filePath + File.separator + fileName.replace(ext, ".jpg")));
                        logger.info("converted to jpg");
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    }
                }
            } catch (Exception e) {
                logger.error("Exception:", e);
//                return mapping.findForward("exceptionPage");
            }
        }
//        Container container=new Container();
//        container.isExcelAdded=true;
//        session.setAttribute("isExcelAdded", container);
        return mapping.findForward("uploadFileSucceed");

    }
// added by Dinanath for uploading header photograph while html report scheduling
    public ActionForward AddTwoFooterPhotoInScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        String message = request.getParameter("message");
        String filePath = null;
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        if (session != null) {
            PrintWriter Out = response.getWriter();
            String fileName = null;
            StrutsUplaodFile fileform = null;
            FormFile myFile = null;
            FileOutputStream fos = null;
            byte[] fileData = null;
            File tempFile = null;
            try {
                fileform = (StrutsUplaodFile) form;
//                myFile = fileform.getFilename();
                MultipartRequestHandler multipartRequestHandler = form.getMultipartRequestHandler();

                Hashtable<String, FormFile> fileElements = multipartRequestHandler.getFileElements();

                for (Map.Entry<String, FormFile> entry : fileElements.entrySet()) {
//      
                    FormFile formFile = entry.getValue();
                    fileName = formFile.getFileName();
                    fileData = formFile.getFileData();

                    String ext = fileName.toString().substring(fileName.toString().lastIndexOf("."), fileName.toString().length());
                    ext = ext.toLowerCase();
                    if (entry.getKey().equalsIgnoreCase("rightLogoImage")) {
                        fileName = "rightLogoImageOfScheduler" + ext;
                    } else if (entry.getKey().equalsIgnoreCase("leftLogoImage")) {
                        fileName = "leftLogoImageOfScheduler" + ext;
                    }
                    tempFile = new File(filePath);
                    if (tempFile != null) {
                        if (tempFile.exists()) {
                        } else {
                            tempFile.mkdirs();
                        }
                        tempFile = new File(filePath + File.separator + fileName);
                        tempFile.createNewFile();
                        fos = new FileOutputStream(tempFile);
                        fos.write(fileData);
                        if (fos != null) {
                            fos.close();
                        }
                    }
                    if (!fileName.contains(".jpg")) {
                        BufferedImage bufferedImage;
                        try {
                            //read image file
                            bufferedImage = ImageIO.read(new File(filePath + File.separator + fileName));
                            // create a blank, RGB, same width and height, and a white background
                            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                            // write to jpeg file
                            ImageIO.write(newBufferedImage, "jpg", new File(filePath + File.separator + fileName.replace(ext, ".jpg")));
                            logger.info("converted to jpg");
                            if (tempFile.exists()) {
                                tempFile.delete();
                            }
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Exception:", e);
//            return mapping.findForward("exceptionPage");
            }
        }
        return mapping.findForward("uploadFileSucceed");

    }
// added by Dinanath for uploading header photograph while html report scheduling
    public ActionForward CreateHtmlSignatureForScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        String signatureHtmlContent = request.getParameter("signatureHtml");
        String filePath = null;
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        if (session != null) {

            try {
                //Whatever the file path is.

                File tempFile = new File(filePath);
                if (tempFile != null) {
                    if (tempFile.exists()) {
                    } else {
                        tempFile.mkdirs();
                    }
                }
                tempFile = new File(filePath + File.separator + "HtmlSignatureForScheduler.html");
                FileOutputStream is = new FileOutputStream(tempFile);
                OutputStreamWriter osw = new OutputStreamWriter(is);
                Writer w = new BufferedWriter(osw);
                w.write(signatureHtmlContent);
                w.close();
            } catch (Exception e) {
                System.err.println("Problem writing to the file statsTest.txt");
            }


        }
        return null;
    }
// added by Dinanath for uploading header photograph while html report scheduling
    public ActionForward CreateOptionalHeaderAsHtml(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        String signatureHtmlContent = request.getParameter("optionalHeaderMessage");
        String filePath = null;
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        if (session != null) {

            try {
                //Whatever the file path is.

                File tempFile = new File(filePath);
                if (tempFile != null) {
                    if (tempFile.exists()) {
                    } else {
                        tempFile.mkdirs();
                    }
                }
                tempFile = new File(filePath + File.separator + "HtmlOptionalHeaderForScheduler.html");
                FileOutputStream is = new FileOutputStream(tempFile);
                OutputStreamWriter osw = new OutputStreamWriter(is);
                Writer w = new BufferedWriter(osw);
                w.write(signatureHtmlContent);
                w.close();
            } catch (Exception e) {
                System.err.println("Problem writing to the file statsTest.txt");
            }


        }
        return null;
    }
// added by Dinanath for uploading header photograph while html report scheduling
    public ActionForward CreateOptionalFooterAsHtml(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        ActionForward forward = null;
        String signatureHtmlContent = request.getParameter("optionalFooterMessage");
        String filePath = null;
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        if (session != null) {

            try {
                //Whatever the file path is.
                File tempFile = new File(filePath);
                if (tempFile != null) {
                    if (tempFile.exists()) {
                    } else {
                        tempFile.mkdirs();
                    }
                }
                tempFile = new File(filePath + File.separator + "HtmlOptionalFooterForScheduler.html");
                FileOutputStream is = new FileOutputStream(tempFile);
                OutputStreamWriter osw = new OutputStreamWriter(is);
                Writer w = new BufferedWriter(osw);
                w.write(signatureHtmlContent);
                w.close();
            } catch (Exception e) {
                System.err.println("Problem writing to the file statsTest.txt");
            }


        }
        return null;
    }
//added by Dinanath for getting customer help email id
    public ActionForward getCustomerHelpPersonNameAndEmailId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        CreatetableDAO dao = new CreatetableDAO();
        String returnStr = dao.getCustomerHelpPersonNameAndEmailId();
        PrintWriter Out = response.getWriter();
        Out.print(returnStr);
        return null;
    }
//added By Dinanath for sending customer bug of report with snapshot file to following recipient

    public ActionForward newCustomerBugReportMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportid = request.getParameter("reportId");
        ActionForward forward = null;
        if (session != null) {
//            if (request.getAttribute("requestFrom") != null) {
//                String tablename = (String) request.getAttribute("sheetName");
//                forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename,response);
//                ////
//            } else {
//                if (request.getParameter("requestFrom") != null) {
//                    String tablename = (String) request.getParameter("tablename");
//                    forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename,response);
//                    ////
//
//                } else {
            String tablename = (String) request.getAttribute("subject");
            forward = newCustomerBugReportMailSub(request, form, mapping, "uploadfile", tablename, response);
            ////
//}

//            }
//            PrintWriter out = null;
//            out = response.getWriter();
//            out.print("Template uploaded");
            response.getWriter().print("Sends Successfully");
            return null;
            // return  mapping.findForward("excelUpload");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
    //added by Dinanath

    public ActionForward newCustomerBugReportMailSub(HttpServletRequest request, ActionForm form, ActionMapping mapping, String forwardpage, String tablename, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // CreatetableDAO xlupload = new CreatetableDAO();
        String reportid = request.getParameter("reportId");
        String reportName = request.getParameter("thisReportName");
        String subject = (String) request.getParameter("subject");
        String sendtoEmailId = (String) request.getParameter("sendToEmailId");
        String bugDescription = (String) request.getParameter("bugDescription");

        boolean isFileExists = false;
        PrintWriter Out = response.getWriter();
//        boolean flag=false;
        //String filePath = null;
//        int sheetNum = -1;
//        int nextSheetNum = -1;
        String fileName = null;
//        String chkid = null;
//        PbReturnObject prgr = null;

        StrutsUplaodFile fileform = null;
        FormFile myFile = null;
        String uploadTableName = null;
//        String connId=null;
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;

//        ArrayList Alist = new ArrayList();
//        String[] strcolumNames = null;
//        String[] strcolumTypes = null;
//        String sheetName = null;
//        int totalSheet = 0;
//        String filename=null;
//        String tablename1=null;
        if (!isFileExists) {
            //  filename = request.getParameter("filename");
            // tablename1=request.getParameter("tablename");
            fileform = (StrutsUplaodFile) form;
            myFile = fileform.getFilename();
            uploadTableName = fileform.getTablename();
            //connId = request.getParameter("connid");
            fileData = myFile.getFileData();
            // filePath = this.getServlet().getServletContext().getRealPath("/") + "TempExcel\\".replace("\\.\\", "\\") + Calendar.getInstance().getTimeInMillis() + "_" + myFile.getFileName();
            String folderPath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
            PbReportViewerDAO pbDAO = new PbReportViewerDAO();
            if (session != null) {
                folderPath = pbDAO.getFilePath(session);
            } else {
                folderPath = "/usr/local/cache";
            }
            // container.getReportCollect().setReportAdvHtmlFileProps(localPath);
            //String folderPath=container.getReportCollect().getReportAdvHtmlFileProps()+"/SharedReports";
            //
            File folderDir = new File(folderPath);
            if (!folderDir.exists()) {
                folderDir.mkdir();
            }
            //String targetPath=(String)session.getAttribute("oldAdvHtmlFileProps");
            fileName = myFile.getFileName();
            String filePath = folderPath + "/" + fileName;
            tempFile = new File(filePath);
//            sheetNum = 0;
//            nextSheetNum = 0;
            if (tempFile != null) {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }
//                session.setAttribute("excelpath1", tempFile);
//                session.setAttribute("fileName1",fileName);
            }
            isFileExists = true;

            PbMailParams params = null;
            String completeContent = "<html>\n"
                    + "<body>\n"
                    + " Dear Sir/Madam,<br>"
                    + "<center><h2>Following bug in report need to be resolve:</h2></center><br><div>"
                    + "<strong>Report Id:</strong> " + reportid + "<br>"
                    + "<strong>Report Name:</strong> " + reportName + "<br>"
                    + "<strong>Subject: </strong>" + subject + "<br>"
                    + "<strong>Bug Description:</strong> " + bugDescription + "<br>";
            completeContent += "</div><br><br>"
                    + "Regards<br>\n"
                    + "</body>\n"
                    + "\n"
                    + "</html>";


            String fullFilePath = filePath;


            ArrayList<String> attachFiles = new ArrayList<String>();
            attachFiles.add(fullFilePath);
            params = new PbMailParams();
            params.setBodyText(completeContent);
            // params.setBodyText(" ");

            params.setSubject("Customer report bug");
            params.setHasAttach(true);
            params.setAttachFile(attachFiles);
            params.setAutoMail(true);
            boolean status = false;
            params.setToAddr(sendtoEmailId);
//                            params.setToAddr("dinanath.parit@progenbusiness.com");
            PbMail mailer = new PbMail(params);
            try {
                status = mailer.sendMail();
            } catch (Exception e) {
                logger.error("Exception:", e);
            }

//                               File file = new File(fullFilePath);
            // if file doesnt exists, then create it
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }


        //flag = xlupload.saveTableDetailsfromExcelSheet(tempFile,uploadTableName,connId,chkid);
//        if(flag==true){
//            return mapping.findForward(forwardpage);
//        }else{
//            return mapping.findForward("exceptionPage");
//        }
        return null;
    }

    //added by krishan pratap
    public ActionForward uploadExcelFileTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportid = request.getParameter("reportId");
        logger.info("reportid...........: " + reportid);
        ActionForward forward = null;
        if (session != null) {
            if (request.getAttribute("requestFrom") != null) {
                String tablename = (String) request.getAttribute("sheetName");
                forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
                ////
            } else {
                if (request.getParameter("requestFrom") != null) {
                    String tablename = (String) request.getParameter("tablename");
                    forward = uploadFileUtility(request, form, mapping, "showExceldetails", tablename, response);
                    ////

                } else {
                    String tablename = (String) request.getAttribute("sheetName");
                    forward = uploadExcelFileUtilityTemplate(request, form, mapping, "uploadfile", tablename, response, reportid);
                    ////
                }

            }
//            PrintWriter out = null;
//            out = response.getWriter();
//            out.print("Template uploaded");
            response.getWriter().print("Sends Successfully");
            return null;
            // return  mapping.findForward("excelUpload");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //added by krishan pratap
    public ActionForward uploadExcelFileUtilityTemplate(HttpServletRequest request, ActionForm form, ActionMapping mapping, String forwardpage, String tablename, HttpServletResponse response, String reportid) throws Exception {
        HttpSession session = request.getSession(false);
        // CreatetableDAO xlupload = new CreatetableDAO();
        boolean isFileExists = false;
        PrintWriter Out = response.getWriter();
        boolean flag = false;
        //String filePath = null;
        int sheetNum = -1;
        int nextSheetNum = -1;
        String fileName = null;
        String chkid = null;
        PbReturnObject prgr = null;

        StrutsUplaodFile fileform = null;
        FormFile myFile = null;
        String uploadTableName = null;
        String connId = null;
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;

        ArrayList Alist = new ArrayList();
        String[] strcolumNames = null;
        String[] strcolumTypes = null;
        String sheetName = null;
        int totalSheet = 0;
        String filename = null;
        String tablename1 = null;
        if (!isFileExists) {
            //  filename = request.getParameter("filename");
            // tablename1=request.getParameter("tablename");
            fileform = (StrutsUplaodFile) form;
            myFile = fileform.getFilename();
            uploadTableName = fileform.getTablename();
            //connId = request.getParameter("connid");
            fileData = myFile.getFileData();
            // filePath = this.getServlet().getServletContext().getRealPath("/") + "TempExcel\\".replace("\\.\\", "\\") + Calendar.getInstance().getTimeInMillis() + "_" + myFile.getFileName();
            String folderPath = (String) session.getAttribute("reportAdvHtmlFileProps") + "/importTemplate" + "/" + reportid;
            // container.getReportCollect().setReportAdvHtmlFileProps(localPath);
            //String folderPath=container.getReportCollect().getReportAdvHtmlFileProps()+"/SharedReports";
            //
            File folderDir = new File(folderPath);
            if (!folderDir.exists()) {
                folderDir.mkdir();
            }
            //String targetPath=(String)session.getAttribute("oldAdvHtmlFileProps");
            fileName = myFile.getFileName();
            String reportFileName = "/" + uploadTableName + ".xls";
            String filePath = folderPath + reportFileName;
            tempFile = new File(filePath);
            sheetNum = 0;
            nextSheetNum = 0;
            if (tempFile != null) {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    fos = new FileOutputStream(tempFile);
                    fos.write(fileData);
                    if (fos != null) {
                        fos.close();
                    }
                }
                session.setAttribute("excelpath1", tempFile);
                session.setAttribute("fileName1", fileName);
            }
            isFileExists = true;
        }


        //flag = xlupload.saveTableDetailsfromExcelSheet(tempFile,uploadTableName,connId,chkid);
//        if(flag==true){
//            return mapping.findForward(forwardpage);
//        }else{
//            return mapping.findForward("exceptionPage");
//        }
        return null;
    }

    public ActionForward ExecuteAnyProcedure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        String procedureName = request.getParameter("procedureName");
        try {
            logger.info(procedureName + " :Procedure is calling.......");
            PbDb pbdb = new PbDb();
//                    Connection connection = ProgenConnection.getInstance().getConnectionByConId(connId);
            connection = pbdb.getConnection();
            logger.info("Connection is established for: " + procedureName);
            CallableStatement proc = null;
            //proc = con.prepareCall("{ call LDR_Proc('2014-01-01','2014-12-31') }");
            proc = connection.prepareCall("{ call " + procedureName + "() }");
            proc.execute();
            logger.info(procedureName + ": Procedure executed Sucessfully");
            response.getWriter().println(procedureName + " Procedure has called successfully");
            proc.close();
            connection.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
            response.getWriter().println(procedureName + " Procedure has not called successfully");
        }
//                try {
//                    
//                    PbDb pbdb = new PbDb();
////                    Connection connection2 = ProgenConnection.getInstance().getConnectionByConId(connId);
//                    connection = pbdb.getConnection();
//                    
//                    CallableStatement proc = null;
//                    //proc = con.prepareCall("{ call LDR_Proc('2014-01-01','2014-12-31') }");
//                    proc = connection.prepareCall("{ call bbraun.EBIT_Proc() }");
//
//                    proc.execute();
//                    
//                    response.getWriter().println("EBIT_Proc Procedure has called successfully");
//                    connection.close();
//                } catch (Exception e) {
//                    logger.error("Exception:",e);
//                }

        return null;
    }
}
