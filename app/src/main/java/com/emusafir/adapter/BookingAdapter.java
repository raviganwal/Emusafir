package com.emusafir.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.booking.BookingDetailsActivity;
import com.emusafir.interfaces.OnCitySelected;
import com.emusafir.model.booking.BookingModel;
import com.emusafir.utility.Constant;
import com.emusafir.utility.Utils;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private List<BookingModel> mList;
    private Context mContext;
    private OnCitySelected mOnCitySelected;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView tvFrom, tvTo, tvPnr, tvDateOfJourney, tvPayment, tvDateOfBooking, tvPaymentStatus;

        public MyViewHolder(View view) {
            super(view);
            mCardView = view.findViewById(R.id.mCardView);
            tvFrom = view.findViewById(R.id.tvFrom);
            tvTo = view.findViewById(R.id.tvTo);
            tvPnr = view.findViewById(R.id.tvPnr);
            tvDateOfJourney = view.findViewById(R.id.tvDateOfJourney);
            tvPayment = view.findViewById(R.id.tvPayment);
            tvPaymentStatus = view.findViewById(R.id.tvPaymentStatus);
            tvDateOfBooking = view.findViewById(R.id.tvDateOfBooking);
        }
    }


    public BookingAdapter(Context mContext, List<BookingModel> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BookingModel model = mList.get(position);
        if (model.getSourceName() != null)
            holder.tvFrom.setText(model.getSourceName());
        if (model.getDestinationName() != null)
            holder.tvTo.setText(model.getDestinationName());
        if (model.getPNRNo() != null)
            holder.tvPnr.setText(model.getPNRNo());
        if (model.getJourneyDate() != null)
            holder.tvDateOfJourney.setText(Utils.dateFormat_dd_MMM_yy(model.getJourneyDate()));
        if (model.getCreatedDate() != null)
            holder.tvDateOfBooking.setText(Utils.dateFormat_dd_MMM_yy(model.getCreatedDate()));
        if (model.getFinalTotalPrice() != null)
            holder.tvPayment.setText(mContext.getResources().getString(R.string.rupee) + model.getFinalTotalPrice());
        if (model.getPaymentStatus() != null) {
            if (model.getPaymentStatus().equals(Constant.FAILED)) {
                holder.tvPaymentStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                holder.tvPaymentStatus.setText("Failed");
            } else if (model.getPaymentStatus().equals(Constant.CONFIRM)) {
                holder.tvPaymentStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                holder.tvPaymentStatus.setText("Booked");
            } else if (model.getPaymentStatus().equals(Constant.PENDING)) {
                holder.tvPaymentStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorYellow));
                holder.tvPaymentStatus.setText("Pending");
            } else if (model.getPaymentStatus().equals(Constant.CANCEL)) {
                holder.tvPaymentStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorHint));
                holder.tvPaymentStatus.setText("Cancelled");
            }

        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BookingDetailsActivity.class).putExtra(Constant.OBJECT, model));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}