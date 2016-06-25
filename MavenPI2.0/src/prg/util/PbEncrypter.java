/*
 * PgEncrypter.java
 *
 * Created on April 18, 2009, 1:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

public class PbEncrypter {

    public static Logger logger = Logger.getLogger(PbEncrypter.class);
    private static final boolean VERBOSE = false;
    private int cipherKey;
    private int randomArray[] = {4, 8, 5, 9, 0, 3, 6, 1, 7, 2};
    private KeyClass keyClass;

    private class KeyClass {

        public String getKey(String key) {
            if (word.contains(key)) {
                int index = word.indexOf(key);
                return (String) cpkey.elementAt(index);
            } else {
                log("No Key Present......");
                return null;
            }
        }

        public String getWord(String cha) {
            if (cpkey.contains(cha)) {
                int index = cpkey.indexOf(cha);
                return (String) word.elementAt(index);
            } else {
                log("No Such Character Present......");
                return null;
            }
        }

        private final void log(String logMsg) {
        }

        private boolean setCipherKey() {
            if (!isKeySet) {
                log("Cipher Key is Not Set    ");
                return false;
            }
            int noOfKeys = set.length;
            word = new Vector();
            cpkey = new Vector();
            for (int i = 0; i < noOfKeys; i++) {
                word.addElement(set[i][0]);
                cpkey.addElement(set[i][1]);
            }

            return true;
        }

        public void setKeyNo(int key) {
            isKeySet = true;
            set = null;
            switch (key) {
                case 0: // '\0'
                    set = set0;
                    break;

                case 1: // '\001'
                    set = set1;
                    break;

                case 2: // '\002'
                    set = set2;
                    break;

                case 3: // '\003'
                    set = set3;
                    break;

                case 4: // '\004'
                    set = set4;
                    break;

                case 5: // '\005'
                    set = set5;
                    break;

                case 6: // '\006'
                    set = set6;
                    break;

                case 7: // '\007'
                    set = set7;
                    break;

                case 8: // '\b'
                    set = set8;
                    break;

                case 9: // '\t'
                    set = set9;
                    break;

                case 10: // '\n'
                    set = set10;
                    break;
            }
            setCipherKey();
        }
        private String set[][];
        private final boolean VERBOSE = false;
        private boolean isKeySet;
        private Vector word;
        private Vector cpkey;
        private String set0[][] = {
            {"a", "t%"}, {"b", "e/"}, {"c", "h~"}, {"d", "r$"}, {"e", "B@"}, {"f", "j/"},
            {"g", "n$"}, {"h", "T#"}, {"i", "N("}, {"j", "5~"}, {"k", "f!"}, {"l", "Y^"},
            {"m", "X="}, {"n", "M!"}, {"o", "6*"}, {"p", "F*"}, {"q", "p@"}, {"r", "z("},
            {"s", "u_"}, {"t", "U?"}, {"u", "0#"}, {"v", "G."}, {"w", "J%"}, {"x", "D_"},
            {"y", "A%"}, {"z", "Q?"}, {"A", "7:"}, {"B", "H>"}, {"C", "O!"}, {"D", "l;"},
            {"E", "P]"}, {"F", "9-"}, {"G", "I["}, {"H", "x)"}, {"I", "b,"}, {"J", "m<"},
            {"K", "d:"}, {"L", "q^"}, {"M", "s|"}, {"N", "y="}, {"O", "1<"}, {"P", "4,"},
            {"Q", "3]"}, {"R", "L+"}, {"S", "g%"}, {"T", "K}"}, {"U", "Z-"}, {"V", "w{"},
            {"W", "E+"}, {"X", "o#"}, {"Y", "k>"}, {"Z", "c{"}, {"0", "R%"}, {"1", "W;"},
            {"2", "v^"}, {"3", "C["}, {"4", "S$"}, {"5", "V)"}, {"6", "i@"}, {"7", "2."},
            {"8", "a|"}, {"9", "8}"
            }
        };
        private String set1[][] = {
            {"a", "p@"}, {"b", "I*"}, {"c", "a["}, {"d", "f."}, {"e", "g~"}, {"f", "X="},
            {"g", "Q>"}, {"h", "L/"}, {"i", "r_"}, {"j", "e#"}, {"k", "o*"}, {"l", "6~"},
            {"m", "3("}, {"n", "2$"}, {"o", "h?"}, {"p", "q}"}, {"q", "1}"}, {"r", "j)"},
            {"s", "V;"}, {"t", "B)"}, {"u", "4]"}, {"v", "R|"}, {"w", "z{"}, {"x", "9%"},
            {"y", "P<"}, {"z", "5-"}, {"A", "O+"}, {"B", "N?"}, {"C", "y:"}, {"D", "m-"},
            {"E", "U^"}, {"F", "J@"}, {"G", "n]"}, {"H", "M."}, {"I", "W$"}, {"J", "Y;"},
            {"K", "7^"}, {"L", "v@"}, {"M", "x|"}, {"N", "T,"}, {"O", "F!"}, {"P", "Z_"},
            {"Q", "0$"}, {"R", "k!"}, {"S", "c>"}, {"T", "H$"}, {"U", "8["}, {"V", "D!"},
            {"W", "s/"}, {"X", "G="}, {"Y", "i,"}, {"Z", "t("}, {"0", "l+"}, {"1", "w^"},
            {"2", "S#"}, {"3", "b<"}, {"4", "E$"}, {"5", "C%"}, {"6", "K#"}, {"7", "u%"},
            {"8", "A{"}, {"9", "d:"}
        };
        private String set2[][] = {
            {"a", "g$"}, {"b", "x?"}, {"c", "h^"}, {"d", "8+"}, {"e", "o("}, {"f", "b/"},
            {"g", "0@"}, {"h", "U>"}, {"i", "s/"}, {"j", "n#"}, {"k", "a;"}, {"l", "I!"},
            {"m", "7!"}, {"n", "T$"}, {"o", "c<"}, {"p", "t-"}, {"q", "F="}, {"r", "v|"},
            {"s", "Y_"}, {"t", "W#"}, {"u", "B|"}, {"v", "D{"}, {"w", "1$"}, {"x", "z}"},
            {"y", "L~"}, {"z", "w="}, {"A", "H]"}, {"B", "J;"}, {"C", "j,"}, {"D", "N_"},
            {"E", "R*"}, {"F", "i<"}, {"G", "y~"}, {"H", "d)"}, {"I", "O-"}, {"J", "C("},
            {"K", "M]"}, {"L", "G*"}, {"M", "6^"}, {"N", "e#"}, {"O", "Z%"}, {"P", "k)"},
            {"Q", "q!"}, {"R", "Q?"}, {"S", "P@"}, {"T", "4:"}, {"U", "X^"}, {"V", "l["},
            {"W", "r#"}, {"X", "2@"}, {"Y", "K."}, {"Z", "u."}, {"0", "A["}, {"1", "p%"},
            {"2", "9,"}, {"3", "E%"}, {"4", "m}"}, {"5", "f#"}, {"6", "S:"}, {"7", "5>"},
            {"8", "V+"}, {"9", "3{"}
        };
        private String set3[][] = {
            {"a", "S*"}, {"b", "F@"}, {"c", "E_"}, {"d", "1:"}, {"e", "r;"}, {"f", "w-"},
            {"g", "Q^"}, {"h", "Z{"}, {"i", "2="}, {"j", "h<"}, {"k", "T%"}, {"l", "j!"},
            {"m", "K{"}, {"n", "0)"}, {"o", "y^"}, {"p", "u["}, {"q", "5/"}, {"r", "m,"},
            {"s", "n|"}, {"t", "L-"}, {"u", "3}"}, {"v", "d%"}, {"w", "x%"}, {"x", "k+"},
            {"y", "C?"}, {"z", "v#"}, {"A", "b~"}, {"B", "s."}, {"C", "i!"}, {"D", "e$"},
            {"E", "V("}, {"F", "X$"}, {"G", "9@"}, {"H", "W@"}, {"I", "J_"}, {"J", "f|"},
            {"K", "R]"}, {"L", "a!"}, {"M", "6["}, {"N", "H>"}, {"O", "o?"}, {"P", "p<"},
            {"Q", "8#"}, {"R", "N#"}, {"S", "Y:"}, {"T", "U~"}, {"U", "G@"}, {"V", "O}"},
            {"W", "c+"}, {"X", "z$"}, {"Y", "7)"}, {"Z", "l,"}, {"0", "P]"}, {"1", "4;"},
            {"2", "q="}, {"3", "I^"}, {"4", "D("}, {"5", "t>"}, {"6", "g."}, {"7", "A/"},
            {"8", "M@"}, {"9", "B*"}
        };
        private String set4[][] = {
            {"a", "x!"}, {"b", "S("}, {"c", "3["}, {"d", "u%"}, {"e", "e^"}, {"f", "H?"},
            {"g", "D/"}, {"h", "T?"}, {"i", "7%"}, {"j", "r,"}, {"k", "P<"}, {"l", "9*"},
            {"m", "X^"}, {"n", "Y_"}, {"o", "1]"}, {"p", "A)"}, {"q", "K#"}, {"r", "R}"},
            {"s", "W!"}, {"t", "g~"}, {"u", "v_"}, {"v", "o|"}, {"w", "M-"}, {"x", "N@"},
            {"y", "m*"}, {"z", "4!"}, {"A", "V%"}, {"B", "k>"}, {"C", "5{"}, {"D", "h;"},
            {"E", "G$"}, {"F", "C>"}, {"G", "I+"}, {"H", "z["}, {"I", "O("}, {"J", "b/"},
            {"K", "q="}, {"L", "2)"}, {"M", "l*"}, {"N", "0*"}, {"O", "F-"}, {"P", "s#"},
            {"Q", "B."}, {"R", "y@"}, {"S", "L="}, {"T", "J:"}, {"U", "a]"}, {"V", "i<"},
            {"W", "w{"}, {"X", "f;"}, {"Y", "E}"}, {"Z", "8~"}, {"0", "d@"}, {"1", "j$"},
            {"2", "Z#"}, {"3", "Q:"}, {"4", "6$"}, {"5", "n|"}, {"6", "p,"}, {"7", "c."},
            {"8", "U+"}, {"9", "t^"}
        };
        private String set5[][] = {
            {"a", "c("}, {"b", "H("}, {"c", "p-"}, {"d", "a~"}, {"e", "o$"}, {"f", "K."},
            {"g", "6="}, {"h", "G#"}, {"i", "T<"}, {"j", "L}"}, {"k", "v>"}, {"l", "E("},
            {"m", "O+"}, {"n", "V["}, {"o", "C$"}, {"p", "9]"}, {"q", "h,"}, {"r", "f@"},
            {"s", "I~"}, {"t", "i."}, {"u", "A{"}, {"v", "1^"}, {"w", "e!"}, {"x", "u|"},
            {"y", "P="}, {"z", "0_"}, {"A", "2["}, {"B", "N/"}, {"C", "w^"}, {"D", "m]"},
            {"E", "t}"}, {"F", "Z*"}, {"G", "J?"}, {"H", "x|"}, {"I", "R!"}, {"J", "s$"},
            {"K", "Y!"}, {"L", "W%"}, {"M", "d%"}, {"N", "k?"}, {"O", "3;"}, {"P", "S("},
            {"Q", "8%"}, {"R", "j*"}, {"S", "D_"}, {"T", "n{"}, {"U", "Q>"}, {"V", "z/"},
            {"W", "q+"}, {"X", "5,"}, {"Y", "B-"}, {"Z", "y<"}, {"0", "U;"}, {"1", "r)"},
            {"2", "4@"}, {"3", "g:"}, {"4", "l^"}, {"5", "7#"}, {"6", "X#"}, {"7", "b:"},
            {"8", "F)"}, {"9", "M@"}
        };
        private String set6[][] = {
            {"a", "B!"}, {"b", "z+"}, {"c", "7?"}, {"d", "E}"}, {"e", "8-"}, {"f", "Q#"},
            {"g", "b="}, {"h", "e*"}, {"i", "J}"}, {"j", "t~"}, {"k", "l%"}, {"l", "q*"},
            {"m", "G_"}, {"n", "W+"}, {"o", "i!"}, {"p", "Z)"}, {"q", "5@"}, {"r", "w%"},
            {"s", "0)"}, {"t", "d{"}, {"u", "m$"}, {"v", "o("}, {"w", "P|"}, {"x", "X~"},
            {"y", "V>"}, {"z", "S["}, {"A", "A^"}, {"B", "1#"}, {"C", "j/"}, {"D", "h%"},
            {"E", "Y$"}, {"F", "a^"}, {"G", "H;"}, {"H", "2^"}, {"I", "y!"}, {"J", "U@"},
            {"K", "M?"}, {"L", "s."}, {"M", "D="}, {"N", "L{"}, {"O", "k,"}, {"P", "r;"},
            {"Q", "f-"}, {"R", "v<"}, {"S", "4/"}, {"T", "p["}, {"U", "F_"}, {"V", "N,"},
            {"W", "C>"}, {"X", "u|"}, {"Y", "I<"}, {"Z", "x("}, {"0", "O]"}, {"1", "c#"},
            {"2", "n<"}, {"3", "g."}, {"4", "3]"}, {"5", "9:"}, {"6", "R<"}, {"7", "T$"},
            {"8", "6@"}, {"9", "K:"}
        };
        private String set7[][] = {
            {"a", "o("}, {"b", "1+"}, {"c", "T!"}, {"d", "d>"}, {"e", "V_"}, {"f", "e{"},
            {"g", "A~"}, {"h", "p~"}, {"i", "Z|"}, {"j", "4)"}, {"k", "R."}, {"l", "y."},
            {"m", "5}"}, {"n", "M:"}, {"o", "D;"}, {"p", "X^"}, {"q", "U["}, {"r", "J^"},
            {"s", "8["}, {"t", "S#"}, {"u", "P{"}, {"v", "W/"}, {"w", "C#"}, {"x", "0}"},
            {"y", "c_"}, {"z", "u]"}, {"A", "r]"}, {"B", "B^"}, {"C", "E@"}, {"D", "m("},
            {"E", "N%"}, {"F", "L#"}, {"G", "q!"}, {"H", "f*"}, {"I", "x$"}, {"J", "2)"},
            {"K", "G?"}, {"L", "t:"}, {"M", "K|"}, {"N", "9!"}, {"O", "j^"}, {"P", "O@"},
            {"Q", "s%"}, {"R", "Q="}, {"S", "7$"}, {"T", "a<"}, {"U", "n?"}, {"V", "g$"},
            {"W", "b<"}, {"X", "H,"}, {"Y", "6@"}, {"Z", "h,"}, {"0", "i+"}, {"1", "3="},
            {"2", "w/"}, {"3", "v*"}, {"4", "l-"}, {"5", "Y%"}, {"6", "F;"}, {"7", "z^"},
            {"8", "k-"}, {"9", "I>"}
        };
        private String set8[][] = {
            {"a", "U."}, {"b", "b~"}, {"c", "Z#"}, {"d", "0-"}, {"e", "J>"}, {"f", "e}"},
            {"g", "l]"}, {"h", "2_"}, {"i", "N="}, {"j", "7#"}, {"k", "i("}, {"l", "V<"},
            {"m", "4<"}, {"n", "Y}"}, {"o", "T:"}, {"p", "a$"}, {"q", "B^"}, {"r", "M("},
            {"s", "r{"}, {"t", "z?"}, {"u", "R~"}, {"v", "k;"}, {"w", "x]"}, {"x", "s!"},
            {"y", "G^"}, {"z", "q$"}, {"A", "j$"}, {"B", "S*"}, {"C", "6#"}, {"D", "n!"},
            {"E", "A)"}, {"F", "E;"}, {"G", "c%"}, {"H", "u="}, {"I", "1>"}, {"J", "3$"},
            {"K", "D@"}, {"L", "f|"}, {"M", "w/"}, {"N", "o^"}, {"O", "H,"}, {"P", "P@"},
            {"Q", "O."}, {"R", "g_"}, {"S", "C%"}, {"T", "W/"}, {"U", "9:"}, {"V", "m|"},
            {"W", "8+"}, {"X", "L*"}, {"Y", "v!"}, {"Z", "X["}, {"0", "F-"}, {"1", "5+"},
            {"2", "I?"}, {"3", "p@"}, {"4", "t)"}, {"5", "K,"}, {"6", "Q%"}, {"7", "y{"},
            {"8", "d)"}, {"9", "h["}
        };
        private String set9[][] = {
            {"a", "L?"}, {"b", "f#"}, {"c", "2~"}, {"d", "l_"}, {"e", "u."}, {"f", "j+"},
            {"g", "8_"}, {"h", "7["}, {"i", "U|"}, {"j", "N="}, {"k", "5@"}, {"l", "c("},
            {"m", "x|"}, {"n", "0@"}, {"o", "e*"}, {"p", "A!"}, {"q", "F>"}, {"r", "9}"},
            {"s", "G#"}, {"t", "Z]"}, {"u", "i|"}, {"v", "m="}, {"w", "d!"}, {"x", "P!"},
            {"y", "C;"}, {"z", "y<"}, {"A", "k+"}, {"B", "g["}, {"C", "q@"}, {"D", "t$"},
            {"E", "D^"}, {"F", "X{"}, {"G", "6}"}, {"H", "p^"}, {"I", "O/"}, {"J", "4$"},
            {"K", "M,"}, {"L", "b;"}, {"M", "a%"}, {"N", "T)"}, {"O", "s%"}, {"P", "Q]"},
            {"Q", "h*"}, {"R", "n:"}, {"S", "S."}, {"T", "Y:"}, {"U", "3,"}, {"V", "V("},
            {"W", "I~"}, {"X", "R$"}, {"Y", "w/"}, {"Z", "1>"}, {"0", "B#"}, {"1", "K%"},
            {"2", "z?"}, {"3", "H{"}, {"4", "v|"}, {"5", "o^"}, {"6", "r-"}, {"7", "E)"},
            {"8", "J-"}, {"9", "W<"}
        };
        private String set10[][] = {
            {"0", "n@"}, {"1", "y;"}, {"2", "j["}, {"3", "m!"}, {"4", "e?"}, {"5", "o^"},
            {"6", "N~"}, {"7", "2{"}, {"8", "K@"}, {"9", "g="}
        };

        public KeyClass() {
            set = null;
            isKeySet = false;
            word = null;
            cpkey = null;
        }
    }

    public PbEncrypter() {
        cipherKey = 10;
        keyClass = null;
        keyClass = new KeyClass();
    }

    private String encode(String password) {
        String encodedPassword = null;
        try {
            byte inputBytes[] = password.getBytes("UTF8");
            MessageDigest MD = MessageDigest.getInstance("MD5");
            MD.update(inputBytes);
            byte digest[] = MD.digest();
            BASE64Encoder encoder = new BASE64Encoder();
            encodedPassword = encoder.encode(digest);
        } catch (UnsupportedEncodingException UsEnExp) {
            logger.error("Exception: ", UsEnExp);
        } catch (NoSuchAlgorithmException NSAExp) {
            logger.error("Exception: ", NSAExp);
        }
        return encodedPassword;
    }

    public String decrypt(String password) {
        try {
            if (password != null) {
                password = password.trim();
            }
//            String decrptedPassword = "";
            StringBuilder decrptedPassword = new StringBuilder();
            int passwordLength = password.length();
            String keyStr = password.substring(passwordLength - 2, passwordLength);
            String secoundaryKey = password.substring(passwordLength - 4, passwordLength - 2);
            keyClass.setKeyNo(10);
            keyStr = keyClass.getWord(keyStr);
            secoundaryKey = keyClass.getWord(secoundaryKey);
            int decryptKey = Integer.parseInt(keyStr);
            int decryptSecoundaryKey = Integer.parseInt(secoundaryKey);
            keyClass.setKeyNo(decryptKey);
            for (int i = 0; i < passwordLength - 4; i += 2) {
//                decrptedPassword = decrptedPassword + keyClass.getWord(password.substring(i, i + 2));
                decrptedPassword.append( keyClass.getWord(password.substring(i, i + 2)));
            }

            password = unscramble(decrptedPassword.toString(), decryptSecoundaryKey);
            return password;
        } catch (NumberFormatException exp) {
           logger.error("Exception: ", exp);
            return null;
        }
    }

    public String encrypt(String password) {
        try {
            if (password != null) {
                password = password.trim();
            }
//            String encrptedPassword = "";
            StringBuilder encrptedPassword = new StringBuilder();
            int passwordLength = password.length();
            int encryptKey = getKey(password);
            int encryptSecoundaryKey = randomArray[encryptKey];
            password = scramble(password, encryptSecoundaryKey);
            keyClass.setKeyNo(encryptKey);
            for (int i = 0; i < passwordLength; i++) {
//                encrptedPassword = encrptedPassword + keyClass.getKey(password.charAt(i) + "");
//                encrptedPassword = encrptedPassword + keyClass.getKey(Character.toString(password.charAt(i)));
                encrptedPassword.append( keyClass.getKey(Character.toString(password.charAt(i))));
            }

            keyClass.setKeyNo(10);
            String primaryKey = keyClass.getKey(Integer.toString(encryptKey ));
//            String primaryKey = keyClass.getKey(encryptKey + "");
//            String secoundaryKey = keyClass.getKey(encryptSecoundaryKey + "");
            String secoundaryKey = keyClass.getKey(String.valueOf(encryptSecoundaryKey));
            encrptedPassword.append( secoundaryKey).append( primaryKey);
            return encrptedPassword.toString();
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
            return null;
        }
    }

    /*
     * public String encryptOld(String password) { if(password != null) password
     * = password.trim(); int passwordLength = password.length(); String
     * encrptedPassword = ""; int encryptKey = getKey(password); int
     * encryptSecoundaryKey = randomArray[encryptKey]; password =
     * scramble(password, encryptSecoundaryKey); keyClass.setKeyNo(encryptKey);
     * for(int i = 0; i < passwordLength; i++) encrptedPassword =
     * encrptedPassword + keyClass.getKey(password.charAt(i) + "");
     *
     * keyClass.setKeyNo(10); String primaryKey = keyClass.getKey(encryptKey +
     * ""); String secoundaryKey = keyClass.getKey(encryptSecoundaryKey + "");
     * encrptedPassword = encrptedPassword + secoundaryKey + primaryKey; String
     * encrptedPassword1 = encode(encrptedPassword); encrptedPassword1 =
     * encrptedPassword1.substring(0, encrptedPassword1.length() - 2); return
     * encrptedPassword1; }
     */
    private int getKey(String testString) {
        int store = 0;
        int value = 0;
        char chars[] = testString.toCharArray();
        for (int i = 0; i < testString.length(); i++) {
            value += chars[i];
        }

        store = value;
        do {
            String test =String.valueOf( store );
            value = store;
            store = 0;
            int strLen = test.length();
            if (strLen != 1) {
                char newChars[] = test.toCharArray();
                for (int k = 0; k < strLen; k++) {
//                    store += Integer.parseInt(newChars[k] + "");
                    store += Integer.parseInt(Character.toString(newChars[k]) );
                }

            } else {
                return value;
            }
        } while (true);
    }

    private static void log(String s) {
    }

    public static void main(String args1[]) {
        PbEncrypter enc = new PbEncrypter();
        String epwd = enc.encode("progen");
        //String dpwd = enc.decrypt(epwd);
    }

    private String scramble(String word, int scrableIndex) {
//        String string = "";
        StringBuilder string = new StringBuilder();
        boolean flag = true;
        label0:
        switch (scrableIndex) {
            default:
                break;

            case 0: // '\0'
            {
                int length = word.length();
                int counter = -1;
                while (flag) {
                    if (++counter >= length) {
                        break;
                    }
                    if (++counter < length) {
//                        string = string + word.charAt(counter) + word.charAt(counter - 1);
                        string.append( word.charAt(counter)).append( word.charAt(counter - 1));
                        continue;
                    }
//                    string = string + word.charAt(counter - 1);
                    string.append( word.charAt(counter - 1));
                    break;
                }
                break;
            }

            case 1: // '\001'
            {
                String even = "";
                String odd = "";
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    even = even + chars[i];
                    i++;
                    odd = odd + chars[i];
                    i++;
                }

                if (noOfChars > counter * 2) {
                    even = even + chars[i];
                }
//                string = even + odd;
                string.append(  even ).append( odd);
                break;
            }

            case 2: // '\002'
            {
                String even = "";
                String odd = "";
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    even = even + chars[i];
                    i++;
                    odd = odd + chars[i];
                    i++;
                }

                if (noOfChars > counter * 2) {
                    even = even + chars[i];
                }
               //                string = even + odd;
                string.append(  even ).append( odd);
                break;
            }

            case 3: // '\003'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    char temp = chars[i];
                    chars[i] = chars[i + 1];
                    chars[i + 1] = temp;
                    i++;
                    i++;
                }

                for (int j = 0; j < noOfChars; j++) {
//                    string = string + chars[j];
                    string.append(chars[j]);
                }

                break;
            }

            case 4: // '\004'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                for (int i = noOfChars - 1; i >= 0; i--) {
                       string.append(chars[i]);
                }

                break;
            }

            case 5: // '\005'
            {
                int length = word.length();
                int counter = -1;
                while (flag) {
                    if (++counter >= length) {
                        break label0;
                    }
                    if (++counter < length) {
//                        string = string + word.charAt(counter) + word.charAt(counter - 1);
                        string.append( word.charAt(counter) ).append( word.charAt(counter - 1));
                    } else {
//                        string = string + word.charAt(counter - 1);
                        string.append(word.charAt(counter - 1));
                        break label0;
                    }
                }
                break;
            }

            case 6: // '\006'
            {
                String even = "";
                String odd = "";
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    even = even + chars[i];
                    i++;
                    odd = odd + chars[i];
                    i++;
                }

                if (noOfChars > counter * 2) {
                    even = even + chars[i];
                }
