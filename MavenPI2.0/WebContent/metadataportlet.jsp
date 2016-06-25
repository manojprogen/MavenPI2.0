<%--
    Document   : metadataScheduler.jsp
    Created on : 12 Jan, 2011, 5:03:59 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb,utils.db.ProgenConnection,java.util.Calendar,java.util.ArrayList,com.progen.portal.PortalDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);
             PortalDAO dao = new PortalDAO();
             PbReturnObject retObj = dao.getPortlets();
            ArrayList val = new ArrayList();
             String userId="";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }

String contextPath=request.getContextPath();
%>

  <html>
      <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
      <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
      <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
       <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
       <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>-->
      <script type="text/javascript" src="<%=contextPath%>/TableDisplay/overlib.js"></script>
      <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
        
        <script type="text/javascript">
           $("#tablePortlet").tablesorter({headers : {0:{sorter:false}}});
            $("#tablePortlet").tablesorterPager({container: $('#pagerPortlet')});
              $("#allportel").val(length);

                        var options, a;
        options = {
                    serviceUrl:'studioAction.do?studioParam=autoSuggest&tab=Portals&userId=<%=userId%>',
                    delimiter: /(,)\s*/, // regex or character
                    minChars:2,
                    deferRequestBy: 500, //miliseconds
                    maxHeight:400,
                    width:450,
                    zIndex: 9999,
                    noCache: false

                };
                a = $("#srchTextPortlet").autocomplete(options);
                $("#srchTextPortlet").keyup(function(event){
                    if(event.keyCode == 13){
                       // searchVals();

                    }
        });
        });
        </script>
     

    </head>
    <body id="mainBody">
    <form name="portletForm" id="portletForm"  method="post" action="" >

            <table  border="0px solid" width="100%">
                <tr valign="top">
                    <td align="left" width="12%">
                        <div id="pagerPortlet" class="pager" align="left" >
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                            <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allportel" value="<%=retObj.getRowCount()%>">All</option>
                            </select>
                        </div>
                    </td>
                    <td align="rigth" width="35%">
                        <input type="text" class="myTextbox3" id="srchTextPortlet" name="srchTextPortlet" align="middle" style="width:280px;height:20px;">
                          <input type="button" value="search" class="navtitle-hover" onclick="javascript:displayStudioItem('srchTextPortlet',<%=userId %>,'Portals','tablePortlet','<%=request.getContextPath()%>')" style="width:50px;height:20px;" />
                    </td>
                    <td align="right" width="38%">
                        <input type="button" value="Create" class="navtitle-hover" style="width:auto"  onclick="javascript:createPortlet()">
                        <%--<input type="button" value="Edit" class="navtitle-hover" style="width:auto"  onclick="javascript:editPortlet()">--%>
                        <%--<input type="button" value="Share" class="navtitle-hover" style="width:auto"  onclick="javascript:sharePortlet('<%=request.getContextPath()%>')">--%>
                        <input type="button" value="Delete" class="navtitle-hover" style="width:auto"  onclick="javascript:deletePortlets('<%=request.getContextPath()%>')">
                    </td>
                </tr>

            </table>

            <table align="center" id="tablePortlet" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
                <thead>
                    <tr>
                        <th nowrap><input type="checkbox" name="portletSelect" id="portletSelect" onclick="return selectAllReps()">Select All</th>
                        <th nowrap>Portlet Name</th>
                        <th nowrap>Portlet Description</th>
                        <th nowrap>Type</th>
                        <th nowrap>Updated On</th>
                        <th nowrap>Updated By</th>

                    </tr>
                </thead>
                <tbody>
                    <%int i = 0;
                                for (i = 0; i < retObj.getRowCount(); i++) {%>
                    <tr>
                        <td style="width:30px"><input type="checkbox" name="portletSelect" id="RepSelect<%=retObj.getFieldValueString(i,0)%>" value="<%=retObj.getFieldValueString(i, 0)%>" onclick="unselAll()"></td>
                        <td class="wordStyle" id="hyperRep" style="font-size:12px;cursor:pointer" onclick="javascript:viewPortlet('<%=retObj.getFieldValueString(i, 0)%>','portletPreviewDivinDesiner','<%=retObj.getFieldValueString(i, 1)%>')">
                            <%=retObj.getFieldValueString(i, 1)%>
                        </td>

                        <td>
                            <%=retObj.getFieldValueString(i, 2)%>
                        </td>
                        <td>
                            <%=retObj.getFieldValueString(i, 4)%>
                        </td>
                        <td>
                            <% Calendar calendar=Calendar.getInstance();
                            calendar.setTime(retObj.getFieldValueDate(i, 6));
                            %>
                            <%=(calendar.get(Calendar.MONTH) + 1)+"/"+calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.YEAR) %>
                        </td>
                        <td>
                            <%=retObj.getFieldValueString(i, 5)%>
                        </td>

                    </tr>
                    <%}%>
                </tbody>
            </table>
                <input type="hidden" name="currportletId" id="currportletId" value="">
                   </form>
                <div id="portletPreviewDivinDesinerDilog">
