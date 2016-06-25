/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var folderId="";
//added by susheela start
var subFolderId="";
var dimId="";
var sFolder="";
var memId="";
var subFassignAccDimSecolderId="";
var dimTabId="";
var subFolId="";
var subFolIdView="";
var roleEleId = "";
var roleFolderId = "";
var roleDimensionId = "";
var userEleId = "";
var userFolderId = "";
var userDimensionId = "";

var subTargetFolder="";
//added by susheela start 02-12-09
var subFolderUserId="";
var subFolderIdUser="";
var userId="";
var userDimId="";
var mbrName="";

//added by chiranjeevi start
var accountFolId="";
var orgId="";

var userIdForAcc="";
var isMemberUseInOtherLevel="false"
var FolderId="";

function updateTargetMeasure(val)
{
    //alert(' lll '+val);
    var all=val.split(":");
    var colId=all[0];
    var roleId=all[1];
    var tabId=all[2];
    // alert(roleId+' tabId '+tabId+" colId "+colId);
    $.ajax({
        url: 'userLayerAction.do?userParam=updateTargetMeasureForRole&tabId='+tabId+'&colId='+colId+'&roleId='+roleId,
        success: function(data) {
        // alert(data)

        }
    });
}

function refreshAccountParent()
{
    window.location.reload(true);
}
function cancelSelectValsAccParent()
{
    document.getElementById("selectAccountDimValuesData").style.display='none';
    document.getElementById('fade').style.display='none';
}


// susheela start 10-12-09
function cancelSelectValsParentRole()
{
    document.getElementById("selectRoleDimValuesData").style.display='none';
    document.getElementById('fade').style.display='none';
}
// susheela over 10-12-09
function cancelSelectValsParent()
{
    $("#selectUserDimValuesDataDiv").dialog('close')
//    document.getElementById("selectUserDimValuesData").style.display='none';
//    document.getElementById('fade').style.display='none';
}
function cancelViewUserDrillParent()
{
    document.getElementById("viewUserDrillData").style.display='none';
    document.getElementById('fade').style.display='none';
// document.getElementById("viewDrillDiv").innerHTML="";
}
function cancelEditUserDrillParent()
{
    document.getElementById("editUserDrillData").style.display='none';
    document.getElementById('fade').style.display='none';
}
//added by susheela over 02-12-09


//added by susheela start 28-11-09
function getTargetMessage(){
    //alert(' in tMsg ')
    $.ajax({
        url: 'userLayerAction.do?userParam=getBussRoleForTarget&subTargetFolder='+subTargetFolder,
        success: function(data) {
          //  alert(data)

        }
    });
}

$(document).ready(function(){
    if ($.browser.msie == true){
        $("#createAcc").dialog({
            autoOpen: false,
            height: 380,
            width: 620,
            position: 'justify',
            modal: true
        });

        $("#assignAccDiv").dialog({
            autoOpen: false,
            height: 380,
            width: 620,
            position: 'justify',
            modal: true
        });
        $("#formulaViewDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 150,
            width: 250,
            position: 'absolute',
            modal: true
        });
         $("#editDrillDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 350,
            width: 400,
            position: 'absolute',
            modal: true
        });
         $("#viewDrillDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 350,
            width: 400,
            position: 'absolute',
            modal: true
        });

    }
    else{
        $("#createAcc").dialog({
            autoOpen: false,
            height: 400,
            width: 620,
            position: 'justify',
            modal: true
        });
        $("#assignAccDiv").dialog({
            autoOpen: false,
            height: 380,
            width: 620,
            position: 'justify',
            modal: true
        });
        $("#formulaViewDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 150,
            width: 250,
            position: 'absolute',
            modal: true
        });
          $("#editDrillDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 350,
            width: 400,
            position: 'absolute',
            modal: true
        });
         $("#viewDrillDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 350,
            width: 400,
            position: 'absolute',
            modal: true
        });

    }
});
function closeAccount(){
    $("#createAcc").dialog('close');
}
function makeTargetFolder()
{
    //subTargetFolder
    $.ajax({
        url: 'userLayerAction.do?userParam=checkBussRoleForTarget&subTargetFolder='+subTargetFolder,
        success: function(data) {
            //alert('data '+data)
            if(data==1){
                //alert('in 1 ');
                getTargetMessage();

            }
            else if(data==2){
                $("#warning").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    resizable: false,
                    height:140,
                    position:'top',
                    modal: true,
                    overlay: {
                        backgroundColor: '#000',
                        opacity: 0.5
                    }
                });
                $('#warning').dialog('open');
                $("#alert").html("The Role Is Available For Target.");
            // window.location.reload(true);

            }

        }
    });
}
//added by susheela over 28-11-09

