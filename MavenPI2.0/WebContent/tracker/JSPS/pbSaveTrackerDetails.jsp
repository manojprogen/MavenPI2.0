
<%@page import="prg.db.Container"%>
<%@page import="java.util.HashMap"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.tracker.client.PbTrackerManager"%>
<%@page import="prg.tracker.params.PbTrackerParams" %>
<%@page import="prg.tracker.scheduler.*" %>
<%@page import="java.sql.*"%>
<%@page import="utils.db.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
        <script>
            function submitTrackerRunner(){
                document.frmtracker.action = "trackerRunner.jsp";

                document.frmtracker.submit();}
        </script>
    </head>
    <body>

        <form name="frmtracker">

       <%
        Session getTrackerSession = new Session();
        PbTrackerManager getTrackerClient = new PbTrackerManager();
        int tracker_id = getTrackerClient.getTackerId();
        HashMap map = new HashMap();
            Container container = null;
       // //////////////////////////////////////////////////////////////////////.println.println("TRACKER ID IS " + tracker_id);
        TimeChange time = new TimeChange();
        String newTime = time.changeTime(request.getParameter("timepicker2"));
       // //////////////////////////////////////////////////////////////////////.println.println("newTime--"+newTime);
        PbTrackerParams insParams = new PbTrackerParams();
        insParams.setTrackerId(tracker_id);
        String url = request.getParameter("url");
        String reportId=request.getParameter("REPORTID");
        if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                }
                if (map.get(reportId) != null) {
                    container = (prg.db.Container) map.get(reportId);
                } else {
                    container = new prg.db.Container();
                }
        String userId=String.valueOf(session.getAttribute("USERID"));
        
        int viewCount=Integer.parseInt(request.getParameter("viewCount"));
        
        String vieyByids=request.getParameter("vieyByids");

        
        
       // //////////////////////////////////////////////////////////////////////.println.println("url in save page==" + url);
       // //////////////////////////////////////////////////////////////////////.println.println("reportId==" + reportId);
       // //////////////////////////////////////////////////////////////////////.println.println("userId" + userId);
       // //////////////////////////////////////////////////////////////////////.println.println("viewCount"+viewCount);
       // //////////////////////////////////////////////////////////////////////.println.println("vieyByids" + vieyByids);
        String selectedViewVals[]=new String[viewCount];
        String selViewValOne=null;
        String selViewValTwo="";
        for(int i=0;i<viewCount;i++){
          selectedViewVals[i]=request.getParameter("allView["+i+"]");
       
          
         // //////////////////////////////////////////////////////////////////////.println.println("selectedViewVals--"+selectedViewVals[i]);
           }
        
           selViewValOne= selectedViewVals[0];
           if(viewCount>1){
               selViewValTwo=selectedViewVals[1];
              }
           else{
               selViewValTwo="";
               }
        String viewIdsList[]=vieyByids.split(",");
        String viewList[]=url.split(";");
        String mainurl="";
        mainurl=viewList[0];
        mainurl+=";"+viewList[1];
        for(int k=2;k<viewList.length;k++){
          String viewlistarr=viewList[k];
          for(int k1=0;k1<viewIdsList.length;k1++){
              if(viewlistarr.contains(viewIdsList[k1])){
                mainurl+=";"+viewIdsList[k1]+"="+selectedViewVals[k1];

              }else{
              mainurl+=";"+viewlistarr;
              }

           }
         }

        
        // //////////////////////////////////////////////////////////////////////.println.println("mainurl---"+mainurl);
        insParams.setTrackerName(request.getParameter("trackerName"));
        insParams.setStartDate(request.getParameter("startdate"));
        insParams.setEndDate(request.getParameter("enddate"));
        String stdate=request.getParameter("startdate");
        String enddate=request.getParameter("enddate");
        insParams.setPrimaryParameters(mainurl);
        insParams.setMeasure(request.getParameter("measure"));
        
        insParams.setViewBy1(selViewValOne);
        insParams.setViewBy2(selViewValTwo);
        //session.setAttribute("measure", request.getParameter("measure"));
        String stime="";
        String smonth="";
        String sdate="";
        String sfrequency="";
        if (Integer.parseInt(request.getParameter("frequency")) == 4) {
            stime="";
            smonth=request.getParameter("alertMonth");
            sdate=request.getParameter("alertDate");
            sfrequency="4";
            insParams.setFrequency(sfrequency);
            insParams.setTime("");
            insParams.setMonth(request.getParameter("alertMonth"));
            insParams.setDate(request.getParameter("alertDate"));
        } else if (Integer.parseInt(request.getParameter("frequency")) == 2) {
            stime="";
            smonth="";
            sdate=request.getParameter("alertDate");
            sfrequency="2";
            insParams.setFrequency(sfrequency);
            insParams.setTime("");
            insParams.setMonth("");
            insParams.setDate(request.getParameter("alertDate"));
        } else if (Integer.parseInt(request.getParameter("frequency")) == 1) {
            stime=newTime;

            
            smonth="";
            sdate="";
            sfrequency="1";
            insParams.setFrequency(sfrequency);
            insParams.setTime(newTime);
            insParams.setMonth("");
            insParams.setDate("");
        } else {
            stime="";
            smonth="";
            sdate="";
            sfrequency="3";
            insParams.setFrequency(sfrequency);
            insParams.setTime("");
            insParams.setMonth("");
            insParams.setDate("");
        }
        insParams.setReportId(reportId);
        insParams.setSubscribers(request.getParameter("toAddress"));

        ////////////////////////////////////////////////.println.println("-toa---"+request.getParameter("toAddress"));
        ////////////////////////////////////////////////.println.println("-em-"+request.getParameter("EorM"));
        insParams.setEorM(request.getParameter("EorM"));
        String EorM=request.getParameter("EorM");

        //added on 20/07/09
        String when = "";
        if (request.getParameter("selectwhen").equals("between")) {
            when = request.getParameter("selectwhen") + "," + request.getParameter("box1") + "," + request.getParameter("box2");
        } else {
            //end
            when = request.getParameter("selectwhen") + "," + request.getParameter("when");

        }// addedd on 20/07/09
        insParams.setCondition(when);
        insParams.setReportType("");
        getTrackerSession.setObject(insParams);
        PbTrackerManager addTrackerClient = new PbTrackerManager();
        addTrackerClient.addTrackerDetails(getTrackerSession);
       

        %>
        <script>
        </script>
        <%


        
        TrackerScheduler ts = new TrackerScheduler(reportId, String.valueOf(tracker_id),stdate,enddate,stime,smonth,sdate,sfrequency,EorM);
        
        

        ts.trackersendMail();
        



        ////////////////////////////////////////////////.println.println("--hhhh----");


            %>
        </form>
    </body>
</html>
