<%--
    Document   : pbParamSecurity
    Created on : Dec 29, 2009, 8:40:00 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.Statement,java.sql.Connection,prg.business.group.BusinessGroupDAO,java.sql.SQLException,com.google.common.base.Joiner,prg.db.*,prg.db.PbReturnObject,com.progen.metadata.Cube"%>
<%@page import="com.progen.metadata.MetadataDAO,com.progen.report.display.DisplayParameters,java.util.*,java.sql.ResultSet,com.progen.report.PbReportCollection"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
    <%
                PbDb pbdb = new PbDb();
                String elementId = request.getParameter("elementId");
                String REPORTID = request.getParameter("REPORTID");

                HashMap map = new HashMap();
                Container container = null;
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(REPORTID);
                HashMap ParametersHashMap = (HashMap) container.getParametersHashMap();
                ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                String allFilteredEle = "";

                Iterable<String> paramIds = container.getParameterElements();
                //
                //for (int g = 0; g < Parameters.size(); g++) {
                for(String g : paramIds){
                    allFilteredEle = allFilteredEle + "," + g;
                }
                if (allFilteredEle.length() > 0) {
                    allFilteredEle = allFilteredEle.substring(1);
                }
                HashMap eleMap = new HashMap();
                String allEleInfo = "select * from prg_user_all_info_details where element_id in(" + allFilteredEle + ")";
                PbReturnObject pbro2 = pbdb.execSelectSQL(allEleInfo);
                for (int p = 0; p < pbro2.getRowCount(); p++) {
                    eleMap.put(pbro2.getFieldValueString(p, "MEMBER_ID"), pbro2.getFieldValueString(p, "ELEMENT_ID"));
                }


                String userId = (String) session.getAttribute("USERID");
                //////////////////////////.println(" ParametersHashMap "+ParametersHashMap);
                String UserFolderIds = (String) ParametersHashMap.get("UserFolderIds");
                PbReturnObject filterObj = new PbReturnObject();
                //Get all the secured values for that report if present
                String filterQueryRole = "select * from prg_role_member_filter where folder_id in (" + UserFolderIds + ")";

                String roleFilterQ = "select * from prg_user_role_member_filter where user_id=" + userId + " and folder_id in (" + UserFolderIds + ")";
                String allMEmberSecForRep = "select * from PRG_AR_PARAMETER_SECURITY where report_id=" + REPORTID;

                String otherMembers = "";
                HashMap lMap = new HashMap();
                ArrayList dimElements = new ArrayList();
                HashMap valuesMap = new HashMap();
                HashMap ParentBussCol = new HashMap();
                ArrayList lBusCols = new ArrayList();
                HashMap membersEle = new HashMap();
                HashMap memberFilters = new HashMap();
                ArrayList filterMemIds = new ArrayList();

                filterObj = pbdb.execSelectSQL(filterQueryRole);
                for (int m = 0; m < filterObj.getRowCount(); m++) {
                    String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
                    String prevClause = "";
                    String newClause = "";
                    String totalClause = "";
                    if (memberFilters.containsKey(memId2)) {
                        prevClause = (String) memberFilters.get(memId2);
                        newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                        totalClause = prevClause + "," + newClause;
                    } else {
                        memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                        filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
                    }
                    if (eleMap.containsKey(filterObj.getFieldValueString(m, "MEMBER_ID"))) {
                        String eleId = (String) eleMap.get(filterObj.getFieldValueString(m, "MEMBER_ID"));
                        valuesMap.put(eleId, filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                    }
                    // allFilteredEle=allFilteredEle+","+filterObj.getFieldValueString(m,"ELEMENT_ID");
                }

                filterObj = pbdb.execSelectSQL(roleFilterQ);
                for (int m = 0; m < filterObj.getRowCount(); m++) {
                    String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
                    String prevClause = "";
                    String newClause = "";
                    String totalClause = "";
                    if (memberFilters.containsKey(memId2)) {
                        prevClause = (String) memberFilters.get(memId2);
                        newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                        totalClause = prevClause + "," + newClause;
                    } else {
                        memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                        filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
                    }
                    if (eleMap.containsKey(filterObj.getFieldValueString(m, "MEMBER_ID"))) {
                        String eleId = (String) eleMap.get(filterObj.getFieldValueString(m, "MEMBER_ID"));
                        valuesMap.put(eleId, filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                    } // allFilteredEle=allFilteredEle+","+filterObj.getFieldValueString(m,"ELEMENT_ID");
                }

                filterObj = pbdb.execSelectSQL(allMEmberSecForRep);
                boolean repSecFlag = false;
                for (int m = 0; m < filterObj.getRowCount(); m++) {
                    repSecFlag = true;
                    String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
                    String prevClause = "";
                    String newClause = "";
                    String totalClause = "";
                    if (memberFilters.containsKey(memId2)) {
                        prevClause = (String) memberFilters.get(memId2);
                        newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                        totalClause = prevClause + "," + newClause;
                    }
                    if (!filterObj.getFieldValueString(m, "ELEMENT_ID").equalsIgnoreCase(elementId)) {
                        memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                        filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
                        valuesMap.put(filterObj.getFieldValueString(m, "ELEMENT_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                    }

                }

                String selectedEleDim = "";
                for (int m = 0; m < pbro2.getRowCount(); m++) {
                    String localDim = pbro2.getFieldValueString(m, "DIM_ID");
                    String localEleId = pbro2.getFieldValueString(m, "ELEMENT_ID");
                    if (localEleId.equalsIgnoreCase(elementId)) {
                        selectedEleDim = localDim;
                    }
                    if (!selectedEleDim.equalsIgnoreCase("")) {
                        break;
                    }
                }
                for (int m = 0; m < pbro2.getRowCount(); m++) {
                    String localDim = pbro2.getFieldValueString(m, "DIM_ID");
                    String localEleId = pbro2.getFieldValueString(m, "ELEMENT_ID");
                    if (selectedEleDim.equalsIgnoreCase(localDim)) {
                        dimElements.add(localEleId);
                    }

                }
                for (int p = 0; p < dimElements.size(); p++) {
                    String parId = dimElements.get(p).toString();
                    //if(!elementId.equalsIgnoreCase(parId)){
                    if (valuesMap.containsKey(parId)) {
                        String paramValues = (String) valuesMap.get(parId);
                        //////////////////////////////////////////////////////////////////////////////.println(" paramValues - "+paramValues);
                        if (!paramValues.equalsIgnoreCase("All")) {
                            lMap.put(parId, paramValues);
                            if (ParentBussCol.containsKey(parId)) {
                                String pBussCol = (String) ParentBussCol.get(parId);
                                lBusCols.add(pBussCol);
                            }

                        } else if (membersEle.containsKey(parId)) {
                            String pBussCol = (String) ParentBussCol.get(parId);
                            lBusCols.add(pBussCol);
                            String newSecuredVal = "";
                            String mId = "";
                            mId = membersEle.get(parId).toString();

                            newSecuredVal = memberFilters.get(mId).toString();
                            lMap.put(parId, newSecuredVal);
                        }
                    }
                    //  }
                }
                DisplayParameters dispParam = new DisplayParameters();
                String mainCaluse = "";
                //////////////////////////.println(lMap+" lBusCols "+lBusCols);
                PbReportCollection prc = new PbReportCollection();
                String whereClause = prc.getWhereClause(lMap, lBusCols);
                String eledetsQuery = "select buss_col_name, buss_table_name, connection_id,member_id from prg_user_all_info_details where element_id=" + elementId;
                PbReturnObject pbro = pbdb.execSelectSQL(eledetsQuery);
                String bussTableName = pbro.getFieldValueString(0, 1);
                String bussColName = pbro.getFieldValueString(0, 0);
                String connectionId = String.valueOf(pbro.getFieldValueInt(0, 2));
                String memberId = String.valueOf(pbro.getFieldValueInt(0, 3));

                String elementvalQuery = "select distinct " + bussColName + " from " + bussTableName + "  where " + bussColName + " is not null " + whereClause + " order by 1";
                BusinessGroupDAO busgrpDao = new BusinessGroupDAO();

                Connection con = null;
                Statement st = null;
                ResultSet rs = null;
                PbReturnObject pbroval = null;
                try
                {
                    con = busgrpDao.getConnectionIdConnection(connectionId);
                    st = con.createStatement();
                    rs = st.executeQuery(elementvalQuery);
                    pbroval = new PbReturnObject(rs);
                    rs.close();
                    rs = null;
                    st.close();
                    st = null;
                    con.close();
                    con = null;
                } catch (SQLException se) {}
                finally {
                    try
                    {
                        if ( rs != null )
                            rs.close();
                        if ( st != null )
                            st.close();
                        if ( con != null )
                            con.close();
                     } catch(SQLException se){}
                }
                //  pbroval.writeString();
                //////////////////////////////////////////////////.println("pbroval rowCount--" + pbroval.getRowCount());
                String existparamvalues = "";
                //String eledetsexistsQuery = "select MEMBER_VALUE from PRG_AR_PARAMETER_SECURITY where element_id=" + elementId + " and report_id=" + REPORTID;
                //PbReturnObject pbroexist = pbdb.execSelectSQL(eledetsexistsQuery);

                //if (pbroexist.getRowCount() > 0) {
                //    existparamvalues = String.valueOf(pbroexist.getFieldValueClobString(0, "MEMBER_VALUE"));
                    ////////////////////////////////////////////////.println("existparamvalues---"+existparamvalues);
                //}
                Iterable<String> paramValues = container.getParamSecureValue(elementId);                
                existparamvalues = Joiner.on(",").join(paramValues);
String contextPath=request.getContextPath();
    %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
       
    </head>

    <body onload="checkExist('<%=existparamvalues%>')">
          <form action=""  name="myForm3" method="post" id="myForm3">
<!--        <table style="width:100%;height:270px" border="solid black 1px">


                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Parameter Values from below</font></div>
                        <div style="height:250px;overflow:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">
                                <li  class="open" style="background-image:url('images/treeViewImages/plus.gif')">

                                    <% for (int i = 0; i < pbroval.getRowCount(); i++) {
                                    %>
                                    <ul>
                                        <li class="closed" ><img alt=""  src='icons pinvoke/table.png' /><span class="myDragTabs" class="ui-state-default" id="s~<%=pbroval.getFieldValueString(i, 0)%>"><%=pbroval.getFieldValueString(i, 0)%></span>
                                        </li>
                                    </ul>
                                    <%}%>
                                </li>
                            </ul>
                        </div>
                    </td>

                    <td  id="dropTabs" width="50%" valign="top">

                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Parameter Values to here</font></div>
                        <div style="height:250px;overflow:auto">
                            <ul id="sortable">
                                <%
                                            if (!existparamvalues.equalsIgnoreCase("")) {
                                                String existVals[] = existparamvalues.split(",");
                                                for (int i = 0; i < existVals.length; i++) {
                                %>

                                <li class="navtitle-hover" style="width:auto;height:auto;color:white" id="<%=existVals[i]%>">
                                    <table id="GrpTab<%=existVals[i]%>">
                                        <tr><td style="background-color:#e6e6e6">
                                                <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('<%=existVals[i]%>')"></a>
                                            </td><td style="background-color:#e6e6e6">
                                                <font color="black">  <%=existVals[i]%></font>
                                            </td>
                                        </tr>
                                    </table>
                                </li>

                                <%}

                                            }
                                %>
                            </ul>
                        </div>
                    </td>
                </tr>

        </table>
        <center>
            <br/><br/>
            <input type="button" class="navtitle-hover" style="width:auto" value="Assign Security" onclick="saveparamValues('<%=elementId%>','<%=REPORTID%>','<%=memberId%>','<%=existparamvalues%>')"></center>  
            <div> <font size="2" style="font-weight:bold">Drag values on right to allow only dragged values to be shown.</font></div>-->


    </form>
                    <Table style="width:90%;" align="center">
            <Tr align="center">
                <td align="center" colspan="4">
                    <input type="button" id="saveElement" name="Save" value="Save" class="navtitle-hover" onclick="saveparamValues('<%=elementId%>','<%=REPORTID%>','<%=memberId%>','<%=existparamvalues%>')">
                </td>
            </Tr>
        </Table>
                 <script type="text/javascript" >
            var xmlHttp;
            var columnsList = '';
            var columnsList1 = '';
            var columnId = '';
            var columnName = '';
            var graphDetails = '';
            var thisGraphId = '';
            var grpColArray=new Array();
            $(document).ready(function() {
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimentionMembers&elementId=<%=elementId%>",function(data){
                    var jsonVar=eval('('+data+')')
//                    alert("jsonVar"+jsonVar.htmlStr)
                    $("#myForm3").html("")
                    $("#myForm3").html(jsonVar.htmlStr)
                   
                   
                    isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                    $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

                    $('ul#myList3 li').quicksearch({
                        position: 'before',
                        attached: 'ul#myList3',
                        loaderText: '',
                        delay: 100
                    })
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
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
//                    grpColArray=new Array
//                    $("#myList3 li span").each(function(){
//                        if($(this).html()!="")
//                            grpColArray.push($(this).html())
//                    })

                });
               
               
             

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


//            function deleteColumn(index){
////                alert(index)
//                var LiObj=document.getElementById(index);
//                var parentUL=document.getElementById("sortable");
//                parentUL.removeChild(LiObj);
//
//                //var x=LiObj.id.split("~");
//                //var x=LiObj.id.replace("GrpCol","");
//                var x=index;
//                //index.replace("GrpCol","");
//                var i=0;
//
//                for(i=0;i<grpColArray.length;i++){
////                    alert(grpColArray[i]+"=="+x)
//                    if(grpColArray[i]==x)
//                        grpColArray.splice(i,1);
//                }
//            }
//            function createColumn(elmntId,elementName,tarLoc){
//                elmntId=elmntId.split("~")[1];
//                var parentUL=document.getElementById(tarLoc);
//                var x=grpColArray.toString();
//                if(x.match(elmntId)==null){
//                    grpColArray.push(elmntId);
//
//                    var childLI=document.createElement("li");
//                    childLI.id=elmntId;
//                    //childLI.style.width='180px';
//                    childLI.style.width='auto';
//                    childLI.style.height='auto';
//                    childLI.style.color='white';
//                    childLI.className='navtitle-hover';
//                    var table=document.createElement("table");
//                    table.id="GrpTab"+elmntId;
//                    var row=table.insertRow(0);
//                    var cell1=row.insertCell(0);
//                    //cell1.style.backgroundColor="#e6e6e6";
//                    var a=document.createElement("a");
//                    var deleteElement = elmntId;
//                    a.href="javascript:deleteColumn('"+deleteElement+"')";
//                    a.innerHTML="a";
//                    a.className="ui-icon ui-icon-close";
//                    cell1.appendChild(a);
//                    var cell2=row.insertCell(1);
//                    // cell2.style.backgroundColor="#e6e6e6";
//                    cell2.style.color='black';
//                    cell2.innerHTML=elementName;
//                    childLI.appendChild(table);
//                    parentUL.appendChild(childLI);
//                }
//
//                /*
//                 $(".sortable").sortable();
//                $(".sortable").disableSelection();
//                 */
//
//            }


            function saveparamValues(elementId,REPORTID,memberId,existparamvalues){ 
//                alert('memberId is : '+memberId)
                var cols="";
                var colIdsvar = "";
                var colsUl=document.getElementById("sortable"); 

                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                           colIdsvar=(colIds[i].id).split("~");
                           cols = cols+","+colIdsvar[0].replace("_li", "", "gi");
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                        }
                        
                        var paramSecurityConfirm=confirm("Parameter Security is Setting");
                        if(paramSecurityConfirm==true){
//                            if(existparamvalues==''){
//                                alert('in if of existparamvalues is null')
                                $.ajax({
                                    url: 'reportTemplateAction.do?templateParam=saveParamSecurity&elementId='+elementId+'&paramValues='+cols+"&REPORTID="+REPORTID+"&memberId="+memberId,
                                    success: function(data){
                                        parent.cancelParamSecurity();
                                    }
                                });
//                            }else{
////                             alert('in else of existparamvalues is null')
//                                $.ajax({
//                                    url: 'reportTemplateAction.do?templateParam=updateParamSecurity&elementId='+elementId+'&paramValues='+cols+"&REPORTID="+REPORTID+"&memberId="+memberId,
//                                    success: function(data){
//                                        parent.cancelParamSecurity();
//                                    }
//                                });
//                            }
                        }else{
                            parent.cancelParamSecurity();
                        }
                }

                else{
                var paramSecurityDelConfirm=confirm("Parameter Security is Removing");
                if(paramSecurityDelConfirm==true){
//                    alert("Please Select Parameter Values");
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=delParamSecurity&elementId='+elementId+'&REPORTID='+REPORTID,
                        success: function(data){
                            parent.cancelParamSecurity();
                        }
                    });
                }else{
                    parent.cancelParamSecurity();
                }
                }
                }
            }

            function checkExist(chk){
                var chkarr=chk.split(",");
                //   alert('else')
                for(var i=0;i<chkarr.length;i++){
                    // alert(chkarr[i])
                    grpColArray.push(chkarr[i]);
                }
            }



        </script>
</body>
</html>
