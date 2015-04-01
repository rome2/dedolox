package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 23.03.15.
 */
public class Lfo2Panel extends  SynthPanel {

  public Lfo2Panel(Context context) {
    super(context, R.drawable.lfo2);
    init(context);
  }

  public Lfo2Panel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.lfo2);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    waveSel.setScrollView(target);
    ratePot.setScrollView(target);
    filterPot.setScrollView(target);
    pulsePot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_LFO2_WAVEFORM) {
      waveSel.blockUpdates(false);
      waveSel.setValue(event.value2);
      waveSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_LFO2_SPEED) {
      ratePot.blockUpdates(false);
      ratePot.setValue(event.value2 / 127.0);
      ratePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_LFO2_CUTOFF) {
      filterPot.blockUpdates(false);
      filterPot.setValue(event.value2 / 127.0);
      filterPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_LFO2_PULSE) {
      pulsePot.blockUpdates(false);
      pulsePot.setValue(event.value2 / 127.0);
      pulsePot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO2_WAVEFORM));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO2_SPEED));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO2_CUTOFF));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO2_PULSE));
  }

  private void init(Context context) {

    waveSel = new MultiSelView(context, 4);
    waveSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newWave) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO2_WAVEFORM, newWave);
      }
     });
    addView(waveSel, new FrameLayout.LayoutParams(100, 100));

    ratePot = new PotView(context);
    ratePot.setControlSteps(25);
    ratePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO2_SPEED, (int) (127.0 * newVal));
      }
    });
    addView(ratePot, new FrameLayout.LayoutParams(100, 100));

    filterPot = new PotView(context);
    filterPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO2_CUTOFF, (int) (127.0 * newVal));
      }
    });
    addView(filterPot, new FrameLayout.LayoutParams(100, 100));

    pulsePot = new PotView(context);
    pulsePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO2_PULSE, (int) (127.0 * newVal));
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

    if (ratePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ratePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.110351563 * height);
      ratePot.setLayoutParams(params);
    }

    if (filterPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) filterPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      filterPot.setLayoutParams(params);
    }

    if (pulsePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) pulsePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.5546875 * height);
      pulsePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView waveSel = null;
  private PotView ratePot = null;
  private PotView filterPot = null;
  private PotView pulsePot = null;
}
