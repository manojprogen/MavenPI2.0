/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.bd;

import com.progen.db.PbBaseDAO;

/**
 *
 * @author Administrator
 */
public class PbBaseBD {

    PbBaseDAO DAO = new PbBaseDAO();

    public void reportMessages(String userId, String reportId, String msgText) throws Exception {
        DAO.reportMessages(userId, reportId, msgText);
    }
}
