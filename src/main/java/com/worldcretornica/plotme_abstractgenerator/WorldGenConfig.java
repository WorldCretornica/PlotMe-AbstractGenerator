package com.worldcretornica.plotme_abstractgenerator;

import com.worldcretornica.configuration.MemorySection;
import com.worldcretornica.plotme_abstractgenerator.bukkit.BukkitBlockRepresentation;

import java.util.List;

public class WorldGenConfig extends MemorySection {

    //TODO Rethink the need for this class

    private BukkitBlockRepresentation getBlockRepresentation(String string) {
        return new BukkitBlockRepresentation(getString(string));
    }

    public BukkitBlockRepresentation getBlockRepresentation(WorldConfigPath wcp) {
        return getBlockRepresentation(wcp.key());
    }

    public boolean contains(WorldConfigPath wcp) {
        return contains(wcp.key());
    }

    public Object get(WorldConfigPath wcp) {
        return get(wcp.key());
    }

    public Object get(WorldConfigPath wcp, Object def) {
        return get(wcp.key(), def);
    }

    public void set(WorldConfigPath wcp, Object value) {
        set(wcp.key(), value);
    }

    public String getString(WorldConfigPath wcp) {
        return getString(wcp.key());
    }

    public String getString(WorldConfigPath wcp, String string1) {
        return getString(wcp.key(), string1);
    }

    public int getInt(WorldConfigPath wcp) {
        return getInt(wcp.key());
    }

    public int getInt(WorldConfigPath wcp, int def) {
        return getInt(wcp.key(), def);
    }

    public boolean getBoolean(WorldConfigPath wcp) {
        return getBoolean(wcp.key());
    }

    public boolean getBoolean(WorldConfigPath wcp, boolean def) {
        return getBoolean(wcp.key(), def);
    }

    public double getDouble(WorldConfigPath wcp) {
        return getDouble(wcp.key());
    }

    public double getDouble(WorldConfigPath wcp, double def) {
        return getDouble(wcp.key(), def);
    }

    public long getLong(WorldConfigPath wcp) {
        return getLong(wcp.key());
    }

    public long getLong(WorldConfigPath wcp, long def) {
        return getLong(wcp.key(), def);
    }

    public List<?> getList(WorldConfigPath wcp) {
        return getList(wcp.key());
    }

    public List<?> getList(WorldConfigPath wcp, List<?> def) {
        return getList(wcp.key(), def);
    }

    public List<String> getStringList(WorldConfigPath wcp) {
        return getStringList(wcp.key());
    }

    public List<Integer> getIntegerList(WorldConfigPath wcp) {
        return getIntegerList(wcp.key());
    }

    public List<Boolean> getBooleanList(WorldConfigPath wcp) {
        return getBooleanList(wcp.key());
    }

    public List<Double> getDoubleList(WorldConfigPath wcp) {
        return getDoubleList(wcp.key());
    }

    public List<Float> getFloatList(WorldConfigPath wcp) {
        return getFloatList(wcp.key());
    }

    public List<Long> getLongList(WorldConfigPath wcp) {
        return getLongList(wcp.key());
    }

    public List<Byte> getByteList(WorldConfigPath wcp) {
        return getByteList(wcp.key());
    }

    public boolean isConfigurationSection(WorldConfigPath wcp) {
        return isConfigurationSection(wcp.key());
    }

}
