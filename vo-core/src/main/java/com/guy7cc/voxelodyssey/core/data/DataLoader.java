/*
 * Copyright (C) 2025 TeamVoxelOdyssey
 *
 * This file is part of VoxelOdysseyPlugins.
 *
 * VoxelOdysseyPlugins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VoxelOdysseyPlugins is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VoxelOdysseyPlugins. If not, see <https://www.gnu.org/licenses/>.
 */
package com.guy7cc.voxelodyssey.core.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.common.ExecutionOrder;
import com.guy7cc.voxelodyssey.core.common.PluginLifecycleListener;
import com.guy7cc.voxelodyssey.core.util.ReflectionUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Handles the loading and saving of data files using Gson.
 * It collects data holders, loads data from a file, and saves data to a file.
 */
public class DataLoader implements PluginLifecycleListener {
    private final JavaPlugin plugin;
    private final File file;
    private final JsonFileIO gson;
    private final String version;
    private final java.util.List<DataHolder> holders = new ArrayList<>();

    private InvalidDataReason reason = null;

    public DataLoader(JavaPlugin plugin, File file, JsonFileIO json, String version) {
        this.plugin = plugin;
        this.file = file;
        this.gson = json;
        this.version = version;
    }

    /**
     * Collects all DataHolder instances from the fields of the given object.
     *
     * @param obj the object to collect DataHolder instances from
     */
    public void collectHolders(Object obj){
        java.util.List<Object> list = ReflectionUtil.collectFields(obj, o -> o instanceof DataHolder, plugin.getLogger());
        holders.addAll(list.stream().map(o -> (DataHolder) o).toList());
    }

    /**
     * Collects all static DataHolder instances from the given class.
     *
     * @param clazz the class to collect static DataHolder instances from
     */
    public void collectStaticHolders(Class<?> clazz){
        java.util.List<Object> list = ReflectionUtil.collectStaticFields(clazz, o -> o instanceof DataHolder, plugin.getLogger());
        holders.addAll(list.stream().map(o -> (DataHolder) o).toList());
    }

    /**
     * Gets the version of the data file.
     *
     * @return the version of the data file
     */
    public String getVersion(){
        return version;
    }

    @Override
    @ExecutionOrder(ExecutionOrder.Order.LATE)
    public void onPluginEnabled(JavaPlugin plugin) {
        load();
    }

    @Override
    @ExecutionOrder(ExecutionOrder.Order.LATE)
    public void onPluginDisabled(JavaPlugin plugin) {
        save();
    }

    /**
     * Loads data from the file.
     * If the file is empty or broken, it marks the data as invalid.
     * If the version does not match, it marks the data as old.
     * If the data is valid, it loads the data into the holders.
     */
    public void load() {
        if(file.exists()){
            JsonElement element = gson.load(file);
            if(element == null || !element.isJsonObject()){
                markAsInvalid(InvalidDataReason.BROKEN);
                return;
            }
            JsonObject data = element.getAsJsonObject();
            if (!matchVersion(data)) {
                markAsInvalid(InvalidDataReason.OLD);
                return;
            }
            for (var holder : holders) {
                if (data.has(holder.getHolderName()) && data.get(holder.getHolderName()).isJsonObject())
                    holder.load(data.getAsJsonObject(holder.getHolderName()));
                else {
                    markAsInvalid(InvalidDataReason.BROKEN);
                    return;
                }
            }
        } else {
            markAsInvalid(InvalidDataReason.EMPTY);
            return;
        }
    }

    /**
     * Saves data to the file.
     * It creates a new JsonObject, adds the version and the data from the holders, and saves it to the file.
     */
    public void save() {
        JsonObject data = new JsonObject();
        data.addProperty("version", getVersion());
        for (var holder : holders) {
            data.add(holder.getHolderName(), holder.getWrittenData());
        }
        gson.save(data, file);
    }

    /**
     * Marks the data as invalid and logs the reason.
     * If the reason requires renaming, it renames the file.
     *
     * @param reason the reason for marking the data as invalid
     */
    public void markAsInvalid(InvalidDataReason reason){
        if(this.reason != null) return;
        this.reason = reason;
        if(reason.renameRequired()) rename();
        plugin.getLogger().warning(reason.getMessage());
        reset();
    }

    private boolean matchVersion(JsonObject data) {
        String key = "version";
        return data.has(key)
                && data.get(key).isJsonPrimitive()
                && data.get(key).getAsString().equals(getVersion());
    }

    private boolean rename(){
        if(reason == null || !reason.renameRequired()) return true;
        File to = reason.addSuffix(file);
        try {
            if (!file.renameTo(to)) {
                plugin.getLogger().severe("Cannot rename data file to " + to.getName());
                return false;
            }
            return true;
        } catch(SecurityException e) {
            plugin.getLogger().log(
                    Level.SEVERE,
                    String.format("Could not move and rename data file to %s due to security problem.", to.getName()),
                    e
            );
        }
        return false;
    }

    private void reset() {
        for (var holder : holders) {
            holder.load(new JsonObject());
        }
    }
}
