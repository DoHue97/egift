package com.crm.egift.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;
import com.crm.egift.R;
import com.crm.egift.services.CrmServices;
import com.crm.egift.services.ResultCodeEnum;
import com.crm.egift.storage.Storage;
import com.crm.egift.utils.AppUtils;
import com.crm.egift.utils.CustomDialog;
import com.crm.egift.utils.CustomProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LOGIN_ACT";

    @BindView(R.id.btnLoginSubmit)
    TextView btnLoginSubmit;

    @BindView(R.id.edLoginUserName)
    EditText edLoginUserName;

    @BindView(R.id.edLoginPassword)
    EditText edLoginPassword;

    private String username;
    private String password;
    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        customProgressDialog = new CustomProgressDialog();
        TextView btnLoginSubmit = findViewById(R.id.btnLoginSubmit);
        btnLoginSubmit.setEnabled(true);
        btnLoginSubmit.setOnClickListener(v -> {
            onLoginSubmit();
        });
    }

    public void onLoginSubmit() {
        username = edLoginUserName.getText().toString();
        password = edLoginPassword.getText().toString();
        Boolean isValidEmail = isValidEmailAddress(username);
        if(!username.isEmpty() && !password.isEmpty()){
            if(isValidEmail){
                customProgressDialog.show(LoginActivity.this);
                if(AppUtils.isNetworkConnected(this)){
                    (new LoginActivity.LoginTask()).execute("");
                }
            }else{
                AppUtils.msg(LoginActivity.this, getString(R.string.invalid_email), null, false);
            }
        }else{
            AppUtils.msg(LoginActivity.this, getString(R.string.invalid_username_password), null, false);
        }
    }
    private class LoginTask extends AsyncTask<String, Void, Void> {

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
                result = services.login(username, password);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                //TODOV: should remove
//                logMessage = "LoginActivity LoginTask stackTrace: " + stackTrace;
                //
                AppUtils.console(getApplicationContext(),TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    AppUtils.msg(LoginActivity.this, getString(R.string.cannot_connect_server), null, false);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    AppUtils.msg(LoginActivity.this, getString(R.string.connect_timeout), null, false);
                }else {
                    AppUtils.msg(LoginActivity.this, getString(R.string.system_error), null, false);
                }
                exception = true;
                customProgressDialog.dismiss();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customProgressDialog.dismiss();
            //TODOV: should remove
//            new SendEmailTask().execute();
            //
            if (result != null && result.has("code")){
                try {
                    String code = result.getString("code");
                    Log.e(TAG, "result code=="+code);
                    if(code.equals(ResultCodeEnum.OK.getLabel())){
                        String access_token = result.getString("access_token");
                        String refresh_token = result.getString("refresh_token");
                        String exp = result.getString("expiration_date");
                        JSONObject user = result.getJSONObject("user");
                        String full_name = user.getString("first_name") + " " + user.getString("last_name");

                        Storage.setToken(LoginActivity.this, access_token);
                        Storage.setRefreshToken(LoginActivity.this, refresh_token);
                        Storage.setExpTime(LoginActivity.this, exp);
                        Storage.setUserFullname(LoginActivity.this, full_name);
                        JSONArray organisations = result.getJSONArray("organisations");
                        Storage.setOrganisations(LoginActivity.this, organisations.toString());

                        //decode token to get user role and primary organisation
                        JWT jwt = new JWT(access_token);
                        String primaryOrg = jwt.getClaim("primary_organisation_id").asString();
                        String loginMode = jwt.getClaim("login_mode").asString();
                        Storage.setPrimaryOrg(LoginActivity.this, primaryOrg);
                        Storage.setLoginmode(LoginActivity.this, loginMode);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else if(code.equals(ResultCodeEnum.INVALIDLOGINEXCEPTION.getLabel())){
                        AppUtils.msg(LoginActivity.this, getString(R.string.invalid_login), null, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if(!exception){
                AppUtils.msg(LoginActivity.this, getString(R.string.login_failed), null, false);
            }
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email)
                ;
        return m.matches();
    }
}