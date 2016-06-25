/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.passwordAction;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class passwordAction extends DispatchAction {

    passwordDAO pswrddao = new passwordDAO();
    prg.util.PbMailParams params = null;
    prg.util.PbMail mailer = null;

    public ActionForward updatePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String oldpswrd = request.getParameter("useroldpswrd");
        String newpswrd = request.getParameter("usernewpswrd");
        String username = request.getParameter("username");
        String status = pswrddao.checkOldpswrd(oldpswrd, username, newpswrd);
        String updtedstatus = null;
        PrintWriter out = response.getWriter();

        if (status.equalsIgnoreCase("Success")) {
            updtedstatus = pswrddao.updatePassword(username, newpswrd);
            if (updtedstatus != null && updtedstatus.equalsIgnoreCase("success")) {
                String sucesmsg = "Password Updated Successfully\t";
                sucesmsg = sucesmsg + "<a href=baseAction.do?param=loginPage>Logout</a>";
                request.setAttribute("succesmsg", sucesmsg);
                //out.print(true);
                //return null;
                return mapping.findForward("loginPage");
            } else {
                String failmsg = "Invalid User Name";
                request.setAttribute("failuremsg", failmsg);
                return mapping.findForward("Invalid Username");
//                return null;
            }

        } else {
            String failmsg = "Invalid User Name";
            request.setAttribute("failuremsg", failmsg);
            return mapping.findForward("Invalid Username");
            //  return null;
        }
    }

    public ActionForward getpswrd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String emailid = request.getParameter("useremailid");
        ////////////////////////////////////////////////////.println("emailid" + emailid);
        String username = request.getParameter("username");
        ////////////////////////////////////////////////////.println("username" + username);
        String password = pswrddao.getPassword(username, emailid);
        if (password.equalsIgnoreCase("failure")) {
            String failuremsg = "Invalid Emailid";
            request.setAttribute("failuremsge", failuremsg);
            return mapping.findForward("InvalidEmailid");
        }
        if (!(password.equalsIgnoreCase("failure"))) {
//            PbMail tosndpswrd = new PbMail();
//            tosndpswrd.sendMail();
            ////////////////////////////////////////////////////.println("password"+password);
            String bodyText = "Hi, \n This is your passowrd \n" + password;
            String toAddress = emailid;
            String subject = "BI Password";
            ////////////////////////////////////////////////////.println("bodytext"+bodyText);
            ////////////////////////////////////////////////////.println("toAddress"+toAddress);
            ////////////////////////////////////////////////////.println("subject"+subject);
            params = new prg.util.PbMailParams();
            params.setBodyText(bodyText);
            params.setToAddr(toAddress);
            params.setSubject(subject);
            params.setHasAttach(true);
            mailer = new prg.util.PbMail(params);
            boolean status = mailer.sendMail();
            if (status == true && !bodyText.equals("")) {
                String succesmsg = "Password Has been generated to your Email\t";
                succesmsg = succesmsg + "<a href=baseAction.do?param=loginPage>Logout</a>";
                ////////////////////////////////////////////////////////////////////////////////////////////.println("Mail Is Sent Successfully");
                request.setAttribute("succesmsg", succesmsg);
                return mapping.findForward("senttomail");
            } else {
                return mapping.findForward("InvalidEmailid");
            }
        }
        return null;
    }

    public ActionForward resetPassword(ActionMapping mapping, ActionForm from, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();
        String newPassword = request.getParameter("newpasswrod");
        String userId = request.getParameter("userid");
        //////.println("newPassword"+newPassword);
        //////.println("userId"+userId);
        String status = pswrddao.resetPassword(newPassword, userId);
        //////.println("status"+status);
        if (status.equalsIgnoreCase("success")) {
            out.print("1");
        } else {
            out.print("2");
        }
        return null;
    }

    public ActionForward verifyResetPasswrod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        //////.println("user is \t"+userId);
        String status = pswrddao.verifyResetPassword(userId);
        //////.println("status"+status);
        PrintWriter out = response.getWriter();
        if (status.equalsIgnoreCase("")) {
            out.print(status);
        } else {
            out.print(status);
        }
        return null;
    }
    //by gopesh

    public ActionForward updateUserName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String oldName = request.getParameter("oldName");
        String newName = request.getParameter("newName");
        String userId = request.getParameter("userid");
        String status = pswrddao.checkOldName(userId, oldName, newName);
        String updtedstatus = null;
        PrintWriter out = response.getWriter();

        if (status.equalsIgnoreCase("Success")) {
            updtedstatus = pswrddao.updateName(oldName, userId, newName);
            /// return mapping.findForward("loginPage");
            response.getWriter().print("success");
            return null;

        } else {
            String failmsg = "Invalid User Name";
            request.setAttribute("failuremsg", failmsg);
            return mapping.findForward("Invalid Username");
        }
    }
}
