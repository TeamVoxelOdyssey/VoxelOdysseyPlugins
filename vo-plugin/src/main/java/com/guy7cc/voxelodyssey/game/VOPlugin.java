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
package com.guy7cc.voxelodyssey.game;

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.game.registry.VORegistryTypes;
import com.guy7cc.voxelodyssey.game.system.effect.VOEffects;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the VoxelOdyssey plugin.
 * This class is the entry point for the plugin and is responsible for loading
 * and enabling the plugin.
 */
public final class VOPlugin extends JavaPlugin {
    private static VOPlugin plugin;

    @Override
    public void onEnable(){
        plugin = this;

        VoxelOdysseyCore.addRegistry(VORegistryTypes.EFFECT, VOEffects.REGISTRY);
    }

    @Override
    public void onDisable(){

    }

    public static VOPlugin getPlugin(){
        return plugin;
    }
}
