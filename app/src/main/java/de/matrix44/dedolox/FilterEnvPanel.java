package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 22.03.15.
 */
public class FilterEnvPanel extends  SynthPanel {

  public FilterEnvPanel(Context context) {
    super(context, R.drawable.filterenvelope);
    init(context);
  }

  public FilterEnvPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.filterenvelope);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    attackFader.setScrollView(target);
    decayFader.setScrollView(target);
    sustainFader.setScrollView(target);
    releaseFader.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_FILTERENV_ATTACK) {
      attackFader.blockUpdates(false);
      attackFader.setValue(event.value2 / 127.0f);
      attackFader.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_FILTERENV_DECAY) {
      decayFader.blockUpdates(false);
      decayFader.setValue(event.value2 / 127.0f);
      decayFader.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_FILTERENV_SUSTAIN) {
      sustainFader.blockUpdates(false);
      sustainFader.setValue(event.value2 / 127.0f);
      sustainFader.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_FILTERENV_RELEASE) {
      releaseFader.blockUpdates(false);
      releaseFader.setValue(event.value2 / 127.0f);
      releaseFader.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTERENV_ATTACK));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTERENV_DECAY));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTERENV_SUSTAIN));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_FILTERENV_RELEASE));
  }

  private void init(Context context) {

    attackFader = new FaderView(context);
    attackFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTERENV_ATTACK, (int)(127.0f * newVal));
      }
    });
    addView(attackFader, new FrameLayout.LayoutParams(100, 100));

    decayFader = new FaderView(context);
    decayFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTERENV_DECAY, (int)(127.0f * newVal));
      }
    });
    addView(decayFader, new FrameLayout.LayoutParams(100, 100));

    sustainFader = new FaderView(context);
    sustainFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTERENV_SUSTAIN, (int)(127.0f * newVal));
      }
    });
    addView(sustainFader, new FrameLayout.LayoutParams(100, 100));

    releaseFader = new FaderView(context);
    releaseFader.setFaderListener(new FaderView.FaderListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTERENV_RELEASE, (int)(127.0f * newVal));
      }
    });
    addView(releaseFader, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    int width  = right - left;
    int height = bottom - top;

    if (attackFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)attackFader.getLayoutParams();
      params.width      = (int)(0.208984375f * width);
      params.height     = (int)(0.734375f * height);
      params.leftMargin = (int)(0.078125f * width);
      params.topMargin  = (int)(0.134765625f * height);
      attackFader.setLayoutParams(params);
    }

    if (decayFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)decayFader.getLayoutParams();
      params.width      = (int)(0.208984375f * width);
      params.height     = (int)(0.734375f * height);
      params.leftMargin = (int)(0.287109375f * width);
      params.topMargin  = (int)(0.134765625f * height);
      decayFader.setLayoutParams(params);
    }

    if (sustainFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)sustainFader.getLayoutParams();
      params.width      = (int)(0.208984375f * width);
      params.height     = (int)(0.734375f * height);
      params.leftMargin = (int)(0.49609375f * width);
      params.topMargin  = (int)(0.134765625f * height);
      sustainFader.setLayoutParams(params);
    }

    if (releaseFader != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)releaseFader.getLayoutParams();
      params.width      = (int)(0.208984375f * width);
      params.height     = (int)(0.734375f * height);
      params.leftMargin = (int)(0.705078125f * width);
      params.topMargin  = (int)(0.134765625f * height);
      releaseFader.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private FaderView attackFader = null;
  private FaderView decayFader = null;
  private FaderView sustainFader = null;
  private FaderView releaseFader = null;
}
