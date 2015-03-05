package com.worldcretornica.plotme_abstractgenerator;

import com.worldcretornica.configuration.Configuration;
import com.worldcretornica.configuration.ConfigurationSection;
import com.worldcretornica.plotme_abstractgenerator.bukkit.BukkitBlockRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the generation configuration for a single world.
 *
 */
public final class WorldGenConfig implements ConfigurationSection {

    //TODO Rethink the need for this class

    private static final HashMap<String, Object> DEFAULTS = new HashMap<>();

    private final ConfigurationSection world;

    /**
     * Creates a {@link WorldGenConfig} by wrapping a
     * <tt>ConfigurationSection</tt> that represents the generation
     * configuration for that world and adding the default values specified in
     * the Map of paths to default values.
     *
     * @param world the <tt>ConfigurationSection</tt>
     * @param defaults a {@link Map} of default paths to default values
     */
    public WorldGenConfig(ConfigurationSection world, HashMap<String, Object> defaults) {
        this.world = world;
        HashMap<String, Object> defaults1 = DEFAULTS;
        defaults1.putAll(defaults);
        for (Map.Entry<String, Object> def : defaults1.entrySet()) {
            if (!world.contains(def.getKey())) {
                world.set(def.getKey(), def.getValue());
            }
        }
    }

    /**
     * Associates the specified value with the specified key as the default for
     * {@link WorldGenConfig}s. If the map previously contained a default for
     * the key, the old value is replaced by the specified value.
     *
     * @param key key with which the specified default is to be associated
     * @param value default value to be associated with the specified key
     * @return the previous default value associated with <tt>key</tt>
     * @see Map#put(Object, Object)
     */
    private static Object putDefault(String key, Object value) {
        return DEFAULTS.put(key, value);
    }

    /**
     * Associates the specified {@link WorldConfigPath}'s default with the
     * specified {@link WorldConfigPath} as the default for
     * {@link WorldGenConfig}s. If the map previously contained a default for
     * the key, the old value is replaced by the specified value.
     *
     * @param wcp {@link WorldConfigPath} with which the specified default is to
     * be associated and obtained from
     * @return the previous default value associated with
     * {@link WorldConfigPath}
     */
    public static Object putDefault(WorldConfigPath wcp) {
        return putDefault(wcp.key(), wcp.value());
    }

    /**
     * Removes all of the defaults from {@link WorldGenConfig}s
     */
    public static void clearDefaults() {
        DEFAULTS.clear();
    }

    public static HashMap<String, Object> defaults() {
        return DEFAULTS;
    }

    private BukkitBlockRepresentation getBlockRepresentation(String string) {
        return new BukkitBlockRepresentation(getString(string));
    }

    public BukkitBlockRepresentation getBlockRepresentation(WorldConfigPath wcp) {
        return getBlockRepresentation(wcp.key());
    }

    @Override
    public Set<String> getKeys(boolean bln) {
        return world.getKeys(bln);
    }

    @Override
    public Map<String, Object> getValues(boolean bln) {
        return world.getValues(bln);
    }

    @Override
    public boolean contains(String string) {
        return world.contains(string);
    }

    public boolean contains(WorldConfigPath wcp) {
        return contains(wcp.key());
    }

    @Override
    public boolean isSet(String string) {
        return world.isSet(string);
    }

    public boolean isSet(WorldConfigPath wcp) {
        return isSet(wcp.key());
    }

