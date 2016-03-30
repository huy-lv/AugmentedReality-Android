package com.freedom.augmentedreality.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.freedom.augmentedreality.app.AppConfig;
import com.freedom.augmentedreality.app.Utils;
import com.freedom.augmentedreality.helper.CacheHelper;
import com.freedom.augmentedreality.helper.SQLiteHandler;
import com.freedom.augmentedreality.model.Marker;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by huylv on 18/02/2016.
 */
public class CheckLocalMarker extends AsyncTask<Void, Void, Integer> {

    Context context;
    ProgressDialog pdLoading;
    ArrayList<String> markerNameList;

    public CheckLocalMarker(Context c) {
        context = c;
        pdLoading = new ProgressDialog(context);
        pdLoading.setTitle("Loading...");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdLoading.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {

        //create root directory
        File rootFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "ARManager");
        boolean success = true;
        if (!rootFolder.exists()) {
            success = rootFolder.mkdir();
        } else {
            Log.e("cxz", "folder exist");
        }
        if (success) {
            Log.e("cxz", "root folder created");
        } else {
            Log.e("cxz", "create error");
            return -4;
        }

        //check file
        String path = rootFolder.getPath();
        Log.e("cxz", "Path: " + path);
        AppConfig.PATH_AR = path;
        File files[] = new File(path).listFiles();
        Log.e("cxz", "Size: " + files.length);
        int i = 0;
        SQLiteHandler db = new SQLiteHandler(context);
        db.deleteAllMarkersOffline();
        markerNameList = new ArrayList<>();
        while (i < files.length) {

            if (Utils.getFileExt(files[i].getName()).equals("fset")) {

                String markerName = Utils.getFileName(files[i].getName());
                if(!markerNameList.contains(markerName)) {
                    File isetFile = new File(path + File.separator + markerName + ".iset");
                    if (checkIfFolderContain(files, isetFile)) {
                        File fset3File = new File(path + File.separator + markerName + ".fset3");
                        if (checkIfFolderContain(files, fset3File)) {
                            File imageFile = new File(path + File.separator + markerName + ".jpg");
                            if (checkIfFolderContain(files, imageFile)) {

                                Marker m = new Marker((i % 3), markerName, imageFile.toString(), isetFile.toString(),files[i].toString(), fset3File.toString());
                                db.addMarkerOffline(m);

                                markerNameList.add(markerName);
                                copyToCache(markerName);
                                Log.e("cxz", "marker:" + m);
                            } else {
                                return -3;
                            }
                        } else {
                            return -1;
                        }
                    } else {
                        return -2;
                    }
                }
            }
            i += 1;
        }
        db.close();
        return 1;
    }

    boolean checkIfFolderContain(File fs[],File f){
        for(File m:fs){
            if(m.getName().equals(f.getName())){
                return true;
            }
        }
        return false;
    }

    private void copyToCache(String markerName) {
        CacheHelper cacheHelper = CacheHelper.getInstance();
        cacheHelper.cacheDataNFT(context,markerName);
    }

    @Override
    protected void onPostExecute(Integer aInteger) {
        super.onPostExecute(aInteger);
        Log.e("cxz", "return code " + aInteger);

        pdLoading.dismiss();
        switch (aInteger) {
            case 1:
                Log.e("cxz", "success");
                break;
        }

    }
}
