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

import com.google.gson.JsonObject;

/**
 * Represents a data holder that can load and save data.
 */
public interface DataHolder {
    /**
     * Gets the name of the data holder.
     * This name is used as the key in the json data.
     *
     * @return the name of the data holder
     */
    String getHolderName();

    /**
     * Loads data from a JsonObject.
     *
     * @param data the JsonObject containing the data to load
     */
    void load(JsonObject data);

    /**
     * Saves data to a JsonObject.
     *
     * @return the JsonObject containing the data to save
     */
    JsonObject getWrittenData();
}