//                string = even + odd;
                string.append(even).append(odd) ;
                break;
            }

            case 7: // '\007'
            {
                String even = "";
                String odd = "";
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    even = even + chars[i];
                    i++;
                    odd = odd + chars[i];
                    i++;
                }

                if (noOfChars > counter * 2) {
                    even = even + chars[i];
                }
                 string.append(even).append(odd) ;
                break;
            }

            case 8: // '\b'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    char temp = chars[i];
                    chars[i] = chars[i + 1];
                    chars[i + 1] = temp;
                    i++;
                    i++;
                }

                for (int j = 0; j < noOfChars; j++) {
//                    string = string + chars[j];
                    string.append(chars[j]);
                }

                break;
            }

            case 9: // '\t'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                for (int i = noOfChars - 1; i >= 0; i--) {
//                    string = string + chars[i];
                    string.append( chars[i]);
                }

                break;
            }
        }
        return string.toString();
    }

    private String unscramble(String word, int scrableIndex) {
//        String string = "";
        StringBuilder string = new StringBuilder();
        boolean flag = true;
        label0:
        switch (scrableIndex) {
            default:
                break;

            case 0: // '\0'
            {
                int length = word.length();
                int counter = -1;
                while (flag) {
                    if (++counter >= length) {
                        break;
                    }
                    if (++counter < length) {
//                        string = string + word.charAt(counter) + word.charAt(counter - 1);
                        string.append(word.charAt(counter)).append( word.charAt(counter - 1));
                        continue;
                    }
                    string.append( word.charAt(counter - 1));
                    break;
                }
                break;
            }

            case 1: // '\001'
            {
                boolean lenFlag = true;
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                if (noOfChars > counter * 2) {
                    lenFlag = false;
                    counter++;
                }
                char even[] = word.substring(0, counter).toCharArray();
                char odd[] = word.substring(counter, noOfChars).toCharArray();
                if (!lenFlag) {
                    counter--;
                }
                for (int i = 0; i < counter; i++) {
//                    string = string + even[i];
//                    string = string + odd[i];
                    string.append(even[i]).append(odd[i]);
                }

                if (!lenFlag) {
//                    string = string + even[counter];
                    string.append( even[counter]);
                }
                break;
            }

            case 2: // '\002'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                char odd[] = word.substring(0, counter).toCharArray();
                char even[] = word.substring(counter, noOfChars).toCharArray();
                for (int i = 0; i < counter; i++) {
//                    string = string + even[i];
//                    string = string + odd[i];
                      string.append(even[i]).append(odd[i]);
                }

                if (noOfChars > counter * 2) {
                    string.append( even[counter]);
                }
                break;
            }

            case 3: // '\003'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    char temp = chars[i];
                    chars[i] = chars[i + 1];
                    chars[i + 1] = temp;
                    i++;
                    i++;
                }

                for (int j = 0; j < noOfChars; j++) {
//                    string = string + chars[j];
                    string.append( chars[j]);
                }

                break;
            }

            case 4: // '\004'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                for (int i = noOfChars - 1; i >= 0; i--) {
