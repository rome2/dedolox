package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by rollo on 17.01.15.
 */
public class WoodPanel extends SynthPanel {

  public WoodPanel(Context context) {
    super(context);
    init(context, R.drawable.woodpanel);
  }

  public WoodPanel(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, R.drawable.woodpanel);
  }
}
