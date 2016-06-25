<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.query.RTDimensionElement,prg.db.PbReturnObject,java.util.*,prg.db.Container"%>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                 
                String tabId = "";
                String currentURL = "";    
                try {
                    tabId = request.getParameter("ReportId");
                    currentURL = request.getParameter("currentURL");
                    if (currentURL == null) {
                        currentURL = "";
                    }
              String contextPath=request.getContextPath();

                    %>
<html>
    <head>
       
        <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbTableMapJS.js"></script>
        
      <!--  <style>
            #iframe1{
                overflow-x: scroll;
            }
           </style>-->
    </head>   
    <body style="background-color:#e6e6e6" >
        <!--<IFRAME NAME='iframe1' SCROLLING="No" width="100%"  ID='iframe1' FRAMEBORDER="0" SRC='<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=tabId%>&path=<%=currentURL%>' STYLE='width:100%;height:<%=String.valueOf(session.getAttribute("tabFrmHeight"))%>px;'>-->
        <IFRAME NAME='iframe1' SCROLLING="No" width="100%"  ID='iframe1' FRAMEBORDER="0" SRC='<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=tabId%>&path=<%=currentURL%>' STYLE='width:100%;'>
        </IFRAME>
        <IFRAME NAME="iframe2" ID="iframe2" STYLE="display:none;width:0px;height:0px;" SRC="TableDisplay/pbSetData.jsp?tabId=<%=tabId%>" frameborder="0"></IFRAME>
        <IFRAME NAME="dFrame" ID="dFrame" STYLE="display:none;width:0px;height:0px" SRC="TableDisplay/pbDownload.jsp" frameborder="0"></IFRAME>
