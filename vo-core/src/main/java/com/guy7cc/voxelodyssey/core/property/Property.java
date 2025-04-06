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

import com.google.gson.JsonElement;
import com.guy7cc.voxelodyssey.core.registry.RegistryObject;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;

/**
 * Interface representing a property of a certain type.
 *
 * @param <T> the type of the property
 */
public interface Property<T> extends RegistryObject {
    /**
     * Gets the default value of the property.
     *
     * @return the default value
     */
    T defaultValue();

    /**
     * Parses a value to a JSON element.
     * @param value the value to parse
     * @return the JSON element representation of the value
     */
    JsonElement parseJson(T value);

    /**
     * Parses a JSON element to a value.
     * @param j the JSON element to parse
     * @return the value representation of the JSON element
     * @throws DataFormatException if the JSON element cannot be parsed
     */
    T parseValue(JsonElement j) throws DataFormatException;
}
