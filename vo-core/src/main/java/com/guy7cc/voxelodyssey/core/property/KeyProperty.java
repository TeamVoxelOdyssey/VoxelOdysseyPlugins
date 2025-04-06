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
import com.google.gson.JsonPrimitive;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.registry.Key;

/**
 * A property that represents a Key.
 * It extends the AbstractProperty class and provides methods to parse JSON elements
 * and convert them to Key objects, as well as to convert Key objects to JSON elements.
 */
public class KeyProperty extends AbstractProperty<Key> {
    public KeyProperty(Key key, Key defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public JsonElement parseJson(Key value) {
        return new JsonPrimitive(value.toString());
    }

    @Override
    public Key parseValue(JsonElement j) throws DataFormatException {
        try {
            return Key.fromString(j.getAsString());
        } catch (Exception exception) {
            throw new DataFormatException(getClass(), exception);
        }
    }
}