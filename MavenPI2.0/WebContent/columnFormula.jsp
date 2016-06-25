
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,utils.db.*,java.sql.*,prg.db.PbReturnObject" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);


                    String dbType = null;
                    if (session != null && session.getAttribute("MetadataDbType") != null) {
                        dbType = (String) session.getAttribute("MetadataDbType");
                    }
                    String group_id = request.getParameter("grpId");
                    String[] grpDetails= group_id.split(",");
                    String connId = request.getParameter("connId");
                    // ////////////////////////////////////////////////////////////////////.println.println("gprId===" + group_id + "connId-" + connId);
                    PbDb pbdb = new PbDb();
                    PbReturnObject columnDetailsObject=null;
                     PbReturnObject factDetailsObject=null;
                     if ( ProgenConnection.SQL_SERVER.equals(dbType) ){

                          String factsQuery = "select buss_table_id, buss_table_name,db_table_id from  prg_grp_buss_table where buss_table_id in "
                            + "(select buss_table_id from prg_grp_buss_table where grp_id=" + grpDetails[2] + " and buss_type!='Query' "
                            + "except SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id="
                            + " gd.dim_id and gd.grp_id=" + grpDetails[2] + ") and buss_table_name!='Calculated Facts' AND BUSS_TABLE_ID ="+grpDetails[0]+"";
                    ////////////////////////////////////////////////////////////////////.println.println("factsQuery--" + factsQuery);
                     factDetailsObject = pbdb.execSelectSQL(factsQuery);
                    ////////////////////////////////////////////////////////////////////.println.println("pbro2----" + pbro2.getRowCount());
                    String query = "select column_name,column_type,actual_col_formula,buss_table_id,buss_column_id,column_disp_name from prg_grp_buss_table_details where buss_table_id in (select buss_table_id from  prg_grp_buss_table where buss_table_id in "
                            + "(select buss_table_id from prg_grp_buss_table where grp_id=" + grpDetails[2] + "  and buss_type!='Query' "
                            + "except SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id="
                            + " gd.dim_id and gd.grp_id=" + grpDetails[2] + ") ) order by column_name";

                   //
                columnDetailsObject = pbdb.execSelectSQL(query);

                    }else if(ProgenConnection.MYSQL.equals(dbType)){
                        String factsQuery = "select buss_table_id, buss_table_name,db_table_id from  prg_grp_buss_table where buss_table_id in "
                            + "(select buss_table_id from prg_grp_buss_table where grp_id=" + grpDetails[2] + " and buss_type!='Query' "
                            + "not in (SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id="
                            + " gd.dim_id and gd.grp_id=" + grpDetails[2] + ")) and buss_table_name!='Calculated Facts' AND BUSS_TABLE_ID ="+grpDetails[0]+"";
                    ////////////////////////////////////////////////////////////////////.println.println("factsQuery--" + factsQuery);
                     factDetailsObject = pbdb.execSelectSQL(factsQuery);

                    String query = "select column_name,column_type,actual_col_formula,buss_table_id,buss_column_id,column_disp_name from prg_grp_buss_table_details where buss_table_id in (select buss_table_id from  prg_grp_buss_table where buss_table_id in "
                            + "(select buss_table_id from prg_grp_buss_table where grp_id=" + grpDetails[2] + "  and buss_type!='Query' "
                            + "not in (SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id="
                            + " gd.dim_id and gd.grp_id=" + grpDetails[2] + ") )) order by column_name";


                    columnDetailsObject = pbdb.execSelectSQL(query);



                    }else{
                     String factsQuery = "select buss_table_id, buss_table_name,db_table_id from  prg_grp_buss_table where buss_table_id in "
                            + "(select buss_table_id from prg_grp_buss_table where grp_id=" + grpDetails[2] + " and buss_type!='Query' "
                            + "minus SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id="
                            + " gd.dim_id and gd.grp_id=" + grpDetails[2] + ") and buss_table_name!='Calculated Facts' AND BUSS_TABLE_ID ="+grpDetails[0]+"";
                    ////////////////////////////////////////////////////////////////////.println.println("factsQuery--" + factsQuery);
                     factDetailsObject = pbdb.execSelectSQL(factsQuery);

                    String query = "select column_name,column_type,actual_col_formula,buss_table_id,buss_column_id,column_disp_name from prg_grp_buss_table_details where buss_table_id in (select buss_table_id from  prg_grp_buss_table where buss_table_id in "
                            + "(select buss_table_id from prg_grp_buss_table where grp_id=" + grpDetails[2] + "  and buss_type!='Query' "
                            + "minus SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id="
                            + " gd.dim_id and gd.grp_id=" + grpDetails[2] + ") ) order by column_name";


                    columnDetailsObject = pbdb.execSelectSQL(query);


                    }

                    String sql = "select buss_table_id from prg_grp_buss_table where grp_id=" + grpDetails[2] + " AND buss_table_name='Calculated Facts'";
                    PbReturnObject pbrosql = pbdb.execSelectSQL(sql);
                    PbReturnObject pbroFormulaCols = null;
                    if (pbrosql.getRowCount() > 0) {
                        sql = "select column_name,column_type,actual_col_formula,buss_table_id,buss_column_id,REFFERED_ELEMENTS from prg_grp_buss_table_details "
                                + "  where buss_table_id in(" + pbrosql.getFieldValueInt(0, 0) + ")";
                        //pbroFormulaCols = pbdb.execSelectSQL(sql);
                    }
                    // ////////////////////////////////////////////////////////////////////.println.println("pbro1----" + pbro1.getRowCount());
                    String contextPath=request.getContextPath();
        %>
