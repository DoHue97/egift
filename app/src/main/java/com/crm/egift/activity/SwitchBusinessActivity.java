package com.crm.egift.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.crm.egift.R;
import com.crm.egift.model.Organisation;
import com.crm.egift.services.CrmServices;
import com.crm.egift.services.ResultCodeEnum;
import com.crm.egift.storage.Storage;
import com.crm.egift.utils.AppUtils;
import com.crm.egift.utils.CustomProgressDialog;
import com.crm.egift.utils.LanguageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.android.material.textfield.TextInputLayout;

public class SwitchBusinessActivity extends AppCompatActivity {
    private final String TAG = "SWITCH_BUSINESS_ACT";
    @BindView(R.id.btSwitchBusinessContinue)
    TextView btSwitchBusinessContinue;
    @BindView(R.id.til_business)
    TextInputLayout til_business;
    @BindView(R.id.actvSelectBusiness)
    AutoCompleteTextView actvSelectBusiness;


    CustomProgressDialog customProgressDialog;

    String orgId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.initLanguage(this);
        setContentView(R.layout.activity_switch_business);
        ButterKnife.bind(this);
        initScreen();
        customProgressDialog = new CustomProgressDialog();

        String loginMode = Storage.getLoginmode(this);
        til_business.setHint(loginMode.equals("MERCHANT")?getString(R.string.select_merchant):getString(R.string.select_business));

        // initial cim settings as false
        Storage.setMediumGiftpass(SwitchBusinessActivity.this, false);
        Storage.setMediumPhone(SwitchBusinessActivity.this, false);
        Storage.setMediumEmail(SwitchBusinessActivity.this, false);

        List<Organisation> orgs = getCacheOrganisations();
        AppUtils.console(getApplicationContext(),TAG, "onCreate orgs=="+ orgs);

        String orgExist = Storage.getBusiness(SwitchBusinessActivity.this);
        if(orgs != null){
            ArrayAdapter<Organisation> langAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_text, orgs);
            langAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
            //set the spinners adapter to the previously created one.
            actvSelectBusiness.setAdapter(langAdapter);

