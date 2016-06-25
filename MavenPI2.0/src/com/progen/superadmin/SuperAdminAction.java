/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import com.google.gson.Gson;
import com.progen.cache.MetadataCacheManager;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import com.progen.users.PrivilegeManager;
import com.progen.users.UserLayerDAO;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

/**
 *
 * @author progen
 */
public class SuperAdminAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(SuperAdminAction.class);

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("viewSuperAdmin", "viewSuperAdmin");
        map.put("viewComponentList", "viewComponentList");
        map.put("enableDisableModuleComponent", "enableDisableModuleComponent");
        map.put("generateXmlForSuperAdmin", "generateXmlForSuperAdmin");
        map.put("enableDisableModule", "enableDisableModule");
        map.put("initializeUserLicenseModules", "initializeUserLicenseModules");
        map.put("getModulesForUserAssignment", "getModulesForUserAssignment");
        map.put("getModuleComponentsForUserAssignment", "getModuleComponentsForUserAssignment");
        map.put("assignPrivilegesToUser", "assignPrivilegesToUser");
        map.put("enableDisableModuleForUser", "enableDisableModuleForUser");
        map.put("enableDisableModuleComponentForUser", "enableDisableModuleComponentForUser");
        map.put("enableDisableAllModuleComponentsForUser", "enableDisableAllModuleComponentsForUser");
        map.put("isComponentAvailableForModule", "isComponentAvailableForModule");
        map.put("enableDisableModuleChildComponent", "enableDisableModuleChildComponent");
        map.put("enableDisableModuleChildComponentForUser", "enableDisableModuleChildComponentForUser");
        map.put("showSuperAdminLicensePrivileges", "showSuperAdminLicensePrivileges");
        map.put("checkUserPrivileges", "checkUserPrivileges");
        map.put("checkReportPrivileges", "checkReportPrivileges");
        map.put("checkLoginId", "checkLoginId");
        map.put("getAllUserDetails", "getAllUserDetails");
        map.put("getPrivilageDetails", "getPrivilageDetails");
        map.put("getXmlData", "getXmlData");
        map.put("saveSubCompents", "saveSubCompents");
        map.put("showPortalAssignment", "showPortalAssignment");
        map.put("saveAssignments", "saveAssignments");
        map.put("getUserStatusDets", "getUserStatusDets");
        map.put("saveUserActivation", "saveUserActivation");
        map.put("validateUserActivation", "validateUserActivation");
        map.put("validateAssignments", "validateAssignments");
        map.put("usertypevalidate", "usertypevalidate");
        map.put("getRoleRelatedReports", "getRoleRelatedReports");
        map.put("saveNewGroup", "saveNewGroup");
        map.put("editGroupDetails", "editGroupDetails");
        map.put("showGroupDetails", "showGroupDetails");
        map.put("featureManagement", "featureManagement");
        map.put("getParamSelectedList", "getParamSelectedList");
        map.put("checkParallelUsage", "checkParallelUsage");
        return map;
    }

    public ActionForward viewSuperAdmin(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        SuperAdminBd adminBd = new SuperAdminBd();
        License license = new License();
        license = adminBd.getMasterLicense();
        session.setAttribute("license", license);
        InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/progenPrivate/superAdmin.xml");
//        InputStream inputstream = null;
        String xmlData = "";
        xmlData = adminBd.convertStreamToString(inputstream);

        String licenseJson = license.getLicenseModulesJSON(xmlData, request.getContextPath());
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
        }
        out.print(licenseJson);
        return null;
    }

    public ActionForward viewComponentList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        String moduleCode = request.getParameter("moduleCode");
        SuperAdminBd adminBd = new SuperAdminBd();
        License license = (License) session.getAttribute("license");
        ArrayList<LicenseModule> licenseModulesLst = license.getLicenseModuleLst();
        String modCompJson = "";
        InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/progenPrivate/superAdmin.xml");
        String xmlData = "";
        xmlData = adminBd.convertStreamToString(inputstream);
        ArrayList<LicenseModule> modulesLst = null;
        if (xmlData != null && !xmlData.equalsIgnoreCase("")) {
            modulesLst = adminBd.parseSuperAdminXml(xmlData);
        }
        ArrayList<String> moduleCodeLst = new ArrayList<String>();
        if (modulesLst != null) {
            for (LicenseModule licModule : modulesLst) {
                moduleCodeLst.add(licModule.getModuleCode());
            }
        }
        int index = 0;
        LicenseModule licModule = null;
        if (moduleCodeLst.contains(moduleCode)) {
            index = moduleCodeLst.indexOf(moduleCode);
            licModule = modulesLst.get(index);
        }
        ArrayList<ModuleComponent> assModuleCompLst = null;
        if (licModule != null) {
            assModuleCompLst = licModule.getModuleComponentsLst();
        }

        for (int i = 0; i < licenseModulesLst.size(); i++) {
            if (licenseModulesLst.get(i).getModuleCode().equalsIgnoreCase(moduleCode)) {
                modCompJson = licenseModulesLst.get(i).getModuleComponentsJSON(assModuleCompLst);
            }
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
        }
        out.print(modCompJson);
        return null;
    }

