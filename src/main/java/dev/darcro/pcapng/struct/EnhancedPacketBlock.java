package dev.darcro.pcapng.struct;

import dev.darcro.pcapng.PcapngReaderException;
import dev.darcro.pcapng.enums.BlockType;
import dev.darcro.pcapng.util.ByteStreamReader;

import java.util.Map;

public class EnhancedPacketBlock extends AbstractPcapngBlock {

    public static final BlockType BLOCK_TYPE = BlockType.ENHANCED_PACKET_BLOCK;

    public final long interfaceId;
    public final long timeUnits;
    public final long capturedLength;
    public final long originalLength;
    public final byte[] packetData;

    public EnhancedPacketBlock(long blockLength, long interfaceId, long timeUnits,
                               long capturedLength, long originalLength,
                               byte[] packetData, Map<Integer, Option> options) {
        super(blockLength, options);
        this.interfaceId = interfaceId;
        this.timeUnits = timeUnits;
        this.capturedLength = capturedLength;
        this.originalLength = originalLength;
        this.packetData = packetData;
    }

    public static EnhancedPacketBlock parse(ByteStreamReader in) throws PcapngReaderException {
        if (in.readInt(4) != BLOCK_TYPE.id) {
            throw new PcapngReaderException("Invalid Block Type.");
        }

        final long blockLength = in.readLong(4);
        final long interfaceId = in.readLong(4);

        final long timeUnits = in.readLong(8);

        final long capturedLength = in.readLong(4);
        final long originalLength = in.readLong(4);

        final byte[] packetData = in.readBytes((int) capturedLength);

        final int offset = (int) (capturedLength % 4);
        final int padding = offset != 0 ? 4 - offset : 0;
        in.skipBytes(padding);

        final Map<Integer, Option> options = Option.parseOptions(in, (int) (blockLength - (32 + capturedLength + padding)));

        in.skipBytes(4); //block length trailer

        return new EnhancedPacketBlock(blockLength, interfaceId, timeUnits, capturedLength, originalLength, packetData, options);
    }

    @Override
    public BlockType getBlockType() {
        return BLOCK_TYPE;
    }
}
