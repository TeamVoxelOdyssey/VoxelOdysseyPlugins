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
package com.guy7cc.voxelodyssey.core.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.common.ExecutionOrder;
import com.guy7cc.voxelodyssey.core.common.PluginLifecycleListener;
import com.guy7cc.voxelodyssey.core.common.Tickable;
import com.guy7cc.voxelodyssey.core.common.WrapperContainer;
import com.guy7cc.voxelodyssey.core.data.DataHolder;
import com.guy7cc.voxelodyssey.core.data.InvalidDataReason;
import com.guy7cc.voxelodyssey.core.entity.player.VOPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

/**
 * VOEntityManager is a class that manages the creation and assignment of VOEntity objects to entities.
 */
public class VOEntityManager extends WrapperContainer<Entity, VOEntity<?>> implements DataHolder, Tickable, Listener, PluginLifecycleListener {
    private final JavaPlugin plugin;
    private JsonObject data;

    public VOEntityManager(JavaPlugin plugin) {
        super(256);
        this.plugin = plugin;
        this.data = new JsonObject();
    }

    /**
     * Spawns a new VOEntity of the specified type with the given arguments.
     *
     * @param type the type of VOEntity to spawn
     * @param args the arguments for the VOEntity
     * @return the spawned VOEntity
     * @param <T> VOEntity type
     * @param <S> VOEntityFactoryArgs type
     */
    public <T extends VOEntity<?>, S extends VOEntityFactoryArgs> T spawn(VOEntityType<T, S> type, S args) {
        T voentity = type.create(args);
        put(voentity.getHandle(), voentity);
        return voentity;
    }

    /**
     * Retrieves the VOPlayer associated with the given player.
     * @param player the player to get the VOPlayer for
     * @return the VOPlayer associated with the player, or null if not found
     */
    public VOPlayer getVOPlayer(Player player){
        VOEntity<?> voentity = get(player);
        if(voentity instanceof VOPlayer voplayer) return voplayer;
        return null;
    }

    @Override
    protected Object createContainerKey(Entity handle) {
        return handle.getUniqueId();
    }

    @Override
    protected VOEntity<?> put(Entity handle, VOEntity<?> wrapper) {
        super.put(handle, wrapper);
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(wrapper, plugin);
        return wrapper;
    }

    @Override
    protected VOEntity<?> remove(Entity handle) {
        VOEntity<?> wrapper = super.remove(handle);
        if(wrapper != null) HandlerList.unregisterAll(wrapper);
        return wrapper;
    }

    @Override
    public void tick(int globalTick) {
        Tickable.super.tick(globalTick);
    }

    @Override
    public Collection<? extends Tickable> getTickables() {
        return values();
    }

    @Override
    @ExecutionOrder(ExecutionOrder.Order.MIDDLE)
    public void onFirstTick(JavaPlugin plugin) {
        Bukkit.getOnlinePlayers().forEach(this::onPlayerJoin);
    }

    @Override
    @ExecutionOrder(ExecutionOrder.Order.MIDDLE)
    public void onPluginDisabled(JavaPlugin plugin) {
        Bukkit.getOnlinePlayers().forEach(this::onPlayerQuit);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        onPlayerJoin(event.getPlayer());
    }

    private void onPlayerJoin(Player player){
        // This type is NoOpVOPlayer.Type if VOPlayer type is not registered by another plugin
        VOEntityType<VOPlayer, VOPlayer.FactoryArgs> castedType = (VOEntityType<VOPlayer, VOPlayer.FactoryArgs>) VOCoreEntityTypes.REGISTRY.get(VOCoreEntityTypes.VOPLAYER.getKey());
        VOPlayer voplayer = castedType.wrap(player, new VOPlayer.FactoryArgs());
        put(player, voplayer);
        String key = player.getUniqueId().toString();
        if (data.has(key)) {
            try {
                voplayer.fromJson(data.getAsJsonObject(key));
                plugin.getLogger().info("Loaded player data for " + key);
            } catch (Exception exception) {
                voplayer.initialize();
                JsonElement element = data.get(key);
                data.remove(key);
                data.add(InvalidDataReason.BROKEN.addSuffix(key), element);
                plugin.getLogger().warning(String.format("Failed to load player data for %s, marked as broken", key));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onPlayerQuit(event.getPlayer());
    }

    private void onPlayerQuit(Player player){
        VOEntity<?> voentity = remove(player);
        if(voentity instanceof VOPlayer voplayer){
            String key = player.getUniqueId().toString();
            data.add(key, voplayer.toJson());
            plugin.getLogger().info("Saved player data for " + key);
        }
    }

    @Override
    public String getHolderName() {
        return "voentity";
    }

    @Override
    public void load(JsonObject data) {
        this.data = data;
    }

    @Override
    public JsonObject getWrittenData() {
        return this.data;
    }
}