function cancelDrillDiv()
{
    document.getElementById("DimenionDrillData").style.display='none';
    document.getElementById('fade').style.display='none';
//  document.getElementById("editDrillDiv").innerHTML="";
}
function cancelViewDrillParent()
{
     $("#viewDrillDiv").dialog('close')
//    document.getElementById("viewDrillData").style.display='none';
//    document.getElementById('fade').style.display='none';
// document.getElementById("viewDrillDiv").innerHTML="";
}
function saveDrillDiv()
{
    $("#editDrillDiv").dialog('close')
//    document.getElementById("DimenionDrillData").style.display='none';
//    document.getElementById('fade').style.display='none';
    window.location.reload(true);
}

function deleteDimension(dimId,subFolderId)
{
    $.ajax({
        url: 'userLayerAction.do?userParam=deleteRoleDimension&dimId='+dimId+'&subFolderId='+subFolderId,
        success: function(data) {
            if(data=="false")
                alert("Unable to Delete Dimension")
            else{
                $("#warning").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    resizable: false,
                    height:140,
                    position:'top',
                    modal: true,
                    overlay: {
                        backgroundColor: '#000',
                        opacity: 0.5
                    }
                });
                $('#warning').dialog('open');
                $("#alert").html("Dimension Deleted Successfully.");
                window.location.reload(true);

            }

        }
    });
}

function deleteDimensionMember(sFolder,memId,dimTabId)
{
    $.ajax({
        url: 'userLayerAction.do?userParam=deleteRoleDimensionMember&sFolder='+sFolder+'&memId='+memId+'&dimTabId='+dimTabId,
        success: function(data) {
            if(data=="false")
                alert("Unable to Delete Dimension Member")
            else{
                $("#warning").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    resizable: false,
                    height:140,
                    position:'top',
                    modal: true,
                    overlay: {
                        backgroundColor: '#000',
                        opacity: 0.5
                    }
                });
                $('#warning').dialog('open');
                $("#alert").html("Dimension Member Deleted Successfully.");
                window.location.reload(true);

            }

        }
    });
}
//added by susheela over


