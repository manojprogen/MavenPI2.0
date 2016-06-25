/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

            function createnewReport(){
                parent.createReport(false);
            }
                        function CreateReportNew(){
                $("#createReportNew").dialog('open');
            }
                        function CreateReportwithAO(){
                $.ajax({
        async:false,
       url: 'reportTemplateAction.do?templateParam=removeSessionAttributes',
        success: function(data){
        }
    });

               $("#createReportwithAO").dialog('open');
            }
             function showMetaData_RS(path){
              var RepSelectObj=document.getElementsByName("RepSelect");
               var repName=""
               var reportIds = new Array();
                for(var i=1;i<RepSelectObj.length;i++)
                {
                    if(RepSelectObj[i].checked )
                    {
                        reportIds.push(RepSelectObj[i].value);
                        repName=$("#"+$(RepSelectObj[i]).attr("id").replace("RepSelect","hyperRep")).html()


                    }
                }
                if(reportIds.length!=0){
                    if(reportIds.length>1)
                        alert("select only one report");
                    else{
                        $("#loadingmetadata").show();
                      $.ajax({
                      type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url:path+'/reportTemplateAction.do?templateParam=showMetaData&reportId='+reportIds.toString(),
                    success: function(data){
                         $("#loadingmetadata").hide();
                       // $("#viewTableDetails").remove();
                        //$("#header").remove();
                        $("#role").remove();
                        $("#dimensiondetails").remove();
                        $("#dimheader").remove();
                        $("#measuredetails").remove();
                        $("#measureheader").remove();

                        var html=parent.$("#reportMetaData").html();
                       parent.$("#reportMetaData").html(data+html)
                    }
                });
                   parent.$('#reportMetaData').data('title.dialog', 'Show Metadata -'+repName);
                   parent.$("#reportMetaData").dialog('open');
                    }
                }
                else
                    alert("please select a report");
            }
            function EditReportName(){
                var overWriteFlag = document.getElementById("overWriteFlag").value;
                if(overWriteFlag == "Yes"){
                var RepSelectObj=document.getElementsByName("RepSelect");
                var editrepids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        editrepids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(editrepids.length!=0){
                    if(editrepids.length<2){
                        var reportId=editrepids.pop();
                        var source = "pbEditRepName.jsp?ReportId="+reportId;
                            var conf = confirm("Changed Report Name will reflect for all other assigned users also. Would you like to continue?")
                            if(conf == true)
                        parent.editRepName(source);
                    }else{
                        alert("Please select only one Report")
                    }
                }else{
                    alert("Please select Report")
                }
                }else{
                    alert("You cannot edit this Report Name as Overwrite Report Privilege is not Assigned")
            }
            }

            function cancelReport(){
                parent. closeReport();
            }
            function tabmsg1(){
            if(document.getElementById('newreportName').value==''){
                document.getElementById('reportDesc').value = document.getElementById('reportName').value;
            }
            else{
               document.getElementById('newreportDesc').value = document.getElementById('newreportName').value;
              }
            }

            function saveReport(){
                var reportName = document.getElementById('reportName').value;
                //reportName=reportName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var reportDesc = document.getElementById('reportDesc').value;
                //reportDesc=reportDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                reportName=encodeURIComponent(reportName);
                reportDesc=encodeURIComponent(reportDesc);

                if(reportName==''){
                    alert("Please enter Report Name");
                }
                else  if(reportDesc==''){
                    alert("Please enter Report Description")
                }
                else{
                                document.forms.reportForm.action = "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc;
                                document.forms.reportForm.method="POST";
                                document.forms.reportForm.submit();
             }
         }
         function viewReportinStudio(path){
             document.forms.reportForm.action=path;
             document.forms.reportForm.submit();
         }

         function viewReportinStudio2(path){
             document.forms.dashboardForm.action=path;
             document.forms.dashboardForm.submit();
         }

         function userFolderAssignment(){
             var frameObj=document.getElementById("userFolderAssignmentDisp");
             var source = "userFolderAssignment.jsp";
             frameObj.src=source;
             document.getElementById('userFolderassign').style.display='none';
             frameObj.style.display='block';
             document.getElementById('fade').style.display='block';

         }
         function cancelUsersFolders(){
             document.getElementById("userFolderAssignmentDisp").style.display='none';
             document.getElementById('fade').style.display='';
             document.getElementById('userFolderassign').style.display='';
             cancelUsersFolders();
         }
        function copyReport()
            {
                var RepSelectObj=document.getElementsByName("RepSelect");

                var reportIds = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        reportIds.push(RepSelectObj[i].value);
            }
                    else{
                        RepSelectObj[i].checked=false;
            }
                }
                if(reportIds.length!=0)
                {
                    if(reportIds.length<2){
                         parent.createReport(true);
                    }
                    else
                      alert("Please select only one Report")
                }
                else{
                    alert("Please select Report(s)")
                }
            }
            function saveCopyReport(path){
                var RepSelectObj=document.getElementsByName("RepSelect");
                var roleid="";
                var reportId = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        reportId.push(RepSelectObj[i].value);
                    }
                }
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: path+'/reportTemplateAction.do?templateParam=getRoleIdByReportID&reportId='+reportId,
                    success: function(data){
                        roleid=data;
                        var reportName = parent.document.getElementById('reportName').value;
                        var reportDesc = parent.document.getElementById('reportDesc').value;
                        if(reportName==''){
                            alert("Please enter Report Name");
                        }
                        else  if(reportDesc==''){
                            alert("Please enter Report Description")
                        }
                        else{
                            reportName = encodeURIComponent(reportName);
                            reportDesc = encodeURIComponent(reportDesc);
                            $.ajax({
                                type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                                url: path+'/reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName+"&roleid="+roleid,
                                success: function(data){
                                    if(data!=""){
                                        parent.document.getElementById('duplicate').innerHTML = data;
                                        parent.document.getElementById('save').disabled = true;
                                    }
                                    else {
                                        $.ajax({
                                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                                            url: path+"/reportTemplateAction.do?templateParam=copyReport&reportName="+reportName+"&reportDesc="+reportDesc+"&reportId="+reportId,
                                            success: function(data){
                                                var json=eval("("+data+")");
                                                if ( json.errorCode == 'REPORT_COPY_SUCCESS' ){
                                                    alert(json.mesgTxt);
                                                }else{
                                                    alert(json.mesgTxt);
                                                }
                                                document.forms.reportForm.action=path+"/home.jsp#Report_Studio";
                                                document.forms.reportForm.submit();
                                            }
                                        });
                                        parent.$("#reportMeta").dialog('close');
                                    }
                                }
                            });
                        }
                    }
                });
            }
            function selectAllReps()
            {
                var RepSelectObj=document.getElementsByName("RepSelect");
                for(var i=0;i<RepSelectObj.length;i++){
                    if(RepSelectObj[0].checked){
                        RepSelectObj[i].checked=true;
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
            }
            function selectAllAOs()
            {
                var RepSelectObj=document.getElementsByName("AOSelect");
                for(var i=0;i<RepSelectObj.length;i++){
                    if(RepSelectObj[0].checked){
                        RepSelectObj[i].checked=true;
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
            }
            function selectAllTemplate()
            {
                var RepSelectObj=document.getElementsByName("AOSelect");
                for(var i=0;i<RepSelectObj.length;i++){
                    if(RepSelectObj[0].checked){
                        RepSelectObj[i].checked=true;
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
            }
            function deleteReport(path)
            {
                var RepSelectObj=document.getElementsByName("RepSelect");
                var deleterepids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        deleterepids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(deleterepids.length!=0){
                    var confirmDel=confirm("Do you want to Delete Report");
                    if(confirmDel==true){
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportTemplateAction.do?templateParam=DeleteUserReports&deleterepids="+deleterepids.toString(),
                            success: function(data){
                                document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                                document.forms.reportForm.submit();
                            }
                        });
                    }else{
                        document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                        document.forms.reportForm.submit();
                    }
                }else{
                    alert("Please select Report(s)")
                }
            }
            //added by Dinanath for EditAo           
function EditAO(path)
{
    var RepSelectObj = document.getElementsByName("AOSelect");
    var editrepids = new Array();
    for (var i = 0; i < RepSelectObj.length; i++) {
        if (RepSelectObj[i].checked) {
            editrepids.push(RepSelectObj[i].value);
        } else {
            RepSelectObj[i].checked = false;
        }
    }
    if (editrepids.length != 0) {
        if (editrepids.length == 1) {
            var confirmDel = confirm("All Reports Related to this AO will be Effected\n\Do you want to Edit AO?");
            if (confirmDel == true) {
                $.ajax({
                    async: false,
                    url: 'reportTemplateAction.do?templateParam=removeSessionAttributes',
                    success: function (data) {
                    }
                });
                var aoid = editrepids[0];
                editionOfAO(aoid);
            } else {
                document.forms.reportForm.action = path + "/home.jsp#AO_Builder";
                document.forms.reportForm.submit();
            }
        } else {
            alert("You can't edit more than one AO");
        }
    } else {
        alert("Please select AO(s)");
    }
}
//added by Dinanath for edition of AO
function editionOfAO(aoId) {
    var timeDetail = "Time-Period Basis";
    var timeDim = 'AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
    var fldObj = document.getElementsByName("userfldsList");
    var foldersIds = "";
    $.ajax({
        url: "reportTemplateAction.do?templateParam=goToCreateAODesigner&aoid=" + aoId + "&fromAOEdit=true&dimId=" + timeDetail + "&timeparams=" + timeDim,
        success: function (data) {
            foldersIds = data;
            document.forms.searchForm.action = "reportTemplateAction.do?templateParam=selectRoleGoToDesinAO&repId=" + aoId + "&roleId=" + foldersIds+"&isFromAOEdit=true";
            document.forms.searchForm.method = "POST";
            document.forms.searchForm.submit();
        }
    });
}
//end of code by Dinanath
            //added by bhargavi
            function deleteAOandReports(path)
            {
                var AOSelectObj=document.getElementsByName("AOSelect");
                var deleteAOids = new Array();
                for(var i=1;i<AOSelectObj.length;i++){
                    if(AOSelectObj[i].checked){
                        deleteAOids.push(AOSelectObj[i].value);
                    }
                    else{
                        AOSelectObj[i].checked=false;
                    }
                }
                if(deleteAOids.length!=0){
                    var confirmDel=confirm("Do you want to Delete AO and Related Reports");
                    if(confirmDel==true){
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportTemplateAction.do?templateParam=DeleteUserAOs&deleterepids="+deleteAOids.toString(),
                            success: function(data){
                                document.forms.reportForm.action = path+"/home.jsp#AO_Builder";
                                document.forms.reportForm.submit();
                            }
                        });
                    }else{
                        document.forms.reportForm.action = path+"/home.jsp#AO_Builder";
                        document.forms.reportForm.submit();
                    }
                }else{
                    alert("Please select AO(s)")
                }
            }
            
            function SendScheduleAO(path){
      $("#ScheduleAO").dialog('close');
     //$("#loading").show();
       $.ajax({
                        async: false,
                        url: 'reportViewerAction.do?reportBy=defineAOSchedule',
                            data: $("#dailyAoScheduleForm").serialize(),
            success: function (data) {
             if(data=='AO is Scheduled Sucessfully'){
                   alert("AO is Scheduled Sucessfully");
                  // $("#loading").hide();
               }
               else{
                   alert(data);
                 //  $("#loading").hide();
               }
               
                   document.forms.reportForm.action = path+"/home.jsp#AO_Builder";
                                document.forms.reportForm.submit();
                            }
                        });
  }
            
            function scheduleAO(path) {
    var AOSelectObj = document.getElementsByName("AOSelect");
    var scheduleAOids = new Array();
    for (var i = 1; i < AOSelectObj.length; i++) {
        if (AOSelectObj[i].checked) {
            scheduleAOids.push(AOSelectObj[i].value);
        } else {
            AOSelectObj[i].checked = false;
        }
    }
    if (scheduleAOids.length !== 0) {
        if (scheduleAOids.length === 1) {
            $.ajax({
                type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                url: "reportViewerAction.do?reportBy=validateScheduleAO&scheduleAOid=" + scheduleAOids[0],
                success: function (data) {
                    var validate = data.toString().split(",");
                    if (validate[0] == "null") {
                        parent.$("#AOname").val(validate[1]);
                        parent.$("#schedulerId").val(scheduleAOids[0]);
                        $("#weekday").hide();
                        $("#monthday").hide();

                        $("#ScheduleAO").dialog("open");
                    } else {
                        alert("Scheduler is already exists");
                        document.forms.reportForm.action = path + "/home.jsp#AO_Builder";
                        document.forms.reportForm.submit();
                    }
                }
            });

        } else {
            alert("You can't Schedule more than one AO");
        }
    } else {
        alert("Please select AO");
    }

}
function editScheduleAO(){
    var AOSelectObj = document.getElementsByName("AOSelect");
    var editscheduleAOids = new Array();
    for (var i = 1; i < AOSelectObj.length; i++) {
        if (AOSelectObj[i].checked) {
            editscheduleAOids.push(AOSelectObj[i].value);
        } else {
            AOSelectObj[i].checked = false;
        }
    }
    if (editscheduleAOids.length !== 0) {
        if (editscheduleAOids.length === 1) {
            
        } else {
            alert("You can't edit more than one AO");
        }
    } else {
        alert("Please select AO(s)");
    }
}
            //added by Mohit
            function invalidateCache(path)
            {
                var RepSelectObj=document.getElementsByName("RepSelect");
                var deleterepids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        deleterepids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(deleterepids.length!=0){
                    var usertype=getUserType();
//                    alert(usertype);
                     var confirmDel="";
                     var isadmin=false;
                      var forallusers=false;
                    if(usertype !=null && (usertype=="ADMIN" || usertype=="admin" || usertype=="Admin" ))
                    {
                        confirmDel =confirm("Do You Want to Invalidate Cache for other User(s) also ?");
                        isadmin=true;
                    }else
                    {
                        confirmDel=confirm("Do You Want to Invalidate Cache for These Report(s)");
                    }
                    
                    if(confirmDel==true || isadmin==true){
                        
                         if(confirmDel == true && isadmin == true)
                         {
                             forallusers=true;
                         }
                         parent.$("#loading").show();
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportViewerAction.do?reportBy=invalidateCache&deleterepids="+deleterepids.toString()+"&usertype="+usertype+"&forallusers="+forallusers,
                            success: function(data){
                                if(data=="Succeed")
                                alert("Caches Invalidated Successfully")
                                else
                                 alert("Something Went Wrong, Caches Couldn't Invalidate Successfully")   
                                document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                                document.forms.reportForm.submit();
                            }
                        });
                    }else{
                        document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                        document.forms.reportForm.submit();
                    }
                }else{
                     parent.$("#loading").hide();
                    alert("Please Select Report(s)")
                }
            }
            //added by Mohit
            function rebuildCache(path)
            {
                var RepSelectObj=document.getElementsByName("RepSelect");
                var deleterepids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        deleterepids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
//                alert("rebuildCache")
                if(deleterepids.length!=0){
                      
                     var usertype=getUserType();
//                    alert(usertype);
                     var confirmDel="";
                     var isadmin=false;
                      var forallusers=false;
                    if(usertype !=null && (usertype=="ADMIN" || usertype=="admin" || usertype=="Admin" ))
                    {
                        confirmDel =confirm("Do You Want to Rebuild Cache for other User(s) also ?");
                        isadmin=true;
                    }else
                    {
                        confirmDel=confirm("Do You Want to Rebuild Cache for These Report(s)");
                    }
                    
                    if(confirmDel==true || isadmin==true){
                        
                         if(confirmDel == true && isadmin == true)
                         {
                             forallusers=true;
                         }  
                        parent.$("#loading").show();
                        
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                            url: "reportViewerAction.do?reportBy=rebuildCache&deleterepids="+deleterepids.toString()+"&usertype="+usertype+"&forallusers="+forallusers,
                            success: function(data){
                               parent.$("#loading").hide();
                                if(data=="Succeed")
                                alert("Caches Rebuilded Successfully")
                                else
                                 alert("Something Went Wrong, Caches Couldn't Invalidate Successfully")   
                                document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                                document.forms.reportForm.submit();
                            }
                        });
                    }else{
                        document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                        document.forms.reportForm.submit();
                    }
                }else{
                    parent.$("#loading").hide();
                    alert("Please Select Report(s)")
                }
            }
            function unselAll(){
//                var allsel = document.getElementById("RepSelect1");
//                allsel.checked = false;
            }
            function orderBYselReport(path){
                var selectValue = document.getElementById("sortBySel").value;
                var seloption;
                var radioObj = document.forms.reportForm.sortOption;
                for(var i=0;i<radioObj.length;i++){
                    if(radioObj[i].checked){
                        seloption=radioObj[i].value
                    }
                }
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: "reportTemplateAction.do?templateParam=repStudioSort&selectValue="+selectValue+"&seloption="+seloption,
                    success: function(data){
                        document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                        document.forms.reportForm.submit();
                    }
                });
            }

            function purgeReport(path)
            {
                var RepSelectObj=document.getElementsByName("RepSelect");
                var purgerepids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        purgerepids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(purgerepids.length!=0){
                    var confirmPurge=confirm("Do you want to Purge Report(s)");
                    if(confirmPurge==true){
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportTemplateAction.do?templateParam=PurgeReports&purgeRepids="+purgerepids.toString(),
                            success: function(data){
                                document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                                document.forms.reportForm.submit();
                            }
                        });
                    }else{
                        document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                        document.forms.reportForm.submit();
                    }
                }else{
                    alert("Please select Report(s)")
                }
            }
             function clearCache(path)
            {
                var RepSelectObj=document.getElementsByName("RepSelect");
                var clearcacheids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        clearcacheids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }

                if(clearcacheids.length!=0){
                    var confirmPurge=confirm("Do you want to clear Cache");
                    if(confirmPurge==true){
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportTemplateAction.do?templateParam=clearCache&clearCacheids="+clearcacheids.toString(),
                            success: function(data){
                                document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                                document.forms.reportForm.submit();
                            }
                        });
                    }else{
                        document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
                        document.forms.reportForm.submit();
                    }
                }else{
                    alert("Please select Report(s)")
                }
            }

            function downloadAsSnapshotInReports()
            {

                var RepSelectObj=document.getElementsByName("RepSelect");
                var reportIds = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        reportIds.push(RepSelectObj[i].value);
                    }
                }
                if ( reportIds.length != 0 )
                    parent.$("#downloadDialog").dialog('open');
                else
                    alert("Please select Report(s)");
            }
