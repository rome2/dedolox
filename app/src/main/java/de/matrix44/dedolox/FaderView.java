package de.matrix44.dedolox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import java.util.ArrayList;

/**
 * Created by rollo on 20.03.15.
 */
public class FaderView extends View {

  public interface FaderListener {
    void onValueChanged(double newVal);
  }

  public FaderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public FaderView(Context context) {
    super(context);
    init(context);
  }

  @Override
  public void onDraw(Canvas canvas) {

    if (faderHandle != null) {

      Rect destRect = new Rect();
      getDrawingRect(destRect);

      int y = (int)((1.0 - value) * (destRect.height() - destRect.width()));
      destRect.set(0, y, destRect.width(), destRect.width() + y);

      Rect srcRect = new Rect(0, 0, faderHandle.getWidth(), faderHandle.getHeight());

      canvas.drawBitmap(faderHandle, srcRect, destRect, faderPaint);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    if (event.getAction() == MotionEvent.ACTION_DOWN) {

      if (!faderDown) {
        Rect destRect = new Rect();
        getDrawingRect(destRect);
        int y1 = (int) ((1.0 - value) * (destRect.height() - destRect.width()));
        int y2 = destRect.width() + y1;
        if (event.getY() > y1 && event.getY() < y2)
          faderDown = true;

        if (faderDown) {
          // Save start values:
          startVal = value;
          startY = event.getY();

          if (scrollView != null)
            scrollView.requestDisallowInterceptTouchEvent(true);
        }
      }
    }

    else if (event.getAction() == MotionEvent.ACTION_UP) {

      if (faderDown) {
        if (scrollView != null)
          scrollView.requestDisallowInterceptTouchEvent(false);
        faderDown = false;
      }
    }

    else if (event.getAction() == MotionEvent.ACTION_MOVE) {

      if (faderDown) {

        // Calc movement in pixels:
        double dy = startY - event.getY();

        // Get movable range:
        Rect destRect = new Rect();
        getDrawingRect(destRect);
        double linearSize = destRect.height() - destRect.width();

        // Scale into pixel range:
        double diff = dy / linearSize;

        // Set new value relative to the start value:
        setValue(startVal + diff);
      }
    }

    return true;
  }

  public void setValue(double val) {

    if (val < 0.0)
      value = 0.0;
    else if (val > 1.0)
      value = 1.0;
    else
      value = val;
    invalidate();
    fireValueChanged();
  }

  public double getValue() {
    return value;
  }

  public void setScrollView(ViewParent target) {
    scrollView = target;
  }

  public void setFaderListener(FaderListener listener) {

    this.listeners.add(listener);
  }

  private void init(Context context) {

    faderHandle = ResourceManager.getBitmap(context, R.drawable.faderhandle);
  }

  private void fireValueChanged() {

    for (FaderListener listener : listeners)
      listener.onValueChanged(value);
  }

  private ArrayList<FaderListener> listeners = new ArrayList<FaderListener>();
  private double startVal = 0.0;
  private double startY;
  private Bitmap faderHandle = null;
  private Paint faderPaint = new Paint(0);
  private double value = 0.0;
  private ViewParent scrollView = null;
  private boolean faderDown = false;
}
