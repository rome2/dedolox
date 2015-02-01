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

/**
 * Created by rollo on 11.01.15.
 */
public class PotView extends View {

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

      int imageNo = (int)(((imageCountX * imageCountY) - 1) * value);
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

      // Use circular mode?
      if (circularMode)
      {
        // Get value from the mouse position point:
        startVal = valueFromMousePos(event.getX(), event.getY());

        // Make value current if needed:
        if (absoluteMode)
          setValue(startVal);
      }

      // No, linear is the way to go:
      else
      {
        // Save start values:
        startVal = value;
        startY   = event.getY();
      }
    }

    else if (event.getAction() == MotionEvent.ACTION_MOVE) {

      // Running in circles?
      if (circularMode)
      {
        // Get value from the mouse position point:
        double val = valueFromMousePos(event.getX(), event.getY());

        // Set value:
        if (absoluteMode)
          setValue(val);
        else
        {
          // Set new value relative to the last value:
          setValue(value + (val - startVal));

          // Save current value for the next round:
          startVal = val;
        }
      }

      // No, we're imitating a fader:
      else
      {
        // Calc movement in pixels:
        double dy = startY - event.getY();

        // Scale into a more usable range:
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
  }

  public double getValue() {
    return value;
  }

  private void init(Context context) {

    imageCountX = 16;
    imageCountY = 8;
    potMovie    = BitmapFactory.decodeResource(this.getResources(), R.drawable.knob_movie);
    linearSize  = (int)((float)linearSize * context.getResources().getDisplayMetrics().density);
  }

  private double valueFromMousePos(float mx, float my) {

    // Get coordinates with respect to the control center:
    double x = (getWidth()  / 2.0) - mx;
    double y = (getHeight() / 2.0) - my;

    // Normalize the values to get a direction vector:
    double len = Math.sqrt(x * x + y * y);
    if (len > 0.0)
    {
      x /= len;
      y /= len;

      // Calc value:
      double val = Math.acos(y) * (x < 0.0 ? 1.0 : -1.0);

      // Move into range [0,1]:
      val += 3.14;
      val /= 6.28;

      // Return the value:
      return val;
    }

    // We hit the center, return current value:
    return value;
  }

  private boolean circularMode = false;
  private boolean absoluteMode = false;
  private double startVal = 0.0;
  private double startY;
  private int linearSize = 128;
  private int imageCountX = 0;
  private int imageCountY = 0;
  private Bitmap potMovie = null;
  private Paint moviePaint = new Paint(0);
  private double value = 0.0;
}