$(document).ready(function() {
    $(".dimensionDiv").contextMenu(
    {
        menu: 'deleteDimension',
        leftButton: true
    }, function(action,el,pos) {
        var fullVal=el.attr('id');
        var str = fullVal.split(':');
        dimId = str[0];
        subFolderId= str[1];
        FolderId=str[2];

        // alert(dimId+" .. "+subFolderId);
        contextMenuWork(action,el.parent("li"), pos);

    });
    
    //added on 02-12-09 satrt
    $(".dimensionUserDiv").contextMenu(
    {
        menu: 'userDimension',
        leftButton: true
    }, function(action,el,pos) {
        var fullVal=el.attr('id');
        var str = fullVal.split(':');
        //subFolderIdUser userDimId
        userDimId = str[0];
        subFolderIdUser= str[1];
        userId =str[2];
        mbrName = str[3];
        userEleId = str[4];
        userDimensionId = str[5];        
        
        //alert(dimId+" .. "+fullVal);
        contextMenuWork(action,el.parent("li"), pos);

    });
    //added on 02-12-09 over

    $("#dimensionDialog").dialog({
        bgiframe: true,
        autoOpen: false,
        resizable: false,
        height:140,
        position:'top',
        modal: true,
        overlay: {
            backgroundColor: '#000',
            opacity: 0.5
        },

        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'Clear Selected Dimension': function() {
                $(this).dialog('close');
                deleteDimension(dimId,subFolderId);

            }
        }
    });

    $(".dimensionMemberDiv").contextMenu(
    {
        menu: 'deleteDimensionMember',
        leftButton: true
    }, function(action,el,pos) {
        var fullVal=el.attr('id');
        var str = fullVal.split(':');
        sFolder = str[0];
        memId= str[1];
        dimTabId= str[2];
        roleEleId = str[3];
        roleFolderId = str[4];
        roleDimensionId = str[5];
        
        // alert('in  kjk ');
        // alert(dimId+" .. "+subFolderId);
        contextMenuWork(action,el.parent("li"), pos);

    });

    $("#dimensionMemberDialog").dialog({
        bgiframe: true,
        autoOpen: false,
        resizable: false,
        height:150,
        position:'top',
        modal: true,
        overlay: {
            backgroundColor: '#000',
            opacity: 0.5
        },

        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'Delete Dimension member': function() {
                $(this).dialog('close');
                deleteDimensionMember(sFolder,memId,dimTabId);

            }
        }
    });

    $(".dimDiv").contextMenu(
    {
        menu: 'showDimensionDrillEdit',
        leftButton: true
    }, function(action,el,pos) {
        subFolId=el.attr('id');
        //alert('here in drill edit '+subFolId)
        contextMenuWork(action,el.parent("li"), pos);

    });


    //added by chiranjeevi 24-12-09 for accounts

    $(".accountDiv").contextMenu({
        menu: 'showAccounts',
        leftButton: true
    }, function(action,el,pos) {
        accountFolId=el.attr('id');
        //alert('here in drill edit '+subFolId)
        contextMenuWork(action,el.parent("li"), pos);

    });
    $(".accountMenu").contextMenu({
        menu: 'assignAccCont',
        leftButton: true
    }, function(action,el,pos) {
        orgId=el.attr('id');
        //alert('here in drill edit '+subFolId)
        contextMenuWork(action,el.parent("li"), pos);

    });
    $(".accountDimMenu").contextMenu({
        menu: 'assignAccDimSec',
        leftButton: true
    }, function(action,el,pos) {
        var all = el.attr('id');
        //alert('all '+all);
        var ind=all.split(":");
        subFolderId=ind[0];
        memId=ind[1];
        orgId=ind[2];
        mbrName=ind[3];
        //alert('here in drill edit '+subFolId)
        contextMenuWork(action,el.parent("li"), pos);

    });

    //added on 13-jan-10 by susheela start
    $(".accountUserMenu").contextMenu({
        menu: 'showAccountUsers',
        leftButton: true
    }, function(action,el,pos) {
        userIdForAcc = el.attr('id');
        subFolderUserId= el.attr('id');

        contextMenuWork(action,el.parent("li"), pos);

    });
    //added on 13-jan-10 by susheela over


    $(".UserFolderMenu").contextMenu({
        menu: 'userFolderMenu',
        leftButton: true
    }, function(action, el, pos) {
        contextMenuUserFolder(action,el.parent("li"), pos);
    });
    //added by chiranjeevi over 24-12-09 for accounts

    //added by susheela 02-12-09 start
    $(".userDrillMenu").contextMenu(
    {
        menu: 'showUserDrillEdit',
        leftButton: true
    }, function(action,el,pos) {
        subFolderUserId=el.attr('id');
        // alert('here in drill edit user '+subFolderUserId)
        contextMenuWork(action,el.parent("li"), pos);
    });

    $(".formulaMenu").contextMenu({
        menu: 'FormulaMenuList',
        leftButton: true
    }, function(action, el, pos) {
        contextMenuFormula(action,el, pos);
    });
    //added by sushela 02-12-09 over

    function contextMenuFormula(action, el, pos) {

        switch (action) {
            case "addFormulafact":
            {

                var foldersubId=el.attr("id");
                //  alert('in alert '+foldersubId)
                document.getElementById("roleFormulaDialog").innerHTML = "<iframe width=500 height=500 frameborder=0 src=busRoleAddFormula.jsp?foldertabId="+foldersubId+"></iframe>";
                $('#roleFormulaDialog').dialog('open');

                break;
            }

        }
    }

    $(".UserFolderMenu").contextMenu({
        menu: 'userFolderMenu',
        leftButton: true
    }, function(action, el, pos) {
        contextMenuUserFolder(action,el.parent("li"), pos);
    });
    $(".userFilterFormulaMenu").contextMenu({
        menu: 'userFilterFormulaMenu',
        leftButton: true
    }, function(action, el, pos) {
        contextMenuUserFolderFromulaFiter(action,el.parent("li"), pos);
    });
    $(".userdelete").contextMenu({
        menu: 'userdelete',
        leftButton: true
    }, function(action, el, pos) {
        contextMenuUserdelete(action,el.parent("li"), pos);
    });


    $("#deleteUserFolder").dialog({
        bgiframe: true,
        autoOpen: false,
        height:140,
        modal: true,
        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'delete Business Role': function() {
                deleteUserFolder();
                $(this).dialog('close');

            }
        },
        close: function() {

        }
    });

    //added by 28-11-09 susheela start
    $("#makeTargetFolder").dialog({
        bgiframe: true,
        autoOpen: false,
        height:140,
        modal: true,
        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'Make It Available For Target': function() {
                makeTargetFolder();
                $(this).dialog('close');

            }

        },
        close: function() {

        }
    });

    //added by susheela over

    $("#filterDialog").dialog({
        //bgiframe: true,
        autoOpen: false,
        height: 500,
        width: 560,
        position: 'top',
        modal: true
    });
    $("#roleFormulaDialog").dialog({
        //bgiframe: true,
        autoOpen: false,
        height:500,
        width: 560,
        position: 'top',
        modal: true
    });
    //added by susheela start 04-12-09
    $("#filterUserDialog").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 580,
        width: 560,
        position: 'top',
        modal: true
    });
    //added by susheela over 04-12-09

    $("#publishUserFolder").dialog({
        bgiframe: true,
        autoOpen: false,
        height:140,
        modal: true,
        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'publish Business Role': function() {
                publishUserFolder();
                $(this).dialog('close');
            }
        },
        close: function() {

        }
    });
    $("#copyUserFolder").dialog({
        bgiframe: true,
        autoOpen: false,
        height:220,
        modal: true,
        buttons: {
            'Copy Business Role': function() {
                copyUserFolder();
                $(this).dialog('close');
            },
            Cancel: function() {
                $("#folderName").val('');
                $("#folderDesc").val('');
                $(this).dialog('close');
            }
        },
        close: function() {
            $("#folderName").val('');
            $("#folderDesc").val('');
        }
    });
    $("#userAssigned").dialog({
        autoOpen: false,
        height: 550,
        width: 700,
        position: 'justify',
        modal: true
    });
    $("#selectUserDimValuesDataDiv").dialog({
        autoOpen: false,
        height: 550,
        width: 700,
        position: 'justify',
        modal: true
    });

    $("#selectConnection").dialog({
        bgiframe: true,
        autoOpen: false,
        height:250,
        width:350,
        modal: true,
        buttons: {
            Cancel: function() {
                // var x=confirm('To See Dimensions Please Select Connection Are You Sure To Cancel')
                // if(x==true){
                $(this).dialog('close');
            // }
            },
            'Select': function() {
                saveTablesusercon();
                $(this).dialog('close');
            }
        },
        close: function() {

        }
    });

});


