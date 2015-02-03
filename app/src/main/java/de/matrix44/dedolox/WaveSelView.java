package de.matrix44.dedolox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import java.util.ArrayList;

/**
 * Created by rollo on 31.01.15.
 */
public class WaveSelView extends View {

  public interface WaveSelectionListener {
    void onWaveSelectionChanged(int newWave);
  }

  public WaveSelView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public WaveSelView(Context context) {
    super(context);
    init(context);
  }

  @Override
  public void onDraw(Canvas canvas) {
    if (buttonMovie != null) {

      int imageNo   = buttonDown ? imageCount - 1 : value;
      int imgWidth  = buttonMovie.getWidth()  / imageCount;
      int imgHeight = buttonMovie.getHeight();
      int imageX    = imageNo % imageCount;

      Rect srcRect   = new Rect();
      srcRect.top    = 0;
      srcRect.bottom = imgHeight;
      srcRect.left   = imageX * imgWidth;
      srcRect.right  = srcRect.left + imgWidth;

      Rect clientRect = new Rect();
      getDrawingRect(clientRect);

      canvas.drawBitmap(buttonMovie, srcRect, clientRect, moviePaint);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    if (event.getAction() == MotionEvent.ACTION_DOWN) {

      if (event.getX() > (getWidth() / 2) && event.getY() > (getHeight() / 4) && event.getY() < ((3 * getHeight()) / 4))
        buttonDown = true;

      invalidate();

      if (buttonDown && scrollView != null)
        scrollView.requestDisallowInterceptTouchEvent(true);
    }

    else if (event.getAction() == MotionEvent.ACTION_UP) {

      if (buttonDown) {
        Rect clientRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
        if (clientRect.contains(getLeft() + (int) event.getX(), getTop() + (int) event.getY())) {

          value++;
          if (value >= (imageCount - 1))
            value = 0;

          fireWaveSelectionChanged();
        }

        if (scrollView != null)
          scrollView.requestDisallowInterceptTouchEvent(false);
      }

      buttonDown = false;
      invalidate();
    }

    return true;
  }

  public void setValue(int val) {

    if (val == value)
      return;

    if (val < 0)
      value = 0;
    else if (val >= imageCount)
      value = imageCount - 1;
    else
      value = val;

    invalidate();

    fireWaveSelectionChanged();
  }

  public int getValue() {

    return value;
  }

  public void setWaveSelectionListener(WaveSelectionListener listener) {

    this.listeners.add(listener);
  }

  public void setScrollView(ViewParent target) {
    scrollView = target;
  }

  private void init(Context context) {

    imageCount = 5;
    buttonMovie = ResourceManager.getBitmap(context, R.drawable.wavesel);
  }

  private void fireWaveSelectionChanged() {

    for (WaveSelectionListener listener : listeners)
      listener.onWaveSelectionChanged(value);
  }

  private  ArrayList<WaveSelectionListener> listeners = new ArrayList<WaveSelectionListener>();
  private boolean buttonDown = false;
  private int imageCount = 0;
  private Bitmap buttonMovie = null;
  private Paint moviePaint = new Paint(0);
  private int value = 0;
  private ViewParent scrollView = null;
}
