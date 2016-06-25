<%-- 
    Document   : pbExternalPortlet
    Created on : Nov 9, 2009, 9:48:37 PM
    Author     : Saurabh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb,java.util.ArrayList,com.progen.portal.Portal,com.progen.portal.PortLet,java.util.Iterator,com.google.common.collect.Iterables"%>


    <%       
  String portletId= request.getParameter("portletId");
     ArrayList<Portal>portals=new ArrayList<Portal>();
     portals =(ArrayList<Portal>) session.getAttribute("PORTALS");
        Portal portal=null;
        PortLet portLet=null;
          Iterator<PortLet> moduleIter=null;
        for(Portal port:portals){
            moduleIter = Iterables.filter(port.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
            if (moduleIter.hasNext()){
                portLet = moduleIter.next();
                portal=port;
                 break;
            }
                     }
      
        out.println(portLet.getXmlString());
    %>


