<%-- 
   sreekanth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection,utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,com.progen.bugDetails.BugDetailsDAO"%>


<%
        String userId = "";
        userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        BugDetailsDAO bugDao = new BugDetailsDAO();
        PbReturnObject buglist = new PbReturnObject();
        PbDb pbdb = new PbDb();
        Connection con = ProgenConnection.getInstance().getBugConn();
// buglist = bugDao.getDetailsList();
        String query = "select * FROM PRG_COMPANY_DETAILS";
        PbReturnObject list = pbdb.execSelectSQL(query);
        if (userId.equalsIgnoreCase("41")) {

            String selectquery = "  select bm.bug_id,bm.subject, bm.log_date,bm.created_by,bm.updated_by, pv.version_name," +
                    "  bm.updated_date,cm.CUSTOMER_NAME from bug_master bm, customer_master cm,  pi_version pv" +
                    "  where bm.customer_id = cm.customer_id  ORDER BY   bm.log_date desc ";
            buglist = pbdb.execSelectSQL(selectquery, con);

        } else {

            String selectquery = " select bm.bug_id,bm.subject, bm.log_date,bm.created_by,bm.updated_by,pv.version_name," +
                    "  bm.updated_date,cm.CUSTOMER_NAME  from bug_master bm,customer_master cm ,pi_version pv " +
                    "  where bm.customer_id = cm.customer_id and cm.CUSTOMER_NAME ='&' ORDER BY   bm.log_date desc";
            Object[] obj2 = new Object[1];
            obj2[0] = list.getFieldValue(0, "COMPANY_NAME");
            String query1 = pbdb.buildQuery(selectquery, obj2);
            buglist = pbdb.execSelectSQL(query1, con);

        }

        //////////////////////.println("row count " + buglist.getRowCount());
        con.close();
        Connection connection = ProgenConnection.getInstance().getBugConn();
        String queryFORpiid = "select VERSION_NAME from PI_VERSION where NUMERIC_VERSION_ID =(select PI_ID from CUSTOMER_MASTER where CUSTOMER_NAME ='&')";
        Object[] obj2 = new Object[1];
        obj2[0] = list.getFieldValue(0, "COMPANY_NAME");
        String queryFORpiid1 = pbdb.buildQuery(queryFORpiid, obj2);
        PbReturnObject pilist = pbdb.execSelectSQL(queryFORpiid1, connection);
        //////////////////////.println("queryFORpiid1" + queryFORpiid1);
        //////////////////////.println("pilist=====" + pilist.getRowCount());
        connection.close();
        String contPath=request.getContextPath();

%>

