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
package com.guy7cc.voxelodyssey.core.registry;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A generic registry for objects that can be identified by a key.
 *
 * @param <T> the type of objects in the registry
 */
public class Registry<T extends RegistryObject> {
    private final Supplier<JavaPlugin> plugin;
    private final Map<Key, T> map = new HashMap<>(128);

    public Registry(Supplier<JavaPlugin> plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers an object in the registry.
     *
     * @param object the object to register
     * @return the registered object
     */
    public T register(T object) {
        if (map.containsKey(object.getKey()) && plugin.get() != null) {
            plugin.get().getLogger().warning(String.format("A registry object keyed by %s is already registered, overriding it", object.getKey()));
        }
        map.put(object.getKey(), object);
        return object;
    }

    /**
     * Gets an object from the registry by its key.
     *
     * @param key the key of the object to get
     * @return the object associated with the key, or null if not found
     */
    public T get(Key key) {
        return map.get(key);
    }

    /**
     * Checks if the registry contains an object with the specified key.
     *
     * @param key the key to check
     * @return true if the registry contains the key, false otherwise
     */
    public boolean containsKey(Key key) {
        return map.containsKey(key);
    }

    /**
     * Retrieves all keys in the registry.
     *
     * @return a set of all keys in the registry
     */
    public Set<Key> keySet() {
        return map.keySet();
    }

    /**
     * Retrieves all registered objects.
     *
     * @return a collection of all registered objects
     */
    public Collection<T> objects() {
        return map.values();
    }
}
