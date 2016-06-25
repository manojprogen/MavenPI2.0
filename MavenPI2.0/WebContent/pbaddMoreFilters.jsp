<%--
    Document   : pbaddMoreFilters  // for adding more dimensions.
    Created on : 23 Dec, 2014, 3:37:40 PM
    Author     : amar
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.progen.connection.ConnectionMetadata"%>
<%@page import="com.progen.connection.ConnectionDAO"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.progen.report.display.DisplayParameters"%>
<%@page import="com.progen.report.PbReportCollection"%>
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
        <%

        String DimData = "";
        String reportId = "";
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
        String contextPath;
        ArrayList list=new ArrayList();
        ArrayList listId = new ArrayList();
        PbReportCollection collect = new PbReportCollection();
        String[] parameterStr = null;
        PbReturnObject retObj=null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int size = 0;
        ArrayList<ArrayList> a = new ArrayList<ArrayList>();
        if (request.getAttribute("reportId") != null) {
                reportId = String.valueOf(request.getAttribute("reportId"));
                 if(String.valueOf(request.getAttribute("isOneview"))!=null && String.valueOf(request.getAttribute("isOneview")).equalsIgnoreCase("true")){
                    request.setAttribute("session",session);
                  ParamRegion=reportTemplateDAO.getParameterForOneView(reportId,session);

            }
                 contextPath=request.getContextPath();
                 collect.reportId = reportId;
                 collect.ctxPath = contextPath;
                 collect.getParamMetaData(true);
                 String reportName = collect.reportName;
                // list = collect.paramValueList;
                 String[] values;
                 String viewbyvalues = "";


                 size = collect.paramValueList.size();
                 
                 if(size>6){
                    size = 5;
                  }

                 for (int k = 0; k < size; k++) {
                     
                     
                        values = collect.paramValueList.get(k).toString().split(":");
                        
                        viewbyvalues = values[1];
//                        if (!viewbyvalues.contains("[All]")) {
//                            String value = (String) container.getReportCollect().paramValueList.get(k);
////                            if (!filterValues.contains(value)) {
////                                filterValues.add(value);
////                            }
//                        }
                        list.add(values[0]);
                    }
//                 Iterator listItr = list.iterator();
//                 while(listItr.hasNext()){
//                     listId.add(listItr.next().toString().replace(" ", ""));
//                 }

                 parameterStr = (String[]) (collect.reportParameters.keySet()).toArray(new String[0]);
            }
        ArrayList b = new ArrayList();
        HashMap<String,List> inFilters = collect.operatorFilters.get("IN");
        //HttpSession session = request.getSession(false);
        ArrayList<String> filter = new ArrayList<String>();
        // code added by Amar
       StringBuilder result = new StringBuilder();
        ArrayList<String> reportParam = collect.reportParamIds;
        ArrayList reportParamNames = collect.reportParamNames;
        HashMap reportParametersValues = (HashMap) collect.operatorFilters.get("IN");
        HashMap reportexcludevalues = (HashMap) collect.operatorFilters.get("NOTIN");
        LinkedHashMap reportParameters = collect.reportParameters;
        //result.append("<div id=\"globalFilter_" + reportId + "\" class=\"globalfilterShow\">");
        //StringBuilder innerResult = new StringBuilder();
        //StringBuilder innerResult1 = new StringBuilder();
        // Modified by Mayank
