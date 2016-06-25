<%@page import="utils.db.*" %>
<%@ page  import="java.awt.*" %>
<%@ page  import="java.io.*" %>
<%@ page  import="java.sql.*" %>
<%@page import="prg.db.*"%>
<%@ page import="java.util.*" %>
<%@page import="com.progen.target.*" %>
<%@page import="com.progen.target.display.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<title>pi EE</title>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <Script language="javascript"  src="<%=request.getContextPath()%>/QTarget/JS/myScripts.js"></Script>

       <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.tabs.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.changedialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/effects.highlight.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/effects.slide.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <%--
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>

       --%>

        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">

        <style>
            .navsection {
                text-decoration: none;
                margin: 0 0 0 0;
                border: 1px solid #CDCDCD;
                background: no-repeat top left;
                -moz-border-radius: 4px 4px 4px 4px;
                COLOR: #000;
                WIDTH: 100%;
            }
            .navtitle
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .displayTotal
            {
            width:99%;
            }
            .myTextbox3 {
            font-family: Verdana, Arial, Helvetica, sans-serif;
            font-weight: normal;
            font-size: 8pt;
            color:#000000;
            padding: 0px;
            width:170px;
            margin-left: 5px;
            border-top: 1px groove #848484;
            border-right: 1px inset #999999;
            border-bottom: 1px inset #999999;
            border-left: 1px groove #848484;
            background-color:#FFFFFF;

            }
             .white_content {
                display: none;
                position: absolute;
                top: 35%;
                left: 35%;
                width: 20%;
                height:17%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
            }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 120%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }

        </style>

        <script type="text/javascript">
