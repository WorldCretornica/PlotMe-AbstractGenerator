/*
 * Copyright (C) 2013 Fabrizio Lungo <fab@lungo.co.uk> - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Fabrizio Lungo <fab@lungo.co.uk>, November 2013
 */
package com.worldcretornica.plotme_abstractgenerator;

import com.worldcretornica.plotme_core.api.*;

import java.util.*;
import java.util.logging.Level;

import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.PLOT_SIZE;

/**
 * @author Fabrizio Lungo <fab@lungo.co.uk>
 */
public abstract class AbstractGenManager implements IPlotMe_GeneratorManager {

    // List of blocks that should be placed last in world generation
    protected static final Set<Integer> blockPlacedLast = new HashSet<>();

    private AbstractGenerator plugin = null;
    private final Map<String, WorldGenConfig> worldConfigs;
    protected IServerObjectBuilder sob;

    public AbstractGenManager(AbstractGenerator instance, IServerObjectBuilder sob) {
        plugin = instance;
        worldConfigs = new HashMap<>();
        this.sob = sob;
        
        blockPlacedLast.add(sob.getMaterial("SAPLING").getId());
        blockPlacedLast.add(sob.getMaterial("BED").getId());
        blockPlacedLast.add(sob.getMaterial("POWERED_RAIL").getId());
        blockPlacedLast.add(sob.getMaterial("DETECTOR_RAIL").getId());
        blockPlacedLast.add(sob.getMaterial("LONG_GRASS").getId());
        blockPlacedLast.add(sob.getMaterial("DEAD_BUSH").getId());
        blockPlacedLast.add(sob.getMaterial("PISTON_EXTENSION").getId());
        blockPlacedLast.add(sob.getMaterial("YELLOW_FLOWER").getId());
        blockPlacedLast.add(sob.getMaterial("RED_ROSE").getId());
        blockPlacedLast.add(sob.getMaterial("BROWN_MUSHROOM").getId());
        blockPlacedLast.add(sob.getMaterial("RED_MUSHROOM").getId());
        blockPlacedLast.add(sob.getMaterial("TORCH").getId());
        blockPlacedLast.add(sob.getMaterial("FIRE").getId());
        blockPlacedLast.add(sob.getMaterial("REDSTONE_WIRE").getId());
        blockPlacedLast.add(sob.getMaterial("CROPS").getId());
        blockPlacedLast.add(sob.getMaterial("LADDER").getId());
        blockPlacedLast.add(sob.getMaterial("RAILS").getId());
        blockPlacedLast.add(sob.getMaterial("LEVER").getId());
        blockPlacedLast.add(sob.getMaterial("STONE_PLATE").getId());
        blockPlacedLast.add(sob.getMaterial("WOOD_PLATE").getId());
        blockPlacedLast.add(sob.getMaterial("REDSTONE_TORCH_OFF").getId());
        blockPlacedLast.add(sob.getMaterial("REDSTONE_TORCH_ON").getId());
        blockPlacedLast.add(sob.getMaterial("STONE_BUTTON").getId());
        blockPlacedLast.add(sob.getMaterial("SNOW").getId());
        blockPlacedLast.add(sob.getMaterial("PORTAL").getId());
        blockPlacedLast.add(sob.getMaterial("DIODE_BLOCK_OFF").getId());
        blockPlacedLast.add(sob.getMaterial("DIODE_BLOCK_ON").getId());
        blockPlacedLast.add(sob.getMaterial("TRAP_DOOR").getId());
        blockPlacedLast.add(sob.getMaterial("VINE").getId());
        blockPlacedLast.add(sob.getMaterial("WATER_LILY").getId());
        blockPlacedLast.add(sob.getMaterial("NETHER_WARTS").getId());
        blockPlacedLast.add(sob.getMaterial("PISTON_BASE").getId());
        blockPlacedLast.add(sob.getMaterial("PISTON_STICKY_BASE").getId());
        blockPlacedLast.add(sob.getMaterial("PISTON_EXTENSION").getId());
        blockPlacedLast.add(sob.getMaterial("PISTON_MOVING_PIECE").getId());
        blockPlacedLast.add(sob.getMaterial("COCOA").getId());
        blockPlacedLast.add(sob.getMaterial("TRIPWIRE_HOOK").getId());
        blockPlacedLast.add(sob.getMaterial("TRIPWIRE").getId());
        blockPlacedLast.add(sob.getMaterial("FLOWER_POT").getId());
        blockPlacedLast.add(sob.getMaterial("CARROT").getId());
        blockPlacedLast.add(sob.getMaterial("POTATO").getId());
        blockPlacedLast.add(sob.getMaterial("WOOD_BUTTON").getId());
        blockPlacedLast.add(sob.getMaterial("SKULL").getId());
        blockPlacedLast.add(sob.getMaterial("GOLD_PLATE").getId());
        blockPlacedLast.add(sob.getMaterial("IRON_PLATE").getId());
        blockPlacedLast.add(sob.getMaterial("REDSTONE_COMPARATOR_OFF").getId());
        blockPlacedLast.add(sob.getMaterial("REDSTONE_COMPARATOR_ON").getId());
        blockPlacedLast.add(sob.getMaterial("ACTIVATOR_RAIL").getId());
    }

