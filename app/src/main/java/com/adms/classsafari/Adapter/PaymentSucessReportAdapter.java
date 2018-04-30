package com.adms.classsafari.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.classsafari.Model.TeacherInfo.FamilyDetailModel;
import com.adms.classsafari.R;

import java.util.List;

public class PaymentSucessReportAdapter extends RecyclerView.Adapter<PaymentSucessReportAdapter.MyViewHolder> {

    private Context mContext;
    List<FamilyDetailModel> arrayList;


    public PaymentSucessReportAdapter(Context mContext, List<FamilyDetailModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView srno_txt, date_txt, transactionID_txt, amount_txt, sucess_txt, family_name_txt;

        public MyViewHolder(View view) {
            super(view);
            srno_txt = (TextView) view.findViewById(R.id.srno_txt);
            date_txt = (TextView) view.findViewById(R.id.date_txt);
            transactionID_txt = (TextView) view.findViewById(R.id.transactionID_txt);
            amount_txt = (TextView) view.findViewById(R.id.amount_txt);
            sucess_txt = (TextView) view.findViewById(R.id.sucess_txt);
            family_name_txt = (TextView) view.findViewById(R.id.family_name_txt);
        }
    }

    @Override
    public PaymentSucessReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_report_list_item, parent, false);

        return new PaymentSucessReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaymentSucessReportAdapter.MyViewHolder holder, int position) {

        String str = String.valueOf(position+1);
            holder.srno_txt.setText(str);
            holder.sucess_txt.setText(arrayList.get(position).getPaymentStatus());
            holder.date_txt.setText(arrayList.get(position).getPaymentDate());
            holder.transactionID_txt.setText(arrayList.get(position).getTrackAndPayPaymentID());
            holder.amount_txt.setText("₹ " + arrayList.get(position).getPaymentAmount());
            holder.family_name_txt.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}






//        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private Context mContext;
//    List<FamilyDetailModel> arrayList;
////    private static final int TYPE_HEADER = 0;
//    private static final int TYPE_ITEM = 0;
//
//
//    public PaymentSucessReportAdapter(Context mContext, List<FamilyDetailModel> arrayList) {
//        this.mContext = mContext;
//        this.arrayList = arrayList;
//    }
//
//    public class Header extends RecyclerView.ViewHolder {
//        Button button;
//
//        public Header(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView srno_txt, date_txt, transactionID_txt, amount_txt, sucess_txt, family_name_txt;
//
//        public MyViewHolder(View view) {
//            super(view);
//            srno_txt = (TextView) view.findViewById(R.id.srno_txt);
//            date_txt = (TextView) view.findViewById(R.id.date_txt);
//            transactionID_txt = (TextView) view.findViewById(R.id.transactionID_txt);
//            amount_txt = (TextView) view.findViewById(R.id.amount_txt);
//            sucess_txt = (TextView) view.findViewById(R.id.sucess_txt);
//            family_name_txt = (TextView) view.findViewById(R.id.family_name_txt);
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_ITEM) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_report_list_item, parent, false);
//            return new MyViewHolder(itemView);
//        }
////        else if (viewType == TYPE_HEADER) {
////
////            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_header_list, parent, false);
////            return new Header(itemView);
////        }
//
//        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
////
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof MyViewHolder) {
//            String str = String.valueOf(position+1);
//            FamilyDetailModel detailModel = getItem(position);
//            ((MyViewHolder) holder).srno_txt.setText(str);
//            ((MyViewHolder) holder).sucess_txt.setText(detailModel.getPaymentStatus());
//            ((MyViewHolder) holder).date_txt.setText(detailModel.getPaymentDate());
//            ((MyViewHolder) holder).transactionID_txt.setText(detailModel.getTrackAndPayPaymentID());
//            ((MyViewHolder) holder).amount_txt.setText("₹ " + detailModel.getPaymentAmount());
//            ((MyViewHolder) holder).family_name_txt.setText(detailModel.getName());
//        } else if (holder instanceof Header) {
//
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
////        if (isPositionHeader(position))
////            return TYPE_HEADER;
//
//        return TYPE_ITEM;
//    }
//
//    private boolean isPositionHeader(int position) {
//        return position == 0;
//    }
//
//    private FamilyDetailModel getItem(int position) {
//        return arrayList.get(position);
//    }
//
//}
//
//
