package com.freedom.augmentedreality.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freedom.augmentedreality.R;
import com.freedom.augmentedreality.app.AppConfig;
import com.freedom.augmentedreality.dialog.MarkerDetailDialog;
import com.freedom.augmentedreality.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by hienbx94 on 3/21/16.
 */
public class MarkersAdapter extends RecyclerView.Adapter<MarkersAdapter.MyViewHolder> {

    private Context context;
    private List<Marker> markersList;

    public MarkersAdapter(Context c, List<Marker> markersList) {
        context = c;
        this.markersList = markersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_marker_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Marker marker = markersList.get(position);
        holder.name.setText(marker.getName());
        if(marker.getImage().contains("ARManager")){
            File imgFile = new  File(marker.getImage());
            if(imgFile.exists()){
                Picasso.with(context).load(imgFile).into(holder.image);
            }
        }else{
            String image_link = AppConfig.baseURL + marker.getImage();
            Picasso.with(context).load(image_link).into(holder.image);
        }

        holder.relativeLayoutItemMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerDetailDialog markerDetailDialog = new MarkerDetailDialog(context, marker);
                markerDetailDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return markersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public RelativeLayout relativeLayoutItemMarker;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_marker);
            image = (ImageView) view.findViewById(R.id.image_marker);
            relativeLayoutItemMarker = (RelativeLayout) view.findViewById(R.id.relativeLayoutItemMarker);
        }
    }
}
