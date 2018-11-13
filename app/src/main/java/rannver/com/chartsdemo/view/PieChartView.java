package rannver.com.chartsdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rannver.com.chartsdemo.R;
import rannver.com.chartsdemo.chartUtil.PieData;

/**
 * Created by  hqy on 2018/11/12
 * 饼图View
 */
public class PieChartView extends View {

    private final String TAG = this.getClass().toString();

    private final int PIETYPE_SOLID = 0;
    private final int PIETYPE_HOLLOW = 1;
    private final int TITLEINDEX_TOP = 0;
    private final int TITLEINDEX_BOTTM = 1;
    private final int TITLEINDEX_CENTER = 2;


    //属性集
    protected int pieType;
    protected int titleIndex;
    protected String title;

    //画笔
    private Paint piePaint;//饼画笔
    private Paint textPaint;//文字画笔
    private Paint linePaint;//分隔线画笔

    //数据
    private Context context;
    private float height;//布局高
    private float width;//布局宽
    private float centerX;//圆心x坐标
    private float centerY;//圆心y坐标
    private float radius;//半径
    private float innerRadius = 0;//内圆半径
    private float touchX;//手指触摸屏幕的x坐标
    private float touchY;//手指触碰屏幕的y坐标
    private int selectIndex = -1;//触摸事件的选择区域
    private boolean isMoveEvent = false;//是否触发了MOVE事件
    protected List<PieData> pieList = new ArrayList<>();//饼图数据
    protected float startAngel = 1;//起始角度
    protected float totalPercent;//总百分比

    public PieChartView(Context context) {
        this(context,null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(context,attrs);
        initPaint();
    }

    /**
     * 画笔初始化
     */
    private void initPaint() {

        piePaint = new Paint();
        textPaint = new Paint();
        linePaint = new Paint();

        //饼画笔初始化
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Paint.Style.FILL);

        //文字画笔初始化
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);

