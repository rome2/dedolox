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
 * This is a Moog style 4 pole filter, optimized for speed.
 */
public class MoogFilter {

  /**
   * Default constructor of this class.
   *
   * Initializes temp variables.
   */
  public MoogFilter() {
    buffer[0] = 0.0;
    buffer[1] = 0.0;
    buffer[2] = 0.0;
    buffer[3] = 0.0;
    buffer[4] = 0.0;
  }

  /**
   * Retrieve the sample rate of this filter.
   *
   * @return Returns the current sample rate in samples / second.
   */
  public int getSampleRate() {

    // Return current sample rate:
    return sampleRate;
  }

  /**
   * Set the sample rate to use in this filter.
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
    resonance.setSampleRate(newSampleRate);
  }

  /**
   * Retrieve the frequency of this filter.
   *
   * @return Returns the current frequency in hertz.
   */
  public double getFrequency() {

    // Return current frequency:
    return frequency.getValue();
  }

  /**
   * Set the frequency of this filter.
   *
   * @param newFrequency The new frequency of this filter.
   */
  public void setFrequency(double newFrequency) {

    // Check frequency:
    if (newFrequency < Tweak.FILTER_MIN_FREQ)
      newFrequency = Tweak.FILTER_MIN_FREQ;
    else if (newFrequency > Tweak.FILTER_MAX_FREQ)
      newFrequency = Tweak.FILTER_MAX_FREQ;

    // Update frequency:
    frequency.setValue(newFrequency);
  }

  /**
   * Retrieve the resonance of this oscillator.
   *
   * @return Returns the current resonance.
   */
  public double getResonance() {

    // Return current resonance:
    return resonance.getValue();
  }

  /**
   * Set the resonance of this filter.
   *
   * @param newResonance The new resonance of this filter.
   */
  public void setResonance(double newResonance) {

    // Check resonance:
    if (newResonance < 0.0)
      newResonance = 0.0;
    else if (newResonance > Tweak.FILTER_MAX_RESONANCE)
      newResonance = Tweak.FILTER_MAX_RESONANCE;

    // Update resonance:
    resonance.setValue(newResonance);
  }

  /**
   * This is the main filter function.
   *
   * @param input The input value.
   * @return Returns the filtered value.
   */
  public double tick(double input) {

    double f = frequency.tick() * 1.16 / sampleRate;
    double fb = resonance.tick() * (1.0 - 0.15 * f * f);

    double x = input - buffer[4] * fb;
    x *= 0.35013 * (f * f) * (f * f);

    // Four cascaded one pole filters:
    double y1 =  x + 0.3 * buffer[0] + (1.0 - f) * buffer[1];
    double y2 = y1 + 0.3 * buffer[1] + (1.0 - f) * buffer[2];
    double y3 = y2 + 0.3 * buffer[2] + (1.0 - f) * buffer[3];
    double y4 = y3 + 0.3 * buffer[3] + (1.0 - f) * buffer[4];

    // Clipper (band limited sigmoid):
    y4 = y4 - (y4 * y4 * y4) / 6.0;

    // Store temporary values:
    buffer[0] =  x;
    buffer[1] = y1;
    buffer[2] = y2;
    buffer[3] = y3;
    buffer[4] = y4;

    // Write out:
    return y4;
  }

  /** The sample rate of the system (in samples / second). */
  private int sampleRate;

  /** Frequency of this filter. */
  private final SmoothParameter frequency = new SmoothParameter(Tweak.FILTER_MAX_FREQ);

  /** Resonance of this filter. */
  private final SmoothParameter resonance = new SmoothParameter(0.0);

  /** Temporary buffer for filtered values. */
  private final double[] buffer = new double[5];
}
