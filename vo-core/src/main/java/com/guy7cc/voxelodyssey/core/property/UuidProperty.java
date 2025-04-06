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
import org.bukkit.NamespacedKey;

import java.util.UUID;

/**
 * A property that represents a UUID.
 * It implements the Property interface and provides methods to parse and format UUID values.
 */
public class UuidProperty implements Property<UUID> {
    private final Key key;

    public UuidProperty(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public UUID defaultValue() {
        return UUID.randomUUID();
    }

    @Override
    public JsonElement parseJson(UUID value) {
        return new JsonPrimitive(value.toString());
    }

    @Override
    public UUID parseValue(JsonElement j) throws DataFormatException {
        try {
            return UUID.fromString(j.getAsString());
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }
}
