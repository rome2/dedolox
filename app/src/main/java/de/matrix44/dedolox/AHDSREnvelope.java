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
 * @since   2014-12-13
 */

package de.matrix44.dedolox;

/**
 * This class implements an exponential AHDSR envelope (Attack, Hold, Decay, Sustain, Release) with
 * a first order lowpass IIR filter. It is very similar to the way analog synths implement ADSR
 * envelopes (with RC circuits) so it is very analog sounding. The attack time, decay time, sustain
 * level and release time are all independently adjustable. Typical A, D and R time range from 1
 * millisecond to 20 (or more) seconds. Most synths have one EG for the VCA and one for the VCF. The
 * generated values are between 0.0 and 1.0.
 */
public class AHDSREnvelope {

  /**
   * Default constructor of this class. Initializes all members to default values.
   */
  public AHDSREnvelope() {

    // Calc initial sample length:
    timeIncrement = 1.0f / sampleRate;

    // Initialize filter values:
    setAttackTime(attackTime);
    setDecayTime(decayTime);
    setReleaseTime(releaseTime);
  }

  /**
   * Retrieve the sample rate of this envelope.
   *
   * @return Returns the current sample rate in samples / second.
   */
  public int getSampleRate() {

    // Return current sample rate:
    return sampleRate;
  }

  /**
   * Set the sample rate to use in this envelope.
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

    // Recalculate sample length:
    timeIncrement = 1.0f / sampleRate;
  }

  /**
   * Retrieves the length of the envelope's hold part.
   *
   * @return  Returns the attack hold time in seconds.
   */
  public float getAttackTime() {

    // Return current attack time:
    return attackTime;
  }

  /**
   * Sets the length of the envelope's attack part.
   *
   * The exponential curve reaches it's maximum value when the attack time is reached. Values below
   * zero will be clipped to zero.
   *
   * @param newAttackTime The new time in seconds.
   */
  public void setAttackTime(float newAttackTime) {

    // Set clipped value:
    if (newAttackTime < Tweak.AHDSR_MIN_TIME)
      attackTime = Tweak.AHDSR_MIN_TIME;
    else if (newAttackTime > Tweak.AHDSR_MAX_TIME)
      attackTime = Tweak.AHDSR_MAX_TIME;
    else
      attackTime  = newAttackTime;

    // Recalculate attack filter coefficient:
    attackCoeff = 1.0f - (float)Math.exp(-1.0f / (attackTime * sampleRate));
  }

  /**
   * Retrieves the length of the envelope's hold part.
   *
   * @return Returns the current hold time in seconds.
   */
  public float getHoldTime() {

    // Return current hold time:
    return holdTime;
  }

  /**
   * Sets the length of the envelope's hold part.
   *
   * The hold time holds the envelope at it's maximum value when the attack time is reached until
   * the decay phase starts. Values below zero will be clipped to zero.
   *
   * @param newHoldTime The new time in seconds.
   */
  public void setHoldTime(float newHoldTime)
  {
    // Set clipped value:
    if (newHoldTime < Tweak.AHDSR_MIN_TIME)
      holdTime = Tweak.AHDSR_MIN_TIME;
    else if (newHoldTime > Tweak.AHDSR_MAX_TIME)
      holdTime = Tweak.AHDSR_MAX_TIME;
    else
      holdTime = newHoldTime;
  }

  /**
   * Retrieves the length of the envelope's decay part.
   *
   * @return Returns the current decay time in seconds.
   */
  public float getDecayTime() {

    // Return current decay time:
    return decayTime;
  }

