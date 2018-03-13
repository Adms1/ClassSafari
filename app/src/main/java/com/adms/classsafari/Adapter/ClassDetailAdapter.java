package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adms.classsafari.Activites.SessionName;
import com.adms.classsafari.Activites.SessionName1;
import com.adms.classsafari.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> arrayList;

    public ClassDetailAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView session_name_txt;


        public MyViewHolder(View view) {
            super(view);
            session_name_txt = (TextView) view.findViewById(R.id.session_name_txt);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.session_name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSession=new Intent(mContext, SessionName.class);
                mContext.startActivity(inSession);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
