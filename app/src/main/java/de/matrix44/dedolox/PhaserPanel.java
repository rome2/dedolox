package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 24.03.15.
 */
public class PhaserPanel extends  SynthPanel {

  public PhaserPanel(Context context) {
    super(context, R.drawable.phaser);
    init(context);
  }

  public PhaserPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.phaser);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    onoffSel.setScrollView(target);
    ratePot.setScrollView(target);
    depthPot.setScrollView(target);
    feedbackPot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_PHASER_ENABLE) {
      onoffSel.blockUpdates(false);
      onoffSel.setValue(event.value2);
      onoffSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_PHASER_RATE) {
      ratePot.blockUpdates(false);
      ratePot.setValue(event.value2 / 127.0);
      ratePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_PHASER_DEPTH) {
      depthPot.blockUpdates(false);
      depthPot.setValue(event.value2 / 127.0);
      depthPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_PHASER_FEEDBACK) {
      feedbackPot.blockUpdates(false);
      feedbackPot.setValue(event.value2 / 127.0);
      feedbackPot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_PHASER_ENABLE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_PHASER_RATE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_PHASER_DEPTH));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_PHASER_FEEDBACK));
  }

  private void init(Context context) {

    onoffSel = new MultiSelView(context, 1);
    onoffSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newWave) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_PHASER_ENABLE, newWave);
      }
    });
    addView(onoffSel, new FrameLayout.LayoutParams(100, 100));

    ratePot = new PotView(context);
    ratePot.setControlSteps(25);
    ratePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_PHASER_RATE, (int) (127.0 * newVal));
      }
    });
    addView(ratePot, new FrameLayout.LayoutParams(100, 100));

    depthPot = new PotView(context);
    depthPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_PHASER_DEPTH, (int) (127.0 * newVal));
      }
    });
    addView(depthPot, new FrameLayout.LayoutParams(100, 100));

    feedbackPot = new PotView(context);
    feedbackPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_PHASER_FEEDBACK, (int) (127.0 * newVal));
      }
    });
    addView(feedbackPot, new FrameLayout.LayoutParams(100, 100));
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

    if (ratePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ratePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.110351563 * height);
      ratePot.setLayoutParams(params);
    }

    if (depthPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) depthPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      depthPot.setLayoutParams(params);
    }

    if (feedbackPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) feedbackPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.5546875 * height);
      feedbackPot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView onoffSel = null;
  private PotView ratePot = null;
  private PotView depthPot = null;
  private PotView feedbackPot = null;
}
