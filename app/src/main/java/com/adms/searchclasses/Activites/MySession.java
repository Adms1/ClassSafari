package com.adms.searchclasses.Activites;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.searchclasses.Adapter.UserSessionListAdapter;
import com.adms.searchclasses.Adapter.UserSessionListAdapter1;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Interface.onChlidClick;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.ActivityMySessionBinding;
import com.adms.searchclasses.databinding.ChangePasswordDialogBinding;
import com.adms.searchclasses.databinding.LayoutMenuBinding;
import com.adms.searchclasses.databinding.SessiondetailConfirmationDialogBinding;

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

public class MySession extends AppCompatActivity implements View.OnClickListener {


    ActivityMySessionBinding mySessionBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;

    Context mContext;
    UserSessionListAdapter1 userSessionListAdapter;
    List<sessionDataModel> sessionList;

    //Use for Dialog
    Dialog confimDialog, changeDialog;
    String paymentStatusstr, sessionIDStr, selectedsessionIDStr, sessionPriceStr, orderIDStr, flag, sessionNameStr;
    SessionDetailModel dataResponse;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;

    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr, wheretocometypeStr, commentStr, ratingValueStr;//sessionId
    Dialog menuDialog;
    Button btnHome, btnMyReport, btnMySession, btnChangePassword, btnLogout, btnmyfamily, btnMyenroll, btnMyprofile;
    View view_home, view_profile, view_myenroll, view_mysession,
            view_myreport, view_btnfamily, view_changepass;
    TextView userNameTxt;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    int avgHoursvalue, avgMinitvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySessionBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_session);

        mContext = this;
        getIntenttValue();
