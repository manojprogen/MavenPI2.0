

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor="blue";
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
            String contextPath=request.getContextPath();
%>
<html>
    <head>

        <title>JSP Page</title>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

-->        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery-latest.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script><!--
-->        <script language="JavaScript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script><!--

-->        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/dragTable.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />

       
        <style>
            .text_classstyle1 {
                color:#336699;
                font-size:10px;
                line-height:16px;
                font-family:verdana;
            }
        </style>
    </head>
    <body>
        <form name="myForm">
            <%
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("In jsp--->" + session.getAttribute("links"));

        PbReturnObject links = (PbReturnObject) session.getAttribute("links");
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("------------------------");
        links.writeString();
                   %>
            <center>
                <br>
                  <%-- <div id="pager" class="pager" align="center" >

                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                <input type="text" class="pagedisplay"/>
                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                <select class="pagesize">
                    <option value="5">5</option>
                    <option selected value="10">10</option>
                </select>
            </div> --%>
            </center>
             <table  height="30px" width="100%">
                    <tr>

                          <td align="right"> <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="saveLinks()" style="display:block"></td>
                    </tr>

                </table>
            <div style="width:100%;height:420px;overflow-y:auto;">

                <center>
                <table id="tablesorter" border="1" width="80%" cellspacing="1" class="tablesorter" align="center">
                    <thead>
                        <tr>
                            <th>Select</th>
                            <th>Report Name <font size="1">(Please use * for wild searches)</font></th>
                        </tr>
                    </thead>

                    <tbody>

                        <%for (int i = 0; i < links.getRowCount(); i++) {%>
                        <tr>
                            <%if (links.getFieldValueString(i, 2).equalsIgnoreCase("Y")) {%>
                            <td><input type="checkbox" name="chk1" id="chk1" value="<%=links.getFieldValueString(i, 1)%>" onclick="countLinks(this)" checked></td>
                            <%} else {%>
                            <td><input type="checkbox" name="chk1" id="chk1" value="<%=links.getFieldValueString(i, 1)%>" onclick="countLinks(this)"></td>
                            <%}%>
                            <td NOWRAP ALIGN="left"  class="text_classstyle1"><%=links.getFieldValueString(i, 0)%></td>
                        </tr>
                        <%}%>

                    </tbody>

                </table>
                </center>

            <center><input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="saveLinks()" style="display:block"></center>
                <script>
                    $(document).ready(function() {
                        $('table#tablesorter').columnFilters({ excludeColumns:[0]});
                    });
                    //divObj.style.visibility="visible";

                </script>
                </div>


        </form>
                         <script>

            var chkCount = 0;

            $(document).ready(function(){
                $("#tablesorter").tablesorter({widthFixed: true});
            });

            function countLinks(val)
            {
                if(val.checked==true){
                    chkCount++;
                  //  alert(chkCount)
                }
                else
                { chkCount--;
                   // alert(chkCount)
                }
            }

            function saveLinks()
            {
                 var i=0;
    var obj = document.myForm.chk1;
    // chk1= document.myForm.chk1

    if(isNaN(obj.length))
    {
        if(document.myForm.chk1.checked)
        {          document.myForm.action = "saveFavReport.do";
                   document.myForm.submit();
        }
        else
        {
           alert("Please select Favourite link to save");
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.myForm.chk1[j].checked==true)
            {
                i++;
                var userId=document.myForm.chk1[j].value;
             //alert('in second if:  '+document.myForm.chkusers[j].value)
            //alert(document.myForm.chk1[j].name)
            }
        }

        if(i>20)
        {
            alert("You can select maximum of only 20 Favourite Links");
        }
        else if(i==0)
        {
            alert("Please select Favourite link to save");
        }
        else
        {

                document.myForm.action = "saveFavReport.do";
                document.myForm.submit();
        }
    }

                //parent.cancelFavLinks();
            }
        </script>
    </body>
</html>
