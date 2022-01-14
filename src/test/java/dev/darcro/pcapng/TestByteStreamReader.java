package dev.darcro.pcapng;

import dev.darcro.pcapng.util.ByteStreamReader;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class TestByteStreamReader {


    @Test
    public void testReadLong() {
        byte[] data = {(byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte)0xFF};
        ByteStreamReader bs = new ByteStreamReader(data);
        bs.mark();
        assertEquals(0xFF00FF00L, bs.readLong(4));
        bs.reset();
        bs.mark();
        bs.skipBytes(1);
        assertEquals(0x00FF00FFL, bs.readLong(4));
        bs.reset();
        bs.mark();
        assertEquals(0xFF00FF00FFL, bs.readLong(5));
        bs.reset();
        bs.mark();
        assertEquals(0xFF00FF00FF00FF00L, bs.readLong(8));
        bs.reset();
        bs.mark();
        bs.skipBytes(1);
        assertEquals(0x00FF00FF00FF00FFL, bs.readLong(8));

        final Random r = new Random(System.currentTimeMillis());
        for(int i = 0; i < 5000; i++) {
            final long l = r.nextLong();
            final ByteStreamReader bsr = new ByteStreamReader(ByteBuffer.allocate(Long.BYTES).putLong(l).array());
            assertEquals(l, bsr.readLong(Long.BYTES));
        }
    }

    @Test
    public void testReadInt() {
        byte[] data = {(byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte) 0xFF, 0x00, (byte)0xFF};
        ByteStreamReader bs = new ByteStreamReader(data);
        bs.mark();
        assertEquals(0xFF00L, bs.readInt(2));
        bs.reset();
        bs.mark();
        bs.skipBytes(1);
        assertEquals(0x00FF, bs.readInt(2));
        bs.reset();
        bs.mark();
        assertEquals(0xFF00FF, bs.readInt(3));
        bs.reset();
        bs.mark();
        assertEquals(0xFF00FF00, bs.readInt(4));
        bs.reset();
        bs.mark();
        bs.skipBytes(1);
        assertEquals(0x00FF00FF, bs.readInt(4));

        final Random r = new Random(System.currentTimeMillis());
        for(int i = 0; i < 5000; i++) {
            final int n = r.nextInt();
            final ByteStreamReader bsr = new ByteStreamReader(ByteBuffer.allocate(Integer.BYTES).putInt(n).array());
            assertEquals(n, bsr.readInt(Integer.BYTES));
        }
    }



}
