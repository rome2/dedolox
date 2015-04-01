package de.matrix44.dedolox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 11.01.15.
 */
public class SynthPanel extends FrameLayout {

  public SynthPanel(Context context) {
    super(context);
    init(context, 0);
  }

  public SynthPanel(Context context, int backpicID) {
    super(context);
    init(context, backpicID);
  }

  public SynthPanel(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, 0);
  }

  public SynthPanel(Context context, AttributeSet attrs, int backpicID) {
    super(context, attrs);
    init(context, backpicID);
  }

  public void setScrollView(ViewParent target) {
  }

  public void midiIn(MIDIEvent event) {
  }

  public void setPreset(DedoloxPreset preset) {
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = MeasureSpec.getSize(heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);

    if (backPic != null) {
      double aspect = (double)backPic.getWidth() / backPic.getHeight();
      width = (int)(aspect * height);
      widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

  protected void init(Context context, int backpicID) {
    this.setWillNotDraw(false);
    if (backpicID != 0)
      backPic = ResourceManager.getBitmap565(context, backpicID);
  }

  private Bitmap backPic = null;
  private Paint backPicPaint = new Paint(0);
}
