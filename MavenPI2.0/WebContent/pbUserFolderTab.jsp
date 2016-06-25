<!doctype html>
<%@page import="prg.db.PbDb,prg.db.PbReturnObject" %>
<% String contexPath=request.getContextPath();%>
<html lang="en">
     <script src="<%=contexPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/tablesorter/dragTable.js"></script>
        <script language="JavaScript" src="<%=contexPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
           <script type="text/javascript" src="<%=contexPath%>/javascript/ui.dialog.js"></script>

        <link rel="stylesheet" href="<%=contexPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
          <link type="text/css" href="<%=contexPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contexPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contexPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
    <head>
             <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
            <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css"/>
            <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
             <link type="text/css" href="<%=contexPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ReportCss.css" rel="stylesheet" />

	

</head>
<%String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        PbReturnObject pbro = new PbDb().execSelectSQL(Query);
        String connIds=String.valueOf(session.getAttribute("connId"));
     
%>
<body onload="alert('Here')">

	 <div>
            <div>
                 <iframe  id="userfoldertab" NAME='userfoldertab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>
        </div>
           <div id="selectConnection" title="Select Connection">
            <form name="myForm">
                <table cellpadding="5">
                    <tr><td>
                        <label for="name">Connection Name</label></td>
                     <td>
                         <select id="connId" name="connId" style="width:146px" onchange="saveTables()">
                        <option value="0000">---Select---</option>
                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                        <option value="<%=pbro.getFieldValueInt(i, 0)%>"><%=pbro.getFieldValueString(i, 1)%></option>
                        <%}%>
                         </select>
                     </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="name">Role</label>
                        </td>
                        <td>
                            <select name="foldersSelect" id="foldersSelect" >

                             </select>
                        </td>
                    </tr>

                </table>
                <%--<input type="button"  value="Connect" onclick="saveTables()">--%>
            </form>

        </div>

    <script type="text/javascript">



         function checkConnId(conId){
           if(conId==null){
                $('#selectConnection').dialog('open');
           }else{
               var frameObj = document.getElementById('businessgrptab');
            var source="getAllBusinessGroups.do?connId="+conId;
            frameObj.src=source;
            document.getElementById('businessgrptab').style.display='';
           }
       }

            function saveTables()
       {
             //$('#selectConnection').dialog('close');
            // $('#selectRole').dialog('open');
            var connId=document.getElementById("connId").value;
            $.ajax({
                    url: 'userLayerAction.do?userParam=getUserFolderList1&connId='+connId,
                    success: function(data) {
                        $("#foldersSelect").html(data)
                    }
                });
             
         

        }
             function saveTables1()
       {
             $('#selectConnection').dialog('close');
            var connId=document.getElementById("connId").value;
            var folderId=document.getElementById("foldersSelect").value;
             var frameObj = document.getElementById('userfoldertab');
             var source;
             if(folderId=="0000")
                 {
                     source="userLayerAction.do?userParam=getUserFolderList&connId="+connId;
                 }
                 else
                     {
             source="userLayerAction.do?userParam=getUserFolderList2&connId="+connId+"&folderId="+folderId;
                     }
            frameObj.src=source;
          //  alert(source);
            document.getElementById('userfoldertab').style.display='';
           // var frameObj = document.getElementById('businessgrptab');

        }



         $("#selectConnection").dialog({
                    bgiframe: true,
                    autoOpen: true,
                    height:250,
                    width:350,
                    modal: true,
                    buttons: {
                        Cancel: function() {
                          // var x=confirm('To See Dimensions Please Select Connection Are You Sure To Cancel')
                          // if(x==true){
                            $(this).dialog('close');
                          // }
                        },
                        'Select': function() {
                            var connId=document.getElementById("connId").value;
                             if (connId=="0000"){
                                alert('Please select a Connection')
                            }
                            else {
                            saveTables1();
                              $(this).dialog('close');
                        }
                        }
                    },
                    close: function() {

                    }
                });
	</script>        

</body>
</html>