// function tabmsg1(){
//                document.getElementById('snapShotDesc').value = document.getElementById('snapShotName').value;
//            }
        function openSnapShotDiv(html){
          
            parent.$("#htmlType").val(html);
                var RepSelectObj=document.getElementsByName("RepSelect");
                var reportIds = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        reportIds.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(reportIds.length==0){
                    alert("Please select Report(s)")
                }else{
               var flag = confirm("do you want to save the snapshot with new name");
                if (flag == true){
                     $("#snapShotNameDiv").dialog('open');
                }else {
//                    if(html==undefined)
//                    html='basicHtml';
                    createDataSnapshotInReports(html,'');
                }/*else if(html==undefined){
                    html='fromAdvancedHtml';
                    SaveAsAdvancedHtml(snapName);
                }*/
                }
            }
//        function SaveAsAdvancedHtml(snapName)
        function getSnapShotName(){
                var html=parent.$("#htmlType").val();
                var snapName = parent.$("#snapShotName").val();
                parent.$("#snapShotNameDiv").dialog('close');
                createDataSnapshotInReports(html,snapName);

            }
	function createDataSnapshotInReports(html,snapName)
            {
                if(snapName==undefined)
                    snapName="";
                parent.$ ("#snapShotName").val(snapName);
                if(html!='fromAdvancedHtml')
                     parent.$("#CreateDataSnapshotDiv").dialog('open');
                 else
                     saveAsSnapshot(html);

            }

            function goToSelectedComponent(path)
            {
                var componentMethod=$("#reportsComponent").val();
                var advancedHtml='fromAdvancedHtml';
                 if(componentMethod=="CREATE_CUSTOM_REPORT")
                    CreateReportNew1();   
                if(componentMethod=="CREATE_REPORT")
//                    createnewReport();
                  CreateReportNew();              // Added by Mayank.
                else if(componentMethod=="CREATE_REPORT_AO")
                   CreateReportwithAO();

                else if(componentMethod=="EDIT_REPORT_NAME")
                    EditReportName();

                else if(componentMethod=="DELETE_REPORT")
                    deleteReport(path);

                else if(componentMethod=="PURGE_REPORT")
                    purgeReport(path);

                else if(componentMethod=="CLEAR_CACHE")
                    clearCache(path);

                else if(componentMethod=="DOWNLOAD")
                    downloadAsSnapshotInReports();

                else if(componentMethod=="SAVE_AS_HTML")
                   openSnapShotDiv('basicHtml');

                else if(componentMethod=="COPY_REPORT")
                    copyReport();
                else if(componentMethod=="SHOW_METADATA")
                    showMetaData_RS(path);
                else if(componentMethod=="COMPARE_REPORTS")
                    compareReports(path);
                else if(componentMethod=="SAVE_AS_ADVANCED_HTML")
                    openSnapShotDiv('fromAdvancedHtml');
                else if(componentMethod=="RETRIEVE_FROM_BKP")
                    retrieveFromBkp(path);
//                else if(componentMethod=="CREATE_CUSTOM_REPORT")
//                    CreateReportNew();
                else if(componentMethod=="INVALIDATE_QUERY_CACHE") //added by Mohit
                    invalidateCache(path);
                else if(componentMethod=="REBUILD_QUERY_CACHE") //added by Mohit
                    rebuildCache(path);
                else if(componentMethod=="CREATE_TEMPLATE"){
                    
                  createManagementDashboard(path);
                }
                //else if(componentMethod=="ADHOC_REPORT"){     //commented by mayank..
                  //  CreateReportNew();


            }

            function tabmsg2(){
                document.getElementById('dashboardDesc').value = document.getElementById('dashboardName').value;
            }

           function tabmsg3(){
                document.getElementById('kpidashboardDesc').value = document.getElementById('kpidashboardName').value;
            }
            function tabmsg4(){
                document.getElementById('newdashboardDesc').value = document.getElementById('newdashboardName').value;
            }
            function EditDashboardName(ctxPath){

                var dashbdSelectObj=document.getElementsByName("DashSelect");
                var editdashbdids = new Array();
                for(var i=1;i<dashbdSelectObj.length;i++){
                    if(dashbdSelectObj[i].checked){
                        editdashbdids.push(dashbdSelectObj[i].value);
                    }
                    else{
                        dashbdSelectObj[i].checked=false;
                    }
                }
                if(editdashbdids.length!=0){
                    if(editdashbdids.length<2){
                        var dashbdId=editdashbdids.pop();
                        var sourcedb = "EditDashboardName.jsp?dashbdId="+dashbdId;
                        var confdb = confirm("Changed Dashboard Name will reflect for all other assigned users also. Would you like to continue?")
                        if(confdb == true)
                            parent.editdashbdName(sourcedb);
                        else{
                            document.forms.dashboardForm.action = ctxPath+"/home.jsp#Dashboard_Studio";
                            document.forms.dashboardForm.submit();
                        }
                    }else{
                        alert("Please select only one Dashboard")
                    }
                }
                else{
                      alert("Please select Dashboard ")
                    }
            }
            function viewDashboardInStudio(path){
//                alert("viewDashboardInStudio")
                document.forms.dashboardForm.action=path;
//                alert("path "+path)
                document.forms.dashboardForm.submit();
            }
            function userFolderAssignment(){
                var frameObj=document.getElementById("userFolderAssignmentDisp");
                var source = "userFolderAssignment.jsp";
                frameObj.src=source;
                document.getElementById('userFolderassign').style.display='none';
                frameObj.style.display='block';
                document.getElementById('fade').style.display='block';

            }
            function cancelUsersFolders(){
                document.getElementById("userFolderAssignmentDisp").style.display='none';
                document.getElementById('fade').style.display='';
                document.getElementById('userFolderassign').style.display='';
                cancelUsersFolders();
            }
            function createDashboard(){
                parent.createDash();
            }
            function cancelDashboard(){
                parent.closeDash();}
