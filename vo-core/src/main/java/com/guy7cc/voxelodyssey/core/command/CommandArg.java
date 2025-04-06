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

import com.guy7cc.voxelodyssey.core.item.VOItem;
import com.guy7cc.voxelodyssey.core.registry.Registry;
import com.guy7cc.voxelodyssey.core.registry.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Represents a command argument.
 *
 * @param <T> The type of the argument.
 */
public interface CommandArg<T> {
    /**
     * Gets the name of the argument.
     *
     * @return The name of the argument.
     */
    String getName();

    /**
     * Checks if the argument matches the intended string.
     *
     * @param arg The string to check.
     * @return True if the argument matches, false otherwise.
     */
    boolean matches(String arg);

    /**
     * Gets the value of the argument from the string.
     *
     * @param arg The string to get the value from.
     * @return The value of the argument.
     */
    T get(String arg);

    /**
     * Gets the tab completion for the argument.
     *
     * @param arg The string to get the tab completion for.
     * @return The list of possible completions.
     */
    @Nullable
    List<String> onTabComplete(String arg);

    static CommandArg<String> literal(String name){
        return new LiteralArg(name);
    }

    static CommandArg<String> literal(String name, String option){
        return new LiteralArg(name, option);
    }

    static CommandArg<String> literal(String name, Set<String> options) {
        return new LiteralArg(name, options);
    }

    static CommandArg<String> literal(String name, Supplier<Set<String>> options){
        return new LiteralArg(name, options);
    }

    static CommandArg<Boolean> bool(String name){
        return new BooleanArg(name);
    }

    static CommandArg<Integer> rangedInt(String name, int min, int max) {
        return new RangedIntegerArg(name, min, max);
    }

    static CommandArg<Long> rangedLong(String name, long min, long max){
        return new RangedLongArg(name, min, max);
    }

    static CommandArg<Float> rangedFloat(String name, float min, float max){
        return new RangedFloatArg(name, min, max);
    }

    static CommandArg<Double> rangedDouble(String name, double min, double max) {
        return new RangedDoubleArg(name, min, max);
    }

    static CommandArg<VOItem> voitem(String name){
        return new VOItemArg(name);
    }

    static <T extends RegistryObject> CommandArg<T> registry(String name, Registry<T> registry, String namespace){
        return new RegistryArg<>(name, registry, namespace);
    }
}
