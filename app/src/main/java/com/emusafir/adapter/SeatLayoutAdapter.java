package com.emusafir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.interfaces.OnSeatSelected;
import com.emusafir.model.buslayoutmodel.SeatModel;
import com.emusafir.utility.Constant;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dev-30 on 9/5/18.
 */

public class SeatLayoutAdapter extends RecyclerView.Adapter<SeatLayoutAdapter.ViewHolder> {

    private static final String TAG = SeatLayoutAdapter.class.getSimpleName();
    private List<SeatModel> mList;
    private Context mContext;
    private CardView mCardViewBookNow;

    private OnSeatSelected mListener;

    public SeatLayoutAdapter(List<SeatModel> mList, Context mContext, OnSeatSelected mListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mCardViewBookNow = mCardViewBookNow;
        this.mListener = mListener;
    }

    @Override
    public SeatLayoutAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seat_layout_row, viewGroup, false);
        return new SeatLayoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SeatLayoutAdapter.ViewHolder mHolder, final int position) {
        final SeatModel model = mList.get(position);
        if (model.getAvailable().equalsIgnoreCase("1"))//if seat is available then "0" returned else "1" returned
        {
            mHolder.tvSeat.setEnabled(false);// seat is not available so disable the tvSeat
            if (model.getSeatType().equalsIgnoreCase(Constant.SITTING)) {
                mHolder.tvSeat.setBackgroundResource(R.drawable.ic_seater_not_available);
            } else
                mHolder.tvSeat.setBackgroundResource(R.drawable.ic_sleeper_not_available);
        } else {//Seat is Available
            mHolder.tvSeat.setEnabled(true);// seat is available so enable the tvSeat
            if (model.getSeatType().equalsIgnoreCase(Constant.SITTING)) {
                mHolder.tvSeat.setText(null);
                if (model.isSelected())
                    mHolder.tvSeat.setBackgroundResource(R.drawable.ic_seater_selected);
                else
                    mHolder.tvSeat.setBackgroundResource(R.drawable.ic_seater_available);
            } else {
                mHolder.tvSeat.setText(mContext.getString(R.string.rupee) + model.getPrice());
                if (model.isSelected())
                    mHolder.tvSeat.setBackgroundResource(R.drawable.ic_sleeper_selected);
                else
                    mHolder.tvSeat.setBackgroundResource(R.drawable.ic_sleeper_available);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSeat;

        public ViewHolder(View view) {
            super(view);
            tvSeat = view.findViewById(R.id.tvSeat);

            tvSeat.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {

                    openDialog(getAdapterPosition());
                }
            });
        }
    }

    private void openDialog(final int position) {
        final SeatModel model = mList.get(position);

        View modelBottomSheet = LayoutInflater.from(mContext).inflate(R.layout.select_seat_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.CustomBottomSheetDialogTheme);
        dialog.setContentView(modelBottomSheet);

        TextView tvSeatNo = modelBottomSheet.findViewById(R.id.tvSeatNo);
        TextView tvPrice = modelBottomSheet.findViewById(R.id.tvPrice);
        TextView tvMale = modelBottomSheet.findViewById(R.id.tvMale);
        TextView tvCancel = modelBottomSheet.findViewById(R.id.tvCancel);
        TextView tvFemale = modelBottomSheet.findViewById(R.id.tvFemale);

        if (model.isSelected())
            tvCancel.setText(mContext.getResources().getString(R.string.deselect));
        else
            tvCancel.setText(mContext.getResources().getString(R.string.cancel));

        tvSeatNo.setText(model.getSeatNo());
        tvPrice.setText(model.getPrice());

        tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSeat(Constant.MALE, model, position, false);
                dialog.dismiss();
            }
        });


        tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSeat(Constant.FEMALE, model, position, false);
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isSelected()) {
                    selectSeat("", model, position, true);
                    dialog.dismiss();
                } else
                    dialog.dismiss();

            }
        });
        dialog.show();

    }

    private void selectSeat(String gender, SeatModel model, int position, boolean deSelectSeat) {
        Log.e(TAG, position + " before " + model.isSelected());
        model.setGender(gender);
        if (deSelectSeat) {
            model.setSelected(false);
        } else {
            model.setSelected(true);
        }
        Log.e(TAG, position + " after " + model.isSelected());
//        mList.set(position, model);
        notifyItemChanged(position);
        mListener.onSeatSelected(model, position);
    }

}