package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 17.01.15.
 */
public class MixerPanel extends  SynthPanel {

  public MixerPanel(Context context) {
    super(context, R.drawable.greenpanel, R.drawable.mixer);
    init(context);
  }

  public MixerPanel(Context context, AttributeSet attrs) {
    super(context, R.drawable.greenpanel, R.drawable.mixer);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    osc1Pot.setScrollView(target);
    osc2Pot.setScrollView(target);
    noisePot.setScrollView(target);
    ringPot.setScrollView(target);
  }

  private void init(Context context) {

    osc1Pot = new PotView(context);
    osc1Pot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC1_LEVEL, (int)(127.0 * newVal));
      }
    });
    addView(osc1Pot, new FrameLayout.LayoutParams(100, 100));

    osc2Pot = new PotView(context);
    osc2Pot.setPotListener(new PotView.PotListener() {
        @Override
        public void onValueChanged(double newVal) {
          MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC2_LEVEL, (int)(127.0 * newVal));
        }
      });
    addView(osc2Pot, new FrameLayout.LayoutParams(100, 100));

    noisePot = new PotView(context);
    noisePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_NOISE_LEVEL, (int)(127.0 * newVal));
      }
    });
    addView(noisePot, new FrameLayout.LayoutParams(100, 100));

    ringPot = new PotView(context);
    ringPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_RINGMOD_LEVEL, (int)(127.0 * newVal));
      }
    });
    addView(ringPot, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    int width  = right - left;
    int height = bottom - top;

    if (osc1Pot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)osc1Pot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.110351563 * height);
      osc1Pot.setLayoutParams(params);
    }

    if (osc2Pot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)osc2Pot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.110351563 * height);
      osc2Pot.setLayoutParams(params);
    }

    if (ringPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)ringPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      ringPot.setLayoutParams(params);
    }

    if (noisePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)noisePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.5546875 * height);
      noisePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private PotView osc1Pot = null;
  private PotView osc2Pot = null;
  private PotView noisePot = null;
  private PotView ringPot = null;
}
