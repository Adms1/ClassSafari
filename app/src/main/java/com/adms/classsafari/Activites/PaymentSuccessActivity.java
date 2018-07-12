package com.adms.classsafari.Activites;

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

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Fragment.PaymentFragment;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityPaymentSuccessBinding;

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

public class PaymentSuccessActivity extends AppCompatActivity {

    ActivityPaymentSuccessBinding paymentSuccessBinding;
    Context mContext;
    SessionDetailModel dataResponse;
    String status,orderIDStr;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentSuccessBinding= DataBindingUtil.setContentView(this,R.layout.activity_payment_success);
        mContext = this;
        //setTypeface();
        init();
        setListner();
    }
    public void setTypeface() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/TitilliumWeb-Regular.ttf");

        paymentSuccessBinding.sessionTxt.setTypeface(custom_font);
        paymentSuccessBinding.txtUserName.setTypeface(custom_font);
        paymentSuccessBinding.txtSucessFail.setTypeface(custom_font);
        paymentSuccessBinding.txtSucessFailDesc.setTypeface(custom_font);
        paymentSuccessBinding.txtTransactionID.setTypeface(custom_font);
        paymentSuccessBinding.txtValue.setTypeface(custom_font);
        paymentSuccessBinding.btnNewCharge.setTypeface(custom_font);
//Class you are trying to book is already full.Please try another class.
    }

    public void init() {
        paymentSuccessBinding.txtUserName.setText(Utils.getPref(mContext,"RegisterUserName"));
        if (getIntent().getStringExtra("responseCode").equalsIgnoreCase("0")) {
            status = "success";
         //   callSessionListApi();
            paymentSuccessBinding.subjectLinear.setVisibility(View.VISIBLE);
            paymentSuccessBinding.teacherNameLinear.setVisibility(View.VISIBLE);
            paymentSuccessBinding.dateLinear.setVisibility(View.VISIBLE);
            paymentSuccessBinding.timeLinear.setVisibility(View.VISIBLE);
            paymentSuccessBinding.transcationLinear.setVisibility(View.VISIBLE);
            paymentSuccessBinding.amountLinear.setVisibility(View.VISIBLE);

            paymentSuccessBinding.txtsubjectname.setText(AppConfiguration.bookingsubjectName);
                paymentSuccessBinding.txtteachername.setText(AppConfiguration.bookingteacherName);
            paymentSuccessBinding.txtdate.setText(AppConfiguration.bookingdate);
            if (AppConfiguration.bookingtime.contains("|")) {
                String[] splitvalue =AppConfiguration.bookingtime.split("\\|");
                String[]time=splitvalue[1].split("\\,");
                String[]splitTime=time[1].split("\\-");
                paymentSuccessBinding.txttime.setText(splitTime[0]);
            }else{
                String[]time=AppConfiguration.bookingtime.split("\\,");
                String[]splitTime=time[1].split("\\-");
                paymentSuccessBinding.txttime.setText(splitTime[0]);
            }

            paymentSuccessBinding.txtTransactionID.setText(getIntent().getStringExtra("transactionId"));
            if (AppConfiguration.bookingamount.equalsIgnoreCase("0.00")) {
                paymentSuccessBinding.txtValue.setText("Free");
            }
            else {
                paymentSuccessBinding.txtValue.setText("₹"+AppConfiguration.bookingamount);
            }

            paymentSuccessBinding.imvSuccessFail.setImageResource(R.drawable.circle_sucess);
            paymentSuccessBinding.txtSucessFail.setText("Success");
            paymentSuccessBinding.txtSucessFailDesc.setText("Your enrollment was successful");
            paymentSuccessBinding.btnNewCharge.setText("Done");
            paymentSuccessBinding.sessionTxt.setText("ENROLLMENT SUCCESSFUL");

        } else {
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
        }

        callSessionPaymentApi();
    }

    public void setListner() {

        paymentSuccessBinding.btnNewCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("responseCode").equalsIgnoreCase("0")) {
                    Intent isearchByuser = new Intent(mContext, MySession.class);
                    startActivity(isearchByuser);
                }else{
                    callSessioncapacityApi();
                }
                        // Stuff that updates the UI
            }
        });
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
                                .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                                .setMessage(getResources().getString(R.string.fail_msg))
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent isearch=new Intent(mContext,SearchByUser.class);
                                        startActivity(isearch);
                                    }
                                })
                                .setIcon(R.drawable.safari)
                                .show();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        AppConfiguration.TeacherSessionIdStr = Utils.getPref(mContext,"sessionId");
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
        map.put("SessionID",getIntent().getStringExtra("sessionId"));//contatIDstr  //Utils.getPref(mContext, "coachID")
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

                                paymentSuccessBinding.subjectLinear.setVisibility(View.VISIBLE);
                                paymentSuccessBinding.teacherNameLinear.setVisibility(View.VISIBLE);
                                paymentSuccessBinding.dateLinear.setVisibility(View.VISIBLE);
                                paymentSuccessBinding.timeLinear.setVisibility(View.VISIBLE);
                                paymentSuccessBinding.transcationLinear.setVisibility(View.VISIBLE);
                                paymentSuccessBinding.amountLinear.setVisibility(View.VISIBLE);

                                paymentSuccessBinding.txtsubjectname.setText(dataResponse.getData().get(j).getLessionTypeName());
                                paymentSuccessBinding.txtteachername.setText(Utils.getPref(mContext, "RegisterUserName"));
                                paymentSuccessBinding.txtdate.setText(dataResponse.getData().get(j).getStartDate());
                                if (dataResponse.getData().get(j).getSchedule().contains("|")) {
                                    String[] splitvalue = dataResponse.getData().get(j).getSchedule().split("\\|");
                                    String[]time=splitvalue[1].split("\\,");
                                    String[]splitTime=time[1].split("\\-");
                                    paymentSuccessBinding.txttime.setText(splitTime[0]);
                                }else{
                                    String[]time=dataResponse.getData().get(j).getSchedule().split("\\,");
                                    String[]splitTime=time[1].split("\\-");
                                    paymentSuccessBinding.txttime.setText(splitTime[0]);
                                }

                                paymentSuccessBinding.txtTransactionID.setText(getIntent().getStringExtra("transactionId"));
                                if (dataResponse.getData().get(j).getSessionAmount().equalsIgnoreCase("0.00")) {
                                    paymentSuccessBinding.txtValue.setText("Free");
                                }
                                else {
                                    paymentSuccessBinding.txtValue.setText("₹"+dataResponse.getData().get(j).getSessionAmount());
                                }

                                paymentSuccessBinding.imvSuccessFail.setImageResource(R.drawable.circle_sucess);
                                paymentSuccessBinding.txtSucessFail.setText("Success");
                                paymentSuccessBinding.txtSucessFailDesc.setText("Your enrollment was successful");
                                paymentSuccessBinding.btnNewCharge.setText("Done");
                                paymentSuccessBinding.sessionTxt.setText("ENROLLMENT SUCCESFUL");
