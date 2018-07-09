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
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;

import java.util.ArrayList;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    ArrayList<sessionDataModel> arrayList;
    private Context mContext;
    private onViewClick onViewClick;
    private ArrayList<String> addressCheck;
    String firstValueStr,secondValueStr;

    public AddressListAdapter(Context mContext, SessionDetailModel sessionList, onViewClick onViewClick) {
        this.mContext = mContext;
        arrayList=new ArrayList<>();
        synchronized (this){
            for (sessionDataModel model : sessionList.getData()) {
                boolean needToAdd = true;
                for (sessionDataModel sessionDataModel : arrayList) {
                    firstValueStr=sessionDataModel.getAddressLine1()+","+sessionDataModel.getAddressLine2()+","+sessionDataModel.getRegionName()+","+sessionDataModel.getAddressCity()+","+sessionDataModel.getAddressState()+" "+sessionDataModel.getAddressZipCode();
                    secondValueStr=model.getAddressLine1()+","+model.getAddressLine2()+","+model.getRegionName()+","+model.getAddressCity()+","+model.getAddressState()+" "+model.getAddressZipCode();
                    if (firstValueStr.equalsIgnoreCase(secondValueStr)) {
                        needToAdd = false;
                    }
                }
                if (needToAdd) arrayList.add(model);
            }
        }
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

        holder.address_rb.setText(arrayList.get(position).getAddressLine1() + ", " +
                arrayList.get(position).getAddressLine2() + "," +
                arrayList.get(position).getRegionName() + ", " +
                arrayList.get(position).getAddressCity() + ", " +
                arrayList.get(position).getAddressState() + ", " +
                arrayList.get(position).getAddressZipCode());

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
        return arrayList.size();
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


