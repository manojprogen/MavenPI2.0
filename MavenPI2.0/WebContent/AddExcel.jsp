<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection "%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%--
    Document   : AddExcel
    Created on : Mar 31, 2015, 9:30:19 PM
    Author     : Mohit
--%>

<%
    String qry = "";
    qry = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;

    list = pbdb.execSelectSQL(qry);
    int vals = 0;
    vals = list.getRowCount();
//added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
            String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
<!--        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%//=themeColor%>/metadataButton.css" rel="stylesheet" />-->
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%//=themeColor%>/TableCss.css" rel="stylesheet" />-->
        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/stlesheets/treeviewstyle/screen.css" />
        <link tyandard.css" rel="stylesheet" type="text/css" />
         <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
        <title>Upload File</title>

        <html:base/>

        <style type="text/css">
            *{font:10px verdana;}
        </style>
    </head>
    <body>

<!--        <div align="right" style=" width:20% ">
            <table align="center" style=" width:40% ">
             <tr><td colspan="4" title="Click Here to Load New Xslx file" style="background-color: #B4D9EE; align:center; width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><input id="Ar" class="navtitle-hover"  value="Load Xslx" type="button" onclick="LoadXslx()"></td></tr>
            </table>
        </div>-->
<!--<div id="SearchResult" align="center" style="display:block">
    <br>
    <br>
     <form name="UserInfoForm"  method="post" action="">
                <div style="overflow: hidden; height: 300px ">
                    <div class="hover" onmouseover="hover" style="  overflow: auto; height: 380px">
                    <table  style="width:95%;border-collapse: collapse;  " id="SearchResultTable">
                <thead style=" background-color: #DDD ; text-align: center; ">
                    <tr><td>To</td><td><input id="tdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="fdate" readonly=""></td>
                  </tr>
                    <tr>
                        <td colspan="10" style="font-size:larger; background-color: #B4D9EE; width: 100%;  padding: 0.6em; border: 1px solid #CCC; ">User's Request Information </td>
                    <td align="left" style="font-size:larger; background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; "><label>Status:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="right" style="font-size:larger; background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC;" ><select name="bbn" id="Status" style="width:80px;" onchange="GetUserTable(this.id)">
<option value="All">All</option>
<option value="Pending">Pending</option>
<option value="Activated">Activated</option>
<option value="Deactivated">Deactivated</option>
<option value="Rejected">Rejected</option>


                             </select>
                             </td></tr>

                    <tr>
                        <td style="background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; ">&nbsp;</td>
                        <td  style="background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="S_no">S.No.</td>
                        <td  style="background-color: #B4D9EE; width: 4%;  padding: 0.6em; border: 1px solid #CCC; " id="Fn">Full Name</td>
                        <td  style="background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Email">Email</td>
                        <td  style="background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Mob">Mobile</td>
                        <td  style="background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Nationality">Nationality</td>
                        <td  style="background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Country">Country</td>
                        <td  style="background-color: #B4D9EE; width: 1%; padding: 0.6em; border: 1px solid #CCC; " id="Prof">Profession</td>
                        <td  style="background-color: #B4D9EE; width: 5%;  padding: 0.6em; border: 1px solid #CCC; " id="Pur">Purpose</td>
                        <td  style="background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Status">Status</td>
                         <td  style="background-color: #B4D9EE; width: 5%;  padding: 0.6em; border: 1px solid #CCC; " id="ut">User Type</td>
                        <td colspan="2" style="background-color: #B4D9EE; width: 5%; padding: 0.6em; border: 1px solid #CCC; " id="Address">Address</td>
                        <td   colspan="2"style="background-color: #B4D9EE; width: 8%; border: 1px solid #CCC; " id="Yes">Activate</td><td></td>

                        <th  style="background-color: #B4D9EE; width: 3.0em;  padding: 0.6em; border: 1px solid #CCC ;" id="No">No</th>


                        <th  style="background-color: #B4D9EE; width: 10.0em; padding: 0.6em; border: 1px solid #CCC; " id="Exp_Fix_Date">Exp Fix Date </th>
</tr>

                </thead>
                <tbody  id="SearchResultBody" style=" border-collapse: collapse ;   text-align: center " >

</tbody>
                    <tr><td colspan="5" style="background-color: #B4D9EE; width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><input id="Ar"  value="Assign Roles" type="button" onclick="activateUser()"></td>
                        <td  colspan="5" style="background-color: #B4D9EE; width: 5%;  padding: 0.6em; border: 1px solid #CCC; " ><input id="r"  value="Deactivate Users" type="button" onclick="dactUser()"></td>
                    </tr>
                    </table> </form></div>-->

        <div id="uploadfile" style="height: 60%; width: 60% ;">
         <form action="createtableAction.do?param=AddExcel"  method="post" style="height: 90%; width: 90% ;" enctype="multipart/form-data">
            <div>
                <h1 align="center"><%=TranslaterHelper.getTranslatedInLocale("Add_Excel_Sheet", cle)%></h1>
            </div>
            <div  align="center">
                <table>
<!--                    <tr><td class="migrate" align="left" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Workbook Name:&nbsp;&nbsp;</label></td>
                          <td class="migrate" style="padding: 0.6em;"><input id="fn" type="text" style="padding: 0.6em;height:15px;" placeholder="Workbook Name:"></td>
                       </tr>-->
                    <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;"><%=TranslaterHelper.getTranslatedInLocale("File_Name", cle)%>: <label style="color:Red">(.xlsx files only)</label></td>
                    <td align="left" colspan="1" class="migrate"><input id="filename" name="filename" type="file" multiple="multiple" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                </table>
                 <input type="submit" class='navtitle-hover' onclick ="return Checkfiles()"  value="<%=TranslaterHelper.getTranslatedInLocale("done", cle)%>" title="">

            </div>
        </form>
    </div>
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
//                    $("#uploadfile").dialog({
//                        autoOpen: false,
//                        height: 450,
//                        width:750,
//                        position: 'justify',
//                        modal: true
//                    });
      });

             function Checkfiles()
            {
                var fup = document.getElementById('filename');

                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                  if( fileName!=null && ext == "xlsx" )
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

           function LoadXslx(){

$('#uploadfile').dialog('open');

}
 function CancelDialog(id){

$("#"+id).dialog('close');

}

function OpenSheetdialog(){

alert($("#uploadfile1234").val());

}

        </script>
    </body>
</html>
