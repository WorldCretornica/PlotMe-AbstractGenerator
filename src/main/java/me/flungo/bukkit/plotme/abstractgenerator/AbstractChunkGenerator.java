/*
 * Copyright (C) 2013 Fabrizio Lungo <fab@lungo.co.uk> - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Fabrizio Lungo <fab@lungo.co.uk>, November 2013
 */
package me.flungo.bukkit.plotme.abstractgenerator;

import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_GeneratorManager;
import java.util.Random;
import static me.flungo.bukkit.plotme.abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static me.flungo.bukkit.plotme.abstractgenerator.AbstractWorldConfigPath.X_TRANSLATION;
import static me.flungo.bukkit.plotme.abstractgenerator.AbstractWorldConfigPath.Z_TRANSLATION;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

/**
 *
 * @author Fabrizio Lungo <fab@lungo.co.uk>
 */
public abstract class AbstractChunkGenerator extends ChunkGenerator implements IPlotMe_ChunkGenerator {

    private final String worldname;
    private final AbstractGenerator plugin;

    public AbstractChunkGenerator(AbstractGenerator instance, String worldname) {
        plugin = instance;
        this.worldname = worldname;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        WorldGenConfig wgc = plugin.getGeneratorManager().getWGC(worldname);
        return new Location(world, wgc.getInt(X_TRANSLATION), wgc.getInt(GROUND_LEVEL) + 2, wgc.getInt(Z_TRANSLATION));
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
