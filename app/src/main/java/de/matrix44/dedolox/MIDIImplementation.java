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
 * This class contains the control change constants.
 *
 * The synth is internally controlled by sending MIDI messages from the graphical user interface
 * and from external MIDI devices. Every public parameter of the synth has one entry in this
 * parameter list.
 */
public final class MIDIImplementation {

  /** Filter frequency. */
  public static final int CC_FILTER_FREQ = 3;

  /** Filter mode.
   *
   * 0: Low pass.
   * 1: High pass.
   * 2: Band pass.
   * */
  public static final int CC_FILTER_MODE = 4;

  /** Master volume of the entire synth. */
  public static final int CC_MASTER_VOL = 7;

  /** Filter resonance. */
  public static final int CC_FILTER_RESONANCE = 9;

  /** Filter slope.
   *
   * 0: 12 dB.
   * 1: 24 db.
   * */
  public static final int CC_FILTER_SLOPE = 14;

  /** Sustain level of the filter envelope. */
  public static final int CC_FILTERENV_SUSTAIN = 15;

  /** Release time of the filter envelope. */
  public static final int CC_FILTERENV_RELEASE = 20;

  /** Attack time of the filter envelope. */
  public static final int CC_FILTERENV_ATTACK = 21;

  /** Decay time of the filrwe envelope. */
  public static final int CC_FILTERENV_DECAY = 22;

  /** Sustain level of the amplifier envelope. */
  public static final int CC_AMPENV_SUSTAIN = 71;

  /** Release time of the amplifier envelope. */
  public static final int CC_AMPENV_RELEASE = 72;

  /** Attack time of the amplifier envelope. */
  public static final int CC_AMPENV_ATTACK = 73;

  /** Decay time of the amplifier envelope. */
  public static final int CC_AMPENV_DECAY = 75;

  /** LFO 1 speed. */
  public static final  int CC_LFO1_SPEED = 76;

  /**
   *  LFO 1 wave form.
   *
   *  0: Sine
   *  1: Saw
   *  2: Rectangle
   *  3: Sample and hold
   *  */
  public static final int CC_LFO1_WAVEFORM = 78;

  /** LFO 2 speed. */
  public static final  int CC_LFO2_SPEED = 79;

  /**
   *  LFO 2 wave form.
   *
   *  0: Sine
   *  1: Saw
   *  2: Rectangle
   *  3: Sample and hold
   *  */
  public static final int CC_LFO2_WAVEFORM = 80;

  /**
   *  OSC 1 wave form.
   *
   *  0: Sine
   *  1: Triangle
   *  2: Rectangle
   *  3: Saw
   *  */
  public static final int CC_OSC1_WAVEFORM = 81;

  /**
   * OSC 1 coarse tuning.
   *
   * Range is +-12 semitones, 64 is center.
   */
  public static final int CC_OSC1_COARSE = 82;

  /**
   * OSC 1 fine tuning.
   *
   * Range is +-1 semitone, 64 is center.
   */
  public static final int CC_OSC1_FINE = 83;

  /** OSC 1 pulse width. */
  public static final int CC_OSC1_PULSE = 84;

  /** OSC 1 level. */
  public static final int CC_OSC1_LEVEL = 85;

  /**
   *  OSC 2 wave form.
   *
   *  0: Sine
   *  1: Triangle
   *  2: Rectangle
   *  3: Saw
   *  */
  public static final int CC_OSC2_WAVEFORM = 86;

  /**
   * OSC 2 coarse tuning.
   *
   * Range is +-12 semitones, 64 is center.
   */
  public static final int CC_OSC2_COARSE = 87;

  /**
   * OSC 2 fine tuning.
   *
   * Range is +-1 semitone, 64 is center.
   */
  public static final int CC_OSC2_FINE = 88;

  /** OSC 2 pulse width. */
  public static final int CC_OSC2_PULSE = 89;

  /** OSC 1 level. */
  public static final int CC_OSC2_LEVEL = 90;

  /** Noise level. */
  public static final int CC_NOISE_LEVEL = 91;

  /** Ringmod level. */
  public static final int CC_RINGMOD_LEVEL = 92;
}
