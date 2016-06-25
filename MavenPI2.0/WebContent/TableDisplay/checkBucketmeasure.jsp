<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.HashMap,java.util.ArrayList,com.progen.reportview.db.PbReportViewerDAO,com.progen.reportdesigner.db.ReportTemplateDAO"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            PbDb pbdb=new PbDb();
            String reportId = request.getParameter("reportId");
            String folderdetssql="SELECT  FOLDER_ID FROM PRG_AR_REPORT_DETAILS where report_id="+reportId;
            PbReturnObject folderpbro=pbdb.execSelectSQL(folderdetssql);
             String folderIds="";
            if(folderpbro.getRowCount()>0){
              for(int i=0;i<folderpbro.getRowCount();i++){

              folderIds += ","+folderpbro.getFieldValueString(i, 0);
              }
              if(!folderIds.equalsIgnoreCase("")){
              folderIds=folderIds.substring(1);
              }
            Container container = null;
            if (session.getAttribute("PROGENTABLES") != null) {
             HashMap    map = (HashMap) session.getAttribute("PROGENTABLES");

            if (map.get(reportId) != null) {
                container = (Container) map.get(reportId);
            } else {
                container = new Container();
            }
           //String viewCount=container.getViewByCount();
           int vcount=container.getViewByCount();
           ////.println("vcount=="+vcount);
           String contextPath=request.getContextPath();
              
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>

        <%--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>--%>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style>
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
                width: 600px;
                height:600px;
                padding: 16px;
                border: 0px white;
                background-color: white;
                z-index:1002;
               


            }
            *{font:11px verdana;}
        </style>

      

    </head>
    <body id="mainBody">
        <%-- <img id="imgId" src="<%=request.getContextPath()%>/images/ajax.gif" width="75px" height="75px">--%>

        <%try {%>
        <%--<div class="drag" id="main" >
            <script>
                var divObj=document.getElementById("main");
                //alert("Starts "+divObj)
                divObj.style.visibility="hidden"
            </script>--%>


        <%
 String Query = "select DISTINCT NVL(disp_name,sub_folder_type),sub_folder_tab_id  from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts') and disp_name not in('Calculated Facts','Formula') and sub_folder_tab_id not in ('0')";
  //  String Query1 = "select DISTINCT element_id, NVL(disp_name,sub_folder_type),nvl(USER_COL_DESC, user_col_name),ref_element_id as column_name,BUSS_COL_NAME, ref_element_type,REFFERED_ELEMENTS from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts','Formula') and use_report_flag='Y'  order by BUSS_COL_NAME, ref_element_type";
    ////.println("bucket Query==="+Query);
  
                        PbReturnObject pbro1 = pbdb.execSelectSQL(Query);
   // PbReturnObject pbro2 = pbdb.execSelectSQL(Query1);
         String ElementId = "";
        String REFElementId = "";
        String ElementId1 = "";
        String REFElementId1 = "";
        String ElementName = "";
        String ElementName1 = "";
        String Formula="";
        String colId = "";
        String[] colNames = null;
        String finalQuery="";
        PbReturnObject retObj=null;
        ReportTemplateDAO dao=new ReportTemplateDAO();
        %>


        <table style="width:100%;height:270px" border="solid black 1px">
            <form name="myForm3" method="post">               
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Columns from below</font></div>
                        <div style="height:250px;overflow-y:auto">
                            <ul id="myList3" class="filetree treeview-famfamfam">
               <li class="open" style="background-image:url('<%=request.getContextPath()%>/images/treeViewImages/plus.gif')">
                                    <% for (int i = 0; i < pbro1.getRowCount(); i++) {
                                        String subFolderTabid=String.valueOf(pbro1.getFieldValueInt(i, 1));
                                    %>
                                    <ul>
                                        <li class="closed"><img src='<%=request.getContextPath()%>/icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=pbro1.getFieldValueString(i, 0)%></font></span>
                                            <ul>
                                                <%//for (int j = 0; j < pbro2.getRowCount(); j++) {
          finalQuery = "select DISTINCT element_id,nvl(USER_COL_DESC, USER_COL_NAME),ref_element_id as column_name,BUSS_COL_NAME, REF_ELEMENT_TYPE,REFFERED_ELEMENTS,disp_name,ACTUAL_COL_FORMULA   " +
                " from PRG_USER_ALL_INFO_DETAILS where folder_id in ("+folderIds+") and sub_folder_tab_id = "+subFolderTabid+" and sub_folder_type in('Facts','Formula') and use_report_flag='Y'  order by BUSS_COL_NAME,REF_ELEMENT_TYPE";
         retObj = pbdb.execSelectSQL(finalQuery);
         colNames = retObj.getColumnNames();
            ArrayList list = new ArrayList();
         for (int j = 0; j < retObj.getRowCount(); j++) {

                 ElementId = retObj.getFieldValueString(j, colNames[0]);
                ElementName = retObj.getFieldValueString(j, colNames[1]);
                REFElementId = retObj.getFieldValueString(j, colNames[2]);
                    Formula= retObj.getFieldValueString(j, colNames[7]);
                //colId = retObj.getFieldValueString(i, colNames[2]);
                if (ElementId.equalsIgnoreCase(REFElementId)) {
                    list.add(ElementId);
                                                %>
                                                <li class="closed" ><img src='<%=request.getContextPath()%>/icons pinvoke/table.png'></img><span class="myDragTabs" <%--class="ui-state-default"--%>  title="<%=Formula%>" id="<%=ElementId%>" ><%=ElementName%></span>
                                                    <ul>
                                                     <% for (int j1 = 0; j1 < retObj.getRowCount(); j1++) {
                        ElementId1 = retObj.getFieldValueString(j1, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j1, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j1, colNames[1]);
                         Formula= retObj.getFieldValueString(j1, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                                                     %>

                                                        <li><img src='<%=request.getContextPath()%>/icons pinvoke/report.png'></img><span class="myDragTabs"<%-- class="ui-state-default"--%> title="<%=Formula%>" id="<%=ElementId1%>"><%=ElementName1%></span></li>
                                               <%    }

                 }

                                                %>
                                                    </ul>
                                                </li>


                                                <%}
                                          }//end of for
                                         %>
                                      <%

            finalQuery = "select DISTINCT element_id,nvl(USER_COL_DESC, USER_COL_NAME),ref_element_id as column_name,BUSS_COL_NAME, REF_ELEMENT_TYPE,REFFERED_ELEMENTS,disp_name,ACTUAL_COL_FORMULA  " +
                "from PRG_USER_ALL_INFO_DETAILS where folder_id in ("+folderIds+") and sub_folder_type in('Facts','Formula') and use_report_flag='Y' and  (disp_name in('Formula','Calculated Facts') or disp_name is null )  order by BUSS_COL_NAME,REF_ELEMENT_TYPE";


            retObj = pbdb.execSelectSQL(finalQuery);
            for (int i1 = 0; i1 < retObj.getRowCount(); i1++) {
                String testQuery = "select distinct sub_folder_tab_id  from prg_user_all_info_details where element_id in(" + dao.removeLastCommas(retObj.getFieldValueString(i, 5)) + ") and sub_folder_tab_id=" + subFolderTabid;

                PbReturnObject testpbro = pbdb.execSelectSQL(testQuery);
                if (testpbro.getRowCount() > 0) {
                    ElementId = retObj.getFieldValueString(i1, colNames[0]);
                    ElementName = retObj.getFieldValueString(i1, colNames[1]);
                    REFElementId = retObj.getFieldValueString(i1, colNames[2]);
                    Formula= retObj.getFieldValueString(i1, colNames[7]);

                    //colId = retObj.getFieldValueString(i, colNames[2]);
                    if (ElementId.equalsIgnoreCase(REFElementId)) {
                        if (!(list.contains(ElementId))) {
                                       %>

                         <li class="closed" ><img src='<%=request.getContextPath()%>/icons pinvoke/table.png'></img><span class="myDragTabs" <%--class="ui-state-default"--%> title="<%=Formula%>" id="<%=ElementId%>" ><%=ElementName%></span>
                                                    <ul>
                                                     <% for (int j1 = 0; j1 < retObj.getRowCount(); j1++) {
                        ElementId1 = retObj.getFieldValueString(j1, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j1, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j1, colNames[1]);
                        Formula= retObj.getFieldValueString(j1, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                                                        %>

                                                        <li><img src='<%=request.getContextPath()%>/icons pinvoke/report.png'></img><span class="myDragTabs" title="<%=Formula%>" <%--class="ui-state-default"--%> id="<%=ElementId1%>"><%=ElementName1%></span></li>
                                               <%    }

                                                     }

                                %>
                                                    </ul>
                                                </li>

                                       <%
                        list.add(ElementId);
         }
                        }
                    }
                }
                        %>
                                            </ul>
                                        </li>

                                    </ul>
                                    <%
    }
                                    %>

                                </li>
                            </ul>
                        </div>
                    </td>
                  
                               
                    <td id="dropTabs" width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Columns to here</font></div>
                        <div style="height:250px;overflow-y:auto">
                            <ul id="sortable">
                                
                            </ul>
                        </div>
                    </td>
                   
                </tr>
            </form>
        </table>
        <table style="width:100%" align="center">
            <tr>
                <td colspan="2" style="height:10px"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="saveCols('<%=reportId%>','<%=vcount%>')">
           </td>
            </tr>
        </table>


        <%} catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
             }
        %>
          <div>
                    <iframe  id="dataDispbucketcols" NAME='dataDispbucketcols'  class="white_content1" SRC='#' style="overflow:auto"></iframe>
                </div>
                <div id="fade" class="black_overlay"></div>
  <script>

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
                
            });


            function saveCols(reportId,vcount){
                var cols="";
                var colNames="";
                var leftgrpColNames="";
                var rightgrpColNames="";
                var colsUl=document.getElementById("sortable");

                

                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");

                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                            colNames=colNames+","+document.getElementById('col-'+colIds[i].id.replace("GrpCol","")).innerHTML;
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                              colNames = colNames.substring(1);
                        }
               
                    var f=document.getElementById('dataDispbucketcols');
                    var s="pbCreateBucketAnalysis.jsp?FactColId="+cols+"&REPORTID="+reportId+"&colNames="+colNames+"&vcount="+vcount;
                    f.src=s;
                    document.getElementById('dataDispbucketcols').style.display='block';
                    document.getElementById('fade').style.display='block';
                    document.getElementById('mainBody').style.overflow='hidden';
                        
                    }
                    else{
                        alert("Please Select One Columns");
                    }
                }
              
            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);;
                var x=index.replace("GrpCol","");
                var i=0;

                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==x)
                        grpColArray.splice(i,1);
                }
            }

            function createColumn(elmntId,elementName){ 

                var parentUL=document.getElementById("sortable");
                var x=grpColArray.toString();
           
                if(grpColArray.length<1){

                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id="GrpTab"+elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.style.color='black';
                    cell2.id='col-'+elmntId;
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
                $(".sortable").sortable();
                $(".sortable").disableSelection();
                }else{
              
                }
            }

            function cancelanalysisBuckets()
            {
                document.getElementById("dataDispbucketcols").style.display='none';
                document.getElementById('fade').style.display='';
                document.getElementById('mainBody').style.overflow='auto';

            }
             function saveanalysisBuckets()
            {
                document.getElementById("dataDispbucketcols").style.display='none';
                document.getElementById('fade').style.display='';
                document.getElementById('mainBody').style.overflow='auto';

            }
        </script>
    </body>
</html>
