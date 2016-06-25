<%@page import="java.util.*" %>
<%@page import="java.sql.*" %>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<HTML>
    <HEAD>
        <%--
		<script type="text/javascript" src="tabs/js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="tabs/js/jquery-ui-1.7.2.custom.min.js"></script>
                <script type="text/javascript" src="javascript/ui.tabs.js"></script>
                <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
                 <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        --%>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
            $(function(){



                // Tabs
                //$('#tabs').tabs();

                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});


                // Dialog

                $('#dialog').dialog({
                    autoOpen: false,
                    width: 600,
                    buttons: {
                        "Cancel": function() {
                            $(this).dialog("close");
                        },
                        "Ok": function() {
                            $(this).dialog("close");
                        }

                    }
                });

                // Dialog Link
                $('#dialog_link').click(function(){
                    $('#dialog').dialog('open');
                    return false;
                });

                // Datepicker
                $('#datepicker').datepicker({
                    inline: true
                });

                // Slider
                $('#slider').slider({
                    range: true,
                    values: [17, 67]
                });

                // Progressbar
                $("#progressbar").progressbar({
                    value: 20
                });

                //hover states on the static widgets
                $('#dialog_link, ul#icons li').hover(
                function() { $(this).addClass('ui-state-hover'); },
                function() { $(this).removeClass('ui-state-hover'); }
            );

            });

            function disableButton()
            {
                document.getElementById('btnn').disabled = false;

                // alert(document.getElementById('btnn').disabled)
            }
            function saveTables()
            {
                document.myForm.action = "saveFolderTables.do";
                document.myForm.submit();
            <%--alert("Successfully Updated")--%>
                    window.location.reload(true);
                    parent.parentCancelTablePropertiesfolder();
                }

                function cancelBuckets()
                {
                    //alert("kkk");
                    parent.parentCancelTablePropertiesfolder();
                }

                function saveColsfolder()
                {
                    // alert('Ghdh');
                    document.columnForm.action = "UpdateTableColumns.do";
                    document.columnForm.submit();
                    alert('Successfully Updated');
                    parent.parentCancelTablePropertiesfolder();
                }
        </script>
        <style>
            .myhead
            {
                font-family: Verdana;
                font-size: 8pt;
                font-weight: bold;
                color: #000;
                padding-left:12px;
                width:50%;
                background-color:#b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
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
    </HEAD>

    <BODY>

        <%
                    int table_id = 0;
                    ArrayList sources = null;
                     String table_tooltip_name="";
                      String table_disp_name="";
                      String table_name ="";
                       String table_description="";
                       ResultSet rs =null;
                        String sourceslines = "";
                    try {
                        table_name = (String) request.getAttribute("table_name");
                         table_description = (String) request.getAttribute("table_description");
   //added for table_disp_name and table_tooltip_name
                        table_disp_name = (String) request.getAttribute("table_disp_name");
                        table_tooltip_name = (String) request.getAttribute("table_tooltip_name");
                        sources = (ArrayList) session.getAttribute("sources");
                        if(request.getAttribute("table_id")!=null){
                          table_id = ((Integer) request.getAttribute("table_id")).intValue();
                        }
                      if(request.getAttribute("columndetails")!=null){
                       rs = (ResultSet) request.getAttribute("columndetails");
                      }
                       

                        for (int i = 0; i < sources.size(); i++) {
                            sourceslines = sourceslines + (String) sources.get(i) + "\n";
                        }




        %>

        <!-- Tabs -->
        <center>
            <div id="tabs" style="width:94%">
                <ul>
                    <li><a href="#tables">Tables</a></li>
                    <li><a href="#columns">Columns</a></li>

                </ul>
                <div id="tables">
                    <!-- Add Tables Related Description Here -->
                    <!--1) Table Name-->
                    <form name="myForm">
                        <table border="0" style="border-width:thin;width:90%">
                            <tr>
                                <td width="50%" class="myHead" align="center">
                                    Table Name
                                </td>
                                <td width="50%" class="myHead">
                                    <%=table_name%>
                                </td>
                            </tr>
                        </table>

                        <!--2) Table Sources-->
                        <input type="hidden" name="tableid" value="<%=table_id%>">


                        <!--3) Joins-->



                        <!--4) Description-->

                        <table border="0" style="border-width:thin;width:90%" >
                            <tr>
                                <td class="myHead" align="center" width="99%">
                                    Display Name
                                </td>
                                <td width="99%">
                                    <input type="text" style="width:100%" name="tabDispName" value="<%=table_disp_name%>">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" align="center" width="99%">
                                    Tooltip Name
                                </td>
                                <td width="100%">
                                    <input type="text" style="width:100%" name="tabTooltip" value="<%=table_tooltip_name%>">
                                </td>
                            </tr>
                        </table>
                        <table border="0" style="border-width:thin;width:90%" >
                            <tr>
                                <td class="myHead" align="center" width="99%">
                                    Description
                                </td>
                            </tr>
                            <tr>
                                <td height="100px">
                                    <textarea name="tabledesc" style="height:100px;width:100%;overflow:auto" <%--onkeyup="disableButton()"--%>><%=table_description%></textarea>
                                </td>
                            </tr>
                        </table>
                    </form>
                    <center><input type="submit" value="Save" id="btnn"class="navtitle-hover" style="width:auto" onclick="saveTables()"></center>
                </div>
                <div id="columns" style="width:96%">
                    <form name="columnForm">
                        <table style="border-width:thin;height:400px" border="0" width="96%" >
                            <tr>
                                <th class="myHead">Column Name</th>
                                <th class="myHead">Display Name</th>
                                <th class="myHead">Column Type</th>

                            </tr>

                            <%
                                     if(rs!=null){
                                            while (rs.next()) {

                            %>

                            <tr>
                                <td style="border-width:thin"><%=rs.getString(2)%></td>
                                <%-- modified by susheela start--%>
                                <%String desc = rs.getString(5);
                                                        if (desc == null) {
                                                            desc = "";
                                                        }
                                %>
                                <td><input type="text" name="<%=rs.getString(1)%>" value="<%=desc%>"></td>
                                    <%-- modified by susheela over--%>
                                <td><%=rs.getString(4)%></td>

                            </tr>

                            <%}}%>
                        </table>
                        <br>
                        <input type="hidden" name="tableid" value="<%=table_id%>">
                        <center><input type="button" class="navtitle-hover" style="width:auto" name="saveColumns" value="Save" onclick="saveColsfolder()"></center>

                    </form>
                </div>

            </div>
        </center>

        <%} catch (Exception e) {
                                    e.printStackTrace();
                                }%>
        <br>
    </BODY>
</HTML>
