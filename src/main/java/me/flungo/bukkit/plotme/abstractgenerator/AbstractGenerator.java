package me.flungo.bukkit.plotme.abstractgenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import com.worldcretornica.plotme_core.api.IConfigSection;

public abstract class AbstractGenerator {

    public static final String CORE_PLUGIN_NAME = "PlotMe";
    public static final String CORE_CONFIG_NAME = "core-config.yml";
    public static final String CORE_LANG_PATH = "Language";
    public static final String CORE_DEFAULT_LANG = "english";
    public static final String CORE_CAPTIONS_PATTERN = "caption-%s.yml";
    public static final String DEFAULT_CONFIG_NAME = "config.yml";
    public static final String DEFAULT_CAPTIONS_FILE = "caption-english.yml";
    public static final String WORLDS_CONFIG_SECTION = "worlds";

    private File coreFolder;
    private File configFolder;

    private File coreConfigFile;

    private IConfigSection coreConfig;
    private HashMap<String, IConfigSection> coreCaptions;
    private ConfigAccessor configCA;
    protected ConfigAccessor captionsCA;

    public final void onEnable() {
        setupConfigFolders();
        setupConfig();
        initialize();
    }

    /**
     * Called when this plugin is enabled.
     */
    public abstract void initialize();

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
        coreFolder = new File(pluginsFolder, CORE_PLUGIN_NAME);
        coreConfigFile = new File(coreFolder, CORE_CONFIG_NAME);
        configFolder = new File(coreFolder, getName());
        configFolder.mkdirs();
    }

    /**
     * Returns the folder that the core PlotMe plugin's data files are located
     * in.
     *
     * @return The folder that the core PlotMe plugin's data files are located
     * in
     */
    public File getCoreFolder() {
        return coreFolder;
    }

    /**
     * Discards any data in {@link #getCoreConfig()} and reloads from disk.
     */
    public void reloadCoreConfig() {
        coreConfig = YamlConfiguration.loadConfiguration(coreConfigFile);
    }

    /**
     * Gets a <code>FileConfiguration</code> for the PlotMe core plugin, read
     * from "config.yml" in the PlotMe core data folder.
     *
     * @return PlotMe Core configuration
     */
    public FileConfiguration getCoreConfig() {
        if (coreConfig == null) {
            reloadCoreConfig();
        }
        return coreConfig;
    }

    /**
     * Saves the <code>FileConfiguration</code> retrievable by
     * {@link #getCoreConfig()}.
     */
    public void saveCoreConfig() {
        if (coreConfig == null || coreConfigFile == null) {
            return;
        }
        try {
            getConfig().save(coreConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + coreConfigFile, ex);
        }
    }

    private File getCoreCaptionsFile(String lang) {
        return new File(coreFolder, String.format(CORE_CAPTIONS_PATTERN, lang));
    }

    /**
     * Discards any data in {@link #getCoreCaptions()} and reloads from disk.
     * <p>
     * Reloads the default PlotMe plugin language.
     */
    public void reloadCoreCaptions() {
        reloadCoreCaptions(getCoreConfig().getString(CORE_LANG_PATH, CORE_DEFAULT_LANG));
    }

    /**
     * Discards any data in {@link #getCoreCaptions(String lang)} and reloads
     * from disk.
     *
     * @param lang The language to reload
     */
    public void reloadCoreCaptions(String lang) {
        coreCaptions.put(lang, YamlConfiguration.loadConfiguration(getCoreCaptionsFile(lang)));
    }

    /**
     * Gets a <code>FileConfiguration</code> for the core PlotMe plugin's
     * caption file for the default language.
     *
     * @return PlotMe Core captions configuration
     */
    public FileConfiguration getCoreCaptions() {
        return getCoreCaptions(getCoreConfig().getString(CORE_LANG_PATH, CORE_DEFAULT_LANG));
    }

    /**
     * Gets a <code>FileConfiguration</code> for the core PlotMe plugin's
     * caption file for the specified language.
     *
     * @param lang The language to get the captions for
     * @return PlotMe Core captions configuration
     */
    public FileConfiguration getCoreCaptions(String lang) {
        if (!coreCaptions.containsKey(lang)) {
            reloadCoreConfig();
        }
        return coreCaptions.get(lang);
    }

    /**
     * Saves the <code>FileConfiguration</code> retrievable by
     * {@link #getCoreCaptions()}.
     */
    public void saveCoreCaptions() {
        saveCoreCaptions(getCoreConfig().getString(CORE_LANG_PATH, CORE_DEFAULT_LANG));
    }

    /**
     * Saves the <code>FileConfiguration</code> retrievable by
     * {@link #getCoreCaptions(java.lang.String) }.
     *
     * @param lang The language to save the captions for
     */
    public void saveCoreCaptions(String lang) {
        if (!coreCaptions.containsKey(lang)) {
            return;
        }
        try {
            coreCaptions.get(lang).save(getCoreCaptionsFile(lang));
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + coreConfigFile, ex);
        }
    }

    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder
     */
    public File getConfigFolder() {
        return configFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reloadConfig() {
        configCA.reloadConfig();
    }

    /**
     * Gets a {@link FileConfiguration} for this plugin, read through
     * "config.yml"
     * <p>
     * If there is a default config.yml embedded in this plugin, it will be
     * provided as a default for this Configuration.
     *
     * @return Plugin configuration
     */
    @Override
    public IConfigSection getConfig() {
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
        configCA = new ConfigAccessor(this, DEFAULT_CONFIG_NAME);

        // Set defaults for WorldGenConfig
        for (AbstractWorldConfigPath configPath : AbstractWorldConfigPath.values()) {
            WorldGenConfig.putDefault(configPath);
        }

        // Set the config accessor for the main caption-english.yml
        captionsCA = new ConfigAccessor(this, DEFAULT_CAPTIONS_FILE);
        // Save default config into file.
        captionsCA.saveConfig();
    }

    /**
     * Gets a {@link WorldGenConfig} for the specified world with just the
     * global defaults for {@link WorldGenConfig}.
     *
     * @param world The world to get the {@link WorldGenConfig} for
     * @return The {@link WorldGenConfig}
     * @see #getWorldGenConfig(java.lang.String, java.util.HashMap)
     */
    protected WorldGenConfig getWorldGenConfig(String world) {
        return getWorldGenConfig(world, new HashMap<String, Object>());
    }

    /**
     * Gets a {@link WorldGenConfig} for the specified world with default set as
     * specified in the HashMap which maps config paths to default values to be
     * added to or override the global defaults for {@link WorldGenConfig}.
     *
     * @param world The world to get the {@link WorldGenConfig} for
     * @param defaults A map of paths to their default values to be populated
     * for the WorldGenConfig
     * @return The {@link WorldGenConfig}
     */
    protected WorldGenConfig getWorldGenConfig(String world, HashMap<String, Object> defaults) {
        ConfigurationSection worldsConfigurationSection;
        if (getConfig().contains(WORLDS_CONFIG_SECTION)) {
            worldsConfigurationSection = getConfig().getConfigurationSection(WORLDS_CONFIG_SECTION);
        } else {
            worldsConfigurationSection = getConfig().createSection(WORLDS_CONFIG_SECTION);
        }
        ConfigurationSection worldConfigurationSection;
        if (worldsConfigurationSection.contains(world)) {
            worldConfigurationSection = worldsConfigurationSection.getConfigurationSection(world);
        } else {
            worldConfigurationSection = worldsConfigurationSection.createSection(world);
        }
        return new WorldGenConfig(worldConfigurationSection, defaults);
    }

    public abstract AbstractGenManager getGeneratorManager();
}
