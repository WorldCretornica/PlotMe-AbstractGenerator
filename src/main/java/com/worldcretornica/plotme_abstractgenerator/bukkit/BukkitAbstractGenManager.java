package com.worldcretornica.plotme_abstractgenerator.bukkit;

import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.PLOT_SIZE;

import com.worldcretornica.configuration.ConfigurationSection;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;
import com.worldcretornica.schematic.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BukkitAbstractGenManager implements IBukkitPlotMe_GeneratorManager {

    public final ConfigurationSection wgc;
    private final BukkitAbstractGenerator plugin;

    public BukkitAbstractGenManager(BukkitAbstractGenerator instance, ConfigurationSection wgc) {
        plugin = instance;
        this.wgc = wgc;
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
            nwcorner = (int) Math.ceil(pathSize / 2) + 0;
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

    public void clearEntities(Location bottom, Location top) {
        double bottomX = bottom.getBlockX();
        double topX = top.getBlockX();
        double bottomZ = bottom.getBlockZ();
        double topZ = top.getBlockZ();

        World world = bottom.getWorld();

        int minChunkX = (int) Math.floor(bottomX / 16);
        int maxChunkX = (int) Math.floor(topX / 16);
        int minChunkZ = (int) Math.floor(bottomZ / 16);
        int maxChunkZ = (int) Math.floor(topZ / 16);

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                Chunk chunk = world.getChunkAt(cx, cz);

                for (Entity entity : chunk.getEntities()) {
                    Location location = entity.getLocation();

                    if (!(entity instanceof Player) && location.getBlockX() >= bottom.getBlockX() && location.getBlockX() <= top.getBlockX()
                            && location.getBlockZ() >= bottom.getBlockZ() && location.getBlockZ() <= top.getBlockZ()) {
                        entity.remove();
                    }
                }
            }
        }
    }

    @Override
    public int getPlotSize() {
        return wgc.getInt(PLOT_SIZE.key(), 32);
    }

    @Override
    public int getRoadHeight() {
        return wgc.getInt(GROUND_LEVEL.key(), 64);
    }

    @Override
    public PlotId getPlotId(Player player) {
        return getPlotId(player.getLocation());
    }

    @Override
    public List<Player> getPlayersInPlot(PlotId id) {
        List<Player> playersInPlot = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (getPlotId(p).equals(id)) {
                playersInPlot.add(p);
            }
        }
        return playersInPlot;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void refreshPlotChunks(World world, PlotId id) {
        double bottomX = bottomX(id, world);
        double topX = topX(id, world);
        double bottomZ = bottomZ(id, world);
        double topZ = topZ(id, world);

        int minChunkX = (int) Math.floor(bottomX / 16);
        int maxChunkX = (int) Math.floor(topX / 16);
        int minChunkZ = (int) Math.floor(bottomZ / 16);
        int maxChunkZ = (int) Math.floor(topZ / 16);

        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                world.refreshChunk(x, z);
            }
        }
    }

    @Override
    public Location getTop(World world, PlotId id) {
        return getPlotTopLoc(world, id);
    }

    @Override
    public Location getBottom(World world, PlotId id) {
        return getPlotBottomLoc(world, id);
    }

    @Override
    public Long[] clear(World world, PlotId id, long maxBlocks, Long[] start) {
        return clear(getBottom(world, id), getTop(world, id), maxBlocks, start);
    }

    @Override
    public boolean isBlockInPlot(PlotId id, Location location) {
        World world = location.getWorld();
        int lowestX = Math.min(bottomX(id, world), topX(id, world));
        int highestX = Math.max(bottomX(id, world), topX(id, world));
        int lowestZ = Math.min(bottomZ(id, world), topZ(id, world));
        int highestZ = Math.max(bottomZ(id, world), topZ(id, world));

        return location.getBlockX() >= lowestX && location.getBlockX() <= highestX
                && location.getBlockZ() >= lowestZ && location.getBlockZ() <= highestZ;
    }

    @Override
    public boolean movePlot(World world, PlotId idFrom, PlotId idTo) {
        Location plot1Bottom = getPlotBottomLoc(world, idFrom);
        Location plot2Bottom = getPlotBottomLoc(world, idTo);
        Location plot1Top = getPlotTopLoc(world, idFrom);
        Location plot2Top = getPlotTopLoc(world, idTo);

        plot1Bottom.subtract(1, 0, 1);
        plot2Bottom.subtract(1, 0, 1);
        plot1Top.add(1, 0, 1);
        plot2Top.add(1, 0, 1);

        Schematic schem1 = plugin.getSchematicUtil().createCompiledSchematic(plot1Bottom, plot1Top);
        Schematic schem2 = plugin.getSchematicUtil().createCompiledSchematic(plot2Bottom, plot2Top);

        clearEntities(plot1Bottom, plot1Top);
        clearEntities(plot2Bottom, plot2Top);

        plugin.getSchematicUtil().pasteSchematic(plot1Bottom, schem2);
        plugin.getSchematicUtil().pasteSchematic(plot2Bottom, schem1);

        return true;
    }

    @Override
    public int bottomX(PlotId id, World world) {
        return getPlotBottomLoc(world, id).getBlockX();
    }

    @Override
    public int bottomZ(PlotId id, World world) {
        return getPlotBottomLoc(world, id).getBlockZ();
    }

    @Override
    public int topX(PlotId id, World world) {
        return getPlotTopLoc(world, id).getBlockX();
    }

    @Override
    public int topZ(PlotId id, World world) {
        return getPlotTopLoc(world, id).getBlockZ();
    }

    @Override
    public Schematic getPlotSchematic(World world, PlotId id) {
        Location plotBottom = getPlotBottomLoc(world, id);
        Location plotTop = getPlotTopLoc(world, id);

        return plugin.getSchematicUtil().createCompiledSchematic(plotBottom, plotTop);
    }
}