<!--        <script type="text/javascript">
            <%
                    if (session.getAttribute("resizeFun") != null) {
                        String resizeFun = (String) session.getAttribute("resizeFun");
                        if (resizeFun.equalsIgnoreCase("T")) {
            %>
                ifrmesizedynamicResize();
            <%} else {%>
                ifrmesizedynamic();
            <%}
                    } else {%>
                        ifrmesizedynamic();
            <%}%>
        </script>-->
        <form name="mytabmapForm" id="mytabmapForm" method="post">
            <input type="hidden" name="frameHgt1" id="frameHgt1" value="">
            <input type="hidden" name="frameHgt2" id="frameHgt2" value="">
            <input type="hidden" name="colName" id="colName" value="">
            <input type="hidden" name="ctxPath" id="ctxPath" value="">

            <div id="editMsrName" style="display:none">
                <center>
                    <br>
                    <table style="width:100%" >
                        <tr>
                            <td valign="top" style="width:40%">Old Measure Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" name="oldMsrName" readonly style="width:150px" id="oldMsrName">
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" style="width:30%">New Measure Name</td>
                            <td valign="top" style="width:70%">
                                <input type="text" name="newMsrName" id="newMsrName"  style="width:150px" >
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td id="btnCol"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Update" onclick="updateMsrName('<%=tabId%>')"></td>
                        </tr>
                    </table>
                </center>
            </div>

             <div id="SubTotalSrch" style="display:none">
                <center>
                    <br>
                    <table style="width:100%" >
                        <tr>
                            <td valign="top" style="width:40%">SubTotal Value</td>
                            <td valign="top" style="width:20%">
                                <select name="SubTotalSrchOption" id="SubTotalSrchOption" >
                                <option value='GT'>></option>"
                                <option value='GE'>>=</option>"
                                <option value='LT'><</option>"
                                <option value='LE'><=</option>"
                                <option value='EQ'>==</option>"
                        </select>
                            </td>
                              <td valign="top" style="width:150px">
                               <input type="text"  id="SubTtlSrch">
                              </td>
                        </tr>
                    </table>
                </center>
                 <center>
                   <input class="navtitle-hover" type="button" value="Clear filter" onclick="clearfilter(<%=tabId%>)">
                   <input class="navtitle-hover" type="button" value="Done" onclick="subTtlSerach(<%=tabId%>)">
                 </center>

            </div>

        </form>
                        <div id="editScriptAlign" style="display:none">
                            <table>
                                <tr>
                                    <td>
                                        Script Align
                                    </td>
                                    <td>
                                       <select id="ScrpitAlign" selected="right">
                                           <option value="left">Left</option>
                                           <option value="center">Center</option>
                                           <option value="right">Right</option>
                                       </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td id="saveAlign" align="center">
                                        
                                    </td>
                                </tr>
                                
                            </table>
                        </div>
                        <div id="editmeasureAlign" style="display:none">
                            <table>
                                <tr>
                                    <td>
                                        Measure Align
                                    </td>
                                    <td>
                                       <select id="MeasureAlign" selected="center">
                                           <option value="left">Left</option>
                                           <option value="center">Center</option>
                                           <option value="right">Right</option>
                                       </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td id="savemeasureAlign" align="center">
                                        
                                    </td>
                                </tr>
                                
                            </table>
                        </div>
                 <script>
            $(document).ready(function(){
                resizeIframe1();
                
// //Start of Code By Manik for increasing scrolling speed
//                $('#iframe1').load(function(){
//                    $($('#iframe1').contents().find('#progenTableDiv')).scroll(function(){
//
//                        $($('#iframe1').contents().find('.dropDownMenu li')).hover(function () {
//
//                        var absoluteLeft=$(this).offset().left + $(this).outerWidth();
//                        var windowWidth=$(window).width()+$("#progentableDiv").scrollLeft();
//
//                         if(windowWidth-absoluteLeft<200){
//                             $(this).children("ul").find("ul").css({'left':'-100%', top : 0});
//                         }else{
//                             $(this).children("ul").find("ul").css({'left':'100%', top : 0});
//                         }
//                        $(this).children("ul").css('visibility','visible');
//                    },function(){
//                        $(this).children("ul").css('visibility','hidden');
//                    })
//                        document.getElementById("iframe1").contentWindow.moveScroll();
//                    })

 $('#iframe1').load(function() {
                    //Start of Code By Manik for increasing scrolling speed
                    var extra = 100;
                    var thing = $('#progenTableDiv1');
                    var old = $(thing).scrollTop();
                    //End of Code By Manik for increasing scrolling speed

                    $($('#iframe1').contents().find('#progenTableDiv')).scroll(function() {

                        //Start of Code By Manik for increasing scrolling speed

                        if ($(thing).scrollTop() < old) {
                            $(thing).scrollTop($(thing).scrollTop() - extra);
                        } else if ($(thing).scrollTop() > old) {
                            $(thing).scrollTop($(thing).scrollTop() + extra);
                        }
                        old = $(thing).scrollTop();
                        //End of Code By Manik for increasing scrolling speed

                        $($('#iframe1').contents().find('.dropDownMenuad li')).hover(function() {

                            var absoluteLeft = $(this).offset().left + $(this).outerWidth();
                            var windowWidth = $(window).width() + $("#progenTableDiv1").scrollLeft();

                            if (windowWidth - absoluteLeft < 200) {
                                $(this).children("ul").find("ul").css({'left': '-100%', top: 0});
                            } else {
                                $(this).children("ul").find("ul").css({'left': '100%', top: 0});
                            }
                            $(this).children("ul").css('visibility', 'visible');
                        }, function() {
                            $(this).children("ul").css('visibility', 'hidden');
                        })
                        document.getElementById("iframe1").contentWindow.moveScroll();
                    })
 //end of Code By Manik for increasing scrolling speed
                    $($('#iframe1').contents().find('.dropDownMenu li')).hover(function () {

                        var absoluteLeft=$(this).offset().left + $(this).outerWidth();
                        var windowWidth=$(window).width()+$("#progenTableDiv").scrollLeft();

                         if(windowWidth-absoluteLeft<200){
                             $(this).children("ul").find("ul").css({'left':'-100%', top : 0});
                         }else{
                             $(this).children("ul").find("ul").css({'left':'100%', top : 0});
                         }
                        $(this).children("ul").css('visibility','visible');
                    },function(){
                        $(this).children("ul").css('visibility','hidden');
                    })

            //added by manik for ViewBy adhoc drill (hide + sign adhoc drill)
                    $($('#iframe1').contents().find('.dropDownMenuad li')).hover(function () {

                        var absoluteLeft=$(this).offset().left + $(this).outerWidth();
                        var windowWidth=$(window).width()+$("#progentableDiv").scrollLeft();
//                        var absHeight = $(this).offset().top + $(this).outerHeight();
//                        var windowHeight = $(window).height();
//                        alert($(window).height());
//                         alert($(this).offset().top + $(this).outerHeight())
//                          alert(windowWidth)
//                          alert(JSON.stringify($(this).children("ul").find("ul")))
//  $(this).children("ul").find("ul").css({'bottom':'100%'})
//                        if(windowHeight-absHeight<300){
//                            $(this).children("ul").find("ul").css({'bottom':'auto'})
//                        }
                         if(windowWidth-absoluteLeft<200){
                             $(this).children("ul").find("ul").css({'left':'-100%', top : 0});
                         }else{
                             $(this).children("ul").find("ul").css({'left':'100%', top : 0});
                         }
                         
                        $(this).children("ul").css('visibility','visible');
                    },function(){
                        $(this).children("ul").css('visibility','hidden'); 
                    });
                });                  
                });
//            });
                window.onresize = function(event) {
                resizeIframe1();
                }
                function resizeIframe1()
                {
                    if($("#repCtrlDiv").is(":visible")){
                    $("#iframe1").height(($(window).height())-120+"px");
                    }
                    else{
                        $("#iframe1").height(($(window).height())-95+"px");
                    }
                    $("#iframe1").width($(window).width()-5);
                }
        </script>
    </body>

</html>
<%} catch (Exception exp) {
                    exp.printStackTrace();
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
            }%>
