<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
    Document   : configEmail
    Created on : Sep 22, 2012, 01:00:01 PM
    Author     : Nazneen Khan
--%>



<!DOCTYPE html>

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
String contextPath=request.getContextPath();
         %>
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />
      
     <style type="text/css">
         .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
                border:0px;
            }
      </style>

      
    </head>
    <body>
        <script type="text/javascript">
            
            $(document).ready(function()
            {                
                  $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getEmailConfigDetails",
                  function(data){
                        var jsonVar=eval('('+data+')')
                        var hostName=jsonVar.hostName
                        var portNo=jsonVar.portNo
                        var fromAdd=jsonVar.fromAdd
                        var checkDebug=jsonVar.debug
                        var userId=jsonVar.userId
                        var password=jsonVar.password
                        $("#hostName").val(hostName)
                        $("#portNo").val(portNo)
                        $("#fromAdd").val(fromAdd)
                        $("#checkDebug").val(checkDebug)                       
                        $("#userId").val(userId)
                        $("#pswd").val(password)
                   }); 
             });

           function validateForm(){
              var hostName=$("#hostName").val()
              var portNo=$("#portNo").val()
              var fromAdd=$("#fromAdd").val()
              var checkDebug=$("#checkDebug").val()
              var userId=$("#userId").val()
              var password=$("#pswd").val()
              var sslStatus=document.getElementById("sslst1").checked;
                
                if(allLetter(hostName))  
                { 
                    if(allnumeric(portNo))  
                    {
                        if(ValidateEmailfromAdd(fromAdd))  
                        {  
                            if(ValidatecheckDebug(checkDebug))  
                            { 
                                if(ValidateEmailuserId(userId))  
                                {
                                    if(passid_validation(password))  
                                    {
                                        $.ajax({
                                       data:{"password":password},
                                       url: "<%=request.getContextPath()%>/userLayerAction.do?userParam=saveEmailConfigDetails&hostName="+hostName+"&portNo="+portNo+"&fromAdd="+fromAdd+"&checkDebug="+checkDebug+"&userId="+userId+"&sslStatus="+sslStatus,
                                        success: function(data){
                                        if(data=="1"){
                                                  alert("Details Saved Successfully.") 
                                              }else{
                                                  alert("Error! Details not Saved")
                                              }
                                        }
                                        });                                        
                                    }  
                                }   
                            }                              
                        }
                    }
                }
                return false;
           }
           function passid_validation(password){              
               var passid_len = password.length;
               if (passid_len == 0) {  
                    alert("Password should not be empty");  
                    password.focus();  
                    return false;  
               }               
               else 
                   return true;
           }
       function allLetter(hostName){
          var hostName_len = hostName.length;
               if (hostName_len == 0) {  
                    alert("Host Name should not be empty");  
                    hostName.focus();  
                    return false;  
               } 
               else
                   return true;
       }
       function ValidateEmailfromAdd(fromAdd){
           var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;  
            if(fromAdd.match(mailformat) && fromAdd!="")  
            {  
                return true;  
            }  
            else  
            {  
                alert("From Address should not be empty /You have entered an invalid from Address!");  
                fromAdd.focus();  
                return false;  
            }            
       }
       function ValidateEmailuserId(userId){
//            var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;  
//            if(userId.match(mailformat) && userId!="")  
            if(userId!="")  
            {  
                return true;  
            }  
            else  
            {  
                alert("User id should not be empty!");  
                userId.focus();  
                return false;  
            }  
       }
       function ValidatecheckDebug(checkDebug){
           if(checkDebug=='true' || checkDebug=='false' && checkDebug!=""){
               return true;
           }
           else {
            alert("Debug should not be empty /Value should be in small case /You have entered an invalid Check debug!");  
            checkDebug.focus();  
            return false; 
           }                   
       }
      function allnumeric(portNo){   
            var numbers = /^[0-9]+$/;  
            if(portNo!="" && portNo.match(numbers))  
            {  
                return true;  
            }  
            else  
            {  
                alert('Port number should not be empty / Port number must have numeric characters only');  
                portNo.focus();  
                return false;  
            }  
        }  
