package com.worldcretornica.plotme_abstractgenerator.bukkit;

import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.X_TRANSLATION;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.Z_TRANSLATION;

import com.worldcretornica.plotme_abstractgenerator.WorldGenConfig;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public abstract class BukkitAbstractChunkGenerator extends ChunkGenerator implements IBukkitPlotMe_ChunkGenerator {

    private final String worldname;
    private final BukkitAbstractGenerator plugin;

    public BukkitAbstractChunkGenerator(BukkitAbstractGenerator instance, String worldname) {
        plugin = instance;
        this.worldname = worldname;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        WorldGenConfig wgc = plugin.getGeneratorManager().getWGC(worldname);
        return new Location(world, wgc.getInt(X_TRANSLATION), wgc.getInt(GROUND_LEVEL) + 2, wgc.getInt(Z_TRANSLATION));
    }

    @Override
    public IBukkitPlotMe_GeneratorManager getManager() {
        return plugin.getGeneratorManager();
    }

    protected void setBlock(short[][] result, int x, int y, int z, short blockkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new short[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blockkid;
    }

}
