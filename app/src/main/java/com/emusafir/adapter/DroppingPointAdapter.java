package com.emusafir.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.interfaces.OnPointSelected;
import com.emusafir.model.buslayoutmodel.PointModel;
import com.emusafir.utility.Utils;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DroppingPointAdapter extends RecyclerView.Adapter<DroppingPointAdapter.ViewHolder> {

    private List<PointModel> mList;
    private Context mContext;
    private int lastSelectedPosition = -1;
    private OnPointSelected mListener;

    public DroppingPointAdapter(List<PointModel> seatLayoutArrayList, Context mContext, OnPointSelected mListener) {
        this.mList = seatLayoutArrayList;
        this.mContext = mContext;
        this.mListener = mListener;

    }

    @Override
    public DroppingPointAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dropping_boarding_row, viewGroup, false);
        return new DroppingPointAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final DroppingPointAdapter.ViewHolder viewHolder, int i) {

        final PointModel model = mList.get(i);

        viewHolder.radioButton.setChecked(model.isSelected());
        viewHolder.tvName.setText(model.getName());
        viewHolder.tvDepartureTime.setText(Utils.timeFormat24hrs(model.getDepartureTime()));
        viewHolder.tvArrivaltime.setText(Utils.timeFormat24hrs(model.getArrivalTime()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvArrivaltime, tvDepartureTime;
        private RadioButton radioButton;
        private CardView mCardView;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvArrivaltime = view.findViewById(R.id.tvArrivaltime);
            tvDepartureTime = view.findViewById(R.id.tvDepartureTime);
            radioButton = view.findViewById(R.id.radioButton);
            mCardView = view.findViewById(R.id.mCardView);

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectPoint(getAdapterPosition());
                }
            });
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPoint(getAdapterPosition());
                }
            });


        }

    }

    private void selectPoint(int position) {
        PointModel model = mList.get(position);
        if (lastSelectedPosition != -1) {
            mList.get(lastSelectedPosition).setSelected(false);
        }
        lastSelectedPosition = position;
        mList.get(lastSelectedPosition).setSelected(true);
        notifyDataSetChanged();
        mListener.onPointSelected(model, false);
    }


}

