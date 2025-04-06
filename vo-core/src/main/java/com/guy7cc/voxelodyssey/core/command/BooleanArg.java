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
 * Represents a boolean command argument.
 */
public class BooleanArg implements CommandArg<Boolean> {
    private final String name;

    public BooleanArg(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String arg) {
        return arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false");
    }

    @Override
    public Boolean get(String arg) {
        return arg.equalsIgnoreCase("true");
    }

    @Override
    public @Nullable List<String> onTabComplete(String arg) {
        List<String> completion = new ArrayList<>();
        org.bukkit.util.StringUtil.copyPartialMatches(arg, List.of("true", "false"), completion);
        return completion;
    }
}
