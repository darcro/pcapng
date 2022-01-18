package dev.darcro.pcapng.util;

import dev.darcro.pcapng.PcapngReaderException;

public class TimestampConverter {

    /**
     * Converts the timeUnits of an Enhanced Packet Block into milliseconds.
     * Not specifying the flag or power defaults to the assumed microsecond resolution.
     * @param timeUnits taken from an Enhanced Packet Block
     * @return millisecond representation of time units
     * @throws PcapngReaderException if timeUnits is too large for java to handle
     */
    public static long getMillisecond(long timeUnits) throws PcapngReaderException {
        // From Interface Description Block: "If this option is not present, a resolution of 10^-6 is assumed"
        return getMillisecond(timeUnits, false, 6);
    }

    /**
     * Converts the timeUnits of an Enhanced Packet Block into milliseconds.
     * The flag and power are specified in the Interface Description Block.
     * @param timeUnits taken from an Enhanced Packet Block
     * @param flag of if_tsresol for base 2 or 10
     * @param pow of if_tsresol as a power of the flag
     * @return millisecond representation of time units
     * @throws PcapngReaderException if timeUnits is too large for java to handle
     */
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

    /**
     * Converts the timeUnits of an Enhanced Packet Block into nanoseconds.
     * Not specifying the flag or power defaults to the assumed microsecond resolution.
     * @param timeUnits taken from an Enhanced Packet Block
     * @return nanosecond representation of time units
     * @throws PcapngReaderException if timeUnits is too large for java to handle
     */
    public static long getNanosecond(long timeUnits) throws PcapngReaderException {
        // From Interface Description Block: "If this option is not present, a resolution of 10^-6 is assumed"
        return getNanosecond(timeUnits, false, 6);
    }

    /**
     * Converts the timeUnits of an Enhanced Packet Block into nanoseconds.
     * The flag and power are specified in the Interface Description Block.
     * @param timeUnits taken from an Enhanced Packet Block
     * @param flag of if_tsresol for base 2 or 10
     * @param pow of if_tsresol as a power of the flag
     * @return nanosecond representation of time units
     * @throws PcapngReaderException if timeUnits is too large for java to handle
     */
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