<html>
    <head>


        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/qdFormulaHelper.js" type="text/javascript"></script>
        <style type="text/css">
	#formulaUl { list-style-type: none; margin: 0; padding: 0; }
	#formulaUl li { margin: 3px 3px 3px 0; padding: 1px; float: left; width: 200px; height: 20px; font-size:.9em; text-align: center; }
	</style>


       
        <title>JSP Page</title>
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

        
        <form id="f1" name="myForm" method="post" action="">
            <%-- <input type="hidden" name="tableId" id="tableId" value="<%=table_id%>">--%>
            <input type="hidden" name="groupId" id="groupId" value="<%=grpDetails[2]%>">
            <input type="hidden" name="connId" id="connId" value="<%=connId%>">
            <div id="formulamainDiv">
                <table id="formulaMainTab" width="100%">
                    <tr><td> <table style="width:100%">
                            <tr style="width:100%" align="center">
                                <td align="left">
                                    <label class="label"><b>Name:</b></label>
                                    <input type="text" name="colName" id="colName" value="">
<!--                                </td>
                                <td align="left">-->
                                    <select name="agretype" id="agretype">
                                        <option value="sum">-select-</option>
                                        <option value="avg">avg</option>
                                        <option value="sum">sum</option>
                                        <option value="min">min</option>
                                        <option value="max">max</option>
                                        <option value="count">count</option>
                                        <option value="CountDistinct">CountDistinct</option>
                                    </select>
                                </td>  <td align="right"><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveFormula('<%=connId%>')"></td>
                            </tr>
                        </table></td></tr>
                    <tr><td><div style="width:97%;height:140px;background-color:#e6e6e6;color:#369;border:1px solid #A6C9E2;overflow:auto" id="txt2" onkeyup="addtoothervals()" >
                                <ul id="formulaUl" style="width:500px; height: 140px;"></ul>
                            </div></td></tr>
                    <tr><td><table><tr><td width="50%">
                                    <div style="height:200px;overflow:auto;border:1px solid #A6C9E2">
                                        <ul id="myList3" class="filetree treeview-famfamfam">
                                            <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">

                                                <%for (int j = 0; j < factDetailsObject.getRowCount(); j++) {%>
                                                <ul>
                                                    <li class="closed"> <img alt="" src='icons pinvoke/table.png'/>
                                                        <span ><font size="1px" face="verdana"><%=factDetailsObject.getFieldValueString(j, 1)%></font></span>
                                                        <ul>

                                                            <%
                                                                for (int i = 0; i < columnDetailsObject.getRowCount(); i++) {
                                                                    //////////////////////////////////////////////////////////////////////.println.println("----" + pbro1.getFieldValueInt(i, 3) + "---" + pbro2.getFieldValueInt(j, 0));
                                                                    if (String.valueOf(columnDetailsObject.getFieldValueInt(i, 3)).equalsIgnoreCase(String.valueOf(factDetailsObject.getFieldValueInt(j, 0)))) {

                                                                        //  if (pbro1.getFieldValueString(i, 1).equalsIgnoreCase("calculated") != true && pbro1.getFieldValueString(i, 1).equalsIgnoreCase("summarised") != true ) {
                                                            %>

                                                            <li>
                                                                <%
                                                                String tempMesType="";
                                                                if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("NUMBER")) {
                                                                tempMesType="NUMBER";
                                                                %>

                                                                <label class="label" style="color:green"><b>N</b></label>
                                                                <%} if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("numeric")) {
                                                                tempMesType="numeric";
                                                                %>

                                                                <label class="label" style="color:green"><b>N</b></label>
                                                                <%}else if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("VARCHAR2")) {
                                                                     tempMesType="VARCHAR2";
                                                                            %>

                                                                <label class="label" style="color:green"><b>T</b></label>
                                                                <%} else if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("VARCHAR")) {
                                                                     tempMesType="VARCHAR";
                                                                            %>

                                                                <label class="label" style="color:green"><b>T</b></label>
                                                                <%} else if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("DATE")) {
                                                                      tempMesType="DATE";
                                                                        %>
                                                                <label class="label" style="color:green"><b>D</b></label>
                                                                <%} else if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("CALCULATED")) {
                                                                   tempMesType="CALCULATED"; %>
                                                                <label class="label" style="color:green"><b>∑ C</b></label>
                                                                <%} else if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("SUMMARISED")) {
                                                                    tempMesType="SUMMARISED";%>
                                                                <label class="label" style="color:green"><b>∑ S</b></label>
                                                                <%} else if (columnDetailsObject.getFieldValueString(i, 1).equalsIgnoreCase("SUMMARIZED")) {
                                                                    tempMesType="SUMMARIZED";%>
                                                                <label class="label" style="color:green"><b>∑ S</b></label>
                                                                <%}%>
                                                                <a href="javascript:void(0)" style="font-family:verdana;font-size:12px;text-decoration:none;color:#369" id="<%=columnDetailsObject.getFieldValueString(i, 0)%>" onclick="addValue('<%=factDetailsObject.getFieldValueString(j,1)%>','<%=factDetailsObject.getFieldValueString(j,1)+"."+columnDetailsObject.getFieldValueString(i,0) %>','<%=columnDetailsObject.getFieldValueString(i, 5)%>','~<%=columnDetailsObject.getFieldValueString(i, 4)%>','Query','<%=tempMesType%>')"><%=columnDetailsObject.getFieldValueString(i,0)%></a></li>



                                                            <%--               <%  else {%>

                                                            <li>
                                                                <%if(pbro1.getFieldValueString(i, 1).equalsIgnoreCase("CALCULATED")){%>
                                                                 <label class="label" style="color:green"><b>C</b></label>
                                                                <%}else if(pbro1.getFieldValueString(i, 1).equalsIgnoreCase("SUMMARISED")){%>
                                                                 <label class="label" style="color:green"><b>S</b></label>
                                                                <%}%>

                                                                <a href="javascript:void(0)" style="font-family:verdana;font-size:12px;text-decoration:none;color:#369" id="<%=pbro1.getFieldValueString(i, 0)%>" onclick="addValue('<%=pbro2.getFieldValueString(j, 1)%>.<%=pbro1.getFieldValueString(i, 2)%>','~<%=pbro1.getFieldValueString(i, 4)%>','Query')">{<%=pbro1.getFieldValueString(i, 0)%>}</a></li>

                                                            <%}%> --%>
                                                            <%       }
                                                                }%>


                                                        </ul>
                                                    </li>
                                                </ul>
                                                <%}%>

                                            </li>
                                        </ul>
                                    </div>
                                </td>
                                <td><div style="height:200px;overflow:auto;border: 1px solid #A6C9E2">
                        <table border="0" width="100%" align="center" cellspacing="5">
                            <tr style="width:100%" align="center">
                                <td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="+"  id="+" onclick="addValue('Operators','+','+','+','Operators','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="-"  id="-" onclick="addValue('Operators','-','-','-','Operators','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="*"  id="*" onclick="addValue('Operators','*','*','*','Operators','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="/"  id="/" onclick="addValue('Operators','/','/','/','Operators','Operators')"></td></tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="("  id="(" onclick="addValue('Operators','(','(','(','OpenOper','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value=")"   id=")" onclick="addValue('Operators',')',')',')','CloseOper','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="()"  id="( )" onclick="addValue('Operators','( )','( )','( )','SpecOper','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="="  id="=" onclick="addValue('Operators','=','=','=','Operators','Operators')"></td></tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="," onclick="addValue('Operators',',',',',',','Operators','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="case" onclick="addCase()"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Undo" value="Undo"  onclick="undoFun()"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Redo" value="Redo"  onclick="redoFun()" ></td></tr>
                                                <%--    <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="CountDistinct"  id="CountDistinct" onclick="addValue('Count(Distinct','Count(Distinct','operands')"></td></tr>--%>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="nvl"  id="nvl" onclick="addValue('Operators','nvl(','nvl(','nvl(','operands','Operators')"></td></tr>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="sum"  id="-" onclick="addValue('Operators','sum(','sum(','sum(','operands','Operators')"></td></tr>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="avg"  id="avg" onclick="addValue('Operators','avg(','avg(','avg(','operands','Operators')"></td></tr>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="count"  id="count" onclick="addValue('Operators','count(','count(','count(','operands','Operators','Operators')"></td></tr>
                                    </table>
                                </td>
                            </tr>
                            <tr align="center" style="width:100%;"><td></td>
                                <td><input type="button" class="navtitle-hover" style="width:auto" value="CountDistinct"  id="CountDistinct" onclick="addValue('Operators','Count(Distinct','Count(Distinct','Count(Distinct','Count(Distinct','operands','Operators')"></td>

                            </tr>
                        </table></div>
                    </td></tr></table></td>
                    </tr>
                    <tr><td><table align="center" width="100%" border="0">
                <tr align="center">
                    <td align="center"><center><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveFormula('<%=connId%>')"></center></td>
                </tr>
            </table></td></tr>

                </table>
            </div>

                <textarea id="tArea" rows="" cols="" name="tArea" style="display:none"> </textarea>
                <textarea id="tArea1" rows="" cols="" name="tArea1" style="display:none"> </textarea>
        </form>
        <div id="dialog" title="Cases" style="display:none;"><center>
                <table>
                    <tr>
                        <td><label class="label" >When</label> &nbsp; <input type="text" id="when"   name="when"  onfocus="focusedElement('when')">

                            <input type="hidden" id="when1" name="when1" onfocus="focusedElement('when1')">
                        </td>
                    </tr>
                    <tr>
                        <td><label class="label" >Then</label> &nbsp;
                            <input type="text" id="then" name="then"  onfocus="focusedElement('then')">
                            <input type="hidden" id="then1" name="then1" onfocus="focusedElement('then1')">
                        </td>
                    </tr>
                    <tr>
                        <td><label class="label" >Else</label> &nbsp;
                            <input type="text" id="else" name="else"   onfocus="focusedElement('else')">
                            <input type="hidden" id="else1" name="else1" onfocus="focusedElement('else1')">
                        </td>
                    </tr>
                </table>
                <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCase()">
                <input type="button" class="navtitle-hover" style="width:auto" value="Clear" onclick="clearCase()">
            </center>
            <br>
        </div>
                 <script type="text/javascript">
             var startIndex;
             var endIndex;
            $(function() {
		$( "#formulaUl" ).sortable({
                    connectWith: ".sortable",
                             start: function (e, ui) {
                                 startIndex=ui.item.index();

//                                  alert("1\t"+ui.item.html())
//                                alert("2\t"+ui.item.index())
                        },
                        update: function(event, ui) {
//                            alert(ui.item.html())
//                            alert("3\t"+ui.item.index())
                            endIndex=ui.item.index()
                            rebuildformula()
                          }
//                        stop: function(event, ui) {
//                            alert("4\t"+ui.item.html())
//                            alert("5\t"+ui.item.index())
////                            if (position_updated) {
////                                processSortWithin(ui.item.attr("id"), ui.item.index());
////                                position_updated = false;
////                            }
//                        },
//                        receive: function(event, ui) {
//                            alert("1\t"+ui.item.attr("id"))
//                            alert("2\t"+ui.item.index())
//                            alert("3\t"+ui.sender.attr("id"))
//
//
//                        }


                })
		$( "#formulaUl" ).disableSelection();
                
           
//                $("#formulaUl").sortable({
//                        connectWith: ".sortable",
//                        update: function(event, ui) {
//                            position_updated = !ui.sender; //if no sender, set sortWithin flag to true
//                        },
//                        stop: function(event, ui) {
//                            if (position_updated) {
//                                processSortWithin(ui.item.attr("id"), ui.item.index());
//                                position_updated = false;
//                            }
//                        },
//                        receive: function(event, ui) {
//                            processSortBetween(ui.item.attr("id"), ui.item.index(),ui.sender.attr("id"));
//                        }
//                    }).disableSelection();

	});

          
            var formArray=new Array();// used for external formula
            var formArray1=new Array();// used for internal formula
            var prevClass=new Array();//used for storing prevClassType
           Array.prototype.remove = function(v) {
                return $.grep(this, function(e) {
                    return e !== v;
                });
            };

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
            var output='';
            var focussed1='';
            var redeoArray=new Array
            var tableNames=new Array
            var actFromula=new Array
            var measures=new Array
            var operators=new Array
            var measureTypes=new Array
            var displayFormula=new Array
            function focusedElement(element)
            {
                focussed = element;
                focussed1=element+"1";
                //alert('focussed is '+element)
            }
            
//            function addValue(str,str2,classType){
//                var htmlLi="<li class='ui-state-default' style='width:100px'>"+str+"</li>"
//                $("#formulaUl").append(htmlLi)
//                htmlLi=""
//
//            }

             function rebuildformula(){
//                 alert("actFromula1\t"+actFromula)
//                 alert("displayFormula1\t"+displayFormula)
                 var tempvar;
              tempvar=actFromula[startIndex]
              actFromula[startIndex]=actFromula[endIndex]
              actFromula[endIndex]=tempvar
              tempvar=displayFormula[startIndex]
              displayFormula[startIndex]=displayFormula[endIndex]
              displayFormula[endIndex]=tempvar
             
//                 alert("actFromula2\t"+actFromula)
//                 alert("displayFormula2\t"+displayFormula)
//                 alert("displayFormula\t"+measures)

             }
            function addValue(tablename,columnNamewithTablename,id,id2,classType,measurTypes){
//                alert("columnNamewithTablename\t"+columnNamewithTablename)
                 actFromula.push(columnNamewithTablename)
                 displayFormula.push(id)
//                 alert("actFromula\t"+actFromula)
                 if(classType!="Operators"){         
                tableNames.push(tablename)
                measures.push(id)
                measureTypes.push(measurTypes)
                }
                if(classType=="Operators")
                operators.push(columnNamewithTablename)
                var htmlLi=""
                if(columnNamewithTablename.length >10)
                htmlLi="<li class='ui-state-default' style='width:200px'>"+columnNamewithTablename+"</li>"
                else if(columnNamewithTablename.length >1 && columnNamewithTablename.length < 10 )
                htmlLi="<li class='ui-state-default' style='width:30px'>"+columnNamewithTablename+"</li>"
                else
                   htmlLi="<li class='ui-state-default' style='width:60px'>"+columnNamewithTablename+"</li>"
                $("#formulaUl").append(htmlLi)
                htmlLi=""
            }
//            function addValue(str,str2,classType)
//            {
//                if(classType=='Query'){
//                    ids+=str2;
//                    document.forms.f1.tArea1.value =ids;
//                }
//                /* if(classType=='Query'){
//                     if(document.forms.f1.tArea.value!=""){
//                         var tareaval= document.forms.f1.tArea.value.substring(1);
//                         var tareavallist=tareaval.split(",");
//                         for(var i=0;i<tareavallist.length;i++){
//                             if(str2.split(".")[0]==tareavallist[i]){
//
//                             }else{
//
//                                  document.forms.f1.tArea.value +=","+str2.split(".")[0];
//                             }
//
//                         }
//
//                     }else{
//                  document.forms.f1.tArea.value +=","+str2.split(".")[0];
//                     }
//
//                }*/
//                if(caseWindowStatus == '1')
//                {
//
//                    document.getElementById(focussed).value = document.getElementById(focussed).value+str;
//                    document.getElementById(focussed1).value= document.getElementById(focussed1).value+str2;
//                    ids+=str2;
//                    document.forms.f1.tArea1.value =ids;
//                    return;
//                }
//                // alert(document.forms.f1.txt2.value=='')//storing external formula
//                if(document.forms.f1.txt2.value=='')
//                {
//                    // alert('in if')
//                    if(classType=='Operators' || classType=='CloseOper' || classType=='SpecOper')
//                    {
//                        //  alert('Please select a column')
//                        prevStr=null;
//                        prevClassType=null;
//                        // return false;
//                        return true;
//                    }
//                    else{
//                        if(checkExpectedClassType(prevClassType,classType)){
//                            formula=document.forms.f1.txt2.value;
//                            if(str=='( )'){
//                                formArray[arrIndex]=formula;
//                                formula='('+formula+')'
//                                document.forms.f1.txt2.value =formula;
//                                arrIndexLength=formArray.length;
//                                arrIndex++;
//                                addTextArea(str2);
//                                prevStr=str;
//                                prevClassType=classType;
//                                prevClass[prevIndex]=prevClassType;
//                                prevIndex++;
//
//                            }
//                            else{
//                                formArray[arrIndex]=formula;
//                                formula=formula+str;
//                                document.forms.f1.txt2.value = formula;
//                                arrIndexLength=formArray.length;
//                                arrIndex++;
//                                addTextArea(str2);
//                                prevStr=str;
//                                //alert("prevStr-----"+prevStr)
//                                prevClassType=classType;
//                                //alert("prevClassType----"+prevClassType)
//                                prevClass[prevIndex]=prevClassType;
//                                //alert("prevClass[prevIndex]---------"+prevClass[prevIndex])
//                                prevIndex++;
//                            }
//                            return true;
//                        }
//                    }
//                }
//
//                else if(!document.forms.f1.txt2.value==''){
//                    if(checkExpectedClassType(prevClassType,classType)){
//                        // alert("checking")
//                        formula=document.forms.f1.txt2.value;
//                        //alert("formula"+formula)
//                        if(str=='( )'){
//                            formArray[arrIndex]=formula;
//                            formula='('+formula+')'
//                            document.forms.f1.txt2.value =formula;
//                            arrIndexLength=formArray.length;
//                            arrIndex++;
//                            addTextArea(str2);
//                            prevStr=str;
//                            prevClassType=classType;
//                            prevClass[prevIndex]=prevClassType;
//                            prevIndex++;
//                        }
//                        else{
//                            formArray[arrIndex]=formula;
//                            formula=formula+str;
//                            document.forms.f1.txt2.value = formula;
//                            arrIndexLength=formArray.length;
//                            arrIndex++;
//                            addTextArea(str2);
//                            prevStr=str;
//                            //alert("prevStr-----"+prevStr)
//                            prevClassType=classType;
//                            // alert("prevClassType----"+prevClassType)
//                            prevClass[prevIndex]=prevClassType;
//                            //alert("prevClass[prevIndex]----"+prevClass[prevIndex])
//                            prevIndex++;
//                        }
//                        return true;
//                    }
//                }
//
//            }
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
//                alert("last LI\t"+ $('#formulaUl li:last'))
                redeoArray.push( $('#formulaUl li:last'))
//                alert(" $('#formulaUl li:last')\t"+ $('#formulaUl li:last').html())

                        var index= $.inArray($('#formulaUl li:last').html(), actFromula);
                        if($.inArray($('#formulaUl li:last').html(), actFromula)!=-1)
                           {
                             actFromula.splice(index,1);
                             tableNames.splice(index,1);
                             measureTypes.splice(index,1);
                             measures.splice(index,1);
                           }



                
                $('#formulaUl li:last').remove();


            }
            function redoFun(){
                $('#formulaUl').append(redeoArray.pop())
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
                        if(classType1=='Operators'|| classType1=='OpenOper'|| classType1=='CloseOper' || classType1=='SpecOper' || classType1=='operands' )
                            return true;
                        else{
                            // alert('Please select an Operator')
                            // return false;
                            return true;
                        }

                    }
                    if(prevClassType1=='Operators'){

                        if(classType1=='Query' || classType1=='Numerics' || classType1=='OpenOper' || classType1=='operands')
                            return true;
                        else{
                            // alert('Please select column')
                            // return false;
                            return true;
                        }
                    }


                    if(prevClassType1=='operands'){

                        if(classType1=='Query' || classType1=='Numerics' || classType1=='OpenOper')
                            return true;
                        else{
                            // alert('Please select column')
                            // return false;
                            return true;
                        }
                    }
                    if(prevClassType1=='Numerics'){
                        if(classType1=='Operators'|| classType1=='OpenOper' || classType1=='CloseOper' || classType1=='SpecOper')
                            return true;
                        else{
                            // alert('Please select an Operator')
                            //return false;
                            return true;
                        }
                    }
                    if(prevClassType1=='OpenOper'){
                        if(classType1=='Query' || classType1=='Numerics'|| classType1=='OpenOper' ||classType1=='operands')
                            return true;
                        else{
                            // alert('Please select column')
                            //return false;
                            return true;
                        }
                    }
                    if(prevClassType1=='CloseOper'){
                        if(classType1=='Operators' || classType1=='SpecOper' || classType1=='CloseOper')
                            return true;
                        else{
                            //  alert('Please select an Operator')
                            //return false;
                            return true;
                        }
                    }
                    if(prevClassType1=='SpecOper'){
                        if(classType1=='Operators')
                            return true;
                        else{
                            //  alert('Please select an Operator')
                            // return false;
                            return true;
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

            function saveFormula(connId)
            {
//                alert("1")
               var measureColumnType=formulaStatus(tableNames,actFromula,measures,operators,measureTypes)
                 var jsonVar=eval('('+measureColumnType+')')
//                alert("2status\t"+jsonVar.status)
//                alert("measureType\t"+jsonVar.measureType)
                var formulaName=$("#colName").val()
                var bussTableID='<%=grpDetails[0]%>'
                if($.trim(formulaName)!="")
                {
                  $.ajax({url: 'businessgroupeditaction.do?groupdetails=validateFormula&connId='+connId+'&tableNames='+encodeURIComponent(tableNames)+'&actFromula='+encodeURIComponent(actFromula)+'&measures='+encodeURIComponent(measures)+'&operators='+encodeURIComponent(operators)+'&measureTypes='+encodeURIComponent(measureTypes)+'&measureColumnType='+encodeURIComponent(measureColumnType)+'&aggregationType='+encodeURIComponent($("#agretype").val())+'&formulaName='+encodeURIComponent(formulaName)+'&displayFormula='+displayFormula+'&bussTableId='+bussTableID,
                              success: function(data)
                              {
                                  if(data=='true'){
                                   alert("Formula created successfully ")
                                   parent.$('#formulaDialog').dialog('close');
                                  }else{
                                      alert("Entered formula is invalid please contact system admin")
                                  }
                              }

                         })
                }else
                {
                     alert("Please enter formula name")
                }

//               alert("status\t"+status)
//                var query = document.getElementById('txt2').value;
//                var tarea = document.getElementById('tArea').value;
//                var tarea1 = document.getElementById('tArea1').value;
//                var tareaval="";
//                if(query.indexOf('+')>=0){
//                    query=query.replace("+","@","gi");
//                }
//
//                if(tarea.indexOf('+')>=0){
//                    tarea=tarea.replace("+","@","gi");
//                }
//
//                if(document.getElementById("colName").value==""){
//                    alert('Please enter Formula Name')
//                }else if(query==""){
//                    alert('Please enter Formula')
//                }
//                // else if(document.getElementById("agretype").value()==""){
//                //     alert('Please select Aggregation type')
//                // }
//                else{
//
//
//                    //var groupId = document.getElementById('groupId').value;
//                    //alert('checkQuery?query='+query+'&groupId='+groupId+'&tArea='+tarea+'&tArea1='+tarea1+'&connId='+connId)
//                    //$.ajax({
//                    //    url: 'checkQuery?query='+query+'&groupId='+groupId+'&tArea='+tarea+'&tArea1='+tarea1+'&connId='+connId,
//                    //    success: function(data) {
//                    //        if(data=='Correct'){
//                    document.getElementById('txt2').value=query;
//                    document.getElementById('tArea').value=tarea;
//                    $.post( "addColumnFormula.do",$("#f1").serialize(),function(data){
//
//                    })
////                    document.myForm.action = "addColumnFormula.do";
////                    document.myForm.submit();
//                    parent.cancelFormula();
//                    //      }
//                    //     else
//                    //      {
//                    //  alert(data);
//                    //      }
//                    //  }
//                    //});
                }

//            }

            function cancelFormula()
            {

                parent.cancelFormula();
            }

            function addCase()
            {
//                alert(document.getElementById("dialog").style.display);
                // document.getElementById("dialog").style.display = 'block';
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

            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow"
                    //persist: "cookie"
                });
                //addeb by bharathi reddy fro search option
                $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
                })

            });

        </script>
    </body>
</html>
