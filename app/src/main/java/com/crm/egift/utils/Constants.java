package com.crm.egift.utils;

public class Constants {
    public static final int TIMEOUT = 5000;

    public static class CrmConfig {
        public static String TOKEN = "";
        public static String REFRESH_TOKEN = "";

        public static String APP_ID = "com.crm.egift";
        public static String SERVICE_URL = "https://sandbox.crm.com/backoffice/v2/";
        public static final String API_KEY = ""; //sandbox

        public static final String SERVICE_URL_GET_WALLET = "wallets/{id}";
        public static final String SERVICE_URL_GET_PASS_PLAN = "pass_plans";
        public static final String SERVICE_URL_CREATE_PASSES = "passes";
        public static final String SERVICE_URL_REDEEM_PASSES = "passes/redeem";
        public static final String SERVICE_URL_GET_APP_CONFIG = "applications";
        public static final String SERVICE_URL_FORGOT_PASSWORD = "users/forgot_password";
        public static final String SERVICE_URL_AUTHENTICATE = "users/authenticate";
        public static final String SERVICE_URL_ORGANISATIONS = "organisations";
        public static final String SERVICE_URL_ORGANISATION_CONFIG = "applications?platform_app_id=" + APP_ID;
        public static final String SERVICE_URL_GET_REWARD_OFFERS = "reward_offers";
        public static final String SERVICE_URL_GET_ACTIVE_REWARD_OFFERS = "reward_offers?state=ACTIVE";
        public static final String SERVICE_URL_GET_OFFER_DETAIL = "reward_offers/{id}";
        public static final String SERVICE_URL_CHANGE_OFFER_STATE = "reward_offers/{id}/life_cycle_state";
        public static final String SERVICE_URL_CHANGE_OFFER_AWARDS = "reward_offers/{id}/awards";
        public static final String SERVICE_URL_CONTACT = "contacts";

        public static final String SERVICE_URL_LOAD_PURCHASES = "purchases?organisations={id}";//What is the API to call ???
        public static final String SERVICE_URL_LOAD_TOPUPS = "journals?entity=WALLET&event=TOP_UP";
        public static final String SERVICE_URL_GET_ORGANISATION = "organisations/{id}";
        public static final String SERVICE_URL_GET_CUSTOMER = "contacts";
        public static final String SERVICE_URL_MAKE_PURCHASE = "purchases";
        public static final String SERVICE_URL_GET_PURCHASE = "purchases/{id}";
        public static final String SERVICE_URL_GET_CONTACT_OTP = "contacts/{id}/otp";
        public static final String SERVICE_URL_VERIFY_OTP = "contacts/{id}/validate_otp";
        public static final String SERVICE_URL_SIGN_UP_ORGANISATION = "contacts/{id}/organisations";
        public static final String SERVICE_URL_GET_LANDINGPAGE_REGIS = "landing_pages?search_value=New%20Contact%20Registration%20without";
        public static final String SERVICE_URL_REFRESH_TOKEN = "users/refresh";
        public static final String SERVICE_URL_CREATE_COMMUNICATION = "communications";
    }
}