//    public ActionForward enableDisableModule(ActionMapping mapping, ActionForm form,
//            HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(false);
//        String moduleCode = request.getParameter("moduleCode");
//        String status = request.getParameter("status");
//        String from = request.getParameter("from");
//        License license;
//        boolean isEnable = false;
//        if (status.equalsIgnoreCase("true")) {
//            isEnable = true;
//        } else {
//            isEnable = false;
//        }
//
//        license = (License) session.getAttribute("license");
//
//        ArrayList<LicenseModule> licenseModulesLst = license.getLicenseModuleLst();
//        for (int i = 0; i < licenseModulesLst.size(); i++) {
//            if (licenseModulesLst.get(i).getModuleCode().equalsIgnoreCase(moduleCode)) {
//                licenseModulesLst.get(i).setEnable(isEnable);
//            }
//        }
//        return null;
//    }
    public ActionForward enableDisableModule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String moduleCode = request.getParameter("moduleCode");
        String status = request.getParameter("status");
        License license;
        boolean isEnable = false;
        if (status.equalsIgnoreCase("true")) {
            isEnable = true;
        } else {
            isEnable = false;
        }

        license = (License) session.getAttribute("license");
        license.enableDisableModule(moduleCode, isEnable);
        return null;
    }

//    public ActionForward enableDisableModuleComponent(ActionMapping mapping, ActionForm form,
//            HttpServletRequest request, HttpServletResponse response) {
//        String chkBoxId = request.getParameter("chkBoxId");
//        String status = request.getParameter("status");
//        String moduleCode = request.getParameter("moduleCode");
//        HttpSession session = request.getSession(false);
//        String from = request.getParameter("from");
//        License license;
//
//        license = (License) session.getAttribute("license");
//
//
//        ArrayList<LicenseModule> licenseModules = license.getLicenseModuleLst();
//
//        boolean isEnable = true;
//        if (status.equalsIgnoreCase("true")) {
//            isEnable = true;
//        } else {
//            isEnable = false;
//        }
//        ArrayList<ModuleComponent> moduleCompsLst;
//        for (int i = 0; i < licenseModules.size(); i++) {
//            moduleCompsLst = licenseModules.get(i).getModuleComponentsLst();
//            for (int j = 0; j < moduleCompsLst.size(); j++) {
//                if (licenseModules.get(i).getModuleCode().equalsIgnoreCase(moduleCode) && moduleCompsLst.get(j).getParentCompCode().equalsIgnoreCase(chkBoxId)) {
//
//                    moduleCompsLst.get(j).setEnable(isEnable);
//
//
//                } else if (licenseModules.get(i).getModuleCode().equalsIgnoreCase(moduleCode) && moduleCompsLst.get(j).getCompCode().equalsIgnoreCase(chkBoxId)) {
//                    moduleCompsLst.get(j).setEnable(isEnable);
//                }
//            }
//        }
//        return null;
//    }
    public ActionForward enableDisableModuleComponent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String compCode = request.getParameter("compCode");
        String status = request.getParameter("status");
        String moduleStatus = request.getParameter("moduleStatus");
        String moduleCode = request.getParameter("moduleCode");
        HttpSession session = request.getSession(false);
        String from = request.getParameter("from");
        License license;

        license = (License) session.getAttribute("license");
        boolean isEnable = true;
        boolean isModuleEnable = false;
        if (status.equalsIgnoreCase("true")) {
            isEnable = true;
        } else {
            isEnable = false;
        }

        if (moduleStatus.equalsIgnoreCase("true")) {
            isModuleEnable = true;
        } else {
            isModuleEnable = false;
        }

        license.enableDisableModuleComponent(moduleCode, compCode, isEnable, isModuleEnable);
        return null;
    }

    public ActionForward enableDisableModuleChildComponent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String compCode = request.getParameter("compCode");
        String status = request.getParameter("status");
        String childCompCode = request.getParameter("childCompCode");
        String moduleStatus = request.getParameter("moduleStatus");
        String compStatus = request.getParameter("compStatus");
        String moduleCode = request.getParameter("moduleCode");
        HttpSession session = request.getSession(false);
        String from = request.getParameter("from");
        License license;

        license = (License) session.getAttribute("license");
        boolean isEnable = true;
        boolean isModuleEnable = false;
        boolean isCompEnable = false;
        if (status.equalsIgnoreCase("true")) {
            isEnable = true;
        } else {
            isEnable = false;
        }

        if (compStatus.equalsIgnoreCase("true")) {
            isCompEnable = true;
        } else {
            isCompEnable = false;
        }

        if (moduleStatus.equalsIgnoreCase("true")) {
            isModuleEnable = true;
        } else {
            isModuleEnable = false;
        }

        license.enableDisableModuleChildComponent(moduleCode, compCode, childCompCode, isEnable, isModuleEnable, isCompEnable);
        return null;

    }

    public ActionForward enableDisableModuleForUser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String status = request.getParameter("status");
        String moduleCode = request.getParameter("moduleCode");
        String userId = request.getParameter("userId");
        boolean isEnable = true;
        if (status.equalsIgnoreCase("true")) {
            isEnable = true;
        } else {
            isEnable = false;
        }
        UserLicense userLicense = UserLicense.getInstance();
        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);

        SuperAdminBd adminBd = new SuperAdminBd();
        if (isEnable) {
            adminBd.assignAllModuleCompsToUser(moduleCode, userAssg, userLicense);
        } else {
            adminBd.revokeAllModuleCompsFromUser(moduleCode, userAssg, userLicense);
        }

        return null;
    }

