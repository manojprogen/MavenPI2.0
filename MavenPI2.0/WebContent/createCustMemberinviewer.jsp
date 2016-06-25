<%@page import="java.util.List"%>
<%@page import="com.progen.report.XtendReportMeta"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,java.sql.*,prg.db.PbReturnObject,prg.db.PbDb,prg.db.Container,java.util.HashMap,java.util.ArrayList,utils.db.ProgenConnection,com.progen.query.RTMeasureElement"%>
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
              String ContextPath=request.getContextPath();
%>
<html>
    <head>
        <title></title>
        <script src="<%=ContextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ContextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=ContextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=ContextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ContextPath%>/javascript/treeview/demo.js"></script>
        <script type="JavaScript" src="<%=ContextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=ContextPath%>/javascript/quicksearch.js"></script>
        <link rel="stylesheet" href="<%=ContextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=ContextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
<!--below plugin added by Dinanath for drag and drop working in ie9.-->
               <script type="text/javascript" src="<%=ContextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=ContextPath%>/javascript/reportDesign.js"></script>
         <link href="<%=ContextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=ContextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" src="<%=ContextPath%>/javascript/pbReportViewerJS.js" ></script>
        <script  type="text/javascript" src="<%=ContextPath%>/javascript/progenGraphViewer.js" ></script>
        <link type="text/css" href="<%=ContextPath%>/css/global.css" rel="stylesheet" />
    <%
                //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                //ended By Mohit Gupta
                String dbType = "";
                if (session.getAttribute("MetadataDbType") != null) {
                    dbType = (String) session.getAttribute("MetadataDbType");
                }
                try {
                    PbDb pbdb = new PbDb();
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
                    int length = 0;


                    String reportId =(String) session.getAttribute("REPORTID");

                   //if (reportId == null)
                        reportId = (String) session.getAttribute("reportId");
                        if(reportId == null) {
                            reportId = request.getParameter("reportId");
                        }else if(reportId.equalsIgnoreCase("null")){
                            reportId = request.getParameter("reportId");
                            }else if(reportId.equalsIgnoreCase("")){
                            reportId = request.getParameter("reportId");
                            }
                        else{
                            }

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
                    String aggType = "";
                    String prePostVal = "";
                if("true".equalsIgnoreCase(fromEdit)){
                      formula = request.getParameter("formula");
                      measureLabel = request.getParameter("measureName");
                      refferedMeasureIds = request.getParameter("refferedMeasureIds");
                      refferedMeasureLabels = request.getParameter("refferedMeasureLabels");
                      elementId = request.getParameter("elementId");
                      aggType = request.getParameter("aggType");
                      prePostVal = request.getParameter("prePostVal");
                    }

                boolean isEdit = false;
                if(("true").equalsIgnoreCase(fromEdit)){
                    isEdit = true;
                    }                    ////////////////////////////////////////////////.println.println(" ParametersHashMap" + ParametersHashMap);
                    if (ParametersHashMap != null) {
                        if (ParametersHashMap.get("Parameters") != null) {
                            params = (ArrayList) ParametersHashMap.get("Parameters");
                        }
                        if (ParametersHashMap.get("ParametersNames") != null) {
                            params_names = (ArrayList) ParametersHashMap.get("ParametersNames");
                        }


                    }
                    // ArrayList selectedElementIds=new ArrayList();
                    // ArrayList selectedElementIdsNaems=new ArrayList();
                    String elementIdsList = "";
                    String elementIdsListwithoutperc = "";
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

                    PbReturnObject pbro = new PbReturnObject();
                    String isSelected = "NO";
                         String[] refferedElementIds = refferedMeasureIds.split(",");

                          for(String refferdElement : refferedElementIds){
                            if(!elementIdsListwithoutperc.contains(refferdElement)){
                                elementIdsListwithoutperc = elementIdsListwithoutperc+","+refferdElement;
                                }
                           }

                    //////.println("elementIdsList====" + elementIdsList);
                    //////.println("elementIdsListwithoutperc====" + elementIdsListwithoutperc);
                    if (!"".equalsIgnoreCase(elementIdsList)) {
                        elementIdsList = elementIdsList.substring(1);
                        elementIdsListwithoutperc = elementIdsListwithoutperc.substring(1);
                        String selecteditemsQuery = "";
                        if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            selecteditemsQuery = "select DISTINCT element_id, isnull(disp_name,sub_folder_type),"
                                    + "isnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
                                    + "user_col_type,actual_col_formula,folder_id,isnull(BUSS_COL_NAME,user_col_name),isnull(AGGREGATION_TYPE,'0')"
                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in(" + elementIdsListwithoutperc + ")";
                        }
                        else if(dbType.equalsIgnoreCase(ProgenConnection.MYSQL)){
                        selecteditemsQuery = "select DISTINCT element_id, ifnull(disp_name,sub_folder_type),"
                                    + "ifnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
                                    + "user_col_type,actual_col_formula,folder_id,ifnull(BUSS_COL_NAME,user_col_name),ifnull(AGGREGATION_TYPE,'0')"
                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in(" + elementIdsListwithoutperc + ")";
                        }

                                               else {

                            selecteditemsQuery = "select DISTINCT element_id, nvl(disp_name,sub_folder_type),"
                                    + "nvl(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, "
                                    + "user_col_type,actual_col_formula,folder_id,nvl(BUSS_COL_NAME,user_col_name),nvl(AGGREGATION_TYPE,'0')"
                                    + " from PRG_USER_ALL_INFO_DETAILS where element_id in(" + elementIdsListwithoutperc + ")";
                        }

                        //////.println("selecteditemsQuery---" + selecteditemsQuery);
                        pbro = pbdb.execSelectSQL(selecteditemsQuery);
                        isSelected = "YES";
                    }
                    /* String Query="";
                      String Query2="";
                       String Query1="";
                    if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        Query = "select DISTINCT isnull(disp_name,sub_folder_type), connection_id,folder_name  from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts'  and disp_name not in('Calculated Facts','Formula') ";
                        Query2 = "select DISTINCT folder_name  from prg_user_all_info_details where folder_id in (" + folderIds + ")";
                        Query1 = "select DISTINCT element_id, isnull(disp_name,sub_folder_type),isnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, user_col_type,actual_col_formula,folder_id,isnull(BUSS_COL_NAME,user_col_name),isnull(AGGREGATION_TYPE,'0') from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts' and ref_element_id=element_id and AGGREGATION_TYPE not in('calculated','summarised' )order by isnull(BUSS_COL_NAME,user_col_name),ref_element_id";


                    } else {
                        Query = "select DISTINCT nvl(disp_name,sub_folder_type), connection_id,folder_name  from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts'  and disp_name not in('Calculated Facts','Formula') ";
                        Query2 = "select DISTINCT folder_name  from prg_user_all_info_details where folder_id in (" + folderIds + ")";
                        Query1 = "select DISTINCT element_id, nvl(disp_name,sub_folder_type),nvl(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, user_col_type,actual_col_formula,folder_id,nvl(BUSS_COL_NAME,user_col_name),nvl(AGGREGATION_TYPE,'0') from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts' and ref_element_id=element_id and AGGREGATION_TYPE not in('calculated','summarised' )order by nvl(BUSS_COL_NAME,user_col_name),ref_element_id";

                    } */
                    String tableNameList = "";
                    String connectionId = "";
                    String measID ="";
                    String tablemeasures = "";
                    for (int i = 0; i < pbro.getRowCount(); i++) {
                    if(!measID.equalsIgnoreCase(pbro.getFieldValueString(i, 0))){
                        String measureType = pbro.getFieldValueString(i, 4);
                        if(measureType.equalsIgnoreCase("NUMBER")){
                       timeBasedMeasures = timeBasedMeasures+"<li style=\'background:white;color:transparent\'><img alt=\'\' src=\'icons pinvoke/report.png\'>";
                       timeBasedMeasures = timeBasedMeasures+"<span class=\'myDragTabs\' class=\'ui-state-default\' style=\'color:#000\' id=\'"+pbro.getFieldValueString(i,0)+"^"+pbro.getFieldValueString(i,2)+"\'>"+pbro.getFieldValueString(i,2)+"</span>";
                       timeBasedMeasures = timeBasedMeasures+"<label class=\'label\' style=\'color:green\'><b>{N}</b></label></li>";
                   }
                       else if(measureType.equalsIgnoreCase("calculated")){
                       timeBasedMeasures = timeBasedMeasures+"<li style=\'background:white;color:transparent\'><img alt=\'\' src=\'icons pinvoke/report.png\'>";
                       timeBasedMeasures = timeBasedMeasures+"<span class=\'myDragTabs\' class=\'ui-state-default\' style=\'color:#000\' id=\'"+pbro.getFieldValueString(i,0)+"^"+pbro.getFieldValueString(i,2)+"\'>"+pbro.getFieldValueString(i,2)+"</span>";
                       timeBasedMeasures = timeBasedMeasures+"<label class=\'label\' style=\'color:green\'><b>{C}</b></label></li>";
                       }

                       tablemeasures = tablemeasures+"<li style=\'background:white;color:transparent\'><img alt=\'\' src=\'icons pinvoke/report.png\'>";
                       tablemeasures = tablemeasures+"<span class=\'myDragTabs\' class=\'ui-state-default\' style=\'color:#000\' id=\'"+pbro.getFieldValueString(i,0)+"^"+pbro.getFieldValueString(i,2)+"\'>"+pbro.getFieldValueString(i,2)+"</span>";
                       if(measureType.equalsIgnoreCase("NUMBER")){
                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{N}</b></label></li>";
                       }
                       else if(measureType.equalsIgnoreCase("VARCHAR2")){
                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{T}</b></label></li>";
                           }
                       else if(measureType.equalsIgnoreCase("DATE")){
                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{D}</b></label></li>";
                           }
                       else if(measureType.equalsIgnoreCase("calculated")){
                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{C}</b></label></li>";
                           }
                       else if(measureType.equalsIgnoreCase("summarised")||measureType.equalsIgnoreCase("SUMMARIZED")){
                           tablemeasures = tablemeasures+"<label class=\'label\' style=\'color:green\'><b>{S}</b></label></li>";
                       }
                    }
                    }