  /**
   * Sets the length of the envelope's decay part.
   *
   * The decay phase of the envelope will start at maximum and decay towards the sustain level in
   * the specified amount of seconds with an exponential curve. Time values below zero will be
   * clipped to zero.
   *
   * @param newDecayTime The new decay time in seconds.
   */
  public void setDecayTime(float newDecayTime) {

    // Set clipped value:
    if (newDecayTime < Tweak.AHDSR_MIN_TIME)
      decayTime = Tweak.AHDSR_MIN_TIME;
    else if (newDecayTime > Tweak.AHDSR_MAX_TIME)
      decayTime = Tweak.AHDSR_MAX_TIME;
    else
      decayTime  = newDecayTime;

    // Recalculate decay filter coefficient:
    decayCoeff = 1.0f - (float)Math.exp(-1.0f / (decayTime * 0.2f * sampleRate));
  }

  /**
   * Retrieves the level of the envelope's sustain part.
   *
   * @return Returns the current level in percent.
   */
  public float getSustainLevel() {

    // Return current sustain level:
    return sustainLevel;
  }

  /**
   * Sets the level of the envelope's sustain part.
   *
   * The sustain phase of the envelope follows the decay phase and it constant until the gate is
   * closed. The level is linear from zero to one. Values outside this range are automatically
   * clamped to this range.
   *
   * @param newSustainLevel The new sustain level in percent.
   */
  public void setSustainLevel(float newSustainLevel) {

    // Set clipped value:
    if (newSustainLevel < 0.0f)
      sustainLevel = 0.0f;
    else if (newSustainLevel > 1.0f)
      sustainLevel = 1.0f;
    else
      sustainLevel = newSustainLevel;
  }

  /**
   * Retrieves the length of the envelope's release part.
   *
   * @return Returns the current release time in seconds.
   */
  public float getReleaseTime() {

    // Return current release time:
    return releaseTime;
  }

  /**
   * Sets the length of the envelope's release part.
   *
   * The release phase of the envelope will start at the sustain level and fade down to zero in the
   * specified amount of seconds with an exponential curve. Time values below zero will be clipped
   * to zero.
   *
   * @param newReleaseTime The new release time in seconds.
   */
  public void setReleaseTime(float newReleaseTime) {

    // Set clipped value:
    if (newReleaseTime < Tweak.AHDSR_MIN_TIME)
      releaseTime = Tweak.AHDSR_MIN_TIME;
    else if (newReleaseTime > Tweak.AHDSR_MAX_TIME)
      releaseTime = Tweak.AHDSR_MAX_TIME;
    else
      releaseTime = newReleaseTime;

    // Recalculate release filter coefficient:
    releaseCoeff = 1.0f - (float)Math.exp(-1.0f / (releaseTime * 0.2f * sampleRate));
  }

  /**
   * Retrieves the active state of the envelope.
   *
   * The envelope will never actually reach zero because of the implementation so the envelope
   * terminates at a very small value.
   *
   * @return Returns true if the envelope's release phase has reached zero or false otherwise.
   */
  public boolean isDone() {

    // Check gate state:
    return !gateOn && currentStage == EnvelopeStage.OFF;
  }

  /**
   * Retrieves the current state of the gate signal.
   *
   * Useful to avoid unwanted re-triggering.
   *
   * @return Returns true if the gate is on or or false otherwise.
   */
  public boolean getGateState() {

    // Return current state:
    return gateOn;
  }

  /**
   * Start the envelope.
   *
   * This will reset the position of the envelope and prepare everything needed in the processing
   * stage.
   */
  public void setGateOn() {

    // Reset time:
    currentTime = 0.0f;

    // Turn on gate:
    gateOn = true;

    // Start with a new attack phase:
    currentStage = EnvelopeStage.ATTACK;
  }

  /**
   * Switch the envelope from the sustain to the release phase.
   *
   * This will not stop the envelope but it will switch from sustain to release. The envelope itself
   * will continue until the value reaches zero (see also isDone() above) or another setGateOn() is
   * received.
   */
  public void setGateOff() {

    // Turn gate off:
    gateOn = false;

    // If still active, switch to release:
    if (currentStage != EnvelopeStage.OFF)
      currentStage = EnvelopeStage.RELEASE;

    // Move current time stamp into the sustain/release phase:
    currentTime = attackTime + holdTime + decayTime + timeIncrement;
  }