//Bhargavi
                function timeBasedDashboard1(){
                parent.timeBasedDash();
            }
            function canceltimeBasedDashboard(){
                parent.closetimeBasedDash();


//                <%--document.getElementById('duplicateDashboard').innerHTML = '';
//                document.getElementById('dashboardsave').disabled = false;
//                document.getElementById('dashboard').style.display='none';
//                document.getElementById('fade').style.display='none';
//                document.getElementById('mainBody').style.overflow='auto';--%>
            }
            function saveDashboard(){
                 var dashboardName = document.getElementById('dashboardName').value;
               // dashboardName=dashboardName.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                var dashboardDesc = document.getElementById('dashboardDesc').value;
                //dashboardDesc=dashboardDesc.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: 'dashboardTemplateAction.do?templateParam2=checkDashboardName&dashboardName='+encodeURIComponent(dashboardName)+'&dashboardDesc='+encodeURIComponent(dashboardDesc),
                    success: function(data){
                        if(data!=""){
                            document.getElementById('duplicateDashboard').innerHTML = data;
                            document.getElementById('dashboardsave').disabled = true;
                        }
                        else if(data==''){
                            document.forms.dashboardForm.action = "dashboardTemplateAction.do?templateParam2=goToDashboardDesigner&dashboardName="+encodeURIComponent(dashboardName)+"&dashboardDesc="+encodeURIComponent(dashboardDesc);
                            document.forms.dashboardForm.method="POST";
                            document.forms.dashboardForm.submit();
                        }
                    }
                });
            }
            function saveTimeBasedDash(){
                 var dashboardName = document.getElementById('timeBaseddashboardName').value;
               // dashboardName=dashboardName.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                var dashboardDesc = document.getElementById('timeBaseddashboardDesc').value;
                //dashboardDesc=dashboardDesc.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: 'dashboardTemplateAction.do?templateParam2=checkDashboardName&dashboardName='+encodeURIComponent(dashboardName)+'&dashboardDesc='+encodeURIComponent(dashboardDesc),
                    success: function(data){
                        if(data!=""){
                            document.getElementById('duplicateTimeBasedDashboard').innerHTML = data;
                            document.getElementById('saveTimeBasedDash').disabled = true;
                        }

                         else if(data==''){
                              $.ajax({
                                    type:'GET',
                                    url: "dashboardTemplateAction.do?templateParam2=goToTimeDashboardDesigner&dashboardName="+encodeURIComponent(dashboardName)+"&reportDesc="+encodeURIComponent(dashboardDesc)+"&iskpidashboard=true",
                                  success: function(data){

                                   parent.$("#timeBasedDash").dialog('close');
                                   $("#selectkpiBussRole").html(data);
                                   $("#selectkpiBussRole").dialog('open');

                              }

                          });

                        }


                    }
                });
            }

            function saveKPIDashboard(){
                alert("in the function");
                    ////////alert("checking username and password")
                    var dashboardName = document.getElementById('dashboardName').value;
                    var dashboardDesc = document.getElementById('dashboardDesc').value;
                     var dashboardDesc = document.getElementById('KpiDashboardType').value;
                    $.ajax({ type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
                        url: 'dashboardTemplateAction.do?templateParam2=checkDashboardName&dashboardName='+encodeURIComponent(dashboardName)+'&dashboardDesc='+encodeURIComponent(dashboardDesc),
                        success: function(data){
                            if(data!=""){
                                document.getElementById('duplicateDashboard').innerHTML = data;
                                document.getElementById('dashboardsave').disabled = true;
                            }
                            else if(data==''){
                                //document.forms.dashboardForm.action = "dashboardTemplateViewerAction.do?templateParam2=goToDashboardDesigner&dashboardName="+dashboardName+"&dashboardDesc="+dashboardDesc;
                                //document.forms.dashboardForm.method="POST";
                                //document.forms.dashboardForm.submit();
                               alert("in the ajax......");

                            document.forms.dashboardForm.action = "dashboardTemplateAction.do?templateParam2=goToDashboardDesigner&dashboardName="+encodeURIComponent(dashboardName)+"&dashboardDesc="+encodeURIComponent(dashboardDesc);
                                document.forms.dashboardForm.submit();
                            }
                        }
                    });

                }

             function selectAllDashs()
            {
                var DashSelectObj=document.getElementsByName("DashSelect");
                for(var i=0;i<DashSelectObj.length;i++){
                    if(DashSelectObj[0].checked){
                        DashSelectObj[i].checked=true;
                    }
                    else{
                        DashSelectObj[i].checked=false;
                    }
                }
            }

             function selectAllPortals()
            {
                var PortalSelectObj=document.getElementsByName("PortalSelect");
                for(var i=0;i<PortalSelectObj.length;i++){
                    if(PortalSelectObj[0].checked){
                        PortalSelectObj[i].checked=true;
                    }
                    else{
                        PortalSelectObj[i].checked=false;
                    }
                }
            }
             function selectAllMyReps()
            {
                var MyRepSelectObj=document.getElementsByName("MyRepSelect");
                for(var i=0;i<MyRepSelectObj.length;i++){
                    if(MyRepSelectObj[0].checked){
                        MyRepSelectObj[i].checked=true;
                    }
                    else{
                        MyRepSelectObj[i].checked=false;
                    }
                }
            }
            function selectAllSnapshots(){
                 var HtmlSelectObj=document.getElementsByName("HtmlSelect");
                for(var i=0;i<HtmlSelectObj.length;i++){
                    if(HtmlSelectObj[0].checked){
                        HtmlSelectObj[i].checked=true;
                    }
                    else{
                        HtmlSelectObj[i].checked=false;
                    }
                }
            }
            function purgeDashboard(ctxPath)
            {
                 var dashbdSelectObj=document.getElementsByName("DashSelect");
                var purgedashbdids = new Array();
                 for(var i=1;i<dashbdSelectObj.length;i++){
                    if(dashbdSelectObj[i].checked){
                        purgedashbdids.push(dashbdSelectObj[i].value);
                    }
                    else{
                        dashbdSelectObj[i].checked=false;
                    }
                }

           if(purgedashbdids.length!=0){

                    var confmdel=confirm("Do you want to purge Dashboard(s)");

                    if(confmdel==true){
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportTemplateAction.do?templateParam=purgeDashboard&dashboardid="+purgedashbdids.toString(),
                            success: function(data){
                                document.forms.dashboardForm.action = ctxPath+"/home.jsp#Dashboard_Studio";
                                document.forms.dashboardForm.submit();
                            }
                        });
                        //  document.forms.dashboardForm.action="reportTemplateAction.do?templateParam=Deletedashboard&dashboardid="+deleterepids.toString();
                        //  document.forms.dashboardForm.submit();

                    }
                }
                else{
                    alert("Please select Dashboards(s)")
                }
            }
            function deleteDashboard(ctxPath)
            {
                var DashSelectObj=document.getElementsByName("DashSelect");
                var deleterepids = new Array();
                for(var i=1;i<DashSelectObj.length;i++){
                    if(DashSelectObj[i].checked){
                        deleterepids.push(DashSelectObj[i].value);
                    }
                    else{
                        DashSelectObj[i].checked=false;
                    }
                }
                if(deleterepids.length!=0){
                    var confmdel=confirm("Do you want to Delete Dashboard");
                    if(confmdel==true){
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportTemplateAction.do?templateParam=Deletedashboard&dashboardid="+deleterepids.toString(),
                            success: function(data){
                                document.forms.dashboardForm.action = ctxPath+"/home.jsp#Dashboard_Studio";
                                document.forms.dashboardForm.submit();
                            }
                        });
                        //  document.forms.dashboardForm.action="reportTemplateAction.do?templateParam=Deletedashboard&dashboardid="+deleterepids.toString();
                        //  document.forms.dashboardForm.submit();

                    }
                    else{
                       // document.forms.dashboardForm.action = "<%=request.getContextPath()%>/home.jsp#Dashboard_Studio";
                       // document.forms.dashboardForm.submit();
                    }
                }
                else{
                    alert("Please select Dashboards(s)")
                }
            }
            function unselAllDash(){
            var allsel = document.getElementById("DashSelect1");
            allsel.checked = false;
            }
            function orderBYselDash(ctxPath){
                var selectValue = document.getElementById("sortBySel").value;
                var seloption;
                var radioObj = document.forms.dashboardForm.sortOption;
                for(var i=0;i<radioObj.length;i++){
                    if(radioObj[i].checked){
                        seloption=radioObj[i].value
                    }
                }
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: "reportTemplateAction.do?templateParam=dashStudioSort&selectValue="+selectValue+"&seloption="+seloption,
                    success: function(data){
                        document.forms.dashboardForm.action = ctxPath+"/home.jsp#Dashboard_Studio";
                        document.forms.dashboardForm.submit();
                    }
                });
            }

