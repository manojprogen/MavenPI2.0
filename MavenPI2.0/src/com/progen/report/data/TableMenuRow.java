/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.progen.query.RTDimensionElement;
import com.progen.query.RTMeasureElement;
import static com.progen.report.data.TableMenuFunctions.*;
import com.progen.users.UserLayerDAO;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author progen
 */
public class TableMenuRow extends TableRow {

    int viewByCount;
    int colCount;
    boolean isCrosstab;
    boolean isRowWisePercentDisplayed;
    String reportId;
    ArrayList<String> dataTypes;
    ArrayList displayLabels;
    String ctxPath;
    boolean isExcelDisplay;
    HashSet<String> menuPrivileges;
    boolean drillAcrossSupported;
    int columnCount;
    ArrayList<Boolean> isFormulaMsr = new ArrayList<Boolean>();
    boolean desigvalue;

    public Menu[] getMenu(int column) throws Exception {
        if (column < viewByCount) {
            return buildMenuForDimension(column);
        } else {
            return buildMenuForMeasure(column);
        }

    }

    private Menu[] buildMenuForDimension(int column) throws Exception {
        String columnId = super.rowDataIds.get(column);
        String rowViewById = columnId.substring(2);
        ArrayList<Menu> menuLst = new ArrayList<Menu>(); //added by mohit for kpi and none
        if (this.displayLabels.get(column) != null) {
            String rowViewByName = (this.displayLabels.get(column)).toString();
            Menu[] subMenu = new Menu[2];
            UserLayerDAO uDAO = new UserLayerDAO();
            String menuFunction;
            Menu menu = null;
//        if(menuPrivileges.contains("SORT")){
            if (uDAO.getFeatureEnableHashMap("Param_Sort", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                menu = new Menu("Sort");
                menu.setMenuFunction("#");
                subMenu[0] = new Menu("Sort Ascend");
                menuFunction = this.getMenuFunctionForSort(super.rowDataIds.get(column), 0, this.dataTypes.get(column), reportId, ctxPath);
                subMenu[0].setMenuFunction(menuFunction);
                subMenu[1] = new Menu("Sort Descend");
                menuFunction = this.getMenuFunctionForSort(super.rowDataIds.get(column), 1, this.dataTypes.get(column), reportId, ctxPath);
                subMenu[1].setMenuFunction(menuFunction);
                menu.setSubMenu(subMenu);
                menuLst.add(menu);
            }
            if (uDAO.getFeatureEnableHashMap("Parameter Rename", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                menu = new Menu("Parameter Rename");
                menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                menu.setMenuFunction(menuFunction);
                menuLst.add(menu);
            }
//        menu = new Menu("Date_format");
//        menu.setMenuFunction("#");
//        subMenu = new Menu[5];
//        subMenu[0] = createMenuFunctionforDDMMYY(column,"dd-MM-yy");
//        subMenu[1] = createMenuFunctionforDDMMYY(column,"dd-MMM-yy");
//        subMenu[2] = createMenuFunctionforDDMMYY(column,"MM-dd-yy");
//        subMenu[3] = createMenuFunctionforDDMMYY(column,"MMM-dd-yy");
//        subMenu[4] = createMenuFunctionforDDMMYY(column,"MM-dd-yyyy");
//        menu.setSubMenu(subMenu);
//        menuLst.add(menu);
            if (uDAO.getFeatureEnableHashMap("Operations(Parameter)", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                menu = new Menu("Other Operations");
                menu.setMenuFunction("#");
                if (!this.desigvalue) {
//                      if(userTypeAdmin.equalsIgnoreCase("SUPERADMIN")){
                    subMenu = new Menu[3];

//                        subMenu[0] = createMORenameMenu(column);

                    subMenu[0] = createMOPropertiesMenu(column);
                    if (uDAO.getFeatureEnableHashMap("Operations(Parameter)", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                        subMenu[1] = createMOScriptAlignMenu(column);
                    }
                    if (uDAO.getFeatureEnableHashMap("Operations(Parameter)", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                        subMenu[2] = createMOMeasureAlignMenu(column);
                    }
                    menu.setSubMenu(subMenu);
                    menuLst.add(menu);

//                    menu = new Menu("Script Align");
//                    menuFunction = this.getMenuFunctionForScriptAlign(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);

//                    menu = new Menu("Measure Align");
//                    menuFunction = this.getMenuFunctionForMeasureAlign(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
                } else {
                    subMenu = new Menu[1];
                    subMenu[0] = createMOPropertiesMenu(column);

                    menu.setSubMenu(subMenu);
                    menuLst.add(menu);
                }
            }
//        }
//        if(menuPrivileges.contains("SEGMENT")){
            if (column == 0) {
//                if ((RTDimensionElement.getDimensionType(super.rowDataIds.get(column)) != RTDimensionElement.SEGMENT) )
//                {
//
//                    menu = new Menu("Segment");
//                    menu.setMenuFunction("#");
//                    if (isCrosstab){
//                        subMenu = new Menu[1];
//                        subMenu[0] = new Menu("By Value");
//                        menuFunction = this.getMenuFunctionForByValue(reportId, rowViewById, rowViewByName, ctxPath);
//                        subMenu[0].setMenuFunction(menuFunction);
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);
//                    }
//                    else{
//                        subMenu = new Menu[2];
//                        subMenu[0] = new Menu("By Measure");
//                        subMenu[1] = new Menu("By Value");
//                        menuFunction = this.getMenuFunctionForSegment(reportId, ctxPath);
//                        subMenu[0].setMenuFunction(menuFunction);
//                        menuFunction = this.getMenuFunctionForByValue(reportId, rowViewById, rowViewByName, ctxPath);
//                        subMenu[1].setMenuFunction(menuFunction);
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);
//                    }
//                }
            }
//        }
//        if(menuPrivileges.contains("SEGMENT")){
//                if ( column == 1 &&  (RTDimensionElement.getDimensionType(super.rowDataIds.get(0)) == RTDimensionElement.SEGMENT) )
//                {
//
//                    menu = new Menu("Segment");
//                    menuFunction = this.getMenuFunctionForSegmentDefinition(reportId, ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
//                 }
//        }

            if (column == 0) {
                if (uDAO.getFeatureEnableHashMap("Show Duplicates", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                    menu = new Menu("Show Duplicate");
                    menuFunction = this.getMenuFunctionForDuplicate(super.rowDataIds.get(column), reportId, ctxPath);
                    menu.setMenuFunction(menuFunction);
                    menuLst.add(menu);
                }
            }
//        if(menuPrivileges.contains("CUSTOMSEQ"))
//        {
            if (viewByCount == 1 && !isCrosstab) {
                if (uDAO.getFeatureEnableHashMap("Custom Sequence", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                    menu = new Menu("Custom Sequence");
                    menuFunction = this.getMenuFunctionForCustomSeq(super.rowDataIds.get(column), reportId, ctxPath);
                    menu.setMenuFunction(menuFunction);
                    menuLst.add(menu);
                }
            }
            if (super.lockdataset != null && !super.lockdataset.isEmpty() && super.lockdataset.get(super.rowDataIds.get(column)).toString().equalsIgnoreCase("true")) {
                menu = new Menu("Release Dataset");
                boolean value = false;
                menuFunction = this.getMenuFunctionForLogDataset(super.rowDataIds.get(column), reportId, ctxPath, value);
                menu.setMenuFunction(menuFunction);
                menuLst.add(menu);
            } else {
                boolean value = true;
                menu = new Menu("Lock Dataset");
                menuFunction = this.getMenuFunctionForLogDataset(super.rowDataIds.get(column), reportId, ctxPath, value);
                menu.setMenuFunction(menuFunction);
                menuLst.add(menu);
            }


            if (isCrosstab) {
                if (!isRowWisePercentDisplayed) {
                    menu = new Menu("Row Wise %");
                    menuFunction = this.getMenuFunctionForRowWisePercent(super.rowDataIds.get(column), reportId, ctxPath);
                    menu.setMenuFunction(menuFunction);
                    menuLst.add(menu);
                } else {
                    menu = new Menu("Reset Row Wise %");
                    menuFunction = this.getMenuFunctionForRowWisePercent(super.rowDataIds.get(column), reportId, ctxPath);
                    menu.setMenuFunction(menuFunction);
                    menuLst.add(menu);
                }

            }
//        if (isCrosstab)
            if (viewByCount == 1 && isCrosstab && columnCount == 1) {
                menu = new Menu("RowWise ColorGroup");
                menuFunction = this.getMenuFunctionForRowWiseColorGroup(super.rowDataIds.get(column), reportId, ctxPath);
                menu.setMenuFunction(menuFunction);
                menuLst.add(menu);
            }

            if (drillAcrossSupported && !isExcelDisplay) {
                if (column == 0) {
                    menu = new Menu("Drill");
                    menuFunction = this.getMenuFunctionForDrill(super.rowDataIds.get(column), reportId, ctxPath);
                    menu.setMenuFunction(menuFunction);
                    menuLst.add(menu);

                    menu = new Menu("Drill Across");
                    menuFunction = this.getMenuFunctionForDrillAcross(super.rowDataIds.get(column), reportId, ctxPath);
                    menu.setMenuFunction(menuFunction);
                    menuLst.add(menu);
                }
            }
            if (viewByCount >= 2 && column == 0) {
                menu = new Menu("Grouping");
                menuFunction = this.getMenuFunctionForGrouping(super.rowDataIds.get(column), this.dataTypes.get(column), reportId, ctxPath);
                menu.setMenuFunction(menuFunction);
                menuLst.add(menu);

            }
////        start of code by Nazneen on Feb14 for Dimension Segment(Grouping)
            if ((RTDimensionElement.getDimensionType(super.rowDataIds.get(column)) != RTDimensionElement.SEGMENT)) {
                menu = new Menu("Dimension Segment");
                menuFunction = this.getMenuFunctionForDimSegment(super.rowDataIds.get(column), reportId, rowViewById, rowViewByName, ctxPath);
                menu.setMenuFunction(menuFunction);
                menuLst.add(menu);
            }
        }
////        end of code by Nazneen on Feb14 for Dimension Segment(Grouping)
        return menuLst.toArray(new Menu[]{});
    }

    private Menu[] buildMenuForMeasure(int column) throws Exception {
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        UserLayerDAO uDAO = new UserLayerDAO();
        Menu menu = null;
        Menu[] subMenu = new Menu[2];
        Menu[] subSortMenu = new Menu[2];
        Menu[] subSortMenuN = new Menu[2];
        String menuFunction;
//       if(menuPrivileges.contains("SORT")){
        if (uDAO.getFeatureEnableHashMap("Sort", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
            menu = new Menu("Sort");
            menu.setMenuFunction("#");
            subMenu[0] = new Menu("Sort Ascend");
            menuFunction = this.getMenuFunctionForSort(super.rowDataIds.get(column), 0, this.dataTypes.get(column), reportId, ctxPath);
            subMenu[0].setMenuFunction(menuFunction);
            subMenu[1] = new Menu("Sort Descend");
            menuFunction = this.getMenuFunctionForSort(super.rowDataIds.get(column), 1, this.dataTypes.get(column), reportId, ctxPath);
            subMenu[1].setMenuFunction(menuFunction);
            menu.setSubMenu(subMenu);
            menuLst.add(menu);
        }
//        }
                /*
         * belowe code is added by srikanth.p for sub total Sort Option
         */

        if (viewByCount >= 2) {
            menu = new Menu("SubTotal Sort");
            menu.setMenuFunction("#");
            subSortMenu[0] = new Menu("Sort Ascend");
            String submenuFunction1 = this.getMenuFunctionForSubSort(super.rowDataIds.get(column), 0, this.dataTypes.get(column), reportId, ctxPath);
            subSortMenu[0].setMenuFunction(submenuFunction1);
            subSortMenu[1] = new Menu("Sort Descend");
            String submenuFunction2 = this.getMenuFunctionForSubSort(super.rowDataIds.get(column), 1, this.dataTypes.get(column), reportId, ctxPath);
            subSortMenu[1].setMenuFunction(submenuFunction2);
            menu.setSubMenu(subSortMenu);
            menuLst.add(menu);
        }

        /*
         * ended by srikanth.p
         */

        /*
         * belowe code is added by Govardhan for sub total Tob/Bottom
         */

        if (viewByCount >= 2) {
            menu = new Menu("SubTotal Top/Bottom");
            menu.setMenuFunction("#");
            //Code Modified
            subSortMenuN = new Menu[6];
            subSortMenuN[0] = new Menu("Top 3");
            String menuFunctionN = this.getMenuFunctionForSubTotalTopRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 3, ctxPath);
            subSortMenuN[0].setMenuFunction(menuFunctionN);
            subSortMenuN[1] = new Menu("Bottom 3");
            String submenuFunctionN = this.getMenuFunctionForSubTotalBottomRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 3, ctxPath);
            subSortMenuN[1].setMenuFunction(submenuFunctionN);
            //End of code By Amar
            subSortMenuN[2] = new Menu("Top 5");
            menuFunction = this.getMenuFunctionForSubTotalTopRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5, ctxPath);
//                    String submenuFunction1=this.getMenuFunctionForSubSort(super.rowDataIds.get(column), 0, this.dataTypes.get(column), reportId,ctxPath);
            subSortMenuN[2].setMenuFunction(menuFunction);
            subSortMenuN[3] = new Menu("Bottom 5");
            String submenuFunction2 = this.getMenuFunctionForSubTotalBottomRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5, ctxPath);
            subSortMenuN[3].setMenuFunction(submenuFunction2);
            subSortMenuN[4] = new Menu("Top 10");
            String submenuFunction3 = this.getMenuFunctionForSubTotalTopRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 10, ctxPath);
            subSortMenuN[4].setMenuFunction(submenuFunction3);
            subSortMenuN[5] = new Menu("Bottom 10");
            String submenuFunction4 = this.getMenuFunctionForSubTotalBottomRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 10, ctxPath);
            subSortMenuN[5].setMenuFunction(submenuFunction4);

            menu.setSubMenu(subSortMenuN);
            menuLst.add(menu);

            menu = new Menu("SubTotal Filter");
            menuFunction = this.getMenuFunctionForSubTotalSearch(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
            menu.setMenuFunction(menuFunction);
            menuLst.add(menu);

        }

        /*
         * ended by Govardhan
         */

        if (RTMeasureElement.isRunTimeMeasure(super.rowDataIds.get(column)) && !this.desigvalue) {
//            if (menuPrivileges.contains("RESET")) {
            menu = new Menu("Reset");
            menuFunction = this.getMenuFunctionForReset(super.rowDataIds.get(column), this.dataTypes.get(column), reportId, ctxPath);
            menu.setMenuFunction(menuFunction);
            menuLst.add(menu);
//            }
//menuPrivileges.contains("STATISTICS") && 
            if (!this.desigvalue) {
                if (uDAO.getFeatureEnableHashMap("Statistical Options", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                    menu = createStatisticsMenu(column);
                    menuLst.add(menu);
                }
            }
//            if(menuPrivileges.contains("COLGROUP"))
//                {
            menu = new Menu("Color Group");
            menu.setMenuFunction("#");

            subMenu = new Menu[2];
            subMenu[0] = new Menu("Create/Edit");
            menuFunction = this.getMenuFunctionForColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId, ctxPath);
            subMenu[0].setMenuFunction(menuFunction);

            subMenu[1] = new Menu("Reset");
            menuFunction = this.getMenuFunctionForResetColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId);
            subMenu[1].setMenuFunction(menuFunction);

            menu.setSubMenu(subMenu);
            menuLst.add(menu);
            if (uDAO.getFeatureEnableHashMap("Measure Rename", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                menu = new Menu("Parameter Rename");
                menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                menu.setMenuFunction(menuFunction);
                menuLst.add(menu);
            }
            menu = new Menu("Round");
            menu.setMenuFunction("#");
            subMenu = new Menu[6];

            subMenu[0] = new Menu("No Decimal");
            menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 0);
            subMenu[0].setMenuFunction(menuFunction);

            subMenu[1] = new Menu("One Decimal");
            menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 1);
            subMenu[1].setMenuFunction(menuFunction);

            subMenu[2] = new Menu("Two Decimal");
            menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 2);
            subMenu[2].setMenuFunction(menuFunction);

