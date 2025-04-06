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
package com.guy7cc.voxelodyssey.core.registry;

/**
 * Abstract class representing a registry object.
 * This class implements the RegistryObject interface and provides a common
 * implementation for the key property.
 */
public abstract class AbstractRegistryObject implements RegistryObject {
    private final Key key;

    public AbstractRegistryObject(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        AbstractRegistryObject other = (AbstractRegistryObject) obj;
        return this.key.equals(other.key);
    }
}
