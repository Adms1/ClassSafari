package com.adms.classsafari.Fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.classsafari.Activites.DashBoardActivity;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
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
    String status;
    private View rootView;
    private Context mContext;

    public PaymentSucessFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        paymentSucessBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_sucess, container, false);
        rootView = paymentSucessBinding.getRoot();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(14, "false");
        setTypeface();
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
        Log.d("userName", AppConfiguration.UserName);
        paymentSucessBinding.txtUserName.setText(AppConfiguration.UserName);
        if (getArguments().getString("responseCode").equalsIgnoreCase("0")) {
            status = "success";
            paymentSucessBinding.imvSuccessFail.setImageResource(R.drawable.circle_sucess);
            paymentSucessBinding.txtSucessFail.setText("Success");
            paymentSucessBinding.txtSucessFailDesc.setText("Your transaction was successful");
            paymentSucessBinding.txtTransactionID.setText(getArguments().getString("transactionId"));
            paymentSucessBinding.txtValue.setText(getArguments().getString("amount"));
            paymentSucessBinding.btnNewCharge.setText("Done");
        } else {
            paymentSucessBinding.imvSuccessFail.setImageResource(R.drawable.failer);
            status = "fail";
            paymentSucessBinding.txtSucessFail.setTextColor(getResources().getColor(R.color.absent));
            paymentSucessBinding.txtSucessFail.setText("Failure");
            paymentSucessBinding.txtSucessFailDesc.setTextColor(getResources().getColor(R.color.text_color));
            paymentSucessBinding.txtSucessFailDesc.setText("Your transaction was not successful\nPlease try again.");
            paymentSucessBinding.txtTransactionID.setVisibility(GONE);
            paymentSucessBinding.txtValue.setVisibility(GONE);
            paymentSucessBinding.btnNewCharge.setText("Try Again");
        }
    }

    public void setListner() {
        paymentSucessBinding.btnNewCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
}

