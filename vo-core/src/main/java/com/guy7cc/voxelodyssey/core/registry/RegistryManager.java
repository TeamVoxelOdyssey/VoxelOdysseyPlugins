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

import com.guy7cc.voxelodyssey.core.common.ExecutionOrder;
import com.guy7cc.voxelodyssey.core.common.PluginLifecycleListener;
import com.guy7cc.voxelodyssey.core.entity.VOCoreEntityTypes;
import com.guy7cc.voxelodyssey.core.item.VOCoreItems;
import com.guy7cc.voxelodyssey.core.property.VOCoreProperties;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * RegistryManager is responsible for managing and registering different types of registries.
 * It allows adding and retrieving registries based on their type.
 */
public class RegistryManager implements PluginLifecycleListener {
    private final JavaPlugin plugin;
    private final Map<RegistryType<?>, Registry<?>> registries = new HashMap<>(16);

    public RegistryManager(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    public <T extends RegistryObject> Registry<T> add(RegistryType<T> type, Registry<T> registry){
        if(registries.containsKey(type)) {
            plugin.getLogger().warning("Registry " + type.getKey() + " is already registered, ignoring it");
            return (Registry<T>) registries.get(type);
        }
        return (Registry<T>) registries.put(type, registry);
    }

    @SuppressWarnings("unchecked")
    public <T extends RegistryObject> Registry<T> get(RegistryType<T> type){
        return (Registry<T>) registries.get(type);
    }

    @Override
    @ExecutionOrder(ExecutionOrder.Order.MIDDLE)
    public void onPluginEnabled(JavaPlugin plugin) {
        add(BuiltInRegistryTypes.ENTITY_TYPE, VOCoreEntityTypes.REGISTRY);
        add(BuiltInRegistryTypes.ITEM, VOCoreItems.REGISTRY);
        add(BuiltInRegistryTypes.PROPERTY, VOCoreProperties.REGISTRY);
    }

    public class Registrar implements PluginLifecycleListener {
        private final Map<RegistryType<?>, Registry<?>> registries;

        public Registrar(Map<RegistryType<?>, Registry<?>> registries){
            this.registries = registries;
        }

        @Override
        @ExecutionOrder(ExecutionOrder.Order.MIDDLE)
        public void onPluginEnabled(JavaPlugin plugin) {
            RegistryManager.this.registries.putAll(registries);
        }
    }
}
