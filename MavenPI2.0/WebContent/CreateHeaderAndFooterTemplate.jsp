<%--
    Document   : CreateHeaderAndFooterTemplate
    Created on : Aug 14, 2015, 12:08:49 PM
    Author     : Kruthika
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.reportview.db.PbReportViewerDAO,java.io.File"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<% String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <script type="text/javascript" src="<%=contextPath%>/JS/jquery-1.11.1.js"></script>

            <style>

</style>
    </head>
    <body>
        <script type="text/javascript">
            function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#imageOutput')
                    .attr('src', e.target.result)
                    .width(705)
                    .height(90);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }
        function readURLLeft(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#imageOutputLeft')
                    .attr('src', e.target.result)
                    .width(100)
                    .height(80);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }
         function readURLRight(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#imageOutputRight')
                    .attr('src', e.target.result)
                    .width(100)
                    .height(80);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }
    $("#message").keyup(function(){
        $('#messageOutput').html($(this).val());
});
 $("#signature").keyup(function(){
        $('#htmlCodeOutput').html($(this).val());
});
$("#optionalHeader").keyup(function(){
        $('#htmlCodeOptionHeaderOutput').html($(this).val());
});
$("#optionalFooter").keyup(function(){
        $('#htmlCodeOptionFooterOutput').html($(this).val());
});
function CreateSignatureAsHtml(){
    var htmlSignetureContent=$('#signature').val();
    $.ajax({
                        async:false,
                        data:{'signatureHtml': htmlSignetureContent},
                        url:'createtableAction.do?param=CreateHtmlSignatureForScheduler',
                        success:function(data) {
                        alert('Your Signature has created');
                        }
    });
}
function CreateOptionalHeaderAsHtml(){
var htmlSignetureContent=$('#optionalHeader').val();
    $.ajax({
                        async:false,
                        data:{'optionalHeaderMessage': htmlSignetureContent},
                        url:'createtableAction.do?param=CreateOptionalHeaderAsHtml',
                        success:function(data) {
                        alert('Your Optional Header has created successfully.');
                        }
    });
}
function CreateOptionalFooterAsHtml(){
var htmlSignetureContent=$('#optionalFooter').val();
    $.ajax({
                        async:false,
                        data:{'optionalFooterMessage': htmlSignetureContent},
                        url:'createtableAction.do?param=CreateOptionalFooterAsHtml',
                        success:function(data) {
                        alert('Your Optional Footer has created successfully.');
                        }
    });
}
            function checkFileAvailability(){
                                    <%
                                    String flag1="Not Uploaded";
                                    String flag2="Not Uploaded";
                                    String flag3="Not Uploaded";
                                    String flag4="Not Uploaded";
                                    String flag5="Not Uploaded";
                                    String flag6="Not Uploaded";
                    String filepath = null;
                    PbReportViewerDAO pbDAO=new PbReportViewerDAO();
                    if(session != null){
                            filepath = pbDAO.getFilePath(session);
                    }else {
                            filepath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                    }

                                    File file1 = new File(filepath + File.separator + "HeaderImageOFHtmlEmail.jpg");
                                    File file2 = new File(filepath + File.separator + "HtmlOptionalHeaderForScheduler.html");
                                    File file3 = new File(filepath + File.separator + "HtmlSignatureForScheduler.html");
                                    File file4 = new File(filepath + File.separator + "HtmlOptionalFooterForScheduler.html");
                                    File file5 = new File(filepath + File.separator + "leftLogoImageOfScheduler.jpg");
                                    File file6 = new File(filepath + File.separator + "rightLogoImageOfScheduler.jpg");
                                    if(file1.exists()){
                                        flag1="Available";
                                        }
                                    if(file2.exists()){
                                        flag2="Available";
                                        }
                                    if(file3.exists()){
                                        flag3="Available";
                                        }
                                    if(file4.exists()){
                                        flag4="Available";
                                        }
                                    if(file5.exists()){
                                        flag5="Available";
                                        }
                                    if(file6.exists()){
                                        flag6="Available";
                                        }
            %>
            }
        </script>
        <h1 style="background-color: #B4D9EE;">Create, Update and Upload Header logo , Footer Logo, Signature, Optional Header Text and Optional Footer Text for Scheduler</h1><br>
