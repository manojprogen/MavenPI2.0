var isCompanyValid=false;



function validate()
{
    var loginId = document.myForm.loginId.value;
    var firstName = document.myForm.firstName.value;
    var lastName = document.myForm.lastName.value;
    var password = document.myForm.password.value;
    var confirmPassword = document.myForm.confirmPassword.value;
    var email = document.myForm.email.value;
    var account="";
    if(isCompanyValid){
        account = document.myForm.account.value;
    }

    var userType = document.myForm.uType.value;
//   alert(loginId)
    // var busAreas[] = document.myForm.bussArea.value;
    invalidChars = "' /:,;";
    cn = /[0-9]/;
    $.post("superAdminAction.do?superAdminParam=checkLoginId&loginId="+loginId,
      
       function(data)
       {
            
         if(data=='true')
         {
       
             alert("User Id already Exists. Please enter a different User Id");
             myForm.loginId.focus();
             return false;
         }
         else
         {
            if(loginId == null ||loginId =="")
            {
             alert("Please Enter User Id");
             myForm.loginId.focus();
             return false;
            }
            else if(loginId.length > 0 && loginId.length < 4)
            {
                //alert('loginId.length is: '+loginId.length)
                alert("User Id must contain atleast 4 characters");
             myForm.loginId.focus();
            return false;
            }
            else if(loginId.length > 0 && loginId.length > 32)
            {
                alert("User Id contain upto 32 characters only");
             myForm.loginId.focus();
            return false;
            }
            else if(firstName==null  ||firstName =="")
            {
                alert("Please enter first name");
            myForm.firstName.focus();
            return false;
            }
            else if(lastName==null  ||lastName =="")
            {
                alert("Please enter last name");
            myForm.lastName.focus();
            return false;
            }
            else if(password==null  ||password =="")
            {
                alert("Please Enter Password");
             myForm.password.focus();
            return false;
            }
            else if(password.length < 4)
            {
                alert("Password must contain atleast 4 characters");
            myForm.password.focus();
            return false;
            }
            else if(password.length > 31)
            {
                alert("Password contain upto 32 characters only");
            myForm.password.focus();
            return false;
            }
            else if(confirmPassword==null ||confirmPassword=="" )
            {
                alert("Please Enter Confirm Password");
            myForm.confirmPassword.focus();
            return false;
            }
            else if(password!=confirmPassword)
            {
                alert("Passwords do not match");
             myForm.confirmPassword.focus();
            return false;
            }
//            else if (email==null || email=="")
//            {
//                alert("Please Enter E-mail");
//             myForm.email.focus();
//            return false;
//            }
            //un comment for indicus only start

            else if ((account==null || account=="") && isCompanyValid==true)
            {

                alert("Please Select Company Name");

            }
            else if (userType==null || userType=="")
            {
                alert("Please Select User Type");
            }
            //ends here
            //commented for email not mandatory
            else if(email.length > 0)
            {
                var c=0;
                for (i = 0; i< invalidChars.length; i++)
                {
                    badChar = invalidChars.charAt(i)
                    if (email.indexOf(badChar,0) != -1)
                    {
                        c=1;
                        alert("You can't use following characters " + invalidChars +" in your Email address.");
                     myForm.email.focus();
                     return false;
                    }
                }
                atPos = email.indexOf("@",1);
                if (atPos == -1)
                {
                    c=1;
                    alert("You need to provide your Email UserId. i.e  your email should be in this format info@ezcommerceinc.com");
                 myForm.email.focus();
                 return false;
                }
                if (email.indexOf("@",atPos+1) != -1)
                {
                    c=1;
                    alert("The Email address you have provided does not have '@' symbol. Please enter valid Email address.");
                 myForm.email.focus();
                 return false;
                }
                periodPos = email.lastIndexOf(".")
                if (periodPos == -1)
                {
                    c=1;
                    alert("The Email address you have provided does not have .com or .net etc. Please provide a valid Email address.");
                 myForm.email.focus();
                return false;
                }
                if (! ( (periodPos+3 == email.length) || (periodPos+4  == email.length) ))
                {
                    c=1;
                    alert("The Email address you have provided does not have .com or .net etc. Please provide a valid Email address.");
                  myForm.email.focus();
                 return false;
                }

        //        if(c==0){
        //      if(isCompanyValid){
        //
        //            $.ajax({
        //                url: 'organisationDetails.do?param=checkUserName&userName='+loginId+'&orgId='+account,
        //                success: function(data) {
        //                    //alert('data '+data);
        //                    if(data==1){
        //                        alert("Same user login name already exists for this account")
        //                        document.myForm.loginId.focus();
        //                    }
        //                    else if(data==2){
        //                        $.ajax({
        //                            url: 'organisationDetails.do?param=checkRoleAssignment&orgId='+account,
        //                            success: function(data) {
        //                                //alert(data)
        //                                if(data==1){
        //                                    var x=confirm("You want to Assign all the reports existing for this Account to this User");
        //                                    if(x==true){
        //                                        document.getElementById("AssignReports").value="Y";
        //                                        document.myForm.action = "userInsert.do";
        //                                        document.myForm.submit();
        //                                        parent.afterSaveUser();
        //                                    }else{
        //                                        document.getElementById("AssignReports").value="N";
        //                                        document.myForm.action = "userInsert.do";
        //                                        document.myForm.submit();
        //                                        parent.afterSaveUser();
        //
        //                                    }
        //                                }else{
        //                                    document.getElementById("AssignReports").value="N";
        //                                    document.myForm.action = "userInsert.do";
        //                                    document.myForm.submit();
        //                                    parent.afterSaveUser();
        //                                }
        //                            }
        //                        });
        //
        //                    }
        //
        //
        //                }
        //            });
        //
        //      }else{
        //
        //           document.myForm.action = "userInsert.do";
        //            document.myForm.submit();
        //              parent.afterSaveUser();
        //      }
        //
        //        }
            }

        //    else
        //    {
              if(isCompanyValid){

                    $.ajax({
                        url: 'organisationDetails.do?param=checkUserName&userName='+loginId+'&orgId='+account,
                        success: function(data) {
                        //alert('data -- '+data);
                        if(data==1){
                            alert("Username already exists for this account");
                            document.myForm.loginId.focus();
                        }
                        else if(data==2){

                            $.ajax({
                                url: 'organisationDetails.do?param=checkRoleAssignment&orgId='+account,
                                success: function(data) {
                                    //alert(data)
                                    if(data==1){
        //                                Commented for not showing alert
        //                                var x=confirm("You want to Assign all the reports existing for this Account to this User");
        //                                if(x==true){
                                            document.getElementById("AssignReports").value="Y";
                                            document.myForm.action = "userInsert.do";
                                            document.myForm.submit();
                                            parent.afterSaveUser();
        //                                }else{
        //                                    document.getElementById("AssignReports").value="N";
        //                                    document.myForm.action = "userInsert.do";
        //                                    document.myForm.submit();
        //                                    parent.afterSaveUser();

        //                                }
                                    }else{
                                        document.getElementById("AssignReports").value="N";
                                        document.myForm.action = "userInsert.do";
                                        document.myForm.submit();
                                        parent.afterSaveUser();
                                    }
                                }
                            });



                        }
                    }
                });

            }else{
                $.ajax({
                         url: "superAdminAction.do?superAdminParam=checkUserPrivileges&userTypeId="+userType,
                         success: function(data){
                             if(data!="available")
                            {
                               var conf=confirm(data);
                               if(conf==true)
                               {
        //                            document.myForm.action = "userInsert.do";
        //                            document.myForm.submit();
                                   alert("New User Created Successfully");
                                   $.post("userInsert.do", $("#myForm").serialize() ,
                                   function(data){
        //                               document.myForm.action="AdminTab.jsp#User_Creation"
        //                               document.myForm.submit();
                                       document.location.href=document.location.href;
                                       parent.afterSaveUser();
                                   });
                               }else{
                                   return false;
                               }
                            }else{
                               alert("New User Created Successfully");
                                   $.post("userInsert.do", $("#myForm").serialize() ,
                                   function(data){
        //                                 document.myForm.action="AdminTab.jsp#User_Creation"
        //                                 document.myForm.submit();
                                           document.location.href=document.location.href;
                                           parent.afterSaveUser();
                                   });
        //                         document.myForm.action = "userInsert.do";
        //                         document.myForm.submit();
        //                         parent.afterSaveUser();
                            }

                          }
                 });

        //        document.myForm.action = "userInsert.do";
        //        document.myForm.submit();
        //        parent.afterSaveUser();
        //    }

            }
                            }

            })
   
    
}

