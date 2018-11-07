package rannver.com.chartsdemo.chartUtil;

/**
 * Created by  hqy on 2018/11/5
 * 用于显示图表的数据
 */
public class LineData {

    private String x;  //x轴
    private int y;  //y轴

    private float xPoint;//坐标点x
    private float yPoint;//坐标点y


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
}
