package dev.darcro.pcapng.struct;

import dev.darcro.pcapng.PcapngReaderException;
import dev.darcro.pcapng.enums.BlockType;
import dev.darcro.pcapng.util.ByteStreamReader;

import java.util.Map;

public class SectionBlockHeader extends AbstractPcapngBlock {

    public static final BlockType BLOCK_TYPE = BlockType.SECTION_BLOCK_HEADER;

    /**
     * Byte-Order Magic (32 bits): an unsigned magic number, whose value is the hexadecimal number 0x1A2B3C4D.
     * This number can be used to distinguish sections that have been saved on little-endian machines from the
     * ones saved on big-endian machines
     */
    public final int byteOrderMagic;

    /**
     * Section Length (64 bits): a signed value specifying the length in octets of the following section, excluding
     * the Section Header Block itself.
     */
    public final long sectionLength;

    public final boolean lengthUnspecified;


    public SectionBlockHeader(long blockLength, int byteOrderMagic, long sectionLength, Map<Integer, Option> options) {
        super(blockLength, options);
        this.byteOrderMagic = byteOrderMagic;
        this.sectionLength = sectionLength;
        this.lengthUnspecified = sectionLength == 0xFFFFFFFFFFFFFFFFL;
    }


    public static SectionBlockHeader parse(ByteStreamReader in) throws PcapngReaderException {
        if (in.readInt(4) != BLOCK_TYPE.id) {
            throw new PcapngReaderException("Data does not begin with a valid Section Block Header");
        }

        final long blockLength = in.readLong(4);

        final int magic = in.readInt(4);
        if (magic != 0x1A2B3C4D) {
            throw new PcapngReaderException("Endianness of this data is not supported");
        }

        final int major = in.readInt(2);
        final int minor = in.readInt(2);

        if (major != 1 || minor != 0) {
            throw new PcapngReaderException("Version mismatch. Expected 1.0; got " + major + "." + minor);
        }

        final long sectionLength = in.readLong(8);

        final Map<Integer, Option> options = Option.parseOptions(in, (int) (blockLength - 28));

        in.skipBytes(4); //block length trailer

        return new SectionBlockHeader(blockLength, magic, sectionLength, options);
    }

    @Override
    public BlockType getBlockType() {
        return BLOCK_TYPE;
    }

}