<!--                <div style="display: none;" title=""  id="portletPreviewDivinDesiner">

                </div>-->

                </div>
                   <script type="text/javascript">
        
       //
             function deletePortlets(ctxPath){

        var portletSelectObj=document.getElementsByName("portletSelect");
        var deleteportletids = new Array();
        for(var i=1;i<portletSelectObj.length;i++){
            if(portletSelectObj[i].checked){
                deleteportletids.push(portletSelectObj[i].value);
            }
            else{
                portletSelectObj[i].checked=false;
            }
        }

        if(deleteportletids.length!=0){

            var confirmDel=confirm("Do you want to delete the selected portlets,if any portlet use in portal level will also be deleted");
            if(confirmDel==true){
                $.post( ctxPath+'/portalViewer.do?portalBy=deletePortlets&portletids='+deleteportletids.toString(),$("#portletForm").serialize(), function(data){
                         document.forms.portletForm.action = ctxPath+"/portalViewer.do?portalBy=viewPortals";
                        document.forms.portletForm.submit();
                });
//                $.ajax({
//                    url: ctxPath+'/portalViewer.do?portalBy=deletePortlets&portletids='+deleteportletids.toString(),
//                    success: function(data){
//                        document.forms.portletForm.action = ctxPath+"/portalViewer.do?portalBy=viewPortals";
//                        document.forms.portletForm.submit();
//                    }
//                });
            }
        }
        else{
            alert("Please select portlets");
        }
    }
    function viewPortlet(portletid,divId,portlname){
  // alert(portlname)
   $("#portletPreviewDivinDesinerDilog").html("")
   //alert("sfafcv\t"+$("#portletPreviewDivinDesinerDilog").html())
 

        var varStr="<div style='display: none;' title='"+portlname+"'id='portletPreviewDivinDesiner'></div>"
        $("#portletPreviewDivinDesinerDilog").html(varStr)
           
     $("#portletPreviewDivinDesiner").dialog({
                    autoOpen: false,
                    height: 500,
                    width: 500,
                    position: 'justify',
                    modal: true,
                    title:portlname,
                   close: function(){
                       $("#portletPreviewDivinDesinerDilog").html("")
                        window.location.href =window.location.href
                   },
                    buttons: {
                 "Cancel": function() {
                     $(this).dialog("close");
                     $("#portletPreviewDivinDesinerDilog").html("")
                     window.location.href =window.location.href 
                 }

//                 ,
//                 "Rename": function() {
//              //   alert($(this).attr('title'));
//                     $(this).dialog("close");
//                 }
             }

                });

$("#ui-dialog-title-portletPreviewDivinDesiner").parent().hide();

    $("#portletPreviewDivinDesiner").dialog('open')
     $("#portletPreviewDivinDesiner  .ui-dialog-titlebar").hide();
// remove the title bar
   


                $.ajax({
                     url:"<%=request.getContextPath()%>/portalViewer.do?portalBy=portletPreview&Portletid="+portletid+"&callFrom=designer&portalID=-1",
                     success:function(data){
                         
                       $("#portletPreviewDivinDesiner").html("")
                       $("#portletPreviewDivinDesiner").html(data)

                     }
                 });
                
    }
    function selectAllReps()
            {
                var RepSelectObj=document.getElementsByName("portletSelect");
                for(var i=0;i<RepSelectObj.length;i++){
                    if(RepSelectObj[0].checked){
                        RepSelectObj[i].checked=true;
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
            }
         function displayPortalItem(id,userid,type,studioId,path)
     {
                var searchText=$("#"+id).val();

            if(searchText=="")
                alert("please enter a report name");
            else{
            var searchTextEncode=encodeURIComponent(searchText);
                       $.ajax({
                url:'studioAction.do?studioParam=displayList&searchText='+searchTextEncode+'&userId='+userid+'&type='+type,
                success: function(data){
                    alert(data);
                    var json=eval('('+data+')');
                       if(json.itemLst.length==0)
                      alert("no item exists with this name");
                        else

                            bulidTable(data,studioId,type,path)

                            }
                    });
            }
        }

        </script>

    </body>
</html>

