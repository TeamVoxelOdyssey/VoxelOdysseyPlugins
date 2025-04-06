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

/**
 * Exception thrown when there is a data format error.
 * This can occur when loading data from JSON or other formats.
 */
public class DataFormatException extends Exception {
    public DataFormatException(String message) {
        super(message);
    }

    public DataFormatException(String objName, Exception base) {
        super(String.format("An error has occurred while loading %s from json", objName), base);
    }

    public DataFormatException(Class<?> clazz, Exception base) {
        this(clazz.getName(), base);
    }
}
