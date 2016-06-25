<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.Container,prg.db.Session,com.progen.i18n.TranslaterHelper,java.util.Locale,java.util.ArrayList,java.util.HashMap,java.util.List"%>
<%@page import="prg.db.Container,prg.db.Session,com.progen.report.PbReportRequestParameter,com.progen.report.PbReportCollection,com.progen.reportview.bd.PbReportViewerBD,com.progen.reportview.db.PbReportViewerDAO"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%--
    Document   : ChangeViewBy
    Created on : Jun 14, 2010, 8:27:04 PM
    Author     : Administrator
--%>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String newone="";
%>
<html>
    <%
            try {
                //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                  //ended By Mohit Gupta
                PbReportViewerBD reportViewBD = new PbReportViewerBD();
                PbReportViewerDAO reportViewDAO = new PbReportViewerDAO();
               // ArrayList<String> allViewIds = null;
              //  ArrayList<String> allViewNames = null;
                ArrayList<String> rowViewIdList = null;
                ArrayList<String> colViewIdList = null;
                ArrayList<String> rowNamesLst = null;
                ArrayList<String> colNamesLst = null;

                ArrayList tagReportId1=null;
                ArrayList tagReportName=null;
                ArrayList tagType=null;
                ArrayList tagIdOfAs=null;
                ArrayList tagRName=null;
                ArrayList<String> tagShortD=null;
                ArrayList<String> tagLongD=null;


               // String rowName = "";
               // String colName = "";
                String rowViewListSTR = "";
                String rowViewNamesListSTR = "";
                String colViewListSTR = "";
                String colViewNamesListSTR = "";
                String rowViewByStr = "";
                ArrayList userId=null;
                 ArrayList<String> idOfAssignementTab=null;
              //  String colViewByStr = "";
              /* String imgpathRow = request.getContextPath() + "/icons pinvoke/arrow-curve.png";
                String imgpathCol = request.getContextPath() + "/icons pinvoke/arrow-curve-270.png"; */
                String imgpath = request.getContextPath() + "/icons pinvoke/tick-small.png";
                String delimgpath = request.getContextPath() + "/icons pinvoke/cross-small.png";
                String reportId = request.getParameter("REPORTID");
                String ctxPath = request.getParameter("ctxPath");

                String themeColor="blue";

                if (session.getAttribute("tagReportId") != null) {
                        tagReportId1=(ArrayList)session.getAttribute("tagReportId");
                        
                }
                if (session.getAttribute("tagReportName") != null) {
                        tagReportName=(ArrayList)session.getAttribute("tagReportName");
                        
                }

                if(session.getAttribute("tagIdOfAssignmentTab")!=null){
                        idOfAssignementTab=(ArrayList<String>)session.getAttribute("tagIdOfAssignmentTab");
                        
                }

                String userid="";
                 userid = String.valueOf(request.getSession(false).getAttribute("USERID"));
                 


//added by Dinanath
             ArrayList<Object> arrayOfId = new ArrayList<Object>();
             ArrayList<Object> arrayOfName = new ArrayList<Object>();
                for (int i = 0; i < idOfAssignementTab.size(); i++)
                    {
                    for(int j=0;j<tagReportId1.size();j++)
                        {
                    if((idOfAssignementTab.get(i)).equals(tagReportId1.get(j))){
                            
                            

                   arrayOfId.add(String.valueOf(tagReportId1.get(j)));
                   arrayOfName.add(String.valueOf(tagReportName.get(j)));
                            
                            
                    //tagRName.add();

                    }
                    }
                }
                rowViewByStr = reportViewDAO.buildChangeViewBy2(arrayOfId, arrayOfName, "RowViewBy", ctxPath);



                //if (session.getAttribute("colViewIdList") != null) {
                   // colViewIdList = (ArrayList<String>) session.getAttribute("colViewIdList");
                   // 
                    //colNamesLst = (ArrayList<String>) session.getAttribute("colNamesLst");
                    //colViewByStr = reportViewDAO.buildChangeViewBy(colViewIdList, colNamesLst, "ColViewBy", ctxPath);

               // } else {
                 //   colViewIdList = new ArrayList<String>();
                  //  colNamesLst = new ArrayList<String>();
               // }
                 if(session.getAttribute("theme")==null)
                    session.setAttribute("theme",themeColor);
                 else
                     themeColor=String.valueOf(session.getAttribute("theme"));
                String contexTPath=request.getContextPath();


    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>piEE</title>

      <script src="<%=contexTPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <script type="text/javascript" src="<%=contexTPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>-->
         <link type="text/css" href="<%=contexTPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contexTPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contexTPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contexTPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />


<!--        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>-->

        <script src="<%=contexTPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contexTPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <%--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>--%>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="<%=contexTPath%>/javascript/quicksearch.js"></script>

        <style type="text/css">
            *{font:11px verdana;}
        </style>
       
   <style>
      #shortNameD,#longNameD
{
    background: none repeat scroll 0 0 #FCFCFC;
    border: 1px solid #A6A6A6;
    border-radius: 2px 2px 2px 2px;
    color: #6C6C6C;
    height: 35px;
    margin-top: 10px;
    outline: medium none;
    padding-left: 5px;
    width: 230px;
}
#shortNameD:focus ,#longNameD:focus {
    background: none repeat scroll 0 0 white;
    border-color: #7DC9E2;
    box-shadow: 0 0 2px rgba(0, 0, 0, 0.3) inset;
    color: black;
 content:"";
}
       </style>
      <script>
 $(function() {
  //   alert("dsfdsfdsadsaf input validatetion");
//        var txt = $("input#shortNameD");
//        var func = function() {
//            txt.val(txt.val().replace(/\s/g, ''));
//        }
//        txt.keyup(func).blur(func);
    });
