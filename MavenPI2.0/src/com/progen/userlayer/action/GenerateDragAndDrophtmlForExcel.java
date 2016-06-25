package com.progen.userlayer.action;

import com.google.common.base.Joiner;
import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GenerateDragAndDrophtmlForExcel {

    private String dragDivTitle;
    private String dropDivTitle;
    private ArrayList<String> dropedList = new ArrayList<String>();
    private ArrayList<String> dragableList = new ArrayList<String>();
    private boolean isdragavleRestrict = false;
    private String contextPath;
    private ArrayList<String> dragableListNames = null;
    private ArrayList<String> dropedmesNames = null;
    private String groupName;

    public GenerateDragAndDrophtmlForExcel(String dragDivTitle, String dropDivTitle, ArrayList<String> dropedList, ArrayList<String> dragableList, String contextPath) {


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

    public GenerateDragAndDrophtmlForExcel(String groupName) {
        this.groupName = groupName;
    }
    // added by krishan pratap

    public GenerateDragAndDrophtmlForExcel() {
    }

    public String getDragAndDropDiv(HttpServletRequest request) {

        //String folderPath=(String)session.getAttribute("reportAdvHtmlFileProps")+"/importExcel";
        HttpSession session = request.getSession(false);
        String folderPath = (String) session.getAttribute("reportAdvHtmlFileProps") + "/importExcel";
        File folderDir = new File(folderPath);
        File[] allFilesAndDirs = folderDir.listFiles();
        ArrayList<String> list = new ArrayList<String>();
        for (File f1 : allFilesAndDirs) {
            list.add(f1.getName());
        }
        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table style='width:90%;' border='solid black 1px' align='center'>");
        htmlTable.append("<tr style='width:90%;' align='center'><center><select name='selName' id='selId' value=''><option value=''>-Select Template-</option>");
        for (String file : list) {
            htmlTable.append("<option value='" + file + "'>").append(file).append("</option>");
        }
        htmlTable.append("</select></center> </tr>");
        htmlTable.append("<tr><td width='45%' valign='top' class='draggedTable1'>");
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
        htmlTable.append("</ul></div></td> <td id='dropTabs' width='55%' valign='top'>");
        htmlTable.append("<div style='height:20px' class='ui-state-default draggedDivs ui-corner-all'><font size='2' style='font-weight:bold'>").append(dropDivTitle).append("</font></div>");
        //htmlTable.append("<div style='height:350px;overflow:auto'> <ul id='sortable' class='sortable'>");
        htmlTable.append("<form  id='sortable' class='sortable' style='height:350px;overflow:auto' name='myForm' action=''  method='POST' enctype='application/x-www-form-urlencoded'>");
        //htmlTable.append("<ul id='sortable' class='sortable'>");
        //htmlTable.append("<table align='center' id='tablesorterUserList' class='tablesorter'   cellpadding='0' cellspacing=1'>");
        //htmlTable.append("<tbody>");
        int i = 0;
        if (!dropedList.isEmpty()) {
            for (String dropedValue : dropedList) {
                //htmlTable.append("<li id='").append(dropedValue.trim()).append("_li").append("' style='width: 180px; height: auto; color: white;' class='navtitle-hover'>");
                htmlTable.append("<table id='").append(dropedValue.trim()).append("_table'> <tbody><tr style='width: 100%; height: auto; color: white;' class='navtitle-hover' ><td style='width: 5%';><a href='javascript:deleteColumn(\\\"" + dropedValue + "\\\")' class='ui-icon ui-icon-close' ></a></td><td style='width: 65%;color: black;'>").append(getDropedmesNames().get(dropedList.indexOf(dropedValue))).append("</td>");
//        htmlTable.append("<td style='width:13%'><input type='number' style='width:100%' name='").append(dropedValue.trim()).append("_sheetName' id='sheetNo' min='0' value='Sheet No.' onclick='document.sortable.'").append(dropedValue.trim()).append("_sheetName'.value='''></td>");
//         htmlTable.append("<td style='width:13%'><input type='number' style='width:100%' name='").append(dropedValue.trim()).append("_lineName' id='lineNo' min='0' value='Line NO.' onclick='document.sortable.'").append(dropedValue.trim()).append("_sheetName'.value='''></td>");
//         htmlTable.append("<td style='width:13%'><input type='number' style='width:100%' name='").append(dropedValue.trim()).append("_colName' id='colNo' min='0' value='Col NO.' onclick='document.sortable.'").append(dropedValue.trim()).append("_sheetName'.value='''></td>");
//         htmlTable.append("<td style='width:10%'><input type='checkbox' style='width:100%' name='").append(dropedValue.trim()).append("_headerName' id='headNo' value='' checked onclick='document.sortable.'").append(dropedValue.trim()).append("_sheetName'.value='''></td>");
                htmlTable.append("</tr></tbody></table>");
                i++;
            }

        }
        // htmlTable.append("<input type='hidden'   name='Counter' value ='").append(i).append("'/>");
        // htmlTable.append("<input type='submit' class='navtitle-hover' name='Download' value ='submit'/></ul></form></td></tr></table>");
        //htmlTable.append("<input type='submit' class='navtitle-hover'  name='Download' value ='submit'/>");
        //htmlTable.append("</form>");
        //usersStrBuilder.append("<center>");
        //htmlTable.append("</form></td></tr></table>");
//            htmlTable.append("<input type='submit' class='navtitle-hover' name='Download' value ='submit'/></form>");
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

    ///added by krishan
    public String getAllTemplate(HttpServletRequest request, String reportid) {

        HttpSession session = request.getSession(false);
        String folderPath = (String) session.getAttribute("reportAdvHtmlFileProps") + "/importTemplate" + "/" + reportid;
        File folderDir = new File(folderPath);
        File[] allFilesAndDirs = folderDir.listFiles();
        ArrayList<String> list = new ArrayList<String>();
        if (allFilesAndDirs != null) {
            for (File f1 : allFilesAndDirs) {
                list.add(f1.getName());
            }
        }
        StringBuilder htmlTable = new StringBuilder();

        htmlTable.append("<td style='width:130px'>Template Name :</td>");
        htmlTable.append("<td style='width:120px'align='right'><select name='selName' id='selId' style='width:130px' value=''><option value=''>Select Template</option>");
        for (String file : list) {
            htmlTable.append("<option value='" + file + "'>").append(file).append("</option>");
        }
        htmlTable.append("</select> </td>");
        return htmlTable.toString();
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