//                                paymentSuccessBinding.sessionNameTxt.setText(dataResponse.getData().get(j).getSessionName());
//                                paymentSuccessBinding.ratingBar.setRating(Float.parseFloat(dataResponse.getData().get(j).getRating()));
//                                paymentSuccessBinding.tutorNameTxt.setText(dataResponse.getData().get(j).getName());
//                                paymentSuccessBinding.locationTxt.setText(dataResponse.getData().get(j).getRegionName());
//                                paymentSuccessBinding.startDateTxt.setText(dataResponse.getData().get(j).getStartDate());
//                                paymentSuccessBinding.endDateTxt.setText(dataResponse.getData().get(j).getEndDate());
//                                paymentSuccessBinding.priceTxt.setText("₹"+dataResponse.getData().get(j).getSessionAmount());
//                                if(!dataResponse.getData().get(j).getTotalRatingUser().equalsIgnoreCase("0")) {
//                                    paymentSuccessBinding.ratingUserTxt.setText("( "+dataResponse.getData().get(j).getTotalRatingUser()+" )");
//                                }
//                                AppConfiguration.classsessionPrice = dataResponse.getData().get(j).getSessionAmount();
//                                totalHours = new ArrayList<>();
//                                totalMinit = new ArrayList<>();
//                                String[] spiltPipes = dataResponse.getData().get(j).getSchedule().split("\\|");
//                                String[] spiltComma;
//                                String[] spiltDash;
//                                Log.d("spilt", "" + spiltPipes.toString());
//                                for (int i = 0; i < spiltPipes.length; i++) {
//                                    spiltComma = spiltPipes[i].split("\\,");
//                                    spiltDash = spiltComma[1].split("\\-");
//                                    calculateHours(spiltDash[0], spiltDash[1]);
//                                    dataResponse.getData().get(j).setDateTime(spiltDash[0]);
//                                    Log.d("DateTime", spiltDash[0]);
//                                    switch (spiltComma[0]) {
//                                        case "sun":
//                                            paymentSuccessBinding.sunHoursTxt.setEnabled(true);
//                                            paymentSuccessBinding.sunHoursTxt.setAlpha(1);
//                                            paymentSuccessBinding.sunTimeTxt.setEnabled(true);
//                                            paymentSuccessBinding.sundayBtn.setEnabled(true);
//                                            paymentSuccessBinding.sunTimeTxt.setAlpha(1);
//                                            paymentSuccessBinding.sundayBtn.setAlpha(1);
//                                            paymentSuccessBinding.sunTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
//                                            paymentSuccessBinding.sunHoursTxt.setText(SessionDuration);
//                                            break;
//                                        case "mon":
//                                            paymentSuccessBinding.monHoursTxt.setEnabled(true);
//                                            paymentSuccessBinding.monHoursTxt.setAlpha(1);
//                                            paymentSuccessBinding.monTimeTxt.setEnabled(true);
//                                            paymentSuccessBinding.mondayBtn.setEnabled(true);
//                                            paymentSuccessBinding.monTimeTxt.setAlpha(1);
//                                            paymentSuccessBinding.mondayBtn.setAlpha(1);
//                                            paymentSuccessBinding.monTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
//                                            paymentSuccessBinding.monHoursTxt.setText(SessionDuration);
//                                            break;
//                                        case "tue":
//                                            paymentSuccessBinding.tuesHoursTxt.setEnabled(true);
//                                            paymentSuccessBinding.tuesHoursTxt.setAlpha(1);
//                                            paymentSuccessBinding.tuesTimeTxt.setEnabled(true);
//                                            paymentSuccessBinding.tuesdayBtn.setEnabled(true);
//                                            paymentSuccessBinding.tuesTimeTxt.setAlpha(1);
//                                            paymentSuccessBinding.tuesdayBtn.setAlpha(1);
//                                            paymentSuccessBinding.tuesTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
//                                            paymentSuccessBinding.tuesHoursTxt.setText(SessionDuration);
//                                            break;
//                                        case "wed":
//                                            paymentSuccessBinding.wedHoursTxt.setEnabled(true);
//                                            paymentSuccessBinding.wedHoursTxt.setAlpha(1);
//                                            paymentSuccessBinding.wedTimeTxt.setEnabled(true);
//                                            paymentSuccessBinding.wednesdayBtn.setEnabled(true);
//                                            paymentSuccessBinding.wedTimeTxt.setAlpha(1);
//                                            paymentSuccessBinding.wednesdayBtn.setAlpha(1);
//                                            paymentSuccessBinding.wedTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
//                                            paymentSuccessBinding.wedHoursTxt.setText(SessionDuration);
//                                            break;
//                                        case "thu":
//                                            paymentSuccessBinding.thurHoursTxt.setEnabled(true);
//                                            paymentSuccessBinding.thurHoursTxt.setAlpha(1);
//                                            paymentSuccessBinding.thurTimeTxt.setEnabled(true);
//                                            paymentSuccessBinding.thursdayBtn.setEnabled(true);
//                                            paymentSuccessBinding.thurTimeTxt.setAlpha(1);
//                                            paymentSuccessBinding.thursdayBtn.setAlpha(1);
//                                            paymentSuccessBinding.thurTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
//                                            paymentSuccessBinding.thurHoursTxt.setText(SessionDuration);
//                                            break;
//                                        case "fri":
//                                            paymentSuccessBinding.friHoursTxt.setEnabled(true);
//                                            paymentSuccessBinding.friHoursTxt.setAlpha(1);
//                                            paymentSuccessBinding.friTimeTxt.setEnabled(true);
//                                            paymentSuccessBinding.fridayBtn.setEnabled(true);
//                                            paymentSuccessBinding.friTimeTxt.setAlpha(1);
//                                            paymentSuccessBinding.fridayBtn.setAlpha(1);
//                                            paymentSuccessBinding.friTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
//                                            paymentSuccessBinding.friHoursTxt.setText(SessionDuration);
//                                            break;
//                                        case "sat":
//                                            paymentSuccessBinding.satHoursTxt.setEnabled(true);
//                                            paymentSuccessBinding.satHoursTxt.setAlpha(1);
//                                            paymentSuccessBinding.satTimeTxt.setEnabled(true);
//                                            paymentSuccessBinding.saturdayBtn.setEnabled(true);
//                                            paymentSuccessBinding.satTimeTxt.setAlpha(1);
//                                            paymentSuccessBinding.saturdayBtn.setAlpha(1);
//                                            paymentSuccessBinding.satTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
//                                            paymentSuccessBinding.satHoursTxt.setText(SessionDuration);
//                                            break;
//                                        default:
//
//                                    }
//                                }
//                            }
                         }}
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
        map.put("SessionID", Utils.getPref(mContext,"sessionId"));
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

            if(SessionMinit>0){
                if(SessionMinit<10) {
                    SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf("0" + SessionMinit + " hrs");
                }else{SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf(SessionMinit + " hrs");

                }
            }else{
                SessionDuration=String.valueOf(SessionHour)+" hrs";
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
        map.put("ContactID",  AppConfiguration.TeacherSessionContactIdStr);
        map.put("SessionID",getIntent().getStringExtra("sessionId"));
        map.put("Amount",  AppConfiguration.classsessionPrice);
        return map;
    }
}
