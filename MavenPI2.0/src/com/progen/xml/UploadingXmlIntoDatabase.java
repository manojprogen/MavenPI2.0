/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//Now only for Leela project...
package com.progen.xml;

/**
 *
 * @author Mohit
 */
import com.progen.userlayer.db.LogReadWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.SourceConn;
import prg.util.PbMail;
import utils.db.ProgenConnection;
import org.apache.log4j.*;

public class UploadingXmlIntoDatabase {

    public static Logger logger = Logger.getLogger(UploadingXmlIntoDatabase.class);

    public static void main(String[] args) throws XMLStreamException, IOException, SQLException {
    }
    LogReadWriter logrw = new LogReadWriter();

    public String UploadXML(String from, String subject, String DownloadLoc, String targettablename, String cont) throws XMLStreamException, IOException, SQLException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Connection con = null;
        PbMail mailer = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        String MumFin = "false";
        String MumRes = "false";
        String MumProf2 = "false";
        String MumProf3 = "false";
        String DelFin = "false";
        String DelRes = "false";
        String DelProf2 = "false";
        String DelProf3 = "false";
        String DelCC = "false";
        String BgRes = "false";
        String BgProf2 = "false";
        String BgProf3 = "false";
        String BgFin = "false";
        String KoRes = "false";
        String KoProf2 = "false";
        String KoProf3 = "false";
        String KoFin = "false";
        String GoaRes = "false";
        String GoaProf2 = "false";
        String GoaProf3 = "false";
        String GoaFin = "false";
        String GnRes = "false";
        String GnProf2 = "false";
        String GnProf3 = "false";
        String GnFin = "false";
        String UdRes = "false";
        String UdProf2 = "false";
        String UdProf3 = "false";
        String UdFin = "false";
        String CnRes = "false";
        String CnProf2 = "false";
        String CnProf3 = "false";
        String CnFin = "false";
        String UdRooms = "false";
        String CnRooms = "false";
        String GoaRooms = "false";
        String GnRooms = "false";
        String KoRooms = "false";
        String BgRooms = "false";
        String DelRooms = "false";
        String MumRooms = "false";

