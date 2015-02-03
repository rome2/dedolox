/**
 * Copyright © Rolf Meyerhoff 2014
 *
 * These coded instructions,  statements and computer programs  contain unpublished proprietary
 * information of Rolf Meyerhoff,  and are protected by federal copyright law.  They may not be
 * disclosed to third parties or copied or duplicated in any form, in whole or in part, without
 * the prior written consent of Rolf Meyerhoff.
 *
 * @author  Rolf Meyerhoff (rm@matrix44.de)
 * @version 1.0
 * @since   2014-12-15
 */
package de.matrix44.dedolox;

/**
 *
 */
public class MainOscillator {

  /** Possible wave forms of this oscillator. */
  public enum WaveForm { SINE, TRIANGLE, SAW, RECT, RANDOM }

  /**
   * Retrieve the sample rate of this oscillator.
   *
   * @return Returns the current sample rate in samples / second.
   */
  public int getSampleRate() {

    // Return current sample rate:
    return sampleRate;
  }

  /**
   * Set the sample rate to use in this oscillator.
   *
   * You'll have to set this to the rate of your dsp system or the times and values will not be
   * correct.
   *
   * @param newSampleRate The sample rate to use in samples / second.
   */
  public void setSampleRate(int newSampleRate) {

    // Parameter check:
    if (newSampleRate > 0)
      sampleRate = newSampleRate;

    // Update parameters:
    frequency.setSampleRate(newSampleRate);
    pulseWidth.setSampleRate(newSampleRate);
  }

  /**
   * Retrieve the frequency of this oscillator.
   *
   * @return Returns the current frequency in hertz.
   */
  public double getFrequency() {

    // Return current frequency:
    return frequency.getValue();
  }

  /**
   * Set the frequency of this oscillator.
   *
   * @param newFrequency The new frequency of this oscillator.
   */
  public void setFrequency(double newFrequency) {

    // Check frequency:
    if (newFrequency < Tweak.OSC_MIN_SPEED)
      newFrequency = Tweak.OSC_MIN_SPEED;
    else if (newFrequency > Tweak.OSC_MAX_SPEED)
      newFrequency = Tweak.OSC_MAX_SPEED;

    // Update frequency:
    frequency.setValue(newFrequency);
  }

  /**
   * Retrieve the wave form of this oscillator.
   *
   * @return Returns the current wave form of this oscillator (see WaveForm enum).
   */
  public WaveForm getWaveForm() {

    // Return current waveform:
    return waveForm;
  }

  /**
   * Set the wave form of this oscillator.
   *
   * @param newWaveForm The new wave form of this oscillator (see WaveForm enum).
   */
  public void setWaveForm(WaveForm newWaveForm) {

    // Update waveform:
    waveForm = newWaveForm;
  }

  /**
   * Retrieve the pulse width of this oscillator.
   *
   * @return Returns the pulse width in percent.
   */
  public double getPulseWidth() {

    // Return current pulse width:
    return pulseWidth.getValue();
  }

  /**
   * Set the pulse width of this oscillator.
   *
   * @param newWidth The new pulse width of this oscillator.
   */
  public void setPulseWidth(double newWidth) {

    // Clip value:
    if (newWidth < 0.0)
      newWidth = 0.0;
    else if (newWidth > 1.0)
      newWidth = 1.0;

    // Update width:
    pulseWidth.setValue(newWidth);
  }

  /**
   * The actual oscillation function.
   *
   * @return The next sample of this oscillator.
   */
  public double tick() {

    double retval = 0.0;

    if (waveForm == WaveForm.SINE)
      retval = Math.sin(omega);
    else if (waveForm == WaveForm.TRIANGLE)
      retval = (omega < Math.PI ? omega / Math.PI : (tau - omega) / Math.PI) * 2.0 - 1.0;
    else if (waveForm == WaveForm.SAW)
      retval = (tau - omega) / tau * 2.0 - 1.0;
    else if (waveForm == WaveForm.RECT)
      retval = omega < Math.PI ? -1.0 : 1.0;

    omega += tau * frequency.tick() / sampleRate;
    if (omega > tau)
      omega -= tau;

    return retval;
  }

  /** Well, two pies... */
  private static final double tau = Math.PI * 2.0;

  /** The sample rate of the system (in samples / second). */
  private int sampleRate;

  /** Current oscillator phase. */
  private double omega = 0.0;

  /** Waveform of this oscillator. */
  private WaveForm waveForm = WaveForm.SAW;

  /** Frequency of this oscillator. */
  private final SmoothParameter frequency = new SmoothParameter(Tweak.LFO_MIN_SPEED);

  /** Pulse width of this oscillator. */
  private final SmoothParameter pulseWidth = new SmoothParameter(1.0);
}
