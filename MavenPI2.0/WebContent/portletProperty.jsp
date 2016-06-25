<%-- 
    Document   : portletProperty
    Created on : 9 May, 2011, 10:04:33 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String PortletID = request.getParameter("PortletID");
            String portalID = request.getParameter("portalID");
            String graphType = request.getParameter("graphType");
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <style type="text/css">
            *{font : 11px verdana}
        </style>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
            function LTrim( value ) {

                var re = /\s*((\S+\s*)*)/;
                return value.replace(re, "$1");

            }

        // Removes ending whitespaces
        function RTrim( value ) {

            var re = /((\s*\S+)*)\s*/;
            return value.replace(re, "$1");

        }

        // Removes leading and ending whitespaces
        function trim( value ) {

            return LTrim(RTrim(value));
        }
        $(document).ready(function(){
            var PortletID='<%=PortletID%>'
            var portalID='<%=portalID%>'
            $.get("<%= request.getContextPath()%>/portalViewer.do?portalBy=getPortletProperty&PortletID="+PortletID+'&portalID='+portalID,function(data){
                //alert("data\t"+data)
                var datajson=eval('('+data+')')
                if(trim(data)!=""){
                    if(datajson.showLabels==true)
                        $('input[name=showLabels]').attr('checked', true);
                    if(datajson.numberFormat!="")
                        $("#nbrFormat").val(datajson.numberFormat)
                    if(datajson.symbol !="")
                        $("#graphSymbol").val(datajson.symbol)
                    if(datajson.minMaxRange==true)
                        $('input[name=showMinMaxRange]').attr('checked', true);
                    if(datajson.showGT=='Y')
                        $('input[name=showGT]').attr('checked', true);
                    if(datajson.showLegends=='Y')
                        $('input[name=showLegends]').attr('checked', true);
                    if(datajson.graphGridLines=='Y')
                        $('input[name=graphGridLines]').attr('checked', true);
                    if(datajson.graphLegendLoc !='')
                        $('#graphLegendLoc').val(datajson.graphLegendLoc)
                    if(datajson.graphDisplayRows !='')
                        $('#graphDisplayRows').val(datajson.graphDisplayRows)
                    if(datajson.grpSize !='')
                        $('#grpSize').val(datajson.grpSize)

                }
            });


        });

        function portletPropertySave(){
            var showLegendsVar='N'
            var graphGridLinesvar='N'
            var showGTvar='N'
            var showMinMaxRangevar='false'
            var showLabelsvar='false'
            var PortletID='<%=PortletID%>'
            var portalID='<%=portalID%>'
            var graphT='<%=graphType %>'
            if ($('#showLegends').attr('checked'))
                showLegendsVar='Y'
            if ($('#graphGridLines').attr('checked'))
                graphGridLinesvar='Y'
            if($('#showGTvar').attr('checked'))
                showGTvar='Y'
            if($('#showMinMaxRange').attr('checked'))
                showMinMaxRangevar='true'
            if($('#showLabels').attr('checked'))
                showLabelsvar='true'
            var rowEdgeParams="";
            var colEdgeParams="";
            var REPNames="";
            var rowParamIdObj=""
            $.post("<%= request.getContextPath()%>/portalViewer.do?portalBy=savePortletProperties&showLegendsVar="+showLegendsVar+'&graphGridLinesvar='+graphGridLinesvar+'&showGTvar='+showGTvar+'&showLabelsvar='+showLabelsvar+'&showMinMaxRangevar='+showMinMaxRangevar+'&PortletID='+PortletID+'&portalID='+portalID,$("#portletPropertyForm").serialize(),function(data){

                if(data=='true')
                  //  alert("chkREP-"+PortletID+"-"+portalID)
                    var rowParamIdObj=parent.document.getElementsByName("chkREP-"+PortletID+"-"+portalID);
               // alert("length\t"+rowParamIdObj.length)
                for(var i=0;i<rowParamIdObj.length;i++){
                    if(rowParamIdObj[i].checked){
                        rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                        REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
                    }
                }
                if(rowEdgeParams!=""){
                    rowEdgeParams=rowEdgeParams.substring(1);
                    REPNames=REPNames.substring(1);
                }
               // alert("rowEdgeParams\t"+rowEdgeParams)
                parent.getPortletDetails(PortletID, rowEdgeParams, '', '', graphT, portalID)
                parent.$("#graphPropertyDiv").dialog('close')
            });

        }
        </script>
    </head>
    <body>

        <form action="" name="portletPropertyForm" method="post" id="portletPropertyForm">

            <table width="100%" border="0">
                <tbody>

                    <tr>
                        <td>Legends</td>
                        <td>

                            <input checked="checked" name="showLegends" id="showLegends" value="Y"  type="checkbox">

                        </td>

                        <td>GridLines</td>
                        <td>

                            <input checked="checked" name="graphGridLines" id="graphGridLines" value="Y"  type="checkbox">

                        </td>

                    </tr>

                    <tr>
                        <td>Grand Total &amp; Average </td>
                        <td>
                            <input name="showGT" id="showGT" value="N" onclick="showGTFun();" type="checkbox">

                        </td>

                        <td>Min/Max Range</td>
                        <td>
                            <input name="showMinMaxRange" id="showMinMaxRange" value="N"  type="checkbox">

                        </td>
                    </tr>


                    <tr>
                        <td>Number Format</td>
                        <td>
                            <select name="nbrFormat" id="nbrFormat" style="width: 150px;">

                                <option selected="selected" value="">Absolute</option>

                                <option value="K">Thousands(K)</option>

                                <option value="M">Millions(M)</option>

                            </select>
                        </td>

                        <td>Legend Location</td>
                        <td>
                            <select name="graphLegendLoc" id="graphLegendLoc" style="width: 150px;">

                                <option selected="selected" value="Bottom">Bottom</option>

                                <option value="Top">Top</option>

                                <option value="Left">Left</option>

                                <option value="Right">Right</option>

                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>Swap Graph Analysis</td>
                        <td>
                            <select name="SwapColumn" id="SwapColumn" style="width: 150px;">

                                <option selected="selected" value="true">Basis: View By</option>

                                <option value="false">Basis: Measures</option>

                            </select>
                        </td>

                        <td>Symbols</td>
                        <td>
                            <select name="graphSymbol" id="graphSymbol" style="width: 150px;">

                                <option value=" "> </option>

                                <option value="$"> $ </option>

                                <option value="Rs"> Rs </option>

                                <option value="Euro"> Euro </option>

                                <option value="Yen"> Yen </option>

                            </select>
                        </td>
                    </tr>


                    <tr>
                        <td>Graph Sizes</td>
                        <td>
                            <select name="grpSize" id="grpSize" style="width: 150px;">

                                <option value="Large">Large</option>

                                <option value="Small">Small</option>

                                <option selected="selected" value="Medium">Medium</option>

                            </select>
                        </td>

                        <td>Display Rows</td>
                        <td>
                            <select name="graphDisplayRows" id="graphDisplayRows" style="width: 150px;">

                                <option value="5">5</option>

                                <option selected="selected" value="10">10</option>

                                <option value="15">15</option>

                                <option value="25">25</option>

                                <option value="50">50</option>

                                <option value="All">All</option>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Show Labels</td>
                        <td>

                            <input name="showLabels" id="showLabels" value="false"  type="checkbox">

                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" height="10px" align="center"></td>
                    </tr>


                </tbody>
                <tfoot>

                    <tr>
                        <td colspan="4" align="center">
                            <input name="Save" value="Done" class="navtitle-hover" onclick="portletPropertySave()" type="button">
                        </td>
                    </tr>

                </tfoot>

            </table>

        </form>

    </body>
</html>
