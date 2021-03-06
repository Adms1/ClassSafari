package com.adms.searchclasses.Fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.searchclasses.Activites.DashBoardActivity;
import com.adms.searchclasses.Adapter.PaymentSucessReportAdapter;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Model.TeacherInfo.FamilyDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.FragmentPaymentReportBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class PaymentReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static boolean isFromDate = false;
    String MonthInt;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    PaymentSucessReportAdapter paymentSucessReportAdapter;
    List<FamilyDetailModel> paymentReportList;
    String startDateStr, endDateStr, flag;
    private FragmentPaymentReportBinding paymentReportBinding;
    private View rootView;
    private Context mContext;
    private DatePickerDialog datePickerDialog;

    public PaymentReportFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        paymentReportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_report, container, false);

        rootView = paymentReportBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mContext = getActivity();

        ((DashBoardActivity) getActivity()).setActionBar(4, "true");
        initViews();
        setListners();

        return rootView;
    }


    //Use for initilize view
    public void initViews() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        paymentReportBinding.txtStartDate.setText(Utils.getPriviousDate());
        paymentReportBinding.txtEndDate.setText(Utils.getTodaysDate());

        callPaymentReportApi();
    }

    //Use for Click Event
    public void setListners() {
        paymentReportBinding.startDateLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(PaymentReportFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        paymentReportBinding.endDateLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(PaymentReportFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        paymentReportBinding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!startDateStr.equalsIgnoreCase("dd/MM/yyyy") && !endDateStr.equalsIgnoreCase("dd/MM/yyyy")) {
                    callPaymentReportApi();
                } else {
                    Utils.ping(mContext, "Please select startdate and enddate");
                }
            }
        });
    }

    //Use for Select Date for display payment report
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
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

        MonthInt = d + "/" + m + "/" + y;
        if (isFromDate) {
            paymentReportBinding.txtStartDate.setText(MonthInt);
        } else {
            paymentReportBinding.txtEndDate.setText(MonthInt);
        }


    }

    //Use for GetPayment Report
    public void callPaymentReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Payment_ByCoachID(getReportDetail(), new retrofit.Callback<TeacherInfoModel>() {
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
                        paymentReportBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        paymentReportBinding.listLinear.setVisibility(View.GONE);
                        paymentReportBinding.headerLinear.setVisibility(View.GONE);
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        paymentReportList = paymentInfoModel.getData();
                        if (paymentInfoModel.getData().size() > 0) {
                            paymentReportBinding.noRecordTxt.setVisibility(View.GONE);
                            paymentReportBinding.listLinear.setVisibility(View.VISIBLE);
                            paymentReportBinding.headerLinear.setVisibility(View.VISIBLE);
                            flag = "1";
                            paymentSucessReportAdapter = new PaymentSucessReportAdapter(mContext, paymentReportList, flag);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            paymentReportBinding.reportRcList.setLayoutManager(mLayoutManager);
                            paymentReportBinding.reportRcList.setItemAnimator(new DefaultItemAnimator());
                            paymentReportBinding.reportRcList.setAdapter(paymentSucessReportAdapter);
                        } else {
                            paymentReportBinding.noRecordTxt.setVisibility(View.VISIBLE);
                            paymentReportBinding.listLinear.setVisibility(View.GONE);
                            paymentReportBinding.headerLinear.setVisibility(View.GONE);
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
        startDateStr = paymentReportBinding.txtStartDate.getText().toString();
        endDateStr = paymentReportBinding.txtEndDate.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Utils.getPref(mContext, "coachID"));
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);

        return map;
    }
}

