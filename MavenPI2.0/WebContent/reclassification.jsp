<%-- 
    Document   : reclassification
    Created on : Feb 16, 2011, 7:00:41 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            String themeColor = "blue";
           if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String classifiedData=(String)request.getAttribute("classifiedData");
             String userId="";
          userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
          String itemList=null;
          if (request.getAttribute("itemList") != null) {
                        itemList = (String) request.getAttribute("itemList");
                    }
          String conetxtPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <script type="text/javascript" src="<%=conetxtPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
         <script type="text/javascript" src="<%=conetxtPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <link type="text/css" href="<%=conetxtPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=conetxtPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=conetxtPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <script src="<%=conetxtPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=conetxtPath%>/stylesheets/treeviewstyle/screen.css" />
        <link href="<%=conetxtPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=conetxtPath%>/javascript/jquery.contextMenu.js" ></script>
        <script type="text/javascript" src="<%=conetxtPath%>/javascript/quicksearch.js"></script>
         <script type="text/javascript" src="<%=conetxtPath%>/javascript/pi.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/docs/js/docs.js"></script>

         <script type="text/javascript" src="<%=conetxtPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link type="text/css" href="<%=conetxtPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
       

      <style type="text/css">
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }

            img{cursor:pointer}
            #hyperRep:hover{text-decoration:underline;}

            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
        </style>
       
    </head>
    <body>
         <table style="width:100%">
                <tr>
                    <td valign="top" style="width:50%;">
                        <jsp:include page="/Headerfolder/headerPage.jsp"/>
                    </td>
                </tr>
         </table>
            <div class="navtitle-hover" style=" max-width: 100%; cursor: auto; height: 20px;"align="left">
                    <span> <font size="2" style="font-weight: bolder">Reclassification</font><b> </b></span>
            </div>
                  <table width="100%">
                    <tr>
                     <td width="25%">
                       <div id="pagerClassify" align="left" >
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allReps" value="">All</option>

                            </select>
                        </div>
                    </td>
                    <td align="left" >
                        <select id="searchType" onchange="changeSearchType()">
                            <option>select</option>
                            <option value="ATTRIBUTE_CHAR5">UHID</option>
                            <option value="ATTRIBUTE_CHAR2">Sentence</option>
                            
                       </select>
                    <input type="text" class="myTextbox3" id="srchTextReport" name="srchTextReport" align="middle" style="width:480px;height:20px;">
                    
                    <input type="button" value="Search" onclick="displayStudioItem('srchTextReport','<%=userId%>','sentiment','classifyTable','<%=request.getContextPath()%>')" style="width:50px;height:20px;" class="navtitle-hover"/>
                    </td>
                    <td align="right">
                                <input type="button" value="Back" align="right" onclick="goBack()" class="navtitle-hover">
                    </td>
                    </table>
             <table align="center" id="classifyTable" class="tablesorter" width="98%" border="0px solid" cellpadding="0" cellspacing="1">

            </table>
            <div id="reClassifyDiv" style="display: none;">
                <form name="ratingForm" id="ratingForm">
                <table id="classifySentenceTable">
                    <tr>
                        <td align="center" class="myhead">Sentence</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <textarea cols="100" rows="6" id="sentenceTxtArea" style="width: 100%; height: 30px;"></textarea>
                        </td>
                    </tr>
                </table>
                <table id="userInfoTable" align="center">
