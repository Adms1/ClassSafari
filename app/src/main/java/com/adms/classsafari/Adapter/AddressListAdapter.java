package com.adms.classsafari.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.R;

import java.util.ArrayList;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    SessionDetailModel arrayList;
    private Context mContext;
    private onViewClick onViewClick;
    private ArrayList<String> addressCheck;

    public AddressListAdapter(Context mContext, SessionDetailModel sessionList, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = sessionList;
        this.onViewClick = onViewClick;
    }

    @Override
    public AddressListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list, parent, false);

        return new AddressListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddressListAdapter.MyViewHolder holder, final int position) {

        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");

        holder.address_rb.setTypeface(custom_font);

        holder.address_rb.setText(arrayList.getData().get(position).getAddressLine1() + ", " +
                arrayList.getData().get(position).getAddressLine2() + "," +
                arrayList.getData().get(position).getRegionName() + ", " +
                arrayList.getData().get(position).getAddressCity() + ", " +
                arrayList.getData().get(position).getAddressState() + ", " +
                arrayList.getData().get(position).getAddressZipCode());

        holder.address_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressCheck = new ArrayList<>();
                addressCheck.add(String.valueOf(position));
                onViewClick.getViewClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.getData().size();
    }

    public ArrayList<String> getAddress() {
        return addressCheck;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_txt;
        RadioButton address_rb;
        LinearLayout linear;

        public MyViewHolder(View view) {
            super(view);
//            name_txt = (TextView) view.findViewById(R.id.name_txt);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            address_rb = (RadioButton) view.findViewById(R.id.address_rb);

        }
    }

}


