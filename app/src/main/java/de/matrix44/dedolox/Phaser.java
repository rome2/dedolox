package de.matrix44.dedolox;

/**
 * Created by rollo on 17.04.15.
 */
public class Phaser {

  /**
   * Default constructor of this class.
   */
  public Phaser() {

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

    // Update phaser steps:
    minDelta = Tweak.PHASER_MIN_FREQ / (0.5f * sampleRate);
    maxDelta = Tweak.PHASER_MAX_FREQ / (0.5f * sampleRate);

    // Update rate:
    setRate(rate);
  }

  /**
   * Retrieve the current enabled state of this phaser.
   *
   * @return The current enabled state (true = on, false = off).
   */
  public boolean isEnabled() {

    // Return current state:
    return enabled;
  }

  /**
   * Set the current enabled state of this phaser.
   *
   * @param newState New enabled state (true = on, false = off).
   */
  public void setEnabled(boolean newState) {

    // Update state:
    enabled = newState;

    // Clear buffer when disabled:
    if (!enabled) {
      for (int i = 0; i < Tweak.PHASER_STAGES; i++)
        buffer[i] = 0.0f;
    }
  }

  public float getRate() {

    // Return current rate:
    return rate;
  }

  public void setRate(float newRate) {

    // Set clipped value:
    if (newRate < Tweak.PHASER_MIN_RATE)
      rate = Tweak.PHASER_MIN_RATE;
    else if (newRate > Tweak.PHASER_MAX_RATE)
      rate = Tweak.PHASER_MAX_RATE;
    else
      rate = newRate;

    // Update LFO phase stepping:
    lfoStep = 2.0f * pi * (rate / sampleRate);
  }

  public float getFeedback() {

    // Return current feedback:
    return feedback;
  }

  public void setFeedback(float newFeedback) {

    // Set clipped value:
    if (newFeedback < 0.0f)
      feedback = 0.0f;
    else if (newFeedback > Tweak.PHASER_MAX_FEEDBACK)
      feedback = Tweak.PHASER_MAX_FEEDBACK;
    else
      feedback = newFeedback;
  }

  public float getDepth() {

    // Return current depth:
    return depth;
  }

  public void setDepth(float newDepth) {

    // Set clipped value:
    if (newDepth < 0.0f)
      depth = 0.0f;
    else if (newDepth > Tweak.PHASER_MAX_DEPTH)
      depth = Tweak.PHASER_MAX_DEPTH;
    else
      depth = newDepth;
  }

  public float tick(float input) {

    // Anything to do?
    if (!enabled)
      return input;

    // Calc lfo phase and allpass filter coefficient:
    float delaytime = minDelta + (maxDelta - minDelta) * (((float)Math.sin(omega) + 1.0f) / 2.0f);
    float coeff = (1.0f - delaytime) / (1.0f + delaytime);

    // Update LFO phase:
    omega += lfoStep;
    if (omega >= tau)
      omega -= tau;

    // Process all allpass filters:
    float output = input + lastSample * feedback;
    for (int i = Tweak.PHASER_STAGES - 1; i >= 0; i--) {
      float temp = output * -coeff + buffer[i];
      buffer[i] = temp * coeff + output;
      output = temp;
    }

    // Save sample for next round:
    lastSample = output;

    // Add scaled output to input:
    return (output * depth) + (input * (1.0f - depth));
  }

  /** One pi. */
  private static final float pi = (float)Math.PI;

  /** Well, two pies... */
  private static final float tau = (float)(Math.PI * 2.0);

  private int sampleRate = 44100;

  private boolean enabled = false;

  private float rate = 0.0f;

  private float feedback = 0.0f;

  private float depth = 0.0f;

  private float omega = 0.0f;

  private float lastSample = 0.0f;

  private float minDelta = 0.0f;

  private float maxDelta = 0.0f;

  private float lfoStep = 0.0f;

  private final float[] buffer = new float[Tweak.PHASER_STAGES];
}

