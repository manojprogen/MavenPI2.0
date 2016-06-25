<%-- 
    Document   : pbaddMoreDimensions  // for adding more dimensions.
    Created on : 23 Jun, 2012, 3:37:40 PM
    Author     : veenadhari.g
--%>

<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="java.text.*"%>
<%@page import="java.math.*"%>
<%@page import="prg.db.Container"%>
<%@page import="prg.db.PbDb"%>
<%@page import="utils.db.ProgenConnection"%>
<%@page import="prg.db.PbReturnObject"%>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                    session.setAttribute("theme", themeColor);
                } else {
                    themeColor = String.valueOf(session.getAttribute("theme"));
                }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
         <link href="<%=request.getContextPath()%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js" ></script>
        <script  type="text/javascript" src="<%=request.getContextPath()%>/template/js/managementTemplate.js" ></script>
        <%
        
        String DimData = "";
        String reportId = "";
        String oneviewname = "";
        String userId = "";
        String roleId = "";
        String  IsrepAdhoc2="";
        HashMap map = null;
        Container container = null;
        HashMap ParametersHashMap=null;
        ArrayList Parameters=null;
        ArrayList ParametersNames=null;
        String ParamRegion="";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        StringBuffer paramStr=new StringBuffer();
        StringBuffer paramNameStr=new StringBuffer();
        String prevCols="";
        String prevColNames="";
        
        if (request.getAttribute("pages") != null && request.getAttribute("pages").equals("true")) {
            
        if (request.getAttribute("pageHtml") != null) {
                DimData = String.valueOf(request.getAttribute("pageHtml"));
            }
        }
        else{
        if (request.getAttribute("measures") != null) {
                DimData = String.valueOf(request.getAttribute("measures"));
            }
        }
        
        if (request.getAttribute("reportId") != null) {
                reportId = String.valueOf(request.getAttribute("reportId"));
                oneviewname = String.valueOf(request.getAttribute("oneviewname"));
                if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(reportId) != null) {
                    container = (Container) map.get(reportId);
                    if(container.getReportType().equalsIgnoreCase("I")){
                     ParametersHashMap = container.getParametersHashMap();
                     Parameters=(ArrayList)ParametersHashMap.get("Parameters");
                     ParametersNames =(ArrayList)ParametersHashMap.get("ParametersNames");
                     
                     if(Parameters!=null){
                         ParamRegion=reportTemplateDAO.getParameterDisplayData((String[])Parameters.toArray(new String[0]), (String[])ParametersNames.toArray(new String[0]));
                        for(int i=0;i<Parameters.size();i++){
                            paramStr.append(",").append(Parameters.get(i).toString());
                            paramNameStr.append(",").append(ParametersNames.get(i).toString());
            }
                         prevCols=paramStr.substring(1);
                         prevColNames=paramNameStr.substring(1);
                     }else{
                         prevCols="";
                         prevColNames="";
                     }
                                         }
                }
                }
                 if(String.valueOf(request.getAttribute("isOneview"))!=null && String.valueOf(request.getAttribute("isOneview")).equalsIgnoreCase("true")){
                    request.setAttribute("session",session);
                  ParamRegion=reportTemplateDAO.getParameterForOneView(reportId,session);

            }
                if(String.valueOf(request.getAttribute("fromoneview"))!=null && String.valueOf(request.getAttribute("fromoneview")).equalsIgnoreCase("true")) {
                request.setAttribute("session",session);
                session.setAttribute("oneviewglobal","true");
                  ParamRegion=reportTemplateDAO.getParameterForOneView(reportId,session);

            }
            }
        
        if (request.getAttribute("USERID") != null) {
                userId = String.valueOf(request.getAttribute("USERID"));
            }
        if (request.getAttribute("roleId") != null) {
                roleId = String.valueOf(request.getAttribute("roleId"));
            }
        if (request.getAttribute("IsrepAdhoc1") != null) {
                IsrepAdhoc2 = String.valueOf(request.getAttribute("IsrepAdhoc1"));
            }
        %>
         <script type="text/javascript">
            var y="";
            var xmlHttp;
            var ctxPath="<%=request.getParameter("ctxPath")%>";
            var prevColsStr="<%=prevCols%>"
            var prevColNames="<%=prevColNames%>"
            var prevCols=prevColsStr.split(",");
            var preNames=prevColNames.split(",")

            for(var k=0;k<prevCols.length;k++){
                var pr= dimArray.toString();
                //alert("pr"+pr)
                if(pr.match(prevCols[k])==null){
                     dimArray.push(preNames[k]+"^elmnt-"+prevCols[k])
                }
            }


            $(document).ready(function() {
                $("#DimTree").treeview({
                    animated: "normal",
                    unique:true
                });

                //addeb by bharathi reddy fro search option
                $('ul#DimTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#DimTree',
                    loaderText: '',
                    delay: 100
                })
                $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView(action, el, pos);
            });
                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });
            });

            $(function() {
                var dragKpi=$('#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span')
                var dropKpis=$('#draggableKpis');

                $(dragKpi).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropKpis).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var kpi=ui.draggable.html();
                        createMeasures(ui.draggable.attr('id'));
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }
            


        </script>

        <style type="text/css">
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
            *{font:11px verdana}
        </style>
    </head>
    <body>
        <form name="myForm2" method="post">
            <table style="width:100%;height:400px" border="solid black 1px" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Measures from below</font></div>
                        <div class="masterDiv" style="height:365px;overflow-y:auto">
                            <ul id="DimTree" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="kpis">
                                        <%=DimData%>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td width="50%" valign="top"  id="draggableKpis" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Drop Measures Here </font>
                        </h3>
                        <div id="dragDiv" style="height:365px;overflow-y:auto">
                            <ul id="sortable">
                                <%=ParamRegion%>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
            
        </form>
        <br/>
        <center>
            <%if (request.getAttribute("pages") != null && request.getAttribute("pages").equals("true")) {
            
            %>
            <input type="button"  class="navtitle-hover" style="width:auto" value="Done" onclick="buildTemplate('<%=request.getContextPath()%>')">
            <%} else {%>
            <input type="button"  class="navtitle-hover" style="width:auto" value="Done" onclick="selectPages('<%=request.getContextPath()%>')">
           <%}%>
            <%--<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelKpi()">--%>
        </center>
        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
        </ul>
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
            <table>
                <tr>
                    <td id="value"></td>
                </tr>
                </table>
                    </div>
    <script>
       function buildTemplate(ctxPath){
     parent.$("#createMgmtDBBox").dialog('close');
           var ul = document.getElementById("sortable");
    var pages=[];
    if (typeof ul !== undefined || ul !== null) {
        var colIds = ul.getElementsByTagName("li");
        if (colIds !== null && colIds.length !== 0) {
            for (var i = 0; i < colIds.length; i++) {
                pages.push(colIds[i].id);
            }
        }
    }
   $.ajax({
       type:'POST',
       async:false,
       url: ctxPath+'/managementViewer.do?templateBy=getTemplateId&pages='+JSON.stringify(pages),
       success:function(templateId){
           alert(templateId)
          var url = ctxPath+'/managementViewer.do?templateBy=viewTemplate&templateId='+templateId;
window.open(url);
       }
       
   })
    
} 
        
    </script>
</body>
</html>
