package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.classsafari.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admsandroid on 3/20/2018.
 */

public class SessionViewStudentListAdapter extends RecyclerView.Adapter<SessionViewStudentListAdapter.MyViewHolder> {

    private Context mContext;
    List<String> arrayList;


    public SessionViewStudentListAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView no_txt, name_txt, phoneno_txt;

        public MyViewHolder(View view) {
            super(view);
            no_txt = (TextView) view.findViewById(R.id.no_txt);
            name_txt = (TextView) view.findViewById(R.id.name_txt);
            phoneno_txt = (TextView) view.findViewById(R.id.phoneno_txt);
        }
    }

    @Override
    public SessionViewStudentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_session_row_item, parent, false);

        return new SessionViewStudentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SessionViewStudentListAdapter.MyViewHolder holder, int position) {

        String str = String.valueOf(position + 1);

        holder.no_txt.setText(str);
        Log.d("arrayData", arrayList.get(position));
        final String[] spilt = arrayList.get(position).split("\\|");
        holder.name_txt.setText(spilt[0] + " " + spilt[2]);
        holder.phoneno_txt.setText(spilt[1]);

        holder.phoneno_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.fromParts("tel", spilt[1], null));
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}


