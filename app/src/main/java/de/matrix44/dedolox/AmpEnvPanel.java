package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 20.03.15.
 */
public class AmpEnvPanel extends  SynthPanel {

  public AmpEnvPanel(Context context) {
    super(context, R.drawable.greenpanel, R.drawable.ampenvelope);
    init(context);
  }

  public AmpEnvPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.greenpanel, R.drawable.ampenvelope);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    attackFader.setScrollView(target);
    decayFader.setScrollView(target);
    sustainFader.setScrollView(target);
    releaseFader.setScrollView(target);
  }

  private void init(Context context) {

    attackFader = new FaderView(context);
    attackFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_ATTACK, (int)(127.0 * newVal));
      }
    });
    addView(attackFader, new FrameLayout.LayoutParams(100, 100));

    decayFader = new FaderView(context);
    decayFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_DECAY, (int)(127.0 * newVal));
      }
    });
    addView(decayFader, new FrameLayout.LayoutParams(100, 100));

    sustainFader = new FaderView(context);
    sustainFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_SUSTAIN, (int)(127.0 * newVal));
      }
    });
    addView(sustainFader, new FrameLayout.LayoutParams(100, 100));

    releaseFader = new FaderView(context);
    releaseFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_RELEASE, (int)(127.0 * newVal));
      }
    });
    addView(releaseFader, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    int width  = right - left;
    int height = bottom - top;

    if (attackFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)attackFader.getLayoutParams();
      params.width      = (int)(0.208984375 * width);
      params.height     = (int)(0.734375 * height);
      params.leftMargin = (int)(0.078125 * width);
      params.topMargin  = (int)(0.134765625 * height);
      attackFader.setLayoutParams(params);
    }

    if (decayFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)decayFader.getLayoutParams();
      params.width      = (int)(0.208984375 * width);
      params.height     = (int)(0.734375 * height);
      params.leftMargin = (int)(0.287109375 * width);
      params.topMargin  = (int)(0.134765625 * height);
      decayFader.setLayoutParams(params);
    }

    if (sustainFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)sustainFader.getLayoutParams();
      params.width      = (int)(0.208984375 * width);
      params.height     = (int)(0.734375 * height);
      params.leftMargin = (int)(0.49609375 * width);
      params.topMargin  = (int)(0.134765625 * height);
      sustainFader.setLayoutParams(params);
    }

    if (releaseFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)releaseFader.getLayoutParams();
      params.width      = (int)(0.208984375 * width);
      params.height     = (int)(0.734375 * height);
      params.leftMargin = (int)(0.705078125 * width);
      params.topMargin  = (int)(0.134765625 * height);
      releaseFader.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private FaderView attackFader = null;
  private FaderView decayFader = null;
  private FaderView sustainFader = null;
  private FaderView releaseFader = null;
}
