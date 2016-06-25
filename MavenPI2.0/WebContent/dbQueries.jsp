<%--
    Document   : newjsp1
    Created on : 16 Mar, 2012, 11:04:53 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>


         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

         %>
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
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


        <script type="text/javascript">
             $(document).ready(function(){
                  if ($.browser.msie == true){
                      $("#accpacDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
                  else {
                    $("#accpacDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
                  if ($.browser.msie == true){
                      $("#ctServerDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
                  else {
                    $("#ctServerDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
                  if ($.browser.msie == true){
                      $("#comInitDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
                  else {
                    $("#comInitDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
                  if ($.browser.msie == true){
                      $("#comIncrDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
                  else {
                    $("#comIncrDivId").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'center',
                        modal: true
                    });
                  }
               });

        </script>

        <script type="text/javascript">

        function comIncrCancel(){
          $("#comIncrDivId").dialog('close');
        }
       function comIncrDialogOpen(){
            $("#comIncrDivId").dialog('open');
        }
        function comInitCancel(){
          $("#comInitDivId").dialog('close');
        }
       function comInitDialogOpen(){
            $("#comInitDivId").dialog('open');
        }
        function CancelDiv(){
           $("#accpacDivId").dialog('close');
        }
        function accDialogOpen(){
            $("#accpacDivId").dialog('open');
        }

        function ctServerCancel(){
           $("#ctServerDivId").dialog('close');
        }
        function ctServerDialogOpen(){
             $("#ctServerDivId").dialog('open');
        }

        function qtInit(id){
                 alert('id---------'+id)
                 $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=executeQuery&ids='+id, $("#columnForm").serialize(),
                 function(data) {
                 });
          }



          function dfLoad(id){
                 alert('id---------'+id)
                 $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=executeQuery&ids='+id, $("#columnForm").serialize(),
                 function(data) {
                    alert('Hello'+data);
                 });
          }

            function qtIncr(id){
                alert('id---------'+id)
                $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=executeQuery&ids='+id, $("#columnForm").serialize(),
                function(data){
                });
            }

            function ctInit(id){
                 var selection = document.myform2.optTrunc;
                 if (selection[0].checked == true)
                    var option='true';
                  else
                    option='false';

                 var connName = $("#connName").val()
                 var userName = $("#userName").val()
                 var password = $("#password").val()

//                 if(connName==''||userName==''||password=='')
//                     alert("enter all Values")
//                 else {
                        alert('id---------'+id)
                        $("#ctServerDivId").dialog('close');
//                        alert('connName-->'+connName+'   userName-->'+userName+'   password-->'+password)
                        $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=executeQuery&ids='+id+'&connName='+connName+'&userName='+userName+'&password='+password+'&option='+option,$("#columnForm").serialize(),
                        function(data)
                        {
                        });
//                 }
            }
            function accInit(id){
                  var selection = document.myform1.optTrunc;
                  if (selection[0].checked == true)
                     var option='true';
                  else
                      option='false';

                alert('id---------'+id)
                $("#accpacDivId").dialog('close');
                $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=executeQuery&ids='+id+'&option='+option, $("#columnForm").serialize(),
                function(data){
                });
             }
             function comInit(id){
                  var selection = document.myform3.optTruncAcc;
                  if (selection[0].checked == true)
                     var optionAcc='true';
                  else
                      optionAcc='false';
                  selection = document.myform3.optTruncCt;
                  if (selection[0].checked == true)
                     var optionCt='true';
                  else
                      optionCt='false';

                 alert('id---------'+id)
                 $("#comInitDivId").dialog('close');
                 $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=executeQuery&ids='+id+'&optionCt='+optionCt+'&optionAcc='+optionAcc, $("#columnForm").serialize(),
                 function(data){
                 });
            }
            function comIncr(id){
                var selection = document.myform4.optTruncAcc;
                  if (selection[0].checked == true)
                     var optionAcc='true';
                  else
                      optionAcc='false';
                  selection = document.myform4.optTruncCt;
                  if (selection[0].checked == true)
                     var optionCt='true';
                  else
                      optionCt='false';

                 alert('id---------'+id)
                 $("#comIncrDivId").dialog('close');
                 $.post('<%=request.getContextPath()%>/studioAction.do?studioParam=executeQuery&ids='+id+'&optionCt='+optionCt+'&optionAcc='+optionAcc, $("#columnForm").serialize(),
                 function(data){
                 });
            }
            function comRequest(id){
                alert('id---------'+id)
                alert('Inside Request')
            }

        </script>
    </head>
    <body>

        <table border="1" align="center">
            <h3 align="center">Initial And Incremental Load</h3>
        </table>

        <form action="" method="post" id="columnForm">
            <br><br><br><br>
            <table  border = "2" align="center">

            <tr>
                <td align="left" class="migrate">
                    <label>Quick Travel init load </label>
                </td>
                <td align="right">
                <input type="button" class="" value="Run" id="QuickInit" onclick="qtInit(this.id)"/>
                </td>

            </tr>
                  <tr>
                <td align="left" class="migrate">
                    <label>Quick Travel incremental load </label>
                </td>
                <td align="right">
                     <input type="button" class="" value="Run" id="QuickIncr" onclick="qtIncr(this.id)"/>
                     </td>
                  </tr>
            <tr>
                <td align="left" class="migrate">
                     <label>CTServer init load </label>
                </td>
                <td align="right" class="">
                     <input type="button" class="" value="Run" id="CtInit" onclick="ctServerDialogOpen()"/>
                </td>
            </tr>
              <tr>
                  <td align="left" class="migrate">
                     <label>Accpac init load </label>
                  </td>
                  <td align="right">
                     <input type="button" class="" value="Run" id="AccInit" onclick="accDialogOpen()"/>
                     </td>
                </tr>
              <tr>
                <td align="left" class="migrate">
                    <label>Combined init load </label>
                </td>
                <td align="right">
                     <input type="button" class="" value="Run" id="ComInit" onclick="comInitDialogOpen()"/>
                </td>
                </tr>
              <tr>
                  <td align="left" class="migrate">
                     <label>Combined incremental load</label>
                  </td>
                  <td align ="right">
                     <input type="button" class="" value="Run" id="ComIncr" onclick="comIncrDialogOpen()"/>
                    </td>
            </tr>
            <tr>
                  <td align="left" class="migrate">
                     <label>Data Flow Load </label>
                  </td>
                  <td align="right">
                     <input type="button" class="" value="Run" id="DFLoad" onclick="dfLoad(this.id)"/>
                     </td>
                </tr>
       </table>
       </form>
       <br><br>

       <div id="comInitDivId" style="display:none;border-width:thick" title="Combined INIT Connection">
            <form name="myform3">
            <table align="center">
                <br><br>
                <tr>
                    <td>&nbsp;</td>
                    <td>Truncate Accpac Tables :</td>
                    <td>&nbsp;<input type="radio" name="optTruncAcc" id="optTrue" value="true" checked/>&nbsp;&nbsp;Yes&nbsp;&nbsp;</td>
                    <td>&nbsp;<input type="radio" name="optTruncAcc" id="optFalse" value="false"/>&nbsp;&nbsp;No</td>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>Truncate CtServer Tables :</td>
                    <td>&nbsp;<input type="radio" name="optTruncCt" id="optTrue" value="true" checked/>&nbsp;&nbsp;Yes&nbsp;&nbsp;</td>
                    <td>&nbsp;<input type="radio" name="optTruncCt" id="optFalse" value="false"/>&nbsp;&nbsp;No</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr align="right">
                    <td>&nbsp;</td>
                    <td><input type="button" value="Ok" id="ComInit" onclick="comInit(this.id)"/>
                    <td>&nbsp;</td>
                    <td><input type="button" value="Cancel" id="comCancel" onclick="comInitCancel()"/>
                </tr>
            </table>
            </form>
        </div>
       <div id="comIncrDivId" style="display:none;border-width:thick" title="Combined INCR Connection">
            <form name="myform4">
            <table align="center">
                <br><br>
                <tr>
                    <td>&nbsp;</td>
                    <td>Truncate Accpac Tables :</td>
                    <td>&nbsp;<input type="radio" name="optTruncAcc" id="optTrue" value="true" checked/>&nbsp;&nbsp;Yes&nbsp;&nbsp;</td>
                    <td>&nbsp;<input type="radio" name="optTruncAcc" id="optFalse" value="false"/>&nbsp;&nbsp;No</td>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>Truncate CtServer Tables :</td>
                    <td>&nbsp;<input type="radio" name="optTruncCt" id="optTrue" value="true" checked/>&nbsp;&nbsp;Yes&nbsp;&nbsp;</td>
                    <td>&nbsp;<input type="radio" name="optTruncCt" id="optFalse" value="false"/>&nbsp;&nbsp;No</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr align="right">
                    <td>&nbsp;</td>
                    <td><input type="button" value="Ok" id="ComIncr" onclick="comIncr(this.id)"/>
                    <td>&nbsp;</td>
                    <td><input type="button" value="Cancel" id="comCancel" onclick="comIncrCancel()"/>
                </tr>
            </table>
            </form>
        </div>
        <div id="accpacDivId" style="display:none;border-width:thick" title="Accpac Connection">
            <form name="myform1">
            <table align="center">
                <br><br>
                <tr>
                    <td>&nbsp;</td>
                    <td>Truncate Tables :</td>
                    <td>&nbsp;<input type="radio" name="optTrunc" id="optTrue" value="true" checked/>&nbsp;&nbsp;Yes&nbsp;&nbsp;</td>
                    <td>&nbsp;<input type="radio" name="optTrunc" id="optFalse" value="false"/>&nbsp;&nbsp;No</td>
                </tr>

                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr align="right">
                    <td>&nbsp;</td>
                    <td><input type="button" value="Ok" id="AccInit" onclick="accInit(this.id)"/>
                    <td>&nbsp;</td>
                    <td><input type="button" value="Cancel" id="accCancel" onclick="CancelDiv()"/>
                </tr>
            </table>
            </form>
        </div>
         <div id="ctServerDivId" style="display:none;border-width:thick" title="CT Server Connection">
             <form name="myform2">
            <table align="center">
                <br><br>
                <tr>
                    <td>&nbsp;</td>
                    <td>Connection Name :</td>
                    <td>&nbsp;<input type="text" name="connName" id="connName"/></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>User Name :</td>
                    <td>&nbsp;<input type="text" name="userName" id="userName"/></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>Password :</td>
                    <td>&nbsp;<input type="text" name="password" id="password"/></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>Truncate Tables :</td>
                    <td>&nbsp;<input type="radio" name="optTrunc" id="optTrue" value="true" checked/>&nbsp;&nbsp;Yes&nbsp;&nbsp;
                    <input type="radio" name="optTrunc" id="optFalse" value="false"/>&nbsp;&nbsp;No</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr align="right">
                    <td>&nbsp;</td>
                    <td><input type="button" value="Ok" id="CtInit" onclick="ctInit(this.id)"/>
                    <td><input type="button" value="Cancel" id="CtCancel" onclick="ctServerCancel()"/>
                    <td>&nbsp;</td>
                </tr>
            </table>
          </form>
        </div>


        <form method="post" action="viewRequest.jsp">
           <table align="center">
            <tr><td><input type="submit" value="View Request"></td></tr>
        </table>
       </form>
     </body>
</html>