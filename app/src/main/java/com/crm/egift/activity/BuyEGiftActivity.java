package com.crm.egift.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.crm.egift.R;
import com.crm.egift.model.DeepLinkingResponse;
import com.crm.egift.services.CrmServices;
import com.crm.egift.services.ResultCodeEnum;
import com.crm.egift.storage.Storage;
import com.crm.egift.utils.AppConfig;
import com.crm.egift.utils.AppUtils;
import com.crm.egift.utils.Constants;
import com.crm.egift.utils.CustomProgressDialog;
import com.crm.egift.utils.LanguageUtil;
import com.crm.egift.utils.MoneyTextWatcher;
import com.crm.egift.utils.ResultCode;
import com.crm.egift.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyEGiftActivity extends AppCompatActivity implements TextWatcher{
    @BindView(R.id.tvHeaderTitle)
    TextView tvHeaderTitle;
    @BindView(R.id.edBuyEGiftAmount)
    EditText edBuyEGiftAmount;
    @BindView(R.id.edBuyEGiftSender)
    EditText edBuyEGiftSender;
    @BindView(R.id.edBuyEGiftRecipientName)
    EditText edBuyEGiftRecipientName;
    @BindView(R.id.edBuyEGiftRecipientEmail)
    EditText edBuyEGiftRecipientEmail;
    @BindView(R.id.edBuyEGiftMessage)
    EditText edBuyEGiftMessage;
    @BindView(R.id.btBuyEGiftContinue)
    TextView btBuyEGiftContinue;

    //view1 : click continue -> view2
    @BindView(R.id.view2)
    ConstraintLayout view2;
    @BindView(R.id.tvBuyEGiftAmount)
    TextView tvBuyEGiftAmount;
    @BindView(R.id.tvBuyEGiftSender)
    TextView tvBuyEGiftSender;
    @BindView(R.id.tvBuyEGiftRecipientEmail)
    TextView tvBuyEGiftRecipientEmail;
    @BindView(R.id.tvBuyEGiftRecipientName)
    TextView tvBuyEGiftRecipientName;
    @BindView(R.id.tvBuyEGiftMessage)
    TextView tvBuyEGiftMessage;
    @BindView(R.id.tvYes)
    TextView tvYes;
    @BindView(R.id.tvNo)
    TextView tvNo;

    private final String TAG = "BUY E GIFT";
    private double amount = 0.00;
    private String passPlanId=""; //6018264f-c721-4966-a4fb-e3399290f9ae
    private String recipientName="";
    private String recipientEmail="";
    private String senderName="";
    private String message="";
    private String spendMethod = "Cash";
    private DeepLinkingResponse deepLinkingResponse;
    private Boolean isCancelTopupDeeplink = false;
    private String clientTransactionId;
    private double passPlanMin, passPlanMax;
    CustomProgressDialog customProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.initLanguage(this);
        setContentView(R.layout.activity_buy_egift);
        ButterKnife.bind(this);
        initScreen();

        tvHeaderTitle.setText(getString(R.string.purchase_a_gift_pass));
        customProgressDialog = new CustomProgressDialog();
        edBuyEGiftAmount.addTextChangedListener(new MoneyTextWatcher(edBuyEGiftAmount));
        edBuyEGiftAmount.addTextChangedListener(this);
        edBuyEGiftRecipientEmail.addTextChangedListener(this);
        edBuyEGiftRecipientName.addTextChangedListener(this);
        edBuyEGiftSender.addTextChangedListener(this);

        if(AppUtils.isNetworkConnected(this))
            new GetListPassPlanTask().execute();

        TextView btBuyEGiftContinue = findViewById(R.id.btBuyEGiftContinue);
        btBuyEGiftContinue.setOnClickListener(v -> {
            onContinue();
        });
    }

    public void onContinue(){
        amount = Double.parseDouble(edBuyEGiftAmount.getText().toString().replace(getString(R.string.euro_symbol), "").replace(",", ""));
        recipientName = edBuyEGiftRecipientName.getText().toString();
        recipientEmail = edBuyEGiftRecipientEmail.getText().toString();
        senderName = edBuyEGiftSender.getText().toString();
        message = edBuyEGiftMessage.getText().toString();
        Log.d(TAG, "onContinue: gaaga"+ passPlanMin + "     " + passPlanMax);
        if(amount >= passPlanMin && amount <= passPlanMax){
            if(isValidEmailAddress(recipientEmail)){
                view2.setVisibility(View.VISIBLE);
                tvBuyEGiftAmount.setText(MoneyTextWatcher.customFormat(amount));
                tvBuyEGiftSender.setText(senderName);
                tvBuyEGiftRecipientName.setText(recipientName);
                tvBuyEGiftRecipientEmail.setText(recipientEmail);
                tvBuyEGiftMessage.setText(message);
            }else{
                AppUtils.msg(this, getString(R.string.invalid_email), null, false);
            }
        }
        else{
            AppUtils.msg(this, getString(R.string.gc_value_must).replace("%1", MoneyTextWatcher.customFormat(passPlanMin)).replace("%2", MoneyTextWatcher.customFormat(passPlanMax)), null, false);
        }

    }

    @OnClick(R.id.tvYes)
    public void onYes(){
        spendMethod = "";
        AppUtils.paymentDialog(this, MoneyTextWatcher.customFormat(amount), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.tvCash) {
                    if (AppUtils.isNetworkConnected(BuyEGiftActivity.this)) {
                        spendMethod = "Cash";
                        new CreatePassesTask().execute("");
                    }
                }
            }
        });

    }

    @OnClick(R.id.tvNo)
    public void onNo(){
        view2.setVisibility(View.GONE);
    }

    @OnClick(R.id.ivHeaderBack)
    public void onBack(View view) {
        if(view2.getVisibility()==View.VISIBLE) view2.setVisibility(View.GONE);
        else super.finish();
    }


