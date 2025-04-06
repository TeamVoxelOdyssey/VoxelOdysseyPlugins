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
package com.guy7cc.voxelodyssey.dev.registry;

import com.guy7cc.voxelodyssey.core.entity.VOEntityType;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.registry.RegistryType;
import com.guy7cc.voxelodyssey.dev.banner.BannerFactory;

/**
 * A class that holds all the registry types in the developer tools.
 * <p>
 * This class is used to register and retrieve registry types.
 * </p>
 */
public class VODevRegistryTypes {
    public static final RegistryType<BannerFactory> BANNER = new RegistryType<>(Key.vo("banner"));
}
