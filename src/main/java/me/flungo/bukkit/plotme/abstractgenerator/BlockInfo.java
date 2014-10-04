package me.flungo.bukkit.plotme.abstractgenerator;

import com.worldcretornica.plotme_core.api.*;

public class BlockInfo {

    public final BlockRepresentation block;
    public final ILocation loc;

    public BlockInfo(BlockRepresentation block, ILocation loc) {
        this.block = block;
        this.loc = loc;
    }

    public BlockInfo(BlockRepresentation block, IWorld w, int x, int y, int z) {
        this(block, w.createLocation(x, y, z));
    }

    public BlockInfo(IBlock block) {
        this(new BlockRepresentation(block), block.getLocation());
    }

}