<!--                    <tr>
                        <td class="myhead">
                          Twitter Id
                        </td>
                        <td id="twitterIdTD">
                        </td>
                   </tr>
                   <tr>
                        <td class="myhead">
                          Login Id
                        </td>
                        <td id="twitterLoginTD">

                        </td>
                   </tr>-->
                   <tr>
                        <td class="myhead">
                          UHID
                        </td>
                        <td id="twitterUserTD">

                        </td>
                   </tr>
                   <tr>
                        <td class="myhead">
                          Name
                        </td>
                        <td id="twitterLocTD">

                        </td>
                    </tr>
                </table>
                <br>
                <table align="center">
                <tr>
                     <td align="center" colspan="1" style="width:10%"  class="myhead">Default Rating</td>
                </tr>
                </table>
                <table id="ratingTable" align="center">

                </table>
                <br>
                <table align="center" width="100%">
                    <tr>
                        <td colspan="2" align="center" style="width:100%" class="myhead">Reclassify</td>
                    </tr>
                </table>
                <table id="reclassifyRatedTable" align="center">
                    <tr id="row0">
                        <td>Parameter: <input type="text" value="" id="paramId0"  name="paramNames"></td>
                        <td><input type="radio" value="positive" name="ratingRadio0"> Positive </td>
                        <td><input type="radio" value="negative" name="ratingRadio0"> Negative </td>
                        <td><input type="radio" value="other" name="ratingRadio0" checked=""> Other </td>
                    </tr>
                </table>
                <br><br>
                <table align="center">
                    <tr>
                        <td align="center">
                            <input type="button" class="navtitle-hover" onclick="addRow()" value="Add Rating">
             
                            <input type="button" class="navtitle-hover" onclick="deleteRow()" value="Delete Rating">
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan='2'>
                            <input type="button" class="navtitle-hover" onclick="updateRatingForSentence()" value="Submit">
                        </td>
                    </tr>
                </table>

                 <input type="hidden" id="sentenceId" name="sentenceId" value="">
                <input type="hidden" id="ratingRows" name="ratingRows"  value="1">
                </form>
            </div>
                      <script type="text/javascript">
             var searchType="";
             $(document).ready(function()
             {
//                var data='<%=classifiedData%>';
//                var json=eval("("+data+")") ;
//                var html="";
//                html="<thead><tr><th nowrap>SentenceId</th><th nowrap>Sentence</th><th nowrap>Reclassify</th></tr></thead><tbody>";
//                for(var i=0;i<json.SentenceIds.length;i++)
//                    {
//                        html+="<tr><td>"+json.SentenceIds[i]+"</td>"
//                        html+="<td>"+json.Sentence[i]+"</td>"
//                        html+="<td align='center'><img onclick=\"reClassify('"+json.SentenceIds[i]+"','"+json.Sentence[i]+"')\" src='<%=request.getContextPath()%>/icons pinvoke/globe.png'  /></td></tr>"
//                    }
//                 html+="</tbody>";
//
//                 $("#classifyTable").html(html);
//                 $("#allReps").val(json.SentenceIds.length);
//                 $("#classifyTable").tablesorter({headers : {0:{sorter:false}}})
//
//                 $("#classifyTable").tablesorterPager({container: $('#pagerClassify')})
//
                 $("#reClassifyDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 550,
                    width: 850,
                    modal: true,
                    Cancel: function() {
                        // refreshPage();
                        $(this).dialog('close');
                    }
                 });

                  var data='<%=itemList%>'
               var length= bulidTable(data,'classifyTable','sentiment','<%=request.getContextPath()%>')
               $("#allReps").val(length);
                  
                
             });
             function reClassify(id,sentence)
             {
                    $.ajax({
                      url:'sentimentAction.do?doAction=reclassifySentence&sentenceId='+id,
                      success: function(data){
                          //alert(data)
                          var json=eval("("+data+")")
                          var html=""
                          for(var i=0;i<json.Parameters.length;i++)
                          {
                               html+="<tr><td>Parameter: <input type='text'  readonly id='paramId"+i+"' value='"+json.Parameters[i]+"' ></td>"
                               if(json.Positives[i]=="1")
                                 html+="<td><input type='radio' checked  value='positive'> Positive </td>"
                                 else
                                 html+="<td><input type='radio'  value='positive'> Positive </td>"
                               if(json.Negatives[i]=="1")
                                 html+="<td><input type='radio' checked  value='negative'> Negative </td>"
                                 else
                                 html+="<td><input type='radio'  value='negative'> Negative </td>"
                              if(json.Others[i]=="1")
                               html+="<td><input type='radio' checked  value='other'> Other </td></tr>"
                                 else
                               html+="<td><input type='radio'  value='other'> Other </td></tr>"
                          }
                          $("#sentenceTxtArea").val(sentence);
                          $("#twitterIdTD").html(json.UserDetails[0]);
                          $("#twitterLoginTD").html(json.UserDetails[1]);
                          $("#twitterUserTD").html(json.UserDetails[2]);
                          $("#twitterLocTD").html(json.UserDetails[3]);
                          $("#ratingTable").html(html);
                          $("#sentenceId").val(id);
//                          $("#ratingRows").val(json.Parameters.length);
                          $("#reClassifyDiv").dialog('open')
                       }
                    });
                   
             }
             function updateRatingForSentence()
             {

                $.post("sentimentAction.do?doAction=updateRatingForSentence", $("#ratingForm").serialize() ,
                function(data){
                    $("#reClassifyDiv").dialog('close')
                });
             }
             function goBack()
             {
                 document.forms.ratingForm.action = "<%=request.getContextPath()%>/home.jsp#Sentiment_Analysis";
                 document.forms.ratingForm.submit();
             }
             function addRow()
             {
                 var rowCount=$("#ratingRows").val();
                 var html="<tr id='row"+rowCount+"' ><td>Parameter: <input type='text' name='paramNames' id='paramId"+rowCount+"' value=''></td>"
                     html+="<td><input type='radio' name='ratingRadio"+rowCount+"' value='positive'> Positive </td>"
                     html+="<td><input type='radio'  name='ratingRadio"+rowCount+"' value='negative'> Negative </td>"
                     html+="<td><input type='radio'  name='ratingRadio"+rowCount+"' value='other'> Other </td></tr>"
                 var tableObj=document.getElementById("reclassifyRatedTable");
                 var tbodyObj=tableObj.getElementsByTagName("tbody");
                 var innrHtml=tbodyObj[0].innerHTML
                 tbodyObj[0].innerHTML=innrHtml+html;
                // $("#reclassifyRatedTable").html($("#reclassifyRatedTable").html()+html);
                 rowCount++;
                 $("#ratingRows").val(rowCount);
             }
             function deleteRow()
             {
                 var rowCount=$("#ratingRows").val();
                 rowCount--;
                 if(rowCount>0)
                 {
                     $("#row"+rowCount).remove();
                     $("#ratingRows").val(rowCount);
                 }
             }
             function changeSearchType()
             {
                 searchType=$("#searchType").val();
                         var options, a;
                          var searchVal="sentiment"+searchType;
                options = {
                    serviceUrl:'studioAction.do?studioParam=autoSuggest&tab='+searchVal+'&userId=<%=userId%>',
                    delimiter: /(,)\s*/, // regex or character
                    minChars:2,
                    deferRequestBy: 500, //miliseconds
                    maxHeight:400,
                    width:450,
                    zIndex: 9999,
                    noCache: false

                };
                a = $("#srchTextReport").autocomplete(options);
                $("#srchTextReport").keyup(function(event){
                    if(event.keyCode == 13){
                       // searchVals();

                    }
                });
             }
         </script>
    </body>
</html>
