package utils.db;

public class TimeChange {

    String INPUT_TIME;
    String TEMP_TIME;
    int OUTPUT_TIME;

    public String changeTime(String input_time) {
        INPUT_TIME = input_time;
        if (INPUT_TIME.indexOf("pm") > 0) {
            INPUT_TIME = INPUT_TIME.substring(0, (INPUT_TIME.indexOf("pm")) - 1);//Now we have time without pm
            TEMP_TIME = INPUT_TIME.substring(INPUT_TIME.indexOf(":"));//Now we have :value
            INPUT_TIME = INPUT_TIME.substring(0, INPUT_TIME.indexOf(":"));//Now we have time before :
            if (INPUT_TIME.equals("12") != true) {
                OUTPUT_TIME = Integer.parseInt(INPUT_TIME) + 12;
                INPUT_TIME = OUTPUT_TIME + TEMP_TIME;
            }

        } else {

            if (INPUT_TIME.indexOf("12") >= 0) {
                INPUT_TIME = INPUT_TIME.replaceAll("12", "00");
            }
            INPUT_TIME = INPUT_TIME.substring(0, (INPUT_TIME.indexOf("am")) - 1);
        }
        if (INPUT_TIME.equals("12")) {
            INPUT_TIME = INPUT_TIME + TEMP_TIME;
        }



        return INPUT_TIME;
    }

    public static void main(String[] args) {
        TimeChange tc = new TimeChange();
        ////////////////////////////////////////////////////////////////.println("New Time is "+tc.changeTime("12:12 am"));//Doesn't work for 12:12 AM
        String value = "45";
        String[] values = value.split(",");
        ////////////////////////////////////////////////////////////////.println("values "+values[0]);
    }
}
