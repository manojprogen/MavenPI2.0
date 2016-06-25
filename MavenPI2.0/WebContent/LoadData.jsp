<%@page contentType="text/html" pageEncoding="UTF-8" import="java.io.File,prg.db.Container,prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%--
    Document   : AddExcel
    Created on : Mar 31, 2015, 9:30:19 PM
    Author     : Mohit & Dinanath
--%>
<%
            File tempFile = null;
            String filePath = null;
            filePath = File.separator+"usr"+File.separator+"local"+File.separator+"cache"+File.separator+"UploadedExcels";
            tempFile = new File(filePath);
            if (tempFile.exists()) {
                
            } else {
                tempFile.mkdirs();
            }
            tempFile = new File(filePath);
            File[] allFilesAndDirs = tempFile.listFiles();
            // String fileStr="";
            //for (File f1 : allFilesAndDirs) {
            // 
            //  fileStr=fileStr+","+f1.getName();
            // 
            //  }

            String qry = "";
            qry = "select * from PRG_USER_CONNECTIONS";
            PbDb pbdb = new PbDb();
            PbReturnObject list = null;

            list = pbdb.execSelectSQL(qry);
            int vals = 0;
            vals = list.getRowCount();
            String contextPath=request.getContextPath();

%>
<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/newUI/jquery-1.11.1.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>-->
<!--        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
<!--        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%//=themeColor%>/metadataButton.css" rel="stylesheet" />-->
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/dragAndDropTable.js"></script>
        <title>Upload File</title>

        <html:base/>
       
        <style type="text/css">
            *{font:10px verdana;}
        </style>
    </head>
    <body >

        <!--        <h1>hello world</h1>-->
        <div id="uploadfile1" style="height: 60%; width: 30% ;top:10px;left:10px">
            <table style="width: 100% ;" >
                <tr>
                    <td align="left" style="font-size:larger;  width: 30%;  padding: 0.6em; border: 1px solid #CCC; "><label>Choose Excel:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td align="right" style="font-size:larger;  width: 70%;  padding: 0.6em; border: 1px solid #CCC;" ><select name="bbn" id="Status" style="width:100%;" onchange="GetAllSheets(this.id)">
                            <option selected value="--SELECT--">--SELECT--</option>

                            <%
                                        for (File f1 : allFilesAndDirs) {
                                            //
                                            //  fileStr=fileStr+","+f1.getName();
                                            // 
%>
                            <option value="<%=f1.getName()%>"><%=f1.getName()%></option>

                            <%}%>
                        </select>
                    </td></tr>

            </table></div>
        <div id="AllSheets" style="height: 30%; width: 30% ;top:10px;">
            <!--               <form id="AllSheetsdewForm" method="post" action="">-->
            <div>
                <h1 align="center">WorkBook Object Information</h1>
            </div>
            <table style="width: 100% ;border: 1px solid #CCC;" >
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>WorkBook Object Name:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="padding: 0.6em;"><input id="wbo" type="text" style="padding: 0.6em;height:15px;width:200px" placeholder="Enter WorkBook Object Name" /></td>

                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>File Name:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="padding: 0.6em;"><input id="fn" type="text" readonly="true" style="padding: 0.6em;height:15px;width:200px" placeholder="" /></td>

                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Location:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="loc" value="Variable" style="color:black" checked id="Variable" onclick="ShowLoc(this.id)" />
                            Variable &nbsp;&nbsp;</center>
                    </td><td style="width:10%"><input type="radio" name="loc" value="Fixed" style="color:black" id="Fixed" onclick="ShowLoc(this.id)" />
                        Fixed &nbsp;&nbsp;&nbsp;&nbsp;
                    </td></tr>
                <tr id="fullpath" style="display:none">
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Full Path:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="padding: 0.6em;"><input id="fullpath1" type="text" style="padding: 0.6em;height:15px;"  placeholder="eg. C:\usr\local\cache\Sample.xslx "></td>.
                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>WorkBook Type:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="wbtype" value="ss" style="width:auto;color:black" id="ss">
                            Single Sheet&nbsp;&nbsp;</center>
                    </td><td style="width:10%" ><input type="radio" value="ms" name="wbtype" style="width:auto;color:black" id="ms">
                        Multiple Sheet&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Insert Data Into:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="insertdata" style="width:auto;color:black" id="st" value ="st" onclick="ShowSheets(this.id)" />
                            Single Table&nbsp;&nbsp;</center>
                    </td><td style="width:10%" ><input type="radio" name="insertdata" style="width:auto;color:black" id="mt" value="mt" onclick="ShowSheets(this.id)"/>
                        Multiple Table&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align="left" colspan="1"  style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Connection Name</label></td>
                    <td  style="padding: 0.6em;">
                        <select name="connid" id="connid" style="width:150px;" onchange="getbbg(this.id)" ><option> --SELECT-- </option>
                           <% for (int i = 0; i < list.getRowCount(); i++) {
                            %>
                            <option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>
            </table>
            <div>
                <h1 align="center">Table Information</h1>
            </div>
            <table id="sstable"style="width: 100% ;border: 1px solid #CCC;display:none;" >

                <tr><td align="left"  style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Table Name:</label></td>
                    <td  style="padding: 0.6em;"><select name="bbg" id="bbg" style="width:150px;" onchange=""><option> --SELECT-- </option>
                        </select></td>
                </tr>

                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Truncate Table:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="Truncate" style="width:auto;color:black" id="Yes" value ="Yes" />Yes&nbsp;&nbsp;</center></td>
                    <td style="width:10%" ><input type="radio" checked name="Truncate" style="width:auto;color:black" id="No" value="No" />
                        No&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                        <td style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; ">Sheet No.</td>
                        <!--                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="S_no">S.No.</td>-->
                        <td  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Sheet Name </td>
                        <td colspan="2" style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Select Sheet</td>
                    </tr>

                <!--                        <tr>-->
                <!--         added by Dinanath-->
                <!--     <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Select Sheet:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>-->
                <!--      <td style="width:10%"><center><input type="checkbox" name="SelectedSheet" style="width:auto;color:black" id="Selected" value ="" /></center></td>-->

                <!--                       </tr>-->

            </table>
            <table id="mstable"style="width: 100% ;border: 1px solid #CCC;display:none;" >
                <thead><tr>
                        <th style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; ">Sheet No.</th>

                        <th  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Sheet Name </th>
                        <th  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Table Name </th>
                        <th colspan="2" style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Truncate Table</th>
                        <th colspan="2" style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Select Sheet</th>
                    </tr></thead>
                <tbody  id="mstablebody" style="border-collapse: collapse ;   text-align: center " >

                </tbody>
            </table>
            <div>
                <table>
                    <tr><td title='Click Here to Save Object information and Load Sheet' style='  width: 5%;  padding: 0.6em; border: 1px solid #CCC; '>
                            <center> <input id='Ar' class='navtitle-hover'  value='Save and Load' type='button' onclick='SaveAllInfo()' /></center></td></tr>
                </table>
            </div>

            <!--</form> -->
        </div>
        <div id='loadingmetadata' class='loading_image' style="display:none;z-index: 1000000000000000">
            <img alt=""  id='imgId' src='<%=request.getContextPath()%>/images/ajaxSpinner.gif'  border='0px' height="150px" width="150px" style='position:absolute; left: 550px; top: 300px;'/>
        </div>

        <!--added by Dinanath                        -->


















        <div id="WorkbookInfo" align="center" style="">
            <br/>
            <br/>
            <!--    <form name="WorkbookInfoForm"  method="post" action="">-->
            <table  style="width:100%;border-collapse: collapse;  " id="SearchResultTable">
                <thead style=" background-color: #DDD ; text-align: center; ">
                    <tr>
                        <td colspan="11" style="font-size:larger;  width: 100%;  padding: 0.6em; border: 1px solid #CCC; ">Workbook Information</td>

                    </tr>
                    <tr>
                        <td style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " nowrap>WORKBOOK ID</td>
                        <td  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; "  id="Fn" nowrap>WORKBOOK OBJECT</td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Email" nowrap>FILE NAME</td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Mob" nowrap>LOCATION</td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Nationality" nowrap>FULL PATH</td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Country" nowrap>WORKBOOK TYPE</td>
                        <td  style=" width: 1%; padding: 0.6em; border: 1px solid #CCC; " id="Prof" nowrap>TABLE TYPE</td>
                        <td  style=" width: 5%;  padding: 0.6em; border: 1px solid #CCC; " id="Pur" nowrap>CONN ID</td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Status" nowrap>LAST LOADED</td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="ut" nowrap>TOTAL SHEETS</td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="utd" nowrap>LOAD DATA</td>

                    </tr>
                </thead>
                <tbody  id="appendWorkbookRowData" style=" border-collapse: collapse ;   text-align: center " >


                </tbody>
            </table>
            <!--    </form>-->
        </div>
        <div id='uploadfileForVariable' style="display:none">


        </div>
        <!--                  endded by Dinanath-->

<!--added by Dinanath-->
        <div id="AllSheetsSecondTime" style="height: 60%; width: 30% ;top:100px;" >
            <div>
                <h1 align="center">WorkBook Object Information</h1>
            </div>
            <table style="width: 100% ;border: 1px solid #CCC;" >
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>WorkBook Object Name:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="padding: 0.6em;"><input id="wbObjectName" type="text" style="padding: 0.6em;height:15px;width:200px" readonly /></td>

                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>File Name:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="padding: 0.6em;"><input id="fileNameSecondTime" type="text" readonly="true" style="padding: 0.6em;height:15px;width:200px"  /></td>

                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Location:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="loc" value="Variable" style="color:black" checked id="VariableSTime" onclick="ShowLoc(this.id)" />
                            Variable &nbsp;&nbsp;</center>
                    </td><td style="width:10%"><input type="radio" name="loc" value="Fixed" style="color:black" id="FixedSTime" onclick="ShowLoc(this.id)" />
                        Fixed &nbsp;&nbsp;&nbsp;&nbsp;
                    </td></tr>
                <tr id="fullpathSTime" style="display:none">
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Full Path:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="padding: 0.6em;"><input id="fullpath1STime" type="text" style="padding: 0.6em;height:15px;" readonly /></td>.
                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>WorkBook Type:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="wbtype" value="ss" style="width:auto;color:black" id="ssSTime"/>
                            Single Sheet&nbsp;&nbsp;</center>
                    </td><td style="width:10%" ><input type="radio" value="ms" name="wbtype" style="width:auto;color:black" id="msSTime"/>
                        Multiple Sheet&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Insert Data Into:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="insertdata" style="width:auto;color:black" id="stSTime" value ="st" onclick="ShowSheetsSTime(this.id)" />
                            Single Table&nbsp;&nbsp;</center>
                    </td><td style="width:10%" ><input type="radio" name="insertdata" style="width:auto;color:black" id="mtSTime" value="mt" onclick="ShowSheetsSTime(this.id)"/>
                        Multiple Table&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align="left" colspan="1"  style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Connection Name</label></td>
                    <td  style="padding: 0.6em;">
                        <select name="connidSTime" id="connidSTime" style="width:150px;" onchange="getbbgSTime(this.id)" ><option id="connidDefaultNameSTime"> --SELECT-- </option>
                            <% for (int i = 0; i < list.getRowCount(); i++) {
                            %>
                            <option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>
            </table>
            <div>
                <h1 align="center">Table Information</h1>
            </div>
            <table id="sstableSTime"style="width: 100% ;border: 1px solid #CCC;display:none;" >
                <tr><td align="left"  style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Table Name:</label></td>
                    <td  style="padding: 0.6em;"><select name="bbgSTime" id="bbgSTime" style="width:150px;" onchange=""><option> --SELECT-- </option>
                        </select></td>
                </tr>
                <tr>
                    <td align="left" style="font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><label>Truncate Table:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td style="width:10%"><center><input type="radio" name="TruncateSTime" style="width:auto;color:black" id="YesSTime" value ="Yes" />Yes&nbsp;&nbsp;</center></td>
                    <td style="width:10%" ><input type="radio" checked name="TruncateSTime" style="width:auto;color:black" id="NoSTime" value="No" />
                        No&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                        <td style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; ">Sheet No.</td>
                        <td  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Sheet Name </td>
                        <td colspan="2" style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Select Sheet</td>
                    </tr>

            </table>
            <table id="mstableSTime"style="width: 100% ;border: 1px solid #CCC;display:none;" >
                <thead><tr>
                        <th style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; ">Sheet No.</th>
                        <th  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Sheet Name </th>
                        <th  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Table Name </th>
                        <th colspan="2" style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Truncate Table</th>
                        <th colspan="2" style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " >Select Sheet</th>
                    </tr></thead>
                <tbody  id="mstablebodySTime" style="border-collapse: collapse ;   text-align: center " >

                </tbody>
            </table>
            <div>
                <table>
                    <tr><td title='Click Here to Save Object information and Load Sheet' style=' align:center; width: 5%;  padding: 0.6em; border: 1px solid #CCC; '>
                            <center> <input id='ArSTime' class='navtitle-hover'  value='Next' type='button' onclick='SaveAllInfoNext()'/></center></td></tr>
                </table>
            </div>

            <!--</form> -->
        </div>
<!--             endded by Dinanath           -->
 <script type="text/javascript">
            //added by Dinanath
           var globalwbid;
           var globalwbobject;
           var globalfn;
           var globallocation;
           var globalfullpath;
           var globalwbtype;
           var globaltabletype;
           var globalconnid;
           var globallastloaded;
           var globaltotalsheets;

            var sheets;
            var allsheets;
            var workbookId;
            var totalSheetsOfPWBID;
            $(document).ready(function(){
                $.get("<%= request.getContextPath()%>/createtableAction.do?param=getLoadDataFromAlreadyInsertedWorkbook&select=all&status=all",
                function(data){
                    //alert(data);
                    $("#appendWorkbookRowData").html(data);
                });

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
                $("#AllSheets").dialog({
                    autoOpen: false,
                    height: 520,
                    width:600,
                    //                        position: 'justify',
                    position: [380,40],
                    modal: true
                });
                $("#uploadfileForVariable").dialog({
                    autoOpen: false,
                    height: 250,
                    width:400,
                    //  position: 'justify',
                    position: [280,0],
                    modal: true
                });

                $("#AllSheetsSecondTime").dialog({
                    autoOpen: false,
                    height: 520,
                    width:600,
                    //                        position: 'justify',
                    position: [380,0],
                    modal: true
                });
            });

            function GetAllSheets(id)
            {
                var sheetname=$("#"+id).val();
                $("#fn").val(sheetname);
                //            alert(sheetname)
                //   sheets;tml;

                var html="";
                var htmlforSingleTable="";
                //         alert("html"+html)
                if(sheetname=="--SELECT--")
                {

                }
                else
                {

                    //                       alert(sheetname)
                    $.ajax({
                        async:false,
                        data:{'sheetname':sheetname},
                        url:'createtableAction.do?param=GetAllSheets',
                        success:function(data) {
                            allsheets=data;
                            sheets=data.split("::");
                            //                     alert("sheets"+sheets)
//                                                alert('Totalsheets '+(sheets.length))
                    var sheetLen=sheets.length;
                            for(var j=0;j<sheetLen-1;j++)
                            {
                             html+="<tr><td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>"+(j+1)+"</td>\n\
                                <td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>"+sheets[j]+"</td>\n\
                                 <td  style='padding: 0.6em;'><select name='bbg' id='bbg"+(j+1)+"' style='width:150px;'><option> --SELECT-- </option>\n\
                                 </select></td><td style='width:10%'><center><input type='radio' name='Truncate"+(j+1)+"' style='width:auto;color:black'  value ='Yes' />\n\
                                  Yes&nbsp;&nbsp;</center>\n\
                       </td><td style='width:10%' ><input type='radio' checked name='Truncate"+(j+1)+"' style='width:auto;color:black'  value='No' />\n\
                                  No&nbsp;&nbsp;&nbsp;&nbsp;\n\
                       </td><td style='width:10%' ><input type='checkbox' checked id='SelectedSheet"+(j+1)+"' style='width:auto;color:black'  value='' />\n\
</td></tr>";
                            }

        htmlforSingleTable+="<tr><td align='left'  style='font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; '><label>Table Name:</label></td>";
        htmlforSingleTable+="<td  style='padding: 0.6em;'><select name='bbg' id='bbg' style='width:150px;' onchange=''><option> --SELECT-- </option></select> </td>";
        htmlforSingleTable+="</tr>";
        htmlforSingleTable+="<tr>";
        htmlforSingleTable+="<td align='left' style='font-size:larger;  width: 5%;  padding: 0.6em; border: 1px solid #CCC; '><label>Truncate Table:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>";
        htmlforSingleTable+="<td style='width:10%'><center><input type='radio' name='Truncate' style='width:auto;color:black' id='Yes' value ='Yes' >Yes&nbsp;&nbsp;</center></td>";
        htmlforSingleTable+="<td style='width:10%' ><input type='radio' checked name='Truncate' style='width:auto;color:black' id='No' value='No' >No&nbsp;&nbsp;&nbsp;&nbsp;</td>";
        htmlforSingleTable+="</tr>";
        htmlforSingleTable+="<tr>";
        htmlforSingleTable+="<td style=' width: 1%;  padding: 0.6em; border: 1px solid #CCC; '>Sheet No.</td>";
        htmlforSingleTable+="<td  style=' width: 4%;  padding: 0.6em; border: 1px solid #CCC; ' >Sheet Name </td>";
        htmlforSingleTable+="<td colspan='2' style=' width: 4%;  padding: 0.6em; border: 1px solid #CCC; ' >Select Sheet</td>";
        htmlforSingleTable+="</tr>";



                               for(var k=0;k<sheetLen-1;k++)
                            {
                             htmlforSingleTable+="<tr><td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>"+(k+1)+"</td>\n\
                                                    <td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>"+sheets[k]+"</td>\n\
                                            <td style='width:10%' ><input type='checkbox' checked id='SelectedSheetForSTab"+(k+1)+"' style='width:auto;color:black'  value='' />\n\
                                            </td></tr>";
                            }


                        }
                    });

                }
                //                      alert(html)
                if((sheets.length-1)==0)
                {
                    document.getElementById("ms").disabled=true;
                    document.getElementById("ss").checked = true;
                    document.getElementById("st").checked = true;
                    document.getElementById("mt").disabled=true;
                    //                      $("#mstablebody").html('');
                    //                      $("#mstablebody").html(html);
                    document.getElementById("sstable").style.display = "";
                    document.getElementById("mstable").style.display = "none";

                    //                      $("#ms").attr('disatmlbled', true);
                    //                      $("#ss").attr('checked', true);
                }
                else
                {
                    document.getElementById("ms").checked = true;
                    document.getElementById("ss").disabled = true;
                    document.getElementById("st").disabled = false;
                    document.getElementById("mt").disabled=false;
                    document.getElementById("st").checked=true;

                    document.getElementById("sstable").style.display = "";
                    $("#mstablebody").html('');
                    $("#mstablebody").html(html);
//                    $("#isSheetSelectableForSingleTab").html('');
//                    $("#isSheetSelectableForSingleTab").html(htmlforSingleTable);

//                    $('#sstable tr').last().after(htmlforSingleTable); // adding new tr after last tr of table
//                    $('#sstable tr:last').remove();
                        $("#sstable").html('');
                        $("#sstable").html(htmlforSingleTable);

                    document.getElementById("mstable").style.display = "none";
                    //                       $("#ms").attr('checked', true);
                    //                      $("#ss").attr('disabled', true);
                }
                //                      $("#ss").dialog('open');
                  $( "#Variable" ).attr( "checked", "checked" );
                $("#AllSheets").dialog('open');
            }

            function ShowLoc(id)
            {
                if(id=="Variable" && document.getElementById(id) != null && document.getElementById(id).checked){

                    document.getElementById("fullpath").style.display = "none";
                } else if(id=="Fixed" && document.getElementById(id) != null && document.getElementById(id).checked){
                    document.getElementById("fullpath").style.display = "";

                }

            }

            function ShowSheets(id)
            {
                if(id=="st" && document.getElementById(id) != null && document.getElementById(id).checked){

                    document.getElementById("sstable").style.display = "";
                    document.getElementById("mstable").style.display = "none";//changed1
                    $("#connid").val('"--SELECT--');
                    $("#AllSheets").height(550);
                } else if(id=="mt" && document.getElementById(id) != null && document.getElementById(id).checked){
                    document.getElementById("sstable").style.display = "none";
                    document.getElementById("mstable").style.display = "";
                    $("#connid").val('"--SELECT--');
                    $("#AllSheets").height(550);
                }

            }
            function ShowSheetsSTime(id)
            {
                if(id=="stSTime" && document.getElementById(id) != null && document.getElementById(id).checked){

                    document.getElementById("sstableSTime").style.display = "";
                    document.getElementById("mstableSTime").style.display = "none";
                    $("#connidSTime").val('"--SELECT--');
                    $("#AllSheetsSTime").height(550);
                } else if(id=="mtSTime" && document.getElementById(id) != null && document.getElementById(id).checked){
                    document.getElementById("sstableSTime").style.display = "none";
                    document.getElementById("mstableSTime").style.display = "";
                    $("#connidSTime").val('"--SELECT--');
                    $("#AllSheetsSecondTime").height(550);
                }
                var html="";
                $.ajax({
                    async:false,
                    data:{'workbookId':workbookId},
                    url:'createtableAction.do?param=GetAllSheetsFromDatabase',
                    success:function(data) {

                        $("#mstablebodySTime").html('');
                        $("#mstablebodySTime").html(data);


                    }
                });

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
                        for(var j=0;j<sheets.length-1;j++)
                        {
                            $("#bbg"+(j+1)).html(data)
                        }
                    });
                }
            }
            function getbbgSTime(id){

                var id=$("#"+id).val()
                //            alert(id)
                if(id=="--SELECT--")
                {
                    alert("Please Select Connection")
                }
                else
                {
                    $.get("<%= request.getContextPath()%>/createtableAction.do?param=getAllTables&conid="+id,function(data){
                        $("#bbgSTime").html(data)
                        //alert(totalSheetsOfPWBID);
                        for(var j=0;j<totalSheetsOfPWBID;j++)
                        {
                            $("#bbgSTime"+(j+1)).html(data)
                        }
                    });
                }
            }