            subMenu[3] = new Menu("Three Decimal");
            menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 3);
            subMenu[3].setMenuFunction(menuFunction);

            subMenu[4] = new Menu("Four Decimal");
            menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 4);
            subMenu[4].setMenuFunction(menuFunction);

            subMenu[5] = new Menu("Five Decimal");
            menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5);
            subMenu[5].setMenuFunction(menuFunction);

            menu.setSubMenu(subMenu);
            menuLst.add(menu);


            //    }
//              if(menuPrivileges.contains("GOALSEEK"))
//                {
//                        menu = new Menu("Goal Seek");
//                        menu.setMenuFunction("#");
//
//                        subMenu=new Menu[1];
//                        subMenu[0]=new Menu("Basic");
//                        menuFunction = this.getMenuFunctionForGoalSeekBasic(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath,false,true);
//                        subMenu[0].setMenuFunction(menuFunction);
//
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);
//                }
        } else {
            if (!this.dataTypes.get(column).equalsIgnoreCase("D")) {
                if (isCrosstab == false) {
                    if (!this.desigvalue) {

                        if (uDAO.getFeatureEnableHashMap("Quick Operations", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                            menu = new Menu("Quick Operations");
                            menu.setMenuFunction("#");
                            if (uDAO.getFeatureEnableHashMap("Custom KPI", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                                subMenu = new Menu[6];
                            } else {
                                subMenu = new Menu[5];
                            }
                            subMenu[0] = createQOPercentWiseMenu(column);

                            subMenu[1] = createQOTopBottomMenu(column);


                            subMenu[2] = createQOTopBottomPercentMenu(column);

                            subMenu[3] = createQORoundMenu(column);

                            subMenu[4] = createQOColorGroupMenu(column);

                            if (uDAO.getFeatureEnableHashMap("Custom KPI", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                                subMenu[5] = createComplexKPIMenu(column);
                            }
                            menu.setSubMenu(subMenu);
                            menuLst.add(menu);
                        }
                        if (uDAO.getFeatureEnableHashMap("Measure Rename", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                            menu = new Menu("Parameter Rename");
                            menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                            menu.setMenuFunction(menuFunction);
                            menuLst.add(menu);
                        }

                        if (uDAO.getFeatureEnableHashMap("Other Operations", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                            menu = new Menu("Other Operations");
                            menu.setMenuFunction("#");
                        }

//                    menu = new Menu("% wise");
//                    menu.setMenuFunction("#");
//                    if(viewByCount>1)
//                    subMenu = new Menu[4];
//                    else
//                    subMenu=new Menu[2];
//
//                    subMenu[0] = new Menu("% wise");
//                    menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath,false,false);
//                    subMenu[0].setMenuFunction(menuFunction);
//
//                    subMenu[1] = new Menu("% wise with Absolute");
//                    menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath,false,true);
//                    subMenu[1].setMenuFunction(menuFunction);
//
//                    if(viewByCount>1){
//                    subMenu[2] = new Menu("% wise(subtotal)");
//                    menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath,true,false);
//                    subMenu[2].setMenuFunction(menuFunction);
//
//                    subMenu[3] = new Menu("% wise with Absolute(subtotal)");
//                    menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath,true,true);
//                    subMenu[3].setMenuFunction(menuFunction);
//                    }
//
//                    menu.setSubMenu(subMenu);
//                    menuLst.add(menu);
//  //              }
////                if(menuPrivileges.contains("TOP10BOT10")){
//                        menu = new Menu("Top/Bottom");
//                        menu.setMenuFunction("#");
//                        subMenu = new Menu[6];
//
//                        subMenu[0] = new Menu("Top 5");
//                        menuFunction = this.getMenuFunctionForTopRows(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 5);
//                        subMenu[0].setMenuFunction(menuFunction);
//
//                        subMenu[1] = new Menu("Bottom 5");
//                        menuFunction = this.getMenuFunctionForBottomRows(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 5);
//                        subMenu[1].setMenuFunction(menuFunction);
//
//                        subMenu[2] = new Menu("Top 10");
//                        menuFunction = this.getMenuFunctionForTopRows(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 10);
//                        subMenu[2].setMenuFunction(menuFunction);
//
//                        subMenu[3] = new Menu("Bottom 10");
//                        menuFunction = this.getMenuFunctionForBottomRows(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 10);
//                        subMenu[3].setMenuFunction(menuFunction);
//
//                        subMenu[4] = new Menu("Top 25");
//                        menuFunction = this.getMenuFunctionForTopRows(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 25);
//                        subMenu[4].setMenuFunction(menuFunction);
//
//                        subMenu[5] = new Menu("Bottom 25");
//                        menuFunction = this.getMenuFunctionForBottomRows(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 25);
//                        subMenu[5].setMenuFunction(menuFunction);
//
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);
                    }
//                if(menuPrivileges.contains("TOP10BOT10PER")){
                    //                        menu = new Menu("Top/Bottom (Percent)");
//                        menu.setMenuFunction("#");
//                        subMenu = new Menu[6];
//
//                        subMenu[0] = new Menu("Top 5 %");
//                        menuFunction = this.getMenuFunctionForTopRowsPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 5);
//                        subMenu[0].setMenuFunction(menuFunction);
//
//                        subMenu[1] = new Menu("Bottom 5%");
//                        menuFunction = this.getMenuFunctionForBottomRowsPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 5);
//                        subMenu[1].setMenuFunction(menuFunction);
//
//                        subMenu[2] = new Menu("Top 10%");
//                        menuFunction = this.getMenuFunctionForTopRowsPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 10);
//                        subMenu[2].setMenuFunction(menuFunction);
//
//                        subMenu[3] = new Menu("Bottom 10%");
//                        menuFunction = this.getMenuFunctionForBottomRowsPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 10);
//                        subMenu[3].setMenuFunction(menuFunction);
//
//                        subMenu[4] = new Menu("Top 25%");
//                        menuFunction = this.getMenuFunctionForTopRowsPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 25);
//                        subMenu[4].setMenuFunction(menuFunction);
//
//                        subMenu[5] = new Menu("Bottom 25%");
//                        menuFunction = this.getMenuFunctionForBottomRowsPercentWise(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 25);
//                        subMenu[5].setMenuFunction(menuFunction);
//
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);
//                }
//                if(menuPrivileges.contains("STATISTICS") && !this.desigvalue){
//                        menu = createStatisticsMenu(column);
//                        menuLst.add(menu);
//
//                }
                    if (!this.desigvalue) {
                        if (uDAO.getFeatureEnableHashMap("Statistical Options", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                            menu = createStatisticsMenu(column);
                            menuLst.add(menu);
                        }

                    }

//                if(menuPrivileges.contains("ROUND")){
//                        menu = new Menu("Round");
//                        menu.setMenuFunction("#");
//                        subMenu = new Menu[6];
//
//                        subMenu[0] = new Menu("No Decimal");
//                        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 0);
//                        subMenu[0].setMenuFunction(menuFunction);
//
//                        subMenu[1] = new Menu("One Decimal");
//                        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 1);
//                        subMenu[1].setMenuFunction(menuFunction);
//
//                        subMenu[2] = new Menu("Two Decimal");
//                        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 2);
//                        subMenu[2].setMenuFunction(menuFunction);
//
//                        subMenu[3] = new Menu("Three Decimal");
//                        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 3);
//                        subMenu[3].setMenuFunction(menuFunction);
//
//                        subMenu[4] = new Menu("Four Decimal");
//                        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 4);
//                        subMenu[4].setMenuFunction(menuFunction);
//
//                        subMenu[5] = new Menu("Five Decimal");
//                        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, 5);
//                        subMenu[5].setMenuFunction(menuFunction);
//
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);

//                }

//                if(menuPrivileges.contains("COLGROUP"))
//                {
                    //                        menu = new Menu("Color Group");
//                        menu.setMenuFunction("#");
//
//                        subMenu=new Menu[2];
//                        subMenu[0]=new Menu("Create/Edit");
//                        menuFunction = this.getMenuFunctionForColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId);
//                        subMenu[0].setMenuFunction(menuFunction);
//
//                        subMenu[1]=new Menu("Reset");
//                        menuFunction=this.getMenuFunctionForResetColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId);
//                        subMenu[1].setMenuFunction(menuFunction);
//
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);
//                }
                    //commented and recoded new user for spanco
//                if(menuPrivileges.contains("PARAMFILT") && !this.desigvalue)
//                {
//                    menu = new Menu("Parameter Filter");
//                    menuFunction = this.getMenuFunctionForParameterFilter(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
//                }
                    if (!this.desigvalue) {
                        if (uDAO.getFeatureEnableHashMap("Quick Formulas", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
//                    menu = new Menu("Parameter Filter");
                            menu = new Menu("Formula's");
                            menu.setMenuFunction("#");
                            if (isFormulaMsr.get(column)) {
//                        subMenu = new Menu[5];
                                subMenu = new Menu[7];
                            } else {
//                        subMenu = new Menu[4];
                                subMenu = new Menu[6];
                            }
                            subMenu[0] = createPFParameterFilterMenu(column);

                            subMenu[1] = createPFConversionFormulaMenu(column);

                            subMenu[2] = createPFFactFilterMenu(column);

                            subMenu[3] = cretePFQuickTimeBaseFormula(column);

                            subMenu[4] = cretePFDateFormula(column);
                            subMenu[5] = createPFSignConversion(column);

                            if (isFormulaMsr.get(column)) {

//                        subMenu[4] = createPFEditFormula(column);
                                subMenu[6] = createPFEditFormula(column);
                            }

                            menu.setSubMenu(subMenu);
                            menuLst.add(menu);
                        }
                    } else {
                        if (isFormulaMsr.get(column)) {
                            menu = new Menu("Parameter Filter");
                            menu.setMenuFunction("#");
                            subMenu = new Menu[1];
                            subMenu[0] = createPFEditFormula(column);
                            menu.setSubMenu(subMenu);
                            menuLst.add(menu);
                        }
                    }
//                 if (!this.desigvalue) {
//                menu = new Menu("Advance Formula");
//                menuFunction = this.getMenuFunctionForAdvanceFormula(super.rowDataIds.get(column),  this.displayLabels.get(column).toString(), reportId,ctxPath);
//                menu.setMenuFunction(menuFunction);
//                menuLst.add(menu);
//                 }
                    //added by Nazneen for Segment
                    if (!this.desigvalue) {
                        menu = new Menu("Segment");
                        menuFunction = this.getMenuFunctionForSegmentation(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                        menu.setMenuFunction(menuFunction);
                        menuLst.add(menu);
                    }

//                if( !this.desigvalue)
//                {
//                    menu = new Menu("Parameter Filter");
//                    menuFunction = this.getMenuFunctionForParameterFilter(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
//                }
//                 if( !this.desigvalue)
//                {
//                    menu = new Menu("Conversion Formula");
//                    menuFunction=this.getMenuFunctionForConversionFormula(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
//                 }
//                  if(!this.desigvalue){
//                      menu=new Menu("Fact Filter");
//                      menuFunction=this.getMenuFunctionForFactFilter(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath);
//                      menu.setMenuFunction(menuFunction);
//                      menuLst.add(menu);
//                  }
//               menu = new Menu("Segment");
//                menuFunction = this.getMenuFunctionForSegment(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                
//                
//                
//                
//                menu.setMenuFunction(menuFunction);
//                menuLst.add(menu);
//               menu = new Menu("Segment");
//                menuFunction = this.getMenuFunctionForSegment(super.rowDataIds.get(column),  this.displayLabels.get(column).toString(), reportId,ctxPath);
//                menu.setMenuFunction(menuFunction);
//                menuLst.add(menu);
                    //               if(menuPrivileges.contains("TABPROPS"))
                    //               {
//                    menu = new Menu("Properties");
//                    menuFunction = this.getMenuFunctionForProperties(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
//                }
//                if(!this.desigvalue)
//                {
//                    menu = new Menu("Rename");
//                    menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
//                }
//                if(menuPrivileges.contains("CUSTOMKPI") && !this.desigvalue)
//                {
//                        menu = new Menu("Custom KPI");
//                        menuFunction = this.getMenuFunctionForcustomKpi(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId);
//                        menu.setMenuFunction(menuFunction);
//                        menuLst.add(menu);
//
//                }





//                  if(menuPrivileges.contains("EDITTABLE"))
//                {
//                     if(isFormulaMsr.get(column))
//                    {
//                        menu=new Menu("Edit Formula");
//                        menuFunction=this.getMenuFunctionForEditTableMsr(super.rowDataIds.get(column),(String)this.displayLabels.get(column),reportId);
//                        menu.setMenuFunction(menuFunction);
//                        menuLst.add(menu);
//                    }
//                }
                    if (!this.desigvalue) {
                        if (uDAO.getFeatureEnableHashMap("Goal Seek", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                            menu = new Menu("Goal Seek");
                            menu.setMenuFunction("#");

                            subMenu = new Menu[4];
//                        subMenu[0]=new Menu("Basic");
//                        menuFunction = this.getMenuFunctionForGoalSeekBasic(super.rowDataIds.get(column),  this.displayLabels.get(column).toString(), reportId, ctxPath,false,true);
//                        subMenu[0].setMenuFunction(menuFunction);


                            subMenu[0] = new Menu("Adhoc");
                            menuFunction = this.getMenuFunctionForGoalSeekAdhoc(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath, false, true);
                            subMenu[0].setMenuFunction(menuFunction);

                            subMenu[1] = new Menu("Time Base");
                            menuFunction = this.getMenuFunctionForGoalSeekTimeBase(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                            subMenu[1].setMenuFunction(menuFunction);

                            subMenu[2] = new Menu("NPI Time Basis");
                            menuFunction = this.getMenuFunctionForGoalSeekNPITimechange(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                            subMenu[2].setMenuFunction(menuFunction);

                            subMenu[3] = new Menu("NPI % Basis");
                            menuFunction = this.getMenuFunctionForGoalSeekNPIPerceBase(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                            subMenu[3].setMenuFunction(menuFunction);

                            menu.setSubMenu(subMenu);
                            menuLst.add(menu);
                        }
                    }
//                if(uDAO.getFeatureEnableHashMap("Parameter Rename",paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")){
//                  menu = new Menu("Parameter Rename");
//                menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column),  this.displayLabels.get(column).toString(), reportId, ctxPath);
//                menu.setMenuFunction(menuFunction);
//                menuLst.add(menu);
//                }
                    if (uDAO.getFeatureEnableHashMap("Other Operations", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                        menu = new Menu("Other Operations");
                        menu.setMenuFunction("#");
                        if (!this.desigvalue) {
                            if (!uDAO.getFeatureEnableHashMap("Operations(Parameter)", paramhashmapPA) && !userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                                subMenu = new Menu[2];
                                subMenu[0] = createMOScriptAlignMenu(column);
                                subMenu[1] = createMOMeasureAlignMenu(column);
                            } else {
                                subMenu = new Menu[4];
                                subMenu[0] = createMOPropertiesMenu(column);
                                subMenu[1] = createMOScriptAlignMenu(column);
                                subMenu[2] = createMOMeasureAlignMenu(column);
                                subMenu[3] = createMOModifyMeasureMenu(column);

                            }

                            menu.setSubMenu(subMenu);
                            menuLst.add(menu);

//                    menu = new Menu("Script Align");
//                    menuFunction = this.getMenuFunctionForScriptAlign(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);

//                    menu = new Menu("Measure Align");
//                    menuFunction = this.getMenuFunctionForMeasureAlign(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
                        } else {
                            subMenu = new Menu[1];
                            subMenu[0] = createMOPropertiesMenu(column);

                            menu.setSubMenu(subMenu);
                            menuLst.add(menu);
                        }
                    }
//                if(!this.desigvalue)
//                {
//                    menu = new Menu("Quick TimeBased Formula");
//                    menuFunction = this.getQuickTimeBasedFormula(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
//                }  

                } else {
                    if (!this.desigvalue) {
                        menu = new Menu("% wise");
                        menu.setMenuFunction("#");
                        if (viewByCount > 1) {
                            subMenu = new Menu[4];
                        } else {
                            subMenu = new Menu[2];
                        }

                        subMenu[0] = new Menu("% wise");
                        menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, false, false);
                        subMenu[0].setMenuFunction(menuFunction);

                        subMenu[1] = new Menu("% wise with Absolute");
                        menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, false, true);
                        subMenu[1].setMenuFunction(menuFunction);

                        if (viewByCount > 1) {
                            subMenu[2] = new Menu("% wise(subtotal)");
                            menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, true, false);
                            subMenu[2].setMenuFunction(menuFunction);

                            subMenu[3] = new Menu("% wise with Absolute(subtotal)");
                            menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, true, true);
                            subMenu[3].setMenuFunction(menuFunction);
                        }

                        menu.setSubMenu(subMenu);
                        menuLst.add(menu);

                    }
//                 if(menuPrivileges.contains("COLGROUP"))
//                {
                    menu = new Menu("Color Group");
                    menu.setMenuFunction("#");

                    subMenu = new Menu[2];
                    subMenu[0] = new Menu("Create/Edit");
                    menuFunction = this.getMenuFunctionForColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId, ctxPath);
                    subMenu[0].setMenuFunction(menuFunction);

                    subMenu[1] = new Menu("Reset");
                    menuFunction = this.getMenuFunctionForResetColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId);
                    subMenu[1].setMenuFunction(menuFunction);

                    menu.setSubMenu(subMenu);
                    menuLst.add(menu);
//                }
//                   if(menuPrivileges.contains("GOALSEEK"))
//                {
//                        menu = new Menu("Goal Seek");
//                        menu.setMenuFunction("#");
//
//                        subMenu=new Menu[1];
//                        subMenu[0]=new Menu("Basic");
//                        menuFunction = this.getMenuFunctionForGoalSeekBasic(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId, ctxPath,false,true);
//                        subMenu[0].setMenuFunction(menuFunction);
//
//
//                        menu.setSubMenu(subMenu);
//                        menuLst.add(menu);
//                }
//                  if(menuPrivileges.contains("ROUND")){
                    menu = new Menu("Round");
                    menu.setMenuFunction("#");
                    subMenu = new Menu[6];

                    subMenu[0] = new Menu("No Decimal");
                    menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, 0);
                    subMenu[0].setMenuFunction(menuFunction);

                    subMenu[1] = new Menu("One Decimal");
                    menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, 1);
                    subMenu[1].setMenuFunction(menuFunction);

                    subMenu[2] = new Menu("Two Decimal");
                    menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, 2);
                    subMenu[2].setMenuFunction(menuFunction);

                    subMenu[3] = new Menu("Three Decimal");
                    menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, 3);
                    subMenu[3].setMenuFunction(menuFunction);

                    subMenu[4] = new Menu("Four Decimal");
                    menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, 4);
                    subMenu[4].setMenuFunction(menuFunction);

                    subMenu[5] = new Menu("Five Decimal");
                    menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, 5);
                    subMenu[5].setMenuFunction(menuFunction);

                    menu.setSubMenu(subMenu);
                    menuLst.add(menu);