//             $(document).ready(function(){
//                    $("#tableDataSorter")
//                    .tablesorter({headers : {0:{sorter:false}}})
//                    .tablesorterPager({container: $('#pagerDataReport')})
//                });


         function viewDataSnapshot(snapShotId,ctxPath)
         {
           $("#snapShotId").val(snapShotId);
           document.forms.snapShotForm.action = ctxPath + "/dataSnapshot.do?doAction=openDataSnapshot&snapShotId="+snapShotId;
           document.forms.snapShotForm.submit();
         }
//         function selectAllReps()
//            {
//                var RepSelectObj=document.getElementsByName("RepSelect");
//
//                for(var i=0;i<RepSelectObj.length;i++){
//                    if(RepSelectObj[0].checked){
//                        RepSelectObj[i].checked=true;
//                    }
//                    else{
//                        RepSelectObj[i].checked=false;
//                    }
//                }
//            }

    function deleteSnapShot(ctxPath)
            {
                var RepSelectObj=document.getElementsByName("HtmlSelect");
                var deletesnapshotids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        deletesnapshotids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(deletesnapshotids.length!=0){
                    var confirmDel=confirm("Do you want to Delete SnapShot");
                    if(confirmDel==true){
                        $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "dataSnapshot.do?doAction=deleteSnapShots&deletesnapshotids="+deletesnapshotids.toString(),
                            success: function(data){
                                document.forms.snapShotForm.action = ctxPath+"/home.jsp#Html_Reports";
                                document.forms.snapShotForm.submit();
                            }
                        });
                        // document.forms.reportForm.action="reportTemplateAction.do?templateParam=DeleteUserReports&deleterepids="+deleterepids.toString();
                        //  document.forms.reportForm.submit();
                    }else{
                        document.forms.snapShotForm.action = ctxPath+"/home.jsp#Html_Reports";
                        document.forms.snapShotForm.submit();
                    }
                }else{
                    alert("Please select SnapShots(s)")
                }

            }

       function downloadAsSnapshot()
            {
                var RepSelectObj=document.getElementsByName("MyRepSelect");
                var reportIds = new Array();
                var reportType=new Array();
                var reportTypeFlag=true;
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        reportIds.push(RepSelectObj[i].value);
                        reportType.push($("#ReportType"+RepSelectObj[i].value).html());
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                for(var j=0;j<reportType.length;j++)
                {
                            if($.trim(reportType[j].toString())=="Dashboard")
                            {
                                reportTypeFlag=false;
                                alert("You cannot download Dashboard");
                                break;
                            }
                }
                if(reportIds.length!=0)
                {
                    if(reportTypeFlag==true)
                        parent.$("#downloadDialog").dialog('open');
                 }
                 else
                     alert("Please select Report(s)");
            }

      function createDataSnapshot()
            {
                var RepSelectObj=document.getElementsByName("MyRepSelect");
                var reportIds = new Array();
                var reportType=new Array();
                var reportTypeFlag=true;
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        reportIds.push(RepSelectObj[i].value);
                        reportType.push($("#ReportType"+RepSelectObj[i].value).html());
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }

                for(var j=0;j<reportType.length;j++)
                {
                            if($.trim(reportType[j].toString())=="Dashboard")
                            {
                                reportTypeFlag=false;
                                alert("You cannot save Dashboard as HTML");
                                break;
                            }
                }
                if(reportIds.length!=0)
                {
                    if(reportTypeFlag==true)
                     parent.$("#CreateDataSnapshotDiv").dialog('open');
                }else{
                    alert("Please select Report(s)")
                }
            }


    function viewAnalyzer()
        {
            document.getElementById("uploadTextFile").style.display='';
        }

    function uploadTextFile(path)
        {
           document.forms.uploadTextForm.action = path+"/sentimentAction.do?doAction=uploadTextFile";
            document.forms.uploadTextForm.submit();
        }
    function openAnalyzedReport(){}

     function displayStudioItem(id,userid,type,studioId,path)
     {
            var searchText=$("#"+id).val();

            if(searchText=="")
                alert("please enter a report name");
            else{
            var searchTextEncode=encodeURIComponent(searchText);
           $.ajax({
               type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                url:'studioAction.do?studioParam=displayList&searchText='+searchTextEncode+'&userId='+userid+'&type='+type,
                success: function(data){
                    var json=eval('('+data+')');
                       if(json.itemLst.length==0)
                      alert("no item exists with this name");
                  else
                            bulidTable(data,studioId,type,path)
                            }
                    });
            }
        }


         function bulidTable(data,id,type,path,themeColor){
                           var color="";
                           if(themeColor=="blue")
                               color="#369";
                           else if(themeColor=="green")
                               color="#006600";
                           else if(themeColor=="orange")
                               color="#C77405";
                           else if(themeColor=="violet")
                               color="#6c94da";
                           else if(themeColor=="purple")
                               color="#901f78";

                           var json=eval('('+data+')')
                            var tableData="";
                            var length= json.itemLst.length;
                            tableData=tableData+"<thead>"
                            tableData=tableData+"<tr>"
                            if(type=="Dashboard")
                            tableData=tableData+"<th nowrap style='width:15px'><input type='checkbox' name='DashSelect' id='DashSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllDashs()'>"
                            else if(type=="Report")
                            tableData=tableData+"<th nowrap style='width:15px'><input type='checkbox' name='RepSelect' id='RepSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllReps()'>"
                            else if(type=="Scorecard")
                            tableData=tableData+"<th nowrap style='width:15px'><input type='checkbox' name='ScoreSelect' id='ScoreSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllScores()'>"
                            else if(type=="MyReports")
                            tableData=tableData+"<th nowrap style='width:15px'><input type='checkbox' name='MyRepSelect' id='MyRepSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllMyReps()'>"
                            else if(type=="HtmlReports")
                           tableData=tableData+"<th nowrap style='width:15px'><input type='checkbox' name='HtmlSelect' id='HtmlSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllSnapshots()'>"
                        else if(type=="Portals"){
                            tableData=tableData+"<th nowrap style='width:15px'><input type='checkbox' name='PortalSelect' id='PortalSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllPortals()'>"
                            
                        }
                        //added by mohit for AO
                           else if(type=="AO")
                            tableData=tableData+"<th nowrap style='width:10px'><input type='checkbox' name='AOSelect' id='AOSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllAOs()'>"
                            else if(type="MgmtTemplate"){
                             tableData=tableData+"<th nowrap style='width:10px'><input type='checkbox' name='TempSelect' id='TempSelect1' style='background-color:#B4D9EE;border:none;' onclick='return selectAllTemplate()'>"   
                            }
                           else if(type!="sentiment")
                            tableData=tableData+"Select All </th>"

                            for(var i=0;i<json.labels.length;i++)
                               {
                                    tableData=tableData+" <th nowrap>"
                                    tableData=tableData+json.labels[i]
                                    tableData=tableData+"</th>"
                               }

                            tableData=tableData+"</tr>"
                            tableData=tableData+"</thead>"
                            tableData=tableData+"<tbody>"

                            for (var i = 0; i < json.itemLst.length; i++)
                              {

                                 tableData=tableData+" <tr>"
                                 
                                 if(type=="Dashboard"){
                                 tableData=tableData+"<td><input type='checkbox' name='DashSelect' id='DashSelect"+(i + 1)+"' style='background-color: #fff;border:none;' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                 tableData=tableData+"<td  id='hyperDash' style='font-size:12px;color:"+color+";cursor:pointer'  onclick='javascript:viewDashboardInStudio(\"dashboardViewer.do?reportBy=viewDashboard&REPORTID="+json.itemLst[i].attribute1+"&pagename="+json.itemLst[i].attribute2+"&editDbrd="+false+"\")'>"
                                 }

                                 else if(type=="Report"){
                                      tableData=tableData+"<td><input type='checkbox' name='RepSelect' id='RepSelect"+(i + 1)+"' style='background-color: #fff;border:none;'  value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep"+(i + 1)+"' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewReportinStudio('reportViewer.do?reportBy=viewReport&REPORTID="+json.itemLst[i].attribute1+"&action=open')>"
                                 }
                                  else if(type=="Portals"){
                                      tableData=tableData+"<td><input type='checkbox' name='PortalSelect' id='PortalSelect"+(i)+"' style='background-color: #fff;border:none;' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep"+(i)+"' style='font-size:12px;color:"+color+";cursor:pointer' onclick=javascript:viewPortlet('"+json.itemLst[i].attribute1+"','portletPreviewDivinDesiner','"+(json.itemLst[i].attribute2).replace(" ", "_").replace(" ", "_")+"')>"
                                 }
                                 else if(type=="Scorecard"){
                                          tableData=tableData+"<td><input type='checkbox' name='ScoreSelect' id='ScoreSelect"+(i + 1)+"' style='background-color: #fff;border:none;' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                          tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewScorecard('"+json.itemLst[i].attribute1+"')>"
                                     }
                                     else if(type=="MyReports"){
                                            tableData=tableData+"<td style='width:30px'><input type='checkbox' name='MyRepSelect' id='MyRepSelect"+(i + 1)+"' style='background-color: #fff;border:none;' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewReport('reportViewer.do?reportBy=viewReport&REPORTID="+json.itemLst[i].attribute1+"&action=open')>"
                                     }
                                     else if(type=="AO"){
                                            tableData=tableData+"<td><input type='checkbox' name='AOSelect' id='AOSelect"+(i + 1)+"' style='background-color: #fff;border:none;' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewReport('reportViewer.do?reportBy=viewReport&REPORTID="+json.itemLst[i].attribute1+"&action=open')>"
                                     }
                                     else if(type=="MgmtTemplate"){
                                            tableData=tableData+"<td><input type='checkbox' name='TempSelect' id='TempSelect"+(i + 1)+"' style='background-color: #fff;border:none;' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewReport('reportViewer.do?reportBy=viewReport&REPORTID="+json.itemLst[i].attribute1+"&action=open')>"
                                     }
                                      else if(type=="HtmlReports"){

                                         tableData=tableData+"<td><input type='checkbox' name='HtmlSelect' id='HtmlSelect"+(i + 1)+"' style='background-color: #fff;border:none;' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                         tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewDataSnapshot('"+json.itemLst[i].attribute1+"','"+path+"')>"
                                         tableData=tableData+json.itemLst[i].attribute2;
                                         tableData=tableData+" </td>"
                                         tableData=tableData+"<td>"+json.itemLst[i].attribute1+"</td>"
                                         tableData=tableData+"<td>"+json.itemLst[i].attribute3.substring(0, json.itemLst[i].attribute3.indexOf("."))+"</td>"
                                         if(json.itemLst[i].attribute3=="-1")
                                             tableData=tableData+"<td>-</td>"
                                         else
                                             tableData=tableData+"<td>"+json.itemLst[i].attribute4+"</td>"
                                          tableData=tableData+"<td>"+json.itemLst[i].attribute5.substring(0, json.itemLst[i].attribute3.indexOf("."))+"</td>"
                                          tableData=tableData+"<td>"+json.itemLst[i].attribute6+"</td>"
                                          tableData=tableData+"<td>"+json.itemLst[i].attribute7+"</td>"

                                      }
                                    if(type!="HtmlReports" && type!="sentiment"){
                                             tableData=tableData+json.itemLst[i].attribute2;
                                             tableData=tableData+" </td>"

                                             tableData=tableData+"<td >"
                                             tableData=tableData+  json.itemLst[i].attribute3
                                             tableData=tableData+" </td>"
                                                    if(type=="MyReports"){

                                              if(json.itemLst[i].attribute4!=null&&json.itemLst[i].attribute4!=undefined&&json.itemLst[i].attribute4=="R")
                                                  tableData=tableData+"<td id='ReportType"+json.itemLst[i].attribute1+"'>Report</td>"
                                              else
                                                  tableData=tableData+"<td id='ReportType"+json.itemLst[i].attribute1+"'>Dashboard</td>"
                                                    }
                                              else
                                             tableData=tableData+"<td>"+json.itemLst[i].attribute4+"</td>"
                                             tableData=tableData+" <td>"+json.itemLst[i].attribute5+"</td>"
                                               if(type=="AO"){
                                                    tableData=tableData+" <td>"+json.itemLst[i].attribute61+"</td>"
                                               }
                                             if(json.itemLst[i].attribute6!=null&&json.itemLst[i].attribute6!=undefined){
                                                 if(type=="Report"){
                                                   tableData=tableData+" <td>"+json.itemLst[i].attribute61+"</td>"
                                                   tableData=tableData+"<td style='text-align:center'><i class='fa fa-file-text-o fa-2x' style='cursor: pointer;' onclick=\"getReportDeatils('"+json.itemLst[i].attribute1+"')\"></i></td>"
                                                 
                                                 }else{
                                                 tableData=tableData+" <td>"+json.itemLst[i].attribute6+"</td>"
                                              }
//                                                 tableData=tableData+" <td>"+json.itemLst[i].attribute61+"</td>"
                               }
                               }
                                     else if(type=="sentiment")
                                    {
                                         tableData=tableData+"<td>"+json.itemLst[i].attribute1;
                                         tableData=tableData+" </td>"

                                         tableData=tableData+"<td>"
                                         tableData=tableData+  json.itemLst[i].attribute2
                                         tableData=tableData+" </td>"
                                         tableData=tableData+"<td align='center'>"
                                         tableData=tableData+ "<img onclick=\"reClassify('"+json.itemLst[i].attribute1+"','"+ json.itemLst[i].attribute2+"')\" src='"+path+"/icons pinvoke/globe.png'  />"
                                         tableData=tableData+" </td>"
                                    }
                                      tableData=tableData+"</tr>"
                              }
                                   tableData=tableData+"</tbody>"

                                   if(length>0)
                                       {
                                   $("#"+id).html('');

                                        $("#"+id).html(tableData);

                                        $("#"+id).tablesorter({headers : {0:{sorter:false}}})
                                        if(type=="Dashboard")
                                        $("#"+id).tablesorterPager({container: $('#pagerDashboard')})
                                        else if(type=="Report" || type=="AO" )
                                        $("#"+id) .tablesorterPager({container: $('#pagerReport')})
                                    else if(type=="Scorecard")
                                         $("#"+id) .tablesorterPager({container: $('#pagerScorecard')})
                                     else if(type=="MyReports")
                                          $("#"+id) .tablesorterPager({container: $('#pagerallReps')})
                                      else if(type=="Portals")
                                          $("#"+id) .tablesorterPager({container: $('#selPagerRep')})
                                       else if(type=="HtmlReports")
                                          $("#"+id) .tablesorterPager({container: $('#pagerDataReport')})
                                      else if(type=="sentiment")
                                          $("#"+id) .tablesorterPager({container: $('#pagerClassify')})
                                       }
                                   
                                    return length;

                                }


