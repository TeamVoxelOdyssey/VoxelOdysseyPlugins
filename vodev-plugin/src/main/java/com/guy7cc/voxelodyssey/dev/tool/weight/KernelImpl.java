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
package com.guy7cc.voxelodyssey.dev.tool.weight;

/**
 * A kernel implementation for use in terrain management.
 * @param <T>
 */
public class KernelImpl<T> implements Kernel<T> {
    protected final int size;
    protected final int diameter;
    protected final Object[][] map;

    public KernelImpl(int size, T defaultValue) {
        if (size <= 0) throw new IllegalArgumentException("Kernel size must be more than 0");
        this.size = size;
        this.diameter = 2 * size + 1;
        this.map = new Object[diameter][];
        for (int i = 0; i < diameter; i++) {
            this.map[i] = new Object[diameter];
            for (int j = 0; j < diameter; j++) {
                this.map[i][j] = defaultValue;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int i, int j) {
        return (T) this.map[Math.clamp(i, -size, size) + size][Math.clamp(j, -size, size) + size];
    }

    @Override
    public int getDiameter() {
        return diameter;
    }

    public void set(int i, int j, T value) {
        this.map[Math.clamp(i, -size, size) + size][Math.clamp(j, -size, size) + size] = value;
    }
}
