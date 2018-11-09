package rannver.com.chartsdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rannver.com.chartsdemo.R;
import rannver.com.chartsdemo.chartUtil.LineData;
import rannver.com.chartsdemo.chartUtil.OnChartClickListener;
import rannver.com.chartsdemo.chartUtil.PointData;

/**
 * Created by  hqy on 2018/11/5
 * 多数据折线图View
 */
public class LinesChartView extends View {

    private final String TAG = LinesChartView.class.toString();

    //属性集
    protected int xyLineColor;
    protected float xyLineWidth;
    protected int backgroundColor;
    protected boolean scrollable;
    protected float xyTextSize;
    protected int xyTextColor;
    protected float xScaleLength;
    protected float threshold;
    protected boolean isThresholdLineShow;

    //画笔
    private Paint xyPaint; //xy轴画笔
    private Paint xyTextPaint; //xy轴文字画笔
    private Paint linePaint; //折线画笔
    private Paint fillPaint; //填充画笔
    private Paint thresholdPaint;//阈值线画笔


    //数据
    private Context context;
//    private List<PointData> pointDataList = new ArrayList<>();   //绘制图表的数据
    private float yTextPadding = 0;  //y轴距离
    private float xTextPadding = 0;  //x轴距离
    private int xStartPoint = 0;//x原点位置
    private int yStartPoint = 0;//y原点位置
    private float distance = 0;//滑动的距离
    private int width;//Layout宽
    private int height;//Layout高
    private float selectXPoint = -1;//点击事件选择的点的x坐标
    private float selectYPoint = -1;//点击事件选择的点的y坐标
    private int selectIndex = -1;//点击事件选择的点是第几个点
    private LineData selectlineData = null;//点击事件选择的折线
    private boolean isDrawSelectPoint = false;// 是否绘制触摸点
    private boolean isMoveTouch = false;//点击事件是否涉及MOVE事件
    private float xLength = 0;//X轴长度
    private float cooDensity;//Y轴坐标密度
    protected OnChartClickListener onChartClickListener = null; //坐标点的监听事件
    protected List<String> xAxisList = new ArrayList<>();//x坐标轴数据的集合


    protected HashMap<LineData,Integer> mLinesMap = new HashMap<>();//存放线的数据的集合


    public LinesChartView(Context context) {
        this(context,null);
    }

