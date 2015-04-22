package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.plotme_abstractgenerator.GeneratorManager;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IChunk;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.schematic.Schematic;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public abstract class BukkitAbstractGenManager extends GeneratorManager implements IPlotMe_GeneratorManager {

    private final BukkitAbstractGenerator plugin;

    public BukkitAbstractGenManager(BukkitAbstractGenerator instance, ConfigurationSection wgc, IWorld world) {
        super(wgc, world);
        plugin = instance;
    }

    public void clearEntities(Vector bottom, Vector top) {
        double bottomX = bottom.getBlockX();
        double topX = top.getBlockX();
        double bottomZ = bottom.getBlockZ();
        double topZ = top.getBlockZ();

        int minChunkX = (int) Math.floor(bottomX / 16);
        int maxChunkX = (int) Math.floor(topX / 16);
        int minChunkZ = (int) Math.floor(bottomZ / 16);
        int maxChunkZ = (int) Math.floor(topZ / 16);
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                IChunk chunk = world.getChunkAt(cx, cz);
                for (IEntity entity : chunk.getEntities()) {
                    ILocation location = entity.getLocation();

                    if (!(entity instanceof IPlayer)) {
                        if (location.getBlockX() >= bottom.getBlockX() && location.getBlockX() <= top.getBlockX()
                                && location.getBlockZ() >= bottom.getBlockZ() && location.getBlockZ() <= top.getBlockZ()) {
                            entity.remove();
                        }
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
    public long[] clear(PlotId id, long maxBlocks, long[] start) {
        return clear(getBottom(id), getTop(id), maxBlocks, start);
    }

    @Override
    public boolean isBlockInPlot(PlotId id, Vector location) {
        int lowestX = Math.min(bottomX(id), topX(id));
        int highestX = Math.max(bottomX(id), topX(id));
        int lowestZ = Math.min(bottomZ(id), topZ(id));
        int highestZ = Math.max(bottomZ(id), topZ(id));

        return location.getBlockX() >= lowestX && location.getBlockX() <= highestX
                && location.getBlockZ() >= lowestZ && location.getBlockZ() <= highestZ;
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
}