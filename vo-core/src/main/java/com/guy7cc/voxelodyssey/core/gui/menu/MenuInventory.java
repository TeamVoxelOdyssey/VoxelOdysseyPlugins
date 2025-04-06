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

import com.guy7cc.voxelodyssey.core.common.AbstractWrapper;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Represents a menu inventory in the game.
 * This class is responsible for creating and managing the inventory.
 */
public class MenuInventory extends AbstractWrapper<Inventory> {
    private MenuInventoryProperty property;

    public MenuInventory(MenuInventoryProperty property) {
        this.property = property;
        handle = Bukkit.createInventory(null, property.lineNum, property.title);
        for (int i = 0; i < property.lineNum * 9; i++) {
            if (property.components[i] != null) {
                handle.setItem(i, property.components[i].getDefaultItemStack());
            }
        }
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (property.components[event.getSlot()] != null) {
            property.components[event.getSlot()].onInventoryClick(event);
        } else {
            event.setCancelled(true);
        }
    }
}
