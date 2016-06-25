
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection,com.progen.bugDetails.BugDetailsDAO"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            PbReturnObject buglist = new PbReturnObject();
            Connection con = ProgenConnection.getInstance().getBugConn();
            PbDb pbdb = new PbDb();
            String query = "select * FROM  bug_user_details";
            PbReturnObject list = pbdb.execSelectSQL(query, con);
            String checkedlist = request.getParameter("checkedList");
            //////////////////////.println("=========" + list.getRowCount());
            //////////////////////.println("checkedList=============" + checkedlist);

String contPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=contPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=contPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=contPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=contPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <title>Assign User</title>


        

        <style>
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:white;
                border:1px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
        </style>

    </head>
    <body>
        <%
            String allusers = "";
            String allusersNames = "";
            for (int i = 0; i < list.getRowCount(); i++) {
                allusers += "*u~" + list.getFieldValueInt(i, 1);
                allusersNames += "*" + list.getFieldValueString(i, 2);
            }
            if (list.getRowCount() > 0) {
                allusers = allusers.substring(1);
                allusersNames = allusersNames.substring(1);
            }
        %>
        <form name="assignuser" method="post" action="">

            <table align="center" border="0" width="100%">
                <tr><td align="left">

                        <b>Available UserFolders</b>
                    </td> <td align="left">

                        <b> Assigned UserFolders </b>
                    </td>
                </tr>
            </table>

            <table align="center" border="1" width="100%" style="height:200px" valign="top">
                <tr>
                    <td  class="draggedTable1" valign="top">
                        <div style="height:200Px;width:100%;">
                            <table  border="0">
                                <%

            if (list.getRowCount() > 0) {
                for (int i = 0; i < list.getRowCount(); i++) {

                                %>

                                <tr><td class="myDragTabs" class="ui-state-default3" style="width:200px" id="u~<%=list.getFieldValueString(i, "USER_ID")%>"><%=list.getFieldValueString(i, "TEXT_USER_ID")%></td></tr>
                                <%
                }
            }
                                %>

                            </table >
                        </div>
                    </td>
                    <td id="dropTabs" style="width:50%" class="myhead" valign="top">
                        <div style="height:200Px;width:100%;">
                            <ul id="sortable" >

                            </ul>
                        </div>
                    </td>
                </tr>

            </table>
            <input type="hidden" name="checkedlist" id="checkedlist" value="<%=checkedlist%>">
            
        </form>
        <br><center>

            <input type="button"  class="navtitle-hover" value="Save" onclick="assignUsers()">


        </center>
    <script type="text/javascript">
            var y='';
            var msrArray=new Array();
            var xmlHttp;
            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });
            });

            $(function() {

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });


                $("#dropTabs").droppable(
                {
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',

                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());

                    }

                }
            );

            });


          
            function createColumn(elmntId,elementName){
                var parentUL=document.getElementById("sortable");
                alert(elmntId+'   '+elementName);
                var x=msrArray.toString();
                if(x.match(elmntId)==null){
                    msrArray.push(elmntId)

                    var childLI=document.createElement("li");
                    var uid=elmntId.split("~");
                    childLI.id=uid[1];
                    childLI.style.width='180px';
                    childLI.className='ui-state-default3';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    cell1.style.backgroundColor="#e6e6e6";

                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn('"+uid[1]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.style.backgroundColor="#e6e6e6";
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }else
                {
                    alert('This userFolder is already added')
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }


            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                alert('LiOBJ '+index);
                var parentUL=document.getElementById("sortable");
                alert('parentUL '+index)

                parentUL.removeChild(LiObj);
                index='u~'+index;
                alert(index);
                var l=msrArray.indexOf(index);
                msrArray.splice(l,1);

            }
            function assignUsers(){
                var ullist =document.getElementById("sortable");
                var assignusers=ullist.getElementsByTagName("li");
               // alert("assignusers:"+assignusers.length)
                var tableobj="";
                var trobj="";
                var tdobj="";
                var usernames= new Array;
                for(var i=0;i<assignusers.length;i++){
                    tableobj=assignusers[i].getElementsByTagName("table");
                    tdobj=tableobj[0].getElementsByTagName("td");
                   // alert("tabval"+tdobj[1].innerHTML)
                    usernames[i] =tdobj[1].innerHTML;
                   // alert("usernames::"+usernames[i]);

                }
                var checkedlist = document.getElementById("checkedlist").value;
                var assignedusers =usernames.toString();
                alert("checkedlist"+checkedlist+"assignedusers"+assignedusers);
                $.ajax({
                    url: 'bugDetailsAction.do?param=saveAssignDetails&checkedList='+checkedlist+'&assignedUsers='+assignedusers,
                    success: function(data){
                       // alert("data"+data)
                        if(data=true){
                             parent.$('#assigntouserDialog').dialog('close');
                        }else{

                        }
                       


                    }

                }
            );
            <%--document.forms.assignuser.action='bugDetailsAction.do?param=saveAssignDetails'
              document.forms.assignuser.submit();--%>

            
                        }


        </script>
    </body>
</html>
