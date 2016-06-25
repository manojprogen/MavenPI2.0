<%--
    Document   : Create Advance Formula
    Created on : March 19, 2013, 5:18:54 PM
    Author     : Nazneen Khan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.connection.ConnectionMetadata,com.progen.connection.ConnectionDAO,java.sql.*,prg.db.PbReturnObject,prg.db.PbDb,prg.db.Container,com.progen.query.RTMeasureElement,utils.db.ProgenConnection,java.util.HashMap,java.util.ArrayList"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">



<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor="blue";
            String timeBasedMeasures = "";
             if (request.getSession().getAttribute("loadDialogs") != null && request.getSession().getAttribute("loadDialogs").equals("true")) {
              if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
              String contextPath=request.getContextPath();
%>
<html>
    <head>
        <title></title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
<!--        <link type="text/css" href="<%=request.getContextPath()%>/javascript/lib/jquery/css/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
 <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />


<!--        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>-->
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>

<!--        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>-->

        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <script type="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />-->

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />




    <%

                String dbType = "";
                if (session.getAttribute("MetadataDbType") != null) {
                    dbType = (String) session.getAttribute("MetadataDbType");
                }
                try {
                    PbDb pbdb = new PbDb();
                    int length = 0;

                    Container container = null;
                    HashMap ParametersHashMap = null;
                    HashMap TableHashMap = null;
                    HashMap GraphHashMap = null;
                    ArrayList REP_Elements = new ArrayList();
                    ArrayList CEP_Elements = new ArrayList();
                    ArrayList REP_Elements_names = new ArrayList();
                    ArrayList CEP_Elements_names = new ArrayList();
                    ArrayList measures = new ArrayList();
                    ArrayList measuresNames = new ArrayList();
                    ArrayList params = new ArrayList();
                    ArrayList params_names = new ArrayList();
                    Object measureIdsFromProperties[] = null;
                    String bussTabId = "";
                    String folderId = "";
                    String grpId = "";
                    String reportId =(String) session.getAttribute("REPORTID");

                        reportId = (String) session.getAttribute("reportId");
                        String columnName = request.getParameter("columnName");
                        String formulaVal = request.getParameter("formulaVal");
                        String basicOp = request.getParameter("basicOp");
                        String elementIdsList = "";
                        String eleId = "";
                        eleId = columnName.replace("A_", "").replace("_B", "").replace("_G", "");

                    String folderIds = (String) request.getSession().getAttribute("folderIds");
                    String from = (String)session.getAttribute("ViewFrom");
                    if ( folderIds == null )
                    {
                        String getFolderIds = "SELECT  FOLDER_ID FROM PRG_AR_REPORT_DETAILS where report_id=" + reportId;
                        PbReturnObject folderIdspbro = pbdb.execSelectSQL(getFolderIds);
                        for (int i = 0; i < folderIdspbro.getRowCount(); i++) {
                            if (i == 0) {
                                folderIds = String.valueOf(folderIdspbro.getFieldValueInt(i, 0));
                            } else {
                                folderIds += "," + String.valueOf(folderIdspbro.getFieldValueInt(i, 0));
                            }
                        }
                     }


                    HashMap map = new HashMap();
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                    container = (prg.db.Container) map.get(reportId);
                    ParametersHashMap = container.getParametersHashMap();
                    TableHashMap = container.getTableHashMap();
                    String fromEdit = request.getParameter("fromEdit");
                    String formula = "";
                    String measureLabel = "";
                    String refferedMeasureIds = "";
                    String refferedMeasureLabels = "";
                    String elementId = "";
                    String tableNameList = "";
                    String connectionId = "";
                    String measID ="";
                    String tablemeasures = "";

                boolean isEdit = false;
                if(("true").equalsIgnoreCase(fromEdit)){
                    isEdit = true;
                    }
                    String elementIdsListwithoutperc = "";
                    PbReturnObject pbro = new PbReturnObject();
                    String isSelected = "NO";


                    if (TableHashMap != null) {
                        if (TableHashMap.get("REP") != null) {
                            REP_Elements = (ArrayList) TableHashMap.get("REP");
                            REP_Elements_names = (ArrayList) TableHashMap.get("REPNames");
                        } /*else {
                        if (params != null && params.size() != 0) {
                        REP_Elements.add(params.get(0));
                        REP_Elements_names.add(params_names.get(0));
                        }
                        }*/
                        if (TableHashMap.get("CEP") != null) {
                            CEP_Elements = (ArrayList) TableHashMap.get("CEP");
                            CEP_Elements_names = (ArrayList) TableHashMap.get("CEPNames");
                        }
                        if (TableHashMap.get("Measures") != null && TableHashMap.get("MeasuresNames") != null) {
                            measures = (ArrayList) TableHashMap.get("Measures");
                            measuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                        }

                        if (TableHashMap.get("TableProperties") != null) {
                            //////.println("");
                            HashMap TableProperties = (HashMap) TableHashMap.get("TableProperties");
                       //    measureIdsFromProperties = ((HashMap) TableProperties.get("ColumnProperties")).keySet().toArray();
                            measureIdsFromProperties = container.getColumnProperties().keySet().toArray();

                            if (measureIdsFromProperties != null) {
                                length = measureIdsFromProperties.length;
                            }

                        }
                        //////.println("length====" + length + "----measures.size()---" + measures.size());
                        //////.println("measureIdsFromProperties.length====" + measureIdsFromProperties.length);//

                        if (measures.size() > 0 && measures.size() >= length) {

                            for (int i = 0; i < measures.size(); i++) {
                                //  selectedElementIds.add(measures.get(i));
                                //   selectedElementIdsNaems.add(measuresNames.get(i));
                                //if (String.valueOf(measures.get(i)).contains("_percentwise")) {
                                if(RTMeasureElement.isRunTimeMeasure(measures.get(i).toString()))
                                    elementIdsListwithoutperc=elementIdsListwithoutperc+"";
                                else
                                if (String.valueOf(measures.get(i)).contains("_percentwise")) {
                                    elementIdsList += "," + String.valueOf(measures.get(i)).replace("A_", "").replace("_B", "").replace("_G", "");
                                } else {
                                    elementIdsList += "," + String.valueOf(measures.get(i)).replace("A_", "").replace("_B", "").replace("_G", "");
                                    elementIdsListwithoutperc += "," + String.valueOf(measures.get(i)).replace("A_", "").replace("_B", "").replace("_G", "");
                                }
                            }
                        } else {
                            if (length > 0) {
                                for (int i = 0; i < measureIdsFromProperties.length; i++) {
                                    //  selectedElementIds.add(measures.get(i));
                                    //   selectedElementIdsNaems.add(measuresNames.get(i));
                                     if(RTMeasureElement.isRunTimeMeasure(measures.get(i).toString()))
                                      elementIdsListwithoutperc=elementIdsListwithoutperc+"";
                                     else
                                    if (String.valueOf(measures.get(i)).contains("_percentwise")) {
                                        elementIdsList += "," + String.valueOf(measures.get(i)).replace("A_", "").replace("_B", "").replace("_G", "");
                                    } else {
                                        elementIdsList += "," + String.valueOf(measureIdsFromProperties[i]).replace("A_", "").replace("_B", "").replace("_G", "");
                                        elementIdsListwithoutperc += "," + String.valueOf(measureIdsFromProperties[i]).replace("A_", "").replace("_B", "").replace("_G", "");
                                    }
                                }
                            } else {
                                for (int i = 0; i < measures.size(); i++) {
                                    //  selectedElementIds.add(measures.get(i));
                                    //   selectedElementIdsNaems.add(measuresNames.get(i));
                                     if(RTMeasureElement.isRunTimeMeasure(measures.get(i).toString()))
                                         elementIdsListwithoutperc=elementIdsListwithoutperc+"";
                                      else
                                    if (String.valueOf(measures.get(i)).contains("_percentwise")) {
                                        elementIdsList += "," + String.valueOf(measures.get(i)).replace("A_", "").replace("_B", "").replace("_G", "");
                                    } else {
                                        elementIdsList += "," + String.valueOf(measures.get(i)).replace("A_", "").replace("_B", "").replace("_G", "");
                                        elementIdsListwithoutperc += "," + String.valueOf(measures.get(i)).replace("A_", "").replace("_B", "").replace("_G", "");
                                    }
                                }

                            }

                        }
                    }

//                    if (!"".equalsIgnoreCase(elementIdsList)) {
//                               PbReturnObject pbro1 = new PbReturnObject();
//                        String getBussTabId = "";
//                        getBussTabId = "select buss_table_id from prg_user_all_info_details where element_id ="+elementIdsList;
//                        pbro1 = pbdb.execSelectSQL(getBussTabId);
//                         bussTabId = pbro1.getFieldValueString(0,0);
//
//                        String selecteditemsQuery = "";
//                        if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                            selecteditemsQuery = "select DISTINCT element_id, isnull(disp_name,sub_folder_type),"
//                                    + "isnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
//                                    + "user_col_type,actual_col_formula,folder_id,isnull(BUSS_COL_NAME,user_col_name),isnull(AGGREGATION_TYPE,'0')"
//                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in (select element_id from PRG_USER_ALL_INFO_DETAILS "
//                                    + " where buss_table_id =" + bussTabId + " and user_col_type in('DATE','date'))";
//                        }
//                        else if(dbType.equalsIgnoreCase(ProgenConnection.MYSQL)){
//                        selecteditemsQuery = "select DISTINCT element_id, ifnull(disp_name,sub_folder_type),"
//                                    + "ifnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
//                                    + "user_col_type,actual_col_formula,folder_id,ifnull(BUSS_COL_NAME,user_col_name),ifnull(AGGREGATION_TYPE,'0')"
//                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in(select element_id from PRG_USER_ALL_INFO_DETAILS "
//                                    + " where buss_table_id =" + bussTabId + " and user_col_type in('DATE','date'))";
//                        }
//
//                                               else {
//
//                            selecteditemsQuery = "select DISTINCT element_id, nvl(disp_name,sub_folder_type),"
//                                    + "nvl(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
//                                    + "user_col_type,actual_col_formula,folder_id,nvl(BUSS_COL_NAME,user_col_name),nvl(AGGREGATION_TYPE,'0')"
//                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in(select element_id from PRG_USER_ALL_INFO_DETAILS "
//                                    + " where buss_table_id =" + bussTabId + " and user_col_type in('DATE','date'))";
//                                                               }
//                         pbro = pbdb.execSelectSQL(selecteditemsQuery);
//                        isSelected = "YES";
//                      }
//
//
//
//                    for (int i = 0; i < pbro.getRowCount(); i++) {
//                    if(!measID.equalsIgnoreCase(pbro.getFieldValueString(i, 0))){
//                        String measureType = pbro.getFieldValueString(i, 4);
//                        if(measureType.equalsIgnoreCase("NUMBER")){
//                       timeBasedMeasures = timeBasedMeasures+"<li style=\'background:white;color:transparent\'><img alt=\'\' src=\'icons pinvoke/report.png\'>";
//                       timeBasedMeasures = timeBasedMeasures+"<span class=\'myDragTabs\' class=\'ui-state-default\' style=\'color:#000\' id=\'"+pbro.getFieldValueString(i,0)+"^"+pbro.getFieldValueString(i,2)+"\'>"+pbro.getFieldValueString(i,2)+"</span>";
//                       timeBasedMeasures = timeBasedMeasures+"<label class=\'label\' style=\'color:green\'><b>{N}</b></label></li>";
//                   }
//                       else if(measureType.equalsIgnoreCase("calculated")){
//                       timeBasedMeasures = timeBasedMeasures+"<li style=\'background:white;color:transparent\'><img alt=\'\' src=\'icons pinvoke/report.png\'>";
//                       timeBasedMeasures = timeBasedMeasures+"<span class=\'myDragTabs\' class=\'ui-state-default\' style=\'color:#000\' id=\'"+pbro.getFieldValueString(i,0)+"^"+pbro.getFieldValueString(i,2)+"\'>"+pbro.getFieldValueString(i,2)+"</span>";
//                       timeBasedMeasures = timeBasedMeasures+"<label class=\'label\' style=\'color:green\'><b>{C}</b></label></li>";
//                       }
//
//                       tablemeasures = tablemeasures+"<li style=\'background:white;color:transparent\'><img alt=\'\' src=\'icons pinvoke/report.png\'>";
//                       tablemeasures = tablemeasures+"<span class=\'myDragTabs\' class=\'ui-state-default\' style=\'color:#000\' id=\'"+pbro.getFieldValueString(i,0)+"^"+pbro.getFieldValueString(i,2)+"\'>"+pbro.getFieldValueString(i,2)+"</span>";
//                       if(measureType.equalsIgnoreCase("NUMBER")){
//                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{N}</b></label></li>";
//                       }
//                       else if(measureType.equalsIgnoreCase("VARCHAR2")){
//                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{T}</b></label></li>";
//                           }
//                       else if(measureType.equalsIgnoreCase("DATE")){
//                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{D}</b></label></li>";
//                           }
//                       else if(measureType.equalsIgnoreCase("calculated")){
//                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{C}</b></label></li>";
//                           }
//                       else if(measureType.equalsIgnoreCase("summarised")||measureType.equalsIgnoreCase("SUMMARIZED")){
//                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{S}</b></label></li>";
//                       }
//                    }
//                }


    %>
     <%
       PreparedStatement pstmtForTransterSource;
       ResultSet resultSet = null;
       PbReturnObject returnObject = null;
       Connection conn = ProgenConnection.getInstance().getConnection();
       String qery1="SELECT CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID ="+eleId;
      pstmtForTransterSource = conn.prepareStatement(qery1);
      resultSet = pstmtForTransterSource.executeQuery();
      returnObject = new PbReturnObject(resultSet);
      connectionId = returnObject.getFieldValueString(0,0);
      String sourceDbType = "";
      ConnectionDAO connectionDAO = new ConnectionDAO();
      ConnectionMetadata conMetadata;
      conMetadata = connectionDAO.getConnectionByConId(connectionId);
      sourceDbType = conMetadata.getDbType();
    if (sourceDbType.equalsIgnoreCase("mysql")) {
        sourceDbType = "mysql";
    } else if (sourceDbType.equalsIgnoreCase("sqlserver")) {
        sourceDbType = "sqlserver";
    } else {
        sourceDbType = "oracle";
    }
       %>
    <script type="text/javascript">
        $(document).ready(function() {
               
             parent.$("#aggVal").val("none");
             parent.$("#fromTimeBased").val("false");
             getDetails();
//                $("#myList3").treeview({
//                    animated:"slow",
//                    persist: "cookie"
//                });
//
//                //addeb by bharathi reddy fro search option
//                $('ul#myList3 li').quicksearch({
//                    position: 'before',
//                    attached: 'ul#myList3',
//                    loaderText: '',
//                    delay: 100
//                });
//                $(".myDragTabs").draggable({
//                    helper:"clone",
//                    effect:["", "fade"]
//                });
//
//                $("#dropTabs").droppable({
//                    activeClass:"blueBorder",
//                    accept:'.myDragTabs',
//                    drop: function(ev, ui) {
//                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortableUL");
//                    }
//                }
//            );



            });
    </script>

       
    </head>

    <body>
        <form action=""  id="f1" name="myForm">

            <table align="center" width="100%" border="0" >
                <tr style="width:100%">
                    <td style="width:100%">
                        <table style="width:100%">
                            <tr>
                                <td>
                                    <label class="label"><b>Name</b>&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    <input type="text" name="columnName" id="columnName" size="30">
                                </td>
                                <td>
                                    <label class="label"><b>Formula Type : </b>&nbsp;&nbsp;<%=formulaVal%></label>
                                </td>
<!--                                 <td>
                                    <label class="label"><b>Formula Type</b>&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    <select id="formulaType" name="formulaType" onchange="getDetails()">
                                            <option value="NONE">--Select--</option>
                                            <option value="DATE">Date</option>
                                            <option value="NUMBER">Number</option>
                                    </select>
                                </td>-->
                            </tr>
                        </table>
                    </td>

                </tr>
            </table>
            <table align="center" width="100%" border="0" >
                <tr style="width:100%">
                    <td style="width:100%" id="myTabBody">
                        <table style="width:100%" border="1px solid">
                            <tr style="width:100%">

                                <td width="50%" valign="top" class="draggedTable1">
                                    <div style="height:15px" class="ui-state-default draggedDivs ui-corner-all"><font size="1" style="font-weight:bold">Drag Columns From here</font></div>
                                    <div id="afterTable" style="height:150px;overflow-y:auto;">
                                        <ul id="myList">

                                        </ul>
                                    </div>
                                </td>
                                <td id="dropTabs" width="50%" valign="top">
                                    <div style="height:15px" class="ui-state-default draggedDivs ui-corner-all"><font size="1" style="font-weight:bold">Drag Columns to here</font></div>
                                    <div style="height:150px;overflow-y:auto" id="draggedcolumns">
                                        <ul id="sortableUL">
                                        </ul>
                                    </div>
                                </td>



                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

                                        <div id="formulatextarea">
            <table align="center" width="100%" border="0"  >
                <tr>
                    <td width="50%" style="height:150px" valign="top">
                        <table>
                            <!--added by Nazneen for defining default summarization in formuals-->
                            <tr>
                                    <td>
                                    <label class="label"><b>Aggregation Type :</b></label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                     <select id="aggrType" name="aggrType">
                                            <option value="default">DEFAULT</option>
                                            <option value="sum">SUM</option>
                                            <option value="avg">AVG</option>
                                            <option value="min">MIN</option>
                                            <option value="max">MAX</option>
                                    </select>
                                </td>
                            </tr>
<!--                            <tr>
                                    <td>
                                        <label class="label"><b>Null Value Handling</b></label>
                                    </td>
                            </tr>

-->                            <tr id="dbType">
<!--                                    <td>
                                        <input type="radio" name="checkVal" value="case" id="checkval1" checked > Case
                                        <input type="radio" name="checkVal" value="none" id="checkval2"> None
                                       <input type="radio" name="checkVal" value="dbspecific" id="checkval3"> DataBase Specific
                                    </td>-->
                            </tr>
<!--                             <tr>
                                <td>
                                    &nbsp;
                                </td>
                            </tr>-->
                            <tr id="lblOp">
                                <td width="50%" valign="top"><label class="label"><b>Operators</b></label></td>
                            </tr>
                            <tr>
                                <td width="50%"  valign="top">
                                    <div id="basicopre">
                                        <select id="basicoperators" name="basicoperators" onchange="buildbasicoperation()">
<!--                                            <option value="">--select--</option>
                                            <option value="+">Sum(+)</option>
                                            <option value="-">Difference(-)</option>
                                            <option value="*">Multiplication(*)</option>
                                            <option value="/">Division(/)</option>
                                            <option value="%">Percent(%)</option>
                                            <option value="ABB">Percent((A-B)/B)</option>
                                            <option value="ABB%">Percent((A-B)/B%)</option>
                                            <option value="ABA">Percent((A-B)/A)</option>
                                            <option value="ABA%">Percent((A-B)/A%)</option>-->
                                        </select>
                                    </div>
<!--                                    <div id="advopre" style="display:none">
                                        <select id="advoperators" name="advoperators" onchange="buildadvoperation()">
                                            <option value="">--select--</option>
                                            <option value="avg">Average</option>
                                            <option value="sd">Std Deviation</option>
                                            <option value="min">Min</option>
                                            <option value="max">Max</option>
                                            <option value="sdper">Deviation(%)</option>
                                            <%-- <option value="totper">% of Total</option> --%>
                                        </select>
                                    </div>-->

                                </td>
                            </tr>
                            <br>
                            <tr id="NumerVal">
                                <td width="50%" valign="top"><label class="label"><b>Number</b></label></td>
                               </tr>
                               <tr id="NumerText">
<!--                                <td>
                                    <input type="hidden" id="Numer" name="Numer" onblur="test()" value="" >
                                </td>-->
                                </tr>
                                <tr id="NumerLbl">
                                    <!--<td width="50%" valign="top"><label class="label">{Press tab after enter number}</label></td>-->
                                </tr>

                        </table>
                    </td>

                    <td width="50%" style="height:150px" valign="top">
                        <center><label class="label"><b>Formula</b>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="editformula()">Edit</a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="clearboxes()">Clear Formula</a></label ><hr></center>
                        <textarea style="width:99%;height:100%" id="txt2" name="txt2" readonly  cols="60" onkeyup="addtoothervals()" style="background-color:white;bbackground:white" rows="1"></textarea>
                    </td>
                </tr>

            </table>
                                        </div>



    <div id="operatorstextarea">
            <table align="center" width="100%" border="1" >
                <tr style="width:100%" align="center">
                    <td width="100%" align="center" valign="top">
                        <table border="0" width="100%" align="center" cellspacing="5">
                            <tr style="width:100%" align="center">
                                <td>
                                    <table> 
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="+"  id="+" onclick="addValue('+','+','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="-"  id="-" onclick="addValue('-','-','Operators')"></td></tr>
                                        <tr><td><input type="button" value="*" class="navtitle-hover" style="width:auto" id="*" onclick="addValue('*','*','Operators')"></td></tr>


                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" value="/" class="navtitle-hover" style="width:auto" id="/" onclick="addValue('/','/','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="("  id="(" onclick="addValue('( ','(','OpenOper')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value=")"   id=")" onclick="addValue(' )',')','CloseOper')"></td></tr>

                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" value="()" class="navtitle-hover" style="width:auto"  id="( )" onclick="addValue('( )','( )','SpecOper')"></td></tr>
                                        <tr><td><input type="button" value="=" class="navtitle-hover" style="width:auto" id="=" onclick="addValue('=','=','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="," onclick="addValue(',',',','Operators')"></td></tr>
                                    </table>
                                </td><td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="Select Column"  onclick="selectColumn()"></td></tr>
                                                <%--  <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="case"  onclick="addCase()"></td></tr>--%>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Undo" value="Undo" class="btn" onclick="undoFun()"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Redo" value="Redo" class="btn" onclick="redoFun()" ></td></tr>

                                    </table>
                                </td>
                                <td>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
                                    </div>





            <br>
            <table align="center" width="100%" border="0" >
                <tr style="width:100%" align="center">
                    <td width="100%" align="center" valign="top">
                        <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCustMember('<%=tableNameList%>','<%=connectionId%>','<%=reportId%>','<%=elementIdsList%>')">
                    </td>
                </tr>
            </table>
            <textarea id="tArea" name="tArea" style="display:none"> </textarea>
            <textarea id="tArea1" name="tArea1" style="display:none"> </textarea>
            <input type="hidden" name="folderIds" id="folderIds" value="<%=folderIds%>">
            <input type="hidden" name="iscalculate" id="iscalculate" >

            <input type="hidden" name="sourcePage" id="sourcePage" value="Viewer">
             <input type="hidden" id="from" value="<%=from%>">
        </form>

<!--        <div id="dialog" title="Cases"><center>
                <form name="caseForm">
                    <table>
                        <tr>
                            <td><label class="label" >When</label> &nbsp; <input type="text" id="when"  readonly name="when"  onfocus="focusedElement('when')">

                                <input type="hidden" id="when1" name="when1" onfocus="focusedElement('when1')">
                            </td>
                        </tr>
                        <tr>
                            <td><label class="label" >Then</label> &nbsp;
                                <input type="text" id="then" name="then" readonly onfocus="focusedElement('then')">
                                <input type="hidden" id="then1" name="then1" onfocus="focusedElement('then1')">
                            </td>
                        </tr>
                        <tr>
                            <td><label class="label" >Else</label> &nbsp;
                                <input type="text" id="else" name="else"  readonly onfocus="focusedElement('else')">
                                <input type="hidden" id="else1" name="else1" onfocus="focusedElement('else1')">
                            </td>
                        </tr>
                    </table>
                    <input type="button" value="Save" onclick="saveCase()">
                    <input type="hidden" id="from" value="<%=from%>">
                    </form></center>

        </div>-->
 <script type="text/javascript">
             var arrIndex=0;
            var arrIndex1=0;
            var prevIndex=0;
            var tableListVal = '<%=tableNameList%>';
            var ConnectionIdval = '<%=connectionId%>';
            var reportidval = '<%=reportId%>';
            var elementIdListString = '<%=elementIdsList%>';
              $(function(){
                  if('<%=measureLabel%>'!='null'){
                          $("#columnName").val('<%=measureLabel%>');
                          $("#txt2").val('<%=formula%>');
                          var measIds = '<%=refferedMeasureIds%>';
                          var measLabel = '<%=refferedMeasureLabels%>'
                          var measureIds = measIds.split(",");
                          var measureLabels = measLabel.split(",");
                          for(var i=0;i<measureIds.length;i++){
                          var elementId = measureIds[i]+"^"+measureLabels[i];
                          createColumn(elementId,measureLabels[i],"sortableUL");
                          }

                    document.getElementById("tArea").value=measIds;
                    document.getElementById('tArea1').value=measIds;
                    prevClass="formula";
                    formArray[arrIndex]='<%=formula%>';
                    formArray1[arrIndex1]=measIds;
                    prevClassType[prevIndex]="formula";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                          }
              });
            var formArray=new Array();// used for external formula
            var formArray1=new Array();// used for internal formula
            var redoArray=new Array();
            var redoArray1=new Array();
            var redoprevClassType=new Array();;//used for storing prevClassType

            var prevClass="";
            var prevClassType=new Array();;//used for storing prevClassType
            var colArray=new Array();
            var formula='';
            var formula1='';
            var redoIndex=0;
            var redoIndex1=0;
            var redoprevIndex=0;
            var Flag=1;

            var arrIndexLength=0;
            var arrIndex1Length=0;
            var prevIndexlength=0;

            var prevStr=null;
            var curStr=null;
            var txtlength=0;
            var caseStr = 'case when {        }'+"\n"+'then {       }'+"\n"+'else  {       }'+"\n"+'end';
            var caseWindowStatus = '0';
            var focussed='';
            var focussed1='';
            var output='';
            var elementForTimeBased='';
           
            $(function() {
                $("#sortableUL").sortable();
                $("#sortableUL").disableSelection();
            });

            function createColumn(elmntId,elementName,tarLoc){
               var i = $('#SelectTimeBased').val();
                var parentUL=document.getElementById(tarLoc);
                var x=colArray.toString();
                var c=elmntId.split("^");
                //alert(x+"==="+c[0])
               // if($("#SelectTimeBased").is(':checked')){
                if(x.match(c[0])==null){

                    if($("#SelectTimeBased").is(':checked')&&parent.document.getElementById("aggVal").value!='none'){
                        alert("Only One Measure is allowed for Time based formula");
                    }else if($("#SelectTimeBased").is(':checked')&&colArray.length==1){
                        alert("Only One Measure is allowed for Time based formula")
                    }else {
                    var s=elmntId.split("^");
                    colArray.push(s[0]);
                    var childLI=document.createElement("li");
                    childLI.id="For-"+elmntId;
                    childLI.style.width='180px';
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var tableEle=document.createElement("table");
                    tableEle.id="ForTab"+elmntId;
                    var row=tableEle.insertRow(0);
                    var cell1=row.insertCell(0);
                    var chk=document.createElement("input");
                    chk.type="checkbox";
                    //chk.checked=true;
                    chk.name="ChkFor";
                    chk.id="Chk-"+elmntId;
                    chk.value=elmntId;
                    cell1.appendChild(chk);
                    chk.setAttribute("checked", "checked");
                    // chk.checked="checked";
                    var cell2=row.insertCell(1);
                    //cell1.style.backgroundColor="#e6e6e6";

                    var a=document.createElement("a");
                    var deleteElement = "For-"+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell2.appendChild(a);
                    var cell3=row.insertCell(2);
                    // cell2.style.backgroundColor="#e6e6e6";
                    var a1=document.createElement("a");
                    var addElement = "Fora-"+elmntId;
                    a1.href="javascript:selectColumn1('"+elmntId+"')";
                    a1.innerHTML=elementName;
                    // a1.className="ui-icon ui-icon-close";
                    cell3.style.color='black';
                    //cell3.innerHTML=elementName;
                    cell3.appendChild(a1);

                    childLI.appendChild(tableEle);
                    parentUL.appendChild(childLI);
                    if($("#SelectTimeBased").is(':checked')){
                        showTimeBased();
                    }
                    }
                }
               // }else {
                     // if(parent.document.getElementById("aggVal").value!='none'){
                         // alert("Uncheck Time Based For creating Formula");
                     // }else{
                         // showTimeBased();
                    //  }
                 }
               //}
            //}
            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById('sortableUL');
                parentUL.removeChild(LiObj);
                //var x=LiObj.id.split("~");
                //var x=LiObj.id.replace("GrpCol","");
                var s=index.split("^");
                var x=s[0].split("-")[1];
                var i=0;
                // alert(colArray+'7--7'+x);
                for(i=0;i<colArray.length;i++){
                    if(colArray[i]==x)
                        colArray.splice(i,1);
                }
                //alert('-'+colArray);
            }
            /*
                 $(".sortable").sortable();
                $(".sortable").disableSelection();
             */



            function addValue(str,str2,classType)
            {
                if(classType=="Operators"){
                    // if(prevClass=="Query" || prevClass=="Numer" ||  prevClass=="CloseOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ str;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ str;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="Operators";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="Operators";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                        alert('Please add formula using default operators or enter number ')
                    }*/
                }else if(classType=="OpenOper"){
                    //  if(prevClass=="Operators" || prevClass==""){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ str;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ str;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="OpenOper";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="OpenOper";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                        alert('Please select operator')
                    }*/

                }else if(classType=="CloseOper"){
                    // if(prevClass=="Query" || prevClass=="Numer" || prevClass=="CloseOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ str;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ str;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="CloseOper";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="CloseOper";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                        alert('Please add formula using default operators or enter number ')
                    }*/

                }else  if(classType=="SpecOper"){
                    // if(prevClass=="Query" || prevClass=="Numer" || prevClass=="CloseOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula="("+actformula+")";
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula="("+acteleformula+")";
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="SpecOper";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="SpecOper";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                          alert('Please add formula  or number properly to enclose in brackets');
                      }*/

                }
            }




            function undoFun(){
                if(arrIndex>0){
                    arrIndex--;
                    arrIndex1--;
                    prevIndex--;
                    // alert('inside arrIndexLength is '+arrIndex)
                    //   alert(formArray[parseInt(arrIndex)]);
                    //  alert(formArray1[parseInt(arrIndex1Length)]);
                    if(arrIndex==0){
                        document.forms.f1.txt2.value ="";
                        document.forms.f1.tArea.value ="";
                        prevClassType="";
                    }else{

                        document.forms.f1.txt2.value =formArray[parseInt(arrIndex)-1];
                        document.forms.f1.tArea.value =formArray1[parseInt(arrIndex1)-1];
                        prevClass=prevClassType[parseInt(prevIndex-1)];
                        redoArray[redoIndex]=formArray[parseInt(arrIndex)];
                        redoArray1[redoIndex1]=formArray1[parseInt(arrIndex1)];
                        redoprevClassType[redoprevIndex]=prevClassType[parseInt(prevIndex)]
                        redoIndex1++;
                        redoIndex++;
                        redoprevIndex++;

                        //alert(document.forms.f1.tArea.value);
                    }

                }
                else{
                    alert('nothing to undo')
                }

                return (Flag);
            }
            function redoFun(){
                // alert(redoIndex);
                if(redoIndex>0){
                    // alert(redoArray[parseInt(redoIndex)-1]+"--"+redoArray1[parseInt(redoIndex1)-1]+"--"+redoprevClassType[parseInt(redoprevIndex)-1])
                    document.forms.f1.txt2.value =redoArray[parseInt(redoIndex)-1];
                    document.forms.f1.tArea.value =redoArray1[parseInt(redoIndex1)-1];
                    prevClass=redoprevClassType[parseInt(redoprevIndex)-1];
                    formArray[parseInt(arrIndex)]=redoArray[parseInt(redoIndex)-1];
                    formArray1[parseInt(arrIndex1)]=redoArray1[parseInt(redoIndex1)-1];
                    prevClassType[parseInt(prevIndex)]=redoprevClassType[parseInt(redoprevIndex)-1];
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    redoIndex--;
                    redoIndex1--;
                    redoprevIndex--;
                }else{

                    alert('Nothing to redo')
                }
            }


            function clearboxes(){
                document.getElementById('txt2').value='';
                document.getElementById('tArea').value='';
                document.getElementById('tArea1').value='';
                document.getElementById('Numer').value='';
                prevClass=null;
                prevClassType="";
            }



            function checkExpectedClassType(prevClassType1,classType1){
                //alert('prevClassType1 is '+prevClassType1+',classType1 is'+classType1)
                if(prevClassType!=null){
                    if(prevClassType1=='Query'){
                        if(classType1=='Operators'|| classType1=='OpenOper'|| classType1=='CloseOper' || classType1=='SpecOper' || classType1=='Operatorsfun')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }

                    }
                    if(prevClassType1=='Operators'){

                        if(classType1=='Query' || classType1=='Numerics' || classType1=='OpenOper' ||  classType1=='Operatorsfun')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }

                    if(prevClassType1=='Operatorsfun'){

                        if(classType1=='Query' || classType1=='Numerics' || classType1=='OpenOper')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }
                    if(prevClassType1=='Numerics'){
                        if(classType1=='Operators'|| classType1=='OpenOper' || classType1=='CloseOper' || classType1=='SpecOper')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                    if(prevClassType1=='OpenOper'){
                        if(classType1=='Query' || classType1=='Numerics'|| classType1=='OpenOper')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }
                    if(prevClassType1=='CloseOper'){
                        if(classType1=='Operators' || classType1=='SpecOper' || classType1=='CloseOper')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                    if(prevClassType1=='SpecOper'){
                        if(classType1=='Operators')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                }
                else{
                    return true;
                }
            }
            function test(){

                var x=document.getElementById("Numer").value;
                if(x.match(/\D/g)!=null){
                    alert("Please Enter Only Digits From 0-9");
                    x=x.replace(/\D/g,"");
                    document.getElementById("Numer").value=x;
                }else{

                    //if(prevClass=="Operators" || prevClass=="" ||prevClass=="OpenOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ document.getElementById("Numer").value;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ document.getElementById("Numer").value;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    document.getElementById("Numer").value="";
                    prevClass="Numer";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="Numer";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;

                }
            }



            function saveCustMember(tableList,ConnectionId,reportId,elementIdsList){
               var functionname = "saveDateFormula";
                if(document.getElementById("columnName").value==""){
                    alert('Please enter Formula Name');
                }
                else{
                    var x= false;//confirm('Do you wish  to apply Prior,Change and Change%/n for calculated measure');
                    if(x==true){
                        document.getElementById("iscalculate").value="Y";
                    }
                    else{
                        document.getElementById("iscalculate").value="N";
                    }
                    var query = document.getElementById('txt2').value;

                    var name=document.getElementById("columnName").value;
                    var cols=elementIdsList;
                    var txt2=document.getElementById('txt2').value;
                    var folderIds=document.getElementById('folderIds').value;
                    var columnName=document.getElementById('columnName').value;
                    var iscalculate="Y";
                    var tArea=document.getElementById('tArea').value;
                    var tArea1=document.getElementById('tArea1').value;
                    var aggrType=document.getElementById('aggrType').value;
//                    var aggrType="aggrType";
                    var formulaType="DATEFORMULA";
                    var basicOp = "<%=basicOp%>";


                     txt2=txt2.replace("+","@","gi");
                     txt2=txt2.replace("%","|_|","gi");
                    txt2=txt2.replace("&","||chr(38)||","gi");


                           if ( document.getElementById("from").value=="Viewer" )
                          parent.document.getElementById('loading').style.display='';

                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy='+functionname+'&txt2='+txt2+'&folderIds='+folderIds+'&columnName='+encodeURIComponent(columnName)+'&iscalculate='+iscalculate+'&tArea='+tArea+'&tArea1='+tArea1+'&fromEdit='+'<%=isEdit%>'+'&elementId='+'<%=elementId%>'+'&aggVal='+encodeURIComponent(parent.document.getElementById("aggVal").value)+"&fromTimeBased="+parent.document.getElementById("fromTimeBased").value+"&aggrType="+aggrType+"&formulaType="+formulaType+"&basicOp="+basicOp,
                        success: function(data){
                             if(data!=""){
                                if ( document.getElementById("from").value == "Viewer" )
                                {
                                   if(cols.indexOf(data)==-1){
                                      cols+=","+data;
                                    }
                                   $.ajax({
                                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+cols+'&REPORTID='+reportId,
                                        success: function(data){
                                           parent.submitFormMeasChange();
                                        }
                                    });
                                }
                                else
                                {
                                   var measList=parent.document.getElementById("Measures").value;
                                    measList = measList +","+data;
                                    parent.document.getElementById("MsrIds").value=measList;
                                    parent.document.getElementById("Measures").value=measList;


                                    $.ajax({
                                        url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&Msrs='+measList+'&MsrsNames='+measList+'&REPORTID='+reportId,
                                        success: function(data) {
                                            parent.PreviewTable();
                                            if(data!=""){
                                            }
                                        }
                                    });

                                }
                            }
                        }
                    });


                    parent.cancelCustMembersave(name);
                }

            }

            function cancelCustMember()
            {

                parent.cancelCustMember();
            }

            function addCase()
            {

                $('#dialog').dialog('open');
                caseWindowStatus = 1;
            }



            $.ui.dialog.defaults.bgiframe = true;
            $(function() {
                $("#dialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 200,
                    position: 'top'

                });
            });


            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }

            function stateChanged()
            {
                if (xmlHttp.readyState==4)
                {

                    var output1=xmlHttp.responseText;
                    // alert("next of output "+output1+"continue");
                    // if(output1=="Correct")
                    // {
                    if(document.getElementById("columnName").value==""){
                        alert('Please enter Formula Name');
                    }
                    else{
                        var x=confirm('Are you want to calculate Prior,Change and Change%');
                        if(x==true){
                            document.getElementById("iscalculate").value="Y";
                        }
                        else{
                            document.getElementById("iscalculate").value="N";
                        }
                        var query = document.getElementById('txt2').value;

                       document.myForm.action = "saveCustMemberRD.do";
                        document.myForm.submit();
                        var name=document.getElementById("columnName").value;
                        parent.cancelCustMembersave(name);
                    }
                    }
            }

            function saveCase()
            {

                var when = ' case when '+document.getElementById('when').value+' '+"\n";
                var then = 'then '+document.getElementById('then').value+' '+"\n";
                var elsee = 'else '+document.getElementById('else').value+' '+"\n"+'end'+"\n";

                var when1 = ' case when '+document.getElementById('when1').value+' '+"\n";
                var then1 = 'then '+document.getElementById('then1').value+' '+"\n";
                var elsee1 = 'else '+document.getElementById('else1').value+' '+"\n"+'end'+"\n";
                //alert(when+then+elsee);

                document.getElementById('txt2').value = document.getElementById('txt2').value+when+then+elsee;
                document.getElementById('when').value="";
                document.getElementById('then').value="";
                document.getElementById('else').value="";
                document.getElementById('tArea').value = document.getElementById('tArea').value+when1+then1+elsee1;
                document.getElementById('when1').value="";
                document.getElementById('then1').value="";
                document.getElementById('else1').value="";
                //alert(document.getElementById('tArea').value);
                caseWindowStatus = '0';
                $('#dialog').dialog('close');
            }
            function clearCase(){
                document.getElementById('when').value="";
                document.getElementById('then').value="";
                document.getElementById('else').value="";
                document.getElementById('when1').value="";
                document.getElementById('then1').value="";
                document.getElementById('else1').value="";


            }
            function addtoothervals(){
                if(document.getElementById("txt2").readOnly){

                }else{
                    var val=document.getElementById("txt2").value;
                    var val2=val.substring(val.length-1);
                    var val3=document.getElementById("tArea").value;
                    val3+=val2;
                    document.getElementById("tArea").value=val3;

                }
            }

            function changeOperators(){
               var opeType=document.getElementById("opeType").value;
                if(opeType=="basic"){
                    document.getElementById("basicopre").style.display='';
                    document.getElementById("advopre").style.display='none';
                }else{
                    document.getElementById("basicopre").style.display='none';
                    document.getElementById("advopre").style.display='';
                }
            }
            function editformula(){
                document.getElementById("txt2").readOnly=false;
            }
            function buildbasicoperation(){

      
                
                var checkflag="NO";
                var checkValue = "";
                var seloperator=document.getElementById("basicoperators").value;
//                if(seloperator=='INTER'){
//                     $('#operatorstextarea').show();
//                }
//                var number=document.getElementById("Numer").value;
                var number="";
                var obj= document.getElementById('draggedcolumns').getElementsByTagName('input');
//                var formulaType = document.forms.myForm.formulaType.value;
                var connType ="<%=dbType%>";
                var sourceDbType ="<%=sourceDbType%>";
                var formulaVal = "<%=formulaVal%>";
                var basicOp = "<%=basicOp%>";
                
                if(formulaVal=='DATE' || basicOp=="INTER"){                    
                        checkValue="";
                }
                else {
                    if(document.getElementById("checkval1").checked)
                        checkValue="case";
                    else if(document.getElementById("checkval2").checked)
                        checkValue="none";
                    else
                        checkValue="dbspecific";
                }
                if(obj==undefined || obj=="undefined"){
                    alert('Please Drag atleast one columns');
                    document.getElementById("basicoperators").value="";
                }
                else if(seloperator==""){
                    alert('Please select operator');

                }else{
                    var i=0;
                    var eleIdsList="";
                    var eleNamesList="";
                    if(obj.length!=undefined){
                        for(var j=0;j<obj.length;j++)
                        {
                            if(document.myForm.ChkFor[j].checked==true)
                            {   var eleidname=document.myForm.ChkFor[j].value.split("^");
                                eleIdsList+=","+eleidname[0];
                                eleNamesList+="^"+eleidname[1];
                                i++;
                            }
                        }
                        if(i==0){
                            alert('Please select columns');
                            document.getElementById("basicoperators").value="";

                        }
                    }else{
                        if(document.myForm.ChkFor.checked==true){
                            var eleidname=document.myForm.ChkFor.value.split("^");
                            eleIdsList+=","+eleidname[0];
                            eleNamesList+="^"+eleidname[1];
                            i++;
                        }else{
                            alert('Please select columns');
                            document.getElementById("basicoperators").value="";

                        }

                    }
                    if(eleNamesList!=""){
                        eleIdsList=eleIdsList.substring(1);
                        eleNamesList=eleNamesList.substring(1);
                    }
                    if(eleIdsList!=""){
                        if(i<1)
                        {
                            alert("Please select at least one column ");
                            document.getElementById("basicoperators").value="";
                        }else {
                            var eleIdsListarr=eleIdsList.split(",");
                            var eleNamesListarr=eleNamesList.split("^");
                            var formulaNames="";
                            var formulaIds="";
                             if(basicOp=="CONCAT"){
                                    for(var i=0;i<eleIdsListarr.length;i++){                                        
                                         if(checkValue=="dbspecific"){
                                            if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
                                                formulaNames+=", ' ' , IFNULL("+eleNamesListarr[i]+",'')";
                                                formulaIds+=", ' ' , IFNULL("+eleIdsListarr[i]+",'')";  
                                            }
                                            else if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
                                                formulaNames+=" ||' '|| NVL("+eleNamesListarr[i]+",'')";
                                                formulaIds+=" ||' '|| NVL("+eleIdsListarr[i]+",'')";  
                                            }
                                            else{
                                                formulaNames+="+ ' ' + ISNULL("+eleNamesListarr[i]+",'')";
                                                formulaIds+="+ ' ' + ISNULL("+eleIdsListarr[i]+",'')";  
                                            }     
                                        }
                                        else if(checkValue=="none"){
                                            if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
                                                formulaNames+=", ' ' ,"+eleNamesListarr[i];
                                                formulaIds+=", ' ' ,"+eleIdsListarr[i];  
                                            }
                                            else if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
                                                formulaNames+=" ||' '|| "+eleNamesListarr[i];
                                                formulaIds+=" ||' '|| "+eleIdsListarr[i];  
                                            }
                                            else{
                                                formulaNames+="+ ' ' +"+eleNamesListarr[i];
                                                formulaIds+="+ ' ' +"+eleIdsListarr[i];  
                                            }   
                                        }
                                        else {
                                            if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
                                                formulaNames+=", ' ' ,case when "+ eleNamesListarr[i] +" is null then '' else " +eleNamesListarr[i] + " end ";
                                                formulaIds+=", ' ' ,case when "+ eleIdsListarr[i] +" is null then '' else " +eleIdsListarr[i] + " end ";  
                                            } 
                                            else if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
                                                formulaNames+=" ||' '|| case when "+ eleNamesListarr[i] +" is null then '' else " +eleNamesListarr[i] + " end ";
                                                formulaIds+=" ||' '|| case when "+ eleIdsListarr[i] +" is null then '' else " +eleIdsListarr[i] + " end ";  
                                            }
                                            else {
                                                formulaNames+="+ ' ' +case when "+ eleNamesListarr[i] +" is null then '' else " +eleNamesListarr[i] + " end ";
                                                formulaIds+="+ ' ' +case when "+ eleIdsListarr[i] +" is null then '' else " +eleIdsListarr[i] + " end ";  
                                            }
                                        }
                                    }                                    
                                    if(formulaNames!=""){
                                        if(checkValue=="dbspecific"){
                                            if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
                                                formulaNames=formulaNames.substring(8);
                                                formulaIds=formulaIds.substring(8); 
                                                formulaNames="CONCAT("+formulaNames+")";
                                                formulaIds="CONCAT("+formulaIds+")";
                                            }  
                                            else if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
                                                formulaNames=formulaNames.substring(9);
                                                formulaIds=formulaIds.substring(9); 
                                            }
                                            else {
                                                 formulaNames=formulaNames.substring(8);
                                                formulaIds=formulaIds.substring(8); 
                                            }                                           
                                        }
                                        else if(checkValue=="none"){
                                            if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
                                                formulaNames=formulaNames.substring(7);
                                                formulaIds=formulaIds.substring(7); 
                                                formulaNames="CONCAT("+formulaNames+")";
                                                formulaIds="CONCAT("+formulaIds+")";
                                            }  
                                            else if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
                                                formulaNames=formulaNames.substring(8);
                                                formulaIds=formulaIds.substring(8); 
                                            }
                                            else {
                                                 formulaNames=formulaNames.substring(7);
                                                formulaIds=formulaIds.substring(7); 
                                            }                                           
                                        }
                                        else {
                                            if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
                                                formulaNames=formulaNames.substring(7);
                                                formulaIds=formulaIds.substring(7); 
                                                formulaNames="CONCAT("+formulaNames+")";
                                                formulaIds="CONCAT("+formulaIds+")";
                                            }  
                                            else if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
                                                formulaNames=formulaNames.substring(8);
                                                formulaIds=formulaIds.substring(8); 
                                                formulaNames="("+formulaNames+")";
                                                formulaIds="("+formulaIds+")";
                                            }
                                            else {
                                                 formulaNames=formulaNames.substring(7);
                                                formulaIds=formulaIds.substring(7); 
                                                formulaNames="("+formulaNames+")";
                                                formulaIds="("+formulaIds+")";
                                            }                                        
                                                                                      
                                        }
                                        
