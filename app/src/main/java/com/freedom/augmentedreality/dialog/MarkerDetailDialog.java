package com.freedom.augmentedreality.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freedom.augmentedreality.R;
import com.freedom.augmentedreality.app.AppConfig;
import com.freedom.augmentedreality.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huylv on 28-Mar-16.
 */
public class MarkerDetailDialog extends Dialog {
    @Bind(R.id.marker_detail_id)
    public TextView marker_detail_id;
    @Bind(R.id.marker_detail_name)
    public TextView marker_detail_name;
    @Bind(R.id.marker_detail_iset)
    public TextView marker_detail_iset;
    @Bind(R.id.marker_detail_fset)
    public TextView marker_detail_fset;
    @Bind(R.id.marker_detail_fset3)
    public TextView marker_detail_fset3;
    @Bind(R.id.marker_detail_status)
    public TextView marker_detail_status;
    @Bind(R.id.marker_detail_image)
    public ImageView marker_detail_image;
    @Bind(R.id.marker_detail_download)
    public Button marker_detail_download;

    Marker marker;
    Context context;
//    DownloadingDialog pdDownload;

    public MarkerDetailDialog(Context c, Marker m) {
        super(c);
        context = c;
        marker = m;
        setContentView(R.layout.dialog_marker_detail);
        ButterKnife.bind(this);

//        pdDownload = new ProgressDialog(context);
//        pdDownload.setTitle("Downloading...");
        setTitle("Marker Detail");

        marker_detail_id.setText(String.valueOf(marker.getId()));
        marker_detail_name.setText(marker.getName());
        if (marker.getIset() != null) {
            marker_detail_iset.setText(marker.getIset());
            marker_detail_fset.setText(marker.getFset());
            marker_detail_fset3.setText(marker.getFset3());
        }

        if (marker.getImage().contains("ARManager")) {
            marker_detail_download.setVisibility(View.GONE);
            File imgFile = new File(marker.getImage());
            if (imgFile.exists()) {
                Picasso.with(context).load(imgFile).into(marker_detail_image);
            } else {
                Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Picasso.with(context).load(AppConfig.baseURL + marker.getImage()).into(marker_detail_image);
        }
    }

    @OnClick(R.id.marker_detail_ok)
    public void dismissDialog() {
        this.dismiss();
    }

    @OnClick(R.id.marker_detail_download)
    public void downloadMarker() {
//        Ion.with(context)
//                .load("http://example.com/really-big-file.zip")
//                // have a ProgressBar get updated automatically with the percent
//                .progressBar()
//                // can also use a custom callback
//                .progress(new ProgressCallback() {
//                    @Override
//                    public void onProgress(long downloaded, long total) {
//
//                    }
//                })
//                .write(new File("/sdcard/really-big-file.zip"))
//                .setCallback(new FutureCallback<File>() {
//                    @Override
//                    public void onCompleted(Exception e, File file) {
//                        // download done...
//                        // do stuff with the File or error
//                    }
//                });
    }
}
