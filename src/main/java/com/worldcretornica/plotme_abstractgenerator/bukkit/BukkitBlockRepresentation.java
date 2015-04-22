package com.worldcretornica.plotme_abstractgenerator.bukkit;

public class BukkitBlockRepresentation {

    private final short id;
    private final byte data;

    public BukkitBlockRepresentation(short id, byte value) {
        this.id = id;
        this.data = value;
    }

    public BukkitBlockRepresentation(String idValue) {
        this(getBlockId(idValue), getBlockData(idValue));
    }

    public static byte getBlockId(String idValue) throws NumberFormatException {
        if (idValue.indexOf(":") > 0) {
            return Byte.parseByte(idValue.split(":")[0]);
        } else {
            return Byte.parseByte(idValue);
        }
    }

    public static byte getBlockData(String idValue) throws NumberFormatException {
        if (idValue.indexOf(":") > 0) {
            return Byte.parseByte(idValue.split(":")[1]);
        } else {
            return 0;
        }
    }

    public short getId() {
        return id;
    }

    public byte getData() {
        return data;
    }

}
