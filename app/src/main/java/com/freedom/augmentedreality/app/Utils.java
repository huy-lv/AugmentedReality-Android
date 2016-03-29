package com.freedom.augmentedreality.app;

/**
 * Created by huylv on 3/29/16.
 */
public class Utils {


    public static String getFileExt(String fileName) {
        return fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());
    }

    public static String getFileName(String fileName){
        return fileName.substring(0,(fileName.lastIndexOf(".")));
    }
}
