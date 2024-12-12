package com.crm.egift.activity;


import static com.crm.egift.utils.LanguageUtil.initLanguage;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.crm.egift.R;
import com.crm.egift.utils.CustomProgressDialog;
import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionSuccessActivity extends AppCompatActivity {
    @BindView(R.id.tvHeaderTitle)
    TextView tvHeaderTitle;

    public static final String TAG = "ACT_TRANSACTIONSUCCESS";
    private File fileReceipt = null;
    Date date = null;
    String initBalance;
    String amount = "0";
    String balance = "0";
    String total;
    String amountDue;
    String panCard; //credit number
    String customerName = "";
    String identity = ""; //walletId, Giftcard number ...
    String type = "";
    private CustomProgressDialog customProgressDialog;
    Boolean ignorePaymentMethodInfo;

    @BindView(R.id.tvSuccessfulMessage)
    TextView tvSuccessfulMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLanguage(this);
        setContentView(R.layout.activity_success_transaction);
        ButterKnife.bind(this);
        initScreen();

        customProgressDialog = new CustomProgressDialog();
        tvHeaderTitle.setText(getString(R.string.process_purchase));
        String headerTitle = getIntent().getStringExtra("header title");
        if(headerTitle != null)
            tvHeaderTitle.setText(headerTitle);

        /*customerName = getIntent().getStringExtra("customerName");
        initBalance = getIntent().getStringExtra("initBalance");
        amount = getIntent().getStringExtra("amount");
        balance = getIntent().getStringExtra("balance");
        total = getIntent().getStringExtra("total");
        amountDue = getIntent().getStringExtra("amountDue");
        panCard = getIntent().getStringExtra("panCard");
        identity = getIntent().getStringExtra("walletId/cardNum");
        if(getIntent().getStringExtra("type")!=null)
            type = getIntent().getStringExtra("type");
        date = new Date(getIntent().getLongExtra("dateInMs", 0));
        ignorePaymentMethodInfo = getIntent().getBooleanExtra("IGNORE_PAYMENT_METHOD_INFO", false);*/

        ConstraintLayout homeBuyEgift = findViewById(R.id.home_buy_egift);

        homeBuyEgift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(TransactionSuccessActivity.this, MainActivity.class);
                Intent selectPurchaseIntent = new Intent(TransactionSuccessActivity.this, BuyEGiftActivity.class);

                TaskStackBuilder.create(TransactionSuccessActivity.this)
                        .addNextIntentWithParentStack(mainIntent)
                        .addNextIntent(selectPurchaseIntent)
                        .startActivities();
            }
        });

        ConstraintLayout homeSpendOtp = findViewById(R.id.home_spend);
        homeSpendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(TransactionSuccessActivity.this, MainActivity.class);
                Intent selectPurchaseIntent = new Intent(TransactionSuccessActivity.this, SelectPurchaseMethodActivity.class);

                TaskStackBuilder.create(TransactionSuccessActivity.this)
                        .addNextIntentWithParentStack(mainIntent)
                        .addNextIntent(selectPurchaseIntent)
                        .startActivities();
            }
        });
    }


    @OnClick({R.id.ivHeaderExit, R.id.ivHeaderBack})
    public void onExit(View view){
        finishAffinity();
        overridePendingTransition(0, 0);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        overridePendingTransition(0, 0);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    private void initScreen() {
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

