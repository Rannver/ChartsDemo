package rannver.com.chartsdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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

    //属性集
    private int pieType;
    private int titleIndex;
    private String title;

    //画笔
    private Paint piePaint;//饼画笔
    private Paint textPaint;//文字画笔

    //数据
    private Context context;
    private float height;//布局高
    private float width;//布局宽
    private float centerX;//圆心x坐标
    private float centerY;//圆心y坐标
    private float startAngel;//起始角度
    protected List<PieData> pieList = new ArrayList<>();//饼图数据
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

        //饼画笔初始化
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Paint.Style.FILL);

        //文字画笔初始化
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);

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
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        centerX = w/2;
        centerY = h/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画饼
        drawPie(canvas);
        //画线
        drawLine(canvas);
    }

    /**
     * 画线
     * @param canvas
     */
    private void drawLine(Canvas canvas) {

    }

    /**
     * 画饼
     * @param canvas
     */
    private void drawPie(Canvas canvas) {
        totalPercent = getTotal();
        float radius = centerX - dp2px(15);
        RectF rectF = new RectF(centerX - radius,centerY - radius,centerX + radius,centerY +radius);
        for (PieData pieData:pieList){
            piePaint.setColor(pieData.getColor());
            canvas.drawArc(rectF,startAngel,(pieData.getPercent()/totalPercent)*360f,true,piePaint);
            startAngel += (pieData.getPercent()/totalPercent)*360f;
        }
        startAngel = 0;
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


    private int dp2px(int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()) + 0.5);
    }

}
