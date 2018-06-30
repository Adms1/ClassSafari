package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySearchByUserBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;
import com.adms.classsafari.databinding.LayoutMenuBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchByUser extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, LocationListener {
    public final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ActivitySearchByUserBinding searchByUserBinding;
    LayoutMenuBinding menuBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    LocationManager locationManager;
    Context mContext;
    SessionDetailModel dataResponse;
    String selectedSessionNameStr = "", selectedLocationStr = "", sessionName = "";
    String passWordStr, confirmpassWordStr, currentpasswordStr, cityName = "";
    Dialog menuDialog, changeDialog;
    //Use for Menu Dialog
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily;
    TextView userNameTxt;
    String SearchPlaystudy;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchByUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_user);
        mContext = SearchByUser.this;
        init();
        setListner();
    }

    public void init() {
//        getLocation();


        if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
            if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Coach")) {
                Intent iDash = new Intent(mContext, DashBoardActivity.class);
                iDash.putExtra("frontLogin", "beforeLogin");
                startActivity(iDash);
            }
            String[] userName = Utils.getPref(mContext, "RegisterUserName").split("\\s+");
            searchByUserBinding.loginTxt.setText(" Hi");
            searchByUserBinding.regiTxt.setText(Html.fromHtml( "<u><b>" + " "+userName[0] + "</u></b>"));
            searchByUserBinding.regiTxt.setPadding(5,0,0,0);
//            searchByUserBinding.regiTxt.setEnabled(false);
            searchByUserBinding.loginMiddleTxt.setVisibility(View.GONE);


        } else {
            searchByUserBinding.regiTxt.setVisibility(View.VISIBLE);
            searchByUserBinding.loginMiddleTxt.setVisibility(View.VISIBLE);
            searchByUserBinding.loginTxt.setText(Html.fromHtml("<u><b>Login<u></b>"));
            searchByUserBinding.regiTxt.setText(Html.fromHtml("<u><b>Register<u></b>"));
        }


    }

    public void setListner() {
        callSessionListApi();
        searchByUserBinding.letsStudyTxt.setOnClickListener(this);
        searchByUserBinding.searchClassEdt.setOnClickListener(this);
        searchByUserBinding.searchTxt.setOnClickListener(this);
        searchByUserBinding.letsPlayTxt.setOnClickListener(this);
        searchByUserBinding.loginTxt.setOnClickListener(this);
        searchByUserBinding.locationEdt.setOnClickListener(this);
        searchByUserBinding.searchClassEdt.setOnEditorActionListener(this);
        searchByUserBinding.regiTxt.setOnClickListener(this);

        searchByUserBinding.locationEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLocationStr = String.valueOf(adapterView.getItemAtPosition(i));
                searchByUserBinding.searchClassEdt.setText("");
                fillSessionList();
            }
        });
        searchByUserBinding.searchClassEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sessionName = String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("session", sessionName);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lets_study_txt:
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                flag = true;
                SearchPlaystudy = "1";
                validation();
//                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
//                inClassDetail.putExtra("flag", "study");
//                inClassDetail.putExtra("withOR", "withOR");
//                inClassDetail.putExtra("SearchBy", "2");
//                inClassDetail.putExtra("sessionType", "1");
//                inClassDetail.putExtra("firsttimesearch", "true");
//                startActivity(inClassDetail);
                break;
            case R.id.search_class_edt:
//                searchByUserBinding.searchClassEdt.showDropDown();
                break;
            case R.id.search_txt:
                flag = false;
                validation();
                break;
            case R.id.lets_play_txt:
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                flag = true;
                SearchPlaystudy = "2";
                validation();
