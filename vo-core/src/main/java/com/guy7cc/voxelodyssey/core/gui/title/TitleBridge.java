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
package com.guy7cc.voxelodyssey.core.gui.title;

import com.guy7cc.voxelodyssey.core.common.Tickable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Interface for managing title components for a player.
 * This interface extends Tickable to allow for periodic updates.
 */
public interface TitleBridge extends Tickable {
    @Override
    default void tick(int globalTick) {
        Tickable.super.tick(globalTick);
        Collection<TitleComponent> components = getComponents();
        Component component = Component.empty();
        for (var titleComponent : getComponents()) {
            component = component.append(titleComponent.getPaperComponent());
        }
        getPlayer().showTitle(Title.title(component, Component.empty()));
        components.removeIf(TitleComponent::shouldBeRemoved);
    }

    default Collection<? extends Tickable> getTickables() {
        return getComponents();
    }

    /**
     * Gets the player associated with this TitleBridge.
     *
     * @return the player
     */
    Player getPlayer();

    /**
     * Gets the collection of title components.
     *
     * @return the collection of title components
     */
    Collection<TitleComponent> getComponents();

    /**
     * Adds a title component to the collection.
     *
     * @param component the title component to add
     */
    void addComponent(TitleComponent component);

    /**
     * Removes a title component from the collection.
     *
     * @param component the title component to remove
     */
    void removeComponent(TitleComponent component);

    /**
     * Sets the visibility status of the title.
     *
     * @param visible true to make the title visible, false to hide it
     */
    void setVisible(boolean visible);
}
