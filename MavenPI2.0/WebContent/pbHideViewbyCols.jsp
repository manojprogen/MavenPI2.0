<%-- 
    Document   : pbHideViewbyCols
    Created on : Jan 20, 2014, 2:43:46 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.HashMap,prg.db.Container,java.util.ArrayList,com.progen.reportview.db.PbReportViewerDAO,prg.db.Container,org.omg.PortableInterceptor.SYSTEM_EXCEPTION"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);  
    String themeColor="blue";    
    if(request.getParameter("loadDialogs") != null && request.getParameter("loadDialogs").equalsIgnoreCase("TRUE")) {
    String reportId=request.getParameter("reportid");
    HashMap map = null;
    Container container = null;
    ArrayList<String> rowViewbyIds = new ArrayList<String>();
    ArrayList<String> rowViewbyNames = new ArrayList<String>();
    String rowViewbyHtml="";
    String dropHtml="";
    PbReportViewerDAO viewDAO = new PbReportViewerDAO();
    String prevColumns = "";
    StringBuffer rowViewHtml=new StringBuffer();
    ArrayList<String> hideviewLst=new ArrayList<String>();
    if (session.getAttribute("PROGENTABLES") != null) {                  
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(reportId) != null) {
                    container = (Container) map.get(reportId);
                    rowViewbyIds = (ArrayList) container.getTableHashMap().get("REP");        
                    rowViewbyNames = (ArrayList) container.getTableHashMap().get("REPNames");
                    rowViewbyHtml=viewDAO.generateDragRowviewbysHtml(request.getContextPath(), rowViewbyIds, rowViewbyNames);
                    hideviewLst=container.getReportCollect().getHideViewbys();
                   
                    dropHtml=viewDAO.generateDropRowViewbyHtmldata(request.getContextPath(), hideviewLst, rowViewbyIds, rowViewbyNames);                   
                    for(int i=0;i<hideviewLst.size();i++){  
//                        prevColumns+=","+(String)hideviewLst.get(i);
                        rowViewHtml.append(",").append((String)hideviewLst.get(i));
                    }
                    if(rowViewHtml!=null && !rowViewHtml.toString().equalsIgnoreCase("")){
                        prevColumns=rowViewHtml.toString().substring(1);
                    }
                }
                }
    String contextPath=request.getContextPath();
//    }
%>
<html>
    <head>
           <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
               <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
         <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
       
    </head>
    <body>
        <table border="solid black 1px" align="center" style="width:90%;">
            <tr>
                <td class="draggedTable1" width="50%" valign="top">
                    <div class="ui-state-default draggedDivs ui-corner-all" style="height:20px"><font size="2" style="font-weight:bold">Drag values from here </font></div>
                        <div id="myList3div" style="height:350px;overflow:auto">
                            <ul id="myList3" class="myList3 filetree treeview-famfamfam treeview">
                                <%=rowViewbyHtml%> 
                             </ul>
                        </div>
                </td>
                <td id="dropTabs1" class="ui-droppable" width="50%" valign="top">
                    <div class="ui-state-default draggedDivs ui-corner-all" style="height:20px"><font size="2" style="font-weight:bold">Drop values here</font></div>
                    <div style="height:350px;overflow:auto">
                      <ul id="sortable" class="sortable ui-sortable" style="">
                          <%=dropHtml%> 
                      </ul>
                    </div>
                </td>
            </tr>
        </table>
       <table><tr><td align="center"><input type="button" name="Done" class="navtitle-hover" value="Done"  onclick="savehideViewbys('<%=request.getContextPath()%>','<%=reportId%>')"></td></tr>
           <tr><td align='left'>Drag Column That U Wish To Hide In Right Pane</td></tr>
       </table>
            <script type="text/javascript" >
            var grpColArray=new Array();
//            $(document).ready(function() { 
//                alert("123")
//                 $("#myList6").treeview({
//                        animated:"slow",
//                        persist: "cookie"
//                    });
//                 });
                 $(function() { 
//                     alert("fun")
                      $("#myList3").treeview({
                        animated:"slow"
//                        persist: "cookie"
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
                    $("#dropTabs1").droppable({
            activeClass:"blueBorder",
            accept:'.myDragTabs',
            drop: function(ev, ui) { 
//                alert("123")
                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
            }
        }
    );
     
    });
    var prevColsStr="<%=prevColumns%>"
            var prevCols=prevColsStr.split(",");

            for(var k=0;k<prevCols.length;k++){
                var pr=grpColArray.toString();
                if(pr.match(prevCols[k])==null){
                    grpColArray.push(prevCols[k]);                    
                }
            }
    function createColumn(elmntId,elementName,tarLoc){
            var parentUL=document.getElementById(tarLoc);
            var x=grpColArray.toString();
            if(x.match(elmntId)==null){
                grpColArray.push(elmntId);
                var childLI=document.createElement("li");
                childLI.id=elmntId+"_li";
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                var table=document.createElement("table");
                table.id=elmntId+"_table";
                var row=table.insertRow(0);
                var cell1=row.insertCell(0);
                //cell1.style.backgroundColor="#e6e6e6";
                var a=document.createElement("a");
                var deleteElement = elmntId;
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
            }else{
                alert("Already assigned")
            }
            
            $(".sortable").sortable();
        }
        function deleteColumn(index)
            {
                // alert("index\t"+index)
                //alert("grpColArray\t"+grpColArray.length)
                var LiObj=document.getElementById(index+"_li");
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);
                
                var i=0;
                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==index)
                        grpColArray.splice(i,1);
                }
                
                
            }
            function savehideViewbys(ctxPath,reportId){            
                var ul = document.getElementById("sortable");
                var viewbyIdsArray = new Array();
                var colIds;
                if(ul!=undefined || ul!=null){
                    colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            var val=colIds[i].id.split("_")[0];
                            viewbyIdsArray.push(val);
                        }
                    }
                }  
                parent.$("#hideViewByDiv").dialog('close');
                $.post(ctxPath+'/reportViewer.do?reportBy=saveHideTableViewbys&reportId='+reportId+'&hideViewbys='+viewbyIdsArray,
                function(data){
                    
                    var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId;
                    var dSrc = parent.document.getElementById("iframe1");
                    dSrc.src = source;
                    //                 var path=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId;
                    //                parent.submiturls1(path);
                });
            }
        </script>
    </body>
</html>
<% } %>