//                Intent inClassDetail1 = new Intent(mContext, ClassSearchScreen.class);
//                inClassDetail1.putExtra("flag", "play");
//                inClassDetail1.putExtra("withOR", "withOR");
//                inClassDetail1.putExtra("SearchBy", "2");
//                inClassDetail1.putExtra("sessionType", "2");
//                inClassDetail1.putExtra("firsttimesearch", "true");
//                startActivity(inClassDetail1);
                break;
            case R.id.login_txt:
                if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
                    menuDialog();
                } else {
                    Intent inClassDetail2 = new Intent(mContext, LoginActivity.class);
                    inClassDetail2.putExtra("frontLogin", "beforeLogin");
                    inClassDetail2.putExtra("ratingLogin", "false");
                    startActivity(inClassDetail2);
                }
                break;
            case R.id.location_edt:
                searchByUserBinding.locationEdt.showDropDown();
                break;
            case R.id.regi_txt:
                if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
                    menuDialog();
                }else {
                    Intent intent = new Intent(mContext, RegistrationActivity.class);
                    intent.putExtra("frontLogin", "beforeLogin");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.search_class_edt:
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    validation();
                }
                break;
        }
        return false;
    }

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionList_Automplated(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel cityInfo, Response response) {
                    Utils.dismissDialog();
                    if (cityInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (cityInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (cityInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (cityInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (cityInfo.getData().size() > 0) {
                            dataResponse = cityInfo;
                            searchByUserBinding.locationEdt.setText("Ahmedabad");
                            fillCity();
//                            fillSessionList();
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

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillCity() {
        ArrayList<String> CityName = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            CityName.add(dataResponse.getData().get(j).getAddressCity());
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(CityName);
        CityName.clear();
        CityName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, CityName);
        searchByUserBinding.locationEdt.setThreshold(1);
        searchByUserBinding.locationEdt.setAdapter(adapterTerm);
        searchByUserBinding.locationEdt.setSelection(searchByUserBinding.locationEdt.getText().length());

//        for (int i = 0; i < CityName.size(); i++) {
//            if (!cityName.equalsIgnoreCase("")) {
//                if (cityName.equalsIgnoreCase(CityName.get(i))) {
//                    searchByUserBinding.locationEdt.setText(CityName.get(i));
//                }
//            }
//        }


    }

    public void fillSessionList() {
        ArrayList<String> SesisonNameArray = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            if (searchByUserBinding.locationEdt.getText().toString().trim().equalsIgnoreCase(dataResponse.getData().get(j).getAddressCity().trim())) {
                SesisonNameArray.add(dataResponse.getData().get(j).getSessionName());
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(SesisonNameArray);
        SesisonNameArray.clear();
        SesisonNameArray.addAll(hashSet);
        ArrayAdapter<String> adapterSessionName = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, SesisonNameArray);
        searchByUserBinding.searchClassEdt.setThreshold(1);
        searchByUserBinding.searchClassEdt.setAdapter(adapterSessionName);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    public void validation() {
        selectedSessionNameStr = searchByUserBinding.searchClassEdt.getText().toString();
        selectedLocationStr = searchByUserBinding.locationEdt.getText().toString();

        if (!selectedSessionNameStr.equalsIgnoreCase("")) {
            String A = selectedSessionNameStr;
            String ACaps = A.toUpperCase().charAt(0) + A.substring(1, A.length());
            selectedSessionNameStr = ACaps;
            Log.d("sessionName", ACaps);
        }
        AppConfiguration.ClassLocation = selectedLocationStr;
        if (!selectedLocationStr.equalsIgnoreCase("")) {//!selectedSessionNameStr.equalsIgnoreCase("") &&
            Utils.setPref(mContext, "location", selectedLocationStr);
            Utils.setPref(mContext, "sessionName", selectedSessionNameStr);
            Intent inClassDetail = new Intent(mContext, ClassDeatilScreen.class);
            inClassDetail.putExtra("city", selectedLocationStr);
            inClassDetail.putExtra("sessionName", selectedSessionNameStr);
//            inClassDetail.putExtra("SearchBy", "1");
//            inClassDetail.putExtra("withOR", "withOutOR");
            inClassDetail.putExtra("searchfront", "searchfront");
            if (flag = true) {
                inClassDetail.putExtra("SearchPlaystudy", SearchPlaystudy);
            } else {
                inClassDetail.putExtra("SearchPlaystudy", "");
            }
//            inClassDetail.putExtra("searchType", "study");
            startActivity(inClassDetail);
        } else {
            Utils.ping(mContext, getResources().getString(R.string.location_validation));
        }
    }

    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) searchByUserBinding.getRoot(), false);

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
        menuDialog.getWindow().getAttributes().verticalMargin = 0.09F;
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

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
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
        menuDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            cityName = addresses.get(0).getLocality();

//            Toast.makeText(mContext,cityName,Toast.LENGTH_LONG).show();
            Log.d("cityName", cityName);

        } catch (Exception e) {

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Toast.makeText(mContext, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
//    private static boolean hasPermissions(Context context, String... permissions) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    public  static final int REQUEST = 112;
//
//    public void getLocation() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION};
//            if (!hasPermissions(mContext, PERMISSIONS)) {
//                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
//            } else {
//                try {
//                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//                }
//                catch(SecurityException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            try {
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//            }
//            catch(SecurityException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    try {
//                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//                    }
//                    catch(SecurityException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(mContext, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
}
