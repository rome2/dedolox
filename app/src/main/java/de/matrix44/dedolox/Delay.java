/**
 * Copyright © Rolf Meyerhoff 2015
 *
 * These coded instructions,  statements and computer programs  contain unpublished proprietary
 * information of Rolf Meyerhoff,  and are protected by federal copyright law.  They may not be
 * disclosed to third parties or copied or duplicated in any form, in whole or in part, without
 * the prior written consent of Rolf Meyerhoff.
 *
 * @author  Rolf Meyerhoff (rm@matrix44.de)
 * @version 1.0
 * @since   2014-05-15
 */
package de.matrix44.dedolox;

/**
 * A simple delay effect with feedback and mix.
 *
 * This delay allocates a fixed amount of samples when the sample rate is set. So in practice the
 * delay buffer created only once at the start of the project.
 */
public class Delay {

  /**
   * Default constructor of this class.
   */
  public Delay() {

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
   * @param newRate New sample rate of the project.
   */
  public void setSampleRate(int newRate) {

    // Update the internal rate:
    sampleRate = newRate;

    // Update delay buffer (plus one extra sample to catch any rounding errors):
    buffer = new float[(int)(Tweak.DELAY_MAX_TIME * sampleRate) + 1];
    clearBuffer();

    // Update delay time:
    setTime(delayTime);
  }

  /**
   * Retrieve the time of this delay.
   *
   * @return The current delay time in seconds.
   */
  public float getTime() {

    // Return current time:
    return delayTime;
  }

  /**
   * Set the time of this delay.
   *
   * @param newTime New delay time in seconds.
   */
  public void setTime(float newTime) {

    // Set new time:
    if (newTime < Tweak.DELAY_MIN_TIME)
      delayTime = Tweak.DELAY_MIN_TIME;
    else if (newTime > Tweak.DELAY_MAX_TIME)
      delayTime = Tweak.DELAY_MAX_TIME;
    else
      delayTime = newTime;

    // Update max buffer position:
    delaySamples = (int) (delayTime * sampleRate);
    if (delaySamples < 2)
      delaySamples = 2;

    // Update read pointer:
    if (readPos >= delaySamples)
      readPos = 0;
  }

  /**
   * Retrieve the current enabled state of this delay.
   *
   * @return The current enabled state (true = on, false = off).
   */
  public boolean isEnabled() {

    // Return current state:
    return enabled;
  }

  /**
   * Set the current enabled state of this delay.
   *
   * @param newState New enabled state (true = on, false = off).
   */
  public void setEnabled(boolean newState) {

    // Update state:
    enabled = newState;

    // Clear buffer when disabled:
    if (!enabled)
      clearBuffer();
  }

  /**
   * Retrieve the feedback of this delay.
   *
   * @return The current feedback of this delay (0 = no, 1 = infinite).
   */
  public float getFeedback() {

    // Return current feedback:
    return feedBack;
  }

  /**
   * Set the feedback of this delay.
   *
   * @param newFeedback The new feedback of this delay (0 = no, 1 = infinite).
   */
  public void setFeedback(float newFeedback) {

    // Set new feedback:
    if (newFeedback < 0.0f)
      feedBack = 0.0f;
    else if (newFeedback > Tweak.DELAY_MAX_FEEDBACK)
      feedBack = Tweak.DELAY_MAX_FEEDBACK;
    else
      feedBack = newFeedback;
  }

  /**
   * Retrieve the mix of this delay.
   *
   * @return The current mix factor (0 = dry, 1 = wet).
   */
  public float getMix() {

    // Return current mix:
    return mix;
  }

  /**
   * Set the mix factor of this delay.

   * @param newMix New mix factor of this delay (0 = dry, 1 = wet).
   */
  public void setMix(float newMix) {

    // Set new feedback:
    if (newMix < 0.0)
      mix = 0.0f;
    else if (newMix > 1.0)
      mix = 1.0f;
    else
      mix = newMix;
  }

  /**
   * This is the main delay function.
   *
   * @param input The input value.
   * @return Returns the delayed value.
   */
  public float tick(float input) {

    if (!enabled)
      return input;

    // The current read pos is the next write position:
    int writePos = readPos;

    // Read delayed value:
    float readVal = buffer[readPos++];
    if (readPos >= delaySamples)
      readPos = 0;

    // Store input value and add feedbacked value:
    buffer[writePos] = input + (readVal * feedBack);

    // Return mixed value:
    return (readVal * mix) + (input * (1.0f - mix));
  }

  /**
   * Internal helper function to clear the delay buffer.
   */
  private void clearBuffer() {

    // No buffer, no fun:
    if (buffer == null)
      return;

    // Reset buffer values:
    for (int i = 0; i < buffer.length; i++)
      buffer[i] = 0.0f;
  }

  /** Current sample rate. */
  private int sampleRate = 44100;

  /** Is this delay enabled? */
  private boolean enabled = false;

  /** Time of this delay in seconds. */
  private float delayTime = 0.0f;

  /** Feedback of this delay. */
  private float feedBack = 0.0f;

  /** Mix of this delay. */
  private float mix = 0.0f;

  /** Number of valid samples in the buffer. */
  private int delaySamples = 0;

  /** Current buffer position. */
  private int readPos = 0;

  /** The delay sample buffer. */
  private float[] buffer = null;
}
