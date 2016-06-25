/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class ModuleComponent implements Serializable {

    private String compCode;
    private int compId;
    private String parentCompCode;
    private int modId;
    private String compName;
    private boolean isEnable;
    private String module;
    private ArrayList<String> subModuleComponent = new ArrayList<String>();

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public String getParentCompCode() {
        return parentCompCode;
    }

    public void setParentCompCode(String parentCompCode) {
        this.parentCompCode = parentCompCode;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public static Predicate<ModuleComponent> getParentComponentPredicate() {
        Predicate<ModuleComponent> predicate = new Predicate<ModuleComponent>() {

            public boolean apply(ModuleComponent input) {
                if ("".equals(input.getParentCompCode()) || null == input.getParentCompCode()) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public static Predicate<ModuleComponent> getChildComponentsPredicate(final ModuleComponent parent) {
        Predicate<ModuleComponent> predicate = new Predicate<ModuleComponent>() {

            public boolean apply(ModuleComponent input) {
                if ((parent.getCompCode()).equals(input.getParentCompCode())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public static Predicate<ModuleComponent> getChildComponentsPredicate(final String parentCode) {
        Predicate<ModuleComponent> predicate = new Predicate<ModuleComponent>() {

            public boolean apply(ModuleComponent input) {
                if (parentCode.equals(input.getParentCompCode())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public static Predicate<ModuleComponent> getAllChildComponentsPredicate() {
        Predicate<ModuleComponent> predicate = new Predicate<ModuleComponent>() {

            public boolean apply(ModuleComponent input) {
                if (input.getParentCompCode() != null && !"".equals(input.getParentCompCode())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public static Predicate<ModuleComponent> getComponentCodePredicate(final String compCode) {
        Predicate<ModuleComponent> predicate = new Predicate<ModuleComponent>() {

            public boolean apply(ModuleComponent input) {
                if ((input.getCompCode()).equals(compCode)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ModuleComponent) {
            if (this.compCode.equals(((ModuleComponent) o).getCompCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.compCode != null ? this.compCode.hashCode() : 0);
        return hash;
    }

    public static Predicate<ModuleComponent> getComponentCodeAndChildPredicate(final String compCode) {
        Predicate<ModuleComponent> predicate = new Predicate<ModuleComponent>() {

            public boolean apply(ModuleComponent input) {
                if ((input.getCompCode()).equals(compCode) || input.getParentCompCode().equals(compCode)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public static Predicate<ModuleComponent> getOnlyParentPredicate(final String compCode) {
        Predicate<ModuleComponent> predicate = new Predicate<ModuleComponent>() {

            public boolean apply(ModuleComponent input) {
                if ((input.getCompCode()).equals(compCode) && input.getParentCompCode().equals(compCode)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public ArrayList<String> getSubModuleComponent() {
        return subModuleComponent;
    }
}
