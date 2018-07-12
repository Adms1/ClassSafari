package com.adms.classsafari.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.classsafari.Activites.DashBoardActivity;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.FragmentPaymentSucessBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;


public class PaymentSucessFragment extends Fragment {

    FragmentPaymentSucessBinding paymentSucessBinding;
    String status,orderIDStr;
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
            paymentSucessBinding.imvSuccessFail.setImageResource(R.drawable.failer);
            status = "fail";
            paymentSucessBinding.txtSucessFail.setTextColor(getResources().getColor(R.color.absent));
            paymentSucessBinding.txtSucessFail.setText("Failure");
            paymentSucessBinding.txtSucessFailDesc.setTextColor(getResources().getColor(R.color.absent));
            paymentSucessBinding.txtSucessFailDesc.setText("Your payment was not successful\nPlease try again.");
            paymentSucessBinding.txtTransactionID.setVisibility(GONE);
            paymentSucessBinding.txtValue.setVisibility(GONE);
            paymentSucessBinding.btnNewCharge.setText("Try Again");
        }
    }

    public void setListner() {
        paymentSucessBinding.btnNewCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments().getString("responseCode").equalsIgnoreCase("0")) {
                    Fragment fragment = new SessionFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    callSessioncapacityApi();
                }
            }
        });
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
                            paymentSucessBinding.subjectLinear.setVisibility(View.VISIBLE);
                            paymentSucessBinding.teacherNameLinear.setVisibility(View.VISIBLE);
                            paymentSucessBinding.dateLinear.setVisibility(View.VISIBLE);
                            paymentSucessBinding.timeLinear.setVisibility(View.VISIBLE);
                            paymentSucessBinding.transcationLinear.setVisibility(View.VISIBLE);
                            paymentSucessBinding.amountLinear.setVisibility(View.VISIBLE);

                            paymentSucessBinding.txtsubjectname.setText(editsessionInfo.getData().get(0).getLessionTypeName());
                            paymentSucessBinding.txtteachername.setText(Utils.getPref(mContext, "RegisterUserName"));
                            paymentSucessBinding.txtdate.setText(editsessionInfo.getData().get(0).getStartDate());
                            if (editsessionInfo.getData().get(0).getSchedule().contains("|")) {
                                String[] splitvalue = editsessionInfo.getData().get(0).getSchedule().split("\\|");
                                String[] time = splitvalue[1].split("\\,");
                                String[] splitTime = time[1].split("\\-");
                                paymentSucessBinding.txttime.setText(splitTime[0]);
                            }else{
                                String[]time=editsessionInfo.getData().get(0).getSchedule().split("\\,");
                                String[]splitTime=time[1].split("\\-");
                                paymentSucessBinding.txttime.setText(splitTime[0]);
                            }
                            paymentSucessBinding.txtTransactionID.setText(getArguments().getString("transactionId"));
                            if (editsessionInfo.getData().get(0).getSessionAmount().equalsIgnoreCase("0.00")) {
                                paymentSucessBinding.txtValue.setText("Free");
                            }
                            else {
                                paymentSucessBinding.txtValue.setText(getArguments().getString("amount"));
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
                                .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
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
                                .setIcon(R.drawable.safari)
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
}

