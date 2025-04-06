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

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listening to plugin lifecycle events.
 * <p>
 * This interface provides methods that can be implemented to handle plugin lifecycle events such as enabling,
 * disabling, and the first tick of the plugin.
 * </p>
 */
public interface PluginLifecycleListener {
    /**
     * Called when the plugin is enabled.
     *
     * @param plugin the JavaPlugin instance
     */
    default void onPluginEnabled(JavaPlugin plugin) {

    }

    /**
     * Called on the first tick of the plugin.
     *
     * @param plugin the JavaPlugin instance
     */
    default void onFirstTick(JavaPlugin plugin) {

    }

    /**
     * Called when the plugin is disabled.
     *
     * @param plugin the JavaPlugin instance
     */
    default void onPluginDisabled(JavaPlugin plugin) {

    }
}
