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
package com.guy7cc.voxelodyssey.core.data;

import com.google.gson.JsonElement;

/**
 * Interface for classes that can be serialized to and from JSON.
 *
 * @param <T> the type of the object being serialized
 */
public interface JsonSerializable<T> {
    /**
     * Initializes the object.
     *
     * @return the initialized object
     */
    T initialize();

    /**
     * Converts the object to a JSON element.
     *
     * @return the JSON element representing the object
     */
    JsonElement toJson();

    /**
     * Converts a JSON element to an object.
     *
     * @param j the JSON element to convert
     * @return the object represented by the JSON element
     * @throws DataFormatException if the JSON element cannot be converted to an object
     */
    T fromJson(JsonElement j) throws DataFormatException;
}
