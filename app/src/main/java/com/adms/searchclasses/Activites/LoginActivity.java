package com.adms.searchclasses.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.AppConstant.Validation;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.ActivityLoginBinding;
import com.adms.searchclasses.databinding.ForgotPasswordDialogBinding;
import com.adms.searchclasses.databinding.OptionDialogBinding;

import java.util.ArrayList;
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
    OptionDialogBinding optionDialogBinding;

    Context mContext;
    String usernameStr, passwordStr, sessionIDStr, contatIDstr, commentStr, ratingValueStr,
            checkStr,
            boardStr, standardStr, streamStr, locationStr, classNameStr,
            subjectStr, genderStr, frontloginStr,
            ratingLoginStr, searchfront, sessionType, durationStr,
            sessionDateStr, RegionName, backStr, SearchPlaystudy, TeacherName;
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
        TeacherName = getIntent().getStringExtra("TeacherName");
        if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
            checkUnmPwd();
        }

        setListner();
    }

    public void setListner() {
        loginScreenBinding.registerTxt.setOnClickListener(this);
        loginScreenBinding.passwordEdt.setOnEditorActionListener(this);
        loginScreenBinding.loginBtn.setOnClickListener(this);
        loginScreenBinding.facebookImg.setOnClickListener(this);
        loginScreenBinding.forgotPassTxt.setOnClickListener(this);
        loginScreenBinding.homeTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_txt:
                Intent intent = new Intent(mContext, SearchByUser.class);
                startActivity(intent);
                break;
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
                inregister.putExtra("TeacherName", TeacherName);
                inregister.putExtra("back", backStr);
                startActivity(inregister);
                break;
            case R.id.login_btn:
                getInsertedValue();
                if (Validation.checkEmail(EmailIdStr) && Validation.checkPassword(passwordStr)) {
                    checkStr = "login";
                    callTeacherLoginApi();
                } else {
                    Utils.ping(mContext, "Invalid email address or password");
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
                        Utils.ping(mContext, "Invalid email address or password");
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
            finish();
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
            intent.putExtra("TeacherName", TeacherName);
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
                        Utils.ping(mContext, "Invalid email address or password.");
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        String[] splitCoachID = teacherInfoModel.getCoachID().split("\\,");


                        Utils.setPref(mContext, "coachID", splitCoachID[0]);
                        Utils.setPref(mContext, "coachTypeID", splitCoachID[1]);
                        Utils.setPref(mContext, "RegisterUserName", teacherInfoModel.getName());
                        Utils.setPref(mContext, "RegisterEmail", teacherInfoModel.getEmailID());
                        Utils.setPref(mContext, "LoginType", teacherInfoModel.getLoginType());
                        Utils.setPref(mContext, "ClassName", teacherInfoModel.getClassName());
                        AppConfiguration.coachId = teacherInfoModel.getCoachID();
                        type = teacherInfoModel.getLoginType();
                        contatIDstr = splitCoachID[0];
                        if (teacherInfoModel.getLoginType().equalsIgnoreCase("Family")) {
                            if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
//                                Intent iSearchByUser = new Intent(mContext, SearchByUser.class);
//                                startActivity(iSearchByUser);
                                Intent iSearchByUser = new Intent(mContext, MySession.class);
                                startActivity(iSearchByUser);
                                finish();
                            } else {
                                if (ratingLoginStr.equalsIgnoreCase("ratingLoginclass")) {
//                                    Intent iSearchByUser = new Intent(mContext, ClassDeatilScreen.class);
//                                    iSearchByUser.putExtra("frontLogin", "afterLogin");
//                                    iSearchByUser.putExtra("sessionID", sessionIDStr);
////                                    iSearchByUser.putExtra("SearchBy", searchByStr);
//                                    iSearchByUser.putExtra("board", boardStr);
//                                    iSearchByUser.putExtra("stream", streamStr);
//                                    iSearchByUser.putExtra("standard", standardStr);
//                                    iSearchByUser.putExtra("city", locationStr);
//                                    iSearchByUser.putExtra("sessionName", classNameStr);
//
//                                    iSearchByUser.putExtra("lessionName", subjectStr);
//                                    iSearchByUser.putExtra("gender", genderStr);
////                                    iSearchByUser.putExtra("withOR", whereTocomestr);
//                                    iSearchByUser.putExtra("searchfront", searchfront);
//                                    iSearchByUser.putExtra("sessionType", sessionType);
//                                    iSearchByUser.putExtra("firsttimesearch", firsttimesearch);
//                                    iSearchByUser.putExtra("RegionName", RegionName);
//                                    iSearchByUser.putExtra("TeacherName",TeacherName);
//                                    startActivity(iSearchByUser);
                                    addRating();
                                } else if (ratingLoginStr.equalsIgnoreCase("ratingLoginSession")) {
                                    addRating();
//                                    Intent iSearchByUser = new Intent(mContext, SessionName.class);
//                                    iSearchByUser.putExtra("frontLogin", "afterLogin");
//                                    iSearchByUser.putExtra("sessionID", sessionIDStr);
////                                    iSearchByUser.putExtra("SearchBy", searchByStr);
//                                    iSearchByUser.putExtra("board", boardStr);
//                                    iSearchByUser.putExtra("stream", streamStr);
//                                    iSearchByUser.putExtra("standard", standardStr);
//                                    iSearchByUser.putExtra("city", locationStr);
//                                    iSearchByUser.putExtra("sessionName", classNameStr);
//
//                                    iSearchByUser.putExtra("lessionName", subjectStr);
//                                    iSearchByUser.putExtra("gender", genderStr);
////                                    iSearchByUser.putExtra("withOR", whereTocomestr);
//                                    iSearchByUser.putExtra("ratingLogin", "false");
//                                    iSearchByUser.putExtra("searchfront", searchfront);
//                                    iSearchByUser.putExtra("sessionType", sessionType);
//                                    iSearchByUser.putExtra("firsttimesearch", firsttimesearch);
//                                    iSearchByUser.putExtra("RegionName", RegionName);
//                                    iSearchByUser.putExtra("TeacherName",TeacherName);
//                                    startActivity(iSearchByUser);
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
                                    iFamilyList.putExtra("TeacherName", TeacherName);
                                    startActivity(iFamilyList);
                                    finish();
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

        //forgotDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        forgotDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                    Utils.ping(mContext, "Invalid email address");
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
                            Utils.ping(mContext, "Please enter register email address");
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
                        Utils.ping(mContext, "Please enter register email address");
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
//        optionDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        optionDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        optionDialog.setCancelable(false);
        optionDialog.setContentView(optionDialogBinding.getRoot());
        String[] userName = Utils.getPref(mContext, "RegisterUserName").split("\\s+");

        optionDialogBinding.titleTxt.setText(Html.fromHtml("Welcome " + "<b>" + userName[0] + "</b>"));
        optionDialogBinding.addClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                inLogin.putExtra("frontLogin", frontloginStr);
                inLogin.putExtra("position", "1");
                startActivity(inLogin);
                optionDialog.dismiss();
            }
        });
        optionDialogBinding.viewClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                inLogin.putExtra("frontLogin", frontloginStr);
                inLogin.putExtra("position", "0");
                startActivity(inLogin);
                optionDialog.dismiss();
            }
        });
