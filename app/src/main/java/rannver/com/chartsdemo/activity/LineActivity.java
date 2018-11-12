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
public class LineActivity extends AppCompatActivity implements OnChartClickListener,View.OnClickListener {

    private final String TAG = this.getClass().toString();
    private LinesChart lineChart;
    private Button btuAdd;
    private Button btuRemove;
    private int removeTarget = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        lineChart = findViewById(R.id.linechart);
        lineChart.setScrollable(true);
        btuAdd = findViewById(R.id.btu_add);
        btuRemove = findViewById(R.id.btu_remove);
        btuRemove.setOnClickListener(this);
        btuAdd.setOnClickListener(this);
        Log.d(TAG, "onCreate: "+lineChart);
        Log.d(TAG, "onCreate: "+ChartDataUtil.getLineChartData());
        try {
            Log.d(TAG, "onCreate: ");
            LineData lineData1 = ChartDataUtil.getLineData();
            lineData1.setLineType(LineData.LINETYPE_DOTTEDLINE);
            lineData1.setFillColor(Color.YELLOW);
            lineData1.setTitle("数据1");
            lineChart.addLinesMap(lineData1);
            LineData lineData2 = ChartDataUtil.getLineData();
            lineData2.setLineColor(Color.GREEN);
            lineData2.setFillColor(Color.BLUE);
            lineData2.setTitle("数据2");
            lineChart.addLinesMap(lineData2);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btu_add:
                addLine();
                break;
            case R.id.btu_remove:
                removeLine();
                break;
        }
    }

    /**
     * 移除折线
     */
    private void removeLine() {
        try {
            lineChart.removeLineData(removeTarget);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加折线
     */
    private void addLine() {
        try {
            LineData lineData = ChartDataUtil.getLineData();
            lineData.setLineColor(Color.GREEN);
            lineData.setFillColor(Color.BLUE);
            lineData.setTitle("数据3");
            lineChart.addLinesMap(lineData);
            removeTarget = lineChart.getLineTarget(lineData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
