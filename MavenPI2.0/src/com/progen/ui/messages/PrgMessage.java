/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.ui.messages;

import com.google.gson.Gson;

/**
 *
 * @author arun
 */
public class PrgMessage {

    private String errorCode;
    private String mesgCode;
    private String mesgTxt;

    public PrgMessage(String errorCode, String mesgCode) {
        this.errorCode = errorCode;
        this.mesgCode = mesgCode;
    }

    public void setMesgTxt(String mesgTxt) {
        this.mesgTxt = mesgTxt;
    }

    public String getMessageJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
