package com.emusafir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.interfaces.OnCitySelected;
import com.emusafir.model.CityModel;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> implements Filterable {

    private List<CityModel> mList;
    private List<CityModel> mListFiltered;
    private Context mContext;
    private OnCitySelected mOnCitySelected;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCity;

        public MyViewHolder(View view) {
            super(view);
            tvCity = view.findViewById(R.id.tvCity);
        }
    }


    public CityAdapter(Context mContext, List<CityModel> mList,OnCitySelected mOnCitySelected) {
        this.mList = mList;
        this.mContext = mContext;
        this.mListFiltered = mList;
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
        final CityModel model = mListFiltered.get(position);
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
        return mListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mListFiltered = mList;
                } else {
                    List<CityModel> filteredList = new ArrayList<>();
                    for (CityModel row : mList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListFiltered = (ArrayList<CityModel>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

}