    public WorldGenConfig getWGC(IWorld iWorld) {
        return getWGC(iWorld.getName());
    }

    public WorldGenConfig getWGC(String worldname) {
        return worldConfigs.get(worldname.toLowerCase());
    }

    public WorldGenConfig putWGC(String worldname, WorldGenConfig wgc) {
        return worldConfigs.put(worldname.toLowerCase(), wgc);
    }

    public boolean containsWGC(IWorld world) {
        return containsWGC(world.getName());
    }

    public boolean containsWGC(String worldname) {
        return worldConfigs.containsKey(worldname.toLowerCase());
    }

    public Set<String> worldSet() {
        return worldConfigs.keySet();
    }

    @Override
    public int getPlotSize(String worldname) {
        if (containsWGC(worldname)) {
            return getWGC(worldname).getInt(PLOT_SIZE);
        } else {
            plugin.getLogger().log(Level.WARNING, "Tried to get plot size for undefined world '{0}'", worldname);
            return 0;
        }
    }

    @Override
    public boolean createConfig(String worldname, Map<String, String> args, ICommandSender cs) {
        WorldGenConfig wgc = plugin.getWorldGenConfig(worldname);

        for (String key : args.keySet()) {
            wgc.set(key, args.get(key));
        }

        return true;
    }

    @Override
    public Map<String, String> getDefaultGenerationConfig() {
        // TODO: Either change interface or change WGC
        //return WorldGenConfig.cloneDefaults();
        throw new UnsupportedOperationException("Not supported yet. Either change interface or change WGC.");
    }

    @Override
    public int getRoadHeight(String worldname) {
        if (containsWGC(worldname)) {
            return getWGC(worldname).getInt(GROUND_LEVEL);
        } else {
            plugin.getLogger().log(Level.WARNING, "Tried to get road height for undefined world '{0}'", worldname);
            return 64;
        }
    }

    @Override
    public String getPlotId(IPlayer player) {
        return getPlotId(player.getLocation());
    }

    @Override
    public List<IPlayer> getPlayersInPlot(IWorld w, String id) {
        List<IPlayer> playersInPlot = new ArrayList<>();

        for (IPlayer p : sob.getOnlinePlayers()) {
            if (getPlotId(p).equals(id)) {
                playersInPlot.add(p);
            }
        }
        return playersInPlot;
    }

    @Override
    public void setBiome(IWorld w, String id, IBiome b) {
        int bottomX = bottomX(id, w) - 1;
        int topX = topX(id, w) + 1;
        int bottomZ = bottomZ(id, w) - 1;
        int topZ = topZ(id, w) + 1;

        for (int x = bottomX; x <= topX; x++) {
            for (int z = bottomZ; z <= topZ; z++) {
                w.getBlockAt(x, 0, z).setBiome(b);
            }
        }

        refreshPlotChunks(w, id);
    }

