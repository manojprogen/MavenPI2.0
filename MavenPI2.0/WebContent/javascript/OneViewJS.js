/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




 function oneViewSettings(){
        parent.$("#oneViewSettings_Dialoge").dialog({
                        autoOpen: false,
                        height: 150,
                        width: 190,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
               parent.$("#oneViewSettings_Dialoge").dialog('open')
    }
// var filterMap1 = {};
// var viewid=[];
// var filternames=[];
//    function applyGlobalFilterinOneview(seletedid,id){
//
//    var filterMap = {};
//    var filterValues=[];
//    var filterValues1=[];
//    var id1 ,name1 ,idname;
//    id1 = id.split("-")[2];
//    idname = id.split("-")[0];
//    var selectid=id.split("-")[1];
//var paramid="paramList_"+id1;alert(paramid)
// var name11=$("#"+seletedid).text();
//var nameslist=[];alert(document.getElementById("paramList_"+id1).value)
////    var view = parent.$("#globalviewby").val();
//filterMap=parent.document.getElementById("paramList_"+id1).value;alert(filterMap)
////    filterMap=list;
//    var viewIds = parent.$("#globalviewbyIds").val();
//    var index = view.indexOf(idname);
//    if(index==-1){
//        index = view.indexOf(" "+idname);
//    }
//
//    name1 = viewIds[index];
//
//
//filterValues=filterMap[id1];
//var filterValues1=filterValues;
//index=viewid.indexOf(id1);
//viewid.splice(index, 1);
//filternames.splice(index, 1);
//  viewid.push(id1);
//  filternames.push(filterValues[id.split("-")[1]]);
//          var index = filterValues.indexOf(filterValues1[id.split("-")[1]]);
////          var index = filterValues.indexOf(filterData[name][id.split("_")[1]])
//
//        filterValues.splice(index, 1);
////}
//        filterMap[id1]=filterValues;
//
//        if(filterMap1!=null){
//
//
//        filterMap1[id1]=filterValues;
//        }else{
//            filterMap1=filterMap;
//        }
//        if(selectid=='00'){
//for (var prop in filterMap1) {
////        if (obj.hasOwnProperty(prop) && reg.test(prop)){
//if(id1==prop){
//            delete filterMap1[prop];
//             var index = viewid.indexOf(id1);
//            viewid.splice(index, 1);
//            filternames.splice(index, 1);
//
//}
////        }
//    }
//        }
//        parent.$("#viewbygblids").val(JSON.stringify(viewid));
//        parent.$("#viewbygblname").val(JSON.stringify(filternames));
////    }
//    parent.$("#filters1").val(JSON.stringify(filterMap1));
////    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
////        function(data){
////            if(parent.$("#type").val()==="advance"){
////                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
////            }else{
////                generateChart(data);
////            }
////        });
//}
function applyGlobalFilter(ctxPath,oneviewId,oneviewName) {
 $("#AddMoreParamsDiv1").dialog({
                     autoOpen: false,
                     height: 550,
                     width: 550,
                     position: 'justify',
                     modal: true,
                     resizable:true
                });
                 $.post(ctxPath+"/reportTemplateAction.do?templateParam=addMoreDimensionsfilter&IsrepAdhoc=true&REPORTID="+parent.oneviewID+"&oneviewname="+oneviewName,
             function(data){

             });
                    var frameObj=parent.document.getElementById("addmoreParamFrame1");
//                    var source="reportTemplateAction.do?templateParam=addMoreDimensionsfilter&IsrepAdhoc=true&REPORTID="+parent.oneviewID+"&oneviewname="+oneviewName;
//                    frameObj.src=source;
                 var source = ctxPath+"/Report/Viewer/editCharts.jsp?fromoneview=true&REPORTID="+oneviewId+"&ctxPath="+ctxPath+"&oneviewname="+oneviewName;
                    frameObj.src=source;
                    parent.$("#AddMoreParamsDiv1").dialog('open');

 }
//code added by bhargavi
function applyGlobalFilter1(ctxPath,oneviewId,oneviewName){

     parent.$("#applyGlobalFilter").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 550,
                      width:500,
                      modal: true,
                      position:'justify'
                     });
   //parent.$("#applyGlobalFilter").dialog('open');
  $.post(ctxPath+'/reportViewer.do?reportBy=oneviewGlobalFilter&oneviewId='+parent.oneviewID,
         function(data){
             alert(data)
           var jsonVar = eval('('+data+')');
           var ul = document.getElementById("sortable");

           var htmlVar="<table><tr><td align=\"center\"><input type=\"button\" name=\"Done\" class=\"navtitle-hover\" value=\"Done\"  onclick=\"saveFiltersApplied('"+ctxPath+"','"+parent.oneviewID+"','"+oneviewName+"')\"></td></tr></table>";
                 parent.$("#applyGlobalFilter").html(jsonVar.htmlStr+htmlVar);
                 isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel;
                  $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        });

                $(".myDragTabs").draggable({
                            helper:"clone",
                            effect:["", "fade"]
                        });


                $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn1(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
            );
                grpColArray=jsonVar.memberValues
                $(".sortable").sortable();
 parent.$("#applyGlobalFilter").dialog('open');

    });

}
function saveFiltersApplied(ctxPath,reportId,oneviewName){
            var ul = document.getElementById("sortable");
            var msrIdsArray = new Array();
            var colIds;
            if(ul!=undefined || ul!=null){
                colIds=ul.getElementsByTagName("li");
                  if(colIds!=null && colIds.length!=0){
                    for(var i=0;i<colIds.length;i++){
                       var val=colIds[i].id.split("_")[0];
                       msrIdsArray.push(val);
                       }
                   }
              }
//              alert(msrIdsArray.toString())
           $.post(ctxPath+'/reportViewer.do?reportBy=saveGlobalFilterOneview&oneviewId='+reportId+'&filterParameters='+msrIdsArray,
             function(data){
//               parent.$("#applyGlobalFilter").dialog('close');
//                 var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId;
//                var dSrc = document.getElementById("iframe1");
//                dSrc.src = source;
             });

    viewOneBy(reportId,oneviewName);

}
//end of code
function seurityenable(dashletid,oneviewid,curval,reportid,reportname){
//var enableseurity="true";
var divId=dashletid;
var oneviewId=oneviewid
               if(parent.document.getElementById("enableseurity").checked){
//                   enableseurity="false";
                parent.document.getElementById("enableseurity").checked = true;
//                parent.oneviewTimecheck="false"
               }
                parent.document.getElementById("desableseurity").checked = false;
$.ajax({
                        url: 'oneViewAction.do?templateParam2=measuresecurity&regionId='+divId+'&oneviewId='+oneviewId+"&enableseurity=true",
                        success: function(data){
                        }
                    });

}
function seuritydesable(dashletid,oneviewid,curval,reportid,reportname){
//var enableseurity="true";
var divId=dashletid;
var oneviewId=oneviewid
               if(parent.document.getElementById("desableseurity").checked){
//                   enableseurity="false";
                parent.document.getElementById("desableseurity").checked = true;
//                parent.oneviewTimecheck="false"
               }
                parent.document.getElementById("enableseurity").checked = false;
$.ajax({
                        url: 'oneViewAction.do?templateParam2=measuresecurity&regionId='+divId+'&oneviewId='+oneviewId+"&enableseurity=false",
                        success: function(data){
                        }
                    });

}
    function saveSettings(){
        var seType=parent.$("input[name='oneSetting_Radio']:checked").val();
        var isEveryTime=parent.$('#one_EveryTime').is(':checked');
//        alert(seType)
//        alert(isEveryTime)
//        alert(parent.oneviewID);
        $.ajax({
                    url: "dashboardViewer.do?reportBy=oneViewSettings&oneViewId="+parent.oneviewID+"&seType="+seType+"&isEveryTime="+isEveryTime,
                    success: function(data){
//                        alert(data);

                        alert('Assigned Successfully..!\nPlease reopen for updated OneView.');
                        parent.$("#oneViewSettings_Dialoge").dialog('close');
                    }
                });
    }
    function copyOneView(oldViewId, oldViewName){
    var inHtml='<table width="100%">'
+'<tr><td class="myhead">New OneView Name</td>'
+'<td><input type="text" class="myTextbox5" id="newOneViewName"><td>'
+'</tr>'
+'<tr/><tr><td/><td align="left" rowspan="1"><input  type="button" name="save" value="save" class="navtitle-hover" onclick="saveAsNew(\''+oldViewId+'\')"/></td></tr>'
+'</table>';
    parent.$("#oneView_Copy_dialogue").html(inHtml);
    parent.$("#oneView_Copy_dialogue").dialog({
           autoOpen: false,
           height: 120,
           width: 345,
           position: 'justify',
           modal: true,
           resizable:true
                               });
               parent.$("#oneView_Copy_dialogue").dialog('open');

    }

// Added By Ram
function showmeasures(){
    seType1="Measure";
      $("#oneviewVersion").val("2.5");
       parent.$("#oneViewlist1").hide();
//   if(isdisplay==''|| isdisplay!='true'){
//$('#regionTableId').append($("<div id= 'listofgraphs'  style='position:absolute;height:700px; width:300px; top:0px; left:5px; border: 1px solid skyblue; background-color: white;'ondrop=\"drop(event)\"  ondragover=\"allowDrop(event)\"></div>"));
isdisplay='true';
nograph='true';
parent.$('#listofgraphs').append($("#measureDialogId"));
$("#measureDialogId").show();
//$("#fixedDiv").show();
parent.$("#listofgraphs").show("slide",{
            derectin:'right'
        },"slow");
//   }else{
//       parent.$('#listofgraphs').append($("#measureDialogId"));
//       nograph='true';
//$("#measureDialogId").show();
////$("#fixedDiv").show();
//parent.$("#listofgraphs").show("slide",{
//            derectin:'right'
//        },"slow");
//
//}
}
function hideaddreport(){

//   edited by manik
   parent.$("#listofgraphs").hide("clip",600);
}
    function showreports(){

      $("#oneviewVersion").val("2.5");
      seType1="Reports";
       parent.$("#oneViewlist1").hide();
//  if(isdisplay==''|| isdisplay!='true'){
//$('#regionTableId').append($("<div id= 'listofgraphs'  style='position:absolute;height:800px; width:250px; top:0px; left:5px; border: 1px solid skyblue; overflow-y:auto; background-color: white;'ondrop=\"drop(event)\"  ondragover=\"allowDrop(event)\"></div>"));
isdisplay='true';
nograph='true';
parent.$('#listofgraphs').append($("#dialogTestByrep"));
$("#dialogTestByrep").show();
//$("#fixedDiv").show();
parent.$("#listofgraphs").show("slide",{
            derectin:'right'
        },"slow");
//   }else{
//       alert("comes inside else")
//       parent.$('#listofgraphs').append($("#dialogTestByrep"));
//       nograph='true';
//$("#dialogTestByrep").show();
////$("#fixedDiv").show();
//parent.$("#listofgraphs").show("slide",{
//            derectin:'right'
//        },"slow");

//}
}
 //Added By Ram
    function createfixedDivRegion(oneViewName,oneViewIdValue){
parent.$('#listofgraphs').append($("#dialogTestByrep"));
$("#dialogTestByrep").show();
//alert(oneViewName)
//alert(oneViewIdValue)
//parent.$("#listofgraphs").hide();
//$("#dialogTestByrep").hide();
$("#dialogTestForKpis").hide();
$("#measureDialogId").hide();
//legendsregions //parent.$("#legendsregions").show("slide",{direction:'right'},"slow");
//    parent.$("#oneViewlist1").toggle(200);


//    parent.$("#listofgraphs").toggle("fold",200);
//    parent.$("#listofgraphs").toggle("puff",500);
    parent.$("#listofgraphs").toggle("clip",600);


//     parent.$("#oneViewlist1").dialog({
//                        autoOpen: false,
//                        height: 150,
//                        width: 190,
//                        position: 'justify',
//                        modal: true,
//                        resizable:false
//                    });

// $('#regionTableId').html(innerHTML);
//   <div id='oneViewlist'  title='Settings'>
//                    <table align='center' border="0" width="100%">
//                        <tr><td><input id='one_YesterDay' type='radio' name='oneSetting_Radio' value='YesterDay' align='left'/>&nbsp; &nbsp;YesterDay</td></tr>
//                        <tr><td><input id='one_ToDay' type='radio' name='oneSetting_Radio' value='ToDay' align='left'/>&nbsp; &nbsp;ToDay</td></tr>
//                        <tr><td style='border-bottom-style:dashed;border-bottom-color:grey;border-width:1.8px;'><input id='one_Tomorrow' type='radio' name='oneSetting_Radio' value='Tommarrow' align='left'/>&nbsp; &nbsp;Tomorrow</td></tr>
//                        <tr><td><input id='one_EveryTime' type='checkbox' value='EveryTime' align='left'/>&nbsp; &nbsp;Every Time</td></tr>
//                        <tr/><tr/><tr><td align='center'><input  type='button' name='save' value='save' class='navtitle-hover' onclick="saveSettings()"/></td></tr>
//                    </table>
//                </div>

//parent.$("#oneViewlist1").show("slide",{
//            derectin:'right'
//        },"slow")
}
    function saveAsNew(oldViewId){
var newOneViewName=parent.$("#newOneViewName").val();
                       if(newOneViewName==""){
                           alert('Please Provide Name For New OneView...!!');
                       }else{
                       $.ajax({
                        url: "reportViewer.do?reportBy=saveAsNewOneView&oldoneViewId="+oldViewId+"&newOneViewName="+newOneViewName+"&filters1="+parent.$("#filters1").val(),
                        success: function(data){
                            //alert(data);
                            parent.$("#oneView_Copy_dialogue").dialog('close');
                           viewOneBy(newOneViewName,data)
                        }
                    });
                   }
}
function writeNote(oneViewId,viewletId){
var writeTd="<td align='right' class='removableTd'><input type='button' class='navtitle-hover' name='write' value='write' onclick='appendNote(\""+oneViewId+"\",\""+viewletId+"\")'></td>"
parent.$("#editableNoteTR").find(".removableTd").remove();
parent.$("#editableNoteTR").append(writeTd);
parent.$("#onViewNoteContentEditable").val("");
parent.$("#noteWriterId").val("");
        parent.$("#writeNoteDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 450,
                        position: 'justify',
                        modal: true,
                        title:'Notes'
                    });
              parent.$("#writeNoteDiv").dialog('open')

}
function appendNote(oneViewId,viewletId){
    var writeContent=parent.$("#onViewNoteContentEditable").val();
    var writerName=$("#noteWriterId").val();
//    alert('writeContent:  '+writeContent+' writerName:'+writerName);
    $.ajax({
                    url: 'oneViewAction.do?templateParam2=appendOneViewNote&oneViewId='+oneViewId+"&viewletId="+viewletId+'&note='+writeContent+"&noteWriter="+writerName,
//                    +"&noteWriter="+writerName

                    success: function(data) {
//                        alert(data);
                        parent.$("#note-"+viewletId).html(data);
                        parent.$("#writeNoteDiv").dialog('close');
                        if(writerName !=""){
                            parent.$("#Dashlets"+viewletId).html(writerName);
                    }
                    }

            });

}
function deleteNote(oneViewId,viewletId,writerName,isAll,index){
$.ajax({
                    url: 'oneViewAction.do?templateParam2=appendOneViewNote&oneViewId='+oneViewId+"&viewletId="+viewletId+'&isAll='+isAll+"&noteWriter="+writerName+"&index="+index+"&isDelete=true",

                    success: function(data) {
//                        alert(data);
                        parent.$("#note-"+viewletId).html(data);
                       }

            });
}

