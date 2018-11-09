package rannver.com.chartsdemo.chartUtil;

import java.util.Objects;

/**
 * Created by  hqy on 2018/11/5
 * 点的数据
 */
public class PointData {

    private String x;  //x轴
    private int y;  //y轴

    private float xPoint;//坐标点x
    private float yPoint;//坐标点y


    private int pointColor;//设置点的颜色


    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getxPoint() {
        return xPoint;
    }

    public void setxPoint(float xPoint) {
        this.xPoint = xPoint;
    }

    public float getyPoint() {
        return yPoint;
    }

    public void setyPoint(float yPoint) {
        this.yPoint = yPoint;
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointData pointData = (PointData) o;
        return y == pointData.y &&
                Float.compare(pointData.xPoint, xPoint) == 0 &&
                Float.compare(pointData.yPoint, yPoint) == 0 &&
                pointColor == pointData.pointColor &&
                Objects.equals(x, pointData.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, xPoint, yPoint, pointColor);
    }
}
