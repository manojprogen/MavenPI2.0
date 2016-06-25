<%-- 
    Document   : setDefaultTime
    Created on : May 21, 2010, 12:33:02 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
   String reportid=(String)request.getParameter("reportid");
   ////.println("reportid-->"+reportid);
   String contexPath=request.getContextPath();
%>
<html>
    <head>

        <%-- <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.datepicker.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>
        <script src="<%=contexPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/jquery.columnfilters.js"></script>
        <script src="<%=contexPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contexPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/effects.explode.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <script type="text/javascript" src="<%=contexPath%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/docs.js"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contexPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contexPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contexPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contexPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=contexPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contexPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=contexPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contexPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contexPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contexPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=contexPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=contexPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=contexPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contexPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<%=contexPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/pbreporttemplateframejs.js"></script>
       
        <title>JSP Page</title>
       
    </head>
    <body>
        <form name="myFormdate" method="post" action="">
            <table align="center" width="100%" >
            <tr width="50%">
                <td align="center" >Date:</td>
                <td>
                    <INPUT TYPE=text id="cdate" NAME="cdate" >
                </td>
                    </tr>
            <%--<tr>
                <td>
                    <INPUT TYPE=RADIO NAME="cdate" VALUE="S">Set Default Date As Sysdate
                </td>
                    </tr>
                    <tr>
                <td>
                    <INPUT TYPE=RADIO NAME="cdate" VALUE="c">User Defined Date
                </td>
                    </tr>
                    --%>
                    
                    <tr>
                        <td align="right">
                            <input type="button" name="save" class="navtitle-hover" value="save" onclick="saveDate()">
                        </td>
                    </tr>

        </table>
        </form>
                     <script>
             $(function(){
                $('#cdate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
             })
        </script>
               <script>
                function saveDate(){
                      var date=document.getElementById("cdate").value;
                      if(date!=null&&date!="")
                          {
                     $.ajax({
                    url:'reportTemplateAction.do?templateParam=setCustomDate&date='+date+"&reportid="+<%=reportid%>,
                    success: function(data){
                       <%-- alert('data-->'+data);--%>
                        if(data==1)
                            parent.$("#customDate").dialog('close');
                    }
                });

                }
                else
                {
                alert("Please Select Date");
                }
                    }
                </script>
 </body>
</html>