//              function logout(){
//                var path = '<%=request.getContextPath()%>';
//                document.forms.frmParameter.action=path+"/baseAction.do?param=logoutApplication";
//                document.forms.frmParameter.submit();
//            }
//            function gohome(){
//                var path = '<%=request.getContextPath()%>';
//                document.forms.frmParameter.action=path+"/baseAction.do?param=goHome";
//                document.forms.frmParameter.submit();
//            }

            $(function() {
                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 3,
                    stepMonths: 3
                });
                $('#datepicker1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 3,
                    stepMonths: 3
                });


            });

           function copyData(targetId)
            {
                var minTimeLevel = document.frmParameter.TIMELEVEL.value;
                var periodType = document.frmParameter.CBO_PRG_PERIOD.value;
                //document.forms.frmParameter.action="pbSelectTimeRange.jsp";
                //document.forms.frmParameter.submit();
                window.open('<%=request.getContextPath()%>'+"/QTarget/JSPs/pbSelectTimeRange.jsp?targetId="+targetId+"&minTime="+minTimeLevel+"&periodType="+periodType, "Select Time Range","scrollbars=1,width=800,height=350,address=no");
            }


            function goToTargetsHome2()
            {
              //  document.forms.frmParameternew.action= "QTarget/JSPs/pbTargetList.jsp";
              // document.forms.frmParameternew.submit();
               document.forms.frmParameternew.action= "home.jsp#Targets";
               document.forms.frmParameternew.submit();
            }
            function submitform()
            {
               // alert(primA);
                //alert(secA);
                var colDrillUrl=document.getElementById('colDrillUrl').value;
                //alert('colDrillUrl '+colDrillUrl);
                var viewby = document.getElementById('primaryAnalyze').value;
                var primParamEleId = document.getElementById('primParamEleId').value;
                var minTimeLevel = document.getElementById('minTimeLevel').value;
                var periodType = document.getElementById('periodType').value;
                var secAnalyze = document.getElementById('secAnalyze').value;
               // alert(document.frmParameter.secondaryViewBy.value)
                var sec =  document.getElementById('secondaryViewBy').value;//.value();//document.frmParameter.secondaryViewBy.value;
                var prim =  document.frmParameter.primaryViewBy.value;

                var tertiary =  document.getElementById('tertiaryViewBy').value;
                var maxTimeLevel=document.getElementById('maxTimeLevel').value;
               // alert(document.getElementsByName('CBO_PRG_PERIOD')[0].value)
                var pType=document.getElementsByName('CBO_PRG_PERIOD')[0].value;
              // alert(maxTimeLevel+ ' tertiaryViewBy '+tertiary+" pType "+pType);
               var targetStartDate = document.getElementById('targetStartDate').value;
                var targetEndDate = document.getElementById('targetEndDate').value;
                var fDate;
                var tDate;
                if(minTimeLevel=="Day")
                {
                    var startDate = new Date(targetStartDate);
                    var endDate = new Date(targetEndDate);
                }


                if(document.frmParameter.CBO_AS_OF_DATE!=undefined)
                {
                    fDate = document.frmParameter.CBO_AS_OF_DATE.value;
                }
                else if(document.frmParameter.CBO_AS_OF_MONTH!=undefined)
                {
                    fDate = document.frmParameter.CBO_AS_OF_MONTH.value;
                }
                else if(document.frmParameter.CBO_AS_OF_QTR!=undefined)
                {
                    fDate = document.frmParameter.CBO_AS_OF_QTR.value;
                }
                else
                {
                    fDate = document.frmParameter.CBO_AS_OF_YEAR.value;
                }


                if(document.frmParameter.CBO_AS_OF_DATE1!=undefined)
                {
                    tDate = document.frmParameter.CBO_AS_OF_DATE1.value;
                }
                else if(document.frmParameter.CBO_AS_OF_MONTH1!=undefined)
                {
                    tDate = document.frmParameter.CBO_AS_OF_MONTH1.value;
                }
                else if(document.frmParameter.CBO_AS_OF_QTR1!=undefined)
                {
                    tDate = document.frmParameter.CBO_AS_OF_QTR1.value;
                }
                else
                {
                    tDate = document.frmParameter.CBO_AS_OF_YEAR1.value;
                }

                if(minTimeLevel=="Day")
                {
                    var fromDate = new Date(fDate);
                    var toDate = new Date(tDate);

                    if(toDate.getTime() > endDate.getTime())
                    {
                        alert("Please choose To-Date within Target Start-Date and End-Date");
                        document.frmParameter.CBO_AS_OF_DATE1.focus();
                        return false;
                    }
                    if(fromDate.getTime() < startDate.getTime())
                    {
                        alert("Please choose From-Date within Target Start-Date and End-Date");
                        document.frmParameter.CBO_AS_OF_DATE.focus();
                        return false;
                    }
                    if(toDate.getTime() < fromDate.getTime())
                    {
                        alert("To-Date must be greater than From-Date");
                        document.frmParameter.CBO_AS_OF_DATE1.focus();
                        return false;
                    }
                }
                else if(minTimeLevel=="Month")
                {
                    var getStartMonthYear = fDate.substring(4, fDate.length);
                    var getEndMonthYear = tDate.substring(4, tDate.length);
                    if(getEndMonthYear < getStartMonthYear)
                    {
                        alert("Target End-Month must be greater than Target Start-Month");
                        document.frmParameter.CBO_AS_OF_MONTH1.focus();
                        return false;
                    }
                    if(getEndMonthYear == getStartMonthYear)
                    {
                        var getStartMonth = fDate.substring(0,3);
                        var getEndMonth = tDate.substring(0,3);
                        //alert(getStartMonth);
                        //alert(getEndMonth);
                        var theFormFieldArray = formFieldArray();
                        for (key in theFormFieldArray)
                        {
                            if(eval(theFormFieldArray[getEndMonth] + ' < '+theFormFieldArray[getStartMonth]))
                            {
                                alert('Target End-Month must be greater than Target Start-Month');
                                document.frmParameter.CBO_AS_OF_MONTH1.focus();
                                return false;
                            }
                        }
                    }
                }
                else if(minTimeLevel=="Quarter")
                {
                    var getStartQtrYear = fDate.substring(3, fDate.length);
                    var getEndQtrYear = tDate.substring(3, tDate.length);

                    if(getEndQtrYear < getStartQtrYear)
                    {
                        alert("Target End-Quarter must be greater than Target Start-Quarter");
                        document.frmParameter.CBO_AS_OF_QTR1.focus();
                        return false;
                    }

                    if(getEndQtrYear == getStartQtrYear)
                    {
                        var getStartQtr = fDate.substring(0,2);
                        var getEndQtr = tDate.substring(0,2);
                        //alert(getStartMonth);
                        //alert(getEndMonth);
                        var theQuarterArray = quarterArray();
                        for (key in theQuarterArray)
                        {
                            if(eval(theQuarterArray[getEndQtr] + ' < '+theQuarterArray[getStartQtr]))
                            {
                                alert('Target End-Quarter must be greater than Target Start-Quarter');
                                document.frmParameter.CBO_AS_OF_QTR1.focus();
                                return false;
                            }
                        }
                    }
                }
                else if(minTimeLevel=="Year")
                {
                    if(fDate > tDate)
                    {
                        alert("Target End-Year must be greater than Target Start-Year");
                        document.frmParameter.CBO_AS_OF_YEAR1.focus();
                        return false;
                    }
                }

                      // alert('sec '+sec);
                       //if(sec==nu)
                    //   alert('prim '+prim+' tertiary '+tertiary)
               if(maxTimeLevel==pType&&tertiary=='Percentage'&&prim=="0")
               {
                   alert('Please select absolute for the maximum time level');
               }
               else if(prim!="0"&&tertiary=='Percentage')
                   {
                         alert('Please select absolute for any other primary view by other than Overall');
                   }
               else{
                if(sec!='Time')
                {
                        if(primParamEleId==prim)
                        {
                            alert('Please dont select Overall Target in Analyze By');
                            return false;
                        }
                        else
                        {
                            document.forms.frmParameter.action = "targetView.do?targetParams=viewTargetForView&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                            document.forms.frmParameter.submit();
                            return true;
                        }
                 }
                else
                {
                    document.forms.frmParameter.action = "targetView.do?targetParams=viewTargetForView&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                    document.forms.frmParameter.submit();
                    return true;
                }
             }
            }


            function submitDrill(drillUrl)
            {
                var viewby = document.getElementById('primaryAnalyze').value;
                var primParamEleId = document.getElementById('primParamEleId').value;
                var minTimeLevel = document.getElementById('minTimeLevel').value;
                var periodType = document.getElementById('periodType').value;
                var secAnalyze = document.getElementById('secAnalyze').value;
                if(minTimeLevel==periodType)
                {
                if(primParamEleId==viewby)
                   {

                          document.forms.frmParameter.action = "targetView.do?targetParams=viewTargetForView&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+drillUrl;
                          document.forms.frmParameter.submit();
                   }
                  
                }
                else
                {
                    if(primParamEleId==viewby)
                     {

                        document.forms.frmParameter.action = "targetView.do?targetParams=viewTargetForView&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+drillUrl;
                        document.forms.frmParameter.submit();
                     }
               }
            }


            function submitTimeDrill(timeDrill)
            {
                     document.forms.frmParameter.action = "targetView.do?targetParams=viewTargetForView&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+timeDrill+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                     document.forms.frmParameter.submit();
            }

            function submitColumnDrillUrl(columnDrillUrl)
            {       document.forms.frmParameter.action = "targetView.do?targetParams=viewTargetForView&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+columnDrillUrl+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                    document.forms.frmParameter.submit();
            }
            //added by susheela start
             function showGraph()
             {
                 document.forms.frmParameter.action = "QTarget/JSPs/pbShowGraph.jsp";
                 document.forms.frmParameter.submit();

             }
              function showGraph2()
             {
                 var urlValues="";
                 var tableObj=document.getElementById('target');
                 var trObjs = tableObj.getElementsByTagName("Tr");
                 var tdObjs = trObjs[0].getElementsByTagName("Td");
                 var trNum=trObjs.length;
                 var tdNum = tdObjs.length;//trObjs.getElementsByTagName("Td").length;
                 var specificRowTdObjs = tdObjs;//.getElementsByTagName("Td");
                 var tertiary =  document.getElementById('tertiaryViewBy').value;

                 var selectedPeriod = document.getElementsByName("CBO_PRG_PERIOD")[0].value;
                 var primaryAnalyze = document.getElementById('primaryAnalyze').value;
                 var maxTimeLevel=document.getElementById('maxTimeLevel').value;

                 if(maxTimeLevel!=selectedPeriod&&primaryAnalyze=="0")
                 {
                      for(var k=2;k<trNum-1;k++)
                      {
                          for(var h=1;h<tdNum;h++)
                          {
                             var tdObjs = trObjs[k].getElementsByTagName("td");
                             var inputObj = tdObjs[h].getElementsByTagName("input")[0];
                             var content = inputObj.value;
                             var inputName = inputObj.attributes[2].value;
                             if(content!="")
                             urlValues=urlValues+"&"+inputName+"="+content;
                           }
                      }
                 }
                 else
                     {
                         for(var k=1;k<trNum-1;k++)
                          {
                              for(var h=1;h<tdNum;h++)
                              {
                                 var tdObjs = trObjs[k].getElementsByTagName("td");
                                 var inputObj = tdObjs[h].getElementsByTagName("input")[0];
                                 var content = inputObj.value;
                                 var inputName = inputObj.attributes[2].value;
                                 if(content!="")
                                 urlValues=urlValues+"&"+inputName+"="+content;
                               }
                          }
                     }

                  
                 
                  if(primaryAnalyze=="0")
                      {
                           //alert(' overall ');
                           var secAnVal=document.getElementById('secAnVal').value;
                           if(tertiary=="Absolute")
                               {
                                   var secAnValArr=secAnVal.split(",");
                                   var content="";
                                   for(var m=0;m<secAnValArr.length;m++)
                                       {
                                           var bName=secAnValArr[m];
                                           var name="OverAll:"+bName;
                                           //alert('name '+name);
                                           content=document.getElementById(name).value;
                                           if(content!="")
                                           urlValues=urlValues+"&"+"OverAll:"+bName+"="+content;
                                       }
                              }
                              else if(tertiary=="Percentage")
                                  {
                                  // alert('in else ');
                                   var secAnValArr=secAnVal.split(",");
                                   var content="";
                                   for(var m=0;m<secAnValArr.length;m++)
                                       {
                                           var bName=secAnValArr[m];
                                           var name="OverAll:"+bName+":d";
                                          // alert('name '+name);
                                           content=document.getElementById(name).value;
                                           if(content!="")
                                           urlValues=urlValues+"&"+"OverAll:"+bName+"="+content;
                                       }
                                  }
                      }

                  urlValues=urlValues.substring(1);
                  var divObj=document.getElementById("graphsFrame");
                   $.ajax({
                                    url: 'targetView.do?targetParams=getGraph&'+urlValues,
                                    success: function(data) {
                                        if(data != ""){
                                            document.getElementById('targetDiv').style.display='fade';
                                            document.getElementById("graphsFrame").innerHTML = data;
                                            //frameObj.style.display='block';
                                           // document.getElementById('fade').style.display='block';
                                        }

                                    }
                                });
                  }


             function goToSave(){
                
                var viewby = document.getElementById('primaryAnalyze').value;
                var primParamEleId = document.getElementById('primParamEleId').value;
                var minTimeLevel = document.getElementById('minTimeLevel').value;
                var periodType = document.getElementById('periodType').value;
                var secAnalyze = document.getElementById('secAnalyze').value;
                //var t = confirm("Are you sure you want to save for now.It is temporary save");
                            //if(t){
                if(secAnalyze=="Time"){
                if(minTimeLevel==periodType)
                {
                   if(primParamEleId==viewby)
                   {

                        var t = confirm("Are you sure you want to save.It is primary parameter changing it will accordingly allocate it to other parameters");
                            if(t==true){
                                            document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                                            document.forms.frmParameter.submit();
                                       }
                   }
                   else
                       {
                         /*  if(checkSum())
                           {
                               var t = confirm("Are you sure you want to save.");
                               if(t==true)
                               {   */
                                            document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                                            document.forms.frmParameter.submit();
                      /*         }
                           }*/
                       }
                }
                 else
                    {
                         var t = confirm("Are you sure you want to save.It is primary parameter.Target is defined at '"+minTimeLevel+"' changing it at '"+periodType+"' will accordingly allocate at '"+minTimeLevel+"'");
                                if(t==true)
                                {
                                    document.forms.frmParameter.action = "targetView.do?targetParams=allocateTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                                    document.forms.frmParameter.submit();
                                }
                    }
                }

                else
                    { alert('here-0-0- ')
                        if(checkRowSum())
                        {
                          var t = confirm("Are you sure you want to save.");
                                if(t==true)
                                {
                                    document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                                    document.forms.frmParameter.submit();
                                }
                        }
                    }
           }


           /* if(!checkRowSum())
                    {
                        var t = confirm("Are you sure you want to save for now.It is temporary save");
                        if(t){ */
            function checkRowSum()
            {
                    //alert(' herr --');

                var tableObj = document.getElementById('target');
                //alert(tableObj.rows.length)
                var trObj = tableObj.getElementsByTagName("tr");
                var tdObj = trObj[0].getElementsByTagName("td");
                var rowName;
                var columnName;
                //var tds = tdObj.length;

                var rowTemp = new Array();
                for(var b=0;b<trObj.length;b++)
                {
                    rowTemp[b] = 0;
                }

                var fl = 0;
                for(var y=1;y<trObj.length;y++)
                {
                    if(y<trObj.length-1)
                    {
                        tdObj = trObj[y].getElementsByTagName("td");
                        for(var z=1;z<tdObj.length;z++)
                        {
                            if(z < parseInt(tdObj.length)-3)
                            {
                                var value = tdObj[z].getElementsByTagName("input")[0].value;
                                if(value=='')
                                value=0;
                                rowTemp[y] = parseInt(rowTemp[y] + parseInt(value));
                            }
                            else if(z==tdObj.length-2)
                            {
                                var rowTotal = parseInt(tdObj[tdObj.length-2].getElementsByTagName("input")[0].value);
                                rowName = tdObj[tdObj.length-2].parentNode.getElementsByTagName("td")[0].innerHTML;
                                var rowSum = parseInt(rowTemp[y]);
                                if(rowSum != rowTotal)
                                {
                                    alert("Row Total at '"+rowName+"' not matched");
                                    fl = fl++;
                                    return false;
                                }

                            }
                        }
                    }
                    else
                    {
                        if(fl==0)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }

                }
            }

            function goToSaveTemporary()
            {
                var viewby = document.getElementById('primaryAnalyze').value;
                var primParamEleId = document.getElementById('primParamEleId').value;
                var minTimeLevel = document.getElementById('minTimeLevel').value;
                var periodType = document.getElementById('periodType').value;
                var secAnalyze = document.getElementById('secAnalyze').value;

                if(secAnalyze=='Time'){
                if(minTimeLevel==periodType)
                {
                    if(primParamEleId==viewby)
                       {

                            var t = confirm("Are you sure you want to save.It is primary parameter changing it will accordingly allocate it to other parameters");
                                if(t==true)
                                {
                                    document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                                    document.forms.frmParameter.submit();
                                }
                       }
                     else
                       {
                           if(checkSum())
                           {
                               var t = confirm("Are you sure you want to save.");
                               if(t==true)
                               {
                                            document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                                            document.forms.frmParameter.submit();
                               }
                           }
                           else
                           {
                               var t = confirm("Are you sure you want to save.It is temporay save.");
                               if(t==true)
                               {
                                            document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                                            document.forms.frmParameter.submit();
                               }

                           }
                       }
                }
                else
                    {
                         var t = confirm("Are you sure you want to save.It is primary parameter.Target is defined at '"+minTimeLevel+"' changing it at '"+periodType+"' will accordingly allocate at '"+minTimeLevel+"'");
                                if(t==true)
                                {
                                    document.forms.frmParameter.action = "targetView.do?targetParams=allocateTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                                    document.forms.frmParameter.submit();
                                }
                    }
                }
                else
                    {   if(checkRowSum())
                        {
                                var t = confirm("Are you sure you want to save.");
                                if(t==true)
                                {
                                    document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                                    document.forms.frmParameter.submit();
                                }
                         }
                         else
                         {
                                var t = confirm("Are you sure you want to save.It is temporary save.");
                                if(t==true)
                                {
                                    document.forms.frmParameter.action = "targetView.do?targetParams=saveTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                                    document.forms.frmParameter.submit();
                                }
                         }
                    }
            }

            function copyUrlParent()
            {
                //var reUrl = copyUrl;
                document.frmParameter.action = "targetView.do?targetParams=viewTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value;
                document.frmParameter.submit();
            }



            function goToTargetsH()
            {
                document.frmParameter.action = "pbTargetList.jsp";
                document.frmParameter.submit();
            }
            function copyToDates()
            {
                var result = confirm("Do you want to copy to other Dates");
                if(result==true)
                {
                    // populateRest(textB)
                }
            }
            function populateRest(textBoxName)
            {
                //alert(textBoxName)
                var data = document.getElementsByName(textBoxName)[0].value;
                //alert(data);
                pNode1 = document.getElementsByName(textBoxName)[0].parentNode;
                //alert(pNode1);
                pNode2 = pNode1.parentNode;
                //alert(pNode2)
                mytds = pNode2.getElementsByTagName("td");
                //alert(mytds.length);

                for(i=2;i<mytds.length;i++)
                {
                    //alert(mytds[i].getElementsByTagName("input")[0].getAttribute("name"));
                    var varName = mytds[i].getElementsByTagName("input")[0].getAttribute("name");
                    document.getElementsByName(varName)[0].value = data;
                }
            }

            function personalizevalues(){
                document.frmParameter.action ="PersonalisedReports/JSPS/pbPersonalisedReportRegister.jsp";
                document.frmParameter.target ="_blank";
                document.frmParameter.submit();

            }


            function isNumberKey(evt)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode
                if (charCode > 31 && (charCode < 48 || charCode > 57))
                    return false;
                return true;
            }
            function checkSum()
            {
                var tableObj = document.getElementById('target');
                //alert(tableObj.rows.length)
                var trObj = tableObj.getElementsByTagName("tr");
                var tdObj = trObj[0].getElementsByTagName("td");
                var rowName;
                var columnName;
                //var tds = tdObj.length;


                var columnTemp = new Array();
                for(var a=0;a<tdObj.length;a++)
                {
                    columnTemp[a] = 0;
                }

                for(var i=1;i<trObj.length;i++)
                {
                    if(i < parseInt(trObj.length)-3)
                    {
                        tdObj = trObj[i].getElementsByTagName("td");
                        for(var j=1;j<tdObj.length-1;j++)
                        {
                            var val = tdObj[j].getElementsByTagName("input")[0].value;
                            // alert(val)
                            if(val=='')
                            {
                                val = 0;
                            }
                            val = parseInt(val);
                            //alert(val)
                            columnTemp[j] = parseInt(columnTemp[j] + parseInt(val));
                            //flag = columnTemp[j];
                        }
                    }
                    else
                    {
                        tdObj = trObj[trObj.length-2].getElementsByTagName("td");
                        var fl = 0;
                        for(var k=1;k<tdObj.length-1;k++)
                        {
                            var columTotal = parseInt(tdObj[k].getElementsByTagName("input")[0].value);
                            columnName = trObj[0].getElementsByTagName("td")[k].innerHTML;
                            var columnSum = parseInt(columnTemp[k])
                            if(columnSum != columTotal)
                            {
                                if(columTotal==0)
                                {
                                    alert("You can't enter any data over here.\nBecause restricting total is '0'");
                                    return false;
                                }
                                else
                                {
                                    alert("Column Total at '"+columnName+"' not matched");
                                    fl = fl++;
                                    return false;
                                }

                            }
                        }
                        if(fl==0)
                            return true;
                        else return false;

                    }
                }
            }

            function checkSum2()
            {
                var tableObj = document.getElementById('target');
                //alert(tableObj.rows.length)
                var trObj = tableObj.getElementsByTagName("tr");
                var tdObj = trObj[0].getElementsByTagName("td");
                var rowName;
                var columnName;
                //var tds = tdObj.length;

                var columnTemp = new Array();
                for(var a=0;a<tdObj.length;a++)
                {
                    columnTemp[a] = 0;
                }

                for(var i=1;i<trObj.length;i++)
                {
                    if(i < parseInt(trObj.length)-3)
                    {
                        tdObj = trObj[trObj.length].getElementsByTagName("td");
                        for(var j=1;j<tdObj.length-1;j++)
                        {
                            var val = tdObj[j].getElementsByTagName("input")[0].value;
                            columnTemp[j] = parseInt(columnTemp[j] + parseInt(val));
                            //flag = columnTemp[j];
                        }
                    }
                    else
                    {
                        tdObj = trObj[trObj.length-2].getElementsByTagName("td");
                        var fl = 0;
                        for(var k=1;k<tdObj.length-1;k++)
                        {
                            var columTotal = parseInt(document.getElementsById('GrandTotal').value);
                            columnName = trObj[0].getElementsByTagName("td")[k].innerHTML;
                            var columnSum = parseInt(columnTemp[k])
                            if(columnSum != columTotal)
                            {
                                alert("Column Total at '"+columnName+"' not matched");
                                fl = fl++;
                                return false;
                            }
                        }
                        if(fl==0)
                            return true;
                        else return false;

                    }
                }
            }

            function showSavedData()
            {
                window.self.close();
                return true;
            }


            var name;

            function recursivePrompt(sDate,eDate)
            {
                var number = prompt("Enter number of consecutive "+period+"(s)",1);
                var selectedPeriod = document.getElementsByName("CBOPRG_PERIOD_TYPE")[0].value;
                var targetStartDate = sDate;
                var targetEndDate = eDate;
                //alert(selectedPeriod)
                //alert(number)
                if(number!=null)
                {
                    number = parseInt(number);
                    //alert(number)
                    if(isNaN(number))
                    {
                        alert("Please enter numbers only");
                        recursivePrompt(targetStartDate,targetEndDate);
                    }
                    if(number==0)
                    {
                        alert("Enter number other than 0");
                        recursivePrompt(targetStartDate,targetEndDate);
                    }
                    if(! isNaN(number) && number!=0)
                    {
                        var fromDate = new Date(targetStartDate)
                        var toDate = new Date(targetEndDate);
                        var existedPeriod = 0;
                        //alert(targetStartDate)
                        //alert(targetEndDate);
                        //alert(number)
                        if(selectedPeriod=="Day")
                        {
                            var oneDay = 1000*60*60*24;
                            existedPeriod = (toDate.getTime() - fromDate.getTime()) / oneDay;
                            //alert(existedPeriod)
                        }
                        if(selectedPeriod=="Month")
                        {
                            var fromDateMonth = fromDate.getMonth();
                            var toDateMonth = toDate.getMonth();
                            //alert(toDateMonth - fromDateMonth)
                            //var numberOfMonths = 0;
                            if(toDate.getFullYear() == fromDate.getFullYear())
                            {
                                existedPeriod = toDateMonth - fromDateMonth;
                            }
                            else
                            {
                                var existedPeriod = 11 - fromDateMonth;
                                existedPeriod = existedPeriod + toDateMonth + 1;
                                existedPeriod = existedPeriod + ((toDate.getFullYear() - fromDate.getFullYear() - 1) * 12);
                            }
                            //alert(existedPeriod)
                            //existedPeriod = toDateMonth - fromDateMonth;
                        }
                        if(selectedPeriod=="Quarter")
                        {
                            var fromDateQuarter = parseInt((fromDate.getMonth()) / 3) + parseInt(1);
                            var toDateQuarter = parseInt((toDate.getMonth()) / 3) + parseInt(1);
                            //alert("From Date quarter is: "+fromDateQuarter+"\nTo Date quarter is: "+toDateQuarter)
                            if(toDate.getFullYear() == fromDate.getFullYear())
                            {
                                existedPeriod = toDateQuarter - fromDateQuarter;
                            }
                            else
                            {
                                var existedPeriod = 4 - fromDateQuarter;
                                existedPeriod = existedPeriod + toDateQuarter;
                                existedPeriod = existedPeriod + ((toDate.getFullYear() - fromDate.getFullYear() - 1) * 4);
                            }
                        }
                        if(selectedPeriod=="Year")
                        {
                            var fromDateYear = fromDate.getFullYear();
                            var toDateYear = toDate.getFullYear();
                            //alert("fromDateYear is: "+fromDateYear+"\ntoDateYear is: "+toDateYear)
                            existedPeriod = toDateYear - fromDateYear;
                        }
                        //alert("available: "+existedPeriod+"\nchosen: "+number)
                        if(number > existedPeriod)
                        {
                            alert("You can not copy beyond target end-date. Enter other number.");
                            recursivePrompt(targetStartDate,targetEndDate);
                        }
                        if(number <= existedPeriod)
                        {
                            //alert(myDate.getDate())
                            //alert(myDate.getMonth()+1)
                            //alert(myDate.getFullYear())


                            //alert("in:::::::::::")
                            //alert(number)
                            var makeSure = confirm("Do you want to proceed");
                            if(makeSure==true)
                            {
                                //alert(number);
                                document.frmParameter.copyToOthers.value = number;
                                document.frmParameter.firstColumn.value = name;
                                var periodType = document.getElementById('periodType').value;
                                var minLevel = document.getElementById('minTimeLevel').value;
                                if(periodType!=minLevel){
                                    alert('The Target is defined at '+minLevel+'. If you copy it at '+periodType+' level. It will be allocated on '+minLevel+' accordingly ');
                                    var t = confirm("Are you sure you want to update.");
                                    if(t){  //alert('here');
                                        document.forms.frmParameter.action="pbAllocateTarget.jsp";
                                        document.forms.frmParameter.submit();
                                        return true;
                                    }
                                }
                                else{
                                    //alert('here..');
                                    document.forms.frmParameter.action="pbSaveTargetFinalInDim.jsp?redirectFlag=Y";//pbSaveTarget.jsp";//pbSaveTargetPrimSecReview.jsp";
                                    document.frmParameter.submit();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }



            function checkGrandTotal()
            {
                var tableObj = document.getElementById("target");
                var trObj = tableObj.getElementsByTagName("tr");
                var tdObj = trObj[0].getElementsByTagName("td");
                var val;
                var content = 0;
                var columnName;


                tdObj = trObj[trObj.length-1].getElementsByTagName("td");
                for(var k=1;k<tdObj.length;k++)
                {
                    var columnTotal = parseInt(tdObj[k].getElementsByTagName("input")[0].value);
                    columnName = trObj[0].getElementsByTagName("td")[k].innerHTML;
                    if(columnTotal < 0)
                    {
                        alert("Column Total at "+columnName+" not matched");
                        return false;
                    }
                    else
                    {
                        return true;
                    }

                }

            }


            function funToBeLoad(secViewBy)
            {
                //alert("secViewBy is::: "+secViewBy);
                if(secViewBy=="Time")
                {
                    showGrandTotalrow();
                }
                else
                {
                    showGrandTotalColumn();
                    disablePeriod();
                }
            }
            function disablePeriod()
            {
                var selectedPeriod = document.forms.frmParameter.CBO_PRG_PERIOD.value;
                //alert("selectedPeriod is::: "+selectedPeriod)
                document.forms.frmParameter.CBO_PRG_PERIOD.disabled = true;
                disableTo(selectedPeriod);
            }
            function disableTo(selectedPeriod)
            {
                if(selectedPeriod=="Day")
                {
                    document.forms.frmParameter.CBO_AS_OF_DATE1.disabled = true;
                }
                else if(selectedPeriod=="Month")
                {
                    document.forms.frmParameter.CBO_AS_OF_MONTH1.disabled = true;
                }
                else if(selectedPeriod=="Quarter")
                {
                    document.forms.frmParameter.CBO_AS_OF_QTR1.disabled = true;
                }
                else if(selectedPeriod=="Year")
                {
                    document.forms.frmParameter.CBO_AS_OF_YEAR1.disabled = true;
                }
            }

              //added by bharathi reddy
          /*  function gotoExcel(){
                var primaryAnalyze = document.getElementById('primaryAnalyze').value;
                var secAnalyze=document.getElementById('secAnalyze').value;
                var TARGETID=document.getElementById('TARGETID').value;
                if(primaryAnalyze==0)
                    {
                     alert("You can"+"'"+"t download sheet for Overall As primary Analyze By");
                    }
                    else
                    {
                        document.forms.frmParameter.action =''+"/QTarget/JSPs/pbgotoExcel.jsp";
                        document.forms.frmParameter.submit();
                }
           } */
         /*  function goToPrevPage(data){
                alert("dta ius:: "+data)
                document.getElementById("uploadexcel").style.display='none';
                document.getElementById("fade").style.display = 'none';
                document.forms.frmParameter.action = "targetView.do?targetParams=viewTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+columnDrillUrl+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                document.forms.frmParameter.submit();
           } */
            function gotoEditData()
                {
                   // alert('in lksda');
                    document.forms.frmParameter.action = "targetView.do?targetParams=viewTarget&targetId="+document.forms.frmParameter.TARGETID.value+"&minTimeLevel="+document.forms.frmParameter.TIMELEVEL.value+"&tertiaryViewBy="+document.getElementById('tertiaryViewBy').value;
                    document.forms.frmParameter.submit();
                }
                 function comeFromExcel(){
                    document.getElementById("uploadexcel").style.display='block';
                    document.getElementById("fade").style.display = 'block';
                    document.getElementById("uploadframe").style.display = 'block';
                    var frameobj = document.getElementById("uploadframe");
                    frameobj.src ="<%=request.getContextPath()%>/QTarget/JSPs/uploadExcel.jsp";
               }

           
               
        </script>
    </head>
    <%-- <body onload="funToBeLoad(document.getElementById('secondaryViewBy').value)"> --%>
   <body>

<%
       String targetParamSectionDisplay = "";
       String currentURL = "";
       String targetId = "";
       String minTimeLevel = "";
       String targetDataRegionDisplay = "";
       ArrayList rowEdgeValues = new ArrayList();
       String columnEdgeValues = "";

       targetId = (String) request.getAttribute("TARGETID");
       minTimeLevel = (String) request.getAttribute("TIMELEVEL");


       if (request.getAttribute("targetParamSectionDisplay") != null)
       {
                targetParamSectionDisplay = String.valueOf(request.getAttribute("targetParamSectionDisplay"));
               // ////////////////////////////////////////////////////////////////////////.println("in if-->"+targetParamSectionDisplay);
       }

       if (request.getAttribute("targetDataRegionDisplay") != null)
       {
                targetDataRegionDisplay = String.valueOf(request.getAttribute("targetDataRegionDisplay"));
               // ////////////////////////////////////////////////////////////////////////.println("in data if-->"+targetDataRegionDisplay);
       }

      ArrayList rowEdgeValuesA = new ArrayList();
       if (request.getAttribute("rowEdgeValues") != null)
       {
                rowEdgeValuesA = (ArrayList)request.getAttribute("rowEdgeValues");
               // ////////////////////////////////////////////////////////////////////////.println("in data if- 444 ->"+rowEdgeValuesA);
       }
       ArrayList columnEdgeValuesA = new ArrayList();
       if (request.getAttribute("columnEdgeValues") != null)
       {
                columnEdgeValuesA = (ArrayList)request.getAttribute("columnEdgeValues");
               // ////////////////////////////////////////////////////////////////////////.println("in data if 44 -->"+columnEdgeValuesA);
       }
       HashMap originalResult = new HashMap();
       if (request.getAttribute("originalResult")!= null)
       {
                originalResult = (HashMap)request.getAttribute("originalResult");
              //  ////////////////////////////////////////////////////////////////////////.println("in data if .. 44 ..//-->"+originalResult);
       }
       String overAllMessage = "";
        if (request.getAttribute("overAllMessage")!= null)
       {
               overAllMessage = (String)request.getAttribute("overAllMessage");
       }
       String errorMessage="";
        if (request.getAttribute("errorMessage")!= null)
       {
               errorMessage = (String)request.getAttribute("errorMessage");
               ////////////////////////////////////////////////////////////////////////.println(overAllMessage+" overAllMessage errorMessage.. "+errorMessage);
       }
       String primaryAnalyze="";
        if (request.getAttribute("primaryAnalyze")!= null)
       {
               primaryAnalyze = (String)request.getAttribute("primaryAnalyze");

       }
        String secAnalyze="";
        if (request.getAttribute("secAnalyze")!= null)
       {
               secAnalyze = (String)request.getAttribute("secAnalyze");

       }
       String startRange="";
       if (request.getAttribute("startRange")!= null)
       {
               startRange = (String)request.getAttribute("startRange");
       }
       String primParamEleId="";
       if (request.getAttribute("primParamEleId")!= null)
       {
               primParamEleId = (String)request.getAttribute("primParamEleId");
       }
       String periodType="";
       if (request.getAttribute("periodType")!= null)
       {
               periodType = (String)request.getAttribute("periodType");
       }

       if (request.getAttribute("minTimeLevel")!= null)
       {
               minTimeLevel = (String)request.getAttribute("minTimeLevel");
       }
       ArrayList defDates = new ArrayList();
       if (request.getAttribute("defDates")!= null)
       {
               defDates = (ArrayList)request.getAttribute("defDates");
               ////////////////////////////////////////////////////////////////////////.println(" defDates -- "+defDates);
       }
       String endRange = "";
       if (request.getAttribute("endRange")!= null)
       {
               endRange = (String)request.getAttribute("endRange");

       }
       String startPeriod="";
       if (request.getAttribute("startPeriod")!= null)
       {
               startPeriod = (String)request.getAttribute("startPeriod");
       }
       String endPeriod="";
       if (request.getAttribute("endPeriod")!= null)
       {
               endPeriod = (String)request.getAttribute("endPeriod");
       }
       String dateMeassage="";
        if (request.getAttribute("dateMeassage")!= null)
       {
               dateMeassage = (String)request.getAttribute("dateMeassage");
       }
       String measureName="";
        if (request.getAttribute("measureName")!= null)
       {
               measureName = (String)request.getAttribute("measureName");
       }
       String freezeButton="";
        if (request.getAttribute("freezeButton")!= null)
       {
               freezeButton = (String)request.getAttribute("freezeButton");
       }
       String periodMsg="";
        if (request.getAttribute("periodMsg")!= null)
       {
               periodMsg = (String)request.getAttribute("periodMsg");
       }
       String targetEndDate ="";
        if (request.getAttribute("targetEndDate")!= null)
       {
               targetEndDate = (String)request.getAttribute("targetEndDate");
       }
       String targetStartDate = "";
        if (request.getAttribute("targetStartDate")!= null)
       {
               targetStartDate = (String)request.getAttribute("targetStartDate");
       }
       HashMap RestrictingTotal=new HashMap();
        if (request.getAttribute("RestrictingTotal")!= null)
       {
         RestrictingTotal = (HashMap)request.getAttribute("RestrictingTotal");
       }

       ////////////////////////////////////////////////////////////////////////.println(targetStartDate +"- in jsp  targetStartDate "+targetEndDate+" .. . ."+errorMessage);
       String basisVal=(String)request.getAttribute("basisVal");
       String maxTimeLevel =(String)request.getAttribute("maxTimeLevel");

       session.setAttribute("startRange",startRange);
       session.setAttribute("secAnalyze",secAnalyze);

       session.setAttribute("primaryAnalyze",primaryAnalyze);

       session.setAttribute("originalResult",originalResult);

       session.setAttribute("columnEdgeValuesA",columnEdgeValuesA);
       session.setAttribute("rowEdgeValuesA",rowEdgeValuesA);
       session.setAttribute("primParamEleId",primParamEleId);
       session.setAttribute("periodType",periodType);

       session.setAttribute("defDates",defDates);
       session.setAttribute("endRange",endRange);
       session.setAttribute("startPeriod",startPeriod);
       session.setAttribute("endPeriod",endPeriod);
       session.setAttribute("measureName",measureName);
       session.setAttribute("primParamEleId",primParamEleId);
       session.setAttribute("RestrictingTotal",RestrictingTotal);
       session.setAttribute("maxTimeLevel",maxTimeLevel);
       String colDrillUrl=(String)request.getAttribute("colDrillUrl");
       colDrillUrl=colDrillUrl.replace("&","!");

       //////////////////////////////////////////.println(rowEdgeValuesA+" columnEdgeValuesAl;l';' "+columnEdgeValuesA);
       //////////////////////////////////////////.println(" primaryAnalyze jsp "+primaryAnalyze);
       //////////////////////////////////////////.println(basisVal+" originalResult in jsp: "+originalResult+" periodType "+periodType);
       //////////////////////////////////////////.println(" RestrictingTotal "+RestrictingTotal);
      // ////////////////////////////////////////////////////////////////////////.println(startRange+" endRange "+endRange);//endPeriod
        String secAnVal="";
       for(int u=0;u<columnEdgeValuesA.size();u++)
           {
           secAnVal=secAnVal+","+columnEdgeValuesA.get(u).toString();
           }
       if(secAnVal.length()>0)
           secAnVal=secAnVal.substring(1);



%>


    <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="../../Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
    <table width="100%" class="ui-corner-all">
        <tr>
            <td valign="top" style="height:10px;width:10%" align="right">
<!--                <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |-->
<!--                <a href="#" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |
                <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
            </td>
        </tr>
    </table>

    <div id="targetDiv" class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width:100%;min-height:500px;max-height:100%">
        <table style="width:100%">
            <tr>
                <td>
                    <div style="height:auto" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                        View Target Data
                    </div>
                </td>
            </tr>
        </table>
        <form name="frmParameter" action="pbDisplayTarget.jsp" method="POST">
            <input type="hidden" name="TARGETID" id="TARGETID" value="<%=targetId%>">
            <input type="hidden" name="TIMELEVEL" id="TIMELEVEL" value="<%=minTimeLevel%>">
            <input type="hidden" name="minTimeLevel" id="minTimeLevel" value="<%=minTimeLevel%>">
            <input type="hidden" name="primParamEleId" id="primParamEleId" value="<%=primParamEleId%>">
            <input type="hidden" name="periodType" id="periodType" value="<%=periodType%>">
            <input type="hidden" name="primaryAnalyze" id="primaryAnalyze" value="<%=primaryAnalyze%>">
            <input type="hidden" name="secAnalyze" id="secAnalyze" value="<%=secAnalyze%>">
            <input type="hidden" name="targetStartDate" id="targetStartDate" value="<%=targetStartDate%>">
            <input type="hidden" name="targetEndDate" id="targetEndDate" value="<%=targetEndDate%>">
            <input type="hidden" name="colDrillUrl" id="colDrillUrl" value="<%=colDrillUrl%>">
            <input type="hidden" name="maxTimeLevel" id="maxTimeLevel" value="<%=maxTimeLevel%>">

            <input type="hidden" name="secAnVal" id="secAnVal" value="<%=secAnVal%>">

            <table style="width:100%" valign="top">
                <tr valign="top">
                    <td valign="top">
                        <div class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">&nbsp;<b>Parameters Region</b></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table id="targetTable">
                            <%=targetParamSectionDisplay%>
                        </table>
                    </td>
                </tr>
                <tr valign="top">
                    <td valign="top">
                        <div class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">&nbsp;<b>Data Region&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        
                       
                        -<a href="javascript:void(0)" onclick="javascript:gotoEditData()">Edit Data </a>-<a href="javascript:void(0)" onclick="javascript:comeFromExcel()">Edit Using Excel Sheet</a></b></div><%-- onclick="javascript:gotoExcel()"> Download To Excel </a> &nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="javascript:comeFromExcel()"> Upload From Excel</a> --%>

                    </td>
                </tr>
                <tr>
                    <td valign="top" align="left">
                        <table align="left" class="tablesorter" id="target" border="0" cellspacing="1" cellpadding="0" style="width:auto">
                            <%=targetDataRegionDisplay%>
                        </table>
                        <input type="hidden" name="previousValue" id="previousValue">
                    </td>
                </tr>
                <tr valign="top" >
                    <td valign="top" width="100%">
                        <div class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">&nbsp;<b>Graph Region</b></div>
                    </td>
                </tr>
                <tr>
                    <td><center>
                        <table>
                            <tr>
                                <td>

                                         <div id="graphsFrame">
                                        </div>

                                </td>
                            </tr>
                    </table></center>
                    </td>
                </tr>
            </table>
            </form>
            <br>
            <form name="frmParameternew" method="post">

                <table>
                    <tr>
                        <td>
                            <input type="button" class="navtitle-hover"  value="Targets Home" onclick="goToTargetsHome2()">
                           
                            <input type="button" class="navtitle-hover"  value="Show Graph" onclick="showGraph2()">
                           <%--<Td><Input type="BUTTON" class="btn" value="Copy Data" onclick='copyData("<%=targetStartDate%>","<%=targetEndDate%>")'> --%>

                        </td>
                    </tr>
                </table>

            </form>
            <Br/>
            <span style="color:gray"><%=periodMsg%></span>
            <Br/>
             <Br/>
              <span style="color:gray"><%=dateMeassage%></span>
            <Br/>
             <Br/>
                <span style="color:gray"><%=errorMessage%></span>
            <Br/>
            <Br/>

               <span style="color:gray"><%=overAllMessage%></span>
            <Br/>
            <Br/>


        </div>
        <div id="fade" class="black_overlay"></div>
        <div id="uploadexcel" name="uploadexcel" style="display:none;"  class="white_content">
            <iframe id="uploadframe" name="uploadframe" frameborder="0" marginheight="0" marginwidth="0" src='#' style="height:300px;width:300px"></iframe>
        </div>


        <br>
        <div class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
            <center style="background-color:#bdbdbd"><font style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright &copy 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
        </div>
    </body>
</html>
