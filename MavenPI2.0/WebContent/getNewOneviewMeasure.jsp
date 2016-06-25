<%--
    Document   : getOneViewMeasures
    Created on : June 19, 2012, 5:10:01 PM
    Author     : Surender
--%>



<%@page import="prg.db.OnceViewContainer,java.util.ArrayList,java.util.List"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String eletIds = (String)session.getAttribute("elementIds");
            String elementNames = (String)session.getAttribute("elementNames");
            String orgelementNames = (String)session.getAttribute("orgelementNames");
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard Kpis</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
               <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>


        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>-->
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/javascript/OneViewJS.js"></script>
        <%

                    String KpiData = "";


                    if (request.getAttribute("oneviewMeasu") != null) {
                        KpiData = String.valueOf(request.getAttribute("oneviewMeasu"));
                    }

        %>
        <script type="text/javascript">

             $(document).ready(function() {
        // put all your jQuery goodness in here.
             });
            var y="";
            var xmlHttp;
            var ctxPath;
            var kpielements;
            var kpinames;
            <% if(elementNames !=null && eletIds != null && orgelementNames!=null){ %>
                kpielements = '<%=orgelementNames%>';
                kpinames = '<%=eletIds%>';
                <%}%>

            function saveKpis(dayDiff,kpiType,divId){
                dispKpis(dayDiff,kpiType,divId);
            }

            function getRollingData(id,name,roleId,dayVal){
                    parent.$("#trendGraphDiv").dialog('open')
                 var value= parent.document.getElementById("trendGraphDiv");
                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getRollingDataForIcal&measureName='+name+'&measureId='+id+'&busroleId='+roleId+'&noofDays='+dayVal+'&iMonth='+parent.imonth+'&iyear='+parent.iyear,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){
                        parent.$("#trendGraphDiv").html(data);
                    }
                });
                }