function bulidTableWithKpi(data,id,type,path,themeColor,data2)
              {



                           var color="";
                           if(themeColor=="blue")
                               color="#369";
                           else if(themeColor=="green")
                               color="#006600";
                           else if(themeColor=="orange")
                               color="#C77405";
                           else if(themeColor=="violet")
                               color="#6c94da";
                           else if(themeColor=="purple")
                               color="#901f78";
                           var json2;
                           var json=eval('('+data+')')
                           if(data2!=""){
                           json2=eval('('+data2+')');
                           }
                            var tableData="";
                            var length= json.itemLst.length;
                            tableData=tableData+"<thead>"
                            tableData=tableData+"<tr>"
                            if(type=="Dashboard")
                            tableData=tableData+"<th nowrap><input type='checkbox' name='DashSelect' id='DashSelect1' onclick='return selectAllDashs()'>"
                            else if(type=="Report")
                            tableData=tableData+"<th nowrap><input type='checkbox' name='RepSelect' id='RepSelect1' onclick='return selectAllReps()'>"
                            else if(type=="Scorecard")
                            tableData=tableData+"<th nowrap><input type='checkbox' name='ScoreSelect' id='ScoreSelect1' onclick='return selectAllScores()'>"
                            else if(type=="MyReports")
                            tableData=tableData+"<th nowrap><input type='checkbox' name='MyRepSelect' id='MyRepSelect1' onclick='return selectAllMyReps()'>"
                            else if(type=="HtmlReports")
                           tableData=tableData+"<th nowrap><input type='checkbox' name='HtmlSelect' id='HtmlSelect1' onclick='return selectAllSnapshots()'>"
                        else if(type=="Portals"){
                            tableData=tableData+"<th nowrap><input type='checkbox' name='PortalSelect' id='PortalSelect1' onclick='return selectAllPortals()'>"

                        }
                           else if(type!="sentiment")
                            tableData=tableData+"Select All </th>"

                            for(var i=0;i<json.labels.length;i++)
                               {

                                    tableData=tableData+" <th nowrap>"
                                    tableData=tableData+json.labels[i];
                                    tableData=tableData+"</th>"

                               }

                            tableData=tableData+"</tr>"
                            tableData=tableData+"</thead>"
                            tableData=tableData+"<tbody>"
                 if(data2!=""){

                        for (var i = 0; i < json2.itemLst.length; i++)
                              {
                                   tableData=tableData+" <tr>";
                                    tableData=tableData+"<td style='width:30px'><input type='checkbox' name='DashSelect' id='DashSelect"+(i + 1)+"' value='"+json2.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperDash' style='font-size:12px;color:"+color+";cursor:pointer'  onclick='javascript:viewReportinStudio2(\"reportViewer.do?reportBy=viewReport&REPORTID="+json2.itemLst[i].attribute1+"&action=open&isKPIDashboard=true\")'>"
//                                      tableData=tableData+"<td  id='hyperDash' style='font-size:12px;color:"+color+";cursor:pointer'  onclick='javascript:viewReportinStudio2(\"reportViewer.do?reportBy=viewReport&REPORTID="+json2.itemLst[i].attribute1+"&action=open&isKPIDashboard=true&firstDb=true\")'>"

//javascript:viewDashboardInStudio(\"dashboardViewer.do?reportBy=viewDashboard&REPORTID="+json2.itemLst[i].attribute1+"&pagename="+json2.itemLst[i].attribute2+"&editDbrd="+false+"\")'>
                                    tableData=tableData+json2.itemLst[i].attribute2;
                                             tableData=tableData+" </td>"

                                             tableData=tableData+"<td >"
                                             tableData=tableData+  json2.itemLst[i].attribute3
                                             tableData=tableData+" </td>"
                                              if(type=="MyReports"){
                                              if(json2.itemLst[i].attribute4!=null&&json2.itemLst[i].attribute4!=undefined&&json2.itemLst[i].attribute4=="R")
                                                  tableData=tableData+"<td id='ReportType"+json2.itemLst[i].attribute1+"'>Report</td>"
                                              else
                                                  tableData=tableData+"<td id='ReportType"+json2.itemLst[i].attribute1+"'>Dashboard</td>"
                                                    }
                                              else
                                             tableData=tableData+"<td>"+json2.itemLst[i].attribute4+"</td>"
                                             tableData=tableData+" <td>"+json2.itemLst[i].attribute5+"</td>"
                                             if(json2.itemLst[i].attribute6!=null&&json2.itemLst[i].attribute6!=undefined){

                                                 tableData=tableData+" <td>"+json2.itemLst[i].attribute6+"</td>"

//                                                 tableData=tableData+" <td>"+json.itemLst[i].attribute61+"</td>"
                               }

                                          tableData=tableData+" </tr>"

                              }
 }


                            for (var i = 0; i < json.itemLst.length; i++)
                              {

                                 tableData=tableData+" <tr>"

                                 if(type=="Dashboard"){
                                 tableData=tableData+"<td style='width:30px'><input type='checkbox' name='DashSelect' id='DashSelect"+(i + 1)+"' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                 tableData=tableData+"<td  id='hyperDash' style='font-size:12px;color:"+color+";cursor:pointer'  onclick='javascript:viewDashboardInStudio(\"dashboardViewer.do?reportBy=viewDashboard&REPORTID="+json.itemLst[i].attribute1+"&pagename="+json.itemLst[i].attribute2+"&editDbrd="+false+"\")'>"
                                 }

                                 else if(type=="Report"){
                                      tableData=tableData+"<td style='width:30px'><input type='checkbox' name='RepSelect' id='RepSelect"+(i + 1)+"' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep"+(i + 1)+"' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewReportinStudio('reportViewer.do?reportBy=viewReport&REPORTID="+json.itemLst[i].attribute1+"&action=open')>"
                                 }
                                  else if(type=="Portals"){
                                      tableData=tableData+"<td style='width:30px'><input type='checkbox' name='PortalSelect' id='PortalSelect"+(i)+"' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep"+(i)+"' style='font-size:12px;color:"+color+";cursor:pointer' onclick=javascript:viewPortlet('"+json.itemLst[i].attribute1+"','portletPreviewDivinDesiner','"+(json.itemLst[i].attribute2).replace(" ", "_").replace(" ", "_")+"')>"
                                 }
                                 else if(type=="Scorecard")
                                     {
                                          tableData=tableData+"<td style='width:30px'><input type='checkbox' name='ScoreSelect' id='ScoreSelect"+(i + 1)+"' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                          tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewScorecard('"+json.itemLst[i].attribute1+"')>"
                                     }
                                     else if(type=="MyReports"){
                                            tableData=tableData+"<td style='width:30px'><input type='checkbox' name='MyRepSelect' id='MyRepSelect"+(i + 1)+"' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                      tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewReport('reportViewer.do?reportBy=viewReport&REPORTID="+json.itemLst[i].attribute1+"&action=open')>"
                                     }
                                      else if(type=="HtmlReports"){

                                         tableData=tableData+"<td style='width:30px'><input type='checkbox' name='HtmlSelect' id='HtmlSelect"+(i + 1)+"' value='"+json.itemLst[i].attribute1+"' onclick='unselAll()'></td>"
                                         tableData=tableData+"<td  id='hyperRep' style='font-size:12px;color:"+color+";cursor:pointer'  onclick=javascript:viewDataSnapshot('"+json.itemLst[i].attribute1+"','"+path+"')>"
                                         tableData=tableData+json.itemLst[i].attribute2;
                                         tableData=tableData+" </td>"
                                         tableData=tableData+"<td>"+json.itemLst[i].attribute1+"</td>"
                                         tableData=tableData+"<td>"+json.itemLst[i].attribute3.substring(0, json.itemLst[i].attribute3.indexOf("."))+"</td>"
                                         if(json.itemLst[i].attribute3=="-1")
                                             tableData=tableData+"<td>-</td>"
                                         else
                                             tableData=tableData+"<td>"+json.itemLst[i].attribute4+"</td>"
                                          tableData=tableData+"<td>"+json.itemLst[i].attribute5.substring(0, json.itemLst[i].attribute3.indexOf("."))+"</td>"
                                          tableData=tableData+"<td>"+json.itemLst[i].attribute6+"</td>"
                                          tableData=tableData+"<td>"+json.itemLst[i].attribute7+"</td>"

                                      }
                                    if(type!="HtmlReports" && type!="sentiment"){
                                             tableData=tableData+json.itemLst[i].attribute2;
                                             tableData=tableData+" </td>"

                                             tableData=tableData+"<td >"
                                             tableData=tableData+  json.itemLst[i].attribute3
                                             tableData=tableData+" </td>"
                                                    if(type=="MyReports"){

                                              if(json.itemLst[i].attribute4!=null&&json.itemLst[i].attribute4!=undefined&&json.itemLst[i].attribute4=="R")
                                                  tableData=tableData+"<td id='ReportType"+json.itemLst[i].attribute1+"'>Report</td>"
                                              else
                                                  tableData=tableData+"<td id='ReportType"+json.itemLst[i].attribute1+"'>Dashboard</td>"
                                                    }
                                              else
                                             tableData=tableData+"<td>"+json.itemLst[i].attribute4+"</td>"
                                             tableData=tableData+" <td>"+json.itemLst[i].attribute5+"</td>"
                                             if(json.itemLst[i].attribute6!=null&&json.itemLst[i].attribute6!=undefined){
                                                 if(type=="Report"){
                                                   tableData=tableData+" <td>"+json.itemLst[i].attribute61+"</td>"
                                                   tableData=tableData+"<td><img width='25px' height='20px' src='/pi/images/csv.gif' style='cursor: pointer;' onclick=\"getReportDeatils('"+json.itemLst[i].attribute1+"')\"></td>"

                                                 }else{
                                                 tableData=tableData+" <td>"+json.itemLst[i].attribute6+"</td>"
                                              }
//                                                 tableData=tableData+" <td>"+json.itemLst[i].attribute61+"</td>"
                               }
                               }
                                     else if(type=="sentiment")
                                    {
                                         tableData=tableData+"<td>"+json.itemLst[i].attribute1;
                                         tableData=tableData+" </td>"

                                         tableData=tableData+"<td>"
                                         tableData=tableData+  json.itemLst[i].attribute2
                                         tableData=tableData+" </td>"
                                         tableData=tableData+"<td align='center'>"
                                         tableData=tableData+ "<img onclick=\"reClassify('"+json.itemLst[i].attribute1+"','"+ json.itemLst[i].attribute2+"')\" src='"+path+"/icons pinvoke/globe.png'  />"
                                         tableData=tableData+" </td>"
                                    }
                                      tableData=tableData+"</tr>"
                              }

                                   tableData=tableData+"</tbody>"

                                   if(length>0)
                                       {
                                   $("#"+id).html('');

                                        $("#"+id).html(tableData);

                                        $("#"+id).tablesorter({headers : {0:{sorter:false}}})
                                        
                                        if(type=="Dashboard")
                                        $("#"+id).tablesorterPager({container: $('#pagerDashboard')})
                                        else if(type=="Report")
                                        $("#"+id) .tablesorterPager({container: $('#pagerReport')})
                                    else if(type=="Scorecard")
                                         $("#"+id) .tablesorterPager({container: $('#pagerScorecard')})
                                     else if(type=="MyReports")
                                          $("#"+id) .tablesorterPager({container: $('#pagerallReps')})
                                      else if(type=="Portals")
                                          $("#"+id) .tablesorterPager({container: $('#selPagerRep')})
                                       else if(type=="HtmlReports")
                                          $("#"+id) .tablesorterPager({container: $('#pagerDataReport')})
                                      else if(type=="sentiment")
                                          $("#"+id) .tablesorterPager({container: $('#pagerClassify')})
                                       
                                   }
                                    return length;

                                }

    function viewScheduler(reportType,reportId,reportName,isEdit)
    {
//        alert(reportType)
        if(reportType=='Scheduler')
        {
            document.forms.schedulerForm.action ="tracker/JSPS/scheduleReport.jsp?schedulerId="+reportId+"&schedulerName="+reportName+"&isEdit="+isEdit;
            document.forms.schedulerForm.submit();
        }
        else if(reportType=='Scorecard')
        {
            document.forms.schedulerForm.action ="scheduler.do?reportBy=editScorecardTracker&schedulerId="+reportId+"&schedulerName="+reportName+"&isEdit="+isEdit;
            document.forms.schedulerForm.submit();
        }
        else
        {
            document.forms.schedulerForm.action ="tracker/JSPS/scheduleTracker.jsp?trackerId="+reportId+"&isEdit="+isEdit;
            document.forms.schedulerForm.submit();

        }


    }

    function deleteScheduler(ctxPath)
    {
        var selTrckrObj=document.getElementsByName("schedulerSelect");
        var deleteSchedulerIds = new Array();
        for(var i=0;i<selTrckrObj.length;i++){
            if(selTrckrObj[i].checked){
                deleteSchedulerIds.push(selTrckrObj[i].value);
            }
            else{
                selTrckrObj[i].checked=false;
            }
        }
        if(deleteSchedulerIds.length!=0){
            var confirmDel=confirm("Do you want to Delete Schedule");
            if(confirmDel==true){
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: ctxPath+'/scheduler.do?reportBy=deleteSchedule&deleteSchedulerids='+deleteSchedulerIds.toString(),
                    success: function(data){
                        document.forms.schedulerForm.action = ctxPath+"/home.jsp#Scheduler";
                        document.forms.schedulerForm.submit();
                    }
                });
            }else{
                document.forms.schedulerForm.action = ctxPath+"/home.jsp#Scheduler";
                document.forms.schedulerForm.submit();
            }
        }else{
            alert("Please select Tracker/Report to be deleted");
        }

    }

    function refreshPortletDesigner(ctxPath){
        document.forms.portletForm.action = ctxPath+"/portalViewer.do?portalBy=viewPortals";//"/  home.jsp#Portals";
        document.forms.portletForm.submit();
    }

    function editPortlet(){
        alert("in edit");
    }

    function sharePortlet(){
        alert("in share");
    }



