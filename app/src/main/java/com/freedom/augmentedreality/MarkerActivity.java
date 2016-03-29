package com.freedom.augmentedreality;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.freedom.augmentedreality.adapters.MarkersAdapter;
import com.freedom.augmentedreality.app.AppConfig;
import com.freedom.augmentedreality.asyntask.CheckLocalMarker;
import com.freedom.augmentedreality.dialog.CreateDialog;
import com.freedom.augmentedreality.helper.SQLiteHandler;
import com.freedom.augmentedreality.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkerActivity extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;

    @Bind(R.id.fabOpenGallery)
    public FloatingActionButton fabOpenGallery;

    @Bind(R.id.swipeRefreshLayoutMarkerOnline)
    SwipeRefreshLayout swipeRefreshLayoutMarkerOnline;

    @Bind(R.id.swipeRefreshLayoutMarkerOffline)
    SwipeRefreshLayout swipeRefreshLayoutMarkerOffline;

    @Bind(R.id.marker_list_recycler_view)
    RecyclerView marker_list_recycler_view;

    @Bind(R.id.marker_list_switch_online)
    Button marker_list_switch_online;

    @Bind(R.id.marker_list_switch_offline)
    Button marker_list_switch_offline;

    private ProgressDialog pDialog;
    private MarkersAdapter markerAdapterOnline;
    private List<Marker> markerListOnline = new ArrayList<>();

    private MarkersAdapter markerAdapterOffline;
    private List<Marker> markerListOffline = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        markerAdapterOnline = new MarkersAdapter(this, markerListOnline);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        marker_list_recycler_view.setLayoutManager(mLayoutManager);
        marker_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        marker_list_recycler_view.setAdapter(markerAdapterOnline);
//        getAllMarker();

        markerAdapterOffline = new MarkersAdapter(this,markerListOffline);

        swipeRefreshLayoutMarkerOnline.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllMarker();
            }
        });

        fabOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });


        //check local marker
        CheckLocalMarker clm = new CheckLocalMarker(this);
        clm.execute();
        try {
            switch (clm.get()) {
                case 1:
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    markerListOffline.clear();
                    SQLiteHandler db = new SQLiteHandler(this);
                    markerListOffline.addAll(db.getAllMarkersOffline());
                    db.close();
                    markerAdapterOffline.notifyDataSetChanged();
                    break;
                default:
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.marker_list_switch_online)
    void switchToOnline() {
        marker_list_recycler_view.setAdapter(markerAdapterOnline);
        marker_list_switch_online.setBackgroundColor(Color.parseColor("#00C431"));
        marker_list_switch_offline.setBackgroundColor(Color.parseColor("#7BFF00"));
    }

    @OnClick(R.id.marker_list_switch_offline)
    void switchToOffline() {
        marker_list_recycler_view.setAdapter(markerAdapterOffline);
        marker_list_switch_online.setBackgroundColor(Color.parseColor("#7BFF00"));
        marker_list_switch_offline.setBackgroundColor(Color.parseColor("#00C431"));
    }

    public void getAllMarker() {
        pDialog.setMessage("Sync marker ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_ALLMARKER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray markers = jObj.getJSONArray("markers");
                    SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                    db.deleteAllMarkersOnline();
                    for (int i = 0; i < markers.length(); i++) {
                        JSONObject marker = (JSONObject) markers.get(i);
                        Integer id = marker.getInt("id");
                        String name = marker.getString("name");
                        String image = marker.getString("image");
                        String iset = marker.getString("iset");
                        String fset = marker.getString("fset");
                        String fset3 = marker.getString("fset3");
                        Marker temp = new Marker(id, name, image, iset, fset, fset3);
                        db.addMarkerOnline(temp);
                    }
                    db.close();
                    Log.e("response:", response);

                    db = new SQLiteHandler(getApplicationContext());
                    markerListOnline.clear();
                    markerListOnline.addAll(db.getAllMarkersOnline());
                    db.close();

                    if (swipeRefreshLayoutMarkerOnline.isRefreshing())
                        swipeRefreshLayoutMarkerOnline.setRefreshing(false);
                    markerAdapterOnline.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ArApplication.getInstance().addToRequestQueue(strReq, AppConfig.QUEUE_TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                        final CreateDialog c = new CreateDialog(this, bitmap);
                        c.setTitle("Enter marker name");
                        c.setCanceledOnTouchOutside(false);
                        c.btCreateMarker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                c.dismiss();
                                doUploadMarker(c.etCreateMarkerName.getText().toString(), c.encoded);
                            }
                        });
                        c.show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void doUploadMarker(final String markerName, final String encoded) {

        pDialog.setMessage("Uploading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CREATE_MARKER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(AppConfig.MARKER_ACTIVITY_TAG, "Upload Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject error = jObj.getJSONObject("marker");


                    if (error != null) {
                        Toast.makeText(getApplicationContext(),
                                "upload success", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getApplicationContext(),
                                "upload fail", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppConfig.MARKER_ACTIVITY_TAG, "Upload Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", markerName);
                params.put("base64", encoded);
                return params;
            }

        };

        ArApplication.getInstance().addToRequestQueue(strReq, "tag");
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
