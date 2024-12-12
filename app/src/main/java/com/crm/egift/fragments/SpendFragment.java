package com.crm.egift.fragments;

import static com.crm.egift.utils.AppUtils.hideSoftKeyboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crm.egift.R;
import com.crm.egift.activity.SelectPurchaseMethodActivity;
import com.crm.egift.activity.TransactionSuccessActivity;
import com.crm.egift.model.Customer;
import com.crm.egift.model.Offer;
import com.crm.egift.services.CrmServices;
import com.crm.egift.services.ResultCodeEnum;
import com.crm.egift.storage.Storage;
import com.crm.egift.utils.AppUtils;
import com.crm.egift.utils.CustomProgressDialog;
import com.crm.egift.utils.MoneyTextWatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SpendFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.edFragmentSpend2Otp)
    EditText edFragmentSpend2Otp;

    @BindView(R.id.edFragmentSpend2Amount)
    EditText edFragmentSpend2Amount;

    @BindView(R.id.edFragmentSpend2EWallet)
    EditText edFragmentSpend2EWallet;

    @BindView(R.id.tvFragmentSpend2AmountDue)
    TextView tvFragmentSpend2AmountDue;

    @BindView(R.id.btFragmentSpend2ProcessPurchase)
    TextView btFragmentSpend2ProcessPurchase;

    private CustomProgressDialog customProgressDialog;
    private String cardNumber = null;
    private final String TAG = "SPEND_2_FRAG";
    private String customerOtp = "";
    private double amountSpend = 0.00;
    private String spendMethod = "Cash";
    private Double requestAmount = 0.0;
    private Double amountDue = 0.00;
    private String purchaseId;

    @OnClick(R.id.btFragmentSpend2ProcessPurchase)
    public void onSubmit() {
        if (customerOtp != null && customerOtp.length() < 6) {
            AppUtils.msg(getActivity(), getString(R.string.input_must_6_digis), null, false);
            return;
        }
            String requestAmountStr = edFragmentSpend2EWallet.getText().toString().replace(getString(R.string.euro_symbol), "").replace(",", "");
            if (!requestAmountStr.isEmpty()) {
                requestAmount = Double.parseDouble(requestAmountStr);
                if (requestAmount > amountSpend) {
                    AppUtils.msg(getActivity(), getString(R.string.request_amount_not_more_purchase), null, false);
                    isEnablePurchaseButton();
                    return;
                } else if (requestAmount == 0.00){
                    AppUtils.msg(getActivity(), getString(R.string.request_amount_not_empty), null, false);
                    isEnablePurchaseButton();
                    return;
                }
            }

        AppUtils.console(getContext(), TAG, "amount spend==" + amountSpend);
        if (amountSpend > 0) {
            new MakePurchaseTask().execute("");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spend, container, false);
        view.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                hideSoftKeyboard(getActivity(), v);
                v.clearFocus();
//                    if (requestAmount > amountSpend) {
//                        AppUtils.msg(getActivity(), getString(R.string.request_amount_not_more_purchase), null, false);
            }
            return true;
        });
        unbinder = ButterKnife.bind(this, view);
        customProgressDialog = new CustomProgressDialog();
        edFragmentSpend2Amount.addTextChangedListener(new MoneyTextWatcher(edFragmentSpend2Amount));
        edFragmentSpend2EWallet.addTextChangedListener(new MoneyTextWatcher(edFragmentSpend2EWallet));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SelectPurchaseMethodActivity activity = (SelectPurchaseMethodActivity) getActivity();
        //for merchant flow
        String loginMode = Storage.getLoginmode(activity);
        if(loginMode.equals("MERCHANT") && activity.offers.isEmpty()){
            view.findViewById(R.id.textView8).setVisibility(View.GONE);
            view.findViewById(R.id.cvActiveOffers).setVisibility(View.GONE);
        }
        //
        ListView lv = view.findViewById(R.id.lvActiveOffers);
        lv.setNestedScrollingEnabled(true);
        ArrayAdapter<Offer> adapter = new ArrayAdapter<>(getActivity(), R.layout.my_simple_list_item, activity.offers);
        lv.setAdapter(adapter);

        edFragmentSpend2EWallet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                isEnablePurchaseButton();
            }
        });
        edFragmentSpend2EWallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideSoftKeyboard(activity, v);
                    edFragmentSpend2EWallet.clearFocus();
                    if (requestAmount > amountSpend) {
                        AppUtils.msg(getActivity(), getString(R.string.request_amount_not_more_purchase), null, false);
                    }
                    isEnablePurchaseButton();
                    return true;
                }
                return false;
            }
        });
        edFragmentSpend2Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                isEnablePurchaseButton();
            }
        });
        edFragmentSpend2Otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                isEnablePurchaseButton();
            }
        });
        edFragmentSpend2Otp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                setDisableOtherField();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    customerOtp = edFragmentSpend2Otp.getText().toString();
                    hideSoftKeyboard(activity, v);
                    if (customerOtp != null && customerOtp.length() < 6) {
                        AppUtils.msg(getActivity(), getString(R.string.input_must_6_digis), null, false);
                    }
                    return true;
                }
                return false;
            }
        });

        edFragmentSpend2Amount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideSoftKeyboard(activity, view);
                    return false;
                }
                return false;
            }
        });
    }

    private Boolean isEnablePurchaseButton() {
        Boolean isEnable = true;
        String spendAmount = edFragmentSpend2Amount.getText().toString().replace(getString(R.string.euro_symbol), "").replace(",", "");
        String amountRequest = edFragmentSpend2EWallet.getText().toString().replace(getString(R.string.euro_symbol), "").replace(",", "");
        if (spendAmount.isEmpty()) {
            isEnable = false;
            amountSpend = 0.00;
        } else {
            amountSpend = Double.parseDouble(spendAmount);
        }
        if (amountRequest.isEmpty()) {
            requestAmount = 0.00;
        } else {
            requestAmount = Double.parseDouble(amountRequest);
        }
        customerOtp = edFragmentSpend2Otp.getText().toString();
        if ((customerOtp != null && customerOtp.length() < 6) || amountSpend == 0 || customerOtp.isEmpty() || requestAmount == 0 || requestAmount > amountSpend) {
            isEnable = false;
        }
        amountDue = amountSpend - requestAmount;
        tvFragmentSpend2AmountDue.setText(getString(R.string.euro_symbol) + MoneyTextWatcher.customFormat(amountDue));

        if (isEnable) {
            btFragmentSpend2ProcessPurchase.setEnabled(true);
            btFragmentSpend2ProcessPurchase.setBackground(getResources().getDrawable(R.drawable.keyboard_ok_button));
        } else {
            btFragmentSpend2ProcessPurchase.setEnabled(false);
            btFragmentSpend2ProcessPurchase.setBackgroundColor(getResources().getColor(R.color.buttonDisable));
        }
        return isEnable;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MakePurchaseTask extends AsyncTask<String, Void, Void> {
        String msg = getString(R.string.system_error);
        boolean success = false;
        boolean exception = false;
        JSONObject result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.show(getContext());
        }

        @Override
        protected Void doInBackground(String... strings) {
            boolean isMerchant = false;
            String loginMode = Storage.getLoginmode(getActivity());
            if(loginMode.equals("MERCHANT"))
                isMerchant = true;
            String outletId = Storage.getOutlet(SpendFragment.this.getActivity());
            try {
                CrmServices services = new CrmServices(getContext());
                result = services.makePurchaseOtp(customerOtp, outletId, amountSpend, amountDue, "purchase", requestAmount, isMerchant);
            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(getActivity(), TAG, "Exception stackTrace: " + stackTrace);

                if (e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())) {
                    msg = getString(R.string.cannot_connect_server);
                } else if (e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())) {
                    msg = getString(R.string.connect_timeout);
                } else {
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
            if (result != null && result.has("code")) {
                try {
                    String code = result.getString("code");
                    if (code.equals(ResultCodeEnum.OK.getLabel())) {
                        success = true;
                        purchaseId = result.getString("id");
                        if(spendMethod.equals("Card")){
//                            callDeeplinkTopup(amountDue);
                        }
                        else new GetContactPurchaseTask().execute(purchaseId);
                    } else if (code.equals(ResultCodeEnum.MANY_REQUESTS.getLabel())) {
                        msg = getString(R.string.many_requests);
                    } else if (code.equals(ResultCodeEnum.NOTFOUNDEXCEPTION.getLabel())) {
                        msg = getString(R.string.record_not_found);
                    } else if (code.equals(ResultCodeEnum.CANNOTSPENDAMOUNTWALLETBALANCENOTENOUGHEXCEPTION.getLabel())) {
                        msg = getString(R.string.not_enough_funds);
                    } else if (code.equals(ResultCodeEnum.BAD_REQUEST.getLabel())) {
                        msg = result.getString("message");
                    } else {
                        msg = getString(R.string.spend_failed);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!success) {
                AppUtils.msg(getActivity(), msg, null, false);
            }
        }
    }


    private class GetContactPurchaseTask extends AsyncTask<String, Void, Void> {
        String msg = getString(R.string.system_error);
        boolean success = false;
        boolean exception = false;
        JSONObject getPurchaseResult = null;
        JSONObject getContactResult = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.show(getContext());
        }

        @Override
        protected Void doInBackground(String... strings) {
            String outletId = Storage.getOutlet(SpendFragment.this.getActivity());
            try {
                CrmServices services = new CrmServices(getContext());
                getPurchaseResult = services.getPurchase(strings[0]);
                if (getPurchaseResult != null && getPurchaseResult.has("Code") && getPurchaseResult.getString("Code").equals(ResultCodeEnum.OK.getLabel())) {
                    JSONObject contact = getPurchaseResult.getJSONObject("contact");
                    getContactResult = services.getCustomer(contact.getString("code"), false, false, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);
                AppUtils.console(getActivity(), TAG, "Exception stackTrace: " + stackTrace);

                if (e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())) {
                    msg = getString(R.string.cannot_connect_server);
                    //AppUtils.msg(getActivity(), getString(R.string.cannot_connect_server), null);
                } else if (e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())) {
                    msg = getString(R.string.connect_timeout);
                    //AppUtils.msg(getActivity(), getString(R.string.connect_timeout), null);
                } else {
                    msg = getString(R.string.system_error);
                    //AppUtils.msg(getActivity(), getString(R.string.system_error), null);
                }
                exception = true;
                customProgressDialog.dismiss();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customProgressDialog.dismiss();
            if (getContactResult != null && getContactResult.has("code")) {
                try {
                    String code = getContactResult.getString("code");
                    if (code.equals(ResultCodeEnum.OK.getLabel())) {
                        success = true;
                        JSONArray content = getContactResult.getJSONArray("content");
                        if (content != null && content.length() > 0) {
                            ObjectMapper mapper = AppUtils.initMapper();
                            JSONObject org = (JSONObject) content.get(0);
                            Customer customer = mapper.readValue(org.toString(), Customer.class);
                            Intent intent = new Intent(getActivity(), TransactionSuccessActivity.class);
                            intent.putExtra("header title", getString(R.string.spend) );
                            startActivity(intent);
                        }

                    } else if (code.equals(ResultCodeEnum.MANY_REQUESTS.getLabel())) {
                        msg = getString(R.string.many_requests);
                    } else if (code.equals(ResultCodeEnum.BAD_REQUEST.getLabel())) {
                        //TODO: change
                        msg = "Bad request...";
                    } else {
                        msg = getString(R.string.system_error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!success) {
                AppUtils.msg(getActivity(), msg, null, false);
            }
        }
    }
}