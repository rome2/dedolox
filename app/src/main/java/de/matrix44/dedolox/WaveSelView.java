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
    init();
  }

  public WaveSelView(Context context) {
    super(context);
    init();
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

      buttonDown = true;
      invalidate();
    }

    else if (event.getAction() == MotionEvent.ACTION_UP) {

      buttonDown = false;

      Rect clientRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
      if (clientRect.contains(getLeft() + (int) event.getX(), getTop() + (int) event.getY())) {


        value++;
        if (value >= (imageCount - 1))
          value = 0;

        fireWaveSelectionChanged();
      }

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

  private void init() {

    imageCount = 5;
    buttonMovie = BitmapFactory.decodeResource(this.getResources(), R.drawable.wavesel);
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
}