</script>
    </head>
    <body>
        <input type="hidden" id="reportId" name="reportId" value="<%=reportId%>">
        <input type="hidden" id="ctxPath" name="ctxPath" value="<%=ctxPath%>">
        <form name="ViewByForm" id="ViewByForm" method="post" >
            <table style="width:100%;height:248px;border:1px solid #3286e7;border-collapse: collapse;" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1" style="  border-right-color: #3286e7;border-right-style: solid;border-right-width: 1px;">
                        <div style="height:20px;" class="bgcolor" align="center"><font size="2" style="font-weight:bold;color:#369"><%=TranslaterHelper.getTranslatedInLocale("Tag_Name", cL)%></font></div>
                        <div style="height:194px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);">
                            <ul id="ViewByList" class="filetree treeview-famfamfam">
                                <li>
                                    <ul>
                                        <%
                for (int i = 0; i < tagReportId1.size(); i++) {
                                        %>
                                        <li class="closed">

                                               <table><tr>
                                                       <td><img alt=""  src='<%=imgpath%>'/></td>
                                            <td width="100"><span title="" id="<%=tagReportId1.get(i)%>" class="viewBys"><%=tagReportName.get(i)%></span></td>
<%--                                            <td title="<bean:message bundle="Tooltips" key="changeViewBy.rowViewBy"/>">
                                                <img alt=""  src='<%=imgpathRow%>' onclick="createViewBY('<%=tagReportId1.get(i)%>','<%=tagReportName.get(i)%>','rowViewUL')"/>
                                           </td> --%>
<%--                                            <td title="<bean:message bundle="Tooltips" key="changeViewBy.colViewBy"/>"><img src='<%=imgpathCol%>' onclick="createViewBY('<%=tagReportId1.get(i)%>','<%=tagReportName.get(i)%>','colViewUL')"/></td>--%>
                                          </tr></table>
                                        </li>
                                        <%}%>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td width="50%" valign="top" style="border-left-color:#3286e7 ;border-left-style: solid; border-left-width: 1px;">
                        <table width="100%">
                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;"><%=TranslaterHelper.getTranslatedInLocale("Tag_Name", cL)%><font></div>
                                    <div style="height:188px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowViewBy">
                                        <ul id="rowViewUL" class="sortable">
                                           <%=rowViewByStr%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
