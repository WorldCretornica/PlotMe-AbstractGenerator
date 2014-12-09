package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.plotme_abstractgenerator.AbstractGenerator;
import com.worldcretornica.plotme_abstractgenerator.WorldGenConfig;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.PLOT_SIZE;

public abstract class BukkitAbstractGenManager implements IBukkitPlotMe_GeneratorManager {

    // List of blocks that should be placed last in world generation
    protected static final Set<Integer> blockPlacedLast = new HashSet<>();

    private final AbstractGenerator plugin;
    private final Map<String, WorldGenConfig> worldConfigs;

    @SuppressWarnings("deprecation")
    public BukkitAbstractGenManager(AbstractGenerator instance) {
        plugin = instance;
        worldConfigs = new HashMap<>();
        
        blockPlacedLast.add(Material.SAPLING.getId());
        blockPlacedLast.add(Material.BED.getId());
        blockPlacedLast.add(Material.POWERED_RAIL.getId());
        blockPlacedLast.add(Material.DETECTOR_RAIL.getId());
        blockPlacedLast.add(Material.LONG_GRASS.getId());
        blockPlacedLast.add(Material.DEAD_BUSH.getId());
        blockPlacedLast.add(Material.PISTON_EXTENSION.getId());
        blockPlacedLast.add(Material.YELLOW_FLOWER.getId());
        blockPlacedLast.add(Material.RED_ROSE.getId());
        blockPlacedLast.add(Material.BROWN_MUSHROOM.getId());
        blockPlacedLast.add(Material.RED_MUSHROOM.getId());
        blockPlacedLast.add(Material.TORCH.getId());
        blockPlacedLast.add(Material.FIRE.getId());
        blockPlacedLast.add(Material.REDSTONE_WIRE.getId());
        blockPlacedLast.add(Material.CROPS.getId());
        blockPlacedLast.add(Material.LADDER.getId());
        blockPlacedLast.add(Material.RAILS.getId());
        blockPlacedLast.add(Material.LEVER.getId());
        blockPlacedLast.add(Material.STONE_PLATE.getId());
        blockPlacedLast.add(Material.WOOD_PLATE.getId());
        blockPlacedLast.add(Material.REDSTONE_TORCH_OFF.getId());
        blockPlacedLast.add(Material.REDSTONE_TORCH_ON.getId());
        blockPlacedLast.add(Material.STONE_BUTTON.getId());
        blockPlacedLast.add(Material.SNOW.getId());
        blockPlacedLast.add(Material.PORTAL.getId());
        blockPlacedLast.add(Material.DIODE_BLOCK_OFF.getId());
        blockPlacedLast.add(Material.DIODE_BLOCK_ON.getId());
        blockPlacedLast.add(Material.TRAP_DOOR.getId());
        blockPlacedLast.add(Material.VINE.getId());
        blockPlacedLast.add(Material.WATER_LILY.getId());
        blockPlacedLast.add(Material.NETHER_WARTS.getId());
        blockPlacedLast.add(Material.PISTON_BASE.getId());
        blockPlacedLast.add(Material.PISTON_STICKY_BASE.getId());
        blockPlacedLast.add(Material.PISTON_EXTENSION.getId());
        blockPlacedLast.add(Material.PISTON_MOVING_PIECE.getId());
        blockPlacedLast.add(Material.COCOA.getId());
        blockPlacedLast.add(Material.TRIPWIRE_HOOK.getId());
        blockPlacedLast.add(Material.TRIPWIRE.getId());
        blockPlacedLast.add(Material.FLOWER_POT.getId());
        blockPlacedLast.add(Material.CARROT.getId());
        blockPlacedLast.add(Material.POTATO.getId());
        blockPlacedLast.add(Material.WOOD_BUTTON.getId());
        blockPlacedLast.add(Material.SKULL.getId());
        blockPlacedLast.add(Material.GOLD_PLATE.getId());
        blockPlacedLast.add(Material.IRON_PLATE.getId());
        blockPlacedLast.add(Material.REDSTONE_COMPARATOR_OFF.getId());
        blockPlacedLast.add(Material.REDSTONE_COMPARATOR_ON.getId());
        blockPlacedLast.add(Material.ACTIVATOR_RAIL.getId());
    }

    public WorldGenConfig getWGC(World iWorld) {
        return getWGC(iWorld.getName());
    }

    public WorldGenConfig getWGC(String worldname) {
        return worldConfigs.get(worldname.toLowerCase());
    }

    public WorldGenConfig putWGC(String worldname, WorldGenConfig wgc) {
        return worldConfigs.put(worldname.toLowerCase(), wgc);
    }

