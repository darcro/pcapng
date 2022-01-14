package dev.darcro.pcapng.util;

public class ByteUtil {
    public static long getLong(byte[] data, int offset, int numBytes) {
        if (numBytes > data.length-offset) throw new IllegalArgumentException("Not enough bytes available");
        long val = 0;
        for (int i = 0; i < numBytes; i++) {
            val = (val << 8) | (data[offset + i++] & 0x00FF);
        }
        return val;
    }
}
