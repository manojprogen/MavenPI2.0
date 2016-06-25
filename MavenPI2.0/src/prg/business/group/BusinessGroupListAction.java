package prg.business.group;

import com.progen.dimensions.DimensionsDAO;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BusinessGroupListAction extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(BusinessGroupListAction.class);
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String method = "";
        try {
            if (request.getParameter("method") != null) {
                method = request.getParameter("method");
            }

            ////.println("method\t"+method);
            DynamicBusinessGroupDAO dynabusrolesdao = new DynamicBusinessGroupDAO();
            if (method.equalsIgnoreCase("dynamicdims")) {
                String connid = request.getParameter("connid");
                session.setAttribute("connId", connid);
                String bussgrpstr = dynabusrolesdao.getBusinessGroups(connid);
                BusinessGroupDAO businessGroupDAO = new BusinessGroupDAO();
                DimensionsDAO dimensionsDAO = new DimensionsDAO();
                String tableList = businessGroupDAO.getTableNamesBsgrp(connid);
                //////////////////////////////////////////////////////////////////////////////////////.println("tableList is " + tableList);
                ////.println("connid is : "+connid);
                String dimensionList = dimensionsDAO.getDimensionsListBsgrp(connid);
                //////////////////////////////////////////////////////////////////////////////////////.println("" + dimensionList.size());
                request.setAttribute("dimensionList", dimensionList);
                request.setAttribute("list", tableList);
                request.setAttribute("bussgrpstr", bussgrpstr);
                return mapping.findForward("dynamicdimensions");
            } else if (method.equalsIgnoreCase("builddimensions")) {
                PrintWriter out = response.getWriter();
                String grpid = request.getParameter("grpid");
                String dimstring = dynabusrolesdao.getDimensions(grpid);
                // //////////////////.println("dimstring" + dimstring);
                out.print(dimstring);
                return null;
            } else if (method.equalsIgnoreCase("buildbuckets")) {
                PrintWriter out = response.getWriter();
                String bktid = request.getParameter("bktid");
                String bucketstr = dynabusrolesdao.getBuckets(bktid);
                //////////////////.println("bucketstr" + bucketstr);
                out.print(bucketstr);
                return null;
            } else if (method.equalsIgnoreCase("buildfacts")) {
                PrintWriter out = response.getWriter();
                String factid = request.getParameter("factid");
                String factstr = dynabusrolesdao.getFacts(factid);
                ////////////////////.println("factstr" + factstr);
                out.print(factstr);
                return null;
            } else if (method.equalsIgnoreCase("buildtrgetmeausers")) {
                PrintWriter out = response.getWriter();
                String trgtmsrid = request.getParameter("trgtmsrid");
                String targetmsrstr = dynabusrolesdao.getAddedTargets(trgtmsrid);
                // //////////////////.println("factstr" + targetmsrstr);
                out.print(targetmsrstr);
                return null;
            } else if (method.equalsIgnoreCase("buildAddtables")) {
                PrintWriter out = response.getWriter();
                String alltablesid = request.getParameter("alltablesid");
                String alltablesidstr = dynabusrolesdao.getAllTables(alltablesid);
                // //////////////////.println("factstr" + alltablesidstr);
                out.print(alltablesidstr);
                return null;
            } else if (method.equalsIgnoreCase("buildbussroles")) {
                PrintWriter out = response.getWriter();
                String bussroleid = request.getParameter("bussroleid");
                String bussroleidstr = dynabusrolesdao.getBusinessRoles(bussroleid);
                ////////////////////.println("factstr" + bussroleidstr);
                out.print(bussroleidstr);
                return null;
            } else if (method.equalsIgnoreCase("changedatabase")) {
                String connid = request.getParameter("connid");
                //////////////////.println("connid" + connid);
                session.removeAttribute("connId");
                session.setAttribute("connId", connid);
                String bussgrpstr = dynabusrolesdao.getBusinessGroups(connid);
                request.setAttribute("bussgrpstr", bussgrpstr);
                return null;
            } else if (method.equalsIgnoreCase("getBussGrpDetails")) {
                PrintWriter out = response.getWriter();
                String bussTableId = request.getParameter("bussTableId");
                String resultStr = dynabusrolesdao.getBussGrpDetails(bussTableId);
                out.print(resultStr);
                return null;

            } else if (method.equalsIgnoreCase("saveAdditionalTimeDim")) {
                PrintWriter out = response.getWriter();
                String bussTabId = request.getParameter("bussTabId");
                String bussColumnNames = request.getParameter("bussColumnNames");
                String bussColuIds = request.getParameter("bussColuIds");
                String[] buscolNameArray = null;
                if (!bussColumnNames.equalsIgnoreCase("null")) {
                    buscolNameArray = bussColumnNames.split(",");
                }
                boolean result = dynabusrolesdao.saveAdditionalTimeDim(bussTabId, buscolNameArray, bussColuIds);
                out.print(result);
                return null;
            } else if (method.equalsIgnoreCase("")) {
                PrintWriter out = response.getWriter();
                out.print(false);
                return null;
            } else {

                String connId = request.getParameter("connId");
                if (request.getSession(false) == null) {
                    ////////////////////////////////////////////////////////////////////////////////////.println("in if ");
                    session = request.getSession(true);

                } else {
                    ////////////////////////////////////////////////////////////////////////////////////.println("in else");
                    session = request.getSession(false);
                }
                if (connId == null) {
                    connId = String.valueOf(session.getAttribute("connId"));
                    ////////////////////////////////////////////////////////////////////////////////////.println("conId in if action--->"+connId);
                }
                session.setAttribute("connId", connId);
                ////////////////////////////////////////////////////////////////////////////////////.println("conId in action--->"+connId);

                ArrayList newList = new ArrayList();
                ////////////////////////////////////////////////////////////////////////////////////.println("HI IN ACTION");
                BusinessGroupListDAO bussGroupListDAO = new BusinessGroupListDAO();
                newList = bussGroupListDAO.getBusinessGroups(connId);
                // newList = bussGroupListDAO.getBusinessGroups();
                if (newList.size() > 0) {
                    request.setAttribute("BusinessGroupList", newList);
                }
                BusinessGroupDAO businessGroupDAO = new BusinessGroupDAO();
                DimensionsDAO dimensionsDAO = new DimensionsDAO();
                String tableList = businessGroupDAO.getTableNamesBsgrp();
                //////////////////////////////////////////////////////////////////////////////////////.println("tableList is " + tableList);
                String dimensionList = dimensionsDAO.getDimensionsListBsgrp(connId);
                //////////////////////////////////////////////////////////////////////////////////////.println("" + dimensionList.size());
                request.setAttribute("dimensionList", dimensionList);
                request.setAttribute("list", tableList);
                ////////////////////////////////////////////////////////////////////////////////////////.println("after action");
                return mapping.findForward(SUCCESS);
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
            return null;
        }
    }
}
