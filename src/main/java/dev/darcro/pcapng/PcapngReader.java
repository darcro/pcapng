package dev.darcro.pcapng;

import dev.darcro.pcapng.enums.BlockType;
import dev.darcro.pcapng.struct.*;
import dev.darcro.pcapng.util.ByteStreamReader;
import dev.darcro.pcapng.util.TimestampConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class PcapngReader {

    private final ByteStreamReader inputStream;

    private SectionBlockHeader sectionBlockHeader;
    private int interfaceId;
    private HashMap<Integer, InterfaceDescriptionBlock> interfaceDescriptions;

    public PcapngReader(byte[] data) {
        inputStream = new ByteStreamReader(data);
    }

    public PcapngReader(String filePath) throws IOException {
        this(new File(filePath));
    }

    public PcapngReader(File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        inputStream = new ByteStreamReader(fis.readAllBytes());
    }

    private PcapngBlock nextBlock() throws PcapngReaderException {
        return switch (BlockType.fromId(inputStream.peekInt(4))) {
            case SECTION_BLOCK_HEADER -> SectionBlockHeader.parse(inputStream);
            case ENHANCED_PACKET_BLOCK -> EnhancedPacketBlock.parse(inputStream);
            case INTERFACE_DESCRIPTION_BLOCK -> InterfaceDescriptionBlock.parse(inputStream);
            default -> UnsupportedBlock.parse(inputStream);
        };
    }

    public PcapngPacket nextPacket() throws PcapngReaderException {
        while (!inputStream.eos()) {
            PcapngBlock block = nextBlock();
            switch (block.getBlockType()) {
                case SECTION_BLOCK_HEADER -> newSection((SectionBlockHeader) block);
                case INTERFACE_DESCRIPTION_BLOCK -> addInterface((InterfaceDescriptionBlock) block);
                case ENHANCED_PACKET_BLOCK -> {
                    return newPacket((EnhancedPacketBlock) block);
                }
                case SIMPLE_PACKET_BLOCK -> {
                    return newPacket((SimplePacketBlock) block);
                }
            }
        }
        return null;
    }

    public boolean eos() {
        return inputStream.eos();
    }

    private void newSection(SectionBlockHeader sbh) {
        this.sectionBlockHeader = sbh;
        this.interfaceDescriptions = new HashMap<>();
        this.interfaceId = 0;
    }

    private void addInterface(InterfaceDescriptionBlock idb) {
        interfaceDescriptions.put(interfaceId++, idb);
    }

    private PcapngPacket newPacket(EnhancedPacketBlock epb) throws PcapngReaderException {
        final InterfaceDescriptionBlock idb = interfaceDescriptions.get((int) epb.interfaceId);
        if (idb != null) {
            return new PcapngPacket(TimestampConverter.getMillisecond(epb.timeUnits, idb.tsFlag, idb.tsPower), epb.packetData);
        } else {
            return new PcapngPacket(TimestampConverter.getMillisecond(epb.timeUnits), epb.packetData);
        }
    }

    private PcapngPacket newPacket(SimplePacketBlock spb) {
        return new PcapngPacket(-1, spb.packetData);
    }
}