//    public ActionForward enableDisableModuleComponentForUser(ActionMapping mapping, ActionForm form,
//            HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(false);
//        String compCode = request.getParameter("compCode");
//        String status = request.getParameter("status");
//        String moduleCode = request.getParameter("moduleCode");
//        String parCompCode = request.getParameter("parCompCode");
//
//        String userId = request.getParameter("userId");
//        boolean isEnable = true;
//        if (status.equalsIgnoreCase("true")) {
//            isEnable = true;
//        } else {
//            isEnable = false;
//        }
//        UserLicense userLicense = UserLicense.getInstance();
//
//        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
//        SuperAdminBd adminBd = new SuperAdminBd();
//        if (isEnable) {
//            adminBd.assignModuleComponentToUser(moduleCode, userAssg, compCode, parCompCode);
//        } else {
//            adminBd.revokeModuleComponentFromUser(moduleCode, userAssg, compCode, parCompCode);
//        }
//        return null;
//    }
    public ActionForward enableDisableModuleChildComponentForUser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String compCode = request.getParameter("compCode");
        String status = request.getParameter("status");
        String moduleCode = request.getParameter("moduleCode");
        String moduleStatus = request.getParameter("moduleStatus");
        String compStatus = request.getParameter("compStatus");
        String childCompCode = request.getParameter("childCompCode");
        String userId = request.getParameter("userId");
        boolean isEnable = true;
        boolean isModuleEnable = false;
        boolean isComponentEnable = false;

        if (status.equalsIgnoreCase("true")) {
            isEnable = true;
        } else {
            isEnable = false;
        }

        if (moduleStatus.equalsIgnoreCase("true")) {
            isModuleEnable = true;
        } else {
            isModuleEnable = false;
        }

        if (compStatus.equalsIgnoreCase("true")) {
            isComponentEnable = true;
        } else {
            isComponentEnable = false;
        }
        UserLicense userLicense = UserLicense.getInstance();

        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
        SuperAdminBd adminBd = new SuperAdminBd();
        if (isModuleEnable) {
            adminBd.assignModuleToUser(moduleCode, userAssg, userLicense);
            if (isComponentEnable) {
                ModuleComponent comp = new ModuleComponent();
                comp.setCompCode(compCode);
                adminBd.assignModuleComponentToUser(userAssg, comp, moduleCode);
                if (isEnable) {
                    ModuleComponent childComp = new ModuleComponent();
                    childComp.setCompCode(childCompCode);
                    childComp.setParentCompCode(compCode);
                    adminBd.assignModuleComponentToUser(userAssg, comp, moduleCode);
                } else {
                    ModuleComponent childComp = new ModuleComponent();
                    childComp.setCompCode(childCompCode);
                    childComp.setParentCompCode(compCode);
                    adminBd.revokeModuleComponentFromUser(userAssg, comp, moduleCode);
                }
            } else {
                ModuleComponent comp = new ModuleComponent();
                comp.setCompCode(compCode);
                adminBd.revokeModuleComponentFromUser(userAssg, comp, moduleCode);
            }
        } else {
            adminBd.revokeModuleFromUser(moduleCode, userAssg, userLicense);
        }


        return null;
    }

    public ActionForward enableDisableModuleComponentForUser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String compCode = request.getParameter("compCode");
        String status = request.getParameter("status");
        String moduleCode = request.getParameter("moduleCode");
        String moduleStatus = request.getParameter("moduleStatus");

        String userId = request.getParameter("userId");
        boolean isEnable = true;
        boolean isModuleEnable = false;
        if (status.equalsIgnoreCase("true")) {
            isEnable = true;
        } else {
            isEnable = false;
        }
        if (moduleStatus.equalsIgnoreCase("true")) {
            isModuleEnable = true;
        } else {
            isModuleEnable = false;
        }
        UserLicense userLicense = UserLicense.getInstance();

        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
        SuperAdminBd adminBd = new SuperAdminBd();
        if (isModuleEnable) {
            adminBd.assignModuleToUser(moduleCode, userAssg, userLicense);
            if (isEnable) {
                ModuleComponent comp = new ModuleComponent();
                comp.setCompCode(compCode);
                adminBd.assignModuleComponentToUser(userAssg, comp, moduleCode);
                ArrayList<ModuleComponent> moduleCompsLst = userLicense.getChildModuleComponents(moduleCode, compCode);

                for (ModuleComponent modComp : moduleCompsLst) {
                    adminBd.assignModuleComponentToUser(userAssg, modComp, moduleCode);
                }
            } else {
                ModuleComponent comp = new ModuleComponent();
                comp.setCompCode(compCode);
                adminBd.revokeModuleComponentFromUser(userAssg, comp, moduleCode);
            }
        } else {
            adminBd.revokeModuleFromUser(moduleCode, userAssg, userLicense);
        }


        return null;
    }

    public ActionForward enableDisableAllModuleComponentsForUser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String moduleCode = request.getParameter("moduleCode");
        String userId = request.getParameter("userId");
        // SuperAdminBd adminBd = new SuperAdminBd();
        String status = request.getParameter("status");
        boolean isEnable = true;
        if (status.equalsIgnoreCase("true")) {
            isEnable = true;
        } else {
            isEnable = false;
        }
        UserLicense userLicense = UserLicense.getInstance();
        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
        ArrayList<ModuleComponent> moduleCompsLst = userLicense.getParentModuleComponents(moduleCode);
        ArrayList<ModuleComponent> childModuleCompsLst = userLicense.getAllChildModuleComponents(moduleCode);

        if (isEnable) {
            for (ModuleComponent moduleComp : moduleCompsLst) {
                userAssg.assignModuleToUser(moduleComp, moduleCode);
            }
            for (ModuleComponent childModComp : childModuleCompsLst) {
                userAssg.assignChildModuleToUser(childModComp, moduleCode);
            }
        } else {
            for (ModuleComponent moduleComp : moduleCompsLst) {
                userAssg.revokeModuleFromUser(moduleComp, moduleCode);
            }
            for (ModuleComponent childModComp : childModuleCompsLst) {
                userAssg.revokeChildModuleFromUser(childModComp, moduleCode);
            }
        }
        return null;
    }

    public ActionForward generateXmlForSuperAdmin(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        SuperAdminBd adminBd = new SuperAdminBd();
        License license = (License) session.getAttribute("license");
        HashMap<String, Integer> licenseMap = new HashMap<String, Integer>();
        ArrayList<LicenseModule> licenseModuleLst = license.getLicenseModuleLst();
        ArrayList<ModuleComponent> moduleCompLst;
        PrintWriter out = null;
        String moduleCode = "";
        int modLicCt;
        for (int i = 0; i < licenseModuleLst.size(); i++) {
            moduleCode = licenseModuleLst.get(i).getModuleCode();
            if (request.getParameter("txt" + moduleCode) != null && !(request.getParameter("txt" + moduleCode)).equalsIgnoreCase("")) {
                modLicCt = Integer.parseInt(request.getParameter("txt" + moduleCode));
            } else {
                modLicCt = 0;
            }
            licenseModuleLst.get(i).setLicenses(modLicCt);
        }

        String xmlData = license.toXml();
        String path = this.getServlet().getServletContext().getRealPath("/") + "progenPrivate".replace("\\.\\", "\\") + "/superAdmin.xml";
//        String path = request.getSession(false).getServletContext().getRealPath("/").replace("/build", "").replace("\\build", "").replace("\\","/") + "WEB-INF/superAdmin.xml";
//        path="C:/pi_may4/web/WEB-INF/superAdmin.xml";
        EncryptDecryptXml enc = new EncryptDecryptXml();
        xmlData = enc.encryptXml(xmlData);
        adminBd.createXmlDocument(path, xmlData);
//        UserLicense.getInstance().removeUserLicense();
        return null;
    }

    public ActionForward initializeUserLicenseModules(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        PrintWriter out = out = response.getWriter();
        InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/progenPrivate/superAdmin.xml");
        String xmlData = "";
        SuperAdminBd adminBd = new SuperAdminBd();
        xmlData = adminBd.convertStreamToString(inputstream);
        String userTypeId = request.getParameter("userTypeId");

        UserLicense.setLincenseXmlData(xmlData);
        UserLicense userLicense = UserLicense.getInstance();
        UserAssignment userAssg = adminBd.createUserAssigmentObject(Integer.parseInt(userTypeId), "userType");
        session.setAttribute("Assg_" + userTypeId, userAssg);

        return null;
    }

    public ActionForward getModulesForUserAssignment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");

        SuperAdminBd adminBd = new SuperAdminBd();
        String licenseJson = "";

        UserLicense userLicense = UserLicense.getInstance();
        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
        SuperAdminBuilder builder = new SuperAdminBuilder();
        boolean isModulesAvailable = false;
        for (LicenseModule module : userLicense.getLicenseModules()) {
            if (module.isEnable()) {
                isModulesAvailable = true;
                break;
            }
        }
        if (isModulesAvailable) {
            licenseJson = builder.generateHtmlForUser(userLicense, userAssg, request.getContextPath());
        }
