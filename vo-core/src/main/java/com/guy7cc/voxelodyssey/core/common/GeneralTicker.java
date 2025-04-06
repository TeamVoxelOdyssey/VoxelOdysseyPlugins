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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

/**
 * GeneralTicker is a class that manages a list of Tickable objects and provides a method to tick them.
 * It also collects Tickable objects from fields and static fields of a given object or class.
 */
public class GeneralTicker {
    private final JavaPlugin plugin;
    private int globalTick = 0;
    private final List<Tickable> list = new ArrayList<>();

    public GeneralTicker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a single Tickable object.
     *
     * @param tickable the Tickable object to register
     */
    public void add(Tickable tickable){
        list.add(tickable);
    }

    /**
     * Registers a collection of Tickable objects.
     *
     * @param tickables the collection of Tickable objects to register
     */
    public void add(Collection<Tickable> tickables){
        tickables.forEach(this::add);
    }

    /**
     * Collects Tickable objects from the fields of a given object and registers them.
     *
     * @param obj the object to collect Tickable objects from
     */
    public void collect(Object obj){
        java.util.List<Object> list = ReflectionUtil.collectFields(obj, o -> o instanceof Tickable, plugin.getLogger());
        add(list.stream().map(o -> (Tickable) o).toList());
    }

    /**
     * Collects static Tickable objects from the fields of a given class and registers them.
     *
     * @param clazz the class to collect static Tickable objects from
     */
    public void collectStatic(Class<?> clazz){
        java.util.List<Object> list = ReflectionUtil.collectStaticFields(clazz, o -> o instanceof Tickable, plugin.getLogger());
        add(list.stream().map(o -> (Tickable) o).toList());
    }

    /**
     * Ticks all registered Tickable objects.
     * This method should be called periodically to update the state of the Tickable objects.
     */
    public void tick() {
        for (Tickable tickable : list) {
            try{
                tickable.tick(globalTick);
            } catch(Exception e){
                plugin.getLogger().log(
                        Level.SEVERE,
                        "An error has occurred while ticking " + tickable,
                        e)
                ;
            }
        }
        ++globalTick;
    }

    /**
     * Returns the current global tick count.
     *
     * @return the current global tick count
     */
    public int getGlobalTick() {
        return globalTick;
    }
}
