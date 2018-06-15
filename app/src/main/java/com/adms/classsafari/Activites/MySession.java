package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.adms.classsafari.Adapter.UserSessionListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityMySessionBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;
import com.adms.classsafari.databinding.ConfirmSessionDialogBinding;
import com.adms.classsafari.databinding.LayoutMenuBinding;
import com.adms.classsafari.databinding.SessiondetailConfirmationDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MySession extends AppCompatActivity implements View.OnClickListener {


    ActivityMySessionBinding mySessionBinding;
    ConfirmSessionDialogBinding confirmSessionDialogBinding;

    LayoutMenuBinding menuBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;

    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;

    Context mContext;
    UserSessionListAdapter userSessionListAdapter;
    List<sessionDataModel> sessionList;

    //Use for Dialog
    Dialog confimDialog, changeDialog;
    String paymentStatusstr, sessionIDStr, selectedsessionIDStr, sessionPriceStr, orderIDStr, flag, sessionNameStr;
    SessionDetailModel dataResponse;
    int SessionHour = 0;
    Integer SessionMinit = 0;

    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr, wheretocometypeStr;
    Dialog menuDialog;
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily;
    TextView userNameTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySessionBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_session);

        mContext = this;
        getIntenttValue();
        init();
        setListner();
    }

    public void getIntenttValue() {
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
    }

    public void init() {
        callSessionReportApi();
    }

    public void setListner() {
        mySessionBinding.back.setOnClickListener(this);
        mySessionBinding.menu.setOnClickListener(this);
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
    public void callSessionReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_FamilySessionList_ByContactID(getSessionReportDetail(), new retrofit.Callback<SessionDetailModel>() {
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
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {

                        sessionList = sessionModel.getData();

                        if (sessionModel.getData().size() > 0) {
                            mySessionBinding.headerLinear.setVisibility(View.VISIBLE);
                            mySessionBinding.noRecordTxt.setVisibility(View.GONE);
                            fillSessionList();
                        } else {
                            mySessionBinding.noRecordTxt.setVisibility(View.VISIBLE);
                            mySessionBinding.headerLinear.setVisibility(View.GONE);
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

    private Map<String, String> getSessionReportDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        return map;
    }

    public void fillSessionList() {
        userSessionListAdapter = new UserSessionListAdapter(mContext, sessionList, new onViewClick() {
            @Override
            public void getViewClick() {
                ArrayList<String> selectedId = new ArrayList<String>();
                String sessionName = "";
                selectedId = userSessionListAdapter.getContactID();
                Log.d("selectedId", "" + selectedId);
                for (int i = 0; i < selectedId.size(); i++) {
                    String[] spilt = selectedId.get(i).split("\\|");
                    sessionIDStr = spilt[0];
                    flag = spilt[1];
                    sessionNameStr = spilt[2];
                }
                selectedsessionIDStr = sessionIDStr;
                if (flag.equalsIgnoreCase("1")) {
//                    SessionDetailDialog();
                    Intent intent = new Intent(mContext, SessionDetailActivity.class);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    intent.putExtra("sessionID", sessionIDStr);
                    startActivity(intent);
                } else {
                    SessionConfirmationDialog();
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mySessionBinding.sessionRcList.setLayoutManager(mLayoutManager);
        mySessionBinding.sessionRcList.setItemAnimator(new DefaultItemAnimator());
        mySessionBinding.sessionRcList.setAdapter(userSessionListAdapter);
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
                        ipayment.putExtra("amount", sessionPriceStr);
                        ipayment.putExtra("mode", AppConfiguration.Mode);
                        ipayment.putExtra("username", Utils.getPref(mContext, "RegisterUserName"));
                        ipayment.putExtra("sessionID", selectedsessionIDStr);
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
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("SessionID", selectedsessionIDStr);
        map.put("Amount", sessionPriceStr);

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
        map.put("SessionID", selectedsessionIDStr);
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("PaymentStatus", paymentStatusstr);
        return map;
    }

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionBySessionID(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionDetailInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionDetailInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess().equalsIgnoreCase("True")) {

                        if (sessionDetailInfo.getData().size() > 0) {
                            dataResponse = sessionDetailInfo;
                            for (int j = 0; j < dataResponse.getData().size(); j++) {
                                sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(dataResponse.getData().get(j).getSessionName());
                                sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(dataResponse.getData().get(j).getRating()));
//                                sessiondetailConfirmationDialogBinding.ratingUserTxt.setText();
                                sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(dataResponse.getData().get(j).getName());
                                sessiondetailConfirmationDialogBinding.locationTxt.setText(dataResponse.getData().get(j).getRegionName());
                                sessiondetailConfirmationDialogBinding.startDateTxt.setText(dataResponse.getData().get(j).getStartDate());
                                sessiondetailConfirmationDialogBinding.endDateTxt.setText(dataResponse.getData().get(j).getEndDate());
                                sessionPriceStr=dataResponse.getData().get(j).getSessionAmount();
                                AppConfiguration.classsessionPrice = dataResponse.getData().get(j).getSessionAmount();
                                String[] spiltPipes = dataResponse.getData().get(j).getSchedule().split("\\|");
                                String[] spiltComma;
                                String[] spiltDash;
                                Log.d("spilt", "" + spiltPipes.toString());
                                for (int i = 0; i < spiltPipes.length; i++) {
                                    spiltComma = spiltPipes[i].split("\\,");
                                    spiltDash = spiltComma[1].split("\\-");
                                    calculateHours(spiltDash[0], spiltDash[1]);
                                    dataResponse.getData().get(j).setDateTime(spiltDash[0]);
                                    Log.d("DateTime", spiltDash[0]);
                                    sessiondetailConfirmationDialogBinding.durationTxt.setText(SessionHour + " hr " + SessionMinit + " min");
                                    switch (spiltComma[0]) {
                                        case "sun":
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "mon":
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "tue":
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "wed":
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "thu":
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "fri":
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "sat":
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        default:

                                    }
                                }
                            }

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

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        return map;
    }

    public void calculateHours(String time1, String time2) {
        Date date1, date2;
        int days, hours, min;
        String hourstr, minstr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            SessionHour = hours;
            SessionMinit = min;
            Log.i("======= Hours", " :: " + hours + ":" + min);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) mySessionBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

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
                        Utils.ping(mContext, "Please Enter Valid Password.");
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
        menuDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        menuDialog.getWindow().getAttributes().verticalMargin = 0.1F;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        btnMySession.setVisibility(View.GONE);

        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "menu");
                startActivity(imyaccount);
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "menu");
                startActivity(isession);
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
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                        .setCancelable(false)
                        .setTitle("Logout")
                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
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
                        .setIcon(R.drawable.safari)
                        .show();
            }
        });
        menuDialog.show();
    }

    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog, (ViewGroup) mySessionBinding.getRoot(), false);
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(sessiondetailConfirmationDialogBinding.getRoot());
        callSessionListApi();


        sessiondetailConfirmationDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        sessiondetailConfirmationDialogBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.getPref(mContext, "coachID").equalsIgnoreCase("") &&
                        !selectedsessionIDStr.equalsIgnoreCase("") &&
                        !sessionPriceStr.equalsIgnoreCase("0.00")) {
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


}
