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

import com.guy7cc.voxelodyssey.core.common.Tickable;
import com.guy7cc.voxelodyssey.core.common.Wrapper;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;

/**
 * Represents a VoxelOdyssey entity.
 * This interface extends the Wrapper interface and provides methods for tick updates and event handling.
 *
 * @param <T> the type of the entity being wrapped
 */
public interface VOEntity<T extends Entity> extends Wrapper<T>, Tickable, Listener {
    /**
     * Checks if the entity should be removed.
     * This method can be overridden to provide custom removal logic.
     *
     * @return true if the entity should be removed, false otherwise
     */
    default boolean shouldBeRemoved(){
        return false;
    }
}
