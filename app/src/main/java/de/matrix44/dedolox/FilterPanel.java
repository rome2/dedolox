package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 21.03.15.
 */
public class FilterPanel extends  SynthPanel {

  public FilterPanel(Context context) {
    super(context, R.drawable.filter);
    init(context);
  }

  public FilterPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.filter);
    init(context);
  }

  public void setScrollView(ViewParent target) {

    modeSel.setScrollView(target);
    cutoffPot.setScrollView(target);
    slopeSel.setScrollView(target);
    resonancePot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_FILTER_MODE) {
      modeSel.blockUpdates(false);
      modeSel.setValue(event.value2);
      modeSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_FILTER_CUTOFF) {
      cutoffPot.blockUpdates(false);
      cutoffPot.setValue(event.value2 / 127.0);
      cutoffPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_FILTER_SLOPE) {
      slopeSel.blockUpdates(false);
      slopeSel.setValue(event.value2);
      slopeSel.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_FILTER_RESONANCE) {
      resonancePot.blockUpdates(false);
      resonancePot.setValue(event.value2 / 127.0);
      resonancePot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTER_MODE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTER_CUTOFF));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTER_SLOPE));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTER_RESONANCE));
  }

  private void init(Context context) {

    modeSel = new MultiSelView(context, 3);
    modeSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newMode) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTER_MODE, newMode);
      }
    });
    addView(modeSel, new FrameLayout.LayoutParams(100, 100));

    cutoffPot = new PotView(context);
    cutoffPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTER_CUTOFF, (int)(127.0 * newVal));
      }
    });
    addView(cutoffPot, new FrameLayout.LayoutParams(100, 100));

    slopeSel = new MultiSelView(context, 2);
    modeSel.setWaveSelectionListener(new MultiSelView.ValueSelectionListener() {
      @Override
      public void onValueSelectionChanged(int newSlope) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTER_SLOPE, newSlope);
      }
    });
    addView(slopeSel, new FrameLayout.LayoutParams(100, 100));

    resonancePot = new PotView(context);
    resonancePot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTER_RESONANCE, (int)(127.0 * newVal));
      }
    });
    addView(resonancePot, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    int width  = right - left;
    int height = bottom - top;

    if (modeSel != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)modeSel.getLayoutParams();
      params.width      = (int)(0.421875 * width);
      params.height     = (int)(0.421875 * height);
      params.leftMargin = (int)(0.060546875 * width);
      params.topMargin  = (int)(0.069335938 * height);
      modeSel.setLayoutParams(params);
    }

    if (cutoffPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)cutoffPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.110351563 * height);
      cutoffPot.setLayoutParams(params);
    }

    if (slopeSel != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)slopeSel.getLayoutParams();
      params.width      = (int)(0.421875 * width);
      params.height     = (int)(0.421875 * height);
      params.leftMargin = (int)(0.060546875 * width);
      params.topMargin  = (int)(0.5096875 * height);
      slopeSel.setLayoutParams(params);
    }

    if (resonancePot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)resonancePot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      resonancePot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private MultiSelView modeSel = null;
  private PotView cutoffPot = null;
  private MultiSelView slopeSel = null;
  private PotView resonancePot = null;
}
