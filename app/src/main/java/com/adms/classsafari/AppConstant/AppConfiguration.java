package com.adms.classsafari.AppConstant;

/**
 * Created by admsandroid on 3/20/2018.
 */

public class AppConfiguration {

//    public static final String Mode="LIVE";
    public static final String Mode="TEST";



    //public static final String BASEURL = "http://192.168.1.20:8088/WebService.asmx/";// use for office
    public static final String BASEURL = "http://www.classsafari.admssvc.com/webservice.asmx/"; // use for client


    public static String coachId;
    public static String DateStr;
    public static String TimeStr;
    public static String SessionName;
    public static String SessionLocation;
    public static String SessionDuration;
    public static String SessionTime;
    public static String SessionPrice;
    public static String UserName;
    public static String RegisterEmail;
    public static String ClassLocation;
    public static String SessionDate;
    public static String RegionName;
    public static String SessionRating;
    public static String SessionUserRating;


    //ClassSafari String
    public static String classSessionName;
    public static String classteacherSessionName;
    public static String classsessionLocation;
    public static String classsessionDate;
    public static String classsessionDuration;
    public static String classsessionPrice;
    public static String famliyName;
    public static String name;


    //Validation String
    public static final String EMAIL_REGEX = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
}