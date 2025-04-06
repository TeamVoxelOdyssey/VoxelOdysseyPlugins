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
package com.guy7cc.voxelodyssey.game.item;

import com.guy7cc.voxelodyssey.core.item.VOCoreItems;
import com.guy7cc.voxelodyssey.core.item.VOItem;

/**
 * A class that holds all the items in VoxelOdyssey.
 */
public class VOItems {

    private VOItems() {

    }

    private static <T extends VOItem> T register(T item) {
        VOCoreItems.REGISTRY.register(item);
        return item;
    }
}