        //分隔线画笔初始化
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5f);

    }

    /**
     * 属性初始化
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
        pieType = typedArray.getInteger(R.styleable.PieChartView_pietype,0);//饼图类型，0为实心，1为空心
        titleIndex = typedArray.getInteger(R.styleable.PieChartView_titleIndex,-1);//标题显示位置
        title = typedArray.getString(R.styleable.PieChartView_title);//标题

        if (!title.equals("") && titleIndex==-1){
            titleIndex = TITLEINDEX_TOP;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        centerX = width / 2;
        centerY = height / 2;
        radius = centerX - dp2px(15);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        centerX = w/2;
        centerY = h/2;
        radius = centerX - dp2px(15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算角度
        getAngle();
        //画饼
        drawPie(canvas);
        //绘制点击区域
        setSelectSpace(canvas);
        //画内圆
        drawInnerCircle(canvas);
        //画文字
        drawText(canvas);
        //画标题
        setTitle(canvas);
    }

    /**
     * 绘制点击区域
     * @param canvas
     */
    private void setSelectSpace(Canvas canvas) {
        if (selectIndex == -1){
            return;
        }
        float tempR = radius + dp2px(5);
        RectF rectF = new RectF(centerX - tempR,centerY - tempR,centerX + tempR,centerY + tempR);
        piePaint.setColor(pieList.get(selectIndex).getColor());
        canvas.drawArc(rectF,pieList.get(selectIndex).getStartAngel(),pieList.get(selectIndex).getRealPercent()-1,true,piePaint);
    }

    /**
     * 画标题
     * @param canvas
     */
    private void setTitle(Canvas canvas) {
        textPaint.setTextSize(64);
        textPaint.setColor(Color.BLACK);
        switch (titleIndex){
            case TITLEINDEX_TOP:
                canvas.drawText(title,centerX - getTextBounds(title,textPaint).width()/2,centerY - radius - (centerY - radius)/2,textPaint);
                break;
            case TITLEINDEX_BOTTM:
                canvas.drawText(title,centerX - getTextBounds(title,textPaint).width()/2,centerY + radius + (centerY - radius)/2,textPaint);
                break;
            case TITLEINDEX_CENTER:
                canvas.drawText(title,centerX - getTextBounds(title,textPaint).width()/2,centerY + getTextBounds(title,textPaint).height(),textPaint);
                break;
            default:
                break;
        }
    }

    /**
     * 计算真实角度
     */
    private void getAngle() {
        totalPercent = getTotal();
        int tempAngel = 0;
        for (PieData pieData:pieList){
            float angel = (pieData.getPercent()/totalPercent)*360f;
            //真实角度
            pieData.setRealPercent(angel);
            //起始角度和结束角度
            pieData.setStartAngel(tempAngel+1);
            pieData.setEndAngel(tempAngel+angel-1);
            //对应的文字的xy轴
            float textAngel = ( pieData.getEndAngel() + pieData.getStartAngel() ) / 2;
            pieData.setTextXPoint((float) (centerX + radius * 3/4 * Math.cos(Math.toRadians(textAngel))));
            pieData.setTextYPoint((float) (centerY + radius * 3/4 * Math.sin(Math.toRadians(textAngel))));
            tempAngel += angel;
        }
    }

    /**
     * 画文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        textPaint.setTextSize(36);
        textPaint.setColor(Color.WHITE);
        for (PieData pieData:pieList){
            canvas.drawText(pieData.getTitle(),pieData.getTextXPoint(),pieData.getTextYPoint(),textPaint);
            canvas.drawText((int)(pieData.getPercent()*100)+"%",pieData.getTextXPoint(),pieData.getTextYPoint()+getTextBounds(pieData.getTitle(),textPaint).height()+dp2px(2),textPaint);
        }
    }

    /**
     * 画内圆
     * @param canvas
     */
    private void drawInnerCircle(Canvas canvas) {
        if (pieType == PIETYPE_SOLID){
            return;
        }
        innerRadius = radius/2;
        piePaint.setColor(Color.WHITE);
        canvas.drawCircle(centerX,centerY,innerRadius,piePaint);
    }

    /**
     * 画饼
     * @param canvas
     */
    private void drawPie(Canvas canvas) {
        RectF rectF = new RectF(centerX - radius,centerY - radius,centerX + radius,centerY +radius);
        if (startAngel<=360f){
            //动画效果
            for (PieData pieData:pieList){
                if (startAngel>pieData.getEndAngel()){
                    piePaint.setColor(pieData.getColor());
                    canvas.drawArc(rectF,pieData.getStartAngel(),pieData.getRealPercent()-1,true,piePaint);
                } else if (pieData.getStartAngel()<=startAngel && startAngel <= pieData.getEndAngel()) {
                    piePaint.setColor(pieData.getColor());
                    canvas.drawArc(rectF,pieData.getStartAngel(),startAngel-pieData.getStartAngel(),true,piePaint);
                }
            }
            startAngel += 3;
            invalidate();
        }else {
            //显示全体
            for (PieData pieData:pieList){
                piePaint.setColor(pieData.getColor());
                canvas.drawArc(rectF,pieData.getStartAngel(),pieData.getRealPercent()-1,true,piePaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX() - centerX;
                touchY = event.getY() - centerY;
                break;
            case MotionEvent.ACTION_UP:
                selectTouchIndex();
                isMoveEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isMoveEvent = true;
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * touch事件的选择区域
     */
    private void selectTouchIndex() {
        if (isMoveEvent){
            return;
        }

        //是否在点击范围内
        float touchR = (float) Math.sqrt(touchX * touchX + touchY * touchY );
        if (innerRadius > touchR || touchR > radius){
            return;
        }

        //计算夹角
        float touchAngel;
        touchAngel = (float) Math.toDegrees(Math.atan2(touchY,touchX));
        touchAngel = touchAngel<0 ? touchAngel+360 : touchAngel;
//        Log.d(TAG, "selectTouchIndex: touchAngel = " + touchAngel );
        for (int i = 0;i<pieList.size();i++){
            if (pieList.get(i).getStartAngel()<=touchAngel && touchAngel<=pieList.get(i).getEndAngel()){
                selectIndex = i;
            }
        }
        invalidate();
    }

    /**
     * 获取总百分比
     * @return
     */
    private float getTotal() {
        float total = 0;
        for (PieData pieData:pieList){
            total += pieData.getPercent();
        }
        return total;
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