//                                        if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
//                                            formulaNames=formulaNames.substring(9);
//                                            formulaIds=formulaIds.substring(9);   
//                                        }
//                                        else if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
//                                            formulaNames=formulaNames.substring(14);
//                                            formulaIds=formulaIds.substring(14); 
//                                            formulaNames="CONCAT("+formulaNames+")";
//                                            formulaIds="CONCAT("+formulaIds+")";
//                                        }
//                                        else {
//                                            formulaNames=formulaNames.substring(14);
//                                            formulaIds=formulaIds.substring(14);
//                                            
//                                        }
                                    }
                                    
                                    checkflag="YES";                                
                            }
                            else if(seloperator=="+"){
                                if(eleIdsListarr.length==1 && number==""){
                                    formulaNames+="(SUM("+eleNamesListarr[0]+"))";
                                    formulaIds+="(SUM("+eleIdsListarr[0]+"))";
                                    checkflag="YES";
                                }else{

                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="+(NVL("+eleNamesListarr[i]+",0))";
                                                formulaIds+="+NVL("+eleIdsListarr[i]+",0))";
                                            }
                                            else {
                                                formulaNames+="+(COALESCE("+eleNamesListarr[i]+",0))";
                                                formulaIds+="+(COALESCE("+eleIdsListarr[i]+",0))";
                                            }
                                        }
                                        else if(checkValue=="none"){
                                            formulaNames+="+"+eleNamesListarr[i];
                                            formulaIds+="+"+eleIdsListarr[i];
                                        }
                                        else {
                                        formulaNames+="+(case when "+eleNamesListarr[i]+" is null then 0 else "+eleNamesListarr[i]+" end )";
                                        formulaIds+="+( case when "+eleIdsListarr[i]+" = is null then 0 else "+eleIdsListarr[i]+"  end  )";
                                    }

                                    }
                                    if(number!=""){
                                        formulaNames+="+"+number;
                                        formulaIds+="+"+number;

                                    }
                                    if(formulaNames!=""){
                                        formulaNames=formulaNames.substring(1);
                                        formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";
                                    }
                                    checkflag="YES";
                                }
                            }
                            else if(seloperator=="-"){
                                if(eleIdsListarr.length<2 || eleIdsListarr.length>2){
                                    alert('Please select atleast two colums')
                                }else{
                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        
                                        //added by nazneen
                                        if(formulaVal!='DATE'){
                                      if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="-(NVL("+eleNamesListarr[i]+",0))";
                                                formulaIds+="-NVL("+eleIdsListarr[i]+",0))";
                                            }
                                            else {
                                                formulaNames+="-(COALESCE("+eleNamesListarr[i]+",0))";
                                                formulaIds+="-(COALESCE("+eleIdsListarr[i]+",0))";
                                            }
                                            }
                                            else if(checkValue=="none"){
                                                formulaNames+="-"+eleNamesListarr[i];
                                                formulaIds+="-"+eleIdsListarr[i];
                                            }
                                            else {
                                            formulaNames+="-(case when "+eleNamesListarr[i]+" is null then 0 else "+eleNamesListarr[i]+" end )";
                                            formulaIds+="-( case when "+eleIdsListarr[i]+" = is null then 0 else "+eleIdsListarr[i]+"  end  )";
                                            }
                                    }
                                    else {
                                        if (sourceDbType=='mysql' || sourceDbType=='MYSQL' || sourceDbType=='sqlserver' || sourceDbType=='SQLSERVER' ) {
                                            formulaNames+=","+eleNamesListarr[i];
                                            formulaIds+=","+eleIdsListarr[i];
                                        }
                                        else if(sourceDbType=='oracle' || sourceDbType=='ORACLE') {
//                                            if(eleNamesListarr[i]=='PROGEN_ST_DATE' || eleNamesListarr[i]=='PROGENT_END_DATE'){
                                                formulaNames+="- "+eleNamesListarr[i]+ " ";
                                                formulaIds+="- "+eleIdsListarr[i]+ " ";
//                                            }
//                                            else {
//                                                formulaNames+="- to_date("+eleNamesListarr[i]+",'YYYY-MM-DD') ";
//                                                formulaIds+="- to_date("+eleIdsListarr[i]+",'YYYY-MM-DD') ";
//                                            }
                                        }
                                    }
                                    }
                                    if(number!=""){
                                        formulaNames+="-"+number;
                                        formulaIds+="-"+number;
                                    }
                                    if(formulaNames!=""){
                                        formulaNames=formulaNames.substring(1);
                                        formulaIds=formulaIds.substring(1);
//                                        formulaNames="("+formulaNames+")";
//                                        formulaIds="("+formulaIds+")";

                                    }
                                    if (sourceDbType=='mysql' || sourceDbType=='MYSQL') {
//                                        formulaNames =  "case when DATEDIFF(" + formulaNames +") is null then null else DATEDIFF(" + formulaNames + ") end ";
//                                        formulaIds =  "case when DATEDIFF(" + formulaIds +") is null then null else DATEDIFF(" + formulaIds + ") end ";
                                        formulaNames =  "DATEDIFF(" + formulaNames +")";
                                        formulaIds =  "DATEDIFF(" + formulaIds +")";
                                    }
                                    else if (sourceDbType=='sqlserver' || sourceDbType=='SQLSERVER') {
//                                        formulaNames =  "case when DATEDIFF(day," + formulaNames +") is null then null else DATEDIFF(day," + formulaNames +") end ";
//                                        formulaIds =  "case when DATEDIFF(day," + formulaIds +") is null then null else DATEDIFF(day," + formulaIds +") end ";
                                        formulaNames =  "DATEDIFF(day," + formulaNames +")";
                                        formulaIds =  "DATEDIFF(day," + formulaIds +")";
                                    }
                                     else if (sourceDbType=='oracle' || sourceDbType=='ORACLE'){
//                                        formulaNames =  "case when "+ formulaNames +" is null then null else " + formulaNames + " end ";
//                                        formulaIds =  "case when " + formulaIds +" is null then null else " + formulaIds + " end ";
                                        formulaNames =  formulaNames;
                                        formulaIds =  formulaIds;
                                    }
                                    formulaNames="("+formulaNames+")";
                                    formulaIds="("+formulaIds+")";

                                    checkflag="YES";
                                }
                            }
                            else  if(seloperator=="*"){
                                if(eleIdsListarr.length<2){
                                    alert('Please select atleast two colums')
                                }else{
                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        //added by nazneen

                                        if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="*(NVL("+eleNamesListarr[i]+",0))";
                                                formulaIds+="*(NVL("+eleIdsListarr[i]+",0))";
                                            }
                                            else {
                                                formulaNames+="*(COALESCE("+eleNamesListarr[i]+",0))";
                                                formulaIds+="*(COALESCE("+eleIdsListarr[i]+",0))";
                                            }
                                        }
                                        else if(checkValue=="none"){
                                            formulaNames+="*"+eleNamesListarr[i];
                                            formulaIds+="*"+eleIdsListarr[i];
                                        }
                                        else {
                                        formulaNames+="*(case when "+eleNamesListarr[i]+" is null then 0 else "+eleNamesListarr[i]+" end )";
                                        formulaIds+="*( case when "+eleIdsListarr[i]+" = is null then 0 else "+eleIdsListarr[i]+"  end  )";
                                    }
                                    }
                                    if(formulaNames!=""){
                                        formulaNames=formulaNames.substring(1);
                                        formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";

                                    }
                                    checkflag="YES";
                                }
                            }
                            else if(seloperator=="/"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select only two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please Select two Columns')
                                }else{

                                    // for(var i=0;i<eleIdsListarr.length;i++){
            <%--formulaNames+=eleNamesListarr[0]+"/NVL("+eleNamesListarr[1]+",0)";--%>
                                        //  formulaIds+="/nvl("+eleIdsListarr[i]+",0)";
            <%--formulaIds+=eleIdsListarr[0]+"/NVL("+eleIdsListarr[1]+",0)";--%>
                                        // }
                                       formulaNames+=eleNamesListarr[0]+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+" end )";
                                        formulaIds+=eleIdsListarr[0]+"/( case when "+eleIdsListarr[1]+" = 0 then null else "+eleIdsListarr[1]+"  end  )";


                                        if(formulaNames!=""){
                                            // formulaNames=formulaNames.substring(1);
                                            //  formulaIds=formulaIds.substring(1);
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                        checkflag="YES";
                                    }
                                }

                                //added by Nazneen
                                else if(seloperator=="%"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select only two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please Select two Columns')
                                }else{
//                                        formulaNames+="((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
//                                        formulaIds+="((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";

                                       if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="(NVL("+eleNamesListarr[0]+"*1.0,0)"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
                                                formulaIds+="(NVL("+eleIdsListarr[0]+"*1.0,0)"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";
                                            }
                                            else {
                                                formulaNames+="(COALESCE("+eleNamesListarr[0]+"*1.0,0)"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
                                                formulaIds+="(COALESCE("+eleIdsListarr[0]+"*1.0,0)"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";
                                           }
                                        }
                                        else if(checkValue=="none"){
                                            formulaNames+="("+eleNamesListarr[0]+"/"+eleNamesListarr[1]+")*100";
                                            formulaIds+="("+eleIdsListarr[0]+"/"+eleIdsListarr[1]+")*100";
                                        }
                                        else {
                                        formulaNames+="((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
                                        formulaIds+="((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";
                                        }


                                       if(formulaNames!=""){buildbasicoperation
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                        checkflag="YES";
                                    }
                                }
                              else if(seloperator=="ABB"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select only two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please Select two Columns')
                                }else{
//                                        formulaNames+="((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end )";
//                                            formulaIds+="((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end )";

                                       if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="(NVL("+eleNamesListarr[0]+"*1.0,0)"+"-NVL("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end )";
                                                formulaIds+="(NVL("+eleIdsListarr[0]+"*1.0,0)"+"-NVL("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end )";
                                            }
                                            else {
                                                formulaNames+="(COALESCE("+eleNamesListarr[0]+"*1.0,0)"+"-COALESCE("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end )";
                                                formulaIds+="(COALESCE("+eleIdsListarr[0]+"*1.0,0)"+"-COALESCE("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end )";
                                            }
                                        }
                                        else if(checkValue=="none"){
                                            formulaNames+="(("+eleNamesListarr[0]+"*1.0-"+eleNamesListarr[1]+"*1.0)/"+eleNamesListarr[1]+"*1.0)";
                                            formulaIds+="(("+eleIdsListarr[0]+"*1.0-"+eleIdsListarr[1]+"*1.0)/"+eleIdsListarr[1]+"*1.0)";
                                        }
                                        else {
                                        formulaNames+="((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end )";
                                        formulaIds+="((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end )";
                                       }

                                       if(formulaNames!=""){
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                        checkflag="YES";
                                    }
                                }
                                else if(seloperator=="ABB%"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select only two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please Select two Columns')
                                }else{
//                                        formulaNames+="(((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
//                                        formulaIds+="(((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";
//
                                        if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="((NVL("+eleNamesListarr[0]+"*1.0,0)"+"-NVL("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
                                                formulaIds+="((NVL("+eleIdsListarr[0]+"*1.0,0)"+"-NVL("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";
                                            }
                                            else {
                                                formulaNames+="((COALESCE("+eleNamesListarr[0]+"*1.0,0)"+"-COALESCE("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
                                                formulaIds+="((COALESCE("+eleIdsListarr[0]+"*1.0,0)"+"-COALESCE("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";
                                            }
                                        }
                                        else if(checkValue=="none"){
                                            formulaNames+="((("+eleNamesListarr[0]+"*1.0-"+eleNamesListarr[1]+"*1.0)/"+eleNamesListarr[1]+"*1.0))*100";
                                            formulaIds+="((("+eleIdsListarr[0]+"*1.0-"+eleIdsListarr[1]+"*1.0)/"+eleIdsListarr[1]+"*1.0))*100";
                                        }
                                        else {
                                        formulaNames+="(((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[1]+"*1.0 end ))*100";
                                        formulaIds+="(((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[1]+"*1.0 end ))*100";
                                       }


                                       if(formulaNames!=""){
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                        checkflag="YES";
                                    }
                                }
                                else if(seloperator=="ABA"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select only two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please Select two Columns')
                                }else{
//                                        formulaNames+="((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[0]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end )";
//                                        formulaIds+="((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[0]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end )";

                                       if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="(NVL("+eleNamesListarr[0]+"*1.0,0)"+"-NVL("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[0]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end )";
                                                formulaIds+="(NVL("+eleIdsListarr[0]+"*1.0,0)"+"-NVL("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[0]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end )";
                                            }
                                            else {
                                                formulaNames+="(COALESCE("+eleNamesListarr[0]+"*1.0,0)"+"-COALESCE("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end )";
                                                formulaIds+="(COALESCE("+eleIdsListarr[0]+"*1.0,0)"+"-COALESCE("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end )";
                                            }
                                        }
                                        else if(checkValue=="none"){
                                            formulaNames+="(("+eleNamesListarr[0]+"*1.0-"+eleNamesListarr[1]+"*1.0)/"+eleNamesListarr[1]+"*1.0)";
                                            formulaIds+="(("+eleIdsListarr[0]+"*1.0-"+eleIdsListarr[1]+"*1.0)/"+eleIdsListarr[1]+"*1.0)";
                                        }
                                        else {
                                        formulaNames+="((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[0]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end )";
                                        formulaIds+="((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[0]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end )";
                                       }

                                       if(formulaNames!=""){
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                        checkflag="YES";
                                    }
                                }
                                else if(seloperator=="ABA%"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select only two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please Select two Columns')
                                }else{
//                                        formulaNames+="(((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[0]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end ))*100";
//                                        formulaIds+="(((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[0]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end ))*100";

                                      if(checkValue=="dbspecific"){
                                            if (connType=='oracle' || connType=='ORACLE') {
                                                formulaNames+="((NVL("+eleNamesListarr[0]+"*1.0,0)"+"-NVL("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end ))*100";
                                                formulaIds+="((NVL("+eleIdsListarr[0]+"*1.0,0)"+"-NVL("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end ))*100";
                                            }
                                            else {
                                                formulaNames+="((COALESCE("+eleNamesListarr[0]+"*1.0,0)"+"-COALESCE("+eleNamesListarr[1]+"*1.0,0))"+"/(case when "+eleNamesListarr[1]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end ))*100";
                                                formulaIds+="((COALESCE("+eleIdsListarr[0]+"*1.0,0)"+"-COALESCE("+eleIdsListarr[1]+"*1.0,0))"+"/(case when "+eleIdsListarr[1]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end ))*100";
                                            }
                                        }
                                        else if(checkValue=="none"){
                                            formulaNames+="((("+eleNamesListarr[0]+"*1.0-"+eleNamesListarr[1]+"*1.0)/"+eleNamesListarr[1]+"*1.0))*100";
                                            formulaIds+="((("+eleIdsListarr[0]+"*1.0-"+eleIdsListarr[1]+"*1.0)/"+eleIdsListarr[1]+"*1.0))*100";
                                        }
                                        else {
                                        formulaNames+="(((case when "+eleNamesListarr[0]+" is null then 0 else "+eleNamesListarr[0]+"*1.0 end )"+"-(case when "+eleNamesListarr[1]+" is null then 0 else "+eleNamesListarr[1]+"*1.0 end ))"+"/(case when "+eleNamesListarr[0]+" = 0  then null else "+eleNamesListarr[0]+"*1.0 end ))*100";
                                        formulaIds+="(((case when "+eleIdsListarr[0]+" is null then 0 else "+eleIdsListarr[0]+"*1.0 end )"+"-(case when "+eleIdsListarr[1]+" is null then 0 else "+eleIdsListarr[1]+"*1.0 end ))"+"/(case when "+eleIdsListarr[0]+" = 0  then null else "+eleIdsListarr[0]+"*1.0 end ))*100";
                                       }

                                       if(formulaNames!=""){
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                        checkflag="YES";
                                    }
                                }


                                //ended by Nazneen

                                document.getElementById("basicoperators").value="";
                                if( checkflag=="YES"){
                                    var actformula=document.getElementById("txt2").value;
                                    actformula=actformula+formulaNames;
                                    var acteleformula=document.getElementById("tArea").value;
                                    acteleformula=acteleformula+formulaIds;
                                    var tArea1val= document.getElementById('tArea1').value;
                                    // alert(eleIdsList)
                                    tArea1val=tArea1val+","+eleIdsList;
                                    //alert(actformula+"===="+acteleformula);
                                    document.getElementById("txt2").value=actformula;
                                    document.getElementById("tArea").value=acteleformula;
                                    document.getElementById('tArea1').value=tArea1val;
                                    prevClass="Query";
                                    formArray[arrIndex]=actformula;
                                    formArray1[arrIndex1]=acteleformula;
                                    prevClassType[prevIndex]="Query";
                                    arrIndex++;
                                    arrIndex1++;
                                    prevIndex++;
                                }
                            }
                        }

                    }
                    /* }else{
                    alert('Please select operator from buttons pad');
                    document.getElementById("basicoperators").value="";

                }*/

                }

                function buildadvoperation(){
                    // alert('hi')
                    // if(prevClass=="" || prevClass=="Operators" ||prevClass=="OpenOper"){
                    var checkflag="NO";
                    var seloperator=document.getElementById("advoperators").value;
                    // var obj = document.myForm.ChkFor;
                    var obj= document.getElementById('draggedcolumns').getElementsByTagName('input');
                    if(obj==undefined){
                        alert('Please Drag atleast one columns');
                        document.getElementById("advoperators").value="";

                    }else if(seloperator==""){
                        alert('Please select operator');

                    }else{
                        var i=0;
                        var eleIdsList="";
                        var eleNamesList="";
                        if(obj.length!=undefined){
                            for(var j=0;j<obj.length;j++)
                            {     //alert(j)
                                if(document.myForm.ChkFor[j].checked==true)
                                {    //alert(document.myForm.ChkFor[j].value)
                                    var eleidname=document.myForm.ChkFor[j].value.split("^");
                                    //alert(eleidname[0])
                                    eleIdsList+=","+eleidname[0];
                                    eleNamesList+="^"+eleidname[1];
                                    i++;
                                }
                            }
                            if(i==0){
                                alert('Please select columns');
                                document.getElementById("advoperators").value="";

                            }
                        }else{
                            if(document.myForm.ChkFor.checked==true){
                                var eleidname=document.myForm.ChkFor.value.split("^");
                                eleIdsList+=","+eleidname[0];
                                eleNamesList+="^"+eleidname[1];
                                i++;
                            }else{
                                alert('Please select columns');
                                document.getElementById("advoperators").value="";

                            }

                        }
                        if(eleIdsList!=""){
                            eleIdsList=eleIdsList.substring(1);
                            eleNamesList=eleNamesList.substring(1);
                        }
                        // alert(eleIdsList+"'==eleIdsList")
                        if(eleIdsList!=""){
                            if(i<1)
                            {
                                alert("Please select at least one column ");
                                document.getElementById("advoperators").value="";
                            }
                            else
                            {
                                var eleIdsListarr=eleIdsList.split(",");
                                var eleNamesListarr=eleNamesList.split("^");
                                var formulaNames="";
                                var formulaIds="";
                                // alert(seloperator)
                                if(seloperator=="avg"){
                                    //alert(eleIdsListarr.length)
                                    if(eleIdsListarr.length==1){


                                        $.ajax({
                                            url: 'reportTemplateAction.do?templateParam=getElementDataType&dependenteleids='+eleIdsListarr,
                                            success: function(data){
                                                if(data!=""){
                                                    var  list=data.split(';');
                                                    var list1=list[0].split('-');
                                                    var first="";


                                                    if(list1[0]!="SUMMARISED" && list1[0]!="SUMMARIZED"){
                                                        for(var i=0;i<1;i++){
                                                            formulaNames+="AVG("+eleNamesListarr[i]+")";
                                                            formulaIds+="AVG("+eleIdsListarr[i]+")";
                                                        }
                                                        checkflag="YES";
                                                    }else{
                                                        first=list1[1];

                                                        formulaNames+=first+"("+eleNamesListarr[0]+")/(case when "+first+"("+eleNamesListarr[0]+")=0 then null else "+first+"("+eleNamesListarr[0]+") end )";
                                                        formulaIds+=first+"("+eleIdsListarr[0]+")/(case when "+first+"("+eleIdsListarr[0]+")=0 then null else "+first+"("+eleIdsListarr[0]+") end )";


                                                        if(formulaNames!=""){
                                                            formulaNames="("+formulaNames+")";
                                                            formulaIds="("+formulaIds+")";
                                                        }
                                                    }
                                                    var actformula=document.getElementById("txt2").value;
                                                    actformula=actformula+formulaNames;
                                                    var acteleformula=document.getElementById("tArea").value;
                                                    acteleformula=acteleformula+formulaIds;
                                                    var tArea1val= document.getElementById('tArea1').value;
                                                    tArea1val=tArea1val+","+eleIdsList;

                                                    document.getElementById("txt2").value=actformula;
                                                    document.getElementById("tArea").value=acteleformula;
                                                    document.getElementById('tArea1').value=tArea1val;
                                                    prevClass="Query";
                                                    formArray[arrIndex]=actformula;
                                                    formArray1[arrIndex1]=acteleformula;
                                                    prevClassType[prevIndex]="Query";
                                                    arrIndex++;
                                                    arrIndex1++;
                                                    prevIndex++;
                                                }
                                                else {
                                                    for(var i=0;i<1;i++){
                                                        formulaNames+="AVG("+eleNamesListarr[i]+")";
                                                        formulaIds+="AVG("+eleIdsListarr[i]+")";
                                                    }
                                                    checkflag="YES";
                                                }
                                            }
                                        });



                                    }else{

                                        if(eleIdsListarr.length==2){



                                            $.ajax({
                                                url: 'reportTemplateAction.do?templateParam=checkavgTwoTables&dependenteleids='+eleIdsListarr,
                                                success: function(data){
                                                    // alert('data'+data)
                                                    if(data!=""){
                                                        var  list=data.split(';');
                                                        var list1=list[0].split('-');
                                                        var list2=list[1].split('-');
                                                        var first="";
                                                        var sec="";
                                                        var third="";
                                                        if(list1[0]==eleNamesListarr[0]){
                                                            if(list1[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                                first=list1[1];
                                                            }
                                                            if(list2[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                                sec=list2[1];
                                                            }

                                                        }else{
                                                            if(list2[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                                first=list2[1];
                                                            }
                                                            if(list1[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                                sec=list1[1];
                                                            }

                                                        }
                                                        formulaNames+=first+"("+eleNamesListarr[0]+")/(case when "+sec+"("+eleNamesListarr[1]+")=0 then null else "+sec+"("+eleNamesListarr[1]+") end )";
                                                        formulaIds+=first+"("+eleIdsListarr[0]+")/(case when "+sec+"("+eleIdsListarr[1]+")=0 then null else "+sec+"("+eleIdsListarr[1]+") end )";


                                                        if(formulaNames!=""){
                                                            // formulaNames=formulaNames.substring(1);
                                                            // formulaIds=formulaIds.substring(1);
                                                            // formulaNames="("+formulaNames+")/count("+eleNamesListarr[0]+")";
                                                            // formulaIds="("+formulaIds+")/count("+eleIdsListarr[0]+")";
                                                            formulaNames="("+formulaNames+")";
                                                            formulaIds="("+formulaIds+")";
                                                        }
                                                        var actformula=document.getElementById("txt2").value;
                                                        actformula=actformula+formulaNames;
                                                        var acteleformula=document.getElementById("tArea").value;
                                                        acteleformula=acteleformula+formulaIds;
                                                        var tArea1val= document.getElementById('tArea1').value;
                                                        tArea1val=tArea1val+","+eleIdsList;
                                                        // alert(actformula+"===="+acteleformula);
                                                        document.getElementById("txt2").value=actformula;
                                                        document.getElementById("tArea").value=acteleformula;
                                                        document.getElementById('tArea1').value=tArea1val;
                                                        prevClass="Query";
                                                        formArray[arrIndex]=actformula;
                                                        formArray1[arrIndex1]=acteleformula;
                                                        prevClassType[prevIndex]="Query";
                                                        arrIndex++;
                                                        arrIndex1++;
                                                        prevIndex++;
                                                    }
                                                    else {
                                                        alert('Average is not supported \n Please select division')
                                                    }
                                                }
                                            });
                                        }else{
                                            alert('Please select two columns only ')
                                        }
                                    }
                                    // alert('vgbhgn')
                                }
                                if(seloperator=="min"){
                                    if(eleIdsListarr.length>1){
                                        alert('Please select only one columns');
                                    }else{
                                        for(var i=0;i<1;i++){
                                            formulaNames+="MIN("+eleNamesListarr[i]+")";
                                            formulaIds+="MIN("+eleIdsListarr[i]+")";
                                        }
                                        checkflag="YES";
                                        if(formulaNames!=""){
                                            //formulaNames=formulaNames.substring(1);
                                            //formulaIds=formulaIds.substring(1);
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";

                                        }
                                    }
                                }
                                if(seloperator=="max"){
                                    if(eleIdsListarr.length>1){
                                        alert('Please select only one columns');
                                    }else{
                                        for(var i=0;i<1;i++){
                                            formulaNames+="MAX("+eleNamesListarr[i]+")";
                                            formulaIds+="MAX("+eleIdsListarr[i]+")";
                                        }
                                        checkflag="YES";
                                        if(formulaNames!=""){
                                            //formulaNames=formulaNames.substring(1);
                                            // formulaIds=formulaIds.substring(1);
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                    }
                                }
                                if(seloperator=="totper"){
                                    if(eleIdsListarr.length>1){
                                        alert('Please select only one columns');
                                    }else{

                                        for(var i=0;i<1;i++){
                                            formulaNames+="/"+eleNamesListarr[i]+"";
                                            formulaIds+="/"+eleIdsListarr[i]+"";
                                        }
                                        checkflag="YES";
                                        if(formulaNames!=""){
                                            // formulaNames=formulaNames.substring(1);
                                            //formulaIds=formulaIds.substring(1);
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";

                                        }
                                    }
                                }
                                if(seloperator=="sd"){
                                    if(eleIdsListarr.length>2){
                                        alert('Please select two columns');
                                    }else if(eleIdsListarr.length<2){
                                        alert('Please select two columns');
                                    }else{


                                        formulaNames+="(SUM("+eleNamesListarr[0]+")-SUM("+eleNamesListarr[1]+"))/(case when SUM("+eleNamesListarr[0]+")=0 then null else SUM("+eleNamesListarr[0]+") end )";
                                        // formulaIds+="("+eleIdsListarr[0]+"-"+eleIdsListarr[1]+")/nvl("+eleIdsListarr[0]+",0)";
                                        formulaIds+="(SUM("+eleIdsListarr[0]+")-SUM("+eleIdsListarr[1]+"))/(case when SUM("+eleIdsListarr[0]+")=0 then null else SUM("+eleIdsListarr[0]+") end )";
                                        checkflag="YES";
                                        if(formulaNames!=""){
                                            // formulaNames=formulaNames.substring(1);
                                            // formulaIds=formulaIds.substring(1);
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                    }
                                }
                                if(seloperator=="sdper"){
                                    if(eleIdsListarr.length>2){
                                        alert('Please select two columns');
                                    }else if(eleIdsListarr.length<2){
                                        alert('Please select two columns');
                                    }else{


                                        formulaNames+="((SUM("+eleNamesListarr[0]+")-SUM("+eleNamesListarr[1]+"))/(case when SUM("+eleNamesListarr[0]+")=0 then null else SUM("+eleNamesListarr[0]+") end ))*100";
                                        // formulaIds+="("+eleIdsListarr[0]+"-"+eleIdsListarr[1]+")/nvl("+eleIdsListarr[0]+",0)";
                                        formulaIds+="((SUM("+eleIdsListarr[0]+")-SUM("+eleIdsListarr[1]+"))/(case when SUM("+eleIdsListarr[0]+")=0 then null else SUM("+eleIdsListarr[0]+") end ))*100";
                                        checkflag="YES";
                                        if(formulaNames!=""){
                                            // formulaNames=formulaNames.substring(1);
                                            // formulaIds=formulaIds.substring(1);
                                            formulaNames="("+formulaNames+")";
                                            formulaIds="("+formulaIds+")";
                                        }
                                    }
                                }
                                document.getElementById("advoperators").value="";
                                //  alert('--------'+checkflag)
                                if(checkflag=="YES"){
                                    var actformula=document.getElementById("txt2").value;
                                    actformula=actformula+formulaNames;
                                    var acteleformula=document.getElementById("tArea").value;
                                    acteleformula=acteleformula+formulaIds;
                                    var tArea1val= document.getElementById('tArea1').value;
                                    tArea1val=tArea1val+","+eleIdsList;
                                    //  alert(actformula+"===="+acteleformula);
                                    document.getElementById("txt2").value=actformula;
                                    document.getElementById("tArea").value=acteleformula;
                                    document.getElementById('tArea1').value=tArea1val;
                                    prevClass="Query";
                                    formArray[arrIndex]=actformula;
                                    formArray1[arrIndex1]=acteleformula;
                                    prevClassType[prevIndex]="Query";
                                    arrIndex++;
                                    arrIndex1++;
                                    prevIndex++;
                                }
                            }

                        }
                    }
                    /* }else{
                    alert('Please select operator from buttons pad');
                    document.getElementById("basicoperators").value="";

                }*/

                }
                function selectColumn(){
                    var obj= document.getElementById('draggedcolumns').getElementsByTagName('input');
                    if(obj==undefined){
                        alert('Please Drag atleast one columns');
                        document.getElementById("advoperators").value="";

                    }else{
                        var i=0;
                        var eleIdsList="";
                        var eleNamesList="";
                        if(obj.length!=undefined){
                            for(var j=0;j<obj.length;j++)
                            {     //alert(j)
                                if(document.myForm.ChkFor[j].checked==true)
                                {    //alert(document.myForm.ChkFor[j].value)
                                    var eleidname=document.myForm.ChkFor[j].value.split("^");
                                    //alert(eleidname[0])
                                    eleIdsList+=","+eleidname[0];
                                    eleNamesList+="^"+eleidname[1];
                                    i++;
                                }
                            }
                            if(i==0){
                                alert('Please select columns');

                            }else if(i>1){
                                alert('Please select only one column at a time');
                            }else{
                                if(eleIdsList!=""){
                                    eleIdsList=eleIdsList.substring(1);
                                    eleNamesList=eleNamesList.substring(1);
                                }
                                var actformula=document.getElementById("txt2").value;
                                actformula=actformula+eleNamesList;
                                var acteleformula=document.getElementById("tArea").value;
                                acteleformula=acteleformula+eleIdsList;
                                var tArea1val= document.getElementById('tArea1').value;
                                tArea1val=tArea1val+","+eleIdsList;
                                //alert(actformula+"===="+acteleformula);
                                document.getElementById("txt2").value=actformula;
                                document.getElementById("tArea").value=acteleformula;
                                document.getElementById('tArea1').value=tArea1val;
                                prevClass="Query";
                                formArray[arrIndex]=actformula;
                                formArray1[arrIndex1]=acteleformula;
                                prevClassType[prevIndex]="Query";
                                arrIndex++;
                                arrIndex1++;
                                prevIndex++;

                            }
                        }else{
                            if(document.myForm.ChkFor.checked==true){
                                var eleidname=document.myForm.ChkFor.value.split("^");
                                eleIdsList+=","+eleidname[0];
                                eleNamesList+="^"+eleidname[1];
                                if(eleIdsList!=""){
                                    eleIdsList=eleIdsList.substring(1);
                                    eleNamesList=eleNamesList.substring(1);
                                }
                                var actformula=document.getElementById("txt2").value;
                                actformula=actformula+eleNamesList;
                                var acteleformula=document.getElementById("tArea").value;
                                acteleformula=acteleformula+eleIdsList;
                                var tArea1val= document.getElementById('tArea1').value;
                                tArea1val=tArea1val+","+eleIdsList;
                                //alert(actformula+"===="+acteleformula);
                                document.getElementById("txt2").value=actformula;
                                document.getElementById("tArea").value=acteleformula;
                                document.getElementById('tArea1').value=tArea1val;
                                prevClass="Query";
                                formArray[arrIndex]=actformula;
                                formArray1[arrIndex1]=acteleformula;
                                prevClassType[prevIndex]="Query";
                                arrIndex++;
                                arrIndex1++;
                                prevIndex++;

                            }else{
                                alert('Please select columns');

                            }
                        }
                    }

                }
                function selectColumn1(str){

                    var i=0;
                    var eleIdsList="";
                    var eleNamesList="";

                    var eleidname=str.split("^");
                    eleIdsList+=","+eleidname[0];
                    eleNamesList+="^"+eleidname[1];
                    if(eleIdsList!=""){
                        eleIdsList=eleIdsList.substring(1);
                        eleNamesList=eleNamesList.substring(1);
                    }
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+eleNamesList;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+eleIdsList;
                    var tArea1val= document.getElementById('tArea1').value;
                    tArea1val=tArea1val+","+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    document.getElementById('tArea1').value=tArea1val;
                    prevClass="Query";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="Query";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;



                }

                function getDetails(){
//                    var formulaType = document.forms.myForm.formulaType.value;
                    var formulaType = '<%=formulaVal%>';
                    var basicOp = '<%=basicOp%>';
                    var htmlVar = "";
//                     $("#myTabBody").html(htmlVar)  
                     if(formulaType!='NONE'){
                                      <%
                                      String userColType = "";
                                      if(formulaVal.equalsIgnoreCase("DATE")){
                                          userColType = "'DATETIME','datetime','DATE','date'";
                                      }
                                      else if(formulaVal.equalsIgnoreCase("NUMBER")){
                                          userColType = "'NUMBER','number','calculated','CALCULATED'";
                                      }
                                      else if(formulaVal.equalsIgnoreCase("VARCHAR")){
                                          if(basicOp.equalsIgnoreCase("CONCAT"))
                                                userColType = "'VARCHAR','VARCHAR2','varchar','varchar2'";
                                          else
                                                userColType = "'VARCHAR','VARCHAR2','varchar','varchar2','NUMBER','number'";
                                      }
                                      
                                        if (!"".equalsIgnoreCase(eleId)) {
                                            elementIdsList = elementIdsList.substring(1);
                               PbReturnObject pbro1 = new PbReturnObject();
                        String getBussTabId = "";
                        getBussTabId = "select buss_table_id,folder_id,grp_id from prg_user_all_info_details where element_id ="+eleId;
                        pbro1 = pbdb.execSelectSQL(getBussTabId);
                         bussTabId = pbro1.getFieldValueString(0,0);
                         folderId = pbro1.getFieldValueString(0,1);
                            grpId = pbro1.getFieldValueString(0,2);
                         String selecteditemsQuery = "";
                         
                        if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            selecteditemsQuery = "select DISTINCT element_id, isnull(disp_name,sub_folder_type),"
                                    + "isnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
                                    + "user_col_type,actual_col_formula,folder_id,isnull(BUSS_COL_NAME,user_col_name),isnull(AGGREGATION_TYPE,'0')"
                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in (select element_id from PRG_USER_ALL_INFO_DETAILS "
                                    + " where buss_table_id =" + bussTabId + " and user_col_type in("+userColType+")) and folder_id = " + folderId + " and grp_id = "+grpId+" and REF_ELEMENT_TYPE = 1";
                        }
                        else if(dbType.equalsIgnoreCase(ProgenConnection.MYSQL)){
                        selecteditemsQuery = "select DISTINCT element_id, ifnull(disp_name,sub_folder_type),"
                                    + "ifnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
                                    + "user_col_type,actual_col_formula,folder_id,ifnull(BUSS_COL_NAME,user_col_name),ifnull(AGGREGATION_TYPE,'0')"
                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in(select element_id from PRG_USER_ALL_INFO_DETAILS "
                                    + " where buss_table_id =" + bussTabId + " and user_col_type in("+userColType+")) and folder_id = " + folderId + " and grp_id = "+grpId+" and REF_ELEMENT_TYPE = 1";
                        }
                                               else {

                            selecteditemsQuery = "select DISTINCT element_id, nvl(disp_name,sub_folder_type),"
                                    + "nvl(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
                                    + "user_col_type,actual_col_formula,folder_id,nvl(BUSS_COL_NAME,user_col_name),nvl(AGGREGATION_TYPE,'0')"
                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in(select element_id from PRG_USER_ALL_INFO_DETAILS "
                                    + " where buss_table_id =" + bussTabId + " and user_col_type in("+userColType+")) and folder_id = " + folderId + " and grp_id = "+grpId+" and REF_ELEMENT_TYPE = 1";
                                                               }
                         pbro = pbdb.execSelectSQL(selecteditemsQuery);
                        isSelected = "YES";
                        
                     }%>


                        htmlVar+="<table style=\"width:100%\" border=\"1px solid\">";
                        htmlVar+="<tr style=\"width:100%\">";
                        htmlVar+="<td width=\"50%\" valign=\"top\" class='draggedTable1'>";
                        htmlVar+="<div style=\"height:15px\" class=\"ui-state-default draggedDivs ui-corner-all\"><font size=\"1\" style=\"font-weight:bold\">Drag Columns From here</font></div>";
                        htmlVar+="<div id=\"afterTable\" style=\"height:150px;overflow-y:auto;\">";
                        htmlVar+="<ul id=\"myList\">";
                        <%
                            if (pbro != null) {
                            if(formulaVal.equalsIgnoreCase("DATE")){    
                         %>
                           htmlVar+="<li style=\"background:white;color:transparent\"><img  alt=\"\" src=\"icons pinvoke/report.png\" />";
                           htmlVar+="<span class=\"myDragTabs\"  style=\"color:#000\" id=\"<%=eleId%>^PROGEN_ST_DATE\" >PROGEN_ST_DATE</span>";
                           htmlVar+="<label class=\"label\" style=\"color:green\"><b>{D}</b></label>";
                           htmlVar+="</li>";
                           htmlVar+="<li style=\"background:white;color:transparent\"><img  alt=\"\" src=\"icons pinvoke/report.png\" />";
                           htmlVar+="<span class=\"myDragTabs\"  style=\"color:#000\" id=\"1111^PROGENT_END_DATE\" >PROGENT_END_DATE</span>";
                           htmlVar+="<label class=\"label\" style=\"color:green\"><b>{D}</b></label>";
                           htmlVar+="</li>";
                                     <%}
                                for (int i = 0; i < pbro.getRowCount(); i++) {
                                    if (!elementId.equalsIgnoreCase(pbro.getFieldValueString(i, 0))) {

                                        String colType = pbro.getFieldValueString(i, 4);
                        %>

                           htmlVar+="<li style=\"background:white;color:transparent\"><img  alt=\"\" src=\"icons pinvoke/report.png\" />";
                           htmlVar+="<span class=\"myDragTabs\"  style=\"color:#000\" id=\"<%=pbro.getFieldValueString(i, 0)%>^<%=pbro.getFieldValueString(i, 2)%>\" ><%=pbro.getFieldValueString(i, 2)%></span>";
                           <%if (colType.equalsIgnoreCase("NUMBER")) {%>
                           htmlVar+="<label class=\"label\" style=\"color:green\"><b>{N}</b></label>";
                           <%} else if (colType.equalsIgnoreCase("VARCHAR2")) {%>
                           htmlVar+="<label class=\"label\" style=\"color:green\"><b>{T}</b></label>";
                           <%} else if (colType.equalsIgnoreCase("DATE")) {%>
                           htmlVar+="<label class=\"label\" style=\"color:green\"><b>{D}</b></label>";
                           <%} else if (colType.equalsIgnoreCase("calculated")) {%>
                           htmlVar+="<label class=\"label\" style=\"color:green\"><b>{C}</b></label>";
                           <%} else if (colType.equalsIgnoreCase("summarised") || colType.equalsIgnoreCase("SUMMARIZED")) {%>
                           htmlVar+="<label class=\"label\" style=\"color:green\"><b>{S}</b></label>";
                           <%}%>
                           htmlVar+="</li>";

                        <%  }
                           }
                           }
                        %>
                    }
                    
              else
                  {
                        htmlVar+="<table style=\"width:100%\" border=\"1px solid\">";
                        htmlVar+="<tr style=\"width:100%\">";
                        htmlVar+="<td width=\"50%\" valign=\"top\" class='draggedTable1'>";
                        htmlVar+="<div style=\"height:15px\" class=\"ui-state-default draggedDivs ui-corner-all\"><font size=\"1\" style=\"font-weight:bold\">Drag Columns From here</font></div>";
                        htmlVar+="<div id=\"afterTable\" style=\"height:150px;overflow-y:auto;\">";
                        htmlVar+="<ul id=\"myList\">";
                  }
                                htmlVar+="</ul>";
                                htmlVar+="</div>";
                                htmlVar+="</td>";
                                htmlVar+="<td id=\"dropTabs\" width=\"50%\" valign=\"top\">";
                                htmlVar+="<div style=\"height:15px\" class=\"ui-state-default draggedDivs ui-corner-all\"><font size=\"1\" style=\"font-weight:bold\">Drag Columns to here</font></div>";
                                htmlVar+="<div style=\"height:150px;overflow-y:auto\" id=\"draggedcolumns\">";
                                htmlVar+="<ul id=\"sortableUL\">";
                                htmlVar+="</ul>";
                                htmlVar+="</div>";
                                htmlVar+="</td>";
                                htmlVar+="</tr>";
                                htmlVar+="</table>";    
                                
                                var htmlVar1 = "";
                                var htmlVar2 = "";                                
                                var htmlVar3 = "";
                                var htmlVar4 = "";
                                
                                htmlVar1+= "<option value=\"\">--select--</option>";
                                if(formulaType=='DATE'){
                                     $('#operatorstextarea').hide();
                                     $('#NumerVal').hide();
                                    htmlVar1+="<option value=\"-\">Difference(-)</option>";
                                }
                                else if(formulaType=='NUMBER'){
                                     $('#operatorstextarea').show();
                                     $('#NumerVal').show();
                                    htmlVar1+="<option value=\"+\">Sum(+)</option>";
                                    htmlVar1+="<option value=\"-\">Difference(-)</option>";
                                    htmlVar1+="<option value=\"*\">Multiplication(*)</option>";
                                    htmlVar1+="<option value=\"/\">Division(/)</option>";
                                    htmlVar1+="<option value=\"%\">Percent(%)</option>";
                                    htmlVar1+="<option value=\"ABB\">Percent((A-B)/B)</option>";
                                    htmlVar1+="<option value=\"ABB%\">Percent((A-B)/B%)</option>";
                                    htmlVar1+="<option value=\"ABA\">Percent((A-B)/A)</option>";
                                    htmlVar1+="<option value=\"ABA%\">Percent((A-B)/A%)</option>";
                                                                       
                                    htmlVar2+="<input type=\"radio\" name=\"checkVal\" value=\"case\" id=\"checkval1\" checked > Case";
                                    htmlVar2+="<input type=\"radio\" name=\"checkVal\" value=\"none\" id=\"checkval2\"> None";
                                    htmlVar2+="<input type=\"radio\" name=\"checkVal\" value=\"dbspecific\" id=\"checkval3\"> DataBase Specific";
                                    htmlVar2+="</td>";
                                    
                                    htmlVar3+="<td><input type=\"text\" id=\"Numer\" name=\"Numer\" onblur=\"test()\" value=\"\"></td>";
                                    htmlVar4+="<td width=\"50%\" valign=\"top\"><label class=\"label\">{Press tab after enter number}</label></td>";                                    
                                }
                                else if(formulaType=='VARCHAR'){
                                    var htmlVarAgg = "";                                    
                                    
                                    if(basicOp=='INTER'){   
                                        htmlVar3+="<td><input type=\"text\" id=\"Numer\" name=\"Numer\" onblur=\"test()\" value=\"\"></td>";
                                        htmlVar4+="<td width=\"50%\" valign=\"top\"><label class=\"label\">{Press tab after enter number}</label></td>";   
                                        
                                        $('#operatorstextarea').show();
                                        $('#NumerVal').show();
                                        $('#lblOp').hide();
                                        $('#basicopre').hide();
                                    }
                                    else {
                                        htmlVar2+="<input type=\"radio\" name=\"checkVal\" value=\"case\" id=\"checkval1\" checked > Case";
                                        htmlVar2+="<input type=\"radio\" name=\"checkVal\" value=\"none\" id=\"checkval2\"> None";
                                        htmlVar2+="<input type=\"radio\" name=\"checkVal\" value=\"dbspecific\" id=\"checkval3\"> DataBase Specific";
                                        htmlVar2+="</td>";
                                    
                                        htmlVar1+="<option value=\"CONCAT\">CONCATINATION</option>";   
                                        
                                        $('#operatorstextarea').hide();
                                        $('#NumerVal').hide();
                                    }                                   
                                    htmlVarAgg+="<option value=\"none\">NONE</option>"; 
                                    htmlVarAgg+="<option value=\"default\">DEFAULT</option>"; 
                                    htmlVarAgg+="<option value=\"sum\">SUM</option>"; 
                                    htmlVarAgg+="<option value=\"avg\">AVG</option>"; 
                                    htmlVarAgg+="<option value=\"min\">MIN</option>"; 
                                    htmlVarAgg+="<option value=\"max\">MAX</option>"; 
                                    $("#aggrType").html(htmlVarAgg)                                    
                                }  
                                
                                 $("#myTabBody").html(htmlVar)
                                 $("#basicoperators").html(htmlVar1)
                                 $("#dbType").html(htmlVar2)
                                 $("#NumerText").html(htmlVar3)
                                 $("#NumerLbl").html(htmlVar4)

                                 $("#myList3").treeview({
                                    animated:"slow",
                                    persist: "cookie"
                                });

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

               $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortableUL");
              }
                    });

                }

        </script>
                        </body>
</html>

        <%
                 request.getSession().removeAttribute("reportId");
                  request.getSession().removeAttribute("loadDialogs");
                  request.getSession().removeAttribute("from");
                  if ( request.getSession().getAttribute("folderIds") != null )
                      request.getSession().removeAttribute("folderIds");
               }  catch (Exception ex) {
                        ex.printStackTrace();
                    }
      }
       %>
