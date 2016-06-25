<%@page import="prg.targetparam.params.PbTargetParamParams"%>
<%@page import="prg.targetparam.client.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.*"%>
<%@ page import="com.progen.charts.*"%>
<%@ page import="utils.db.*"%>
<html>
<head>
   
      <Script LANGUAGE="JavaScript">
    function goEdit()
    {
      window.history.go(-1)
    }


 </Script>

 <title>pi 1.0</title>

        <style type="text/css">
            .navsection {
                text-decoration: none;
                margin: 0 0 0 0;
                border: 1px solid #CDCDCD;
/*                background: url(../images/navtitlebg.gif) no-repeat top left;*/
                -moz-border-radius: 4px 4px 4px 4px;
                COLOR: #000;
                WIDTH: 100%;
            }
            .navtitle
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
            .navtitle1
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
            .ui-corner-all {
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .ui-state-default, .ui-widget-content .ui-state-default {
                -moz-background-clip:border;
                -moz-background-inline-policy:continuous;
                -moz-background-origin:padding;
                background:#E6E6E6 repeat-x scroll 50% 50%;
                border:1px solid #E6E6E6;
                height:20px;
                color:#000;
                font-weight:normal;

                outline-style:none;
                outline-width:medium;
            }
            .frame1{
                border: 0px;
                width: 700px;
                height: 500px;
            }

            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
        </style>
        <style>
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .white_content {
                display: none;
                position: absolute;
                top: 15%;
                left: 25%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
            }
            a {font-family:Georgia, "Times New Roman", Times, serif;font-size:12px;cursor:pointer}
            a:link {color:#3300CC;}
            a:visited {color: #660066;}
            a:hover {text-decoration: none; color: #ff9900; font-weight:bold;}
            a:active {color: #ff0000;text-decoration: none}
        </style>


<%     String UserId = null;//ReportId.lastIndexOf(str)
        if (session.getAttribute("ID") != null) {
            UserId = (String) session.getAttribute("ID");
        } else if (request.getParameter("userId") != null) {
            UserId = request.getParameter("userId");
        } else if (session.getAttribute("userid") != null) {
            UserId = (String) session.getAttribute("userid");
        }

        String cPath = request.getContextPath();
        //out.println("User Id is -----" +UserId );
        ////////////////////////////////////////////////////////////////////////.println("User Id is -----" + UserId);

             if (UserId == null) {
           // UserId = "41";
            UserId = (String)session.getAttribute("userId");
        }
           // UserId = "41";
        ////////////////////////////////////////////////////////////////////////.println("UserId . "+UserId);
      
%>

</head>
<body>
    <center>

<%
        try {
            ////////////////////////////////////////////////////////////////////////.println(" in show graph..// ");
            Enumeration parameterNames = request.getParameterNames();
            //////////////////////////////////////////////////////////////////////////.println("parameterNames is "+parameterNames.hasMoreElements());
            HashMap boxNames = new HashMap();
            int i = 1;
            Vector names = new Vector();
            String nextElement = null;
            while (parameterNames.hasMoreElements()) {
                //////////////////////////////////////////////////////////////////////////.println("inside hasMoreElements");
                nextElement = (String) parameterNames.nextElement();
                String val = request.getParameter(nextElement);
                if (val.equalsIgnoreCase("") || val == null) {
                    val = "0";
                }
                boxNames.put(nextElement, val);
                names.add(nextElement);
                i++;
            }
            ////////////////////////////////////////////////////////////////////////.println(" boxNames...// "+boxNames);
            String paramName = "";//(String) session.getAttribute("paramName");

          //  if(session.getAttribute("columnEdgeValuesA")!=null)
         //  columnEdgeValuesA = (ArrayList)session.getAttribute("columnEdgeValuesA");
         //    if(session.getAttribute("rowEdgeValuesA")!=null)
          //  rowEdgeValuesA = (ArrayList)session.getAttribute("rowEdgeValuesA");
            ArrayList dateValues = (ArrayList) session.getAttribute("columnEdgeValuesA");
            ArrayList paramValues = (ArrayList) session.getAttribute("rowEdgeValuesA");
            paramName = paramValues.get(0).toString();
            
            ////////////////////////////////////////////////////////////////////////.println(paramName+" dateValues - "+dateValues+" dateValues - "+paramValues);
            ArrayList graphArray = new ArrayList();

            String[] temp = (String[]) dateValues.toArray(new String[0]);

            String barChartColNames[] = new String[temp.length + 1];
            String barChartColTitles[] = new String[temp.length + 1];
            String[] pieChartColumns=new String[temp.length + 1];
            String viewByColumns[] = new String[1];
            
            barChartColNames[0] = paramName;
            barChartColTitles[0] = paramName;
            pieChartColumns[0] = paramName;
            viewByColumns[0] = paramName;

            for (int index = 1; index < barChartColNames.length; index++) {
                barChartColNames[index] = temp[index-1];
                barChartColTitles[index] = temp[index-1];
                pieChartColumns[index]  = temp[index-1];
            }
           ////////////////////////////////////////////////////////////////////////.println("boxNames - "+boxNames);
            for (int n = 1; n < paramValues.size(); n++)
                {
                HashMap hm = new HashMap();
                hm.put(paramName, paramValues.get(n));
                for (int m = 0; m < dateValues.size(); m++)
                    {
                    String key =  paramValues.get(n) + ":" + dateValues.get(m);
                    ////////////////////////////////////////////////////////////////////////.println("key -- "+key);
                    if (boxNames.containsKey(key)) {
                        int val = Integer.parseInt(boxNames.get(key).toString());
                        hm.put(dateValues.get(m), Integer.valueOf(val));
                        ////////////////////////////////////////////////////////////////////////.println(" val...." +val);
                    }
                }
                graphArray.add(hm);
            }
            ////////////////////////////////////////////////////////////////////////.println(" -- graph - " + graphArray);
            
            String jscal = "1";

            ProgenChartDatasets cDataSet = new ProgenChartDatasets();
            cDataSet.setBarChartColumnNames(barChartColTitles);
            cDataSet.setBarChartColumnTitles(barChartColTitles);
            cDataSet.setViewByColumns(viewByColumns);
            cDataSet.setPieChartColumns(pieChartColumns); 
            ProgenChartDisplay chartDisplay = new ProgenChartDisplay(600, 250);
            chartDisplay.setCtxPath(request.getContextPath());
          //  String path = chartDisplay.GetCategoryAxisChart(graphArray, cDataSet, "line", "Revenue", " ", session, response, out);
            String path = chartDisplay.GetCategoryAxisChart(graphArray, cDataSet, "bar", "Revenue", " ", session, response, out);

            //String path = chartDisplay.GetPieAxisChart(graphArray, cDataSet, "ring", "", session, response, out);
          //  out.println(path);
            ////////////////////////////////////////////////////////////////////////.println(" path -- "+path);
            out.println(chartDisplay.chartDisplay);
       %>
            </center>
            <center>
                           <table>
                           <Tr>
                               <Td ALIGN="center">
                                   <br>
                                   <Input ALIGN="center" type="BUTTON" value="Back" onclick="goEdit();">
                               </Td>
                           </Tr>
                           </table>
            </center>
           <br>
     
     
       <%
        } catch (Exception exp) {
            exp.printStackTrace();
        }

%>
  
  </body>
</html>