//        innerResult1.append("<div><table><tr><td><a title=\"Edit ViewBy\" onclick=\"editViewBy();\" class=\"ui-icon ui-icon-copy\" href=\"javascript:void(0)\"></a></td></tr></table></div>");
        //innerResult1.append("<div><table><tr><td><a title=\"Edit ViewBy\" onclick=\"\" class=\"\" href=\"javascript:void(0)\"></a></td></tr></table></div>");
        if (reportParam != null) {
            for (int i = 0; i < reportParam.size(); i++) {
                ArrayList<String> paramDetails = (ArrayList) reportParameters.get(reportParam.get(i));
                String Name = paramDetails.get(9);
                List<String> values = (List<String>) reportParametersValues.get(reportParam.get(i));
                if (values != null) {
                    for (String filters : values) {
                        if (!filters.equalsIgnoreCase("All")) {
//                            String divId = Name + "_" + filter + "_Global";
//                            innerResult.append("<div id='" + divId + "' class='newparamView' style='width:" + (10 * (filter.length())) + ";float:left;'>");
//                            innerResult.append("<table><tr>");
//                            innerResult.append("<td><span class='newParamName' >" + filter + "</span></td>");
//                            innerResult.append("<td ><a href=\"javascript:deleteglobalParam('" + Name + "','" + divId + "','" + filter + "')\" style=\"float:left\" class=\"ui-icon ui-icon-close\" ></a></td>");
//                            innerResult.append("</tr></table>");
//                            innerResult.append("</div>");
                        filter.add(filters);
                        }
                    }
                }
            }
        }
//        filter.add("AHMEDABAD");
//        filter.add("AGRA");
//        filter.add("HYDERABAD");
            //end of code
        session.setAttribute("IN",inFilters);
        DisplayParameters dispParam = new DisplayParameters();
        String Query="";
        int count=0;
        String startValue = "1";
        String endValue = "20";
        //= dispParam.getParameterQuery(parameterStr[2]);
        PbDb pbParam = new PbDb();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String parentName = "";
//            PbTimeRanges pb = new PbTimeRanges();
            con=ProgenConnection.getInstance().getConnectionForElement(parameterStr[2]);
//            con = pb.getConnection(request.getParameter("query"));
              ConnectionDAO connectionDAO = new ConnectionDAO();
            ConnectionMetadata conMetadata =connectionDAO.getConnectionForElement(parameterStr[2]);
            st = con.createStatement();
            StringBuffer changedQuery = new StringBuffer();
            for(int m=0;m<size;m++){
            Query = dispParam.getParameterQuery(parameterStr[m]);
            //Added by Amar
            if(conMetadata.getDbType().equalsIgnoreCase("Mysql"))
             changedQuery.append("select A1,A2 from (select A1,A2 from ( ");
            else
            changedQuery.append("select A1,A2 from (select RANK() over(order by A1,A2) AS num1,A1,A2 from ( ");
            changedQuery.append(Query);
            changedQuery.append(" ) O1 ");
            String orderSeq = "asc";
            if(conMetadata.getDbType().equalsIgnoreCase("Mysql")){
             if(startValue.equalsIgnoreCase("1"))
                    startValue ="0";
            changedQuery.append(") O2 order by 1 " +orderSeq+ " Limit " + startValue + " ," + endValue + "");
             }
             else
               changedQuery.append(") O2 where num1 between " + startValue + " and " + endValue + " order by 1 " +orderSeq);
            //End of code
            rs = st.executeQuery(changedQuery.toString());
            while (rs.next()) {
                b.add(rs.getString(1));
            }
            a.add(b);
            b=new ArrayList();
            changedQuery = new StringBuffer();
            }
            rs.close();
            rs = null;
            st.close();
            st = null;
            con.close();
            con = null;
        }
        catch(Exception e){
            }
        if (request.getAttribute("USERID") != null) {
                userId = String.valueOf(request.getAttribute("USERID"));
            }
        int tdWidth = 0;
        if(size!=0){
         tdWidth=(int)Math.floor(100/size);
        }
        %>
         <script type="text/javascript">

