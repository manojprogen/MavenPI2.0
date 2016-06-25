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
            String userId = request.getParameter("userId");
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
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=getModuleComponentsForUserAssignment&moduleCode=<%=moduleCode%>&userId=<%=userId%>',
                    success: function(data){
                        if(data!=null&&data!=""){
                            var dataJson=eval("("+data+")")
                            //var strJSON = encodeURIComponent(JSON.stringify(dataJson));

                            var table="<table id='ComponentsTable'>"
                            for(var i=0;i<dataJson.ParentComponentCodes.length;i++){
                                var totdata=dataJson.ParentComponentCodes[i]+",<%=moduleCode%>,";
                                table=table+"<tr>"
                                table=table+"<td>"
                                table=table+"<input type='checkbox' name='componentCheckBoxes' "+dataJson.ParentComponentStatus[i]+" id="+dataJson.ParentComponentCodes[i]+" onclick=findCheckStatus('"+dataJson.ParentComponentCodes[i]+"','<%=moduleCode%>','')  value="+totdata+" >"
                                table=table+"</td><td>"+dataJson.ParentComponentNames[i]+"</td>"
                                table=table+"</tr>"

                                var childCodes=dataJson.ParentComponentCodes[i]+"_childCodes";
                                var childNames=dataJson.ParentComponentCodes[i]+"_childNames";
                                var childStatus=dataJson.ParentComponentCodes[i]+"_status";

                                if(dataJson[childCodes]!=null&&dataJson[childCodes].length>0){
                                    var childData=dataJson[childCodes][j]+",<%=moduleCode%>,"+dataJson.ParentComponentCodes[i];
                                    var childTable="<tr align='center'><div><td><table id='table"+dataJson.ParentComponentCodes[i]+"'>";
                                    for(var j=0;j<dataJson[childCodes].length;j++){
                                        childTable=childTable+"<tr><td>"
                                        childTable=childTable+"<input type='checkbox' name='componentCheckBoxes' "+dataJson[childStatus][j]+" id="+dataJson[childCodes][j]+" onchange=findCheckStatus('"+dataJson[childCodes][j]+"','<%=moduleCode%>','"+dataJson.ParentComponentCodes[i]+"') >"
                                        childTable=childTable+"</td><td>"+dataJson[childNames][j]+"</td></tr>"
                                    }

                                    childTable=childTable+"</table></td></div></tr>";
                                    table=table+childTable;
                                }

                            }
                            table=table+"</table><br><table align='center'><tr><td><input type='button' class='navtitle-hover' value='Enable All' onclick=checkAll()></td><td><input type='button' class='navtitle-hover' value='Disable All' onclick=uncheckAll()></td></tr></table>"
                            table=table+"<table align='center'><tr><td><input type='button' class='navtitle-hover' value='Save' onclick=closeDialog()></td></tr></table>"
                            $("#moduleComponentsBody").html(table);
                        }
                    }
                });


            });

            function checkAll(){
                var compCheckBoxes=document.getElementsByName("componentCheckBoxes");
                for(var i=0;i<compCheckBoxes.length;i++){
                    var chkBxId=compCheckBoxes[i].id;
                    var value=$("#"+chkBxId).attr("value")
                  //  var data=value.split(",");
                    $("#"+chkBxId).attr('checked','checked')
                    // findCheckStatus(data[0], data[1], data[2]);

                }
//                var compChildCheckBoxes=document.getElementsByName("componentChildCheckBoxes");
//                for(var i=0;i<compCheckBoxes.length;i++){
//                    var chkBxId=compCheckBoxes[i].id;
//                    var value=$("#"+chkBxId).attr("value")
//                    var data=value.split(",");
//                    $("#"+chkBxId).attr('checked','checked')
//                   // findCheckStatus(data[0], data[1], data[2]);
//                }
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableAllModuleComponentsForUser&moduleCode=<%=moduleCode%>&userId=<%=userId%>&status=true',
                    success: function(data){

                    }
                })
            }
            function uncheckAll(){
                var compCheckBoxes=document.getElementsByName("componentCheckBoxes");
                for(var i=0;i<compCheckBoxes.length;i++){
                    var chkBxId=compCheckBoxes[i].id;
                    var value=$("#"+chkBxId).attr("value")
                    //var data=value.split(",");
                    $("#"+chkBxId).attr('checked',false)
                   // findCheckStatus(data[0], data[1], data[2]);
                }
//                var compChildCheckBoxes=document.getElementsByName("componentChildCheckBoxes");
//                for(var i=0;i<compCheckBoxes.length;i++){
//                    var chkBxId=compCheckBoxes[i].id;
//                    var value=$("#"+chkBxId).attr("value")
//                    var data=value.split(",");
//                    $("#"+chkBxId).attr('checked',false)
//                   // findCheckStatus(data[0], data[1], data[2]);
//                }
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableAllModuleComponentsForUser&moduleCode=<%=moduleCode%>&userId=<%=userId%>&status=false',
                    success: function(data){

                    }
                })
            }

            function findCheckStatus(chkBoxId,moduleCode,parCompCode){
                var statusObj=document.getElementById(chkBoxId);
                var tableObj=document.getElementById("table"+chkBoxId);
                if(tableObj!=null&&parCompCode==""){
                    var trObj=tableObj.getElementsByTagName('tr');
                    for(var i=0;i<trObj.length;i++){
                        var tdObj=trObj[i].getElementsByTagName('td');
                        var inputObj=tdObj[0].getElementsByTagName('input');
                        var childStatus=statusObj.checked;

                        if(statusObj.checked){
                            inputObj[0].checked=true;
                        }else{
                            inputObj[0].checked=false;
                        }

                        $.ajax({
                            url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableModuleComponentForUser&moduleCode='+moduleCode+'&compCode='+inputObj[0].id+'&status='+childStatus+'&parCompCode='+chkBoxId+'&userId=<%=userId%>',
                            success: function(data){

                            }
                        })
                    }
                }
                var status="false";
                if(statusObj.checked){
                    status="true";
                }
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableModuleComponentForUser&moduleCode='+moduleCode+'&compCode='+chkBoxId+'&status='+status+'&parCompCode='+parCompCode+'&userId=<%=userId%>',
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
