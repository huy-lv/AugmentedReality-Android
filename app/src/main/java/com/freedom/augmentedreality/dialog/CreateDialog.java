package com.freedom.augmentedreality.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.freedom.augmentedreality.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by huylv on 23-Feb-16.
 */
public class CreateDialog extends Dialog {

    public EditText etCreateMarkerName;
    public Button btCreateMarker;
    public Button btCancel;
    public String encoded;

    public CreateDialog(Context context, Bitmap bitmap) {
        super(context);
        setContentView(R.layout.dialog_create_marker);
        etCreateMarkerName = (EditText)findViewById(R.id.etCreateMarkerName);
        btCreateMarker = (Button)findViewById(R.id.btCreateMarker);
        btCancel = (Button) findViewById(R.id.btCreateCancel);

        //encode to base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//        Log.e("cxz", "base64:" + encoded);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }



}
