package rannver.com.chartsdemo.chartUtil;

/**
 * Created by  hqy on 2018/11/12
 * 饼图数据
 */
public class PieData {

    private float percent; //所占百分比
    private String title;//所占标题
    private int color;//颜色

    private float realPercent;//实际百分比所占的角度
    private float textXPoint;//文字x坐标
    private float textYPoint;//文字y坐标
    private float startAngel;//起始角度
    private float endAngel;//结束角度

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRealPercent() {
        return realPercent;
    }

    public void setRealPercent(float realPercent) {
        this.realPercent = realPercent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getTextXPoint() {
        return textXPoint;
    }

    public void setTextXPoint(float textXPoint) {
        this.textXPoint = textXPoint;
    }

    public float getTextYPoint() {
        return textYPoint;
    }

    public void setTextYPoint(float textYPoint) {
        this.textYPoint = textYPoint;
    }

    public float getStartAngel() {
        return startAngel;
    }

    public void setStartAngel(float startAngel) {
        this.startAngel = startAngel;
    }

    public float getEndAngel() {
        return endAngel;
    }

    public void setEndAngel(float endAngel) {
        this.endAngel = endAngel;
    }
}
