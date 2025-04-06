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
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a component that can be displayed in the action bar.
 * This interface extends the Tickable interface and provides methods to get the rank,
 * paper component, and whether the message should be sent or removed.
 */
public interface ActionBarComponent extends Tickable, Comparable<ActionBarComponent> {
    @Override
    default int compareTo(@NotNull ActionBarComponent o){
        return o.getRank() - this.getRank();
    }

    /**
     * Gets the rank of the component.
     * The rank determines the order in which components are displayed.
     *
     * @return the rank of the component
     */
    int getRank();

    /**
     * Gets the paper component associated with this action bar component.
     *
     * @return the paper component
     */
    Component getPaperComponent();

    /**
     * Checks if the message should be sent.
     *
     * @return
     */
    boolean shouldSendMessage();

    /**
     * Checks if the component should be removed.
     * This method can be overridden to provide custom removal logic.
     *
     * @return true if the component should be removed, false otherwise
     */
    boolean shouldBeRemoved();
}
