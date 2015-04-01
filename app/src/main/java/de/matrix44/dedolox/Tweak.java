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
  public static final double AHDSR_MIN_TIME = 0.00125;

  /** Max times for the envelopes in seconds. */
  public static final double AHDSR_MAX_TIME = 2.0;

  /** Level epsilon, used for termination of the envelope. */
  public static final double AHDSR_ENV_THRESHOLD = 0.0005;

  /** Min LFO speed in hertz: */
  public static final double LFO_MIN_SPEED = 0.5;

  /** Max LFO speed in hertz: */
  public static final double LFO_MAX_SPEED = 25.0;

  /** Min oscillator speed in hertz: */
  public static final double OSC_MIN_SPEED = 0.5;

  /** Max oscillator speed in hertz: */
  public static final double OSC_MAX_SPEED = 14000.0;

  /** Min filter cutoff: */
  public static final double FILTER_MIN_CUTOFF = 0.01;

  /** Max filter frequency in hertz: */
  public static final double FILTER_MAX_CUTOFF = 0.99;

  /** Max filter resonance: */
  public static final double FILTER_MAX_RESONANCE = 1.0;

  /** Min pulse width: */
  public static final double OSC_MIN_PW = 0.1;
}
