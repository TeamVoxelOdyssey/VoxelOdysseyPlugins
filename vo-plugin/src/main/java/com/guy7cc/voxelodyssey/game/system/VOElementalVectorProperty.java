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
package com.guy7cc.voxelodyssey.game.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.property.AbstractProperty;
import com.guy7cc.voxelodyssey.core.registry.Key;

/**
 * A property for the {@link VOElementalVector} class.
 */
public class VOElementalVectorProperty extends AbstractProperty<VOElementalVector> {
    public VOElementalVectorProperty(Key key, VOElementalVector defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public JsonElement parseJson(VOElementalVector value) {
        return value.toJson();
    }

    @Override
    public VOElementalVector parseValue(JsonElement j) throws DataFormatException {
        try {
            VOElementalVector v = new VOElementalVector();
            v.fromJson((JsonArray) j);
            return v;
        } catch (Exception exception) {
            throw new DataFormatException(getClass(), exception);
        }
    }
}
