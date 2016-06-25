/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.ui.messages;

import com.google.gson.Gson;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class PrgUIContainer {

    private ArrayList<PrgUIComponent> componentLst;
    private String containerCode;

    public PrgUIContainer(String containerCode) {
        this.containerCode = containerCode;
        this.componentLst = new ArrayList<PrgUIComponent>();
    }

    public void addComponent(String componentCode, String componentText) {
        PrgUIComponent component = new PrgUIComponent(componentCode, componentText);
        this.componentLst.add(component);
    }

    public String generateJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