//boolean graphFlag = false;
                   // added by sruthi for complex and hybrid mes in graph 4/2/2016
String graphflagstr=request.getParameter("graphflag");
                   // String eleList = "";
                    // ////////////////////////////////////////////////.println.println("Query---" + Query);
                    ////////////////////////////////////////////////.println.println("Query1---" + Query1);
                    // ////////////////////////////////////////////////.println.println("Query2---" + Query2);
                    /*PbReturnObject pbro1 = pbdb.execSelectSQL(Query);
                    PbReturnObject pbro2 = pbdb.execSelectSQL(Query1);
                    PbReturnObject pbro3 = pbdb.execSelectSQL(Query2);*/
    %>

      
    </head>

    <body>
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
                      //added by Nazneen for Edit formula
                      <%if("true".equalsIgnoreCase(fromEdit)){%>
                            $("#columnName").attr('readonly','readonly');
                          <%}%>
                          $("#txt2").val('<%=formula%>');
                          var tempAggType = '<%=aggType%>';
                          var prePostVal = '<%=prePostVal%>';
                          var aggType = '';
                          if(tempAggType=='sum' || tempAggType=='SUM')
                              aggType = 'sum';
                          else if(tempAggType=='avg' || tempAggType=='AVG')
                              aggType = 'avg';
                          else if(tempAggType=='min' || tempAggType=='MIN')
                              aggType = 'min';
                          else if(tempAggType=='max' || tempAggType=='MAX')
                              aggType = 'max';
                          else
                              aggType = 'default';
                          document.getElementById('aggrType').value = aggType;
                          if(prePostVal=='pre')
                            document.getElementById('pre').checked = true;

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
            $(document).ready(function() {
             parent.$("#aggVal").val("none");
             parent.$("#fromTimeBased").val("false");
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                //addeb by bharathi reddy fro search option
                $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
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
                }
            );



            });
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
                 //changed by nazneen
                 var numbers = /^[0-9.]+$/;
                 var negNumbers = /^-[0-9.]+$/;

