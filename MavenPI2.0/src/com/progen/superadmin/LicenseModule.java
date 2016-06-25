/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author progen
 */
public class LicenseModule implements Serializable {

    private int moduleId;
    private int licenses;
    private String moduleName;
    private String moduleCode;
    private boolean isEnable;
    private int consumedCount;
    private ArrayList<String> moduleComponents = new ArrayList<String>();
    ArrayList<ModuleComponent> moduleComponentsLst = new ArrayList<ModuleComponent>();

    public LicenseModule() {
//        moduleComponentsLst = new ArrayList<ModuleComponent>();
    }

    public LicenseModule(int moduleId, int licenses, String moduleName, String moduleCode, boolean isEnable) {
        this.moduleId = moduleId;
        this.licenses = licenses;
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.isEnable = isEnable;
    }

    public void createModuleComponent(int compId, String compCode, String compName, int modId, String parCompCode, boolean isEnable) {
        ModuleComponent moduleComponent = new ModuleComponent();
        moduleComponent.setCompId(compId);
        moduleComponent.setCompCode(compCode);
        moduleComponent.setCompName(compName);
        moduleComponent.setModId(modId);
        moduleComponent.setParentCompCode(parCompCode);
        moduleComponent.setEnable(isEnable);
        moduleComponentsLst.add(moduleComponent);
    }

    public ArrayList<ModuleComponent> getModuleComponentsLst() {
        return moduleComponentsLst;
    }

    public void addComponent(String compCode, String parCompCode) {
        ModuleComponent moduleComponent = new ModuleComponent();
        moduleComponent.setCompCode(compCode);
        moduleComponent.setParentCompCode(parCompCode);
        this.moduleComponentsLst.add(moduleComponent);
    }

    public void addComponent(ModuleComponent moduleComp) {
        if (!moduleComponentsLst.contains(moduleComp)) {
            moduleComponentsLst.add(moduleComp);
        }

    }

    public void addChildComponent(ModuleComponent moduleComp) {
        if (!moduleComponentsLst.contains(moduleComp)) {
            moduleComponentsLst.add(moduleComp);
        }

    }

    public void revokeComponent(ModuleComponent moduleComp) {
        if (moduleComponentsLst.contains(moduleComp)) {
            moduleComponentsLst.remove(moduleComp);
        }
    }

    public void revokeChildComponent(ModuleComponent moduleComp) {
        if (moduleComponentsLst.contains(moduleComp)) {
            moduleComponentsLst.remove(moduleComp);
        }
    }

    public void removeComponent(String compCode, String parCompCode) {
        ModuleComponent moduleComponent = new ModuleComponent();
        moduleComponent.setCompCode(compCode);
        moduleComponent.setParentCompCode(parCompCode);
        this.moduleComponentsLst.remove(moduleComponent);
    }

