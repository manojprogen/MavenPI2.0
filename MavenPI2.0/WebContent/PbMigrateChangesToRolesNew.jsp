<%@page import="prg.db.PbReturnObject"%>
<%@page import="prg.business.group.PbBusinessGroupEditDAO"%>
<%@page import="java.util.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% String contextPath= request.getContextPath();%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
    <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
    <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

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
    <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>


<!--    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
    <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
    <title>JSP Page</title>

    <style type="text/css">
/*    added by krishan*/
      .loading_image{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width:900%;
                height: 82%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                z-index:1001;
                overflow:auto;
            }
</style>
    

</head>
<body>
<Center>
    <br>
    <center><b style="color:#369;font-size:11px;font-family:verdana;">Check Business Role And Dimension To Be added.</b></center>
    <br>
    <Form name="myForm">

        <%
        String grp = request.getParameter("grp");

        //Get Folder and Folder Name
        PbBusinessGroupEditDAO bgDao = new PbBusinessGroupEditDAO();
        PbReturnObject all = bgDao.getGroupRoles(grp);
        PbReturnObject pbro = (PbReturnObject) all.getObject("grpRoleObj");//heck of a logic
        
        String allRoleIds = "";
        String allRoleNames = "";
        for (int i = 0; i < pbro.getRowCount(); i++) {
            allRoleIds += "*u," + pbro.getFieldValueInt(i, 0) + "~" + pbro.getFieldValueString(i, 1);
            allRoleNames += "*" + pbro.getFieldValueString(i, 1);
        }
        if (pbro.getRowCount() > 0) {
            allRoleIds = allRoleIds.substring(1);
            allRoleNames = allRoleNames.substring(1);
        }

        //Find out Newly Added Dimension
        HashMap allMap = bgDao.getGroupRoleExtraDims(grp);
        HashMap Dim = (HashMap) allMap.get("Dim");
        String allDims = "";
        String allFolderIds = "";

        //Find out newly added Facts
        HashMap allMap1 = bgDao.getGroupRoleExtraFacts(grp);
        
        HashMap Facts = (HashMap) allMap.get("Facts");
        
        String allFacts = "";
        HashMap allMap2 = bgDao.getGroupRoleExtraFactsCols(grp);
        
        HashMap FactCols = (HashMap) allMap2.get("FactCols");
        
        HashMap extraFacts = (HashMap) allMap2.get("Facts");
        HashMap extraFactsForRole = (HashMap) allMap2.get("extraFactsForRole");
        ////////////////////////////.println("extraFacts==" + extraFacts);
        String allFactscols = "";

        for (int i = 0; i < pbro.getRowCount(); i++) {
            String folderId = pbro.getFieldValueString(i, 0);
            allFolderIds = allFolderIds + "," + folderId;
        }
        if (!allFolderIds.equalsIgnoreCase("")) {

            allFolderIds = allFolderIds.substring(1);
        }
        %>
        <table align="center" border="1" width="100%" style="height:200px" valign="top">
            <tr>
                <td>Dimensions</td>
                <td>Extra Columns In Facts</td>

            </tr>
            <tr>
            <td   valign="top">
                <Center>
                    <div style="height:200Px;width:100%;overflow-y:auto">
                        <table  border="0">

                            <%for (int i = 0; i < pbro.getRowCount(); i++) {
            String folderId = pbro.getFieldValueString(i, 0);

            String isDisabled = "";
            String message = "";
            PbReturnObject folderDim = new PbReturnObject();
            if (Dim.containsKey(folderId)) {
                folderDim = (PbReturnObject) Dim.get(folderId);
            }
            if (folderDim.getRowCount() == 0) {
                isDisabled = "disabled";
                message = "No extra dimensions in Business Group";
            }
                            %>
                            <%--<tr><td class="myDragTabs" class="ui-state-default3" style="width:200px" id="u,<%=pbro.getFieldValueInt(i,0)%>~<%=pbro.getFieldValueString(i,1)%>"><font style="color:black;font-family:verdana;font-size:12px"><%=pbro.getFieldValueString(i,1)%></font></td></tr>--%>
                            <Tr><Td><%=message%></Td></Tr>
                            <Tr><Td>
                                    <Table>
                                        <Tr><Td>
                                            <Input type="CHECKBOX" <%=isDisabled%> id="<%=pbro.getFieldValueString(i, 0)%>" value="<%=pbro.getFieldValueString(i, 0)%>" name="<%=pbro.getFieldValueString(i, 0)%>"></Td>
                                            <Td STYLE="font-weight:bold"><%=pbro.getFieldValueString(i, 1)%> &nbsp;&nbsp;--Role
                                            </Td>
                                        </Tr>
                                        <%for (int h = 0; h < folderDim.getRowCount(); h++) {
        if (!folderDim.getFieldValueString(h, "DIM_NAME").equalsIgnoreCase("Time")) {
            allDims = allDims + "," + folderId + ":" + folderDim.getFieldValueString(h, "DIM_ID");
                                        %>
                                        <Tr>
                                            <Td><Input type="CHECKBOX" ONCLICK="changeDim('<%=folderId%>:<%=folderDim.getFieldValueString(h, "DIM_ID")%>')" id="<%=folderId%>:<%=folderDim.getFieldValueString(h, "DIM_ID")%>" value="<%=folderId%>:<%=folderDim.getFieldValueString(h, "DIM_ID")%>" name="<%=folderId%>:<%=folderDim.getFieldValueString(h, "DIM_ID")%>"></Td>

                                            <Td><%=folderDim.getFieldValueString(h, "DIM_NAME")%> &nbsp;&nbsp;&nbsp;&nbsp;-->Dimension</Td>
                                        </Tr>
                                        <%}
    }%>
                            </Table></Td></Tr><Tr></Tr>
                            <%}
        if (allDims.length() > 0) {
            allDims = allDims.substring(1);
        }

                            %>
                            <td>

                        </table >
                    </div>
                </Center>
            </td>
            <%--    <td valign="top">
                        <Center>
                        <div style="height:100Px;width:100%;overflow-y:auto">
                        <table  border="0">

                            <%for(int i=0;i<pbro.getRowCount();i++){
                            String folderId=pbro.getFieldValueString(i,0);
                            allFolderIds=allFolderIds+","+folderId;
                            String isDisabled="";
                            String message="";
                            PbReturnObject folderFact=new PbReturnObject();
                            if(Facts.containsKey(folderId))
                                folderFact=(PbReturnObject)Facts.get(folderId);
                            if(folderFact.getRowCount()==0){
                                isDisabled="disabled";
                                message="No extra Facts in Business Group";
                                }
                                %>

                               <Tr><Td><%=message%></Td></Tr>
                               <Tr><Td>
                                   <Table>
                                    <Tr><Td>
                                      <Input type="CHECKBOX" <%=isDisabled%> id="<%=pbro.getFieldValueString(i,0)%>" value="<%=pbro.getFieldValueString(i,0)%>" name="<%=pbro.getFieldValueString(i,0)%>"></Td>
                                      <Td STYLE="font-weight:bold"><%=pbro.getFieldValueString(i,1)%>
                                      </Td>
                               </Tr>
                               <%for(int h=0;h<folderFact.getRowCount();h++){

                                    allFacts=allFacts+","+folderId+":"+folderFact.getFieldValueInt(h,"BUSS_TABLE_ID");
                                                                     %>
                                            <Tr>
                                                <Td><Input type="CHECKBOX" id="<%=folderId%>:<%=folderFact.getFieldValueString(h,"BUSS_TABLE_ID")%>" value="<%=folderId%>:<%=folderFact.getFieldValueString(h,"BUSS_TABLE_ID")%>" name="<%=folderId%>:<%=folderFact.getFieldValueString(h,"BUSS_TABLE_ID")%>"></Td>

                                                <Td><%=folderFact.getFieldValueString(h,"BUSS_TABLE_NAME")%></Td>
                                            </Tr>
                                   <%}%>
                                   </Table>

                                   </Td></Tr><Tr></Tr>
                               <%}
                                 if(allFacts.length()>0)
                                     allFacts=allFacts.substring(1);
                                    if(allFolderIds.length()>0)
                                    allFolderIds=allFolderIds.substring(1);

                                %>

                        </table >
                        </div>
                        </Center>
            </td>--%>
            <td valign="top">

                            <%-- <div style="height:200px;width:100%;overflow-y:auto">--%>
            <table  border="0">

                        <%String allFactsofCols = "";
        for (int i = 0; i < pbro.getRowCount(); i++) {
            String folderId = pbro.getFieldValueString(i, 0);
            ////////////////////////////.println(" folderId " + folderId);
            ArrayList al = new ArrayList();
            if (extraFactsForRole.containsKey(folderId)) {
                al = (ArrayList) extraFactsForRole.get(folderId);
            }

            String isDisabled = "";
            String message = "";
            boolean showColsFlag = true;
            PbReturnObject folderFact = new PbReturnObject();
            if (extraFacts.containsKey(folderId)) {
                folderFact = (PbReturnObject) extraFacts.get(folderId);
            }
            if (folderFact.getRowCount() == 0) {
                isDisabled = "disabled";
                message = "No extra Fact Columns in Business Group";
                        %>
                        <Tr><Td><%=message%></Td></Tr>
                                <%
                                    } else {
                                %>



                                <Tr><Td></Td><Td>&nbsp;</Td></Tr>
                        <Tr><Td>
                                        <Input type="CHECKBOX" <%=isDisabled%> id="FC<%=pbro.getFieldValueString(i, 0)%>" value="<%=pbro.getFieldValueString(i, 0)%>" name="FC<%=pbro.getFieldValueString(i, 0)%>"></Td>
                                        <Td STYLE="font-weight:bold"><%=pbro.getFieldValueString(i, 1)%> &nbsp;&nbsp;&nbsp;&nbsp;-->Role
                                        </Td>
                                    </Tr>

                                <%

                                        for (int h = 0; h < folderFact.getRowCount(); h++) {
                                            if (al.contains(folderFact.getFieldValueString(h, "BUSS_TABLE_ID"))) {
                                %>
                                    <Tr>
                                    <%
                                allFactsofCols = allFactsofCols + "," + folderId + ":" + folderFact.getFieldValueString(h, "BUSS_TABLE_ID");
                                        %>

                                        <Td><Input type="CHECKBOX" id="FC<%=folderId%>:<%=folderFact.getFieldValueString(h, "BUSS_TABLE_ID")%>" value="<%=folderId%>:<%=folderFact.getFieldValueString(h, "BUSS_TABLE_ID")%>" name="FC<%=folderId%>:<%=folderFact.getFieldValueString(h, "BUSS_TABLE_ID")%>"></Td>

                                    <Td STYLE="font-weight:bold"><%=folderFact.getFieldValueString(h, "BUSS_TABLE_NAME")%> &nbsp;&nbsp;&nbsp;&nbsp;-->Fact</Td>
                                    <% }
    PbReturnObject folderFactcol = new PbReturnObject();
    if (FactCols.containsKey(folderId + "-" + folderFact.getFieldValueInt(h, "BUSS_TABLE_ID"))) {
        folderFactcol = (PbReturnObject) FactCols.get(folderId + "-" + folderFact.getFieldValueInt(h, "BUSS_TABLE_ID"));
    }

                                        %>

                                    <%
                                                                                 ////////////////////////////.println(" folderFactcol " + folderFactcol.getRowCount());
                                                                                 ArrayList colName = new ArrayList();
                                                                                 ////////////////////////////.println(showColsFlag+" al "+al+" ---= "+folderFact.getFieldValueString(h, "BUSS_TABLE_ID")+". "+folderFact.getFieldValueString(h, "BUSS_TABLE_NAME"));
                                                                                 if (!al.contains(folderFact.getFieldValueString(h,"BUSS_TABLE_ID"))) {
                                                                                     %>

                                                                                     <%
                                                                                     for (int k = 0; k < folderFactcol.getRowCount(); k++) {
                                                                                         if(k==0){
                                                                                     %>
                                                                                       <Tr><Td></Td><Td><%=folderFact.getFieldValueString(h, "BUSS_TABLE_NAME")%></Td></Tr>
                                                                                     <%
                                                                                             }
                                                                                         %>

                                                                                         <%
        allFactscols = allFactscols + "," + folderId + ":" + folderFact.getFieldValueInt(h, "BUSS_TABLE_ID") + "-" + folderFactcol.getFieldValueString(k, "BUSS_COLUMN_ID");
                                            %>
                                            <tr>

                                            <Td  STYLE="width:20px"><Input type="CHECKBOX" ONCHANGE="checkTabRole('<%=folderId%>:<%=folderFact.getFieldValueString(h, "BUSS_TABLE_ID")%>-<%=folderFactcol.getFieldValueString(k, "BUSS_COLUMN_ID")%>')" id="<%=folderId%>:<%=folderFact.getFieldValueString(h, "BUSS_TABLE_ID")%>-<%=folderFactcol.getFieldValueString(k, "BUSS_COLUMN_ID")%>" value="<%=folderId%>:<%=folderFact.getFieldValueString(h, "BUSS_TABLE_ID")%>-<%=folderFactcol.getFieldValueString(k, "BUSS_COLUMN_ID")%>" name="<%=folderId%>:<%=folderFact.getFieldValueString(h, "BUSS_TABLE_ID")%>-<%=folderFactcol.getFieldValueString(k, "BUSS_COLUMN_ID")%>"></Td>

                                            <Td><%=folderFactcol.getFieldValueString(k, "COLUMN_NAME")%> &nbsp;&nbsp;&nbsp;&nbsp;-->Column</Td>

                                            </tr>
                                    <%}
                                                                                 }
                                                                                 %>
                                    </Tr>
                                <%
                                                                            %>


                                <% }
            }
        }
        if (allFactscols.length() > 0) {
            allFactscols = allFactscols.substring(1);
        }
        if (allFactsofCols.length() > 0) {
            allFactsofCols = allFactsofCols.substring(1);
        }
                                ////////////////////////////.println(" allFactscols "+allFactscols);
                                ////////////////////////////.println(" allFactsofCols "+allFactsofCols);

                        %>
                            </table>
                    </td></tr>

        </table >

    <INPUT type="HIDDEN" name="allFolderIds" id="allFolderIds" value="<%=allFolderIds%>">
    <INPUT type="HIDDEN" name="allDims" id="allDims" value="<%=allDims%>">
    <INPUT type="HIDDEN" name="allFactscols" id="allFactscols" value="<%=allFactscols%>">
    <INPUT type="HIDDEN" name="allFactsofCols" id="allFactsofCols" value="<%=allFactsofCols%>">

    <INPUT type="HIDDEN" name="grp" id="grp" value="<%=grp%>">
    <Table>
        <Tr>
            <Td align="center"><Input TYPE="button" value="Save" onclick="saveMigration()" class="navtitle-hover"></Td>
                       <%-- <Td align="center"><Input TYPE="button" value="Save And Publish" ONCLICK="saveMigrationPublish()"></Td> --%>
            <Td align="center"><Input TYPE="button" value="Cancel" onclick="cancelMigration()" class="navtitle-hover"></Td>
        </Tr>
    </Table>
    <INPUT type="hidden" name="grp" id="grp" value="<%=grp%>">
