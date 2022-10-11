package com.example.mtmp;

import java.io.Serializable;

public class PointItem extends android.app.Activity implements Serializable {
    private Double timeData, xData, yData;

    public PointItem(Double timeData, Double xData, Double yData){
        this.timeData = timeData;
        this.xData = xData;
        this.yData = yData;
    }

    public Double getTimeData() {
        return timeData;
    }

    public void setTimeData(Double timeData) {
        this.timeData = timeData;
    }

    public Double getxData() {
        return xData;
    }

    public void setxData(Double xData) {
        this.xData = xData;
    }

    public Double getyData() {
        return yData;
    }

    public void setyData(Double yData) {
        this.yData = yData;
    }
}
