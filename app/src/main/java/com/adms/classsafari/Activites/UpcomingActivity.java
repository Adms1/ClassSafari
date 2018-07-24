package com.adms.classsafari.Activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.adms.classsafari.Adapter.UpcomingClassAdapter;
import com.adms.classsafari.Adapter.UserSessionListAdapter1;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityMySessionBinding;
import com.adms.classsafari.databinding.ActivityUpcomingBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;
import com.adms.classsafari.databinding.LayoutMenuBinding;
import com.adms.classsafari.databinding.SessiondetailConfirmationDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpcomingActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUpcomingBinding activityUpcomingBinding;
    Context mContext;
    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr, wheretocometypeStr;
    Dialog menuDialog, changeDialog;
    Button btnHome,btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily, btnMyenroll, btnMyprofile;
    View view_home, view_profile, view_myenroll, view_mysession,
            view_myreport, view_btnfamily, view_changepass;
    TextView userNameTxt;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    UpcomingClassAdapter upcomingClassAdapter;
    ArrayList<String> arrayList;
    SessionDetailModel dataresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpcomingBinding = DataBindingUtil.setContentView(this, R.layout.activity_upcoming);

        mContext = this;
        init();
        setListner();
    }

    public void init() {
        callSessionUpcomingReportApi();
    }

    public void setListner() {
        activityUpcomingBinding.back.setOnClickListener(this);
        activityUpcomingBinding.menu.setOnClickListener(this);
    }

    public void getIntenttValue() {
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent(mContext, SearchByUser.class);
                intent.putExtra("wheretocometype", wheretocometypeStr);
                startActivity(intent);
                break;
            case R.id.menu:
                menuDialog();
                break;
        }
    }

    //Use for GetSession Report
    public void callSessionUpcomingReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Upcoming_Classes(getSessionUpcomingReportDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Classes not found");
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        dataresponse=sessionModel;
                        if (sessionModel.getData().size()>0) {
                            fillSessionList();
                        }
                        Utils.dismissDialog();
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

    private Map<String, String> getSessionUpcomingReportDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("FamilyID", Utils.getPref(mContext, "coachTypeID"));
        return map;
    }

    public void fillSessionList() {

        upcomingClassAdapter = new UpcomingClassAdapter(mContext, dataresponse, new onViewClick() {
            @Override
            public void getViewClick() {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        activityUpcomingBinding.upcomingClassRcList.setLayoutManager(mLayoutManager);
        activityUpcomingBinding.upcomingClassRcList.setItemAnimator(new DefaultItemAnimator());
        activityUpcomingBinding.upcomingClassRcList.setAdapter(upcomingClassAdapter);
    }

    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) activityUpcomingBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        // changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        changeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(changePasswordDialogBinding.getRoot());

        changePasswordDialogBinding.changepwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = changePasswordDialogBinding.edtcurrentpassword.getText().toString();
                confirmpassWordStr = changePasswordDialogBinding.edtconfirmpassword.getText().toString();
                passWordStr = changePasswordDialogBinding.edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContext, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            changePasswordDialogBinding.edtconfirmpassword.setError("Confirm Password does not match.");
                        }
                    } else {
                        changePasswordDialogBinding.edtnewpassword.setError("Password must be 4-8 Characters.");
                        changePasswordDialogBinding.edtnewpassword.setText("");
                        changePasswordDialogBinding.edtconfirmpassword.setText("");
                    }
                } else {
                    changePasswordDialogBinding.edtcurrentpassword.setError("Password does not match to current password.");
                }


            }
        });
        changePasswordDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDialog.dismiss();
            }
        });

        changeDialog.show();

    }

    //USe for Change Password
    public void callChangePasswordApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Change_Password(getChangePasswordDetail(), new retrofit.Callback<TeacherInfoModel>() {
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
                        Utils.ping(mContext, "Please enter valid password");
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.ping(mContext, getResources().getString(R.string.changPassword));
                        Utils.setPref(mContext, "Password", passWordStr);
                        changeDialog.dismiss();
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

    private Map<String, String> getChangePasswordDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", Utils.getPref(mContext, "RegisterEmail"));
        map.put("Password", passWordStr);
        return map;
    }

    public void menuDialog() {
        menuDialog = new Dialog(mContext);//, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.x = 10;
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP|Gravity.RIGHT;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnHome=(Button)menuDialog.findViewById(R.id.btnHome);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
//        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);
        btnMyenroll = (Button) menuDialog.findViewById(R.id.btnMyenroll);
        btnMyprofile = (Button) menuDialog.findViewById(R.id.btnMyprofile);
        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);

        view_home = (View) menuDialog.findViewById(R.id.view_home);
        view_profile = (View) menuDialog.findViewById(R.id.view_home);
        view_myenroll = (View) menuDialog.findViewById(R.id.view_myenroll);
        view_mysession = (View) menuDialog.findViewById(R.id.view_mysession);
        view_myreport = (View) menuDialog.findViewById(R.id.view_myreport);
        view_btnfamily = (View) menuDialog.findViewById(R.id.view_btnfamily);
        view_changepass = (View) menuDialog.findViewById(R.id.view_changepass);

        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        btnMySession.setVisibility(View.GONE);
        view_mysession.setVisibility(View.GONE);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext,SearchByUser.class);
                startActivity(i);
            }
        });
        btnMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, AddStudentScreen.class);
                imyaccount.putExtra("wheretocometype", "menu");
                imyaccount.putExtra("myprofile", "true");
                imyaccount.putExtra("type", "myprofile");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "menu");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnMyenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "menu");
                startActivity(isession);
                menuDialog.dismiss();
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpcomingActivity.class);
                intent.putExtra("wheretocometype", "menu");
                startActivity(intent);
                menuDialog.dismiss();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                changePasswordDialog();
            }
        });
        btnmyfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FamilyListActivity.class);
                intent.putExtra("froncontanct", "true");
                intent.putExtra("wheretocometype", "menu");
                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
                startActivity(intent);
                menuDialog.dismiss();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogTheme))
                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.setPref(mContext, "coachID", "");
                                Utils.setPref(mContext, "coachTypeID", "");
                                Utils.setPref(mContext, "RegisterUserName", "");
                                Utils.setPref(mContext, "RegisterEmail", "");
                                Utils.setPref(mContext, "LoginType", "");
                                Utils.setPref(mContext, "Password", "");
                                Utils.setPref(mContext, "FamilyID", "");
                                Utils.setPref(mContext, "location", "");
                                Utils.setPref(mContext, "sessionName", "");
                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
                                intentLogin.putExtra("frontLogin", "beforeLogin");
                                startActivity(intentLogin);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
//                        .setIcon(R.drawable.safari)
                        .show();
            }
        });
        menuDialog.show();
    }
}
