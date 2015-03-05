package com.worldcretornica.plotme_abstractgenerator;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public interface AbstractGenerator {

    Logger getLogger();

    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder
     */
    File getPluginFolder();

    InputStream getResource(String fileName);

    void saveResource(String fileName, boolean b);

    WorldGenConfig getWorldGenConfig(String worldName);

}
