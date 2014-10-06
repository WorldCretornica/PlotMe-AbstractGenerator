/*
 * Copyright (C) 2013 Fabrizio Lungo <fab@lungo.co.uk> - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Fabrizio Lungo <fab@lungo.co.uk>, November 2013
 */
package com.worldcretornica.plotme_abstractgenerator;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.worldcretornica.plotme_abstractgenerator.bukkit.BukkitBlockRepresentation;

import java.util.*;

/**
 * Represents the generation configuration for a single world.
 *
 * @author Fabrizio Lungo <fab@lungo.co.uk>
 */
public final class WorldGenConfig implements ConfigurationSection {

    private static HashMap<String, Object> DEFAULTS = new HashMap<>();

    private final ConfigurationSection world;

    /**
     * Creates a {@link WorldGenConfig} by wrapping a
     * <tt>ConfigurationSection</tt> that represents the generation
     * configuration for that world.
     *
     * @param world the <tt>ConfigurationSection</tt>
     */
    public WorldGenConfig(ConfigurationSection world) {
        this(world, new HashMap<String, Object>());
    }

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
     * Returns the number of items specified in the global default mapping for
     * {@link WorldGenConfig}s.
     *
     * @return The number of default values
     * @see Map#size()
     */
    public static int defaultSize() {
        return DEFAULTS.size();
    }

    /**
     * Returns <tt>true</tt> if there are no default mappings for
     * {@link WorldGenConfig}s.
     *
     * @return <tt>true</tt> if there are no default mappings for
     * {@link WorldGenConfig}s.
     * @see Map#isEmpty()
     */
    public static boolean isDefaultEmpty() {
        return DEFAULTS.isEmpty();
    }

    /**
     * Returns the default value to which the specified path is mapped, or
     * {@code null} if there is no default for the path.
     *
     * @param path the path whose default value is to be returned
     * @return the default value to which the specified path is mapped
     * @see Map#get(java.lang.Object)
     */
    public static Object getDefault(String path) {
        return DEFAULTS.get(path);
    }

    /**
     * Returns the default value to which the specified path is mapped, or
     * {@code null} if there is no default for the path.
     *
     * @param wcp the {@link WorldConfigPath} whose default value is to be
     * returned
     * @return the default value to which the specified path is mapped
     * @see #getDefault(java.lang.String)
     */
    public static Object getDefault(WorldConfigPath wcp) {
        return getDefault(wcp.path());
    }

    /**
     * Returns <tt>true</tt> if there is a default default for the specified
     * <tt>path</tt>.
     *
     * @param path The path whose presence in the {@link WorldGenConfig}
     * defaults is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * path.
     * @see Map#containsKey(java.lang.Object)
     */
    public static boolean defaultContainsPath(String path) {
        return DEFAULTS.containsKey(path);
    }

    /**
     * Returns <tt>true</tt> if there is a default default for the specified
     * {@link WorldConfigPath}.
     *
     * @param wcp The {@link WorldConfigPath} whose presence in the
     * {@link WorldGenConfig} defaults is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * path.
     * @see #defaultContainsPath(java.lang.String)
     */
    public static boolean defaultContainsPath(WorldConfigPath wcp) {
        return defaultContainsPath(wcp.path());
    }

    /**
     * Associates the specified value with the specified path as the default for
     * {@link WorldGenConfig}s. If the map previously contained a default for
     * the path, the old value is replaced by the specified value.
     *
     * @param path path with which the specified default is to be associated
     * @param value default value to be associated with the specified path
     * @return the previous default value associated with <tt>path</tt>
     * @see Map#put(java.lang.Object, java.lang.Object)
     */
    public static Object putDefault(String path, Object value) {
        return DEFAULTS.put(path, value);
    }

    /**
     * Associates the specified value with the specified {@link WorldConfigPath}
     * as the default for {@link WorldGenConfig}s. If the map previously
     * contained a default for the path, the old value is replaced by the
     * specified value.
     *
     * @param wcp {@link WorldConfigPath} with which the specified default is to
     * be associated
     * @param value default value to be associated with the specified path
     * @return the previous default value associated with
     * {@link WorldConfigPath}
     * @see Map#put(java.lang.Object, java.lang.Object)
     */
    public static Object putDefault(WorldConfigPath wcp, Object value) {
        return putDefault(wcp.path(), value);
    }

