package com.adms.searchclasses.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.adms.searchclasses.Activites.DashBoardActivity;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.FragmentPaymentSucessBinding;
import com.adms.searchclasses.databinding.SessiondetailConfirmationDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;


public class PaymentSucessFragment extends Fragment implements View.OnClickListener {

    FragmentPaymentSucessBinding paymentSucessBinding;
    String status, orderIDStr;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;
    private View rootView;
    private Context mContext;

    public PaymentSucessFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        paymentSucessBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_sucess, container, false);
        rootView = paymentSucessBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        if (getArguments().getString("responseCode").equalsIgnoreCase("0")) {
            ((DashBoardActivity) getActivity()).setActionBar(14, "true");
        } else {
            ((DashBoardActivity) getActivity()).setActionBar(14, "false");
        }
        //setTypeface();
        init();
        setListner();
        callSessionPaymentApi();
        return rootView;
    }

    public void setTypeface() {

        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");
        paymentSucessBinding.txtUserName.setTypeface(custom_font);
        paymentSucessBinding.txtSucessFail.setTypeface(custom_font);
        paymentSucessBinding.txtSucessFailDesc.setTypeface(custom_font);
        paymentSucessBinding.txtTransactionID.setTypeface(custom_font);
        paymentSucessBinding.txtValue.setTypeface(custom_font);
        paymentSucessBinding.btnNewCharge.setTypeface(custom_font);
    }

    public void init() {
//        Log.d("userName", AppConfiguration.UserName);//
        paymentSucessBinding.txtUserName.setText(AppConfiguration.UserName);
        if (getArguments().getString("responseCode").equalsIgnoreCase("0")) {
            status = "success";
            paymentSucessBinding.imvSuccessFail.setImageResource(R.drawable.circle_sucess);
            paymentSucessBinding.txtSucessFail.setText("Success");
            paymentSucessBinding.txtSucessFailDesc.setText("Your enrollment was successful");
            paymentSucessBinding.btnNewCharge.setText("Done");
            callEditSessionApi();
        } else {
            paymentSucessBinding.linearClick.setVisibility(GONE);
            paymentSucessBinding.imvSuccessFail.setImageResource(R.drawable.failer);
            status = "fail";
            paymentSucessBinding.txtSucessFail.setTextColor(getResources().getColor(R.color.absent));
            paymentSucessBinding.txtSucessFail.setText("Failure");
            paymentSucessBinding.txtSucessFailDesc.setTextColor(getResources().getColor(R.color.absent));
            paymentSucessBinding.txtSucessFailDesc.setText("Your payment was not successful\nPlease try again.");
            paymentSucessBinding.txtTransactionID.setVisibility(GONE);
            paymentSucessBinding.txtValue.setVisibility(GONE);
            paymentSucessBinding.btnNewCharge.setText("Try Again");
            paymentSucessBinding.btnNewSearch.setVisibility(View.VISIBLE);
        }
    }

    public void setListner() {
        paymentSucessBinding.btnNewCharge.setOnClickListener(this);
        paymentSucessBinding.btnNewSearch.setOnClickListener(this);

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
        map.put("OrderID", getArguments().getString("order_id"));
        map.put("PaymentID", getArguments().getString("transactionId"));
        map.put("Status", status);
        return map;
    }

    //Use for EditSession
    public void callEditSessionApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionDetailBySessionID(getEditSessionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel editsessionInfo, Response response) {
                    Utils.dismissDialog();
                    if (editsessionInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (editsessionInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (editsessionInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (editsessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (editsessionInfo.getData().size() > 0) {
//                            paymentSucessBinding.subjectLinear.setVisibility(View.VISIBLE);
//                            paymentSucessBinding.teacherNameLinear.setVisibility(View.VISIBLE);
//                            paymentSucessBinding.dateLinear.setVisibility(View.VISIBLE);
//                            paymentSucessBinding.timeLinear.setVisibility(View.VISIBLE);
//                            paymentSucessBinding.transcationLinear.setVisibility(View.VISIBLE);
//                            paymentSucessBinding.amountLinear.setVisibility(View.VISIBLE);
paymentSucessBinding.linearClick.setVisibility(View.VISIBLE);
                            paymentSucessBinding.sessionNameTxt.setText(editsessionInfo.getData().get(0).getLessionTypeName());
                            paymentSucessBinding.tutorNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
                            paymentSucessBinding.startDateTxt.setText(editsessionInfo.getData().get(0).getStartDate());
                            paymentSucessBinding.endDateTxt.setText(editsessionInfo.getData().get(0).getEndDate());

//                            if (editsessionInfo.getData().get(0).getSchedule().contains("|")) {
//                                String[] splitvalue = editsessionInfo.getData().get(0).getSchedule().split("\\|");
//                                String[] time = splitvalue[1].split("\\,");
//                                String[] splitTime = time[1].split("\\-");
//                                paymentSucessBinding.txttime.setText(splitTime[0]);
//                            }else{
//                                String[]time=editsessionInfo.getData().get(0).getSchedule().split("\\,");
//                                String[]splitTime=time[1].split("\\-");
//                                paymentSucessBinding.txttime.setText(splitTime[0]);
//                            }
                            paymentSucessBinding.txtTransactionID.setText(getArguments().getString("transactionId"));
                            if (editsessionInfo.getData().get(0).getSessionAmount().equalsIgnoreCase("0.00")) {
                                paymentSucessBinding.txtValue.setText("Free");
                            } else {
                                paymentSucessBinding.txtValue.setText("₹" + getArguments().getString("amount"));
                            }

                            String[] spiltPipes = editsessionInfo.getData().get(0).getSchedule().split("\\|");
                            String[] spiltComma;
                            String[] spiltDash;
                            Log.d("spilt", "" + spiltPipes.toString());
                            for (int j = 0; j < spiltPipes.length; j++) {
                                spiltComma = spiltPipes[j].split("\\,");
                                spiltDash = spiltComma[1].split("\\-");
                                calculateHours(spiltDash[0], spiltDash[1]);
                                switch (spiltComma[0]) {
                                    case "sun":
                                        paymentSucessBinding.sunTimeTxt.setEnabled(true);
                                        paymentSucessBinding.sundayBtn.setEnabled(true);
                                        paymentSucessBinding.sunTimeTxt.setAlpha(1);
                                        paymentSucessBinding.sundayBtn.setAlpha(1);
                                        paymentSucessBinding.sunTimeTxt.setText(spiltDash[0]);
                                        paymentSucessBinding.sunHoursTxt.setText(SessionDuration);
                                        paymentSucessBinding.sunHoursTxt.setEnabled(true);
                                        paymentSucessBinding.sunHoursTxt.setAlpha(1);
                                        break;
                                    case "mon":
                                        paymentSucessBinding.monTimeTxt.setEnabled(true);
                                        paymentSucessBinding.mondayBtn.setEnabled(true);
                                        paymentSucessBinding.monTimeTxt.setAlpha(1);
                                        paymentSucessBinding.mondayBtn.setAlpha(1);
                                        paymentSucessBinding.monTimeTxt.setText(spiltDash[0]);
                                        paymentSucessBinding.monHoursTxt.setText(SessionDuration);
                                        paymentSucessBinding.monHoursTxt.setEnabled(true);
                                        paymentSucessBinding.monHoursTxt.setAlpha(1);
                                        break;
                                    case "tue":
                                        paymentSucessBinding.tuesTimeTxt.setEnabled(true);
                                        paymentSucessBinding.tuesdayBtn.setEnabled(true);
                                        paymentSucessBinding.tuesTimeTxt.setAlpha(1);
                                        paymentSucessBinding.tuesdayBtn.setAlpha(1);
                                        paymentSucessBinding.tuesTimeTxt.setText(spiltDash[0]);
                                        paymentSucessBinding.tuesHoursTxt.setText(SessionDuration);
                                        paymentSucessBinding.tuesHoursTxt.setEnabled(true);
                                        paymentSucessBinding.tuesHoursTxt.setAlpha(1);
                                        break;
                                    case "wed":
                                        paymentSucessBinding.wedTimeTxt.setEnabled(true);
                                        paymentSucessBinding.wednesdayBtn.setEnabled(true);
                                        paymentSucessBinding.wedTimeTxt.setAlpha(1);
                                        paymentSucessBinding.wednesdayBtn.setAlpha(1);
                                        paymentSucessBinding.wedTimeTxt.setText(spiltDash[0]);
                                        paymentSucessBinding.wedHoursTxt.setText(SessionDuration);
                                        paymentSucessBinding.wedHoursTxt.setEnabled(true);
                                        paymentSucessBinding.wedHoursTxt.setAlpha(1);
                                        break;
                                    case "thu":
                                        paymentSucessBinding.thurTimeTxt.setEnabled(true);
                                        paymentSucessBinding.thursdayBtn.setEnabled(true);
                                        paymentSucessBinding.thurTimeTxt.setAlpha(1);
                                        paymentSucessBinding.thursdayBtn.setAlpha(1);
                                        paymentSucessBinding.thurTimeTxt.setText(spiltDash[0]);
                                        paymentSucessBinding.thurHoursTxt.setText(SessionDuration);
                                        paymentSucessBinding.thurHoursTxt.setEnabled(true);
                                        paymentSucessBinding.thurHoursTxt.setAlpha(1);
                                        break;
                                    case "fri":
                                        paymentSucessBinding.friTimeTxt.setEnabled(true);
                                        paymentSucessBinding.fridayBtn.setEnabled(true);
                                        paymentSucessBinding.friTimeTxt.setAlpha(1);
                                        paymentSucessBinding.fridayBtn.setAlpha(1);
                                        paymentSucessBinding.friTimeTxt.setText(spiltDash[0]);
                                        paymentSucessBinding.friHoursTxt.setText(SessionDuration);
                                        paymentSucessBinding.friHoursTxt.setEnabled(true);
                                        paymentSucessBinding.friHoursTxt.setAlpha(1);
                                        break;
                                    case "sat":
                                        paymentSucessBinding.satTimeTxt.setEnabled(true);
                                        paymentSucessBinding.saturdayBtn.setEnabled(true);
                                        paymentSucessBinding.satTimeTxt.setAlpha(1);
                                        paymentSucessBinding.saturdayBtn.setAlpha(1);
                                        paymentSucessBinding.satTimeTxt.setText(spiltDash[0]);
                                        paymentSucessBinding.satHoursTxt.setText(SessionDuration);
                                        paymentSucessBinding.satHoursTxt.setEnabled(true);
                                        paymentSucessBinding.satHoursTxt.setAlpha(1);
                                        break;
                                    default:

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

    private Map<String, String> getEditSessionDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Utils.getPref(mContext, "coachID"));//coachIdStr
        map.put("SessionID", getArguments().getString("sessionId"));
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
                                        Fragment fragment = new SessionFragment();
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.frame, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                })
                                .show();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
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
        map.put("SessionID", AppConfiguration.TeacherSessionIdStr);//contatIDstr  //Utils.getPref(mContext, "coachID")
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
                        Fragment fragment = new PaymentFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("orderID", orderIDStr);
                        args.putString("amount", AppConfiguration.SessionPrice);
                        args.putString("mode", AppConfiguration.Mode);
                        args.putString("username", AppConfiguration.UserName);
                        args.putString("sessionID", AppConfiguration.TeacherSessionIdStr);
                        args.putString("contactID", AppConfiguration.TeacherSessionContactIdStr);
                        args.putString("type", getArguments().getString("type"));
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

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
        map.put("SessionID", AppConfiguration.TeacherSessionIdStr);
        map.put("Amount", AppConfiguration.SessionPrice);
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
//            totalHours.add(SessionHour);
//            totalMinit.add(SessionMinit);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCharge:
                if (getArguments().getString("responseCode").equalsIgnoreCase("0")) {
//                    Fragment fragment = new SessionFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.frame, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                    Intent i = new Intent(getActivity(), DashBoardActivity.class);
                    startActivity(i);
                } else {
                    callSessioncapacityApi();
                }
                break;
            case R.id.btnNewSearch:
//                Fragment fragment = new SessionFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                Intent i = new Intent(getActivity(), DashBoardActivity.class);
                startActivity(i);
                break;
        }
    }
}