//        setTypeface();
        init();
        setListner();
    }

    public void getIntenttValue() {
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
    }

    //Use for initilize view
    public void init() {
        callSessionReportApi();
    }

    //Use for Click Event
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
            ApiHandler.getApiService().get_FamilySessionList_ByFamilyID(getSessionReportDetail(), new retrofit.Callback<SessionDetailModel>() {
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
                    if (sessionModel.getSuccess().equalsIgnoreCase("False")) {
                        Utils.ping(mContext, "Classes not found");
                        mySessionBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {

                        sessionList = sessionModel.getData();

                        if (sessionModel.getData().size() > 0) {
                            mySessionBinding.headerLinear.setVisibility(View.GONE);
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
        map.put("FamilyID", Utils.getPref(mContext, "coachTypeID"));
        return map;
    }

    //Use for fill our class and class detail
    public void fillSessionList() {
        userSessionListAdapter = new UserSessionListAdapter1(mContext, sessionList, new onViewClick() {
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
        }, new onChlidClick() {
            @Override
            public void getChilClick() {
                addRating();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mySessionBinding.sessionRcList.setLayoutManager(mLayoutManager);
        mySessionBinding.sessionRcList.setItemAnimator(new DefaultItemAnimator());
        mySessionBinding.sessionRcList.setAdapter(userSessionListAdapter);
    }

    //Use for Give class to rating
    public void addRating() {
        ArrayList<String> selectedId = new ArrayList<String>();
        String sessionName = "";
        String[] splitvalue = new String[0];
        selectedId = userSessionListAdapter.getSessionDetail();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            splitvalue = selectedId.get(i).split("\\|");
            sessionName = splitvalue[0];
            sessionIDStr = splitvalue[1];
        }
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_dialog_layout, null);
        final RatingBar ratingBar = alertLayout.findViewById(R.id.rating_bar);
        final TextView sessionNametxt = alertLayout.findViewById(R.id.session_name_txt);
        final TextView session_rating_view_txt = alertLayout.findViewById(R.id.session_rating_view_txt);
        final TextView cancel_txt = alertLayout.findViewById(R.id.cancel_txt);
        final TextView confirm_txt = alertLayout.findViewById(R.id.confirm_txt);
        final EditText comment_edt = alertLayout.findViewById(R.id.comment_edt);
        final TextView teacher_name_txt = alertLayout.findViewById(R.id.teacher_name_txt);
        sessionNametxt.setText(sessionName);
        teacher_name_txt.setText(splitvalue[2]);
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

        android.app.AlertDialog.Builder sayWindows = new android.app.AlertDialog.Builder(
                mContext);

        sayWindows.setPositiveButton("Rate", null);
        sayWindows.setNegativeButton("Not Now", null);
        sayWindows.setView(alertLayout);

        final android.app.AlertDialog mAlertDialog = sayWindows.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button b1 = mAlertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
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
                        //callSessionListApi();
                        callSessionReportApi();
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
                        Utils.ping(mContext, "Login succesfully");
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

    //Use for GetSession Report
    public void callSessioncapacityApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Check_SpotAvailability_By_SessionID(getSessioncapacityDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel sessionModel, Response response) {
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
                        new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                                .setMessage(getResources().getString(R.string.fail_msg))
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        AppConfiguration.TeacherSessionIdStr = selectedsessionIDStr;
                        AppConfiguration.TeacherSessionContactIdStr = Utils.getPref(mContext, "coachID");
                        if (!Utils.getPref(mContext, "coachID").equalsIgnoreCase("") &&
                                !selectedsessionIDStr.equalsIgnoreCase("") &&
                                !sessionPriceStr.equalsIgnoreCase("0.00")) {
                            callpaymentRequestApi();
                        } else {
                            paymentStatusstr = "1";
                            callSessionConfirmationApi();
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

    private Map<String, String> getSessioncapacityDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);//contatIDstr  //Utils.getPref(mContext, "coachID")
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
                                AppConfiguration.bookingsubjectName = dataResponse.getData().get(j).getSessionName();
                                AppConfiguration.bookingteacherName = dataResponse.getData().get(j).getName();
                                AppConfiguration.bookingdate = dataResponse.getData().get(j).getStartDate();
                                AppConfiguration.bookingtime = dataResponse.getData().get(j).getSchedule();
                                AppConfiguration.bookingamount = dataResponse.getData().get(j).getSessionAmount();
                                AppConfiguration.bookinhEnddate=dataResponse.getData().get(j).getEndDate();
                                sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(dataResponse.getData().get(j).getSessionName());
                                sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(dataResponse.getData().get(j).getRating()));
//                                sessiondetailConfirmationDialogBinding.ratingUserTxt.setText();
                                sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(dataResponse.getData().get(j).getName());
                                sessiondetailConfirmationDialogBinding.locationTxt.setText(dataResponse.getData().get(j).getRegionName());
                                sessiondetailConfirmationDialogBinding.startDateTxt.setText(dataResponse.getData().get(j).getStartDate());
                                sessiondetailConfirmationDialogBinding.endDateTxt.setText(dataResponse.getData().get(j).getEndDate());
                                if (dataResponse.getData().get(j).getSessionAmount().equalsIgnoreCase("0.00")) {
                                    sessiondetailConfirmationDialogBinding.priceTxt.setText("Free");
                                } else {
                                    sessiondetailConfirmationDialogBinding.priceTxt.setText("₹" + dataResponse.getData().get(j).getSessionAmount());
                                }
                                if (!dataResponse.getData().get(j).getTotalRatingUser().equalsIgnoreCase("0")) {
                                    sessiondetailConfirmationDialogBinding.ratingUserTxt.setText("( " + dataResponse.getData().get(j).getTotalRatingUser() + " )");
                                }

                                sessionPriceStr = dataResponse.getData().get(j).getSessionAmount();
                                AppConfiguration.classsessionPrice = dataResponse.getData().get(j).getSessionAmount();
                                totalHours = new ArrayList<>();
                                totalMinit = new ArrayList<>();
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

                                    switch (spiltComma[0]) {
                                        case "sun":
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setText(SessionDuration);
                                            break;
                                        case "mon":
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setText(SessionDuration);
                                            break;
                                        case "tue":
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setText(SessionDuration);
                                            break;
                                        case "wed":
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setText(SessionDuration);
                                            break;
                                        case "thu":
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setText(SessionDuration);
                                            break;
                                        case "fri":
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setText(SessionDuration);
                                            break;
                                        case "sat":
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setText(SessionDuration);
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

    //Use for calculate class time
    public void calculateHours(String time1, String time2) {
        Date date1, date2;
        int days, hours, min;
        String hourstr, minstr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
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

            if (SessionMinit > 0) {
                if (SessionMinit < 10) {
                    SessionDuration = SessionHour + ":" + "0" + SessionMinit + " hrs";
                } else {
                    SessionDuration = SessionHour + ":" + SessionMinit + " hrs";
                }
            } else {
                SessionDuration = SessionHour + ":" + "00" + " hrs";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    //Use for password change
    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) mySessionBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);//, R.style.Theme_Dialog
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        //changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
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

    //Use for Menu
    public void menuDialog() {
        menuDialog = new Dialog(mContext);//, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.x = 10;
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnHome = (Button) menuDialog.findViewById(R.id.btnHome);
        btnMyprofile = (Button) menuDialog.findViewById(R.id.btnMyprofile);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
//        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);
        btnMyenroll = (Button) menuDialog.findViewById(R.id.btnMyenroll);

        view_home = (View) menuDialog.findViewById(R.id.view_home);
        view_profile = (View) menuDialog.findViewById(R.id.view_home);
        view_myenroll = (View) menuDialog.findViewById(R.id.view_myenroll);
        view_mysession = (View) menuDialog.findViewById(R.id.view_mysession);
        view_myreport = (View) menuDialog.findViewById(R.id.view_myreport);
        view_btnfamily = (View) menuDialog.findViewById(R.id.view_btnfamily);
        view_changepass = (View) menuDialog.findViewById(R.id.view_changepass);

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        btnMyenroll.setVisibility(View.GONE);
        view_myenroll.setVisibility(View.GONE);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, SearchByUser.class);
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

    //Use for class confirmation
    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog, (ViewGroup) mySessionBinding.getRoot(), false);
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        // confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        confimDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                AppConfiguration.UserName = (Utils.getPref(mContext, "RegisterUserName"));
                callSessioncapacityApi();
                confimDialog.dismiss();
            }
        });
        confimDialog.show();

    }


}
