package com.emusafir.interfaces;


import com.emusafir.model.buslayoutmodel.SeatModel;

/**
 * Created by Ravi on 3/26/2018.
 */

public interface OnSeatSelected {
    void onSeatSelected(SeatModel model, int position);
}
