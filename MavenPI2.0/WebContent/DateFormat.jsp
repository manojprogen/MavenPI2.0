<%-- 
    Document   : DateFormat
    Created on : 15 May, 2012, 12:25:16 PM
    Author     : progen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Locale"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
            String themeColor = "blue";
            Locale locale = null;
  //          String fromReport=String.valueOf(request.getAttribute("fromReport"));
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String contextPath=request.getContextPath();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/scripts.js"></script>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.tabs.js"></script>
                <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
                 <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
                 <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
                 <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">

    </head>
    <body>
         <script type="text/javascript">

             $('#defaultDate').datepicker({
                        changeMonth: true,
                        changeYear: true
                    });
                    var date=new Date();
                    $('#defaultDate').val(date.getDate()+"/"+(1+(date.getMonth()))+"/"+date.getFullYear());
                    //$('#defaultDate').val(1+(date.getMonth())+"/"+date.getDate()+"/"+date.getFullYear());


             function dateFormat(){
                $( "#datepicker" ).datepicker();
                var dateFormt=$("#format").val();
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=getDateforDateFormat&dateFormt="+dateFormt,
                function(data){
                });

//		$( "#format").change(function() {
//			$( "#datepicker" ).datepicker( "option", "dateFormat", $( this ).val() );
//		});

                
             }
         </script>
         <table align="right">
            <tr>
                <td >
                 <input type="button" value="Done" class="navtitle-hover" style="width:auto"  onclick="dateFormat()" >
                </td>
                 <td >
              <a  href="home.jsp" title="Go to Home Page" ><input type="button" value="Home Page" class="navtitle-hover" style="width:auto"   ></a>
                </td>
        </tr>
        </table>
        <br><br>
       &nbsp;&nbsp;&nbsp;
        
        <div align="left" style=" height: 100px; border-width: 4px; border-style: groove; border-color: skyblue; margin-top: auto; margin-left: 100px; margin-right: 100px; margin-bottom: 100px;  padding-bottom: 80px; padding-left: 150px; padding-top: 50px;"> &nbsp;
            <table align="left"  >
               <Tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead"><span style="color:red">*</span>Company Name </Td>
                            <Td><Input type="text" name="schdReportName" id="schdReportName"   style="width:auto" value="" >
                            </Td>
                        </tr>
                    </table>
              </Tr>

                <Tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead">Select DateFormat</Td>
                            <Td>
                                <select   name="ViewSelect" id="format"  style="width:auto">
                                      <option value="mm/dd/yy">dd/mm/yyyy</option>
                                      <option value="dd/mm/yy">mm/dd/yyyy</option>
                                </select>
                            </Td>
                        </tr>
                    </table>
                </Tr>
                   <Tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead">Default Date</Td>
                            <Td><Input type="text" name="schdReportName" id="defaultDate"  onclick="datePicker()" style="width:auto" value="" >
                            </Td>
                        </tr>
                    </table>
                  </Tr>
                   
<!--               <tr>
                   <td  width="100%">
                        <font style="font-weight: bolder;color: #000000">Select DateFormat</font>
                         <table width="100%">
                                <tr>
                                    <Td class="myhead" width="60%">Select DateFormat</Td>
                                    <Td width="20%" align="left"> <select style="width: 130px;" class="myTextbox5" name="ViewSelect" id="format" >
                                                    <option value="mm/dd/yy">mm/dd/yyyy</option>
                                                     <option value="dd/mm/yy">dd/mm/yyyy</option>
                                             </select></Td>

                                    
                                </tr>
                        
                           </table>
                    </td>
            
            </tr>-->
        </table>
        </div>

    </body>
</html>
