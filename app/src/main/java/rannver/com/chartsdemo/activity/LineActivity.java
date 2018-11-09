package rannver.com.chartsdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import rannver.com.chartsdemo.R;
import rannver.com.chartsdemo.chartUtil.ChartDataUtil;
import rannver.com.chartsdemo.chartUtil.LineData;
import rannver.com.chartsdemo.chartUtil.PointData;
import rannver.com.chartsdemo.chartUtil.OnChartClickListener;
import rannver.com.chartsdemo.view.LinesChart;
import rannver.com.chartsdemo.view.LinesChartView;

/**
 * Created by  hqy on 2018/11/5
 */
public class LineActivity extends AppCompatActivity implements OnChartClickListener {

    private final String TAG = this.getClass().toString();
    private LinesChart lineChart;
    private Button btuAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        lineChart = findViewById(R.id.linechart);
//        btuAdd = findViewById(R.id.btu_add);
//        btuAdd.setOnClickListener(this);
        Log.d(TAG, "onCreate: "+lineChart);
        Log.d(TAG, "onCreate: "+ChartDataUtil.getLineChartData());
        try {
            lineChart.addLinesMap(ChartDataUtil.getLineData());
            LineData lineData = ChartDataUtil.getLineData();
            lineData.setLineColor(Color.GREEN);
            lineChart.addLinesMap(lineData);
            lineChart.setOnChartClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(Object data, int index) {
        PointData pointData = (PointData) data;
        Toast.makeText(this,"["+ pointData.getX()+","+ pointData.getY()+"],"+(index+1),Toast.LENGTH_SHORT).show();
    }
//
//    @Override
//    public void onClick(View v) {
//        try {
//            lineChart.addLinesMap(ChartDataUtil.getLineData());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
