/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import com.progen.db.PbBaseDAO;
import com.progen.superadmin.SuperAdminBd;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEncrypter;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class UserInsert extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    private boolean isCompanyValid = false;

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //////////////////////////////////////////////////////////////////////////.println("--insert user--");
        PbDb pbdb = new PbDb();
        String puLoginId = request.getParameter("loginId");
        String puLoginId1 = puLoginId.toUpperCase();
        String puFirstName = request.getParameter("firstName");
        String puMiddleName = request.getParameter("middleName");
        String puLastName = request.getParameter("lastName");
        String puPassword = request.getParameter("password");
        PbEncrypter enc = new PbEncrypter();
        String pwd = enc.encrypt(puPassword);
        String confirmPassword = request.getParameter("confirmPassword ");
        String puEmail = request.getParameter("email");
        String puContactNo = request.getParameter("contactNo");
        String puAddress = request.getParameter("address");
        String puCity = request.getParameter("city");
        String puState = request.getParameter("state");
        String puCountry = request.getParameter("country");
        String puPin = request.getParameter("pin");
        String START_DATE = request.getParameter("START_DATE");
        String END_DATE = request.getParameter("END_DATE");
        String uType = request.getParameter("uType");
        String AssignReports = request.getParameter("AssignReports");
        //added by nazneen
        String loginUserId = request.getParameter("loginUserId");
        HttpSession session = request.getSession(false);
        String loginId = String.valueOf(session.getAttribute("LOGINID"));

        String status = null;
        String userActiveStatus = null;
        PbBaseDAO dao = new PbBaseDAO();
        int activeUserCount = 0;
        int purchasedUsersCount = 0;
        activeUserCount = dao.getActiveUsers();
        purchasedUsersCount = dao.getPurchaseUsers();
        if (purchasedUsersCount > activeUserCount) {
            userActiveStatus = "Y";
        } else {
            userActiveStatus = "N";
        }
        //added by Nazneen
        if (loginUserId.equalsIgnoreCase("362") && loginId.equalsIgnoreCase("JOHN MANITARAS")) {
            userActiveStatus = "Y";
        }

        ////.println("AssignReports--"+AssignReports);
        //--for account type added on 26-12-09
        String account = "";
        if (isCompanyValid) {

            if (request.getParameter("account") != null) {
                account = request.getParameter("account");
            }
            //////.println(" account in java "+account);

        }
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String validationQry1 = null;
        PbReturnObject pbro1 = null;
        String validationQry2 = null;
        PbReturnObject pbro2 = null;


        if (isCompanyValid) {
            String[] userList = new String[15];
            userList[0] = puLoginId1;
            userList[1] = puFirstName;
            userList[2] = puMiddleName;
            userList[3] = puLastName;
            userList[4] = pwd;
            //////////////////////////////////////////////////////////////////////////.println("pwd---"+pwd);
            // userList[5] = confirmPassword;
            userList[5] = puEmail;
            userList[6] = puContactNo;
            userList[7] = puAddress;
            userList[8] = puCity;
            userList[9] = puState;
            userList[10] = puCountry;
            userList[11] = puPin;
            userList[12] = account;
            userList[13] = account;
            userList[14] = uType;
            String maxusercountsql = "select * from prg_role_rows";
            PbReturnObject pbr = pbdb.execSelectSQL(maxusercountsql);
            int rowcount = 0;
            if (pbr.getRowCount() > 0) {

                String maxusercountsql1 = "select * from prg_ar_users";
                PbReturnObject pbr1 = pbdb.execSelectSQL(maxusercountsql1);
                if (pbr1.getRowCount() > 0) {
                    if (pbr.getRowCount() > pbr1.getRowCount()) {
                        rowcount = pbr.getRowCount() + 1;
                    } else {
                        rowcount = pbr1.getRowCount() + 1;
                    }
                } else {
                    rowcount = pbr.getRowCount() + 1;
                }
            } else {
                String maxusercountsql1 = "select * from prg_ar_users";
                PbReturnObject pbr1 = pbdb.execSelectSQL(maxusercountsql1);
                if (pbr1.getRowCount() > 0) {
                    if (rowcount > pbr1.getRowCount()) {
                        rowcount = rowcount + 1;
                    } else {
                        rowcount = pbr1.getRowCount() + 1;
                    }
                } else {
                    rowcount = rowcount + 1;
                }

            }
            userLayerDAO.insertUserPrivileges(puLoginId1, account, String.valueOf(rowcount));
//        Object Objnew[] = new Object[3];
//        Objnew[0]=enc.encrypt(puLoginId1);
//        Objnew[1]=enc.encrypt(account);
//        Objnew[2]=enc.encrypt(String.valueOf(rowcount));
//               ////.println("rowcount==="+String.valueOf(rowcount));
//        String insertUserPrivileges = "insert into prg_role_rows(count_id,USER_ID,company_id,max_count) values(prg_role_rows_seq.nextval,'&','&','&')";
//        String finInsert = pbdb.buildQuery(insertUserPrivileges, Objnew);
//        pbdb.execModifySQL(finInsert);
            //userList[13] = START_DATE;
            //userList[14] = END_DATE;
            int userId = userLayerDAO.addUserDetails(userList);
            // if(AssignReports.equalsIgnoreCase("Y")){
            String result1 = userLayerDAO.addUserRoleReports(account, puLoginId1, AssignReports);
        } else {

            validationQry1 = "SELECT ANALYZERS from PRG_TEST_DATA";
            pbro1 = pbdb.execSelectSQL(validationQry1);
            int purchasedUsers = Integer.parseInt(pbro1.getFieldValueString(0, 0));
            validationQry2 = "SELECT PU_ID from PRG_AR_USERS where USER_TYPE NOT IN ('9999')";
            pbro2 = pbdb.execSelectSQL(validationQry2);
            int existingUsers = pbro2.getRowCount();
//               if(existingUsers<purchasedUsers){
            status = userActiveStatus;
//               }else{
//                   status = "N";
//               }
            if (loginUserId.equalsIgnoreCase("362") && loginId.equalsIgnoreCase("JOHN MANITARAS")) {
                status = "Y";
            }
            String[] userList;
            int sequence = 0;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                if (uType.equalsIgnoreCase("9999")) {
                    status = "Y";
                }
                userList = new String[15];
                String seqQry = "select PRG_AR_USERS_SEQ.nextval from dual";
                PbReturnObject pbro = pbdb.execSelectSQL(seqQry);
                sequence = pbro.getFieldValueInt(0, 0);
                userList[0] = String.valueOf(sequence);
                userList[1] = puLoginId1;
                userList[2] = puFirstName;
                userList[3] = puMiddleName;
                userList[4] = puLastName;
                userList[5] = pwd;
                //////////////////////////////////////////////////////////////////////////.println("pwd---"+pwd);
                // userList[5] = confirmPassword;
                userList[6] = puEmail;
                userList[7] = puContactNo;
                userList[8] = puAddress;
                userList[9] = puCity;
                userList[10] = puState;
                userList[11] = puCountry;
                userList[12] = puPin;
                // userList[12] =account;
                // userList[13] =account;
                userList[13] = uType;
                userList[14] = status;
            } else {
                validationQry1 = "SELECT ANALYZERS from PRG_TEST_DATA";
                pbro1 = pbdb.execSelectSQL(validationQry1);
                purchasedUsers = Integer.parseInt(pbro1.getFieldValueString(0, 0));
                validationQry2 = "SELECT PU_ID from PRG_AR_USERS where USER_TYPE=10002";
                pbro2 = pbdb.execSelectSQL(validationQry2);
                existingUsers = pbro2.getRowCount();
//               if(existingUsers<=purchasedUsers){
                status = userActiveStatus;
//               }else{
//                   status = "N";
//               }
                userList = new String[14];
                userList[0] = puLoginId1;
                userList[1] = puFirstName;
                userList[2] = puMiddleName;
                userList[3] = puLastName;
                userList[4] = pwd;
                //////////////////////////////////////////////////////////////////////////.println("pwd---"+pwd);
                // userList[5] = confirmPassword;
                userList[5] = puEmail;
                userList[6] = puContactNo;
                userList[7] = puAddress;
                userList[8] = puCity;
                userList[9] = puState;
                userList[10] = puCountry;
                userList[11] = puPin;
                // userList[12] =account;
                // userList[13] =account;
                userList[12] = uType;
                userList[13] = status;
            }

            //userList[13] = START_DATE;
            // userList[14] = END_DATE;
            int userId = userLayerDAO.addUserDetails(userList);
            if (userId == -1) {
                userId = sequence;
            }
            //assign privelges to user
            SuperAdminBd adminBd = new SuperAdminBd();
            adminBd.deleteModulesForUserType(userId);
            adminBd.saveModulesForUserType(request.getSession(false), userId, Integer.parseInt(uType));
            //added by veena for assigning Modules to users
            adminBd.saveAssignedModules(userId, Integer.parseInt(uType), puLoginId1, status);

        }



        ////////////////////////////////////////////////////////////////////////.println("aaa--->"+userList.length);


        // }



        return mapping.findForward(SUCCESS);

    }
}
