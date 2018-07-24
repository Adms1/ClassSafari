package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.adms.classsafari.Interface.onChlidClick;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.TeacherInfo.ChildDetailModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.AddStudentHeaderBinding;
import com.adms.classsafari.databinding.FamilyGrouplistFromfrontBinding;
import com.adms.classsafari.databinding.FamilyListFromfrontBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExapndableListAdapterFromFront extends BaseExpandableListAdapter {

    String FamilyID;
    ArrayList<String> value;
    String froncontanctStr, arraowStr, phoneStr;
    onViewClick onViewClick;
    onChlidClick onChlidClick;
    private Context mContext;
    private List<String> _listDataHeader;
    private HashMap<String, List<ChildDetailModel>> _listDataChild;
    private ArrayList<String> familyIdCheck;
    private ArrayList<String> sesionDeatil;
    String[] spiltValue;
    public ExapndableListAdapterFromFront(Context mContext, List<String> listDataHeader, HashMap<String, List<ChildDetailModel>> listDataChild, onViewClick onViewClick, onChlidClick onChlidClick) {
        this.mContext = mContext;
        this._listDataChild = listDataChild;
        this._listDataHeader = listDataHeader;
        this.onViewClick = onViewClick;
        this.onChlidClick = onChlidClick;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final FamilyListFromfrontBinding familyListFromfrontBinding;
        AddStudentHeaderBinding headerBinding;

        if (childPosition > 0) {// && childPosition < getChildrenCount(groupPosition) - 1
            familyListFromfrontBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.family_list_fromfront, parent, false);
            convertView = familyListFromfrontBinding.getRoot();

            final ChildDetailModel childDetail = getChild(groupPosition, childPosition - 1);
            familyListFromfrontBinding.nameTxt.setText(childDetail.getFirstName() + " " + childDetail.getLastName());

            familyListFromfrontBinding.phoneNoTxt.setText(childDetail.getContactPhoneNumber());

            familyListFromfrontBinding.phoneNoTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", familyListFromfrontBinding.phoneNoTxt.getText().toString(), null));
                    mContext.startActivity(intent);

                }
            });
            familyListFromfrontBinding.editImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    familyIdCheck = new ArrayList<String>();
                    familyIdCheck.add(childDetail.getContactID() + "|" + childDetail.getFirstName() + "|" + childDetail.getLastName() + "|" + childDetail.getContactTypeName() + "|" + childDetail.getGenderID() + "|" + childDetail.getDateofBirth() + "|" + "Contact" + "|" + phoneStr+"|"+spiltValue[0] + "|" + spiltValue[1]);
                    onChlidClick.getChilClick();
                }
            });
        } else {
            headerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.add_student_header, parent, false);

            String headerTitle = (String) getGroup(groupPosition);
            String valueStr;
            value = new ArrayList<>();
            for (String key : _listDataChild.keySet()) {
                System.out.println("------------------------------------------------");
                System.out.println("Iterating or looping map using java5 foreach loop");
                System.out.println("key: " + key + " value: " + _listDataChild.get(key));
                value.add(key);


            }
            for (int i = 0; i < value.size(); i++) {
                if (headerTitle.equalsIgnoreCase(value.get(i))) {
                    valueStr = String.valueOf(_listDataChild.get(value.get(i)));
                    Log.d("value", valueStr);

                    if (valueStr.equalsIgnoreCase("[]")) {
                        headerBinding.tableRowNodata.setVisibility(View.VISIBLE);
                        headerBinding.tableRowHeader.setVisibility(View.GONE);
                    } else {
                        headerBinding.tableRowNodata.setVisibility(View.GONE);
                        headerBinding.tableRowHeader.setVisibility(View.GONE);
                    }
                }
            }
            convertView = headerBinding.getRoot();
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public ChildDetailModel getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        FamilyGrouplistFromfrontBinding grouplistFromfrontBinding;

        String headerTitle = (String) getGroup(groupPosition);
        spiltValue = headerTitle.split("\\|");
        if (convertView == null) {

        }
        grouplistFromfrontBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.family_grouplist_fromfront, parent, false);

        convertView = grouplistFromfrontBinding.getRoot();


        grouplistFromfrontBinding.familynameRb.setText(spiltValue[0] + " " + spiltValue[1]);
        grouplistFromfrontBinding.noTxt.setText(spiltValue[2]);
        FamilyID = spiltValue[3];
        phoneStr = spiltValue[2];
        grouplistFromfrontBinding.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                familyIdCheck = new ArrayList<String>();
                familyIdCheck.add(spiltValue[0] + "|" + spiltValue[1] + "|" + spiltValue[2] + "|" + spiltValue[4] + "|" + spiltValue[5] + "|" + spiltValue[6] + "|" + spiltValue[7] + "|" + "Family");
                onViewClick.getViewClick();
            }
        });

        grouplistFromfrontBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.fromParts("tel", spiltValue[2], null));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<String> getFamilyID() {
        return familyIdCheck;
    }

    public ArrayList<String> getSessionDetail() {
        return sesionDeatil;
    }

}



