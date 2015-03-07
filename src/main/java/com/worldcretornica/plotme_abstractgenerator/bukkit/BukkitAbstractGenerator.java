package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.configuration.MemorySection;
import com.worldcretornica.configuration.file.FileConfiguration;
import com.worldcretornica.plotme_abstractgenerator.AbstractGenerator;
import com.worldcretornica.plotme_abstractgenerator.WorldGenConfig;
import com.worldcretornica.plotme_core.bukkit.AbstractSchematicUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BukkitAbstractGenerator extends JavaPlugin implements AbstractGenerator {

    public MemorySection mainWorldsSection;
    private File configFolder;
    private BukkitConfigAccessor configCA;
    private Map<String, WorldGenConfig> worldConfigs = new HashMap<>();
    private AbstractSchematicUtil schematicutil;

    @Override
    public final void onEnable() {
        setupConfigFolders();
        setupConfig();
        initialize();
        setupMetrics();
    }

    public AbstractSchematicUtil getSchematicUtil() {
        return this.schematicutil;
    }

    public void setSchematicUtil(AbstractSchematicUtil schematicutil) {
        this.schematicutil = schematicutil;
    }

    /**
     * Called when this plugin is enabled.
     */
    public abstract void initialize();

    @Override
    public final void onDisable() {
        configFolder = null;
        configCA = null;
    }

    private void setupConfigFolders() {
        File pluginsFolder = getDataFolder().getParentFile();
        File coreFolder = new File(pluginsFolder, "PlotMe");
        configFolder = new File(coreFolder, getName());
        //noinspection ResultOfMethodCallIgnored
        configFolder.mkdirs();
    }

    @Override
    public File getPluginFolder() {
        return configFolder;
    }

    @Override
    public void reloadConfig() {
        configCA.reloadConfig();
    }

    /**
     * Gets a {@link FileConfiguration} for this plugin, read through
     * "config.yml"
     * <p/>
     * If there is a default config.yml embedded in this plugin, it will be
     * provided as a default for this Configuration.
     *
     * @return Plugin configuration
     */
    public FileConfiguration getConfiguration() {
        return configCA.getConfig();
    }

    /**
     * Saves the {@link FileConfiguration} retrievable by {@link #getConfiguration()}.
     */
    public void saveConfigFile() {
        configCA.saveConfig();
    }

    /**
     * Discards any data in {@link #getConfiguration()} and reloads from disk.
     */
    private void setupConfig() {
        // Set the config accessor for the main config.yml
        configCA = new BukkitConfigAccessor(this);

        if (getConfiguration().contains("worlds")) {
            mainWorldsSection = (MemorySection) getConfiguration().getConfigurationSection("worlds");
        } else {
            mainWorldsSection = (MemorySection) getConfiguration().createSection("worlds");
        }
        configCA.saveConfig();
    }

    private void setupMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ignored) {
        }
    }

    public WorldGenConfig putWGC(String world, WorldGenConfig worldGenConfig) {
        return worldConfigs.put(world.toLowerCase(), worldGenConfig);
    }

    public WorldGenConfig getWGC(String world) {
        return worldConfigs.get(world.toLowerCase());
    }

}
