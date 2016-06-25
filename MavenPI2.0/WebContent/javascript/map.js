var latLngArr = new Array();
var colorArray;
var geocoder;
var map;
var cities;
var cityCodes;
var measureids;
var measures;
var measureValues = new Array();
var dimName = new Array();
var dimId = new Array();
var drillAvailable;
var latitudes;
var longitudes;
var markerArray;
var targetDivId;
var reportType="R";
var measureSize = new Array();
 var suppDim = new Array();
 var suppDimId = new Array();
 var MeasuresName = new Array();
 var MainMeasLength_GT_ONE;

Array.prototype.contains = function(vitem){
    var flag = false;

    for(var i=0;i<this.length;i++){
        if(this[i].toLowerCase() == vitem.toLowerCase()){
            flag = true;
        }
    }
    return flag;
}

function submitFormMapMeasChange(divId, rptType,sortType,mapView,geoView, REPORTID){
    if (divId == null || divId == 'undefined' || divId == 'null')
        divId = targetDivId;
    var ctxPath=document.getElementById("h").value;
    reportType = rptType;
        $.ajax({
            url:ctxPath+'/mapAction.do?reportBy=isMapEnabled&REPORTID='+REPORTID+'&reportType='+reportType+'&sortType='+sortType+'&mapView='+mapView+'&geoView='+geoView,
            success : function(data){
                if (data != null && data != ""){
                     if(data=="dimensionerror"){
                        alert("Map is not applicable for this view");
                     }else if(data=="error")
                         {
                             alert("You have not configured Geography Dimension in Setup. Please configure the same for viewing Maps");
                         }
                         else{
                            if (rptType == "D"){
                                var mapHtml="<div id=\"divHead\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\""
                                + "style=\"width:100%;\">";
                                mapHtml+="<table width=\"100%\"><tr>";
                                mapHtml+="<td width=\"74%\"><strong>Map Region</strong></td></tr></table></div>";
                                mapHtml+="<table width=\"100%\"><tr>";
                                mapHtml+="<td align=\"left\" width=\"10%\">";
                                mapHtml+="<a href=\"javascript:void(0);\" style=\"text-decoration: none;\" onclick=\"addMapMeasures('"+ctxPath+"','','"+REPORTID+"','"+divId+"')\">Add Measures</a>";
                                mapHtml+="</td></tr></table>";
                                mapHtml+="<div id=\"map_"+divId+"\" style=\"height:90%; width:100%\">";
                                $("#"+divId).html(mapHtml);
                                divId = "map_"+divId;
                            }
                            dispMap(data,divId,rptType);
                         }
                }
//                else
//                    alert("Map is not applicable for this view.");
                $("#MapMeasures").dialog('close');
            }
        });
}

function dispMap(json,divId,rptType){

    latLngArr = new Array();
    reportType = rptType;
    var details = eval('('+json+')');
    cities = details.City;
    cityCodes = details.CityCode;
    measures = details.Measures;
    measureIds = details.MeasureIds;
    dimName = details.DIMNAME;
    dimId = details.DimId;
    drillAvailable = details.DrillAvailable;
    latitudes = details.Latitude;
    longitudes = details.Longitude;
    measureValues = new Array();
    colorArray=details.ColorCode;
    markerArray=details.MarkerSize;
    suppDim = details.SuppDimName;
    suppDimId = details.SuppDimId;
    MeasuresName = details.MeasuresName;
    MainMeasLength_GT_ONE = parseInt(details.MainMeasLength_GT_ONE);
    measureSize = MeasuresName.length+suppDim.length;
        if (document.getElementById("hideDiv") != null){
            var  hideDiv=document.getElementById("hideDiv").value;
            if (hideDiv != null){
                if (document.getElementById("hideDiv") != null)
                    document.getElementById("hideDiv").style.display='block';
            }
        }
//    document.getElementById(divId).innerHTML ="";
     $("#"+divId).html('')
    geocoder = new google.maps.Geocoder();
    var myOptions;

    if (reportType == "D"){
        myOptions = {
            zoom: 5,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            scrollwheel: false
        }
    }
    else{
        myOptions = {
            zoom: 4,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            scrollwheel: false
        }
    }
    map = new google.maps.Map(document.getElementById(divId), myOptions);
    targetDivId = divId;
    for (var i=0;i<cities.length;i++){
        if (cities[i] == "null")
            continue;
        addMarker(i,cities[i],cityCodes[i] );
    }
}

