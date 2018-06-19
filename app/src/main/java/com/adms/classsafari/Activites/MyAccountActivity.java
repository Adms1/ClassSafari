package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.adms.classsafari.Adapter.PaymentSucessReportAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.FamilyDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityMyAccountBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;
import com.adms.classsafari.databinding.LayoutMenuBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyAccountActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private static boolean isFromDate = false;
    ActivityMyAccountBinding myAccountBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    LayoutMenuBinding menuBinding;
    Context mContext;

    //Use for calender
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    String finalDate, startDateStr, endDateStr, flag, wheretocometypeStr;
    //Use for paymentreport List
    List<FamilyDetailModel> paymentReportList;
    PaymentSucessReportAdapter paymentSucessReportAdapter;

    //Use for menu
    String passWordStr, confirmpassWordStr, currentpasswordStr;
    Dialog menuDialog, changeDialog;
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily;
    TextView userNameTxt;
    //
    Calendar[] daysArray;
    List<Calendar> dayslist = new LinkedList<Calendar>();
    int startDay, startMonth, startYear;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        mContext = this;

        getIntenttValue();
        init();
        setListner();
    }

    public void getIntenttValue() {
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
    }

    public void init() {
//Use for startdate and enddate
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        myAccountBinding.txtStartDate.setText(Utils.getPriviousDate());
        myAccountBinding.txtEndDate.setText(Utils.getTodaysDate());

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DATE);
        Log.d("StartDate:",""+Year+"/"+Month+"/"+Day);


        callPaymentReportApi();
    }

    public void setListner() {
        myAccountBinding.back.setOnClickListener(this);
        myAccountBinding.startDateLinear.setOnClickListener(this);
        myAccountBinding.endDateLinear.setOnClickListener(this);
        myAccountBinding.btnShow.setOnClickListener(this);
        myAccountBinding.menu.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
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

        finalDate = d + "/" + m + "/" + y;
        if (isFromDate) {
            myAccountBinding.txtStartDate.setText(finalDate);
        } else {
            myAccountBinding.txtEndDate.setText(finalDate);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date_linear:
                isFromDate = true;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(MyAccountActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.end_date_linear:

                isFromDate = false;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(MyAccountActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.btnShow:
                if (!startDateStr.equalsIgnoreCase("dd/MM/yyyy") && !endDateStr.equalsIgnoreCase("dd/MM/yyyy")) {
                    callPaymentReportApi();
                } else {
                    Utils.ping(mContext, "Please Select StartDate and EndDate.");
                }
                break;
            case R.id.back:
                if (wheretocometypeStr.equalsIgnoreCase("mySession")) {
                    Intent intent = new Intent(mContext, MySession.class);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    startActivity(intent);
                } else if (wheretocometypeStr.equalsIgnoreCase("myAccount")) {
                    Intent intent = new Intent(mContext, MyAccountActivity.class);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    startActivity(intent);
                } else if (wheretocometypeStr.equalsIgnoreCase("menu")) {
                    Intent intent = new Intent(mContext, SearchByUser.class);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    startActivity(intent);
                }
                break;
            case R.id.menu:
                menuDialog();
                break;
        }
    }

    //Use for GetPayment Report
    public void callPaymentReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Payment_ByContactID(getReportDetail(), new retrofit.Callback<TeacherInfoModel>() {
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
//                        Utils.ping(mContext, getString(R.string.false_msg));
                        myAccountBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        myAccountBinding.listLinear.setVisibility(View.GONE);
                        myAccountBinding.headerLinear.setVisibility(View.GONE);
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        paymentReportList = paymentInfoModel.getData();
                        if (paymentInfoModel.getData().size() > 0) {
                            myAccountBinding.noRecordTxt.setVisibility(View.GONE);
                            myAccountBinding.listLinear.setVisibility(View.VISIBLE);
                            myAccountBinding.headerLinear.setVisibility(View.VISIBLE);
                            flag = "2";
                            paymentSucessReportAdapter = new PaymentSucessReportAdapter(mContext, paymentReportList, flag);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            myAccountBinding.reportRcList.setLayoutManager(mLayoutManager);
                            myAccountBinding.reportRcList.setItemAnimator(new DefaultItemAnimator());
                            myAccountBinding.reportRcList.setAdapter(paymentSucessReportAdapter);
                        } else {
                            myAccountBinding.noRecordTxt.setVisibility(View.VISIBLE);
                            myAccountBinding.listLinear.setVisibility(View.GONE);
                            myAccountBinding.headerLinear.setVisibility(View.GONE);
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
        startDateStr = myAccountBinding.txtStartDate.getText().toString();
        endDateStr = myAccountBinding.txtEndDate.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);

        return map;
    }

    public void changePasswordDialog() {

        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) myAccountBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(changePasswordDialogBinding.getRoot());

        changePasswordDialogBinding.changepwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = changePasswordDialogBinding.edtcurrentpassword.getText().toString();
                confirmpassWordStr = changePasswordDialogBinding.edtconfirmpassword.getText().toString();
                passWordStr = changePasswordDialogBinding.edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContext, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            changePasswordDialogBinding.edtconfirmpassword.setError("Confirm Password does not match.");
                        }
                    } else {
                        changePasswordDialogBinding.edtnewpassword.setError("Password must be 4-8 Characters.");
                        changePasswordDialogBinding.edtnewpassword.setText("");
                        changePasswordDialogBinding.edtconfirmpassword.setText("");
                    }
                } else {
                    changePasswordDialogBinding.edtcurrentpassword.setError("Password does not match to current password.");
                }


            }
        });
        changePasswordDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDialog.dismiss();
            }
        });

        changeDialog.show();

    }

    //USe for Change Password
    public void callChangePasswordApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Change_Password(getChangePasswordDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel forgotInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (forgotInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Please Enter Valid Password.");
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.ping(mContext, getResources().getString(R.string.changPassword));
                        Utils.setPref(mContext, "Password", passWordStr);
                        changeDialog.dismiss();
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

    private Map<String, String> getChangePasswordDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", Utils.getPref(mContext, "RegisterEmail"));
        map.put("Password", passWordStr);
        return map;
    }

    public void menuDialog() {
        menuDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));

        btnMyReport.setVisibility(View.GONE);
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "menu");
                startActivity(imyaccount);
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "menu");
                startActivity(isession);
                menuDialog.dismiss();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                changePasswordDialog();
            }
        });
        btnmyfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FamilyListActivity.class);
                intent.putExtra("froncontanct", "true");
                intent.putExtra("wheretocometype", "menu");
                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                        .setCancelable(false)
                        .setTitle("Logout")
                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.setPref(mContext, "coachID", "");
                                Utils.setPref(mContext, "coachTypeID", "");
                                Utils.setPref(mContext, "RegisterUserName", "");
                                Utils.setPref(mContext, "RegisterEmail", "");
                                Utils.setPref(mContext, "LoginType", "");
                                Utils.setPref(mContext, "Password", "");
                                Utils.setPref(mContext, "FamilyID", "");
                                Utils.setPref(mContext, "location", "");
                                Utils.setPref(mContext, "sessionName", "");
                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
                                intentLogin.putExtra("frontLogin", "beforeLogin");
                                startActivity(intentLogin);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
                        .setIcon(R.drawable.safari)
                        .show();
            }
        });