//    function viewPortlet(portletId, readOnly){
//        //alert("in view : "+portletId+" "+readOnly);
//    }



    function enableMaps(reportId)
    {
                       var flag=false;

                if(parent.$("#enableMapId").val()=="Disable Map") {
                    parent.$("#enableMapId").attr("mapEnabled","false");
                    flag=parent.$("#enableMapId").attr("mapEnabled").valueOf();
                    parent.$("#enableMapId").val("Enable Map");
                    alert("Geo Map has been disabled for this report");
                }
                else{
                   parent.$("#enableMapId").attr("mapEnabled","true");
                    flag=parent.$("#enableMapId").attr("mapEnabled").valueOf();
                   parent.$("#enableMapId").val("Disable Map")
                    alert("Geo Map has been enabled for this report");
                }



                    $.ajax({
                        type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                        url:"reportTemplateAction.do?templateParam=enableMap&reportId="+reportId+"&mapFlag="+flag,
                        success:function(data){
                            if(data!=null){
                            }
                        }
                    });


    }

 function editScheduler()
 {
      var selSchdObj=document.getElementsByName("schedulerSelect");
        var schedulerDetails = new Array();
        for(var i=0;i<selSchdObj.length;i++){
            if(selSchdObj[i].checked){
                schedulerDetails.push(selSchdObj[i].value);
            }
            else{
                selSchdObj[i].checked=false;
            }
        }
        if(schedulerDetails.length==0)
            {
                alert("Please Select Scheduler/Tracker to be edited")
            }
        else if(schedulerDetails.length==1)
        {
            var schedulerArr=schedulerDetails.toString().split("~");
            if(schedulerArr[1]=='Scheduler')
            {
              document.forms.schedulerForm.action ="tracker/JSPS/scheduleReport.jsp?schedulerId="+schedulerArr[0]+"&isEdit="+"true";
              document.forms.schedulerForm.submit();
            }
             else if(schedulerArr[1]=='Scorecard')
            {
              document.forms.schedulerForm.action ="scheduler.do?reportBy=editScorecardTracker&schedulerId="+schedulerArr[0]+"&isEdit="+true;
              document.forms.schedulerForm.submit();
            }
            else if(schedulerArr[1]=='Tracker')
            {
                document.forms.schedulerForm.action ="tracker/JSPS/scheduleTracker.jsp?trackerId="+schedulerArr[0]+"&isEdit="+"true";
                document.forms.schedulerForm.submit();
            }

        }
        else
        {
            alert("Please Select only one Scheduler/Tracker")
        }
 }


