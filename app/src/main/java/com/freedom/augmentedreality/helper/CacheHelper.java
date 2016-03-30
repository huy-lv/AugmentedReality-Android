package com.freedom.augmentedreality.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.freedom.augmentedreality.app.AppConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by huylv on 21-Mar-16.
 */
public class CacheHelper {
    static final int READ_BLOCK_SIZE = 100;
    public static CacheHelper instance;
    private ArrayList<String> contentDat;

    public static CacheHelper getInstance() {
        if (instance == null) instance = new CacheHelper();
        return instance;
    }

    private CacheHelper() {
    }

    public void cacheDataNFT(Context ctx, String markerName) {
        File cacheFolder = new File(ctx.getCacheDir().getAbsolutePath() + "/" + "DataNFT");
        boolean success = true;
        if (!cacheFolder.exists()) {
            success = cacheFolder.mkdir();
        } else {
            Log.e("cxz", "cache folder exist");
        }
        if (success) {
            Log.e("cxz", "cache folder created");
        } else {
            Log.e("cxz", "create cache error");
        }


        File fsetPath = new File(AppConfig.PATH_AR + File.separator + markerName + ".fset");
        File fset3Path = new File(AppConfig.PATH_AR + File.separator + markerName + ".fset3");
        File isetPath = new File(AppConfig.PATH_AR + File.separator + markerName + ".iset");

        if (fsetPath.exists() && fset3Path.exists() && isetPath.exists()) {
            Log.e("cxz", "fset exists");
            File fsetDest = new File(cacheFolder + File.separator + markerName + ".fset");
            if (fsetDest.exists()) {
                Log.e("cxz", "fset dest exists");
            } else {
                Log.e("cxz", "fset dest not exists");
                copyFile(fsetPath.toString(), cacheFolder.toString());
            }

            File fset3Dest = new File(cacheFolder + File.separator + markerName + ".fset3");
            if (fset3Dest.exists()) {
                Log.e("cxz", "fset3 dest exists");
            } else {
                Log.e("cxz", "fset3 dest not exists");
                copyFile(fset3Path.toString(), cacheFolder.toString());
            }

            File isetDest = new File(cacheFolder + File.separator + markerName + ".iset");
            if (isetDest.exists()) {
                Log.e("cxz", "iset dest exists");
            } else {
                Log.e("cxz", "iset dest not exists");
                copyFile(isetPath.toString(), cacheFolder.toString());
            }
        } else {
            Log.e("cxz", " fset not exists");
        }

        changeMarkerNameInDat(ctx,markerName);
    }

    private void changeMarkerNameInDat(Context ctx,String markerName) {
        //change marker name in marker.dat
        File markersdat = new File(ctx.getCacheDir().getAbsolutePath() + "/Data/markers.dat");
        contentDat = new ArrayList<>();
        contentDat.clear();
        //reading text from file
        try {
            FileInputStream fileIn = new FileInputStream(markersdat);
            InputStreamReader inputStreamReader = new InputStreamReader(fileIn);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                if(line.contains("/DataNFT/")){
                    line = "../DataNFT/"+markerName;
                }
                contentDat.add(line);
            }
            inputStreamReader.close();

            new RandomAccessFile(markersdat,"rw").setLength(0);

            FileOutputStream fOut = new FileOutputStream(markersdat);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            for(String s : contentDat){
                myOutWriter.append(s+"\n");
            }
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean copyFile(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end + 1, from.length());
                File source = new File(str1, str2);
                File destination = new File(to, str2);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
