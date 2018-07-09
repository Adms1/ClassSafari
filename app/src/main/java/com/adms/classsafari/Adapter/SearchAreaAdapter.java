package com.adms.classsafari.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;

import java.util.ArrayList;

public class SearchAreaAdapter extends BaseAdapter {

    private Context mContext;
    String dialogselectareaStr;
    ArrayList<sessionDataModel> arrayList;


    public SearchAreaAdapter(Context mContext, SessionDetailModel dataResponse, String dialogselectareaStr) {
        this.mContext = mContext;
//        this.arrayList = dataResponse;
        arrayList = new ArrayList<>();
        synchronized (this) {
            for (sessionDataModel model : dataResponse.getData()) {
                boolean needToAdd = true;
                for (sessionDataModel sessionDataModel : arrayList) {
                    if (sessionDataModel.getRegionName().equalsIgnoreCase(model.getRegionName())) {
                            sessionDataModel.setCount(sessionDataModel.getCount() + 1);
                        needToAdd = false;
                    }
                }
                if (!dialogselectareaStr.equalsIgnoreCase("")) {
                    String[] split = dialogselectareaStr.split("\\,");
                    for (int i = 0; i < split.length; i++) {
                        if (model.getRegionName().trim().equalsIgnoreCase(split[i].trim())) {
                            model.setCheckStatus("1");
                        }
                    }
                }
                if (needToAdd) arrayList.add(model);
            }
        }
        this.dialogselectareaStr=dialogselectareaStr;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
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
            viewHolder.count=(TextView)convertView.findViewById(R.id.count);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");
            viewHolder.areachk.setTypeface(custom_font);

            viewHolder.areachk.setText(arrayList.get(position).getRegionName());
viewHolder.count.setText("( "+arrayList.get(position).getCount()+" )");
            viewHolder.areachk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        arrayList.get(position).setCheckStatus("1");
                        arrayList.get(position).setCheckValue(viewHolder.areachk.getText().toString());
                    }else{arrayList.get(position).setCheckStatus("0");

                    }
                }
            });

            switch (arrayList.get(position).getCheckStatus()){
                case "1":
                    viewHolder.areachk.setChecked(true);
                    break;
                case "0":
                    viewHolder.areachk.setChecked(false);
                    break;
                default:
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }



    private class ViewHolder {
        CheckBox areachk;
        TextView count;
    }


}

