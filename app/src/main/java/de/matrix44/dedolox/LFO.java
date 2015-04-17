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
 * @since   2014-12-14
 */
package de.matrix44.dedolox;

/**
 * A low frequency oscillator.
 *
 * This LFO  is not as fancy as the main oscillators but it will do the job fine as long as the
 * frequency is kept low (preferably below hearing threshold).
 */
public class LFO {

  /** Possible wave forms of this LFO. */
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
  }

  /**
   * Retrieve the frequency of this oscillator.
   *
   * @return Returns the current frequency in hertz.
   */
  public float getFrequency() {

    // Return current frequency:
    return frequency.getValue();
  }

  /**
   * Set the frequency of this oscillator.
   *
   * @param newFrequency The new frequency of this oscillator.
   */
  public void setFrequency(float newFrequency) {

    // Check frequency:
    if (newFrequency < Tweak.LFO_MIN_SPEED)
      newFrequency = Tweak.LFO_MIN_SPEED;
    else if (newFrequency > Tweak.LFO_MAX_SPEED)
      newFrequency = Tweak.LFO_MAX_SPEED;

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
   * Retrieve the wave form of this oscillator.
   *
   * @param newWaveForm The new wave form of this oscillator (see WaveForm enum).
   */
  public void setWaveForm(WaveForm newWaveForm) {

    // Update waveform:
    waveForm = newWaveForm;
  }

  /**
   * The actual oscillation function.
   *
   * @return The next sample of this oscillator.
   */
  public float tick() {

    float retval = 0.0f;

    if (waveForm == WaveForm.SINE)
      retval = (float)Math.sin(omega);
    else if (waveForm == WaveForm.TRIANGLE)
      retval = (omega < pi ? omega / pi : (tau - omega) / pi) * 2.0f - 1.0f;
    else if (waveForm == WaveForm.SAW)
      retval = (tau - omega) / tau * 2.0f - 1.0f;
    else if (waveForm == WaveForm.RECT)
      retval = omega < pi ? -1.0f : 1.0f;

    omega += tau * frequency.tick() / sampleRate;
    if (omega > tau)
      omega -= tau;

    return retval;
  }

  /** One pi. */
  private static final float pi = (float)Math.PI;

  /** Well, two pies... */
  private static final float tau = (float)(Math.PI * 2.0);

  /** The sample rate of the system (in samples / second). */
  private int sampleRate;

  /** Current oscillator phase. */
  private float omega = 0.0f;

  /** Waveform of this oscillator. */
  private WaveForm waveForm = WaveForm.SAW;

  /** Frequency of this oscillator. */
  private final SmoothParameter frequency = new SmoothParameter(Tweak.LFO_MIN_SPEED);
}