<!--                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;">Column ViewBys</font></div>
                                    <div  style="height:88px;overflow-y:auto" id="ColViewBy">
                                        <ul id="colViewUL" class="sortable">
                                      <%--      <%=colViewByStr%>  --%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>-->
                        </table>
                    </td>
                </tr>
                 <tr>
                    <td colspan="2" style="height:102px; border-top-color:#3286e7 ;border-top-style: solid; border-top-width: 1px;">
 <div style="width:463px;height:104px;border:0px solid #1663bb;display:block;color:#1355a0;">
      <table style="margin-left:10%;">
          <tr><td>
                  <%=TranslaterHelper.getTranslatedInLocale("Short_Name", cL)%>:</td><td><input type="text" id="shortNameD" name="shortNameDesc"  size="30" maxlength="30" placeholder="Enter here Short Name"  style="width:199px;margin-top:5px;height: 20px;background-color:white;border-color:#2981a3;"><br/></td></tr><tr><td>
                  <%=TranslaterHelper.getTranslatedInLocale("Description", cL)%>: </td><td><textarea rows="3" id="longNameD" cols="60" placeholder="Enter here Description about Reports" name="longNameDesc"  maxlength="255"  style="width:199px;background-color: white;border-color: #2981a3;height:50px;"  ></textarea> <br/></td></tr><tr><td>
      </table>
</div>
                    </td>
                </tr>
            </table>
            <table style="width:100%" align="center">


                <tr>
                    <td colspan="2" align="center">
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="saveViewBy()">

                    </td>
                </tr>
            </table>
        </form>
                         <script type="text/javascript">
            var ViewByArray=new Array();
            <%
            if (arrayOfId != null && arrayOfId.size() != 0) {
                    for (int i = 0; i < arrayOfId.size(); i++) {
                        rowViewListSTR += "," + arrayOfId.get(i);
                        rowViewNamesListSTR += "," + arrayOfName.get(i);
                    }
                    rowViewListSTR = rowViewListSTR.trim().substring(1);
                    
                    rowViewNamesListSTR = rowViewNamesListSTR.trim().substring(1);
                    
            %>
                var rowViewStr = '<%=rowViewListSTR%>';
                var rowNamesStr = '<%=rowViewNamesListSTR%>'
                var RowViewByArray  = rowViewStr.split(",");
                var rowViewNamesArr = rowNamesStr.split(",");
                ViewByArray = rowViewStr.split(",");
            <%
} else {%>
    var RowViewByArray=new Array();
    var rowViewNamesArr=new Array();

 <%}%>

                $(document).ready(function() {

//                    var v=parent.document.getElementById("Designer").value;
//                   // /
//                  if(parent.document.getElementById("Designer").value=="fromDesigner"){
//                        // <//%rowViewByStr="";colViewByStr=""; %>
//                    }
                    $("#ViewByList").treeview({
                        animated:"slow"
//                        persist: "cookie"
                    });
                    $(".sortable").sortable();
                    $(".sortable").disableSelection();
                    $("#Rowdrop").sortable();
                    $('ul#ViewByList li').quicksearch({
                        position: 'before',
                        attached: 'ul#ViewByList',
                        loaderText: '',
                        delay: 100
                    })
                    $(".viewBys").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#RowViewBy").droppable({
                        activeClass:"blueBorder",
                         accept:function(d){return true;},
                         drop: function(ev, ui) {
                             //Code added by mohit for drag and drop in rows and cols
                             //changed by sruthi for drag and drop in rows and rows
                       var parentid=  $("#"+ui.draggable.attr('id')).parent().attr('id');//added by sruthi
                      // alert("parentid"+parentid);
                        if(parentid=="rowViewUL")//added by sruthi
                            {
                            }
                        else
                            {
                          if(ui.draggable.attr('id').substring(0,6)=="ViewBy")
                            {
                           var oldid=ui.draggable.attr('id');
                          // alert("oldid   "+oldid);
                           deleteColumn(ui.draggable.attr('id'),'ColViewBy',ui.draggable.html());
                           var newid=ui.draggable.attr('id').substring(ui.draggable.attr('id').lastIndexOf("y")+1);
                         //  alert("newid   "+newid);
                           var str=ui.draggable.html().substring(0,ui.draggable.html().lastIndexOf("</td>"));
                           var name=str.substring(str.lastIndexOf(">")+1);
                          // alert("name "+name);
                           createViewBY(newid,name,"rowViewUL");
                                }
                                else
                                    {

                                        createViewBY(ui.draggable.attr('id'),ui.draggable.html(),"rowViewUL");
                                    }
                            }}
                    });

                     From=parent.document.getElementById("Designer").value;
                    // alert("From "+From);
                    if(From=="fromDesigner"){
                        $("#ChangeViewbyButton").val("Next")
                        //alert("automatic assigment"); //added by mohit for create report
                        //<//%rowViewByStr="";colViewByStr=""; %>
                        createViewBY('<%=tagReportId1.get(0)%>','<%=tagReportName.get(0)%>','rowViewUL')
                        saveViewBy()

                    }
                });
                function saveViewBy(){
              var k=$("#shortNameD").val()
              $myList = $('#rowViewUL')
              //alert($("#shortNameD").val())

                   if($("#shortNameD").val()=="" || $("#shortNameD").val()==null ||$("#shortNameD").val()==" ")
                  {
                      alert("Please Specify the Short name")
                      return false;
                  }
                 else if($("#longNameD").val()=="" || $("#longNameD").val()==null || $("#shortNameD").val()==" ")
                  {
                      alert("Please Specify the Description")
                      return false;
                  }
                  else if ( $myList.children().length === 0 )
                      {
                          alert("Please select and drop atleast one tag name")
                          return false;
                      }
                  else{
              var From="";
                if(parent.document.getElementById("Designer") != null)
                From=parent.document.getElementById("Designer").value;

                    var reportId  = document.getElementById("reportId").value;//not array
                    //alert("reportId"+reportId);
                    var ctxpath = document.getElementById("ctxPath").value;//not array
                   // alert("ctxpath"+ctxpath);
                   var shortDesc = document.getElementById("shortNameD").value;//not array
                  // alert("shortDesc"+shortDesc);
                   var longDesc= document.getElementById("longNameD").value;//not array
                   //alert("longDesc"+longDesc);
                    var ttype='R';//not array
                  // alert("tgType"+ttype);
                    var uid='<%=userid%>';//not array
                   //alert("userId="+uid);
                    var count=0;
                    var viewByArray=new Array();
                    var tagIds=new Array();
                    var colViewByArray=new Array();

                     var ul = document.getElementById("rowViewUL");
                    if(ul!=undefined || ul!=null)
                    {
                    var colIds=ul.getElementsByTagName("li");
//                    alert("colIds "+colIds);
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            tagIds.push(colIds[i].id.replace("ViewBy",""));//array
                            //alert("Tag Ids array:"+tagIds[i]);
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));

                        }
                    }
                   // alert("tagIds which is passed to....."+tagIds);
