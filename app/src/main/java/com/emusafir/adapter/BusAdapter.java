package com.emusafir.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.emusafir.BusLayoutActivity;
import com.emusafir.R;
import com.emusafir.fragments.AmenityDialogFragment;
import com.emusafir.model.BusAmenity;
import com.emusafir.model.BusModel;
import com.emusafir.model.BusUserSelectedData;
import com.emusafir.model.SearchModel;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.GlideApp;
import com.emusafir.utility.Utils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {

    private static final String TAG = BusAdapter.class.getSimpleName();
    private List<BusModel> mList;
    private Context mContext;
    private SearchModel mSearchModel;
    private String tripType;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBusName, tvDepartureTime, tvArrivalTime, tvTravelTime, tvType, tvSeats, tvPrice, tvRating, tvMore;
        public CardView mCardView;
        public LinearLayout mLinearLayout;


        public MyViewHolder(View view) {
            super(view);
            tvBusName = view.findViewById(R.id.tvBusName);
            tvDepartureTime = view.findViewById(R.id.tvDepartureTime);
            tvArrivalTime = view.findViewById(R.id.tvArrivalTime);
            tvTravelTime = view.findViewById(R.id.tvTravelTime);
            tvType = view.findViewById(R.id.tvType);
            tvSeats = view.findViewById(R.id.tvSeats);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvRating = view.findViewById(R.id.tvRating);
            tvMore = view.findViewById(R.id.tvMore);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
            mCardView = view.findViewById(R.id.mCardView);
        }
    }


    public BusAdapter(Context mContext, List<BusModel> mList, SearchModel mSearchModel, String tripType) {
        this.mList = mList;
        this.mContext = mContext;
        this.mSearchModel = mSearchModel;
        this.tripType = tripType;
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
                .inflate(R.layout.bus_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BusModel model = mList.get(position);
        if (model.getBusName() != null)
            holder.tvBusName.setText(model.getBusName());
        if (model.getSourceDepartureTime() != 0)
            holder.tvDepartureTime.setText(Utils.timeFormat24hrs(model.getSourceDepartureTime()));
        if (model.getDestinationArrivalTime() != 0)
            holder.tvArrivalTime.setText(Utils.timeFormat24hrs(model.getDestinationArrivalTime()));

        if (model.getSourceDepartureTime() != 0 && model.getDestinationArrivalTime() != 0) {
            long travelTime = model.getSourceDepartureTime() - model.getDestinationArrivalTime();
            holder.tvTravelTime.setText(Utils.travelTime(travelTime));
        }

        if (model.getBusTypeName() != null)
            holder.tvType.setText(String.valueOf(model.getBusTypeName()));

        holder.tvSeats.setText(mContext.getResources().getQuantityString(R.plurals.numberOfSeats, model.getRemainingSeat(), model.getRemainingSeat(), model.getRemainingSeat()));
        if (model.getRemainingSeat() == 0) {
            holder.tvSeats.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            holder.mCardView.setEnabled(false);
            holder.mCardView.setAlpha(0.5f);
        } else {
            holder.mCardView.setEnabled(true);
            holder.mCardView.setAlpha(1f);
            if (model.getRemainingSeat() <= 10)
                holder.tvSeats.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            if (model.getRemainingSeat() > 10 && model.getRemainingSeat() <= 15)
                holder.tvSeats.setTextColor(ContextCompat.getColor(mContext, R.color.colorYellow));
            else
                holder.tvSeats.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryText));
        }
        if (model.getBusFare() != null)
            holder.tvPrice.setText(String.valueOf(mContext.getResources().getString(R.string.rupee) + model.getBusFare()));
        if (model.getBusrating() != null) {
            holder.tvRating.setText(String.valueOf(model.getBusrating()));
            float rating = Float.parseFloat(model.getBusrating());
            if (rating < 2)
                holder.tvRating.setBackgroundResource(R.drawable.rect_red);
            if (rating >= 2 && rating < 3.7)
                holder.tvRating.setBackgroundResource(R.drawable.rect_yellow);
            if (rating >= 3.7)
                holder.tvRating.setBackgroundResource(R.drawable.rect_green);
        }
        int limit;
        if (model.getBusAmenities().size() > 2) {
            holder.tvMore.setText(mContext.getResources().getString(R.string.plus_more, model.getBusAmenities().size() - 2));
            limit = 2;
        } else
            limit = model.getBusAmenities().size();

        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmenityDialogFragment addPhotoBottomDialogFragment =
                        AmenityDialogFragment.newInstance(model.getBusAmenities());
                addPhotoBottomDialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }
        });

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date;
                if (tripType.equalsIgnoreCase(Constant.ROUND_TRIP)) {
                    if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress() == null || App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ONE_WAY)) {
                        Utils.showMessage("Please book onward journey first", (AppCompatActivity) mContext);
                        return;
                    }

                    App.getInstance().getPrefManager().setOneWayOrRoundTripOnProgress(Constant.ROUND_TRIP);
                    date = mSearchModel.getReturnDate();
                    Log.e(TAG, date);

                    App.getInstance().getPrefManager().setRoundTripBus(new BusUserSelectedData(model.getBusId(), model.getRouteId(), mSearchModel.getReturnDate(), model.getSourceCityName(), model.getDestinationCityName()));
                } else {
                    if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress() != null && App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ROUND_TRIP)) {
                        Utils.showMessage("Already booked, Please book return journey now.", (AppCompatActivity) mContext);
                        return;
                    }
                    App.getInstance().getPrefManager().setOneWayOrRoundTripOnProgress(Constant.ONE_WAY);
                    date = mSearchModel.getOnwardDate();
                    Log.e(TAG, date);
                    App.getInstance().getPrefManager().setOneWayBus(new BusUserSelectedData(model.getBusId(), model.getRouteId(), mSearchModel.getOnwardDate(), model.getSourceCityName(), model.getDestinationCityName()));
                }
//                .putExtra("bus_id", model.getBusId()).putExtra("book_date", date).putExtra("route_id", model.getRouteId())
                mContext.startActivity(new Intent(mContext, BusLayoutActivity.class));
            }
        });

        for (int i = 0; i < limit; i++) {
            BusAmenity mBusAmenity = model.getBusAmenities().get(i);
            LinearLayout.LayoutParams imParams =
                    new LinearLayout.LayoutParams(40, 40);
            imParams.setMargins(8, 0, 8, 0);
            ImageView imageView = new ImageView(mContext);
            imageView.requestLayout();
            GlideApp
                    .with(mContext)
                    .load(mBusAmenity.getIconImage())
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
                    .into(imageView);
            holder.mLinearLayout.addView(imageView, imParams);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}