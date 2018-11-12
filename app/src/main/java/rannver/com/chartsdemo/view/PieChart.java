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

    public float getTotalPercent() {
        return totalPercent;
    }

    public void setTotalPercent(float totalPercent) {
        this.totalPercent = totalPercent;
    }

}
