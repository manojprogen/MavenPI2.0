/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import java.util.ArrayList;

/**
 * Holder for the master license (what components are enabled at the site and
 * no. of licenses available for component) When user logs in and assigns
 * privilege to user this class is used for tracking the licenses
 *
 * @author progen
 */
public class UserLicense {

    private static License userMasterLicense;
    private static UserLicense userLicense = null;
    private static String licenseXmlData;

    private UserLicense() {
    }

    public static UserLicense getInstance() {
        synchronized (UserLicense.class) {
            if (userLicense == null) {
                userLicense = new UserLicense();
                SuperAdminBd adminBd = new SuperAdminBd();
                userMasterLicense = adminBd.createUserLicenseObject(licenseXmlData);
            }
        }
        return userLicense;
    }

    public static void setLincenseXmlData(String xmlData) {
        licenseXmlData = xmlData;
    }

    public synchronized ArrayList<LicenseModule> getLicenseModules() {
        return userMasterLicense.getLicenseModuleLst();
    }

    public ArrayList<ModuleComponent> getParentModuleComponents(String modCode) {
        return userMasterLicense.getParentModuleComponents(modCode);
    }

    public ArrayList<ModuleComponent> getAllChildModuleComponents(String modCode) {
        return userMasterLicense.getAllChildModuleComponents(modCode);
    }

    public ArrayList<ModuleComponent> getChildModuleComponents(String modCode, String compCode) {
        return userMasterLicense.getChildModuleComponents(modCode, compCode);
    }

    public void consumeLicense(String moduleCode) {
        userMasterLicense.consumeModule(moduleCode);
    }

    public void revokeLicense(String moduleCode) {
        userMasterLicense.revokeModule(moduleCode);
    }

    public boolean isCompAvailableForModule(String modCode) {
        return userMasterLicense.isCompAvailableForModule(modCode);
    }

    public LicenseModule getModulePredicate(String moduleCode) {
        return userMasterLicense.getLicenseModulePredicate(moduleCode);
    }

    public LicenseModule getModuleComponentPredicate(String moduleCode, String compCode) {
        return userMasterLicense.getLicenseModulePredicate(moduleCode);
    }

    public void removeUserLicense() {
        synchronized (UserLicense.class) {
            userLicense = null;
        }
    }
}
