package com.progen.userlayer.action;

import com.google.common.base.Joiner;
import java.util.ArrayList;

public class GenerateDragAndDrophtml {

    private String dragDivTitle;
    private String dropDivTitle;
    private ArrayList<String> dropedList = new ArrayList<String>();
    private ArrayList<String> dragableList = new ArrayList<String>();
    private boolean isdragavleRestrict = false;
    private String contextPath;
    private ArrayList<String> dragableListNames = null;
    private ArrayList<String> dropedmesNames = null;
    private String groupName;

    public GenerateDragAndDrophtml(String dragDivTitle, String dropDivTitle, ArrayList dropedList, ArrayList<String> dragableList, String contextPath) {


        if (dragDivTitle == null ? "" != null : !dragDivTitle.equals("")) {
            this.dragDivTitle = dragDivTitle;
        } else {
            this.dragDivTitle = "Select Columns from below";
        }

        if (dropDivTitle == null ? "" != null : !dropDivTitle.equals("")) {
            this.dropDivTitle = dropDivTitle;
        } else {
            this.dropDivTitle = "Drag Columns to here";
        }
        if (dropedList != null) {
            this.dropedList = dropedList;
        } else {
            this.dropedList = new ArrayList<String>();
        }
        this.dragableList = dragableList;
        this.contextPath = contextPath;

    }

    public GenerateDragAndDrophtml(String groupName) {
        this.groupName = groupName;
    }

    // modified by krishan
    public String getDragAndDropDiv() {

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table style='width:100%;' border='solid black 4px' align='center'>");
        htmlTable.append("<tr><td width='50%' valign='top' class='draggedTable1'>");
        htmlTable.append("<div style='height:20px' class='ui-state-default draggedDivs ui-corner-all'><font size='2' style='font-weight:bold'>").append(dragDivTitle).append("</font></div>");
        htmlTable.append("<div id='myList3div' style='height:350px;overflow:auto'><ul id='myList3' class='myList3 filetree treeview-famfamfam'>");
        for (String value : dragableList) {
            StringBuilder append = htmlTable;
            if (!value.trim().equalsIgnoreCase("")) {
                if (!contextPath.equals("")) {
                    append = htmlTable.append(" <li><img alt=''  src='").append(contextPath).append("/icons pinvoke/report.png'/><span class='myDragTabs' class='ui-state-default' id='").append(value).append("'>").append(getDragableListNames().get(dragableList.indexOf(value))).append("</span></li>");
                } else {
                    append = htmlTable.append(" <li style='width: 180px; height: 23px; color: white;' class='navtitle-hover myDragTabs ui-state-default sortable' id='" + value + "' ><span>").append(getDragableListNames().get(dragableList.indexOf(value))).append("</span></li>");
                }
            }
        }
        htmlTable.append("</ul></div></td> <td id='dropTabs' width='50%' valign='top'>");
        htmlTable.append("<div style='height:20px' class='ui-state-default draggedDivs ui-corner-all'><font size='2' style='font-weight:bold'>").append(dropDivTitle).append("</font></div>");
        htmlTable.append("<div style='height:350px;overflow:auto'> <ul id='sortable' class='sortable'>");
        if (!dropedList.isEmpty()) {
            for (String dropedValue : dropedList) {
                htmlTable.append("<li id='").append(dropedValue.trim()).append("_li").append("' style='width: 180px; height: auto; color: white;' class='navtitle-hover'>");
                htmlTable.append("<table id='").append(dropedValue.trim()).append("_table'> <tbody><tr><td><a href='javascript:deleteColumn(\\\"" + dropedValue + "\\\")' class='ui-icon ui-icon-close' ></a></td><td style='color: black;'>").append(getDropedmesNames().get(dropedList.indexOf(dropedValue))).append("</td></tr></tbody></table></li>");

            }
        }
        htmlTable.append("</ul></div></td></tr></table>");
        String resultStr = "{\"htmlStr\":\"" + htmlTable.toString() + "\",\"memberValues\":[\"" + Joiner.on("\",\"").join(dropedList) + "\"] ,\"isMemberUseInOtherLevel\":\"" + isdragavleRestrict + "\",\"groupName\":\"" + this.getGroupName() + "\"}";

        return resultStr;
    }

