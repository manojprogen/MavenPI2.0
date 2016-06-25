/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.progen.report.data.Menu;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableMenuRow;
import org.apache.log4j.Logger;

/**
 *
 * @author progen
 */
public class TableMenuDisplay extends TableDisplay {

    public static Logger logger = Logger.getLogger(TableMenuDisplay.class);

    public TableMenuDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateHTML() {
        StringBuilder menuHtml = new StringBuilder();
        TableMenuRow row = new TableMenuRow();
        row = (TableMenuRow) tableBldr.getMenuRowData();
        int colCount = tableBldr.getColumnCount();
        Menu[] tableMenu = null;
        String menuName;
        String menuFunction;
        String dispStyle;

        menuHtml.append("<Tr style=\"display:none;\">");
        if (tableBldr.isDrillAcrossSupported()) {
            menuHtml.append("<td width=\"5\"/>");
        }
//commented by manik to hide + sign adhoc drill
//        if(!tableBldr.getAdhocDrillType().equalsIgnoreCase("none"))
//        menuHtml.append("<td><a href=\"#\" class=\"tabMenuCol\"></a></td>");

        if (tableBldr.isSerialNumDisplay()) {
            menuHtml.append("<td width=\"5\"/><a href=\"#\" class=\"tabMenuCol\"></a></td>");
        }

        for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
            try {
                tableMenu = row.getMenu(i);
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
            dispStyle = row.getDisplayStyle(i);
            menuHtml.append("<Td style=\"display:" + dispStyle + ";\">");
            menuHtml.append("<ul class=\"dropDownMenu\">");


            if (!(row.isExcelDisplay())) {
                menuHtml.append("<li style=\"width:100%\"><a href=\"#\" class=\"tabMenuCol\"></a>");
                menuHtml.append("<ul style=\"width:110px\">");
            }

            for (Menu menu : tableMenu) {
                menuName = menu.getMenuEntry();
                menuFunction = menu.getMenuFunction();
                menuHtml.append("<li><a href=\"javascript:").append(menuFunction).append("\">").append(menuName).append("</a>");
                menuHtml.append(this.getSubMenu(menu.getSubMenu()));
                menuHtml.append("</li>");

            }
            if (!(row.isExcelDisplay())) {
                menuHtml.append("</ul>");
                menuHtml.append("</li>");
            }
            menuHtml.append("</ul>");
            menuHtml.append("</Td>");
        }
        menuHtml.append("</Tr>");
        return super.parentHtml.append(menuHtml);
    }

    private StringBuilder getSubMenu(Menu[] subMenu) {
        StringBuilder subMenuHtml = new StringBuilder();
        subMenuHtml.append("<ul>");
        String menuName;
        String menuFunction;
        for (int i = 0; i < subMenu.length; i++) {
            menuName = subMenu[i].getMenuEntry();
            menuFunction = subMenu[i].getMenuFunction();
            //subMenuHtml.append("<li><a href=\"#\" onClick=\"parent.doCallOperation(document.getElementById('HMENU_").append(j).append("'),'").append(columnName).append("',0,'").append(dataType).append("','").append(container.getTableId()).append("')\">Sort Ascend</a></li>");
            subMenuHtml.append("<li style=\"width:108px\" ><a href=\"javascript:").append(menuFunction).append("\">").append(menuName).append("</a>");
            subMenuHtml.append(this.getSubMenu(subMenu[i].getSubMenu()));
            subMenuHtml.append("</li>");
        }
        subMenuHtml.append("</ul>");
        return subMenuHtml;
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
}
