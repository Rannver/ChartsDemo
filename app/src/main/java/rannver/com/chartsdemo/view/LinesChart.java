package rannver.com.chartsdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rannver.com.chartsdemo.chartUtil.LineData;
import rannver.com.chartsdemo.chartUtil.OnChartClickListener;
import rannver.com.chartsdemo.chartUtil.PointData;


/**
 * Created by  hqy on 2018/11/9
 * 多数据折线图
 */
public class LinesChart extends LinesChartView  {

    public LinesChart(Context context) {
        super(context);
    }

    public LinesChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinesChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    /**
     * 移除折线数据
     * @param lineData
     */
    public void removeLineData(LineData lineData) throws Exception {

        if (!mLinesMap.containsKey(lineData)){
            throw new Exception("The Line is not included in the LinesChart");
        }

        mLinesMap.remove(lineData);
        postInvalidate();
    }

    /**
     * 移除折线数据
     * @param target 对应折线的target值
     */
    public void removeLineData(int target) throws Exception {

        if (!mLinesMap.containsValue(target)){
            throw new Exception("The Line is not included in the LinesChart");
        }

        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getValue().equals(target)){
                mLinesMap.remove(entry.getValue());
                return;
            }
        }
        postInvalidate();
    }

    /**
     * 获取折线的target
     * @param lineData
     * @return 对应折线的target
     */
    public int getLineTarget(LineData lineData) throws Exception {

        if (mLinesMap.containsKey(lineData)){
            return mLinesMap.get(lineData);
        }

        throw new Exception("The Line is not included in the LinesChart");
    }

    /**
     * 添加折线数据
     */
    private static int linesTarget = 0;
    public void addLinesMap(LineData lineData) throws Exception {

        if (mLinesMap.containsKey(lineData)){
            return;
        }
        if (xAxisList.size()>0 &&!xAxisList.equals(lineData.getxAxisList())){
            throw new Exception("line and view have different XAxis");
        }
        if (linesTarget == 0){
            xAxisList = lineData.getxAxisList();
        }
        mLinesMap.put(lineData,linesTarget++);
    }

    public void setXyLineColor(int xyLineColor) {
        this.xyLineColor = xyLineColor;
    }

    public void setXyLineWidth(float xyLineWidth) {
        this.xyLineWidth = xyLineWidth;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public void setXyTextSize(float xyTextSize) {
        this.xyTextSize = xyTextSize;
    }

    public void setXyTextColor(int xyTextColor) {
        this.xyTextColor = xyTextColor;
    }

    public void setxScaleLength(float xScaleLength) {
        this.xScaleLength = xScaleLength;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public void setThresholdLineShow(boolean thresholdLineShow) {
        isThresholdLineShow = thresholdLineShow;
    }

    public OnChartClickListener getOnChartClickListener() {
        return onChartClickListener;
    }

    public void setOnChartClickListener(OnChartClickListener onChartClickListener) {
        this.onChartClickListener = onChartClickListener;
    }


}