package com.freedom.augmentedreality.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.freedom.augmentedreality.ArApplication;
import com.freedom.augmentedreality.R;
import com.freedom.augmentedreality.app.AppConfig;
import com.freedom.augmentedreality.model.Marker;

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
    public NetworkImageView marker_detail_image;

    Marker marker;

    public MarkerDetailDialog(Context context, Marker m) {
        super(context);
        marker = m;
        setContentView(R.layout.dialog_marker_detail);
        ButterKnife.bind(this);

        setTitle("Marker Detail");

        marker_detail_id.setText(String.valueOf(marker.getId()));
        marker_detail_name.setText(marker.getName());
        if (marker.getIset() != null) {
            marker_detail_iset.setText(marker.getIset());
            marker_detail_fset.setText(marker.getFset());
            marker_detail_fset3.setText(marker.getFset3());
        }

        marker_detail_image.setImageUrl(AppConfig.baseURL + marker.getImage(), ArApplication.getInstance().getImageLoader());

        //check file local

    }

    @OnClick(R.id.marker_detail_ok)
    public void dismissDialog() {
        this.dismiss();
    }

    @OnClick(R.id.marker_detail_download)
    public void downloadMarker() {

    }
}
