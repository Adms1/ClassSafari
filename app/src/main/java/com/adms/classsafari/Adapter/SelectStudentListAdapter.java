package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.classsafari.Activites.SessionName;
import com.adms.classsafari.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/6/2018.
 */

public class SelectStudentListAdapter extends RecyclerView.Adapter<SelectStudentListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> arrayList;

    public SelectStudentListAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);

        }
    }

    @Override
    public SelectStudentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_select_student, parent, false);

        return new SelectStudentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectStudentListAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
