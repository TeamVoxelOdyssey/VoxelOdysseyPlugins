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

import com.guy7cc.voxelodyssey.core.VOCorePlugin;
import com.guy7cc.voxelodyssey.core.entity.player.NoOpVOPlayer;
import com.guy7cc.voxelodyssey.core.entity.player.VOPlayer;
import com.guy7cc.voxelodyssey.core.registry.Registry;

/**
 * This class is responsible for registering and managing entity types in the VoxelOdyssey game.
 * It provides a static registry for all entity types, including the player entity type.
 */
public class VOCoreEntityTypes {
    public static final Registry<VOEntityType<?, ?>> REGISTRY;

    public static final VOEntityType<VOPlayer, VOPlayer.FactoryArgs> VOPLAYER;

    static {
        REGISTRY = new Registry<>(VOCorePlugin::getPlugin);

        VOPLAYER = register(new NoOpVOPlayer.Type());
    }

    private VOCoreEntityTypes(){

    }

    private static <T extends VOEntity<?>, S extends VOEntityFactoryArgs> VOEntityType<T, S> register(VOEntityType<T, S> object) {
        REGISTRY.register(object);
        return object;
    }
}
