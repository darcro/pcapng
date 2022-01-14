package dev.darcro.pcapng;

import dev.darcro.pcapng.util.TimestampConverter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestTimestampConverter {

    @Test
    public void testMillisecond10() throws PcapngReaderException {
        assertEquals(10000L, TimestampConverter.getMillisecond(10, false, 0)); //10s
        assertEquals(1000L, TimestampConverter.getMillisecond(1, false, 0)); //1s
        assertEquals(100L, TimestampConverter.getMillisecond(100, false, 3)); //100ms
        assertEquals(2000000L, TimestampConverter.getMillisecond(2000000, false, 3)); //2000000ms
        assertEquals(0L, TimestampConverter.getMillisecond(2000, false, 9)); //2000ns
        assertEquals(2000L, TimestampConverter.getMillisecond(2000000, false, 6)); //2000000us
    }

    @Test
    public void testNanosecond10() throws PcapngReaderException {
        assertEquals(10000000000L, TimestampConverter.getNanosecond(10, false, 0)); //10s
        assertEquals(1000000000L, TimestampConverter.getNanosecond(1, false, 0)); //1s
        assertEquals(100000000L, TimestampConverter.getNanosecond(100, false, 3)); //100ms
        assertEquals(2000000000000L, TimestampConverter.getNanosecond(2000000, false, 3)); //2000000ms
        assertEquals(2000L, TimestampConverter.getNanosecond(2000, false, 9)); //2000ns
        assertEquals(2000000000L, TimestampConverter.getNanosecond(2000000, false, 6)); //2000000us
    }

    @Test
    public void testMillisecond2() throws PcapngReaderException {
        assertEquals(1000L, TimestampConverter.getMillisecond(2, true, 1));
        assertEquals(2000L, TimestampConverter.getMillisecond(4, true, 1));
        assertEquals(1000L, TimestampConverter.getMillisecond(4, true, 2));
        assertEquals(1000L, TimestampConverter.getMillisecond(8, true, 3));
        assertEquals(1000L, TimestampConverter.getMillisecond(16, true, 4));
        assertEquals(1000L, TimestampConverter.getMillisecond(4294967296L, true, 32));
        assertEquals(2000L, TimestampConverter.getMillisecond(4294967296L, true, 31));
    }

    @Test
    public void testNanosecond2() throws PcapngReaderException {
        assertEquals(1000000000L, TimestampConverter.getNanosecond(2, true, 1));
        assertEquals(2000000000L, TimestampConverter.getNanosecond(4, true, 1));
        assertEquals(1000000000L, TimestampConverter.getNanosecond(4, true, 2));
        assertEquals(1000000000L, TimestampConverter.getNanosecond(8, true, 3));
        assertEquals(1000000000L, TimestampConverter.getNanosecond(16, true, 4));
        assertEquals(1000000000L, TimestampConverter.getNanosecond(4294967296L, true, 32));
        assertEquals(2000000000L, TimestampConverter.getNanosecond(4294967296L, true, 31));
    }

}
