package dev.darcro.pcapng.struct;

import dev.darcro.pcapng.PcapngReaderException;
import dev.darcro.pcapng.enums.BlockType;
import dev.darcro.pcapng.util.ByteStreamReader;

import java.util.Map;

public class InterfaceDescriptionBlock extends AbstractPcapngBlock {

    public static final BlockType BLOCK_TYPE = BlockType.INTERFACE_DESCRIPTION_BLOCK;

    public final int linkType;
    public final long snapLength;

    public final boolean tsFlag; //indicates if_tsresol is a negative power of 2
    public final int tsPower; //magnitude of if_tsresol

    public InterfaceDescriptionBlock(long blockLength, int linkType, long snapLength, Map<Integer, Option> options) {
        super(blockLength, options);
        this.linkType = linkType;
        this.snapLength = snapLength;

        if (options.containsKey(9)) { //if_tsresol
            Option opt = options.get(9);
            tsFlag = (opt.value[0] & 0x80) == 0x00;
            tsPower = (opt.value[0] & 0x7F);
        } else {
            // If this option is not present, a resolution of 10^-6 is assumed
            tsFlag = false;
            tsPower = 6;
        }
    }

    public static InterfaceDescriptionBlock parse(ByteStreamReader in) throws PcapngReaderException {
        if (in.readInt(4) != BLOCK_TYPE.id) {
            throw new PcapngReaderException("Invalid Block Type.");
        }

        final long blockLength = in.readLong(4);
        final int linkType = in.readInt(2);
        in.skipBytes(2); //RESERVED
        final long snapLength = in.readLong(4);

        final Map<Integer, Option> options = Option.parseOptions(in, (int) (blockLength - 20));

        in.skipBytes(4); //block length trailer

        return new InterfaceDescriptionBlock(blockLength, linkType, snapLength, options);
    }

    @Override
    public long getBlockLength() {
        return blockLength;
    }

    @Override
    public BlockType getBlockType() {
        return BLOCK_TYPE;
    }

}
