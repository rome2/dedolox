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
 * This class holds the constants that define the behaviour of the synth. It is a central place to
 * tweak the sound and other important properties.
 */
public class Tweak {

  /** Min times for the envelopes in seconds. */
  public static final float AHDSR_MIN_TIME = 0.00125f;

  /** Max times for the envelopes in seconds. */
  public static final float AHDSR_MAX_TIME = 2.0f;

  /** Level epsilon, used for termination of the envelope. */
  public static final float AHDSR_ENV_THRESHOLD = 0.0005f;

  /** Min LFO speed in hertz: */
  public static final float LFO_MIN_SPEED = 0.5f;

  /** Max LFO speed in hertz: */
  public static final float LFO_MAX_SPEED = 25.0f;

  /** Min oscillator speed in hertz: */
  public static final float OSC_MIN_SPEED = 0.5f;

  /** Max oscillator speed in hertz: */
  public static final float OSC_MAX_SPEED = 14000.0f;

  /** Min filter cutoff: */
  public static final float FILTER_MIN_CUTOFF = 0.01f;

  /** Max filter cutoff: */
  public static final float FILTER_MAX_CUTOFF = 0.99f;

  /** Max filter resonance: */
  public static final float FILTER_MAX_RESONANCE = 1.0f;

  /** Min pulse width: */
  public static final float OSC_MIN_PW = 0.1f;

  /** Min delay time. */
  public static final float DELAY_MIN_TIME = 0.075f;

  /** Max delay time. */
  public static final float DELAY_MAX_TIME = 1.2f;

  /** Max delay feedback. */
  public static final float DELAY_MAX_FEEDBACK = 0.75f;
}
