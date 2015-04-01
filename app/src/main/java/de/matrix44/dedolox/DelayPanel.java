package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 26.03.15.
 */
public class DelayPanel extends  SynthPanel {

  public DelayPanel(Context context) {
    super(context, R.drawable.delay);
    init(context);
  }

  public DelayPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.delay);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    onoffSel.setScrollView(target);
    timePot.setScrollView(target);
    feedbackPot.setScrollView(target);
    mixPot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_DELAY_ENABLE) {
      onoffSel.blockUpdates(false);
      onoffSel.setValue(event.value2);
      onoffSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_DELAY_TIME) {
      timePot.blockUpdates(false);
      timePot.setValue(event.value2 / 127.0);
      timePot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_DELAY_FEEDBACK) {
      feedbackPot.blockUpdates(false);
      feedbackPot.setValue(event.value2 / 127.0);
      feedbackPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_DELAY_MIX) {
      mixPot.blockUpdates(false);
      mixPot.setValue(event.value2 / 127.0);
      mixPot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DELAY_ENABLE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DELAY_TIME));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DELAY_FEEDBACK));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_DELAY_MIX));
  }

  private void init(Context context) {

    onoffSel = new MultiSelView(context, 1);
    onoffSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newWave) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DELAY_ENABLE, newWave);
      }
    });
    addView(onoffSel, new FrameLayout.LayoutParams(100, 100));

    timePot = new PotView(context);
    timePot.setControlSteps(25);
    timePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DELAY_TIME, (int) (127.0 * newVal));
      }
    });
    addView(timePot, new FrameLayout.LayoutParams(100, 100));

    feedbackPot = new PotView(context);
    feedbackPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DELAY_FEEDBACK, (int) (127.0 * newVal));
      }
    });
    addView(feedbackPot, new FrameLayout.LayoutParams(100, 100));

    mixPot = new PotView(context);
    mixPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_DELAY_MIX, (int) (127.0 * newVal));
      }
    });
    addView(mixPot, new FrameLayout.LayoutParams(100, 100));
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

    if (timePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) timePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.110351563 * height);
      timePot.setLayoutParams(params);
    }

    if (feedbackPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) feedbackPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      feedbackPot.setLayoutParams(params);
    }

    if (mixPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mixPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.5546875 * height);
      mixPot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView onoffSel = null;
  private PotView timePot = null;
  private PotView feedbackPot = null;
  private PotView mixPot = null;
}
