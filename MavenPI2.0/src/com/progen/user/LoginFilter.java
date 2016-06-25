/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.user;

/**
 *
 * @author saugupta
 */
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

    FilterConfig config = null;
    ServletContext servletContext = null;

    public LoginFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        servletContext = config.getServletContext();
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        String requestPath = httpRequest.getPathInfo();
        String id = (String) session.getAttribute("id");
        if (id == null) {
            session.setAttribute("ORIGINAL_VIEW_KEY", httpRequest.getPathInfo());
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        } else {
            session.removeAttribute("ORIGINAL_VIEW_KEY");
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