//                }
                    menu = new Menu("Number format");
                    menu.setMenuFunction("#");
                    subMenu = new Menu[5];
                    subMenu[0] = new Menu("Absolute");
                    menuFunction = this.getMenuFunctionForNumberFormat(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, "A");
                    subMenu[0].setMenuFunction(menuFunction);
                    subMenu[1] = new Menu("Thousands(K)");
                    menuFunction = this.getMenuFunctionForNumberFormat(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, "K");
                    subMenu[1].setMenuFunction(menuFunction);
                    subMenu[2] = new Menu("Millions(M)");
                    menuFunction = this.getMenuFunctionForNumberFormat(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, "M");
                    subMenu[2].setMenuFunction(menuFunction);

                    subMenu[3] = new Menu("Lakhs(L)");
                    menuFunction = this.getMenuFunctionForNumberFormat(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, "L");
                    subMenu[3].setMenuFunction(menuFunction);

                    subMenu[4] = new Menu("Crores(Cr)");
                    menuFunction = this.getMenuFunctionForNumberFormat(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath, "Cr");
                    subMenu[4].setMenuFunction(menuFunction);
                    menu.setSubMenu(subMenu);
                    menuLst.add(menu);
                    if (uDAO.getFeatureEnableHashMap("Measure Rename", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                        menu = new Menu("Parameter Rename");
                        menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                        menu.setMenuFunction(menuFunction);
                        menuLst.add(menu);
                    }
                    if (uDAO.getFeatureEnableHashMap("Other Operations", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                        menu = new Menu("Other Operations");
                        menu.setMenuFunction("#");
                        subMenu = new Menu[3];

//              subMenu[0] = createMORenameMenu(column);

                        subMenu[0] = createMOPropertiesMenu(column);

                        subMenu[1] = createMOScriptAlignMenu(column);

                        subMenu[2] = createMOMeasureAlignMenu(column);

                        menu.setSubMenu(subMenu);
                        menuLst.add(menu);
                    }
                    if (!this.desigvalue) {
                        menu = new Menu("Script Align");
                        menuFunction = this.getMenuFunctionForScriptAlign(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath);
                        menu.setMenuFunction(menuFunction);
                        menuLst.add(menu);
                    }
                    if (!this.desigvalue) {
                        menu = new Menu("Measure Align");
                        menuFunction = this.getMenuFunctionForMeasureAlign(super.rowDataIds.get(column), (String) ((ArrayList) displayLabels.get(column)).get(colCount), reportId, ctxPath);
                        menu.setMenuFunction(menuFunction);
                        menuLst.add(menu);
                    }
                }
            } else {
                if (uDAO.getFeatureEnableHashMap("Measure Rename", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                    menu = new Menu("Parameter Rename");
                    menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
                    menu.setMenuFunction(menuFunction);
                    menuLst.add(menu);
                }
                if (uDAO.getFeatureEnableHashMap("Other Operations", paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
                    menu = new Menu("Other Operations");
                    menu.setMenuFunction("#");
                    if (!this.desigvalue) {
                        subMenu = new Menu[3];

//                        subMenu[0] = createMORenameMenu(column);

                        subMenu[0] = createMOPropertiesMenu(column);

                        subMenu[1] = createMOScriptAlignMenu(column);

                        subMenu[2] = createMOMeasureAlignMenu(column);

                        menu.setSubMenu(subMenu);
                        menuLst.add(menu);

//                    menu = new Menu("Script Align");
//                    menuFunction = this.getMenuFunctionForScriptAlign(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);

//                    menu = new Menu("Measure Align");
//                    menuFunction = this.getMenuFunctionForMeasureAlign(super.rowDataIds.get(column), (String) this.displayLabels.get(column), reportId,ctxPath);
//                    menu.setMenuFunction(menuFunction);
//                    menuLst.add(menu);
                    } else {
                        subMenu = new Menu[1];
                        subMenu[0] = createMOPropertiesMenu(column);

                        menu.setSubMenu(subMenu);
                        menuLst.add(menu);
                    }
                }
            }
        }
        return menuLst.toArray(new Menu[]{});
    }

    private String getMenuFunctionForSort(String elementId, int sortType, String dataType, String reportId, String ctxPath) {
        String menuFunction = SORT_FUNCTION;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + sortType + "\\\',\\\'" + dataType + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + sortType + "','" + dataType + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForSubSort(String elementId, int sortType, String dataType, String reportId, String ctxPath) {
        String menuFunction = SUBTOTAL_SORT;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + sortType + "\\\',\\\'" + dataType + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + sortType + "','" + dataType + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForDuplicate(String elementId, String reportId, String ctxPath) {
        String menuFunction = SHOW_DUPLICATE;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForLogDataset(String elementId, String reportId, String ctxPath, boolean lockdata) {
        String menuFunction = LOCK_DATASET;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\',\\\'" + lockdata + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + reportId + "','" + ctxPath + "','" + lockdata + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForDrill(String elementId, String reportId, String ctxPath) {
        String menuFunction = DRILL;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForDrillAcross(String elementId, String reportId, String ctxPath) {
        String menuFunction = DRILL_ACROSS;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForPercentWise(String elementId, String displayLabel, String reportId, String ctxPath, boolean onSubtotal, boolean withAbsolute) {
        String menuFunction = PERCENTWISE_FUNCTION;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\',\\\'" + onSubtotal + "\\\',\\\'" + withAbsolute + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "','" + onSubtotal + "','" + withAbsolute + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForParameterFilter(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = PARAMETER_FILTER;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForConversionFormula(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = CONVERSION_FORMULA;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForSignConversion(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = SIGN_CONVERSION;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForFactFilter(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = FACT_FILTER;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForTopRows(String elementId, String displayLabel, String reportId, int noOfRows, String ctxPath) {
        String menuFunction = TOP_ROWS;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + noOfRows + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + noOfRows + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForSubTotalTopRows(String elementId, String displayLabel, String reportId, int noOfRows, String ctxPath) {
        String menuFunction = SUBTOTAL_TOP;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + noOfRows + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + noOfRows + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForSubTotalBottomRows(String elementId, String displayLabel, String reportId, int noOfRows, String ctxPath) {
        String menuFunction = SUBTOTAL_BOTTOM;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + noOfRows + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + noOfRows + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForTopRowswithOthers(String elementId, String displayLabel, String reportId, int noOfRows, String ctxPath) {
        String menuFunction = TOP_ROWS_OTHERS;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + noOfRows + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + noOfRows + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForTopRowsPercentWise(String elementId, String displayLabel, String reportId, int noOfRows, String ctxPath) {
        String menuFunction = TOP_ROWS_PERCENTWISE;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + noOfRows + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + noOfRows + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForRounding(String elementId, String displayLabel, String reportId, int precision) {
        String menuFunction = ROUNDING;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + precision + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + precision + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForBottomRowsPercentWise(String elementId, String displayLabel, String reportId, int noOfRows, String ctxPath) {
        String menuFunction = BOTTOM_ROWS_PERCENTWISE;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + noOfRows + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + noOfRows + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForBottomRows(String elementId, String displayLabel, String reportId, int noOfRows, String ctxPath) {
        String menuFunction = BOTTOM_ROWS;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + noOfRows + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + noOfRows + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForStatistics(String elementId, String displayLabel, String reportId, String type, String ctxPath) {
        String menuFunction = STATISTICS;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + type + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + type + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForRunningTotal(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = RUNNING_TOTAL;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + RTMeasureElement.RUNNING_TOTAL_COLUMN + "\\\',\\\'" + RTMeasureElement.RUNNINGTOTAL.getColumnType() + "\\\',\\\'" + RTMeasureElement.RUNNINGTOTAL.getColumnDisplay() + "\\\',\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + RTMeasureElement.RUNNING_TOTAL_COLUMN + "','" + RTMeasureElement.RUNNINGTOTAL.getColumnType() + "','" + RTMeasureElement.RUNNINGTOTAL.getColumnDisplay() + "','" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForRank(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = RANK;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + RTMeasureElement.RANK_COLUMN + "\\\',\\\'" + RTMeasureElement.RANK.getColumnType() + "\\\',\\\'" + RTMeasureElement.RANK.getColumnDisplay() + "\\\',\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + RTMeasureElement.RANK_COLUMN + "','" + RTMeasureElement.RANK.getColumnType() + "','" + RTMeasureElement.RANK.getColumnDisplay() + "','" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;

    }

    private String getMenuFunctionForRankPrior(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = RANK_PRIOR;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + RTMeasureElement.RANK.getColumnType() + "\\\',\\\'" + RTMeasureElement.RANK.getColumnDisplay() + "\\\',\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + RTMeasureElement.RANK.getColumnType() + "','" + RTMeasureElement.RANK.getColumnDisplay() + "','" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;

    }

    private String getMenuFunctionForByValue(String reportId, String rowViewById, String rowViewByName, String ctxPath) {
        String menuFunction = SELECT_BY_VALUE;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + reportId + "\\\',\\\'" + rowViewById + "\\\',\\\'" + rowViewByName + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + reportId + "','" + rowViewById + "','" + rowViewByName + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForSegment(String reportId, String ctxPath) {
        String menuFunction = SELECTMEASURE;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForSegmentDefinition(String reportId, String ctxPath) {
        String menuFunction = GET_SEGMENT_DEFINITION;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForSegment(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = SEGMENT;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForProperties(String elementId, String displayLabel, String reportId) {
        String menuFunction = PROPERTIES;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForColorGroup(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = COLOR_GROUP;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForResetColorGroup(String elementId, String displayLabel, String reportId) {
        String menuFunction = RESET_COLOR_GROUP;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForNumberFormat(String elementId, String displayLabel, String reportId, String ctxpath, String numFrmt) {
        String menuFunction = NUMBER_FORMAT;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxpath + "\\\',\\\'" + numFrmt + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + numFrmt + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForRowWisePercent(String elementId, String reportId, String path) {
        String menufunction = ROW_WISE_PERCENT;
        if (isExcelDisplay) {
            menufunction = menufunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + path + "\\\')";

        } else {
            menufunction = menufunction + "('" + elementId + "','" + reportId + "','" + path + "')";

        }
        return menufunction;
    }

    @Override
    public String getRowData(int column) {
        return "";
    }

    @Override
    public String getID(int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isExcelDisplay() {
        return isExcelDisplay;
    }

    public HashSet<String> getMenuPrivileges() {
        return menuPrivileges;
    }

    public void setMenuPrivileges(HashSet<String> menuPrivileges) {
        this.menuPrivileges = menuPrivileges;
    }

    private String getMenuFunctionForReset(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = RESET;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private Menu createCorrelationMenu(int column) {
        String displayLabel;
        String corrDisplayLabel;
        String colId;
        displayLabel = (String) this.displayLabels.get(column);
        colId = rowDataIds.get(column);
        // int size=displayLabels.size()-viewByCount-1;
        Menu menu = new Menu("Correlation");
        menu.setMenuFunction("#");
        Menu[] innerSubMenu;


        ArrayList<String> innerMenuLst = new ArrayList<String>();
        ArrayList<String> measIds = new ArrayList<String>();
        for (int i = viewByCount; i < displayLabels.size(); i++) {
            if (!colId.equalsIgnoreCase(rowDataIds.get(i))) {
                innerMenuLst.add(displayLabels.get(i).toString());
                measIds.add(rowDataIds.get(i));
            }
        }

        innerSubMenu = new Menu[innerMenuLst.size()];
        for (int j = 0; j < innerMenuLst.size(); j++) {
            corrDisplayLabel = innerMenuLst.get(j).toString();
            innerSubMenu[j] = new Menu(innerMenuLst.get(j));
            String menuFunction = getMenuFunctionForCorrelation(super.rowDataIds.get(column), measIds.get(j), displayLabel, corrDisplayLabel, reportId, "Correlation");
            innerSubMenu[j].setMenuFunction(menuFunction);

        }

        menu.setSubMenu(innerSubMenu);
        return menu;
    }

    private Menu createStatisticsMenu(int column) {
        Menu menu = new Menu("Statistics");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        if (isCrosstab) {
            subMenu = new Menu[9];
        } else {
            subMenu = new Menu[10];
        }

        String displayLabel;
        if (isCrosstab) {
            displayLabel = "";
        } else {
            displayLabel = (String) this.displayLabels.get(column);
        }


        int menuIndex = 0;
        subMenu[menuIndex] = new Menu("Mean");
        String menuFunction = this.getMenuFunctionForStatistics(super.rowDataIds.get(column), displayLabel, reportId, "Mean", ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);

        if (!isCrosstab) {
            subMenu[menuIndex] = new Menu("Correlation");
            subMenu[menuIndex++] = this.createCorrelationMenu(column);
        }

        subMenu[menuIndex] = new Menu("Median");
        menuFunction = this.getMenuFunctionForStatistics(super.rowDataIds.get(column), displayLabel, reportId, "Median", ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);

        subMenu[menuIndex] = new Menu("Std. Deviation");
        menuFunction = this.getMenuFunctionForStatistics(super.rowDataIds.get(column), displayLabel, reportId, "SD", ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);

        subMenu[menuIndex] = new Menu("Variance");
        menuFunction = this.getMenuFunctionForStatistics(super.rowDataIds.get(column), displayLabel, reportId, "Variance", ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);

        subMenu[menuIndex] = new Menu("Running Total");
        menuFunction = this.getMenuFunctionForRunningTotal(super.rowDataIds.get(column), displayLabel, reportId, ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);

        subMenu[menuIndex] = new Menu("Rank");
        menuFunction = this.getMenuFunctionForRank(super.rowDataIds.get(column), displayLabel, reportId, ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);

        subMenu[menuIndex] = new Menu("Rank(Prior)");
        menuFunction = this.getMenuFunctionForRankPrior(super.rowDataIds.get(column), displayLabel, reportId, ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);

        subMenu[menuIndex] = new Menu("Deviation From Mean");
        menuFunction = this.getMenuFunctionForDeviationFromMean(super.rowDataIds.get(column), displayLabel, reportId, ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);
        // start of code by Nazneen For Rank For ST in May 2014
        subMenu[menuIndex] = new Menu("Rank(ST)");
        menuFunction = this.getMenuFunctionForRankST(super.rowDataIds.get(column), displayLabel, reportId, ctxPath);
        subMenu[menuIndex++].setMenuFunction(menuFunction);
        // end of code by Nazneen For Rank For ST in May 2014
        menu.setSubMenu(subMenu);
        return menu;
    }

    private String getMenuFunctionForDeviationFromMean(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = DEVIATION_FROM_MEAN;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + RTMeasureElement.DEVIATION_FROM_MEAN + "\\\',\\\'" + RTMeasureElement.DEVIATIONFROMMEAN.getColumnType() + "\\\',\\\'" + RTMeasureElement.RUNNINGTOTAL.getColumnDisplay() + "\\\',\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + RTMeasureElement.DEVIATION_FROM_MEAN + "','" + RTMeasureElement.DEVIATIONFROMMEAN.getColumnType() + "','" + RTMeasureElement.RUNNINGTOTAL.getColumnDisplay() + "','" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForRenameMsr(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = RENAMEMSR;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForSubTotalSearch(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = SUBTOTAL_SEARCH;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForCorrelation(String elementId, String corrElementId, String displayLabel1, String displayLabel2, String reportId, String type) {
        String menuFunction = CORRELATION;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + corrElementId + "\\\',\\\'" + displayLabel1 + "\\\',\\\'" + displayLabel2 + "\\\',\\\'" + reportId + "\\\',\\\'" + type + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + corrElementId + "','" + displayLabel1 + "','" + displayLabel2 + "','" + reportId + "','" + type + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForEditTableMsr(String elementId, String displayLabel, String reportId) {
        String menuFunction = EDITFORMULA;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForCustomSeq(String elementId, String reportId, String ctxPath) {
        String menuFunction = CUSTOMSEQ;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForcustomKpi(String elementId, String displayLabel, String reportId) {
        String menuFunction = CUSTOM_KPI;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "')";
        }
        return menuFunction;

    }

    private String getMenuFunctionForGoalSeekBasic(String elementId, String displayLabel, String reportId, String ctxPath, boolean onSubtotal, boolean withAbsolute) {
        String menuFunction = GOAL_SEEK;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\',\\\'" + onSubtotal + "\\\',\\\'" + withAbsolute + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "','" + onSubtotal + "','" + withAbsolute + "')";

        }
        return menuFunction;
    }

    private String getMenuFunctionForGoalSeekAdhoc(String elementId, String displayLabel, String reportId, String ctxPath, boolean onSubtotal, boolean withAbsolute) {
        String menuFunction = GOAL_SEEK_ADHOC;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\',\\\'" + onSubtotal + "\\\',\\\'" + withAbsolute + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "','" + onSubtotal + "','" + withAbsolute + "')";

        }
        return menuFunction;

    }

    private String getMenuFunctionForGoalSeekTimeBase(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = TIME_BASE;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;

    }

    private String getMenuFunctionForGoalSeekNPITimechange(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = NEW_PRODUCT_CHANGE_TIME;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;

    }

    private String getMenuFunctionForGoalSeekNPIPerceBase(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = NEW_PRODUCT_PERCENT_BASE;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;

    }

    private String getMenuFunctionForRowWiseColorGroup(String elementId, String reportId, String path) {
        String menufunction = ROW_WISE_COLORGROUP;
        if (isExcelDisplay) {
            menufunction = menufunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + path + "\\\')";

        } else {
            menufunction = menufunction + "('" + elementId + "','" + reportId + "','" + path + "')";

        }
        return menufunction;
    }

    private String getMenuFunctionForScriptAlign(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menufunction = SCRIPT_ALIGN;
        if (isExcelDisplay) {
            menufunction = menufunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menufunction = menufunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menufunction;
    }

    private String getMenuFunctionForMeasureAlign(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menufunction = MEASURE_ALIGN;
        if (isExcelDisplay) {
            menufunction = menufunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menufunction = menufunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menufunction;
    }

    private String getMenuFunctionForModifyAlign(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menufunction = MODIFY_ALIGN;
        if (isExcelDisplay) {
            menufunction = menufunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menufunction = menufunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }

        return menufunction;
    }

    private String getQuickTimeBasedFormula(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menufunction = QUICK_TIMEBASED_FORMULA;
        if (isExcelDisplay) {
            menufunction = menufunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menufunction = menufunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menufunction;
    }

    private String getMenuFunctionForAdvanceFormula(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = ADVANCE_FORMULA;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menuFunction;
    }

    private String getMenuFunctionForSegmentation(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = SEGMENTATION;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";
        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";
        }
        return menuFunction;
    }

    private Menu createQOPercentWiseMenu(int column) {
        Menu menu = new Menu("% wise");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        if (viewByCount > 1) {
            subMenu = new Menu[4];
        } else {
            subMenu = new Menu[2];
        }

        subMenu[0] = new Menu("% wise");
        menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath, false, false);
        subMenu[0].setMenuFunction(menuFunction);

        subMenu[1] = new Menu("% wise with Absolute");
        menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath, false, true);
        subMenu[1].setMenuFunction(menuFunction);

        if (viewByCount > 1) {
            subMenu[2] = new Menu("% wise(subtotal)");
            menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath, true, false);
            subMenu[2].setMenuFunction(menuFunction);

            subMenu[3] = new Menu("% wise with Absolute(subtotal)");
            menuFunction = this.getMenuFunctionForPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath, true, true);
            subMenu[3].setMenuFunction(menuFunction);
        }

        menu.setSubMenu(subMenu);
        return menu;
    }

    private Menu createQOTopBottomMenu(int column) {
        Menu menu = new Menu("Top/Bottom");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        subMenu = new Menu[8];
        subMenu[0] = new Menu("Top 5");
        menuFunction = this.getMenuFunctionForTopRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5, ctxPath);
        subMenu[0].setMenuFunction(menuFunction);

        subMenu[1] = new Menu("Bottom 5");
        menuFunction = this.getMenuFunctionForBottomRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5, ctxPath);
        subMenu[1].setMenuFunction(menuFunction);

        subMenu[2] = new Menu("Top 10");
        menuFunction = this.getMenuFunctionForTopRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 10, ctxPath);
        subMenu[2].setMenuFunction(menuFunction);

        subMenu[3] = new Menu("Bottom 10");
        menuFunction = this.getMenuFunctionForBottomRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 10, ctxPath);
        subMenu[3].setMenuFunction(menuFunction);

        subMenu[4] = new Menu("Top 25");
        menuFunction = this.getMenuFunctionForTopRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 25, ctxPath);
        subMenu[4].setMenuFunction(menuFunction);

        subMenu[5] = new Menu("Bottom 25");
        menuFunction = this.getMenuFunctionForBottomRows(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 25, ctxPath);
        subMenu[5].setMenuFunction(menuFunction);

        subMenu[6] = new Menu("Top 5 with others");
        menuFunction = this.getMenuFunctionForTopRowswithOthers(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5, ctxPath);
        subMenu[6].setMenuFunction(menuFunction);

        subMenu[7] = new Menu("Top 10 with others");
        menuFunction = this.getMenuFunctionForTopRowswithOthers(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 10, ctxPath);
        subMenu[7].setMenuFunction(menuFunction);

        menu.setSubMenu(subMenu);
        return menu;
    }

    private Menu createQOTopBottomPercentMenu(int column) {
        Menu menu = new Menu("Top/Bottom (Percent)");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        subMenu = new Menu[6];


        subMenu[0] = new Menu("Top 5 %");
        menuFunction = this.getMenuFunctionForTopRowsPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5, ctxPath);
        subMenu[0].setMenuFunction(menuFunction);

        subMenu[1] = new Menu("Bottom 5%");
        menuFunction = this.getMenuFunctionForBottomRowsPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5, ctxPath);
        subMenu[1].setMenuFunction(menuFunction);

        subMenu[2] = new Menu("Top 10%");
        menuFunction = this.getMenuFunctionForTopRowsPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 10, ctxPath);
        subMenu[2].setMenuFunction(menuFunction);

        subMenu[3] = new Menu("Bottom 10%");
        menuFunction = this.getMenuFunctionForBottomRowsPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 10, ctxPath);
        subMenu[3].setMenuFunction(menuFunction);

        subMenu[4] = new Menu("Top 25%");
        menuFunction = this.getMenuFunctionForTopRowsPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 25, ctxPath);
        subMenu[4].setMenuFunction(menuFunction);

        subMenu[5] = new Menu("Bottom 25%");
        menuFunction = this.getMenuFunctionForBottomRowsPercentWise(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 25, ctxPath);
        subMenu[5].setMenuFunction(menuFunction);

        menu.setSubMenu(subMenu);
        return menu;
    }

    private Menu createQORoundMenu(int column) {
        Menu menu = new Menu("Round");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();

        subMenu = new Menu[6];

        subMenu[0] = new Menu("No Decimal");
        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 0);
        subMenu[0].setMenuFunction(menuFunction);

        subMenu[1] = new Menu("One Decimal");
        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 1);
        subMenu[1].setMenuFunction(menuFunction);

        subMenu[2] = new Menu("Two Decimal");
        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 2);
        subMenu[2].setMenuFunction(menuFunction);

        subMenu[3] = new Menu("Three Decimal");
        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 3);
        subMenu[3].setMenuFunction(menuFunction);

        subMenu[4] = new Menu("Four Decimal");
        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 4);
        subMenu[4].setMenuFunction(menuFunction);

        subMenu[5] = new Menu("Five Decimal");
        menuFunction = this.getMenuFunctionForRounding(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, 5);
        subMenu[5].setMenuFunction(menuFunction);

        menu.setSubMenu(subMenu);
        return menu;
    }

    private Menu createQOColorGroupMenu(int column) {
        Menu menu = new Menu("Color Group");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        subMenu = new Menu[2];

        subMenu[0] = new Menu("Create/Edit");
        menuFunction = this.getMenuFunctionForColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId, ctxPath);
        subMenu[0].setMenuFunction(menuFunction);

        subMenu[1] = new Menu("Reset");
        menuFunction = this.getMenuFunctionForResetColorGroup(super.rowDataIds.get(column), this.dataTypes.get(column), reportId);
        subMenu[1].setMenuFunction(menuFunction);

        menu.setSubMenu(subMenu);
        return menu;
    }

    private Menu createComplexKPIMenu(int column) {
        String menuFunction;
        Menu menu = new Menu("Custom KPI");
        menuFunction = this.getMenuFunctionForcustomKpi(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId);
        menu.setMenuFunction(menuFunction);
//                        menuLst.add(menu);
        return menu;
    }

    private Menu createMOScriptAlignMenu(int column) {
        Menu menu = new Menu("Script Align");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForScriptAlign(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createMORenameMenu(int column) {
        Menu menu = new Menu("Parameter Rename");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForRenameMsr(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createMOMeasureAlignMenu(int column) {
        Menu menu = new Menu("Measure Align");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForMeasureAlign(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createMOModifyMeasureMenu(int column) {
        Menu menu = new Menu("Modify Measure");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForModifyAlign(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createMOPropertiesMenu(int column) {
        Menu menu = new Menu("Properties");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForProperties(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createPFParameterFilterMenu(int column) {
        Menu menu = new Menu("Parameter Filter");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForParameterFilter(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createPFConversionFormulaMenu(int column) {
        Menu menu = new Menu("Conversion Formula");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForConversionFormula(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createPFSignConversion(int column) {
        Menu menu = new Menu("Sign Conversion");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForSignConversion(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createPFFactFilterMenu(int column) {
        Menu menu = new Menu("Fact Filter");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        menuFunction = this.getMenuFunctionForFactFilter(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu createPFEditFormula(int column) {
        Menu menu = new Menu("Edit Formula");
        Menu[] subMenu = null;
        String menuFunction;
        menuFunction = this.getMenuFunctionForEditTableMsr(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private Menu cretePFQuickTimeBaseFormula(int column) {
//        Menu menu = new Menu("Quick TimeBased Formula");
        Menu menu = new Menu("AGG Time Formula");
        Menu[] subMenu = null;
        String menuFunction;
        menuFunction = this.getQuickTimeBasedFormula(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }
    //added by Nazneen

    private Menu cretePFDateFormula(int column) {
        Menu menu = new Menu("Single Fact Formula's");
        Menu[] subMenu = null;
        String menuFunction;
        menuFunction = this.getMenuFunctionForAdvanceFormula(super.rowDataIds.get(column), this.displayLabels.get(column).toString(), reportId, ctxPath);
        menu.setMenuFunction(menuFunction);
        return menu;
    }

    private String getMenuFunctionForGrouping(String elementId, String dataType, String reportId, String ctxPath) {
        String menuFunction = Grouping;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + dataType + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + dataType + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
    }
//      start of code by Nazneen on Feb14 for Dimension Segment(Grouping)

    private String getMenuFunctionForDimSegment(String elementId, String reportId, String rowViewById, String rowViewByName, String ctxPath) {
        String menuFunction = DIMENSION_SEGMENT;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + elementId + "\\\',\\\'" + reportId + "\\\',\\\'" + rowViewById + "\\\',\\\'" + rowViewByName + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + elementId + "','" + reportId + "','" + rowViewById + "','" + rowViewByName + "','" + ctxPath + "')";

        }
        return menuFunction;
    }
//   end of code by Nazneen on Feb14 for Dimension Segment(Grouping)
//start of code by Nazneen For Rank For ST in May 2014

    private String getMenuFunctionForRankST(String elementId, String displayLabel, String reportId, String ctxPath) {
        String menuFunction = RANK_ST;
        if (isExcelDisplay) {
            menuFunction = menuFunction + "(\\\'" + RTMeasureElement.RANK_ST + "\\\',\\\'" + RTMeasureElement.RANKST.getColumnType() + "\\\',\\\'" + RTMeasureElement.RANKST.getColumnDisplay() + "\\\',\\\'" + elementId + "\\\',\\\'" + displayLabel + "\\\',\\\'" + reportId + "\\\',\\\'" + ctxPath + "\\\')";

        } else {
            menuFunction = menuFunction + "('" + RTMeasureElement.RANK_ST + "','" + RTMeasureElement.RANKST.getColumnType() + "','" + RTMeasureElement.RANKST.getColumnDisplay() + "','" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxPath + "')";

        }
        return menuFunction;
//end of code by Nazneen For Rank For ST in May 2014
    }
}
