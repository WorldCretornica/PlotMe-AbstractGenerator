package com.worldcretornica.plotme_abstractgenerator;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public interface AbstractGenerator {

    Logger getLogger();

    File getDataFolder();

    File getConfigFolder();

    InputStream getResource(String fileName);

    void saveResource(String fileName, boolean b);

    WorldGenConfig getWorldGenConfig(String worldname);

}
