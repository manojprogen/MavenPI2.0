<%-- 
    Document   : sentimentAnalysis
    Created on : 6 Jan, 2011, 2:26:35 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%
    String themeColor="";
        if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
    String conetcxPath=request.getContextPath();
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=conetcxPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=conetcxPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
         <link type="text/css" href="<%=conetcxPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=conetcxPath%>/javascript/pi.js"></script>
         <script type="text/javascript" src="<%=conetcxPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=conetcxPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=conetcxPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=conetcxPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=conetcxPath%>/tablesorter/docs/js/docs.js"></script>
         <link rel="stylesheet" href="<%=conetcxPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <script src="<%=conetcxPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=conetcxPath%>/stylesheets/treeviewstyle/screen.css" />
        <link href="<%=conetcxPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
            <link rel="stylesheet" href="<%=conetcxPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
       
    </head>
   
    <body>
        <form name="uploadTextForm" id="uploadTextForm" enctype="multipart/form-data" method="post" action="" style="height: 55%"  >
            <table>
                <tr>
                    <td> <input type="button" class="navtitle-hover" value="Analyze" onclick="viewAnalyzer('<%=request.getContextPath()%>')"></td>
                    <td><input type="button" class="navtitle-hover" value="Report" onclick="openAnalyzedReport()"></td>
                    <td><input type="button" class="navtitle-hover" value="Setup" onclick="openSentiment()"></td>
                    <td><input type="button" id="apollo" class="navtitle-hover" value="Analyze Apollo" onclick="analyseData(this.id)"></td>
                    <td><input type="button" id="twitter" class="navtitle-hover" value="Analyze Twitter" onclick="analyseData(this.id)"></td>
                    <td><input type="button" id="twitterclassify" class="navtitle-hover" value="Re Classify Data" onclick="reClassifyData(this.id)"></td>
                    <td><input type="button" class="navtitle-hover" value="View/Edit Parameters" onclick="openViewParams()"></td>
                </tr> </table>
                     <table>
                <tr  id="uploadTextFile"   style="display:none">
                    <td class="wordStyle"valign="top" style="width: auto;" > Browse to upload a text file</td>
                    <td> <input type="file" name="textFile" size="27" id="textFile"></td>
                    <td>
                        <input type="submit"  class="navtitle-hover" value="Upload" onclick="uploadTextFile('<%=request.getContextPath()%>')">
                    </td>
                </tr> </table>
           
                      <div id="sentimentId" style="display: none">
                    <table align="center">
                        <tr>
                            <td><h5>Subject</h5></td>
                            <td ><textarea id="subject" style="height:40px" cols="20" rows="10"  ></textarea></td>
                        </tr>
                        <tr>
                            <td><h5>Parameters</h5></td>
                            <td><textarea id="parameter" style="height:40px" cols="20" rows="10" /></td>
                        </tr>

                    </table>
                          <table align="center">
                              <tr>
                                  <td width="70"></td>
                                  <td>    <input type="button" class="navtitle-hover" value="Save" onclick="saveToTable()"></td>
                                  <td>    <input type="button" class="navtitle-hover" value="Add Buzz" onclick="openBuzzWordDialog()"></td>
                                  <td>    <input type="button" class="navtitle-hover" value="Done" onclick="closeSentimentDialog()"></td>
                              </tr></table>
                      </div>
                    <div id="buzzWordDialog" style="display:none">
                        <table align="center" >
                            <tr>
                                <td><h5>Parameter:</h5></td>
                                <td><select id="paramsComponent" onchange="getBuzzWords()"></select></td>
                            </tr>
                            <tr>
                                <td><h5>Positive:</h5></td>
                                <td><textarea cols="" rows=""  id="positive"/></td>
                            </tr>
                            <tr>
                                <td><h5>Negative:</h5></td>
                                <td><textarea cols="" rows=""  id="negative"/></td>
                            </tr>
                            <tr>
                                <td><h5>Neutral</h5></td>
                                <td><textarea cols="" rows=""  id="neutral"/></td>
                            </tr>
                        </table>
                         <table align="center">
                              <tr>
                                  <td>    <input type="button" class="navtitle-hover" value="Save" onclick="saveBuzzwords()"></td>
                                  <td>    <input type="button" class="navtitle-hover" value="Done" onclick="closeBuzzDialog()"></td>
                              </tr>
                         </table>
                    </div>
                    <div id="viewParameters" style="display: none">
                        <table   id="sentimentTable" class="tablesorter" >
                            <thead>
                            <th >Parameter</th>
                            <th>Hierarchy</th>
                            <th>Keywords</th>
                            <th>Positive Buzzwords</th>
                            <th>Negative Buzzwords</th>
                            <th>Other Buzzwords</th>
                            </thead>
                            <tbody id="tableBody">

                            </tbody>
                        </table>
                        <div id="sentimentPager" align="left" >
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="all" value="">All</option>

                            </select>
                        </div>
                    </div>
                    <div id="editDialog"  style="display:none">
                         <table align="center">
                             <tr>
                                <td><h5>ParameterId:</h5></td>
                                <td><textarea cols="" rows=""  id="paramId" readonly/></td>
                            </tr>
                            <tr>
                                <td><h5>Parameter:</h5></td>
                                <td><textarea cols="" rows=""  id="param"/></td>
                            </tr>
                             <tr>
                                <td><h5>Keywords</h5></td>
                                <td><textarea cols="" rows=""  id="keyword"/></td>
                            </tr>
                            <tr>
                                <td><h5>Positive:</h5></td>
                                <td><textarea cols="" rows=""  id="positiveBuzz"/></td>
                            </tr>
                            <tr>
                                <td><h5>Negative:</h5></td>
                                <td><textarea cols="" rows=""  id="negativeBuzz"/></td>
                            </tr>
                            <tr>
                                <td><h5>Neutral</h5></td>
                                <td><textarea cols="" rows=""  id="neutralBuzz"/></td>
                            </tr>
                        </table><br>
                        <table align="center">
                            <tr>  <td>    <input type="button" class="navtitle-hover" value="Save" onclick="saveChanges()"></td></tr>
                        </table>
                    </div>

        </form>
                              <script type="text/javascript">
             $(document).ready(function(){
                  $("#sentimentId").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        title:'Save Dialog'

                    });

                   $("#buzzWordDialog").dialog({
                            autoOpen: false,
                            height: 220,
                            width: 500,
                            position: 'justify',
                            modal: true,
                            title:'BuzzWord Dialog'

                        });
                   $("#viewParameters").dialog({
                            autoOpen: false,
                            height: 600,
                            width: 1000,
                            position: 'justify',
                            modal: true,
                            title:'View/Edit Parameters'

                        });
                   $("#editDialog").dialog({
                            autoOpen: false,
                            height: 300,
                            width: 300,
                            position: 'justify',
                            modal: true,
                            title:'Edit Data'

                        });
                       
             });

             function openSentiment(){
                 $("#sentimentId").dialog('open');
                 var subjectText="";
                 var parameterText="";
                
                 $.ajax({
                     url:"sentimentAction.do?doAction=getSubjectsAndParameters",
                     success: function(data){
                         var subsAndParams=data.split("||");
                         
                         var jsonSubject=eval('('+subsAndParams[0]+')');
                         var jsonParam=eval('('+subsAndParams[1]+')');
                         
                         for(var i=0;i<jsonSubject.subjectKeys.length;i++)
                             {
                                 subjectText=subjectText+jsonSubject.subjectKeys[i];
                                 if(i!=jsonSubject.subjectKeys.length-1)
                                     subjectText=subjectText+";";
                                     
                             }
                             for(var j=0;j<jsonParam.length;j++)
                                 {
                                     parameterText=parameterText+jsonParam[j].parameter;
                                     if(j!=jsonParam.length-1)
                                         parameterText=parameterText+";";
                                 }
                        
                         $("#subject").val(subjectText);
                         $("#parameter").val(parameterText);
                         
                         
                     }
                 });
                 
             }
             function saveToTable(){
                 var subject=$("#subject").val();
                 var parameter=$("#parameter").val();
                 if(subject==""&&parameter=="")
                     alert("please enter values");
                 else{
                  $.ajax({
                    url: "sentimentAction.do?doAction=saveSubjectsAndParameters&subject="+subject+"&parameter="+parameter,
                    success: function(data){
                        
                    }
                    });

                  alert("data saved to database");
                
                 
                 }
                   
             }

             function openBuzzWordDialog(){
                     $("#buzzWordDialog").dialog('open');

                       $.ajax({
                            url:'sentimentAction.do?doAction=getParameters',
                            success: function(data){
                               
                                var json=eval('('+data+')');
                                var options="";
                                options=options+"<option  value='Select'>Select</option>";
                                for(var i=0;i<json.length;i++)
                                    {
                                        options=options+"<option  value='"+json[i].parameterId+"'>"+json[i].parameter+"</option>";
                                    }
                                    $("#paramsComponent").html(options);

                            }

                        });

             }

             function saveBuzzwords(){
             var parameterId=$("select#paramsComponent").val();
             var parameter=$("#paramsComponent option:selected").text();
             var buzzPositive= $("#positive").val();
             var buzzNegative=$("#negative").val();
             var buzzNeutral=$("#neutral").val();
           
                  $.ajax({
                      url:'sentimentAction.do?doAction=saveBuzzwords&buzzPositive='+buzzPositive+'&buzzNegative='+buzzNegative+'&buzzNeutral='+buzzNeutral+'&paramId='+parameterId+'&parameter='+parameter,
                      success: function(data){

                      }

                  });
                 
             }

             function getBuzzWords()
             {
                 var parameterId=$("select#paramsComponent").val();
                 var positiveText="";
                 var negativeText="";
                 var neutralText="";
                  $.ajax({
                      url:'sentimentAction.do?doAction=getParamBuzzwords&paramId='+parameterId,
                      success: function(data){
                          

                                  var json=eval('('+data+')');
                                 
                                for(var i=0;i<json.Positive.length;i++)
                                    {
                                        positiveText=positiveText+json.Positive[i];
                                        if(i!=json.Positive.length-1)
                                            positiveText=positiveText+";";
                                    }
                                 for(var i=0;i<json.Negative.length;i++)
                                    {
                                        negativeText=negativeText+json.Negative[i];
                                        if(i!=json.Negative.length-1)
                                            negativeText=negativeText+";";
                                    }
                                     for(var i=0;i<json.Neutral.length;i++)
                                    {
                                        neutralText=neutralText+json.Neutral[i];
                                        if(i!=json.Neutral.length-1)
                                            neutralText=neutralText+";";
                                    }
                                    $("#positive").val(positiveText);
                                    $("#negative").val(negativeText);
                                    $("#neutral").val(neutralText);
                      }

                  });
             }

       function closeSentimentDialog()
       {
             $("#subject").val("");
           $("#parameter").val("");
           $("#sentimentId").dialog('close');
       }
       function closeBuzzDialog()
       {
           $("#positive").val("");
           $("#negative").val("");
           $("#neutral").val("");
           $("#buzzWordDialog").dialog('close');
       }

       function analyseData(id)
       {
           $.ajax({
                      url:'sentimentAction.do?doAction=analyzeData&from='+id,
                      success: function(data){
                          data=data.toString();
                          if(data=="true")
                             alert("analysed successfully")
                           else
                             alert("insertion failed")
                      }
           });
       }

       function reClassifyData(id)
       {
//           $.ajax({
//                      url:'sentimentAction.do?doAction=reclassifyData',
//                      success: function(data){
//
//                      }
//           });
            document.forms.uploadTextForm.action = "<%=request.getContextPath()%>/sentimentAction.do?doAction=reclassifyData";
            document.forms.uploadTextForm.submit();
        }

    function openViewParams()
       {
           var paramIdText="";
           var paramText="";
           var positiveText;
           var negativeText;
           var neutralText;
           var keywordText;
           $.ajax({
               url:'sentimentAction.do?doAction=getParametersAndBuzzwords',
               success:function(data){
                   var paramKeywordjson=data.split("|");
                   var json=eval('('+paramKeywordjson[0]+')');
                   var keyword=eval('('+paramKeywordjson[1]+')');
                   var tableData="";
                   for(var i=0;i<json.length;i++)
                       {
                           positiveText="";
                           negativeText="";
                           neutralText="";
                           tableData=tableData+"<tr><td>"+json[i].parameter+"</td>";
                           paramIdText=json[i].parameterId;
                           paramText=json[i].parameter;
                           tableData=tableData+"<td></td>";
                           tableData=tableData+"<td>";
                           keywordText="";
                           for(var k=0;k<keyword.length;k++)
                               {
                                   if(json[i].parameterId==keyword[k].paramId)
                                   {
                                       tableData=tableData+keyword[k].keywords;
                                       keywordText=keywordText+keyword[k].keywords;
                                   }
                               }
                           tableData=tableData+"</td><td>";
                           for(var j=0;j<json[i].positive.buzzWords.length;j++)
                           {
                               tableData=tableData+json[i].positive.buzzWords[j].buzzWord;
                               positiveText=positiveText+json[i].positive.buzzWords[j].buzzWord;
                               if(j!=json[i].positive.buzzWords.length-1)
                               {
                                tableData=tableData+";";
                                positiveText=positiveText+";";
                               }
                           }
                           tableData=tableData+"</td>";
                           tableData=tableData+"<td>";
                           for(var j=0;j<json[i].negative.buzzWords.length;j++)
                           {
                               tableData=tableData+json[i].negative.buzzWords[j].buzzWord;
                               negativeText=negativeText+json[i].negative.buzzWords[j].buzzWord;
                               if(j!=json[i].negative.buzzWords.length-1)
                               {
                                 tableData=tableData+";";
                                 negativeText=negativeText+";";
                               }
                           }
                           tableData=tableData+"</td>";
                           tableData=tableData+"<td>";
                           for(var j=0;j<json[i].neutral.buzzWords.length;j++)
                           {
                               tableData=tableData+json[i].neutral.buzzWords[j].buzzWord;
                               neutralText=neutralText+json[i].neutral.buzzWords[j].buzzWord;
                               if(j!=json[i].neutral.buzzWords.length-1)
                               {
                                 tableData=tableData+";";
                                 neutralText=neutralText+";";
                               }
                           }
                           tableData=tableData+"</td>";
                           tableData=tableData+"<td><a onclick='editData("+paramIdText+",\""+paramText+"\",\""+positiveText+"\",\""+negativeText+"\",\""+neutralText+"\",\""+keywordText+"\")'>Edit</a></td></tr>";
                       }
                       $("#all").val(json.length);
                       $("#tableBody").html(tableData);
                       $("#sentimentTable").tablesorter({headers : {0:{sorter:false}}})
                       $("#sentimentTable").tablesorterPager({container: $('#sentimentPager')})
               }
           });
           $("#viewParameters").dialog('open');
          
       }
       function editData(id,param,posTxt,negTxt,otherTxt,keywordTxt)
       {
           
           $("#paramId").val(id);
           $("#param").val(param);
           $("#positiveBuzz").val(posTxt);
           $("#negativeBuzz").val(negTxt);
            $("#neutralBuzz").val(otherTxt);
           // if(keywordTxt!=undefined||keywordTxt!=null);
            $("#keyword").val(keywordTxt);
          $("#editDialog").dialog('open');
       }
       function saveChanges()
       {
           var paramId=$("#paramId").val();
           var parameter=$("#param").val();
           var posTxt=$("#positiveBuzz").val();
           var negTxt=$("#negativeBuzz").val();
           var neutralTxt=$("#neutralBuzz").val();
           var keywordTxt=$("#keyword").val();

              $.ajax({
               url:'sentimentAction.do?doAction=saveEditedChanges&parameterId='+paramId+'&parameter='+parameter+'&positiveTxt='+posTxt+'&negativeTxt='+negTxt+'&neutralTxt='+neutralTxt+'&keyword='+keywordTxt,
               success:function(data){
               }
              });
               $("#editDialog").dialog('close');
       }
         </script>
    </body>
</html>
