package com.worldcretornica.plotme_abstractgenerator.bukkit;

import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.GROUND_LEVEL;
import static com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath.PLOT_SIZE;

import com.worldcretornica.plotme_abstractgenerator.WorldGenConfig;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;
import com.worldcretornica.schematic.Schematic;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.Serializable;
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

    public boolean containsWGC(World world) {
        return containsWGC(world.getName());
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
    public Map<String, String> getDefaultGenerationConfig() {
        // TODO: Either change interface or change WGC
        throw new UnsupportedOperationException("Not supported yet. Either change interface or change WGC.");
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
    public void setBiome(World world, String id, Biome biome) {
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
    public void refreshPlotChunks(World world, String id) {
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
    public Location getTop(World world, String id) {
        return getPlotTopLoc(world, id);
    }

    @Override
    public Location getBottom(World world, String id) {
        return getPlotBottomLoc(world, id);
    }

    @Override
    public Long[] clear(World world, String id, long maxBlocks, Long[] start) {
        return clear(getBottom(world, id), getTop(world, id), maxBlocks, start);
    }

    @Override
    public boolean isBlockInPlot(String id, Location location) {
        World world = location.getWorld();
        int lowestX = Math.min(bottomX(id, world), topX(id, world));
        int highestX = Math.max(bottomX(id, world), topX(id, world));
        int lowestZ = Math.min(bottomZ(id, world), topZ(id, world));
        int highestZ = Math.max(bottomZ(id, world), topZ(id, world));

        return location.getBlockX() >= lowestX && location.getBlockX() <= highestX
               && location.getBlockZ() >= lowestZ && location.getBlockZ() <= highestZ;
    }

    @Override
    public boolean movePlot(World world, String idFrom, String idTo) {
        Location plot1Bottom = getPlotBottomLoc(world, idFrom);
        Location plot2Bottom = getPlotBottomLoc(world, idTo);
        Location plot1Top = getPlotTopLoc(world, idFrom);
        Location plot2Top = getPlotTopLoc(world, idTo);
        
        Schematic schem1 = (Schematic) getPlotSchematic(world, idFrom);
        Schematic schem2 = (Schematic) getPlotSchematic(world, idTo);
        
        clearEntities(plot1Bottom, plot1Top);
        clearEntities(plot2Bottom, plot2Top);
        
        plugin.getSchematicUtil().pasteSchematic(plot1Bottom, schem2);
        plugin.getSchematicUtil().pasteSchematic(plot2Bottom, schem1);

        /*int distanceX = plot1Bottom.getBlockX() - plot2Bottom.getBlockX();
        int distanceZ = plot1Bottom.getBlockZ() - plot2Bottom.getBlockZ();

        Collection<Block> lastblocks = new HashSet<>();

        int bottomX = plot1Bottom.getBlockX();
        int topX = plot1Top.getBlockX();
        int bottomZ = plot1Bottom.getBlockZ();
        int topZ = plot1Top.getBlockZ();

        for (int x = bottomX; x <= topX; x++) {
            for (int z = bottomZ; z <= topZ; z++) {
                Block plot1Block = world.getBlockAt(x, 0, z);
                Block plot2Block = world.getBlockAt(x - distanceX, 0, z - distanceZ);

                plot1Block.setBiome(plot2Block.getBiome());
                plot2Block.setBiome(plot1Block.getBiome());

                for (int y = 0; y < 256; y++) {
                    plot1Block = world.getBlockAt(x, y, z);
                    plot2Block = world.getBlockAt(x - distanceX, y, z - distanceZ);

                    if (!plugin.getSchematicUtil().blockPlacedLast.contains(plot2Block.getTypeId())) {
                        plot2Block.setTypeIdAndData(plot1Block.getTypeId(), plot1Block.getData(), false);
                    } else {
                        plot1Block.setType(Material.AIR);
                        lastblocks.add(plot2Block);
                    }

                    if (!plugin.getSchematicUtil().blockPlacedLast.contains(plot1Block.getTypeId())) {
                        plot1Block.setTypeIdAndData(plot2Block.getTypeId(), plot2Block.getData(), false);
                    } else {
                        plot2Block.setType(Material.AIR);
                        lastblocks.add(plot1Block);
                    }
                }
            }
        }

        for (Block bi : lastblocks) {
            Block block = bi.getLocation().getBlock();
            bi.setTypeIdAndData(block.getTypeId(), block.getData(), false);
        }

        lastblocks.clear();

        //Move entities
        int minChunkX1 = (int) Math.floor(bottomX / 16);
        int maxChunkX1 = (int) Math.floor(topX / 16);
        int minChunkZ1 = (int) Math.floor(bottomZ / 16);
        int maxChunkZ1 = (int) Math.floor(topZ / 16);

        int minChunkX2 = (int) Math.floor((bottomX - distanceX) / 16);
        int maxChunkX2 = (int) Math.floor((topX - distanceX) / 16);
        int minChunkZ2 = (int) Math.floor((bottomZ - distanceZ) / 16);
        int maxChunkZ2 = (int) Math.floor((topZ - distanceZ) / 16);

        Collection<Entity> entities1 = new HashSet<>();
        Collection<Entity> entities2 = new HashSet<>();

        for (int cx = minChunkX1; cx <= maxChunkX1; cx++) {
            for (int cz = minChunkZ1; cz <= maxChunkZ1; cz++) {
                Chunk chunk = world.getChunkAt(cx, cz);

                for (Entity entity : chunk.getEntities()) {
                    Location location = entity.getLocation();

                    if (!(entity instanceof Player) && location.getBlockX() >= plot1Bottom.getBlockX()
                        && location.getBlockX() <= plot1Top.getBlockX()
                        && location.getBlockZ() >= plot1Bottom.getBlockZ() && location.getBlockZ() <= plot1Top.getBlockZ()) {
                        entities1.add(entity);
                    }
                }
            }
        }

        for (int cx = minChunkX2; cx <= maxChunkX2; cx++) {
            for (int cz = minChunkZ2; cz <= maxChunkZ2; cz++) {
                Chunk chunk = world.getChunkAt(cx, cz);

                for (Entity entity : chunk.getEntities()) {
                    Location location = entity.getLocation();

                    if (!(entity instanceof Player) && location.getBlockX() >= plot2Bottom.getBlockX()
                        && location.getBlockX() <= plot2Top.getBlockX()
                        && location.getBlockZ() >= plot2Bottom.getBlockZ() && location.getBlockZ() <= plot2Top.getBlockZ()) {
                        entities2.add(entity);
                    }
                }
            }
        }

        for (Entity entity1 : entities1) {
            Location location = entity1.getLocation();
            Location location1 = new Location(world, location.getX() - distanceX, location.getY(), location.getZ() - distanceZ);

            if (entity1.getType() == EntityType.ITEM_FRAME) {
                ItemFrame itemFrame = ((ItemFrame) entity1);
                BlockFace blockFace = itemFrame.getFacing();
                ItemStack item = itemFrame.getItem();
                Rotation rotation = itemFrame.getRotation();

                itemFrame.teleport(location1);
                itemFrame.setItem(item);
                itemFrame.setRotation(rotation);
                itemFrame.setFacingDirection(blockFace, true);
            } else if (entity1.getType() == EntityType.PAINTING) {
                Painting painting = ((Painting) entity1);
                BlockFace blockFace = painting.getFacing();
                location1 = location1.add(painting.getLocation());
                painting.teleport(location1);
                painting.setFacingDirection(blockFace, true);
            } else {
                entity1.teleport(location1);
            }
        }

        for (Entity entity : entities2) {
            Location location = entity.getLocation();
            Location location1 = new Location(world, location.getX() + distanceX, location.getY(), location.getZ() + distanceZ);

            if (entity.getType() == EntityType.ITEM_FRAME) {
                ItemFrame itemFrame = ((ItemFrame) entity);
                BlockFace blockFace = itemFrame.getFacing();
                ItemStack item = itemFrame.getItem();
                Rotation rotation = itemFrame.getRotation();

                itemFrame.teleport(location1);
                itemFrame.setItem(item);
                itemFrame.setRotation(rotation);
                itemFrame.setFacingDirection(blockFace, true);

            } else if (entity.getType() == EntityType.PAINTING) {
                Painting painting = ((Painting) entity);
                BlockFace blockFace = painting.getFacing();
                location1 = location1.add(painting.getLocation());
                painting.teleport(location1);
                painting.setFacingDirection(blockFace, true);
            } else {
                entity.teleport(location1);
            }
        }*/

        return true;
    }

    @Override
    public int bottomX(String id, World world) {
        return getPlotBottomLoc(world, id).getBlockX();
    }

    @Override
    public int bottomZ(String id, World world) {
        return getPlotBottomLoc(world, id).getBlockZ();
    }

    @Override
    public int topX(String id, World world) {
        return getPlotTopLoc(world, id).getBlockX();
    }

    @Override
    public int topZ(String id, World world) {
        return getPlotTopLoc(world, id).getBlockZ();
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
    
    @Override
    public Schematic getPlotSchematic(World world, String id) {
        Location plotBottom = getPlotBottomLoc(world, id);
        Location plotTop = getPlotTopLoc(world, id);
        
        Schematic schem1 = plugin.getSchematicUtil().createCompiledSchematic(plotBottom, plotTop);
        
        return schem1;
    }
}
