<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="utils.db.*"%>
<%@page import="com.progen.metadata.*"%>
<%@page import="com.progen.report.params.PrgReportParams" %>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.tracker.client.PbTrackerManager"%>
<%@page import="prg.tracker.bean.PbTrackerBean"%>
<%@page import="prg.tracker.db.*"%>
<Html>
    <Head>
       <%
        DisplayFrame disp = new DisplayFrame("", "Alert Registration");
        disp.setAdv();
        disp.setQuick("Value Analysis");
        disp.setCtxPath(request.getContextPath());
        disp.setProgenUtilitiesStatus(false);
        disp.setPersonalizedStatus(false);
        out.println(disp.DisplayPreHead());
        %>
         <link href="myStyles.css" rel="stylesheet" type="text/css">
         <link href="../css/emailsajax.css" rel="stylesheet" type="text/css">
         <link href="myStyles.css" rel="stylesheet" type="text/css">
         <link href="../css/cal.css" rel="stylesheet" type="text/css">
         <Script language="javascript"  src="../JS/emailsajax.js"></Script>
         <Script language="javascript"  src="../JS/testdate.js"></Script>

        <%
        out.println(disp.DisplayPostHead());
        %>

    </Head>
     <Body onload="sample()">
         <%

        out.println(disp.DisplayPreBody());
        out.println(disp.DisplayPreRow());
        %>
         <%          FactzQry factq=new FactzQry();
                     ProgenParam pparam=new ProgenParam();
                     int oldId=Integer.parseInt(request.getParameter("REPORTID"));
                     //////////////////////////////////////////////////////////////////////.println.println("ReportId in 2d is "+oldId);
                     ReportData rd = new ReportData(oldId+"");
                     rd.getReportMetaData();
                     PrgReportParams prp = new PrgReportParams(rd);
                     prp.storeParams(request);
                     TableData td= new TableData(rd);
                     factq.ReportData1=rd;
                     String tableId =td.getTableIds(String.valueOf(oldId));
                     //////////////////////////////////////////////////////////////////////.println.println("Got table ID --in 2d+++++"+tableId+"hm=="+prp.hm);
                     PbTrackerManager client=new PbTrackerManager();
                     PbReturnObject retObj=(PbReturnObject)client.get2DMeasures(Integer.parseInt(tableId));
                     session.setAttribute("objectName",prp.hm);
                     session.setAttribute("reportdata",rd);
                     java.sql.Date datestr=new java.sql.Date(System.currentTimeMillis());
                     String newdate=datestr.toString();
                     String newdatearr[]=newdate.split("-");
                     String strdate="";
                     strdate+=newdatearr[1]+"/"+newdatearr[2]+"/"+newdatearr[0];
                            %>
         <Center>
            <Form name="myForm" method="get">
                <center>
                    <font size=2px> Fields marked <span style="color:red">*</span> are MANDATORY</font>


             </Center>
              <Center>
                <Table align="center">
                    <Br>
                    <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                    <Tr>
                    <Td class="myhead"><span style="color:red">*</Span>Tracker Name</Td>
                    <Td><Input type="text"  class="myTextbox5" name="trackerName" maxlength=100 ></Td>
                    </Tr>
                  <%--  <Tr>
                        <Td class="myhead"><span style="color:red">*</Span>Tracker Frequency</Td>
                        <Td>
                            <Select name="TrackerFrq" id="TrackerFrq" class="myTextbox5" onchange="addDate(this)">
                                <Option value="1">Daily</Option>
                                <Option value="2">Monthly</Option>
                                <Option value="3">Quarterly</Option>
                                <Option value="4">Yearly</Option>
                            </Select>
                        </Td>
                    </Tr>
                    --%>
                    <Tr>
                    <Td class="myhead"><span style="color:red">*</Span>Start Date</Td>
                    <Td><Input type="text" readonly class="myTextbox5" name="startdate" maxlength=100  value="<%=strdate%>"onClick="displayDatePicker('startdate')" ></Td>
                    </Tr>
                    <Tr>
                    <Td class="myhead"><span style="color:red">*</Span>End Date</Td>
                    <Td><Input type="text" readonly  class="myTextbox5" name="enddate" maxlength=100 onClick="displayDatePicker('enddate')" ></Td>
                   </Tr>
                   <Tr>
                    <Td class="myhead"><span style="color:red">*</Span>Tracker Basis</Td>
                    <%--<%=pparam.getsingleTextBox("basis", "Tracker Basis", "", prp.getViewByParameter())%>--%>
                <%--    <%=pparam.getSingleAutoCompleteBox("basis",prp.getViewByParameter())%> --%>
                   </Tr>
                   <Tr>
                     <Td class="myhead"><span style="color:red">*</Span>Measure</Td>
                     <Td>
                    <select name="measure" id="measure" class="myTextbox5">
                    <% for(int i=0;i<retObj.getRowCount();i++){%>

                    <option value="<%=retObj.getFieldValueString(i,"COLUMN_DISP_NAME")%>"><%=retObj.getFieldValueString(i,"COLUMN_DISP_NAME")%></option>
                     <% }%>
                    </select>
                    </Td>
                   </Tr>
                   <Tr>
                     <Td class="myhead"><span style="color:red">*</Span>To Whom</Td>
                     <Td width=80%><input type=text name=toAddress id="toAddress" class="myTextBox5" autocomplete="off" size="50" onfocus="selId('toAddress','emaildiv','qq')">
                   <div id="emaildiv"></div>
                  </Td>
                   </Tr>
                   <Tr>
                    <Td class="myhead"><span style="color:red">*</Span>When</Td>
                     <Td><Input type="text"  class="myTextbox5" name="when"></Td>
                   </Tr>
                </Table>
                </center>
            </Form>
         <%  out.println(disp.DisplayPostRow());
        out.println(disp.DisplayPostBody());%>
    </Body>
</Html>

