function isNumberKey(evt)
{
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}
function goBack()
{
    document.myForm.action="pbUserList.jsp";
    document.myForm.submit();
}
function checkCreate()
{
    document.myForm.action="pbUserRegister.jsp";
    document.myForm.submit();
}

function checkEdit()
{
    var i=0;
    var obj = document.myForm.chkusers;
    // chk1= document.myForm.chk1

    if(isNaN(obj.length))
    {
        if(document.myForm.chkusers.checked)
        {
            document.myForm.action="pbUserGetUpdate.jsp";
            document.myForm.submit();
        }
        else
        {
            alert("Please select user to update")
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.myForm.chkusers[j].checked==true)
            {
                i++;
                var userId=document.myForm.chkusers[j].value;
            //alert('in second if:  '+document.myForm.chkusers[j].value)
            //alert(document.myForm.chk1[j].name)
            }
        }

        if(i>1)
        {
            alert("Please select only one user to update");
        }
        else if(i==0)
        {
            alert("Please select user to update");
        }
        else
        {

            document.myForm.action="pbUserGetUpdate.jsp?userId="+userId;
            document.myForm.submit();
        }
    }
}

function checkDelete()
{
    var i=0;
    var obj = document.myForm.chkusers;
    if(isNaN(obj.length))
    {
        if(document.myForm.chkusers.checked)
        {
            var r = confirm("Are you sure you want to delete user(s)");
            if(r==true)
            {
                document.myForm.action="pbUserDelete.jsp";
                document.myForm.submit();
            }
        }
        else
        {
            alert("Please select user(s) to delete")
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.myForm.chkusers[j].checked==true)
            {              
                i++;
          
            }
        }
        if(i==0)
        {
            alert("Please select user(s) to delete ");
        }
        else
        {
            var t = confirm("Are you sure you want to delete user(s)");
            if(t==true)
            {
                document.myForm.action="pbUserDelete.jsp";
                document.myForm.submit();
            }
        }
    }
}

