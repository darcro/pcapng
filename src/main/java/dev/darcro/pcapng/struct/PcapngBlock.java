package dev.darcro.pcapng.struct;

import dev.darcro.pcapng.enums.BlockType;

public interface PcapngBlock {

    /**
     * Block Total Length (32 bits): an unsigned value giving the total size of this block, in octets. For instance,
     * the length of a block that does not have a body is 12 octets. This value MUST be a multiple of 4.
     *
     * @return {@code long} Block Total Length
     */
    long getBlockLength();

    /**
     * Block Type (32 bits): a unique unsigned value that identifies the block.
     *
     * @return enum {@code BlockType} of the block's type
     */
    BlockType getBlockType();

    Option getOption(int code);
}