</Form>
</Center>
<!--added by krishan-->
 <div id="loading" class="loading_image">
            <img id="imgId" src="images/help-loading.gif"  border="0px" style="position:absolute;left:150px;top:0px">
 </div>
<script type="text/javascript">
        function changeDim(val)
        {
           //alert('val=-=- '+val)
           var all=val.split(":");
           var dimId="";
           dimId=all[1];
           var roleId=all[0];
           if(document.getElementById(roleId+":"+dimId).checked==true)
                {
                    document.getElementById(roleId).checked=true;
                }
                else
                {
                    document.getElementById(roleId).checked=false;
                }
        }
        function checkTabRole(val)
        {
            var allV=val.split(":");
            var roleId=allV[0];
            var tabCol=allV[1];
            var v=tabCol.split("-");
            var tabId=v[0];
            if(document.getElementById(val).checked==true)
                {
                    //alert(' in ijjjj ');//document.getElementById("parameters2").checked=false;
                    document.getElementById("FC"+roleId).checked=true;
                    document.getElementById("FC"+roleId+":"+tabId).checked=true;
                }
                else
                {
                     document.getElementById("FC"+roleId).checked=false;
                     document.getElementById("FC"+roleId+":"+tabId).checked=false;
                }

        }

        var y='';
        var msrArray=new Array();
        var xmlHttp;
        $(document).ready(function() {
            $("#myList3").treeview({
                animated:"slow",
                persist: "cookie"
            });
        });

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


                    createColumn(ui.draggable.attr('id'),ui.draggable.html());

                }

            }
        );

        });

        function deleteColumn(index){
            var index1=index.split(",")[1];
            // alert(index1)
            var LiObj=document.getElementById(index1);
            // alert('ined '+index);
            var parentUL=document.getElementById("sortable");

            parentUL.removeChild(LiObj);
            var l=msrArray.indexOf(index);
            //alert(l)
            msrArray.splice(l,1);
            // alert('mrrarr  '+msrArray)
        }

        function createColumn(elmntId,elementName){

            var parentUL=document.getElementById("sortable");

            var x=msrArray.toString();
            // alert(elmntId+'   '+x.match(elmntId));
            if(x.match(elmntId)==null){
                msrArray.push(elmntId)

                var childLI=document.createElement("li");
                var uid=elmntId.split(",");
                childLI.id=uid[1];
                childLI.style.width='150px';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                var table=document.createElement("table");
                table.id=elmntId;
                var row=table.insertRow(0);
                var cell1=row.insertCell(0);
                //cell1.style.backgroundColor="#e6e6e6";

                var a=document.createElement("a");
                a.href="javascript:deleteColumn('"+elmntId+"')";
                a.innerHTML="a";
                a.className="ui-icon ui-icon-close";
                cell1.appendChild(a);
                var cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                cell2.style.color='black';
                cell2.innerHTML=elementName;
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }else
            {
                // alert('This user is already added')
            }
            $("#sortable").sortable();
            $("#sortable").disableSelection();
        }

        function cancelMigration()
        {
            parent.cancelMigrationToRole();
        }
        function moveAll(allUsers,allNames)
        {
            var allList=allUsers.split("*");
            var allNamesList=allNames.split("*");
            for(var i=0;i<allList.length;i++)
            {
                createColumn(allList[i],allNamesList[i]);
            }
        }
        function deleteAll(){
            msrArray.splice(0,msrArray.length);
            var parentUL=document.getElementById("sortable");
            parentUL.innerHTML='';

        }
        function saveMigrationPublish()
        {

                 var allExtraFactsVal=document.getElementById("allFactsofCols").value;
                var allFactscolsVal=document.getElementById("allFactscols").value;

            var allDims=document.getElementById("allDims").value;
            var grp=document.getElementById("grp").value;
            var allFolderIds=document.getElementById("allFolderIds").value;
            var allFactscols=document.getElementById("allFactscols").value;
            var allFactsofCols=document.getElementById("allFactsofCols").value;

            var allDimId = allDims.split(",");
            var folderId = allFolderIds.split(",");
            var factColId=allFactscols.split(",");
            var factIdsofcol=allFactsofCols.split(",");
            var totalUrl="";
            var totalurlFactCols="";

                var cols="";
                var factsVal="";
                //alert('allExtraFacts '+allExtraFacts)
                var allExtraFacts=allExtraFactsVal.split(",");
               // alert('allExtraFactsVal '+allExtraFacts.length);
                for(var t=0;t<allExtraFacts.length;t++)
                {
                    //alert('allExtraFacts[t]=--- '+"FC"+allExtraFacts[t]);
                    var fact=document.getElementById('FC'+allExtraFacts[t]);
                    var v=allExtraFacts[t];
                    //alert('fact '+fact.value)
                    if(document.getElementById('FC'+allExtraFacts[t]).checked==true)
                    {
                        factsVal=factsVal+","+v;
                    }
                }
                //alert('factsVal '+factsVal);
                var allFactscols=allFactscolsVal.split(",");
                for(var t=0;t<allFactscols.length;t++)
                {
                    var col=document.getElementById(allFactscols[t]);
                    if(document.getElementById(col.value).checked==true)
                    {
                        cols=cols+","+allFactscols[t];
                    }
                }

            for(var m=0;m<folderId.length;m++)
            {
                var val= document.getElementById(folderId[m]); //document.getElementById("parameters2").checked=false

                if(document.getElementById(val.value).checked==true)
                {
                    totalUrl=totalUrl+"~"+val.value+"=";
                    for(var t=0;t<allDimId.length;t++)
                    {
                        var dim=allDimId[t];
                        var val2= document.getElementById(dim).value;
                        if(document.getElementById(val2).checked==true)
                        {
                            var dim=val2.split(":");
                            if(dim[0]==val.value)
                                totalUrl = totalUrl+","+dim[1];
                        }
                    }
                }
                                 }

                $.ajax({
                  url: 'userLayerAction.do?userParam=migrateChangesToRoleNewPublish&grpId='+grp+'&totalUrl='+totalUrl+'&totalurlFactCols='+totalurlFactCols+'&cols='+cols+'&factsVal='+factsVal,

                   // url: 'userLayerAction.do?userParam=migrateChangesToRoleNewPublish&grpId='+grp+'&totalUrl='+totalUrl+'&totalurlFactCols='+totalurlFactCols,
                success: function(data) {
                    //alert(data)
                    alert('Changes Migrated To Role and published Successfully.');
                    cancelMigration();
                }
            });
        }

        //migrateChangesToRoleNewPublish
        function saveMigration()
        {
             $("#loading").show();
       
            <%--alert('Save Migration');--%>
            var allExtraFactsVal=document.getElementById("allFactsofCols").value;
            var allFactscolsVal=document.getElementById("allFactscols").value;
            var allDims=document.getElementById("allDims").value;
            var grp=document.getElementById("grp").value;
            var allFolderIds=document.getElementById("allFolderIds").value;
            var allFactscols=document.getElementById("allFactscols").value;
            var allFactsofCols=document.getElementById("allFactsofCols").value;
           

<%--            alert(allExtraFactsVal);//null
            alert(allExtraFactsVal==null);
            alert((allExtraFactsVal.toString())=="");
            alert(allFactscolsVal);//xxx
            alert(allDims);//506
            alert(grp);//group 1062
            alert(allFolderIds);// 1203
            alert(allFactscols);//xx
            alert(allFactsofCols);//null--%>

           
            var allDimId = allDims.split(",");
            var folderId = allFolderIds.split(",");
            var factColId=allFactscols.split(",");
            var factIdsofcol=allFactsofCols.split(",");
            var totalUrl="";
            var totalurlFactCols="";

                var cols="";
                var factsVal="";
                //alert('allExtraFacts '+allExtraFacts)
                if ( (allExtraFactsVal.toString()) != "" )
                {
                    var allExtraFacts=allExtraFactsVal.split(",");
                    // alert('allExtraFactsVal '+allExtraFacts.length);
                    for(var t=0;t<allExtraFacts.length;t++)
                    {
                        //alert('allExtraFacts[t]=--- '+"FC"+allExtraFacts[t]);
                        var fact=document.getElementById('FC'+allExtraFacts[t]);
                        var v=allExtraFacts[t];
                        //alert('fact '+fact.value)
                        if(document.getElementById('FC'+allExtraFacts[t]).checked==true)
                        {
                            factsVal=factsVal+","+v;
                        }
                    }
                }
                //alert('factsVal '+factsVal);

                if ( (allFactscolsVal.toString()) != "" )
                {
                    var allFactscols=allFactscolsVal.split(",");
                    for(var t=0;t<allFactscols.length;t++)
                    {
                        var col=document.getElementById(allFactscols[t]);
                        if(document.getElementById(col.value).checked==true)
                        {
                            cols=cols+","+allFactscols[t];
                        }
                    }
                }

                //alert('cols  '+cols);

            for(var m=0;m<folderId.length;m++)
            {
                var val= document.getElementById(folderId[m]); //document.getElementById("parameters2").checked=false

                if(document.getElementById(val.value).checked==true)
                {
                    totalUrl=totalUrl+"~"+val.value+"=";
                    for(var t=0;t<allDimId.length;t++)
                    {
                        var dim=allDimId[t];
                        //alert('dim - '+dim);
                        var val2= document.getElementById(dim).value;
                        if(document.getElementById(val2).checked==true)
                        {
                            var dim=val2.split(":");
                            if(dim[0]==val.value)
                                totalUrl = totalUrl+","+dim[1];
                        }
                    }

                }

                  /*  var val1= document.getElementById('FC'+folderId[m]);
                if(val1.checked==true)
                {
                    for(var k=0;k< factIdsofcol.length;k++){
                        var s='FC'+folderId[m]+':'+factIdsofcol[k].split(":")[1];
                        var val3=document.getElementById(s);
                        if(document.getElementById(s).checked==true){
                            totalurlFactCols=totalurlFactCols+"~"+val3.value+"=";
                            for(var t=0;t<factColId.length;t++)
                            {
                                var factcol=factColId[t];
                                var val2= document.getElementById(factcol).value;
                               if(document.getElementById(val2).checked==true)
                                {
                                    var factcol=val2.split("-");
                                    var a=val2.split(":");
                                    totalurlFactCols = totalurlFactCols+","+val2;
                                    // }
                                }
                            }
                        }
                        } */
                  //  }

            }
            //alert('totalUrl '+totalUrl);
            // alert('totalurlFactCols= '+totalurlFactCols);

            $.ajax({
                    url: 'userLayerAction.do?userParam=migrateChangesToRoleNew&grpId='+grp+'&totalUrl='+totalUrl+'&totalurlFactCols='+totalurlFactCols+'&cols='+cols+'&factsVal='+factsVal,
                success: function(data) {
                   // alert(data)
                    if(data==2){
                    $("#loading").hide();
                    alert('Changes Migrated to Role Successfully.');
                    cancelMigration();
                }else{
                     $("#loading").hide();
                    alert('Unable to Migrate');
                    cancelMigration();
            }
            }

            });



        }
    </script>
</body>
</html>
