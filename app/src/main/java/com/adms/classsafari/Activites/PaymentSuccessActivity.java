package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;

public class PaymentSuccessActivity extends AppCompatActivity {

    Context mContext;
    String status;
    TextView txtTransactionID, txtValue, txtSucessFailDesc, txtSucessFail, txtUserName;
    RelativeLayout rlInnerBox;
    ImageView imvSuccessFail;
    Button btnNewCharge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        mContext = this;
        init();
        setListner();
    }

    public void init() {
        txtTransactionID = (TextView) findViewById(R.id.txtTransactionID);
        txtValue = (TextView) findViewById(R.id.txtValue);
        txtSucessFailDesc = (TextView) findViewById(R.id.txtSucessFailDesc);
        txtSucessFail = (TextView) findViewById(R.id.txtSucessFail);
        imvSuccessFail = (ImageView) findViewById(R.id.imvSuccessFail);
        btnNewCharge = (Button) findViewById(R.id.btnNewCharge);
        txtUserName = (TextView) findViewById(R.id.txtUserName);

        txtUserName.setText(AppConfiguration.famliyName);
        if (getIntent().getStringExtra("responseCode").equalsIgnoreCase("0")) {
            status = "success";
            imvSuccessFail.setImageResource(R.drawable.circle_sucess);
            txtSucessFail.setText("Success");
            txtSucessFailDesc.setText("Your transaction was successful");
            txtTransactionID.setText(getIntent().getStringExtra("transactionId"));
            txtValue.setText(getIntent().getStringExtra("amount"));
            btnNewCharge.setText("Done");
        } else {
            imvSuccessFail.setImageResource(R.drawable.failer);
            status = "fail";
            txtSucessFail.setTextColor(getResources().getColor(R.color.absent));
            txtSucessFail.setText("Failure");
            txtSucessFailDesc.setTextColor(getResources().getColor(R.color.text_color));
            txtSucessFailDesc.setText("Your transaction was not successful\nPlease try again.");
            txtTransactionID.setVisibility(GONE);
            txtValue.setVisibility(GONE);
            btnNewCharge.setText("Try Again");
        }

        callSessionPaymentApi();
    }

    public void setListner() {
        btnNewCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isearchByuser = new Intent(mContext, MySession.class);
                startActivity(isearchByuser);
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