//alert("before ajax call for saving the data");
  // $.post(ctxpath+'/reportViewer.do?reportBy=showViewByTag&REPORTID='+reportId+'&ctxPath='+ctxpath, $("ViewByForm").serialize() ,
   //$.post(ctxpath+'/reportViewer.do?reportBy=saveDescInAssignment&rId='+reportId+'&ctxPath='+ctxpath+'&taggId='+tagIds+'&taggName='+<%=tagReportName%>+'&shorttDesc='+shortDesc+'&longgDesc='+longDesc+'&taggType='+<%=tagType%>+'&userrIds='+<%=userId%>, $("ViewByForm").serialize() ,
    $.post(ctxpath+'/reportViewer.do?reportBy=saveDescInAssignment&rId='+reportId+'&ctxPath='+ctxpath+'&taggId='+tagIds+'&uid='+uid+'&tagType='+ttype+'&shortDesc='+shortDesc+'&longDesc='+longDesc, $("ViewByForm").serialize() ,
         function(data){
            // alert("dataaaaaaaa"+data)
              submitChangeViewBy();
              });

 //                     $.ajax({
 <%--                         url: ctxpath+"/reportViewer.do?reportBy=saveDescInAssignment&tagId="+tagIds+"&tagName="+<%=tagReportName%>+"&shortDesc="+shortDesc+"&longDesc="+longDesc+"&ctxpath="+ctxpath+"&tagType="+<%=tagType%>+"&usersId="+<%=userId%>+"&reportId="+reportId, --%>
 //                           success: function(data){
 //                               alert("data for insert");
//                                submitChangeViewBy();
  //                          }
  //                      });
//alert("after ajax call for saving the data");

                }
                         for(var i=0;i<tagIds.length;i++){
                             for(var j=i+1;j<tagIds.length;j++){
                                 if(tagIds[i]==tagIds[j])
                                     count =count+1;
                             }
                         }

