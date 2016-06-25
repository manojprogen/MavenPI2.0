/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
class DimenkeyvalueResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"testQuery", "select distinct & from & where & like('&')"}, {"DimentionDetails", "select distinct  DIMENSION_NAME,DIMENSION_DESC,DIM_HELPTEXT from PRG_QRY_DIMENSIONS WHERE DIMENSION_ID = &"}
    };
}
