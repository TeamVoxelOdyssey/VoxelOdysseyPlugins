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
package com.guy7cc.voxelodyssey.core.gui.menu;

import net.kyori.adventure.text.Component;

/**
 * Represents the properties of a menu inventory.
 * This includes the number of lines, title, and components.
 */
public class MenuInventoryProperty {
    public final int lineNum;
    public final Component title;
    public final MenuComponent[] components;

    public MenuInventoryProperty(int lineNum, Component title) {
        if (lineNum <= 0 || lineNum > 6) throw new IllegalArgumentException("Menu inventory's line num must be from 1 to 6");
        this.lineNum = lineNum;
        this.title = title;
        this.components = new MenuComponent[lineNum * 9];
    }

    public MenuInventoryProperty setComponent(int index, MenuComponent component) {
        if (0 <= index && index < components.length) {
            components[index] = component;
        }
        return this;
    }
}
