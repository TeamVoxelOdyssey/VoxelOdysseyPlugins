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
package com.guy7cc.voxelodyssey.game.system;

/**
 * An enum representing the different elements in VoxelOdyssey.
 */
public enum VOElement {
    BASE(0xFFFFFF),
    FLAME(0xFF5555),
    FROST(0x55FFFF),
    LIGHTNING(0xFFFF55),
    VENOM(0x55FF55),
    WITHER(0xAAAAAA),
    PHANTOM(0x9370DB),
    DIVINE(0xFFAA00),
    ASTRAL(0x7FFFD4);

    public final int color;

    VOElement(int color) {
        this.color = color;
    }
}