  /**
   * Reset the envelope to the starting position.
   *
   * This will not stop the envelope, just reset the position.
   */
  public void reset() {

    // Set time to start of the envelope:
    currentTime = 0.0f;

    // Start with a new attack:
    currentStage = EnvelopeStage.ATTACK;

    // Clear filter value:
    lastSample = 0.0f;
  }

  /**
   * This is the place where the envelope values are calculated.
   *
   * The time position is automatically advanced to the next sample when the function returns.
   *
   * @return Returns the envelope value at the current time and stage.
   */
  public float tick() {

    // Get value:
    float result = 0.0f;
    switch (currentStage) {

      // Attack stage?
      case ATTACK:

        // Get filter value:
        result = lastSample + attackCoeff * ((1.0f / 0.633f) - lastSample);

        // Next stage?
        if (currentTime > attackTime)
          currentStage = EnvelopeStage.HOLD;

        break;

      // Hold stage?
      case HOLD:

        // Hold the last known value:
        result = lastSample;

        // Next stage?
        if (currentTime > (attackTime + holdTime))
          currentStage = EnvelopeStage.DECAY;

        break;

      // Decay stage?
      case DECAY:

        // Get filter value:
        result = lastSample + decayCoeff * (sustainLevel - lastSample);

        // Next stage?
        if (currentTime > (attackTime + holdTime + decayTime))
          currentStage = EnvelopeStage.SUSTAIN;

        break;

      // Sustain stage?
      case SUSTAIN:

        // Just hold the sustain value:
        result = lastSample;

        // Key released?
        if (!gateOn)
          currentStage = EnvelopeStage.RELEASE;

        break;

      // Release stage?
      case RELEASE:

        // Get filter value:
        result = lastSample + releaseCoeff * (-lastSample);

        // Terminated?
        if (lastSample < Tweak.AHDSR_ENV_THRESHOLD)
          currentStage = EnvelopeStage.OFF;

        break;

      // Done?
      case OFF:

        // Envelope has finished:
        result = 0.0f;
        break;
    }

    // Advance to next sample position:
    currentTime += timeIncrement;

    // Store value for next round:
    lastSample = result;

    // Done:
    return result;
  }

  /** The available stages of the envelope. */
  private enum EnvelopeStage { ATTACK, HOLD, DECAY, SUSTAIN, RELEASE, OFF }

  /** Current envelope stage. */
  private EnvelopeStage currentStage = EnvelopeStage.OFF;

  /** The sample rate of the system (in samples / second). */
  private int sampleRate = 44100;

  /** The attack time of this envelope (in seconds). */
  private float attackTime = Tweak.AHDSR_MIN_TIME;

  /** The hold time of this envelope (in seconds). */
  private float holdTime = Tweak.AHDSR_MIN_TIME;

  /** The decay time of this envelope (in seconds). */
  private float decayTime = Tweak.AHDSR_MIN_TIME;

  /** The sustain level of this envelope (in percent). */
  private float sustainLevel = 0.0f;

  /** The attack time of this envelope (in seconds). */
  private float releaseTime = Tweak.AHDSR_MIN_TIME;

  /** Current state of the gate (on /off). */
  private boolean gateOn = false;

  /** Current time off the envelope (in seconds). */
  private float currentTime = 0.0f;

  /** Length of a sample (in seconds). */
  private float timeIncrement = 0.0f;

  /** Temp variable to preserve the last filter sample. */
  private float lastSample = 0.0f;

  /** Coefficient for the attack filter. */
  private float attackCoeff = 0.0f;

  /** Coefficient for the decay filter. */
  private float decayCoeff = 0.0f;

  /** Coefficient for the release filter. */
  private float releaseCoeff = 0.0f;
}
