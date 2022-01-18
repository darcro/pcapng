package dev.darcro.pcapng.util;

public class ByteStreamReader {
    private final byte[] buffer;
    private final int endIndex;

    private int position;
    private int mark;

    public ByteStreamReader(byte[] data) {
        this.buffer = data;
        this.position = 0;
        this.mark = -1;
        this.endIndex = data.length - 1;
    }

    public ByteStreamReader(byte[] data, int offset, int length) {
        this.buffer = data;
        this.position = offset;
        this.mark = -1;
        this.endIndex = offset + length - 1;
    }

    public ByteStreamReader subStream(int length) {
        if (available() < length) throw new IllegalArgumentException("Not enough data available in stream");
        final ByteStreamReader sub = new ByteStreamReader(buffer, position, length);
        position += length;
        return sub;
    }

    public byte[] readBytes(int length) {
        if (available() < length) throw new IllegalArgumentException("Not enough data available in stream");
        final byte[] val = new byte[length];
        System.arraycopy(buffer, position, val, 0, length);
        position += length;
        return val;
    }

    /**
     * Read the specified number of bytes as an {@code int}. Helpful for unsigned values.
     *
     * @param numBytes number of bytes to read as an integer
     * @return {@code int}
     */
    public int readInt(int numBytes) {
        if (available() < numBytes) throw new IllegalArgumentException("Not enough data available in stream");
        int val = 0;
        if(bigEndian) {
            for (int i = 0; i < numBytes; i++) {
                val = (val << 8) | (buffer[position++] & 0x00FF);
            }
        } else {
            for (int i = numBytes-1; i >= 0; i--) {
                val = (val << 8) | (buffer[position+i] & 0x00FF);
            }
            position += numBytes;
        }
        return val;
    }

    /**
     * Read the specified number of bytes as a {@code long}. Helpful for unsigned values.
     *
     * @param numBytes number of bytes to read as a long
     * @return {@code long}
     */
    public long readLong(int numBytes) {
        if (available() < numBytes) throw new IllegalArgumentException("Not enough data available in stream");
        long val = 0;
        if(bigEndian) {
            for (int i = 0; i < numBytes; i++) {
                val = (val << 8) | (buffer[position++] & 0x00FF);
            }
        } else {
            for (int i = numBytes-1; i >= 0; i--) {
                val = (val << 8) | (buffer[position+i] & 0x00FF);
            }
            position += numBytes;
        }
        return val;
    }

    public void mark() {
        if (mark != -1) throw new IllegalStateException("Mark already set");
        mark = position;
    }

    public void reset() {
        if (mark == -1) throw new IllegalStateException("No mark set");
        position = mark;
        mark = -1;
    }

    public byte peekByte() {
        return buffer[position];
    }

    public void skipBytes(int length) {
        if (available() < length) throw new IllegalArgumentException("Not enough data available in stream");
        position += length;
    }

    public int peekInt(int numBytes) {
        if (available() < numBytes) throw new IllegalArgumentException("Not enough data available in stream");
        final int pos = position;
        final int out = readInt(numBytes);
        this.position = pos;
        return out;
    }

    public long peekLong(int numBytes) {
        if (available() < numBytes) throw new IllegalArgumentException("Not enough data available in stream");
        final int pos = position;
        final long out = readLong(numBytes);
        this.position = pos;
        return out;
    }

    public int available() {
        return endIndex - position + 1;
    }

    public boolean eos() {
        return position > endIndex;
    }



    private boolean bigEndian = true;
    public void setBigEndian() {
        this.bigEndian = true;
    }
    public void setLittleEndian() {
        this.bigEndian = false;
    }
}
