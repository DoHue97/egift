package com.crm.egift.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONObject;

public class Storage {
    public static String getAppConfig(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("APP_CONFIG", "");
    }
    public static void setAppConfig(Context context, String config){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("APP_CONFIG",config);
        editor.commit();
    }
    public static String getToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("TOKEN", "");
    }
    public static void setToken(Context context, String token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TOKEN",token);
        editor.commit();
    }
    public static String getOrganisations(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("ORGANISATIONS", "");
    }
    public static void setOrganisations(Context context, String orgs){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ORGANISATIONS", orgs);
        editor.commit();
    }
    public static String getBusiness(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("BUSINESS", "");
    }
    public static void setBusiness(Context context, String business){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BUSINESS", business);
        editor.commit();
    }
    public static String getBusinessName(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("BUSINESS_NAME", "");
    }
    public static void setBusinessName(Context context, String business){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BUSINESS_NAME", business);
        editor.commit();
    }
    public static String getBusinessLogo(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("BUSINESS_LOGO", "");
    }
    public static void setBusinessLogo(Context context, String logo){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BUSINESS_LOGO", logo);
        editor.commit();
    }
    public static String getOutletsJSON(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLETSJSON", "");
    }
    public static void setOutletsJSON(Context context, String outletjson){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLETSJSON", outletjson);
        editor.commit();
    }
    public static String getOutlet(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLET", "");
    }
    public static void setOutlet(Context context, String outlet){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLET", outlet);
        editor.commit();
    }
    public static String getOutletName(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLET_NAME", "");
    }
    public static void setOutletName(Context context, String outlet){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLET_NAME", outlet);
        editor.commit();
    }
    public static String getOutletAddress(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLET_ADDRESS", "");
    }
    public static void setOutletAddress(Context context, String address){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLET_ADDRESS", address);
        editor.commit();
    }
    public static String getOutletCity(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLET_CITY", "");
    }
    public static void setOutletCity(Context context, String city){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLET_CITY", city);
        editor.commit();
    }
    public static String getOutletPostalcode(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLET_POSTALCODE", "");
    }
    public static void setOutletPostalcode(Context context, String postalcode){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLET_POSTALCODE", postalcode);
        editor.commit();
    }
    public static String getOutletPhone(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLET_PHONE", "");
    }
    public static void setOutletPhone(Context context, String phone){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLET_PHONE", phone);
        editor.commit();
    }
    public static String getOutletEmail(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("OUTLET_EMAIL", "");
    }
    public static void setOutletEmail(Context context, String email){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OUTLET_EMAIL", email);
        editor.commit();
    }
    public static String getPrimaryOrg(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("PRIMARY_ORG", "");
    }
    public static void setPrimaryOrg(Context context, String org){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PRIMARY_ORG", org);
        editor.commit();
    }
    public static String getLoginmode(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("LOGIN_MODE", "");
    }
    public static void setLoginmode(Context context, String mode){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LOGIN_MODE", mode);
        editor.commit();
    }
    public static String getUserFullname(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("USER_FULLNAME", "");
    }
    public static void setUserFullname(Context context, String fullname){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_FULLNAME", fullname);
        editor.commit();
    }
    public static String getRefreshToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("REFRESH_TOKEN", "");
    }
    public static void setRefreshToken(Context context, String refreshToken){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("REFRESH_TOKEN",refreshToken);
        editor.commit();
    }

    public static String getExpTime(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("EXP_TIME", "");
    }
    public static void setExpTime(Context context, String expTime){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EXP_TIME", expTime);
        editor.commit();
    }
    public static String getOrganisationSelected(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("ORG_SELECTED", "");
    }
    public static void setOrganisationSelected(Context context, String orgId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ORG_SELECTED",orgId);
        editor.commit();
    }
    public static void setMediumEmail(Context context, Boolean isEnable){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("MEDIUM_EMAIL", isEnable);
        editor.commit();
    }
    public static Boolean getMediumEmail(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getBoolean("MEDIUM_EMAIL", false);
    }

    public static void setMediumPhone(Context context, Boolean isEnable){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("MEDIUM_PHONE", isEnable);
        editor.commit();
    }
    public static Boolean getMediumPhone(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getBoolean("MEDIUM_PHONE", false);
    }

    public static void setMediumGiftpass(Context context, Boolean isEnable){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("MEDIUM_GIFTPASS", isEnable);
        editor.commit();
    }
    public static Boolean getMediumGiftpass(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getBoolean("MEDIUM_GIFTPASS", false);
    }
}
