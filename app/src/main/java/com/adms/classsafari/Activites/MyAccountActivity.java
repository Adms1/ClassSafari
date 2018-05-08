package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.classsafari.Adapter.PaymentSucessReportAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.FamilyDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyAccountActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    Context mContext;
    LinearLayout start_date_linear, end_date_linear, header_linear, list_linear;
    RecyclerView report_rcList;
    TextView no_record_txt, txtStartDate, txtEndDate;
    Button btnShow;
    ImageView back;

    //Use for calender
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    private static boolean isFromDate = false;
    String finalDate, startDateStr, endDateStr,flag;

    //Use for paymentreport List
    List<FamilyDetailModel> paymentReportList;
    PaymentSucessReportAdapter paymentSucessReportAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        mContext = this;

        init();
        setListner();
    }

    public void init() {
        start_date_linear = (LinearLayout) findViewById(R.id.start_date_linear);
        end_date_linear = (LinearLayout) findViewById(R.id.end_date_linear);
        header_linear = (LinearLayout) findViewById(R.id.header_linear);
        list_linear = (LinearLayout) findViewById(R.id.list_linear);

        report_rcList = (RecyclerView) findViewById(R.id.report_rcList);
        no_record_txt = (TextView) findViewById(R.id.no_record_txt);
        txtEndDate = (TextView) findViewById(R.id.txtEndDate);
        txtStartDate = (TextView) findViewById(R.id.txtStartDate);

        back = (ImageView) findViewById(R.id.back);
        btnShow = (Button) findViewById(R.id.btnShow);

//Use for startdate and enddate
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        txtStartDate.setText(Utils.getPriviousDate());
        txtEndDate.setText(Utils.getTodaysDate());
        callPaymentReportApi();
    }

    public void setListner() {
       back.setOnClickListener(this);
       start_date_linear.setOnClickListener(this);
       end_date_linear.setOnClickListener(this);
       btnShow.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mDay = dayOfMonth;
        mMonth = monthOfYear + 1;
        mYear = year;
        String d, m, y;
        d = Integer.toString(mDay);
        m = Integer.toString(mMonth);
        y = Integer.toString(mYear);

        if (mDay < 10) {
            d = "0" + d;
        }
        if (mMonth < 10) {
            m = "0" + m;
        }

        finalDate = d + "/" + m + "/" + y;
        if (isFromDate) {
            txtStartDate.setText(finalDate);
        } else {
            txtEndDate.setText(finalDate);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date_linear:
                isFromDate = true;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(MyAccountActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.end_date_linear:
                isFromDate = false;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(MyAccountActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.btnShow:
                if (!startDateStr.equalsIgnoreCase("dd/MM/yyyy") && !endDateStr.equalsIgnoreCase("dd/MM/yyyy")) {
                    callPaymentReportApi();
                } else {
                    Utils.ping(mContext, "Please Select StartDate and EndDate.");
                }
                break;
            case R.id.back:
                Intent isearchuser = new Intent(mContext, SearchByUser.class);
                startActivity(isearchuser);
                break;
        }
    }

    //Use for GetPayment Report
    public void callPaymentReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Payment_ByContactID(getReportDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (paymentInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        paymentReportList = paymentInfoModel.getData();
                        if (paymentInfoModel.getData().size() > 0) {
                            no_record_txt.setVisibility(View.GONE);
                            list_linear.setVisibility(View.VISIBLE);
                            header_linear.setVisibility(View.VISIBLE);
                            flag="2";
                            paymentSucessReportAdapter = new PaymentSucessReportAdapter(mContext, paymentReportList,flag);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            report_rcList.setLayoutManager(mLayoutManager);
                            report_rcList.setItemAnimator(new DefaultItemAnimator());
                            report_rcList.setAdapter(paymentSucessReportAdapter);
                        } else {
                            no_record_txt.setVisibility(View.VISIBLE);
                            list_linear.setVisibility(View.GONE);
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

    private Map<String, String> getReportDetail() {
        startDateStr = txtStartDate.getText().toString();
        endDateStr = txtEndDate.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);

        return map;
    }
}
