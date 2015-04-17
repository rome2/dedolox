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
 * @since   2014-12-12
 */

package de.matrix44.dedolox;

/**
 * Helper class to avoid zipper noise while changing parameters in a effect or synth plugin.
 * Internally it uses a simple one pole low pass filter.
 *
 * Usage: Add one SmoothParameter for each parameter into your class. Set the desired value via
 *        setValue() and read the smoothed values in the process function through the interpolator.
 *
 * @code
 *
 * // In constructor:
 * SmoothParameter smoothGain = new SmoothParameter(1.0);
 *
 * [...]
 *
 * // Set value from GUI:
 * smoothGain.setValue(0.5);
 *
 * [...]
 *
 * // Process samples:
 * for (int i = 0; i < sampleCount; i++)
 * {
 *   // No more zipper noise:
 *   double gain = smoothGain.tick();
 *   out1[i] = in1[i] * gain;
 *   out2[i] = in2[i] * gain;
 * }
 */
public class SmoothParameter {

  /**
   * Initialization constructor of this class.
   *
   * It is usually a good idea to provide a realistic initial value to avoid smoothing at the very
   * first tick() calls.
   *
   * @param initialValue Initial value of the parameter to smooth.
   **/
  public SmoothParameter(float initialValue) {

    // Init state:
    sampleRate    = 44100;
    smoothedValue = initialValue;
    rawValue      = initialValue;
    coeff         = 1.0f;
    time          = 0.02f;
    updateCoefficient();
  }

  /**
   * Calc the smoothed parameter value.
   *
   * Only call this once per sample as the smoothing is done on a per sample basis. So if you need
   * the value more than once then store it in a temporary variable.
   *
   * @return Returns the current smoothed parameter.
   */
  public float tick() {

    // Filter value:
    smoothedValue = smoothedValue + coeff * (rawValue - smoothedValue);

    // Return filtered value:
    return smoothedValue;
  }

  /**
   * Get accessor for the SampleRate property.
   *
   * This must be set to the current sample rate of the project or else the parameter smoothing will
   * not work as expected.
   *
   * @return Returns the current sample rate of this object.
   */
  public int getSampleRate() {

    // Return our sample rate:
    return sampleRate;
  }

  /**
   * Set accessor for the SampleRate property.
   *
   * This must be set to the current sample rate of the project or else the parameter smoothing will
   * not work as expected.
   *
   * @param newRate
   */
  public void setSampleRate(int newRate) {

    // Update the internal rate:
    sampleRate = newRate;

    // Update state:
    updateCoefficient();
  }

  /**
   * Get accessor for the Time property.
   *
   * Reasonable values are between 0.01 and 0.03 to avoid zipper noises while parameters change.
   *
   * @return Returns the current smoothing time in seconds.
   */
  public float getTime() {

    // Return current time:
    return time;
  }

  /**
   * Set accessor for the Time property.
   *
   * Reasonable values are between 0.01 and 0.03 to avoid zipper noises while parameters change.
   *
   * @param newTime New smoothing time in seconds.
   */
  public void setTime(float newTime) {

    // Update the internal time:
    time = newTime;

    // Update state:
    updateCoefficient();
  }

  /**
   * Get accessor for the Value property.
   *
   * This is the real value as it is set by the DAW and as it stored to disk. To get the smoothed
   * value use tick().
   *
   * @return Returns the current raw parameter value.
   */
  public float getValue() {

    // Return current value:
    return rawValue;
  }

  /**
   * Set accessor for the Value property.
   *
   * This is the real value as it is set by the DAW and as it stored to disk. The smoothed values
   * from tick() will reach this value eventually.
   *
   * @param newValue New target value of this parameter.
   */
  public void setValue(float newValue) {

    // Set new value:
    rawValue = newValue;
  }

  /**
   * Update the internal filter state. This is called after a sample rate or time change.
   */
  private void updateCoefficient() {

    // Recalculate coefficient:
    coeff = 1.0f - (float)Math.exp(-1.0f / (time * sampleRate));
  }

  /** Current sample rate. */
  private int sampleRate;

  /** Current smoothed value. */
  private float smoothedValue;

  /** Current real value. */
  private float rawValue;

  /** Current filter coefficient. */
  private float coeff;

  /** Smoothing time for the filter. */
  private float time;
}
