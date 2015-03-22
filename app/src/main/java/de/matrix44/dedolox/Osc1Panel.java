package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 17.01.15.
 */
public class Osc1Panel extends  SynthPanel {

  public Osc1Panel(Context context) {
    super(context, R.drawable.greenpanel, R.drawable.osc1);
    init(context);
  }

  public Osc1Panel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.greenpanel, R.drawable.osc1);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    waveSel.setScrollView(target);
    coarsePot.setScrollView(target);
    finePot.setScrollView(target);
    pulsePot.setScrollView(target);
  }

  private void init(Context context) {

    waveSel = new MultiSelView(context, 4);
    waveSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
        @Override
        public void onValueSelectionChanged(int newWave) {
          MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC1_WAVEFORM, newWave);
        }
      }
    );
    addView(waveSel, new FrameLayout.LayoutParams(100, 100));

    coarsePot = new PotView(context);
    coarsePot.setControlSteps(25);
    coarsePot.setPotListener(new PotView.PotListener() {
        @Override
        public void onValueChanged(double newVal) {
          newVal = (newVal * 2.0) - 1.0;
          int iVal = 64 + (int)Math.floor(newVal * 12.0);
          MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC1_COARSE, iVal);
        }
      });
    addView(coarsePot, new FrameLayout.LayoutParams(100, 100));

    finePot = new PotView(context);
    finePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC1_FINE, (int)(127.0 * newVal));
      }
    });
    addView(finePot, new FrameLayout.LayoutParams(100, 100));

    pulsePot = new PotView(context);
    pulsePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC1_PULSE, (int)(127.0 * newVal));
      }
    });
    addView(pulsePot, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    int width  = right - left;
    int height = bottom - top;

    if (waveSel != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)waveSel.getLayoutParams();
      params.width      = (int)(0.421875 * width);
      params.height     = (int)(0.421875 * height);
      params.leftMargin = (int)(0.060546875 * width);
      params.topMargin  = (int)(0.069335938 * height);
      waveSel.setLayoutParams(params);
    }

    if (coarsePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)coarsePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.110351563 * height);
      coarsePot.setLayoutParams(params);
    }

    if (finePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)finePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      finePot.setLayoutParams(params);
    }

    if (pulsePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)pulsePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.5546875 * height);
      pulsePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView waveSel = null;
  private PotView coarsePot = null;
  private PotView finePot = null;
  private PotView pulsePot = null;
}