        try {
            if (subject.contains("actual") || subject.contains("Actual")) {
                String PROPERTY_ID = "";
                String PROPERTY_NAME = "";
                if (from.contains("ocis.reports@theleela.com")) {
                    PROPERTY_ID = "2";
                    PROPERTY_NAME = "The Leela Mumbai";
                } else if (from.contains("rakesh.mehta@theleela.com")) {
                    PROPERTY_ID = "7";
                    PROPERTY_NAME = "The Leela Udaipur";
                } else if (from.contains("reservations.newdelhi@theleela.com")) {
                    PROPERTY_ID = "6";
                    PROPERTY_NAME = "The Leela New Delhi";
                } else if (from.contains("it.goa@theleela.com")) {
                    PROPERTY_ID = "5";
                    PROPERTY_NAME = "The Leela Goa";
                } else if (from.contains("jey.balaji@theleela.com")) {
                    PROPERTY_ID = "3";
                    PROPERTY_NAME = "The Leela Bangalore";
                } else if (from.contains("it.kovalam@theleela.com")) {
                    PROPERTY_ID = "8";
                    PROPERTY_NAME = "The Leela Kovalam";
                } else if (from.contains("reservations.gurgaon-delhi@theleela.com")) {
                    PROPERTY_ID = "1";
                    PROPERTY_NAME = "The Leela Gurgaon";
                } else if (from.contains("daniel.dsuza@theleela.com")) {
                    PROPERTY_ID = "4";
                    PROPERTY_NAME = "The Leela Chennai";
                }
                query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Actual_Checkin_Date,Conf_Number,Insert_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME)values( ?,?,?,GETDATE()," + PROPERTY_ID + ",'" + PROPERTY_NAME + "')";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                String PROPERTY_ID = "";
                String PROPERTY_NAME = "";
                if (from.contains("ocis.reports@theleela.com")) {
                    PROPERTY_ID = "2";
                    PROPERTY_NAME = "The Leela Mumbai";
                } else if (from.contains("rakesh.mehta@theleela.com")) {
                    PROPERTY_ID = "7";
                    PROPERTY_NAME = "The Leela Udaipur";
                } else if (from.contains("reservations.newdelhi@theleela.com")) {
                    PROPERTY_ID = "6";
                    PROPERTY_NAME = "The Leela New Delhi";
                } else if (from.contains("it.goa@theleela.com")) {
                    PROPERTY_ID = "5";
                    PROPERTY_NAME = "The Leela Goa";
                } else if (from.contains("jey.balaji@theleela.com")) {
                    PROPERTY_ID = "3";
                    PROPERTY_NAME = "The Leela Bangalore";
                } else if (from.contains("it.kovalam@theleela.com")) {
                    PROPERTY_ID = "8";
                    PROPERTY_NAME = "The Leela Kovalam";
                } else if (from.contains("reservations.gurgaon-delhi@theleela.com")) {
                    PROPERTY_ID = "1";
                    PROPERTY_NAME = "The Leela Gurgaon";
                } else if (from.contains("daniel.dsuza@theleela.com")) {
                    PROPERTY_ID = "4";
                    PROPERTY_NAME = "The Leela Chennai";
                }
                query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Email_Id,"
                        + "Conf_Nbr,"
                        + "Guest_Name_Id,"
                        + "Membership_Type,"
                        + "First_name,"
                        + "Last_Name,"
                        + "Address1,"
                        + "Address2,"
                        + "Address3,"
                        + "City,"
                        + "State,"
                        + "Country,"
                        + "Phone_No,"
                        + "Company_name,"
                        + "Profile_Type,"
                        + "Arrival_Date,"
                        + "Property_id,"
                        + "Property_name,"
                        + "Rec_Insert_Date)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + PROPERTY_ID + ",'" + PROPERTY_NAME + "',GETDATE())";

            } else if (from.contains("ocis.reports@theleela.com") || from.contains("mohit.jain@progenbusiness.com") || from.contains("mum.financepg@theleela.com") || from.contains("ranjana.nikam@theleela.com")) {
                if (subject.contains("finance") || subject.contains("Finance")) {
                    MumFin = "true";//62
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,Conf_Nbr ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Adjustment_Reason_Code,Adjustment_YN, Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,Cashier_ID,Check_Nbr,"
                            + "Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,Folio_Name,Food_Tax,Genrates,Gross_Amt2,Guest_Acct_Credit,Guest_Acct_Debit,"
                            + ""
                            + ""
                            + "Insert_Date,Insert_User,Internal_Yn,Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,Net_amt3,"
                            + ""
                            + ""
                            + ""
                            + "Other_Tax,Price,Product,Property,Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Revenue_Amt4,Room_Class,Room_Nbr,Room_Tax,"
                            + ""
                            + ""
                            + " Source_Code, Summery_Reference_Code,Ta_Commissionable,Tax_preferred_yn,Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,"
                            + ""
                            + ""
                            + ""
                            + "Transaction_group_Type,Transaction_Type,Transfer_Date, Gross_Amt5,Net_Amt6,Update_Date,Update_User,Creation_Date,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?     ,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?  ,?,?,GETDATE(),2,'The Leela Mumbai')";
                } else if (subject.contains("reservation") || subject.contains("Reservation") || cont.contains("Reservation_Arrival")) {
                    MumRes = "true";//83
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + " Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Room_Class,Preffered_Room_Type,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Suite_Room_Number,"
                            + ""
                            + ""
                            + ""
                            + "Market_Code_Description,Origin_Code,Party_Code,Source_Code_Description,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,Company_Type,Company_Of_The_Profile,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Departure_Time,Full_Rate_Amount10,Group_Name, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Language11,Nationality12,Promotions,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,GETDATE(),2,'The Leela Mumbai' )";

                } else if (subject.contains("profile2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    MumProf2 = "true";//70
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,"
                            + "CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( "
                            + "?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,GETDATE(),2,'The Leela Mumbai' )";


                } else if (subject.contains("Profile3") || subject.contains("profile3") || cont.contains("profile3_Arrival")) {
                    MumProf3 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,"
                            + "   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),2,'The Leela Mumbai' )";
                }

            } else if (from.contains("reservations.newdelhi@theleela.com") || from.contains("del.financepg@theleela.com")) {
                if (subject.contains("finance") || subject.contains("Finance")) {
                    DelFin = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,Conf_Nbr ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Adjustment_Reason_Code,Adjustment_YN, Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,Cashier_ID,Check_Nbr,"
                            + "Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,Folio_Name,Food_Tax,Genrates,Gross_Amt2,Guest_Acct_Credit,Guest_Acct_Debit,"
                            + ""
                            + ""
                            + "Insert_Date,Insert_User,Internal_Yn,Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,Net_amt3,"
                            + ""
                            + ""
                            + ""
                            + "Other_Tax,Price,Product,Property,Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Revenue_Amt4,Room_Class,Room_Nbr,Room_Tax,"
                            + ""
                            + ""
                            + " Source_Code, Summery_Reference_Code,Ta_Commissionable,Tax_preferred_yn,Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,"
                            + ""
                            + ""
                            + ""
                            + "Transaction_group_Type,Transaction_Type,Transfer_Date, Gross_Amt5,Net_Amt6,Update_Date,Update_User,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?     ,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,?,GETDATE(),6,'The Leela New Delhi')";
                } else if (subject.contains("test4") || subject.contains("Reservation") || cont.contains("Reservation_Arrival")) {
                    DelRes = "true";//112
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number,Full_Name ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Market_Code,Source_Code, Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Membership_Number,Membership_Type,Membership_Level,Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Country,Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Membership_Number5,Membership_Type6,Membership_Level7,Room_Class,Preffered_Room_Type,Corporate_Id_IATA,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Name_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Commission_Code,Suite_Room_Number,Industry_Code,"
                            + ""
                            + ""
                            + ""
                            + "Job_Title_Code,Language,Market_Code_Description,Origin_Code,Nationality,Party_Code,Source_Code_Description,AR_Number,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Negotiated_Rate,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,City,Company_Type,Company_Of_The_Profile,Contact_YN,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Date_Of_Birth,Department,Departure_Time,Full_Rate_Amount10,Group_Name,Group_Of_Last_Reservation, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Identification_Type,Identification_Number,Language11,Nationality12,Promotions,Profile_Type,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,State14,Commission_Bank,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,   ?,?,GETDATE(),6,'The Leela New Delhi' )";

                } else if (subject.contains("Test_Profile_08_2 - test_profile_08_2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    DelProf2 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,Cnl_Res_LY,"
                            + "Cnl_Res_TY,Cnl_Rms_LY,Cnl_Rms_TY,CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,Extra_Rev_LY,Extra_Rev_TY,F_And_B_Rev_LY,F_And_B_Rev_TY,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,GETDATE(),6,'The Leela New Delhi' )";
                } else if (subject.contains("Test_Profile_08_3 - test_profile_08_3") || subject.contains("profile3") || cont.contains("profile3_Arrival")) {
                    DelProf3 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),6,'The Leela New Delhi' )";

                } else if (subject.contains("Test_Profile_08_02_Rem_Col")) {
                    DelCC = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Conf_Nbr,Credit_Card_Exp_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,GETDATE(),6,'The Leela New Delhi' )";
                }

            } else if (from.contains("jey.balaji@theleela.com") || from.contains("bg.financepg@theleela.com") || (from.contains("is.bangalore@theleela.com") && subject.contains("finance"))) {
                if (subject.contains("Finance") || subject.contains("finance")) {
                    BgFin = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,Adjustment_Reason_Code,\n"
                            + "Adjustment_YN,Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,\n"
                            + "Cashier_ID,Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,\n"
                            + "Folio_Name,Food_Tax,Genrates,Gross_Amt2,Guest_Acct_Credit,Guest_Acct_Debit,Insert_Date,Insert_User,Internal_Yn,\n"
                            + "Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,Net_amt3,Other_Tax,Price,Product,Property,\n"
                            + "Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Revenue_Amt4,Room_Class,\n"
                            + "Room_Nbr,Room_Tax,Source_Code,Summery_Reference_Code,Ta_Commissionable,Tax_preferred_yn,\n"
                            + "Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,Transaction_group_Type,Transaction_Type,\n"
                            + "Transfer_Date,Gross_Amt5,Net_Amt6,Update_Date,Update_User,Conf_Nbr,Creation_Date,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values(?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?     ,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,?,GETDATE(),3,'The Leela Bangalore')";

                } else if (subject.contains("Reservation") || subject.contains("reservation") || cont.contains("Reservation_Arrival")) {
                    BgRes = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number,Full_Name ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Market_Code,Source_Code, Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Membership_Number,Membership_Type,Membership_Level,Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Country,Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Membership_Number5,Membership_Type6,Membership_Level7,Room_Class,Preffered_Room_Type,Corporate_Id_IATA,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Name_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Commission_Code,Suite_Room_Number,Industry_Code,"
                            + ""
                            + ""
                            + ""
                            + "Job_Title_Code,Language,Market_Code_Description,Origin_Code,Nationality,Party_Code,Source_Code_Description,AR_Number,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Negotiated_Rate,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,City,Company_Type,Company_Of_The_Profile,Contact_YN,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Date_Of_Birth,Department,Departure_Time,Full_Rate_Amount10,Group_Name,Group_Of_Last_Reservation, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Identification_Type,Identification_Number,Language11,Nationality12,Promotions,Profile_Type,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,State14,Commission_Bank,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,   ?,?,GETDATE(),3,'The Leela Bangalore' )";

                } else if (subject.contains("profile2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    BgProf2 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,Cnl_Res_LY,"
                            + "Cnl_Res_TY,Cnl_Rms_LY,Cnl_Rms_TY,CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,Extra_Rev_LY,Extra_Rev_TY,F_And_B_Rev_LY,F_And_B_Rev_TY,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,GETDATE(),3,'The Leela Bangalore'  )";


                } else if (subject.contains("profile3") || subject.contains("Profile3") || cont.contains("profile3_Arrival")) {
                    BgProf3 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),3,'The Leela Bangalore' )";

                }

            } else if (from.contains("it.kovalam@theleela.com") || from.contains("kov.financepg@theleela.com")) {
                if (subject.contains("finance") || subject.contains("Finance")) {
                    KoFin = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + " (Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,Conf_Nbr ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Adjustment_Reason_Code,Adjustment_YN, Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,Cashier_ID,Check_Nbr,"
                            + "Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,Folio_Name,Food_Tax,Genrates,Guest_Acct_Credit,Guest_Acct_Debit,"
                            + ""
                            + ""
                            + "Insert_Date,Insert_User,Internal_Yn,Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,"
                            + ""
                            + ""
                            + ""
                            + "Other_Tax,Price,Product,Property,Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Room_Class,Room_Nbr,Room_Tax,"
                            + ""
                            + ""
                            + " Source_Code, Summery_Reference_Code,Ta_Commissionable,Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,"
                            + ""
                            + ""
                            + ""
                            + "Transaction_group_Type,Transaction_Type,Transfer_Date,Update_Date,Update_User,Creation_Date,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values(?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?     ,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,GETDATE(),8,'The Leela Kovalam')";

                } else if (subject.contains("reservations") || subject.contains("Reservation") || cont.contains("Reservation_Arrival")) {
                    KoRes = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number,Full_Name ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Market_Code,Source_Code, Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Membership_Number,Membership_Type,Membership_Level,Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Country,Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Membership_Number5,Membership_Type6,Membership_Level7,Room_Class,Preffered_Room_Type,Corporate_Id_IATA,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Name_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Commission_Code,Suite_Room_Number,Industry_Code,"
                            + ""
                            + ""
                            + ""
                            + "Job_Title_Code,Language,Market_Code_Description,Origin_Code,Nationality,Party_Code,Source_Code_Description,AR_Number,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Negotiated_Rate,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,City,Company_Type,Company_Of_The_Profile,Contact_YN,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Date_Of_Birth,Department,Departure_Time,Full_Rate_Amount10,Group_Name,Group_Of_Last_Reservation, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Identification_Type,Identification_Number,Language11,Nationality12,Promotions,Profile_Type,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,State14,Commission_Bank,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,   ?,?,GETDATE(),8,'The Leela Kovalam' )";


                } else if (subject.contains("profile2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    KoProf2 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,Cnl_Res_LY,"
                            + "Cnl_Res_TY,Cnl_Rms_LY,Cnl_Rms_TY,CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,Extra_Rev_LY,Extra_Rev_TY,F_And_B_Rev_LY,F_And_B_Rev_TY,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( "
                            + "?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,GETDATE(),8,'The Leela Kovalam'  )";


                } else if (subject.contains("profile3") || subject.contains("Profile3") || cont.contains("profile3_Arrival")) {
                    KoProf3 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,"
                            + "   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),8,'The Leela Kovalam' )";
                }

            } else if (from.contains("it.goa@theleela.com") || from.contains("goa.financepg@theleela.com")) {
                if (subject.contains("finance") || subject.contains("Finance")) {
                    GoaFin = "true";//60
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Adjustment_Reason_Code,Adjustment_YN, Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,Cashier_ID,"
                            + "Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,Folio_Name,Food_Tax,Genrates,Gross_Amt2,Guest_Acct_Credit,Guest_Acct_Debit,"
                            + ""
                            + ""
                            + "Insert_Date,Insert_User,Internal_Yn,Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,Net_amt3,"
                            + ""
                            + ""
                            + ""
                            + "Other_Tax,Price,Product,Property,Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Revenue_Amt4,Room_Class,Room_Nbr,Room_Tax,"
                            + ""
                            + ""
                            + " Source_Code, Summery_Reference_Code,Ta_Commissionable,Tax_preferred_yn,Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,"
                            + ""
                            + ""
                            + ""
                            + "Transaction_group_Type,Transaction_Type,Transfer_Date, Gross_Amt5,Net_Amt6,Update_Date,Update_User,Conf_Nbr,Creation_Date,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?     ,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,?,?,GETDATE(),5,'The Leela Goa')";

                } else if (subject.contains("reservation") || subject.contains("Reservation") || cont.contains("Reservation_Arrival")) {
                    GoaRes = "true";//112
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number,Full_Name ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Market_Code,Source_Code, Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Membership_Number,Membership_Type,Membership_Level,Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Country,Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Membership_Number5,Membership_Type6,Membership_Level7,Room_Class,Preffered_Room_Type,Corporate_Id_IATA,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Name_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Commission_Code,Suite_Room_Number,Industry_Code,"
                            + ""
                            + ""
                            + ""
                            + "Job_Title_Code,Language,Market_Code_Description,Origin_Code,Nationality,Party_Code,Source_Code_Description,AR_Number,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Negotiated_Rate,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,City,Company_Type,Company_Of_The_Profile,Contact_YN,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Date_Of_Birth,Department,Departure_Time,Full_Rate_Amount10,Group_Name,Group_Of_Last_Reservation, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Identification_Type,Identification_Number,Language11,Nationality12,Promotions,Profile_Type,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,State14,Commission_Bank,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,   ?,?,GETDATE(),5,'The Leela Goa' )";

                } else if (subject.contains("profil2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    GoaProf2 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,Cnl_Res_LY,"
                            + "Cnl_Res_TY,Cnl_Rms_LY,Cnl_Rms_TY,CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,Extra_Rev_LY,Extra_Rev_TY,F_And_B_Rev_LY,F_And_B_Rev_TY,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( "
                            + "?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,GETDATE(),5,'The Leela Goa'  )";


                } else if (subject.contains("profil3") || subject.contains("Profile3") || cont.contains("profile3_Arrival")) {
                    GoaProf3 = "true";
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),5,'The Leela Goa' )";

                }

            } else if (from.contains("reservations.gurgaon-delhi@theleela.com") || from.contains("ggn.financepg@theleela.com")) {
                if (subject.contains("finance") || subject.contains("Finance")) {
                    GnFin = "true";//61
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,Conf_Nbr ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Adjustment_Reason_Code,Adjustment_YN, Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,Cashier_ID,Check_Nbr,"
                            + "Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,Folio_Name,Food_Tax,Genrates,Gross_Amt2,Guest_Acct_Credit,Guest_Acct_Debit,"
                            + ""
                            + ""
                            + "Insert_Date,Insert_User,Internal_Yn,Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,Net_amt3,"
                            + ""
                            + ""
                            + ""
                            + "Other_Tax,Price,Product,Property,Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Revenue_Amt4,Room_Class,Room_Nbr,Room_Tax,"
                            + ""
                            + ""
                            + " Source_Code, Summery_Reference_Code,Ta_Commissionable,Tax_preferred_yn,Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,"
                            + ""
                            + ""
                            + ""
                            + "Transaction_group_Type,Transaction_Type,Transfer_Date, Gross_Amt5,Net_Amt6,Update_Date,Update_User,Creation_Date,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?     ,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,?,?,GETDATE(),1,'The Leela Gurgaon')";

                } else if (subject.contains("reservation") || subject.contains("Reservation") || cont.contains("Reservation_Arrival")) {
                    GnRes = "true";//112
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number,Full_Name ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Market_Code,Source_Code, Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Membership_Number,Membership_Type,Membership_Level,Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Country,Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Membership_Number5,Membership_Type6,Membership_Level7,Room_Class,Preffered_Room_Type,Corporate_Id_IATA,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Name_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Commission_Code,Suite_Room_Number,Industry_Code,"
                            + ""
                            + ""
                            + ""
                            + "Job_Title_Code,Language,Market_Code_Description,Origin_Code,Nationality,Party_Code,Source_Code_Description,AR_Number,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Negotiated_Rate,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,City,Company_Type,Company_Of_The_Profile,Contact_YN,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Date_Of_Birth,Department,Departure_Time,Full_Rate_Amount10,Group_Name,Group_Of_Last_Reservation, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Identification_Type,Identification_Number,Language11,Nationality12,Promotions,Profile_Type,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,State14,Commission_Bank,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,   ?,?,GETDATE(),1,'The Leela Gurgaon' )";

                } else if (subject.contains("profile2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    GnProf2 = "true";//78
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,Cnl_Res_LY,"
                            + "Cnl_Res_TY,Cnl_Rms_LY,Cnl_Rms_TY,CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,Extra_Rev_LY,Extra_Rev_TY,F_And_B_Rev_LY,F_And_B_Rev_TY,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,GETDATE(),1,'The Leela Gurgaon'  )";


                } else if (subject.contains("profile3") || subject.contains("Profile3") || cont.contains("profile3_Arrival")) {
                    GnProf3 = "true";//87
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),1,'The Leela Gurgaon' )";

                }

            } else if (from.contains("rakesh.mehta@theleela.com") || from.contains("udp.financepg@theleela.com")) {
                if (subject.contains("finance") || subject.contains("Finance")) {
                    UdFin = "true";//61
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,Conf_Nbr ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Adjustment_Reason_Code,Adjustment_YN, Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,Cashier_ID,Check_Nbr,"
                            + "Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,Folio_Name,Food_Tax,Genrates,Gross_Amt2,Guest_Acct_Credit,Guest_Acct_Debit,"
                            + ""
                            + ""
                            + "Insert_Date,Insert_User,Internal_Yn,Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,Net_amt3,"
                            + ""
                            + ""
                            + ""
                            + "Other_Tax,Price,Product,Property,Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Revenue_Amt4,Room_Class,Room_Nbr,Room_Tax,"
                            + ""
                            + ""
                            + " Source_Code, Summery_Reference_Code,Ta_Commissionable,Tax_preferred_yn,Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,"
                            + ""
                            + ""
                            + ""
                            + "Transaction_group_Type,Transaction_Type,Transfer_Date, Gross_Amt5,Net_Amt6,Update_Date,Update_User,Creation_Date,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,?,?,GETDATE(),7,'The Leela Udaipur')";

                } else if (subject.contains("reservation") || subject.contains("servation") || subject.contains("Reservation") || cont.contains("Reservation_Arrival")) {
                    UdRes = "true";//112
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number,Full_Name ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Market_Code,Source_Code, Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Membership_Number,Membership_Type,Membership_Level,Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Country,Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Membership_Number5,Membership_Type6,Membership_Level7,Room_Class,Preffered_Room_Type,Corporate_Id_IATA,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Name_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Commission_Code,Suite_Room_Number,Industry_Code,"
                            + ""
                            + ""
                            + ""
                            + "Job_Title_Code,Language,Market_Code_Description,Origin_Code,Nationality,Party_Code,Source_Code_Description,AR_Number,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Negotiated_Rate,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,City,Company_Type,Company_Of_The_Profile,Contact_YN,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Date_Of_Birth,Department,Departure_Time,Full_Rate_Amount10,Group_Name,Group_Of_Last_Reservation, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Identification_Type,Identification_Number,Language11,Nationality12,Promotions,Profile_Type,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,State14,Commission_Bank,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,   ?,?,GETDATE(),7,'The Leela Udaipur' )";

                } else if (subject.contains("profile2") || subject.contains("rofile 2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    UdProf2 = "true";//78
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,Cnl_Res_LY,"
                            + "Cnl_Res_TY,Cnl_Rms_LY,Cnl_Rms_TY,CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,Extra_Rev_LY,Extra_Rev_TY,F_And_B_Rev_LY,F_And_B_Rev_TY,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,GETDATE(),7,'The Leela Udaipur'  )";


                } else if (subject.contains("profile3") || subject.contains("rofile 3") || subject.contains("Profile3") || cont.contains("profile3_Arrival")) {
                    UdProf3 = "true";//87
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),7,'The Leela Udaipur' )";

                }

            } else if (from.contains("daniel.dsuza@theleela.com") || from.contains("chn.financepg@theleela.com")) {
                if (subject.contains("finance") || subject.contains("Finance")) {
                    CnFin = "true";//61
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Transaction_Date,Net_Amt,Transaction_Code_Group,Transaction_Amt,Revenue_Amt,Gross_Amt,Conf_Nbr ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Adjustment_Reason_Code,Adjustment_YN, Amount_Posted,Arrangement_Code,Arrangement_Desc,Bill_No,Business_Date_Of_Transaction,Cashier_ID,Check_Nbr,"
                            + "Currency_Code,Deposite_Ledger_Credit,Deposite_Ledger_Debit,Fixed_Charge_yn,Folio_Name,Food_Tax,Genrates,Gross_Amt2,Guest_Acct_Credit,Guest_Acct_Debit,"
                            + ""
                            + ""
                            + "Insert_Date,Insert_User,Internal_Yn,Invoice_Closing_Date,Invoice_Type,Market_Code,Minibar_Tax,Net_amt3,"
                            + ""
                            + ""
                            + ""
                            + "Other_Tax,Price,Product,Property,Quantity_Of_Product,Rate_Code,Receipt_Nbr,Reference_Nbr,Revenue_Amt4,Room_Class,Room_Nbr,Room_Tax,"
                            + ""
                            + ""
                            + " Source_Code, Summery_Reference_Code,Ta_Commissionable,Tax_preferred_yn,Transaction_Code,Transaction_Code_Desc,Transaction_Code_subgroup,"
                            + ""
                            + ""
                            + ""
                            + "Transaction_group_Type,Transaction_Type,Transfer_Date, Gross_Amt5,Net_Amt6,Update_Date,Update_User,Creation_Date,Payment_Method,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,?,?,GETDATE(),4,'The Leela Chennai')";

                } else if (subject.contains("reservation") || subject.contains("Reservation") || cont.contains("Reservation_Arrival")) {
                    CnRes = "true";//112
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Room_Number,Arrival_Date,Actual_Checkin_Date,Departure_Date,Actual_Departure_Date,Conf_Number,Full_Name ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Market_Code,Source_Code, Rate_Code,Rate_Amount ,Fixed_Rate_YN,Discount_Amount,Discount_Percent,Discount_Reason,Payment_Amount,"
                            + "Membership_Number,Membership_Type,Membership_Level,Extra_Revenue_LY,Extra_Revenue_TY,F_And_B_Revenue_LY,F_And_B_Revenue_TY,F_And_B_Revenue,Room_Revenue_Balance,Total_Room_Revenue,"
                            + ""
                            + ""
                            + "Room_Revenue_Balance2,Room_Revenue_LY,Room_Revenue_TY, Total_F_And_B_Revenue ,Total_Revenue,Total_Revenue3,Total_Revenue_LY,Total_Revenue_TY,Total_Room_Revenue4 ,"
                            + ""
                            + ""
                            + ""
                            + "Country,Room_Type,Company_Name,Travel_Agent_Name,Source_Name,Membership_Number5,Membership_Type6,Membership_Level7,Room_Class,Preffered_Room_Type,Corporate_Id_IATA,Company_Name_Id,"
                            + ""
                            + ""
                            + " Event_Id, Group_Name_Id,Guest_Name_Id,Membership_Id,Name_Id,Last_Room_Number,Room_Number8,"
                            + ""
                            + ""
                            + ""
                            + "Parent_Reservation_Name_Id,Reservation_Name_Id,Share_Id , Extension_Id,Last_Room ,Commission_Code,Suite_Room_Number,Industry_Code,"
                            + ""
                            + ""
                            + ""
                            + "Job_Title_Code,Language,Market_Code_Description,Origin_Code,Nationality,Party_Code,Source_Code_Description,AR_Number,Fixed_Rate_YN9,"
                            + ""
                            + ""
                            + ""
                            + "Full_Rate_Amount,Hurdle_Rate,Last_Rate ,Multiple_Fixed_Rate_YN,Multiple_Rate_Code_YN,Negotiated_Rate,Arrival_Carrier_Code,Arrival_Time,"
                            + ""
                            + ""
                            + "Arrival_Transport_Code,Arrival_Transport_Type,Billing_Contact,Cancellatin_Number,Cancellation_Date,City,Company_Type,Company_Of_The_Profile,Contact_YN,"
                            + ""
                            + ""
                            + "Contract_Number,Country_Description,Date_Of_Birth,Department,Departure_Time,Full_Rate_Amount10,Group_Name,Group_Of_Last_Reservation, "
                            + ""
                            + ""
                            + "Guarantee_Code_Description,Identification_Type,Identification_Number,Language11,Nationality12,Promotions,Profile_Type,Reservation_Status,Source_Code_Description13,"
                            + ""
                            + ""
                            + ""
                            + "State,State14,Commission_Bank,Travel_Agent_Locator,Travel_Agent_Name_Id,Walkin_YN,No_Of_Rooms,rec_insert_date,PROPERTY_ID,PROPERTY_NAME ) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,   ?,?,GETDATE(),4,'The Leela Chennai' )";

                } else if (subject.contains("profile2") || subject.contains("Profile2") || cont.contains("profile2_Arrival")) {
                    CnProf2 = "true";//78
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Account_Type,Action_Code_Mail_Action,Action_Code_Mail_Action2,Active_YN,Address_Type,Alien_Registration_Number,Ar_Number ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Arr_Rms_LY,Arr_Rms_TY, Attachment_YN,[Billing Contact],Billing_Instructions,Birth_Country,Birth_Place,Blacklist_Message,Cnl_Res_LY,"
                            + "Cnl_Res_TY,Cnl_Rms_LY,Cnl_Rms_TY,CIS_History_YN,City,Commission_Code,Competition_Code,Contact_YN,Contact_Number,Corporate_ID_IATA,"
                            + ""
                            + ""
                            + "Corporate_Type,Country,Country3,Creation_Date,CRS_Profile_Id,Currency_Code,Date_Of_Birth,Day_Use_Res_LY,"
                            + ""
                            + ""
                            + ""
                            + "Day_Use_Res_TY,Day_Use_Rms_LY,Day_Use_Rms_TY,Department,Direct_Bill_Batch_Type,Download_Date_From_PMS_To_CRS,Download_Name_Of_PMS_Property,Email,Extra_Rev_LY,Extra_Rev_TY,F_And_B_Rev_LY,F_And_B_Rev_TY,"
                            + ""
                            + ""
                            + " Fax_Number, First_Name,Full_Name,Gender,Group_Of_Last_Reservation,History_YN,Identification_Issue_Date,"
                            + ""
                            + ""
                            + ""
                            + "Identification_Issued_Country,Identification_Issued_Place,Identification_Number, Identification_Type,Immigration_Status,Inactive_Date,Industry_Code,Influence,"
                            + ""
                            + ""
                            + ""
                            + "Insert_User,Job_Title_Code,Keyword,Language,Language4,Last_Rate,Last_Room,Last_Update_Date,Last_update_User,"
                            + ""
                            + ""
                            + ""
                            + "Last_Visit,Arrival_Date,Conf_Number,Credit_Card_Expiration_Date,Credit_Card_Name,Credit_Card_Number,Credit_Card_Type,Last_Name,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,GETDATE(),4,'The Leela Chennai'  )";


                } else if (subject.contains("profile3") || subject.contains("Profile3") || cont.contains("profile3_Arrival")) {
                    CnProf3 = "true";//87
                    query = "insert into Mumbai_Test_Schema.dbo." + targettablename + "(Mailing_List_YN,Market_Code,Member_Name,Membership_Exp_Date,Membership_Level,Membership_Number,Membership_Type ,"
                            + ""
                            + ""
                            + ""
                            + ""
                            + ""
                            + "Middle_Name,Multiple_Address_YN, Multiple_Notes_YN,Multiple_Phones_YN,Name,Name_Id,Name_Keyword,Nationality,Nationality2,"
                            + "Negotiated_Rate,Next_Stay_Date,No_Show_Res_LY,No_Show_Res_TY,No_Show_Rms_LY,No_Show_Rms_TY,Non_Rev_LY,Non_Rev_TY,Owner,Owner_Full_Name,"
                            + ""
                            + ""
                            + "Passport,Phone_Number,Phone_Type,Postal_Code,Postal_Code_Extension,Potential_Revenue,Potential_Room_Nights,Priority,"
                            + ""
                            + ""
                            + ""
                            + "Interests,Profession_Of_Profile,Profile_Type,Reference_Currency,Res_Arr_LY,Res_Arr_TY,Res_Nts_LY,Res_Nts_TY,Restricted_YN,Rm_Nts_LY,Rm_Nts_TY,Rm_Rev_LY,"
                            + ""
                            + ""
                            + " Rm_Rev_TY, Rooms_Potential,Salutation,Scope,Scope_City,Source_Code,Source_Of_Last_Reservation_Of_Profile,"
                            + ""
                            + ""
                            + ""
                            + "State,State3,Tax_Id_Number1, Tax_Id_Number2,Tax_Percent1,Tax_Percent2,Tax_Percent3,Tax_Percent4,"
                            + ""
                            + ""
                            + ""
                            + "Tax_Percent5,Tax_Type,Territory,Title,Title_Description,Ttl_Arr_Rms,Ttl_Can_Rms,Ttl_Day_Use_Res,Ttl_Day_Use_Rms,"
                            + ""
                            + ""
                            + ""
                            + "Ttl_Extra_Rev,Ttl_F_And_B_Rev,Ttl_No_Show_Res,Ttl_No_Show_Rms,Ttl_Non_Rev,VIP_Code,VIP_Status,Ttl_Res_Arr,"
                            + ""
                            + ""
                            + "Ttl_Res_Nts,Ttl_Rev,Ttl_Rev_LY,Ttl_Rm_Nts,Ttl_Rm_Rev,Trace_Code,Commission_Bank,Conf_Number,Arrival_Date,rec_insert_date,PROPERTY_ID,PROPERTY_NAME) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,    ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,GETDATE(),4,'The Leela Chennai' )";

                }

            } else if (from.contains("mum.gibyroom@theleela.com") || from.contains("udp.gibyroom@theleela.com")
                    || from.contains("del.gibyroom@theleela.com") || from.contains("goa.gibyroom@theleela.com") || (from.contains("is.bangalore@theleela.com") && subject.contains("room"))
                    || from.contains("kov.gibyroom@theleela.com") || from.contains("ggn.gibyroom@theleela.com") || from.contains("chn.gibyroom@theleela.com") || from.contains("gaurav.sharma@theleela.com")) {
                String table = "";
                String PROPERTY_ID = "";
                String PROPERTY_NAME = "";


                if (from.contains("mum.gibyroom@theleela.com")) {
                    MumRooms = "true";
                    PROPERTY_ID = "2";
                    PROPERTY_NAME = "The Leela Mumbai";
                    table = "Room_Data_Mumbai";
                } else if (from.contains("udp.gibyroom@theleela.com")) {
                    UdRooms = "true";
                    PROPERTY_ID = "7";
                    PROPERTY_NAME = "The Leela Udaipur";
                    table = "Room_Data_Udaipur";
                } else if (from.contains("del.gibyroom@theleela.com") || from.contains("gaurav.sharma@theleela.com")) {
                    DelRooms = "true";
                    PROPERTY_ID = "6";
                    PROPERTY_NAME = "The Leela New Delhi";
                    table = "Room_Data_New_Delhi";
                } else if (from.contains("goa.gibyroom@theleela.com")) {
                    GoaRooms = "true";
                    PROPERTY_ID = "5";
                    PROPERTY_NAME = "The Leela Goa";
                    table = "Room_Data_Goa";
                } else if (from.contains("is.bangalore@theleela.com")) {
                    BgRooms = "true";
                    PROPERTY_ID = "3";
                    PROPERTY_NAME = "The Leela Bangalore";
                    table = "Room_Data_Banglore";
                } else if (from.contains("kov.gibyroom@theleela.com")) {
                    KoRooms = "true";
                    PROPERTY_ID = "8";
                    PROPERTY_NAME = "The Leela Kovalam";
                    table = "Room_Data_Kovalam";
                } else if (from.contains("ggn.gibyroom@theleela.com")) {
                    GnRooms = "true";
                    PROPERTY_ID = "1";
                    PROPERTY_NAME = "The Leela Gurgaon";
                    table = "Room_Data_Gurgaon";
                } else if (from.contains("chn.gibyroom@theleela.com")) {
                    GnRooms = "true";
                    PROPERTY_ID = "4";
                    PROPERTY_NAME = "The Leela Chennai";
                    table = "Room_Data_Chennai";
                }
                //87
                query = "insert into Mumbai_Test_Schema.dbo." + table + "(SHARE_NAMES,ACCOMPANYING_NAMES          "
                        + ",CURRENCY_CODE               "
                        + ",SHARE_AMOUNT                "
                        + ",ROWNUM                      "
                        + ",RESORT                      "
                        + ",IS_SHARED_YN                "
                        + ",ACCOMPANYING_YN             "
                        + ",STAY_ROOMS                  "
                        + ",PREFERENCES                 "
                        + ",ROOM                        "
                        + ",VIP                         "
                        + ",COMPANY_NAME                "
                        + ",ARRIVAL                     "
                        + ",DEPARTURE                   "
                        + ",ROOM_CATEGORY_LABEL         "
                        + ",NO_OF_ROOMS                 "
                        + ",PAYMENT_METHOD              "
                        + ",BALANCE                     "
                        + ",RESV_NAME_ID                "
                        + ",CHILDREN                    "
                        + ",ADULTS                      "
                        + ",GUEST_TITLE                 "
                        + ",RATE_CODE                   "
                        + ",SPECIAL_REQUESTS            "
                        + ",BLOCK_CODE                  "
                        + ",FULL_NAME                   "
                        + ",GUEST_NAME                  "
                        + ",DEPARTURE_TIME              "
                        + ",EXTERNAL_REFERENCE          "
                        + ",GUEST_NAME_ID               "
                        + ",ROOM_FEATURES               "
                        + ",LIST_G_COMMENT_RESV_NAME_ID "
                        + ",LIST_G_COMMENT_NAME_ID      "
                        + ",LIST_G_EX_RESV_NAME_ID      "
                        + ",AUTH_AMT                    "
                        + ",SP_REQUEST                  "
                        + ",PREFERENCE                  "
                        + ",PROF_COUNT                  "
                        + ",RES_COUNT                   "
                        + ",CS_EXTENSION_COUNT          "
                        + ",rec_insert_date"
                        + ",PROPERTY_ID"
                        + ",PROPERTY_NAME,room_date) values( ?,?,?,?,?,?,?,?,?,?,   ?,?,?,?,?,?,?,?,?,?,     ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?,GETDATE()," + PROPERTY_ID + ",'" + PROPERTY_NAME + "',GETDATE()-1 )";
            }
            fileInputStream = new FileInputStream(DownloadLoc);
            XMLStreamReader reader =
                    factory.createXMLStreamReader(fileInputStream);