//code added by mohit and modified by Dinanath
            function SaveAllInfo(){

        var wbo=$("#wbo").val();
        var  fn=$("#fn").val();
        var loc=$("input[name='loc']:checked").val();
        var fullpath=$("#fullpath1").val();
        // alert(fullpath)
        var wbtype=$("input[name='wbtype']:checked").val();
        var insertdata=$("input[name='insertdata']:checked").val();
        var connid=$("#connid").val();
        var bbg=$("#bbg").val();
        var Truncate=$("input[name='Truncate']:checked").val();
        var alltables="";
        var alltruncate="";
        var selectedSheet="";
        //        alert(wbo+fn+loc+fullpath+wbtype+insertdata+connid+bbg)
        if(wbo=="")
        {
            alert("Please Enter Workbook Object Name")
            return false;
        }
        else if(loc=="Fixed" && fullpath=="" )
        {
            alert("Please Enter Full Path for Excel File")
            return false;
        }
        else if(connid=="--SELECT--")
        {
            alert("Please Select Connection")
            return false;
        }
        else if(insertdata=="st")
        {
            if(bbg=="--SELECT--")
            {
                alert("Please Select Table Name")
                return false;
            }
            for(var j=0;j<sheets.length-1;j++)
            {

                selectedSheet+=$("#SelectedSheetForSTab"+(j+1)).is(':checked')+"::";


            }
            var flag=false;
            var allsheetinfo=selectedSheet.split("::");
            for(var i=0;i<allsheetinfo.length;i++){
                if(allsheetinfo[i]=="true"){
                    flag=true;
                }
            }
            if(flag==false){
                alert("Please select at least one sheet");
                return false;
            }
        }
        else if(insertdata=="mt")
        {
            for(var j=0;j<sheets.length-1;j++)
            {
                alltables += $("#bbg"+(j+1)).val()+"::";
                alltruncate+= $("input[name='Truncate"+(j+1)+"']:checked").val()+"::";
//                if($("#bbg"+(j+1)).val()=="--SELECT--")
//                {
//                    alert("Please Select Table Name for "+sheets[j])
//                    return false;
//                }
                selectedSheet+=$("#SelectedSheet"+(j+1)).is(':checked')+"::";


            }
            //                     if(!selectedSheet.toString().contains("true")){
            //                     alert("Please select at least one sheet");
            //                     return false;
            //                     }
            var flag=false;
            var allsheetinfo=selectedSheet.split("::");
            for(var i=0;i<allsheetinfo.length;i++){
                if(allsheetinfo[i]=="true"){
                    flag=true;
                     if($("#bbg"+(i+1)).val()=="--SELECT--")
                     {
                    alert("Please Select Table Name for "+sheets[i])
                    return false;
                    }
                }
            }
            if(flag==false){
                alert("Please select at least one sheet");
                return false;
            }


        }
//               alert("else");
                  $("#loadingmetadata").show();
                //               alert(selectedSheet);
                $("#AllSheets").dialog('close');

                $.ajax({
                    async:true,
                    data:{'wbo':wbo,'fn':fn,'loc':loc,'fullpath':fullpath,
                        'wbtype':wbtype,'insertdata':insertdata,'connid':connid,'bbg':bbg,
                        'sheets':allsheets,'alltables':alltables,'Truncate':Truncate,'alltruncate':alltruncate,'selectedSheet':selectedSheet},
                    url:'createtableAction.do?param=SaveAndUploadXslx',
                    success:function(data) {
                        alert("All Data has uploaded successfully"+data);
                        $("#loadingmetadata").hide();
                        window.location.href="<%= request.getContextPath()%>/srchQueryAction.do?srchParam=pbBiManager";
                    }
                });

            }
            //added by Dinanath
            function loadDataXls(wbid,wbobject,fn,location,fullpath,wbtype,tabletype,connid,lastloaded,totalsheets){
             globalwbid='';
            globalwbobject='';
            globalfn='';
            globallocation='';
            globalfullpath='';
            globalwbtype='';
            globaltabletype='';
            globalconnid='';
            globallastloaded='';
            globaltotalsheets='';
            globalwbid=wbid;
            globalwbobject=wbobject;
            globalfn=fn;
            globallocation=location;
            globalfullpath=fullpath;
            globalwbtype=wbtype;
            globaltabletype=tabletype;
            globalconnid=connid;
            globallastloaded=lastloaded;
            globaltotalsheets=totalsheets;
                document.getElementById('wbObjectName').value=wbobject;
                document.getElementById('fileNameSecondTime').value=fn;

                if(location=='Variable'){
                    $( "#VariableSTime" ).attr( "checked", "checked" );
                    document.getElementById("FixedSTime").disabled = true;
                    document.getElementById("fullpathSTime").style.display = "none";
                }else if(location=='Fixed'){
                    $( "#FixedSTime" ).attr( "checked", "checked" );
                    document.getElementById("VariableSTime").disabled = true;
                    document.getElementById("fullpathSTime").style.display = "";
                    document.getElementById("fullpath1STime").value=fullpath;
                }
                if(wbtype=='ss'){
                    $( "#ssSTime" ).attr( "checked", "checked" );
                    document.getElementById("msSTime").disabled = true;
                }else if(wbtype=='ms'){
                    //    alert('ms')
                    $( "#msSTime" ).attr( "checked", "checked" );
                    document.getElementById("ssSTime").disabled = true;
                }
                if(tabletype=='st'){
                    $( "#stSTime" ).attr( "checked", "checked" );
                    document.getElementById("mtSTime").disabled = true;
                }else if(tabletype=='mt'){
                    //    alert("mt");
                    $( "#mtSTime" ).attr( "checked", "checked" );
                    document.getElementById("stSTime").disabled = true;
                }
                if(tabletype=='st'){
                    $("#mstablebodySTime").html('');
                    document.getElementById("sstableSTime").style.display = "";
                    document.getElementById("mstableSTime").style.display = "none";
                    $("#connidSTime").val('"--SELECT--');
                    $("#AllSheetsSTime").height(550);
                      $.ajax({
                        async:false,
                        data:{'workbookId':wbid,'tabletype':tabletype},
                        url:'createtableAction.do?param=GetAllSheetsFromDatabase',
                        success:function(data) {

                            $("#sstableSTime").html('');
                            $("#sstableSTime").html(data);
                        }
                    });

                }
                if(tabletype=='mt'){
                    document.getElementById("sstableSTime").style.display = "none";
                    document.getElementById("mstableSTime").style.display = "";
                    $("#connidSTime").val('"--SELECT--');
                    $("#AllSheetsSecondTime").height(550);

                    $.ajax({
                        async:false,
                        data:{'workbookId':wbid,'tabletype':tabletype},
                        url:'createtableAction.do?param=GetAllSheetsFromDatabase',
                        success:function(data) {

                            $("#mstablebodySTime").html('');
                            $("#mstablebodySTime").html(data);
                        }
                    });
                }
                workbookId='';
                workbookId=wbid;
                totalSheetsOfPWBID='';
                totalSheetsOfPWBID=totalsheets;
                var htmldata="";
                htmldata+="<option > --SELECT-- </option>";
            <% for (int i = 0; i < list.getRowCount(); i++) {
            %>
                                    var idConn='<%=list.getFieldValueInt(i, 0)%>';
                                    var nameComm='<%=list.getFieldValueString(i, 1)%>';
                                    if(idConn==connid){
                                        htmldata+="<option value='"+idConn+"' selected>"+nameComm+"</option>";
                                    }else{
                                        htmldata+="<option value='"+idConn+"' >"+nameComm+"</option>";
                                    }
            <%}%>

                                    $("#connidSTime").html('');
                                    $("#connidSTime").html(htmldata);

                                    $("#AllSheetsSecondTime").dialog('open');


                                }
                                function SaveAllInfoNext(){
    var connid=$("#connidSTime").val();
    var singleTName=$("#bbgSTime").val();
    var singleTruncate=$("input[name='TruncateSTime']:checked").val();
    var alltables="";
    var alltruncate="";
    var selectedSheet="";
    var allSheetName="";
    if(connid=="--SELECT--")
    {
        alert("Please Select Connection")
        return false;
    }
    else if(globaltabletype=="st")
    {
        if(singleTName=="--SELECT--")
        {
            alert("Please Select Table Name")
            return false;
        }
          for(var j=0;j<globaltotalsheets;j++)
        {
        allSheetName+=document.getElementById("allSheetName"+(j+1)).innerHTML+"::";
        selectedSheet+=$("#SelectedSheetSTime"+(j+1)).is(':checked')+"::";
        }
         var flag=false;
        var allsheetinfo=selectedSheet.split("::");
        for(var i=0;i<allsheetinfo.length;i++){
            if(allsheetinfo[i]=="true"){
                flag=true;
            }
        }
        if(flag==false){
            alert("Please select at least one sheet");
            return false;
        }
    }
    else if(globaltabletype=="mt")
    {
        for(var j=0;j<globaltotalsheets;j++)
        {
            alltables += $("#bbgSTime"+(j+1)).val()+"::";
            allSheetName+=document.getElementById("allSheetName"+(j+1)).innerHTML+"::";
            alltruncate+= $("input[name='TruncateSTime"+(j+1)+"']:checked").val()+"::";
            selectedSheet+=$("#SelectedSheetSTime"+(j+1)).is(':checked')+"::";
//            if($("#bbgSTime"+(j+1)).val()=="--SELECT--")
//            {
//                alert("Please Select Table Name for Selected sheet")
//                return false;
//            }

        }
        //                    if(!selectedSheet.toString().contains("true")){
        //                        alert("Please select at least one sheet");
        //                        return false;
        //                    }
        var flag=false;
        var allsheetinfo=selectedSheet.split("::");
        for(var i=0;i<allsheetinfo.length;i++){
            if(allsheetinfo[i]=="true"){
                flag=true;
                 if($("#bbgSTime"+(i+1)).val()=="--SELECT--")
                 {
                var sheetname=document.getElementById("allSheetName"+(i+1)).innerHTML;
                alert("Please Select Table for Selected Sheet "+sheetname)
                return false;
                 }

            }
        }
        if(flag==false){
            alert("Please select at least one sheet");
            return false;
        }

    }

    $("#AllSheetsSecondTime").dialog('close');
    //           $("#AllSheetsSecondTime").dialog({
    //                    autoOpen: false,
    //                    modal: true
    //               });

    if(globallocation=="Fixed" ||globallocation=="fixed"){
        $("#loadingmetadata").show();
        $.ajax({
            async:true,
            data:{'fn':globalfn,'fullpath':globalfullpath,
                'wbtype':globalwbtype,'tabletype':globaltabletype,
                'connid':connid,'totalsheets':globaltotalsheets,'wbid':globalwbid,
                'allSheetName':allSheetName,'alltables':alltables,'alltruncate':alltruncate,'selectedSheet':selectedSheet,
                'singleTName':singleTName,'singleTruncate':singleTruncate

            },
            url:'createtableAction.do?param=getFixedFullpathFilename',
            success:function(data) {
                $("#loadingmetadata").hide();
                if(data=="success"){
                    alert("Data loading is finished successfully");
                    window.location.href="<%= request.getContextPath()%>/srchQueryAction.do?srchParam=pbBiManager";
                }else{
                    alert("Exception occured at:"+data);
                    window.location.href="<%= request.getContextPath()%>/srchQueryAction.do?srchParam=pbBiManager";
                }
            }
        });
    }
    var variableLoc="";
    if(globallocation=="Variable" || globallocation=="variable"){
        variableLoc="<form action='createtableAction.do?param=addExcelData&singleTName="+singleTName+"&singleTruncate="+singleTruncate+"&allSheetName="+allSheetName+"&alltables="+alltables+"&alltruncate="+alltruncate+"&selectedSheet="+selectedSheet+"&wbid="+globalwbid+"&globaltotalsheets="+globaltotalsheets+"&fn="+globalfn+"&fullpath="+globalfullpath+"&connid="+connid+"&tabletype="+globaltabletype+"'  method='post'  enctype='multipart/form-data'>\n\
           <div>\n\
               <h1 align='center'>Add Excel Sheet</h1>\n\
            </div>\n\
            <div  align='center'>\n\
               <table>\n\
                    <tr style='width:100%'>\n\
                    <td align='left' colspan='1' class='migrate' style=' padding: 0.6em; border: 1px solid #CCC;'>File Name: <label style='color:Red'>(.xlsx files only)</label></td>\n\
                    <td align='left' colspan='1' class='migrate'><input id='filename' name='filename' type='file' multiple='multiple' style='background-color:lightgoldenrodyellow; color:black;'/></td>\n\
                </tr>\n\
                </table>\n\
                 <input type='submit' class='navtitle-hover' onclick ='return Checkfiles1()'  value='Done' title=''>\n\
                          </div>\n\
       </form>";
        $("#uploadfileForVariable").html(variableLoc);
        $("#uploadfileForVariable").dialog('open');
    }

}
function Checkfiles1()
            {
                var fup = document.getElementById('filename');

                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                  if( fileName!=null && ext == "xlsx" ){
                    //$("#loadingmetadata").show();
                    $("#uploadfileForVariable").dialog('close');
                    $("#loadingmetadata").show();

                    return true;
                }
                else
                {
                    alert("Upload .xlsx files only");
                    return false;
                }
            }
        </script>
    </body>
</html>
