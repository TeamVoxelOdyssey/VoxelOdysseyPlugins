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
package com.guy7cc.voxelodyssey.core.gui.sidebar;

import com.guy7cc.voxelodyssey.core.common.Tickable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Interface representing a bridge for a sidebar in the VoxelOdyssey game.
 * This interface extends Tickable to provide tick functionality.
 */
public interface SidebarBridge extends Tickable {
    @Override
    default void tick(int globalTick){
        Tickable.super.tick(globalTick);
        getComponents().removeIf(SidebarComponent::shouldBeRemoved);
    }

    @Override
    default Collection<? extends Tickable> getTickables() {
        return getComponents();
    }

    /**
     * Returns the player associated with this sidebar.
     *
     * @return the player associated with this sidebar
     */
    Player getPlayer();

    /**
     * Returns the components of this sidebar.
     *
     * @return a collection of components in this sidebar
     */
    Collection<SidebarComponent> getComponents();

    /**
     * Adds a component to this sidebar.
     *
     * @param component the component to add
     */
    void addComponent(SidebarComponent component);

    /**
     * Sets a component at the specified index in this sidebar.
     *
     * @param component the component to set
     * @param index the index at which to set the component
     */
    void setComponent(SidebarComponent component, int index);

    /**
     * Removes a component from this sidebar.
     *
     * @param component the component to remove
     */
    void removeComponent(SidebarComponent component);

    /**
     * Refreshes the specified component in this sidebar.
     *
     * @param component the component to refresh
     */
    void refreshComponent(SidebarComponent component);

    /**
     * Refreshes all components in this sidebar.
     */
    default void refreshAll() {
        getComponents().forEach(this::refreshComponent);
    }

    /**
     * Sets the display name of this sidebar.
     *
     * @param display the display name to set
     */
    void setDisplay(Component display);

    /**
     * Sets the visibility of this sidebar.
     *
     * @param visible true to set the sidebar visible, false to hide it
     */
    void setVisible(Boolean visible);

    /**
     * Disposes of this sidebar, cleaning up any resources and removing it from the player.
     */
    void dispose();
}
