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
import com.guy7cc.voxelodyssey.core.registry.AbstractRegistryObject;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.registry.Key;

import java.util.function.Supplier;

/**
 * A property that can be serialized to and from JSON.
 *
 * @param <T> the type of the property
 */
public class JsonSerializableProperty<T extends JsonSerializable<T>> extends AbstractRegistryObject implements Property<T> {
    private final Supplier<T> supplier;

    public JsonSerializableProperty(Key key, Supplier<T> supplier) {
        super(key);
        this.supplier = supplier;
    }

    @Override
    public T defaultValue() {
        T value = supplier.get();
        value.initialize();
        return value;
    }

    @Override
    public JsonElement parseJson(T value) {
        return value.toJson();
    }

    @Override
    public T parseValue(JsonElement j) throws DataFormatException {
        try {
            T value = supplier.get();
            value.fromJson(j);
            return value;
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }
}