    public LinesChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LinesChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(context,attrs);
        initPaint();
    }

    //初始化画笔
    private void initPaint() {

        xyPaint = new Paint();
        xyTextPaint = new Paint();
        linePaint = new Paint();
        fillPaint = new Paint();
        thresholdPaint = new Paint();

        //xy轴画笔
        xyPaint.setAntiAlias(true);//抗锯齿
        xyPaint.setStrokeWidth(xyLineWidth);//xy轴的粗细
        xyPaint.setStrokeCap(Paint.Cap.ROUND);//设置线帽
        xyPaint.setColor(xyLineColor);//设置颜色
        xyPaint.setStyle(Paint.Style.STROKE);

        //xy轴文字画笔
        xyTextPaint.setAntiAlias(true);
        xyTextPaint.setTextSize(xyTextSize);
        xyTextPaint.setStrokeCap(Paint.Cap.ROUND);
        xyTextPaint.setColor(xyTextColor);
        xyTextPaint.setStyle(Paint.Style.STROKE); //设置描边

        //折线画笔
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(xyLineWidth);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStyle(Paint.Style.STROKE);

        //填充画笔
        fillPaint.setAntiAlias(true);

        //阈值线画笔
        thresholdPaint.setAntiAlias(true);
        thresholdPaint.setPathEffect(new DashPathEffect(new float[]{20f,10f}, 15));
        thresholdPaint.setStyle(Paint.Style.STROKE);

    }

    /**
     * 初始化LineChart属性
     */
    private void initAttrs(Context context, @Nullable AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.LineChartView);
        xyLineColor = array.getColor(R.styleable.LineChartView_xylinecolor,Color.BLACK);
        xyLineWidth = array.getFloat(R.styleable.LineChartView_xylinewidth,3);
        backgroundColor = array.getColor(R.styleable.LineChartView_backroundcolor,Color.WHITE);
        scrollable = array.getBoolean(R.styleable.LineChartView_scrollable,false);
        xyTextSize = array.getDimension(R.styleable.LineChartView_xytextsize,20);
        xyTextColor = array.getColor(R.styleable.LineChartView_xytextcolor,Color.BLACK);
        xScaleLength = array.getDimension(R.styleable.LineChartView_xyscalelength,50);
        threshold = array.getFloat(R.styleable.LineChartView_threshold,0);
        isThresholdLineShow = array.getBoolean(R.styleable.LineChartView_thresholdlineshow,false);

        array.recycle();

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        width = getWidth();
        height = getHeight();

        if (changed){
            //确定xy原点位置
            getMaxTextPadding();
            setXYPoint();
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);//背景颜色

        //绘制Y轴
        drawY(canvas);
        //新开图层达到滑动效果
        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        //绘制X轴
        drawX(canvas);
        //绘制折线
        drawAllLines(canvas);
        //绘制阈值线
        drawThresholdLine(canvas);
        //绘制选择的点
        drawSelectPoint(canvas);
        //保存图层
        save(canvas,layerId);
    }

    private void drawAllLines(Canvas canvas) {

        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            LineData lineData = (LineData) entry.getKey();
            List<PointData> pointDataList = lineData.getPointDataList();
            //绘制填充区域
            drawFillRegion(canvas,lineData.getFillColor(),pointDataList);
            //绘制点线
            drawPointAndLine(canvas,lineData);
        }

    }

    /**
     * 保存图层
     * @param layerId
     */
    private void save(Canvas canvas,int layerId) {
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(backgroundColor);
        linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        RectF rectF = new RectF(0,0,xStartPoint+yTextPadding,yStartPoint);
        canvas.drawRect(rectF,linePaint);
        linePaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    /**
     * 绘制Y轴
     * @param canvas
     */
    private void drawY(Canvas canvas) {
        float yEnd = yStartPoint-xTextPadding; //y轴终点（以屏幕坐标轴为参考系）
        //绘制y轴
        canvas.drawLine(xStartPoint+yTextPadding,0,xStartPoint+yTextPadding,yEnd,xyPaint);
        //绘制y轴箭头
        Path yPath = new Path();
        yPath.moveTo(xStartPoint+yTextPadding-dp2px(5),dp2px(12));
        yPath.lineTo(xStartPoint+yTextPadding,0);
        yPath.lineTo(xStartPoint+yTextPadding+dp2px(5),dp2px(12));
        canvas.drawPath(yPath,xyPaint);

        HashMap<Integer,Float> map = getAllNum();
        Iterator it = map.entrySet().iterator();
        int pointLineLength = dp2px(4);

        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            //Y轴的刻度和字符
            String yStr = "" + entry.getKey();
            float yPoint = (float) entry.getValue();
            Rect rectY = getTextBounds(yStr,xyTextPaint);
            float yTextIndexX = xStartPoint - xyLineWidth - dp2px(2) - rectY.width()/2;
            float yTextIndexY = yPoint + rectY.height()/2;
            canvas.drawLine(xStartPoint+yTextPadding,yPoint,xStartPoint+yTextPadding+pointLineLength,yPoint,xyPaint);
            canvas.drawText(yStr,0,yStr.length(),yTextIndexX,yTextIndexY,xyTextPaint);
        }
    }

    /**
     * 添加坐标点的位置，是否超出阈值的判断
     */
    private void setXYPoint() {

        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            LineData lineData = (LineData) entry.getKey();
            List<PointData> pointDataList = lineData.getPointDataList();

            float yEnd = yStartPoint-xTextPadding; //y轴终点（以屏幕坐标轴为参考系）
            for (int i = 0; i< pointDataList.size(); i++){

                //设置坐标
                cooDensity =  (yEnd - dp2px(24)) / getMaxYNumber(); //Y轴坐标密度
                float xPoint = dp2px((int) xScaleLength) * i + xStartPoint + yTextPadding;
                float yPoint = yEnd - cooDensity * pointDataList.get(i).getY();
                pointDataList.get(i).setxPoint(xPoint);
                pointDataList.get(i).setyPoint(yPoint);

                //设置点的颜色
                if (threshold<=0){
                    break;
                }
                if (pointDataList.get(i).getY()<=threshold){
                    pointDataList.get(i).setPointColor(lineData.getPointColor());
                }else {
                    pointDataList.get(i).setPointColor(Color.RED);
                }

            }
        }
    }

    /**
     * 绘制阈值线
     */
    private void drawThresholdLine(Canvas canvas) {

        if (!isThresholdLineShow || threshold <= 0){
            return ;
        }

        float thresholdLineYIndex = yStartPoint - xTextPadding - threshold * cooDensity;
        Path path = new Path();
        path.moveTo(xStartPoint + yTextPadding,thresholdLineYIndex);
        path.lineTo(xLength - distance,thresholdLineYIndex);
        thresholdPaint.setColor(Color.GRAY);
        thresholdPaint.setAlpha(100);
        canvas.drawPath(path,thresholdPaint);

    }

    /**
     * 绘制选择的点
     * @param canvas
     */
    private void drawSelectPoint(Canvas canvas) {

        if (selectXPoint == -1 && selectYPoint == -1 || !isDrawSelectPoint){
            return;
        }

        //画星星
        drawStar(canvas);

    }

    /**
     * 在选择点画星星
     * @param canvas
     */
    private void drawStar(Canvas canvas) {

        float r = dp2px(13);
        float xA = selectXPoint;
        float yA = selectYPoint - r/2;
        float xD = (float) (xA - r * Math.sin(Math.toRadians(18)));
        float xC = (float) (xA + r * Math.sin(Math.toRadians(18)));
        float yD = (float) (yA + Math.cos(Math.toRadians(18)) * r);
        float yC = (float) (yA + Math.cos(Math.toRadians(18)) * r);
        float yB = (float) (yA + Math.sqrt(Math.pow((xC - xD), 2) - Math.pow((r / 2), 2)));
        float yE = (float) (yA + Math.sqrt(Math.pow((xC - xD), 2) - Math.pow((r / 2), 2)));
        float xB = xA + (r / 2);
        float xE = xA - (r / 2);

        float[] floats = new float[]{xA, yA,  xD, yD,xB, yB, xE, yE, xC, yC,xA, yA};

        Path starPath = new Path();
        starPath.moveTo(xA,yA);

        //画星星
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(selectlineData.getPointDataList().get(selectIndex).getPointColor());
            for (int i = 0;i<floats.length;i++){
            starPath.lineTo(floats[i],floats[i+1]);
            i++;
        }
        canvas.drawPath(starPath,linePaint);
        isDrawSelectPoint = false;

    }

    /**
     * 绘制填充区域
     */
    private void drawFillRegion(Canvas canvas, int fillColor, List<PointData> pointDataList) {
        Path fillPath = new Path();
        fillPath.moveTo(pointDataList.get(0).getxPoint()-distance,yStartPoint- xTextPadding);
        for (PointData pointData : pointDataList){
            fillPath.lineTo(pointData.getxPoint()-distance, pointData.getyPoint());
        }
        float lastX = pointDataList.get(pointDataList.size()-1).getxPoint();
        fillPath.lineTo(lastX,yStartPoint - xTextPadding);

        LinearGradient linearGradient = new LinearGradient(0,0,0,getMeasuredHeight(),new int[]{fillColor,Color.TRANSPARENT},null,LinearGradient.TileMode.CLAMP);
        fillPaint.setShader(linearGradient);
        fillPaint.setColor(fillColor);

        canvas.drawPath(fillPath,fillPaint);
    }

    /**
     *绘制坐标点和折线
     */
    private void drawPointAndLine(Canvas canvas, LineData lineData) {

        List<PointData> pointDataList = lineData.getPointDataList();

        if (pointDataList.size()<=0){
            return ;
        }

        //画折线
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineData.getLineColor());
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        Path linePath = new Path();
        linePath.moveTo(pointDataList.get(0).getxPoint()-distance, pointDataList.get(0).getyPoint());
        for (PointData pointData : pointDataList){
            linePath.lineTo(pointData.getxPoint()-distance, pointData.getyPoint());
        }
        //保留后续再做的平滑曲线部分
