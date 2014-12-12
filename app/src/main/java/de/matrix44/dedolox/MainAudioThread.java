package de.matrix44.dedolox;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

class MainAudioThread extends Thread {

    int sampleRate = 44100;
    int bufferSize = 256;
    boolean isRunning = true;
    double ph1 = 0.0;
    double ph2 = 0.0;

    public void stopAudio() {
        isRunning = false;
    }

    void audioLoop(double[] left, double[] right, int sampleFrames) {

        double gain = 0.75;
        double tau = Math.PI * 2.0;
        double fr1 = 440.f;
        double fr2 = 880.f;
        double d1 = tau * fr1 / sampleRate;
        double d2 = tau * fr2 / sampleRate;

        for (int i = 0, t = 0; i < bufferSize; i++) {
            left[i]  = Math.sin(ph1) * gain;
            right[i] = Math.sin(ph2) * gain;
            ph1 += d1;
            ph2 += d2;
        }
    }

    public void run() {

        // Audio threads should have as much priority as possible:
        setPriority(Thread.MAX_PRIORITY);

        // Get the current native sample rate und minimal buffer size:
        sampleRate = AudioTrack.getNativeOutputSampleRate(AudioTrack.MODE_STREAM);
        bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT) / 4;

        // Create an AudioTrack object:
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSize  * 4, AudioTrack.MODE_STREAM);

        short[] samples = new short[bufferSize * 2];
        double[] bufferLeft = new double[bufferSize];
        double[] bufferRight = new double[bufferSize];

        // start audio
        audioTrack.play();

        // synthesis loop
        while (isRunning) {

            // Do the audio processing:
            audioLoop(bufferLeft, bufferRight, bufferSize);

            // Interlace data:
            for (int i = 0, t = 0; i < bufferSize; i++) {
                samples[t++] = (short)(32767.0 * bufferLeft[i]);
                samples[t++] = (short)(32767.0 * bufferRight[i]);
            }

            // Write to device:
            audioTrack.write(samples, 0, bufferSize * 2);
        }

        audioTrack.stop();
        audioTrack.release();
    }
}