//                 if(x.match(numbers))
//                     alert('true')
//                 else
//                     alert('false')

//                if(x.match(/\D/g)!=null){
//                    alert("Please Enter Only Digits From 0-9");
//                    x=x.replace(/\D/g,"");
//                    document.getElementById("Numer").value=x;
//                }else{
                var a = x.split(".");
                if(x.match(numbers)==null && x.match(negNumbers)==null){
                    alert("Please Enter Only Digits From 0-9 or Digits with - sign");
                    x=x.replace(numbers,"").replace(negNumbers,"");
                    document.getElementById("Numer").value=x;
                }
                else if(a.length>2){
                   alert("Only one decimal point is allowed");
                   x=x.replace(numbers,"").replace(negNumbers,"").replace(".","");
                    document.getElementById("Numer").value=x;
                }
                else
                {
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
                    // document.getElementById('tArea1').value=tArea1val;

                    /*  }else{
                        alert('Please select operator from buttons pad')
                        document.getElementById("Numer").value="";
                    }*/


                }
            }

            function saveCustMember1(tableList,ConnectionId)
            {
                var query = document.getElementById('txt2').value;

                if(query.indexOf('+')>=0){
                    query=query.replace("+","@","gi");
                }
                //alert(query)




                var tArea1val= document.getElementById('tArea1').value;



                var tareaval= document.getElementById('tArea').value;
                if(tareaval.indexOf('+')>=0){
                    tareaval=tareaval.replace("+","@","gi");
                }

                var url = "CheckCustMeasureQuery?query="+query+"&tableList="+tableList+"&ConnectionId="+ConnectionId+"&tArea1="+tArea1val+"&tArea="+tareaval;

                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }

                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);

            }

            function saveCustMember(tableList,ConnectionId,reportId,elementIdsList){
                var functionname = "saveViewerCustomFormula";
//              added by sruthi for complex and hybrid mes in graph 4/2/2016
                var graph='<%=graphflagstr%>';
                //ended by sruthi
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


                    // document.myForm.action = "saveCustMemberRD.do";
                    //  document.myForm.submit();
                    var name=document.getElementById("columnName").value;
                    var cols=elementIdsList;
                    var txt2=document.getElementById('txt2').value;
                    var folderIds=document.getElementById('folderIds').value;
                    var columnName=document.getElementById('columnName').value;
                    var iscalculate=document.getElementById('iscalculate').value;
                    var tArea=document.getElementById('tArea').value;
                    var tArea1=document.getElementById('tArea1').value;
                    var aggrType=document.getElementById('aggrType').value;
                    var prePostVal;
                    if(document.getElementById("post").checked)
                        prePostVal="post";
                    else
                        prePostVal="pre";

                    //var eleList=document.getElementById('eleList').value;
                     txt2=txt2.replace("+","@","gi");
                     txt2=txt2.replace("%","|_|","gi");
                    txt2=txt2.replace("&","||chr(38)||","gi");
                    if(parent.document.getElementById("fromTimeBased").value=='true'){
                       tArea = elementForTimeBased;
                       tArea1 = elementForTimeBased;
                       functionname = "saveTimeBasedFormula";

                    }
                           if ( document.getElementById("from").value=="Viewer" )
                          parent.document.getElementById('loading').style.display='';
                      //changed by sruthi for complex and hybrid mes in graph 4/2/2016
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy='+functionname+'&txt2='+txt2+'&folderIds='+folderIds+'&columnName='+encodeURIComponent(columnName)+'&iscalculate='+iscalculate+'&tArea='+tArea+'&tArea1='+tArea1+'&fromEdit='+'<%=isEdit%>'+'&elementId='+'<%=elementId%>'+'&aggVal='+encodeURIComponent(parent.document.getElementById("aggVal").value)+"&fromTimeBased="+parent.document.getElementById("fromTimeBased").value+"&aggrType="+aggrType+"&prePostVal="+prePostVal+'&reportId='+reportId+'&graph='+graph,
                        success: function(data){
                            //added by shobhit for custom measure for graph on 23/1/2016
                            if(parent.$("#type").val()==='graph'){
                                   if(data===""){
                               // alert("we can't create any measure using hybrid measure");
                             parent.submitFormMeasChange();
                            }else{
                               parent.createCustomMeasureForGraphs(data,reportId);
                            }
                             return;
                            }
                            //end
                            if(data!=""){
                                if ( document.getElementById("from").value == "Viewer" )
                                {
                                    if(cols.indexOf(data)==-1){
                                      cols+=","+data;
                                    }

                                    $.ajax({
                                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+cols+'&REPORTID='+reportId,
                                        success: function(data){
                                            // alert(data+'iiii')
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


            if(parent.$("#type").val()!=='graph'){//condition added by shobhit for custom measure for graph on 23/1/2016
            $.ui.dialog.defaults.bgiframe = true;
            }
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

                        // if(query.indexOf('+')>=0){
                        //     document.getElementById('txt2').value=query.replace("+","@","gi");
                        //   }
                        //   alert(document.getElementById('txt2').value+"===="+document.getElementById('tArea').value+"==="+document.getElementById('tArea1').value)
                        document.myForm.action = "saveCustMemberRD.do";
                        document.myForm.submit();
                        var name=document.getElementById("columnName").value;
                        parent.cancelCustMembersave(name);
                    }
                    // }
                    // else
                    // {
                    //     alert(output1);
                    //  }

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
                    /*  var val=document.getElementById("txt2").value;
               var stlen=val.length;
                if(stlen<txtlength && txtlength!=stlen){
                var val2=val.substring(val.length-1);
                var val3=document.getElementById("tArea").value;
               // alert(val3)
                val3=val3.substring(0,val3.length-1);
            }else{
                var val2=val.substring(val.length-1);
                var val3=document.getElementById("tArea").value;
                val3+=val2;
                }
               // alert(val3)
                document.getElementById("tArea").value=val3;
                txtlength=val.length;
                     */
                }
            }

            function changeOperators(){
                //  alert('hi');
                var opeType=document.getElementById("opeType").value;
                if(opeType=="basic"){
                    document.getElementById("basicopre").style.display='';
                    document.getElementById("advopre").style.display='none';
                }else{
                    document.getElementById("basicopre").style.display='none';
                    document.getElementById("advopre").style.display='';
                }
            }
<%--            function activateDivs(isSelected){
                // alert('hi')
                if(isSelected=="NO"){
                    document.getElementById("beforeTable").style.display='';
                    document.getElementById("afterTable").style.display='none';
                    document.getElementById("moreColumns").style.display='none';
                }else{
                    document.getElementById("beforeTable").style.display='none';
                    document.getElementById("afterTable").style.display='';
                    document.getElementById("moreColumns").style.display='';

                }

            }
           function moreColumns(chkval){
                //alert(chkval)
                if(chkval.value=="MoreCols"){
                    // alert('in if')
                    document.getElementById("beforeTable").style.display='';
                    document.getElementById("afterTable").style.display='none';
                } else{
                    //  alert('in else')
                    document.getElementById("beforeTable").style.display='none';
                    document.getElementById("afterTable").style.display='';
                }


            }--%>
            function editformula(){
                // alert("jj")
                document.getElementById("txt2").readOnly=false;

            }

            /*    function buildbasicoperation(){
                var seloperator=document.getElementById("basicoperators").value;
                var obj = document.myForm.ChkFor;
                if(obj==undefined){
                alert('Please Drag atleast two columns');
                document.getElementById("basicoperators").value="";

                }else if(seloperator==""){
                alert('Please select operator');

                }else{

                var i=0;
                var eleIdsList="";
                var eleNamesList="";
                for(var j=0;j<obj.length;j++)
                {
                    if(document.myForm.ChkFor[j].checked==true)
                    {
                        var eleidname=document.myForm.ChkFor[j].value.split("^");
                        eleIdsList+=","+eleidname[0];
                         eleNamesList+="^"+eleidname[1];
                        i++;
                    }
                }
                if(eleIdsList!=""){
                    eleIdsList=eleIdsList.substring(1);
                    eleNamesList=eleNamesList.substring(1);
                }
                if(i<=1 && (seloperator!="+" || seloperator!="-" || seloperator!="*"  || seloperator!="/") )
                {
                    alert("Please select at least two columns ");
                    document.getElementById("basicoperators").value="";
                }else if(seloperator!="+" || seloperator!="-" || seloperator!="*"  || seloperator!="/"){
                    var eleIdsListarr=eleIdsList.split(",");
                    var eleNamesListarr=eleNamesList.split("^");
                    var formulaNames="";
                    var formulaIds="";
                  if(seloperator=="pwr"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(pwr("+eleNamesListarr[i]+"))";
                       formulaIds+="(pwr("+eleIdsListarr[i]+"))";
                      }
                    }else if(seloperator=="sqrt"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(sqrt("+eleNamesListarr[i]+"))";
                       formulaIds+="(sqrt("+eleIdsListarr[i]+"))";
                      }
                    }else if(seloperator=="Round"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(Round("+eleNamesListarr[i]+"))";
                       formulaIds+="(Round("+eleIdsListarr[i]+"))";
                      }
                    }else if(seloperator=="sqrt"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(RoundDown("+eleNamesListarr[i]+"))";
                       formulaIds+="(RoundDown("+eleIdsListarr[i]+"))";
                      }
                    }
                    else if(seloperator=="abs"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(RoundDown("+eleNamesListarr[i]+"))";
                       formulaIds+="(RoundDown("+eleIdsListarr[i]+"))";
                      }
                    }
                }
                else
                {
                    var eleIdsListarr=eleIdsList.split(",");
                    var eleNamesListarr=eleNamesList.split("^");
                    var formulaNames="";
                    var formulaIds="";
                     if(seloperator=="+"){
                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="+"+eleNamesListarr[i];
                       formulaIds+="+"+eleIdsListarr[i];
                      }
                     if(formulaNames!=""){
                           formulaNames=formulaNames.substring(1);
                           formulaIds=formulaIds.substring(1);
                           formulaNames="("+formulaNames+")";
                           formulaIds="("+formulaIds+")";
                     }
                    }
                    if(seloperator=="-"){
                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="-"+eleNamesListarr[i];
                       formulaIds+="+"+eleIdsListarr[i];
                      }
                     if(formulaNames!=""){
                         formulaNames=formulaNames.substring(1);
                         formulaIds=formulaIds.substring(1);
                         formulaNames="("+formulaNames+")";
                         formulaIds="("+formulaIds+")";

                     }
                    }
                    if(seloperator=="*"){
                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="*"+eleNamesListarr[i];
                      }
                     if(formulaNames!=""){
                         formulaNames=formulaNames.substring(1);
                         formulaIds=formulaIds.substring(1);
                         formulaNames="("+formulaNames+")";
                         formulaIds="("+formulaIds+")";

                     }
                    }
                    if(seloperator=="/"){
                      if(eleIdsListarr.length>2){
                      alert('Please select only two columns');
                      }else{

                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="/"+eleNamesListarr[i]+"";
                      }
                     if(formulaNames!=""){
                         formulaNames=formulaNames.substring(1);
                         formulaIds=formulaIds.substring(1);

                     }
                      }
                    }
                 }
                 var actformula=document.getElementById("txt2").value;
                 actformula=actformula+formulaNames;
                 var acteleformula=document.getElementById("tArea").value;
                 acteleformula=acteleformula+formulaIds;

                 alert(actformula+"===="+acteleformula);
                 document.getElementById("txt2").value=actformula;
                 document.getElementById("tArea").value=acteleformula;

                }

            }*/

            function buildbasicoperation(){

                //  if(prevClass=="" || prevClass=="Operators" ||prevClass=="OpenOper"){
                var checkflag="NO";
                var checkValue = "";
                var seloperator=document.getElementById("basicoperators").value;
                var number=document.getElementById("Numer").value;
                // var obj = document.myForm.ChkFor;
                var obj= document.getElementById('draggedcolumns').getElementsByTagName('input');
                var connType ="<%=dbType%>";

                if(document.getElementById("checkval1").checked)
                    checkValue="case";
                else if(document.getElementById("checkval2").checked)
                    checkValue="none";
                else
                    checkValue="dbspecific";

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
                    // alert(i)
                    if(eleIdsList!=""){
                        if(i<1)
                        {
                            alert("Please select at least one column ");
                            document.getElementById("basicoperators").value="";
                        }else {
                            //  alert('else if')
                            var eleIdsListarr=eleIdsList.split(",");
                            var eleNamesListarr=eleNamesList.split("^");
                            var formulaNames="";
                            var formulaIds="";
//                            if(seloperator=="pwr"){
//                                if(eleIdsListarr.length>1){
//                                    alert('Please select only one column')
//                                }else{
//                                    for(var i=0;i<1;i++){
//                                        formulaNames+="(POWER("+eleNamesListarr[i]+",2))";
//                                        formulaIds+="(POWER("+eleIdsListarr[i]+",2))";
//                                    }
//                                    checkflag="YES";
//                                }
//                            }else if(seloperator=="sqrt"){
//                                if(eleIdsListarr.length>1){
//                                    alert('Please select only one column')
//                                }else{
//                                    for(var i=0;i<1;i++){
//                                        formulaNames+="(SQRT("+eleNamesListarr[i]+"))";
//                                        formulaIds+="(SQRT("+eleIdsListarr[i]+"))";
//                                    }
//                                    checkflag="YES";
//                                }
//                            }else if(seloperator=="count"){
//                                if(eleIdsListarr.length>1){
//                                    alert('Please select only one column')
//                                }else{
//                                    for(var i=0;i<1;i++){
//                                        formulaNames+="(COUNT("+eleNamesListarr[i]+"))";
//                                        formulaIds+="(COUNT("+eleIdsListarr[i]+"))";
//                                    }
//                                    checkflag="YES";
//                                }
//                            }
//                            else if(seloperator=="Round"){
//                                if(eleIdsListarr.length>1){
//                                    alert('Please select only one column')
//                                }else{
//                                    for(var i=0;i<1;i++){
//                                        formulaNames+="(ROUND("+eleNamesListarr[i]+"))";
//                                        formulaIds+="(ROUND("+eleIdsListarr[i]+"))";
//                                    }
//                                    checkflag="YES";
//                                }
//                            }else if(seloperator=="RoundDown"){
//                                if(eleIdsListarr.length>1){
//                                    alert('Please select only one column')
//                                }else{
//
//                                    for(var i=0;i<1;i++){
//                                        formulaNames+="(ROUND("+eleNamesListarr[i]+",2))";
//                                        formulaIds+="(ROUND("+eleIdsListarr[i]+",2))";
//                                    }
//                                    checkflag="YES";
//                                }
//                            }
//                            else if(seloperator=="abs"){
//                                if(eleIdsListarr.length>1){
//                                    alert('Please select only one column')
//                                }else{
//                                    for(var i=0;i<1;i++){
//                                        formulaNames+="(ABS("+eleNamesListarr[i]+"))";
//                                        formulaIds+="(ABS("+eleIdsListarr[i]+"))";
//                                    }
//                                    checkflag="YES";
//                                }
//                            }
                            if(seloperator=="+"){
                                if(eleIdsListarr.length==1 && number==""){
                                    formulaNames+="(SUM("+eleNamesListarr[0]+"))";
                                    formulaIds+="(SUM("+eleIdsListarr[0]+"))";
                                    checkflag="YES";
                                }else{

                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        //added by nazneen

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
                                if(eleIdsListarr.length<2 ){
                                    alert('Please select atleast two colums')
                                }else{
                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        //added by nazneen

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
                                    if(number!=""){
                                        formulaNames+="-"+number;
                                        formulaIds+="-"+number;

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

                function selectTimeBased(){
            if(document.getElementById('columnName').value!=""){
                   if($("#SelectTimeBased").is(':checked')==true){
                      $("#columnName").attr('readonly','readonly');
                      $('#myList').html("");
                      $('#myList').html("<%=timeBasedMeasures%>");
                      $('#sortableUL').html("");
                     // $('#txt2').attr('readonly','readonly');
                      //$('#txt2').attr("disabled", true);
                      $('#formulatextarea').hide();
                      $('#operatorstextarea').hide();
                      parent.$('#custmemMeasureDispDia').css('height','550%');
                     //parent.document.getElementById("custmemMeasureDispDia").style.height = '550px';
                     //$('#')
                     //  $('#vv').hide();
                   }else{
                       $("#columnName").removeAttr('readonly','readonly');
                       $('#myList').html("");
                       $('#myList').html("<%=tablemeasures%>");
                       $('#sortableUL').html("");
                       parent.$("#aggVal").val("none");
                       parent.$("#fromTimeBased").val("false");
                      $('#formulatextarea').show();
                      $('#operatorstextarea').show();
                   }
//                   $('#myList').html("");
//                   $('#myList').html("<%=timeBasedMeasures%>");
//                   $('#sortableUL').html("");
                   colArray = new Array();

                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                //addeb by bharathi reddy fro search option
                $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
                });
                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                }else {
                    $("#SelectTimeBased").attr('checked',false)
                    alert("Please enter Formula Name")
                }

                }

                function showTimeBased(){
                   var divObj = document.getElementById('draggedcolumns');
                   var ulObj  = divObj.getElementsByTagName("ul");

                   var columnName = document.getElementById('columnName').value;
                   if(columnName==''){
                       alert("Enter Measure Name");
                   }else if(colArray.length==0){
                       alert("please Select One Measure");
                   }
                   else if(colArray.length>1){
                       alert("please Select Only One Measure");
                   }
                   else{
                    var liObj  = ulObj[0].getElementsByTagName("li");
                    var idVal = liObj[0].id;
                    var elementDetails = idVal.split("^");
                    var elementName = elementDetails[1];
                    var elementID = elementDetails[0].split("-");
                    elementForTimeBased = elementID[1];
                    parent.$("#TimeBasedDiaolgDisplay").dialog('open');
                    parent.$("#ui-dialog-title-TimeBasedDiaolgDisplay").html(document.getElementById('columnName').value)
                    var iframeObj =  parent.document.getElementById("TimeBasedDisplay");
                    iframeObj.src = "<%=request.getContextPath()%>/TimeBased.jsp?elementId="+elementID[1]+'&elementName='+encodeURIComponent(elementName);
                    }
                }

        </script>
        <form action=""  id="f1" name="myForm">

            <table align="center" width="100%" border="0" >
                <tr style="width:100%">
                    <td style="width:100%">
                        <table style="width:100%">
                            <tr>
                                <td >
                                    <label class="label gFontFamily gFontSize12"><b><%=TranslaterHelper.getTranslatedInLocale("Name", cL)%></b></label>
                                </td>

                                <td>
                                    <input type="text" class="gFontFamily gFontSize12" name="columnName" id="columnName" size="30">
                                </td>
                                <td width="20%" class="gFontFamily gFontSize12">
                                    <input type="checkbox" onclick="selectTimeBased()" name="SelectTimeBased" id="SelectTimeBased" value="Time Based"><%=TranslaterHelper.getTranslatedInLocale("Time_Based", cL)%>
                                    </td>
                                <td width="20%" align="center" valign="top">
                                    <input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>" onclick="saveCustMember('<%=tableNameList%>','<%=connectionId%>','<%=reportId%>','<%=elementIdsList%>')">
                                </td>
                            </tr>
                        </table>
                    </td>

                </tr>
            </table>



            <table align="center" width="100%" border="0" >
                <tr style="width:100%">
                    <td style="width:100%">
                        <table style="width:100%" border="1px solid">
                            <tr style="width:100%">

                                <td width="50%" valign="top" class="draggedTable1">
                                    <div style="height:15px" class="ui-state-default draggedDivs ui-corner-all"><font size="1" class="gFontFamily gFontSize12" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Drag_Columns_From_here", cL)%></font></div>
                                    <div id="afterTable" style="height:150px;overflow-y:auto;">
                                        <ul id="myList">
                                            <!--<script>-->
                                       
                                      <!--added by sruthi for complex and hybrid mes in graph 4/2/2016 -->     
                                            <%
                       List aggregationType = new ArrayList();
                          List nameListName = new ArrayList(); 
                          List nameListIds = new ArrayList();
                          
                                                       if(graphflagstr!=null && graphflagstr.equalsIgnoreCase("true")){
                                                           XtendReportMeta reportMeta = new XtendReportMeta();   
                                                            reportMeta = container.getReportMeta();
                                                              nameListName = reportMeta.getMeasures();
                                                             nameListIds = reportMeta.getMeasuresIds();
                                                            aggregationType = reportMeta.getAggregations();
                                                                for (int j = 0; j < nameListIds.size(); j++) {%>
                                                                <li style="background:white;color:transparent"><img  alt="" src='icons pinvoke/report.png' />
                                                <span class="myDragTabs gFontFamily gFontSize12" class="ui-state-default"  style="color:#000" id="<%=nameListIds.get(j)%>^<%=nameListName.get(j)%>" ><%=nameListName.get(j)%></span>
                                            </li>
                                                                
                                                        <%        }
                                                       }else{
                                                                if (pbro != null) {
                                                                    for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                   if(!elementId.equalsIgnoreCase(pbro.getFieldValueString(i, 0))){


                                                                        String colType = pbro.getFieldValueString(i, 4);
                                                                        //   ////////////////////////////////////////////////////.println.println("colname==="+pbro2.getFieldValueString(j,2));


                                            %>

                                            <li style="background:white;color:transparent"><img  alt="" src='icons pinvoke/report.png' />

                                                <span class="myDragTabs gFontFamily gFontSize12" class="ui-state-default"  style="color:#000" id="<%=pbro.getFieldValueString(i, 0)%>^<%=pbro.getFieldValueString(i, 2)%>" ><%=pbro.getFieldValueString(i, 2)%></span>
                                                <%if (colType.equalsIgnoreCase("NUMBER")) {%>
                                                <label class="label  gFontFamily gFontSize12" style="color:green"><b>{N}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("VARCHAR2")) {%>
                                                <label class="label  gFontFamily gFontSize12" style="color:green"><b>{T}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("DATE")) {%>
                                                <label class="label  gFontFamily gFontSize12" style="color:green"><b>{D}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("calculated")) {%>
                                                <label class="label  gFontFamily gFontSize12" style="color:green"><b>{C}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("summarised") || colType.equalsIgnoreCase("SUMMARIZED")) {%>
                                                <label class="label gFontFamily gFontSize12" style="color:green"><b>{S}</b></label>
                                                <%}%>
                                            </li>
                                            <%}
                                                                    }
                                                                }} //ended by sruthi 
                                            %>
                                        </ul>
                                    </div>


                                </td>


                                <td id="dropTabs" width="50%" valign="top">
                                    <div style="height:15px" class="ui-state-default draggedDivs ui-corner-all"><font size="1" class="gFontFamily gFontSize12" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Drag_Columns_to_here", cL)%></font></div>
                                    <div style="height:150px;overflow-y:auto" id="draggedcolumns">
                                        <ul id="sortableUL" class="gFontFamily gFontSize12">
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
                            <tr>
                                <td class="gFontFamily gFontSize12">
                                        <input type="radio" name="prePostVal" value="post" id="post" checked ><%=TranslaterHelper.getTranslatedInLocale("Post", cL)%>
                                        <input type="radio" name="prePostVal" value="pre" id="pre"><%=TranslaterHelper.getTranslatedInLocale("Pre", cL)%>
                                </td>
                            </tr>
                            <!--added by Nazneen for defining default summarization in formuals-->
                            <tr>
                                    <td>
                                    <label class="label gFontFamily gFontSize12"><b><%=TranslaterHelper.getTranslatedInLocale("Aggregation_Type", cL)%> :</b></label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                     <select id="aggrType" name="aggrType">
                                            <option class="gFontFamily gFontSize12" value="default">DEFAULT</option>
                                            <option class="gFontFamily gFontSize12" value="sum">SUM</option>
                                            <option class="gFontFamily gFontSize12" value="avg">AVG</option>
                                            <option class="gFontFamily gFontSize12" value="min">MIN</option>
                                            <option class="gFontFamily gFontSize12" value="max">MAX</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                    <td>
                                        <label class="label gFontFamily gFontSize12"><b><%=TranslaterHelper.getTranslatedInLocale("Null_Value_Handling", cL)%></b></label>
                                    </td>
                            </tr>
                            <tr>
                                <td class="gFontFamily gFontSize12">
                                        <input type="radio" name="checkVal" value="case" id="checkval1" checked ><%=TranslaterHelper.getTranslatedInLocale("Case", cL)%>
                                        <input type="radio" name="checkVal" value="none" id="checkval2"> <%=TranslaterHelper.getTranslatedInLocale("None", cL)%>
                                       <input type="radio" name="checkVal" value="dbspecific" id="checkval3"> <%=TranslaterHelper.getTranslatedInLocale("DataBase_Specific", cL)%>
                                    </td>

<!--                                <td width="50%"  valign="top">
                                    <select id="opeType" name="opeType" onchange="changeOperators()">
                                        <option value="basic">Basic(Arithmetic)</option>
                                        <option value="advanced">Advanced/Statistical</option>
                                    </select>

                                </td>-->
                            </tr>
                            <tr>
                                <td width="50%" valign="top"><label class="label gFontFamily gFontSize12"><b><%=TranslaterHelper.getTranslatedInLocale("Operators", cL)%></b></label></td>
                            </tr>
                            <tr>
                                <td width="50%"  valign="top">
                                    <div id="basicopre">
                                        <select id="basicoperators" name="basicoperators" onchange="buildbasicoperation()">
                                            <option class="gFontFamily gFontSize12" value="">--select--</option>
                                            <option class="gFontFamily gFontSize12" value="+">Sum(+)</option>
                                            <option class="gFontFamily gFontSize12" value="-">Difference(-)</option>
                                            <option class="gFontFamily gFontSize12" value="*">Multiplication(*)</option>
                                            <option class="gFontFamily gFontSize12" value="/">Division(/)</option>
<!--                                            added by Nazneen-->
                                            <option class="gFontFamily gFontSize12" value="%">Percent(%)</option>
                                            <option class="gFontFamily gFontSize12" value="ABB">Percent((A-B)/B)</option>
                                            <option class="gFontFamily gFontSize12" value="ABB%">Percent((A-B)/B%)</option>
                                            <option class="gFontFamily gFontSize12" value="ABA">Percent((A-B)/A)</option>
                                            <option class="gFontFamily gFontSize12" value="ABA%">Percent((A-B)/A%)</option>
<!--                                            commented by Nazneen-->
<!--                                            <option value="pwr">Power(^)</option>
                                            <option value="sqrt">Square Root</option>
                                            <option value="Round">Round</option>
                                            <option value="RoundDown">Round DOwn</option>
                                            <option value="abs">Absolute Value</option>
                                            <option value="count">count</option>-->
                                            <!--                                            end by Nazneen-->
                                        </select>
                                    </div>
                                    <div id="advopre" style="display:none">
                                        <select id="advoperators" name="advoperators" onchange="buildadvoperation()">
                                            <option value="">--select--</option>
                                            <option value="avg">Average</option>
                                            <option value="sd">Std Deviation</option>
                                            <option value="min">Min</option>
                                            <option value="max">Max</option>
                                            <option value="sdper">Deviation(%)</option>
                                            <%-- <option value="totper">% of Total</option> --%>
                                        </select>
                                    </div>

                                </td>
                            </tr>
                            <tr>
                                <td width="50%" valign="top"><label class="label gFontFamily gFontSize12"><b><%=TranslaterHelper.getTranslatedInLocale("Number", cL)%></b></label></td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" id="Numer" name="Numer" onblur="test()">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" valign="top"><label class="label gFontFamily gFontSize12">{<%=TranslaterHelper.getTranslatedInLocale("Press_tab_after_enter_number", cL)%>}</label></td>
                            </tr>

                        </table>
                    </td>

                    <td width="50%" style="height:150px" valign="top">
                        <center><label class="label gFontFamily gFontSize12"><b><%=TranslaterHelper.getTranslatedInLocale("formula", cL)%></b>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="editformula()"><%=TranslaterHelper.getTranslatedInLocale("Edit", cL)%></a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="clearboxes()"><%=TranslaterHelper.getTranslatedInLocale("Clear_Formula", cL)%></a></label ><hr></center>
                        <textarea class="gFontFamily gFontSize12" style="width:99%;height:100%" id="txt2" name="txt2" readonly  cols="60" onkeyup="addtoothervals()" style="background-color:white;bbackground:white" rows="1"></textarea>
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
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="+"  id="+" onclick="addValue('+','+','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="-"  id="-" onclick="addValue('-','-','Operators')"></td></tr>
                                        <tr><td><input type="button" value="*" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" id="*" onclick="addValue('*','*','Operators')"></td></tr>


                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" value="/" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" id="/" onclick="addValue('/','/','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="("  id="(" onclick="addValue('( ','(','OpenOper')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value=")"   id=")" onclick="addValue(' )',')','CloseOper')"></td></tr>

                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" value="()" class="navtitle-hover gFontFamily gFontSize12" style="width:auto"  id="( )" onclick="addValue('( )','( )','SpecOper')"></td></tr>
                                        <tr><td><input type="button" value="=" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" id="=" onclick="addValue('=','=','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="," onclick="addValue(',',',','Operators')"></td></tr>
                                    </table>
                                </td><td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("Select_Column", cL)%>"  onclick="selectColumn()"></td></tr>
                                                <%--  <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="case"  onclick="addCase()"></td></tr>--%>
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" id="Undo" value="<%=TranslaterHelper.getTranslatedInLocale("Undo", cL)%>" class="btn" onclick="undoFun()"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" id="Redo" value="<%=TranslaterHelper.getTranslatedInLocale("Redo", cL)%>" class="btn" onclick="redoFun()" ></td></tr>

                                    </table>
                                </td>
                                <td>

                                    <%--   <table>
                                      <tr><td valign="top"><input type="button" value="nvl" class="navtitle-hover" style="width:auto" id="nvl" onclick="addValue('nvl(','nvl(','Operatorsfun')"></td></tr>
                                        <tr><td valign="top"><input type="button" value="sum" class="navtitle-hover" style="width:auto" id="-" onclick="addValue('sum(','sum(','Operatorsfun')"></td></tr>

                                      <tr><td valign="top"><input type="button" value="count" class="navtitle-hover" style="width:auto" id="count" onclick="addValue('count(','count(','Operatorsfun')"></td></tr>
                                    </table>
                                    --%>

                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
                                    </div>






            <table align="center" width="100%" border="0" >
                <tr style="width:100%" align="center">
                    <td width="100%" align="center" valign="top">
                        <input type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="Save" onclick="saveCustMember('<%=tableNameList%>','<%=connectionId%>','<%=reportId%>','<%=elementIdsList%>')">
                    </td>
                </tr>
            </table>
            <textarea id="tArea" name="tArea" style="display:none"> </textarea>
            <textarea id="tArea1" name="tArea1" style="display:none"> </textarea>
            <input type="hidden" name="folderIds" id="folderIds" value="<%=folderIds%>">
            <input type="hidden" name="iscalculate" id="iscalculate" >

            <input type="hidden" name="sourcePage" id="sourcePage" value="Viewer">
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
                    </form></center>

        </div>-->
                    <input type="hidden" id="from" value="<%=from%>">

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