function oneViewPdfSettings(){
    parent.$("#pdfDialog").dialog({
           autoOpen: false,
           height: 120,
           width: 345,
           position: 'justify',
           modal: true,
           resizable:true,
           title:'Download As PDF'
         });
         $("#loadPdfDiv").hide();
               parent.$("#pdfDialog").dialog('open');

    }

function oneViewAsPDF(){
    var assignedDetails="";
    var ids="";
    var pageType=parent.$("#pdfTypeSelect").val();
    var fitTo= "SinglePage"; //parent.$("#pdfPageFitSelect").val();
//    $("#Dashlets-"+viewLetId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="50px" height="50px"  style="position:absolute" /></center>');
    //alert(pageType + '--' +fitTo);
    $("#loadPdfDiv").show();
    $.ajax({
                    url: 'oneViewAction.do?templateParam2=getAssignedGraphDetails&oneViewId='+parent.oneviewID,

                    success: function(data) {
                        if(data=='Not Support'){
                            alert(" Your on the older version of OneVIew please click Go and Save to download as PDF");
                        }else{

                        //alert(data);
                    var json=eval('('+data+')');
                    assignedDetails=new Array(json.details.length);

                    for(var i=0;i<json.details.length;i++){
                        var encodeImgStr=parent.graphImage("chart-"+json.details[i],"0","PDF");
                        $.ajax({
                            type:"POST",
                            datatype:"text",
                            data:{
                                base64data:encodeImgStr,
                                oneViewId:parent.oneviewID,
                                viewletId:json.details[i]
                            },
                                url:'oneViewAction.do?templateParam2=saveCanvasImage',
                                success: function(data) {
                            }

                        });

                    }
                     $.ajax({
                            type:"POST",
                            datatype:"text",
                            data:{
                                oneViewId:parent.oneviewID,
                                viewletId:json.details[i]
                            },
                                url:'oneViewAction.do?templateParam2=buildOneViewPdf'+'&pageType='+pageType+'&fitTo='+fitTo,
                                success: function(data) {
                                //alert('goinf=g to pbDownload.jsp' );
                                parent.$("#pdfDialog").dialog('close');
                                var source = parent.ctxpath+'/TableDisplay/pbDownload.jsp?dType=oneviewPdf';
                                var dSrc = parent.document.getElementById("oneFrame");
                                dSrc.src = source;
                        }

                        });
                    }

              }

       });

}
    //sandeep
function addgblfilterui(data){
//     $("select").multiselect({
//   selectedText: "# of # selected",
//   selectedList: 3
//   });
           parent.$("#globalDiv").html(data);
              parent.$("#globalDiv").show();

}
var regid=0;
    //sandeep
function insertNamegrid(){

                //                $("#onveViewByName").html($("#viewBydivid").val());
                parent.oneViewName= parent.$("#viewBydivid").val();
                 parent.oneviewHeight= parent.$("#oneviewRegHeightId").val();
                 parent.oneViewType= parent.$("#oneViewType").val();
                 parent.oneViewBusRole= parent.$("#roleId").val();
                 parent.$("#oneviewType").val(parent.oneViewType);
//                var oneId="";
                var innerWidth=parseInt(window.innerWidth);
                 parent.timeDetail =  parent.$("#time").val();
                if( parent.oneViewName!=''){
                     parent.$("#tabs").hide();
                    $("#headTable").css("display", "none"); //added by manik    
//                 parent.$("#tabIds").html(parent.oneViewName);
//                 parent.$("#onveViewByName").html(parent.oneViewName);
                $.ajax({
                    url: parent.ctxpath+'/reportViewer.do?reportBy=insertOneViewName&oneviewName='+encodeURIComponent(parent.oneViewName)+'&oneViewType='+parent.oneViewType+'&roleId='+parent.oneViewBusRole+'&innerWidth='+innerWidth+'&datetype='+parent.timeDetail,
                    success: function(data) {
//                        alert(data)
                        if(data!=""){
                         parent.oneViewIdValue=data;
                        }
                         parent.$("#br1").remove("br");
                 parent.$("#br2").remove("br");

                                 parent.$("#regionTableId").show();

 var source = parent.ctxpath+'/newOneView.jsp?action=desginer&oneviewname='+parent.oneViewName+'&oneViewIdValue='+data;
                        var dSrc = parent.document.getElementById("regionTableIdgrid");
                        dSrc.src = source;

                 parent.$("#designID").hide();
                 parent.$("#showNameId").show();
                 parent.$("#designValueId").show();
                 parent.$("#footID").hide();
//                 parent.$("#tabs").show();
//                 parent.$("#createdId").show();
                //                $("#datedetailId").show();
                //                $("#timedetailsId").show();
//                 parent.$("#savebuttonId").show();
//                 parent.$("#favreportid").show();
//                 parent.$("#cacheId").show();
//                  parent.$("#toogleregion1").show();
                 parent.$("#busroleId").show();
                 parent.$("#busroleTdId").show();
                 parent.$("#busroleForKpsId").show();
                 parent.$("#busNamId").show();
//                 parent.$("#clearId").show();
                 parent.$("#designId").show();
                 parent.$("#divIdTest").hide();
                 parent.$("#divIdTest1").hide();
                 parent.$("#paraminfoId").hide();
                 parent.$("#oneviewbyNameId").dialog('close');
                  if( parent.oneViewType=="Business TemplateView" ||  parent.oneViewType=="Measure Based Business Template"){
                      $("#action1").val("fromDesigner");
                    AddMoreDimension('<%=request.getContextPath()%>', parent.oneViewIdValue);

                  }
                    }
                });

                }
                else{
                alert("Please Enter Oneview Name!");
                }
                //alert(oneViewIdValue)

            }
           
//sandeep
      function settimeto(){
//              document.getElementById("measureType"+divId).style.display='none';
var divId=parent.colNumber;
var oneviewId=parent.oneviewID
               if(parent.document.getElementById("reportTime").checked){
                parent.document.getElementById("reportTime").checked = false;
//                parent.oneviewTimecheck="false"
               }
                parent.document.getElementById("oneviewTime").checked = true;
//                if(document.getElementById("reigonOptionsDivId"+divId)!=null && document.getElementById("reigonOptionsDivId"+divId).style.display=='block'){
//                 $("#reigonOptionsDivId"+divId).toggle(500);
//                }
parent.oneviewTimecheck="false"
                $.ajax({
                        url: 'oneViewAction.do?templateParam2=oneviewAndReportTimeDeatails&regionId='+divId+'&oneviewId='+oneviewId+"&oneviewTime=false",
                        success: function(data){
                        }
                    });
            }
      function settimetoreport(){
//              document.getElementById("measureType"+divId).style.display='none';
var divId=parent.colNumber;
var oneviewId=parent.oneviewID
               if(document.getElementById("oneviewTime").checked){
                     document.getElementById("oneviewTime").checked = false;
//                     parent.oneviewTimecheck="true"
               }
                document.getElementById("reportTime").checked = true;
//                if(document.getElementById("reigonOptionsDivId"+divId)!=null && document.getElementById("reigonOptionsDivId"+divId).style.display=='block'){
//                 $("#reigonOptionsDivId"+divId).toggle(500);
//                }
   parent.oneviewTimecheck="true"
                $.ajax({
                        url: 'oneViewAction.do?templateParam2=oneviewAndReportTimeDeatails&regionId='+divId+'&oneviewId='+oneviewId+"&oneviewTime=true",
                        success: function(data){
                        }
                    });
            }
