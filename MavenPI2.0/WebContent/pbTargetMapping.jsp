<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.KPIElement,com.progen.report.entities.KPI,com.progen.report.DashletDetail,java.util.*,java.text.*,java.math.*,prg.db.Container"%>
<%@page import="prg.db.PbDb,utils.db.ProgenConnection,prg.db.PbReturnObject,com.progen.report.pbDashboardCollection,com.progen.report.kpi.KPISingleGroupHelper,com.google.common.base.Joiner"%>
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
             String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
         <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>
         <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
         <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>

        

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
           <%
                   try{
                          PbDb pbdb = new PbDb();
                        String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
                        AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID"));
                        AvailableFiolers += " union ";
                        AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
                        AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID"));
                        AvailableFiolers += " and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID")) + ")))";
                        PbReturnObject folderpbro = pbdb.execSelectSQL(AvailableFiolers);                                    
         %>
    <body>
        <ul id="mymenu" class="contextMenu"><li class="select"><a href="#select">select</a></li></ul>
         <form name="targetForm" method="post">
            <table style="width:100%">  <tr>
                    <td align="right"><h5>Business Roles</h5></td>
                    <td align="left">
                        <h5><select id="bsrSelect" name="BusinessRoles">
                <option value=""> ---Select---</option>
       <%
                for(int i=0;i<folderpbro.getRowCount();i++){%>
                <option value="<%=folderpbro.getFieldValueString(i,0)%>">
                <%=folderpbro.getFieldValueString(i,1)%></option>
          <%} 
            }  catch (Exception e) {
                                    e.printStackTrace();
                       }%>
            </select>&nbsp;<input class="navtitle-hover" type="button" value="Go" onclick="getBusinessRolesMethod()"></h5>
                    </td>
                </tr>
           </table>
       <div id="dataDivId"> </div>
     </form>
             <script>
            var y="";
            var xmlHttp;
            var ctxPath;
            var eleID;
             var mapID;
            function getBusinessRolesMethod(){
               var bsrSelectedID= $("#bsrSelect").val();
               $.post(
                        'userLayerAction.do?userParam=getBusinessRolesMethod&bsrSelectedID='+bsrSelectedID+"&contextPath="+"<%=contextPath%>",
                        function(data){
                           $("#dataDivId").html(data);
               $("#kpiTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $('ul#kpiTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#kpiTree',
                    loaderText: '',
                    delay: 100
                })
                $("#kpiTree1").treeview({
                    animated: "normal",
                    unique:true
                });
                $('ul#kpiTree1 li').quicksearch({
                    position: 'before',
                    attached: 'ul#kpiTree1',
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
                $(".1_span").contextMenu({menu:"mymenu",rightButton: true},
                   function(action, el, pos) {
                       var val=el.attr("id");
                       var names=val.split("~");
                        eleID=names[0];
                     var name=names[1];
                     $("#elementname").val(name);

                   });
                $(".2_span").contextMenu({menu:"mymenu",rightButton: true},
                   function(action, el, pos) {
                       var val=el.attr("id");
                       var names=val.split("~");
                        mapID=names[0];
                     var name=names[1];
                    $("#mapto").val(name);
                   });
                     });
                 }
            function mappedElements(){
            var ele=$("#elementname").val();
            var mappedEle=$("#mapto").val();
             $.post(
                        'userLayerAction.do?userParam=mappedElements&eleID='+eleID+"&mapID="+mapID+"&contextPath="+"<%=contextPath%>",
                        function(data){
                           if(data!=null){alert("Elemets Mapped Successfully");}

                        });
            
            }
            function saveKpis(dayDiff,kpiType,divId){
                dispKpis(dayDiff,kpiType,divId);
            }
            function cancelKpi(){
                parent.cancelRepKpi();
            }          
            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function contextMenuWorkFormulaView(action, el, pos){
            document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');
            }
        </script>
    </body>
</html>