//        menuBinding = DataBindingUtil.
//                inflate(LayoutInflater.from(mContext), R.layout.layout_menu, (ViewGroup) myAccountBinding.getRoot(), false);
//
//        menuDialog = new Dialog(mContext, R.style.Theme_Dialog);
//        Window window = menuDialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        menuDialog.getWindow().getAttributes().verticalMargin = 0.1F;
//        wlp.gravity = Gravity.TOP;
//        window.setAttributes(wlp);
//
//        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
//
//
//        menuBinding.userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
//        menuBinding.btnMyReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
//                imyaccount.putExtra("wheretocometype", "myAccount");
//                startActivity(imyaccount);
//            }
//        });
//        menuBinding.btnMySession.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent isession = new Intent(mContext, MySession.class);
//                isession.putExtra("wheretocometype", "myAccount");
//                startActivity(isession);
//                menuDialog.dismiss();
//            }
//        });
//        menuBinding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                menuDialog.dismiss();
//                changePasswordDialog();
//            }
//        });
//        menuBinding.btnmyfamily.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, FamilyListActivity.class);
//                intent.putExtra("froncontanct", "true");
//                intent.putExtra("wheretocometype", "myAccount");
//                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
//                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
//                startActivity(intent);
//            }
//        });
//        menuBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                menuDialog.dismiss();
//                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
//                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
//                        .setMessage("Are you sure you want to logout?")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Utils.setPref(mContext, "coachID", "");
//                                Utils.setPref(mContext, "coachTypeID", "");
//                                Utils.setPref(mContext, "RegisterUserName", "");
//                                Utils.setPref(mContext, "RegisterEmail", "");
//                                Utils.setPref(mContext, "LoginType", "");
//                                Utils.setPref(mContext, "Password", "");
//                                Utils.setPref(mContext, "FamilyID", "");
//                                Utils.setPref(mContext, "location", "");
//                                Utils.setPref(mContext, "sessionName", "");
//                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
//                                intentLogin.putExtra("frontLogin", "beforeLogin");
//                                startActivity(intentLogin);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//
//                            }
//                        })
//                        .setIcon(R.drawable.safari)
//                        .show();
//            }
//        });
        menuDialog.show();
    }
}