//        licenseJson = adminBd.getLicenseModuleJSONForUser(userLicense, userAssg);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
        }
        out.print(licenseJson);
        return null;
    }

    public ActionForward getModuleComponentsForUserAssignment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String moduleCode = request.getParameter("moduleCode");
        String userId = request.getParameter("userId");
        SuperAdminBd adminBd = new SuperAdminBd();
        UserLicense userLicense = UserLicense.getInstance();
        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
        String modCompJson = adminBd.getModuleComponentsJSONforUser(userLicense, userAssg, moduleCode);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
        }
        out.print(modCompJson);
        return null;
    }

    public ActionForward assignPrivilegesToUser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        int userId = Integer.parseInt(request.getParameter("userId"));
        SuperAdminBd adminBd = new SuperAdminBd();
        HttpSession session = request.getSession(false);
        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
        UserLicense userLicense = UserLicense.getInstance();
        adminBd.deleteAllPrivilegesToUser(userId);
        adminBd.savePrivilegesToUser(userLicense, userAssg, userId);


        InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/superAdmin.xml");
        String xmlData = "";
        try {
            xmlData = adminBd.convertStreamToString(inputstream);
            adminBd.deleteModulesForAllUsersOfUserType(userId);
            adminBd.savePrivilegesToAllUsersOfUserType(userId, xmlData, session);
            MetadataCacheManager.MANAGER.removeUserAssignment(userId);
            userLicense.removeUserLicense();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward isComponentAvailableForModule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String moduleCode = request.getParameter("moduleCode");
        SuperAdminBd adminBd = new SuperAdminBd();
        HttpSession session = request.getSession(false);
        UserAssignment userAssg = (UserAssignment) session.getAttribute("Assg_" + userId);
        UserLicense userLicense = UserLicense.getInstance();
        boolean isAvailable = userLicense.isCompAvailableForModule(moduleCode);
        // boolean isAvailable=adminBd.isCompsAvailForModule(userLicense, moduleCode);
        try {
            response.getWriter().print(isAvailable);
        } catch (IOException ex) {
        }
        return null;
    }

    public ActionForward showSuperAdminLicensePrivileges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        SuperAdminBd adminBd = new SuperAdminBd();
        InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/superAdmin.xml");
        String xmlData = "";
        String html = "";
        try {
            xmlData = adminBd.convertStreamToString(inputstream);
            html = adminBd.showLicensePrivileges(xmlData);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        response.getWriter().print(html);
        return null;
    }

    public ActionForward checkUserPrivileges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        SuperAdminBd adminBd = new SuperAdminBd();
        String userTypeId = request.getParameter("userTypeId");
        HttpSession session = request.getSession(false);
        InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/superAdmin.xml");
        String xmlData = "";
        String message = "";
        try {
            xmlData = adminBd.convertStreamToString(inputstream);
            message = adminBd.checkUserPrivileges(Integer.parseInt(userTypeId), xmlData, session);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        response.getWriter().print(message);
        return null;
    }

    public ActionForward checkReportPrivileges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        String message = "";
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        String userId = (String) request.getSession(false).getAttribute("USERID");
        if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))
                || PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))
                || PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId))) {

            message = "true";

        } else {
            message = "false";
        }
        out.print(message);
        return null;
    }

    public ActionForward checkLoginId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String loginId = request.getParameter("loginId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        PrintWriter out = null;
        boolean flag = false;
        flag = userLayerDAO.checkLoginId(loginId);
        try {
            out = response.getWriter();
            out.print(flag);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward getAllUserDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String userId = request.getParameter("userId");
        String modulesListOfJson = userLayerDAO.getAllUserDetails(userId);
        PrintWriter out = response.getWriter();
        out.print(modulesListOfJson);

        return null;
    }

    public ActionForward getPrivilageDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String previlageName = request.getParameter("previlageName");
        String previlageid = request.getParameter("previlageid");
        String modulesListOfJson = userLayerDAO.getPrivilageDetails(previlageName, previlageid);
//         HashMap<String,ArrayList> hlist = new HashMap<String, ArrayList>();
//         ArrayList<String> alist = new ArrayList();
//         String[] varstr  =  modulesListOfJson.split(",");
//         for(int i=0;i<varstr.length;i++){
//             alist.add(varstr[i]);
//         }
//         alist.addAll(Arrays.asList(modulesListOfJson)); 
//         GenerateDragAndDrophtml dragAndDrophtml=new GenerateDragAndDrophtml("Drag sub-privileges from here", "Drop sub-privileges here", null,alist, request.getContextPath());
//         dragAndDrophtml.setDragableListNames(alist);
//         dragAndDrophtml.setDropedmesNames(alist);
//         String htmlJson=dragAndDrophtml.getDragAndDropDiv();
        try {
            response.getWriter().print(modulesListOfJson);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getXmlData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
//         SuperAdminBd adminBd = new SuperAdminBd();
//         InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/superAdmin/superAdmin.xml");
//         String xmlData="";
        Integer value = 0;
//         xmlData = adminBd.convertStreamToString(inputstream);
//         value = Integer.parseInt(xmlData);
        response.getWriter().print(value);
        return null;
    }

    public ActionForward saveSubCompents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
//          UserLayerDAO userLayerDAO=new UserLayerDAO();
        String moduleID = request.getParameter("moduleID");

        String previlagesDetails = request.getParameter("previlagesDetails");
        String details = request.getParameter("details");
        String preViName = request.getParameter("preViName");
//        try {
//            //         String modulesListOfJson=userLayerDAO.saveSubCompents(previlageDetailsId,previlageFunction,previlageid);
//                       JSONArray jSONArray = (JSONArray)new JSONParser().parse(details);
//                       for(int i=0;i<jSONArray.size();i++){
//                           
//                           JSONObject json = (JSONObject)new JSONParser().parse(jSONArray.get(i).toString());
//                           
//                          JSONArray jSONArray1=(JSONArray) json.get("optionValue");
//                           
//                       }
//        } catch (ParseException ex) {
//            logger.error("Exception:",ex);
//        }
//           

        return null;
    }

    public ActionForward getRoleRelatedReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String folderId = request.getParameter("folderId");
        String userId = request.getParameter("userId");
        UserLayerDAO dao = new UserLayerDAO();
        PbReturnObject retObj = dao.getRoleRelatedReports(folderId);
        HashMap<String, String> map = new HashMap<String, String>();
        if (retObj.getRowCount() > 0) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                map.put(retObj.getFieldValueString(i, 0), retObj.getFieldValueString(i, 1));
            }
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        response.getWriter().print(jsonString);
        return null;
    }

    public ActionForward showPortalAssignment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String moduleName = "";
        moduleName = request.getParameter("moduleName");
        String[] userIds;
        String[] userNames;
        HashMap usridsandnames = new HashMap();
        HashMap membersAssigned = new HashMap();
        UserLayerDAO dao = new UserLayerDAO();
        usridsandnames = dao.getUserIdsAndNames(moduleName);
        if (!moduleName.equalsIgnoreCase("createGroup") && !moduleName.equalsIgnoreCase("Groups")) {
            membersAssigned = dao.getAssignedUsers(moduleName);
        }
        GenerateDragAndDrophtml generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", (ArrayList<String>) membersAssigned.get("memAssignedIds"), (ArrayList<String>) usridsandnames.get("userids"), request.getContextPath());
        generateDragAndDrophtml.setDragableListNames((ArrayList<String>) usridsandnames.get("usernames"));
        if (!moduleName.equalsIgnoreCase("createGroup") && !moduleName.equalsIgnoreCase("Groups")) {
            generateDragAndDrophtml.setDropedmesNames((ArrayList<String>) membersAssigned.get("memAssignedNames"));
        }
        String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
        response.getWriter().print(dragndrop);
        return null;
    }

    public ActionForward saveAssignments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String modulename = request.getParameter("moduleName");
        String userIdstr = request.getParameter("UserIds");
        String userNamesstr = request.getParameter("userNames");
        String[] userId = null;
        String[] userName = null;
        userId = userIdstr.split(",");
        userName = userNamesstr.split(",");
        UserLayerDAO dao = new UserLayerDAO();
        dao.updateUserModuleAssignment(userId, userNamesstr, modulename);

        return null;
    }

    public ActionForward getUserStatusDets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userStatus = null;
        userStatus = request.getParameter("status");
        UserLayerDAO dao = new UserLayerDAO();
        if (userStatus != null && userStatus.equalsIgnoreCase("activateUser")) {
            ArrayList<String> activeUsers = null;
            HashMap inactiveUsersMap = new HashMap();
            activeUsers = dao.getUsersList(userStatus);
            inactiveUsersMap = dao.getUsersMap(userStatus);
            GenerateDragAndDrophtml generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", activeUsers, (ArrayList<String>) inactiveUsersMap.get("userIds"), request.getContextPath());
            generateDragAndDrophtml.setDragableListNames((ArrayList<String>) inactiveUsersMap.get("usernames"));

            String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
            response.getWriter().print(dragndrop);
        }
        if (userStatus != null && userStatus.equalsIgnoreCase("inactivateUser")) {
            ArrayList<String> inactivateUsers = new ArrayList<String>();
            HashMap activeUsersMap = new HashMap();
            inactivateUsers = dao.getUsersList(userStatus);
            activeUsersMap = dao.getUsersMap(userStatus);
            GenerateDragAndDrophtml generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", inactivateUsers, (ArrayList<String>) activeUsersMap.get("userIds"), request.getContextPath());
            generateDragAndDrophtml.setDragableListNames((ArrayList<String>) activeUsersMap.get("usernames"));

            String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
            response.getWriter().print(dragndrop);
        }

        return null;
    }

    public ActionForward saveUserActivation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userStatus = null;
        userStatus = request.getParameter("status");
        String userIds = request.getParameter("UserIds");
        String userNames = request.getParameter("userNames");
        String[] userId = null;
        String[] userName = null;
        userId = userIds.split(",");
        userName = userNames.split(",");
        UserLayerDAO dao = new UserLayerDAO();
        dao.updateUserStatus(userStatus, userId, userNames);

        return null;
    }

    public ActionForward validateUserActivation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userIds = "";
        String userNames = "";

        userIds = request.getParameter("UserIds");
        userNames = request.getParameter("userNames");

        String[] userId = null;
        String[] userName = null;
        userId = userIds.split(",");
        userName = userNames.split(",");
        UserLayerDAO dao = new UserLayerDAO();
        Boolean validate = dao.validateUserActivation(userNames);
        response.getWriter().print(validate);
        return null;
    }

    public ActionForward validateAssignments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userIds = "";
        String userNames = "";
        String modulename = "";

        userIds = request.getParameter("UserIds");
        userNames = request.getParameter("userNames");
        modulename = request.getParameter("moduleName");

        String[] userId = null;
        String[] userName = null;
        userId = userIds.split(",");
        userName = userNames.split(",");
        UserLayerDAO dao = new UserLayerDAO();
        Boolean validate = dao.validateUserAssignment(userNames, modulename, request);
        response.getWriter().print(validate);
        return null;
    }

    public ActionForward usertypevalidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String usertype = request.getParameter("Usertype");
        String Userid = request.getParameter("Userid");
        String oldusertype = request.getParameter("oldusertype");
        boolean testfor = false;
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        testfor = userLayerDAO.checkusertype(usertype, Userid, oldusertype);
        response.getWriter().print(testfor);
        return null;
    }
    //by gopesh for user group

    public ActionForward showGroupDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String moduleName = "";
        moduleName = request.getParameter("moduleName");
        HashMap usridsandnames = new HashMap();
        HashMap membersAssigned = new HashMap();
        UserLayerDAO dao = new UserLayerDAO();
        String groupId = request.getParameter("groupId");
        usridsandnames = dao.getUserIdsAndNames(moduleName);
        membersAssigned = dao.getAssignedUsersForGroup(groupId);
        GenerateDragAndDrophtml generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", (ArrayList<String>) membersAssigned.get("memAssignedIds"), (ArrayList<String>) usridsandnames.get("userids"), request.getContextPath());
        generateDragAndDrophtml.setDragableListNames((ArrayList<String>) usridsandnames.get("usernames"));
        generateDragAndDrophtml.setDropedmesNames((ArrayList<String>) membersAssigned.get("memAssignedNames"));
        String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
        response.getWriter().print(dragndrop);
        return null;
    }
    //by gopesh for save new user group

    public ActionForward saveNewGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String userIdstr = request.getParameter("UserIds");
        String userNamesstr = request.getParameter("userNames");
        String groupName = request.getParameter("groupName");
        String[] userId = null;
        String[] userName = null;
        userId = userIdstr.split(",");
        userName = userNamesstr.split(",");
        UserLayerDAO dao = new UserLayerDAO();
        dao.saveNewGroup(userIdstr, userNamesstr, groupName, userId, userName);
        return null;
    }
    //by gopesh

    public ActionForward editGroupDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String userIdstr = request.getParameter("UserIds");
        String userNamesstr = request.getParameter("userNames");
        String groupId = request.getParameter("groupId");
        String groupName = request.getParameter("groupName");
        String[] userId = null;
        String[] userName = null;
        userId = userIdstr.split(",");
        userName = userNamesstr.split(",");
        UserLayerDAO dao = new UserLayerDAO();
        dao.editGroupDetails(userIdstr, userNamesstr, groupId, userId, userName, groupName);
        return null;
    }

    //by gopesh
    public ActionForward featureManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        UserLayerDAO userdao = new UserLayerDAO();
        PbDb pbdb = new PbDb();
        String[] paramList = userdao.paramList;
        String[] graphList = userdao.graphList;
        String[] tableList = userdao.tableList;
        String[] actionList = userdao.actionList;
        ArrayList nameList = new ArrayList();
        ArrayList CAnalyzer = new ArrayList();
        ArrayList CPAnalyzer = new ArrayList();
        ArrayList sectionList = new ArrayList();
        ArrayList sections = new ArrayList();
        String moduleName = request.getParameter("moduleName");
        String groupIds = request.getParameter("groupIds");

        int USERID = 0;
        String userType = null;
        HttpSession session = request.getSession(false);
        if (session.getAttribute("USERID") != null) {
            USERID = Integer.parseInt((String) session.getAttribute("USERID"));
        }
        userType = userdao.getUserTypeForFeatures(USERID);


        boolean PA = false;
        boolean PPA = false;
        boolean CA = false;
        boolean CPA = false;
        boolean CCA = false;
        boolean CCPA = false;
        for (int i = 0; i < paramList.length; i++) {
            PA = false;
            PPA = false;
            if (request.getParameter(paramList[i]) != null) {
                PA = true;
            }
            if (request.getParameter("P_" + paramList[i]) != null) {
                PPA = true;
            }
            nameList.add(paramList[i]);
            sectionList.add("Parameter Section");
            sections.add("Parameter");
            CAnalyzer.add(PA);
            CPAnalyzer.add(PPA);
        }
        for (int i = 0; i < graphList.length; i++) {
            PA = false;
            PPA = false;
            if (request.getParameter(graphList[i]) != null) {
                PA = true;
            }
            if (request.getParameter("P_" + graphList[i]) != null) {
                PPA = true;
            }
            nameList.add(graphList[i]);
            sectionList.add("Graph Section");
            sections.add("Graph");
            CAnalyzer.add(PA);
            CPAnalyzer.add(PPA);
        }
        for (int i = 0; i < tableList.length; i++) {
            PA = false;
            PPA = false;
            if (request.getParameter(tableList[i]) != null) {
                PA = true;
            }
            if (request.getParameter("P_" + tableList[i]) != null) {
                PPA = true;
            }
            nameList.add(tableList[i]);
            sectionList.add("Table Section");
            sections.add("Table");
            CAnalyzer.add(PA);
            CPAnalyzer.add(PPA);
        }
        for (int i = 0; i < actionList.length; i++) {
            PA = false;
            PPA = false;
            if (request.getParameter(actionList[i]) != null) {
                PA = true;
            }
            if (request.getParameter("P_" + actionList[i]) != null) {
                PPA = true;
            }
            nameList.add(actionList[i]);
            sectionList.add("Actions");
            sections.add("Action");
            CAnalyzer.add(PA);
            CPAnalyzer.add(PPA);
        }

        if (nameList != null && userType.equalsIgnoreCase("SUPERADMIN") && groupIds.isEmpty()) {
            PA = true;
            PPA = true;
            String deleteQuery = "delete from PRG_AR_FEATURE_ASSIGNMENT where FEATURE_GROUP='" + moduleName + "'";
            pbdb.execUpdateSQL(deleteQuery);
            ArrayList queryList = new ArrayList();
            for (int i = 0; i < nameList.size(); i++) {
                int seq = i + 1;
                String query = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query = "insert into PRG_AR_FEATURE_ASSIGNMENT (SEQUENCE, FEATURE_GROUP, SUB_GROUP, FEATURE_TYPE, FEATURE_NAME, PROGEN_ANALYZER, PROGEN_POWER_ANALYZER, CUSTOMER_ANALYZER, "
                            + "CUSTOMER_POWER_ANALYZER, CUSTOMER_CUSTOMER_ANALYZER, CUSTOMER_CUSTOMER_POWER)"
                            + " values(" + seq + ",'" + moduleName + "','" + sectionList.get(i) + "','" + sections.get(i) + "','" + nameList.get(i) + "'," + CAnalyzer.get(i) + "," + CPAnalyzer.get(i) + "," + CAnalyzer.get(i) + "," + CPAnalyzer.get(i) + "," + CAnalyzer.get(i) + "," + CPAnalyzer.get(i) + ")";
                    queryList.add(query);
                } else {
                    query = "insert into PRG_AR_FEATURE_ASSIGNMENT (SEQUENCE, FEATURE_GROUP, SUB_GROUP, FEATURE_TYPE, FEATURE_NAME, PROGEN_ANALYZER, PROGEN_POWER_ANALYZER, CUSTOMER_ANALYZER, "
                            + "CUSTOMER_POWER_ANALYZER, CUSTOMER_CUSTOMER_ANALYZER, CUSTOMER_CUSTOMER_POWER)"
                            + " values(" + seq + ",'" + moduleName + "','" + sectionList.get(i) + "','" + sections.get(i) + "','" + nameList.get(i) + "','" + CAnalyzer.get(i) + "','" + CPAnalyzer.get(i) + "','" + CAnalyzer.get(i) + "','" + CPAnalyzer.get(i) + "','" + CAnalyzer.get(i) + "','" + CPAnalyzer.get(i) + "')";
                    queryList.add(query);
                }
            }
            pbdb.executeMultiple(queryList);
        } else if (nameList != null && userType.equalsIgnoreCase("ADMIN") && groupIds.isEmpty()) {

            for (int i = 0; i < nameList.size(); i++) {
                String query = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query = "update PRG_AR_FEATURE_ASSIGNMENT set CUSTOMER_POWER_ANALYZER=" + CPAnalyzer.get(i) + ",CUSTOMER_CUSTOMER_ANALYZER=" + CAnalyzer.get(i) + ","
                            + " CUSTOMER_CUSTOMER_POWER=" + CPAnalyzer.get(i) + ",CUSTOMER_ANALYZER=" + CAnalyzer.get(i) + " where FEATURE_NAME=" + "'" + nameList.get(i) + "'";
                } else {
                    query = "update PRG_AR_FEATURE_ASSIGNMENT set CUSTOMER_POWER_ANALYZER='" + CPAnalyzer.get(i) + "',CUSTOMER_CUSTOMER_ANALYZER='" + CAnalyzer.get(i) + "',"
                            + "CUSTOMER_CUSTOMER_POWER='" + CPAnalyzer.get(i) + "',CUSTOMER_ANALYZER='" + CAnalyzer.get(i) + "'where FEATURE_NAME=" + "'" + nameList.get(i) + "'";
                }
                pbdb.execInsert(query);
            }
        } else if (nameList != null && !groupIds.isEmpty()) {

            String[] groupId = null;
            String[] userName = null;
            groupId = groupIds.split(",");
            for (int j = 0; j < groupId.length; j++) {
                PbReturnObject retObj = null;
                String querySelect = "select * from PRG_AR_GROUP_FEATURE_ASSIGN where GROUP_ID=" + groupId[j];
                retObj = pbdb.execSelectSQL(querySelect);
                if (retObj != null) {
                    String deleteQuery = "delete from PRG_AR_GROUP_FEATURE_ASSIGN where GROUP_ID=" + groupId[j];
                    pbdb.execUpdateSQL(deleteQuery);
                }
                for (int i = 0; i < nameList.size(); i++) {
                    String query = null;
                    int seq = i + 1;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        query = "insert into PRG_AR_GROUP_FEATURE_ASSIGN (SEQUENCE, GROUP_ID, FEATURE_GROUP, SUB_GROUP, FEATURE_TYPE, FEATURE_NAME, ANALYZER)"
                                + " values(" + seq + "," + groupId[j] + ",'" + moduleName + "','" + sectionList.get(i) + "','" + sections.get(i) + "','" + nameList.get(i) + "'," + CAnalyzer.get(i) + ")";
                    } else {
                        query = "insert into PRG_AR_GROUP_FEATURE_ASSIGN (SEQUENCE, GROUP_ID, FEATURE_GROUP, SUB_GROUP, FEATURE_TYPE, FEATURE_NAME, ANALYZER)"
                                + " values(" + seq + "," + groupId[j] + ",'" + moduleName + "','" + sectionList.get(i) + "','" + sections.get(i) + "','" + nameList.get(i) + "','" + CAnalyzer.get(i) + "')";
                    }
                    pbdb.execInsert(query);
                }
            }
        }
        return null;
    }

    public ActionForward checkParallelUsage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        PbDb pbdb = new PbDb();
        String ParallelUsage = null;
        HttpSession session = request.getSession(false);
//     String userId = String.valueOf(session.getAttribute("USERID"));
//     PbReturnObject pbpretobj1 = null;

        ParallelUsage = String.valueOf(session.getAttribute("ParallelUsage"));
        if (ParallelUsage.equalsIgnoreCase("Y")) {
            session.setAttribute("ParallelUsage", "N");
//                    String querycheckPU = "insert into prg_ar_parallel_usage(user_id,parallel_usage) values("+userId+",'No')";
//                    pbpretobj1=pbdb.execSelectSQL(querycheckPU);
            response.getWriter().print("Parallel Usage Deactivated");
        } else {
            session.setAttribute("ParallelUsage", "Y");
//                    String querycheckPU = "insert into prg_ar_parallel_usage(user_id,parallel_usage) values("+userId+",'Yes')";
//                    pbpretobj1=pbdb.execSelectSQL(querycheckPU);
            response.getWriter().print("Parallel Usage Activated");
        }
        return null;
    }
}