//            function cancelKpi(){
//                parent.cancelRepKpi();
//            }

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
                        createDims(ui.draggable.html(),ui.draggable.attr('id'));
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

    function applyFilter(){
        var chkCnt = 0;
        parent.$("#AddMoreFiltersDiv").dialog('close');
        var repId = document.getElementById("interRepId").value;
        var liClass;
        var count=0;
            var form = document.getElementById("filterForm");
            var viewByIds = new Array;
            var viewValuCount = new Array;
            var viewMap={};
            var chekedVal =new Array;
            var c=0;
            $("#tr1").find("td").each(function(){
                var id = $(this).attr("id").split("_");
                viewByIds.push(id[0]);
                count++;
            })
            for(var cn=0;cn<count;cn++){
                c=0;
                chkCnt=0;
                liClass=viewByIds[cn]+"_filters";
                $("."+liClass).each(function(){
                    if($(this).is(":checked")){
                      chkCnt++;
                      chekedVal.push($(this).val());
                    }
                    c++;
                })
//               viewMap[viewByIds[cn]]=chekedVal;
                if(chkCnt>0)
                viewMap[viewByIds[cn]]=chekedVal;
                else {
                chekedVal.push("All");
                viewMap[viewByIds[cn]]=chekedVal;
                }

                chekedVal=[];
            }
            $.ajax({
                   type:'POST',
                   data:{"viewMap":JSON.stringify(viewMap),"viewByIds":viewByIds,"reportId":repId},
                   url:'reportViewer.do?reportBy=setIntermediateFilters',

            success : function(data){
             window.open('reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
            }
            });
    }
    function selectAll(a){
        var inClass = a+'_filters';
            if($("#"+a).is(':checked')){
                 $("."+inClass).attr('checked',true);
            }else{
                $("."+inClass).removeAttr('checked');
            }
    }
    function moreOption(ds){
        var eleId = $(ds).attr('id').split('_');
        $("."+eleId[0]+"_more").css({'display':'block'});
        $(ds).remove();
    }
    function openReportWithDefaultFilters(){
        parent.$("#AddMoreFiltersDiv").dialog('close');
        var repId = document.getElementById("interRepId").value;
        window.open('reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
    }
    function changeSelect(selId){
        var splitId = selId.split("_");
        var inId = splitId[0];
        var clName = inId+"_filters";
        var totalLi=0;
        var checkedLi=0;
        $("."+clName).each(function(){
                    totalLi++;
                    if($(this).is(":checked")){
                        checkedLi++;
                    }

                })
                if(totalLi==checkedLi){
                     $("#"+inId).attr('checked',true);
                }else{
                        $("#"+inId).attr('checked',false);
                }

    }
    function moreFilterValues(ds){
        var eleId = $(ds).attr('id').split('_');
        var startVal = document.getElementById(eleId[0]+"_count").value;
        var repId = document.getElementById("interRepId").value;
        var ctxPath = "<%=request.getContextPath()%>";
        var url = ctxPath+"/dsrv"+"?query="+eleId[0]+"&REPORTID="+repId+"&startValue="+startVal;
        getParameterLOV(url,eleId[0]);
    }
    function getParameterLOV(mainURL,id){
        $.ajax({
            url: mainURL,
            success: function(data){
            if(data=="Search Exception"){
                alert(data)
                //handleResponse1(data);
            }
            else{
                addMoreValues(data,id);
            }

        }
    });

}
     function addMoreValues(response,id){
          var ulId = id+"_ul";
          var valCnt=0;
          var startVal = document.getElementById(id+"_count").value;
          var names = response.split("\n");
          for(var c=0;c<names.length-1;c++){
              valCnt++;
              var html = "<li><input class=\"navtitle-hoverNew "+id+"_filters\" type=\"checkbox\" id='"+id+"_"+parseInt(parseInt(startVal)+parseInt(c))+"' \n\
                value='"+names[c]+"' onchange='changeSelect(this.id)'>"+names[c]+"</li>";
              $("#"+ulId).append(html);
          }
          if(valCnt<20){
              $("#"+id+"_in").remove();
          }else{
              startVal = parseInt(startVal)+ parseInt(valCnt);
              document.getElementById(id+"_count").value = startVal;
          }
      }
     function callFilterDiv(){
         $("#filDisplayDiv").toggle();

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
                /*border:0px;*/
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
/*                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;*/
                margin:2px;
            }

            ul{
                list-style-type: none;
                box-shadow: #B5B5B5;
                margin: 0;
                padding: 0;
            }
    .navtitle-hoverNew{
    -moz-border-radius-bottomleft:2px;
    -moz-border-radius-bottomright:2px;
    -moz-border-radius-topleft:2px;
    -moz-border-radius-topright:2px;
    background:#79C9EC url(images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%;
    border:1px solid #448DAE;
    color:#000;
    FONT-SIZE: 10px;
    FONT-FAMILY: Verdana;
    VERTICAL-ALIGN: middle;
    HEIGHT: 8px;
    WIDTH: auto;
    cursor:pointer;
}
.navtitile-hoverOld{
    -moz-border-radius-bottomleft:4px;
    -moz-border-radius-bottomright:4px;
    -moz-border-radius-topleft:4px;
    -moz-border-radius-topright:4px;
    background:#79C9EC url(images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%;
    border:1px solid #448DAE;
    color:#000;
    FONT-SIZE: 11px;
    FONT-FAMILY: Verdana;
    VERTICAL-ALIGN: middle;
    HEIGHT: 22px;
    WIDTH: 30%;
    cursor:pointer;
}
.newparamViewFilter{
    border:1px solid;
    border-color:#B2B2B2;
    background:#E5E5E5;
    overflow: hidden;
/*    width:100px;*/
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    border-radius: 4px
}
#111613_div::-webkit-scrollbar {
      width: 10px;
}
#111613_div::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5);
}
        </style>
    </head>
    <body>
        <form id="filterForm" name="filterForm" method="post">
            <table style="width:100%;overflow: hidden" >
                <tr id="tr1" style="background-color: #009fe3;height: 5 px">
                    <%for(int j=0;j<size;j++){%>
                    <td style="font-size: larger;font-weight:bold; width: <%=tdWidth%>%"id="<%=parameterStr[j]%>_a<%=j%>"><input type="checkbox" class="class1" id="<%=parameterStr[j]%>" onchange="selectAll(this.id)"><font color="#fff"><%=list.get(j)%></td>
                    <%}%>
                </tr>
                <tr id="tr2">
                    <%for(int h=0;h<size;h++){%>
                    <td style=""><div  id="<%=parameterStr[h]%>_div" style="height: 400px;overflow: auto;">
                            <ul id="<%=parameterStr[h]%>_ul">
                        <% for (int k=0;k<a.get(h).size();k++){ %>
                        <li><input class="navtitle-hoverNew <%=parameterStr[h]%>_filters" type="checkbox" id="<%=parameterStr[h]%>_<%=k%>" value="<%=a.get(h).get(k)%>" onchange="changeSelect(this.id)"><%=a.get(h).get(k)%></li>
                        <%
                            count++;
                        }%>


                        </ul></div>
                        <%if(count>=20){%>
                        <input type="hidden" id="<%=parameterStr[h]%>_count" name="interRepName" value="<%=count%>" >
                        <input class="navtitle-hover" id="<%=parameterStr[h]%>_in" type="button" onclick="moreFilterValues(this);" value="More">
                        <%}count=0;%>
                    </td><%}%>
                </tr>
            </table>
                        <br/>
                        <input type="hidden" id="interRepId" name="interRepName" value="<%=reportId%>" >
                        <div><img src="images/filter.png" title="Click To View Already Applied Report Filters" style="margin-left: 0px;margin-right: 150px; padding-left: 5px; cursor: pointer" onclick="callFilterDiv()">
                        <input class="navtitile-hoverOld" type="button" name="submit" value="Run Report With Filters" onclick="applyFilter()">&nbsp;&nbsp;&nbsp;
                            <input class="navtitile-hoverOld" type="button" name="submit" value="Skip To Default Report View" onclick="openReportWithDefaultFilters()">
                            </div>

                        <div id="filDisplayDiv" style="display:none; margin-top: 5px;margin-left: 10px; height: 60px;overflow: auto;">

                            <%if(filter.size()>0){%>
                            <%for(int j=0;j<filter.size();j++){%>
                            <div class="newparamViewFilter" style="float:left;"><table><tr>
                            <td><span class="newParamName"><%=filter.get(j)%></span></td></tr></table></div>
                            <%}}else{%>
                            <div style="margin-top: 15px;margin-left: 47%;"><table><tr>
                            <td style="color: #E72B2B; "><span class="newParamName">NO Filters Applied</span></td>
                            <%}%><tr>
                        </table></div></div>
        </form>
    </body>
</html>
