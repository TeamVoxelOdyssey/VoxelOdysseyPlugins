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
package com.guy7cc.voxelodyssey.core.entity.player;

import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import com.guy7cc.voxelodyssey.core.entity.VOEntity;
import com.guy7cc.voxelodyssey.core.entity.VOEntityFactoryArgs;
import org.bukkit.entity.Player;

/**
 * Represents a player in the VoxelOdyssey game.
 * This interface extends VOEntity and JsonSerializable to provide additional functionality.
 */
public interface VOPlayer extends VOEntity<Player>, JsonSerializable<VOPlayer> {
    /**
     * Represents the arguments used to create a VOPlayer instance.
     * This class extends VOEntityFactoryArgs but includes no additional arguments.
     */
    class FactoryArgs extends VOEntityFactoryArgs {

    }
}
