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
package com.guy7cc.voxelodyssey.dev;

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the VoxelOdysseyDeveloperTools plugin.
 * <p>
 *     This class is the entry point for the plugin and is responsible for loading
 *     and enabling the plugin.
 * </p>
 */
public final class VODevPlugin extends JavaPlugin {
    private static VODevPlugin plugin;

    @Override
    public void onLoad(){
        plugin = this;

    }

    @Override
    public void onEnable() {
        VoxelOdysseyDeveloperTools.setup(this);

        VoxelOdysseyDeveloperTools.getLifecycleHandler().onPluginEnabled();

        Bukkit.getScheduler().runTask(this, VoxelOdysseyDeveloperTools.getLifecycleHandler()::onFirstTick);

        Bukkit.getScheduler().runTaskTimer(this, VoxelOdysseyDeveloperTools.getTicker()::tick, 1L, 1L);
    }

    @Override
    public void onDisable(){
        VoxelOdysseyDeveloperTools.getLifecycleHandler().onPluginDisabled();
    }

    public static VODevPlugin getPlugin(){
        return plugin;
    }
}
