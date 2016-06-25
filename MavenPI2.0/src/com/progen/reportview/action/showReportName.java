package com.progen.reportview.action;

import java.util.ArrayList;

public class showReportName {

    public ArrayList buildReportName(String name) {

        //  name = "Friendship isnot about,kjsdf jsdf slfdced jsldfklji;kjfuj kmdfs iojdklf ujisjdlkfkij klsdmfjk sdklfkjsdfklmkjsdkf kjmdkf il;j  io;j dkj kl jfinding similarities it is, aboutrespectinghellomyname.isdaboutrespectinghellomyname.isdxyz one two three four aboutrespectinghellomyname.isdifferences You are not myfriend coz you are like me but because i accept you and respect you the way you are";

        //.println("Length of a string" + name.length() + "\n");
        String str2 = null, str1 = null, str3 = null;
        int div = 30, endloop = 0, ind = 0, strstart = 0;
        float limit = 0f;
        limit = (float) name.length() / 30;
        ArrayList repnames = new ArrayList();

        str2 = String.valueOf(limit);
//        //.println("str2" + str2);
        if (Integer.parseInt(str2.substring(str2.indexOf(".") + 1, str2.length())) > 0) {
            limit++;
        }
//        endloop = (int) limit;
        endloop = 1;
        for (int i = 0; i < endloop; i++) {
            if (name.length() > div) {
                str1 = String.valueOf(name.charAt(div));
//                //.println("str1\t"+str1);
                if (str1.equals(" ")) {
//                    //.println(name.substring(strstart, div));
                    repnames.add(name.substring(strstart, div));
//                    //.println(name.substring(strstart, div).length());
                    strstart = div + 1;
                    div += 30;
                    endloop++;
                } else /*
                 * if (!(str1.equals(" ")))
                 */ {
                    str3 = name.substring(strstart, div);
                    if (str3.lastIndexOf(" ") != -1) {
                        ind = str3.lastIndexOf(" ");
//                        //.println("ind\t" + ind);
                        div += ind + 1;
                        ind += strstart + 1;
//                        //.println(name.substring(strstart, ind));
                        repnames.add(name.substring(strstart, ind));
//                        //.println(name.substring(strstart, ind).length());
                        strstart = ind;
                        endloop++;
                    } else {
                        ind += 30;
                        div += 30;
                        //ind += strstart + 1;
                        // //.println(name.substring(strstart, ind));
//                        //.println(name.substring(strstart, ind).concat("-"));
                        repnames.add(name.substring(strstart, ind).concat("-"));
//                        //.println(name.substring(strstart, ind).length());
                        strstart = ind;
                        endloop++;

//                        //.println("ind in if\t" + ind);
                    }
//                    div += ind;
                }
//                strstart=ind;
//                div += 30;
            } else {
//                div -= 31;
//                //.println(name.substring(strstart, name.length()));
                repnames.add(name.substring(strstart, name.length()));
//                //.println(name.substring(strstart, name.length()).length());
            }
        }
        for (int i = 0; i < repnames.size(); i++) {
            //.println(repnames.get(i));
        }
        return repnames;
    }
}