    /**
     * Associates the specified {@link WorldConfigPath}'s default with the
     * specified {@link WorldConfigPath} as the default for
     * {@link WorldGenConfig}s. If the map previously contained a default for
     * the path, the old value is replaced by the specified value.
     *
     * @param wcp {@link WorldConfigPath} with which the specified default is to
     * be associated and obtained from
     * @return the previous default value associated with
     * {@link WorldConfigPath}
     */
    public static Object putDefault(WorldConfigPath wcp) {
        return putDefault(wcp.path(), wcp.def());
    }

    /**
     * Copies all of the mappings from the specified map to the
     * {@link WorldGenConfig} defaults.
     *
     * @param m mappings to be stored as defaults
     * @see Map#putAll(java.util.Map)
     */
    public static void putAllDefaults(Map<? extends String, ?> m) {
        DEFAULTS.putAll(m);
    }

    /**
     * Removes the default for a path if it is present
     *
     * @param path path whose default is to be removed from the defaults
     * @return the previous value associated with <tt>path</tt>, or
     * <tt>null</tt> if there was no default for <tt>path</tt>.
     * @see Map#remove(java.lang.Object)
     */
    public static Object removeDefault(String path) {
        return DEFAULTS.remove(path);
    }

    /**
     * Removes the default for a {@link WorldConfigPath} if it is present
     *
     * @param wcp {@link WorldConfigPath} whose default is to be removed from
     * the defaults
     * @return the previous value associated with <tt>wcp</tt>, or
     * <tt>null</tt> if there was no default for <tt>wcp</tt>.
     * @see #removeDefault(java.lang.String)
     */
    public static Object removeDefault(WorldConfigPath wcp) {
        return DEFAULTS.remove(wcp.path());
    }