//        for (int i = 0;i<pointDataList.size()-1;i++){
//            PointData start = pointDataList.get(i);
//            PointData end = pointDataList.get(i+1);
//            float x1 = (start.getxPoint() + end.getxPoint())/2;
//            float y1 = start.getyPoint();
//            float x2 = (start.getxPoint() + end.getxPoint())/2;
//            float y2 = end.getyPoint();
//            linePath.cubicTo(x1,y1,x2,y2,end.getxPoint(),end.getyPoint());
//        }
        canvas.drawPath(linePath,linePaint);

        //画点
        float radiusMax = dp2px(3);
        float radiusMin = dp2px(2);
        for (PointData pointData : pointDataList){
            linePaint.setStyle(Paint.Style.FILL);
            linePaint.setColor(pointData.getPointColor());
            canvas.drawCircle(pointData.getxPoint()-distance, pointData.getyPoint(),radiusMax,linePaint);

            if (backgroundColor == Color.TRANSPARENT){
                linePaint.setColor(Color.WHITE);
            }else {
                linePaint.setColor(backgroundColor);
            }
            canvas.drawCircle(pointData.getxPoint()-distance, pointData.getyPoint(),radiusMin,linePaint);
        }

    }

    /**
     * 绘制X轴
     * @param canvas
     */
    private void drawX(Canvas canvas) {

        xLength = dp2px((int) (xAxisList.size()*xScaleLength+12));//x轴的长度12dp预留x轴画箭头
        float xEnd = xStartPoint+yTextPadding+xLength; //x轴的终点(以屏幕坐标轴为参考系)
        float yEnd = yStartPoint-xTextPadding; //y轴终点（以屏幕坐标轴为参考系）

        //绘制x轴
        canvas.drawLine(xStartPoint+yTextPadding,yStartPoint-xTextPadding,xEnd-distance,yStartPoint-xTextPadding,xyPaint);
        //绘制X轴箭头
        xyPaint.setStyle(Paint.Style.STROKE);
        Path xPath = new Path();
        xPath.moveTo(xEnd-dp2px(12)-distance,yEnd-dp2px(5));
        xPath.lineTo(xEnd-distance,yStartPoint-xTextPadding);
        xPath.lineTo(xEnd-dp2px(12)-distance,yEnd+dp2px(5));
        canvas.drawPath(xPath,xyPaint);


        //绘制X轴刻度
        int pointLineLength = dp2px(4);
        for (int i = 0; i< xAxisList.size(); i++){

            float xPoint =dp2px((int) xScaleLength) * i + xStartPoint + yTextPadding;
            String x = xAxisList.get(i);

            //x轴的刻度和字符
            Rect rectX = getTextBounds(x,xyTextPaint);
            float xTextIndexY = yStartPoint+xyLineWidth+dp2px(2)+rectX.height();
            float xTextIndexX = xPoint-rectX.width()/2;
            canvas.drawLine(xPoint-distance,yStartPoint-xTextPadding,xPoint-distance,yStartPoint-xTextPadding-pointLineLength,xyPaint);
            if (isDrawSelectPoint && i == selectIndex){
                xyTextPaint.setTextSize(xyTextSize+4);
            }
            canvas.drawText(x,0,x.length(),xTextIndexX-distance,xTextIndexY,xyTextPaint);
            xyTextPaint.setTextSize(xyTextSize);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                selectXPoint = event.getX();
                selectYPoint = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                selectTouchPoint();
                isMoveTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isMoveTouch = true;
                if (scrollable){
                    moveEventProcess(event);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 处理Move事件
     * @param event
     */
    private void moveEventProcess(MotionEvent event) {

        //滑动的距离
        float dis = selectXPoint - event.getX();
        selectXPoint = event.getX();
        //x轴总长
        float diffLength = xStartPoint + xTextPadding + xLength - width;
        if (width < xLength){

            if (0<diffLength-distance-dis && diffLength-distance-dis<diffLength){
                distance += dis;
            }else if (dis<0){
                distance = 0;
            }else if (dis>0){
                distance = diffLength;
            }
        }

        invalidate();

    }

    /**
     * 选择离触摸点最近的坐标点
     */
    private void selectTouchPoint() {

        if (isMoveTouch){
            return;
        }

        int selectR = dp2px(5);

        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            LineData lineData = (LineData) entry.getKey();
            List<PointData> pointDataList = lineData.getPointDataList();
            for (int i = 0; i< pointDataList.size(); i++){
                float x = pointDataList.get(i).getxPoint() - distance;
                float y = pointDataList.get(i).getyPoint();

                boolean isXNearby = ( selectXPoint <= x + selectR )&&( x - selectR <= selectXPoint );//是否在坐标点附近
                boolean isYNearby = ( selectYPoint <= y + selectR ) && ( y - selectR <= selectYPoint);

                if (isXNearby && isYNearby && !isMoveTouch){
                    selectXPoint = x;
                    selectYPoint = y;
                    selectIndex = i;
                    isDrawSelectPoint = true;
                    selectlineData = lineData;
                }
            }
        }

        invalidate();//更新视图

        if (onChartClickListener!=null){
            onChartClickListener.onClick(selectlineData.getPointDataList().get(selectIndex),selectIndex);
        }
    }

    /**
     *获取Y轴不重复的数据
     * @return
     */
    private HashMap<Integer,Float> getAllNum() {

        HashMap<Integer,Float> YMap = new HashMap<>();

        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            LineData lineData = (LineData) entry.getKey();
            List<PointData> pointDataList = lineData.getPointDataList();
            for (PointData pointData : pointDataList){
                if((!YMap.containsKey(pointData.getY())) && isYScale(YMap, pointData.getY())){
                    YMap.put(pointData.getY(),pointData.getyPoint());
                }
            }
        }

        return YMap;
    }

    /**
     * 判断是否允许作为刻度显示在Y轴上
     */
    private boolean isYScale(HashMap<Integer,Float> map,int target){
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            int y = (int) entry.getKey();
            if (Math.abs(target - y )<5 && y!=target ){
                return false;
            }
        }

        return true;
    }

    /**
     * 获取Y轴最大数据
     * @return
     */
    private int getMaxYNumber(){
        int max = 0;
        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            LineData lineData = (LineData) entry.getKey();
            List<PointData> pointDataList = lineData.getPointDataList();
            max = pointDataList.get(0).getY();
            for (PointData data: pointDataList){
                if (data.getY()>max){
                    max = data.getY();
                }
            }
        }
        return max;
    }

    /**
     * 获取XY轴原点距离
     */
    private void getMaxTextPadding() {
        Iterator it = mLinesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            LineData lineData = (LineData) entry.getKey();
            List<PointData> pointDataList = lineData.getPointDataList();
            //设置y轴文字的最大距离
            for (int i = 0; i< pointDataList.size(); i++){
                float padding = getTextBounds(""+ pointDataList.get(i).getY(),xyTextPaint).width();
                if (padding>yTextPadding){
                    yTextPadding = padding;
                }
            }
        }
        //设置x轴文字的最大距离
        for (String x:xAxisList){
            float padding = getTextBounds(x,xyTextPaint).height();
            if (padding>xTextPadding){
                xTextPadding = padding;
            }
        }
        //设置xy轴原点位置
        xStartPoint = (int) ( yTextPadding + xyLineWidth );
        yStartPoint = height - dp2px((int) (xTextPadding - xyLineWidth));
    }

    /**
     * 获取文字长宽
     * @param str 要获取的文字
     * @param paint 画笔
     * @return 保存宽高的矩阵
     */
    private Rect getTextBounds(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str,0,str.length(),rect);
        return rect;
    }

    private int dp2px(int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()) + 0.5);
    }

}
