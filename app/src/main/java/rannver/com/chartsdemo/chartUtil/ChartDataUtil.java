package rannver.com.chartsdemo.chartUtil;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by  hqy on 2018/11/6
 * 表格数据提供工具类
 */
public class ChartDataUtil {

    public static List<PointData> getLineChartData(){
        List<PointData> list = new ArrayList<>();
        for (int i = 0;i<10;i++){
            PointData data = new PointData();
            Random random = new Random();
            data.setX((i+1)+"月");
            data.setY(random.nextInt(100)+1);
            list.add(data);
        }
        PointData data = new PointData();
        data.setX("");
        data.setyPoint(0);
        list.add(data);
        return list;
    }

    public static LineData getLineData(){
        LineData lineData = new LineData();
        lineData.setPointDataList(getLineChartData());
        int count = 0;

        List<String> list = new ArrayList<>();
        for (int i = 0;i<10;i++){
            list.add((i+1)+"月");
        }
        lineData.setLineColor(Color.GREEN);
        lineData.setFillColor(Color.BLUE);
        lineData.setTitle("数据"+(count++));
        lineData.setxAxisList(list);
        return lineData;
    }

}