function createcacheobject(ctxpath,oneviewid){
    $.post(ctxpath+"/reportViewer.do?reportBy=addNewChartsUI&fromoneview=true&oneviewid="+oneviewid,
                             function(data){
                                 parent.$("#loading").hide();
                                 alert("oneview cache object created");
                            }

                )

}
//saneep
 var paramters=[];
  var paramtersids=[];
    function getglobalparamvales(id,paramvale,paramid){

var  selectedparam = parent.$("#"+id).val();
selectedparam=selectedparam;
 paramters.push(selectedparam);
 paramtersids.push(paramid);
 parent.$("#globalparams").val(paramters);
 parent.$("#globalparamsids").val(paramtersids);
}
function createcacheobject(ctxpath,oneviewid){
    $.post(ctxpath+"/reportViewer.do?reportBy=addNewChartsUI&fromoneview=true&oneviewid="+oneviewid,
                             function(data){
                                 parent.$("#loading").hide();
                                 alert("oneview cache object created");
                            }

                )

}
//function gotForOneview1(action1){
//    var oneviewID=parent.oneviewID;
//    var oneviewName=parent.oneviewName;
//
//var value= parent.document.getElementById("regionTableId");
//                $("#designId").css({'height':'400px'})
//                 value.innerHTML='<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>';
////                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"'+parent.ctxpath+'>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
//                $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=getingOneviews&reportBy=viewReport&oneViewIdValue='+oneviewID+'&oneviewName='+oneviewName+'&action=global',parent.$("#oneviewgraphForm").serialize(),
//                function(data){
//                    if(data!='NO DATA'){
//                        document.getElementById("designId").style.display='';
//                        $("#designId").css({'padding-bottom':''})
//                        $("#designId").css({'height':''})
////                        document.getElementById("regionTableId").innerHTML=data;
//                        $("#regionTableId").html(data);
//                          buildd3chartsglobal(oneviewID);
//
//
//                         var size;
//                     var visible;
//       $.ajax({
//         url:  parent.ctxpath+'/oneViewAction.do?templateParam2=NoOfRegions&oneViewIdValue='+oneviewID+'&icons=al',
//         success:function(data){
//
//         var  datalist = new Array();
//         datalist = data.toString().split(",");
//         size=datalist[0].replace("[","").replace("]","");
//         visible=datalist[1].replace("[","").replace("]","").trim();
//          var isPowerAnalyserEnableforUser='<%=isPowerAnalyserEnableforUser%>'
//
//      if(visible!='hide'){
//     for(var j=0;j<size;j++){
//       $("#refreshTabId"+j).show();
//       $("#saveTabId"+j).show();
//       $("#optionId"+j).show();
//       $("#optionIds"+j).show();
//       if(oneviewtypedate1=='true'){
// $("#alertTabId"+j).hide();
//       parent.$("#measureNavigateId"+j).hide();
//                        parent.$("#relatedMeasureInfoId"+j).hide();
//       }else{
//          $("#alertTabId"+j).show();
//         $("#measureNavigateId"+j).hide();
//                       $("#relatedMeasureInfoId"+j).hide();
//       }
//         }
//             }
//       else{for(var j=0;j<size;j++){
//               if(oneviewtypedate1=='true'){
//               }else{
//       $("#refreshTabId"+j).hide();
//       $("#saveTabId"+j).hide();
//       $("#optionId"+j).hide();
//       $("#optionId1"+j).hide();
//       $("#alertTabId"+j).hide()
//               }
//         }
//         $("#measureNavigateId"+j).hide();
//                      $("#relatedMeasureInfoId"+j).hide();
//                              }
//             if(isPowerAnalyserEnableforUser == 'false') { //kruthika
//           for(var j=0;j<size;j++){
//        $("#refreshTabId"+j).show();
//       $("#saveTabId"+j).hide();
//       $("#optionId"+j).hide();
//       $("#optionIds"+j).hide();
//       $("#alertTabId"+j).hide();
//         }
//             }
//
//       }
//                });
//                        initCollapser("");
////                         home();
//                    }
//                    else{
//                        $("#designId").css({'height':'400px'})
//                        $("#designId").css({'padding-bottom':''})
//                        var display="<tr><td height='300px' align='center' >NO DATA</td></tr>";
//                        document.getElementById("designId").style.display='';
//                         $("#regionTableId").html(display);
////                        document.getElementById("regionTableId").innerHTML=display;
//
//                    }
//                });
//
//            }
//sandeep
//function gotforoneviewgbl(ivvalue,oneViewName){
//
// var source = parent.ctxpath+'/newOneView.jsp?action=globalftr&oneviewname='+parent.oneViewName+'&oneViewIdValue='+ivvalue;
//                        var dSrc = parent.document.getElementById("regionTableIdgrid");
//                        dSrc.src = source;
//
//
//}
//sandeep
//function buildd3chartsglobal(idvalue,gridster){
//   $.post(parent.ctxpath+'/reportViewer.do?reportBy=gettingJsonData&oneViewIdValue='+idvalue,
//                function(data){
//                     var jsonVar=eval('('+data+')');
//                 var  regionids=jsonVar.regionids
//                 var  reportids=jsonVar.reportids
//                 var  repnames=jsonVar.repnames
//                 var  chartnames=jsonVar.chartnames;
//                 var  busrolename=jsonVar.busrolename;
//                 var  idArr=jsonVar.idArr;
//                 var  drillviewby=jsonVar.drillviewby;
//                 for(var i=0;i<regionids.length;i++){
//                      parent.$("#graphsId").val(reportids[i]);
//                      parent.$("#graphName").val(repnames[i]);
//                      parent.$("#busrolename").val(busrolename[i]);
//                        parent.$("#chartname").val(chartnames[i]);
//                        parent.$("#oneViewId").val(idvalue);
//                        var idArr1=idArr[i];
//
//                  $.ajax({
//                       async:false,
//                       type:"POST",
//                          data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+parent.$("#graphName").val(),
//
//             url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&&fromoneview=true&action=open&fromoneview=true&oneviewid="+idvalue+"&regid="+regionids[i],
//
//                            success: function(data){
//                $("#loading").hide();
//                if(data=="false"){
//
//            }
//            else{
//                var jsondata = JSON.parse(data)["data"];
//                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
//
//                var meta = JSON.parse(JSON.parse(data)["meta"]);
//                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
//                parent.$("#measure").val(JSON.stringify(meta["measures"]));
//                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
//                parent.$("#drilltype").val((meta["drilltype"]));
////                parent.$("#filterMap").val(JSON.stringify(meta["filterMap"]));
//var filterMap1 = {};filterMap1=JSON.stringify(meta["filterMap"]);
////parent.$("#filterMap").val(JSON.stringify(meta["filterMap"]));
//
//            }
//
////                             var  Dashlets="Dashlets-"+regionids[i];
////                              var wid ;
////                        var hgt1 ;
//  var chartData = JSON.parse(parent.$("#chartData").val());
////  chartData[chartnames[i]]["viewBys"][0]=drillviewby[i];
//    parent.$("#chartData").val(JSON.stringify(chartData));
////                        hgt1 = document.getElementById(Dashlets).offsetHeight;
////                        wid = document.getElementById(Dashlets).offsetWidth;
////                        $.post(parent.ctxpath+"/reportViewer.do?reportBy=drillCharts&fromoneview=true&reportName="+parent.$("#graphName").val()+"&reportId="+parent.$("#graphsId").val()+'&chartname='+chartnames[i], parent.$("#graphForm").serialize(),
////        function(data){alert(data)
////
////                generateChart(data);
////
////        });
//         $.ajax({
//                       async:false,
//                       type:"POST",
//                          data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+parent.$("#graphName").val()+'&chartname='+chartnames[i],
//
//             url: parent.ctxpath+"/reportViewer.do?reportBy=drillCharts&fromoneview=true&oneviewID="+idvalue+"&regid="+regionids[i],
//
//                            success: function(data){
//                                 generateglobalDataOneview(gridster,idvalue,regionids[i],chartnames[i],"open",data);
//viewid=[];
//filternames=[];
////generateChart(data);
//                                }
//                     });
//
////                    drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],"globalparam");
////setTimeout(function(){drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i],"open") }, 200);
////                          drillWithinchart1(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i])
//             }
//            });
////                         }
//                 }
//                     });
//
//}
 var regiontdid='';
            var colNumber='';
            var regionnameId='';
var repid='';
var repname='';
var divid='';
            function renameRegion(regionTdid,colNo,regionNameId,divid1,repid1,repname1,onviewId,action){
                regiontdid=regionTdid;
                colNumber=colNo;
                regionnameId=regionNameId;
                oneViewIdValue=onviewId;
                repid=repid1;
                repname=repname1;
                divid=divid1;
//                $("#"+testforRegionOption).hide();
             $(".overlapDiv").hide();
                 $("#renameDivId").dialog({
                        autoOpen: false,
                        height: 110,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                $("#renameDivId").dialog('open');
            }
            var rename=''
            function renamingfunction(){
                var rename=$("#renameId").val();
                var renameMeasure=encodeURIComponent(rename);
                parent.$("#rename").val(renameMeasure);

oneviewupdate(regiontdid,regionnameId,divid,repid,repname,oneViewIdValue,"rename")
                $("#renameId").val('');
                $("#renameDivId").dialog('close');
            }

function buildd3charts(idvalue){
    $.ajax({
        url:'reportTemplateAction.do?templateParam=saveGlobalFilterOneview&oneviewId='+idvalue+'&oneviewopen='+parent.oneviewopen,
        success:function(data)
        {
if(data!='nofilter'){
            parent.$("#globalDiv").html(data);
               parent.$("#globalDiv").show();
}
        }});
   $.post(parent.ctxpath+'/reportViewer.do?reportBy=gettingJsonData&oneViewIdValue='+idvalue,
                function(data){
                     var jsonVar=eval('('+data+')');
                 var  regionids=jsonVar.regionids
                 var  reportids=jsonVar.reportids
                 var  repnames=jsonVar.repnames
                 var  chartnames=jsonVar.chartnames;
                 var  busrolename=jsonVar.busrolename;
                 var  idArr=jsonVar.idArr;
                 var  drillviewby=jsonVar.drillviewby;
                 for(var i=0;i<regionids.length;i++){
                      parent.$("#graphsId").val(reportids[i]);
                      parent.$("#graphName").val(repnames[i]);
                      parent.$("#busrolename").val(busrolename[i]);
                        parent.$("#chartname").val(chartnames[i]);
                        parent.$("#oneViewId").val(idvalue);
                        var idArr1=idArr[i];
//                         $("#chart"+regionids[i]).html('<div class="tooltip" id="my_tooltip" style="display: none"></div>');
                          if(idArr1==null || idArr1=='null'){
// var myVar= setTimeout(function(){ generateJsonDataOneview("Dashlets-"+regionids[i],idvalue,regionids[i],chartnames[i],"add")}, 1000);
//clearTimeout(myVar);
                    setTimeout(function(){ generateJsonDataOneview("null",idvalue,regionids[i],parent.oneViewName,"open",gridster5)}, 1000);
                         }else{
//                                $.post(
//            'reportViewer.do?reportBy=getAvailableCharts&fromoneview=true&reportId='+ reportids[i]+"&reportName="+$("#graphName").val(), $("#oneviewgraphForm").serialize(),
//            function(data) {
                  $.ajax({
                       async:false,
                       type:"POST",
                          data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+parent.$("#graphName").val(),

             url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts&fromoneview=true",

                            success: function(data){
                $("#loading").hide();
                if(data=="false"){

            }
            else{
                var jsondata = JSON.parse(data)["data"];
                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                var meta = JSON.parse(JSON.parse(data)["meta"]);
                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                parent.$("#aggregation").val(JSON.stringify(meta["aggregation"]));
                parent.$("#drilltype").val((meta["drilltype"]));

            }

                             var  Dashlets="Dashlets-"+regionids[i];
                              var wid ;
                        var hgt1 ;
  var chartData = JSON.parse(parent.$("#chartData").val());
  chartData[chartnames[i]]["viewBys"][0]=drillviewby[i];
    parent.$("#chartData").val(JSON.stringify(chartData));
                        hgt1 = document.getElementById(Dashlets).offsetHeight;
                        wid = document.getElementById(Dashlets).offsetWidth;
                    drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i]);
//setTimeout(function(){drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i],"open") }, 200);
//                          drillWithinchart1(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i])
             }
            });
                         }
                 }
                     });

}
function hideicons(j){
//for(var j=0;j<size;j++){
// if(document.getElementById("compareMeasIdfirst"+j)!=null || document.getElementById("compareMeasIdfirst"+j)!=null || document.getElementById("compareMeasIdfirst"+j)!=null){
//           if(document.getElementById("compareMeasIdfirst"+j)!=null && parent.document.getElementById("compareMeasurIdfirst"+j).style.display==''){
                     $("#measureNavigateId"+j).hide();
                      $("#relatedMeasureInfoId"+j).hide();

//                 }
//              else if(document.getElementById("compareMeasIdsecond"+j)!=null && parent.document.getElementById("compareMeasIdsecond"+j).style.display==''){
                    $("#measureNavigateIdsecond"+j).hide();
                      $("#relatedMeasureInfoIdsecond"+j).hide();
//                        }



//                 else if(document.getElementById("compareMeasIdthird"+j)!=null && parent.document.getElementById("compareMeasurIdthird"+j).style.display==''){
                     parent.$("#measureNavigateIdthird"+j).hide();
                      parent.$("#relatedMeasureInfoIdthird"+j).hide();



//                 }
//                  else if(document.getElementById("compareMeasIdfourth"+j)!=null && parent.document.getElementById("compareMeasurIdfourth"+j).style.display==''){
                      $("#measureNavigateIdfourth"+j).hide();
                      $("#relatedMeasureInfoIdfourth"+j).hide();


//                 }
//                  else if(document.getElementById("compareMeasIdfifth"+j)!=null && parent.document.getElementById("compareMeasurIdfifth"+j).style.display==''){
                     $("#measureNavigateIdfifth"+j).hide();
                      $("#relatedMeasureInfoIdfifth"+j).hide();

//                 }





}
function saveEachOneVIewReg(regId){
    //sandeep
var istranseposse=parent.istranseposse;
var graphtoreport=parent.graphtoreport;

       $("#Dashlets-"+regId).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
       $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=saveEachReg&oneViewIdValue='+parent.oneviewID+'&regId='+regId+'&istranseposse='+istranseposse+'&graphtoreport='+graphtoreport+'&action=save',
                function(data){
                    var checkdata=data;
                    if(checkdata == 'Region data not saved Problem occured...!!'){
                        alert('Region data not saved Problem occured...!!');
                        }else{
                        parent.$("#"+regId).html(data);
                                 if(parent.oneviewtypedate1=='true'){
                         parent.$("#measureNavigateId"+regId).hide();
                        parent.$("#relatedMeasureInfoId"+regId).hide();
                         }else{
                            hideicons(regId)
                         }
                    }
                });
   }
