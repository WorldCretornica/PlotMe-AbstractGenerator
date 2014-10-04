/*
 * Copyright (C) 2012
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.flungo.bukkit.plotme.abstractgenerator;

import java.io.File;
import java.io.InputStream;
import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.api.IServerObjectBuilder;

public class ConfigAccessor {

    private final String fileName;
    private final AbstractGenerator plugin;
    private File configFile;
    private IConfigSection fileConfiguration;
    private IServerObjectBuilder sob;

    @SuppressWarnings("deprecation")
    public ConfigAccessor(AbstractGenerator plugin, String fileName, IServerObjectBuilder sob) {
        this.sob = sob;
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (!plugin.isInitialized()) {
            throw new IllegalArgumentException("plugin must be initiaized");
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
        fileConfiguration = sob.getConfig(fileName);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            IConfigSection defConfig = sob.getConfig(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public IConfigSection getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null || configFile == null) {
            return;
        }
        
        getConfig().save(configFile);
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
