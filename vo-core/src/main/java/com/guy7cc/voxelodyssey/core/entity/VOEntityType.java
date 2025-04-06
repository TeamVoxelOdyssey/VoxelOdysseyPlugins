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

import com.guy7cc.voxelodyssey.core.registry.AbstractRegistryObject;
import com.guy7cc.voxelodyssey.core.registry.Key;
import org.bukkit.entity.Entity;

/**
 * Represents a type of entity in VoxelOdyssey game.
 * This class is responsible for creating and wrapping entities of a specific type.
 *
 * @param <T> the type of entity
 * @param <S> the type of arguments used to create the entity
 */
public abstract class VOEntityType<T extends VOEntity<?>, S extends VOEntityFactoryArgs> extends AbstractRegistryObject {
    public VOEntityType(Key key) {
        super(key);
    }

    /**
     * Creates a new entity of this type with the specified arguments.
     *
     * @param args the arguments used to create the entity
     * @return the created entity
     */
    public abstract T create(S args);

    /**
     * Wraps an existing entity of this type with the specified arguments.
     *
     * @param entity the existing entity to wrap
     * @param args   the arguments used to wrap the entity
     * @return the wrapped entity
     */
    public abstract T wrap(Entity entity, S args);
}