<html>
    <head>
        <script src="<%=contPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/dragTable.js"></script>
        <script language="JavaScript" src="<%=contPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/ui.dialog.js"></script>

        <link rel="stylesheet" href="<%=contPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="<%=contPath%>/jQuery/jquery/themes/base/facebook.alert.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/jquery_facebook.alert.js"></script>
        <link href="<%=contPath%>/jQuery/jquery/themes/base/jquery.alerts.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/jquery.alerts.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <title>Bug Details List</title>
        <script type="text/javascript">
            $(document).ready(function(){
                if ($.browser.msie == true){
                    $(".userDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 550,
                        position: 'top',
                        modal: true
                    });
                    $(".bugeditDialog").dialog({
                        autoOpen: false,
                        height: 570,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $(".createuserDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $(".assigntouserDialog").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });

                }
                else{
                    $(".userDialog").dialog({
                        autoOpen: false,
                        height:400,
                        width: 550,
                        position: 'top',
                        modal: true
                    });
                    $(".bugeditDialog").dialog({
                        autoOpen: false,
                        height: 580,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $(".createuserDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $(".assigntouserDialog").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });

                }
            });
        </script>


       


    </head>
    <body>
        <form name="buglist" action="" method="post">
            <table  class="tablesorter" id="tablesorter">
                <thead style="width:auto; height:auto;" >
                    <tr> <th class="header" width="9%"> </th>
                        <th class="header" width="13%">Bug Subject</th>
                        <th class="header" width="13%">LogDate</th>
                        <th class="header" width="13%">Created By</th>
                        <th class="header" width="13%">Updated BY</th>
                        <th class="header" width="13%">Updated On</th>
                        <th class="header" width="13%">Company Name</th>
                        <th class="header">Pi Version Name</th>
                    </tr>
                </thead>
                <tbody>
                    <%  for (int i = 0; i < buglist.getRowCount(); i++) {%>

                    <tr>
                        <td style="width:20px">
                            <input type="checkbox" id="chkusers" name="chkusers" value="<%=buglist.getFieldValueString(i, "BUG_ID")%>">
                        </td>

                        <td valign="top" style="width:100px">
                            <%=buglist.getFieldValueString(i, "SUBJECT")%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=buglist.getFieldValueString(i, "LOG_DATE")%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=buglist.getFieldValueString(i, "CREATED_BY")%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=buglist.getFieldValueString(i, "UPDATED_BY")%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=buglist.getFieldValueString(i, "UPDATED_DATE")%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=buglist.getFieldValueString(i, "CUSTOMER_NAME")%>
                        </td>
                        <input type="hidden" id="compName" value="<%=list.getFieldValueString(i, "COMPANY_NAME")%>">
                        <td valign="top" style="width:100px">
                            <%=buglist.getFieldValueString(i, "VERSION_NAME")%>
                        </td>
                        <input type="hidden" id="versionName" value="<%=pilist.getFieldValueString(i, "VERSION_NAME")%>">
                    </tr>
                    <%}%>
                </tbody>
            </table>
            <table align="center">
                <tr>
                    <Td></Td>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Post Issue " onclick="createIssuelist()" ><Td>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Edit Issue" onclick="editBugList()" ></Td>
                    <Td><Input  class="navtitle-hover" style="width:auto" type="button" id="deletebuglist" value="Delete Issue" onclick="deletebuglist()"></Td>
                    <Td><Input  class="navtitle-hover" style="width:auto" type="button" value="Create User" onclick="createUser()"></Td>
                    <Td><Input  class="navtitle-hover" style="width:auto" type="button" value="Assign to User" onclick="assigntouser()"></Td>

                </tr>

            </table>
            <input type="hidden" id="compName" value="">

            <div id="userDialog" class="userDialog" title="Company Details">
                <iframe id="userframe" name="userframe" frameborder="0" height="100%" width="100%" >
                </iframe>
            </div>
            <%--  style="background-image:url(images/bugimage.jpg) "--%>
            <div id="bugeditDialog" class="bugeditDialog" title="Edit Bug Details" >
                <iframe id="bugeditframe" name="bugeditframe" frameborder="0" height="100%" width="100%" >
                </iframe>
            </div>
            <div id="createuserDialog" class="createuserDialog" title="Create User" >
                <iframe id="createuserframe" name="createuserframe" frameborder="0" height="100%" width="100%" >
                </iframe>
            </div>
            <div id="assigntouserDialog" class="assigntouserDialog" title="Assign To User" >
                <iframe id="assigntouserframe" name="assigntouserframe" frameborder="0" height="100%" width="100%" >
                </iframe>
            </div>

        </form>
 <script type="text/javascript">

            function createIssuelist(){
                //  alert("in create");
                var compaName =document.getElementById("compName").value;
                var versionName =document.getElementById("versionName").value;
                //
                //  alert("company name "+compaName+","+versionName);
                $('#userDialog').dialog('open');
                var frameobj = document.getElementById("userframe");
                frameobj.src ='compDetails.jsp?compName='+compaName+'&versionName='+versionName;
                //document.forms.buglist.action="compDetails.jsp";
                // alert(document.forms.myForm.action)
                //document.forms.buglist.submit();
            }
            function editBugList(){

                //  alert("in Edit");
                var i=0;
                var userId;
                var chkusersobj = document.forms.buglist.chkusers;
                for(var j=0;j<chkusersobj.length;j++){
                    if(chkusersobj[j].checked){
                        userId=chkusersobj[j].value;

                        i++;
                    }
                }
                if(i==0){
                    jAlert("Please Select a Issue")
                    return false;
                }
                else if(i>1){
                    jAlert("Please Select only one Issue")

                    return false;
                }
                else{

                    var bugid =userId;
                    //  alert("bugid is "+bugid);
                    var frameobj = document.getElementById("bugeditframe");
                    frameobj.src ="editBugList.jsp?bugid="+bugid;
                    $('#bugeditDialog').dialog('open');
                }
            }

            $(document).ready( function() {
                $("#deletebuglist").click( function()
                {


                    //  alert("in Delete");
                    var i=0;
                    var userId;
                    var chkusersobj = document.forms.buglist.chkusers;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userId=chkusersobj[j].value;

                            i++;
                        }
                    }
                    if(i==0){
                        jAlert("Please Select a Issue");

                        return false;
                    }
                    else if(i>1){
                        jAlert("Please Select only one Issue");
                        return false;
                    }
                    else{
                        jConfirm('Are you sure you want ot delete ?', 'Confirmation Dialog',

                        function(r) {


                            if(r==true)
                            {
                                var bugid =userId;
                                // alert("bugid is "+bugid);

                                $.ajax({
                                    url: 'bugDetailsAction.do?param=deleteBugdetails&bugid='+bugid,
                                    success: function(data){
                                        window.location.reload(true);


                                    }

                                }
                            );


                            }

                            window.location.reload(true);
                        });
                    }
                });
            });
            function createUser(){


                var frameobj =document.getElementById("createuserframe");
                frameobj.src="createUser.jsp";
                $('#createuserDialog').dialog('open');
            }

            function assigntouser(){
                var i=0;
                var bugIds;
                var bugids = new Array;

                var chkusersobj = document.forms.buglist.chkusers;
                for(var j=0;j<chkusersobj.length;j++){

                    if(chkusersobj[j].checked){
                        bugId=chkusersobj[j].value;
                        bugids[i]=bugId;
                        i++;
                    }
                }
                if(i==0){
                    jAlert("Please Select a Issue")
                    return false;
                }

                else{
                    //  alert("b=="+bugids[0])
                    var checkedlist = bugids.toString();
                    var frameobj = document.getElementById("assigntouserframe");
                    frameobj.src ="assignuser.jsp?checkedList="+checkedlist;
                    $('#assigntouserDialog').dialog('open');
                }

            }

        </script>
    </body>
</html>
