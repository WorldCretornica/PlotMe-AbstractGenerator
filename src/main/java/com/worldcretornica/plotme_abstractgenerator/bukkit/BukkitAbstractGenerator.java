package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.configuration.ConfigurationSection;
import com.worldcretornica.plotme_abstractgenerator.AbstractGenerator;
import com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath;
import com.worldcretornica.plotme_abstractgenerator.WorldGenConfig;
import com.worldcretornica.plotme_core.bukkit.AbstractSchematicUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public abstract class BukkitAbstractGenerator extends JavaPlugin implements AbstractGenerator {

    private static final String CONFIG_NAME = "config.yml";
    public ConfigurationSection mainWorldsSection;
    private File coreFolder;
    private File configFolder;
    private BukkitConfigAccessor configCA;
    private BukkitConfigAccessor captionsCA;
    private AbstractSchematicUtil schematicutil;

    @Override
    public final void onEnable() {
        setupConfigFolders();
        setupConfig();
        initialize();
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
        captionsCA = null;
        takedown();
    }

    /**
     * Called when this plugin is disabled.
     */
    public abstract void takedown();

    private void setupConfigFolders() {
        File pluginsFolder = getDataFolder().getParentFile();
        coreFolder = new File(pluginsFolder, "PlotMe");
        configFolder = new File(coreFolder, getName());
        //noinspection ResultOfMethodCallIgnored
        configFolder.mkdirs();
    }

    /**
     * Get the folder that contains data files for PlotMe-Core
     *
     * @return the folder that contains data files for PlotMe-Core
     */
    public File getCoreFolder() {
        return coreFolder;
    }

    @Override
    public File getConfigFolder() {
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
    public com.worldcretornica.configuration.file.FileConfiguration getConfiguration() {
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
        configCA = new BukkitConfigAccessor(this, CONFIG_NAME);

        // Set defaults for WorldGenConfig
        for (AbstractWorldConfigPath configPath : AbstractWorldConfigPath.values()) {
            WorldGenConfig.putDefault(configPath);
        }
        if (getConfiguration().contains("worlds")) {
            mainWorldsSection = getConfiguration().getConfigurationSection("worlds");
        } else {
            mainWorldsSection = getConfiguration().createSection("worlds");
        }
        configCA.saveConfig();
        // Set the config accessor for the captions.yml
        captionsCA = new BukkitConfigAccessor(this, "captions.yml");
        // Save default config into file.
        captionsCA.saveConfig();
    }

    /**
     * Gets a {@link WorldGenConfig} for the specified world with just the
     * global defaults for {@link WorldGenConfig}.
     *
     * @param worldName The world to get the {@link WorldGenConfig} for
     * @return The {@link WorldGenConfig}
     * @see #getWorldGenConfig(String, HashMap)
     */
    @Override
    public WorldGenConfig getWorldGenConfig(String worldName) {
        return getWorldGenConfig(worldName.toLowerCase(), new HashMap<String, Object>());
    }

    /**
     * Gets a {@link WorldGenConfig} for the specified world with default set as
     * specified in the HashMap which maps config paths to default values to be
     * added to or override the global defaults for {@link WorldGenConfig}.
     *
     * @param world    The world to get the {@link WorldGenConfig} for
     * @param defaults A map of paths to their default values to be populated
     *                 for the WorldGenConfig
     * @return The {@link WorldGenConfig}
     */
    public WorldGenConfig getWorldGenConfig(String world, HashMap<String, Object> defaults) {
        ConfigurationSection worldConfigurationSection;
        if (mainWorldsSection.contains(world)) {
            worldConfigurationSection = mainWorldsSection.getConfigurationSection(world);
        } else {
            worldConfigurationSection = mainWorldsSection.createSection(world, defaults);
        }
        return new WorldGenConfig(worldConfigurationSection, defaults);
    }

    public abstract BukkitAbstractGenManager getGeneratorManager();
}