//                    string = string + chars[i];
                    string.append( chars[i]);
                }

                break;
            }

            case 5: // '\005'
            {
                int length = word.length();
                int counter = -1;
                while (flag) {
                    if (++counter >= length) {
                        break label0;
                    }
                    if (++counter < length) {
//                        string = string + word.charAt(counter) + word.charAt(counter - 1);
                        string.append(word.charAt(counter) ).append( word.charAt(counter - 1));
                    } else {
//                        string = string + word.charAt(counter - 1);
                        string.append( word.charAt(counter - 1));
                        break label0;
                    }
                }
                break;
            }

            case 6: // '\006'
            {
                boolean lenFlag = true;
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                if (noOfChars > counter * 2) {
                    lenFlag = false;
                    counter++;
                }
                char even[] = word.substring(0, counter).toCharArray();
                char odd[] = word.substring(counter, noOfChars).toCharArray();
                if (!lenFlag) {
                    counter--;
                }
                for (int i = 0; i < counter; i++) {
//                    string = string + even[i];
//                    string = string + odd[i];
                    string.append(even[i]).append(odd[i]);
                }

                if (!lenFlag) {
//                    string = string + even[counter];
                    string.append(even[counter]);
                }
                break;
            }

            case 7: // '\007'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                char odd[] = word.substring(0, counter).toCharArray();
                char even[] = word.substring(counter, noOfChars).toCharArray();
                for (int i = 0; i < counter; i++) {
//                    string = string + even[i];
//                    string = string + odd[i];
                      string.append(even[i]).append(odd[i]);
                }

                if (noOfChars > counter * 2) {
//                    string = string + even[counter];
                       string.append(even[counter]);
                }
                break;
            }

            case 8: // '\b'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                int counter = noOfChars / 2;
                int i = 0;
                for (int j = 0; j < counter; j++) {
                    char temp = chars[i];
                    chars[i] = chars[i + 1];
                    chars[i + 1] = temp;
                    i++;
                    i++;
                }

                for (int j = 0; j < noOfChars; j++) {
//                    string = string + chars[j];
                    string.append(chars[j]);
                }

                break;
            }

            case 9: // '\t'
            {
                char chars[] = word.toCharArray();
                int noOfChars = chars.length;
                for (int i = noOfChars - 1; i >= 0; i--) {
//                    string = string + chars[i];
                    string.append(chars[i]);
                }

                break;
            }
        }
        return string.toString();
    }
}
