package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.adms.classsafari.Adapter.SessionDetailAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySessionNameNewBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionName extends AppCompatActivity {
    ActivitySessionNameNewBinding sessionNameBinding;
    Context mContext;
    SessionDetailAdapter sessionDetailAdapter;
    List<sessionDataModel> arrayList;
    Dialog confimDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_student_txt_view, session_name_txt, location_txt, duration_txt, time_txt, session_fee_txt;
    String sessionIDStr;
    SessionDetailModel dataResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionNameBinding = DataBindingUtil.setContentView(this, R.layout.activity_session_name_new);
        mContext = SessionName.this;

        sessionIDStr = getIntent().getStringExtra("sessionID");
        init();
        setListner();
    }

    public void init() {

        callSessionListApi();

    }

    public void setListner() {
        sessionNameBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, ClassDeatilScreen.class);
                startActivity(inback);
            }
        });

        sessionNameBinding.bookSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConformSessionDialog();
//                Intent iLogin = new Intent(mContext, LoginActivity.class);
//                startActivity(iLogin);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inback = new Intent(mContext, ClassDeatilScreen.class);
        inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inback);
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
                Intent inLogin = new Intent(SessionName.this, LoginActivity.class);
                inLogin.putExtra("flag", "1");
                startActivity(inLogin);
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
                        Utils.dismissDialog();
                        if (sessionDetailInfo.getData().size() > 0) {
                            dataResponse = sessionDetailInfo;
                            setData();
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

    public void setData() {
        arrayList = new ArrayList<sessionDataModel>();

        for (int i = 0; i < dataResponse.getData().size(); i++) {
            arrayList.add(dataResponse.getData().get(i));
            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                dataResponse.getData().get(i).setSessionAmount("Free");
            }
            sessionNameBinding.priceTxt.setText("₹ " + dataResponse.getData().get(i).getSessionAmount());
        }

        sessionDetailAdapter = new SessionDetailAdapter(mContext, arrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        sessionNameBinding.sessionListRecView.setLayoutManager(mLayoutManager);
        sessionNameBinding.sessionListRecView.setItemAnimator(new DefaultItemAnimator());
        sessionNameBinding.sessionListRecView.setAdapter(sessionDetailAdapter);
    }

    public void setDialogData() {
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                session_fee_txt.setText("Free");
            } else {
                session_fee_txt.setText("₹ " + dataResponse.getData().get(i).getSessionAmount());
            }
            session_name_txt.setText(dataResponse.getData().get(i).getSessionName());
            location_txt.setText(dataResponse.getData().get(i).getAddressLine1()
                    + ", " + dataResponse.getData().get(i).getRegionName()
                    + ", " + dataResponse.getData().get(i).getAddressCity()
                    + ", " + dataResponse.getData().get(i).getAddressState()
                    + "- " + dataResponse.getData().get(i).getAddressZipCode());
            duration_txt.setText(AppConfiguration.SessionDuration);
            time_txt.setText(AppConfiguration.SessionTime);
            session_student_txt.setText(dataResponse.getData().get(i).getName());
        }
    }

}
