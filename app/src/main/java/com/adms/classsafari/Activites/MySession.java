package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    ImageView back;
    Context mContext;
    UserSessionListAdapter userSessionListAdapter;
    List<sessionDataModel> sessionList;
    //Use for Dialog
    Dialog confimDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_student_txt_view,
            session_name_txt, location_txt, duration_txt, time_txt, time_txt_view, session_fee_txt;
    String paymentStatusstr, sessionIDStr, selectedsessionIDStr,sessionPriceStr, orderIDStr,flag;
    SessionDetailModel dataResponse;
    int SessionHour = 0;
    Integer SessionMinit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_session);

        mContext = this;
        init();
        setListner();
    }

    public void init() {
        header_linear = (LinearLayout) findViewById(R.id.header_linear);
        list_linear = (LinearLayout) findViewById(R.id.list_linear);
        session_rcList = (RecyclerView) findViewById(R.id.session_rcList);
        no_record_txt = (TextView) findViewById(R.id.no_record_txt);
        back = (ImageView) findViewById(R.id.back);
        callSessionReportApi();
        callSessionListApi();
    }

    public void setListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isearbyuser = new Intent(mContext, SearchByUser.class);
                startActivity(isearbyuser);
            }
        });
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
                        Utils.dismissDialog();
                        sessionList = sessionModel.getData();
                        if (sessionModel.getData().size() > 0) {
                            header_linear.setVisibility(View.VISIBLE);
                            fillSessionList();
                        } else {
                            header_linear.setVisibility(View.GONE);
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
                    String []spilt=selectedId.get(i).split("\\|");
                    sessionIDStr = spilt[0];
                    flag=spilt[1];
                }

                if(flag.equalsIgnoreCase("1")){

                }else {
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
        map.put("Amount",sessionPriceStr);

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
                        Utils.dismissDialog();
                        dataResponse = sessionInfo;
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
//        ArrayList<String> selectedId = new ArrayList<String>();
//        String sessionName = "";
//        selectedId = userSessionListAdapter.getContactID();
//        Log.d("selectedId", "" + selectedId);
//        for (int i = 0; i < selectedId.size(); i++) {
//            String []spilt=selectedId.get(i).split("\\|");
//            sessionIDStr = spilt[0];
//            flag=spilt[1];
//        }
        time_txt_view.setText("Date");
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            if (sessionIDStr.equalsIgnoreCase(dataResponse.getData().get(i).getSessionID())) {
                selectedsessionIDStr = dataResponse.getData().get(i).getSessionID();
                if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                    session_fee_txt.setText("Free");
                } else {
                    session_fee_txt.setText("₹ " + dataResponse.getData().get(i).getSessionAmount() + " /-");
                }
                sessionPriceStr=dataResponse.getData().get(i).getSessionAmount();
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
//        setDialogData();
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
}
