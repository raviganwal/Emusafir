
package com.emusafir.model.buslayoutmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LowerDack implements Serializable {
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected = true;

    @SerializedName("Left")
    @Expose
    private List<List<SeatModel>> left = null;
    @SerializedName("Right")
    @Expose
    private List<List<SeatModel>> right = null;

    public List<List<SeatModel>> getLeft() {
        return left;
    }

    public void setLeft(List<List<SeatModel>> left) {
        this.left = left;
    }

    public List<List<SeatModel>> getRight() {
        return right;
    }

    public void setRight(List<List<SeatModel>> right) {
        this.right = right;
    }
}
