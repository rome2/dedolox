package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 17.01.15.
 */
public class Osc2Panel extends  SynthPanel {

  public Osc2Panel(Context context) {
    super(context, R.drawable.osc2);
    init(context);
  }

  public Osc2Panel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.osc2);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    waveSel.setScrollView(target);
    coarsePot.setScrollView(target);
    finePot.setScrollView(target);
    pulsePot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_OSC2_WAVEFORM) {
      waveSel.blockUpdates(false);
      waveSel.setValue(event.value2);
      waveSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_OSC2_COARSE) {
      coarsePot.blockUpdates(false);
      coarsePot.setValue((((event.value2 - 64) / 12.0f) + 1.0f) * 0.5f);
      coarsePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_OSC2_FINE) {
      finePot.blockUpdates(false);
      finePot.setValue(event.value2 / 127.0f);
      finePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_OSC2_PULSE) {
      pulsePot.blockUpdates(false);
      pulsePot.setValue(event.value2 / 127.0f);
      pulsePot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_OSC2_WAVEFORM));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_OSC2_COARSE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_OSC2_FINE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_OSC2_PULSE));
  }

  private void init(Context context) {

    waveSel = new MultiSelView(context, 4);
    waveSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newWave) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC2_WAVEFORM, newWave);
      }
    });
    addView(waveSel, new FrameLayout.LayoutParams(100, 100));

    coarsePot = new PotView(context);
    coarsePot.setControlSteps(25);
    coarsePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        newVal = (newVal * 2.0f) - 1.0f;
        int iVal = 64 + (int)Math.floor(newVal * 12.0f);
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC2_COARSE, iVal);
      }
    });
    addView(coarsePot, new FrameLayout.LayoutParams(100, 100));

    finePot = new PotView(context);
    finePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC2_FINE, (int)(127.0f * newVal));
      }
    });
    addView(finePot, new FrameLayout.LayoutParams(100, 100));

    pulsePot = new PotView(context);
    pulsePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC2_PULSE, (int)(127.0f * newVal));
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
      params.width      = (int)(0.421875f * width);
      params.height     = (int)(0.421875f * height);
      params.leftMargin = (int)(0.060546875f * width);
      params.topMargin  = (int)(0.069335938f * height);
      waveSel.setLayoutParams(params);
    }

    if (coarsePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)coarsePot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.5546875f * width);
      params.topMargin  = (int)(0.110351563f * height);
      coarsePot.setLayoutParams(params);
    }

    if (finePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)finePot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.5546875f * width);
      params.topMargin  = (int)(0.5546875f * height);
      finePot.setLayoutParams(params);
    }

    if (pulsePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)pulsePot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.109375f * width);
      params.topMargin  = (int)(0.5546875f * height);
      pulsePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView waveSel = null;
  private PotView coarsePot = null;
  private PotView finePot = null;
  private PotView pulsePot = null;
}
