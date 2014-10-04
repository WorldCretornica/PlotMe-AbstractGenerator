package me.flungo.bukkit.plotme.abstractgenerator;

import com.worldcretornica.plotme_core.api.IBlock;

public class BlockRepresentation {

    private final Short id;
    private final Byte data;

    public BlockRepresentation(short id, byte value) {
        this.id = id;
        this.data = value;
    }

    public BlockRepresentation(String idvalue) {
        this(getBlockId(idvalue), getBlockData(idvalue));
    }

    public BlockRepresentation(IBlock block) {
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

    public boolean setBlock(IBlock b) {
        return setBlock(b, true);
    }

    public boolean setBlock(IBlock block, boolean applyPhysics) {
        return block.setTypeIdAndData(getId(), getData(), applyPhysics);
    }

}
