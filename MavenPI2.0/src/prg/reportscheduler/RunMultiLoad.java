/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.reportscheduler;

import com.progen.userlayer.db.LogReadWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import prg.db.SourceConn;

/**
 *
 * @author Mohit //for Veraction etl
 */
public class RunMultiLoad extends Thread {

    public static Logger logger = Logger.getLogger(RunMultiLoad.class);

    LogReadWriter logrw = new LogReadWriter();
    static HashMap<String,ArrayList> clientInfo=new HashMap<>();
   static ArrayList<String> allClientCodes=new ArrayList<>();
    public static String filename = "";
    public  static  int totalThreads = 0;
    public static int totalClients = 0;
    public static Connection con = null;
     public static Thread myThreads[]= null;
    
    
    @Override
    public void run() {
            try {     
                
                logger.info("Thread started");
            HashMap<String, ArrayList> createThreads = createThreads();
               
            if(createThreads!=null && createThreads.size()>0)
            {
        CallableStatement proc = null;
        Connection con1 = null;
final SourceConn sc = new SourceConn();
  con1 = sc.getConnection("VR_BETA", "", "", "", "", "", "", "", "");
  String key=createThreads.keySet().toArray()[0].toString();
  ArrayList<String> accpds=createThreads.get(key);
  
  for (int i = 0; i < accpds.size(); i++) {
//            proc = con1.prepareCall("{ call (?,?) }");
//             proc.setString(1, key);
//             proc.setString(2, accpds.get(i));

logger.info("Procedure Started for Client="+key+" and Accounting Period="+accpds.get(i)+"  @"+new Date());
 logrw.fileWriterWithFileName("Procedure Started for Client="+key+" and Accounting Period="+accpds.get(i)+"  @"+new Date()+"\n", filename);
//             proc.execute();

logger.info("Procedure Completed for Client="+key+" and Accounting Period="+accpds.get(i)+"  @"+new Date());
 logrw.fileWriterWithFileName("Procedure Completed for Client="+key+" and Accounting Period="+accpds.get(i)+"  @"+new Date()+"\n", filename);
                                             

                      }                 
            con1.close();
            decrementCount();
            
            }
            
            
        }
        catch (Exception ex) {
            java.util.logging.Logger.getLogger(RunMultiLoad.class.getName()).log(Level.SEVERE, null, ex);
        }            finally
            {
                
            }
//catch (Exception ex) {
////            logger.error("Exception: ", ex);
//////        }
    }
    public synchronized HashMap<String,ArrayList> createThreads() throws ClassNotFoundException {
     
HashMap<String,ArrayList> oneclientInfo=new HashMap<>();
        
        oneclientInfo.put(allClientCodes.get(totalClients), clientInfo.get(allClientCodes.get(totalClients)));
      totalThreads++;
     totalClients++;
return oneclientInfo;
    }

    public synchronized void decrementCount() {
//              
        totalThreads--;
        if(totalClients < allClientCodes.size())
        {
             RunMultiLoad.myThreads[totalClients] = new Thread(new RunMultiLoad());
             RunMultiLoad.myThreads[totalClients].start();
        }
        
        try {
//            logrw.fileWriterWithFileName("{ call cs_dw." + IndependentProcs[objectNo] + "() }", filename);
//                          
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        // return proc;

    }
    public synchronized void incrementCount() {
//              
        totalThreads++;
     totalClients++;
        try {
//            logrw.fileWriterWithFileName("{ call cs_dw." + IndependentProcs[objectNo] + "() }", filename);
//                          
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        // return proc;

    }

}