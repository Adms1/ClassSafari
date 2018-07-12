package com.adms.classsafari.Fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioGroup;

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


public class BankDetailFragment extends Fragment implements View.OnClickListener {

    FragmentBankDetailBinding bankDetailBinding;
    String bankNameStr, accountNameStr, accountNumberStr, accountTypeStr = "", accountCodeStr;
    TeacherInfoModel bankDetailResponse;
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
        ((DashBoardActivity) getActivity()).setActionBar(2, "true");

        init();
        setListner();
        callGetBankDetailApi();
        return rootView;
    }


    public void init() {

    }

    public void setListner() {
        bankDetailBinding.submitBtn.setOnClickListener(this);
        bankDetailBinding.emailTxt.setOnClickListener(this);
        bankDetailBinding.accountTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = bankDetailBinding.accountTypeRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.saving_rb:
                        accountTypeStr = bankDetailBinding.savingRb.getTag().toString();
                        break;
                    case R.id.current_rb:
                        accountTypeStr = bankDetailBinding.currentRb.getTag().toString();
                        break;
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
                        if (bankDetailBinding.submitBtn.getText().toString().equalsIgnoreCase("Update")) {
                            Utils.ping(mContext, getString(R.string.bank_usucees));
                        } else {
                            Utils.ping(mContext, getString(R.string.bank_sucees));
                        }
                        Fragment fragment = new SessionFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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

    //Use for Get Bank detail
    public void callGetBankDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {
//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_BankDetail_By_CoachID(getGetBankdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel bankInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (bankInfoModel == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (bankInfoModel.getSuccess() == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (bankInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (bankInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        bankDetailResponse = bankInfoModel;
                        Utils.dismissDialog();
                        setBankData();
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

    private Map<String, String> getGetBankdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Utils.getPref(mContext, "coachID"));
        return map;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                getInsertValue();
                if (!accountTypeStr.equalsIgnoreCase("")) {
                    if (!bankNameStr.equalsIgnoreCase("")) {
                        if (!accountNameStr.equalsIgnoreCase("")) {
                            if (!accountNumberStr.equalsIgnoreCase("")) {
                                if (accountNumberStr.length() >= 10) {
                                    if (!accountCodeStr.equalsIgnoreCase("")) {
                                        callBankDetailApi();
                                    } else {
                                        bankDetailBinding.codeEdt.setError("Please enter IFSC code");
                                    }
                                } else {
                                    bankDetailBinding.accountNumberEdt.setError("Please enter valid account number");
                                }
                            } else {
                                bankDetailBinding.accountNumberEdt.setError("Please enter account number");
                            }
                        } else {
                            bankDetailBinding.accountNameEdt.setError("Please enter name on account");
                        }
                    } else {
                        bankDetailBinding.bankNameEdt.setError("Please enter bank name");
                    }
                } else {
                    Utils.ping(mContext, "Please select type of account");
                }

                break;
            case R.id.email_txt:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {bankDetailBinding.emailTxt.getText().toString()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                mContext.startActivity(Intent.createChooser(intent, "Send mail"));
                break;
        }
    }

    public void setBankData() {
        for (int i = 0; i < bankDetailResponse.getData().size(); i++) {
            bankDetailBinding.bankNameEdt.setText(bankDetailResponse.getData().get(i).getBankName());
            bankDetailBinding.accountNameEdt.setText(bankDetailResponse.getData().get(i).getNameonAccount());
            bankDetailBinding.accountNumberEdt.setText(bankDetailResponse.getData().get(i).getAccountNumber());
            bankDetailBinding.codeEdt.setText(bankDetailResponse.getData().get(i).getIFCCode());
            if (bankDetailResponse.getData().get(i).getTypeofAccount() == 1) {
                bankDetailBinding.savingRb.setChecked(true);
            } else {
                bankDetailBinding.currentRb.setChecked(true);
            }
        }
        bankDetailBinding.submitBtn.setText("Update");
    }
}

