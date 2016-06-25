/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.util.HashMap;
import java.util.TreeSet;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class ProGenChartUtilities {

    ReportTemplateDAO DAO = new ReportTemplateDAO();

    public String buildGraphTypesDiv(String ctxPath, String[] grpTypeskeys, HashMap GraphTypesHashMap, String javascriptFunName) {
        StringBuffer SBThumbNails = new StringBuffer("");
        StringBuffer SBTitles = new StringBuffer("");
        StringBuffer SBFinalStr = new StringBuffer("");
        String[] grpTypesValues = null;

        if (grpTypeskeys != null && GraphTypesHashMap != null) {
            grpTypesValues = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);



            for (int i = 0; i < grpTypesValues.length; i++) {
                if (grpTypesValues[i] != null) {
                    ////.println("grpTypesValues[i]"+grpTypesValues[i]);

                    if (i == 0) {
                        SBThumbNails.append("<Tr>");
                        SBTitles.append("<Tr>");
                    } else if (i % 4 == 0) {
                        SBThumbNails.append("</Tr>");
                        SBTitles.append("</Tr>");
                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());
                        SBThumbNails = new StringBuffer("");
                        SBTitles = new StringBuffer("");
                        SBTitles.append("<Tr>");
                        SBThumbNails.append("<Tr>");
                    }

                    SBThumbNails.append("<Td align=\"center\">");
                    //SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + GraphTypesHashMap.get(grpTypeskeys[i]) + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName + "\">&nbsp;");
                    SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + grpTypesValues[i] + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName + "(this,'" + grpTypesValues[i] + "')\">&nbsp;");
                    SBThumbNails.append("</Td>");

                    SBTitles.append("<Td align=\"center\">");
                    SBTitles.append(grpTypesValues[i]);
                    SBTitles.append("</Td>");

                    if (i == grpTypeskeys.length - 1) {
                        SBTitles.append("</Tr>");
                        SBThumbNails.append("</Tr>");
                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());

                        SBThumbNails = new StringBuffer("");
                        SBTitles = new StringBuffer("");
                    }
                }
            }
        }

        return SBFinalStr.toString();
    }

    public String buildAddMoreGraphTypesDiv(String ctxPath, String[] grpTypeskeys, HashMap GraphTypesHashMap, String javascriptFunName) {
        StringBuffer SBThumbNails = new StringBuffer("");
        StringBuffer SBTitles = new StringBuffer("");
        StringBuffer SBFinalStr = new StringBuffer("");

        String[] grpTypesValues = null;


        if (grpTypeskeys != null && GraphTypesHashMap != null) {
            grpTypesValues = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);

            int count = 0;
            for (int i = 0; i < grpTypesValues.length; i++) {



                if (grpTypesValues[i] != null) {
                    if (i == 0) {
                        SBThumbNails.append("<Tr>");
                        SBTitles.append("<Tr>");
                    } else if (count % 5 == 0) {
                        SBThumbNails.append("</Tr>");
                        SBTitles.append("</Tr>");
                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());
                        SBThumbNails = new StringBuffer("");
                        SBTitles = new StringBuffer("");
                        SBTitles.append("<Tr>");
                        SBThumbNails.append("<Tr>");
                    }

                    if (!grpTypesValues[i].equalsIgnoreCase("Dial") && !grpTypesValues[i].equalsIgnoreCase("Meter") && !grpTypesValues[i].equalsIgnoreCase("Thermometer")) {
                        count++;
                        SBThumbNails.append("<Td align=\"center\">");
                        SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + grpTypesValues[i].trim() + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName.replace("¥", grpTypesValues[i]) + "\">&nbsp;");
                        SBThumbNails.append("</Td>");

                        SBTitles.append("<Td align=\"center\">");
                        SBTitles.append(grpTypesValues[i]);
                        SBTitles.append("</Td>");
                    }

                    if (i == grpTypeskeys.length - 1) {
                        SBTitles.append("</Tr>");
                        SBThumbNails.append("</Tr>");
                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());

                        SBThumbNails = new StringBuffer("");
                        SBTitles = new StringBuffer("");
                    }
                }
            }
        }

        return SBFinalStr.toString();
    }

    //building graph types div for report designer
    public String buildGraphTypesForRD(String ctxPath, String[] grpTypeskeys, HashMap GraphTypesHashMap, String javascriptFunName) {
        StringBuffer SBThumbNails = new StringBuffer("");
        StringBuffer SBTitles = new StringBuffer("");
        StringBuffer SBFinalStr = new StringBuffer("");
        String[] grpTypesValues = null;

        if (grpTypeskeys != null && GraphTypesHashMap != null) {
            grpTypesValues = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);

            for (int i = 0; i < grpTypesValues.length; i++) {
                if (grpTypesValues[i] != null) {
                    if (i == 0) {
                        SBThumbNails.append("<Tr>");
                        SBTitles.append("<Tr>");
                    } else if (i % 4 == 0) {
                        SBThumbNails.append("</Tr>");
                        SBTitles.append("</Tr>");
                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());
                        SBThumbNails = new StringBuffer("");
                        SBTitles = new StringBuffer("");
                        SBTitles.append("<Tr>");
                        SBThumbNails.append("<Tr>");
                    }

                    SBThumbNails.append("<Td align=\"center\">");
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + grpTypesValues[i] + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName + "(this,'" + grpTypesValues[i] + "'," + DAO.getSequence("ident_current('PRG_AR_GRAPH_MASTER')") + ")\">&nbsp;");
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + grpTypesValues[i] + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName + "(this,'" + grpTypesValues[i] + "'," + DAO.getSequence("LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER") + ")\">&nbsp;");
                    } else {
                        SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + grpTypesValues[i] + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName + "(this,'" + grpTypesValues[i] + "'," + DAO.getSequence("PRG_AR_GRAPH_MASTER_SEQ") + ")\">&nbsp;");
                    }

                    SBThumbNails.append("</Td>");

                    SBTitles.append("<Td align=\"center\">");
                    SBTitles.append(grpTypesValues[i]);
                    SBTitles.append("</Td>");

                    if (i == grpTypeskeys.length - 1) {
                        SBTitles.append("</Tr>");
                        SBThumbNails.append("</Tr>");
                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());

                        SBThumbNails = new StringBuffer("");
                        SBTitles = new StringBuffer("");
                    }
                }
            }
        }

        return SBFinalStr.toString();
    }

