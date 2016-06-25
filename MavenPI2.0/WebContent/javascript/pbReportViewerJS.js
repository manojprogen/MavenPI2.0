/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//added by susheela
  var isMemberUseInOtherLevel="false"
  var flag;
   var grpColArray=new Array();
   var dimArray = new Array();
    var screenHeight;
  var current_defaulttab=null;
  var current_reportId=null;
    
    
    
// $(document).ready(function(){
    
    
//    $("body").on("click",".currentloadingMenu",function(){
//     current_defaulttab=$(this).attr("name");
//     current_reportId=document.getElementById("REPORTID").value;    
//     var urlxx=$("#contextpath").attr("value")+'/reportTemplateAction.do?templateParam=saveCurrentTabAndReportId&current_defaulttab='+current_defaulttab+'&current_reportId='+current_reportId;
//    // var urlxx=$("#contextpath").attr("value")+'/currentReportViewer.do?reportBy=saveCurrentTabAndReportId&current_defaulttab='+current_defaulttab+'&current_reportId='+current_reportId;
//    // alert(urlxx);
//        $.ajax({
//          url:urlxx,
//          success:function(data){
//           }
//         });
//    }); 
//  });
      
    
    
function saveReportDrillDiv()
{

    $('#customReportDrill').dialog('close');
}

$(window).load(function(){
     var innerWidth=parseInt(window.innerWidth);
      screenHeight=parseInt(window.innerHeight);
    if(checkBrowser() == "ie")
    {

        $("#dispTabProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 600,
            width: 480,
            modal: true,
            position:'justify'
        });
         $("#applycolrdiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 450,
            width: 750,
            modal: true
        });
         $("#correlationId").dialog({
            autoOpen: false,
            height: 200,
            width: 500,
            position: 'justify',
            modal: true
                    });

    $("#customReportDrill").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 500,
            width: 500,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#favLinksDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 680,
            width: 660,
            position: 'top',
            modal: true
        });
        $("#composeMessageDialog").dialog({
            autoOpen: false,
            height: 480,
            width: 740,
            position: 'justify',
            modal: true
        });
        $("#snapShotDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 600,
            position: 'justify',
            modal: true
        });
         $("#snapshotHeadlineDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 200,
            width: 500,
            position: 'justify',
            modal: true
        });
        $("#DynamicHeadlineDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 200,
            width: 500,
            position: 'justify',
            modal: true
        });
        $("#reportSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 380,
            width: 580,
            position: 'justify',
            modal: true
        });
        $("#TrackerSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            width: 700,
            height:610,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#busRoleDiv").dialog({
            autoOpen: false,
            height: 600,
            width: 350,
            position: 'justify',
            modal: true
        });
        $(".navigateDialog").dialog({
            autoOpen: false,
            height: 620,
            width: 820,
            position: 'justify',
            modal: true
        });
        $("#showExports").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 250,
            width: 380,
            modal: true
        });
//        $("#dispTabProp").dialog({
//            bgiframe: true,
//            autoOpen: false,
//            height: 470,
//            width: 400,
//            modal: true,
//            position:'justify'
//        });
        $("#dispGrpProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 350,
            width: 700,
            modal: true,
            // title: grpName+" Graph Properties"
            title: "Graph Properties"
        });
        $("#iTextDiv").dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            position: 'justify',
            modal: true
        });
//        $("#dispTabProp").dialog({
//            bgiframe: true,
//            autoOpen: false,
//            height: 470,
//            width: 400,
//            modal: true,
//            position:'justify'
//        });
        $("#saveNewReport").dialog({
            autoOpen: false,
            height: 320,
            width: 350,
            position: 'justify',
            modal: true
        });
        $("#replyMessageDialog").dialog({
            autoOpen: false,
            height: 500,
            width: 740,
            position: 'justify',
            modal: true
        });
        $("#graphColsDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#editViewByDiv").dialog({
            autoOpen: false,
            height: 400,
            width: 520,
            position: 'justify',
            modal: true
        });
        $("#filterViewByDiv").dialog({
            autoOpen: false,
            height: 400,
            width: 520,
            position: 'justify',
            modal: true
        });
        $("#editViewByDivgbl").dialog({
            autoOpen: false,
            height: 400,
            width: 320,
            position: 'justify',
            modal: true
        });
        //added by Dinanath
        $("#saveRTD").dialog({
            autoOpen: false,
            height: 400,
            width: 520,
            position: 'justify',
            modal: true
        });
        $("#tableColsDialog").dialog({
            autoOpen: false,
            height: 520,
            width: 720,
            position: 'justify',
            modal: true
        });

        $("#dispgrpAnalysis").dialog({
            bgiframe: true,
            autoOpen: false,
            height:500,
            width: 650,
            modal: true,
            position:'justify'
        });
        $("#dispbucketAnalysis").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 600,
            width: 650,
            modal: true,
            position:'justify'
        });
        $("#shwparamAssis").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 520,
            width: 400,
            modal: true,
            position:'justify'
        });
        $("#columnPropertiesdiv").dialog({
            autoOpen: false,
            height: 250,
            width: 450,
            modal: true
        });
        $("#custmemDispDia").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 750,
            width: 550,
            modal: true
        });
        $("#custmemMeasureDispDia").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 750,
            width: 550,
            modal: true
        });
        $("#showSqlStrDialog").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 650,
            width: 550,
            modal: true
        });
        $("#createSegmentDiv").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 550,
            width: 550,
            modal: true
        });
        $("#performWhatIfDiv").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 1050,
            width: 780,
            modal: true,
            close:function(){
                rebuildingWhatIf()
            }
        });
        $("#openSlidreRangeDiv").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 380,
            width: 800,
            modal: true
        });
        $("#DefineTarget").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 700,
            width: 700,
            modal: true
        });
        $("#MapMeasures").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 700,
            modal: true
        });
        $("#sensitivityDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 500,
            width: 700,
            modal: true
        });
        $("#createSegmentDialogDiv").dialog({
            autoOpen: false,
            height: 220,
            width: 500,
            position: 'justify',
            modal: true
        });
       $("#editSensitivityDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 100,
            width: 400,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#groupMeasureDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 600,
            width: 700,
            modal: true
        });
         $("#openEditGrpMesNames").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 400,
            modal: true
        });
        $("#TimeBasedDiaolgDisplay").dialog({
                   autoOpen: false,
                   height: 397,
                   width: 572,
                   modal: true
       });

         $("#mapColorGrouping").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 550,
            modal: true
        });

         $("#comparableReportsDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 550,
            width:500,
            modal: true,
            position:'justify'
        });
        $("#sequenceDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 480,
            width: 520,
            modal: true,
            position:'justify',
            title:'Custom Sequencing'
        });

     $("#overWriteReport").dialog({
            autoOpen: false,
            height: 350,
            width: 520,
            position: 'justify',
            modal: true
        });
          $("#DefinecustSeqDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                            $("#zoomgraph").dialog({
            bgiframe: true,
            autoOpen: false,
            height: screenHeight,
            width: innerWidth,
            modal: true,
            // title: grpName+" Graph Properties"
            title: "Graph Properties"
        });
         $("#hideMeasureDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 550,
                      width:500,
                      modal: true,
                      position:'justify'
                     });
           $("#hideViewByDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 550,
                      width:500,
                      modal: true,
                      position:'justify'
                     });
             $("#importExcelFileDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height:250,
                      width:380,
                      modal: true,
                      position:'justify'
                     });
            $("#CustomerReportBugMail").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 520,
                      width:490,
                      modal: true,
                      position:'justify'
                     });
                  $("#mappingExcelFileDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height:350,
                      width:950,
                      modal: true,
                      position:'justify'
                     });
    }
    else
    {
        $("#mappingExcelFileDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height:350,
                      width:950,
                      modal: true,
                      position:'justify'
                     });
         $("#importExcelFileDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 250,
                      width:380,
                      modal: true,
                      position:'justify'
                     });
      $("#CustomerReportBugMail").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 520,
                      width: 490,
                      modal: true,
                      position:'justify'
                     });
        $("#hideViewByDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 550,
                      width:500,
                      modal: true,
                      position:'justify'
                     });
         $("#hideMeasureDiv").dialog({
                      bgiframe: true,
                      autoOpen: false,
                      height: 550,
                      width:500,
                      modal: true,
                      position:'justify'
                     });

      $("#overWriteReport").dialog({
            autoOpen: false,
            height: 550,
            width: 550,
            position: 'justify',
            modal: false
        });
  $("#snapshotHeadlineDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 200,
            width: 500,
            position: 'justify',
            modal: true
        });

        $("#DynamicHeadlineDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 200,
            width: 500,
            position: 'justify',
            modal: true
        });
        $("#correlationId").dialog({
                        autoOpen: false,
                        height: 150,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
        $("#dispTabProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 440,
            modal: true
        });
         $("#applycolrdiv").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 350,
        width: 700,
        modal: true
    });
$("#columnPropertiesdiv").dialog({
            autoOpen: false,
            height: 250,
            width: 450,
            modal: true
        });
        $("#customReportDrill").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 500,
            width: 500,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#busRoleDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 340,
            width: 340,
            position: 'justify',
            modal: true
        });
        $("#snapShotDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 600,
            position: 'justify',
            modal: true
        });
        $("#reportSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 280,
            width: 580,
            position: 'justify',
            modal: true
        });

        $("#favLinksDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 520,
            width: 660,
            position: 'justify',
            modal: true
        });
        $(".navigateDialog").dialog({
            autoOpen: false,
            height: 460,
            width: 820,
            position: 'justify',
            modal: true
        });
        $("#composeMessageDialog").dialog({
            autoOpen: false,
            height: 360,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#TrackerSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            width: 700,
            height:450,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#dispGrpProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 350,
            width: 700,
            modal: true,
            // title: grpName+" Graph Properties"
            title: "Graph Properties"
        });
        $("#iTextDiv").dialog({
            autoOpen: false,
            height: 200,
            width: 350,
            position: 'justify',
            modal: true
        });
        $("#showExports").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 250,
            width: 380,
            modal: true
        });
      //added by krishan
          $("#showExports1").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 250,
            width: 380,
            modal: true
        });
//        $("#dispTabProp").dialog({
//            bgiframe: true,
//            autoOpen: false,
//            height: 285,
//            width: 350,
//            modal: true
//        });
        $("#saveNewReport").dialog({
            autoOpen: false,
            height: 200,
            width: 350,
            position: 'justify',
            modal: true
        });
        $("#replyMessageDialog").dialog({
            autoOpen: false,
            height: 360,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#graphColsDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#editViewByDiv").dialog({
            autoOpen: false,
            height: 450,
            width: 650,
            position: 'justify',
            modal: true
        });
        $("#editMeasureAsViewBysDiv").dialog({
            autoOpen: false,
            height: 450,
            width: 650,
            position: 'justify',
            modal: true
        });
        $("#filterViewByDiv").dialog({
            autoOpen: false,
            height: 450,
            width: 650,
            position: 'justify',
            modal: true
        });
          $("#editViewByDivgbl").dialog({
            autoOpen: false,
            height: 400,
            width: 320,
            position: 'justify',
            modal: true
        });
        //added by Dinanath
         $("#saveRTD").dialog({
             autoOpen: false,
            height: 400,
            width: 520,
            position: 'justify',
            modal: true
        });
        $("#tableColsDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });

        $("#dispgrpAnalysis").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 500,
            width: 650,
            modal: true,
            position:'justify'
        });

        $("#dispbucketAnalysis").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 600,
            width: 650,
            modal: true,
            position:'justify'
        });
        $("#shwparamAssis").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 250,
            width: 300,
            modal: true,
            position:'justify'
        });
        $("#custmemDispDia").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 650,
            width: 550,
            modal: true
        });
        $("#custmemMeasureDispDia").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 750,
            width: 550,
            modal: true
        });
        $("#showSqlStrDialog").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 500,
            width: 550,
            modal: true
        });
          //added byy sruthi to display the full query
         $("#showSqlStrDialogfull").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 500,
            width: 550,
            modal: true
        });//ended by sruthi
        $("#createSegmentDiv").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 350,
            width: 550,
            modal: true
        });
        $("#performWhatIfDiv").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 550,
            width: 780,
            modal: true,
            close:function(){
                rebuildingWhatIf()
            }

        });
        $("#openSlidreRangeDiv").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 380,
            width: 800,
            modal: true
        });
        $("#DefineTarget").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 700,
            modal: true

        });
        $("#MapMeasures").dialog({
            // bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 700,
            modal: true
        });
        $("#sensitivityDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 500,
            width: 700,
            modal: true

        });
        $("#createSegmentDialogDiv").dialog({
            autoOpen: false,
            height: 230,
            width: 500,
            position: 'justify',
            modal: true
        });
        $("#editSensitivityDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 100,
            width: 400,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#TimeBasedDiaolgDisplay").dialog({
           autoOpen: false,
           height: 397,
           width: 572,
           modal: true
       });
         $("#mapColorGrouping").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 500,
            modal: true
        });
        $("#sequenceDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 480,
            width: 520,
            modal: true,
            position:'justify',
            title:'Custom Sequencing'
        });
                $("#DefinecustSeqDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
       $("#zoomgraph").dialog({
            bgiframe: true,
            autoOpen: false,
            height: screenHeight,
            width: innerWidth,
            modal: true,
            // title: grpName+" Graph Properties"
            title: "Graph Properties"
        });

   }

    $("#msgframe").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });


    $("#applyAlarmdiv").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        width: 430,
        modal: true
    });




    $("#cstLinksFrame").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        width: 700,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
      $("#groupMeasureDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 600,
            width: 700,
            modal: true
        });
        $("#openEditGrpMesNames").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 400,
            modal: true
        });
    $("#prtLinksFrame").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#Scheduler").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });



    $('#defineSchedulerDiv').click(function() {
        $('#reportSchedulerDialog').dialog('open');
    });
    $('#defineTrackerDiv').click(function() {
        $('#TrackerSchedulerDialog').dialog('open');
    });
    $('#composeMessageDiv').click(function() {

        $('#composeMessageDialog').dialog('open');
    });

    $('#Customize').click(function() {

        $('#favLinksDialog').dialog('open');
    });

    $('#Prioritize').click(function() {
        $('#prtLinksFrame').dialog('open');
    });
//    $('#sanpShotDiv').click(function() {
//
//        $('#snapShotDialog').dialog('open');
//    });
//     $('#snapShotHeadline').click(function() {
//
//        $('#snapshotHeadlineDiv').dialog('open');
//    });

    $test=$(".navtitle");

    $test.hover(
        function() {
            $(this).addClass('navtitle-hover');
        },
        function() {
            $(this).removeClass('navtitle-hover');
        }
        );

    $test1=$(".navtitle1");
    $test1.hover(
        function() {
            $(this).addClass('navtitle1-hover');
        },
        function() {
            $(this).removeClass('navtitle1-hover');
        }
        );
             $("#comparableReportsDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 550,
            width: 500,
            modal: true,
            position:'justify'
        });
});
function  rebuildingWhatIf(){
   var trgtList="<td>Target measure name  <input type='text' id='trgtMesNameDisp' name='tegtMesNameDisp' style='height:21px; ' ></td>"
    $("#trgtMesNameDisp").parent().parent().html(trgtList)

}

