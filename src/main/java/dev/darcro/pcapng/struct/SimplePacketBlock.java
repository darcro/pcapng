package dev.darcro.pcapng.struct;

import dev.darcro.pcapng.PcapngReaderException;
import dev.darcro.pcapng.enums.BlockType;
import dev.darcro.pcapng.util.ByteStreamReader;

import java.util.Collections;

public class SimplePacketBlock extends AbstractPcapngBlock {

    public static final BlockType BLOCK_TYPE = BlockType.SIMPLE_PACKET_BLOCK;

    public final long originalLength;
    public final byte[] packetData;

    public SimplePacketBlock(long blockLength, long originalLength, byte[] packetData) {
        super(blockLength, Collections.emptyMap());
        this.originalLength = originalLength;
        this.packetData = packetData;
    }

    public static SimplePacketBlock parse(ByteStreamReader in) throws PcapngReaderException {
        if (in.readInt(4) != BLOCK_TYPE.id) {
            throw new PcapngReaderException("Invalid Block Type.");
        }

        final long blockLength = in.readLong(4);
        final long originalLength = in.readLong(4);

        final int derivedPacketLength = (int) (blockLength - 16);
        final byte[] packetData = in.readBytes(derivedPacketLength);

        in.skipBytes(4); //block length trailer

        return new SimplePacketBlock(blockLength, originalLength, packetData);
    }

    @Override
    public BlockType getBlockType() {
        return BLOCK_TYPE;
    }
}
