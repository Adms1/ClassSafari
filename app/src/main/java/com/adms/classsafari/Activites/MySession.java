package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.classsafari.Adapter.UserSessionListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MySession extends AppCompatActivity {

    LinearLayout header_linear, list_linear;
    RecyclerView session_rcList;
    TextView no_record_txt;
    ImageView back, menu;
    Context mContext;
    UserSessionListAdapter userSessionListAdapter;
    List<sessionDataModel> sessionList;
    //Use for Dialog
    Dialog confimDialog, sessionDetailDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_student_txt_view,
            session_name_txt, location_txt, duration_txt, time_txt, time_txt_view,
            session_fee_txt, session_board_txt, session_standard_txt, session_stream_txt,
            session_lession_txt, session_date_txt, session_time_txt;
    LinearLayout linear_board, linear_standard, linear_stream, linear_lession;
    String paymentStatusstr, sessionIDStr, selectedsessionIDStr, sessionPriceStr, orderIDStr, flag;
    SessionDetailModel dataResponse;
    int SessionHour = 0;
    Integer SessionMinit = 0;

    //Use for menu
    String passWordStr, confirmpassWordStr, currentpasswordStr, wheretocometypeStr;
    EditText edtnewpassword, edtconfirmpassword, edtcurrentpassword;
    Button changepwd_btn, cancel_btn;
    Dialog menuDialog;
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily;
    TextView user_name_txt;
    Dialog changeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_session);

        mContext = this;
        getIntenttValue();
        init();
        setListner();
    }

    public void getIntenttValue() {
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
    }

    public void init() {
        header_linear = (LinearLayout) findViewById(R.id.header_linear);
        list_linear = (LinearLayout) findViewById(R.id.list_linear);
        session_rcList = (RecyclerView) findViewById(R.id.session_rcList);
        no_record_txt = (TextView) findViewById(R.id.no_record_txt);
        back = (ImageView) findViewById(R.id.back);
        menu = (ImageView) findViewById(R.id.menu);
        callSessionListApi();


    }

    public void setListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (wheretocometypeStr.equalsIgnoreCase("mySession")) {
//                    Intent intent = new Intent(mContext, MySession.class);
//                    intent.putExtra("wheretocometype", wheretocometypeStr);
//                    startActivity(intent);
//                }else if(wheretocometypeStr.equalsIgnoreCase("myAccount")){
//                    Intent intent = new Intent(mContext, MyAccountActivity.class);
//                    intent.putExtra("wheretocometype", wheretocometypeStr);
//                    startActivity(intent);
//                }else if(wheretocometypeStr.equalsIgnoreCase("menu")){
                Intent intent = new Intent(mContext, SearchByUser.class);
                intent.putExtra("wheretocometype", wheretocometypeStr);
                startActivity(intent);
//                }
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog();
            }
        });
    }

    //Use for GetSession Report
    public void callSessionReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
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
                            header_linear.setVisibility(View.VISIBLE);
                            fillSessionList();
                        } else {
                            header_linear.setVisibility(View.GONE);
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
                }

                if (flag.equalsIgnoreCase("1")) {
                    SessionDetailDialog();
                } else {
                    ConformSessionDialog();
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        session_rcList.setLayoutManager(mLayoutManager);
        session_rcList.setItemAnimator(new DefaultItemAnimator());
        session_rcList.setAdapter(userSessionListAdapter);
    }

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
        setDialogData();
        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirm_txt.setOnClickListener(new View.OnClickListener() {
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
                        ipayment.putExtra("mode", "TEST");
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
            ApiHandler.getApiService().get_SessionList(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {

                        dataResponse = sessionInfo;
                        callSessionReportApi();
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

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void setDialogData() {
        if (!flag.equalsIgnoreCase("1")) {
            time_txt_view.setText("Date");
            for (int i = 0; i < dataResponse.getData().size(); i++) {
                if (sessionIDStr.equalsIgnoreCase(dataResponse.getData().get(i).getSessionID())) {
                    selectedsessionIDStr = dataResponse.getData().get(i).getSessionID();
                    if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                        session_fee_txt.setText("Free");
                    } else {
                        session_fee_txt.setText("₹ " + dataResponse.getData().get(i).getSessionAmount() + " /-");
                    }
                    sessionPriceStr = dataResponse.getData().get(i).getSessionAmount();
                    session_name_txt.setText(dataResponse.getData().get(i).getSessionName());
                    location_txt.setText(dataResponse.getData().get(i).getAddressLine1()
                            + ", " + dataResponse.getData().get(i).getRegionName()
                            + ", " + dataResponse.getData().get(i).getAddressCity()
                            + ", " + dataResponse.getData().get(i).getAddressState()
                            + "- " + dataResponse.getData().get(i).getAddressZipCode());
                    session_student_txt.setText(dataResponse.getData().get(i).getName());
                    duration_txt.setText(dataResponse.getData().get(i).getStartDate() + " To " + dataResponse.getData().get(i).getEndDate());
                    String[] spiltPipes = dataResponse.getData().get(i).getSchedule().split("\\|");
                    String[] spiltComma;
                    String[] spiltDash;
                    Log.d("spilt", "" + spiltPipes.toString());
                    for (int j = 0; j < spiltPipes.length; j++) {
                        spiltComma = spiltPipes[j].split("\\,");
                        spiltDash = spiltComma[1].split("\\-");
                        calculateHours(spiltDash[0], spiltDash[1]);
                    }
                    time_txt.setText(SessionHour + " hr " + SessionMinit + " min");

                }
            }
        } else {
            for (int i = 0; i < dataResponse.getData().size(); i++) {
                if (sessionIDStr.equalsIgnoreCase(dataResponse.getData().get(i).getSessionID())) {
                    if (!dataResponse.getData().get(i).getCoachTypeID().equalsIgnoreCase("1")) {
                        linear_board.setVisibility(View.GONE);
                        linear_standard.setVisibility(View.GONE);
                        linear_stream.setVisibility(View.GONE);
                    } else {
                        linear_board.setVisibility(View.VISIBLE);
                        linear_standard.setVisibility(View.VISIBLE);
                        linear_stream.setVisibility(View.VISIBLE);
                    }
                    session_name_txt.setText(dataResponse.getData().get(i).getSessionName());
                    session_student_txt.setText(dataResponse.getData().get(i).getName());
                    session_board_txt.setText(dataResponse.getData().get(i).getBoard());
                    session_standard_txt.setText(dataResponse.getData().get(i).getStandard());
                    session_stream_txt.setText(dataResponse.getData().get(i).getStream());
                    session_lession_txt.setText(dataResponse.getData().get(i).getLessionTypeName());
                    location_txt.setText(dataResponse.getData().get(i).getAddressLine1()
                            + ", " + dataResponse.getData().get(i).getRegionName()
                            + ", " + dataResponse.getData().get(i).getAddressCity()
                            + ", " + dataResponse.getData().get(i).getAddressState()
                            + "- " + dataResponse.getData().get(i).getAddressZipCode());
                    session_date_txt.setText(dataResponse.getData().get(i).getStartDate() + " To " + dataResponse.getData().get(i).getEndDate());
                    String[] spiltPipes = dataResponse.getData().get(i).getSchedule().split("\\|");
                    String[] spiltComma;
                    String[] spiltDash;
                    Log.d("spilt", "" + spiltPipes.toString());
                    for (int j = 0; j < spiltPipes.length; j++) {
                        spiltComma = spiltPipes[j].split("\\,");
                        spiltDash = spiltComma[1].split("\\-");
                        calculateHours(spiltDash[0], spiltDash[1]);
                    }
                    session_time_txt.setText(SessionHour + " hr " + SessionMinit + " min");
                }
            }
        }
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


    public void SessionDetailDialog() {
        sessionDetailDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = sessionDetailDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        sessionDetailDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        sessionDetailDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        sessionDetailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sessionDetailDialog.setCancelable(false);
        sessionDetailDialog.setContentView(R.layout.dialog_session_detail);


        session_board_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_board_txt);
        session_standard_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_standard_txt);
        session_stream_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_stream_txt);
        session_lession_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_lession_txt);
        session_student_txt_view = (TextView) sessionDetailDialog.findViewById(R.id.session_student_txt_view);
        session_student_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_student_txt);
        session_name_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_name_txt);
        location_txt = (TextView) sessionDetailDialog.findViewById(R.id.location_txt);
        cancel_txt = (TextView) sessionDetailDialog.findViewById(R.id.cancel_txt);
        session_date_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_date_txt);
        session_time_txt = (TextView) sessionDetailDialog.findViewById(R.id.session_time_txt);
        linear_board = (LinearLayout) sessionDetailDialog.findViewById(R.id.linear_board);
        linear_standard = (LinearLayout) sessionDetailDialog.findViewById(R.id.linear_standard);
        linear_stream = (LinearLayout) sessionDetailDialog.findViewById(R.id.linear_stream);
        linear_lession = (LinearLayout) sessionDetailDialog.findViewById(R.id.linear_lession);

        session_student_txt_view.setText("TEACHER NAME");
        setDialogData();

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionDetailDialog.dismiss();
            }
        });


        sessionDetailDialog.show();
    }


    public void changePasswordDialog() {
        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(R.layout.change_password_dialog);

        cancel_btn = (Button) changeDialog.findViewById(R.id.cancel_btn);
        changepwd_btn = (Button) changeDialog.findViewById(R.id.changepwd_btn);
        edtconfirmpassword = (EditText) changeDialog.findViewById(R.id.edtconfirmpassword);
        edtnewpassword = (EditText) changeDialog.findViewById(R.id.edtnewpassword);
        edtcurrentpassword = (EditText) changeDialog.findViewById(R.id.edtcurrentpassword);

        changepwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = edtcurrentpassword.getText().toString();
                confirmpassWordStr = edtconfirmpassword.getText().toString();
                passWordStr = edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContext, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            edtcurrentpassword.setError("Confirm Password does not match.");
                        }
                    } else {
//                    Util.ping(mContex, "Confirm Password does not match.");
                        edtconfirmpassword.setError("Password must be 4-8 Characters.");
                        edtconfirmpassword.setText("");
                        edtconfirmpassword.setText("");
                    }
                } else {
                    edtcurrentpassword.setError("Password does not match to current password.");
                }


            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
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
        menuDialog.setContentView(R.layout.layout_menu);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);
        user_name_txt = (TextView) menuDialog.findViewById(R.id.user_name_txt);

        user_name_txt.setText(Utils.getPref(mContext, "RegisterUserName"));
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "mySession");
                startActivity(imyaccount);
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "mySession");
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
                intent.putExtra("wheretocometype", "mySession");
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
}
