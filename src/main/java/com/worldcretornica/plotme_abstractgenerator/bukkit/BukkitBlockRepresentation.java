package com.worldcretornica.plotme_abstractgenerator.bukkit;

import org.bukkit.block.Block;

public class BukkitBlockRepresentation {

    private final Short id;
    private final Byte data;

    public BukkitBlockRepresentation(short id, byte value) {
        this.id = id;
        this.data = value;
    }

    public BukkitBlockRepresentation(String idvalue) {
        this(getBlockId(idvalue), getBlockData(idvalue));
    }

    @SuppressWarnings("deprecation")
    public BukkitBlockRepresentation(Block block) {
        this((short) block.getTypeId(), block.getData());
    }

    public static short getBlockId(String idvalue) throws NumberFormatException {
        if (idvalue.indexOf(":") > 0) {
            return Short.parseShort(idvalue.split(":")[0]);
        } else {
            return Short.parseShort(idvalue);
        }
    }

    public static byte getBlockData(String idvalue) throws NumberFormatException {
        if (idvalue.indexOf(":") > 0) {
            return Byte.parseByte(idvalue.split(":")[1]);
        } else {
            return 0;
        }
    }

    public Short getId() {
        return id;
    }

    public Byte getData() {
        return data;
    }

    public String getBlockIdValue() {
        return (data == 0) ? id.toString() : id.toString() + ":" + data.toString();
    }

    public boolean setBlock(Block b) {
        return setBlock(b, true);
    }

    @SuppressWarnings("deprecation")
    public boolean setBlock(Block block, boolean applyPhysics) {
        return block.setTypeIdAndData(getId(), getData(), applyPhysics);
    }

}
