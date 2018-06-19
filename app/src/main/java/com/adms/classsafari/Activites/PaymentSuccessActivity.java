package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityPaymentSuccessBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;

public class PaymentSuccessActivity extends AppCompatActivity {

    ActivityPaymentSuccessBinding paymentSuccessBinding;
    Context mContext;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentSuccessBinding= DataBindingUtil.setContentView(this,R.layout.activity_payment_success);
        mContext = this;
        init();
        setListner();
    }

    public void init() {
        paymentSuccessBinding.txtUserName.setText(AppConfiguration.famliyName);
        if (getIntent().getStringExtra("responseCode").equalsIgnoreCase("0")) {
            status = "success";
            paymentSuccessBinding.imvSuccessFail.setImageResource(R.drawable.circle_sucess);
            paymentSuccessBinding.txtSucessFail.setText("Success");
            paymentSuccessBinding.txtSucessFailDesc.setText("Your transaction was successful");
            paymentSuccessBinding.txtTransactionID.setText(getIntent().getStringExtra("transactionId"));
            paymentSuccessBinding.txtValue.setText(getIntent().getStringExtra("amount"));
            paymentSuccessBinding.btnNewCharge.setText("Done");
        } else {
            paymentSuccessBinding.imvSuccessFail.setImageResource(R.drawable.failer);
            status = "fail";
            paymentSuccessBinding.txtSucessFail.setTextColor(getResources().getColor(R.color.absent));
            paymentSuccessBinding.txtSucessFail.setText("Failure");
            paymentSuccessBinding.txtSucessFailDesc.setTextColor(getResources().getColor(R.color.text_color));
            paymentSuccessBinding.txtSucessFailDesc.setText("Your transaction was not successful\nPlease try again.");
            paymentSuccessBinding.txtTransactionID.setVisibility(GONE);
            paymentSuccessBinding.txtValue.setVisibility(GONE);
            paymentSuccessBinding.btnNewCharge.setText("Try Again");
        }

        callSessionPaymentApi();
    }

    public void setListner() {
        paymentSuccessBinding.btnNewCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent isearchByuser = new Intent(mContext, MySession.class);
                        startActivity(isearchByuser);
                        // Stuff that updates the UI
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
        map.put("OrderID", getIntent().getStringExtra("order_id"));
        map.put("PaymentID", getIntent().getStringExtra("transactionId"));
        map.put("Status", status);
        return map;
    }
}
