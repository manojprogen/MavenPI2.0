
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.db.PbDb" %>
<%@page import="java.util.HashMap" %>
<%@page import="prg.business.group.BusinessGroupDAO" %>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String [] oldValuesArray=null;
            ArrayList<String>oldValueidsList=new ArrayList<String>();
            String oldValuesids=request.getParameter("oldValuesids");
            if(!oldValuesids.equalsIgnoreCase("")){
                oldValuesids=oldValuesids.substring(1);
            oldValuesArray=oldValuesids.split(",");
            for(String str:oldValuesArray)
                oldValueidsList.add(str);
                        }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />


        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script>
            var y='';
            var xmlHttp;

            var grpColArray=new Array();
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

            $(function() {

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                }
            );

                $("#sortable1").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
                    }
                }
            );

                $("#sortable2").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable2");
                    }
                }
            );
            });

            function createColumn(elmntId,elementName,tarLoc){

                var parentUL=document.getElementById(tarLoc);
                var x=grpColArray.toString();
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id=elementName+'~'+elmntId;
                    childLI.style.width='180px';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    //cell1.style.backgroundColor="#e6e6e6";
                    var a=document.createElement("a");
                    var deleteElement = elementName+'~'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    // cell2.style.backgroundColor="#e6e6e6";
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
                /*
                 $(".sortable").sortable();
                $(".sortable").disableSelection();
                 */

            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);

                var x=LiObj.id.split("~");
                var i=0;

                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==x[1])
                        grpColArray.splice(i,1);
                }
            }

            function cancelGrpnew()
            {
                parent.cancelGrpnew();
            }



            function saveGrpnew(id1,id2){

                var valListUl=document.getElementById("sortable");
                var valListUlIds=valListUl.getElementsByTagName("li");
                var valList="";
                for(var i=0;i<valListUlIds.length;i++){
                    //  alert(usersIds[i].id);
                    valList=valList+","+valListUlIds[i].id.split("~")[0];

                }
                parent.oldValuesids+=valList
               // alert("oldValuesids\t"+parent.oldValuesids)
                if(valListUlIds.length!=0){

                    valList=valList.substr(1);
                    //alert(valList);
                    parent.savecancelGrpnew(valList,id1,id2);
                }
                else{
                    parent.savecancelGrpnew(valList,id1,id2);

                }

            }

            function checkExistval(orival){
                var orilist=orival.split(";");
                for(var i=0;i<orilist.length;i++){
                    // alert(chkarr[i])
                    grpColArray.push(orilist[i]);
                }

            }

        </script>

    </head>
    <%--<body onload="loadGraphCols('<%=grpId%>')">--%>

    <%
    String parentGrpValQuery = "";
            try {
                String connectionId = request.getParameter("connectionId");
                PbReturnObject memdetPbro = null;
                String id1 = "";
                String id2 = "";
                String colList = "";
                String orival = "";
                String colType = "";
                
                if (connectionId != null) {
                    ////////////////////////////////////////////////////////////////////////////////.println.println("connectionId---" + connectionId);
                    String tableName = request.getParameter("tableName");
                    String colName = request.getParameter("colName");
                    id1 = request.getParameter("id1");
                    id2 = request.getParameter("id2");
                    orival = request.getParameter("orival");
                    colList = request.getParameter("colList");
                    colType = request.getParameter("colType");
                    //////////////////////////////////////////////////////////////.println.println("colType--" + colType);
                    
                    if (colList.equalsIgnoreCase("")) {
                        parentGrpValQuery = "select distinct " + colName + "  from " + tableName + " order by " + colName;
                    } else {
                        String colListarr[] = colList.split(";");
                        String colListNew = "";
                        for (int i = 0; i < colListarr.length; i++) {
                            colListNew += ",'" + colListarr[i] + "'";

                        }
                        if (!colListNew.equalsIgnoreCase("")) {
                            colListNew = colListNew.substring(1);
                        }
                        parentGrpValQuery = "select distinct " + colName + "  from " + tableName + " where " + colName + " not in(" + colListNew + ") order by " + colName;
                    }
                    // ////////////////////////////////////////////////////////////.println.println("parentGrpValQuery===" + parentGrpValQuery);
                    Connection con = new BusinessGroupDAO().getConnectionIdConnection(connectionId);
                    Statement st = con.createStatement();
                    
                    ResultSet rs = st.executeQuery(parentGrpValQuery);
                    memdetPbro = new PbReturnObject(rs);
                }
    %>
    <%if (!orival.equalsIgnoreCase("")) {%>

    <body onload="checkExistval('<%=orival%>')">
        <%} else {%>
    <body>
        <%  }%>
        <br><br>
        <table style="width:90%;height:200px" border="solid black 1px">
            <form name="myForm3" method="post">

                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Columns from below</font></div>
                        <div style="height:190px;overflow-y:auto">
                            <ul id="myList3" class="filetree treeview-famfamfam">

                                <% 
                                for (int i = 0; i < memdetPbro.getRowCount(); i++) {

                                    
                                     
                if (colType.equalsIgnoreCase("DATE")) {
                    String dateval = memdetPbro.getFieldValueDateString(i, 0).replace(",", "-").toUpperCase();
                    String dateval1 = "";
                    dateval1 = String.valueOf(dateval.charAt(0));
                    dateval1 += String.valueOf(dateval.charAt(1));
                    dateval1 += "-" + dateval.substring(2).trim();

                    // ////////////////////////////////////////////////////////////.println.println("dateval--"+dateval1);
                    if (!dateval1.equalsIgnoreCase("")) {
                        if(!oldValueidsList.contains(dateval1)){
                                %>
                                <li><img alt=""  src='icons pinvoke/report.png'/><span class="myDragTabs" class="ui-state-default" id="<%=dateval1%>"><%=dateval1%></span></li>
                                    <%
                        }}
                                    } else {
                                        if (!memdetPbro.getFieldValueString(i, 0).equalsIgnoreCase("")) {
                                            if(!oldValueidsList.contains(memdetPbro.getFieldValueString(i, 0))){
                                    %>
                                <li><img alt=""  src='icons pinvoke/report.png'/><span class="myDragTabs" class="ui-state-default" id="<%=memdetPbro.getFieldValueString(i, 0)%>"><%=memdetPbro.getFieldValueString(i, 0)%></span></li>


                                <%}}
                }
            }
                                %>
                            </ul>
                        </div>
                    </td>



                    <td id="dropTabs" width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Columns to here</font></div>
                        <div style="height:190px;overflow-y:auto">
                            <ul id="sortable">
                                <%if (!orival.equalsIgnoreCase("")) {
                String oriList[] = orival.split(";");
                for (int i = 0; i < oriList.length; i++) {
                                %>

                                <li id="<%=oriList[i]%>~<%=oriList[i]%>" class="navtitle-hover" style="width: 180px; color: white;">
                                    <table id="<%=oriList[i]%>">
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('<%=oriList[i]%>~<%=oriList[i]%>')">a</a>
                                                </td>
                                                <td style="color: black;"><%=oriList[i]%></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </li>
                                <%}

            }%>
                            </ul>
                        </div>
                    </td>

                </tr>
            </form>
        </table>

        <center>
            <table><tr>

                    <td>
                        <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveGrpnew('<%=id1%>','<%=id2%>')">
                    </td>

                    <td>
                        <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelGrpnew()">
                    </td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                </tr>
            </table>
        </center>

        <%} catch (Exception e) {
                e.printStackTrace();
            }

        %>
    </body>
</html>
