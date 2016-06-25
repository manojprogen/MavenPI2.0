/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;

/**
 *
 * @author sreekanth
 */
public class WhatIfScenarioDAO extends PbDb {

    public static Logger logger = Logger.getLogger(WhatIfScenarioDAO.class);

    String getProgenTableMeasures(ArrayList measuresList, ArrayList measuresNameList) {
        StringBuffer measureUlSB = new StringBuffer();
        try {
            measureUlSB.append("<ul id='MeasuresUL' class='sortable'>");
            for (int count = 0; count < measuresNameList.size(); count++) {

                measureUlSB.append("<li class='MeasuresULClass ui-draggable' id='MeasuresUL" + measuresList.get(count) + "'  style='width:220px;height:auto;color:white'>");
                measureUlSB.append(" <table width='100%'><tr align='left' valign='top'>");
                measureUlSB.append("<td  align='left'>" + measuresNameList.get(count) + "</td> </tr> </table> </li>");


            }
            measureUlSB.append(" </ul>");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return measureUlSB.toString();
    }
}
