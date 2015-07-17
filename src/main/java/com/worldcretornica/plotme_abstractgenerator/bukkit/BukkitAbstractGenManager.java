package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.plotme_abstractgenerator.GeneratorManager;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import com.worldcretornica.schematic.Schematic;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public abstract class BukkitAbstractGenManager extends GeneratorManager implements IPlotMe_GeneratorManager {

    private final BukkitAbstractGenerator plugin;

    public BukkitAbstractGenManager(BukkitAbstractGenerator instance, ConfigurationSection wgc, IWorld world) {
        super(wgc, world);
        plugin = instance;
    }

    public void clearEntities(Vector bottom, Vector top) {
        if (world instanceof BukkitWorld) {
            BukkitWorld bukkitWorld = (BukkitWorld) world;
            for (Entity entity : bukkitWorld.getWorld().getEntities()) {
                org.bukkit.Location location = entity.getLocation();

                if (!entity.getType().equals(EntityType.PLAYER)) {
                    final int x = location.getBlockX();
                    final int z = location.getBlockZ();
                    if (x >= bottom.getBlockX() && x <= top.getBlockX()
                            && z >= bottom.getBlockZ() && z <= top.getBlockZ()) {
                        entity.remove();
                    }
                }
            }
        }
    }

    @Override
    public PlotId getPlotId(IPlayer player) {
        return getPlotId(player.getPosition());
    }

    @Override
    public List<IPlayer> getPlayersInPlot(PlotId id) {
        List<IPlayer> playersInPlot = new ArrayList<>();

        for (IPlayer p : plugin.plotMePlugin.getServerObjectBuilder().getOnlinePlayers()) {
            if (getPlotId(p).equals(id)) {
                playersInPlot.add(p);
            }
        }
        return playersInPlot;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void refreshPlotChunks(PlotId id) {
        double bottomX = bottomX(id);
        double topX = topX(id);
        double bottomZ = bottomZ(id);
        double topZ = topZ(id);

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
    public Vector getTop(PlotId id) {
        return getPlotTopLoc(id);
    }

    @Override
    public Vector getBottom(PlotId id) {
        return getPlotBottomLoc(id);
    }

    @Override
    public boolean isBlockInPlot(PlotId id, Vector location) {
        int lowestX = Math.min(bottomX(id), topX(id));
        int highestX = Math.max(bottomX(id), topX(id));
        int lowestZ = Math.min(bottomZ(id), topZ(id));
        int highestZ = Math.max(bottomZ(id), topZ(id));

        if (location.getBlockX() >= lowestX) {
            if (location.getBlockX() <= highestX) {
                if (location.getBlockZ() >= lowestZ) {
                    if (location.getBlockZ() <= highestZ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean movePlot(PlotId idFrom, PlotId idTo) {
        Vector plot1Bottom = getPlotBottomLoc(idFrom);
        Vector plot2Bottom = getPlotBottomLoc(idTo);
        Vector plot1Top = getPlotTopLoc(idFrom);
        Vector plot2Top = getPlotTopLoc(idTo);

        plot1Bottom.subtract(1, 0, 1);
        plot2Bottom.subtract(1, 0, 1);
        plot1Top.add(1, 0, 1);
        plot2Top.add(1, 0, 1);

        Schematic schem1 = plugin.getSchematicUtil().createCompiledSchematic(world, plot1Bottom, plot1Top);
        Schematic schem2 = plugin.getSchematicUtil().createCompiledSchematic(world, plot2Bottom, plot2Top);

        clearEntities(plot1Bottom, plot1Top);
        clearEntities(plot2Bottom, plot2Top);
        plugin.getSchematicUtil().pasteSchematic(world, plot1Bottom, schem2);
        plugin.getSchematicUtil().pasteSchematic(world, plot2Bottom, schem1);

        return true;
    }

    @Override
    public int bottomX(PlotId id) {
        return getPlotBottomLoc(id).getBlockX();
    }

    @Override
    public int bottomZ(PlotId id) {
        return getPlotBottomLoc(id).getBlockZ();
    }

    @Override
    public int topX(PlotId id) {
        return getPlotTopLoc(id).getBlockX();
    }

    @Override
    public int topZ(PlotId id) {
        return getPlotTopLoc(id).getBlockZ();
    }

    @Override
    public Schematic getPlotSchematic(PlotId id) {
        Vector plotBottom = getPlotBottomLoc(id);
        Vector plotTop = getPlotTopLoc(id);

        return plugin.getSchematicUtil().createCompiledSchematic(world, plotBottom, plotTop);
    }

    @Override public void setBiome(PlotId id, String biome) {
        int bottomX = bottomX(id) - 1;
        int topX = topX(id) + 1;
        int bottomZ = bottomZ(id) - 1;
        int topZ = topZ(id) + 1;
        Biome bukkitBiome = null;
        switch (biome) {
            case "Ocean":
                bukkitBiome = Biome.OCEAN;
                break;
            case "Plains":
                bukkitBiome = Biome.PLAINS;
                break;
            case "Desert":
                bukkitBiome = Biome.DESERT;
                break;
            case "Extreme Hills":
                bukkitBiome = Biome.EXTREME_HILLS;
                break;
            case "Forest":
                bukkitBiome = Biome.FOREST;
                break;
            case "MushroomIsland":
                bukkitBiome = Biome.MUSHROOM_ISLAND;
                break;
            case "MushroomIslandShore":
                bukkitBiome = Biome.MUSHROOM_SHORE;
                break;
            case "Tiaga":
                bukkitBiome = Biome.TAIGA;
                break;
            case "Swampland":
                bukkitBiome = Biome.SWAMPLAND;
                break;
            case "River":
                bukkitBiome = Biome.RIVER;
                break;
            case "Hell":
                bukkitBiome = Biome.HELL;
                break;
            case "The End":
                bukkitBiome = Biome.SKY;
                break;
            case "Cold Taiga Hills":
                bukkitBiome = Biome.COLD_TAIGA_HILLS;
                break;
            case "FrozenOcean":
                bukkitBiome = Biome.FROZEN_OCEAN;
                break;
            case "FrozenRiver":
                bukkitBiome = Biome.FROZEN_RIVER;
                break;
            case "Ice Plains":
                bukkitBiome = Biome.ICE_PLAINS;
                break;
            case "Ice Mountains":
                bukkitBiome = Biome.ICE_MOUNTAINS;
                break;
            case "Beach":
                bukkitBiome = Biome.BEACH;
                break;
            case "DesertHills":
                bukkitBiome = Biome.DESERT_HILLS;
                break;
            case "ForestHills":
                bukkitBiome = Biome.FOREST_HILLS;
                break;
            case "TiagaHills":
                bukkitBiome = Biome.TAIGA_HILLS;
                break;
            case "Jungle":
                bukkitBiome = Biome.JUNGLE;
                break;
            case "JungleHills":
                bukkitBiome = Biome.JUNGLE_HILLS;
                break;
            case "Deep Ocean":
                bukkitBiome = Biome.DEEP_OCEAN;
                break;
            case "Stone Beach":
                bukkitBiome = Biome.STONE_BEACH;
                break;
            case "Cold Beach":
                bukkitBiome = Biome.COLD_BEACH;
                break;
            case "Birch Forest":
                bukkitBiome = Biome.BIRCH_FOREST;
                break;
            case "Birch Forest Hills":
                bukkitBiome = Biome.BIRCH_FOREST_HILLS;
                break;
            case "Roofed Forest":
                bukkitBiome = Biome.ROOFED_FOREST;
                break;
            case "Cold Taiga":
                bukkitBiome = Biome.COLD_TAIGA;
                break;
            case "Mega Taiga":
                bukkitBiome = Biome.MEGA_TAIGA;
                break;
            case "Mega Taiga Hills":
                bukkitBiome = Biome.MEGA_TAIGA_HILLS;
                break;
            case "Extreme Hills+":
                bukkitBiome = Biome.EXTREME_HILLS_PLUS;
                break;
            case "Savanna":
                bukkitBiome = Biome.SAVANNA;
                break;
            case "Savanna Plateau":
                bukkitBiome = Biome.SAVANNA_PLATEAU;
                break;
            case "Mesa":
                bukkitBiome = Biome.MESA;
                break;
            case "Mesa Plateau F":
                bukkitBiome = Biome.MESA_PLATEAU_FOREST;
                break;
            case "Mesa Plateau":
                bukkitBiome = Biome.MESA_PLATEAU;
                break;
        }

        for (int x = bottomX; x <= topX; x++) {
            for (int z = bottomZ; z <= topZ; z++) {
                world.getBlockAt(x, 0, z).setBiome(bukkitBiome);
            }
        }

    }
}