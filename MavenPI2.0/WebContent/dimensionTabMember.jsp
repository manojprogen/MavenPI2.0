<%--
    Document   : dimensionTabMember
    Created on : 16 Oct, 2012, 5:40:31 PM
    Author     : arun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,utils.db.ProgenConnection,utils.db.*,java.sql.*,prg.db.PbDb,com.progen.datadisplay.db.PbDataDisplayBeanDb"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

<%
                            Connection conc=null;

                        // Connection con, conc = null;
                        Statement st, stc, st1 = null;
                        // ResultSet rs, rs1, rsc = null;
                        PbDb pbDb = new PbDb();
                        // con = ProgenConnection.getInstance().getConnection();
                        //  st = con.createStatement();
                        PbReturnObject pbrors = new PbReturnObject();
                        PbReturnObject pbrors1 = new PbReturnObject();
                            String dimId = "";

                        String dimtabId = "";
                        String colName = "";
                        String is_pk = "";
                        String tableId = "";
                        String tabName = "";
                        String sql="";
        String listarr[] = request.getParameter("colId").split(",");
        String flag = request.getParameter("flag");
                        dimtabId = listarr[0];
                        tableId = listarr[1];
                        dimId = listarr[2];
                        tabName = listarr[3];
                        if(flag!=null)
                            {
                            sql = "select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id=" + dimtabId + " and d.is_available='Y' and m.column_id not in (select distinct COL_ID from PRG_QRY_DIM_MEMBER_DETAILS where MEM_ID in(select MEMBER_ID from prg_qry_dim_member where DIM_ID=" + dimId + " and DIM_TAB_ID="+dimtabId+")) order by d.is_pk_key desc,m.table_col_name ASC";
                            String sql1 = "select MEMBER_ID,MEMBER_NAME from prg_qry_dim_member where DIM_ID=" + dimId + " and DIM_TAB_ID="+dimtabId;
                         pbrors1 = pbDb.execSelectSQL(sql1);
                            }
                        else{
                          sql = "select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id=" + dimtabId + " and d.is_available='Y' order by d.is_pk_key desc,m.table_col_name ASC";
                        }pbrors = pbDb.execSelectSQL(sql);
                        int addMoreCount = pbrors.getRowCount();
                            %>
       

        <style>
            .myhead
            {
                font-family: Verdana;
                font-size: 8pt;
                font-weight: bold;
                padding-left:12px;
                background-color:#b4d9ee;
                width:50%;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
             *{
                margin:         0px;
                padding:        0px;
                font:           11px verdana;
            }

        </style>
    </head>
    <body>
        <center>
            <table width="100%" id="head">
                 <input type="hidden" id="cols" >
                <tr>
                    <th><font style="font-family:verdana;font-size:12px;color:#369">Columns List (Drag Columns to Right and click Save)</font></th>
                    <%
                        if(flag==null){
                    %>
                    <th><input type="button" class="navtitle-hover" style="width:auto" value="Move All" onclick="moveAllColumn()"></th>
                    <%}%>
                </tr>
            </table>
        </center>
        <form name="myForm">
            <div id="main">
            <table align="center" border="1px" width="100%" style="height:100%">
                <tr>
                    <td id="dragtab"class="myhead" style="width:50%" valign="top">
                        <table style="height:100%" border="0">
                            
                     <% for (int l = 0; l < pbrors.getRowCount(); l++) { %>
                            <tr>
                            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                                <td height="5px" class="myDragTabs" valign="top" style="font-weight:normal"  id="<%=pbrors.getFieldValueInt(l, 0)%>"><%=pbrors.getFieldValueString(l, 1)%></td>
                            </tr>
                           <%}%>
                        </table >
                    </td>
                    <td id="dropTabs" style="width:50%" class="myhead" valign="top">
                    <%
                    if(flag!=null){
                    %>


                    <table>
                     <% for (int l = 0; l < pbrors1.getRowCount(); l++) { %>
                            <tr>
                                <td  valign="top" height="5px" style="font-weight:normal"  id="<%=pbrors1.getFieldValueInt(l, 0)%>"><%=pbrors1.getFieldValueString(l, 1)%></td>
                            </tr>
                           <%}%>
                           <tr>
                               <td  class="myhead" valign="top">

                    </td>
                           </tr>
                        </table >



                    <%}
                       %>
                    </td>
                </tr>
        </table>
        <br><center>
            <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="saveColumn()">&nbsp;<input type="button" class="navtitle-hover" style="width:auto"  value="Cancel" onclick="cancelWizard()">
        </center>
        </div>


</form>
                     <script type="text/javascript">
            var y="";
            var xmlHttp;
            var ctxPath;
            var colid='<%=request.getParameter("colId")%>'
            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
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

                    var output1=xmlHttp.responseText
                }
            }

            $(function() {

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });


                $("#dropTabs").droppable(
                {
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',

                    drop: function(ev, ui) {
                        /*   for(a in ui.draggable){
                    alert(ui.draggable.html());
                    alert(a)
                }*/
                        //$draggableTables.html(ui.draggable.html());
                        var x=$("#dropTabs").html();
                        y = y+ui.draggable.attr('id')+",";
                        // alert("y is "+y)
                        // alert("x is "+x)

                        $("#dropTabs").html(x+ui.draggable.html()+"<br>");
                        var z = ui.draggable.attr('id');
                        //alert("z is "+z)
                        document.getElementById(z).style.display='none';
                    }

                }
            );

            });
            function moveAllColumn()
            {
                var x1=$("#dropTabs").html();
               var x=$("#dragtab").html();
               $("#dropTabs").html(x);
               $("#dragtab").html(x1);
               <% for (int l = 0; l < pbrors.getRowCount(); l++) { %>
                       y=y+<%=pbrors.getFieldValueInt(l, 0)%>+",";
                       <%}%>
            }
            function saveColumn()
             { 
                 var s;
                 var f=parent.document.getElementById('dataDispmem');
                 var flag=<%=flag%>;
                 //alert(flag);
                 if(flag!=null )
                     {//alert("addcolumnwizard")
                       s="createMember.jsp?cols="+y+"&colId="+colid+"&flag=true";
                     }
                     else {
                         //alert("addmemberwizard")
                 s="createMember.jsp?cols="+y+"&colId="+colid;}
                f.src=s;
               parent.$("#tabmemberDiv").dialog('close');
              parent.$("#dataDispmemDiv").dialog('open');


            }
            function cancelWizard()
            {
              parent.$("#tabmemberDiv").dialog('close');
            }


        </script>
    </body>
</html>
