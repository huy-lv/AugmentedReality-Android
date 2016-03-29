package com.freedom.augmentedreality.dialog;

import android.app.Dialog;
import android.content.Context;

import com.freedom.augmentedreality.R;

/**
 * Created by huylv on 3/29/16.
 */
public class DownloadingDialog extends Dialog{

    public DownloadingDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_download_marker);
    }


}