<!--        <input type="button" class="navtitle-hover" value="Check Availability of Files" onclick="checkFileAvailability()">-->
        <div id="checkAvailableFiles" style="" rules="all">
            <table style="border:solid 1px;width:50%;border-color:black;">
                <tr><th style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;width:50%;">Options In Report Schedule</th><th style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;">Availability of File on System</th></tr>
                <tr style="background-color: rgb(206, 222, 239); padding: 0.6em; border: 1px solid black;"><td>Header Image</td><td><%=flag1%></td><tr>
                <tr style="background-color: rgb(206, 222, 239); padding: 0.6em; border: 1px solid black;"><td>Optional Header Text</td><td><%=flag2%></td><tr>
                <tr style="background-color: rgb(206, 222, 239); padding: 0.6em; border: 1px solid black;"><td>Signature</td><td><%=flag3%></td><tr>
                <tr style="background-color: rgb(206, 222, 239); padding: 0.6em; border: 1px solid black;"><td>Optional Footer Text</td><td><%=flag4%></td><tr>
                <tr style="background-color: rgb(206, 222, 239); padding: 0.6em; border: 1px solid black;"><td>Footer Left Image</td><td><%=flag5%></td><tr>
                <tr style="background-color: rgb(206, 222, 239); padding: 0.6em; border: 1px solid black;"><td>Footer Right Image</td><td><%=flag6%></td><tr>
            </table>
        </div>
            <br/>
        <div id="uploadHeaderImage" style="height: 250px; width: 60% ;margin-bottom: 2%;border:1px solid #003f81;">
         <form action="createtableAction.do?param=AddHeaderPhotoScheduler"  method="post" style="height: 90%; width: 90% ;" enctype="multipart/form-data">
            <div><br>
                    <h1 align="center">Upload Header Logo For Report Schedule</h1>
            </div><br>
            <div  align="center">
                <table style="width:90%">

                    <tr style="width:100%">
                            <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;width: 40%">Upload Image: </td>
                    <td align="left" colspan="1" class="migrate"><input id="filename" name="filename" type="file" multiple="multiple" onchange="readURL(this);"  style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                </table><br>
                 <input type="submit" class='navtitle-hover' onclick ="return Checkfiles()"  value="Upload" title=""><br>
                 <div style="border:1px solid lightblue;margin-top:1%;"><table style="width:100%"><tr><td>
                 <img id="imageOutput" src="#" alt="Your Image Output" />
                 </td></tr></table>
                 </div>
            </div>
        </form>
    </div>

     <div id="optionalHeaderDiv" style="height: 300px; width: 60% ;margin-bottom: 2%;border:1px solid #003f81;">
         <form action="createtableAction.do?param=AddExcel"  method="post" style="height: 90%; width: 90% ;" >
            <div>
                    <h1 align="center">Add or Update Optional Header Text For Report Schedule</h1>
            </div><br>
            <div  align="center">
                <table style="width:100%;">

                    <tr >
                            <!--                    <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;width: 35%;">Signature In Html:</td>-->
                    <td align="left" colspan="1" class="migrate"><textarea id="optionalHeader" name="optionalHeader" style="width:100%;height: 100px;" placeholder="Ex.
                                                                           <h3>Something</h3>
                                                                                                                                                 <h3>Write here</h3>
                                                                                                                                                                <div>and see output below</div>"></textarea></td>
                    <td colspan="2"></td>
                </tr>
                </table><br>
                    <input type="button" class='navtitle-hover' onclick ="CreateOptionalHeaderAsHtml()"  value="Update" title="">
                  <div style="border:1px solid lightblue;margin-top:1%;">
                     <table style="width:100%">
                         <tr>
                         <td>
                 <div id="htmlCodeOptionHeaderOutput" style="width: 100%;height:90px;overflow-y: auto;">

                 </div>
                 </td>
                         </tr>
                     </table>
                 </div>
            </div>
        </form>
    </div>

   <div id="updateSignature" style="height: 300px; width: 60% ;margin-bottom: 2%;border:1px solid #003f81;">
         <form action="createtableAction.do?param=AddExcel"  method="post" style="height: 90%; width: 90% ;" >
            <div>
                    <h1 align="center">Add or Update Signature For Report Schedule</h1>
            </div><br>
            <div  align="center">
                <table style="width:100%;">

                    <tr >
                            <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;width: 17%;">Signature In Html:</td>
                    <td align="left" colspan="1" class="migrate"><textarea id="signature" name="signature" style="width:100%;height: 100px;" placeholder="Ex. <h3>Regards,</h3><h3>Dinanath Parit</h3><div><b>Phone No:</b>9452698012</div>"></textarea></td>
                    <td colspan="2"></td>
                </tr>
                </table><br>
                    <input type="button" class='navtitle-hover' onclick ="CreateSignatureAsHtml()"  value="Update" title="">
                  <div style="border:1px solid lightblue;margin-top:1%;">
                     <table style="width:100%">
                         <tr>
                         <td>
                 <div id="htmlCodeOutput" style="width: 100%;height:90px;overflow-y: auto;">

                 </div>
                 </td>
                         </tr>
                     </table>
                 </div>
            </div>
        </form>
    </div>

          <div id="optionalFooterDiv" style="height: 300px; width: 60% ;margin-bottom: 2%;border:1px solid #003f81;">
         <form action="createtableAction.do?param=AddExcel"  method="post" style="height: 90%; width: 90% ;" >
            <div>
                    <h1 align="center">Add or Update Optional Footer Text For Report Schedule</h1>
            </div><br>
            <div  align="center">
                <table style="width:100%;">

                    <tr >
                            <!--                    <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;width: 35%;">Signature In Html:</td>-->
                    <td align="left" colspan="1" class="migrate"><textarea id="optionalFooter" name="optionalFooter" style="width:100%;height: 100px;" placeholder="Ex.
                                                                           <h3>Something</h3>
                                                                                                                                                 <h3>Write here</h3>
                                                                                                                                                                <div>and see output below</div>"></textarea></td>
                    <td colspan="2"></td>
                </tr>
                </table><br>
                    <input type="button" class='navtitle-hover' onclick ="CreateOptionalFooterAsHtml()"  value="Update" title="">
                  <div style="border:1px solid lightblue;margin-top:1%;">
                     <table style="width:100%">
                         <tr>
                         <td>
                 <div id="htmlCodeOptionFooterOutput" style="width: 100%;height:90px;overflow-y: auto;">

                 </div>
                 </td>
                         </tr>
                     </table>
                 </div>
            </div>
        </form>
    </div>

     <div id="uploadfile" style="height: 280px; width: 60% ;margin-bottom: 2%;border:1px solid #003f81;">
         <form action="createtableAction.do?param=AddTwoFooterPhotoInScheduler"  method="post" style="height: 90%; width: 90% ;" enctype="multipart/form-data">
            <div>
                    <h1 align="center">Upload Footer Logo For Report Schedule</h1>
            </div><br>
            <div  align="center">
                <table style="width:100%">
                    <tr style="width:100%">
                            <th align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;width: 30%;">Left Logo</th>
                            <th align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;">Message</th>
                            <th align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;width:30%;">Right Logo</th>
                     </tr>
                     <tr style="width:100%">
                            <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;"><input id="leftLogoImage" name="leftLogoImage" type="file" multiple="multiple" onchange="readURLLeft(this);"style="background-color:lightgoldenrodyellow; color:black;"/></td>
                            <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;"><input type="text" name="message" id="message" placeholder="Ex. Powered By ProGen Business Solutions" style="width:100%"></td>
                            <td align="left" colspan="1" class="migrate" style="background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;"><input id="rightLogoImage" name="rightLogoImage" type="file" multiple="multiple" onchange="readURLRight(this);" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                     </tr>
                </table><br><br>
                    <input type="submit" class='navtitle-hover' onclick ="return Checkfiles()"  value="Upload" title="">
                 <div style="border:1px solid lightblue;margin-top:1%;">
                     <table style="width:100%">
                         <tr><td style="width:16%;">
                 <img id="imageOutputLeft" src="#" alt="Your Image Output" />
                 </td>
                         <td><center><strong>
                 <span id="messageOutput" style="font-weight: 500;font-size: 23px;"></span></strong></center>
                 </td>
                         <td style="width:16%;">
                 <img id="imageOutputRight" src="#" alt="Your Image Output" />
                 </td></tr>
                     </table>
                 </div>
            </div>
        </form>
    </div>

    </body>
</html>
