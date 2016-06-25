/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import com.google.common.collect.Iterables;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEncrypter;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class SuperAdminBd extends PbDb {

    public static Logger logger = Logger.getLogger(SuperAdminBd.class);
    private ResourceBundle resBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBundle = new SuperAdminSqlServerResBundle();
            } else {

                resBundle = new SuperAdminResourceBundle();

            }
        }
        return resBundle;

    }

    public License getMasterLicense() {
        PbDb pbdb = new PbDb();
        PbReturnObject pbroModule = new PbReturnObject();
        PbReturnObject pbroComp = new PbReturnObject();

        License license = new License();
        LicenseModule licenseModule = new LicenseModule();
        int moduleId;
        String moduleName;
        String moduleCode;
        int licensesCt = 0;

        int compId;
        String compCode;
        String compName;
        String parCompCode;
        String componetQry = "";

        String moduleQry = "select * from PRG_MODULES";
        try {
            pbroModule = pbdb.execSelectSQL(moduleQry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


        for (int i = 0; i < pbroModule.getRowCount(); i++) {
            moduleId = pbroModule.getFieldValueInt(i, 0);
            moduleName = pbroModule.getFieldValueString(i, 1);
            moduleCode = pbroModule.getFieldValueString(i, 2);
            licenseModule = license.createLicenseModule(moduleId, licensesCt, moduleName, moduleCode, false);

            ///qry
            componetQry = "select * from prg_module_components where module_id=" + moduleId;
            try {
                pbroComp = pbdb.execSelectSQL(componetQry);
                for (int j = 0; j < pbroComp.getRowCount(); j++) {
                    moduleId = pbroComp.getFieldValueInt(j, 1);
                    compId = pbroComp.getFieldValueInt(j, 0);
                    compCode = pbroComp.getFieldValueString(j, 3);
                    compName = pbroComp.getFieldValueString(j, 2);
                    parCompCode = pbroComp.getFieldValueString(j, 5);

                    licenseModule.createModuleComponent(compId, compCode, compName, moduleId, parCompCode, false);
                }

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

        }

        return license;

    }

    public void createXmlDocument(String path, String xmlData) {

        File file = new File(path);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            if (file.exists()) {
                outputStream.write(xmlData.getBytes());
            }
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

    public ArrayList<LicenseModule> parseSuperAdminXml(String xmlData) {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        Element root = null;
        List moduleLst;
        Element moduleEle;
        List moduleCodeLst;
        Element moduleCodeEle;
        List licenseLst;
        Element licenseEle;

        String moduleCode;
        int noOfLicenses;

        List enableCompLst;
        Element enableCompEle;

        List compsList;
        Element compEle;
        String componentCode;
        ArrayList<String> componentLst = null;
        ArrayList<LicenseModule> licenseModLst = new ArrayList<LicenseModule>();
        try {
            document = builder.build(new ByteArrayInputStream(xmlData.toString().getBytes()));
            root = document.getRootElement();
            moduleLst = root.getChildren("Module");
            for (int i = 0; i < moduleLst.size(); i++) {
                componentLst = new ArrayList<String>();
                moduleEle = (Element) moduleLst.get(i);

                moduleCodeLst = moduleEle.getChildren("ModuleCode");
                moduleCodeEle = (Element) moduleCodeLst.get(0);
                moduleCode = moduleCodeEle.getText();

                licenseLst = moduleEle.getChildren("License");
                licenseEle = (Element) licenseLst.get(0);
                noOfLicenses = Integer.parseInt(licenseEle.getText());


                LicenseModule licModule = new LicenseModule();
                licModule.setModuleCode(moduleCode);
                licModule.setLicenses(noOfLicenses);
                licModule.setEnable(true);

                //if (noOfLicenses > 0) {
                enableCompLst = moduleEle.getChildren("EnabledComponents");
                enableCompEle = (Element) enableCompLst.get(0);

                compsList = enableCompEle.getChildren("Component");
                for (int j = 0; j < compsList.size(); j++) {
                    compEle = (Element) compsList.get(j);
                    componentCode = compEle.getText();
                    licModule.createModuleComponent(0, componentCode, "", 0, "", true);
                }
                licenseModLst.add(licModule);
            }


        } catch (Exception e) {
//            logger.error("Exception:",e);
        }
        return licenseModLst;
    }

    public void savePrivilegesToUser(UserLicense userLicense, UserAssignment userAssg, int userId) {
        ArrayList<LicenseModule> licenseModuleLst = userAssg.getLicenseModuleLst();
        ArrayList<ModuleComponent> moduleCompLst;
        int moduleCompId = 0;
        Object moduleObj[] = null;
        Object componentObj[] = null;
        String modulesQry = this.getResourceBundle().getString("insertModules");
        String compsQry = this.getResourceBundle().getString("insertComponents");
        String finalQry;
        String finalQuery;
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String seqQry = this.getResourceBundle().getString("nextQry");
        ArrayList<String> queriesLst = new ArrayList<String>();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            for (LicenseModule licenseModule : licenseModuleLst) {
                moduleObj = new Object[3];
                moduleObj[0] = userId;
                moduleObj[1] = licenseModule.getModuleId();
                moduleObj[2] = licenseModule.getModuleCode();
                finalQry = pbdb.buildQuery(modulesQry, moduleObj);
                try {
                    moduleCompId = super.insertAndGetSequenceInSQLSERVER(finalQry, "PRG_USER_MODULE_ASSIGNMENTS");
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
                //queriesLst.add(finalQry);
                try {
                    moduleCompLst = licenseModule.getModuleComponentsLst();

                    for (ModuleComponent modComp : moduleCompLst) {
                        componentObj = new Object[4];
                        componentObj[0] = moduleCompId;
                        componentObj[1] = modComp.getCompId();
                        componentObj[2] = modComp.getCompCode();
                        componentObj[3] = modComp.getParentCompCode();
                        finalQuery = pbdb.buildQuery(compsQry, componentObj);
                        queriesLst.add(finalQuery);
                    }
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
            }
            pbdb.executeMultiple(queriesLst);

        } else {
            for (LicenseModule licenseModule : licenseModuleLst) {
                try {
                    pbro = pbdb.execSelectSQL(seqQry);
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
                moduleObj = new Object[5];
                componentObj = new Object[4];
                moduleObj[0] = pbro.getFieldValueInt(0, 0);
                moduleObj[1] = userId;
                moduleObj[2] = licenseModule.getModuleId();
                moduleObj[3] = licenseModule.getModuleCode();
                moduleObj[4] = userId;
                finalQry = pbdb.buildQuery(modulesQry, moduleObj);

                queriesLst.add(finalQry);

                moduleCompLst = licenseModule.getModuleComponentsLst();

                for (ModuleComponent modComp : moduleCompLst) {
                    componentObj[0] = moduleObj[0];
                    componentObj[1] = modComp.getCompId();
                    componentObj[2] = modComp.getCompCode();
                    componentObj[3] = modComp.getParentCompCode();
                    finalQry = pbdb.buildQuery(compsQry, componentObj);
                    queriesLst.add(finalQry);
                }

            }
            pbdb.executeMultiple(queriesLst);
        }
    }

    public String convertStreamToString(InputStream is)
            throws IOException {
        EncryptDecryptXml dec = new EncryptDecryptXml();

        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            if (writer.toString().length() > 0) {
                return dec.decryptXml(writer.toString());
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public License createUserLicenseObject(String xmlData) {
        License masterLicense = this.getMasterLicense();
        ArrayList<LicenseModule> userLicenseModules = this.parseSuperAdminXml(xmlData);

        masterLicense.disableAll();

        for (LicenseModule module : userLicenseModules) {
            masterLicense.enableModule(module);

            masterLicense.setLicenseCount(module, module.getLicenses());
            int assignedCount = this.getAssignmentCountForModule(module.getModuleCode());

            masterLicense.setConsumedCountForModule(module, assignedCount);

            for (ModuleComponent component : module.getModuleComponentsLst()) {
                masterLicense.enableModuleComponent(module, component);
            }
        }
        return masterLicense;

    }

    public UserAssignment createUserAssigmentObject(int userId, String type) {
        UserAssignment userAssignment = new UserAssignment(userId);
//        int userTypeId=getUserTypeId(userId);
        PbDb pbdb = new PbDb();
        PbReturnObject pbroMod = new PbReturnObject();
        PbReturnObject pbroComp = new PbReturnObject();
        Object objMod[] = new Object[1];
        Object objComp[] = new Object[1];
        String moduleQuery = "";
        if ("user".equalsIgnoreCase(type)) {
            moduleQuery = this.getResourceBundle().getString("getAssignedModulesQry");
        } else {
            moduleQuery = this.getResourceBundle().getString("getModulesQry");
        }

        String compQuery = this.getResourceBundle().getString("getComponentsQry");
        objMod[0] = userId;
        String moduleCode;
        int moduleAssId;
        String compCode;
        String parCompCode;
        String finalQry = pbdb.buildQuery(moduleQuery, objMod);
        try {
            pbroMod = pbdb.execSelectSQL(finalQry);

            if (pbroMod != null & pbroMod.getRowCount() > 0) {
                for (int i = 0; i < pbroMod.getRowCount(); i++) {
                    moduleAssId = pbroMod.getFieldValueInt(i, "MODULE_ASSIGN_ID");
                    moduleCode = pbroMod.getFieldValueString(i, "MODULE_CODE");
                    userAssignment.assignModuleToUser(moduleCode);
                    objComp[0] = moduleAssId;
                    // objComp[0] = moduleCode;

                    pbroComp = pbdb.execSelectSQL(pbdb.buildQuery(compQuery, objComp));
                    if (pbroComp != null && pbroComp.getRowCount() > 0) {
                        for (int j = 0; j < pbroComp.getRowCount(); j++) {
                            compCode = pbroComp.getFieldValueString(j, "COMPONENT_CODE");
                            parCompCode = pbroComp.getFieldValueString(j, "PARENT_COMP_CODE");
                            userAssignment.assingModuleComponentToUser(moduleCode, compCode, parCompCode);
                        }
                    }

                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return userAssignment;
    }

    public int getUserTypeId(int userId) {
        String userTypeQry = getResourceBundle().getString("userTypeQry");
        Object bindObj[] = new Object[1];
        bindObj[0] = userId;
        int userTypeId = 0;
        try {
            PbReturnObject pbro = execSelectSQL(buildQuery(userTypeQry, bindObj));
            if (pbro.getRowCount() > 0) {
                userTypeId = pbro.getFieldValueInt(0, "USER_TYPE");
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return userTypeId;
    }

    public int getAssignmentCountForModule(String moduleCode) {
        int count = 0;
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String countQry = this.getResourceBundle().getString("countQry");
        Object obj[] = new Object[1];
        try {
            obj[0] = moduleCode;
            pbro = pbdb.execSelectSQL(pbdb.buildQuery(countQry, obj));
        } catch (Exception ex) {
        }
        if (pbro != null) {
            count = pbro.getFieldValueInt(count, "COUNT");
        }
        return count;
    }

    public String getLicenseModuleJSONForUser(UserLicense userLicense, UserAssignment userAssg) {
        ArrayList<LicenseModule> licenseModuleLst = userLicense.getLicenseModules();
        StringBuilder builder = new StringBuilder("");
        builder.append("{");
        StringBuilder moduleCodes=new StringBuilder();
        moduleCodes.append("ModuleCodes:[");
        StringBuilder moduleNames=new StringBuilder();
        
        moduleNames.append("ModuleNames:[");
        StringBuilder moduleStatus=new StringBuilder();
        moduleStatus.append("ModuleStatus:[");
         StringBuilder compsAvail=new StringBuilder();
       compsAvail.append("ComponenentsAvail:[");
        int count = 0;
        for (LicenseModule module : licenseModuleLst) {
            if (module.isEnable() && module.isModuleAvailableForAssignment()) {
                 moduleCodes.append("\"");
                 moduleCodes.append(module.getModuleCode());
                 moduleCodes.append("\"");
                moduleNames.append("\"");
                moduleNames.append(module.getModuleName());
                moduleNames.append("\"");
                moduleStatus.append("\"");

                if (userAssg.isModuleAssigned(module.getModuleCode())) {
                    moduleStatus.append("checked");
                } else {
                    moduleStatus.append("unchecked");
                }
                moduleStatus.append("\"");
                compsAvail.append("\"");
                if (userLicense.isCompAvailableForModule(module.getModuleCode())) {
                    compsAvail.append("true");
                } else {
                    compsAvail.append("false");
                }
                compsAvail.append("\"");

                if (count != licenseModuleLst.size() - 1) {
                     moduleCodes.append(",");
                    moduleNames.append(",");
                    moduleStatus.append(",");
                    compsAvail.append(",");
                }
                count++;
            } else if (module.isEnable()) {
                if (userAssg.isModuleAssigned(module.getModuleCode())) {
                     moduleCodes.append("\"");
                     moduleCodes.append(module.getModuleCode());
                     moduleCodes.append("\"");
                    moduleNames.append("\"");
                    moduleNames.append(module.getModuleName());
                    moduleNames.append("\"");
                    moduleStatus.append("\"");
                    moduleStatus.append("checked");
                    moduleStatus.append("\"");
                    compsAvail.append("\"");
                    if (userLicense.isCompAvailableForModule(module.getModuleCode())) {
                        compsAvail.append("true");
                    } else {
                        compsAvail.append("false");
                    }
                    compsAvail.append("\"");
                    if (count != licenseModuleLst.size() - 1) {
                         moduleCodes.append(",");
                        moduleNames.append(",");
                        moduleStatus.append(",");
                        compsAvail.append(",");
                    }
                    count++;
                }

            }
        }
         moduleCodes.append("],");
        moduleNames.append("],");
        moduleStatus.append("],");
        compsAvail.append("]");

        builder.append(moduleCodes + moduleNames.toString() + moduleStatus + compsAvail + "}");

        return builder.toString();


    }

    public String getModuleComponentsJSONforUser(UserLicense userLicense, UserAssignment userAssg, String moduleCode) {
        StringBuilder json = new StringBuilder("");
        json.append("{");
       StringBuilder parCompCode=new StringBuilder();
                 
        parCompCode.append("ParentComponentCodes:[");
        StringBuilder parCompName=new StringBuilder();
        parCompName.append("ParentComponentNames:[");
        StringBuilder compStatus=new StringBuilder();
        compStatus.append("ParentComponentStatus:[");
        StringBuilder childCompCode =new StringBuilder();
        StringBuilder childCompName=new StringBuilder();
        StringBuilder childCompStatus=new StringBuilder();

        Iterator<ModuleComponent> parentComponents = userLicense.getParentModuleComponents(moduleCode).iterator();

        while (parentComponents.hasNext()) {
            ModuleComponent parent = parentComponents.next();
            childCompCode.append("");
            childCompName.append("");
            childCompStatus.append("");
            if (parent.isEnable()) {
                parCompCode.append("\"").append(parent.getCompCode()).append("\"");
                parCompName.append("\"").append(parent.getCompName()).append("\"");

                if (userAssg.isModuleComponentAssigned(moduleCode, parent.getCompCode())) {
                    compStatus.append("\"" + "checked" + "\"");
                } else {
                    compStatus.append("\"" + "unchecked" + "\"");
                }

                if (parentComponents.hasNext()) {
                    parCompCode.append(",");
                    parCompName.append(",");
                    compStatus.append(",");

                }


                Iterator<ModuleComponent> childComponents = userLicense.getChildModuleComponents(moduleCode, parent.getCompCode()).iterator();
                if (childComponents.hasNext()) {
                    childCompCode.append(parent.getCompCode()).append("_childCodes:[");
                    childCompName.append(parent.getCompCode()).append("_childNames:[");
                    childCompStatus.append(parent.getCompCode()).append("_status:[");
                }
                while (childComponents.hasNext()) {
                    ModuleComponent child = childComponents.next();
                    if (child.isEnable()) {
                        childCompCode.append("\"").append(child.getCompCode()).append("\"");
                        childCompName.append("\"").append(child.getCompName()).append("\"");

                        if (userAssg.isModuleComponentAssigned(moduleCode, child.getCompCode())) {
                            childCompStatus.append("\"" + "checked" + "\"");
                        } else {
                            childCompStatus.append("\"" + "unchecked" + "\"");
                        }
                        if (childComponents.hasNext()) {
                            childCompCode.append(",");
                            childCompName.append(",");
                            childCompStatus.append(",");
                        }
                    }
                }
                if (!childCompCode.equals("")) {
                    childCompCode.append("],");
                    childCompName.append("],");
                    childCompStatus.append("],");

                    json.append(childCompCode);
                    json.append(childCompName);
                    json.append(childCompStatus);

                }
            }
        }
        parCompCode.append("],");
        parCompName.append("],");
        compStatus.append("]");
        json.append(parCompCode);
        json.append(parCompName);
        json.append(compStatus);
        json.append("}");
        //  
        return json.toString();
    }

    public void assignModuleToUser(String moduleCode, UserAssignment userAssg, UserLicense license) {
        boolean isAssigned = userAssg.assignModuleToUser(moduleCode);
        if (isAssigned) {
            license.consumeLicense(moduleCode);
        }
    }

    public void assignAllModuleCompsToUser(String moduleCode, UserAssignment userAssg, UserLicense license) {
        userAssg.assignModuleToUser(moduleCode);
        ArrayList<ModuleComponent> moduleCompsLst = license.getParentModuleComponents(moduleCode);
        ArrayList<ModuleComponent> childModuleCompsLst = license.getAllChildModuleComponents(moduleCode);
        for (ModuleComponent moduleComp : moduleCompsLst) {
            userAssg.assignModuleToUser(moduleComp, moduleCode);
        }
        for (ModuleComponent childModComp : childModuleCompsLst) {
            userAssg.assignChildModuleToUser(childModComp, moduleCode);
        }

        license.consumeLicense(moduleCode);
    }

    public void revokeModuleFromUser(String moduleCode, UserAssignment userAssg, UserLicense license) {
        boolean isModuleRevoked = userAssg.revokeModuleFromUser(moduleCode);
        if (isModuleRevoked) {
            license.revokeLicense(moduleCode);
        }

    }

    public void revokeAllModuleCompsFromUser(String moduleCode, UserAssignment userAssg, UserLicense license) {
        userAssg.revokeModuleFromUser(moduleCode);
        ArrayList<ModuleComponent> moduleCompsLst = license.getParentModuleComponents(moduleCode);
        ArrayList<ModuleComponent> childModuleCompsLst = license.getAllChildModuleComponents(moduleCode);
        for (ModuleComponent moduleComp : moduleCompsLst) {
            userAssg.revokeModuleFromUser(moduleComp, moduleCode);
        }
        for (ModuleComponent childModComp : childModuleCompsLst) {
            userAssg.revokeChildModuleFromUser(childModComp, moduleCode);
        }

        license.revokeLicense(moduleCode);

    }

//    public void assignModuleComponentToUser(String moduleCode, UserAssignment userAssg, String compCode, String parCompCode) {
//        userAssg.assingModuleComponentToUser(moduleCode, compCode, parCompCode);
//    }
//
//    public void revokeModuleComponentFromUser(String moduleCode, UserAssignment userAssg, String compCode, String parCompCode) {
//        userAssg.revokeModuleComponentToUser(moduleCode, compCode, parCompCode);
//    }
    public void assignModuleComponentToUser(String moduleCode, UserAssignment userAssg, String compCode, String parCompCode) {
        userAssg.assingModuleComponentToUser(moduleCode, compCode, parCompCode);
    }

    public void assignModuleComponentToUser(UserAssignment userAssg, ModuleComponent modComponent, String moduleCode) {
        userAssg.assignChildModuleToUser(modComponent, moduleCode);
    }

    public void revokeModuleComponentFromUser(UserAssignment userAssg, ModuleComponent modComponent, String moduleCode) {
        userAssg.revokeChildModuleFromUser(modComponent, moduleCode);
    }

    public void revokeModuleComponentFromUser(String moduleCode, UserAssignment userAssg, String compCode, String parCompCode) {
        userAssg.revokeModuleComponentToUser(moduleCode, compCode, parCompCode);
    }

    public void deleteAllPrivilegesToUser(int userTypeId) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String selectQry = this.getResourceBundle().getString("getModulesQry");
        Object obj[] = new Object[1];
        obj[0] = userTypeId;
        try {
            pbro = pbdb.execSelectSQL(pbdb.buildQuery(selectQry, obj));
            if (pbro.getRowCount() > 0) {
//                String modIds = "";
                StringBuilder modIds=new StringBuilder();
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    modIds.append(pbro.getFieldValueString(i, "MODULE_ASSIGN_ID"));
                    if (i != pbro.getRowCount() - 1) {
                        modIds.append(",");
                    }
                }

                String deleteQuery = this.getResourceBundle().getString("deleteModQuery");
                pbdb.execModifySQL(pbdb.buildQuery(deleteQuery, obj));
                obj[0] = modIds;
                String deleteCompQry = this.getResourceBundle().getString("deleteCompModQuery");
                pbdb.execModifySQL(pbdb.buildQuery(deleteCompQry, obj));
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public String showLicensePrivileges(String xmlData) {
        StringBuilder builder = new StringBuilder();
        License masterLicense = this.getMasterLicense();
        LicenseModule licModule = null;
        ArrayList<LicenseModule> userLicenseModules = this.parseSuperAdminXml(xmlData);
        int totalLic = 0;
        int assignedCount = 0;
        int available = 0;
        String query = "";
        PbReturnObject pbro;
        PbDb pbdb = new PbDb();
        String userNames = "";
        builder.append("<table border='0' align='center' class='tablesorter'><thead><tr>");
        builder.append("<th>Module Name</th><th align='center'>Licenses Purchased</th><th align='center'>Licenses Consumed</th><th align='center'>Licenses Available</th><th>Assigned To</th></tr></thead>");
        builder.append("<tbody>");
        for (LicenseModule module : masterLicense.getLicenseModuleLst()) {
            userNames = "";
            builder.append("<tr>");

            Iterator<LicenseModule> moduleIter = Iterables.filter(userLicenseModules, LicenseModule.getLicenseModulePredicate(module.getModuleCode())).iterator();
            if (moduleIter.hasNext()) {
                licModule = moduleIter.next();
            }
            if (licModule != null) {
                totalLic = licModule.getLicenses();
            } else {
                totalLic = 0;
            }
            assignedCount = this.getAssignmentCountForModule(module.getModuleCode());
            licModule = masterLicense.getLicenseModulePredicate(module.getModuleCode());
            available = totalLic - assignedCount;
            query = "select PU_LOGIN_ID from PRG_AR_USERS where PU_ID in (select user_id from PRG_USER_TYPE_PRIVILEGE where module_code='" + module.getModuleCode() + "')";
            try {
                pbro = pbdb.execSelectSQL(query);
//                for(int i=0;i<pbro.getRowCount();i++)
//                {
//                    userNames+=","+pbro.getFieldValueString(i, "PU_LOGIN_ID");
//                }
//                if(userNames.length()>0)
//                 userNames=userNames.substring(1);
//                else
//                 userNames="Not Assigned to any User";
                builder.append("<td style='font-weight:bolder'>").append(licModule.getModuleName()).append("</td>");
                builder.append("<td align='center'>").append(totalLic).append("</td>");
                builder.append("<td align='center'>").append(assignedCount).append("</td>");
                builder.append("<td align='center'>").append(available).append("</td>");
                builder.append("<td align='center'>").append("<a onclick=\"showUsers('" + getUsersHtml(pbro) + "')\" >View</a>").append("</td>");
//              builder.append("<td align='center'>").append("<a class='bubbleAnchor' tooltip='"+getUsersHtml(pbro)+"' ><span class='ui-icon ui-icon-circle-arrow-n'></span></a>").append("</td>");
                builder.append("</tr>");
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        builder.append("</tbody>");
        builder.append("</table>");
        builder.append("<br><table align='center'><tr><td><input type='button' class='navtitle-hover' value='Close' onclick='closeLicenseDiv()'></td></tr></table>");
        return builder.toString();
    }

    public String getUsersHtml(PbReturnObject pbro) {
        StringBuilder builder = new StringBuilder();
        if (pbro.getRowCount() > 0) {
            builder.append("<table>");
            for (int i = 0; i < pbro.getRowCount(); i++) {
                builder.append("<tr>");
                builder.append("<td>").append(pbro.getFieldValueString(i, "PU_LOGIN_ID")).append("</td></tr>");
//             builder.append("<td>").append("<span class='ui-icon ui-icon-arrowreturnthick-1-e'></span>").append("</td><td>").append(pbro.getFieldValueString(i, "PU_LOGIN_ID")).append("</td></tr>");
            }
            builder.append("</table>");
        } else {
            builder.append("Not Assigned to any user");
        }
        return builder.toString();
    }

    public String checkUserPrivileges(int userTypeId, String xmlData, HttpSession session) {
        String message = "";
        String usersQry = "select PU_ID from PRG_AR_USERS where USER_TYPE=" + userTypeId;
        String moduleCountQry = "select Count(*) from PRG_USER_TYPE_PRIVILEGE where MODULE_CODE='&'";
        String modulesQry = "select MODULE_CODE from PRG_USER_MODULE_ASSIGNMENTS where USER_TYPE_ID=" + userTypeId;
        License masterLicense = this.getMasterLicense();
        LicenseModule licModule = null;
        ArrayList<LicenseModule> userLicenseModules = this.parseSuperAdminXml(xmlData);
        ArrayList<Integer> userLst = new ArrayList<Integer>();
        ArrayList<String> userTypeModules = new ArrayList<String>();
        Map<String, Integer> privlegeMap = new HashMap<String, Integer>();
        int totalLicensesCount = 0;
        int moduleAssignCt = 0;
        int availableCt = 0;
        try {
            PbReturnObject usersPbro = execSelectSQL(usersQry);
            for (int i = 0; i < usersPbro.getRowCount(); i++) {
                userLst.add(usersPbro.getFieldValueInt(i, "PU_ID"));
            }
            PbReturnObject modulesPbro = execSelectSQL(modulesQry);
            for (int i = 0; i < modulesPbro.getRowCount(); i++) {
                userTypeModules.add(modulesPbro.getFieldValueString(i, "MODULE_CODE"));
            }
            if (userTypeModules.isEmpty()) {
                message = "available";
            } else {
                for (String moduleCode : userTypeModules) {
                    totalLicensesCount = 0;
                    moduleAssignCt = 0;
                    availableCt = 0;
                    Iterator<LicenseModule> moduleIter = Iterables.filter(userLicenseModules, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
                    if (moduleIter.hasNext()) {
                        licModule = moduleIter.next();
                        if (licModule != null) {
                            totalLicensesCount = licModule.getLicenses();
                        }
                    }
                    Object[] bindObj = new Object[1];
                    bindObj[0] = moduleCode;
                    PbReturnObject moduleCtPbro = execSelectSQL(buildQuery(moduleCountQry, bindObj));
                    if (moduleCtPbro.getRowCount() > 0) {
                        moduleAssignCt = moduleCtPbro.getFieldValueInt(0, 0);
                    }
                    availableCt = totalLicensesCount - moduleAssignCt;
                    privlegeMap.put(moduleCode, availableCt);
                }
            }
            if (!privlegeMap.isEmpty()) {
                String keys[] = privlegeMap.keySet().toArray(new String[0]);
//                String notAvailableModules = "";
                StringBuilder notAvailableModules=new StringBuilder();
                ArrayList<String> notAvailLst = new ArrayList<String>();
                for (String key : keys) {
                    if (privlegeMap.get(key) == 0) {
                        notAvailLst.add(key);
                    }
                }
                if (notAvailLst.isEmpty()) {
                    message = "available";
                } else if (notAvailLst.size() == userTypeModules.size()) {

                    message = "All Licenses Exhausted for this User Type,Do you want to continue?";
                } else {
                    for (String moduleCode : notAvailLst) {
                        Iterator<LicenseModule> moduleIter = Iterables.filter(masterLicense.getLicenseModuleLst(), LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
                        if (moduleIter.hasNext()) {
                            licModule = moduleIter.next();
                            notAvailableModules.append(",").append(licModule.getModuleName());
                        }
                    }
                    notAvailableModules.append(notAvailableModules.substring(1));

                    message = notAvailableModules + " license/s Exhausted for this User type, Do you want to continue?";
                }
                session.setAttribute("ModulesNotAvail", notAvailLst);
            }


        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }


        return message;
    }

    public void saveModulesForUserType(HttpSession session, int userId, int userTypeId) {
//      deleteModulesForUserType(userId);
        ArrayList<String> notAvailModules = new ArrayList<String>();
        if (session.getAttribute("ModulesNotAvail") != null) {
            notAvailModules = (ArrayList<String>) session.getAttribute("ModulesNotAvail");
        }
        ArrayList<String> qryList = new ArrayList<String>();
        String insertQry = getResourceBundle().getString("insertUserTypeQry");
        String selectQry = getResourceBundle().getString("getModulesQry");
        Object bindObj[] = new Object[1];
        try {
            bindObj[0] = userTypeId;
            PbReturnObject selectRetObj = execSelectSQL(buildQuery(selectQry, bindObj));
            bindObj = new Object[3];
            for (int i = 0; i < selectRetObj.getRowCount(); i++) {

                if (!notAvailModules.contains(selectRetObj.getFieldValueString(i, "MODULE_CODE"))) {
                    bindObj[0] = userId;
                    bindObj[1] = selectRetObj.getFieldValueString(i, "MODULE_CODE");
                    bindObj[2] = selectRetObj.getFieldValueInt(i, "MODULE_ASSIGN_ID");
                    qryList.add(buildQuery(insertQry, bindObj));
                }
            }
            executeMultiple(qryList);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

    public void deleteModulesForUserType(int userId) {
        String qry = "delete from PRG_USER_TYPE_PRIVILEGE where USER_ID=" + userId;
        try {
            execUpdateSQL(qry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void deleteModulesForAllUsersOfUserType(int userType) {
        String qry = "delete from PRG_USER_TYPE_PRIVILEGE where USER_ID in( select PU_ID from PRG_AR_USERS where USER_TYPE=" + userType + " )";
        try {
            execUpdateSQL(qry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void savePrivilegesToAllUsersOfUserType(int userTypeId, String xmlData, HttpSession session) {
        String qry = "select PU_ID from PRG_AR_USERS where USER_TYPE=" + userTypeId;
        try {
            PbReturnObject pbro = execSelectSQL(qry);
            for (int i = 0; i < pbro.getRowCount(); i++) {
                checkUserPrivileges(userTypeId, xmlData, session);
                saveModulesForUserType(session, pbro.getFieldValueInt(i, "PU_ID"), userTypeId);
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

    }

    public void saveAssignedModules(int userId, int userType, String userName, String status) {
        String moduleAssignmentToUsers = getResourceBundle().getString("moduleAssignmentToUsers");
        String existingPrevilagedetails = "select * from PRG_TEST_DATA ";
        PbReturnObject pbro = null;
        PbDb pbdb = new PbDb();
        String finalQry = null;
        int existingRepStdCnt = 0;
        try {
            pbro = pbdb.execSelectSQL(existingPrevilagedetails);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        Object[] obj = new Object[21];
        obj[0] = userId;
        obj[1] = userName;
        if (userType == 10002) {
            obj[2] = 'Y';
            obj[3] = 'N';
            obj[4] = 'N';
            obj[5] = 'N';
            obj[6] = 'N';
            obj[7] = 'N';
            obj[8] = 'N';
            obj[9] = 'N';
            obj[10] = 'N';
            obj[11] = 'N';
            obj[12] = 'N';
            obj[13] = 'Y';
            obj[14] = 'Y';
            obj[17] = 'N';
            obj[18] = 'N';
            obj[20] = 'N';
        } else if (userType == 9999) {
            obj[2] = 'N';
            obj[3] = 'Y';
            obj[4] = 'Y';
            obj[5] = 'Y';
            obj[6] = 'N';
            obj[7] = 'Y';
            obj[8] = 'Y';
            obj[9] = 'Y';
            obj[10] = 'Y';
            obj[11] = 'Y';
            obj[12] = 'Y';
            obj[13] = 'Y';
            obj[14] = 'Y';
            obj[17] = 'Y';
            obj[18] = 'Y';
            obj[20] = 'N';

        } else if (userType == 10000) {
            obj[2] = 'N';
            obj[3] = 'N';
            obj[4] = 'Y';
            obj[5] = 'Y';
            obj[6] = 'N';
            obj[7] = 'Y';
            obj[8] = 'Y';
            obj[9] = 'Y';
            obj[10] = 'Y';
            obj[11] = 'Y';
            obj[12] = 'N';
            obj[13] = 'Y';
            obj[14] = 'Y';
            obj[17] = 'Y';
            obj[18] = 'Y';
            obj[20] = 'N';


        } else if (userType == 10001) {
            obj[2] = 'N';
            obj[3] = 'N';
            obj[4] = 'Y';
            obj[5] = 'Y';
            obj[6] = 'N';
            obj[7] = 'Y';
            obj[8] = 'Y';
            obj[9] = 'Y';
            obj[10] = 'Y';
            obj[11] = 'Y';
            obj[12] = 'N';
            obj[13] = 'Y';
            obj[14] = 'Y';
            obj[17] = 'Y';
            obj[18] = 'Y';
            obj[20] = 'Y';
        }



        obj[15] = userType;
        obj[16] = status;

        finalQry = buildQuery(moduleAssignmentToUsers, obj);
        try {
            pbdb.execUpdateSQL(finalQry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public static void main(String a[]) {
        PbEncrypter enc = new PbEncrypter();
        String xml = "<Modules> <Module><ModuleCode>PORTAL</ModuleCode><License>2</License><EnabledComponents></EnabledComponents> </Module><Module><ModuleCode>SCEDULER</ModuleCode><License>2</License><EnabledComponents></EnabledComponents></Module></Modules>";
        String userPassword = "PassWord";
        String message = "Some text which is to be encrypted by the user password. Later it is decrypted to the original plain-text using the same password.";

        //Encrypt
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(userPassword);
        String myEncryptedText = textEncryptor.encrypt(xml);


        //Decrypt
        BasicTextEncryptor textDecryptor = new BasicTextEncryptor();
        textDecryptor.setPassword("ProGen");
        String plainText = textDecryptor.decrypt("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");


    }
}
