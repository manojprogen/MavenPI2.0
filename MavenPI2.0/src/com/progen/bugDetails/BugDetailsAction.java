package com.progen.bugDetails;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class BugDetailsAction extends LookupDispatchAction {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();

        map.put("bugstatus", "bugstatus");
        map.put("savebugdetails", "savebugdetails");
        map.put("saveEditBugdetails", "saveEditBugdetails");
        map.put("deleteBugdetails", "deleteBugdetails");
        map.put("saveUserDetails", "saveUserDetails");
        map.put("saveAssignDetails", "saveAssignDetails");
        return map;
    }

    /*
     * public ActionForward bugstatus(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * java.lang.Exception { PbReturnObject prgr =new PbReturnObject();
     * PrintWriter Out = response.getWriter(); BugDetailsDAO budao = new
     * BugDetailsDAO(); prgr =budao.bugstatus(); String[] qstr = new
     * String[prgr.getRowCount()]; for(int i=0;i<prgr.getRowCount();i++){
     * qstr[i]=prgr.getFieldValueString(i,"STATUS_NAME" );
     *
     * }
     * String strval= qstr.toString(); Out.print(strval); return null;
    }
     */
    public ActionForward savebugdetails(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        PbReturnObject prgr = new PbReturnObject();
        PrintWriter Out = response.getWriter();
        String comName = request.getParameter("compName");
        String bugNo = request.getParameter("bugno");
        String piver = request.getParameter("piversion");
        String bugStatus = request.getParameter("bugstatus");
        String bugPriority = request.getParameter("bugpriority");
        String bugSubject = request.getParameter("bugsubject");
        String setpsToFollow = request.getParameter("setpstofollow");
        String moduleid = request.getParameter("module");
        /*
         * //////////////////////.println("moduleid====" + moduleid);
         * //////////////////////.println("bugno====" + bugNo);
         * //////////////////////.println("piver====" + piver);
         * //////////////////////.println("bugStatus====" + bugStatus);
         * //////////////////////.println("bugPriority====" + bugPriority);
         * //////////////////////.println("bugSubject====" + bugSubject);
         * //////////////////////.println("setpsToFollow====" + setpsToFollow);
         */
        String[] bugDetailslist = new String[7];
        String[] bugDetailslist1 = new String[2];
        bugDetailslist[0] = bugNo;
        bugDetailslist[1] = piver;
        bugDetailslist[2] = comName;
        bugDetailslist[3] = bugSubject;
        bugDetailslist[4] = bugPriority;
        bugDetailslist[5] = bugStatus;
        bugDetailslist[6] = moduleid;
        bugDetailslist1[0] = setpsToFollow;
        bugDetailslist1[1] = bugNo;
        BugDetailsDAO budao = new BugDetailsDAO();
        budao.savebugdetails(bugDetailslist, bugDetailslist1);



        return null;

    }

    public ActionForward saveEditBugdetails(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        PbReturnObject prgr = new PbReturnObject();
        PrintWriter Out = response.getWriter();
        String comName = request.getParameter("compName");
        String bugNo = request.getParameter("bugno");
        String piver = request.getParameter("piversion");
        String bugStatus = request.getParameter("bugstatus");
        String bugPriority = request.getParameter("bugpriority");
        String bugSubject = request.getParameter("bugsubject");
        String setpsToFollow = request.getParameter("setpstofollow");
        /*
         * //////////////////////.println("compName====" + comName);
         * //////////////////////.println("bugno====" + bugNo);
         * //////////////////////.println("piver====" + piver);
         * //////////////////////.println("bugStatus====" + bugStatus);
         * //////////////////////.println("bugPriority====" + bugPriority);
         * //////////////////////.println("bugSubject====" + bugSubject);
         * //////////////////////.println("setpsToFollow====" + setpsToFollow);
         */
        String[] bugDetailslist = new String[6];
        String[] bugDetailslist1 = new String[2];
        //INSERT INTO BUG_MASTER(BUG_ID,PI_ID,CUSTOMER_ID,CUST_USER_ID,USER_ID,SUBJECT,BUG_PRIORITY_ID,BUG_STATUS_ID,LOG_DATE,UPDATED_DATE,CREATED_BY,UPDATED_BY)
        //VALUES(BUG_MASTER_SEQ.nextval,(select DISTINCT TEXT_VERSION_ID from PI_VERSION where VERSION_NAME ='&'),(select DISTINCT CUSTOMER_ID from CUSTOMER_MASTER where CUSTOMER_NAME ='&'),
        // '','','&',&,(select DISTINCT STATUS_ID from BUG_STATUS where STATUS_NAME ='&'),sysdate, sysdate,'','')

        bugDetailslist[0] = comName;
        bugDetailslist[1] = piver;
        bugDetailslist[2] = bugStatus;//
        bugDetailslist[3] = bugPriority;
        bugDetailslist[4] = bugSubject;
        bugDetailslist[5] = bugNo;
        // INSERT INTO BUG_DETAILS (bug_det_id, bug_id,BUG_DESC) VALUES(BUG_DETAILS_SEQ.nextval,28,'testing on bug from progen')
        bugDetailslist1[0] = setpsToFollow;
        bugDetailslist1[1] = bugNo;
        BugDetailsDAO budao = new BugDetailsDAO();
        budao.saveEditBugdetails(bugDetailslist, bugDetailslist1);



        return null;

    }

//
    public ActionForward deleteBugdetails(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        PbReturnObject prgr = new PbReturnObject();
        PrintWriter Out = response.getWriter();
        String bugid = request.getParameter("bugid");
        //////////////////////.println("bugid " + bugid);
        BugDetailsDAO budao = new BugDetailsDAO();
        budao.deleteBugdetails(bugid);



        return null;

    }

    public ActionForward saveUserDetails(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        String username = request.getParameter("username");
        String companyname = request.getParameter("companyname");
        String customerid = request.getParameter("companyname");
        String emailid = request.getParameter("mailid");
        String[] userlist = new String[4];
        userlist[0] = username;
        userlist[1] = companyname;
        userlist[2] = customerid;
        userlist[3] = emailid;
        BugDetailsDAO budao = new BugDetailsDAO();
        budao.saveUserDetails(userlist);

        return null;
    }

    public ActionForward saveAssignDetails(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        PrintWriter Out = response.getWriter();
        String selectedbuglist = request.getParameter("checkedList");
        String assignedusers = request.getParameter("assignedUsers");
        //////////////////////.println("selectedbuglist:===="+selectedbuglist);
        //////////////////////.println("assignedusers:"+assignedusers);
        String[] userlist = new String[2];
        userlist[0] = assignedusers;
        userlist[1] = selectedbuglist;

        BugDetailsDAO budao = new BugDetailsDAO();
        boolean result = budao.saveAssignDetails(userlist);
        Out.print(result);
        return null;
    }
}
