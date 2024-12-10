package com.crm.egift.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;


import com.auth0.android.jwt.JWT;
import com.crm.egift.model.PurchaseResponse;
import com.crm.egift.storage.Storage;
import com.crm.egift.utils.AppUtils;
import com.crm.egift.utils.Constants;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CrmServices {
    private static final String TAG = "CL_API_LOG";
    private Context context;
    public CrmServices(Context context){
        this.context = context;
        Constants.CrmConfig.TOKEN = Storage.getToken(context);
        Constants.CrmConfig.REFRESH_TOKEN = Storage.getRefreshToken(context);
    }
    public JSONObject getAppConfig() throws Exception {
        Log.d(TAG, "getAppConfig: APP_ID =" + Constants.CrmConfig.APP_ID);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_APP_CONFIG + "?platform_app_id=" + Constants.CrmConfig.APP_ID;
            result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            Log.d(TAG, "getAppConfig result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getAppConfig: " + Constants.CrmConfig.APP_ID + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject getOrganisation(String orgId) throws Exception {
        Log.d(TAG, "getOrganisation: orgId =" + orgId);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_ORGANISATION.replace("{id}", orgId);
            result = ServiceUtils.sendGetRequestWithToken(url, context);

            Log.d(TAG, "getOrganisation result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getOrganisation orgId = " + orgId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject getOrganisationOutlet(String orgId) throws Exception {
        Log.d(TAG, "getOrganisationOutlet: orgId =" + orgId);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_ORGANISATION.replace("{id}", orgId)+"/network?type=VENUE";
//
            result = ServiceUtils.sendGetRequestWithToken(url, context);
            Log.d(TAG, "getOrganisationOutlet result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getOrganisationOutlet orgId = " + orgId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject getCustomer(String customerValue, Boolean isEmail, Boolean byEmailOnly , Boolean isCallWithToken) throws Exception {
        Log.d(TAG, "getCustomer: customerValue =" + customerValue);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_CUSTOMER + "?include_wallet=true&";
            if(byEmailOnly)
                url += (isEmail?"email_address=":"code=") + Uri.encode(customerValue);
            else
                url += "search_value=" + Uri.encode(customerValue);

            if (isCallWithToken){
                result = ServiceUtils.sendGetRequestWithToken(url, context);
            }
            else{
                result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            }
            Log.d(TAG, "getCustomer result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getCustomer customerValue = " + customerValue + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject getCustomerByCim(String cim, Boolean isCallWithToken) throws Exception {
        Log.d(TAG, "getCustomerByCim: cim =" + cim);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_CUSTOMER + "?include_wallet=true&cim=" + cim;
            if (isCallWithToken){
                result = ServiceUtils.sendGetRequestWithToken(url, context);
            }
            else{
                result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            }
            Log.d(TAG, "getCustomer result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getCustomerByCim: cim =" + cim + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject makePurchase(String contactId, String outLetId, Double amount, Double netAmount, String type, Double requestAmount, boolean isMerchant) throws Exception {
        Log.d(TAG, "makePurchase: contactId=" + contactId );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_MAKE_PURCHASE;

            JSONObject body = new JSONObject();

            JSONObject contact = new JSONObject();
            contact.put("id", contactId);
            JSONObject organisation = new JSONObject();
            organisation.put("id", outLetId);
            JSONObject transaction_amounts = new JSONObject();
            transaction_amounts.put("total", amount);
            transaction_amounts.put("net", netAmount);
            JSONObject classification = new JSONObject();
            classification.put("name", type);

            body.put("contact", contact);
            body.put("organisation", organisation);
            body.put("transaction_amounts", transaction_amounts);
            body.put("classification", classification);

            if(requestAmount > 0){
                JSONObject spend_request = new JSONObject();
                spend_request.put("amount", requestAmount);
                spend_request.put("restrict_fully_covered", false);
                body.put("spend_request", spend_request);
            }
            if(isMerchant)
                result = ServiceUtils.sendPostRequestWithApiKey(url,  body, context);
            else
                result = ServiceUtils.sendPostRequestWithToken(url, body, context);
            Log.d(TAG, "makePurchase result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 429){
                    result.put("code", ResultCodeEnum.MANY_REQUESTS);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "makePurchase: contactId" + contactId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject makePurchaseOtp(String otp, String outLetId, Double amount, Double netAmount, String type, Double requestAmount, boolean isMerchant) throws Exception {
        Log.d(TAG, "makePurchaseOTP: contactOtp=" + otp );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_MAKE_PURCHASE;

            JSONObject body = new JSONObject();

            JSONObject contact = new JSONObject();
            contact.put("otp", otp);
            JSONObject organisation = new JSONObject();
            organisation.put("id", outLetId);
            JSONObject transaction_amounts = new JSONObject();
            transaction_amounts.put("total", amount);
            transaction_amounts.put("net", netAmount);
            JSONObject classification = new JSONObject();
            classification.put("name", type);

            body.put("contact", contact);
            body.put("organisation", organisation);
            body.put("transaction_amounts", transaction_amounts);
            body.put("classification", classification);

            if(requestAmount > 0){
                JSONObject spend_request = new JSONObject();
                spend_request.put("amount", requestAmount);
                spend_request.put("restrict_fully_covered", true);
                body.put("spend_request", spend_request);
            }

            if(isMerchant)
                result = ServiceUtils.sendPostRequestWithApiKey(url,  body, context);
            else
                result = ServiceUtils.sendPostRequestWithToken(url, body, context);
            Log.d(TAG, "makePurchase result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 429){
                    result.put("code", ResultCodeEnum.MANY_REQUESTS);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 400 && result.has("error")){
                    if(result.getString("error").equals("CRM.EXCEPTIONS.NOTFOUNDEXCEPTION"))
                        result.put("code", ResultCodeEnum.NOTFOUNDEXCEPTION);
                    else if(result.getString("error").equals("CRM.EXCEPTIONS.CANNOTSPENDAMOUNTWALLETBALANCENOTENOUGHEXCEPTION"))
                        result.put("code", ResultCodeEnum.CANNOTSPENDAMOUNTWALLETBALANCENOTENOUGHEXCEPTION);
                    else result.put("code", ResultCodeEnum.BAD_REQUEST);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "makePurchaseOtp: contactOTP" + otp + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject getWallet(String walletId) throws Exception {
        Log.d(TAG, "getWallet: walletId=" + walletId );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_WALLET.replace("{id}", walletId);;
            result = ServiceUtils.sendGetRequestWithToken(url, context);
            Log.d(TAG, "getWallet result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 429){
                    result.put("code", ResultCodeEnum.MANY_REQUESTS);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getWallet: " + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject getLandingPageURL(boolean isMerchant) throws Exception {
        Log.d(TAG, "getLandingPageURL: orgID=" + "HIHIHIHI" );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_LANDINGPAGE_REGIS;
            if(isMerchant)
                result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            else
                result = ServiceUtils.sendGetRequestWithToken(url , context);
            Log.d(TAG, "getLandingPageURL result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 429){
                    result.put("code", ResultCodeEnum.MANY_REQUESTS);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getLandingPageURL: " + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject getPassPlan(boolean isMerchant) throws Exception {
        Log.d(TAG, "getPassPlan: ... =" );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_PASS_PLAN + "?printed=false&state=ACTIVE";
            if(isMerchant)
                result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            else
                result = ServiceUtils.sendGetRequestWithToken(url , context);
            Log.d(TAG, "getPasses result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getPassPlan , Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject getPassPlan(String passPlanId, boolean isMerchant) throws Exception {
        Log.d(TAG, "getPassPlan: passPlanId =" + passPlanId);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_PASS_PLAN + "/"+passPlanId;
            if(isMerchant)
                result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            else
                result = ServiceUtils.sendGetRequestWithToken(url , context);
            Log.d(TAG, "getPasses result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getPassPlan , Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject getPasses(String passCode, String state, boolean isMerchant) throws Exception {
        Log.d(TAG, "getPasses: passCode =" + passCode);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_CREATE_PASSES + "?pass_code=" + passCode+"&printed=true&state="+state;
            if(isMerchant)
                result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            else
                result = ServiceUtils.sendGetRequestWithToken(url , context);
            Log.d(TAG, "getPasses result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getPasses: passCode =" + passCode + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject updatePasses(String passId, String state, boolean isMerchant) throws Exception {
        Log.d(TAG, "updatePasses: passId =" + passId);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_CREATE_PASSES + "/" + passId;
            JSONObject body = new JSONObject();
            body.put("state", state);
            if(isMerchant)
                result = ServiceUtils.sendPutRequestWithApiKey(url, body, context);
            else
                result = ServiceUtils.sendPutRequestWithToken(url, body, context);

            Log.d(TAG, "updatePasses result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "updatePasses: passId =" + passId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject createPasses(String passPlanId, Double amount, String recipientName, String recipientEmail, String senderName, String msg, boolean isMerchant) throws Exception {
        Log.d(TAG, "Create Pass in pass plan id: =" + passPlanId );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_CREATE_PASSES;
            JSONObject body = new JSONObject();

            JSONObject recipient = new JSONObject();
            recipient.put("name", recipientName);
            recipient.put("medium_type", "EMAIL");
            recipient.put("medium_value", recipientEmail);
            JSONObject gift_sender = new JSONObject();
            gift_sender.put("name", senderName);
            gift_sender.put("message", msg);

            body.put("pass_plan_id", passPlanId);
            body.put("pass_value", amount);
            body.put("recipient", recipient);
            body.put("gift_sender", gift_sender);

            if(isMerchant)
                result = ServiceUtils.sendPostRequestWithApiKey(url, body, context);
            else
                result = ServiceUtils.sendPostRequestWithToken(url, body, context);
            Log.d(TAG, "CreatePasses result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 201){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 400){
                    result.put("code", ResultCodeEnum.BAD_REQUEST);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 429){
                    result.put("code", ResultCodeEnum.MANY_REQUESTS);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Create Pass in pass plan id: =" + passPlanId +", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject redeemPasses(String passCode, String value, boolean isMerchant) throws Exception {
        Log.d(TAG, "redeemPasses: passCode =" + passCode);
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_REDEEM_PASSES;
            JSONObject body = new JSONObject();
            body.put("code", passCode);
            body.put("value", value);
            if(isMerchant)
                result = ServiceUtils.sendPostRequestWithApiKey(url, body, context);
            else
                result = ServiceUtils.sendPostRequestWithToken(url, body, context);
            Log.d(TAG, "redeemPasses result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 400) {
                    result.put("code", ResultCodeEnum.BAD_REQUEST);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "redeemPasses: passCode =" + passCode + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject getOTPContact(String contactId, String method, Boolean isCallWithToken) throws Exception {
        Log.d(TAG, "getOTPContact: contactId=" + contactId );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_CONTACT_OTP.replace("{id}", contactId);
            JSONObject body = new JSONObject();
            body.put("method", method);
            if (isCallWithToken)
                result = ServiceUtils.sendPostRequestWithToken(url, body, context);
            else
                result = ServiceUtils.sendPostRequestWithApiKey(url, body, context);
            Log.d(TAG, "getOTPContact result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 429){
                    result.put("code", ResultCodeEnum.MANY_REQUESTS);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 400) {
                    result.put("code", ResultCodeEnum.BAD_REQUEST);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getOTPContact: " + contactId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject verifyOTP(String contactId, String auth_otp, String otp, Boolean isCallWithToken) throws Exception {
        Log.d(TAG, "verifyOTP: contactId=" + contactId );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_VERIFY_OTP.replace("{id}", contactId);
            JSONObject body = new JSONObject();
            body.put("auth_otp", auth_otp);
            body.put("code", otp);
            if(isCallWithToken){
                result = ServiceUtils.sendPostRequestWithToken(url, body, context);
            }
            else{
                result = ServiceUtils.sendPostRequestWithApiKey(url, body, context);
            }

            Log.d(TAG, "verifyOTP result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 204){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if (result.has("error") && result.getString("error").equals("CRM.EXCEPTIONS.INVALIDLOGINEXCEPTION")){
                    result.put("code", ResultCodeEnum.INVALIDLOGINEXCEPTION);
                }
                else if (result.has("error") && result.getString("error").equals("COM.CRM.EXCEPTIONS.CONTACTUNIQUELYIDENTIFYEXCEPTION")){
                    result.put("code", ResultCodeEnum.INVALIDLOGINEXCEPTION);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "verifyOTP: " + contactId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject createCommunication(String name, String channel, String language, String recipient, String subject, String content) throws Exception {
        Log.d(TAG, "Create communication in communication plan: =" + name );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_CREATE_COMMUNICATION;
            JSONObject body = new JSONObject();

            body.put("name", name);
            body.put("channel", channel);
            body.put("language", language);
            body.put("recipient", recipient);
            body.put("subject",subject);
            body.put("content",content);

            result = ServiceUtils.sendPostRequestWithToken(url, body, context);
            Log.d(TAG, "CreateCommunication result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 400){
                    result.put("code", ResultCodeEnum.BAD_REQUEST);
                }
                else if(result.has("responseCode") && result.getInt("responseCode") == 429){
                    result.put("code", ResultCodeEnum.MANY_REQUESTS);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Create communication in communication plan: =" + name  +", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject forgotPassword(String email) throws Exception {
        Log.d(TAG, "forgotPassword: email=" + email );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_FORGOT_PASSWORD;

            JSONObject body = new JSONObject();
            body.put("email", email);

            result = ServiceUtils.sendPostRequest(url, body, context);
            Log.d(TAG, "forgotPassword result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else  if(result.has("responseCode") && result.getInt("responseCode") == 400){
                    result.put("code", ResultCodeEnum.INVALIDLOGINEXCEPTION);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "forgotPassword: " + email + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject login(String email, String password) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_AUTHENTICATE;

            JSONObject body = new JSONObject();
            body.put("username", email);
            body.put("password", password);
            body.put("provider", "EMAIL");

            result = ServiceUtils.sendPostRequest(url, body, context);
            Log.d(TAG, "login result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else  if(result.has("responseCode") && result.getInt("responseCode") == 400){
                    result.put("code", ResultCodeEnum.INVALIDLOGINEXCEPTION);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "login: " + email + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject switchBusiness(String orgId) throws Exception {
        Log.d(TAG, "switch business: orgId=" + orgId );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_ORGANISATIONS;
            JSONObject body = new JSONObject();

            result = ServiceUtils.sendPostRequestWithToken(url  + "/switch" + "/"+ orgId, body, context);
            Log.d(TAG, "switchBusiness result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "switchBusiness: " + orgId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }


    public JSONObject getContactSetting(boolean isMerchant) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_CONTACT+"/settings";
            if(isMerchant)
                result = ServiceUtils.sendGetRequestWithApiKey(url, context);
            else
                result = ServiceUtils.sendGetRequestWithToken(url , context);
            AppUtils.console(context, TAG, "getContactSetting result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else  if(result.has("responseCode") && result.getInt("responseCode") == 400){
                    result.put("code", ResultCodeEnum.INVALIDLOGINEXCEPTION);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getContactSetting Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject getOrganisationConfig() throws Exception {
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_ORGANISATION_CONFIG;
            JSONObject body = new JSONObject();

            result = ServiceUtils.sendGetRequestWithToken(url , context);
            AppUtils.console(context, TAG, "getOrganisationConfig result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else  if(result.has("responseCode") && result.getInt("responseCode") == 400){
                    result.put("code", ResultCodeEnum.INVALIDLOGINEXCEPTION);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getOrganisationConfig Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }

    public JSONObject signUpOrganisation(String contactId) throws Exception {
        Log.d(TAG, "signUpOrganisation: contactId=" + contactId );
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_SIGN_UP_ORGANISATION.replace("{id}", contactId);
            JSONObject body = new JSONObject();
            body.put("organisation_id", Storage.getOrganisationSelected(context));
            body.put("action", "SIGNUP");
            body.put("service_acceptance", true);
            body.put("email_opt_out", false);
            body.put("sms_opt_out", false);
            body.put("consent_state", "ACCEPTED");
            result = ServiceUtils.sendPostRequestWithApiKey(url, body, context);
            Log.d(TAG, "signUpOrganisation result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "signUpOrganisation: " + contactId + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }


    //tạm thời truyền mỗi page sang
    public JSONObject getPurchases(int page, String outletId, String classification_id, String customerId,
                                   String fromDate, String toDate, boolean isMerchant) throws Exception {
        JSONObject result = new JSONObject();
        try {
            //RecipientResponse customer = getCustomer(cardNumber);
            String url = Constants.CrmConfig.SERVICE_URL_LOAD_PURCHASES.replace("{id}", outletId);
            if(customerId != null && !customerId.isEmpty())
                url = url+"&contact_id="+customerId;
            if(fromDate != null && !fromDate.isEmpty())
                url = url+"&date[gte]="+fromDate;
            if(toDate != null && !toDate.isEmpty())
                url = url+"&date[lte]="+toDate;
            if (page > 0){
                url = url+"&page="+page;
            }
            if(!classification_id.isEmpty()){
                url = url + "&classification_id=" + classification_id;
            }

            Log.d(TAG, "getPurchases url: " + url);
            JSONObject response = null;
            if(isMerchant)
                response = ServiceUtils.sendGetRequestWithApiKey(url, context);
            else
                response = ServiceUtils.sendGetRequestWithToken(url, context);
            Log.d(TAG, "getPurchases: " + response.toString());

            if(response != null) {
                ObjectMapper mapper = initMapper();
                PurchaseResponse purchaseResponse = mapper.readValue(response.toString(), PurchaseResponse.class);
                result.put("purchases", purchaseResponse);
                Log.d(TAG, "getPurchases: " + purchaseResponse.toString());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getPurchases:Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
    }
    public JSONObject getPurchase(String purchaseId) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String url = Constants.CrmConfig.SERVICE_URL_GET_PURCHASE.replace("{id}", purchaseId);
            JSONObject body = new JSONObject();

            result = ServiceUtils.sendGetRequestWithToken(url , context);
            AppUtils.console(context, TAG, "getPurchase result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("Code", ResultCodeEnum.OK);
                }
                else  if(result.has("responseCode") && result.getInt("responseCode") == 400){
                    result.put("Code", ResultCodeEnum.INVALIDLOGINEXCEPTION);
                }
            }
            else{
                result = new JSONObject();
                result.put("Code", ResultCodeEnum.UNKNOWN_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getContactSetting Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
    }
    public JSONObject refreshToken() throws Exception {
        Log.d(TAG, "getAppConfig: APP_ID =" + Constants.CrmConfig.APP_ID);
        JSONObject result = new JSONObject();
        try {
            result = ServiceUtils.sendRefreshToken(context);
            Log.d(TAG, "sendRefreshToken result: " + result);
            if(result != null) {
                if(result.has("responseCode") && result.getInt("responseCode") == 200){
                    result.put("code", ResultCodeEnum.OK);
                }
                else{
                    result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            else{
                result = new JSONObject();
                result.put("code", ResultCodeEnum.UNKNOWN_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "sendRefreshToken: " + Constants.CrmConfig.APP_ID + ", Exception: " + e.getMessage());
            if(e.getMessage().equals(ResultCodeEnum.NOT_CONNECT_API.toString())){
                throw new Exception(ResultCodeEnum.NOT_CONNECT_API.toString());
            }
            else if(e.getMessage().equals(ResultCodeEnum.REQUEST_TIMEOUT.toString())){
                throw new Exception(ResultCodeEnum.REQUEST_TIMEOUT.toString());
            }
            else {
                throw new Exception(ResultCodeEnum.UNKNOWN_ERROR.toString());
            }
        }
        return result;
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
}