    /**
     * Removes all of the defaults from {@link WorldGenConfig}s
     */
    public static void clearDefaults() {
        DEFAULTS.clear();
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more paths to the specified
     * value.
     *
     * @param value value whose presence as a default is to be tested
     * @return <tt>true</tt> if this map maps one or more paths to the specified
     * value.
     */
    public static boolean containsDefaultValue(Object value) {
        return DEFAULTS.containsValue(value);
    }

    /**
     * Returns a clone of the Map representing the defaults
     *
     * @return a clone of the Map representing the defaults
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> cloneDefaults() {
        return (HashMap<String, Object>) DEFAULTS.clone();
    }

    /**
     * Returns a {@link Set} view of the paths which defaults are defined for.
     *
     * @return
     */
    public static Set<String> defaultPathSet() {
        return DEFAULTS.keySet();
    }

    /**
     * Returns a {@link Collection} view of the default values defined for
     * {@link WorldGenConfig}.
     *
     * @return a {@link Collection} view of the default values defined for
     * {@link WorldGenConfig}.
     */
    public static Collection<Object> defaultValues() {
        return DEFAULTS.values();
    }

    /**
     * Returns a {@link Set} view of the mappings from path to default value
     * defined for {@link WorldGenConfig}s.
     *
     * @return a {@link Set} view of the mappings from path to default value
     * defined for {@link WorldGenConfig}s.
     */
    public static Set<Map.Entry<String, Object>> defaultEntrySet() {
        return DEFAULTS.entrySet();
    }

    public BukkitBlockRepresentation getBlockRepresentation(String string) {
        return new BukkitBlockRepresentation(getString(string));
    }

    public BukkitBlockRepresentation getBlockRepresentation(WorldConfigPath wcp) {
        return getBlockRepresentation(wcp.path());
    }

    public boolean isBlockRepresentation(String string) {
        try {
            getBlockRepresentation(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public boolean isBlockRepresentation(WorldConfigPath wcp) {
        return isBlockRepresentation(wcp.path());
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
        return contains(wcp.path());
    }

    @Override
    public boolean isSet(String string) {
        return world.isSet(string);
    }

    public boolean isSet(WorldConfigPath wcp) {
        return isSet(wcp.path());
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
        return get(wcp.path());
    }

    @Override
    public Object get(String string, Object o) {
        return world.get(string, o);
    }

    public Object get(WorldConfigPath wcp, Object o) {
        return get(wcp.path(), o);
    }

    @Override
    public void set(String string, Object o) {
        if (o instanceof BukkitBlockRepresentation) {
            o = ((BukkitBlockRepresentation) o).getBlockIdValue();
        }
        world.set(string, o);
    }

    public void set(WorldConfigPath wcp, Object o) {
        set(wcp.path(), o);
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
        return getString(wcp.path());
    }

    @Override
    public String getString(String string, String string1) {
        return world.getString(string, string1);
    }

    public String getString(WorldConfigPath wcp, String string1) {
        return getString(wcp.path(), string1);
    }

    @Override
    public boolean isString(String string) {
        return world.isString(string);
    }

    public boolean isString(WorldConfigPath wcp) {
        return isString(wcp.path());
    }

    @Override
    public int getInt(String string) {
        return world.getInt(string);
    }

    public int getInt(WorldConfigPath wcp) {
        return getInt(wcp.path());
    }

    @Override
    public int getInt(String string, int i) {
        return world.getInt(string, i);
    }

    public int getInt(WorldConfigPath wcp, int i) {
        return getInt(wcp.path(), i);
    }

    @Override
    public boolean isInt(String string) {
        return world.isInt(string);
    }

    public boolean isInt(WorldConfigPath wcp) {
        return isInt(wcp.path());
    }

    @Override
    public boolean getBoolean(String string) {
        return world.getBoolean(string);
    }

    public boolean getBoolean(WorldConfigPath wcp) {
        return getBoolean(wcp.path());
    }

    @Override
    public boolean getBoolean(String string, boolean bln) {
        return world.getBoolean(string, bln);
    }

    public boolean getBoolean(WorldConfigPath wcp, boolean bln) {
        return getBoolean(wcp.path(), bln);
    }

    @Override
    public boolean isBoolean(String string) {
        return world.isBoolean(string);
    }

    public boolean isBoolean(WorldConfigPath wcp) {
        return isBoolean(wcp.path());
    }

    @Override
    public double getDouble(String string) {
        return world.getDouble(string);
    }

    public double getDouble(WorldConfigPath wcp) {
        return getDouble(wcp.path());
    }

    @Override
    public double getDouble(String string, double d) {
        return world.getDouble(string, d);
    }

    public double getDouble(WorldConfigPath wcp, double d) {
        return getDouble(wcp.path(), d);
    }

    @Override
    public boolean isDouble(String string) {
        return world.isDouble(string);
    }

    public boolean isDouble(WorldConfigPath wcp) {
        return isDouble(wcp.path());
    }

    @Override
    public long getLong(String string) {
        return world.getLong(string);
    }

    public long getLong(WorldConfigPath wcp) {
        return getLong(wcp.path());
    }

    @Override
    public long getLong(String string, long l) {
        return world.getLong(string, l);
    }

    public long getLong(WorldConfigPath wcp, long l) {
        return getLong(wcp.path(), l);
    }

    @Override
    public boolean isLong(String string) {
        return world.isLong(string);
    }

    public boolean isLong(WorldConfigPath wcp) {
        return isLong(wcp.path());
    }

    @Override
    public List<?> getList(String string) {
        return world.getList(string);
    }

    public List<?> getList(WorldConfigPath wcp) {
        return getList(wcp.path());
    }

    @Override
    public List<?> getList(String string, List<?> list) {
        return world.getList(string, list);
    }

    public List<?> getList(WorldConfigPath wcp, List<?> list) {
        return getList(wcp.path(), list);
    }

    @Override
    public boolean isList(String string) {
        return world.isList(string);
    }

    public boolean isList(WorldConfigPath wcp) {
        return isList(wcp.path());
    }

    @Override
    public List<String> getStringList(String string) {
        return world.getStringList(string);
    }

    public List<String> getStringList(WorldConfigPath wcp) {
        return getStringList(wcp.path());
    }

    @Override
    public List<Integer> getIntegerList(String string) {
        return world.getIntegerList(string);
    }

    public List<Integer> getIntegerList(WorldConfigPath wcp) {
        return getIntegerList(wcp.path());
    }

    @Override
    public List<Boolean> getBooleanList(String string) {
        return world.getBooleanList(string);
    }

    public List<Boolean> getBooleanList(WorldConfigPath wcp) {
        return getBooleanList(wcp.path());
    }

    @Override
    public List<Double> getDoubleList(String string) {
        return world.getDoubleList(string);
    }

    public List<Double> getDoubleList(WorldConfigPath wcp) {
        return getDoubleList(wcp.path());
    }

    @Override
    public List<Float> getFloatList(String string) {
        return world.getFloatList(string);
    }

    public List<Float> getFloatList(WorldConfigPath wcp) {
        return getFloatList(wcp.path());
    }

    @Override
    public List<Long> getLongList(String string) {
        return world.getLongList(string);
    }

    public List<Long> getLongList(WorldConfigPath wcp) {
        return getLongList(wcp.path());
    }

    @Override
    public List<Byte> getByteList(String string) {
        return world.getByteList(string);
    }

    public List<Byte> getByteList(WorldConfigPath wcp) {
        return getByteList(wcp.path());
    }

    @Override
    public List<Character> getCharacterList(String string) {
        return world.getCharacterList(string);
    }

    public List<Character> getCharacterList(WorldConfigPath wcp) {
        return getCharacterList(wcp.path());
    }

    @Override
    public List<Short> getShortList(String string) {
        return world.getShortList(string);
    }

    public List<Short> getShortList(WorldConfigPath wcp) {
        return getShortList(wcp.path());
    }

    @Override
    public List<Map<?, ?>> getMapList(String string) {
        return world.getMapList(string);
    }

    public List<Map<?, ?>> getMapList(WorldConfigPath wcp) {
        return getMapList(wcp.path());
    }

    @Override
    public Vector getVector(String string) {
        return world.getVector(string);
    }

    public Vector getVector(WorldConfigPath wcp) {
        return getVector(wcp.path());
    }

    @Override
    public Vector getVector(String string, Vector vector) {
        return world.getVector(string, vector);
    }

    public Vector getVector(WorldConfigPath wcp, Vector vector) {
        return getVector(wcp.path(), vector);
    }

    @Override
    public boolean isVector(String string) {
        return world.isVector(string);
    }

    public boolean isVector(WorldConfigPath wcp) {
        return isVector(wcp.path());
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String string) {
        return world.getOfflinePlayer(string);
    }

    public OfflinePlayer getOfflinePlayer(WorldConfigPath wcp) {
        return getOfflinePlayer(wcp.path());
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String string, OfflinePlayer op) {
        return world.getOfflinePlayer(string, op);
    }

    public OfflinePlayer getOfflinePlayer(WorldConfigPath wcp, OfflinePlayer op) {
        return getOfflinePlayer(wcp.path(), op);
    }

    @Override
    public boolean isOfflinePlayer(String string) {
        return world.isOfflinePlayer(string);
    }

    public boolean isOfflinePlayer(WorldConfigPath wcp) {
        return isOfflinePlayer(wcp.path());
    }

    @Override
    public ItemStack getItemStack(String string) {
        return world.getItemStack(string);
    }

    public ItemStack getItemStack(WorldConfigPath wcp) {
        return getItemStack(wcp.path());
    }

    @Override
    public ItemStack getItemStack(String string, ItemStack is) {
        return world.getItemStack(string, is);
    }

    public ItemStack getItemStack(WorldConfigPath wcp, ItemStack is) {
        return getItemStack(wcp.path(), is);
    }

    @Override
    public boolean isItemStack(String string) {
        return world.isItemStack(string);
    }

    public boolean isItemStack(WorldConfigPath wcp) {
        return isItemStack(wcp.path());
    }

    @Override
    public Color getColor(String string) {
        return world.getColor(string);
    }

    public Color getColor(WorldConfigPath wcp) {
        return getColor(wcp.path());
    }

    @Override
    public Color getColor(String string, Color color) {
        return world.getColor(string, color);
    }

    public Color getColor(WorldConfigPath wcp, Color color) {
        return getColor(wcp.path(), color);
    }

    @Override
    public boolean isColor(String string) {
        return world.isColor(string);
    }

    public boolean isColor(WorldConfigPath wcp) {
        return isColor(wcp.path());
    }

    @Override
    public ConfigurationSection getConfigurationSection(String string) {
        return world.getConfigurationSection(string);
    }

    public ConfigurationSection getConfigurationSection(WorldConfigPath wcp) {
        return getConfigurationSection(wcp.path());
    }

    @Override
    public boolean isConfigurationSection(String string) {
        return world.isConfigurationSection(string);
    }

    public boolean isConfigurationSection(WorldConfigPath wcp) {
        return isConfigurationSection(wcp.path());
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
        addDefault(wcp.path(), o);
    }

}
