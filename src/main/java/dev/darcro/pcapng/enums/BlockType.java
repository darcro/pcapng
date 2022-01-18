package dev.darcro.pcapng.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum BlockType {

    SECTION_BLOCK_HEADER(0x0A0D0D0A),
    INTERFACE_DESCRIPTION_BLOCK(0x00000001),
    SIMPLE_PACKET_BLOCK(0x00000003),
    ENHANCED_PACKET_BLOCK(0x00000006),
    UNKNOWN_OR_UNSUPPORTED(0xFFFFFFFF);

    public final int id;

    BlockType(int id) {
        this.id = id;
    }

    private static final Map<Integer, BlockType> lookupMap =
            Arrays.stream(BlockType.values())
                    .collect(Collectors.toMap(v -> v.id, v -> v));

    public static BlockType fromId(int id) {
        return lookupMap.getOrDefault(id, UNKNOWN_OR_UNSUPPORTED);
    }
}
