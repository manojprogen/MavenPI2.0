function onSave(jspName){
    var alertName=document.myForm.alertName.value;
    var frequency=document.myForm.frequency.value;
    var hrisk=document.myForm.highRisk.value;
    var mrisk=document.myForm.mediumRisk.value;
    var lrisk=document.myForm.lowRisk.value;
    var alertinfo =document.myForm.infoAlert.value;
    /*
if(frequency!=1){
         if(hrisk=="between" && mrisk=="between"&&lrisk=="between")
             {
                var hv1=document.myForm.box1.value;
                var hv2=document.myForm.box2.value;  
                var mv1=document.myForm.mediumbox1.value;
                var mv2=document.myForm.mediumbox2.value;
                var lv1=document.myForm.lowbox1.value;
                var lv2=document.myForm.lowbox2.value;
                if(hv1=="" || hv2=="" ||mv1=="" || mv2==""||lv1=="" || lv2==""&&alertName=="")
                    {
                        var x=confirm('Plaese enter AlertName and Risk values ');
                    }else{
                        var x=confirm('Please enter Alert name');
                    }
                 
             }
             else if(hrisk=="between" && mrisk!="between"&&lrisk!="between"){
                var hv1=document.myForm.box1.value;
                var hv2=document.myForm.box2.value;
                var mv1=document.myForm.mediumbox1m.value;

                var lv1=document.myForm.lowbox1l.value;

              if(hv1=="" || hv2=="" ||mv1==""||lv1=="" &&alertName=="")
                    {
                       var x=confirm('Plaese enter AlertName and Risk values ');
                    }else{
                        var x=confirm('Please enter Alert name');
                    }

             }
             else if(hrisk!="between" && mrisk=="between"&&lrisk!="between")
                 {
                var hv1=document.myForm.box1h.value;

                var mv1=document.myForm.mediumbox1.value;
                var mv2=document.myForm.mediumbox2.value;
                var lv1=document.myForm.lowbox1l.value;

              if(hv1==""||mv1==""||mv2==""||lv1=="" &&alertName=="")
                    {
                        var x=confirm('Plaese enter AlertName and Risk values ');
                    }else{
                        var x=confirm('Please enter Alert name');
                    }

             }
              else if(hrisk!="between" && mrisk!="between"&&lrisk=="between")
                 {
                var hv1=document.myForm.box1h.value;

                var mv1=document.myForm.mediumbox1m.value;

                var lv1=document.myForm.lowbox1.value;
                var lv2=document.myForm.lowbox2.value;
              if(hv1==""||mv1==""||lv2==""||lv1=="" &&alertName=="")
                    {
                      var x=confirm('Plaese enter AlertName and Risk values ');
                    }else{
                       var x=confirm('Please enter Alert name');
                    }

             }
             else if(hrisk=="between" && mrisk=="between"&&lrisk!="between")
                 {
                var hv1=document.myForm.box1.value;
                var hv2=document.myForm.box2.value;
                var mv1=document.myForm.mediumbox1.value;
                var mv2=document.myForm.mediumbox2.value;
                var lv1=document.myForm.lowbox1l.value;

              if(hv1==""||hv2==""||mv1==""||mv2==""||lv1=="" &&alertName=="")
                    {
                        var x=confirm('Plaese enter AlertName and Risk values ');
                    }else{
                        var x=confirm('Please enter Alert name');
                    }

             }
             else if(hrisk!="between" && mrisk=="between"&&lrisk=="between")
                 {
                var hv1=document.myForm.box1h.value;
                var mv1=document.myForm.mediumbox1.value;
                var mv2=document.myForm.mediumbox2.value;
                var lv1=document.myForm.lowbox1.value;
                var lv2=document.myForm.lowbox2.value;

              if(hv1==""||lv2==""||mv1==""||mv2==""||lv1=="" &&alertName=="")
                    {
                        var x=confirm('Plaese enter AlertName and Risk values ');
                    }else{
                        var x=confirm('Please enter Alert name');
                    }

             }
             else if(hrisk=="between" && mrisk!="between"&&lrisk=="between")
                 {
                var hv1=document.myForm.box1.value;
                var hv2=document.myForm.box2.value;
                var mv1=document.myForm.mediumbox1m.value;
                var lv1=document.myForm.lowbox1.value;
                var lv2=document.myForm.lowbox2.value;


              if(hv1==""||lv2==""||mv1==""||hv2==""||lv1=="" &&alertName=="")
                    {
                        var x=confirm('Plaese enter AlertName and Risk values ');
                    }else{
                        var x=confirm('Please enter Alert name');
                    }

             }
             else if(hrisk=="between" && mrisk=="between"&&lrisk=="between")
             {
                var hv1=document.myForm.box1.value;
                var hv2=document.myForm.box2.value;
                var mv1=document.myForm.mediumbox1.value;
                var mv2=document.myForm.mediumbox2.value;
                var lv1=document.myForm.lowbox1.value;
                var lv2=document.myForm.lowbox2.value;
               if(hv2>hv1 || mv2>mv1||lv2>lv1){
                   var x=confirm('Please Enter first value less than the second value in % Deviation of Target');
               }else if(hv2!=mv1||mv2!=lv1||lv2!=hv1){
                      var x=confirm('You are selecting non continuous values for % Deviantion.\n\
                             Are sure to save this Alert.');
                }
             }
             /*else if(hrisk=="between" && mrisk!="between"&&lrisk!="between"){
                var hv1=document.myForm.box1.value;
                var hv2=document.myForm.box2.value;
                var mv1=document.myForm.mediumbox1m.value;
                var lv1=document.myForm.lowbox1l.value;

              if(hv1>hv2){
                    alert('Please Enter first value less than the second value in % Deviation of Target');
              }else if(hv2!=mv1||mv2!=lv1||){
                      alert('You are selecting non continuous values for % Deviantion.\n\
                             Are sure to this Alert.');
              }

             }*/
    //else{

    document.myForm.action=jspName;
    document.myForm.submit();
// }

//}


/*else if(frequency!=1){
    if(hrisk=="between"){
        var hv1=document.myForm.box1.value;
        var hv2=document.myForm.box2.value;
       // var hv3=document.myForm.text1.value;

        if(hv1=="" || hv2==""){
            var x=confirm('Please enter high risk values');
        }else if(hv1=="" || hv2=="" || hv3==""){
            var x=confirm('Please enter high risk Subscribers');
        }
    }else if(hrisk!="between"){
       var hv1=document.myForm.box1.value;
        var hv3=document.myForm.text1.value;
       if(hv1==""){
            var x=confirm('Please enter high risk values');
        }else if(hv3==""){
            var x=confirm('Please enter low risk Subscibers')
        }
    }
   else if(mrisk=="between"){
        var mv1=document.myForm.mediumbox1.value;
        var mv2=document.myForm.mediumbox2.value;
        var mv3=document.myForm.textm.value;
        if(mv1=="" || mv2==""){
            var x=confirm('Please enter medium risk values');
        }else if(mv1=="" || mv2=="" || mv3==""){
            var x=confirm('Please enter medium risk Subscribers');
        }
    }else if(mrisk!="between"){
       var mv1=document.myForm.box1.value;
        var mv3=document.myForm.textm.value;
       if(mv1==""){
           var x=confirm('Please enter medium risk values');
        }else if(mv3==""){
            var x=confirm('Please enter low risk Subscibers')
        }
    }
    else if(lrisk=="between"){
        var lv1=document.myForm.lowbox1.value;
        var lv2=document.myForm.lowbox2.value;
        var lv3=document.myForm.textm.value;
        if(lv1=="" || lv2==""){
            var x=confirm('Please enter low risk values');
        }else if(lv1=="" || lv2=="" || lv3==""){
            var x=confirm('Please enter low risk Subscribers');
        }
    }else if(lrisk!="between"){
       var lv1=document.myForm.lowbox1.value;
       var lv3=document.myForm.textm.value;
       if(lv1==""){
            var x=confirm('Please enter low risk values');
        }else if(lv3==""){
            var x=confirm('Please enter low risk Subscibers')
        }
    }

}else if(alertinfo=="Y"){
   var dv3=document.myForm.textd.value;
  if(document.myForm.radio1.checked!=true){
      var x=confirm('Please Select PTD or Current')
  }else if(dv3==""){
    var x=confirm('Please enter Informative Alert Subscibers');
  }

}else if(frequency==1 && alertinfo=="N"){
    var x=confirm('Please select Daily Informative Alert');
}
else{
 alert('ok all');
document.myForm.action=jspName;
document.myForm.submit();
}
if(x==true){
    document.myForm.action=jspName;
    document.myForm.submit();
    }
*/
//else if(frequency!=1){

}