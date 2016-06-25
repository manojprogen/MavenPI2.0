/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Tracks the module assignment for a user Used while assigning privileges for
 * the user
 *
 * @author progen
 */
public class UserAssignment implements Serializable {

    int userId;
    ArrayList<LicenseModule> licenseModuleLst;

    public UserAssignment(int userId) {
        this.userId = userId;
        this.licenseModuleLst = new ArrayList<LicenseModule>();
    }

    public boolean assignModuleToUser(String moduleCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (!moduleIter.hasNext()) {
            LicenseModule licenseModule = new LicenseModule();
            licenseModule.setModuleCode(moduleCode);
            licenseModuleLst.add(licenseModule);
            return true;
        }
        return false;
    }

    public void assignModuleToUser(ModuleComponent moduleComp, String moduleCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            moduleIter.next().addComponent(moduleComp);
        }
    }

    public void assignChildModuleToUser(ModuleComponent moduleComp, String moduleCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            moduleIter.next().addChildComponent(moduleComp);
        }
    }

    public void revokeModuleFromUser(ModuleComponent moduleComp, String moduleCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            moduleIter.next().revokeComponent(moduleComp);
        }
    }

    public void revokeChildModuleFromUser(ModuleComponent moduleComp, String moduleCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            moduleIter.next().revokeChildComponent(moduleComp);
        }
    }

    public boolean revokeModuleFromUser(String moduleCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule licenseModule = new LicenseModule();
            licenseModule.setModuleCode(moduleCode);
            licenseModuleLst.remove(licenseModule);
            return true;
        }
        return false;
    }
//    public void assingModuleComponentToUser(String moduleCode, String compCode,String parCompCode)
//    {
//        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
//        if ( moduleIter.hasNext() )
//            moduleIter.next().addComponent(compCode,parCompCode);
//    }

    public void assingModuleComponentToUser(String moduleCode, String compCode, String parCompCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            moduleIter.next().addComponent(compCode, parCompCode);
        }
    }

//    public void revokeModuleComponentToUser(String moduleCode, String compCode,String parCompCode)
//    {
//        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
//        if ( moduleIter.hasNext() )
//            moduleIter.next().removeComponent(compCode,parCompCode);
//    }
    public void revokeModuleComponentToUser(String moduleCode, String compCode, String parCompCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            moduleIter.next().removeComponent(compCode, parCompCode);
        }
    }

    public boolean isModuleAssigned(String moduleCode) {
        Iterator moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isModuleComponentAssigned(String moduleCode, String compCode) {
        Iterator<LicenseModule> moduleIter = Iterables.filter(licenseModuleLst, LicenseModule.getLicenseModulePredicate(moduleCode)).iterator();
        if (moduleIter.hasNext()) {
            LicenseModule module = moduleIter.next();
            Iterator<ModuleComponent> componentIter = Iterables.filter(module.getModuleComponentsLst(), ModuleComponent.getComponentCodePredicate(compCode)).iterator();
            if (componentIter.hasNext()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


    }

    public ArrayList<LicenseModule> getLicenseModuleLst() {
        return this.licenseModuleLst;
    }
}
