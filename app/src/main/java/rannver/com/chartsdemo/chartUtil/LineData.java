package rannver.com.chartsdemo.chartUtil;

import android.graphics.Color;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by  hqy on 2018/11/8
 * 线的数据
 */
public class LineData {

    public static int LINETYPE_SOLIDLINE = 0;//实线
    public static int LINETYPE_DOTTEDLINE = 1;//虚线

    private int lineColor = Color.BLACK;//线的颜色
    private int pointColor = lineColor;//坐标点的颜色
    private int fillColor = Color.TRANSPARENT;//填充区域的颜色
    private int lineType = LINETYPE_SOLIDLINE;//线的类型

    private List<PointData> pointDataList = new ArrayList<>();//点数据的集合
    private List<String> xAxisList = new ArrayList<>();//x坐标轴数据的集合

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    public List<PointData> getPointDataList() {
        return pointDataList;
    }

    public void setPointDataList(List<PointData> pointDataList) {
        this.pointDataList = pointDataList;
    }

    public List<String> getxAxisList() {
        return xAxisList;
    }

    public void setxAxisList(List<String> xAxisList) {
        this.xAxisList = xAxisList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineData lineData = (LineData) o;
        return lineColor == lineData.lineColor &&
                pointColor == lineData.pointColor &&
                fillColor == lineData.fillColor &&
                lineType == lineData.lineType &&
                Objects.equals(pointDataList, lineData.pointDataList) &&
                Objects.equals(xAxisList, lineData.xAxisList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineColor, pointColor, fillColor, lineType, pointDataList, xAxisList);
    }
}
