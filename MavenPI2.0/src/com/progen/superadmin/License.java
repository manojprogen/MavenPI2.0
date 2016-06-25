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
public class License {

    ArrayList<LicenseModule> licenseModuleLst;

    public License() {
        licenseModuleLst = new ArrayList<LicenseModule>();
    }

    public LicenseModule createLicenseModule(int moduleId, int licenses, String moduleName, String moduleCode, boolean isEnable) {
        LicenseModule licenseModule = new LicenseModule(moduleId, licenses, moduleName, moduleCode, isEnable);
        licenseModuleLst.add(licenseModule);
        return licenseModule;
    }

    public synchronized ArrayList<LicenseModule> getLicenseModuleLst() {
        return licenseModuleLst;
    }

    public String getLicenseModulesJSON(String xmlData, String ctxPath) {
        SuperAdminBd adminBd = new SuperAdminBd();
        ArrayList<LicenseModule> assglicenseModLst = null;
        if (xmlData != null && !xmlData.equalsIgnoreCase("")) {
            assglicenseModLst = adminBd.parseSuperAdminXml(xmlData);
        }
//        ArrayList<String> moduleCodeLst = new ArrayList<String>();
//        if (licenseModLst != null) {
//            for (LicenseModule licModule : licenseModLst) {
//                moduleCodeLst.add(licModule.getModuleCode());
//            }
//        }
        SuperAdminBuilder adminBuilder = new SuperAdminBuilder();
        String generateHtml = adminBuilder.generateHtml(licenseModuleLst, assglicenseModLst, ctxPath);

//        StringBuilder builder = new StringBuilder("");
//        builder.append("{");
//        String moduleCodes = "ModuleCodes:[";
//        String moduleNames = "ModuleNames:[";
//        String moduleStatus = "ModuleStatus:[";
//        String moduleLicenses="ModuleLicenses:[";
//        String compsAvail="ComponentsAvail:[";
//        int index=0;
//        for (int i = 0; i < licenseModuleLst.size(); i++) {
//
//            moduleCodes += "\"";
//            moduleCodes += licenseModuleLst.get(i).getModuleCode();
//            moduleCodes += "\"";
//            moduleNames += "\"";
//            moduleNames += licenseModuleLst.get(i).getModuleName();
//            moduleNames += "\"";
//            moduleStatus += "\"";
//            moduleLicenses+="\"";
//
//            if (moduleCodeLst != null && moduleCodeLst.size() > 0) {
//                if (moduleCodeLst.contains(licenseModuleLst.get(i).getModuleCode())) {
//                    licenseModuleLst.get(i).setEnable(true);
//                    moduleStatus += "checked";
//                    index=moduleCodeLst.indexOf(licenseModuleLst.get(i).getModuleCode());
//                    moduleLicenses+=licenseModLst.get(index).getLicenses();
//                } else {
//                    moduleStatus += "unchecked";
//                }
//            } else {
//                moduleStatus += "unchecked";
//            }
//            moduleStatus += "\"";
//            moduleLicenses+="\"";
//            compsAvail+="\"";
//            if(licenseModuleLst.get(i).getModuleComponentsLst()!=null&&licenseModuleLst.get(i).getModuleComponentsLst().size()>0){
//               compsAvail+="true" ;
//            }else{
//                compsAvail+="false";
//            }
//            compsAvail+="\"";
//            if (i != licenseModuleLst.size() - 1) {
//                moduleCodes += ",";
//                moduleNames += ",";
//                moduleStatus += ",";
//                moduleLicenses+=",";
//                compsAvail+=",";
//            }
//
//
//            if (i == licenseModuleLst.size() - 1) {
//                moduleCodes += "],";
//                moduleNames += "],";
//                moduleStatus += "],";
//                 moduleLicenses+="],";
//                 compsAvail+="]";
//            }
//        }
//        builder.append(moduleCodes).append(moduleNames).append(moduleStatus).append(moduleLicenses).append(compsAvail).append("}");
        //
//        return builder.toString();
        return generateHtml;
    }

    public String toXml() {
        StringBuffer licModXml = new StringBuffer("");
        licModXml.append("<Modules>");
        for (int i = 0; i < licenseModuleLst.size(); i++) {
            if (licenseModuleLst.get(i).isEnable()) {
                licModXml.append("<Module>");
                licModXml.append("<ModuleCode>");
                licModXml.append(licenseModuleLst.get(i).getModuleCode());
                licModXml.append("</ModuleCode>");
                licModXml.append("<License>");
                licModXml.append(licenseModuleLst.get(i).getLicenses());
                licModXml.append("</License>");
                licModXml.append(licenseModuleLst.get(i).toXml());
                licModXml.append("</Module>");
            }
        }
        licModXml.append("</Modules>");
        return licModXml.toString();
    }

