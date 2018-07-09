package com.adms.classsafari.Fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.adms.classsafari.Activites.DashBoardActivity;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.FragmentPaymentBinding;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class PaymentFragment extends Fragment {

    FragmentPaymentBinding paymentBinding;
    private View rootView;
    private Context mContext;
    private Bundle extras = null;
    private static final String TAG = "TNPRequestDebugTag";
    String sessionIDStr, contatIDstr, type,paymentStatusstr;

    public PaymentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        paymentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);

        rootView = paymentBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(12, "false");
        sessionIDStr = getArguments().getString("sessionID");
        contatIDstr = getArguments().getString("contactID");
        type = getArguments().getString("type");
        init();

        return rootView;
    }

    public void init() {
         /*
        * IMPORTANT: For this to work your return_page_android.php should have following code.
        * This php page should be hosted on your server and return_url is set to http://yourdomain.com/return_page_android.php
        *
        <script type="text/javascript">
            Android.getPaymentResponse('<?php echo isset($_REQUEST)?json_encode($_REQUEST):'{}' ?>');
        </script>
        *
        */

//        String salt = "531553f8d6b906aa3342948a3c535ca301de9d5d"; // put your salt
//        String api_key = "535ee616-a161-4e16-88ed-a338582e841a";  // put your api_key
        String return_url = "https://biz.traknpay.in/tnp/return_page_android.php";
        String mode = "";
        String order_id = "";
        String amount = "";
        String currency = "INR";
        String description = "";
        String name = getArguments().getString("username");//AppConfiguration.CustomerDetail.get("CustomerName")
        String email = "saralpayonline@gmail.com";//saralpayonline@gmail.com
        String phone = "7575809733";//7575809733
        String address_line_1 = "-";
        String address_line_2 = "-";
        String city = "Ahmedabad";
        String state = "Gujarat";
        String zip_code = "380015";
        String country = "IND";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String card_number = "";
        String card_name = "";
        String card_exp_month = "";
        String card_exp_year = "";
        String show_saved_cards = "n";

        mode = "TEST";//mode
        amount = "10";//amount
        order_id = "A0008180309052207";
        description = "description";


        order_id = getArguments().getString("orderID");
        amount = getArguments().getString("amount");
        mode = getArguments().getString("mode");
        //comment from megha
        // Getting these values from Main activity
//        extras = getIntent().getExtras();
//        if (extras != null) {
//            mode = extras.getString("TEST");//mode
//            amount = extras.getString("10");//amount
//            order_id = extras.getString("A0008180309052298");
//            description = extras.getString("description");

//            if(extras.containsKey("CardDetails")) {
//
//                String cardDetails = extras.getString("CardDetails");
//                String[] details = cardDetails.split("\\-");//card.CardNumber + "-" + card.CardHolderName + " - " + card.ValidToDate
//                String expiryDate = details[2].trim();
//                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, expiryDate.length());
//
//                card_number = details[0].trim();
//                //card_name = details[1].toString().replace("/", "").trim();22-12-2016 old
//                card_name = details[1].toString(). replaceAll("[;\\/:*?\"<>|&']","").trim();//22-12-2016 navin
//
//                card_exp_month = expiryDate.split("\\/")[0];
//                card_exp_year = expiryDate.split("\\/")[1];
//            }

//        }

//        'address_line_1', 'address_line_2', 'amount', 'api_key', 'city', 'country', 'currency', 'description', 'email', 'mode', 'name', 'order_id', 'phone', 'return_url', 'state', 'udf1', 'udf2', 'udf3', 'udf4', 'udf5', 'zip_code'

        Map<String, String> mapHashData = new HashMap<String, String>();
        mapHashData.put("address_line_1", address_line_1);
        mapHashData.put("address_line_2", address_line_2);
        mapHashData.put("amount", amount);
        mapHashData.put("api_key", "535ee616-a161-4e16-88ed-a338582e841a");//AppConfiguration.api_key
        mapHashData.put("city", city);
        mapHashData.put("country", country);
        mapHashData.put("currency", currency);
        mapHashData.put("description", description);
        mapHashData.put("email", email);
        mapHashData.put("mode", mode);
        mapHashData.put("name", name);
        mapHashData.put("order_id", order_id);
        mapHashData.put("phone", phone);
        mapHashData.put("return_url", return_url);
        mapHashData.put("state", state);
        mapHashData.put("udf1", udf1);
        mapHashData.put("udf2", udf2);
        mapHashData.put("udf3", udf3);
        mapHashData.put("udf4", udf4);
        mapHashData.put("udf5", udf5);
        mapHashData.put("zip_code", zip_code);
        mapHashData.put("show_saved_cards", show_saved_cards);

        Map<String, String> mapPostData = new HashMap<String, String>();
        mapPostData.put("address_line_1", address_line_1);
        mapPostData.put("address_line_2", address_line_2);
        mapPostData.put("amount", amount);
        mapPostData.put("api_key", "535ee616-a161-4e16-88ed-a338582e841a");//AppConfiguration.api_key

//        if(extras.containsKey("CardDetails")) {
//            mapPostData.put("card_exp_month", card_exp_month);
//            mapPostData.put("card_exp_year", "20" + card_exp_year);
//            mapPostData.put("card_name", card_name);
//            mapPostData.put("card_number", card_number);
//        }

        mapPostData.put("city", city);
        mapPostData.put("country", country);
        mapPostData.put("currency", currency);
        mapPostData.put("description", description);
        mapPostData.put("email", email);
        mapPostData.put("mode", mode);
        mapPostData.put("name", name);
        mapPostData.put("order_id", order_id);
        mapPostData.put("phone", phone);
        mapPostData.put("return_url", return_url);
        mapPostData.put("state", state);
        mapPostData.put("udf1", udf1);
        mapPostData.put("udf2", udf2);
        mapPostData.put("udf3", udf3);
        mapPostData.put("udf4", udf4);
        mapPostData.put("udf5", udf5);
        mapPostData.put("zip_code", zip_code);
        mapPostData.put("show_saved_cards", show_saved_cards);

        String hashData = "531553f8d6b906aa3342948a3c535ca301de9d5d";//AppConfiguration.secret_key;
        String postData = "";

        for (String key : new TreeSet<String>(mapHashData.keySet())) {
            if (!mapHashData.get(key).equals("")) {
                hashData = hashData + "|" + mapHashData.get(key);

            }
        }

        // Sort the map by key and create the hashData and postData
        for (String key : new TreeSet<String>(mapPostData.keySet())) {
            if (!mapPostData.get(key).equals("")) {

                postData = postData + key + "=" + mapPostData.get(key) + "&";
            }
        }

        // Generate the hash key using hashdata and append the has to postData query string
        String hash = generateSha512Hash(hashData).toUpperCase();
        postData = postData + "hash=" + hash;

        Log.d(TAG, "HashData: " + hashData);
//        Log.d(TAG, "Hash: " + hash);
        Log.d(TAG, "PostData: " + postData);


        WebSettings webSettings = paymentBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDatabasePath("/data/data/" + getPackageName() + "/databases/");
//        webSettings.setAppCacheMaxSize(1024*1024*8);
//        webSettings.setAppCachePath("/data/data/" + getPackageName() + "/cache/");
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setLightTouchEnabled(true);
        webSettings.setBuiltInZoomControls(true);
//        webView.setWebChromeClient(new WebChromeClient());
        paymentBinding.webView.setWebViewClient(new WebViewClient());
        paymentBinding.webView.postUrl("https://biz.traknpay.in/v1/paymentrequest", postData.getBytes());
        paymentBinding.webView.addJavascriptInterface(new MyJavaScriptInterface(mContext), "Android");

    }

    /**
     * Generates the SHA-512 hash (same as PHP) for the given string
     *
     * @param toHash string to be hashed
     * @return return hashed string
     */
    public String generateSha512Hash(String toHash) {
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(toHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    /**
     * Converts the given byte[] to a hex string.
     *
     * @param raw the byte[] to convert
     * @return the string the given byte[] represents
     */
    private String convertToHex(byte[] raw) {
        StringBuilder sb = new StringBuilder();
        for (byte aRaw : raw) {
            sb.append(Integer.toString((aRaw & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Interface to bind Javascript from WebView with Android
     */
    public class MyJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void getPaymentResponse(String jsonResponse) {
            try {
                JSONObject resposeData = new JSONObject(jsonResponse);
                Log.d(TAG, "ResponseJson: " + resposeData.toString());
                if (resposeData.getString("response_code").equalsIgnoreCase("0")) {
                    paymentStatusstr = "1";
                } else {
                    paymentStatusstr = "0";
                }
                callSessionConfirmationApi();
                Fragment fragment = new PaymentSucessFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("sessionId",sessionIDStr);
                args.putString("transactionId", resposeData.getString("transaction_id"));
                args.putString("responseCode", resposeData.getString("response_code"));
                args.putString("amount", resposeData.getString("amount"));
                args.putString("description", resposeData.getString("description"));
                args.putString("order_id", resposeData.getString("order_id"));
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Use for Family and Child Session Confirmation
    public void callSessionConfirmationApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Session_ContactEnrollment(getSessionConfirmationdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel sessionconfirmationInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionconfirmationInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        if (type.equalsIgnoreCase("Child")) {
                            Utils.ping(mContext, "Child confirmation successfully");
                        } else {
                            Utils.ping(mContext, "Family confirmation successfully");
                        }


                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Utils.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Utils.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getSessionConfirmationdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        map.put("ContactID", contatIDstr);
        map.put("PaymentStatus",paymentStatusstr);
        return map;
    }
}