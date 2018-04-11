package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.MainClassModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassSearchScreenBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassSearchScreen extends AppCompatActivity {

    ActivityClassSearchScreenBinding classSearchScreenBinding;
    Context mContext;
    MainClassModel dataResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classSearchScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_class_search_screen);
        mContext = ClassSearchScreen.this;

        setListner();
    }

    public void setListner() {
        callBoardApi();
        callstandardApi();
        callStreamApi();

        classSearchScreenBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSearchUser = new Intent(mContext, SearchByUser.class);
                startActivity(inSearchUser);
            }
        });

        classSearchScreenBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSearchUser = new Intent(mContext, ClassDeatilScreen.class);
                inSearchUser.putExtra("subject", classSearchScreenBinding.classAutoTxt.getText().toString());
                inSearchUser.putExtra("board", classSearchScreenBinding.boardAutoTxt.getText().toString());
                inSearchUser.putExtra("standard", classSearchScreenBinding.standardAutoTxt.getText().toString());
                inSearchUser.putExtra("stream", classSearchScreenBinding.streamAutoTxt.getText().toString());
                startActivity(inSearchUser);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inback = new Intent(mContext, SearchByUser.class);
        inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inback);
    }

    //Use for Board
    public void callBoardApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Board(getBoardDetail(), new retrofit.Callback<MainClassModel>() {
                @Override
                public void success(MainClassModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillBoard();
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

    private Map<String, String> getBoardDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillBoard() {
        ArrayList<String> BoardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            BoardName.add(dataResponse.getData().get(j).getBoardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, BoardName);
        classSearchScreenBinding.boardAutoTxt.setThreshold(1);
        classSearchScreenBinding.boardAutoTxt.setAdapter(adapterTerm);

    }

    //Use for standard
    public void callstandardApi() {
        if (Utils.checkNetwork(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Standard(getstandardDetail(), new retrofit.Callback<MainClassModel>() {
                @Override
                public void success(MainClassModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();

                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillStandard();
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

    private Map<String, String> getstandardDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillStandard() {
        ArrayList<String> StandardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StandardName.add(dataResponse.getData().get(j).getStandardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StandardName);
        classSearchScreenBinding.standardAutoTxt.setThreshold(1);
        classSearchScreenBinding.standardAutoTxt.setAdapter(adapterTerm);
    }

    //Use for Stream
    public void callStreamApi() {
        if (Utils.checkNetwork(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Stream(getStreamDeatil(), new retrofit.Callback<MainClassModel>() {
                @Override
                public void success(MainClassModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillStream();
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

    private Map<String, String> getStreamDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillStream() {

        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StreamName.add(dataResponse.getData().get(j).getStreamName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StreamName);
        classSearchScreenBinding.streamAutoTxt.setThreshold(1);
        classSearchScreenBinding.streamAutoTxt.setAdapter(adapterTerm);
    }

}