            if(!orgExist.isEmpty()){
                for(Organisation org: orgs) {
                    if(orgExist.equals(org.getId())){
                        actvSelectBusiness.setText(org.getName(), false);
                    }
                }
            }
            else if (orgs.size() > 0){
                Storage.setBusiness(SwitchBusinessActivity.this , orgs.get(0).getId());
                Storage.setBusinessName(SwitchBusinessActivity.this , orgs.get(0).getName());
                actvSelectBusiness.setText(orgs.get(0).getName(), false);
            }
        }
        actvSelectBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object s = adapterView.getItemAtPosition(i);
                Organisation orgSelected = (Organisation)   adapterView.getItemAtPosition(i);
                AppUtils.console(getApplicationContext(),TAG, " onItemSelected orgSelected ===" + orgSelected.getId());

                Storage.setBusiness(SwitchBusinessActivity.this , orgSelected.getId());
                Storage.setBusinessName(SwitchBusinessActivity.this , orgSelected.getName());
            }
        });
    }

    @OnClick(R.id.btSwitchBusinessContinue)
    public void onClickContinue(){
        String cacheBusiness = Storage.getBusiness(SwitchBusinessActivity.this);
        if(cacheBusiness != null && !cacheBusiness.isEmpty()){
            orgId = cacheBusiness;
            customProgressDialog.show(this);
            if(AppUtils.isNetworkConnected(this)){
                new SwitchBusinessTask().execute("");
            }
        }else{
            AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.select_business_warn), null, false);
        }
    }

    private void initScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // Hide the status bar and navigation bar
        fullSceen();
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                fullSceen();
            }
        });
    }

    private List<Organisation> getCacheOrganisations(){
        List<Organisation> listOrgs = new ArrayList<>();
        try {
            // listOrgs.add(0, new Organisation(getString(R.string.select_business), "0", ""));
            String cacheOrgs = Storage.getOrganisations(SwitchBusinessActivity.this);
            if(cacheOrgs != null){
                JSONArray orgs = new JSONArray(cacheOrgs);
                for (int i=0; i< orgs.length() ; i++){
                    ObjectMapper mapper = initMapper();
                    JSONObject org = (JSONObject)orgs.get(i);
                    Organisation convertOrg = mapper.readValue(org.toString(), Organisation.class);
                    if(convertOrg.getStatus().equals("ACTIVE")) {
                        Log.e("AAAAA", convertOrg.getName()+ " "+ convertOrg.getStatus());
                        listOrgs.add(convertOrg);
                    }
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
            String stackTrace = Log.getStackTraceString(e);
            AppUtils.console(getApplicationContext(),TAG, "JSONException stackTrace: " + stackTrace);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            String stackTrace = Log.getStackTraceString(e);
            AppUtils.console(getApplicationContext(),TAG, "JsonMappingException stackTrace: " + stackTrace);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            String stackTrace = Log.getStackTraceString(e);
            AppUtils.console(getApplicationContext(),TAG, "JsonProcessingException stackTrace: " + stackTrace);
        }
        return listOrgs;
    }
    private static ObjectMapper initMapper() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Boolean fromHome = getIntent().getBooleanExtra("from_home", false);
        if (fromHome) {
            Intent intent = new Intent(SwitchBusinessActivity.this, MainActivity.class);
            startActivity(intent);
            SwitchBusinessActivity.this.finish();
        } else {
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            fullSceen();
        }
    }
    private void fullSceen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private class SwitchBusinessTask extends AsyncTask<String, Void, Void> {

        boolean success = false;
        boolean exception = false;
        JSONObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                CrmServices services = new CrmServices(getApplicationContext());
                result = services.switchBusiness(orgId);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(getApplicationContext(),TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.cannot_connect_server), null, false);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.connect_timeout), null, false);
                }else {
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);
                }
                exception = true;

            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (result != null && result.has("code")){
                try {
                    String code = result.getString("code");
                    AppUtils.console(getApplicationContext(),TAG, "result code=="+code);
                    if(code.equals(ResultCodeEnum.OK.getLabel())){
                        String access_token = result.getString("access_token");
                        String refresh_token = result.getString("refresh_token");
                        String exp = result.getString("expiration_date");
                        Storage.setToken(SwitchBusinessActivity.this, access_token);
                        Storage.setRefreshToken(SwitchBusinessActivity.this, refresh_token);
                        Storage.setExpTime(SwitchBusinessActivity.this, exp);
                        JSONArray organisations = result.getJSONArray("organisations");
                        Storage.setOrganisations(SwitchBusinessActivity.this, organisations.toString());
                        Storage.setOrganisationSelected(SwitchBusinessActivity.this, orgId);

                        new GetBusinessLogoTask().execute("");
                        new GetContactSettingTask().execute("");
                        new GetOutLetTask().execute("");



                    }
                    else{
                        AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    String stackTrace = Log.getStackTraceString(e);
                    AppUtils.console(getApplicationContext(),TAG, "Exception stackTrace: " + stackTrace);
                }

            }
            else if(!exception){
                AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.login_failed), null, false);
            }
        }
    }

    private class GetBusinessLogoTask extends AsyncTask<String, Void, Void> {
        boolean exception = false;
        JSONObject result = null;

        @Override
        protected void onPreExecute() {
            customProgressDialog.show(SwitchBusinessActivity.this);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            boolean isMerchant = false;
            String loginMode = Storage.getLoginmode(SwitchBusinessActivity.this);
            if(loginMode.equals("MERCHANT"))
                isMerchant = true;
            try {
                CrmServices services = new CrmServices(getApplicationContext());
                if(isMerchant){
                    result = services.getOrganisation(Storage.getPrimaryOrg(SwitchBusinessActivity.this));
                }
                else result = services.getOrganisation(orgId);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(SwitchBusinessActivity.this,TAG, "Exception stackTrace: " + stackTrace);
                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.cannot_connect_server), null, false);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.connect_timeout), null, false);
                }else {
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);
                }
                exception = true;
                customProgressDialog.dismiss();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customProgressDialog.dismiss();
            if (result != null && result.has("code")){
                try {
                    String code = result.getString("code");
                    if(code.equals(ResultCodeEnum.OK.getLabel())){
                        JSONArray creatives = result.getJSONArray("creatives");
                        if(creatives != null && creatives.length()>0){
                            JSONObject creative = creatives.getJSONObject(0);
                            String urlLogo = creative.getString("url");
                            Storage.setBusinessLogo(SwitchBusinessActivity.this,urlLogo);
                            new DownloadImageTask().execute(urlLogo);
                        }
                    }
                    else {
                        AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(!exception){
                AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);
            }
        }
    }

    private class GetOutLetTask extends AsyncTask<String, Void, Void> {
        boolean exception = false;
        JSONObject result = null;

        @Override
        protected void onPreExecute() {
            customProgressDialog.show(SwitchBusinessActivity.this);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                CrmServices services = new CrmServices(getApplicationContext());
                result = services.getOrganisationOutlet(orgId);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(SwitchBusinessActivity.this,TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.cannot_connect_server), null, false);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.connect_timeout), null, false);
                }else {
                    AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);
                }
                exception = true;
                customProgressDialog.dismiss();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customProgressDialog.dismiss();
            if (result != null && result.has("code")){
                try {
                    String code = result.getString("code");
                    if(code.equals(ResultCodeEnum.OK.getLabel())){
                        JSONArray content = result.getJSONArray("content");
                        if(content != null && content.length()>0){
                            Storage.setOutletsJSON(SwitchBusinessActivity.this,content.toString());
                            Intent intent = new Intent(SwitchBusinessActivity.this, SelectOutletActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Storage.setOutlet(SwitchBusinessActivity.this , Storage.getBusiness(SwitchBusinessActivity.this));
                            Storage.setOutletName(SwitchBusinessActivity.this , Storage.getBusinessName(SwitchBusinessActivity.this));
                            Intent intent = new Intent(SwitchBusinessActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                    }
                    else {
                        AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(!exception){
                AppUtils.msg(SwitchBusinessActivity.this, getString(R.string.system_error), null, false);
            }
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
                String filename = "merchant_logo.png";
                File dir = new ContextWrapper(SwitchBusinessActivity.this).getCacheDir();
                File dest = new File(dir, filename);
                FileOutputStream out = new FileOutputStream(dest);
                Boolean result = bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {

        }
    }

    private class GetContactSettingTask extends AsyncTask<String, Void, Void> {

        boolean success = false;
        boolean exception = false;
        JSONObject result = null;

        @Override
        protected void onPreExecute() {
            customProgressDialog.show(SwitchBusinessActivity.this);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String loginMode = Storage.getLoginmode(SwitchBusinessActivity.this);
                CrmServices services = new CrmServices(getApplicationContext());
                boolean isMerchant = false;
                if(loginMode.equals("MERCHANT"))
                    isMerchant = true;
                result = services.getContactSetting(isMerchant);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(SwitchBusinessActivity.this,TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    AppUtils.console(SwitchBusinessActivity.this,TAG, getString(R.string.cannot_connect_server));
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    AppUtils.console(SwitchBusinessActivity.this, TAG, getString(R.string.connect_timeout));
                }else {
                    AppUtils.console(SwitchBusinessActivity.this, TAG, getString(R.string.system_error));
                }
                exception = true;
                customProgressDialog.dismiss();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customProgressDialog.dismiss();
            if (result != null && result.has("code")){
                try {
                    String code = result.getString("code");
                    if(code.equals(ResultCodeEnum.OK.getLabel())){
                        JSONObject cim_settings = (JSONObject) result.get("cim_settings");
                        JSONArray mediums = (JSONArray) cim_settings.get("mediums");
                        for(int i = 0; i < mediums.length(); i++){
                            String medium = mediums.getString(i);
                            switch (medium){
                                case "GIFT_PASS":
                                    Storage.setMediumGiftpass(SwitchBusinessActivity.this, true);
                                    break;
                                case "EMAIL":
                                    Storage.setMediumEmail(SwitchBusinessActivity.this, true);
                                    break;
                                case "PHONE":
                                    Storage.setMediumPhone(SwitchBusinessActivity.this, true);
                                    break;
                            }
                        }
                    }
                    else {
                        AppUtils.console(SwitchBusinessActivity.this, TAG, "Cannot get cim settings. "+getString(R.string.system_error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(!exception){
                AppUtils.console(SwitchBusinessActivity.this, TAG, "Cannot get cim settings. "+getString(R.string.system_error));
            }
        }
    }
}