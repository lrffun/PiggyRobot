package com.example.piggyrobot;

public class RobotManager {
    /*public static String ApiKey = "dc8386759666ede3600e19ececd8ae5e";
    public static String ApiSecret = "lrffun";
    private static String url = "http://i.itpk.cn/api.php?question=!!&api_key=##&api_secret=$$";*/
    private static String  url = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=!!";

    public static String getUrl(String question){
        String real_url = url.replace("!!",question);
        return real_url;
    }
}
