package com.adms.classsafari.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.FragmentBankDetailBinding;
import com.adms.classsafari.databinding.FragmentPaymentSucessBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;


public class BankDetailFragment extends Fragment {

    FragmentBankDetailBinding bankDetailBinding;
    String bankNameStr, accountNameStr, accountNumberStr, accountTypeStr, accountCodeStr;
    private View rootView;
    private Context mContext;

    public BankDetailFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bankDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bank_detail, container, false);
        rootView = bankDetailBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(15, "false");

        init();
        setListner();
        return rootView;
    }


    public void init() {

    }

    public void setListner() {
        bankDetailBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInsertValue();
                if (!bankNameStr.equalsIgnoreCase("")) {
                    if (!accountNameStr.equalsIgnoreCase("")) {
                        if (!accountNumberStr.equalsIgnoreCase("")) {
                            if (!accountTypeStr.equalsIgnoreCase("")) {
                                if (!accountCodeStr.equalsIgnoreCase("")) {
                                    callBankDetailApi();
                                } else {
                                    bankDetailBinding.codeEdt.setError("Please enter IFSC code");
                                }
                            } else {
                                Utils.ping(mContext, "Please select account type");
                            }
                        } else {
                            bankDetailBinding.accountNumberEdt.setError("Please enter account number");
                        }
                    } else {
                        bankDetailBinding.accountNameEdt.setError("Please enter account name");
                    }
                } else {
                    bankDetailBinding.bankNameEdt.setError("Please enter bank name");
                }


            }
        });
    }

    //Use for Add Bank detail
    public void callBankDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {
//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Add_BankDetail(getBankdetail(), new retrofit.Callback<TeacherInfoModel>() {
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

    private Map<String, String> getBankdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Utils.getPref(mContext, "coachID"));
        map.put("BankName", bankNameStr);
        map.put("NameonAccount", accountNameStr);
        map.put("TypeofAccount", accountTypeStr);
        map.put("AccountNumber", accountNumberStr);
        map.put("IFCCode", accountCodeStr);
        return map;
    }

    public void getInsertValue() {
        bankNameStr = bankDetailBinding.bankNameEdt.getText().toString();
        accountNameStr = bankDetailBinding.accountNameEdt.getText().toString();
        accountNumberStr = bankDetailBinding.accountNumberEdt.getText().toString();
        accountCodeStr = bankDetailBinding.codeEdt.getText().toString();
    }
}

