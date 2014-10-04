package me.flungo.bukkit.plotme.abstractgenerator;

import java.util.Random;

import com.worldcretornica.plotme_core.api.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.api.*;

import static me.flungo.bukkit.plotme.abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static me.flungo.bukkit.plotme.abstractgenerator.AbstractWorldConfigPath.X_TRANSLATION;
import static me.flungo.bukkit.plotme.abstractgenerator.AbstractWorldConfigPath.Z_TRANSLATION;

public abstract class AbstractChunkGenerator implements IPlotMe_ChunkGenerator {

    private final String worldname;
    private final AbstractGenerator plugin;

    public AbstractChunkGenerator(AbstractGenerator instance, String worldname) {
        plugin = instance;
        this.worldname = worldname;
    }

    @Override
    public ILocation getFixedSpawnLocation(IWorld world, Random random) {
        WorldGenConfig wgc = plugin.getGeneratorManager().getWGC(worldname);
        return world.createLocation(wgc.getInt(X_TRANSLATION), wgc.getInt(GROUND_LEVEL) + 2, wgc.getInt(Z_TRANSLATION));
    }

    @Override
    public IPlotMe_GeneratorManager getManager() {
        return plugin.getGeneratorManager();
    }

    protected void setBlock(short[][] result, int x, int y, int z, short blockkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new short[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blockkid;
    }

}
