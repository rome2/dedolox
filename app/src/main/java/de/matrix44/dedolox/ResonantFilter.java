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
 * This is a moog style resonant 4 pole filter, optimized for speed.
 */
public class ResonantFilter {

  public enum Mode { LOWPASS, HIGHPASS, BANDPASS };

  public enum Slope { DB12, DB24 };

  /**
   * Default constructor of this class.
   *
   * Initializes temp variables.
   */
  public ResonantFilter() {
    buffer[0] = 0.0;
    buffer[1] = 0.0;
    buffer[2] = 0.0;
    buffer[3] = 0.0;
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
    cutoff.setSampleRate(newSampleRate);
    resonance.setSampleRate(newSampleRate);
  }

  /**
   * Retrieve the mode of the filter.
   *
   * @return The current filter mode.
   */
  public Mode getMode() {

    // Return current mode:
    return mode;
  }

  /**
   * Set the mode of the filter.
   *
   * @param newMode The new mode of the filter.
   */
  public void setMode(Mode newMode) {

    // Anything to do?
    if (mode == newMode)
      return;

    // Update mode:
    mode = newMode;

    // Clear filter:
    buffer[0] = 0.0;
    buffer[1] = 0.0;
    buffer[2] = 0.0;
    buffer[3] = 0.0;
  }

  /**
   * Retrieve the sploe of the filter.
   *
   * @return The current filter slope.
   */
  public Slope getSlope() {

    // Return current slope:
    return slope;
  }

  /**
   * Set the slope of the filter.
   *
   * @param newSlope The new slope of the filter.
   */
  public void setSlope(Slope newSlope) {

    // Anything to do?
    if (slope == newSlope)
      return;

    // Update slope:
    slope = newSlope;

    // Clear filter:
    buffer[0] = 0.0;
    buffer[1] = 0.0;
    buffer[2] = 0.0;
    buffer[3] = 0.0;
  }

  /**
   * Retrieve the cutoff of this filter.
   *
   * @return Returns the current cutoff.
   */
  public double getCutoff() {

    // Return current cutoff:
    return cutoff.getValue();
  }

  /**
   * Set the cutoff of this filter.
   *
   * @param newFrequency The new cutoff of this filter.
   */
  public void setCutoff(double newFrequency) {

    // Check cutoff:
    if (newFrequency < Tweak.FILTER_MIN_CUTOFF)
      newFrequency = Tweak.FILTER_MIN_CUTOFF;
    else if (newFrequency > Tweak.FILTER_MAX_CUTOFF)
      newFrequency = Tweak.FILTER_MAX_CUTOFF;

    // Update cutoff:
    cutoff.setValue(newFrequency);
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

    double f = cutoff.tick();
    double fb = resonance.tick() * (1.0 - 0.15 * f * f) * 4.0;

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
    if (mode == Mode.LOWPASS)
      return y4;
    else if (mode == Mode.HIGHPASS)
      return input - y3;
    else
      return x - y4;

    // Write out:
    //return y4;
/*
    double co = cutoff.tick();
    double res = resonance.tick();
    double fb = res + res / (1.0 - co);

    buffer[0] += co * (input - buffer[0] + fb * (buffer[0] - buffer[1]));
    buffer[1] += co * (buffer[0] - buffer[1]);

    double retVal;
    if (slope == Slope.DB12) {

      // Write out:
      if (mode == Mode.LOWPASS)
        retVal = buffer[1];
      else if (mode == Mode.HIGHPASS)
        retVal = input - buffer[1];
      else
        retVal = buffer[0] - buffer[1];

    } else {

      // Two more filter stages:
      buffer[2] += co * (buffer[1] - buffer[2]);
      buffer[3] += co * (buffer[2] - buffer[3]);

      // Write out:
      if (mode == Mode.LOWPASS)
        retVal = buffer[3];
      else if (mode == Mode.HIGHPASS)
        retVal = input - buffer[3];
      else
        retVal = buffer[0] - buffer[3];
    }

    // Clip output (band limited sigmoid):
    return retVal - (retVal * retVal * retVal) / 6.0;
    */
  }

  /** Filter mode. */
  private Mode mode = Mode.LOWPASS;

  /** Filter slope. */
  private Slope slope = Slope.DB24;

  /** The sample rate of the system (in samples / second). */
  private int sampleRate;

  /** Frequency of this filter. */
  private final SmoothParameter cutoff = new SmoothParameter(Tweak.FILTER_MAX_CUTOFF);

  /** Resonance of this filter. */
  private final SmoothParameter resonance = new SmoothParameter(0.0);

  /** Temporary buffer for filtered values. */
  private final double[] buffer = new double[5];
}