    @Override
    public void refreshPlotChunks(IWorld w, String id) {
        int bottomX = bottomX(id, w);
        int topX = topX(id, w);
        int bottomZ = bottomZ(id, w);
        int topZ = topZ(id, w);

        int minChunkX = (int) Math.floor((double) bottomX / 16);
        int maxChunkX = (int) Math.floor((double) topX / 16);
        int minChunkZ = (int) Math.floor((double) bottomZ / 16);
        int maxChunkZ = (int) Math.floor((double) topZ / 16);

        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                w.refreshChunk(x, z);
            }
        }
    }

    @Override
    public ILocation getTop(IWorld w, String id) {
        return getPlotTopLoc(w, id);
    }

    @Override
    public ILocation getBottom(IWorld w, String id) {
        return getPlotBottomLoc(w, id);
    }

    @Override
    public void clear(IWorld w, String id) {
        clear(getBottom(w, id), getTop(w, id));
    }

    @Override
    public Long[] clear(IWorld w, String id, long maxBlocks, boolean clearEntities, Long[] start) {
        return clear(getBottom(w, id), getTop(w, id), maxBlocks, clearEntities, start);
    }

    public void clearEntities(ILocation bottom, ILocation top) {
        int bottomX = bottom.getBlockX();
        int topX = top.getBlockX();
        int bottomZ = bottom.getBlockZ();
        int topZ = top.getBlockZ();

        IWorld w = bottom.getWorld();

        int minChunkX = (int) Math.floor((double) bottomX / 16);
        int maxChunkX = (int) Math.floor((double) topX / 16);
        int minChunkZ = (int) Math.floor((double) bottomZ / 16);
        int maxChunkZ = (int) Math.floor((double) topZ / 16);

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                IChunk chunk = w.getChunkAt(cx, cz);

                for (IEntity e : chunk.getEntities()) {
                    ILocation eloc = e.getLocation();

                    if (!(e instanceof IPlayer) && eloc.getBlockX() >= bottom.getBlockX() && eloc.getBlockX() <= top.getBlockX()
                                && eloc.getBlockZ() >= bottom.getBlockZ() && eloc.getBlockZ() <= top.getBlockZ()) {
                        e.remove();
                    }
                }
            }
        }
    }

    @Override
    public boolean isBlockInPlot(String id, ILocation blocklocation) {
        IWorld w = blocklocation.getWorld();
        int lowestX = Math.min(bottomX(id, w), topX(id, w));
        int highestX = Math.max(bottomX(id, w), topX(id, w));
        int lowestZ = Math.min(bottomZ(id, w), topZ(id, w));
        int highestZ = Math.max(bottomZ(id, w), topZ(id, w));

        return blocklocation.getBlockX() >= lowestX && blocklocation.getBlockX() <= highestX
                       && blocklocation.getBlockZ() >= lowestZ && blocklocation.getBlockZ() <= highestZ;
    }

    @Override
    public boolean movePlot(IWorld wFrom, IWorld wTo, String idFrom, String idTo) {
        ILocation plot1Bottom = getPlotBottomLoc(wFrom, idFrom);
        ILocation plot2Bottom = getPlotBottomLoc(wTo, idTo);
        ILocation plot1Top = getPlotTopLoc(wFrom, idFrom);
        ILocation plot2Top = getPlotTopLoc(wTo, idTo);

        int distanceX = plot1Bottom.getBlockX() - plot2Bottom.getBlockX();
        int distanceZ = plot1Bottom.getBlockZ() - plot2Bottom.getBlockZ();

        Set<BlockInfo> lastblocks = new HashSet<>();

        int bottomX = plot1Bottom.getBlockX();
        int topX = plot1Top.getBlockX();
        int bottomZ = plot1Bottom.getBlockZ();
        int topZ = plot1Top.getBlockZ();

        for (int x = bottomX; x <= topX; x++) {
            for (int z = bottomZ; z <= topZ; z++) {
                IBlock plot1Block = wFrom.getBlockAt(x, 0, z);
                BlockRepresentation plot1BlockRepresentation = new BlockRepresentation(plot1Block);
                IBlock plot2Block = wTo.getBlockAt(x - distanceX, 0, z - distanceZ);
                BlockRepresentation plot2BlockRepresentation = new BlockRepresentation(plot2Block);

                String plot1Biome = plot1Block.getBiome().name();
                String plot2Biome = plot2Block.getBiome().name();

                plot1Block.setBiome(sob.getBiome(plot2Biome));
                plot2Block.setBiome(sob.getBiome(plot1Biome));

                for (int y = 0; y < wFrom.getMaxHeight(); y++) {
                    plot1Block = wFrom.getBlockAt(x, y, z);
                    plot2Block = wTo.getBlockAt(x - distanceX, y, z - distanceZ);

                    if (!blockPlacedLast.contains((int) plot2BlockRepresentation.getId())) {
                        plot2BlockRepresentation.setBlock(plot1Block, false);
                    } else {
                        plot1Block.setTypeId(0, false);
                        lastblocks.add(new BlockInfo(plot2BlockRepresentation, wFrom, x, y, z));
                    }

                    if (!blockPlacedLast.contains((int) plot1BlockRepresentation.getId())) {
                        plot1BlockRepresentation.setBlock(plot2Block, false);
                    } else {
                        plot2Block.setTypeId(0, false);
                        lastblocks.add(new BlockInfo(plot1BlockRepresentation, wTo, x - distanceX, y, z - distanceZ));
                    }
                }
            }
        }

        for (BlockInfo bi : lastblocks) {
            IBlock block = bi.loc.getBlock();
            bi.block.setBlock(block, false);
        }

        lastblocks.clear();

        //Move entities
        int minChunkX1 = (int) Math.floor((double) bottomX / 16);
        int maxChunkX1 = (int) Math.floor((double) topX / 16);
        int minChunkZ1 = (int) Math.floor((double) bottomZ / 16);
        int maxChunkZ1 = (int) Math.floor((double) topZ / 16);

        int minChunkX2 = (int) Math.floor((double) (bottomX - distanceX) / 16);
        int maxChunkX2 = (int) Math.floor((double) (topX - distanceX) / 16);
        int minChunkZ2 = (int) Math.floor((double) (bottomZ - distanceZ) / 16);
        int maxChunkZ2 = (int) Math.floor((double) (topZ - distanceZ) / 16);

        Set<IEntity> entities1 = new HashSet<>();
        Set<IEntity> entities2 = new HashSet<>();

        for (int cx = minChunkX1; cx <= maxChunkX1; cx++) {
            for (int cz = minChunkZ1; cz <= maxChunkZ1; cz++) {
                IChunk chunk = wFrom.getChunkAt(cx, cz);

                for (IEntity e : chunk.getEntities()) {
                    ILocation eloc = e.getLocation();

                    if (!(e instanceof IPlayer) /*&& !(e instanceof Hanging)*/ && eloc.getBlockX() >= plot1Bottom.getBlockX() && eloc.getBlockX() <= plot1Top.getBlockX()
                                && eloc.getBlockZ() >= plot1Bottom.getBlockZ() && eloc.getBlockZ() <= plot1Top.getBlockZ()) {
                        entities1.add(e);
                    }
                }
            }
        }

        for (int cx = minChunkX2; cx <= maxChunkX2; cx++) {
            for (int cz = minChunkZ2; cz <= maxChunkZ2; cz++) {
                IChunk chunk = wFrom.getChunkAt(cx, cz);

                for (IEntity e : chunk.getEntities()) {
                    ILocation eloc = e.getLocation();

                    if (!(e instanceof IPlayer) /*&& !(e instanceof Hanging)*/ && eloc.getBlockX() >= plot2Bottom.getBlockX() && eloc.getBlockX() <= plot2Top.getBlockX()
                                && eloc.getBlockZ() >= plot2Bottom.getBlockZ() && eloc.getBlockZ() <= plot2Top.getBlockZ()) {
                        entities2.add(e);
                    }
                }
            }
        }

        for (IEntity e : entities1) {
            ILocation l = e.getLocation();
            ILocation newl = wTo.createLocation(l.getX() - distanceX, l.getY(), l.getZ() - distanceZ);

            if (e.getType() == sob.getEntityType("ITEM_FRAME")) {
                IItemFrame i = ((IItemFrame) e);
                IBlockFace bf = i.getFacing();
                IItemStack is = i.getItem();
                IRotation rot = i.getRotation();

                i.teleport(newl);
                i.setItem(is);
                i.setRotation(rot);
                i.setFacingDirection(bf, true);
            } else if (e.getType() == sob.getEntityType("PAINTING")) {
                IPainting p = ((IPainting) e);
                IBlockFace bf = p.getFacing();
                int[] mod = getPaintingMod(p.getArt(), bf);
                if (mod != null) {
                    newl = newl.add(mod[0], mod[1], mod[2]);
                }
                p.teleport(newl);
                p.setFacingDirection(bf, true);
            } else {
                e.teleport(newl);
            }
        }

        for (IEntity e : entities2) {
            ILocation l = e.getLocation();
            ILocation newl = wFrom.createLocation(l.getX() + distanceX, l.getY(), l.getZ() + distanceZ);

            if (e.getType() == sob.getEntityType("ITEM_FRAME")) {
                IItemFrame i = ((IItemFrame) e);
                IBlockFace bf = i.getFacing();
                IItemStack is = i.getItem();
                IRotation rot = i.getRotation();

                i.teleport(newl);
                i.setItem(is);
                i.setRotation(rot);
                i.setFacingDirection(bf, true);

            } else if (e.getType() == sob.getEntityType("PAINTING")) {
                IPainting p = ((IPainting) e);
                IBlockFace bf = p.getFacing();
                int[] mod = getPaintingMod(p.getArt(), bf);
                if (mod != null) {
                    newl = newl.add(mod[0], mod[1], mod[2]);
                }
                p.teleport(newl);
                p.setFacingDirection(bf, true);
            } else {
                e.teleport(newl);
            }
        }

        return true;
    }

    private int[] getPaintingMod(IArt a, IBlockFace bf) {
        int H = a.getBlockHeight();
        int W = a.getBlockWidth();

        //Same for all faces
        if (H == 2 && W == 1) {
            return new int[]{0, -1, 0};
        }

        switch (bf.getFaceName()) {
            case "WEST":
                if (H == 3 && W == 4 || H == 1 && W == 2) {
                    return new int[]{0, 0, -1};
                } else if (H == 2 && W == 2 || H == 4 && W == 4 || H == 2 && W == 4) {
                    return new int[]{0, -1, -1};
                }

                break;
            case "SOUTH":
                if (H == 3 && W == 4 || H == 1 && W == 2) {
                    return new int[]{-1, 0, 0};
                } else if (H == 2 && W == 2 || H == 4 && W == 4 || H == 2 && W == 4) {
                    return new int[]{-1, -1, 0};
                }

                break;
            case "EAST":
                if (H == 2 && W == 2 || H == 4 && W == 4 || H == 2 && W == 4) {
                    return new int[]{0, -1, 0};
                }

                break;
            case "NORTH":
                if (H == 2 && W == 2 || H == 4 && W == 4 || H == 2 && W == 4) {
                    return new int[]{0, -1, 0};
                }

                break;
            default:
                return new int[]{0, 0, 0};
        }

        return new int[]{0, 0, 0};
    }

    @Override
    public int bottomX(String id, IWorld w) {
        return getPlotBottomLoc(w, id).getBlockX();
    }

    @Override
    public int bottomZ(String id, IWorld w) {
        return getPlotBottomLoc(w, id).getBlockZ();
    }

    @Override
    public int topX(String id, IWorld w) {
        return getPlotTopLoc(w, id).getBlockX();
    }

    @Override
    public int topZ(String id, IWorld w) {
        return getPlotTopLoc(w, id).getBlockZ();
    }

    @Override
    public boolean isValidId(String id) {
        String[] coords = id.split(";");

        if (coords.length != 2) {
            return false;
        } else {
            try {
                Integer.parseInt(coords[0]);
                Integer.parseInt(coords[1]);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    @Override
    public int getIdX(String id) {
        return Integer.parseInt(id.substring(0, id.indexOf(";")));
    }

    @Override
    public int getIdZ(String id) {
        return Integer.parseInt(id.substring(id.indexOf(";") + 1));
    }
}
