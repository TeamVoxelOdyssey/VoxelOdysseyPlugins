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
package com.guy7cc.voxelodyssey.core.item;

import com.guy7cc.voxelodyssey.core.registry.AbstractRegistryObject;
import com.guy7cc.voxelodyssey.core.registry.Key;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a VoxelOdyssey item.
 * This class extends AbstractRegistryObject and provides methods for item interaction.
 */
public class VOItem extends AbstractRegistryObject {
    public final Material material;
    public final int loreLineNum;
    public final VOItemUsage usage;

    protected VOItem(Key key, Material material, int loreLineNum, VOItemUsage usage) {
        super(key);
        this.material = material;
        this.loreLineNum = loreLineNum;
        this.usage = usage;
    }

    /**
     * Gets the default state of the item.
     * This method creates a new ItemStack with the specified material and wraps it in a VOItemStackWrapper.
     *
     * @return the default state of the item
     */
    public VOItemStackWrapper getDefaultState() {
        ItemStack itemStack = new ItemStack(material);
        VOItemStackWrapper state = new VOItemStackWrapper(itemStack);
        state.setOwner(this);
        state.updateName();
        state.setItemLore();
        state.setModel();
        return state;
    }

    /**
     * Handles the use action on the item.
     * This method can be overridden to provide custom behavior when the item is used.
     *
     * @param state the current state of the item
     * @param player the player using the item
     * @param slot the equipment slot
     * @param action the action performed
     * @return the result of the item interaction
     */
    public VOItemInteractionResult use(VOItemStackWrapper state, Player player, EquipmentSlot slot, Action action) {
        return VOItemInteractionResult.UNAVAILABLE;
    }

    /**
     * Handles the use action on an entity with the item.
     * This method can be overridden to provide custom behavior when the item is used on an entity.
     *
     * @param state the current state of the item
     * @param player the player using the item
     * @param slot the equipment slot
     * @param entity the entity being interacted with
     * @return the result of the item interaction
     */
    public VOItemInteractionResult useOnEntity(VOItemStackWrapper state, Player player, EquipmentSlot slot, Entity entity) {
        return VOItemInteractionResult.UNAVAILABLE;
    }

    /**
     * Handles the action when the item is set to the inventory.
     * This method can be overridden to provide custom behavior when the item is set to the inventory.
     *
     * @param state the current state of the item
     * @param player the player setting the item
     * @param slot the equipment slot
     */
    public void onSetToInventory(VOItemStackWrapper state, Player player, int slot) {

    }

    /**
     * Handles the action on tick in the equipment slot.
     * This method can be overridden to provide custom behavior when the item is ticked in the equipment slot.
     *
     * @param state the current state of the item
     * @param player the player holding the item
     * @param slot the equipment slot
     */
    public void tickInEquipmentSlot(VOItemStackWrapper state, Player player, EquipmentSlot slot) {

    }

    /**
     * Handles the action on tick in the quick bar.
     * This method can be overridden to provide custom behavior when the item is ticked in the quick bar.
     *
     * @param state the current state of the item
     * @param player the player holding the item
     * @param index the index of the quick bar slot
     */
    public void tickInQuickBar(VOItemStackWrapper state, Player player, int index) {

    }

    /**
     * Handles the action when the inventory is changed.
     * This method can be overridden to provide custom behavior when the inventory is changed.
     *
     * @param player the player whose inventory has changed
     */
    public void onInventoryChanged(Player player) {

    }
}
