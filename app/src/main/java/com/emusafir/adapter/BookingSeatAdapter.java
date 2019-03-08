package com.emusafir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.model.booking.Detail;
import com.emusafir.utility.Constant;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BookingSeatAdapter extends RecyclerView.Adapter<BookingSeatAdapter.MyViewHolder> {

    private List<Detail> mList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView  tvGender, tvSeatNo, tvPrice, tvSeatType;
        public ImageView ivSeat;
        public LinearLayout parentLayout;

        public MyViewHolder(View view) {
            super(view);
            ivSeat = view.findViewById(R.id.ivSeat);
            tvGender = view.findViewById(R.id.tvGender);
            tvSeatNo = view.findViewById(R.id.tvSeatNo);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvSeatType = view.findViewById(R.id.tvSeatType);
            parentLayout = view.findViewById(R.id.parentLayout);
        }
    }

    public BookingSeatAdapter(Context mContext, List<Detail> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (layoutInflater == null) {
//            layoutInflater = LayoutInflater.from(parent.getContext());
//        }
//        TimelineRowBinding binding =
//                DataBindingUtil.inflate(layoutInflater, R.layout.timeline_row, parent, false);
//        return new MyViewHolder(binding);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_seat_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Detail model = mList.get(position);
        if (position % 2 == 0)
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        else
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorF8));
        if (model.getGender() != null) {
            if (model.getGender().equalsIgnoreCase(Constant.MALE)) {
                if (model.getSeatType().equalsIgnoreCase(Constant.SITTING))
                    holder.ivSeat.setImageResource(R.drawable.ic_seater_booked_by_male);
                else
                    holder.ivSeat.setImageResource(R.drawable.ic_sleeper_booked_by_male);
            } else {
                if (model.getSeatType().equalsIgnoreCase(Constant.SITTING))
                    holder.ivSeat.setImageResource(R.drawable.ic_seater_booked_by_female);
                else
                    holder.ivSeat.setImageResource(R.drawable.ic_sleeper_booked_by_female);
            }
        }

        if (model.getGender() != null)
            holder.tvGender.setText(String.valueOf(model.getGender().toUpperCase().charAt(0)));

        if (model.getSeatNo() != null)
            holder.tvSeatNo.setText(model.getSeatNo());
        if (model.getSeatType() != null)
            holder.tvSeatType.setText(model.getSeatType());
        if (model.getPrice() != null)
            holder.tvPrice.setText(mContext.getResources().getString(R.string.rupee) + model.getPrice());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}