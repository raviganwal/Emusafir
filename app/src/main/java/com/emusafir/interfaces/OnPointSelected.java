package com.emusafir.interfaces;


import com.emusafir.model.buslayoutmodel.PointModel;

/**
 * Created by Ravi on 3/26/2018.
 */

public interface OnPointSelected {
    void onPointSelected(PointModel point, boolean isBoarding);
}