    public String getDragAndDropDivParam() {

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table style='width:90%;' border='solid black 1px' align='center'>");
        htmlTable.append("<tr><td width='50%' valign='top' class='draggedTable1'>");
        htmlTable.append("<div style='height:20px' class='ui-state-default draggedDivs ui-corner-all'><font size='2' style='font-weight:bold'>").append(dragDivTitle).append("</font></div>");
        htmlTable.append("<div id='myList3div' style='height:350px;overflow:auto' onscroll='onScrollDiv1(this)'><ul id='myList3' class='myList3 filetree treeview-famfamfam'>");
        for (String value : dragableList) {
            StringBuilder append = htmlTable;
            if (!value.trim().equalsIgnoreCase("")) {
                if (!contextPath.equals("")) {
                    append = htmlTable.append(" <li><img alt=''  src='").append(contextPath).append("/icons pinvoke/report.png'/><span class='myDragTabs' class='ui-state-default' id='").append(value).append("'>").append(getDragableListNames().get(dragableList.indexOf(value))).append("</span></li>");
                } else {
                    append = htmlTable.append(" <li style='width: 180px; height: 23px; color: white;' class='navtitle-hover myDragTabs ui-state-default sortable' id='" + value + "' ><span>").append(getDragableListNames().get(dragableList.indexOf(value))).append("</span></li>");
                }
            }
        }
        htmlTable.append("</ul></div></td> <td id='dropTabs' width='50%' valign='top'>");
        htmlTable.append("<div style='height:20px' class='ui-state-default draggedDivs ui-corner-all'><font size='2' style='font-weight:bold'>").append(dropDivTitle).append("</font></div>");
        htmlTable.append("<div id='dropDiv' style='height:350px;overflow:auto'> <ul id='sortable' class='sortable'>");
        if (!dropedList.isEmpty()) {
            for (String dropedValue : dropedList) {
                htmlTable.append("<li id='").append(dropedValue.trim()).append("_li").append("' style='width: 180px; height: auto; color: white;' class='navtitle-hover'>");
                htmlTable.append("<table id='").append(dropedValue.trim()).append("_table'> <tbody><tr><td><a href='javascript:deleteColumn(\\\"" + dropedValue + "\\\")' class='ui-icon ui-icon-close' ></a></td><td style='color: black;'>").append(getDropedmesNames().get(dropedList.indexOf(dropedValue))).append("</td></tr></tbody></table></li>");

            }
        }
        htmlTable.append("</ul></div></td></tr></table>");
        String resultStr = "{\"htmlStr\":\"" + htmlTable.toString() + "\",\"memberValues\":[\"" + Joiner.on("\",\"").join(dropedList) + "\"] ,\"isMemberUseInOtherLevel\":\"" + isdragavleRestrict + "\",\"groupName\":\"" + this.getGroupName() + "\"}";

        return resultStr;
    }

    public void setIsdragavleRestrict(boolean isdragavle) {
        this.isdragavleRestrict = isdragavle;
    }

    public ArrayList<String> getDragableListNames() {

        if (dragableListNames == null) {
            return dragableList;
        } else {
            return dragableListNames;
        }
    }

    public void setDragableListNames(ArrayList<String> dragableListNames) {
        this.dragableListNames = dragableListNames;
    }

    public ArrayList<String> getDropedmesNames() {
        if (dropedmesNames == null) {
            return dropedList;
        } else {
            return dropedmesNames;
        }
    }

    public void setDropedmesNames(ArrayList<String> dropedmesNames) {
        this.dropedmesNames = dropedmesNames;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
