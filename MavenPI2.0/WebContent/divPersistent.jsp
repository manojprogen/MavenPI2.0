
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import ="prg.db.Container"%>
<%@page import ="java.util.HashMap"%>
<%@page import ="java.util.ArrayList"%>
<%@page import ="java.io.PrintWriter"%>

<%--<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>--%>
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            HashMap stickListHashMap = null;
            HashMap stickValueMap = null;
            HashMap map = new HashMap();
            Container container = null;
            if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
            }

            ArrayList stickArrayList = null;
            String method = request.getParameter("method");
            if (method != null) {
                if (method.equalsIgnoreCase("forwidgets")) {
                    String widstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("widstatusout of if\t" + widstatus);
                        if (session.getAttribute("widgetStatus") != null) {
                            widstatus = "block";
                            session.setAttribute("widgetStatus", widstatus);
                            // ////.println("widstatus\t" + widstatus);
                        }
                    } else {
                        // ////.println("widstatusout else of if\t" + widstatus);
                        if (session.getAttribute("widgetStatus") != null) {
                            widstatus = "none";
                            session.setAttribute("widgetStatus", widstatus);
                            // ////.println("none in widstatus\t" + widstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forfavlinkcont")) {
                    String favstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("favstatus of if\t" + favstatus);
                        if (session.getAttribute("favlinkStatus") != null) {
                            favstatus = "block";
                            session.setAttribute("favlinkStatus", favstatus);
                            // ////.println("favstatus\t" + favstatus);
                        }
                    } else {
                        // ////.println("favlinkStatusout else of if\t" + favstatus);
                        if (session.getAttribute("favlinkStatus") != null) {
                            favstatus = "none";
                            session.setAttribute("favlinkStatus", favstatus);
                            // ////.println("none in favstatus\t" + favstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("fortabTable")) {
                    String tabTablestatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("tabTablestatus of if\t" + tabTablestatus);
                        if (session.getAttribute("TableStatus") != null) {
                            tabTablestatus = "block";
                            session.setAttribute("TableStatus", tabTablestatus);
                            // ////.println("tabTablestatus\t" + tabTablestatus);
                        }
                    } else {
                        // ////.println("tabTablestatus else of if\t" + tabTablestatus);
                        if (session.getAttribute("TableStatus") != null) {
                            tabTablestatus = "none";
                            session.setAttribute("TableStatus", tabTablestatus);
                            // ////.println("none in tabTablestatus\t" + tabTablestatus);
                        }
                    }
                }
                 else if (method.equalsIgnoreCase("fortabTable")) {
                    String tabTablestatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("tabTablestatus of if\t" + tabTablestatus);
                        if (session.getAttribute("TableStatus") != null) {
                            tabTablestatus = "block";
                            session.setAttribute("TableStatus", tabTablestatus);
                            // ////.println("tabTablestatus\t" + tabTablestatus);
                        }
                    } else {
                        // ////.println("tabTablestatus else of if\t" + tabTablestatus);
                        if (session.getAttribute("TableStatus") != null) {
                            tabTablestatus = "none";
                            session.setAttribute("TableStatus", tabTablestatus);
                            // ////.println("none in tabTablestatus\t" + tabTablestatus);
                        }
                    }
                }
                else if (method.equalsIgnoreCase("forMapMenuInDashBoard")) {
                    String mapMenustatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("snapshotsstatus of if\t" + snapshotsstatus);
                        if (session.getAttribute("mapMenustatus") != null) {
                            mapMenustatus = "block";
                            session.setAttribute("mapMenustatus",mapMenustatus);
                            // ////.println("snapshotsstatus\t" + snapshotsstatus);
                        }
                    } else {
                        // ////.println("tabTablestatus else of if\t" + snapshotsstatus);
                        if (session.getAttribute("mapMenustatus") != null) {
                            mapMenustatus = "none";
                            session.setAttribute("mapMenustatus",mapMenustatus);
                            // ////.println("none in snapshotsstatus\t" + snapshotsstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forMapDisplayInDashBoard")) {
                    String dashletsColumnstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("snapshotsstatus of if\t" + snapshotsstatus);
                        if (session.getAttribute("dashletsColumnstatus") != null) {
                            dashletsColumnstatus = "block";
                            session.setAttribute("dashletsColumnstatus",dashletsColumnstatus);
                            // ////.println("snapshotsstatus\t" + snapshotsstatus);
                        }
                    } else {
                        // ////.println("tabTablestatus else of if\t" + snapshotsstatus);
                        if (session.getAttribute("dashletsColumnstatus") != null) {
                            dashletsColumnstatus = "none";
                            session.setAttribute("dashletsColumnstatus",dashletsColumnstatus);
                            // ////.println("none in snapshotsstatus\t" + snapshotsstatus);
                        }
                    }
                }
                else if(method.equalsIgnoreCase("forMap")){
                    String mapstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("Mapstatus") != null) {
                            mapstatus = "block";
                            session.setAttribute("Mapstatus", mapstatus);
                        }
                        }else {
                        if (session.getAttribute("Mapstatus") != null) {
                            mapstatus = "none";
                            session.setAttribute("Mapstatus", mapstatus);
                        }
                    }
                    }else if (method.equalsIgnoreCase("fortopBot")) {
                    String topBotstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("topBotstatus of if\t" + topBotstatus);
                        if (session.getAttribute("TopBotStatus") != null) {
                            topBotstatus = "block";
                            session.setAttribute("TopBotStatus", topBotstatus);
                            // ////.println("topBotstatus\t" + topBotstatus);
                        }
                    } else {
                        // ////.println("topBotstatus else of if\t" + topBotstatus);
                        if (session.getAttribute("TopBotStatus") != null) {
                            topBotstatus = "none";
                            session.setAttribute("TopBotStatus", topBotstatus);
                            // ////.println("none in topBotstatus\t" + topBotstatus);
                        }
                    }
                }else if (method.equalsIgnoreCase("forManageSticky")) {
                    String manageStickyStatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("topBotstatus of if\t" + topBotstatus);
                        if (session.getAttribute("manageStickyStatus") != null) {
                            manageStickyStatus = "block";
                            session.setAttribute("manageStickyStatus", manageStickyStatus);
                            // ////.println("topBotstatus\t" + topBotstatus);
                        }
                    } else {
                        // ////.println("topBotstatus else of if\t" + topBotstatus);
                        if (session.getAttribute("manageStickyStatus") != null) {
                            manageStickyStatus = "none";
                            session.setAttribute("manageStickyStatus", manageStickyStatus);
                            // ////.println("none in topBotstatus\t" + topBotstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("formessages")) {
                    String msgstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("msgstatus of if\t" + msgstatus);
                        if (session.getAttribute("MsgStatus") != null) {
                            msgstatus = "block";
                            session.setAttribute("MsgStatus", msgstatus);
                            // ////.println("msgstatus\t" + msgstatus);
                        }
                    } else {
                        // ////.println("msgstatus else of if\t" + msgstatus);
                        if (session.getAttribute("MsgStatus") != null) {
                            msgstatus = "none";
                            session.setAttribute("MsgStatus", msgstatus);
                            // ////.println("none in msgstatus\t" + msgstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forBusRoles")) {
                    String busrolesstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("busrolesstatus of if\t" + busrolesstatus);
                        if (session.getAttribute("BusRolesStatus") != null) {
                            busrolesstatus = "block";
                            session.setAttribute("BusRolesStatus", busrolesstatus);
                            // ////.println("busrolesstatus\t" + busrolesstatus);
                        }
                    } else {
                        // ////.println("busrolesstatus else of if\t" + busrolesstatus);
                        if (session.getAttribute("BusRolesStatus") != null) {
                            busrolesstatus = "none";
                            session.setAttribute("BusRolesStatus", busrolesstatus);
                            // ////.println("none in busrolesstatus\t" + busrolesstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forParameters")) {
                    String paramstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("paramstatus of if\t" + paramstatus);
                        if (session.getAttribute("ParamStatus") != null) {
                            paramstatus = "block";
                            session.setAttribute("ParamStatus", paramstatus);
                            // ////.println("paramstatus\t" + paramstatus);
                        }
                    } else {
                        // ////.println("paramstatus else of if\t" + paramstatus);
                        if (session.getAttribute("ParamStatus") != null) {
                            paramstatus = "none";
                            session.setAttribute("ParamStatus", paramstatus);
                            // ////.println("none in paramstatus\t" + paramstatus);
                        }
                    }
                }else if (method.equalsIgnoreCase("forDashParameters")) {
                    String dashparamstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("DashParamStatus") != null) {
                            dashparamstatus = "block";
                            session.setAttribute("DashParamStatus", dashparamstatus);
                        }
                    }else {
                        // ////.println("paramstatus else of if\t" + paramstatus);
                        if (session.getAttribute("DashParamStatus") != null) {
                            dashparamstatus = "none";
                            session.setAttribute("DashParamStatus", dashparamstatus);
                            // ////.println("none in paramstatus\t" + paramstatus);
                        }
                    }
                }
                else if (method.equalsIgnoreCase("forText")) {
                    String textstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("TextStatus") != null) {
                            textstatus = "block";
                            session.setAttribute("TextStatus", textstatus);
                        }
                    } else {
                        if (session.getAttribute("TextStatus") != null) {
                            textstatus = "none";
                            session.setAttribute("TextStatus", textstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forGraph")) {
                    String graphstatus = "block";
                    int frmheight = 0;
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("GraphStatus") != null) {
                            graphstatus = "none";
                            frmheight = 725;
                            session.setAttribute("tabFrmHeight", frmheight);
                            session.setAttribute("GraphStatus", graphstatus);
                        }
                    } else {
                        if (session.getAttribute("GraphStatus") != null) {
                            graphstatus = "block";
                            frmheight = 370;
                            session.setAttribute("tabFrmHeight", frmheight);
                            session.setAttribute("GraphStatus", graphstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forhideStickText")) {

                    String stickListId = request.getParameter("stickListId");
                    String stkText = request.getParameter("stkText");
                    // ////.println("------stkText-----" + stkText);
                    String dispStick = String.valueOf(request.getParameter("disp"));

                    if (dispStick.equalsIgnoreCase("block")) {
                        if (session.getAttribute("stickListHashMap") == null) {
                            stickListHashMap = new HashMap();
                        } else {
                            stickListHashMap = (HashMap) session.getAttribute("stickListHashMap");
                        }
                        stickListHashMap.put(stickListId, "none");
                        session.setAttribute("stickListHashMap", stickListHashMap);


                        if (session.getAttribute("stickValueMap") == null) {
                            stickValueMap = new HashMap();
                        } else {
                            stickValueMap = (HashMap) session.getAttribute("stickValueMap");
                        }

                        stickValueMap.put(stickListId, stkText);
                        session.setAttribute("stickValueMap", stickValueMap);
                        // ////.println("stickValueMap----" + session.getAttribute("stickValueMap"));

                        if (session.getAttribute("stickArrayList") == null) {
                            stickArrayList = new ArrayList();
                        } else {
                            stickArrayList = (ArrayList) session.getAttribute("stickArrayList");
                        }

                        stickArrayList.add(stickListId);
                        session.setAttribute("stickArrayList", stickArrayList);

                    }

                } else if (method.equalsIgnoreCase("forWIfavlinkcont")) {
                    String WIfavstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("-------------WIfavstatus of if\t" + WIfavstatus);
                        if (session.getAttribute("WIfavlinkStatus") != null) {
                            WIfavstatus = "block";
                            session.setAttribute("WIfavlinkStatus", WIfavstatus);
                            // ////.println("WIfavstatus\t" + WIfavstatus);
                        }
                    } else {
                        // ////.println("WIfavlinkStatusout else of if\t" + WIfavstatus);
                        if (session.getAttribute("WIfavlinkStatus") != null) {
                            WIfavstatus = "none";
                            session.setAttribute("WIfavlinkStatus", WIfavstatus);
                            // ////.println("----------------------none in WIfavstatus\t" + WIfavstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("WIforwidgets")) {
                    String widstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("----------widstatusout of if\t" + widstatus);
                        if (session.getAttribute("WIwidgetStatus") != null) {
                            widstatus = "block";
                            session.setAttribute("WIwidgetStatus", widstatus);
                            // ////.println("widstatus\t" + widstatus);
                        }
                    } else {
                        // ////.println("widstatusout else of if\t" + widstatus);
                        if (session.getAttribute("WIwidgetStatus") != null) {
                            widstatus = "none";
                            session.setAttribute("WIwidgetStatus", widstatus);
                            // ////.println("--------none in widstatus\t" + widstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("WIfortopBot")) {
                    String topBotstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("----topBotstatus of if\t" + topBotstatus);
                        if (session.getAttribute("WITopBotStatus") != null) {
                            topBotstatus = "block";
                            session.setAttribute("WITopBotStatus", topBotstatus);
                            // ////.println("----topBotstatus\t" + topBotstatus);
                        }
                    } else {
                        // ////.println("-----topBotstatus else of if\t" + topBotstatus);
                        if (session.getAttribute("WITopBotStatus") != null) {
                            topBotstatus = "none";
                            session.setAttribute("WITopBotStatus", topBotstatus);
                            // ////.println("-----none in topBotstatus\t" + topBotstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("WIforsnapshots")) {
                    String snapshotsstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("-----------snapshotsstatus of if\t" + snapshotsstatus);
                        if (session.getAttribute("WISnapshotStatus") != null) {
                            snapshotsstatus = "block";
                            session.setAttribute("WISnapshotStatus", snapshotsstatus);
                            // ////.println("----------WISnapshotStatus\t" + snapshotsstatus);
                        }
                    } else {
                        // ////.println("----------tabTablestatus else of if\t" + snapshotsstatus);
                        if (session.getAttribute("WISnapshotStatus") != null) {
                            snapshotsstatus = "none";
                            session.setAttribute("WISnapshotStatus", snapshotsstatus);
                            // ////.println("-------none in snapshotsstatus\t" + snapshotsstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("WIformessages")) {
                    String msgstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("--------msgstatus of if\t" + msgstatus);
                        if (session.getAttribute("WIMsgStatus") != null) {
                            msgstatus = "block";
                            session.setAttribute("WIMsgStatus", msgstatus);
                            // ////.println("----------msgstatus\t" + msgstatus);
                        }
                    } else {
                        // ////.println("----------msgstatus else of if\t" + msgstatus);
                        if (session.getAttribute("WIMsgStatus") != null) {
                            msgstatus = "none";
                            session.setAttribute("WIMsgStatus", msgstatus);
                            // ////.println("--------none in msgstatus\t" + msgstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("WIforBusRoles")) {
                    String busrolesstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("-------busrolesstatus of if\t" + busrolesstatus);
                        if (session.getAttribute("WIBusRolesStatus") != null) {
                            busrolesstatus = "block";
                            session.setAttribute("WIBusRolesStatus", busrolesstatus);
                            // ////.println("-----------busrolesstatus\t" + busrolesstatus);
                        }
                    } else {
                        // ////.println("-----------busrolesstatus else of if\t" + busrolesstatus);
                        if (session.getAttribute("WIBusRolesStatus") != null) {
                            busrolesstatus = "none";
                            session.setAttribute("WIBusRolesStatus", busrolesstatus);
                            // ////.println("---------none in busrolesstatus\t" + busrolesstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("WIforParameters")) {
                    String paramstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("-----------paramstatus of if\t" + paramstatus);
                        if (session.getAttribute("WIParamStatus") != null) {
                            paramstatus = "block";
                            session.setAttribute("WIParamStatus", paramstatus);
                            // ////.println("--------------paramstatus\t" + paramstatus);
                        }
                    } else {
                        // ////.println("------------paramstatus else of if\t" + paramstatus);
                        if (session.getAttribute("WIParamStatus") != null) {
                            paramstatus = "none";
                            session.setAttribute("WIParamStatus", paramstatus);
                            // ////.println("----------none in paramstatus\t" + paramstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("WIforGraph")) {
                    String graphstatus = "block";
                    int frmheight = 0;
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("WIGraphStatus") != null) {
                            graphstatus = "none";
                            frmheight = 725;
                            session.setAttribute("tabFrmHeightWI", frmheight);
                            session.setAttribute("WIGraphStatus", graphstatus);
                        }
                    } else {
                        if (session.getAttribute("WIGraphStatus") != null) {
                            graphstatus = "block";
                            frmheight = 370;
                            session.setAttribute("tabFrmHeightWI", frmheight);
                            session.setAttribute("WIGraphStatus", graphstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forWhatIfRegion")) {
                    String WhatIfStatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("WhatIfRegion") != null) {
                            WhatIfStatus = "block";
                            session.setAttribute("WhatIfRegion", WhatIfStatus);
                            // ////.println("--------------WhatIfStatus\t" + WhatIfStatus);
                        }
                    } else {
                        if (session.getAttribute("WhatIfRegion") != null) {
                            WhatIfStatus = "none";
                            session.setAttribute("WhatIfRegion", WhatIfStatus);
                            // ////.println("----------none in WhatIfStatus\t" + WhatIfStatus);

                        }
                    }
                } else if (method.equalsIgnoreCase("WIfortabTable")) {
                    String tabTablestatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        // ////.println("---------tabTablestatus of if\t" + tabTablestatus);
                        if (session.getAttribute("WITableStatus") != null) {
                            tabTablestatus = "block";
                            session.setAttribute("WITableStatus", tabTablestatus);
                            // ////.println("----------tabTablestatus\t" + tabTablestatus);
                        }
                    } else {
                        // ////.println("----------tabTablestatus else of if\t" + tabTablestatus);
                        if (session.getAttribute("WITableStatus") != null) {
                            tabTablestatus = "none";
                            session.setAttribute("WITableStatus", tabTablestatus);
                            // ////.println("---------none in tabTablestatus\t" + tabTablestatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forleftTdDash")) {
                    // ////.println("forParametersDash");
                    String leftTdDashStatus = "";
                    String imageTypeDash = "";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("leftTdDashStatus") != null) {
                            leftTdDashStatus = "none";
                            imageTypeDash = "control";
                            session.setAttribute("imageTypeDash", imageTypeDash);
                            session.setAttribute("leftTdDashStatus", leftTdDashStatus);
                        }
                    } else {
                        if (session.getAttribute("leftTdDashStatus") != null) {
                            leftTdDashStatus = "";
                            imageTypeDash = "control-180";
                            session.setAttribute("imageTypeDash", imageTypeDash);
                            session.setAttribute("leftTdDashStatus", leftTdDashStatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forleftTd")) {
                    String leftTdstatus = "";
                    String imageType = "";
                    String resizeFun = "";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("leftTdStatus") != null) {
                            leftTdstatus = "none";
                            imageType = "control";
                            resizeFun = "T";
                            session.setAttribute("resizeFun", resizeFun);
                            session.setAttribute("imageType", imageType);
                            session.setAttribute("leftTdStatus", leftTdstatus);
                        }
                    } else {
                        if (session.getAttribute("leftTdStatus") != null) {
                            leftTdstatus = "";
                            resizeFun = "F";
                            imageType = "control-180";
                            session.setAttribute("resizeFun", resizeFun);
                            session.setAttribute("imageType", imageType);
                            session.setAttribute("leftTdStatus", leftTdstatus);
                        }
                    }
                } else if (method.equalsIgnoreCase("forFrameheight")) {
                    String grpSize = request.getParameter("grpSize");
                    String reportId = request.getParameter("reportId");
                    String graphFrameHeight = "";
                    if (map.get(reportId) != null) {
                        container = (prg.db.Container) map.get(reportId);
                    } else {
                        container = new prg.db.Container();
                    }
                    if (grpSize.equalsIgnoreCase("Large")) {
                        graphFrameHeight = "440";
                        container.setFrameHgt1(graphFrameHeight);
                    }else if(grpSize.equalsIgnoreCase("Others")){
                        graphFrameHeight = "370";
                        container.setFrameHgt1(graphFrameHeight);
                    }
                }else if (method.equalsIgnoreCase("forResultStatus")) {
                    String resultstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("ResultStatus") != null) {
                            resultstatus = "block";
                            session.setAttribute("ResultStatus", resultstatus);
                        }
                    } else {
                        if (session.getAttribute("ResultStatus") != null) {
                            resultstatus = "none";
                            session.setAttribute("ResultStatus", resultstatus);
                        }
                    }
                }else if (method.equalsIgnoreCase("forKpiStatus")) {
                    String Kpistatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("KPIStatus") != null) {
                            Kpistatus = "block";
                            session.setAttribute("KPIStatus", Kpistatus);
                        }
                    } else {
                        if (session.getAttribute("KPIStatus") != null) {
                            Kpistatus = "none";
                            session.setAttribute("KPIStatus", Kpistatus);
                        }
                    }
                }else if (method.equalsIgnoreCase("forTopBtmStatus")) {
                    String topBtmstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("topBtmStatus") != null) {
                            topBtmstatus = "block";
                            session.setAttribute("topBtmStatus", topBtmstatus);
                        }
                    } else {
                        if (session.getAttribute("topBtmStatus") != null) {
                            topBtmstatus = "none";
                            session.setAttribute("topBtmStatus", topBtmstatus);
                        }
                    }
                }else if (method.equalsIgnoreCase("forTrends")) {
                    String trendstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("TrendStatus") != null) {
                            trendstatus = "block";
                            session.setAttribute("TrendStatus", trendstatus);
                        }
                    } else {
                        if (session.getAttribute("TrendStatus") != null) {
                            trendstatus = "none";
                            session.setAttribute("TrendStatus", trendstatus);
                        }
                    }
                   } else if (method.equalsIgnoreCase("forTrendChartsSearch")) {
                    String trendstatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("TrendChartStatus") != null) {
                            trendstatus = "block";
                            session.setAttribute("TrendChartStatus", trendstatus);
                        }
                    } else {
                        if (session.getAttribute("TrendChartStatus") != null) {
                            trendstatus = "none";
                            session.setAttribute("TrendChartStatus", trendstatus);
                        }
                    }
                }else if (method.equalsIgnoreCase("forRelatedReport")) {
                    String trendtatus = "block";
                    String block = String.valueOf(request.getParameter("block"));
                    if (block.equalsIgnoreCase("yes")) {
                        if (session.getAttribute("RelatedReportStatus") != null) {
                            trendtatus = "block";
                            session.setAttribute("RelatedReportStatus", trendtatus);
                        }
                    } else {
                        if (session.getAttribute("RelatedReportStatus") != null) {
                            trendtatus = "none";
                            session.setAttribute("RelatedReportStatus", trendtatus);
                        }
                    }
                }


            }
%>
</body>
</html>
