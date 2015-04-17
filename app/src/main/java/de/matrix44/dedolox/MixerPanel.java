package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 17.01.15.
 */
public class MixerPanel extends SynthPanel {

  public MixerPanel(Context context) {
    super(context, R.drawable.mixer);
    init(context);
  }

  public MixerPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.mixer);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    osc1Pot.setScrollView(target);
    osc2Pot.setScrollView(target);
    noisePot.setScrollView(target);
    drivePot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_OSC1_LEVEL) {
      osc1Pot.blockUpdates(false);
      osc1Pot.setValue(event.value2 / 127.0f);
      osc1Pot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_OSC2_LEVEL) {
      osc2Pot.blockUpdates(false);
      osc2Pot.setValue(event.value2 / 127.0f);
      osc2Pot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_NOISE_LEVEL) {
      noisePot.blockUpdates(false);
      noisePot.setValue(event.value2 / 127.0f);
      noisePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_MIXER_DRIVE) {
      drivePot.blockUpdates(false);
      drivePot.setValue(event.value2 / 127.0f);
      drivePot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_OSC1_LEVEL));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_OSC2_LEVEL));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_NOISE_LEVEL));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_MIXER_DRIVE));
  }

  private void init(Context context) {

    osc1Pot = new PotView(context);
    osc1Pot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC1_LEVEL, (int)(127.0f * newVal));
      }
    });
    addView(osc1Pot, new FrameLayout.LayoutParams(100, 100));

    osc2Pot = new PotView(context);
    osc2Pot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_OSC2_LEVEL, (int)(127.0f * newVal));
      }
    });
    addView(osc2Pot, new FrameLayout.LayoutParams(100, 100));

    noisePot = new PotView(context);
    noisePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_NOISE_LEVEL, (int)(127.0f * newVal));
      }
    });
    addView(noisePot, new FrameLayout.LayoutParams(100, 100));

    drivePot = new PotView(context);
    drivePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(float newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_MIXER_DRIVE, (int) (127.0f * newVal));
      }
    });
    addView(drivePot, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    int width  = right - left;
    int height = bottom - top;

    if (osc1Pot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)osc1Pot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.109375f * width);
      params.topMargin  = (int)(0.110351563f * height);
      osc1Pot.setLayoutParams(params);
    }

    if (osc2Pot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)osc2Pot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.5546875f * width);
      params.topMargin  = (int)(0.110351563f * height);
      osc2Pot.setLayoutParams(params);
    }

    if (drivePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) drivePot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.5546875f * width);
      params.topMargin  = (int)(0.5546875f * height);
      drivePot.setLayoutParams(params);
    }

    if (noisePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)noisePot.getLayoutParams();
      params.width      = (int)(0.336914063f * width);
      params.height     = (int)(0.336914063f * height);
      params.leftMargin = (int)(0.109375f * width);
      params.topMargin  = (int)(0.5546875f * height);
      noisePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private PotView osc1Pot = null;
  private PotView osc2Pot = null;
  private PotView noisePot = null;
  private PotView drivePot = null;
}
