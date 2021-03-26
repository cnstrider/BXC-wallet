package com.landis.eoswallet.util;

import java.util.Random;

public class RandomAccountUtils {

    private static String strAccount=".12345abcdefghijklmnopqrstuvwxyz";

    public static String getRandomAccount(){

        Random random=new Random();

        StringBuffer stringBuffer = new StringBuffer();


        for (int i = 0 ; i <11;i++){
            int number = random.nextInt(32);
            stringBuffer.append(strAccount.charAt(number));
            if (i==10){
                if (stringBuffer.toString().endsWith(".")) {
                    stringBuffer = stringBuffer.replace(stringBuffer.length() - 1, stringBuffer.length(), "");
                    i--;
                }
                if (stringBuffer.toString().startsWith(".")) {
                    stringBuffer = stringBuffer.replace(0, 1, "");
                    i--;
                }
            }
        }
        return stringBuffer.toString();
    }
}
