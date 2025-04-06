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
package com.guy7cc.voxelodyssey.game.entity;

import com.guy7cc.voxelodyssey.core.VOCorePlugin;
import com.guy7cc.voxelodyssey.core.entity.VOCoreEntityTypes;
import com.guy7cc.voxelodyssey.core.entity.VOEntity;
import com.guy7cc.voxelodyssey.core.entity.VOEntityFactoryArgs;
import com.guy7cc.voxelodyssey.core.entity.VOEntityType;
import com.guy7cc.voxelodyssey.core.entity.player.NoOpVOPlayer;
import com.guy7cc.voxelodyssey.core.entity.player.VOPlayer;
import com.guy7cc.voxelodyssey.game.entity.player.VOPlayerImpl;

/**
 * A class that holds all the entity types in the game.
 * <p>
 *     This class is used to register and retrieve entity types.
 * </p>
 */
public class VOEntityTypes {
    public static final VOEntityType<VOPlayer, VOPlayer.FactoryArgs> VOPLAYER;
    public static final VOEntityType<VODamageNum, VODamageNum.FactoryArgs> DAMAGE_NUM;

    static {
        VOPLAYER = register(new VOPlayerImpl.Type());
        DAMAGE_NUM = register(new VODamageNum.Type());
    }

    private VOEntityTypes(){

    }

    private static <T extends VOEntity<?>, S extends VOEntityFactoryArgs> VOEntityType<T, S> register(VOEntityType<T, S> object) {
        VOCoreEntityTypes.REGISTRY.register(object);
        return object;
    }
}
