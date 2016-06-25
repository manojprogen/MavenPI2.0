<%-- 
    Document   : showComponentsList
    Created on : Oct 15, 2010, 5:34:59 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            String moduleCode = request.getParameter("moduleCode");
            //
             String themeColor="blue";
            if(session.getAttribute("theme")==null)
                      session.setAttribute("theme",themeColor);
                  else
                      themeColor=String.valueOf(session.getAttribute("theme"));

%>

<html>
    <head>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>-->

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Module Components</title>
        <script type="text/javascript">
            $(document).ready(function(){
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=viewComponentList&moduleCode=<%=moduleCode%>',
                    success: function(data){
                        if(data!=null&&data!=""){
                            var dataJson=eval("("+data+")")
                            //var strJSON = encodeURIComponent(JSON.stringify(dataJson));
                          
                            var table="<table>"
                            for(var i=0;i<dataJson.ParentComponentCodes.length;i++){
                                table=table+"<tr>"
                                table=table+"<td>"
                                table=table+"<input type='checkbox' "+dataJson.ParentComponentStatus[i]+" id="+dataJson.ParentComponentCodes[i]+" onclick=findCheckStatus('"+dataJson.ParentComponentCodes[i]+"','<%=moduleCode%>') >"
                                table=table+"</td><td>"+dataJson.ParentComponentNames[i]+"</td>"
                                table=table+"</tr>"

                                var childCodes=dataJson.ParentComponentCodes[i]+"_childCodes";
                                var childNames=dataJson.ParentComponentCodes[i]+"_childNames";
                                var childStatus=dataJson.ParentComponentCodes[i]+"_childStatus";
                                if(dataJson[childCodes]!=null&&dataJson[childCodes].length>0){
                                    var childTable="<tr align='center'><div><td><table id='table"+dataJson.ParentComponentCodes[i]+"'>";
                                    for(var j=0;j<dataJson[childCodes].length;j++){
                                        childTable=childTable+"<tr><td>"
                                        childTable=childTable+"<input type='checkbox' "+dataJson[childStatus][j]+" id="+dataJson[childCodes][j]+" onchange=findChildCheckStatus('"+dataJson[childCodes][j]+"','<%=moduleCode%>') >"
                                        childTable=childTable+"</td><td>"+dataJson[childNames][j]+"</td></tr>"
                                    }

                                    childTable=childTable+"</table></td></div></tr>";
                                    table=table+childTable;
                                }

                            }
                            table=table+"</table><br><table align='center'><tr><td><input type='button' class='navtitle-hover' value='Save' onclick=closeDialog()></td></tr></table>"
                            $("#moduleComponentsBody").html(table);
                        }
                    }
                });
                
               
            });

            function findCheckStatus(chkBoxId,moduleCode){
                var statusObj=document.getElementById(chkBoxId);
                var tableObj=document.getElementById("table"+chkBoxId);
                if(tableObj!=null){
                    var trObj=tableObj.getElementsByTagName('tr');
                    for(var i=0;i<trObj.length;i++){
                        var tdObj=trObj[i].getElementsByTagName('td');
                        var inputObj=tdObj[0].getElementsByTagName('input');
                        if(statusObj.checked){
                         inputObj[0].checked=true;
                        }else{
                          inputObj[0].checked=false;
                        }
                    }
                }
                var status="false";
                if(statusObj.checked){
                    status="true";
                }
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableModuleComponent&from=SuperAdmin&moduleCode='+moduleCode+'&chkBoxId='+chkBoxId+'&status='+status,
                    success: function(data){

                    }
                })

            }
            function findChildCheckStatus(chkBoxId,moduleCode){
                var statusObj=document.getElementById(chkBoxId);
                var status="false";
                if(statusObj.checked){
                    status="true";
                }
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableModuleComponent&from=SuperAdmin&moduleCode='+moduleCode+'&chkBoxId='+chkBoxId+'&status='+status,
                    success: function(data){

                    }
                })
            }
            function closeDialog(){
                parent.closeDialog();
            }

            
        </script>
    </head>
    <body id="moduleComponentsBody">

    </body>

</html>
