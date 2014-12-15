package de.matrix44.dedolox;

public final class MIDIEvent {

    public int channel = 0;
    public int message = 0;
    public int value1 = 0;
    public int value2 = 0;

    public MIDIEvent() {
    }

    public MIDIEvent(int channel, int message, int value1, int value2) {
        this.channel = channel;
        this.message = message;
        this.value1  = value1;
        this.value2  = value2;
    }

    public MIDIEvent(int[] values) {
        if (values == null || values.length < 3)
            return;
        this.message = values[0] & 0xF0;
        this.channel = values[0] & 0x0F;
        this.value1  = values[1] & 0x7F;
        this.value2  = values[2] & 0x7F;
    }
}
