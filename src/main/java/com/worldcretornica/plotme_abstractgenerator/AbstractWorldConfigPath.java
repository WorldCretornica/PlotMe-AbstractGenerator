package com.worldcretornica.plotme_abstractgenerator;

public enum AbstractWorldConfigPath implements WorldConfigPath {

    PLOT_SIZE("PlotSize", 16),
    X_TRANSLATION("XTranslation", 0),
    Z_TRANSLATION("ZTranslation", 0),
    GROUND_LEVEL("GroundHeight", 64),
    BASE_BLOCK("BottomBlock", "7"),
    FILL_BLOCK("FillBlock", "3");

    public final String path;
    public final Object def;

    private AbstractWorldConfigPath(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public Object def() {
        return def;
    }

    @Override
    public String toString() {
        return path;
    }

}