$(function() {
    $("#userFolderTree").treeview({
        animated: "normal",
        unique:true
    });
});

//added by susheela start
function contextMenuWork(action, el, pos) {
    var count=9;
    switch (action) {

        case "deleteDimension":
        {
            if(count>0){
                $('#dimensionDialog').dialog('open');
                count=0;
            }
            else{
                //alert("in else")
                $('#warning').dialog('open');
                $("#alert").html("New Message")
            }
            break;
        }
           case "partialPublish":
           {
            var fullVal=el.attr('id');
            var str = fullVal.split(':');

            var dimid = str[0];
            var subfolderId= str[1];
            var folderid=str[2];
           partialPublish(dimid,subfolderId,FolderId)

            break;
        }

        case "deleteDimensionMember":
        {
            if(count>0){
                $('#dimensionMemberDialog').dialog('open');
                count=0;
            }
            else{
                //alert("in else")
                $('#warning').dialog('open');
                $("#alert").html("New Message")
            }
            break;
        }
        case "showDimensionDrill":
        {
             $("#editDrillDiv").dialog('open')
            var frameObj=document.getElementById("DimenionDrillData");
//            frameObj.style.display='block';
//            document.getElementById('fade').style.display='block';
            // alert('in edit subFolId '+subFolId);
            var source = "pbEditDimensionRoleDrill.jsp?subFoldId="+subFolId;
            frameObj.src=source;
            break;
        }
        case "viewDimensionDrill":
        {
             $("#viewDrillDiv").dialog('open')
            var frameObj2=document.getElementById("viewDrillData");
//            frameObj2.style.display='block';
            //alert('in view drill.');
            //document.getElementById('fade').style.display='block';
            //  alert('in view subFolId '+subFolId);
            var source2 = "pbViewDimensionRoleDrill.jsp?subFoldId="+subFolId;
            frameObj2.src=source2;
            break;
        }

        //added by susheela start 02-12-09
        case "editUserDimensionDrill":
        {
            //alert('subFolderUserId '+subFolderUserId);
            var frameObj2=document.getElementById("editUserDrillData");
            frameObj2.style.display='block';
            document.getElementById('fade').style.display='block';
            var source2 = "pbEditUserRoleDrill.jsp?subFolderUserId="+subFolderUserId;
            frameObj2.src=source2;
            break;
        }
        case "viewUserDimensionDrill":
        {
            // alert('subFolderUserId '+subFolderUserId);
            var frameObj2=document.getElementById("viewUserDrillData");
            frameObj2.style.display='block';
            document.getElementById('fade').style.display='block';
            var source2 = "pbViewUserRoleDrill.jsp?subFolderUserId="+subFolderUserId;
            frameObj2.src=source2;
            break;
        }
        case "restrictAccess":
        {
            subFolderIdUser=$("#"+el.attr("id")+ " input").attr("id")
            userDimId=memId
            
            mbrName=$("#"+el.attr("id")+ " span font").html()
            $("#accessLevel").val("roleLevel")
            $("#subFolderIdUser").val($("#"+el.attr("id")+ " input").attr("id"))
            $("#userMemId").val(memId)
            
            mbrName=$("#"+el.attr("id")+ " span font").html()
            $("#mbrName").val(mbrName)
            

            restrictAccessFun()
            //
            //            var frameObj2=document.getElementById("selectRoleDimValuesData");
            //            frameObj2.style.display='block';
            //            document.getElementById('fade').style.display='block';
            //            var source2 = "pbRoleSelectDimMembers.jsp?memId="+memId+"&sFolder="+sFolder+"&dimTabId="+dimTabId;
            //            frameObj2.src=source2;
            break;
        }
        case "userDimension":
        {
                     $("#accessLevel").val("userLevel")
               $("#userId").val(userId)
                 $("#subFolderIdUser").val(subFolderIdUser)
                 $("#userMemId").val(userDimId)
                 $("#mbrName").val(mbrName)
            restrictAccessFun();

            break;
        }

        //added by susheela over 02-12-09
        //added by susheela over 02-12-09
        //added by chiranjeevi on 24-12-09 for accounts start
        case "showCreateAccount":
        {
            var frameObj2=document.getElementById("createAccData");
            var source2 = "createOrganisation.jsp?accountFolderId="+accountFolId;
            frameObj2.src=source2;
            $("#createAcc").dialog('open');

            break;
        }
        case "assignAccount":
        {
            //alert("in asasign account....");
            var frameObj = document.getElementById("assignAccFrame");
            var source = "assignAccounts.jsp?orgId="+orgId;
            frameObj.src = source;
            $("#assignAccDiv").dialog('open');
            break;
        }
        case "expireAccount":
        {
            $.ajax({
                url: 'organisationDetails.do?param=verifyExpireOrg&orgId='+orgId,
                success: function(data) {
                    // alert("data is"+data)
                    if(data=='ok'){
                        // alert('Account Expired.');
                        var r=confirm("Confirm Expire");
                        if (r==true){
                            saveExpireAccount();
                        }
                    }else{
                        alert(data);
                    }
                }
            });


            break;
        }
        case "getaccessAccount":
        {
            //alert(orgId+' in acco accesss '+memId+" ';';' "+mbrName);
            var frameObj2=document.getElementById("selectAccountDimValuesData");
            frameObj2.style.display='block';
            document.getElementById('fade').style.display='block';
            var source2 = "pbAccountSelectDimMembers.jsp?orgId="+orgId+"&memId="+memId+"&memberName="+mbrName+"&subFolderId="+subFolderId;
            frameObj2.src=source2;
            break;
        }
        case "showInvalidateUser":
        {
            //alert(' ina showInvalidateUser '+userIdForAcc)
            $.ajax({
                url: 'organisationDetails.do?param=expireUser&userId='+userIdForAcc.split(":")[1],
                success: function(data) {
                    if(data==1){
                        alert('User Invalidated.');
                    }else{
                        alert('User is already Invalidated');
                    }

                }
            });
            break;
        }
    //added by chiranjeevi on 24-12-09 for accounts start
    }
}
//added by susheela over

