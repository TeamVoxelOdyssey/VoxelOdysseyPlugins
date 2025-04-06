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

import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.registry.Registry;
import com.guy7cc.voxelodyssey.core.registry.RegistryObject;
import com.guy7cc.voxelodyssey.core.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a registry object command argument.
 *
 * @param <T> The type of the argument.
 */
public class RegistryArg<T extends RegistryObject> implements CommandArg<T>{
    private final String name;
    private final Registry<T> registry;
    private final String namespace;

    public RegistryArg(String name, Registry<T> registry, String namespace){
        this.name = name;
        this.registry = registry;
        this.namespace = namespace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String arg) {
        return options().contains(arg);
    }

    @Override
    public T get(String arg) {
        return registry.get(Key.fromString(arg));
    }

    @Override
    public @Nullable List<String> onTabComplete(String arg) {
        return StringUtil.getOptions(arg, options());
    }

    private Set<String> options(){
        Set<String> keySet;
        if(namespace.isEmpty()){
            keySet = registry.keySet()
                    .stream()
                    .map(Key::toString)
                    .collect(Collectors.toSet());
        } else {
            keySet = registry.keySet()
                    .stream()
                    .filter(k -> k.namespace().equals(namespace))
                    .map(Key::key)
                    .collect(Collectors.toSet());
        }
        return keySet;
    }
}
