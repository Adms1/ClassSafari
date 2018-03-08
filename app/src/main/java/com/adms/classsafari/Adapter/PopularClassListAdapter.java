package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.classsafari.Activites.SessionName1;
import com.adms.classsafari.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/8/2018.
 */

public class PopularClassListAdapter  extends RecyclerView.Adapter<PopularClassListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> arrayList;

    public PopularClassListAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public PopularClassListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_list_item, parent, false);

        return new PopularClassListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PopularClassListAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

