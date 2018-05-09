package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.classsafari.Adapter.ExpandableSelectStudentListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onChlidClick;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.TeacherInfo.ChildDetailModel;
import com.adms.classsafari.Model.TeacherInfo.FamilyDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class FamilyListActivity extends AppCompatActivity {

    public Context mContext;
    ImageView back;
    LinearLayout list_linear;
    ExpandableListView lvExpfamilylist;
    TextView no_record_txt;

    List<FamilyDetailModel> finalFamilyDetail;
    List<String> listDataHeader;
    HashMap<String, List<ChildDetailModel>> listDataChild;
    private int lastExpandedPosition = -1;
    ExpandableSelectStudentListAdapter expandableSelectStudentListAdapter;
    Button addchild_txt;

    //Conformation Dialog
    Dialog confimDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_student_txt_view,
            session_name_txt, location_txt, duration_txt, time_txt, time_txt_view, session_fee_txt;
    String paymentStatusstr, sessionIDStr, familysessionfeesStr, familysessionnameStr,
            familylocationStr, familysessionStudentStr, sessionDateStr, durationStr,
            orderIDStr, contatIDstr, type, familyIdStr = "", familyNameStr = "", locationStr,
            boardStr, standardStr, streamStr, searchTypeStr, subjectStr,
            wheretoComeStr, searchByStr, genderStr, froncontanctStr;
    ArrayList<String> selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);

        mContext = this;
        getIntenetValue();
        init();
        setListner();
        callFamilyListApi();
    }

    public void getIntenetValue() {
        sessionIDStr = getIntent().getStringExtra("sessionID");
        familysessionStudentStr = getIntent().getStringExtra("sessionStudent");
        familylocationStr = getIntent().getStringExtra("location");
        durationStr = getIntent().getStringExtra("duration");
        familysessionfeesStr = getIntent().getStringExtra("sessionfees");
        familysessionnameStr = getIntent().getStringExtra("sessionName");
        sessionDateStr = getIntent().getStringExtra("sessiondate");
        locationStr = getIntent().getStringExtra("city");
        subjectStr = getIntent().getStringExtra("lessionName");
        searchByStr = getIntent().getStringExtra("SearchBy");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        boardStr = getIntent().getStringExtra("board");
        searchTypeStr = getIntent().getStringExtra("searchType");
        genderStr = getIntent().getStringExtra("gender");
        wheretoComeStr = getIntent().getStringExtra("withOR");
        froncontanctStr = getIntent().getStringExtra("froncontanct");
        familyIdStr=getIntent().getStringExtra("familyID");
        familyNameStr=getIntent().getStringExtra("familyNameStr");
    }

    public void init() {
        back = (ImageView) findViewById(R.id.back);
        list_linear = (LinearLayout) findViewById(R.id.list_linear);
        lvExpfamilylist = (ExpandableListView) findViewById(R.id.lvExpfamilylist);
        no_record_txt = (TextView) findViewById(R.id.no_record_txt);
        addchild_txt = (Button) findViewById(R.id.addchild_txt);
    }

    public void setListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!froncontanctStr.equalsIgnoreCase("true")) {
                    Intent intent = new Intent(mContext, SessionName.class);
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
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, SearchByUser.class);
                    startActivity(intent);
                }
            }
        });
        addchild_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!froncontanctStr.equalsIgnoreCase("true")) {
//                    getFamilyID();
                    Intent addchild = new Intent(mContext, AddStudentScreen.class);
                    addchild.putExtra("type", "book");
                    addchild.putExtra("familyID", familyIdStr);
                    addchild.putExtra("familyNameStr", familyNameStr);
                    addchild.putExtra("sessionID", sessionIDStr);
                    addchild.putExtra("duration", durationStr);
                    addchild.putExtra("sessiondate", sessionDateStr);
                    addchild.putExtra("sessionName", familysessionnameStr);
                    addchild.putExtra("location", familylocationStr);
                    addchild.putExtra("sessionfees", familysessionfeesStr);
                    addchild.putExtra("sessionStudent", familysessionStudentStr);
                    addchild.putExtra("city", locationStr);
                    addchild.putExtra("SearchBy", searchByStr);
                    addchild.putExtra("board", boardStr);
                    addchild.putExtra("stream", streamStr);
                    addchild.putExtra("standard", standardStr);
                    addchild.putExtra("searchType", searchTypeStr);
                    addchild.putExtra("lessionName", subjectStr);
                    addchild.putExtra("gender", genderStr);
                    addchild.putExtra("withOR", wheretoComeStr);
                    startActivity(addchild);
                }else{
                    Intent addchild = new Intent(mContext, AddStudentScreen.class);
                    addchild.putExtra("familyNameStr", familyNameStr);
                    addchild.putExtra("type", "menu");
                    startActivity(addchild);
                }
            }
        });
    }


    //Use for Get FamilyList
    public void callFamilyListApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_FamiliyByFamilyID(getFamilyListDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel familyInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (familyInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        list_linear.setVisibility(View.GONE);
                        no_record_txt.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        finalFamilyDetail = familyInfoModel.getData();
                        if (familyInfoModel.getData() != null) {
                            if (familyInfoModel.getData().size() > 0) {
                                list_linear.setVisibility(View.VISIBLE);
                                no_record_txt.setVisibility(View.GONE);
                                fillExpLV();
                                expandableSelectStudentListAdapter = new ExpandableSelectStudentListAdapter(mContext, listDataHeader, listDataChild,froncontanctStr, new onChlidClick() {
                                    @Override
                                    public void getChilClick() {
//                                        getFamilyID();
//
//                                        Intent addchild = new Intent(mContext, AddStudentScreen.class);
//                                        addchild.putExtra("type", "book");
//                                        addchild.putExtra("familyID", familyIdStr);
//                                        addchild.putExtra("familyNameStr", familyNameStr);
//                                        addchild.putExtra("sessionID", sessionIDStr);
//                                        addchild.putExtra("duration", durationStr);
//                                        addchild.putExtra("sessiondate", sessionDateStr);
//                                        addchild.putExtra("sessionName", familysessionnameStr);
//                                        addchild.putExtra("location", familylocationStr);
//                                        addchild.putExtra("sessionfees", familysessionfeesStr);
//                                        addchild.putExtra("sessionStudent", familysessionStudentStr);
//                                        addchild.putExtra("city", locationStr);
//                                        addchild.putExtra("SearchBy", searchByStr);
//                                        addchild.putExtra("board", boardStr);
//                                        addchild.putExtra("stream", streamStr);
//                                        addchild.putExtra("standard", standardStr);
//                                        addchild.putExtra("searchType", searchTypeStr);
//                                        addchild.putExtra("lessionName", subjectStr);
//                                        addchild.putExtra("gender", genderStr);
//                                        addchild.putExtra("withOR", wheretoComeStr);
//                                        startActivity(addchild);
                                    }
                                }, new onViewClick() {
                                    @Override
                                    public void getViewClick() {
                                        ConformSessionDialog();
                                    }
                                });
                                lvExpfamilylist.setAdapter(expandableSelectStudentListAdapter);
                                lvExpfamilylist.expandGroup(0);
                            } else {
                                list_linear.setVisibility(View.GONE);
                                no_record_txt.setVisibility(View.VISIBLE);
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

    private Map<String, String> getFamilyListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FamilyID", Utils.getPref(mContext, "coachTypeID"));
        return map;
    }

    //Use for fill Family List
    public void fillExpLV() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<ChildDetailModel>>();
        for (int i = 0; i < finalFamilyDetail.size(); i++) {
            listDataHeader.add(finalFamilyDetail.get(i).getFirstName() + "|"
                    + finalFamilyDetail.get(i).getLastName() + "|"
                    + finalFamilyDetail.get(i).getContactPhoneNumber() + "|"
                    + finalFamilyDetail.get(i).getFamilyID() + "|"
                    + finalFamilyDetail.get(i).getContactID());
            Log.d("header", "" + listDataHeader);
            ArrayList<ChildDetailModel> row = new ArrayList<ChildDetailModel>();
            for (int j = 0; j < finalFamilyDetail.get(i).getFamilyContact().size(); j++) {
                row.add(finalFamilyDetail.get(i).getFamilyContact().get(j));
                Log.d("row", "" + row);
            }
            listDataChild.put(listDataHeader.get(i), row);
            Log.d("child", "" + listDataChild);
        }
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

        getsessionID();
        session_student_txt.setText(familysessionStudentStr);
        session_name_txt.setText(familysessionnameStr);
        location_txt.setText(familylocationStr);
        duration_txt.setText(durationStr);
        time_txt.setText(sessionDateStr);

        if (familysessionfeesStr.equalsIgnoreCase("0.00")) {
            session_fee_txt.setText("Free");
        } else {
            session_fee_txt.setText("₹ " + familysessionfeesStr + " /-");
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
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("SessionID", sessionIDStr);
        map.put("Amount", familysessionfeesStr);

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

    public void getsessionID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableSelectStudentListAdapter.getSessionDetail();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
//            contatIDstr = selectedId.get(i);
            String[] spilt = selectedId.get(i).split("\\|");
            contatIDstr = spilt[2];
            Utils.setPref(mContext, "FamilyID", contatIDstr);
            session_student_txt.setText(spilt[0] + " " + spilt[1]);
            session_student_txt_view.setText(spilt[3]);
            type = spilt[4];
            Utils.setPref(mContext, "Type", type);
            Log.d("selectedIdStr", contatIDstr);
        }
    }

    public void getFamilyID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableSelectStudentListAdapter.getFamilyID();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] spiltValue = selectedId.get(i).split("\\|");
//            familyIdStr = spiltValue[0];
            familyNameStr = spiltValue[1] + " " + spiltValue[2];
            Log.d("selectedIdStr", familyIdStr);
        }
    }
}
