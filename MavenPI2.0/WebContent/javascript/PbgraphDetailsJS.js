

var fntColor="";
var bckColor="";

function setColor(clr)
{
    fntColor=clr;
}
function setbColor(cl)
{
    bckColor=cl;
}

$(document).ready(function(){
    $('#MyDemoColorPicker2').empty().addColorPicker({
        clickCallback: function(c) {
            $('#body').css('background-color',c);
        },
        colors: [ '#fff', '#000', '#f00', '#0f0', '#00f' ],
        iterationCallback: function(target,elem,color,iterationNumber) {
            elem.html(color);
        }
    });

    $('#MyDemoColorPicker1').empty().addColorPicker({
        clickCallback: function(c) {
            $('#demo').css('border-color',c).css('color',c);
        },
        colors: [ '#fff', '#000', '#f00', '#0f0', '#00f' ],
        iterationCallback: function(target,elem,color,iterationNumber) {
            elem.html(color);
        }
    });
});



function checkBot(value){
    if(value==true){
        document.getElementById('radBot').className='selected';
        document.getElementById('radRyt').className='unselected';
    }
}
function checkRyt(value){
    if(value==true){
        document.getElementById('radRyt').className='selected';
        document.getElementById('radBot').className='unselected';
    }
}
function checkBox1(value){
    if(value==true){
        document.getElementById("shwlgds").innerHTML='Yes';
        document.getElementById('ShowLegend').className='checked';
    }
    else{
        document.getElementById("shwlgds").innerHTML='No';
        document.getElementById('ShowLegend').className='unchecked'

    }
}
function checkBox2(value){
    if(value==true){
        document.getElementById("shwGridX").innerHTML='Yes';
        document.getElementById('shGrd').className='checked';
    }
    else{
        document.getElementById("shwGridX").innerHTML='No';
        document.getElementById('shGrd').className='unchecked';
    }
}
function checkBox3(value){
    if(value==true){
        document.getElementById("shwGridY").innerHTML='Yes';
        document.getElementById('shGrdY').className='checked';
    }
    else{
        document.getElementById("shwGridY").innerHTML='No';
        document.getElementById('shGrdY').className='unchecked';
    }
}
function checkBox4(value){
    if(value==true){
        document.getElementById("alwDrill").innerHTML='Yes';
        document.getElementById('allDrl').className='checked';
    }
    else{
        document.getElementById("alwDrill").innerHTML='No';
        document.getElementById('allDrl').className='unchecked';
    }
}
function checkBox5(value){
    if(value==true){
        document.getElementById("shwData").innerHTML='Yes';
        document.getElementById('shwDV').className='checked';
    }
    else{
        document.getElementById("shwData").innerHTML='No';
        document.getElementById('shwDV').className='unchecked';
    }
}
function rstFlds(){
    document.getElementById("shwlgds").innerHTML='No';
    document.getElementById('ShowLegend').className='unchecked';
    document.getElementById('radBot').className='selected';
    document.getElementById('radRyt').className='unselected';
    document.getElementById("shwGridX").innerHTML='No';
    document.getElementById('shGrd').className='unchecked';
    document.getElementById("shwGridY").innerHTML='No';
    document.getElementById('shGrdY').className='unchecked';
    document.getElementById("alwDrill").innerHTML='No';
    document.getElementById('allDrl').className='unchecked';
    document.getElementById("shwData").innerHTML='No';
    document.getElementById('shwDV').className='unchecked';
}
function cancelDetails()
{
    document.getElementById('reset').disabled = false;
    document.getElementById('cancel').disabled = false;
    parent.document.getElementById('report').style.display='none';
    parent.document.getElementById('fade').style.display='none';
}

