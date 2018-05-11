package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.login.LoginManager;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginScreenBinding;
    Context mContext;
    //    CallbackManager callbackManager;
//    private LoginManager mLoginManager;
//    private AccessTokenTracker mAccessTokenTracker;
    private boolean loggedin;
    String usernameStr, passwordStr, sessionIDStr, contatIDstr,
            whereTocomestr, orderIDStr, checkStr, paymentStatusstr,
            boardStr, standardStr, streamStr, locationStr, classNameStr,
            searchTypeStr, subjectStr, genderStr, frontloginStr, ratingLoginStr,searchfront;

    //    Use for Dialog
    Dialog forgotDialog;
    EditText edtEmail;
    TextView btnSendRegEmail, cancel_txt;
    String EmailIdStr, type, searchByStr,familylocationStr;

    //Use for Confirmation dialog
    Dialog confimDialog;
    TextView confirm_txt, session_student_txt, session_student_txt_view,
            session_name_txt, location_txt, duration_txt, time_txt,
            time_txt_view, session_fee_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;
        sessionIDStr = getIntent().getStringExtra("sessionID");
        whereTocomestr = getIntent().getStringExtra("withOR");
        searchByStr = getIntent().getStringExtra("SearchBy");
        boardStr = getIntent().getStringExtra("board");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        searchTypeStr = getIntent().getStringExtra("searchType");
        subjectStr = getIntent().getStringExtra("lessionName");
        genderStr = getIntent().getStringExtra("gender");
        frontloginStr = getIntent().getStringExtra("frontLogin");
        ratingLoginStr = getIntent().getStringExtra("ratingLogin");
        familylocationStr=getIntent().getStringExtra("location");
        searchfront=getIntent().getStringExtra("searchfront");


        if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
            checkUnmPwd();
        }

        // Init
//        setupInit();

        // Facebook
//        setupFacebook();


        setListner();
    }

    public void setListner() {
        loginScreenBinding.registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inregister = new Intent(mContext, RegistrationActivity.class);
                inregister.putExtra("sessionID", sessionIDStr);
                inregister.putExtra("frontLogin", frontloginStr);
                inregister.putExtra("SearchBy", searchByStr);
                inregister.putExtra("board", boardStr);
                inregister.putExtra("stream", streamStr);
                inregister.putExtra("standard", standardStr);
                inregister.putExtra("city", locationStr);
                inregister.putExtra("sessionName", classNameStr);
                inregister.putExtra("searchType", searchTypeStr);
                inregister.putExtra("lessionName", subjectStr);
                inregister.putExtra("gender", genderStr);
                inregister.putExtra("withOR", whereTocomestr);
                inregister.putExtra("ratingLogin", ratingLoginStr);
                inregister.putExtra("searchfront",searchfront);
                startActivity(inregister);
            }
        });
        loginScreenBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInsertedValue();
                if (!EmailIdStr.equalsIgnoreCase("") && Utils.isValidEmaillId(EmailIdStr) && !passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {
                    checkStr = "login";
//                    callCheckEmailIdApi();
                    callTeacherLoginApi();
                } else {
                    Utils.ping(mContext, "Invalid Email Address or Password.");
                }
            }
        });
        loginScreenBinding.facebookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (loggedin) {
//                    LoginManager.getInstance().logOut();
//                    loggedin = false;
//                } else {
//                    mAccessTokenTracker.startTracking();
//                    mLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_birthday"));
//                }
            }
        });
        loginScreenBinding.forgotPassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordDialog();
            }
        });
    }

    private void setupInit() {
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        loggedin = false;
    }

    private void setupFacebook() {
//        mLoginManager = LoginManager.getInstance();
//        callbackManager = CallbackManager.Factory.create();
//        mAccessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                // handle
//            }
//        };
//
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Utils.ping(mContext, "HEllo Adms");
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//        if (AccessToken.getCurrentAccessToken() != null) {
//            requestObjectUser(AccessToken.getCurrentAccessToken());
//        }
    }

