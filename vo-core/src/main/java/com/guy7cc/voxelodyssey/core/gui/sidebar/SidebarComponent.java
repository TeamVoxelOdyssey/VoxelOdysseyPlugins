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

import java.util.Collection;

/**
 * Represents a component of a sidebar in the VoxelOdyssey game.
 * This interface extends Tickable to provide functionality for updating the component.
 */
public interface SidebarComponent extends Tickable {
    /**
     * Gets the containers that this component is part of.
     *
     * @return a collection of SidebarBridge objects representing the containers
     */
    Collection<SidebarBridge> getContainers();

    /**
     * Adds a container to this component.
     *
     * @param bridge the SidebarBridge object representing the container to add
     */
    void addContainer(SidebarBridge bridge);

    /**
     * Removes a container from this component.
     *
     * @param bridge the SidebarBridge object representing the container to remove
     */
    void removeContainer(SidebarBridge bridge);

    /**
     * Gets the text associated with this component.
     *
     * @return the text of this component
     */
    String getText();

    /**
     * Sets the text for this component.
     *
     * @param text the new text for this component
     */
    void setText(String text);

    /**
     * Refreshes the component in all containers.
     * This method is called to update the component's state in all associated containers.
     */
    default void refresh() {
        for (SidebarBridge bridge : getContainers()) {
            bridge.refreshComponent(this);
        }
    }

    /**
     * Gets the score associated with this component.
     *
     * @return the score of this component
     */
    int getScore();

    /**
     * Sets the score for this component.
     *
     * @param score the new score for this component
     */
    void setScore(int score);

    /**
     * Checks if this component should be removed.
     *
     * @return true if the component should be removed, false otherwise
     */
    boolean shouldBeRemoved();
}
