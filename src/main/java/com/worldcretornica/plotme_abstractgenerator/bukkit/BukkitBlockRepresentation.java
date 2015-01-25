package com.worldcretornica.plotme_abstractgenerator.bukkit;

import org.bukkit.block.Block;

public class BukkitBlockRepresentation {

    private final Short id;
    private final Byte data;

    public BukkitBlockRepresentation(short id, byte value) {
        this.id = id;
        this.data = value;
    }

    public BukkitBlockRepresentation(String idValue) {
        this(getBlockId(idValue), getBlockData(idValue));
    }

    @SuppressWarnings("deprecation")
    public BukkitBlockRepresentation(Block block) {
        this((short) block.getTypeId(), block.getData());
    }

    public static short getBlockId(String idValue) throws NumberFormatException {
        if (idValue.indexOf(":") > 0) {
            return Short.parseShort(idValue.split(":")[0]);
        } else {
            return Short.parseShort(idValue);
        }
    }

    public static byte getBlockData(String idValue) throws NumberFormatException {
        if (idValue.indexOf(":") > 0) {
            return Byte.parseByte(idValue.split(":")[1]);
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
        return data == 0 ? id.toString() : id + ":" + data;
    }

    public boolean setBlock(Block b) {
        return setBlock(b, true);
    }

    @SuppressWarnings("deprecation")
    public boolean setBlock(Block block, boolean applyPhysics) {
        return block.setTypeIdAndData(getId(), getData(), applyPhysics);
    }

}
