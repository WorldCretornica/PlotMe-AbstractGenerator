package com.worldcretornica.plotme_abstractgenerator.bukkit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockInfo {

    public final BukkitBlockRepresentation block;
    public final Location loc;

    public BlockInfo(BukkitBlockRepresentation block, Location loc) {
        this.block = block;
        this.loc = loc;
    }

    public BlockInfo(BukkitBlockRepresentation block, World w, int x, int y, int z) {
        this(block, new Location(w, x, y, z));
    }

    public BlockInfo(Block block) {
        this(new BukkitBlockRepresentation(block), block.getLocation());
    }

}
