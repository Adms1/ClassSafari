package com.adms.searchclasses.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.R;

import java.util.ArrayList;
import java.util.List;

public class UserSessionListAdapter extends RecyclerView.Adapter<UserSessionListAdapter.MyViewHolder> {

    private Context mContext;
    List<sessionDataModel> arrayList;
    onViewClick onViewClick;
    private ArrayList<String> familyIdCheck;

    public UserSessionListAdapter(Context mContext, List<sessionDataModel> sessionList, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = sessionList;
        this.onViewClick = onViewClick;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView no_txt, date_txt, transactionID_txt, amount_txt, paid_txt, session_name_txt;
        LinearLayout linear;

        public MyViewHolder(View view) {
            super(view);
            no_txt = (TextView) view.findViewById(R.id.no_txt);
            date_txt = (TextView) view.findViewById(R.id.date_txt);
            transactionID_txt = (TextView) view.findViewById(R.id.transactionID_txt);
            amount_txt = (TextView) view.findViewById(R.id.amount_txt);
            paid_txt = (TextView) view.findViewById(R.id.paid_txt);
            session_name_txt = (TextView) view.findViewById(R.id.session_name_txt);
            linear = (LinearLayout) view.findViewById(R.id.linear);
        }
    }

    @Override
    public UserSessionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_list_item, parent, false);

        return new UserSessionListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserSessionListAdapter.MyViewHolder holder, final int position) {

        String str = String.valueOf(position + 1);
        final String paidStr = arrayList.get(position).getIsActive();
        holder.no_txt.setText(str);
        holder.date_txt.setText(arrayList.get(position).getCreateDate());
        if(arrayList.get(position).getSessionPrice().equalsIgnoreCase("0.00")){
            holder.amount_txt.setText("Free");
        }else {
            holder.amount_txt.setText("₹ " + arrayList.get(position).getSessionPrice());
        }
        holder.session_name_txt.setText(arrayList.get(position).getSessionName());

        holder.session_name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyIdCheck = new ArrayList<String>();
                familyIdCheck.add(arrayList.get(position).getSessionID()+"|"+"1"+"|"+arrayList.get(position).getSessionName());
                onViewClick.getViewClick();
            }
        });
        if (paidStr.equalsIgnoreCase("False")) {
            holder.linear.setBackgroundResource(R.drawable.unpaid_linear);
            holder.paid_txt.setText("UnPaid");
            holder.paid_txt.setBackgroundResource(R.drawable.round_yello);
            holder.paid_txt.setTextColor(Color.parseColor("#f2552c"));
            holder.paid_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    familyIdCheck = new ArrayList<String>();
                    familyIdCheck.add(arrayList.get(position).getSessionID()+"|"+"2"+"|"+arrayList.get(position).getSessionName());
                    onViewClick.getViewClick();
                }
            });
        } else {
            holder.linear.setBackgroundResource(R.drawable.list_line);
            holder.paid_txt.setText("Success");
            holder.paid_txt.setTextColor(Color.parseColor("#3db45c"));

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public ArrayList<String> getContactID() {
        return familyIdCheck;
    }
}

