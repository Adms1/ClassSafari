package com.adms.searchclasses.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admsandroid on 3/20/2018.
 */

public class SessionViewStudentListAdapter extends RecyclerView.Adapter<SessionViewStudentListAdapter.MyViewHolder> {

    List<sessionDataModel> arrayList;
    onViewClick onViewClick;
    private Context mContext;
    private ArrayList<String> familyIdCheck;

    public SessionViewStudentListAdapter(Context mContext, List<sessionDataModel> arrayList, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.onViewClick = onViewClick;
    }

    @Override
    public SessionViewStudentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_session_row_item, parent, false);

        return new SessionViewStudentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SessionViewStudentListAdapter.MyViewHolder holder, final int position) {
        final String paidStr = arrayList.get(position).getIsActive();
        String str = String.valueOf(position + 1);

        holder.no_txt.setText(str);
        holder.name_txt.setText(arrayList.get(position).getFirstName() + " " + arrayList.get(position).getLastName());
        holder.phoneno_txt.setText(arrayList.get(position).getPhoneNumber());

        holder.phoneno_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.checkAndRequestPermissions(mContext)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", arrayList.get(position).getPhoneNumber(), null));
                    mContext.startActivity(intent);
                }
            }
        });
//        if (paidStr.equalsIgnoreCase("0")) {
//            holder.linear.setBackgroundResource(R.drawable.unpaid_linear);
//            holder.paid_txt.setText("UnPaid");
//            holder.paid_txt.setBackgroundResource(R.drawable.round_yello);
//            holder.paid_txt.setTextColor(Color.parseColor("#f2552c"));
//            holder.paid_txt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    familyIdCheck = new ArrayList<String>();
//                    familyIdCheck.add(arrayList.get(position).getContactID() + "|" + arrayList.get(position).getFirstName() + "|" + arrayList.get(position).getLastName());
//                    onViewClick.getViewClick();
//                }
//            });
//        } else {
//            holder.linear.setBackgroundResource(R.drawable.list_line);
//            holder.paid_txt.setText("");
//            holder.paid_txt.setTextColor(Color.parseColor("#000000"));
//
//        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public ArrayList<String> getContactID() {
        return familyIdCheck;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView no_txt, name_txt, phoneno_txt, paid_txt;
        LinearLayout linear;

        public MyViewHolder(View view) {
            super(view);
            no_txt = (TextView) view.findViewById(R.id.no_txt);
            name_txt = (TextView) view.findViewById(R.id.name_txt);
            phoneno_txt = (TextView) view.findViewById(R.id.phoneno_txt);
            paid_txt = (TextView) view.findViewById(R.id.paid_txt);
            linear = (LinearLayout) view.findViewById(R.id.linear);
        }
    }

}