function checkIfUsed(latLng){
    for (var i=0;i<latLngArr.length;i++){
        if (latLngArr[i].equals(latLng)){
            return true;
        }
    }
    return false;
}

function getNewLatLng(latLng){
var radius = 0.25;
var angle = 15;
 var newLatLng ;
 var xcordinate;
 var ycordinate

for(var i=1;i<=10;i++){

     xcordinate = radius*(Math.cos(15*i));
     ycordinate = radius*(Math.sin(15*i));

    newLatLng = new google.maps.LatLng(latLng.lat()+xcordinate,latLng.lng()+ycordinate);
    if(!checkIfUsed(newLatLng)){
        return newLatLng;
    }
}
    return latLng;
}



function addMarker(cityIndex, city, cityCode){

    if (latitudes[cityIndex] != "0.0" && longitudes[cityIndex] != "0.0"){
      for(var i=0;i<measures[cityIndex].length;i++)
        processMeasure(cityIndex, city, cityCode, new google.maps.LatLng(latitudes[cityIndex], longitudes[cityIndex]),colorArray[cityIndex][i],markerArray[cityIndex][i],measures[cityIndex][i],MeasuresName[i],measureIds[i]);

              }
    else{
        geocoder.geocode({'address':city},
        function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                for(var i=0;i<measures[cityIndex].length;i++){
                    if(i<25)

                processMeasure(cityIndex, city, cityCode, results[0].geometry.location,colorArray[cityIndex][i],markerArray[cityIndex][i],measures[cityIndex][i],MeasuresName[i],measureIds[i]);
                }

                var lat=results[0].geometry.location.lat();
                var lng=results[0].geometry.location.lng();
                var ctxPath=document.getElementById("h").value;
                var REPORTID = document.getElementById("REPORTID").value;

                $.ajax({
                    url:ctxPath+'/mapAction.do?reportBy=storeLatLng&latvalues='+lat+'&lngvalues='+lng+'&address='+city+'&reportId='+REPORTID,
                    success : function(data){
                    }
                });
            } else {
                //alert("Geocode was not successful for the following reason: " + status);
            }
        }
    );
    }
}


