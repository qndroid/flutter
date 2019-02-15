package com.youdu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.youdu.vuandroidadsdk.R;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by renzhiqiang on 2018/4/16.
 * function:登陆图型验证码
 */
public class ValidateImageView extends View {

  /**
   * 控件的宽度
   */
  private int mWidth;
  /**
   * 控件的高度
   */
  private int mHeight;
  /**
   * 验证码文本画笔
   */
  private Paint mTextPaint; // 文本画笔
  /**
   * 干扰点坐标的集合
   */
  private ArrayList<PointF> mPoints = new ArrayList<>();
  private Random mRandom = new Random();
  /**
   * 干扰点画笔
   */
  private Paint mPointPaint;
  /**
   * 绘制贝塞尔曲线的路径集合
   */
  private ArrayList<Path> mPaths = new ArrayList<>();
  /**
   * 干扰线画笔
   */
  private Paint mPathPaint;
  /**
   * 验证码字符串
   */
  private String mCodeString;
  /**
   * 验证码的位数
   */
  private int mCodeCount;
  /**
   * 验证码字符的大小
   */
  private float mTextSize;
  /**
   * 验证码字符串的显示宽度
   */
  private float mTextWidth;

  public ValidateImageView(Context context) {
    this(context, null);
  }

  public ValidateImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ValidateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    getAttrValues(context, attrs);
    init();
  }

  /**
   * 获取布局文件中的值
   */
  private void getAttrValues(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndentifyingCode);
    mCodeCount =
        typedArray.getInteger(R.styleable.IndentifyingCode_codeCount, 4); // 获取布局中验证码位数属性值，默认为5个
    // 获取布局中验证码文字的大小，默认为20sp
    mTextSize = typedArray.getDimension(R.styleable.IndentifyingCode_textSize,
        typedArray.getDimensionPixelSize(R.styleable.IndentifyingCode_textSize,
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                getResources().getDisplayMetrics())));
    typedArray.recycle();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mHeight = h;
    mWidth = w;
    resetData();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measureWidth = measureWidth(widthMeasureSpec);
    int measureHeight = measureHeight(heightMeasureSpec);

    setMeasuredDimension(measureWidth, measureHeight);
  }

  @Override protected void onDraw(Canvas canvas) {
    int length = mCodeString.length();
    float charLength = mTextWidth / length;
    //每位验证码的绘制
    for (int i = 1; i <= length; i++) {
      int offsetDegree = mRandom.nextInt(15);
      //这里只会产生0和1，如果是1那么正旋转正角度，否则旋转负角度
      offsetDegree = mRandom.nextInt(2) == 1 ? offsetDegree : -offsetDegree;
      canvas.save();
      canvas.rotate(offsetDegree, mWidth / 2, mHeight / 2);
      //给画笔设置随机颜色
      mTextPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20,
          mRandom.nextInt(200) + 20);
      canvas.drawText(String.valueOf(mCodeString.charAt(i - 1)), (i - 1) * charLength * 1.6f + 30,
          mHeight * 2 / 3f, mTextPaint);
      canvas.restore();
    }

    //干扰点绘制
    for (PointF pointF : mPoints) {
      mPointPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20,
          mRandom.nextInt(200) + 20);
      canvas.drawPoint(pointF.x, pointF.y, mPointPaint);
    }

    //干扰线绘制
    for (Path path : mPaths) {
      mPathPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20,
          mRandom.nextInt(200) + 20);
      canvas.drawPath(path, mPathPaint);
    }
  }

  private void resetData() {
    //生成随机数字和字母组合
    mCodeString = getRandomText();
    mTextWidth = mTextPaint.measureText(mCodeString);
    // 生成干扰点坐标
    mPoints.clear();
    for (int i = 0; i < 40; i++) {
      PointF pointF = new PointF(mRandom.nextInt(mWidth) + 10, mRandom.nextInt(mHeight) + 10);
      mPoints.add(pointF);
    }
    // 生成干扰线坐标
    mPaths.clear();
    for (int i = 0; i < 2; i++) {
      Path path = new Path();
      int startX = mRandom.nextInt(mWidth / 3) + 10;
      int startY = mRandom.nextInt(mHeight / 3) + 10;
      int endX = mRandom.nextInt(mWidth / 2) + mWidth / 2 - 10;
      int endY = mRandom.nextInt(mHeight / 2) + mHeight / 2 - 10;
      path.moveTo(startX, startY);
      path.quadTo(Math.abs(endX - startX) / 2, Math.abs(endY - startY) / 2, endX, endY);
      mPaths.add(path);
    }
  }

  //初始化
  private void init() {
    // 初始化文字画笔
    mTextPaint = new Paint();
    mTextPaint.setStrokeWidth(3); // 画笔大小为3
    mTextPaint.setTextSize(mTextSize); // 设置文字大小
    // 初始化干扰点画笔
    mPointPaint = new Paint();
    mPointPaint.setStrokeWidth(6);
    mPointPaint.setStrokeCap(Paint.Cap.ROUND); // 设置断点处为圆形
    // 初始化干扰线画笔
    mPathPaint = new Paint();
    mPathPaint.setStrokeWidth(5);
    mPathPaint.setColor(Color.GRAY);
    mPathPaint.setStyle(Paint.Style.STROKE); // 设置画笔为空心
    mPathPaint.setStrokeCap(Paint.Cap.ROUND); // 设置断点处为圆形
  }

  //测量宽度
  private int measureWidth(int widthMeasureSpec) {
    int result = (int) (mTextWidth * 1.8f);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    if (widthMode == MeasureSpec.EXACTLY) {
      result = widthSize; // 既然是精确模式，那么直接返回测量的宽度即可
    } else {
      if (widthMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, widthSize);
      }
    }
    return result;
  }

  //测量高度
  private int measureHeight(int heightMeasureSpec) {
    int result = (int) (mTextWidth / 1.6f);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    if (heightMode == MeasureSpec.EXACTLY) {
      result = heightSize; // 既然是精确模式，那么直接返回测量的宽度即可
    } else {
      if (heightMode == MeasureSpec.AT_MOST) {
        //wrap_content
        result = Math.min(result, heightSize);
      }
    }
    return result;
  }

  //生成指定位数的验证码
  private String getRandomText() {
    String generatedCaptcha = "";
    for (int index = 0; index < mCodeCount; index++) {
      String temp = generateRandomText();
      generatedCaptcha = generatedCaptcha + temp;
    }
    return generatedCaptcha;
  }

  //生成每一位验证码数据
  private String generateRandomText() {
    String[] charest = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
        "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z"
    };
    String temp = charest[mRandom.nextInt(charest.length)];
    return temp;
  }

  //供外部调用，是否输入一致
  public boolean isValidate(String origin) {
    return origin.equalsIgnoreCase(mCodeString);
  }

  //重新生成验证码并绘制
  public void reCaptchaCode() {
    resetData();
    invalidate();
  }
}