function openStickyNotes(ctxPath,currentURL)
{
    $.get(ctxPath+'/stickyNoteAction.do?stickyNoteParam=buildAllStickyNotes&REPORTID='+document.getElementById("REPORTID").value+'&currentURL='+currentURL,
        function(data) {
            if(data!=null||data!=""){
                var stickyHTML=data.split("|_|");
                for(var i=0;i<stickyHTML.length;i++){
                    var innerData =$('#StickNoteSpan').html();
                    $('#StickNoteSpan').html(stickyHTML[i]+innerData);
                }
                if(document.getElementById('StickNoteSpan')!=null)
                document.getElementById('StickNoteSpan').style.display="";
            }
        });

}

function doDrillAcross(reportId, ctxPath, targetReportId, params){

    $.ajax({
        url:ctxPath+"/reportTemplateAction.do?templateParam=drillAcross&reportId="+reportId+"&targetReportId="+targetReportId+"&params="+params,
        success : function(data){
            document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+targetReportId+data;
            document.forms.frmParameter.submit();
        }
    });
}

function doDrill(reportId, ctxPath, params){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+reportId+params;
    document.forms.frmParameter.submit();
}

function checkMap(){
  //  alert("rajesh from js file");
/*var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;

    $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=isMapEnabled&REPORTID='+REPORTID,
        success : function(data){
            alert.show(data);
        }
    })*/
}

function displayfavlink(){
    $("#favlinkcont").toggle({
        persist: "cookie"
    });
}
function displayWidgets(){
    $("#Widgets").toggle({
        persist: "cookie"
    });
}
function displayTopBottomRes(){
    $("#topBot").toggle(500);

}
function dispParameters(){
    $("#tabParameters").toggle({
        persist: "cookie"
    });
}
function dispGraphs(){
    $("#tabGraphs").toggle({
        persist: "cookie"
    });
}
function dispTables(){
    $("#tabTable").toggle({
        persist: "cookie"
    });
    function dispMessages(){
        $("#messages").toggle({
            persist: "cookie"
        });
    }
    function dispSnapShots(){
        $("#snapshots").toggle({
            persist: "cookie"
        });
    }
}
function cancelGrpProperties(){
    $("#dispGrpProp").dialog('close');

}
function refreshReportGraphs(ctxPath,tabId){
    $("#dispGrpProp").dialog('close');
    var source = ctxPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+tabId;
    document.getElementById("iframe4").src= source;

}
function showGrpProperties(reportId,graphId,ctxPath,grpName,selectedgraphtype){
  //  alert("pbReportViewerJs");
    $('#dispGrpProp').data('title.dialog', grpName);
    $("#dispGrpProp").dialog('open');
    var frameObj=document.getElementById("dispGrpPropFrame");
    var source =ctxPath+'/TableDisplay/PbGraphProperties.jsp?reportId='+reportId+'&graphId='+graphId+'&selectedgraphtype='+selectedgraphtype;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
}

 function refreshReportTables(ctxPath,tabId,pageSize){
    //    $("#dispTabProp").dialog('close');
 // if(pageSize==null){
    var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+tabId+"&slidePages="+pageSize;
    document.getElementById("iframe1").src= source;
    parent.document.getElementById('loading').style.display='none';
}
function refreshReportTablesColumn(ctxPath,tabId,pageSize){
    var source = ctxPath+'/TableDisplay/pbDisplay.jsp?tabId='+tabId+'&slidePages='+pageSize;
     var frameObj=parent.document.getElementById("iframe1");
   
  frameObj.src= source;
}
function showTableProperties(reportId,ctxPath,pageSize){
//
//    $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//           if(userType=="Admin"){
               parent.$("#dispTabProp").dialog('open');
               var frameObj=document.getElementById("dispTabPropFrame");
               var source =ctxPath+'/TableDisplay/PbReportTableProperties.jsp?reportId='+reportId+"&slidePages="+pageSize;
              frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
             frameObj.src=source;
//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});
}
//added by sruthi for tablecolumn pro
function showTableColumnProperties(reportId,ctxPath,pageSize){
 $("#dispTabColumnProp").dialog({
                   autoOpen: false,
                    height: 550,
                    width: 900,
                   modal: true,
                   resizable:true
//                 title:'Table Column Properties'
                });

$("#dispTabColumnProp").dialog('open');
 var frameObj=document.getElementById("dispTabColumnPropFrame");
   var source =ctxPath+'/PbTableColumnProperties.jsp?reportId='+reportId+"&slidePages="+pageSize;
    frameObj.src=source;
}
//ended by sruthi
//added by chiranjeevi for iText
function iText(disColumnName,colName){
    $.ajax({
        url:'reportTemplateAction.do?templateParam=getitext&elementId='+colName,
        success: function(data){
            document.getElementById("itextarea").value=data;
        }
    });
    $('#iTextDiv').data('title.dialog', disColumnName);
    $("#iTextDiv").dialog('open')
}
function refreshGraphs1(ctxPath,tabId){
    var source = ctxPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+tabId;
    document.getElementById("iframe4").src= source;
}
function openNewReportDtls(ctxPath){

//    $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
              $("#saveNewReport").dialog('open')
//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});

}
//sandeep
//function getenablevalues(msrdata, priorvalue){
//    alert(msrdata)
//    alert(priorvalue)
//     $("#getenblevalues").dialog({
//            autoOpen: false,
//            height: 100,
//            width: 220,
//            position: 'justify',
//            modal: true
//        });
//         $("#getenblevalues").dialog('open');
//}
//sandeep
function kpieditViewBy(){
    $("#editViewByDivkpi").dialog({
            autoOpen: false,
            height: 400,
            width: 520,
            position: 'justify',
            modal: true
        });
 var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("editViewByFramekpi");
    $("#Designer").val("viewer");

     $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("#ViewByForm").serialize() ,
         function(data){
            var source = ctxPath+"/Report/Viewer/ChangeViewBy.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+'&iskpidashboard=true';
            frameObj.src = source;
      });
       parent.$("#measuresDialog").dialog('close');
     $("#editViewByDivkpi").dialog('open');


        }
//sandeep for global filter
        function editFilterBy(){

    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("filterViewByFrame");
     $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath+'&globalfilter=true', $("#ViewByForm").serialize() ,
         function(data){
            var source = ctxPath+"/Report/Viewer/ChangeViewBy.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&globalfilter=true";
            frameObj.src = source;
      });
     $("#filterViewByDiv").dialog('open');
        }
        //end of sandeep code
function editViewByprop(){

    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("editViewByFrame");
 //   var flag=parent.selectedview;
//alert(flag)
     $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("#ViewByForm").serialize() ,
         function(data){
            var source = ctxPath+"/Report/Viewer/ChangeViewBy.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&flag=true";
            frameObj.src = source;
      });
     $("#editViewByDiv").dialog('open');
        }
function editViewBy(){

    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("editViewByFrame");
    var flag=parent.selectedview;

     $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("#ViewByForm").serialize() ,
         function(data){
            var source = ctxPath+"/Report/Viewer/ChangeViewBy.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&flag=false";
            frameObj.src = source;
      });
     $("#editViewByDiv").dialog('open');
        }
//Added By Ram 05 Jan 2016 for Measures as ViewBys
function editMeasureAsViewBy(){
    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("editMeasureAsViewBysFrame");
     $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("#ViewByForm").serialize() ,
         function(data){
             $("#editMeasureAsViewBysDiv").dialog('open');
            var source = ctxPath+"/Report/Viewer/ChangeMeasuresAsViewbys.jsp?loadDialogs=true&REPORTID="+REPORTID+"&ctxPath="+ctxPath;
            frameObj.src = source;
      });
   //  $("#editMeasureAsViewBysDiv").dialog('open');
        }
//Ended by Ram
  // added by veena on jun23 2012
 function AddMoreDims(IsrepAdhoc) {

//          $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
                var roleId = document.getElementById("roleid").value;
                //added by Mohit for AO
                 var aoid = document.getElementById("AOId").value;
                
//                alert(roleId)
//                 alert(aoid)
                var From = "";
//                if(parent.document.getElementById("Designer")!= null){
//                    From=parent.document.getElementById("Designer").value;
//                }
//                var timeDetail = parent.document.getElementsByName("time");
//                 var selectdDetails = "";
//            var timeDim="";
//           if(timeDetail.length!=0){
//                    for(var i=0;i<timeDetail.length;i++){
//                        if(timeDetail[i].checked){
//                            selectdDetails=selectdDetails+","+timeDetail[i].id;
//                        }
//                    }
//                }
//                if(From != null && From=="fromDesigner"){
////                    if(selectdDetails != null && selectdDetails != ""){
//                var frameObj=document.getElementById("addmoreParamFrame");
//                var source="reportTemplateAction.do?templateParam=addMoreDimensions&foldersIds="+roleId+"&REPORTID="+document.getElementById("REPORTID").value;
//                frameObj.src=source;
//                $("#AddMoreParamsDiv").dialog('open');
////                    } else {
////                        alert("Please Select Time Basis from clock icon")
////                    }
//            }else{
                    var frameObj=document.getElementById("addmoreParamFrame");
                    var source="reportTemplateAction.do?templateParam=addMoreDimensions&foldersIds="+roleId+"&IsrepAdhoc="+IsrepAdhoc+
                            "&REPORTID="+document.getElementById("REPORTID").value+"&aoid="+aoid;
                    frameObj.src=source;
                    $("#AddMoreParamsDiv").dialog('open');
//                }
//            }else{
//                alert("You do not have the sufficient previlages")
//            }
//       }});

//     $.ajax({
//        url: 'reportTemplateAction.do?templateParam=getAddMoreDims&foldersIds='+roleId,
//        success: function(data) {
//            $("#AddMoreParamsDiv").html(data)
//        }
//        });

 }
 function saveNewRepGrpTab1(ctxPath,userId,roleId,reportId,reportName){
 //alert(reportName)
// $(document).ready(function () {
            $("#divassign").dialog({
                autoOpen: false,
                modal: true,
                width: 600,
                height: 450,
                draggable: true,
                resizable: true
            });
//        });+
        $("#divassign").dialog('open')
        var frameObj=document.getElementById("divassignframe");
        var source =ctxPath+'/pbAssignReportsToUsers.jsp?SourcePage=Viewer&ReportType=R&ReportName='+reportName+"&UserFolderIds="+roleId+"&REPORTID="+reportId+"&USERID"+userId;
        frameObj.src=source;

 }

//added by mohit

  function OpenDefTabs()
       {

 $("#AllSheets").dialog().dialog('open');

        }

//modified by krishan pratap
  function SaveDefTab(reportId1){
     
  var show_id =  [];
  var radioValue="";
 var defaulttab=$("#TabId").val();

   
//  $("input[type='button']").click(function(){
//        	 radioValue = $("input[name='type']:checked").val();
//                 });
    //  radioValue=document.getElementsByName("type");
               
//    $("input:[name=check[]]:checked").each(function () {
//        show_id= show_id+","+$(this).attr("id");
////        console.log(show_id)
//
//    });
var eles =    document.getElementsByClassName("defChk");
for(var i=0;i<eles.length;i++){
if(eles[i].checked){
 show_id=show_id+","+eles[i].getAttribute("id");
}
}
          if (document.getElementById("defaultName").checked){
                radioValue= document.getElementById("defaultName").value;
            } else {
                radioValue= document.getElementById("defaultIcons").value;
            }
            show_id= show_id+","+radioValue;
if(defaulttab=='--SELECT--')
    {
        alert("Please Choose Your Default Tab")
        return false;
    }
    else
        {
 console.log("show_id ::"+show_id+" ;defaulttab ::"+defaulttab);
 $.ajax({
             url: 'reportTemplateAction.do?templateParam=SaveDefaultTab&reportId='+encodeURIComponent(reportId1)+"&defaulttab="+defaulttab+"&show_id="+show_id,
            success: function(data){
                if(data=="0"){
                     alert("Oops Something Went wrong Your Default Tab couldtn't be Saved")
                }
                else
                    {
                        alert("Default Tab Saved Successfully")
                         $("#AllSheets").dialog('close');
                    }

            }

        });
        }

 }
function saveNewRepGrpTab(repId,ctxPath,defaultGraph){
    var roleid=document.getElementById("roleid").value;
    var reportName = document.getElementById('reportName1').value;
    var reportDesc = document.getElementById('reportDesc1').value;
    var frmObj = document.getElementById("iframe4");
    var showGraphTable = frmObj.contentWindow.document.getElementById("showTable").value;
    var cacheAO = "false";

    if(reportName==''){
        alert("Please enter Report Name");
    }
    else  if(reportDesc==''){
        alert("Please enter Report Description")
    }
    else{

        $.ajax({
            url: ctxPath+'/reportTemplateAction.do?templateParam=checkReportName&reportName='+encodeURIComponent(reportName)+"&roleid="+roleid,
            success: function(data){

                if(data!=""){
                    document.getElementById('duplicate1').innerHTML = data;
                    document.getElementById('save').disabled = true;
                }
                else {
                    document.getElementById("newReportName").value=reportName;
                    document.getElementById("REPORTDESC").value=reportDesc;
                    if(showGraphTable == "GTM"){
                        document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&showGraphTable="+showGraphTable+"&cacheAO="+cacheAO;
                    }else{
                        document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&showGraphTable=GM"+"&cacheAO="+cacheAO;
                    }
                    document.forms.frmParameter.method="POST";
                    document.forms.frmParameter.submit();
                    $("#saveNewReport").dialog('close')
                }
            }
        });
    }
}

