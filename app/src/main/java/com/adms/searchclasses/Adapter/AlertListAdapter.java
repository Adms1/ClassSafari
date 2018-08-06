package com.adms.searchclasses.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/14/2018.
 */

public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> timegapArray;
    ArrayList<String> getTime = new ArrayList<>();
    onViewClick onViewClick;

    public AlertListAdapter(Context mContext, ArrayList<String> timegapArray, onViewClick onViewClick) {
        this.mContext = mContext;
        this.timegapArray = timegapArray;
        this.onViewClick=onViewClick;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView alert_time_txt;
        RadioGroup alert_status_rg;
        RadioButton alert_rb;

        public MyViewHolder(View view) {
            super(view);

            alert_time_txt = (TextView) view.findViewById(R.id.alert_time_txt);
            alert_status_rg = (RadioGroup) view.findViewById(R.id.alert_status_rg);
            alert_rb = (RadioButton) view.findViewById(R.id.alert_rb);
        }
    }

    @Override
    public AlertListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_list_item, parent, false);

        return new AlertListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlertListAdapter.MyViewHolder holder, final int position) {

        holder.alert_time_txt.setText(timegapArray.get(position));
        holder.alert_status_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = holder.alert_status_rg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.alert_rb:
                        getTime.add(timegapArray.get(position));
                        onViewClick.getViewClick();
                        break;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timegapArray.size();
    }


    public ArrayList<String> getTime() {
        return getTime;
    }
}


