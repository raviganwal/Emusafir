package com.emusafir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.interfaces.OnCitySelected;
import com.emusafir.model.CityModel;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private List<CityModel> mList;
    private Context mContext;
    private OnCitySelected mOnCitySelected;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCity;

        public MyViewHolder(View view) {
            super(view);
            tvCity = view.findViewById(R.id.tvCity);
        }
    }


    public BookingAdapter(Context mContext, List<CityModel> mList, OnCitySelected mOnCitySelected) {
        this.mList = mList;
        this.mContext = mContext;
        this.mOnCitySelected = mOnCitySelected;
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
                .inflate(R.layout.city_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CityModel model = mList.get(position);
        if (model.getName() != null)
            holder.tvCity.setText(model.getName());
        holder.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCitySelected.onCitySelected(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}