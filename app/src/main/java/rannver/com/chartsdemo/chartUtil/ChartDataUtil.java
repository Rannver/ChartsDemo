package rannver.com.chartsdemo.chartUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by  hqy on 2018/11/6
 * 表格数据提供工具类
 */
public class ChartDataUtil {

    public static List<LineData> getLineChartData(){
        List<LineData> list = new ArrayList<>();
        for (int i = 0;i<10;i++){
            LineData data = new LineData();
            Random random = new Random();
            data.setX((i+1)+"月");
            data.setY(random.nextInt(100)+1);
            list.add(data);
        }
        LineData data = new LineData();
        data.setX("");
        data.setyPoint(0);
        list.add(data);
        return list;
    }

}
