package com.freedom.augmentedreality.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.freedom.augmentedreality.app.AppConfig;
import com.freedom.augmentedreality.app.Utils;
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
    ArrayList<Marker> markerListOffline;

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
        File file[] = new File(path).listFiles();
        Log.e("cxz", "Size: " + file.length);
        int i = 0;
        markerListOffline = new ArrayList<>();
        while (i < file.length) {
            if (Utils.getFileExt(file[i].getName()).equals("fset")) {
                String markerName = Utils.getFileName(file[i].getName());

                if (Utils.getFileName(file[i + 1].getName()).equals(markerName)) {
                    if (Utils.getFileName(file[i + 2].getName()).equals(markerName)) {
                        if (Utils.getFileName(file[i + 3].getName()).equals(markerName)) {
                            SQLiteHandler db = new SQLiteHandler(context);
                            Marker m = new Marker((i % 3), markerName, file[i + 3].toString(), file[i].toString(), file[i + 1].toString(), file[i + 2].toString());
                            db.addMarkerOffline(m);
                            db.close();

                            copyToCache();
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
            i += 1;
        }

        return 1;
    }

    private void copyToCache() {

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
