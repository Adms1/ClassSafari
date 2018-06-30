package com.adms.classsafari.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.R;

public class SearchAreaAdapter extends BaseAdapter {
   SessionDetailModel arrayList;
    private Context mContext;
String dialogselectareaStr;
//    public SearchAreaAdapter(Context mContext, ArrayList<String> areaName, String dialogselectareaStr) {
//        this.mContext = mContext;
//        this.arrayList = areaName;
//        this.dialogselectareaStr=dialogselectareaStr;
//    }

    public SearchAreaAdapter(Context mContext, SessionDetailModel dataResponse, String dialogselectareaStr) {
        this.mContext = mContext;
        this.arrayList = dataResponse;
        this.dialogselectareaStr=dialogselectareaStr;
    }

    @Override
    public int getCount() {
        return arrayList.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = null;
        convertView = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.arear_filter_list, null);
            viewHolder.areachk = (CheckBox) convertView.findViewById(R.id.area_chk);


            viewHolder.areachk.setText(arrayList.getData().get(position).getRegionName());

            viewHolder.areachk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        arrayList.getData().get(position).setCheckStatus("1");
                        arrayList.getData().get(position).setCheckValue(viewHolder.areachk.getText().toString());
                    }
                }
            });

            switch (arrayList.getData().get(position).getCheckStatus()){
                case "1":
                    viewHolder.areachk.setChecked(true);
                    break;
                case "2":
                    viewHolder.areachk.setChecked(false);
                    break;
                    default:
            }

//            if (!dialogselectareaStr.equalsIgnoreCase("")){
//                String [] split=dialogselectareaStr.split("\\,");
//
//                for (int i=0;i<split.length;i++){
//                    if(arrayList.getData().get(position).getRegionName().equalsIgnoreCase(split[i])) {
//                        viewHolder.areachk.setChecked(true);
//                    }
//                    else{
//                        viewHolder.areachk.setChecked(false);
//                    }
//                }
//            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }



    private class ViewHolder {
        CheckBox areachk;
    }


}

