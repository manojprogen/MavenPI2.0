<%@page import="java.util.ArrayList,prg.db.PbReturnObject,prg.business.group.PbBusinessGroupEditDAO"%>

<%String contextPath=request.getContextPath(); %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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


<!--                <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>-->

           
    </head>
    <%
        String grp=request.getParameter("grp");
        ////////////////////////////////////////////////////////////////////////.println.println(" grp  in edit bg "+grp);
        PbBusinessGroupEditDAO bgEdit=new PbBusinessGroupEditDAO();
        PbReturnObject all =bgEdit.getExtraDimForGrp(grp);
        PbReturnObject conDims= (PbReturnObject)all.getObject("conDims");
        PbReturnObject grpDims= (PbReturnObject)all.getObject("grpDims");
        ArrayList grpDimList = new ArrayList();
        for(int m=0;m<grpDims.getRowCount();m++)
            {
            if(!grpDims.getFieldValueString(m,"DIM_NAME").equalsIgnoreCase("Time"))
                grpDimList.add(grpDims.getFieldValueString(m,"QRY_DIM_ID"));
            }
        String allDimIds="";
        String allDimNames="";
        for(int m=0;m<conDims.getRowCount();m++)
            {
            if(!grpDimList.contains(conDims.getFieldValueString(m,"DIMENSION_ID")))
               {
                allDimIds=allDimIds+"*u,"+conDims.getFieldValueString(m,"DIMENSION_ID");
                allDimNames=allDimNames+"*" +conDims.getFieldValueString(m,"DIMENSION_NAME");
                }
            }
     if(allDimIds.length()>0)
         {
         allDimIds= allDimIds.substring(1);
         allDimNames=allDimNames.substring(1);
         }
        ////////////////////////////////////////////////////////////////////////.println.println(" allDimIds "+allDimIds+" allDimNames "+allDimNames);
        ////////////////////////////////////////////////////////////////////////.println.println(" grpDimList "+grpDimList);
    %>
    <body>
        <Center>
                 <br>
        <center><b style="color:#369;font-size:11px;font-family:verdana;">Dimension List (Drag Dimension to Right and click Save)</b></center>
                <br>
             <Form>
                <table align="center" border="1" width="100%" style="height:200px" valign="top">
                <tr><td align="left">
                    <a href="javascript:moveAll('<%=allDimIds%>','<%=allDimNames%>')" style="text-decoration:none;font-family:verdana;font-size:11px"> MoveAll</a>
                         &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;">Available Dimensions.</b>
                </td> <td align="left">
                         <a href="javascript:deleteAll()" style="text-decoration:none;font-family:verdana;font-size:11px">DeleteAll</a>
                         &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;">Dimensions To Be Added.</b>
                    </td>
                </tr>
                <tr>
                    <td  class="draggedTable1" valign="top">
                        <div style="height:250Px;width:100%;overflow-y:auto">
                        <table  border="0">
                            <%for(int i=0;i<conDims.getRowCount();i++){
                            if(!grpDimList.contains(conDims.getFieldValueString(i,"DIMENSION_ID"))){
                                %>
                               <tr><td class="myDragTabs" class="ui-state-default3" style="width:200px" id="u,<%=conDims.getFieldValueInt(i,"DIMENSION_ID")%>~<%=conDims.getFieldValueString(i,"DIMENSION_NAME")%>"><font style="color:black;font-family:verdana;font-size:12px"><%=conDims.getFieldValueString(i,"DIMENSION_NAME")%></font></td></tr>
                               <%}}%>
                        </table>
                        </div>
                    </td>
                    <td id="dropTabs" style="width:50%;border:1px solid silver" class="myhead" valign="top">

                         <div style="height:250Px;width:100%;overflow-y:auto">
                              <ul id="sortable" >

                              </ul>
                         </div>
                    </td>
                </tr>

             </table>

                <Table>
                       <Tr>
                           <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Save" ONCLICK="saveEdit()">
                           </Td>
                           <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Cancel" ONCLICK="cancelEdit()">
                               <Input TYPE="hidden" name="grp" id="grp" value="<%=grp%>">
                           </Td>
                       </Tr>
                 </Table>
               </Form>
         </Center>
                            <script type="text/javascript">
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

             function saveEdit()
             {

                var dims="";
                var grp=document.getElementById("grp").value;
                var rolesUl=document.getElementById("sortable");
                var dimIds=rolesUl.getElementsByTagName("li");
                for(var i=0;i<dimIds.length;i++){
                    dims=dims+","+dimIds[i].id;
                }
               // alert(' roles '+dims);
                if(dimIds.length!=0){
                    dims=dims.substr(1);
                }
                    //alert(' dims '+dims);
                   // alert('businessgroupeditaction.do?groupdetails=editGrpAddDim&dims='+dims+'&grp='+grp);

                  $.ajax({
                     url: 'businessgroupeditaction.do?groupdetails=editGrpAddDim&dims='+dims+'&grp='+grp,
                    success: function(data) {
                        if(data==1)
                           {
                              alert('Dimensions Added successfully');
                              refreshSaveEdit();
                              
                           }
                        }
                    });

             }
             function refreshSaveEdit()
             {
                  parent.refreshSaveEdit();
             }
              function cancelEdit()
             {
                 parent.cancelGrpEditParent();
             }
    </script>
    </body>
</html>
