package dev.darcro.pcapng.struct;

import dev.darcro.pcapng.util.ByteStreamReader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Option {
    public final int type;
    public final int length;
    public final byte[] value;

    public Option(int type, int length, byte[] value) {
        this.type = type;
        this.length = length;
        this.value = value;
    }

    public static Option parse(ByteStreamReader reader) {
        int type = reader.readInt(2);
        int length = reader.readInt(2);
        byte[] val = reader.readBytes(length);

        // check for 32-bit padding
        final int offset = length % 4;
        final int padding = offset != 0 ? 4 - offset : 0;
        reader.skipBytes(padding);

        return new Option(type, length, val);
    }

    public static Map<Integer, Option> parseOptions(ByteStreamReader reader, int length) {
        if (length == 0) return Collections.emptyMap();

        final HashMap<Integer, Option> options = new HashMap<>();
        final ByteStreamReader optionStream = reader.subStream(length);

        while (!optionStream.eos()) {
            Option opt = parse(optionStream);
            options.put(opt.type, opt);
        }

        return options;
    }


}
