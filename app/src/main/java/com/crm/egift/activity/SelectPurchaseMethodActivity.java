package com.crm.egift.activity;

import static com.crm.egift.utils.AppUtils.hideSoftKeyboard;
import static com.crm.egift.utils.LanguageUtil.initLanguage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.crm.egift.R;
import com.crm.egift.adapters.ViewPager2Adapter;
import com.crm.egift.model.Offer;
import com.crm.egift.services.CrmServices;
import com.crm.egift.services.ResultCodeEnum;
import com.crm.egift.utils.AppUtils;
import com.crm.egift.utils.CustomDialog;
import com.crm.egift.utils.CustomProgressDialog;
import com.crm.egift.utils.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPurchaseMethodActivity extends AppCompatActivity {
    @BindView(R.id.tvHeaderTitle)
    TextView tvHeaderTitle;
    @BindView(R.id.btOtpSpend)
    TextView btOtpSpend;
    @BindView(R.id.purchaseViewpager)
    ViewPager2 purchaseViewpager;

    public ArrayList<Offer> offers;
    private ViewPager2Adapter vpadapter;
    private CustomProgressDialog customProgressDialog;
    private final String TAG ="SELECT_PURCHASE_METHOD_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLanguage(this);
        setContentView(R.layout.activity_select_purchase_method);
        ButterKnife.bind(this);
        initScreen();

        customProgressDialog = new CustomProgressDialog();
        tvHeaderTitle.setText(getString(R.string.spend));
        purchaseViewpager.setUserInputEnabled(false); //disable swipe
        //set purchase option
        vpadapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());
        purchaseViewpager.setAdapter(vpadapter);
        //get offers
        offers = new ArrayList<>();

        if(AppUtils.isNetworkConnected(this)){
            new GetRewardOffersTask().execute("");
        }

    }

    @OnClick(R.id.ivHeaderExit)
    public void onExit(View view) {
        setResult(ResultCode.EXIT_AND_GO_MAIN);
        super.finish();
    }

    @OnClick( R.id.ivHeaderBack)
    public void onBack(){
        if(purchaseViewpager.getVisibility() == View.VISIBLE) {
            purchaseViewpager.setVisibility(View.GONE);
            hideSoftKeyboard(this, purchaseViewpager);
        }
        else {
            super.finish();
        }
    }
    @OnClick(R.id.btOtpSpend)
    public void onClickOtp(){
        purchaseViewpager.setVisibility(View.VISIBLE);
        vpadapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());
        purchaseViewpager.setAdapter(vpadapter);
        purchaseViewpager.setCurrentItem(1, false);
    }

    private class GetRewardOffersTask extends AsyncTask<String, Void, Void> {
        boolean exception = false;
        JSONObject result = null;
        int page = 1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.show(SelectPurchaseMethodActivity.this);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                CrmServices services = new CrmServices(getApplicationContext());
                result = services.getActiveRewardOffers();
                if (result != null && result.getString("code").equals(ResultCodeEnum.OK.getLabel())){
                    JSONArray content = result.getJSONArray("content");
                    if (page == 1){
                        offers.clear();
                    }
                    if(content != null){
                        for (int i=0; i< content.length() ; i++){
                            ObjectMapper mapper = AppUtils.initMapper();
                            JSONObject org = (JSONObject) content.get(i);
                            if(org.getString("state").equals("ACTIVE")) {
                                Offer convertOfr = mapper.readValue(org.toString(), Offer.class);
                                List<Offer.Award> awards = services.getRewardOffersDetail(convertOfr.getId());
                                AppUtils.console(getApplicationContext(), TAG, "GetRewardOffersTask offer awards = " + awards.toString());
                                convertOfr.setAwards(awards);
                                AppUtils.console(getApplicationContext(), TAG, "GetRewardOffersTask offer item = " + convertOfr.toString());
                                offers.add(convertOfr);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(getApplicationContext(),TAG, "Exception stackTrace: " + stackTrace);

                if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                    AppUtils.msg(SelectPurchaseMethodActivity.this, getString(R.string.cannot_connect_server), null, false);
                }
                else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                    AppUtils.msg(SelectPurchaseMethodActivity.this, getString(R.string.connect_timeout), null, false);
                }else {
                    AppUtils.msg(SelectPurchaseMethodActivity.this, getString(R.string.system_error), null, false);
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
                    AppUtils.console(getApplicationContext(), TAG, "GetRewardOffersTask result=="+result);
                    if(code.equals(ResultCodeEnum.OK.getLabel())){
//                        offerAdapter.notifyDataSetChanged();
                    }
                    else{
                        AppUtils.msg(SelectPurchaseMethodActivity.this, getString(R.string.system_error), new CustomDialog.OKListener() {
                            @Override
                            public void onOk() {
                                SelectPurchaseMethodActivity.this.finish();
                            }

                            @Override
                            public void onCancel() {
                            }
                        }, false);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    String stackTrace = Log.getStackTraceString(e);
                    AppUtils.console(getApplicationContext(),TAG, "JSONException stackTrace: " + stackTrace);
                }
            }
        }
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

    public ViewPager2 getPurchaseViewpager() {
        return purchaseViewpager;
    }
}