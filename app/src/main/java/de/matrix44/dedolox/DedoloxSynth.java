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
 * This is the actual synth of this app, It receives MIDI events and creates audio accordingly.
 */
public class DedoloxSynth {

  /**
   * Default constructor of this class.
   *
   * Initializes lookup tables and synth parameters.
   */
  public DedoloxSynth() {

    // Create note number to frequency conversion table:
    for (int i = 0; i < 127; i++)
      noteToFrequency[i] = 440.0 * Math.pow(2.0, ((double)(i - 69)) / 12.0);
  }

  /**
   * Retrieve the sample rate of this synth.
   *
   * @return Returns the current sample rate in samples / second.
   */
  public int getSampleRate() {

      // Return current sample rate:
      return sampleRate;
  }

  /**
   * Set the sample rate to use in this synth.
   *
   * You'll have to set this to the rate of the dsp system or the times and values will not be
   * correct.
   *
   * @param sampleRate The sample rate to use in samples / second.
   */
  public void setSampleRate(int sampleRate) {

    // Save sample rate:
    this.sampleRate = sampleRate;

    // Update sample rate dependent synth elements:
    ampEnvelope.setSampleRate(sampleRate);
    masterVolume.setSampleRate(sampleRate);
    lfo1.setSampleRate(sampleRate);
    lfo2.setSampleRate(sampleRate);
    filter.setSampleRate(sampleRate);
    oscillator1.setSampleRate(sampleRate);
    oscillator2.setSampleRate(sampleRate);
  }

  /**
   * Retrieve the buffer size of this synth.
   *
   * @return Returns the current buffer size in samples.
   */
  public int getBufferSize() {

    // Return current buffer size:
    return bufferSize;
  }

  /**
   * Set new buffer size of this synth.
   *
   * The buffer size is the maximum number of samples that may be requested from this synth from
   * the main audio thread.
   *
   * @param bufferSize New buffer size.
   */
  public void setBufferSize(int bufferSize) {

    // Save buffer size:
    this.bufferSize = bufferSize;

    // Update static caches...

    // Update buffer size dependent synth elements...
  }

  /**
   * The actual audio processing function.
   *
   * This function is called from the audio engine. The synth will fill the supplied buffers with
   * fresh audio data that can be sent to the audio hardware afterwards.
   *
   * @param left         Target buffer, left channel.
   * @param right        Target buffer, right channel.
   * @param sampleFrames Number of samples to process.
   * @param events       Incoming MIDI events.
   * @param eventCount   Number of valid entries in the events array.
   */
  public void process(double[] left, double[] right, int sampleFrames, MIDIEvent[] events, int eventCount) {

    // Update input data:
    handleMIDIEvents(events, eventCount);

    double freq = noteToFrequency[currentNote];
    oscillator1.setFrequency(freq);
    oscillator2.setFrequency(freq);

    for (int i = 0; i < sampleFrames; i++) {

      // Get envelope and LFO values:
      double ampEnvelopeValue = ampEnvelope.tick();
      double lfo1Value = lfo1.tick();
      double lfo2Value = lfo2.tick();

      double envVal = ampEnvelopeValue * currentVelocity;

      //envVal *= (lfo1Buffer[i] + 1.0) * 0.5;

      // Evaluate oscillators:
      double osc1Val  = oscillator1.tick();
      double osc2Val  = oscillator2.tick();
      double noiseVal = 2.0 * Math.random() - 1.0;
      double ringVal  = osc1Val * osc2Val * 2.0;

      // Clip ring modulated values:
      ringVal = ringVal - (ringVal * ringVal * ringVal) / 6.0;

      // Mix oscillators:
      double oscVal = (osc1Val * osc1Volume.tick()) + (ringVal  * ringmodVolume.tick()) +
                      (osc2Val * osc2Volume.tick()) + (noiseVal * noiseVolume.tick());

      // Apply amp envelope:
      oscVal *= envVal;

      // Apply filter:
      oscVal = filter.tick(oscVal);

      // Apply master volume:
      oscVal *= masterVolume.tick();

      // Write out:
      left[i] =  oscVal;
      right[i] = left[i];
    }
  }

