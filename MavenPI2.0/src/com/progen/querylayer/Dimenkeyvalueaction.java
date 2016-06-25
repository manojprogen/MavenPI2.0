/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class Dimenkeyvalueaction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(Dimenkeyvalueaction.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    private PbReturnObject rs;

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("testQuery.key", "testQuery");
        return map;
    }

    public ActionForward testQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        DimenkeyvalueResourceBundle resBundle = new DimenkeyvalueResourceBundle();
        PbDb pbdb = new PbDb();
        String finalQuery = "";
        ArrayList list = new ArrayList();
        Connection con = null;
        BusinessGroupDAO businessgroupdao = new BusinessGroupDAO();

        try {

            //tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orival='+orival+'&colType='+colType,
            String tableName = request.getParameter("tableName");
            String colName = request.getParameter("colName");
            String connectionId = request.getParameter("connectionId");
            // int id1 = Integer.parseInt(request.getParameter("id1"));
            //  int id2 = Integer.parseInt(request.getParameter("id2"));
            String colList = request.getParameter("colList");
            String orival = request.getParameter("orivalue");
            orival = orival.replace("~", "%");
            String colType = request.getParameter("colType");
            Connection conn = businessgroupdao.getConnectionIdConnection(connectionId);
            String testQueryDet = resBundle.getString("testQuery");
            Object obj[] = new Object[4];
            obj[0] = colName;
            obj[1] = tableName;
            obj[2] = colName;
            obj[3] = orival;
            //  ////////////////////////////////////////////////////////////////////////////////.println.println("orival"+orival);
            finalQuery = pbdb.buildQuery(testQueryDet, obj);
            // ////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery----------"+finalQuery);
            //  ////////////////////////////////////////////////////////////////////////////////.println.println("---collist----"+colList);

            PbReturnObject pbretobj = pbdb.execSelectSQL(finalQuery, conn);
            PrintWriter out = response.getWriter();

            //  ////////////////////////////////////////////////////////////////////////////////.println.println("pbretobj "+pbretobj.getRowCount());

            String existcolNames[] = colList.split(",");
            for (int i = 0; i < pbretobj.getRowCount(); i++) {
                int count = 0;
                for (int j = 0; j < existcolNames.length; j++) {
                    if (pbretobj.getFieldValueString(i, 0).equalsIgnoreCase(existcolNames[j])) {
                        count = 1;
                        break;
                    }
                }
                if (count == 0) {
                    list.add(pbretobj.getFieldValueString(i, 0));
                }
                // ////////////////////////////////////////////////////////////////////////////////.println.println(pbretobj.getFieldValueString(i, 0));
            }
            out.print(list);
            return null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward DimentionDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        DimenkeyvalueResourceBundle resBundle = new DimenkeyvalueResourceBundle();
        PbDb pbdb = new PbDb();
        String finalQuery = "";
        ArrayList list = new ArrayList();
        Connection con = null;
        BusinessGroupDAO businessgroupdao = new BusinessGroupDAO();

        try {

            //tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orival='+orival+'&colType='+colType,
            String dimentionID = request.getParameter("qryDimIdToUpdate");
            //Connection conn=businessgroupdao.getConnectionIdConnection(connectionId);
            String DimentionDetails = resBundle.getString("DimentionDetails");
            Object obj[] = new Object[1];
            obj[0] = dimentionID;


            finalQuery = pbdb.buildQuery(DimentionDetails, obj);
            ////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery----------"+finalQuery);

            PbReturnObject pbretobj = pbdb.execSelectSQL(finalQuery);
            PrintWriter out = response.getWriter();

            ////////////////////////////////////////////////////////////////////////////////.println.println("pbretobj "+pbretobj.getRowCount());
            for (int i = 0; i < pbretobj.getRowCount(); i++) {
                list.add(pbretobj.getFieldValueString(i, 0));
                ////////////////////////////////////////////////////////////////////////////////.println.println(pbretobj.getFieldValueString(i, 0));
            }
            out.print(list);
            return null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }
}