//    public String buildGraphTableForRD(String ctxPath, String[] grpTypeskeys, HashMap GraphTypesHashMap, String javascriptFunName) {
//        StringBuffer SBThumbNails = new StringBuffer("");
//        StringBuffer SBTitles = new StringBuffer("");
//        StringBuffer SBFinalStr = new StringBuffer("");
//        String[] grpTypesValues = null;
//
//        if (grpTypeskeys != null && GraphTypesHashMap != null) {
//            grpTypesValues = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);
//
//            for (int i = 0; i < grpTypesValues.length; i++) {
//                if (grpTypesValues[i] != null) {
//                    if (i == 0) {
//                        SBThumbNails.append("<Tr>");
//                        SBTitles.append("<Tr>");
//                    } else if (i % 4 == 0) {
//                        SBThumbNails.append("</Tr>");
//                        SBTitles.append("</Tr>");
//                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());
//                        SBThumbNails = new StringBuffer("");
//                        SBTitles = new StringBuffer("");
//                        SBTitles.append("<Tr>");
//                        SBThumbNails.append("<Tr>");
//                    }
//
//                    SBThumbNails.append("<Td align=\"center\">");
//                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                        SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + grpTypesValues[i] + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName + "(this,'" + grpTypesValues[i] + "'," + DAO.getSequence("ident_current('PRG_AR_GRAPH_MASTER')") + ")\">&nbsp;");
//                    } else {
//                        SBThumbNails.append("<img src=\"" + ctxPath + "/images/" + grpTypesValues[i] + "_thumb.gif\"  style=\"cursor:pointer;cursor:hand\" title=\"Bar\" height=\"100\" width=\"100px\" onclick=\"" + javascriptFunName + "(this,'" + grpTypesValues[i] + "'," + DAO.getSequence("PRG_AR_GRAPH_MASTER_SEQ") + ")\">&nbsp;");
//                    }
//
//                    SBThumbNails.append("</Td>");
//
//                    SBTitles.append("<Td align=\"center\">");
//                    SBTitles.append(grpTypesValues[i]);
//                    SBTitles.append("</Td>");
//
//                    if (i == grpTypeskeys.length - 1) {
//                        SBTitles.append("</Tr>");
//                        SBThumbNails.append("</Tr>");
//                        SBFinalStr.append(SBThumbNails.toString() + SBTitles.toString());
//
//                        SBThumbNails = new StringBuffer("");
//                        SBTitles = new StringBuffer("");
//                    }
//                }
//            }
//        }
//
//        return SBFinalStr.toString();
//    }
    public int buildGraphTypesForDesigner() {
        int id;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            id = DAO.getSequence("ident_current('PRG_AR_GRAPH_MASTER')");
            id = id + 1;

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            id = DAO.getSequence("LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER");

        } else {
            id = DAO.getSequence("PRG_AR_GRAPH_MASTER_SEQ");

        }
        return id;
    }

    public static void main(String[] args) {
        ProGenChartUtilities utilities = new ProGenChartUtilities();
        HashMap GraphTypesHashMap = new HashMap();
        GraphTypesHashMap.put("Bar", "Bar");
        GraphTypesHashMap.put("Bar3D", "Bar");
        GraphTypesHashMap.put("Line3D", "Line3D");
        //GraphTypesHashMap.put("Line", "Line");
        //GraphTypesHashMap.put("Pie3D", "Pie3D");
        //GraphTypesHashMap.put("Pie", "Pie");

        //String[] grpTypeskey = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
        //String str = utilities.buildGraphTypesDiv("file:\\E:\\PiEE with Fx 01-02-2010\\piEE\\web", grpTypeskey, GraphTypesHashMap, "getGraphName");

    }
}
