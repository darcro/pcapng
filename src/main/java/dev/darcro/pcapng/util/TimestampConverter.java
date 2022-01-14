package dev.darcro.pcapng.util;

import dev.darcro.pcapng.PcapngReaderException;

public class TimestampConverter {

    public static long getMillisecond(long timeUnits, boolean flag, int pow) throws PcapngReaderException {
        if (timeUnits < 0) {
            //the 64bit unsigned value is too big to work with a long
            throw new PcapngReaderException("Time Unit too big for primitives");
        }
        if (!flag) {
            if (pow == 3) return timeUnits; //already ms
            return (long) (timeUnits * Math.pow(10, 3 - pow));
        } else {
            final double seconds = timeUnits / Math.pow(2, pow);
            return (long) (seconds * 1000L);
        }
    }

    public static long getNanosecond(long timeUnits, boolean flag, int pow) throws PcapngReaderException {
        if (timeUnits < 0) {
            //the 64bit unsigned value is too big to work with a long
            throw new PcapngReaderException("Time Unit too big for primitives");
        }
        if (!flag) {
            if (pow == 9) return timeUnits; //already ns
            return (long) (timeUnits * Math.pow(10, 9 - pow));
        } else {
            final double seconds = timeUnits / Math.pow(2, pow);
            return (long) (seconds * 1000000000L);
        }
    }

}