function restrictAccessFun(){

    $("#selectUserDimValuesDataDiv").dialog('open')
     var accessLevel= $("#accessLevel").val()
    // alert("accessLevel\t"+accessLevel)
    $.ajax({
        url:'userLayerAction.do?userParam=getDimentionMembers&userMemId='+userDimId+'&subFolderIdUser='+subFolderIdUser+'&userId='+userId+'&MemberName='+mbrName+'&accessLevel='+accessLevel+"&type=restrictAccess",
        success: function(data) {

            var jsonVar=eval('('+data+')')
            $("#contentDiv").html("")
            $("#contentDiv").html(jsonVar.htmlStr)
            grpColArray=jsonVar.memberValues
            isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
           // alert("isMemberUseInOtherLevel\t"+isMemberUseInOtherLevel)
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
            $(".sortable").sortable();

        }

    });


}
function saveExpireAccount()
{
    document.getElementById(orgId).style.color="red";
    document.getElementById('li-'+orgId).style.color="red";
    document.getElementById('user-'+orgId).style.color="red";
    document.getElementById('dim-'+orgId).style.color="red";
    $.ajax({
        url: 'organisationDetails.do?param=expireOrg&orgId='+orgId,
        success: function(data) {
            if(data==1){
        // alert('Account Expired.');
        }

        }
    });
}
//added by susheela over

