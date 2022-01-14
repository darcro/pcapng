package dev.darcro.pcapng.struct;

import dev.darcro.pcapng.enums.BlockType;
import dev.darcro.pcapng.util.ByteStreamReader;

import java.util.Collections;

public class UnsupportedBlock extends AbstractPcapngBlock {

    public static final BlockType BLOCK_TYPE = BlockType.UNKNOWN_OR_UNSUPPORTED;

    public final int type;
    public final long blockLength;

    public UnsupportedBlock(int type, long blockLength) {
        super(blockLength, Collections.emptyMap());
        this.type = type;
        this.blockLength = blockLength;
    }

    public static UnsupportedBlock parse(ByteStreamReader in) {
        final int type = in.readInt(4);
        final long blockLength = in.readLong(4);
        in.skipBytes((int) (blockLength - 8));
        return new UnsupportedBlock(type, blockLength);
    }

    @Override
    public BlockType getBlockType() {
        return BLOCK_TYPE;
    }
}