//     function passid_validation(passid,mx,my)  
//        {  
//            var passid_len = passid.value.length;  
//            if (passid_len == 0 ||passid_len >= my || passid_len < mx)  
//            {  
//            alert("Password should not be empty / length be between "+mx+" to "+my);  
//            passid.focus();  
//            return false;  
//            }  
//            return true;  
//        }  
       
             
       </script>
        <table border="1" align="center">
            <h3 align="center">Configure Email </h3>
        </table>  
        <div align="center" style=" width: 100%"> 
        <form id="configEmailForm" name="configEmailForm" action="javascript:void(0)" onsubmit="return validateForm();">
            <br><br><br>
            <table>
            <tr>
                <table  border = "2" align="center" cellpadding="1" cellspacing="1">
                    <tr>
                        <td align="left" class="migrate" colspan="2"><label style="color:Red">*</label> All fields are compulsory </td>
                    </tr>
                    <tr>
                        <td align="left" class="migrate">
                            <label>Host Name : &nbsp;&nbsp;&nbsp;&nbsp;</label>
                        </td>
                        <td align="left">
                            <input type="text" class="" value="" id="hostName" name="hostName" size="35px" style="background-color:lightgoldenrodyellow; color:black;"/>
                            
                        </td>
                    </tr>   
                    <tr>
                        <td align="left" class="migrate">
                            <label>Port Number : </label>
                            <label style="color:red;">(Must be number only) </label>
                        </td>
                        <td align="left">
                            <input type="text" class="" value="" id="portNo" name="portNo" size="35px" style="background-color:lightgoldenrodyellow; color:black;"/>
                        </td>
                    </tr>  
                    <tr>
                        <td align="left" class="migrate">
                            <label>From Address : </label>
                        </td>
                        <td align="left">
                            <input type="text" class="" value="" id="fromAdd" name="fromAdd" size="35px" style="background-color:lightgoldenrodyellow; color:black;"/>
                        </td>
                    </tr>  
                    <tr>
                        <td align="left" class="migrate">
                            <label>Debug : </label>
                            <label style="color:red;">(Must be either true or false) </label>
                       </td>
                        <td align="left">
                            <input type="text" class="" value="" id="checkDebug" name="checkDebug" size="35px" style="background-color:lightgoldenrodyellow; color:black;"/>
                       </td>
                    </tr>  
                    <tr>
                        <td align="left" class="migrate">
                            <label>User Id :</label>
                        </td>
                        <td align="left">
                            <input type="text" class="" value="" id="userId" name="userId" size="35px" style="background-color:lightgoldenrodyellow; color:black;"/>
                        </td>
                    </tr>  
                    <tr>
                        <td align="left" class="migrate">
                            <label>Password : </label>
                        </td>
                        <td align="left">
                            <input type="password" class="" value="" id="pswd" name="pswd" size="35px" style="background-color:lightgoldenrodyellow; color:black;"/>
                        </td>
                    </tr>  
                    <tr>
                        <td align="left" class="migrate">
                            <label>SSLStatus : </label>
                        </td>
                        <td align="left">
                            Enable:  <input type="radio" class="" value="true" id="sslst1" name="2" size="10px" checked/>&nbsp;&nbsp;&nbsp;
                            Disable: <input type="radio" name="2" id="sslst2" onclick="" value="true" />
                        </td>
                    </tr>
                    </table>                
            </tr>
            <br>
             <tr>
                <td colspan="2">
                &nbsp;
                </td>
            </tr>
            <tr>
            <td colspan="2">
                <center><input type="submit" class="migrate" style="width:auto;color:black" value="Update Details" id="btnn"/></center>
            </td>
        </tr> 
    </table>       
             
       </form> 
            <div>
     </body>
</html>