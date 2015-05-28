package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_abstractgenerator.AbstractGenerator;
import com.worldcretornica.plotme_core.AbstractSchematicUtil;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class BukkitAbstractGenerator extends JavaPlugin implements AbstractGenerator, Listener {

    public final PlotMe_CorePlugin plotMePlugin = PlotMe_CorePlugin.getInstance();
    private final Map<String, ConfigurationSection> worldConfigs = new HashMap<>();
    private final File configFolder = new File(new File("plugins", "PlotMe"), getName());
    public ConfigurationSection mainWorldsSection;
    private ConfigAccessor configCA;

    @Override
    public final void onEnable() {
        setupConfigFolders();
        setupConfig();
        PluginManager pm = Bukkit.getPluginManager();
        if (plotMePlugin != null) {
            getLogger().severe("Something went extremely wrong.");
            this.getPluginLoader().disablePlugin(this);
        }
        for (World world : getServer().getWorlds()) {
            getServer().unloadWorld(world, false);
        }
        initialize();
    }

    public AbstractSchematicUtil getSchematicUtil() {
        return this.plotMePlugin.getAPI().getSchematicUtil();
    }

    /**
     * Called when this plugin is enabled.
     */
    public abstract void initialize();

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
        configCA = new ConfigAccessor(getPluginFolder(), "config.yml");

        if (getConfiguration().contains("worlds")) {
            mainWorldsSection = getConfiguration().getConfigurationSection("worlds");
        } else {
            mainWorldsSection = getConfiguration().createSection("worlds");
        }
        configCA.saveConfig();
    }

    public ConfigurationSection putWGC(String world, ConfigurationSection worldGenConfig) {
        return worldConfigs.put(world.toLowerCase(), worldGenConfig);
    }

    @EventHandler
    public void onWorldEnable(WorldInitEvent event) {
        worldLoadEvent(event.getWorld());
    }

    protected abstract void worldLoadEvent(World world);

}
