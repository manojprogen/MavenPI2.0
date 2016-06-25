
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.datadisplay.db.PbDataDisplayBeanDb,prg.db.PbDb,prg.db.PbReturnObject,utils.db.ProgenConnection,utils.db.*,java.sql.*,prg.business.group.BusinessGroupDAO"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% String contextPath= request.getContextPath();%>
<html>
    <head>

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

<!--        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>-->

<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->


        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="confirm.css" rel="stylesheet" type="text/css" />
        <link href="jquery.contextMenu.css" rel="stylesheet" type="text/css" />

        <script src="jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="jquery.contextMenu.js" type="text/javascript"></script>
        <link href="myStyles.css" rel="stylesheet" type="text/css">
<!--              <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
       
        <style>
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#EAF2F7;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }

            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }




            .white_content1 {
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 700px;
                height:400px;
                padding: 16px;
                border: 0px white;
                background-color: white;
                z-index:1002;


            }
            .suggestLink { background-color: #FFFFFF;
                padding: 2px 6px 2px 6px; }
            .suggestLinkOver { background-color: #0099CC;
                padding: 2px 6px 2px 6px; }
            #suggestList { position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px;
                width: 160px; }

            #suggestList1 { position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px;
                width: 160px; }
            #wrapper { display: inline;}
            #country { width: 160px; }
            #country1 { width: 160px; }

            .suggestdiv{
                display:none;
                width:auto;
                height:auto;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
            }
        </style>

        <script>




        </script>
    </head>
    <body id="mainBody">
        <%String cpath = request.getContextPath();
 
        PbDb pbdb = new PbDb();
        String memId = request.getParameter("memId");
        String hieId = request.getParameter("hieId");
        String dimId = request.getParameter("dimId");

        ////////////////////////////////////////////////////////////////////////////////.println.println("memId---" + memId);
        //  //////////////////////////////////////////////////////////////////////////////.println.println("hieId---" + hieId);
        // //////////////////////////////////////////////////////////////////////////////.println.println("dimId---" + dimId);

        String connectionSql = "select Connection_id from prg_qry_dimensions where dimension_id=" + dimId;
        PbReturnObject pbro = pbdb.execSelectSQL(connectionSql);
        String connectionId = String.valueOf(pbro.getFieldValueInt(0, 0));
        // //////////////////////////////////////////////////////////////////////////////.println.println("connectionId---" + connectionId);
        String tabDetailsQuery = "SELECT m.TABLE_NAME,d.TABLE_COL_NAME,d.column_id, m.table_id,d.col_type  from prg_db_master_table m, prg_db_master_table_details d where m.table_id= d.table_id and d.column_id=";
        tabDetailsQuery += "(select col_id FROM PRG_QRY_DIM_MEMBER_DETAILS where mem_id=" + memId + " and COL_TYPE='VALUE')";
        //////////////////////////////////////////////////////////////////////////////.println.println("tabDetailsQuery---" + tabDetailsQuery);
        PbReturnObject tabDetailsPbro = pbdb.execSelectSQL(tabDetailsQuery);
        String tableName = tabDetailsPbro.getFieldValueString(0, 0);
        String colName = tabDetailsPbro.getFieldValueString(0, 1);
        String colId = tabDetailsPbro.getFieldValueString(0, 2);
        String tabId = tabDetailsPbro.getFieldValueString(0, 3);
        String colType = tabDetailsPbro.getFieldValueString(0, 4);


        //code to check parent is from same table
        String rellevelQuery = "select rel_level from prg_qry_dim_rel_details where mem_id=" + memId + "  and rel_id=" + hieId;
        PbReturnObject pbrorellevel = pbdb.execSelectSQL(rellevelQuery);
        int rellevel = pbrorellevel.getFieldValueInt(0, 0);
        String isNewTable = "N";
        String parentdimtabId = "";
        String parenttabName = "";
        String parentcolName = "";
        if (rellevel > 1) {
            String dimNewTabIdQuery = "select dim_tab_id  from prg_qry_dim_tables where tab_id=" + tabId + " and dim_id=" + dimId;
            //////////////////////////////////////////////////////////////////////////////.println.println("dimNewTabIdQuery=="+dimNewTabIdQuery);
            PbReturnObject pbrodimtab = pbdb.execSelectSQL(dimNewTabIdQuery);
            String dimtabId = String.valueOf(pbrodimtab.getFieldValueInt(0, 0));
            String parentdimTabIdQuery = "select dim_tab_id from prg_qry_dim_member where member_id in (select mem_id from prg_qry_dim_rel_details where rel_id=" + hieId + " and rel_level=" + (rellevel - 1) + ")";
            //////////////////////////////////////////////////////////////////////////////.println.println("parentdimTabIdQuery=="+parentdimTabIdQuery);
            PbReturnObject pbroparent = pbdb.execSelectSQL(parentdimTabIdQuery);
            parentdimtabId = String.valueOf(pbroparent.getFieldValueInt(0, 0));
            if (!dimtabId.equalsIgnoreCase(parentdimtabId)) {
                isNewTable = "Y";
            }

        }
        PbReturnObject parentvaluespbro = null;
        if (isNewTable.equalsIgnoreCase("Y")) {
            String parentTabNameQuery = "SELECT m.TABLE_NAME,d.TABLE_COL_NAME from prg_db_master_table m, prg_db_master_table_details d where m.table_id= d.table_id and d.column_id=";
            parentTabNameQuery += "(select col_id FROM PRG_QRY_DIM_MEMBER_DETAILS where mem_id in(select mem_id from prg_qry_dim_rel_details where rel_id=" + hieId + " and rel_level=" + (rellevel - 1) + ") and COL_TYPE='VALUE')";
            //String parentTabNameQuery = "select table_name from  prg_db_master_table where table_id in (select tab_id from prg_qry_dim_tables where dim_tab_id=" + parentdimtabId + ")";
            //////////////////////////////////////////////////////////////////////////////.println.println("parentTabNameQuery=="+parentTabNameQuery);
            PbReturnObject pbroparenttanMane = pbdb.execSelectSQL(parentTabNameQuery);
            parenttabName = pbroparenttanMane.getFieldValueString(0, 0);
            parentcolName = pbroparenttanMane.getFieldValueString(0, 1);
            Connection con = new BusinessGroupDAO().getConnectionIdConnection(connectionId);
            Statement st = con.createStatement();
            //////////////////////////////////////////////////////////////////////////////.println.println("select distinct "+parentcolName+" from "+parenttabName);
            ResultSet rs = st.executeQuery("select distinct " + parentcolName + " from " + parenttabName);

            parentvaluespbro = new PbReturnObject(rs);
            if (con != null) {
                con.close();
            }
        }
            /*   String parentGrpValQuery = "select distinct " + colName + "  from " + tableName;
                Connection con = new BusinessGroupDAO().getConnectionIdConnection(connectionId);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(parentGrpValQuery);
               PbReturnObject memdetPbro = new PbReturnObject(rs);
               if (con != null) {
                con.close();
            }
               int repeatCount=2;
               if(memdetPbro.getRowCount()<2){
                   repeatCount=memdetPbro.getRowCount();
                }
 * */
        %>

        <center>
            <form name="myForm" method="post">

                <table align="center" border="0">
                    <tr>
                        <td class="myhead" style="background-color:silver">
                            Group Name
                        </td>
                        <td>
                            <input type="text" name="grpName" id="grpName" onkeyup="gogrpDesc(this)" onchange="duplicateCheck(this,'<%=tabId%>','<%=cpath%>','<%=connectionId%>','<%=isNewTable%>','<%=tableName%>')">
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead" style="background-color:silver">
                            Group Desc
                        </td>
                        <td>
                            <input type="text" name="grpDesc" id="grpDesc" >
                        </td>
                    </tr>

                </table>
                <br>
                <br>
                <div id="headings" >
                    <table align="center" border="0" id="addTable">
                        <tr>
                            <%

        if (isNewTable.equalsIgnoreCase("Y")) {%>
                            <th class="prgtableheader" style="background-color:silver;">Parent Value</th>
                            <%}%>
                            <th class="prgtableheader" style="background-color:silver;">Group Value</th>
                            <th class="prgtableheader" style="background-color:silver">Key Value</th>

                        </tr>
                        <%for (int i = 0; i < 2; i++) {%>
                        <Tr>
                            <%if (isNewTable.equalsIgnoreCase("Y")) {
        if (parentvaluespbro.getRowCount() > 0) {%>
                            <td>
                                <select id="gParent[<%=i%>]" name="gParent[<%=i%>]" style="width:120px" class="myTextbox5" >
                                    <%for (int k = 0; k < parentvaluespbro.getRowCount(); k++) {%>
                                    <option value="<%=parentvaluespbro.getFieldValueString(k, 0)%>"><%=parentvaluespbro.getFieldValueString(k, 0)%></option>
                                    <% }%>
                                </select>
                            </td>
                            <%}
                            }%>
                            <td><input type="text"  id="parent[<%=i%>]" name="parent[<%=i%>]" class="myTextbox5" style="width:120px" maxlength="255"></td>
                            <td><%--add by sreekanth start--%>
                                <select id="selectName<%=i%>" name="selectName" onchange="createParentnew(this.id,'keyValue[<%=i%>]','keyValue[<%=i%>]suggestList1','<%=colName%>','<%=tableName%>','<%=connectionId%>')">
                                    <option >----Select-----</option>
                                    <option  value="Like"> Like </option>
                                    <option value="In">In</option>

                                </select> <div id="tbox"></div>
                            </td>
                            <Td>
                                <input type="text" class="myTextbox5" id="keyValue[<%=i%>]" name="keyValue[<%=i%>]"  autocomplete="off" onkeyup="reChange(event,'keyValue[<%=i%>]','keyValue[<%=i%>]suggestList1','<%=colName%>','<%=tableName%>','<%=connectionId%>')" >
                                <%-- <input type="text" class="myTextbox5" id="keyValue[<%=i%>]" name="keyValue[<%=i%>]"  autocomplete="off" onfocus="createParentnew('keyValue[<%=i%>]','keyValue[<%=i%>]suggestList1','<%=colName%>','<%=tableName%>','<%=connectionId%>')" > --%>
                                <%--add by sreekanth over--%>
                            </Td>
                        </Tr>
                        <Tr>
                            <% if (isNewTable.equalsIgnoreCase("Y")) {%>
                            <td></td>
                            <%}%>
                            <td>
                            </td>
                            <Td>
                                <div id="keyValue[<%=i%>]suggestList1" style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;'></div>
                            </Td>
                        </Tr>

                        <% }%>



                </table></div><br>
                <div id="buttons"><table><tr>
                            <td> <input type="button"  value="Save" class="navtitle-hover" onclick="saveGrp('addTable','<%=colName%>','<%=tableName%>','<%=connectionId%>','<%=isNewTable%>')"></td>
                            <td> <input type="button"   value="Cancel" class="navtitle-hover" onclick="cancelGrp()"></td>
                            <td>  <input type="button"  value="Add Row" class="navtitle-hover" onclick="addParentSingleRow('addTable','<%=colName%>','<%=tableName%>','<%=connectionId%>','<%=isNewTable%>')"/></td>
                            <td><input type="button"   value="Delete Row" class="navtitle-hover" onclick="deleteParentRow('addTable')" /></td>
                        </tr>
                    </table>
                </div>
                <input type="hidden" id="tabRowCount" name="tabRowCount" class="myTextbox5"  maxlength="255" >
                <input type="hidden" id="tableName" name="tableName" class="myTextbox5"  maxlength="255" value="<%=tableName%>">
                <input type="hidden" id="colName" name="colName" class="myTextbox5"  maxlength="255" value="<%=colName%>">
                 <input type="hidden" id="colType" name="colType" class="myTextbox5"  maxlength="255" value="<%=colType%>">
                <input type="hidden" id="tabId" name="tabId" class="myTextbox5"  maxlength="255" value="<%=tabId%>">
                <input type="hidden" id="colId" name="colId" class="myTextbox5"  maxlength="255" value="<%=colId%>">
                <input type="hidden" id="connectionId" name="connectionId" class="myTextbox5"  maxlength="255" value="<%=connectionId%>">
                <input type="hidden" id="memId" name="memId" class="myTextbox5"  maxlength="255" value="<%=memId%>">
                <input type="hidden" id="hieId" name="hieId" class="myTextbox5"  maxlength="255" value="<%=hieId%>">
                <input type="hidden" id="dimId" name="dimId" class="myTextbox5"  maxlength="255" value="<%=dimId%>">
                <input type="hidden" id="isNewTable" name="isNewTable" class="myTextbox5"  maxlength="255" value="<%=isNewTable%>">

                <input type="hidden" value="<%=cpath%>" id="h">
                <div>
                    <iframe  id="dataDispparentcols" NAME='dataDispparentcols'  class="white_content1" SRC='#'></iframe>
                </div>
                <div id="fade" class="black_overlay"></div>

            </form>

        </center>
 <script type="text/javascript">
            //newly added
              var oldValuesids=""
            var tevname="";
            var selIndexValue="";
            var extposition="";
            function gogrpDesc(name){
                //alert('desc')
                document.getElementById('grpDesc').value = name.value;
            }

            function addParentSingleRow(tableID,colName1,tableName1,connectionId1,isNew)
            {
                var table = document.getElementById(tableID);

                var rowCount = table.rows.length;
                totalRowCount = rowCount;
                var row = table.insertRow(rowCount);

                row.id = rowCount;
                if(isNew=="N"){
                    var cell1 = row.insertCell(0);
                    var element1 = document.createElement("input");
                    element1.type = "text";
                    extposition=(rowCount-1)/2;
                    element1.name = "parent["+extposition+"]";
                    element1.id = "parent["+extposition+"]";
                    element1.className="myTextbox5";
                    element1.style.width="120px";
                    cell1.appendChild(element1);
                    var obj=document.getElementById('keyValue[0]');
                    var cell2 = row.insertCell(1);


                    var Str="<select id=\"selectName"+extposition+"\" name=\"selectName\" onChange=\"createParentnew(this.id,'keyValue["+extposition+"]','keyValue["+extposition+"]suggestList1','"+colName1+"','"+tableName1+"','"+connectionId1+"')\">"+
                        "<option >----Select-----</option>"+
                        "<option  value=\"Like\"   autocomplete=\"off\" >Like </option>"+
                        "<option value=\"In\"    autocomplete=\"off\" >In</option>"+

                        "</select>";
                    cell2.innerHTML=Str;


                    /*
                     var ElementSelect = document.createElement("select");
                    ElementSelect.name ="selectName["+extposition+"]";
                    ElementSelect.id ="selectName["+extposition+"]";
                    ElementSelect.className ="myTextbox5";
                    ElementSelect.style.width ="120px";

                     *///add by sreekanth start
                    var cell3 = row.insertCell(2);
                    var lengthElement2 = document.createElement("input");
                    lengthElement2.type = "text";
                    lengthElement2.name = "keyValue["+extposition+"]";
                    lengthElement2.id = "keyValue["+extposition+"]";
                    lengthElement2.className="myTextbox5";
                    lengthElement2.style.width="140px";
                    lengthElement2.autocomplete="off";
                    lengthElement2.onkeyup=function(event){
                        reChange(event,'keyValue['+extposition+']','keyValue['+extposition+']suggestList1',colName1,tableName1,connectionId1);

                    }
                    cell3.appendChild(lengthElement2);
                    var rown = table.insertRow(rowCount+1);
                    var cell1n = rown.insertCell(0);

                    var cell2n = rown.insertCell(1);
                    var ajaxDiv = document.createElement('div');

                    ajaxDiv.id = "keyValue["+extposition+"]suggestList1";
                    ajaxDiv.style.display="none";
                    ajaxDiv.style.width="auto";
                    ajaxDiv.style.height="auto";
                    ajaxDiv.style.backgroundColor="#ffffff";
                    ajaxDiv.style.overflow="auto";
                    ajaxDiv.style.position="absolute";
                    ajaxDiv.style.border="1px";
                    ajaxDiv.style.textAlign="left";
                    ajaxDiv.style.border="1px solid #000000";
                    ajaxDiv.style.borderTop="0px";

                    cell2n.appendChild(ajaxDiv);
                }else{
                    var extposition=(rowCount-1)/2;

                    var cellnew = row.insertCell(0);
                    var lengthElement = document.createElement("select");
                    lengthElement.name = "gParent["+extposition+"]";
                    lengthElement.id = "gParent["+extposition+"]";
                    lengthElement.className="myTextbox5";
                    lengthElement.style.width="120px";
                    cellnew.appendChild(lengthElement);
                    var obj=document.getElementById('gParent[0]');
                    for(var selLen=0;selLen<obj.length;selLen++){
                        document.getElementById("gParent["+extposition+"]").options[selLen] = new Option(document.getElementById("gParent[0]").options[selLen].text,document.getElementById("gParent[0]").options[selLen].value);
                    }

                    ////add by sreekanth over
                    var cell1 = row.insertCell(1);
                    var element1 = document.createElement("input");
                    element1.type = "text";

                    element1.name = "parent["+extposition+"]";
                    element1.id = "parent["+extposition+"]";
                    element1.className="myTextbox5";
                    element1.style.width="120px";
                    cell1.appendChild(element1);
                    var obj=document.getElementById('keyValue[0]');
                    var cell2 = row.insertCell(2);
                    var lengthElement2 = document.createElement("input");
                    lengthElement2.type = "text";
                    lengthElement2.name = "keyValue["+extposition+"]";
                    lengthElement2.id = "keyValue["+extposition+"]";
                    lengthElement2.className="myTextbox5";
                    lengthElement2.style.width="140px";
                    lengthElement2.autocomplete="off";
                    lengthElement2.onfocus=function(){
                        createParentnew('keyValue['+extposition+']','keyValue['+extposition+']suggestList1',colName1,tableName1,connectionId1);
                    }
                    cell2.appendChild(lengthElement2);
                    var rown = table.insertRow(rowCount+1);
                    var cell1n = rown.insertCell(0);
                    var cell1n3 = rown.insertCell(1);
                    var cell2n = rown.insertCell(2);
                    var ajaxDiv = document.createElement('div');

                    ajaxDiv.id = "keyValue["+extposition+"]suggestList1";
                    ajaxDiv.style.display="none";
                    ajaxDiv.style.width="auto";
                    ajaxDiv.style.height="auto";
                    ajaxDiv.style.backgroundColor="#ffffff";
                    ajaxDiv.style.overflow="auto";
                    ajaxDiv.style.position="absolute";
                    ajaxDiv.style.border="1px";
                    ajaxDiv.style.textAlign="left";
                    ajaxDiv.style.border="1px solid #000000";
                    ajaxDiv.style.borderTop="0px";

                    cell2n.appendChild(ajaxDiv);

                }

            }

            function deleteParentRow(tableID) {
                try {
                    var table = document.getElementById(tableID);
                    var rowCount = table.rows.length;
                    //alert(rowCount+' in delete ')
                    if(rowCount > 2) {
                        table.deleteRow(rowCount - 1);
                    }

                }catch(e) {
                    alert(e);
                }
            }
            function cancelGrp()
            {
                parent.cancelGrp();
            }


            var tid;
            var did;
            var gSelectedIndex = -1;
            var preservedvalue="";
            var selectedItem="";
            /* key code constants */
            var ENTER = 13;
            var KEYUP = 38;
            var KEYDOWN = 40;
            var BACKSPACE = 8;
            var xmlHttp;
            var ctxPath;
            var tablename="";
            var colName="";
            var connectionId="";
            var nexrowId=0;

            window.onload = function()
            {
                //alert("Main");
                //document.getElementById("suggestList").style.display = "none";
                // document.getElementById("suggestList1").style.display = "none";
                //  document.myForm.onsubmit = function(){return false;};
            };

            function sample()
            {

                document.getElementById("keyValue[0]").style.display = "none";

            }
            document.onkeydown = function(){

                if(window.event && window.event.keyCode == 116)
                { // Capture and remap F5
                    window.event.keyCode = 505;
                }

                if(window.event && window.event.keyCode == 505)
                { // New action for F5
                    return false;
                    // Must return false or the browser will refresh anyway
                }
            }


            function selId(id1,id2,actcolName,tabName,conId)
            {

                tid=id1;
                did=id2;
                tablename=tabName;
                colName=actcolName;
                connectionId=conId;
                if(tid=='country')
                {
                    preservedvalue="";
                }
                //alert("values set are "+tid+ "  " +did)
                document.getElementById(did).style.display = "none";
                document.getElementById(tid).onkeyup = function(e)
                {
                    //alert("value is "+document.getElementById(tid).value);
                    if(document.getElementById(tid).value.indexOf('ALL')<0)
                    {
                        checkKey(e, this);

                        // checkKey(e, document.getElementById(tid));
                    }

                };

                // checkKey(e, document.getElementById(tid));
                //alert(1);
                document.onclick = checkClick;

            }
            function selId1(id1,id2,actcolName,tabName,conId,imgId)
            {

                tid=id1;
                did=id2;
                tablename=tabName;
                colName=actcolName;
                connectionId=conId;
                if(tid=='country')
                {
                    preservedvalue="";
                }
                //alert("values set are "+tid+ "  " +did)
                document.getElementById(did).style.display = "none";
                document.getElementById(tid).onclick = function(e)
                {
                    //alert("value is "+document.getElementById(tid).value);
                    if(document.getElementById(imgId).value.indexOf('ALL')<0)
                    {
                        checkKey(e, this);

                        checkKey(e, document.getElementById(tid));
                    }

                };

                // checkKey(e, document.getElementById(tid));
                //alert(1);
                document.onclick = checkClick;

            }


            function sendRequest(url, payload)
            {
                //alert("url is "+url+"payload is "+payload);
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }

                var mainUrl=url+"?"+payload;
                //alert("mainUrl "+mainUrl);
                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",mainUrl,true);
                xmlHttp.send(null);

            }


            function handleResponse1(response)
            {
                //alert("in handleResponse1");
                var suggestList = document.getElementById(did);

                suggestList.innerHTML = "";

                var names1=new Array();
                // var names = response.xhr.responseText.split("\n");
                var names = response.split("\n");
                //  alert("length "+names.length)
                // alert("total names "+names[j]+" "+j)names.length-1
                var tablenew = document.getElementById('addTable');

                var rowCountnew = tablenew.rows.length-1;
                var checkcount=rowCountnew/2;
                // alert(checkcount)
                for(var j=0;j<names.length-1;j++)
                {
                    var g=0;
                    for(var i=0;i<checkcount;i++){

                        var checkkeyId=document.getElementById('keyValue['+i+']');
                        //  alert(checkkeyId.value+"---"+names[j])
                        if(checkkeyId.value.indexOf(names[j])<0)
                        {

                            g=g+1;
                            // alert('n if'+g)


                            /*   var n=0;
                      if(names1.length>0){
                        for(var c=0;c<names1.length;c++){

                           if(names1[c]==names[j]){
                              // alert(names1[c]+"---"+names[j])
                                n=1;
                                break;
                            }
                        }
                        if(n==0){
                             alert(names[j])
                              names1.push(names[j]);
                        }
                      }else{
                         //alert(names[j])
                          names1.push(names[j]);
                      }
                             */

                        }

                    }
                    if(g==checkcount){
                        names1.push(names[j]);
                    }
                }
                // alert("length is "+names1.length)
                var suggestItem = document.createElement('table');
                // var table=document.getElementById(suggestItem);
                var str="";
                for(var i=0; i < names1.length; i++)
                {
                    if(i%2==0){
                        var val="'"+names1[i]+"'";
                        // var row = suggestItem.insertRow(i);

                        //var cell1 = row.insertCell(0);
                        //  nexrowId=row;
                        //  alert(row);
                        str+='<tr>';
                        str+='<td class="suggestLink" id="resultlist'+i+'" onmouseover="selectItem('+this+')" onmouseout ="unselectItem('+this+')" onclick ="setCountry('+val+')">';
                        str+= names1[i];
                        str+='</td>'
                        /*  var suggestItem1 = document.createElement("div");
                    suggestItem1.id = "resultlist" + i;
                    suggestItem1.onmouseover = function(){selectItem(this);};
                    suggestItem1.onmouseout = function(){unselectItem(this);};
                    suggestItem1.onclick = function(){setCountry(this.innerHTML);};
                    suggestItem1.className = "suggestLink";
                    suggestItem1.appendChild(document.createTextNode(names1[i]));
                   cell1.appendChild(suggestItem1);
                         */

                    }else{
                        var val="'"+names1[i]+"'";
                        //  alert(val)
                        str+='<td class="suggestLink" id="resultlist'+i+'" onmouseover="selectItem('+this+')" onmouseout ="unselectItem('+this+')" onclick ="setCountry('+val+')">';
                        str+= names1[i];
                        str+='</td></tr>'
                        /*  alert('--'+nexrowId)
                    var cell2 = nexrowId.insertCell(0);

                    var suggestItem2 = document.createElement("div");
                    suggestItem2.id = "resultlist" + i;
                    suggestItem2.onmouseover = function(){selectItem(this);};
                    suggestItem2.onmouseout = function(){unselectItem(this);};
                    suggestItem2.onclick = function(){setCountry(this.innerHTML);};
                    suggestItem2.className = "suggestLink";
                    suggestItem2.appendChild(document.createTextNode(names1[i]));

                     cell2.appendChild(suggestItem2);
                      alert('else')*/
                    }

                    /*
                    var suggestItem = document.createElement("div");
                    suggestItem.id = "resultlist" + i;
                    suggestItem.onmouseover = function(){selectItem(this);};
                    suggestItem.onmouseout = function(){unselectItem(this);};
                    suggestItem.onclick = function(){setCountry(this.innerHTML);};
                    suggestItem.className = "suggestLink";
                    suggestItem.appendChild(document.createTextNode(names1[i]));
                     */
                    // alert("In For");
                }

                str+='</table>'

                suggestItem.innerHTML=str;
                // alert('str---'+suggestItem.innerHTML)
                suggestList.style.height="100px";
                suggestList.style.overflow="scrollHeight";
                suggestList.appendChild(suggestItem);
                if (names1.length >= 1)
                    suggestList.style.display = "";
                else
                    suggestList.style.display = "none";

            }

            function getSuggestions(country)
            {
                //alert("In getSuggestions "+country.value+"tid is "+tid);
                var url;
                ctxPath=document.getElementById("h").value;

                //alert("url is "+url);
                var payload = "q="+country.value+"&tableName="+tablename+"&colName="+colName+"&connectionId="+connectionId;
                url=ctxPath+"/ParentGrpValuesajax";
                // alert("PayLoad is "+payload);

                sendRequest(url, payload);
            }

            function checkKey(e, obj)
            {

                var country = document.getElementById(tid);
                // alert("In CheckKey")
                //alert("Country value is "+document.getElementById(tid).value)
                //alert("country is "+country.value)


                //alert(country.length)
                if(country.value.length==0)
                {
                    //alert("text box is empty")
                    preservedvalue="";
                }


                if(country.value.length!=0)//if text field is not empty
                {



                    if(country.value.lastIndexOf(',')==(country.value.length -1 ))
                    {
                        preservedvalue=country.value;
                        //alert("preserved value is "+preservedvalue)
                    }
                }

                /* get key pressed */
                var code = (e && e.which) ? e.which : window.event.keyCode;
                // alert("code "+code)
                /* if up or down move thru the suggestion list */
                if (code == KEYDOWN || code == KEYUP)
                {
                    var index = gSelectedIndex;
                    if (code ==  KEYDOWN)
                        index++;
                    else
                        index--;
                    /* find item in suggestion list being looked at if any */
                    selectedItem = document.getElementById("resultlist" + index);

                    if (selectedItem)
                    {

                        selectItem(selectedItem);


                        /* set the field to the suggestion */
                    }


                }
                else if (code == ENTER)  /* clear list if enter key */
                {

                    if(preservedvalue.length==0)
                    {
                        country.value = selectedItem.innerHTML;
                        //alert("in if pv is zero"+country.value)
                    }
                    else
                    {
                        //alert("value in text field is "+country.value)
                        if(country.value.indexOf(',')==-1)
                        {

                            country.value = selectedItem.innerHTML;
                            // alert("In if value is "+country.value)
                        }
                        else{
                            country.value=preservedvalue+selectedItem.innerHTML;
                            //alert("in else preservedvalue "+preservedvalue);
                        }}

                    clearList();

                }

                else if (code == BACKSPACE)
                {
                    var temp=country.value;
                    //alert("temp is "+temp)
                    temp=temp.substring(0,(temp.lastIndexOf(',')+1));
                    // alert("new temp is "+temp)
                    preservedvalue=temp;
                    //alert("new preserved value is "+preservedvalue)
                    gSelectedIndex = -1;
                    getSuggestions(obj);
                    //alert(hai);


                }


                else if (country == obj) /* otherwise get more suggestions */
                {
                    // alert("In obj")
                    gSelectedIndex = -1;
                    getSuggestions(obj);
                }


            }

            function selectItem(selectedItem)
            {
                var lastItem = document.getElementById("resultlist" + gSelectedIndex);
                // alert("resultlist" + gSelectedIndex)
                if (lastItem != null)
                    unselectItem(lastItem);

                selectedItem.className = 'suggestLinkOver';
                gSelectedIndex = parseInt(selectedItem.id.substring(10));
            }

            function unselectItem(selectedItem)
            {
                selectedItem.className = 'suggestLink';
            }

            function setCountry(value)
            {

                if(document.getElementById(tid).value.lastIndexOf(',')==-1)
                {   // alert('in if')
                    document.getElementById(tid).value=value;

                }

                else
                {
                    var temp=document.getElementById(tid).value.substring(0,(document.getElementById(tid).value.lastIndexOf(',')+1))
                    document.getElementById(tid).value=temp+value;
                }




                clearList();
            }

            function checkClick(e)
            {
                var target = ((e && e.target) ||(window && window.event && window.event.srcElement));
                var tag = target.tagName;
                if (tag.toLowerCase() != "input" && tag.toLowerCase() != "div")
                    clearList();
            }

            function clearList()
            {
                var suggestList = document.getElementById(did);
                suggestList.innerHTML = '';
                suggestList.style.display = "none";
            }

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
                    handleResponse1(xmlHttp.responseText);
                    //alert("In state Changed ready state is 4"+xmlHttp.responseText);
                }
            }
            function saveGrp(tableId1,colName1,tableName1,connectionId1){


                var table = document.getElementById(tableId1);
                var rcount1 = table.rows.length;
                var dupcount=0;
                var colcount=0;
                var cname="";
                var cname1="";
                // alert(rcount1);
                var extrowcount=(rcount1-1)/2;
                // alert(document.getElementById('grpName'));
                if(document.getElementById('grpName').value==''){
                    alert('Please enter Group Name')
                }
                else{


                    var countall=0;
                    for(var v=0;v<extrowcount;v++)
                    {
                        if(document.getElementById('parent['+v+']').value=="" ||document.getElementById('keyValue['+v+']').value==""){
                            countall=1;
                        }

                    }
                    if(countall==0){
                        //  alert(extrowcount)
                        for(var i=0;i<extrowcount;i++)
                        {
                            // alert(document.getElementById('key['+i+']').value.split(',')[0])

                            cname=document.getElementById('parent['+i+']').value;
                            // alert(cname)
                            for(var k=i+1;k<extrowcount;k++)
                            {   //alert(k+"---"+i);
                                cname1=document.getElementById('parent['+k+']').value;
                                // alert(cname1)
                                if(cname==cname1){
                                    dupcount++;
                                    break;
                                }
                                else
                                    colcount++;

                            }
                        }
                        if((extrowcount)>1){
                            if(dupcount>0){
                                alert('No Two Names are Same Please Select Different Name');
                            }
                            else{
                                document.getElementById('tabRowCount').value=extrowcount;
                                document.myForm.action="saveParentGroup.do";
                                document.myForm.submit();
                                parent.savecancelGrp();
                            }
                        }else{
                            document.getElementById('tabRowCount').value=extrowcount;
                            document.myForm.action="saveParentGroup.do";
                            document.myForm.submit();
                            parent.savecancelGrp();
                        }
                    }else{
                        alert('Please enter all Parent and Key values');
                    }


                }



            }

            function savecancelGrp()
            { //alert('in jsp');
                parent.savecancelGrp();
            }

            function duplicateCheck(grpName,tabId,cpath,connectionId,isNewTable,tableName){
                var gname=grpName.value;
                // alert(cpath+'/DuplicateGroup?grpName='+gname+'&tabId='+tabId)
                $.ajax({
                    url: cpath+'/DuplicateGroup?grpName='+gname+'&tabId='+tabId+"&connectionId="+connectionId+"&isNewTable="+isNewTable+"&tableName="+tableName,
                    success: function(data){
                        //alert(data)
                        if(data==0){

                        }else{
                            alert('Please Enter Different Group Name');
                        }

                    }
                });

            }




    //sreekanth start
    function createParentnew(selObj,id1,id2,actcolName,tabName,conId){
                var tablenew = document.getElementById('addTable');
                var rowCountnew = tablenew.rows.length-1;
                var checkcount=rowCountnew/2;

        //alert('selobject'+document.getElementById(selObj).value);
        selIndexValue = document.getElementById(selObj).value;
        var num = "selectName".length;
        var textboxnum=selObj.substring(num,selObj.length);

        /* for(var i=0;i<selObj.length;i++){
                    if(selObj[i].selected){
                        selIndexValue=selObj[i].value;
                        alert(selIndexValue);
                        textboxnum=i;
                    }
                } */
                var colList="";
        if(selIndexValue =="In"){
                for(var k=0;k<checkcount;k++){
                var checkkeyId=document.getElementById('keyValue['+k+']');
                if(document.getElementById(id1)!=checkkeyId){
                   if(checkkeyId.value!=""){
                    colList+=","+checkkeyId.value;
                    
                   }
                }

                }
                if(colList!=""){
                    colList=colList.substring(1);
                }
                var orival=document.getElementById(id1).value;
                var colType=document.getElementById("colType").value;
                var f=document.getElementById('dataDispparentcols');
                var s="pbgroupingparentColumns.jsp?tableName="+tabName+"&colName="+actcolName+"&connectionId="+conId+"&id1="+id1+"&id2="+id2+"&colList="+colList+"&orival="+orival+"&colType="+colType+"&oldValuesids="+oldValuesids;
                f.src=s;

            document.getElementById('dataDispparentcols').style.display='block';
                document.getElementById('fade').style.display='block';
                document.getElementById('mainBody').style.overflow='hidden';
        }else if(selIndexValue == "Like"){

            //alert(document.getElementById('keyValue['+textboxnum+']').value);
            document.getElementById('keyValue['+textboxnum+']').value = "";

           // alert('tevname is '+tevname);
            //alert(document.getElementById('keyValue['+textboxnum+']').value);
            /* for(var k=0;k<checkcount;k++){
                        alert(selObj[k].value);
                        alert("keyValue["+k+"]");
                        textboxnum=document.getElementById('keyValue['+k+']').value;
                       // textboxnum.value='testing';
                         alert('textboxnum'+textboxnum);
                         textboxnum.value="";
                        // textboxnum+='empty';

                         alert("After textboxnum----------------"+textboxnum)
                        }*/


        }else{

            }

    }
     /*function CParentnew(id1,id2,actcolName,tabName,conId){
        var tablenew = document.getElementById('addTable');
        var rowCountnew = tablenew.rows.length-1;
        var checkcount=rowCountnew/2;
         var colList="";
             for(var k=0;k<checkcount;k++){
                var checkkeyId=document.getElementById('keyValue['+k+']');
                if(document.getElementById(id1)!=checkkeyId){
                    if(checkkeyId.value!=""){
                        colList+=","+checkkeyId.value;
                    } } }
            if(colList!=""){
                colList=colList.substring(1);
            }
            var orival=document.getElementById(id1).value;
            var colType=document.getElementById("colType").value;
            var f=document.getElementById('dataDispparentcols');
            var s="pbgroupingparentColumns.jsp?tableName="+tabName+"&colName="+actcolName+"&connectionId="+conId+"&id1="+id1+"&id2="+id2+"&colList="+colList+"&orival="+orival+"&colType="+colType;
            f.src=s;

            document.getElementById('dataDispparentcols').style.display='block';
            document.getElementById('fade').style.display='block';
            document.getElementById('mainBody').style.overflow='hidden';
         }*/
    //sreekanth over
    //add by sreekanth start
    function reChange(evt,id1,id2,actcolName,tabName,conId) {
        //alert('event is '+evt)
        var charCode = (evt.which) ? evt.which : evt.keyCode;

       if(charCode == '13'){
            if(selIndexValue =="Like"){
              //  alert('rechange function')
                var tablenew = document.getElementById('addTable');
                var rowCountnew = tablenew.rows.length-1;
                var colList="";
                var checkcount=rowCountnew/2;
                for(var k=0;k<checkcount;k++){
                    var checkkeyId=document.getElementById('keyValue['+k+']');
                    if(document.getElementById(id1)!=checkkeyId){
                        if(checkkeyId.value!=""){
                            colList+=","+checkkeyId.value;
                        }
                        var orival=document.getElementById(id1).value;
                        var strReplaceAll = orival;
                        var intIndexOfMatch = strReplaceAll.indexOf( "%" );
                        while (intIndexOfMatch != -1){
                            strReplaceAll = strReplaceAll.replace( "%", "~" )
                            intIndexOfMatch = strReplaceAll.indexOf( "%" );
                        }
                        orival =strReplaceAll
                        //alert( strReplaceAll );
                        var tvalue=document.getElementById('keyValue['+k+']').value;
                        //alert('orival is'+orival);
                        var colType=document.getElementById("colType").value;
                    }

                }// alert('colType'+colType)


                //tableName="+tabName+"&colName="+actcolName+"&connectionId="+conId+"&id1="+id1+"&id2="+id2+"&colList="+colList+"&orival="+orival+"&colType="+colType;
                $.ajax({
                    url: 'dimenkeyvalueaction.do?dimkeyparam=testQuery&tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orivalue='+orival+'&colType='+colType,
                    success: function(data) {
                        var retlist=document.getElementById(id1).value="";
                        var sx= data;
                        sx=sx.replace('[' ,"" );
                        sx=sx.replace(']' ,"" );
                        alert('sx'+sx);
                        document.getElementById(id1).value=sx;
                    }
                });
            }
            else if(selIndexValue =="In"){
           // alert('id1 is '+id1);
            var selectId=id1.split('[');
           // alert('selectid is '+selectId[1].split("]")[0]);
            createParentnew('selectName'+selectId[1].split("]")[0],id1,id2,actcolName,tabName,conId);
         //  alert('In else if case after ');
            }
        }
    }


    // add by sreekanth over

            function cancelGrpnew()
            {
        document.getElementById("dataDispparentcols").style.display='none';
                document.getElementById('fade').style.display='';
                document.getElementById('mainBody').style.overflow='auto';

            }
            function savecancelGrpnew(valList,newid1,newid2)
    {   document.getElementById("dataDispparentcols").style.display='none';
                document.getElementById('fade').style.display='';
                //alert(valList+"--"+newid1+"--"+newid2);
                document.getElementById(newid1).value=valList;
              
            }
            function savecancelGrpwaitnew(){ 
                window.location.reload(true);
                document.getElementById('fade1').style.display='';
            }


        </script>
    </body>
</html>

