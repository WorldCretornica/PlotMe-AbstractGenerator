package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.plotme_abstractgenerator.AbstractGenerator;
import com.worldcretornica.plotme_abstractgenerator.AbstractWorldConfigPath;
import com.worldcretornica.plotme_abstractgenerator.WorldGenConfig;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.AbstractSchematicUtil;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public abstract class BukkitAbstractGenerator extends JavaPlugin implements AbstractGenerator {

    public static final String CONFIG_NAME = "config.yml";
    private File coreFolder;
    private File configFolder;

    private BukkitConfigAccessor configCA;
    private BukkitConfigAccessor captionsCA;
    
    private AbstractSchematicUtil schematicutil;

    @Override
    public final void onEnable() {
        
        if (Bukkit.getVersion().contains("1.7")) {
            schematicutil = new com.worldcretornica.plotme_core.bukkit.v1_7.SchematicUtil(this);
        } else if (Bukkit.getVersion().contains("1.8")) {
            schematicutil = new com.worldcretornica.plotme_core.bukkit.v1_8.SchematicUtil(this);
        } else {
            getLogger().warning("This MC version is not supported yet, trying latest version!");
            schematicutil = new com.worldcretornica.plotme_core.bukkit.v1_8.SchematicUtil(this);
        }
        
        setupConfigFolders();
        setupConfig();
        initialize();
    }
    
    public AbstractSchematicUtil getSchematicUtil() {
        return this.schematicutil;
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
        getLogger().info(coreFolder.getName());
        configFolder = new File(coreFolder, getName());
        getLogger().info(configFolder.getName());
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
    @Override
    public FileConfiguration getConfig() {
        return configCA.getConfig();
    }

    /**
     * Saves the {@link FileConfiguration} retrievable by {@link #getConfig()}.
     */
    @Override
    public void saveConfig() {
        configCA.saveConfig();
    }

    /**
     * Saves the raw contents of the default config.yml file to the location
     * retrievable by {@link #getConfig()}. If there is no default config.yml
     * embedded in the plugin, an empty config.yml file is saved. This should
     * fail silently if the config.yml already exists.
     */
    @Override
    public void saveDefaultConfig() {
        configCA.saveDefaultConfig();
    }

    /**
     * Discards any data in {@link #getConfig()} and reloads from disk.
     */
    private void setupConfig() {
        // Set the config accessor for the main config.yml
        configCA = new BukkitConfigAccessor(this, CONFIG_NAME);

        // Set defaults for WorldGenConfig
        for (AbstractWorldConfigPath configPath : AbstractWorldConfigPath.values()) {
            WorldGenConfig.putDefault(configPath);
        }

        // Set the config accessor for the main caption-english.yml
        captionsCA = new BukkitConfigAccessor(this, PlotMe_Core.CAPTION_FILE);
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
        return getWorldGenConfig(worldName, new HashMap<String, Object>());
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
    protected WorldGenConfig getWorldGenConfig(String world, HashMap<String, Object> defaults) {
        ConfigurationSection worldsConfigurationSection;
        if (getConfig().contains(PlotMe_Core.WORLDS_CONFIG_SECTION)) {
            worldsConfigurationSection = getConfig().getConfigurationSection(PlotMe_Core.WORLDS_CONFIG_SECTION);
        } else {
            worldsConfigurationSection = getConfig().createSection(PlotMe_Core.WORLDS_CONFIG_SECTION);
        }
        ConfigurationSection worldConfigurationSection;
        if (worldsConfigurationSection.contains(world)) {
            worldConfigurationSection = worldsConfigurationSection.getConfigurationSection(world);
        } else {
            worldConfigurationSection = worldsConfigurationSection.createSection(world);
        }
        return new WorldGenConfig(worldConfigurationSection, defaults);
    }

    public abstract BukkitAbstractGenManager getGeneratorManager();
}
