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
 * Represents a property that holds a Double value.
 * This class extends AbstractProperty and provides methods to parse JSON elements
 * and convert them to Double values.
 */
public class DoubleProperty extends AbstractProperty<Double> {
    public DoubleProperty(Key key, Double defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public JsonElement parseJson(Double value) {
        return new JsonPrimitive(value);
    }

    @Override
    public Double parseValue(JsonElement j) throws DataFormatException {
        try {
            return j.getAsDouble();
        } catch (Exception exception) {
            throw new DataFormatException(getClass(), exception);
        }
    }
}
