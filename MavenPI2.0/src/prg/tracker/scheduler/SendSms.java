/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.tracker.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.log4j.Logger;

/**
 *
 * @author Saurabh
 */
public class SendSms {

    private String userName;
    private String password;
    private String mobileNo;
    private String messageText;
    public static Logger logger = Logger.getLogger(SendSms.class);

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String getEncodeMessageText() throws UnsupportedEncodingException {
        return (URLEncoder.encode(getMessageText(), "UTF-8"));
    }

    private String getEncodeMessageText(String myMessage) throws UnsupportedEncodingException {
        return (URLEncoder.encode(myMessage, "UTF-8"));
    }

    private String getPushURL() throws UnsupportedEncodingException {
        return ("http://push.vasitsolutions.com/bulkpush.php?user=" + getUserName() + "&pass=" + getPassword() + "&message=" + getEncodeMessageText() + "&msisdn=" + getMobileNo() + "&sender=VAS&type=text");
    }

    private String getPushURL(String myMobileNo, String myMessage) throws UnsupportedEncodingException {
        String myEncodeMessage = getEncodeMessageText(myMessage);
        return ("http://push.vasitsolutions.com/bulkpush.php?user=" + getUserName() + "&pass=" + getPassword() + "&message=" + myEncodeMessage + "&msisdn=" + myMobileNo + "&sender=VAS&type=text");
    }

    public void sendSms() throws UnsupportedEncodingException {


        try {
            URL yahoo = new URL(getPushURL());

            HttpURLConnection connection =
                    (HttpURLConnection) yahoo.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Check the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Read the server's response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                ////////////////////////////////////////////////.println.println("Submission result: " + in.readLine());
                in.close();

            } else {
                // There was an error
                System.err.println("Server responded with code " + responseCode);
            }



        } catch (Exception e) {
            System.err.println("Unable to make HTTP request due to an exception:");
            logger.error("Exception: ", e);

        }

    }

    public void smssending(String mailMsg, String phnumber) throws MalformedURLException, IOException {
        try {
            URL yahoo = new URL(getPushURL(phnumber, mailMsg));

            HttpURLConnection connection =
                    (HttpURLConnection) yahoo.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Check the response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Read the server's response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                ////////////////////////////////////////////////.println.println("Submission result: " + in.readLine());
                in.close();

            } else {
                // There was an error
                System.err.println("Server responded with code " + responseCode);
            }



        } catch (Exception e) {
            System.err.println("Unable to make HTTP request due to an exception:");
            logger.error("Exception: ", e);

        }

    }

    public void smssending(String mailMsg[], String phnumber) throws MalformedURLException, IOException {
        try {
            for (int i = 0; i < mailMsg.length; i++) {
                URL yahoo = new URL(getPushURL(phnumber, mailMsg[i]));

                HttpURLConnection connection =
                        (HttpURLConnection) yahoo.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // Check the response
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    // Read the server's response
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    ////////////////////////////////////////////////.println.println("Submission result: " + in.readLine());
                    in.close();

                } else {
                    // There was an error
                    System.err.println("Server responded with code " + responseCode);
                }

            }

        } catch (Exception e) {
            System.err.println("Unable to make HTTP request due to an exception:");
            logger.error("Exception: ", e);

        }

    }

    public void smssending(String mailMsg, String phnumber[]) throws MalformedURLException, IOException {

        try {
            for (int i = 0; i < phnumber.length; i++) {
                URL yahoo = new URL(getPushURL(phnumber[i], mailMsg));

                HttpURLConnection connection =
                        (HttpURLConnection) yahoo.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // Check the response
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    // Read the server's response
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    ////////////////////////////////////////////////.println.println("Submission result: " + in.readLine());
                    in.close();

                } else {
                    // There was an error
                    System.err.println("Server responded with code " + responseCode);
                }

            }

        } catch (Exception e) {
            System.err.println("Unable to make HTTP request due to an exception:");
            logger.error("Exception: ", e);

        }

    }

    public void smssending(String mailMsg[], String phnumber[]) throws MalformedURLException, IOException {
        String a = "http://localhost:81/ShadesAnalyzer/smstest.jsp";
        //"http://push.vasitsolutions.com/bulkpush.php?user=progen&pass=progen123&message=Test Message&msisdn=9885432548&sender=VAS&type=text";

        try {
            for (int i = 0; i < phnumber.length; i++) {
                for (int j = 0; j < mailMsg.length; j++) {
                    URL yahoo = new URL(getPushURL(phnumber[i], mailMsg[i]));

                    HttpURLConnection connection =
                            (HttpURLConnection) yahoo.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    // Check the response
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        // Read the server's response
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));

                        ////////////////////////////////////////////////.println.println("Submission result: " + in.readLine());
                        in.close();

                    } else {
                        // There was an error
                        System.err.println("Server responded with code " + responseCode);
                    }

                }
            }

        } catch (Exception e) {
            System.err.println("Unable to make HTTP request due to an exception:");
            logger.error("Exception: ", e);

        }

    }
}
