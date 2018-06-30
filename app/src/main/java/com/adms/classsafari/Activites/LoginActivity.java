package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.AppConstant.Validation;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityLoginBinding;
import com.adms.classsafari.databinding.ConfirmSessionDialogBinding;
import com.adms.classsafari.databinding.ForgotPasswordDialogBinding;
import com.adms.classsafari.databinding.OptionDialogBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.login.LoginManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    ActivityLoginBinding loginScreenBinding;
    ForgotPasswordDialogBinding forgotPasswordDialogBinding;
    ConfirmSessionDialogBinding confirmSessionDialogBinding;
    OptionDialogBinding optionDialogBinding;

    Context mContext;
    String usernameStr, passwordStr, sessionIDStr, contatIDstr,
            checkStr,
            boardStr, standardStr, streamStr, locationStr, classNameStr,
            subjectStr, genderStr, frontloginStr,
            ratingLoginStr, searchfront, sessionType, durationStr,
            sessionDateStr, RegionName, backStr, SearchPlaystudy;
    //    Use for Dialog
    Dialog forgotDialog;
    String EmailIdStr, type, familylocationStr, familysessionStudentStr, firsttimesearch;
    //DashboardDialog
    Dialog optionDialog;
    private boolean loggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;
        sessionIDStr = getIntent().getStringExtra("sessionID");
        boardStr = getIntent().getStringExtra("board");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        subjectStr = getIntent().getStringExtra("lessionName");
        genderStr = getIntent().getStringExtra("gender");
        frontloginStr = getIntent().getStringExtra("frontLogin");
        ratingLoginStr = getIntent().getStringExtra("ratingLogin");
        familylocationStr = getIntent().getStringExtra("location");
        searchfront = getIntent().getStringExtra("searchfront");
        sessionType = getIntent().getStringExtra("sessionType");
        sessionDateStr = getIntent().getStringExtra("sessiondate");
        durationStr = getIntent().getStringExtra("duration");
        familysessionStudentStr = getIntent().getStringExtra("sessionStudent");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        RegionName = getIntent().getStringExtra("RegionName");
        backStr = getIntent().getStringExtra("back");
        SearchPlaystudy = getIntent().getStringExtra("SearchPlaystudy");
        if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
            checkUnmPwd();
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "font/TitilliumWeb-Regular.ttf");

        loginScreenBinding.emailEdt.setTypeface(custom_font);
        loginScreenBinding.passwordEdt.setTypeface(custom_font);
        setListner();
    }

    public void setListner() {
        loginScreenBinding.registerTxt.setOnClickListener(this);
        loginScreenBinding.passwordEdt.setOnEditorActionListener(this);
        loginScreenBinding.loginBtn.setOnClickListener(this);
        loginScreenBinding.facebookImg.setOnClickListener(this);
        loginScreenBinding.forgotPassTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_txt:
                Intent inregister = new Intent(mContext, RegistrationActivity.class);
                inregister.putExtra("sessionID", sessionIDStr);
                inregister.putExtra("frontLogin", frontloginStr);
//                inregister.putExtra("SearchBy", searchByStr);
                inregister.putExtra("board", boardStr);
                inregister.putExtra("stream", streamStr);
                inregister.putExtra("standard", standardStr);
                inregister.putExtra("city", locationStr);
                inregister.putExtra("sessionName", classNameStr);
                inregister.putExtra("lessionName", subjectStr);
                inregister.putExtra("gender", genderStr);
//                inregister.putExtra("withOR", whereTocomestr);
                inregister.putExtra("SearchPlaystudy", SearchPlaystudy);
                inregister.putExtra("ratingLogin", ratingLoginStr);
                inregister.putExtra("searchfront", searchfront);
                inregister.putExtra("sessionType", sessionType);
                inregister.putExtra("duration", durationStr);
                inregister.putExtra("sessiondate", sessionDateStr);
                inregister.putExtra("sessionStudent", familysessionStudentStr);
                inregister.putExtra("firsttimesearch", firsttimesearch);
                inregister.putExtra("RegionName", RegionName);
                inregister.putExtra("back", backStr);
                startActivity(inregister);
                break;
            case R.id.login_btn:
                getInsertedValue();
                if (Validation.checkEmail(EmailIdStr) && Validation.checkPassword(passwordStr)) {
                    checkStr = "login";
                    callTeacherLoginApi();
                } else {
                    Utils.ping(mContext, "Invalid Email Address or Password");
                }
                break;
            case R.id.facebook_img:
                break;
            case R.id.forgot_pass_txt:
                forgotPasswordDialog();
                break;

        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.password_edt:
                if (i == EditorInfo.IME_ACTION_DONE) {
                    getInsertedValue();
                    if (Validation.checkEmail(EmailIdStr) && Validation.checkPassword(passwordStr)) {
                        checkStr = "login";
                        callTeacherLoginApi();
                    } else {
                        Utils.ping(mContext, "Invalid Email Address or Password.");
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
            Intent isearchbyuser = new Intent(mContext, SearchByUser.class);
            startActivity(isearchbyuser);
        } else {
            Intent intent = new Intent(mContext, SessionName.class);
//            intent.putExtra("SearchBy", searchByStr);
            intent.putExtra("sessionID", sessionIDStr);
            intent.putExtra("board", boardStr);
            intent.putExtra("stream", streamStr);
            intent.putExtra("standard", standardStr);
            intent.putExtra("city", locationStr);
            intent.putExtra("sessionName", classNameStr);
            intent.putExtra("lessionName", subjectStr);
            intent.putExtra("gender", genderStr);
//            intent.putExtra("withOR", whereTocomestr);
            intent.putExtra("ratingLogin", ratingLoginStr);
            intent.putExtra("searchfront", searchfront);
            intent.putExtra("sessionType", sessionType);
            intent.putExtra("duration", durationStr);
            intent.putExtra("sessiondate", sessionDateStr);
            intent.putExtra("sessionStudent", familysessionStudentStr);
            intent.putExtra("firsttimesearch", firsttimesearch);
            intent.putExtra("RegionName", RegionName);
            intent.putExtra("back", backStr);
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
//                                Intent iSearchByUser = new Intent(mContext, SearchByUser.class);
//                                startActivity(iSearchByUser);
                                Intent iSearchByUser = new Intent(mContext, MySession.class);
                                startActivity(iSearchByUser);

                            } else {
                                if (ratingLoginStr.equalsIgnoreCase("ratingLoginclass")) {
                                    Intent iSearchByUser = new Intent(mContext, ClassDeatilScreen.class);
                                    iSearchByUser.putExtra("frontLogin", "afterLogin");
                                    iSearchByUser.putExtra("sessionID", sessionIDStr);
//                                    iSearchByUser.putExtra("SearchBy", searchByStr);
                                    iSearchByUser.putExtra("board", boardStr);
                                    iSearchByUser.putExtra("stream", streamStr);
                                    iSearchByUser.putExtra("standard", standardStr);
                                    iSearchByUser.putExtra("city", locationStr);
                                    iSearchByUser.putExtra("sessionName", classNameStr);

                                    iSearchByUser.putExtra("lessionName", subjectStr);
                                    iSearchByUser.putExtra("gender", genderStr);
//                                    iSearchByUser.putExtra("withOR", whereTocomestr);
                                    iSearchByUser.putExtra("searchfront", searchfront);
                                    iSearchByUser.putExtra("sessionType", sessionType);
                                    iSearchByUser.putExtra("firsttimesearch", firsttimesearch);
                                    iSearchByUser.putExtra("RegionName", RegionName);
                                    startActivity(iSearchByUser);
                                } else if (ratingLoginStr.equalsIgnoreCase("ratingLoginSession")) {
                                    Intent iSearchByUser = new Intent(mContext, SessionName.class);
                                    iSearchByUser.putExtra("frontLogin", "afterLogin");
                                    iSearchByUser.putExtra("sessionID", sessionIDStr);
//                                    iSearchByUser.putExtra("SearchBy", searchByStr);
                                    iSearchByUser.putExtra("board", boardStr);
                                    iSearchByUser.putExtra("stream", streamStr);
                                    iSearchByUser.putExtra("standard", standardStr);
                                    iSearchByUser.putExtra("city", locationStr);
                                    iSearchByUser.putExtra("sessionName", classNameStr);

                                    iSearchByUser.putExtra("lessionName", subjectStr);
                                    iSearchByUser.putExtra("gender", genderStr);
//                                    iSearchByUser.putExtra("withOR", whereTocomestr);
                                    iSearchByUser.putExtra("ratingLogin", "false");
                                    iSearchByUser.putExtra("searchfront", searchfront);
                                    iSearchByUser.putExtra("sessionType", sessionType);
                                    iSearchByUser.putExtra("firsttimesearch", firsttimesearch);
                                    iSearchByUser.putExtra("RegionName", RegionName);
                                    startActivity(iSearchByUser);
                                } else {
                                    Intent iFamilyList = new Intent(mContext, FamilyListActivity.class);
                                    iFamilyList.putExtra("sessionfees", AppConfiguration.classsessionPrice);
                                    iFamilyList.putExtra("sessionName", classNameStr);
                                    iFamilyList.putExtra("duration", AppConfiguration.classsessionDuration);
                                    iFamilyList.putExtra("sessiondate", AppConfiguration.classsessionDate);
                                    iFamilyList.putExtra("froncontanct", "false");
//                                    iFamilyList.putExtra("SearchBy", searchByStr);
                                    iFamilyList.putExtra("sessionID", sessionIDStr);
                                    iFamilyList.putExtra("board", boardStr);
                                    iFamilyList.putExtra("stream", streamStr);
                                    iFamilyList.putExtra("standard", standardStr);
                                    iFamilyList.putExtra("city", locationStr);

                                    iFamilyList.putExtra("lessionName", subjectStr);
                                    iFamilyList.putExtra("gender", genderStr);
//                                    iFamilyList.putExtra("withOR", whereTocomestr);
                                    iFamilyList.putExtra("location", familylocationStr);
                                    iFamilyList.putExtra("searchfront", searchfront);
                                    iFamilyList.putExtra("sessionType", sessionType);
                                    iFamilyList.putExtra("sessionStudent", familysessionStudentStr);
                                    iFamilyList.putExtra("firsttimesearch", firsttimesearch);
                                    iFamilyList.putExtra("RegionName", RegionName);
                                    iFamilyList.putExtra("back", backStr);
                                    startActivity(iFamilyList);
                                }
                            }

                        } else {
                            selectOptionDialog();
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
            Utils.ping(mContext, getResources().getString(R.string.internet_connection_error));
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
        forgotPasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.forgot_password_dialog, (ViewGroup) loginScreenBinding.getRoot(), false);

        forgotDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = forgotDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        forgotDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        forgotDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        forgotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotDialog.setCancelable(false);
        forgotDialog.setContentView(forgotPasswordDialogBinding.getRoot());


        forgotPasswordDialogBinding.btnSendRegEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailIdStr = forgotPasswordDialogBinding.edtEmail.getText().toString();
                if (!EmailIdStr.equalsIgnoreCase("") && Utils.isValidEmaillId(EmailIdStr)) {
                    callCheckEmailIdApi();
                } else {
                    Utils.ping(mContext, "Invalid Email Address.");
                }


            }
        });
        forgotPasswordDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
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


    public void selectOptionDialog() {
        optionDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.option_dialog, (ViewGroup) loginScreenBinding.getRoot(), false);

        optionDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = optionDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        optionDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        optionDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        optionDialog.setCancelable(false);
        optionDialog.setContentView(optionDialogBinding.getRoot());
        String[] userName = Utils.getPref(mContext, "RegisterUserName").split("\\s+");

        optionDialogBinding.titleTxt.setText(Html.fromHtml("Hi " + "<u><b>" + userName[0] + "</u></b>"));
        optionDialogBinding.addClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                inLogin.putExtra("frontLogin", frontloginStr);
                inLogin.putExtra("position","1");
                startActivity(inLogin);
                optionDialog.dismiss();
            }
        });
        optionDialogBinding.viewClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                inLogin.putExtra("frontLogin", frontloginStr);
                inLogin.putExtra("position","0");
                startActivity(inLogin);
                optionDialog.dismiss();
            }
        });
        optionDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginScreenBinding.emailEdt.setText("");
                loginScreenBinding.passwordEdt.setText("");
                Utils.setPref(mContext, "coachID", "");
                Utils.setPref(mContext, "coachTypeID","");
                Utils.setPref(mContext, "RegisterUserName","");
                Utils.setPref(mContext, "RegisterEmail","");
                Utils.setPref(mContext, "LoginType","");
                optionDialog.dismiss();
            }
        });
        optionDialog.show();

    }

}
