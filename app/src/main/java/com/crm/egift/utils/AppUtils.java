package com.crm.egift.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.crm.egift.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static final String TAG = "CARDLINK_APPUTILS";
    public static final String TYPE_WALLET_TOPUP = "wallettopup";
    public static final String TYPE_BUY_PHYSICGIFT = "buyphysicgift";
    public static final String TYPE_BUY_EGIFT = "buyegift";
    public static final String TYPE_PURCHASE_GIFTCARD = "purchasegiftcard";
    public static final String TYPE_PURCHASE_PHONEEMAILOTP = "purchasephone";
    public static final String TYPE_PURCHASE_OTP = "purchaseotp";
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

    static public String customFormat(double value ) {
        Locale locale = new Locale("el", "GR");
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getNumberInstance(locale);

        decimalFormat.applyPattern(pattern);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(value);
    }

    static public ArrayList<Float> extractFloat(String str) {
        ArrayList<Float> f = new ArrayList<Float>();
        Pattern pat = Pattern.compile("[-]?[0-9]*\\.?[0-9]+");
        //matching the string with the pattern
        Matcher m = pat.matcher(str);
        //extracting and storing the float values
        while(m.find()) {
            f.add(Float.parseFloat(m.group()));
        }
        return f;
    }

    static public String customStringFormat(double value ) {
        Locale locale = new Locale("en", "EN");
        // Locale locale = new Locale("el", "GR");
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getNumberInstance(locale);

        decimalFormat.applyPattern(pattern);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(value);

    }
    public static void console(Context context, String tag, String msg){
        if(context != null){
            String date = new java.text.SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());
        }
        Log.d(tag,msg);
    }
    public static ObjectMapper initMapper() {
        ObjectMapper mapper = new ObjectMapper();

        //mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper;
    }
    public static String getRamdomBaseOnDatetime() {
        String transref = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        transref += sdf.format(System.currentTimeMillis());
        transref += String.format("%08d", getRamdom());
        return transref;
    }
    public static int getRamdom() {
        try {
            Random rand = new Random();
            int num = rand.nextInt(10000000);
            return num;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void paymentDialog(Activity activity, String amount, View.OnClickListener listener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomAlertDialog);
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.custom_alert_dialog, viewGroup, false);
//                Button buttonOk=dialogView.findViewById(R.id.buttonOk);
        TextView tvAmount = dialogView.findViewById(R.id.tvAmount);
        TextView btnCash = dialogView.findViewById(R.id.tvCash);
        tvAmount.setText("€"+amount);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        btnCash.setOnClickListener(view -> {
            alertDialog.dismiss();
            listener.onClick(view);
        });
        alertDialog.show();
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
