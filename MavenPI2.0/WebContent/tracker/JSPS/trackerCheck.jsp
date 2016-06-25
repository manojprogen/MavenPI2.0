<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@page import="java.awt.Font" %>
<%@page import="utils.db.*" %>
<%@ page language="java" import="java.sql.*, java.lang.*, java.util.*" %>
<%@ page  import="java.awt.*" %>
<%@ page  import="java.io.*" %>
<%@page import="com.progen.charts.*"%>
<%@page import="java.util.HashMap" %>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="com.progen.metadata.*"%>
<%@page import="prg.db.*"%>
<%@page import="prg.util.TableUtils"%>
<%@ page import="java.util.*" %>
<%@page import="com.progen.report.params.PrgReportParams" %>
<%
        try {

            HashMap hashMap=new HashMap();

            hashMap.put("CBOAS_OF_DATE",request.getParameter("CBOAS_OF_DATE"));
            hashMap.put("CBOPRG_PERIOD_TYPE",request.getParameter("CBOPRG_PERIOD_TYPE"));
            hashMap.put("CBOCOMPANY",request.getParameter("CBOCOMPANY"));
            hashMap.put("CBOSTATE",request.getParameter("CBOSTATE"));
            hashMap.put("CBOPRODUCT",request.getParameter("CBOPRODUCT"));
            hashMap.put("CBODEALER",request.getParameter("CBODEALER"));
            hashMap.put("CBOBASE",request.getParameter("CBOBASE"));
            hashMap.put("CBOREGION",request.getParameter("CBOREGION"));
            hashMap.put("CBOVIEW_BY_NAME",request.getParameter("CBOVIEW_BY_NAME"));
            hashMap.put("CBOPRG_COMPARE",request.getParameter("CBOPRG_COMPARE"));
            hashMap.put("CBOLOCATION",request.getParameter("CBOLOCATION"));

              //////////////////////////////////////////////////////////////////////.println.println("------hashMap-----"+hashMap);
            
            Connection con=ProgenConnection.getInstance().getConnection();
            Statement st=con.createStatement();
            ResultSet rs=null;

            //String REPORTID =session.getAttribute("REPORTID").toString();
            String REPORTID ="56";
            String UserId = "41";
            String passurl = "";

            ReportData rd = new ReportData(REPORTID);
            rd.ReportType="Progen Standard Report";
            rd.ReportName="Shades Sales Analysis";
            //////////////////////////////////////////////////////////////////////.println.println("rd===" + rd);
            PrgReportParams prp = new PrgReportParams(rd);
            
          

            prp.storeParams(hashMap);
            
            //////////////////////////////////////////////////////////////////////.println.println("IN JSPPPP-----> "+prp.hm.get("VIEW_BY_NAME")+"-------->"+prp.hmViewBy);
            //////////////////////////////////////////////////////////////////////.println.println("prp==" + prp);
            prp.setUserId(UserId);
            TableData td = new TableData(rd);
            //////////////////////////////////////////////////////////////////////.println.println("td===" + td);
            String TableId = td.getTableIds(REPORTID);
            //////////////////////////////////////////////////////////////////////.println.println("TABLEID ====>"+ TableId);
            String measure=session.getAttribute("measure").toString();
            String sqlstr = td.getTableQueryId(TableId, prp);
            sqlstr= td.setTableData(sqlstr);
            //////////////////////////////////////////////////////////////////////.println.println("sqlstr====" + sqlstr);
            //outer wrapper Query starts
            rs=st.executeQuery("select column_name  from prg_report_table_details where (is_view_by_column='Y' or upper(column_disp_name)=upper('Sales Value')) and table_id="+TableId+" order by is_view_by_column desc ");
            PbReturnObject pbro=new PbReturnObject(rs);
           //////////////////////////////////////////////////////////////////////.println.println("prbo==="+pbro);
            String wrapperQuery="select ";
            String arr[]={"VIEW_BY",measure};
            for(int i=0;i<pbro.getRowCount();i++){
              wrapperQuery+=pbro.getFieldValueString(i,"COLUMN_NAME")+" as \""+arr[i]+"\",";
               }
            wrapperQuery=wrapperQuery.substring(0,(wrapperQuery.length()-1));
            wrapperQuery=wrapperQuery+" from("+sqlstr+")";
            //////////////////////////////////////////////////////////////////////.println.println("main wraper query=="+wrapperQuery);
            out.println("main wraper query=="+wrapperQuery);
            //rs=st.executeQuery(

        } catch (Exception e) {
            e.printStackTrace();
        }
%>
