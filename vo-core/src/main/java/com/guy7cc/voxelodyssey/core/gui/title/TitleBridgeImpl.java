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

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the TitleBridge interface.
 * This class manages the title components for a player.
 */
public class TitleBridgeImpl implements TitleBridge {
    private final Player player;
    private final Set<TitleComponent> components = new HashSet<>();
    private boolean visible;

    public TitleBridgeImpl(Player player, boolean visible){
        this.player = player;
        this.visible = visible;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Collection<TitleComponent> getComponents() {
        return components;
    }

    @Override
    public void addComponent(TitleComponent component) {
        components.add(component);
    }

    @Override
    public void removeComponent(TitleComponent component) {
        components.remove(component);
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
