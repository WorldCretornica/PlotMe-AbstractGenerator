package com.worldcretornica.plotme_abstractgenerator;

import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.PLOT_SIZE;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.configuration.ConfigurationSection;

public abstract class GeneratorManager {

    public final ConfigurationSection wgc;
    public final IWorld world;
    private final int plotSize;
    private final int height;

    public GeneratorManager(ConfigurationSection wgc, IWorld world) {
        this.wgc = wgc;
        this.world = world;
        plotSize = this.wgc.getInt(PLOT_SIZE.key(), 32);
        height = this.wgc.getInt(GROUND_LEVEL.key());
    }

    public static PlotId internalgetPlotId(int pathSize, int size, int posx, int posz) {

        int xmod = posx % size;
        int zmod = posz % size;

        // negative location
        if (xmod < 0) {
            xmod += size;
        }
        if (zmod < 0) {
            zmod += size;
        }

        // SouthEast plot corner
        int secorner = size - (int) Math.floor(pathSize / 2) - 1;
        // NorthWest plot corner
        int nwcorner;
        if (pathSize % 2 == 1) {
            nwcorner = (int) Math.ceil(pathSize / 2) + 1;
        } else {
            nwcorner = (int) Math.ceil(pathSize / 2);
        }

        // are we inside or outside the plot?
        if (nwcorner <= xmod && xmod <= secorner && nwcorner <= zmod && zmod <= secorner) {

            // Division needs to use floats.
            // Otherwise it converts the quotient to int, rendering Math.floor useless
            // If we use ints, we will end up with 4x 0;0 plots
            // adding 1 for backwards compatibility with old PlotMe versions
            double idx = 1 + Math.floor((float) posx / (float) size);
            double idz = 1 + Math.floor((float) posz / (float) size);
            //noinspection NumericCastThatLosesPrecision
            return new PlotId((int) idx, (int) idz);
        } else {
            // We hit the road, Jack!
            return null;
        }
    }

    public int getPlotSize() {
        return plotSize;
    }

    public int getGroundHeight() {
        return height;
    }
}