function saveAsNewReport(repId,ctxPath,repName,GraphFlag){
    
  var graphCheck = "false";
 
  if($("#withGraph").is(":checked")||GraphFlag=='Graph'){
      
      graphCheck = "true";
  }else {
      alert("Graph Not Added");
  }
    var oldReportName = repName;
    var roleid=document.getElementById("roleid").value;
    var reportName = document.getElementById('reportName1').value;
    var reportDesc = document.getElementById('reportDesc1').value;
    var cacheAO = "false";
    if(reportName==''){
        alert("Please enter Report Name");
    }
    else  if(reportDesc==''){
        alert("Please enter Report Description")
    }
    else{

        $.ajax({
            url: ctxPath+'/reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName+"&roleid="+roleid,
            success: function(data){
                if(data!=""){
                    document.getElementById('duplicate1').innerHTML = data;
                    document.getElementById('save').disabled = true;
                }
                else {
                    document.getElementById("newReportName").value=reportName;
                    document.getElementById("REPORTDESC").value=reportDesc;
                    document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&cacheAO="+cacheAO+"&isGraph="+graphCheck+"&oldReportName="+oldReportName+"&istoggledate="+istoggledate+"&defaulttab="+GraphFlag;
                    document.forms.frmParameter.method="POST";
                    document.forms.frmParameter.submit();
                    $("#saveNewReport").dialog('close')
                }
            }
        });
    }
}
function saveAsNewDashboard(repId,ctxPath){
   // alert("kpi")
    var roleid=parent.document.getElementById("roleid").value;
    var reportName = parent.document.getElementById('reportName1').value;
    var reportDesc = parent.document.getElementById('reportDesc1').value;
    if(reportName==''){
        alert("Please enter Report Name");
    }
    else  if(reportDesc==''){
        alert("Please enter Report Description")
    }
    else{

        $.ajax({
            url: ctxPath+'/reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName+"&roleid="+roleid,
            success: function(data){
                if(data!=""){
                    document.getElementById('duplicate1').innerHTML = data;
                    document.getElementById('save').disabled = true;
                }
                else {
//                    document.getElementById("newReportName").value=reportName;
//                    document.getElementById("REPORTDESC").value=reportDesc;
                    document.forms.submitReportForm.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+reportName+"&REPORTDESC="+reportDesc+"&isKPIDashboard=true&REPORTID="+repId;
                    document.forms.submitReportForm.method="POST";
                    document.forms.submitReportForm.submit();
                    $("#saveNewdashboard").dialog('close')
                }
            }
        });
    }
}
function showExports(reportId,ctxPath){
//    $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
                
                var frameObj=document.getElementById("showExportsFrame1");
                var source =ctxPath+'/TableDisplay/pbTableExports.jsp?reportId='+reportId+'&ctxPath='+ctxPath;
                frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                frameObj.src=source;
                $("#showExports1").dialog('open');
//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});


    //parent.$("#showExports").dialog('open');


}
function changeExportType(){
    var fileType=document.getElementById("fileType").value;
    var expType=document.getElementById("expType").value;
    var expRec=document.getElementById("expRec").value;
    if(fileType=="H"||fileType=="CSV"||fileType=="TS"){
        $("#SP").hide();
        $("#TI").hide();
        $("#expType").val("Report");
        $("#reportgraphOpt").hide();
        $("#graphOpt").hide();
        $("#pageTypeId").hide();
        $("#pdfcellHeightId").hide();
        $("#pdfcellFontId").hide();
        if(fileType=="H"){
        $("#paramTypeId").show();
        $("#htmlcellHeightId").show();
        }else{
         $("#paramTypeId").hide();
         $("#htmlcellHeightId").hide();
        }

//       if(fileType=="H"){
//        $("#expType").val("Report");
//        $("#reportgraphOpt").hide();
//        $("#graphOpt").hide();

    }
    else if(fileType=="CD"){
        $("#SP").show();
        $("#TI").show();
        $("#expType").val("Report");
        $("#reportgraphOpt").hide();
        $("#graphOpt").hide();
        $("#paramTypeId").hide();
        $("#pageTypeId").hide();
        $("#pdfcellHeightId").hide();
        $("#pdfcellFontId").hide();
        $("#htmlcellHeightId").hide();
        $("#tempTypeId").hide(); //added by krishan
        }
        else if(fileType=="P" || fileType=="E"){
        $("#SP").hide();
        $("#TI").hide();
        $("#htmlcellHeightId").hide();
        $("#expType").show();
        $("#reportgraphOpt").show()
        $("#graphOpt").show();
        $("#paramTypeId").show();
        if(fileType=="P"){
 
         $("#tempTypeId").hide();
         $("#pageTypeId").show();
         $("#pdfcellHeightId").show();
         $("#pdfcellFontId").show();
        }
        else{
          $("#tempTypeId").show();
          $("#pageTypeId").hide();
          $("#pdfcellHeightId").hide();
          $("#pdfcellFontId").hide();
        }
        }
        else{
        $("#SP").hide();
        $("#TI").hide();
        $("#expType").show();
        $("#reportgraphOpt").show()
        $("#graphOpt").show();
        $("#paramTypeId").hide();
        $("#pageTypeId").hide();
        $("#pdfcellHeightId").hide();
        $("#pdfcellFontId").hide();
        $("#htmlcellHeightId").hide();
        $("#tempTypeId").hide(); // added by krishans
       }
}
//function downloadExport(){
//    alert("hi");
//    var fileType=document.getElementById("fileType").value;
//    alert("filrType"+fileType);
//    var expType=document.getElementById("expType").value;
//    alert("expType"+expType);
//    var expRec=document.getElementById("expRec").value;
//    alert("expType"+expRec);
//    var REPORTID=document.getElementById("REPORTID").value;
//    alert("reportid"+REPORTID);
//    var ctxPath=document.getElementById("h").value;
//    alert("ctxPath"+ctxPath);
//
//    $("#showExports").dialog('close');
//    var source = ctxPath+"/TableDisplay/pbDownload.jsp?dType="+fileType+"&tabId="+REPORTID+"&displayType="+expType+"&expRec="+expRec;
//    var dSrc = document.getElementById("dFrame");
//    dSrc.src = source;
//
//    if(Obj.style.display=='none'){
//        Obj.style.display="block";
//    }else{
//        Obj.style.display="none";
//    }
//
//}


// modified by krishan

    function downloadExport(){

    
    var fileType=document.getElementById("fileType").value;
    var expType=document.getElementById("expType").value;
    var expRec=document.getElementById("expRec").value;
    var REPORTID=document.getElementById("REPORTID").value;
    var pdfTypeSelect=document.getElementById("pdfTypeSelect").value;
    var paramType=document.getElementById("paramWithOrwithoutId").value;
    var pdfCellFont=document.getElementById("selectCellFontId").value;
    var pdfCellHeight=document.getElementById("selectCellHeightId").value;
    var htmlCellHeight=document.getElementById("selecthtmlCellHeightId").value;
    var ctxPath=document.getElementById("h").value;
    var delimiter=$('#sDelimiter').val();
    var txtId=$('#txtIdentifier').val();
   
  //$("#showExports1").hide();
    var source = ctxPath+"/TableDisplay/pbDownload.jsp?dType="+fileType+"&tabId="+REPORTID+"&displayType="+expType+"&expRec="+expRec+"&dlimiter="+delimiter+"&txtIdentifier="+txtId+"&paramType="+paramType+"&pdfTypeSelect="+pdfTypeSelect+"&pdfCellFont="+pdfCellFont+"&pdfCellHeight="+pdfCellHeight+"&htmlCellHeight="+htmlCellHeight;
    var dSrc = document.getElementById("dFrame");
    dSrc.src = source;
     parent.$("#showExports1").dialog('close');
    if(Obj.style.display=='none'){
        Obj.style.display="block";
    }else{
        Obj.style.display="none";
    }

     
    }

function grpByParamAnalysis(reportId,viewbyId,viewbyName,ctxPath){
    var x=confirm('Gorup By Analysis is created on '+viewbyName+',Do you wish to create');
    if(x==true){
        $("#dispgrpAnalysis").dialog('open');
        var frameObj=document.getElementById("dispgrpAnalysisFrame");
        var source =ctxPath+'/TableDisplay/createParentParameter.jsp?reportId='+reportId+'&viewbyId='+viewbyId;
        frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
    }
    else{

}

}