//        optionDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginScreenBinding.emailEdt.setText("");
//                loginScreenBinding.passwordEdt.setText("");
//                Utils.setPref(mContext, "coachID", "");
//                Utils.setPref(mContext, "coachTypeID","");
//                Utils.setPref(mContext, "RegisterUserName","");
//                Utils.setPref(mContext, "RegisterEmail","");
//                Utils.setPref(mContext, "LoginType","");
//                optionDialog.dismiss();
//            }
//        });
        optionDialog.show();

    }


    public void addRating() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_dialog_layout, null);
        final RatingBar ratingBar = alertLayout.findViewById(R.id.rating_bar);
        final TextView sessionNametxt = alertLayout.findViewById(R.id.session_name_txt);
        final TextView session_rating_view_txt = alertLayout.findViewById(R.id.session_rating_view_txt);
        final TextView cancel_txt = alertLayout.findViewById(R.id.cancel_txt);
        final TextView confirm_txt = alertLayout.findViewById(R.id.confirm_txt);
        final EditText comment_edt = alertLayout.findViewById(R.id.comment_edt);
        final TextView teacher_name_txt = alertLayout.findViewById(R.id.teacher_name_txt);
        sessionNametxt.setText(classNameStr);
        teacher_name_txt.setText(TeacherName);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                    int rating = (int) ratingBar.getRating();
                    if (rating == 1) {
                        session_rating_view_txt.setText("Very poor");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.remarks));
                    } else if (rating == 2) {
                        session_rating_view_txt.setText("Poor");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.remarks));
                    } else if (rating == 3) {
                        session_rating_view_txt.setText("Average");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.rating_bar));
                    } else if (rating == 4) {
                        session_rating_view_txt.setText("Good");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.present));
                    } else if (rating == 5) {
                        session_rating_view_txt.setText("Excellent");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.present));
                    }
                }
            }
        });

        AlertDialog.Builder sayWindows = new AlertDialog.Builder(
                mContext);

        sayWindows.setPositiveButton("Rate", null);
        sayWindows.setNegativeButton("Not Now", null);
        sayWindows.setView(alertLayout);

        final AlertDialog mAlertDialog = sayWindows.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button b1 = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String rating = String.valueOf(ratingBar.getRating());
//                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
                        commentStr = comment_edt.getText().toString();
                        if (commentStr.equalsIgnoreCase("")) {
                            commentStr = session_rating_view_txt.getText().toString();
                        }
                        ratingValueStr = String.valueOf(ratingBar.getRating());
                        if (!Utils.getPref(mContext, "coachID").equalsIgnoreCase("")) {
                            if (!ratingValueStr.equalsIgnoreCase("0.0")) {
                                callAddrating();
                                mAlertDialog.dismiss();
                            } else {
                                Utils.ping(mContext, "Please select rate");
                            }
                        } else {
                            Utils.ping(mContext, getResources().getString(R.string.not_loging));
                        }
                    }
                });
                b.setTextColor(getResources().getColor(R.color.blue));
                b1.setTextColor(getResources().getColor(R.color.gray1));
            }
        });
        mAlertDialog.show();
    }

    //Use for AddRating
    public void callAddrating() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().Add_Session_Rating(getratingDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel addratingmodel, Response response) {
                    Utils.dismissDialog();
                    if (addratingmodel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (addratingmodel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (addratingmodel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (addratingmodel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        Intent i = new Intent(mContext, SearchByUser.class);
                        startActivity(i);
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

    private Map<String, String> getratingDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("Comment", commentStr);
        map.put("RatingValue", ratingValueStr);

        return map;
    }
}
