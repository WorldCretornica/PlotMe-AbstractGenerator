package com.worldcretornica.plotme_abstractgenerator.bukkit;

import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.PLOT_SIZE;

import com.worldcretornica.plotme_abstractgenerator.WorldGenConfig;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;
import com.worldcretornica.schematic.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class BukkitAbstractGenManager implements IBukkitPlotMe_GeneratorManager {

    private final BukkitAbstractGenerator plugin;
    private final Map<String, WorldGenConfig> worldConfigs;

    public BukkitAbstractGenManager(BukkitAbstractGenerator instance) {
        plugin = instance;
        worldConfigs = new HashMap<>();
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

    public WorldGenConfig getWGC(World world) {
        return getWGC(world.getName());
    }

    public WorldGenConfig getWGC(String world) {
        return worldConfigs.get(world.toLowerCase());
    }

    public WorldGenConfig putWGC(String worldName, WorldGenConfig wgc) {
        return worldConfigs.put(worldName.toLowerCase(), wgc);
    }

    public boolean containsWGC(String worldName) {
        return worldConfigs.containsKey(worldName.toLowerCase());
    }

    @Override
    public int getPlotSize(String worldName) {
        if (getWGC(worldName) != null) {
            return getWGC(worldName).getInt(PLOT_SIZE);
        } else {
            plugin.getLogger().log(Level.WARNING, "Tried to get plot size for undefined world '{0}'", worldName);
            return 0;
        }
    }

    @Override
    public boolean createConfig(String worldName, Map<String, String> args) {
        WorldGenConfig wgc = plugin.getWorldGenConfig(worldName);

        for (String key : args.keySet()) {
            wgc.set(key, args.get(key));
        }

        return true;
    }

    @Override
    public int getRoadHeight(String worldName) {
        if (containsWGC(worldName)) {
            return getWGC(worldName).getInt(GROUND_LEVEL);
        } else {
            plugin.getLogger().log(Level.WARNING, "Tried to get road height for undefined world '{0}'", worldName);
            return 64;
        }
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

    @Override
    public void setBiome(World world, PlotId id, Biome biome) {
        int bottomX = bottomX(id, world) - 1;
        int topX = topX(id, world) + 1;
        int bottomZ = bottomZ(id, world) - 1;
        int topZ = topZ(id, world) + 1;

        for (int x = bottomX; x <= topX; x++) {
            for (int z = bottomZ; z <= topZ; z++) {
                world.getBlockAt(x, 0, z).setBiome(biome);
            }
        }

        refreshPlotChunks(world, id);
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
