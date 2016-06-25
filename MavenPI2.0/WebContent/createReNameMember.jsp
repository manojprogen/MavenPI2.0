
<%@page import="prg.db.PbDb,java.sql.*,prg.db.PbReturnObject" contentType="text/html" pageEncoding="UTF-8"%>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>

<html>
    <head>


        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <style>

                        .black_overlay{
                            display: none;
                            position: absolute;
                            top: 0%;
                            left: 0%;
                            width: 200%;
                            height: 200%;
                            background-color: black;
                            z-index:1001;
                            -moz-opacity: 0.5;
                            opacity:.50;
                            filter:alpha(opacity=50);
                            overflow:auto;
                        }
        </style>
    </head>
    <body>

        <%
        try {
            PbDb pbdb = new PbDb();
            String connectionId=request.getParameter("connectionId");
            String memId=request.getParameter("memId");
            String memName1=request.getParameter("memName");
            PbReturnObject pbro=pbdb.execSelectSQL("SELECT MEMBER_NAME, MEMBER_DESC FROM PRG_QRY_DIM_MEMBER where member_id="+memId) ;
            String memDesc=pbro.getFieldValueString(0,0);
            String memName=pbro.getFieldValueString(0,1);
         %>
        <form id="f1" name="myForm">

            <table align="center"  border="0"><br>
                <br><tr>
                <th colspan="2">Rename Member</th></tr>
              
              <%--  <tr>
                  <td width="120px">
                      Member Name
                    </td>
                    <td><input type="text" readonly name="oldName" id="oldName"  class="myTextbox5"  style="width:120Px" value="<%=memName%>"></td>
                    
                </tr>--%>
                <tr></tr><tr></tr><tr></tr>
                <tr>
                    <td width="100px">
                      Member Name
                    </td>
                    <td><input type="text" name="newName" id="newName" class="myTextbox5"  style="width:120Px" maxlength="31" value="<%=memName%>" onchange="checkExistmemName('<%=connectionId%>','<%=memName%>')"> </td>

                </tr>
                 <tr>
                    <td width="100px" >
                      Member Desc
                    </td>
                    <td><input type="text" name="desc" id="desc" class="myTextbox5"  style="width:120Px" maxlength="255" value="<%=memDesc%>"> </td>

                </tr>
            </table>


            <table align="center"  border="0">
                <tr>
                    
                    <td><input type="button" value="Save" onclick="saveReNameMember('<%=memId%>')"><input type="button" value="Cancel" onclick="cancelRenameMember()"></td>
                </tr>
            </table>
           <div id="fade1" class="black_overlay" >

            <img id="imgId" src="images/ajax.gif"  width="100px" height="100px"  style="position:absolute;left:600px;top:200px" >

        </div>
            <input type="hidden" name="eleList" id="eleList" value="">
        </form>
 <script type="text/javascript">
           function cancelRenameMember(){
               parent.cancelRenameMember();

           }
          function checkExistmemName(connectionId,oldmemName){
              var newMemName=document.getElementById("newName").value;
            $.ajax({
                    url: 'dimensioncheck.do?dimensionParam=checkExistmemName&connectionId='+connectionId+'&newMemName='+newMemName+'&oldmemName='+oldmemName,
                    success: function(data){
                      if(data!=""){
                              alert('This member name Already Exist \n Please give another name');
                      }
                    }
                });
          }

          function saveReNameMember(memId){
             //  document.getElementById('fade1').style.display='block';
               var newMemName=document.getElementById("newName").value;
               var desc=document.getElementById("desc").value;
               $.ajax({
                    url: 'dimensioncheck.do?dimensionParam=saveMemRename&memDesc='+desc+'&newMemName='+newMemName+"&memId="+memId,
                    success: function(data){
                      //  document.getElementById('fade1').style.display='';
                      if(data!="0"){

                              parent.saveReNameMember();
                      }else{
                          alert('Error occured while Renaming Member');
                      }
                    }
                });
          }

        </script>

        <%} catch (Exception ex) {
            ex.printStackTrace();
        }%>
    </body>
</html>
