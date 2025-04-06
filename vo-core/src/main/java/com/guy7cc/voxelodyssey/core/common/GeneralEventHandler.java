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
package com.guy7cc.voxelodyssey.core.common;

import com.guy7cc.voxelodyssey.core.util.ReflectionUtil;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;

/**
 * A utility class for handling Bukkit events.
 * It provides methods to register event listeners and collect them from objects.
 */
public class GeneralEventHandler {
    private final JavaPlugin plugin;

    public GeneralEventHandler(JavaPlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Registers a single event listener.
     *
     * @param listener the event listener to register
     */
    public void add(Listener listener){
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(listener, plugin);
    }

    /**
     * Registers a collection of event listeners.
     *
     * @param listeners the collection of event listeners to register
     */
    public void add(Collection<Listener> listeners){
        listeners.forEach(this::add);
    }

    /**
     * Collects event listeners from the fields of an object and registers them.
     *
     * @param obj the object to collect event listeners from
     */
    public void collect(Object obj){
        List<Object> list = ReflectionUtil.collectFields(obj, o -> o instanceof Listener, plugin.getLogger());
        add(list.stream().map(o -> (Listener) o).toList());
    }

    /**
     * Collects static event listeners from the fields of a class and registers them.
     *
     * @param clazz the class to collect static event listeners from
     */
    public void collectStatic(Class<?> clazz){
        List<Object> list = ReflectionUtil.collectStaticFields(clazz, o -> o instanceof Listener, plugin.getLogger());
        add(list.stream().map(o -> (Listener) o).toList());
    }
}
