/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author progen
 */
public class SuperAdminBuilder {

    public String generateHtml(ArrayList<LicenseModule> licenseModuleLst, ArrayList<LicenseModule> licenseModLst, String ctxPath) {
        StringBuilder builder = new StringBuilder();
        builder.append("<ul id='myList' class='filetree' style='width:300px;'>");
        builder.append("<li  class='closed' style='background-image:url('')'><img src='icons pinvoke/arrow-move.png'><span id='123' class='openmenu1'><font size='1px' face='verdana'>Super Admin Privileges</font></span>");
        builder.append("<ul>");
        int index = 0;
//        String moduleStatus = "";
        StringBuilder moduleStatus=new StringBuilder();
        int licenses = 0;
        ArrayList<String> moduleCodeLst = new ArrayList<String>();
        if (licenseModLst != null) {
            for (LicenseModule licModule : licenseModLst) {
                moduleCodeLst.add(licModule.getModuleCode());
            }
        }
        for (LicenseModule licModule : licenseModuleLst) {
            licenses = 0;
            if (moduleCodeLst != null && moduleCodeLst.size() > 0) {
                if (moduleCodeLst.contains(licModule.getModuleCode())) {
                    licModule.setEnable(true);
                    moduleStatus.append("checked");
                    index = moduleCodeLst.indexOf(licModule.getModuleCode());
                    licenses = licenseModLst.get(index).getLicenses();
                } else {
                    moduleStatus.append("unchecked");
                }
            } else {
                moduleStatus.append("unchecked");
            }
            builder.append("<li id='").append(licModule.getModuleCode()).append("~LI' class='closed' >");
            builder.append("<input type='checkbox' name=").append(licModule.getModuleCode()).append(" id=").append(licModule.getModuleCode()).append(" ").append(moduleStatus).append(" onchange='enableAllComponents(this.id)'>");
            builder.append("<input type='text' id='").append(licModule.getModuleCode()).append("~Lic' value=").append(licenses).append(" name='txt").append(licModule.getModuleCode()).append("' style='width:30px;height:10px' title='Number of licenses'>");
            builder.append("<span id='").append(licModule.getModuleCode()).append("~SPAN' class='connMenu'><font size='1px' face='verdana' style='font-size: 11px;font-weight: bolder;' class='subTotalCell'>&nbsp;").append(licModule.getModuleName()).append("</font></span>");
            LicenseModule assModule = null;
            ArrayList<ModuleComponent> assModCompLst = null;
            if (licenseModLst != null) {
                Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModLst, LicenseModule.getLicenseModulePredicate(licModule.getModuleCode())).iterator();
                if (moduleIter.hasNext()) {
                    assModule = moduleIter.next();
                    assModCompLst = assModule.getModuleComponentsLst();
                }
            }
            builder.append(licModule.getModuleComponentsHtml(assModCompLst, licModule.getModuleCode(), ctxPath));
            builder.append("</li>");
        }
        builder.append("</ul>");
        builder.append("</li>");
        builder.append("</ul>");
        builder.append("<br><br>");
        builder.append("<table align='center'><tr><td><input type='button' value='Save' class='navtitle-hover' align='center' onclick='generateXmlForSuperAdmin()'></td></tr></table>");
        return builder.toString();
    }

    public String generateHtmlForUser(UserLicense userLicense, UserAssignment userAssg, String ctxPath) {
        StringBuilder builder = new StringBuilder();
        builder.append("<ul id='myList' class='filetree' style='width:300px;'>");
        builder.append("<li  class='closed' style='background-image:url('')'><img src='" + ctxPath + "/icons pinvoke/arrow-move.png'><span id='123' class='openmenu1'><font size='1px' face='verdana' style='font-weight:bolder;font-size:11px'>User Privileges</font></span>");
        builder.append("<ul>");
        ArrayList<LicenseModule> licenseModuleLst = userLicense.getLicenseModules();
//        String moduleStatus = "";
        StringBuilder moduleStatus=new StringBuilder();
        for (LicenseModule module : licenseModuleLst) {
            if (module.isEnable() && module.isModuleAvailableForAssignment()) {
                if (userAssg.isModuleAssigned(module.getModuleCode())) {
                    moduleStatus.append("checked");
                } else {
                    moduleStatus.append("unchecked");
                }

                builder.append("<li id='").append(module.getModuleCode()).append("~LI' class='closed' >");
                builder.append("<input type='checkbox' name=").append(module.getModuleCode()).append(" id=").append(module.getModuleCode()).append(" ").append(moduleStatus).append(" onchange='enableAllComponents(this.id)'>");
                builder.append("<img src='").append(ctxPath).append("/icons pinvoke/arrow-out.png' style='height:12px;width:12px'>");
                builder.append("<span id='").append(module.getModuleCode()).append("~SPAN' class='connMenu'><font size='1px' face='verdana'  class='subTotalCell'>&nbsp;").append(module.getModuleName()).append("</font></span>");
                builder.append(generateModuleComponentsHtmlforUser(userLicense, userAssg, module.getModuleCode(), ctxPath));
                builder.append("</li>");

            } else if (module.isEnable()) {
                if (userAssg.isModuleAssigned(module.getModuleCode())) {
                    moduleStatus.append("checked");

                    builder.append("<li id='").append(module.getModuleCode()).append("~LI' class='closed' >");
                    builder.append("<input type='checkbox' name=").append(module.getModuleCode()).append(" id=").append(module.getModuleCode()).append(" ").append(moduleStatus).append(" onchange='enableAllComponents(this.id)'>");
                    builder.append("<img src='/pi/icons pinvoke/arrow-out.png' style='height:12px;width:12px'>");
                    builder.append("<span id='").append(module.getModuleCode()).append("~SPAN' class='connMenu'><font size='1px' face='verdana'  class='subTotalCell'>&nbsp;").append(module.getModuleName()).append("</font></span>");
                    builder.append(generateModuleComponentsHtmlforUser(userLicense, userAssg, module.getModuleCode(), ctxPath));
                    builder.append("</li>");
                }
            }
        }
        builder.append("</ul>");
        builder.append("</li>");
        builder.append("</ul>");
        builder.append("<br><br>");
        builder.append("<table align='center'><tr><td><input type='button' value='Save' class='navtitle-hover' align='center' onclick='savePrivileges()'></td></tr></table>");
        return builder.toString();
    }

    public String generateModuleComponentsHtmlforUser(UserLicense userLicense, UserAssignment userAssg, String moduleCode, String ctxPath) {
        StringBuilder builder = new StringBuilder("");


        String parCompCode = "";
        String parCompName = "";
        String compStatus = "";

        builder.append("<ul id='").append(moduleCode).append("~UL'>");
        Iterator<ModuleComponent> parentComponents = userLicense.getParentModuleComponents(moduleCode).iterator();

        while (parentComponents.hasNext()) {
            ModuleComponent parent = parentComponents.next();

            if (parent.isEnable()) {
                parCompCode = parent.getCompCode();
                parCompName = parent.getCompName();

                if (userAssg.isModuleComponentAssigned(moduleCode, parent.getCompCode())) {
                    compStatus = "checked";
                } else {
                    compStatus = "unchecked";
                }
                builder.append("<li id='").append(parCompCode).append("~LI' class='closed' >");
                builder.append("<input type='checkbox' id=").append(moduleCode).append("~").append(parCompCode).append("  ").append(compStatus).append("  name='").append(moduleCode).append("~Component' onchange=\"enableAllChildComponents(this.id,'").append(moduleCode).append("')\" >");
                builder.append("<img src='").append(ctxPath).append("/icons pinvoke/block-small.png'>");
                builder.append("<span id='").append(parCompCode).append("~SPAN' class='connMenu'><font size='1px' face='verdana' class='dimensionCell'>&nbsp;").append(parCompName).append("</font></span>");
                builder.append(generateChildComponentsHtmlForUser(userLicense, userAssg, moduleCode, parCompCode, ctxPath));
                builder.append("</li>");
            }
        }
        builder.append("</ul>");
        //  
        return builder.toString();
    }

    public String generateChildComponentsHtmlForUser(UserLicense userLicense, UserAssignment userAssg, String moduleCode, String compCode, String ctxPath) {
        StringBuilder builder = new StringBuilder();
        builder.append("<ul id='" + compCode + "~UL'>");
        String childCompCode = "";
        String childCompName = "";
        String childCompStatus = "";
        Iterator<ModuleComponent> childComponents = userLicense.getChildModuleComponents(moduleCode, compCode).iterator();

        while (childComponents.hasNext()) {
            ModuleComponent child = childComponents.next();
            if (child.isEnable()) {
                childCompCode = child.getCompCode();
                childCompName = child.getCompName();

                if (userAssg.isModuleComponentAssigned(moduleCode, child.getCompCode())) {
                    childCompStatus = "checked";
                } else {
                    childCompStatus = "unchecked";
                }
                builder.append("<li id='").append(childCompCode).append("~LI' class='closed'>");
                builder.append("<img src='").append(ctxPath).append("/icons pinvoke/leaf.png' style='height:11px'>");
                builder.append("<input type='checkbox' name='").append(moduleCode).append("~ChildComponent' id=").append(moduleCode).append("~").append(compCode).append("~").append(childCompCode).append("   ").append(childCompStatus).append(" onchange=\"enableChildComponent(this.id,'").append(compCode).append("','").append(moduleCode).append("')\"> ");
//                     builder.append("<img src='"+ctxPath+"/icons pinvoke/information-small.png'>");
                builder.append("<span id='").append(childCompCode).append("~SPAN' class='connMenu'><font size='1px' face='verdana'>&nbsp;").append(childCompName).append("</font></span>");
                builder.append("</li>");
            }
        }
        builder.append("</ul>");
        return builder.toString();
    }
}
