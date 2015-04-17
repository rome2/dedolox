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
 * Created by rollo on 11.01.15.
 */
public class PotView extends View {

  public interface PotListener {
    void onValueChanged(float newVal);
  }

  public PotView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PotView(Context context) {
    super(context);
    init(context);
  }

  @Override
  public void onDraw(Canvas canvas) {

    if (potMovie != null) {

      int csteps = controlSteps - 1;
      float dval = ((float)((int)(value * csteps))) / csteps;
      int imageNo = (int)(((imageCountX * imageCountY) - 1) * dval);
      int imgWidth  = potMovie.getWidth()  / imageCountX;
      int imgHeight = potMovie.getHeight() / imageCountY;
      int imageX = imageNo % imageCountX;
      int imageY = imageNo / imageCountX;

      Rect srcRect = new Rect();
      srcRect.top = imageY * imgHeight;
      srcRect.bottom = srcRect.top + imgHeight;
      srcRect.left = imageX * imgWidth;
      srcRect.right = srcRect.left + imgWidth;

      Rect clientRect = new Rect();
      getDrawingRect(clientRect);
      canvas.drawBitmap(potMovie, srcRect, clientRect, moviePaint);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    if (event.getAction() == MotionEvent.ACTION_DOWN) {

      if (event.getX() > (getWidth() / 4) && event.getY() > (getHeight() / 4) && event.getX() < ((3 * getWidth()) / 4) && event.getY() < ((3 * getHeight()) / 4)) {

        knobDown = true;

        // Use circular mode?
        if (circularMode) {

          // Get value from the mouse position point:
          startVal = valueFromMousePos(event.getX(), event.getY());

          // Make value current if needed:
          if (absoluteMode)
            setValue(startVal);
        }

        // No, linear is the way to go:
        else {
          // Save start values:
          startVal = value;
          startY = event.getY();
        }

        if (scrollView != null)
          scrollView.requestDisallowInterceptTouchEvent(true);
      }
    }

    else if (event.getAction() == MotionEvent.ACTION_UP) {

      if (knobDown && scrollView != null)
        scrollView.requestDisallowInterceptTouchEvent(false);
      knobDown = false;
    }

    else if (event.getAction() == MotionEvent.ACTION_MOVE) {

      if (knobDown) {

        // Running in circles?
        if (circularMode) {

          // Get value from the mouse position point:
          float val = valueFromMousePos(event.getX(), event.getY());

          // Set value:
          if (absoluteMode)
            setValue(val);
          else {

            // Set new value relative to the last value:
            setValue(value + (val - startVal));

            // Save current value for the next round:
            startVal = val;
          }
        }

        // No, we're imitating a fader:
        else {
          // Calc movement in pixels:
          float dy = startY - event.getY();

          // Scale into a more usable range:
          float diff = dy / linearSize;

          // Set new value relative to the start value:
          setValue(startVal + diff);
        }
      }
    }

    return true;
  }

  public void setValue(float val) {

    if (val < 0.0f)
      value = 0.0f;
    else if (val > 1.0f)
      value = 1.0f;
    else
      value = val;
    invalidate();
    fireValueChanged();
  }

  public float getValue() {
    return value;
  }

  public void setScrollView(ViewParent target) {
    scrollView = target;
  }

  public void setControlSteps(int stepCount) {
    controlSteps = stepCount;
  }

  public void setPotListener(PotListener listener) {

    this.listeners.add(listener);
  }

  public void blockUpdates(boolean block) {
    if (block)
      blockCount++;
    else
      blockCount--;
    if (blockCount == 0)
      invalidate();
  }

  private void init(Context context) {

    imageCountX = 16;
    imageCountY = 8;
    controlSteps = imageCountX * imageCountY;
    potMovie    = ResourceManager.getBitmap(context, R.drawable.knobmovie);
    linearSize  = (int)((float)linearSize * context.getResources().getDisplayMetrics().density);
  }

  private float valueFromMousePos(float mx, float my) {

    // Get coordinates with respect to the control center:
    float x = (getWidth()  / 2.0f) - mx;
    float y = (getHeight() / 2.0f) - my;

    // Normalize the values to get a direction vector:
    float len = (float)Math.sqrt(x * x + y * y);
    if (len > 0.0) {
      x /= len;
      y /= len;

      // Calc value:
      float val = (float)Math.acos(y) * (x < 0.0f ? 1.0f : -1.0f);

      // Move into range [0,1]:
      val += 3.14f;
      val /= 6.28f;

      // Return the value:
      return val;
    }

    // We hit the center, return current value:
    return value;
  }

  private void fireValueChanged() {

    if (blockCount != 0)
      return;

    for (PotListener listener : listeners)
      listener.onValueChanged(value);
  }

  private ArrayList<PotListener> listeners = new ArrayList<PotListener>();
  private boolean circularMode = false;
  private boolean absoluteMode = false;
  private float startVal = 0.0f;
  private float startY = 0.0f;
  private int linearSize = 128;
  private int imageCountX = 0;
  private int imageCountY = 0;
  private int controlSteps = 0;
  private Bitmap potMovie = null;
  private Paint moviePaint = new Paint(0);
  private float value = 0.0f;
  private ViewParent scrollView = null;
  private boolean knobDown = false;
  private int blockCount = 0;
}
