package rannver.com.chartsdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import rannver.com.chartsdemo.R;
import rannver.com.chartsdemo.chartUtil.ChartDataUtil;
import rannver.com.chartsdemo.chartUtil.LineData;
import rannver.com.chartsdemo.chartUtil.OnChartClickListener;
import rannver.com.chartsdemo.view.LineChartView;

/**
 * Created by  hqy on 2018/11/5
 */
public class LineActivity extends AppCompatActivity implements OnChartClickListener {

    private final String TAG = this.getClass().toString();
    private LineChartView lineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        lineChart = findViewById(R.id.linechart);
        Log.d(TAG, "onCreate: "+lineChart);
        Log.d(TAG, "onCreate: "+ChartDataUtil.getLineChartData());
        lineChart.setLineDataList(ChartDataUtil.getLineChartData());
        lineChart.setOnChartClickListener(this);
    }

    @Override
    public void onClick(Object data, int index) {
        LineData lineData = (LineData) data;
        Toast.makeText(this,"["+lineData.getX()+","+lineData.getY()+"],"+(index+1),Toast.LENGTH_SHORT).show();
    }
}
