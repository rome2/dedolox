package de.matrix44.dedolox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by rollo on 11.01.15.
 */
public class SynthPanel extends FrameLayout {

  public SynthPanel(Context context) {
    super(context);
    init(context);
  }

  public SynthPanel(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width  = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);

    if (backPic != null) {
      double aspect = (double)backPic.getWidth() / backPic.getHeight();
      width = (int)(aspect * height);
    }

    setMeasuredDimension(width, height);
  }

  @Override
  public void onDraw(Canvas canvas) {
    if (backPic != null) {

      Rect srcRect = new Rect();
      srcRect.top = 0;
      srcRect.bottom = backPic.getHeight();
      srcRect.left = 0;
      srcRect.right = backPic.getWidth();

      Rect clientRect = new Rect();
      getDrawingRect(clientRect);

      canvas.drawBitmap(backPic, srcRect, clientRect, backPicPaint);
    }
    else
      super.draw(canvas);
  }

  private void init(Context context) {
    this.setWillNotDraw(false);
    backPic = BitmapFactory.decodeResource(this.getResources(), R.drawable.osc1panel);
  }

  private Bitmap backPic = null;
  private Paint backPicPaint = new Paint(0);
}
