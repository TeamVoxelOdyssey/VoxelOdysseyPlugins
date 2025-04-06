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

import com.guy7cc.voxelodyssey.core.entity.VOEntityType;
import com.guy7cc.voxelodyssey.core.item.VOItem;
import com.guy7cc.voxelodyssey.core.property.Property;

/**
 * This class contains built-in registry types for the VoxelOdyssey game.
 */
public class BuiltInRegistryTypes {
    public static final RegistryType<VOEntityType<?, ?>> ENTITY_TYPE = new RegistryType<>(Key.vo("entity_type"));
    public static final RegistryType<VOItem> ITEM = new RegistryType<>(Key.vo("item"));
    public static final RegistryType<Property<?>> PROPERTY = new RegistryType<>(Key.vo("property"));
}