function OverWriteReportGrpTab(reportName,reportDesc,ctxPath){
    var res=confirm("Do you want to over write the report ?");
    var frmObj = document.getElementById("iframe4");
    var showGraphTable = frmObj.contentWindow.document.getElementById("showTable").value;
    var cacheAO ="false";
    if(res){
        if(showGraphTable == "GTM"){
            document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+reportName+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&showGraphTable="+showGraphTable+"&cacheAO="+cacheAO;
        }else{
            document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+reportName+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&showGraphTable=GM"+"&cacheAO="+cacheAO;
        }
        document.forms.frmParameter.method="POST";
        document.forms.frmParameter.submit();
    }
}
function overWriteReport1(ctxPath,flagg,restrictedFlag,isMultiCompany,isPowerAnalyserEnableforUser){
    flag=flagg;
//    $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){

    //Added by Ashutosh for Restricted PowerAnalyzer 11-12-2015
    $.ajax({
        url:ctxPath+'/userLayerAction.do?userParam=isReportCreator'+"&REPORTID="+document.getElementById("REPORTID").value,
        success:function(data){
            var data1 = JSON.parse(data);
            var keys = Object.keys(data1);
//            alert("Report Creator is "+data1[keys[0]])
//            alert("RPA is "+restrictedFlag);
            if(isMultiCompany==="true" || isMultiCompany===true){
                $.ajax({
                    url:ctxPath+'/userLayerAction.do?userParam=getUserRole',
                    success:function(data){ 
                        if(data!=null || data !="null"){
                            window.notInsight=false;
                            var data1 = JSON.parse(data);
                            var admin = data1["Admin"];
                            isPowerAnalyserEnableforUser = data1["isPowerAnalyserEnableforUser"];
                            restrictedFlag = data1["restrictedFlag"];
                            var isCompanyAccessible = data1["isCompanyAccessible"];
                            if(admin === true || (isPowerAnalyserEnableforUser === true && restrictedFlag === false) || (restrictedFlag === true && window.isReportCreator === true)){
                                var res=confirm("It Saves TimeDetails,Parameter,Graph and Table regions Would you like to Continue?");
                                if(res){
                                    var con=confirm("would you like to have a backup for this Report");
                                    if(con){
                                        $.ajax({
                                            url:ctxPath+'/reportViewer.do?reportBy=chkforBackupfiles&REPORTID='+document.getElementById("REPORTID").value+"&current_defaulttab="+current_defaulttab+"&current_reportId="+current_reportId,
                                            success:function(data){

                                                $("#overWriteReport").dialog('open')

                                            }
                                        });
                                    } else{
                                        $("#overWriteReport").dialog('open')
                                    }
                                }

                            }else if(restrictedFlag === true && isCompanyAccessible === true){
                                alert("Not Enough Privilege to OverWrite Report!!");
                                openNewReportDtls(ctxPath);
                            }else{
                                alert("Not Enough Privilege to OverWrite Report!!");
                            }
                        }else{window.notInsight=true;}
                    }
                });
                if(window.notInsight){
                if(data1[keys[0]]===false && restrictedFlag===true) {
                 alert("Not Enough Privilege to OverWrite Report!!")
                    openNewReportDtls(ctxPath);
                }else if ((data1[keys[0]]===true) || (data1[keys[0]]===false && isPowerAnalyserEnableforUser === true && restrictedFlag===false)){
                    var res=confirm(" It Saves TimeDetails,Parameter,Graph and Table regions Would you like to Continue?");
                    if(res){
                        var con=confirm("would you like to have a backup for this Report");
                        if(con){
                            $.ajax({
                                url:ctxPath+'/reportViewer.do?reportBy=chkforBackupfiles&REPORTID='+document.getElementById("REPORTID").value+"&current_defaulttab="+current_defaulttab+"&current_reportId="+current_reportId,
                                success:function(data){

                                    $("#overWriteReport").dialog('open')

                                }
                            });
                        } else{
                            $("#overWriteReport").dialog('open')
                        }
                    }
                }
            }
            }
            else{
                var res=confirm("It Saves TimeDetails,Parameter,Graph and Table regions Would you like to Continue?");
    if(res){
                    var con=confirm("would you like to have a backup for this Report");
                    if(con){
                        $.ajax({
                            url:ctxPath+'/reportViewer.do?reportBy=chkforBackupfiles&REPORTID='+document.getElementById("REPORTID").value+"&current_defaulttab="+current_defaulttab+"&current_reportId="+current_reportId,
                            success:function(data){

              $("#overWriteReport").dialog('open')

    }
                        });
                    } else{
                    $("#overWriteReport").dialog('open')
                }
                }


}
        }
    });


}
function kpiOverWriteReport(ctxPath,reportId){
//    var res=confirm("Do you want to over write the report ?");
//    if(res){

var reportName=document.getElementById("Name").value;
var reportDesc=document.getElementById("Desc").value;

//alert(reportName)
var Gtregion;
if($("#grandTotalDiv").is(':visible')){
    Gtregion=true;}
else{
    Gtregion=false;
}
  var autometicDate;
     if($("#autometicDate").is(':checked'))
             autometicDate=true;
        else
             autometicDate=false;

        var Date=""
        if(document.getElementById("sysDate") != null && document.getElementById("sysDate").checked){
           Date="systemDate"
        }else if(document.getElementById("reportDate") != null && document.getElementById("reportDate").checked){
            Date="reportDate"
        }if(document.getElementById("currdetails") != null && document.getElementById("currdetails").checked){
           Date="currdetails"
        }else if(document.getElementById("yestrday") != null && document.getElementById("yestrday").checked){
            Date = "yestrday"
        }else if(document.getElementById("tomorow") != null && document.getElementById("tomorow").checked){
            Date = "tomorow"
        }else if(document.getElementById("newSysDate") != null && document.getElementById("newSysDate").checked){
            var sysSign = $("#sysSign").val();
            var newSysVal = $("#newSysVal").val();
            if(newSysVal==""){
                newSysVal="0";
            }
            Date = "newSysDate,".concat(sysSign,",", newSysVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("globalDate") != null && document.getElementById("globalDate").checked){
            var globalSign = $("#globalSign").val();
            var newGlobVal = $("#newGlobVal").val();
            if(newGlobVal==""){
                newGlobVal="0";
            }
            Date = "globalDate,".concat(globalSign,",", newGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        if(document.getElementById("customDate") != null && document.getElementById("customDate").checked){
            var frmDate = "";
            var toDate = "";
            var cmpfrmDate = "";
            var cmptoDate = "";
        if(document.getElementById("fromyestrday") != null && document.getElementById("fromyestrday").checked){
//            var fromToSign = $("#fromToSign").val();
//            var fromToVal = $("#fromToVal").val();
//            //Date = "fromToDate,".concat(fromToSign,",", fromToVal)
//           // Date = Date.toString().replace(" ", "+", "gi");
           Date = "fromyestrday";
        }else if(document.getElementById("fromtomorow") != null && document.getElementById("fromtomorow").checked){
            Date="fromtomorow"
        }else if(document.getElementById("fromSysDate") != null && document.getElementById("fromSysDate").checked){
            var fromSysSign = $("#fromSysSign").val();
            var fromSysVal = $("#fromSysVal").val();
            if(fromSysVal=="")
               fromSysVal = "0";
            Date = "fromSysDate,".concat(fromSysSign,",", fromSysVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("fromglobalDate") != null && document.getElementById("fromglobalDate").checked){
            var fromglobalSign = $("#fromglobalSign").val();
            var fromGlobVal = $("#fromGlobVal").val();
            if(fromGlobVal=="")
               fromGlobVal = "0";
            Date = "fromglobalDate,".concat(fromglobalSign,",", fromGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        frmDate = Date;
        if(document.getElementById("toyestrday") != null && document.getElementById("toyestrday").checked){
           Date = frmDate+"@toyestrday";
        }else if(document.getElementById("totomorow") != null && document.getElementById("totomorow").checked){
           Date = frmDate+"@totomorow";
        }else if(document.getElementById("toSystDate") != null && document.getElementById("toSystDate").checked){
            var toSysSign = $("#toSysSign").val();
            var toSysVal = $("#toSysVal").val();
            if(toSysVal==""){
            toSysVal = "0"
            }
            Date = "toSystDate,".concat(toSysSign,",", toSysVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("toglobalDdate") != null && document.getElementById("toglobalDdate").checked){
            var toglobalSign = $("#toglobalSign").val();
            var toGlobVal = $("#toGlobVal").val();
            if(toGlobVal=="")
               toGlobVal = "0";
            Date = "toglobalDdate,".concat(toglobalSign,",", toGlobVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        toDate = Date;
        if(document.getElementById("CmpFrmyestrday") != null && document.getElementById("CmpFrmyestrday").checked){
           Date = toDate+"@CmpFrmyestrday";
        }else if(document.getElementById("CmpFrmtomorow") != null && document.getElementById("CmpFrmtomorow").checked){
            Date = toDate+"@CmpFrmtomorow"
        }else if(document.getElementById("CmpFrmSysDate") != null && document.getElementById("CmpFrmSysDate").checked){
            var CmpFrmSysSign = $("#CmpFrmSysSign").val();
            var CmpFrmSysVal = $("#CmpFrmSysVal").val();
            if(CmpFrmSysVal==""){
                CmpFrmSysVal = "0";
            }
            Date = "CmpFrmSysDate,".concat(CmpFrmSysSign,",", CmpFrmSysVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("CmpFrmglobalDate") != null && document.getElementById("CmpFrmglobalDate").checked){
            var CmpFrmglobalSign = $("#CmpFrmglobalSign").val();
            var CmpFrmGlobVal = $("#CmpFrmGlobVal").val();
            if(CmpFrmGlobVal=="")
               CmpFrmGlobVal = "0";
            Date = "CmpFrmglobalDate,".concat(CmpFrmglobalSign,",", CmpFrmGlobVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmpfrmDate = Date;
        if(document.getElementById("cmptoyestrday") != null && document.getElementById("cmptoyestrday").checked){
           Date = cmpfrmDate+"@cmptoyestrday";
        }else if(document.getElementById("cmptotomorow") != null && document.getElementById("cmptotomorow").checked){
            Date = cmpfrmDate+"@cmptotomorow"
        }else if(document.getElementById("cmptoSysDate") != null && document.getElementById("cmptoSysDate").checked){
            var cmptoSysSign = $("#cmptoSysSign").val();
            var cmptoSysVal = $("#cmptoSysVal").val();
            if(cmptoSysVal==""){
                cmptoSysVal = "0";
            }
            Date = "cmptoSysDate,".concat(cmptoSysSign,",", cmptoSysVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("cmptoglobalDate") != null && document.getElementById("cmptoglobalDate").checked){
            var cmptoglobalSign = $("#cmptoglobalSign").val();
            var cmptoGlobVal = $("#cmptoGlobVal").val();
             if(cmptoGlobVal=="")
               cmptoGlobVal = "0";
            Date = "cmptoglobalDate,".concat(cmptoglobalSign,",", cmptoGlobVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmptoDate = Date;
        Date = cmptoDate;
    }
        if(flag=='overWrite'){
//      added by manik
//      document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&autometicDate="+autometicDate+"&overWriteExistingReport=true&Date="+Date+"&Gtregion="+Gtregion;
        parent.document.forms.submitReportForm.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&autometicDate="+autometicDate+"&overWriteExistingReport=true&Date="+Date+"&Gtregion="+Gtregion+"&REPORTID="+reportId+"&isKPIDashboard=true";
        parent.document.forms.submitReportForm.method="POST";
        parent.document.forms.submitReportForm.submit();
        }
        else if(flag=='time'){
//            document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=saveTimeDetails&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&Date="+Date;
//            document.forms.frmParameter.method="POST";
            //document.forms.frmParameter.submit();
             $.post(
                  ctxPath+"/reportViewer.do?reportBy=saveTimeDetails&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&Date="+Date+"&reportId="+reportId,
             function(data){
                 if(data){
                     alert("TimeDetails Saved Successfully");
                 }

             });
        }
        $("#overWriteReport").dialog('close');


//    }
}

// added by krishan Pratap
function OverWriteReport(ctxPath,reportId,istoggledate1,incriment){
    var istoggledate;
    if(incriment==0){
$.ajax({
                async:false,
                type:"POST",
                url:  ctxPath+'/reportViewer.do?reportBy=getToggle&reportid='+reportId,
                success:function(data) {
                      if(data=='Yes'){
                istoggledate='Yes';
//                alert("istoggledate"+istoggledate)
                    }else{
                        istoggledate='No';
                        
                    }
                }

                
});

 OverWriteReport1(ctxPath,reportId,istoggledate)
}
else
    {
        //istoggledate=istoggledate1;
        OverWriteReport1(ctxPath,reportId,istoggledate1)
    }
}
// modified by krishan pratap
function OverWriteReport1(ctxPath,reportId,istoggledate){

    //alert("istoggledate........"+istoggledate)
//    var res=confirm("Do you want to over write the report ?");
//    if(res){if((document.getElementById("fixeddate") == null && document.getElementById("fixeddate").checked )|| (document.getElementById("fromfixeddate") == null && document.getElementById("fromfixeddate").checked )|| (document.getElementById("tofixeddate") == null && document.getElementById("tofixeddate").checked )
//alert($("#fdatepicker").val())
if((document.getElementById("fixeddate")!= null && $("#fdatepicker").val() == "" && document.getElementById("fixeddate").checked )|| (document.getElementById("fromfixeddate")!= null && $("#ffdatepicker").val() == "" && document.getElementById("fromfixeddate").checked )|| (document.getElementById("tofixeddate")!= null && $("#ftdatepicker").val() == "" && document.getElementById("tofixeddate").checked )||(document.getElementById("CmpFrmfixeddate")!= null && $("#fcfdatepicker").val() == "" && document.getElementById("CmpFrmfixeddate").checked )|| (document.getElementById("Cmptofixeddate")!= null && $("#fctdatepicker").val() == "" && document.getElementById("Cmptofixeddate").checked )){
    alert("Please Choose Fixed Date");

      }else{
var reportName=document.getElementById("Name").value;
var reportDesc=document.getElementById("Desc").value;

//alert(reportName)
var Gtregion;
if($("#grandTotalDiv").is(':visible')){
    Gtregion=true;}
else{
    Gtregion=false;
}
  var autometicDate;
  var cacheAO = "false";
     if($("#autometicDate").is(':checked'))
             autometicDate=true;
        else
             autometicDate=false;

          if($("#cacheAO").is(':checked'))
             cacheAO="true";
        else
             cacheAO="false";

        var Date=""
        if(document.getElementById("sysDate") != null && document.getElementById("sysDate").checked){
           Date="systemDate"
        }else if(document.getElementById("reportDate") != null && document.getElementById("reportDate").checked){
            Date="reportDate"
        }if(document.getElementById("currdetails") != null && document.getElementById("currdetails").checked){
           Date="currdetails"
        }else if(document.getElementById("yestrday") != null && document.getElementById("yestrday").checked){
            Date = "yestrday"
        }else if(document.getElementById("tomorow") != null && document.getElementById("tomorow").checked){
            Date = "tomorow"
        }else if(document.getElementById("newSysDate") != null && document.getElementById("newSysDate").checked){
            var sysSign = $("#sysSign").val();
            var newSysVal = $("#newSysVal").val();
            if(newSysVal==""){
                newSysVal="0";
            }
            Date = "newSysDate,".concat(sysSign,",", newSysVal)
            Date = Date.toString().replace(" ", "+", "gi");           //added by mohit for fix date
        }else if(document.getElementById("fixeddate") != null && document.getElementById("fixeddate").checked){
                  var fdatepicker=$("#fdatepicker").val();
                   Date = "fixeddate,".concat(fdatepicker);
        }else if(document.getElementById("globalDate") != null && document.getElementById("globalDate").checked){
            var globalSign = $("#globalSign").val();
            var newGlobVal = $("#newGlobVal").val();
            if(newGlobVal==""){
                newGlobVal="0";
            }
            Date = "globalDate,".concat(globalSign,",", newGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        if(document.getElementById("customDate") != null && document.getElementById("customDate").checked){
            var frmDate = "";
            var toDate = "";
            var cmpfrmDate = "";
            var cmptoDate = "";
        if(document.getElementById("fromyestrday") != null && document.getElementById("fromyestrday").checked){
//            var fromToSign = $("#fromToSign").val();
//            var fromToVal = $("#fromToVal").val();
//            //Date = "fromToDate,".concat(fromToSign,",", fromToVal)
//           // Date = Date.toString().replace(" ", "+", "gi");
           Date = "fromyestrday";
        }else if(document.getElementById("fromtomorow") != null && document.getElementById("fromtomorow").checked){
            Date="fromtomorow"
        }else if(document.getElementById("fromSysDate") != null && document.getElementById("fromSysDate").checked){
            var fromSysSign = $("#fromSysSign").val();
            var fromSysVal = $("#fromSysVal").val();
            if(fromSysVal=="")
               fromSysVal = "0";
            Date = "fromSysDate,".concat(fromSysSign,",", fromSysVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("fromfixeddate") != null && document.getElementById("fromfixeddate").checked){
                  var ffdatepicker=$("#ffdatepicker").val();
                     Date = "fromfixeddate,".concat(ffdatepicker);
        }else if(document.getElementById("fromglobalDate") != null && document.getElementById("fromglobalDate").checked){
            var fromglobalSign = $("#fromglobalSign").val();
            var fromGlobVal = $("#fromGlobVal").val();
            if(fromGlobVal=="")
               fromGlobVal = "0";
            Date = "fromglobalDate,".concat(fromglobalSign,",", fromGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        frmDate = Date;
        if(document.getElementById("toyestrday") != null && document.getElementById("toyestrday").checked){
           Date = frmDate+"@toyestrday";
        }else if(document.getElementById("totomorow") != null && document.getElementById("totomorow").checked){
           Date = frmDate+"@totomorow";
        }else if(document.getElementById("toSystDate") != null && document.getElementById("toSystDate").checked){
            var toSysSign = $("#toSysSign").val();
            var toSysVal = $("#toSysVal").val();
            if(toSysVal==""){
            toSysVal = "0"
            }
            Date = "toSystDate,".concat(toSysSign,",", toSysVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("tofixeddate") != null && document.getElementById("tofixeddate").checked){
                  var ftdatepicker=$("#ftdatepicker").val();
                     Date = frmDate+"@tofixeddate,".concat(ftdatepicker);
        }else if(document.getElementById("toglobalDdate") != null && document.getElementById("toglobalDdate").checked){
            var toglobalSign = $("#toglobalSign").val();
            var toGlobVal = $("#toGlobVal").val();
            if(toGlobVal=="")
               toGlobVal = "0";
            Date = "toglobalDdate,".concat(toglobalSign,",", toGlobVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        toDate = Date;
        if(document.getElementById("CmpFrmyestrday") != null && document.getElementById("CmpFrmyestrday").checked){
           Date = toDate+"@CmpFrmyestrday";
        }else if(document.getElementById("CmpFrmtomorow") != null && document.getElementById("CmpFrmtomorow").checked){
            Date = toDate+"@CmpFrmtomorow"
        }else if(document.getElementById("CmpFrmSysDate") != null && document.getElementById("CmpFrmSysDate").checked){
            var CmpFrmSysSign = $("#CmpFrmSysSign").val();
            var CmpFrmSysVal = $("#CmpFrmSysVal").val();
            if(CmpFrmSysVal==""){
                CmpFrmSysVal = "0";
            }
            Date = "CmpFrmSysDate,".concat(CmpFrmSysSign,",", CmpFrmSysVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("CmpFrmfixeddate") != null && document.getElementById("CmpFrmfixeddate").checked){
                  var fcfdatepicker=$("#fcfdatepicker").val();
                     Date = toDate+"@CmpFrmfixeddate,".concat(fcfdatepicker);
        }else if(document.getElementById("CmpFrmglobalDate") != null && document.getElementById("CmpFrmglobalDate").checked){
            var CmpFrmglobalSign = $("#CmpFrmglobalSign").val();
            var CmpFrmGlobVal = $("#CmpFrmGlobVal").val();
            if(CmpFrmGlobVal=="")
               CmpFrmGlobVal = "0";
            Date = "CmpFrmglobalDate,".concat(CmpFrmglobalSign,",", CmpFrmGlobVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmpfrmDate = Date;
        if(document.getElementById("cmptoyestrday") != null && document.getElementById("cmptoyestrday").checked){
           Date = cmpfrmDate+"@cmptoyestrday";
        }else if(document.getElementById("cmptotomorow") != null && document.getElementById("cmptotomorow").checked){
            Date = cmpfrmDate+"@cmptotomorow"
        }else if(document.getElementById("cmptoSysDate") != null && document.getElementById("cmptoSysDate").checked){
            var cmptoSysSign = $("#cmptoSysSign").val();
            var cmptoSysVal = $("#cmptoSysVal").val();
            if(cmptoSysVal==""){
                cmptoSysVal = "0";
            }
            Date = "cmptoSysDate,".concat(cmptoSysSign,",", cmptoSysVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("Cmptofixeddate") != null && document.getElementById("Cmptofixeddate").checked){
                  var fctdatepicker=$("#fctdatepicker").val();
                     Date = cmpfrmDate+"@Cmptofixeddate,".concat(fctdatepicker);
        }else if(document.getElementById("cmptoglobalDate") != null && document.getElementById("cmptoglobalDate").checked){
            var cmptoglobalSign = $("#cmptoglobalSign").val();
            var cmptoGlobVal = $("#cmptoGlobVal").val();
             if(cmptoGlobVal=="")
               cmptoGlobVal = "0";
            Date = "cmptoglobalDate,".concat(cmptoglobalSign,",", cmptoGlobVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmptoDate = Date;
        Date = cmptoDate;
    }
        if(flag=='overWrite'){
//      added by manik
//      document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&autometicDate="+autometicDate+"&overWriteExistingReport=true&Date="+Date+"&Gtregion="+Gtregion;
        document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&autometicDate="+autometicDate+"&overWriteExistingReport=true&Date="+Date+"&Gtregion="+Gtregion+"&REPORTID="+reportId+"&cacheAO="+cacheAO+"&istoggledate="+istoggledate;
        document.forms.frmParameter.method="POST";
        document.forms.frmParameter.submit();
        }
        else if(flag=='time'){
//            document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=saveTimeDetails&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&Date="+Date;
//            document.forms.frmParameter.method="POST";
            //document.forms.frmParameter.submit();
             $.post(
                  ctxPath+"/reportViewer.do?reportBy=saveTimeDetails&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&Date="+Date+"&reportId="+reportId+"&cacheAO="+cacheAO+"&istoggledate="+istoggledate,
             function(data){
                 if(data){
                     alert("TimeDetails Saved Successfully");
                 }

             });

        }

        $("#overWriteReport").dialog('close');
        }
//    }
}

function OverWriteDashboard(timeDetails,timeDetsMap,PbReportId,grpCnt,dbId,uId,DashIds,NewDashIds,ctxPath){
    var Date=""
        if(document.getElementById("sysDate") != null && document.getElementById("sysDate").checked){
           Date="systemDate"
        }else if(document.getElementById("reportDate") != null && document.getElementById("reportDate").checked){
            Date="reportDate"
        }if(document.getElementById("currdetails") != null && document.getElementById("currdetails").checked){
           Date="currdetails"
        }else if(document.getElementById("yestrday") != null && document.getElementById("yestrday").checked){
            Date = "yestrday"
        }else if(document.getElementById("tomorow") != null && document.getElementById("tomorow").checked){
            Date = "tomorow"
        }else if(document.getElementById("newSysDate") != null && document.getElementById("newSysDate").checked){
            var sysSign = $("#sysSign").val();
            var newSysVal = $("#newSysVal").val();
            Date = "newSysDate,".concat(sysSign,",", newSysVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("globalDate") != null && document.getElementById("globalDate").checked){
            var globalSign = $("#globalSign").val();
            var newGlobVal = $("#newGlobVal").val();
            Date = "globalDate,".concat(globalSign,",", newGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        if(document.getElementById("customDate") != null && document.getElementById("customDate").checked){
            var frmDate = "";
            var toDate = "";
            var cmpfrmDate = "";
            var cmptoDate = "";
        if(document.getElementById("fromyestrday") != null && document.getElementById("fromyestrday").checked){
//            var fromToSign = $("#fromToSign").val();
//            var fromToVal = $("#fromToVal").val();
//            //Date = "fromToDate,".concat(fromToSign,",", fromToVal)
//           // Date = Date.toString().replace(" ", "+", "gi");
           Date = "fromyestrday";
        }else if(document.getElementById("fromtomorow") != null && document.getElementById("fromtomorow").checked){
            Date="fromtomorow"
        }else if(document.getElementById("fromSysDate") != null && document.getElementById("fromSysDate").checked){
            var fromSysSign = $("#fromSysSign").val();
            var fromSysVal = $("#fromSysVal").val();
            Date = "fromSysDate,".concat(fromSysSign,",", fromSysVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("fromglobalDate") != null && document.getElementById("fromglobalDate").checked){
            var fromglobalSign = $("#fromglobalSign").val();
            var fromGlobVal = $("#fromGlobVal").val();
            Date = "fromglobalDate,".concat(fromglobalSign,",", fromGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        frmDate = Date;
        if(document.getElementById("toyestrday") != null && document.getElementById("toyestrday").checked){
           Date = frmDate+"@toyestrday";
        }else if(document.getElementById("totomorow") != null && document.getElementById("totomorow").checked){
           Date = frmDate+"@totomorow";
        }else if(document.getElementById("toSystDate") != null && document.getElementById("toSystDate").checked){
            var toSysSign = $("#toSysSign").val();
            var toSysVal = $("#toSysVal").val();
            Date = "toSystDate,".concat(toSysSign,",", toSysVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("toglobalDdate") != null && document.getElementById("toglobalDdate").checked){
            var toglobalSign = $("#toglobalSign").val();
            var toGlobVal = $("#toGlobVal").val();
            Date = "toglobalDdate,".concat(toglobalSign,",", toGlobVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        toDate = Date;
        if(document.getElementById("CmpFrmyestrday") != null && document.getElementById("CmpFrmyestrday").checked){
           Date = toDate+"@CmpFrmyestrday";
        }else if(document.getElementById("CmpFrmtomorow") != null && document.getElementById("CmpFrmtomorow").checked){
            Date = toDate+"@CmpFrmtomorow"
        }else if(document.getElementById("CmpFrmSysDate") != null && document.getElementById("CmpFrmSysDate").checked){
            var CmpFrmSysSign = $("#CmpFrmSysSign").val();
            var CmpFrmSysVal = $("#CmpFrmSysVal").val();
            Date = "CmpFrmSysDate,".concat(CmpFrmSysSign,",", CmpFrmSysVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("CmpFrmglobalDate") != null && document.getElementById("CmpFrmglobalDate").checked){
            var CmpFrmglobalSign = $("#CmpFrmglobalSign").val();
            var CmpFrmGlobVal = $("#CmpFrmGlobVal").val();
            Date = "CmpFrmglobalDate,".concat(CmpFrmglobalSign,",", CmpFrmGlobVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmpfrmDate = Date;
        if(document.getElementById("cmptoyestrday") != null && document.getElementById("cmptoyestrday").checked){
           Date = cmpfrmDate+"@cmptoyestrday";
        }else if(document.getElementById("cmptotomorow") != null && document.getElementById("cmptotomorow").checked){
            Date = cmpfrmDate+"@cmptotomorow"
        }else if(document.getElementById("cmptoSysDate") != null && document.getElementById("cmptoSysDate").checked){
            var cmptoSysSign = $("#cmptoSysSign").val();
            var cmptoSysVal = $("#cmptoSysVal").val();
            Date = "cmptoSysDate,".concat(cmptoSysSign,",", cmptoSysVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("cmptoglobalDate") != null && document.getElementById("cmptoglobalDate").checked){
            var cmptoglobalSign = $("#cmptoglobalSign").val();
            var cmptoGlobVal = $("#cmptoGlobVal").val();
            Date = "cmptoglobalDate,".concat(cmptoglobalSign,",", cmptoGlobVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmptoDate = Date;
        Date = cmptoDate;
    }
    $.post(ctxPath+'/dashboardTemplateViewerAction.do?templateParam2=saveTimeDetails&timeDetails='+timeDetails+"&timeDetsMap="+timeDetsMap+"&PbReportId="+PbReportId+"&Date="+Date,
    function(data){
         alert("Time Details has been saved");
        window.location.href=window.location.href;
        if(data == 'true'){

       
//            document.forms.frmParameter.action=ctxPath+"/dashboardTemplateViewerAction.do?templateParam2=OverrideDashboard&divCnt="+grpCnt+'&dashboardId='+dbId+'&userId='+uId+'&dashletIds='+DashIds+'&NewDashIds='+NewDashIds;
//            document.forms.frmParameter.submit();
        }else{
//            document.forms.frmParameter.action=ctxPath+"/dashboardTemplateViewerAction.do?templateParam2=OverrideDashboard&divCnt="+grpCnt+'&dashboardId='+dbId+'&userId='+uId+'&dashletIds='+DashIds+'&NewDashIds='+NewDashIds;
//            document.forms.frmParameter.submit();
        }
    });
        $("#overWriteDashboard").dialog('close');
}
function cancelOverWriteDashboard(){
    $("#overWriteDashboard").dialog('close');
}


function cancelOverWriteReport(){
    $("#overWriteReport").dialog('close');
}

function bucketAnalysis(reportId,ctxPath){
    $("#dispbucketAnalysis").dialog('open');
    var frameObj=document.getElementById("dispbucketAnalysisFrame");
    var source =ctxPath+'/TableDisplay/checkBucketmeasure.jsp?reportId='+reportId;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;

}
function shwparamAssis(viewBY){
    $.ajax({
        url:'reportTemplateAction.do?templateParam=shwparamAssis&viewBY='+viewBY,
        success: function(data){
            document.getElementById("shwparamAssisDisplay").innerHTML = data;
        }
    });
    $("#shwparamAssis").data("title.dialog",viewBY+" - Filter ")
    $("#shwparamAssis").dialog('open');
}

function addsegSingleRow()
{

    var table = document.getElementById("segmentTable");

    var rowCount = table.rows.length;
    totalRowCount = rowCount;
    var row = table.insertRow(rowCount);
    var disrowCount=rowCount-1;
    //row.id = rowCount;
    var cell1 = row.insertCell(0);
    var element1 = document.createElement("input");
    element1.type = "text";
    element1.name = "segmentInput"+disrowCount;
    element1.id = "segmentInput"+disrowCount;
    cell1.appendChild(element1);
    var cell3 = row.insertCell(1);
    var element3 = document.createElement("input");
    element3.type = "text";
    element3.readOnly=true
    element3.name = "minInput"+disrowCount;
    element3.id = "minInput"+disrowCount;
    element3.style.textAlign="right";
    element3.setAttribute('onkeypress','return isNumberKey(event)')
    var valStmin=document.getElementById("maxInput"+(disrowCount-1)).value
    element3.value = Number(valStmin) +.0001
    cell3.appendChild(element3);

    var cell4 = row.insertCell(2);
    var element4 = document.createElement("input");
    element4.type = "text";
    element4.name = "maxInput"+disrowCount;
    element4.id = "maxInput"+disrowCount;
    element4.style.textAlign="right";
    element4.setAttribute('onkeypress','return isNumberKey(event)')
    element4.setAttribute('onblur','displayMinLimit(this)')
    cell4.appendChild(element4);

}
function deleteSegSingleRow()
{
    try{
        var table1 = document.getElementById('segmentTable');
        var table =  document.getElementById('segmentTable');
        var trowCount = table.rows.length-1;
        if(trowCount!=1){
            var conCheck=confirm("Are You Sure, You Want to Delete ?")
            if(conCheck){
                table1.deleteRow(trowCount);
            }
        }


    }
    catch(e){
        alert("e is "+e);
    }
}
function isNumberKey(evt)
{
    var charCode = (evt.which) ? evt.which : evt.keyCode
    if(charCode==44 || charCode==46 || charCode==45)
        return true;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}
function displayMinLimit(val){
    var varVal=val.value;
    varVal=Number(varVal);
    var disIdnumber=(val.id).replace("maxInput","")
    if(document.getElementById("minInput"+(parseInt(disIdnumber)+1))!=null){
        document.getElementById("minInput"+(parseInt(disIdnumber)+1)).value=''
        document.getElementById("minInput"+(parseInt(disIdnumber)+1)).value=varVal+.0001
    }
//inputStartlimt
}
function saveSegmentValues(ctxPath){
    var segName=$("#segmentId").val();
    var tableObj = document.getElementById("segmentTable")
    var tBodyobj=tableObj.getElementsByTagName("tbody")
    var trobj=tBodyobj[0].getElementsByTagName("tr")
    var segmentArray=new Array
    var highArray=new Array
    var lowArray=new Array
    for(var i=0;i<trobj.length;i++){
        segmentArray[i]=document.getElementById("segmentInput"+i).value
        highArray[i]= document.getElementById("maxInput"+i).value
        lowArray[i]=document.getElementById("minInput"+i).value

    }

    var reportId = $("#segmentReportId").val();
    var measureId = $("#segmentMeasureId").val();

    $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=createSegment&reportId='+reportId+'&measureId='+measureId+'&segmentNames='+segmentArray.toString()+'&upperLimit='+highArray.toString()+'&lowerLimit='+lowArray.toString()+'&segName='+segName,
        success:function(data) {
            $("#createSegmentDiv").dialog('close')
            var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId;
            var dSrc = document.getElementById("iframe1");
            dSrc.src = source;
        }
    });
}

function addMapMeasures(ctxPath,REPORTID)
{
             var sortType = $("#sortValuesForMap").val();
             var mapView = $("#ViewSelect").val();
             var geoView = $("#GeoViewForMap").val();
                $("#MapMeasures").dialog('open');
                $("#MapMeasures").html("");
                $('<iframe id="mapMeasureFrame" name="mapMeasureFrame"  style="width:100%;height:100%" frameborder="0" src='+ctxPath+'/TableDisplay/PbChangeMapColumnsRT.jsp?folderIds='+document.getElementById("roleid").value+'&REPORTID='+REPORTID+'&sortType='+sortType+'&mapView='+mapView+'&geoView='+geoView+'>').appendTo($("#MapMeasures"));

}

function saveMeasureGroup(reportID){
 var grpMesArray=new Array
 var grpMesIDsArray=new Array
 var grpName=$("#mesGrpName").val()
 if(trim(grpName)==""){
     alert("Please enter Group name")
 }else{
  $("#sortable li ").each(function()
   {
      var tempId=$(this).attr("id")

      grpMesIDsArray.push(tempId.replace("_li","", "gi"))
    })
   $("#sortable li table tbody tr").each(function()
   {
      var trObj=$(this)
      var tdObj=trObj[0].getElementsByTagName("td")
      var spanObj=tdObj[1].getElementsByTagName("span")

      grpMesArray.push(spanObj[0].innerHTML)
    })
    if(grpMesArray.length !=0){
         $.ajax({
       url:'reportViewer.do?reportBy=saveGroupMeasure&repID='+reportID+'&grpMesArray='+grpMesArray+'&grpName='+grpName+'&grpMesIDsArray='+grpMesIDsArray,
        success:function(data) {

        }
    });
     document.getElementById("iframe1").src="TableDisplay/pbDisplay.jsp?tabId="+reportID
     $("#groupMeasureDiv").dialog('close')
    }else{
        $("#groupMeasureDiv").dialog('close')
    }


 }

}
function openGroupMeasureList(contextPath,reportID)
{
    $("#groupMeasureDiv").dialog('open')
    $.ajax({
        url:'reportViewer.do?reportBy=openGroupMeasure&repID='+reportID,
        success:function(data){
            //isMemberUseInOtherLevel
          var jsonVar=eval('('+data+')')
                    $("#mesGrpName").val("")
                    $("#grpContentDiv").html("")
                    $("#grpContentDiv").html(jsonVar.htmlStr)
                     isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel

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
        }
    });



}
function editGrpMeasure(){
    var grpMesArray=new Array
   $("#sortable li table tbody tr").each(function()
   {
      var trObj=$(this)
      var tdObj=trObj[0].getElementsByTagName("td")
      var spanObj=tdObj[1].getElementsByTagName("span")

      grpMesArray.push(spanObj[0].innerHTML)
    })
//     alert("grpMesArray\t"+grpMesArray)
     var htmlVar=""
     for(var i=0;i<grpMesArray.length;i++){
       htmlVar+="<tr><td>"+grpMesArray[i]+"</td><td><input type='text' id=grpeditMes"+i+" value='"+grpMesArray[i]+"'></td>"
       htmlVar+="</tr>"

     }
     $("#editMeasureNameTbody").html(htmlVar)
     $("#openEditGrpMesNames").dialog('open')

}

function saveEditMesNames()
{
var liObj= $("#sortable li")
//alert("liObj\t"+liObj.length)
var i=0;
$("#sortable li table tbody tr").each(function()
   {
      var trObj=$(this)
      var tdObj=trObj[0].getElementsByTagName("td")
      var spanObj=tdObj[1].getElementsByTagName("span")
      spanObj[0].innerHTML=$("#grpeditMes"+i).val()
     i++;

    })

    $("#openEditGrpMesNames").dialog('close')
}



function editMeasure(elementId,formula,measureName,ctxPath,reportId,aggType,prePostVal){
        //var ctxPath='/pi';

        $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=getRefferedElementsofMeasure&reportId='+reportId+'&elementId='+elementId,
            success:function(data) {
                if(data!="empty"){
                    data = eval('('+data+')');
                    var refferedMeasureId = data.MeasureIds;
                    var refferedMeasureLabel = data.MeasureLabels;
                    parent.$("#custmemMeasDisp").attr('src',"about:blank");
                    parent.$("#custmemMeasureDispDia").dialog('open');
                    //                                alert("dialog is open");
                    parent.$("#custmemMeasDisp").attr('src', ctxPath+"/createCustMemberinviewer.jsp?reportId="+reportId+'&formula='+encodeURIComponent(formula)+'&measureName='+encodeURIComponent(measureName)+'&refferedMeasureIds='+refferedMeasureId.toString()+'&refferedMeasureLabels='+encodeURIComponent(refferedMeasureLabel.toString())+'&fromEdit=true&elementId='+elementId+'&aggType='+aggType+'&prePostVal='+prePostVal)
                }
                else{
                     parent.$("#custmemMeasDisp").attr('src',"about:blank");
                    parent.$("#custmemMeasureDispDia").dialog('open');
                    parent.document.getElementById("custmemMeasDisp").src=ctxPath+"/createCustMemberinviewer.jsp?fromData=no";
                }
            }
        });
    }

function getComparableReports(ctxPath,reportId)
{


 $.ajax({
        url:ctxPath+'/reportTemplateAction.do?templateParam=getComparableReports&reportId='+reportId,
        success:function(data)
        {

           var jsonVar=eval('('+data+')')
                   // $("#paramFilterMbrsForm").html("")
                  var html="<br><table align='center'><tr><td><input type='button' class='navtitle-hover' value='Next'onclick='getComparedReports("+reportId+")' style='width:100px'></td></tr></table>"
                    $("#comparableReportsDiv").html(jsonVar.htmlStr+html);


                    //isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                    $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
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
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
                     $("#comparableReportsDiv").dialog('open');
        }
    })



}

function getComparedReports(reportId)
{
     $("#comparableReportsDiv").dialog('close');
   var reports="";
    var viewBysUl=document.getElementById("sortable");
     if(viewBysUl!=undefined || viewBysUl!=null)
                {
                    var reportIds=viewBysUl.getElementsByTagName("li");
                     for(var i=0;i<reportIds.length;i++){
                            reports = reports+","+reportIds[i].id.replace("_li","");
                        }
                        if(reports!="")
                            reports=reports.substring(1);
                        var compareReportId=reports;
                         document.forms.reportForm.action = "reportTemplateAction.do?templateParam=getReportsToCompare1&firstRepId="+reportId+"&reports="+reports;
                                document.forms.reportForm.submit();

                }

}

//function saveHeadlines(ctxPath,reportId,userId)
//{
//
//
//var txtcompleteurl=document.getElementById("txtcompleteurl").value;
//var headline=$("#headlinename").val();
//var refresh=$("#refresh").val();
//$.post(ctxPath+'/dataSnapshot.do?ReportId='+reportId+'&userId='+userId+'&fromOption=headline&repcustname='+encodeURIComponent(headline)+'&UrlVal='+txtcompleteurl,
//function(data){
//        alert("Headline saved successfully");
//         $("#snapshotHeadlineDiv").dialog('close');
//});
//
//}

function saveHeadlines(ctxPath,reportId,userId,roleID)
{


var txtcompleteurl=document.getElementById("txtcompleteurl").value;
var headline=$("#headlinename").val();
var refresh=$("#refresh").val();
$.post(ctxPath+'/dataSnapshot.do?doAction=saveHeadline&ReportId='+reportId+'&fromOption=headline&headline='+encodeURIComponent(headline),
function(data){
    if(data!='null'){
    $("#snapshotHeadlineDiv").dialog('close');
        alert("Headline saved successfully");
        var headlineId=eval('('+data+')')
         //alert(headlineId)
         var confimMsg=confirm("Do you want to assign html reports to other Users ?")
                    if(confimMsg){
                     $("#assignheadlineReps").dialog('open');
                      var frameObj = document.getElementById("userAssignHeadline");
                        frameObj.src = "pbAssignHeadlines.jsp?roleId="+roleID+"&reportId="+reportId+"&userId="+userId+"&headlineId="+headlineId;
                   }
    }

});

}
function createDims(DimName,elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
    var parentDiv=parent.document.getElementById("AddMoreParamsDiv");
    var x=dimArray.toString();
    if(x.match(elmntId)==null){
        dimArray.push(DimName+"^"+elmntId)
        var childLI=document.createElement("li");
        childLI.id=DimName+"^"+elmntId;
        childLI.style.width='auto';
        childLI.style.height='auto';
        childLI.style.color='white';
        childLI.className='navtitle-hover';
        var table=document.createElement("table");
        table.id=DimName+i;
        var row=table.insertRow(0);
        var cell1=row.insertCell(0);
        // cell1.style.backgroundColor="#e6e6e6";
        var a=document.createElement("a");
        a.href="javascript:deleteDim('"+DimName+'^'+elmntId+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        var cell2=row.insertCell(1);
        cell2.style.color='black';
        cell2.innerHTML=DimName;
        childLI.appendChild(table);
        parentUL.appendChild(childLI);
        i++;
    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}
function createMeasures(elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
    var parentDiv=parent.document.getElementById("AddMoreParamsDiv");
    var x=dimArray.toString();
    if(x.match(elmntId)==null){
        dimArray.push("m_"+elmntId)
        var childLI=document.createElement("li");
        childLI.id="m_"+elmntId;
        childLI.style.width='auto';
        childLI.style.height='auto';
        childLI.style.color='white';
        childLI.className='navtitle-hover';
        var table=document.createElement("table");
        table.id=elmntId.split(":")[1]+i;
        var row=table.insertRow(0);
        var cell1=row.insertCell(0);
        // cell1.style.backgroundColor="#e6e6e6";
        var a=document.createElement("a");
        a.href="javascript:deleteDim('"+"m_"+elmntId+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        var cell2=row.insertCell(1);
        cell2.style.color='black';
        cell2.innerHTML=elmntId.split(":")[1];
        childLI.appendChild(table);
        parentUL.appendChild(childLI);
        i++;
    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}
function deleteDim(DimName){
    var LiObj=document.getElementById(DimName);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;

    var parentUL=document.getElementById("sortable");
    parentUL.removeChild(LiObj);
    var x=(LiObj.id);
    var i=0;
    for(i=0;i<dimArray.length;i++){
        if(dimArray[i]==x)
            dimArray.splice(i,1);
    }
}
//added by Dinanath for showing existing dim of AO
function showExistingDimForEditAO(aoId) {
    $.ajax({
        async: false,
        url: "reportViewerAction.do?reportBy=showExistingDimForEditAO&aoId=" + aoId + "&REPORTID=null",
        success: function (data) {
            if (data == "Failed") {
                alert("Something Went Wrong AO Object Couldn't be Added");
                $("#selectAO").dialog('close');
            } else
            {
                var jsonVar = eval('(' + data + ')');
                var viewbyNames = jsonVar.viewbyName;
                var viewbyIds = jsonVar.viewbyIds;
                var measure = jsonVar.measure;
                var measureIds = jsonVar.measureIds;
                if(viewbyIds.length>0)
                $("#dropimage").hide();
                if(measureIds.length>0)
                $("#dropimage1").hide();
                for (var i = 0; i < viewbyIds.length; i++) {
                    createDims1(viewbyNames[i], "elmnt-"+viewbyIds[i]);
                }
                for (var i = 0; i < measureIds.length; i++) {
                    createMeasures1(measure[i], measureIds[i]);
                }
            }
        }
    });
}
//Added by Ram 06Aug15
function createDims1(DimName,elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
    var parentDiv=parent.document.getElementById("AddMoreParamsDiv");
    var x=dimArray.toString();
    if(x.match(elmntId)==null){
        dimArray.push(DimName+"^"+elmntId)
        var childLI=document.createElement("li");
        childLI.id=DimName+"^"+elmntId;
        childLI.style.width='auto';
        childLI.style.height='auto';
        childLI.style.color='white';
        childLI.className='navtitle-hover';
        var table=document.createElement("table");
        table.id=DimName+i;
        var row=table.insertRow(0);
        var cell1=row.insertCell(0);
        // cell1.style.backgroundColor="#e6e6e6";
        var a=document.createElement("a");
        a.href="javascript:deleteDim1('"+DimName+'^'+elmntId+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        var cell2=row.insertCell(1);
        cell2.style.color='black';
        cell2.innerHTML=DimName;
        childLI.appendChild(table);
        parentUL.appendChild(childLI);
        i++;
    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}
//Added by Ram 06Aug15
function deleteDim1(DimName){
      var c = document.getElementById("sortable").childNodes;
    if(c.length==2){
    document.getElementById('dropimage').style.display='block';
    document.getElementById("demo").style.backgroundColor='#FFFFFF';
    }
    var LiObj=document.getElementById(DimName);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;

    var parentUL=document.getElementById("sortable");
    parentUL.removeChild(LiObj);
    var x=(LiObj.id);
    var i=0;
    for(i=0;i<dimArray.length;i++){
        if(dimArray[i]==x)
            dimArray.splice(i,1);
    }
}
 function dispDims(reportId){
//      alert("iam in pbReportviewerJS.js")
    var dims="";
    var dimName="";
    var dimUl=document.getElementById("sortable");
    var dimIds=dimUl.getElementsByTagName("li");
    var reportId = parent.document.getElementById("REPORTID").value;
     var ReportLayout = parent.document.getElementById("ReportLayout").value;
    for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
        dimName=dimName+","+dimIds[i].id;
    }
    if(dimIds.length!=0){
        dims=dims.substr(1);
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
    }
    else if(dimIds.length==0 && ReportLayout=='ViewBy'){
        parent.$("#AddMoreParamsDiv").dialog('close');
        alert("Please Drop atlest one Dimension");
        parent.$("#AddMoreParamsDiv").dialog('open');
    }

//var ctxPath=document.getElementById("h").value;
$.ajax({
        url:'reportTemplateAction.do?templateParam=saveNewDimensions&ReportId='+reportId+'&dimIds='+dims+'&dimName='+dimName+'&ReportLayout='+ReportLayout,
        success:function(data)
        {
             if(parent.fromReport==true){
          alert("Changes Will Be Reflected Once You Reset The Report")
        }
//          alert("Changes Will Be Reflected Once You Reset The Report")
        }
});
//   $.post('/reportTemplateAction.do?templateParam=saveNewDimensions&ReportId='+reportId+'&dimIds='+dims+'&dimName='+dimName,
//function(data){
//
//       });

//    alert("dims"+dims)
//    alert("dimName"+dimName)
//    alert("reportId"+reportId)
      parent.cancelDim();
 }
function saveDynamicHeadlines(ctxtPath,reportId,userId,roleID){
         $("#DynamicHeadlineDiv").dialog("close");
         var url= parent.document.getElementById("txtcompleteurl").value;
         var headlineName=$("#dynamicheadlinename").val();
         $.post(ctxtPath+"/reportViewer.do?reportBy=saveDynamicHeadline&reportId="+reportId+'&url='+encodeURIComponent(url)+'&headlineName='+encodeURIComponent(headlineName),
                                function(data){
                                //code written by swati
                                if(data!='null'){
                    alert("Dynamic Headline Saved Sucessfully");
                    var headlineId=eval('('+data+')')
                    var confimMsg=confirm("Do you want to assign html reports to other Users ?")
                    if(confimMsg){
                        $("#assignheadlineReps").dialog('open');
                        var frameObj = document.getElementById("userAssignHeadline");
                        frameObj.src = "pbAssignHeadlines.jsp?roleId="+roleID+"&reportId="+reportId+"&userId="+userId+"&headlineId="+headlineId;
                               }
                            }

                                });
}
function AddInnerViewbys(ctxtPath){
   $.post(ctxtPath+"/reportViewer.do?reportBy=getReportParameters&REPORTID="+document.getElementById("REPORTID").value,
    function(data){
            if(data!='null'){
                var jsonVar=eval('('+data+')')
                var paramIds=jsonVar.parameters;
                var paramNames=jsonVar.parameterNames;
                var htmlVar;
                htmlVar="<table><tr><td>Add Additional Viewby</td><td><select id='elementId' name='elementId'>";
                for(var i=0;i<paramIds.length;i++){
                    htmlVar+="<option value="+paramIds[i]+">"+paramNames[i]+"</option>";
                }
                htmlVar+="</select></td></tr><br/>";
                htmlVar+="<tr><td colspan='2' align='center'><input type='button' value='save' class='navtitle-hover' style='width:auto;height:20px;color:black' onclick=saveInnerViewbys('"+ctxtPath+"') ></center></td></tr></table>";
                $("#AddInnerViewbysDiv").html(htmlVar);
                $("#AddInnerViewbysDiv").dialog('open');
            }

      });
}
 function saveInnerViewbys(ctxtPath){
    var eId=$("#elementId").val();
    $("#AddInnerViewbysDiv").dialog('close');
    $.post(ctxtPath+"/reportViewer.do?reportBy=saveInnerViewbys&REPORTID="+document.getElementById("REPORTID").value+"&eId="+eId,
    function(data){
        alert("Inner viewby saved successfully")

    });
 }
    function saveParamSection(ctxPath) {
//          $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
       var confirmDel=confirm("Do you want to Save the Paramter changes Permanently");
                    if(confirmDel==true){

        $.post(ctxPath+"/reportViewer.do?reportBy=saveParameterSection&REPORTID="+document.forms.frmParameter.REPORTID.value,
                function(data){
                alert("Parameter Region is saved")
      document.forms.frmParameter.action =ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+document.forms.frmParameter.REPORTID.value+"&isRepChanges=Y&action=reset";//added by Dinanath for isRepChanges
      document.forms.frmParameter.submit();
                        });
  }
//           }else{
//              alert("You do not have the sufficient previlages")
//  }
//       }});

  }
    function submit(url){
               parent.submiturls1(url)
           }
           function zoomgraph(reportId,graphtypename,selectedgraphtype1,graphTypeid,graphIds,count,ctxpath){
    $('#dispGrpProp').data('title.dialog', "");
    $("#zoomgraph").dialog('open');
    var frameObj=document.getElementById("zoomgraphFrame");
    var source =ctxpath+'/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID='+reportId+'&grptypid='+graphtypename+'&selectedgraph='+selectedgraphtype1+'&gid='+graphTypeid+'&grpidfrmrep='+graphIds+'&graphCount='+count+'&screenHeight='+screenHeight+'&graphChange=default';
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="'+screenHeight+'px"  style="position:absolute" ></center>';
    frameObj.src=source;
}

function submitval(ctxPath)
{
    var paramNames=new Array();
    var paramIds=new Array();
    var reportId =document.getElementById("REPORTID").value;
    var ul = document.getElementById("removeDimValues");
    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length>0){
                        for(var i=0;i<colIds.length;i++){
                            var idNames = colIds[i].id.split("@")
                            paramNames.push(idNames[0]);
                            paramIds.push(idNames[1]);
                        }
                    }
                    var confirmDel=confirm("Do you want to Remove ViewBy Paramters Permanently");
                    if(confirmDel==true){
//                       alert(ctxPath+'/reportTemplateAction.do?templateParam=removeDimensions&ReportId='+reportId+'&dimIds='+paramIds+'&dimName='+paramNames);

                  $.post(ctxPath+'/reportTemplateAction.do?templateParam=removeDimensions&ReportId='+reportId+'&dimIds='+paramIds+'&dimName='+paramNames,
                        function(data){
                            $("#removeMoreParamsDiv").dialog('close')
                             alert("Changes Will Be Reflected Once You Save/Overwrite Report")
                window.location.href = ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID=' + reportId + '&isRepChanges=Y&action=open';//added by Dinanath for isRepChanges
                        });
//                          $.ajax({
//                        url:'reportTemplateAction.do?templateParam=removeDimensions&ReportId='+reportId+'&dimIds='+paramIds+'&dimName='+paramNames,
//                        success:function(data)
//                        {
//                         $("#removeMoreParamsDiv").dialog('close')
//                             window.location.href = window.location.href;
//                       }
                   }
                    if(paramIds!=''){
                    }
                    else{
                      alert('No viewBys')
                    }



    }


}

 function RemoveMoreDims (ctxPath) {
//    alert(parent.reportId)
//   alert('RemoveMoreDims');
// parent.$("#RemoveMoreParamsDiv").dialog({
//                     autoOpen: false,
//                     height: 550,
//                     width: 550,
//                     position: 'justify',
//                     modal: true,
//                     resizable:true
//                });
    parent.$("#removeMoreParamsDiv").dialog('option','title','Remove Dimensions');
//    parent.$("#AddMoreParamsDiv").dialog('option','height','550px');
//       parent.$("#AddMoreParamsDiv").dialog('option','width','300px');
     parent.$("#removeMoreParamsDiv").dialog('open');
      $.post(ctxPath+'/reportViewer.do?reportBy=getAllParams&REPORTID='+document.getElementById("REPORTID").value+'&ctxPath='+ctxPath,
         function(data){


//             alert(data);
//             var jsonValue = eval('('+data+')')
//             var html = '';
//             html+="<ul id='rowViewUL' class='sortable'>"
//                                          <%=rowViewByStr%>
//             html+="<table><tr><td><select id='idvaluesId' name='idvaluesId'>"
//             for(var i=0;i<jsonValue.ParameterNames.length;i++){
//                 html+="<option value='"+jsonValue.ParameterIds[i]+"'>"+jsonValue.ParameterNames[i]+"</option>";
//             }
//             html+="</select></td></tr><tr><td><input type='button' name='' id='buttonId' value='Done' onclick=\"removeParams()\"></td></tr></table>";
//             html+="</ul>"
             parent.$("#removeMoreParamsDiv").html(data);

         });

 }
  function deleteColumnParameters(viwById,viewByname){
                var LiObj=document.getElementById(viwById);

                var parentUL=document.getElementById("removeDimValues");

                parentUL.removeChild(LiObj);
            }

 function sequenceParams(ctxPath){
//    parent.$("#sequnceParamsDiv").dialog('option','title','Sequnce Dimensions');
     $("#sequnceParamsDiv").dialog('open');
      $.post(ctxPath+'/reportViewer.do?reportBy=getSequnceParams&REPORTID='+parent.reportId+'&ctxPath='+ctxPath,
         function(data){

             parent.$("#sequnceParamsDiv").html(data);
                parent.$("#sortable2").sortable();
                parent.$("#sortable2").disableSelection();

         });
 }
 function submitval1(ctxPath)
{
    var paramNames=new Array();
    var paramIds=new Array();
    var reportId = parent.reportId;
    var ul = document.getElementById("sortable2");
    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length>0){
                        for(var i=0;i<colIds.length;i++){
                            var idNames = colIds[i].id.split("@")
                            paramNames.push(idNames[0]);
                            paramIds.push(idNames[1]);
                        }
                    }
                    var confirmDel=confirm("Do you want to change Paramter Sequence Permanently");
                    if(confirmDel==true){
                  $.post(ctxPath+'/reportTemplateAction.do?templateParam=sequenceDimensions&ReportId='+reportId+'&dimIds='+paramIds+'&dimName='+paramNames,
                        function(data){
                         {
                            $("#sequnceParamsDiv").dialog('close')
                             alert("Changes Will Be Reflected Once You Save/Overwrite Report")
                             document.forms.frmParameter.action =ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&isRepChanges=Y&action=reset";//added by Dinanath for isRepChanges

                        }
                    });
                   }
                    if(paramIds!=''){
                    }
                    else{
                      alert('No viewBys')
                    }
    }
}

 function dispDesignerDims(reportId){
//      alert("iam in pbReportviewerJS.js")
    var dims="";
    var dimName="";
    var dimUl=document.getElementById("sortable");
    var dimIds=dimUl.getElementsByTagName("li");
    var reportId = parent.document.getElementById("REPORTID").value;
    var roleId=parent.document.getElementById("roleid").value;
     var ReportLayout = parent.document.getElementById("ReportLayout").value;          //added by mohit for kpi and none
    for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
        dimName=dimName+","+dimIds[i].id.toString().substr(0, dimIds[i].id.toString().lastIndexOf("^"));
    }
    if(dimIds.length!=0){
        dims=dims.substr(1).replace("elmnt-", "","gi");
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
    }
  var timeDetail = parent.document.getElementById("time").value
    //var timeDetail = parent.document.getElementsByName("time");
//           var selectdDetails = "";
            var timeDim="";
//           if(timeDetail.length!=0){
//                    for(var i=0;i<timeDetail.length;i++){
//                        if(timeDetail[i].checked){
//                            selectdDetails=selectdDetails+","+timeDetail[i].id;
//                        }
//                    }
//                }
//                if (selectdDetails!=""){
//                    selectdDetails=selectdDetails.substr(1,selectdDetails.length);
//                }
                if(timeDetail == "StandardTime"){
                    timeDetail = "Time-Period Basis";
                     timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                    var timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                } else if(timeDetail == "RangeBasis"){
                    timeDetail = "Time-Range Basis";
                    timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
                else{
                     timeDetail = "";
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
//    alert("dims"+dims)
//var ctxPath=document.getElementById("h").value;
$.ajax({
        url:'reportTemplateAction.do?templateParam=buildParams&REPORTID='+reportId+'&params='+dims+'&paramNames='+dimName+'&foldersIds='+roleId+'&dimId='+timeDetail+'&timeparams='+timeDim+'&dispDesignerDims='+"dispDesignerDims"+'&ReportLayout='+ReportLayout,
        success:function(data)
        {
            var REPORTID = parent.document.getElementById("REPORTID").value;
            var ctxPath=parent.document.getElementById("h").value;
            var frameObj = parent.document.getElementById("editViewByFrame");
            var designer = parent.document.getElementById("Designer").value;
            $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath+'&fromdesigner='+designer,
                function(data){
                    if(data=='NoViewBys' && ReportLayout=='ViewBy'){
                        alert("Please Select Paramters From Plus Icon")
                    }else{
                        //parent.$("#editViewByDiv").dialog('open'); // comment by mohit for create report
                        var source = ctxPath+"/Report/Viewer/ChangeViewBy.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
                        frameObj.src = source;

        }
});
        }
});

      parent.cancelDim();
 }
 function dispInsightDesiignerDims(reportId){
     var dims="";
    var dimName="";
    var dimUl=document.getElementById("sortable");
    var dimIds=dimUl.getElementsByTagName("li");
    var reportId = parent.document.getElementById("REPORTID").value;
    var roleId=parent.document.getElementById("roleid").value;
    for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
        dimName=dimName+","+dimIds[i].id.toString().substr(0, dimIds[i].id.toString().lastIndexOf("^"));
    }
    if(dimIds.length!=0){
        dims=dims.substr(1).replace("elmnt-", "","gi");
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
    }
  var timeDetail = parent.document.getElementById("time").value
  if(timeDetail == "StandardTime"){
                    timeDetail = "Time-Period Basis";
                     timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                    var timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                } else if(timeDetail == "RangeBasis"){
                    timeDetail = "Time-Range Basis";
                    timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
                else{
                     timeDetail = "";
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
                $.ajax({
        url:'reportTemplateAction.do?templateParam=buildParams&REPORTID='+reportId+'&params='+dims+'&paramNames='+dimName+'&foldersIds='+roleId+'&dimId='+timeDetail+'&timeparams='+timeDim+'&dispDesignerDims='+"dispDesignerDims"+'&fromDesigner=insightDesigner',
        success:function(data)
        {
            //alert("parameters")
            parent.cancelDim();
            parent.$("#measuresDialog").dialog('open');
            var frameObj=parent.document.getElementById("dataDispmem");
            var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleId+'&REPORTID='+parent.document.getElementById("REPORTID").value;
            frameObj.src=source;
        }});
 }
 function closeDiv(){
     parent.$("#paramDesign").hide();
 }
 function designerViewBy(){
    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("editViewByFrame");
    var designer = parent.document.getElementById("Designer").value;
     $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath+'&fromdesigner='+designer, $("#ViewByForm").serialize() ,
         function(data){
             if(data=='NoViewBys'){
                 alert("Please Select Paramters From Plus Icon")
             }else{
                  $("#editViewByDiv").dialog('open');
            var source = ctxPath+"/Report/Viewer/ChangeViewBy.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
            frameObj.src = source;
             }
      });

        }

   function displayGrandTotal(ctxPath,reportId){
              $.ajax({
               url:ctxPath+'/reportViewer.do?reportBy=getSubtotal&reportId='+reportId,
               success:function(data){
                parent.$("#grandTotalDiv").html(data);

               }
          });
    parent.$("#grandTotalDiv").toggle();
}
function dispInsightDims(reportId){
     var dims="";
    var dimName="";
    var dimUl=document.getElementById("sortable");
    var dimIds=dimUl.getElementsByTagName("li");
    var reportId = parent.document.getElementById("REPORTID").value;
    var roleId=parent.document.getElementById("roleid").value;
    for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
        dimName=dimName+","+dimIds[i].id.toString().substr(0, dimIds[i].id.toString().lastIndexOf("^"));
    }
    if(dimIds.length!=0){
        dims=dims.substr(1).replace("elmnt-", "","gi");
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
    }
    var ctxPath=parent.document.getElementById("h").value;
    $.ajax({
        url:'reportTemplateAction.do?templateParam=changeParameters&REPORTID='+reportId+'&params='+dims+'&paramNames='+dimName+'&foldersIds='+roleId,
        success:function(data)
        {
            parent.$("#AddMoreParamsDiv").dialog('close');
            parent.$("#DisplayInsightdiv").html('<center><img id="imgId" src="'+ctxPath+'/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>');
              $.post(ctxPath+'/reportTemplateAction.do?templateParam=generateInsightTable&action1=InsightTimeChange&reportId='+reportId,parent.$("#InsightForm").serialize(),
              function(data){
                   parent.$("#DisplayInsightdiv").html(data);
                   parent.initCollapser("");
                        });

        }});
}
 function lockDataset(){
    // alert("hi")
     parent.$("#lockDatasetDivid").toggle();
//     parent.$("#lockDatasetDiv").show("slide",{direction:'left'},"slow");
 }
 function closeLockdatasetDiv(reportId,ctxpath){
//     alert()lockdataform
//    alert(parent.$("#lockdataset1").is(":checked"));
    var lockdataset1 = parent.$("#lockdataset1").is(":checked");
     parent.$("#lockDatasetDivid").hide();
      $.post(ctxpath+'/reportViewer.do?reportBy=lockDataset&reportId='+reportId+"&lockdataset1="+lockdataset1,
      function(data){

      });
 }
 function averageValue(){
        var GTAverageObj=document.getElementById("GTAverage1");
                if(GTAverageObj.checked){
                    GTAverageObj.value="true"
                }else{
                    GTAverageObj.value="false";
                }
                document.getElementById("GTAverage").value=GTAverageObj.value;
 }
 function quickRefresh(ctxPath,reportId){
    var refreshEnable=false;
    var res=confirm("This will store the report data in a local folder. Do you wish to continue");
    if(res){
      refreshEnable=true;
      $("#refreshweekday").hide();
      $("#refreshmonthday").hide();
      $("#refreshhourly").hide();
      $("select#refreshFrequency").attr("value","Daily");
      $("#quickRefreshDiv").dialog('open');
    }

}
function saveQuickRefresh(ctxPath,reportId){
    var refreshEnable=true;
    $("#quickRefreshDiv").dialog('close');
    parent.$("#loading").show();
   $.post(ctxPath+'/reportViewer.do?reportBy=quickRefresh&reportId='+reportId+"&refreshEnable="+refreshEnable,$("#refreshForm").serialize(),
      function(data){
          if(data=='true'){
          parent.$("#loading").hide();
          alert("Quick Refresh Option Enabled");
          document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&action=open";
          document.forms.frmParameter.submit();
         }
      });
}
function disableQuickRefresh(ctxPath,reportId){
    var res=confirm("This will Remove the report data in a local folder. Do you wish to continue");
    if(res){
        $("#quickRefreshDiv").dialog('close');
      $.post(ctxPath+'/reportViewer.do?reportBy=disableQuickRefresh&reportId='+reportId,
      function(data){
          alert("Quick Refresh Option Disabled");
          document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&action=open";
          document.forms.frmParameter.submit();
      });
    }
}
function checkRefreshSelection(id){
    if($("#"+id).val()=="false")
        $("#autoRefreshTr").hide();
    else
        $("#autoRefreshTr").show();
}
function saveExcelFileDetails(ctxPath,reportId){
//    alert("saveexcel");
     parent.$("#mappingExcelFileDiv").dialog('close');
     alert("Excel File Details are Mapped with Report Rowviewbys")
                $.post(ctxPath+'/reportTemplateAction.do?templateParam=readExcelFileDetails&reportid='+reportId, this.$("#ExcelFileForm").serialize() ,
                 function(data){
                     var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+reportId;
                    parent.submiturls1(path);
                 });
            }
 //Added by Amar
 function applyNewFilters(reportId,reportName){

     var frameObj=document.getElementById("addmoreFiltersFrame");
     try{
     var source="reportTemplateAction.do?templateParam=addMoreFilters&REPORTID="+reportId;
     frameObj.src=source;}catch(e){
     }

     $("#AddMoreFiltersDiv").dialog({
         resizable: false,
         autoOpen: false,
         height: 600,
         width: 850,
         modal: true,
         title:"Apply Intermediate Filters [ "+reportName+" ]"
})
     $("#AddMoreFiltersDiv").dialog('open');

//     $("#AddMoreFiltersDiv .ui-dialog-title").text('My New Title');
//);
 }

  function getXtendUI(reportName,dir){
//        window.open("http://localhost:8078/xtend/user?report="+reportName+"&dir="+dir,"_blank");
        window.open("http://183.82.3.61:8088/xtend_5.6.2/user?report="+reportName+"&dir="+dir,"_blank");
   location.reload();
        }
// End of Code
//function quickRefresh(ctxPath,reportId){
//    var refreshEnable=false;
//     var res=confirm("This will store the report data in a local folder. Do you wish to continue");
//    if(res){
//      refreshEnable=true;
// // alert(refreshEnable)
//   parent.$("#loading").show();
//   $.post(ctxPath+'/reportViewer.do?reportBy=quickRefresh&reportId='+reportId+"&refreshEnable="+refreshEnable,
//      function(data){
//          if(data=='true'){
//          parent.$("#loading").hide();
//          alert("Quick Refresh Option Enabled");
//          document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&action=open";
//          document.forms.frmParameter.submit();
//         }
//      });
//    }
//
//}
//added by Dinanath
function saveReportTagName(){
    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("saveReportByFrame");
     $.post(ctxPath+'/reportViewer.do?reportBy=showViewByTag&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("ViewByForm").serialize() ,
         function(data){
//             alert("post.........report...")
            var source = ctxPath+"/SaveReportTagDesc.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
            //var source = ctxPath+"/TableDisplay/pbDisplay.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
            frameObj.src = source;
      });
     $("#saveRTD").dialog('open');
 }
//ended by Dinanath

// added by mayank for reset Graphs
 function resetGraphs()
       {
          
       

 $("#setGORefresh").dialog('open');

        }

  function saveResetGraph(reportId1){
// alert("hello");
 var graphRefresh = false;
 var graphGO = false;
 
  if($("#resetGO").is(":checked") && $("#refreshGraph").is(":checked")){
      
      graphGO = true;
      graphRefresh = true;
  }else if($("#resetGO").is(":checked")) {
      graphGO = true;
      graphRefresh=false;
  }else {
     graphGO = false;
      graphRefresh=false;
  }
 
 $.ajax({
            url: 'reportTemplateAction.do?templateParam=saveResetGraph&reportId='+encodeURIComponent(reportId1)+"&graphGO="+graphGO+"&graphRefresh="+graphRefresh,
            success: function(data){
            if(data=="0")
                {
                     alert("Something Went Wrong Graph Not Created")
                }
                else
                    {
                        alert("Graph Refreshed")
                         $("#setGORefresh").dialog('close');
                    }

            }

        });

 }

 //add by krishan

//function dispDesignerDims2(reportId){
////    alert("***");
////alert("@@@:dispdes");
//    var dims="";
//    var dimName="";
//    var dimUl=document.getElementById("sortable");
//    if(dimUl==null){
//        alert("Please Select the Dimension!")
//    }
//    //alert(dimUl);
//    var dimIds=dimUl.getElementsByTagName("li");
//
////    alert(JSON.stringify(dimIds));
//    var reportId = parent.document.getElementById("REPORTID").value;
//    var roleId=parent.document.getElementById("roleid").value;
//      var ReportLayout = parent.document.getElementById("ReportLayout").value;
//   // alert(ReportLayout);
//
//    for(var i=0;i<dimIds.length;i++){
//        var dkpiIds=(dimIds[i].id).split("^");
//        dims=dims+","+dkpiIds[1];
////        alert(dims);
//        dimName=dimName+","+dimIds[i].id.toString().substr(0, dimIds[i].id.toString().lastIndexOf("^"));
//    }
//    if(dimIds.length!=0){
//        dims=dims.substr(1).replace("elmnt-", "","gi");
//        dimName=dimName.substr(1);
//        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
//    }
//  var timeDetail = parent.document.getElementById("time").value
//    //var timeDetail = parent.document.getElementsByName("time");
////           var selectdDetails = "";
//            var timeDim="";
////           if(timeDetail.length!=0){
////                    for(var i=0;i<timeDetail.length;i++){
////                        if(timeDetail[i].checked){
////                            selectdDetails=selectdDetails+","+timeDetail[i].id;
////                        }
////                    }
////                }
////                if (selectdDetails!=""){
////                    selectdDetails=selectdDetails.substr(1,selectdDetails.length);
////                }
//                if(timeDetail == "StandardTime"){
//                    timeDetail = "Time-Period Basis";
//                     timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
//                    var timeDimension=timeDim.split(",");
//                    for(var i=0;i<timeDimension.length;i++){
//                        timeDimArray.push(timeDimension[i]);
//                    }
//                } else if(timeDetail == "RangeBasis"){
//                    timeDetail = "Time-Range Basis";
//                    timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
//                    timeDimension=timeDim.split(",");
//                    for(var i=0;i<timeDimension.length;i++){
//                        timeDimArray.push(timeDimension[i]);
//                    }
//                }
//                else{
//                     timeDetail = "";
//                    timeDimension=timeDim.split(",");
//                    for(var i=0;i<timeDimension.length;i++){
//                        timeDimArray.push(timeDimension[i]);
//                    }
//                }
////    alert("dims"+dims)
////var ctxPath=document.getElementById("h").value;S
////alert("@@@:first call");
//$.ajax({
//        async:false,
//        url:'reportTemplateAction.do?templateParam=buildParams&REPORTID='+reportId+'&params='+dims+'&paramNames='+dimName+'&foldersIds='+roleId+'&dimId='+timeDetail+'&timeparams='+timeDim+'&dispDesignerDims='+"dispDesignerDims"+'&ReportLayout='+ReportLayout,
//        success:function(data)
//        {
////            alert("DDDDDD : "+JSON.stringify(data));
//            var REPORTID = parent.document.getElementById("REPORTID").value;
//            var ctxPath=parent.document.getElementById("h").value;
//            var frameObj = parent.document.getElementById("editViewByFrame");
//            var designer = parent.document.getElementById("Designer").value;
////            alert("1.report id : "+REPORTID +"ctxpath : "+ctxPath+"designer : "+designer);
////            alert("@@@:second call");
//            $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath+'&fromdesigner='+designer,
//                function(data){
////                alert(JSON.stringify(data));
//
//                    if(data=='NoViewBys'){
//                        alert("Please Select Paramters From Plus Icon")
//                    }else{
////                        alert("@@@:Here!"+frameObj)
//                        //parent.$("#editViewByDiv").dialog('open'); // comment by mohit for create report
//                        var source = ctxPath+"/Report/Viewer/ChangeViewByAORep.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
//                        frameObj.src = source;
//
//        }
//});
//        }
//});
//
////      parent.cancelDim();
// }
 function dispDims2(reportId){
//      alert("%%%%%%%")
//    alert("@@@::::");
    var dims="";
    var dimName="";
    var dimUl=document.getElementById("sortable");
    var dimIds=dimUl.getElementsByTagName("li");
 var ReportLayout = parent.document.getElementById("ReportLayout").value;
    var reportId = parent.document.getElementById("REPORTID").value;
    for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
        dimName=dimName+","+dimIds[i].id;
    }
//    alert("@@@:dimID:"+dims+"\n"+dimName);
    if(dimIds.length!=0){
        dims=dims.substr(1);
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
    }
    else if(dimIds.length==0){
        parent.$("#AddMoreParamsDiv").dialog('close');
        alert("Please Drop atlest one Dimension");
       // parent.$("#AddMoreParamsDiv").dialog('open');
    }


//var ctxPath=document.getElementById("h").value;
//    alert("@@@:ajax call");
$.ajax({
        async:false,
        url:'reportTemplateAction.do?templateParam=saveNewDimensions&ReportId='+reportId+'&dimIds='+dims+'&dimName='+dimName+'&ReportLayout='+ReportLayout,
        success:function(data)
        {
             if(parent.fromReport==true){
          alert("Changes Will Be Reflected Once You Reset The Report")
        }
//          alert("Changes Will Be Reflected Once You Reset The Report")
        }
});
//   $.post('/reportTemplateAction.do?templateParam=saveNewDimensions&ReportId='+reportId+'&dimIds='+dims+'&dimName='+dimName,
//function(data){
//
//       });

//    alert("dims"+dims)
//    alert("dimName"+dimName)
//    alert("reportId"+reportId)
//      parent.cancelDim();
 }

  function savestandredtime(){
           var timeDetail = "Time-Period Basis";
           var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                $.ajax({
                    url: 'reportTemplateAction.do?templateParam=goToCreateReportTime&dimId='+timeDetail+'&timeparams='+timeDim,
                    success: function(data){             
                    }
                });
    
 }
 //added by sruthi for custom header
 function hideCustomHeader(){
     var numberformate=document.getElementById("NumberHeader");
    // alert(numberformate.checked)
     if(numberformate.checked){
 $("#CustomHeaderTd").show();
     }else
 $("#CustomHeaderTd").hide();
 }
 //ended by sruthi
//Added by Faiz Ansari
//function resizeTblCont(repType){
//    var x=document.getElementsByClassName("tblHdWd");
////    alert(x.length)
//    for(var i in x){                                        
//        var name=$(x[i]).attr("name");
//        if(name != "undefined"){                                                 
//                var ele=document.getElementsByClassName("N"+name);  
//                          
////            aler(name)
////            alert($(x[i]).width())
////            alert($(ele[0]).width())
////            alert($(ele[0]).attr("id"))
//            if($(x[i]).width() > $(ele[0]).width()){
//                $(ele[0]).children().width($(x[i]).width()+8);
//                $(x[i]).children().width($(x[i]).width()+4);
//            }
//            else if($(x[i]).width() < $(ele[0]).width()){
//                $(x[i]).children().width($(ele[0]).width()+4);
//                $(ele[0]).children().width($(ele[0]).width()+8);
//            }
//            else{}
//        }
//        if(i==(x.length-1)){                                            
//            $("#progenTable").css({
//                "transform":"translate(0px)"
//            })
//            $("#theaddiv").css({
//                "position":"absolute"
//            });
//            $("#theaddiv").next().css({
//                "margin-top":$("#progenTable thead").height(),
//                "position":"absolute",
//                "overflow":"auto",
//                "height":($(window).height()-($("#progenTable thead").height()+42))
//            });
//            
//            if($(window).width() > $("#theaddiv").width()){
////                alert($(window).width())
////                alert($("#theaddiv").width())
//                var addLen=(($(window).width()-$("#theaddiv").width())/(x.length-1))*0.93;
//                for(var i in x){                                        
//                    if(i==0){
//                        
//                    }
//                    else{
//                    var name=$(x[i]).attr("name");
//                    if(name != "undefined"){                                                 
//                        var ele=document.getElementsByClassName("N"+name);                                             
//                        if($(x[i]).width() > $(ele[0]).width()){
//                            $(ele[0]).children().width($(x[i]).width()+8+addLen);
//                            $(x[i]).children().width($(x[i]).width()+4+addLen);
//                        }
//                        else if($(x[i]).width() < $(ele[0]).width()){
//                            $(x[i]).children().width($(ele[0]).width()+4+addLen);
//                            $(ele[0]).children().width($(ele[0]).width()+8+addLen);
//                        }
//                        else{}
//                    }
//                }
//            }
//            }
//            for(var k in ele){
//                $(ele[k]).children().css("padding-right","10px");
//            }
//        }   
//
//    }
//    $("#theaddiv").next().width($("#theaddiv").next().width()+10);                                 
//                                
//}
//End!!!


 function OpenHideHeader()
       {

 $("#ShowHideHeader").dialog().dialog('open');

        }

  //added by krishan 
function saveShowHeader(radioValue,reportId1){
//var radioValue="";

//    if (document.getElementById("hideheader").checked){
//               radioValue= document.getElementById("hideheader").value;
//            } else {
//               radioValue= document.getElementById("showheader").value;
//            }
            if(radioValue=='')
    {
        alert("Error, ask your Administrator")
        return false;
    }
    else
        {

 $.ajax({
             url: 'reportTemplateAction.do?templateParam=saveShowHeader&reportId='+encodeURIComponent(reportId1)+"&radioValue="+radioValue,
            success: function(data){

            if(data=="0")
                {
                     alert("Oops Something Went wrong Your Hide Report Header couldtn't be Saved")
                }
                else
                    {
                        alert("Saved Successfully")
                         $("#ShowHideHeader").dialog('close');
                      //   location.reload();
                       document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+reportId1+"&action=open";
          document.forms.frmParameter.submit();
                    }
         

}
 });
        }
    }
    
    
/*Added by Ashutosh*/
function isDraggable(draggable,reportId1){
    if(draggable=='')
    {
        alert("Error, ask your Administrator")
        return false;
    }
    else
    {

        $.ajax({
            url: 'reportTemplateAction.do?templateParam=isDraggable&reportId='+encodeURIComponent(reportId1)+"&draggable="+draggable,
            success: function(data){

                if(data=="0")
                {
                    alert("Oops Something Went wrong Your Chart Draggability couldtn't be Saved")
                }
                else
                {
                    alert("Saved Successfully")
                    $("#ShowHideHeader").dialog('close');
                    //   location.reload();
                    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+reportId1+"&action=open";
                    document.forms.frmParameter.submit();
                }
         

            }
        });
    }
}
    
    
    //added by  sruthi for showfilters
    function ShowFilters(ctxpath,reportid,size,selectedfield){//alert("selecteddata"+selecteddata)
         $("#ShowFilters").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true
    });
    var html="";
    var list;
    if(size>6)
      list=6;
   else
     list=size;
    html=html+"<table id=showfilters ><tr><td>No of Filters </td><td></td><td></td><td>"
    html=html+"<select name=selectedShowfilters id=selectedShowfilters> "
    for(var no=0;no<=list;no++){
       //if(selecteddata==no)
         // html=html+"<option selected value="+no+">"+no+"</option>"
         //else
       html=html+"<option value="+no+">"+no+"</option>"
    }
    html=html+"</select></td><tr><td><input type='button' value='DONE' onclick=saveShowFilter('"+ctxpath+"',"+reportid+",'"+selectedfield+"') ></td><tr></table>"
    parent.$("#ShowFilters").html(html);
        parent.$("#ShowFilters").dialog('open');

    }
    function saveShowFilter(ctxpath,rportid,selectedfield){
        parent.$("#ShowFilters").dialog('close');
       var seleteddata=$("#selectedShowfilters").find(":selected").text();
        $.post(ctxpath+'/reportTemplateAction.do?templateParam=getShowFilters&rportid='+rportid+'&seleteddata='+seleteddata+'&selectedfield='+selectedfield,
                        function(data){
                            submitform();
                        });
    }
    //ended by sruthi
    
     //Added by Amar on dec 14, 2015
// function dispDesignerDimsAO(reportId){
////    alert("inside dispDesignerDimsAO");
////alert("@@@:dispdes");
//    var dims="";
//    var dimName="";
//    var dimUl=document.getElementById("sortable");
//    if(dimUl==null){
//        alert("Please Select the Dimension!")
//    }
//    //alert(dimUl);
//    var dimIds=dimUl.getElementsByTagName("li");
//
////    alert(JSON.stringify(dimIds));
//    var reportId = parent.document.getElementById("REPORTID").value;
//    var roleId=parent.document.getElementById("roleid").value;
////    alert("role : "+roleId);
//
//    for(var i=0;i<dimIds.length;i++){
//        var dkpiIds=(dimIds[i].id).split("^");
//        dims=dims+","+dkpiIds[1];
////        alert(dims);
//        dimName=dimName+","+dimIds[i].id.toString().substr(0, dimIds[i].id.toString().lastIndexOf("^"));
//    }
//    if(dimIds.length!=0){
//        dims=dims.substr(1).replace("elmnt-", "","gi");
//        dimName=dimName.substr(1);
//        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
//    }
//  var timeDetail = parent.document.getElementById("time").value
//            var timeDim="";
//                if(timeDetail == "StandardTime"){
//                    timeDetail = "Time-Period Basis";
//                     timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
//                    var timeDimension=timeDim.split(",");
//                    for(var i=0;i<timeDimension.length;i++){
//                        timeDimArray.push(timeDimension[i]);
//                    }
//                } else if(timeDetail == "RangeBasis"){
//                    timeDetail = "Time-Range Basis";
//                    timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
//                    timeDimension=timeDim.split(",");
//                    for(var i=0;i<timeDimension.length;i++){
//                        timeDimArray.push(timeDimension[i]);
//                    }
//                }
//                else{
//                     timeDetail = "";
//                    timeDimension=timeDim.split(",");
//                    for(var i=0;i<timeDimension.length;i++){
//                        timeDimArray.push(timeDimension[i]);
//                    }
//                }
//
//$.ajax({
//        async:false,
//        url:'reportTemplateAction.do?templateParam=buildParamsAO&REPORTID='+reportId+'&params='+dims+'&paramNames='+dimName+'&foldersIds='+roleId+'&dimId='+timeDetail+'&timeparams='+timeDim+'&dispDesignerDims='+"dispDesignerDims",
//        success:function(data)
//        {
//            var REPORTID = parent.document.getElementById("REPORTID").value;
//            var ctxPath=parent.document.getElementById("h").value;
//            var frameObj = parent.document.getElementById("editViewByFrame");
//            var designer = parent.document.getElementById("Designer").value;
//            $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath+'&fromdesigner='+designer,
//                function(data){
////                    alert("ViewBys"+ data);
//                    if(data=='NoViewBys'){
//                        alert("Please Select Paramters From Plus Icon")
//                    }else{
////                        alert("ChangeViewBy Source AO");
//                        var source = ctxPath+"/Report/Viewer/ChangeViewByAO.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
//                        frameObj.src = source;
//
//        }
//});
//        }
//});
// }
