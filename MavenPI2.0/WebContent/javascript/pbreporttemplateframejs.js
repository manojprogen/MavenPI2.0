/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function repDesigndisptablesize(){
    var ifrmesizes = document.getElementById("previewDispTable");
    if ($.browser.msie == true){
        if(screen.width > 1440){
            ifrmesizes.style.width='1200px';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';

        }
        else if(screen.width == 1400 ){
            ifrmesizes.style.width='1000px';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1280 ){
            ifrmesizes.style.width='1000px';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1024 ){
            ifrmesizes.style.width='950px';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1000 ){
            ifrmesizes.style.width='700px';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }else{
            ifrmesizes.style.width=(ifrmesizes.style.width -400)+ 'px';
            ifrmesizes.style.position='relative';
        }
    }else{
        if(screen.width >= 1440){
            ifrmesizes.style.width='385%';
            ifrmesizes.style.position='relative';
            ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1400 ){
            ifrmesizes.style.width='377%';
            ifrmesizes.style.position='relative';
            ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1280 ){
            ifrmesizes.style.width='337%';
            ifrmesizes.style.position='relative';
            ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1024 ){
            ifrmesizes.style.width='250%';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1000 ){
            ifrmesizes.style.width='230%';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }else{
            ifrmesizes.style.width='200%';
            ifrmesizes.style.position='relative';
        }
    }
}

function rpsavedate(reportid){
    var dateFormat=$( "#cdate" ).datepicker( "option", "dateFormat" )
    var date=document.getElementById("cdate").value;
    var dateType=$("#dateType").val()
    if(date!=null && date!="")
    {
        $.ajax({
            url:'reportTemplateAction.do?templateParam=setCustomDate&date='+date+"&reportid="+reportid+'&dateType='+dateType+'&dateFormat='+dateFormat,
            success: function(data){
                if(data==1){
                    parent.$("#customDate").dialog('close');
                }else if(data==2){
                    alert("To date should be greater than from date")
                }
            }
        });

    }
    else
    {
        alert("Please Select Date");
    }
}
