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
import android.support.v7.widget.OrientationHelper;
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

import com.adms.classsafari.Adapter.SessionDetailAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySessionNameNewBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;
import com.adms.classsafari.databinding.ConfirmSessionDialogBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionName extends AppCompatActivity implements View.OnClickListener {
    ActivitySessionNameNewBinding sessionNameBinding;
    ConfirmSessionDialogBinding confirmSessionDialogBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;

    Context mContext;


    SessionDetailAdapter sessionDetailAdapter;
    List<sessionDataModel> arrayList;
    List<sessionDataModel> sessionRatingList;
    ArrayList<String> reviewarray;
    ArrayList<String> descriptionarray;
    ArrayList<String> descriptionviewarray;
    String ratinguserStr,sessionratingcommentStr="";
    float rating=0;
    Dialog confimDialog;
    //    TextView cancel_txt, confirm_txt, session_student_txt, session_student_txt_view, session_name_txt, location_txt, duration_txt, time_txt, time_txt_view, session_fee_txt;
    String sessionIDStr, searchByStr, locationStr, classNameStr, genderStr, sessionDateStr, durationStr,
            paymentStatusstr, orderIDStr, boardStr, standardStr, streamStr, searchTypeStr, subjectStr,
            wheretoComeStr, sessionId, commentStr="", ratingValueStr, purchaseSessionIDStr = "", searchfront,
            familysessionfeesStr, familysessionnameStr, familylocationStr, familysessionStudentStr, sessionType, firsttimesearch;

    ArrayList<String> purchaseSessionIDArray;
    SessionDetailModel dataResponse, dataResponseRating;

    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr, wheretocometypeStr;
    Dialog menuDialog, changeDialog;
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily;
    TextView userNameTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionNameBinding = DataBindingUtil.setContentView(this, R.layout.activity_session_name_new);
        mContext = SessionName.this;
        subjectStr = getIntent().getStringExtra("lessionName");
        sessionIDStr = getIntent().getStringExtra("sessionID");
        searchByStr = getIntent().getStringExtra("SearchBy");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        sessionDateStr = getIntent().getStringExtra("sessiondate");
        durationStr = getIntent().getStringExtra("duration");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        boardStr = getIntent().getStringExtra("board");
        searchTypeStr = getIntent().getStringExtra("searchType");
        genderStr = getIntent().getStringExtra("gender");
        wheretoComeStr = getIntent().getStringExtra("withOR");
        searchfront = getIntent().getStringExtra("searchfront");
        sessionType = getIntent().getStringExtra("sessionType");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        init();
        setListner();
    }

    public void init() {
        if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
            sessionNameBinding.menu.setVisibility(View.GONE);
        }else{
            sessionNameBinding.menu.setVisibility(View.VISIBLE);
        }
        callSessionListApi();

    }

    public void setListner() {

        sessionNameBinding.back.setOnClickListener(this);
        sessionNameBinding.bookSessionBtn.setOnClickListener(this);
        sessionNameBinding.menu.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_session_btn:
//                if (purchaseSessionIDArray.size() > 0) {
//                    for (int i = 0; i < purchaseSessionIDArray.size(); i++) {
//                        if (sessionIDStr.equalsIgnoreCase(purchaseSessionIDArray.get(i))) {
//                            purchaseSessionIDStr = purchaseSessionIDArray.get(i);
//                        }
//                    }
//                }
//                if (purchaseSessionIDStr.equalsIgnoreCase("")) {
                    if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
                        new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                                .setCancelable(false)
                                .setTitle("Login")
                                .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                                .setMessage("You are not login,So Please Login.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        setDialogData();
                                        Intent intentLogin = new Intent(mContext, LoginActivity.class);
                                        intentLogin.putExtra("frontLogin", "afterLogin");
                                        intentLogin.putExtra("sessionID", sessionIDStr);
                                        intentLogin.putExtra("SearchBy", searchByStr);
                                        intentLogin.putExtra("board", boardStr);
                                        intentLogin.putExtra("stream", streamStr);
                                        intentLogin.putExtra("standard", standardStr);
                                        intentLogin.putExtra("city", locationStr);
                                        intentLogin.putExtra("sessionName", classNameStr);
                                        intentLogin.putExtra("searchType", searchTypeStr);
                                        intentLogin.putExtra("lessionName", subjectStr);
                                        intentLogin.putExtra("gender", genderStr);
                                        intentLogin.putExtra("withOR", wheretoComeStr);
                                        intentLogin.putExtra("ratingLogin", "false");
                                        intentLogin.putExtra("location", familylocationStr);
                                        intentLogin.putExtra("searchfront", searchfront);
                                        intentLogin.putExtra("sessionType", sessionType);
                                        intentLogin.putExtra("firsttimesearch", firsttimesearch);
                                        intentLogin.putExtra("duration", durationStr);
                                        intentLogin.putExtra("sessiondate", sessionDateStr);
                                        intentLogin.putExtra("sessionStudent", familysessionStudentStr);
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
                    } else {
//                        ConformSessionDialog();
                        setDialogData();
                        Intent intent = new Intent(mContext, FamilyListActivity.class);
                        intent.putExtra("sessionID", sessionIDStr);
                        intent.putExtra("duration", durationStr);
                        intent.putExtra("sessiondate", sessionDateStr);
                        intent.putExtra("sessionName", familysessionnameStr);
                        intent.putExtra("location", familylocationStr);
                        intent.putExtra("sessionfees", familysessionfeesStr);
                        intent.putExtra("sessionStudent", familysessionStudentStr);
                        intent.putExtra("city", locationStr);
                        intent.putExtra("SearchBy", searchByStr);
                        intent.putExtra("board", boardStr);
                        intent.putExtra("stream", streamStr);
                        intent.putExtra("standard", standardStr);
                        intent.putExtra("searchType", searchTypeStr);
                        intent.putExtra("lessionName", subjectStr);
                        intent.putExtra("gender", genderStr);
                        intent.putExtra("withOR", wheretoComeStr);
                        intent.putExtra("froncontanct", "false");
                        intent.putExtra("searchfront", searchfront);
                        intent.putExtra("sessionType", sessionType);
                        intent.putExtra("firsttimesearch", firsttimesearch);
                        startActivity(intent);
                    }
//                } else {
//                    new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
//                            .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
//                            .setMessage("You are already purchase.")
//                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .setIcon(R.drawable.safari)
//                            .show();
//                }
                break;
            case R.id.back:
                Intent inback = new Intent(mContext, ClassDeatilScreen.class);
                inback.putExtra("SearchBy", searchByStr);
                inback.putExtra("city", locationStr);
                inback.putExtra("sessionName", classNameStr);
                inback.putExtra("board", boardStr);
                inback.putExtra("stream", streamStr);
                inback.putExtra("standard", standardStr);
                inback.putExtra("searchType", searchTypeStr);
                inback.putExtra("lessionName", subjectStr);
                inback.putExtra("gender", genderStr);
                inback.putExtra("withOR", wheretoComeStr);
                inback.putExtra("city", locationStr);
                inback.putExtra("sessionName", classNameStr);
                inback.putExtra("searchfront", searchfront);
                inback.putExtra("sessionType", sessionType);
                inback.putExtra("firsttimesearch", firsttimesearch);
                startActivity(inback);
                break;
            case R.id.menu:
                menuDialog();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inback = new Intent(mContext, ClassDeatilScreen.class);
        inback.putExtra("SearchBy", searchByStr);
        inback.putExtra("city", locationStr);
        inback.putExtra("sessionName", classNameStr);
        inback.putExtra("board", boardStr);
        inback.putExtra("stream", streamStr);
        inback.putExtra("standard", standardStr);
        inback.putExtra("searchType", searchTypeStr);
        inback.putExtra("lessionName", subjectStr);
        inback.putExtra("gender", genderStr);
        inback.putExtra("withOR", wheretoComeStr);
        inback.putExtra("city", locationStr);
        inback.putExtra("sessionName", classNameStr);
        inback.putExtra("city", locationStr);
        inback.putExtra("searchfront", searchfront);
        inback.putExtra("sessionName", classNameStr);
        inback.putExtra("sessionType", sessionType);
        inback.putExtra("firsttimesearch", firsttimesearch);
        inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inback);
    }

    public void ConformSessionDialog() {

        confirmSessionDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.confirm_session_dialog, (ViewGroup) sessionNameBinding.getRoot(), false);

        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(confirmSessionDialogBinding.getRoot());


        setDialogData();
        confirmSessionDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirmSessionDialogBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.getPref(mContext, "coachID").equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
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
//                            setData();
                            callSessionReportApi();
                            callSessionRatingApi();
                            Utils.dismissDialog();
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

    //Use for SessionRating
    public void callSessionRatingApi() {
        if (Utils.checkNetwork(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionRating_By_Session_ID(getSessionRatingDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionRatingInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionRatingInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionRatingInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionRatingInfo.getSuccess().equalsIgnoreCase("false")) {
//                        Utils.ping(mContext, getString(R.string.false_msg));
                        dataResponseRating = sessionRatingInfo;
                        sessionRatingList = new ArrayList<sessionDataModel>();
                        if (dataResponseRating.getData().size() > 0) {
                            for (int i = 0; i < dataResponseRating.getData().size(); i++) {
                                sessionRatingList.add(dataResponseRating.getData().get(i));
                            }
                        }
                        ratinguserStr= String.valueOf(sessionRatingList.size());
                        reviewarray = new ArrayList<>();
                        setData();
                        Utils.dismissDialog();
                        return;
                    }
                    if (sessionRatingInfo.getSuccess().equalsIgnoreCase("True")) {

                        if (sessionRatingInfo.getData().size() > 0) {
                            dataResponseRating = sessionRatingInfo;
                            sessionRatingList = new ArrayList<sessionDataModel>();
                            for (int i = 0; i < dataResponseRating.getData().size(); i++) {
                                sessionRatingList.add(dataResponseRating.getData().get(i));
                            }
                            ratinguserStr= String.valueOf(sessionRatingList.size());
                            reviewarray = new ArrayList<>();
                            reviewarray.add("Reviews");
                            setData();
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

    private Map<String, String> getSessionRatingDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Session_ID", sessionIDStr);
        return map;
    }

    public void setData() {
        arrayList = new ArrayList<sessionDataModel>();
        descriptionarray = new ArrayList<>();
        descriptionviewarray = new ArrayList<String>();


        for (int i = 0; i < dataResponse.getData().size(); i++) {
            arrayList.add(dataResponse.getData().get(i));
            AppConfiguration.classsessionPrice = dataResponse.getData().get(i).getSessionAmount();
            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                dataResponse.getData().get(i).setSessionAmount("Free");
            }
            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("Free")) {
                sessionNameBinding.priceTxt.setText(dataResponse.getData().get(i).getSessionAmount());
            } else {
                sessionNameBinding.priceTxt.setText("₹ " + dataResponse.getData().get(i).getSessionAmount() + " /-");
            }
            AppConfiguration.classSessionName = dataResponse.getData().get(i).getSessionName();
            AppConfiguration.classteacherSessionName = dataResponse.getData().get(i).getName();
            AppConfiguration.classsessionLocation = dataResponse.getData().get(i).getAddressLine1()
                    + ", " + dataResponse.getData().get(i).getRegionName()
                    + ", " + dataResponse.getData().get(i).getAddressCity()
                    + ", " + dataResponse.getData().get(i).getAddressState()
                    + "- " + dataResponse.getData().get(i).getAddressZipCode();
            AppConfiguration.classsessionDate = sessionDateStr;
            AppConfiguration.classsessionDuration = durationStr;

            descriptionviewarray.add(dataResponse.getData().get(i).getDescription());

        }

        if (!descriptionviewarray.get(0).equalsIgnoreCase("")) {
            descriptionarray.add("Description");
        } else {
            descriptionarray = new ArrayList<>();
            descriptionviewarray = new ArrayList<>();
        }
        //,descriptionarray
        sessionDetailAdapter = new SessionDetailAdapter(mContext, arrayList, descriptionviewarray, reviewarray, sessionRatingList,ratinguserStr, new onViewClick() {
            @Override
            public void getViewClick() {
                if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
                    addRating();
                } else {
                    new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                            .setCancelable(false)
                            .setTitle("Login")
                            .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                            .setMessage("You are not login,So Please Login.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentLogin = new Intent(mContext, LoginActivity.class);
                                    intentLogin.putExtra("frontLogin", "afterLogin");
                                    intentLogin.putExtra("sessionID", sessionIDStr);
                                    intentLogin.putExtra("SearchBy", searchByStr);
                                    intentLogin.putExtra("board", boardStr);
                                    intentLogin.putExtra("stream", streamStr);
                                    intentLogin.putExtra("standard", standardStr);
                                    intentLogin.putExtra("city", locationStr);
                                    intentLogin.putExtra("sessionName", classNameStr);
                                    intentLogin.putExtra("searchType", searchTypeStr);
                                    intentLogin.putExtra("lessionName", subjectStr);
                                    intentLogin.putExtra("gender", genderStr);
                                    intentLogin.putExtra("withOR", wheretoComeStr);
                                    intentLogin.putExtra("ratingLogin", "ratingLoginSession");
                                    intentLogin.putExtra("searchfront", searchfront);
                                    intentLogin.putExtra("sessionType", sessionType);
                                    intentLogin.putExtra("sessionStudent", familysessionStudentStr);
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
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        sessionNameBinding.sessionListRecView.setLayoutManager(mLayoutManager);
        sessionNameBinding.sessionListRecView.setItemAnimator(new DefaultItemAnimator());
        sessionNameBinding.sessionListRecView.setAdapter(sessionDetailAdapter);


    }

    public void setDialogData() {

        for (int i = 0; i < dataResponse.getData().size(); i++) {
//            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("Free")) {
//                session_fee_txt.setText(dataResponse.getData().get(i).getSessionAmount());
//            } else {
//                session_fee_txt.setText("₹ " + dataResponse.getData().get(i).getSessionAmount() + " /-");
//            }
//            session_name_txt.setText(dataResponse.getData().get(i).getSessionName());
//            location_txt.setText(dataResponse.getData().get(i).getAddressLine1()
//                    + ", " + dataResponse.getData().get(i).getRegionName()
//                    + ", " + dataResponse.getData().get(i).getAddressCity()
//                    + ", " + dataResponse.getData().get(i).getAddressState()
//                    + "- " + dataResponse.getData().get(i).getAddressZipCode());
//            session_student_txt.setText(dataResponse.getData().get(i).getName());


            familysessionfeesStr = dataResponse.getData().get(i).getSessionAmount();
            familysessionnameStr = dataResponse.getData().get(i).getSessionName();
            familylocationStr = dataResponse.getData().get(i).getAddressLine1()
                    + ", " + dataResponse.getData().get(i).getRegionName()
                    + ", " + dataResponse.getData().get(i).getAddressCity()
                    + ", " + dataResponse.getData().get(i).getAddressState()
                    + "- " + dataResponse.getData().get(i).getAddressZipCode();
            familysessionStudentStr = dataResponse.getData().get(i).getName();

        }
//        time_txt_view.setText("Date");
//        duration_txt.setText(durationStr);
//        time_txt.setText(sessionDateStr);


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
                        ipayment.putExtra("mode", AppConfiguration.Mode);
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
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
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
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("PaymentStatus", paymentStatusstr);
        return map;
    }

    public void addRating() {
        ArrayList<String> selectedId = new ArrayList<String>();
        String sessionName = "";
        selectedId = sessionDetailAdapter.getSessionDetail();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] splitvalue = selectedId.get(i).split("\\|");
            sessionName = splitvalue[0];
            sessionId = splitvalue[1];
        }

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_dialog_layout, null);
        final RatingBar ratingBar = alertLayout.findViewById(R.id.rating_bar);
        final TextView sessionNametxt = alertLayout.findViewById(R.id.session_name_txt);
        final TextView session_rating_view_txt = alertLayout.findViewById(R.id.session_rating_view_txt);
        final TextView cancel_txt = alertLayout.findViewById(R.id.cancel_txt);
        final TextView confirm_txt = alertLayout.findViewById(R.id.confirm_txt);
        final EditText comment_edt = alertLayout.findViewById(R.id.comment_edt);
        sessionNametxt.setText(sessionName);

        if(!commentStr.equalsIgnoreCase("")){
            comment_edt.setText(commentStr);
        }
        if(rating!=0) {
            ratingBar.setRating(rating);
        }
        if(!sessionratingcommentStr.equalsIgnoreCase("")){
            session_rating_view_txt.setText(sessionratingcommentStr);
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                rating = (int) ratingBar.getRating();
                    if (rating == 1) {
                        session_rating_view_txt.setText("Very poor");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.remarks));
                        sessionratingcommentStr=session_rating_view_txt.getText().toString();
                    } else if (rating == 2) {
                        session_rating_view_txt.setText("Poor");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.remarks));
                        sessionratingcommentStr=session_rating_view_txt.getText().toString();
                    } else if (rating == 3) {
                        session_rating_view_txt.setText("Average");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.rating_bar));
                        sessionratingcommentStr=session_rating_view_txt.getText().toString();
                    } else if (rating == 4) {
                        session_rating_view_txt.setText("Good");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.present));
                        sessionratingcommentStr=session_rating_view_txt.getText().toString();
                    } else if (rating == 5) {
                        session_rating_view_txt.setText("Excellent");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.present));
                        sessionratingcommentStr=session_rating_view_txt.getText().toString();
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
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        rating = ratingBar.getRating();
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
                                Utils.ping(mContext, "Please Select Rate.");
                            }
                        } else {
                            Utils.ping(mContext, getResources().getString(R.string.not_loging));
                        }
                    }
                });
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


                        callSessionListApi();
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
        map.put("SessionID", sessionId);
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("Comment", commentStr);
        map.put("RatingValue", ratingValueStr);

        return map;
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
//                        Utils.ping(mContext, getString(R.string.false_msg));
                        purchaseSessionIDArray = new ArrayList<>();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();

                        if (sessionModel.getData().size() > 0) {
                            purchaseSessionIDArray = new ArrayList<>();
                            for (int i = 0; i < sessionModel.getData().size(); i++) {
                                purchaseSessionIDArray.add(sessionModel.getData().get(i).getSessionID());
                            }
                        } else {
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

    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) sessionNameBinding.getRoot(), false);

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

        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "session");
                startActivity(imyaccount);
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "session");
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
                intent.putExtra("wheretocometype", "session");
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
//        menuBinding = DataBindingUtil.
//                inflate(LayoutInflater.from(mContext), R.layout.layout_menu, (ViewGroup) mySessionBinding.getRoot(), false);
//
//        menuDialog = new Dialog(mContext, R.style.Theme_Dialog);
//        Window window = menuDialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        menuDialog.getWindow().getAttributes().verticalMargin = 0.1F;
//        wlp.gravity = Gravity.TOP;
//        window.setAttributes(wlp);
//
//        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
//
//        menuBinding.userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
//        menuBinding.btnMyReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
//                imyaccount.putExtra("wheretocometype", "mySession");
//                startActivity(imyaccount);
//            }
//        });
//        menuBinding.btnMySession.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent isession = new Intent(mContext, MySession.class);
//                isession.putExtra("wheretocometype", "mySession");
//                startActivity(isession);
//                menuDialog.dismiss();
//            }
//        });
//        menuBinding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                menuDialog.dismiss();
//                changePasswordDialog();
//            }
//        });
//        menuBinding.btnmyfamily.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, FamilyListActivity.class);
//                intent.putExtra("froncontanct", "true");
//                intent.putExtra("wheretocometype", "mySession");
//                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
//                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
//                startActivity(intent);
//            }
//        });
//        menuBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                menuDialog.dismiss();
//                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
//                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
//                        .setMessage("Are you sure you want to logout?")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Utils.setPref(mContext, "coachID", "");
//                                Utils.setPref(mContext, "coachTypeID", "");
//                                Utils.setPref(mContext, "RegisterUserName", "");
//                                Utils.setPref(mContext, "RegisterEmail", "");
//                                Utils.setPref(mContext, "LoginType", "");
//                                Utils.setPref(mContext, "Password", "");
//                                Utils.setPref(mContext, "FamilyID", "");
//                                Utils.setPref(mContext, "location", "");
//                                Utils.setPref(mContext, "sessionName", "");
//                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
//                                intentLogin.putExtra("frontLogin", "beforeLogin");
//                                startActivity(intentLogin);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//
//                            }
//                        })
//                        .setIcon(R.drawable.safari)
//                        .show();
//            }
//        });
        menuDialog.show();
    }
}


