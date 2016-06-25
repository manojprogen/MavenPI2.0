/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.ui.messages;

import com.google.gson.Gson;

/**
 *
 * @author progen
 */
public class PrgUIComponent {

    private String componentCode;
    private String componentText;

    public PrgUIComponent(String componentCode, String componentText) {
        this.componentCode = componentCode;
        this.componentText = componentText;
    }

    public String generateJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}