//    @OnClick({R.id.ivHeaderExit})
//    public void onExit(View view){
//        setResult(ResultCode.EXIT_AND_GO_MAIN);
//        super.finish();
//    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void afterTextChanged(Editable editable) {
        isEnablePurchaseButton();
    }

    private class CreatePassesTask extends AsyncTask<String, Void, Void> {
        String msg = getString(R.string.system_error);
        boolean success = false;
        boolean exception = false;
        JSONObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.show(BuyEGiftActivity.this);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                CrmServices services = new CrmServices(BuyEGiftActivity.this);
                boolean isMerchant = false;
                String loginMode = Storage.getLoginmode(BuyEGiftActivity.this);
                if(loginMode.equals("MERCHANT"))
                    isMerchant = true;
                result = services.createPasses(passPlanId, amount, recipientName, recipientEmail, senderName, message, isMerchant);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(BuyEGiftActivity.this,TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    msg = getString(R.string.cannot_connect_server);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    msg = getString(R.string.connect_timeout);
                }else {
                    msg = getString(R.string.system_error);
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
                        success = true;
                        Intent intent = new Intent(BuyEGiftActivity.this, MainActivity.class);
                        Toast.makeText(BuyEGiftActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();

                    }
                    else if(code.equals(ResultCodeEnum.BAD_REQUEST.getLabel())){
                        msg = result.getString("message");
//                        String param = result.getJSONArray("parameters").toString();
//                        if(param != null){
//                            ArrayList<Float> values = AppUtils.extractFloat(param);
//                            if(values.size() > 1)
//                                msg = getString(R.string.gc_value_must).replace("%1", MoneyTextWatcher.customFormat(values.get(0))).replace("%2", MoneyTextWatcher.customFormat(values.get(1)));
//                        }
                    }
                    else if(code.equals(ResultCodeEnum.MANY_REQUESTS.getLabel())){
                        msg = getString(R.string.many_requests);
                    }
                    else {
                        msg = getString(R.string.spend_failed);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!success){
                AppUtils.msg(BuyEGiftActivity.this, msg, null, false);
            }
        }
    }

    private class GetListPassPlanTask extends AsyncTask<String, Void, Void> {
        String msg = getString(R.string.system_error);
        boolean success = false;
        boolean exception = false;
        JSONObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.show(BuyEGiftActivity.this);
        }

        @Override
        protected Void doInBackground(String... strings) {
//            String outletId = Storage.getOutlet(Spend1Fragment.this.BuyEGiftActivity.this);
            try {
                CrmServices services = new CrmServices(BuyEGiftActivity.this);
                boolean isMerchant = false;
                String loginMode = Storage.getLoginmode(BuyEGiftActivity.this);
                if(loginMode.equals("MERCHANT"))
                    isMerchant = true;
                result = services.getPassPlan(isMerchant);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(BuyEGiftActivity.this,TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    msg = getString(R.string.cannot_connect_server);
                    //AppUtils.msg(BuyEGiftActivity.this, getString(R.string.cannot_connect_server), null);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    msg = getString(R.string.connect_timeout);
                    //AppUtils.msg(BuyEGiftActivity.this, getString(R.string.connect_timeout), null);
                }else {
                    msg = getString(R.string.system_error);
                    //AppUtils.msg(BuyEGiftActivity.this, getString(R.string.system_error), null);
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
                        if(content != null && content.length()>0) {
                            JSONObject pass_plan = (JSONObject) content.get(0);
                            passPlanId = pass_plan.getString("id");
                            new GetPassPlanTask().execute("");
                            success = true;
                        }
                    }
                    else if(code.equals(ResultCodeEnum.MANY_REQUESTS.getLabel())){
                        msg = getString(R.string.many_requests);
                    }
                    else {
                            msg = getString(R.string.cannot_get_passplans);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!success){
                AppUtils.msg(BuyEGiftActivity.this, msg, null, false);
            }
        }
    }

    private class GetPassPlanTask extends AsyncTask<String, Void, Void> {
        String msg = getString(R.string.system_error);
        boolean success = false;
        boolean exception = false;
        JSONObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.show(BuyEGiftActivity.this);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                CrmServices services = new CrmServices(BuyEGiftActivity.this);
                boolean isMerchant = false;
                String loginMode = Storage.getLoginmode(BuyEGiftActivity.this);
                if(loginMode.equals("MERCHANT"))
                    isMerchant = true;
                result = services.getPassPlan(passPlanId, isMerchant);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(BuyEGiftActivity.this,TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    msg = getString(R.string.cannot_connect_server);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    msg = getString(R.string.connect_timeout);
                }else {
                    msg = getString(R.string.system_error);
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
                        JSONObject passPlanValue = result.getJSONObject("value");
                        if(passPlanValue.get("minimum") != null)
                            passPlanMin = passPlanValue.getDouble("minimum");

                        if(passPlanValue.get("maximum") != null)
                            passPlanMax = passPlanValue.getDouble("maximum");
                        success = true;
                    }
                    else if(code.equals(ResultCodeEnum.MANY_REQUESTS.getLabel())){
                        msg = getString(R.string.many_requests);
                    }
                    else {
                        msg = getString(R.string.cannot_get_passplans);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!success){
                AppUtils.msg(BuyEGiftActivity.this, msg, null, false);
            }
        }
    }

    private Boolean isEnablePurchaseButton(){
        Boolean isEnable = true;
        if(edBuyEGiftAmount.getText().toString().isEmpty()){
            isEnable = false;
        }
        if(edBuyEGiftSender.getText().toString().isEmpty()){
            isEnable = false;
        }
        if(edBuyEGiftRecipientEmail.getText().toString().isEmpty()){
            isEnable = false;
        }
        if(edBuyEGiftRecipientName.getText().toString().isEmpty()){
            isEnable = false;
        }

        if(isEnable){
            btBuyEGiftContinue.setEnabled(true);
            btBuyEGiftContinue.setBackground(getDrawable(R.drawable.keyboard_ok_button));
        }
        else{
            btBuyEGiftContinue.setEnabled(false);
            btBuyEGiftContinue.setBackgroundColor(getColor(R.color.buttonDisable));
        }
        return isEnable;
    }


    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email)
                ;
        return m.matches();
    }

    private void initScreen() {
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
}