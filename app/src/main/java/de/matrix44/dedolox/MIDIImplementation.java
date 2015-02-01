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

  /** Master volume of the entire synth. */
  public static final int CC_MASTER_VOL = 7;

  /** Filter resonance. */
  public static final int CC_FILTER_RESONANCE = 9;

  /** Sustain level of the amplifier envelope. */
  public static final int CC_AMPENV_SUSTAIN = 71;

  /** Release time of the amplifier envelope. */
  public static final int CC_AMPENV_RELEASE = 72;

  /** Attack time of the amplifier envelope. */
  public static final int CC_AMPENV_ATTACK = 73;

  /** Hold time of the amplifier envelope. */
  public static final int CC_AMPENV_HOLD = 74;

  /** Decay time of the amplifier envelope. */
  public static final int CC_AMPENV_DECAY = 75;

  /** LFO 1 speed. */
  public static final  int CC_LFO1_SPEED = 76;

  /**
   *  LFO 1 wave form.
   *
   *  0: Sine
   *  1: Triangle
   *  2: Saw
   *  3: Rectangle
   *  4: Sample and hold
   *  */
  public static final int CC_LFO1_WAVEFORM = 78;

  /** LFO 2 speed. */
  public static final  int CC_LFO2_SPEED = 79;

  /**
   *  LFO 2 wave form.
   *
   *  0: Sine
   *  1: Triangle
   *  2: Saw
   *  3: Rectangle
   *  4: Sample and hold
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
}