function runScheduler(path)
{
    var selectObj=document.getElementsByName("schedulerSelect");
        var scheduler = new Array();
        var scheduleId=new Array();
        var trackerId=new Array();
        var scorecardTrakId=new Array();

        for(var i=0;i<selectObj.length;i++)
        {
            if(selectObj[i].checked){
                scheduler.push(selectObj[i].value);
            }
            else{
                selectObj[i].checked=false;
            }
        }
        if(scheduler.length>0)
        {
            for(var j=0;j<scheduler.length;j++)
            {
                var scheduledType=scheduler[j].split("~");
                 if($.trim(scheduledType[1])=="Scheduler")
                     scheduleId.push(scheduledType[0]);
                 else if($.trim(scheduledType[1])=="Scorecard")
                     scorecardTrakId.push(scheduledType[0]);
                 else
                     trackerId.push(scheduledType[0]);
             }

            if(scheduleId.length!=0 || trackerId.length!=0|| scorecardTrakId.length!=0)
            {
                 var confirmDel=confirm("Do you want to run");
                    if(confirmDel==true)
                    {
                        if(scheduleId.length!=0)
                         {
                            $.ajax({
                                type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: path+'/scheduler.do?reportBy=runReportScheduler&schedulerId='+scheduleId.toString()+'&fromStudio='+true,
                            success: function(data){
                                alert("Report Scheduled");
                                document.forms.schedulerForm.action = path+"/home.jsp#Scheduler";
                                document.forms.schedulerForm.submit();
                            }
                            });
                        }
                        if(trackerId.length!=0)
                        {
//                            alert()
                            $.ajax({
                                type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                                url: path+'/scheduler.do?reportBy=runTracker&trackerId='+trackerId.toString()+'&fromStudio='+true,
                                success: function(data){
                                    alert("Tracker Completed")
                                    document.forms.schedulerForm.action = path+"/home.jsp#Scheduler";
                                    document.forms.schedulerForm.submit();
                                }
                            });
                        }
                        if(scorecardTrakId.length!=0)
                        {
//                            alert()
                            $.ajax({
                                type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                                url: path+'/scheduler.do?reportBy=runScorecardTracker&trackerIds='+scorecardTrakId.toString()+'&from=MetaPage',
                                success: function(data){
                                    alert("Scorecard Tracker Completed")
                                    document.forms.schedulerForm.action = path+"/home.jsp#Scheduler";
                                    document.forms.schedulerForm.submit();
                                }
                            });
                        }
                    }
                    else
                    {
                     document.forms.schedulerForm.action = path+"/home.jsp#Scheduler";
                     document.forms.schedulerForm.submit();

                    }
                }
        }
        else
            {
                alert("Please Select either Scheduler or Tracker")
            }



}

function editDashboard(ctxPath)
{
    var dashbdSelectObj=document.getElementsByName("DashSelect");
    var editdashbdids = new Array();
    for(var i=1;i<dashbdSelectObj.length;i++){
        if(dashbdSelectObj[i].checked){
            editdashbdids.push(dashbdSelectObj[i].value);
        }
        else{
            dashbdSelectObj[i].checked=false;
        }
    }
    if(editdashbdids.length!=0){
        if(editdashbdids.length<2){
            var dashbdId=editdashbdids.pop();
            var path = ctxPath+'/dashboardViewer.do?reportBy=viewDashboard&REPORTID='+dashbdId+'&editDbrd='+true;
            document.forms.dashboardForm.action = path;
            document.forms.dashboardForm.submit();
        }
        else{
                alert("Please select only one Dashboard")
            }
    }
    else{
        alert("Please select Dashboard ")
    }
}

 function viewScorecardScheduler()
 {
    document.forms.schedulerForm.action ="scoreCardScheduler.jsp?mode=create";
    document.forms.schedulerForm.submit();
 }

function compareReports(path){
        var RepSelectObj=document.getElementsByName("RepSelect");
        var compareRepIds = new Array();
        for(var i=1;i<RepSelectObj.length;i++){
            if(RepSelectObj[i].checked){
                compareRepIds.push(RepSelectObj[i].value);
            }
            else{
                RepSelectObj[i].checked=false;
            }
        }
        if(compareRepIds.length!=0){
            if(compareRepIds.length==2){
                var firstRepId=compareRepIds[0];
                var secondRepId=compareRepIds[1];
                    $.ajax({
                        type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                        url: "reportTemplateAction.do?templateParam=prepareReportsToCompare&firstRepId="+firstRepId+"&secondRepId="+secondRepId,
                        success: function(data){
                            if(data==""){
                                alert("Cannot compare reports - RowViewBy must be same to compare")
                            }
                            else{
//                                alert("ajax")
                                 document.forms.reportForm.action = "reportTemplateAction.do?templateParam=getReportsToCompare&firstRepId="+firstRepId+"&secondRepId="+secondRepId;
                                document.forms.reportForm.submit();
//                                $.post(
//                                         "reportTemplateAction.do?templateParam=getReportsToCompare&firstRepId="+firstRepId+"&secondRepId="+secondRepId,$("#reportForm").serialize(),
//                                         function(data){
//                                        }
//                                    );
                            }
    //                        document.forms.reportForm.action = path+"/home.jsp#Report_Studio";
    //                        document.forms.reportForm.submit();
                        }
                    });
            }
            else{
                alert("please select 2 reports for comparison")
            }
        }else{
            alert("Please select Report(s)")
        }
}
   function getReportDeatils(reportid){   
       //alert(reportid)
         $.post("reportTemplateAction.do?templateParam=getReportDetailsList&reportId="+reportid,
         function(data){
            if(data!=null){
                 var jsonVar=eval('('+data+')')

                 var data="<table align='center'><tr><td>Report Name</td><td><input type='text' value='"+jsonVar.ReportName+"' readonly></td></tr>\n\
                            <tr><td>Report Description</td><td><input type='text' value='"+jsonVar.ReportDesc+"' readonly></td></tr>\n\
                            <tr><td>Business Role</td><td><input type='text' value='"+jsonVar.BusinessRole+"' readonly></td></tr>\n\
                            <tr><td>Created On</td><td><input type='text' value='"+jsonVar.CreatedOn+"' readonly></td></tr>\n\
                            <tr><td>Created By</td><td><input type='text' value='"+jsonVar.CreatedBy+"' readonly></td></tr>\n\
                            <tr><td>LastUpdate On</td><td><input type='text' value='"+jsonVar.LastUpdateOn+"' readonly></td></tr>\n\
                            <tr><td>LastUpdate By</td><td><input type='text' value='"+jsonVar.LastUpdateBy+"' readonly></td></tr></table>";

                $("#ReportDetails").html(data)
                $("#ReportDetails").dialog('open');
                 //alert(jsonVar.toString())

             }
            }
         );

}
function retrieveFromBkp(path){
    var RepSelectObj=document.getElementsByName("RepSelect");
                var clearcacheids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        clearcacheids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
           viewReportinStudio("reportViewer.do?reportBy=viewReport&REPORTID="+clearcacheids.toString()+"&action=open"+"&fromBKP=true")
}


function saveReportNameAO()
{   // repName=repName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
//                repName=encodeURIComponent(repName);
//                var folders=buildFldIds();
var reportName = document.getElementById('newreportNameAO').value;

   $.ajax({
                    url: 'reportTemplateAction.do?templateParam=checkReportNameatAOLevel&repName='+reportName+"&fromRepDesigner=fromrepDesigner",
                    success: function(data){

                        if(data==1){
                            saveReportAO();
                        }
                        else{
                            alert('Report Name Already Exists,Enter New Reportname')
//                            initialog();
//                            repExist='Y';
//                            $("#changeNameDialog").dialog('open');
                        }
                    }
                });
}


//Start of code By Bhargavi Parsi on 30th nov 2015
function saveReportAO(){
   // document.getElementById('aoFlag').value="true";
   var aoFlag= document.getElementById('aoFlag').value;
    var reportName = document.getElementById('newreportNameAO').value;
                //reportName=reportName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var reportDesc = document.getElementById('newreportDescAO').value;
                //reportDesc=reportDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                reportName=encodeURIComponent(reportName);
                reportDesc=encodeURIComponent(reportDesc);
                $("#createReportwithAO").dialog('close');
                if(reportName==''){
                    alert("Please enter Report Name");
                }
                else{
                $.ajax({
                    async:false,
                      url:"reportTemplateAction.do?templateParam=goToCreateReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc+"&aoFlag="+aoFlag,
                       success:function(data){
            $("#selectAO").html(data);
            $("#selectAO").dialog('open');
        }
                  });
                }
}
//end of code by Bhargavi
function saveReport1(){     
                var reportName = document.getElementById('newreportName').value;
                //reportName=reportName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var reportDesc = document.getElementById('newreportDesc').value;
                //reportDesc=reportDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                reportName=encodeURIComponent(reportName);
                reportDesc=encodeURIComponent(reportDesc);
                $("#createReportNew").dialog('close');
                if(reportName==''){
                    alert("Please enter Report Name");
                }
//                else  if(reportDesc==''){
//                    alert("Please enter Report Description")
//                }
                else{

                    $.ajax({
                    url: "reportTemplateAction.do?templateParam=goToCreateReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc,
                    success: function(data){
                        $("#selectBussRole").html(data);
                        $("#selectBussRole").dialog('open');
                    }
                });
//                                document.forms.searchForm.action = "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc;
//                                document.forms.searchForm.method="POST";
//                                document.forms.searchForm.submit();
             }
         }
                  function creationOfReport(repId){
         var fldObj=document.getElementsByName("userfldsList");
         var foldersIds="";

        if(fldObj.length!=0){
         for(var i=0;i<fldObj.length;i++){
            if(fldObj[i].checked){
                foldersIds=foldersIds+","+fldObj[i].id;
            }
        }
      }
    if (foldersIds!=""){
        foldersIds=foldersIds.substr(1,foldersIds.length);
             document.forms.searchForm.action = "reportTemplateAction.do?templateParam=selectRoleGoToDesin&repId="+repId+"&roleId="+foldersIds;
             document.forms.searchForm.method="POST";
             document.forms.searchForm.submit();
         }
    else{
        $("#selectBussRole").dialog('close');
        alert("Please select one Business Role");             
        $("#selectBussRole").dialog('open');

    }

}