    @Override
    public String getCurrentPath() {
        return world.getCurrentPath();
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public Configuration getRoot() {
        return world.getRoot();
    }

    @Override
    public ConfigurationSection getParent() {
        return world.getParent();
    }

    @Override
    public Object get(String string) {
        return world.get(string);
    }

    public Object get(WorldConfigPath wcp) {
        return get(wcp.key());
    }

    @Override
    public Object get(String string, Object o) {
        return world.get(string, o);
    }

    public Object get(WorldConfigPath wcp, Object o) {
        return get(wcp.key(), o);
    }

    @Override
    public void set(String string, Object object) {
        if (object instanceof BukkitBlockRepresentation) {
            object = ((BukkitBlockRepresentation) object).getBlockIdValue();
        }
        world.set(string, object);
    }

    public void set(WorldConfigPath wcp, Object object) {
        set(wcp.key(), object);
    }

    @Override
    public ConfigurationSection createSection(String string) {
        return world.createSection(string);
    }

    @Override
    public ConfigurationSection createSection(String string, Map<?, ?> map) {
        return world.createSection(string, map);
    }

    @Override
    public String getString(String string) {
        return world.getString(string);
    }

    public String getString(WorldConfigPath wcp) {
        return getString(wcp.key());
    }

    @Override
    public String getString(String string, String string1) {
        return world.getString(string, string1);
    }

    public String getString(WorldConfigPath wcp, String string1) {
        return getString(wcp.key(), string1);
    }

    @Override
    public boolean isString(String string) {
        return world.isString(string);
    }

    public boolean isString(WorldConfigPath wcp) {
        return isString(wcp.key());
    }

    @Override
    public int getInt(String string) {
        return world.getInt(string);
    }

    public int getInt(WorldConfigPath wcp) {
        return getInt(wcp.key());
    }

    @Override
    public int getInt(String string, int i) {
        return world.getInt(string, i);
    }

    public int getInt(WorldConfigPath wcp, int i) {
        return getInt(wcp.key(), i);
    }

    @Override
    public boolean isInt(String string) {
        return world.isInt(string);
    }

    public boolean isInt(WorldConfigPath wcp) {
        return isInt(wcp.key());
    }

    @Override
    public boolean getBoolean(String string) {
        return world.getBoolean(string);
    }

    public boolean getBoolean(WorldConfigPath wcp) {
        return getBoolean(wcp.key());
    }

    @Override
    public boolean getBoolean(String string, boolean bln) {
        return world.getBoolean(string, bln);
    }

    public boolean getBoolean(WorldConfigPath wcp, boolean bln) {
        return getBoolean(wcp.key(), bln);
    }

    @Override
    public boolean isBoolean(String string) {
        return world.isBoolean(string);
    }

    public boolean isBoolean(WorldConfigPath wcp) {
        return isBoolean(wcp.key());
    }

    @Override
    public double getDouble(String string) {
        return world.getDouble(string);
    }

    public double getDouble(WorldConfigPath wcp) {
        return getDouble(wcp.key());
    }

    @Override
    public double getDouble(String string, double d) {
        return world.getDouble(string, d);
    }

    public double getDouble(WorldConfigPath wcp, double d) {
        return getDouble(wcp.key(), d);
    }

    @Override
    public boolean isDouble(String string) {
        return world.isDouble(string);
    }

    public boolean isDouble(WorldConfigPath wcp) {
        return isDouble(wcp.key());
    }

    @Override
    public long getLong(String string) {
        return world.getLong(string);
    }

    public long getLong(WorldConfigPath wcp) {
        return getLong(wcp.key());
    }

    @Override
    public long getLong(String string, long l) {
        return world.getLong(string, l);
    }

    public long getLong(WorldConfigPath wcp, long l) {
        return getLong(wcp.key(), l);
    }

    @Override
    public boolean isLong(String string) {
        return world.isLong(string);
    }

    public boolean isLong(WorldConfigPath wcp) {
        return isLong(wcp.key());
    }

    @Override
    public List<?> getList(String string) {
        return world.getList(string);
    }

    public List<?> getList(WorldConfigPath wcp) {
        return getList(wcp.key());
    }

    @Override
    public List<?> getList(String string, List<?> list) {
        return world.getList(string, list);
    }

    public List<?> getList(WorldConfigPath wcp, List<?> list) {
        return getList(wcp.key(), list);
    }

    @Override
    public boolean isList(String string) {
        return world.isList(string);
    }

    public boolean isList(WorldConfigPath wcp) {
        return isList(wcp.key());
    }

    @Override
    public List<String> getStringList(String string) {
        return world.getStringList(string);
    }

    public List<String> getStringList(WorldConfigPath wcp) {
        return getStringList(wcp.key());
    }

    @Override
    public List<Integer> getIntegerList(String string) {
        return world.getIntegerList(string);
    }

    public List<Integer> getIntegerList(WorldConfigPath wcp) {
        return getIntegerList(wcp.key());
    }

    @Override
    public List<Boolean> getBooleanList(String string) {
        return world.getBooleanList(string);
    }

    public List<Boolean> getBooleanList(WorldConfigPath wcp) {
        return getBooleanList(wcp.key());
    }

    @Override
    public List<Double> getDoubleList(String string) {
        return world.getDoubleList(string);
    }

    public List<Double> getDoubleList(WorldConfigPath wcp) {
        return getDoubleList(wcp.key());
    }

    @Override
    public List<Float> getFloatList(String string) {
        return world.getFloatList(string);
    }

    public List<Float> getFloatList(WorldConfigPath wcp) {
        return getFloatList(wcp.key());
    }

    @Override
    public List<Long> getLongList(String string) {
        return world.getLongList(string);
    }

    public List<Long> getLongList(WorldConfigPath wcp) {
        return getLongList(wcp.key());
    }

    @Override
    public List<Byte> getByteList(String string) {
        return world.getByteList(string);
    }

    public List<Byte> getByteList(WorldConfigPath wcp) {
        return getByteList(wcp.key());
    }

    @Override
    public List<Character> getCharacterList(String string) {
        return world.getCharacterList(string);
    }

    public List<Character> getCharacterList(WorldConfigPath wcp) {
        return getCharacterList(wcp.key());
    }

    @Override
    public List<Short> getShortList(String string) {
        return world.getShortList(string);
    }

    public List<Short> getShortList(WorldConfigPath wcp) {
        return getShortList(wcp.key());
    }

    @Override
    public List<Map<?, ?>> getMapList(String string) {
        return world.getMapList(string);
    }

    public List<Map<?, ?>> getMapList(WorldConfigPath wcp) {
        return getMapList(wcp.key());
    }

    @Override
    public ConfigurationSection getConfigurationSection(String string) {
        return world.getConfigurationSection(string);
    }

    public ConfigurationSection getConfigurationSection(WorldConfigPath wcp) {
        return getConfigurationSection(wcp.key());
    }

    @Override
    public boolean isConfigurationSection(String string) {
        return world.isConfigurationSection(string);
    }

    public boolean isConfigurationSection(WorldConfigPath wcp) {
        return isConfigurationSection(wcp.key());
    }

    @Override
    public ConfigurationSection getDefaultSection() {
        return world.getDefaultSection();
    }

    @Override
    public void addDefault(String string, Object o) {
        world.addDefault(string, o);
    }

    public void addDefault(WorldConfigPath wcp, Object o) {
        addDefault(wcp.key(), o);
    }

}