//added by susheela over
function copyfolderDesc(){
    var $foldName=$("#folderName");
    var $foldDesc=$("#folderDesc");
    $foldDesc.val($foldName.val());
}

// modified by krishan pratap
function contextMenuUserFolder(action, el, pos) {

    switch (action) {
        case "assignToUser":
        {
            // alert("assignToUser")
            contextMenuWorkAssignUser(action, el, pos);
            // $('#assignToUser').dialog('open');
            break;
        }
        case "deleteUserFolder":
        {
            //alert(el.attr("id"));
            folderId=el.attr("id");
            $('#deleteUserFolder').dialog('open');
            //deleteUserFolder();
            break;
        }

        //added by 28-11-09 susheela start
        case "makeTargetFolder":
        {
            // alert('in ');
            subTargetFolder=el.attr("id");
            // alert('in target folder subTargetFolder '+subTargetFolder);
            $('#makeTargetFolder').dialog('open');
            break;
        }
        case "copyUserFolder":
        {
            // alert('in ');
            folderId=el.attr("id");
            //  alert('in sr '+folderId);
            $('#copyUserFolder').dialog('open');
            break;
        }
        //added by susheela over
        case "publishUserFolder":
        {
            //  alert(el.attr("id"));
            folderId=el.attr("id");
            //parent.$("#loading").show(); commented By Bhargavi on 4th may 2016

            $.ajax({
                url: 'CheckPublishBusRole?folderId='+folderId,
                success: function(data) {
                    // alert(data)
                    if(data==0){
                        //alert('in if')

                        $('#publishUserFolder').dialog('open');
                    }else{
                        var x=confirm('Business Role Already Published Are you want to Republish');
                        if(x==true){
                            //  $('#publishUserFolder').dialog('open');
                            $.ajax({
                                url: 'userLayerAction.do?userParam=publishUserFolder&folderId='+folderId,
                                success: function(data) {
                                    if(data=="false"){
                                        parent.$("#loading").hide();
                                        alert("Unable to publish Business Role")
                                    
                                    }else{
                                        //parent.checkUserFolder();
                                         parent.$("#loading").hide();
                                        alert("Business Role published successfully")
                                    }

                                }
                            });
                        }
                        else{
                             parent.$("#loading").hide();
                        }

                    }
                }
            });

            break;
        }
    }
}
function contextMenuWorkAssignUser(action, el, pos)
{

    var props = el.attr('id');
    // alert("props\t"+props)
    var roleId=props;
    // alert("grpID\t"+$("#"+props+" img").attr("id"))
    var grpId = $("#"+props+" img").attr("id")


    $("#userAssigned").dialog('open')
    var frameObj=document.getElementById("userAssignDisp");
    // alert(tabId)
    var source="getUserAssignList.jsp?grpId="+grpId+'&roleId='+roleId;
    frameObj.src=source;
// frameObj.style.display='block';
// document.getElementById('fade').style.display='block';

}
function contextMenuUserFolderFromulaFiter(action, el, pos) {

    switch (action) {

        case "addFilter":
        {

            foldertabId=el.attr("id");
            //  alert('in alert '+foldertabId)
            document.getElementById("filterDialog").innerHTML = "<iframe width=560 height=580 src=AddFolderFilter.jsp?foldertabId="+foldertabId+"></iframe>";
            $('#filterDialog').dialog('open');

            break;
        }
        case "addProperties":
        {

            foldertabId=el.attr("id");
            // alert('in alert '+foldertabId)
            document.getElementById("filterDialog").innerHTML = "<iframe width=560 height=580 src=getFolderTables.do?foldertabId="+foldertabId+"></iframe>";
            $('#filterDialog').dialog('open');

            break;
        }
        case "publishfact":
        {

            var subFolderTabId=el.attr("id");
            //            var grpId=a[2];
            $.ajax({
                url: 'userLayerAction.do?userParam=publishFact&subFolderTabId='+subFolderTabId,
                success: function(data) {
                    //                    alert("data\t"+data)
                    if(data=="false")
                        alert("Unable to publish Fact")
                    else{
                        //parent.checkUserFolder();
                        alert("Fact published successfully")
                        refreshPage()
                    }

                }
            });

            break;
        }

        case "addparamFormula":
        {

            foldertabId=el.attr("id");
            // alert('in alert '+foldertabId)
            document.getElementById("roleFormulaDialog").innerHTML = "<iframe width=500 height=500 frameborder=0 src=busRoleParamAddFormula.jsp?foldertabId="+foldertabId+"></iframe>";
            $('#roleFormulaDialog').dialog('open');

            break;
        }
        //added by susheela start 04-12-09
        case "addFilterFacts":
        {
            var foldertab=el.attr("id");
            //alert('foldertab in filter '+foldertab);
            document.getElementById("filterUserDialog").innerHTML = "<iframe border='0'  width=560 height=580 class=frame1 src=checkUserLayerFilter.jsp?foldertab="+foldertab+"></iframe>";
            $('#filterUserDialog').dialog('open');
            break;
        }
    //added by susheela start

    }
}
function contextMenuUserdelete(action, el, pos) {
    //  alert('in')
    switch (action) {


        case "userdelete":
        {
            foldertabId=el.attr("id");
            //  alert('hi -----deleteUser.do?elementId='+foldertabId)
            document.myForm.action="deleteUser.do?elementId="+foldertabId;
            document.myForm.submit();

            parent.deleteFolder();
            break;
        }
        case "viewFormula":
        {

            // alert('hi')
            document.getElementById("value").innerHTML=$(el).attr('title');
            $("#formulaViewDiv").dialog('open');


            break;
        }

    }
}
function deleteUserFolder(){

    /*  document.myForm.action="userLayerAction.do?userParam=deleteUserFolder&folderId="+folderId;
                document.myForm.submit();
                 parent.deleteFolder(); */
    $.ajax({
        url: 'userLayerAction.do?userParam=deleteUserFolder&folderId='+folderId,
        success: function(data) {
            // alert('data '+data);
            if(data==1){
                alert("Unable to Delete Business Role. It is being used in Target.");
            }
            else if(data==2){
                //parent.checkUserFolder();
                alert("Business Role deleted successfully");
                //parent.deleteFolder();
                window.location.reload(true);
            }

        }
    });

}