    public boolean containsWGC(World world) {
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
    public boolean createConfig(String worldname, Map<String, String> args) {
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
    public String getPlotId(Player player) {
        return getPlotId(player.getLocation());
    }

    @Override
    public List<Player> getPlayersInPlot(String id) {
        List<Player> playersInPlot = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (getPlotId(p).equals(id)) {
                playersInPlot.add(p);
            }
        }
        return playersInPlot;
    }

    @Override
    public void setBiome(World w, String id, Biome b) {
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
    public void refreshPlotChunks(World w, String id) {
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
    public Location getTop(World w, String id) {
        return getPlotTopLoc(w, id);
    }

    @Override
    public Location getBottom(World w, String id) {
        return getPlotBottomLoc(w, id);
    }

    @Override
    public void clear(World w, String id) {
        clear(getBottom(w, id), getTop(w, id));
    }

    @Override
    public Long[] clear(World w, String id, long maxBlocks, Long[] start) {
        return clear(getBottom(w, id), getTop(w, id), maxBlocks, start);
    }

    public void clearEntities(Location bottom, Location top) {
        int bottomX = bottom.getBlockX();
        int topX = top.getBlockX();
        int bottomZ = bottom.getBlockZ();
        int topZ = top.getBlockZ();

        World w = bottom.getWorld();

        int minChunkX = (int) Math.floor((double) bottomX / 16);
        int maxChunkX = (int) Math.floor((double) topX / 16);
        int minChunkZ = (int) Math.floor((double) bottomZ / 16);
        int maxChunkZ = (int) Math.floor((double) topZ / 16);

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                Chunk chunk = w.getChunkAt(cx, cz);

                for (Entity e : chunk.getEntities()) {
                    Location eloc = e.getLocation();

                    if (!(e instanceof Player) && eloc.getBlockX() >= bottom.getBlockX() && eloc.getBlockX() <= top.getBlockX()
                                && eloc.getBlockZ() >= bottom.getBlockZ() && eloc.getBlockZ() <= top.getBlockZ()) {
                        e.remove();
                    }
                }
            }
        }
    }

    @Override
    public boolean isBlockInPlot(String id, Location blocklocation) {
        World w = blocklocation.getWorld();
        int lowestX = Math.min(bottomX(id, w), topX(id, w));
        int highestX = Math.max(bottomX(id, w), topX(id, w));
        int lowestZ = Math.min(bottomZ(id, w), topZ(id, w));
        int highestZ = Math.max(bottomZ(id, w), topZ(id, w));

        return blocklocation.getBlockX() >= lowestX && blocklocation.getBlockX() <= highestX
                       && blocklocation.getBlockZ() >= lowestZ && blocklocation.getBlockZ() <= highestZ;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean movePlot(World world, String idFrom, String idTo) {
        Location plot1Bottom = getPlotBottomLoc(world, idFrom);
        Location plot2Bottom = getPlotBottomLoc(world, idTo);
        Location plot1Top = getPlotTopLoc(world, idFrom);
        Location plot2Top = getPlotTopLoc(world, idTo);

        int distanceX = plot1Bottom.getBlockX() - plot2Bottom.getBlockX();
        int distanceZ = plot1Bottom.getBlockZ() - plot2Bottom.getBlockZ();

        Set<BlockInfo> lastblocks = new HashSet<>();

        int bottomX = plot1Bottom.getBlockX();
        int topX = plot1Top.getBlockX();
        int bottomZ = plot1Bottom.getBlockZ();
        int topZ = plot1Top.getBlockZ();

        for (int x = bottomX; x <= topX; x++) {
            for (int z = bottomZ; z <= topZ; z++) {
                Block plot1Block = world.getBlockAt(x, 0, z);
                BukkitBlockRepresentation plot1BlockRepresentation = new BukkitBlockRepresentation(plot1Block);
                Block plot2Block = world.getBlockAt(x - distanceX, 0, z - distanceZ);
                BukkitBlockRepresentation plot2BlockRepresentation = new BukkitBlockRepresentation(plot2Block);

                plot1Block.setBiome(plot2Block.getBiome());
                plot2Block.setBiome(plot1Block.getBiome());

                for (int y = 0; y < 256; y++) {
                    plot1Block = world.getBlockAt(x, y, z);
                    plot2Block = world.getBlockAt(x - distanceX, y, z - distanceZ);

                    if (!blockPlacedLast.contains((int) plot2BlockRepresentation.getId())) {
                        plot2BlockRepresentation.setBlock(plot1Block, false);
                    } else {
                        plot1Block.setTypeId(0, false);
                        lastblocks.add(new BlockInfo(plot2BlockRepresentation, world, x, y, z));
                    }

                    if (!blockPlacedLast.contains((int) plot1BlockRepresentation.getId())) {
                        plot1BlockRepresentation.setBlock(plot2Block, false);
                    } else {
                        plot2Block.setTypeId(0, false);
                        lastblocks.add(new BlockInfo(plot1BlockRepresentation, world, x - distanceX, y, z - distanceZ));
                    }
                }
            }
        }

        for (BlockInfo bi : lastblocks) {
            Block block = bi.loc.getBlock();
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

        Set<Entity> entities1 = new HashSet<>();
        Set<Entity> entities2 = new HashSet<>();

        for (int cx = minChunkX1; cx <= maxChunkX1; cx++) {
            for (int cz = minChunkZ1; cz <= maxChunkZ1; cz++) {
                Chunk chunk = world.getChunkAt(cx, cz);

                for (Entity e : chunk.getEntities()) {
                    Location eloc = e.getLocation();

                    if (!(e instanceof Player) /*&& !(e instanceof Hanging)*/ && eloc.getBlockX() >= plot1Bottom.getBlockX() && eloc.getBlockX() <= plot1Top.getBlockX()
                                && eloc.getBlockZ() >= plot1Bottom.getBlockZ() && eloc.getBlockZ() <= plot1Top.getBlockZ()) {
                        entities1.add(e);
                    }
                }
            }
        }

        for (int cx = minChunkX2; cx <= maxChunkX2; cx++) {
            for (int cz = minChunkZ2; cz <= maxChunkZ2; cz++) {
                Chunk chunk = world.getChunkAt(cx, cz);

                for (Entity e : chunk.getEntities()) {
                    Location eloc = e.getLocation();

                    if (!(e instanceof Player) /*&& !(e instanceof Hanging)*/ && eloc.getBlockX() >= plot2Bottom.getBlockX() && eloc.getBlockX() <= plot2Top.getBlockX()
                                && eloc.getBlockZ() >= plot2Bottom.getBlockZ() && eloc.getBlockZ() <= plot2Top.getBlockZ()) {
                        entities2.add(e);
                    }
                }
            }
        }

        for (Entity e : entities1) {
            Location l = e.getLocation();
            Location newl = new Location(world, l.getX() - distanceX, l.getY(), l.getZ() - distanceZ);

            if (e.getType() == EntityType.ITEM_FRAME) {
                ItemFrame i = ((ItemFrame) e);
                BlockFace bf = i.getFacing();
                ItemStack is = i.getItem();
                Rotation rot = i.getRotation();

                i.teleport(newl);
                i.setItem(is);
                i.setRotation(rot);
                i.setFacingDirection(bf, true);
            } else if (e.getType() == EntityType.PAINTING) {
                Painting p = ((Painting) e);
                BlockFace bf = p.getFacing();
                int[] mod = getPaintingMod(p.getArt(), bf);
                newl = newl.add(mod[0], mod[1], mod[2]);
                p.teleport(newl);
                p.setFacingDirection(bf, true);
            } else {
                e.teleport(newl);
            }
        }

        for (Entity entity : entities2) {
            Location location = entity.getLocation();
            Location newl = new Location(world, location.getX() + distanceX, location.getY(), location.getZ() + distanceZ);

            if (entity.getType() == EntityType.ITEM_FRAME) {
                ItemFrame i = ((ItemFrame) entity);
                BlockFace bf = i.getFacing();
                ItemStack is = i.getItem();
                Rotation rot = i.getRotation();

                i.teleport(newl);
                i.setItem(is);
                i.setRotation(rot);
                i.setFacingDirection(bf, true);

            } else if (entity.getType() == EntityType.PAINTING) {
                Painting p = ((Painting) entity);
                BlockFace bf = p.getFacing();
                int[] mod = getPaintingMod(p.getArt(), bf);
                if (mod != null) {
                    newl = newl.add(mod[0], mod[1], mod[2]);
                }
                p.teleport(newl);
                p.setFacingDirection(bf, true);
            } else {
                entity.teleport(newl);
            }
        }

        return true;
    }

    private int[] getPaintingMod(Art a, BlockFace bf) {
        int H = a.getBlockHeight();
        int W = a.getBlockWidth();

        //Same for all faces
        if (H == 2 && W == 1) {
            return new int[]{0, -1, 0};
        }

        switch (bf) {
            case WEST:
                if (H == 3 && W == 4 || H == 1 && W == 2) {
                    return new int[]{0, 0, -1};
                } else if (H == 2 && W == 2 || H == 4 && W == 4 || H == 2 && W == 4) {
                    return new int[]{0, -1, -1};
                }

                break;
            case SOUTH:
                if (H == 3 && W == 4 || H == 1 && W == 2) {
                    return new int[]{-1, 0, 0};
                } else if (H == 2 && W == 2 || H == 4 && W == 4 || H == 2 && W == 4) {
                    return new int[]{-1, -1, 0};
                }

                break;
            case EAST:
                if (H == 2 && W == 2 || H == 4 && W == 4 || H == 2 && W == 4) {
                    return new int[]{0, -1, 0};
                }

                break;
            case NORTH:
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
    public int bottomX(String id, World w) {
        return getPlotBottomLoc(w, id).getBlockX();
    }

    @Override
    public int bottomZ(String id, World w) {
        return getPlotBottomLoc(w, id).getBlockZ();
    }

    @Override
    public int topX(String id, World w) {
        return getPlotTopLoc(w, id).getBlockX();
    }

    @Override
    public int topZ(String id, World w) {
        return getPlotTopLoc(w, id).getBlockZ();
    }

    @Override
    public boolean isValidId(String id) {
        String[] coords = id.split(";");

        if (coords.length == 2) {
            try {
                Integer.parseInt(coords[0]);
                Integer.parseInt(coords[1]);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
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
