/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
    Created on : 8 Dec, 2015, 5:45:03 PM
    Author     : Faiz Ansari
*/
function hideEle(){
//    alert("hi");
    if($(".hideDropDownMenu").is(":visible")){
        $(".hideDropDownMenu").slideUp("fast");
}
    //   TA_R_11 : Menus are not hiding when clicked on page/body
    if($(".hideDropDown").is(":visible")){
        $(".hideDropDown").slideUp("fast");
    }

}
function scrollBar(id){
    $(id).niceScroll({cursorcolor:"#000",cursoropacitymax:0.5,cursorwidth:5,mousescrollstep:6,enablekeyboard:true});

}
function checkBrowser() { 
    if((navigator.userAgent.indexOf("Opera") || navigator.userAgent.indexOf('OPR')) != -1 ){
      //  alert('Opera');
        return "opera";
    }
    else if(navigator.userAgent.indexOf("Chrome") != -1 ){
//        alert('Chrome');
        return "chrome";
    }
    else if(navigator.userAgent.indexOf("Safari") != -1){
//        alert('Safari');
        return "safari";
    }
    else if(navigator.userAgent.indexOf("Firefox") != -1 ) {
//         alert('Firefox');
         return "firefox";
    }
    else if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) {//IF IE > 10
    //  alert('IE'); 
      return "ie";
    }  
    else if(navigator.userAgent.indexOf("Edge")  != -1 || navigator.userAgent.indexOf("Trident")  != -1 ){
        return "edge";
    }
    else  {
//       alert('unknown');
       return "unknown";
    }
}
//TA_EN_1005 : Added new custom alert box.
function alertBox(msg){
//    alert("hia")
    if($("#alertBg").is(":visible")){return;}
    $("body").append("<div id='alertBg' class='alertBg'></div><div id='alertBox' class='alertBox'><div class='topSec'>pi - Progen Business Solutions</div><div class='midSec'><span style=''>"+msg+"</span></div><div class='botSec'><button id='alertBtn' class='btn btn-gr' onclick='hideAlert()'>OK</button></div></div>",function(){
        $("#alertBtn").focus();
    });    
}
function hideAlert(){
    $("#alertBg,#alertBox").remove();
}
function adjustReportArrows(){                                      
    $(".measureNumericCellNegative").find("div").append(" <img src='../stylesheets/images/negative.png' style='float:left;height:10px;margin:2px 0px 0px 3px;'>");
    $(".measureNumericCellPositive").find("div").append(" <img src='../stylesheets/images/positive.png' style='float:left;height:10px;margin:2px 0px 0px 3px;'>");
    $(".measureNumericCellPositiveGnf").find("div").append(" <img src='../stylesheets/images/positivecol.png' style='float:left;height:10px;margin:2px 0px 0px 3px;'>");
    $(".measureNumericCellNegativeGnf").find("div").append(" <img src='../stylesheets/images/negativecol.png' style='float:left;height:10px;margin:2px 0px 0px 3px;'>");
        var l=document.getElementsByClassName("repTblMnuPr");  
        var d=1;
        while(d <= l.length){
            var x=document.getElementsByClassName("widthClass"+d);  
            var tdClass="";
            if(x.length > 0){
                tdClass=x[0].parentNode.getAttribute("class");
            }            
//            var divWidth=0
            var maxWidth=0;
            if(tdClass=="measureNumericCellNegative" || tdClass=="measureNumericCellPositive" || tdClass=="measureNumericCellPositiveGnf" || tdClass=="measureNumericCellNegativeGnf"){
            for(var i=0;i<x.length;i++){
                if(x[i].firstChild.offsetWidth > maxWidth){
                    maxWidth=x[i].firstChild.offsetWidth;
//                    divWidth=x[i].offsetWidth;
                } 
                    if((i+1)==x.length && (tdClass=="measureNumericCellNegative" || tdClass=="measureNumericCellPositive" || tdClass=="measureNumericCellPositiveGnf" || tdClass=="measureNumericCellNegativeGnf")){
//                        if((divWidth-maxWidth) <=15 ){
//                            x[0].style.width=(divWidth+(15-(divWidth-maxWidth)))+"px";
//                        }                    
                    for(var j=0;j<x.length;j++){
                        x[j].firstChild.style.width=maxWidth+"px";
                        x[j].firstChild.style.cssFloat="left";
                        x[j].firstChild.style.display="block";
//                            if(x[j].style.cssFloat == "" || x[j].style.cssFloat == "center"){ 
//                                    if((divWidth-maxWidth) >= 30){
//                                        x[j].firstChild.style.marginLeft =(((divWidth-maxWidth)/2)-10)+"px";
//                                    }                                
//                            }
                                }                                
                        }
                    }
                }
            d++;
        }
}

function resizePage(pageName){
    $("#tabs").height($(window).height()-140);
    if(pageName == "modifyMeasures"){  
        $("#ModifyMeasures").height($(window).height()-320);
        var childLen=document.getElementById("pbiTbl").getElementsByTagName("th");
        for(var i=0;i<childLen.length;i++){
            childLen[i].firstChild.style.width=(childLen[i].offsetWidth-9)+"px";
        }  
        $("#pbiTblContainer").height($(window).height()-320);
        $("#modifyMesurAjaxBk").css({
            height:$("#modifyMesurAjaxBk").next().height(),
            width:$("#modifyMesurAjaxBk").next().width()
        });
        $("#RolesTab1").children().css("border","none");
    }
    if(pageName == "pbBIManager"){
        $("#extratabs").height($(window).height()-200);
    }
    if(pageName == "editSchedulers"){
        $("#editTableScheduleDiv").height($(window).height()-220);
    }
    if(pageName == "globalParameters"){
        $("#GlobalParameters").css({
			"height":$(window).height()-200,
			"overflow":"auto",
			"width":"97%"
		});
		$("#RolesTab1").children().css("border","none");
}
    if(pageName == "modifyMembers"){
        $("#modifyMembers").css({
            "height":$(window).height()-200,
            "overflow":"auto",
            "width":"97%"
        });
}
    if(pageName == "modifyDispName"){
        $("#ModifyDispFact").css({
            "height":$(window).height()-200,
            "overflow":"auto",
            "width":"97%"
        });
    }
    if(pageName == "modifyDispDimName"){
        $("#ModifyDispDim").css({
            "height":$(window).height()-200,
            "overflow":"auto",
            "width":"97%"
        });
    }
    if(pageName == "multiTalentSecurity"){
        $("#securityForm").css({
            "height":$(window).height()-200,
            "overflow":"auto",
            "width":"97%"
        });
}
}
