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
 * @since   2014-12-12
 */

package de.matrix44.dedolox;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * This is the main audio engine of this app.
 *
 * It handles MIDI input, calculates the resulting audio and feeds the audio interface.
 */
class MainAudioThread extends Thread {

  /**
   * Default constructor of this class.
   */
  public MainAudioThread() {
  }

  static {
    System.loadLibrary("stlport_shared");
    System.loadLibrary("dedoloxNative");
  }

  private native void audioStart(int sampleRate, int bufferSize);

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
   * The buffer size is the number of samples that is sent to the audio hardware.
   *
   * @param bufferSize New buffer size.
   */
  public void setBufferSize(int bufferSize) {

    // Save buffer size:
    this.bufferSize = bufferSize;
  }

  /**
   * Send a note on message to the synth.
   *
   * @param channel    MIDI channel.
   * @param noteNumber Note number.
   * @param velocity   Velocity.
   */
  public void noteOn(int channel, int noteNumber, int velocity) {

    // Add as new event:
    addMidiEvent(channel, 0x90, noteNumber, velocity);
  }

  /**
   * Send a note off message to the synth.
   *
   * @param channel    MIDI channel.
   * @param noteNumber Note number.
   * @param velocity   Velocity.
   */
  public void noteOff(int channel, int noteNumber, int velocity) {

    // Add as new event:
    addMidiEvent(channel, 0x80, noteNumber, velocity);
  }

  /**
   * Send a polyphonic aftertouch message to the synth.
   *
   * @param channel    MIDI channel.
   * @param noteNumber Note number.
   * @param velocity   Aftertouch amount.
   */
  public void polyphonicAftertouch(int channel, int noteNumber, int velocity) {

    // Add as new event:
    addMidiEvent(channel, 0xA0, noteNumber, velocity);
  }

  /**
   * Send a control change message to the synth.
   *
   * @param channel       MIDI channel.
   * @param controlNumber Controller number.
   * @param controlValue  Controller value.
   */
  public void controlChange(int channel, int controlNumber, int controlValue) {

    // Add as new event:
    addMidiEvent(channel, 0xB0, controlNumber, controlValue);
  }

  /**
   * Send a program change message to the synth.
   *
   * @param channel       MIDI channel.
   * @param programNumber Program number.
   */
  public void programChange(int channel, int programNumber) {

    // Add as new event:
    addMidiEvent(channel, 0xC0, programNumber, 0);
  }

  /**
   * Send a channel aftertouch change message to the synth.
   *
   * @param channel  MIDI channel.
   * @param velocity Aftertouch amount.
   */
  public void channelAftertouch(int channel, int velocity) {

    // Add as new event:
    addMidiEvent(channel, 0xD0, velocity, 0);
  }

  /**
   * Send a pitch bend message to the synth.
   *
   * @param channel MIDI channel.
   * @param lowVal  MSB of the pitch value.
   * @param highVal LSB of the pitch value.
   */
  public void pitchBend(int channel, int lowVal, int highVal) {

    // Add as new event:
    addMidiEvent(channel, 0xE0, lowVal, highVal);
  }

  /**
   * The heart beat function of the audio engine.
   *
   * This function runs in a separate thread, calculates the output, handles MIDI messages and feeds
   * the audio hardware.
   */
  public void run() {

    // Only one thread at a time please:
    if (isRunning)
      return;
    isRunning = true;

    // Audio threads should have as much priority as possible:
    setPriority(Thread.MAX_PRIORITY);

    // Get the current native sample rate und minimal buffer size:
    sampleRate = AudioTrack.getNativeOutputSampleRate(AudioTrack.MODE_STREAM);
    bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT) / 4;

