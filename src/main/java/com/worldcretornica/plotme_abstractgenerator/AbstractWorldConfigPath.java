package com.worldcretornica.plotme_abstractgenerator;

public enum AbstractWorldConfigPath implements WorldConfigPath {

    PLOT_SIZE("PlotSize", 32),
    X_TRANSLATION("XTranslation", 0),
    Z_TRANSLATION("ZTranslation", 0),
    GROUND_LEVEL("GroundHeight", 64),
    FILL_BLOCK("FillBlock", "3");

    private final String path;
    private final Object value;

    AbstractWorldConfigPath(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    @Override
    public String key() {
        return path;
    }

    @Override
    public Object value() {
        return value;
    }

}