function imposeMaxLength(Object, MaxLen)
{
    return (Object.value.length <= MaxLen);
}

















function funEmail(sValue)
{

    var s="";
    invalidChars = "' /:,;";
    if (sValue == "")
    {
        return false;
    }

    for (i = 0; i< invalidChars.length; i++)
    {
        badChar = invalidChars.charAt(i)
        if (sValue.indexOf(badChar,0) != -1)
        {
            msgString = "You can't use following characters " + invalidChars +" in your Email address."
            return false;
        }
    }
    atPos = sValue.indexOf("@",1)
    if (atPos == -1)
    {

        msgString = "You need to provide your Email UserId. i.e  your email should be in this format info@ezcommerceinc.com ."
        return false;
    }
    if (sValue.indexOf("@",atPos+1) != -1)
    {
        msgString ="The Email address you have provided does not have @ symbol. Please enter valid Email address."
        return false;
    }

    periodPos = sValue.lastIndexOf(".")
    if (periodPos == -1)
    {
        msgString ="The Email address you have provided does not have .com or .net etc. Please provide a valid Email address."

        return false;
    }
    if (! ( (periodPos+3 == sValue.length) || (periodPos+4  == sValue.length) ))
    {
        msgString ="The Email address you have provided does not have .com or .net etc. Please provide a valid Email address."
        return false;
    }
    return true;
}




























