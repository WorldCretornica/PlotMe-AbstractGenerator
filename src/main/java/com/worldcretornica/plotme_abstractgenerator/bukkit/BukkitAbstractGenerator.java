package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.configuration.ConfigurationSection;
import com.worldcretornica.configuration.file.FileConfiguration;
import com.worldcretornica.plotme_abstractgenerator.AbstractGenerator;
import com.worldcretornica.plotme_core.AbstractSchematicUtil;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BukkitAbstractGenerator extends JavaPlugin implements AbstractGenerator {

    private final Map<String, ConfigurationSection> worldConfigs = new HashMap<>();
    private final File configFolder = new File(new File("plugins", "PlotMe"), getName());
    public ConfigurationSection mainWorldsSection;
    public PlotMe_CorePlugin plotMePlugin = null;
    private ConfigAccessor configCA;
    private AbstractSchematicUtil schematicutil;

    @Override
    public final void onEnable() {
        setupConfigFolders();
        setupConfig();
        PluginManager pm = Bukkit.getPluginManager();
        plotMePlugin = (PlotMe_CorePlugin) pm.getPlugin("PlotMe");
        if (plotMePlugin != null) {
            initialize(plotMePlugin);
        }
        setupMetrics();
    }

    public AbstractSchematicUtil getSchematicUtil() {
        return this.schematicutil;
    }

    protected void setSchematicUtil(AbstractSchematicUtil schematicutil) {
        this.schematicutil = schematicutil;
    }

    /**
     * Called when this plugin is enabled.
     * @param plotMePlugin PlotMe-Core
     */
    public abstract void initialize(PlotMe_CorePlugin plotMePlugin);

    @Override
    public final void onDisable() {
    }

    private void setupConfigFolders() {
        //noinspection ResultOfMethodCallIgnored
        configFolder.mkdirs();
    }

    @Override
    public File getPluginFolder() {
        return configFolder;
    }

    @Override
    public void reloadConfig() {
        configCA.reloadFile();
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
        configCA = new ConfigAccessor(this, getPluginFolder(), "config.yml");

        if (getConfiguration().contains("worlds")) {
            mainWorldsSection = getConfiguration().getConfigurationSection("worlds");
        } else {
            mainWorldsSection = getConfiguration().createSection("worlds");
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

    public ConfigurationSection putWGC(String world, ConfigurationSection worldGenConfig) {
        return worldConfigs.put(world.toLowerCase(), worldGenConfig);
    }

    public ConfigurationSection getWGC(String world) {
        return worldConfigs.get(world.toLowerCase());
    }

}
