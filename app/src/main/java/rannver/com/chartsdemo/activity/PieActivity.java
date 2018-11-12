package rannver.com.chartsdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import rannver.com.chartsdemo.R;
import rannver.com.chartsdemo.chartUtil.ChartDataUtil;
import rannver.com.chartsdemo.view.PieChart;

/**
 * Created by  hqy on 2018/11/12
 */
public class PieActivity extends AppCompatActivity {

    private PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_pie);
        pieChart = findViewById(R.id.piechart);
        pieChart.setPieList(ChartDataUtil.getPieDataList());
    }
}
