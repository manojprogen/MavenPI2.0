<%@page import="com.progen.scenariodesigner.db.ScenarioTemplateDAO"%>

<%
//String foldersIds=request.getParameter("foldersIds");
//////////////////////////////////////////.println.println(" foldersIds in m js p "+foldersIds);
%>

<%@page import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO" %>
<%@page import="prg.db.PbReturnObject,prg.db.Container" %>
<%
            String MeasureRegion = "";
            StringBuffer prevColumns = new StringBuffer("");
            String PrevMsrStr = null;
            HashMap map = null;

            ArrayList MeasureIds = null;
            ArrayList MeasureNames = null;
            HashMap TableHashMap = null;
            Container container = null;
            String[] selSeededModels = null;

            PbReportViewerDAO viewDAO = new PbReportViewerDAO();

            String scenarioId = "";
            String scenarioName = "";
            String selectedSeededModels = "";
            ArrayList seededModels = new ArrayList();

            scenarioName = (String) request.getAttribute("scenarioName");
            selectedSeededModels = (String) request.getAttribute("selectedSeededModels");
            if((selectedSeededModels != null) || (! selectedSeededModels.equalsIgnoreCase(""))) {                
              selSeededModels = selectedSeededModels.split(",");
              for(int a=0;a<selSeededModels.length;a++) {
                seededModels.add(selSeededModels[a]);
              }
            }
            ////////////////////////////////////////.println.println("selectedSeededModels is:: "+selectedSeededModels);
            ////////////////////////////////////////.println.println("selSeededModels.length is:: "+selSeededModels.length);
            ScenarioTemplateDAO scnDao = new ScenarioTemplateDAO();
            PbReturnObject pbro = scnDao.getSeededModels();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Meaures</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>

        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>


        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>



        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=request.getContextPath()%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />

        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></script>

        <script>
            var y="";
            var xmlHttp;
            var ctxPath;
            var msrArray=new Array();

            var prevColsStr="<%=PrevMsrStr%>"
            var prevCols=prevColsStr.split(",");

            for(var k=0;k<prevCols.length;k++){
                var pr=msrArray.toString();
                if(pr.match(prevCols[k])==null){
                    msrArray.push(prevCols[k]);
                }
            }

            function saveSeededModels(){
                var path=document.getElementById('path').value;
                var scenarioName=document.getElementById('scenarioName').value;
                var allModIds = document.getElementById('allModIds').value;
                var totalUrl="";
                var allId = allModIds.split(",");

                for(var m=0;m<allId.length;m++)
                {
                    var val= document.getElementById(allId[m]); //document.getElementById("parameters2").checked=false
                    if(document.getElementById(val.value).checked==true)
                        totalUrl = totalUrl+","+val.value;
                }
           
                $.ajax({
                    url: path+'/ScenarioTemplateAction.do?scnTemplateParam=saveScenarioSeededModels&scenarioName='+scenarioName+'&totalUrl='+totalUrl,
                    success: function(data) {
                        if(data!=""){                            
                            parent.document.getElementById("selectedSeededModels").value = data;
                            cancelSeededModels();
                        }
                    }
                });
            }

            function dispMeasures(){
                var msrs="";
                var path=document.getElementById('path').value;
                var msrUl=document.getElementById("sortable");
                var msrIds=msrUl.getElementsByTagName("li");
                var scenarioName=document.getElementById('scenarioName').value;
                alert('scenarioName '+scenarioName);
                for(var i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;

                }

                if(msrIds.length!=0){
                    msrs=msrs.substring(1);

                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;
                    alert(' in asve ')
                    $.ajax({
                        url: path+'/ScenarioViewerAction.do?scenarioParam=saveScenarioSeededModels&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrs+'&scenarioName='+scenarioName,
                        success: function(data) {
                            if(data!=""){
                              
                            }
                        }
                    });
                }
                
            }
            function cancelSeededModels(){
                parent.cancelSeededModelsPar();
            }


            $(document).ready(function() {
                $("#measureTree").treeview({
                    animated: "normal",
                    unique:true
                });
            });

            $(function() {
                var dragMeasure=$('#measures > li > ul > li > span')
                var dropMeasures=$('#draggableMeasures');

                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropMeasures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createMeasures(ui.draggable.html(),ui.draggable.attr('id'));

                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function prevMeasures(){
                var prevMsrs=parent.document.getElementById("Measures").value;
                if(prevMsrs.length!=0){
                    prevMsrs=prevMsrs.split(",");
                    for(var m=0;m<prevMsrs.length;m++){
                        var msrElmnts=prevMsrs[m].split("-");
                        createMeasures(msrElmnts[0],"elmnt-"+msrElmnts[1]);
                    }
                }
            }

        </script>

        <style>
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
        </style>
    </head>
    <body>
        <center>

            <%
                String path = request.getContextPath();
                String allModIds = "";

            %>
            <form name="myForm2" method="post">
                <INPUT TYPE="hidden" name="path" id="path" value="<%=path%>">
                <INPUT TYPE="hidden" name="scenarioName" id="scenarioName" value="<%=scenarioName%>">
                <Table>
                    <%
                    String tempId = "";
                    for (int h = 0; h < pbro.getRowCount(); h++) {
                    allModIds = allModIds + "," + pbro.getFieldValueString(h, "MODEL_ID");
                    tempId = pbro.getFieldValueString(h, "MODEL_ID");
                    %>
                    <Tr>
                        <Td>
                            <%
                            if(! selectedSeededModels.equalsIgnoreCase("")) {
                                
                                    if(seededModels.contains(tempId)) {
                                %>
                                <Input type="CHECKBOX" CHECKED id="<%=pbro.getFieldValueString(h, "MODEL_ID")%>" value="<%=pbro.getFieldValueString(h, "MODEL_ID")%>" name="<%=pbro.getFieldValueString(h, "MODEL_ID")%>">
                                <%
                                    }                                    
                                    else {
                                    %>
                                 <Input type="CHECKBOX" id="<%=pbro.getFieldValueString(h, "MODEL_ID")%>" value="<%=pbro.getFieldValueString(h, "MODEL_ID")%>" name="<%=pbro.getFieldValueString(h, "MODEL_ID")%>">
                                <%
                                    }
                            }
                            else {
                            
                            %>
                            <Input type="CHECKBOX" checked id="<%=pbro.getFieldValueString(h, "MODEL_ID")%>" value="<%=pbro.getFieldValueString(h, "MODEL_ID")%>" name="<%=pbro.getFieldValueString(h, "MODEL_ID")%>">
                            <%
                            }
                            %>
                        </Td>
                        <Td>
                            <%=pbro.getFieldValueString(h, "MODEL_NAME")%>
                        </Td>
                    </Tr>
                    <%
                    }
                   if (allModIds.length() > 1) {
                       allModIds = allModIds.substring(1);
                   }
                    %>
                </Table>
                <INPUT TYPE="hidden" name="allModIds" id="allModIds" value="<%=allModIds%>">
            </form>
        </center>
        <center>
            <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveSeededModels()">
            <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelSeededModels()">
        </center>
    </body>
</html>
