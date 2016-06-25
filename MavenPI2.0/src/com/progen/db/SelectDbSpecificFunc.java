/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.db;

import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class SelectDbSpecificFunc {

    public SelectDbSpecificFunc() {
    }

    public String ifnullthen() {
        String dbName = ProgenConnection.getInstance().getDatabaseType();
        return (ifnullthen(dbName));
    }

    public String ifnullthen(String dbName) {
        if (dbName.equalsIgnoreCase("ORACLE")) {
            return ("NVL");
        } else {
            return ("COALESCE");
        }
    }
}
