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
  public static final int CC_FILTER_CUTOFF = 3;

  /** Filter mode.
   *
   * 0: Low pass.
   * 1: High pass.
   * 2: Band pass.
   * */
  public static final int CC_FILTER_MODE = 4;

  /** Portamento time (0 = off). */
  public static final int CC_PORTAMENTO_TIME = 5;

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

  /** LFO 1 amp modulation. */
  public static final int CC_LFO1_VOLUME = 23;

  /** LFO 2 filter modulation. */
  public static final int CC_LFO2_CUTOFF = 24;

  /** LFO 2 pulse width modulation. */
  public static final int CC_LFO2_PULSE = 25;

  /** Velocity sensitivity. */
  public static final int CC_VELOCITY_SENS = 26;

  // [...]
  // 31

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

  /** LFO 1 pitch modulation. */
  public static final int CC_LFO1_PITCH = 77;

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
  public static final int CC_OSC2_FINE = 89;

  /** OSC 2 pulse width. */
  public static final int CC_OSC2_PULSE = 90;

  /** OSC 1 level. */
  public static final int CC_OSC2_LEVEL = 102;

  /** Noise level. */
  public static final int CC_NOISE_LEVEL = 103;

  /** Oscillator drive. */
  public static final int CC_MIXER_DRIVE = 104;

  /** Phaser on/off (0 = off, 1 = on) */
  public static final int CC_PHASER_ENABLE = 105;

  /** Phaser rate. */
  public static final int CC_PHASER_RATE = 106;

  /** Phaser depth. */
  public static final int CC_PHASER_DEPTH = 107;

  /** Phaser feedback. */
  public static final int CC_PHASER_FEEDBACK = 108;

  /** Delay on/off (0 = off, 1 = on) */
  public static final int CC_DELAY_ENABLE = 109;

  /** Delay time. */
  public static final int CC_DELAY_TIME = 110;

  /** Delay feedback. */
  public static final int CC_DELAY_FEEDBACK = 111;

  /** Delay mix. */
  public static final int CC_DELAY_MIX = 112;

  // [...]
  // 119
}