function publishUserFolder(){
    //alert(folderId)
    //var frameObj = parent.document.getElementById('userfoldertab');
    //var source="userLayerAction.do?userParam=deleteUserFolder&folderId="+folderId;
    //alert(source)
    //frameObj.src=source;
    //frameObj.style.display='';

    $.ajax({
        url: 'userLayerAction.do?userParam=publishUserFolder&folderId='+folderId,
        success: function(data) {
            if(data=="false")
                alert("Unable to publish Business Role")
            else{
                //parent.checkUserFolder();
                alert("Business Role published successfully")
            }

        }
    });



}
function copyUserFolder(){
    $('#createUserFolder').dialog('close');
    var folderName=$("#folderName").val();
    var folderDesc=$("#folderDesc").val();
    folderName=folderName.replace("&","'|| chr(38) ||'","gi");
    folderDesc=folderDesc.replace("&","'|| chr(38) ||'","gi");
    //alert( 'userLayerAction.do?userParam=copyUserFolder&fldName='+folderName+'&fldDesc='+folderDesc+'&oldFolderId='+folderId)
    alert('Only Dimensions,Facts and Buckets are Copied')
    $.ajax({
        url: 'userLayerAction.do?userParam=copyUserFolder&fldName='+folderName+'&fldDesc='+folderDesc+'&oldFolderId='+folderId,
        success: function(data) {
            if(data=="false")
                alert("Unable to copy Business Role")
            else{
                alert("Business Role copied successfully")
                folderName="";
                document.getElementById("folderName").value=folderName;
                document.getElementById("folderDesc").value=folderName;
                window.location.reload(true);
            //parent.checkUserFolder();
            // alert("Business Role Created successfully")
            }

        }
    });

}


function deleteFolder(){

    window.location.reload(true);
}