//    private void requestObjectUser(final AccessToken accessToken) {
//        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                if (response.getError() != null) {
//                    // handle error
//                } else {
//                    Toast.makeText(LoginActivity.this, "Access Token: " + accessToken.getToken(), Toast.LENGTH_SHORT).show();
//
//                    loggedin = true;
//
////                    mFacebookText.setText("Log out");
//                    Log.d("response", response.toString());
//                    Log.d("First Name: ", "" + object.optString("first_name"));
//                    Log.d("Last Name: ", "" + object.optString("last_name"));
//                    Log.d("Email: ", "" + object.optString("email"));
//
//
//                    try {
//                        String userId = String.valueOf(object.get("id"));
//                        String profilePic = "https://graph.facebook.com/" + userId + "/picture?type=large";
//                        Log.d("Profile Pic", profilePic);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
////        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onBackPressed() {
        if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
            Intent isearchbyuser = new Intent(mContext, SearchByUser.class);
            startActivity(isearchbyuser);
        } else {
            Intent intent = new Intent(mContext, SessionName.class);
            intent.putExtra("SearchBy", searchByStr);
            intent.putExtra("sessionID", sessionIDStr);
            intent.putExtra("board", boardStr);
            intent.putExtra("stream", streamStr);
            intent.putExtra("standard", standardStr);
            intent.putExtra("city", locationStr);
            intent.putExtra("sessionName", classNameStr);
            intent.putExtra("searchType", searchTypeStr);
            intent.putExtra("lessionName", subjectStr);
            intent.putExtra("gender", genderStr);
            intent.putExtra("withOR", whereTocomestr);
            intent.putExtra("ratingLogin", ratingLoginStr);
            intent.putExtra("searchfront",searchfront);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
    }

    //Use for Login Teacher
    public void callTeacherLoginApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().getTeacherLogin(getTeacherLoginDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (teacherInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Invalid Email Address or Password.");
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        String[] splitCoachID = teacherInfoModel.getCoachID().split("\\,");
                        Utils.setPref(mContext, "coachID", splitCoachID[0]);
                        Utils.setPref(mContext, "coachTypeID", splitCoachID[1]);
                        Utils.setPref(mContext, "RegisterUserName", teacherInfoModel.getName());
                        Utils.setPref(mContext, "RegisterEmail", teacherInfoModel.getEmailID());
                        Utils.setPref(mContext, "LoginType", teacherInfoModel.getLoginType());
                        AppConfiguration.coachId = teacherInfoModel.getCoachID();
                        type = teacherInfoModel.getLoginType();
                        contatIDstr = splitCoachID[0];
                        if (teacherInfoModel.getLoginType().equalsIgnoreCase("Family")) {
                            if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
                                Intent iSearchByUser = new Intent(mContext, SearchByUser.class);
                                startActivity(iSearchByUser);
                            } else {
                                if (ratingLoginStr.equalsIgnoreCase("ratingLoginclass")) {
                                    Intent iSearchByUser = new Intent(mContext, ClassDeatilScreen.class);
                                    iSearchByUser.putExtra("frontLogin", "afterLogin");
                                    iSearchByUser.putExtra("sessionID", sessionIDStr);
                                    iSearchByUser.putExtra("SearchBy", searchByStr);
                                    iSearchByUser.putExtra("board", boardStr);
                                    iSearchByUser.putExtra("stream", streamStr);
                                    iSearchByUser.putExtra("standard", standardStr);
                                    iSearchByUser.putExtra("city", locationStr);
                                    iSearchByUser.putExtra("sessionName", classNameStr);
                                    iSearchByUser.putExtra("searchType", searchTypeStr);
                                    iSearchByUser.putExtra("lessionName", subjectStr);
                                    iSearchByUser.putExtra("gender", genderStr);
                                    iSearchByUser.putExtra("withOR", whereTocomestr);
                                    iSearchByUser.putExtra("searchfront",searchfront);
                                    startActivity(iSearchByUser);
                                } else if (ratingLoginStr.equalsIgnoreCase("ratingLoginSession")) {
                                    Intent iSearchByUser = new Intent(mContext, SessionName.class);
                                    iSearchByUser.putExtra("frontLogin", "afterLogin");
                                    iSearchByUser.putExtra("sessionID", sessionIDStr);
                                    iSearchByUser.putExtra("SearchBy", searchByStr);
                                    iSearchByUser.putExtra("board", boardStr);
                                    iSearchByUser.putExtra("stream", streamStr);
                                    iSearchByUser.putExtra("standard", standardStr);
                                    iSearchByUser.putExtra("city", locationStr);
                                    iSearchByUser.putExtra("sessionName", classNameStr);
                                    iSearchByUser.putExtra("searchType", searchTypeStr);
                                    iSearchByUser.putExtra("lessionName", subjectStr);
                                    iSearchByUser.putExtra("gender", genderStr);
                                    iSearchByUser.putExtra("withOR", whereTocomestr);
                                    iSearchByUser.putExtra("ratingLogin", "false");
                                    iSearchByUser.putExtra("searchfront",searchfront);
                                    startActivity(iSearchByUser);
                                } else {
//                                    ConformSessionDialog(); session_student_txt.setText(AppConfiguration.classteacherSessionName);
//        session_name_txt.setText(AppConfiguration.classSessionName);
//        location_txt.setText(AppConfiguration.classsessionLocation);
//        duration_txt.setText(AppConfiguration.classsessionDuration);
//        time_txt.setText(AppConfiguration.classsessionDate);
//        if (AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
//            session_fee_txt.setText("Free");
//        } else {
//            session_fee_txt.setText("₹ " + AppConfiguration.classsessionPrice);
//        }

                                    Intent iFamilyList = new Intent(mContext, FamilyListActivity.class);
                                    iFamilyList.putExtra("sessionfees", AppConfiguration.classsessionPrice);
                                    iFamilyList.putExtra("sessionName",classNameStr);
//                                    iFamilyList.putExtra("location",locationStr);
                                    iFamilyList.putExtra("duration", AppConfiguration.classsessionDuration);
                                    iFamilyList.putExtra("sessiondate", AppConfiguration.classsessionDate);
                                    iFamilyList.putExtra("froncontanct", "false");
                                    iFamilyList.putExtra("SearchBy",searchByStr);
                                    iFamilyList.putExtra("sessionID",sessionIDStr);
                                    iFamilyList.putExtra("board", boardStr);
                                    iFamilyList.putExtra("stream", streamStr);
                                    iFamilyList.putExtra("standard", standardStr);
                                    iFamilyList.putExtra("city", locationStr);
                                    iFamilyList.putExtra("searchType", searchTypeStr);
                                    iFamilyList.putExtra("lessionName", subjectStr);
                                    iFamilyList.putExtra("gender", genderStr);
                                    iFamilyList.putExtra("withOR", whereTocomestr);
                                    iFamilyList.putExtra("location",familylocationStr);
                                    iFamilyList.putExtra("searchfront",searchfront);
                                    startActivity(iFamilyList);
                                }
                            }

                        } else {
                            Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                            inLogin.putExtra("frontLogin", frontloginStr);
                            startActivity(inLogin);
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

    private Map<String, String> getTeacherLoginDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", EmailIdStr);
        map.put("Password", passwordStr);

        return map;
    }

    public void getInsertedValue() {
        EmailIdStr = loginScreenBinding.emailEdt.getText().toString();
        passwordStr = loginScreenBinding.passwordEdt.getText().toString();
        Utils.setPref(mContext, "Password", passwordStr);
    }

    public void checkUnmPwd() {
        if (!Utils.getPref(mContext, "coachID").equalsIgnoreCase("")) {
            Intent intentDashboard = new Intent(LoginActivity.this, DashBoardActivity.class);
            startActivity(intentDashboard);
            finish();
        }
    }

    public void forgotPasswordDialog() {
        forgotDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = forgotDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        forgotDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        forgotDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        forgotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotDialog.setCancelable(false);
        forgotDialog.setContentView(R.layout.forgot_password_dialog);

        cancel_txt = (TextView) forgotDialog.findViewById(R.id.cancel_txt);
        btnSendRegEmail = (TextView) forgotDialog.findViewById(R.id.btnSendRegEmail);
        edtEmail = (EditText) forgotDialog.findViewById(R.id.edtEmail);

        btnSendRegEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailIdStr = edtEmail.getText().toString();
                if (!EmailIdStr.equalsIgnoreCase("") && Utils.isValidEmaillId(EmailIdStr)) {
                    callCheckEmailIdApi();
                } else {
                    Utils.ping(mContext, "Invalid Email Address.");
                }


            }
        });
        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotDialog.dismiss();
            }
        });

        forgotDialog.show();

    }

    public void callCheckEmailIdApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().getCheckEmailAddress(getcheckEmailidDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (teacherInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        if (!checkStr.equalsIgnoreCase("login")) {
                            Utils.ping(mContext, "Please Enter Register Email Address.");
                        } else {

                        }
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
//                        if (!checkStr.equalsIgnoreCase("login")) {
                        callForgotPasswordApi();
//
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

    private Map<String, String> getcheckEmailidDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", EmailIdStr);

        return map;
    }

    //Use for ForgotPassword
    public void callForgotPasswordApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Forgot_Password(getForgotPasswordDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel forgotInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (forgotInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Please Enter Register Email Address.");
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        forgotDialog.dismiss();
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

    private Map<String, String> getForgotPasswordDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", EmailIdStr);

        return map;
    }

    //Use for Payment Conrfimation Dialog
    public void ConformSessionDialog() {
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(R.layout.confirm_session_dialog);


        session_student_txt_view = (TextView) confimDialog.findViewById(R.id.session_student_txt_view);
        session_student_txt = (TextView) confimDialog.findViewById(R.id.session_student_txt);
        session_name_txt = (TextView) confimDialog.findViewById(R.id.session_name_txt);
        location_txt = (TextView) confimDialog.findViewById(R.id.location_txt);
        duration_txt = (TextView) confimDialog.findViewById(R.id.duration_txt);
        time_txt = (TextView) confimDialog.findViewById(R.id.time_txt);
        time_txt_view = (TextView) confimDialog.findViewById(R.id.time_txt_view);
        session_fee_txt = (TextView) confimDialog.findViewById(R.id.session_fee_txt);
        confirm_txt = (TextView) confimDialog.findViewById(R.id.confirm_txt);
        cancel_txt = (TextView) confimDialog.findViewById(R.id.cancel_txt);
        session_student_txt_view.setText("TEACHER NAME");

        session_student_txt.setText(AppConfiguration.classteacherSessionName);
        session_name_txt.setText(AppConfiguration.classSessionName);
        location_txt.setText(AppConfiguration.classsessionLocation);
        duration_txt.setText(AppConfiguration.classsessionDuration);
        time_txt.setText(AppConfiguration.classsessionDate);
        if (AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
            session_fee_txt.setText("Free");
        } else {
            session_fee_txt.setText("₹ " + AppConfiguration.classsessionPrice);
        }


        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
                    callpaymentRequestApi();
                } else {
                    paymentStatusstr = "1";
                    callSessionConfirmationApi();
                }
                confimDialog.dismiss();
            }
        });


        confimDialog.show();
    }

    //Use for paymentRequest
    public void callpaymentRequestApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_GeneratePaymentRequest(getpaymentRequestdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentRequestModel, Response response) {
                    Utils.dismissDialog();
                    if (paymentRequestModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("True")) {
                        orderIDStr = paymentRequestModel.getOrderID();
                        Intent ipayment = new Intent(mContext, PaymentActivity.class);
                        ipayment.putExtra("orderID", orderIDStr);
                        ipayment.putExtra("amount", AppConfiguration.classsessionPrice);
                        ipayment.putExtra("mode", "TEST");
                        ipayment.putExtra("username", Utils.getPref(mContext, "RegisterUserName"));
                        ipayment.putExtra("sessionID", sessionIDStr);
                        ipayment.putExtra("contactID", Utils.getPref(mContext, "coachID"));
                        ipayment.putExtra("type", Utils.getPref(mContext, "LoginType"));
                        startActivity(ipayment);

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

    private Map<String, String> getpaymentRequestdetail() {

        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);
        map.put("SessionID", sessionIDStr);
        map.put("Amount", AppConfiguration.classsessionPrice);

        return map;
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
                        Utils.ping(mContext, "Login Succesfully");
                        confimDialog.dismiss();
                        Intent isearchBYuser = new Intent(mContext, SearchByUser.class);
                        startActivity(isearchBYuser);

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
        map.put("PaymentStatus", paymentStatusstr);
        return map;
    }
}