function saveGraphDetails(graphId,GraphIds){
    var graphTypeName=document.getElementById("graphTypeName").value;
    var forwardFlag = document.getElementById("forwardFlag").value;
    var graphTitle=document.getElementById("grpTitle").value;
    var parentDiv = parent.document.getElementById('previewDispGraph');
    var GraphDisplayRows=document.getElementById("GraphDisplayRows").value;
    var ShowlyAxis=document.getElementById("ShowlyAxis");
    var ShowryAxis=document.getElementById("ShowryAxis");
    var ShowxAxis=document.getElementById("ShowxAxis");
    var MeasureNamePosition=document.getElementById("MeasureNamePosition");
    var startindex=document.getElementById("startindex").value;
    var endindex=document.getElementById("endindex").value;
    var sleg=document.getElementById('showLegend');
    var l1=document.getElementById('r1');
    var l2=document.getElementById('r2');
    var sx=document.getElementById('showX');
    var sy=document.getElementById('showY');
    var showLabels=document.getElementById('showLabels').value;
    var lylabel;
    var rylabel;
    if(graphTypeName=="Dual Axis"){
        lylabel=document.getElementById('lyaxisLabel').value;
        rylabel=document.getElementById('ryaxisLabel').value;
    }
    else{
         lylabel=document.getElementById('lyaxisLabel').value;
         rylabel=document.getElementById('lyaxisLabel').value;
    }
    
    var dr=document.getElementById('Drill');
    var  dat =document.getElementById("Data");

    var slegend="";
    var Loc="";
    var shoX="";
    var shoY="";
    var Dril="";
    var data="";


    if(sleg!=null && sleg.checked){
        slegend="Y";
    }
    else{
        slegend="N";
    }
    
    if(l1!=null && l1.checked){
        Loc="Bottom";
    }
   
    if(l2!=null && l2.checked){
        Loc="Right";
    }
   
    if(sx!=null && sx.checked){
        shoX="Y";
    }
    else{
        shoX="N";
    }
    
    if(sy!=null && sy.checked){
        shoY="Y";
    }
    else{
        shoY="N";
    }
   
    if(dr!=null && dr.checked){
        Dril="Y";
    }
    else{
        Dril="N";
    }
  
    if(dat!=null && dat.checked){
        data="Y";
    }
    else{
        data="N"
    }
    parent.document.getElementById('previewDispGraph').style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    if(forwardFlag == "GTM"){
        $.ajax({
            url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&grpIds='+GraphIds+'&graphChange=GraphDetails&grpTitle='+graphTitle
            +'&grpLegend='+slegend+'&grpLegendLoc='+Loc+'&showX='+shoX+'&showY='+shoY+'&lyaxisLabel='+lylabel+'&ryaxisLabel='+rylabel+'&ShowlyAxis='+ShowlyAxis+'&ShowryAxis='+ShowryAxis+'&ShowxAxis='+ShowxAxis+'&MeasureNamePosition='+MeasureNamePosition+'&Drill='+Dril+'&bcolor='+bckColor+
            '&fcolor='+fntColor+'&Data='+data+'&GraphDisplayRows='+GraphDisplayRows+'&REPORTID='+parent.document.getElementById("REPORTID").value+
            '&graphTableMethod=GTM&startindex='+startindex+'&endindex='+endindex+'&showLabels='+showLabels,
           
            success: function(data){
                var parentDiv = parent.document.getElementById('previewDispGraph');
                parentDiv.innerHTML=data;
                parent.cancelGraphDetails();
                parent.document.getElementById('previewDispGraph').style.height = 'auto';
            }
        });
    }else{
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&grpIds='+GraphIds+'&graphChange=GraphDetails&grpTitle='+graphTitle
        +'&grpLegend='+slegend+'&grpLegendLoc='+Loc+'&showX='+shoX+'&showY='+shoY+'&lyaxisLabel='+lylabel+'&ryaxisLabel='+rylabel+'&ShowlyAxis='+ShowlyAxis+'&ShowryAxis='+ShowryAxis+'&ShowxAxis='+ShowxAxis+'&MeasureNamePosition='+MeasureNamePosition+'&Drill='+Dril+'&bcolor='+bckColor+
        '&fcolor='+fntColor+'&Data='+data+'&GraphDisplayRows='+GraphDisplayRows+'&REPORTID='+parent.document.getElementById("REPORTID").value+
        '&graphTableMethod=GM&startindex='+startindex+'&endindex='+endindex+'&showLabels='+showLabels,

        success: function(data){
            var parentDiv = parent.document.getElementById('previewDispGraph');
            parentDiv.innerHTML=data;
            parent.cancelGraphDetails();
            parent.document.getElementById('previewDispGraph').style.height = 'auto';
        }
    });
    }
    parent.cancelGraphDetails();
}
