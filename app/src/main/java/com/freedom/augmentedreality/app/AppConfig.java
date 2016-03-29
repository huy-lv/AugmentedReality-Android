package com.freedom.augmentedreality.app;

/**
 * Created by hienbx94 on 3/6/16.
 */
public class AppConfig {
    public static final String QUEUE_TAG = "queue_tag";
    public static final String MARKER_ACTIVITY_TAG = "Marker Activity";
    public static String PATH_AR ;
    public static String baseURL = "http://192.168.0.8:3000";
    // Server user login url
    public static String URL_LOGIN = baseURL + "/api/logins";
    public static String URL_CREATE_MARKER  = baseURL+"/api/markers";

    // Server user register url
    public static String URL_REGISTER = baseURL + "/api/registers";

    public static String URL_ALLMARKER = baseURL + "/api/markers";
}