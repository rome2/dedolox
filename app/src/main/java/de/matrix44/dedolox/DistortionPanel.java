package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 23.03.15.
 */
public class DistortionPanel extends  SynthPanel {

  public DistortionPanel(Context context) {
    super(context, R.drawable.distortion);
    init(context);
  }

  public DistortionPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.distortion);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    onoffSel.setScrollView(target);
    gainPot.setScrollView(target);
    tonePot.setScrollView(target);
    volumePot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_DISTORTION_ENABLE) {
      onoffSel.blockUpdates(false);
      onoffSel.setValue(event.value2);
      onoffSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_DISTORTION_GAIN) {
      gainPot.blockUpdates(false);
      gainPot.setValue(event.value2 / 127.0);
      gainPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_DISTORTION_TONE) {
      tonePot.blockUpdates(false);
      tonePot.setValue(event.value2 / 127.0);
      tonePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_DISTORTION_VOLUME) {
      volumePot.blockUpdates(false);
      volumePot.setValue(event.value2 / 127.0);
      volumePot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DISTORTION_ENABLE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DISTORTION_GAIN));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DISTORTION_TONE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DISTORTION_VOLUME));
  }

  private void init(Context context) {

    onoffSel = new MultiSelView(context, 1);
    onoffSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newWave) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DISTORTION_ENABLE, newWave);
      }
    });
    addView(onoffSel, new FrameLayout.LayoutParams(100, 100));

    gainPot = new PotView(context);
    gainPot.setControlSteps(25);
    gainPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
      MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DISTORTION_GAIN, (int) (127.0 * newVal));
      }
    });
    addView(gainPot, new FrameLayout.LayoutParams(100, 100));

    tonePot = new PotView(context);
    tonePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DISTORTION_TONE, (int) (127.0 * newVal));
      }
    });
    addView(tonePot, new FrameLayout.LayoutParams(100, 100));

    volumePot = new PotView(context);
    volumePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DISTORTION_VOLUME, (int) (127.0 * newVal));
      }
    });
    addView(volumePot, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    int width  = right - left;
    int height = bottom - top;

    if (onoffSel != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) onoffSel.getLayoutParams();
      params.width      = (int)(0.421875 * width);
      params.height     = (int)(0.421875 * height);
      params.leftMargin = (int)(0.060546875 * width);
      params.topMargin  = (int)(0.069335938 * height);
      onoffSel.setLayoutParams(params);
    }

    if (gainPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) gainPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.110351563 * height);
      gainPot.setLayoutParams(params);
    }

    if (tonePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tonePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      tonePot.setLayoutParams(params);
    }

    if (volumePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) volumePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.5546875 * height);
      volumePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView onoffSel = null;
  private PotView gainPot = null;
  private PotView tonePot = null;
  private PotView volumePot = null;
}