    public String getModuleComponentsJSON(ArrayList<ModuleComponent> assModCompLst) {

        ArrayList<String> assCompCodes = new ArrayList<String>();
        if (assModCompLst != null && assModCompLst.size() > 0) {
            for (ModuleComponent assModComp : assModCompLst) {
                assCompCodes.add(assModComp.getCompCode());
            }
        }
        StringBuilder json = new StringBuilder("");
        json.append("{");
        Iterator<ModuleComponent> parentComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getParentComponentPredicate()).iterator();
        StringBuilder parCompCode=new StringBuilder();
        parCompCode.append("ParentComponentCodes:[");
        StringBuilder parCompName=new StringBuilder();
        parCompName.append("ParentComponentNames:[");
        StringBuilder parCompStatus=new StringBuilder();
        parCompStatus.append("ParentComponentStatus:[");
        String childCompCode;
        String childCompName;
        String childCompStatus;
        while (parentComponents.hasNext()) {
            childCompCode = "";
            childCompName = "";
            childCompStatus = "";

            ModuleComponent parent = parentComponents.next();

            if (parentComponents.hasNext()) {

                parCompCode.append("\"").append(parent.getCompCode()).append("\",");
                parCompName.append("\"").append(parent.getCompName()).append("\",");
                if (assCompCodes.contains(parent.getCompCode())) {
                    parCompStatus.append("\"checked\",");
                    parent.setEnable(true);
                } else {
                    parCompStatus.append("\"unchecked\",");
                }
            } else {

                parCompCode.append("\"").append(parent.getCompCode()).append("\"");
                parCompName.append("\"").append(parent.getCompName()).append("\"");
                if (assCompCodes.contains(parent.getCompCode())) {
                    parCompStatus.append("\"checked\"");
                    parent.setEnable(true);
                } else {
                    parCompStatus.append("\"unchecked\"");
                }

            }

            Iterator<ModuleComponent> childComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getChildComponentsPredicate(parent)).iterator();
            childCompCode = parent.getCompCode() + "_childCodes:[";
            childCompName = parent.getCompCode() + "_childNames:[";
            childCompStatus = parent.getCompCode() + "_childStatus:[";
            boolean childAdded = false;
            while (childComponents.hasNext()) {
                childAdded = true;
                ModuleComponent child = childComponents.next();

                if (childComponents.hasNext()) {

                    childCompCode += "\"" + child.getCompCode() + "\",";
                    childCompName += "\"" + child.getCompName() + "\",";
                    if (assCompCodes.contains(child.getCompCode())) {
                        childCompStatus += "\"checked\",";
                        child.setEnable(true);
                    } else {
                        childCompStatus += "\"unchecked\",";
                    }

                } else {
                    childCompCode += "\"" + child.getCompCode() + "\"";
                    childCompName += "\"" + child.getCompName() + "\"";
                    if (assCompCodes.contains(child.getCompCode())) {
                        childCompStatus += "\"checked\"";
                    } else {
                        childCompStatus += "\"unchecked\"";
                    }
                }
            }
            if (childAdded) {
                childCompCode += "],";
                childCompName += "],";
                childCompStatus += "],";
                json.append(childCompCode);
                json.append(childCompName);
                json.append(childCompStatus);
            }
        }

        parCompCode.append("],");
        parCompName.append("],");
        parCompStatus.append("]");
        json.append(parCompCode);
        json.append(parCompName);
        json.append(parCompStatus);
        json.append("}");
        //
        return json.toString();
    }

    public String toXml() {
        StringBuffer compModXml = new StringBuffer("");
        compModXml.append("<EnabledComponents>");
        for (int i = 0; i < moduleComponentsLst.size(); i++) {
            if (moduleComponentsLst.get(i).isEnable()) {
                compModXml.append("<Component>");
                compModXml.append(moduleComponentsLst.get(i).getCompCode());
                compModXml.append("</Component>");
            }

        }
        compModXml.append("</EnabledComponents>");
        return compModXml.toString();
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getLicenses() {
        return licenses;
    }

    public void setLicenses(int licenses) {
        this.licenses = licenses;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getUserAssignModuleComponentsJSON() {
        StringBuilder json = new StringBuilder("");
        json.append("{");
        Iterator<ModuleComponent> parentComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getParentComponentPredicate()).iterator();
 StringBuilder parCompCode=new StringBuilder();
       parCompCode.append("ParentComponentCodes:[");
       StringBuilder parCompName=new StringBuilder();
        parCompName.append("ParentComponentNames:[");
       StringBuilder childCompCode=new StringBuilder();
        StringBuilder childCompName=new StringBuilder();
        while (parentComponents.hasNext()) {
//            childCompCode = "";
//            childCompName = "";

            ModuleComponent parent = parentComponents.next();

            if (parentComponents.hasNext()) {
                if (parent.isEnable()) {
                    parCompCode.append("\"").append(parent.getCompCode()).append("\",");
                    parCompName.append("\"").append(parent.getCompName()).append("\",");
                }
            } else {
                if (parent.isEnable()) {
                    parCompCode.append("\"").append(parent.getCompCode()).append("\"");
                    parCompName.append("\"").append(parent.getCompName()).append("\"");
                }
            }

            Iterator<ModuleComponent> childComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getChildComponentsPredicate(parent)).iterator();
            childCompCode.append(parent.getCompCode()).append("_childCodes:[");
            childCompName.append(parent.getCompCode()).append("_childNames:[");
            boolean childAdded = false;
            while (childComponents.hasNext()) {
                childAdded = true;
                ModuleComponent child = childComponents.next();

                if (childComponents.hasNext()) {
                    if (child.isEnable()) {
                        childCompCode.append("\"").append(child.getCompCode()).append("\",");
                        childCompName.append("\"").append(child.getCompName()).append("\",");
                    }
                } else {
                    if (child.isEnable()) {
                        childCompCode.append("\"").append(child.getCompCode()).append("\"");
                        childCompName.append("\"").append(child.getCompName()).append("\"");
                    }
                }
            }
            if (childAdded) {
                childCompCode.append("],");
                childCompName.append("],");
                json.append(childCompCode);
                json.append(childCompName);
            }
        }

        parCompCode.append("],");
        parCompName.append("]");
        json.append(parCompCode);
        json.append(parCompName);
        json.append("}");
        //
        return json.toString();
    }

    public void disableAll() {
        this.isEnable = false;
        for (ModuleComponent component : this.moduleComponentsLst) {
            component.setEnable(false);
        }
    }

    public void enableModuleComponent(ModuleComponent component) {
        int index = this.moduleComponentsLst.indexOf(component);
        if (index >= 0) {
            ModuleComponent componentToEnable = this.moduleComponentsLst.get(index);
            componentToEnable.setEnable(true);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof LicenseModule) {
            if (this.moduleCode.equals(((LicenseModule) o).getModuleCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.moduleCode != null ? this.moduleCode.hashCode() : 0);
        return hash;
    }

    public synchronized void setConsumeCount(int count) {
        this.consumedCount = count;
    }

    public synchronized void consumeModule() {
        this.consumedCount++;
    }

    public synchronized void revokeModule() {
        this.consumedCount--;
    }

    public synchronized boolean isModuleAvailableForAssignment() {
        return (this.licenses - this.consumedCount) > 0;
    }

    public static Predicate<LicenseModule> getLicenseModulePredicate(final String moduleCode) {
        Predicate<LicenseModule> predicate = new Predicate<LicenseModule>() {

            public boolean apply(LicenseModule input) {
                if (input.getModuleCode().equals(moduleCode)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public String getModuleComponentsHtml(ArrayList<ModuleComponent> assModCompLst, String moduleCode, String ctxPath) {

        ArrayList<String> assCompCodes = new ArrayList<String>();
        if (assModCompLst != null && assModCompLst.size() > 0) {
            for (ModuleComponent assModComp : assModCompLst) {
                assCompCodes.add(assModComp.getCompCode());
            }
        }
        StringBuilder builder = new StringBuilder("");
//        json.append("{");
        builder.append("<ul id='" + moduleCode + "~UL'>");
        Iterator<ModuleComponent> parentComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getParentComponentPredicate()).iterator();

        String parCompCode = "";
        String parCompName = "";
        String parCompStatus = "";
        String childCompCode;
        String childCompName;
        String childCompStatus;
        while (parentComponents.hasNext()) {
            childCompCode = "";
            childCompName = "";
            childCompStatus = "";

            ModuleComponent parent = parentComponents.next();
            parCompCode = parent.getCompCode();
            parCompName = parent.getCompName();
            if (assCompCodes.contains(parent.getCompCode())) {
                parCompStatus = "checked";
                parent.setEnable(true);
            } else {
                parCompStatus = "unchecked";
            }
            builder.append("<li id='").append(parCompCode).append("~LI' class='closed' >");
            builder.append("<img src='").append(ctxPath).append("/icons pinvoke/block-small.png'>");
            builder.append("<input type='checkbox' id=").append(moduleCode).append("~").append(parCompCode).append("  ").append(parCompStatus).append("  name='").append(moduleCode).append("~Component' onchange=\"enableAllChildComponents(this.id,'").append(moduleCode).append("')\" >");
            builder.append("<span id='").append(parCompCode).append("~SPAN' class='connMenu'><font size='1px' face='verdana' class='dimensionCell'>&nbsp;").append(parCompName).append("</font></span>");
            builder.append(getModuleChildComponentHtml(parent, assCompCodes, moduleCode, ctxPath));
            builder.append("</li>");

        }
        builder.append("</ul>");

        //
        return builder.toString();
    }

    public String getModuleChildComponentHtml(ModuleComponent parent, ArrayList<String> assCompCodes, String moduleCode, String ctxPath) {
        StringBuilder builder = new StringBuilder();
        String childCompCode;
        String childCompName;
        String childCompStatus;
        builder.append("<ul id='").append(parent.getCompCode()).append("~UL'>");
        Iterator<ModuleComponent> childComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getChildComponentsPredicate(parent)).iterator();
        childCompCode = parent.getCompCode();
        childCompName = parent.getCompName();
        childCompStatus = "";
        boolean childAdded = false;
        while (childComponents.hasNext()) {
            childAdded = true;
            ModuleComponent child = childComponents.next();
            childCompCode = child.getCompCode();
            childCompName = child.getCompName();
            if (assCompCodes.contains(child.getCompCode())) {
                childCompStatus = "checked";
                child.setEnable(true);
            } else {
                childCompStatus = "unchecked";
            }
            builder.append("<li id='").append(childCompCode).append("~LI' class='closed'>");
            builder.append("<img src='").append(ctxPath).append("/icons pinvoke/leaf.png' style='height:11px'>");
            builder.append("<input type='checkbox' name='").append(moduleCode).append("~ChildComponent' id=").append(moduleCode).append("~").append(parent.getCompCode()).append("~").append(childCompCode).append("   ").append(childCompStatus).append(" onchange=\"enableChildComponent(this.id,'").append(parent.getCompCode()).append("','").append(moduleCode).append("')\"> ");
            builder.append("<span id='").append(childCompCode).append("~SPAN' class='connMenu'><font size='1px' face='verdana'>&nbsp;").append(childCompName).append("</font></span>");
            builder.append("</li>");
        }
        builder.append("</ul>");
        return builder.toString();
    }

    public void enableDisableModuleComponents(boolean status) {
        for (ModuleComponent comp : moduleComponentsLst) {
            comp.setEnable(status);
        }
    }

    public void enableDisableModuleComponent(String compCode, boolean status) {
        Iterator<ModuleComponent> parentComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getComponentCodeAndChildPredicate(compCode)).iterator();
        while (parentComponents.hasNext()) {
            ModuleComponent comp = parentComponents.next();
            comp.setEnable(status);
        }
    }

    public void enableDisableModuleChildComponent(String compCode, String childCompCode, boolean status, boolean isCompEnable) {
        Iterator<ModuleComponent> childComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getComponentCodePredicate(childCompCode)).iterator();
        Iterator<ModuleComponent> parentComponents = Iterables.filter(moduleComponentsLst, ModuleComponent.getComponentCodePredicate(compCode)).iterator();
        while (childComponents.hasNext()) {
            ModuleComponent comp = childComponents.next();
            comp.setEnable(status);
        }
        while (parentComponents.hasNext()) {
            ModuleComponent comp = parentComponents.next();
            comp.setEnable(isCompEnable);
        }
    }

    public ArrayList<String> getModuleComponents() {
        return moduleComponents;
    }

    public void setModuleComponents(ArrayList<String> moduleComponents) {
        this.moduleComponents = moduleComponents;
    }
}