var measurName;
   var measurId;
    var checkval;
    var colIds;
    var measList=new Array();
            function buildMeasures(){
            alert("hi")
                parent.$("#calendrMeasureID").dialog('close');
                var rowViewByArray=new Array();
                var icalIdArray = new Array();
                var icalNamesArray = new Array();
                var ul = document.getElementById("sortable");
                if(ul!=undefined || ul!=null){
                     colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        var test=parent.dashletId;
//                        if(test==undefined){
//                            rowViewByArray.push(colIds[colIds.length-1].id);
//                        }else{
                        for(var i=0;i<colIds.length;i++){
                            //                            var value=colIds[i].id.indexof("^","")
                            //                            alert("inxex"+value)
                            icalIdArray.push(colIds[i].id.split("^")[1]);
                            icalNamesArray.push(colIds[i].id.split("^")[0]);
                            rowViewByArray.push(colIds[i].id);
                            //                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }
//                        }

                    }
                }
                var test=parent.dashletId;
               if(test==undefined){
                   var comVal = parent.$("#comparision").val();
                   var htm = '';
                   for(var d=0;d<31;d++){
                   parent.$("#t"+d).html(htm);
                   }
                   checkval = false;
                         if(parent.$("#viewDailId").is(':checked')){
                             checkval = true;
                         }
                   for(var l=0;l<colIds.length;l++){
//                    var regionId=parent.dashletId;
//                    var regionName=parent.regionName;
//                    var oneViewIdValue=parent.oneViewIdValue;
                    var testvalue=rowViewByArray[l];
                    var pName = testvalue.replace("_"," ","gi");
                    var index=pName.indexOf('^', 0);
                    var measurName=testvalue.substring(0, index);
                    var measurId=testvalue.substring(index+1, testvalue.length);

//                    var div=$("#"+test);
//                    parent.$("#readdId"+parent.colNumber).css({'display':''})
//                    var roleId = parent.$("#roleType").val();
                     }
                         $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=measureDisplyinIcal&measName='+icalNamesArray+'&measId='+icalIdArray+'&roleId='+roleId+'&overWriteId='+parent.overWriteId,
                     function(data){
                     });
                     getIcalMeasures(icalNamesArray,icalIdArray,checkval,comVal);
                    }
                    else if(test!=undefined){

                   if(rowViewByArray.length==1){
                    var regionId=parent.dashletId;
                    var regionName=parent.regionName;
                    var oneViewIdValue=parent.oneViewIdValue;
                    var testvalue=rowViewByArray[0];
                    var pName = testvalue.replace("_"," ","gi")
                    var index=pName.indexOf('^', 0);
                    var measurName=testvalue.substring(0, index)
                    var measurId=testvalue.substring(index+1, testvalue.length)
                    var test=parent.dashletId;
                    var div=$("#"+test);
                    parent.$("#readdId"+parent.colNumber).css({'display':''})
                    var roleId = parent.$("#roleType").val();
                     parent.$("#measureDialogId").dialog('close');
                      parent.$("#regionId"+parent.colNumber).show();
                       if(parent.document.getElementById("readdDivId"+parent.colNumber)!=null && parent.document.getElementById("readdDivId"+parent.colNumber).style.display=='block'){
                         parent.$("#readdDivId"+parent.colNumber).toggle(500);
                     }
//                     parent.document.getElementById("GrpTyp"+regionId.substr(regionId.length-1, regionId.length)).style.display='none'
                      parent.document.getElementById(regionId).innerHTML='<center><img id="imgId" src="<%=request.getContextPath()%>/images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                    $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getOneViewMeasureValue&measureName='+encodeURIComponent(measurName)+'&measureId='+measurId+'&height='+parent.height+'&width='+parent.width+'&oneViewIdValue='+oneViewIdValue+'&divId='+parent.colNumber+'&colSp='+parent.colSp+'rowSp='+parent.rowSp+'&busroleId='+parent.busroleId+'&measureType=measures',
                    function(data){
                        //                          var htmltest="";
                        //                          htmltest+="<center><table align='center' border='1' height='"+parent.height+"' width='"+parent.width+"'><tr><td colspan='1' width='650px' height='100px' align='center' style='font-size:20pt'>100,000</td><td colspan='1' width='650px' height='100px' style=' cursor: pointer;  align:center;'><img width='100px'id=\"tdImage\" src=\"<%=request.getContextPath()%>/images/Green Arrow.jpg\" onclick=\"closeOldPortlet()\"/><img width='100px' align='right' id=\"tdImage\" src=\"<%=request.getContextPath()%>/images/Red Arrow.jpeg\" onclick=\"closeOldPortlet()\"/></td></tr><tr><td colspan='2' width='1300px' height='100px' align='center'></td></tr><table></center>";
                        if(data!='ApplyCurrent'){
                            parent.document.getElementById(regionId).innerHTML ="";
                            parent.$(regionId).css({'font-size':'15pt'});
                            //                       var jsonVal=eval('('+data+')');
                            //                       var html="";
                            //                       var curValue=jsonVal.MeasureValues[0];
                            //                       var priorval=jsonVal.MeasureValues[1];
                            //                       var chePer=jsonVal.MeasureValues[2];
                            //                         if(curValue > priorval){
                            //                             html+="<center><table align='center' style='overflow:auto;' height='"+parent.height+"' width='"+parent.width+"'><tr><td colspan='1'  style='font-size:20pt' >"+curValue+"</td><td colspan='1' align='center'><img  style='border-left:medium hidden;border-top:medium hidden;' width='100' height='100' id=\"tdImage\" src=\"<%=request.getContextPath()%>/images/Green Arrow.jpg\" onclick=\"closeOldPortlet()\"/></td></tr><tr><td colspan='2' width='1300px' height='100px' align='center'><a ><table border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;border-top:medium hidden;'><tr><td style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#90EE90'>"+measurName+"<br>Increased by "+chePer+"%</td></tr></table></a></td></tr></table></center>";
                            //                         }
                            //                         else{
                            //                             html+="<center><table align='center' style='overflow:auto;' height='"+parent.height+"' width='"+parent.width+"'><tr><td colspan='1'   style='font-size:20pt' >"+curValue+"</td><td colspan='1' align='center'><img  style='border-left:medium hidden;border-top:medium hidden;' width='100' height='100' id=\"tdImage\" src=\"<%=request.getContextPath()%>/images/Red Arrow.jpeg\" onclick=\"closeOldPortlet()\"/></td></tr><tr><td colspan='2' width='1300px' height='100px' align='center'><a ><table border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;border-top:medium hidden;'><tr><td style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#C71585'>"+measurName+"<br>Descreased by "+chePer+"%</td></tr></table></a></td></tr></table></center>";
                            //                         }
//                            parent.document.getElementById(regionId).innerHTML='<center><img id="imgId" src="<%=request.getContextPath()%>/images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                            parent.document.getElementById(regionId).innerHTML = data;
                            parent.document.getElementById(regionName).innerHTML=testvalue.substring(0, index);
                            var width =parent.width;
                            var oneviewName =parent.oneviewName;
//                            parent.document.getElementById("GrpTyp"+regionId.substr(regionId.length-1, regionId.length)).style.display='none'
                            checknamelenght(oneViewIdValue,measurName,regionId,regionName,width,oneviewName);


                        }

                        else{
                            parent.$("#imgId").remove('img');
                            alert("No Data for this measure")
                        }
                    });

                }
                   else{
                    alert("Please Select only one Measure!");
                }

                }

                parent.$("#calendar").show();

            }
            function getIcalMeasures(measurName,measurId,checkval,compareVal){
            parent.$("#loadingmetadata").show();
                $.ajax({url:'<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getMeasureGraphtest&measureName='+measurName+'&measureId='+measurId+'&busroleId='+parent.busroleId+'&iMonth='+parent.imonth+'&iYear='+parent.iyear+'&days='+parent.nodays+'&checkval='+checkval+'&compareVal='+compareVal,
                         type: 'GET',
                async: false,
                cache: false,
                timeout: 30000,
                success:function(data){
                            parent.$("#loadingmetadata").hide();
                            var jsonavle = eval('('+data+')');
                            var j=0;
                            var count=parent.rowsno;
                             var value=0;
                            var k=parent.nodays;
                             if(parent.$("#TillCurrentDate").is(':checked'))
                                 {k=parent.current}
                                 else if(parent.$("#TillpreviousDate").is(':checked'))
                                     {k=parent.current-1}
                                     else if(parent.$("#NextOneWeek").is(':checked'))
                                         {k=parent.current+7}
                                         else if(parent.$("#Next15Days").is(':checked'))
                                         {k=parent.current+15}
                                          else
                                         {k=parent.nodays;}


                            var d=0;
                            var jsonVare=eval('('+data+')')
                        var keys = [];
                        var jsonvalues = [];
                        for (var key in jsonVare) {
                            if (jsonVare.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }
                        for(var i=0;i<keys.length;i++)
                        {
                            var PM = false;
                            if(keys[i] != undefined && keys[i].indexOf("I")!=-1){
                                PM = true;
                            }
                            jsonvalues = jsonVare[keys[i]];
                            for(var j=0;j<k;j++){
                                var html=" <table width='100%'>";
                                    var m="";
                                    var sumValue=0.0;
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        m = 'hover2';
                                    }else if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                    }else  if(jsonvalues[j] != undefined && jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover';
                                    }
//                                   if(jsonvalues[j] != undefined){
//                                    html+="<tr ><td class='"+m+"' width='70%'>"+measurName[i]+"</td>";
//                                   }else{
                                     //html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'>"+measurName[i]+"</td>";
                                     html+="<tr ><td class='hover' style='font-size: 9pt;' width='70%'><a class='hover' href='javascript:void(0)' onclick=\"getRollingData('"+keys[i].replace("I", "", "gi")+"','"+measurName[i]+"','"+parent.busroleId+"','"+j+"')\">"+measurName[i]+"</a></td>";
                                 //  }
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("N")!=-1){
                                        jsonvalues[j] = jsonvalues[j].replace("N", "", "gi")
                                        m = 'hover2';
                                        if(PM == true){
                                            parent.$("#tr"+j).css("background-color", "#FF1414");
                                        }
                                    }else
                                    if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("P")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("P", "", "gi")
                                        if(PM == true){
                                            parent.$("#tr"+j).css("background-color", "#8AFB17");
                                        }
                                    }else if(jsonvalues[j] != undefined &&jsonvalues[j].indexOf("V")!=-1){
                                        m = 'hover1';
                                        jsonvalues[j] = jsonvalues[j].replace("V", "", "gi")
                                    }
                                   if(checkval == true){
                                   if(jsonvalues[j]!=undefined){
                                       if(PM == false){
                                   html+="<td class='"+m+"' width='30%' style='font-weight: bold' align='right'>"+jsonvalues[j]+"</td></tr>";
                                       }else{
                                          html+="<td class='hover' width='30%' style='font-weight: bold' align='right'>"+jsonvalues[j]+"</td></tr>";
                                       }
                                   }
                                   else
                                   {
                                       if(PM == false){
                                       html+="<td class='"+m+"' width='50%' align='right'>-</td></tr>";
                                       }else{
                                           html+="<td class='hover' width='50%' align='right'>-</td></tr>";
                                       }
                                    }
                                    }
                                  else if(jsonvalues[j]!=undefined){
                                      var length = 2;
                                       for(d=j;d>=0;d--)
                                           {
                                               var l = "";
                                               var lenarray = new Array();
                                               l = jsonvalues[d].indexOf("K");
                                               if(jsonvalues[d].indexOf(".")){
                                                   lenarray = jsonvalues[d].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}
                                               if(jsonvalues[d].indexOf("K")!= -1){
                                                   l = "K";
                                               }else if(jsonvalues[d].indexOf("L")!= -1){
                                                   l = "L";
                                               }else if(jsonvalues[d].indexOf("M")!= -1){
                                                   l = "M";
                                               }else if(jsonvalues[d].indexOf("r")!= -1){
                                                   l = "Cr";
                                               }
                                               sumValue = parseFloat(sumValue)+parseFloat(jsonvalues[d].replace(",", "", "gi")) ;
                                           }value=sumValue;
                                           if(sumValue.toString().indexOf(".") !=-1){
                                               lenarray = sumValue.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               sumValue = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                           sumValue = addCommas(sumValue);
                                           if(l == "r")
                                           sumValue = sumValue+"Cr";
                                       else if(l == "M" || l=="K" || l == "L")
                                           sumValue = sumValue+l;
                                       else
                                           sumValue = sumValue;
                                           if(PM == false){
                                           html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }else{
                                               html+="<td class='hover' width='50%' style='font-weight: bold' align='right'>"+sumValue+"</td></tr>";
                                           }

                                   }


                            else {
                                var l = "";
                                               var lenarray = new Array();
                                               if(jsonvalues[j] != undefined){
                                               l = jsonvalues[j].indexOf("K");
                                               if(jsonvalues[j].indexOf(".")){
                                                   lenarray = jsonvalues[j].split(".");
                                                   if(lenarray[1] != undefined){
                                                   if(lenarray[1].indexOf("K") != -1 ||lenarray[1].indexOf("L")!=-1 ||lenarray[1].indexOf("M")!=-1){
                                                       length = lenarray[1].length;
                                                       length = length -1;
                                                   }else if(lenarray[1].indexOf("r") != -1){
                                                       length = lenarray[1].length;
                                                       length = length -2;
                                                   }else{
                                                       length = lenarray[1].length;
                                                   }
                                               }}}
                                           if(value.toString().indexOf(".") !=-1){
                                               lenarray = value.toString().split(".");
                                               if(lenarray[1] != undefined){
                                               lenarray[1] = lenarray[1].substr(0, length);
                                               value = lenarray[0]+"."+lenarray[1];
                                               }
                                           }
                                value = addCommas(value);
                                if(PM == false){
                                  html+="<td class='"+m+"' width='50%' style='font-weight: bold' align='right'>"+value+"</td></tr>";
                                }else{
                                    html+="<td class='hover' width='50%' style='font-weight: bold' align='right'>"+value+"</td></tr>";
                                }
                            }
                            html+="</table>";
                                    parent.$("#t"+j).append(html);
                            }
                        }  }
                        });

            }

            function cancelKpi(){
                parent.cancelRepKpi();
            }

            $(document).ready(function() {
                $("#kpiTree").treeview({
                    animated: "normal",
                    unique:true
                });


                $('ul#kpiTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#kpiTree',
                    loaderText: '',
                    delay: 100
                })
                $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView(action, el, pos);
                });
                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });
            });

            $(function() {
                var dragKpi=$('#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span')
                var dropKpis=$('#draggableKpis');

                $(dragKpi).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropKpis).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var kpi=ui.draggable.html();
                        createKpis(ui.draggable.html(),ui.draggable.attr('id'));
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }



        </script>

        <style type="text/css">
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#EAF2F7;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .hover
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                 color: black;
            }
            .hover1
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                color: green;
            }
            .hover2
            {
                font-family: Calibri, Calibri, Calibri, sans-serif;
                color: red;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
            *{font:11px verdana}
        </style>

        <script type="text/javascript">


            function getElementDetails()
            {
                var kpiid;
                var Eleid;
                if(kpielements != null && kpinames !=null){
                    kpiid=kpielements;
                    Eleid=kpinames;
                }


                var kpiidarray= kpiid.split(",");
                var elearray=Eleid.split(",");
                var part_num=0;
                while (part_num < kpiidarray.length)
                {
                    createDynamicKpis(kpiidarray[part_num],elearray[part_num])
                    part_num+=1;
                }
            }

            function createDynamicKpis(kpiName,elementName){
                var i=0;
                var parentUL=document.getElementById("sortable");
                var parentDiv=parent.document.getElementById("editDispKpi");
                var x=kpiArray.toString();
                if(x.match(elementName)==null){
                    kpiArray.push(kpiName+"^"+elementName)
                    var childLI=document.createElement("li");
                    childLI.id=kpiName+"^"+elementName;
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=kpiName+i;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    var a=document.createElement("a");
                    a.href="javascript:deleteKpi('"+kpiName+'^'+elementName+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.style.color='black';
                    cell2.innerHTML=kpiName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                    i++;
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }
            function deleteKpi(kpiName){
        var LiObj=document.getElementById(kpiName);
        var tab=LiObj.getElementsByTagName("table");
        var row=tab[0].rows;
        var cells=row[0].cells;

        var parentUL=document.getElementById("sortable");
        parentUL.removeChild(LiObj);
        var x=(LiObj.id);
        var i=0;
        for(i=0;i<kpiArray.length;i++){
            if(kpiArray[i]==x)
                kpiArray.splice(i,1);
        }
        $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=deleteMeasures&kpiArray='+kpiArray,
                     function(data){
                     });
    }
        </script>
    </head>
    <body onload="getElementDetails()">


        <form name="myForm2" method="post" action="">
            <table style="width:100%;height:700px" border="solid black 1px" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Measures from below</font></div>
                        <div class="masterDiv" style="height:700px;overflow-y:auto">
                            <ul id="kpiTree" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="kpis" style="float:left;margin-left:-10%" >
                                        <%=KpiData%>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    
                </tr>
            </table>
        </form>

        <br/>
<!--        <center>
            <input type="button"  class="navtitle-hover" style="width:auto" value="Done" onclick="buildMeasures()">
            <%--<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelKpi()">--%>
        </center>-->

        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
            <table>
                <tr>
                    <td id="value"></td>
                </tr>
            </table>
        </div>
    </body>
</html>
