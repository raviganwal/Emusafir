package com.emusafir.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.emusafir.R;
import com.emusafir.model.BusAmenity;
import com.emusafir.utility.GlideApp;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AmenityAdapter extends RecyclerView.Adapter<AmenityAdapter.MyViewHolder> {

    private List<BusAmenity> mList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageView ivLogo;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            ivLogo = view.findViewById(R.id.ivLogo);
        }
    }

    public AmenityAdapter(Context mContext, List<BusAmenity> mList) {
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
                .inflate(R.layout.amenity_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BusAmenity model = mList.get(position);
        if (model.getName() != null)
            holder.tvTitle.setText(model.getName());
        GlideApp
                .with(mContext)
                .load(model.getIconImage())
                .thumbnail(0.1f)
                .error(R.drawable.broken_image)
                .placeholder(R.drawable.broken_image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.mProgressBar.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivLogo);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}