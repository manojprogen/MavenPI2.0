function goTrackerSave(jspName)
{ 
    var trackerName=document.getElementById("trackerName").value;
    var strdate=document.getElementById("datepicker").value;
    var enddate=document.getElementById("datepicker1").value;
    var viewCount=document.getElementById("viewCount").value;

    var toAddress=document.getElementById("toAddress").value;

      var EorM=document.getElementById("EorM").value;
      if(EorM=='Email'){


      }else{

          toAddress=document.getElementById("toAddresssms").value;

      }
    // alert('gyhgyg'+toAddress);
    var selectwhen=document.getElementById("selectwhen").value;
    if(trackerName==""){
        alert('Please enter Tracker Name');

    }else if(strdate==""){
        alert('Please enter Start Date');
    }else if(enddate==""){
        alert('Please enter End Date');
    }
    else if(toAddress==""){
        alert('Please enter To Whom You want to send mail');
    }else{
        if(strdate>enddate)
        {
            alert ('Please enter Start-Date less than End-Date');

        }
        else{
            var countall=0;
            var countoperators=0;
            for(var v=0;v<viewCount;v++)
            {
                if(document.getElementById('allView['+v+']').value==""){
                    countall=1;
                    break;
                }

            }
            if(selectwhen=="between"){
                if(document.getElementById("box1").value=="" || document.getElementById("box2").value=="" ){
                    countoperators=1;
                }
            }else{
                if(document.getElementById("when").value==""){
                    countoperators=1;
                }
            }

            if(countall==1){
                alert('Please enter all View By Values');
            }else if(countoperators==1){
                alert('Please enter all the values when you want tracker');
            }else{
                document.getElementById("toAddress").value= toAddress;
                document.myForm.action=jspName;
                            //alert("submit");
                document.myForm.submit();
                parent.cancelTracker();
            }

        }
    }
   
}
