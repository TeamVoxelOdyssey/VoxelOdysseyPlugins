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

import com.guy7cc.voxelodyssey.core.item.VOCoreItems;
import com.guy7cc.voxelodyssey.core.item.VOItem;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a Voxel Odyssey item command argument.
 */
public class VOItemArg implements CommandArg<VOItem> {
    private final String name;

    public VOItemArg(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String arg) {
        Set<String> keySet = VOCoreItems.REGISTRY.keySet().stream().map(Key::toString).collect(Collectors.toSet());
        return keySet.contains(arg);
    }

    @Override
    public VOItem get(String arg) {
        return VOCoreItems.REGISTRY.get(Key.fromString(arg));
    }

    @Override
    public @Nullable List<String> onTabComplete(String arg) {
        return StringUtil.getOptions(arg, VOCoreItems.REGISTRY.keySet().stream().map(Key::toString).collect(Collectors.toSet()));
    }
}
