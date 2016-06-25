        function checkSave(jspName)
          {
          // var x=confirm('Are you sure you want to Submit');
           //if(x==true)
          // {
           document.myForm.action=jspName;
           document.myForm.submit();
         //  }
          parent.cancelPerLinks();
          }
          function checkCancel(jspName)
          {
                
                   document.myForm.action=jspName;
                   document.myForm.submit();
              
         }
     function checkEdit(msg,jspName)
     {
        //document.forms.ec.ec_eti.value='';
           var i=0;
           //var obj = document.myForm.chk1;
           var obj = document.forms.ec.chk2;
           if(isNaN(obj.length))
            {
              if(document.forms.ec.chk2.checked)
               {
                document.forms.ec.action=jspName;
                document.forms.ec.submit();
               }
              else
               {
                alert('Please select '+msg);
               }
            }
          else 
              {
                for(var j=0;j<obj.length;j++)
               {
                if(document.forms.ec.chk2[j].checked==true)
                { 
                 i++;
                // alert(document.forms.ec.chk2[j].value) 

                }
               } 

              if(i>1) 
               { 
                alert('Please select only one '+msg);
               }
              else if(i==0)
               { 
                alert('Please select '+msg); 
               }
              else
               { 
                document.forms.ec.action=jspName;
                document.forms.ec.submit();
               }
           }
     } 

    function checkCreate(jspName)
 {
         document.forms.ec.action=jspName;
         document.forms.ec.submit();
 }

 function checkDelete(msg,jspName)
 {
    var i=0;
    var obj = document.forms.ec.chk2;
     if(isNaN(obj.length))
      {
         if(document.forms.ec.chk2.checked)
          {
             var x=confirm('Are you sure you want to Delete '+msg+'(s)');
           if(x==true)
           {
           document.forms.ec.action=jspName;
           document.forms.ec.submit();
           }
          }
          else
           {
            alert('Please select '+msg +'(s)')
           }
       }
    else
    {
       for(var j=0;j<obj.length;j++)
        {
          if(document.forms.ec.chk2[j].checked==true)
            {
             i++;
             //alert(document.forms.ec.chk2[j].value)
            }
          }
         if(i==0)
         {

          alert('Please select '+msg+'(s)')
         }
         else
         {
           var x=confirm('Are you sure you want to Delete '+msg+'(s)');
           if(x==true)
           {
         document.forms.ec.action=jspName;
         document.forms.ec.submit();
           }
       }
      }
 }

  function submiturls1($ch)
     {
          //alert($ch);
          /*if(document.frmParameter.REPORTTYPE.value.toLowerCase() =='Progen 3D Report'.toLowerCase()
                  || document.frmParameter.REPORTTYPE.value.toLowerCase() =='Progen 3D Trend'.toLowerCase() )
                  alert($ch.substring(1,$ch.lastIndexOf(' -')));*/
          document.frmParameter.action = $ch;
          document.frmParameter.target ="_blank";
          document.frmParameter.submit();
     }
