package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 23.03.15.
 */
public class Lfo1Panel extends  SynthPanel {

  public Lfo1Panel(Context context) {
    super(context, R.drawable.lfo1);
    init(context);
  }

  public Lfo1Panel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.lfo1);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    waveSel.setScrollView(target);
    ratePot.setScrollView(target);
    pitchPot.setScrollView(target);
    volumePot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_LFO1_WAVEFORM) {
      waveSel.blockUpdates(false);
      waveSel.setValue(event.value2);
      waveSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_LFO1_SPEED) {
      ratePot.blockUpdates(false);
      ratePot.setValue(event.value2 / 127.0f);
      ratePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_LFO1_PITCH) {
      pitchPot.blockUpdates(false);
      pitchPot.setValue(event.value2 / 127.0f);
      pitchPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_LFO1_VOLUME) {
      volumePot.blockUpdates(false);
      volumePot.setValue(event.value2 / 127.0f);
      volumePot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO1_WAVEFORM));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO1_SPEED));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO1_PITCH));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_LFO1_VOLUME));
  }

  private void init(Context context) {

    waveSel = new MultiSelView(context, 4);
    waveSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newWave) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO1_WAVEFORM, newWave);
      }
    });
    addView(waveSel, new FrameLayout.LayoutParams(100, 100));

    ratePot = new PotView(context);
    ratePot.setControlSteps(25);
    ratePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO1_SPEED, (int) (127.0f * newVal));
      }
    });
    addView(ratePot, new FrameLayout.LayoutParams(100, 100));

    pitchPot = new PotView(context);
    pitchPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO1_PITCH, (int) (127.0f * newVal));
      }
    });
    addView(pitchPot, new FrameLayout.LayoutParams(100, 100));

    volumePot = new PotView(context);
    volumePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO1_VOLUME, (int) (127.0f * newVal));
      }
    });
    addView(volumePot, new FrameLayout.LayoutParams(100, 100));
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

    if (ratePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ratePot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.5546875f * width);
      params.topMargin  = (int)(0.110351563f * height);
      ratePot.setLayoutParams(params);
    }

    if (pitchPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) pitchPot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.5546875f * width);
      params.topMargin  = (int)(0.5546875f * height);
      pitchPot.setLayoutParams(params);
    }

    if (volumePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) volumePot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.109375f * width);
      params.topMargin  = (int)(0.5546875f * height);
      volumePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView waveSel = null;
  private PotView ratePot = null;
  private PotView pitchPot = null;
  private PotView volumePot = null;
}