function saveEachOneVIewRegd3(regId,chartname,idArr,repId,repname,drillviewby){
    //sandeep
var istranseposse=parent.istranseposse;
var graphtoreport=parent.graphtoreport;
           var  Dashlets="Dashlets-"+regId;
           var busrolename=parent.$("#busrolename").val();
       $("#Dashlets-"+regId).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
       $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=saveEachReg&oneViewIdValue='+parent.oneviewID+'&regId='+regId+'&istranseposse='+istranseposse+'&chartname='+chartname+'&graphtoreport='+graphtoreport+'&idArr='+idArr+'&drillviewby='+drillviewby+'&busrolename='+busrolename,
                function(data){
                    var checkdata=data;
                    if(checkdata == 'Region data not saved Problem occured...!!'){
                        alert('Region data not saved Problem occured...!!');
                        }else{
//                        parent.$("#"+regId).html(data);
var jsonVar=eval('('+data+')');
                                  var  result=jsonVar.result;
                 var  repid=jsonVar.repid
                        parent.$("#"+regId).html(result);
                            $("#Dashlets-"+regId).html("");
                         parent.$("#graphsId").val(repid);
                          parent.$("#graphName").val(repname);
                         var wid ;
                        var hgt1 ;
                        hgt1 = document.getElementById(Dashlets).offsetHeight;
                        wid = document.getElementById(Dashlets).offsetWidth;
//                         $("#Dashlets"+regId).html(chartname);
                        if(idArr=='null'){
                         generateJsonDataOneview("Dashlets-"+regId,parent.oneviewID,regId,chartname,"save");
                        }else{ alert(parent.$("#chartData").val())
                         drillWithinchart11(idArr,"Dashlets-"+regId,wid,hgt1,regId,parent.oneviewID,repname,repId,chartname,"save")
                        }
                                 if(parent.oneviewtypedate1=='true'){
                         parent.$("#measureNavigateId"+regId).hide();
                        parent.$("#relatedMeasureInfoId"+regId).hide();
                         }
                    }
                });
   }
   function showReportOrDBoards(dashletName,folderid,graphid,regId,onviewId,type1){
       var type=$("#selectRepOrDB").val();

//       alert(type);
       parent.repType=type;
      // alert(repType)
       var innerdivofdrill = "<table id='dynamicTableID'><tbody id='grTorepTbody'></tbody><tfoot><tr><td>&nbsp;&nbsp;</td></tr><tr colspan='4' align='center'><td colspan='4' align='center'><input class='navtitle-hover' type='button' value='GO' onclick='graphToRep(  \""+repType+"\", \""+type1+"\")' align='center'/></td></tr></tfoot></table>";
       $("#dynamicTableID").remove();

       if(type=='report'){
                    $.ajax({
                        url: 'dashboardTemplateViewerAction.do?templateParam2=GetReportNamesforGraph&foldersIds='+folderid+'&graphid='+graphid+'&newGraphName='+dashletName+'&fromOneview=true',
                        success: function(data){
                            if(data!='')
                            {
                                var htmlVal=data.split("~")
                                $("#grTorepTbody").html(htmlVal[0]);
                                parent.selectedgrRepId=htmlVal[1];
                                //                                alert("selectedgrRepId\t"+selectedgrRepId)
                            }
                        }
                    });
                }
                else{
                    $.ajax({
                        url: 'oneViewAction.do?templateParam2=GetDashBoardNames&foldersIds='+folderid+'&graphid='+graphid+'&newGraphName='+dashletName+'&fromOneview=true',
                        success: function(data){
                            if(data!='')
                            {
                                var htmlVal=data.split("~")
                                $("#grTorepTbody").html(htmlVal[0]);
                                parent.selectedgrRepId=htmlVal[1];
                                //                                alert("selectedgrRepId\t"+selectedgrRepId)
                            }
                        }
                    });
                }
                 $("#GraphdrillToRep").append(innerdivofdrill);

   }
