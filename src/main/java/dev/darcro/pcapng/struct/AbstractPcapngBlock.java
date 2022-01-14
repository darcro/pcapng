package dev.darcro.pcapng.struct;

import java.util.Map;

public abstract class AbstractPcapngBlock implements PcapngBlock {

    public final long blockLength;
    private final Map<Integer, Option> optionMap;

    protected AbstractPcapngBlock(long blockLength, Map<Integer, Option> optionMap) {
        this.blockLength = blockLength;
        this.optionMap = optionMap;
    }

    @Override
    public long getBlockLength() {
        return blockLength;
    }

    @Override
    public Option getOption(int id) {
        return optionMap.get(id);
    }

}
