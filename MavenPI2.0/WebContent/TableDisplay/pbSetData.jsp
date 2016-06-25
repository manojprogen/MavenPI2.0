<%@page import="prg.db.Container"%>
<%@ page import="java.util.HashMap,java.util.ArrayList" %>

<%      //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            Container container = null;
            String tabName = null;
            HashMap map = null;
            String selected = null;
            String unselected = null;
            tabName = request.getParameter("tabId");
            ArrayList vect = null;
            if (session != null && session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(tabName);
                //////////////////////////////////////////////////////.println.println("container is " + container);
                selected = request.getParameter("selected");
                unselected = request.getParameter("unselected");
                if (container != null) {
                    vect = container.getSelected();
                    if (selected != null) {
                        if (!vect.contains(selected)) {
                            vect.add(selected);
                        }
                    } else if (unselected != null) {
                        if (vect.contains(unselected)) {
                            vect.remove(unselected);
                        }
                    }

                    container.setSelected(vect);
                    container.setSessionContext(session, container);
                }
            }

%>