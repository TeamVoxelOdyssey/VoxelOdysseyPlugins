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
package com.guy7cc.voxelodyssey.core.property;

import com.guy7cc.voxelodyssey.core.registry.AbstractRegistryObject;
import com.guy7cc.voxelodyssey.core.registry.Key;

/**
 * Abstract class representing a property with a default value.
 *
 * @param <T> the type of the property value
 */
public abstract class AbstractProperty<T> extends AbstractRegistryObject implements Property<T> {
    private final T defaultValue;

    public AbstractProperty(Key key, T defaultValue) {
        super(key);
        this.defaultValue = defaultValue;
    }

    public final T defaultValue() {
        return defaultValue;
    }
}
