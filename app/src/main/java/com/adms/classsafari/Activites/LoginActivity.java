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
    String usernameStr, passwordStr;

    //    Use for Dialog
    Dialog forgotDialog;
    EditText edtEmail;
    TextView btnSendRegEmail, cancel_txt;
    String EmailIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;

        checkUnmPwd();

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
                startActivity(inregister);
            }
        });
        loginScreenBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInsertedValue();
                if (!usernameStr.equalsIgnoreCase("") && Utils.isValidEmaillId(usernameStr) && !passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
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
                        AppConfiguration.coachId = teacherInfoModel.getCoachID();
                        Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                        startActivity(inLogin);
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
        map.put("EmailAddress", usernameStr);
        map.put("Password", passwordStr);

        return map;
    }

    public void getInsertedValue() {
        usernameStr = loginScreenBinding.emailEdt.getText().toString();
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
                        Utils.ping(mContext, "Please Enter Register Email Address.");
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        callForgotPasswordApi();
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
}