function  createKPIDashboard(){
    parent.createKPIDash1();


}
 function saveKPIDashboard(){
     var action =parent.$("#timedash").val();
                 var dashboardName = document.getElementById('kpidashboardName').value;
               // dashboardName=dashboardName.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                var dashboardDesc = document.getElementById('kpidashboardDesc').value;
                //dashboardDesc=dashboardDesc.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                    url: 'dashboardTemplateAction.do?templateParam2=checkDashboardName&dashboardName='+encodeURIComponent(dashboardName)+'&dashboardDesc='+encodeURIComponent(dashboardDesc),
                    success: function(data){

                        if(data!=""){
                            document.getElementById('duplicatekpiDashboard').innerHTML = data;
                            document.getElementById('kpidashboardsave').disabled = true;
                        }
                        else if(data==''){
                              $.ajax({
                                    type:'GET',
                                    url: "dashboardTemplateAction.do?templateParam2=goToKPIDashboardDesigner&dashboardName="+encodeURIComponent(dashboardName)+"&reportDesc="+encodeURIComponent(dashboardDesc)+"&iskpidashboard=true&Timedash="+action,
                                  success: function(data){

                                   parent.$("#CreateKPIdash").dialog('close');
                                   $("#selectkpiBussRole").html(data);
                                   $("#selectkpiBussRole").dialog('open');

                              }

                          });

                        }
                    }
                });
            }



function creationOfKPIDashboard(repId,cntxtpath){

  var action =parent.$("#timedash").val();
//  alert(action)
         var fldObj=document.getElementsByName("userfldsList");
         var foldersIds="";

        if(fldObj.length!=0){
         for(var i=0;i<fldObj.length;i++){
            if(fldObj[i].checked){
                foldersIds=foldersIds+","+fldObj[i].id;
            }
        }
      }

    if (foldersIds!=""){
         foldersIds=foldersIds.substr(1,foldersIds.length);
        $("#selectkpiBussRole").dialog('close');
        parent. $("#Createdash1").dialog('close');

          document.forms.kpidashform.action = "dashboardTemplateAction.do?templateParam2=selectRoleGoToKPiDashbrdDesin&repId="+repId+"&roleId="+foldersIds+"&timedash="+action,
          document.forms.kpidashform.method="POST";
          document.forms.kpidashform.submit();
         }
    else{
        $("#selectkpiBussRole").dialog('close');
        $("#selectkpiBussRole").dialog('open');

    }

}
function creationOfTimeBasedDashboard(repId,cntxtpath){


         var fldObj=document.getElementsByName("userfldsList");
         var foldersIds="";

        if(fldObj.length!=0){
         for(var i=0;i<fldObj.length;i++){
            if(fldObj[i].checked){
                foldersIds=foldersIds+","+fldObj[i].id;
            }
        }
      }

    if (foldersIds!=""){
         foldersIds=foldersIds.substr(1,foldersIds.length);
        $("#selectkpiBussRole").dialog('close');
       // parent. $("#Createdash1").dialog('close');

          document.forms.timedashForm.action = "dashboardTemplateAction.do?templateParam2=selectRoleGoToTimeDashbrdDesin&repId="+repId+"&roleId="+foldersIds,
          document.forms.timedashForm.method="POST";
          document.forms.timedashForm.submit();
         }
    else{
        $("#selectkpiBussRole").dialog('close');
        $("#selectkpiBussRole").dialog('open');

    }

}
//added by krishan
function saveReport2(){
//alert("save report2 call")
           var timeDetail = "Time-Period Basis";
           var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                var reportName = document.getElementById('newreportName1').value;
                //reportName=reportName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var reportDesc = document.getElementById('newreportDesc1').value;
                //reportDesc=reportDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                reportName=encodeURIComponent(reportName);
                reportDesc=encodeURIComponent(reportDesc);
                $("#createReportNew1").dialog('close');
                if(reportName==''){
                    alert("Please enter Report Name");
                }
//                else  if(reportDesc==''){
//                    alert("Please enter Report Description")
//                }
                else{

                    $.ajax({
                    url: "reportTemplateAction.do?templateParam=goToCreateReportDesigner1&reportName="+reportName+"&reportDesc="+reportDesc+'&dimId='+timeDetail+'&timeparams='+timeDim,
                    success: function(data){
                        $("#selectBussRole").html(data);
                        $("#selectBussRole").dialog('open');
                    }
                });
//                                document.forms.searchForm.action = "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc;
//                                document.forms.searchForm.method="POST";
//                                document.forms.searchForm.submit();
             }
         }

function saveReport3(){
//alert("save report3 call")
            var timeDetail = "Time-Period Basis";
           var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                var reportName = document.getElementById('newreportName2').value;
                //reportName=reportName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var reportDesc = document.getElementById('newreportDesc2').value;
                //reportDesc=reportDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                reportName=encodeURIComponent(reportName);
                reportDesc=encodeURIComponent(reportDesc);
                $("#createReportNew").dialog('close');
                if(reportName==''){
                    alert("Please enter Report Name");
                }
//                else  if(reportDesc==''){
//                    alert("Please enter Report Description")
//                }
                else{

                    $.ajax({
                    url: "reportTemplateAction.do?templateParam=goToCreateReportDesigner2&reportName="+reportName+"&reportDesc="+reportDesc+'&dimId='+timeDetail+'&timeparams='+timeDim,
                    success: function(data){
                        $("#selectBussRole").html(data);
                        $("#selectBussRole").dialog('open');
                    }
                });
//                                document.forms.searchForm.action = "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc;
//                                document.forms.searchForm.method="POST";
//                                document.forms.searchForm.submit();
             }
         }

  function goToSelectedComponent1(path)
            {
                // alert("goToSelectedComponent1")
                var componentMethod=$("#reportsComponent").val();
                var advancedHtml='fromAdvancedHtml';
                if(componentMethod=="CREATE_REPORT")
//                    createnewReport();
                  CreateReportNew1();              // Added by Mayank.

                else if(componentMethod=="EDIT_REPORT_NAME")
                    EditReportName();

                else if(componentMethod=="DELETE_REPORT")
                    deleteReport(path);

                else if(componentMethod=="PURGE_REPORT")
                    purgeReport(path);

                else if(componentMethod=="CLEAR_CACHE")
                    clearCache(path);

                else if(componentMethod=="DOWNLOAD")
                    downloadAsSnapshotInReports();

                else if(componentMethod=="SAVE_AS_HTML")
                   openSnapShotDiv('basicHtml');

                else if(componentMethod=="COPY_REPORT")
                    copyReport();
                else if(componentMethod=="SHOW_METADATA")
                    showMetaData_RS(path);
                else if(componentMethod=="COMPARE_REPORTS")
                    compareReports(path);
                else if(componentMethod=="SAVE_AS_ADVANCED_HTML")
                    openSnapShotDiv('fromAdvancedHtml');
                else if(componentMethod=="RETRIEVE_FROM_BKP")
                    retrieveFromBkp(path);
                //else if(componentMethod=="ADHOC_REPORT"){     //commented by mayank..
                  //  CreateReportNew();


            }

            function goToSelectedAOComponent(path)
            {
    var componentMethod = $("#reportsComponent").val();
    var advancedHtml = 'fromAdvancedHtml';
    if (componentMethod == "CREATE_AO") {
//      createnewReport();
        CreateAONew();// Added by Amar.
    } else if (componentMethod == "EDIT_AO") {
        EditAO(path);//added by Diananth
    } else if (componentMethod == "DELETE_AO") {
        deleteAOandReports(path);
            } else if (componentMethod == "SCHEDULE_AO") {
        scheduleAO(path);
            } else if (componentMethod == "EDIT_SCHEDULE_AO") {
        editScheduleAO(path);
            }
            }
function saveReportAO1()
{   // repName=repName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
//                repName=encodeURIComponent(repName);
//                var folders=buildFldIds();
var reportName = document.getElementById('newAOName').value;

   $.ajax({
                    url: 'reportTemplateAction.do?templateParam=checkReportNameatAOCreateLevel&repName='+reportName+"&fromRepDesigner=fromrepDesigner",
                    success: function(data){

                        if(data==1){
                            saveAO();
                        }
                        else{
                            alert('AO Name Already Exists,Enter New AO Name')
//                            initialog();
//                            repExist='Y';
//                            $("#changeNameDialog").dialog('open');
                        }
                    }
                });
}

            function saveAO(){
//alert("save report2 call")
           var timeDetail = "Time-Period Basis";
           var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
//           alert(document.getElementById('newAOName'));
//           alert(document.getElementById('newAOName').value);
                var aoName = document.getElementById('newAOName').value;
//                alert(aoName);
                //reportName=reportName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var aoDesc = document.getElementById('newAODesc').value;
                //reportDesc=reportDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                aoName=encodeURIComponent(aoName);
                aoDesc=encodeURIComponent(aoDesc);
                $("#createAONew").dialog('close');
                if(aoName==''){
                    alert("Please enter AO Name");
                }
//                else  if(reportDesc==''){
//                    alert("Please enter Report Description")
//                }
                else{

                    $.ajax({
                    url: "reportTemplateAction.do?templateParam=goToCreateAODesigner&aoName="+aoName+"&aoDesc="+aoDesc+"&dimId="+timeDetail+"&timeparams="+timeDim,
                    success: function(data){
                        $("#selectBussRole").html(data);
                        $("#selectBussRole").dialog('open');
                    }
                });
//                                document.forms.searchForm.action = "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc;
//                                document.forms.searchForm.method="POST";
//                                document.forms.searchForm.submit();
             }
         }
//              function createnewReport(){
//                parent.createReport(false);
//            }
                 function CreateReportNew1(){
                $("#createReportNew1").dialog('open');
            }



function createManagementDashboard(path){
    parent.$("#createMgmtDashboard").dialog().dialog('open');
}
