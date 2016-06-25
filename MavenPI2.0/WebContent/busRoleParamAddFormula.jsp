<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb,java.sql.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>
<html>
<head>


    <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
    <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
    <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>

    <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>
    <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
     <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
    <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
    <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />

    <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
    <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

    <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />


   


    <style type="text/css">
        a {font-family:verdana;font-size:10px;cursor:pointer}
        a:hover {text-decoration: none; color: #ff9900; font-weight:bold;}
        /* a:link {color:blue;}
        a:visited {color: #660066;}

        a:active {color: #ff0000;text-decoration: none}*/
        .label{
            font-family:verdana;
            font-size:12px;
            font-weight:normal;
        }
    </style>
</head>
<body>

<%
        PbDb pbdb = new PbDb();
        String folderIds = request.getParameter("foldertabId");
        String Query = "SELECT distinct t.disp_name,t.sub_folder_id,t.sub_folder_tab_id,t.buss_table_id FROM PRG_USER_SUB_FOLDER_ELEMENTS e, PRG_USER_SUB_FOLDER_TABLES t  where e.sub_folder_tab_id=" + folderIds + " AND " +
                " t.sub_folder_tab_id = e.sub_folder_tab_id and e.ref_element_id= e.element_id and t.disp_name not in('Time','Calculated Facts') order by t.disp_name  ";
        ////////////////////////////////////////////////////////////////.println.println("Query---" + Query);
//  String Query = "select DISTINCT nvl(disp_name,sub_folder_type), connection_id,folder_name,grp_id  from PRG_USER_ALL_INFO_DETAILS where sub_folder_id in (" + folderIds + ") and sub_folder_type='Facts'";
        String Query1 = "SELECT e.ELEMENT_ID, t.disp_name   ,nvl(e.USER_COL_NAME,e.USER_COL_DESC), e.user_col_type,nvl(BUSS_COL_NAME,user_col_name)  FROM ";
        Query1 += " PRG_USER_SUB_FOLDER_ELEMENTS e, PRG_USER_SUB_FOLDER_TABLES t  where e.sub_folder_tab_id=" + folderIds + " AND ";
        Query1 += " t.sub_folder_tab_id = e.sub_folder_tab_id and e.ref_element_id= e.element_id and t.disp_name!='Time' and USE_REPORT_FLAG='Y' ";
        Query1 += " order by nvl(e.USER_COL_NAME,e.USER_COL_DESC),ref_element_id, ref_element_type";
// String Query2 = "select DISTINCT folder_name  from prg_user_all_info_details where sub_folder_id in (" + folderIds + ")";
        String tableNameList = "";
        String connectionId = "";
        String grpId = "";
        String eleList = "";
        PbReturnObject pbro1 = pbdb.execSelectSQL(Query);
        PbReturnObject pbro2 = pbdb.execSelectSQL(Query1);

//  PbReturnObject pbro3 = pbdb.execSelectSQL(Query2);
        ////////////////////////////////////////////////////////////////.println.println("Query---" + Query);
        ////////////////////////////////////////////////////////////////.println.println("Query1---" + Query1);
//////////////////////////////////////////////////////////////////.println.println("Query2---"+Query2);
        String tableName = pbro1.getFieldValueString(0, 0);
        String factSubFolderId = String.valueOf(pbro1.getFieldValueInt(0, 1));
        String factSubFoldertabId= String.valueOf(pbro1.getFieldValueInt(0, 2));
        String factBussTableId= String.valueOf(pbro1.getFieldValueInt(0, 3));
        ////////////////////////////////////////////////////////////////.println.println("factSubFoldertabId---"+factSubFoldertabId);

        String dimsubFolderidQuery = "SELECT FOLDER_ID, SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where folder_id in(select folder_id from PRG_USER_FOLDER_DETAIL where sub_folder_id=" + factSubFolderId + ") and sub_folder_type='Dimensions'";
        PbReturnObject pbro6 = pbdb.execSelectSQL(dimsubFolderidQuery);
        String dimSubFolderId = String.valueOf(pbro6.getFieldValueInt(0, 1));
        String FolderId = String.valueOf(pbro6.getFieldValueInt(0, 0));
        String dimListSql = "SELECT DISTINCT DIM_ID, DIM_NAME,use_report_member FROM PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and USE_REPORT_MEMBER='Y'  and sub_folder_id=" + dimSubFolderId + "  and DIM_NAME!='Time' order by dim_name";
        ////////////////////////////////////////////////////////////////.println.println("dimListSql---" + dimListSql);
        PbReturnObject pbro4 = pbdb.execSelectSQL(dimListSql);
//String dimmemListSql="SELECT DISTINCT DIM_ID, DIM_NAME,use_report_member FROM PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and sub_folder_id="+dimSubFolderId+" order by dim_name";
// ////////////////////////////////////////////////////////////////.println.println("dimListSql---"+dimmemListSql);
// PbReturnObject pbro5 = pbdb.execSelectSQL(dimmemListSql);

%>
<form id="f1" name="myForm">

    <table align="center" width="100%" border="1" >
        <tr style="width:100%">
            <td style="width:100%">
                <table style="width:100%">
                    <tr style="width:100%" align="center">
                        <td width="10%" >
                            <label class="label"><b>Name</b></label>
                        </td>
                        <td width="70%">
                            <input type="text" name="columnName" id="columnName" size="40">
                        </td>


                    </tr>
                </table>
            </td>
        </tr>
        <tr style="width:100%">
            <td style="width:100%">
                <table style="width:100%" border="0px solid">
                    <tr style="width:100%">
                        <td width="50%" style="height:150px" valign="top">
                            <center><label class="label"><b>Display</b></label ><hr></center>
                            <textarea style="width:99%;height:100%;background-color:#e6e6e6;color:#369;border:0;" id="txt2" name="txt2"  onkeyup="addtoothervals()" cols="60" rows="1"></textarea>
                        </td>
                        <td width="50%">

                            <center><label class="label"><b>Columns</b></label ><hr></center>
                            <div style="height:200px;overflow-y:auto">
                                <ul id="myList3" class="filetree treeview-famfamfam">
                                    <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">


                                        <ul>
                                            <li class="closed"><img src='icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=tableName%></font></span>
                                                <ul>
                                                    <%for (int j = 0; j < pbro2.getRowCount(); j++) {
 if (pbro2.getFieldValueString(j, 3).equalsIgnoreCase("calculated") != true && pbro2.getFieldValueString(j, 3).equalsIgnoreCase("summarised") != true){
                                                    %>

                                                    <li>
                                                         <%if(pbro2.getFieldValueString(j, 3).equalsIgnoreCase("NUMBER")){%>
                                                                 <label class="label" style="color:green"><b>N</b></label>
                                                                <%}else if(pbro2.getFieldValueString(j, 3).equalsIgnoreCase("VARCHAR2")){%>
                                                                 <label class="label" style="color:green"><b>T</b></label>
                                                                <%}else if(pbro2.getFieldValueString(j, 3).equalsIgnoreCase("DATE")){%>
                                                                    <label class="label" style="color:green"><b>D</b></label>
                                                                <%}%>
                                                                <%if(pbro2.getFieldValueString(j, 3).equalsIgnoreCase("CALCULATED")){%>
                                                                 <label class="label" style="color:green"><b>C</b></label>
                                                                <%}else if(pbro2.getFieldValueString(j, 3).equalsIgnoreCase("SUMMARISED")){%>
                                                                 <label class="label" style="color:green"><b>S</b></label>
                                                                <%}%>

                                                        <a href="javascript:void(0)" id="<%=pbro2.getFieldValueString(j, 2)%>" onclick="addValue('<%=pbro2.getFieldValueString(j, 4)%>','~<%=pbro2.getFieldValueString(j, 0)%>','Query')"><%=pbro2.getFieldValueString(j, 2)%></a></li>

                                                    <%}
        }%>
                                                </ul>
                                            </li>
                                            <%for (int j = 0; j < pbro4.getRowCount(); j++) {
                                                  String dimmemListSql = "select distinct t.member_id,t.member_name,m.COL_ID,t.sub_folder_tab_id from PRG_USER_SUB_FOLDER_TABLES t,PRG_GRP_DIM_MEMBER_DETAILS m " +
            " where t.dim_id =" + pbro4.getFieldValueInt(j, 0) + " and t.sub_folder_id=" + dimSubFolderId + " and m.mem_id=t.member_id and m.col_type='KEY' and  t.USE_REPORT_DIM_MEMBER='Y'";
    ////////////////////////////////////////////////////////////////.println.println("dimListSql---" + dimmemListSql);
    PbReturnObject pbro5 = pbdb.execSelectSQL(dimmemListSql);
    if(pbro5.getRowCount()>0){

                                                %>
                                            <li class="closed"><img src='icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=pbro4.getFieldValueString(j, 1)%></font></span>
                                                <ul>
                                                    <%





    for (int j1 = 0; j1 < pbro5.getRowCount(); j1++) {
//add to below li to get memId on monday-<%=pbro5.getFieldValueString(j1, 0) additional 
    
                                                    %>

                                                    <li><img src='icons pinvoke/report.png'></img><a href="javascript:void(0)" id="<%=pbro5.getFieldValueString(j1, 0)%>" onclick="addValue('<%=pbro5.getFieldValueString(j1, 1)%>','~M-<%=pbro5.getFieldValueInt(j1, 3)%>-<%=pbro5.getFieldValueInt(j1, 2)%>-<%=pbro5.getFieldValueString(j1, 0)%>','Query')"><%=pbro5.getFieldValueString(j1, 1)%></a></li>

                                                    <%
    }%>
                                                </ul>
                                            </li>
                                            <%
        }
    }
    %>
                                        </ul>


                                    </li>
                                </ul>
                            </div>



                            <%--             <div style="height:200px;overflow-y:auto;overflow-x:hidden">
                                <ul id="myList3" class="filetree treeview-famfamfam">
                                    <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">

                                        <%for (int j = 0; j < pbro1.getRowCount(); j++) {%>
                                        <ul>
                                            <li class="closed"><img src='icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=pbro1.getFieldValueString(j, 0)%></font></span>
                                                <ul>

                                                    <%
    for (int i = 0; i < pbro2.getRowCount(); i++) {
        //////////////////////////////////////////////////////////////////.println.println("----" +pbro2.getFieldValueInt(i, 1) + "---" + pbro2.getFieldValueInt(j, 0));
        if (String.valueOf(pbro2.getFieldValueString(i, 1)).equalsIgnoreCase(String.valueOf(pbro1.getFieldValueString(j, 0)))) {

            if (pbro2.getFieldValueString(i, 3).equalsIgnoreCase("calculated") != true) {
                                                    %>

                                                    <li> <a href="javascript:void(0)" style="font-family:verdana;font-size:12px;text-decoration:none;color:#369" id="<%=pbro1.getFieldValueString(j, 0)%>" onclick="addValue('<%=pbro1.getFieldValueString(j, 0)%>.<%=pbro2.getFieldValueString(i, 4)%>','~<%=pbro2.getFieldValueString(i, 0)%>','Query')"><%=pbro2.getFieldValueString(i, 2)%></a></li>

                                                    <%} else {%>

                                                    <li><a href="javascript:void(0)" style="font-family:verdana;font-size:12px;text-decoration:none;color:#369" id="<%=pbro1.getFieldValueString(j, 0)%>" onclick="addValue('<%=pbro1.getFieldValueString(j, 0)%>.<%=pbro2.getFieldValueString(i, 4)%>','~<%=pbro2.getFieldValueString(i, 0)%>','Query')">{<%=pbro2.getFieldValueString(i, 2)%>}</a></li>

                                                    <%}
        }
    }%>

                                                </ul>
                                            </li>
                                        </ul>
                                        <%}%>

                                    </li>
                                </ul>
                            </div> --%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

    <table style="width:100%" border="1px solid" align="center">
        <tr style="width:100%" align="center">
            <td width="100%" align="center" valign="top">
                <table border="0" width="100%" align="center" cellspacing="5">
                    <tr style="width:100%" align="center">
                        <td>
                            <table>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="+"  id="+" onclick="addValue('+','+','Operators')"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="-"  id="-" onclick="addValue('-','-','Operators')"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="*"  id="*" onclick="addValue('*','*','Operators')"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="/"  id="/" onclick="addValue('/','/','Operators')"></td></tr>
                            </table>
                        </td>
                        <td>
                            <table>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="("  id="(" onclick="addValue('( ','(','OpenOper')"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value=")"   id=")" onclick="addValue(' )',')','CloseOper')"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="()"  id="( )" onclick="addValue('( )','( )','SpecOper')"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="="  id="=" onclick="addValue('=','=','Operators')"></td></tr>
                            </table>
                        </td>
                        <td>
                            <table>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="," onclick="addValue(',',',','Operators')"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="case" onclick="addCase()"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Undo" value="Undo"  onclick="undoFun()"></td></tr>
                                <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Redo" value="Redo"  onclick="redoFun()" ></td></tr>
                            </table>
                        </td>
                        <td>
                            <table>
                                <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="nvl"  id="nvl" onclick="addValue('nvl(','nvl(','Operatorsfun')"></td></tr>
                                <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="sum"  id="-" onclick="addValue('SUM(','SUM(','Operatorsfun')"></td></tr>
                                <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="avg"  id="avg" onclick="addValue('AVG(','AVG(','Operatorsfun')"></td></tr>
                                <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="count"  id="count" onclick="addValue('COUNT(','COUNT(','Operatorsfun')"></td></tr>
                            </table>
                        </td>
                    </tr>
                    <tr align="center" style="width:100%;">
                        <td></td>
                        <td></td>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="CountDistinct"  id="CountDistinct" onclick="addValue('COUNT(DISTINCT','COUNT(DISTINCT','operands')"></td>
                        <td></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <br>
    <table align="center" width="100%" border="0">
        <tr align="center">
            <td align="center">
                <center>
                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveRoleFormula()">&nbsp;&nbsp;
                 <%--   <input type="button" class="navtitle-hover" style="width:auto" value="AddMore" onclick="saveAddMore()">&nbsp;&nbsp;--%>

            </center></td>
        </tr>
    </table>
    <textarea id="tArea" name="tArea" style="display:none"> </textarea>
    <textarea id="tArea1" name="tArea1" style="display:none"> </textarea>
    <input type="hidden" name="foldertabIds" id="foldertabIds" value="<%=folderIds%>">
    <input type="hidden" name="folderId" id="folderId" value="<%=FolderId%>">
    <input type="hidden" name="dimSubFolderId" id="dimSubFolderId" value="<%=dimSubFolderId%>">
    <input type="hidden" name="factSubFolderId" id="factSubFolderId" value="<%=factSubFolderId%>">
    <input type="hidden" name="iscalculate" id="iscalculate" >
    <input type="hidden" name="tableName" id="tableName" value="<%=tableName%>">
    <input type="hidden" name="factBussTableId" id="factBussTableId" value="<%=factBussTableId%>">
    <input type="hidden" name="factSubFoldertabId" id="factSubFoldertabId" value="<%=factSubFoldertabId%>">

  
</form>
<div id="dialog" title="Cases" style="display:none;"><center>
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

        <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCase()">
        <input type="button" class="navtitle-hover" style="width:auto" value="Clear" onclick="clearCase()">

    </form>

    </form>
</center>
<br>
</div>
<!--<div id="dialog" title="Cases" style="display:none;"><center>
                <form name="caseForm">
                    When &nbsp; <input type="text" id="when" name="when" onfocus="focusedElement('when')"><br>
                    Then &nbsp; <input type="text" id="then" name="then" onfocus="focusedElement('then')"><br>
                    Else &nbsp; <input type="text" id="else" name="else" onfocus="focusedElement('else')"><br><br>
            <input type="button" value="Save" onclick="saveCase()"></form></center>
</div>-->
 <script type="text/javascript">
        var formArray=new Array();// used for external formula
        var formArray1=new Array();// used for internal formula
        var prevClass=new Array();//used for storing prevClassType

        var formula='';
        var formula1='';

        var arrIndex=0;
        var arrIndex1=0;
        var prevIndex=0;
        var Flag=1;

        var arrIndexLength=0;
        var arrIndex1Length=0;
        var prevIndexlength=0;
        var prevClassType=null;
        var prevStr=null;
        var curStr=null;

        var caseStr = 'case when {        }'+"\n"+'then {       }'+"\n"+'else  {       }'+"\n"+'end';
        var caseWindowStatus = '0';
        var focussed='';
        var focussed1='';
        var output='';
        $(document).ready(function() {
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
                })

        });
        function focusedElement(element)
        {
            focussed = element;
            focussed1=element+"1";
            //alert('focussed is '+element)
        }
        var ids="";
        function addValue(str,str2,classType)
        {


            if(classType=='Query'){
                ids+=str2;
                document.forms.f1.tArea1.value =ids;
            }


            if(caseWindowStatus == '1')
            {

                document.getElementById(focussed).value = document.getElementById(focussed).value+str;
                document.getElementById(focussed1).value= document.getElementById(focussed1).value+str2;
                ids+=str2;
                document.forms.f1.tArea1.value =ids;
                return;
            }
            // alert(document.forms.f1.txt2.value=='')//storing external formula
            if(document.forms.f1.txt2.value=='')
            {
                // alert('in if')
                if(classType=='Operators' || classType=='CloseOper' || classType=='SpecOper')
                {
                    alert('Please select a column')
                    prevStr=null;
                    prevClassType=null;
                    return false;
                }

                else{
                    if(checkExpectedClassType(prevClassType,classType)){
                        formula=document.forms.f1.txt2.value;
                        if(str=='( )'){
                            formArray[arrIndex]=formula;
                            formula='('+formula+')'
                            document.forms.f1.txt2.value =formula;
                            arrIndexLength=formArray.length;
                            arrIndex++;
                            addTextArea(str2);
                            prevStr=str;
                            prevClassType=classType;
                            prevClass[prevIndex]=prevClassType;
                            prevIndex++;

                        }
                        else{
                            formArray[arrIndex]=formula;
                            formula=formula+str;
                            document.forms.f1.txt2.value = formula;
                            arrIndexLength=formArray.length;
                            arrIndex++;
                            addTextArea(str2);
                            prevStr=str;
                            //alert("prevStr-----"+prevStr)
                            prevClassType=classType;
                            //alert("prevClassType----"+prevClassType)
                            prevClass[prevIndex]=prevClassType;
                            //alert("prevClass[prevIndex]---------"+prevClass[prevIndex])
                            prevIndex++;
                        }
                        return true;
                    }
                }
            }

            else if(!document.forms.f1.txt2.value==''){
                if(checkExpectedClassType(prevClassType,classType)){
                    // alert("checking")
                    formula=document.forms.f1.txt2.value;
                    //alert("formula"+formula)
                    if(str=='( )'){
                        formArray[arrIndex]=formula;
                        formula='('+formula+')'
                        document.forms.f1.txt2.value =formula;
                        arrIndexLength=formArray.length;
                        arrIndex++;
                        addTextArea(str2);
                        prevStr=str;
                        prevClassType=classType;
                        prevClass[prevIndex]=prevClassType;
                        prevIndex++;
                    }
                    else{
                        formArray[arrIndex]=formula;
                        formula=formula+str;
                        document.forms.f1.txt2.value = formula;
                        arrIndexLength=formArray.length;
                        arrIndex++;
                        addTextArea(str2);
                        prevStr=str;
                        //alert("prevStr-----"+prevStr)
                        prevClassType=classType;
                        // alert("prevClassType----"+prevClassType)
                        prevClass[prevIndex]=prevClassType;
                        //alert("prevClass[prevIndex]----"+prevClass[prevIndex])
                        prevIndex++;
                    }
                    return true;
                }
            }

        }
        function addTextArea(str2){//storing internal formula
            formula1=document.forms.f1.tArea.value;

            if(str2=='( )'){
                formArray1[arrIndex1]=formula1;
                formula1='('+formula1+')'
                document.forms.f1.tArea.value =formula1;
                arrIndex1Length=formArray1.length;

                arrIndex1++;
            }
            else{
                formArray1[arrIndex1]=formula1;
                formula1=formula1+str2;
                document.forms.f1.tArea.value = formula1;
                arrIndex1Length=formArray1.length;

                arrIndex1++;
            }
            return true;
        }
        function undoFun(){
            if(arrIndexLength>0){
                arrIndexLength--;
                arrIndex1Length--;
                //  alert('inside arrIndexLength is '+arrIndexLength)
                //  alert(formArray[parseInt(arrIndexLength)]);
                //  alert(formArray1[parseInt(arrIndex1Length)]);
                document.forms.f1.txt2.value =formArray[parseInt(arrIndexLength)];
                document.forms.f1.tArea.value =formArray1[parseInt(arrIndex1Length)];
                //alert(document.forms.f1.tArea.value);
            }
            else{
                alert('nothing to undo')
            }
            if(prevIndex>0){
                prevIndex--;
                prevClassType=prevClass[parseInt(prevIndex-1)];
            }
            return (Flag);
        }
        function redoFun(){
            // alert(arrIndexLength+","+(formArray.length-1));
            if(arrIndexLength<(formArray.length-1)){
                //if(arrIndexLength){
                arrIndexLength++;
                arrIndex1Length++;
                document.forms.f1.txt2.value =formArray[parseInt(arrIndexLength)];
                document.forms.f1.tArea.value =formArray1[parseInt(arrIndex1Length)];

            }
            else{
                alert('nothing to redo')
            }
        }

        function Editname(){
            var Ename = document.forms.f1.txt1.value;
            Ename = Ename.replace(/[^a-zA-Z0-9 ]+/g,'');
            Ename = Ename.replace(/\s/g, "");
            document.getElementById('h').value=Ename;
            return Ename;
        }

        function addNumeric(classType3){
            if(checkExpectedClassType(prevClassType,classType3)){
                var r=document.forms.f1.Numer.value;
                if(Flag==1)
                {
                    formula=document.forms.f1.txt2.value;
                    formula1=document.forms.f1.tArea.value;
                }

                formArray1[arrIndex1]=formula1;
                formArray[arrIndex]=formula;

                formula=formula+r;
                formula1=formula1+r;

                document.forms.f1.txt2.value=formula;
                document.forms.f1.tArea.value=formula1;
                arrIndexLength=formArray.length;
                arrIndex1Length=formArray1.length;
                arrIndex1++;
                arrIndex++;
                prevIndex++;

                prevClassType=classType3;
            }

        }
        function clearboxes(){
            document.getElementById('txt2').value='';
            document.getElementById('tArea').value='';
            document.getElementById('Numer').value='';
            prevClassType=null;
        }
        function checksave(jspname){
            document.forms.f1.action=jspname;
            document.forms.f1.submit();
        }
        function val_fun(reportId,lastSeq,path){
            if(document.forms.f1.txt1.value==" ")
            {
                alert("Enter NewColumn Name")
                return false;
            }
            if(document.forms.f1.txt2.value==""){
                alert("Enter Formula")
                return false;
            }
            else
            {
                //alert('pbFormula_save.jsp?reportId='+reportId+"&lastSeq="+lastSeq+"&path="+path);
                checksave('pbFormula_save.jsp?reportId='+reportId+"&lastSeq="+lastSeq+"&path="+path);
            }
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
            }
        }

        function saveRoleFormula()
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
            //alert('userLayerAction.do?userParam=checkRoleFormulaQuery&query='+query+'&ConnectionId='+ConnectionId)
            // $.ajax({
            //    url: 'userLayerAction.do?userParam=checkRoleFormulaQuery&query='+query+'&ConnectionId='+ConnectionId,
            //   success: function(data) {

            // if(data=="Correct")
            // {
            if(document.getElementById("columnName").value==""){
                alert('Please enter Formula Name');
            }
            else{
                 var x=confirm('Are you want to calculate Prior,Change and Change%');
                var x=false;
                if(x==true){
                    document.getElementById("iscalculate").value="Y";
                }
                else{
                    document.getElementById("iscalculate").value="N";
                }
                var query = document.getElementById('txt2').value;

                if(query.indexOf('+')>=0){
                    document.getElementById('txt2').value=query.replace("+","@","gi");
                }

                var txt2val=document.getElementById("txt2").value;
                var folderTabIdval=document.getElementById("foldertabIds").value;
                var iscalculateval= document.getElementById("iscalculate").value;
                var columnNameval=document.getElementById("columnName").value;
                var dimSubFolderId=document.getElementById("dimSubFolderId").value;
                var factSubFolderId=document.getElementById("factSubFolderId").value;
                var folderId=document.getElementById("folderId").value;
                var tableName=document.getElementById("tableName").value;
                var factSubFoldertabId=document.getElementById("factSubFoldertabId").value;
                var factBussTableId=document.getElementById("factBussTableId").value;
              //  alert( 'userLayerAction.do?userParam=saveRoleParamFormula&factBussTableId='+factBussTableId+'&factSubFoldertabId='+factSubFoldertabId+'&tableName='+tableName+'&factSubFolderId='+factSubFolderId+'&dimSubFolderId='+dimSubFolderId+'&txt2='+txt2val+'&folderTabId='+folderTabIdval+'&iscalculate='+iscalculateval+'&tArea1='+tArea1val+'&tArea='+tareaval+'&columnName='+columnNameval+'&folderId='+folderId)

                $.ajax({
                    url: 'userLayerAction.do?userParam=saveRoleParamFormula&factBussTableId='+factBussTableId+'&factSubFoldertabId='+factSubFoldertabId+'&tableName='+tableName+'&factSubFolderId='+factSubFolderId+'&dimSubFolderId='+dimSubFolderId+'&txt2='+txt2val+'&folderTabId='+folderTabIdval+'&iscalculate='+iscalculateval+'&tArea1='+tArea1val+'&tArea='+tareaval+'&columnName='+columnNameval+'&folderId='+folderId,
                    success: function(data) {
                        // alert(data)
                        if(data=="Correct"){
                            parent.window.location.reload(true);
                        }
                    }
                });


            }
            //   }
            // else
            //  {
            //      alert(data);
            //  }


            //  }
            //  });



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
            // alert(document.getElementById('tArea').value);
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
            var val=document.getElementById("txt2").value;
            var val2=val.substring(val.length-1);
            var val3=document.getElementById("tArea").value;
            val3+=val2;
            document.getElementById("tArea").value=val3;

        }

    </script>
</body>
</html>
