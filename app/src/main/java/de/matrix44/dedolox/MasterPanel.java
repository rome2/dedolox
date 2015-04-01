package de.matrix44.dedolox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by rollo on 26.03.15.
 */
public class MasterPanel extends SynthPanel {

  public MasterPanel(Context context) {
    super(context, R.drawable.master);
    init(context);
  }

  public MasterPanel(Context context, AttributeSet attrs) {
    super(context, attrs, R.drawable.master);
    init(context);
  }

  public void setScrollView(ViewParent target) {
    portamentoPot.setScrollView(target);
    velocityPot.setScrollView(target);
    masterPot.setScrollView(target);
  }

  public void midiIn(MIDIEvent event) {

    if (event.message != 0xB0 || event.value2 < 0)
      return;

    if (event.value1 == MIDIImplementation.CC_PORTAMENTO_TIME) {
      portamentoPot.blockUpdates(false);
      portamentoPot.setValue(event.value2 / 127.0);
      portamentoPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_VELOCITY_SENS) {
      velocityPot.blockUpdates(false);
      velocityPot.setValue(event.value2 / 127.0);
      velocityPot.blockUpdates(true);
    }

    if (event.value1 == MIDIImplementation.CC_MASTER_VOL) {
      masterPot.blockUpdates(false);
      masterPot.setValue(event.value2 / 127.0);
      masterPot.blockUpdates(true);
    }
  }

  public void setPreset(DedoloxPreset preset) {
    midiIn(preset.getValueEvent(MIDIImplementation.CC_PORTAMENTO_TIME));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_VELOCITY_SENS));
    midiIn(preset.getValueEvent(MIDIImplementation.CC_MASTER_VOL));
  }

  private void init(Context context) {

    portamentoPot = new PotView(context);
    portamentoPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_PORTAMENTO_TIME, (int) (127.0 * newVal));
      }
    });
    addView(portamentoPot, new FrameLayout.LayoutParams(100, 100));

    velocityPot = new PotView(context);
    velocityPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_VELOCITY_SENS, (int) (127.0 * newVal));
      }
    });
    addView(velocityPot, new FrameLayout.LayoutParams(100, 100));

    masterPot = new PotView(context);
    masterPot.setPotListener(new PotView.PotListener() {
      @Override
      public void onValueChanged(double newVal) {
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_MASTER_VOL, (int) (127.0 * newVal));
      }
    });
    addView(masterPot, new FrameLayout.LayoutParams(100, 100));
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    int width  = right - left;
    int height = bottom - top;

    if (portamentoPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) portamentoPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.110351563 * height);
      portamentoPot.setLayoutParams(params);
    }

    if (masterPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) masterPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.5546875 * width);
      params.topMargin  = (int)(0.5546875 * height);
      masterPot.setLayoutParams(params);
    }

    if (velocityPot != null) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) velocityPot.getLayoutParams();
      params.width      = (int)(0.336914063 * width);
      params.height     = (int)(0.336914063 * height);
      params.leftMargin = (int)(0.109375 * width);
      params.topMargin  = (int)(0.5546875 * height);
      velocityPot.setLayoutParams(params);
    }

    super.onLayout(changed, left, top, right, bottom);
  }

  private PotView portamentoPot = null;
  private PotView velocityPot = null;
  private PotView masterPot = null;
}
