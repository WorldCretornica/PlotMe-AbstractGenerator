package com.worldcretornica.plotme_abstractgenerator;

public enum AbstractWorldConfigPath implements WorldConfigPath {

    PLOT_SIZE("PlotSize", 32),
    X_TRANSLATION("XTranslation", 0),
    Z_TRANSLATION("ZTranslation", 0),
    GROUND_LEVEL("GroundHeight", 64),
    FILL_BLOCK("FillBlock", "3");

    public final String path;
    public final Object def;

    AbstractWorldConfigPath(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    @Override
    public String key() {
        return path;
    }

    @Override
    public Object def() {
        return def;
    }

}
