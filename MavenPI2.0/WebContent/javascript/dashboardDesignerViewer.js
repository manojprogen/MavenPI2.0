

        function showGraphProperties(dashletId ,dashBoardId, refReportId, graphId, kpiMasterId, dispSequence,dispType,dashletName,contextPath,fromDesigner)
        {
//            alert("In showGraphProperties");
              var divIdObj =parent.document.getElementById("graphPropertiesDiv");
             divIdObj.innerHTML ='<iframe  frameborder=\"0\" id=\"graphPropertiesFrame\" src=\"'+contextPath+'/TableDisplay/PbGraphProperties.jsp?reportId='+dashBoardId+'&graphId='+graphId+'&from=dashboard&dashletId='+dashletId+'&kpiMastId='+kpiMasterId+'&refReportId='+refReportId+'&displaySeq='+dispSequence+'&dispType='+dispType+'&dashName='+dashletName+'&fromDesigner='+fromDesigner+'\" width=\"100%\" height=\"100%\" name=\"graphPropertiesFrame\"></iframe>';
//          var source=contextPath+"/TableDisplay/PbGraphProperties.jsp?reportId="+dashBoardId+'&graphId='+graphId+'&from=dashboard&dashletId='+dashletId+'&kpiMastId='+kpiMasterId+'&refReportId='+refReportId+'&displaySeq='+dispSequence+'&dispType='+dispType+'&dashName='+dashletName+'&fromDesigner='+fromDesigner;
//          var frameObj=document.getElementById("graphPropertiesFrame")
//          frameObj.src=source;
          parent.$("#graphPropertiesDiv").dialog("open");
        }


        function addMoreKpis(dashBoardId,dashletId,kpiMasterId,folderDetails,kpiType){
            var kpidashid= ""+dashletId;
            document.getElementById("folderId").value=folderDetails
            document.getElementById("reportId").value=dashBoardId
            var frameObj=document.getElementById("kpidataDispmem");
            var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpis&foldersIds="+folderDetails+'&divId='+kpidashid+'&kpiType='+kpiType+'&dashboardId='+dashBoardId+'&kpiMasterId='+kpiMasterId+"&dashletId="+dashletId;
            frameObj.src=source;
            $("#kpisDialog").dialog('option','title','Edit KPI')
            $("#kpisDialog").dialog('open');


        }


            function gotoInsight(elementId, kpiMasterId){
                var dashBoardId =document.getElementById("dbrdId").value;
//                alert("dashBoardId"+dashBoardId)
                document.forms.frmParameter.action="dashboardViewer.do?reportBy=getKPIInsightViewerPage&elementId="+elementId+"&kpiMasterId="+kpiMasterId+"&dashBoardId="+dashBoardId;
                document.forms.frmParameter.submit();
            }


            function clearDashlet(dashletId,numOfDashlets,row,col,rowSpan,colSpan)
            {
//                alert("clear in dashboardDesignerViewer");
                var dashBoardId = document.getElementById("dbrdId").value;
                var div=document.getElementById(dashletId);
                var ctxPath=document.getElementById("h").value;
//                alert(ctxPath)
//                alert(div);
                var regionHtml="<div id=\"Dashlets-"+dashletId+"\"class='portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all' style=\"width:100%; height:100%\">";
                regionHtml+="<table width='100%'>";
                regionHtml+="<tr><td align='right'><img id=\"tdImage\" src=\""+ctxPath+"/images/cross.png\"  onclick=\"closeOldPortlet('Dashlets-"+dashletId+"',"+dashletId+")\"/></td></tr>";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+dashletId+",'BasicTarget')\" style='font-size:8pt;'>Add Basic Target KPI</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+dashletId+",'Basic')\" style='font-size:8pt;'>Add Basic KPI</a></td></tr>&nbsp";
//                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+dashletId+",'Kpi')\" style='font-size:8pt;'>Add KPI</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+dashletId+",'Standard')\" style='font-size:8pt;'>Add Std KPI</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+dashletId+",'Target')\" style='font-size:8pt;'>Add Target KPI</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+dashletId+",'MultiPeriod')\" style='font-size:8pt;'>Add MultiPeriod KPI</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'MultiPeriodCurrentPrior')\" style='font-size:8pt;'>Add MultiPeriod with current&prior</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"createDbrdGraphs("+dashletId+")\" style='font-size:8pt;'>Add Graph</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showGraphs("+dashletId+")\" style='font-size:8pt;'>Add Graph from Report</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"getDbrdGraphColumns(null,'table')\" style='font-size:8pt;'>Add Table</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showKpiGraphs("+dashletId+")\" style='font-size:8pt;'>Add KPI Graph</a></td></tr>&nbsp";
//                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showmap("+dashletId+")\" style='font-size:8pt;'>Add Map</a></td></tr>&nbsp";
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a  onclick=\"mergeRow("+dashletId+","+numOfDashlets+")\" style='font-size:8pt;'>Merge Row</a></td></tr>&nbsp";;
                regionHtml+="<tr width='100%'><td width='100%' align='center'><a  onclick=\"mergeColumn("+dashletId+","+numOfDashlets+")\" style='font-size:8pt;'>Merge Column</a></td></tr>&nbsp";;
                regionHtml+="</table></div></td>";
                div.innerHTML=regionHtml;
                
                
                $.ajax({
                    url: 'dashboardTemplateViewerAction.do?templateParam2=clearDashlet&dashboardId='+dashBoardId+'&dashletId='+dashletId+'&row='+row+'&col='+col+'&rowSpan='+rowSpan+'&colSpan='+colSpan,
                    success:function(data){
                    }
                });

            }

            function closeOldPortlet(ids, dashletId){
                    var dashboardId = document.getElementById("dbrdId").value;
                    var confirmText= confirm("Are you sure you want to delete");
                    if(confirmText==true){
                        var delDashlet=document.getElementById(dashletId);
                        delDashlet.parentNode.removeChild(delDashlet);
                        $.ajax({
                            url: 'dashboardTemplateViewerAction.do?templateParam2=deleteDbrdGraphs&dashboardId='+dashboardId+'&dashletId='+dashletId,
                            success: function(data){
                                if (data != null && data != "")
                                    alert("Region is Deleted");
                            }
                        });
                    }
            }
