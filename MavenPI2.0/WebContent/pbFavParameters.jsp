<%-- 
    Document   : pbFavParameters
    Created on : Dec 29, 2009, 5:35:31 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<% String contextPath=request.getContextPath();%>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Favourite Parameters</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>-->

        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />

         <style>
             *{font:11px verdana}
         </style>
    </head>



    <body>
        <form name="favParamsForm"  method="post" style="width:100%">
            <%--<div id="favParams" class="white_content"  align="justify" style="height:120px;width:380px">--%>
                <center>
                    <br>

                    <table style="width:100%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">Favourite Name</td>
                            <td valign="top" style="width:60%" align="left">&nbsp;
                                <input type="text" maxlength="35" name="favName" style="width:150px" id="favName" onkeyup="favmsg()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%" align="left">
                                <textarea name="favDesc" id="favDesc" style="width:155px"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" id="save" onclick="saveParamsFav()"></td>
                           <%-- <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelParams()"></td>--%>
                        </tr>
                    </table>

                </center>
          <%--  </div>--%>

        </form>
        <div id="fade" class="black_overlay"></div>
        <script>
            function favmsg(){
                document.getElementById('favDesc').value = document.getElementById('favName').value;
            }

            function saveParamsFav(){
                var favName = document.getElementById('favName').value;
                var favDesc = document.getElementById('favDesc').value;
                var foldersIds=parent.buildFldIds();
                var params=parent.getBuildedParams();
                var paramswithTime=parent.getBuildedParamswithTime();
                //alert('params are '+params)
                //alert('paramswithtime are '+paramswithTime)
                
                if(favName==''){
                    alert("Please enter Favourite Param Name");
                }
                else  if(favDesc==''){
                    alert("Please enter Favourite Param Description")
                }
                else  if(foldersIds==''){
                    alert("Please Select atleast one Business Role")
                }
                else{
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=checkParamName&favName='+favName,
                        success: function(data){
                            if(data!=""){
                                //alert('in if')
                                document.getElementById('duplicate').innerHTML = "Favourite Param Name already exists";
                                document.getElementById('save').disabled = true;
                            }
                            else if(data==''){
                                //alert('in if else')
                                document.forms.favParamsForm.action = "reportTemplateAction.do?templateParam=saveFavParameters&favName="+favName+"&favDesc="+favDesc+"&foldersIds="+foldersIds+"&paramswithTime="+paramswithTime;
                                document.forms.favParamsForm.submit();
                                parent.$("#favouriteParamsDialog").dialog('close');
                                <%--document.getElementById('favParams').style.display='none';
                                parent.document.getElementById('favouriteParams').style.display='none';
                                parent.document.getElementById('fade').style.display='none';--%>
                            }
                        }
                    });
                }
            }
            function cancelParams(){
                 parent.$("#favouriteParamsDialog").dialog('close');
                <%--document.getElementById('favParams').style.display='none';
                parent.document.getElementById('favouriteParams').style.display='none';
                parent.document.getElementById('fade').style.display='none';--%>

            }
        </script>
    </body>
</html>
