package rannver.com.chartsdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
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
     * @param target 对应折线的target值
     */
    public void removeLineData(int target) throws Exception {
        if (!mLinesMap.containsKey(target)){
            throw new Exception("The Line is not included in the LinesChart");
        }
        mLinesMap.remove(target);
        postInvalidate();
    }

    /**
     * 获取折线的target
     * @param lineData
     * @return 对应折线的target
     */
    public int getLineTarget(LineData lineData) throws Exception {

        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()){
            HashMap.Entry entry = (HashMap.Entry) it.next();
            if (entry.getValue().equals(lineData)){
                return (int) entry.getKey();
            }
        }
        throw new Exception("The Line is not included in the LinesChart");
    }

    /**
     * 添加折线数据
     */
    private  int linesTarget = 0;
    public void addLinesMap(LineData lineData) throws Exception {

        if (mLinesMap.containsKey(lineData)){
            return;
        }
        if (xAxisList.size()>0 &&!xAxisList.equals(lineData.getxAxisList())){
            throw new Exception("line and view have different XAxis");
        }
        if (linesTarget == 0){
            xAxisList = lineData.getxAxisList();
        }else {
            List<PointData> pointDataList = lineData.getPointDataList();
            for (PointData pointData:pointDataList){
                pointData.setyCurrentPoint(height-xTextPadding);
                pointData.setyPointPre(height-xTextPadding);
            }
        }
        mLinesMap.put(linesTarget++,lineData);
        postInvalidate();
    }

    /**
     * 更改折线数据
     * @param target
     */
    public void changeLine(int target,LineData lineData) throws Exception {
        if (!mLinesMap.containsKey(target)){
            throw new Exception("Target does not exist");
        }
        if (lineData.equals(mLinesMap.get(target))){
            return;
        }
        List<PointData> oldPointList = mLinesMap.get(target).getPointDataList();
        List<PointData> newPointList = lineData.getPointDataList();

        int i = 0;
        for ( ;i<oldPointList.size();i++){
            newPointList.get(i).setyCurrentPoint(oldPointList.get(i).getyPoint());
            newPointList.get(i).setyPointPre(oldPointList.get(i).getyPoint());
        }

        while (i<newPointList.size()){
            newPointList.get(i++).setyCurrentPoint(height-xTextPadding);
            newPointList.get(i++).setyPointPre(height-xTextPadding);
        }
        lineData.setPointDataList(newPointList);

        mLinesMap.put(target,lineData);
        postInvalidate();
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
