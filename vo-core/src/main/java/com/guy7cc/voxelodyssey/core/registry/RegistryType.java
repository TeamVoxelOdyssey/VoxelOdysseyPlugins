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

import java.util.Objects;

/**
 * Represents a type of registry object with a unique key.
 *
 * @param <T> the type of the registry object
 */
public final class RegistryType<T> implements Keyed {
    private final Key key;

    public RegistryType(Key key){
        this.key = key;
    }

    @Override
    public Key getKey(){
        return key;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RegistryType<?> that = (RegistryType<?>) obj;
        return Objects.equals(key, that.key);
    }

    @Override
    public String toString() {
        return "RegistryType{key='" + key.toString() + "'}";
    }
}
