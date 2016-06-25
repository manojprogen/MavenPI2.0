/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.chating;

import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class progentalkaction extends DispatchAction {

    private final static String SUCCESS = "success";
    progenchatDAO chatdao = new progenchatDAO();

    public ActionForward chatdata(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();

        String userid = "";
        String msg = "";
        if (session.getAttribute("USERID") != null) {
            userid = session.getAttribute("USERID").toString();
        }
        String data = request.getParameter("chtmsg");
        String repid = request.getParameter("reprtid");
        String username = request.getParameter("usrname");
        String status = chatdao.saveMessages(userid, data, repid, username);

        if (status.equalsIgnoreCase("success")) {
            ArrayList chatdata = chatdao.GetMessages(repid);
            if (chatdata != null && chatdata.size() != 0) {
                for (int i = 0; i < chatdata.size(); i++) {
                    progenchatbean chatbean = (progenchatbean) chatdata.get(i);
                    msg = msg + chatbean.getUsername();
                    msg = msg + " <font style='font-size:11px'>" + chatbean.getMsgdate() + "</font>";
                    msg = msg + ":<br/><font style=font-weight:normal;color:black>";
                    msg = msg + chatbean.getMessage() + "</font><br><br>\n";
                }
            }
            out.println(msg);
            return null;
        } else if (status.equalsIgnoreCase("failure")) {
            out.println("Invalid Message");
            return null;
        }

        return mapping.findForward(SUCCESS);
    }

    public ActionForward dispdata(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String repid = request.getParameter("reprtid");
        StringBuffer msg = new StringBuffer("");
        ArrayList chatdata = null;

        if (session != null) {
            chatdata = chatdao.GetMessages(repid);
            if (chatdata != null && chatdata.size() != 0) {
                for (int i = 0; i < chatdata.size(); i++) {
                    progenchatbean chatbean = (progenchatbean) chatdata.get(i);
                    msg.append(chatbean.getUsername());
                    msg.append(" <font style='font-size:11px'>" + chatbean.getMsgdate() + "</font>");
                    msg.append(":<br/><font style=font-weight:normal;color:black>");
                    msg.append(chatbean.getMessage() + "</font><br><br>\n");
                }
            }
            out.println(msg.toString());

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }


    }
}
