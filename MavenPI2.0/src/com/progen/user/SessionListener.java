package com.progen.user;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Ashutosh
 */
public class SessionListener implements HttpSessionListener {

    private static final Map<String, HttpSession> sessions = Collections.synchronizedMap(new WeakHashMap<String, HttpSession>());
    private static final Map<String, HttpSession> ssoSessionMap = Collections.synchronizedMap(new WeakHashMap<String, HttpSession>());
    private static final Map<String, String> ssoTomcatSession = Collections.synchronizedMap(new WeakHashMap<String, String>());
    private static String ssoSessionId = "";
    private int sessionCount = 0;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        synchronized (this) {
            sessionCount++;
            HttpSession session = event.getSession();
            sessions.put(session.getId(), session);
//        if(sessionCount > 1){
            ssoSessionId = session.getId();
//            
//        }
//        
//        
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        synchronized (this) {
            try {
                if (ssoSessionId != null) {
                    if ((event.getSession().getAttribute("status")).toString().equalsIgnoreCase("OK")) {
                        if ((event.getSession().getId()).equalsIgnoreCase(ssoSessionId)) {
                            ssoSessionMap.remove(event.getSession().getAttribute("ssoToken").toString());
                            ssoTomcatSession.remove(ssoSessionId);
                        }
                    }
//        
                }
            } catch (Exception ex) {
            }
            sessions.remove(event.getSession().getId());

            //  


        }
    }

    public static Map<String, HttpSession> getAllSessionMap() {
        return sessions;
    }

    public static String getSessionID() {
        return ssoSessionId;
    }

    public static HttpSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static HttpSession getSSOSession(String ssoToken) {
        ssoSessionMap.get(ssoToken);

        return ssoSessionMap.get(ssoToken);
    }

    public static String getSSOTomcatSession(String ssosessionId) {
        return ssoTomcatSession.get(ssosessionId);
    }

    public static void setSSOSessionMap() {
        try {
            if (ssoSessionId != null) {
                HttpSession ssosession = getSession(ssoSessionId);

                ssoSessionMap.put(ssosession.getAttribute("ssoToken").toString(), ssosession);
                ssoTomcatSession.put(ssoSessionId, ssosession.getAttribute("ssoToken").toString());


            }
        } catch (Exception ex) {
        }
    }
}
