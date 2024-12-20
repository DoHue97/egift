package com.crm.egift.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.crm.egift.R;

public class CustomDialog extends Dialog {
    public interface OKListener {
        public void onOk();

        public void onCancel();
    }

    private Context context;
    private OKListener okListener;
    private String message;
    private String txtOK;
    private String txtCancel;
    private boolean isSuccess = false;

    @BindView(R.id.txtCustomDialogMessage)
    TextView txtCustomDialogMessage;
    @BindView(R.id.txtCustomDialogProceed)
    TextView txtCustomDialogProceed;
    @BindView(R.id.txtCustomDialogCancel)
    TextView txtCustomDialogCancel;
    @BindView(R.id.imgNoticeSpentCardApproved)
    ImageView imgNoticeSpentCardApproved;
    @BindView(R.id.cvCancel)
    MaterialCardView cvCancel;

    public CustomDialog(@NonNull Context context, OKListener listener) {
        super(context);
        this.context = context;
        this.okListener = listener;
    }

    public CustomDialog(@NonNull Context context, OKListener listener, String message, String txtOk, String txtCancel, boolean isSuccess) {
        super(context);
        this.context = context;
        this.okListener = listener;
        this.message = message;
        this.txtOK = txtOk;
        this.txtCancel = txtCancel;
        this.isSuccess = isSuccess;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        ButterKnife.bind(this);
        if (message != null)
            txtCustomDialogMessage.setText(message);
        if (txtOK != null)
            txtCustomDialogProceed.setText(txtOK);

        if (txtCancel != null && txtCancel.equals("")) {
            cvCancel.setVisibility(View.GONE);
        } else if (txtCancel != null) {
            txtCustomDialogCancel.setText(txtCancel);
        }

        if (isSuccess) {
            imgNoticeSpentCardApproved.setImageDrawable(context.getDrawable(R.drawable.ic_tick));
        }
    }

    @Override
    public void show() {
        try {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            Activity activity = (Activity) context;
            if (activity != null && !activity.isFinishing()) {
                super.show();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        } catch (Exception e) {
            System.out.println("CustomDialog show exception: " + e.getMessage());
        }
    }

    @Override
    public void dismiss() {
        try {
            Activity activity = (Activity) context;
            if (activity != null && !activity.isFinishing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            System.out.println("CustomDialog dismiss exception: " + e.getMessage());
        }
    }

    @OnClick(R.id.txtCustomDialogProceed)
    public void onClickProceed() {
        dismiss();
        if (okListener != null) {
            okListener.onOk();
        }
    }

    @OnClick(R.id.txtCustomDialogCancel)
    public void onClickCancel() {
        dismiss();
        if (okListener != null) {
            okListener.onCancel();
        }
    }
}
