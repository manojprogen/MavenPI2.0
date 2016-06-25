/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

/**
 *
 * @author progen
 */
public class Menu {

    private String menuEntry;
    private String menuFunction;
    private int menuLevel;
    private Menu[] subMenu;

    public String getMenuEntry() {
        return this.menuEntry;
    }

    public String getMenuFunction() {
        return this.menuFunction;
    }

    public int getMenuLevel() {
        return this.menuLevel;
    }

    public Menu(String menuEntry) {
        this.menuEntry = menuEntry;
        this.subMenu = new Menu[0];
    }

    public void setMenuFunction(String menuFunction) {
        this.menuFunction = menuFunction;
    }

    public Menu[] getSubMenu() {
        return this.subMenu;
    }

    public void setSubMenu(Menu[] subMenu) {
        this.subMenu = subMenu;
    }
}
