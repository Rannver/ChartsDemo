package rannver.com.chartsdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

import rannver.com.chartsdemo.chartUtil.PieData;

/**
 * Created by  hqy on 2018/11/12
 */
public class PieChart extends PieChartView {

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public List<PieData> getPieList() {
        return pieList;
    }

    public void setPieList(List<PieData> pieList) {
        Log.d("Pie", "setPieList: "+pieList.size());
        this.pieList = pieList;
        invalidate();
    }

    public int getTitleIndex() {
        return titleIndex;
    }

    public void setTitleIndex(int titleIndex) {
        this.titleIndex = titleIndex;
        invalidate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }



}
