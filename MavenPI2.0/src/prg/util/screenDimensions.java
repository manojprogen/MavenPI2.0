package prg.util;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class screenDimensions {

    public HashMap getFontSize(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int pageFont = 0;
        HashMap map = new HashMap();
        if (session.getAttribute("USERID") != null) {
            //   if (session.getAttribute("screenwidth") != null && session.getAttribute("screenheight") != null) {
            int screenWidth = Integer.parseInt(String.valueOf(session.getAttribute("screenwidth")));
            int screenHeight = Integer.parseInt(String.valueOf(session.getAttribute("screenheight")));
            if (screenWidth <= 1024) {
                pageFont = 10;
            } else {
                pageFont = 11;
            }
            map.put("Redirect", "No");
        } else if (session.getAttribute("USERID") == null) {
            String path = request.getContextPath() + "/baseAction.do?param=logoutApplication";
            //  response.sendRedirect(path);
            map.put("Redirect", "Yes");
        }
        // ////.println("return");
        map.put("pageFont", pageFont);
        return map;
    }
}