function processMeasure(cityIndex, address, label, latLng,color,marker,measureValArray,primaryMesureLabel,measureEleId){

var status=false
for(var j=0;j<measureValArray.length;j++){
              if(measureValArray[j]=="0"){
                  status=false;
              }else{
               status=true
               break
              }

          }
          if(status){
    var image="stylesheets/images/";

    if(color[0]=='red'){
         if(marker[0]=='small')
            image=image+"redColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"redColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"redColor_MediumMarker.png";
        if(marker[0]=='large')
            image= image+"redColor_BigMarker.png";
    }
    if(color[0]=='green'){
        if(marker[0]=='small')
            image=image+"greenColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"greenColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"greenColor_MediumMarker.png";
        if(marker[0]=='large')
            image= image+"greenColor_BigMarker.png";
    }
    if(color[0]=='orange'){
        if(marker[0]=='small')
            image=image+"orangeColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"orangeColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"orangeColor_MediumMarker.png";
        if(marker[0]=='large')
            image=image+"orangeColor_BigMarker.png";
    }
    if(color[0]==''){
        if(marker[0]=='small')
            image=image+"blueColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"blueColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"blueColor_MediumMarker.png";
        if(marker[0]=='large')
            image=image+"blueColor_BigMarker.png";
    }
    if(color[0]=='blue'){
        if(marker[0]=='small')
            image=image+"blueColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"blueColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"blueColor_MediumMarker.png";
        if(marker[0]=='large')
            image=image+"blueColor_BigMarker.png";
    }
    if(color[0]=='gray'){
        if(marker[0]=='small')
            image=image+"grayColor_SmallMarker.png";
         if(marker[0]=='normal')
              image=image+"grayColor_SmallMarker.png";
          if(marker[0]=='medium')
              image=image+"grayColor_MediumMarker.png";
           if(marker[0]=='large')
                 image=image+"grayColor_BigMarker.png";
    }
    if(color[0]=='lightpink'){
        if(marker[0]=='small')
            image=image+"lightPinkColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"lightPinkColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"lightPinkColor_MediumMarker.png";
        if(marker[0]=='large')
            image=image+"lightPinkColor_BigMarker.png";



    }
    if(color[0]=='lilac'){
        if(marker[0]=='small')
            image=image+"etonBlueColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"etonBlueColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"etonBlueColor_MediumMarker.png";
        if(marker[0]=='large')
            image=image+"etonBlueColor_BigMarker.png";

    }
    if(color[0]=='lavender'){
        if(marker[0]=='small')
            image=image+"lavenderBlueColor_SmallMarker.png";
        if(marker[0]=='normal')
            image=image+"lavenderBlueColor_SmallMarker.png";
        if(marker[0]=='medium')
            image=image+"lavenderBlueColor_MediumMarker.png";
        if(marker[0]=='large')
            image=image+"lavenderBlueColor_BigMarker.png";
    }
    var red_color_array =  new Array("E60000","FF0000","FF1919","FF3333","FF4D4D","FF6666","FF8080","FF9999","FFB2B2","FFCCCC","FFE6E6");
//    if(color[0]=='#E60000'|| color[0]=='#FF0000'||color[0]=='#FF1919'||color[0]=='#FF3333'||color[0]=='#FF4D4D'||color[0]=='#FF6666'||color[0]=='#FF8080'||color[0]=='#FF9999'||color[0]=='#FFB2B2'||color[0]=='#FFCCCC'||color[0]=='#FFE6E6'){
//       alert("in red")
//       if(marker[0]=='small'){
//            image=image+color[0]+"_Red_SmallMarker.png";
//        }
//        if(marker[0]=='normal'){
//            image=image+color[0]+"_Red_SmallMarker.png";
//        }
//        if(marker[0]=='medium'){
//            image=image+color[0]+"_Red_MediumMarker.png";
//        }
//        if(marker[0]=='large'){
//            image=image+color[0]+"_Red_BigMarker.png";
//        }
//    }
if(red_color_array.contains(color[0])){
           if(marker[0]=='small'){
            image=image+color[0].toUpperCase()+"_Red_SmallMarker.png";
        }
        if(marker[0]=='normal'){
            image=image+color[0].toUpperCase()+"_Red_SmallMarker.png";
        }
        if(marker[0]=='medium'){
            image=image+color[0].toUpperCase()+"_Red_MediumMarker.png";
        }
        if(marker[0]=='large'){
            image=image+color[0].toUpperCase()+"_Red_BigMarker.png";
}
}
    var green_color_array =  new Array("00E600","00ff00","19FF19","33FF33","4DFF4D","66FF66","80FF80","99FF99","B2FFB2","CCFFCC","E6FFE6");
//    if(color[0]=='#00E600'||color[0]=='#00ff00'||color[0]=='#19FF19'||color[0]=='#33FF33'||color[0]=='#4DFF4D'||color[0]=='#66FF66'||color[0]=='#80FF80'||color[0]=='#99FF99'||color[0]=='#B2FFB2'||color[0]=='#CCFFCC'||color[0]=='#E6FFE6'){
//        if(marker[0]=='small'){
//            image=image+color[0]+"_Green_SmallMarker.png";
//        }
//        if(marker[0]=='normal'){
//            image=image+color[0]+"_Green_SmallMarker.png";
//        }
//        if(marker[0]=='medium'){
//            image=image+color[0]+"_Green_MediumMarker.png";
//        }
//        if(marker[0]=='large'){
//            image=image+color[0]+"_Green_BigMarker.png";
//        }
//    }
if(green_color_array.contains(color[0])){
            if(marker[0]=='small'){
            image=image+color[0].toUpperCase()+"_Green_SmallMarker.png";
        }
        if(marker[0]=='normal'){
            image=image+color[0].toUpperCase()+"_Green_SmallMarker.png";
        }
        if(marker[0]=='medium'){
            image=image+color[0].toUpperCase()+"_Green_MediumMarker.png";
        }
        if(marker[0]=='large'){
            image=image+color[0].toUpperCase()+"_Green_BigMarker.png";
        }
}
    var blue_color_array =  new Array("0000E6","0000ff","1919FF","3333FF","4D4DFF","6666FF","8080FF","9999FF","B2B2FF","CCCCFF","E6E6FF");
//    if(color[0]=='#0000E6'||color[0]=='#0000ff'||color[0]=='#1919FF'||color[0]=='#3333FF'||color[0]=='#4D4DFF'||color[0]=='#6666FF'||color[0]=='#8080FF'||color[0]=='#9999FF'||color[0]=='#B2B2FF'||color[0]=='#CCCCFF'||color[0]=='#E6E6FF'){
//        if(marker[0]=='small'){
//            image=image+color[0]+"_Blue_SmallMarker.png";
//        }
//        if(marker[0]=='normal'){
//            image=image+color[0]+"_Blue_SmallMarker.png";
//        }
//        if(marker[0]=='medium'){
//            image=image+color[0]+"_Blue_MediumMarker.png";
//        }
//        if(marker[0]=='large'){
//            image=image+color[0]+"_Blue_BigMarker.png";
//        }
//    }


if(blue_color_array.contains(color[0])){
            if(marker[0]=='small'){
            image=image+color[0].toUpperCase()+"_Blue_SmallMarker.png";
        }
        if(marker[0]=='normal'){
            image=image+color[0].toUpperCase()+"_Blue_SmallMarker.png";
        }
        if(marker[0]=='medium'){
            image=image+color[0].toUpperCase()+"_Blue_MediumMarker.png";
        }
        if(marker[0]=='large'){
            image=image+color[0].toUpperCase()+"_Blue_BigMarker.png";
        }
}
    var orange_color_array = new Array("E68A2E","FF9933","FFA347","FFAD5C","FFB870","FFC285","FFCC99","FFD6AD","FFE0C2","FFEBD6","FFF5EB");
//    if(color[0]=='#E68A2E'||color[0]=='#FF9933'||color[0]=='#FFA347'||color[0]=='#FFAD5C'||color[0]=='#FFB870'||color[0]=='#FFC285'||color[0]=='#FFCC99'||color[0]=='#FFD6AD'||color[0]=='#FFE0C2'||color[0]=='#FFEBD6'||color[0]=='#FFF5EB'){
//        if(marker[0]=='small'){
//            image=image+color[0]+"_Orange_SmallMarker.png";
//        }
//        if(marker[0]=='normal'){
//            image=image+color[0]+"_Orange_SmallMarker.png";
//        }
//        if(marker[0]=='medium'){
//            image=image+color[0]+"_Orange_MediumMarker.png";
//        }
//        if(marker[0]=='large'){
//            image=image+color[0]+"_Orange_BigMarker.png";
//        }
//    }

if(orange_color_array.contains(color[0])){
            if(marker[0]=='small'){
            image=image+color[0].toUpperCase()+"_Orange_SmallMarker.png";
        }
        if(marker[0]=='normal'){
            image=image+color[0].toUpperCase()+"_Orange_SmallMarker.png";
        }
        if(marker[0]=='medium'){
            image=image+color[0].toUpperCase()+"_Orange_MediumMarker.png";
        }
        if(marker[0]=='large'){
            image=image+color[0].toUpperCase()+"_Orange_BigMarker.png";
        }
}


    var marker;
    var info;
    var suppdimval = "";

    var markerTitle = dimName[0] + " : " + label;

    if(MainMeasLength_GT_ONE > 1){
        markerTitle = markerTitle+" \n "+primaryMesureLabel+" : "+measureValArray[0];
    }else{

  for(var i=1;i<dimName.length;i++){
      markerTitle = markerTitle+" \n "+dimName[i]+" : "+measureValArray[i-1];
      suppdimval = suppdimval+","+measureValArray[i-1];

  }
  markerTitle = markerTitle+" \n "+MeasuresName[0]+" : "+measureValArray[dimName.length-1];
    }


  var index;

  var index = MeasuresName.length-suppDim.length;


  info = "<strong>"+dimName[0]+" : "+label;

if(MainMeasLength_GT_ONE>1){
    info = info+"<br/><strong>" +primaryMesureLabel+ " : </strong>" + measureValArray[0];
    var suppMeasLength = measureValArray.length-1;
    if(suppMeasLength>0){

        for(var i=0;i<measureValArray.length-1;i++){
            info = info+ "<br/><strong>" + MeasuresName[MainMeasLength_GT_ONE+i] + " : </strong>" + measureValArray[i+1];
        }
    }
}else{
    if(measureSize==1){
        info = info+ "<br/><strong>" + MeasuresName[0] + " : </strong>" + measureValArray[0];
    }
    else{


            for(var j=0;j<suppDim.length;j++){
                info = info+"<br/><strong>" + suppDim[j] + " : </strong>" + measureValArray[j];
            }
            for(var i=0;i<MeasuresName.length;i++){
                info = info+"<br/><strong>" + MeasuresName[i] + " : </strong>" + measureValArray[j];
                j++;
            }

    }
}
   if(!suppdimval==""){
       suppdimval = suppdimval.substring(1);

   }

    info = info + "</p>";
    info = info + "<br>";
    if (reportType == "D"){
        label = label+","+suppdimval;
        if (drillAvailable == "true")
            info = info + "<a href=\"javascript:drillItem('"+targetDivId+"','"+dimId+"','"+label+"','"+reportType+"')\">Drill Down</a> | ";
        info = info + " <a href=\"javascript:gotoInsightFromMap('"+measureEleId+"','"+dimId+"','"+label+"')\">Insights</a>";
    }

    map.setCenter(latLng);
    var newLatLng = latLng;
    if(checkIfUsed(latLng)){
        newLatLng = getNewLatLng(latLng);
        latLngArr.push(newLatLng);
    }
    else{
        latLngArr.push(newLatLng);
    }

    marker = new google.maps.Marker({
        map: map,
        position: newLatLng,
        title: markerTitle,
        icon: image

    });

    var infoWindow = new google.maps.InfoWindow({
        content: info
    });

    google.maps.event.addListener(marker,'click',function(){
        infoWindow.open(map,marker);
    });
}
}
function drillItem(divId, dimId, dimVal, reportType){
    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;

    $.ajax({
        url:ctxPath+'/mapAction.do?reportBy=drillDownData&dimId='+dimId+'&dimValue='+dimVal+'&reportType='+reportType+'&reportId='+REPORTID,
        success : function(data){
            dispMap(data, divId,reportType);
        }
    });
}

function gotoInsightFromMap(elementId, insightDimId, insightDimValue){
    var REPORTID = document.getElementById("REPORTID").value;
    document.forms.frmParameter.action="dashboardViewer.do?reportBy=getKPIInsightViewerPage&elementId="+elementId+"&dashBoardId="+REPORTID+"&insightDimId="+insightDimId+"&insightDimValue="+insightDimValue;
    document.forms.frmParameter.submit();
}
