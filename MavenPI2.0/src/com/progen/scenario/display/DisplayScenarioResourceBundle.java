package com.progen.scenario.display;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class DisplayScenarioResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getParameterQuery1", "select distinct BUSS_COL_NAME , BUSS_TABLE_ID , DISP_NAME from PRG_USER_ALL_INFO_DETAILS "
            + "where element_id ='&'"},};
}