//                    var colviewByUl = document.getElementById("colViewUL");
//                    if(colviewByUl!=undefined || colviewByUl!=null){
//                    var colIds=colviewByUl.getElementsByTagName("li");
//                    if(colIds!=null && colIds.length!=0){
//                        for(var i=0;i<colIds.length;i++){
//                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            colViewByArray.push(colIds[i].id.replace("ViewBy",""));
//                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
//                        }
//
//                    }
//                }
//                         for(var i=0;i<colViewByArray.length;i++){
//                            for(var j=0;j<tagIds.length;j++){
//                                 if(colViewByArray[i]==tagIds[j])
//                                     count =count+1;
//                             }
//                         }
                    if(colViewByArray.length == 0 && tagIds.length == 0){
                          alert("Please Select Atleast one Row Viewby")
                    }else if(count>0)
                    {
                        alert("Please Select different Row Viewby and Col Viewby")
                    }
                    else if(tagIds.length>0 )
                    {
                    if(From!="fromDesigner")
                    {
                        $("#saveRTD").dialog('close')
                        parent.$("#saveRTD").dialog('close')
                        parent.document.getElementById('loading').style.display='';
                       // alert("ssssssssssssssssssssssssssssssssssssssssssssssssss");
                    }
//                        $.ajax({
//                            url: ctxpath+"/reportViewer.do?reportBy=changeViewBy&ViewByArray="+ViewByArray+"&RowViewByArray="+RowViewByArray+"&ColViewByArray="+ColViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr,
//                            success: function(data){
//                                submitChangeViewBy();
//                            }
//                        });

//                            if(From=="fromDesigner"){
//                                $.post(ctxpath+"/reportTemplateAction.do?templateParam=designerViewbys&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr, $("#ViewByForm").serialize(),
//                             function(data){//
//                               parent.$("#editViewByDiv").dialog('close');
//                               parent.$("#paramDesign").hide();
//                    var prevREPIds=parent.document.getElementById("REPIds").value
////                    alert(prevREPIds)
//                    var frameObj=parent.document.getElementById("dataDispmem");
//                    var prevCEPIds=parent.document.getElementById("CEPIds").value
//                    var roleid=parent.document.getElementById("roleid").value
//                    var RepIdsArray = new Array()
//                    var CepIdsArray = new Array()
//                    RepIdsArray = prevREPIds.split(",")
//                    CepIdsArray = prevCEPIds.split(",")
//                    parent.document.getElementById("REPIds").value = rowViewByArray;
//                    parent.document.getElementById("CEPIds").value = colViewByArray;
//                    var flag = 0;
//                    for(var i=0;i<rowViewByArray.length;i++){
//                        for(var j=0;j<colViewByArray.length;j++){
//                            if(RepIdsArray[i] == CepIdsArray[j]){
//                                flag=flag+1;
//                            }
//                        }
//                    }
////                    alert(rowViewByArray+"=======rowViewByArray")
//                    if(rowViewByArray!=""){
//                        if(flag==0){
//                            //alert("mohit");
//                            parent.$("#measuresDialog").dialog('open');
//                            var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleid+'&REPORTID='+parent.document.getElementById("REPORTID").value;
//                            //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
//                            frameObj.src=source;
//                        }else{
//                            alert("Please select different Row Edge and Col Edge")
//                        }
//
//                    }
//                    else{
//                        if(rowViewByArray=="" || prevREPIds==undefined ){
//                            alert("Please select Row Edge ")
//                        }
//                    }
//
//                        });
//                            }else{
////                         $.post(ctxpath+"/reportViewer.do?reportBy=changeViewBy&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr, $("#ViewByForm").serialize(),
////                             function(data){
//                                submitChangeViewBy();
////                        });
//                    }
                }}
                }

                function submitChangeViewBy(){
               // alert("data is submitted");
                 parent.document.getElementById('loading').style.display='none';
                    var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
//                    parent.document.forms.frmParameter.action = ctxpath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&REPORTID="+reportId
//                    parent.document.forms.frmParameter.submit();
//                    parent.$("#editViewByDiv").dialog('close');
                    parent.$("#saveRTD").dialog('close');
                }

                function createViewBY(elmntId,elementName,tarLoc){
                    var parentUL=document.getElementById(tarLoc);
                    var x=ViewByArray.toString();
                 //   alert("x "+x);
                    if(tarLoc == "colViewUL"){
//                        if(colFlag == 0){
//                          if(x.match(elmntId)==null){
//                              alert("x.match(elmdtid) "+x.match(elmntId));
//                                ViewByArray.push(elmntId);
//                                ColViewByArray.push(elmntId);
//                                colViewNamesArr.push(elementName);
//                                var childLI=document.createElement("li");
//                                childLI.id='ViewBy'+elmntId;
//                                childLI.style.width='auto';
//                                childLI.style.height='auto';
//                                childLI.style.color='white';
//                                var table=document.createElement("table");
//                                table.id="viewTab"+elmntId;
//                                var row=table.insertRow(0);
//                                var cell1=row.insertCell(0);
//                                cell1.style.cursor = "pointer";
//                                cell1.onclick =function(){javascript:deleteColumn('ViewBy'+elmntId,'ColViewBy',elementName)};
//                                var img=document.createElement("img");
// <%--                               img.src = "<%=delimgpath%>"  --%>
//                                cell1.appendChild(img);
//                                var cell2=row.insertCell(1);
//                                cell2.style.color='black';
//                                cell2.innerHTML=elementName;
//                                childLI.appendChild(table);
//                                parentUL.appendChild(childLI);
                                colFlag = 1;
//                            }
//                        }else{
//                            alert("You Can Select One Column ViewBy Only")
//                        }
                    }else if(tarLoc == "rowViewUL"){
                      if(x.match(elmntId)==null){
                            ViewByArray.push(elmntId);
                            RowViewByArray.push(elmntId);
                            rowViewNamesArr.push(elementName);
                            var childLI=document.createElement("li");
                            childLI.id='ViewBy'+elmntId;
                            childLI.style.width='auto';
                            childLI.style.height='auto';
                            childLI.style.color='white';
                            var table=document.createElement("table");
                            table.id="viewTab"+elmntId;
                            var row=table.insertRow(0);
                            var cell1=row.insertCell(0);
                            cell1.style.cursor = "pointer";
                            cell1.onclick =function(){javascript:deleteColumn('ViewBy'+elmntId,'RowViewBy',elementName)};
                            var img=document.createElement("img");
                            img.src = "<%=delimgpath%>"
                            cell1.appendChild(img);
                            var cell2=row.insertCell(1);
                            cell2.style.color='black';
                            cell2.innerHTML=elementName;
                            childLI.appendChild(table);
                            parentUL.appendChild(childLI);
                        }
                    }
                    $(".sortable").sortable();
                    $(".sortable").disableSelection();
                }
                function deleteColumn(index,dropLoc,name){
                    var LiObj=document.getElementById(index);
                    //alert("LiObj"+LiObj);
                    var parentUL=document.getElementById(LiObj.parentNode.id);
                    //alert("parentUL"+parentUL);
                    parentUL.removeChild(LiObj);;
                    var x=index.replace("ViewBy","");
                    var i=0;
                    for(i=0;i<ViewByArray.length;i++){
                        if(ViewByArray[i]==x)
                            ViewByArray.splice(i,1);
                    }
                    if(dropLoc == 'RowViewBy'){
                        for(i=0;i<RowViewByArray.length;i++){
                            if(RowViewByArray[i]==x)
                                RowViewByArray.splice(i,1);
                        }
                        for(i=0;i<rowViewamesArr.length;i++){
                            if(rowViewNamesArr[i]==name)
                                rowViewNamesArr.splice(i,1);
                        }
                    }else if(dropLoc == 'ColViewBy'){
//                        for(i=0;i<ColViewByArray.length;i++){
//                            if(ColViewByArray[i]==x)
//                                ColViewByArray.splice(i,1);
//                        }
//                        for(i=0;i<colViewNamesArr.length;i++){
//                            if(colViewNamesArr[i]==name)
//                                colViewNamesArr.splice(i,1);
//                        }
                        colFlag = 0;
                    }
                }
        </script>
    </body>
    <%
            } catch (Exception e) {
                e.printStackTrace();
            }
    %>
</html>