function  parentCancelTablePropertiesfolder(){

    $('#filterDialog').dialog('close');
    document.getElementById('fade').style.display='';

}

function saveTablesusercon()
{
    $('#selectConnection').dialog('close');
    var connId1=document.getElementById("connId1").value;

    /*  var frameObj = document.getElementById('userfoldertab');
    var source="userLayerAction.do?userParam=getUserFolderList&connId="+connId1;
    frameObj.src=source;*/
    document.forms.myFormcon.action = "userLayerAction.do?userParam=getUserFolderList&connId="+connId1;
    document.forms.myFormcon.submit();
    document.getElementById('userfoldertab').style.display='';
// var frameObj = document.getElementById('businessgrptab');
// window.location.reload(true);
}
function goConnection(){
    // alert('in go con')
    $('#selectConnection').dialog('open');
}

function updateVals()
{
    var userMemId = document.getElementById("userMemId").value;
    var userId = document.getElementById("userId").value;
    var subFolderIdUser = document.getElementById("subFolderIdUser").value;
    var totalUrl = "userId="+userId+"~subFolderIdUser="+subFolderIdUser+"~userMemId="+userMemId;
    // alert('Are you sure you want to remove the access.');
    var t=confirm('Are you sure you want to remove the access.');
    if(t==true){
        $.ajax({
            url: 'userLayerAction.do?userParam=deleteDimMemberValues&totalUrl='+totalUrl,
            success: function(data) {
                // alert('data '+data);
                if(data=="1")
                    alert("Values Not Saved.")
                else{
                    alert('Values Set To Default.');
                    cancelSelectValsParent();
                }
            }
        });
    }
}

function saveSelectVals()
{
    var userMemId = $("#userMemId").val();
    var userId = $("#userId").val();
    var subFolderIdUser =$("#subFolderIdUser").val();
    var meemberValues ="";    
    var roleElementId = roleEleId;    
    var roleSubFolderId = roleFolderId;
    var roleDimId = roleDimensionId;
    var userElementId = userEleId;    
    var userDimId = userDimensionId;
    
    $("#sortable li").each(function(){
//        meemberValues.push($(this).attr("id").replace("_li","").replace("&",";").replace(",","~"))
//          meemberValues.push($(this).attr("id").replace("_li","").replace(",","'''"))
        meemberValues = meemberValues+","+$(this).attr("id").replace("_li","").replace(",","'''");
    })
   if(meemberValues!=""){
       meemberValues=meemberValues.substr(1);
    }
    var urlVar=""
//    meemberValues = encodeURIComponent(meemberValues);
    
    if($("#accessLevel").val()=="roleLevel")
        urlVar='userLayerAction.do?userParam=addUserDimMemberValuesForRole&userMemId='+userMemId+'&subFolderIdUser='+subFolderIdUser+'&meemberValues='+encodeURIComponent(meemberValues)+'&elementId='+roleElementId+'&subFolderId='+roleSubFolderId+'&dimId='+roleDimId
    else if($("#accessLevel").val()=="userLevel")
        urlVar='userLayerAction.do?userParam=addUserDimMemberValues&userId='+userId+'&userMemId='+userMemId+'&subFolderIdUser='+subFolderIdUser+'&meemberValues='+encodeURIComponent(meemberValues)+'&elementId='+userElementId+'&dimId='+userDimId
    $.ajax({
        url: urlVar,
        success: function(data) {
            if(data=="1")
                alert("Values Not Saved.")
            else{
                alert('Values Saved Successfully.')
                cancelSelectValsParent();
            }
        }
    });
  
}        
function partialPublish(dimid,subfolderid,folderid)
{
        var x=confirm('Do you Want Publish This Dimension.');
        if(x==true)
        {

        $.ajax({
            url: 'userLayerAction.do?userParam=partialPublishDimentionsCheck&dimid='+dimid+'&subfolderid='+subfolderid+'&folderid='+folderid,
            success: function(data) {
                                     
                                        if(data=='true')
                                        alert("Dimension already published")
                                    else{
                                        $.ajax({
            url: 'userLayerAction.do?userParam=partialPublishDimentions&dimid='+dimid+'&subfolderid='+subfolderid+'&folderid='+folderid,
            success: function(data) {
                                        if(data=="false")
                                        alert("Unable to publish this Dimension.")
                                    else{
                                        //parent.checkUserFolder();
                                        alert("Dimension published successfully.")
                                    }


            }
        });
                                    }


            }
        });
        
        }

}
