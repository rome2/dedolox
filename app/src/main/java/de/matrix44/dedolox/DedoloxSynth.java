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
      noteToFrequency[i] = 440.0f * (float)Math.pow(2.0f, ((float)(i - 69)) / 12.0f);
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
    lfo1.setSampleRate(sampleRate);
    lfo2.setSampleRate(sampleRate);
    masterVolume.setSampleRate(sampleRate);
    osc1Volume.setSampleRate(sampleRate);
    osc2Volume.setSampleRate(sampleRate);
    noiseVolume.setSampleRate(sampleRate);
    mixerDrive.setSampleRate(sampleRate);
    filter.setSampleRate(sampleRate);
    oscillator1.setSampleRate(sampleRate);
    oscillator2.setSampleRate(sampleRate);
    delay.setSampleRate(sampleRate);
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
   * Extract the current state of the synth as preset.
   *
   * @return A preset containing the state of the synth.
   */
  public DedoloxPreset getPreset() {
    return new DedoloxPreset(currentBuffer);
  }

  /**
   * Set a new state from a preset.

   * @param preset The new state.
   */
  public void loadPreset(DedoloxPreset preset) {

    // Block audio out:
    muted = true;

    // Load values:
    handleMIDIEvents(preset.getValues(), 128);

    // Copy meta data:
    currentBuffer.copyMeta(preset);

    // Unblock audio:
    muted = false;
  }

  /**
   *  Is the synth currently muted?
   *
   * @return The current muted state.
   */
  public boolean isMuted() {
    return muted;
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
  public void process(float[] left, float[] right, int sampleFrames, MIDIEvent[] events, int eventCount) {

    // Currently muted?
    if (muted) {
      for (int i = 0; i < sampleFrames; i++)
        left[i] = right[i] = 0.0f;
      return;
    }

    // Update input data:
    handleMIDIEvents(events, eventCount);

    int note1 = currentNote + osc1coarse;
    if (note1 < 0)
      note1 = 1;
    else if (note1 > 127)
      note1 = 126;
    float freq1 = noteToFrequency[note1];
    if (osc1fine < -0.01) {
      float d = freq1 - noteToFrequency[note1 - 1];
      freq1 += d * osc1fine;
    }
    else if (osc1fine > 0.01) {
      float d = noteToFrequency[note1 + 1] - freq1;
      freq1 += d * osc1fine;
    }


    int note2 = currentNote + osc2coarse;
    if (note2 < 0)
      note2 = 0;
    else if (note2 > 127)
      note2 = 127;
    float freq2 = noteToFrequency[note2];
    if (osc2fine < -0.01) {
      float d = freq2 - noteToFrequency[note2 - 1];
      freq2 += d * osc2fine;
    }
    else if (osc2fine > 0.01) {
      float d = noteToFrequency[note2 + 1] - freq2;
      freq2 += d * osc2fine;
    }

    oscillator1.setFrequency(freq1);
    oscillator2.setFrequency(freq2);

    for (int i = 0; i < sampleFrames; i++) {

      // Get envelope and LFO values:
      float ampEnvelopeValue = ampEnvelope.tick();
      float lfo1Value = lfo1.tick();
      float lfo2Value = lfo2.tick();

      float envVal = ampEnvelopeValue * currentVelocity;

      //envVal *= (lfo1Buffer[i] + 1.0) * 0.5;

      // Evaluate oscillators:
      float osc1Val  = oscillator1.tick();
      float osc2Val  = oscillator2.tick();
      float noiseVal = 2.0f * (float)Math.random() - 1.0f;
      float ringVal  = osc1Val * osc2Val * 2.0f;

      // Clip ring modulated values:
      ringVal = ringVal - (ringVal * ringVal * ringVal) / 6.0f;

      // Mix oscillators:
      float oscVal = (osc1Val * osc1Volume.tick()) + (ringVal  * mixerDrive.tick()) +
                     (osc2Val * osc2Volume.tick()) + (noiseVal * noiseVolume.tick());

      // Apply amp envelope:
      oscVal *= envVal;

      // Apply filter:
      oscVal = filter.tick(oscVal);

      // Apply delay:
      oscVal = delay.tick(oscVal);

      // Apply master volume:
      oscVal *= masterVolume.tick();

      // Write out:
      left[i] =  oscVal;
      right[i] = left[i];
    }
  }

  /**
   * Internal helper function that decodes incoming MIDI events and update the synth accordingly. It
   * is also used to load preset values via loadPreset();
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
        currentVelocity = (float)events[i].value2 / 127.0f;

        // Start envelope:
        ampEnvelope.setGateOn();
      }

      // Control change
      else if (events[i].message == 0xB0) {

        // Update preset:
        currentBuffer.setValue(events[i].value1, events[i].value2);

        // Negative values mean empty events (during preset loading etc):
        if (events[i].value2 < 0)
          continue;

        // Scale value to range [0, 1]:
        float nrmVal = (float)events[i].value2 / 127.0f;

        // Check CC numbers:
        switch (events[i].value1) {

          case MIDIImplementation.CC_FILTER_CUTOFF:
            filter.setCutoff(Tweak.FILTER_MIN_CUTOFF + ((Tweak.FILTER_MAX_CUTOFF - Tweak.FILTER_MIN_CUTOFF) * nrmVal));
            break;

          case MIDIImplementation.CC_FILTER_MODE:
            if (events[i].value2 == 0)
              filter.setMode(ResonantFilter.Mode.LOWPASS);
            else if (events[i].value2 == 1)
              filter.setMode(ResonantFilter.Mode.HIGHPASS);
            else if (events[i].value2 == 2)
              filter.setMode(ResonantFilter.Mode.BANDPASS);
            break;

          case MIDIImplementation.CC_FILTER_SLOPE:
            if (events[i].value2 == 0)
              filter.setSlope(ResonantFilter.Slope.DB12);
            else if (events[i].value2 == 1)
              filter.setSlope(ResonantFilter.Slope.DB24);
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
              lfo1.setWaveForm(LFO.WaveForm.SAW);
            else if (events[i].value2 == 2)
              lfo1.setWaveForm(LFO.WaveForm.RECT);
            else if (events[i].value2 == 3)
              lfo1.setWaveForm(LFO.WaveForm.RANDOM);
            break;

          case MIDIImplementation.CC_LFO2_SPEED:
            lfo2.setFrequency(Tweak.LFO_MIN_SPEED + ((Tweak.LFO_MAX_SPEED - Tweak.LFO_MIN_SPEED) * nrmVal * nrmVal));
            break;

          case MIDIImplementation.CC_LFO2_WAVEFORM:
            if (events[i].value2 == 0)
              lfo2.setWaveForm(LFO.WaveForm.SINE);
            else if (events[i].value2 == 1)
              lfo2.setWaveForm(LFO.WaveForm.SAW);
            else if (events[i].value2 == 2)
              lfo2.setWaveForm(LFO.WaveForm.RECT);
            else if (events[i].value2 == 3)
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

          case MIDIImplementation.CC_OSC1_COARSE:
            osc1coarse = events[i].value2 - 64;
            break;

          case MIDIImplementation.CC_OSC1_FINE:
            osc1fine = (float)(events[i].value2 - 64) / 64.0f;
            break;

          case MIDIImplementation.CC_OSC1_PULSE:
            oscillator1.setPulseWidth(Tweak.OSC_MIN_PW + ((1.0f - Tweak.OSC_MIN_PW) * nrmVal));
            break;

          case MIDIImplementation.CC_OSC1_LEVEL:
            osc1Volume.setValue(nrmVal);
            break;

          case MIDIImplementation.CC_OSC2_WAVEFORM:
            if (events[i].value2 == 0)
              oscillator2.setWaveForm(MainOscillator.WaveForm.SINE);
            else if (events[i].value2 == 1)
              oscillator2.setWaveForm(MainOscillator.WaveForm.TRIANGLE);
            else if (events[i].value2 == 2)
              oscillator2.setWaveForm(MainOscillator.WaveForm.RECT);
            else if (events[i].value2 == 3)
              oscillator2.setWaveForm(MainOscillator.WaveForm.SAW);
            break;

          case MIDIImplementation.CC_OSC2_COARSE:
            osc2coarse = events[i].value2 - 64;
            break;

          case MIDIImplementation.CC_OSC2_FINE:
            osc2fine = (float)(events[i].value2 - 64) / 64.0f;
            break;

          case MIDIImplementation.CC_OSC2_PULSE:
            oscillator2.setPulseWidth(nrmVal);
            break;

          case MIDIImplementation.CC_OSC2_LEVEL:
            osc2Volume.setValue(nrmVal);
            break;

          case MIDIImplementation.CC_NOISE_LEVEL:
            noiseVolume.setValue(nrmVal);
            break;

          case MIDIImplementation.CC_MIXER_DRIVE:
            mixerDrive.setValue(nrmVal);
            break;

          case MIDIImplementation.CC_DELAY_ENABLE:
            delay.setEnabled(events[i].value2 != 0);
            break;

          case MIDIImplementation.CC_DELAY_TIME:
            delay.setTime(Tweak.DELAY_MIN_TIME + ((Tweak.DELAY_MAX_TIME - Tweak.DELAY_MIN_TIME) * nrmVal));
            break;

          case MIDIImplementation.CC_DELAY_FEEDBACK:
            delay.setFeedback(Tweak.DELAY_MAX_FEEDBACK * nrmVal);
            break;

          case MIDIImplementation.CC_DELAY_MIX:
            delay.setMix(nrmVal);
            break;

          default:
            break;
        }
      }
    }
  }

  /** Lookup table for MIDI note numbers. */
  private final float[] noteToFrequency = new float[127];

  /** Current sample rate for this synth in samples/second. */
  private int sampleRate = 44100;

  /** Current audio buffer size in samples. */
  private int bufferSize = 256;

  /** Currently played MIDI note. */
  private int currentNote = 64;

  /** Velocity value [0-1] of the current note. */
  private float currentVelocity = 1.0f;

  /** The amplifier envelope. */
  private final AHDSREnvelope ampEnvelope = new AHDSREnvelope();

  /** First LFO. */
  private final LFO lfo1 = new LFO();

  /** Second LFO. */
  private final LFO lfo2 = new LFO();

  /** Master volume of this synth. */
  private final SmoothParameter masterVolume = new SmoothParameter(0.75f);

  /** Mixer, oscillator 1 volume. */
  private final SmoothParameter osc1Volume = new SmoothParameter(1.0f);

  /** Mixer, oscillator 2 volume. */
  private final SmoothParameter osc2Volume = new SmoothParameter(1.0f);

  /** Mixer, noise oscillator volume. */
  private final SmoothParameter noiseVolume = new SmoothParameter(0.0f);

  /** Mixer drive. */
  private final SmoothParameter mixerDrive = new SmoothParameter(0.0f);

  /** The filter used in this synth. */
  private final ResonantFilter filter = new ResonantFilter();

  /** First main oscillator. */
  private final MainOscillator oscillator1 = new MainOscillator();

  /** Oscillator 1 coarse tuning (+-12 semitones). */
  private int osc1coarse = 0;

  /** Oscillator 1 fine tuning (+-1 semitone). */
  private float osc1fine = 0.0f;

  /** Oscillator 2 coarse tuning (+-12 semitones). */
  private int osc2coarse = 0;

  /** Oscillator 2 fine tuning (+-1 semitone). */
  private float osc2fine = 0.0f;

  /** Second main oscillator. */
  private final MainOscillator oscillator2 = new MainOscillator();

  /** The current synth state as MIDI preset. */
  private final DedoloxPreset currentBuffer = new DedoloxPreset();

  /** Is the synth currently muted? */
  private boolean muted = false;

  /** The delay effect. */
  private final Delay delay = new Delay();
}
