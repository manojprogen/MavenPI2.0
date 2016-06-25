<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,prg.db.PbReturnObject,prg.db.PbDb"%>
<%--
    Document   : defineRelatedMeasures
    Created on : 31 Oct, 2012, 4:38:57 PM
    Author     : Anil
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

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

             //added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
           
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
        <script type="text/javascript" src="<%=contextPath%>/reportDesigner.css"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
         <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>
         <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
         <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>

        
    </head>
    <%
                   try{
                          PbDb pbdb = new PbDb();
                      String AvailableFiolers=  "SELECT FOLDER_ID, GRP_ID, FOLDER_NAME FROM PRG_USER_FOLDER";
                        PbReturnObject folderpbro = pbdb.execSelectSQL(AvailableFiolers);
         %>
    <body>
         <script type="text/javascript">

            var grpColArray=new Array();
            var grpColArray1=new Array();
            var count=1;
            var dropid='';
          var eleID="";
          var rtMeasures=new Array();
              $(document).ready(function() {
                 
            });
            



           function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);;
                var x=index.replace("GrpCol","");
                var i=0;

                for(i=0;i<grpColArray1.length;i++){
                    if(grpColArray1[i]==x){
                        grpColArray1.splice(i,1);
                        grpColArray.splice(i,1);
                }
                count=1;
            }
           }
             function createColumn(elmntId,elementName){

                var parentUL=document.getElementById("sortable");
                var x=grpColArray1.toString();
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId.split("~")[0]);
                      grpColArray1.push(elmntId);
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
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
                $(".sortable").sortable();
                $(".sortable").disableSelection();
                
            }
          
           function getMeasuress(){

              var bsrSelectedID= $("#bsrSelect").val();
               $.post(
                        'userLayerAction.do?userParam=getDefineMeasures&bsrSelectedID='+bsrSelectedID+"&contextPath="+"<%=contextPath%>",
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
                });
               
                var dragMeasure=$('#kpiTree > li > ul > li > ul > li > span,#kpiTree > li > ul > li >  span')
              var dropMeasures=$('#dropTabs');
               var dropMeasures1=$('#relateddropTabs1');
//               alert(dropMeasures1)
//               alert(dropMeasures)

// $(dragMeasure).dblclick(function(event) {
//                            var elementId=event.target.id;
//                            var pElementName=$("#"+elementId).html();
//                            var obj=document.getElementById(elementId).parentNode;
//                            var ulobj=obj.getElementsByTagName("ul");
//                            if(ulobj!=null&&ulobj[0]!=null&&ulobj[0]!=undefined){
//                            var liobjects=ulobj[0].getElementsByTagName("li");
//                            var spanObj;
//                            createColumn(elementId,pElementName,"sortable");
//                            for(var i=0;i<liobjects.length;i++)
//                               {
//                                   spanObj=liobjects[i].getElementsByTagName("span");
//                                  createColumn(spanObj[0].id,spanObj[0].innerHTML,"sortable");
//
//                               }
//                       }
//
//             });










                 $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropMeasures).droppable(
                {
                    
                    activeClass:"blueBorder",
                    accept:'#kpiTree > li > ul > li > ul > li > span,#kpiTree > li > ul > li >  span',
                    drop: function(ev, ui) {
                       
                        
                        var measure=ui.draggable.html();
                       
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        
                        
                        
                    }
                });
//                 $(dropMeasures1).droppable(
//                {
//                    activeClass:"blueBorder",
//                    accept:'#kpiTree > li > ul > li > ul > li > span,#kpiTree > li > ul > li >  span',
//                    drop: function(ev, ui) {
//                        var measure=ui.draggable.html();
//                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
//                    }
//                });


