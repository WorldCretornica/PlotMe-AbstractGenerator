package com.worldcretornica.plotme_abstractgenerator.bukkit;

import com.worldcretornica.configuration.file.FileConfiguration;
import com.worldcretornica.configuration.file.YamlConfiguration;
import com.worldcretornica.plotme_abstractgenerator.AbstractGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BukkitConfigAccessor {

    private final String fileName;
    private final AbstractGenerator plugin;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    public BukkitConfigAccessor(AbstractGenerator plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null) {
            throw new IllegalStateException();
        }
        this.configFile = new File(plugin.getConfigFolder(), fileName);
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource(fileName, false);
        }
    }

    private void saveResource(String fileName, boolean b) {
        this
    }
}
