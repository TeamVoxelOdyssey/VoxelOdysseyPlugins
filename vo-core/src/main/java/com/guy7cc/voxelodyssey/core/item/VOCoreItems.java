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
package com.guy7cc.voxelodyssey.core.item;

import com.guy7cc.voxelodyssey.core.VOCorePlugin;
import com.guy7cc.voxelodyssey.core.registry.Registry;

/**
 * This class is responsible for managing the core items in the VoxelOdyssey game.
 * It provides a registry for VOItem objects and a method to register new items.
 */
public class VOCoreItems {
    public static final Registry<VOItem> REGISTRY;

    static {
        REGISTRY = new Registry<>(VOCorePlugin::getPlugin);
    }

    private VOCoreItems(){

    }

    private static VOItem register(VOItem object) {
        REGISTRY.register(object);
        return object;
    }
}
