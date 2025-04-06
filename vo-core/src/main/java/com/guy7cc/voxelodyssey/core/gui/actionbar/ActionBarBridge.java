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
package com.guy7cc.voxelodyssey.core.gui.actionbar;

import com.guy7cc.voxelodyssey.core.common.Tickable;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.SortedSet;

/**
 * Interface for managing action bar components.
 * This interface extends the Tickable interface to allow for periodic updates.
 */
public interface ActionBarBridge extends Tickable {
    @Override
    default void tick(int globalTick) {
        Tickable.super.tick(globalTick);
        SortedSet<ActionBarComponent> components = getComponents();
        if(getVisible()){
            for(var component : getComponents()){
                if(component.shouldSendMessage()){
                    getPlayer().sendMessage(component.getPaperComponent());
                    break;
                }
            }
        }
        components.removeIf(ActionBarComponent::shouldBeRemoved);
    }

    @Override
    default Collection<? extends Tickable> getTickables(){
        return getComponents();
    }

    /**
     * Returns the player associated with this action bar.
     *
     * @return the player
     */
    Player getPlayer();

    /**
     * Returns the components of the action bar.
     *
     * @return a sorted set of action bar components
     */
    SortedSet<ActionBarComponent> getComponents();

    /**
     * Adds a component to the action bar.
     *
     * @param component the component to add
     */
    void addComponent(ActionBarComponent component);

    /**
     * Removes a component from the action bar.
     *
     * @param component the component to remove
     */
    void removeComponent(ActionBarComponent component);

    /**
     * Checks if the action bar is visible.
     *
     * @return true if visible, false otherwise
     */
    boolean getVisible();

    /**
     * Sets the visibility of the action bar.
     *
     * @param visible true to make it visible, false to hide it
     */
    void setVisible(boolean visible);
}
