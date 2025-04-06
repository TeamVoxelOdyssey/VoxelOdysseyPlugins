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
package com.guy7cc.voxelodyssey.core.command;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a ranged long command argument.
 */
public class RangedLongArg implements CommandArg<Long> {
    private final String name;
    private final long min;
    private final long max;

    public RangedLongArg(String name, long min, long max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String arg) {
        try {
            int value = Integer.parseInt(arg);
            return min <= value && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Long get(String arg) {
        try {
            long value = Long.parseLong(arg);
            if (min <= value && value <= max) return value;
            return 0L;
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(String arg) {
        if (arg.isEmpty()) {
            return List.of(String.valueOf(min), String.valueOf(max));
        } else return List.of();
    }
}
