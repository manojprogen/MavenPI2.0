<!doctype html>

<%@page import="prg.db.PbDb,prg.db.PbReturnObject" %>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>
<html lang="en">
     <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/dragTable.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
          <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
    <head>

	

</head>
<%String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        PbReturnObject pbro = new PbDb().execSelectSQL(Query);
        String connIds=String.valueOf(session.getAttribute("connId"));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connId in  grp-->"+connIds);
//"checkConnId('connIds
%>
<body onload="alert('Here')">

	<div >
        <div>
         <iframe  id="businessgrptab" NAME='businessgrptab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
        </div>
    </div>

           <div id="selectConnection" title="Select Connection">
            <form name="myForm">
                <table cellpadding="5">
                    <tr><td>
                        <label for="name">Connection Name</label></td>
                     <td> <select id="connId" name="connId" style="width:146px">
                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                        <option value="<%=pbro.getFieldValueInt(i, 0)%>"><%=pbro.getFieldValueString(i, 1)%></option>
                        <%}%>
                    </select></td></tr>

                </table>
                <%--<input type="button"  value="Connect" onclick="saveTables()">--%>
            </form>

        </div>

<script type="text/javascript">



                function checkConnId(conId){
          //alert('hi')
          // alert("gfffng"+conId);
           if(conId==null){
              // alert('if');
                $('#selectConnection').dialog('open');
           }else{
               //alert('else');
               var frameObj = document.getElementById('businessgrptab');
            var source="getAllBusinessGroups.do?method=&connId="+conId;
            frameObj.src=source;
            //alert(source+"if connis null")
            document.getElementById('businessgrptab').style.display='';
           }
       }

            function saveTables()
       {
             $('#selectConnection').dialog('close');
            var connId=document.getElementById("connId").value;

             var frameObj = document.getElementById('businessgrptab');
            var source="getAllBusinessGroups.do?method=dynamicdims&connid="+connId;
            //alert(source+"if conn is null next to same")
            frameObj.src=source;
            document.getElementById('businessgrptab').style.display='';

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
                            saveTables();
                              $(this).dialog('close');
                        }
                    },
                    close: function() {

                    }
                });
	</script>

</body>
</html>

