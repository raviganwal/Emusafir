
package com.emusafir.model.buslayoutmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BusLayoutModel implements Serializable {

    @SerializedName("UpperDack")
    @Expose
    private UpperDack upperDack;
    @SerializedName("LowerDack")
    @Expose
    private LowerDack lowerDack;
    @SerializedName("SeatModel")
    @Expose
    private List<SeatModel> endRowDack = null;
    @SerializedName("DroppingPoint")
    @Expose
    private List<PointModel> droppingPoint = null;
    @SerializedName("BoardingPoint")
    @Expose
    private List<PointModel> boardingPoint = null;

    public UpperDack getUpperDack() {
        return upperDack;
    }

    public void setUpperDack(UpperDack upperDack) {
        this.upperDack = upperDack;
    }

    public LowerDack getLowerDack() {
        return lowerDack;
    }

    public void setLowerDack(LowerDack lowerDack) {
        this.lowerDack = lowerDack;
    }

    public List<SeatModel> getEndRowDack() {
        return endRowDack;
    }

    public void setEndRowDack(List<SeatModel> endRowDack) {
        this.endRowDack = endRowDack;
    }

    public List<PointModel> getDroppingPoint() {
        return droppingPoint;
    }

    public void setDroppingPoint(List<PointModel> droppingPoint) {
        this.droppingPoint = droppingPoint;
    }

    public List<PointModel> getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(List<PointModel> boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

}
