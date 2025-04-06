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
package com.guy7cc.voxelodyssey.core.common;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;

/**
 * ScoreboardContainer is a singleton class that manages the creation and assignment of scoreboards to players.
 * It implements the Listener interface to handle player join and quit events.
 */
public class ScoreboardContainer implements Listener, PluginLifecycleListener {
    private final JavaPlugin plugin;
    private boolean initialized = false;
    private ScoreboardManager manager;
    private final Map<Player, Scoreboard> map = new HashMap<>();

    public ScoreboardContainer(JavaPlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Checks if the scoreboard manager is initialized.
     *
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized(){
        return initialized;
    }

    /**
     * Gets the scoreboard for a player.
     *
     * @param player the player to get the scoreboard for
     * @return the player's scoreboard, or null if not initialized
     */
    public Scoreboard getScoreboard(Player player){
        return map.get(player);
    }

    private Scoreboard register(Player player){
        if(!initialized) return null;
        Scoreboard sb = manager.getNewScoreboard();
        player.setScoreboard(sb);
        return map.put(player, sb);
    }

    @Override
    @ExecutionOrder(ExecutionOrder.Order.EARLY)
    public void onFirstTick(JavaPlugin plugin){
        if(!initialized){
            manager = Bukkit.getScoreboardManager();
            Bukkit.getOnlinePlayers().forEach(this::register);
            initialized = true;
        } else {
            plugin.getLogger().warning("Scoreboard manager was already initialized");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event){
        register(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        map.remove(event.getPlayer());
    }
}