    public void disableAll() {
        for (LicenseModule module : this.licenseModuleLst) {
            module.disableAll();
        }
    }

    public void enableModule(LicenseModule module) {
        LicenseModule moduleToEnable = this.getLicenseModuleFromList(module);
        moduleToEnable.setEnable(true);
    }

    public void enableModuleComponent(LicenseModule module, ModuleComponent component) {
        LicenseModule moduleToEnable = this.getLicenseModuleFromList(module);
        moduleToEnable.enableModuleComponent(component);
    }

    public String getModuleComponentsJSONforUser(String moduleCode) {
        LicenseModule module = new LicenseModule();
        module.setModuleCode(moduleCode);
        LicenseModule srcModule = this.getLicenseModuleFromList(module);
        return srcModule.getUserAssignModuleComponentsJSON();
    }

    public void setConsumedCountForModule(LicenseModule module, int count) {
        LicenseModule srcModule = this.getLicenseModuleFromList(module);
        srcModule.setConsumeCount(count);
    }

    public void setLicenseCount(LicenseModule module, int count) {
        LicenseModule srcModule = this.getLicenseModuleFromList(module);
        srcModule.setLicenses(count);
    }

    private LicenseModule getLicenseModuleFromList(LicenseModule module) {
        int index = this.licenseModuleLst.indexOf(module);
        if (index >= 0) {
            LicenseModule moduleFromList = this.licenseModuleLst.get(index);
            return moduleFromList;
        }
        return module;
    }

    public void consumeModule(String moduleCode) {
        LicenseModule module = new LicenseModule();
        module.setModuleCode(moduleCode);
        LicenseModule srcModule = this.getLicenseModuleFromList(module);
        srcModule.consumeModule();
    }

    public void revokeModule(String moduleCode) {
        LicenseModule module = new LicenseModule();
        module.setModuleCode(moduleCode);
        LicenseModule srcModule = this.getLicenseModuleFromList(module);
        srcModule.revokeModule();
    }

    public ArrayList<ModuleComponent> getParentModuleComponents(String modCode) {
        ArrayList<ModuleComponent> compLst = new ArrayList<ModuleComponent>();
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(modCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();

            Iterable<ModuleComponent> modComps = Iterables.filter(module.getModuleComponentsLst(), ModuleComponent.getParentComponentPredicate());
            for (ModuleComponent component : modComps) {
                compLst.add(component);
            }
        }
        return compLst;
    }

    public boolean isCompAvailableForModule(String modCode) {
        ArrayList<ModuleComponent> compLst = new ArrayList<ModuleComponent>();
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(modCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();

            Iterator<ModuleComponent> modComps = Iterables.filter(module.getModuleComponentsLst(), ModuleComponent.getParentComponentPredicate()).iterator();
            if (modComps.hasNext()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public ArrayList<ModuleComponent> getChildModuleComponents(String modCode, String compCode) {
        ArrayList<ModuleComponent> compLst = new ArrayList<ModuleComponent>();
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(modCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();

            Iterable<ModuleComponent> modComps = Iterables.filter(module.getModuleComponentsLst(), ModuleComponent.getChildComponentsPredicate(compCode));
            for (ModuleComponent component : modComps) {
                compLst.add(component);
            }
        }
        return compLst;
    }

    public ArrayList<ModuleComponent> getAllChildModuleComponents(String modCode) {
        ArrayList<ModuleComponent> compLst = new ArrayList<ModuleComponent>();
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(modCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();

            Iterable<ModuleComponent> modComps = Iterables.filter(module.getModuleComponentsLst(), ModuleComponent.getAllChildComponentsPredicate());
            for (ModuleComponent component : modComps) {
                compLst.add(component);
            }
        }
        return compLst;
    }

    public void enableDisableModule(String moduleCode, boolean status) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();
            module.setEnable(status);
            module.enableDisableModuleComponents(status);
        }
    }

    public void enableDisableModuleComponent(String moduleCode, String compCode, boolean status, boolean isModuleEnable) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();
            module.setEnable(isModuleEnable);
            module.enableDisableModuleComponent(compCode, status);
        }
    }

    public void enableDisableModuleChildComponent(String moduleCode, String compCode, String childCompCode, boolean status, boolean isModuleEnable, boolean isCompEnable) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();
            module.setEnable(isModuleEnable);
            module.enableDisableModuleChildComponent(compCode, childCompCode, status, isCompEnable);
        }
    }

    public LicenseModule getLicenseModulePredicate(String moduleCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(this.licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();
            return module;

        } else {
            return null;
        }
    }
}
