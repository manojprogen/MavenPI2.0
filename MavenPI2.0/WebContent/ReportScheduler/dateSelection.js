
var months = new Array() // array holding months of the year
months[1] = "Jan" // start array index at 1 for easy reading
months[2] = "Feb"
months[3] = "Mar"
months[4] = "Apr"
months[5] = "May"
months[6] = "Jun"
months[7] = "Jul"
months[8] = "Augt"
months[9] = "Sep"
months[10] = "Oct"
months[11] = "Nov"
months[12] = "Dec"

var today = new Date() // create new Date object
var month = today.getMonth()+1 // store integer representing current month in variable, "month". add 1 for easy reading
var date = today.getDate() // store current date in variable, "date"

function correctDate(form,monthMenu){
// 'form' is a reference to the form, 'monthMenu' is a reference to the 1st menu in that form. both are passed from the onChange event handler of the 1st select menu which invokes this function
// loop through the form's elements for the purpose of creating a variable representing the 2nd menu
 for(i = 0; i < form.elements.length; i++){
    if(form.elements[i] == monthMenu){
    var dateMenu = form.elements[i+1]
    break
    }
  }
var formerlength = dateMenu.options.length //formerlength is number of options currently in menu
var thelength // thelength is number of options the menu will have, based on the month selected
// if 2nd option (feb) is selected,
  if(monthMenu.options[1].selected){
  thelength = 28 // thelength is 28
  }
// if 3rd (apr), 5th (jun), 8th (sep) or 10th (nov) option is selected,
  else if(monthMenu.options[3].selected || monthMenu.options[5].selected || monthMenu.options[8].selected || monthMenu.options[10].selected){
  thelength = 30 // thelength is 30
  }
  else{
  thelength = 31 //otherwise thelength is 31
  }

dateMenu.options.length = thelength //re-set the number of options displayed in dateMenu

  for(i=formerlength; i<thelength; i++){ // if number of current options is less than options to be displayed, write more options
  dateMenu.options[i].value=i+1 // give each option a value and text label
  dateMenu.options[i].text=i+1
  }

dateMenu.selectedIndex = thelength-1 // display the last day of selected month

}

//add dynamically

function addDate(date)
{
   // alert("inn function date=="+date.value);
    if(date.value=="4")
      {
          document.getElementById('dateSelect').style.display='';
         }
      else{
         // alert('in esl=======')
          document.getElementById('dateSelect').style.display='none';
      }
      if(date.value=="2")
      {
          document.getElementById('onlyDateSelect').style.display='';
         }
      else{
         // alert('in esl=======')
          document.getElementById('onlyDateSelect').style.display='none';
      }
      if(date.value=="1")
      {
          document.getElementById('timeselect').style.display='';
         }
      else{
         // alert('in esl=======')
          document.getElementById('timeselect').style.display='none';
      }
}

function addHighRisk(risk)
{  
   if(risk.value=="between")
      { 
          document.getElementById('doubleRisk').style.display='';
          document.getElementById('singleRisk').style.display='none';
     }
      else{

           document.getElementById('doubleRisk').style.display='none';
           document.getElementById('singleRisk').style.display='';
      }

}
function addMediumRisk(risk)
{
   if(risk.value=="between")
      {
          document.getElementById('mediumdoubleRisk').style.display='';
          document.getElementById('mediumsingleRisk').style.display='none';
     }
      else{

           document.getElementById('mediumdoubleRisk').style.display='none';
           document.getElementById('mediumsingleRisk').style.display='';
      }

}
function addLowRisk(risk)
{
   if(risk.value=="between")
      {
          document.getElementById('lowdoubleRisk').style.display='';
          document.getElementById('lowsingleRisk').style.display='none';
     }
      else{

           document.getElementById('lowdoubleRisk').style.display='none';
           document.getElementById('lowsingleRisk').style.display='';
      }

}
function addinfoRadio(time)
{
   // alert("inn function date=="+date.value);
    if(time.value=="Y")
      {
          document.getElementById('infoAlertDiv').style.display='';
         }
      else{
         // alert('in esl=======')
          document.getElementById('infoAlertDiv').style.display='none';
      }

}