//                $("#dropTabs").droppable({
//                    activeClass:"blueBorder",
//                    accept:'.myDragTabs',
//                    drop: function(ev, ui) {
//                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
//                    }
//                }
//            );
//                    $(".1_span").contextMenu({menu:"mymenu",rightButton: true},
//                   function(action, el, pos) {
//                       var val=el.attr("id");
//                       var names=val.split("~");
//                        eleID=names[0];
//                        alert(eleID)
//                     var name=names[1];
//                     $("#elementname").val(name);

                  // });
      $(".1_span").bind('dblclick', function(event) {
                 // alert($(event.eventData).attr('id'));
                 // alert($(event.attr('id')));
                // alert($(event.pageX + "," + event.pageY));
               var val=$(event.target).attr('id');
                       var names=val.split("~");
                        eleID=names[0];
                        //alert(eleID)
                     var name=names[1];
                     $("#elementname").val(name);
                   });




                        });
                 }
                 var ids = new Array();
                 var names1 = new Array();
                 var idsNew = new Array();
                 var names1New = new Array();
                 function viewOldRelMeasures(busRoleId){
                 idsNew = new Array();
                 names1New = new Array();
                    $.post(
                        'userLayerAction.do?userParam=getOldRelatedMeasures&busRoleId='+busRoleId,
                        function(data){
                            $("#oldRelatedMeasures").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                        var jsonVar=eval('('+data+')')
                        var jsonvalues = [];
                        var htmlVar = "";
                        var keys = [];
                        for (var key in jsonVar) {
                            if (jsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        htmlVar = "<table>"
                        for(var i=0;i<keys.length;i++)
                        {
                            ids.push(keys[i]);
                            names1.push(jsonVar[keys[i]]);                            
                            htmlVar += "<tr id='"+jsonVar[keys[i]]+"'><td id='"+keys[i]+"'>";
                            htmlVar += "<img align=\"middle\" SRC='<%=request.getContextPath() %>/icons pinvoke/cross-small.png' onclick=\"deleteColumn1('"+keys[i]+"','"+jsonVar[keys[i]]+"')\" BORDER=\"0\" title=\"Delete Related Measure\" />";
                            htmlVar += ""+jsonVar[keys[i]]+"";
                            htmlVar += "</td></tr>";
                        }
                        htmlVar += "</table>"
                        $("#tablebuttonTab").html(htmlVar)
                        $("#donebuttonTab").html("<tr><td><input type=\"button\" name=\"Done\" class=\"navtitle-hover\" value=\"Done\" onclick=\"deleteRelatedMeasures()\" ></td></tr>")                    
                        $("#oldRelatedMeasures").dialog('open');
                        });                        
                 }
                 function deleteRelatedMeasures(){
                 $("#oldRelatedMeasures").dialog('close');
                    $.post(
                        'userLayerAction.do?userParam=deleteRelateMeasures&idsNew='+idsNew+'&names1New='+names1New,
                        function(data){
                            if(data == "true")
                                alert("Related Measures Deleted successfully")
                            else
                                alert("Related measures not deleted Successfully")
                        });
                        window.location.href = window.location.href;
                 }
                 function deleteColumn1(index,name){
                     idsNew.push(index);
                     names1New.push(name);
                    var LiObj=document.getElementById(index);
                    var ULObj=document.getElementById(name);
                    ULObj.removeChild(LiObj);
                    var i=0
                        for(i=0;i<ids.length;i++){
                        var idVals = ids[i].replace(" ", "", "gi");                        
                        var idVal = index.trim();
                            if(idVals==idVal){
                                ids.splice(i, 1);
                            }
                        }
                        for(i=0;i<names1.length;i++){
                            var nameVals = names1[i].trim();
                            var nameVal = name.trim();                           
                            if(nameVals==nameVal){
                                names1.splice(i, 1);
                            }
                        }                        
                }
                 function mappedElements(){
            var ele=$("#elementname").val();
            if(ele==""){
               alert("please Select the measures for Mapping..")
           }else if(grpColArray.length>0){
            var mappedEle=dropid.split("~")[0];
           // alert(mappedEle)
             $.post(
                        'userLayerAction.do?userParam=mappedRelatedMeasure&eleID='+eleID+"&mapID="+grpColArray+"&contextPath="+"<%=contextPath%>",
                        function(data){
                           if(data){
                               alert(data)
                              $("#dataDivId").html(" ");
                               $("#bsrSelect").val("1");
                           }

                        });
                }else{
                      alert("please drag atleast one measure for Mapping")
                }
                }

</script>
        <ul id="mymenu" class="contextMenu"><li class="select"><a href="#select">select</a></li></ul>
         <form name="targetForm" method="post">
            <table style="width:100%">  <tr>
                    <td align="right"><h5><%=TranslaterHelper.getTranslatedInLocale("business_role", cle)%></h5></td>
                    <td align="left">
                        <select id="bsrSelect" name="BusinessRoles" onChange="getMeasuress()">
                <option value="1"> ---Select---</option>
       <%
                for(int i=0;i<folderpbro.getRowCount();i++){%>
                <option value="<%=folderpbro.getFieldValueString(i,0)%>">
                <%=folderpbro.getFieldValueString(i,2)%></option>
          <%}
            }  catch (Exception e) {
                                    e.printStackTrace();
                       }%>
            </select>
                    </td>
                </tr>
           </table>
       <div id="dataDivId" style="height:250px;"> </div>
     </form>
            <div id="oldRelatedMeasures" style="display:none;">
            <table align="right" id="tablebuttonTab"></table>
            <table align="right" id="donebuttonTab"></table>
            </div>
    </body>
</html>
