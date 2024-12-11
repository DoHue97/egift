package com.crm.egift.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.crm.egift.R;
import com.crm.egift.model.Outlet;
import com.crm.egift.storage.Storage;
import com.crm.egift.utils.AppUtils;
import com.crm.egift.utils.CustomProgressDialog;
import com.crm.egift.utils.LanguageUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectOutletActivity extends AppCompatActivity {
    private final String TAG = "SELECT_OUTLET_ACT";
    @BindView(R.id.btSelectOutletContinue)
    TextView btSelectOutletContinue;

    @BindView(R.id.actvSelectOutlet)
    AutoCompleteTextView actvSelectOutlet;
    @BindView(R.id.tvMerchantTitle)
    TextView tvMerchantTitle;
    CustomProgressDialog customProgressDialog;

    private Outlet outletSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.initLanguage(this);
        setContentView(R.layout.activity_select_outlet);
        ButterKnife.bind(this);
        tvMerchantTitle.setText(Storage.getBusinessName(this));
        initScreen();
        customProgressDialog = new CustomProgressDialog();

        List<Outlet> outlets = getCacheOulets();
        AppUtils.console(getApplicationContext(),TAG, "onCreate orgs=="+ outlets);


        String outletExist = Storage.getOutletName(SelectOutletActivity.this);
        if(outlets != null){
            ArrayAdapter<Outlet> langAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_text, outlets);
            langAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
            actvSelectOutlet.setAdapter(langAdapter);
            // select san
//            if(!outletExist.isEmpty()){
//                for(Outlet outlet: outlets) {
//                    if(outletExist.equals(outlet.getName())){
//                        actvSelectOutlet.setText(outlet.getName(), false);
//                    }
//                }
//            }
//            else if (outlets.size() > 0){
//                Storage.setOutlet(SelectOutletActivity.this , outlets.get(0).getId());
//                Storage.setOutletName(SelectOutletActivity.this , outlets.get(0).getName());
//                actvSelectOutlet.setText(outlets.get(0).getName(), false);
//            }
        }

        actvSelectOutlet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Storage.setOutletAddress(SelectOutletActivity.this, "");
                Storage.setOutletCity(SelectOutletActivity.this, "");
                Storage.setOutletPostalcode(SelectOutletActivity.this, "");
                Storage.setOutletEmail(SelectOutletActivity.this, "");
                Storage.setOutletPhone(SelectOutletActivity.this, "");
                Storage.setOutlet(SelectOutletActivity.this , "");
                Storage.setOutletName(SelectOutletActivity.this , "");

                outletSelected = (Outlet)   adapterView.getItemAtPosition(i);
                AppUtils.console(getApplicationContext(),TAG, " onItemSelected ouletSelected ===" + outletSelected.getId());
                Storage.setOutlet(SelectOutletActivity.this , outletSelected.getId());
                Storage.setOutletName(SelectOutletActivity.this , outletSelected.getName());

                ArrayList<Outlet.ContactInfo> contacts = outletSelected.getContact_info();
                if(contacts!=null)
                    for(Outlet.ContactInfo c: contacts){
                        if(c.getType().equals("EMAIL")) Storage.setOutletEmail(SelectOutletActivity.this, c.getValue());
                        if(c.getType().equals("PHONE")) Storage.setOutletPhone(SelectOutletActivity.this, c.getValue());
                    }
                if(outletSelected.getAddresses()!=null) {
                    Outlet.Address addressObj = outletSelected.getAddresses().get(0);
                    String address = addressObj.getAddress_line_1() + (addressObj.getAddress_line_2() != null ? ", " + addressObj.getAddress_line_2() : "");
                    String city = addressObj.getTown_city();
                    String postalcode = addressObj.getPostal_code();
                    Storage.setOutletAddress(SelectOutletActivity.this, address);
                    Storage.setOutletCity(SelectOutletActivity.this, city!=null?city:"");
                    Storage.setOutletPostalcode(SelectOutletActivity.this, postalcode!=null?postalcode:"");
                    Log.e("SELECT_OUTLET_ACT", postalcode+", "+city+"  , "+address);
                }

            }
        });
    }

    @OnClick(R.id.btSelectOutletContinue)
    public void onClickContinue(){
        if(outletSelected != null){
            Storage.setOutlet(SelectOutletActivity.this , outletSelected.getId());
            Storage.setOutletName(SelectOutletActivity.this , outletSelected.getName());
            Intent intent = new Intent(SelectOutletActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            AppUtils.msg(SelectOutletActivity.this, SelectOutletActivity.this.getString(R.string.please_select_outlet), null, false);
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

    private List<Outlet> getCacheOulets(){
        List<Outlet> listOutlets = new ArrayList<>();
        try {
            String cacheOutlets = Storage.getOutletsJSON(SelectOutletActivity.this);
            if(cacheOutlets != null){
                JSONArray orgs = new JSONArray(cacheOutlets);
                for (int i=0; i< orgs.length() ; i++){
                    ObjectMapper mapper = initMapper();
                    JSONObject org = (JSONObject)orgs.get(i);
                    Outlet convertOutlet = mapper.readValue(org.toString(), Outlet.class);
                    if(convertOutlet.getState().equals("ACTIVE") )
                        listOutlets.add(convertOutlet);
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
        return listOutlets;
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
            Intent intent = new Intent(SelectOutletActivity.this, SwitchBusinessActivity.class);
            startActivity(intent);
            SelectOutletActivity.this.finish();
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


}