  /**
   * Internal helper function that decodes incoming MIDI events and update the synth accordingly.
   *
   * The MIDI array is pre allocated so never use it's length to determine the number of events to
   * process.
   *
   * @param events     The MIDI events send to this synth.
   * @param eventCount Number of valid events in the events array,
   */
  private void handleMIDIEvents(MIDIEvent[] events, int eventCount) {

    // Anything to do?
    if (eventCount == 0)
      return;

    // Inspect MIDI messages and update synth parameters accordingly...
    for (int i = 0; i < eventCount; i++) {

      // Note on with zero velocity is treated as note off too:
      if (events[i].message == 0x80 || (events[i].message == 0x90 && events[i].value2 == 0)) {

        // Is this the currently played note?
        if (currentNote == events[i].value1)

          // Go to release phase:
          ampEnvelope.setGateOff();
      }

      // A new note pressed?
      if (events[i].message == 0x90) {

        // Save note values:
        currentNote     = events[i].value1;
        currentVelocity = (double)events[i].value2 / 127.0;

        // Start envelope:
        ampEnvelope.setGateOn();
      }

      // Control change
      else if (events[i].message == 0xB0) {

        // Scale value to range [0, 1]:
        double nrmVal = (double)events[i].value2 / 127.0;

        // Check CC numbers:
        switch (events[i].value1) {

          case MIDIImplementation.CC_FILTER_FREQ:
            filter.setFrequency(Tweak.FILTER_MIN_FREQ + ((Tweak.FILTER_MAX_FREQ - Tweak.FILTER_MIN_FREQ) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_MASTER_VOL:
            masterVolume.setValue(nrmVal);
            break;

          case MIDIImplementation.CC_FILTER_RESONANCE:
            filter.setResonance(nrmVal * Tweak.FILTER_MAX_RESONANCE);
            break;

          case MIDIImplementation.CC_AMPENV_ATTACK:
            ampEnvelope.setAttackTime(Tweak.AHDSR_MIN_TIME + ((Tweak.AHDSR_MAX_TIME - Tweak.AHDSR_MIN_TIME) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_AMPENV_HOLD:
            ampEnvelope.setHoldTime(Tweak.AHDSR_MIN_TIME + ((Tweak.AHDSR_MAX_TIME - Tweak.AHDSR_MIN_TIME) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_AMPENV_DECAY:
            ampEnvelope.setDecayTime(Tweak.AHDSR_MIN_TIME + ((Tweak.AHDSR_MAX_TIME - Tweak.AHDSR_MIN_TIME) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_AMPENV_SUSTAIN:
            ampEnvelope.setSustainLevel(nrmVal);
            break;

          case MIDIImplementation.CC_AMPENV_RELEASE:
            ampEnvelope.setReleaseTime(Tweak.AHDSR_MIN_TIME + ((Tweak.AHDSR_MAX_TIME - Tweak.AHDSR_MIN_TIME) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_LFO1_SPEED:
            lfo1.setFrequency(Tweak.LFO_MIN_SPEED + ((Tweak.LFO_MAX_SPEED - Tweak.LFO_MIN_SPEED) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_LFO1_WAVEFORM:
            if (events[i].value2 == 0)
              lfo1.setWaveForm(LFO.WaveForm.SINE);
            else if (events[i].value2 == 1)
              lfo1.setWaveForm(LFO.WaveForm.TRIANGLE);
            else if (events[i].value2 == 2)
              lfo1.setWaveForm(LFO.WaveForm.SAW);
            else if (events[i].value2 == 3)
              lfo1.setWaveForm(LFO.WaveForm.RECT);
            else if (events[i].value2 == 4)
              lfo1.setWaveForm(LFO.WaveForm.RANDOM);
            break;

          case MIDIImplementation.CC_LFO2_SPEED:
            lfo2.setFrequency(Tweak.LFO_MIN_SPEED + ((Tweak.LFO_MAX_SPEED - Tweak.LFO_MAX_SPEED) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_LFO2_WAVEFORM:
            if (events[i].value2 == 0)
              lfo2.setWaveForm(LFO.WaveForm.SINE);
            else if (events[i].value2 == 1)
              lfo2.setWaveForm(LFO.WaveForm.TRIANGLE);
            else if (events[i].value2 == 2)
              lfo2.setWaveForm(LFO.WaveForm.SAW);
            else if (events[i].value2 == 3)
              lfo2.setWaveForm(LFO.WaveForm.RECT);
            else if (events[i].value2 == 4)
              lfo2.setWaveForm(LFO.WaveForm.RANDOM);
            break;

          case MIDIImplementation.CC_OSC1_WAVEFORM:
            if (events[i].value2 == 0)
              oscillator1.setWaveForm(MainOscillator.WaveForm.SINE);
            else if (events[i].value2 == 1)
              oscillator1.setWaveForm(MainOscillator.WaveForm.TRIANGLE);
            else if (events[i].value2 == 2)
              oscillator1.setWaveForm(MainOscillator.WaveForm.RECT);
            else if (events[i].value2 == 3)
              oscillator1.setWaveForm(MainOscillator.WaveForm.SAW);
            break;

          default:
            break;
        }
      }
    }
  }

  /** Lookup table for MIDI note numbers. */
  private final double[] noteToFrequency = new double[127];

  /** Current sample rate for this synth in samples/second. */
  private int sampleRate = 44100;

  /** Current audio buffer size in samples. */
  private int bufferSize = 256;

  /** Currently played MIDI note. */
  private int currentNote = 64;

  /** Velocity value [0-1] of the current note. */
  private double currentVelocity = 1.0;

  /** The amplifier envelope. */
  private final AHDSREnvelope ampEnvelope = new AHDSREnvelope();

  /** First LFO. */
  private final LFO lfo1 = new LFO();

  /** Second LFO. */
  private final LFO lfo2 = new LFO();

  /** Master volume of this synth. */
  private final SmoothParameter masterVolume = new SmoothParameter(0.75);

  /** Mixer, oscillator 1 volume. */
  private final SmoothParameter osc1Volume = new SmoothParameter(1.0);

  /** Mixer, oscillator 2 volume. */
  private final SmoothParameter osc2Volume = new SmoothParameter(0.0);

  /** Mixer, noise oscillator volume. */
  private final SmoothParameter noiseVolume = new SmoothParameter(0.0);

  /** Mixer, ring modulated volume. */
  private final SmoothParameter ringmodVolume = new SmoothParameter(0.0);

  /** The filter used in this synth. */
  private final MoogFilter filter = new MoogFilter();

  /** First main oscillator. */
  private final MainOscillator oscillator1 = new MainOscillator();

  /** Second main oscillator. */
  private final MainOscillator oscillator2 = new MainOscillator();
}
