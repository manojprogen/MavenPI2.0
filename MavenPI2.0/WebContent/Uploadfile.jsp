<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%--
    Document   : Uploadfile
    Created on : Jan 28, 2010, 9:30:19 PM
    Author     : Saurabh
--%>
<!--//edited by mohit for xslx loading, ETL part is only for data flow-->
<%
    String qry = "";
    qry = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;

    list = pbdb.execSelectSQL(qry);
    int vals = 0;
    vals = list.getRowCount();
    String contextPath=request.getContextPath();

%>

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%//=themeColor%>/metadataButton.css" rel="stylesheet" />-->
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%//=themeColor%>/TableCss.css" rel="stylesheet" />-->
        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/stlesheets/treeviewstyle/screen.css" />
        <link tyandard.css" rel="stylesheet" type="text/css" />
         <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=contextPath%>//dragAndDropTable.js"></script>
        <title>Upload File</title>

        <html:base/>
       
        <style type="text/css">
            .migrate{
                font-family: inherit;
                font-size: 10pt;
                color: #000;
                padding-left:12px;
                background-color:#8BC34A;
                border:0px;
            }
        </style>
    </head>
    <body>
         <script type="text/javascript">

      $(document).ready(function(){

       $('#sddatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });
                    $('#eddatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });
      });



            function Checkfiles()
            {
                var fup = document.getElementById('filename');

                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
               if($("#connid").val()=="--SELECT--")
                   {
                        alert("Please Select Connection");
                         return false;

                   }
                 else if($("#bbg").val()=="--SELECT--")
                   {
                         alert("Please Select Table");
                         return false;
                   }

                else if( fileName!=null && ext == "xlsx" )
                {
                    $("#loadingmetadata").show();
                    return true;
                }
                else
                {
                    alert("Upload .xlsx files only");
                    // fup.focus();
                    // document.getElementById("checkDiv").style.display ='block'

                    return false;
                }

            }
             function getbbg(id){

            var id=$("#"+id).val()
//            alert(id)
        if(id=="--SELECT--")
       {
        alert("Please Select Connection")
       }
       else
           {

             $.get("<%= request.getContextPath()%>/createtableAction.do?param=getAllTables&conid="+id,function(data){
//                           alert('Record Deleted Successfully............')
//                           alert("data"+data)
                        $("#bbg").html(data)
                        });
            }
            }

             function ChangeDiv(){
//              alert("data")

              if(document.getElementById("Uploadfile") != null && document.getElementById("Uploadfile").checked){
                   $("#EtlDiv").hide();
//                   $("#UploadForm").trigger('reset')
                $("#uploadexcel").show();
//           $("#overWriteReport").height(500);
        } else if(document.getElementById("calletl") != null && document.getElementById("calletl").checked){
                $("#uploadexcel").hide();

                $("#EtlDiv").show();
//                $("#SearchResult").hide();

        }

           }
            function SubmitEtl(){

       var etl=$("#Etlname").val()
       var sd=$("#sddatepicker").val()
       var ed=$("#eddatepicker").val()
//            alert(etl)
//             alert(sd)
//              alert(ed)
        if(etl=="--SELECT--")
       {
        alert("Please Choose ETL Name")
        return false;
       }if(etl=="Finance_DATA_proc" ||  etl=="KPI_HR_Proc" )
       {
           if(sd=="")
               {
                   alert("Please Choose Start date")
                   return false;

               }
               else if(ed=="")
               {
                   alert("Please Choose End date")
                   return false;

               }

       }


              $("#loadingmetadata").show();
               $.ajax({
                    async:false,
                    data:{'etl':etl,'sd':sd,'ed':ed},
                    url:'createtableAction.do?param=CallEtls',
                    success:function(data) {
                      if(data=="success")
                          {
                              $("#loadingmetadata").hide();
                              alert("ETL Called Successfully")
                          }
                          else
                              {
                                  alert("ETL Failed")
                              }
                        }
                      });

           }

           function showDate(id){
// alert("jj")
 if($("#"+id).val()=="Finance_DATA_proc" || $("#"+id).val()=="KPI_HR_Proc")
     {
   $("#sddatepicker").val(null);
   $("#eddatepicker").val(null);
//    document.getElementById("edate").value="";
    document.getElementById("sdate").style.display = "";
     document.getElementById("edate").style.display = "";
     }
     else
     {
               $("#sddatepicker").val(null);
   $("#eddatepicker").val(null);
         document.getElementById("sdate").style.display ="none";
     document.getElementById("edate").style.display = "none";}

}




        </script>
 <table align="center" style=" width:40% ">
             <tr><td>
<!--                <form id="SaveSettings" name=SaveSettings" id="" method="post" action="">-->
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0" style=" width: 100%">
                       <tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>

                       <tr><td ><center><input type="radio" name="add" style="width:auto;color:black" checked id="Uploadfile" onclick="ChangeDiv()"/>
                                  Upload File &nbsp;&nbsp;</center>
                       </td><td ><center><input type="radio" name="add" style="width:auto;color:black" id="calletl" onclick="ChangeDiv()"/>
                                  Call ETL &nbsp;&nbsp;&nbsp;&nbsp;</center>
                       </td> <td colspan="1">&nbsp;</td><td colspan="1">&nbsp;</td>
                       </tr>
<!--<tr>
                           <td colspan="1">&nbsp;</td>
                       </tr><tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>-->


                   </table>
<!--                </form>-->
                 </td></tr>
        </table>

<!--         <table  align="center">
             <h1 align="center" style="color:blue">Upload Excel Sheet Data </h1>
        </table>-->
        <html:form  action="createtableAction.do?param=uploadFile" method="post" enctype="multipart/form-data">

            <table id="uploadexcel" width="50%" align="center" class="migrate">
                  <tr style="width:100%">
                    <td align="left" colspan="2"><h1 align="center" style="color:blue">Upload Excel Sheet Data </h1>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="2"><font color="red"><html:errors/></font>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                 <tr>
                    <td align="left" colspan="1" class="migrate">Connection Name</td>
                    <td align="left">
                        <select name="connid" id="connid" onchange="getbbg(this.id)" ><option> --SELECT-- </option>
                            <% for (int i = 0; i < list.getRowCount(); i++) {
                            %>
                            <option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr><td align="left" class="migrate">Table: &nbsp;&nbsp;&nbsp;&nbsp;</td>
                           <td align="left"><select name="bbg" id="bbg" style="width:150px;" onchange=""><option> --SELECT-- </option>
                             </select></td>
                       </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>


                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate">File Name: <label style="color:Red">(.xlsx files only)</label></td>
                    <td align="left" colspan="1" class="migrate"><html:file property="filename" styleId="filename" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="right" colspan="1" class="migrate"><input type="radio" name="id" id="truncate" value="truncate"> Truncate data<BR></td>
                    <td align="left" colspan="1" class="migrate"><input type="radio" name="id" id="append" value="append" checked="checked"> Append data<BR></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="center" colspan="2" class="migrate"><html:submit onclick ="return Checkfiles()" style="background-color:silver; color:darkblue;" >Upload File</html:submit>
                    </td>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table>

        </html:form>
        <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/Dataloading.gif'  border='0px' height="150px" weight="150px" style='position:absolute; left: 550px; top: 300px;'/>
</div>

<!--Added by mohit only for Data flow -->
       <div id="EtlDiv" style="display:none;width:30%">
             <table width="80%" align="left" class="migrate">
                        <tr>
                    <td align="left" <tr style="width:100%">
                    <td align="left" colspan="2"><font color="red"><html:errors/></font>
                </tr>
                 <tr>
                    <td align="center" colspan="1" class="migrate">Select ETL:</td>
                    <td align="left">
                        <select name="Etlname" id="Etlname" style="width:150px;" onchange="showDate(this.id)"><option> --SELECT-- </option>
                            <option value="CleanUp_Data_proc">CleanUp_Data_proc</option>
                            <option value="Finance_DATA_proc">Finance_DATA_proc</option>
                            <option value="KPI_HR_Proc">KPI_HR_Proc</option>
                        </select>
                    </td>
                </tr>
                 <tr id="sdate" style="display:none"><td align="center" class="migrate"> &nbsp;&nbsp;Start Date:&nbsp;&nbsp;</td><td><input id="sddatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="" readonly="" onclick=""></td>
                  </tr>
                       <tr id="edate" style="display:none"><td align="center" class="migrate">&nbsp;End Date:&nbsp;&nbsp;</td><td><input id="eddatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="" readonly="" onclick=""></td>
                  </tr>
                <tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>
<!--                       <tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>-->
                 <tr><td colspan="2"><center><input type="button" style="width:auto;color:black" value="Call ETL" id="" onclick="SubmitEtl()"/></center></td></tr>
             </table>
        </div>

    </body>
</html:html>
