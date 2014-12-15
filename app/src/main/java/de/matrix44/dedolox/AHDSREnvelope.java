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
    timeIncrement = 1.0 / sampleRate;

    // Initialize filter values:
    setAttackTime(attackTime.getValue());
    setDecayTime(decayTime.getValue());
    setReleaseTime(releaseTime.getValue());
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
    timeIncrement = 1.0 / (double)sampleRate;

    // Update parameters:
    attackTime.setSampleRate(newSampleRate);
    holdTime.setSampleRate(newSampleRate);
    decayTime.setSampleRate(newSampleRate);
    sustainLevel.setSampleRate(newSampleRate);
    releaseTime.setSampleRate(newSampleRate);
    attackCoeff.setSampleRate(newSampleRate);
    decayCoeff.setSampleRate(newSampleRate);
    releaseCoeff.setSampleRate(newSampleRate);

    // Update envelope filters:
    setAttackTime(attackTime.getValue());
    setDecayTime(decayTime.getValue());
    setReleaseTime(releaseTime.getValue());
  }

  /**
   * Retrieves the length of the envelope's hold part.
   *
   * @return  Returns the attack hold time in seconds.
   */
  public double getAttackTime() {

    // Return current attack time:
    return attackTime.getValue();
  }

  /**
   * Sets the length of the envelope's attack part.
   *
   * The exponential curve reaches it's maximum value when the attack time is reached. Values below
   * zero will be clipped to zero.
   *
   * @param newAttackTime The new time in seconds.
   */
  public void setAttackTime(double newAttackTime) {

    // Check attack time:
    if (newAttackTime < Tweak.AHDSR_MIN_TIME)
      newAttackTime = Tweak.AHDSR_MIN_TIME;
    else if (newAttackTime > Tweak.AHDSR_MAX_TIME)
      newAttackTime = Tweak.AHDSR_MAX_TIME;

    // Recalculate attack filter coefficient:
    attackTime.setValue(newAttackTime);
    attackCoeff.setValue(1.0 - Math.exp(-1.0 / ((double)sampleRate * newAttackTime)));
  }

  /**
   * Retrieves the length of the envelope's hold part.
   *
   * @return Returns the current hold time in seconds.
   */
  public double getHoldTime() {

    // Return current hold time:
    return holdTime.getValue();
  }

  /**
   * Sets the length of the envelope's hold part.
   *
   * The hold time holds the envelope at it's maximum value when the attack time is reached until
   * the decay phase starts. Values below zero will be clipped to zero.
   *
   * @param newHoldTime The new time in seconds.
   */
  public void setHoldTime(double newHoldTime)
  {
    // Set clipped value:
    if (newHoldTime < Tweak.AHDSR_MIN_TIME)
      holdTime.setValue(Tweak.AHDSR_MIN_TIME);
    else if (newHoldTime > Tweak.AHDSR_MAX_TIME)
      holdTime.setValue(Tweak.AHDSR_MAX_TIME);
    else
      holdTime.setValue(newHoldTime);
  }

  /**
   * Retrieves the length of the envelope's decay part.
   *
   * @return Returns the current decay time in seconds.
   */
  public double getDecayTime() {

    // Return current decay time:
    return decayTime.getValue();
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
  public void setDecayTime(double newDecayTime) {

    // Check decay time:
    if (newDecayTime < Tweak.AHDSR_MIN_TIME)
      newDecayTime = Tweak.AHDSR_MIN_TIME;
    else if (newDecayTime > Tweak.AHDSR_MAX_TIME)
      newDecayTime = Tweak.AHDSR_MAX_TIME;

    // Recalculate decay filter coefficient:
    decayTime.setValue(newDecayTime);
    decayCoeff.setValue(1.0 - Math.exp(-1.0 / ((double)sampleRate * newDecayTime * 0.2)));
  }

  /**
   * Retrieves the level of the envelope's sustain part.
   *
   * @return Returns the current level in percent.
   */
  public double getSustainLevel() {

    // Return current sustain level:
    return sustainLevel.getValue();
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
  public void setSustainLevel(double newSustainLevel) {

    // Clip value:
    if (newSustainLevel < 0.0)
      sustainLevel.setValue(0.0);
    else if (newSustainLevel > 1.0)
      sustainLevel.setValue(1.0);
    else
      sustainLevel.setValue(newSustainLevel);
  }

  /**
   * Retrieves the length of the envelope's release part.
   *
   * @return Returns the current release time in seconds.
   */
  public double getReleaseTime() {

    // Return current release time:
    return releaseTime.getValue();
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
  public void setReleaseTime(double newReleaseTime) {

    // Check release time:
    if (newReleaseTime < Tweak.AHDSR_MIN_TIME)
      newReleaseTime = Tweak.AHDSR_MIN_TIME;
    else if (newReleaseTime > Tweak.AHDSR_MAX_TIME)
      newReleaseTime = Tweak.AHDSR_MAX_TIME;

    // Recalculate release filter coefficient:
    releaseTime.setValue(newReleaseTime);
    releaseCoeff.setValue(1.0 - Math.exp(-1.0 / ((double)sampleRate * newReleaseTime * 0.2)));
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
    currentTime = 0.0;

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
    currentTime = attackTime.getValue() + holdTime.getValue() + decayTime.getValue() + timeIncrement;
  }

  /**
   * Reset the envelope to the starting position.
   *
   * This will not stop the envelope, just reset the position.
   */
  public void reset() {

    // Set time to start of the envelope:
    currentTime = 0.0;

    // Start with a new attack:
    currentStage = EnvelopeStage.ATTACK;

    // Clear filter value:
    lastSample = 0.0;
  }

  /**
   * This is the place where the envelope values are calculated.
   *
   * The time position is automatically advanced to the next sample when the function returns.
   *
   * @return Returns the envelope value at the current time and stage.
   */
  public double tick() {

    // Tick parameters:
    releaseTime.tick();
    double at = attackTime.tick();
    double ht = holdTime.tick();
    double dt = decayTime.tick();
    double sl = sustainLevel.tick();
    double ac = attackCoeff.tick();
    double dc = decayCoeff.tick();
    double rc = releaseCoeff.tick();

    // Done?
    if (currentStage == EnvelopeStage.OFF)
      return 0.0;

    double result = 0.0;

    // Attack stage?
    if (currentStage == EnvelopeStage.ATTACK) {

      // Get filter value:
      result = lastSample + ac * ((1.0 / 0.633) - lastSample);

      // Advance to next sample:
      currentTime += timeIncrement;
      if (currentTime > at)
        currentStage = EnvelopeStage.HOLD;
    }

    // Hold stage?
    else if (currentStage == EnvelopeStage.HOLD) {

      // Hold the last known value:
      result = lastSample;

      // Advance to next sample:
      currentTime += timeIncrement;
      if (currentTime > (at + ht))
        currentStage = EnvelopeStage.DECAY;
    }

    // Decay stage?
    else if (currentStage == EnvelopeStage.DECAY) {

      // Get filter value:
      result = lastSample + dc * (sl - lastSample);

      // Advance to next sample:
      currentTime += timeIncrement;
      if (currentTime > (at + ht + dt))
        currentStage = EnvelopeStage.SUSTAIN;
    }

    // Sustain stage?
    else if (currentStage == EnvelopeStage.SUSTAIN) {

      // Just hold the sustain value:
      result = lastSample;

      // Key released?
      if (!gateOn)
        currentStage = EnvelopeStage.RELEASE;
    }

    // Release stage?
    else if (currentStage == EnvelopeStage.RELEASE) {

      // Get filter value:
      result = lastSample + rc * (-lastSample);

      // Terminated?
      if (lastSample < Tweak.AHDSR_ENV_THRESHOLD)
        currentStage = EnvelopeStage.OFF;
    }

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
  private final SmoothParameter attackTime = new SmoothParameter(Tweak.AHDSR_MIN_TIME);

  /** The hold time of this envelope (in seconds). */
  private final SmoothParameter holdTime = new SmoothParameter(Tweak.AHDSR_MIN_TIME);

  /** The decay time of this envelope (in seconds). */
  private final SmoothParameter decayTime = new SmoothParameter(Tweak.AHDSR_MIN_TIME);

  /** The sustain level of this envelope (in percent). */
  private final SmoothParameter sustainLevel = new SmoothParameter(0.0);

  /** The attack time of this envelope (in seconds). */
  private final SmoothParameter releaseTime = new SmoothParameter(Tweak.AHDSR_MIN_TIME);

  /** Current state of the gate (on /off). */
  private boolean gateOn = false;

  /** Current time off the envelope (in seconds). */
  private double currentTime = 1.0e30;

  /** Length of a sample (in seconds). */
  private double timeIncrement = 0.0;

  /** Temp variable to preserve the last filter sample. */
  private double lastSample = 0.0;

  /** Coefficient for the attack filter. */
  private final SmoothParameter attackCoeff = new SmoothParameter(1.0);

  /** Coefficient for the decay filter. */
  private final SmoothParameter decayCoeff = new SmoothParameter(1.0);

  /** Coefficient for the release filter. */
  private final SmoothParameter releaseCoeff = new SmoothParameter(1.0);
}
