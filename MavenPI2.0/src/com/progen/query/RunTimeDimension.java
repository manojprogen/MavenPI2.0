/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.query;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class RunTimeDimension implements RunTimeColumn, Serializable {

    private static final long serialVersionUID = 22766680530749879L;
    private ArrayList<String> dimData = new ArrayList<String>();

    public RunTimeDimension(ArrayList dimData) {
        this.dimData = dimData;
    }

    public String getData(int row) {
        return this.dimData.get(row);
    }
}