function getRolesForOneView(oneviewID){
//     $("#GlabalSelectDiv").dialog({
//                        autoOpen: false,
//                        height: 180,
//                        width: 300,
//                        position: 'justify',
//                        modal: true,
//                        resizable:false
//    });
    if(parent.$("#BisiessRole").is(":visible")){
        parent.$("#globalFilterDiv").hide("slide",{derectin:'right'},"slow");
    }else{
         var jsonvalues = [];
         var params = [];
         var dimJsonVar;
    var keys = [];
    var jsonVar;
    var htmlVar="<option value='select'>select</option>";
    var htmlVar1="<option value='select'>--select--</option>";
    $.ajax({
       url: parent.ctxpath+'/oneViewAction.do?templateParam2=getDimesions&oneviewID='+oneviewID,
       success:function(data){
          // alert(data);
          jsonVar=eval('('+data+')');
           var jsonvardim = eval('('+jsonVar.Dimensions+')');
            if(jsonVar.roleId!=null && jsonVar.roleId=='select'){
jsonvarrole='select';
            }else{
           var jsonvarrole = eval('('+jsonVar.roleId+')');
            }
           var jsonglobalparameterVals = jsonVar.globalparameterVals;
           for (var key in jsonvardim) {
               if (jsonvardim.hasOwnProperty(key)) {
                   keys.push(key);
               }
           }
           if(jsonglobalparameterVals != null){
                   params = jsonglobalparameterVals.toString().split("=");
           }
           for(var i=0;i<keys.length;i++){
               jsonvalues = jsonvardim[keys[i]];
               if(jsonvarrole != null && keys[i] == jsonvarrole){
                   htmlVar+="<option selected value='"+keys[i]+"'>"+jsonvalues+"</option>";
               }else{
                   htmlVar+="<option value='"+keys[i]+"'>"+jsonvalues+"</option>";
               }
           }
           if(jsonvarrole != null && jsonvarrole != ""){
           $.ajax({
                url:  parent.ctxpath+'/portalViewer.do?portalBy=getDimensionBasedOnRule&folderId='+jsonvarrole,
                success: function(data){
                    keys = [];
                    dimJsonVar=eval('('+data+')')
                    for (var key in dimJsonVar) {
                            if (dimJsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        for(var i=0;i<keys.length;i++)
                        {
                            jsonvalues = dimJsonVar[keys[i]];
                            if(keys[i] == params[0].replace("{", "", "gi"))
                               htmlVar1+="<option selected value='"+keys[i]+"'>"+jsonvalues+"</option>";
                            else
                               htmlVar1+="<option value='"+keys[i]+"'>"+jsonvalues+"</option>";
                        }
                        $("#selectedMeasure").html(htmlVar1);
                }
         });
          $.post("portalViewer.do?portalBy=getDimensionsForFilter&elementID="+params[0].replace("{", "", "gi")+"&path="+parent.ctxpath,function(data){
                    var jsonVar=eval('('+data+')');
                    var jsonvaleuse="";
                    var htmlVar2="";
                         keys = [];
//                         alert(data)
                    var dimensionNames = jsonVar.dimNames;
           var val = params[1].replace("}", "", "gi")
           val=val.replace("[", "")
           val=val.replace("]", "")
                    for(var i=0;i<dimensionNames.length;i++)
                        {
                            jsonvaleuse=dimensionNames[i]
                            if(jsonvaleuse == val){
                                    htmlVar2+="<option selected value='"+val+"'>"+val+"</option>";
                            }else{
//
                            htmlVar2+="<option value='"+jsonvaleuse+"'>"+jsonvaleuse+"</option>";
                        }
                        }
                        $("#measureValues").html(htmlVar2);
                });
           }else{
               $("#selectedMeasure").html(htmlVar1);
               $("#measureValues").html(htmlVar1);
           }
           parent.$("#BisiessRole").html(htmlVar);
           parent.$("#selectedMeasure").val();
//           parent.$("#globalFilterDiv").effect("slide","slow");
           parent.$("#globalFilterDiv").show("slide",{direction:'left'},"slow");
       }
    });
 }

}
function getDimensionsForOneView(oneviewID){
   var htmlVar="";
                var jsonvalues = [];
                var keys = [];
                var folderId = parent.$("#BisiessRole").val();
                var dimJsonVar;
                 $.ajax({
                url:  parent.ctxpath+'/portalViewer.do?portalBy=getDimensionBasedOnRule&folderId='+folderId,
                success: function(data){
                    dimJsonVar=eval('('+data+')')
                    for (var key in dimJsonVar) {
                            if (dimJsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        for(var i=0;i<keys.length;i++)
                        {
                            jsonvalues = dimJsonVar[keys[i]];
                            htmlVar+="<option value='"+keys[i]+"'>"+jsonvalues+"</option>";
                        }
                        $("#selectedMeasure").html(htmlVar);
                }
         });
}
function getMeasureForOneView(oneviewID){
     var elementID = $("#selectedMeasure").val();
                var contextPathVar=parent.ctxpath;
                $.post("portalViewer.do?portalBy=getDimensionsForFilter&elementID="+elementID+"&path="+contextPathVar,function(data){
                    var jsonVar=eval('('+data+')');
                    var jsonvaleuse="";
                    var htmlVar="";
                    var dimensionNames = jsonVar.dimNames;
                    for(var i=0;i<dimensionNames.length;i++)
                        {
                            jsonvaleuse=dimensionNames[i]
                            htmlVar+="<option value='"+jsonvaleuse+"'>"+jsonvaleuse+"</option>";
                        }
                        $("#measureValues").html(htmlVar);
                });
}

function applyGlobalSave(oneviewID,name){
    if(parent.$("#BisiessRole").is(":visible")){
        parent.$("#globalFilterDiv").hide("slide",{derectin:'right'},"slow");
    }
    var finalFilter = $("#measureValues").val();
    var selectedMeas = $("#selectedMeasure").val();
    var BisiessRole = $("#BisiessRole").val();
    $.ajax({
       url: parent.ctxpath+'/oneViewAction.do?templateParam2=applyGlobalFilter&finalFilter='+finalFilter+'&oneviewID='+oneviewID+'&selectedMeas='+selectedMeas+'&BisiessRole='+BisiessRole,
       success:function(data){
            gotForOneview('Go');
       }
    });
}
function resetGlobalFilter(){
   $.ajax({
       url: parent.ctxpath+'/oneViewAction.do?templateParam2=resetGlobalFilter',
       success:function(data){
            gotForOneview('Go');
       }
    });
}
function showLogicalColor(){
   parent.$("#logicalColorDiv").toggle(500);
}
function onChangeOperator(id,val,index){
    if(val =="<>"){
        parent.$("#range"+index+"_1").removeAttr("readonly");
        parent.$("#range"+index+"_2").show();
    }else{
        parent.$("#range"+index+"_2").hide();
    }
}
function calcTarVal(){
    var currVal=$("#measreIdVal").val();
    var tarDayVal=$("#TargetDayValue").val();
    var targetTotalVal=parseFloat(tarDayVal)*parseFloat(parent.numdays);
    var actualVal=parseFloat(currVal.replace(",","","gi"));
    //alert(actualVal);
   // alert(targetTotalVal);
    var deviation = ((actualVal-targetTotalVal)/targetTotalVal)*100;
    //alert(deviation);
    deviation=(deviation*100);
    //alert(deviation);
    deviation=Math.round(deviation);
    //alert(deviation);
    deviation=(deviation/100);
    //alert(deviation);
    $('input[id="range1"]').val(deviation);
}
function reflectValue(id,nextIndex){
    var val=parent.$("#"+id).val();
    var nextlemId=parent.$("#range"+(nextIndex)+"_1");
    if(nextlemId != 'undefined' && nextlemId !=null){
        parent.$('input[id="range'+(nextIndex)+'_1"]').val(val);
//        nextlemId.attr("readonly", "readonly");
    }
}
function getColorForMeasureVal(){
    var operator='';
    var color='';
    var range1;
    var range2;
    var range1Val;
    var currVal=parseFloat(parent.currVlaue.replace(",","","gi"));
    for(var i=0;i<3;i++){
        operator=$("#LogicalOperator"+i).val();
//        alert($("#logicalColorId_"+i).attr("colorcode"));
//        alert(operator);
        range1=$("#range"+i+"_1").val();
        var colorStatus=parent.$("#colorSelectId").val();
        if(colorStatus=='Absolute'){
        range1Val=range1;//alert("a"+range1Val);
        }
        else{
        range1Val=parseFloat(range1)*parseFloat(parent.numdays);
        }
        if(operator == '>'){
            if(currVal> parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '<'){
            if(currVal < parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }

        }
        if(operator == '='){
            if(currVal == parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '>='){
            if(currVal >= parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '<='){
            if( currVal <= parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '!='){
            if(currVal != parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '<>'){
            var range2Val="";
            var range2=$("#range"+i+"_2").val();
      if(colorStatus=='Absolute'){
        range2Val=range2;//alert("a"+range1Val);
        }
        else{
        range2Val=parseFloat(parseFloat(range2)*parseFloat(parent.numdays));
        }
          //  var range2Val=parseFloat(parseFloat(range2)*parseFloat(parent.numdays));
            if(currVal > range1Val && currVal < range2Val){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
    }
    return color;
}
function evalRangeColor(colorMap){

        $("#logicalChecked").attr("checked","checked");
        $("#logicalChecked").val("TRUE");
        $("#logicalColorDiv").show();
        for(var i=0 ;i<3;i++){
            var key="color"+i;
            //alert(colorMap[key]);

            $("#LogicalOperator"+i).val(colorMap[key][0]);
            if(colorMap[key][0] == "<>"){
                $("#range"+i+"_2").show();
            }
            $("#range"+i+"_1").val(colorMap[key][1]);
            $("#range"+i+"_2").val(colorMap[key][2]);
            $("#logicalColorId_"+i).attr("colorcode",colorMap[key][3]);
            $("#logicalColorId_"+i).css("background",colorMap[key][3]);
        }
        $("#logicalColorDiv").show();
}
function moveAllFromsortable(){
//    alert("moveAllFromsortable");
//    alert(parent.grpColArray +' Type of::'+typeof(parent.grpColArray));
    parent.$("span.myDragTabs").each(function(){
        var elemId=$(this).attr('id');
        var elemName=$(this).html();
        if($.inArray(elemId, parent.grpColArray)== -1){
            parent.createColumn(elemId,elemName,"sortable");
        }
    });
}
function DefineDialChart(){
    var dialStatus=parent.$("#dialSelectId").val();
//    var isChecked=parent.$("#DefineDialChartId").is(":checked");
    if(dialStatus =='yes'){
      parent.$("#DefineDialChartDiv").val("");  //// && isChecked
//        alert('parent.currVlaue'+parent.currVlaue+'  parent.numdays'+parent.numdays)
        var currValCal=parseFloat(parent.currVlaue.replace(",","","gi"));
//        alert('parent.currVlaue'+currValCal+'  parent.numdays'+parent.numdays)
        var perDay=currValCal/parseInt(parent.numdays);
        perDay=(perDay*100);
    perDay=Math.round(perDay);
    perDay=(perDay/100);
//        $("#DialChartChangePerVal").val(parent.changValue);
        parent.$(".dialMeasureName").html(parent.measrureName);
        parent.$("#dialMeasureValue").val(parent.currVlaue);
        parent.$("#dialMeasureTotalDays").html(parent.numdays);
        parent.$("#DialMeasureValuePerDay").val(perDay);
        parent.$("#DefineDialChartDiv").show();
        parent.$("#dialMeasureTotalDays").hide();
        parent.$("#dialMeasureTotalDaysTd").hide();
        parent.$("#dialMeasureName").hide();
        parent.$("#DialMeasureValuePerDay").hide();
        parent.$("#dialMeasureTotalDaysTD").hide();
        parent.$("#DialMeasureValuePerDayTd").hide();
    }else{
//        alert('Please enable Dial-Chart option to define ranges.');
//        parent.$("#DefineDialChartId").attr('checked',false)
        parent.$("#DefineDialChartDiv").hide();
    }
}
function showNumOfDays(){
    if(parent.document.getElementById("showNumOfDays").value=='absolute'){
        parent.$("#dialMeasureTotalDays").hide();
        parent.$("#dialMeasureTotalDaysTd").hide();
        parent.$("#dialMeasureName").hide();
        parent.$("#DialMeasureValuePerDay").hide();
        parent.$("#dialMeasureTotalDaysTD").hide();
        parent.$("#DialMeasureValuePerDayTd").hide();
    }else{
     parent.$("#dialMeasureTotalDays").show();
     parent.$("#dialMeasureTotalDaysTd").show();
     parent.$("#dialMeasureName").show();
     parent.$("#DialMeasureValuePerDay").show();
     parent.$("#dialMeasureTotalDaysTD").show();
     parent.$("#DialMeasureValuePerDayTd").show();
    }
}
function buildDialChart(regionId,changePer){
    var keyArray=['High','Medium','Low'];
    var dialMap=new Object();
    var validate=true;
    var dialMeasureBase=parent.$("#dialtypeMusureType").val();
     var actualVal=parseFloat(parent.currVlaue.replace(",","","gi"));
    var measureType=parent.$("#measureTypeId").val();
    for(var i=0;i<3;i++){
        var condition=$("#DialChartOperator"+keyArray[i]).val();
        var range1=$("#range"+keyArray[i]+"_1").val();
        var range2=$("#range"+keyArray[i]+"_2").val();
        if(range2 == 'undefined' || range2==''){
            range2=0;
        }

        if(range1 == 'undefined' || range1==''){
            validate=false;
        }
        var mapDetails=condition+'~'+range1+'~'+range2;
        dialMap[keyArray[i]]=mapDetails;
    }
    dialMap=encodeURIComponent(JSON.stringify(dialMap));
    var targetVal=changePer;
    var day=false;
    if( dialMeasureBase == "deviationPer"){
//        targetVal=$("#dailDeviationValue").val();
 if(parent.document.getElementById("showNumOfDays").value=='absolute'){
        targetVal=$("#DialTargetValue").val();
        day=true;
        }
        else{
        actualVal=$("#DialMeasureValuePerDay").val();
        targetVal=$("#DialTargetValue").val();
    }
    }else if( dialMeasureBase == "absolute"){
        if(parent.document.getElementById("showNumOfDays").value=='absolute'){
        targetVal=$("#dialMeasureValue").val();
        day=true;
        }
        else{
        actualVal=$("#DialMeasureValuePerDay").val();
        targetVal=$("#DialMeasureValuePerDay").val();
    }
    }

    if(validate){
        $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=buildDialChart&oneViewId='+parent.oneviewID+'&regionId='+regionId+'&dialMap='+dialMap+'&targetVal='+targetVal+'&dialMeasureBase='+dialMeasureBase+'&measureType='+measureType+'&currVal='+actualVal+'&day1='+day,
        function(data){
            $("#Dashlets-"+regionId).html(data);

        });
    }else{
        alert('Please define Range for Dial-Chart');
    }
}
function evalDialChartRanges(dialMap){
        parent.isDialEnabled=true;
        $("#dialSelectId").val('yes');
        $("#DefineDialChartId").attr("checked","checked");
        $("#DefineDialChartId").val("TRUE");
        $("#DefineDialChartDiv").show();
        var keys=['High','Medium','Low']
        var dialMeasureBase=dialMap.MeasureOptions[11];
        var targetVal=dialMap.MeasureOptions[12];
        var totDays=dialMap.MeasureOptions[8];
        var actualVal=parseFloat(parent.currVlaue.replace(",","","gi"));
        var perDay=(actualVal/parseInt(totDays));


        $(".dialMeasureName").html(parent.measrureName);
        $("#dialMeasureTotalDays").html(totDays);
        $("#DialMeasureValuePerDay").val(perDay);
        $("#dialtypeMusureType").val(dialMeasureBase);
        $("#dialMeasureValue").val(parent.currVlaue);

        if(dialMeasureBase == 'deviationPer'){
            var targetTotalVal=parseFloat(targetVal)*parseFloat(totDays);
//            alert(actualVal);
//            alert(targetTotalVal);
            var deviation = calDeviation(actualVal,targetTotalVal)
            $("#dailDeviationValue").val(deviation);
            $("#DialChartChangePerVal").val(targetVal)
            $("#DialChartChangePerVal").show();
            $("#dialDeveationTd").show();
        }else if(dialMeasureBase =='changePer'){
            $("#DialChartChangePerVal").val(parent.changValue);
            $("#DialChartChangePerVal").show();
        }
        for(var i=0 ;i<3;i++){

            //alert(colorMap[key]);
             $("#DialChartOperator"+keys[i]).val(dialMap[keys[i]][0]);
            if(dialMap[keys[i]][0] == "<>"){
                $("#range"+keys[i]+"_2").show();
            }
            $("#range"+keys[i]+"_1").val(dialMap[keys[i]][1]);
            $("#range"+keys[i]+"_2").val(dialMap[keys[i]][2]);

        }
        if(dialMeasureBase == 'deviationPer'){
            $("#DialTargetValue").val(targetVal)
            $("#deviationDiv").show();
        }else if(dialMeasureBase =='changePer'){
            $("#DialChartChangePerVal").val(parent.changValue);
            $("#changePerDiv").show();
        }
        $("#DefineDialChartDiv").show();
}

     function oneviewAlerts(regionId,oneViewId,currVal,changePer,measId, measName,priorVal,roleId){

        //alert(regionId+"  "+oneViewId+" "+currVal+" "+changePer+" "+measId+" "+measName+" "+priorVal+" "+roleId)
            parent.$("#oneviewMeasuresAlertId").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                    parent.$("#oneviewMeasuresAlertId").dialog('option', 'title', measName+' Alert');
                    parent.$("#oneviewMeasuresAlertId").dialog('open');
                    var alertsHtml = ''
                    alertsHtml+="<table align='center' border='0' width='100%'><tr><td><input type='checkbox' id='dayId'  name='' align='left'>&nbsp;&nbsp;&nbsp;Daily</td></tr>";
                    alertsHtml+="<tr><td><input type='checkbox' id='wtdId'  name='' align='left'>&nbsp;&nbsp;&nbsp;WTD</td></tr>";
                    alertsHtml+="<tr><td><input type='checkbox' id='mtdId'  name='' checked align='left'>&nbsp;&nbsp;&nbsp;MTD</td><tr>"
                    alertsHtml+="<tr><td><input type='checkbox' id='qtdId'  name='' align='left'>&nbsp;&nbsp;&nbsp;QTD</td></tr>";
                    alertsHtml+="<tr><td><input type='checkbox' id='ytdId'  name='' align='left'>&nbsp;&nbsp;&nbsp;YTD</td></tr>";
                    alertsHtml+="</table><br>";

                    alertsHtml+="<table align='center'><tr><td><input type='button' id='' name='' value='Next' onclick=\"oneviewMeasureAlertBox('"+regionId+"','"+oneViewId+"','"+currVal+"','"+changePer+"','"+measId+"','"+measName+"','"+priorVal+"','"+roleId+"')\"></td>"
                    alertsHtml+="</tr></table>"
                    parent.$("#oneviewMeasuresAlertId").html(alertsHtml);
            }
            function oneviewMeasureAlertBox(regionId,oneViewId,currVal,changePer,measId, measName,priorVal,roleId){
                 //alert(regionId+"  "+oneViewId+" "+currVal+" "+changePer+" "+measId+" "+measName+" "+priorVal+" "+roleId)
                     var innerHtml = ""
                     var devPercent = ""
                     var durarinType = $("#timedetailselectId").val();
                      var curvalAvg = parseFloat(currVal.replace(/\,/g,''));
                      if(durarinType=='Month'){
                          devPercent= (curvalAvg/30);
                      }
                      if(durarinType=='Week'){
                          devPercent= (curvalAvg/7);
                      }
                      if(durarinType=='Quarter'){
                          devPercent= (curvalAvg/90);
                      }
                      if(durarinType=='Year'){
                          devPercent= (curvalAvg/365);
                      }else{
                           devPercent= curvalAvg;
                      }
                     var result=Math.round(devPercent*100)/100

                      innerHtml+="<td><font style='font-size:11px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#000000;'>Avg Pro-related Daily :"+result.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")+"</font></td>"
                      innerHtml+="<td style='display: none;'><input type='text' id='measureValueId' style='text-align:right;width: 50%;'  value='"+result+"'></td>"
                      parent.$("#innerValuesId").html(innerHtml);

                 parent.$("#scheduleReportalertId").dialog({
                        autoOpen: false,
                        height: 540,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });

                    parent.$("#sDatepicker").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });

                    parent.$("#eDatepicker").datepicker({
                       changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
                    var daily = '';
                    var monthly = '';
                    var quarterly = '';
                    var weekly = '';
                    var yearly = '';
                    if($("#mtdId").is(':checked')){
                        monthly = "Month";
                    }
                    if($("#qtdId").is(':checked')){
                         quarterly = "Qtr";
                    }
                     if($("#wtdId").is(':checked')){
                         weekly = "Week";
                    }
                     if($("#dayId").is(':checked')){
                        daily = "Day";
                    }
                     if($("#ytdId").is(':checked')){
                         yearly = "Year";
                    }
                    var innerhtml = '';
                    innerhtml+="<tr><td><input type='text' id='innerdivTableId' name='innerdivTable' value='"+regionId+","+oneViewId+","+currVal+","+priorVal+","+changePer+","+measName+","+measId+","+roleId+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Day' value='"+daily+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Month' value='"+monthly+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Week' value='"+weekly+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Qtr' value='"+quarterly+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Year' value='"+yearly+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='eleId' value='"+measId+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='roleId' value='"+roleId+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='measName' value='"+measName+"'></td></tr>";

                    parent.$("#sDatepicker").val('');
                    parent.$("#eDatepicker").val('');
                    parent.$("#scheduleName").val('');
                    parent.$("#usertextarea").val('');
                    parent.$('#hrs option[value="0"]').attr('selected','selected');
                    parent.$('#mins option[value="0"]').attr('selected','selected');
                    parent.$('#alertDateTypeId option[value="oneviewdate"]').attr('selected','selected');
                    parent.$('#frequency option[value="Daily"]').attr('selected','selected');
                    parent.$('#absolute').attr('checked','checked');

                         parent.$("#weekday").hide();
		         parent.$("#hourlyId").hide();
	        	 parent.$("#monthday").hide();
	        	 parent.$("#customSysdateId").hide();
	        	 parent.$("#customGlobaldateId").hide();
                         parent.$("#targetConditions").hide();
                         parent.$("#trackerCondition").show();

                         var arreytest = new Array();
                         arreytest.push("<");
                         arreytest.push(">");
                         arreytest.push("<=");
                         arreytest.push(">=");
                         arreytest.push("=");
                         arreytest.push("<>");
                          parent.tableCondition = "absolute"
                      var condIdx = "0";
                      var rowID="cond"+condIdx
                      var condHtml="";
                      var temTrgtType="";

                      temTrgtType="When Value";
                      var condHtml="";
                      condHtml+="<tr id='"+rowID+"'><td align='left'><span id='condTD"+condIdx+"'>"+temTrgtType+"</span></td>";
                      condHtml+="<td>";
                      condHtml+="<select name=\"condOp\" id=\""+condIdx+"condOp\" onchange=\"addTextBox(this,'"+condIdx+"')\">";
                       for(var i=0; i<arreytest.length; i++){
                        condHtml+="<option value='"+arreytest[i]+"'>"+arreytest[i]+"</option>"
                       }
                        condHtml+="</select></td>";

                      condHtml+="<td><input type='text' style='width: 80px;' id=\""+condIdx+"sCondVal\" name='sCondVal' class='myTextbox3'></td>\n\
                       <td><input type='text' style='width: 80px; display: none;' id=\""+condIdx+"eCondVal\" name='eCondVal' class='myTextbox3'></td>\n\
                       <td>Send Mail to </td><td><input type='text' id=\""+condIdx+"condMail\" class='myTextbox3' name='condMail' style='width: auto'> </td>\n\
                       <td>Tag</td><td><select id=\""+condIdx+"tagValueId\" name='tagValues'><option value='normal'>Normal</optiong><option value='important'>Important</optiong><option value='critical'>Crictical</optiong></select></td>\n\
                       <td><img border='0' align='middle' title='Add Row' alt='' src='"+parent.ctxpath+"/icons pinvoke/plus-circle.png' onclick='addCondRow()'></td>\n\
                       <td><img border='0' align='middle' title='Delete Row' alt='' src='"+parent.ctxpath+"/icons pinvoke/cross-circle.png' onclick=\"deleteCondRow('"+rowID+"')\"></td></tr>";

                      parent.$('#condTable').html('');
                      parent.$('#condTable').html(condHtml);

                 parent.$("#innerRegionId").html(innerhtml);
                if(daily!='' || monthly!='' || weekly!='' || quarterly!='' || yearly!=''){
                    parent.$("#oneviewMeasuresAlertId").dialog('close');
                    parent.$("#scheduleReportalertId").dialog('option', 'title', measName+' Alert ');
                    parent.$("#scheduleReportalertId").dialog('open');
                }
                else{
                    alert("Please Select atleast one type!")
                }

           }
          function sendOneviewMeasure(){

               var oneveiwDate = $("#dateId").val();
               var checkforCustDate = '';
               var absolutOrPercent = '';
                if(parent.$("#fromSysDate").is(':checked')){
                        checkforCustDate = "sysDate";
                 }
                 else{
                        checkforCustDate = "globalDate";
                 }
               var subject = "";
               var sDatepicker = "";
               var eDatepicker = "";
               var usertextarea = "";
               var hrs = "0";
               subject=parent.$("#scheduleName").val();
               sDatepicker = parent.$("#sDatepicker").val();
               eDatepicker = parent.$("#eDatepicker").val();
//               usertextarea = parent.$("#usertextarea").val();
//               alert(usertextarea)
               hrs = parent.$("#hrs").val();
               var measureValueCurrVal = parent.$("#measureValueId").val();
               var validate = true;

               if(parent.$("#absolute").is(':checked')){
                   absolutOrPercent = "absoulteBasis";
               }else{
                   absolutOrPercent = "percentBasis";
               }

               if(subject==""){
                   alert("Please Select Alert Name!")
                   validate = false;
               }
              else if(sDatepicker==""){
                   alert("Please Select Start Date!")
                   validate = false;
               }
              else if(eDatepicker==""){
                   alert("Please Select End Date!")
                   validate = false;
               }
//              else if(usertextarea==""){
//                   alert("Please Enter EmailId!")
//                   validate = false;
//               }
              else if(hrs=="0"){
                   alert("Please Select Time!")
                   validate = false;
               }

              if(validate){
                parent.$("#scheduleReportalertId").dialog('close');
                   $.post(
                         parent.ctxpath+'/oneViewAction.do?templateParam2=oneviewMeasureAlerts&oneveiwDate='+oneveiwDate+"&checkforCustDate="+checkforCustDate+"&absolutOrPercent="+absolutOrPercent+"&measureValueCurrVal="+measureValueCurrVal,$("#dailyReportScheduleForm").serialize(),
                         function(data){
                      }
                   );
                 }
          }
function zoomMeasureDialChart(changePer,regionId,divTitle,currVal){
    var dialogHeight=500;
    var dialogWidth=600;
     if(parent.oneviewtypedate1=='true'){

     }else{
     $("#zoomMeasureDialDiv").css("height",dialogHeight);
     $("#zoomMeasureDialDiv").css("width",dialogWidth);
    $("#zoomMeasureDialDiv").dialog({
        autoOpen: false,
        height: dialogHeight,
        width: dialogWidth,
        position: 'justify',
        modal: true,
        resizable:true,
        title:divTitle
    });
     $("#zoomMeasureDialDiv").dialog('open');
    $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=zoomDialChart&oneViewId='+parent.oneviewID+'&regionId='+regionId+'&height='+(dialogHeight-70)+'&width='+(dialogWidth-50)+'&changePer='+changePer+'&isZoom=true&currVal='+currVal,
        function(data){
            $("#zoomMeasureDialDiv").html(data);
        });
}
}
function checkDialStatus(id){
    if($("#"+id).val()=='no'){
//        $("#DefineDialChartId").attr('checked',false);
        $("#DefineDialChartDiv").hide();
    }else{
//        $("#DefineDialChartId").attr('checked',true);
        DefineDialChart()
    }
}
function buildExixtentMeasure(regionId){

    $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=buildExixtentMeasure&oneViewId='+parent.oneviewID+'&regionId='+regionId+'&isDialChart=false',
        function(data){
            parent.$("#"+regionId).html(data);
             parent.$("#loadImg").remove();
        });
}
function changeDialMeasuretype(id){
    var selType=$("#"+id).val();
    if(selType == "absolute"){
//        $("#dialTargetTd").show();
        $("#deviationDiv").hide();
         $("#changePerDiv").hide();
        $("#dialDeveationTd").hide();
    }else if(selType == "deviationPer"){
        $("#deviationDiv").show();
         $("#changePerDiv").hide();
    }else{
        $("#DialChartChangePerVal").val(parent.changValue);
        $("#deviationDiv").hide();
         $("#changePerDiv").show();
         $("#dialDeveationTd").hide();
    }
}
function getDialDeviation(){
    var tarDayVal=$("#DialTargetValue").val();
    var actualVal="";
    var targetTotalVal="";
    if(parent.document.getElementById("showNumOfDays").value=='absolute'){
        actualVal=$("#dialMeasureValue").val();
        targetTotalVal=parseFloat(tarDayVal);
        }
        else{
        actualVal=parent.$("#DialMeasureValuePerDay").val();
        targetTotalVal=parseFloat(tarDayVal);
        }
   // var targetTotalVal=parseFloat(tarDayVal)*parseFloat(parent.numdays);//dialMeasureTotalDays
    actualVal=parseFloat(actualVal.replace(",","","gi"));
    //alert(actualVal);
    //alert(targetTotalVal);
    var deviation = calDeviation(actualVal,targetTotalVal);
    $("#dailDeviationValue").val(deviation);
    $("#dialDeveationTd").show();
}
function calDeviation(actualVal,targetval){
    var deviation = ((actualVal-targetval)/targetval)*100;
    //alert(deviation);
    deviation=(deviation*100);
    deviation=Math.round(deviation);
    deviation=(deviation/100);
    return deviation;
}
function getDisplayTables(ctxpath,oneviewID,paramslist){
//        getdimmap.getFact(Parameters);
        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
            var   busroleId = $("#busrolemeasId").val();
            $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+oneviewID,
                function(data){
                var frameObj=document.getElementById("measureFrameId");
                var source=ctxpath+"/reportViewer.do?reportBy=getMeasuresForOneView&busroleID="+busroleId;
                frameObj.src=source;
                });
            //$("#tableList").attr('checked',true);
        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+busroleId,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var frameObj=document.getElementById("measureFrameId");
                var source=ctxpath+"/reportViewer.do?reportBy=getMeasuresForOneView&busroleID="+busroleId;
                frameObj.src=source;
                });

        }
    }
    function showList(ctxpath,paramslist){
        var   busroleId = $("#busrolemeasId").val();
        if(document.getElementById("paramVals").style.display=='none'){
            $("#paramVals").show();
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+busroleId,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

    }
    function selectTables(tdId,tname){
        //alert(tname)
        document.getElementById(tdId).style.display='none';
        if($("#tabsListVals").val() == ""){
            $("#tabsListVals").val(tname)
            $("#tabsListIds").val(tdId)
        }else{
            var Ids = $("#tabsListIds").val()+","+tdId
            var value = $("#tabsListVals").val()+","+tname
            $("#tabsListIds").val(Ids)
            $("#tabsListVals").val(value)
        }
    }
    function setValueToContainer(ctxpath,repId,bizRoles){
        $("#paramVals").hide();
        var tabLst = $("#tabsListIds").val();
        $("#tabsListVals").val('')
            $("#tabsListIds").val('')
        $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    var frameObj=document.getElementById("measureFrameId");
                var source=ctxpath+"/reportViewer.do?reportBy=getMeasuresForOneView&busroleID="+busroleId;
                frameObj.src=source;
                });
    }
    function coditionalyes(){
        if(document.getElementById("trackerConditions").style.display==''){
            document.getElementById("trackerConditions").style.display='none'
            document.getElementById("nonCondtionaldMailId").style.display=''
        }else{
           document.getElementById("trackerConditions").style.display=''
            document.getElementById("nonCondtionaldMailId").style.display='none'
        }
    }
    function coditionalno(){

    }

    function refreshOneVIewReg(regId){

       $("#Dashlets-"+regId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
       $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=refreshOneVIewReg&oneViewIdValue='+parent.oneviewID+'&regId='+regId,
                function(data){
                    var checkdata=data;
                    if(checkdata == 'Region data not saved Problem occured...!!'){
                        }else{
                        parent.$("#"+regId).html(data);
                                  if(parent.oneviewtypedate1=='true'){
                         parent.$("#measureNavigateId"+regId).hide();
                        parent.$("#relatedMeasureInfoId"+regId).hide();
                         }
                         else{
                              hideicons(regId)
                         }
                    }
                });
   }
    function refreshOneVIewRegd3(regId,chartname,idArr,repId,repname,drillviewby){
 var  Dashlets="Dashlets-"+regId;
       $("#Dashlets-"+regId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
       $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=refreshOneVIewReg&oneViewIdValue='+parent.oneviewID+'&regId='+regId+'&idArr='+idArr+'&drillviewby='+drillviewby,
                function(data){
                    var checkdata=data;
                    if(checkdata == 'Region data not saved Problem occured...!!'){
                        }else{
                            var jsonVar=eval('('+data+')');
                                  var  result=jsonVar.result;
                 var  repid=jsonVar.repid
                        parent.$("#"+regId).html(result);
                        parent.$("#graphsId").val(repid);
                        parent.$("#graphName").val(repname);
                       $("#Dashlets-"+regId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute;display:none;" /></center>');
  var wid ;
                        var hgt1 ;
                        hgt1 = document.getElementById(Dashlets).offsetHeight;
                        wid = document.getElementById(Dashlets).offsetWidth;
//                         $("#Dashlets"+regId).html(chartname);
                        if(idArr=='null'){
                         generateJsonDataOneview("Dashlets-"+regId,parent.oneviewID,regId,chartname,"refresh");
//                         $("#Dashlets-"+regId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute;display:none;" /></center>');
                        }else{
                         drillWithinchart11(idArr,"Dashlets-"+regId,wid,hgt1,regId,parent.oneviewID,repname,repId,chartname,"save")
                        }
//                         generateJsonDataOneview("Dashlets-"+regId,parent.oneviewID,regId,chartname,"null");
                                  if(parent.oneviewtypedate1=='true'){
                         parent.$("#measureNavigateId"+regId).hide();
                        parent.$("#relatedMeasureInfoId"+regId).hide();
                         }
                    }
                });
   }
   function oneViewIcons(){
       var size;
       var icons="";
            if($("#saveTabId0").is(':visible')){
                    icons="hide";
                }else{
                    icons="visible";
                }
       $.ajax({
         url:  parent.ctxpath+'/oneViewAction.do?templateParam2=NoOfRegions&oneViewIdValue='+parent.oneviewID+'&icons='+icons,
         success:function(data){
         var  datalist = new Array();
         datalist = data.toString().split(",");
         size=datalist[0].replace("[","").replace("]","");
     for(var j=0;j<size;j++){
       $("#refreshTabId"+j).toggle();
       $("#saveTabId"+j).toggle();
       $("#optionId"+j).toggle();
       $("#optionIds"+j).toggle();
        $("#alertTabId"+j).toggle();
         }
                              }
            });

      }
function customTimeAggregation(oneviewletId,oneviewId){
    colNumber=oneviewletId;
    oneViewIdValue=oneviewId;
    getCustomTimeMsrDetails();
              $("#customTimeMsr").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                $("#customTimeMsr").dialog('open');
}
function customTimeSelection(){
    if($("#customTimeOption").is(":checked")){
        $("#customTimeTable").show();
    }else{
        $("#customTimeTable").hide();
    }
}
function enableCustomTimeMeasure(){
    var isCustomTimeApplied=false;
    var customMsrDate='';
    var customMsrDuration='';
    var customMsrCompre='';
    var customMsrAgr='';
    if(document.getElementById("customTimeOption") != null && document.getElementById("customTimeOption").checked){
         isCustomTimeApplied=true;
         customMsrDate=$("#customMsrdate").val();
         customMsrDuration=$("#customMsrDuration").val();
         customMsrCompre=$("#customMscompare").val();
          }
//      customMsrAgr=$("#msrAggr").val();
     $("#customTimeMsr").dialog('close');
    var loadImg='<img id="loadImg" width="15px" height="15px" src=\"<%=request.getContextPath()%>/images/ajax-loader.gif\" align="right"  width="5px" height="5px"  style="position:absolute;float:right;margin-left:45px;" >';
    $("#Dashlets-"+colNumber).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
    $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=enableCustomTimeMeasure&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber+'&isCustomTimeApplied='+isCustomTimeApplied
                  +'&customMsrDate='+customMsrDate+'&customMsrDuration='+customMsrDuration+'&customMsrCompre='+customMsrCompre+'&customMsrAgr='+customMsrAgr,
                function(data){
                    parent.$("#"+colNumber).html(data);
                });
}
function getCustomTimeMsrDetails(){
    $.ajax({
        url:parent.ctxpath+'/oneViewAction.do?templateParam2=getCustomTimeMsrDetails&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber,
                        success: function(data){
                             var jsonValue=eval('('+data+')');
//                             alert("**customMsrAgr***"+jsonValue.customMsrAgr)
                             var customMsrAgr=jsonValue.customMsrAgr;
                            if(jsonValue.customTimeDetails[0] != 'null'){
                            var customTimeenable=jsonValue.customTimeDetails[0];
                            if($.trim(customTimeenable)== 'false'){
//                                $("#customTimeOption").val('FALSE');
                                $("#customTimeTable").hide();
                                $("#customTimeOption").attr('checked',false);
                            }else{
                                $("#customTimeOption").attr('checked',true);
//                                $("#customTimeOption").val("TRUE");
                                $("#customTimeTable").show();
                                $("#customMsrdate").val(jsonValue.customTimeDetails[1]);
                                $('#customMsrDuration option[value='+jsonValue.customTimeDetails[2]+']').attr('selected','selected');
                                $('#customMscompare option[value='+jsonValue.customTimeDetails[3]+']').attr('selected','selected');
                            }
                          }
//                          if(customMsrAgr!='null'){
//                              $('#msrAggr option[value='+jsonValue.customMsrAgr+']').attr('selected','selected');
//                          }

                        }
                });
}
//function AddMoreDimension(ctxPath,oneviewId) {
////    alert("dimenssions")
////    alert("AddMoreDimension***"+oneViewIdValue)
////    alert("oneviewId**"+oneviewId)
//    parent.$("#AddMoreParamsDiv").dialog({
//       autoOpen: false,
//       height: 420,
//       width: 500,
//       position: 'justify',
//       modal: true,
//       resizable:true
//    });
//    var frameObj=parent.document.getElementById("addmoreParamFrame");
//    var source=ctxPath+"/reportTemplateAction.do?templateParam=addMoreDimensions&foldersIds="+oneViewBusRole+"&REPORTID="+oneviewId+'&isbusTemaplateView=true';
//    frameObj.src=source;
////    alert(source)
//   parent.$("#AddMoreParamsDiv").dialog('open');
//    }
    function cancelDim(){
             parent.$("#AddMoreParamsDiv").dialog('close');
             parent.$("#measuresDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
            }
function cancelTabMeasure(){
    parent.$("#measuresDialog").dialog('close');
}
 function getDisplayTablesInDesigner(ctxpath,paramslist,repId){
       var repId=oneViewIdValue;
        var frameObj=document.getElementById("dataDispmem");
//        getdimmap.getFact(Parameters);
        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
         var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+oneViewBusRole+'&REPORTID='+oneViewIdValue+'&tableList=true';
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
            //$("#tableList").attr('checked',true);
        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+oneViewBusRole,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+oneViewBusRole+'&REPORTID='+oneViewIdValue;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
                });

        }
    }
    function showListInDesigner(ctxpath,paramslist){
        if(document.getElementById("paramVals").style.display=='none'){
            $("#paramVals").show();
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+oneViewBusRole,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

    }
    function selectTablesInDesigner(tdId,tname){
        //alert(tname)
        document.getElementById(tdId).style.display='none';
        if($("#tabsListVals").val() == ""){
            $("#tabsListVals").val(tname)
            $("#tabsListIds").val(tdId)
        }else{
            var Ids = $("#tabsListIds").val()+","+tdId
            var value = $("#tabsListVals").val()+","+tname
            $("#tabsListIds").val(Ids)
            $("#tabsListVals").val(value)
        }
    }
    function setValueToContainerInDesigner(ctxpath,repId,bizRoles){
       var frameObj=document.getElementById("dataDispmem");
       $("#paramVals").hide();
       var tabLst = $("#tabsListIds").val();
       $("#tabsListVals").val('')
       $("#tabsListIds").val('')
       $("#tabListDiv").hide();
           $("#tablistLink").hide();
           $("#goButton").hide();
           $("#tabsListIds").val("");
           $("#tabsListVals").val("");
               var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+oneViewBusRole+'&REPORTID='+oneViewIdValue+'&tableList=true';
       frameObj.src=source;
   }
   function PreviewTable(){
        var value= parent.document.getElementById("regionTableId");
        var action1=$("#action1").val();
        value.innerHTML='<center><img id="imgId" width="200px" height="200px" src="'+parent.ctxpath+'/images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        parent.$("#designId").css({'height':'400px'});
        $.ajax({
            url: 'oneViewAction.do?templateParam2=buildOneviewTemplateGraph'+'&oneViewIdValue='+oneViewIdValue+'&action=initialSave'+'&action1='+action1,
            success: function(data){
             parent.$("#designId").css({'padding-bottom':''})
             parent.$("#designId").css({'height':''})
            parent.$("#regionTableId").html(data);
        }});
   }
   function changeGraphColumn(msrId,oneviewId,rowviewby){
     $("#Dashlets-0").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
      $.ajax({
            url: 'oneViewAction.do?templateParam2=changeBusinessTemplateGraphColumn&REPORTID='+oneviewId+'&gid=0&graphChange=GrpColumns'+'&grpColumns='+msrId+'&grpIds=0'+'&viewletId=0',
            success: function(data){
            parent.$("#0").html(data);
        }});
   }
   function changeTemplateMsr(oneviewId){
       var msrId=$("#templateMsrId").val();
    var value= parent.document.getElementById("regionTableId");
        value.innerHTML='<center><img id="imgId" width="200px" height="200px" src="'+parent.ctxpath+'/images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        parent.$("#designId").css({'height':'400px'});
        $.ajax({
            url: 'oneViewAction.do?templateParam2=changeBusinessTemplateGraphColumn&REPORTID='+oneviewId+'&gid=0&graphChange=GrpColumns'+'&grpColumns='+msrId+'&grpIds=0'+'&action=GO',
            success: function(data){
            parent.$("#designId").css({'padding-bottom':''})
             parent.$("#designId").css({'height':''})
            parent.$("#regionTableId").html(data);
        }});

   }
   function changeRowViewby(oneviewId,parmId){
//       alert(oneviewId+",,,"+parmId)
       var value= parent.document.getElementById("regionTableId");
        value.innerHTML='<center><img id="imgId" width="200px" height="200px" src="'+parent.ctxpath+'/images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        parent.$("#designId").css({'height':'400px'});
        $.ajax({
            url: 'oneViewAction.do?templateParam2=changeBusinessTemplateRowViewby'+'&oneviewId='+oneviewId+'&rowviewby='+parmId+'&action=GO',
            success: function(data){
             parent.$("#designId").css({'padding-bottom':''})
             parent.$("#designId").css({'height':''})
            parent.$("#regionTableId").html(data);
        }});
   }

   function changeGraphTypes(Id){
//       alert(Id)
       var divId=document.getElementById("grphTypes"+Id);
        if(divId.style.display=='none'){
        divId.style.display='';
    }
    else{
        divId.style.display='none';
    }
   }
   function buildJqGraph(jqGraphTypeName,grpId,jqgraphId,oneviewId,viewbyId,viewletId){
       alert(viewletId)
        $("#Dashlets-0").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
      $.ajax({
            url: 'oneViewAction.do?templateParam2=changeBusinessTemplateGraphColumn&REPORTID='+oneviewId+'&gid=0&graphChange=GrpType'+'&jqgraphId='+jqgraphId+'&jqGraphTypeName='+jqGraphTypeName+'&viewbyId='+viewbyId+'&viewletId='+viewletId,
            success: function(data){
            parent.$("#"+viewletId).html(data);
        }});
   }
    function changeTemplateMeasures(ctxpath){

    //alert("oneviewID"+oneviewID)
     parent.$("#measuresDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
     $.ajax({
            url: 'oneViewAction.do?templateParam2=getOneviewBusinessRole&REPORTID='+oneviewID,
            success: function(data){
             var json=eval('('+data+')');
             var roleId=json.roleId;
             var oneviewId=json.oneviewId;
             var oneviewType=json.oneviewType;
             parent.oneViewType=oneviewType;
             parent.$("#oneviewType").val(oneviewType);
             parent.oneViewIdValue=oneviewId;
             oneViewBusRole=roleId;
              $("#action1").val("measChange");
             $("#measuresDialog").dialog('open');
             var frameObj=parent.document.getElementById("dataDispmem");
             var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleId+'&REPORTID='+oneviewId;
             frameObj.src=source;
        }});
}
function PreviewMsrTemplateTable(){
     var value= parent.document.getElementById("regionTableId");
        var action1=$("#action1").val();
        value.innerHTML='<center><img id="imgId" width="200px" height="200px" src="'+parent.ctxpath+'/images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        parent.$("#designId").css({'height':'400px'});
        $.ajax({
            url: 'oneViewAction.do?templateParam2=buildMesureBasedTemplateData'+'&oneViewIdValue='+oneViewIdValue+'&action=initialSave'+'&action1='+action1,
            success: function(data){
             parent.$("#designId").css({'padding-bottom':''})
             parent.$("#designId").css({'height':''})
            parent.$("#regionTableId").html(data);
        }});
}

//sandeep code for oneview date toggle
function oneviewdatetoggle(oneid){
 var value= parent.document.getElementById("regionTableId");
//parent.$("#designId").css({'height':'400px'})
        value.innerHTML='<center><img id="imgId" width="200px" height="200px" src="'+parent.ctxpath+'/images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                      $.ajax({
            url: 'oneViewAction.do?templateParam2=getingOneviews&oneViewIdValue='+oneid+'&action=datetoggle',
            success: function(data){
                    if(data!='NO DATA'){
                        document.getElementById("designId").style.display='';
                        $("#designId").css({'padding-bottom':''})
                        $("#designId").css({'height':''})

                        $.post('oneViewAction.do?templateParam2=getContainerFromSession&oneViewIdValue='+oneid,
                function(data){
//alert(data)
        var jsonVar=eval('('+data+')');
                        $("#assignDiv").html("")
         parent.datetype=jsonVar.datetype
            var  timedetails=jsonVar.timedetails
              var  timedetails1=jsonVar.timedetails1
              if(parent.oneviewtypedate1=='false'){
              var  timedetailscf=jsonVar.timedetailscf
              var  timedetails1ct=jsonVar.timedetails1ct
                }
//              alert(timedetails)
//              alert(timedetails1)
 datetype=datetype[0].replace("[","").replace("]","");
 if(datetype=='PRG_STD'){
      parent.$("#fdatetdspan").hide();
      parent.$("#fdatetdcal").hide();
      parent.$("#totd").hide();
      parent.$("#tdatetdspan").hide();
      parent.$("#tdatetdcal").hide();
      parent.$("#timedetailsId1").show();
                parent.$("#compareWithId1").show();
                parent.$("#sdatetdspan").show();
                parent.$("#sdatetdcal").show();
 if(parent.oneviewtypedate1=='false'){
          $("#compare").hide();
 parent.$("#cfdatetdspan").hide();
      parent.$("#cfdatetdcal").hide();
      parent.$("#totd1").hide();
      parent.$("#ctdatetdspan").hide();
      parent.$("#ctdatetdcal").hide();
      }
for(var i=0;i<timedetails.length;i++){
    var day=timedetails[0].replace("[","").replace("]","");
var date=timedetails[1].replace("[","").replace("]","");
var year=timedetails[2].replace("[","").replace("]","");
 parent.$("#stfield1").html(year)
                    parent.$("#stfield2").html(date)
                    parent.$("#stfield3").html(day)
}
parent.$("#dateId").val("")
parent.$("#todate").val("")
parent.$("#defvaluedate").val(timedetails);
 }else if(datetype=='PRG_DATE_RANGE'){
      parent.$("#sdatetdspan").hide();
      parent.$("#sdatetdcal").hide();
       parent.$("#timedetailsId1").hide();
                parent.$("#compareWithId1").hide();
      parent.$("#fdatetdspan").show();
      parent.$("#fdatetdcal").show();
      parent.$("#totd").show();
      parent.$("#tdatetdspan").show();
      parent.$("#tdatetdcal").show();
       if(parent.oneviewtypedate1=='false'){
          $("#compare").show();
 parent.$("#cfdatetdspan").show();
      parent.$("#cfdatetdcal").show();
      parent.$("#totd1").show();
      parent.$("#ctdatetdspan").show();
      parent.$("#ctdatetdcal").show();
      }

for(var j=0;j<timedetails.length;j++){
    var day0=timedetails[0].replace("[","").replace("]","");
var date0=timedetails[1].replace("[","").replace("]","");
var year0=timedetails[2].replace("[","").replace("]","");
 parent.$("#pfield1").html(year0)
                    parent.$("#pfield2").html(date0)
                    parent.$("#pfield3").html(day0)
    var day1=timedetails1[0].replace("[","").replace("]","");
var date1=timedetails1[1].replace("[","").replace("]","");
var year1=timedetails1[2].replace("[","").replace("]","");
 parent.$("#tdfield1").html(year1)
                    parent.$("#tdfield2").html(date1)
                    parent.$("#tdfield3").html(day1)
                     if(parent.oneviewtypedate1=='false'){
    var daycf=timedetailscf[0].replace("[","").replace("]","");
var datecf=timedetailscf[1].replace("[","").replace("]","");
var yearcf=timedetailscf[2].replace("[","").replace("]","");
 parent.$("#cffield1").html(yearcf)
                     parent.$("#cffield2").html(datecf)
                     parent.$("#cffield3").html(daycf)
                      parent.$("#defvaluedate").val(timedetails);
    var dayct=timedetails1ct[0].replace("[","").replace("]","");
var datect=timedetails1ct[1].replace("[","").replace("]","");
var yearct=timedetails1ct[2].replace("[","").replace("]","");
  parent.$("#ctfield1").html(yearct)
                     parent.$("#ctfield2").html(datect)
                     parent.$("#ctfield3").html(dayct)
                      parent.$("#defvaluedate").val(timedetails);
                       }

}
parent.$("#stdate").val("")
parent.$("#defvaluedate").val(timedetails);
parent.defultdate=timedetails1;
 }
                        });



//                        document.getElementById("regionTableId").innerHTML=data;
                        $("#regionTableId").html(data);
//                        initCollapser("");
//                         home();
                    }
                    else{
                        $("#designId").css({'height':'400px'})
                        $("#designId").css({'padding-bottom':''})
                        var display="<tr><td height='300px' align='center' >NO DATA</td></tr>";
                        document.getElementById("designId").style.display='';
                         $("#regionTableId").html(display);
//                        document.getElementById("regionTableId").innerHTML=display;

                    }
            }
                });

}
//kruthika
function scheduletheoneview(oneviewid,oneviewname){
var idofoneview;
idofoneview = parent.oneviewID;

// $("#scheduleName").val("");
   $("#sDatepicker").val("");
         $("#eDatepicker").val("");
          $("#hrs").val("");
         $("#mins").val("");
              parent.$("#scheduleReportalertId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    parent.$("#selectdatetype1").show();
                     parent.$("#selectdatetype").hide();
                       parent.$("#sDatepicker").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });

                    parent.$("#eDatepicker").datepicker({
                       changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
                      parent.$("#sDatepicker").val('');
                    parent.$("#eDatepicker").val('');
       parent.$("#selectdatetype").hide();
             parent.$("#scheduleReportalertId").dialog('open')
             parent.$("#alertnameid").hide();
             parent.$("#schedulenameid").show();
             parent.$("#nonCondtionaldMailId").hide();
             parent.$("#alert123").hide();
             parent.$("#nonConditionalValId").hide();
             parent.$("#sendoneviewmeasure").hide();
          //   parent.$("#scheduleoneview").show();
         var innerHtml = '+<td   align="center"><input  class="navtitle-hover" type="button" onclick="scheduleoneviews(\''+idofoneview+'\',\''+oneviewname+'\')" value="Schedule">'
                     '</td>'
                     '</tr>'
             parent.$("#scheduleoneview").html(innerHtml);
                  parent.$("#scheduleoneview").show();


}

//kruthika
function scheduleoneviews(oneviewid , oneviewname){
               var validate = true;
               var oneveiwDate = $("#dateId").val();
//               alert(oneveiwDate)

               var checkforCustDate = '';
               var absolutOrPercent = '';

               var subject = "";
               var sDatepicker = "";
               var eDatepicker = "";
               var usertextarea = "";
              var  fileType="H";
               var hrs = "0";
               var mnts = "0";
             //  var periodType="Previous Day";
           subject=parent.$("#scheduleName1").val();
//                 alert(subject);

           usertextarea="sandeep.nagapuri@progenbusiness.com";
               sDatepicker = parent.$("#sDatepicker").val();
//               alert(sDatepicker);

               eDatepicker = parent.$("#eDatepicker").val();
//                alert(eDatepicker);

//               usertextarea = parent.$("#usertextarea").val();
//               alert(usertextarea)
               hrs = parent.$("#hrs").val();
               mnts = parent.$("#mins").val();
                   var periodType = $("#alertDateTypeId2").val();
//               alert(periodType);
                   var selectdatetype=$("#frequency").val();
//                   alert(selectdatetype);

               if(sDatepicker==""){
                   alert("Please Select Start Date!")
                   validate = false;
               }
              else if(eDatepicker==""){
                   alert("Please Select End Date!")
                   validate = false;
               }
//              else if(usertextarea==""){
//                   alert("Please Enter EmailId!")
//                   validate = false;
//               }
              else if(hrs=="0"){
                   alert("Please Select Time!")
                   validate = false;
               }
var path =parent.ctxpath;
              if(validate){
                parent.$("#scheduleReportalertId").dialog('close');
                   $.post(

                         parent.ctxpath+'/reportViewer.do?reportBy=dailyScheduleReport&reportId='+oneviewid+'&scheduleName='+subject+'&usertextarea='+usertextarea+'&frequency='+selectdatetype+'&fileType='+fileType+'&startdate='+sDatepicker+'&enddate='+eDatepicker+'&hrs='+hrs+'&mins='+mnts+'&Data='+periodType+'&oneveiwDate='+oneveiwDate+'&fromoneviewshcedule=true&oneviewid='+oneviewid+'&contextpath='+path,
                         function(data){
  if(data==''){
                                    $("#scheduleoneview").dialog("close");
                               alert("Oneview is Scheduled Sucessfully")
                                    parent.$("#loading").hide();
                                }
                                else{
                                    alert('Scheduler with this name already exists! Please enter another name.')
                                    parent.$("#loading").hide();

                                }
                }
                   );
                 }



}
