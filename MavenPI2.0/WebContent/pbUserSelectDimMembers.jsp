<%--<%@page import="prg.db.PbReturnObject"%>--%>
<%@page import="java.util.*,com.progen.userlayer.db.UserLayerDAO"%>

<%
            String userMemId = request.getParameter("userDimId");//+"&subFolderIdUser="+subFolderIdUser+"&userId="+userId
            String subFolderIdUser = request.getParameter("subFolderIdUser");
            String userId = request.getParameter("userId");
            String MemberName = request.getParameter("MemberName");
            ////////////////////////////////////////////////////////////////////////////.println.println(" userId "+userId+" userDimId "+userMemId+" subFolderIdUser "+subFolderIdUser);//getMemberValuesForDim
            MemberName = MemberName.replace("~", " ");
            UserLayerDAO uDao = new UserLayerDAO();
            //   HashMap details =uDao.getMemberValuesForDim(userMemId,subFolderIdUser);
            // ArrayList insertedMemberValues = new ArrayList();//"";//
            //  insertedMemberValues = uDao.getAddedMemberValues(userId,subFolderIdUser,userMemId);

            ////////////////////////////////////////////////////////////////////////////.println.println(" details "+details);
            ////////////////////////////////////////////////////////////////////////////.println.println(" insertedMemberValues "+insertedMemberValues);
            // String detailsArr[]=(String[])details.keySet().toArray(new String[0]);
            //  String allVals = "";
            // boolean dataFlag=false;
            /////////////////////////////////////////////////////////////////////////////.println.println(" insertedMemberValues "+insertedMemberValues.size());
            //  if(insertedMemberValues.size()>0)
            //  dataFlag = true;
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/pbUserLayer.js"></script>
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
        <script type="text/javascript">
$(document).ready(function() {
                alert("in ready "+<%=userMemId%>)
                var  ctxPath='<%=request.getContextPath()%>';

                $.ajax({
                    url:ctxPath+'/userLayerAction.do?userParam=getDimentionMembers&userMemId=<%=userMemId%>&subFolderIdUser=<%=subFolderIdUser%>&userId=<%=userId%>&MemberName=<%=MemberName%>',
                    success: function(data) {
                        
                        var jsonVar=eval('('+data+')')
                        $("#membersDetailsTable").html("")
                        $("#membersDetailsTable").html(jsonVar.htmlStr)
                        alert(jsonVar.memberValues.length)
                        grpColArray=jsonVar.memberValues
                        $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        })
                        $(".myDragTabs").draggable({
                            helper:"clone",
                            effect:["", "fade"]
                        });
                        $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                 createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
                    );
                      $(".sortable").sortable();

                    }
                     
                });
            });
                
</script>
        
        <style type="text/css">
           *{
                -x-system-font: none;
                font-family: verdana;
                font-size: 11px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>
    </head>

    <body>
        <Center> <div height="50px"> <font style="color:black;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">Select Values To Restrict Access</font></div><Br>

            <Table id="membersDetailsTable" width="100%" >

            </Table>
            <!--        <Input type="HIDDEN" name="allVals" id="allVals" value="">-->
            <Input type="HIDDEN" name="userId" id="userId" value="<%=userId%>">
            <Input type="HIDDEN" name="subFolderIdUser" id="subFolderIdUser" value="<%=subFolderIdUser%>">
            <Input type="HIDDEN" name="userMemId" id="userMemId" value="<%=userMemId%>">
            <Br/>
            <Br/>
            <Table>
                <Tr>
                    <Td align="center"><Input TYPE="button" value="Reset To Default" ONCLICK="updateVals()"></Td>
                    <Td align="center"><Input TYPE="button" value="Save" ONCLICK="saveSelectVals()"></Td>
                    <Td align="center"><Input TYPE="button" value="Cancel" ONCLICK="cancelSelectVals()"></Td>
                </Tr>
            </Table>
        </Center>
            <script type="text/javascript">
            

            function updateVals()
            {
                var userMemId = document.getElementById("userMemId").value;
                var userId = document.getElementById("userId").value;
                var subFolderIdUser = document.getElementById("subFolderIdUser").value;
                var totalUrl = "userId="+userId+"~subFolderIdUser="+subFolderIdUser+"~userMemId="+userMemId;
                // alert('Are you sure you want to remove the access.');
                var t=confirm('Are you sure you want to remove the access.');
                if(t==true){
                    $.ajax({
                        url: 'userLayerAction.do?userParam=deleteDimMemberValues&totalUrl='+totalUrl,
                        success: function(data) {
                            // alert('data '+data);
                            if(data=="1")
                                alert("Values Not Saved.")
                            else{
                                alert('Values Set To Default.');
                                cancelSelectValsParent();
                            }

                        }
                    });
                }
            }
            function cancelSelectValsParent()
            {
                parent.cancelSelectValsParent();
            }
            function cancelSelectVals()
            {
                cancelSelectValsParent();
            }
            function saveSelectVals()
            {
                var totalUrl="";
                var userMemId = document.getElementById("userMemId").value;
                var userId = document.getElementById("userId").value;
                var allVals = document.getElementById("allVals").value;
                var subFolderIdUser = document.getElementById("subFolderIdUser").value;
                totalUrl = "userId="+userId+"~subFolderIdUser="+subFolderIdUser+"~userMemId="+userMemId+"~allVals=";
                var allId = allVals.split(",");
                //alert('allVals '+allVals);

                for(var m=0;m<allId.length;m++)
                {
                    var val= document.getElementById(allId[m]); //document.getElementById("parameters2").checked=false
                    if(document.getElementById(val.value).checked==true)
                        totalUrl = totalUrl+","+val.value.replace("&",";");
                                
                    // }
                }
                //alert('totalUrl '+totalUrl);
                $.ajax({
                    url: 'userLayerAction.do?userParam=addUserDimMemberValues&totalUrl='+totalUrl,
                    success: function(data) {
                        // alert('data '+data);
                        if(data=="1")
                            alert("Values Not Saved.")
                        else{
                            alert('Values Saved Successfully.')
                            cancelSelectValsParent();
                        }

                    }
                });
            }
        </script>
    </body>
</html>
