<%@page contentType="text/html" pageEncoding="UTF-8"  import="prg.db.PbDb,java.sql.*,utils.db.*,utils.db.*,utils.db.ProgenConnection,prg.db.PbReturnObject,com.progen.datadisplay.db.PbDataDisplayBeanDb"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
String contextPath=request.getContextPath();
%>
<html>
    <head>

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/treeview/jquery.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

<!--        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>-->

<!--        <script type="text/javascript"  language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->


        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="confirm.css" rel="stylesheet" type="text/css" />
        <link href="jquery.contextMenu.css" rel="stylesheet" type="text/css" />

        <script src="jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="jquery.contextMenu.js" type="text/javascript"></script>
        <link href="myStyles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
       
        <style type="text/css" >
            .myHead
            {
                font-family: Verdana;
                font-size: 12px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#b4d9ee;
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
        </style>
    </head>
    <body>
        <%
                    String flag="wizard";
                    String check="true";
        String listarr1[]=null;
        String cols=(String)request.getParameter("cols");
        String status=(String)request.getParameter("flag");
        if(cols!=null)
        { listarr1 = cols.split(",");}
                   Connection conc=null;
                    try {
                        // Connection con, conc = null;
                        Statement st, stc, st1 = null;
                        // ResultSet rs, rs1, rsc = null;
                        PbDb pbDb = new PbDb();
                        // con = ProgenConnection.getInstance().getConnection();
                        //  st = con.createStatement();
                        PbReturnObject pbrors = new PbReturnObject();
                        String dimId = "";
                        //String colId = "";
                        String dimtabId = "";
                        String colName = "";
                        String is_pk = "";
                        String tableId = "";
                        String tabName = "";
                        String colid = (String)request.getParameter("colId");
                   //     
                        String listarr[]=colid.split(",");

                        dimtabId = listarr[0];
                        tableId = listarr[1];
                        dimId = listarr[2];
                        tabName = listarr[3];

                        String sql = "select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id=" + dimtabId + " and d.is_available='Y' order by d.is_pk_key desc,m.table_col_name ASC";
                        pbrors = pbDb.execSelectSQL(sql);
                        int addMoreCount = pbrors.getRowCount();


        %>



        <center>
            <form action=""  name="myForm" method="post">
                <br> <br>
                <table style="width:60%">
                    <input type="hidden" name="dimId" value="<%=dimId%>">
                    <input type="hidden" name="dimtabId" value="<%=dimtabId%>">
                    <tr>
                        <td class="myHead" style="width:50%">
                            Is Denorm
                        </td>
                        <td style="width:50%">
                            <% if(cols!=null)
                                {%>
                                 <input type="checkbox" name="isdenom" id="isdenom" onchange="" checked="checked">
                                 <%}
                        else {%>
                            <input type="checkbox" name="isdenom" id="isdenom" onchange="addDenorm()">
                            <%}%>
                        </td>
                    </tr>
                </table>
                        <% if(cols!=null)
                                {%>
                                 <div id="normal"  style="display:none">
                                 <%}
                        else {%>
                <div id="normal">
                            <%}%>

                    <table style="width:60%">
                        <tr>
                            <td  class="myHead" style="width:50%">
                                Primary Key
                            </td>
                            <td width="50%">
                                <select id="key" name="key" class="myTextbox5"  style="width:122Px" onchange="showpk('<%=dimtabId%>','<%=tabName%>','0','<%=tableId%>')">
                                    <%  int i = 0;
                                                            String pkId = "";
                                                            String pkname = "";
                                                            String ids = "";
                                                            String names = "";
                                                            for (int l = 0; l < pbrors.getRowCount(); l++) {
                                                                if (i == 0) {
                                                                    pkId = String.valueOf(pbrors.getFieldValueInt(l, 0));
                                                                    pkname = pbrors.getFieldValueString(l, 1);
                                                                }
                                                                ids += "," + String.valueOf(pbrors.getFieldValueInt(l, 0));

                                                                names += "," + pbrors.getFieldValueString(l, 1);
                                                                i++;
                                    %>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}%>


                                </select>
                            </td>
                        </tr>
                        <%

                                                // st1 = con.createStatement();
                                                // String sql1 = "select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id=" + dimtabId + " and d.is_available='Y' order by d.is_pk_key desc";

                                                //rs1 = st1.executeQuery(sql1);
                                                //PbReturnObject pbrors1 = new PbReturnObject(rs1);

                        %>
                        <tr>
                            <td class="myHead"  width="50%">
                                Primary Key Value
                            </td>



                            <td width="50%">
                                <select id="keyValue" name="keyValue" class="myTextbox5" style="width:122Px" onchange="tabmsg('<%=tabName%>','0','<%=tableId%>')">
                                    <%
                                                            int row1 = 0;

                                                            int j = 0;
                                                            String pkvalueId = "";
                                                            String pkvaluename = "";
                                                            for (int l = 0; l < pbrors.getRowCount(); l++) {
                                                                if (j == 0) {
                                                                    pkvalueId = String.valueOf(pbrors.getFieldValueInt(l, 0));
                                                                    pkvaluename = pbrors.getFieldValueString(l, 1);
                                                                    j++;
                                                                }
                                                                row1++;
                                    %>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}

                                    %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="myHead"  width="50%">
                                Member Name
                            </td>

                            <%
                                                    String val = "";
                                                    val = pkvaluename.split("_")[0];
                            %>
                            <td width="50%">
                                <input type="text" id="memname" name="memname" class="myTextbox5"  style="width:120Px" value="<%=val%>">
                            </td>
                        </tr>
                        <tr>
                            <td class="myHead"  width="50%">Description</td>
                            <td width="50%">
                                <input type="text" id="desc" name="desc" class="myTextbox5"  style="width:120Px" value="<%=val%>"></td>
                        </tr>
                         <tr>
                              <td class="myHead" width="50%">Order BY</td>
                              <td  width="50%"><select id="orderBy" name="orderBy" class="myTextbox5" style="width:122Px">
                                       <% for (int l = 0; l < pbrors.getRowCount(); l++) { %>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}%>
                               </select></td>
                         </tr>
                         <tr>
                              <td class="myHead" width="50%">Is Null Value</td>
                             <td  width="50%"<input type="text" id="isnullValue" name="isnullValue" class="myTextbox5"  style="width:120Px" value="NAN"></td>
                         </tr>
       <!--                       <tr>
                                                  <td class="myHead"   width="50%">
                                                            All Allowed
                                                        </td>
                                                        <td width="50%">
                                                            <input type="checkbox" checked id="all" name="all" value="Y" onchange="typedis()">
                                                        </td>
                        </tr>-->
                    </table>
                </div>

                <div id="notall" style="display:none;">
                    <Table style="width:60%">
                        <Tr>
                            <td class="myHead"   width="50%">
                                Default value
                            </td>
                            <Td width="50%">

                                <select id="defaultValue" name="defaultValue" class="myTextbox5" style="width:122px">
                                    <%
                                                            ids = ids.substring(1);
                                                            names = names.substring(1);
                                                            String defaultids[] = ids.split(",");
                                                            String defaultnames[] = names.split(",");

                                                             conc = ProgenConnection.getInstance().getConnectionByTable(tableId);

                                                            String sqlc = "select distinct " + pkvaluename + " from " + tabName;

                                                            PbReturnObject pbrorsc = new PbReturnObject();
                                                            pbrorsc = pbDb.execSelectSQL(sqlc, conc);
                                                            conc = null;
                                                            //  for (int l = 0; l < pbrorsc.getRowCount(); l++) {

                                    %>

                                    <%-- <option value="<%=pbrorsc.getFieldValueString(l, 0)%>"><%=pbrorsc.getFieldValueString(l, 0)%></option>--%>
                                    <%// }%>
                                </select>


                            </Td>
                        </Tr>
                    </Table>

                </div>
                <%
                if(cols==null){
                %>
                <div id="normalbutton">
                    <table >
                        <tr>
                            <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveMember()"></td>
                            <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelMember()"></td>

                        </tr>
                    </table>
                </div>
                <div id="headings" style="display:none">
                    <%}
                       else{%>
                       <div id="headings"><%}%>
                    <table align="center" border="0" id="addTable">
                        <tr><td></td>
                        </tr>
                        <tr id="0">
                            <td style="width:120px" class="myHead" align="center" ><b>Primary Key</b></td>

                            <td style="width:120px" class="myHead" align="center" ><b>Primary Key Value</b></td>
                            <td style="width:120px" class="myHead" class="myHead" align="center" ><b>Member Name</b></td>
                            <td style="width:120px" class="myHead" align="center" ><b>Description</b></td>
                            <td style="width:120px" class="myHead" align="center" ><b>Order BY</b></td>
                            <td style="width:120px" class="myHead" align="center" ><b>Is Null Value</b></td>
                            <td style="width:120px" class="myHead" align="center" ><b>All Allowed</b></td>
                            <td  style="width:120px" class="myHead" align="center" ><div id="thdesc" style="display:none">
                                    <b> Default Value</b>
                                </div></td>


                        </tr>
                        <%int r=0;
                        int rowcount = 0;
                        if(cols!=null)
                            {
                            rowcount=(listarr1.length)+1;

                            } else
                                {
                                                if (pbrors.getRowCount() >= 4) {
                                                    rowcount = 5;
                                                } else {

                                                    rowcount = pbrors.getRowCount() + 1;
                                                }
}
                                                for (int k = 1; k < rowcount; k++) {%>
                        <Tr id="<%=k%>">
                            <Td>
                                <select name="key[<%=k%>]" id="key[<%=k%>]" size=1  class="myTextbox5" style="width:120px" onchange="showpk('<%=dimtabId%>','<%=tabName%>','<%=k%>','<%=tableId%>')">

                                    <% for (int l = 0; l < pbrors.getRowCount(); l++) {
                                       if(cols!=null){
                                            String s1=(String)pbrors.getFieldValueString(l, 0);
                                        if(listarr1[r].equalsIgnoreCase(s1)){
                                    %>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>,<%=pbrors.getFieldValueString(l, 1)%>" selected="selected"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}
                                        else{%>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>,<%=pbrors.getFieldValueString(l, 1)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}}
                                       else {%>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>,<%=pbrors.getFieldValueString(l, 1)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%} }%>
                                </select>

                            </Td>
                            <Td>
                                <select name="keyValue[<%=k%>]" id="keyValue[<%=k%>]" size=1  class="myTextbox5" style="width:120px" onchange="tabmsg('<%=tabName%>','<%=k%>','<%=tableId%>')">

                                    <% int count = 0;
                                                                       String desc = "";
                                                                       String[] arr;
                                                                       StringBuffer sb=new StringBuffer();

                                                                       for (int l = 0; l < pbrors.getRowCount(); l++) {
                                                                           if(cols!=null)
                                                                               {
                                                                                String s1=(String)pbrors.getFieldValueString(l, 0);
                                        if(listarr1[r].equalsIgnoreCase(s1)){
                                                                           if (count == 0) {
                                                                               //desc = pbrors.getFieldValueString(l, 1).split("_")[0];
                                                                               desc = pbrors.getFieldValueString(l, 1).replaceAll("_", " ");
                                                                               arr=desc.split(" ");
                                                                               for(int x=0;x<arr.length;x++)
                                                                                   sb.append(" ").append(arr[x].charAt(0)).append((arr[x].substring(1,arr[x].length())).toLowerCase());


                                                                               count++;
                                                                           }


                                    %>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>,<%=pbrors.getFieldValueString(l, 1)%>" selected="selected"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}
                                       else{%>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>,<%=pbrors.getFieldValueString(l, 1)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}
                                                                           }
                                                                           else{
                                                                           if (count == 0) {
                                                                               //desc = pbrors.getFieldValueString(l, 1).split("_")[0];
                                                                               desc = pbrors.getFieldValueString(l, 1).replaceAll("_", " ");
                                                                               arr=desc.split(" ");
                                                                               for(int x=0;x<arr.length;x++)
                                                                                   sb.append(" ").append(arr[x].charAt(0)).append((arr[x].substring(1,arr[x].length())).toLowerCase());
                                                                             //  

                                                                               count++;
                                                                           }
                                    %>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>,<%=pbrors.getFieldValueString(l, 1)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}}%>
                                </select>

                            </Td>
                            <td>
                                <input type="text" id="memname[<%=k%>]" name="memname[<%=k%>]" class="myTextbox5" style="width:120px" maxlength="255" value="<%=sb.toString().substring(1)%>">
                            </td>

                            <Td>
                                <Input type="text" class="myTextbox5" name="desc[<%=k%>]" id="desc[<%=k%>]" maxlength=255 style="width:120px" value="<%=sb.toString().substring(1)%>">
                            </Td>
                            <td>
                                <select id="orderBy[<%=k%>]" name="orderBy[<%=k%>]" class="myTextbox5" style="width:120px">
                                 <%


                                                                       for (int l = 0; l < pbrors.getRowCount(); l++) {
                                                                           if (count == 0) {
                                                                               desc = pbrors.getFieldValueString(l, 1).split("_")[0];
                                                                               count++;
                                                                           }
                                  if(cols!=null){
                                      String s1=(String)pbrors.getFieldValueString(l, 0);
                                        if(listarr1[r].equalsIgnoreCase(s1)){

                                    %>
                                    <option value="<%=pbrors.getFieldValueInt(l, 0)%>" selected="selected"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}
                                      else{%>
                                      <option value="<%=pbrors.getFieldValueInt(l, 0)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}
                                      }
                                      else {
                                    %>
                                      <option value="<%=pbrors.getFieldValueInt(l, 0)%>"><%=pbrors.getFieldValueString(l, 1)%></option>
                                    <%}}%>
                                </select>
                            </td>
                            <td>
                                <input type="text" class="myTextbox5" id="isnullValue[<%=k%>]" name="isnullValue[<%=k%>]" value="NAN" maxlength=255 style="width:120px">
                            </td>

                            <%--  <input type="hidden" id="memname[<%=k%>]" name="memname[<%=k%>]" class="myTextbox5" style="width:120px" maxlength="255" value="<%=desc%>">--%>
                            <td align="center">
                                <input type="checkbox" checked id="all[<%=k%>]" name="all[<%=k%>]" value="Y" onchange="checknotall1(this,<%=k%>,'addTable'), tabmsg('<%=tabName%>','<%=k%>','<%=tableId%>')" >
                            </td>
                            <td>
                                <div id="notall[<%=k%>]" style="display:none">

                                    <select id="defaultValue[<%=k%>]" name="defaultValue[<%=k%>]" style="width:120px" class="myTextbox5" style="width:150px" >
                                        <%   for (int l = 0; l < pbrors.getRowCount(); l++) {
                                            if(cols!=null){
                                                 String s1=(String)pbrors.getFieldValueString(l, 0);
                                        if(listarr1[r].equalsIgnoreCase(s1)){%>
                                            <option value="<%=pbrors.getFieldValueString(l, 0)%>" selected="selected"><%=pbrors.getFieldValueString(l, 0)%></option>
                                        <% }
                                            else{%>
                                        <option value="<%=pbrors.getFieldValueString(l, 0)%>"><%=pbrors.getFieldValueString(l, 0)%></option>
                                        <% }}
                                            else {%>
                                        <option value="<%=pbrors.getFieldValueString(l, 0)%>"><%=pbrors.getFieldValueString(l, 0)%></option>
                                        <% }}%>
                                    </select>

                                </div>
                            </td>
                        </Tr>
                        <%
                           if(cols!=null){r++;}                     }

                        %>
                    </table>

                    <Table><Tr>
                            <Td>  <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveMemberDenorm('addTable')">&nbsp;&nbsp;<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelMember()"></Td>
                            <%if(cols==null){%>

                            <td>  <input type="button" class="navtitle-hover" style="width:auto" value="Add Row" onclick="addRow('addTable','<%=dimtabId%>','<%=tabName%>','<%=addMoreCount%>','<%=tableId%>')"/></td>
                            <td>  <input type="button"  class="navtitle-hover" style="width:auto" value="Delete Row" onclick="deleteRow('addTable')" /></td>

             <%}%>
                        </Tr>
                    </Table>

                </div>
                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                <%--  <input type="hidden" id="memname" name="memname" class="myTextbox5"  maxlength="255" value="<%=val%>"> --%>
                <input type="hidden" id="tabRowCount" name="tabRowCount" class="myTextbox5"  maxlength="255" >
                <input type="hidden" id="dbtabId" name="dbtabId" class="myTextbox5"  value="<%=tableId%>" >
                <input type="hidden" id="dbtabName" name="dbtabName" class="myTextbox5"  value="<%=tabName%>" >
                <%if(cols!=null){%>
             <input type="hidden" id="flag" name="flag" class="myTextbox5"  value="<%=flag%>" >
             <%}%>
             <%if(status!=null){%>
             <input type="hidden" id="check" name="check" class="myTextbox5"  value="<%=check%>" >
             <%}%>
            </form>
        </center>
        <%
                        if (conc != null) {
                            conc.close();
                            conc=null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (conc != null) {
                            conc.close();
                            conc=null;
                        }

                    }

        %>
         <script type="text/javascript" >
            function saveMember()
            {
                //  alert('save');
                document.myForm.action="saveMember.jsp";
                document.myForm.submit();
            }
             
            function saveMemberDenorm(tableId)
            {   

                var table = document.getElementById(tableId);
                var rcount1 = table.rows.length;
                var dupcount=0;
                var colcount=0;
                // alert(rcount1);
                for(var i=1;i<rcount1-1;i++)
                {
                    // alert(document.getElementById('key['+i+']').value.split(',')[0])
                 
                    cname=document.getElementById('key['+i+']').value.split(',')[0];
                    for(var k=i+1;k<rcount1-1;k++)
                    {
                        cname1=document.getElementById('key['+k+']').value.split(',')[0];
                        if(cname==cname1){
                            dupcount++;
                            break;
                        }
                        else
                            colcount++;
             
                    }
                }
                if((rcount1-2)>1){
                    if(dupcount>0){
                        alert('No Two Primary keys are Same Please Select Different Primary Key');
                    }
                    else{
                        document.getElementById('tabRowCount').value=rcount1;
                        // alert(document.getElementById('tabRowCount').value);
                        document.myForm.action="saveMember.jsp";
                        // alert(document.myForm.action);
                        document.myForm.submit();
                    }
                }else{
                    document.getElementById('tabRowCount').value=rcount1;
                    document.myForm.action="saveMember.jsp";
                    // alert(document.myForm.action);
                    document.myForm.submit();
                }
            }
            function cancelMember()
            {
                parent.refreshparentmem();
            }

            var tabName1;
            var pkvalue;
            var kId;
            var tabId;
            function tabmsg(tabName,k,tabId1){
                kId=k;
                tabId=tabId1;
                // alert(tabId+'in tab')
                var p;
                var obj;
                if(kId>0){
                    p=document.getElementById('keyValue['+kId+']').value;

                    obj=document.getElementById('keyValue['+kId+']');
                }
                else{
                    p=document.getElementById("keyValue").value;

                    obj=document.getElementById("keyValue");
                }
                for(var i=0;i<obj.length;i++)
                {
                    var k=obj.options[i].value;
               
                    if(p==k){
                        var name1=obj.options[i].text;
                        var name=obj.options[i].text.split("_")[0];
                        if(kId>0){
                            document.getElementById('desc['+kId+']').value=name;
                            // alert(document.getElementById('memname['+kId+']').value);
                            document.getElementById('memname['+kId+']').value=name;
                        }else{
                            document.getElementById('desc').value=name;
                            document.getElementById('memname').value=name;
                        }
                        

                    }
                }
                tabName1=tabName;
                pkvalue=name1;
                showpkDefault(tabName,name1,kId,tabId);
            }
            function typedis(){
                // alert('hi');

                if(document.myForm.all.checked ==true)
                { //alert('checked');
                    document.getElementById("notall").style.display='none';

                } else {
                    //  alert('not checked');
                    document.getElementById("notall").style.display='';

                }
            }





            var xmlHttp2;

            function showpkDefault(str,str1,k,tabId1)
            {   //alert(tabId1)
                kId=k;
                tabId=tabId1;
                if (str.length==0)
                {
                    document.getElementById("txtHint").innerHTML="";

                    return;
                }
                xmlHttp2=GetXmlHttpObject();
                if (xmlHttp2==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                ctxPath=document.getElementById("h").value;
                var pkkey = document.getElementById("key").value;
                var url=ctxPath+"/GetDefaultValues";
                url=url+"?tabName="+str+"&pkvaluename="+str1+"&tableId="+tabId;
                // alert(url)
                // var payload = "q="+str+"&id="+id;
                //alert('target url is---'+url);
                xmlHttp2.onreadystatechange=stateChangedpkdefault;
                xmlHttp2.open("GET",url,true);
                xmlHttp2.send(null);
            }

            function stateChangedpkdefault()
            {
                //  alert('hi in target')

                if (xmlHttp2.readyState==4)
                {
                    var output=xmlHttp2.responseText;
                    // alert("output is "+output);
                    var pkNames=output.split("\n");
                    //alert("names "+pkNames);
                    var obj;
                    if(kId>0){
                        obj=document.getElementById('defaultValue['+kId+']');

                        document.getElementById('defaultValue['+kId+']').options.length=0;
                    }else{
                        obj=document.myForm.defaultValue;
                        document.getElementById('defaultValue').options.length=0;
                    }
                    for(var i=obj.length-1;i>=0;i--)
                    {
                        obj.options[i] = null;

                    }



                    for(var j=0;j<pkNames.length-1;j++)
                    {


                        obj.options[j] = new Option(pkNames[j].split(",")[0],pkNames[j].split(",")[0]);

                    }
                }
            }


            function showpk(str,tabName,k,tabId1)
            {
               
                kId=k;
                tabName1=tabName;
                tabId=tabId1;
                //alert(tabId)
                if (str.length==0)
                {
                    document.getElementById("txtHint").innerHTML="";

                    return;
                }
                xmlHttp2=GetXmlHttpObject();
                if (xmlHttp2==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                ctxPath=document.getElementById("h").value;
                var pkkey = document.getElementById("key").value.split(',')[0];
                var url=ctxPath+"/getPKValues";
                url=url+"?dimtabId="+str+"&key="+pkkey;
                //alert(url)
                // var payload = "q="+str+"&id="+id;
                //alert('target url is---'+url);
                xmlHttp2.onreadystatechange=stateChangedpkTarget;
                xmlHttp2.open("GET",url,true);
                xmlHttp2.send(null);

            }


            function stateChangedpkTarget()
            {
                //alert('hi in target'+tabName1);

                if (xmlHttp2.readyState==4)
                {
                    var output=xmlHttp2.responseText;
                    //alert("output is "+output);
                    var pkNames=output.split("\n");
                    //alert("names "+pkNames);
                    var obj;
                    var objkey;
                    var oderByObj;
                    if(kId>0){

                        obj=document.getElementById('keyValue['+kId+']');
                        oderByObj=document.getElementById('orderBy['+kId+']');
                        objkey=document.getElementById('key['+kId+']').value.split(',')[0];
                        // alert('hi option'+document.getElementById("key").text);
                        document.getElementById('keyValue['+kId+']').options.length=0;
                    }

                    else{
                        obj=document.myForm.keyValue;
                        oderByObj=document.myForm.orderBy;
                        objkey=document.getElementById("key").value.split(',')[0];
                        // alert('hi option'+document.getElementById("key").text);
                        document.getElementById("keyValue").options.length=0;
                    }

                    for(var i=obj.length-1;i>=0;i--)
                    {
                        obj.options[i] = null;
                        oderByObj.options[i] = null;

                    }
                    var count=0;

                    for(var j=0;j<pkNames.length-1;j++)
                    {        //  alert('in for first'+pkNames[j].split(",")[0])
                        if(pkNames[j].split(",")[0]==(objkey))
                        {
                            // alert('in if'+j);
                            count=j;
                            obj.options[0] = new Option(pkNames[j].split(",")[1],pkNames[j]);
                            oderByObj.options[0] = new Option(pkNames[j].split(",")[1],pkNames[j].split(",")[0]);
                            var name=new Array
                             name=pkNames[j].split(",")[1].split("_");
                             var memName="";
                             for(var x=0;x<name.length;x++){
                                 alert(name[x])
                                 memName=memName.concat(" ",name[x].charAt(0),(name[x].substr(1)).toLowerCase());}
                            if(kId>0){
                                document.getElementById('desc['+kId+']').value=memName.substr(1);
                               // document.getElementById('desc['+kId+']').value=pkNames[j].split(",")[1].split("_")[0];
                                pkvalue=pkNames[j].split(",")[1];
                                //document.getElementById('memname['+kId+']').value=pkNames[j].split(",")[1].split("_")[0];
                                document.getElementById('memname['+kId+']').value=memName.substr(1);

                             
                            }else{
                                document.getElementById("desc").value=memName.substr(1);;
                              //  document.getElementById("desc").value=pkNames[j].split(",")[1].split("_")[0];
                                pkvalue=pkNames[j].split(",")[1];
                             //   document.getElementById('memname').value=pkNames[j].split(",")[1].split("_")[0];
                                document.getElementById('memname').value=memName.substr(1);;
                              
                              
                                

                            }
                           
                        }

                    }


                    for(var j=0,k=1;j<pkNames.length-1;j++)
                    {
                        if(count!=j){

                            //alert('in for '+j);
                            obj.options[k] = new Option(pkNames[j].split(",")[1],pkNames[j]);
                            oderByObj.options[k] = new Option(pkNames[j].split(",")[1],pkNames[j].split(",")[0]);
                            k++;
                            //document.getElementById("desc").value=pkNames[j].split(",")[1].split("_")[0];
                        }
                    }
                    //alert('tabname='+tabName1+'pvalue'+pkvalue);
                    showpkDefault(tabName1,pkvalue,kId,tabId);

                }
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

            function addOption(selectbox,text,value)
            {
                var optn = document.createElement("OPTION");
                optn.text = text;
                optn.value = value;
                //alert("In addOPtions--> "+value);
                selectbox.options.add(optn);
            }
            // scrpipt for denorm table


            function addRow(tableID,dimTabId1,tabNamet,addMoreCount,tabId1) {


                //  alert(document.getElementById(tableID))
                var table = document.getElementById(tableID);

                var rowCount = table.rows.length;
                //alert(rowCount+' in add before')
                var idx = rowCount ;
                //alert(idx);
                //  alert(addMoreCount);
                if(addMoreCount>=rowCount-1){
                    var row = table.insertRow(rowCount);
                    row.id = idx;
                    idx=idx-1;

                    var cell1 = row.insertCell(0);
                    var lengthElement = document.createElement("select");
                    lengthElement.name = "key["+idx+"]";
                    lengthElement.id = "key["+idx+"]";
                    lengthElement.className="myTextbox5";
                    lengthElement.style.width="120px";
                    lengthElement.onchange=function(){
                        showpk(dimTabId1,tabNamet,idx,tabId1);
                    }
                    cell1.appendChild(lengthElement);
                    var obj=document.getElementById('key[1]');
                    for(var selLen=0;selLen<obj.length;selLen++){
                        document.getElementById("key["+idx+"]").options[selLen] = new Option(document.getElementById("key[1]").options[selLen].text,document.getElementById("key[1]").options[selLen].value);
                    }
               
                    var cell2 = row.insertCell(1);
                    var lengthElement2 = document.createElement("select");
                    lengthElement2.name = "keyValue["+idx+"]";
                    lengthElement2.id = "keyValue["+idx+"]";
                    lengthElement2.className="myTextbox5";
                    lengthElement2.style.width="120px";
                    lengthElement2.onchange=function(){
                        tabmsg(tabNamet,idx);
                    }
                    cell2.appendChild(lengthElement2);
                    for(var selLen=0;selLen<obj.length;selLen++){
                        document.getElementById("keyValue["+idx+"]").options[selLen] = new Option(document.getElementById("keyValue[1]").options[selLen].text,document.getElementById("keyValue[1]").options[selLen].value);
                    }

                    var cell5 = row.insertCell(2);
                    var splitElement5 = document.createElement("input");
                    splitElement5.type = "text";
                    splitElement5.name = "memname["+idx+"]";
                    splitElement5.id = "memname["+idx+"]";
                    splitElement5.value=document.getElementById('desc[1]').value;
                    splitElement5.className = "myTextbox5";
                    splitElement5.style.width="120px";
                    cell5.appendChild(splitElement5);


                    var cell3 = row.insertCell(3);
                    var splitElement = document.createElement("input");
                    splitElement.type = "text";
                    splitElement.name = "desc["+idx+"]";
                    splitElement.id = "desc["+idx+"]";
                    splitElement.value=document.getElementById('desc[1]').value;
                    splitElement.className = "myTextbox5";
                    splitElement.style.width="120px";
                    cell3.appendChild(splitElement);

                   var orderBy = row.insertCell(4);
                    var orderByElement = document.createElement("select");
                    orderByElement.name = "orderBy["+idx+"]";
                    orderByElement.id = "orderBy["+idx+"]";
                    orderByElement.className="myTextbox5";
                    orderByElement.style.width="120px";
//                    orderByElement.onchange=function(){
//                        tabmsg(tabNamet,idx);
//                    }
                     orderBy.appendChild(orderByElement);
                    for(var selLen=0;selLen<obj.length;selLen++){
                        document.getElementById("orderBy["+idx+"]").options[selLen] = new Option(document.getElementById("orderBy[1]").options[selLen].text,document.getElementById("orderBy[1]").options[selLen].value);
                    }

                    orderByElement.className="myTextbox5";

                   var isnulvalue = row.insertCell(5);
                    var isnullElement= document.createElement("input");
                    isnullElement.type = "text";
                    isnullElement.name = "isnullValue["+idx+"]";
                    isnullElement.id = "isnullValue["+idx+"]";
                    isnullElement.value='NAN';
                    isnullElement.className = "myTextbox5";
                    isnullElement.style.width="120px";
                    isnulvalue.appendChild(isnullElement);

                    var cell4 = row.insertCell(6);
                    cell4.align = "center";
                    var lengthElement1 = document.createElement("input");
                    lengthElement1.type = "checkbox";
                    lengthElement1.name = "all["+idx+"]";
                    lengthElement1.id = "all["+idx+"]";
                    lengthElement1.checked="true";
                    // lengthElement1.style.width="";
                    lengthElement1.onchange= function(){
                        if(!lengthElement1.checked){
                            checknotall(row,idx);
                            tabmsg(tabNamet,idx,tabId1);
                            // showpkDefault(tabNamet,document.getElementById('keyValue['+idx+']').options[0].text,idx);
                        }else{
                            checknotalldelete(row,idx,tableID);
                             
                             
                        }
                    }
                    lengthElement1.className="myTextbox5";
                     
                    cell4.appendChild(lengthElement1);

                }else{
                    alert('No more Members to add');
                }
                

            }

            function deleteRow(tableID) {
                try {
                    var table = document.getElementById(tableID);
                    var rowCount = table.rows.length;
                    // alert(rowCount+' in delete ')
                    if(rowCount > 3) {
                        table.deleteRow(rowCount - 1);
                    }

                }catch(e) {
                    alert(e);
                }
            }
            function copyDropDown(){
                var obj=document.getElementById('tableColumn0');
                alert(obj.length);
            }
            function addOption(selectbox,text,value)
            {
                var optn = document.createElement("OPTION");
                optn.text = text;
                optn.value = value;
                selectbox.options.add(optn);
            }
            function goSubmit(){
                var table = document.getElementById('addTable');
                var rowCount = table.rows.length;
                alert(document.getElementById('columnName[0]').value)
                alert('Rows in Table is : '+rowCount);
            }
            function addDenorm(){
                if(document.myForm.isdenom.checked ==true){
                    document.getElementById('headings').style.display='';
                    document.getElementById('normalbutton').style.display='none';

                    document.getElementById('normal').style.display='none';
                    document.getElementById("notall").style.display='none';
                    document.getElementById("all").checked=true;
                }else{
                    document.getElementById('headings').style.display='none';
                    document.getElementById('normalbutton').style.display='';

                    document.getElementById('normal').style.display='';
                }
            }

            function checknotall(rowId,idx){
                document.getElementById('thdesc').style.display='';
                var cell5 = rowId.insertCell(5);
                var lengthElement = document.createElement("select");
                lengthElement.name = "defaultValue["+idx+"]";
                lengthElement.id = "defaultValue["+idx+"]";
                lengthElement.className="myTextbox5";
                lengthElement.style.width="120px"
                cell5.appendChild(lengthElement);
                var obj=document.getElementById('defaultValue[1]');
                for(var selLen=0;selLen<obj.length;selLen++){
                    document.getElementById("defaultValue["+idx+"]").options[selLen] = new Option(document.getElementById("defaultValue[1]").options[selLen].text,document.getElementById("defaultValue[1]").options[selLen].value);
                }

            }
            function checknotalldelete(rowId,idx,tableId){


                var table = document.getElementById(tableId);
                var rcount1 = table.rows.length;
                var chkcheck;
                var c=0;
                var c1=0;
                for(var i=1;i<rcount1-1;i++)
                {

                    // alert('for'+i+document.getElementById('all['+i+']'));
                    if(document.getElementById('all['+i+']').checked==true){
                        c=0;
                    }else{
                        // alert('else');
                        c=1;
                        break;
                    }
                }
                //alert(c)
                if(c==1){
                    document.getElementById('thdesc').style.display='';
                }
                else{
                    document.getElementById('thdesc').style.display='none';
                }
            
                rowId.deleteCell(4);

            }
            function checknotall1(chk,k,tableId){
                // alert('hi'+chk);

                if(chk.checked ==true)
                { 
                    document.getElementById('notall['+k+']').style.display='none';
                    var table = document.getElementById(tableId);
                    var rcount1 = table.rows.length;
                    var chkcheck;
                    var c=0;
                    var c1=0;
                    for(var i=1;i<rcount1-1;i++)
                    {

                        // alert('for'+i+document.getElementById('all['+i+']'));
                        if(document.getElementById('all['+i+']').checked==true){
                            c=0;
                        }else{
                            // alert('else');
                            c=1;
                            break;
                        }
                    }
                    //alert(c)
                    if(c==1){
                        document.getElementById('thdesc').style.display='';
                    }
                    else{
                        document.getElementById('thdesc').style.display='none';
                    }
                } else {
                    
                    document.getElementById('notall['+k+']').style.display='';
                    document.getElementById('thdesc').style.display='';
                }
            }

        </script>
    </body>
</html>

