<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%
String PbReportId=request.getParameter("ReportId");
    String themeColor="blue";
    if(session.getAttribute("theme")==null)
        session.setAttribute("theme",themeColor);
    else
        themeColor=String.valueOf(session.getAttribute("theme"));
%>
<Html>
    <Head>
<!--        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">-->
        <link href="../css/emailsajax.css" rel="stylesheet" type="text/css">
        <Script type="text/javascript" language="javascript"  src="../JS/myPersonalisedReport.js"></Script>
        <Script type="text/javascript" language="javascript"  src="../JS/emailsajax.js"></Script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />
        <script type="text/javascript" >
            function checkSave(ctxpath){
                if(document.getElementById("repcustname").value==""){
                    alert('Please Enter Snapshot name')
                }else{
                    document.myForm.UrlVal.value = parent.document.getElementById("txtcompleteurl").value;
                    document.myForm.action=ctxpath+"/SaveSnapShot.do?ReportId=<%=PbReportId%>";
                    document.myForm.submit();
                    document.getElementById("repcustname").value="";          
                    parent.cancelPerLinks();
                }
            }
        </script>
    </Head>
    <Body onload="sample()">
        <center>
            <Form action=""  name="myForm" method=post >
                <center>
                    <font size=2px>Please use @ for searching the database.</font>
                </center><Br/>
                <Table>
                    
                    <Tr><Td class="myhead">Snapshot Name</Td>
                        <Td><Input type="text"  class="myTextbox5" name="repcustname" id="repcustname" maxlength=50 style="width:147px"></Td></Tr>
                    <Tr><Td class="myhead">Snapshot Date</Td>
                        <Td> <select id="snapDate" name="snapDate" class="myTextbox5" style="width:147px">
                                <option value="sysdate">Sysdate</option>
                                <option value="userExecutedDate">User Executed Date</option>
                            </select>
                        </Td>
                    </Tr>
                    <Tr><Td class="myhead">Shared(Emails)</Td>
                        <Td >
                            <input type=text name=toAddress id="toAddress" class="myTextBox5" autocomplete="off"  onfocus="selId('#toAddress','emaildiv','qq')">
                            <%-- <input type=hidden name=toAddress id="toAddress" class="myTextBox5" autocomplete="off" value="admin@progenbusiness.com"> --%>
                            <div id="emaildiv" style="height:150px;overflow:auto"></div>   </Td>
                    </Tr>
                </Table>
                <Table>
                    <Tr>
                        <%-- <Td><input class="btn" type="button" value="Cancel"  onclick="checkCancel('pbPersonalisedReportsList.jsp')"; ></Td> --%>
                        <Td><Input class="navtitle-hover" type="button" value="Save" onclick="checkSave('<%=request.getContextPath()%>')"></Td>
                        <Td><Input class="navtitle-hover" type="reset" value="Clear">
                            <input type="hidden" name="UrlVal">
                        </Td>
                    </Tr>
                </Table>
                <%-- <Input type="hidden"  class="myTextbox5" name="userid"  value="<%=session.getAttribute("userid")%>" >
                 <Input type="hidden"  class="myTextbox5" name="reportid"  value="<%=request.getParameter("REPORTID")%>"  >
                 <Input type="hidden"  class="myTextbox5" name="repparams"  value="<%=request.getParameter("repparams")%>"  >
                 <Input type="hidden"  class="myTextbox5" name="contextpath"  value="<%=request.getParameter("contextpath")%>" >--%>
                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
            </Form>
        </center>
    </Body>
</Html>