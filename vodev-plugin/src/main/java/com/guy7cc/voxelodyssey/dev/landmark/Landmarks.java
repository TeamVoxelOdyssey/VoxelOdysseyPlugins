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
package com.guy7cc.voxelodyssey.dev.landmark;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A class that manages landmarks in the world.
 */
public class Landmarks {
    private static final Map<String, Supplier<Location>> landmarks = new HashMap<>();

    static {
        landmarks.put("world1", () -> new Location(Bukkit.getWorld("world"), 52.5, 14.0, -25.5, -120f, 0f));
        landmarks.put("world1_puzzle", () -> new Location(Bukkit.getWorld("world"), 165.5, 3.0, -417.5));
        landmarks.put("world2_start", () -> new Location(Bukkit.getWorld("world"), 255.5, 11.0, -1008.5, 115f, -11f));
        landmarks.put("world2_underground", () -> new Location(Bukkit.getWorld("world"), 40.5, -34.0, -1004.5, -135, 0));
        landmarks.put("hub", () -> new Location(Bukkit.getWorld("world"), 0.5, 0.0, 1000.5, 0f, 0f));
    }

    public static Set<String> names() {
        return landmarks.keySet();
    }

    public static Location get(String name) {
        return landmarks.get(name).get();
    }
}
