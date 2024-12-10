package com.crm.egift.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import com.crm.egift.R;
import com.crm.egift.utils.CustomDialog;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLoginSubmit)
    public void onLoginSubmit() {
        username = edLoginUserName.getText().toString();
        password = edLoginPassword.getText().toString();
        Boolean isValidEmail = isValidEmailAddress(username);
        if (!username.isEmpty() && !password.isEmpty()) {
            if (isValidEmail) {
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