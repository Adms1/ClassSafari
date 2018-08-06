package com.adms.searchclasses.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Fragment.PaymentFragment;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.ActivityPaymentSuccessBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;

public class PaymentSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPaymentSuccessBinding paymentSuccessBinding;
    Context mContext;
    SessionDetailModel dataResponse;
    String status, orderIDStr;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentSuccessBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment_success);
        mContext = this;
        //setTypeface();
        init();
        setListner();
    }

    public void init() {
        paymentSuccessBinding.txtUserName.setText(AppConfiguration.UserName);
        if (getIntent().getStringExtra("responseCode").equalsIgnoreCase("0")) {
            status = "success";
            paymentSuccessBinding.linearClick.setVisibility(View.VISIBLE);
            paymentSuccessBinding.sessionNameTxt.setText(AppConfiguration.bookingsubjectName);
            paymentSuccessBinding.tutorNameTxt.setText(AppConfiguration.bookingteacherName);
            paymentSuccessBinding.startDateTxt.setText(AppConfiguration.bookingdate);
            paymentSuccessBinding.endDateTxt.setText(AppConfiguration.bookinhEnddate);

            paymentSuccessBinding.txtTransactionID.setText(getIntent().getStringExtra("transactionId"));
            if (AppConfiguration.bookingamount.equalsIgnoreCase("0.00")) {
                paymentSuccessBinding.txtValue.setText("Free");
            } else {
                paymentSuccessBinding.txtValue.setText("₹" + AppConfiguration.bookingamount);
            }
            String[] spiltPipes = AppConfiguration.bookingtime.split("\\|");
            String[] spiltComma;
            String[] spiltDash;
            Log.d("spilt", "" + spiltPipes.toString());
            for (int j = 0; j < spiltPipes.length; j++) {
                spiltComma = spiltPipes[j].split("\\,");
                spiltDash = spiltComma[1].split("\\-");
                calculateHours(spiltDash[0], spiltDash[1]);
                switch (spiltComma[0]) {
                    case "sun":
                        paymentSuccessBinding.sunTimeTxt.setEnabled(true);
                        paymentSuccessBinding.sundayBtn.setEnabled(true);
                        paymentSuccessBinding.sunTimeTxt.setAlpha(1);
                        paymentSuccessBinding.sundayBtn.setAlpha(1);
                        paymentSuccessBinding.sunTimeTxt.setText(spiltDash[0]);
                        paymentSuccessBinding.sunHoursTxt.setText(SessionDuration);
                        paymentSuccessBinding.sunHoursTxt.setEnabled(true);
                        paymentSuccessBinding.sunHoursTxt.setAlpha(1);
                        break;
                    case "mon":
                        paymentSuccessBinding.monTimeTxt.setEnabled(true);
                        paymentSuccessBinding.mondayBtn.setEnabled(true);
                        paymentSuccessBinding.monTimeTxt.setAlpha(1);
                        paymentSuccessBinding.mondayBtn.setAlpha(1);
                        paymentSuccessBinding.monTimeTxt.setText(spiltDash[0]);
                        paymentSuccessBinding.monHoursTxt.setText(SessionDuration);
                        paymentSuccessBinding.monHoursTxt.setEnabled(true);
                        paymentSuccessBinding.monHoursTxt.setAlpha(1);
                        break;
                    case "tue":
                        paymentSuccessBinding.tuesTimeTxt.setEnabled(true);
                        paymentSuccessBinding.tuesdayBtn.setEnabled(true);
                        paymentSuccessBinding.tuesTimeTxt.setAlpha(1);
                        paymentSuccessBinding.tuesdayBtn.setAlpha(1);
                        paymentSuccessBinding.tuesTimeTxt.setText(spiltDash[0]);
                        paymentSuccessBinding.tuesHoursTxt.setText(SessionDuration);
                        paymentSuccessBinding.tuesHoursTxt.setEnabled(true);
                        paymentSuccessBinding.tuesHoursTxt.setAlpha(1);
                        break;
                    case "wed":
                        paymentSuccessBinding.wedTimeTxt.setEnabled(true);
                        paymentSuccessBinding.wednesdayBtn.setEnabled(true);
                        paymentSuccessBinding.wedTimeTxt.setAlpha(1);
                        paymentSuccessBinding.wednesdayBtn.setAlpha(1);
                        paymentSuccessBinding.wedTimeTxt.setText(spiltDash[0]);
                        paymentSuccessBinding.wedHoursTxt.setText(SessionDuration);
                        paymentSuccessBinding.wedHoursTxt.setEnabled(true);
                        paymentSuccessBinding.wedHoursTxt.setAlpha(1);
                        break;
                    case "thu":
                        paymentSuccessBinding.thurTimeTxt.setEnabled(true);
                        paymentSuccessBinding.thursdayBtn.setEnabled(true);
                        paymentSuccessBinding.thurTimeTxt.setAlpha(1);
                        paymentSuccessBinding.thursdayBtn.setAlpha(1);
                        paymentSuccessBinding.thurTimeTxt.setText(spiltDash[0]);
                        paymentSuccessBinding.thurHoursTxt.setText(SessionDuration);
                        paymentSuccessBinding.thurHoursTxt.setEnabled(true);
                        paymentSuccessBinding.thurHoursTxt.setAlpha(1);
                        break;
                    case "fri":
                        paymentSuccessBinding.friTimeTxt.setEnabled(true);
                        paymentSuccessBinding.fridayBtn.setEnabled(true);
                        paymentSuccessBinding.friTimeTxt.setAlpha(1);
                        paymentSuccessBinding.fridayBtn.setAlpha(1);
                        paymentSuccessBinding.friTimeTxt.setText(spiltDash[0]);
                        paymentSuccessBinding.friHoursTxt.setText(SessionDuration);
                        paymentSuccessBinding.friHoursTxt.setEnabled(true);
                        paymentSuccessBinding.friHoursTxt.setAlpha(1);
                        break;
                    case "sat":
                        paymentSuccessBinding.satTimeTxt.setEnabled(true);
                        paymentSuccessBinding.saturdayBtn.setEnabled(true);
                        paymentSuccessBinding.satTimeTxt.setAlpha(1);
                        paymentSuccessBinding.saturdayBtn.setAlpha(1);
                        paymentSuccessBinding.satTimeTxt.setText(spiltDash[0]);
                        paymentSuccessBinding.satHoursTxt.setText(SessionDuration);
                        paymentSuccessBinding.satHoursTxt.setEnabled(true);
                        paymentSuccessBinding.satHoursTxt.setAlpha(1);
                        break;
                    default:

                }
            }
            paymentSuccessBinding.imvSuccessFail.setImageResource(R.drawable.circle_sucess);
            paymentSuccessBinding.txtSucessFail.setText("Success");
            paymentSuccessBinding.txtSucessFailDesc.setText("Your enrollment was successful");
            paymentSuccessBinding.btnNewCharge.setText("Done");
            paymentSuccessBinding.sessionTxt.setText("ENROLLMENT SUCCESSFUL");

        } else {
            paymentSuccessBinding.linearClick.setVisibility(GONE);
            paymentSuccessBinding.imvSuccessFail.setImageResource(R.drawable.failer);
            status = "fail";
            paymentSuccessBinding.txtSucessFail.setTextColor(getResources().getColor(R.color.absent));
            paymentSuccessBinding.txtSucessFail.setText("Failure");
            paymentSuccessBinding.txtSucessFailDesc.setTextColor(getResources().getColor(R.color.absent));
            paymentSuccessBinding.txtSucessFailDesc.setText("Your payment was not successful\nPlease try again.");
            paymentSuccessBinding.txtTransactionID.setVisibility(GONE);
            paymentSuccessBinding.txtValue.setVisibility(GONE);
            paymentSuccessBinding.btnNewCharge.setText("Try Again");
            paymentSuccessBinding.sessionTxt.setText("ENROLLMENT FAILURE");
            paymentSuccessBinding.btnNewSearch.setVisibility(View.VISIBLE);
        }

        callSessionPaymentApi();
    }

    public void setListner() {

        paymentSuccessBinding.btnNewCharge.setOnClickListener(this);
        paymentSuccessBinding.btnNewSearch.setOnClickListener(this);
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
                                        Intent isearch = new Intent(mContext, SearchByUser.class);
                                        startActivity(isearch);
                                    }
                                })
                                .show();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        AppConfiguration.TeacherSessionIdStr = Utils.getPref(mContext, "sessionId");
                        AppConfiguration.TeacherSessionContactIdStr = Utils.getPref(mContext, "coachID");
                        callpaymentRequestApi();
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
        map.put("SessionID", getIntent().getStringExtra("sessionId"));//contatIDstr  //Utils.getPref(mContext, "coachID")
        return map;
    }

    //Use for Family and Child Session Add PAyment
    public void callSessionPaymentApi() {
        if (Utils.isNetworkConnected(mContext)) {
//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Add_Payment(getSessionPaymentdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (paymentInfoModel == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess() == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("True")) {
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

    private Map<String, String> getSessionPaymentdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("OrderID", getIntent().getStringExtra("order_id"));
        map.put("PaymentID", getIntent().getStringExtra("transactionId"));
        map.put("Status", status);
        return map;
    }

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
//            if (SessionHour > 0) {
//                totalHours.add(SessionHour);
//            }
////            if(SessionMinit>0) {
//            totalMinit.add(SessionMinit);
////            }

            if (SessionMinit > 0) {
                if (SessionMinit < 10) {
                    SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf("0" + SessionMinit + " hrs");
                } else {
                    SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf(SessionMinit + " hrs");

                }
            } else {
                SessionDuration = String.valueOf(SessionHour) + " hrs";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


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
                        ipayment.putExtra("sessionID", getIntent().getStringExtra("sessionId"));
                        ipayment.putExtra("contactID", AppConfiguration.TeacherSessionContactIdStr);//Utils.getPref(mContext, "coachID")
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
        map.put("ContactID", AppConfiguration.TeacherSessionContactIdStr);
        map.put("SessionID", getIntent().getStringExtra("sessionId"));
        map.put("Amount", AppConfiguration.classsessionPrice);
        return map;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCharge:
                if (getIntent().getStringExtra("responseCode").equalsIgnoreCase("0")) {
                    Intent isearchByuser = new Intent(mContext, MySession.class);
                    startActivity(isearchByuser);
                } else {
                    callSessioncapacityApi();
                }
                break;
            case R.id.btnNewSearch:
                Intent isearch = new Intent(mContext, ClassDeatilScreen.class);
                isearch.putExtra("city", "Ahmedabad");
                startActivity(isearch);
                break;
        }
    }
}
