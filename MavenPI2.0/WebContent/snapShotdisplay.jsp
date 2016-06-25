<%@page import="prg.db.*,java.util.ArrayList,com.google.gson.reflect.TypeToken,java.lang.reflect.Type,java.util.List,com.google.gson.Gson,java.util.Arrays"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String themeColor="blue";
    if(session.getAttribute("theme")==null)
        session.setAttribute("theme",themeColor);
    else
        themeColor=String.valueOf(session.getAttribute("theme"));


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <title>JSP Page</title>

        <script>
            function getFavLinks(){
                document.myForm.action = "reportViewer.do?reportBy=favReports";
                document.myForm.submit();
            }
            function viewReport(path){
//                parent.document.forms[0].action=path;
//                parent.document.forms[0].submit();
                parent.submiturls1(path);
            }

        </script>
        <style>
            a {font-family:Verdana;font-size:10px;cursor:pointer;}
/*            a:link {color:#369}*/
            /* .body{
                 background-color:#e6e6e6;
             }*/
        </style>
    </head>
    <body class="body">
        <form name="myForm" action="" method="post">
            <table>
                <%
                    Gson gson=new Gson();
                  Type tarType=new TypeToken<List<String>>() {}.getType();
                PbDb pbdb = new PbDb();
                String loguserId = String.valueOf(session.getAttribute("USERID"));
                PbReturnObject pbro = pbdb.execSelectSQL("select PRG_PERSONALIZED_ID, PRG_USER_ID, PRG_REPORT_ID, PRG_REPORT_CUST_NAME, PRG_REPORT_PARAMETERS, PRG_REPORT_CONTEXT_PATH, PRG_SEQUENCE_NO,SNAP_URL,MODULE_TYPE from prg_ar_personalized_reports where prg_user_id=" + loguserId + " AND PRG_REPORT_CUST_NAME is not null and scheduler_details is null order by upper(PRG_REPORT_CUST_NAME)");
                int len = 0;
                if (pbro.getRowCount() > 10) {
                    len = 10;
                } else {
                    len = pbro.getRowCount();
                }
                String message1 = "";
                String snapurl = "";
                for (int i = 0; i < len; i++) {
                    if(!pbro.getFieldValueString(i,"MODULE_TYPE").equalsIgnoreCase("dynamicHeadline") && !pbro.getFieldValueString(i,"MODULE_TYPE").equalsIgnoreCase("scheduler")){
                    message1 = (pbro.getFieldValueString(i, 3).length() < 25) ? pbro.getFieldValueString(i, 3) : pbro.getFieldValueString(i, 3).substring(0, 25) + "..";
                    if (!pbro.getFieldValueString(i, "PRG_REPORT_CONTEXT_PATH").equalsIgnoreCase("") && !(pbro.getFieldValueString(i, "PRG_REPORT_CONTEXT_PATH") == null)) {
                         String url1= pbro.getFieldUnknown(i, 7);
                         String[] query=url1.split(";");
                         StringBuilder finalQ=new StringBuilder();
                         for(int p=0; p < query.length;p++)
                         {
                            String[] item=query[p].split("=");
                            List<String> fList=new ArrayList<String>();
                            if(item.length ==2){
                                if(item[0].contains("CBOARP"))
                                {
                                    String filter=item[1];
                                    try{
                                        fList = gson.fromJson(filter, tarType);
                                    }catch(com.google.gson.JsonParseException e) {
                                        String[] splitedStr = filter.split(",");
                                        fList = Arrays.asList(splitedStr);
                                    }
                                     item[1]=gson.toJson(fList);

                                }
                                   finalQ.append(item[0]).append("=").append(item[1]).append(";");
                            }

                             // 


//                   }
//                           snapurl = pbro.getFieldUnknown(i, 7).replace(";", "&") + "&isSnapShot=true&SnapShotId=" + pbro.getFieldValueString(i, 0);
                           snapurl = finalQ.toString().replace(";", "&") + "&isSnapShot=true&SnapShotId=" + pbro.getFieldValueString(i, 0);

                    }
                    }else {
                        String url=pbro.getFieldValueClobString(i, "SNAP_URL");
                        snapurl = pbro.getFieldValueString(i, "PRG_REPORT_CONTEXT_PATH") + "/" + pbro.getFieldValueClobString(i, "SNAP_URL").replace(";", "&") + "&isSnapShot=true&SnapShotId=" + pbro.getFieldValueString(i, 0);
                    }

                    //////////.println("snapurl is "+snapurl);
                %>
                <tr>
                    <td>

                    </td>
                </tr>
                <tr>
                    <td>
                        <a  href='<%=snapurl%>' title="<%=pbro.getFieldValueString(i, 3)%>" target="_blank" style="text-decoration:none"><%=message1%></a>
                    </td>
                </tr>
                    <%}
                }%>
            </table>
        </form>
    </body>
</html>