    // Create an AudioTrack object:
    AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSize  * 4, AudioTrack.MODE_STREAM);

    audioStart(sampleRate, bufferSize);

    // Create audio buffers:
    short[] samples = new short[bufferSize * 2];
    double[] bufferLeft = new double[bufferSize];
    double[] bufferRight = new double[bufferSize];

    // Create and init our synth:
    synth = new DedoloxSynth();
    synth.setBufferSize(bufferSize);
    synth.setSampleRate(sampleRate);

    // Start audio processing:
    audioTrack.play();

    // Run audio loop:
    while (isRunning) {

      // Prepare MIDI messages:
      int eventCount = 0;
      synchronized (inputBufferLock) {

        // Anything to do?
        if (inputBufferCursor >= 0) {

          // Save count for later use:
          eventCount = inputBufferCursor + 1;

          // Swap MIDI buffers:
          MIDIEvent[] tmp = inputBuffer;
          inputBuffer = workingBuffer;
          workingBuffer = tmp;

          // Reset count:
          inputBufferCursor = -1;
        }
      }

      // Check for buffer size changes and notify synth:
      if (synth.getBufferSize() != bufferSize)
        synth.setBufferSize(bufferSize);

      // Check for sample rate changes and notify synth:
      if (synth.getSampleRate() != sampleRate)
        synth.setSampleRate(sampleRate);

      // Do the actual audio processing:
      synth.process(bufferLeft, bufferRight, bufferSize, workingBuffer, eventCount);

      // Interlace data:
      for (int i = 0, t = 0; i < bufferSize; i++) {
        samples[t++] = (short)(32767.0 * bufferLeft[i]);
        samples[t++] = (short)(32767.0 * bufferRight[i]);
      }

      // Write to device:
      audioTrack.write(samples, 0, bufferSize * 2);
    }

    // Stop audio and free the device:
    audioTrack.stop();
    audioTrack.release();
  }

  /**
   * Stop the audio thread.
   *
   * This one should be called when the app closes so the audio hardware can be closed securely.
   */
  public void stopAudio() {

    // Update running state:
    isRunning = false;
  }

  public static void createAudioThread() {
    if (audioThread == null)
      audioThread = new MainAudioThread();
  }

  /**
   * Extract the current state of the synth as preset.
   *
   * @return A preset containing the state of the synth.
   */
  public DedoloxPreset getPreset() {
    if (synth != null)
      return synth.getPreset();
    return null;
  }

  /**
   * Set a new state from a preset.

   * @param preset The new state.
   */
  public void loadPreset(DedoloxPreset preset) {
    if (synth != null)
      synth.loadPreset(preset);
    else {
      MIDIEvent[] data = preset.getValues();
      for (int i = 0; i < data.length; i++)
        addMidiEvent(data[i].channel, data[i].message, data[i].value1, data[i].value2);
    }
  }

  public static MainAudioThread getAudioThread() {
    return audioThread;
  }

  public static void disposeAudioThread() {

    // Already disposed?
    if (audioThread == null)
      return;

    // Stop audio playback:
    try {
      audioThread.stopAudio();
      audioThread.join();
      audioThread = null;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a new MIDI message to the input queue.
   *
   * This will resize the buffers as needed.
   *
   * @param channel MIDI channel.
   * @param message The MIDI command.
   * @param value1  First parameter of the command
   * @param value2  Second parameter of the commands.
   */
  private void addMidiEvent(int channel, int message, int value1, int value2) {

    // Ignore messages in muted state:
    if (synth != null && synth.isMuted())
      return;

    // Lock buffers:
    synchronized (inputBufferLock) {

      // First allocation?
      if (inputBuffer == null) {

        // Start with 256 events:
        inputBuffer = new MIDIEvent[256];
        for (int i = 0; i < inputBuffer.length; i++)
          inputBuffer[i] = new MIDIEvent();
      }

      // Advance cursor:
      inputBufferCursor++;

      // Do we need more space?
      if (inputBufferCursor >= inputBuffer.length) {

        // Create new buffer:
        MIDIEvent[] newBuffer = new MIDIEvent[inputBuffer.length * 2];

        // Copy old contents:
        System.arraycopy(inputBuffer, 0, newBuffer, 0, inputBuffer.length);

        // Fill empty space:
        for (int i = inputBuffer.length; i < newBuffer.length; i++)
          newBuffer[i] = new MIDIEvent();

        // Swap buffers:
        inputBuffer = newBuffer;
      }

      // Insert event:
      inputBuffer[inputBufferCursor].channel = channel;
      inputBuffer[inputBufferCursor].message = message;
      inputBuffer[inputBufferCursor].value1  = value1;
      inputBuffer[inputBufferCursor].value2  = value2;
    }
  }

  /** Current sample rate of the audio engine. */
  private int sampleRate = 44100;

  /** Current block size of the audio engine. */
  private int bufferSize = 256;

  /** Is the audio engine thread still running? */
  private boolean isRunning = false;

  /** Current MIDI event input buffer of the audio engine. */
  private MIDIEvent[] inputBuffer = null;

  /** Current back MIDI event buffer, used for double buffering. */
  private MIDIEvent[] workingBuffer = null;

  /** Cursor for the MIDI event input buffer (= where to put the next event). */
  private int inputBufferCursor = -1;

  /** Thread synchronization object for the MIDI event input buffer. */
  private final Object inputBufferLock = new Object();

  /** The one and only audio thread. */
  private static MainAudioThread audioThread = null;

  /** The synth itself. */
  private DedoloxSynth synth = null;
}
