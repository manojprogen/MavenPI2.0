<%-- 
    Document   : pbSelectTimeFlow
    Created on : Dec 28, 2009, 5:06:39 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
             <script type="text/javascript" src="<%=request.getContextPath()%>/QTarget/JS/myScripts.js"></script>
             <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
             
        <title>JSP Page</title>
        <script type="text/javascript">
            function showSelectedTime()
            {
                 var selTime= window.opener.document.getElementById("timeLevels").value;
                // alert(' load '+selTime);
                 var indTime=selTime.split(",");
                 for(var i=0;i<indTime.length;i++)
                     {
                         if(i==0)
                             {
                                 //alert('in if ');
                                 document.getElementById("level1").value.selectedItem="1";
                             }
                     }
            }
            function saveTimeFlow()
            {
                var l="";
                var pw = window.opener;
				if(pw)
                {
                    var selTime= window.opener.document.getElementById("timeLevels").value;
                   // alert('selTime '+selTime);
                    var inputFrm = pw.document.forms['myForm'];
                    l=document.getElementById("level1").value;
                    l=l+","+document.getElementById("level2").value;
                    l=l+","+document.getElementById("level3").value;
                    l=l+","+document.getElementById("level4").value;
                   // alert('l '+l);
                    window.opener.document.getElementById("timeLevels").value=l;
                    parent.showTargetStartAndEnd();
                    window.close();
                }
            }
            function closeTime(){
             window.close();
            }
        </script>
        <style>
            *{font:12px verdana;color:#000}
        </style>
    </head>
    <%
    String timeLevels=request.getParameter("timeLevels");
    String l[]=timeLevels.split(",");
    String l1="";
    String l2="";
    String l3="";
    String l4="";
    for(int m=0;m<l.length;m++)
        {
        if(m==0)
        l1=l[m];
        if(m==1)
        l2=l[m];
        if(m==2)
        l3=l[m];
        if(m==3)
        l4=l[m];

        }
    //////////////////////////////////////////.println(" in if time "+timeLevels);
    String isSelected="";
    //////////////////////////////////////////.println(" in time dim");%>
    <body>
        <Center>
              <font size="3px" color="black" style="font-weight:bold">Define Time Flow</font>
              <Br/><Br/>
        <Form name="myForm" id="myForm">


            <Table>
                <Tr>
                    <Td>
                       Level 1
                    </Td>
                    <Td>
                        <select id="level1" name="level1"  style="width:173px">
                                 <option value=""></option>
                                 <% isSelected="";
                                    if(l1.equalsIgnoreCase("1")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="1">Year</option>
                                 <%}else{%>
                                 <option value="1">Year</option>
                                 <%}%>
                                    <%if(l1.equalsIgnoreCase("2")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="2">Qtr</option>
                                 <%}else{%>
                                 <option value="2">Qtr</option>
                                 <%}%>
                                  <%if(l1.equalsIgnoreCase("3")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="3">Month</option>
                                 <%}else{%>
                                  <option value="3">Month</option>
                                 <%}%>
                                 <%if(l1.equalsIgnoreCase("4")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="4">Day</option>
                                 <%}else{%>
                                 <option value="4">Day</option>
                                 <%}%>
                        </select>
                    </Td>
                </Tr>
                <Tr>
                    <Td>
                       Level 2
                    </Td>
                    <Td>
                        <select id="level2" name="level2"  style="width:173px">
                                 <option value=""></option>
                                 <%isSelected="";
                                    if(l2.equalsIgnoreCase("1")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="1">Year</option>
                                 <%}else{%>
                                 <option value="1">Year</option>
                                 <%}%>
                                  <%if(l2.equalsIgnoreCase("2")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="2">Qtr</option>
                                 <%}else{%>
                                 <option value="2">Qtr</option>
                                 <%}%>
                                  <%if(l2.equalsIgnoreCase("3")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="3">Month</option>
                                 <%}else{%>
                                 <option value="3">Month</option>
                                 <%}%>
                                  <%if(l2.equalsIgnoreCase("4")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="4">Day</option>
                                 <%}else{%>
                                  <option value="4">Day</option>
                                 <%}%>
                        </select>
                    </Td>
                </Tr>
                <Tr>
                    <Td>
                       Level 3
                    </Td>
                    <Td>
                        <select id="level3" name="level3"  style="width:173px">
                                 <option value=""></option>
                                 <%isSelected="";
                                   if(l3.equalsIgnoreCase("1")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="1">Year</option>
                                 <%}else{%>
                                 <option value="1">Year</option>
                                 <%}%>
                                  <%if(l3.equalsIgnoreCase("2")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="2">Qtr</option>
                                 <%}else{%>
                                  <option value="2">Qtr</option>
                                 <%}%>
                                  <%if(l3.equalsIgnoreCase("3")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="3">Month</option>
                                 <%}else{%>
                                 <option value="3">Month</option>
                                 <%}%>
                                 <%if(l3.equalsIgnoreCase("4")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="4">Day</option>
                                 <%}else{%>
                                 <option value="4">Day</option>
                                 <%}%>
                        </select>
                    </Td>
                </Tr>
                <Tr>
                    <Td>
                       Level 4
                    </Td>
                    <Td>
                        <select id="level4" name="level4"  style="width:173px">
                                 <option value=""></option>
                                  <%isSelected="";
                                   if(l4.equalsIgnoreCase("1")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="1">Year</option>
                                 <%}else{%>
                                 <option value="1">Year</option>
                                 <%}%>
                                  <% if(l4.equalsIgnoreCase("2")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="2">Qtr</option>
                                 <%}else{%>
                                  <option value="2">Qtr</option>
                                 <%}%>
                                  <% if(l4.equalsIgnoreCase("3")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="3">Month</option>
                                 <%}else{%>
                                 <option value="3">Month</option>
                                 <%}%>
                                  <% if(l4.equalsIgnoreCase("3")){
                                        isSelected="selected";
                                   %>
                                 <option <%=isSelected%> value="4">Day</option>
                                 <%}else{%>
                                  <option value="4">Day</option>
                                 <%}%>
                        </select>
                    </Td>
                </Tr>
            </Table>
                <Input TYPE="hidden" name="l1" id="l1" value="">
                <Input TYPE="hidden" name="l2" id="l2" value="">
                <Input TYPE="hidden" name="l3" id="l3" value="">
                <Input TYPE="hidden" name="l4" id="l4" value="">

            <Table>
                <Tr>
                     <Td><INPUT type="BUTTON" class="navtitle-hover"  value="Save" name="Save" onclick="saveTimeFlow()"></Td>
                     <Td><INPUT type="BUTTON" class="navtitle-hover"  value="Cancel" name="Cancel" onclick="closeTime()"></Td>
                </Tr>
            </Table>
        </Form>
        </Center>
    </body>
</html>
