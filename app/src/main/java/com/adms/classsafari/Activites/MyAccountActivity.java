package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.classsafari.Adapter.PaymentSucessReportAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.FamilyDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyAccountActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    Context mContext;
    LinearLayout start_date_linear, end_date_linear, header_linear, list_linear;
    RecyclerView report_rcList;
    TextView no_record_txt, txtStartDate, txtEndDate;
    Button btnShow;
    ImageView back, menu;

    //Use for calender
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    private static boolean isFromDate = false;
    String finalDate, startDateStr, endDateStr, flag,wheretocometypeStr;

    //Use for paymentreport List
    List<FamilyDetailModel> paymentReportList;
    PaymentSucessReportAdapter paymentSucessReportAdapter;

    //Use for menu
    String passWordStr, confirmpassWordStr, currentpasswordStr;
    EditText edtnewpassword, edtconfirmpassword, edtcurrentpassword;
    Button changepwd_btn, cancel_btn;
    Dialog menuDialog;
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout,btnmyfamily;
    TextView user_name_txt;
    Dialog changeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        mContext = this;

        getIntenttValue();
        init();
        setListner();
    }
    public void getIntenttValue(){
        wheretocometypeStr=getIntent().getStringExtra("wheretocometype");
    }
    public void init() {
        start_date_linear = (LinearLayout) findViewById(R.id.start_date_linear);
        end_date_linear = (LinearLayout) findViewById(R.id.end_date_linear);
        header_linear = (LinearLayout) findViewById(R.id.header_linear);
        list_linear = (LinearLayout) findViewById(R.id.list_linear);

        report_rcList = (RecyclerView) findViewById(R.id.report_rcList);
        no_record_txt = (TextView) findViewById(R.id.no_record_txt);
        txtEndDate = (TextView) findViewById(R.id.txtEndDate);
        txtStartDate = (TextView) findViewById(R.id.txtStartDate);

        back = (ImageView) findViewById(R.id.back);
        btnShow = (Button) findViewById(R.id.btnShow);
        menu = (ImageView) findViewById(R.id.menu);

//Use for startdate and enddate
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        txtStartDate.setText(Utils.getPriviousDate());
        txtEndDate.setText(Utils.getTodaysDate());
        callPaymentReportApi();
    }

    public void setListner() {
        back.setOnClickListener(this);
        start_date_linear.setOnClickListener(this);
        end_date_linear.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        menu.setOnClickListener(this);
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
            txtStartDate.setText(finalDate);
        } else {
            txtEndDate.setText(finalDate);
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
                }else if(wheretocometypeStr.equalsIgnoreCase("myAccount")){
                    Intent intent = new Intent(mContext, MyAccountActivity.class);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    startActivity(intent);
                }else if(wheretocometypeStr.equalsIgnoreCase("menu")){
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
                        no_record_txt.setVisibility(View.VISIBLE);
                        list_linear.setVisibility(View.GONE);
                        header_linear.setVisibility(View.GONE);
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        paymentReportList = paymentInfoModel.getData();
                        if (paymentInfoModel.getData().size() > 0) {
                            no_record_txt.setVisibility(View.GONE);
                            list_linear.setVisibility(View.VISIBLE);
                            header_linear.setVisibility(View.VISIBLE);
                            flag = "2";
                            paymentSucessReportAdapter = new PaymentSucessReportAdapter(mContext, paymentReportList, flag);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            report_rcList.setLayoutManager(mLayoutManager);
                            report_rcList.setItemAnimator(new DefaultItemAnimator());
                            report_rcList.setAdapter(paymentSucessReportAdapter);
                        } else {
                            no_record_txt.setVisibility(View.VISIBLE);
                            list_linear.setVisibility(View.GONE);
                            header_linear.setVisibility(View.GONE);
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
        startDateStr = txtStartDate.getText().toString();
        endDateStr = txtEndDate.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);

        return map;
    }

    public void changePasswordDialog() {
        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(R.layout.change_password_dialog);

        cancel_btn = (Button) changeDialog.findViewById(R.id.cancel_btn);
        changepwd_btn = (Button) changeDialog.findViewById(R.id.changepwd_btn);
        edtconfirmpassword = (EditText) changeDialog.findViewById(R.id.edtconfirmpassword);
        edtnewpassword = (EditText) changeDialog.findViewById(R.id.edtnewpassword);
        edtcurrentpassword = (EditText) changeDialog.findViewById(R.id.edtcurrentpassword);

        changepwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = edtcurrentpassword.getText().toString();
                confirmpassWordStr = edtconfirmpassword.getText().toString();
                passWordStr = edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContext, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            edtcurrentpassword.setError("Confirm Password does not match.");
                        }
                    } else {
//                    Util.ping(mContex, "Confirm Password does not match.");
                        edtconfirmpassword.setError("Password must be 4-8 Characters.");
                        edtconfirmpassword.setText("");
                        edtconfirmpassword.setText("");
                    }
                } else {
                    edtcurrentpassword.setError("Password does not match to current password.");
                }


            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
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
        menuDialog.getWindow().getAttributes().verticalMargin = 0.1F;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
        menuDialog.setContentView(R.layout.layout_menu);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);

        user_name_txt = (TextView) menuDialog.findViewById(R.id.user_name_txt);

        user_name_txt.setText(Utils.getPref(mContext, "RegisterUserName"));
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "myAccount");
                startActivity(imyaccount);
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "myAccount");
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
                intent.putExtra("wheretocometype", "myAccount");
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
        menuDialog.show();
    }
}