//    XMLStreamReader reader =
//             factory.createXMLStreamReader(fileInputStream);
            final SourceConn sc = new SourceConn();
//                   Class.forName("net.sourceforge.jtds.jdbc.Driver");         //192.168.0.254   183.82.3.61
//                     con = DriverManager.getConnection("jdbc:jtds:sqlserver://183.82.3.61/Mumbai_Test_Schema", "sa", "ProGen2008");
//                   con=sc.getConnection("Mumbai_Test_Schema_183", "", "", "", "", "", "", "", "");
            con = ProgenConnection.getInstance().getConnection();
            if (query.equalsIgnoreCase("")) {
                return "No Table";
            }
            ps = con.prepareStatement(query);

            double totalrow = -1;
            int row500 = -1;
            int i = 0;
            String s = "";
            int totalparameter = query.split("\\?").length - 1;

            while (reader.hasNext()) {
//        int event = reader.next();
                if (reader.isStartElement()) {
//   
                    switch (reader.getLocalName()) {
                        case "C1": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C2": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C3": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C4": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C5": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C6": {
//               
                            i++;
//               
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C7": {
                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C8": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C9": {
                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                i--;
                            } else if (DelCC.equalsIgnoreCase("true")) {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C10": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C11": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C12": {
                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C13": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C14": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C15": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C16": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C17": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C18": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C19": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C20": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C21": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C22": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C23": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C24": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C25": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C26": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C27": {

                            i++;
                            s = reader.getElementText(); //conf nbr all resev
                            if (DelFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (DelRes.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (BgFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (UdFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgRes.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C28": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C29": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C30": {

                            i++;
                            s = reader.getElementText();
                            if (DelFin.equalsIgnoreCase("true")) {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
                                ps.setString(i, s);
                            } else if (GnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                ps.setString(i, s);//confnbr
                            } else if (MumRes.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C31": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C32": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C33": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GnFin.equalsIgnoreCase("true")) {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
                                ps.setString(i, s);
                            } else if (UdFin.equalsIgnoreCase("true")) {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C34": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C35": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C36": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C37": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C38": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C39": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C40": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C41": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C42": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C43": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C44": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C45": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C46": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C47": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C48": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C49": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C50": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C51": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C52": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C53": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C54": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C55": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C56": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C57": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C58": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C59": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C60": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C61": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C62": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C63": {

                            i++;
                            s = reader.getElementText();
                            if (DelFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgFin.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C64": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C65": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C66": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C67": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C68": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C69": {
                            i++;
//               try{
                            s = reader.getElementText();
//                   s=reader.getText();
//                }
//               catch(Exception e)
//               {
//                   
//                   
//               }
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C70": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C71": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C72": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C73": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C74": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C75": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C76": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C77": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C78": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C79": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C80": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C81": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C82": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C83": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C84": {

                            i++;
                            s = reader.getElementText();
                            if (DelFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C85": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C86": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C87": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C88": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C89": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C90": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C91": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C92": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C93": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C94": {
                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C95": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C96": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C97": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C98": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C99": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C100": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C101": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C102": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C103": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C104": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C105": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C106": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C107": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C108": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C109": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C110": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C111": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C112": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C113": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C114": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C115": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C116": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C117": {

                            i++;
                            s = reader.getElementText();
                            if (DelFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C118": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C119": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C120": {
                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (UdFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C121": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C122": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C123": {
                            i++;
                            s = reader.getElementText();
                            if (DelFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C124": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C125": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C126": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C127": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C128": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C129": {

                            i++;
                            s = reader.getElementText();
                            if (GnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C130": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C131": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C132": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C133": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C134": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C135": {

                            i++;
                            s = reader.getElementText();
                            if (GnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C136": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C137": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C138": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (DelFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C139": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C140": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C141": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C142": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C143": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C144": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C145": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C146": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C147": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C148": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C149": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C150": {

                            i++;
                            s = reader.getElementText();
                            if (GnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C151": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C152": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C153": {

                            i++;
                            s = reader.getElementText();

                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C154": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C155": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C156": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C157": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C158": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C159": {

                            i++;
                            s = reader.getElementText();
                            if (DelFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C160": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C161": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C162": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C163": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C164": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C165": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C166": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C167": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C168": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C169": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C170": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C171": {

                            i++;
                            s = reader.getElementText();
                            if (GnFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C172": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C173": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C174": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C175": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C176": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C177": {

                            i++;
                            s = reader.getElementText();
//                if(DelFin.equalsIgnoreCase("true"))
//                {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
//                }
//                 else
                            if (BgFin.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (GoaFin.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (CnFin.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C178": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C179": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C180": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C181": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C182": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C183": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C184": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C185": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C186": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C187": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C188": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C189": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (GnFin.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C190": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C191": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C192": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C193": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C194": {
                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C195": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C196": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C197": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C198": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C199": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }

                        case "C200": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C201": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C202": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C203": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C204": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C205": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C206": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C207": {
                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C208": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C209": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C210": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C211": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C212": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C213": {

                            i++;
                            s = reader.getElementText();
//                if(MumProf3.equalsIgnoreCase("true"))
//                {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
//                }
//                 else
                            if (BgFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C214": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C215": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C216": {
                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C217": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C218": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C219": {
                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C220": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C221": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C222": {
                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C223": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C224": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C225": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C226": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C227": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C228": {

                            i++;//conf number all profile2
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (BgProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (CnProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (GoaProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (GnProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (KoProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (UdProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C229": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C230": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C231": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (DelProf2.equalsIgnoreCase("true")) {
                                ps.setString(i, s);   //conf number
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C232": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C233": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C234": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C235": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C236": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C237": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C238": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C239": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C240": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C241": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C242": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C243": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C244": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C245": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C246": {

                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C247": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C248": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C249": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C250": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C251": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C252": {

                            i++;
                            s = reader.getElementText();
//                if(MumProf3.equalsIgnoreCase("true"))
//                {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
//                }
//                else
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C253": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C254": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C255": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (MumFin.equalsIgnoreCase("true")) {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C256": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C257": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C258": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C259": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C260": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C261": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C262": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C263": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C264": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C265": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C266": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C267": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C268": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C269": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C270": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C271": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C272": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C273": {

                            i++;
                            s = reader.getElementText(); //conf number profile3 all
                            if (DelProf3.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C274": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C275": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C276": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C277": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C278": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C279": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C280": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C281": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C282": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C283": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C284": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C285": {

                            i++;
                            s = reader.getElementText();
                            if (MumProf3.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else if (KoFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C286": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C287": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C288": {

                            i++;
                            s = reader.getElementText();
                            if (KoFin.equalsIgnoreCase("true")) {
//                    if(s.equalsIgnoreCase(""))
//                    ps.setFloat(i, Float.parseFloat("0"));
//                    else
//                     ps.setFloat(i, Float.parseFloat(s));
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C289": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C290": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C291": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C292": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C293": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C294": {
                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C295": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C296": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C297": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C298": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C299": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }

                        case "C300": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C301": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C302": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C303": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C304": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C305": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C306": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C307": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C308": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C309": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C310": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C311": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C312": {
                            i++;
                            s = reader.getElementText();
                            if (DelRes.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else if (KoFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }
                            break;
                        }
                        case "C313": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C314": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C315": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C316": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C317": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C318": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C319": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C320": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C321": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C322": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C323": {
                            i++;
                            ps.setString(i, reader.getElementText());
                            break;
                        }
                        case "C324": {
                            i++;
                            s = reader.getElementText();
                            ps.setString(i, s);
                            break;
                        }
                        case "C325": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C326": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C327": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C328": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C329": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C330": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C331": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C332": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C333": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C334": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C335": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C336": {

                            i++;
                            s = reader.getElementText();
                            if (KoFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C337": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C338": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C339": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C340": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C341": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C342": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C343": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C344": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C345": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C346": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C347": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C348": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C349": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C350": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C351": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C352": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C353": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C354": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C355": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C356": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C357": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C358": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C359": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C360": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C361": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C362": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C363": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C364": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C365": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C366": {

                            i++;
                            s = reader.getElementText();
                            if (KoFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C367": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C368": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C369": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C370": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C371": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C372": {

                            i++;
                            s = reader.getElementText();
                            if (MumFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            }
                            if (KoFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C373": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C374": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C375": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C376": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C377": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C378": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C379": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C380": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C381": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C382": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C383": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C384": {

                            i++;
                            s = reader.getElementText();
                            if (KoFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C385": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C386": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C387": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C388": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C389": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C390": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C391": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C392": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C393": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C394": {
                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C395": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C396": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C397": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C398": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C399": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C400": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C401": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C402": {

                            i++;
                            s = reader.getElementText();
                            if (KoFin.equalsIgnoreCase("true")) {
                                if (s.equalsIgnoreCase("")) {
                                    ps.setFloat(i, Float.parseFloat("0"));
                                } else {
                                    ps.setFloat(i, Float.parseFloat(s));
                                }
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C403": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C404": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C405": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C406": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C407": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C408": {

                            i++;
                            s = reader.getElementText();
                            ps.setString(i, s);

                            break;
                        }
                        case "C409": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C410": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C411": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C412": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C413": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C414": {

                            i++;
                            s = reader.getElementText();
                            if (KoFin.equalsIgnoreCase("true")) {
                                ps.setString(i, s);
                            } else {
                                ps.setString(i, s);
                            }

                            break;
                        }
                        case "C415": {
                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C416": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C417": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C418": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C419": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C420": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C421": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C422": {
                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C423": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C424": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C425": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C426": {

                            i++;
                            s = reader.getElementText();
                            ps.setString(i, s);

                            break;
                        }
                        case "C427": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C428": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C429": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C430": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C431": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C432": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C433": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C434": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C435": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C436": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C437": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C438": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C439": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C440": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C441": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C442": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C443": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C444": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C445": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C446": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C447": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C448": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C449": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C450": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C451": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C452": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C453": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C454": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C455": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "C456": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "SHARE_NAMES": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "ACCOMPANYING_NAMES": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "CURRENCY_CODE": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "SHARE_AMOUNT": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setFloat(i, Float.parseFloat("0"));
                            } else {
                                ps.setFloat(i, Float.parseFloat(s));
                            }


                            break;
                        }
                        case "ROWNUM": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "RESORT": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "IS_SHARED_YN": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "ACCOMPANYING_YN": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "STAY_ROOMS": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "PREFERENCES": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "ROOM": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "VIP": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "COMPANY_NAME": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "ARRIVAL": {

                            i++;
                            s = reader.getElementText();
                            ps.setString(i, s);

//                    if(s.equalsIgnoreCase(""))
//                    ps.setDate(i, null);
//                    else{
//                            try {
//                                Date parse = dateFormat.parse(s);
//
//                                 ps.setDate(i, new java.sql.Date(parse.getTime()));
//                            } catch (ParseException ex) {
//                                logger.error("Exception: ",ex);
//                            }
//
//                }

                            break;
                        }
                        case "DEPARTURE": {

                            i++;
                            s = reader.getElementText();
                            ps.setString(i, s);

//                    if(s.equalsIgnoreCase(""))
//                    ps.setDate(i, null);
//                    else{
//                            try {
//                                Date parse = dateFormat.parse(s);
//
//                                 ps.setDate(i, new java.sql.Date(parse.getTime()));
//                            } catch (ParseException ex) {
//                                logger.error("Exception: ",ex);
//                            }
//
//                }

                            break;
                        }
                        case "ROOM_CATEGORY_LABEL": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "NO_OF_ROOMS": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }



                            break;
                        }
                        case "PAYMENT_METHOD": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "BALANCE": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setFloat(i, Float.parseFloat("0"));
                            } else {
                                ps.setFloat(i, Float.parseFloat(s));
                            }

                            break;
                        }
                        case "RESV_NAME_ID": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "CHILDREN": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "ADULTS": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "GUEST_TITLE": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "RATE_CODE": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "SPECIAL_REQUESTS": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "BLOCK_CODE": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "FULL_NAME": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "GUEST_NAME": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "DEPARTURE_TIME": {

                            i++;
                            s = reader.getElementText();
                            ps.setString(i, s);

//                    if(s.equalsIgnoreCase(""))
//                    ps.setDate(i, null);
//                    else{
//                            try {
//                                Date parse = dateFormat.parse(s);
//
//                                 ps.setDate(i, new java.sql.Date(parse.getTime()));
//                            } catch (ParseException ex) {
//                                logger.error("Exception: ",ex);
//                            }
//
//                }

                            break;
                        }
                        case "EXTERNAL_REFERENCE": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "GUEST_NAME_ID": {
                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setInt(i, Integer.parseInt("0"));
                            } else {
                                ps.setInt(i, Integer.parseInt(s));
                            }

                            break;
                        }
                        case "ROOM_FEATURES": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "LIST_G_COMMENT_RESV_NAME_ID": {


                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "LIST_G_COMMENT_NAME_ID": {


                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "LIST_G_EX_RESV_NAME_ID": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "AUTH_AMT": {
                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setFloat(i, Float.parseFloat("0"));
                            } else {
                                ps.setFloat(i, Float.parseFloat(s));
                            }

                            break;
                        }
                        case "SP_REQUEST": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "PREFERENCE": {

                            i++;
                            ps.setString(i, reader.getElementText());

                            break;
                        }
                        case "PROF_COUNT": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setFloat(i, Float.parseFloat("0"));
                            } else {
                                ps.setFloat(i, Float.parseFloat(s));
                            }

                            break;
                        }
                        case "RES_COUNT": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setFloat(i, Float.parseFloat("0"));
                            } else {
                                ps.setFloat(i, Float.parseFloat(s));
                            }

                            break;
                        }
                        case "CS_EXTENSION_COUNT": {

                            i++;
                            s = reader.getElementText();

                            if (s.equalsIgnoreCase("")) {
                                ps.setFloat(i, Float.parseFloat("0"));
                            } else {
                                ps.setFloat(i, Float.parseFloat(s));
                            }

                            break;
                        }

                        case "G_C6": {

                            totalrow++;
                            row500++;
                            if (totalrow > 0) {
                                for (int j = i + 1; j <= totalparameter; j++) {
                                    ps.setString(j, null);

                                }
//                      
//                      
                                ps.addBatch();
//                     
                                if (row500 == 10000) {
                                    ps.executeBatch();

                                    ps.close();
                                    ps = con.prepareStatement(query);

                                    row500 = 0;
                                }
                            }
                            i = 0;
                            break;
                        }
                        case "G_C9": {
                            totalrow++;
                            row500++;
                            if (totalrow > 0) {
                                for (int j = i + 1; j <= totalparameter; j++) {

                                    ps.setString(j, null);
                                }
                                ps.addBatch();
                                if (row500 == 10000) {
                                    ps.executeBatch();

                                    ps.close();
                                    ps = con.prepareStatement(query);

                                    row500 = 0;
                                }
                            }
                            i = 0;
                            break;
                        }
                        case "G_C12": {

                            totalrow++;
                            row500++;
                            if (totalrow > 0) {
                                for (int j = i + 1; j <= totalparameter; j++) {

                                    ps.setString(j, null);
                                }
                                ps.addBatch();
                                if (row500 == 10000) {

                                    ps.executeBatch();

                                    ps.close();
                                    ps = con.prepareStatement(query);

                                    row500 = 0;
                                }
                            }
                            i = 0;
                            break;
                        }
                        case "G_C237": {

                            totalrow++;
                            row500++;
                            if (totalrow > 0) {
                                for (int j = i + 1; j <= totalparameter; j++) {

                                    ps.setString(j, null);
                                }
                                ps.addBatch();
                                if (row500 == 10000) {

                                    ps.executeBatch();

                                    ps.close();
                                    ps = con.prepareStatement(query);

                                    row500 = 0;
                                }
                            }
                            i = 0;
                            break;
                        }
                        case "G_C270": {

                            totalrow++;
                            row500++;
                            if (totalrow > 0) {
                                for (int j = i + 1; j <= totalparameter; j++) {

                                    ps.setString(j, null);
                                }
                                ps.addBatch();
                                if (row500 == 10000) {

                                    ps.executeBatch();

                                    ps.close();
                                    ps = con.prepareStatement(query);

                                    row500 = 0;
                                }
                            }
                            i = 0;
                            break;
                        }
                        case "G_ROOM": {

//                i=0;
                            totalrow++;
                            row500++;;
                            if (totalrow > 0) {
                                for (int j = i + 1; j <= totalparameter; j++) {

                                    ps.setString(j, null);
                                }
                                ps.addBatch();
                                if (row500 == 10000) {

                                    ps.executeBatch();

                                    ps.close();
                                    ps = con.prepareStatement(query);

                                    row500 = 0;
                                }
                            }
                            i = 0;
                            break;
                        }
//Added By Ram
                        case "G_C48": {

                            totalrow++;
                            row500++;
                            if (totalrow > 0) {
                                for (int j = i + 1; j <= totalparameter; j++) {
                                    ps.setString(j, null);

                                }
//                      
//                      
                                ps.addBatch();
//                     
                                if (row500 == 10000) {
                                    ps.executeBatch();

                                    ps.close();
                                    ps = con.prepareStatement(query);

                                    row500 = 0;
                                }
                            }
                            i = 0;
                            break;
                        }
                        ////Ended By Ram
                    }

                }

                if (reader.hasNext()) {
//       try{
                    reader.next();
//    }
//      catch(Exception e){
//      }
                }
//else
//{
//     
//     totalrow++;
//      ps.addBatch();
//       ps.executeBatch();
//
//}



            }

            totalrow++;
            ps.addBatch();
            ps.executeBatch();

            ps.close();
            if (con != null) {
                con.close();
            }
            return "success::" + totalrow;
//        mailer.sendXmlLoadingMail(from,subject,emailId,sentDate);

        } //catch (XMLStreamException e) {
        ////              mailer.sendXmlExceptionMail(from,subject,emailId,sentDate);
        //            logger.error("Exception: ",e);
        //        }
        //    catch (Exception e) {
        ////         mailer.sendXmlExceptionMail(from,subject,emailId,sentDate);
        //        logger.error("Exception: ",e);
        //        }
        finally {
//    fileInputStream.close();
//            reader.close();
            con.close();
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /*
                     * ignored
                     */

                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { /*
                     * ignored
                     */

                }
            }
            if (con != null) {
                con.close();
            }

        }
//return "success";
    }

    public int InsertIntoTrackerMaster(String TAB_NAME, String TYPE, String COMMENTS, String STATUS) {
        PbDb db = new PbDb();
        String query = "";
        int execUpdateSQL = 0;
        switch (ProgenConnection.getInstance().getDatabaseType()) {
            case ProgenConnection.SQL_SERVER:
                query = "insert into PRG_LOAD_TRACKER(TAB_NAME,LAST_UPDATE_DATE,TYPE,COMMENTS,STATUS) values('&',getDate(),'&','&','&')";
                break;
            case ProgenConnection.MYSQL:
                query = "insert into PRG_LOAD_TRACKER(TAB_NAME,LAST_UPDATE_DATE,TYPE,COMMENTS,STATUS) values('&',curdate(),'&','&','&')";
                break;
            default:
                query = "insert into PRG_LOAD_TRACKER(TAB_NAME,LAST_UPDATE_DATE,TYPE,COMMENTS,STATUS) values('&',sysdate,'&','&','&')";
                break;
        }
        Object[] objArr = new Object[4];
        objArr[0] = TAB_NAME;
        objArr[1] = TYPE;
        objArr[2] = "Total Rows=" + COMMENTS;
        objArr[3] = STATUS;
        String finalQuery = db.buildQuery(query, objArr);
        try {
            execUpdateSQL = db.execUpdateSQL(finalQuery);
            return execUpdateSQL;

        } catch (Exception ex) {
        }

        return execUpdateSQL;

    }

    public int UpdateSchedulerlogs(int SCHEDULER_ID, String SCHEDULER_TYPE, String STATUS) {
        PbDb db = new PbDb();
        String query = "";
        int execUpdateSQL = 0;
        switch (ProgenConnection.getInstance().getDatabaseType()) {
            case ProgenConnection.SQL_SERVER:
                query = "insert into PRG_SCHEDULER_LOGS(SCHEDULER_ID,SCHEDULER_TYPE,EXECUTION_DATE,STATUS) values(&,'&',getDate(),'&')";
                break;
            case ProgenConnection.MYSQL:
                query = "insert into PRG_SCHEDULER_LOGS(SCHEDULER_ID,SCHEDULER_TYPE,EXECUTION_DATE,STATUS) values(&,'&',curdate(),'&')";
                break;
            default:
                query = "insert into PRG_SCHEDULER_LOGS(SCHEDULER_ID,SCHEDULER_TYPE,EXECUTION_DATE,STATUS) values(&,'&',sysdate,'&')";
                break;
        }
        Object[] objArr = new Object[3];
        objArr[0] = SCHEDULER_ID;
        objArr[1] = SCHEDULER_TYPE;
        objArr[2] = STATUS;
        String finalQuery = db.buildQuery(query, objArr);
        try {
            execUpdateSQL = db.execUpdateSQL(finalQuery);
            return execUpdateSQL;

        } catch (Exception ex) {
        }
        return execUpdateSQL;

    }

    public int UpdateLoadTracker(String Location_name) {
        PbDb db = new PbDb();
        String query = "";
        int execUpdateSQL = 0;
        query = "UPDATE prg_load_tracker_master SET LAST_UPDATE_DATE=getDate() WHERE Location_name='&'";

        Object[] objArr = new Object[1];
        objArr[0] = Location_name;
        String finalQuery = db.buildQuery(query, objArr);
        try {
            execUpdateSQL = db.execUpdateSQL(finalQuery);
            return execUpdateSQL;

        } catch (Exception ex) {
        }

        return execUpdateSQL;

    }

    public String TruncateLoadTracker(String Location_name) {
// public int TruncateLoadTracker()
//    {
        PbDb db = new PbDb();
        final SourceConn sc = new SourceConn();
        Connection con = null;
        PbReturnObject execSelectSQL = null;
        String query = "";
        if (Location_name.equalsIgnoreCase("ALL")) {
            query = "SELECT TAB_NAME from prg_load_tracker_master";
        } else {
            query = "SELECT TAB_NAME from prg_load_tracker_master WHERE Location_name='&'";
            Object[] objArr = new Object[1];
            objArr[0] = Location_name;
            query = db.buildQuery(query, objArr);
        }

//
        try {
            execSelectSQL = db.execSelectSQL(query);
            logrw.fileWriter("---------------------Truncation Started-----------------------\n");
//            PbReturnObject execSelectSQL = db.execSelectSQL(finalQuery);

            if (execSelectSQL != null) {
                for (int i = 0; i < execSelectSQL.getRowCount(); i++) {
//                        con=sc.getConnection("Mumbai_Test_Schema_183", "", "", "", "", "", "", "", "");
//                    int execUpdateSQL = db.execUpdateSQL("TRUNCATE TABLE " + execSelectSQL.getFieldValueString(i, "TAB_NAME") + ";", con);
                    int execUpdateSQL = db.execUpdateSQL("TRUNCATE TABLE Mumbai_Test_Schema.dbo." + execSelectSQL.getFieldValueString(i, "TAB_NAME") + ";");
                    logrw.fileWriter("(" + (i + 1) + ") TRUNCATE TABLE " + execSelectSQL.getFieldValueString(i, "TAB_NAME") + ";\n");


                }
                return execSelectSQL.getFieldValueString(0, "TAB_NAME");
            }

        } catch (Exception ex) {
            return "0";

        }
        return "0";
    }

    public String GetLocationName(String from, String subject) {
        String cont = "";


        if (from.contains("jey.balaji@theleela.com")) {
            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Bangalore_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Bangalore_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Bangalore_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Bangalore_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "Bangalore_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Bangalore_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "Bangalore_Guest_Arrival";
            } else if (subject.contains("Reservation")) {
                cont = "Bangalore_Reservation";
            } else if (subject.contains("profile2")) {
                cont = "Bangalore_profile2";
            } else if (subject.contains("profile3")) {
                cont = "Bangalore_profile3";
            }

        } else if (from.contains("ocis.reports@theleela.com") || from.contains("mohit.jain@progenbusiness.com") || from.contains("ranjana.nikam@theleela.com")) {
//                       if(subject.contains("Finance"))
//                        cont="Mumbai_Finance";
//                      else
            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Mumbai_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Mumbai_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Mumbai_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Mumbai_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "Mumbai_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Mumbai_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "Mumbai_Guest_Arrival";
            } else if (subject.contains("reservation")) {
                cont = "Mumbai_Reservation";
            } else if (subject.contains("profile2")) {
                cont = "Mumbai_profile2";
            } else if (subject.contains("Profile3")) {
                cont = "Mumbai_profile3";
            }
        } else if (from.contains("reservations.newdelhi@theleela.com")) {
//                       if(subject.contains("Finance"))
//                        cont="New_Delhi_Finance";
//                      else

            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "New_Delhi_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "New_Delhi_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "New_Delhi_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "New_Delhi_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "New_Delhi_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "New_Delhi_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "New_Delhi_Guest_Arrival";
            } else if (subject.contains("test4")) {
                cont = "New_Delhi_Reservation";
            } else if (subject.contains("Test_Profile_08_2 - test_profile_08_2")) {
                cont = "New_Delhi_profile2";
            } else if (subject.contains("Test_Profile_08_3 - test_profile_08_3")) {
                cont = "New_Delhi_profile3";
            } else if (subject.contains("Test_Profile_08_02_Rem_Col")) {
                cont = "New_Delhi_CreditCard";
            }
        } else if (from.contains("it.goa@theleela.com")) {
//                       if(subject.contains("finance"))
//                        cont="Goa_Finance";
//                      else

            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Goa_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Goa_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Goa_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Goa_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "Goa_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Goa_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "Goa_Guest_Arrival";
            } else if (subject.contains("reservation")) {
                cont = "Goa_Reservation";
            } else if (subject.contains("profil2")) {
                cont = "Goa_profile2";
            } else if (subject.contains("profil3")) {
                cont = "Goa_profile3";
            }
        } else if (from.contains("reservations.gurgaon-delhi@theleela.com")) {
//                       if(subject.contains("finance"))
//                        cont="Gurgaon_Finance";
//                      else

            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Gurgaon_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Gurgaon_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Gurgaon_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Gurgaon_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "Gurgaon_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Gurgaon_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "Gurgaon_Guest_Arrival";
            } else if (subject.contains("reservation")) {
                cont = "Gurgaon_Reservation";
            } else if (subject.contains("profile2")) {
                cont = "Gurgaon_profile2";
            } else if (subject.contains("profile3")) {
                cont = "Gurgaon_profile3";
            }
        } else if (from.contains("it.kovalam@theleela.com")) {
//                       if(subject.contains("finance"))
//                        cont="Kovalam_Finance";
//                      else

            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Kovalam_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Kovalam_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Kovalam_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Kovalam_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "Kovalam_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Kovalam_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "Kovalam_Guest_Arrival";
            } else if (subject.contains("reservations")) {
                cont = "Kovalam_Reservation";
            } else if (subject.contains("profile2")) {
                cont = "Kovalam_profile2";
            } else if (subject.contains("profile3")) {
                cont = "Kovalam_profile3";
            }
        } else if (from.contains("rakesh.mehta@theleela.com")) {
//                       if(subject.contains("finance"))
//                        cont="Udaipur_Finance";
//                      else

            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Udaipur_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Udaipur_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Udaipur_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Udaipur_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "Udaipur_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Udaipur_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "Udaipur_Guest_Arrival";
            } else if (subject.contains("reservation") || subject.contains("servation")) {
                cont = "Udaipur_Reservation";
            } else if (subject.contains("profile2") || subject.contains("Profile2") || subject.contains("rofile 2")) {
                cont = "Udaipur_profile2";
            } else if (subject.contains("profile3") || subject.contains("Profile3") || subject.contains("rofile 3")) {
                cont = "Udaipur_profile3";
            }
        } else if (from.contains("daniel.dsuza@theleela.com")) {
//                       if(subject.contains("finance"))
//                        cont="Chennai_Finance";
//                      else

            if ((subject.contains("tion")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Chennai_Reservation_Arrival";
            } else if ((subject.contains("Profile2") || subject.contains("profile2")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Chennai_profile2_Arrival";
            } else if ((subject.contains("Profile3") || subject.contains("profile3")) && (subject.contains("Arrival") || subject.contains("arrival"))) {
                cont = "Chennai_profile3_Arrival";
            } else if ((subject.contains("Actual") || subject.contains("actual")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Chennai_Actual_Creation";
            } else if (subject.contains("Actual") || subject.contains("actual")) {
                cont = "Chennai_Actual_Arrival";
            } else if ((subject.contains("Guest") || subject.contains("guest")) && (subject.contains("Crea") || subject.contains("crea"))) {
                cont = "Chennai_Guest_Creation";
            } else if (subject.contains("Guest") || subject.contains("guest")) {
                cont = "Chennai_Guest_Arrival";
            } else if (subject.contains("reservation")) {
                cont = "Chennai_Reservation";
            } else if (subject.contains("profile2")) {
                cont = "Chennai_profile2";
            } else if (subject.contains("profile3")) {
                cont = "Chennai_profile3";
            } else if (subject.contains("Finance")) {
                cont = "Chennai_Finance";
            }
        } else if (from.contains("mum.gibyroom@theleela.com")) {
            cont = "Room_Data_Mumbai";
        } else if (from.contains("udp.gibyroom@theleela.com")) {

            cont = "Room_Data_Udaipur";
        } else if (from.contains("del.gibyroom@theleela.com")) {

            cont = "Room_Data_New_Delhi";
        } else if (from.contains("goa.gibyroom@theleela.com")) {

            cont = "Room_Data_Goa";
        } else if (from.contains("is.bangalore@theleela.com")) {
            if (subject.contains("finance")) {
                cont = "Bangalore_Finance";
            } else if (subject.contains("room")) {
                cont = "Room_Data_Bangalore";
            }
        } else if (from.contains("kov.gibyroom@theleela.com")) {

            cont = "Room_Data_Kovalam";
        } else if (from.contains("ggn.gibyroom@theleela.com")) {

            cont = "Room_Data_Gurgaon";
        } else if (from.contains("chn.gibyroom@theleela.com")) {

            cont = "Room_Data_Chennai";
        } else if (from.contains("chn.financepg@theleela.com")) {

            cont = "Chennai_Finance";
        } else if (from.contains("udp.financepg@theleela.com")) {
            cont = "Udaipur_Finance";
        } else if (from.contains("mum.financepg@theleela.com")) {

            cont = "Mumbai_Finance";
        } else if (from.contains("kov.financepg@theleela.com")) {

            cont = "Kovalam_Finance";
        } else if (from.contains("del.financepg@theleela.com")) {

            cont = "New_Delhi_Finance";
        } else if (from.contains("goa.financepg@theleela.com")) {

            cont = "Goa_Finance";
        } else if (from.contains("ggn.financepg@theleela.com")) {

            cont = "Gurgaon_Finance";
        } else if (from.contains("bg.financepg@theleela.com")) {

            cont = "Bangalore_Finance";
        } else if (from.contains("gaurav.sharma@theleela.com")) {

            cont = "Room_Data_New_Delhi";
        }

        return cont;

    }
}
