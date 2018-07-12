package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.UpcomingClassesListBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpcomingClassAdapter extends RecyclerView.Adapter<UpcomingClassAdapter.MyViewHolder> {

    UpcomingClassesListBinding upcomingClassesListBinding;
    private SessionDetailModel arrayList;
    private Context mContext;
    String address;
//    public UpcomingClassAdapter(Context mContext, List<sessionDataModel> arrayList, onViewClick onViewClick) {
//        this.mContext = mContext;
//
//    }

    public UpcomingClassAdapter(Context mContext, SessionDetailModel arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @Override
    public UpcomingClassAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.upcoming_classes_list, parent, false);

        upcomingClassesListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.upcoming_classes_list, parent, false);
        itemView = upcomingClassesListBinding.getRoot();
        return new UpcomingClassAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UpcomingClassAdapter.MyViewHolder holder, final int position) {

        upcomingClassesListBinding.studentNameTxt.setText(arrayList.getData().get(position).getStudentName());
        upcomingClassesListBinding.dateNameTxt.setText(arrayList.getData().get(position).getDate());
        upcomingClassesListBinding.teacherNameTxt.setText(arrayList.getData().get(position).getTeacherName());
        upcomingClassesListBinding.sessionNameTxt.setText(arrayList.getData().get(position).getSubject());
        upcomingClassesListBinding.emailTxt.setText(arrayList.getData().get(position).getEmailID());
        upcomingClassesListBinding.phoneTxt.setText(arrayList.getData().get(position).getMobile());

        String[] time = arrayList.getData().get(position).getTime().split("\\,");
        String[]secondtime=time[1].split("\\-");
        upcomingClassesListBinding.timeNameTxt.setText(secondtime[0]+" to "+secondtime[1]);

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "EEEE";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null, startdateTime = null, enddateTime = null;
        String str = null, StartTimeStr = null, EndTimeStr = null;

        try {
            date = inputFormat.parse(arrayList.getData().get(position).getDate());
            str = outputFormat.format(date);

            Log.i("mini", "Converted Date Today:" + StartTimeStr + "=" + EndTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        upcomingClassesListBinding.dayNameTxt.setText(str);
        address = arrayList.getData().get(position).getAddressLine1()+
                "," + arrayList.getData().get(position).getAddressLine2() +
                "," + arrayList.getData().get(position).getRegionName() +
                "," + arrayList.getData().get(position).getAddressCity() +
                "," + arrayList.getData().get(position).getAddressState() +
                "-" + arrayList.getData().get(position).getAddressZipCode();

        if (arrayList.getData().get(position).getAddressLine2().equalsIgnoreCase("")){
            upcomingClassesListBinding.addressTxt.setText(Html.fromHtml(arrayList.getData().get(position).getAddressLine1()
                    + "<br>" + arrayList.getData().get(position).getRegionName() +
                    ", " + arrayList.getData().get(position).getAddressCity() + "<br>" +
                    arrayList.getData().get(position).getAddressState() + " " + arrayList.getData().get(position).getAddressZipCode()));
        }else {
            upcomingClassesListBinding.addressTxt.setText(Html.fromHtml(arrayList.getData().get(position).getAddressLine1() + "<br>" +
                    arrayList.getData().get(position).getAddressLine2() + "<br>" + arrayList.getData().get(position).getRegionName() +
                    ", " + arrayList.getData().get(position).getAddressCity() + "<br>" +
                    arrayList.getData().get(position).getAddressState() + " " + arrayList.getData().get(position).getAddressZipCode()));
        }
        upcomingClassesListBinding.addressLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(mapIntent);
            }
        });
        upcomingClassesListBinding.emailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {arrayList.getData().get(position).getEmailID()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                mContext.startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
        upcomingClassesListBinding.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.fromParts("tel", upcomingClassesListBinding.phoneTxt.getText().toString(), null));
                mContext.startActivity(intent);
            }
        });

        upcomingClassesListBinding.addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(mapIntent);
            }
        });
        upcomingClassesListBinding.emailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {arrayList.getData().get(position).getEmailID()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                mContext.startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
        upcomingClassesListBinding.phoneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.fromParts("tel", upcomingClassesListBinding.phoneTxt.getText().toString(), null));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.getData().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

        }
    }
}


