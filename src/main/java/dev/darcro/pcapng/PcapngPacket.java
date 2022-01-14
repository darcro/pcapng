package dev.darcro.pcapng;

public class PcapngPacket {

    public final long timestamp;
    public final byte[] dat;

    public PcapngPacket(long timestamp, byte[] dat) {
        this.timestamp = timestamp;
        this.dat = dat;
    }

}
