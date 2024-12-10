package com.crm.egift.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.crm.egift.R;

public class AppUtils {
    public static final String TAG = "EGIFT_APPUTILS";

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean status = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        if(!status){
            CustomDialog.OKListener listener = new CustomDialog.OKListener() {
                @Override
                public void onOk() {
                    Intent in = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS );
                    context.startActivity(in);
                }
                @Override
                public void onCancel() {

                }
            };
            final CustomDialog dialog = new CustomDialog(context,listener, context.getString(R.string.network_not_connect), context.getString(R.string.menu_setting), context.getString(R.string.cancel), false);
            dialog.show();
        }
        return status;
    }

    public static void msg(Activity activity, String str, CustomDialog.OKListener listener, Boolean isSuccess){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog customDialog = new CustomDialog(activity,listener, str, activity.getString(R.string.btn_ok), "", isSuccess);
                customDialog.show();
            }
        });

    }
}
