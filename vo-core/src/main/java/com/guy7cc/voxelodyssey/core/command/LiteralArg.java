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
import java.util.Set;
import java.util.function.Supplier;

/**
 * Represents a literal command argument.
 */
public class LiteralArg implements CommandArg<String> {
    private final String name;
    private final boolean anyMatches;
    private final Supplier<Set<String>> options;

    public LiteralArg(String name){
        this.name = name;
        this.anyMatches = true;
        this.options = Set::of;
    }

    public LiteralArg(String name, String option){
        this(name, Set.of(option));
    }

    public LiteralArg(String name, Set<String> options){
        this(name, () -> options);
    }

    public LiteralArg(String name, Supplier<Set<String>> options) {
        this.name = name;
        this.anyMatches = false;
        this.options = options;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String arg) {
        return anyMatches || options.get().contains(arg);
    }

    @Override
    public String get(String arg) {
        return arg;
    }

    @Override
    @Nullable
    public List<String> onTabComplete(String arg) {
        if(anyMatches) return List.of();
        List<String> completion = new ArrayList<>();
        org.bukkit.util.StringUtil.copyPartialMatches(arg, options.get(), completion);
        return completion;
    }
}
