<%-- 
    Document   : modifyDimensions
    Created on : 29 May, 2012, 6:43:47 PM
    Author     : arun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
            #ui-datepicker-div
            {
                z-index: 9999999;
            }
        </style>
       <script type="text/javascript">
            $(document).ready(function(){
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyDimensions",function(data){
                    $("#connnames").html(data)
                     $("#modifyDimensionid").dialog('open')
                 });
                 if ($.browser.msie == true){
                    $("#modifyDimensionid").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                 }
                 else
                     {
                        $("#modifyDimensionid").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                     }
            });
       </script>
         <script type="text/javascript">
          function cancelBuckets()
          {
           $("#modifyDimensionid").dialog('close')
          }
          function getDimensions()
          {
            $("#modifyDimensionid").dialog('close')
            var conid=$("#connnames").val()
            $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllDimensions&conid="+conid, $("#modifyDimensionForm").serialize(), function(data){
               var jsonVar=eval('('+data+')')
               var dimid=jsonVar.dimensionidlist
               var dimnames=jsonVar.dimensionnamelist
               var htmlVar=""
               for(var i=0;i<dimnames.length;i++)
                   {
                       htmlVar+="<tr><td><input type='checkbox'/></td>\n\
                                     <td><input type='checkbox'/></td>\n\
                                      <td><input type='text' value='"+dimnames[i]+"' readonly/></td>\n\
                                      <td><input type='text' value='"+dimnames[i]+"'/></td>  </tr>"
                        $("#measureDetTabBody").html(htmlVar)
                   }
            });
            
          }
         </script>
    </head>
    <body>
          <div id="modifyDimensionid" title="Select Connection" style="width: 1400px; height: 443px; overflow: auto; z-index: 1001;">
              <form id="modifyDimensionForm" name="modifyDimensionForm" method="post" action="">
          <table align="center">
                 <tr>
                     <td ><b> Connection Name </b>&nbsp;&nbsp;&nbsp; </td>
                     <td><select id="connnames" name="connnames" style="width:auto">
                          <option>---SELECT---</option>
                          </select></td>
                 </tr>
          </table><br><br><br><br><br><br><br><br>
           <table align="right">
                      <tr>
                          <td>
                              <input type="button" class="migrate" style="width:auto;color:black" value="Save" id="btnn" onclick="getDimensions()"/>&nbsp;&nbsp;<input type="button"  class="migrate" style="width:auto" value="Cancel" onclick="cancelBuckets()"/>
                          </td>
                      </tr>
                  </table>
                  </form>
                </div>
           <table id="measureDetTab" class="tablesorter"  border="1px solid" cellpadding="1" cellspacing="1">
                    <thead>
                        <tr>
                            
                            <th class="migrate">Change <br/>in group</th>
                            <th class="migrate" >Change <br/>in report</th>
                            <th class="migrate">Dimensions names<br/>(OLD)</th>
                            <th class="migrate" >Dimensions names <br/>(Modified)</th>
                        </tr>
                    </thead>
                    <tbody id="measureDetTabBody">
                    </tbody>
                    <div id="popupwindow"></div>
                </table>
        <center><input type="submit" class="migrate" style="width:auto;color:black" value="Save" id="btnn"/>&nbsp;&nbsp;<input type="button"  class="migrate" style="width:auto" value="Cancel" /></center>

           
        
    </body>
</html>
