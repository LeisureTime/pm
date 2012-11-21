package com.xlabz;

public class Rot13 { 

    public static String rot(String s){
    	StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            strbuf.append(c);
        }
       return strbuf.toString();
    }

}
